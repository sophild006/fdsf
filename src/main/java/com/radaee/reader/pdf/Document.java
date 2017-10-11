package com.radaee.reader.pdf;

import android.graphics.Bitmap;

public class Document {
    protected int hand_val = 0;
    private int page_count = 0;

    public interface PDFFontDelegate {
        String GetExtFont(String str, String str2, int i, int[] iArr);
    }

    public class DocFont {
        Document doc;
        protected int hand;

        public float GetAscent() {
            return Document.getFontAscent(this.doc.hand_val, this.hand);
        }

        public float GetDescent() {
            return Document.getFontDescent(this.doc.hand_val, this.hand);
        }
    }

    public class DocGState {
        Document doc;
        protected int hand;

        public boolean SetFillAlpha(int alpha) {
            return Document.setGStateFillAlpha(this.doc.hand_val, this.hand, alpha);
        }

        public boolean SetStrokeAlpha(int alpha) {
            return Document.setGStateStrokeAlpha(this.doc.hand_val, this.hand, alpha);
        }
    }

    public class DocImage {
        protected int hand;
    }

    public class ImportContext {
        protected int hand;
        protected Document m_doc;

        protected ImportContext(Document doc, int value) {
            this.hand = value;
            this.m_doc = doc;
        }

        public void Destroy() {
            Document.importEnd(this.m_doc.hand_val, this.hand);
            this.hand = 0;
        }
    }

    public class Outline {
        protected Document doc;
        protected int hand;

        public String GetTitle() {
            return Document.getOutlineTitle(this.doc.hand_val, this.hand);
        }

        public boolean SetTitle(String title) {
            return Document.setOutlineTitle(this.doc.hand_val, this.hand, title);
        }

        public Outline GetNext() {
            int ret = Document.getOutlineNext(this.doc.hand_val, this.hand);
            if (ret == 0) {
                return null;
            }
            Outline ol = new Outline();
            ol.hand = ret;
            ol.doc = this.doc;
            return ol;
        }

        public Outline GetChild() {
            int ret = Document.getOutlineChild(this.doc.hand_val, this.hand);
            if (ret == 0) {
                return null;
            }
            Outline ol = new Outline();
            ol.hand = ret;
            ol.doc = this.doc;
            return ol;
        }

        public int GetDest() {
            return Document.getOutlineDest(this.doc.hand_val, this.hand);
        }

        public String GetURI() {
            return Document.getOutlineURI(this.doc.hand_val, this.hand);
        }

        public String GetFileLink() {
            return Document.getOutlineFileLink(this.doc.hand_val, this.hand);
        }

        public boolean AddNext(String label, int pageno, float top) {
            return Document.addOutlineNext(this.doc.hand_val, this.hand, label, pageno, top);
        }

        public boolean AddChild(String label, int pageno, float top) {
            return Document.addOutlineChild(this.doc.hand_val, this.hand, label, pageno, top);
        }

        public boolean RemoveFromDoc() {
            boolean ret = Document.removeOutline(this.doc.hand_val, this.hand);
            this.hand = 0;
            return ret;
        }
    }

    public interface PDFStream {
        int get_size();

        int read(byte[] bArr);

        void seek(int i);

        int tell();

        int write(byte[] bArr);

        boolean writeable();
    }

    private static native boolean addOutlineChild(int i, int i2, String str, int i3, float f);

    private static native boolean addOutlineNext(int i, int i2, String str, int i3, float f);

    private static native boolean canSave(int i);

    private static native boolean changePageRect(int i, int i2, float f, float f2, float f3, float f4);

    private static native int checkSignByteRange(int i);

    private static native void close(int i);

    private static native int create(String str);

    private static native int createForStream(PDFStream pDFStream);

    private static native String exportForm(int i);

    private static native float getFontAscent(int i, int i2);

    private static native float getFontDescent(int i, int i2);

    private static native String getMeta(int i, String str);

    private static native int getOutlineChild(int i, int i2);

    private static native int getOutlineDest(int i, int i2);

    private static native String getOutlineFileLink(int i, int i2);

    private static native int getOutlineNext(int i, int i2);

    private static native String getOutlineTitle(int i, int i2);

    private static native String getOutlineURI(int i, int i2);

    private static native int getPage(int i, int i2);

    private static native int getPageCount(int i);

    private static native float getPageHeight(int i, int i2);

    private static native float getPageWidth(int i, int i2);

    private static native int getPerm(int i);

    private static native int getPermission(int i);

    private static native int[] getSignByteRange(int i);

