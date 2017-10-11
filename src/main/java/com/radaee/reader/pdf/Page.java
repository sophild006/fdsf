package com.radaee.reader.pdf;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.InputDeviceCompat;
import com.flyersoft.books.A;

public class Page {
    public static int note_end;
    public static int note_start;
    protected int hand = 0;

    public class Annotation {
        protected int hand;
        protected Page page;

        public int GetIndexInPage() {
            if (this.page == null || this.page.hand == 0 || this.hand == 0) {
                return -1;
            }
            Page page = this.page;
            int cnt = Page.getAnnotCount(this.page.hand);
            for (int index = 0; index < cnt; index++) {
                page = this.page;
                if (this.hand == Page.getAnnot(this.page.hand, index)) {
                    return index;
                }
            }
            return -1;
        }

        public boolean MoveToPage(Page dst_page, float[] rect) {
            return Page.moveAnnot(this.page.hand, dst_page.hand, this.hand, rect);
        }

        public boolean RenderToBmp(Bitmap bitmap) {
            return Page.renderAnnotToBmp(this.page.hand, this.hand, bitmap);
        }

        public int GetFieldType() {
            return Page.getAnnotFieldType(this.page.hand, this.hand);
        }

        public String GetFieldName() {
            return Page.getAnnotFieldName(this.page.hand, this.hand);
        }

        public String GetFieldFullName() {
            return Page.getAnnotFieldFullName(this.page.hand, this.hand);
        }

        public String GetFieldFullName2() {
            return Page.getAnnotFieldFullName2(this.page.hand, this.hand);
        }

        public int GetType() {
            return Page.getAnnotType(this.page.hand, this.hand);
        }

        public boolean IsLocked() {
            return Page.isAnnotLocked(this.page.hand, this.hand);
        }

        public void setLocked(boolean lock) {
            Page.setAnnotLock(this.page.hand, this.hand, lock);
        }

        public boolean IsLockedContent() {
            return Page.isAnnotLockedContent(this.page.hand, this.hand);
        }

        public boolean IsHide() {
            return Page.isAnnotHide(this.page.hand, this.hand);
        }

        public float[] GetRect() {
            float[] rect = new float[4];
            Page.getAnnotRect(this.page.hand, this.hand, rect);
            return rect;
        }

        public void SetRect(float left, float top, float right, float bottom) {
            Page.setAnnotRect(this.page.hand, this.hand, new float[]{left, top, right, bottom});
        }

        public float[] GetMarkupRects() {
            return Page.getAnnotMarkupRects(this.page.hand, this.hand);
        }

        public void SetHide(boolean hide) {
            Page.setAnnotHide(this.page.hand, this.hand, hide);
        }

        public String GetPopupText() {
            return Page.getAnnotPopupText(this.page.hand, this.hand);
        }

        public boolean SetPopupText(String val) {
            return Page.setAnnotPopupText(this.page.hand, this.hand, val);
        }

        public String GetPopupSubject() {
            return Page.getAnnotPopupSubject(this.page.hand, this.hand);
        }

        public boolean SetPopupSubject(String val) {
            return Page.setAnnotPopupSubject(this.page.hand, this.hand, val);
        }

        public int GetDest() {
            return Page.getAnnotDest(this.page.hand, this.hand);
        }

        public String GetURI() {
            return Page.getAnnotURI(this.page.hand, this.hand);
        }

        public String GetJS() {
            return Page.getAnnotJS(this.page.hand, this.hand);
        }

        public String GetFileLink() {
            return Page.getAnnotFileLink(this.page.hand, this.hand);
        }

        public String Get3D() {
            return Page.getAnnot3D(this.page.hand, this.hand);
        }

        public String GetMovie() {
            return Page.getAnnotMovie(this.page.hand, this.hand);
        }

        public String GetSound() {
            return Page.getAnnotSound(this.page.hand, this.hand);
        }

        public String GetAttachment() {
            return Page.getAnnotAttachment(this.page.hand, this.hand);
        }

