package com.flyersoft.components;

import android.graphics.RectF;
import android.text.Layout;
import com.flyersoft.books.A;
import com.flyersoft.books.T;
import java.util.ArrayList;
import java.util.HashMap;

public class CSS {
    public static final int ALIGN_CENTER = 3;
    public static final int ALIGN_JUSTIFY = 2;
    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 4;
    public static final float FLOAT_ADDED = 6.0f;
    public static final float KEEP_MAGIN = 0.35f;
    public static final int LINK_COLOR = -10460956;
    public static final float ZERO_FONTSIZE = 0.001f;
    public static Float em2 = null;
    public static final float no_margin = -999.0f;
    public HashMap<Integer, Style> cacheStyles = new HashMap();
    public Integer link_color;
    public boolean link_no_underline;
    public Integer link_visited_color;
    public HashMap<String, Style> styles = new HashMap();

    public static class BackgroundColorSpan {
        public int color;

        public BackgroundColorSpan(int color) {
            this.color = color;
        }
    }

    public static class PAGE_BREAK {
    }

    public static class Style {
        public int align;
        public String background;
        public String backgroundColor;
        public String backgroundImage;
        public String backgroundPosition;
        public boolean bold;
        public RectF border = CSS.emptyMargin();
        public String[] borderColor;
        public Float borderRadius;
        public String[] borderStyle;
        public String color;
        public String css_text;
        public String display;
        public String emphasis_style;
        public String floatSp;
        public String fontFace;
        public float fontSize;
        public boolean fontSizeInherited;
        public boolean fontStyleNormal;
        public String fontWeight;
        public int htmlEndPos;
        public int htmlStartPos;
        public Float indent;
        public boolean italic;
        public RectF margin = CSS.emptyMargin();
        public RectF marginInherited = CSS.emptyMargin();
        public String maxWidth;
        public String name;
        public RectF padding = CSS.emptyMargin();
        public RectF paddingInherited = CSS.emptyMargin();
        public boolean pageBreak;
        public boolean shadow;
        public boolean strikethrough;
        public String text_fill_color;
        public String ul_style;
        public boolean underline;
        public String width;

        class Border {
            int color = 2;
            int size = 0;
            final /* synthetic */ Style this$0;
            int type = 1;

            public Border(Style this$0, String[] v) {
                int i = 2;
                int i2 = 1;
                this.this$0 = this$0;
                if (this$0.isBorderSize(v[0])) {
                    if (!this$0.isCssColor(v[2])) {
                        this.color = 1;
                        this.type = 2;
                    }
                } else if (this$0.isBorderSize(v[1])) {
                    this.size = 1;
                    this.color = this$0.isCssColor(v[0]) ? 0 : 2;
                    if (this.color != 0) {
                        i = 0;
                    }
                    this.type = i;
                } else {
                    int i3;
                    this.size = 2;
                    if (this$0.isCssColor(v[0])) {
                        i3 = 0;
                    } else {
                        i3 = 1;
                    }
                    this.color = i3;
                    if (this.color != 0) {
                        i2 = 0;
                    }
                    this.type = i2;
                }
            }
        }

        public Style(Style from) {
            if (from != null) {
                combineStyle(from);
            }
        }

        public void combineStyle(Style from) {
            if (from.name != null) {
                this.name = from.name;
            }
            if (this.css_text == null) {
                this.css_text = from.css_text;
            } else if (from.css_text != null) {
                if (A.isBetaVersion || this.css_text.length() < 500) {
                    this.css_text += "\n\n" + from.css_text;
                } else {
                    this.css_text += "\n\n" + (from.css_text.length() < 100 ? from.css_text : from.css_text.substring(0, 100));
                }
            }
            if (from.align > 0) {
                this.align = from.align;
            }
            if (from.bold || from.fontWeight != null) {
                this.bold = from.bold;
            }
            if (from.fontStyleNormal) {
                this.fontStyleNormal = from.fontStyleNormal;
                this.italic = false;
            } else if (from.italic) {
                this.italic = from.italic;
            }
            if (from.underline) {
                this.underline = from.underline;
            }
            if (from.strikethrough) {
                this.strikethrough = from.strikethrough;
            }
            if (from.shadow) {
                this.shadow = from.shadow;
            }
            if (from.pageBreak) {
                this.pageBreak = from.pageBreak;
            }
            if (from.indent != null) {
                this.indent = from.indent;
            }
            if (from.text_fill_color != null) {
                String str = from.text_fill_color;
                this.text_fill_color = str;
                this.color = str;
            } else if (this.text_fill_color == null && from.color != null) {
                this.color = from.color;
            }
            if (from.fontSize > 0.0f) {
                this.fontSize = from.fontSize;
                this.fontSizeInherited = from.fontSizeInherited;
            }
            if (from.backgroundColor != null) {
                this.backgroundColor = from.backgroundColor;
            }
            if (from.fontFace != null) {
                this.fontFace = from.fontFace;
            }
            if (from.fontWeight != null) {
                this.fontWeight = from.fontWeight;
            }
            if (from.display != null) {
                this.display = from.display;
            }
            if (from.emphasis_style != null) {
                this.emphasis_style = from.emphasis_style;
            }
            if (from.ul_style != null) {
                this.ul_style = from.ul_style;
            }
            if (from.floatSp != null) {
                this.floatSp = from.floatSp;
            }
            if (from.width != null) {
                this.width = from.width;
            }
            if (from.maxWidth != null) {
                this.maxWidth = from.maxWidth;
            }
            mergeMargins(this.margin, from.margin);
            mergeMargins(this.marginInherited, from.marginInherited);
            mergeMargins(this.padding, from.padding);
            mergeMargins(this.paddingInherited, from.paddingInherited);
            mergeMargins(this.border, from.border);
            this.borderColor = mergeBorderStyle(this.borderColor, from.borderColor);
            this.borderStyle = mergeBorderStyle(this.borderStyle, from.borderStyle);
            if (from.borderRadius != null) {
                this.borderRadius = from.borderRadius;
            }
            if (from.background != null) {
                this.background = from.background;
            }
            if (from.backgroundImage != null) {
                this.backgroundImage = from.backgroundImage;
            }
            if (from.backgroundPosition != null) {
                this.backgroundPosition = from.backgroundPosition;
            }
        }

        private String[] mergeBorderStyle(String[] orig, String[] from) {
            if (from == null) {
                return orig;
            }
            if (orig == null) {
                orig = new String[4];
            }
            for (int i = 0; i < 4; i++) {
                if (from[i] != null) {
                    orig[i] = from[i];
                }
            }
            return orig;
        }

        private void mergeMargins(RectF orig, RectF from) {
            if (from.left != CSS.no_margin) {
                orig.left = from.left;
            }
            if (from.top != CSS.no_margin) {
                orig.top = from.top;
            }
            if (from.right != CSS.no_margin) {
                orig.right = from.right;
            }
            if (from.bottom != CSS.no_margin) {
                orig.bottom = from.bottom;
            }
        }

