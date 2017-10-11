package com.flyersoft.staticlayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.Scroller;
import com.flyersoft.books.A;
import com.flyersoft.books.T;
import com.flyersoft.components.CSS;
import com.flyersoft.moonreader.ActivityTxt;
import com.flyersoft.moonreader.R;
import com.flyersoft.staticlayout.MyLayout.Alignment;

public class MyTextView extends View implements OnPreDrawListener {
    private static final int PREDRAW_DONE = 2;
    private static final int PREDRAW_NOT_REGISTERED = 0;
    private static final int PREDRAW_PENDING = 1;
    public boolean drawMarginBackgroundOnly;
    private int mCurTextColor;
    public Paint mHighlightPaint;
    private MyLayout mHintLayout;
    private MyLayout mLayout;
    private ColorStateList mLinkTextColor;
    private int mMaximum;
    private int mMinimum;
    private int mPreDrawState;
    private Scroller mScroller;
    private float mShadowDx;
    private float mShadowDy;
    private float mShadowRadius;
    public float mSpacingAdd;
    public float mSpacingMult;
    private CharSequence mText;
    private ColorStateList mTextColor;
    public int mTextHash;
    private TextPaint mTextPaint;
    private boolean mUserSetTextScaleX;
    public int outerWidth2;

    public enum BufferType {
        NORMAL,
        SPANNABLE,
        EDITABLE
    }