        public boolean Get3DData(String save_file) {
            return Page.getAnnot3DData(this.page.hand, this.hand, save_file);
        }

        public boolean GetMovieData(String save_file) {
            return Page.getAnnotMovieData(this.page.hand, this.hand, save_file);
        }

        public boolean GetSoundData(int[] paras, String save_file) {
            return Page.getAnnotSoundData(this.page.hand, this.hand, paras, save_file);
        }

        public boolean GetAttachmentData(String save_file) {
            return Page.getAnnotAttachmentData(this.page.hand, this.hand, save_file);
        }

        public int GetEditType() {
            return Page.getAnnotEditType(this.page.hand, this.hand);
        }

        public int GetEditMaxlen() {
            return Page.getAnnotEditMaxlen(this.page.hand, this.hand);
        }

        public boolean GetEditTextRect(float[] rect) {
            return Page.getAnnotEditTextRect(this.page.hand, this.hand, rect);
        }

        public float GetEditTextSize() {
            return Page.getAnnotEditTextSize(this.page.hand, this.hand);
        }

        public String GetEditTextFormat() {
            return Page.getAnnotEditTextFormat(this.page.hand, this.hand);
        }

        public int GetEditTextColor() {
            return Page.getAnnotEditTextColor(this.page.hand, this.hand);
        }

        public boolean SetEditTextColor(int color) {
            return Page.setAnnotEditTextColor(this.page.hand, this.hand, color);
        }

        public String GetEditText() {
            return Page.getAnnotEditText(this.page.hand, this.hand);
        }

        public boolean SetEditText(String text) {
            return Page.setAnnotEditText(this.page.hand, this.hand, text);
        }

        public int GetComboItemCount() {
            return Page.getAnnotComboItemCount(this.page.hand, this.hand);
        }

        public String GetComboItem(int item) {
            return Page.getAnnotComboItem(this.page.hand, this.hand, item);
        }

        public int GetComboItemSel() {
            return Page.getAnnotComboItemSel(this.page.hand, this.hand);
        }

        public boolean SetComboItem(int item) {
            return Page.setAnnotComboItem(this.page.hand, this.hand, item);
        }

        public int GetListItemCount() {
            return Page.getAnnotListItemCount(this.page.hand, this.hand);
        }

        public String GetListItem(int item) {
            return Page.getAnnotListItem(this.page.hand, this.hand, item);
        }

        public int[] GetListSels() {
            return Page.getAnnotListSels(this.page.hand, this.hand);
        }

        public boolean SetListSels(int[] items) {
            return Page.setAnnotListSels(this.page.hand, this.hand, items);
        }

        public int GetCheckStatus() {
            return Page.getAnnotCheckStatus(this.page.hand, this.hand);
        }

        public boolean SetCheckValue(boolean check) {
            return Page.setAnnotCheckValue(this.page.hand, this.hand, check);
        }

        public boolean SetRadio() {
            return Page.setAnnotRadio(this.page.hand, this.hand);
        }

        public int GetSignStatus() {
            return Page.getAnnotSignStatus(this.page.hand, this.hand);
        }

        public boolean GetReset() {
            return Page.getAnnotReset(this.page.hand, this.hand);
        }

        public boolean SetReset() {
            return Page.setAnnotReset(this.page.hand, this.hand);
        }

        public String GetSubmitTarget() {
            return Page.getAnnotSubmitTarget(this.page.hand, this.hand);
        }

        public String GetSubmitPara() {
            return Page.getAnnotSubmitPara(this.page.hand, this.hand);
        }

        public int GetFillColor() {
            return Page.getAnnotFillColor(this.page.hand, this.hand);
        }

        public boolean SetFillColor(int color) {
            return Page.setAnnotFillColor(this.page.hand, this.hand, color);
        }

        public int GetStrokeColor() {
            return Page.getAnnotStrokeColor(this.page.hand, this.hand);
        }

        public boolean SetStrokeColor(int color) {
            return Page.setAnnotStrokeColor(this.page.hand, this.hand, color);
        }