        public void scanPropertyForStyle(String property) {
            while (true) {
                int i1 = property.indexOf("//");
                if (i1 == -1) {
                    break;
                }
                int i2 = property.indexOf("\n", i1);
                if (i2 != -1) {
                    property = property.substring(0, i1) + property.substring(i2);
                } else {
                    property = property.substring(0, i1);
                }
            }
            if (this.css_text == null) {
                this.css_text = property;
            } else {
                this.css_text += "\n\n" + property;
            }
            try {
                int j;
                String value;
                property = property.replace("\r", "");
                if (!(A.disableCSS && A.cssFontStyle)) {
                    if (!this.bold) {
                        if (CSS.propertyTagIndex(property, "bold") != -1) {
                            this.bold = true;
                        }
                    }
                    if (!this.italic) {
                        if (CSS.propertyTagIndex(property, "italic") != -1) {
                            this.italic = true;
                        }
                    }
                    if (!this.italic) {
                        if (CSS.propertyTagIndex(property, "oblique") != -1) {
                            this.italic = true;
                        }
                    }
                    if (!this.underline) {
                        if (CSS.propertyTagIndex(property, "underline") != -1) {
                            this.underline = true;
                        }
                    }
                    if (!this.strikethrough) {
                        if (CSS.propertyTagIndex(property, "line-through") != -1) {
                            this.strikethrough = true;
                        }
                    }
                    j = CSS.propertyTagIndex(property, "text-shadow");
                    if (j != -1) {
                        if (!CSS.propertyTagValue(property, ("text-shadow".length() + j) + 1, true).contains("none")) {
                            this.shadow = true;
                        }
                    }
                    j = CSS.propertyTagIndex(property, "font-weight");
                    if (j != -1) {
                        this.fontWeight = CSS.propertyTagValue(property, ("font-weight".length() + j) + 1, true);
                        this.bold = CSS.isFontWeightBold(this.fontWeight);
                    }
                    j = CSS.propertyTagIndex(property, "font-style");
                    if (j != -1) {
                        if (CSS.propertyTagValue(property, ("font-style".length() + j) + 1, true).equals("normal")) {
                            this.fontStyleNormal = true;
                            this.italic = false;
                        }
                    }
                }
                j = CSS.propertyTagIndex(property, "text-align");
                if (j != -1) {
                    value = CSS.propertyTagValue(property, ("text-align".length() + j) + 1, true);
                    if (!(A.disableCSS && A.cssAlignment)) {
                        if (value.equals("center")) {
                            this.align = 3;
                        } else if (value.equals("right") || value.equals("end")) {
                            this.align = 4;
                        } else if (value.equals("left") || value.equals("start")) {
                            this.align = 1;
                        }
                    }
                    if (!(A.disableCSS && A.cssJustify) && value.equals("justify")) {
                        this.align = 2;
                    }
                }
                if (!(A.disableCSS && A.cssIndent)) {
                    j = CSS.propertyTagIndex(property, "text-indent");
                    if (j != -1) {
                        this.indent = Float.valueOf(CSS.getEmSize(CSS.propertyTagValue(property, ("text-indent".length() + j) + 1, true)));
                    }
                }
                if (!(A.disableCSS && A.cssFontColor)) {
                    j = CSS.propertyTagIndex(property, "color");
                    if (j != -1) {
                        this.color = CSS.propertyTagValue(property, ("color".length() + j) + 1, true);
                    }
                    j = CSS.propertyTagIndex(property, "-webkit-text-fill-color");
                    if (j != -1) {
                        value = CSS.propertyTagValue(property, ("-webkit-text-fill-color".length() + j) + 1, true);
                        if (!(value.equals("transparent") || T.getHtmlColor(value) == null)) {
                            this.text_fill_color = value;
                            this.color = this.text_fill_color;
                        }
                    }
                    j = CSS.propertyTagIndex(property, "background-color");
                    if (j != -1) {
                        this.backgroundColor = CSS.propertyTagValue(property, ("background-color".length() + j) + 1, true);
                    }
                }
                j = CSS.propertyTagIndex(property, "display");
                if (j != -1) {
                    this.display = CSS.propertyTagValue(property, ("display".length() + j) + 1, true);
                }
                j = CSS.propertyTagIndex(property, "text-emphasis-style", true);
                if (j != -1) {
                    this.emphasis_style = get_emphasis_style(CSS.propertyTagValue(property, ("text-emphasis-style".length() + j) + 1, false));
                }
                j = CSS.propertyTagIndex(property, "list-style-type");
                if (j != -1) {
                    this.ul_style = CSS.propertyTagValue(property, ("list-style-type".length() + j) + 1, true);
                } else {
                    j = CSS.propertyTagIndex(property, "list-style");
                    if (j != -1) {
                        this.ul_style = CSS.propertyTagValue(property, ("list-style".length() + j) + 1, true);
                    }
                }
                j = CSS.propertyTagIndex(property, "width");
                if (j != -1) {
                    this.width = CSS.propertyTagValue(property, ("width".length() + j) + 1, true);
                }
                j = CSS.propertyTagIndex(property, "max-width");
                if (j != -1) {
                    this.maxWidth = CSS.propertyTagValue(property, ("max-width".length() + j) + 1, true);
                }
                if (A.useCssFont) {
                    j = CSS.propertyTagIndex(property, "font-family");
                    if (j != -1) {
                    }
                }
                if (!(A.disableCSS && A.cssFontSize)) {
                    j = CSS.propertyTagIndex(property, "font-size");
                    if (j != -1) {
                        value = CSS.propertyTagValue(property, ("font-size".length() + j) + 1, false);
                        this.fontSize = CSS.getCssSize(value, true);
                        this.fontSizeInherited = CSS.isInheritedSize(value);
                    } else {
                        j = CSS.propertyTagIndex(property, "font");
                        if (j != -1) {
                            value = CSS.propertyTagValue(property, ("font".length() + j) + 1, false);
                            String[] values = value.split(" +");
                            int i = 0;
                            while (i < 4 && i < values.length) {
                                String s = values[i];
                                if (s.length() > 2) {
                                    char c = s.charAt(0);
                                    if (c == '.' || (c >= '0' && c <= '9')) {
                                        float size = CSS.getCssSize(s, true);
                                        if (size > 0.0f) {
                                            this.fontSize = size;
                                            this.fontSizeInherited = CSS.isInheritedSize(value);
                                        }
                                    }
                                }
                                i++;
                            }
                        }
                    }
                }
                if (!(A.disableCSS && A.cssLineSpace)) {
                    getMarginPaddingValue(property);
                    j = CSS.propertyTagIndex(property, "page-break-after");
                    if (j != -1) {
                        this.pageBreak = "always".equals(CSS.propertyTagValue(property, ("page-break-after".length() + j) + 1, true));
                    }
                }
                if (!A.disableCSS || !A.cssOthers) {
                    getBorderAndBackground(property);
                    j = CSS.propertyTagIndex(property, "float");
                    if (j != -1) {
                        value = CSS.propertyTagValue(property, ("float".length() + j) + 1, true);
                        if (value.equals("left") || value.equals("right")) {
                            this.floatSp = value;
                        }
                    }
                }
            } catch (Exception e) {
                A.error(e);
            }
        }

