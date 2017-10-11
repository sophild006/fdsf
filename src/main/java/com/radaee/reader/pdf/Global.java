package com.radaee.reader.pdf;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.DisplayMetrics;
import com.flyersoft.books.A;
import com.flyersoft.books.T;
import com.flyersoft.components.BaseCompressor;
import com.flyersoft.moonreader.BuildConfig;
import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;

public class Global {
    public static final String PDF_VER = "140801";
    public static final String[] PDF_VER_PRE = new String[]{"140710", "140701", "140530"};
    public static boolean dark_mode = false;
    public static int def_view = 0;
    public static int fillColor = -16711936;
    public static float fling_dis = 1.0f;
    public static float fling_speed = 0.2f;
    public static float freeTextSize = 18.0f;
    public static int inkColor = -11184811;
    public static float inkWidth = 2.0f;
    public static boolean need_time_span = false;
    public static int render_mode = 2;
    public static int selColor = 1086324736;
    public static boolean selRTOL = false;
    public static String tmpNoteText;
    public static String tmp_path = null;
    public static float zoomLevel = 3.0f;

    private static native boolean activePremium(ContextWrapper contextWrapper, String str, String str2, String str3);

    private static native boolean activePremiumForVer(ContextWrapper contextWrapper, String str, String str2, String str3);

    private static native boolean activeProfessional(ContextWrapper contextWrapper, String str, String str2, String str3);

    private static native boolean activeProfessionalForVer(ContextWrapper contextWrapper, String str, String str2, String str3);

    private static native boolean activeStandard(ContextWrapper contextWrapper, String str, String str2, String str3);

    private static native boolean activeStandardForVer(ContextWrapper contextWrapper, String str, String str2, String str3);

    private static native boolean activeTime(ContextWrapper contextWrapper, String str, String str2, String str3, String str4, String str5);

    public static native int dibFree(int i);

    public static native int dibGet(int i, int i2, int i3);

    public static native void drawRect(int i, int i2, int i3, int i4, int i5, int i6, int i7);

    public static native void drawRectToDIB(int i, int i2, int i3, int i4, int i5, int i6, int i7);

    public static native void drawScroll(Bitmap bitmap, int i, int i2, int i3, int i4, int i5);

    public static native void drawToBmp(int i, int i2, int i3, int i4);

    public static native void drawToBmp2(int i, int i2, int i3, int i4, int i5, int i6);

    public static native void drawToDIB(int i, int i2, int i3, int i4);

    private static native void fontfileListAdd(String str);

    private static native void fontfileListEnd();

    private static native void fontfileListStart();

    private static native boolean fontfileMapping(String str, String str2);

    private static native int getFaceCount();

    private static native String getFaceName(int i);

    private static native String getVersion();

    private static native void hideAnnots(boolean z);

    public static native void invertBmp(int i);

    private static native void loadStdFont(int i, String str);

    public static native int lockBitmap(Bitmap bitmap);

    private static native boolean setAnnotFont(String str);

    private static native void setAnnotTransparency(int i);

    private static native void setCMapsPath(String str, String str2);

    private static native boolean setDefaultFont(String str, String str2, boolean z);

    private static native boolean setTextFont(String str);

    private static native void toDIBPoint(int i, float[] fArr, float[] fArr2);

    private static native void toDIBRect(int i, float[] fArr, float[] fArr2);

    private static native void toPDFPoint(int i, float[] fArr, float[] fArr2);

    private static native void toPDFRect(int i, float[] fArr, float[] fArr2);

    public static native void unlockBitmap(Bitmap bitmap, int i);

    public static boolean isPriorInited(Activity act) {
        String path = A.getContext().getFilesDir().getAbsolutePath();
        boolean ok = T.isFolder(path + "/fonts");
        if (!ok) {
            return ok;
        }
        File cfile = new File(path + "/cmaps");
        return cfile.isFile() && cfile.length() > 10;
    }

