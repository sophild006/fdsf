package com.flyersoft.staticlayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.widget.AutoScrollHelper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ParagraphStyle;
import android.text.style.ReplacementSpan;
import android.text.style.TabStopSpan;
import android.util.SparseIntArray;
import android.widget.ScrollView;
import com.flyersoft.books.A;
import com.flyersoft.books.T;
import com.flyersoft.components.CSS;
import com.flyersoft.staticlayout.TextUtils.TruncateAt;
import java.util.ArrayList;
import java.util.Map;

public abstract class MyLayout {
    private static final boolean DEBUG = false;
    static final Directions DIRS_ALL_LEFT_TO_RIGHT = new Directions(new short[]{Short.MAX_VALUE});
    static final Directions DIRS_ALL_RIGHT_TO_LEFT = new Directions(new short[]{(short) 0, Short.MAX_VALUE});
    public static final int DIR_LEFT_TO_RIGHT = 1;
    public static final int DIR_RIGHT_TO_LEFT = -1;
    static final int MAX_EMOJI = -1;
    static final int MIN_EMOJI = -1;
    private static final int TAB_INCREMENT = 20;
    private static Rect sTempRect = new Rect();
    Map<Integer, MyFloatSpan> floatLineSpans;
    SparseIntArray floatLines;
    SparseIntArray hyphLines;
    private Alignment mAlignment = Alignment.ALIGN_LEFT;
    private RectF mEmojiRect;
    private TextPaint mPaint;
    private float mSpacingAdd;
    private float mSpacingMult;
    private boolean mSpannedText;
    CharSequence mText;
    private int mWidth;
    TextPaint mWorkPaint;
    SparseIntArray topAddedLines;
    SparseIntArray topAddedLines2;
    public MRTextView tv;

    public enum Alignment {
        ALIGN_LEFT,
        ALIGN_JUSTIFY,
        ALIGN_CENTER,
        ALIGN_RIGHT
    }

    public static class Directions {
        private short[] mDirections;

        Directions(short[] dirs) {
            this.mDirections = dirs;
        }
    }

    static class Ellipsizer implements CharSequence, GetChars {
        MyLayout mLayout;
        TruncateAt mMethod;
        CharSequence mText;
        int mWidth;

        public Ellipsizer(CharSequence s) {
            this.mText = s;
        }

        public char charAt(int off) {
            char[] buf = TextUtils.obtain(1);
            getChars(off, off + 1, buf, 0);
            char ret = buf[0];
            TextUtils.recycle(buf);
            return ret;
        }

        public void getChars(int start, int end, char[] dest, int destoff) {
            int line1 = this.mLayout.getLineForOffset(start);
            int line2 = this.mLayout.getLineForOffset(end);
            TextUtils.getChars(this.mText, start, end, dest, destoff);
            for (int i = line1; i <= line2; i++) {
                this.mLayout.ellipsize(start, end, i, dest, destoff);
            }
        }

        public int length() {
            return this.mText.length();
        }

        public CharSequence subSequence(int start, int end) {
            char[] s = new char[(end - start)];
            getChars(start, end, s, 0);
            return new String(s);
        }

        public String toString() {
            char[] s = new char[length()];
            getChars(0, length(), s, 0);
            return new String(s);
        }
    }

    static class SpannedEllipsizer extends Ellipsizer implements Spanned {
        private Spanned mSpanned;

        public SpannedEllipsizer(CharSequence display) {
            super(display);
            this.mSpanned = (Spanned) display;
        }

        public <T> T[] getSpans(int start, int end, Class<T> type) {
            return this.mSpanned.getSpans(start, end, type);
        }

        public int getSpanStart(Object tag) {
            return this.mSpanned.getSpanStart(tag);
        }

        public int getSpanEnd(Object tag) {
            return this.mSpanned.getSpanEnd(tag);
        }

        public int getSpanFlags(Object tag) {
            return this.mSpanned.getSpanFlags(tag);
        }

        public int nextSpanTransition(int start, int limit, Class type) {
            return this.mSpanned.nextSpanTransition(start, limit, type);
        }

        public CharSequence subSequence(int start, int end) {
            char[] s = new char[(end - start)];
            getChars(start, end, s, 0);
            SpannableString ss = new SpannableString(new String(s));
            TextUtils.copySpansFrom(this.mSpanned, start, end, Object.class, ss, 0);
            return ss;
        }
    }