        private void getMarginPaddingValue(String property) {
            String[] values;
            RectF rectF;
            RectF rectF2;
            float emSize;
            RectF rectF3;
            String value;
            int j = CSS.propertyTagIndex(property, "margin");
            if (j != -1) {
                values = CSS.propertyTagValue(property, ("margin".length() + j) + 1, false).split(" +");
                if (values.length == 4) {
                    this.margin.top = CSS.getEmSize(values[0]);
                    if (CSS.isInheritedSize(values[0])) {
                        this.marginInherited.top = 1.0f;
                    }
                    this.margin.right = CSS.getEmSize(values[1]);
                    if (CSS.isInheritedSize(values[1])) {
                        this.marginInherited.right = 1.0f;
                    }
                    this.margin.bottom = CSS.getEmSize(values[2]);
                    if (CSS.isInheritedSize(values[2])) {
                        this.marginInherited.bottom = 1.0f;
                    }
                    this.margin.left = CSS.getEmSize(values[3]);
                    if (CSS.isInheritedSize(values[3])) {
                        this.marginInherited.left = 1.0f;
                    }
                } else if (values.length == 3) {
                    this.margin.top = CSS.getEmSize(values[0]);
                    if (CSS.isInheritedSize(values[0])) {
                        this.marginInherited.top = 1.0f;
                    }
                    rectF = this.margin;
                    rectF2 = this.margin;
                    emSize = CSS.getEmSize(values[1]);
                    rectF2.right = emSize;
                    rectF.left = emSize;
                    if (CSS.isInheritedSize(values[1])) {
                        rectF = this.marginInherited;
                        this.marginInherited.right = 1.0f;
                        rectF.left = 1.0f;
                    }
                    this.margin.bottom = CSS.getEmSize(values[2]);
                    if (CSS.isInheritedSize(values[2])) {
                        this.marginInherited.bottom = 1.0f;
                    }
                } else if (values.length == 2) {
                    rectF = this.margin;
                    rectF2 = this.margin;
                    emSize = CSS.getEmSize(values[0]);
                    rectF2.bottom = emSize;
                    rectF.top = emSize;
                    if (CSS.isInheritedSize(values[0])) {
                        rectF = this.marginInherited;
                        this.marginInherited.bottom = 1.0f;
                        rectF.top = 1.0f;
                    }
                    rectF = this.margin;
                    rectF2 = this.margin;
                    emSize = CSS.getEmSize(values[1]);
                    rectF2.right = emSize;
                    rectF.left = emSize;
                    if (CSS.isInheritedSize(values[1])) {
                        rectF = this.marginInherited;
                        this.marginInherited.right = 1.0f;
                        rectF.left = 1.0f;
                    }
                } else if (values.length == 1) {
                    rectF = this.margin;
                    rectF2 = this.margin;
                    rectF3 = this.margin;
                    RectF rectF4 = this.margin;
                    float emSize2 = CSS.getEmSize(values[0]);
                    rectF4.right = emSize2;
                    rectF3.left = emSize2;
                    rectF2.bottom = emSize2;
                    rectF.top = emSize2;
                    if (CSS.isInheritedSize(values[0])) {
                        rectF = this.marginInherited;
                        rectF2 = this.marginInherited;
                        rectF3 = this.marginInherited;
                        this.marginInherited.right = 1.0f;
                        rectF3.left = 1.0f;
                        rectF2.bottom = 1.0f;
                        rectF.top = 1.0f;
                    }
                }
            }
            j = CSS.propertyTagIndex(property, "margin-top");
            if (j != -1) {
                value = CSS.propertyTagValue(property, ("margin-top".length() + j) + 1, true);
                this.margin.top = CSS.getEmSize(value);
                if (CSS.isInheritedSize(value)) {
                    this.marginInherited.top = 1.0f;
                }
            }
            j = CSS.propertyTagIndex(property, "margin-bottom");
            if (j != -1) {
                value = CSS.propertyTagValue(property, ("margin-bottom".length() + j) + 1, true);
                this.margin.bottom = CSS.getEmSize(value);
                if (CSS.isInheritedSize(value)) {
                    this.marginInherited.bottom = 1.0f;
                }
            }
            j = CSS.propertyTagIndex(property, "margin-left");
            if (j != -1) {
                value = CSS.propertyTagValue(property, ("margin-left".length() + j) + 1, true);
                this.margin.left = CSS.getEmSize(value);
                if (CSS.isInheritedSize(value)) {
                    this.marginInherited.left = 1.0f;
                }
            }
            j = CSS.propertyTagIndex(property, "margin-right");
            if (j != -1) {
                value = CSS.propertyTagValue(property, ("margin-right".length() + j) + 1, true);
                this.margin.right = CSS.getEmSize(value);
                if (CSS.isInheritedSize(value)) {
                    this.marginInherited.right = 1.0f;
                }
            }
            j = CSS.propertyTagIndex(property, "padding");
            if (j != -1) {
                values = CSS.propertyTagValue(property, ("padding".length() + j) + 1, false).split(" +");
                if (values.length == 4) {
                    this.padding.top = CSS.getEmSize(values[0]);
                    if (CSS.isInheritedSize(values[0])) {
                        this.paddingInherited.top = 1.0f;
                    }
                    this.padding.right = CSS.getEmSize(values[1]);
                    if (CSS.isInheritedSize(values[1])) {
                        this.paddingInherited.right = 1.0f;
                    }
                    this.padding.bottom = CSS.getEmSize(values[2]);
                    if (CSS.isInheritedSize(values[2])) {
                        this.paddingInherited.bottom = 1.0f;
                    }
                    this.padding.left = CSS.getEmSize(values[3]);
                    if (CSS.isInheritedSize(values[3])) {
                        this.paddingInherited.left = 1.0f;
                    }
                } else if (values.length == 3) {
                    this.padding.top = CSS.getEmSize(values[0]);
                    if (CSS.isInheritedSize(values[0])) {
                        this.paddingInherited.top = 1.0f;
                    }
                    rectF = this.padding;
                    rectF2 = this.padding;
                    emSize = CSS.getEmSize(values[1]);
                    rectF2.right = emSize;
                    rectF.left = emSize;
                    if (CSS.isInheritedSize(values[1])) {
                        rectF = this.paddingInherited;
                        this.paddingInherited.right = 1.0f;
                        rectF.left = 1.0f;
                    }
                    this.padding.bottom = CSS.getEmSize(values[2]);
                    if (CSS.isInheritedSize(values[2])) {
                        this.paddingInherited.bottom = 1.0f;
                    }
                } else if (values.length == 2) {
                    rectF = this.padding;
                    rectF2 = this.padding;
                    emSize = CSS.getEmSize(values[0]);
                    rectF2.bottom = emSize;
                    rectF.top = emSize;
                    if (CSS.isInheritedSize(values[0])) {
                        rectF = this.paddingInherited;
                        this.paddingInherited.bottom = 1.0f;
                        rectF.top = 1.0f;
                    }
                    rectF = this.padding;
                    rectF2 = this.padding;
                    emSize = CSS.getEmSize(values[1]);
                    rectF2.right = emSize;
                    rectF.left = emSize;
                    if (CSS.isInheritedSize(values[1])) {
                        rectF = this.paddingInherited;
                        this.paddingInherited.right = 1.0f;
                        rectF.left = 1.0f;
                    }
                } else if (values.length == 1) {
                    rectF = this.padding;
                    rectF2 = this.padding;
                    rectF3 = this.padding;
                    if (CSS.isInheritedSize(values[0])) {
                        rectF = this.paddingInherited;
                        rectF2 = this.paddingInherited;
                        rectF3 = this.paddingInherited;
                        this.paddingInherited.right = 1.0f;
                        rectF3.left = 1.0f;
                        rectF2.bottom = 1.0f;
                        rectF.top = 1.0f;
                    }
                }
            }
            j = CSS.propertyTagIndex(property, "padding-top");
            if (j != -1) {
                value = CSS.propertyTagValue(property, ("padding-top".length() + j) + 1, true);
                this.padding.top = CSS.getEmSize(value);
                if (CSS.isInheritedSize(value)) {
                    this.paddingInherited.top = 1.0f;
                }
            }
            j = CSS.propertyTagIndex(property, "padding-bottom");
            if (j != -1) {
                value = CSS.propertyTagValue(property, ("padding-bottom".length() + j) + 1, true);
                this.padding.bottom = CSS.getEmSize(value);
                if (CSS.isInheritedSize(value)) {
                    this.paddingInherited.bottom = 1.0f;
                }
            }
            j = CSS.propertyTagIndex(property, "padding-left");
            if (j != -1) {
                value = CSS.propertyTagValue(property, ("padding-left".length() + j) + 1, true);
                this.padding.left = CSS.getEmSize(value);
                if (CSS.isInheritedSize(value)) {
                    this.paddingInherited.left = 1.0f;
                }
            }
            j = CSS.propertyTagIndex(property, "padding-right");
            if (j != -1) {
                value = CSS.propertyTagValue(property, ("padding-right".length() + j) + 1, true);
                this.padding.right = CSS.getEmSize(value);
                if (CSS.isInheritedSize(value)) {
                    this.paddingInherited.right = 1.0f;
                }
            }
            if (this.padding.left != CSS.no_margin && this.padding.left < 0.0f) {
                this.padding.left = 0.0f;
            }
            if (this.padding.top != CSS.no_margin && this.padding.top < 0.0f) {
                this.padding.top = 0.0f;
            }
            if (this.padding.right != CSS.no_margin && this.padding.right < 0.0f) {
                this.padding.right = 0.0f;
            }
            if (this.padding.bottom != CSS.no_margin && this.padding.bottom < 0.0f) {
                this.padding.bottom = 0.0f;
            }
        }