        public float GetStrokeWidth() {
            return Page.getAnnotStrokeWidth(this.page.hand, this.hand);
        }

        public boolean SetStrokeWidth(float width) {
            return Page.setAnnotStrokeWidth(this.page.hand, this.hand, width);
        }

        public Path GetInkPath() {
            int ret = Page.getAnnotInkPath(this.page.hand, this.hand);
            if (ret == 0) {
                return null;
            }
            Path path = new Path();
            path.m_hand = ret;
            return path;
        }

        public boolean SetInkPath(Path path) {
            if (path == null) {
                return false;
            }
            return Page.setAnnotInkPath(this.page.hand, this.hand, path.m_hand);
        }

        public Path GetPolygonPath() {
            int ret = Page.getAnnotPolygonPath(this.page.hand, this.hand);
            if (ret == 0) {
                return null;
            }
            Path path = new Path();
            path.m_hand = ret;
            return path;
        }

        public boolean SetPolygonPath(Path path) {
            if (path == null) {
                return false;
            }
            return Page.setAnnotPolygonPath(this.page.hand, this.hand, path.m_hand);
        }

        public Path GetPolylinePath() {
            int ret = Page.getAnnotPolylinePath(this.page.hand, this.hand);
            if (ret == 0) {
                return null;
            }
            Path path = new Path();
            path.m_hand = ret;
            return path;
        }

        public boolean SetPolylinePath(Path path) {
            if (path == null) {
                return false;
            }
            return Page.setAnnotPolylinePath(this.page.hand, this.hand, path.m_hand);
        }

        public boolean SetIcon(int icon) {
            return Page.setAnnotIcon(this.page.hand, this.hand, icon);
        }

        public boolean SetIcon(String name, PageContent content) {
            return Page.setAnnotIcon2(this.page.hand, this.hand, name, content.hand);
        }

        public int GetIcon() {
            return Page.getAnnotInkPath(this.page.hand, this.hand);
        }

        public boolean RemoveFromPage() {
            boolean ret = Page.removeAnnot(this.page.hand, this.hand);
            this.hand = 0;
            return ret;
        }
    }

    public class Finder {
        protected int hand;

        public int GetCount() {
            return Page.findGetCount(this.hand);
        }

        public int GetFirstChar(int index) {
            return Page.findGetFirstChar(this.hand, index);
        }

        public void Close() {
            Page.findClose(this.hand);
            this.hand = 0;
        }
    }

    public class ResFont {
        protected int hand;
    }

    public class ResGState {
        protected int hand;
    }

    public class ResImage {
        protected int hand;
    }

    private static native boolean addAnnotAttachment(int i, String str, int i2, float[] fArr);

    private static native boolean addAnnotBitmap(int i, Bitmap bitmap, boolean z, float[] fArr);

    private static native boolean addAnnotEditbox(int i, int i2, float[] fArr, int i3, float f, int i4, float f2, int i5);

    private static native boolean addAnnotEditbox2(int i, float[] fArr, int i2, float f, int i3, float f2, int i4);

    private static native boolean addAnnotEllipse(int i, int i2, float[] fArr, float f, int i3, int i4);

    private static native boolean addAnnotEllipse2(int i, float[] fArr, float f, int i2, int i3);

    private static native boolean addAnnotGlyph(int i, int i2, int i3, int i4, boolean z);

    private static native boolean addAnnotGoto(int i, float[] fArr, int i2, float f);

    private static native boolean addAnnotHWriting(int i, int i2, int i3, float f, float f2);

    private static native boolean addAnnotInk(int i, int i2, int i3, float f, float f2);

    private static native boolean addAnnotInk2(int i, int i2);

    private static native boolean addAnnotLine(int i, int i2, float[] fArr, float[] fArr2, int i3, int i4, float f, int i5, int i6);

    private static native boolean addAnnotLine2(int i, float[] fArr, float[] fArr2, int i2, int i3, float f, int i4, int i5);

    private static native boolean addAnnotMarkup(int i, int i2, float[] fArr, int i3, int i4);