    public abstract int getBottomPadding();

    public abstract int getEllipsisCount(int i);

    public abstract int getEllipsisStart(int i);

    public abstract boolean getLineContainsTab(int i);

    public abstract int getLineCount();

    public abstract int getLineDescent(int i);

    public abstract Directions getLineDirections(int i);

    public abstract int getLineStart(int i);

    public abstract int getLineTop(int i);

    public abstract int getParagraphDirection(int i);

    public abstract int getTopPadding();

    public void setLineSep(int line, int v) {
        if (this.hyphLines != null) {
            this.hyphLines.put(line, v);
        }
    }

    public int getLineSep(int line) {
        if (this.hyphLines == null) {
            return 0;
        }
        return this.hyphLines.get(line);
    }

    public void removeLineSep(int line) {
        if (this.hyphLines != null) {
            this.hyphLines.delete(line);
        }
    }

    public void setLineTopAdded(int line, int v) {
        if (this.topAddedLines != null) {
            this.topAddedLines.put(line, v);
        }
    }

    public int getLineTopAdded(int line) {
        if (this.topAddedLines == null) {
            return 0;
        }
        return this.topAddedLines.get(line);
    }

    public void setLineTopAdded2(int line, int v) {
        if (this.topAddedLines2 != null) {
            this.topAddedLines2.put(line, v);
        }
    }

    public int getLineTopAdded2(int line) {
        if (this.topAddedLines2 == null) {
            return 0;
        }
        return this.topAddedLines2.get(line);
    }

    public void setLineFloat(int line, MyFloatSpan sp, int value) {
        if (this.floatLines != null) {
            this.floatLines.put(line, value);
            this.floatLineSpans.put(Integer.valueOf(line), sp);
        }
    }

    public int getLineFloat(int line) {
        if (this.floatLines == null) {
            return 0;
        }
        return this.floatLines.get(line);
    }

    public MyFloatSpan getLineFloatSp(int line) {
        if (this.floatLineSpans == null) {
            return null;
        }
        return (MyFloatSpan) this.floatLineSpans.get(Integer.valueOf(line));
    }

    public static float getDesiredWidth(MRTextView tv, CharSequence source, TextPaint paint) {
        return getDesiredWidth(tv, source, 0, source.length(), paint);
    }

    public static float getDesiredWidth(MRTextView tv, CharSequence source, int start, int end, TextPaint paint) {
        float need = 0.0f;
        TextPaint workPaint = new TextPaint();
        int i = start;
        while (i <= end) {
            int next = TextUtils.indexOf(source, '\n', i, end);
            if (next < 0) {
                next = end;
            }
            float w = measureText(tv, paint, workPaint, source, i, next, null, true, null);
            if (w > need) {
                need = w;
            }
            i = next + 1;
        }
        return need;
    }

    protected MyLayout(MyTextView tv, CharSequence text, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd) {
        if (width < 0) {
            throw new IllegalArgumentException("Layout: " + width + " < 0");
        }
        this.tv = (MRTextView) tv;
        this.mText = text;
        this.mPaint = paint;
        this.mWorkPaint = new TextPaint();
        this.mWidth = width;
        this.mAlignment = align;
        this.mSpacingMult = spacingmult;
        this.mSpacingAdd = spacingadd;
        this.mSpannedText = text instanceof Spanned;
    }

    void replaceWith(CharSequence text, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd) {
        if (width < 0) {
            throw new IllegalArgumentException("Layout: " + width + " < 0");
        }
        this.mText = text;
        this.mPaint = paint;
        this.mWidth = width;
        this.mAlignment = align;
        this.mSpacingMult = spacingmult;
        this.mSpacingAdd = spacingadd;
        this.mSpannedText = text instanceof Spanned;
    }

    public void draw(Canvas c) {
        draw(c, null, null, 0);
    }

