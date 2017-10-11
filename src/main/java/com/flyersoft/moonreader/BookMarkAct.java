package com.flyersoft.moonreader;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.flyersoft.books.A;
import com.flyersoft.books.A.Bookmark;
import com.flyersoft.books.A.Bookmarks;
import com.flyersoft.books.BaseEBook;
import com.flyersoft.books.BookDb;
import com.flyersoft.books.BookDb.NoteInfo;
import com.flyersoft.books.T;
import com.flyersoft.staticlayout.BookmarkItem;
import com.flyersoft.staticlayout.ClearableEditText;
import com.flyersoft.staticlayout.PicGalleryShow;
import com.flyersoft.staticlayout.PrefEditBookmark;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import static android.widget.LinearLayout.VERTICAL;

public class BookMarkAct extends AppCompatActivity implements OnClickListener, OnLongClickListener {
    public static BookMarkAct selfPref;
    Button addButton;
    int annotFilter = 0;
    String annotKey;
    ArrayList<AnnotItem> annots;
    int anoteType = 0;
    Bookmarks bms;
    String bookmark;
    View bookmarkB;
    ListView bookmarkList;
    View bookmarkOptionB;
    int chapter;
    View colorB;
    View exportB;
    boolean exportClicked = false;
    int filterColor = 0;
    String[] filterColors;
    LinearLayout filterLay;
    String fullPathName;
    View highlightB;
    View noteB;
    View phDelete;
    View phExit;
    TextView phTitle;
    long position;
    String progress = "";
    View searchB;
    int splitIndex;

    public static class AnnotItem {
        public Bookmark bookmarkItem;
        public int chapter;
        public boolean checked = true;
        public String filename;
        public String name;
        public NoteInfo note;
        public int number;
        public long position;
        public int resId;
        public int splitIndex;
        public long time;

        public AnnotItem(String filename, int chapter, int splitIndex, long position, String name, int resId, Bookmark bookmarkItem, NoteInfo note, long time, int number) {
            this.filename = filename;
            this.chapter = chapter;
            this.splitIndex = splitIndex;
            this.position = position;
            this.name = name;
            this.resId = resId;
            this.bookmarkItem = bookmarkItem;
            this.note = note;
            this.time = time;
            this.number = number;
        }
    }

