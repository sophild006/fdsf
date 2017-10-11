package com.flyersoft.staticlayout;

import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.MetricAffectingSpan;
import android.text.style.ParagraphStyle;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import com.flyersoft.books.A;
import com.flyersoft.books.BaseEBook.Chapter;
import com.flyersoft.books.T;
import com.flyersoft.components.CSS;
import com.flyersoft.components.CSS.BackgroundColorSpan;
import com.flyersoft.components.CSS.PAGE_BREAK;
import com.flyersoft.components.CSS.Style;
import com.flyersoft.moonreader.ActivityTxt;
import com.flyersoft.staticlayout.AlignmentSpan.Standard;
import com.flyersoft.staticlayout.MyHtml.Emphasis;
import com.flyersoft.staticlayout.MyHtml.MyImageGetter;
import com.flyersoft.staticlayout.MyHtml.Ruby;
import com.flyersoft.staticlayout.MyHtml.TagHandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/* compiled from: MyHtml */
class HtmlToSpannedConverter implements ContentHandler {
    private static final float[] HEADER_SIZES = new float[]{2.0f, 1.5f, 1.17f, 1.0f, 0.83f, 0.75f};
    boolean LI_statement;
    ArrayList<Style> alignTag;
    ArrayList<Style> backgroundColorTag;
    ArrayList<Integer> backgroundColorValue;
    ArrayList<Style> boldTag;
    private int chapterId;
    ArrayList<String> classes = new ArrayList();
    ArrayList<Style> cssStyles = new ArrayList();
    CharSequence deletedFloatContent;
    ArrayList<Style> emphasisTag;
    int floatSpanType = 0;
    ArrayList<Style> floatTag;
    ArrayList<Style> fontSizeTag;
    ArrayList<Style> fontTag;
    private boolean hasBr = false;
    boolean headerStart;
    ArrayList<Style> hideTag;
    boolean ignoreStart;
    ArrayList<Style> italicTag;
    private String lastEndTag;
    String li_tag;
    ArrayList<Style> lightTag;
    private MyImageGetter mMyImageGetter;
    private XMLReader mReader;
    private String mSource;
    private TagHandler mTagHandler;
    ArrayList<Style> marginTag;
    boolean mediaTagStart;
    int ol_value;
    boolean preTagStart;
    boolean rubyRbStart;
    boolean rubyRtStart;
    String rubyRtText;
    ArrayList<Style> shadowTag;
    private SpannableStringBuilder spanBulder;
    ArrayList<Style> strikethroughTag;
    ArrayList<String> tableHtmls;
    int tableIndexInHtml = 0;
    int tableLevel = 0;
    ArrayList<TableElement> tableList;
    MyTableSpan tableSp;
    int tableStart = -1;
    ArrayList<String> tags = new ArrayList();
    private int td_start;
    ArrayList<Style> textFillColorTag;
    int ul_level;
    ArrayList<Style> underlineTag;

    /* compiled from: MyHtml */
    private static class Alignment {
        int align;

        public Alignment(int align) {
            this.align = align;
        }
    }

    /* compiled from: MyHtml */
    private static class BackgroundColor {
        private BackgroundColor() {
        }
    }

    /* compiled from: MyHtml */
    private static class Big {
        boolean inherited = true;
        float size = 1.25f;

        public Big(String tag, Style style) {
            if (style != null && style.fontSize > 0.0f) {
                this.size = style.fontSize;
                this.inherited = style.fontSizeInherited;
            } else if (CSS.isHeaderTag(tag)) {
                this.size = HtmlToSpannedConverter.HEADER_SIZES[tag.charAt(1) - 49];
            }
        }
    }

    /* compiled from: MyHtml */
    private static class BlockQuote {
        Style style;

        public BlockQuote(Style style) {
            this.style = style;
        }
    }

    /* compiled from: MyHtml */
    private static class Bold {
        private Bold() {
        }
    }

    /* compiled from: MyHtml */
    private static class Emphasis_Tag {
        public String emphasis;

        public Emphasis_Tag(String emphasis) {
            this.emphasis = emphasis;
        }
    }

    /* compiled from: MyHtml */
    private static class FloatQuote {
        Attributes attributes;
        Style style;
        String tag;

        public FloatQuote(String tag, Style style) {
            this.tag = tag;
            this.style = style;
        }
    }

    /* compiled from: MyHtml */
    private static class Font {
        public boolean fontSize_inherited;
        public boolean has_fill_color;
        public String mColor;
        public String mFace;
        public float mSize;

        public Font(String color, String face, float size, boolean inherited) {
            this.mColor = color;
            this.mFace = face;
            this.mSize = size;
            this.fontSize_inherited = inherited;
        }
    }

    /* compiled from: MyHtml */
    private static class FontQuote {
        Style style;
        String tag;

        public FontQuote(String tag, Style style) {
            this.tag = tag;
            this.style = style;
        }
    }

    /* compiled from: MyHtml */
    private static class Header {
        private float fontSize;
        Style style;

        public Header(String tag, Style style) {
            this.style = style;
            this.fontSize = HtmlToSpannedConverter.HEADER_SIZES[tag.charAt(1) - 49];
        }
    }

    /* compiled from: MyHtml */
    private static class Hide {
        private Hide() {
        }
    }

    /* compiled from: MyHtml */
    private static class Href {
        public Attributes mAttributes;
        public String mHref;

        public Href(String href, Attributes attributes) {
            this.mHref = href;
            this.mAttributes = attributes;
        }
    }

    /* compiled from: MyHtml */
    private static class Italic {
        private Italic() {
        }
    }

    /* compiled from: MyHtml */
    private static class Light {
        private Light() {
        }
    }

    /* compiled from: MyHtml */
    private static class MarginQuote {
        Style style;
        String tag;

        public MarginQuote(String tag, Style style) {
            this.tag = tag;
            this.style = style;
        }
    }

    /* compiled from: MyHtml */
    private static class Monospace {
        private Monospace() {
        }
    }

    /* compiled from: MyHtml */
    private static class MyBullet {
        String bullet;
        Style style;

        public MyBullet(String bullet, Style style) {
            this.bullet = bullet;
            this.style = style;
        }
    }

    /* compiled from: MyHtml */
    private static class PreQuote {
        Style style;

        public PreQuote(Style style) {
            this.style = style;
        }
    }

    /* compiled from: MyHtml */
    private static class RubyTag {
        private RubyTag() {
        }
    }

    /* compiled from: MyHtml */
    private static class Shadow {
        private Shadow() {
        }
    }

    /* compiled from: MyHtml */
    private static class Small {
        private Small() {
        }
    }

    /* compiled from: MyHtml */
    private static class Strikethrough {
        private Strikethrough() {
        }
    }

    /* compiled from: MyHtml */
    private static class Strong {
        Style style;

        public Strong(Style style) {
            this.style = style;
        }
    }

    /* compiled from: MyHtml */
    private static class Sub {
        private Sub() {
        }
    }

    /* compiled from: MyHtml */
    private static class Super {
        private Super() {
        }
    }

    /* compiled from: MyHtml */
    class TableElement {
        Attributes attr;
        String tag;
        String tdText;
        int type;

        public TableElement(String tag, Attributes attr, int type) {
            this.tag = tag;
            this.attr = attr;
            this.type = type;
        }
    }

    /* compiled from: MyHtml */
    private static class TableQuote {
        Style style;
        String tag;

        public TableQuote(String tag, Style style) {
            this.tag = tag;
            this.style = style;
        }
    }

    /* compiled from: MyHtml */
    private static class TextFillColor {
        private TextFillColor() {
        }
    }

    /* compiled from: MyHtml */
    private static class ULQuote {
        Style style;

        public ULQuote(Style style) {
            this.style = style;
        }
    }

    /* compiled from: MyHtml */
    private static class Underline {
        private Underline() {
        }
    }

    public HtmlToSpannedConverter(String source, MyImageGetter imageGetter, TagHandler tagHandler, Parser parser, int chapterId) {
        this.mSource = source;
        initCssTagList();
        this.spanBulder = new SpannableStringBuilder();
        this.mMyImageGetter = imageGetter;
        this.mTagHandler = tagHandler;
        this.mReader = parser;
        this.chapterId = chapterId;
    }

    public Spanned convert() {
        SpannableStringBuilder text = null;
        this.mReader.setContentHandler(this);
        try {
            this.mReader.parse(new InputSource(new StringReader(this.mSource)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        text = this.spanBulder;
        Object[] obj = text.getSpans(0, text.length(), ParagraphStyle.class);
        int lastBr = 0;
        MyMarginSpan lastFloatSp = null;
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] instanceof MyMarginSpan) {
                MyMarginSpan m = (MyMarginSpan) obj[i];
                if (m.spEnd == text.length() && CSS.hasBorderOrBackgroundColor(m.cssStyle)) {
                    lastBr = 1;
                }
                if (m instanceof MyFloatSpan) {
                    lastFloatSp = m;
                }
            } else {
                try {
                    int start = text.getSpanStart(obj[i]);
                    int end = text.getSpanEnd(obj[i]);
                    if (end - 2 >= 0 && text.charAt(end - 1) == '\n' && text.charAt(end - 2) == '\n') {
                        end--;
                    }
                    if (end == start) {
                        text.removeSpan(obj[i]);
                    } else {
                        text.setSpan(obj[i], start, end, 51);
                    }
                } catch (Exception e) {
                    A.error(e);
                    A.log("obj:" + obj + " | " + obj.getClass());
                } catch (Throwable e2) {
                    A.error(e2);
                    if (e2 instanceof OutOfMemoryError) {
                        A.tmpOutOfMemoryTag = true;
                        ActivityTxt.show_received_message(A.errorMsg(e2));
                    }
                }
            }
        }
        if (lastFloatSp != null) {
            String s = text.toString();
            int i1 = s.indexOf(10, lastFloatSp.spEnd);
            if (i1 == -1) {
                text.append('\n');
                text.append('\n');
                lastBr = 2;
            } else {
                int i2 = s.indexOf(10, i1 + 1);
                if (i2 == -1) {
                    text.append('\n');
                    lastBr = 2;
                } else if (s.indexOf(10, i2 + 1) == -1) {
                    lastBr = 2;
                }
            }
        }
        boolean deleted = false;
        while (text.length() > 0 && A.isEmptyChar(text.charAt(text.length() - 1))) {
            text.delete(text.length() - 1, text.length());
            deleted = true;
        }
        if (deleted) {
            for (MyMarginSpan sp : (MyMarginSpan[]) text.getSpans(0, text.length(), MyMarginSpan.class)) {
                if (sp.spEnd > text.length()) {
                    sp.spEnd = text.length();
                }
            }
        }
        if (lastBr > 0) {
            text.append('\n');
        }
        return text;
    }

    private void appendText(SpannableStringBuilder text, String addTag) {
        int len = text.length();
        if (addTag.startsWith("\n") && len >= 1 && text.charAt(len - 1) == '\n') {
            addTag = addTag.substring(1);
            if (addTag.length() == 0) {
                return;
            }
        }
        if (len > 0) {
            if (addTag.equals("\n")) {
                handleBr(text);
            } else {
                text.append(addTag);
            }
        } else if (addTag.length() > 1) {
            text.append(addTag);
        }
    }

