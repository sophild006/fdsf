package com.flyersoft.staticlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.text.Layout;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;
import com.flyersoft.books.A;
import com.flyersoft.books.A.Bookmark;
import com.flyersoft.books.A.Bookmarks;
import com.flyersoft.books.BookDb.NoteInfo;
import com.flyersoft.books.T;
import com.flyersoft.components.CSS;
import com.flyersoft.components.CSS.BackgroundColorSpan;
import com.flyersoft.components.CSS.PAGE_BREAK;
import com.flyersoft.moonreader.ActivityTxt;
import com.flyersoft.moonreader.R;
import com.flyersoft.staticlayout.MyLayout.Alignment;
import com.flyersoft.staticlayout.MyLayout.Directions;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MRTextView extends MyTextView {
    static String HYPH_RIGHT_PUNCS1 = "-,.;?\"':)}]!，。；＂”“：？）、！’‘»>";
    static String HYPH_RIGHT_PUNCS2 = "-,.!";
    private static float cnCharWidth;
    private static float cnDotWidth;
    private static boolean dontAdjustDot;
    public static int global_alignment;
    static boolean isPdf;
    static boolean isTxt;
    public static HashMap<Integer, LineRecord> lrCache;
    static ArrayList<MRSpanLine> mrSpanLines;
    private static boolean vertOld;
    public int appendLineCount;
    public boolean disableDraw;
    private boolean forceRedraw;
    public int hEnd;
    public int hStart = -1;
    private boolean hardwareAcceleratedChecked;
    ArrayList<NoteInfo> highlightAll;
    private ArrayList<Integer> highlightLines = new ArrayList();
    public float indentWidth;
    private float italicIgnoreWidth;
    private int lastBorderTopLine;
    private int lastIgnoreLine;
    int lastTxtChapterLine = -100;
    public int layoutState;
    Map<Integer, Integer> lineAligns = new HashMap();
    private Map<Integer, TextPaint> lineHashPaints = new HashMap();
    private int lineHeight2;
    Map<Integer, MarginF> lineMargins = new HashMap();
    Map<Integer, Object[]> lineSpans = new HashMap();
    private Map<Integer, Boolean> lineUnherited = new HashMap();
    private int mBottom2;
    private int mLineHeight;
    private int mLineHeight2;
    private Bitmap noteBm;
    private Bitmap noteBm2;
    private String pureText;
    public ScrollView scrollView;
    public String selectedText;
    private boolean selfJustified;
    Drawable tableIcon;
    public ArrayList<Bookmark> visualBookmarks = new ArrayList();

    class LineRecord {
        char[] chars;
        float[] ends;
        float[] starts;

        LineRecord() {
        }
    }

    static class MRSpan {
        int end;
        TextPaint paint;
        int start;
        String text;
        float width;
        float x;
        float y;

        public MRSpan(String text, float x, float y, TextPaint paint, int start, int end) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.paint = paint;
            this.start = start;
            this.end = end;
        }
    }

    static class MRSpanLine {
        int line;
        ArrayList<MRSpan> mrSpans = new ArrayList();

        MRSpanLine() {
        }
    }

    public class MarginF {
        public float bottom;
        public float indent;
        public MyMarginSpan indentSp;
        public float left;
        public float right;
        public float top;

        public MarginF(float left, float top, float right, float bottom, float indent) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.indent = indent;
        }
    }

    public MRTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCurrentViewObjs();
    }

    public MRTextView(Context context, ScrollView scrollView) {
        super(context);
        this.scrollView = scrollView;
        initCurrentViewObjs();
    }

    public MRTextView(Context context) {
        super(context);
        initCurrentViewObjs();
    }

    public MRTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCurrentViewObjs();
    }

    public void buildDrawingCache(boolean autoScale) {
        destroyDrawingCache();
    }

    public ScrollView getScrollView() {
        if (this.scrollView == null) {
            this.scrollView = (ScrollView) getParent();
        }
        return this.scrollView;
    }

    public int getTxtHeight() {
        ScrollView sv = getScrollView();
        return (sv.getHeight() - sv.getPaddingTop()) - sv.getPaddingBottom();
    }

    private void initCurrentViewObjs() {
        if (A.verticalAlignment) {
            vertOld = VERSION.SDK_INT < 11;
            if (!vertOld && A.fontName.length() > 0) {
                if (Character.getType(A.fontName.charAt(A.fontName.length() - 1)) > 2 || Character.getType(A.fontName.charAt(0)) > 2) {
                    vertOld = true;
                }
            }
        }
    }

    public void onDraw(Canvas canvas) {
        if (A.lastFile == null) {
            super.onDraw(canvas);
        } else if (ActivityTxt.selfPref != null && !ActivityTxt.selfPref.isFinishing()) {
            disableGPU(canvas);
            if (!this.disableDraw) {
                boolean z;
                this.lastBorderTopLine = -1;
                this.lastIgnoreLine = -1;
                initCurrentViewObjs();
                isTxt = !A.isHtmlContent();
                if (A.getBookType() == 7) {
                    z = true;
                } else {
                    z = false;
                }
                isPdf = z;
                setCnDotAndCharWidth(getPaint());
                if (A.textJustified || A.textHyphenation || A.verticalAlignment) {
                    z = true;
                } else {
                    z = false;
                }
                this.selfJustified = z;
                if (getText() instanceof Spanned) {
                    this.lastBorderTopLine = getLastBorderTopLine(-1);
                    this.drawMarginBackgroundOnly = true;
                    super.onDraw(canvas);
                }
                drawAllHighlight(canvas);
                this.drawMarginBackgroundOnly = false;
                super.onDraw(canvas);
                drawMRSpanLines(canvas);
            }
        }
    }

    private void disableGPU(Canvas canvas) {
        if (!this.hardwareAcceleratedChecked) {
            this.hardwareAcceleratedChecked = true;
            if (VERSION.SDK_INT >= 11 && !A.fitHardwareAccelerated) {
                A.hardwareAccelerated = A.disableGPU(this, canvas);
                A.disableGPUView(this);
            }
        }
    }

    private void setCnDotAndCharWidth(TextPaint paint) {
        if (!A.verticalAlignment && A.adjustLineBreak()) {
            cnDotWidth = Layout.getDesiredWidth("”", paint);
            cnCharWidth = Layout.getDesiredWidth("一", paint);
            dontAdjustDot = cnDotWidth < (cnCharWidth * 4.0f) / 5.0f;
        }
    }

    public void mrDrawText(Canvas canvas, CharSequence text, int start, int end, float x, float y, TextPaint paint) {
        if (text == null) {
            try {
                if (A.txtView != null) {
                    text = A.txtView.getText();
                }
            } catch (Exception e) {
                A.error(e);
                if (text == null || start >= text.length() || end >= text.length()) {
                    A.log("############ ERROR DRAW TEXT:" + start + "," + end + " ############");
                    return;
                } else {
                    vDrawText(canvas, text, start, end, x, y, paint);
                    return;
                }
            }
        }
        String lt_original = text.subSequence(start, end).toString();
        String lt = lt_original.replace("\t", " ");
        int width = getWidth2();
        MyLayout lo = getLayout();
        int line = getLayout().getLineForOffset(start);
        MarginF margins = getCssMargins(line);
        int start2 = lo.getLineStart(line);
        int end2 = lo.getLineVisibleEnd(line);
        boolean wholeLine = start == start2 && (end == end2 || ((end + 1 == end2 && text.charAt(end) == ' ') || text.charAt(end) == '\n'));
        if (!isLastHalfLine(line) && !isSpecialLine(canvas, x, y, paint, line, lt, margins)) {
            float off;
            boolean isRtf = getLayout().getParagraphDirection(line) == -1;
            if (isRtf) {
                x -= margins.left;
                if (drawRTL(canvas, text, wholeLine, line, start, end, x, y, paint, margins)) {
                    return;
                }
            }
            Spanned spanned = getSpanned();
            Object[] spans = getLineSpans(line);
            BackgroundColorSpan[] bgSpans = null;
            if (spans != null && spans.length > 0) {
                BackgroundColorSpan[] bgSpans2;
                if (T.spansHasKind(spans, BackgroundColorSpan.class)) {
                    bgSpans2 = (BackgroundColorSpan[]) spanned.getSpans(start, end, BackgroundColorSpan.class);
                } else {
                    bgSpans2 = null;
                }
                drawRuby(spanned, spans, canvas, text, start, end, x, y, line, lt, margins);
                if (!isFloatText(spans, start, end)) {
                    bgSpans = bgSpans2;
                } else {
                    return;
                }
            }
            int align = getAlign(getAlignmentSpan(spans, spanned, start, end));
            drawTableZoomIcon(canvas, spans, start, line);
            boolean isCenterRight = align > 2;
            drawBackgroundColorSpans(canvas, start, end, x, y, spanned, line, bgSpans);
            TextPaint fpaint = getFromOriginalPaint(paint);
            resetFontColorIfSameAsBackground(fpaint, spanned, spans, bgSpans);
            if (isCenterRight) {
                if (wholeLine) {
                    float l = Layout.getDesiredWidth(lt, fpaint);
                    float w = (((float) width) - margins.left) - margins.right;
                    if (align == 3) {
                        float f;
                        float x2 = margins.left + ((w - l) / 2.0f);
                        if (x2 > 0.0f) {
                            f = x2;
                        } else {
                            f = 0.0f;
                        }
                        vDrawText(canvas, text, start, end, f, y, fpaint);
                        return;
                    }
                    off = margins.right > 0.0f ? 0.0f : isItalicPaint(fpaint) ? A.df(2.0f) : 1.0f;
                    vDrawText(canvas, text, start, end, ((((float) width) - l) - off) - margins.right, y, fpaint);
                    return;
                } else if (align == 4) {
                    x -= margins.right;
                }
            }
            boolean paragraphEnd = isParagraphEnd(text, line, end2);
            boldTxtChapter(lt, fpaint, line, start, paragraphEnd);
            if (isRtf || (align <= 1 && !this.selfJustified)) {
                if (isRtf) {
                    x += (float) getLayout().getLineFloat(line);
                }
                if (x == 0.0f && isItalicPaint(fpaint)) {
                    x += getItalicIgnoreWidth();
                }
                vDrawText(canvas, text, start, end, x, y, fpaint);
                return;
            }
            float marginLeft = margins.left;
            if (isParagraphBegin(text, start)) {
                marginLeft += margins.indent;
            }
            if (wholeLine && !paragraphEnd) {
                ArrayList<String> words = getLineWords(lt, line, true, null, 0.0f);
                if (words.size() > 0) {
                    off = isItalicPaint(fpaint) ? getItalicIgnoreWidth() / 2.0f : 0.0f;
                    width = (int) (((float) width) - (3.0f * off));
                    float[] sizes = getWordSizes(words, (((float) width) - marginLeft) - margins.right, fpaint, true);
                    if (sizes != null) {
                        int k = 0;
                        while (k < words.size()) {
                            vDrawText2(canvas, (String) words.get(k), (marginLeft + sizes[k]) + (k == 0 ? off : 0.0f), y, fpaint);
                            k++;
                        }
                    } else {
                        vDrawText2(canvas, lt, marginLeft, y, fpaint);
                    }
                    if (A.textHyphenation) {
                        drawHyphenSep(lo, line, null, ((float) width) - margins.right, y, getPaint(), canvas);
                        return;
                    }
                    return;
                }
            }
            if (A.textHyphenation && end == end2) {
                drawHyphenSep(lo, line, lt, ((float) getWidth2()) - margins.right, y, getPaint(), canvas);
            }
//            if (!isTxt) {
//                if (!T.spansHasKind(spans, MyImageSpan.class) && (!paragraphEnd || isCenterRight)) {
//                    getSpanLine(line).mrSpans.add(new MRSpan(lt_original, x, y, new TextPaint(fpaint), start, end));
//                    return;
//                }
//            }
            if (A.verticalAlignment || !A.adjustLineBreak()) {
                vDrawText(canvas, text, start, end, x, y, fpaint);
            } else {
                doAdjustLineBreak(canvas, text, start, end, y, fpaint, lt, (((float) width) - marginLeft) - margins.right, x);
            }
        }
    }

    private void drawTableZoomIcon(Canvas canvas, Object[] spans, int start, int line) {
        MyTableSpan sp = null;
        MyMarginSpan msp = null;
        if (spans != null) {
            for (Object obj : spans) {
                if (obj instanceof MyTableSpan) {
                    sp = (MyTableSpan) obj;
                    if (SystemClock.elapsedRealtime() - sp.renderTime > 100) {
                        sp.renderTime = -1;
                    }
                } else if ((obj instanceof MyMarginSpan) && ((MyMarginSpan) obj).isTable) {
                    msp = (MyMarginSpan) obj;
                }
            }
        }
        if (sp != null && msp != null) {
            int y = getScrollView().getScrollY();
            if (sp.spStart == start || (msp.t > ((float) y) && msp.t < ((float) (getTxtHeight() + y)) && SystemClock.elapsedRealtime() - sp.renderTime > 100)) {
                sp.renderTime = SystemClock.elapsedRealtime();
                sp.l = msp.l;
                sp.t = msp.t;
                sp.r = msp.r;
                sp.b = msp.b;
                if (this.tableIcon == null) {
                    this.tableIcon = getContext().getResources().getDrawable(R.drawable.zoomhtml);
                }
                int w = A.d(15.0f);
                int l = (int) (msp.r - ((float) w));
                int t = (int) msp.t;
                this.tableIcon.setBounds(l, t, l + w, t + w);
                this.tableIcon.draw(canvas);
            }
        }
    }

    private boolean isFloatText(Object[] spans, int start, int end) {
        if (spans != null) {
            for (Object sp : spans) {
                if ((sp instanceof MyFloatSpan) && ((MyFloatSpan) sp).spStart == start && ((MyFloatSpan) sp).spEnd == end) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isParagraphBegin(CharSequence text, int start) {
        return start == 0 || text.charAt(start - 1) == '\n';
    }

    private boolean drawRTL(Canvas canvas, CharSequence text, boolean wholeLine, int line, int start, int end, float x, float y, TextPaint paint, MarginF margins) {
        if (wholeLine) {
            return false;
        }
        x = (((((float) getWidth()) - x) - Layout.getDesiredWidth(text, start, end, getPaint())) - A.df(1.5f)) - margins.left;
        if (x > ((float) getWidth2())) {
            x = (float) getWidth2();
        }
        canvas.drawText(text, start, end, x + ((float) getLayout().getLineFloat(line)), y, paint);
        return true;
    }

    private void resetFontColorIfSameAsBackground(TextPaint fpaint, Spanned spanned, Object[] objs, BackgroundColorSpan[] bgSpans) {
        if (spanned != null && !A.useBackgroundImage && fpaint.getColor() == A.backgroundColor) {
            if (bgSpans == null || bgSpans.length <= 0) {
                MyMarginSpan[] spans = (MyMarginSpan[]) T.getSpans(objs, MyMarginSpan.class);
                int length = spans.length;
                int i = 0;
                while (i < length) {
                    MyMarginSpan sp = spans[i];
                    if (sp.cssStyle == null || sp.cssStyle.backgroundColor == null) {
                        i++;
                    } else {
                        return;
                    }
                }
                if (fpaint.getColor() != A.fontColor) {
                    fpaint.setColor(A.fontColor);
                }
            }
        }
    }

    private void drawRuby(Spanned spanned, Object[] objs, Canvas canvas, CharSequence text, int start, int end, float x, float y, int line, String lt, MarginF margins) {
        MyHtml.Ruby[] rubys = null;
        MyHtml.Emphasis[] emphasis = null;
        if (T.spansHasKind(objs, MyHtml.Ruby.class)) {
            rubys = (MyHtml.Ruby[]) getSpanned().getSpans(start, end, MyHtml.Ruby.class);
        }
        if (T.spansHasKind(objs, MyHtml.Emphasis.class)) {
            emphasis = (MyHtml.Emphasis[]) getSpanned().getSpans(start, end, MyHtml.Emphasis.class);
        }
        if ((rubys != null && rubys.length > 0) || (emphasis != null && emphasis.length > 0)) {
            int i1;
            int i2;
            LineRecord lr = getLineRecord(line);
            int lb = getLayout().getLineStart(line);
            int y2 = getLayout().getLineTop(line);
            TextPaint textPaint = new TextPaint(getPaint());
            textPaint.setTextSize(textPaint.getTextSize() * 0.5f);
            TextPaint pe = null;
            if (emphasis != null) {
                int i;
                textPaint = new TextPaint(getPaint());
                textPaint.setTextSize(textPaint.getTextSize() * 0.7f);
                int le = getLayout().getLineVisibleEnd(line);
                ArrayList<MyHtml.Ruby> newList = new ArrayList();
                for (i = 0; i < emphasis.length; i++) {
                    i1 = spanned.getSpanStart(emphasis[i]);
                    i2 = spanned.getSpanEnd(emphasis[i]);
                    int j = i1;
                    while (j < i2) {
                        if (j >= lb && j < le) {
                            newList.add(new MyHtml.Ruby(emphasis[i].emphasis, null, j, j + 1));
                        }
                        j++;
                    }
                }
                int size = rubys == null ? 0 : rubys.length;
                MyHtml.Ruby[] rubys3 = new MyHtml.Ruby[(newList.size() + size)];
                if (rubys != null) {
                    for (i = 0; i < rubys.length; i++) {
                        rubys3[i] = rubys[i];
                    }
                }
                for (i = 0; i < newList.size(); i++) {
                    rubys3[i + size] = (MyHtml.Ruby) newList.get(i);
                }
                rubys = rubys3;
            }
            boolean hasMarginTop = margins.top > 0.0f;
            if (!hasMarginTop) {
                hasMarginTop = getLayout().getLineTopAdded2(line) > 0;
            }
            for (MyHtml.Ruby r : rubys) {
                if (r.end > r.start && !T.isNull(r.rt)) {
                    float x1;
                    float x2;
                    TextPaint p;
                    String rt = r.rt;
                    i1 = r.start;
                    i2 = r.end;
                    if (r.original != null) {
                        if (i1 < start) {
                            rt = rt.substring((rt.length() * (start - i1)) / (i2 - i1), rt.length());
                            i1 = start;
                        }
                        if (i2 > end) {
                            rt = rt.substring(0, (rt.length() * ((i2 - i1) - (i2 - end))) / (i2 - i1));
                            i2 = end;
                        }
                    }
                    if (lr != null) {
                        x1 = lr.starts[i1 - lb < 0 ? 0 : i1 - lb];
                        x2 = lr.ends[(i2 - lb) + -1 > lr.ends.length + -1 ? lr.ends.length - 1 : (i2 - lb) - 1];
                    } else {
                        x1 = getTextX(line, i1);
                        x2 = x1 + Layout.getDesiredWidth(text, i1, i2, getPaint());
                    }
                    if (r.original == null) {
                        p = pe;
                    } else {
                        p = textPaint;
                    }
                    float w1 = Layout.getDesiredWidth(rt, p);
                    x1 -= (w1 - (x2 - x1)) / 2.0f;
                    if (x1 + w1 > ((float) getWidth2())) {
                        x1 = ((float) getWidth2()) - w1;
                    }
                    if (x1 < 0.0f) {
                        x1 = 0.0f;
                    }
                    int top = y2;
                    if (r.original == null) {
                        top += (getLineHeight(line) * 9) / 100;
                    } else if (A.verticalAlignment) {
                        top += (getLineHeight(line) * 7) / 100;
                    }
                    if (hasMarginTop) {
                        top = ((int) y) - ((getTextRealHeight(start, end) * 65) / 100);
                    }
                    vDrawText(canvas, rt, 0, rt.length(), x1, (float) top, p);
                }
            }
        }
    }

    private boolean lineHasRuby(int line) {
        MyHtml.Ruby[] spans = (MyHtml.Ruby[]) T.getSpans(getLineSpans(line), MyHtml.Ruby.class);
        return spans != null && spans.length > 0;
    }

    public boolean isItalicPaint(TextPaint fpaint) {
        if (((double) fpaint.getTextSkewX()) < -0.1d) {
            return true;
        }
        if (fpaint.getTypeface() == null || (fpaint.getTypeface().getStyle() & 2) != 2) {
            return false;
        }
        return true;
    }

    private boolean isParagraphEnd(CharSequence text, int line, int end2) {
        return line >= getRealLineCount() + -1 || (end2 >= 0 && end2 < text.length() && text.charAt(end2) == '\n');
    }

    private boolean isSpecialLine(Canvas canvas, float x, float y, TextPaint paint, int line, String lt, MarginF margins) {
        if (lt.length() > 0) {
            if (lt.charAt(lt.length() - 1) == '\n') {
                lt = lt.substring(0, lt.length() - 1);
            }
        }
        if (lt.equals(MyHtml.HR_TAG)) {
            int lh = getLineHeight();
            Paint p2 = new Paint(paint);
            p2.setStrokeWidth(1.0f);
            p2.setColor(A.getAlphaColor(p2.getColor(), -150));
            if (x > 0.0f && (x == indentWidth() || getLineAlign(line) == 3)) {
                x = 0.0f;
            }
            if (x < margins.left) {
                x = margins.left;
            }
            float f = y - ((float) (lh / 4));
            canvas.drawLine(x, f, ((float) getWidth2()) - margins.right, y - ((float) (lh / 4)), p2);
            return true;
        }
        if (A.chapterEndPrompt) {
            if (lt.equals(A.theEndText())) {
                TextPaint fpaint = new TextPaint(paint);
                if (A.fontBold) {
                    fpaint.setShadowLayer(A.df(1.0f), A.df(1.0f), A.df(1.0f), -7829368);
                } else {
                    fpaint.setFakeBoldText(true);
                }
                float l = Layout.getDesiredWidth(lt, fpaint);
                int width = getWidth();
                if (l > ((float) width)) {
                    fpaint = paint;
                    l = Layout.getDesiredWidth(lt, fpaint);
                }
                vDrawText(canvas, lt, 0, lt.length(), (((float) width) - l) / 2.0f, y, fpaint);
                return true;
            }
        }
        return false;
    }

    private void boldTxtChapter(String lt, TextPaint p, int line, int start, boolean paragraphEnd) {
        int i = -1;
        if (isTxt) {
            if (!(this.lastTxtChapterLine == line - 1 && paragraphEnd)) {
                int iTag = A.isAsiaLanguage ? lt.indexOf("第") : lt.toLowerCase().indexOf("chapter");
                if (iTag == -1) {
                    return;
                }
                if (iTag <= 0 || A.isPreChapterTag(lt.charAt(iTag - 1))) {
                    if (!paragraphEnd) {
                        i = getText2().indexOf("\n", start + iTag);
                    }
                    if (i > 0) {
                        lt = getText2().substring(start, i);
                    }
                    if (A.isAsiaLanguage) {
                        if (!(A.chapterLengthOk(lt) && A.chineseChapterTagOk(lt))) {
                            return;
                        }
                    } else if (lt.length() > 100) {
                        return;
                    } else {
                        if (iTag >= (lt.length() / 4) + 4) {
                            return;
                        }
                    }
                    if (paragraphEnd) {
                        line = -100;
                    }
                    this.lastTxtChapterLine = line;
                } else {
                    return;
                }
            }
            if (A.fontBold) {
                p.setShadowLayer(A.df(1.0f), A.df(1.0f), A.df(1.0f), -7829368);
            } else {
                p.setFakeBoldText(true);
            }
        }
    }

    public boolean isLastHalfLine(int line) {
        if (A.moveStart || inPixelAutoScroll()) {
            return false;
        }
        if (lastIgnoreLine() > 0 && line >= lastIgnoreLine()) {
            return true;
        }
        int bottom = getScrollView().getScrollY() + A.getPageHeight();
        if (line < getLayout().getLineForVertical(bottom - 1)) {
            return false;
        }
        boolean result = false;
        if (getLineTop3(line) > bottom - A.oneLineTagHeight(line)) {
            result = true;
        } else if (A.txtView2.getVisibility() ==VISIBLE && A.txtScroll2.getVisibility() == VISIBLE && line == A.txtView2.getLayout().getLineForVertical(A.txtScroll2.getScrollY())) {
            result = true;
        }
        if (!result) {
            return result;
        }
        this.lastIgnoreLine = line;
        return result;
    }

    public int lastIgnoreLine() {
        if (this.lastBorderTopLine > 0) {
            return this.lastBorderTopLine;
        }
        if (this.lastIgnoreLine > 0) {
            return this.lastIgnoreLine;
        }
        return -1;
    }

    private void doAdjustLineBreak(Canvas canvas, CharSequence text, int start, int end, float y, TextPaint paint, String lt, float width, float x2) {
        boolean overflow;
        if (dontAdjustDot) {
            int l = lt.length();
            if (l > 1) {
                if (lt.charAt(l - 1) == '”') {
                    if (A.isChineseDot(lt.charAt(l - 2))) {
                        float offset;
                        float min;
                        float nx = 0;
                        vDrawText(canvas, text, start, end - 2, x2, y, paint);
                        float textWidth = Layout.getDesiredWidth(lt.substring(0, l - 2), paint);
                        overflow = Layout.getDesiredWidth(lt.substring(l + -2), paint) + textWidth > width;
                        int sdk = VERSION.SDK_INT;
                        float per = sdk >= 14 ? 0.4f : (sdk == 9 || sdk == 10) ? 0.05f : 0.3f;
                        if (overflow) {
                            if (lt.charAt(l - 2) != '。') {
                                offset = cnDotWidth * per;
                                canvas.drawText(lt.substring(l - 2, l - 1), (x2 + textWidth) - offset, y, paint);
                                min = (x2 + width) - (cnDotWidth * 0.9f);
                                nx = overflow ? min : (x2 + textWidth) + (cnCharWidth * 0.8f);
                                if (nx > min) {
                                    nx = min;
                                }
                                canvas.drawText(lt.substring(l - 1, l), nx, y, paint);
                                return;
                            }
                        }
                        offset = 0.0f;
                        canvas.drawText(lt.substring(l - 2, l - 1), (x2 + textWidth) - offset, y, paint);
                        min = (x2 + width) - (cnDotWidth * 0.9f);
                        if (overflow) {
                        }
                        if (nx > min) {
                            nx = min;
                        }
                        canvas.drawText(lt.substring(l - 1, l), nx, y, paint);
                        return;
                    }
                }
            }
            vDrawText(canvas, text, start, end, x2, y, paint);
            return;
        }
        ArrayList<String> list = new ArrayList();
        int p = 0;
        int i = 1;
        while (i < lt.length()) {
            if (lt.charAt(i) == '“' || lt.charAt(i) == '”') {
                if (A.isChineseDot(lt.charAt(i - 1))) {
                    if (i > p + 1) {
                        list.add(lt.substring(p, i - 1));
                    }
                    list.add(lt.substring(i - 1, i + 1));
                    p = i + 1;
                }
            }
            i++;
        }
        if (p < lt.length()) {
            list.add(lt.substring(p));
        }
        if (list.size() > 1) {
            float w = width;
            overflow = Layout.getDesiredWidth(lt, paint) > w;
            float f = x2;
            i = 0;
            while (i < list.size()) {
                String s = (String) list.get(i);
                if (isDotLinkQuote(s)) {
                    float add = cnDotWidth / 8.0f;
                    boolean last = i == list.size() + -1;
                    if (last && overflow) {
                        add = s.charAt(0) == '。' ? 0.0f : (-cnDotWidth) / 8.0f;
                    }
                    f += add;
                    canvas.drawText(s.substring(0, 1), f, y, paint);
                    float dotOffWidth = overflow ? 0.5f * cnDotWidth : !last ? getDotOffWidth(s) : 0.8f * cnDotWidth;
                    f += dotOffWidth;
                    if (last && overflow && list.size() == 2 && f < w - (cnDotWidth * 0.6f)) {
                        f = w - (cnDotWidth * 0.6f);
                    }
                    if (f - x2 > w - (cnDotWidth * 0.5f)) {
                        f = (x2 + w) - (cnDotWidth * 0.5f);
                    }
                    canvas.drawText(s.substring(1), f, y, paint);
                    f += cnDotWidth + (cnDotWidth / 8.0f);
                } else {
                    vDrawText2(canvas, s, f, y, paint);
                    f += Layout.getDesiredWidth(s, paint);
                }
                i++;
            }
            return;
        }
        vDrawText(canvas, text, start, end, x2, y, paint);
    }

    private boolean inPixelAutoScroll() {
        if (A.isInAutoScroll && (A.autoScrollMode == 2 || A.autoScrollMode == 3)) {
            return true;
        }
        return false;
    }

    private void vDrawText(Canvas canvas, CharSequence text, int start, int end, float x, float y, TextPaint paint) {
        if (start != end) {
            if (A.verticalAlignment) {
                vDrawText2(canvas, text.subSequence(start, end).toString(), x, y, paint);
            } else if (VERSION.SDK_INT == 14 || VERSION.SDK_INT == 15) {
                canvas.drawText(text.subSequence(start, end).toString(), x, y, paint);
            } else {
                canvas.drawText(text, start, end, x, y, paint);
            }
        }
    }

    private void vDrawText2(Canvas canvas, String text, float x, float y, TextPaint paint) {
        if (!T.isNull(text)) {
            if (A.verticalAlignment) {
                TextPaint p;
                if (paint.isUnderlineText()) {
                    p = new TextPaint(paint);
                } else {
                    p = paint;
                }
                if (p.isUnderlineText()) {
                    p.setUnderlineText(false);
                }
                float dw = Layout.getDesiredWidth("一", paint);
                float dx = x + ((9.0f * dw) / 10.0f);
                float y2 = y + (dw / 3.0f);
                int i = 0;
                while (i < text.length()) {
                    char c = text.charAt(i);
                    int type = Character.getType(c);
                    if (c == ' ' || ((type == 1 && (c < 'Ａ' || c > 'Ｚ')) || ((type == 2 && (c < 'ａ' || c > 'ｚ')) || (type == 9 && (c < '０' || c > '９'))))) {
                        canvas.drawText(c + "", dx - ((9.0f * dw) / 10.0f), (dw / CSS.FLOAT_ADDED) + y, paint);
                        dx += Layout.getDesiredWidth(c + "", paint);
                    } else {
                        if ((c == '.' || c == '_') && i > 0) {
                            if (!isSplitChar(text.charAt(i - 1))) {
                                canvas.drawText(c + "", dx - ((9.0f * dw) / 10.0f), ((2.0f * dw) / 10.0f) + y, p);
                                dx += dw;
                            }
                        }
                        if (true && (c == 'ー' || c == '─' || c == '～' || c == '―' || c == '「' || c == '」' || c == '『' || c == '』' || c == '［' || c == '］' || c == '【' || c == '】' || c == '〔' || c == '〕' || c == '〝' || c == '〟' || c == '＝' || c == '/' || c == '=')) {
                            canvas.drawText(c + "", dx - ((9.0f * dw) / 10.0f), ((2.0f * dw) / 10.0f) + y, p);
                            dx += dw;
                        } else {
                            Path path = new Path();
                            path.moveTo(dx, y2);
                            path.lineTo(dx, y2 - 100.0f);
                            if (c != A.INDENT_CHAR) {
                                drawVerticalChar(canvas, c, path, p, dw);
                            }
                            dx += dw;
                        }
                    }
                    i++;
                }
            } else if (isDotLinkQuote(text)) {
                canvas.drawText(text.substring(0, 1), x, y, paint);
                Canvas canvas2 = canvas;
                canvas2.drawText(text.substring(1), x + getDotOffWidth(text), y, paint);
            } else {
                canvas.drawText(text, x, y, paint);
            }
        }
    }

    private float getDotOffWidth(String text) {
        return text.charAt(1) == '”' ? (cnDotWidth * 7.0f) / 10.0f : (cnDotWidth * 5.0f) / 10.0f;
    }

    private void drawBackgroundColorSpans(Canvas canvas, int start, int end, float x, float y, Spanned spanStr, int line, BackgroundColorSpan[] bgSpans) {
        if (bgSpans != null) {
            for (BackgroundColorSpan sp : bgSpans) {
                drawBackgroundColorSpan(canvas, start, end, x, y, spanStr, line, sp);
            }
        }
    }

    private void drawBackgroundColorSpan(Canvas canvas, int start, int end, float x, float y, Spanned spanStr, int line, BackgroundColorSpan bSpan) {
        int pStart = spanStr.getSpanStart(bSpan);
        int pEnd = spanStr.getSpanEnd(bSpan);
        if (pStart < start) {
            pStart = start;
        }
        if (pEnd > end) {
            pEnd = end;
        }
        if (pStart != pEnd) {
            float l1 = getTextX(line, pStart);
            float l2 = getTextX(line, pEnd);
            float baseline = y;
            int lineHeight = getTextRealHeight(start, end);
            int lineOff = (lineHeight / 4) + 1;
            float y1 = (baseline - ((float) lineHeight)) + ((float) lineOff);
            float y2 = baseline + ((float) lineOff);
            Paint p = new Paint();
            int alpha = (A.isWhiteFont(bSpan.color) && A.isWhiteFont(A.fontColor)) ? -120 : this.highlightLines.contains(Integer.valueOf(line)) ? -100 : 0;
            p.setColor(A.getAlphaColor(bSpan.color, alpha));
            canvas.drawRect(new RectF(l1, y1, l2, y2), p);
        }
    }

    private void drawMRSpanLines(Canvas canvas) {
        if (mrSpanLines != null) {
            Iterator it = mrSpanLines.iterator();
            while (it.hasNext()) {
                MRSpanLine sl = (MRSpanLine) it.next();
                if (sl.mrSpans.size() != 0) {
                    MRSpan sp;
                    int i;
                    MRSpanLine sl2 = new MRSpanLine();
                    Iterator it2 = sl.mrSpans.iterator();
                    while (it2.hasNext()) {
                        sp = (MRSpan) it2.next();
                        ArrayList<String> words = getLineWords(sp.text, -1, false, null, 0.0f);
                        if (words.size() > 0) {
                            int off = 0;
                            for (i = 0; i < words.size(); i++) {
                                MRSpan wsp = new MRSpan((String) words.get(i), sp.x, sp.y, sp.paint, sp.start + off, sp.end + off);
                                off += ((String) words.get(i)).length();
                                while (wsp.text.length() > 0 && A.isSpaceChar(wsp.text.charAt(0))) {
                                    wsp.text = wsp.text.substring(1);
                                    wsp.start++;
                                }
                                wsp.x = getTextX2(sl.line, wsp.start);
                                if (wsp.x == -1.0f) {
                                    wsp.x = getTextX(sl.line, wsp.start);
                                }
                                sl2.mrSpans.add(wsp);
                            }
                        } else {
                            sl2.mrSpans.add(sp);
                        }
                    }
                    int l = sl2.mrSpans.size();
                    float off1 = (l <= 1 || ((MRSpan) sl2.mrSpans.get(0)).x > 0.0f || !isItalicPaint(((MRSpan) sl2.mrSpans.get(0)).paint)) ? 0.0f : getItalicIgnoreWidth();
                    float off2 = (l <= 1 || !isItalicPaint(((MRSpan) sl2.mrSpans.get(l - 1)).paint)) ? 0.0f : getItalicIgnoreWidth();
                    for (i = 0; i < l; i++) {
                        sp = (MRSpan) sl2.mrSpans.get(i);
                        if (off1 > 0.0f) {
                            sp.x += (((float) ((l - 1) - i)) * off1) / ((float) (l - 1));
                        }
                        if (off2 > 0.0f) {
                            sp.x -= (((float) i) * off2) / ((float) (l - 1));
                        }
                        vDrawText2(canvas, sp.text, sp.x, sp.y, sp.paint);
                    }
                }
            }
        }
        mrSpanLines = null;
    }

    public boolean isImageDrawed(int line) {
//        if (getSpanned() == null) {
//            return false;
//        }
//        int y = getScrollView().getScrollY();
//        int y1 = getLayout().getLineTop(line);
//        if (y > getLayout().getLineTop(line + 1) || y < (y1 - A.getPageHeight()) + getLineHeight2()) {
//            return false;
//        }
//        MyImageSpan[] spans = (MyImageSpan[]) T.getSpans(getLineSpans(line), MyImageSpan.class);
//        for (MyImageSpan sp : spans) {
//            if (!sp.drawed) {
//                return false;
//            }
//        }
//        if (spans.length > 0) {
//            return true;
//        }
        return false;
    }

    public int getIgnoreBorderTop(int fromLine) {
        MyMarginSpan[] spans = (MyMarginSpan[]) T.getSpans(getLineSpans(fromLine), MyMarginSpan.class);
        if (spans == null) {
            return -1;
        }
        MyMarginSpan m = null;
        for (int i = spans.length - 1; i >= 0; i--) {
            MyMarginSpan sp = spans[i];
            if (sp.cssStyle != null && CSS.hasBorderOrBackgroundColor(sp.cssStyle)) {
                m = sp;
                break;
            }
        }
        if (m == null || getLayout().getLineForOffset(m.spEnd - 1) == fromLine) {
            return -1;
        }
        return getLayout().getLineForOffset(m.spStart);
    }

    public int getLastBorderTopLine(int last) {
        if (A.trimBlankSpace) {
            return -1;
        }
        if (A.moveStart || inPixelAutoScroll()) {
            return -1;
        }
        int page_break = getPageBreakLine();
        if (page_break != -1) {
            return page_break;
        }
        int y = getScrollView().getScrollY();
        int ph = A.getPageHeight();
        if (last == -1) {
            last = getLayout().getLineForVertical((y + ph) - 1);
            if (last >= getRealLineCount()) {
                return -1;
            }
            if (last > 0 && getLineTop3(last) > (y + ph) - A.oneLineTagHeight(last)) {
                last--;
            }
        }
        int bLine = -1;
        if (getLineTop(last + 1) < y + ph) {
            int nextbLine = getIgnoreBorderTop(last + 1);
            if (nextbLine == last || nextbLine == last + 1) {
                bLine = nextbLine;
            }
        }
        if (bLine == -1) {
            bLine = getIgnoreBorderTop(last);
            if (shouldIgnoreBorder(last, y, ph, bLine)) {
                return bLine;
            }
            bLine = -1;
        }
        if (getLayout().getLineFloat(last) == 0 || getLayout().getLineFloat(last + 1) == 0) {
            return bLine;
        }
        int floatbLine = getLayout().getLineForOffset(getLayout().getLineFloatSp(last).spStart);
        if (shouldIgnoreBorder(last, y, ph, floatbLine)) {
            return floatbLine;
        }
        return bLine;
    }

    private boolean shouldIgnoreBorder(int last, int y, int ph, int bLine) {
        if (!(bLine == last && last == getRealLineCount() - 1) && bLine > 0 && bLine <= last) {
            int bY = getLineTop2(bLine);
            int first = getLayout().getLineForVertical(y);
            int allow = A.dualPageEnabled() ? ph / 3 : ph / 2;
            if (bLine > first && bY > y + allow) {
                return true;
            }
        }
        return false;
    }

    public int getPageBreakLine() {
        if (A.trimBlankSpace) {
            return -1;
        }
        if (getSpanned() == null || getLayout() == null) {
            return -1;
        }
        int y = getScrollView().getScrollY();
        int first = getLayout().getLineForVertical(y);
        for (PAGE_BREAK sp : (PAGE_BREAK[]) getSpanned().getSpans(getLayout().getLineStart(first), getLayout().getLineVisibleEnd(getLayout().getLineForVertical((A.getPageHeight() + y) - 1)), PAGE_BREAK.class)) {
            int start = getSpanned().getSpanStart(sp);
            if (start < getText().length()) {
                int line = getLayout().getLineForOffset(start);
                if ((getLineTop2(line) - y >= getLineHeight() || getLineTop2(line) - y >= getLineHeight(line)) && line > first) {
                    for (int i = first; i < line; i++) {
                        if (getLineText(i).length() > 0) {
                            return line;
                        }
                    }
                    continue;
                }
            }
        }
        return -1;
    }

    public MyMarginSpan lineTopBorderSp(int line) {
        MyMarginSpan[] spans = (MyMarginSpan[]) T.getSpans(getLineSpans(line), MyMarginSpan.class);
        if (spans != null) {
            for (MyMarginSpan sp : spans) {
                if (sp.cssStyle != null && CSS.hasBorder(sp.cssStyle)) {
                    return sp;
                }
            }
        }
        return null;
    }

    public int getCurPosition() {
        return getLayout().getLineStart(getLayout().getLineForVertical(getScrollView().getScrollY()));
    }

    static MRSpanLine getSpanLine(int line) {
        MRSpanLine sl;
        if (mrSpanLines == null) {
            mrSpanLines = new ArrayList();
        }
        Iterator it = mrSpanLines.iterator();
        while (it.hasNext()) {
            sl = (MRSpanLine) it.next();
            if (sl.line == line) {
                return sl;
            }
        }
        sl = new MRSpanLine();
        sl.line = line;
        mrSpanLines.add(sl);
        return sl;
    }

    public static AlignmentSpan getAlignmentSpan(Object[] spans, Spanned spanned, int start, int end) {
        AlignmentSpan a = null;
        Integer off = null;
        if (spans != null) {
            for (Object o : spans) {
                if (o instanceof AlignmentSpan) {
                    AlignmentSpan a2 = (AlignmentSpan) o;
                    if (a == null) {
                        a = a2;
                    } else {
                        if (off == null) {
                            off = Integer.valueOf((start - spanned.getSpanStart(a)) + (spanned.getSpanEnd(a) - end));
                        }
                        int start2 = spanned.getSpanStart(a2);
                        int end2 = spanned.getSpanEnd(a2);
                        int off2 = (start - start2) + (end2 - end);
                        if (start2 != end2 && off2 < off.intValue()) {
                            a = a2;
                            off = Integer.valueOf(off2);
                        }
                    }
                }
            }
        }
        return a;
    }

    public static int getAlign(AlignmentSpan a) {
        int result = 0;
        if (a != null) {
             Alignment value = a.getAlignment();
            if (value == Alignment.ALIGN_LEFT) {
                result = 1;
            } else if (value == Alignment.ALIGN_JUSTIFY) {
                result = 2;
            } else if (value == Alignment.ALIGN_CENTER) {
                result = 3;
            } else if (value == Alignment.ALIGN_RIGHT) {
                result = 4;
            }
        }
        if (result != 0 || global_alignment <= 1) {
            return result;
        }
        return global_alignment;
    }

    public static Alignment getAlignment(int a) {
        if (a == 2) {
            return Alignment.ALIGN_JUSTIFY;
        }
        if (a == 3) {
            return Alignment.ALIGN_CENTER;
        }
        if (a == 4) {
            return Alignment.ALIGN_RIGHT;
        }
        return Alignment.ALIGN_LEFT;
    }

    public int getLineAlign(int line) {
        if (getSpanned() == null) {
            return 0;
        }
        if (this.lineAligns.containsKey(Integer.valueOf(line))) {
            return ((Integer) this.lineAligns.get(Integer.valueOf(line))).intValue();
        }
        int align = getAlign(getAlignmentSpan(getLineSpans(line), getSpanned(), getLayout().getLineStart(line), getLayout().getLineVisibleEnd(line)));
        this.lineAligns.put(Integer.valueOf(line), Integer.valueOf(align));
        return align;
    }

    public TextPaint getFromOriginalPaint(TextPaint p) {
        TextPaint p2 = new TextPaint(p);
        if (A.fontItalic) {
            p2.setTextSkewX(-0.25f);
        }
        if (A.fontBold) {
            p2.setFakeBoldText(true);
        }
        if (A.fontUnderline) {
            p2.setUnderlineText(true);
        }
        return p2;
    }

    private String rtrim(String s) {
        int i = s.length() - 1;
        while (i >= 0 && A.isSpaceChar(s.charAt(i))) {
            i--;
        }
        return i == s.length() + -1 ? s : s.substring(0, i + 1);
    }

    public static boolean isSplitChar(char c) {
        return Character.getType(c) == 5;
    }

    public ArrayList<String> getLineWords(String lt, int line, boolean trimRight, ArrayList<Float> sizes, float lt_width) {
        if (trimRight) {
            try {
                lt = rtrim(lt);
            } catch (Exception e) {
                A.error(e);
                return new ArrayList();
            }
        }
        ArrayList<String> list = new ArrayList();
        if (lt.length() == 0 || lt.endsWith("\n")) {
            return list;
        }
        int i;
        TextPaint p;
        int j = 0;
        float total = 0.0f;
        if (A.verticalAlignment) {
            while (j < lt.length() && A.isSpaceChar(lt.charAt(j))) {
                j++;
            }
            if (j == lt.length()) {
                j = lt.length() - 1;
            }
            list.add(lt.substring(0, j + 1));
            for (i = j + 1; i < lt.length(); i++) {
                list.add("" + lt.charAt(i));
            }
            if (sizes == null) {
                return list;
            }
            p = getPaint();
            float l = Layout.getDesiredWidth(lt.substring(0, j + 1), p);
            total = 0.0f + l;
            sizes.add(Float.valueOf(l));
            if (list.size() > 1) {
                float[] widths = new float[(lt.length() - (j + 1))];
                p.getTextWidths(lt, j + 1, lt.length(), widths);
                for (i = 0; i < widths.length; i++) {
                    total += widths[i];
                    sizes.add(Float.valueOf(widths[i]));
                }
            }
        }
        String word;
        if (sizes != null) {
            if (!A.verticalAlignment && line != -1) {
                p = getPaint();
                CharSequence s = getText();
                int j1 = getLayout().getLineStart(line);
                int j2 = getLayout().getLineVisibleEnd(line);
                i = j1;
                float l;
                while (true) {
                    j = i;
                    if (i == j1) {
                        while (j < j2 && A.isSpaceChar(s.charAt(j))) {
                            j++;
                        }
                    }
                    if (j == j2) {
                        break;
                    }
                    if (!isSplitChar(s.charAt(j))) {
                        while (true) {
                            j++;
                            if (j >= j2 || A.isSpaceChar(s.charAt(j)) || isSplitChar(s.charAt(j))) {
                                break;
                            }
                        }
                    } else {
                        j++;
                    }
                    while (j < j2 && A.isSpaceChar(s.charAt(j))) {
                        j++;
                    }
                    word = s.subSequence(i, j).toString();
                    list.add(word);
                    if (isDotLinkQuote(word)) {
                       l = cnDotWidth + getDotOffWidth(word);
                    } else {
                        l = Layout.getDesiredWidth(s, i, j, p);
                    }
                    total += l;
                    sizes.add(Float.valueOf(l));
                    i = j;
                }
            }
            if (lt_width <= 0.0f) {
                return list;
            }
            float space = (lt_width - total) / ((float) (sizes.size() - 1));
            float offset = ((Float) sizes.get(0)).floatValue();
            sizes.set(0, Float.valueOf(0.0f));
            for (i = 1; i < sizes.size(); i++) {
                float tmp = ((Float) sizes.get(i)).floatValue();
                sizes.set(i, Float.valueOf((((float) i) * space) + offset));
                offset += tmp;
            }
            return list;
        }
        i = 0;
        int l2 = lt.length();
        while (true) {
            j = i;
            if (i == 0) {
                while (j < l2 && A.isSpaceChar(lt.charAt(j))) {
                    j++;
                }
            }
            if (j == l2) {
                break;
            }
            if (!isSplitChar(lt.charAt(j))) {
                while (true) {
                    j++;
                    if (j >= l2 || A.isSpaceChar(lt.charAt(j)) || isSplitChar(lt.charAt(j))) {
                        break;
                    }
                }
            } else {
                j++;
            }
            word = lt.substring(i, j);
            i = j;
            while (j < l2 && A.isSpaceChar(lt.charAt(j))) {
                j++;
            }
            if (i != j) {
                word = word + " ";
            }
            list.add(word);
            i = j;
        }
        if (!A.textHyphenation || line == -1 || list.size() <= 0) {
            return list;
        }
        MyLayout layout = getLayout();
        String last = (String) list.get(list.size() - 1);
        char lastChar = last.charAt(last.length() - 1);
        if (layout.getLineSep(line) > 0) {
            if (HYPH_RIGHT_PUNCS1.indexOf(lastChar) != -1) {
                layout.setLineSep(line, last.charAt(last.length() - 1));
                list.set(list.size() - 1, last.substring(0, last.length() - 1));
                return list;
            } else if (getText().charAt(layout.getLineStart(line) + lt.length()) != ' ') {
                return list;
            } else {
                layout.removeLineSep(line);
                A.log("######error hyph draw, ignore:" + lt);
                return list;
            }
        } else if (HYPH_RIGHT_PUNCS2.indexOf(lastChar) == -1) {
            return list;
        } else {
            layout.setLineSep(line, last.charAt(last.length() - 1));
            list.set(list.size() - 1, last.substring(0, last.length() - 1));
            return list;
        }
    }

    public float[] getWordSizes(ArrayList<String> words, float width, TextPaint fpaint, boolean checkPaint) {
        float[] sizes = new float[words.size()];
        float l = getWordsLength(words, sizes, fpaint, -1.0f);
        boolean ok = true;
        if (checkPaint) {
            float off = width * 0.01f;
            if (width < l - off) {
                A.log("+getWordsLength() ERROR:" + width + "/" + l + " off:" + off + " word:" + words.toString());
                l = getWordsLength(words, sizes, fpaint, 0.98f);
            }
            if (width < l - off) {
                l = getWordsLength(words, sizes, fpaint, 0.95f);
            }
            if (width < l - off) {
                l = getWordsLength(words, sizes, fpaint, 0.9f);
            }
            if (width < l - off) {
                l = getWordsLength(words, sizes, fpaint, 0.85f);
            }
            ok = width >= l - off;
        }
        if (!ok) {
            return null;
        }
        if (words.size() > 1) {
            float space = ((width - l) - ((float) (A.textCJK ? 0.0f : A.d(1.0f)))) / ((float) (words.size() - 1));
            float offset = sizes[0];
            sizes[0] = 0.0f;
            for (int i = 1; i < words.size(); i++) {
                float tmp = sizes[i];
                sizes[i] = (((float) i) * space) + offset;
                offset += tmp;
            }
            return sizes;
        } else if (words.size() <= 0) {
            return sizes;
        } else {
            sizes[0] = 0.0f;
            return sizes;
        }
    }

    private boolean isDotLinkQuote(String s) {
        boolean adjustQuote;
        if (A.verticalAlignment || dontAdjustDot || !A.adjustLineBreak()) {
            adjustQuote = false;
        } else {
            adjustQuote = true;
        }
        return adjustQuote && s.length() == 2 && A.isChineseDot(s.charAt(0)) && (s.charAt(1) == '“' || s.charAt(1) == '”');
    }

    public float getWordsLength(ArrayList<String> words, float[] sizes, TextPaint fpaint, float scale) {
        int i;
        TextPaint paint = fpaint;
        if (scale != -1.0f) {
            paint = new TextPaint(fpaint);
            paint.setTextScaleX(scale);
        }
        StringBuilder sb = new StringBuilder();
        for (i = 0; i < words.size(); i++) {
            if (!isDotLinkQuote((String) words.get(i))) {
                sb.append((String) words.get(i));
            }
        }
        float[] widths = new float[sb.length()];
        paint.getTextWidths(sb.toString(), widths);
        int m = 0;
        for (i = 0; i < words.size(); i++) {
            sizes[i] = 0.0f;
            if (isDotLinkQuote((String) words.get(i))) {
                sizes[i] = getDotOffWidth((String) words.get(i)) + cnDotWidth;
            } else {
                int j = 0;
                while (j < ((String) words.get(i)).length()) {
                    int m2 = m + 1;
                    sizes[i] = sizes[i] + widths[m];
                    j++;
                    m = m2;
                }
            }
        }
        float l = 0.0f;
        for (float f : sizes) {
            l += f;
        }
        return l;
    }

    public boolean drawHyphenSep(MyLayout lo, int line, String lt, float x, float y, TextPaint paint, Canvas canvas) {
        if (lo.getLineSep(line) > 0) {
            char sep = (char) lo.getLineSep(line);
            if (sep > '\u0000' && (lt == null || sep != lt.charAt(lt.length() - 1))) {
                vDrawText2(canvas, "" + ((char) sep), x, y, paint);
                return true;
            }
        }
        return false;
    }

    public float getItalicIgnoreWidth() {
        if (this.italicIgnoreWidth == 0.0f) {
            this.italicIgnoreWidth = (Layout.getDesiredWidth("A", getPaint()) * 2.0f) / 10.0f;
        }
        return this.italicIgnoreWidth;
    }

    public int getWidth2() {
        if (!A.textHyphenation) {
            return getWidth();
        }
        int hw = SoftHyphenStaticLayout.mInnerWidth;
        if (hw == 0) {
            return (int) (((float) getWidth()) - Layout.getDesiredWidth("?", getPaint()));
        }
        return hw;
    }

    private void drawAllHighlight(Canvas canvas) {
        this.visualBookmarks.clear();
        this.highlightLines.clear();
        if (hasHighlight()) {
            try {
                ScrollView sv = getScrollView();
                int from = getLayout().getLineForVertical(sv.getScrollY());
                int to = getLayout().getLineForVertical(sv.getScrollY() + getTxtHeight());
                createHighlightAllItems(from - 1, to + 1);
                for (int line = from; line <= to; line++) {
                    int baseline = getLineBounds(line, null);
                    int j1 = getLayout().getLineStart(line);
                    int j2 = getLayout().getLineVisibleEnd(line);
                    if (!isPdf) {
                        Iterator it;
                        NoteInfo note;
                        int start;
                        Canvas canvas2;
                        long A1 = 0;
                        if (isTxt) {
                            A1 = A.getTxtRealPos((long) j1);
                            if (A1 == -1) {
                                continue;
                            }
                        }
                        if (A.getHighlights().size() > 0) {
                            it = A.getHighlights().iterator();
                            while (it.hasNext()) {
                                note = (NoteInfo) it.next();
                                if (isTxt) {
                                    start = (int) ((note.lastPosition - A1) + ((long) j1));
                                    drawLineHighlight(canvas, line, baseline, j1, j2, (long) start, (long) (note.highlightLength + start), note.highlightColor, note.underline, note.strikethrough, note.squiggly, note, null);
                                } else if (A.lastChapter == note.lastChapter && A.lastSplitIndex == note.lastSplitIndex) {
                                    canvas2 = canvas;
                                    drawLineHighlight(canvas2, line, baseline, j1, j2, note.lastPosition, ((long) note.highlightLength) + note.lastPosition, note.highlightColor, note.underline, note.strikethrough, note.squiggly, note, null);
                                }
                            }
                        }
                        int id = A.getBookmarksId(A.lastFile);
                        if (id != -1) {
                            Iterator it2 = ((Bookmarks) A.getBookmarks().get(id)).list.iterator();
                            while (it2.hasNext()) {
                                Bookmark bm = (Bookmark) it2.next();
                                if (isTxt) {
                                    start = (int) ((bm.position - A1) + ((long) j1));
                                    drawLineHighlight(canvas, line, baseline, j1, j2, (long) start, (long) (start + 1), 0, false, false, false, null, bm);
                                } else if (A.lastChapter == bm.chapter && A.lastSplitIndex == bm.splitIndex) {
                                    drawLineHighlight(canvas, line, baseline, j1, j2, bm.position, bm.position + 1, 0, false, false, false, null, bm);
                                }
                            }
                        }
                        if (this.highlightAll != null) {
                            it = this.highlightAll.iterator();
                            while (it.hasNext()) {
                                note = (NoteInfo) it.next();
                                canvas2 = canvas;
                                drawLineHighlight(canvas2, line, baseline, j1, j2, note.lastPosition, ((long) note.highlightLength) + note.lastPosition, note.highlightColor, note.underline, note.strikethrough, note.squiggly, note, null);
                            }
                        }
                    }
                    if (this.hStart != -1) {
                        drawLineHighlight(canvas, line, baseline, j1, j2, (long) this.hStart, (long) this.hEnd, A.highlight_color1, false, false, false, null, null);
                    }
                }
            } catch (Exception e) {
                A.error(e);
                this.forceRedraw = false;
            }
        }
        if (this.forceRedraw) {
            this.forceRedraw = false;
            drawAllHighlight(canvas);
        }
    }

    private void createHighlightAllItems(int from, int to) {
        if (A.highlightAllItems == null || A.highlightAllItems.size() <= 0) {
            this.highlightAll = null;
            return;
        }
        if (from < 0) {
            from = 0;
        }
        if (to > getRealLineCount() - 1) {
            to = getRealLineCount() - 1;
        }
        int start = getLayout().getLineStart(from);
        int end = getLayout().getLineVisibleEnd(to);
        this.highlightAll = new ArrayList();
        String text = getText2();
        int i = 0;
        while (i < A.highlightAllItems.size()) {
            try {
                String key = (String) A.highlightAllItems.get(i);
                Integer mode = null;
                Integer color = null;
                int j = start;
                do {
                    int j2 = text.indexOf(key, j);
                    if (j2 == -1 || j2 >= end) {
                        break;
                    }
                    if (mode == null) {
                        String s = (String) A.highlightAllProperties.get(i);
                        mode = Integer.valueOf(s.substring(0, 1));
                        color = Integer.valueOf(s.substring(2));
                    }
                    NoteInfo n = new NoteInfo(0, "", "", 0, 0, (long) j2, key.length(), color.intValue(), 0, "", "", "", mode.intValue() == 1, mode.intValue() == 2, mode.intValue() == 3 ? "1" : "");
                    n.checkRealPosition = true;
                    this.highlightAll.add(n);
                    j = j2 + key.length();
                } while (j < text.length());
                i++;
            } catch (Exception e) {
                A.error(e);
                return;
            }
        }
    }

    private void drawLineHighlight(Canvas canvas, int line, int baseline, int j1, int j2, long start, long end, int color, boolean underline, boolean strike, boolean squiggly, NoteInfo note, Bookmark bm) {
        if (end > start) {
            boolean draw1 = start > ((long) j1) && start < ((long) j2);
            boolean draw2 = start <= ((long) j1) && end > ((long) j1);
            if (draw1 || draw2) {
                boolean update = false;
                if (!(note == null || note.checkRealPosition)) {
                    note.checkRealPosition = true;
                    if (!T.isNull(note.original)) {
                        if (!getText2().substring((int) start, (int) end).equals(note.original)) {
                            int noteIndex = getText2().indexOf(note.original, j1);
                            if (((long) noteIndex) != start) {
                                int forward = noteIndex;
                                if ((noteIndex == -1 || noteIndex - j1 > 200) && j1 > 50) {
                                    noteIndex = getText2().indexOf(note.original, j1 - 50);
                                }
                                if (noteIndex == -1 && j1 > 100) {
                                    noteIndex = getText2().indexOf(note.original, j1 - 100);
                                }
                                if (noteIndex == -1 && j1 > 200) {
                                    noteIndex = getText2().indexOf(note.original, j1 - 200);
                                }
                                if (noteIndex == -1 || Math.abs(start - ((long) noteIndex)) >= 200) {
                                    int backword = getText2().lastIndexOf(note.original, j1);
                                    if (!(backword == -1 && forward == -1)) {
                                        int i = backword == -1 ? forward : forward == -1 ? backword : j1 - backword > forward - j1 ? forward : backword;
                                        note.lastPosition += ((long) i) - start;
                                        update = true;
                                    }
                                } else {
                                    note.lastPosition += ((long) noteIndex) - start;
                                    update = true;
                                }
                            }
                        }
                    } else {
                        return;
                    }
                }
                if (!isLastHalfLine(line)) {
                    if (bm != null) {
                        bm.drawY = getLineTop(line);
                        if (A.fixIndentBookmarkPos(bm, getText2())) {
                        }
                        this.visualBookmarks.add(bm);
                    } else if (update) {
                        this.forceRedraw = true;
                        A.updateNote(note);
                    } else if (!isEmptyLine(line)) {
                        float l1;
                        Paint p;
                        int lineHeight = getLineHeight(line);
                        CharSequence text = getText();
                        if (draw2) {
                            int tmp = j1;
                            while (tmp < j2 && A.isEmptyChar(text.charAt(tmp))) {
                                tmp++;
                            }
                            l1 = (float) ((int) getTextX(line, tmp));
                        } else {
                            l1 = (float) ((int) getTextX(line, (int) start));
                        }
                        if (end >= ((long) j2)) {
                            while (((long) (j2 - 1)) > start && j2 - 1 > j1) {
                                if (text.charAt(j2 - 1) != ' ') {
                                    if (text.charAt(j2 - 1) != '\n') {
                                        break;
                                    }
                                }
                                j2--;
                            }
                        } else {
                            j2 = (int) end;
                        }
                        float l2 = (float) ((int) getTextX(line, j2));
                        if (lineHeight > (getLineHeight() * 110) / 100) {
                            lineHeight = getTextRealHeight((int) start, (int) end);
                        }
                        if (getLayout().getParagraphDirection(line) == -1) {
                            float l3 = l1;
                            if (selfDrawLine(line)) {
                                l1 = l2;
                                l2 = l3;
                            } else {
                                l1 = ((float) getWidth2()) - l2;
                                l2 = ((float) getWidth2()) - l3;
                            }
                        }
                        int lineOff = (lineHeight / 4) + 1;
                        int y1 = (baseline - lineHeight) + lineOff;
                        int y2 = baseline + lineOff;
                        if (underline || strike || squiggly) {
                            p = new Paint();
                            float stw = (A.fontSize / 9.0f) * A.getDensity();
                            if (stw < ((float) A.d(1.0f))) {
                                stw = (float) A.d(1.0f);
                            }
                            p.setStrokeWidth(stw);
                            int hori;
                            if (underline) {
                                if (color == 0) {
                                    color = -11184811;
                                }
                                p.setColor(color);
                                hori = baseline + A.d((3.0f * A.fontSize) / 18.0f);
                                canvas.drawLine(l1, (float) hori, l2, (float) hori, p);
                            } else if (strike) {
                                if (color == 0) {
                                    color = -11184811;
                                }
                                p.setColor(color);
                                hori = y2 - (((48 - A.lineSpace) * lineHeight) / 100);
                                canvas.drawLine(l1, (float) hori, l2, (float) hori, p);
                            } else {
                                if (color == 0) {
                                    color = -16711936;
                                }
                                p.setColor(color);
                                hori = baseline + A.d((2.0f * A.fontSize) / 17.0f);
                                A.drawSquiggly(canvas, l1, (float) hori, l2, (float) hori, p, (float) A.d(A.fontSize / 5.0f));
                            }
                        } else if (color != 0) {
                            p = new Paint();
                            p.setColor(color);
                            p.setAntiAlias(true);
                            boolean b = VERSION.SDK_INT >= 18;
                            int off = (y2 - y1) / 30;
                            canvas.drawRoundRect(new RectF(l1 - ((float) A.d(1.0f)), (float) (y1 + off), ((float) A.d(1.0f)) + l2, (float) (y2 - off)), (float) ((y2 - y1) / (b ? 3 : 7)), (float) (b ? 100 :300), p);
                        }
                        if (note != null && note.note.length() > 0) {
                            boolean isTxtFile = A.getFileType() == 0;
                            long x1 = isTxtFile ? A.getTxtRealPos((long) j1) : (long) j1;
                            long x2 = isTxtFile ? A.getTxtRealPos((long) j2) : (long) j2;
                            if (note.lastPosition >= x1 && note.lastPosition <= x2) {
                                Bitmap nbm = getNoteBitmap();
                                int bh = getLineHeight(line);
                                float w = (float) ((bh * 40) / 120);
                                float l = l1 - ((float) A.d(2.0f));
                                float t = (underline || squiggly) ? ((float) (y1 + bh)) - w : strike ? (((float) ((bh / 2) + y1)) - (w / 2.0f)) + ((float) A.d(1.5f)) : (float) (y1 - A.d(3.0f));
                                if (l < 0.0f) {
                                    l = 0.0f;
                                }
                                if (t < 0.0f) {
                                    t = 0.0f;
                                }
                                p = null;
                                if (underline || strike || squiggly || t == 0.0f) {
                                    p = new Paint();
                                    p.setAlpha(strike ? 180 : 200);
                                }
                                canvas.drawBitmap(nbm, new Rect(0, 0, nbm.getWidth(), nbm.getHeight()), new RectF(l, t, l + w, t + w), p);
                            }
                        }
                        if (!this.highlightLines.contains(Integer.valueOf(line))) {
                            this.highlightLines.add(Integer.valueOf(line));
                        }
                    }
                }
            }
        }
    }

    private int getTextRealHeight(int start, int end) {
        TextPaint workPaint = getPaint();
        if (getSpanned() != null) {
            CharacterStyle[] spans = (CharacterStyle[]) getSpanned().getSpans(start, end, CharacterStyle.class);
            if (spans.length > 0) {
                workPaint = new TextPaint(getPaint());
            }
            for (CharacterStyle sp : spans) {
                updateState(workPaint, sp);
            }
        }
        return Math.round((((float) workPaint.getFontMetricsInt(null)) * this.mSpacingMult) + this.mSpacingAdd);
    }

    public int getInLineHeight(Paint paint) {
        FontMetricsInt fm = new FontMetricsInt();
        paint.getFontMetricsInt(fm);
        return -fm.ascent;
    }

    public int getPaintExtra(Paint paint) {
        FontMetricsInt fm = new FontMetricsInt();
        paint.getFontMetricsInt(fm);
        return (int) (((double) ((((float) (fm.descent - fm.ascent)) * (this.mSpacingMult - 1.0f)) + this.mSpacingAdd)) + 0.5d);
    }

    public float getHoriCssMargin(int line, int lineAlign) {
        MarginF margins = getCssMargins(line);
        if (lineAlign == 4) {
            return -margins.right;
        }
        if (lineAlign == 3) {
            return margins.left - ((margins.left + margins.right) / 2.0f);
        }
        return margins.left;
    }

    public MarginF getCssMargins(int line) {
        if (this.lineMargins.containsKey(Integer.valueOf(line))) {
            return (MarginF) this.lineMargins.get(Integer.valueOf(line));
        }
        MarginF margins = new MarginF(0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        margins.indent = indentWidth();
        if (getSpanned() == null) {
            return margins;
        }
        for (MyMarginSpan sp : (MyMarginSpan[]) T.getSpans(getLineSpans(line), MyMarginSpan.class)) {
            margins.left += sp.leftMargin * CSS.EM();
            margins.top += sp.topMargin * CSS.EM();
            margins.right += sp.rightMargin * CSS.EM();
            margins.bottom += sp.bottomMargin * CSS.EM();
            if (sp.indent != 0.0f && (margins.indentSp == null || sp.spStart > margins.indentSp.spStart)) {
                margins.indentSp = sp;
                margins.indent = sp.indent * CSS.EM2();
            }
        }
        float floatWidth = (float) getLayout().getLineFloat(line);
        if (floatWidth >= 0.0f) {
            margins.left += floatWidth;
        } else {
            margins.right -= floatWidth;
        }
        if (margins.left > 0.0f) {
            int max_margin = (int) (((float) getWidth()) * 0.65f);
            if (margins.left > ((float) max_margin)) {
                margins.left = (float) max_margin;
            }
        }
        if (margins.right > 0.0f) {
            int max = getWidth() - ((int) (((((float) getWidth()) * CSS.KEEP_MAGIN) * 3.0f) / 4.0f));
            if (margins.left + margins.right > ((float) max)) {
                margins.right = ((float) max) - margins.left;
            }
        }
        this.lineMargins.put(Integer.valueOf(line), margins);
        return margins;
    }

    private Bitmap getNoteBitmap() {
        if (this.noteBm == null) {
            this.noteBm = BitmapFactory.decodeResource(getResources(), R.drawable.note_tag3);
        }
        return this.noteBm;
    }

    public boolean hasHighlight() {
        return true;
    }

    public LineRecord getLineRecord(int line) {
        if (lrCache == null) {
            lrCache = new HashMap();
        }
        if (lrCache.containsKey(Integer.valueOf(line))) {
            return (LineRecord) lrCache.get(Integer.valueOf(line));
        }
        LineRecord lr = getLineRecord2(line);
        lrCache.put(Integer.valueOf(line), lr);
        return lr;
    }

    public LineRecord getLineRecord2(int line) {
        if (line > getLineCount() - 1) {
            return null;
        }
        int j1 = getLayout().getLineStart(line);
        int j2 = getLayout().getLineVisibleEnd(line);
        if (j1 >= j2) {
            return null;
        }
        String lt = getText().subSequence(j1, j2).toString().replace("\t", " ");
        if (lt.length() == 0) {
            return null;
        }
        int lineAlign = getLineAlign(line);
        float off;
        LineRecord lineRecord;
        int l;
        int p;
        int i;
        if (lineAlign == 3 || lineAlign == 4) {
            off = ((float) getWidth2()) - getLayout().getLineMax(line);
            if (lineAlign == 3) {
                off /= 2.0f;
            }
            off += getHoriCssMargin(line, lineAlign);
            lineRecord = new LineRecord();
            l = lt.length();
            lineRecord.starts = new float[l];
            lineRecord.ends = new float[l];
            if (A.isBetaVersion) {
                lineRecord.chars = new char[l];
            }
            p = 0;
            boolean hasUnheritedSize = lineHasUnheritedSize(line);
            for (i = 0; i < lt.length(); i++) {
                if (A.isBetaVersion) {
                    lineRecord.chars[i] = getText().charAt(j1 + i);
                }
                lineRecord.starts[i] = ((float) p) + off;
                if (hasUnheritedSize) {
                    p = (int) (((float) p) + getDesiredWidth(line, j1 + i, (j1 + i) + 1));
                } else {
                    p = (int) (((float) p) + Layout.getDesiredWidth(getText(), j1 + i, (j1 + i) + 1, getPaint()));
                }
                lineRecord.ends[i] = ((float) p) + off;
            }
            return checkLineRecordDir(lineRecord, line);
        } else if (lt.endsWith("\n")) {
            return null;
        } else {
            if (selfDrawLine(line)) {
                MarginF margins = getCssMargins(line);
                float indent = isParagraphBegin(getText(), j1) ? margins.indent : 0.0f;
                off = getHoriCssMargin(line, lineAlign) + indent;
                float lt_width = ((((float) getWidth2()) - margins.left) - margins.right) - indent;
                ArrayList<Float> sizes = new ArrayList();
                ArrayList<String> words = getLineWords(lt, line, true, sizes, lt_width);
                if (words.size() > 0) {
                    lineRecord = new LineRecord();
                    l = 0;
                    Iterator it = words.iterator();
                    while (it.hasNext()) {
                        l += ((String) it.next()).length();
                    }
                    lineRecord.starts = new float[l];
                    lineRecord.ends = new float[l];
                    if (A.isBetaVersion) {
                        lineRecord.chars = new char[l];
                    }
                    l = 0;
                    for (i = 0; i < words.size(); i++) {
                        String word = (String) words.get(i);
                        p = 0;
                        for (int j = 0; j < word.length(); j++) {
                            if (A.isBetaVersion) {
                                lineRecord.chars[l] = getText().charAt(j1 + l);
                            }
                            lineRecord.starts[l] = (((Float) sizes.get(i)).floatValue() + off) + ((float) p);
                            p = (int) (((float) p) + getDesiredWidth(line, j1 + l, (j1 + l) + 1));
                            lineRecord.ends[l] = (((Float) sizes.get(i)).floatValue() + off) + ((float) p);
                            l++;
                        }
                    }
                    return checkLineRecordDir(lineRecord, line);
                }
            }
            return null;
        }
    }

    private boolean lineHasUnheritedSize(int line) {
        if (this.lineUnherited.containsKey(Integer.valueOf(line))) {
            return ((Boolean) this.lineUnherited.get(Integer.valueOf(line))).booleanValue();
        }
        if (T.spansHasKind(getLineSpans(line), MyRelativeSizeSpan.class)) {
            MyRelativeSizeSpan[] spans = (MyRelativeSizeSpan[]) T.getSpans(getLineSpans(line), MyRelativeSizeSpan.class);
            int length = spans.length;
            int i = 0;
            while (i < length) {
                if (spans[i].inherited) {
                    i++;
                } else {
                    this.lineUnherited.put(Integer.valueOf(line), Boolean.valueOf(true));
                    return true;
                }
            }
        }
        this.lineUnherited.put(Integer.valueOf(line), Boolean.valueOf(false));
        return false;
    }

    private LineRecord checkLineRecordDir(LineRecord lr, int line) {
        if (getLayout().getParagraphDirection(line) == -1) {
            int len = lr.starts.length;
            float w0 = lr.ends[0] - lr.starts[0];
            float x0 = lr.ends[len - 1] - w0;
            lr.starts[0] = x0;
            lr.ends[0] = x0 + w0;
            for (int i = 1; i < len; i++) {
                lr.starts[i] = lr.starts[i - 1] - (lr.ends[i] - lr.starts[i]);
                lr.ends[i] = lr.starts[i - 1];
            }
        }
        return lr;
    }

    private int getMyOffset(int line, float x) {
        LineRecord lr = getLineRecord(line);
        if (lr == null) {
            return -1;
        }
        int i;
        if (getLayout().getParagraphDirection(line) == -1) {
            for (i = 0; i < lr.ends.length; i++) {
                if (x > lr.starts[i] + (((lr.ends[i] - lr.starts[i]) + 1.0f) / 2.0f)) {
                    return i + 1;
                }
            }
        } else {
            for (i = lr.ends.length - 1; i >= 0; i--) {
                if (x > lr.starts[i] + (((lr.ends[i] - lr.starts[i]) + 1.0f) / 2.0f)) {
                    return i + 1;
                }
            }
        }
        return 0;
    }

    public int getLineOffset(int line, float x) {
        if (line >= getRealLineCount()) {
            return -1;
        }
        try {
            int offset;
            int j1 = getLayout().getLineStart(line);
            int j2 = getLayout().getLineVisibleEnd(line);
            if (selfDrawLine(line)) {
                offset = getMyOffset(line, x);
                if (offset != -1) {
                    return j1 + offset;
                }
            }
            if (isParagraphBegin(getText(), j1)) {
                x -= getCssMargins(line).indent;
            }
            int floatWidth = getLayout().getLineFloat(line);
            if (floatWidth > 0) {
                x -= (float) floatWidth;
            }
            offset = getLayout().getOffsetForHorizontal(line, x);
            if (offset != j2 - 1 || x <= (getCssMargins(line).left + getDesiredWidth(line, j1, offset)) + (getDesiredWidth(line, offset, offset + 1) / 3.0f)) {
                return offset;
            }
            return j2;
        } catch (Exception e) {
            A.error(e);
            A.log("-getLineOffset error, not finish measure?");
            return 0;
        }
    }

    public boolean selfDrawLine(int line) {
        boolean result = this.selfJustified;
//        Boolean paragraphEnd = null;
//        int lineEnd = getLayout().getLineVisibleEnd(line);
//        if (!isTxt) {
//            int lineAlign = getLineAlign(line);
//            if (lineAlign == 3 || lineAlign == 4) {
//                return true;
//            }
//            if (result || lineAlign == 2) {
//                paragraphEnd = Boolean.valueOf(isParagraphEnd(getText(), line, lineEnd));
//                result = (T.spansHasKind(getLineSpans(line), MyImageSpan.class) || paragraphEnd.booleanValue()) ? false : true;
//            }
//        }
//        if (result && this.selfJustified) {
//            if (paragraphEnd == null) {
//                paragraphEnd = Boolean.valueOf(isParagraphEnd(getText(), line, lineEnd));
//            }
//            if (paragraphEnd.booleanValue()) {
//                result = false;
//            }
//        }
        return result;
    }

    public void removeLineSpans(int line) {
        if (getSpanned() != null && this.lineSpans.containsKey(Integer.valueOf(line))) {
            this.lineSpans.remove(Integer.valueOf(line));
        }
    }

    public Object[] getLineSpans(int line) {
        if (line < 0 || getSpanned() == null) {
            return null;
        }
        if (this.lineSpans.containsKey(Integer.valueOf(line))) {
            return (Object[]) this.lineSpans.get(Integer.valueOf(line));
        }
        Object[] spans = getSpanned().getSpans(getLayout().getLineStart(line), getLayout().getLineVisibleEnd(line), Object.class);
        this.lineSpans.put(Integer.valueOf(line), spans);
        return spans;
    }

    public float getTextX(int line, int end) {
        float f = 0.0f;
        float width = -1.0f;
        if (selfDrawLine(line)) {
            width = getTextX2(line, end);
        }
        if (width != -1.0f) {
            return width;
        }
        int start = getLayout().getLineStart(line);
        float margin = getHoriCssMargin(line, getLineAlign(line));
        if (isParagraphBegin(getText(), start)) {
            margin += getCssMargins(line).indent;
        }
        if (margin < 0.0f) {
            margin = 0.0f;
        }
        if (start != end) {
            f = getDesiredWidth(line, start, end);
        }
        return margin + f;
    }

    private float getDesiredWidth(int line, int start, int end) {
        if (!lineHasUnheritedSize(line)) {
            return Layout.getDesiredWidth(getText(), start, end, getPaint());
        }
        int w = 0;
        for (int i = start; i < end; i++) {
            TextPaint p;
            CharacterStyle[] sps = (CharacterStyle[]) getSpanned().getSpans(i, i + 1, CharacterStyle.class);
            int hash = 0;
            for (CharacterStyle sp : sps) {
                hash += sp.hashCode();
            }
            if (this.lineHashPaints.containsKey(Integer.valueOf(hash))) {
                p = (TextPaint) this.lineHashPaints.get(Integer.valueOf(hash));
            } else {
                p = new TextPaint(getPaint());
                int k;
                if (VERSION.SDK_INT < 23) {
                    for (k = sps.length - 1; k >= 0; k--) {
                        updateState(p, sps[k]);
                    }
                } else {
                    Styled.sortSpansForAndroid6(getSpanned(), sps);
                    for (CharacterStyle updateState : sps) {
                        updateState(p, updateState);
                    }
                }
                this.lineHashPaints.put(Integer.valueOf(hash), p);
            }
            w = (int) (((float) w) + p.measureText(String.valueOf(getText().charAt(i))));
        }
        return (float) w;
    }

    private void updateState(TextPaint p, CharacterStyle sp) {
        if (!(sp instanceof MyRelativeSizeSpan)) {
            sp.updateDrawState(p);
        } else if (((MyRelativeSizeSpan) sp).inherited) {
            sp.updateDrawState(p);
        } else {
            p.setTextSize(getPaint().getTextSize() * ((RelativeSizeSpan) sp).getSizeChange());
        }
    }

    public float getTextX2(int line, int end) {
        LineRecord lr = getLineRecord(line);
        if (lr == null) {
            return -1.0f;
        }
        end -= getLayout().getLineStart(line);
        if (end < 0) {
            return -1.0f;
        }
        int len = lr.starts.length;
        return end < len ? lr.starts[end] : lr.ends[len - 1];
    }

    public MyLayout getLayout() {
        if (this != A.txtView2 || A.txtView == null) {
            return super.getLayout();
        }
        return A.txtView.getLayout();
    }

    public void drawVerticalChar(Canvas canvas, char c, Path path, TextPaint paint, float dw) {
        boolean m = VERSION.SDK_INT >= 23;
        float x = 0.0f;
        float y = 0.0f;
        if (c == ',' || c == '.') {
            x = m ? dw * 0.6f : vertOld ? dw * 0.65f : dw * 0.55f;
            y = m ? (-dw) * 0.5f : (-dw) * 0.7f;
        }
        if (c == '。') {
            x = m ? dw * 0.55f : vertOld ? dw * 0.5f : dw * 0.66f;
            y = m ? (-dw) * 0.45f : (-dw) * 0.6f;
        }
        if (c == '，' || c == '﹐' || c == '、' || c == '﹑' || c == '¸') {
            x = m ? dw * 0.22f : vertOld ? dw * 0.5f : dw * 0.33f;
            if (m) {
                y = (-dw) * 0.45f;
            } else {
                y = (-dw) * 0.6f;
            }
        } else if (c == '!') {
            if (m) {
                x = dw * 0.48f;
            } else {
                x = 0.45f;
            }
            y = m ? (-dw) * 0.05f : (-dw) * 0.2f;
        } else if (c == ':' || c == ';') {
            x = m ? dw * 0.4f : 0.65f;
            y = (-dw) * 0.2f;
        } else if (c == '﹕' || c == '﹔' || c == '：' || c == '；' || c == '`') {
            x = m ? dw * 0.25f : vertOld ? dw * 0.5f : dw * 0.33f;
            if (m) {
                y = (-dw) * 0.15f;
            } else {
                y = 0.0f;
            }
        } else if (c == '？' || c == '?' || c == '！') {
            x = vertOld ? dw * 0.3f : dw * 0.25f;
        } else if (c == '!') {
            x = m ? dw * 0.05f : vertOld ? dw * 0.3f : dw * 0.25f;
            if (m) {
                y = dw * 0.15f;
            } else {
                y = 0.0f;
            }
        } else if (c == '﹖' || c == '︰') {
            if (vertOld) {
                x = dw * 0.25f;
            } else {
                x = 0.0f;
            }
            if (m) {
                y = (-dw) * 0.1f;
            } else {
                y = 0.0f;
            }
        } else if (c == '“' || c == '‘' || c == '〝' || c == '「' || c == '『' || c == '﹁') {
            c = '﹁';
        } else if (c == '”' || c == '’' || c == '〞' || c == '」' || c == '』' || c == '﹂') {
            c = '﹂';
        } else if (c == '(' || c == '﹝' || c == '（') {
            c = '︵';
        } else if (c == ')' || c == '﹞' || c == '）') {
            c = '︶';
        } else if (c == '{') {
            c = '︷';
        } else if (c == '}') {
            c = '︸';
        } else if (c == '[') {
            c = '︹';
        } else if (c == ']') {
            c = '︺';
        } else if (c == '<' || c == '︿') {
            c = '︿';
            x = m ? 0.0f : dw * 0.33f;
        } else if (c == '>' || c == '﹀') {
            c = '﹀';
            x = m ? 0.0f : dw * 0.33f;
        } else if (c == '《') {
            c = '︽';
        } else if (c == '》') {
            c = '︾';
        } else if (c == '…' || c == '—' || c == '-' || c == '_') {
            c = '|';
            x = dw * 0.25f;
        }
        canvas.drawTextOnPath(c + "", path, x, y, paint);
    }

    public String getLineText(int line) {
        return getText().subSequence(getLayout().getLineStart(line), getLayout().getLineVisibleEnd(line)).toString();
    }

    public boolean isEmptyLine(int line) {
        int i1 = getLayout().getLineStart(line);
        int i2 = getLayout().getLineVisibleEnd(line);
        return i2 - i1 < 5 && A.isEmtpyText(getText2(), i1, i2);
    }

    public int getLineHeight() {
        if (this == A.txtView2 && A.txtView != null) {
            return A.txtView.getLineHeight();
        }
        if (this.mLineHeight > 0) {
            return this.mLineHeight;
        }
        try {
            MyLayout lo = getLayout();
            if (lo != null) {
                int count = getLineCount();
                if (count > 1) {
                    for (int i = count - 1; i > 0; i--) {
                        if (lo.getLineVisibleEnd(i - 1) - lo.getLineStart(i - 1) > 4) {
                            boolean ok;
                            Object[] spans = getLineSpans(i);
                            if (spans == null || spans.length == 0) {
                                ok = true;
                            } else {
                                ok = false;
                            }
                            if (!ok && A.useCssFont && spans != null && spans.length == 1 && (spans[0] instanceof MyTypefaceSpan)) {
                                ok = true;
                            }
                            if (ok) {
                                int t1 = lo.getLineTop(i);
                                int t2 = lo.getLineTop(i - 1);
                                if (t1 > t2) {
                                    this.mLineHeight = t1 - t2;
                                    break;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            A.error(e);
            this.mLineHeight = this.mLineHeight2;
        }
        if (this.mLineHeight == 0) {
            this.mLineHeight = super.getLineHeight();
        }
        this.mLineHeight2 = this.mLineHeight;
        return this.mLineHeight;
    }

    public int getLineHeight2() {
        if (this.lineHeight2 == 0) {
            this.lineHeight2 = super.getLineHeight();
        }
        return this.lineHeight2;
    }

    public int getLineHeight(int line) {
        try {
            int lh = (getLayout().getLineTop(line + 1) - getLayout().getLineTop(line)) - getLayout().getLineTopAdded(line);
            if (lh > 0) {
                return lh;
            }
            return getLineHeight();
        } catch (Exception e) {
            A.error(e);
            return getLineHeight();
        }
    }

    public void clearLrCache() {
        clearLrCache2();
        CSS.em2 = null;
        this.lineHeight2 = 0;
        this.mLineHeight = 0;
        this.appendLineCount = 0;
        this.italicIgnoreWidth = 0.0f;
        this.layoutState = 0;
        this.indentWidth = Layout.getDesiredWidth("一", getPaint());
        this.lastTxtChapterLine = -100;
    }

    public void clearLrCache2() {
        if (lrCache != null) {
            lrCache.clear();
        }
        if (this.lineSpans != null) {
            this.lineSpans.clear();
        }
        if (this.lineMargins != null) {
            this.lineMargins.clear();
        }
        if (this.lineHashPaints != null) {
            this.lineHashPaints.clear();
        }
        if (this.lineUnherited != null) {
            this.lineUnherited.clear();
        }
        if (this.lineAligns != null) {
            this.lineAligns.clear();
        }
    }

    public float indentWidth() {
        return A.indentParagraph ? this.indentWidth * ((float) A.indentLength) : 0.0f;
    }

    public void setText(CharSequence text) {
        this.pureText = null;
        clearLrCache();
        super.setText(text);
    }

    public void setTextSize(float size) {
        clearLrCache();
        super.setTextSize(size);
    }

    public void setTypeface(Typeface tf) {
        clearLrCache();
        super.setTypeface(tf);
    }

    public void setLineSpacing(float add, float mult) {
        clearLrCache();
        super.setLineSpacing(add, mult);
    }

    public CharSequence getText() {
        if (this != A.txtView2 || A.txtView == null) {
            return super.getText();
        }
        return A.txtView.getText();
    }

    public int getLineTop(int line) {
        if (line == 0) {
            return 0;
        }
        return getLayout().getLineTop(line);
    }

    public int getLineTop2(int line) {
        if (line == 0) {
            return 0;
        }
        int t = getLayout().getLineTop(line) + getLayout().getLineTopAdded2(line);
        if (lineTopBorderSp(line) != null) {
            t = (int) (((float) t) - A.df(2.0f));
        }
        if (!lineHasRuby(line)) {
            return t;
        }
        return t - (((A.verticalAlignment ? 23 : 25) * getLineHeight(line)) / 100);
    }

    public int getLineTop3(int line) {
        return getLayout().getLineTop(line) + getLayout().getLineTopAdded(line);
    }

    public String getText2() {
        if (this.pureText == null) {
            this.pureText = getText().toString();
        }
        return this.pureText;
    }

    public int getRealLineCount() {
        if (this == A.txtView2 && A.txtView != null) {
            return A.txtView.getRealLineCount();
        }
        return getLineCount() - (this.appendLineCount == 0 ? 0 : 1);
    }

    public int getRealHeight() {
        if (this != A.txtView2 || A.txtView == null) {
            return this.appendLineCount == 0 ? getHeight() : getLayout().getLineTop(getRealLineCount());
        } else {
            return A.txtView.getRealHeight();
        }
    }

    public void appendEmptyLines(int count) {
        if (count > 0 && (getLayout() instanceof SoftHyphenStaticLayout)) {
            SoftHyphenStaticLayout lo = (SoftHyphenStaticLayout) getLayout();
            if (this.appendLineCount == 0) {
                lo.mLineCount++;
            }
            this.appendLineCount += count;
            int nlen = ArrayUtils.idealIntArraySize(((lo.mColumns + (lo.mLineCount * lo.mColumns)) + 1) + 1);
            if (nlen > lo.mLines.length) {
                int[] grow = new int[nlen];
                System.arraycopy(lo.mLines, 0, grow, 0, lo.mLines.length);
                lo.mLines = grow;
                Directions[] grow2 = new Directions[nlen];
                System.arraycopy(lo.mLineDirections, 0, grow2, 0, lo.mLineDirections.length);
                lo.mLineDirections = grow2;
            }
            int lh = getLineHeight();
            int height = lo.getLineTop(getRealLineCount());
            lo.setLineStart(lo.mLineCount, getText().length());
            int i = lo.mLineCount;
            int i2 = (this.appendLineCount * lh) + height;
            if (A.dualPageEnabled()) {
                lh *= 5;
            }
            lo.setLineTop(i, i2 + lh);
            int add = lo.getHeight() - height;
            try {
                Field field = View.class.getDeclaredField("mBottom");
                field.setAccessible(true);
                this.mBottom2 = field.getInt(this) + add;
                field.set(this, Integer.valueOf(this.mBottom2));
                if (A.dualPageEnabled() && A.txtView2 != null) {
                    field.set(A.txtView2, Integer.valueOf(this.mBottom2));
                }
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    public void restoreAppendedBottom() {
        if (this.appendLineCount > 0 && this.mBottom2 > 0) {
            try {
                Field field = View.class.getDeclaredField("mBottom");
                field.setAccessible(true);
                if (field.getInt(this) < this.mBottom2) {
                    field.set(this, Integer.valueOf(this.mBottom2));
                    if (A.dualPageEnabled() && A.txtView2 != null) {
                        field.set(A.txtView2, Integer.valueOf(this.mBottom2));
                    }
                }
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    public void setForceHeight(int h) {
        try {
            Field topField = View.class.getDeclaredField("mTop");
            topField.setAccessible(true);
            int top = topField.getInt(this);
            Field bottomField = View.class.getDeclaredField("mBottom");
            bottomField.setAccessible(true);
            bottomField.set(this, Integer.valueOf(top + h));
        } catch (Exception e) {
            A.error(e);
        }
    }

    public Spanned getSpanned() {
        if (getText() != null && (getText() instanceof Spanned)) {
            return (Spanned) getText();
        }
        return null;
    }

    public boolean isNormalImageLine(int line) {
        if (isTxt || this == A.txtView2 || line > getLineCount() - 1) {
            return false;
        }
        int lh = getLineHeight(line);
        if (((double) lh) < ((double) getLineHeight()) * 1.5d || !lineHasImage(line)) {
            return false;
        }
        if ((lh < A.getPageHeight() || A.dualPageEnabled()) && getLayout().getLineTop(line + 1) > A.getPageHeight() + A.txtScroll.getScrollY()) {
            return true;
        }
        return false;
    }

    public boolean lineHasImage(int line) {
//        if (isTxt || line > getLineCount() - 1 || getSpanned() == null) {
//            return false;
//        }
//        MyImageSpan[] images = (MyImageSpan[]) T.getSpans(getLineSpans(line), MyImageSpan.class);
//        if (images == null || images.length <= 0) {
//            return false;
//        }
        return false;
    }

    public boolean lineIsSuperLongImage(int line) {
        if (isTxt || line > getLineCount() - 1 || !lineHasImage(line)) {
            return false;
        }
        if (getLayout().getLineTop(line + 1) - getLayout().getLineTop(line) >= A.getPageHeight()) {
            return true;
        }
        return false;
    }
}