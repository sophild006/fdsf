package com.flyersoft.staticlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DotImageView extends ImageView {
    private int dBottom;
    private int dLeft = 0;
    private int dRight;
    private int dTop = 0;

    public DotImageView(Context context) {
        super(context);
    }

    public DotImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DotImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (left != 0 || top != 0) {
            this.dLeft = left;
            this.dTop = top;
            this.dRight = right;
            this.dBottom = bottom;
        } else if (this.dLeft != 0 || this.dTop != 0) {
            layout(this.dLeft, this.dTop, this.dRight, this.dBottom);
        }
    }
}