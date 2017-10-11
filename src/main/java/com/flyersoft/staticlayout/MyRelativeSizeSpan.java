package com.flyersoft.staticlayout;

import android.text.style.RelativeSizeSpan;

public class MyRelativeSizeSpan extends RelativeSizeSpan {
    public float fontSize;
    public boolean inherited = true;

    public MyRelativeSizeSpan(float proportion) {
        super(proportion);
        this.fontSize = proportion;
    }
}