    public static boolean Init(Activity act, String filename) {
        tmp_path = A.download_cache_path + "/cache/.pdf";
        T.createFolder(tmp_path);
        File files = act.getFilesDir();
        boolean loaded = false;
        try {
            String outer_plugin = getOuterPluginFile(T.getFilePath(A.appDataPath));
            if (outer_plugin != null) {
                System.load(outer_plugin);
                loaded = true;
            }
        } catch (Throwable e) {
            A.error(e);
        }
        if (!loaded && A.BUILD_CPU(2).equals("armeabi-v7a")) {
            try {
                System.loadLibrary("rdpdf");
                loaded = true;
            } catch (Throwable e2) {
                A.error(e2);
            }
        }
        if (!loaded) {
            return false;
        }
        boolean activate = false;
        if (act.getPackageName().equals("com.flyersoft.moonreaderp")) {
            activate = activePremium(act, "moonreader", "seanyzhou@gmail.com", "PQZPAJ-GV2VZ1-HLAIAS-BNJQSH-KRNQKG-HRUSDL");
        } else if (act.getPackageName().equals("com.flyersoft.moonreaderpj")) {
            activate = activePremium(act, "moonreader", "seanyzhou@gmail.com", "IXHW6G-3A7ABB-HLAIAS-BNJQSH-KRNQKG-HRUSDL");
        } else if (act.getPackageName().equals(BuildConfig.APPLICATION_ID)) {
            activate = activeProfessional(act, "moonreader", "seanyzhou@gmail.com", "ITIQ7X-C65DMY-HLAIAS-BNJQSH-KRNQKG-HRUSDL");
        }
        A.log("pdf activate:" + activate + " package:" + act.getPackageName());
        String cmaps_path = files.getAbsolutePath() + "/cmaps";
        String umaps_path = files.getAbsolutePath() + "/umaps";
        File cfile = new File(cmaps_path);
        if (!cfile.isFile() || cfile.length() < 10) {
            copyCmapsUmaps(act.getAssets(), files, cmaps_path, umaps_path);
        }
        setCMapsPath(cmaps_path, umaps_path);
        String fonts_path = files.getAbsolutePath() + "/fonts";
        if (copyStdFonts(act.getAssets(), files, fonts_path)) {
            loadStdFont(0, fonts_path + "/Courier");
            loadStdFont(1, fonts_path + "/Courier-Bold");
            loadStdFont(2, fonts_path + "/Courier-BoldItalic");
            loadStdFont(3, fonts_path + "/Courier-Italic");
            loadStdFont(4, fonts_path + "/Helvetica");
            loadStdFont(5, fonts_path + "/Helvetica-Bold");
            loadStdFont(6, fonts_path + "/Helvetica-BoldItalic");
            loadStdFont(7, fonts_path + "/Helvetica-Italic");
            loadStdFont(8, fonts_path + "/Symbol");
            loadStdFont(9, fonts_path + "/Times");
            loadStdFont(10, fonts_path + "/Times-Bold");
            loadStdFont(11, fonts_path + "/Times-BoldItalic");
            loadStdFont(12, fonts_path + "/Times-Italic");
            loadStdFont(13, fonts_path + "/ZapfDingbats");
        }
        fontfileListStart();
        fontfileListAdd("/system/fonts/DroidSans.ttf");
        boolean hasRobot = T.isFile("/system/fonts/Roboto-Regular.ttf");
        if (hasRobot) {
            fontfileListAdd("/system/fonts/Roboto-Regular.ttf");
        }
        fontfileListAdd("/system/fonts/DroidSansFallback.ttf");
        addDelegateFonts(act, filename);
        fontfileListEnd();
        int face_count = getFaceCount();
        String face_name = null;
        for (int face_first = 0; face_first < face_count; face_first++) {
            face_name = getFaceName(face_first);
            if (face_name != null) {
                break;
            }
        }
        if (hasRobot) {
            if (!(setDefaultFont(null, "Roboto-Regular", true) || face_name == null || setDefaultFont(null, "DroidSans", true))) {
                setDefaultFont(null, face_name, true);
            }
            if (!(setDefaultFont(null, "Roboto-Regular", false) || face_name == null || setDefaultFont(null, "DroidSans", false))) {
                setDefaultFont(null, face_name, false);
            }
        } else {
            if (!(setDefaultFont(null, "DroidSans", true) || face_name == null)) {
                setDefaultFont(null, face_name, true);
            }
            if (!(setDefaultFont(null, "DroidSans", false) || face_name == null)) {
                setDefaultFont(null, face_name, false);
            }
        }
        if (!(setDefaultFont("GB1", "DroidSansFallback", true) || face_name == null)) {
            setDefaultFont(null, face_name, true);
        }
        if (!(setDefaultFont("GB1", "DroidSansFallback", false) || face_name == null)) {
            setDefaultFont(null, face_name, false);
        }
        if (!(setDefaultFont("CNS1", "DroidSansFallback", true) || face_name == null)) {
            setDefaultFont(null, face_name, true);
        }
        if (!(setDefaultFont("CNS1", "DroidSansFallback", false) || face_name == null)) {
            setDefaultFont(null, face_name, false);
        }
        if (!(setDefaultFont("Japan1", "DroidSansFallback", true) || face_name == null)) {
            setDefaultFont(null, face_name, true);
        }
        if (!(setDefaultFont("Japan1", "DroidSansFallback", false) || face_name == null)) {
            setDefaultFont(null, face_name, false);
        }
        if (!(setDefaultFont("Korea1", "DroidSansFallback", true) || face_name == null)) {
            setDefaultFont(null, face_name, true);
        }
        if (!(setDefaultFont("Korea1", "DroidSansFallback", false) || face_name == null)) {
            setDefaultFont(null, face_name, false);
        }
        if (!(setAnnotFont("DroidSansFallback") || face_name == null)) {
            setAnnotFont(face_name);
        }
        default_config();
        return true;
    }