    private static native boolean addAnnotMarkup2(int i, int i2, int i3, int i4, int i5);

    private static native boolean addAnnotPolygon(int i, int i2, int i3, int i4, float f);

    private static native boolean addAnnotPolyline(int i, int i2, int i3, int i4, int i5, int i6, float f);

    private static native boolean addAnnotRect(int i, int i2, float[] fArr, float f, int i3, int i4);

    private static native boolean addAnnotRect2(int i, float[] fArr, float f, int i2, int i3);

    private static native boolean addAnnotStamp(int i, float[] fArr, int i2);

    private static native boolean addAnnotText(int i, float[] fArr);

    private static native boolean addAnnotURI(int i, float[] fArr, String str);

    private static native boolean addContent(int i, int i2, boolean z);

    private static native int addResFont(int i, int i2);

    private static native int addResGState(int i, int i2);

    private static native int addResImage(int i, int i2);

    private static native void close(int i);

    private static native boolean copyAnnot(int i, int i2, float[] fArr);

    private static native int findClose(int i);

    private static native int findGetCount(int i);

    private static native int findGetFirstChar(int i, int i2);

    private static native int findOpen(int i, String str, boolean z, boolean z2);

    private static native int getAnnot(int i, int i2);

    private static native String getAnnot3D(int i, int i2);

    private static native boolean getAnnot3DData(int i, int i2, String str);

    private static native String getAnnotAttachment(int i, int i2);

    private static native boolean getAnnotAttachmentData(int i, int i2, String str);

    private static native int getAnnotCheckStatus(int i, int i2);

    private static native String getAnnotComboItem(int i, int i2, int i3);

    private static native int getAnnotComboItemCount(int i, int i2);

    private static native int getAnnotComboItemSel(int i, int i2);

    private static native int getAnnotCount(int i);

    private static native int getAnnotDest(int i, int i2);

    private static native int getAnnotEditMaxlen(int i, int i2);

    private static native String getAnnotEditText(int i, int i2);

    private static native int getAnnotEditTextColor(int i, int i2);

    private static native String getAnnotEditTextFormat(int i, int i2);

    private static native boolean getAnnotEditTextRect(int i, int i2, float[] fArr);

    private static native float getAnnotEditTextSize(int i, int i2);

    private static native int getAnnotEditType(int i, int i2);

    private static native String getAnnotFieldFullName(int i, int i2);

    private static native String getAnnotFieldFullName2(int i, int i2);

    private static native String getAnnotFieldName(int i, int i2);

    private static native int getAnnotFieldType(int i, int i2);

    private static native String getAnnotFileLink(int i, int i2);

    private static native int getAnnotFillColor(int i, int i2);

    private static native int getAnnotFromPoint(int i, float f, float f2);

    private static native int getAnnotIcon(int i, int i2);

    private static native int getAnnotInkPath(int i, int i2);

    private static native String getAnnotJS(int i, int i2);

    private static native String getAnnotListItem(int i, int i2, int i3);

    private static native int getAnnotListItemCount(int i, int i2);

    private static native int[] getAnnotListSels(int i, int i2);

    private static native float[] getAnnotMarkupRects(int i, int i2);

    private static native String getAnnotMovie(int i, int i2);

    private static native boolean getAnnotMovieData(int i, int i2, String str);

    private static native int getAnnotPolygonPath(int i, int i2);

    private static native int getAnnotPolylinePath(int i, int i2);

    private static native String getAnnotPopupSubject(int i, int i2);

    private static native String getAnnotPopupText(int i, int i2);

    private static native void getAnnotRect(int i, int i2, float[] fArr);

    private static native boolean getAnnotReset(int i, int i2);

    private static native int getAnnotSignStatus(int i, int i2);

    private static native String getAnnotSound(int i, int i2);

    private static native boolean getAnnotSoundData(int i, int i2, int[] iArr, String str);

    private static native int getAnnotStrokeColor(int i, int i2);

    private static native float getAnnotStrokeWidth(int i, int i2);

