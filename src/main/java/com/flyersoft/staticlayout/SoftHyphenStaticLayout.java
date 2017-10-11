package com.flyersoft.staticlayout;

import android.graphics.Paint.FontMetricsInt;
import android.text.TextPaint;
import com.flyersoft.books.A;
import com.flyersoft.staticlayout.MyLayout.Alignment;
import com.flyersoft.staticlayout.MyLayout.Directions;
import com.flyersoft.staticlayout.TextUtils.TruncateAt;

public class SoftHyphenStaticLayout extends MyLayout {
    private static final int COLUMNS_ELLIPSIZE = 6;
    private static final int COLUMNS_NORMAL = 4;
    public static final int DESCENT = 2;
    private static final int DIR = 0;
    public static final Directions DIRS_ALL_LEFT_TO_RIGHT = new Directions(new short[]{Short.MAX_VALUE});
    static final Directions DIRS_ALL_RIGHT_TO_LEFT = new Directions(new short[]{(short) 0, Short.MAX_VALUE});
    private static final int DIR_SHIFT = 30;
    private static final int ELLIPSIS_COUNT = 5;
    private static final int ELLIPSIS_START = 4;
    private static final char FIRST_CJK = '⺀';
    private static final char FIRST_RIGHT_TO_LEFT = '֐';
    private static final int HYPHEN = 1;
    private static final int HYPHEN_MASK = 536870912;
    public static final int SEP = 3;
    private static final int SEPIND = 1;
    private static final int SEP_MASK = 1073741824;
    private static final int START = 0;
    private static final int START_MASK = 536870911;
    private static final int TAB = 0;
    private static final int TAB_INCREMENT = 20;
    private static final int TAB_MASK = 536870912;
    public static final int TOP = 1;
    private static final int TOP_MASK = 536870911;
    public static int mInnerWidth;
    MyFloatSpan floatSp;
    float float_width;
    private int mBottomPadding;
    private byte[] mChdirs;
    private char[] mChs;
    public int mColumns;
    private int mEllipsizedWidth;
    public FontMetricsInt mFontMetricsInt;
    private float mHyphenWidth;
    private boolean mIsBidirectional;
    private boolean mIsCJK;
    public int mLineCount;
    public Directions[] mLineDirections;
    public int[] mLines;
    private float mSpaceWidth;
    private int mTopPadding;
    private float[] mWidths;
    private int maxExtra;

    public SoftHyphenStaticLayout(MyTextView tv, CharSequence source, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(tv, source, 0, source.length(), paint, width, align, spacingmult, spacingadd, includepad);
    }

    public SoftHyphenStaticLayout(MyTextView tv, CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(tv, source, bufstart, bufend, paint, outerwidth, align, spacingmult, spacingadd, includepad, null, 0);
    }

    public SoftHyphenStaticLayout(MyTextView tv, CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Alignment align, float spacingmult, float spacingadd, boolean includepad, TruncateAt ellipsize, int ellipsizedWidth) {
        super(tv, source, paint, outerwidth, align, spacingmult, spacingadd);
        this.mIsBidirectional = false;
        this.mIsCJK = false;
        this.mFontMetricsInt = new FontMetricsInt();
        this.mColumns = 4;
        this.mEllipsizedWidth = outerwidth;
        this.mLines = new int[ArrayUtils.idealIntArraySize(this.mColumns * 2)];
        this.mLineDirections = new Directions[ArrayUtils.idealIntArraySize(this.mColumns * 2)];
        boolean ok = true;
        if (!(A.getBookType() == 7 || A.txtLay == null || A.txtView2 == null)) {
            int w = tv.getWidth();
            if (w == 0 || outerwidth == 0) {
                ok = false;
            } else if (A.dualPageEnabled()) {
                int w2 = A.txtScroll2.getWidth();
                int max = (A.baseFrame.getWidth() / 2) + 2;
                if (w > max || outerwidth > max || w2 == 0) {
                    ok = false;
                }
            }
        }
        if (ok) {
        }
        this.mChdirs = null;
        this.mChs = null;
        this.mWidths = null;
        this.mFontMetricsInt = null;
    }

    private boolean isHyphenation() {
        return A.textHyphenation;
    }

    private boolean isLeftQuote(char c) {
        return c == '“' || c == '«' || c == '„' || c == '(' || c == '<' || c == '[' || c == '{';
    }

    private boolean isRightQuote(char c) {
        return c == '”' || c == '\"' || c == '»' || c == ')' || c == '>' || c == ']' || c == '}' || isSingleQuote(c);
    }

