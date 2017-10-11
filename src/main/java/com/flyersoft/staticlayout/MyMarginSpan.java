package com.flyersoft.staticlayout;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.ParagraphStyle;
import com.flyersoft.books.A;
import com.flyersoft.books.T;
import com.flyersoft.components.CSS;
import com.flyersoft.components.CSS.Style;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class MyMarginSpan implements ParagraphStyle {
    public float b;
    public int baseline;
    public int bottom;
    public float bottomMargin;
    public float bottomPadding;
    public Style cssStyle;
    public int end;
    public int endLine;
    int fromIndex;
    public float indent;
    public boolean isTable;
    public boolean isULSpan;
    public float l;
    public float leftMargin;
    public float leftPadding;
    private WeakReference<Drawable> mDrawableRef;
    public float r;
    public float rightMargin;
    public float rightPadding;
    public int spEnd;
    public int spStart;
    public int start;
    public int startLine;
    public float t;
    public String tag;
    public int top;
    public float topMargin;
    public float topPadding;

    private static class MarginLevelComparable implements Comparator<MyMarginSpan> {
        private MarginLevelComparable() {
        }

        public int compare(MyMarginSpan o1, MyMarginSpan o2) {
            if (o2 instanceof MyFloatSpan) {
                return 1;
            }
            if (o1.spStart > o2.spStart || o1.spEnd < o2.spEnd) {
                return -1;
            }
            return 1;
        }
    }

    public MyMarginSpan(int type) {
        float f;
        float f2;
        float f3 = 0.0f;
        float MARGIN_HORI =2.2f;
        if (type == 2) {
            f = 0.0f;
        } else {
            f = CSS.MARGIN_VERT() * A.getParagraphRatio();
        }
        if (type == 1) {
            f2 = 0.0f;
        } else {
            f2 = 2.2f;
        }
        if (type != 2) {
            f3 = CSS.MARGIN_VERT() * A.getParagraphRatio();
        }
        new MyMarginSpan(MARGIN_HORI, f, f2, f3);
    }

    public MyMarginSpan(float leftMargin, float topMargin, float rightMargin, float bottomMargin) {
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
    }

    public void drawFrameMargin(Canvas c, TextPaint paint, MRTextView tv, ArrayList<MyMarginSpan> drawMargins, int fromIndex) {
        if (this.cssStyle != null) {
            if (this.cssStyle.backgroundImage == null && !CSS.hasBorderOrBackgroundColor(this.cssStyle)) {
                return;
            }
            if (tv.lastIgnoreLine() <= 0 || this.startLine < tv.lastIgnoreLine()) {
                float f;
                if (this.startLine == this.endLine) {
                    if (tv.isLastHalfLine(this.startLine)) {
                        if (!tv.isNormalImageLine(this.startLine)) {
                            return;
                        }
                    }
                }
                this.fromIndex = fromIndex;
                int backgroundColor = getBackgroundColor();
                int line = this.startLine;
                this.l = 0.0f;
                this.l += outerHoriMargins(drawMargins, 0) * CSS.EM();
                this.l += (this.leftMargin - this.leftPadding) * CSS.EM();
                this.t = this.start == 0 ? 0.0f : (float) this.top;
                if (this.start != this.spStart) {
                    line = tv.getLayout().getLineForOffset(this.spStart);
                    this.t = line == 0 ? 0.0f : (float) tv.getLineTop(line);
                }
                this.t += outerTopMargins(drawMargins) * CSS.EM();
                this.t += (this.topMargin - this.topPadding) * CSS.EM();
                this.t += (float) tv.getLayout().getLineTopAdded2(line);
                this.r = (float) tv.getWidth2();
                this.r -= outerHoriMargins(drawMargins, 2) * CSS.EM();
                this.r -= (this.rightMargin - this.rightPadding) * CSS.EM();
                boolean endLineVisible = this.spEnd == this.end + 1 || (this.spEnd == this.end && this.end == tv.getText().length());
                int spEndLine = endLineVisible ? this.endLine : tv.getLayout().getLineForOffset(this.spEnd - 1);
                if (endLineVisible) {
                    f = (float) this.bottom;
                } else {
                    f = (float) tv.getLineTop(spEndLine + 1);
                }
                this.b = f;
                this.b += (this.bottomPadding + innerBottomMargins(drawMargins)) * CSS.EM();
                if (this.b > ((float) tv.getRealHeight())) {
                    this.b = (float) tv.getRealHeight();
                }
                this.t -= A.df(1.0f);
                this.b -= A.df(1.0f);
                float floatBottom = extendBottomFromFloatSpan(this.b, drawMargins);
                float floatBottom2 = floatBottom;
                int maxBottom = tv.lastIgnoreLine() > 0 ? tv.getLineTop2(tv.lastIgnoreLine()) : 0;
                if (maxBottom > 0 && floatBottom > ((float) maxBottom)) {
                    floatBottom = (float) maxBottom;
                }
                if (CSS.hasBorder(this.cssStyle)) {
                    RectF border = this.cssStyle.border;
                    float bL = CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 0);
                    float bT = CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 1);
                    float bR = CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 2);
                    float bB = CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 3);
                    this.l -= (bL / 2.0f) * CSS.EM();
                    this.t -= (bT / 2.0f) * CSS.EM();
                    this.r += (bR / 2.0f) * CSS.EM();
                    if (sameBorders(border, this.cssStyle.borderStyle, this.cssStyle.borderColor)) {
                        String str;
                        this.b = floatBottom2;
                        if (this.b > ((float) tv.getLineTop3(tv.getRealLineCount()))) {
                            this.b = (float) (tv.getLineTop3(tv.getRealLineCount()) - 1);
                        }
                        float sw = bL * CSS.EM();
                        String style = this.cssStyle.borderStyle[0];
                        if (this.cssStyle.borderColor == null) {
                            str = this.cssStyle.color;
                        } else {
                            str = this.cssStyle.borderColor[0];
                        }
                        int color = getBorderColor(str, style);
                        Paint p = new Paint();
                        if (this.cssStyle.borderRadius == null || this.cssStyle.borderRadius.floatValue() == 0.0f) {
                            drawBackgroundColor(c, backgroundColor, this.l, this.t, this.r, floatBottom, -1.0f, p);
                            p.setColor(color);
                            updatePaintStyles(p, style, sw);
                            if (style.equals("double")) {
                                p.setStrokeWidth(sw / 3.0f);
                                c.drawRect(this.l, this.t, this.r, this.b, p);
                                c.drawRect(this.l + (sw / 2.0f), this.t + (sw / 2.0f), this.r - (sw / 2.0f), this.b - (sw / 2.0f), p);
                            } else {
                                c.drawRect(this.l, this.t, this.r, this.b, p);
                            }
                        } else {
                            float radius = A.df(sw);
                            drawBackgroundColor(c, backgroundColor, this.l, this.t, this.r, floatBottom, radius, p);
                            p.setColor(color);
                            updatePaintStyles(p, style, sw);
                            c.drawRoundRect(new RectF(this.l, this.t, this.r, this.b), radius, radius, p);
                        }
                    } else {
                        drawBackgroundColor(c, backgroundColor, this.l, this.t, this.r, floatBottom, -1.0f, new Paint());
                        float floatMargin = marginOfFloatSpan(drawMargins);
                        if (floatMargin != 0.0f) {
                            if (floatMargin > 0.0f && this.l < floatMargin) {
                                this.l = floatMargin;
                            } else if (floatMargin < 0.0f && this.r > ((float) tv.getWidth2()) + floatMargin) {
                                this.r = ((float) tv.getWidth2()) + floatMargin;
                            }
                        }
                        if (bL > 0.0f) {
                            drawLine(0, c, this.l, this.t, this.l, this.b, bL, this.cssStyle.borderColor == null ? this.cssStyle.color : this.cssStyle.borderColor[0], this.cssStyle.borderStyle[0]);
                        }
                        if (bT > 0.0f) {
                            drawLine(1, c, this.l, this.t, this.r, this.t, bT, this.cssStyle.borderColor == null ? this.cssStyle.color : this.cssStyle.borderColor[1], this.cssStyle.borderStyle[1]);
                        }
                        if (bR > 0.0f) {
                            drawLine(2, c, this.r, this.t, this.r, this.b, bR, this.cssStyle.borderColor == null ? this.cssStyle.color : this.cssStyle.borderColor[2], this.cssStyle.borderStyle[2]);
                        }
                        if (bB > 0.0f) {
                            String str2;
                            float f2 = this.l;
                            float f3 = this.b;
                            float f4 = this.r;
                            float f5 = this.b;
                            if (this.cssStyle.borderColor == null) {
                                str2 = this.cssStyle.color;
                            } else {
                                str2 = this.cssStyle.borderColor[3];
                            }
                            drawLine(3, c, f2, f3, f4, f5, bB, str2, this.cssStyle.borderStyle[3]);
                        }
                    }
                } else {
                    float preBorderBottom = 0.0f;
                    float outerBoderBottom = 0.0f;
                    for (int i = fromIndex + 1; i < drawMargins.size(); i++) {
                        MyMarginSpan sp = (MyMarginSpan) drawMargins.get(i);
                        if (sp.cssStyle != null) {
                            float spBL = CSS.getBorder(sp.cssStyle.borderStyle, sp.cssStyle.border, 3);
                            if (spBL > 0.0f) {
                                if (sp.spEnd == this.spStart) {
                                    preBorderBottom = spBL;
                                } else if (sp.spEnd == this.spEnd) {
                                    outerBoderBottom = spBL;
                                }
                            }
                        }
                    }
                    if (preBorderBottom == 0.0f && outerBoderBottom == 0.0f) {
                        this.t -= A.df(1.5f);
                        this.b -= A.df(1.5f);
                    } else {
                        if (preBorderBottom > 0.0f) {
                            this.t += (CSS.EM() * preBorderBottom) / 2.0f;
                        }
                        if (outerBoderBottom > 0.0f) {
                            this.b -= (CSS.EM() * outerBoderBottom) / 2.0f;
                        }
                    }
                    drawBackgroundColor(c, backgroundColor, this.l, this.t, this.r, floatBottom, -1.0f, new Paint());
                }
                if (this.cssStyle.backgroundImage != null && A.ebook != null) {
                    drawBackgroundImage(c, (int) (this.l - this.leftPadding), (int) (this.t - this.topPadding), (int) (this.r + this.rightPadding), (int) (this.b + this.bottomPadding));
                }
            }
        }
    }

    private float extendBottomFromFloatSpan(float b, ArrayList<MyMarginSpan> drawMargins) {
        Iterator it = drawMargins.iterator();
        while (it.hasNext()) {
            MyMarginSpan sp = (MyMarginSpan) it.next();
            if ((sp instanceof MyFloatSpan) && sp.spStart >= this.spStart && sp.spEnd <= this.spEnd) {
                MyFloatSpan fp = (MyFloatSpan) sp;
                float floatBottom = (fp.float_v + fp.float_height) + A.df(2.0f);
                if (b < floatBottom) {
                    return floatBottom;
                }
            }
        }
        return b;
    }

    private float marginOfFloatSpan(ArrayList<MyMarginSpan> drawMargins) {
        Iterator it = drawMargins.iterator();
        while (it.hasNext()) {
            MyMarginSpan sp = (MyMarginSpan) it.next();
            if (sp instanceof MyFloatSpan) {
                MyFloatSpan fp = (MyFloatSpan) sp;
                if (fp.spStart >= this.spStart && fp.spEnd <= this.spEnd) {
                    return fp.isLeft ? fp.float_width : -fp.float_width;
                }
            }
        }
        return 0.0f;
    }

    protected boolean sameBorders(RectF border, String[] bs, String[] bc) {
        if (border.left != border.top || border.top != border.right || border.right != border.bottom || bs == null || bs[0] == null || bs[1] == null || bs[2] == null || bs[3] == null || !bs[0].equals(bs[1]) || !bs[1].equals(bs[2]) || !bs[2].equals(bs[3])) {
            return false;
        }
        if (bc == null || (String.valueOf(bc[0]).equals(String.valueOf(bc[1])) && String.valueOf(bc[1]).equals(String.valueOf(bc[2])) && String.valueOf(bc[2]).equals(String.valueOf(bc[3])))) {
            return true;
        }
        return false;
    }

    protected void drawBackgroundImage(Canvas c, int l, int t, int r, int b) {
        String s = this.cssStyle.backgroundImage;
        int i1 = s.indexOf("(");
        int i2 = s.indexOf(")");
        if (i1 != -1 && i2 > i1) {
            try {
                Drawable d = getCachedDrawable(T.deleteQuotes(s.substring(i1 + 1, i2)));
                if (d != null) {
                    if (l < 0) {
                        l = 0;
                    }
                    if (t < 0) {
                        t = 0;
                    }
                    if (r > A.getPageWidth()) {
                        r = A.getPageWidth();
                    }
                    int dw = d.getIntrinsicWidth();
                    int dh = d.getIntrinsicHeight();
                    int w = r - l;
                    int h = b - t;
                    if (dw > 0 && dh > 0) {
                        String css_text = this.cssStyle.css_text != null ? this.cssStyle.css_text : "";
                        boolean repeat = !css_text.contains("no-repeat");
                        boolean contain = css_text.contains("contain;");
                        boolean cover = !contain && css_text.contains("cover;");
                        if (css_text.contains("background-size") && css_text.contains("100%")) {
                            d.setBounds(l, t, r, b);
                            d.draw(c);
                            return;
                        }
                        if (this.cssStyle.backgroundPosition != null) {
                            Rect rect;
                            Rect rect2;
                            if (dw > r - l || dh > b - t) {
                                if (((float) dh) / ((float) h) > ((float) dw) / ((float) w)) {
                                    d = T.zoomDrawable(A.getContext().getResources(), d, (dw * h) / dh, h);
                                } else {
                                    d = T.zoomDrawable(A.getContext().getResources(), d, w, (dh * w) / dw);
                                }
                                dw = d.getIntrinsicWidth();
                                dh = d.getIntrinsicHeight();
                            }
                            String pos = this.cssStyle.backgroundPosition;
                            if (pos.equals("top") || pos.equals("top center")) {
                                if (repeat) {
                                    rect = new Rect(0, 0, w, dh - A.d(1.0f));
                                } else {
                                    rect = new Rect((w - dw) / 2, 0, ((w - dw) / 2) + dw, dh);
                                }
                                rect2 = rect;
                            } else if (pos.equals("top left")) {
                                rect2 = new Rect(0, 0, dw, dh);
                            } else if (pos.equals("top right")) {
                                rect2 = new Rect(w - dw, 0, w, dh);
                            } else if (pos.equals("center") || pos.equals("center center")) {
                                if (repeat) {
                                    rect = new Rect(0, (h - dh) / 2, w, (((h - dh) / 2) + dh) - A.d(1.0f));
                                } else {
                                    rect = new Rect((w - dw) / 2, (h - dh) / 2, ((w - dw) / 2) + dw, ((h - dh) / 2) + dh);
                                }
                                rect2 = rect;
                            } else if (pos.equals("center left") || pos.equals("left")) {
                                rect2 = new Rect(0, (h - dh) / 2, dw, ((h - dh) / 2) + dh);
                            } else if (pos.equals("center right") || pos.equals("right")) {
                                rect2 = new Rect(w - dw, (h - dh) / 2, w, ((h - dh) / 2) + dh);
                            } else if (pos.equals("bottom") || pos.equals("bottom center")) {
                                if (repeat) {
                                    rect = new Rect(0, (h - dh) + A.d(1.0f), w, h);
                                } else {
                                    rect = new Rect((w - dw) / 2, h - dh, ((w - dw) / 2) + dw, h);
                                }
                                rect2 = rect;
                            } else if (pos.equals("bottom left")) {
                                rect2 = new Rect(0, h - dh, dw, h);
                            } else if (pos.equals("bottom right")) {
                                rect2 = new Rect(w - dw, h - dh, w, h);
                            } else {
                                rect2 = null;
                            }
                            if (rect2 != null) {
                                rect = new Rect(rect2.left + l, rect2.top + t, rect2.right + l, rect2.bottom + t);
                                if (repeat && (d instanceof BitmapDrawable)) {
                                    setTileRepeat((BitmapDrawable) d, null);
                                    d.setBounds(rect);
                                    d.draw(c);
                                    return;
                                }
                                c.drawBitmap(T.drawableToBitmap(d), new Rect(0, 0, dw, dh), rect, null);
                                return;
                            }
                        }
                        if (contain) {
                            if (((float) dh) / ((float) h) > ((float) dw) / ((float) w)) {
                                d = T.zoomDrawable(A.getContext().getResources(), d, (dw * h) / dh, h);
                            } else {
                                d = T.zoomDrawable(A.getContext().getResources(), d, w, (dh * w) / dw);
                            }
                        } else if (cover) {
                            d = T.zoomDrawable(A.getContext().getResources(), d, w, (dh * w) / dw);
                        }
                        if (repeat && (d instanceof BitmapDrawable)) {
                            setTileRepeat((BitmapDrawable) d, css_text);
                        }
                        if (cover) {
                            c.drawBitmap(T.drawableToBitmap(d), new Rect(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight() > b ? b : d.getIntrinsicHeight()), new RectF((float) l, (float) t, (float) r, (float) b), null);
                            return;
                        }
                        if (contain) {
                            d.setBounds(l, t, d.getIntrinsicWidth() + l, d.getIntrinsicHeight() + t);
                        } else {
                            d.setBounds(l, t, r, b);
                        }
                        d.draw(c);
                    }
                }
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    private void setTileRepeat(BitmapDrawable d, String css_text) {
        boolean xOk = true;
        boolean yOk = true;
        if (css_text != null && css_text.contains("repeat-x")) {
            yOk = false;
        } else if (css_text != null && css_text.contains("repeat-y")) {
            xOk = false;
        }
        if (xOk) {
            d.setTileModeX(TileMode.REPEAT);
        }
        if (yOk) {
            d.setTileModeY(TileMode.REPEAT);
        }
    }

    protected int getBackgroundColor() {
        int color = T.getHtmlColorInt(this.cssStyle.backgroundColor);
        if (color == 0) {
            return 0;
        }
        if (Math.abs(A.colorValue(A.fontColor) - A.colorValue(color)) < 200) {
            color = A.getAlphaColor(color, -100);
        }
        return color;
    }

    protected void drawBackgroundColor(Canvas c, int backgroundColor, float l, float t, float r, float b, float radius, Paint p) {
        if (backgroundColor != 0) {
            l -= (CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 0) * CSS.EM()) / 2.0f;
            t -= (CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 1) * CSS.EM()) / 2.0f;
            r += (CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 2) * CSS.EM()) / 2.0f;
            b += (CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 3) * CSS.EM()) / 2.0f;
            if (radius == -1.0f && this.cssStyle.css_text != null) {
                int j = this.cssStyle.css_text.indexOf("border-radius");
                if (j != -1) {
                    float v = CSS.getCssSize(CSS.propertyTagValue(this.cssStyle.css_text, ("border-radius".length() + j) + 1, true), false);
                    if (v > 0.0f) {
                        radius = v * CSS.EM();
                    }
                }
            }
            p.setColor(backgroundColor);
            if (radius == -1.0f) {
                c.drawRect(l, t, r, b, p);
            } else {
                c.drawRoundRect(new RectF(l, t, r, b), radius, radius, p);
            }
        }
    }

    protected void drawLine(int direction, Canvas c, float l, float t, float r, float b, float em_width, String colorValue, String style) {
        if (style != null) {
            if (!style.equals("none")) {
                float w;
                if (direction == 0 || direction == 2) {
                    t -= (CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 1) * CSS.EM()) / 2.0f;
                    b += (CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 3) * CSS.EM()) / 2.0f;
                }
                Paint p = new Paint();
                p.setColor(getBorderColor(colorValue, style));
                if (em_width == 0.0625f) {
                    w = 1.0f;
                } else {
                    w = em_width * CSS.EM();
                }
                if (updatePaintStyles(p, style, w)) {
                    Path path = new Path();
                    path.moveTo(l, t);
                    path.lineTo(r, b);
                    c.drawPath(path, p);
                    return;
                }
                if (style.equals("double")) {
                    p.setStrokeWidth(w / 3.0f);
                    if (t == b) {
                        c.drawLine(l, ((w / 2.0f) + t) - ((w / 3.0f) / 2.0f), r, ((w / 2.0f) + b) - ((w / 3.0f) / 2.0f), p);
                        c.drawLine(l, (t - (w / 2.0f)) + ((w / 3.0f) / 2.0f), r, (b - (w / 2.0f)) + ((w / 3.0f) / 2.0f), p);
                        return;
                    }
                    c.drawLine(((w / 2.0f) + l) - ((w / 3.0f) / 2.0f), t, ((w / 2.0f) + r) - ((w / 3.0f) / 2.0f), b, p);
                    c.drawLine(((w / 3.0f) / 2.0f) + (l - (w / 2.0f)), t, (r - (w / 2.0f)) + ((w / 3.0f) / 2.0f), b, p);
                    return;
                }
                p.setStrokeWidth(w);
                c.drawLine(l, t, r, b, p);
            }
        }
    }

    protected boolean updatePaintStyles(Paint p, String style, float w) {
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(w);
        if (!style.equals("dotted") && !style.equals("dashed")) {
            return false;
        }
        float off = style.equals("dotted") ? w > 1.0f ? w : 1.5f : w * 4.0f;
        p.setPathEffect(new DashPathEffect(new float[]{off, off}, 0.0f));
        return true;
    }

    protected int getBorderColor(String colorValue, String style) {
        int color = T.getHtmlColorInt(colorValue);
        if (color == 0) {
            if (style.equals("solid") || style.equals("dotted") || style.equals("dashed") || style.equals("double")) {
                color = A.fontColor;
            } else {
                color = A.getAlphaColor(A.fontColor, -100);
            }
        }
        if (this.tag.equals("table")) {
            return A.getAlphaColor(color, -150);
        }
        if (this.tag.equals("tr")) {
            return A.getAlphaColor(color, -150);
        }
        return color;
    }

    private Drawable getCachedDrawable(String img) {
        WeakReference<Drawable> wr = this.mDrawableRef;
        Drawable d = null;
        if (wr != null) {
            d = (Drawable) wr.get();
        }
        if (d != null) {
            return d;
        }
        d = A.ebook.getDrawableFromSource(img, 0);
        this.mDrawableRef = new WeakReference(d);
        return d;
    }

    public float outerHoriMargins(ArrayList<MyMarginSpan> drawMargins, int direction) {
        float add = 0.0f;
        for (int i = this.fromIndex + 1; i < drawMargins.size(); i++) {
            MyMarginSpan sp = (MyMarginSpan) drawMargins.get(i);
            if (sp.spStart <= this.spStart && sp.spEnd >= this.spEnd) {
                add += direction == 0 ? sp.leftMargin : sp.rightMargin;
            }
        }
        return add;
    }

    protected float outerTopMargins(ArrayList<MyMarginSpan> drawMargins) {
        float add = 0.0f;
        for (int i = this.fromIndex + 1; i < drawMargins.size(); i++) {
            MyMarginSpan sp = (MyMarginSpan) drawMargins.get(i);
            if (sp.spStart == this.spStart && sp.spEnd >= this.spEnd) {
                add += sp.topMargin;
            }
        }
        return add;
    }

    private float innerBottomMargins(ArrayList<MyMarginSpan> drawMargins) {
        float add = 0.0f;
        for (int i = 0; i < this.fromIndex; i++) {
            MyMarginSpan sp = (MyMarginSpan) drawMargins.get(i);
            if (sp.spStart >= this.spStart && sp.spEnd == this.spEnd) {
                add += sp.bottomMargin;
            }
        }
        return add;
    }

    public static void sortDrawMargins(ArrayList<MyMarginSpan> drawMagins) {
        if (drawMagins != null && drawMagins.size() >= 2) {
            try {
                Collections.sort(drawMagins, new MarginLevelComparable());
            } catch (Exception e) {
                A.error(e);
            }
        }
    }
}