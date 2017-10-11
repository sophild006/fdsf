package com.flyersoft.books;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.provider.DocumentFile;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.flyersoft.components.SD;
import com.flyersoft.staticlayout.XmlUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.InputSource;

public class T {
    public static HashMap<String, Integer> COLORS = buildColorMap();
    private static final String[][] MIME_MapTable;
    public static boolean isOutOfMemoryError;
    private static Toast mToast;
    public static boolean scanCanceled;
    public static boolean tmpGetFileDrawableOutOfMemory;

    public static abstract class MyClickableSpan extends ClickableSpan {
        public OnUrlClick onUrlClick;
        public String url;
    }

    public interface OnUrlClick {
        void onClick(String str);
    }

    public static Drawable getFileDrawable(Context context, String imageFile) {
        return getFileDrawable(context, imageFile, 1);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.drawable.Drawable getFileDrawable(android.content.Context r13, java.lang.String r14, int r15) {
        /*
        r9 = 0;
        r10 = isFile(r14);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        if (r10 != 0) goto L_0x0009;
    L_0x0007:
        r0 = r9;
    L_0x0008:
        return r0;
    L_0x0009:
        r10 = r14.toLowerCase();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r11 = ".svg";
        r10 = r10.endsWith(r11);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        if (r10 == 0) goto L_0x001f;
    L_0x0015:
        r7 = new java.io.FileInputStream;	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r7.<init>(r14);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r0 = com.flyersoft.books.A.getSvgDrawable(r7);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        goto L_0x0008;
    L_0x001f:
        r10 = r13.getContentResolver();	 Catch:{ Exception -> 0x003c, OutOfMemoryError -> 0x008b }
        r11 = new java.io.File;	 Catch:{ Exception -> 0x003c, OutOfMemoryError -> 0x008b }
        r11.<init>(r14);	 Catch:{ Exception -> 0x003c, OutOfMemoryError -> 0x008b }
        r11 = android.net.Uri.fromFile(r11);	 Catch:{ Exception -> 0x003c, OutOfMemoryError -> 0x008b }
        r12 = "r";
        r8 = r10.openFileDescriptor(r11, r12);	 Catch:{ Exception -> 0x003c, OutOfMemoryError -> 0x008b }
        r6 = r8.getFileDescriptor();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r0 = 0;
        r0 = getFileDrawable_proc(r13, r14, r6, r15);	 Catch:{ OutOfMemoryError -> 0x0042, Exception -> 0x00d5 }
        goto L_0x0008;
    L_0x003c:
        r1 = move-exception;
        com.flyersoft.books.A.error(r1);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r0 = r9;
        goto L_0x0008;
    L_0x0042:
        r2 = move-exception;
        r10 = 1;
        tmpGetFileDrawableOutOfMemory = r10;	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        java.lang.System.gc();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r15 = r15 + 1;
        r10 = new java.lang.StringBuilder;	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10.<init>();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r11 = "1) OutOfMemory, startSampleSize:";
        r10 = r10.append(r11);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10 = r10.append(r15);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10 = r10.toString();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        com.flyersoft.books.A.log(r10);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r0 = getFileDrawable_proc(r13, r14, r6, r15);	 Catch:{ OutOfMemoryError -> 0x0066, Exception -> 0x00d5 }
        goto L_0x0008;
    L_0x0066:
        r3 = move-exception;
        java.lang.System.gc();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r15 = r15 + 2;
        r10 = new java.lang.StringBuilder;	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10.<init>();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r11 = "2) OutOfMemory, startSampleSize:";
        r10 = r10.append(r11);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10 = r10.append(r15);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10 = r10.toString();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        com.flyersoft.books.A.log(r10);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r0 = getFileDrawable_proc(r13, r14, r6, r15);	 Catch:{ OutOfMemoryError -> 0x0097, Exception -> 0x00d5 }
    L_0x0086:
        java.lang.System.gc();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        goto L_0x0008;
    L_0x008b:
        r1 = move-exception;
        r10 = "(*) getFileDrawable OutOfMemoryError";
        com.flyersoft.books.A.log(r10);
        java.lang.System.gc();
        r0 = r9;
        goto L_0x0008;
    L_0x0097:
        r4 = move-exception;
        java.lang.System.gc();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r15 = r15 + 2;
        r10 = new java.lang.StringBuilder;	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10.<init>();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r11 = "3) OutOfMemory, startSampleSize:";
        r10 = r10.append(r11);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10 = r10.append(r15);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10 = r10.toString();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        com.flyersoft.books.A.log(r10);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r0 = getFileDrawable_proc(r13, r14, r6, r15);	 Catch:{ OutOfMemoryError -> 0x00b8, Exception -> 0x00d5 }
        goto L_0x0086;
    L_0x00b8:
        r5 = move-exception;
        r10 = new java.lang.StringBuilder;	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10.<init>();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r11 = "4) still OutOfMemory, startSampleSize:";
        r10 = r10.append(r11);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10 = r10.append(r15);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r10 = r10.toString();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        com.flyersoft.books.A.log(r10);	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        java.lang.System.gc();	 Catch:{ OutOfMemoryError -> 0x008b, Exception -> 0x00d5 }
        r0 = r9;
        goto L_0x0008;
    L_0x00d5:
        r1 = move-exception;
        com.flyersoft.books.A.error(r1);
        r0 = r9;
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.books.T.getFileDrawable(android.content.Context, java.lang.String, int):android.graphics.drawable.Drawable");
    }

    private static Drawable getFileDrawable_proc(Context context, String imageFile, FileDescriptor fd, int inSampleSize) {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, opts);
        opts.inSampleSize = inSampleSize;
        opts.inJustDecodeBounds = false;
        return new BitmapDrawable(context.getResources(), BitmapFactory.decodeFile(imageFile, opts));
    }

    public static String getBattery() {
        try {
            return getFileText("/sys/class/power_supply/battery/capacity");
        } catch (Exception e) {
            A.error(e);
            return null;
        }
    }

    public static void mySleep(int i) {
        try {
            Thread.sleep((long) i);
        } catch (InterruptedException e) {
            A.error(e);
        }
    }

    public static boolean openAppInMarket(Activity activity) {
        try {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + activity.getPackageName())));
            return true;
        } catch (Exception e) {
            A.error(e);
            return false;
        }
    }

    public static boolean openAppInMarket(Activity activity, String packageName) {
        try {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
            return true;
        } catch (Exception e) {
            A.error(e);
            return false;
        }
    }

    public static void openTtsOptions(Activity activity) {
        if (activity != null) {
            try {
                if (VERSION.SDK_INT >= 11) {
                    activity.startActivity(new Intent(VERSION.SDK_INT >= 14 ? "com.android.settings.TTS_SETTINGS" : "com.android.settings.VOICE_INPUT_OUTPUT_SETTINGS"));
                    return;
                }
                Intent intent = new Intent();
                if (VERSION.SDK_INT == 7) {
                    intent.setClassName("com.google.tts", "com.google.tts.ConfigurationManager");
                } else {
                    intent.setClassName("com.android.settings", "com.android.settings.TextToSpeechSettings");
                }
                activity.startActivity(intent);
            } catch (Exception e) {
                A.error(e);
                try {
                    activity.startActivity(new Intent("android.settings.SETTINGS"));
                } catch (Exception e2) {
                    A.error(e2);
                }
            }
        }
    }

    public static void openHomeScreen(Activity activity) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            activity.startActivity(intent);
        } catch (Exception e) {
            A.error(e);
        }
    }

    public static String mergeHref(String baseUrl, String url) {
        try {
            if (url.startsWith("//")) {
                return "http://" + url.substring(2);
            }
            if (!baseUrl.startsWith("http")) {
                baseUrl = "http://" + baseUrl;
            }
            if (baseUrl.lastIndexOf("/") < 12) {
                baseUrl = baseUrl + "/";
            }
            if (url.startsWith("http")) {
                return url;
            }
            if (url.startsWith("/")) {
                return baseUrl.substring(0, baseUrl.indexOf("/", baseUrl.indexOf("."))) + url;
            } else if (baseUrl.endsWith("/")) {
                return baseUrl + url;
            } else {
                return baseUrl.substring(0, baseUrl.lastIndexOf("/") + 1) + url;
            }
        } catch (Exception e) {
            A.error(e);
            return url;
        }
    }

    public static Drawable getUrlImage(String url) {
        return getUrlImage(url, null, null);
    }

    public static Drawable getUrlImage(String url, String username, String password) {
        return null;
    }

    public static String getUrlTextWithHttpClient(String url_str) {
        try {
            return inputStream2String(getUrlStreamWithHttpClient(url_str));
        } catch (Exception e) {
            A.error(e);
            return null;
        }
    }

    public static long getUrlFileSize(String url_str) {
        try {
            URLConnection urlConnection = new URL(url_str).openConnection();
            urlConnection.connect();
            return (long) urlConnection.getContentLength();
        } catch (Exception e) {
            A.error(e);
            return 0;
        }
    }

    public static String getUrlText(String url_str) {
        return getUrlText(url_str, "");
    }

    public static String getUrlText(String url_str, String user_agent) {
        try {
            try {
                URLConnection conn = new URL(url_str).openConnection();
                conn.setConnectTimeout(10000);
                if (!isNull(user_agent)) {
                    conn.setRequestProperty("User-Agent", user_agent);
                }
                return inputStream2String(conn.getInputStream());
            } catch (Exception e) {
                A.error(e);
                return null;
            }
        } catch (Exception e2) {
            A.error(e2);
        }
        return null;
    }

    public static InputStream getUrlStream(String url_str) throws Exception {
        return getUrlStream(url_str, "");
    }

    public static InputStream getUrlStream(String url_str, String user_agent) throws Exception {
        URLConnection conn = new URL(url_str).openConnection();
        conn.setConnectTimeout(10000);
        if (!isNull(user_agent)) {
            conn.setRequestProperty("User-Agent", user_agent);
        }
        return conn.getInputStream();
    }

    public static InputStream getUrlStreamWithHttpClient(String url_str) throws Exception {
        return null;
    }

//    public static InputStream getUrlStreamWithHttpClient(String url_str, String user_agent) throws Exception {
//        HttpClient hc;
//        if (user_agent == null || user_agent.equals("")) {
//            hc = new DefaultHttpClient();
//        } else {
//            BasicHttpParams httpParams = new BasicHttpParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
//            if (!isNull(user_agent)) {
//                HttpProtocolParams.setUserAgent(httpParams, user_agent);
//            }
//            hc = new DefaultHttpClient(httpParams);
//        }
//        return hc.execute(new HttpGet(url_str)).getEntity().getContent();
//    }
//
//    public static InputStream getUrlStreamWithLogin(String url, String username, String password) throws Exception {
//        DefaultHttpClient hc = new DefaultHttpClient();
//        HttpGet hg = new HttpGet(url);
//        hc.getCredentialsProvider().setCredentials(new AuthScope(null, -1, null), new UsernamePasswordCredentials(username, password));
//        try {
//            HttpResponse response = hc.execute(hg);
//            if (response.getStatusLine().getStatusCode() / 100 == 4) {
//                throw new Exception("Unauthorized.");
//            }
//            try {
//                return response.getEntity().getContent();
//            } catch (IllegalStateException e) {
//                throw new Exception(A.errorMsg(e));
//            } catch (IOException e2) {
//                throw new Exception(A.errorMsg(e2));
//            }
//        } catch (ClientProtocolException e1) {
//            throw new Exception(e1.getMessage());
//        } catch (IOException e12) {
//            throw new Exception(e12.getMessage());
//        }
//    }

    public static InputStream String2InputStream(String str) {
        try {
            return new ByteArrayInputStream(str.getBytes());
        } catch (OutOfMemoryError e) {
            System.gc();
        } catch (Exception e2) {
            A.error(e2);
        }
        return null;
    }

    public static InputSource String2InputSource(String str) {
        return new InputSource(new StringReader(str));
    }

    public static String inputStream2String(InputStream is) {
        return null;
    }

    public static String inputStream2String(InputStream is, String encoding) {
        return null;
    }


    public static String string2Encode(String s, String old_encode, String new_encode) {
        try {
            return new String(s.getBytes(old_encode), new_encode);
        } catch (Exception e) {
            A.error(e);
            return s;
        }
    }

    public static String stringList2Text(ArrayList<String> categories) {
        StringBuilder sb = new StringBuilder();
        Iterator it = categories.iterator();
        while (it.hasNext()) {
            sb.append(((String) it.next()) + "\n");
        }
        return sb.toString();
    }

    public static ArrayList<String> text2StringList(String text) {
        return text2StringList(text, false);
    }

    public static ArrayList<String> text2StringList(String text, boolean anti_order) {
        ArrayList<String> al = new ArrayList();
        int j = 0;
        if (!isNull(text)) {
            while (true) {
                int i = text.indexOf("\n", j);
                if (i == -1) {
                    break;
                }
                if (anti_order) {
                    al.add(0, text.substring(j, i));
                } else {
                    al.add(text.substring(j, i));
                }
                j = i + 1;
            }
            if (j < text.length() - 1) {
                if (anti_order) {
                    al.add(0, text.substring(j));
                } else {
                    al.add(text.substring(j));
                }
            }
        }
        return al;
    }

    public static ArrayList<String> inputStream2StringList(InputStream is) {
        try {
            return inputStream2StringList(is, "UTF-8");
        } catch (Exception e) {
            A.error(e);
            return null;
        }
    }

    public static ArrayList<String> inputStream2StringList(InputStream is, String encoding) throws Exception {
        if (is == null) {
            return null;
        }
        ArrayList<String> al = new ArrayList();
        try {
            BufferedReader in;
            if (encoding.equals("")) {
                in = new BufferedReader(new InputStreamReader(is));
            } else {
                in = new BufferedReader(new InputStreamReader(is, encoding));
            }
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    return al;
                }
                al.add(line);
            }
        } catch (Exception e) {
            A.error(e);
            return al;
        }
    }

    public static byte[] InputStream2Byte(InputStream is) {
        byte[] bArr = null;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];
        while (true) {
            try {
                int nRead = is.read(data, 0, data.length);
                if (nRead == -1) {
                    break;
                }
                buffer.write(data, 0, nRead);
            } catch (OutOfMemoryError e) {
            } catch (Exception e2) {
                A.error(e2);
            }
        }
        try {
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bArr = buffer.toByteArray();
        return bArr;
    }

    public static InputStream Byte2InputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    public static boolean inputStream2File(InputStream is, String filename) {
        boolean z = false;
        if (createFolder(getFilePath(filename))) {
            OutputStream out = null;
            try {
                out = getFileOutputStream(filename);
                if (out == null) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e) {
                            A.error(e);
                        }
                    }
                    if (is != null) {
                        is.close();
                    }
                } else {
                    byte[] bs = new byte[8192];
                    while (true) {
                        int len = is.read(bs);
                        if (len == -1) {
                            break;
                        }
                        out.write(bs, 0, len);
                    }
                    z = true;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e2) {
                            A.error(e2);
                        }
                    }
                    if (is != null) {
                        is.close();
                    }
                }
            } catch (Exception e22) {
                A.error(e22);
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e222) {
                        A.error(e222);
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e2222) {
                        A.error(e2222);
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return z;
    }

    public static boolean appendInputStream2File(InputStream is, String filename) {
        OutputStream out = null;
        try {
            if (!createFolder(getFilePath(filename))) {
                return false;
            }
            out = null;
            out = getFileOutputStream(filename, true);
            if (out == null) {
                if (out != null) {
                    out.close();
                }
                if (is == null) {
                    return false;
                }
                is.close();
                return false;
            }
            byte[] bs = new byte[8192];
            while (true) {
                int len = is.read(bs);
                if (len == -1) {
                    break;
                }
                out.write(bs, 0, len);
            }
            if (out != null) {
                out.close();
            }
            if (is != null) {
                is.close();
            }
            return true;
        } catch (Exception e) {
            A.error(e);
            return false;
        } catch (Throwable th) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static InputStream file2InputStream(String filename) {
        try {
            return new DataInputStream(new FileInputStream(filename));
        } catch (FileNotFoundException e) {
            A.error(e);
            return null;
        }
    }

    public static String getFileExt(String filename) {
        if (filename == null) {
            return "";
        }
        int dot = filename.lastIndexOf(".");
        if (dot == -1) {
            return "";
        }
        return filename.substring(dot, filename.length()).toLowerCase();
    }

    public static String getFilename(String filename) {
        if (filename == null) {
            return "";
        }
        int dot = filename.indexOf("?");
        if (dot != -1) {
            filename = filename.substring(dot + 1);
        }
        dot = filename.lastIndexOf("/");
        return dot != -1 ? filename.substring(dot + 1, filename.length()) : filename;
    }

    public static String getOnlyFilename(String filename) {
        if (filename == null) {
            return "";
        }
        String name = getFilename(filename);
        int dot = name.lastIndexOf(".");
        return dot != -1 ? name.substring(0, dot) : name;
    }

    public static String getFilePath(String filename) {
        if (isNull(filename)) {
            return "";
        }
        int dot = filename.lastIndexOf("/");
        if (dot == -1) {
            return "";
        }
        return filename.substring(0, dot);
    }

    public static long getFileSize(String filename) {
        if (isNull(filename)) {
            return -1;
        }
        File afile = new File(filename);
        if (afile.isFile()) {
            return afile.length();
        }
        return -1;
    }

    public static boolean isFile(String filename) {
        if (isNull(filename)) {
            return false;
        }
        return new File(filename).isFile();
    }

    public static boolean isEmptyFile(String filename) {
        if (filename == null || filename.equals("")) {
            return true;
        }
        File afile = new File(filename);
        if (!afile.isFile() || afile.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isFolder(String folder) {
        if (folder == null || folder.equals("")) {
            return false;
        }
        return new File(folder).isDirectory();
    }

    public static ArrayList<String> getFolderFileList(String path, boolean includeSubFolder, boolean listFilesOnly, boolean includeHiddenFiles, boolean includePathSelf) {
        return getFolderFileList(path, includeSubFolder, listFilesOnly, includeHiddenFiles, includePathSelf, null, 0);
    }

    public static ArrayList<String> getFolderFileList(String path, boolean includeSubFolder, boolean listFilesOnly, boolean includeHiddenFiles, boolean includePathSelf, Handler handler, int msgWhat) {
        ArrayList<String> al = new ArrayList();
        if (includePathSelf) {
            al.add(path);
        }
        if (handler != null) {
            try {
                handler.removeMessages(msgWhat);
                handler.sendMessage(handler.obtainMessage(msgWhat, path));
            } catch (OutOfMemoryError e) {
                A.error(e);
                System.gc();
            } catch (Exception e2) {
                A.error(e2);
            } catch (Throwable e3) {
                A.error(e3);
            }
        }
        File[] files = new File(path).listFiles();
        if (files != null) {
            if (includeSubFolder) {
                for (File file : files) {
                    if (scanCanceled) {
                        break;
                    }
                    if (file.isFile() && (includeHiddenFiles || !file.getName().startsWith("."))) {
                        al.add(file.getAbsolutePath());
                    }
                    if (file.isDirectory() && (includeHiddenFiles || !file.getName().startsWith("."))) {
                        al.addAll(getFolderFileList(file.getAbsolutePath(), true, listFilesOnly, includeHiddenFiles, includePathSelf, handler, msgWhat));
                    }
                }
            } else {
                for (File file2 : files) {
                    if (file2.isFile()) {
                        if (includeHiddenFiles || !file2.getName().startsWith(".")) {
                            al.add(file2.getAbsolutePath());
                        }
                    } else if (!listFilesOnly && (includeHiddenFiles || !file2.getName().startsWith("."))) {
                        al.add(file2.getAbsolutePath());
                    }
                }
            }
        }
        return al;
    }

    public static String getFileText(String filename) {
        return getFileText(filename, "UTF-8");
    }

    public static String getFileText(String filename, String encoding) {
        return null;
    }


    public static boolean saveFileText(String filename, String text, String encoding) {
        return false;
    }

    public static boolean saveFileText(String filename, String text) {
        return false;
    }

    public static boolean saveNormalFileText(String filename, String text) {
        Exception e;
        OutOfMemoryError e2;
        Throwable th;
        BufferedReader bufferedReader = null;
        try {
            if (createFolder(getFilePath(filename))) {
                File distFile = new File(filename);
                BufferedReader bufferedReader2 = new BufferedReader(new StringReader(text));
                try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(distFile));
                    char[] buf = new char[1024];
                    while (true) {
                        int len = bufferedReader2.read(buf);
                        if (len == -1) {
                            break;
                        }
                        bufferedWriter.write(buf, 0, len);
                    }
                    bufferedWriter.flush();
                    bufferedReader2.close();
                    bufferedWriter.close();
                    if (bufferedReader2 != null) {
                        try {
                            bufferedReader2.close();
                        } catch (Exception e3) {
                            A.error(e3);
                        }
                    }
                    bufferedReader = bufferedReader2;
                    return true;
                } catch (OutOfMemoryError e4) {
                    e2 = e4;
                    bufferedReader = bufferedReader2;
                    try {
                        A.error(e2);
                        if (bufferedReader != null) {
                            return false;
                        }
                        try {
                            bufferedReader.close();
                            return false;
                        } catch (Exception e32) {
                            A.error(e32);
                            return false;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (Exception e322) {
                                A.error(e322);
                            }
                        }
                        throw null;
                    }
                } catch (Exception e5) {

                    bufferedReader = bufferedReader2;
                    if (!SD.isSDFile(filename)) {
                        A.error(e5.getCause());
                    }
                    if (bufferedReader != null) {
                        return false;
                    }
                    try {
                        bufferedReader.close();
                        return false;
                    } catch (Exception e3222) {
                        A.error(e3222);
                        return false;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    throw null;
                }
            } else if (bufferedReader == null) {
                return false;
            } else {
                try {
                    bufferedReader.close();
                    return false;
                } catch (Exception e32222) {
                    A.error(e32222);
                    return false;
                }
            }
        } catch (OutOfMemoryError e6) {
            e2 = e6;
            A.error(e2);
            if (bufferedReader != null) {
                return false;
            }
            try {
                bufferedReader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        } catch (Exception e7) {

            if (SD.isSDFile(filename)) {
                A.error(e7.getCause());
            }
            if (bufferedReader != null) {
                return false;
            }
            try {
                bufferedReader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }

    public static boolean appendFileText(String filename, String text) {
        if (isFile(filename)) {
            return saveFileText(filename, getFileText(filename) + "\n" + text);
        }
        return saveFileText(filename, text);
    }

    public static int myRandom(int range) {
        return new Double(Math.ceil(Math.random() * ((double) range))).intValue() - 1;
    }

    public static String deleteSpecialChar(String str) {
        try {
            str = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]").matcher(str).replaceAll("").trim().replace("\n", "");
        } catch (Exception e) {
            A.error(e);
        }
        return str;
    }

    public static void showToastText(Context context, CharSequence text) {
        showToastText(context, text, 0);
        A.log("Toast: " + text);
    }

    public static void showToastText(Context context, CharSequence text, int duration) {
        if (A.isKindleFire && !(text instanceof Spanned)) {
            text = forceWhite(text);
        }
        if (mToast != null) {
            mToast.setText(text);
            mToast.setDuration(duration);
        } else {
            mToast = Toast.makeText(context, text, duration);
        }
        mToast.show();
    }

    public static void showToastText(Context context, String title, String text, int duration) {
        StringBuilder append = new StringBuilder().append("<b><font color=\"#FFFF00\">").append(title).append("</font></b><br>");
        if (A.isKindleFire) {
            text = String.valueOf(forceWhite(text));
        }
        CharSequence html = Html.fromHtml(append.append(text).toString());
        if (mToast != null) {
            mToast.setText(html);
            mToast.setDuration(duration);
        } else {
            mToast = Toast.makeText(context, html, duration);
        }
        mToast.show();
    }

    public static void hideToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    public static CharSequence forceWhite(CharSequence text) {
        return Html.fromHtml("<font color=\"#FFFFFF\">" + text + "</font>");
    }

    public static void showAlertText(Context context, CharSequence text) {
        try {
            new AlertDialog.Builder(context).setMessage(text).setCancelable(false).setPositiveButton("17039370", null).show();
        } catch (Exception e) {
            A.error(e);
        }
    }

    public static void showAlertText(Context context, CharSequence title, CharSequence text) {
        if (title == null || title.length() == 0) {
            showAlertText(context, text);
            return;
        }
        try {
            new AlertDialog.Builder(context).setTitle(title).setMessage(text).setCancelable(false).setPositiveButton("17039370", null).show();
        } catch (Exception e) {
            A.error(e);
        }
    }

    public static String time() {
        return time(true, false, Locale.US);
    }

    public static String time(boolean showSecond, boolean use12Hour, Locale locale) {
        return time(showSecond, use12Hour, locale, System.currentTimeMillis());
    }

    public static String time(boolean showSecond, boolean use12Hour, Locale locale, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("", locale);
        boolean isAsiaLocale = false;
        if (use12Hour) {
            if (locale.getLanguage().equals("zh") || locale.getLanguage().equals("jp") || locale.getLanguage().equals("ko")) {
                isAsiaLocale = true;
            } else {
                isAsiaLocale = false;
            }
            if (showSecond) {
                if (isAsiaLocale) {
                    sdf.applyPattern("a hh:mm:ss");
                } else {
                    sdf.applyPattern("hh:mm:ss a");
                }
            } else if (isAsiaLocale) {
                sdf.applyPattern("a hh:mm");
            } else {
                sdf.applyPattern("hh:mm a");
            }
        } else if (showSecond) {
            sdf.applyPattern("HH:mm:ss");
        } else {
            sdf.applyPattern("HH:mm");
        }
        String str = sdf.format(Long.valueOf(time));
        if (!isAsiaLocale) {
            return str;
        }
        if (str.startsWith("AM ") || str.startsWith("PM ")) {
            return str.substring(3) + " " + str.substring(0, 2);
        }
        return str;
    }

    public static String dateTimeToStr(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.US);
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    public static String dateToStr(Long time, Locale locale) {
        return DateFormat.getDateInstance(2, locale).format(new Date(time.longValue()));
    }

    public static String chinaTime() {
        return chinaTime(Long.valueOf(System.currentTimeMillis()));
    }

    public static String chinaTime(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
        sdf.applyPattern("yyyy年MM月dd日 HH时mm分ss秒");
        return sdf.format(time);
    }

    public static String getMatcherText(String regEx, String text) {
        Matcher matcher = Pattern.compile(regEx).matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static ArrayList<String> getMatcherTexts(String regEx, String text) {
        Matcher matcher = Pattern.compile(regEx).matcher(text);
        ArrayList<String> sl = new ArrayList();
        while (matcher.find()) {
            sl.add(matcher.group());
        }
        return sl;
    }

    public static boolean filenameMatch(String pattern, String filename) {
        return filenameMatch(pattern, filename, true);
    }

    public static boolean filenameMatch(String pattern, String filename, boolean checkEmptyAndPart) {
        if (checkEmptyAndPart && (pattern.equals("") || filename.indexOf(pattern) != -1)) {
            return true;
        }
        int patternLength = pattern.length();
        int strLength = filename.length();
        int strIndex = 0;
        for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
            char ch = pattern.charAt(patternIndex);
            if (ch == '*') {
                while (strIndex < strLength) {
                    if (filenameMatch(pattern.substring(patternIndex + 1), filename.substring(strIndex), false)) {
                        return true;
                    }
                    strIndex++;
                }
                continue;
            } else if (ch == '?') {
                strIndex++;
                if (strIndex > strLength) {
                    return false;
                }
            } else if (strIndex >= strLength || ch != filename.charAt(strIndex)) {
                return false;
            } else {
                strIndex++;
            }
        }
        if (strIndex != strLength) {
            return false;
        }
        return true;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static String getScreenDescription(Context context) {
        int w = context.getResources().getDisplayMetrics().widthPixels;
        int h = context.getResources().getDisplayMetrics().heightPixels;
        return w < h ? w + "x" + h : h + "x" + w;
    }

    public static Bitmap zoomImage(Bitmap bm, int newWidth, int newHeight) {
        if (newWidth <= 0) {
            newWidth = 1;
        }
        if (newHeight <= 0) {
            newHeight = 1;
        }
        try {
            int width = bm.getWidth();
            int height = bm.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(((float) newWidth) / ((float) width), ((float) newHeight) / ((float) height));
            bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        } catch (OutOfMemoryError e) {
            System.gc();
        } catch (Exception e2) {
            A.error(e2);
        }
        return bm;
    }

    public static Drawable zoomDrawable(Resources res, Drawable d, int w, int h) {
        try {
            if (d instanceof BitmapDrawable) {
                return new BitmapDrawable(res, Bitmap.createScaledBitmap(((BitmapDrawable) d).getBitmap(), w, h, true));
            }
            return new BitmapDrawable(res, drawableToBitmap(d, w, h));
        } catch (Exception e) {
            A.error(e);
            return d;
        }
    }

    public static Bitmap drawableToBitmap(Drawable d) {
        if (d == null) {
            return null;
        }
        if (d instanceof BitmapDrawable) {
            return ((BitmapDrawable) d).getBitmap();
        }
        return drawableToBitmap(d, d.getIntrinsicWidth(), d.getIntrinsicHeight());
    }

    public static Bitmap drawableToBitmap(Drawable d, int width, int height) {
        try {
            if ((d instanceof BitmapDrawable) && d.getIntrinsicWidth() == width && d.getIntrinsicHeight() == height) {
                return ((BitmapDrawable) d).getBitmap();
            }
            Config config =  Config.RGB_565;
            if (width <= 0) {
                width = 1;
            }
            if (height <= 0) {
                height = 1;
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(bitmap);
            d.setBounds(0, 0, width, height);
            d.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            System.gc();
            A.error(e);
            return null;
        } catch (Exception e2) {
            A.error(e2);
            return null;
        }
    }

    public static boolean drawableToFile(Drawable d, String filename) {
        try {
            return bitmapToFile(drawableToBitmap(d, d.getIntrinsicWidth(), d.getIntrinsicHeight()), filename);
        } catch (OutOfMemoryError e) {
            A.error(e);
            return false;
        } catch (Exception e2) {
            A.error(e2);
            return false;
        }
    }

    public static Drawable bitmapToDrawble(Context context, Bitmap bm) {
        return context != null ? new BitmapDrawable(context.getResources(), bm) : new BitmapDrawable(bm);
    }

    public static boolean bitmapToFile(Bitmap bm, String filename) {
        boolean z = false;
        if (createFolder(getFilePath(filename))) {
            OutputStream out = null;
            try {
                out = getFileOutputStream(filename);
                if (out != null) {
                    if (filename.endsWith(".jpg")) {
                        bm.compress(CompressFormat.JPEG, 90, out);
                    } else {
                        bm.compress(CompressFormat.PNG, 100, out);
                    }
                    out.flush();
                    out.close();
                    z = true;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e) {
                            A.error(e);
                        }
                    }
                } else if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e2) {
                        A.error(e2);
                    }
                }
            } catch (OutOfMemoryError e3) {
                System.gc();
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e22) {
                        A.error(e22);
                    }
                }
            } catch (Exception e222) {
                A.error(e222);
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e2222) {
                        A.error(e2222);
                    }
                }
            } catch (Throwable th) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e22222) {
                        A.error(e22222);
                    }
                }
            }
        }
        return z;
    }

    public static OutputStream getFileOutputStream(String filename) {
        return getFileOutputStream(filename, false);
    }

    public static OutputStream getFileOutputStream(String filename, boolean append) {
        try {
            return new FileOutputStream(filename, append);
        } catch (Exception e) {
            if (SD.isSDFile(filename)) {
                return SD.getDocumentFileOutputStream(filename, append);
            }
            return null;
        }
    }

    public static String getPercentStr(long pos, long total) {
        if (pos < 0) {
            pos = 0;
        }
        if (pos > total) {
            pos = total;
        }
        String s = total == 0 ? "0.0%" : (((double) (((1000 * pos) / total) * 100)) / 1000.0d) + "%";
        if (s.equals("100.0%")) {
            return "100%";
        }
        return s;
    }

    public static boolean moveFile(String oldFilename, String destFilename, boolean overwrite) {
        if (moveNormalFile(oldFilename, destFilename, overwrite)) {
            return true;
        }
        if (SD.isSDFile(oldFilename) || SD.isSDFile(destFilename)) {
            return moveDocumentFile(oldFilename, destFilename, overwrite);
        }
        return false;
    }

    public static boolean moveNormalFile(String oldFilename, String destFilename, boolean overwrite) {
        File srcFile = new File(oldFilename);
        if (!srcFile.isFile()) {
            return false;
        }
        File destFile = new File(destFilename);
        if (destFile.isFile() && (!overwrite || !destFile.delete())) {
            return false;
        }
        if ((!destFile.isDirectory() || (overwrite && deleteFolder(destFilename))) && createFolder(getFilePath(destFilename)) && srcFile.renameTo(destFile)) {
            return true;
        }
        return false;
    }

    private static boolean moveDocumentFile(String oldFilename, String destFilename, boolean overwrite) {
        if (oldFilename.toLowerCase().equals(destFilename.toLowerCase())) {
            return true;
        }
        if (!new File(oldFilename).isFile()) {
            return false;
        }
        if (new File(destFilename).isFile()) {
            if (!overwrite) {
                return false;
            }
            if (!deleteFile(destFilename)) {
                return false;
            }
        }
        if (!copyFile(oldFilename, destFilename, true)) {
            return false;
        }
        deleteFile(oldFilename);
        return true;
    }

    public static boolean renameFile(String oldFilename, String destFilename, boolean overwrite) {
        File srcFile = new File(oldFilename);
        if (!srcFile.isFile() && !srcFile.isDirectory()) {
            return false;
        }
        File destFile = new File(destFilename);
        if (destFile.isFile() || destFile.isDirectory()) {
            if (!overwrite) {
                return false;
            }
            if (!(oldFilename.toLowerCase().equals(destFilename.toLowerCase()) || deleteFile(destFilename))) {
                return false;
            }
        }
        if (srcFile.renameTo(destFile)) {
            return true;
        }
        if (!SD.isSDFile(oldFilename)) {
            return false;
        }
        DocumentFile df = SD.getDocumentFile(oldFilename);
        if (df == null || !df.renameTo(getFilename(destFilename))) {
            return false;
        }
        return true;
    }

    public static boolean deleteFile(String filename) {
        File file = new File(filename);
        if (file.isFile() || file.isDirectory()) {
            if (file.delete()) {
                return true;
            }
            if (SD.isSDFile(filename)) {
                DocumentFile df = SD.getDocumentFile(filename);
                if (df != null && df.delete()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean copyFile(String oldFilename, String destFilename, boolean overwrite) {
        Exception e;
        Throwable th;
        if (!isFile(oldFilename)) {
            return false;
        }
        if (oldFilename.toLowerCase().equals(destFilename.toLowerCase())) {
            return true;
        }
        File destFile = new File(destFilename);
        if (destFile.isFile() && (!overwrite || !deleteFile(destFilename))) {
            return false;
        }
        if ((destFile.isDirectory() && (!overwrite || !deleteFolder(destFilename))) || !createFolder(getFilePath(destFilename))) {
            return false;
        }
        OutputStream out = null;
        InputStream inputStream = null;
        try {
            out = getFileOutputStream(destFilename);
            if (out == null) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e2) {
                        A.error(e2);
                        return false;
                    }
                }
                if (inputStream == null) {
                    return false;
                }
                inputStream.close();
                return false;
            }
            InputStream in = new FileInputStream(oldFilename);
            try {
                byte[] buffer = new byte[8192];
                while (true) {
                    int byteread = in.read(buffer);
                    if (byteread == -1) {
                        break;
                    }
                    out.write(buffer, 0, byteread);
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e22) {
                        A.error(e22);
                    }
                }
                if (in != null) {
                    in.close();
                }
                return true;
            } catch (Exception e3) {
                inputStream = in;
                try {
                    A.error(e3.getCause());
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e222) {
                            A.error(e222);
                            return false;
                        }
                    }
                    if (inputStream != null) {
                        return false;
                    }
                    inputStream.close();
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (Exception e2222) {
                            A.error(e2222);
                            throw null;
                        }
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    throw null;
                }
            } catch (Throwable th3) {
                th = th3;
                inputStream = in;
                if (out != null) {
                    out.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                throw null;
            }
        } catch (Exception e4) {
            A.error(e4.getCause());
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (inputStream != null) {
                return false;
            }
            try {
                inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }

    public static boolean deleteFolder(String fullname) {
        if (deleteNormalFolder(fullname)) {
            return true;
        }
        if (SD.isSDFile(fullname)) {
            DocumentFile df = SD.getDocumentFile(fullname);
            if (df != null && df.delete()) {
                return true;
            }
        }
        return false;
    }

    public static boolean deleteNormalFolder(String fullname) {
        if (!isFolder(fullname)) {
            return false;
        }
        try {
            ArrayList<String> list = getFolderFileList(fullname, true, false, true, true);
            for (int i = list.size() - 1; i >= 0; i--) {
                if (!new File((String) list.get(i)).delete()) {
                    return false;
                }
            }
        } catch (Exception e) {
            A.error(e);
        }
        return true;
    }

    public static boolean createFolder(String path) {
        if (createNormalFolder(path)) {
            return true;
        }
        if (!SD.isSDFile(path) || SD.getDocumentFile(path, true, true) == null) {
            return false;
        }
        return true;
    }

    public static boolean createNormalFolder(String path) {
        if (isNull(path)) {
            return false;
        }
        File folder = new File(path);
        if (folder.isFile()) {
            return false;
        }
        if (folder.exists() || folder.mkdirs()) {
            return true;
        }
        return false;
    }

    public static void openUrl(Activity activity, String url) {
        try {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
        } catch (Exception e) {
            A.error(e);
        }
    }

    public static String getHtmlBody(String html) {
        if (html == null) {
            return null;
        }
        try {
            String html2 = html.toLowerCase();
            int i1 = html2.indexOf("<body");
            int i2;
            if (i1 != -1) {
                i1 = html2.indexOf(">", i1);
                i2 = html2.indexOf("</body>", i1);
                if (i2 != -1) {
                    return html.substring(i1 + 1, i2);
                }
                return html.substring(i1 + 1, html.length());
            }
            i1 = html2.indexOf("<html");
            if (i1 == -1) {
                return html;
            }
            i1 = html2.indexOf(">", i1);
            i2 = html2.indexOf("</html>", i1);
            if (i2 != -1) {
                return html.substring(i1 + 1, i2);
            }
            return html.substring(i1 + 1, html.length());
        } catch (OutOfMemoryError e) {
            System.gc();
            return html;
        } catch (Exception e2) {
            A.error(e2);
            return html;
        }
    }

    public static String deleteHtmlStyle(String html) {
        try {
            int i1;
            int i2;
            String html2 = html.toLowerCase();
            while (true) {
                i1 = html2.indexOf("<style");
                if (i1 != -1) {
                    i2 = html2.indexOf("</style>");
                    if (i2 == -1 || i2 <= i1) {
                        break;
                    }
                    html = html.substring(0, i1) + html.substring(i2 + 8);
                    html2 = html.toLowerCase();
                } else {
                    break;
                }
            }
            while (true) {
                i1 = html2.indexOf("<script");
                if (i1 != -1) {
                    i2 = html2.indexOf("</script>");
                    if (i2 == -1 || i2 <= i1) {
                        break;
                    }
                    html = html.substring(0, i1) + html.substring(i2 + 9);
                    html2 = html.toLowerCase();
                } else {
                    break;
                }
            }
        } catch (OutOfMemoryError e) {
            System.gc();
        } catch (Exception e2) {
            A.error(e2);
        }
        return html;
    }

    public static String[][] r0;
    static {
        r0 = new String[52][];
        r0[0] = new String[]{".3gp", "video/3gpp"};
        r0[1] = new String[]{".apk", "application/vnd.android.package-archive"};
        r0[2] = new String[]{".asf", "video/x-ms-asf"};
        r0[3] = new String[]{".avi", "video/x-msvideo"};
        r0[4] = new String[]{".bin", "application/octet-stream"};
        r0[5] = new String[]{".bmp", "image/bmp"};
        r0[6] = new String[]{".class", "application/octet-stream"};
        r0[7] = new String[]{".doc", "application/msword"};
        r0[8] = new String[]{".exe", "application/octet-stream"};
        r0[9] = new String[]{".gif", "image/gif"};
        r0[10] = new String[]{".gtar", "application/x-gtar"};
        r0[11] = new String[]{".gz", "application/x-gzip"};
        r0[12] = new String[]{".jar", "application/java-archive"};
        r0[13] = new String[]{".jpeg", "image/jpeg"};
        r0[14] = new String[]{".jpg", "image/jpeg"};
        r0[15] = new String[]{".m3u", "audio/x-mpegurl"};
        r0[16] = new String[]{".m4a", "audio/mp4a-latm"};
        r0[17] = new String[]{".m4b", "audio/mp4a-latm"};
        r0[18] = new String[]{".m4p", "audio/mp4a-latm"};
        r0[19] = new String[]{".m4u", "video/vnd.mpegurl"};
        r0[20] = new String[]{".m4v", "video/x-m4v"};
        r0[21] = new String[]{".mov", "video/quicktime"};
        r0[22] = new String[]{".mp2", "audio/x-mpeg"};
        r0[23] = new String[]{".mp3", "audio/x-mpeg"};
        r0[24] = new String[]{".mp4", "video/mp4"};
        r0[25] = new String[]{".mpc", "application/vnd.mpohun.certificate"};
        r0[26] = new String[]{".mpe", "video/mpeg"};
        r0[27] = new String[]{".mpeg", "video/mpeg"};
        r0[28] = new String[]{".mpg", "video/mpeg"};
        r0[29] = new String[]{".mpg4", "video/mp4"};
        r0[30] = new String[]{".mpga", "audio/mpeg"};
        r0[31] = new String[]{".msg", "application/vnd.ms-outlook"};
        r0[32] = new String[]{".ogg", "audio/ogg"};
        r0[33] = new String[]{".pdf", "application/pdf"};
        r0[34] = new String[]{".doc", "application/doc"};
        r0[35] = new String[]{".doc", "application/mkv"};
        r0[36] = new String[]{A.NETCOVER_TAG, "image/png"};
        r0[37] = new String[]{".pps", "application/vnd.ms-powerpoint"};
        r0[38] = new String[]{".ppt", "application/vnd.ms-powerpoint"};
        r0[39] = new String[]{".rar", "application/x-rar-compressed"};
        r0[40] = new String[]{".rmvb", "video/x-pn-realaudio"};
        r0[41] = new String[]{".rm", "video/x-pn-realaudio"};
        r0[42] = new String[]{".rtf", "application/rtf"};
        r0[43] = new String[]{".tar", "application/x-tar"};
        r0[44] = new String[]{".tgz", "application/x-compressed"};
        r0[45] = new String[]{".wav", "audio/x-wav"};
        r0[46] = new String[]{".wma", "audio/x-ms-wma"};
        r0[47] = new String[]{".wmv", "audio/x-ms-wmv"};
        r0[48] = new String[]{".wps", "application/vnd.ms-works"};
        r0[49] = new String[]{".z", "application/x-compress"};
        r0[50] = new String[]{".zip", "application/zip"};
        r0[51] = new String[]{"", "*/*"};
        MIME_MapTable = r0;
    }

    public static String getMIMEType(String filename) {
        String type1 = "*/*";
        if (filename == null) {
            return type1;
        }
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex < 0) {
            return type1;
        }
        String ext = filename.substring(dotIndex, filename.length()).toLowerCase();
        if (ext == "") {
            return type1;
        }
        String type2 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(filename.substring(dotIndex + 1, filename.length()));
        if (type2 != null) {
            return type2;
        }
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (ext.equals(MIME_MapTable[i][0])) {
                return MIME_MapTable[i][1];
            }
        }
        return type1;
    }

    public static void openFileWithDefaultApp(Activity activity, String filename) {
        Intent intent;
        try {
            intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse("file://" + filename), getMIMEType(filename));
            activity.startActivity(intent);
        } catch (Exception e) {
            A.error(e);
            try {
                intent = new Intent("android.intent.action.VIEW");
                intent.setDataAndType(Uri.parse("file://" + filename), "*/*");
                activity.startActivity(intent);
            } catch (Exception e2) {
                A.error(e);
            }
        }
    }

    public static int string2Int(String s) {
        int i = 0;
        if (!isNull(s)) {
            int i2 = 0;
            while (i2 < s.length() && (s.charAt(i2) < '0' || s.charAt(i2) > '9')) {
                i2++;
            }
            int start = i2;
            while (i2 < s.length() && s.charAt(i2) >= '0' && s.charAt(i2) <= '9') {
                i2++;
            }
            if (start != i2) {
                try {
                    i = Integer.valueOf(s.substring(start, i2)).intValue();
                } catch (Exception e) {
                    A.error(e);
                }
            }
        }
        return i;
    }

    public static float string2Float(String s) {
        float f = 0.0f;
        if (!isNull(s)) {
            int i = 0;
            while (i < s.length() && s.charAt(i) != '.' && s.charAt(i) != '-' && (s.charAt(i) < '0' || s.charAt(i) > '9')) {
                i++;
            }
            int start = i;
            while (i < s.length() && (s.charAt(i) == '.' || s.charAt(i) == '-' || (s.charAt(i) >= '0' && s.charAt(i) <= '9'))) {
                i++;
            }
            if (start != i) {
                try {
                    f = Float.valueOf(s.substring(start, i)).floatValue();
                } catch (Exception e) {
                    A.error(e);
                }
            }
        }
        return f;
    }

    public static boolean charIsNumber(char c) {
        return c >= '0' && c <= '9';
    }

    public static String buildString(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].length() > 0) {
                sb.append(strings[i]);
            }
        }
        return sb.toString();
    }

    public static String getEmailFileName(ContentResolver resolver, Uri uri) {
        Cursor cursor;
        int nameIndex;
        String result = null;
        try {
            cursor = resolver.query(uri, new String[]{"_display_name"}, null, null, null);
            cursor.moveToFirst();
            nameIndex = cursor.getColumnIndex("_display_name");
            if (nameIndex >= 0) {
                result = cursor.getString(nameIndex);
            }
        } catch (Exception e) {
            A.error(e);
            result = null;
        }
        if (result != null) {
            return result;
        }
        try {
            cursor = resolver.query(uri, null, null, null, null);
            cursor.moveToFirst();
            nameIndex = cursor.getColumnIndex("_display_name");
            if (nameIndex >= 0) {
                return cursor.getString(nameIndex);
            }
            return result;
        } catch (Exception e2) {
            A.error(e2);
            return null;
        }
    }

    public static String getFileTypeFromBinary(String binary) {
        if (binary.length() > 100) {
            binary = binary.substring(0, 100);
        }
        if (binary.indexOf("PNG") == 1) {
            return A.NETCOVER_TAG;
        }
        if (binary.indexOf("PDF") >= 0 && binary.indexOf("PDF") < 5) {
            return ".pdf";
        }
        if (binary.indexOf("GIF") == 0) {
            return ".gif";
        }
        if (binary.indexOf("JFIF") == 6) {
            return ".jpg";
        }
        if (binary.indexOf("application/epub+zip") == 38) {
            return ".epub";
        }
        if (binary.indexOf("PK") == 0) {
            return ".zip";
        }
        if (binary.indexOf("Rar!") == 0) {
            return ".rar";
        }
        return "";
    }

    public static int getWordsCount(String text, boolean isCJK) {
        if (!isCJK) {
            return new StringTokenizer(text, "\n,. :;/").countTokens();
        }
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.getType(c) == 5 || Character.isLetter(c)) {
                count++;
            }
        }
        return count;
    }

    public static String html2Text(String html) {
        try {
            html = deleteHtmlComment(html);
            StringBuilder sb = new StringBuilder();
            int start = html.indexOf("<body");
            if (start == -1) {
                start = html.indexOf("<BODY");
            }
            if (start == -1) {
                start = 0;
            }
            int i1 = -1;
            int i2 = -1;
            for (int i = start; i < html.length(); i++) {
                int c = html.charAt(i);
                if (c == 62) {
                    i1 = i;
                    i2 = -1;
                }
                if (c == 60) {
                    i2 = i;
                }
                if (i1 > 0 && i2 > i1 + 1) {
                    String s = html.substring(i1 + 1, i2).trim();
                    if (s.length() > 0) {
                        sb.append(s + " ");
                    }
                    i2 = -1;
                    i1 = -1;
                }
            }
            html = sb.toString();
        } catch (OutOfMemoryError e) {
            A.error(e);
        }
        return html;
    }

    public static String deleteHtmlTag(String html) {
        try {
            StringBuilder sb = new StringBuilder();
            boolean start = false;
            for (int i = 0; i < html.length(); i++) {
                char c = html.charAt(i);
                if (c == '<') {
                    start = true;
                }
                if (!start) {
                    sb.append(c);
                }
                if (c == '>') {
                    start = false;
                }
            }
            html = sb.toString();
        } catch (OutOfMemoryError e) {
            A.error(e);
        }
        return html;
    }

    public static int getDrawableAboutColor(Drawable d) {
        return getBitmapAboutColor(drawableToBitmap(d));
    }

    public static int getBitmapAboutColor(Bitmap bm) {
        return getBitmapAboutColor(bm, 1.0f, false, false);
    }

    public static int getBitmapAboutColor(Bitmap bm, float grayRate, boolean centerOnly, boolean includeAlpha) {
        if (bm == null) {
            return 0;
        }
        int w = bm.getWidth();
        int h = bm.getHeight();
        if (w <= 0 || h <= 0) {
            return 0;
        }
        int[][] colors = (int[][]) Array.newInstance(Integer.TYPE, new int[]{10, 4});
        for (int i = 0; i < 10; i++) {
            int c;
            if (centerOnly) {
                c = bm.getPixel((w / 3) + ((w / 3) * (i / 10)), (h / 3) + ((h / 3) * (i / 10)));
            } else {
                c = bm.getPixel(((i + 1) * w) / 12, ((i + 1) * h) / 12);
            }
            colors[i][0] = Color.red(c);
            colors[i][1] = Color.green(c);
            colors[i][2] = Color.blue(c);
            colors[i][3] = Color.alpha(c);
        }
        return Color.argb(includeAlpha ? getAverageColorValue(colors, 3, 1.0f) : 255, getAverageColorValue(colors, 0, grayRate), getAverageColorValue(colors, 1, grayRate), getAverageColorValue(colors, 2, grayRate));
    }

    private static int getAverageColorValue(int[][] colors, int column, float grayRate) {
        int count = 0;
        for (int[] iArr : colors) {
            count += iArr[column];
        }
        return (int) ((((float) count) * grayRate) / ((float) colors.length));
    }

    public static int getColorValue(int c) {
        return ((Color.red(c) + Color.green(c)) + Color.blue(c)) / 3;
    }

    public static boolean isNull(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNull(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean isNull(Activity act) {
        return act == null || act.isFinishing();
    }

    public static boolean extractFileFromAsset(AssetManager am, String asset_filename, String dest_filename) {
        boolean z = false;
        OutputStream out = null;
        InputStream in = null;
        try {
            out = getFileOutputStream(dest_filename);
            if (out == null) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        A.error(e);
                    }
                }
                if (in != null) {
                    in.close();
                }
            } else {
                in = am.open(asset_filename);
                byte[] bs = new byte[1024];
                while (true) {
                    int len = in.read(bs);
                    if (len == -1) {
                        break;
                    }
                    out.write(bs, 0, len);
                }
                z = true;
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e2) {
                        A.error(e2);
                    }
                }
                if (in != null) {
                    in.close();
                }
            }
        } catch (Exception e22) {
            A.error(e22);
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e222) {
                    A.error(e222);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e2222) {
                    A.error(e2222);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return z;
    }

    public static String mySimpleEncript(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = s.length() - 1; i >= 0; i--) {
            sb.append((char) ((i + 90) + s.charAt(i)));
        }
        return sb.toString();
    }

    public static String mySimpleDecript(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = s.length() - 1; i >= 0; i--) {
            sb.append((char) ((s.charAt(i) - 90) - ((s.length() - 1) - i)));
        }
        return sb.toString();
    }

    public static boolean isNetworkConnecting(Activity act) {

        return true;
    }

    public static boolean isWiFiConnecting(Activity act) {
        return true;
    }

    public static int getHours(long eclipsedTime) {
        return (int) (((eclipsedTime / 60) / 60) / 1000);
    }

    public static int getMinitues(long eclipsedTime) {
        return (int) ((eclipsedTime / 60) / 1000);
    }

    public static int getMinituesAfterHours(long eclipsedTime) {
        return (int) (((eclipsedTime % 3600000) / 60) / 1000);
    }

    public static boolean recycle(Bitmap bm) {
        if (bm == null) {
            return false;
        }
        bm.recycle();
        return true;
    }

    public static boolean isRecycled(Bitmap bm) {
        return bm == null || bm.isRecycled();
    }

    public static String deleteQuotes(String s) {
        s = s.trim();
        if (s.startsWith("'") || s.startsWith("\"") || s.startsWith("[")) {
            s = s.substring(1);
        }
        if (s.endsWith("'") || s.endsWith("\"") || s.endsWith("]") || s.endsWith(":")) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    public static String getMinuteTag(int minute) {
        return (minute < 10 ? "0" : "") + minute;
    }

    public static String combineString(String s1, String s2) {
        if (s1.length() > 0 && Character.getType(s1.charAt(s1.length() - 1)) != 5) {
            s1 = s1 + " ";
        }
        return s1 + s2;
    }

    public static String UrlEncode(String href) {
        return Uri.encode(href, "@#&=*+-_.,:!?()/~'%");
    }

    public static String deleteHtmlComment(String html) {
        int start = 0;
        while (true) {
            int i = html.indexOf("<!--", start);
            if (i == -1) {
                break;
            }
            int j = html.indexOf("-->", i);
            if (j == -1) {
                break;
            }
            html = html.substring(0, i) + html.substring(j + 3, html.length());
            start = i;
        }
        return html;
    }

    public static Object getLast(Spanned text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);
        if (objs.length == 0) {
            return null;
        }
        return objs[objs.length - 1];
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getStatFsTotal(StatFs sf) {
        return VERSION.SDK_INT >= 18 ? sf.getBlockCountLong() * sf.getBlockSizeLong() : (long) (sf.getBlockCount() * sf.getBlockSize());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getStatFsFree(StatFs sf) {
        return VERSION.SDK_INT >= 18 ? sf.getAvailableBlocksLong() * sf.getBlockSizeLong() : (long) (sf.getAvailableBlocks() * sf.getBlockSize());
    }

    public static String bytesToHuman(long size) {
        return bytesToHuman(size, false, false);
    }

    public static String bytesToHuman(long size, boolean numOnly, boolean noByte) {
        long Mb = PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID * PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        long Gb = Mb * PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        long Tb = Gb * PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        long Pb = Tb * PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        long Eb = Pb * PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        StringBuilder append;
        String str;
        if (size < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) {
            append = new StringBuilder().append(floatForm((double) size));
            str = numOnly ? "" : noByte ? "" : " byte";
            return append.append(str).toString();
        } else if (size >= PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID && size < Mb) {
            append = new StringBuilder().append(floatForm(((double) size) / ((double) 1024)));
            str = numOnly ? "" : noByte ? " K" : " KB";
            return append.append(str).toString();
        } else if (size >= Mb && size < Gb) {
            append = new StringBuilder().append(floatForm(((double) size) / ((double) Mb)));
            str = numOnly ? "" : noByte ? " M" : " MB";
            return append.append(str).toString();
        } else if (size >= Gb && size < Tb) {
            append = new StringBuilder().append(floatForm(((double) size) / ((double) Gb)));
            str = numOnly ? "" : noByte ? " G" : " GB";
            return append.append(str).toString();
        } else if (size >= Tb && size < Pb) {
            append = new StringBuilder().append(floatForm(((double) size) / ((double) Tb)));
            str = numOnly ? "" : noByte ? " T" : " TB";
            return append.append(str).toString();
        } else if (size >= Pb && size < Eb) {
            append = new StringBuilder().append(floatForm(((double) size) / ((double) Pb)));
            str = numOnly ? "" : noByte ? " P" : " PB";
            return append.append(str).toString();
        } else if (size < Eb) {
            return "0";
        } else {
            append = new StringBuilder().append(floatForm(((double) size) / ((double) Eb)));
            str = numOnly ? "" : noByte ? " E" : " EB";
            return append.append(str).toString();
        }
    }

    public static String floatForm(double d) {
        return new DecimalFormat("#.##").format(d);
    }

    public static String color2Html(int c) {
        return String.format("#%06X", new Object[]{Integer.valueOf(ViewCompat.MEASURED_SIZE_MASK & c)});
    }

    private static HashMap<String, Integer> buildColorMap() {
        HashMap<String, Integer> map = new HashMap();
        map.put("aliceblue", Integer.valueOf(15792383));
        map.put("antiquewhite", Integer.valueOf(16444375));
        map.put("aqua", Integer.valueOf(SupportMenu.USER_MASK));
        map.put("aquamarine", Integer.valueOf(8388564));
        map.put("azure", Integer.valueOf(15794175));
        map.put("beige", Integer.valueOf(16119260));
        map.put("bisque", Integer.valueOf(16770244));
        map.put("black", Integer.valueOf(0));
        map.put("blanchedalmond", Integer.valueOf(16772045));
        map.put("blue", Integer.valueOf(255));
        map.put("blueviolet", Integer.valueOf(9055202));
        map.put("brown", Integer.valueOf(10824234));
        map.put("burlywood", Integer.valueOf(14596231));
        map.put("cadetblue", Integer.valueOf(6266528));
        map.put("chartreuse", Integer.valueOf(8388352));
        map.put("chocolate", Integer.valueOf(13789470));
        map.put("coral", Integer.valueOf(16744272));
        map.put("cornflowerblue", Integer.valueOf(6591981));
        map.put("cornsilk", Integer.valueOf(16775388));
        map.put("crimson", Integer.valueOf(14423100));
        map.put("cyan", Integer.valueOf(SupportMenu.USER_MASK));
        map.put("darkblue", Integer.valueOf(139));
        map.put("darkcyan", Integer.valueOf(35723));
        map.put("darkgoldenrod", Integer.valueOf(12092939));
        map.put("darkgray", Integer.valueOf(11119017));
        map.put("darkgrey", Integer.valueOf(11119017));
        map.put("darkgreen", Integer.valueOf(25600));
        map.put("darkkhaki", Integer.valueOf(12433259));
        map.put("darkmagenta", Integer.valueOf(9109643));
        map.put("darkolivegreen", Integer.valueOf(5597999));
        map.put("darkorange", Integer.valueOf(16747520));
        map.put("darkorchid", Integer.valueOf(10040012));
        map.put("darkred", Integer.valueOf(9109504));
        map.put("darksalmon", Integer.valueOf(15308410));
        map.put("darkseagreen", Integer.valueOf(9419919));
        map.put("darkslateblue", Integer.valueOf(4734347));
        map.put("darkslategray", Integer.valueOf(3100495));
        map.put("darkslategrey", Integer.valueOf(3100495));
        map.put("darkturquoise", Integer.valueOf(52945));
        map.put("darkviolet", Integer.valueOf(9699539));
        map.put("deeppink", Integer.valueOf(16716947));
        map.put("deepskyblue", Integer.valueOf(49151));
        map.put("dimgray", Integer.valueOf(6908265));
        map.put("dimgrey", Integer.valueOf(6908265));
        map.put("dodgerblue", Integer.valueOf(2003199));
        map.put("firebrick", Integer.valueOf(11674146));
        map.put("floralwhite", Integer.valueOf(16775920));
        map.put("forestgreen", Integer.valueOf(2263842));
        map.put("fuchsia", Integer.valueOf(16711935));
        map.put("gainsboro", Integer.valueOf(14474460));
        map.put("ghostwhite", Integer.valueOf(16316671));
        map.put("gold", Integer.valueOf(16766720));
        map.put("goldenrod", Integer.valueOf(14329120));
        map.put("gray", Integer.valueOf(8421504));
        map.put("grey", Integer.valueOf(8421504));
        map.put("green", Integer.valueOf(32768));
        map.put("greenyellow", Integer.valueOf(11403055));
        map.put("honeydew", Integer.valueOf(15794160));
        map.put("hotpink", Integer.valueOf(16738740));
        map.put("indianred", Integer.valueOf(13458524));
        map.put("indigo", Integer.valueOf(4915330));
        map.put("ivory", Integer.valueOf(16777200));
        map.put("khaki", Integer.valueOf(15787660));
        map.put("lavender", Integer.valueOf(15132410));
        map.put("lavenderblush", Integer.valueOf(16773365));
        map.put("lawngreen", Integer.valueOf(8190976));
        map.put("lemonchiffon", Integer.valueOf(16775885));
        map.put("lightblue", Integer.valueOf(11393254));
        map.put("lightcoral", Integer.valueOf(15761536));
        map.put("lightcyan", Integer.valueOf(14745599));
        map.put("lightgoldenrodyellow", Integer.valueOf(16448210));
        map.put("lightgray", Integer.valueOf(13882323));
        map.put("lightgrey", Integer.valueOf(13882323));
        map.put("lightgreen", Integer.valueOf(9498256));
        map.put("lightpink", Integer.valueOf(16758465));
        map.put("lightsalmon", Integer.valueOf(16752762));
        map.put("lightseagreen", Integer.valueOf(2142890));
        map.put("lightskyblue", Integer.valueOf(8900346));
        map.put("lightslategray", Integer.valueOf(7833753));
        map.put("lightslategrey", Integer.valueOf(7833753));
        map.put("lightsteelblue", Integer.valueOf(11584734));
        map.put("lightyellow", Integer.valueOf(16777184));
        map.put("lime", Integer.valueOf(65280));
        map.put("limegreen", Integer.valueOf(3329330));
        map.put("linen", Integer.valueOf(16445670));
        map.put("magenta", Integer.valueOf(16711935));
        map.put("maroon", Integer.valueOf(8388608));
        map.put("mediumaquamarine", Integer.valueOf(6737322));
        map.put("mediumblue", Integer.valueOf(205));
        map.put("mediumorchid", Integer.valueOf(12211667));
        map.put("mediumpurple", Integer.valueOf(9662683));
        map.put("mediumseagreen", Integer.valueOf(3978097));
        map.put("mediumslateblue", Integer.valueOf(8087790));
        map.put("mediumspringgreen", Integer.valueOf(64154));
        map.put("mediumturquoise", Integer.valueOf(4772300));
        map.put("mediumvioletred", Integer.valueOf(13047173));
        map.put("midnightblue", Integer.valueOf(1644912));
        map.put("mintcream", Integer.valueOf(16121850));
        map.put("mistyrose", Integer.valueOf(16770273));
        map.put("moccasin", Integer.valueOf(16770229));
        map.put("navajowhite", Integer.valueOf(16768685));
        map.put("navy", Integer.valueOf(128));
        map.put("oldlace", Integer.valueOf(16643558));
        map.put("olive", Integer.valueOf(8421376));
        map.put("olivedrab", Integer.valueOf(7048739));
        map.put("orange", Integer.valueOf(16753920));
        map.put("orangered", Integer.valueOf(16729344));
        map.put("orchid", Integer.valueOf(14315734));
        map.put("palegoldenrod", Integer.valueOf(15657130));
        map.put("palegreen", Integer.valueOf(10025880));
        map.put("paleturquoise", Integer.valueOf(11529966));
        map.put("palevioletred", Integer.valueOf(14381203));
        map.put("papayawhip", Integer.valueOf(16773077));
        map.put("peachpuff", Integer.valueOf(16767673));
        map.put("peru", Integer.valueOf(13468991));
        map.put("pink", Integer.valueOf(16761035));
        map.put("plum", Integer.valueOf(14524637));
        map.put("powderblue", Integer.valueOf(11591910));
        map.put("purple", Integer.valueOf(8388736));
        map.put("red", Integer.valueOf(16711680));
        map.put("rosybrown", Integer.valueOf(12357519));
        map.put("royalblue", Integer.valueOf(4286945));
        map.put("saddlebrown", Integer.valueOf(9127187));
        map.put("salmon", Integer.valueOf(16416882));
        map.put("sandybrown", Integer.valueOf(16032864));
        map.put("seagreen", Integer.valueOf(3050327));
        map.put("seashell", Integer.valueOf(16774638));
        map.put("sienna", Integer.valueOf(10506797));
        map.put("silver", Integer.valueOf(12632256));
        map.put("skyblue", Integer.valueOf(8900331));
        map.put("slateblue", Integer.valueOf(6970061));
        map.put("slategray", Integer.valueOf(7372944));
        map.put("slategrey", Integer.valueOf(7372944));
        map.put("snow", Integer.valueOf(16775930));
        map.put("springgreen", Integer.valueOf(65407));
        map.put("steelblue", Integer.valueOf(4620980));
        map.put("tan", Integer.valueOf(13808780));
        map.put("teal", Integer.valueOf(32896));
        map.put("thistle", Integer.valueOf(14204888));
        map.put("tomato", Integer.valueOf(16737095));
        map.put("turquoise", Integer.valueOf(4251856));
        map.put("violet", Integer.valueOf(15631086));
        map.put("wheat", Integer.valueOf(16113331));
        map.put("white", Integer.valueOf(ViewCompat.MEASURED_SIZE_MASK));
        map.put("whitesmoke", Integer.valueOf(16119285));
        map.put("yellow", Integer.valueOf(16776960));
        map.put("yellowgreen", Integer.valueOf(10145074));
        return map;
    }

    public static Integer getHtmlColor(String color) {
        if (isNull(color)) {
            return null;
        }
        Integer i = (Integer) COLORS.get(color.toLowerCase());
        if (i != null) {
            return Integer.valueOf(i.intValue() | ViewCompat.MEASURED_STATE_MASK);
        }
        boolean hasAlpa = false;
        try {
            i = Integer.valueOf(color.indexOf("rgb("));
            if (i.intValue() == -1) {
                i = Integer.valueOf(color.indexOf("rgba("));
                hasAlpa = true;
            }
            if (i.intValue() == -1) {
                return Integer.valueOf(XmlUtils.convertValueToInt(convertSpecialColorFormat(color), -1) | ViewCompat.MEASURED_STATE_MASK);
            }
            int b;
            int i2 = color.indexOf(",", i.intValue());
            int r = string2Int(color.substring(i.intValue(), i2));
            i = Integer.valueOf(i2);
            i2 = color.indexOf(",", i2 + 1);
            int g = string2Int(color.substring(i.intValue(), i2));
            i = Integer.valueOf(i2);
            int a = 255;
            if (hasAlpa) {
                i2 = color.indexOf(",", i2 + 1);
                b = string2Int(color.substring(i.intValue(), i2));
                i = Integer.valueOf(i2);
                a = (int) (255.0f * string2Float(color.substring(i.intValue(), color.indexOf(")", i2 + 1))));
            } else {
                b = string2Int(color.substring(i.intValue(), color.indexOf(")", i2 + 1)));
            }
            return Integer.valueOf(Color.argb(a, r, g, b));
        } catch (Exception e) {
            return null;
        }
    }

    public static int getHtmlColorInt(String color) {
        Integer c = getHtmlColor(color);
        return c == null ? 0 : c.intValue();
    }

    private static String convertSpecialColorFormat(String color) {
        if (color.startsWith("#") && color.length() == 4) {
            return "#" + color.charAt(1) + color.charAt(1) + color.charAt(2) + color.charAt(2) + color.charAt(3) + color.charAt(3);
        }
        return color;
    }

    public static <T> T[] getSpans(Object[] objs, Class<T> kind) {
        if (objs == null) {
            return null;
        }
        int count = 0;
        for (Object obj : objs) {
            if (kind.isInstance(obj)) {
                count++;
            }
        }
        Object[] ret = (Object[]) ((Object[]) Array.newInstance(kind, count));
        if (count <= 0) {
            return (T[]) ret;
        }
        int t = 0;
        for (int i = 0; i < objs.length; i++) {
            if (kind.isInstance(objs[i])) {
                int t2 = t + 1;
                ret[t] = objs[i];
                t = t2;
            }
        }
        return (T[]) ret;
    }

    public static boolean spansHasKind(Object[] objs, Class kind) {
        if (objs == null) {
            return false;
        }
        for (Object obj : objs) {
            if (kind.isInstance(obj)) {
                return true;
            }
        }
        return false;
    }

    public static void makeLinkClickable(SpannableStringBuilder strBuilder, URLSpan span, OnUrlClick onUrlClick) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        MyClickableSpan clickable = new MyClickableSpan() {
            public void onClick(View view) {
                this.onUrlClick.onClick(this.url);
            }
        };
        clickable.url = span.getURL();
        clickable.onUrlClick = onUrlClick;
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    public static void setTextViewLinkClickable(TextView text, String html, OnUrlClick onUrlClick) {
        int i = 0;
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = (URLSpan[]) strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        int length = urls.length;
        while (i < length) {
            makeLinkClickable(strBuilder, urls[i], onUrlClick);
            i++;
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }
}