        private void getBorderAndBackground(String property) {
            String[] values;
            RectF rectF;
            RectF rectF2;
            RectF rectF3;
            RectF rectF4;
            float borderSize;
            String[] strArr;
            String[] strArr2;
            String[] strArr3;
            String[] strArr4;
            String str;
            String str2;
            int i = 0;
            if (this.borderColor == null) {
                this.borderColor = new String[4];
            }
            if (this.borderStyle == null) {
                this.borderStyle = new String[4];
            }
            int j = CSS.propertyTagIndex(property, "border");
            if (j != -1) {
                values = CSS.propertyTagValue(property, ("border".length() + j) + 1, false).split(" +");
                if (values.length == 1) {
                    if (isBorderSize(values[0])) {
                        rectF = this.border;
                        rectF2 = this.border;
                        rectF3 = this.border;
                        rectF4 = this.border;
                        borderSize = CSS.getBorderSize(values[0]);
                        rectF4.right = borderSize;
                        rectF3.left = borderSize;
                        rectF2.bottom = borderSize;
                        rectF.top = borderSize;
                    } else {
                        strArr = this.borderStyle;
                        strArr2 = this.borderStyle;
                        strArr3 = this.borderStyle;
                        strArr4 = this.borderStyle;
                        str = values[0];
                        strArr4[3] = str;
                        strArr3[2] = str;
                        strArr2[1] = str;
                        strArr[0] = str;
                    }
                } else if (values.length == 2) {
                    if (isBorderSize(values[0])) {
                        rectF = this.border;
                        rectF2 = this.border;
                        rectF3 = this.border;
                        rectF4 = this.border;
                        borderSize = CSS.getBorderSize(values[0]);
                        rectF4.right = borderSize;
                        rectF3.left = borderSize;
                        rectF2.bottom = borderSize;
                        rectF.top = borderSize;
                        strArr = this.borderStyle;
                        strArr2 = this.borderStyle;
                        strArr3 = this.borderStyle;
                        strArr4 = this.borderStyle;
                        str = values[1];
                        strArr4[3] = str;
                        strArr3[2] = str;
                        strArr2[1] = str;
                        strArr[0] = str;
                    } else {
                        strArr = this.borderStyle;
                        strArr2 = this.borderStyle;
                        strArr3 = this.borderStyle;
                        strArr4 = this.borderStyle;
                        str = values[0];
                        strArr4[3] = str;
                        strArr3[2] = str;
                        strArr2[1] = str;
                        strArr[0] = str;
                        strArr = this.borderColor;
                        strArr2 = this.borderColor;
                        strArr3 = this.borderColor;
                        strArr4 = this.borderColor;
                        str = values[1];
                        strArr4[3] = str;
                        strArr3[1] = str;
                        strArr2[2] = str;
                        strArr[0] = str;
                    }
                } else if (values.length == 3) {
                    Border b = new Border(this, values);
                    rectF = this.border;
                    rectF2 = this.border;
                    rectF3 = this.border;
                    rectF4 = this.border;
                    borderSize = CSS.getBorderSize(values[b.size]);
                    rectF4.right = borderSize;
                    rectF3.left = borderSize;
                    rectF2.bottom = borderSize;
                    rectF.top = borderSize;
                    strArr = this.borderStyle;
                    strArr2 = this.borderStyle;
                    strArr3 = this.borderStyle;
                    strArr4 = this.borderStyle;
                    str = values[b.type];
                    strArr4[3] = str;
                    strArr3[2] = str;
                    strArr2[1] = str;
                    strArr[0] = str;
                    strArr = this.borderColor;
                    strArr2 = this.borderColor;
                    strArr3 = this.borderColor;
                    strArr4 = this.borderColor;
                    str = values[b.color];
                    strArr4[3] = str;
                    strArr3[1] = str;
                    strArr2[2] = str;
                    strArr[0] = str;
                }
            }
            j = CSS.propertyTagIndex(property, "border-radius");
            if (j != -1) {
                this.borderRadius = Float.valueOf(CSS.getEmSize(CSS.propertyTagValue(property, ("border-radius".length() + j) + 1, true)));
            }
            j = CSS.propertyTagIndex(property, "border-style");
            if (j != -1) {
                values = CSS.propertyTagValue(property, ("border-style".length() + j) + 1, false).split(" +");
                if (values.length == 4) {
                    this.borderStyle[0] = values[3];
                    this.borderStyle[1] = values[0];
                    this.borderStyle[2] = values[1];
                    this.borderStyle[3] = values[2];
                } else if (values.length == 3) {
                    this.borderStyle[1] = values[0];
                    strArr = this.borderStyle;
                    strArr2 = this.borderStyle;
                    str2 = values[1];
                    strArr2[2] = str2;
                    strArr[0] = str2;
                    this.borderStyle[3] = values[2];
                } else if (values.length == 2) {
                    strArr = this.borderStyle;
                    strArr2 = this.borderStyle;
                    str2 = values[0];
                    strArr2[3] = str2;
                    strArr[1] = str2;
                    strArr = this.borderStyle;
                    strArr2 = this.borderStyle;
                    str2 = values[1];
                    strArr2[2] = str2;
                    strArr[0] = str2;
                } else if (values.length == 1) {
                    strArr = this.borderStyle;
                    strArr2 = this.borderStyle;
                    strArr3 = this.borderStyle;
                    strArr4 = this.borderStyle;
                    str = values[0];
                    strArr4[3] = str;
                    strArr3[2] = str;
                    strArr2[1] = str;
                    strArr[0] = str;
                }
            }
            j = CSS.propertyTagIndex(property, "border-width");
            if (j != -1) {
                values = CSS.propertyTagValue(property, ("border-width".length() + j) + 1, false).split(" +");
                if (values.length == 4) {
                    this.border.top = CSS.getBorderSize(values[0]);
                    this.border.right = CSS.getBorderSize(values[1]);
                    this.border.bottom = CSS.getBorderSize(values[2]);
                    this.border.left = CSS.getBorderSize(values[3]);
                } else if (values.length == 3) {
                    this.border.top = CSS.getBorderSize(values[0]);
                    rectF = this.border;
                    rectF2 = this.border;
                    this.border.bottom = CSS.getBorderSize(values[2]);
                } else if (values.length == 2) {
                    rectF = this.border;
                    rectF2 = this.border;
                } else if (values.length == 1) {
                    rectF = this.border;
                    rectF2 = this.border;
                    rectF3 = this.border;
                    rectF4 = this.border;
                    borderSize = CSS.getBorderSize(values[0]);
                    rectF4.right = borderSize;
                    rectF3.left = borderSize;
                    rectF2.bottom = borderSize;
                    rectF.top = borderSize;
                }
            }
            j = CSS.propertyTagIndex(property, "border-color");
            if (j != -1) {
                values = deleteBlanksInBrackets(CSS.propertyTagValue(property, ("border-color".length() + j) + 1, false)).split(" +");
                if (values.length == 4) {
                    this.borderColor[0] = values[3];
                    this.borderColor[1] = values[0];
                    this.borderColor[2] = values[1];
                    this.borderColor[3] = values[2];
                } else if (values.length == 3) {
                    this.borderColor[1] = values[0];
                    strArr = this.borderColor;
                    strArr2 = this.borderColor;
                    str2 = values[1];
                    strArr2[2] = str2;
                    strArr[0] = str2;
                    this.borderColor[3] = values[2];
                } else if (values.length == 2) {
                    strArr = this.borderColor;
                    strArr2 = this.borderColor;
                    str2 = values[0];
                    strArr2[3] = str2;
                    strArr[1] = str2;
                    strArr = this.borderColor;
                    strArr2 = this.borderColor;
                    str2 = values[1];
                    strArr2[2] = str2;
                    strArr[0] = str2;
                } else if (values.length == 1) {
                    strArr = this.borderColor;
                    strArr2 = this.borderColor;
                    strArr3 = this.borderColor;
                    strArr4 = this.borderColor;
                    str = values[0];
                    strArr4[3] = str;
                    strArr3[1] = str;
                    strArr2[2] = str;
                    strArr[0] = str;
                }
            }
            j = CSS.propertyTagIndex(property, "border-left");
            if (j != -1) {
                values = CSS.propertyTagValue(property, ("border-left".length() + j) + 1, false).split(" +");
                if (values.length > 2) {
                } else if (values.length > 1) {
                    if (isBorderSize(values[0])) {
                        this.border.left = CSS.getBorderSize(values[0]);
                        this.borderStyle[0] = values[1];
                    } else {
                        this.borderStyle[0] = values[0];
                        this.borderColor[0] = values[1];
                    }
                } else if (values.length > 0) {
                    if (isBorderSize(values[0])) {
                        this.border.left = CSS.getBorderSize(values[0]);
                    } else {
                        this.borderStyle[0] = values[0];
                    }
                }
            } else {
                j = CSS.propertyTagIndex(property, "border-left-style");
                if (j != -1) {
                    this.borderStyle[0] = CSS.propertyTagValue(property, ("border-left-style".length() + j) + 1, true);
                }
                j = CSS.propertyTagIndex(property, "border-left-width");
                if (j != -1) {
                    this.border.left = CSS.getBorderSize(CSS.propertyTagValue(property, ("border-left-width".length() + j) + 1, true));
                }
                j = CSS.propertyTagIndex(property, "border-left-color");
                if (j != -1) {
                    this.borderColor[0] = CSS.propertyTagValue(property, ("border-left-color".length() + j) + 1, true);
                }
            }
            j = CSS.propertyTagIndex(property, "border-top");
            if (j != -1) {
                values = CSS.propertyTagValue(property, ("border-top".length() + j) + 1, false).split(" +");
                if (values.length > 2) {
                } else if (values.length > 1) {
                    if (isBorderSize(values[0])) {
                        this.border.top = CSS.getBorderSize(values[0]);
                        this.borderStyle[1] = values[1];
                    } else {
                        this.borderStyle[1] = values[0];
                        this.borderColor[1] = values[1];
                    }
                } else if (values.length > 0) {
                    if (isBorderSize(values[0])) {
                        this.border.top = CSS.getBorderSize(values[0]);
                    } else {
                        this.borderStyle[1] = values[0];
                    }
                }
            } else {
                j = CSS.propertyTagIndex(property, "border-top-style");
                if (j != -1) {
                    this.borderStyle[1] = CSS.propertyTagValue(property, ("border-top-style".length() + j) + 1, true);
                }
                j = CSS.propertyTagIndex(property, "border-top-width");
                if (j != -1) {
                    this.border.top = CSS.getBorderSize(CSS.propertyTagValue(property, ("border-top-width".length() + j) + 1, true));
                }
                j = CSS.propertyTagIndex(property, "border-top-color");
                if (j != -1) {
                    this.borderColor[1] = CSS.propertyTagValue(property, ("border-top-color".length() + j) + 1, true);
                }
            }
            j = CSS.propertyTagIndex(property, "border-right");
            if (j != -1) {
                values = CSS.propertyTagValue(property, ("border-right".length() + j) + 1, false).split(" +");
                if (values.length > 2) {
                } else if (values.length > 1) {
                    if (isBorderSize(values[0])) {
                        this.border.right = CSS.getBorderSize(values[0]);
                        this.borderStyle[2] = values[1];
                    } else {
                        this.borderStyle[2] = values[0];
                        this.borderColor[2] = values[1];
                    }
                } else if (values.length > 0) {
                    if (isBorderSize(values[0])) {
                        this.border.right = CSS.getBorderSize(values[0]);
                    } else {
                        this.borderStyle[2] = values[0];
                    }
                }
            } else {
                j = CSS.propertyTagIndex(property, "border-right-style");
                if (j != -1) {
                    this.borderStyle[2] = CSS.propertyTagValue(property, ("border-right-style".length() + j) + 1, true);
                }
                j = CSS.propertyTagIndex(property, "border-right-width");
                if (j != -1) {
                    this.border.right = CSS.getBorderSize(CSS.propertyTagValue(property, ("border-right-width".length() + j) + 1, true));
                }
                j = CSS.propertyTagIndex(property, "border-right-color");
                if (j != -1) {
                    this.borderColor[2] = CSS.propertyTagValue(property, ("border-right-color".length() + j) + 1, true);
                }
            }
            j = CSS.propertyTagIndex(property, "border-bottom");
            if (j != -1) {
                values = CSS.propertyTagValue(property, ("border-bottom".length() + j) + 1, false).split(" +");
                if (values.length > 2) {
                } else if (values.length > 1) {
                    if (isBorderSize(values[0])) {
                        this.border.bottom = CSS.getBorderSize(values[0]);
                        this.borderStyle[3] = values[1];
                    } else {
                        this.borderStyle[3] = values[0];
                        this.borderColor[3] = values[1];
                    }
                } else if (values.length > 0) {
                    if (isBorderSize(values[0])) {
                        this.border.bottom = CSS.getBorderSize(values[0]);
                    } else {
                        this.borderStyle[3] = values[0];
                    }
                }
            } else {
                j = CSS.propertyTagIndex(property, "border-bottom-style");
                if (j != -1) {
                    this.borderStyle[3] = CSS.propertyTagValue(property, ("border-bottom-style".length() + j) + 1, true);
                }
                j = CSS.propertyTagIndex(property, "border-bottom-width");
                if (j != -1) {
                    this.border.bottom = CSS.getBorderSize(CSS.propertyTagValue(property, ("border-bottom-width".length() + j) + 1, true));
                }
                j = CSS.propertyTagIndex(property, "border-bottom-color");
                if (j != -1) {
                    this.borderColor[3] = CSS.propertyTagValue(property, ("border-bottom-color".length() + j) + 1, true);
                }
            }
            if ((!CSS.borderStyleOk(this.borderStyle[0]) || "transparent".equals(this.borderColor[0])) && this.border.left > 0.0f) {
                this.border.left = 0.0f;
            }
            if ((!CSS.borderStyleOk(this.borderStyle[1]) || "transparent".equals(this.borderColor[1])) && this.border.top > 0.0f) {
                this.border.top = 0.0f;
            }
            if ((!CSS.borderStyleOk(this.borderStyle[2]) || "transparent".equals(this.borderColor[2])) && this.border.right > 0.0f) {
                this.border.right = 0.0f;
            }
            if ((!CSS.borderStyleOk(this.borderStyle[3]) || "transparent".equals(this.borderColor[3])) && this.border.bottom > 0.0f) {
                this.border.bottom = 0.0f;
            }
            if (this.border.left > 20.0f) {
                this.border.left = 0.0f;
            }
            if (this.border.top > 20.0f) {
                this.border.top = 0.0f;
            }
            if (this.border.right > 20.0f) {
                this.border.right = 0.0f;
            }
            if (this.border.bottom > 20.0f) {
                this.border.bottom = 0.0f;
            }
            j = CSS.propertyTagIndex(property, "background");
            if (j != -1) {
                this.background = CSS.propertyTagValue(property, ("background".length() + j) + 1, false);
                if (!(T.isNull(this.background) || this.background.contains("transparent"))) {
                    values = deleteBlanksInBrackets(this.background).split(" +");
                    int length = values.length;
                    while (i < length) {
                        String value = values[i];
                        if (value.startsWith("#") || value.startsWith("rgb")) {
                            this.backgroundColor = value;
                        } else if (value.startsWith("url(")) {
                            this.backgroundImage = value;
                        } else if (T.getHtmlColor(value) != null) {
                            this.backgroundColor = value;
                        } else if (value.contains("top") || value.contains("center") || value.contains("bottom")) {
                            this.backgroundPosition = value;
                        }
                        i++;
                    }
                }
            }
            j = CSS.propertyTagIndex(property, "background-image");
            if (j != -1) {
                this.backgroundImage = CSS.propertyTagValue(property, ("background-image".length() + j) + 1, true);
            }
            if (this.backgroundImage != null) {
                j = CSS.propertyTagIndex(property, "background-position");
                if (j != -1) {
                    this.backgroundPosition = CSS.propertyTagValue(property, ("background-position".length() + j) + 1, true);
                }
            }
        }

