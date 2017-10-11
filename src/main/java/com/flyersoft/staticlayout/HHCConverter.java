package com.flyersoft.staticlayout;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class HHCConverter {
    static final String ICON_CLOSE_FOLDER = "as/cf.png";
    static final String ICON_FILE = "as/f.png";
    private static int count = 0;
    public static String fileHHCTemplate = "%s";
    public static final String key_local = "Local";
    public static final String key_name = "Name";
    private static Logger logger = Logger.getLogger("HHCConverter");
    public static final String object = "object";
    public static final String object_e = "/object";
    public static final String param = "param";
    public static final String ul = "ul";
    public static final String ul_e = "/ul";
    public static final String value_deli = "/> \t\r\n";

    public enum KeyType {
        unknown,
        name,
        value
    }

    public enum NameType {
        unknown,
        name,
        local
    }

    private static class ParsingContext {
        public CharArray caTemp;
        public CharArray caTempValue;
        public CharArray caWriteBuffer;
        public CharArray fileName;
        public List<HHC.IndexEntry> hhcIndex;
        public int id;
        public CharArray name;
        public FileChannel outChannel;

        private ParsingContext() {
            this.fileName = new CharArray();
            this.name = new CharArray();
            this.hhcIndex = new ArrayList(1024);
            this.caTemp = new CharArray();
            this.caTempValue = new CharArray();
            this.caWriteBuffer = new CharArray(4096);
        }
    }

    enum State {
        begin,
        ul,
        object
    }

    public static List<HHC.IndexEntry> convertToTree(MyBufferedReader in, OutputStream fout, ArrayList<HHC.HChapter> chapters) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(fout), "UTF-8");
        int p = fileHHCTemplate.indexOf("%s");
        String prefix = fileHHCTemplate.substring(0, p);
        String postfix = fileHHCTemplate.substring(p + 2);
        out.write(prefix);
        State state = State.begin;
        ParsingContext ctx = new ParsingContext();
        if (fout instanceof FileOutputStream) {
            ctx.outChannel = ((FileOutputStream) fout).getChannel();
        }
        logger.logTimeStart("convertToTree: start");
        count = 0;
        int indent = 0;
        while (in.readNextXMLTag(ctx.caTemp) > 0) {
            count++;
            if (count % 10000 == 0) {
                logger.logTime("convertToTree: converted node:" + count);
            }
            ctx.caTemp.toLowerCase();
            switch (state) {
                case begin:
                    if (!ctx.caTemp.equals(ul)) {
                        break;
                    }
                    state = State.ul;
                    break;
                case ul:
                    if (!ctx.caTemp.equals(object)) {
                        if (!ctx.caTemp.equals(ul)) {
                            if (!ctx.caTemp.equals(ul_e)) {
                                break;
                            }
                            writeNode(false, out, ctx, chapters, indent);
                            indent--;
                            out.write("</div>");
                            state = State.ul;
                            break;
                        }
                        writeNode(true, out, ctx, chapters, indent);
                        indent++;
                        state = State.ul;
                        break;
                    }
                    writeNode(false, out, ctx, chapters, indent);
                    state = State.object;
                    break;
                case object:
                    if (!ctx.caTemp.equals(param)) {
                        if (!ctx.caTemp.equals(object_e)) {
                            break;
                        }
                        state = State.ul;
                        break;
                    }
                    readAttributes(in, ctx);
                    break;
                default:
                    break;
            }
        }
        out.write(postfix);
        out.flush();
        logger.logTime("convertToTree: finish");
        return ctx.hhcIndex;
    }

    public static void readAttributes(MyBufferedReader in, ParsingContext ctx) throws IOException {
        KeyType currentKeyType = KeyType.unknown;
        NameType currentNameType = NameType.unknown;
        while (true) {
            in.skipSpace();
            int c = in.peek();
            if (c == 47 || c == 62) {
                switch (currentNameType) {
                    case name:
                        ctx.name.copyFrom(ctx.caTempValue);
                        return;
                    case local:
                        ctx.fileName.copyFrom(ctx.caTempValue);
                        ctx.fileName = ctx.fileName.replace("%20", " ");
                        return;
                    default:
                        return;
                }
            }
            in.readUntil("=", ctx.caTemp);
            ctx.caTemp.toLowerCase();
            if (ctx.caTemp.equals("name")) {
                currentKeyType = KeyType.name;
            } else if (ctx.caTemp.equals("value")) {
                currentKeyType = KeyType.value;
            }
            in.read();
            c = in.peek();
            if (c == 39) {
                in.read();
                in.readUntil('\'', ctx.caTemp);
                in.read();

            } else if (c == 34) {
                in.read();
                in.readUntil('\"', ctx.caTemp);
                in.read();
            } else {
                in.readUntil(value_deli, ctx.caTemp);
            }
            switch (currentKeyType) {
                case name:
                    if (!ctx.caTemp.equals(key_name)) {
                        if (ctx.caTemp.equals(key_local)) {
                            currentNameType = NameType.local;
                            break;
                        }
                    }
                    currentNameType = NameType.name;
                    break;
                case value:
                    break;
                default:
                    continue;
            }
            ctx.caTempValue.copyFrom(ctx.caTemp);
        }
    }

    private static void writeNode(boolean hasChildren, Writer out, ParsingContext ctx, ArrayList<HHC.HChapter> chapters, int indent) throws IOException {
        if (ctx.name.len != 0) {
            HHC.IndexEntry ie = new HHC.IndexEntry();
            ie.pathHash = ctx.fileName.hashCode();
            ctx.id = ctx.hhcIndex.size();
            ctx.hhcIndex.add(ie);
            HHC.HChapter chapter = new HHC.HChapter();
            chapters.add(chapter);
            chapter.hasSubChapter = hasChildren;
            chapter.name = ctx.name.toString();
            chapter.indent = indent;
            String icon = hasChildren ? ICON_CLOSE_FOLDER : ICON_FILE;
            String idStr = String.valueOf(ctx.id);
            if (ctx.fileName.len == 0) {
                ctx.caWriteBuffer.clear().a("<a name=").a(idStr).a(" /><div id=n_").a(idStr).a("><img id=ic_").a(idStr).a(" class=ic src=").a(icon).a(" onclick=tn(").a(idStr).a(") />").a(ctx.name).a("</div>\n");
                out.write(ctx.caWriteBuffer.buffer, 0, ctx.caWriteBuffer.len);
            } else {
                char fileNameQuote = '\'';
                if (ctx.fileName.indexOf('\'') >= 0) {
                    fileNameQuote = '\"';
                }
                ctx.caWriteBuffer.clear().a("<a name=").a(idStr).a(" /><div id=n_").a(idStr).a("><img id=ic_").a(idStr).a(" class=ic src=").a(icon).a(" onclick=tn(").a(idStr).a(") /><a href=").a(fileNameQuote);
                out.write(ctx.caWriteBuffer.buffer, 0, ctx.caWriteBuffer.len);
                out.flush();
                ie.position = ctx.outChannel.position();
                ctx.caWriteBuffer.clear().a(ctx.fileName).a(fileNameQuote).a(">").a(ctx.name).a("</a></div>\n");
                out.write(ctx.caWriteBuffer.buffer, 0, ctx.caWriteBuffer.len);
                chapter.filename = ctx.fileName.toString();
            }
            if (hasChildren) {
                ctx.caWriteBuffer.clear().a("<div id=c_").a(idStr).a(" class=c style=display:none;>\n");
                out.write(ctx.caWriteBuffer.buffer, 0, ctx.caWriteBuffer.len);
            }
            ctx.fileName.clear();
            ctx.name.clear();
        }
    }
}