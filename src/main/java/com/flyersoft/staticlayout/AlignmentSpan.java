package com.flyersoft.staticlayout;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.style.ParagraphStyle;
import com.flyersoft.staticlayout.MyLayout.Alignment;

public interface AlignmentSpan extends ParagraphStyle {

    public static class Standard implements AlignmentSpan, ParcelableSpan {
        private final Alignment mAlignment;

        public Standard(Alignment align) {
            this.mAlignment = align;
        }

        public Standard(Parcel src) {
            this.mAlignment = Alignment.valueOf(src.readString());
        }

        public int getSpanTypeId() {
            return 1;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mAlignment.name());
        }

        public Alignment getAlignment() {
            return this.mAlignment;
        }

        public int getSpanTypeIdInternal() {
            return getSpanTypeId();
        }

        public void writeToParcelInternal(Parcel dest, int flags) {
            writeToParcel(dest, flags);
        }
    }

    Alignment getAlignment();
}