    public void draw(Canvas c, Path highlight, Paint highlightpaint, int cursorOffsetVertical) {
        ScrollView sv = this.tv.getScrollView();
        int first = getLineForVertical(sv.getScrollY());
        int last = getLineForVertical(sv.getScrollY() + this.tv.getTxtHeight());
        int previousLineBottom = getLineTop(first);
        int previousLineEnd = getLineStart(first);
        ParagraphStyle[] spans = (ParagraphStyle[]) ArrayUtils.emptyArray(ParagraphStyle.class);
        int max_margin = (int) (((float) this.tv.getWidth()) * 0.65f);
        int i;
        int start;
        int end;
        int ltop;
        int lbottom;
        int lbaseline;
        int length;
        int n;
        if (this.tv.drawMarginBackgroundOnly) {
            ArrayList<MyMarginSpan> drawMargins = null;
            for (i = first; i <= last; i++) {
                start = previousLineEnd;
                previousLineEnd = getLineStart(i + 1);
                end = getLineVisibleEnd(i, start, previousLineEnd);
                ltop = previousLineBottom;
                lbottom = getLineTop(i + 1);
                previousLineBottom = lbottom;
                lbaseline = lbottom - getLineDescent(i);
                this.tv.removeLineSpans(i);
                spans = (ParagraphStyle[]) T.getSpans(this.tv.getLineSpans(i), ParagraphStyle.class);
                length = spans.length;
                for (n = 0; n < length; n++) {
                    if (spans[n] instanceof MyMarginSpan) {
                        MyMarginSpan margin = (MyMarginSpan) spans[n];
                        margin.bottom = lbottom;
                        margin.end = end;
                        margin.endLine = i;
                        if (drawMargins == null) {
                            drawMargins = new ArrayList();
                        }
                        if (!drawMargins.contains(margin)) {
                            margin.top = ltop;
                            margin.start = start;
                            margin.startLine = i;
                            margin.baseline = lbaseline;
                            drawMargins.add(margin);
                        }
                    }
                }
                if (getLineFloat(i) != 0) {
                    MyFloatSpan floatSp = getLineFloatSp(i);
                    if (drawMargins == null) {
                        drawMargins = new ArrayList();
                    }
                    if (!(floatSp == null || drawMargins.contains(floatSp))) {
                        drawMargins.add(floatSp);
                    }
                }
            }
            if (drawMargins != null) {
                MyMarginSpan.sortDrawMargins(drawMargins);
                for (i = drawMargins.size() - 1; i >= 0; i--) {
                    ((MyMarginSpan) drawMargins.get(i)).drawFrameMargin(c, this.mPaint, this.tv, drawMargins, i);
                }
                return;
            }
            return;
        }
        Alignment align = this.mAlignment;
        for (i = first; i <= last; i++) {
            int x;
            start = previousLineEnd;
            previousLineEnd = getLineStart(i + 1);
            end = getLineVisibleEnd(i, start, previousLineEnd);
            ltop = previousLineBottom;
            lbottom = getLineTop(i + 1);
            previousLineBottom = lbottom;
            lbaseline = lbottom - getLineDescent(i);
            boolean par = start == 0 || this.mText.charAt(start - 1) == '\n';
            if (this.mSpannedText) {
                Spanned spanned = (Spanned) this.mText;
                spans = (ParagraphStyle[]) T.getSpans(this.tv.getLineSpans(i), ParagraphStyle.class);
                align = this.mAlignment;
                AlignmentSpan a = null;
                Integer off = null;
                for (n = 0; n < spans.length; n++) {
                    if (spans[n] instanceof AlignmentSpan) {
                        AlignmentSpan a2 = (AlignmentSpan) spans[n];
                        if (a == null) {
                            a = a2;
                        } else {
                            if (off == null) {
                                off = Integer.valueOf((start - spanned.getSpanStart(a)) + (spanned.getSpanEnd(a) - end));
                            }
                            int off2 = (start - spanned.getSpanStart(a2)) + (spanned.getSpanEnd(a2) - end);
                            if (off2 < off.intValue()) {
                                a = a2;
                                off = Integer.valueOf(off2);
                            }
                        }
                    }
                }
                if (a != null) {
                    align = a.getAlignment();
                }
            }
            int dir = getParagraphDirection(i);
            int left = 0;
            int right = this.mWidth;
            int indent = 0;
            MyMarginSpan indentSp = null;
            if (this.mSpannedText) {
                length = spans.length;
                for (n = 0; n < length; n++) {
                    if (spans[n] instanceof MyMarginSpan) {
                        MyMarginSpan sp = (MyMarginSpan) spans[n];
                        left = (int) (((float) left) + (sp.leftMargin * CSS.EM()));
                        if (par && sp.indent != 0.0f && (indentSp == null || sp.spStart > indentSp.spStart)) {
                            indentSp = sp;
                            indent = (int) (sp.indent * CSS.EM2());
                        }
                        if (left > max_margin) {
                            left = max_margin;
                        }
                    }
                }
                left += indent;
                int floatWidth = getLineFloat(i);
                if (floatWidth > 0) {
                    left += floatWidth;
                }
                if (left > max_margin) {
                    left = max_margin;
                }
                if (left < 0) {
                    left = 0;
                }
            }
            if (par && A.indentParagraph) {
                left = (int) (((float) left) + this.tv.indentWidth());
            }
            if (!alignNormal(align)) {
                int max = (int) getLineMax(i, spans, false);
                if (align == Alignment.ALIGN_RIGHT) {
                    int off3 = A.textHyphenation ? this.mWidth - SoftHyphenStaticLayout.mInnerWidth : 0;
                    if (dir == -1) {
                        x = left + max;
                    } else {
                        x = (right - max) - off3;
                    }
                } else {
                    int half = ((right - left) - (max & -2)) >> 1;
                    if (dir == -1) {
                        x = right - half;
                    } else {
                        x = left + half;
                    }
                }
            } else if (dir == 1) {
                x = left;
            } else {
                x = right;
            }
            Directions directions = getLineDirections(i);
            boolean hasTab = getLineContainsTab(i);
            if (directions != DIRS_ALL_LEFT_TO_RIGHT || this.mSpannedText || hasTab) {
                drawText(c, this.mText, start, end, dir, directions, (float) x, ltop, lbaseline, lbottom, this.mPaint, this.mWorkPaint, hasTab, spans);
            } else {
                c.drawText(this.mText, start, end, (float) x, (float) lbaseline, this.mPaint);
            }
        }
    }

