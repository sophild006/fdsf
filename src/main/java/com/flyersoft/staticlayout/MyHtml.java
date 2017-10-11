package com.flyersoft.staticlayout;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spanned;
import com.flyersoft.books.A;
import com.flyersoft.books.BaseEBook.Chapter;
import com.flyersoft.books.T;
import com.flyersoft.components.CSS;
import com.flyersoft.components.CSS.Style;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class MyHtml {
    public static final String HR_TAG = "───";
    public static String LI_TAG = "•　";
    public static final String PAGE_BREAK = "<hr2>";
    static int cacheStylesCount;
    public static String familyBold;
    public static String familyBoldItalic;
    public static String familyItalic;
    public static boolean hasRuby;
    static int tagCount;

    public interface MyImageGetter {
        Drawable getDrawable(String str, boolean z);

        Rect getDrawableBounds(String str, boolean z);
    }

    public static class Emphasis {
        public String emphasis;

        public Emphasis(String emphasis) {
            this.emphasis = emphasis;
        }
    }

    private static class HtmlParser {
        private static final HTMLSchema schema = new HTMLSchema();

        private HtmlParser() {
        }
    }

    public static class Ruby {
        public int end;
        public String original;
        public String rt;
        public int start;

        public Ruby(String rt, String original, int start, int end) {
            this.rt = rt;
            this.original = original;
            this.start = start;
            this.end = end;
        }
    }

    private static class StyleMap {
        boolean basic;
        String section;
        int value;

        public StyleMap(int value, boolean basic, String section) {
            this.value = value;
            this.basic = basic;
            this.section = section;
        }
    }

    private static class TagClassValueComparable implements Comparator<StyleMap> {
        private TagClassValueComparable() {
        }

        public int compare(StyleMap o1, StyleMap o2) {
            return o1.value < o2.value ? -1 : 1;
        }
    }

    public interface TagHandler {
        void handleTag(boolean z, String str, Editable editable, XMLReader xMLReader);
    }

    public static void initFamilyFontParams() {
        familyBoldItalic = null;
        familyItalic = null;
        familyBold = null;
        String font = T.getOnlyFilename(A.fontName);
        int i = font.indexOf("-");
        if (i != -1) {
            font = font.substring(0, i);
        }
        String path = null;
        if (A.fontName.startsWith("/")) {
            path = T.getFilePath(A.fontName);
        } else if (T.isFile(A.outerFontsFolder + "/" + A.fontName + ".ttf")) {
            path = A.outerFontsFolder;
        } else if (T.isFile(A.SYSTEM_FONT_PATH + "/" + A.fontName + ".ttf")) {
            path = A.SYSTEM_FONT_PATH;
        }
        if (path != null) {
            if (T.isFile(path + "/" + font + "-Bold.ttf")) {
                familyBold = path + "/" + font + "-Bold.ttf";
            }
            if (T.isFile(path + "/" + font + "-Italic.ttf")) {
                familyItalic = path + "/" + font + "-Italic.ttf";
            }
            if (T.isFile(path + "/" + font + "-BoldItalic.ttf")) {
                familyBoldItalic = path + "/" + font + "-BoldItalic.ttf";
            }
        }
    }

    private MyHtml() {
    }

    public static Spanned fromHtml(String source) {
        return fromHtml(source, null, -1);
    }

    public static Spanned fromHtml(String source, MyImageGetter imageGetter, TagHandler tagHandler, int chapterId) {
        hasRuby = false;
        Parser parser = new Parser();
        try {
            parser.setProperty(Parser.schemaProperty, HtmlParser.schema);
            cacheStylesCount = 0;
            tagCount = 0;
            long t = System.currentTimeMillis();
            Spanned sp = new HtmlToSpannedConverter(source, imageGetter, tagHandler, parser, chapterId).convert();
            A.log("----------fromHtml scan time:" + (System.currentTimeMillis() - t) + ", tags count: " + tagCount + ", source length:" + source.length() + ", new cache styles:" + cacheStylesCount);
            return sp;
        } catch (SAXNotRecognizedException e) {
            throw new RuntimeException(e);
        } catch (SAXNotSupportedException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static Spanned fromHtml(String source, MyImageGetter imageGetter, int chapterId) {
        return fromHtml(source, imageGetter, null, chapterId);
    }

    public static Style getClassStyle(String tag, String idName, String className, Chapter c, ArrayList<String> tags, ArrayList<String> classes) {
        if (c == null) {
            return null;
        }
        CSS css = c.css;
        if (css == null || css.styles.size() == 0) {
            return null;
        }
        Style idStyle = idName != null ? (Style) css.styles.get("#" + idName) : null;
        if (idStyle == null) {
            idName = null;
        }
        int secCache = getSecCacheName(tags, classes, tag, className, idName);
        if (css.cacheStyles.containsKey(Integer.valueOf(secCache))) {
            return (Style) css.cacheStyles.get(Integer.valueOf(secCache));
        }
        Style cssStyle = null;
        ArrayList<StyleMap> available = new ArrayList();
        boolean hasPreTags = tags != null && (tags.size() > 1 || (tags.size() == 1 && !((String) tags.get(0)).equals(tag)));
        if (hasPreTags) {
            for (String sec : css.styles.keySet()) {
                if (sec.endsWith(" " + tag) && secFitPriorTags(sec, tag, null, tags, classes, c)) {
                    available.add(new StyleMap(0, false, sec));
                }
            }
        } else if (css.styles.containsKey("body " + tag)) {
            available.add(new StyleMap(0, false, "body " + tag));
        }
        if (className != null) {
            boolean higher;
            String[] names = className.split(" +");
            for (String name : names) {
                if (name.length() > 0) {
                    cssStyle = appendStyle(cssStyle, (Style) css.styles.get("." + name));
                    if (css.styles.containsKey(tag + "." + name)) {
                        available.add(new StyleMap(10, true, tag + "." + name));
                    }
                    if (hasPreTags) {
                        for (String sec2 : css.styles.keySet()) {
                            higher = sec2.endsWith(" " + tag + "." + name);
                            if ((higher || sec2.endsWith(" ." + name)) && secFitPriorTags(sec2, tag, name, tags, classes, c)) {
                                available.add(new StyleMap(higher ? 11 : 1, false, sec2));
                            }
                        }
                    }
                }
            }
            if (names.length > 1) {
                String wholeClass = getWholeClassName(names);
                if (css.styles.containsKey(wholeClass)) {
                    available.add(new StyleMap(20, true, wholeClass));
                }
                if (css.styles.containsKey(tag + wholeClass)) {
                    available.add(new StyleMap(30, true, tag + wholeClass));
                }
                if (hasPreTags) {
                    for (String sec22 : css.styles.keySet()) {
                        higher = sec22.endsWith(" " + tag + wholeClass);
                        if ((higher || sec22.endsWith(" " + wholeClass)) && secFitPriorTags(sec22, tag, wholeClass, tags, classes, c)) {
                            available.add(new StyleMap(higher ? 31 : 21, false, sec22));
                        }
                    }
                }
            }
        }
        if (available.size() > 1) {
            int i;
            for (i = 0; i < available.size(); i++) {
                if (!((StyleMap) available.get(i)).basic) {
                    StyleMap styleMap = (StyleMap) available.get(i);
                    styleMap.value = getTagClassValue(((StyleMap) available.get(i)).section, tag, className, tags, classes) + styleMap.value;
                }
            }
            Collections.sort(available, new TagClassValueComparable());
            for (i = 0; i < available.size(); i++) {
                cssStyle = appendStyle(cssStyle, (Style) css.styles.get(((StyleMap) available.get(i)).section));
            }
        } else if (available.size() == 1) {
            cssStyle = appendStyle(cssStyle, (Style) css.styles.get(((StyleMap) available.get(0)).section));
        }
        cssStyle = appendStyle((Style) css.styles.get(tag), cssStyle);
        if (cssStyle != null && css.styles.containsKey("*")) {
            cssStyle = appendStyle((Style) css.styles.get("*"), cssStyle);
        }
        cssStyle = appendStyle(cssStyle, idStyle);
        if (!(cssStyle == null || css.styles.containsValue(cssStyle))) {
            cssStyle.name = tag + "@" + className;
        }
        css.cacheStyles.put(Integer.valueOf(secCache), cssStyle);
        cacheStylesCount++;
        return cssStyle;
    }

    private static int getSecCacheName(ArrayList<String> tags, ArrayList<String> classes, String tag, String className, String idName) {
        int hash = tag.hashCode();
        if (className != null) {
            hash += className.hashCode() * 123;
        }
        if (idName != null) {
            hash += idName.hashCode();
        }
        if (tags == null || tags.size() <= 0) {
            return hash;
        }
        int off;
        int len = tags.size();
        if (tag.equals(tags.get(len - 1))) {
            off = 1;
        } else {
            off = 0;
        }
        if (len > off) {
            hash += ((String) tags.get((len - 1) - off)).hashCode() * len;
            if (classes.get((len - 1) - off) != null) {
                hash += ((String) classes.get((len - 1) - off)).hashCode();
            }
        }
        if (len <= off + 1) {
            return hash;
        }
        hash += ((String) tags.get(0)).hashCode();
        if (classes.get(0) != null) {
            return hash + ((String) classes.get(0)).hashCode();
        }
        return hash;
    }

    private static String getWholeClassName(String[] classes2) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < classes2.length; i++) {
            if (classes2[i].length() > 0) {
                sb.append("." + classes2[i]);
            }
        }
        return sb.toString();
    }

    private static boolean secFitPriorTags(String sec, String tag, String className, ArrayList<String> tags, ArrayList<String> classes, Chapter c) {
        if (sec.startsWith("html") || sec.startsWith("body") || (c.css_body_class != null && sec.startsWith("." + c.css_body_class))) {
            sec = sec.substring(sec.indexOf(32) + 1);
            if (!sec.contains(" ")) {
                return nextEqualsTagClass(sec, tag, className);
            }
        }
        int last = tags.size();
        last = ((String) tags.get(last + -1)).equals(tag) ? last - 2 : last - 1;
        int i = 0;
        while (i <= last) {
            int j;
            String tag2 = (String) tags.get(i);
            if (classes.get(i) != null) {
                String[] classes2 = ((String) classes.get(i)).split(" +");
                if (classes2.length > 1) {
                    if (step2Fit(sec.indexOf(tag2 + getWholeClassName(classes2) + " "), sec, tag, className, i, last, tags, classes)) {
                        return true;
                    }
                    j = sec.indexOf(getWholeClassName(classes2) + " ");
                    if ((j == 0 || (j > 0 && sec.charAt(j - 1) == ' ')) && step2Fit(j, sec, tag, className, i, last, tags, classes)) {
                        return true;
                    }
                }
                for (String c2 : classes2) {
                    if (c2.length() > 0) {
                        if (step2Fit(sec.indexOf(tag2 + "." + c2 + " "), sec, tag, className, i, last, tags, classes)) {
                            return true;
                        }
                        j = sec.indexOf("." + c2 + " ");
                        if ((j == 0 || (j > 0 && sec.charAt(j - 1) == ' ')) && step2Fit(j, sec, tag, className, i, last, tags, classes)) {
                            return true;
                        }
                    }
                }
            }
            j = sec.indexOf(tag2 + " ");
            if ((j == 0 || (j > 0 && sec.charAt(j - 1) == ' ')) && step2Fit(j, sec, tag, className, i, last, tags, classes)) {
                return true;
            }
            i++;
        }
        return false;
    }

    private static boolean step2Fit(int j, String sec, String tag, String className, int cur, int last, ArrayList<String> tags, ArrayList<String> classes) {
        if (j == -1) {
            return false;
        }
        String tag2;
        String wholeClass;
        if (j != 0) {
            if (cur == 0) {
                return false;
            }
            boolean ok = false;
            String pre = getPreTagName(sec, j);
            if (pre != null) {
                for (int k = 0; k < cur; k++) {
                    tag2 = (String) tags.get(k);
                    if (classes.get(k) != null) {
                        if (!pre.equals(tag2)) {
                            String[] classes2 = ((String) classes.get(k)).split(" +");
                            if (classes2.length > 1) {
                                wholeClass = getWholeClassName(classes2);
                                if (pre.equals(wholeClass) || pre.equals(tag2 + wholeClass)) {
                                    ok = true;
                                    break;
                                }
                            }
                            for (String c2 : classes2) {
                                if (pre.equals("." + c2) || pre.equals(tag2 + "." + c2)) {
                                    ok = true;
                                    break;
                                }
                            }
                            if (ok) {
                                break;
                            }
                        } else {
                            ok = true;
                            break;
                        }
                    } else if (pre.equals(tag2)) {
                        ok = true;
                        break;
                    }
                }
            }
            if (!ok) {
                return false;
            }
        }
        if (cur == last) {
            int i = sec.indexOf(">");
            if (i != -1) {
                return sec.substring(i + 1).trim().equals(tag);
            }
        }
        String next = sec.substring(sec.indexOf(32, j) + 1);
        while (next.startsWith(" ")) {
            next = next.substring(1);
        }
        if (next.startsWith(">")) {
            next = sec.substring(1);
            while (next.startsWith(" ")) {
                next = next.substring(1);
            }
        }
        if (nextEqualsTagClass(next, tag, className)) {
            return true;
        }
        if (cur == last) {
            return false;
        }
        next = next.substring(0, CSS.nextSplit(next, 0, true));
        tag2 = (String) tags.get(cur + 1);
        if (classes.get(cur + 1) != null) {
            wholeClass = getWholeClassName(((String) classes.get(cur + 1)).split(" +"));
            if (next.equals(wholeClass) || next.equals(tag2 + wholeClass)) {
                return true;
            }
        } else if (next.equals(tag2)) {
            return true;
        }
        return false;
    }

    private static boolean nextEqualsTagClass(String next, String tag, String className) {
        if (next.equals(tag)) {
            return true;
        }
        if (className != null) {
            if (!className.startsWith(".")) {
                className = "." + className;
            }
            if (next.equals(className) || next.equals(tag + className)) {
                return true;
            }
        }
        return false;
    }

    private static String getPreTagName(String sec, int j) {
        int i2 = sec.lastIndexOf(32, j);
        if (i2 > 0) {
            int i1 = sec.lastIndexOf(32, i2 - 1);
            i1 = i1 == -1 ? 0 : i1 + 1;
            if (i2 > i1) {
                String pre = sec.substring(i1, i2);
                if (pre.equals(">")) {
                    return getPreTagName(sec, i1);
                }
                return pre.substring(0, CSS.nextSplit(pre, 0, true));
            }
        }
        return null;
    }

    private static int getTagClassValue(String sec, String tag, String className, ArrayList<String> tags, ArrayList<String> classes) {
        int value = 0;
        for (int i = 0; i < tags.size() - 1; i++) {
            String tag2 = (String) tags.get(i);
            if (classes.get(i) != null) {
                int old = value;
                String[] classes2 = ((String) classes.get(i)).split(" +");
                if (classes2.length > 1) {
                    String wholeClass = getWholeClassName(classes2);
                    if (sec.contains(tag2 + wholeClass + " ")) {
                        value += i + 3;
                    } else if (sec.contains(wholeClass + " ")) {
                        value += i + 2;
                    }
                }
                if (old == value) {
                    for (String c2 : classes2) {
                        if (c2.length() > 0) {
                            if (sec.contains(tag2 + "." + c2 + " ")) {
                                value += i + 1;
                            } else if (sec.contains("." + c2 + " ")) {
                                value += i;
                            }
                        }
                    }
                }
            } else if (sec.contains(tag2 + " ")) {
                value += i;
            }
        }
        return value;
    }

    public static Style appendStyle(Style style1, Style style2) {
        if (style1 == null) {
            return style2;
        }
        if (style2 == null) {
            return style1;
        }
        Style style = new Style(style1);
        style.combineStyle(style2);
        return style;
    }
}