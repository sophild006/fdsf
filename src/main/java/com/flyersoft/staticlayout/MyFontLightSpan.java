package com.flyersoft.staticlayout;

import android.graphics.Typeface;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

public class MyFontLightSpan extends CharacterStyle implements UpdateAppearance, ParcelableSpan {
    public MyFontLightSpan(Parcel src) {
    }

    public int getSpanTypeId() {
        return 115;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    public void updateDrawState(TextPaint paint) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }
        if ((oldStyle & 1) != 0) {
            Typeface tf;
            int want = oldStyle & 2;
            if (old == null) {
                tf = Typeface.defaultFromStyle(want);
            } else {
                tf = Typeface.create(old, want);
            }
            int fake = want & (tf.getStyle() ^ -1);
            if ((fake & 1) != 0) {
                paint.setFakeBoldText(true);
            }
            if ((fake & 2) != 0) {
                paint.setTextSkewX(-0.25f);
            }
            paint.setTypeface(tf);
        }
    }

    public int getSpanTypeIdInternal() {
        return getSpanTypeId();
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
        writeToParcel(dest, flags);
    }
}