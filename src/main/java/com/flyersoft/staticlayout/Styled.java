package com.flyersoft.staticlayout;

import android.graphics.Canvas;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.os.Build.VERSION;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;

import com.flyersoft.books.A;
import com.flyersoft.components.CSS;

public class Styled {
    public static float leftMargin;
    public static float rightMargin;

    private static float each(MRTextView tv, Canvas canvas, Spanned text, int start, int end, int dir, boolean reverse, float x, int top, int y, int bottom, FontMetricsInt fmi, TextPaint paint, TextPaint workPaint, boolean needwid) {
        boolean havewid = false;
        float ret = 0.0f;
        paint.bgColor = 0;
        paint.baselineShift = 0;
        workPaint.set(paint);
        Spanned spanned = text;
        TextPaint textPaint = paint;
        TextPaint textPaint2 = workPaint;
        ReplacementSpan replacement = getReplacementAndUpdateState(spanned, textPaint, textPaint2, (CharacterStyle[]) text.getSpans(start, end, CharacterStyle.class), null, false);
        if (replacement == null) {
            CharSequence tmp;
            int tmpstart;
            int tmpend;
            if (reverse) {
                tmp = TextUtils.getReverse(text, start, end);
                tmpstart = 0;
                tmpend = end - start;
            } else {
                Object tmp2 = text;
                tmpstart = start;
                tmpend = end;
            }
            if (fmi != null) {
                workPaint.getFontMetricsInt(fmi);
            }
            if (canvas != null) {
                if (workPaint.bgColor != 0) {
                    int c = workPaint.getColor();
                    Style s = workPaint.getStyle();
                    workPaint.setColor(workPaint.bgColor);
                    workPaint.setStyle(Style.FILL);
                    if (null == null) {
                        ret = getMeasureWidth(workPaint, tmp, tmpstart, tmpend);
                        havewid = true;
                    }
                    if (dir == -1) {
                        canvas.drawRect(x - ret, (float) top, x, (float) bottom, workPaint);
                    } else {
                        canvas.drawRect(x, (float) top, x + ret, (float) bottom, workPaint);
                    }
                    workPaint.setStyle(s);
                    workPaint.setColor(c);
                }
                if (dir == -1) {
                    if (!havewid) {
                        ret = getMeasureWidth(workPaint, tmp, tmpstart, tmpend);
                    }
                    tv.mrDrawText(canvas, tmp, tmpstart, tmpend, x - ret, (float) (workPaint.baselineShift + y), workPaint);
                } else {
                    if (needwid && !havewid) {
                        ret = getMeasureWidth(workPaint, tmp, tmpstart, tmpend);
                    }
                    tv.mrDrawText(canvas, tmp, tmpstart, tmpend, x, (float) (workPaint.baselineShift + y), workPaint);
                }
            } else if (needwid && null == null) {
                ret = getMeasureWidth(workPaint, tmp, tmpstart, tmpend);
            }
        } else {
            ret = (float) replacement.getSize(workPaint, text, start, end, fmi);
            if (canvas != null) {
                if (dir == -1) {
                    replacement.draw(tv, canvas, text, start, end, x - ret, top, y, bottom, workPaint);
                } else {
                    replacement.draw(tv, canvas, text, start, end, x, top, y, bottom, workPaint);
                }
            }
        }
        if (dir == -1) {
            return -ret;
        }
        return ret;
    }

    public static ReplacementSpan getReplacementAndUpdateState(Spanned text, TextPaint paint, TextPaint workPaint, CharacterStyle[] sps, boolean measure, boolean ignoreZeroSize) {
        ReplacementSpan replacement = null;
        int i;
        if (VERSION.SDK_INT < 23) {
            for (i = sps.length - 1; i >= 0; i--) {
                replacement = doUpdateProc(paint, workPaint, sps[i], measure, ignoreZeroSize, replacement);
            }
        } else {
            sortSpansForAndroid6(text, sps);
            for (CharacterStyle doUpdateProc : sps) {
                replacement = doUpdateProc(paint, workPaint, doUpdateProc, measure, ignoreZeroSize, replacement);
            }
        }
        return replacement;
    }

    private static ReplacementSpan doUpdateProc(TextPaint paint, TextPaint workPaint, CharacterStyle span1, boolean measure, boolean ignoreZeroSize, ReplacementSpan replacement) {
        CharacterStyle span = span1;
        if (span instanceof ReplacementSpan) {
            return (ReplacementSpan) span;
        }
        if (span instanceof MyRelativeSizeSpan) {
            MyRelativeSizeSpan rp = (MyRelativeSizeSpan) span;
            if (ignoreZeroSize && rp.fontSize <= CSS.ZERO_FONTSIZE) {
                return replacement;
            }
            if (rp.inherited) {
                span.updateDrawState(workPaint);
                return replacement;
            }
            workPaint.setTextSize(paint.getTextSize() * rp.getSizeChange());
            return replacement;
        } else if (measure) {
            ((MetricAffectingSpan) span).updateMeasureState(workPaint);
            return replacement;
        } else {
            span.updateDrawState(workPaint);
            return replacement;
        }
    }