    private boolean isSingleQuote(char c) {
        return c == '\'' || c == '’';
    }

    private static final boolean isIdeographic(char c, boolean includeNonStarters) {
        if ((c >= FIRST_CJK && c <= '⿿') || c == A.INDENT_CHAR) {
            return true;
        }
        if (c < '぀' || c > 'ゟ') {
            if (c < '゠' || c > 'ヿ') {
                if (c >= '㐀' && c <= '䶵') {
                    return true;
                }
                if (c >= '一' && c <= '龻') {
                    return true;
                }
                if (c >= '豈' && c <= '龎') {
                    return true;
                }
                if (c >= 'ꀀ' && c <= '꒏') {
                    return true;
                }
                if (c >= '꒐' && c <= '꓏') {
                    return true;
                }
                if (c >= '﹢' && c <= '﹦') {
                    return true;
                }
                if (c < '０' || c > '９') {
                    return false;
                }
                return true;
            } else if (includeNonStarters) {
                return true;
            } else {
                switch (c) {
                    case '゠':
                    case 'ァ':
                    case 'ィ':
                    case 'ゥ':
                    case 'ェ':
                    case 'ォ':
                    case 'ッ':
                    case 'ャ':
                    case 'ュ':
                    case 'ョ':
                    case 'ヮ':
                    case 'ヵ':
                    case 'ヶ':
                    case '・':
                    case 'ー':
                    case 'ヽ':
                    case 'ヾ':
                        return false;
                    default:
                        return true;
                }
            }
        } else if (includeNonStarters) {
            return true;
        } else {
            switch (c) {
                case 'ぁ':
                case 'ぃ':
                case 'ぅ':
                case 'ぇ':
                case 'ぉ':
                case 'っ':
                case 'ゃ':
                case 'ゅ':
                case 'ょ':
                case 'ゎ':
                case 'ゕ':
                case 'ゖ':
                case '゛':
                case '゜':
                case 'ゝ':
                case 'ゞ':
                    return false;
                default:
                    return true;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int out(java.lang.CharSequence r33, int r34, int r35, int r36, int r37, int r38, int r39, int r40, float r41, float r42, android.text.style.LineHeightSpan[] r43, int[] r44, android.graphics.Paint.FontMetricsInt r45, boolean r46, boolean r47, int r48, byte[] r49, int r50, boolean r51, boolean r52, boolean r53, boolean r54, float[] r55, int r56, int r57, com.flyersoft.staticlayout.TextUtils.TruncateAt r58, float r59, float r60, android.text.TextPaint r61) {
        /*
        r32 = this;
        r30 = -1;
        r2 = com.flyersoft.books.A.trimBlankSpace;
        if (r2 == 0) goto L_0x0015;
    L_0x0006:
        r2 = r35 - r34;
        r3 = 1;
        if (r2 != r3) goto L_0x0015;
    L_0x000b:
        r2 = r33.charAt(r34);
        r3 = 10;
        if (r2 != r3) goto L_0x0015;
    L_0x0013:
        r30 = r40;
    L_0x0015:
        r28 = 0;
        r2 = com.flyersoft.books.A.paragraphSpace;
        r3 = 10;
        if (r2 == r3) goto L_0x0026;
    L_0x001d:
        r2 = r35 - r34;
        r3 = 5;
        if (r2 >= r3) goto L_0x0026;
    L_0x0022:
        r28 = com.flyersoft.books.A.isEmtpyText(r33, r34, r35);
    L_0x0026:
        r0 = r32;
        r0 = r0.mLineCount;
        r20 = r0;
        r0 = r32;
        r2 = r0.mColumns;
        r27 = r20 * r2;
        r0 = r32;
        r2 = r0.mColumns;
        r2 = r2 + r27;
        r31 = r2 + 1;
        r0 = r32;
        r0 = r0.mLines;
        r25 = r0;
        r0 = r25;
        r2 = r0.length;
        r0 = r31;
        if (r0 < r2) goto L_0x007d;
    L_0x0047:
        r2 = r31 + 1;
        r26 = com.flyersoft.staticlayout.ArrayUtils.idealIntArraySize(r2);	 Catch:{ OutOfMemoryError -> 0x00b4 }
        r0 = r26;
        r15 = new int[r0];	 Catch:{ OutOfMemoryError -> 0x00b4 }
        r2 = 0;
        r3 = 0;
        r0 = r25;
        r4 = r0.length;	 Catch:{ OutOfMemoryError -> 0x00b4 }
        r0 = r25;
        java.lang.System.arraycopy(r0, r2, r15, r3, r4);	 Catch:{ OutOfMemoryError -> 0x00b4 }
        r0 = r32;
        r0.mLines = r15;	 Catch:{ OutOfMemoryError -> 0x00b4 }
        r25 = r15;
        r0 = r26;
        r0 = new com.flyersoft.staticlayout.MyLayout.Directions[r0];	 Catch:{ OutOfMemoryError -> 0x00b4 }
        r16 = r0;
        r0 = r32;
        r2 = r0.mLineDirections;	 Catch:{ OutOfMemoryError -> 0x00b4 }
        r3 = 0;
        r4 = 0;
        r0 = r32;
        r5 = r0.mLineDirections;	 Catch:{ OutOfMemoryError -> 0x00b4 }
        r5 = r5.length;	 Catch:{ OutOfMemoryError -> 0x00b4 }
        r0 = r16;
        java.lang.System.arraycopy(r2, r3, r0, r4, r5);	 Catch:{ OutOfMemoryError -> 0x00b4 }
        r0 = r16;
        r1 = r32;
        r1.mLineDirections = r0;	 Catch:{ OutOfMemoryError -> 0x00b4 }
    L_0x007d:
        if (r43 == 0) goto L_0x00d1;
    L_0x007f:
        r0 = r36;
        r1 = r45;
        r1.ascent = r0;
        r0 = r37;
        r1 = r45;
        r1.descent = r0;
        r0 = r38;
        r1 = r45;
        r1.top = r0;
        r0 = r39;
        r1 = r45;
        r1.bottom = r0;
        r18 = 0;
    L_0x0099:
        r0 = r43;
        r2 = r0.length;
        r0 = r18;
        if (r0 >= r2) goto L_0x00b9;
    L_0x00a0:
        r2 = r43[r18];
        r6 = r44[r18];
        r3 = r33;
        r4 = r34;
        r5 = r35;
        r7 = r40;
        r8 = r45;
        r2.chooseHeight(r3, r4, r5, r6, r7, r8);
        r18 = r18 + 1;
        goto L_0x0099;
    L_0x00b4:
        r13 = move-exception;
        com.flyersoft.books.A.error(r13);
        goto L_0x007d;
    L_0x00b9:
        r0 = r45;
        r0 = r0.ascent;
        r36 = r0;
        r0 = r45;
        r0 = r0.descent;
        r37 = r0;
        r0 = r45;
        r0 = r0.top;
        r38 = r0;
        r0 = r45;
        r0 = r0.bottom;
        r39 = r0;
    L_0x00d1:
        if (r20 != 0) goto L_0x00df;
    L_0x00d3:
        if (r54 == 0) goto L_0x00db;
    L_0x00d5:
        r2 = r38 - r36;
        r0 = r32;
        r0.mTopPadding = r2;
    L_0x00db:
        if (r53 == 0) goto L_0x00df;
    L_0x00dd:
        r36 = r38;
    L_0x00df:
        if (r52 == 0) goto L_0x00ed;
    L_0x00e1:
        if (r54 == 0) goto L_0x00e9;
    L_0x00e3:
        r2 = r39 - r37;
        r0 = r32;
        r0.mBottomPadding = r2;
    L_0x00e9:
        if (r53 == 0) goto L_0x00ed;
    L_0x00eb:
        r37 = r39;
    L_0x00ed:
        if (r47 == 0) goto L_0x021c;
    L_0x00ef:
        r0 = r35;
        r1 = r34;
        if (r0 <= r1) goto L_0x0207;
    L_0x00f5:
        r2 = r33.charAt(r34);
        r3 = 65532; // 0xfffc float:9.183E-41 double:3.2377E-319;
        if (r2 != r3) goto L_0x0207;
    L_0x00fe:
        r2 = r35 - r34;
        r3 = 1;
        if (r2 == r3) goto L_0x0114;
    L_0x0103:
        r2 = r35 - r34;
        r3 = 2;
        if (r2 != r3) goto L_0x0207;
    L_0x0108:
        r2 = r34 + 1;
        r0 = r33;
        r2 = r0.charAt(r2);
        r3 = 10;
        if (r2 != r3) goto L_0x0207;
    L_0x0114:
        r19 = 1;
    L_0x0116:
        if (r19 == 0) goto L_0x020b;
    L_0x0118:
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r14 = com.flyersoft.books.A.d(r2);
    L_0x011e:
        r0 = r32;
        r2 = r0.maxExtra;
        if (r14 <= r2) goto L_0x012e;
    L_0x0124:
        r0 = r32;
        r2 = r0.maxExtra;
        if (r2 <= 0) goto L_0x012e;
    L_0x012a:
        r0 = r32;
        r14 = r0.maxExtra;
    L_0x012e:
        r0 = r32;
        r2 = r0.mLineCount;
        r0 = r32;
        r1 = r34;
        r0.setLineStart(r2, r1);
        r0 = r32;
        r2 = r0.mLineCount;
        if (r2 != 0) goto L_0x014a;
    L_0x013f:
        r0 = r32;
        r2 = r0.mLineCount;
        r0 = r32;
        r1 = r40;
        r0.setLineTop(r2, r1);
    L_0x014a:
        r2 = r27 + 2;
        r3 = r37 + r14;
        r25[r2] = r3;
        r2 = r37 - r36;
        r12 = r2 + r14;
        r40 = r40 + r12;
        r0 = r32;
        r2 = r0.mLineCount;
        r2 = r2 + 1;
        r0 = r32;
        r1 = r35;
        r0.setLineStart(r2, r1);
        r0 = r32;
        r2 = r0.floatSp;
        if (r2 == 0) goto L_0x018c;
    L_0x0169:
        r0 = r32;
        r2 = r0.float_width;
        r3 = 0;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 <= 0) goto L_0x018c;
    L_0x0172:
        r0 = r32;
        r3 = r0.mLineCount;
        r0 = r32;
        r4 = r0.floatSp;
        r0 = r32;
        r2 = r0.floatSp;
        r2 = r2.isLeft;
        if (r2 == 0) goto L_0x021f;
    L_0x0182:
        r0 = r32;
        r2 = r0.float_width;
    L_0x0186:
        r2 = (int) r2;
        r0 = r32;
        r0.setLineFloat(r3, r4, r2);
    L_0x018c:
        if (r28 != 0) goto L_0x0226;
    L_0x018e:
        r29 = 0;
    L_0x0190:
        r2 = -1;
        r0 = r30;
        if (r0 != r2) goto L_0x024d;
    L_0x0195:
        if (r29 != 0) goto L_0x0238;
    L_0x0197:
        r23 = r40;
    L_0x0199:
        r2 = r33.length();
        r0 = r35;
        if (r0 != r2) goto L_0x01cb;
    L_0x01a1:
        r0 = r32;
        r2 = r0.floatSp;
        if (r2 == 0) goto L_0x01cb;
    L_0x01a7:
        r0 = r23;
        r2 = (float) r0;
        r0 = r32;
        r3 = r0.floatSp;
        r3 = r3.float_v;
        r0 = r32;
        r4 = r0.floatSp;
        r4 = r4.float_height;
        r3 = r3 + r4;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 >= 0) goto L_0x01cb;
    L_0x01bb:
        r0 = r32;
        r2 = r0.floatSp;
        r2 = r2.float_v;
        r0 = r32;
        r3 = r0.floatSp;
        r3 = r3.float_height;
        r2 = r2 + r3;
        r0 = (int) r2;
        r23 = r0;
    L_0x01cb:
        r0 = r32;
        r2 = r0.mLineCount;
        r2 = r2 + 1;
        r0 = r32;
        r1 = r23;
        r0.setLineTop(r2, r1);
    L_0x01d8:
        if (r46 == 0) goto L_0x01e3;
    L_0x01da:
        r2 = r27 + 0;
        r3 = r25[r2];
        r4 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
        r3 = r3 | r4;
        r25[r2] = r3;
    L_0x01e3:
        r2 = r27 + 0;
        r3 = r25[r2];
        r4 = r50 << 30;
        r3 = r3 | r4;
        r25[r2] = r3;
        r11 = 0;
        r9 = 0;
        if (r51 != 0) goto L_0x025c;
    L_0x01f0:
        r21 = r34;
    L_0x01f2:
        r0 = r21;
        r1 = r35;
        if (r0 >= r1) goto L_0x025c;
    L_0x01f8:
        r2 = r21 - r48;
        r2 = r49[r2];
        if (r2 == r11) goto L_0x0204;
    L_0x01fe:
        r9 = r9 + 1;
        r2 = r21 - r48;
        r11 = r49[r2];
    L_0x0204:
        r21 = r21 + 1;
        goto L_0x01f2;
    L_0x0207:
        r19 = 0;
        goto L_0x0116;
    L_0x020b:
        r2 = r37 - r36;
        r2 = (float) r2;
        r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = r41 - r3;
        r2 = r2 * r3;
        r2 = r2 + r42;
        r2 = (double) r2;
        r4 = 4602678819172646912; // 0x3fe0000000000000 float:0.0 double:0.5;
        r2 = r2 + r4;
        r14 = (int) r2;
        goto L_0x011e;
    L_0x021c:
        r14 = 0;
        goto L_0x012e;
    L_0x021f:
        r0 = r32;
        r2 = r0.float_width;
        r2 = -r2;
        goto L_0x0186;
    L_0x0226:
        r2 = com.flyersoft.books.A.paragraphSpace;
        if (r2 <= 0) goto L_0x0235;
    L_0x022a:
        r2 = com.flyersoft.books.A.paragraphSpace;
        r2 = r2 * 10;
    L_0x022e:
        r2 = 100 - r2;
        r2 = r2 * r12;
        r29 = r2 / 100;
        goto L_0x0190;
    L_0x0235:
        r2 = 8;
        goto L_0x022e;
    L_0x0238:
        r3 = r40 - r29;
        r2 = com.flyersoft.books.A.paragraphSpace;
        if (r2 <= 0) goto L_0x024b;
    L_0x023e:
        r2 = com.flyersoft.books.A.fontSize;
        r4 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r2 = r2 / r4;
        r2 = com.flyersoft.books.A.d(r2);
    L_0x0247:
        r23 = r3 - r2;
        goto L_0x0199;
    L_0x024b:
        r2 = 0;
        goto L_0x0247;
    L_0x024d:
        r0 = r32;
        r2 = r0.mLineCount;
        r2 = r2 + 1;
        r0 = r32;
        r1 = r30;
        r0.setLineTop(r2, r1);
        goto L_0x01d8;
    L_0x025c:
        if (r9 != 0) goto L_0x0278;
    L_0x025e:
        r24 = DIRS_ALL_LEFT_TO_RIGHT;
    L_0x0260:
        r0 = r32;
        r2 = r0.mLineDirections;
        r2[r20] = r24;
        r0 = r32;
        r2 = r0.mLineCount;
        r2 = r2 + 1;
        r0 = r32;
        r0.mLineCount = r2;
        r2 = -1;
        r0 = r30;
        if (r0 == r2) goto L_0x02be;
    L_0x0275:
        r40 = r30;
    L_0x0277:
        return r40;
    L_0x0278:
        r2 = r9 + 1;
        r0 = new short[r2];
        r22 = r0;
        r11 = 0;
        r9 = 0;
        r17 = r34;
        r21 = r34;
        r10 = r9;
    L_0x0285:
        r0 = r21;
        r1 = r35;
        if (r0 >= r1) goto L_0x02a2;
    L_0x028b:
        r2 = r21 - r48;
        r2 = r49[r2];
        if (r2 == r11) goto L_0x02c1;
    L_0x0291:
        r9 = r10 + 1;
        r2 = r21 - r17;
        r2 = (short) r2;
        r22[r10] = r2;
        r2 = r21 - r48;
        r11 = r49[r2];
        r17 = r21;
    L_0x029e:
        r21 = r21 + 1;
        r10 = r9;
        goto L_0x0285;
    L_0x02a2:
        r2 = r35 - r17;
        r2 = (short) r2;
        r22[r10] = r2;
        r2 = 1;
        if (r10 != r2) goto L_0x02b3;
    L_0x02aa:
        r2 = 0;
        r2 = r22[r2];
        if (r2 != 0) goto L_0x02b3;
    L_0x02af:
        r24 = DIRS_ALL_RIGHT_TO_LEFT;
        r9 = r10;
        goto L_0x0260;
    L_0x02b3:
        r24 = new com.flyersoft.staticlayout.MyLayout$Directions;
        r0 = r24;
        r1 = r22;
        r0.<init>(r1);
        r9 = r10;
        goto L_0x0260;
    L_0x02be:
        r40 = r40 - r29;
        goto L_0x0277;
    L_0x02c1:
        r9 = r10;
        goto L_0x029e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.staticlayout.SoftHyphenStaticLayout.out(java.lang.CharSequence, int, int, int, int, int, int, int, float, float, android.text.style.LineHeightSpan[], int[], android.graphics.Paint$FontMetricsInt, boolean, boolean, int, byte[], int, boolean, boolean, boolean, boolean, float[], int, int, com.flyersoft.staticlayout.TextUtils$TruncateAt, float, float, android.text.TextPaint):int");
    }

    private void calculateEllipsis(int linestart, int lineend, float[] widths, int widstart, int widoff, float avail, TruncateAt where, int line, float textwidth, TextPaint paint) {
        int len = lineend - linestart;
        if (textwidth <= avail) {
            this.mLines[(this.mColumns * line) + 4] = 0;
            this.mLines[(this.mColumns * line) + 5] = 0;
            return;
        }
        int ellipsisStart;
        int ellipsisCount;
        float ellipsiswid = paint.measureText("…");
        float sum;
        int i;
        float w;
        if (where == TruncateAt.START) {
            sum = 0.0f;
            i = len;
            while (i >= 0) {
                w = widths[(((i - 1) + linestart) - widstart) + widoff];
                if ((w + sum) + ellipsiswid > avail) {
                    break;
                }
                sum += w;
                i--;
            }
            ellipsisStart = 0;
            ellipsisCount = i;
        } else if (where == TruncateAt.END || where == TruncateAt.MARQUEE) {
            sum = 0.0f;
            i = 0;
            while (i < len) {
                w = widths[((i + linestart) - widstart) + widoff];
                if ((w + sum) + ellipsiswid > avail) {
                    break;
                }
                sum += w;
                i++;
            }
            ellipsisStart = i;
            ellipsisCount = len - i;
        } else {
            float lsum = 0.0f;
            float rsum = 0.0f;
            int right = len;
            float ravail = (avail - ellipsiswid) / 2.0f;
            right = len;
            while (right >= 0) {
                w = widths[(((right - 1) + linestart) - widstart) + widoff];
                if (w + rsum > ravail) {
                    break;
                }
                rsum += w;
                right--;
            }
            float lavail = (avail - ellipsiswid) - rsum;
            int left = 0;
            while (left < right) {
                w = widths[((left + linestart) - widstart) + widoff];
                if (w + lsum > lavail) {
                    break;
                }
                lsum += w;
                left++;
            }
            ellipsisStart = left;
            ellipsisCount = right - left;
        }
        this.mLines[(this.mColumns * line) + 4] = ellipsisStart;
        this.mLines[(this.mColumns * line) + 5] = ellipsisCount;
    }

    public int getLineForVertical(int vertical) {
        int high = this.mLineCount;
        int low = -1;
        int[] lines = this.mLines;
        while (high - low > 1) {
            int guess = (high + low) >> 1;
            if ((lines[(this.mColumns * guess) + 1] & 536870911) > vertical) {
                high = guess;
            } else {
                low = guess;
            }
        }
        if (low < 0) {
            return 0;
        }
        return low;
    }

    public int getLineCount() {
        return this.mLineCount;
    }

    public int getLineTop(int line) {
        return this.mLines[(this.mColumns * line) + 1] & 536870911;
    }

    public void setLineTop(int line, int v) {
        this.mLines[(this.mColumns * line) + 1] = (this.mLines[(this.mColumns * line) + 1] & -536870912) | v;
    }

    public void setLineStart(int line, int v) {
        this.mLines[(this.mColumns * line) + 0] = (this.mLines[(this.mColumns * line) + 0] & -536870912) | v;
    }

    public boolean isBidirectional() {
        return this.mIsBidirectional;
    }

    public boolean isCJK() {
        return this.mIsCJK;
    }

    public int getLineDescent(int line) {
        return this.mLines[(this.mColumns * line) + 2];
    }

    public int getLineStart(int line) {
        return this.mLines[(this.mColumns * line) + 0] & 536870911;
    }

    public int getParagraphDirection(int line) {
        return this.mLines[(this.mColumns * line) + 0] >> 30;
    }

    public boolean getLineContainsTab(int line) {
        return (this.mLines[(this.mColumns * line) + 0] & 536870912) != 0;
    }

    public final Directions getLineDirections(int line) {
        return this.mLineDirections[line];
    }

    public int getTopPadding() {
        return this.mTopPadding;
    }

    public int getBottomPadding() {
        return this.mBottomPadding;
    }

    public int getEllipsisCount(int line) {
        if (this.mColumns < 6) {
            return 0;
        }
        return this.mLines[(this.mColumns * line) + 5];
    }

    public int getEllipsisStart(int line) {
        if (this.mColumns < 6) {
            return 0;
        }
        return this.mLines[(this.mColumns * line) + 4];
    }

    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }
}