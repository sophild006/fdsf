package com.flyersoft.components;

import android.content.Context;
import android.content.SharedPreferences;
import com.flyersoft.books.A;
import com.flyersoft.books.T;
import com.flyersoft.moonreader.ActivityTxt;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class DropboxSync {
    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";
    private static final AccessType ACCESS_TYPE = AccessType.DROPBOX;
    public static final String ACCOUNT_PREFS_NAME = "dropbox";
    public static DropboxSync selfPref;
    public boolean isLoggined = ((AndroidAuthSession) this.mApi.getSession()).isLinked();
    public DropboxAPI<AndroidAuthSession> mApi = new DropboxAPI(buildSession());
    private Context mContext;
    SharedPreferences sharedPref;
    public boolean working = false;

    public interface OnDropboxUploaded {
        void afterUpload(String str, String str2);
    }

    public interface OnDropBoxDownloaded {
        void afterDownload(ByteArrayOutputStream byteArrayOutputStream, String str, String str2);
    }

    public DropboxSync(Context context) {
        this.mContext = context;
        selfPref = this;
        checkDropboxAuth();
    }

    public boolean checkDropboxAuth() {
        if (!((AndroidAuthSession) this.mApi.getSession()).authenticationSuccessful()) {
            return false;
        }
        try {
            A.log("*dropbox auth successfull");
            ((AndroidAuthSession) this.mApi.getSession()).finishAuthentication();
            TokenPair tokens = ((AndroidAuthSession) this.mApi.getSession()).getAccessTokenPair();
            storeKeys(tokens.key, tokens.secret);
            this.isLoggined = true;
        } catch (Exception e) {
            A.error(e);
        }
        return true;
    }

    public void showLogin(Context context) {
        try {
            ((AndroidAuthSession) this.mApi.getSession()).startAuthentication(context);
        } catch (Exception e) {
            A.error(e);
        }
    }

    public boolean uploadFile(String dropboxFilename, InputStream is, OnDropboxUploaded onUploaded, ProgressListener onProgress, boolean ignoreWorking) {
        if (!ignoreWorking && this.working) {
            return false;
        }
        final String str = dropboxFilename;
        final InputStream inputStream = is;
        final ProgressListener progressListener = onProgress;
        final OnDropboxUploaded onDropboxUploaded = onUploaded;
        new Thread() {
            public void run() {
                DropboxSync.this.working = true;
                try {
                    A.log(">>upload:" + str);
                    Entry entry = DropboxSync.this.mApi.putFileOverwrite(str, inputStream, (long) inputStream.available(), progressListener);
                    DropboxSync.this.saveEntryRev(str, entry.rev);
                    DropboxSync.this.working = false;
                    if (onDropboxUploaded != null) {
                        onDropboxUploaded.afterUpload(entry.rev, null);
                    }
                } catch (Throwable e) {
                    DropboxSync.this.working = false;
                    if (onDropboxUploaded != null) {
                        onDropboxUploaded.afterUpload(null, A.errorMsg(e));
                    }
                }
                DropboxSync.this.working = false;
            }
        }.start();
        return true;
    }

    public boolean downloadFile(final String dropboxFilename, final OnDropBoxDownloaded onDownloaded, final ProgressListener onProgress) {
        if (this.working) {
            return false;
        }
        new Thread() {
            public void run() {
                DropboxSync.this.working = true;
                try {
                    A.log(">>chek if download:" + dropboxFilename);
                    Entry entry = DropboxSync.this.mApi.metadata(dropboxFilename, 1, null, false, null);
                    ByteArrayOutputStream os = null;
                    if (!entry.rev.equals(DropboxSync.this.getEntryRev(dropboxFilename))) {
                        A.log(">>confirm download:" + dropboxFilename);
                        DropboxSync.this.saveEntryRev(dropboxFilename, entry.rev);
                        os = new ByteArrayOutputStream();
                        DropboxSync.this.mApi.getFile(dropboxFilename, null, os, onProgress);
                    }
                    DropboxSync.this.working = false;
                    if (onDownloaded != null) {
                        onDownloaded.afterDownload(os, entry.rev, null);
                    }
                } catch (Throwable e) {
                    DropboxSync.this.working = false;
                    if (e.toString().indexOf(" 403 ") != -1) {
                        new File(A.xml_files_folder + "/" + DropboxSync.ACCOUNT_PREFS_NAME + ".xml").delete();
                        if (!T.isNull(ActivityTxt.selfPref)) {
                            ActivityTxt.selfPref.handler.sendEmptyMessage(ActivityTxt.DROPBOX_AUTH_AGAIN);
                        }
                    }
                    if (onDownloaded != null) {
                        if (DropboxSync.isFolderNotExistError(e)) {
                            onDownloaded.afterDownload(null, null, null);
                        } else {
                            onDownloaded.afterDownload(null, null, A.errorMsg(e));
                        }
                    }
                }
                DropboxSync.this.working = false;
            }
        }.start();
        return true;
    }

    public static boolean isFolderNotExistError(Throwable e) {
        String err = A.errorMsg(e);
        return (err == null || err.indexOf("404") == -1) ? false : true;
    }

    private SharedPreferences getSharedPref() {
        if (this.sharedPref == null) {
            this.sharedPref = this.mContext.getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        }
        return this.sharedPref;
    }

    protected void saveEntryRev(String filename, String rev) {
        getSharedPref().edit().putString(filename, rev).commit();
    }

    protected String getEntryRev(String filename) {
        return getSharedPref().getString(filename, null);
    }

    private String[] getKeys() {
        String key = getSharedPref().getString(ACCESS_KEY_NAME, null);
        String secret = getSharedPref().getString("ACCESS_SECRET", null);
        if (key == null || secret == null) {
            return null;
        }
        return new String[]{key, secret};
    }

    private void storeKeys(String key, String secret) {
        getSharedPref().edit().putString(ACCESS_KEY_NAME, key).putString("ACCESS_SECRET", secret).commit();
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(MrAd.APP_KEY, MrAd.APP_SECRET);
        String[] stored = getKeys();
        if (stored == null) {
            return new AndroidAuthSession(appKeyPair, ACCESS_TYPE);
        }
        return new AndroidAuthSession(appKeyPair, ACCESS_TYPE, new AccessTokenPair(stored[0], stored[1]));
    }
}