        private boolean isBorderSize(String value) {
            if (T.isNull(value)) {
                return false;
            }
            if (value.contains("thin") || value.contains("medium") || value.contains("thick")) {
                return true;
            }
            char c = value.charAt(0);
            if (c == '.' || (c >= '0' && c <= '9')) {
                return true;
            }
            return false;
        }

        private boolean isCssColor(String value) {
            if (value == null) {
                return false;
            }
            if (value.equals("currentcolor") || value.equals("transparent") || value.startsWith("hsl") || T.getHtmlColor(value) != null) {
                return true;
            }
            return false;
        }

        private String deleteBlanksInBrackets(String s) {
            int start = 0;
            while (true) {
                int b1 = s.indexOf("(", start);
                if (b1 != -1) {
                    int b2 = s.indexOf(")", b1);
                    if (b2 == -1) {
                        break;
                    }
                    s = s.substring(0, b1) + s.substring(b1, b2).replace(" ", "") + s.substring(b2);
                    start++;
                } else {
                    break;
                }
            }
            return s;
        }

        private String get_emphasis_style(String s) {
            if (s.indexOf("filled sesame") != -1) {
                return "﹅";
            }
            if (s.indexOf("open sesame") != -1) {
                return "﹆";
            }
            if (s.indexOf("triangle") != -1) {
                return "▲";
            }
            if (s.indexOf("filled double-circle") != -1) {
                return "◉";
            }
            if (s.indexOf("double-circle") != -1) {
                return "◎";
            }
            if (s.indexOf("circle") != -1) {
                return "●";
            }
            if (s.indexOf("dot") != -1) {
                return "•";
            }
            return null;
        }
    }

