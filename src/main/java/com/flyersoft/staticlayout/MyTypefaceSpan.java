package com.flyersoft.staticlayout;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import com.flyersoft.books.A;

public class MyTypefaceSpan extends MetricAffectingSpan implements ParcelableSpan {
    private final String mFamily;

    public MyTypefaceSpan(String family) {
        this.mFamily = family;
    }

    public MyTypefaceSpan(Parcel src) {
        this.mFamily = src.readString();
    }

    public int getSpanTypeId() {
        return 111;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mFamily);
    }

    public String getFamily() {
        return this.mFamily;
    }

    public void updateDrawState(TextPaint ds) {
        apply(ds, this.mFamily);
    }

    public void updateMeasureState(TextPaint paint) {
        apply(paint, this.mFamily);
    }

    private static void apply(Paint paint, String family) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }
        Typeface tf = A.getTypeFace2(family, oldStyle);
        int fake = oldStyle & (tf.getStyle() ^ -1);
        if ((fake & 1) != 0) {
            paint.setFakeBoldText(true);
        }
        if ((fake & 2) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(tf);
    }

    public int getSpanTypeIdInternal() {
        return getSpanTypeId();
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
        writeToParcel(dest, flags);
    }
}