    private static native String getAnnotSubmitPara(int i, int i2);

    private static native String getAnnotSubmitTarget(int i, int i2);

    private static native int getAnnotType(int i, int i2);

    private static native String getAnnotURI(int i, int i2);

    private static native float[] getCropBox(int i);

    private static native float[] getMediaBox(int i);

    private static native int getRotate(int i);

    private static native boolean isAnnotHide(int i, int i2);

    private static native boolean isAnnotLocked(int i, int i2);

    private static native boolean isAnnotLockedContent(int i, int i2);

    private static native boolean moveAnnot(int i, int i2, int i3, float[] fArr);

    private static native int objsAlignWord(int i, int i2, int i3);

    private static native int objsGetCharCount(int i);

    private static native String objsGetCharFontName(int i, int i2);

    private static native int objsGetCharIndex(int i, float[] fArr);

    private static native void objsGetCharRect(int i, int i2, float[] fArr);

    private static native String objsGetString(int i, int i2, int i3);

    private static native void objsStart(int i, boolean z);

    private static native boolean reflow(int i, int i2, float f, float f2);

    private static native int reflowGetCharColor(int i, int i2, int i3);

    private static native int reflowGetCharCount(int i, int i2);

    private static native String reflowGetCharFont(int i, int i2, int i3);

    private static native float reflowGetCharHeight(int i, int i2, int i3);

    private static native void reflowGetCharRect(int i, int i2, int i3, float[] fArr);

    private static native int reflowGetCharUnicode(int i, int i2, int i3);

    private static native float reflowGetCharWidth(int i, int i2, int i3);

    private static native int reflowGetParaCount(int i);

    private static native String reflowGetText(int i, int i2, int i3, int i4, int i5);

    private static native float reflowStart(int i, float f, float f2, boolean z);

    private static native boolean reflowToBmp(int i, Bitmap bitmap, float f, float f2);

    private static native boolean removeAnnot(int i, int i2);

    private static native boolean render(int i, int i2, int i3, int i4);

    private static native boolean renderAnnotToBmp(int i, int i2, Bitmap bitmap);

    private static native void renderCancel(int i);

    private static native boolean renderIsFinished(int i);

    private static native void renderPrepare(int i, int i2);

    private static native boolean renderThumb(int i, Bitmap bitmap);

    private static native boolean renderThumbToBuf(int i, int[] iArr, int i2, int i3);

    private static native boolean renderToBmp(int i, Bitmap bitmap, int i2, int i3);

    private static native boolean renderToBuf(int i, int[] iArr, int i2, int i3, int i4, int i5);

    private static native boolean setAnnotCheckValue(int i, int i2, boolean z);

    private static native boolean setAnnotComboItem(int i, int i2, int i3);

    private static native boolean setAnnotEditText(int i, int i2, String str);

    private static native boolean setAnnotEditTextColor(int i, int i2, int i3);

    private static native boolean setAnnotFillColor(int i, int i2, int i3);

    private static native void setAnnotHide(int i, int i2, boolean z);

    private static native boolean setAnnotIcon(int i, int i2, int i3);

    private static native boolean setAnnotIcon2(int i, int i2, String str, int i3);

    private static native boolean setAnnotInkPath(int i, int i2, int i3);

    private static native boolean setAnnotListSels(int i, int i2, int[] iArr);

    private static native void setAnnotLock(int i, int i2, boolean z);

    private static native boolean setAnnotPolygonPath(int i, int i2, int i3);

    private static native boolean setAnnotPolylinePath(int i, int i2, int i3);

    private static native boolean setAnnotPopupSubject(int i, int i2, String str);

    private static native boolean setAnnotPopupText(int i, int i2, String str);

    private static native boolean setAnnotRadio(int i, int i2);

    private static native void setAnnotRect(int i, int i2, float[] fArr);

    private static native boolean setAnnotReset(int i, int i2);

    private static native boolean setAnnotStrokeColor(int i, int i2, int i3);