    public static boolean alignNormal(Alignment align) {
        return align == Alignment.ALIGN_LEFT || align == Alignment.ALIGN_JUSTIFY;
    }

    public final CharSequence getText() {
        return this.mText;
    }

    public final TextPaint getPaint() {
        return this.mPaint;
    }

    public final int getWidth() {
        return this.mWidth;
    }

    public int getEllipsizedWidth() {
        return this.mWidth;
    }

    public final void increaseWidthTo(int wid) {
        if (wid < this.mWidth) {
            throw new RuntimeException("attempted to reduce Layout width");
        }
        this.mWidth = wid;
    }

    public int getHeight() {
        return getLineTop(getLineCount());
    }

    public final Alignment getAlignment() {
        return this.mAlignment;
    }

    public final float getSpacingMultiplier() {
        return this.mSpacingMult;
    }

    public final float getSpacingAdd() {
        return this.mSpacingAdd;
    }

    public int getLineBounds(int line, Rect bounds) {
        if (bounds != null) {
            bounds.left = 0;
            bounds.top = getLineTop(line);
            bounds.right = this.mWidth;
            bounds.bottom = getLineBottom(line);
        }
        return getLineBaseline(line);
    }

    public float getPrimaryHorizontal(int offset) {
        return getHorizontal(offset, false, true);
    }

    public float getSecondaryHorizontal(int offset) {
        return getHorizontal(offset, true, true);
    }

    private float getHorizontal(int offset, boolean trailing, boolean alt) {
        return getHorizontal(offset, trailing, alt, getLineForOffset(offset));
    }

    private float getHorizontal(int offset, boolean trailing, boolean alt, int line) {
        int start = getLineStart(line);
        int end = getLineVisibleEnd(line);
        int dir = getParagraphDirection(line);
        boolean tab = getLineContainsTab(line);
        Directions directions = getLineDirections(line);
        TabStopSpan[] tabs = null;
        if (tab && (this.mText instanceof Spanned)) {
            tabs = (TabStopSpan[]) T.getSpans(this.tv.getLineSpans(line), TabStopSpan.class);
        }
        float wid = measureText(this.tv, this.mPaint, this.mWorkPaint, this.mText, start, offset, end, dir, directions, trailing, alt, tab, tabs);
        if (offset > end) {
            if (dir == -1) {
                wid -= measureText(this.tv, this.mPaint, this.mWorkPaint, this.mText, end, offset, null, tab, tabs);
            } else {
                wid += measureText(this.tv, this.mPaint, this.mWorkPaint, this.mText, end, offset, null, tab, tabs);
            }
        }
        Alignment align = getParagraphAlignment(line);
        int left = getParagraphLeft(line);
        int right = getParagraphRight(line);
        if (!alignNormal(align)) {
            float max = getLineMax(line);
            if (align != Alignment.ALIGN_RIGHT) {
                int imax = ((int) max) & -2;
                if (dir == -1) {
                    return ((float) (right - (((right - left) - imax) / 2))) + wid;
                }
                return ((float) ((((right - left) - imax) / 2) + left)) + wid;
            } else if (dir == -1) {
                return (((float) left) + max) + wid;
            } else {
                return (((float) right) - max) + wid;
            }
        } else if (dir == -1) {
            return ((float) right) + wid;
        } else {
            return ((float) left) + wid;
        }
    }