    public static void sortSpansForAndroid6(Spanned text, CharacterStyle[] spans) {
        int i = spans.length - 1;
        while (i > 0) {
            int j = 0;
            while (j < i) {
                if (text.getSpanStart(spans[i]) == text.getSpanStart(spans[j])) {
                    int here = j;
                    int k = i;
                    do {
                        CharacterStyle sp = spans[k];
                        spans[k] = spans[j];
                        spans[j] = sp;
                        k--;
                        j++;
                    } while (j < k);
                    i = here;
                    break;
                }
                j++;
            }
            i--;
        }
    }

    private static float getMeasureWidth(TextPaint workPaint, CharSequence text, int start, int end) {
        if (text == null || start == end) {
            return 0.0f;
        }
        return workPaint.measureText(text, start, end);
    }

    public static int getTextWidths(TextPaint paint, TextPaint workPaint, Spanned text, int start, int end, float[] widths, FontMetricsInt fmi) {
        workPaint.set(paint);
        Spanned spanned = text;
        TextPaint textPaint = paint;
        TextPaint textPaint2 = workPaint;
        ReplacementSpan replacement = getReplacementAndUpdateState(spanned, textPaint, textPaint2, (MetricAffectingSpan[]) text.getSpans(start, end, MetricAffectingSpan.class), true, false);
        if (replacement == null) {
            workPaint.getFontMetricsInt(fmi);
            workPaint.getTextWidths(text, start, end, widths);
        } else {
            int wid = replacement.getSize(workPaint, text, start, end, fmi);
            if (end > start) {
                widths[0] = (float) wid;
                for (int i = start + 1; i < end; i++) {
                    widths[i - start] = 0.0f;
                }
            }
        }
        return end - start;
    }

    private static float foreach(MRTextView tv, Canvas canvas, CharSequence text, int start, int end, int dir, boolean reverse, float x, int top, int y, int bottom, FontMetricsInt fmi, TextPaint paint, TextPaint workPaint, boolean needWidth) {
        if (paint == null) {
            paint = A.txtView.getPaint();
        }
        if (text instanceof Spanned) {
            Class division;
            float ox = x;
            int asc = 0;
            int desc = 0;
            int ftop = 0;
            int fbot = 0;
            Spanned sp = (Spanned) text;
            if (canvas == null) {
                division = MetricAffectingSpan.class;
            } else {
                division = CharacterStyle.class;
            }
            int i = start;
            while (i < end) {
                int next = sp.nextSpanTransition(i, end, division);
                boolean z = needWidth || next != end;
                x += each(tv, canvas, sp, i, next, dir, reverse, x, top, y, bottom, fmi, paint, workPaint, z);
                if (fmi != null) {
                    if (fmi.ascent < asc) {
                        asc = fmi.ascent;
                    }
                    if (fmi.descent > desc) {
                        desc = fmi.descent;
                    }
                    if (fmi.top < ftop) {
                        ftop = fmi.top;
                    }
                    if (fmi.bottom > fbot) {
                        fbot = fmi.bottom;
                    }
                }
                i = next;
            }
            if (fmi != null) {
                if (start == end) {
                    paint.getFontMetricsInt(fmi);
                } else {
                    fmi.ascent = asc;
                    fmi.descent = desc;
                    fmi.top = ftop;
                    fmi.bottom = fbot;
                }
            }
            return x - ox;
        }
        float ret = 0.0f;
        if (reverse) {
            CharSequence tmp = TextUtils.getReverse(text, start, end);
            int tmpend = end - start;
            if (canvas != null || needWidth) {
                ret = paint.measureText(tmp, 0, tmpend);
            }
            if (canvas != null) {
                tv.mrDrawText(canvas, tmp, 0, tmpend, x - ret, (float) y, paint);
            }
        } else {
            if (needWidth) {
                ret = getMeasureWidth(paint, text, start, end);
            }
            if (canvas != null) {
                tv.mrDrawText(canvas, text, start, end, x, (float) y, paint);
            }
        }
        if (fmi != null) {
            paint.getFontMetricsInt(fmi);
        }
        return ((float) dir) * ret;
    }

    static float drawText(MRTextView tv, Canvas canvas, CharSequence text, int start, int end, int direction, boolean reverse, float x, int top, int y, int bottom, TextPaint paint, TextPaint workPaint, boolean needWidth) {
        boolean isRTL = direction == -1;
        if ((!isRTL || null != null) && (null == null || isRTL)) {
            return foreach(tv, canvas, text, start, end, direction, false, x, top, y, bottom, null, paint, workPaint, needWidth);
        }
        float ch = foreach(tv, null, text, start, end, 1, false, 0.0f, 0, 0, 0, null, paint, workPaint, true) * ((float) direction);
        foreach(tv, canvas, text, start, end, -direction, false, x + ch, top, y, bottom, null, paint, workPaint, true);
        return ch;
    }

    public static float drawText(MRTextView tv, Canvas canvas, CharSequence text, int start, int end, int direction, float x, int top, int y, int bottom, TextPaint paint, TextPaint workPaint, boolean needWidth) {
        return drawText(tv, canvas, text, start, end, direction >= 0 ? 1 : -1, false, x, top, y, bottom, paint, workPaint, needWidth);
    }

    public static float measureText(MRTextView tv, TextPaint paint, TextPaint workPaint, CharSequence text, int start, int end, FontMetricsInt fmi) {
        return foreach(tv, null, text, start, end, 1, false, 0.0f, 0, 0, 0, fmi, paint, workPaint, true);
    }
}