    private static native boolean setAnnotStrokeWidth(int i, int i2, float f);

    public float[] GetCropBox() {
        return getCropBox(this.hand);
    }

    public float[] GetMediaBox() {
        return getMediaBox(this.hand);
    }

    public void Close() {
        close(this.hand);
        this.hand = 0;
    }

    public void RenderPrePare(int dib) {
        renderPrepare(this.hand, dib);
    }

    public boolean Render(int dib, Matrix mat) {
        return render(this.hand, dib, mat.hand, Global.render_mode);
    }

    public boolean RenderToBmp(Bitmap bitmap, Matrix mat) {
        return renderToBmp(this.hand, bitmap, mat.hand, Global.render_mode);
    }

    public boolean RenderToBuf(int[] data, int w, int h, Matrix mat) {
        return renderToBuf(this.hand, data, w, h, mat.hand, Global.render_mode);
    }

    public void Render_Normal(int dib, Matrix mat) {
        render(this.hand, dib, mat.hand, 1);
    }

    public void RenderCancel() {
        renderCancel(this.hand);
    }

    public boolean RenderThumb(Bitmap bmp) {
        return renderThumb(this.hand, bmp);
    }

    public boolean RenderThumbToBuf(int[] data, int w, int h) {
        return renderThumbToBuf(this.hand, data, w, h);
    }

    public boolean RenderIsFinished() {
        return renderIsFinished(this.hand);
    }

    public void ObjsStart() {
        objsStart(this.hand, Global.selRTOL);
    }

    public String ObjsGetString(int from, int to) {
        return objsGetString(this.hand, from, to);
    }

    public int ObjsAlignWord(int from, int dir) {
        return objsAlignWord(this.hand, from, dir);
    }

    public void ObjsGetCharRect(int index, float[] vals) {
        objsGetCharRect(this.hand, index, vals);
    }

    public String ObjsGetCharFontName(int index) {
        return objsGetCharFontName(this.hand, index);
    }

    public int ObjsGetCharCount() {
        return objsGetCharCount(this.hand);
    }

    public int ObjsGetCharIndex(float[] pt) {
        return objsGetCharIndex(this.hand, pt);
    }

    public Finder FindOpen(String str, boolean match_case, boolean whole_word) {
        int ret = findOpen(this.hand, str, match_case, whole_word);
        if (ret == 0) {
            return null;
        }
        Finder find = new Finder();
        find.hand = ret;
        return find;
    }

    public int GetRotate() {
        return getRotate(this.hand);
    }

    public int GetAnnotCount() {
        return getAnnotCount(this.hand);
    }

    public Annotation GetAnnot(int index) {
        int ret = getAnnot(this.hand, index);
        if (ret == 0) {
            return null;
        }
        Annotation annot = new Annotation();
        annot.hand = ret;
        annot.page = this;
        return annot;
    }

    public Annotation GetAnnotFromPoint(float x, float y) {
        int ret = getAnnotFromPoint(this.hand, x, y);
        if (ret == 0) {
            return null;
        }
        Annotation annot = new Annotation();
        annot.hand = ret;
        annot.page = this;
        return annot;
    }

    public boolean AddAnnotGoto(float[] rect, int pageno, float top) {
        return addAnnotGoto(this.hand, rect, pageno, top);
    }

    public boolean AddAnnotURI(float[] rect, String uri) {
        return addAnnotURI(this.hand, rect, uri);
    }

    public boolean AddAnnotStamp(float[] rect, int icon) {
        return addAnnotStamp(this.hand, rect, icon);
    }

    public boolean AddAnnotInk(Matrix mat, Ink ink, float orgx, float orgy) {
        return addAnnotInk(this.hand, mat.hand, ink.hand, orgx, orgy);
    }

    public boolean AddAnnotHWriting(Matrix mat, HWriting hwriting, float orgx, float orgy) {
        return addAnnotHWriting(this.hand, mat.hand, hwriting.hand, orgx, orgy);
    }