    private void handleStartTag(String tag, Attributes attributes) {
        if (!this.ignoreStart) {
            MyHtml.tagCount++;
            SpannableStringBuilder s = this.spanBulder;
            Style cssStyle = null;
            if (cssUsedTag(tag)) {
                cssStyle = startCSS(s, tag, attributes);
            }
            if (tag.equals("p")) {
                if (!(this.LI_statement && s.charAt(s.length() - 1) == '\n') && s.length() > this.td_start) {
                    handleP(s, cssStyle, false);
                }
            } else if (tag.equals("div")) {
                handleP(s, cssStyle, true);
            } else if (tag.equals("span")) {
                if (styleHasBlockDisplay(cssStyle)) {
                    handleBrSingle(s);
                }
            } else if (!tag.equals("br")) {
                if (tag.equals("hr")) {
                    appendHR(s);
                } else if (tag.equals("hr2")) {
                    handleP(s, null, false);
                    insertPgaeBreak(s);
                } else if (tag.equals("aside")) {
                    if ("footnote".equals(attributes.getValue("epub:type"))) {
                        this.ignoreStart = true;
                    } else {
                        startCSS(s, tag, attributes);
                    }
                } else if (tag.equals("ruby")) {
                    this.rubyRbStart = false;
                    startRuby(s);
                } else if (tag.equals("rb")) {
                    this.rubyRbStart = true;
                } else if (tag.equals("rt")) {
                    startRubyRt(s);
                } else if (tag.equals("rp")) {
                    this.ignoreStart = true;
                } else if (tag.equals("code") || tag.equals("tt")) {
                    start(s, new Monospace());
                    start(s, new FontQuote(tag, getStyleFromAttr(tag, attributes)));
                } else if (tag.equals("pre")) {
                    this.preTagStart = true;
                    handleP(s, null, false);
                    start(s, new Monospace());
                    start(s, new PreQuote(getStyleFromAttr(tag, attributes)));
                    addTagCache(tag, attributes);
                } else if (tag.equals("center")) {
                    start(s, new Alignment(3));
                } else if (tag.equals("figure") || tag.equals("figcaption")) {
                    handleBrSingle(s);
                    startCSS(s, tag, attributes);
                } else if (tag.equals("dl")) {
                    handleP(s, null, false);
                    startCSS(s, tag, attributes);
                } else if (tag.equals("dt")) {
                    handleBrSingle(s);
                    addTagCache(tag, attributes);
                    start(s, new FontQuote(tag, getStyleFromAttr(tag, attributes)));
                } else if (tag.equals("dd")) {
                    handleBrSingle(s);
                    start(s, new BlockQuote(null));
                    start(s, new FontQuote(tag, getStyleFromAttr(tag, attributes)));
                } else if (tag.equals("table")) {
                    handleP(s, null, false);
                    start(s, new TableQuote(tag, getStyleFromAttr(tag, attributes)));
                } else if (tag.equals("tr")) {
                    start(s, new TableQuote(tag, getStyleFromAttr(tag, attributes)));
                } else if (tag.equals("td") || tag.equals("th")) {
                    this.td_start = s.length();
                } else if (tag.equals(HHCConverter.ul) || tag.equals("ol")) {
                    handleBrSingle(s);
                    addTagCache(tag, attributes);
                    this.li_tag = MyHtml.LI_TAG;
                    this.ul_level++;
                    Style style = getStyleFromAttr(tag, attributes);
                    setLiTag(tag, style);
                    if (tag.equals("ol") && (style == null || style.ul_style == null || !style.ul_style.equals("none"))) {
                        this.ol_value = 1;
                    }
                    start(s, new ULQuote(style));
                } else if (tag.equals("li")) {
                    setLiTag(tag, getStyleFromAttr(tag, attributes));
                    if (this.ol_value > 0 && this.li_tag != null && this.li_tag.startsWith(" 　")) {
                        this.ol_value = 0;
                    }
                    String bullet = this.ol_value > 0 ? this.ol_value + "." + A.INDENT_CHAR : getLiTag();
                    if (this.ol_value > 0) {
                        this.ol_value++;
                    }
                    this.LI_statement = true;
                    handleBrSingle(s);
                    start(s, new MyBullet(bullet, getStyleFromAttr(tag, attributes)));
                } else if (tag.equals("em")) {
                    start(s, new Italic());
                    start(s, new FontQuote(tag, getStyleFromAttr(tag, attributes)));
                } else if (tag.equals("b")) {
                    start(s, new Bold());
                } else if (tag.equals("strong")) {
                    start(s, new FontQuote(tag, getStyleFromAttr(tag, attributes)));
                } else if (tag.equals("cite")) {
                    start(s, new Italic());
                } else if (tag.equals("i")) {
                    start(s, new Italic());
                } else if (tag.equals("big")) {
                    start(s, new Big(tag, getStyleFromAttr(tag, attributes)));
                } else if (tag.equals("small")) {
                    start(s, new Small());
                } else if (tag.equals("font")) {
                    startHtmlFont(s, attributes);
                } else if (tag.equals("blockquote")) {
                    if (A.disableCSS && A.cssLineSpace) {
                        handleP(s, null, false);
                        return;
                    }
                    handleBrSingle(s);
                    start(s, new BlockQuote(cssStyle));
                } else if (tag.equals("a")) {
                    startA(s, attributes);
                } else if (tag.equals("u")) {
                    start(s, new Underline());
                } else if (tag.equals("del") || tag.equals("s") || tag.equals("strike")) {
                    start(s, new Strikethrough());
                } else if (tag.equals("sup")) {
                    start(s, new Super());
                } else if (tag.equals("sub")) {
                    start(s, new Sub());
                } else if (CSS.isHeaderTag(tag)) {
                    if (!A.trimBlankSpace || s.length() <= 1) {
                        handleP(s, cssStyle, false);
                    } else {
                        handleBr2(s);
                    }
                    start(s, new Header(tag, cssStyle));
                } else if (tag.equals("img") || tag.equals("image") || tag.equals("audio") || tag.equals("video")) {
                    if (tag.equals("audio") || tag.equals("video")) {
                        this.mediaTagStart = true;
                    }
                    startImg(tag, s, attributes, this.mMyImageGetter, this.mSource);
                } else if (this.mTagHandler != null) {
                    this.mTagHandler.handleTag(true, tag, s, this.mReader);
                } else if (tag.equals("nav")) {
                    handleBr(s);
                    addTagCache(tag, attributes);
                }
            }
        }
    }

    private void handleEndTag(String tag) {
        if (!this.ignoreStart) {
            SpannableStringBuilder s = this.spanBulder;
            this.lastEndTag = tag;
            if (tag.equals("p")) {
                if (this.td_start == 0) {
                    handleP(s, getLastCssStyle(tag), false);
                }
            } else if (tag.equals("div")) {
                handleP(s, getLastCssStyle(tag), true);
            } else if (tag.equals("span")) {
                if (styleHasBlockDisplay(getLastCssStyle(tag))) {
                    handleBrSingle(s);
                }
            } else if (tag.equals("br")) {
                handleBr(s);
            } else if (CSS.isHeaderTag(tag)) {
                handleP(s, getLastCssStyle(tag), false);
                endHeader(s);
            } else if (tag.equals("aside")) {
                endCSS(s, tag);
            } else if (tag.equals("ruby")) {
                if (!this.rubyRbStart) {
                    endRuby(s, true);
                }
                this.rubyRtText = "";
            } else if (tag.equals("rt")) {
                endRubyRt(s);
            } else if (tag.equals("code") || tag.equals("tt")) {
                end(s, FontQuote.class, null);
                end(s, Monospace.class, new TypefaceSpan("monospace"));
            } else if (tag.equals("pre")) {
                this.preTagStart = false;
                handleBrSingle(s);
                end(s, PreQuote.class, null);
                end(s, Monospace.class, new TypefaceSpan("monospace"));
                handleP(s, null, false);
                removeTagCache();
            } else if (tag.equals("audio") || tag.equals("video")) {
                this.mediaTagStart = false;
            } else if (tag.equals("center")) {
                end(s, Alignment.class, null);
            } else if (tag.equals("figure") || tag.equals("figcaption")) {
                endCSS(s, tag);
                handleP(s, null, false);
            } else if (tag.equals("dl")) {
                endCSS(s, tag);
                handleP(s, null, false);
            } else if (tag.equals("dt")) {
                end(s, FontQuote.class, null);
                handleBrSingle(s);
                removeTagCache();
            } else if (tag.equals("dd")) {
                end(s, FontQuote.class, null);
                handleBr(s);
                end(s, BlockQuote.class, new MyMarginSpan(2.2f, 0.0f, 0.0f, 0.0f));
            } else if (tag.equals("table")) {
                end(s, TableQuote.class, null);
                handleBr(s);
            } else if (tag.equals("tr")) {
                handleBr(s);
                end(s, TableQuote.class, null);
            } else if (tag.equals("td") || tag.equals("th")) {
                this.td_start = 0;
                if (s.length() > 0 && s.charAt(s.length() - 1) != '\n') {
                    appendText(s, "　 ");
                }
            } else if (tag.equals(HHCConverter.ul)) {
                this.li_tag = MyHtml.LI_TAG;
                this.ul_level--;
                handleBrSingle(s);
                end(s, ULQuote.class, null);
                removeTagCache();
            } else if (tag.equals("ol")) {
                this.ol_value = 0;
                this.ul_level--;
                handleBrSingle(s);
                end(s, ULQuote.class, null);
            } else if (tag.equals("li")) {
                this.LI_statement = false;
                end(s, MyBullet.class, null);
            } else if (tag.equals("em")) {
                end(s, Italic.class, new StyleSpan(2));
                end(s, FontQuote.class, null);
            } else if (tag.equals("b")) {
                end(s, Bold.class, new StyleSpan(1));
            } else if (tag.equals("strong")) {
                end(s, FontQuote.class, null);
            } else if (tag.equals("cite")) {
                end(s, Italic.class, new StyleSpan(2));
            } else if (tag.equals("i")) {
                end(s, Italic.class, new StyleSpan(2));
            } else if (tag.equals("big")) {
                end(s, Big.class, null);
            } else if (tag.equals("small")) {
                end(s, Small.class, new MyRelativeSizeSpan(0.8f));
            } else if (tag.equals("font")) {
                endFont(s);
            } else if (tag.equals("blockquote")) {
                if (A.disableCSS && A.cssLineSpace) {
                    handleP(s, null, false);
                } else {
                    handleBrSingle(s);
                    end(s, BlockQuote.class, new MyMarginSpan(0));
                }
            } else if (tag.equals("a")) {
                endA(s);
            } else if (tag.equals("u")) {
                end(s, Underline.class, new UnderlineSpan());
            } else if (tag.equals("del") || tag.equals("s") || tag.equals("strike")) {
                end(s, Strikethrough.class, new StrikethroughSpan());
            } else if (tag.equals("sup")) {
                end(s, Super.class, new SuperscriptSpan());
            } else if (tag.equals("sub")) {
                end(s, Sub.class, new SubscriptSpan());
            } else if (this.mTagHandler != null) {
                this.mTagHandler.handleTag(false, tag, s, this.mReader);
            } else if (tag.equals("nav")) {
                handleBr(s);
                removeTagCache();
            }
            if (cssUsedTag(tag)) {
                endCSS(s, tag);
            }
        } else if (tag.equals("aside") || tag.equals("rp")) {
            this.ignoreStart = false;
        }
    }

    private void removeTagCache() {
        if (this.tags.size() > 0) {
            this.tags.remove(this.tags.size() - 1);
            this.classes.remove(this.classes.size() - 1);
            this.cssStyles.remove(this.cssStyles.size() - 1);
        }
    }

    private void addTagCache(String tag, Attributes attributes) {
        this.tags.add(tag);
        this.classes.add(getValue(attributes, "class"));
        this.cssStyles.add(null);
    }

    private Style getStyleFromAttr(String tag, Attributes attributes) {
        Style cssStyle = MyHtml.getClassStyle(tag, getValue(attributes, "id"), getValue(attributes, "class"), getChapter(this.chapterId), this.tags, this.classes);
        String style = attributes == null ? null : attributes.getValue("style");
        if (style == null) {
            return cssStyle;
        }
        Style cssStyle2 = new Style(cssStyle);
        cssStyle2.scanPropertyForStyle(style);
        return cssStyle2;
    }

    private String getValue(Attributes attributes, String name) {
        if (attributes == null) {
            return null;
        }
        String value = attributes.getValue(name);
        if (value != null) {
            return value.toLowerCase().trim();
        }
        return null;
    }

    private void setLiTag(String tag, Style cssStyle) {
        if (cssStyle != null && cssStyle.ul_style != null) {
            if (cssStyle.ul_style.equals("none")) {
                this.li_tag = "";
            } else if (cssStyle.ul_style.equals("circle")) {
                this.li_tag = "○　";
            } else if (cssStyle.ul_style.equals("square")) {
                this.li_tag = "■　";
            }
        }
    }

    private String getLiTag() {
        if (this.li_tag == null) {
            return MyHtml.LI_TAG;
        }
        return this.ul_level > 1 ? this.li_tag.replace("•", "○") : this.li_tag;
    }

    private boolean cssUsedTag(String tag) {
        return tag.equals("blockquote") || tag.equals("p") || tag.equals("div") || CSS.isHeaderTag(tag) || tag.equals("span") || tag.equals("section");
    }

    private boolean styleHasBlockDisplay(Style style) {
        return style != null && "block".equals(style.display);
    }

    private void startRuby(SpannableStringBuilder s) {
        Object obj = getLast(s, RubyTag.class);
        if (obj != null) {
            s.removeSpan(obj);
        }
        start(s, new RubyTag());
        this.rubyRtStart = false;
        this.rubyRtText = null;
        MyHtml.hasRuby = true;
    }

