package com.flyersoft.staticlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.flyersoft.books.A;
import com.flyersoft.books.T;
import com.flyersoft.moonreader.ActivityTxt;
import com.flyersoft.moonreader.R;

public class HighlightLay extends DotLinearLayout {
    public Bitmap bm;

    public HighlightLay(Context context) {
        super(context);
    }

    public HighlightLay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void drawMagnifier(MotionEvent event) {
        T.recycle(this.bm);
        this.bm = null;
        ActivityTxt act = ActivityTxt.selfPref;
        if (act != null) {
            int x = (int) act.getTouchX(event);
            int y = (int) event.getY();
            Bitmap source =  null;
            if (source != null) {
                int h1;
                int w2 = (getWidth() - getPaddingLeft()) - getPaddingRight();
                int h2 = (getHeight() - getPaddingTop()) - getPaddingBottom();
                int w1 = A.showColorTemplate ? w2 / 2 : (w2 * 2) / 3;
                if (A.showColorTemplate) {
                    h1 = h2 / 2;
                } else {
                    h1 = (h2 * 2) / 3;
                }
                Rect r1 = new Rect(x - (w1 / 2), y - (h1 / 2), (w1 / 2) + x, (h1 / 2) + y);
                Rect r2 = new Rect(0, 0, w2, h2);
                this.bm = Bitmap.createBitmap(w2, h2, Config.ARGB_8888);
                Canvas canvas = new Canvas(this.bm);
                A.setBackgroundImage(canvas);
                canvas.drawBitmap(source, r1, r2, null);
                int d = A.d(1.0f);
                Paint p = new Paint();
                p.setStyle(Style.STROKE);
                p.setStrokeWidth((float) d);
                p.setColor(A.isWhiteFont(A.fontColor) ? A.fontColor : 858993459);
                canvas.drawRect(r2, p);
                p.setColor(288568115);
                canvas.drawRect(new Rect(r2.left + d, r2.top + d, r2.right - d, r2.bottom - d), p);
                postInvalidate();
            }
        }
    }

    public void stopMagnifier() {
        T.recycle(this.bm);
        this.bm = null;
        System.gc();
        postInvalidate();
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (T.isRecycled(this.bm)) {
            super.dispatchDraw(canvas);
            if (A.isNightState()) {
                Paint p = new Paint();
                p.setStyle(Style.STROKE);
                p.setStrokeWidth((float) A.d(1.0f));
                p.setColor(getResources().getColor(R.color.colorPrimary));
                int w = getWidth();
                int h = getHeight();
                int l = getPaddingLeft() - A.d(1.0f);
                int r = getPaddingRight() - A.d(1.0f);
                int t = getPaddingTop() - A.d(1.0f);
                int b = getPaddingBottom() - A.d(1.0f);
                canvas.drawLine((float) l, (float) t, (float) (w - r), (float) t, p);
                Canvas canvas2 = canvas;
                canvas2.drawLine((float) l, (float) t, (float) l, (float) (h - b), p);
                canvas2 = canvas;
                canvas2.drawLine((float) (w - r), (float) t, (float) (w - r), (float) (h - b), p);
                return;
            }
            return;
        }
        canvas.drawBitmap(this.bm, (float) getPaddingLeft(), (float) getPaddingTop(), null);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}