    public CSS(String cssText) {
        long t = System.currentTimeMillis();
        scanTextForStyles(cssText);
        checkLinkStyle();
        A.log("==========css scan time:" + (System.currentTimeMillis() - t) + ", css count:" + this.styles.size() + ", cssText length:" + cssText.length());
    }

    public void scanTextForStyles(String text) {
        text = deleteComments(text).replace(" !important", "").replace("!important", "");
        int current = 0;
        while (true) {
            int i = text.indexOf("{", current);
            if (i != -1) {
                current = text.indexOf("}", i);
                if (current != -1) {
                    int ignore = 0;
                    do {
                        int i2 = text.indexOf("{", i + 1);
                        if (i2 == -1 || i2 > current) {
                            break;
                        }
                        ignore = 1;
                        i = current;
                        current = text.indexOf("}", i + 1);
                    } while (current != -1);
                    ignore = 2;
                    if (ignore != 1) {
                        if (ignore != 2) {
                            ArrayList<String> names = getClassName(text, i);
                            if (names.size() != 0) {
                                String property = text.substring(i, current + 1);
                                for (int k = 0; k < names.size(); k++) {
                                    Style item;
                                    String name = (String) names.get(k);
                                    if (this.styles.containsKey(name)) {
                                        item = (Style) this.styles.get(name);
                                    } else {
                                        try {
                                        } catch (Exception e) {
                                            A.error(e);
                                            return;
                                        }
                                    }
                                }
                                continue;
                            } else {
                                continue;
                            }
                        } else {
                            return;
                        }
                    }
                } else {
                    return;
                }
            }
            return;
        }
    }