    public boolean AddAnnotGlyph(Matrix mat, Path path, int color, boolean winding) {
        return addAnnotGlyph(this.hand, mat.hand, path.m_hand, color, winding);
    }

    public boolean AddAnnotRect(Matrix mat, float[] rect, float width, int color, int fill_color) {
        return addAnnotRect(this.hand, mat.hand, rect, width, color, fill_color);
    }

    public boolean AddAnnotLine(Matrix mat, float[] pt1, float[] pt2, int style1, int style2, float width, int color, int fill_color) {
        return addAnnotLine(this.hand, mat.hand, pt1, pt2, style1, style2, width, color, fill_color);
    }

    public boolean AddAnnotEllipse(Matrix mat, float[] rect, float width, int color, int fill_color) {
        return addAnnotEllipse(this.hand, mat.hand, rect, width, color, fill_color);
    }

    public boolean AddAnnotMarkup(Matrix mat, float[] rects, int type) {
        int color = InputDeviceCompat.SOURCE_ANY;
        if (type == 1) {
            color = -16777024;
        }
        if (type == 2) {
            color = -4194304;
        }
        if (type == 4) {
            color = -16728064;
        }
        return addAnnotMarkup(this.hand, mat.hand, rects, color, type);
    }

    public boolean AddAnnotInk(Ink ink) {
        return addAnnotInk2(this.hand, ink.hand);
    }

    public boolean AddAnnotLine(float[] pt1, float[] pt2, int style1, int style2, float width, int color, int icolor) {
        return addAnnotLine2(this.hand, pt1, pt2, style1, style2, width, color, icolor);
    }

    public boolean AddAnnotRect(float[] rect, float width, int color, int fill_color) {
        return addAnnotRect2(this.hand, rect, width, color, fill_color);
    }

    public boolean AddAnnotEllipse(float[] rect, float width, int color, int fill_color) {
        return addAnnotEllipse2(this.hand, rect, width, color, fill_color);
    }

    public boolean AddAnnotEditbox(float[] rect, int line_clr, float line_w, int fill_clr, float tsize, int text_clr) {
        return addAnnotEditbox2(this.hand, rect, line_clr, line_w, fill_clr, tsize, text_clr);
    }

    public boolean AddAnnotPolygon(Path path, int color, int fill_color, float width) {
        return addAnnotPolygon(this.hand, path.m_hand, color, fill_color, width);
    }

    public boolean AddAnnotPolyline(Path path, int style1, int style2, int color, int fill_color, float width) {
        return addAnnotPolyline(this.hand, path.m_hand, style1, style2, color, fill_color, width);
    }

    public boolean AddAnnotMarkup(int cindex1, int cindex2, int type) {
        int color = A.pdf_highlight_color;
        if (type == 1) {
            color = A.underline_color;
        }
        if (type == 2) {
            color = A.strikethrough_color;
        }
        if (type == 4) {
            color = A.squiggly_color;
        }
        if (type > 0) {
            color = Color.argb(255, Color.red(color), Color.green(color), Color.blue(color));
        } else {
            color = fixHighlightColor(color);
        }
        note_start = cindex1;
        note_end = cindex2;
        return addAnnotMarkup2(this.hand, cindex1, cindex2, color, type);
    }

    public static int fixHighlightColor(int color) {
        return color;
    }

    public boolean AddAnnotBitmap(Bitmap bitmap, boolean has_alpha, float[] rect) {
        return addAnnotBitmap(this.hand, bitmap, has_alpha, rect);
    }

    public boolean AddAnnotAttachment(String path, int icon, float[] rect) {
        return addAnnotAttachment(this.hand, path, icon, rect);
    }

    public boolean AddAnnotText(float[] pt) {
        return addAnnotText(this.hand, pt);
    }

    public boolean AddAnnotEditbox(Matrix mat, float[] rect, int line_clr, float line_w, int fill_clr, int text_clr, float tsize) {
        return addAnnotEditbox(this.hand, mat.hand, rect, Color.argb(Color.alpha(text_clr), 0, 0, 0), line_w, text_clr, tsize, text_clr);
    }