    private static void addDelegateFonts(Activity act, String filename) {
        if (filename != null) {
            for (Entry<String, ?> entry : act.getSharedPreferences(filename.replace("/", "") + "_font", 0).getAll().entrySet()) {
                String font = A.outerFontsFolder + "/" + ((String) entry.getKey()) + ".ttf";
                if (T.isFile(font)) {
                    fontfileListAdd(font);
                    A.log("###add delegateFont:" + font);
                } else {
                    A.log("***missed delegateFont:" + font);
                }
            }
        }
    }

    private static String getOuterPluginFile(String sysDataPath) {
        String so = sysDataPath + "/com.flyersoft.plugin_pdf/lib/librdpdf" + PDF_VER + ".so";
        if (T.isFile(so)) {
            return so;
        }
        for (String str : PDF_VER_PRE) {
            so = sysDataPath + "/com.flyersoft.plugin_pdf/lib/librdpdf" + str + ".so";
            if (T.isFile(so)) {
                return so;
            }
        }
        return null;
    }

    private static void copyCmapsUmaps(AssetManager am, File files, String cmaps_path, String umaps_path) {
        try {
            if (!files.isDirectory()) {
                files.mkdirs();
            }
            String cjk_rar = files.getAbsolutePath() + "/cjk.rar";
            T.extractFileFromAsset(am, "cjk", cjk_rar);
            BaseCompressor rar = BaseCompressor.createZipper(cjk_rar);
            rar.saveToFile("libcmaps.so", cmaps_path);
            rar.saveToFile("libumaps.so", umaps_path);
            new File(cjk_rar).delete();
        } catch (Exception e) {
            A.error(e);
        }
    }

    private static boolean copySoFile(AssetManager am, File files, String so_file) {
        try {
            if (!files.isDirectory()) {
                files.mkdirs();
            }
            String pdf_rar = files.getAbsolutePath() + "/pdf.rar";
            T.extractFileFromAsset(am, "pdf", pdf_rar);
            BaseCompressor.createZipper(pdf_rar).saveToFile("librdpdf.so", so_file);
            new File(pdf_rar).delete();
            return true;
        } catch (Exception e) {
            A.error(e);
            return false;
        }
    }

    private static boolean copyStdFonts(AssetManager am, File files, String fonts_path) {
        File folder = new File(fonts_path);
        if (folder.isDirectory()) {
            return true;
        }
        try {
            if (!folder.mkdirs()) {
                return false;
            }
            String eu_rar = files.getAbsolutePath() + "/eu.rar";
            T.extractFileFromAsset(am, "eu", eu_rar);
            BaseCompressor rar = BaseCompressor.createZipper(eu_rar);
            Iterator it = rar.getAllList().iterator();
            while (it.hasNext()) {
                String filename = (String) it.next();
                rar.saveToFile(filename, fonts_path + "/" + filename);
            }
            new File(eu_rar).delete();
            return true;
        } catch (Exception e) {
            A.error(e);
            return false;
        }
    }