    private void endRuby(SpannableStringBuilder s, boolean forceDelete) {
        int len = s.length();
        Object obj = getLast(s, RubyTag.class);
        if (obj != null) {
            int where = s.getSpanStart(obj);
            if (where != -1) {
                s.removeSpan(obj);
                if (!forceDelete && where != len) {
                    if (!this.hasBr) {
                        this.hasBr = true;
                        s.insert(0, "\n");
                        where++;
                        len++;
                    }
                    while (where < len - 1 && A.isBlankChar(s.charAt(where))) {
                        where++;
                    }
                    while (len - 1 > where && A.isBlankChar(s.charAt(len - 1))) {
                        len--;
                    }
                    setSpan(s, new Ruby(this.rubyRtText, "", where, len), where, len);
                }
            }
        }
    }

    private void startRubyRt(SpannableStringBuilder s) {
        if (this.rubyRtText == null) {
            this.rubyRtStart = true;
            this.rubyRtText = "";
            return;
        }
        this.rubyRtStart = false;
        start(s, new Small());
    }

    private void endRubyRt(SpannableStringBuilder s) {
        if (this.rubyRtStart) {
            Ruby pre = null;
            Ruby tmp = (Ruby) getLast(s, Ruby.class);
            if (tmp != null) {
                pre = tmp;
            }
            endRuby(s, false);
            Ruby obj = (Ruby) getLast(s, Ruby.class);
            if (obj != null) {
                Ruby cur = obj;
                cur.rt = this.rubyRtText.trim();
                cur.original = s.subSequence(cur.start, cur.end).toString();
                if (pre != null && pre.end == cur.start) {
                    int add = 0;
                    if (pre.rt.length() > (pre.end - pre.start) * 2) {
                        add = 0 + (pre.rt.length() - ((pre.end - pre.start) * 2));
                    }
                    if (cur.rt.length() > (cur.end - cur.start) * 2) {
                        add += cur.rt.length() - ((cur.end - cur.start) * 2);
                    }
                    if (add > 0) {
                        String indent = (add == 2 || add == 3) ? "　" : " ";
                        if (add > 3) {
                            add /= 3;
                            for (int i = 0; i < add; i++) {
                                indent = indent + A.INDENT_CHAR;
                            }
                        }
                        s.insert(cur.start, indent);
                        cur.start += indent.length();
                        cur.end += indent.length();
                        cur.original = indent + cur.original;
                    }
                }
            }
            if (!this.rubyRbStart) {
                startRuby(s);
            }
        } else {
            end(s, Small.class, new MyRelativeSizeSpan(0.5f));
        }
        this.rubyRtStart = false;
    }

    private void appendHR(SpannableStringBuilder text) {
        int l = text.length();
        String tmp = (l <= 0 || text.charAt(l - 1) != '\n') ? "\n" : "";
        appendText(text, tmp + MyHtml.HR_TAG + "\n");
    }

    private Style startCSS(SpannableStringBuilder text, String tag, Attributes attributes) {
        int htmlAlign = 0;
        if (!(A.disableCSS && A.cssAlignment)) {
            String align = attributes.getValue("align");
            if (align != null) {
                if (align.equals("center")) {
                    htmlAlign = 3;
                } else if (align.equals("right")) {
                    htmlAlign = 4;
                } else if (align.equals("justify")) {
                    htmlAlign = 2;
                }
            }
        }
        String className = getValue(attributes, "class");
        this.tags.add(tag);
        this.classes.add(className);
        Style cssStyle = getStyleFromAttr(tag, attributes);
        if (htmlAlign > 0) {
            Style cssStyle2 = new Style(cssStyle);
            cssStyle2.align = htmlAlign;
            cssStyle = cssStyle2;
        }
        if (cssStyle != null) {
            cssStyle.htmlStartPos = text.length();
        }
        this.cssStyles.add(cssStyle);
        if (cssStyle == null) {
            return null;
        }
        if ("none".equals(cssStyle.display)) {
            start(text, new Hide());
            this.hideTag.add(cssStyle);
            return cssStyle;
        }
        if (cssStyle.pageBreak) {
            insertPgaeBreak(text);
        }
        if (!tag.equals("span") || styleHasBlockDisplay(cssStyle)) {
            if (CSS.hasParagraphProperty(cssStyle)) {
                start(text, new MarginQuote(tag, cssStyle));
                this.marginTag.add(cssStyle);
            }
            if (cssStyle.align > 0 && this.tableStart == -1) {
                start(text, new Alignment(cssStyle.align));
                this.alignTag.add(cssStyle);
            }
        } else if (cssStyle.backgroundColor != null) {
            Integer color = T.getHtmlColor(cssStyle.backgroundColor);
            if (color != null) {
                start(text, new BackgroundColor());
                this.backgroundColorTag.add(cssStyle);
                this.backgroundColorValue.add(color);
            }
        }
        boolean fontWeightLight = CSS.isFontWeightLight(cssStyle.fontWeight);
        if (cssStyle.bold || (CSS.isHeaderTag(tag) && !fontWeightLight)) {
            start(text, new Bold());
            this.boldTag.add(cssStyle);
        } else if (fontWeightLight) {
            start(text, new Light());
            this.lightTag.add(cssStyle);
        }
        if (cssStyle.italic) {
            start(text, new Italic());
            this.italicTag.add(cssStyle);
        }
        if (cssStyle.underline) {
            start(text, new Underline());
            this.underlineTag.add(cssStyle);
        }
        if (cssStyle.strikethrough) {
            start(text, new Strikethrough());
            this.strikethroughTag.add(cssStyle);
        }
        if (cssStyle.shadow) {
            start(text, new Shadow());
            this.shadowTag.add(cssStyle);
        }
        if (cssStyle.fontSize > 0.0f || CSS.isHeaderTag(tag)) {
            start(text, new Big(tag, cssStyle));
            this.fontSizeTag.add(cssStyle);
        }
        if (!(cssStyle.color == null && cssStyle.fontFace == null)) {
            Font font = new Font(cssStyle.color, cssStyle.fontFace, 0.0f, cssStyle.fontSizeInherited);
            font.has_fill_color = cssStyle.text_fill_color != null;
            text.setSpan(font, text.length(), text.length(), 17);
            this.fontTag.add(cssStyle);
        }
        if (cssStyle.text_fill_color != null) {
            start(text, new TextFillColor());
            this.textFillColorTag.add(cssStyle);
        }
        if (cssStyle.emphasis_style != null) {
            start(text, new Emphasis_Tag(cssStyle.emphasis_style));
            this.emphasisTag.add(cssStyle);
        }
        if (cssStyle.floatSp == null) {
            return cssStyle;
        }
        start(text, new FloatQuote(tag, cssStyle));
        this.floatTag.add(cssStyle);
        return cssStyle;
    }

    private void initCssTagList() {
        this.floatTag = new ArrayList();
        this.emphasisTag = new ArrayList();
        this.hideTag = new ArrayList();
        this.boldTag = new ArrayList();
        this.lightTag = new ArrayList();
        this.italicTag = new ArrayList();
        this.underlineTag = new ArrayList();
        this.strikethroughTag = new ArrayList();
        this.shadowTag = new ArrayList();
        this.fontSizeTag = new ArrayList();
        this.textFillColorTag = new ArrayList();
        this.fontTag = new ArrayList();
        this.alignTag = new ArrayList();
        this.backgroundColorTag = new ArrayList();
        this.backgroundColorValue = new ArrayList();
        this.marginTag = new ArrayList();
    }

    private void endCSS(SpannableStringBuilder text, String tag) {
        Style lastStyle = getLastCssStyle(tag);
        if (this.hideTag.size() > 0 && this.hideTag.get(this.hideTag.size() - 1) == lastStyle) {
            this.hideTag.remove(this.hideTag.size() - 1);
            end(text, Hide.class, null);
        }
        if (this.emphasisTag.size() > 0 && this.emphasisTag.get(this.emphasisTag.size() - 1) == lastStyle) {
            this.emphasisTag.remove(this.emphasisTag.size() - 1);
            end(text, Emphasis_Tag.class, new Emphasis(null));
        }
        if (this.boldTag.size() > 0 && this.boldTag.get(this.boldTag.size() - 1) == lastStyle) {
            this.boldTag.remove(this.boldTag.size() - 1);
            end(text, Bold.class, new StyleSpan(1));
        }
        if (this.lightTag.size() > 0 && this.lightTag.get(this.lightTag.size() - 1) == lastStyle) {
            this.lightTag.remove(this.lightTag.size() - 1);
        }
        if (this.italicTag.size() > 0 && this.italicTag.get(this.italicTag.size() - 1) == lastStyle) {
            this.italicTag.remove(this.italicTag.size() - 1);
            end(text, Italic.class, new StyleSpan(2));
        }
        if (this.underlineTag.size() > 0 && this.underlineTag.get(this.underlineTag.size() - 1) == lastStyle) {
            this.underlineTag.remove(this.underlineTag.size() - 1);
            end(text, Underline.class, new UnderlineSpan());
        }
        if (this.strikethroughTag.size() > 0 && this.strikethroughTag.get(this.strikethroughTag.size() - 1) == lastStyle) {
            this.strikethroughTag.remove(this.strikethroughTag.size() - 1);
            end(text, Strikethrough.class, new StrikethroughSpan());
        }
        if (this.shadowTag.size() > 0 && this.shadowTag.get(this.shadowTag.size() - 1) == lastStyle) {
            this.shadowTag.remove(this.shadowTag.size() - 1);
        }
        if (this.fontSizeTag.size() > 0 && this.fontSizeTag.get(this.fontSizeTag.size() - 1) == lastStyle) {
            this.fontSizeTag.remove(this.fontSizeTag.size() - 1);
            end(text, Big.class, null);
        }
        if (this.textFillColorTag.size() > 0 && this.textFillColorTag.get(this.textFillColorTag.size() - 1) == lastStyle) {
            this.textFillColorTag.remove(this.textFillColorTag.size() - 1);
            Object obj = getLast(text, TextFillColor.class);
            if (obj != null) {
                text.removeSpan(obj);
            }
        }
        if (this.fontTag.size() > 0 && this.fontTag.get(this.fontTag.size() - 1) == lastStyle) {
            this.fontTag.remove(this.fontTag.size() - 1);
            endFont(this.spanBulder);
        }
        if (this.backgroundColorTag.size() > 0 && this.backgroundColorTag.get(this.backgroundColorTag.size() - 1) == lastStyle) {
            this.backgroundColorTag.remove(this.backgroundColorTag.size() - 1);
            end(text, BackgroundColor.class, new BackgroundColorSpan(((Integer) this.backgroundColorValue.remove(this.backgroundColorValue.size() - 1)).intValue()));
        }
        if (this.marginTag.size() > 0 && this.marginTag.get(this.marginTag.size() - 1) == lastStyle) {
            this.marginTag.remove(this.marginTag.size() - 1);
            end(text, MarginQuote.class, new MyMarginSpan(0.0f, 0.0f, 0.0f, 0.0f));
        }
        if (this.alignTag.size() > 0 && this.alignTag.get(this.alignTag.size() - 1) == lastStyle) {
            this.alignTag.remove(this.alignTag.size() - 1);
            end(text, Alignment.class, null);
        }
        if (this.floatTag.size() > 0 && this.floatTag.get(this.floatTag.size() - 1) == lastStyle) {
            this.floatTag.remove(this.floatTag.size() - 1);
            end(text, FloatQuote.class, null);
        }
        if (this.tags.size() > 0) {
            this.tags.remove(this.tags.size() - 1);
            this.classes.remove(this.classes.size() - 1);
            this.cssStyles.remove(this.cssStyles.size() - 1);
        }
    }

    private Style getLastCssStyle(String tag) {
        for (int i = this.tags.size() - 1; i >= 0; i--) {
            if (((String) this.tags.get(i)).equals(tag)) {
                return (Style) this.cssStyles.get(i);
            }
        }
        return null;
    }

    private Chapter getChapter(int chapterId) {
        return (chapterId == -1 || A.ebook == null) ? null : (Chapter) A.ebook.getChapters().get(chapterId);
    }

    private int getLastAlignValue(SpannableStringBuilder text) {
        Alignment[] objs = (Alignment[]) text.getSpans(0, text.length(), Alignment.class);
        if (objs.length == 0) {
            return 0;
        }
        return objs[objs.length - 1].align;
    }

    private void handleP(SpannableStringBuilder text) {
        handleP(text, null, false);
    }

    private void handleP(SpannableStringBuilder text, Style style, boolean isDiv) {
        int len = text.length();
        if (len < 1 || text.charAt(len - 1) != '\n') {
            if (len != 0) {
                handleBr(text);
                if (!CSS.hasParagraphProperty(style) && !isDiv) {
                    handleBr(text);
                }
            }
        } else if ((len < 2 || text.charAt(len - 2) != '\n') && !CSS.hasParagraphProperty(style) && !isDiv) {
            handleBr(text);
        }
    }