    public float getLineLeft(int line) {
        int dir = getParagraphDirection(line);
        Alignment align = getParagraphAlignment(line);
        if (alignNormal(align)) {
            if (dir == -1) {
                return ((float) getParagraphRight(line)) - getLineMax(line);
            }
            return 0.0f;
        } else if (align != Alignment.ALIGN_RIGHT) {
            int left = getParagraphLeft(line);
            return (float) ((((getParagraphRight(line) - left) - (((int) getLineMax(line)) & -2)) / 2) + left);
        } else if (dir != -1) {
            return ((float) this.mWidth) - getLineMax(line);
        } else {
            return 0.0f;
        }
    }

    public float getLineRight(int line) {
        int dir = getParagraphDirection(line);
        Alignment align = getParagraphAlignment(line);
        if (alignNormal(align)) {
            if (dir == -1) {
                return (float) this.mWidth;
            }
            return ((float) getParagraphLeft(line)) + getLineMax(line);
        } else if (align != Alignment.ALIGN_RIGHT) {
            int left = getParagraphLeft(line);
            int right = getParagraphRight(line);
            return (float) (right - (((right - left) - (((int) getLineMax(line)) & -2)) / 2));
        } else if (dir == -1) {
            return getLineMax(line);
        } else {
            return (float) this.mWidth;
        }
    }

    public float getLineMax(int line) {
        return getLineMax(line, null, false);
    }

    public float getLineWidth(int line) {
        return getLineMax(line, null, true);
    }

    private float getLineMax(int line, Object[] tabs, boolean full) {
        int end;
        int start = getLineStart(line);
        if (full) {
            end = getLineEnd(line);
        } else {
            end = getLineVisibleEnd(line);
        }
        boolean tab = getLineContainsTab(line);
        if (tabs == null && tab && (this.mText instanceof Spanned)) {
            tabs = T.getSpans(this.tv.getLineSpans(line), TabStopSpan.class);
        }
        return measureText(this.tv, this.mPaint, this.mWorkPaint, this.mText, start, end, null, tab, tabs);
    }

    public int getLineForVertical(int vertical) {
        int high = getLineCount();
        int low = -1;
        while (high - low > 1) {
            int guess = (high + low) / 2;
            if (getLineTop(guess) > vertical) {
                high = guess;
            } else {
                low = guess;
            }
        }
        if (low < 0) {
            return 0;
        }
        return low;
    }

    public int getLineForOffset(int offset) {
        int high = getLineCount();
        int low = -1;
        while (high - low > 1) {
            int guess = (high + low) / 2;
            if (getLineStart(guess) > offset) {
                high = guess;
            } else {
                low = guess;
            }
        }
        if (low < 0) {
            return 0;
        }
        return low;
    }