    public static void default_config() {
        selColor = 1086324736;
        fling_dis = 1.0f;
        fling_speed = 0.2f;
        def_view = 3;
        render_mode = 2;
        dark_mode = false;
        setAnnotTransparency(536887551);
        zoomLevel = (float) A.pdf_zoom_level;
    }

    public static void ToDIBPoint(Matrix mat, float[] ppoint, float[] dpoint) {
        toDIBPoint(mat.hand, ppoint, dpoint);
    }

    public static void ToPDFPoint(Matrix mat, float[] dpoint, float[] ppoint) {
        toPDFPoint(mat.hand, dpoint, ppoint);
    }

    public static void ToDIBRect(Matrix mat, float[] prect, float[] drect) {
        toDIBRect(mat.hand, prect, drect);
    }

    public static void ToPDFRect(Matrix mat, float[] drect, float[] prect) {
        toPDFRect(mat.hand, drect, prect);
    }

    public static void RemoveTmp() {
        tmpNoteText = null;
        File[] files = new File(tmp_path).listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                A.log("---remove pdf cache:" + files[i].getAbsolutePath());
                T.deleteFile(files[i].getAbsolutePath());
            }
        }
    }

    public static Bitmap createBitmapOfPage(Document doc, int pageno, DisplayMetrics dm) {
        try {
            int disp_w;
            int disp_h;
            Page page = doc.GetPage(0);
            if (dm != null) {
                disp_w = dm.widthPixels;
                disp_h = dm.heightPixels;
            } else {
                disp_w = 640;
                disp_h = 480;
            }
            float dpix = doc.GetPageWidth(0);
            dpix = (float) Math.round(((float) (disp_w * 72)) / dpix);
            float dpiy = (float) Math.round(((float) ((disp_h - 50) * 72)) / doc.GetPageHeight(0));
            if (dpix > dpiy) {
                dpix = dpiy;
            }
            int w = (int) ((doc.GetPageWidth(0) * dpix) / 72.0f);
            int h = (int) ((doc.GetPageHeight(0) * dpix) / 72.0f);
            Matrix mat = new Matrix(dpix / 72.0f, (-dpix) / 72.0f, 0.0f, (doc.GetPageHeight(0) * dpix) / 72.0f);
            int dib = dibGet(0, w, h);
            page.RenderPrePare(dib);
            page.Render(dib, mat);
            Bitmap m_bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
            m_bmp.eraseColor(-1);
            int bmp = lockBitmap(m_bmp);
            drawToBmp(bmp, dib, 0, 0);
            unlockBitmap(m_bmp, bmp);
            mat.Destroy();
            dibFree(dib);
            page.Close();
            return m_bmp;
        } catch (OutOfMemoryError e) {
            return null;
        } catch (Exception e2) {
            A.error(e2);
            return null;
        }
    }

    public static void ToDIBPoint(float ratio, int dib_h, float[] ppoint, float[] dpoint) {
        dpoint[0] = ppoint[0] * ratio;
        dpoint[1] = ((float) dib_h) - (ppoint[1] * ratio);
    }

    public static void ToPDFPoint(float ratio, int dib_h, float[] dpoint, float[] ppoint) {
        ppoint[0] = dpoint[0] / ratio;
        ppoint[1] = (((float) dib_h) - dpoint[1]) / ratio;
    }

    public static void ToDIBRect(float ratio, int dib_h, float[] prect, float[] drect) {
        drect[0] = prect[0] * ratio;
        drect[1] = ((float) dib_h) - (prect[3] * ratio);
        drect[2] = prect[2] * ratio;
        drect[3] = ((float) dib_h) - (prect[1] * ratio);
    }

    public static void ToPDFRect(float ratio, int dib_h, float[] drect, float[] prect) {
        prect[0] = drect[0] / ratio;
        prect[1] = (((float) dib_h) - drect[3]) / ratio;
        prect[2] = drect[2] / ratio;
        prect[3] = (((float) dib_h) - drect[1]) / ratio;
    }
}