package com.flyersoft.staticlayout;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.AndroidCharacter;
import android.text.Annotation;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan.Standard;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.MetricAffectingSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ReplacementSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.Printer;
import java.util.Iterator;
import java.util.regex.Pattern;

public class TextUtils {
    public static final int ABSOLUTE_SIZE_SPAN = 16;
    public static final int ALIGNMENT_SPAN = 1;
    public static final int ANNOTATION = 18;
    public static final int BACKGROUND_COLOR_SPAN = 12;
    public static final int BULLET_SPAN = 8;
    public static final int CAP_MODE_CHARACTERS = 4096;
    public static final int CAP_MODE_SENTENCES = 16384;
    public static final int CAP_MODE_WORDS = 8192;
    public static final Creator<CharSequence> CHAR_SEQUENCE_CREATOR = new Creator<CharSequence>() {
        public CharSequence createFromParcel(Parcel p) {
            if (p.readInt() == 1) {
                return p.readString();
            }
            SpannableString sp = new SpannableString(p.readString());
            while (true) {
                int kind = p.readInt();
                if (kind == 0) {
                    return sp;
                }
                switch (kind) {
                    case 1:
                        TextUtils.readSpan(p, sp, new Standard(p));
                        break;
                    case 2:
                        TextUtils.readSpan(p, sp, new ForegroundColorSpan(p));
                        break;
                    case 3:
                        TextUtils.readSpan(p, sp, new RelativeSizeSpan(p));
                        break;
                    case 4:
                        TextUtils.readSpan(p, sp, new ScaleXSpan(p));
                        break;
                    case 5:
                        TextUtils.readSpan(p, sp, new StrikethroughSpan(p));
                        break;
                    case 6:
                        TextUtils.readSpan(p, sp, new UnderlineSpan(p));
                        break;
                    case 7:
                        TextUtils.readSpan(p, sp, new StyleSpan(p));
                        break;
                    case 8:
                        TextUtils.readSpan(p, sp, new BulletSpan(p));
                        break;
                    case 9:
                        TextUtils.readSpan(p, sp, new QuoteSpan(p));
                        break;
                    case 10:
                        TextUtils.readSpan(p, sp, new LeadingMarginSpan.Standard(p));
                        break;
                    case 11:
                        TextUtils.readSpan(p, sp, new MyUrlSpan(p));
                        break;
                    case 12:
                        TextUtils.readSpan(p, sp, new BackgroundColorSpan(p));
                        break;
                    case 13:
                        TextUtils.readSpan(p, sp, new TypefaceSpan(p));
                        break;
                    case 14:
                        TextUtils.readSpan(p, sp, new SuperscriptSpan(p));
                        break;
                    case 15:
                        TextUtils.readSpan(p, sp, new SubscriptSpan(p));
                        break;
                    case 16:
                        TextUtils.readSpan(p, sp, new AbsoluteSizeSpan(p));
                        break;
                    case 17:
                        TextUtils.readSpan(p, sp, new TextAppearanceSpan(p));
                        break;
                    case 18:
                        TextUtils.readSpan(p, sp, new Annotation(p));
                        break;
                    default:
                        throw new RuntimeException("bogus span encoding " + kind);
                }
            }
        }

        public CharSequence[] newArray(int size) {
            return new CharSequence[size];
        }
    };
    private static String[] EMPTY_STRING_ARRAY = new String[0];
    public static final int FOREGROUND_COLOR_SPAN = 2;
    public static final int LEADING_MARGIN_SPAN = 10;
    public static final int QUOTE_SPAN = 9;
    public static final int RELATIVE_SIZE_SPAN = 3;
    public static final int SCALE_X_SPAN = 4;
    public static final int STRIKETHROUGH_SPAN = 5;
    public static final int STYLE_SPAN = 7;
    public static final int SUBSCRIPT_SPAN = 15;
    public static final int SUPERSCRIPT_SPAN = 14;
    public static final int TEXT_APPEARANCE_SPAN = 17;
    public static final int TYPEFACE_SPAN = 13;
    public static final int UNDERLINE_SPAN = 6;
    public static final int URL_SPAN = 11;
    private static String sEllipsis = null;
    private static Object sLock = new Object();
    private static char[] sTemp = null;

    public interface EllipsizeCallback {
        void ellipsized(int i, int i2);
    }