    public int getOffsetForHorizontal(int line, float horiz) {
        float dist;
        int max = getLineEnd(line) - 1;
        int min = getLineStart(line);
        Directions dirs = getLineDirections(line);
        if (line == getLineCount() - 1) {
            max++;
        }
        int best = min;
        float bestdist = Math.abs(getPrimaryHorizontal(best) - horiz);
        int here = min;
        if (dirs.mDirections != null) {
            for (int i = 0; i < dirs.mDirections.length; i++) {
                int there = here + dirs.mDirections[i];
                int swap = (i & 1) == 0 ? 1 : -1;
                if (there > max) {
                    there = max;
                }
                int high = (there - 1) + 1;
                int low = (here + 1) - 1;
                while (high - low > 1) {
                    int guess = (high + low) / 2;
                    if (getPrimaryHorizontal(getOffsetAtStartOf(guess)) * ((float) swap) >= ((float) swap) * horiz) {
                        high = guess;
                    } else {
                        low = guess;
                    }
                }
                if (low < here + 1) {
                    low = here + 1;
                }
                if (low < there) {
                    low = getOffsetAtStartOf(low);
                    dist = Math.abs(getPrimaryHorizontal(low) - horiz);
                    int aft = TextUtils.getOffsetAfter(this.mText, low);
                    if (aft < there) {
                        float other = Math.abs(getPrimaryHorizontal(aft) - horiz);
                        if (other < dist) {
                            dist = other;
                            low = aft;
                        }
                    }
                    if (dist < bestdist) {
                        bestdist = dist;
                        best = low;
                    }
                }
                dist = Math.abs(getPrimaryHorizontal(here) - horiz);
                if (dist < bestdist) {
                    bestdist = dist;
                    best = here;
                }
                here = there;
            }
        }
        dist = Math.abs(getPrimaryHorizontal(max) - horiz);
        if (dist >= bestdist) {
            return best;
        }
        bestdist = dist;
        return max;
    }

    public final int getLineEnd(int line) {
        return getLineStart(line + 1);
    }

    public int getLineVisibleEnd(int line) {
        return getLineVisibleEnd(line, getLineStart(line), getLineStart(line + 1));
    }

    private int getLineVisibleEnd(int line, int start, int end) {
        CharSequence text = this.mText;
        if (line == getLineCount() - 1) {
            return end;
        }
        while (end > start) {
            if (text != null) {
                char ch = text.charAt(end - 1);
                if (ch != '\n') {
                    if (ch != ' ' && ch != '\t') {
                        break;
                    }
                    end--;
                } else {
                    return end - 1;
                }
            }
            return end;
        }
        return end;
    }

    public final int getLineBottom(int line) {
        return getLineTop(line + 1);
    }

    public final int getLineBaseline(int line) {
        return getLineTop(line + 1) - getLineDescent(line);
    }

    public final int getLineAscent(int line) {
        return getLineTop(line) - (getLineTop(line + 1) - getLineDescent(line));
    }

    private int getOffsetAtStartOf(int offset) {
        if (offset == 0 || this.mText == null) {
            return 0;
        }
        CharSequence text = this.mText;
        char c = text.charAt(offset);
        if (c >= '�' && c <= '�') {
            char c1 = text.charAt(offset - 1);
            if (c1 >= '�' && c1 <= '�') {
                offset--;
            }
        }
        if (this.mSpannedText) {
            ReplacementSpan[] spans = (ReplacementSpan[]) ((Spanned) text).getSpans(offset, offset, ReplacementSpan.class);
            for (int i = 0; i < spans.length; i++) {
                int start = ((Spanned) text).getSpanStart(spans[i]);
                int end = ((Spanned) text).getSpanEnd(spans[i]);
                if (start < offset && end > offset) {
                    offset = start;
                }
            }
        }
        return offset;
    }

    public final Alignment getParagraphAlignment(int line) {
        return MRTextView.getAlignment(this.tv.getLineAlign(line));
    }

    public final int getParagraphLeft(int line) {
        int dir = getParagraphDirection(line);
        int left = 0;
        if (this.mText == null) {
            return 0;
        }
        if (dir == 1 && this.mSpannedText) {
            for (MyMarginSpan myMarginSpan : (MyMarginSpan[]) T.getSpans(this.tv.getLineSpans(line), MyMarginSpan.class)) {
                left = (int) (((float) left) + (myMarginSpan.leftMargin * CSS.EM()));
            }
        }
        return left;
    }

    public final int getParagraphRight(int line) {
        int dir = getParagraphDirection(line);
        int right = this.mWidth;
        if (this.mText == null) {
            return right;
        }
        if (dir == -1 && this.mSpannedText) {
            for (MyMarginSpan myMarginSpan : (MyMarginSpan[]) T.getSpans(this.tv.getLineSpans(line), MyMarginSpan.class)) {
                right = (int) (((float) right) - (myMarginSpan.leftMargin * CSS.EM()));
            }
        }
        return right;
    }