    static {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.measureText("H");
    }

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842884);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mPreDrawState = 0;
        this.mSpacingMult = 1.0f;
        this.mSpacingAdd = 0.0f;
        this.mMaximum = Integer.MAX_VALUE;
        this.mMinimum = 0;
        this.mScroller = null;
        this.mText = "";
        this.mTextPaint = new TextPaint(1);
        this.mHighlightPaint = new Paint(1);
        setTextColor(ColorStateList.valueOf(ViewCompat.MEASURED_STATE_MASK));
        setRawTextSize((float) 15);
        setText("");
    }

    public void setTypeface(Typeface tf, int style) {
        boolean z = false;
        if (style > 0) {
            int typefaceStyle;
            float f;
            if (tf == null) {
                tf = Typeface.defaultFromStyle(style);
            } else {
                tf = Typeface.create(tf, style);
            }
            setTypeface(tf);
            if (tf != null) {
                typefaceStyle = tf.getStyle();
            } else {
                typefaceStyle = 0;
            }
            int need = style & (typefaceStyle ^ -1);
            TextPaint textPaint = this.mTextPaint;
            if ((need & 1) != 0) {
                z = true;
            }
            textPaint.setFakeBoldText(z);
            textPaint = this.mTextPaint;
            if ((need & 2) != 0) {
                f = -0.25f;
            } else {
                f = 0.0f;
            }
            textPaint.setTextSkewX(f);
            return;
        }
        this.mTextPaint.setFakeBoldText(false);
        this.mTextPaint.setTextSkewX(0.0f);
        setTypeface(tf);
    }

    public CharSequence getText() {
        return this.mText;
    }

    public int getLineHeight() {
        int result = Math.round((((float) this.mTextPaint.getFontMetricsInt(null)) * this.mSpacingMult) + this.mSpacingAdd);
        return result > 0 ? result : A.d(A.fontSize);
    }

    public MyLayout getLayout() {
        return this.mLayout;
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (!(left == getPaddingLeft() && right == getPaddingRight() && top == getPaddingTop() && bottom == getPaddingBottom())) {
            nullLayouts();
        }
        super.setPadding(left, top, right, bottom);
        invalidate();
    }

    public float getTextSize() {
        return this.mTextPaint.getTextSize();
    }

    public void setTextSize(float size) {
        setTextSize(2, size);
    }

    public void setTextSize(int unit, float size) {
        Resources r;
        Context c = getContext();
        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        setRawTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
    }

    private void setRawTextSize(float size) {
        if (size != this.mTextPaint.getTextSize()) {
            this.mTextPaint.setTextSize(size);
            if (this.mLayout != null) {
                rebuildLayout();
            }
        }
    }

    private void rebuildLayout() {
        if (T.isNull(this.mText)) {
            checkForRelayout();
            return;
        }
        nullLayouts();
        requestLayout();
        invalidate();
    }

    public float getTextScaleX() {
        return this.mTextPaint.getTextScaleX();
    }

    public void setTextScaleX(float size) {
        if (size != this.mTextPaint.getTextScaleX()) {
            this.mUserSetTextScaleX = true;
            this.mTextPaint.setTextScaleX(size);
            if (this.mLayout != null) {
                rebuildLayout();
            }
        }
    }

    public void setTypeface(Typeface tf) {
        if (this.mTextPaint.getTypeface() != tf) {
            this.mTextPaint.setTypeface(tf);
            if (this.mLayout != null) {
                rebuildLayout();
            }
        }
    }

    public Typeface getTypeface() {
        return this.mTextPaint.getTypeface();
    }

    public void setTextColor(int color) {
        this.mTextColor = ColorStateList.valueOf(color);
        this.mLinkTextColor = ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary));
        updateTextColors();
    }

    public void setTextColor(ColorStateList colors) {
        if (colors == null) {
            throw new NullPointerException();
        }
        this.mTextColor = colors;
        updateTextColors();
    }

    public void setShadowLayer(float radius, float dx, float dy, int color) {
        this.mTextPaint.setShadowLayer(radius, dx, dy, color);
        this.mShadowRadius = radius;
        this.mShadowDx = dx;
        this.mShadowDy = dy;
        invalidate();
    }

    public TextPaint getPaint() {
        return this.mTextPaint;
    }

    public void setLineSpacing(float add, float mult) {
        this.mSpacingMult = mult;
        this.mSpacingAdd = add;
        if (this.mLayout != null) {
            rebuildLayout();
        }
    }

    private void updateTextColors() {
        boolean inval = false;
        int color = this.mTextColor.getColorForState(getDrawableState(), 0);
        if (color != this.mCurTextColor) {
            this.mCurTextColor = color;
            inval = true;
        }
        if (this.mLinkTextColor != null) {
            color = this.mLinkTextColor.getColorForState(getDrawableState(), 0);
            if (color != this.mTextPaint.linkColor) {
                this.mTextPaint.linkColor = color;
                inval = true;
            }
        }
        if (inval) {
            invalidate();
        }
    }

    public void setText(CharSequence text) {
        if (text == null) {
            text = "";
        }
        if (!this.mUserSetTextScaleX) {
            this.mTextPaint.setTextScaleX(1.0f);
        }
        this.mText = TextUtils.stringOrSpannedString(text);
        if (this.mLayout != null) {
            checkForRelayout();
        }
    }

    private void registerForPreDraw() {
        ViewTreeObserver observer = getViewTreeObserver();
        if (observer != null) {
            if (this.mPreDrawState == 0) {
                observer.addOnPreDrawListener(this);
                this.mPreDrawState = 1;
            } else if (this.mPreDrawState == 2) {
                this.mPreDrawState = 1;
            }
        }
    }

    public boolean onPreDraw() {
        if (this.mPreDrawState != 1) {
            return true;
        }
        if (this.mLayout == null) {
            assumeLayout();
        }
        boolean changed = bringTextIntoView();
        this.mPreDrawState = 2;
        if (changed) {
            return false;
        }
        return true;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int right = getRight();
        int left = getLeft();
        int bottom = getBottom();
        int top = getTop();
        int color = this.mCurTextColor;
        if (this.mLayout == null) {
            assumeLayout();
        }
        MyLayout layout = this.mLayout;
        this.mTextPaint.setColor(color);
        this.mTextPaint.drawableState = getDrawableState();
        canvas.save();
        float clipLeft = (float) scrollX;
        float clipTop = (float) scrollY;
        float clipRight = (float) ((right - left) + scrollX);
        float clipBottom = (float) ((bottom - top) + scrollY);
        if (this.mShadowRadius != 0.0f) {
            clipLeft += Math.min(0.0f, this.mShadowDx - this.mShadowRadius);
            clipRight += Math.max(0.0f, this.mShadowDx + this.mShadowRadius);
            clipTop += Math.min(0.0f, this.mShadowDy - this.mShadowRadius);
            clipBottom += Math.max(0.0f, this.mShadowDy + this.mShadowRadius);
        }
        canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);
        canvas.translate(0.0f, 0.0f);
        try {
            layout.tv = (MRTextView) this;
            layout.draw(canvas, null, this.mHighlightPaint, 0);
        } catch (Exception e) {
            A.error(e);
        }
        canvas.restore();
    }

    public int getLineCount() {
        return this.mLayout != null ? this.mLayout.getLineCount() : 0;
    }

    public int getLineBounds(int line, Rect bounds) {
        if (this.mLayout == null) {
            if (bounds != null) {
                bounds.set(0, 0, 0, 0);
            }
            return 0;
        }
        int baseline = this.mLayout.getLineBounds(line, bounds);
        if (bounds == null) {
            return baseline;
        }
        bounds.offset(0, 0);
        return baseline;
    }

    public int getBaseline() {
        if (this.mLayout == null) {
            return super.getBaseline();
        }
        return this.mLayout.getLineBaseline(0);
    }

    public void nullLayouts() {
        this.mHintLayout = null;
        this.mLayout = null;
    }

    public void assumeLayout() {
        int width = getRight() - getLeft();
        if (width < 0) {
            width = 0;
        }
        makeNewLayout(width);
    }

    protected void makeNewLayout(int w) {
        if (w < 0) {
            w = 0;
        }
        ActivityTxt act = ActivityTxt.selfPref;
        if (act != null && !act.isFinishing()) {
            if (act.isPaused && !A.isSpeaking) {
                return;
            }
            if (A.txtView == null || this == A.txtView || A.txtView.getLayout() == null) {
                this.mLayout = new SoftHyphenStaticLayout(this, this.mText, this.mTextPaint, w, Alignment.ALIGN_LEFT, this.mSpacingMult, this.mSpacingAdd, true);
                return;
            }
            this.mLayout = A.txtView.getLayout();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = widthSize;
        int want = width;
        int unpaddedWidth = want;
        if (this.mLayout == null) {
            makeNewLayout(want);
        } else if (this.mLayout.getWidth() != want) {
            boolean ok = true;
            if (ActivityTxt.selfPref != null && SystemClock.elapsedRealtime() - ActivityTxt.selfPref.setLayoutTime < 300) {
                ok = false;
            } else if (A.dualPageEnabled() && getLayout().getLineCount() > 0) {
                if (this.outerWidth2 == want && this.mTextHash == this.mText.hashCode()) {
                    ok = false;
                } else if (A.baseFrame != null && want > (A.baseFrame.getWidth() / 2) + 2) {
                    ok = false;
                }
            }
            if (ok) {
                if (this instanceof MRTextView) {
                    ((MRTextView) this).clearLrCache();
                }
                makeNewLayout(want);
            }
        }
        if (heightMode == Schema.M_PCDATA) {
            height = heightSize;
        } else {
            int desired = getDesiredHeight();
            height = desired;
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(desired, height);
            }
        }
        int unpaddedHeight = height;
        if (this.mLayout != null) {
            if (this.mLayout.getLineCount() > this.mMaximum) {
                unpaddedHeight = Math.min(unpaddedHeight, this.mLayout.getLineTop(this.mMaximum));
            }
            if (this.mLayout.getWidth() > unpaddedWidth || this.mLayout.getHeight() > unpaddedHeight) {
                registerForPreDraw();
            } else {
                scrollTo(0, 0);
            }
        }
        setMeasuredDimension(width, height);
    }

    private int getDesiredHeight() {
        return Math.max(getDesiredHeight(this.mLayout, true), getDesiredHeight(this.mHintLayout, false));
    }

    private int getDesiredHeight(MyLayout layout, boolean cap) {
        if (layout == null) {
            return 0;
        }
        int linecount = layout.getLineCount();
        int desired = layout.getLineTop(linecount);
        if (cap && linecount > this.mMaximum) {
            desired = layout.getLineTop(this.mMaximum) + layout.getBottomPadding();
            linecount = this.mMaximum;
        }
        if (linecount < this.mMinimum) {
            desired += getLineHeight() * (this.mMinimum - linecount);
        }
        return Math.max(desired, getSuggestedMinimumHeight());
    }

    private void checkForRelayout() {
        int oldht = this.mLayout.getHeight();
        makeNewLayout(this.mLayout.getWidth());
        if (this.mLayout.getHeight() == oldht) {
            invalidate();
            return;
        }
        requestLayout();
        invalidate();
    }

    private boolean bringTextIntoView() {
        int scrollx;
        Alignment a = this.mLayout.getParagraphAlignment(0);
        int dir = this.mLayout.getParagraphDirection(0);
        int hspace = getRight() - getLeft();
        if (a == Alignment.ALIGN_CENTER) {
            int left = (int) Math.floor((double) this.mLayout.getLineLeft(0));
            int right = (int) Math.ceil((double) this.mLayout.getLineRight(0));
            if (right - left < hspace) {
                scrollx = ((right + left) / 2) - (hspace / 2);
            } else if (dir < 0) {
                scrollx = right - hspace;
            } else {
                scrollx = left;
            }
        } else if (MyLayout.alignNormal(a)) {
            if (dir < 0) {
                scrollx = ((int) Math.ceil((double) this.mLayout.getLineRight(0))) - hspace;
            } else {
                scrollx = (int) Math.floor((double) this.mLayout.getLineLeft(0));
            }
        } else if (dir < 0) {
            scrollx = (int) Math.floor((double) this.mLayout.getLineLeft(0));
        } else {
            scrollx = ((int) Math.ceil((double) this.mLayout.getLineRight(0))) - hspace;
        }
        if (scrollx == getScrollX() && 0 == getScrollY()) {
            return false;
        }
        scrollTo(scrollx, 0);
        return true;
    }

    public void computeScroll() {
        if (this.mScroller != null && this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
        }
    }

    protected int computeHorizontalScrollRange() {
        if (this.mLayout != null) {
            return this.mLayout.getWidth();
        }
        return super.computeHorizontalScrollRange();
    }

    protected int computeVerticalScrollRange() {
        if (this.mLayout != null) {
            return this.mLayout.getHeight();
        }
        return super.computeVerticalScrollRange();
    }

    protected int computeVerticalScrollExtent() {
        return getHeight();
    }
}