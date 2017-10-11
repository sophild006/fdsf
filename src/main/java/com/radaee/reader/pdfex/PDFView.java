package com.radaee.reader.pdfex;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public interface PDFView {

    public interface PDFAnnotListener {
        void onAnnotComboBox(int i, String[] strArr, float f, float f2, float f3, float f4);

        void onAnnotDragStart(boolean z, boolean z2);

        void onAnnotEditBox(int i, String str, float f, float f2, float f3, float f4, float f5);

        void onAnnotEnd();

        void onAnnotUpdate();
    }

    public static class PDFPageDispPara {
        public float bottom;
        public Canvas canvas;
        public boolean finished;
        public float left;
        public int pageno;
        public float real_ratio;
        public long render_time_span;
        public float right;
        public float top;
    }

    public static class PDFPosition {
        public int page = 0;
        public int page_x = 0;
        public int page_y = 0;
    }

    public static class PDFSelDispPara {
        public Canvas canvas;
        public float x1;
        public float x2;
        public float y1;
        public float y2;
    }

    public interface PDFViewListener {
        void onFound(boolean z);

        void onInvalidate();

        void onOpen3D(String str);

        void onOpenAttachment(String str);

        void onOpenMovie(String str);

        void onOpenSound(int[] iArr, String str);

        void onOpenURL(String str);

        void onPageChanged(int i);

        void onPageDisplayed(PDFPageDispPara pDFPageDispPara);

        void onSelDisplayed(PDFSelDispPara pDFSelDispPara);

        void onSelectEnd(String str);

        void onSelectStart();

        void onSingleTap(float f, float f2);

        void onSubmit(String str, String str2);
    }

    public enum STATUS {
        sta_none,
        sta_hold,
        sta_sel_prepare,
        sta_sel,
        sta_zoom,
        sta_annot,
        sta_ink,
        sta_rect,
        sta_scroll,
        sta_ellipse,
        sta_line,
        sta_arrow,
        sta_note,
        sta_freetext
    }

    void annotArrow();

    void annotEllipse();

    void annotEnd();

    void annotFreeText(String str);

    String annotGetSubject();

    String annotGetText();

    void annotInk();

    void annotLine();

    void annotNote(String str);

    void annotPerform();

    void annotRect();

    void annotRemove();

    boolean annotSetChoice(int i);

    boolean annotSetEditText(String str);

    boolean annotSetMarkup(int i);

    boolean annotSetSubject(String str);

    boolean annotSetText(String str);

    PDFPage getCurPDFPage();

    float[] getDibPoint(float f, float f2);

    float[] getPdfPoint(float f, float f2);

    Annotation getSelectedAnnot();

    void refreshCurPage();

    void viewClose();

    void viewDraw(Canvas canvas);

    void viewEnableTextSelection(boolean z);

    int viewFind(int i);

    void viewFindEnd();

    void viewFindStart(String str, boolean z, boolean z2);

    PDFAnnotListener viewGetAnnotListener();

    int viewGetCurPageNo();

    String viewGetCurPageText();

    Document viewGetDoc();

    PDFPosition viewGetPos();

    float viewGetRatio();

    PDFThread viewGetThread();

    PDFViewListener viewGetViewListener();

    void viewGoto(PDFPosition pDFPosition);

    void viewGotoNextPage();

    void viewGotoPage(int i);

    void viewGotoPrevPage();

    void viewLockSide(boolean z, Context context);

    void viewOpen(Context context, Document document, int i, int i2);

    void viewResize(int i, int i2);

    void viewSetAnnotListener(PDFAnnotListener pDFAnnotListener);

    boolean viewSetRatio(float f, float f2, float f3, boolean z);

    void viewSetSel(float f, float f2, float f3, float f4);

    void viewSetViewListener(PDFViewListener pDFViewListener);

    boolean viewTouchEvent(MotionEvent motionEvent);
}