    private static class Reverser implements CharSequence, GetChars {
        private int mEnd;
        private CharSequence mSource;
        private int mStart;

        public Reverser(CharSequence source, int start, int end) {
            this.mSource = source;
            this.mStart = start;
            this.mEnd = end;
        }

        public int length() {
            return this.mEnd - this.mStart;
        }

        public CharSequence subSequence(int start, int end) {
            char[] buf = new char[(end - start)];
            getChars(start, end, buf, 0);
            return new String(buf);
        }

        public String toString() {
            return subSequence(0, length()).toString();
        }

        public char charAt(int off) {
            return AndroidCharacter.getMirror(this.mSource.charAt((this.mEnd - 1) - off));
        }

        public void getChars(int start, int end, char[] dest, int destoff) {
            TextUtils.getChars(this.mSource, this.mStart + start, this.mStart + end, dest, destoff);
            AndroidCharacter.mirror(dest, 0, end - start);
            int len = end - start;
            int n = (end - start) / 2;
            for (int i = 0; i < n; i++) {
                char tmp = dest[destoff + i];
                dest[destoff + i] = dest[((destoff + len) - i) - 1];
                dest[((destoff + len) - i) - 1] = tmp;
            }
        }
    }

    public interface StringSplitter extends Iterable<String> {
        void setString(String str);
    }

    public static class SimpleStringSplitter implements StringSplitter, Iterator<String> {
        private char mDelimiter;
        private int mLength;
        private int mPosition;
        private String mString;

        public SimpleStringSplitter(char delimiter) {
            this.mDelimiter = delimiter;
        }

        public void setString(String string) {
            this.mString = string;
            this.mPosition = 0;
            this.mLength = this.mString.length();
        }

        public Iterator<String> iterator() {
            return this;
        }

        public boolean hasNext() {
            return this.mPosition < this.mLength;
        }

