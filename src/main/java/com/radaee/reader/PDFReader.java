package com.radaee.reader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.flyersoft.books.A;
import com.flyersoft.books.A.Bookmark;
import com.flyersoft.books.A.Bookmarks;
import com.flyersoft.books.BookDb.NoteInfo;
import com.flyersoft.books.T;
import com.flyersoft.moonreader.ActivityTxt;
import com.flyersoft.moonreader.R;
import java.util.ArrayList;
import java.util.Iterator;

public class PDFReader extends View implements PDFViewListener {
    public static int innerLinkRecord = -1;
    PDFAnnotListener annot_listener = null;
    private ActivityTxt dotsAct;
    public long fileOpenTime;
    String filename;
    public boolean forbid_immersive_mode;
    private boolean fromPausedResumeOfLandscape;
    boolean isMultiTouch;
    public boolean isOnPaused;
    private boolean m_lock_resize = false;
    private int m_save_h = 0;
    private int m_save_w = 0;
    private int maxH;
    int moveCount;
    private Integer[] page_chars;
    private Integer[] page_words;
    private float paraRatio = 0.0f;
    public PDFView pdfView = null;
    private int px1;
    private int px2;
    boolean releaseFromMultiTouch;
    public ArrayList<PDFNote> remoteNotes;
    private boolean selectEnd;
    public PDFSelDispPara selectedPara;
    public String selectedText;
    public boolean textReflow;
    Bitmap themeBitmap;
    Canvas themeCanvas;
    public PDFThumbView2 thumbView;
    boolean touchDisabled;
    int touchX;
    int touchY;
    public ArrayList<Integer> turnedPages = new ArrayList();
    private Bitmap visualBm;
    private int y1Offset;

    public static class PDFNote {
        int color;
        int end;
        String note_text;
        String original;
        int page;
        int start;
        long timeStamp;
        int type;

        public PDFNote(int page, long timeStamp, int start, int end, int color, int type, String note_text, String original) {
            this.page = page;
            this.timeStamp = timeStamp;
            this.start = start;
            this.end = end;
            this.color = color;
            this.type = type;
            this.note_text = note_text;
            this.original = original;
        }
    }

    public PDFReader(Context context) {
        super(context);
    }

    public PDFReader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void set_viewer(int view_style) {
        PDFPosition pos = null;
        PDFViewListener view_listener = null;
        Document doc = null;
        if (this.pdfView != null) {
            doc = this.pdfView.viewGetDoc();
            this.annot_listener = this.pdfView.viewGetAnnotListener();
            view_listener = this.pdfView.viewGetViewListener();
            pos = this.pdfView.viewGetPos();
            this.pdfView.viewClose();
        }
        switch (view_style) {
            case 1:
            case 2:
            case 5:
                break;
            case 3:
                this.pdfView = new PDFViewSingle();
                break;
            case 4:
                this.pdfView = new PDFViewSingleEx();
                break;
            default:
                this.pdfView = new PDFViewVert();
                break;
        }
        if (this.pdfView != null) {
            if (doc != null) {
                viewOpen2(doc);
            }
            this.pdfView.viewSetAnnotListener(this.annot_listener);
            this.pdfView.viewSetViewListener(view_listener);
            this.pdfView.viewResize(getWidth(), getHeight());
            if (pos != null) {
                this.pdfView.viewGoto(pos);
            }
        }
    }

    public void open(Document doc, String filename) {
        this.filename = filename;
        this.fileOpenTime = SystemClock.elapsedRealtime();
        this.remoteNotes = null;
        set_viewer(Global.def_view);
        if (this.pdfView != null) {
            viewOpen2(doc);
        }
    }

    public void viewOpen2(Document doc) {
        int gap = 4;
        if (Global.def_view == 4) {
            gap = 0;
        }
        this.pdfView.viewOpen(getContext(), doc, get_bg_color(), gap);
    }

    private int get_bg_color() {
        return A.pdf_theme != 0 ? -1 : RGB2PDfColor(A.pdf_back_color);
    }

    public int RGB2PDfColor(int color) {
        return RGB2PDfColor(color, 255, 0);
    }

