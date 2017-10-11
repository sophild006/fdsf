package com.flyersoft.books;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.SystemClock;
import android.text.Html.ImageGetter;
import com.flyersoft.components.CSS;
import com.flyersoft.moonreader.ActivityTxt;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class BaseEBook {
    public static final String ADITIONAL_ERROR_TAG = "#ERROR#";
    public static final String CHAPTER_END_HTMLHINT1 = " <br/><span align=\"right\"><font color=#6060EE><u>(";
    public static final String CHAPTER_END_HTMLHINT2 = "\")</u></font></span>";
    public static final String HAS_ID_TAG = "HAS_ID_TAG";
    public static final int MAX_HTML_SIZE = 1000000;
    public static final String UN_LOAD_TAG = "UN_LOAD_TAG";
    public static boolean isGetWordsWoking;
    public String ISBN;
    public String author;
    public String bookName;
    public ArrayList<String> categories = new ArrayList();
    protected ArrayList<Chapter> chapters;
    public String description = "";
    public String errMsg;
    protected ImageGetter imageGetter;
    public boolean inited;
    protected boolean isHtml;
    protected boolean showChaptersAtBegin;
    protected long totalSize;
    public boolean treeTOC;

    public interface AfterGetUnReadWords {
        void refresh(int i);
    }

    public class Chapter {
        public String additionalText;
        public boolean brokenChapter;
        public CSS css;
        public String css_body_class;
        public String css_body_id;
        public String css_body_style;
        public String css_str;
        public boolean expanded;
        public String filename;
        public String hasImageOnly;
        public boolean hasSubChapter;
        public String id_Tag;
        public int indent;
        public String name;
        public String pure_text;
        public int pure_text_length;
        public long size;
        public String text;
        public String unload_Tag;
        public ArrayList<String> usedFiles = new ArrayList();
        public int word_count;

        Chapter(String name, String filename, String text, long size) {
            if (name != null && name.length() > 403) {
                name = name.substring(0, 400) + "...";
            }
            this.name = name;
            this.filename = filename;
            this.text = text;
            this.size = size;
            this.unload_Tag = text;
            this.hasSubChapter = false;
            this.indent = 0;
            this.expanded = true;
            this.id_Tag = null;
            this.additionalText = "";
            this.word_count = -1;
            this.pure_text_length = -1;
            BaseEBook.this.totalSize = -1;
        }
    }

    public class FootNote {
        public String html;
        public String tag;
        public String title;
    }

    public abstract String dealSplitHtml(int i, int i2, int i3, String str);

    public abstract String getAuthor();

    public abstract String getBookName();

    public abstract String getCacheFilename(Uri uri);

    public abstract String getChapterText(int i);

    public abstract ArrayList<Chapter> getChapters();

    public abstract String getCoverFile();

    public abstract Drawable getDrawableFromSource(String str, int i);

    public abstract String getFontFile(String str);

    public abstract FootNote getFootNote(String str);

    public abstract ArrayList<String> getImageFileList();

    public abstract ImageGetter getImageGetter();


    public abstract int getPriorTextLength(int i);

    public abstract String getSingleFileText(String str);

    public abstract long getTotalSize();

    public abstract boolean isDrmProtected();

    public abstract boolean isHtml();

    public abstract boolean isInited();

    public abstract boolean showChaptersAtBegin();

    public static int getUnReadWordCountFrom(final BaseEBook ebook, final int fromChapter, final AfterGetUnReadWords onDone, final boolean whole) {
        int i = -1;
        boolean ok = true;
        int i2 = whole ? 0 : fromChapter + 1;
        while (i2 < ebook.getChapters().size()) {
            if (((Chapter) ebook.getChapters().get(i2)).pure_text_length < 0) {
                ok = false;
                break;
            }
            i2++;
        }
        if (ok && ebook.getChapters().size() == 1 && ((Chapter) ebook.getChapters().get(0)).pure_text_length < 0) {
            ok = false;
        }
        if (ok) {
            i = 0;
            for (i2 = fromChapter + 1; i2 < ebook.getChapters().size(); i2++) {
                i += ((Chapter) ebook.getChapters().get(i2)).word_count;
            }
        } else if (!isGetWordsWoking) {
            int i3;
            Thread thread = new Thread() {
                public void run() {
                    A.saveMemoryLog("mem: ");
                    BaseEBook.isGetWordsWoking = true;
                    int count = 0;
                    long t = SystemClock.elapsedRealtime();
                    try {
                        Chapter c;
                        String text;
                        boolean isCJK = A.isAsiaLanguage;
                        int i = fromChapter + 1;
                        while (i < ebook.getChapters().size()) {
                            if (!T.isNull(ActivityTxt.selfPref)) {
                                c = (Chapter) ebook.getChapters().get(i);
                                if (c.pure_text_length < 0) {
                                    text = ebook.getChapterText(i);
                                    if (ebook.isHtml()) {
                                        text = T.html2Text(text);
                                    }
                                    c.pure_text_length = text.length();
                                    c.word_count = T.getWordsCount(text, isCJK);
                                }
                                count += c.word_count;
                                i++;
                            } else {
                                return;
                            }
                        }
                        if (!(whole || onDone == null)) {
                            onDone.refresh(count);
                        }
                        A.saveMemoryLog("*used time 1): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
                        if (whole || !A.isLowestMemory()) {
                            for (i = fromChapter; i >= 0; i--) {
                                if (T.isNull(ActivityTxt.selfPref)) {
                                    BaseEBook.isGetWordsWoking = false;
                                    A.saveMemoryLog("*used time 2): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
                                    return;
                                }
                                c = (Chapter) ebook.getChapters().get(i);
                                if (c.pure_text_length < 0) {
                                    text = ebook.getChapterText(i);
                                    if (ebook.isHtml()) {
                                        text = T.html2Text(text);
                                    }
                                    c.pure_text_length = text.length();
                                    c.word_count = T.getWordsCount(text, isCJK);
                                }
                            }
                        }
                        if (!(whole || onDone == null)) {
                            onDone.refresh(-1);
                        }
                        if (whole && onDone != null) {
                            onDone.refresh(count);
                        }
                        BaseEBook.isGetWordsWoking = false;
                        A.saveMemoryLog("*used time 2): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
                    } catch (OutOfMemoryError e) {
                        A.error(e);
                    } catch (Exception e2) {
                        A.error(e2);
                    } finally {
                        BaseEBook.isGetWordsWoking = false;
                        A.saveMemoryLog("*used time 2): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
                    }
                }
            };
            if (whole) {
                i3 = 5;
            } else {
                i3 = 1;
            }
            thread.setPriority(i3);
            thread.start();
        }
        return i;
    }

    public static int getTxtUnReadWordCount(final AfterGetUnReadWords onDone, final boolean whole) {
        int i;
        int i2 = -1;
        boolean ok = true;
        int block = (int) (A.lastPosition / ((long) A.fixedBlockLength));
        final int from = whole ? 0 : block == 0 ? 3 : block + 2;
        for (i = from; i < A.txts.size(); i++) {
            if (A.txtWordCount[i] == null) {
                ok = false;
                break;
            }
        }
        if (ok) {
            i2 = 0;
            for (i = from; i < A.txts.size(); i++) {
                i2 += A.txtWordCount[i].intValue();
            }
        } else if (!isGetWordsWoking) {
            Thread thread = new Thread() {
                public void run() {
                    BaseEBook.isGetWordsWoking = true;
                    int count = 0;
                    long t = SystemClock.elapsedRealtime();
                    try {
                        boolean isCJK = A.isAsiaLanguage;
                        int i = from;
                        while (i < A.txts.size()) {
                            if (!T.isNull(ActivityTxt.selfPref)) {
                                if (A.txtWordCount[i] == null) {
                                    A.txtWordCount[i] = Integer.valueOf(T.getWordsCount((String) A.txts.get(i), isCJK));
                                }
                                count += A.txtWordCount[i].intValue();
                                i++;
                            } else {
                                return;
                            }
                        }
                        if (!(whole || onDone == null)) {
                            onDone.refresh(count);
                        }
                        A.saveMemoryLog("*used time 1): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
                        if (whole || !A.isLowestMemory()) {
                            for (i = from - 1; i >= 0; i--) {
                                if (T.isNull(ActivityTxt.selfPref)) {
                                    BaseEBook.isGetWordsWoking = false;
                                    A.saveMemoryLog("*used time 2): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
                                    return;
                                }
                                if (A.txtWordCount[i] == null) {
                                    A.txtWordCount[i] = Integer.valueOf(T.getWordsCount((String) A.txts.get(i), isCJK));
                                }
                            }
                        }
                        if (whole && onDone != null) {
                            onDone.refresh(count);
                        }
                        BaseEBook.isGetWordsWoking = false;
                        A.saveMemoryLog("*used time 2): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
                    } catch (OutOfMemoryError e) {
                        A.error(e);
                    } catch (Exception e2) {
                        A.error(e2);
                    } finally {
                        BaseEBook.isGetWordsWoking = false;
                        A.saveMemoryLog("*used time 2): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
                    }
                }
            };
            thread.setPriority(whole ? 5 : 1);
            thread.start();
        }
        return i2;
    }

//    public static int getPdfUnReadWordCoun(final AfterGetUnReadWords onDone, final boolean whole) {
//        int i = -1;
//        if (!(T.isNull(ActivityTxt.selfPref) || ActivityTxt.selfPref.pdf == null)) {
//            PDFReader pdf = ActivityTxt.selfPref.pdf;
//            if (pdf != null) {
//                boolean ok = true;
//                final int cur = whole ? 0 : ActivityTxt.selfPref.pdfGetCurrPageNo();
//                if (cur != -1) {
//                    int i2;
//                    for (i2 = cur; i2 < pdf.getPageWordCount().length; i2++) {
//                        if (pdf.getPageWordCount()[i2] == null) {
//                            ok = false;
//                            break;
//                        }
//                    }
//                    if (ok) {
//                        i = 0;
//                        for (i2 = cur; i2 < pdf.getPageWordCount().length; i2++) {
//                            i += pdf.getPageWordCount()[i2].intValue();
//                        }
//                    } else if (!isGetWordsWoking) {
//                        Thread thread = new Thread() {
//                            public void run() {
//                                BaseEBook.isGetWordsWoking = true;
//                                int count = 0;
//                                long t = SystemClock.elapsedRealtime();
//                                PDFReader pdf = ActivityTxt.selfPref.pdf;
//                                SharedPreferences sp = A.getContext().getSharedPreferences("pdf_words_failed", 0);
//                                sp.edit().putBoolean(A.lastFile, true).commit();
//                                try {
//                                    String text;
//                                    boolean isCJK = A.isAsiaLanguage;
//                                    int i = cur;
//                                    while (i < pdf.getPageWordCount().length) {
//                                        if (!T.isNull(ActivityTxt.selfPref)) {
//                                            if (pdf.getPageWordCount()[i] == null) {
//                                                text = ActivityTxt.selfPref.pdfGetPageText(i);
//                                                pdf.getPageCharCount()[i] = Integer.valueOf(text.length());
//                                                pdf.getPageWordCount()[i] = Integer.valueOf(T.getWordsCount(text, isCJK));
//                                            }
//                                            count += pdf.getPageWordCount()[i].intValue();
//                                            i++;
//                                        } else {
//                                            return;
//                                        }
//                                    }
//                                    if (!(whole || onDone == null)) {
//                                        onDone.refresh(count);
//                                    }
//                                    A.saveMemoryLog("*used time 1): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
//                                    if (whole || !A.isLowestMemory()) {
//                                        for (i = cur - 1; i >= 0; i--) {
//                                            if (T.isNull(ActivityTxt.selfPref)) {
//                                                sp.edit().remove(A.lastFile).commit();
//                                                BaseEBook.isGetWordsWoking = false;
//                                                A.saveMemoryLog("*used time 2): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
//                                                return;
//                                            }
//                                            if (pdf.getPageWordCount()[i] == null) {
//                                                text = ActivityTxt.selfPref.pdfGetPageText(i);
//                                                pdf.getPageCharCount()[i] = Integer.valueOf(text.length());
//                                                pdf.getPageWordCount()[i] = Integer.valueOf(T.getWordsCount(text, isCJK));
//                                            }
//                                        }
//                                    }
//                                    if (whole && onDone != null) {
//                                        onDone.refresh(count);
//                                    }
//                                    sp.edit().remove(A.lastFile).commit();
//                                    BaseEBook.isGetWordsWoking = false;
//                                    A.saveMemoryLog("*used time 2): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
//                                } catch (OutOfMemoryError e) {
//                                    A.error(e);
//                                } catch (Throwable e2) {
//                                    A.error(e2);
//                                } finally {
//                                    sp.edit().remove(A.lastFile).commit();
//                                    BaseEBook.isGetWordsWoking = false;
//                                    A.saveMemoryLog("*used time 2): " + (SystemClock.elapsedRealtime() - t) + ", wordCount: " + count + ", ");
//                                }
//                            }
//                        };
//                        thread.setPriority(whole ? 5 : 1);
//                        thread.start();
//                    }
//                }
//            }
//        }
//        return i;
//    }

    public static int getBookWordCountIfComplete() {
        int count = 0;
        try {
            int i;
            switch (A.getBookType()) {
                case 0:
                    for (i = 0; i < A.txts.size(); i++) {
                        if (A.txtWordCount[i] == null) {
                            return 0;
                        }
                        count += A.txtWordCount[i].intValue();
                    }
                    return count;
                case 1:
                    return ActivityTxt.selfPref.getHtmlFileWordCount();
                case 7:
//                    PDFReader pdf = ActivityTxt.selfPref.pdf;
//                    for (i = 0; i < pdf.getPageWordCount().length; i++) {
//                        if (pdf.getPageWordCount()[i] == null) {
//                            return 0;
//                        }
//                        count += pdf.getPageWordCount()[i].intValue();
//                    }
//                    return count;
                    break;
                case 100:
                    for (i = 0; i < A.ebook.getChapters().size(); i++) {
                        if (((Chapter) A.ebook.getChapters().get(i)).pure_text_length < 0) {
                            return 0;
                        }
                        count += ((Chapter) A.ebook.getChapters().get(i)).word_count;
                    }
                    return count;
                default:
                    return 0;
            }
        } catch (Exception e) {
            A.error(e);
            return 0;
        }
        return count;
    }

    public static int getPriorTextLength2(int id) {
        int l = getBookCharCountIfComplete(id - 1);
        if (l == 0) {
            return A.ebook.getPriorTextLength(id);
        }
        return getBookCharCountIfComplete(-1) - l;
    }

    public static int getBookCharCountIfComplete(int fromChapter) {
        int count = 0;
        try {
            int i;
            switch (A.getBookType()) {
                case 0:
                    return A.getPriorTxtLength(fromChapter);
                case 1:
                    return ActivityTxt.selfPref.getHtmlFileCharCount();
                case 7:
//                    PDFReader pdf = ActivityTxt.selfPref.pdf;
//                    for (i = 0; i < pdf.getPageWordCount().length; i++) {
//                        if (pdf.getPageCharCount()[i] == null) {
//                            return 0;
//                        }
//                        count += pdf.getPageCharCount()[i].intValue();
//                    }
                    break;
                case 100:
                    for (i = fromChapter + 1; i < A.ebook.getChapters().size(); i++) {
                        if (((Chapter) A.ebook.getChapters().get(i)).pure_text_length < 0) {
                            return 0;
                        }
                        count += ((Chapter) A.ebook.getChapters().get(i)).pure_text_length;
                    }
                    break;
            }
        } catch (Exception e) {
            A.error(e);
            count = 0;
        }
        return count;
    }

    public static String getISBN(String filename) {
        try {
            String s;
            int i1;
            int i;
//            if (filename.endsWith(".epub")) {
//                MyZip_Base zip = MyZip_Base.createZipper(filename);
//                Iterator it = zip.getFileInfoOfZip().iterator();
//                while (it.hasNext()) {
//                    FileInfo_In_Zip file = (FileInfo_In_Zip) it.next();
//                    if (file.filename.endsWith("content.opf")) {
//                        s = T.inputStream2String(zip.getFileStreamFromZip(file.filename));
//                        i1 = s.indexOf("ISBN");
//                        if (i1 == -1) {
//                            return null;
//                        }
//                        i1 = s.indexOf(">", i1) + 1;
//                        String isbn = s.substring(i1, s.indexOf("<", i1)).trim();
//                        i = 0;
//                        while (i < isbn.length()) {
//                            if (isbn.charAt(i) != '-' && (isbn.charAt(i) < '0' || isbn.charAt(i) > '9')) {
//                                return null;
//                            }
//                            i++;
//                        }
//                        if (isbn.length() >= 30) {
//                            return null;
//                        }
//                        return isbn;
//                    }
//                }
//            }
            if (filename.endsWith(".mobi")) {
                s = T.getFileText(filename);
                int x = s.indexOf(">ISBN");
                if (x == -1) {
                    x = s.indexOf("isbn:");
                }
                if (x == -1) {
                    return null;
                }
                i1 = -1;
                i = x + 1;
                while (i < x + 30) {
                    if (s.charAt(i) >= '0' && s.charAt(i) <= '9') {
                        i1 = i;
                        break;
                    }
                    i++;
                }
                if (i1 == -1) {
                    return null;
                }
                int i2 = -1;
                i = i1 + 1;
                while (i < i1 + 30) {
                    if (s.charAt(i) != '-' && (s.charAt(i) < '0' || s.charAt(i) > '9')) {
                        i2 = i;
                        break;
                    }
                    i++;
                }
                if (i2 > i1 && i2 - i1 < 30) {
                    return s.substring(i1, i2).trim();
                }
            }
        } catch (Throwable e) {
            A.error(e);
        }
        return null;
    }
}