    private void drawText(Canvas canvas, CharSequence text, int start, int end, int dir, Directions directions, float x, int top, int y, int bottom, TextPaint paint, TextPaint workPaint, boolean hasTabs, Object[] parspans) {
        if (dir == -1) {
            Styled.drawText(this.tv, canvas, text, start, end, dir, false, x, top, y, bottom, paint, workPaint, false);
            return;
        }
        char[] buf;
        if (hasTabs) {
            buf = TextUtils.obtain(end - start);
            TextUtils.getChars(text, start, end, buf, 0);
        } else {
            buf = null;
        }
        float h = 0.0f;
        int here = 0;
        if (!(directions == null || directions.mDirections == null)) {
            for (int i = 0; i < directions.mDirections.length; i++) {
                int there = here + directions.mDirections[i];
                if (there > end - start) {
                    there = end - start;
                }
                int segstart = here;
                int j = hasTabs ? here : there;
                while (j <= there) {
                    if (j == there || buf[j] == '\t') {
                        boolean z;
                        MRTextView mRTextView = this.tv;
                        int i2 = start + segstart;
                        int i3 = start + j;
                        boolean z2 = (i & 1) != 0;
                        float f = x + h;
                        if (start + j != end) {
                            z = true;
                        } else {
                            z = false;
                        }
                        h += Styled.drawText(mRTextView, canvas, text, i2, i3, dir, z2, f, top, y, bottom, paint, workPaint, z);
                        if (j != there && buf[j] == '\t') {
                            h = ((float) dir) * nextTab(text, start, end, ((float) dir) * h, parspans);
                        }
                        segstart = j + 1;
                    }
                    j++;
                }
                here = there;
            }
        }
        if (hasTabs) {
            TextUtils.recycle(buf);
        }
    }

    private static float measureText(MRTextView tv, TextPaint paint, TextPaint workPaint, CharSequence text, int start, int offset, int end, int dir, Directions directions, boolean trailing, boolean alt, boolean hasTabs, Object[] tabs) {
        char[] buf = null;
        if (hasTabs) {
            buf = TextUtils.obtain(end - start);
            TextUtils.getChars(text, start, end, buf, 0);
        }
        float h = 0.0f;
        if (alt && dir == -1) {
            trailing = !trailing;
        }
        int here = 0;
        if (!(directions == null || directions.mDirections == null)) {
            int i = 0;
            while (i < directions.mDirections.length) {
                if (alt) {
                    trailing = !trailing;
                }
                int there = here + directions.mDirections[i];
                if (there > end - start) {
                    there = end - start;
                }
                int segstart = here;
                int j = hasTabs ? here : there;
                while (j <= there) {
                    int codept = 0;
                    Bitmap bm = null;
                    if (hasTabs && j < there) {
                        codept = buf[j];
                    }
                    if (codept >= 55296 && codept <= 57343 && j + 1 < there) {
                        codept = Character.codePointAt(buf, j);
                        if (codept >= MIN_EMOJI && codept <= MAX_EMOJI) {
                            bm = null;
                        }
                    }
                    if (j == there || codept == 9 || bm != null) {
                        if (offset < start + j || (trailing && offset <= start + j)) {
                            if (dir == 1 && (i & 1) == 0) {
                                return h + Styled.measureText(tv, paint, workPaint, text, start + segstart, offset, null);
                            } else if (dir == -1 && (i & 1) != 0) {
                                return h - Styled.measureText(tv, paint, workPaint, text, start + segstart, offset, null);
                            }
                        }
                        float segw = Styled.measureText(tv, paint, workPaint, text, start + segstart, start + j, null);
                        if (offset < start + j || (trailing && offset <= start + j)) {
                            if (dir == 1) {
                                return h + (segw - Styled.measureText(tv, paint, workPaint, text, start + segstart, offset, null));
                            } else if (dir == -1) {
                                return h - (segw - Styled.measureText(tv, paint, workPaint, text, start + segstart, offset, null));
                            }
                        }
                        if (dir == -1) {
                            h -= segw;
                        } else {
                            h += segw;
                        }
                        if (j != there && buf[j] == '\t') {
                            if (offset == start + j) {
                                return h;
                            }
                            h = ((float) dir) * nextTab(text, start, end, ((float) dir) * h, tabs);
                        }
                        if (bm != null) {
                            workPaint.set(paint);
                            Styled.measureText(tv, paint, workPaint, text, j, j + 2, null);
                            float wid = (((float) bm.getWidth()) * (-workPaint.ascent())) / ((float) bm.getHeight());
                            if (dir == -1) {
                                h -= wid;
                            } else {
                                h += wid;
                            }
                            j++;
                        }
                        segstart = j + 1;
                    }
                    j++;
                }
                here = there;
                i++;
            }
        }
        if (hasTabs) {
            TextUtils.recycle(buf);
        }
        return h;
    }