    public int RGB2PDfColor(int color, int alpha, int offset) {
        int r = Color.red(color) + offset;
        int b = Color.blue(color) + offset;
        int g = Color.green(color) + offset;
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }
        if (r < 0) {
            r = 0;
        }
        if (g < 0) {
            g = 0;
        }
        if (b < 0) {
            b = 0;
        }
        if (alpha == -1) {
            alpha = Color.alpha(color);
        }
        return Color.argb(alpha, r, g, b);
    }

    public void close() {
        if (this.pdfView != null) {
            this.pdfView.viewClose();
        }
        this.pdfView = null;
    }

    public void set_thumb(PDFThumbView2 view) {
        this.thumbView = view;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        if (!this.fromPausedResumeOfLandscape) {
            this.fromPausedResumeOfLandscape = this.isOnPaused;
        }
        super.onConfigurationChanged(newConfig);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!A.immersive_fullscreen || !A.isLandscape() || !this.isOnPaused) {
            ActivityTxt a = getAct();
            if (A.immersive_fullscreen && a != null && Global.def_view == 0 && !A.isLandscape()) {
                if (!this.isOnPaused) {
                    if (a.pausedTime > 0 && SystemClock.elapsedRealtime() - a.resumeTime < 500) {
                        return;
                    }
                    if (this.maxH > 0 && h == this.maxH) {
                        return;
                    }
                }
                return;
            }
            if (h > this.maxH) {
                this.maxH = h;
            }
            if (!(this.pdfView == null || this.fromPausedResumeOfLandscape || this.forbid_immersive_mode)) {
                try {
                    this.pdfView.viewResize(w, h);
                } catch (Exception e) {
                    A.error(e);
                }
            }
            if (!this.isOnPaused) {
                this.fromPausedResumeOfLandscape = false;
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        boolean z = false;
        if (A.pdf_theme == 1) {
            z = true;
        }
        Global.dark_mode = z;
        if (this.pdfView != null && !this.textReflow) {
            if (this.isMultiTouch) {
                canvas.drawColor(get_bg_color());
            }
            if (A.pdf_theme > 1) {
                try {
                    int w = getWidth();
                    int h = getHeight();
                    if (this.themeCanvas == null) {
                        this.themeBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                        this.themeCanvas = new Canvas(this.themeBitmap);
                    }
                    drawPdfView(this.themeCanvas);
                    ColorMatrix cm = new ColorMatrix();
                    cm.set(getThemeArray(A.pdf_theme));
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
                    Paint paint = new Paint();
                    paint.setColorFilter(filter);
                    canvas.drawBitmap(this.themeBitmap, new Rect(0, 0, w, h), new Rect(0, 0, w, h), paint);
                } catch (OutOfMemoryError e) {
                    System.gc();
                    drawPdfView(canvas);
                } catch (Exception e2) {
                    A.error(e2);
                    drawPdfView(canvas);
                }
            } else {
                drawPdfView(canvas);
            }
            drawVisualBookmark(canvas);
        }
    }

    private void drawVisualBookmark(Canvas canvas) {
        Bookmarks bms = A.getBookmarksOfOneFile(this.filename);
        if (bms != null) {
            int cur = this.pdfView.viewGetCurPageNo();
            Iterator it = bms.list.iterator();
            while (it.hasNext()) {
                Bookmark bm = (Bookmark) it.next();
                if (bm.position == ((long) cur)) {
                    if (this.visualBm == null) {
                        this.visualBm = BitmapFactory.decodeResource(getResources(), R.drawable.bookmark_tag);
                    }
                    int h = A.d(A.isLargeTablet ? 30.0f : 24.0f);
                    int w = (h * 40) / 70;
                    int y = (h * 9) / 100;
                    int x = (getWidth() - w) - y;
                    canvas.drawBitmap(this.visualBm, new Rect(0, 0, this.visualBm.getWidth(), this.visualBm.getHeight()), new Rect(x, y, x + w, y + h), A.createBookmarkPaint(A.d(1.0f), 0, bm.color));
                    return;
                }
            }
        }
    }

    private void drawPdfView(Canvas canvas) {
        try {
            this.pdfView.viewDraw(canvas);
        } catch (Exception e) {
            A.error(e);
        }
    }

    public static float[] getThemeArray(int theme) {
        switch (theme) {
            case 2:
                return new float[]{1.0f, 0.0f, 0.0f, 0.0f, -50.0f, 0.0f, 1.0f, 0.0f, 0.0f, -60.0f, 0.0f, 0.0f, 1.0f, 0.0f, -90.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            case 3:
                return new float[]{1.0f, 0.0f, 0.0f, 0.0f, -100.0f, 0.0f, 1.0f, 0.0f, 0.0f, -30.0f, 0.0f, 0.0f, 1.0f, 0.0f, -20.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            case 4:
                return new float[]{1.0f, 0.0f, 0.0f, 0.0f, -60.0f, 0.0f, 1.0f, 0.0f, 0.0f, -60.0f, 0.0f, 0.0f, 1.0f, 0.0f, -60.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            case 5:
                return new float[]{1.0f, 0.0f, 0.0f, 0.0f, -10.0f, 0.0f, 1.0f, 0.0f, 0.0f, -60.0f, 0.0f, 0.0f, 1.0f, 0.0f, -40.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            case 6:
                return new float[]{1.0f, 0.0f, 0.0f, 0.0f, -60.0f, 0.0f, 1.0f, 0.0f, 0.0f, -60.0f, 0.0f, 0.0f, 1.0f, 0.0f, -10.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            case 7:
                return new float[]{1.0f, 0.0f, 0.0f, 0.0f, -60.0f, 0.0f, 1.0f, 0.0f, 0.0f, -10.0f, 0.0f, 0.0f, 1.0f, 0.0f, -55.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            case 8:
                return new float[]{-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, 0.0f, 1.0f, 30.0f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            case 9:
                return new float[]{-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, -1.0f, 1.0f, 30.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            case 10:
                return new float[]{-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, 0.0f, 1.0f, 51.0f, 0.0f, 0.0f, -1.0f, 1.0f, 50.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
            default:
                return new float[]{-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
        }
    }

    public static int getThemeColor(int theme) {
        switch (theme) {
            case 0:
                return -1;
            case 2:
                return -3292251;
            case 3:
                return -6561301;
            case 4:
                return -3947581;
            case 5:
                return -670761;
            case 6:
                return -3947531;
            case 7:
                return -3934776;
            case 8:
                return -16703999;
            case 9:
                return -16711394;
            case 10:
                return -16698574;
            default:
                return ViewCompat.MEASURED_STATE_MASK;
        }
    }

    public int getThemeCount() {
        return 11;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!(getAct() == null || isDrawState() || event.getAction() != 0)) {
            getAct().pressDownX = event.getX();
            this.releaseFromMultiTouch = false;
            if (getAct().isTouchInEdge(event)) {
                this.touchDisabled = true;
            }
        }
        if (!this.touchDisabled) {
            try {
                boolean z = !isDrawState() && event.getPointerCount() > 1;
                this.isMultiTouch = z;
                this.touchX = (int) event.getX();
                this.touchY = (int) event.getY();
                A.pdfHighlightFromDot = false;
                if (!(this.pdfView == null || this.textReflow || A.isInAutoScroll || A.isSpeaking)) {
                    this.pdfView.viewTouchEvent(event);
                }
                if (isDrawState()) {
                    if (event.getAction() == 1 && (A.pdfStatus == STATUS.sta_note || A.pdfStatus == STATUS.sta_freetext)) {
                        final STATUS status = A.pdfStatus;
                        A.pdfStatus = STATUS.sta_none;
                        final EditText et = new EditText(getContext());
                        et.setText("");
                        new Builder(getContext()).setView(et).setPositiveButton(17039370, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if (et.getText().toString().length() > 0) {
                                        if (status == STATUS.sta_note) {
                                            PDFReader.this.pdfView.annotNote(et.getText().toString());
                                            A.pdfAnnotUpdated = true;
                                        }
                                        if (status == STATUS.sta_freetext) {
                                            PDFReader.this.pdfView.annotFreeText(et.getText().toString());
                                            A.pdfAnnotUpdated = true;
                                        }
                                    }
                                    if (PDFReader.this.annot_listener != null) {
                                        PDFReader.this.annot_listener.onAnnotEnd();
                                    }
                                } catch (Exception e) {
                                    A.error(e);
                                }
                            }
                        }).setNegativeButton(17039360, new OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (PDFReader.this.annot_listener != null) {
                                    PDFReader.this.annot_listener.onAnnotEnd();
                                }
                            }
                        }).setOnCancelListener(new OnCancelListener() {
                            public void onCancel(DialogInterface dialog) {
                                if (PDFReader.this.annot_listener != null) {
                                    PDFReader.this.annot_listener.onAnnotEnd();
                                }
                            }
                        }).show();
                    }
                    return true;
                }
                if (this.isMultiTouch) {
                    this.releaseFromMultiTouch = true;
                }
                if (event.getAction() == 0) {
                    this.selectedText = null;
                    this.selectedPara = null;
                    this.y1Offset = 0;
                    this.releaseFromMultiTouch = false;
                    if (A.pdfStatus == STATUS.sta_sel) {
                        A.pdfStatus = STATUS.sta_none;
                    }
                }
            } catch (Exception e) {
                A.error(e);
            }
        }
        if (getAct() != null) {
            if (this.releaseFromMultiTouch && event.getAction() == 1) {
                getAct().pdfShowFullStatusBarInfoHandler();
            }
            if (!isDrawState()) {
                getAct().onTouch(this, event);
            }
            if (getAct().hBar.getVisibility() == 0 && !getAct().pdfAnnotStart) {
                A.pdfStatus = STATUS.sta_sel;
            }
        }
        if (event.getAction() == 1) {
            this.touchDisabled = false;
        }
        return true;
    }

    public void setAnnotListener(PDFAnnotListener listener) {
        this.annot_listener = listener;
        if (this.pdfView != null) {
            this.pdfView.viewSetAnnotListener(listener);
        }
    }

    public void setViewListener(PDFViewListener listener) {
        if (this.pdfView != null) {
            this.pdfView.viewSetViewListener(listener);
        }
    }

    public String annotGetText() {
        if (this.pdfView != null) {
            return this.pdfView.annotGetText();
        }
        return null;
    }

    public String annotGetSubject() {
        if (this.pdfView != null) {
            return this.pdfView.annotGetSubject();
        }
        return null;
    }

    public boolean annotSetText(String txt) {
        if (this.pdfView != null) {
            return this.pdfView.annotSetText(txt);
        }
        return false;
    }

    public boolean annotSetSubject(String subj) {
        if (this.pdfView != null) {
            return this.pdfView.annotSetSubject(subj);
        }
        return false;
    }

    public void annotInk() {
        A.pdfStatus = STATUS.sta_none;
        if (this.pdfView != null) {
            this.pdfView.annotInk();
        }
    }

    public void annotRect() {
        A.pdfStatus = STATUS.sta_none;
        if (this.pdfView != null) {
            this.pdfView.annotRect();
        }
    }

    public void annotLine() {
        A.pdfStatus = STATUS.sta_none;
        if (this.pdfView != null) {
            this.pdfView.annotLine();
        }
    }

    public void annotArrow() {
        A.pdfStatus = STATUS.sta_none;
        if (this.pdfView != null) {
            this.pdfView.annotArrow();
        }
    }

    public void annotEllipse() {
        A.pdfStatus = STATUS.sta_none;
        if (this.pdfView != null) {
            this.pdfView.annotEllipse();
        }
    }

    public void annotNote() {
        A.pdfStatus = STATUS.sta_none;
        if (this.pdfView != null) {
            this.pdfView.annotNote(null);
        }
    }

    public void annotFreeText() {
        A.pdfStatus = STATUS.sta_none;
        if (this.pdfView != null) {
            this.pdfView.annotFreeText(null);
        }
    }

    public boolean isDrawState() {
        return A.pdfStatus == STATUS.sta_ink || A.pdfStatus == STATUS.sta_rect || A.pdfStatus == STATUS.sta_line || A.pdfStatus == STATUS.sta_arrow || A.pdfStatus == STATUS.sta_ellipse || A.pdfStatus == STATUS.sta_freetext || A.pdfStatus == STATUS.sta_note;
    }

    public void annotPerform() {
        if (this.pdfView != null) {
            this.pdfView.annotPerform();
        }
    }

    public void annotEnd() {
        if (this.pdfView != null) {
            try {
                this.pdfView.annotEnd();
            } catch (Exception e) {
                A.error(e);
            }
        }
        if (getAct() != null) {
            getAct().pdfAnnotStart = false;
            getAct().longTimeTapEventDown = false;
        }
    }

    public void annotRemove() {
        if (this.pdfView != null) {
            try {
                this.pdfView.annotRemove();
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    public void find(int dir) {
        if (this.pdfView != null) {
            this.pdfView.viewFind(dir);
        }
    }

    public void findStart(String str, boolean match_case, boolean whole_word) {
        if (this.pdfView != null) {
            this.pdfView.viewFindStart(str, match_case, whole_word);
        }
    }

    public void onInvalidate() {
        if (this.pdfView != null) {
            invalidate();
        }
    }

    public void onFound(boolean found) {
        if (!found) {
            T.showToastText(getContext(), getContext().getString(R.string.no_more_found));
        }
    }

    public void onOpenURL(String url) {
        if (getAct() != null) {
            innerLinkRecord = -2;
            getAct().longTimeTapEvent = false;
            getAct().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
        }
    }

    public void onPageChanged(int pageno) {
        if (this.turnedPages.indexOf(Integer.valueOf(pageno)) == -1) {
            this.turnedPages.add(Integer.valueOf(pageno));
        }
        if (this.thumbView != null && this.thumbView.getVisibility() == 0) {
            this.thumbView.thumbGotoPage(pageno);
        }
        if (getAct() != null) {
            getAct().pdfAnnotStart = false;
            if (SystemClock.elapsedRealtime() - this.fileOpenTime > 2000) {
                getAct().showReadProgress(0);
            }
            if (SystemClock.elapsedRealtime() - this.fileOpenTime > 5000) {
                getAct().saveLastPostion(true);
                A.forceRebootToTxt = true;
                A.SaveOptions(ActivityTxt.selfPref);
            }
        }
    }

    public void onSingleTap(float x, float y) {
    }

    public void onOpen3D(String file_name) {
    }

    public void onOpenMovie(String file_name) {
        if (!file_name.startsWith("/")) {
            file_name = Global.tmp_path + "/" + file_name;
        }
        if (!T.isNull(ActivityTxt.selfPref)) {
            String ext = T.getFileExt(file_name);
            if (ext.equals(".mp3") || ext.equals(".wma") || ext.equals(".wav")) {
                ActivityTxt.selfPref.playMp3(file_name);
            } else {
                ActivityTxt.selfPref.playVideo(file_name);
            }
            innerLinkRecord = -2;
            getAct().longTimeTapEvent = false;
        }
    }

    public void onOpenSound(int[] paras, String file_name) {
        if (!file_name.startsWith("/")) {
            file_name = Global.tmp_path + "/" + file_name;
        }
        if (!T.isNull(ActivityTxt.selfPref)) {
            ActivityTxt.selfPref.playMp3(file_name);
            innerLinkRecord = -2;
            getAct().longTimeTapEvent = false;
        }
    }

    public void onOpenAttachment(String file_name) {
    }

    public void onSelectStart() {
        this.selectEnd = false;
    }

    public void onSelectEnd(String text) {
        this.selectEnd = true;
        this.selectedText = text;
        ActivityTxt a = this.dotsAct;
        if (this.selectedText == null && a != null) {
            int dw = a.dot1.getWidth();
            int dh = a.dot1.getHeight();
            int x1 = a.dot1.getLeft();
            int x2 = x1 + dw;
            int y = a.dot1.getTop();
            a.dot1.layout(x1 - (dw / 2), y, (x1 - (dw / 2)) + dw, y + dh);
            a.dot2.layout(x2 - (dw / 2), y, (x2 - (dw / 2)) + dw, y + dh);
            a.layoutHBar();
        } else if (this.selectedText != null && !A.pdfHighlightFromDot) {
            if (a == null && A.pdfStatus == STATUS.sta_sel) {
                a = getAct();
            }
            if (a != null) {
                showSelector(a, this.selectedPara);
                a.dot1.setVisibility(0);
                a.dot2.setVisibility(0);
            }
        }
    }

    public void onSelDisplayed(PDFSelDispPara para) {
        if (para != null) {
            this.selectedPara = para;
            if (this.selectEnd) {
                ActivityTxt a = this.dotsAct;
                if (a == null && getAct() != null && getAct().hBar.getVisibility() == 0) {
                    a = getAct();
                }
                if (a != null) {
                    showSelector(a, para);
                    this.dotsAct = null;
                }
            }
        }
    }

    private void showSelector(ActivityTxt a, PDFSelDispPara para) {
        if (a != null && para != null) {
            boolean z = this.y1Offset == 0;
            if (this.y1Offset == 0) {
                this.y1Offset = (int) (para.y2 - para.y1);
            }
            int x1 = (int) para.x1;
            int x2 = (int) para.x2;
            int y2 = (int) para.y2;
            int y1 = ((int) para.y1) + this.y1Offset;
            int dw = a.dot1.getWidth();
            int dh = a.dot1.getHeight();
            if (!A.pdfHighlightFromDot) {
                this.px1 = x1;
                this.px2 = x2;
            } else if (this.px1 != x1 && this.px2 != x2) {
                return;
            } else {
                if (this.px1 > 0 && this.px2 > 0 && x1 < 0 && x2 < 0) {
                    return;
                }
            }
            if (x1 < 0) {
                z = true;
                x1 = dw / 2;
            }
            if (x2 < 0) {
                z = true;
                x2 = dw;
            }
            if (x1 == x2) {
                z = true;
                x2 = x1 + (dw / 2);
            }
            if (z || Global.def_view != 4 || (this.touchX >= x1 && this.touchX <= x2 && this.touchY >= y1 - A.d(100.0f) && this.touchY <= A.d(100.0f) + y2)) {
                a.dot1.layout(x1 - (dw / 2), y1, (x1 - (dw / 2)) + dw, y1 + dh);
                a.dot2.layout(x2 - (dw / 2), y2, (x2 - (dw / 2)) + dw, y2 + dh);
                a.layoutHBar();
            }
        }
    }

    public float getParaRatio() {
        if ((this.paraRatio == 0.0f || this.paraRatio == 1.0f) && this.pdfView != null) {
            this.paraRatio = this.pdfView.viewGetRatio();
        }
        return this.paraRatio;
    }

    public void setParaRatio(float value) {
        if (value < 0.1f) {
            value = 0.1f;
        }
        this.paraRatio = value;
    }

    public void onPageDisplayed(PDFPageDispPara para) {
        this.paraRatio = para.real_ratio;
    }

    public void lockResize(boolean lock) {
        if (this.m_lock_resize != lock) {
            this.m_lock_resize = lock;
            if (lock) {
                this.m_save_w = getWidth();
                this.m_save_h = getHeight();
                return;
            }
            this.pdfView.viewResize(this.m_save_w, this.m_save_h);
        }
    }

    public boolean annotSetEditText(String txt) {
        if (this.pdfView != null) {
            return this.pdfView.annotSetEditText(txt);
        }
        return false;
    }

    public boolean annotSetChoice(int item) {
        if (this.pdfView != null) {
            return this.pdfView.annotSetChoice(item);
        }
        return false;
    }

    public void onSubmit(String target, String para) {
        Toast.makeText(getContext(), "onSubmit:" + target + "\nparameters:" + para, 1).show();
    }

    public void recordDotsAct(ActivityTxt act) {
        this.dotsAct = act;
    }

    private static ActivityTxt getAct() {
        return !T.isNull(ActivityTxt.selfPref) ? ActivityTxt.selfPref : null;
    }

    public static void saveCurPageNo() {
        innerLinkRecord = getAct() != null ? getAct().pdfCurPageNo : -1;
        if (innerLinkRecord >= 0) {
            getAct().longTimeTapEvent = false;
            getAct().saveLinkBackInfo(true);
            getAct().linkBackSetVisible();
        }
    }

    public void addNewRemoteNote(long page, long timeStamp, int start, int end, int color, int type, String note_text, String original) {
        if (start != end) {
            if (this.remoteNotes == null) {
                this.remoteNotes = new ArrayList();
            }
            this.remoteNotes.add(new PDFNote((int) page, timeStamp, start, end, color, type, note_text, original));
        }
    }

    public String createTextFromNotes() {
        if (this.remoteNotes == null || this.remoteNotes.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("" + this.pdfView.viewGetDoc().GetPageCount());
        Iterator it = this.remoteNotes.iterator();
        while (it.hasNext()) {
            PDFNote n = (PDFNote) it.next();
            sb.append("#A*#" + n.page);
            sb.append("#A1#" + n.timeStamp);
            sb.append("#A2#" + n.start);
            sb.append("#A3#" + n.end);
            sb.append("#A4#" + n.color);
            sb.append("#A5#" + n.type);
            sb.append("#A6#" + n.note_text);
            sb.append("#A7#" + n.original);
            sb.append("#A@#");
        }
        return sb.toString();
    }

    public boolean createNotesFromCloud(String s) {
        this.remoteNotes = new ArrayList();
        boolean result = false;
        int p = 0;
        while (true) {
            int i = s.indexOf("#A*#", p);
            if (p == 0) {
                if (T.string2Int(s.substring(0, i)) != this.pdfView.viewGetDoc().GetPageCount()) {
                    return false;
                }
            }
            p = i + 10;
            if (i == -1) {
                break;
            }
            try {
                this.remoteNotes.add(new PDFNote(Integer.valueOf(s.substring(i + 4, s.indexOf("#A1#", i))).intValue(), Long.valueOf(s.substring(s.indexOf("#A1#", i) + 4, s.indexOf("#A2#", i))).longValue(), Integer.valueOf(s.substring(s.indexOf("#A2#", i) + 4, s.indexOf("#A3#", i))).intValue(), Integer.valueOf(s.substring(s.indexOf("#A3#", i) + 4, s.indexOf("#A4#", i))).intValue(), Integer.valueOf(s.substring(s.indexOf("#A4#", i) + 4, s.indexOf("#A5#", i))).intValue(), Integer.valueOf(s.substring(s.indexOf("#A5#", i) + 4, s.indexOf("#A6#", i))).intValue(), s.substring(s.indexOf("#A6#", i) + 4, s.indexOf("#A7#", i)), s.substring(s.indexOf("#A7#", i) + 4, s.indexOf("#A@#", i))));
            } catch (Exception e) {
                A.error(e);
            }
        }
        if (this.remoteNotes.size() == 0) {
            return false;
        }
        A.getBookmarks(true);
        A.checkNotesHighlights(true);
        Iterator it = this.remoteNotes.iterator();
        while (it.hasNext()) {
            PDFNote n1 = (PDFNote) it.next();
            boolean has = false;
            Iterator it2 = A.highlights.iterator();
            while (it2.hasNext()) {
                NoteInfo n2 = (NoteInfo) it2.next();
                if (n2.lastPosition == ((long) n1.page) && n2.lastChapter == n1.start) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                NoteInfo note = new NoteInfo(0, A.getBookName(), this.filename, n1.start, n1.end, (long) n1.page, n1.end - n1.start, n1.color, n1.timeStamp, "", n1.note_text, n1.original, n1.type == 1, n1.type == 2, "");
                A.addNote(note);
                if (n1.note_text.length() > 0) {
                    A.getNotes().add(note);
                }
                result = true;
            }
        }
        updateNotesToFile();
        this.remoteNotes.clear();
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateNotesToFile() {
        /*
        r21 = this;
        r0 = r21;
        r0 = r0.remoteNotes;
        r16 = r0;
        if (r16 == 0) goto L_0x0014;
    L_0x0008:
        r0 = r21;
        r0 = r0.remoteNotes;
        r16 = r0;
        r16 = r16.size();
        if (r16 != 0) goto L_0x0015;
    L_0x0014:
        return;
    L_0x0015:
        r8 = com.flyersoft.books.A.pdf_highlight_color;
        r15 = com.flyersoft.books.A.underline_color;
        r14 = com.flyersoft.books.A.strikethrough_color;
        r13 = com.flyersoft.books.A.squiggly_color;
        r16 = new java.io.File;	 Catch:{ Exception -> 0x0100 }
        r0 = r21;
        r0 = r0.filename;	 Catch:{ Exception -> 0x0100 }
        r17 = r0;
        r16.<init>(r17);	 Catch:{ Exception -> 0x0100 }
        r6 = r16.lastModified();	 Catch:{ Exception -> 0x0100 }
        r9 = new java.util.HashSet;	 Catch:{ Exception -> 0x0100 }
        r9.<init>();	 Catch:{ Exception -> 0x0100 }
        r0 = r21;
        r0 = r0.remoteNotes;	 Catch:{ Exception -> 0x0100 }
        r16 = r0;
        r16 = r16.iterator();	 Catch:{ Exception -> 0x0100 }
    L_0x003b:
        r17 = r16.hasNext();	 Catch:{ Exception -> 0x0100 }
        if (r17 == 0) goto L_0x011f;
    L_0x0041:
        r10 = r16.next();	 Catch:{ Exception -> 0x0100 }
        r10 = (com.radaee.reader.PDFReader.PDFNote) r10;	 Catch:{ Exception -> 0x0100 }
        r0 = r10.page;	 Catch:{ Exception -> 0x0100 }
        r17 = r0;
        r17 = java.lang.Integer.valueOf(r17);	 Catch:{ Exception -> 0x0100 }
        r0 = r17;
        r17 = r9.contains(r0);	 Catch:{ Exception -> 0x0100 }
        if (r17 != 0) goto L_0x003b;
    L_0x0057:
        r0 = r10.timeStamp;	 Catch:{ Exception -> 0x0100 }
        r18 = r0;
        r17 = (r18 > r6 ? 1 : (r18 == r6 ? 0 : -1));
        if (r17 <= 0) goto L_0x003b;
    L_0x005f:
        r0 = r10.page;	 Catch:{ Exception -> 0x0100 }
        r17 = r0;
        r17 = java.lang.Integer.valueOf(r17);	 Catch:{ Exception -> 0x0100 }
        r0 = r17;
        r9.add(r0);	 Catch:{ Exception -> 0x0100 }
        r0 = r21;
        r0 = r0.pdfView;	 Catch:{ Exception -> 0x0100 }
        r17 = r0;
        r17 = r17.viewGetDoc();	 Catch:{ Exception -> 0x0100 }
        r0 = r10.page;	 Catch:{ Exception -> 0x0100 }
        r18 = r0;
        r12 = r17.GetPage(r18);	 Catch:{ Exception -> 0x0100 }
        r12.ObjsStart();	 Catch:{ Exception -> 0x0100 }
        r0 = r21;
        r0 = r0.remoteNotes;	 Catch:{ Exception -> 0x0100 }
        r17 = r0;
        r17 = r17.iterator();	 Catch:{ Exception -> 0x0100 }
    L_0x008b:
        r18 = r17.hasNext();	 Catch:{ Exception -> 0x0100 }
        if (r18 == 0) goto L_0x003b;
    L_0x0091:
        r11 = r17.next();	 Catch:{ Exception -> 0x0100 }
        r11 = (com.radaee.reader.PDFReader.PDFNote) r11;	 Catch:{ Exception -> 0x0100 }
        r0 = r11.timeStamp;	 Catch:{ Exception -> 0x0100 }
        r18 = r0;
        r18 = (r18 > r6 ? 1 : (r18 == r6 ? 0 : -1));
        if (r18 <= 0) goto L_0x008b;
    L_0x009f:
        r0 = r11.page;	 Catch:{ Exception -> 0x0100 }
        r18 = r0;
        r0 = r10.page;	 Catch:{ Exception -> 0x0100 }
        r19 = r0;
        r0 = r18;
        r1 = r19;
        if (r0 != r1) goto L_0x008b;
    L_0x00ad:
        r0 = r11.color;	 Catch:{ Exception -> 0x0100 }
        r18 = r0;
        com.flyersoft.books.A.pdf_highlight_color = r18;	 Catch:{ Exception -> 0x0100 }
        r0 = r11.color;	 Catch:{ Exception -> 0x0100 }
        r18 = r0;
        com.flyersoft.books.A.underline_color = r18;	 Catch:{ Exception -> 0x0100 }
        r0 = r11.color;	 Catch:{ Exception -> 0x0100 }
        r18 = r0;
        com.flyersoft.books.A.strikethrough_color = r18;	 Catch:{ Exception -> 0x0100 }
        r0 = r11.color;	 Catch:{ Exception -> 0x0100 }
        r18 = r0;
        com.flyersoft.books.A.squiggly_color = r18;	 Catch:{ Exception -> 0x0100 }
        r0 = r11.start;	 Catch:{ Exception -> 0x0100 }
        r18 = r0;
        r0 = r11.end;	 Catch:{ Exception -> 0x0100 }
        r19 = r0;
        r0 = r11.type;	 Catch:{ Exception -> 0x0100 }
        r20 = r0;
        r0 = r18;
        r1 = r19;
        r2 = r20;
        r12.AddAnnotMarkup(r0, r1, r2);	 Catch:{ Exception -> 0x0100 }
        r0 = r11.note_text;	 Catch:{ Exception -> 0x0100 }
        r18 = r0;
        r18 = com.flyersoft.books.T.isNull(r18);	 Catch:{ Exception -> 0x0100 }
        if (r18 != 0) goto L_0x008b;
    L_0x00e4:
        r18 = r12.GetAnnotCount();	 Catch:{ Exception -> 0x0100 }
        if (r18 <= 0) goto L_0x010e;
    L_0x00ea:
        r18 = r12.GetAnnotCount();	 Catch:{ Exception -> 0x0100 }
        r18 = r18 + -1;
        r0 = r18;
        r4 = r12.GetAnnot(r0);	 Catch:{ Exception -> 0x0100 }
        r0 = r11.note_text;	 Catch:{ Exception -> 0x0100 }
        r18 = r0;
        r0 = r18;
        r4.SetPopupText(r0);	 Catch:{ Exception -> 0x0100 }
        goto L_0x008b;
    L_0x0100:
        r5 = move-exception;
        com.flyersoft.books.A.error(r5);	 Catch:{ all -> 0x0115 }
        com.flyersoft.books.A.pdf_highlight_color = r8;
        com.flyersoft.books.A.underline_color = r15;
        com.flyersoft.books.A.strikethrough_color = r14;
        com.flyersoft.books.A.squiggly_color = r13;
        goto L_0x0014;
    L_0x010e:
        r18 = "****updateNotesToFile add note_text ERROR: getAnnotCount==0";
        com.flyersoft.books.A.log(r18);	 Catch:{ Exception -> 0x0100 }
        goto L_0x008b;
    L_0x0115:
        r16 = move-exception;
        com.flyersoft.books.A.pdf_highlight_color = r8;
        com.flyersoft.books.A.underline_color = r15;
        com.flyersoft.books.A.strikethrough_color = r14;
        com.flyersoft.books.A.squiggly_color = r13;
        throw r16;
    L_0x011f:
        r16 = r9.size();	 Catch:{ Exception -> 0x0100 }
        if (r16 <= 0) goto L_0x012c;
    L_0x0125:
        r21.annotEnd();	 Catch:{ Exception -> 0x0100 }
        r16 = 1;
        com.flyersoft.books.A.pdfAnnotUpdated = r16;	 Catch:{ Exception -> 0x0100 }
    L_0x012c:
        com.flyersoft.books.A.pdf_highlight_color = r8;
        com.flyersoft.books.A.underline_color = r15;
        com.flyersoft.books.A.strikethrough_color = r14;
        com.flyersoft.books.A.squiggly_color = r13;
        goto L_0x0014;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.radaee.reader.PDFReader.updateNotesToFile():void");
    }

    public Integer[] getPageWordCount() {
        if (this.page_words == null || this.page_words.length != this.pdfView.viewGetDoc().GetPageCount()) {
            this.page_words = new Integer[this.pdfView.viewGetDoc().GetPageCount()];
        }
        return this.page_words;
    }

    public Integer[] getPageCharCount() {
        if (this.page_chars == null || this.page_chars.length != this.pdfView.viewGetDoc().GetPageCount()) {
            this.page_chars = new Integer[this.pdfView.viewGetDoc().GetPageCount()];
        }
        return this.page_chars;
    }

    public static boolean isFreeTextAnnot(Annotation annot) {
        if (annot == null || annot.GetType() != 3) {
            return false;
        }
        return true;
    }

    public static float getFreeTextWidth(String text, TextPaint tp) {
        float result = -1.0f;
        int start = 0;
        while (true) {
            int end2;
            int end = text.indexOf("\n", start);
            if (end == -1) {
                end2 = text.length();
            } else {
                end2 = end;
            }
            float l = Layout.getDesiredWidth(text.substring(start, end2), tp);
            if (l > result) {
                result = l;
            }
            if (end == -1) {
                return result;
            }
            start = end + 1;
        }
    }

    public static int getFreeTextLineCount(String text) {
        int result = 1;
        int start = 0;
        while (true) {
            start = text.indexOf("\n", start) + 1;
            if (start == 0) {
                return result;
            }
            result++;
        }
    }
}