    private void handleBr(SpannableStringBuilder text) {
        if (A.trimBlankSpace) {
            handleBrSingle(text);
        } else {
            handleBr2(text);
        }
    }

    private void handleBrSingle(SpannableStringBuilder text) {
        if (text.length() <= 0 || text.charAt(text.length() - 1) != '\n') {
            handleBr2(text);
        }
    }

    private void handleBrDouble(SpannableStringBuilder text) {
        if (text.length() <= 1 || text.charAt(text.length() - 1) != '\n' || text.charAt(text.length() - 2) != '\n') {
            handleBr2(text);
        }
    }

    private void handleBr2(SpannableStringBuilder text) {
        if (this.floatSpanType <= 0) {
            text.append('\n');
            this.hasBr = true;
        }
    }

    private Object getLast(Spanned text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);
        if (objs.length == 0) {
            return null;
        }
        return objs[objs.length - 1];
    }

    private void insertPgaeBreak(SpannableStringBuilder text) {
        Object o = getLast(text, PAGE_BREAK.class);
        if (o != null) {
            int where = text.getSpanStart(o);
            if (where != -1) {
                int len = text.length();
                if (where < len) {
                    if (where > len - 10 && A.isEmtpyText(text, where, len)) {
                        return;
                    }
                }
                return;
            }
        }
        start(text, new PAGE_BREAK());
    }

    private void start(SpannableStringBuilder text, Object mark) {
        int len = text.length();
        text.setSpan(mark, len, len, 17);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void end(android.text.SpannableStringBuilder r24, java.lang.Class r25, java.lang.Object r26) {}

    private void setSpan(SpannableStringBuilder text, Object repl, int where, int len) {
        if (where == len) {
            A.log("error setspan, where==len");
        } else {
            text.setSpan(repl, where, len, 33);
        }
    }

    private void setTableQuote(SpannableStringBuilder text, TableQuote obj, int where, int len) {
        if (!A.disableCSS || !A.cssOthers) {
//            String tag = obj.tag;
//            MyMarginSpan repl = new MyMarginSpan(0.0f, 0.0f, 0.0f, 0.0f);
////            Style style = new Style();
//            String color = "#777777";
//            if (obj.style != null) {
//                style.backgroundColor = obj.style.backgroundColor;
//            }
//            if (tag.equals("table")) {
//                repl.isTable = true;
//                if (obj.style != null) {
//                    style.margin = obj.style.margin;
//                }
//                style.padding.left = 0.5f;
//                style.padding.right = 0.5f;
//                style.borderColor = new String[]{color, color, color, color};
//                style.borderStyle = new String[]{"solid", "solid", "solid", "solid"};
//                style.border = new RectF(0.0625f, 0.0625f, 0.0625f, 0.0625f);
//                if (getLastAlignValue(text) > 1) {
//                    setSpan(text, new Standard(com.flyersoft.staticlayout.MyLayout.Alignment.ALIGN_LEFT), where, len);
//                }
//            } else {
//                style.borderColor = new String[]{null, color, null, null};
//                style.borderStyle = new String[]{null, "solid", null, null};
//                style.border = new RectF(0.0f, 0.0625f, 0.0f, 0.0f);
//            }
//            setMarginSpanProperty(repl, tag, style, where, len);
//            setSpan(text, repl, where, len);
        }
    }

//    private boolean addImgFloatQuote(SpannableStringBuilder text, Style cssStyle, MyImageSpan myImageSpan, Attributes attributes) {
////        FloatQuote obj = new FloatQuote("img", cssStyle);
////        myImageSpan.originalSize = true;
////        obj.imageSpan = myImageSpan;
////        obj.attributes = attributes;
////        text.append('￼');
//        return false;
//    }

    private boolean setFloatQuote(SpannableStringBuilder text, FloatQuote obj, int where, int len) {
        if (removeFloatInSameLine(text)) {
            return false;
        }
        int i;
        int i1 = -1;
        for (i = where; i < len; i++) {
            if (!A.isEmptyChar(text.charAt(i))) {
                i1 = i;
                break;
            }
        }
        if (i1 == -1) {
            return false;
        }
        for (i = i1 + 1; i < len; i++) {
            if (text.charAt(i) == '￼') {
                return false;
            }
        }
        char c = text.charAt(i1);
        boolean isDropcap = c != '￼';
        int i3 = len - 1;
        while (i3 > i1 && A.isEmptyChar(text.charAt(i3))) {
            i3--;
        }
        int i2 = i1 + 1;
        while (i2 < i3 && A.isEmptyChar(text.charAt(i2))) {
            i2++;
        }
        String dropcap = "" + c;
        if (isDropcap) {
            if (i3 > i2) {
                return false;
            }
            if (i3 == i2) {
                dropcap = dropcap + text.charAt(i2);
            }
        }
        MyFloatSpan repl = new MyFloatSpan();
        String tag = obj.tag;
        Style style = obj.style;
        if (!isDropcap && i3 >= i2) {
            repl.float_text = text.subSequence(i2, i3 + 1);
        }
        repl.padding.left = CSS.getPadding(style.padding, 0) * CSS.EM();
        repl.padding.top = CSS.getPadding(style.padding, 1) * CSS.EM();
        repl.padding.right = CSS.getPadding(style.padding, 2) * CSS.EM();
        repl.padding.bottom = CSS.getPadding(style.padding, 3) * CSS.EM();
        if (isDropcap) {
            if (where > 0) {
                if (text.charAt(where - 1) != '\n') {
                    return false;
                }
            }
            CharacterStyle[] spans = (CharacterStyle[]) text.getSpans(where, len, CharacterStyle.class);
            TextPaint workPaint = new TextPaint(A.txtView.getPaint());
            Styled.getReplacementAndUpdateState(text, A.txtView.getPaint(), workPaint, spans, false, false);
            if (workPaint.getTextSize() / CSS.EM() < 2.0f) {
                workPaint.setTextSize(CSS.EM() * 2.0f);
            }
            FontMetricsInt fm = new FontMetricsInt();
            workPaint.getFontMetricsInt(fm);
            if (Character.isLetterOrDigit(c)) {
                repl.float_height = (float) ((-fm.ascent) + (fm.top - fm.ascent));
            } else {
                repl.float_height = (float) (((-fm.ascent) + fm.descent) + (fm.top - fm.ascent));
            }
            repl.float_width = Layout.getDesiredWidth(dropcap, workPaint);
            repl.float_height += repl.padding.top + repl.padding.bottom;
            if (repl.padding.left + repl.padding.right < A.df(2.0f)) {
                repl.padding.right = A.df(2.0f);
            }
            if (style.indent != null) {
                RectF rectF = repl.padding;
                rectF.left += style.indent.floatValue() * CSS.EM2();
            }
            repl.float_width += repl.padding.left + repl.padding.right;
            repl.isDropcap = true;
            repl.workPaint = workPaint;
            repl.isLeft = true;
        } else {
//            if (obj.imageSpan == null) {
//                Object last = getLast(text, MyImageSpan.class);
//                if (last == null) {
//                    return false;
//                }
//                if (text.getSpanStart(last) != where) {
//                    return false;
//                }
//                repl.imageSpan = (MyImageSpan) last;
//                repl.imageSpan.originalSize = true;
//            } else {
//                repl.imageSpan = obj.imageSpan;
//            }
//            if ("100%".equals(style.width)) {
//                return false;
//            }
//            Drawable d;
//            repl.isLeft = style.floatSp.equals("left");
//            float w = (float) A.getPageWidth();
//            float w1 = CSS.getEmSize(style.width) * CSS.EM();
//            if (w1 == 0.0f && obj.attributes != null) {
//                String ws = obj.attributes.getValue("width");
//                if (ws != null) {
//                    try {
//                        w1 = (float) A.d((float) Integer.valueOf(ws).intValue());
//                    } catch (Exception e) {
//                        w1 = CSS.getEmSize(ws) * CSS.EM();
//                    }
//                }
//            }
//            if (w1 == 0.0f && !"auto".equals(style.width) && obj.imageSpan == null) {
//                repl.imageSpan.mDrawable = null;
//                d = repl.imageSpan.getDrawable();
//                if (d != null && d.getIntrinsicWidth() > 0) {
//                    repl.imageSpan.mRect = new Rect(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//                }
//            }
//            Rect r = repl.imageSpan.mRect;
//            if (r == null || r.width() == 0) {
//                d = repl.imageSpan.getDrawable();
//                if (d == null || d.getIntrinsicWidth() <= 0) {
//                    return false;
//                }
//                MyImageSpan myImageSpan = repl.imageSpan;
//                Rect rect = new Rect(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//                myImageSpan.mRect = rect;
//            }
//            boolean no_width = w1 == 0.0f;
//            if (w1 == 0.0f) {
//                w1 = (float) r.width();
//            }
//            float max = (CSS.getEmSize(style.maxWidth) * CSS.EM()) * 1.05f;
//            if (max > 0.0f && w1 > max) {
//                w1 = max;
//            }
//            if (no_width && w1 > w / 2.0f) {
//                w1 = w / 2.0f;
//            } else if (w1 == 0.0f) {
//                w1 = (4.0f * w) / 10.0f;
//            } else if (w1 > (2.0f * w) / 3.0f) {
//                w1 = (2.0f * w) / 3.0f;
//            } else if (w1 < w / 10.0f) {
//                w1 = w / 10.0f;
//            }
//            if (!T.isNull(repl.float_text)) {
//                repl.float_sl = new StaticLayout(repl.float_text, A.txtView.getPaint(), (int) w1, getLastAlignValue(text) == 3 ? android.text.Layout.Alignment.ALIGN_CENTER : android.text.Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//                repl.float_tv_w = (float) ((int) w1);
//                repl.float_tv_h = (float) (repl.float_sl.getLineTop(repl.float_sl.getLineCount()) + A.d(4.0f));
//            }
//            repl.float_width = w1;
//            repl.float_height = ((((float) r.height()) * w1) / ((float) r.width())) + repl.float_tv_h;
//            repl.margin.left = CSS.getPadding(style.margin, 0) * CSS.EM();
//            repl.margin.top = CSS.getPadding(style.margin, 1) * CSS.EM();
//            repl.margin.right = CSS.getPadding(style.margin, 2) * CSS.EM();
//            repl.margin.bottom = CSS.getPadding(style.margin, 3) * CSS.EM();
//            if (repl.margin.left < 0.0f) {
//                repl.margin.left = 0.0f;
//            }
//            if (repl.margin.right < 0.0f) {
//                repl.margin.right = 0.0f;
//            }
//            if (repl.padding.top + repl.margin.top < A.df(4.0f)) {
//                repl.padding.top = A.df(4.0f);
//            }
//            if (repl.padding.bottom + repl.margin.bottom < A.df(4.0f)) {
//                repl.padding.bottom = A.df(4.0f);
//            }
//            repl.float_height += ((repl.padding.top + repl.padding.bottom) + repl.margin.top) + repl.margin.bottom;
//            if (repl.padding.left + repl.margin.left < A.df(4.0f)) {
//                repl.padding.left = A.df(4.0f);
//            }
//            if (repl.padding.right + repl.margin.right < A.df(4.0f)) {
//                repl.padding.right = A.df(4.0f);
//            }
//            repl.float_width += ((repl.padding.left + repl.padding.right) + repl.margin.left) + repl.margin.right;
        }
        this.deletedFloatContent = text.subSequence(where, len);
        text.delete(where, len);
        where = text.length();
        if (!isDropcap) {
            dropcap = "" + c;
        }
        text.append(dropcap);
        len = text.length();
        setSpan(text, new MyRelativeSizeSpan(CSS.ZERO_FONTSIZE), where, len);
        repl.tag = tag;
        repl.cssStyle = style;
        repl.spStart = where;
        repl.spEnd = len;
        setSpan(text, repl, where, len);
        this.floatSpanType = isDropcap ? 2 : 1;
        return true;
    }

    private boolean removeFloatInSameLine(SpannableStringBuilder text) {
        boolean z = false;
        Object obj = getLast(text, MyFloatSpan.class);
        if (obj != null) {
            int where = text.getSpanStart(obj);
            if (where != -1) {
                z = true;
                int len = text.length() - 1;
                while (len > 0 && text.charAt(len) == '\n') {
                    len--;
                }
                for (int i = where + 1; i < len; i++) {
                    if (text.charAt(i) == '\n') {
                        z = false;
                        break;
                    }
                }
                if (z && (this.deletedFloatContent == null || this.deletedFloatContent.toString().indexOf(65532) == -1)) {
                    text.removeSpan(obj);
                    if (this.deletedFloatContent != null) {
                        text.insert(where, this.deletedFloatContent);
                    }
                }
            }
        }
        return z;
    }

    private void setULQuote(SpannableStringBuilder text, ULQuote obj, int where, int len) {
        int i;
        if (this.ul_level > 0) {
            i = 2;
        } else {
            i = 1;
        }
        MyMarginSpan repl = new MyMarginSpan(i);
        repl.isULSpan = true;
        repl.spStart = where;
        repl.spEnd = len;
        setFontStyles(text, where, len, obj.style, false);
        if (obj.style != null) {
            float def = repl.leftMargin;
            setMarginSpanProperty(repl, HHCConverter.ul, obj.style, where, len);
            if (repl.leftMargin < def) {
                Style liStyle = getStyleFromAttr("li", null);
                if (liStyle == null || liStyle.ul_style == null || !liStyle.ul_style.equals("none")) {
                    repl.leftMargin = def;
                } else if (repl.leftMargin < 1.0f) {
                    repl.leftMargin = 1.0f;
                }
            }
        }
        deleteTopMarginIfPriorHas(repl, text, where);
        setSpan(text, repl, where, len);
    }

    private void setPreQuote(SpannableStringBuilder text, PreQuote obj, int where, int len) {
        Style style = obj.style;
        setFontStyles(text, where, len, style, false);
        if (CSS.hasParagraphProperty(style)) {
            MyMarginSpan repl = new MyMarginSpan(0.0f, 0.0f, 0.0f, 0.0f);
            setMarginSpanProperty(repl, "pre", style, where, len);
            deleteTopMarginIfPriorHas(repl, text, where);
            setSpan(text, repl, where, len);
        }
    }

    private void setFontQuote(SpannableStringBuilder text, FontQuote obj, int where, int len) {
        if (obj.tag.equals("strong")) {
            if (obj.style == null) {
//                obj.style = new Style();
            }
            if (!CSS.isFontWeightLight(obj.style.fontWeight)) {
                obj.style.bold = true;
            }
        }
        setFontStyles(text, where, len, obj.style, true);
    }

    private void setFontStyles(SpannableStringBuilder text, int where, int len, Style style, boolean includeBackgroundColor) {
        if (style != null && where != len) {
            if (style.bold) {
                setSpan(text, new StyleSpan(1), where, len);
            } else if (CSS.isFontWeightLight(style.fontWeight)) {
//                setSpan(text, new MyFontLightSpan(), where, len);
            }
            if (style.italic) {
                setSpan(text, new StyleSpan(2), where, len);
            }
            if (style.underline) {
                setSpan(text, new UnderlineSpan(), where, len);
            }
            if (style.strikethrough) {
                setSpan(text, new StrikethroughSpan(), where, len);
            }
            if (style.shadow) {
//                setSpan(text, new MyShadowSpan(), where, len);
            }
            if (style.color != null && (style.text_fill_color != null || getLast(text, TextFillColor.class) == null)) {
                int c = T.getHtmlColorInt(style.color);
                if (!(c == 0 || c == ViewCompat.MEASURED_STATE_MASK)) {
                    setSpan(text, new ForegroundColorSpan(c), where, len);
                }
            }
            if (style.fontFace != null) {
                MetricAffectingSpan tfSp = getTypefaceSpan(style.fontFace);
                if (tfSp != null) {
                    setSpan(text, tfSp, where, len);
                }
            }
            if (style.fontSize > 0.0f) {
                MyRelativeSizeSpan repl = new MyRelativeSizeSpan(style.fontSize);
                repl.inherited = style.fontSizeInherited;
                setSpan(text, repl, where, len);
            }
            if (includeBackgroundColor && style.backgroundColor != null) {
                Integer color = T.getHtmlColor(style.backgroundColor);
                if (color != null) {
                    setSpan(text, new BackgroundColorSpan(color.intValue()), where, len);
                }
            }
        }
    }

    private void deleteTopMarginIfPriorHas(MyMarginSpan sp, SpannableStringBuilder text, int where) {
        if (sp.cssStyle == null || sp.cssStyle.margin.top != 0.0f) {
            boolean priorHas;
            MyMarginSpan[] objs;
            MyMarginSpan last;
            boolean curIsP = false;
            float shouldBe = 0;
            float mt;
            float lastBottomM = CSS.no_margin;
            boolean lastIsP = false;
            if (where > 2) {
                if (text.charAt(where - 1) == '\n') {
                    if (text.charAt(where - 2) == '\n') {
                        priorHas = true;
                        if (!priorHas) {
                            objs = (MyMarginSpan[]) text.getSpans(0, where, MyMarginSpan.class);
                            if (objs.length > 0) {
                                last = objs[objs.length - 1];
                                if (last != null && text.getSpanEnd(last) == where) {
                                    lastIsP = "div".equals(last.tag) && !"span".equals(last.tag);
                                    if (!(last.cssStyle == null || last.cssStyle.margin == null)) {
                                        lastBottomM = last.cssStyle.margin.bottom;
                                    }
                                }
                            }
                        }
                        curIsP = "div".equals(sp.tag) && !"span".equals(sp.tag);
                        shouldBe = curIsP ? 0.0f : lastBottomM != CSS.no_margin ? lastIsP ? 0.0f : 1.0f : lastBottomM < 1.0f ? 0.0f : lastBottomM <= 0.0f ? 1.0f - lastBottomM : 1.0f + lastBottomM;
                        if (sp.cssStyle != null) {
                            sp.topMargin = A.getParagraphRatio() * shouldBe;
                        }
                        mt = sp.cssStyle.margin.top;
                        if (!(mt == 0.0f || mt == CSS.no_margin)) {
                            if (curIsP) {
                                shouldBe += mt;
                            } else if (lastBottomM == 0.0f) {
                                shouldBe = mt;
                            } else if (mt >= 1.0f) {
                                shouldBe += mt - 1.0f;
                            } else if (mt < 0.0f) {
                                shouldBe += mt;
                            }
                        }
                        sp.topMargin = ((CSS.getPadding(sp.cssStyle.padding, 1) + shouldBe) + CSS.getBorder(sp.cssStyle.borderStyle, sp.cssStyle.border, 1)) * A.getParagraphRatio();
                        sp.cssStyle = new Style(sp.cssStyle);
                        sp.cssStyle.margin.top = shouldBe;
                        return;
                    }
                }
            }
            priorHas = false;
            if (priorHas) {
                objs = (MyMarginSpan[]) text.getSpans(0, where, MyMarginSpan.class);
                if (objs.length > 0) {
                    last = objs[objs.length - 1];
                    if ("div".equals(last.tag)) {
                    }
                    lastBottomM = last.cssStyle.margin.bottom;
                }
            }
            if ("div".equals(sp.tag)) {
            }
            if (curIsP) {
                if (lastBottomM != CSS.no_margin) {
                    if (lastBottomM < 1.0f) {
                        if (lastBottomM <= 0.0f) {
                        }
                    }
                }
            }
            if (sp.cssStyle != null) {
                mt = sp.cssStyle.margin.top;
                if (curIsP) {
                    shouldBe += mt;
                } else if (lastBottomM == 0.0f) {
                    shouldBe = mt;
                } else if (mt >= 1.0f) {
                    shouldBe += mt - 1.0f;
                } else if (mt < 0.0f) {
                    shouldBe += mt;
                }
                sp.topMargin = ((CSS.getPadding(sp.cssStyle.padding, 1) + shouldBe) + CSS.getBorder(sp.cssStyle.borderStyle, sp.cssStyle.border, 1)) * A.getParagraphRatio();
                sp.cssStyle = new Style(sp.cssStyle);
                sp.cssStyle.margin.top = shouldBe;
                return;
            }
            sp.topMargin = A.getParagraphRatio() * shouldBe;
        }
    }

    private void setMarginSpanProperty(MyMarginSpan sp, String tag, Style style, int spStart, int spEnd) {
        float f;
        sp.cssStyle = style;
        sp.tag = tag;
        sp.spStart = spStart;
        sp.spEnd = spEnd;
        if (!A.indentParagraph) {
            if (style.indent == null) {
                f = 0.0f;
            } else {
                f = style.indent.floatValue();
            }
            sp.indent = f;
        }
        float r = 1.0f;
        if (!(CSS.isMarginNull(style.margin) && CSS.isMarginNull(style.padding)) && (style.marginInherited.top > 0.0f || style.marginInherited.bottom > 0.0f || style.paddingInherited.top > 0.0f || style.paddingInherited.bottom > 0.0f)) {
            CharacterStyle[] spans = (CharacterStyle[]) this.spanBulder.getSpans(spStart, spEnd, MyRelativeSizeSpan.class);
            if (spans.length > 0) {
                TextPaint workPaint = new TextPaint(A.txtView.getPaint());
                Styled.getReplacementAndUpdateState(this.spanBulder, A.txtView.getPaint(), workPaint, spans, false, true);
                r = workPaint.getTextSize() / A.txtView.getPaint().getTextSize();
            }
        }
        sp.leftPadding = CSS.getPadding(style.padding, 0);
        sp.leftMargin = (CSS.getMargin(style.margin, tag, 0) + sp.leftPadding) + CSS.getBorder(style.borderStyle, style.border, 0);
        sp.topPadding = (style.paddingInherited.top > 0.0f ? r : 1.0f) * CSS.getPadding(style.padding, 1);
        sp.topMargin = (((style.marginInherited.top > 0.0f ? r : 1.0f) * CSS.getMargin(style.margin, tag, 1)) + sp.topPadding) + CSS.getBorder(style.borderStyle, style.border, 1);
        sp.rightPadding = CSS.getPadding(style.padding, 2);
        sp.rightMargin = (CSS.getMargin(style.margin, tag, 2) + sp.rightPadding) + CSS.getBorder(style.borderStyle, style.border, 2);
        sp.bottomPadding = (style.paddingInherited.bottom > 0.0f ? r : 1.0f) * CSS.getPadding(style.padding, 3);
        f = CSS.getMargin(style.margin, tag, 3);
        if (style.marginInherited.bottom <= 0.0f) {
            r = 1.0f;
        }
        sp.bottomMargin = ((f * r) + sp.bottomPadding) + CSS.getBorder(style.borderStyle, style.border, 3);
        if (A.paragraphSpace != 10 || A.trimBlankSpace) {
            float ratio = A.getParagraphRatio();
            sp.topPadding *= ratio;
            sp.topMargin *= ratio;
            sp.bottomPadding *= ratio;
            sp.bottomMargin *= ratio;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void startImg(java.lang.String r38, android.text.SpannableStringBuilder r39, org.xml.sax.Attributes r40, com.flyersoft.staticlayout.MyHtml.MyImageGetter r41, java.lang.String r42) {
        /*
        r37 = this;
        r3 = "audio";
        r0 = r38;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0037;
    L_0x000a:
        r3 = "src";
        r0 = r40;
        r34 = r0.getValue(r3);
        if (r34 != 0) goto L_0x001f;
    L_0x0014:
        r3 = 1;
        r0 = r37;
        r1 = r42;
        r2 = r40;
        r34 = r0.getMediaUrl(r1, r2, r3);
    L_0x001f:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "#audio#";
        r3 = r3.append(r6);
        r0 = r34;
        r3 = r3.append(r0);
        r9 = r3.toString();
    L_0x0034:
        if (r9 != 0) goto L_0x00ab;
    L_0x0036:
        return;
    L_0x0037:
        r3 = "video";
        r0 = r38;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x006c;
    L_0x0041:
        r3 = "src";
        r0 = r40;
        r34 = r0.getValue(r3);
        if (r34 != 0) goto L_0x0056;
    L_0x004b:
        r3 = 0;
        r0 = r37;
        r1 = r42;
        r2 = r40;
        r34 = r0.getMediaUrl(r1, r2, r3);
    L_0x0056:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "#video#";
        r3 = r3.append(r6);
        r0 = r34;
        r3 = r3.append(r0);
        r9 = r3.toString();
        goto L_0x0034;
    L_0x006c:
        r3 = "img";
        r0 = r38;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x007f;
    L_0x0076:
        r3 = "src";
        r0 = r40;
        r9 = r0.getValue(r3);
        goto L_0x0034;
    L_0x007f:
        r3 = "xlink:href";
        r0 = r40;
        r9 = r0.getValue(r3);
        if (r9 != 0) goto L_0x0091;
    L_0x0089:
        r3 = "href";
        r0 = r40;
        r9 = r0.getValue(r3);
    L_0x0091:
        if (r9 != 0) goto L_0x0034;
    L_0x0093:
        r3 = "l:href";
        r0 = r40;
        r9 = r0.getValue(r3);
        if (r9 == 0) goto L_0x0034;
    L_0x009d:
        r3 = "#";
        r3 = r9.startsWith(r3);
        if (r3 == 0) goto L_0x0034;
    L_0x00a5:
        r3 = 1;
        r9 = r9.substring(r3);
        goto L_0x0034;
    L_0x00ab:
        r3 = "alt";
        r0 = r40;
        r14 = r0.getValue(r3);
        r3 = "class";
        r0 = r37;
        r1 = r40;
        r5 = r0.getValue(r1, r3);
        r3 = "id";
        r0 = r37;
        r1 = r40;
        r4 = r0.getValue(r1, r3);
        r3 = "style";
        r0 = r40;
        r33 = r0.getValue(r3);
        r3 = com.flyersoft.books.T.isNull(r5);
        if (r3 == 0) goto L_0x02c3;
    L_0x00d5:
        r3 = com.flyersoft.books.T.isNull(r4);
        if (r3 == 0) goto L_0x02c3;
    L_0x00db:
        r29 = 1;
    L_0x00dd:
        r11 = 0;
        r3 = "nobreak";
        r28 = r3.equals(r14);
        r17 = 0;
        r12 = 0;
        r3 = com.flyersoft.staticlayout.HtmlToSpannedConverter.Href.class;
        r0 = r37;
        r1 = r39;
        r30 = r0.getLast(r1, r3);
        if (r30 == 0) goto L_0x02c7;
    L_0x00f3:
        r30 = (com.flyersoft.staticlayout.HtmlToSpannedConverter.Href) r30;
        r25 = r30;
    L_0x00f7:
        r0 = r37;
        r3 = r0.chapterId;
        r0 = r37;
        r6 = r0.getChapter(r3);
        r0 = r37;
        r7 = r0.tags;
        r0 = r37;
        r8 = r0.classes;
        r3 = r38;
        r15 = com.flyersoft.staticlayout.MyHtml.getClassStyle(r3, r4, r5, r6, r7, r8);
        if (r33 == 0) goto L_0x0121;
    L_0x0111:
        r16 = new com.flyersoft.components.CSS$Style;
        r0 = r16;
        r0.<init>(r15);
        r0 = r16;
        r1 = r33;
        r0.scanPropertyForStyle(r1);
        r15 = r16;
    L_0x0121:
        r18 = 0;
        r3 = "align";
        r0 = r40;
        r13 = r0.getValue(r3);
        if (r13 == 0) goto L_0x0141;
    L_0x012d:
        r3 = "left";
        r3 = r13.equals(r3);
        if (r3 == 0) goto L_0x0137;
    L_0x0135:
        r18 = 1;
    L_0x0137:
        r3 = "right";
        r3 = r13.equals(r3);
        if (r3 == 0) goto L_0x0141;
    L_0x013f:
        r18 = 4;
    L_0x0141:
        r3 = 1;
        r0 = r18;
        if (r0 == r3) goto L_0x014b;
    L_0x0146:
        r3 = 4;
        r0 = r18;
        if (r0 != r3) goto L_0x015f;
    L_0x014b:
        r16 = new com.flyersoft.components.CSS$Style;
        r0 = r16;
        r0.<init>(r15);
        r3 = 1;
        r0 = r18;
        if (r0 != r3) goto L_0x02cb;
    L_0x0157:
        r3 = "left";
    L_0x0159:
        r0 = r16;
        r0.floatSp = r3;
        r15 = r16;
    L_0x015f:
        if (r5 == 0) goto L_0x016f;
    L_0x0161:
        r3 = "duokan";
        r3 = r5.contains(r3);
        if (r3 == 0) goto L_0x016f;
    L_0x0169:
        if (r14 == 0) goto L_0x016f;
    L_0x016b:
        r17 = 1;
        r11 = r17;
    L_0x016f:
        if (r15 == 0) goto L_0x02e9;
    L_0x0171:
        r3 = r15.display;
        if (r3 == 0) goto L_0x018a;
    L_0x0175:
        r3 = r15.display;
        r6 = "none";
        r3 = r3.equals(r6);
        if (r3 != 0) goto L_0x0036;
    L_0x017f:
        r3 = r15.display;
        r6 = "inline";
        r3 = r3.equals(r6);
        if (r3 == 0) goto L_0x02cf;
    L_0x0189:
        r11 = 1;
    L_0x018a:
        if (r11 != 0) goto L_0x01bb;
    L_0x018c:
        r3 = r15.css_text;
        r6 = "height";
        r24 = com.flyersoft.components.CSS.propertyTagIndex(r3, r6);
        r3 = -1;
        r0 = r24;
        if (r0 == r3) goto L_0x01bb;
    L_0x0199:
        r3 = r15.css_text;
        r6 = "height";
        r6 = r6.length();
        r6 = r6 + r24;
        r6 = r6 + 1;
        r10 = 1;
        r3 = com.flyersoft.components.CSS.propertyTagValue(r3, r6, r10);
        r22 = com.flyersoft.components.CSS.getEmSize(r3);
        r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = (r22 > r3 ? 1 : (r22 == r3 ? 0 : -1));
        if (r3 > 0) goto L_0x02dc;
    L_0x01b4:
        r3 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r3 = (r22 > r3 ? 1 : (r22 == r3 ? 0 : -1));
        if (r3 < 0) goto L_0x02dc;
    L_0x01ba:
        r11 = 1;
    L_0x01bb:
        if (r15 == 0) goto L_0x0309;
    L_0x01bd:
        r3 = r15.floatSp;
        if (r3 == 0) goto L_0x0309;
    L_0x01c1:
        r3 = com.flyersoft.books.A.disableCSS;
        if (r3 == 0) goto L_0x01c9;
    L_0x01c5:
        r3 = com.flyersoft.books.A.cssOthers;
        if (r3 != 0) goto L_0x0309;
    L_0x01c9:
        r0 = r37;
        r1 = r39;
        r3 = r0.removeFloatInSameLine(r1);
        if (r3 != 0) goto L_0x0309;
    L_0x01d3:
        r19 = 1;
    L_0x01d5:
        r7 = 0;
        r8 = 0;
        if (r41 == 0) goto L_0x0201;
    L_0x01d9:
        if (r19 == 0) goto L_0x030d;
    L_0x01db:
        r3 = "auto";
        r6 = r15.width;
        r3 = r3.equals(r6);
        if (r3 != 0) goto L_0x030d;
    L_0x01e5:
        r31 = 1;
    L_0x01e7:
        if (r15 == 0) goto L_0x0311;
    L_0x01e9:
        r3 = "100%";
        r6 = r15.width;
        r3 = r3.equals(r6);
        if (r3 == 0) goto L_0x0311;
    L_0x01f3:
        r20 = 1;
    L_0x01f5:
        r3 = com.flyersoft.books.A.ebook;
        if (r3 != 0) goto L_0x0315;
    L_0x01f9:
        r0 = r41;
        r1 = r31;
        r7 = r0.getDrawable(r9, r1);
    L_0x0201:
        if (r7 != 0) goto L_0x022d;
    L_0x0203:
        if (r8 == 0) goto L_0x020b;
    L_0x0205:
        r3 = r8.width();
        if (r3 > 0) goto L_0x022d;
    L_0x020b:
        r3 = com.flyersoft.books.A.getContext();
        r3 = r3.getResources();
        r6 = 2130837936; // 0x7f0201b0 float:1.728084E38 double:1.052773821E-314;
        r7 = r3.getDrawable(r6);
        r3 = 0;
        r6 = 0;
        r10 = r7.getIntrinsicWidth();
        r36 = r7.getIntrinsicHeight();
        r0 = r36;
        r7.setBounds(r3, r6, r10, r0);
        r8 = r7.getBounds();
    L_0x022d:
        if (r19 == 0) goto L_0x0242;
    L_0x022f:
        r6 = new com.flyersoft.staticlayout.MyImageSpan;
        r10 = r41;
        r6.<init>(r7, r8, r9, r10, r11, r12);
        r0 = r37;
        r1 = r39;
        r2 = r40;
        r3 = r0.addImgFloatQuote(r1, r15, r6, r2);
        if (r3 != 0) goto L_0x0036;
    L_0x0242:
        r23 = r12;
        if (r28 != 0) goto L_0x025e;
    L_0x0246:
        if (r23 != 0) goto L_0x025e;
    L_0x0248:
        r3 = "audio";
        r0 = r38;
        r3 = r0.equals(r3);
        if (r3 != 0) goto L_0x025c;
    L_0x0252:
        r3 = "video";
        r0 = r38;
        r3 = r0.equals(r3);
        if (r3 == 0) goto L_0x0397;
    L_0x025c:
        r23 = 1;
    L_0x025e:
        if (r23 == 0) goto L_0x0267;
    L_0x0260:
        r0 = r37;
        r1 = r39;
        r0.handleBrSingle(r1);
    L_0x0267:
        r3 = 65532; // 0xfffc float:9.183E-41 double:3.2377E-319;
        r0 = r39;
        r0.append(r3);
        r26 = r39.length();
        r6 = new com.flyersoft.staticlayout.MyImageSpan;
        r10 = r41;
        r6.<init>(r7, r8, r9, r10, r11, r12);
        r3 = r26 + -1;
        r0 = r37;
        r1 = r39;
        r2 = r26;
        r0.setSpan(r1, r6, r3, r2);
        if (r17 == 0) goto L_0x02b8;
    L_0x0287:
        r3 = com.flyersoft.books.T.isNull(r14);
        if (r3 != 0) goto L_0x02b8;
    L_0x028d:
        if (r25 == 0) goto L_0x0295;
    L_0x028f:
        r0 = r25;
        r3 = r0.mHref;
        if (r3 != 0) goto L_0x02b8;
    L_0x0295:
        r3 = new com.flyersoft.staticlayout.MyUrlSpan;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r10 = "***";
        r6 = r6.append(r10);
        r6 = r6.append(r14);
        r6 = r6.toString();
        r3.<init>(r6);
        r6 = r26 + -1;
        r0 = r37;
        r1 = r39;
        r2 = r26;
        r0.setSpan(r1, r3, r6, r2);
    L_0x02b8:
        if (r23 == 0) goto L_0x0036;
    L_0x02ba:
        r0 = r37;
        r1 = r39;
        r0.handleBr2(r1);
        goto L_0x0036;
    L_0x02c3:
        r29 = 0;
        goto L_0x00dd;
    L_0x02c7:
        r25 = 0;
        goto L_0x00f7;
    L_0x02cb:
        r3 = "right";
        goto L_0x0159;
    L_0x02cf:
        r3 = r15.display;
        r6 = "block";
        r3 = r3.equals(r6);
        if (r3 == 0) goto L_0x018a;
    L_0x02d9:
        r12 = 1;
        goto L_0x018a;
    L_0x02dc:
        r3 = "1em";
        r6 = r15.width;
        r3 = r3.equals(r6);
        if (r3 == 0) goto L_0x01bb;
    L_0x02e6:
        r11 = 1;
        goto L_0x01bb;
    L_0x02e9:
        if (r17 != 0) goto L_0x01bb;
    L_0x02eb:
        if (r25 == 0) goto L_0x01bb;
    L_0x02ed:
        r0 = r25;
        r3 = r0.mAttributes;
        r6 = "class";
        r0 = r37;
        r5 = r0.getValue(r3, r6);
        if (r5 == 0) goto L_0x01bb;
    L_0x02fb:
        r3 = "duokan";
        r3 = r5.contains(r3);
        if (r3 == 0) goto L_0x01bb;
    L_0x0303:
        r17 = 1;
        r11 = r17;
        goto L_0x01bb;
    L_0x0309:
        r19 = 0;
        goto L_0x01d5;
    L_0x030d:
        r31 = 0;
        goto L_0x01e7;
    L_0x0311:
        r20 = 0;
        goto L_0x01f5;
    L_0x0315:
        r0 = r41;
        r1 = r31;
        r8 = r0.getDrawableBounds(r9, r1);
        if (r11 == 0) goto L_0x0338;
    L_0x031f:
        if (r29 == 0) goto L_0x0338;
    L_0x0321:
        r3 = r39.length();
        if (r3 == 0) goto L_0x0337;
    L_0x0327:
        r3 = r39.length();
        r3 = r3 + -1;
        r0 = r39;
        r3 = r0.charAt(r3);
        r6 = 10;
        if (r3 != r6) goto L_0x0338;
    L_0x0337:
        r11 = 0;
    L_0x0338:
        if (r11 == 0) goto L_0x0374;
    L_0x033a:
        r3 = com.flyersoft.books.A.txtView;
        r6 = com.flyersoft.books.A.txtView;
        r6 = r6.getPaint();
        r21 = r3.getInLineHeight(r6);
        r3 = r8.height();
        if (r3 != 0) goto L_0x0367;
    L_0x034c:
        r27 = r21;
    L_0x034e:
        r3 = com.flyersoft.books.A.getPageWidth();
        r3 = r3 * 8;
        r3 = r3 / 10;
        r0 = r27;
        if (r0 >= r3) goto L_0x0201;
    L_0x035a:
        r8 = new android.graphics.Rect;
        r3 = 0;
        r6 = 0;
        r0 = r27;
        r1 = r21;
        r8.<init>(r3, r6, r0, r1);
        goto L_0x0201;
    L_0x0367:
        r3 = r8.width();
        r3 = r3 * r21;
        r6 = r8.height();
        r27 = r3 / r6;
        goto L_0x034e;
    L_0x0374:
        if (r20 == 0) goto L_0x0201;
    L_0x0376:
        if (r8 == 0) goto L_0x0201;
    L_0x0378:
        r35 = com.flyersoft.books.A.getPageWidth2();
        r32 = new android.graphics.Rect;
        r3 = 0;
        r6 = 0;
        r10 = r8.height();
        r10 = r10 * r35;
        r36 = r8.width();
        r10 = r10 / r36;
        r0 = r32;
        r1 = r35;
        r0.<init>(r3, r6, r1, r10);
        r8 = r32;
        goto L_0x0201;
    L_0x0397:
        if (r7 == 0) goto L_0x03a9;
    L_0x0399:
        r3 = r7.getIntrinsicWidth();
        r6 = com.flyersoft.books.A.getPageWidth();
        r6 = r6 / 3;
        if (r3 <= r6) goto L_0x03a9;
    L_0x03a5:
        r23 = 1;
        goto L_0x025e;
    L_0x03a9:
        if (r8 == 0) goto L_0x025e;
    L_0x03ab:
        r3 = r8.width();
        r6 = com.flyersoft.books.A.getPageWidth();
        r6 = r6 / 3;
        if (r3 <= r6) goto L_0x025e;
    L_0x03b7:
        r23 = 1;
        goto L_0x025e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.staticlayout.HtmlToSpannedConverter.startImg(java.lang.String, android.text.SpannableStringBuilder, org.xml.sax.Attributes, com.flyersoft.staticlayout.MyHtml$MyImageGetter, java.lang.String):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getMediaUrl(java.lang.String r20, org.xml.sax.Attributes r21, boolean r22) {
        /*
        r19 = this;
        r16 = "id";
        r0 = r21;
        r1 = r16;
        r7 = r0.getValue(r1);
        r16 = "poster";
        r0 = r21;
        r1 = r16;
        r8 = r0.getValue(r1);
        r11 = 0;
        if (r22 == 0) goto L_0x002b;
    L_0x0017:
        r2 = "<audio ";
    L_0x0019:
        if (r22 == 0) goto L_0x002e;
    L_0x001b:
        r3 = "</audio>";
    L_0x001d:
        r0 = r20;
        r5 = r0.indexOf(r2, r11);	 Catch:{ Exception -> 0x00de }
        r16 = -1;
        r0 = r16;
        if (r5 != r0) goto L_0x0031;
    L_0x0029:
        r13 = 0;
    L_0x002a:
        return r13;
    L_0x002b:
        r2 = "<video ";
        goto L_0x0019;
    L_0x002e:
        r3 = "</video>";
        goto L_0x001d;
    L_0x0031:
        r0 = r20;
        r6 = r0.indexOf(r3, r5);	 Catch:{ Exception -> 0x00de }
        r16 = -1;
        r0 = r16;
        if (r6 == r0) goto L_0x0029;
    L_0x003d:
        r16 = ">";
        r0 = r20;
        r1 = r16;
        r16 = r0.indexOf(r1, r5);	 Catch:{ Exception -> 0x00de }
        r0 = r20;
        r1 = r16;
        r12 = r0.substring(r5, r1);	 Catch:{ Exception -> 0x00de }
        if (r7 == 0) goto L_0x0057;
    L_0x0051:
        r16 = r12.indexOf(r7);	 Catch:{ Exception -> 0x00de }
        if (r16 <= 0) goto L_0x0133;
    L_0x0057:
        if (r8 == 0) goto L_0x005f;
    L_0x0059:
        r16 = r12.indexOf(r8);	 Catch:{ Exception -> 0x00de }
        if (r16 <= 0) goto L_0x0133;
    L_0x005f:
        r0 = r20;
        r10 = r0.substring(r5, r6);	 Catch:{ Exception -> 0x00de }
        r11 = 0;
        r14 = new java.util.ArrayList;	 Catch:{ Exception -> 0x00de }
        r14.<init>();	 Catch:{ Exception -> 0x00de }
    L_0x006b:
        r16 = "<source ";
        r0 = r16;
        r15 = r10.indexOf(r0, r11);	 Catch:{ Exception -> 0x00de }
        r16 = -1;
        r0 = r16;
        if (r15 != r0) goto L_0x009e;
    L_0x0079:
        r16 = r14.iterator();	 Catch:{ Exception -> 0x00de }
    L_0x007d:
        r17 = r16.hasNext();	 Catch:{ Exception -> 0x00de }
        if (r17 == 0) goto L_0x00e4;
    L_0x0083:
        r13 = r16.next();	 Catch:{ Exception -> 0x00de }
        r13 = (java.lang.String) r13;	 Catch:{ Exception -> 0x00de }
        r17 = ".mp";
        r0 = r17;
        r17 = r13.lastIndexOf(r0);	 Catch:{ Exception -> 0x00de }
        r18 = r13.length();	 Catch:{ Exception -> 0x00de }
        r18 = r18 + -4;
        r0 = r17;
        r1 = r18;
        if (r0 != r1) goto L_0x007d;
    L_0x009d:
        goto L_0x002a;
    L_0x009e:
        r11 = r15 + 1;
        r16 = ">";
        r0 = r16;
        r16 = r10.indexOf(r0, r15);	 Catch:{ Exception -> 0x00de }
        r0 = r16;
        r9 = r10.substring(r15, r0);	 Catch:{ Exception -> 0x00de }
        r16 = " src";
        r0 = r16;
        r15 = r9.indexOf(r0);	 Catch:{ Exception -> 0x00de }
        r16 = -1;
        r0 = r16;
        if (r15 == r0) goto L_0x0079;
    L_0x00bc:
        r16 = "\"";
        r0 = r16;
        r15 = r9.indexOf(r0, r15);	 Catch:{ Exception -> 0x00de }
        r16 = r15 + 1;
        r17 = "\"";
        r18 = r15 + 1;
        r0 = r17;
        r1 = r18;
        r17 = r9.indexOf(r0, r1);	 Catch:{ Exception -> 0x00de }
        r0 = r16;
        r1 = r17;
        r13 = r9.substring(r0, r1);	 Catch:{ Exception -> 0x00de }
        r14.add(r13);	 Catch:{ Exception -> 0x00de }
        goto L_0x006b;
    L_0x00de:
        r4 = move-exception;
        com.flyersoft.books.A.error(r4);
        goto L_0x0029;
    L_0x00e4:
        r16 = r14.iterator();	 Catch:{ Exception -> 0x00de }
    L_0x00e8:
        r17 = r16.hasNext();	 Catch:{ Exception -> 0x00de }
        if (r17 == 0) goto L_0x0100;
    L_0x00ee:
        r13 = r16.next();	 Catch:{ Exception -> 0x00de }
        r13 = (java.lang.String) r13;	 Catch:{ Exception -> 0x00de }
        r17 = ".mp";
        r0 = r17;
        r17 = r13.lastIndexOf(r0);	 Catch:{ Exception -> 0x00de }
        if (r17 <= 0) goto L_0x00e8;
    L_0x00fe:
        goto L_0x002a;
    L_0x0100:
        r16 = r14.iterator();	 Catch:{ Exception -> 0x00de }
    L_0x0104:
        r17 = r16.hasNext();	 Catch:{ Exception -> 0x00de }
        if (r17 == 0) goto L_0x011c;
    L_0x010a:
        r13 = r16.next();	 Catch:{ Exception -> 0x00de }
        r13 = (java.lang.String) r13;	 Catch:{ Exception -> 0x00de }
        r17 = ".m";
        r0 = r17;
        r17 = r13.lastIndexOf(r0);	 Catch:{ Exception -> 0x00de }
        if (r17 <= 0) goto L_0x0104;
    L_0x011a:
        goto L_0x002a;
    L_0x011c:
        r16 = r14.size();	 Catch:{ Exception -> 0x00de }
        if (r16 <= 0) goto L_0x0130;
    L_0x0122:
        r16 = 0;
        r0 = r16;
        r16 = r14.get(r0);	 Catch:{ Exception -> 0x00de }
        r16 = (java.lang.String) r16;	 Catch:{ Exception -> 0x00de }
        r13 = r16;
        goto L_0x002a;
    L_0x0130:
        r13 = 0;
        goto L_0x002a;
    L_0x0133:
        r11 = r6;
        goto L_0x001d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.staticlayout.HtmlToSpannedConverter.getMediaUrl(java.lang.String, org.xml.sax.Attributes, boolean):java.lang.String");
    }

    private void startHtmlFont(SpannableStringBuilder text, Attributes attributes) {
        String color = attributes.getValue("", "color");
        String face = attributes.getValue("", "face");
        String size = attributes.getValue("", "size");
        float sizeValue = 0.0f;
        if (size != null) {
            try {
                if (size.endsWith("%")) {
                    sizeValue = (((Float.valueOf(size.substring(0, size.length() - 1)).floatValue() / 100.0f) - 1.0f) * 0.5f) + 1.0f;
                } else if (size.endsWith("em")) {
                    sizeValue = Float.valueOf(size.substring(0, size.length() - 2)).floatValue() / 3.0f;
                } else {
                    sizeValue = Float.valueOf(size.substring(0, size.length())).floatValue();
                    if (size.startsWith("+") || size.startsWith("-")) {
                        sizeValue += 3.0f;
                    }
                    sizeValue = sizeValue == 1.0f ? 0.7f : sizeValue == 2.0f ? 0.85f : sizeValue / 3.0f;
                }
            } catch (Exception e) {
                A.error(e);
            }
        }
        text.setSpan(new Font(color, face, sizeValue, false), text.length(), text.length(), 17);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void endFont(android.text.SpannableStringBuilder r21) {
        /*
        r20 = this;
        r13 = r21.length();
        r4 = com.flyersoft.staticlayout.HtmlToSpannedConverter.Font.class;
        r0 = r20;
        r1 = r21;
        r15 = r0.getLast(r1, r4);
        if (r15 != 0) goto L_0x0011;
    L_0x0010:
        return;
    L_0x0011:
        r0 = r21;
        r19 = r0.getSpanStart(r15);
        r4 = -1;
        r0 = r19;
        if (r0 == r4) goto L_0x0010;
    L_0x001c:
        r0 = r21;
        r0.removeSpan(r15);
        r0 = r19;
        if (r0 == r13) goto L_0x0010;
    L_0x0025:
        r12 = r15;
        r12 = (com.flyersoft.staticlayout.HtmlToSpannedConverter.Font) r12;
        r4 = r12.mColor;
        r4 = com.flyersoft.books.T.isNull(r4);
        if (r4 != 0) goto L_0x0079;
    L_0x0030:
        r4 = r12.has_fill_color;
        if (r4 != 0) goto L_0x0040;
    L_0x0034:
        r4 = com.flyersoft.staticlayout.HtmlToSpannedConverter.TextFillColor.class;
        r0 = r20;
        r1 = r21;
        r4 = r0.getLast(r1, r4);
        if (r4 != 0) goto L_0x0079;
    L_0x0040:
        r4 = r12.mColor;
        r5 = "@";
        r4 = r4.startsWith(r5);
        if (r4 == 0) goto L_0x00b5;
    L_0x004a:
        r17 = android.content.res.Resources.getSystem();
        r4 = r12.mColor;
        r5 = 1;
        r14 = r4.substring(r5);
        r4 = "color";
        r5 = "android";
        r0 = r17;
        r11 = r0.getIdentifier(r14, r4, r5);
        if (r11 == 0) goto L_0x0079;
    L_0x0061:
        r0 = r17;
        r8 = r0.getColorStateList(r11);
        r4 = new android.text.style.TextAppearanceSpan;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r9 = 0;
        r4.<init>(r5, r6, r7, r8, r9);
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r0.setSpan(r1, r4, r2, r13);
    L_0x0079:
        r4 = r12.mFace;
        if (r4 == 0) goto L_0x0092;
    L_0x007d:
        r4 = r12.mFace;
        r0 = r20;
        r18 = r0.getTypefaceSpan(r4);
        if (r18 == 0) goto L_0x0092;
    L_0x0087:
        r0 = r20;
        r1 = r21;
        r2 = r18;
        r3 = r19;
        r0.setSpan(r1, r2, r3, r13);
    L_0x0092:
        r4 = r12.mSize;
        r5 = 0;
        r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
        if (r4 == 0) goto L_0x0010;
    L_0x0099:
        r16 = new com.flyersoft.staticlayout.MyRelativeSizeSpan;
        r4 = r12.mSize;
        r0 = r16;
        r0.<init>(r4);
        r4 = r12.fontSize_inherited;
        r0 = r16;
        r0.inherited = r4;
        r0 = r20;
        r1 = r21;
        r2 = r16;
        r3 = r19;
        r0.setSpan(r1, r2, r3, r13);
        goto L_0x0010;
    L_0x00b5:
        r4 = r12.mColor;
        r4 = r4.length();
        if (r4 <= 0) goto L_0x0079;
    L_0x00bd:
        r4 = r12.mColor;
        r10 = com.flyersoft.books.T.getHtmlColor(r4);
        if (r10 == 0) goto L_0x0079;
    L_0x00c5:
        r4 = r10.intValue();
        if (r4 == 0) goto L_0x0010;
    L_0x00cb:
        r4 = r10.intValue();
        r5 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        if (r4 == r5) goto L_0x0010;
    L_0x00d3:
        r4 = new android.text.style.ForegroundColorSpan;
        r5 = r10.intValue();
        r4.<init>(r5);
        r0 = r20;
        r1 = r21;
        r2 = r19;
        r0.setSpan(r1, r4, r2, r13);
        goto L_0x0079;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.staticlayout.HtmlToSpannedConverter.endFont(android.text.SpannableStringBuilder):void");
    }

    private MetricAffectingSpan getTypefaceSpan(String name) {
        if (name.equals("sans-serif") || name.equals(A.DEFAULT_FONTFACE) || name.equals("monospace")) {
            return new TypefaceSpan(name);
        }
        if (A.useCssFont) {
            return new MyTypefaceSpan(name);
        }
        return null;
    }

    private void startA(SpannableStringBuilder text, Attributes attributes) {
        String href = attributes.getValue("", "href");
        if (href == null && A.ebook != null) {
            href = attributes.getValue("", "filepos");
            if (href != null) {
                href = "@mobi" + href;
            }
        }
        int len = text.length();
        text.setSpan(new Href(href, attributes), len, len, 17);
    }

    private void endA(SpannableStringBuilder text) {
        int len = text.length();
        Href obj = (Href) getLast(text, Href.class);
        if (obj != null) {
            int where = text.getSpanStart(obj);
            text.removeSpan(obj);
            if (where != len) {
                Href h = obj;
                if (h.mHref != null) {
                    MyUrlSpan url = new MyUrlSpan(h.mHref);
                    CSS css = getChapter(this.chapterId) != null ? getChapter(this.chapterId).css : null;
                    if (css != null) {
                        url.link_color = css.link_color;
                        url.link_visited_color = css.link_visited_color;
                        url.link_no_underline = css.link_no_underline;
                    }
                    Style style = getStyleFromAttr("a", h.mAttributes);
                    if (style != null) {
                        Integer color = T.getHtmlColor(style.color);
                        if (color != null) {
                            url.link_color = color;
                        }
                        if (CSS.urlNoUnderline(style.css_text)) {
                            url.link_no_underline = true;
                        }
                    }
                    setSpan(text, url, where, len);
                }
            }
        }
    }

    private void endHeader(SpannableStringBuilder text) {
        int len = text.length();
        Header obj = (Header) getLast(text, Header.class);
        int where = text.getSpanStart(obj);
        text.removeSpan(obj);
        Header h = obj;
        if (h.style == null) {
            while (len > where && text.charAt(len - 1) == '\n') {
                len--;
            }
            if (where != len) {
                MyRelativeSizeSpan repl = new MyRelativeSizeSpan(h.fontSize);
                repl.inherited = true;
                setSpan(text, repl, where, len);
                if (MyHtml.familyBold != null) {
                    setSpan(text, new MyTypefaceSpan(MyHtml.familyBold), where, len);
                } else {
                    setSpan(text, new StyleSpan(1), where, len);
                }
            }
        }
        if (A.trimBlankSpace) {
            handleBr2(text);
        }
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        String tag = localName.toLowerCase();
        handleStartTag(tag, attributes);
        if (tag.equals("table")) {
            if (this.tableLevel == 0) {
                this.tableStart = this.spanBulder.length();
                this.tableSp = new MyTableSpan();
                this.tableList = new ArrayList();
            }
            this.tableLevel++;
            MyTableSpan myTableSpan = this.tableSp;
            myTableSpan.level++;
        }
        if (this.tableStart != -1) {
            this.tableList.add(new TableElement(tag, attributes, 0));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        String tag = localName.toLowerCase();
        handleEndTag(tag);
        if (this.tableStart != -1) {
            this.tableList.add(new TableElement(tag, null, 1));
            if (tag.equals("table")) {
                if (this.tableLevel == 1) {
                    dealTableContent();
                    this.tableStart = -1;
                    this.tableList = null;
                    this.tableSp = null;
                    this.tableIndexInHtml++;
                }
                this.tableLevel--;
            }
        }
    }

    private void dealTableContent() {
        int i;
        boolean tr = false;
        boolean td = false;
        boolean td2 = false;
        int col = 1;
        ArrayList<TableElement> td1List = new ArrayList();
        for (i = 0; i < this.tableList.size(); i++) {
            TableElement e = (TableElement) this.tableList.get(i);
            if (e.type == 2) {
                if (td) {
                    if (td2) {
                        td1List.add(e);
                        e.tdText = e.tag;
                        td2 = false;
                    } else {
                        e.tdText = ((TableElement) td1List.get(td1List.size() - 1)).tdText + e.tag;
                        td1List.set(td1List.size() - 1, e);
                    }
                }
            } else if (e.tag.equals("tr")) {
                tr = true;
                td = false;
                if (e.type == 0) {
                    col = 0;
                }
            } else if (e.tag.equals("td")) {
                if (tr) {
                    td2 = true;
                    td = true;
                    tr = false;
                } else {
                    td2 = false;
                    td = false;
                }
                if (e.type == 0) {
                    col++;
                }
            }
        }
        if (td1List.size() > 1 && col > 1) {
            this.spanBulder.delete(this.tableStart, this.spanBulder.length());
            ArrayList<Float> td1Width = new ArrayList();
            float max = 0.0f;
            int pw = A.getPageWidth();
            for (i = 0; i < td1List.size(); i++) {
                String s = ((TableElement) td1List.get(i)).tdText;
                if (s.contains("\n")) {
                    td1Width.add(Float.valueOf(0.0f));
                } else {
                    float w1 = ((TableElement) td1List.get(i)).tag.length() > 50 ? (float) (pw / 2) : Layout.getDesiredWidth(s, A.txtView.getPaint());
                    td1Width.add(Float.valueOf(w1));
                    if (w1 > max) {
                        max = w1;
                    }
                }
            }
            float max2 = col < 3 ? (float) (pw / 2) : (float) ((pw * 2) / 5);
            if (max > max2) {
                max = max2;
            }
            int fit = 0;
            for (i = 0; i < td1Width.size(); i++) {
                if (((Float) td1Width.get(i)).floatValue() > max / 4.0f) {
                    fit++;
                }
            }
            if (fit > 1) {
                float ap = Layout.getDesiredWidth(" ", A.txtView.getPaint());
                StringBuilder sb = new StringBuilder();
                i = 0;
                while (i < td1List.size()) {
                    if (((Float) td1Width.get(i)).floatValue() > 0.0f && ((Float) td1Width.get(i)).floatValue() < max) {
                        int add = Math.round((max - ((Float) td1Width.get(i)).floatValue()) / ap);
                        if (add > 0) {
                            sb.setLength(0);
                            for (int j = 0; j < add; j++) {
                                sb.append(' ');
                            }
                            ((TableElement) td1List.get(i)).tag += sb.toString();
                        }
                    }
                    i++;
                }
            }
            for (i = 0; i < this.tableList.size(); i++) {
//                e = (TableElement) this.tableList.get(i);
//                if (e.type == 0) {
//                    handleStartTag(e.tag, e.attr);
//                } else if (e.type == 1) {
//                    handleEndTag(e.tag);
//                } else {
//                    this.spanBulder.append(e.tag);
//                }
            }
        }
        this.tableSp.spStart = this.tableStart;
        this.tableSp.spEnd = this.spanBulder.length();
        this.tableSp.tableIndex = this.tableIndexInHtml;
        this.tableSp.html = getTableHtml(this.tableSp.tableIndex);
        setSpan(this.spanBulder, this.tableSp, this.tableSp.spStart, this.tableSp.spEnd);
    }

    private String getTableHtml(int tableIndex) {
        initTabletHtmls();
        if (tableIndex < this.tableHtmls.size()) {
            return (String) this.tableHtmls.get(tableIndex);
        }
        return null;
    }

    private void initTabletHtmls() {
        if (this.tableHtmls == null) {
            this.tableHtmls = new ArrayList();
            String s = this.mSource.toLowerCase();
            int level = 0;
            int start = 0;
            int len = s.length() - 7;
            int i1 = s.indexOf("<table");
            int i2 = s.indexOf("<tr");
            if (i2 == -1 || (i1 != -1 && i1 <= i2)) {
                i2 = s.indexOf("<td");
                if (i2 != -1 && (i1 == -1 || i1 > i2)) {
                    level = 1;
                    start = i2;
                }
            } else {
                level = 1;
                start = i2;
            }
            int i = 0;
            int level2 = level;
            while (i < len) {
                int i3 = i + 1;
                if (s.charAt(i) == '<') {
                    if (s.charAt(i3) == 't') {
                        i3++;
                        i = i3 + 1;
                        if (s.charAt(i3) == 'a') {
                            i3 = i + 1;
                            if (s.charAt(i) == 'b') {
                                i = i3 + 1;
                                if (s.charAt(i3) == 'l') {
                                    i3 = i + 1;
                                    if (s.charAt(i) == 'e') {
                                        level = level2 + 1;
                                        if (level2 == 0) {
                                            start = i3 - 6;
                                            i = i3;
                                            level2 = level;
                                        } else {
                                            i = i3;
                                            level2 = level;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (s.charAt(i3) == '/') {
                        i3++;
                        i = i3 + 1;
                        if (s.charAt(i3) == 't') {
                            i3 = i + 1;
                            if (s.charAt(i) == 'a') {
                                i = i3 + 1;
                                if (s.charAt(i3) == 'b') {
                                    i3 = i + 1;
                                    if (s.charAt(i) == 'l') {
                                        i = i3 + 1;
                                        if (s.charAt(i3) == 'e') {
                                            i3 = i + 1;
                                            if (s.charAt(i) == '>') {
                                                if (level2 - 1 == 0) {
                                                    this.tableHtmls.add(this.mSource.substring(start, i3));
                                                }
                                                if (level2 > 0) {
                                                    i = i3;
                                                    level2--;
                                                } else {
                                                    i = i3;
                                                    level2 = 0;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        i = i3 + 1;
                    }
                }
                i = i3;
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.rubyRtStart) {
            this.rubyRtText += String.copyValueOf(ch, start, length);
        } else if (!this.mediaTagStart && !this.ignoreStart) {
            int i;
            StringBuilder sb = new StringBuilder();
            SpannableStringBuilder s = this.spanBulder;
            for (i = 0; i < length; i++) {
                char c = ch[i + start];
                if (this.preTagStart && (c == '\n' || c == ' ')) {
                    sb.append(c);
                } else if (c == ' ' || c == '\n' || c == '\t') {
                    char pred;
                    if (sb.length() != 0) {
                        pred = sb.charAt(sb.length() - 1);
                    } else if (s.length() == 0) {
                        pred = '\n';
                    } else {
                        pred = s.charAt(s.length() - 1);
                    }
                    if (!(pred == ' ' || pred == '\n')) {
                        sb.append(' ');
                    }
                } else if (c != A.INDENT_CHAR || !A.indentParagraph || sb.length() != 0) {
                    sb.append(c);
                }
            }
            if (sb.length() > 0) {
                boolean allow = true;
                if (this.floatSpanType > 0 || (A.trimBlankSpace && sb.length() < 5 && s.length() >= 1 && s.charAt(s.length() - 1) == '\n')) {
                    allow = false;
                    for (i = 0; i < sb.length(); i++) {
                        if (!A.isEmptyChar(sb.charAt(i))) {
                            allow = true;
                            break;
                        }
                    }
                }
                if (allow) {
                    s.append(sb);
                    if (this.tableStart != -1) {
                        this.tableList.add(new TableElement(sb.toString(), null, 2));
                    }
                    this.floatSpanType = 0;
                }
            }
        }
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
    }

    public void skippedEntity(String name) throws SAXException {
    }
}