    static float measureText(MRTextView tv, TextPaint paint, TextPaint workPaint, CharSequence text, int start, int end, FontMetricsInt fm, boolean hasTabs, Object[] tabs) {
        char[] buf = null;
        if (hasTabs) {
            buf = TextUtils.obtain(end - start);
            TextUtils.getChars(text, start, end, buf, 0);
        }
        int len = end - start;
        int here = 0;
        float h = 0.0f;
        int ab = 0;
        int be = 0;
        int top = 0;
        int bot = 0;
        if (fm != null) {
            fm.ascent = 0;
            fm.descent = 0;
        }
        int i = hasTabs ? 0 : len;
        while (i <= len) {
            int codept = 0;
            Bitmap bm = null;
            if (hasTabs && i < len) {
                codept = buf[i];
            }
            if (codept >= 55296 && codept <= 57343 && i < len) {
                codept = Character.codePointAt(buf, i);
                if (codept >= MIN_EMOJI && codept <= MAX_EMOJI) {
                    bm = null;
                }
            }
            if (i == len || codept == 9 || bm != null) {
                workPaint.baselineShift = 0;
                h += Styled.measureText(tv, paint, workPaint, text, start + here, start + i, fm);
                if (fm != null) {
                    if (workPaint.baselineShift < 0) {
                        fm.ascent += workPaint.baselineShift;
                        fm.top += workPaint.baselineShift;
                    } else {
                        fm.descent += workPaint.baselineShift;
                        fm.bottom += workPaint.baselineShift;
                    }
                }
                if (i != len) {
                    if (bm == null) {
                        h = nextTab(text, start, end, h, tabs);
                    } else {
                        workPaint.set(paint);
                        Styled.measureText(tv, paint, workPaint, text, start + i, (start + i) + 1, null);
                        h += (((float) bm.getWidth()) * (-workPaint.ascent())) / ((float) bm.getHeight());
                        i++;
                    }
                }
                if (fm != null) {
                    if (fm.ascent < ab) {
                        ab = fm.ascent;
                    }
                    if (fm.descent > be) {
                        be = fm.descent;
                    }
                    if (fm.top < top) {
                        top = fm.top;
                    }
                    if (fm.bottom > bot) {
                        bot = fm.bottom;
                    }
                }
                here = i + 1;
            }
            i++;
        }
        if (fm != null) {
            fm.ascent = ab;
            fm.descent = be;
            fm.top = top;
            fm.bottom = bot;
        }
        if (hasTabs) {
            TextUtils.recycle(buf);
        }
        return h;
    }

    static float nextTab(CharSequence text, int start, int end, float h, Object[] tabs) {
        float nh = AutoScrollHelper.NO_MAX;
        boolean alltabs = false;
        if (text instanceof Spanned) {
            if (tabs == null) {
                tabs = ((Spanned) text).getSpans(start, end, TabStopSpan.class);
                alltabs = true;
            }
            int i = 0;
            while (i < tabs.length) {
                if (alltabs || (tabs[i] instanceof TabStopSpan)) {
                    int where = ((TabStopSpan) tabs[i]).getTabStop();
                    if (((float) where) < nh && ((float) where) > h) {
                        nh = (float) where;
                    }
                }
                i++;
            }
            if (nh != AutoScrollHelper.NO_MAX) {
                return nh;
            }
        }
        return (float) (((int) ((h + 20.0f) / 20.0f)) * 20);
    }

    protected final boolean isSpanned() {
        return this.mSpannedText;
    }

    private void ellipsize(int start, int end, int line, char[] dest, int destoff) {
        int ellipsisCount = getEllipsisCount(line);
        if (ellipsisCount != 0) {
            int ellipsisStart = getEllipsisStart(line);
            int linestart = getLineStart(line);
            for (int i = ellipsisStart; i < ellipsisStart + ellipsisCount; i++) {
                char c;
                if (i == ellipsisStart) {
                    c = '…';
                } else {
                    c = '﻿';
                }
                int a = i + linestart;
                if (a >= start && a < end) {
                    dest[(destoff + a) - start] = c;
                }
            }
        }
    }
}