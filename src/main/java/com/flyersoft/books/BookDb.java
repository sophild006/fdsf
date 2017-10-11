package com.flyersoft.books;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.flyersoft.books.A.Bookmark;
import com.flyersoft.books.A.Bookmarks;
import com.flyersoft.moonreader.ActivityTxt;
import com.flyersoft.moonreader.BookMarkAct;
import com.flyersoft.moonreader.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class BookDb {
    private static final String DATABASE_CREATE_BOOKS = "create table books (_id integer primary key autoincrement, book text, filename text, lowerFilename text, author text, description text, category text, thumbFile text, coverFile text, addTime text, favorite text, downloadUrl text, rate text, bak1 text, bak2 text);";
    private static final String DATABASE_CREATE_NOTES = "create table notes (_id integer primary key autoincrement, book text, filename text, lowerFilename text, lastChapter NUMERIC, lastSplitIndex NUMERIC, lastPosition NUMERIC, highlightLength NUMERIC, highlightColor NUMERIC, time NUMERIC, bookmark text, note text, original text, underline NUMERIC, strikethrough NUMERIC, bak text);";
    private static final String DATABASE_CREATE_RANDOMCOVERS = "create table covers2 (_id integer primary key autoincrement, filename text, cover text);";
    private static final String DATABASE_CREATE_STATISTICS = "create table statistics (_id integer primary key autoincrement, filename text, usedTime NUMERIC, readWords NUMERIC, dates text);";
    private static final String DATABASE_TEMP_BOOKS = "create table tmpbooks (_id integer primary key autoincrement, book text, filename text, lowerFilename text, author text, description text, category text, thumbFile text, coverFile text, addTime text, favorite text, downloadUrl text, rate text, bak1 text, bak2 text);";
    public static final String DBNAME = "mrbooks.db";
    public static final String DEFAULT_FAV = "default_fav";
    public static final String MANUAL_SORT_TAG = "#@#";
    public static final int SHELF_TYPE_ALL = 0;
    public static final int SHELF_TYPE_AUTHORS = 2;
    public static final int SHELF_TYPE_CATEGORIES = 3;
    public static final int SHELF_TYPE_FAVORITES = 1;
    public static final int SHELF_TYPE_FOLDERS = 4;
    public static boolean hasNewDownload = false;
    private static boolean inTransition = false;
    public static boolean initFailed;
    private static SQLiteDatabase mDb;
    private static BookDBHelper mDbHelper;

    public static class BookCollection {
        public String fieldName;
        public String fieldValue;
        public boolean multiType;

        public BookCollection(String fieldName, String fieldValue, boolean multiType) {
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
            this.multiType = multiType;
        }
    }

    public static class BookDBHelper extends SQLiteOpenHelper {
        public BookDBHelper(Context context, String dbName) {
            super(context, dbName, null, 7);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(BookDb.DATABASE_CREATE_BOOKS);
            db.execSQL(BookDb.DATABASE_CREATE_NOTES);
            db.execSQL(BookDb.DATABASE_CREATE_STATISTICS);
            db.execSQL(BookDb.DATABASE_TEMP_BOOKS);
            db.execSQL(BookDb.DATABASE_CREATE_RANDOMCOVERS);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < 2) {
                db.execSQL(BookDb.DATABASE_CREATE_NOTES);
            }
            if (oldVersion < 4) {
                db.execSQL(BookDb.DATABASE_CREATE_STATISTICS);
            }
            if (oldVersion < 6) {
                db.execSQL(BookDb.DATABASE_CREATE_RANDOMCOVERS);
            }
            if (oldVersion < 7) {
                db.execSQL(BookDb.DATABASE_TEMP_BOOKS);
            }
        }
    }

    public static class BookInfo {
        public String addTime;
        public String author;
        public String bak1;
        public String bak2;
        public String book;
        public String book_arb;
        public String category;
        public String coverFile;
        public String description;
        public String downloadUrl;
        public String favorite;
        public String filename;
        public String rate;
        public int recentId;
        public String thumbFile;

        public BookInfo(String book, String filename, String author, String description, String category, String thumbFile, String coverFile, String addTime, String favorite, String downloadUrl, String rate, String bak1, String bak2, String book_arb) {
            this.book = book;
            this.filename = filename;
            this.author = author;
            this.category = category;
            this.thumbFile = thumbFile;
            this.coverFile = coverFile;
            this.addTime = addTime;
            this.favorite = favorite;
            this.downloadUrl = downloadUrl;
            this.rate = rate;
            this.bak1 = bak1;
            this.bak2 = bak2;
            this.book_arb = book_arb;
            try {
                if (description.length() > 2000) {
                    description = description.substring(0, 2000) + "...";
                }
                this.description = description;
            } catch (OutOfMemoryError e) {
                this.description = "";
            }
            this.recentId = -2;
        }
    }

    public static class NoteInfo {
        public String bak;
        public String book;
        public String bookmark;
        public boolean checkRealPosition;
        public String filename;
        public int highlightColor;
        public int highlightLength;
        public int id;
        public int lastChapter;
        public long lastPosition;
        public int lastSplitIndex;
        public String note;
        public String original;
        public boolean squiggly;
        public boolean strikethrough;
        public long time;
        public boolean underline;

        public NoteInfo(int id, String book, String filename, int lastChapter, int lastSplitIndex, long lastPosition, int highlightLength, int highlightColor, long time, String bookmark, String note, String original, boolean underline, boolean strikethrough, String bak) {
            this.id = id;
            this.book = book;
            this.filename = filename;
            this.lastChapter = lastChapter;
            this.lastSplitIndex = lastSplitIndex;
            this.lastPosition = lastPosition;
            this.highlightLength = highlightLength;
            this.highlightColor = highlightColor;
            this.time = time;
            this.bookmark = bookmark;
            this.note = note;
            this.original = original;
            this.underline = underline;
            this.strikethrough = strikethrough;
            this.squiggly = bak.startsWith("1");
            this.bak = bak;
        }
    }

    public static class PDF_META {
        public String author;
        public String category;
        public String subject;
        public String title;
    }

    public static class ReadStatistics {
        public String dates;
        public String filename;
        public long readWords;
        public long usedTime;

        public ReadStatistics(String filename, long usedTime, long readWords, String dates) {
            this.filename = filename;
            this.usedTime = usedTime;
            this.readWords = readWords;
            this.dates = dates;
        }
    }

    public static SQLiteDatabase getDb() {
        initFailed = true;
        if (mDb == null) {
            try {
                mDbHelper = new BookDBHelper(A.getContext(), DBNAME);
                mDb = mDbHelper.getWritableDatabase();
                deleteErrorDb();
            } catch (Exception e) {
                A.error(e);
                return null;
            }
        }
        initFailed = false;
        return mDb;
    }

    private static void deleteErrorDb() {
    }

    public static void beginTransition() {
        if (!inTransition) {
            try {
                inTransition = true;
                getDb().beginTransaction();
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    public static void endTransition() {
        if (inTransition) {
            try {
                inTransition = false;
                getDb().setTransactionSuccessful();
                getDb().endTransaction();
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    public static long insertRandomBookCover(String filename, String cover) {
        long u = 0;
        if (filename == null || cover == null) {
            return 0;
        }
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("filename", filename.toLowerCase());
            initialValues.put("cover", cover);
            u = (long) getDb().update("covers2", initialValues, "filename=?", new String[]{filename.toLowerCase()});
            if (u == 0) {
                u = getDb().insert("covers2", null, initialValues);
            }
        } catch (Exception e) {
            A.error(e);
        }
        return u;
    }

    public static String getSavedRandomBookCover(String filename) {
        return getSavedRandomBookCover(getDb(), filename);
    }

    public static String getSavedRandomBookCover(SQLiteDatabase db, String filename) {
        try {
            SQLiteDatabase sQLiteDatabase = db;
            Cursor cursor = sQLiteDatabase.query("covers2", null, "filename=?", new String[]{T.getFilename(filename).toLowerCase()}, null, null, null);
            if (cursor.moveToNext()) {
                String cover = cursor.getString(2);
                cursor.close();
                return cover;
            }
            cursor.close();
            return null;
        } catch (Exception e) {
            A.error(e);
        }
        return null;
    }

    public static ReadStatistics getSavedStatistics(String filename) {
        return getSavedStatistics(filename, true);
    }

    public static ReadStatistics getSavedStatistics(String filename, boolean createNewIfNone) {
        return getSavedStatistics(getDb(), filename, createNewIfNone);
    }

    public static ReadStatistics getSavedStatistics(SQLiteDatabase db, String filename, boolean createNewIfNone) {
        ReadStatistics readStatistics;
        if (db != null) {
            try {
                SQLiteDatabase sQLiteDatabase = db;
                Cursor cursor = sQLiteDatabase.query("statistics", null, "filename=?", new String[]{filename}, null, null, null);
                if (cursor.moveToNext()) {
                    ReadStatistics r = new ReadStatistics(cursor.getString(1), cursor.getLong(2), cursor.getLong(3), cursor.getString(4));
                    cursor.close();
                    return r;
                }
                cursor.close();
            } catch (Exception e) {
                A.error(e);
            }
        }
        if (createNewIfNone) {
            readStatistics = new ReadStatistics(filename, 0, 0, "");
        } else {
            readStatistics = null;
        }
        return readStatistics;
    }

    public static int deleteStatistics(String filename) {
        try {
            return getDb().delete("statistics", "filename=?", new String[]{filename});
        } catch (Exception e) {
            A.error(e);
            return -1;
        }
    }

    public static void deleteAllStatistics() {
        if (getDb() != null) {
            getDb().delete("statistics", null, null);
        }
    }

    public static long insertReadStatistics(ReadStatistics r) {
        long u = 0;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("filename", r.filename);
            initialValues.put("usedTime", Long.valueOf(r.usedTime));
            initialValues.put("readWords", Long.valueOf(r.readWords));
            initialValues.put("dates", r.dates);
            u = (long) getDb().update("statistics", initialValues, "filename=?", new String[]{r.filename});
            if (u == 0) {
                u = getDb().insert("statistics", null, initialValues);
            }
        } catch (Exception e) {
            A.error(e);
        }
        return u;
    }

    public static long insertBook(BookInfo book) {
        long u = 0;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("book", book.book);
            initialValues.put("filename", book.filename);
            initialValues.put("lowerFilename", book.filename.toLowerCase());
            initialValues.put("author", book.author);
            initialValues.put("description", book.description);
            initialValues.put("category", book.category);
            initialValues.put("thumbFile", book.thumbFile);
            initialValues.put("coverFile", book.coverFile);
            initialValues.put("addTime", book.addTime);
            initialValues.put("favorite", book.favorite);
            initialValues.put("downloadUrl", book.downloadUrl);
            initialValues.put("rate", book.rate);
            initialValues.put("bak1", book.bak1);
            initialValues.put("bak2", book.bak2);
            u = (long) getDb().update("books", initialValues, "lowerFilename=?", new String[]{book.filename.toLowerCase()});
            if (u == 0) {
                u = getDb().insert("books", null, initialValues);
            }
        } catch (Exception e) {
            A.error(e);
        }
        return u;
    }

    public static long insertBookToTemp(BookInfo book) {
        long u = 0;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put("book", book.book);
            initialValues.put("filename", book.filename);
            initialValues.put("lowerFilename", book.filename.toLowerCase());
            initialValues.put("author", book.author);
            initialValues.put("description", book.description);
            initialValues.put("category", book.category);
            initialValues.put("thumbFile", book.thumbFile);
            initialValues.put("coverFile", book.coverFile);
            initialValues.put("addTime", book.addTime);
            initialValues.put("favorite", book.favorite);
            initialValues.put("downloadUrl", book.downloadUrl);
            initialValues.put("rate", book.rate);
            initialValues.put("bak1", book.bak1);
            initialValues.put("bak2", book.bak2);
            u = (long) getDb().update("tmpbooks", initialValues, "lowerFilename=?", new String[]{book.filename.toLowerCase()});
            if (u == 0) {
                u = getDb().insert("tmpbooks", null, initialValues);
            }
        } catch (Exception e) {
            A.error(e);
        }
        return u;
    }

    public static boolean bookExists(String filename) {
        boolean result = true;
        Cursor cursor = getDb().query("books", null, "lowerFilename=?", new String[]{filename.toLowerCase()}, null, null, null);
        if (cursor.getCount() <= 0) {
            result = false;
        }
        cursor.close();
        return result;
    }

    public static boolean noteExists(String filename) {
        boolean result = true;
        Cursor cursor = getDb().query("notes", null, "lowerFilename=?", new String[]{filename.toLowerCase()}, null, null, null);
        if (cursor.getCount() <= 0) {
            result = false;
        }
        cursor.close();
        return result;
    }

    public static boolean statisticsExists(String filename) {
        boolean result = true;
        Cursor cursor = getDb().query("statistics", null, "filename=?", new String[]{filename}, null, null, null);
        if (cursor.getCount() <= 0) {
            result = false;
        }
        cursor.close();
        return result;
    }

    public static int deleteBook(String filename) {
        try {
            return getDb().delete("books", "lowerFilename=?", new String[]{filename.toLowerCase()});
        } catch (Exception e) {
            A.error(e);
            return -1;
        }
    }

    public static BookInfo getBookFromAll(String filename) {
        BookInfo bi = getBook(filename, -1);
        if (bi == null) {
            try {
                Cursor cursor = getDb().query("tmpbooks", null, "lowerFilename=?", new String[]{filename.toLowerCase()}, null, null, null);
                if (cursor.moveToNext()) {
                    bi = createBookItem(cursor);
                }
                cursor.close();
            } catch (Exception e) {
                A.error(e);
            }
        }
        return bi;
    }

    public static BookInfo getBook(String filename) {
        return getBook(filename, -1);
    }

    public static BookInfo getBook(String filename, long fileSize) {
        return getBook(getDb(), filename, fileSize);
    }

    public static BookInfo getBook(SQLiteDatabase db, String filename, long fileSize) {
        if (db != null) {
            try {
                SQLiteDatabase sQLiteDatabase = db;
                Cursor cursor = sQLiteDatabase.query("books", null, "lowerFilename=?", new String[]{filename.toLowerCase()}, null, null, null);
                while (cursor.moveToNext()) {
                    BookInfo bookItem = createBookItem(cursor);
                    if (fileSize != -1) {
                        if (new File(bookItem.filename).length() == fileSize) {
                        }
                    }
                    cursor.close();
                    return bookItem;
                }
                cursor.close();
            } catch (Exception e) {
                A.error(e);
            }
        }
        return null;
    }

    public static boolean hasSameBook(String bookName, String filename) {
        try {
            Cursor cursor = getDb().query("books", null, "book=?", new String[]{bookName}, null, null, null);
            ArrayList<String> removeFiles = new ArrayList();
            boolean same = false;
            while (cursor.moveToNext()) {
                File existFile = new File(cursor.getString(2));
                if (!existFile.isFile()) {
                    removeFiles.add(cursor.getString(2));
                } else if (existFile.length() == new File(filename).length()) {
                    same = true;
                }
            }
            cursor.close();
            if (removeFiles.size() <= 0) {
                return same;
            }
            Iterator it = removeFiles.iterator();
            while (it.hasNext()) {
                String removeFilename = (String) it.next();
                getDb().delete("books", "filename=?", new String[]{removeFilename});
            }
            return same;
        } catch (Exception e) {
            A.error(e);
            return false;
        }
    }

    public static void changeBookFile(String oldFile, String newFile) {
        try {
            ContentValues initialValues;
            if (bookExists(oldFile)) {
                initialValues = new ContentValues();
                initialValues.put("filename", newFile);
                initialValues.put("lowerFilename", newFile.toLowerCase());
                A.log("update1: " + getDb().update("books", initialValues, "lowerFilename=?", new String[]{oldFile.toLowerCase()}));
            }
            if (noteExists(oldFile)) {
                initialValues = new ContentValues();
                initialValues.put("filename", newFile);
                initialValues.put("lowerFilename", newFile.toLowerCase());
                if (getDb().update("notes", initialValues, "lowerFilename=?", new String[]{oldFile.toLowerCase()}) > 0) {
                    A.bookmarksList = null;
                }
            }
            if (statisticsExists(oldFile)) {
                initialValues = new ContentValues();
                initialValues.put("filename", newFile);
                A.log("update3: " + getDb().update("statistics", initialValues, "filename=?", new String[]{oldFile}));
            }
        } catch (Exception e) {
            A.error(e);
        }
    }

    public static int getBookCount() {
        try {
            Cursor query = getDb().query("books", null, null, null, null, null, null);
            int result = query.getCount();
            query.close();
            return result;
        } catch (Exception e) {
            return 0;
        }
    }

    public static ArrayList<BookInfo> getAllBooks() {
        return getBooks(null, null, null, false);
    }

    public static ArrayList<BookInfo> getBooks(String fieldName, String fieldValue, String searchKey, boolean multiType) {
        return getBooks(getDb(), fieldName, fieldValue, searchKey, multiType);
    }

    public static ArrayList<BookInfo> getBooks(SQLiteDatabase db, String fieldName, String fieldValue, String searchKey, boolean multiType) {
        ArrayList<BookInfo> books = new ArrayList();
        String fieldValue2 = fieldValue;
        if (!(fieldName == null || fieldValue == null || !fieldName.equals("favorite"))) {
            if (fieldValue.equals(A.getContext().getString(R.string.default_favorite_name))) {
                fieldValue = DEFAULT_FAV;
            }
        }
        if (db != null) {
            if (fieldValue != null) {
                try {
                    fieldValue = fieldValue.replace("'", "''");
                } catch (Exception e) {
                    A.error(e);
                }
            }
            String selection = null;
            if (searchKey == null) {
                if (fieldName != null) {
                    if (multiType) {
                        selection = fieldName + " like '%' || x'0A' || '%'";
                    } else {
                        selection = fieldName + "='" + fieldValue + "'";
                    }
                }
            } else if (fieldName == null) {
                selection = "((book like '%" + searchKey + "%') or (description like '%" + searchKey + "%')" + "or (author like '%" + searchKey + "%') or (category like '%" + searchKey + "%'))";
            } else {
                selection = "((" + fieldName + (multiType ? " like '%' || x'0A' || '%'" : "='" + fieldValue + "'") + ") and ((book like '%" + searchKey + "%') or (description like '%" + searchKey + "%')" + "or (author like '%" + searchKey + "%') or (category like '%" + searchKey + "%')))";
            }
            Cursor cursor = db.query("books", null, selection, null, null, null, getSortField());
            while (cursor.moveToNext()) {
                if (multiType) {
                    String s = cursor.getString(6);
                    if (s.startsWith(fieldValue2 + "\n") || s.indexOf("\n" + fieldValue2 + "\n") != -1) {
                        addBookToCollection(books, cursor);
                    }
                } else {
                    addBookToCollection(books, cursor);
                }
            }
            cursor.close();
            checkBooksAvailable(books);
        }
        return books;
    }

    private static String getSortField() {
        switch (A.shelf_sort_by) {
            case 1:
                return "author COLLATE NOCASE";
            case 2:
                return "addTime desc";
            case 3:
                return "filename COLLATE NOCASE";
            default:
                return "book COLLATE NOCASE";
        }
    }

    private static void addBookToCollection(ArrayList<BookInfo> books, Cursor cursor) {
        try {
            books.add(createBookItem(cursor));
        } catch (OutOfMemoryError e) {
            A.error(e);
        }
    }

    public static ArrayList<BookInfo> getDownloadBooks() {
        ArrayList<BookInfo> books = new ArrayList();
        try {
            Cursor cursor = getDb().query("books", null, "downloadUrl<>''", null, null, null, getSortField());
            while (cursor.moveToNext()) {
                addBookToCollection(books, cursor);
            }
            cursor.close();
            checkBooksAvailable(books);
        } catch (Exception e) {
            A.error(e);
        }
        return books;
    }

    public static ArrayList<BookInfo> getFolderBooks(String folder) {
        ArrayList<BookInfo> books = new ArrayList();
        folder = folder.toLowerCase();
        try {
            Cursor cursor = getDb().query("books", null, null, null, null, null, getSortField());
            while (cursor.moveToNext()) {
                if (T.getFilePath(cursor.getString(3)).equals(folder)) {
                    addBookToCollection(books, cursor);
                }
            }
            cursor.close();
            checkBooksAvailable(books);
        } catch (Exception e) {
            A.error(e);
        }
        return books;
    }

    public static ArrayList<BookCollection> getFolderValues() {
        ArrayList<BookCollection> collections = new ArrayList();
        if (getDb() != null) {
            try {
                ArrayList<String> paths = new ArrayList();
                Cursor cursor = getDb().query("books", null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    String filePath = T.getFilePath(cursor.getString(2));
                    if (paths.indexOf(filePath) == -1) {
                        paths.add(filePath);
                        collections.add(new BookCollection("filename", filePath, false));
                    }
                }
                cursor.close();
            } catch (Exception e) {
                A.error(e);
            }
        }
        return collections;
    }

    public static ArrayList<BookCollection> getDistinctValues(String fieldName, boolean multiType) {
        return getDistinctValues(getDb(), fieldName, multiType);
    }

    public static ArrayList<BookCollection> getDistinctValues(SQLiteDatabase db, String fieldName, boolean multiType) {
        ArrayList<BookCollection> collections = new ArrayList();
        if (db != null) {
            Cursor cursor = db.rawQuery("SELECT distinct " + fieldName + " FROM books order by " + fieldName, null);
            while (cursor.moveToNext()) {
                String type = cursor.getString(0);
                if (type != null && type.length() > 0) {
                    if (multiType) {
                        Iterator it = T.text2StringList(type).iterator();
                        while (it.hasNext()) {
                            String category = (String) it.next();
                            if (!category.startsWith("#") || !category.startsWith("#")) {
                                boolean exists = false;
                                Iterator it2 = collections.iterator();
                                while (it2.hasNext()) {
                                    if (((BookCollection) it2.next()).fieldValue.equals(category)) {
                                        exists = true;
                                        break;
                                    }
                                }
                                if (!exists && category.length() > 0) {
                                    collections.add(new BookCollection(fieldName, category, true));
                                }
                            }
                        }
                    } else if (fieldName.equals("favorite") && type.equals(DEFAULT_FAV)) {
                        collections.add(0, new BookCollection(fieldName, A.getContext().getString(R.string.default_favorite_name), false));
                    } else {
                        collections.add(new BookCollection(fieldName, type, false));
                    }
                }
            }
            cursor.close();
            if (fieldName.equals("favorite") && collections.size() == 1 && ((BookCollection) collections.get(0)).fieldValue.equals(A.getContext().getString(R.string.default_favorite_name))) {
                collections.clear();
            }
        }
        return collections;
    }

    public static String getFavName(String name) {
        return name.equals(DEFAULT_FAV) ? A.getContext().getString(R.string.default_favorite_name) : name;
    }

    private static BookInfo createBookItem(Cursor cursor) {
        return new BookInfo(removeSortTag(cursor.getString(1)), cursor.getString(2), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(1));
    }

    public static String removeSortTag(String s) {
        if (s.indexOf(MANUAL_SORT_TAG) > 0) {
            return s.substring(s.lastIndexOf(MANUAL_SORT_TAG) + MANUAL_SORT_TAG.length());
        }
        return s;
    }

    public static String getSortTag(String s) {
        if (s.indexOf(MANUAL_SORT_TAG) > 0) {
            return s.substring(0, s.lastIndexOf(MANUAL_SORT_TAG) + MANUAL_SORT_TAG.length());
        }
        return "";
    }

    private static void checkBooksAvailable(ArrayList<BookInfo> books) {
        if (T.isFolder(A.default_book_folder) && books.size() <= 200) {
            for (int i = books.size() - 1; i >= 0; i--) {
                String filename = ((BookInfo) books.get(i)).filename;
                if (filename.indexOf("?") != -1) {
                    filename = filename.substring(0, filename.indexOf("?"));
                }
                if (!T.isFile(filename)) {
                    deleteBook(filename);
                    books.remove(i);
                }
            }
        }
    }

    public static String encodeCollection(BookCollection bc) {
        return bc.fieldName + "\n##" + bc.fieldValue + "\n**" + (bc.multiType ? "1" : "0");
    }

    public static BookCollection decodeCollection(String s) {
        if (s != null && s.length() > 8) {
            try {
                return new BookCollection(s.substring(0, s.indexOf("\n##")), s.substring(s.indexOf("\n##") + 3, s.indexOf("\n**")), s.endsWith("\n**1"));
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static long insertNote(NoteInfo note) {
        int i = 1;
        ContentValues initialValues = new ContentValues();
        initialValues.put("book", note.book);
        initialValues.put("filename", note.filename);
        initialValues.put("lowerFilename", note.filename.toLowerCase());
        initialValues.put("lastChapter", Integer.valueOf(note.lastChapter));
        initialValues.put("lastSplitIndex", Integer.valueOf(note.lastSplitIndex));
        initialValues.put("lastPosition", Long.valueOf(note.lastPosition));
        initialValues.put("highlightLength", Integer.valueOf(note.highlightLength));
        initialValues.put("highlightColor", Integer.valueOf(note.highlightColor));
        initialValues.put("time", Long.valueOf(System.currentTimeMillis()));
        initialValues.put("bookmark", note.bookmark);
        initialValues.put("note", note.note);
        initialValues.put("original", note.original);
        initialValues.put("underline", Integer.valueOf(note.underline ? 1 : 0));
        String str = "strikethrough";
        if (!note.strikethrough) {
            i = 0;
        }
        initialValues.put(str, Integer.valueOf(i));
        initialValues.put("bak", note.squiggly ? "1" : "0");
        try {
            return getDb().insert("notes", null, initialValues);
        } catch (Exception e) {
            A.error(e);
            return 0;
        }
    }

    public static void getNotesHighlights(String filename, ArrayList<NoteInfo> notes, ArrayList<NoteInfo> highlights) {
        notes.clear();
        highlights.clear();
        ArrayList<Integer> duplicated = new ArrayList();
        try {
            NoteInfo item;
            boolean ok;
            int i;
            Iterator it;
            String orderBy = A.getBookType() == 7 ? "lastPosition, lastChapter" : "lastChapter, lastSplitIndex, lastPosition";
            Cursor cursor = getDb().query("notes", null, "lowerFilename=? and highlightLength>0", new String[]{filename.toLowerCase()}, null, null, orderBy);
            while (cursor.moveToNext()) {
                item = createNoteItem(cursor);
                ok = true;
                i = 0;
                while (i < highlights.size()) {
                    NoteInfo n = (NoteInfo) highlights.get(i);
                    if (n.lastChapter == item.lastChapter && n.lastSplitIndex == item.lastSplitIndex && n.lastPosition == item.lastPosition && n.highlightLength == item.highlightLength && n.highlightColor == item.highlightColor) {
                        if (n.note.length() > 0) {
                            ok = false;
                        } else if (!duplicated.contains(Integer.valueOf(i))) {
                            duplicated.add(Integer.valueOf(i));
                        }
                        if (ok) {
                            highlights.add(item);
                            if (!cursor.getString(11).equals("")) {
                                notes.add(item);
                            }
                        }
                    } else {
                        i++;
                    }
                }
                if (ok) {
                    highlights.add(item);
                    if (!cursor.getString(11).equals("")) {
                        notes.add(item);
                    }
                }
            }
            cursor.close();
            if (duplicated.size() > 0) {
                for (i = duplicated.size() - 1; i >= 0; i--) {
                    int j = ((Integer) duplicated.get(i)).intValue();
                    A.log("#" + j + ":" + highlights.remove(j));
                }
                deleteNotes(filename, false, false);
                it = highlights.iterator();
                while (it.hasNext()) {
                    insertNote((NoteInfo) it.next());
                }
            }
            if (highlights.size() == 0) {
                if (filename.equals(A.lastFile)) {
                    cursor = getDb().query("notes", null, "book=? and highlightLength>0", new String[]{A.getBookName()}, null, null, orderBy);
                    String existFilename = null;
                    File currentFile = new File(filename);
                    while (cursor.moveToNext()) {
                        ok = false;
                        if (existFilename == null) {
                            File existFile = new File(cursor.getString(2));
                            if (!existFile.isFile() || currentFile.length() == existFile.length()) {
                                existFilename = cursor.getString(2);
                                ok = true;
                            }
                        } else if (cursor.getString(2).equals(existFilename)) {
                            ok = true;
                        }
                        if (ok) {
                            item = createNoteItem(cursor);
                            highlights.add(item);
                            if (!cursor.getString(11).equals("")) {
                                notes.add(item);
                            }
                        }
                    }
                    cursor.close();
                    if (highlights.size() > 0) {
                        deleteNotes(existFilename, false, false);
                        it = highlights.iterator();
                        while (it.hasNext()) {
                            insertNote((NoteInfo) it.next());
                        }
                    }
                }
            }
        } catch (Exception e) {
            A.error(e);
        }
    }

    public static boolean removeNote(NoteInfo note) {
        try {
            if (getDb().delete("notes", "lowerFilename=? and lastChapter=? and lastSplitIndex=? and lastPosition=?", new String[]{note.filename.toLowerCase(), "" + note.lastChapter, "" + note.lastSplitIndex, "" + note.lastPosition}) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            A.error(e);
            return false;
        }
    }

    public static void deleteNotes(String filename, boolean onlyNotes, boolean onlyHighlights) {
        if (getDb() != null) {
            if (onlyNotes) {
                getDb().delete("notes", "lowerFilename=? and note<>'' and bookmark=''", new String[]{filename.toLowerCase()});
            } else if (onlyHighlights) {
                getDb().delete("notes", "lowerFilename=? and note='' and bookmark=''", new String[]{filename.toLowerCase()});
            } else {
                getDb().delete("notes", "lowerFilename=? and bookmark=''", new String[]{filename.toLowerCase()});
            }
        }
    }

    private static NoteInfo createNoteItem(Cursor cursor) {
        NoteInfo n = new NoteInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(4), cursor.getInt(5), cursor.getLong(6), cursor.getInt(7), cursor.getInt(8), cursor.getLong(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getInt(13) > 0, cursor.getInt(14) > 0, cursor.getString(15));
        if (n.original == null) {
            n.original = "";
        }
        if (n.bookmark == null) {
            n.bookmark = "";
        }
        if (n.note == null) {
            n.note = "";
        }
        if (n.bak == null) {
            n.bak = "";
        }
        return n;
    }

    public static boolean scanAcceptableFileSize(String filename) {
        long bigSize = 30;
        String ext = T.getFileExt(filename);
        long mem = Runtime.getRuntime().maxMemory() / A.BIG_FILESIZE;
        if (mem <= 60) {
            int i = mem > 30 ? 10 : mem > 20 ? 3 : 2;
            bigSize = (long) i;
        }
        bigSize *= A.BIG_FILESIZE;
        if (ext.equals(".fb2") && T.getFileSize(filename) > bigSize) {
            return false;
        }
        if (!ext.equals(".umd") || T.getFileSize(filename) <= bigSize) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.flyersoft.books.BookDb.BookInfo createBookInfoFromFile(java.lang.String r26, java.lang.String r27, boolean r28) {
        /*
        r2 = 0;
        r27 = com.flyersoft.components.BaseCompressor.getZRCacheFile(r27);
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r8 = "";
        r4 = r4.append(r8);
        r8 = new java.io.File;
        r0 = r27;
        r8.<init>(r0);
        r8 = r8.lastModified();
        r4 = r4.append(r8);
        r10 = r4.toString();
        r23 = com.flyersoft.books.A.isDownloadFile(r27);
        r21 = com.flyersoft.books.T.getFileExt(r27);
        r4 = com.flyersoft.books.A.getBookType(r27);
        r8 = 100;
        if (r4 != r8) goto L_0x00f9;
    L_0x0033:
        r4 = scanAcceptableFileSize(r27);
        if (r4 == 0) goto L_0x00f9;
    L_0x0039:
        r20 = 0;
        r4 = ".epub";
        r0 = r21;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x004f;
    L_0x0045:
        r20 = new com.flyersoft.books.Epub;
        r4 = 1;
        r0 = r20;
        r1 = r27;
        r0.<init>(r1, r4);
    L_0x004f:
        r4 = ".mobi";
        r0 = r21;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0067;
    L_0x0059:
        r20 = new com.flyersoft.books.Mobi;
        r4 = com.flyersoft.books.A.getContext();
        r8 = 1;
        r0 = r20;
        r1 = r27;
        r0.<init>(r4, r1, r8);
    L_0x0067:
        r4 = ".fb2";
        r0 = r21;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x007b;
    L_0x0071:
        r4 = ".fb2.zip";
        r0 = r27;
        r4 = r0.endsWith(r4);
        if (r4 == 0) goto L_0x0085;
    L_0x007b:
        r20 = new com.flyersoft.books.Fb2;
        r4 = 1;
        r0 = r20;
        r1 = r27;
        r0.<init>(r1, r4);
    L_0x0085:
        r4 = ".umd";
        r0 = r21;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0099;
    L_0x008f:
        r20 = new com.flyersoft.books.Umd;
        r4 = 1;
        r0 = r20;
        r1 = r27;
        r0.<init>(r1, r4);
    L_0x0099:
        r4 = ".chm";
        r0 = r21;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x00ad;
    L_0x00a3:
        r20 = new com.flyersoft.books.Chm;
        r4 = 1;
        r0 = r20;
        r1 = r27;
        r0.<init>(r1, r4);
    L_0x00ad:
        if (r20 == 0) goto L_0x00f3;
    L_0x00af:
        r4 = r20.isInited();
        if (r4 == 0) goto L_0x00f3;
    L_0x00b5:
        r2 = new com.flyersoft.books.BookDb$BookInfo;
        r3 = r20.getBookName();
        r5 = r20.getAuthor();
        r0 = r20;
        r4 = r0.description;
        r4 = com.flyersoft.books.A.trimHtml(r4);
        r6 = com.flyersoft.books.A.doTrimBlankSpace(r4);
        r0 = r20;
        r4 = r0.categories;
        r7 = com.flyersoft.books.T.stringList2Text(r4);
        r8 = com.flyersoft.books.A.getBookThumbFile(r27);
        r9 = com.flyersoft.books.A.getBookCoverFile(r27);
        if (r23 == 0) goto L_0x00f6;
    L_0x00dd:
        r12 = "UNKNOW";
    L_0x00df:
        r13 = "";
        r14 = "";
        r15 = "";
        r16 = "";
        r4 = r27;
        r11 = r26;
        r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16);
        if (r28 == 0) goto L_0x00f3;
    L_0x00f0:
        insertBook(r2);
    L_0x00f3:
        r17 = r2;
    L_0x00f5:
        return r17;
    L_0x00f6:
        r12 = "";
        goto L_0x00df;
    L_0x00f9:
        r4 = r21.length();
        if (r4 != 0) goto L_0x0102;
    L_0x00ff:
        r17 = r2;
        goto L_0x00f5;
    L_0x0102:
        r3 = com.flyersoft.books.T.getOnlyFilename(r27);
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r8 = "(";
        r4 = r4.append(r8);
        r8 = r21.toUpperCase();
        r9 = 1;
        r11 = r21.length();
        r8 = r8.substring(r9, r11);
        r4 = r4.append(r8);
        r8 = ")";
        r4 = r4.append(r8);
        r5 = r4.toString();
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4 = r4.append(r5);
        r8 = "\n";
        r4 = r4.append(r8);
        r7 = r4.toString();
        r6 = "";
        r4 = ".pdf";
        r0 = r21;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x01dd;
    L_0x014b:
        r7 = "PDF\n";
        r4 = com.flyersoft.moonreader.ActivityMain.selfPref;	 Catch:{ Exception -> 0x0218 }
        r8 = 0;
        r4 = com.radaee.pdf.Global.Init(r4, r8);	 Catch:{ Exception -> 0x0218 }
        if (r4 == 0) goto L_0x01dd;
    L_0x0156:
        r25 = new com.radaee.pdf.Document;	 Catch:{ Exception -> 0x0218 }
        r25.<init>();	 Catch:{ Exception -> 0x0218 }
        r4 = "";
        r0 = r25;
        r1 = r27;
        r4 = r0.Open(r1, r4);	 Catch:{ Exception -> 0x0218 }
        if (r4 != 0) goto L_0x01dd;
    L_0x0167:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0218 }
        r4.<init>();	 Catch:{ Exception -> 0x0218 }
        r8 = com.flyersoft.books.A.download_cache_path;	 Catch:{ Exception -> 0x0218 }
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0218 }
        r8 = "/";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0218 }
        r8 = com.flyersoft.books.T.getFilename(r27);	 Catch:{ Exception -> 0x0218 }
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0218 }
        r8 = "_2.png";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0218 }
        r22 = r4.toString();	 Catch:{ Exception -> 0x0218 }
        r4 = com.flyersoft.books.T.isFile(r22);	 Catch:{ Exception -> 0x0218 }
        if (r4 != 0) goto L_0x01a0;
    L_0x0190:
        r4 = 0;
        r8 = 0;
        r0 = r25;
        r18 = com.radaee.pdf.Global.createBitmapOfPage(r0, r4, r8);	 Catch:{ Exception -> 0x0218 }
        r4 = 0;
        r0 = r18;
        r1 = r27;
        com.flyersoft.books.A.generateBookCovers(r0, r1, r4);	 Catch:{ Exception -> 0x0218 }
    L_0x01a0:
        r24 = getPDF_META(r25);	 Catch:{ Exception -> 0x0218 }
        r0 = r24;
        r4 = r0.title;	 Catch:{ Exception -> 0x0218 }
        if (r4 == 0) goto L_0x01ae;
    L_0x01aa:
        r0 = r24;
        r3 = r0.title;	 Catch:{ Exception -> 0x0218 }
    L_0x01ae:
        r0 = r24;
        r4 = r0.author;	 Catch:{ Exception -> 0x0218 }
        if (r4 == 0) goto L_0x01b8;
    L_0x01b4:
        r0 = r24;
        r5 = r0.author;	 Catch:{ Exception -> 0x0218 }
    L_0x01b8:
        r0 = r24;
        r4 = r0.category;	 Catch:{ Exception -> 0x0218 }
        if (r4 == 0) goto L_0x01d3;
    L_0x01be:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0218 }
        r4.<init>();	 Catch:{ Exception -> 0x0218 }
        r4 = r4.append(r7);	 Catch:{ Exception -> 0x0218 }
        r0 = r24;
        r8 = r0.category;	 Catch:{ Exception -> 0x0218 }
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0218 }
        r7 = r4.toString();	 Catch:{ Exception -> 0x0218 }
    L_0x01d3:
        r0 = r24;
        r4 = r0.subject;	 Catch:{ Exception -> 0x0218 }
        if (r4 == 0) goto L_0x01dd;
    L_0x01d9:
        r0 = r24;
        r6 = r0.subject;	 Catch:{ Exception -> 0x0218 }
    L_0x01dd:
        r4 = ".cbz";
        r0 = r21;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x01f1;
    L_0x01e7:
        r4 = ".cbr";
        r0 = r21;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x01f8;
    L_0x01f1:
        r4 = 0;
        r8 = 0;
        r0 = r27;
        com.flyersoft.components.BaseCompressor.createCbzBookCover(r0, r4, r8);
    L_0x01f8:
        r2 = new com.flyersoft.books.BookDb$BookInfo;
        r8 = "";
        r9 = "";
        if (r23 == 0) goto L_0x021d;
    L_0x0200:
        r12 = "UNKNOW";
    L_0x0202:
        r13 = "";
        r14 = "";
        r15 = "";
        r16 = "";
        r4 = r27;
        r11 = r26;
        r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16);
        if (r28 == 0) goto L_0x00f3;
    L_0x0213:
        insertBook(r2);
        goto L_0x00f3;
    L_0x0218:
        r19 = move-exception;
        com.flyersoft.books.A.error(r19);
        goto L_0x01dd;
    L_0x021d:
        r12 = "";
        goto L_0x0202;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.books.BookDb.createBookInfoFromFile(java.lang.String, java.lang.String, boolean):com.flyersoft.books.BookDb$BookInfo");
    }

//    public static PDF_META getPDF_META(Document m_doc) {
//        PDF_META m = new PDF_META();
//        String s = m_doc.GetMeta("Title");
//        if (s.length() > 1) {
//            m.title = s;
//        }
//        s = m_doc.GetMeta("Author");
//        if (s.length() > 0) {
//            m.author = s;
//        }
//        s = m_doc.GetMeta("Subject");
//        if (s.length() > 0 && (m.title == null || !s.equals(m.title))) {
//            m.subject = s;
//        }
//        s = m_doc.GetMeta("Keywords");
//        if (s.length() > 0) {
//            StringBuilder sb = new StringBuilder();
//            while (s.indexOf(", ") != -1) {
//                int i = s.indexOf(", ");
//                sb.append(s.substring(0, i) + "\n");
//                s = s.substring(i + 2);
//            }
//            if (s.length() > 0) {
//                sb.append(s + "\n");
//            }
//            m.category = sb.toString();
//        }
//        return m;
//    }

    public static ArrayList<Bookmarks> getBookmarks() {
        ArrayList<Bookmarks> all = new ArrayList();
        try {
            Cursor cursor = getDb().query("notes", null, "bookmark <> ''", null, null, null, "lastChapter, lastSplitIndex, lastPosition");
            while (cursor.moveToNext()) {
                String filename = cursor.getString(2);
                Bookmark bm = new Bookmark(filename, cursor.getString(10), cursor.getInt(4), cursor.getInt(5), (long) cursor.getInt(6), cursor.getLong(9), cursor.getInt(7));
                int id = getBookmarksId(all, filename);
                Bookmarks bms = id != -1 ? (Bookmarks) all.get(id) : new Bookmarks(filename);
                bms.list.add(bm);
                if (id == -1) {
                    all.add(bms);
                }
            }
            cursor.close();
        } catch (Exception e) {
            A.error(e);
        }
        return all;
    }

    private static int getBookmarksId(ArrayList<Bookmarks> all, String filename) {
        for (int i = 0; i < all.size(); i++) {
            if (((Bookmarks) all.get(i)).filename.equals(filename)) {
                return i;
            }
        }
        return -1;
    }

    public static void clearBookmarks(String filename) {
        try {
            getDb().delete("notes", "lowerFilename=? and bookmark<>''", new String[]{filename.toLowerCase()});
        } catch (Exception e) {
            A.error(e);
        }
    }

    public static boolean removeBookmark(Bookmark bm) {
        try {
            if (getDb().delete("notes", "lowerFilename=? and lastChapter=? and lastSplitIndex=? and lastPosition=? and bookmark<>''", new String[]{bm.filename.toLowerCase(), "" + bm.chapter, "" + bm.splitIndex, "" + bm.position}) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            A.error(e);
            return false;
        }
    }

    public static void addBookmark(Bookmark bm) {
        insertNote(new NoteInfo(0, T.getFilename(bm.filename), bm.filename, bm.chapter, bm.splitIndex, bm.position, bm.color, A.highlight_color1, bm.time, bm.name, "", "", false, false, ""));
    }

    public static String exportNotesToText(String bookFile, ArrayList<BookMarkAct.AnnotItem> annots) {
        boolean isPdf = bookFile.toLowerCase().endsWith(".pdf");
        StringBuilder sb = new StringBuilder();
        try {
            Cursor c = getDb().query("notes", null, "lowerFilename=?", new String[]{bookFile.toLowerCase()}, null, null, null);
            if (c.getCount() > 0) {
                if (!isPdf) {
                    sb.append(new File(bookFile).length() + "\n");
                    sb.append("indent:" + A.indentParagraph + "\n");
                    sb.append("trim:" + A.trimBlankSpace + "\n");
                    while (c.moveToNext()) {
                        if (annots == null || noteInAnnots(createNoteItem(c), annots)) {
                            sb.append("#\n");
                            for (int i = 0; i < c.getColumnCount(); i++) {
                                sb.append(c.getString(i).replace("\n", "<BR>") + "\n");
                            }
                        }
                    }
                } else if (!(T.isNull(ActivityTxt.selfPref) || ActivityTxt.selfPref.pdf == null)) {
                    ActivityTxt.selfPref.pdf.remoteNotes = null;
                    while (c.moveToNext()) {
                        NoteInfo n = createNoteItem(c);
                        if (annots == null || noteInAnnots(n, annots)) {
                            int type = n.underline ? 1 : n.strikethrough ? 2 : n.squiggly ? 3 : 0;
                            ActivityTxt.selfPref.pdf.addNewRemoteNote(n.lastPosition, n.time, n.lastChapter, n.lastSplitIndex, n.highlightColor, type, n.note, n.original);
                        }
                    }
                }
            }
            c.close();
        } catch (OutOfMemoryError e) {
            A.error(e);
        } catch (Exception e2) {
            A.error(e2);
        }
        if (!isPdf || T.isNull(ActivityTxt.selfPref) || ActivityTxt.selfPref.pdf == null) {
            return sb.toString();
        }
        return ActivityTxt.selfPref.pdf.createTextFromNotes();
    }

    private static boolean noteInAnnots(NoteInfo n, ArrayList<AnnotItem> annots) {
        Iterator it = annots.iterator();
        while (it.hasNext()) {
            AnnotItem a = (AnnotItem) it.next();
            if (n.lastChapter == a.chapter && n.lastSplitIndex == a.splitIndex && n.lastPosition == a.position) {
                if ((n.bookmark.length() > 0 && a.note == null) || (a.note != null && n.note.equals(a.note.note))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String exportNotesToText(ArrayList<NoteInfo> deletedNotes) {
        if (deletedNotes == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            Iterator it = deletedNotes.iterator();
            while (it.hasNext()) {
                NoteInfo n = (NoteInfo) it.next();
                sb.append("#\n");
                int i = 0;
                while (i < 16) {
                    StringBuilder stringBuilder = new StringBuilder();
                    r4 = i == 9 ? n.time + "" : i == 10 ? "" : i == 15 ? "*DELETED*" : "0";
                    sb.append(stringBuilder.append(r4).append("\n").toString());
                    i++;
                }
            }
        } catch (Throwable e) {
            A.error(e);
        }
        return sb.toString();
    }

    public static boolean exportNotesToFile(String bookFile, String filename, ArrayList<BookMarkAct.AnnotItem> annots) {
        String text = exportNotesToText(bookFile, annots);
        if (text.length() <= 0) {
            return false;
        }
        T.saveFileText(filename, text);
        return true;
    }

    private static String alGet(ArrayList<String> al, int index) {
        return ((String) al.get(index)).replace("<BR>", "\n");
    }

    public static int importNotesFromFile(String bookFile, String filename) {
        return importNotesFromText(bookFile, T.getFileText(filename));
    }

    public static int importNotesFromText(String bookFile, String text) {
        try {
            if (!bookFile.toLowerCase().endsWith(".pdf")) {
                ArrayList<String> al = T.inputStream2StringList(T.String2InputStream(text));
                if (Math.abs(new File(bookFile).length() - ((long) Integer.valueOf(alGet(al, 0)).intValue())) > 5000) {
                    return -2;
                }
                boolean imported = false;
                int i = 1;
                while (i < al.size()) {
                    if (alGet(al, i).equals("#") && al.size() - i > 15) {
                        Cursor cursor;
                        NoteInfo n1 = new NoteInfo(0, T.getFilename(bookFile), bookFile, Integer.valueOf(alGet(al, i + 5)).intValue(), Integer.valueOf(alGet(al, i + 6)).intValue(), Long.valueOf(alGet(al, i + 7)).longValue(), Integer.valueOf(alGet(al, i + 8)).intValue(), Integer.valueOf(alGet(al, i + 9)).intValue(), Long.valueOf(alGet(al, i + 10)).longValue(), alGet(al, i + 11), alGet(al, i + 12), alGet(al, i + 13), Integer.valueOf(alGet(al, i + 14)).intValue() > 0, Integer.valueOf(alGet(al, i + 15)).intValue() > 0, alGet(al, i + 16));
                        boolean deleted = alGet(al, i + 16).equals("*DELETED*");
                        long timeTag = Long.valueOf(alGet(al, i + 10)).longValue();
                        String bookmarkTag = deleted ? "" : alGet(al, i + 11);
                        if (bookmarkTag.equals("")) {
                            cursor = getDb().query("notes", null, "lowerFilename=? and time=" + timeTag, new String[]{bookFile.toLowerCase()}, null, null, null);
                        } else {
                            cursor = getDb().query("notes", null, "lowerFilename=? and bookmark=?", new String[]{bookFile.toLowerCase(), bookmarkTag}, null, null, null);
                        }
                        boolean exists = cursor.getCount() > 0;
                        if (exists) {
                            cursor.moveToNext();
                            NoteInfo n2 = createNoteItem(cursor);
                            if (deleted || differentNote(n1, n2)) {
                                exists = false;
                                if (deleted) {
                                    imported = true;
                                }
                                if (bookmarkTag.equals("")) {
                                    getDb().delete("notes", "lowerFilename=? and time=" + timeTag, new String[]{bookFile.toLowerCase()});
                                } else {
                                    getDb().delete("notes", "lowerFilename=? and bookmark=?", new String[]{bookFile.toLowerCase(), bookmarkTag});
                                }
                            }
                        }
                        cursor.close();
                        if (!(deleted || exists)) {
                            insertNote(n1);
                            imported = true;
                        }
                        i += 16;
                    }
                    i++;
                }
                if (imported) {
                    return 1;
                }
                return -1;
            } else if (T.isNull(ActivityTxt.selfPref) || ActivityTxt.selfPref.pdf == null || !ActivityTxt.selfPref.pdf.createNotesFromCloud(text)) {
                return -1;
            } else {
                return 1;
            }
        } catch (Exception e) {
            A.error(e);
        }
    }

    private static boolean differentNote(NoteInfo n1, NoteInfo n2) {
        return (n1.bookmark.equals(n2.bookmark) && n1.note.equals(n2.note) && n1.highlightColor == n2.highlightColor && n1.squiggly == n2.squiggly && n1.strikethrough == n2.strikethrough && n1.underline == n2.underline && n1.lastPosition == n2.lastPosition && n1.highlightLength == n2.highlightLength) ? false : true;
    }

    public static void emptyDatabase() {
        if (getDb() != null) {
            getDb().delete("books", null, null);
        }
        Iterator it = T.getFolderFileList(A.download_cache_path, false, true, true, false).iterator();
        while (it.hasNext()) {
            String filename = (String) it.next();
            if (filename.toLowerCase().endsWith(A.NETCOVER_TAG) || filename.toLowerCase().endsWith(".jpg")) {
                if (ActivityMain.selfPref != null) {
                    ActivityMain.selfPref.handler.sendMessage(ActivityMain.selfPref.handler.obtainMessage(ActivityMain.EMPTY_SHELF_START, filename));
                }
                T.deleteFile(filename);
            }
        }
    }

    public static int getNotesCount() {
        return getDb().query("notes", null, null, null, null, null, null).getCount();
    }

    public static String createSerialText(BookInfo bi) {
        if (bi == null) {
            return null;
        }
        return createSerialText(bi.category);
    }

    public static String createSerialText(String category) {
        int x2 = category.indexOf(">\n");
        if (x2 != -1) {
            int x1 = category.lastIndexOf("<", x2);
            if (x1 != -1 && x2 - x1 > 1) {
                String serial = category.substring(x1 + 1, x2);
                float index = PrefEditBook.getSeriesIndex(category);
                String tag = index != -1.0f ? " (" + index + ")" : "";
                if (tag.endsWith(".0)")) {
                    tag = tag.substring(0, tag.length() - 3) + ")";
                }
                return serial + tag;
            }
        }
        return null;
    }
}