    public float ReflowStart(float width, float scale, boolean enable_images) {
        return reflowStart(this.hand, width, scale, enable_images);
    }

    public boolean Reflow(int dib, float orgx, float orgy) {
        return reflow(this.hand, dib, orgx, orgy);
    }

    public boolean ReflowToBmp(Bitmap bitmap, float orgx, float orgy) {
        return reflowToBmp(this.hand, bitmap, orgx, orgy);
    }

    public int ReflowGetParaCount() {
        return reflowGetParaCount(this.hand);
    }

    public int ReflowGetCharCount(int iparagraph) {
        if (iparagraph < 0 || iparagraph >= reflowGetParaCount(this.hand)) {
            return 0;
        }
        return reflowGetCharCount(this.hand, iparagraph);
    }

    public float ReflowGetCharWidth(int iparagraph, int ichar) {
        if (ichar < 0 || ichar >= ReflowGetCharCount(iparagraph)) {
            return 0.0f;
        }
        return reflowGetCharWidth(this.hand, iparagraph, ichar);
    }

    public float ReflowGetCharHeight(int iparagraph, int ichar) {
        if (ichar < 0 || ichar >= ReflowGetCharCount(iparagraph)) {
            return 0.0f;
        }
        return reflowGetCharHeight(this.hand, iparagraph, ichar);
    }

    public int ReflowGetCharColor(int iparagraph, int ichar) {
        if (ichar < 0 || ichar >= ReflowGetCharCount(iparagraph)) {
            return 0;
        }
        return reflowGetCharColor(this.hand, iparagraph, ichar);
    }

    public int ReflowGetCharUnicode(int iparagraph, int ichar) {
        if (ichar < 0 || ichar >= ReflowGetCharCount(iparagraph)) {
            return 0;
        }
        return reflowGetCharUnicode(this.hand, iparagraph, ichar);
    }

    public String ReflowGetCharFont(int iparagraph, int ichar) {
        if (ichar < 0 || ichar >= ReflowGetCharCount(iparagraph)) {
            return null;
        }
        return reflowGetCharFont(this.hand, iparagraph, ichar);
    }

    public void ReflowGetCharRect(int iparagraph, int ichar, float[] rect) {
        if (ichar >= 0 && ichar < ReflowGetCharCount(iparagraph)) {
            reflowGetCharRect(this.hand, iparagraph, ichar, rect);
        }
    }

    public String ReflowGetText(int iparagraph1, int ichar1, int iparagraph2, int ichar2) {
        if (ichar1 < 0 || ichar1 >= ReflowGetCharCount(iparagraph1) || ichar2 < 0 || ichar1 >= ReflowGetCharCount(iparagraph2)) {
            return null;
        }
        return reflowGetText(this.hand, iparagraph1, ichar1, iparagraph2, ichar2);
    }

    public ResFont AddResFont(DocFont font) {
        if (font == null) {
            return null;
        }
        int ret = addResFont(this.hand, font.hand);
        if (ret == 0) {
            return null;
        }
        ResFont fnt = new ResFont();
        fnt.hand = ret;
        return fnt;
    }

    public ResImage AddResImage(DocImage image) {
        if (image == null) {
            return null;
        }
        int ret = addResImage(this.hand, image.hand);
        if (ret == 0) {
            return null;
        }
        ResImage img = new ResImage();
        img.hand = ret;
        return img;
    }

    public ResGState AddResGState(DocGState gstate) {
        if (gstate == null) {
            return null;
        }
        int ret = addResGState(this.hand, gstate.hand);
        if (ret == 0) {
            return null;
        }
        ResGState gs = new ResGState();
        gs.hand = ret;
        return gs;
    }

    public boolean AddContent(PageContent content, boolean flush) {
        if (content == null) {
            return false;
        }
        return addContent(this.hand, content.hand, flush);
    }

    public boolean CopyAnnot(Annotation annot, float[] rect) {
        return copyAnnot(this.hand, annot.hand, rect);
    }
}