        public String next() {
            int end = this.mString.indexOf(this.mDelimiter, this.mPosition);
            if (end == -1) {
                end = this.mLength;
            }
            String nextString = this.mString.substring(this.mPosition, end);
            this.mPosition = end + 1;
            return nextString;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public enum TruncateAt {
        START,
        MIDDLE,
        END,
        MARQUEE
    }

    private TextUtils() {
    }

    public static void getChars(CharSequence s, int start, int end, char[] dest, int destoff) {
        Class c = s.getClass();
        if (c == String.class) {
            ((String) s).getChars(start, end, dest, destoff);
        } else if (c == StringBuffer.class) {
            ((StringBuffer) s).getChars(start, end, dest, destoff);
        } else if (c == StringBuilder.class) {
            ((StringBuilder) s).getChars(start, end, dest, destoff);
        } else if (s instanceof GetChars) {
            ((GetChars) s).getChars(start, end, dest, destoff);
        } else {
            int i = start;
            int destoff2 = destoff;
            while (i < end) {
                destoff = destoff2 + 1;
                dest[destoff2] = s.charAt(i);
                i++;
                destoff2 = destoff;
            }
            destoff = destoff2;
        }
    }

    public static int indexOf(CharSequence s, char ch) {
        return indexOf(s, ch, 0);
    }

    public static int indexOf(CharSequence s, char ch, int start) {
        if (s.getClass() == String.class) {
            return ((String) s).indexOf(ch, start);
        }
        return indexOf(s, ch, start, s.length());
    }

    public static int indexOf(CharSequence s, char ch, int start, int end) {
        Class c = s.getClass();
        int i;
        if ((s instanceof GetChars) || c == StringBuffer.class || c == StringBuilder.class || c == String.class) {
            char[] temp = obtain(500);
            while (start < end) {
                int segend = start + 500;
                if (segend > end) {
                    segend = end;
                }
                getChars(s, start, segend, temp, 0);
                int count = segend - start;
                for (i = 0; i < count; i++) {
                    if (temp[i] == ch) {
                        recycle(temp);
                        return i + start;
                    }
                }
                start = segend;
            }
            recycle(temp);
            return -1;
        }
        for (i = start; i < end; i++) {
            if (s.charAt(i) == ch) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(CharSequence s, char ch) {
        return lastIndexOf(s, ch, s.length() - 1);
    }

    public static int lastIndexOf(CharSequence s, char ch, int last) {
        if (s.getClass() == String.class) {
            return ((String) s).lastIndexOf(ch, last);
        }
        return lastIndexOf(s, ch, 0, last);
    }

    public static int lastIndexOf(CharSequence s, char ch, int start, int last) {
        if (last < 0) {
            return -1;
        }
        if (last >= s.length()) {
            last = s.length() - 1;
        }
        int end = last + 1;
        Class c = s.getClass();
        int i;
        if ((s instanceof GetChars) || c == StringBuffer.class || c == StringBuilder.class || c == String.class) {
            char[] temp = obtain(500);
            while (start < end) {
                int segstart = end - 500;
                if (segstart < start) {
                    segstart = start;
                }
                getChars(s, segstart, end, temp, 0);
                for (i = (end - segstart) - 1; i >= 0; i--) {
                    if (temp[i] == ch) {
                        recycle(temp);
                        return i + segstart;
                    }
                }
                end = segstart;
            }
            recycle(temp);
            return -1;
        }
        for (i = end - 1; i >= start; i--) {
            if (s.charAt(i) == ch) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(CharSequence s, CharSequence needle) {
        return indexOf(s, needle, 0, s.length());
    }

    public static int indexOf(CharSequence s, CharSequence needle, int start) {
        return indexOf(s, needle, start, s.length());
    }

    public static int indexOf(CharSequence s, CharSequence needle, int start, int end) {
        int nlen = needle.length();
        if (nlen == 0) {
            return start;
        }
        char c = needle.charAt(0);
        while (true) {
            start = indexOf(s, c, start);
            if (start > end - nlen || start < 0) {
                return -1;
            }
            if (regionMatches(s, start, needle, 0, nlen)) {
                return start;
            }
            start++;
        }
    }

    public static boolean regionMatches(CharSequence one, int toffset, CharSequence two, int ooffset, int len) {
        char[] temp = obtain(len * 2);
        getChars(one, toffset, toffset + len, temp, 0);
        getChars(two, ooffset, ooffset + len, temp, len);
        boolean match = true;
        for (int i = 0; i < len; i++) {
            if (temp[i] != temp[i + len]) {
                match = false;
                break;
            }
        }
        recycle(temp);
        return match;
    }

    public static String substring(CharSequence source, int start, int end) {
        if (source instanceof String) {
            return ((String) source).substring(start, end);
        }
        if (source instanceof StringBuilder) {
            return ((StringBuilder) source).substring(start, end);
        }
        if (source instanceof StringBuffer) {
            return ((StringBuffer) source).substring(start, end);
        }
        char[] temp = obtain(end - start);
        getChars(source, start, end, temp, 0);
        String ret = new String(temp, 0, end - start);
        recycle(temp);
        return ret;
    }

    public static String join(CharSequence delimiter, Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    public static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    public static String[] split(String text, String expression) {
        if (text.length() == 0) {
            return EMPTY_STRING_ARRAY;
        }
        return text.split(expression, -1);
    }

    public static String[] split(String text, Pattern pattern) {
        if (text.length() == 0) {
            return EMPTY_STRING_ARRAY;
        }
        return pattern.split(text, -1);
    }

    public static CharSequence stringOrSpannedString(CharSequence source) {
        if (source == null) {
            return null;
        }
        if (source instanceof SpannedString) {
            return source;
        }
        if (source instanceof Spanned) {
            return new SpannedString(source);
        }
        return source.toString();
    }

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static int getTrimmedLength(CharSequence s) {
        int len = s.length();
        int start = 0;
        while (start < len && s.charAt(start) <= ' ') {
            start++;
        }
        int end = len;
        while (end > start && s.charAt(end - 1) <= ' ') {
            end--;
        }
        return end - start;
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        if (!(a == null || b == null)) {
            int length = a.length();
            if (length == b.length()) {
                if ((a instanceof String) && (b instanceof String)) {
                    return a.equals(b);
                }
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static CharSequence getReverse(CharSequence source, int start, int end) {
        return new Reverser(source, start, end);
    }

    public static void writeToParcel(CharSequence cs, Parcel p, int parcelableFlags) {
        if (cs instanceof Spanned) {
            p.writeInt(0);
            p.writeString(cs.toString());
            Spanned sp = (Spanned) cs;
            Object[] os = sp.getSpans(0, cs.length(), Object.class);
            for (int i = 0; i < os.length; i++) {
                Object o = os[i];
                CharacterStyle characterStyle = os[i];
                if (characterStyle instanceof CharacterStyle) {
                    characterStyle = characterStyle.getUnderlying();
                }
                if (characterStyle instanceof ParcelableSpan) {
                    ParcelableSpan ps = (ParcelableSpan) characterStyle;
                    p.writeInt(ps.getSpanTypeId());
                    ps.writeToParcel(p, parcelableFlags);
                    writeWhere(p, sp, o);
                }
            }
            p.writeInt(0);
            return;
        }
        p.writeInt(1);
        if (cs != null) {
            p.writeString(cs.toString());
        } else {
            p.writeString(null);
        }
    }

    private static void writeWhere(Parcel p, Spanned sp, Object o) {
        p.writeInt(sp.getSpanStart(o));
        p.writeInt(sp.getSpanEnd(o));
        p.writeInt(sp.getSpanFlags(o));
    }

    public static void dumpSpans(CharSequence cs, Printer printer, String prefix) {
        if (cs instanceof Spanned) {
            Spanned sp = (Spanned) cs;
            Object[] os = sp.getSpans(0, cs.length(), Object.class);
            for (Object o : os) {
                printer.println(prefix + cs.subSequence(sp.getSpanStart(o), sp.getSpanEnd(o)) + ": " + Integer.toHexString(System.identityHashCode(o)) + " " + o.getClass().getCanonicalName() + " (" + sp.getSpanStart(o) + "-" + sp.getSpanEnd(o) + ") fl=#" + sp.getSpanFlags(o));
            }
            return;
        }
        printer.println(prefix + cs + ": (no spans)");
    }

    public static CharSequence replace(CharSequence template, String[] sources, CharSequence[] destinations) {
        int i;
        SpannableStringBuilder tb = new SpannableStringBuilder(template);
        for (i = 0; i < sources.length; i++) {
            int where = indexOf(tb, sources[i]);
            if (where >= 0) {
                tb.setSpan(sources[i], where, sources[i].length() + where, 33);
            }
        }
        for (i = 0; i < sources.length; i++) {
            int start = tb.getSpanStart(sources[i]);
            int end = tb.getSpanEnd(sources[i]);
            if (start >= 0) {
                tb.replace(start, end, destinations[i]);
            }
        }
        return tb;
    }

    public static CharSequence expandTemplate(CharSequence template, CharSequence... values) {
        if (values.length > 9) {
            throw new IllegalArgumentException("max of 9 values are supported");
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(template);
        int i = 0;
        while (i < ssb.length()) {
            try {
                if (ssb.charAt(i) == '^') {
                    char next = ssb.charAt(i + 1);
                    if (next == '^') {
                        ssb.delete(i + 1, i + 2);
                        i++;
                    } else if (Character.isDigit(next)) {
                        int which = Character.getNumericValue(next) - 1;
                        if (which < 0) {
                            throw new IllegalArgumentException("template requests value ^" + (which + 1));
                        } else if (which >= values.length) {
                            throw new IllegalArgumentException("template requests value ^" + (which + 1) + "; only " + values.length + " provided");
                        } else {
                            ssb.replace(i, i + 2, values[which]);
                            i += values[which].length();
                        }
                    }
                }
                i++;
            } catch (IndexOutOfBoundsException e) {
            }
        }
        return ssb;
    }

    public static int getOffsetBefore(CharSequence text, int offset) {
        if (offset == 0 || offset == 1) {
            return 0;
        }
        char c = text.charAt(offset - 1);
        if (c < '�' || c > '�') {
            offset--;
        } else {
            char c1 = text.charAt(offset - 2);
            if (c1 < '�' || c1 > '�') {
                offset--;
            } else {
                offset -= 2;
            }
        }
        if (text instanceof Spanned) {
            ReplacementSpan[] spans = (ReplacementSpan[]) ((Spanned) text).getSpans(offset, offset, ReplacementSpan.class);
            for (int i = 0; i < spans.length; i++) {
                int start = ((Spanned) text).getSpanStart(spans[i]);
                int end = ((Spanned) text).getSpanEnd(spans[i]);
                if (start < offset && end > offset) {
                    offset = start;
                }
            }
        }
        return offset;
    }

    public static int getOffsetAfter(CharSequence text, int offset) {
        if (text == null) {
            return offset;
        }
        int len = text.length();
        if (offset == len || offset == len - 1) {
            return len;
        }
        char c = text.charAt(offset);
        if (c < '�' || c > '�') {
            offset++;
        } else {
            char c1 = text.charAt(offset + 1);
            if (c1 < '�' || c1 > '�') {
                offset++;
            } else {
                offset += 2;
            }
        }
        if (text instanceof Spanned) {
            ReplacementSpan[] spans = (ReplacementSpan[]) ((Spanned) text).getSpans(offset, offset, ReplacementSpan.class);
            for (int i = 0; i < spans.length; i++) {
                int start = ((Spanned) text).getSpanStart(spans[i]);
                int end = ((Spanned) text).getSpanEnd(spans[i]);
                if (start < offset && end > offset) {
                    offset = end;
                }
            }
        }
        return offset;
    }

    private static void readSpan(Parcel p, Spannable sp, Object o) {
        sp.setSpan(o, p.readInt(), p.readInt(), p.readInt());
    }

    public static void copySpansFrom(Spanned source, int start, int end, Class kind, Spannable dest, int destoff) {
        if (kind == null) {
            kind = Object.class;
        }
        Object[] spans = source.getSpans(start, end, kind);
        for (int i = 0; i < spans.length; i++) {
            int st = source.getSpanStart(spans[i]);
            int en = source.getSpanEnd(spans[i]);
            int fl = source.getSpanFlags(spans[i]);
            if (st < start) {
                st = start;
            }
            if (en > end) {
                en = end;
            }
            dest.setSpan(spans[i], (st - start) + destoff, (en - start) + destoff, fl);
        }
    }

    public static CharSequence ellipsize(CharSequence text, TextPaint p, float avail, TruncateAt where) {
        return ellipsize(text, p, avail, where, false, null);
    }

    public static CharSequence ellipsize(CharSequence text, TextPaint p, float avail, TruncateAt where, boolean preserveLength, EllipsizeCallback callback) {
        if (sEllipsis == null) {
            sEllipsis = "...";
        }
        int len = text.length();
        int i;
        float ellipsiswid;
        char[] buf;
        int i2;
        int left;
        if (text instanceof Spanned) {
            float[] wid = new float[(len * 2)];
            TextPaint temppaint = new TextPaint();
            Spanned sp = (Spanned) text;
            i = 0;
            while (i < len) {
                int next = sp.nextSpanTransition(i, len, MetricAffectingSpan.class);
                Styled.getTextWidths(p, temppaint, sp, i, next, wid, null);
                System.arraycopy(wid, 0, wid, len + i, next - i);
                i = next;
            }
            float sum = 0.0f;
            for (i = 0; i < len; i++) {
                sum += wid[len + i];
            }
            if (sum <= avail) {
                if (callback != null) {
                    callback.ellipsized(0, 0);
                }
                return text;
            }
            ellipsiswid = p.measureText(sEllipsis);
            CharSequence spannableString;
            if (ellipsiswid > avail) {
                if (callback != null) {
                    callback.ellipsized(0, len);
                }
                if (!preserveLength) {
                    return "";
                }
                buf = obtain(len);
                for (i = 0; i < len; i++) {
                    buf[i] = '﻿';
                }
                spannableString = new SpannableString(new String(buf, 0, len));
                recycle(buf);
                copySpansFrom(sp, 0, len, Object.class, spannableString, 0);
                return spannableString;
            } else if (where == TruncateAt.START) {
                sum = 0.0f;
                i = len;
                while (i >= 0) {
                    w = wid[(len + i) - 1];
                    if ((w + sum) + ellipsiswid > avail) {
                        break;
                    }
                    sum += w;
                    i--;
                }
                if (callback != null) {
                    callback.ellipsized(0, i);
                }
                if (preserveLength) {
                    spannableString = new SpannableString(blank(text, 0, i));
                    copySpansFrom(sp, 0, len, Object.class, spannableString, 0);
                    return spannableString;
                }
                r0 = new SpannableStringBuilder(sEllipsis);
                r0.insert(1, text, i, len);
                return r0;
            } else if (where == TruncateAt.END) {
                sum = 0.0f;
                i = 0;
                while (i < len) {
                    w = wid[len + i];
                    if ((w + sum) + ellipsiswid > avail) {
                        break;
                    }
                    sum += w;
                    i++;
                }
                if (callback != null) {
                    callback.ellipsized(i, len);
                }
                if (preserveLength) {
                    spannableString = new SpannableString(blank(text, i, len));
                    copySpansFrom(sp, 0, len, Object.class, spannableString, 0);
                    return spannableString;
                }
                r0 = new SpannableStringBuilder(sEllipsis);
                r0.insert(0, text, 0, i);
                return r0;
            } else {
                float lsum = 0.0f;
                float rsum = 0.0f;
                i2 = len;
                float ravail = (avail - ellipsiswid) / 2.0f;
                i2 = len;
                while (i2 >= 0) {
                    w = wid[(len + i2) - 1];
                    if (w + rsum > ravail) {
                        break;
                    }
                    rsum += w;
                    i2--;
                }
                float lavail = (avail - ellipsiswid) - rsum;
                left = 0;
                while (left < i2) {
                    w = wid[len + left];
                    if (w + lsum > lavail) {
                        break;
                    }
                    lsum += w;
                    left++;
                }
                if (callback != null) {
                    callback.ellipsized(left, i2);
                }
                if (preserveLength) {
                    spannableString = new SpannableString(blank(text, left, i2));
                    copySpansFrom(sp, 0, len, Object.class, spannableString, 0);
                    return spannableString;
                }
                r0 = new SpannableStringBuilder(sEllipsis);
                r0.insert(0, text, 0, left);
                r0.insert(r0.length(), text, i2, len);
                return r0;
            }
        } else if (p.measureText(text, 0, len) <= avail) {
            if (callback != null) {
                callback.ellipsized(0, 0);
            }
            return text;
        } else {
            ellipsiswid = p.measureText(sEllipsis);
            if (ellipsiswid > avail) {
                if (callback != null) {
                    callback.ellipsized(0, len);
                }
                if (!preserveLength) {
                    return "";
                }
                buf = obtain(len);
                for (i = 0; i < len; i++) {
                    buf[i] = '﻿';
                }
                String str = new String(buf, 0, len);
                recycle(buf);
                return str;
            } else if (where == TruncateAt.START) {
                fit = p.breakText(text, 0, len, false, avail - ellipsiswid, null);
                if (callback != null) {
                    callback.ellipsized(0, len - fit);
                }
                if (!preserveLength) {
                    return sEllipsis + text.toString().substring(len - fit, len);
                }
                return blank(text, 0, len - fit);
            } else if (where == TruncateAt.END) {
                fit = p.breakText(text, 0, len, true, avail - ellipsiswid, null);
                if (callback != null) {
                    callback.ellipsized(fit, len);
                }
                if (preserveLength) {
                    return blank(text, fit, len);
                }
                return text.toString().substring(0, fit) + sEllipsis;
            } else {
                i2 = p.breakText(text, 0, len, false, (avail - ellipsiswid) / 2.0f, null);
                TextPaint textPaint = p;
                CharSequence charSequence = text;
                left = textPaint.breakText(charSequence, 0, len - i2, true, (avail - ellipsiswid) - p.measureText(text, len - i2, len), null);
                if (callback != null) {
                    callback.ellipsized(left, len - i2);
                }
                if (preserveLength) {
                    return blank(text, left, len - i2);
                }
                String s = text.toString();
                return s.substring(0, left) + sEllipsis + s.substring(len - i2, len);
            }
        }
    }

    private static String blank(CharSequence source, int start, int end) {
        int len = source.length();
        char[] buf = obtain(len);
        if (start != 0) {
            getChars(source, 0, start, buf, 0);
        }
        if (end != len) {
            getChars(source, end, len, buf, end);
        }
        if (start != end) {
            buf[start] = '…';
            for (int i = start + 1; i < end; i++) {
                buf[i] = '﻿';
            }
        }
        String ret = new String(buf, 0, len);
        recycle(buf);
        return ret;
    }

    public static CharSequence commaEllipsize(CharSequence text, TextPaint p, float avail, String oneMore, String more) {
        int i;
        float[] wid;
        int len = text.length();
        char[] buf = new char[len];
        getChars(text, 0, len, buf, 0);
        int commaCount = 0;
        for (i = 0; i < len; i++) {
            if (buf[i] == ',') {
                commaCount++;
            }
        }
        if (text instanceof Spanned) {
            Spanned sp = (Spanned) text;
            TextPaint temppaint = new TextPaint();
            wid = new float[(len * 2)];
            i = 0;
            while (i < len) {
                int next = sp.nextSpanTransition(i, len, MetricAffectingSpan.class);
                Styled.getTextWidths(p, temppaint, sp, i, next, wid, null);
                System.arraycopy(wid, 0, wid, len + i, next - i);
                i = next;
            }
            System.arraycopy(wid, len, wid, 0, len);
        } else {
            wid = new float[len];
            p.getTextWidths(text, 0, len, wid);
        }
        int ok = 0;
        int okRemaining = commaCount + 1;
        String okFormat = "";
        int w = 0;
        int count = 0;
        for (i = 0; i < len; i++) {
            w = (int) (((float) w) + wid[i]);
            if (buf[i] == ',') {
                String format;
                count++;
                int remaining = (commaCount - count) + 1;
                if (remaining == 1) {
                    format = " " + oneMore;
                } else {
                    format = " " + String.format(more, new Object[]{Integer.valueOf(remaining)});
                }
                if (((float) w) + p.measureText(format) <= avail) {
                    ok = i + 1;
                    okRemaining = remaining;
                    okFormat = format;
                }
            }
        }
        if (((float) w) <= avail) {
            return text;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(okFormat);
        spannableStringBuilder.insert(0, text, 0, ok);
        return spannableStringBuilder;
    }

    static char[] obtain(int len) {
        synchronized (sLock) {
            char[] buf = sTemp;
            sTemp = null;
        }
        if (buf == null || buf.length < len) {
            return new char[ArrayUtils.idealCharArraySize(len)];
        }
        return buf;
    }

    static void recycle(char[] temp) {
        if (temp.length <= 1000) {
            synchronized (sLock) {
                sTemp = temp;
            }
        }
    }

    public static String htmlEncode(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    public static CharSequence concat(CharSequence... text) {
        if (text.length == 0) {
            return "";
        }
        if (text.length == 1) {
            return text[0];
        }
        int i;
        boolean spanned = false;
        for (CharSequence charSequence : text) {
            if (charSequence instanceof Spanned) {
                spanned = true;
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (CharSequence charSequence2 : text) {
            sb.append(charSequence2);
        }
        if (!spanned) {
            return sb.toString();
        }
        SpannableString ss = new SpannableString(sb);
        int off = 0;
        for (i = 0; i < text.length; i++) {
            int len = text[i].length();
            if (text[i] instanceof Spanned) {
                copySpansFrom((Spanned) text[i], 0, len, Object.class, ss, off);
            }
            off += len;
        }
        return new SpannedString(ss);
    }

    public static boolean isGraphic(CharSequence str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            int gc = Character.getType(str.charAt(i));
            if (gc != 15 && gc != 16 && gc != 19 && gc != 0 && gc != 13 && gc != 14 && gc != 12) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGraphic(char c) {
        int gc = Character.getType(c);
        return (gc == 15 || gc == 16 || gc == 19 || gc == 0 || gc == 13 || gc == 14 || gc == 12) ? false : true;
    }

    public static boolean isDigitsOnly(CharSequence str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int getCapsMode(CharSequence cs, int off, int reqModes) {
        int mode = 0;
        if ((reqModes & 4096) != 0) {
            mode = 0 | 4096;
        }
        if ((reqModes & 24576) == 0) {
            return mode;
        }
        int i = off;
        while (i > 0) {
            char c = cs.charAt(i - 1);
            if (c != '\"' && c != '\'' && Character.getType(c) != 21) {
                break;
            }
            i--;
        }
        int j = i;
        while (j > 0) {
            c = cs.charAt(j - 1);
            if (c != ' ' && c != '\t') {
                break;
            }
            j--;
        }
        if (j == 0 || cs.charAt(j - 1) == '\n') {
            return mode | 8192;
        }
        if ((reqModes & 16384) == 0) {
            if (i != j) {
                mode |= 8192;
            }
            return mode;
        } else if (i == j) {
            return mode;
        } else {
            while (j > 0) {
                c = cs.charAt(j - 1);
                if (c != '\"' && c != '\'' && Character.getType(c) != 22) {
                    break;
                }
                j--;
            }
            if (j > 0) {
                c = cs.charAt(j - 1);
                if (c == '.' || c == '?' || c == '!') {
                    if (c == '.') {
                        for (int k = j - 2; k >= 0; k--) {
                            c = cs.charAt(k);
                            if (c == '.') {
                                return mode;
                            }
                            if (!Character.isLetter(c)) {
                                break;
                            }
                        }
                    }
                    return mode | 16384;
                }
            }
            return mode;
        }
    }
}