    private String deleteComments(String text) {
        text = text.replace("\r", "").toLowerCase();
        StringBuilder sb = new StringBuilder();
        int start = 0;
        do {
            int i1 = text.indexOf("/*", start);
            if (i1 != -1) {
                sb.append(text.substring(start, i1));
                int i2 = text.indexOf("*/", i1);
                if (i2 == -1) {
                    break;
                }
                start = i2 + 2;
            } else {
                sb.append(text.substring(start, text.length()));
                break;
            }
        } while (start < text.length());
        return sb.toString();
    }

    private void checkLinkStyle() {
        Style style = (Style) this.styles.get("a");
        if (style != null) {
            this.link_color = T.getHtmlColor(style.color);
            if (urlNoUnderline(style.css_text)) {
                this.link_no_underline = true;
            }
        }
        style = (Style) this.styles.get("a:link");
        if (style == null) {
            style = (Style) this.styles.get("a.link");
        }
        if (style != null) {
            this.link_color = T.getHtmlColor(style.color);
            if (urlNoUnderline(style.css_text)) {
                this.link_no_underline = true;
            }
        }
        style = (Style) this.styles.get("a:visited");
        if (style == null) {
            style = (Style) this.styles.get("a.visited");
        }
        if (style != null) {
            this.link_visited_color = T.getHtmlColor(style.color);
        }
    }

    public static boolean urlNoUnderline(String css_text) {
        if (T.isNull(css_text)) {
            return false;
        }
        int i = css_text.indexOf("text-decoration");
        if (i == -1 || css_text.indexOf("none", i) == -1) {
            return false;
        }
        return true;
    }

    private ArrayList<String> getClassName(String text, int i) {
        ArrayList<String> names = new ArrayList();
        int end = priorNotSplit(text, i, true);
        i = end;
        while (i > 0) {
            i--;
            if (text.charAt(i) == ',') {
                names.add(trimClassName(text.substring(i + 1, end + 1)));
                end = i - 1;
            }
            if (text.charAt(i) != '}' && text.charAt(i) != ';') {
                if (text.charAt(i) == '/') {
                    break;
                }
            }
            break;
        }
        if (i == 0 && text.charAt(i) == '﻿') {
            i++;
        }
        names.add(trimClassName(text.substring(i, end + 1)));
        for (int j = names.size() - 1; j >= 0; j--) {
            String name = (String) names.get(j);
            if (name.length() == 0 || name.startsWith("@") || (name.contains(":") && !name.startsWith("a:"))) {
                names.remove(j);
            }
        }
        return names;
    }

    private String trimClassName(String name) {
        String result = name.substring(nextNotSplit(name, 0), priorNotSplit(name, name.length(), true) + 1);
        int i1 = result.indexOf("[");
        if (i1 != -1) {
            int i2 = result.indexOf("]", i1);
            if (i2 != -1) {
                if (result.substring(i1, i2).contains("rtl")) {
                    return "";
                }
                if (result.startsWith("html") || result.startsWith("body")) {
                    result = result.substring(0, i1) + result.substring(i2 + 1);
                }
            }
        }
        int i = result.indexOf(">");
        if (i > 0) {
            if (i < result.length() - 1 && result.charAt(i + 1) != ' ') {
                result = result.substring(0, i + 1) + " " + result.substring(i + 1);
            }
            if (result.charAt(i - 1) != ' ') {
                result = result.substring(0, i) + " " + result.substring(i);
            }
        }
        return result.replace('\n', ' ').trim();
    }

    public static float getBorderSize(String value) {
        if (value.contains("thin")) {
            return 1.0f / EM_no_density();
        }
        if (value.contains("medium")) {
            return 2.0f / EM_no_density();
        }
        if (value.contains("thick")) {
            return 4.0f / EM_no_density();
        }
        return getEmSize(value);
    }

    public static float getEmSize(String value) {
        if (value == null) {
            return 0.0f;
        }
        value = value.trim();
        if (value.length() == 0) {
            return 0.0f;
        }
        float f = T.string2Float(value);
        if (value.contains("%")) {
            return ((((float) A.getPageWidth()) * f) / 105.0f) / EM();
        }
        if (value.contains("em")) {
            return f;
        }
        if (value.contains("px")) {
            return f / 16.0f;
        }
        if (value.contains("pt")) {
            return f / 12.0f;
        }
        if (value.contains("in")) {
            return f * FLOAT_ADDED;
        }
        if (value.contains("cm")) {
            return f * 2.0f;
        }
        return f / 16.0f;
    }

    public static float getCssSize(String value, boolean forFontSize) {
        if (value == null) {
            return 0.0f;
        }
        value = value.trim();
        if (value.length() == 0 || value.charAt(0) == '-') {
            return 0.0f;
        }
        if (forFontSize) {
            if (value.equals("xx-small")) {
                return 0.57f;
            }
            if (value.equals("x-small")) {
                return 0.7f;
            }
            if (value.equals("small")) {
                return 0.82f;
            }
            if (value.equals("medium")) {
                return 1.0f;
            }
            if (value.equals("large")) {
                return 1.18f;
            }
            if (value.equals("x-large")) {
                return 1.44f;
            }
            if (value.equals("xx-large")) {
                return 1.75f;
            }
            if (value.equals("smaller")) {
                return 0.833f;
            }
            if (value.equals("larger")) {
                return 1.2f;
            }
        }
        float f = T.string2Float(value);
        if (!value.contains("em")) {
            if (value.contains("px")) {
                f /= 16.0f;
            } else if (value.contains("pt")) {
                f /= 12.0f;
            } else if (value.contains("%")) {
                f /= 100.0f;
            } else {
                f /= 16.0f;
            }
        }
        if (!forFontSize || f <= 4.0f) {
            return f;
        }
        return 4.0f;
    }

