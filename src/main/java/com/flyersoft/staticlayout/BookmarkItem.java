package com.flyersoft.staticlayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;
import com.flyersoft.books.A;

public class BookmarkItem extends TextView {
    public int s_end;
    public int s_start;
    public boolean squiggly;

    public BookmarkItem(Context context) {
        super(context);
    }

    public BookmarkItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookmarkItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.squiggly) {
            int i = 0;
            while (i < getLineCount()) {
                try {
                    int baseline = getLineBounds(i, null);
                    int j1 = getLayout().getLineStart(i);
                    int j2 = getLayout().getLineEnd(i);
                    if (i < getLineCount() - 1) {
                        j2--;
                    }
                    if (j1 <= this.s_end) {
                        if (i == 0) {
                            j1 = this.s_start;
                        }
                        if (j2 > this.s_end) {
                            j2 = this.s_end;
                        }
                        float hori = (float) (A.d(1.0f) + baseline);
                        float l1 = i == 0 ? Layout.getDesiredWidth(getText(), 0, j1, getPaint()) : 0.0f;
                        float l2 = l1 + Layout.getDesiredWidth(getText(), j1, j2, getPaint());
                        l1 += (float) getPaddingLeft();
                        l2 += (float) getPaddingLeft();
                        Paint p = new Paint(getPaint());
                        p.setStrokeWidth((float) A.d(1.0f));
                        A.drawSquiggly(canvas, l1, hori, l2, hori, p, (float) A.d(2.8f));
                    }
                    i++;
                } catch (Exception e) {
                    A.error(e);
                    return;
                }
            }
        }
    }
}