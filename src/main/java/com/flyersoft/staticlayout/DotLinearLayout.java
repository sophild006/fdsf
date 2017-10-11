package com.flyersoft.staticlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class DotLinearLayout extends LinearLayout {
    private int dBottom;
    private int dLeft = 0;
    private int dRight;
    private int dTop = 0;

    public DotLinearLayout(Context context) {
        super(context);
    }

    public DotLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (left != 0 || top != 0) {
            this.dLeft = left;
            this.dTop = top;
            this.dRight = right;
            this.dBottom = bottom;
            super.onLayout(changed, left, top, right, bottom);
        } else if (this.dLeft != 0 || this.dTop != 0) {
            layout(this.dLeft, this.dTop, this.dRight, this.dBottom);
        }
    }
}