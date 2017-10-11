package com.flyersoft.components;

import android.content.UriPermission;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.v4.provider.DocumentFile;
import com.flyersoft.books.A;
import com.flyersoft.books.T;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;

public class SD {
    public static HashSet<String> ExtSDs;
    private static String tree_string;

    public static boolean isSDFile(String filename) {
        Iterator it = getSDs().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            int i = filename.indexOf("/" + name + "/");
            if (i != -1) {
                String ext = filename.substring(0, i) + "/" + name;
                if (T.isFolder(ext + "/Android") || T.isFolder(ext + "/LOST.DIR")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getSDRelativePath(String filename) {
        Iterator it = getSDs().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            int i = filename.indexOf("/" + name + "/");
            if (i != -1) {
                String ext = filename.substring(0, i) + "/" + name;
                if (T.isFolder(ext + "/Android") || T.isFolder(ext + "/LOST.DIR")) {
                    return filename.substring(ext.length() + 1);
                }
            }
        }
        return null;
    }

    public static boolean isSDPath(String path) {
        Iterator it = getSDs().iterator();
        while (it.hasNext()) {
            if (path.endsWith("/" + ((String) it.next())) && (T.isFolder(path + "/Android") || T.isFolder(path + "/LOST.DIR"))) {
                return true;
            }
        }
        return false;
    }

    public static String getSDName() {
        Iterator it = getSDs().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            if (T.isFolder("/storage/" + name + "/Android") || T.isFolder("/storage/" + name + "/LOST.DIR") || T.isFolder("/mnt/" + name + "/Android")) {
                return name;
            }
            if (T.isFolder("/mnt/" + name + "/LOST.DIR")) {
                return name;
            }
        }
        return null;
    }

    public static String getSDPath() {
        Iterator it = getSDs().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            if (T.isFolder("/storage/" + name + "/Android") || T.isFolder("/storage/" + name + "/LOST.DIR")) {
                return "/storage/" + name;
            }
            if (!T.isFolder("/mnt/" + name + "/Android")) {
                if (T.isFolder("/mnt/" + name + "/LOST.DIR")) {
                }
            }
            return "/mnt/" + name;
        }
        return null;
    }

    public static HashSet<String> getSDs() {
        if (ExtSDs == null) {
            ExtSDs = new HashSet();
            if (VERSION.SDK_INT >= 21) {
                StringBuilder sb = new StringBuilder();
                try {
                    Process process = new ProcessBuilder(new String[0]).command(new String[]{"mount"}).redirectErrorStream(true).start();
                    process.waitFor();
                    InputStream is = process.getInputStream();
                    byte[] buffer = new byte[1024];
                    while (is.read(buffer) != -1) {
                        sb.append(new String(buffer));
                    }
                    is.close();
                } catch (Exception e) {
                    A.error(e);
                }
                for (String line : sb.toString().split("\n")) {
                    if (line.contains("/media_rw/")) {
                        for (String part : line.split(" ")) {
                            if (part.startsWith("/") && part.contains("/media_rw/")) {
                                String name = T.getOnlyFilename(part);
                                if (!ExtSDs.contains(name)) {
                                    ExtSDs.add(name);
                                }
                            }
                        }
                    }
                }
            }
        }
        return ExtSDs;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Uri getTreeUri() {
        if (tree_string == null) {
            tree_string = A.getContext().getSharedPreferences("extsd", 0).getString("TreeUri", null);
            if (!(tree_string == null || checkStoragePermissions(Uri.parse(tree_string)))) {
                tree_string = null;
            }
        }
        if (tree_string == null) {
            return null;
        }
        return Uri.parse(tree_string);
    }

    public static void saveTreeUri(Uri uri) {
        A.getContext().getSharedPreferences("extsd", 0).edit().putString("TreeUri", uri.toString()).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean checkStoragePermissions(Uri myUri) {
        for (UriPermission up : A.getContext().getContentResolver().getPersistedUriPermissions()) {
            if (up.getUri().equals(myUri) && up.isWritePermission()) {
                A.log(myUri + ", write:" + up.isWritePermission() + ", read:" + up.isReadPermission());
                return true;
            }
        }
        return false;
    }

    public static DocumentFile getDocumentFile(String filename) {
        return getDocumentFile(filename, false, true);
    }

    public static DocumentFile getDocumentFile(String filename, boolean isDirectory, boolean createDirectories) {
        Uri treeUri = getTreeUri();
        if (treeUri == null) {
            return null;
        }
        String relativePath = getSDRelativePath(filename);
        if (relativePath == null) {
            return null;
        }
        DocumentFile document = DocumentFile.fromTreeUri(A.getContext(), treeUri);
        if (document == null) {
            return null;
        }
        String[] parts = relativePath.split("/");
        for (int i = 0; i < parts.length; i++) {
            DocumentFile nextDocument = document.findFile(parts[i]);
            if (nextDocument == null) {
                if (i < parts.length - 1) {
                    if (!createDirectories) {
                        return null;
                    }
                    nextDocument = document.createDirectory(parts[i]);
                } else if (isDirectory) {
                    nextDocument = document.createDirectory(parts[i]);
                } else {
                    nextDocument = document.createFile("*/*", parts[i]);
                }
            }
            document = nextDocument;
        }
        return document;
    }

    public static OutputStream getDocumentFileOutputStream(String filename) {
        return getDocumentFileOutputStream(filename, false);
    }

    public static OutputStream getDocumentFileOutputStream(String filename, boolean append) {
        DocumentFile df = getDocumentFile(filename);
        if (df != null) {
            try {
                return A.getContext().getContentResolver().openOutputStream(df.getUri(), append ? "wa" : "w");
            } catch (Exception e) {
                A.error(e);
            }
        }
        return null;
    }
}