    public class AnnotsAdapter extends BaseAdapter {
        boolean[] checked;
        int column = 2;
        OnClickListener onBookClick = new OnClickListener() {
            public void onClick(View v) {
                int position = ((Integer) v.getTag()).intValue();
                if (position >= BookMarkAct.this.annots.size()) {
                    return;
                }
                if (AnnotsAdapter.this.getSelectedCount() > 0) {
                    AnnotsAdapter.this.inverseChecked(position);
                } else {
                    BookMarkAct.this.doNoteClick((AnnotItem) BookMarkAct.this.annots.get(position));
                }
            }
        };
        OnLongClickListener onBookLongClick = new OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (AnnotsAdapter.this.checked.length != BookMarkAct.this.annots.size()) {
                    AnnotsAdapter.this.checked = new boolean[BookMarkAct.this.annots.size()];
                }
                if (AnnotsAdapter.this.getSelectedCount() > 0) {
                    return false;
                }
                AnnotsAdapter.this.setChecked(((Integer) v.getTag()).intValue());
                return true;
            }
        };
        OnClickListener onOverflowClick = new OnClickListener() {
            public void onClick(View v) {
                final int position = ((Integer) v.getTag()).intValue();
                final AnnotItem n = (AnnotItem) BookMarkAct.this.annots.get(position);
                final String[] items = new String[(n.resId == R.drawable.bmnote ? 4 : 3)];
                if (n.resId == R.drawable.bmnote) {
                    items[0] = BookMarkAct.this.getString(R.string.share);
                    items[1] = A.getStringArrayItem(R.array.catalog_popup_menu, 1);
                    items[2] = A.getStringArrayItem(R.array.one_file_bookmark, 1);
                } else if (n.resId == R.drawable.bmhighlight) {
                    items[0] = BookMarkAct.this.getString(R.string.share);
                    items[1] = A.getStringArrayItem(R.array.one_file_bookmark, 1);
                } else {
                    items[0] = A.getStringArrayItem(R.array.catalog_popup_menu, 1);
                    items[1] = A.getStringArrayItem(R.array.one_file_bookmark, 1);
                }
                items[items.length - 1] = A.getStringArrayItem(R.array.one_file_bookmark, 2);

            }
        };

        public int getSelectedCount() {
            int count = 0;
            for (boolean z : this.checked) {
                if (z) {
                    count++;
                }
            }
            return count;
        }

        public void clearChecked() {
            this.checked = new boolean[BookMarkAct.this.annots.size()];
            BookMarkAct.this.phDelete.setVisibility(View.GONE);
            BookMarkAct.this.phTitle.setText(R.string.bookmark);
            super.notifyDataSetChanged();
        }

        public void inverseChecked(int i) {
            if (i < this.checked.length) {
                this.checked[i] = !this.checked[i];
                BookMarkAct.this.phTitle.setText(BookMarkAct.this.getString(R.string.bookmark) + " (" + getSelectedCount() + ")");
                if (getSelectedCount() == 0) {
                    clearChecked();
                }
                super.notifyDataSetChanged();
            }
        }

        public void setChecked(int i) {
            if (i <= this.checked.length - 1) {
                this.checked[i] = true;
                BookMarkAct.this.phDelete.setVisibility(View.VISIBLE);
                BookMarkAct.this.phTitle.setText(BookMarkAct.this.getString(R.string.bookmark) + " (" + getSelectedCount() + ")");
                super.notifyDataSetChanged();
            }
        }

        public AnnotsAdapter() {
            this.column = BookMarkAct.this.getListColumn();
            this.checked = new boolean[BookMarkAct.this.annots.size()];
        }

        public int getCount() {
            int i = 1;
            int c = BookMarkAct.this.annots.size();
            int i2 = c / this.column;
            if (this.column <= 1 || c % this.column <= 0) {
                i = 0;
            }
            return i + i2;
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout lay;
            int i;
            View[] items = new View[this.column];
            if (convertView == null) {
                lay = new LinearLayout(BookMarkAct.this);
                for (i = 0; i < items.length; i++) {
                    items[i] = LayoutInflater.from(BookMarkAct.this).inflate(R.layout.annotation_item, null);
                    LayoutParams lp = new LayoutParams(-1, -2);
                    lp.weight = 5.0f;
                    lay.addView(items[i], lp);
                }
            } else {
                lay = (LinearLayout) convertView;
                for (i = 0; i < lay.getChildCount(); i++) {
                    if (i < items.length) {
                        items[i] = lay.getChildAt(i);
                    }
                }
            }
            for (i = 0; i < items.length; i++) {
                int index = (this.column * position) + i;
                if (index >= BookMarkAct.this.annots.size()) {
                    items[i].setVisibility(View.INVISIBLE);
                } else {
                    items[i].setVisibility(View.VISIBLE);
                    BookmarkItem tv = (BookmarkItem) items[i].findViewById(R.id.TextView01);
                    ImageView iv = (ImageView) items[i].findViewById(R.id.ImageView01);
                    TextView noteTv = (TextView) items[i].findViewById(R.id.noteTv);
                    TextView timeTv = (TextView) items[i].findViewById(R.id.timeTv);
                    if (A.isNightState()) {
                        tv.setTextColor(BookMarkAct.this.getResources().getColor(R.color.material_grey_100));
                        noteTv.setTextColor(BookMarkAct.this.getResources().getColor(R.color.material_grey_200));
                        timeTv.setTextColor(BookMarkAct.this.getResources().getColor(R.color.material_grey_300));
                    }
                    if (A.isTablet) {
                        tv.setTextSize(15.0f);
                        noteTv.setTextSize(15.0f);
                        timeTv.setTextSize(13.0f);
                    }
                    AnnotItem n = (AnnotItem) BookMarkAct.this.annots.get(index);
                    if (n.note == null || n.note.note.length() <= 0) {
                        noteTv.setVisibility(View.GONE);
                    } else {
                        noteTv.setVisibility(View.VISIBLE);
                        noteTv.setText(BookMarkAct.this.annotKey == null ? n.note.note : Html.fromHtml(BookMarkAct.this.boldKey(n.note.note, BookMarkAct.this.annotKey)));
                    }
                    int resId = n.resId;
                    if (resId == R.drawable.bmtag) {
                        Drawable d = BookMarkAct.this.getResources().getDrawable(R.drawable.bookmark_tag);
                        if (n.bookmarkItem.color != 0) {
                            d.mutate().setColorFilter(new LightingColorFilter(0, n.bookmarkItem.color));
                        } else {
                            d.mutate().setColorFilter(null);
                        }
                        iv.setImageDrawable(d);
                    } else {
                        iv.setImageResource(resId);
                    }
                    CharSequence text = n.bookmarkItem == null ? BookMarkAct.createSp(n.note, n.number, BookMarkAct.this.annotKey) : BookMarkAct.this.annotKey == null ? n.name : Html.fromHtml(BookMarkAct.this.boldKey(n.name, BookMarkAct.this.annotKey));
                    tv.setText(text);
                    tv.squiggly = n.note == null ? false : n.note.squiggly;
                    tv.s_start = !tv.squiggly ? 0 : text.toString().indexOf(". ") + 2;
                    tv.s_end = !tv.squiggly ? 0 : tv.s_start + n.note.original.replace("\n", " ").trim().length();
                    timeTv.setText(T.dateToStr(Long.valueOf(n.time), A.getLocale()) + " " + T.time(false, A.use12Hour, A.getLocale(), n.time));
                    View view = items[i];
                    int i2 = (index >= this.checked.length || !this.checked[index]) ? R.drawable.my_list_selector : R.drawable.list_selector_background_transition_holo_dark;
                    view.setBackgroundResource(i2);
                    items[i].setTag(Integer.valueOf(index));
                    items[i].setOnClickListener(this.onBookClick);
                    items[i].setOnLongClickListener(this.onBookLongClick);
                    View menuB = items[i].findViewById(R.id.overflow1);
                    menuB.setTag(Integer.valueOf(index));
                    menuB.setOnClickListener(this.onOverflowClick);
                }
            }
            return lay;
        }

        void refreshList(int deleteId) {
            if (deleteId != -1) {
                BookMarkAct.this.annots.remove(deleteId);
            }
            clearChecked();
        }

        void deleteSelectedAnnots() {
            for (int i = this.checked.length - 1; i >= 0; i--) {
                if (this.checked[i]) {
                    AnnotItem n = (AnnotItem) BookMarkAct.this.annots.get(i);
                    if (n.bookmarkItem == null || BookMarkAct.this.bms == null) {
                        A.removeNote(n.note);
                    } else {
                        BookMarkAct.this.bms.remove(n.bookmarkItem);
                    }
                    BookMarkAct.this.annots.remove(i);
                }
            }
            clearChecked();
            A.hasNewNoteForUpload = true;
            A.lastFileAnnotUpdated = true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().addFlags(1024);
        if (VERSION.SDK_INT >= 11) {
            getWindow().addFlags(16777216);
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("bookFile")) {
            this.fullPathName = extras.getString("bookFile");
            if (extras.containsKey("bookmark")) {
                this.bookmark = extras.getString("bookmark");
                this.position = extras.getLong("position");
                this.chapter = extras.getInt("chapter");
                this.splitIndex = extras.getInt("splitIndex");
                this.progress = extras.getString("progress");
            }
        }
        setContentView(R.layout.bookmark_tabs);
        initView();
        A.setDialogNightState(findViewById(R.id.bmAddButton));
        selfPref = this;
    }

    private void initView() {
        this.phExit = findViewById(R.id.exitB);
        this.phDelete = findViewById(R.id.funcB);
        this.phExit.setOnClickListener(this);
        this.phDelete.setOnClickListener(this);
        this.phTitle = (TextView) findViewById(R.id.titleB);
        this.phTitle.setText(R.string.bookmark);
        A.lastFileFromBookmark = null;
        this.bookmarkOptionB = findViewById(R.id.bookmarkOption);
        this.addButton = (Button) findViewById(R.id.bmAddButton);
        this.bookmarkList = (ListView) findViewById(R.id.bmListView01);
        this.bookmarkList.setFastScrollEnabled(false);
        this.noteB = findViewById(R.id.noteLay);
        this.searchB = findViewById(R.id.searchB);
        this.bookmarkB = findViewById(R.id.bookmarkLay);
        this.highlightB = findViewById(R.id.highlightLay);
        this.colorB = findViewById(R.id.colorTv);
        this.filterLay = (LinearLayout) findViewById(R.id.filterLay);
        this.exportB = findViewById(R.id.exportLay);
        this.bookmarkOptionB.setOnClickListener(this);
        this.addButton.setOnClickListener(this);
        this.addButton.setOnLongClickListener(this);
        this.noteB.setOnClickListener(this);
        this.searchB.setOnClickListener(this);
        this.bookmarkB.setOnClickListener(this);
        this.highlightB.setOnClickListener(this);
        this.colorB.setOnClickListener(this);
        this.exportB.setOnClickListener(this);
        if (A.isNightState()) {
//            findViewById(R.id.bmTabOneBook).setBackgroundColor(-10461088);
        }
        refreshBookmarkList();
    }

    public boolean onLongClick(View v) {
        if (v == this.addButton) {
            addNewFavorites(false);
        }
        return false;
    }

    public void onClick(View v) {
        if (v == this.phExit) {
            finish();
        }
        if (v == this.searchB) {
            final EditText et = new ClearableEditText(this);
            et.setText(A.lastSearchKey);
            new AlertDialog.Builder(this).setTitle(A.getContext().getString(R.string.search)).setView(et).setPositiveButton("17039370", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    BookMarkAct.this.annotKey = et.getText().toString();
                    if (!T.isNull(BookMarkAct.this.annotKey)) {
                        A.lastSearchKey = BookMarkAct.this.annotKey;
                        A.saveLastSearchKey();
                    }
                    BookMarkAct.this.annotKey = BookMarkAct.this.annotKey.toLowerCase();
                    BookMarkAct.this.refreshBookmarkList();
                }
            }).setNegativeButton("17039360", null).show();
        }
        if (v == this.bookmarkB) {
            this.annotFilter = 1;
            this.filterColor = 0;
            refreshBookmarkList();
        }
        if (v == this.noteB) {
            this.annotFilter = 2;
            this.filterColor = 0;
            refreshBookmarkList();
        }
        if (v == this.highlightB) {
            this.annotFilter = 3;
            this.filterColor = 0;
            refreshBookmarkList();
        }
        if (v == this.colorB) {
            new AlertDialog.Builder(this).setTitle((int) R.string.highlight_color).setItems(this.filterColors, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String s = BookMarkAct.this.filterColors[which];
                    BookMarkAct.this.filterColor = Integer.valueOf(s.substring(7)).intValue();
                    BookMarkAct.this.refreshBookmarkList();
                }
            }).show();
        }
        if (v == this.exportB) {
            AnnotsAdapter a = (AnnotsAdapter) this.bookmarkList.getAdapter();
            if (a.getSelectedCount() > 0) {
                ArrayList<AnnotItem> annots2 = new ArrayList();
                for (int i = 0; i < a.checked.length; i++) {
                    if (a.checked[i]) {
                        annots2.add(this.annots.get(i));
                    }
                }
                doShare(this, this.fullPathName, annots2, false);
            } else {
                doShare(this, this.fullPathName, this.annots, false);
            }
        }
        if (v == this.addButton) {
            addNewFavorites(true);
        }
        if (v == this.bookmarkOptionB) {
            LinearLayout lay = new LinearLayout(this);
            lay.setOrientation(VERTICAL);
            lay.setPadding(A.d(8.0f), A.d(16.0f), A.d(16.0f), A.d(16.0f));
            final CheckBox cb1 = new CheckBox(this);
            final CheckBox cb2 = new CheckBox(this);
            final boolean sortByDate = A.bookmarkSortByDate;
            lay.addView(cb1);
            lay.addView(cb2);
            cb1.setText(R.string.bookmark_manually);
            cb1.setChecked(A.bookmarkManually);
            cb2.setText(getString(R.string.shelf_sort) + ": " + T.deleteQuotes(getString(R.string.filter)));
            cb2.setChecked(A.bookmarkSortByDate);
            new AlertDialog.Builder(this).setTitle((int) R.string.bookmark).setView(lay).setPositiveButton("17039370", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    A.bookmarkManually = cb1.isChecked();
                    A.bookmarkSortByDate = cb2.isChecked();
                    if (sortByDate != A.bookmarkSortByDate) {
                        BookMarkAct.this.refreshBookmarkList();
                    }
                }
            }).setNegativeButton("17039370", null).show();
        }
        if (v == this.phDelete) {
            ((AnnotsAdapter) this.bookmarkList.getAdapter()).deleteSelectedAnnots();
        }
    }

    private void addNewFavorites(boolean clickDirectly) {
        String text = "(" + this.progress + ") " + this.bookmark;
        boolean tip = (A.bookmarkManually && clickDirectly) || !(A.bookmarkManually || clickDirectly);
        if (tip) {
            PrefEditBookmark prefEditBookmark = new PrefEditBookmark(this, text, new PrefEditBookmark.OnAfterEdit() {
                public void AfterEdit(String result, Integer editColor) {
                    BookMarkAct.this.createBookmarkAndExit(result);
                }
            }, null);
        } else {
            createBookmarkAndExit(text);
        }
    }

    private void createBookmarkAndExit(String text) {
        this.bookmark = text;
        if (this.bms == null) {
            this.bms = new Bookmarks(this.fullPathName);
            A.getBookmarks().add(this.bms);
        }
        this.bms.filename = A.lastFile;
        Bookmark bm = new Bookmark(A.lastFile, this.bookmark, this.chapter, this.splitIndex, this.position, System.currentTimeMillis(), A.getBookmarkColor());
        this.bms.list.add(bm);
        BookDb.addBookmark(bm);
        A.hasNewNoteForUpload = true;
        A.lastFileAnnotUpdated = true;
        T.showToastText(this, getString(R.string.add_bookmark), this.bookmark, 0);
        finish();
    }

    protected void refreshBookmarkList() {
        int i;
        String s;
        NoteInfo n;
        this.bms = A.getBookmarksOfOneFile(this.fullPathName);
        A.sortNotes(A.getHighlights());
        A.sortNotes(A.getNotes());
        if (this.bms != null) {
            A.sortBookmarks(this.bms);
        }
        this.annots = new ArrayList();
        ArrayList<String> colors = new ArrayList();
        if ((this.annotFilter == 0 || this.annotFilter == 1) && this.bms != null) {
            for (i = 0; i < this.bms.list.size(); i++) {
                Bookmark b = (Bookmark) this.bms.list.get(i);
                if ((this.filterColor == 0 || this.filterColor == b.color) && (T.isNull(this.annotKey) || b.name.toLowerCase().indexOf(this.annotKey) != -1)) {
                    this.annots.add(new AnnotItem(this.fullPathName, b.chapter, b.splitIndex, b.position, b.name, R.drawable.bmtag, b, null, b.time, i + 1));
                }
                if (b.color != 0) {
                    s = String.valueOf(b.color);
                    if (colors.indexOf(s) == -1) {
                        colors.add(s);
                    }
                }
            }
        }
        int count = 0;
        if (this.annotFilter == 0 || this.annotFilter == 2) {
            for (i = 0; i < A.getNotes().size(); i++) {
                n = (NoteInfo) A.getNotes().get(i);
                s = n.original.trim();
                if ((this.filterColor == 0 || this.filterColor == n.highlightColor) && (T.isNull(this.annotKey) || (n.note + " " + s).toLowerCase().indexOf(this.annotKey) != -1)) {
                    count++;
                    this.annots.add(new AnnotItem(this.fullPathName, n.lastChapter, n.lastSplitIndex, n.lastPosition, s, R.drawable.bmnote, null, n, n.time, count));
                }
                s = String.valueOf(n.highlightColor);
                if (colors.indexOf(s) == -1) {
                    colors.add(s);
                }
            }
        }
        count = 0;
        if (this.annotFilter == 0 || this.annotFilter == 3) {
            for (i = 0; i < A.getHighlights().size(); i++) {
                n = (NoteInfo) A.getHighlights().get(i);
                if (A.getNotes().indexOf(n) == -1) {
                    if ((this.filterColor == 0 || this.filterColor == n.highlightColor) && (T.isNull(this.annotKey) || n.original.toLowerCase().indexOf(this.annotKey) != -1)) {
                        count++;
                        this.annots.add(new AnnotItem(this.fullPathName, n.lastChapter, n.lastSplitIndex, n.lastPosition, n.original.replace("\n", " "), R.drawable.bmhighlight, null, n, n.time, count));
                    }
                    s = String.valueOf(n.highlightColor);
                    if (colors.indexOf(s) == -1) {
                        colors.add(s);
                    }
                }
            }
        }
        if (colors.size() > 0) {
            this.colorB.setVisibility(0);
            this.filterColors = (String[]) colors.toArray(new String[colors.size()]);
            for (i = 0; i < this.filterColors.length; i++) {
                this.filterColors[i] = "#color#" + this.filterColors[i];
            }
        } else {
            this.colorB.setVisibility(View.GONE);
        }
        if (!A.isLandscape() && A.isSmallPhone) {
            findViewById(R.id.bmexportIv).setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.bmexport)).getPaint().setUnderlineText(true);
        this.bookmarkList.setAdapter(new AnnotsAdapter());
    }

    private void doNoteClick(AnnotItem n) {
        if (n.bookmarkItem != null && n.chapter == A.lastChapter) {
            A.fixIndentBookmarkPos(n.bookmarkItem, A.txtView.getText2());
        }
        openFileFromBookmark(this.fullPathName, n.chapter, n.splitIndex, n.position);
    }

    private int getListColumn() {
        boolean landscape = A.isLandscape();
        if (A.isLargeTablet) {
            if (landscape) {
                return 3;
            }
            return 2;
        } else if (A.isTablet) {
            if (landscape) {
                return 2;
            }
            return 1;
        } else if (landscape) {
            return 2;
        } else {
            return 1;
        }
    }

    private String boldKey(String s, String key) {
        if (T.isNull(key)) {
            return s;
        }
        try {
            s = s.replaceAll("(?i)(" + key + ")", "<b>$1</b>");
        } catch (Exception e) {
            A.error(e);
        }
        return s;
    }

    public static Spannable createSp(NoteInfo n, int number, String key) {
        String start = number + ". ";
        String original = n.original.replace("\n", " ").trim();
        String s = start + original;
        int j1 = start.length();
        int j2 = j1 + original.length();
        Spannable sp = new SpannableString(s);
        if (n.strikethrough) {
            sp.setSpan(new StrikethroughSpan(), j1, j2, 33);
        } else if (n.underline) {
            sp.setSpan(new UnderlineSpan(), j1, j2, 33);
        } else if (!n.squiggly) {
            sp.setSpan(new BackgroundColorSpan(n.highlightColor), j1, j2, 33);
        }
        if (key != null) {
            j1 = s.indexOf(key);
            if (j1 != -1) {
                sp.setSpan(new StyleSpan(1), j1, key.length() + j1, 33);
            }
        }
        return sp;
    }

    protected void openFileFromBookmark(String fullPathName, int chapter, int splitIndex, long position) {
        if (!T.isFile(fullPathName)) {
            T.showAlertText(this, getString(R.string.error), "\"" + fullPathName + "\" " + getString(R.string.not_exists));
        } else if (A.isGalleryImage(fullPathName)) {
            Intent i = new Intent(this, PicGalleryShow.class);
            i.putExtra("imageFile", fullPathName);
            startActivity(i);
        } else {
            disableReaderPauseState();
            A.lastFileFromBookmark = fullPathName;
            A.lastPosition = position;
            A.lastChapter = chapter;
            A.lastSplitIndex = splitIndex;
            getSharedPreferences(A.POSITION_FILE, 0).edit().putString(fullPathName.toLowerCase(), A.lastChapter + "@" + A.lastSplitIndex + "#" + A.lastPosition).commit();
            finish();
        }
    }

    private void disableReaderPauseState() {
        ActivityTxt act = ActivityTxt.selfPref;
        if (act != null) {
            act.isPaused = false;
        }
    }

    public static void doShare(final Context context, final String bookFile, final ArrayList<AnnotItem> annots, final boolean fromMain) {
        if (bookFile != null && annots != null) {
            String[] original = context.getResources().getStringArray(R.array.notes_import_export);
            original[0] = original[0].trim();
            if (original[0].endsWith("...")) {
                original[0] = original[0].substring(0, original[0].length() - 3);
            }
            new AlertDialog.Builder(context).setIcon((int) R.drawable.ic_menu_share_v).setTitle((int) R.string.share).setItems(new String[]{original[0] + "(HTML)", original[0] + "(TXT)", original[1], original[2]}, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (fromMain && bookFile.toLowerCase().endsWith(".pdf")) {
                        T.showAlertText(context, "For Pdf document, please open it to export/import highlights/notes.");
                        return;
                    }
                    switch (which) {
                        case 0:
                        case 1:
                            if (annots != null) {
                                try {
                                    StringBuilder sb = new StringBuilder();
                                    int i1 = 0;
                                    int i2 = 0;
                                    boolean hasChapter = A.ebook != null && A.lastFile.equals(bookFile);
                                    Iterator it = annots.iterator();
                                    while (it.hasNext()) {
                                        AnnotItem N = (AnnotItem) it.next();
                                        if (N.note != null && N.checked) {
                                            if (N.note.note.equals("")) {
                                                i1++;
                                                sb.append("<br><br>\"" + N.note.original + "\"");
                                            } else {
                                                i2++;
                                                sb.append("<br><br>\"" + N.note.original + "\" <br><small><font color='#555555'>(" + context.getString(R.string.annotation) + ": <b><i>" + N.note.note + "</i></b>)</font></small>");
                                            }
                                            if (hasChapter && N.note.lastChapter < A.ebook.getChapters().size()) {
                                                sb.append("<br><small><font color='#555555'>(" + context.getString(R.string.current_chapter) + "<b><i>" + ((BaseEBook.Chapter) A.ebook.getChapters().get(N.note.lastChapter)).name + "</i></b>)</font></small>");
                                            }
                                        }
                                    }
                                    if (sb.length() > 0) {
                                        String author = (A.getBookType() != 100 || A.ebook == null || A.ebook.getAuthor().equals("")) ? "" : " - " + A.ebook.getAuthor();
                                        sb.insert(0, "<b>" + A.getBookName() + author + "</b> (" + context.getString(R.string.highlight) + ": " + i1 + "; " + context.getString(R.string.annotation) + ": " + i2 + ")<br><br>-------------");
                                    }
                                    BookMarkAct.shareContent(context, bookFile, sb.toString(), which == 0, fromMain);
                                    return;
                                } catch (Throwable e) {
                                    A.error(e);
                                    return;
                                }
                            }
                            return;
                        case 2:
                            if (annots != null && annots.size() != 0) {
                                final EditText et = new EditText(context);
                                et.setText("/sdcard/" + T.getOnlyFilename(bookFile) + ".mrexpt");
                                new AlertDialog.Builder(context).setTitle((int) R.string.export).setView(et).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String filename = et.getText().toString();
                                        if (BookDb.exportNotesToFile(bookFile, filename, annots)) {
                                            T.showToastText(context, context.getString(R.string.export_success) + filename, 1);
                                        } else {
                                            T.showToastText(context, context.getString(R.string.export_failed), 1);
                                        }
                                    }
                                }).setNegativeButton(17039360, null).show();
                                return;
                            }
                            return;
                        case 3:
                            final EditText et2 = new EditText(context);
                            String filename = T.getFilePath(bookFile) + "/" + T.getOnlyFilename(bookFile) + ".mrexpt";
                            if (!T.isFile(filename)) {
                                filename = "/sdcard/" + T.getOnlyFilename(bookFile) + ".mrexpt";
                            }
                            et2.setText(filename);
                            new AlertDialog.Builder(context).setTitle((int) R.string.import_ebooks).setView(et2).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String annotFile = et2.getText().toString();
                                    if (T.isFile(annotFile)) {
                                        int result = BookDb.importNotesFromFile(bookFile, annotFile);
                                        if (result == 1 || result == 2) {
                                            A.getBookmarks(true);
                                            if (fromMain) {

                                            } else if (!T.isNull(BookMarkAct.selfPref)) {
                                                A.hasNewNoteForUpload = true;
                                                A.lastFileAnnotUpdated = true;
                                                A.checkNotesHighlights(true);
                                                BookMarkAct.selfPref.refreshBookmarkList();
                                            }
                                            T.showToastText(context, context.getString(R.string.import_success), 1);
                                        }
                                        if (result == -1) {
                                            T.showToastText(context, context.getString(R.string.import_failed), 1);
                                        }
                                        if (result == -2) {
                                            T.showToastText(context, context.getString(R.string.import_not_same_file), 1);
                                            return;
                                        }
                                        return;
                                    }
                                    T.showAlertText(context, context.getString(R.string.error), annotFile + " " + context.getString(R.string.not_exists));
                                }
                            }).setNegativeButton(17039360, null).show();
                            return;
                        default:
                            return;
                    }
                }
            }).show();
        }
    }

    protected static void shareContent(final Context context, final String bookFile, String s, boolean isHtml, boolean fromMain) {
        if (s.length() < 80000) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", context.getString(R.string.share_page) + ": " + A.getBookName());
            if (isHtml) {
                intent.putExtra("android.intent.extra.TEXT", Html.fromHtml(s));
                String thumb = A.getBookThumbFile(bookFile);
                if (T.isFile(thumb)) {
                    intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(thumb)));
                }
            } else {
                intent.putExtra("android.intent.extra.TEXT", Html.fromHtml(s).toString());
            }

        }
        final CharSequence content = !isHtml ? Html.fromHtml(s).toString() : Html.fromHtml(s);
        new AlertDialog.Builder(context).setTitle((CharSequence) "Too many items to share directly").setItems(new String[]{"Copy to clipboard", "Save to txt file"}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    ((ClipboardManager) context.getSystemService("clipboard")).setText(content);
                    T.showToastText(context, context.getString(R.string.copy_to_clipboard));
                }
                if (which == 1) {
                    final EditText et = new EditText(context);
                    et.setText("/sdcard/" + T.getOnlyFilename(bookFile) + ".share.txt");
                    new AlertDialog.Builder(context).setTitle((CharSequence) "Save to txt file").setView(et).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String filename = et.getText().toString();
                            if (T.saveFileText(filename, content.toString())) {
                                T.showToastText(context, context.getString(R.string.export_success) + filename);
                            } else {
                                T.showToastText(context, context.getString(R.string.export_failed));
                            }
                        }
                    }).setNegativeButton(17039360, null).show();
                }
            }
        }).show();
    }

    protected void onStart() {
        super.onStart();
        setScreenOrientation();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((AnnotsAdapter) this.bookmarkList.getAdapter()).getSelectedCount() <= 0) {
            return super.onKeyDown(keyCode, event);
        }
        ((AnnotsAdapter) this.bookmarkList.getAdapter()).clearChecked();
        return true;
    }

    void setScreenOrientation() {
        setRequestedOrientation(A.getScreenOrientation(A.screenState));
    }
}