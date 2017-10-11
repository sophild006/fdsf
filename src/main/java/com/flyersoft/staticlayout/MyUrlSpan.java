package com.flyersoft.staticlayout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import com.flyersoft.books.A;
import com.flyersoft.components.CSS;

public class MyUrlSpan extends ClickableSpan implements ParcelableSpan {
    public boolean clicked;
    public Integer link_color;
    public boolean link_no_underline;
    public Integer link_visited_color;
    private final String mURL;

    public MyUrlSpan(String url) {
        this.mURL = url;
    }

    public MyUrlSpan(Parcel src) {
        this.mURL = src.readString();
    }

    public int getSpanTypeId() {
        return 110;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mURL);
    }

    public String getURL() {
        return this.mURL;
    }

    public void onClick(View widget) {
        try {
            Uri uri = Uri.parse(getURL());
            Context context = widget.getContext();
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            intent.putExtra("com.android.browser.application_id", context.getPackageName());
            context.startActivity(intent);
        } catch (Exception e) {
            A.error(e);
        }
    }

    public void updateDrawState(TextPaint ds) {
        int c;
        if (!this.clicked) {
            c = this.link_color != null ? this.link_color.intValue() : CSS.LINK_COLOR;
        } else if (this.link_visited_color != null) {
            c = this.link_visited_color.intValue();
        } else if (this.link_color != null) {
            c = A.getAlphaColor(this.link_color.intValue(), -100);
        } else {
            c = A.getAlphaColor(this.link_color.intValue(), -100);
        }
        ds.setColor(c);
        ds.setUnderlineText(!this.link_no_underline);
    }

    public int getSpanTypeIdInternal() {
        return getSpanTypeId();
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
        writeToParcel(dest, flags);
    }
}