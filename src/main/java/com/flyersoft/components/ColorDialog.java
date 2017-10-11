package com.flyersoft.components;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.flyersoft.books.A;
import com.flyersoft.moonreader.R;

public class ColorDialog extends Dialog implements OnSeekBarChangeListener, OnClickListener {
    static final int[] STATE_FOCUSED = new int[]{16842908};
    static final int[] STATE_PRESSED = new int[]{16842919};
    static boolean locked = false;
    EditText alphaEt;
    Button b1;
    Button b2;
    EditText blueEt;
    ImageView c1;
    ImageView c2;
    ImageView c3;
    ImageView c4;
    ImageView c5;
    ImageView c6;
    ImageView c7;
    ImageView c8;
    ImageView c9;
    private SeekBar cAlpha;
    private SeekBar cB;
    private SeekBar cG;
    private SeekBar cR;
    private ColorPickerView cView;
    LinearLayout colorLay;
    OnFocusChangeListener etFocusChanged = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            try {
                int red = ColorDialog.this.cR.getProgress();
                int green = ColorDialog.this.cG.getProgress();
                int blue = ColorDialog.this.cB.getProgress();
                int alpha = ColorDialog.this.cAlpha.getProgress();
                int value = ColorDialog.this.getValue((EditText) v);
                if (v == ColorDialog.this.redEt) {
                    red = value;
                } else if (v == ColorDialog.this.greenEt) {
                    green = value;
                } else if (v == ColorDialog.this.blueEt) {
                    blue = value;
                } else if (v == ColorDialog.this.alphaEt) {
                    alpha = value;
                }
                if (ColorDialog.this.mUseAlpha) {
                    ColorDialog.this.mColor = Color.argb(alpha, red, green, blue);
                } else {
                    ColorDialog.this.mColor = Color.rgb(red, green, blue);
                }
                ColorDialog.this.setRGBColor(ColorDialog.this.mColor);
            } catch (Exception e) {
                ColorDialog.this.update();
            }
        }
    };
    EditText greenEt;
    OnColorChangedListener l = new OnColorChangedListener() {
        public void colorChanged(int color) {
            ColorDialog.this.setRGBColor(Color.argb(Color.alpha(ColorDialog.this.mColor), Color.red(color), Color.green(color), Color.blue(color)));
        }
    };
    private int mColor = -1;
    Context mContext;
    private IconPreviewDrawable mIcon;
    private OnSaveColor mListener;
    private GradientDrawable mPreviewDrawable;
    private boolean mUseAlpha;
    View phExit;
    TextView phTitle;
    EditText redEt;
    Resources res;
    View root;

    public interface OnColorChangedListener {
        void colorChanged(int i);
    }

    private static class ColorPickerView extends View {
        private static final float PI = 3.1415925f;
        private int CENTER_X = 90;
        private int CENTER_Y = 90;
        private Paint mCenterPaint;
        private final int[] mColors;
        private int[] mHSVColors;
        private Paint mHSVPaint;
        private OnColorChangedListener mListener;
        private Paint mPaint;
        private boolean mRedrawHSV;

        ColorPickerView(Context c, OnColorChangedListener l, int color) {
            super(c);
            this.mListener = l;
            this.mColors = new int[]{SupportMenu.CATEGORY_MASK, -65281, -16776961, -16711681, -16711936, InputDeviceCompat.SOURCE_ANY, SupportMenu.CATEGORY_MASK};
            Shader s = new SweepGradient(0.0f, 0.0f, this.mColors, null);
            this.mPaint = new Paint(1);
            this.mPaint.setShader(s);
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setStrokeWidth((float) this.CENTER_X);
            this.mCenterPaint = new Paint(1);
            this.mCenterPaint.setColor(color);
            this.mCenterPaint.setStrokeWidth(5.0f);
            this.mHSVColors = new int[]{ViewCompat.MEASURED_STATE_MASK, color, -1};
            this.mHSVPaint = new Paint(1);
            this.mHSVPaint.setStrokeWidth(10.0f);
            this.mRedrawHSV = true;
        }

        protected void onDraw(Canvas canvas) {
            float r = ((float) this.CENTER_X) - (this.mPaint.getStrokeWidth() * 0.5f);
            canvas.translate((float) this.CENTER_X, (float) this.CENTER_X);
            int c = this.mCenterPaint.getColor();
            if (this.mRedrawHSV) {
                this.mHSVColors[1] = c;
                this.mHSVPaint.setShader(getLinearGradient());
            }
            canvas.drawOval(new RectF(-r, -r, r, r), this.mPaint);
            canvas.drawRect(new RectF(-100.0f, 130.0f, 100.0f, 110.0f), this.mHSVPaint);
            this.mRedrawHSV = true;
        }

        private LinearGradient getLinearGradient() {
            return new LinearGradient(-100.0f, 0.0f, 100.0f, 0.0f, this.mHSVColors, null, TileMode.CLAMP);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(this.CENTER_X * 2, this.CENTER_Y * 2);
        }

        private int ave(int s, int d, float p) {
            return Math.round(((float) (d - s)) * p) + s;
        }

        private int interpColor(int[] colors, float unit) {
            if (unit <= 0.0f) {
                return colors[0];
            }
            if (unit >= 1.0f) {
                return colors[colors.length - 1];
            }
            float p = unit * ((float) (colors.length - 1));
            int i = (int) p;
            p -= (float) i;
            int c0 = colors[i];
            int c1 = colors[i + 1];
            return Color.argb(ave(Color.alpha(c0), Color.alpha(c1), p), ave(Color.red(c0), Color.red(c1), p), ave(Color.green(c0), Color.green(c1), p), ave(Color.blue(c0), Color.blue(c1), p));
        }

        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX() - ((float) this.CENTER_X);
            float y = event.getY() - ((float) this.CENTER_Y);
            switch (event.getAction()) {
                case 0:
                case 2:
                    if (x >= -100.0f && x <= 100.0f && y <= 130.0f && y >= 110.0f) {
                        int c0;
                        int c1;
                        float p;
                        if (x < 0.0f) {
                            c0 = this.mHSVColors[0];
                            c1 = this.mHSVColors[1];
                            p = (100.0f + x) / 100.0f;
                        } else {
                            c0 = this.mHSVColors[1];
                            c1 = this.mHSVColors[2];
                            p = x / 100.0f;
                        }
                        this.mCenterPaint.setColor(Color.argb(ave(Color.alpha(c0), Color.alpha(c1), p), ave(Color.red(c0), Color.red(c1), p), ave(Color.green(c0), Color.green(c1), p), ave(Color.blue(c0), Color.blue(c1), p)));
                        this.mRedrawHSV = false;
                        break;
                    }
                    float unit = ((float) Math.atan2((double) y, (double) x)) / 6.283185f;
                    if (unit < 0.0f) {
                        unit += 1.0f;
                    }
                    this.mCenterPaint.setColor(interpColor(this.mColors, unit));
                    break;
                case 1:
                    this.mListener.colorChanged(this.mCenterPaint.getColor());
                    break;
            }
            return true;
        }
    }

    static class IconPreviewDrawable extends Drawable {
        private Bitmap mBitmap;
        private int mTintColor;
        private Bitmap mTmpBitmap;
        private Canvas mTmpCanvas;

        public IconPreviewDrawable(Resources res) {
            try {
                Bitmap b = BitmapFactory.decodeResource(res, R.drawable.icon);
                this.mBitmap = b;
                this.mTmpBitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight() - 6, Config.ARGB_8888);
                this.mTmpCanvas = new Canvas(this.mTmpBitmap);
            } catch (Exception e) {
                A.error(e);
            }
        }

        public void draw(Canvas canvas) {
            Rect b = getBounds();
            float x = ((float) (b.width() - this.mBitmap.getWidth())) / 2.0f;
            float y = (0.75f * ((float) b.height())) - (((float) this.mBitmap.getHeight()) / 2.0f);
            this.mTmpCanvas.drawColor(0, Mode.CLEAR);
            this.mTmpCanvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, null);
            this.mTmpCanvas.drawColor(this.mTintColor, Mode.SRC_ATOP);
            canvas.drawBitmap(this.mTmpBitmap, x, y, null);
        }


        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter cf) {
        }

        public void setColorFilter(int color, Mode mode) {
            this.mTintColor = color;
        }

        @Override
        public int getOpacity() {
            return 0;
        }
    }

    public interface OnSaveColor {
        void getColor(int i);
    }

    static class ScrollAnimation extends Animation {
        private static final long DURATION = 250;
        private float mCurrent;
        private float mFrom;
        private float mTo;

        public ScrollAnimation() {
            setDuration(DURATION);
            setInterpolator(new DecelerateInterpolator());
        }

        public void startScrolling(float from, float to) {
            this.mFrom = from;
            this.mTo = to;
            startNow();
        }

        protected void applyTransformation(float interpolatedTime, Transformation t) {
            this.mCurrent = this.mFrom + ((this.mTo - this.mFrom) * interpolatedTime);
        }

        public float getCurrent() {
            return this.mCurrent;
        }
    }

    class TextSeekBarDrawable extends Drawable implements Runnable {
        private static final long DELAY = 50;
        private boolean mActive;
        private ScrollAnimation mAnimation;
        private int mDelta;
        private Paint mOutlinePaint;
        private Paint mPaint = new Paint(1);
        private Drawable mProgress;
        private String mText;
        private float mTextWidth;
        private float mTextXScale;

        public TextSeekBarDrawable(Resources res, String text, boolean labelOnRight) {
            this.mText = text;
            this.mProgress = res.getDrawable(R.drawable.progress_color);
            this.mPaint.setTextSize(A.getDensity() > 2.0f ? 30.0f : 20.0f);
            this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
            this.mOutlinePaint = new Paint(this.mPaint);
            this.mOutlinePaint.setStyle(Style.STROKE);
            this.mOutlinePaint.setStrokeWidth(3.0f);
            this.mOutlinePaint.setColor(Color.parseColor("#ffffff"));
            this.mOutlinePaint.setMaskFilter(new BlurMaskFilter(1.0f, Blur.NORMAL));
            this.mTextWidth = this.mOutlinePaint.measureText(this.mText);
            this.mTextXScale = labelOnRight ? 1.0f : 0.0f;
            this.mAnimation = new ScrollAnimation();
            this.mAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    ColorDialog.this.root.postInvalidateDelayed(0);
                }
            });
        }

        protected void onBoundsChange(Rect bounds) {
            this.mProgress.setBounds(bounds);
        }

        protected boolean onStateChange(int[] state) {
            this.mActive = StateSet.stateSetMatches(ColorDialog.STATE_FOCUSED, state) | StateSet.stateSetMatches(ColorDialog.STATE_PRESSED, state);
            invalidateSelf();
            return false;
        }

        public boolean isStateful() {
            return true;
        }

        protected boolean onLevelChange(int level) {
            if (level < 4000 && this.mDelta <= 0) {
                this.mDelta = 1;
                this.mAnimation.startScrolling(this.mTextXScale, 1.0f);
                scheduleSelf(this, SystemClock.uptimeMillis() + DELAY);
            } else if (level > 6000 && this.mDelta >= 0) {
                this.mDelta = -1;
                this.mAnimation.startScrolling(this.mTextXScale, 0.0f);
                scheduleSelf(this, SystemClock.uptimeMillis() + DELAY);
            }
            return this.mProgress.setLevel(level);
        }

        public void draw(Canvas canvas) {
            int i = 255;
            if (!ColorDialog.locked) {
                int i2;
                this.mProgress.draw(canvas);
                if (this.mAnimation.hasStarted() && !this.mAnimation.hasEnded()) {
                    this.mAnimation.getTransformation(AnimationUtils.currentAnimationTimeMillis(), null);
                    this.mTextXScale = this.mAnimation.getCurrent();
                }
                Rect bounds = getBounds();
                float x = CSS.FLOAT_ADDED + (this.mTextXScale * (((((float) bounds.width()) - this.mTextWidth) - CSS.FLOAT_ADDED) - CSS.FLOAT_ADDED));
                float y = ((float) A.d(7.0f)) + ((((float) bounds.height()) + this.mPaint.getTextSize()) / 2.0f);
                Paint paint = this.mOutlinePaint;
                if (this.mActive) {
                    i2 = 255;
                } else {
                    i2 = 180;
                }
                paint.setAlpha(i2);
                Paint paint2 = this.mPaint;
                if (!this.mActive) {
                    i = 180;
                }
                paint2.setAlpha(i);
                canvas.drawText(this.mText, x, y, this.mOutlinePaint);
                canvas.drawText(this.mText, x, y, this.mPaint);
            }
        }

        public int getOpacity() {
            return -3;
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter cf) {
        }

        public void run() {
            this.mAnimation.getTransformation(AnimationUtils.currentAnimationTimeMillis(), null);
            this.mTextXScale = this.mAnimation.getCurrent();
            if (!this.mAnimation.hasEnded()) {
                scheduleSelf(this, SystemClock.uptimeMillis() + DELAY);
            }
            invalidateSelf();
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public ColorDialog(Context context, String title, boolean useAlpha, int color, OnSaveColor listener) {
        Drawable[] layers;
        super(context, R.style.dialog_fullscreen);
        this.mUseAlpha = useAlpha;
        this.mListener = listener;
        this.mContext = context;
        this.res = context.getResources();
        this.root = LayoutInflater.from(context).inflate(R.layout.color_picker, null);
        setContentView(this.root);
        A.setDialogNightState(this.root);
        this.phTitle = (TextView) this.root.findViewById(R.id.titleB);
        this.phTitle.setText(title);
        this.phExit = this.root.findViewById(R.id.exitB);
        this.phExit.setVisibility(8);
        View preview = this.root.findViewById(R.id.preview);
        this.mPreviewDrawable = new GradientDrawable();
        this.mPreviewDrawable.setCornerRadius(7.0f);
        if (useAlpha) {
            this.mIcon = new IconPreviewDrawable(getContext().getResources());
            new ClipDrawable(this.mPreviewDrawable, 48, 2).setLevel(5000);
            layers = new Drawable[]{topClip, this.mIcon, this.res.getDrawable(R.drawable.color_picker_frame)};
        } else {
            layers = new Drawable[]{this.mPreviewDrawable, this.res.getDrawable(R.drawable.color_picker_frame)};
        }
        preview.setBackgroundDrawable(new LayerDrawable(layers));
        this.cR = (SeekBar) this.root.findViewById(R.id.hue);
        this.cG = (SeekBar) this.root.findViewById(R.id.saturation);
        this.cB = (SeekBar) this.root.findViewById(R.id.value);
        this.cAlpha = (SeekBar) this.root.findViewById(R.id.alpha);
        this.redEt = (EditText) this.root.findViewById(R.id.redEt);
        this.greenEt = (EditText) this.root.findViewById(R.id.greenEt);
        this.blueEt = (EditText) this.root.findViewById(R.id.blueEt);
        this.alphaEt = (EditText) this.root.findViewById(R.id.alphaEt);
        if (A.isHoneycombTablet()) {
            setEtSize(this.redEt);
            setEtSize(this.greenEt);
            setEtSize(this.blueEt);
            setEtSize(this.alphaEt);
        }
        this.redEt.setOnFocusChangeListener(this.etFocusChanged);
        this.greenEt.setOnFocusChangeListener(this.etFocusChanged);
        this.blueEt.setOnFocusChangeListener(this.etFocusChanged);
        this.alphaEt.setOnFocusChangeListener(this.etFocusChanged);
        this.b1 = (Button) this.root.findViewById(R.id.okB);
        this.b2 = (Button) this.root.findViewById(R.id.cancelB);
        this.b1.setOnClickListener(this);
        this.b2.setOnClickListener(this);
        initSavedColors();
        setRGBColor(color == -1 ? -2 : color);
        initColorView2(this.root);
        if (color == -1) {
            new Handler() {
                public void handleMessage(Message msg) {
                    ColorDialog.this.setRGBColor(-1);
                }
            }.sendEmptyMessageDelayed(0, 50);
        }
        A.setAlertDialogWidth(getWindow(), 0.75f, true);
        getWindow().setSoftInputMode(3);
    }

    private void initSavedColors() {
        this.c1 = (ImageView) this.root.findViewById(R.id.c1);
        this.c2 = (ImageView) this.root.findViewById(R.id.c2);
        this.c3 = (ImageView) this.root.findViewById(R.id.c3);
        this.c4 = (ImageView) this.root.findViewById(R.id.c4);
        this.c5 = (ImageView) this.root.findViewById(R.id.c5);
        this.c6 = (ImageView) this.root.findViewById(R.id.c6);
        this.c7 = (ImageView) this.root.findViewById(R.id.c7);
        this.c8 = (ImageView) this.root.findViewById(R.id.c8);
        this.c9 = (ImageView) this.root.findViewById(R.id.c9);
        this.c1.setOnClickListener(this);
        this.c2.setOnClickListener(this);
        this.c3.setOnClickListener(this);
        this.c4.setOnClickListener(this);
        this.c5.setOnClickListener(this);
        this.c6.setOnClickListener(this);
        this.c7.setOnClickListener(this);
        this.c8.setOnClickListener(this);
        this.c9.setOnClickListener(this);
        this.c1.setTag(Integer.valueOf(SupportMenu.CATEGORY_MASK));
        this.c2.setTag(Integer.valueOf(-1));
        this.c3.setTag(Integer.valueOf(InputDeviceCompat.SOURCE_ANY));
        this.c4.setTag(Integer.valueOf(-16711936));
        this.c5.setTag(Integer.valueOf(-7829368));
        this.c6.setTag(Integer.valueOf(-16711681));
        this.c7.setTag(Integer.valueOf(-16776961));
        this.c8.setTag(Integer.valueOf(ViewCompat.MEASURED_STATE_MASK));
        this.c9.setTag(Integer.valueOf(-65281));
        this.c1.setImageDrawable(new ColorDrawable(((Integer) this.c1.getTag()).intValue()));
        this.c2.setImageDrawable(new ColorDrawable(((Integer) this.c2.getTag()).intValue()));
        this.c3.setImageDrawable(new ColorDrawable(((Integer) this.c3.getTag()).intValue()));
        this.c4.setImageDrawable(new ColorDrawable(((Integer) this.c4.getTag()).intValue()));
        this.c5.setImageDrawable(new ColorDrawable(((Integer) this.c5.getTag()).intValue()));
        this.c6.setImageDrawable(new ColorDrawable(((Integer) this.c6.getTag()).intValue()));
        this.c7.setImageDrawable(new ColorDrawable(((Integer) this.c7.getTag()).intValue()));
        this.c8.setImageDrawable(new ColorDrawable(((Integer) this.c8.getTag()).intValue()));
        this.c9.setImageDrawable(new ColorDrawable(((Integer) this.c9.getTag()).intValue()));
    }

    private void setEtSize(EditText et) {
        et.getLayoutParams().width = A.d(60.0f);
    }

    private void setRGBColor(int color) {
        locked = true;
        setupSeekBar(this.cR, this.redEt, "Red", Color.red(color), this.res);
        setupSeekBar(this.cG, this.greenEt, "Green", Color.green(color), this.res);
        setupSeekBar(this.cB, this.blueEt, "Blue", Color.blue(color), this.res);
        if (this.mUseAlpha) {
            setupSeekBar(this.cAlpha, this.alphaEt, "Alpha", (Color.alpha(color) * this.cAlpha.getMax()) / 255, this.res);
        } else {
            this.cAlpha.setVisibility(8);
        }
        updatePreview(color);
        this.mColor = color;
        locked = false;
    }

    protected int getValue(EditText v) {
        int value = Integer.valueOf(v.getText().toString()).intValue();
        if (value < 0) {
            value = 0;
        }
        if (value > 255) {
            return 255;
        }
        return value;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(1024);
    }

    private void setupSeekBar(SeekBar seekBar, EditText et, String text, int value, Resources res) {
        if (this.mColor == -1 || !locked) {
            seekBar.setProgressDrawable(new TextSeekBarDrawable(res, text, value < seekBar.getMax() / 2));
        }
        seekBar.setProgress(value);
        et.setText("" + value);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private void update() {
        int red = this.cR.getProgress();
        int green = this.cG.getProgress();
        int blue = this.cB.getProgress();
        int alpha = this.cAlpha.getProgress();
        this.redEt.setText("" + red);
        this.greenEt.setText("" + green);
        this.blueEt.setText("" + blue);
        this.alphaEt.setText("" + alpha);
        if (this.mUseAlpha) {
            this.mColor = Color.argb(alpha, red, green, blue);
        } else {
            this.mColor = Color.rgb(red, green, blue);
        }
        updatePreview(this.mColor);
        this.root.postInvalidateDelayed(50);
    }

    private void updatePreview(int color) {
        if (this.mUseAlpha) {
            this.mIcon.setColorFilter(color, Mode.SRC_ATOP);
            color |= ViewCompat.MEASURED_STATE_MASK;
        }
        this.mPreviewDrawable.setColor(color);
        this.mPreviewDrawable.invalidateSelf();
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            update();
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void initColorView2(View root) {
        this.colorLay = (LinearLayout) root.findViewById(R.id.colorLay);
        this.cView = new ColorPickerView(this.mContext, this.l, this.mColor);
        LayoutParams params1 = new LayoutParams(-2, -2);
        params1.gravity = 17;
        this.colorLay.addView(this.cView, params1);
    }

    public void onClick(View v) {
        if (v == this.b1) {
            try {
                int alpha = getValue(this.alphaEt);
                int red = getValue(this.redEt);
                int green = getValue(this.greenEt);
                int blue = getValue(this.blueEt);
                if (this.mUseAlpha) {
                    this.mColor = Color.argb(alpha, red, green, blue);
                } else {
                    this.mColor = Color.rgb(red, green, blue);
                }
                this.mListener.getColor(this.mColor);
            } catch (Exception e) {
                A.error(e);
            }
            cancel();
        }
        if (v == this.b2) {
            cancel();
        }
        if (v == this.c1 || v == this.c2 || v == this.c3 || v == this.c4 || v == this.c5 || v == this.c6 || v == this.c7 || v == this.c8 || v == this.c9) {
            int c = ((Integer) v.getTag()).intValue();
            setRGBColor(Color.argb(Color.alpha(this.mColor), Color.red(c), Color.green(c), Color.blue(c)));
        }
        if (v == this.phExit) {
            cancel();
        }
    }
}