    public static boolean isInheritedSize(String value) {
        return value.endsWith("%") || value.equals("smaller") || value.equals("larger") || (value.endsWith("em") && !value.endsWith("rem"));
    }

    public static boolean isFontWeightBold(String fontWeight) {
        if (T.isNull(fontWeight)) {
            return false;
        }
        if (fontWeight.contains("bold")) {
            return true;
        }
        if (T.string2Int(fontWeight) >= 600) {
            return true;
        }
        return false;
    }

    public static boolean isFontWeightLight(String fontWeight) {
        if (T.isNull(fontWeight) || fontWeight.contains("bold") || T.string2Int(fontWeight) >= 600) {
            return false;
        }
        return true;
    }

    public static String propertyTagValue(String property, int m1, boolean dealBlank) {
        m1 = nextNotSplit(property, m1);
        return property.substring(m1, nextSplit(property, m1, dealBlank));
    }

    public static int propertyTagIndex(String property, String tag) {
        return propertyTagIndex(property, tag, false);
    }

    public static int propertyTagIndex(String property, String tag, boolean partOk) {
        if (property == null) {
            return -1;
        }
        int i = property.indexOf(tag);
        while (i != -1 && property.length() > tag.length() + i) {
            boolean startOk;
            char c1;
            if (i > 0) {
                c1 = property.charAt(i - 1);
            } else {
                c1 = '\u0000';
            }
            char c2 = property.charAt(tag.length() + i);
            if (i == 0 || isSplitChar(c1, true)) {
                startOk = true;
            } else {
                startOk = false;
            }
            boolean endOk = isSplitChar(c2, true);
            if (startOk && endOk) {
                return i;
            }
            if (partOk && (startOk || endOk)) {
                return i;
            }
            i = property.indexOf(tag, tag.length() + i);
        }
        return -1;
    }

    public static int priorNotSplit(String text, int i, boolean dealBlank) {
        i--;
        while (i > 0 && isSplitChar(text.charAt(i), dealBlank)) {
            i--;
        }
        return i;
    }

    public static int priorSplit(String text, int i, boolean dealBlank) {
        i--;
        while (i > 0 && !isSplitChar(text.charAt(i), dealBlank)) {
            i--;
        }
        return i;
    }

    public static int nextNotSplit(String property, int m1) {
        while (m1 < property.length() - 1 && isSplitChar(property.charAt(m1), true)) {
            m1++;
        }
        return m1;
    }

    public static int nextSplit(String property, int m1, boolean dealBlank) {
        int m2 = m1 + 1;
        while (m2 < property.length()) {
            if (property.charAt(m2) == '(') {
                int brackEnd = property.indexOf(")", m2);
                if (brackEnd != -1) {
                    m2 = brackEnd + 1;
                }
            }
            if (isSplitChar(property.charAt(m2), dealBlank)) {
                break;
            }
            m2++;
        }
        return m2;
    }

    public static boolean isSplitChar(char c1, boolean dealBlank) {
        return (dealBlank && c1 == ' ') || c1 == ';' || c1 == ':' || c1 == '\n' || c1 == '\r' || c1 == '\"' || c1 == ',' || c1 == '\t' || c1 == '{' || c1 == '}';
    }

    public static boolean isMarginNull(RectF r) {
        if (r == null) {
            return true;
        }
        if (r.left == no_margin && r.top == no_margin && r.right == no_margin && r.bottom == no_margin) {
            return true;
        }
        return false;
    }

    public static boolean hasParagraphProperty(Style style) {
        if (style == null) {
            return false;
        }
        if (style.backgroundImage == null && !hasBorderOrBackgroundColor(style) && isMarginNull(style.margin) && isMarginNull(style.padding) && (style.indent == null || A.indentParagraph)) {
            return false;
        }
        return true;
    }

    public static float EM() {
        return A.df(A.fontSize);
    }

    public static float EM2() {
        if (A.txtView == null) {
            return EM();
        }
        if (em2 == null) {
            em2 = Float.valueOf(Layout.getDesiredWidth("一", A.txtView.getPaint()));
        }
        return em2.floatValue();
    }

    public static float EM_no_density() {
        return A.fontSize;
    }



    public static float MARGIN_VERT() {
        return (A.txtView != null ? (float) A.txtView.getLineHeight2() : A.df(A.fontSize)) / EM();
    }

    public static RectF emptyMargin() {
        return new RectF(no_margin, no_margin, no_margin, no_margin);
    }

    public static float getMargin(RectF margin, String tag, int d) {
        float v = no_margin;
        if (margin != null) {
            v = d == 0 ? margin.left : d == 1 ? margin.top : d == 2 ? margin.right : margin.bottom;
        }
        if (v != no_margin) {
            return v;
        }
        if (tag.equals("blockquote")) {
            return (d == 0 || d == 2) ? 2.2f : MARGIN_VERT();
        } else {
            if (tag.equals("p") && (d == 1 || d == 3)) {
                return 1.0f;
            }
            if (isHeaderTag(tag) && (d == 1 || d == 3)) {
                return MARGIN_VERT() * 0.83f;
            }
            return 0.0f;
        }
    }

    public static float getPadding(RectF padding, int d) {
        float v = no_margin;
        if (padding != null) {
            v = d == 0 ? padding.left : d == 1 ? padding.top : d == 2 ? padding.right : padding.bottom;
        }
        if (v == no_margin) {
            return 0.0f;
        }
        return v;
    }

    public static float getBorder(String[] borderStyle, RectF border, int d) {
        float v = no_margin;
        if (border != null) {
            v = d == 0 ? border.left : d == 1 ? border.top : d == 2 ? border.right : border.bottom;
        }
        if (v != no_margin) {
            return v;
        }
        if (borderStyle == null) {
            return 0.0f;
        }
        String style = d == 0 ? borderStyle[0] : d == 1 ? borderStyle[1] : d == 2 ? borderStyle[2] : borderStyle[3];
        if (borderStyleOk(style)) {
            return getBorderSize("medium");
        }
        return 0.0f;
    }

    public static boolean hasBorder(Style style) {
        if (style == null || style.borderStyle == null) {
            return false;
        }
        if (style.border.left != 0.0f && borderStyleOk(style.borderStyle[0])) {
            return true;
        }
        if (style.border.top != 0.0f && borderStyleOk(style.borderStyle[1])) {
            return true;
        }
        if (style.border.right != 0.0f && borderStyleOk(style.borderStyle[2])) {
            return true;
        }
        if (style.border.bottom == 0.0f || !borderStyleOk(style.borderStyle[3])) {
            return false;
        }
        return true;
    }

    public static boolean borderStyleOk(String style) {
        return (style == null || style.equals("none")) ? false : true;
    }

    public static boolean hasBorderOrBackgroundColor(Style style) {
        if (style == null) {
            return false;
        }
        if (hasBorder(style) || T.getHtmlColor(style.backgroundColor) != null) {
            return true;
        }
        return false;
    }

    public static boolean isHeaderTag(String tag) {
        return tag.length() == 2 && tag.charAt(0) == 'h' && tag.charAt(1) >= '1' && tag.charAt(1) <= '6';
    }
}