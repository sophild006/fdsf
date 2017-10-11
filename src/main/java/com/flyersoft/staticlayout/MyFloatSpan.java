package com.flyersoft.staticlayout;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import com.flyersoft.books.A;
import com.flyersoft.components.CSS;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MyFloatSpan extends MyMarginSpan {
    public float float_height;
    public StaticLayout float_sl;
    public float float_sl_v;
    public CharSequence float_text;
    public float float_tv_h;
    public float float_tv_w;
    public float float_v;
    public float float_width;
    public MetricAffectingSpan imageSpan;
    public boolean isDropcap;
    public boolean isLeft;
    private WeakReference<Drawable> mDrawableRef;
    public RectF margin = new RectF();
    public RectF padding = new RectF();
    public TextPaint workPaint;

    public MyFloatSpan() {
        super(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public void drawFrameMargin(Canvas c, TextPaint paint, MRTextView tv, ArrayList<MyMarginSpan> drawMargins, int fromIndex) {
        if (tv.lastIgnoreLine() <= 0 || this.startLine < tv.lastIgnoreLine()) {
            if (this.startLine == this.endLine) {
                if (tv.isLastHalfLine(this.startLine)) {
                    if (!tv.isNormalImageLine(this.startLine)) {
                        return;
                    }
                }
            }
            float t2 = this.float_v;
            drawBorderAndBackground(c, tv, t2);
            if (this.isDropcap) {
                FontMetricsInt fm = new FontMetricsInt();
                this.workPaint.getFontMetricsInt(fm);
                float y = ((float) ((-fm.ascent) + (fm.top - fm.ascent))) + A.df(0.5f);
                if (VERSION.SDK_INT >= 24) {
                    y -= y / 5.0f;
                }
                y += t2;
                TextPaint p = this.workPaint;
                if (p.getColor() == ViewCompat.MEASURED_STATE_MASK) {
                    p.setColor(A.fontColor);
                }
                c.drawText(tv.getText(), this.spStart, this.spEnd, this.padding.left, this.padding.top + y, p);
                return;
            }
            Drawable d = getCachedDrawable();
            if (d != null) {
                float f;
                float ratio;
                float outHori = ((outerMargin(drawMargins, this.isLeft ? 0 : 2, false) * CSS.EM()) * 4.0f) / 5.0f;
                float image_h = this.float_height - this.float_tv_h;
                float mLeft = this.padding.left + this.margin.left;
                float mRight = this.padding.right + this.margin.right;
                if (this.isLeft) {
                    f = mLeft;
                } else {
                    f = (((float) tv.getWidth()) - this.float_width) + mLeft;
                }
                int x = (int) f;
                int y2 = (int) ((this.padding.top + t2) + this.margin.top);
                if (((float) y2) > (image_h / 2.0f) + t2) {
                    y2 = (int) ((image_h / 2.0f) + t2);
                }
                int w1 = (int) ((this.float_width - mLeft) - mRight);
                if (outHori > 0.0f) {
                    if (((float) w1) + outHori > this.float_width - ((float) A.d(5.0f))) {
                        outHori = (this.float_width - ((float) w1)) - ((float) A.d(5.0f));
                    }
                    if (this.isLeft && outHori > ((float) x)) {
                        x = (int) outHori;
                    } else if (!this.isLeft && ((float) (tv.getWidth() - w1)) - outHori < ((float) x)) {
                        x = (int) (((float) (tv.getWidth() - w1)) - outHori);
                    }
                }
                int h1 = ( w1) / 100;
                if (this.padding.bottom + this.margin.bottom > (t2 + image_h) - ((float) (y2 + h1))) {
                    ratio = 1.0f - (((this.padding.bottom + this.margin.bottom) - ((t2 + image_h) - ((float) (y2 + h1)))) / ((float) h1));
                    if (ratio < 0.8f) {
                        ratio = 0.8f;
                    }
                    w1 = (int) (((float) w1) * ratio);
                    h1 = (int) (((float) h1) * ratio);
                }
                float off = 0.0f;
                if (this.float_sl != null) {
                    this.float_sl_v = this.float_v + (this.float_height - this.float_tv_h);
                    float sl_h = (float) this.float_sl.getHeight();
                    float adjust = ((float) tv.getLineHeight2()) * 2.0f;
                    int tvBottom = tv.getScrollView().getScrollY() + A.getPageHeight();
                    boolean bottomOk = this.float_sl_v < ((float) tvBottom) - (((float) this.float_sl.getLineTop(1)) * 0.9f);
                    boolean topOk = A.moveStart || (this.float_sl_v + sl_h) - ((float) tv.getScrollView().getScrollY()) > adjust;
                    if (bottomOk && topOk) {
                        float tmp = (this.float_sl_v + sl_h) - ((float) tvBottom);
                        if (tmp > 0.0f && tmp < adjust) {
                            float allow = (this.float_sl_v - ((float) (y2 + h1))) - ((float) A.d(2.0f));
                            if (allow >= tmp) {
                                this.float_sl_v -= tmp;
                            } else if (((float) h1) - (tmp - allow) > ((float) (h1 / 2))) {
                                off = tmp - allow;
                                this.float_sl_v -= tmp;
                            }
                        }
                        c.save();
                        c.translate((float) x, this.float_sl_v);
                        this.float_sl.draw(c);
                        c.restore();
                    }
                }
                if (off > 0.0f) {
                    ratio = 1.0f - (off / ((float) h1));
                    w1 = (int) (((float) w1) * ratio);
                    h1 = (int) (((float) h1) * ratio);
                }
                d.setBounds(new Rect(x, y2, x + w1, y2 + h1));
                d.draw(c);
                if (A.lastTheme.equals(A.NIGHT_THEME)) {
                    Paint p2 = new Paint();
                    p2.setColor(1711276032);
                    c.drawRect(d.getBounds(), p2);
                }
            }
        }
    }

    private void drawBorderAndBackground(Canvas c, MRTextView tv, float t2) {
        float r;
        float l = this.isLeft ? this.margin.left : ((((float) tv.getWidth()) - this.float_width) + this.margin.left) + A.df(1.0f);
        float t = t2 + this.margin.top;
        if (this.isLeft) {
            r = (this.float_width - this.margin.right) - A.df(1.0f);
        } else {
            r = ((float) tv.getWidth()) - this.margin.right;
        }
        float b = (this.float_height + t2) - this.margin.bottom;
        int backgroundColor = getBackgroundColor();
        if (CSS.hasBorder(this.cssStyle)) {
            RectF border = this.cssStyle.border;
            l += (CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 0) * CSS.EM()) / 2.0f;
            r -= (CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 2) * CSS.EM()) / 2.0f;
            if (sameBorders(border, this.cssStyle.borderStyle, this.cssStyle.borderColor)) {
                float sw = border.left * CSS.EM();
                String style = this.cssStyle.borderStyle[0];
                int color = getBorderColor(this.cssStyle.borderColor == null ? this.cssStyle.color : this.cssStyle.borderColor[0], style);
                Paint p = new Paint();
                if (this.cssStyle.borderRadius == null || this.cssStyle.borderRadius.floatValue() == 0.0f) {
                    drawBackgroundColor(c, backgroundColor, l, t, r, b, -1.0f, p);
                    p.setColor(color);
                    updatePaintStyles(p, style, sw);
                    c.drawRect(l, t, r, b, p);
                    return;
                }
                float radius = A.df(sw);
                drawBackgroundColor(c, backgroundColor, l, t, r, b, radius, p);
                p.setColor(color);
                updatePaintStyles(p, style, sw);
                c.drawRoundRect(new RectF(l, t, r, b), radius, radius, p);
                return;
            }
            drawBackgroundColor(c, backgroundColor, l, t, r, b, -1.0f, new Paint());
            t -= (CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 1) * CSS.EM()) / 2.0f;
            b += (CSS.getBorder(this.cssStyle.borderStyle, this.cssStyle.border, 3) * CSS.EM()) / 2.0f;
            if (border.left > 0.0f) {
                drawLine(0, c, l, t, l, b, border.left, this.cssStyle.borderColor == null ? this.cssStyle.color : this.cssStyle.borderColor[0], this.cssStyle.borderStyle[0]);
            }
            if (border.top > 0.0f) {
                drawLine(1, c, l, t, r, t, border.top, this.cssStyle.borderColor == null ? this.cssStyle.color : this.cssStyle.borderColor[1], this.cssStyle.borderStyle[1]);
            }
            if (border.right > 0.0f) {
                drawLine(2, c, r, t, r, b, border.right, this.cssStyle.borderColor == null ? this.cssStyle.color : this.cssStyle.borderColor[2], this.cssStyle.borderStyle[2]);
            }
            if (border.bottom > 0.0f) {
                String str;
                float f = border.bottom;
                if (this.cssStyle.borderColor == null) {
                    str = this.cssStyle.color;
                } else {
                    str = this.cssStyle.borderColor[3];
                }
                drawLine(3, c, l, b, r, b, f, str, this.cssStyle.borderStyle[3]);
                return;
            }
            return;
        }
        drawBackgroundColor(c, backgroundColor, l, t, r, b, -1.0f, new Paint());
    }

    protected float outerMargin(ArrayList<MyMarginSpan> drawMargins, int d, boolean sameStart) {
        float add = 0.0f;
        for (int i = 0; i < drawMargins.size(); i++) {
            MyMarginSpan sp = (MyMarginSpan) drawMargins.get(i);
            if (sp != this && (((sameStart && sp.spStart == this.spStart) || (!sameStart && sp.spStart <= this.spStart)) && sp.spEnd >= this.spEnd)) {
                float f = d == 0 ? sp.leftMargin : d == 1 ? sp.topMargin : d == 2 ? sp.rightMargin : sp.bottomMargin;
                add += f;
            }
        }
        return add;
    }

    private Drawable getCachedDrawable() {
        if (this.imageSpan == null) {
            return null;
        }
        WeakReference<Drawable> wr = this.mDrawableRef;
        Drawable d = null;
        if (wr != null) {
            d = (Drawable) wr.get();
        }
        if (d != null) {
            return d;
        }
        this.mDrawableRef = new WeakReference(d);
        return d;
    }
}