    private static native byte[] getSignContents(int i);

    private static native String getSignFilter(int i);

    private static native String getSignSubFilter(int i);

    private static native void importEnd(int i, int i2);

    private static native boolean importPage(int i, int i2, int i3, int i4);

    private static native int importStart(int i, int i2);

    private static native boolean isEncrypted(int i);

    private static native boolean movePage(int i, int i2, int i3);

    private static native int newFontCID(int i, String str, int i2);

    private static native int newGState(int i);

    private static native int newImage(int i, Bitmap bitmap, boolean z);

    private static native int newImageJPEG(int i, String str);

    private static native int newImageJPX(int i, String str);

    private static native int newPage(int i, int i2, float f, float f2);

    private static native int open(String str, String str2);

    private static native int openMem(byte[] bArr, String str);

    private static native int openStream(PDFStream pDFStream, String str);

    private static native boolean removeOutline(int i, int i2);

    private static native boolean removePage(int i, int i2);

    private static native boolean save(int i);

    private static native boolean saveAs(int i, String str);

    private static native boolean setCache(int i, String str);

    private static native void setFontDel(int i, PDFFontDelegate pDFFontDelegate);

    private static native boolean setGStateFillAlpha(int i, int i2, int i3);

    private static native boolean setGStateStrokeAlpha(int i, int i2, int i3);

    private static native boolean setMeta(int i, String str, String str2);

    private static native boolean setOutlineTitle(int i, int i2, String str);

    private static native boolean setPageRotate(int i, int i2, int i3);

    public Document(int[] vals) {
        if (vals != null) {
            this.hand_val = vals[0];
            this.page_count = vals[1];
        }
    }

    public int[] getVals() {
        return new int[]{this.hand_val, 1};
    }

    private int getOutlineRoot(int hand) {
        return getOutlineNext(this.hand_val, 0);
    }

    public boolean is_opened() {
        return this.hand_val != 0;
    }

    public int Create(String path) {
        if (this.hand_val != 0) {
            return 0;
        }
        this.hand_val = create(path);
        if (this.hand_val > 0 || this.hand_val < -10) {
            this.page_count = getPageCount(this.hand_val);
            return 0;
        }
        int ret = this.hand_val;
        this.hand_val = 0;
        this.page_count = 0;
        return ret;
    }

    public int CreateForStream(PDFStream stream) {
        if (this.hand_val != 0) {
            return 0;
        }
        this.hand_val = createForStream(stream);
        if (this.hand_val > 0 || this.hand_val < -10) {
            this.page_count = getPageCount(this.hand_val);
            return 0;
        }
        int ret = this.hand_val;
        this.hand_val = 0;
        this.page_count = 0;
        return ret;
    }

    public boolean SetCache(String path) {
        return setCache(this.hand_val, path);
    }

    public void SetFontDel(PDFFontDelegate del) {
        setFontDel(this.hand_val, del);
    }

    public int Open(String path, String password) {
        if (this.hand_val != 0) {
            return 0;
        }
        this.hand_val = open(path, password);
        if (this.hand_val > 0 || this.hand_val < -10) {
            this.page_count = getPageCount(this.hand_val);
            return 0;
        }
        int ret = this.hand_val;
        this.hand_val = 0;
        this.page_count = 0;
        return ret;
    }

    public int OpenMem(byte[] data, String password) {
        if (this.hand_val != 0) {
            return 0;
        }
        this.hand_val = openMem(data, password);
        if (this.hand_val > 0 || this.hand_val < -10) {
            this.page_count = getPageCount(this.hand_val);
            return 0;
        }
        int ret = this.hand_val;
        this.hand_val = 0;
        this.page_count = 0;
        return ret;
    }

    public int OpenStream(PDFStream stream, String password) {
        if (this.hand_val != 0) {
            return 0;
        }
        this.hand_val = openStream(stream, password);
        if (this.hand_val > 0 || this.hand_val < -10) {
            this.page_count = getPageCount(this.hand_val);
            return 0;
        }
        int ret = this.hand_val;
        this.hand_val = 0;
        this.page_count = 0;
        return ret;
    }

    public int GetPermission() {
        return getPermission(this.hand_val);
    }

    public String ExportForm() {
        return exportForm(this.hand_val);
    }

    public int GetPerm() {
        return getPerm(this.hand_val);
    }

    public void Close() {
        if (this.hand_val != 0) {
            close(this.hand_val);
        }
        this.hand_val = 0;
        this.page_count = 0;
    }

    public Page GetPage(int pageno) {
        Page page = null;
        if (this.hand_val != 0) {
            int hand = getPage(this.hand_val, pageno);
            if (hand != 0) {
                page = new Page();
                if (page != null) {
                    page.hand = hand;
                }
            }
        }
        return page;
    }

    public int GetPageCount() {
        return this.page_count;
    }

    public float GetPageWidth(int pageno) {
        float w = getPageWidth(this.hand_val, pageno);
        if (w <= 0.0f) {
            return 1.0f;
        }
        return w;
    }

    public float GetPageHeight(int pageno) {
        float h = getPageHeight(this.hand_val, pageno);
        if (h <= 0.0f) {
            return 1.0f;
        }
        return h;
    }

    public String GetMeta(String tag) {
        return getMeta(this.hand_val, tag);
    }

    public boolean SetMeta(String tag, String val) {
        return setMeta(this.hand_val, tag, val);
    }

    public Outline GetOutlines() {
        int ret = getOutlineRoot(this.hand_val);
        if (ret == 0) {
            return null;
        }
        Outline ol = new Outline();
        ol.doc = this;
        ol.hand = ret;
        return ol;
    }

    public boolean CanSave() {
        return canSave(this.hand_val);
    }

    public boolean Save() {
        return save(this.hand_val);
    }

    public boolean SaveAs(String path) {
        return saveAs(this.hand_val, path);
    }

    public boolean IsEncrypted() {
        return isEncrypted(this.hand_val);
    }

    public boolean NewRootOutline(String label, int pageno, float top) {
        return addOutlineChild(this.hand_val, 0, label, pageno, top);
    }

    public ImportContext ImportStart(Document src) {
        if (src == null) {
            return null;
        }
        int hand = importStart(this.hand_val, src.hand_val);
        if (hand != 0) {
            return new ImportContext(this, hand);
        }
        return null;
    }

    public boolean ImportPage(ImportContext ctx, int srcno, int dstno) {
        if (ctx == null) {
            return false;
        }
        return importPage(this.hand_val, ctx.hand, srcno, dstno);
    }

    public Page NewPage(int pageno, float w, float h) {
        int ret = newPage(this.hand_val, pageno, w, h);
        if (ret == 0) {
            return null;
        }
        Page page = new Page();
        page.hand = ret;
        return page;
    }

    public boolean RemovePage(int pageno) {
        return removePage(this.hand_val, pageno);
    }

    public boolean MovePage(int pageno1, int pageno2) {
        return movePage(this.hand_val, pageno1, pageno2);
    }

    public DocFont NewFontCID(String font_name, int style) {
        int ret = newFontCID(this.hand_val, font_name, style);
        if (ret == 0) {
            return null;
        }
        DocFont font = new DocFont();
        font.hand = ret;
        font.doc = this;
        return font;
    }

    public DocGState NewGState() {
        int ret = newGState(this.hand_val);
        if (ret == 0) {
            return null;
        }
        DocGState gs = new DocGState();
        gs.hand = ret;
        gs.doc = this;
        return gs;
    }

    public DocImage NewImage(Bitmap bmp, boolean has_alpha) {
        int ret = newImage(this.hand_val, bmp, has_alpha);
        if (ret == 0) {
            return null;
        }
        DocImage img = new DocImage();
        img.hand = ret;
        return img;
    }

    public DocImage NewImageJPEG(String path) {
        int ret = newImageJPEG(this.hand_val, path);
        if (ret == 0) {
            return null;
        }
        DocImage img = new DocImage();
        img.hand = ret;
        return img;
    }

    public DocImage NewImageJPX(String path) {
        int ret = newImageJPX(this.hand_val, path);
        if (ret == 0) {
            return null;
        }
        DocImage img = new DocImage();
        img.hand = ret;
        return img;
    }

    public boolean ChangePageRect(int pageno, float dl, float dt, float dr, float db) {
        return changePageRect(this.hand_val, pageno, dl, dt, dr, db);
    }

    public boolean SetPageRotate(int pageno, int degree) {
        return setPageRotate(this.hand_val, pageno, degree);
    }

    public byte[] GetSignContents() {
        return getSignContents(this.hand_val);
    }

    public String GetSignFilter() {
        return getSignFilter(this.hand_val);
    }

    public String GetSignSubFilter() {
        return getSignSubFilter(this.hand_val);
    }

    public int[] GetSignByteRange() {
        return getSignByteRange(this.hand_val);
    }

    public int CheckSignByteRange() {
        return checkSignByteRange(this.hand_val);
    }
}