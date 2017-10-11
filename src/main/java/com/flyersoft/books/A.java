package com.flyersoft.books;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.dropbox.client2.exception.DropboxServerException;
import com.flyersoft.books.BaseEBook.Chapter;
import com.flyersoft.books.BookDb.NoteInfo;
import com.flyersoft.components.BaseCompressor;
import com.flyersoft.components.CSS;
import com.flyersoft.components.ChineseJianFanConvert;
import com.flyersoft.components.ContentLay;
import com.flyersoft.components.DropboxSync;
import com.flyersoft.components.DropboxSync.OnDropBoxDownloaded;
import com.flyersoft.components.DropboxSync.OnDropboxUploaded;
import com.flyersoft.components.DropboxTask;
import com.flyersoft.components.GdriveSync;
import com.flyersoft.components.GdriveSync.OnGdriveDownloaded;
import com.flyersoft.components.GdriveSync.OnGdriveUploaded;
import com.flyersoft.components.GdriveTask;
import com.flyersoft.components.MyDialog.Builder;
import com.flyersoft.components.MyZip_Java;
import com.flyersoft.components.SD;
import com.flyersoft.components.SimpleCrypto;
import com.flyersoft.components.WidgetProvider;
import com.flyersoft.moonreader.ActivityMain;
import com.flyersoft.moonreader.ActivityTxt;
import com.flyersoft.moonreader.BookDownloadService;
import com.flyersoft.moonreader.PicGalleryShow;
import com.flyersoft.moonreader.PrefAbout;
import com.flyersoft.moonreader.R;
import com.flyersoft.moonreader.R.drawable;
import com.flyersoft.staticlayout.MRTextView;
import com.flyersoft.staticlayout.MyHtml;
import com.flyersoft.staticlayout.MyHtml.Ruby;
import com.flyersoft.staticlayout.MyImageSpan;
import com.flyersoft.staticlayout.MyLayout;
import com.flyersoft.staticlayout.SHTextHyphenator;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.http.HttpStatusCodes;
import com.larvalabs.svgandroid.SVGParser;
import com.radaee.pdf.Global;
import com.radaee.pdfex.PDFView.STATUS;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class A {
    public static final String BAIDUBAIKE_URL = "http://baike.baidu.com/search?word=%s&ie=utf-8";
    public static final String BAIDUE_URL = "http://www.baidu.com/s?wd=%s&ie=utf-8";
    public static final long BIG_FILESIZE = 1000000;
    public static final int BIG_SIZE = -1;
    public static final String CHARSET_AUTO = "AUTO";
    public static final int CLICK_BOTTOM = 2;
    public static final int CLICK_LEFT = 3;
    public static final int CLICK_MIDDLE = 0;
    public static final int CLICK_RIGHT = 4;
    public static final int CLICK_TOP = 1;
    public static final int CLOUD_COPY = 5;
    public static final int CLOUD_DELETE = 6;
    public static final int CLOUD_DIR = 0;
    public static final int CLOUD_DOWNLOAD = 1;
    public static final int CLOUD_FILEEXIST = 8;
    public static final int CLOUD_MESSAGE = 50001;
    public static final int CLOUD_MOVE = 4;
    public static final int CLOUD_RENAME = 7;
    public static final int CLOUD_SEARCH = 3;
    public static final int CLOUD_UPLOAD = 2;
    public static final String COVER_TAG = "_2.png";
    public static final String DAY_THEME = "Day Theme";
    public static final int DEAULT_MAX_HTML_CHAPTER_SIZE = 100000;
    public static final String DEFAULT_BOOK_FOLDER = "/sdcard/Books";
    public static final String DEFAULT_DOWNLOAD_SAVED_PATH = "/sdcard/Books/MoonReader";
    public static final String DEFAULT_FONTFACE = "serif";
    public static final String DICTIONARY_URL = "http://dictionary.com/browse/%s";
    public static final String DICT_HISTORY_FILE = "dict_history";
    public static final int DO_AUTOSCROLL = 18;
    public static final int DO_BACK_TO_BOOK_STACK = 14;
    public static final int DO_BOOKMARK = 5;
    public static final int DO_BOOK_STATE = 6;
    public static final int DO_BRIGHTNESS = 20;
    public static final int DO_CHAPTERS = 11;
    public static final int DO_CONTROL = 22;
    public static final int DO_EXIT_READER = 16;
    public static final int DO_FONT_SIZE = 10;
    public static final int DO_MISC = 23;
    public static final int DO_NAVIGATE_MENU = 9;
    public static final int DO_NEXT_CHAPTER = 13;
    public static final int DO_NEXT_FILE = 3;
    public static final int DO_NONE = 15;
    public static final int DO_OPTIONS_MENU = 8;
    public static final int DO_PAGE_DOWN = 1;
    public static final int DO_PAGE_UP = 0;
    public static final int DO_PRIOR_CHAPTER = 12;
    public static final int DO_PRIOR_FILE = 2;
    public static final int DO_RANDOM_THEME = 7;
    public static final int DO_SEARCH = 4;
    public static final int DO_SELECT_TEXT = 17;
    public static final int DO_SPEAK = 19;
    public static final int DO_VISUAL = 21;
    public static final int DROPBOX = 2;
    public static final int DROPBOX_FILES = 2;
    public static final int DROPBOX_SEARCH = 3;
    public static String DROPBOX_TAG = "[Dropbox]";
    public static final int EDGE_BOTTOM = 3;
    public static final int EDGE_LEFT = 0;
    public static final int EDGE_RIGHT = 1;
    public static final int EDGE_TOP = 2;
    public static final String EDITCOVER_TAG = "_3.png";
    public static final String ENCODING_FILE = "file_encoding";
    private static final String FAV_FOLDERS_FILE = "favfolders6";
    public static final String FB2_CSS_FILE = "_fb2.css";
    public static final int FILE_CBR = 11;
    public static final int FILE_CBZ = 10;
    public static final int FILE_CHM = 6;
    public static final int FILE_EBOOK = 100;
    public static final int FILE_EPUB = 2;
    public static final int FILE_FB2 = 5;
    public static final int FILE_HTML = 1;
    public static final int FILE_MOBI = 8;
    public static final int FILE_PDF = 7;
    public static final int FILE_RAR = 9;
    public static final int FILE_TXT = 0;
    public static final int FILE_UMD = 4;
    public static final int FILE_UNKNOW = -1;
    public static final int FILE_ZIP = 3;
    public static final int FLIP_CURL3D = 1;
    public static final int FLIP_CURL3D_G = 2;
    public static final int FLIP_FADE = 5;
    public static final int FLIP_HORIZONTAL = 3;
    public static final int FLIP_NONE = 0;
    public static final int FLIP_SHIFT_HORIZONTAL = 6;
    public static final int FLIP_SHIFT_VERTICAL = 7;
    public static final int FLIP_VERTICAL = 4;
    public static final int FROM_ASSETS = 1;
    public static final int FROM_RESOURCES = 2;
    public static final int FROM_SDCARD = 0;
    public static final int GDRIVE = 4;
    public static final int GDRIVE_FILES = 4;
    public static final int GDRIVE_SEARCH = 5;
    public static String GDRIVE_TAG = "[Gdrive]";
    public static final String GOOGLE_URL = "http://www.google.com/search?q=%s";
    public static final String HISTORY_FILE = "history.txt";
    public static final String IMPORT_FAILED_FILE = "import_failed_list";
    public static final char INDENT_CHAR = '　';
    public static final int LOCAL = 0;
    public static final int LOCAL_FILES = 0;
    public static final int LOCAL_SEARCH = 1;
    public static final int LOW_QUALITY = 1;
    public static final int MOBI_CPU_TYPE = 1;
    public static final String MOONREADER_OPEN_BROADCAST = "com.flyersoft.moonreader.open_broadcast";
    public static final String NETCOVER_TAG = ".png";
    public static final String NIGHT_THEME = "Night Theme";
    public static final int NORMAL_QUALITY = 0;
    public static final int NORMAL_SIZE = 0;
    public static final int ONTOUCH_IGNORETIME = 800;
    public static final String OPTIONS_FILE = "options1002";
    public static final int PDF_CPU_TYPE = 2;
    public static final String POSITION_FILE = "positions10";
    public static String PUNCTUATIONS = "　/ ,.;?\"':(){}[]!，。；＂”“：？（）、！’‘\n\t";
    public static final String READ_STATE_FILTER_FILE = "state_filter";
    private static final String RUNCOUNTKEY = "appRunCount_v4.1.2";
    public static final int SCREEN_ORIENTATION_FULL_SENSOR = 10;
    public static final int SCREEN_ORIENTATION_LANDSCAPE = 0;
    public static final int SCREEN_ORIENTATION_PORTRAIT = 1;
    public static final int SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8;
    public static final int SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9;
    public static final int SCREEN_ORIENTATION_SENSOR = 4;
    public static final int SCREEN_ORIENTATION_SENSOR_LANDSCAPE = 6;
    public static final int SCREEN_ORIENTATION_SENSOR_PORTRAIT = 7;
    public static final int SCROLL_LINE = 3;
    public static final int SCROLL_PAGE = 4;
    public static final int SCROLL_PIXEL = 2;
    public static final int SCROLL_ROLLING_LINE = 1;
    public static final int SCROLL_ROLLING_PIXEL = 0;
    public static final String SEARCH_HISTORY_FILE = "search_history";
    public static final int SMALL_SIZE = 1;
    public static String SYSTEM_FONT_PATH = "/system/fonts";
    public static final String TEMP_THEME = "circle_only";
    public static final String THEME_FILE_TAG = "theme_";
    public static final String THUMB_TAG = "_1.png";
    public static final String TRANSLATION_URL = "http://www.google.com/translate_t?sl=<SL>&tl=<TL>&text=%s";
    public static final int TTS_BY_COMMA = 0;
    public static final int TTS_BY_FULLSTOP = 1;
    public static final int TTS_BY_PAGE = 3;
    public static final int TTS_BY_PARAGRAPH = 2;
    public static final int VERY_SMALL_SIZE = 2;
    public static boolean adTipShowed = false;
    public static boolean adjustBrightness = false;
    public static boolean adjustBrightnessTipped = false;
    public static boolean adjustFontSizeAtSlide = false;
    public static boolean adjustLed = false;
    public static boolean adjustNightLed = false;
    public static boolean allow_scroll_horizontally = false;
    public static Context appContext = null;
    public static String appDataPath = null;
    public static boolean askAutoScrollTip = false;
    public static boolean askClearMasterIssue = false;
    public static boolean askForScrollEvent = false;
    public static boolean askForShakeEvent = false;
    public static boolean askForSwipeEvent = false;
    public static boolean askForTiltAction = false;
    public static boolean askForTts = false;
    public static boolean askTtsTip = false;
    private static ArrayList<String> assetFonts = null;
    public static int autoScrollMode = 0;
    public static int autoScrollSpeed = 0;
    public static boolean autoTheme = false;
    public static boolean autoThemeDay = false;
    public static int autoThemeDayBrightness = 0;
    public static int autoThemeDayTime = 0;
    public static boolean autoThemeNight = false;
    public static int autoThemeNightBrightness = 0;
    public static int autoThemeNightTime = 0;
    private static ArrayList<String> auto_folders = null;
    public static boolean auto_import = false;
    public static long auto_import_time = 0;
    public static int backgroundColor = 0;
    public static String backgroundImage = null;
    private static ArrayList<MyDrawable> backgroundImages = null;
    public static boolean backgroundTtsOption = false;
    public static String backup_folder = null;
    public static FrameLayout baseFrame = null;
    public static boolean bluelightEnable = false;
    public static int bluelightOpacity = 0;
    public static String book_cache = null;
    public static boolean bookmarkManually = false;
    public static boolean bookmarkNoColor = false;
    public static boolean bookmarkSortByDate = false;
    public static int bookmark_color = 0;
    public static ArrayList<Bookmarks> bookmarksList = null;
    public static int bottomMargin = 0;
    public static int brightnessValue = 0;
    public static int brightness_edge = 0;
    public static boolean chapterEndPrompt = false;
    public static ArrayList<TxTChapter> chapters = null;
    public static float cjkFontSize = -1.0f;
    public static boolean cloudHasCurrentBook = false;
    public static String cloud_cache_path = null;
    public static ContentLay contentLay = null;
    private static boolean copiedThemes = false;
    public static String coverImage = null;
    private static ArrayList<Integer> coverImageResIDs = null;
    private static ArrayList<MyDrawable> coverImages = null;
    public static int cover_quality = 0;
    public static int cover_search_engine = 0;
    public static boolean cssAlignment = false;
    public static boolean cssFontColor = false;
    public static boolean cssFontSize = false;
    public static boolean cssFontStyle = false;
    public static boolean cssIndent = false;
    public static boolean cssJustify = false;
    public static boolean cssLineSpace = false;
    public static boolean cssOthers = false;
    public static PageTheme dayTheme = null;
    public static String day_theme_name = null;
    public static String default_book_folder = null;
    public static ArrayList<NoteInfo> deletedNotes = null;
    public static String destLanguage = null;
    public static String deviceRandomID = null;
    public static int dict_index = 0;
    public static int dict_index2 = 0;
    public static boolean directHighlight = false;
    public static boolean disableAudioFocus = false;
    public static boolean disableCSS = false;
    public static int disableLedValue = 0;
    public static boolean disableMove = false;
    public static int doBackKey = 0;
    public static int doCameraKey = 0;
    public static int doDPadCenter = 0;
    public static int doDPadDown = 0;
    public static int doDPadLeft = 0;
    public static int doDPadRight = 0;
    public static int doDPadUp = 0;
    public static int doHeadsetKey = 0;
    public static int doHomeKey = 0;
    public static int doLongTap = 0;
    public static int doMediaPlayNext = 0;
    public static int doMediaPlayPause = 0;
    public static int doMediaPlayPrevious = 0;
    public static int doMenuKey = 0;
    public static int doSearchKey = 0;
    public static int doShakePhone = 0;
    public static int doSwipeBottomToTop = 0;
    public static int doSwipeLeftToRight = 0;
    public static int doSwipeRightToLeft = 0;
    public static int doSwipeTopToBottom = 0;
    public static int doTapScreenBottom = 0;
    public static int doTapScreenLeft = 0;
    public static int doTapScreenRight = 0;
    public static int doTapScreenTop = 0;
    public static int doVolumeKeyDown = 0;
    public static int doVolumeKeyUp = 0;
    public static String download_cache_path = null;
    public static String download_saved_path = null;
    public static DropboxSync dropbox = null;
    public static String dropboxBookPath = null;
    public static BaseEBook ebook = null;
    public static boolean epubThunbnail = false;
    public static int fabButtonType = 0;
    public static String failedScanBook = null;
    public static String failedScanProgress = null;
    public static ArrayList<String> favFolders = null;
    public static int fileCoverSize = 0;
    public static String fileEncoding = null;
    public static boolean fileImportWithFav = false;
    private static ArrayList<String> fileList = null;
    public static boolean filesTabHint = false;
    public static int files_from = 0;
    public static int files_sort_by = 0;
    public static int files_type = 0;
    public static boolean filterAll = false;
    public static boolean filterCbz = false;
    public static boolean filterChm = false;
    public static boolean filterEpub = false;
    public static boolean filterFb2 = false;
    public static boolean filterHtml = false;
    public static boolean filterMobi = false;
    public static boolean filterPdf = false;
    public static boolean filterTxt = false;
    public static boolean filterUmd = false;
    public static boolean firstTimeInstall = false;
    public static boolean firstTimeShowShelf = false;
    public static boolean firstTimeUseTts = false;
    public static String first_history_file = null;
    public static boolean fitHardwareAccelerated = false;
    public static int fixedBlockLength = 1024;
    public static int flipSpeed = 0;
    public static int flip_animation = 0;
    public static boolean fontBold = false;
    public static int fontColor = 0;
    public static boolean fontItalic = false;
    public static String fontName = null;
    public static boolean fontShadow = false;
    public static float fontSize = 0.0f;
    public static int fontSpace = 0;
    public static boolean fontUnderline = false;
    public static int fontsize_edge = 0;
    public static String forceCssFontName = null;
    public static ArrayList<String> forceCssFontTipped = null;
    public static boolean forceEnglishAboutText = false;
    public static boolean forceRebootToMain = false;
    public static boolean forceRebootToTxt = false;
    public static boolean fullscreen = false;
    public static int galleryBrightnessValue = 0;
    public static boolean galleryShowStatusbar = false;
    public static int gallerySlideshowInterval = 0;
    public static boolean galleryWhiteBackground = false;
    public static GdriveSync gdrive = null;
    public static String gdriveBookPath = null;
    public static boolean hardwareAccelerated = false;
    private static Boolean hasNavBar = null;
    public static boolean hasNewNoteForUpload = false;
    public static boolean hasStatisticsAction = false;
    public static boolean highlighInited = false;
    public static ArrayList<String> highlightAllItems = null;
    public static ArrayList<String> highlightAllProperties = null;
    public static int highlightMode = 0;
    public static int highlight_color1 = 1996532479;
    public static int highlight_color2 = -28160;
    public static int highlight_color3 = -3368512;
    public static int highlight_color4 = -6706569;
    public static ArrayList<NoteInfo> highlights = null;
    public static boolean hintForBottomBar = false;
    private static ArrayList<String> historyFiles = null;
    public static int hori_fling_animation = 0;
    public static String hyphenationLang = null;
    public static boolean immersive_fullscreen = false;
    public static boolean importCbz = false;
    public static boolean importChm = false;
    public static boolean importEpub = false;
    public static boolean importFb2 = false;
    public static int importFileSize = 0;
    public static boolean importHtml = false;
    public static boolean importMobi = false;
    public static boolean importPdf = false;
    public static boolean importTxt = false;
    public static boolean importUmd = false;
    public static int indentLength = 0;
    public static boolean indentParagraph = false;
    private static ArrayList<MyDrawable> innerCoverImages = null;
    public static boolean inviteProVersion = false;
    public static long invokeRemind1Time = 0;
    public static boolean invokeRemind2 = false;
    public static boolean isAmazonVersion = false;
    public static boolean isAsiaLanguage = false;
    public static boolean isBetaVersion = false;
    public static boolean isChinese = false;
    public static boolean isChinesePRC = false;
    public static boolean isHtcRom = false;
    public static boolean isInAutoScroll = false;
    public static boolean isJapanese = false;
    public static boolean isKindleFire = false;
    public static boolean isKorean = false;
    public static boolean isLargePhone = false;
    public static boolean isLargeTablet = false;
    public static boolean isProVersion = false;
    public static boolean isSmallPhone = false;
    public static boolean isSmalltablet = false;
    public static boolean isSonyROM = false;
    public static boolean isSpeaking = false;
    public static boolean isTablet = false;
    public static boolean keepOneLineWhenPaging = false;
    public static boolean keepScreenAwake = false;
    public static boolean landscape2PageMode = false;
    public static int languageID = 0;
    public static int lastBlockIndex = 0;
    public static int lastChapter = 0;
    public static String lastDropboxPath = null;
    public static String lastErrorInfo = null;
    public static String lastFile = null;
    public static boolean lastFileAnnotUpdated = false;
    public static String lastFileFromBookmark = null;
    public static String lastGdrivePath = null;
    public static String lastPath = null;
    public static int lastPathItem = 0;
    public static long lastPosition = 0;
    public static long lastReadTime = 0;
    public static String lastSearchKey = null;
    public static int lastSplitIndex = 0;
    public static int lastStatisticsPage = 0;
    public static int lastTab = 0;
    public static String lastTheme = null;
    public static int lastWebFontSize = 0;
    public static int lastWebY = 0;
    public static String last_catalog_url = null;
    public static int leftMargin = 0;
    public static long license_state = 0;
    public static int lineSpace = 0;
    public static boolean loadThemeWithColorOnly = false;
    public static ArrayList<String> localFontfaces = null;
    public static String localeCountry = null;
    public static String localeLanguage = null;
    public static long logTime = 0;
    public static float longTapInterval = 0.0f;
    public static boolean mainNightTheme = false;
    public static int mainNightThemeDim = 32;
    public static boolean mainThemeChanged = false;
    public static int maxFontSize = 100;
    public static int maxHtmlChapterSize = 0;
    public static int minFontSize = 10;
    public static ArrayList<String> missedFonts = null;
    public static ArrayList<String> missedFontsTipped = null;
    public static boolean moveStart = false;
    static MediaPlayer mp = null;
    public static boolean mult_touch = false;
    public static int myDebugTag = 0;
    public static String my_dict_url = null;
    public static boolean needPasswordProtect = false;
    public static int netCoverSize = 0;
    public static boolean newVersionRun = false;
    public static PageTheme nightTheme = null;
    public static String night_theme_name = null;
    public static boolean noMenuKey = false;
    public static ArrayList<NoteInfo> notes = null;
    public static long onConfigurationChangedTime = 0;
    public static boolean oneCharDict = false;
    public static int openBookCount = 0;
    public static boolean openDictDirect = false;
    public static CloudRecentItem openFromRemoteRecent = null;
    public static boolean openLastFile = false;
    public static String outerCoversFolder = null;
    public static String outerFontsFolder = null;
    public static String outerImagesFolder = null;
    public static boolean pageSound = false;
    public static String pageSoundFile = null;
    public static int pageSoundIndex = 0;
    public static boolean pageStyle2PageMode = false;
    public static int paragraphSpace = 0;
    public static boolean passwordCheckFailed = false;
    public static boolean passwordOK = false;
    public static boolean pdfAnnotUpdated = false;
    public static boolean pdfHighlightFromDot = false;
    public static STATUS pdfStatus = STATUS.sta_none;
    public static boolean pdf_annot_button = false;
    public static int pdf_back_color = 0;
    public static boolean pdf_dual_page = false;
    public static boolean pdf_font_hint = false;
    public static int pdf_highlight_color = 0;
    public static int pdf_render_quality = 0;
    public static boolean pdf_scoll_lock = false;
    public static boolean pdf_show_thumb = false;
    public static boolean pdf_text_button = false;
    public static int pdf_theme = 0;
    public static int pdf_view_mode = 0;
    public static int pdf_zoom_level = 0;
    public static long priorLogTime = 0;
    public static boolean ratedPro = false;
    public static int readStateFilter = 0;
    public static int recentCoverSize = 0;
    public static int recent_last_item = 0;
    public static boolean remaingTimeInStatusBar = false;
    public static boolean remind1 = false;
    public static String remind1Text = null;
    public static int remind1Time = 0;
    public static boolean remind2 = false;
    public static String remind2Text = null;
    public static int remind2TimeHour = 0;
    public static int remind2TimeMinute = 0;
    public static CloudRecentItem remoteRecentItem = null;
    public static int rightMargin = 0;
    private static String rom_info = null;
    public static int runCount = 0;
    public static int runCountInApp = 0;
    public static boolean saveThemeHint = false;
    private static String savedBackgroundDrawableName = null;
    public static Drawable savedBackgroundDrwable = null;
    public static String scanFav = null;
    public static String scanPath = null;
    public static int screenAwakeExtend = 0;
    public static Integer screenRealHeight = null;
    public static Integer screenRealWidth = null;
    public static int screenState = 0;
    public static long screensize = 0;
    public static boolean searchCaseSensitive = false;
    public static boolean searchForwardOnly = false;
    public static boolean searchWholeWord = false;
    private static Integer serviceInstalledState = null;
    public static boolean shelfBoldFont = false;
    public static int shelfCoverSize = 0;
    public static int shelfFontSize = 0;
    public static int shelfStyle = 0;
    public static int shelf_category = 0;
    public static int shelf_last_item = 0;
    public static int shelf_sort_by = 0;
    public static String shelf_sub_collection = null;
    public static boolean showAutoscrollButton = false;
    public static boolean showBookmarkButton = false;
    public static boolean showBrightnessButton = false;
    public static boolean showChapterButton = false;
    public static boolean showChromeButton = false;
    public static boolean showColorTemplate = false;
    public static boolean showControlButton = false;
    public static boolean showDayNightButton = false;
    public static boolean showDropboxInLocalFiles = false;
    public static boolean showFileDetail = false;
    public static boolean showFontSizeButton = false;
    public static boolean showGdriveInLocalFiles = false;
    public static boolean showHiddenFiles = false;
    public static boolean showMagnifier = false;
    public static boolean showMiscButton = false;
    public static boolean showOptionButton = false;
    public static boolean showOrientationButton = false;
    public static boolean showRemainingTime = false;
    public static boolean showSearchButton = false;
    public static boolean showShutDownButton = false;
    public static boolean showSpeakButton = false;
    public static boolean showStatusbar = false;
    public static boolean showTiltButton = false;
    public static boolean showVisualButton = false;
    public static boolean showedImageClickTip = false;
    public static boolean showedReaderClickTip = false;
    public static String sourceLanguage = null;
    public static ArrayList<String> splitHtmls = new ArrayList();
    public static int squiggly_color = 0;
    public static boolean startAutoScroll = false;
    public static String startPassword = null;
    public static long statistics_days = 0;
    public static long statistics_end_day = 0;
    public static long statistics_last_day = 0;
    public static long statistics_pages = 0;
    public static long statistics_start_day = 0;
    public static long statistics_time = 0;
    public static long statistics_words = 0;
    public static int statusBackColor = 0;
    public static int statusClickLeft = 0;
    public static int statusClickLeft2 = 0;
    public static int statusClickMiddle = 0;
    public static int statusClickMiddle2 = 0;
    public static int statusClickRight = 0;
    public static int statusClickRight2 = 0;
    public static boolean statusCustomizeFont = false;
    public static int statusFontColor = 0;
    public static String statusFontName = null;
    public static int statusFontSize = 0;
    public static int statusMargin = 0;
    public static boolean statusOnBottom = false;
    public static int strikethrough_color = 0;
    public static boolean syncBookInWiFiOnly = false;
    public static boolean syncReadingBookFile = false;
    public static boolean syncRecentList = false;
    public static int syncType = 0;
    public static boolean syncWindowOpened = false;
    public static int sysbar_height = -1;
    public static int tapMode = 0;
    public static PageTheme tempTheme = null;
    public static boolean textBreakOnlyAtSpaces = false;
    public static boolean textCJK = false;
    public static String textEncode = null;
    public static CharSequence[] textEncodes = null;
    public static boolean textFan2Jian = false;
    public static boolean textHyphenation = false;
    public static boolean textJian2Fan = false;
    public static boolean textJustified = false;
    private static String theEndText = null;
    private static ArrayList<PageTheme> themeList = null;
    public static int tilt_backward = 0;
    public static float tilt_backward_sensitive = 0.0f;
    public static int tilt_forward = 0;
    public static float tilt_forward_sensitive = 0.0f;
    public static boolean tilt_turn_page = false;
    public static boolean tmpOutOfMemoryTag = false;
    public static String tmpRecentListText = null;
    public static String tmp_out_file = null;
    public static boolean toggleTapMode = false;
    public static int topMargin = 0;
    public static View touchingView = null;
    public static GoogleAnalyticsTracker tracker = null;
    static long trackerDispatchTime = 0;
    public static boolean translateInited = false;
    public static boolean trimBlankSpace = false;
    public static ArrayList<TTS_Filter_Item> ttsFilters = null;
    public static int tts_divide = 0;
    public static boolean tts_filter_with_regular_expression = false;
    public static boolean tts_interval_enable = false;
    public static int tts_interval_time = 0;
    public static int tts_pitch = 0;
    public static int tts_speed = 0;
    public static boolean tts_stop_enable = false;
    public static int tts_stop_time = 0;
    public static View txtLay = null;
    public static String txtPauseOnlyTagFile = null;
    public static ScrollView txtScroll = null;
    public static ScrollView txtScroll2 = null;
    public static MRTextView txtView = null;
    public static MRTextView txtView2 = null;
    public static Integer[] txtWordCount = null;
    public static ArrayList<String> txts = new ArrayList();
    private static String[] txts2 = null;
    public static HashMap<String, Typeface> typefaceCache = null;
    public static int underline_color = 0;
    public static String uploadingBookFile = null;
    public static boolean use12Hour = false;
    public static boolean useBackgroundImage = false;
    public static boolean useCssFont = false;
    public static boolean useWebView = false;
    private static ArrayList<Integer> usedCoverRes = null;
    public static final String versionTag = "v4.1.2";
    public static boolean verticalAlignment;
    public static Context widgetContext;
    public static boolean woody;
    public static String xml_files_folder;

    public interface AfterFlipCurl {
        void Finished(boolean z);
    }

    public static class Bookmark {
        public int chapter;
        public boolean checkRealPosition;
        public int color;
        public int drawY;
        public int drawY2;
        public String filename;
        public String name;
        public long position;
        public int splitIndex;
        public long time;

        public Bookmark(String filename, String name, int chapter, int splitIndex, long position, long time, int color) {
            this.filename = filename;
            this.name = name;
            this.chapter = chapter;
            this.splitIndex = splitIndex;
            this.position = position;
            this.time = time;
            this.color = color;
        }
    }

    public static class Bookmarks {
        public String filename;
        public ArrayList<Bookmark> list = new ArrayList();

        public void remove(Bookmark bm) {
            this.list.remove(bm);
            BookDb.removeBookmark(bm);
        }

        public Bookmarks(String filename) {
            this.filename = filename;
        }
    }

    public static class CloudRecentItem {
        public String bookName;
        public String cloudFile;
        public String filenameInUploadedDevice;
        public boolean ok;
        public String positionInfo;
        public float readingProgress;
        public String remoteDeviceId;
        public long remoteFilesize;
        public long remoteTime;

        public CloudRecentItem(String s) {
            try {
                int i1 = s.indexOf("??1");
                int i2 = s.indexOf("??2");
                int i3 = s.indexOf("??3");
                int i4 = s.indexOf("??4");
                int i5 = s.indexOf("??5");
                int i_end = s.indexOf("*", i5);
                this.cloudFile = s.substring(0, i1);
                this.bookName = s.substring(i1 + 3, i2);
                this.remoteTime = Long.valueOf(s.substring(i2 + 3, i3)).longValue();
                this.remoteFilesize = Long.valueOf(s.substring(i3 + 3, i4)).longValue();
                this.filenameInUploadedDevice = s.substring(i4 + 3, i5);
                this.remoteDeviceId = s.substring(i5 + 3, i_end);
                this.positionInfo = s.substring(i5 + 3);
                this.ok = true;
                try {
                    this.readingProgress = Float.valueOf(s.substring(s.lastIndexOf(":") + 1, s.length() - 1)).floatValue();
                } catch (Exception e) {
                    A.error(e);
                }
            } catch (Exception e2) {
                A.error(e2);
                A.log(s);
                this.ok = false;
            }
        }
    }

    public static class MyDrawable {
        public String filename;
        public int from;
        public String imageName;
        public int resourceId;
    }

    public static class PageTheme {
        public String displayName;
        public boolean emptyFontStyle;
        public int pBackgroundColor;
        public String pBackgroundImage;
        public int pBottomMargin;
        public boolean pFontBold;
        public int pFontColor;
        public boolean pFontItalic;
        public String pFontName;
        public boolean pFontShadow;
        public float pFontSize;
        public int pFontSpace;
        public boolean pFontUnderline;
        public int pLeftMargin;
        public int pLineSpace;
        public String pName;
        public int pParagraphSpace;
        public int pRightMargin;
        public boolean pTextJustified;
        public int pTopMargin;
        public boolean pUseBackgroundImage;

        public MyDrawable getDrawableImage() {
            if (this.pBackgroundImage.startsWith("/")) {
                MyDrawable image = new MyDrawable();
                image.from = 0;
                image.filename = this.pBackgroundImage;
                return image;
            }
            Iterator it = A.getBackgroundImages(false).iterator();
            while (it.hasNext()) {
                MyDrawable di = (MyDrawable) it.next();
                if (di.imageName.equals(this.pBackgroundImage)) {
                    return di;
                }
            }
            return null;
        }

        PageTheme(String xml) {
            loadFromXml(xml);
        }

        public void loadFromXml(String xml) {
            SharedPreferences sp = A.getContext().getSharedPreferences(A.THEME_FILE_TAG + xml, 0);
            this.pName = xml;
            if (this.pName.equals(A.DAY_THEME)) {
                this.displayName = A.day_theme_name;
            } else if (this.pName.equals(A.NIGHT_THEME)) {
                this.displayName = A.night_theme_name;
            } else {
                this.displayName = this.pName;
                this.emptyFontStyle = sp.getString("pFontName", "false").equals("false");
            }
            this.pBackgroundImage = sp.getString("pBackgroundImage", A.backgroundImage);
            this.pFontName = sp.getString("pFontName", A.fontName);
            this.pFontColor = sp.getInt("pFontColor", A.fontColor);
            this.pBackgroundColor = sp.getInt("pBackgroundColor", A.backgroundColor);
            this.pTopMargin = sp.getInt("pTopMargin2", A.topMargin);
            this.pBottomMargin = sp.getInt("pBottomMargin2", A.topMargin);
            this.pLeftMargin = sp.getInt("pLeftMargin", A.leftMargin);
            this.pRightMargin = sp.getInt("pRightMargin", A.rightMargin);
            this.pFontSize = A.getSpFloatOrIntValue(sp, "pFontSize", A.fontSize);
            this.pLineSpace = sp.getInt("pLineSpace", A.lineSpace);
            this.pParagraphSpace = sp.getInt("pParagraphSpace", A.paragraphSpace);
            this.pFontSpace = sp.getInt("pFontSpace", A.fontSpace);
            this.pFontBold = sp.getBoolean("pFontBold", A.fontBold);
            this.pFontUnderline = sp.getBoolean("pFontUnderline", A.fontUnderline);
            this.pFontItalic = sp.getBoolean("pFontItalic", A.fontItalic);
            this.pFontShadow = sp.getBoolean("pFontShadow", A.fontShadow);
            this.pTextJustified = sp.getBoolean("pTextJustified", A.textJustified);
            this.pUseBackgroundImage = sp.getBoolean("pUseBackgroundImage", this.pUseBackgroundImage);
        }

        public void saveToXml(String xml, boolean overwrite) {
            this.pName = xml;
            SharedPreferences sp = A.getContext().getSharedPreferences(A.THEME_FILE_TAG + xml, 0);
            if (overwrite) {
                sp.edit().putString("pBackgroundImage", A.backgroundImage).putString("pFontName", A.fontName).putInt("pFontColor", A.fontColor).putInt("pBackgroundColor", A.backgroundColor).putInt("pTopMargin2", A.topMargin).putInt("pBottomMargin2", A.bottomMargin).putInt("pLeftMargin", A.leftMargin).putInt("pRightMargin", A.rightMargin).putFloat("pFontSize", A.fontSize).putInt("pLineSpace", A.lineSpace).putInt("pParagraphSpace", A.paragraphSpace).putInt("pFontSpace", A.fontSpace).putBoolean("pFontBold", A.fontBold).putBoolean("pFontUnderline", A.fontUnderline).putBoolean("pFontItalic", A.fontItalic).putBoolean("pFontShadow", A.fontShadow).putBoolean("pTextJustified", A.textJustified).putBoolean("pUseBackgroundImage", A.useBackgroundImage).commit();
            } else {
                sp.edit().putString("pBackgroundImage", this.pBackgroundImage).putString("pFontName", this.pFontName).putInt("pFontColor", this.pFontColor).putInt("pBackgroundColor", this.pBackgroundColor).putInt("pTopMargin2", this.pTopMargin).putInt("pBottomMargin2", this.pBottomMargin).putInt("pLeftMargin", this.pLeftMargin).putInt("pRightMargin", this.pRightMargin).putFloat("pFontSize", this.pFontSize).putInt("pLineSpace", this.pLineSpace).putInt("pParagraphSpace", this.pParagraphSpace).putInt("pFontSpace", this.pFontSpace).putBoolean("pFontBold", this.pFontBold).putBoolean("pFontUnderline", this.pFontUnderline).putBoolean("pFontItalic", this.pFontItalic).putBoolean("pFontShadow", this.pFontShadow).putBoolean("pTextJustified", this.pTextJustified).putBoolean("pUseBackgroundImage", this.pUseBackgroundImage).commit();
            }
        }
    }

    public static class TTS_Filter_Item {
        public String original;
        public String replaceWith;

        public TTS_Filter_Item(String original, String replaceWith) {
            this.original = original;
            this.replaceWith = replaceWith;
        }
    }

    public static class TtsLine {
        public int end;
        public String s;
        public int start;

        TtsLine(String s, int start, int end, String original, boolean isPdf, int pageStartPos) {
            if (MyHtml.hasRuby) {
                Spanned spanStr = A.txtView.getSpanned();
                if (spanStr != null) {
                    Ruby[] rubys = (Ruby[]) spanStr.getSpans(pageStartPos + start, pageStartPos + end, Ruby.class);
                    if (rubys != null && rubys.length > 0) {
                        for (Ruby r : rubys) {
                            if (!T.isNull(r.original)) {
                                s = s.replace(r.original, r.rt);
                            }
                        }
                    }
                }
            }
            if (MyHtml.hasRuby || A.isJapanese) {
                StringBuilder sb = new StringBuilder();
                int i = 0;
                while (i < s.length()) {
                    if (!A.isSpaceChar(s.charAt(i))) {
                        sb.append(s.charAt(i));
                    } else if (i > 0 && !MRTextView.isSplitChar(s.charAt(i - 1)) && i < s.length() - 1 && !MRTextView.isSplitChar(s.charAt(i + 1))) {
                        sb.append(' ');
                    }
                    i++;
                }
                s = sb.toString();
            }
            A.initTTSFilters();
            Iterator it = A.ttsFilters.iterator();
            while (it.hasNext()) {
                TTS_Filter_Item item = (TTS_Filter_Item) it.next();
                if (item.original.length() > 0) {
                    try {
                        if (A.tts_filter_with_regular_expression) {
                            s = s.replaceAll(item.original, item.replaceWith);
                        } else {
                            s = s.replace(item.original, item.replaceWith);
                        }
                    } catch (Exception e) {
                        A.error(e);
                    }
                }
            }
            s = s.replace('「', '\"').replace('」', '\"').replace(" ’", " ").replace("’ ", " ").replace("‘", " ").replaceAll(" *\n *", " ").replace(A.INDENT_CHAR, ' ').replaceAll(" +", " ");
            while (s.length() > 0 && " '‘’”“\"".indexOf(s.charAt(0)) != -1) {
                s = s.substring(1);
            }
            while (s.length() > 0 && " '‘’”“\"".indexOf(s.charAt(s.length() - 1)) != -1) {
                s = s.substring(0, s.length() - 1);
            }
            if (isPdf) {
                s = s.replace("☀", "");
            }
            if (s.indexOf(A.getChapterEndText()) != -1 || s.equals(MyHtml.HR_TAG)) {
                s = "";
            }
            while (original.length() > start && start > 0 && start < end && "　 ,.;?:\\)}]!，。；\"“”：？）、！\n\t\r".indexOf("" + original.charAt(start)) != -1) {
                start++;
            }
            this.start = start;
            while (original.length() > end && end > start && "　 \\({[\"“”（\n\t\r".indexOf("" + original.charAt(end - 1)) != -1) {
                end--;
            }
            if (original.length() > end && end > start && ",.;，。；?？!！".indexOf("" + original.charAt(end)) != -1) {
                end++;
            }
            this.s = s.replace("#1^", "Mr.").replace("#11^", "Mrs.").replace("#2^", "Dr.");
            this.end = end;
        }

        public boolean isAvialable() {
            if (this.end > this.start) {
                if (this.s.length() > 1) {
                    return true;
                }
                if (this.s.length() == 1 && this.s.charAt(0) != '￼' && A.PUNCTUATIONS.indexOf("" + this.s.charAt(0)) == -1) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class TxTChapter {
        public String chapter;
        public String chapter_trim;
        public long position;

        TxTChapter(String chapter_trim, String chapter, long position) {
            this.chapter_trim = A.trimChapterTitle2(chapter_trim);
            this.chapter = chapter;
            this.position = position;
        }
    }

    public static void LoadOptions(Context context) {
        int i = 15;
        int i2 = 0;
        try {
            boolean z;
            if (appContext == null) {
                appContext = context.getApplicationContext();
            }
            files_type = 0;
            files_from = 0;
            uploadingBookFile = null;
            remoteRecentItem = null;
            checkIfHasMenuKey(context);
            appDataPath = appContext.getApplicationInfo().dataDir;
            xml_files_folder = appDataPath + "/shared_prefs";
            firstTimeInstall = !T.isFile(new StringBuilder().append(xml_files_folder).append("/").append(OPTIONS_FILE).append(".xml").toString());
            theEndText = null;
            txtPauseOnlyTagFile = xml_files_folder + "/txt.paused";
            int h = getScreenHeight2();
            int w = getScreenWidth2();
            int screenSize = appContext.getResources().getConfiguration().screenLayout & 15;
            if (screenSize == 4) {
                z = true;
            } else {
                z = false;
            }
            isLargeTablet = z;
            if (screenSize >= 3) {
                z = true;
            } else {
                z = false;
            }
            isTablet = z;
            if (!isTablet || isLargeTablet) {
                z = false;
            } else {
                z = true;
            }
            isSmalltablet = z;
            if (isTablet) {
                minFontSize = 12;
            } else if (vdf((float) w) > 400.0f && vdf((float) h) > 400.0f) {
                isLargePhone = true;
            } else if (vdf((float) w) < 350.0f && vdf((float) h) < 350.0f) {
                isSmallPhone = true;
            }
            SharedPreferences sp = appContext.getSharedPreferences(OPTIONS_FILE, 0);
            default_book_folder = sp.getString("default_book_folder", DEFAULT_BOOK_FOLDER);
            initPathParameters(false);
            historyFiles = null;
            runCountInApp = sp.getInt(RUNCOUNTKEY, 0) + 1;
            runCount = getRunCount(runCountInApp);
            fixedBlockLength = isTablet ? 4096 : 2048;
            passwordCheckFailed = false;
            getLocaleInfo(appContext);
            if (getROMInfo().indexOf("sony") != -1) {
                z = true;
            } else {
                z = false;
            }
            isSonyROM = z;
            if (getROMInfo().indexOf("htc") != -1) {
                z = true;
            } else {
                z = false;
            }
            isHtcRom = z;
            if (getROMInfo().indexOf("amazon") == -1 && getROMInfo().indexOf("kindle") == -1) {
                z = false;
            } else {
                z = true;
            }
            isKindleFire = z;
            set_default_Line_Font_Space();
            set_default_Margin();
            set_default_flip();
            set_default_Do_Events();
            set_default_Misc();
            newVersionRun = sp.getBoolean(versionTag, true);
            firstTimeShowShelf = sp.getBoolean("firstTimeShowShelf", true);
            adTipShowed = sp.getBoolean("adTipShowed", false);
            landscape2PageMode = sp.getBoolean("landscape2PageMode", isTablet);
            pageStyle2PageMode = sp.getBoolean("pageStyle2PageMode", false);
            showedReaderClickTip = sp.getBoolean("showedClickTop2", false);
            showedImageClickTip = sp.getBoolean("showedImageClickTip", false);
            forceEnglishAboutText = sp.getBoolean("forceEnglishAboutText", false);
            mainNightTheme = sp.getBoolean("mainNightTheme", false);
            askAutoScrollTip = sp.getBoolean("askAutoScrollTip", true);
            askTtsTip = sp.getBoolean("askTtsTip", true);
            tts_filter_with_regular_expression = sp.getBoolean("tts_regular", false);
            ratedPro = sp.getBoolean("ratedPro2", false);
            bookmarkNoColor = sp.getBoolean("bookmarkNoColor", true);
            bookmarkSortByDate = sp.getBoolean("bookmarkSortByDate", false);
            bookmarkManually = sp.getBoolean("bookmarkManually2", true);
            saveThemeHint = sp.getBoolean("saveThemeHint", true);
            filesTabHint = sp.getBoolean("filesTabHint2", true);
            woody = sp.getBoolean("woody", false);
            askForTts = sp.getBoolean("askForTts", true);
            backgroundTtsOption = sp.getBoolean("backgroundTtsOption2", false);
            disableAudioFocus = sp.getBoolean("disableAudioFocus", false);
            firstTimeUseTts = sp.getBoolean("firstTimeUseTts", true);
            lastFile = sp.getString("lastFile", "");
            lastTheme = sp.getString("lastTheme", "DAY_THEME");
            pdf_theme = sp.getInt("pdfTheme", 0);
            lastPathItem = sp.getInt("lastPathItem", -1);
            backup_folder = sp.getString("backup_folder", download_saved_path + "/Backup");
            lastPosition = sp.getLong("lastPosition", 0);
            screensize = sp.getLong("screensize", (long) (h * w));
            lastWebFontSize = sp.getInt("lastWebFontSize", 16);
            lastChapter = sp.getInt("lastChapter", 0);
            lastSplitIndex = sp.getInt("lastSplitIndex", 0);
            openBookCount = sp.getInt("openBookCount", 0);
            lastPath = sp.getString("lastPath", "/sdcard");
            lastDropboxPath = sp.getString("lastDropboxPath", "/");
            lastGdrivePath = sp.getString("lastGdrivePath", "/");
            dropboxBookPath = sp.getString("dropboxBookPath", "/Apps/Books");
            gdriveBookPath = sp.getString("gdriveBookPath", "/Apps/Books");
            scanPath = sp.getString("scanPath", default_book_folder);
            scanFav = sp.getString("scanFav", "");
            deviceRandomID = sp.getString("deviceRandomID2", "" + System.currentTimeMillis());
            failedScanBook = sp.getString("failedScanBook", "");
            failedScanProgress = sp.getString("failedScanProgress", "");
            license_state = sp.getLong("license_state", -1);
            textEncode = getDefaultTextEncode(sp.getString("textEncode", ""));
            outerFontsFolder = sp.getString("outerFontsFolder", "/sdcard/fonts");
            fontSize = getSpFloatOrIntValue(sp, "fontSize", 14.0f);
            if (fontSize == 0.0f) {
                sp.edit().clear().commit();
                T.deleteFile(xml_files_folder + "/" + OPTIONS_FILE + ".xml");
                LoadOptions(context);
                return;
            }
            fontColor = sp.getInt("fontColor", -14999266);
            longTapInterval = sp.getFloat("longTapInterval", 0.4f);
            bookmark_color = sp.getInt("bookmark_color", SupportMenu.CATEGORY_MASK);
            highlight_color1 = sp.getInt("highlight_color11", highlight_color1);
            highlight_color2 = sp.getInt("highlight_color2", highlight_color2);
            highlight_color3 = sp.getInt("highlight_color3", highlight_color3);
            highlight_color4 = sp.getInt("highlight_color4", highlight_color4);
            backgroundColor = deleteAlphaOfColor(sp.getInt("backgroundColor", -1643825));
            pdf_back_color = sp.getInt("pdf_back_color3", -4144960);
            pdf_highlight_color = sp.getInt("pdf_highlight_color", InputDeviceCompat.SOURCE_ANY);
            underline_color = sp.getInt("underline_color", -11184811);
            strikethrough_color = sp.getInt("strikethrough_color", -11184811);
            squiggly_color = sp.getInt("squiggly_color", -16711936);
            Global.inkWidth = sp.getFloat("inkWidth", 2.0f);
            Global.freeTextSize = sp.getFloat("freeTextSize", 18.0f);
            Global.inkColor = sp.getInt("inkColor2", -2143272896);
            Global.fillColor = sp.getInt("fillColor", 0);
            pdf_zoom_level = sp.getInt("pdf_zoom_level", 3);
            pdf_view_mode = sp.getInt("pdf_view_mode2", 0);
            pdf_render_quality = sp.getInt("pdf_render_quality", 1);
            pdf_scoll_lock = sp.getBoolean("pdf_scoll_lock", false);
            pdf_dual_page = sp.getBoolean("pdf_dual_page", false);
            pdf_show_thumb = sp.getBoolean("pdf_show_thumb", false);
            pdf_font_hint = sp.getBoolean("pdf_font_hint", true);
            pdf_text_button = sp.getBoolean("pdf_text_button2", false);
            pdf_annot_button = sp.getBoolean("pdf_annot_button", isProVersion);
            backgroundImage = sp.getString("backgroundImage", "");
            coverImage = sp.getString("coverImage", "cover2");
            highlightMode = sp.getInt("highlightMode", 0);
            useBackgroundImage = sp.getBoolean("useBackgroundImage", false);
            showColorTemplate = sp.getBoolean("highlightColorTemplate", true);
            directHighlight = sp.getBoolean("directHighlight", false);
            String str = "showMagnifier";
            if (isTablet) {
                z = false;
            } else {
                z = true;
            }
            showMagnifier = sp.getBoolean(str, z);
            fontName = sp.contains("fontName") ? sp.getString("fontName", "") : getDefaultFontName();
            fontBold = sp.getBoolean("fontBold", false);
            fontUnderline = sp.getBoolean("fontUnderline", false);
            fontItalic = sp.getBoolean("fontItalic", false);
            fontShadow = sp.getBoolean("fontShadow", false);
            textJustified = sp.getBoolean("textJustified3", isChinese);
            verticalAlignment = sp.getBoolean("verticalAlignment", false);
            textHyphenation = sp.getBoolean("textHyphenation", false);
            textCJK = sp.getBoolean("textCJK", isChinese);
            textJian2Fan = sp.getBoolean("textJian2Fan", false);
            textFan2Jian = sp.getBoolean("textFan2Jian", false);
            hyphenationLang = sp.getString("hyphenationLang", localeLanguage);
            lineSpace = sp.getInt("lineSpace", lineSpace);
            paragraphSpace = sp.getInt("paragraphSpace2", paragraphSpace);
            fontSpace = sp.getInt("fontSpace", fontSpace);
            topMargin = sp.getInt("topMargin2", topMargin);
            bottomMargin = sp.getInt("bottomMargin2", bottomMargin);
            leftMargin = sp.getInt("leftMargin", leftMargin);
            rightMargin = sp.getInt("rightMargin", rightMargin);
            flip_animation = sp.getInt("flip_animation", flip_animation);
            hori_fling_animation = sp.getInt("hori_fling_animation", hori_fling_animation);
            flipSpeed = sp.getInt("flipSpeed2", flipSpeed);
            pageSound = sp.getBoolean("pageSound", false);
            pageSoundIndex = sp.getInt("pageSoundIndex", 0);
            pageSoundFile = sp.getString("pageSoundFile", "");
            screenState = sp.getInt("screenState", defaultScreenState());
            doShakePhone = sp.getInt("doShakePhone", doShakePhone);
            doTapScreenTop = sp.getInt("doTapScreenTop", doTapScreenTop);
            doTapScreenBottom = sp.getInt("doTapScreenBottom", doTapScreenBottom);
            tapMode = sp.getInt("tapMode", 1);
            toggleTapMode = sp.getBoolean("toggleTapMode", toggleTapMode);
            doTapScreenLeft = sp.getInt("doTapScreenLeft", doTapScreenLeft);
            doTapScreenRight = sp.getInt("doTapScreenRight", doTapScreenRight);
            doSwipeLeftToRight = sp.getInt("doSwipeLeftToRight", 15);
            doSwipeRightToLeft = sp.getInt("doSwipeRightToLeft", 15);
            doSwipeTopToBottom = sp.getInt("doSwipeTopToBottom", 15);
            doSwipeBottomToTop = sp.getInt("doSwipeBottomToTop", 15);
            doVolumeKeyUp = sp.getInt("doVolumeKeyUp", doVolumeKeyUp);
            doVolumeKeyDown = sp.getInt("doVolumeKeyDown", doVolumeKeyDown);
            doDPadUp = sp.getInt("doDPadUp", doDPadUp);
            doDPadDown = sp.getInt("doDPadDown", doDPadDown);
            doDPadLeft = sp.getInt("doDPadLeft", doDPadLeft);
            doDPadRight = sp.getInt("doDPadRight", doDPadRight);
            doDPadCenter = sp.getInt("doDPadCenter", doDPadCenter);
            doHomeKey = sp.getInt("doHomeKey", doHomeKey);
            doHeadsetKey = sp.getInt("doHeadsetHook", doHeadsetKey);
            doMediaPlayPause = sp.getInt("doMediaPlayPause", doMediaPlayPause);
            doMediaPlayNext = sp.getInt("doMediaPlayNext", doMediaPlayNext);
            doMediaPlayPrevious = sp.getInt("doMediaPlayPrevious", doMediaPlayPrevious);
            doBackKey = sp.getInt("doBackKey", doBackKey);
            doSearchKey = sp.getInt("doSearchKey", doSearchKey);
            doMenuKey = sp.getInt("doMenuKey", doMenuKey);
            doCameraKey = sp.getInt("doCameraKey", doCameraKey);
            doLongTap = sp.getInt("doLongTap", doLongTap);
            cover_search_engine = sp.getInt("cover_search_engine", -1);
            statistics_pages = sp.getLong("statistics_pages", 0);
            statistics_words = sp.getLong("statistics_words", 0);
            statistics_time = sp.getLong("statistics_time", 0);
            statistics_days = sp.getLong("statistics_days", 1);
            statistics_last_day = sp.getLong("statistics_last_day", 0);
            incReadDays();
            trimBlankSpace = sp.getBoolean("trimBlankSpace", trimBlankSpace);
            indentParagraph = sp.getBoolean("indentParagraph", indentParagraph);
            fullscreen = sp.getBoolean("fullscreen", fullscreen);
            keepScreenAwake = sp.getBoolean("keepScreenAwake", keepScreenAwake);
            adjustBrightness = sp.getBoolean("adjustBrightness", adjustBrightness);
            adjustBrightnessTipped = sp.getBoolean("adjustBrightnessTipped", false);
            adjustLed = sp.getBoolean("adjustLed", adjustLed);
            adjustNightLed = sp.getBoolean("adjustNightLed", adjustNightLed);
            adjustFontSizeAtSlide = sp.getBoolean("adjustFontSizeAtSlide", adjustFontSizeAtSlide);
            bluelightEnable = sp.getBoolean("bluelightEnable", false);
            openLastFile = sp.getBoolean("openLastFile", openLastFile);
            showStatusbar = sp.getBoolean("showStatusbar", showStatusbar);
            remaingTimeInStatusBar = sp.getBoolean("remaingTimeInStatusBar", false);
            showRemainingTime = sp.getBoolean("showRemainingTime", false);
            galleryShowStatusbar = sp.getBoolean("galleryShowStatusbar", true);
            galleryWhiteBackground = sp.getBoolean("galleryWhiteBackground", false);
            keepOneLineWhenPaging = sp.getBoolean("keepOneLineWhenPaging", keepOneLineWhenPaging);
            startAutoScroll = sp.getBoolean("startAutoScroll", startAutoScroll);
            indentLength = sp.getInt("indentLength", 2);
            autoScrollMode = sp.getInt("autoScrollMode", autoScrollMode);
            autoScrollSpeed = sp.getInt("autoScrollSpeed", autoScrollSpeed);
            disableMove = sp.getBoolean("disableMove", false);
            mult_touch = sp.getBoolean("mult_touch", false);
            if (VERSION.SDK_INT < 19 || !noMenuKey) {
                immersive_fullscreen = false;
            } else {
                immersive_fullscreen = sp.getBoolean("immersive_fullscreen", true);
            }
            shelfStyle = sp.getInt("shelfStyle", 0);
            readStateFilter = sp.getInt("readStateFilter", 0);
            tilt_forward = sp.getInt("tilt_forward", 0);
            tilt_backward = sp.getInt("tilt_backward", 1);
            tilt_forward_sensitive = sp.getFloat("tilt_forward_sensitive", 1.0f);
            tilt_backward_sensitive = sp.getFloat("tilt_backward_sensitive", 1.2f);
            showDropboxInLocalFiles = sp.getBoolean("showDropboxInLocalFiles", true);
            showGdriveInLocalFiles = sp.getBoolean("showGdriveInLocalFiles", true);
            allow_scroll_horizontally = sp.getBoolean("allow_scroll_horizontally", allow_scroll_horizontally);
            if (isProVersion) {
                tilt_turn_page = sp.getBoolean("tilt_turn_page", false);
            }
            askForTiltAction = sp.getBoolean("askForTiltAction", true);
            hintForBottomBar = sp.getBoolean("hintForBottomBar", true);
            showSpeakButton = sp.getBoolean("showSpeakButton", true);
            showOrientationButton = sp.getBoolean("showOrientationButton", true);
            showOptionButton = sp.getBoolean("showOptionButton", false);
            showVisualButton = sp.getBoolean("showVisualButton", false);
            showControlButton = sp.getBoolean("showControlButton", false);
            showMiscButton = sp.getBoolean("showMiscButton", false);
            showSearchButton = sp.getBoolean("showSearchButton", false);
            showBrightnessButton = sp.getBoolean("showBrightnessButton", false);
            showTiltButton = sp.getBoolean("showTiltButton", false);
            showFontSizeButton = sp.getBoolean("showFontSizeButton", false);
            showShutDownButton = sp.getBoolean("showShutDownButton", false);
            showChapterButton = sp.getBoolean("showChapterButton", true);
            showBookmarkButton = sp.getBoolean("showBookmarkButton", true);
            showAutoscrollButton = sp.getBoolean("showAutoscrollButton", true);
            showDayNightButton = sp.getBoolean("showDayNightButton", true);
            needPasswordProtect = sp.getBoolean("needPasswordProtect", false);
            startPassword = getDecryptText(sp.getString("loginPassword", ""));
            remind1 = sp.getBoolean("remind11", remind1);
            remind1Time = sp.getInt("remind1Time", remind1Time);
            remind1Text = sp.getString("remind1Text", remind1Text);
            remind2 = sp.getBoolean("remind2", remind2);
            remind2TimeHour = sp.getInt("remind2TimeHour", remind2TimeHour);
            remind2TimeMinute = sp.getInt("remind2TimeMinute", remind2TimeMinute);
            lastTab = sp.getInt("lastDash", 0);
            remind2Text = sp.getString("remind2Text", remind2Text);
            chapterEndPrompt = sp.getBoolean("chapterEndPrompt", chapterEndPrompt);
            epubThunbnail = sp.getBoolean("epubThunbnail", epubThunbnail);
            showHiddenFiles = sp.getBoolean("showHiddenFiles", false);
            showFileDetail = true;
            auto_import = sp.getBoolean("auto_import", false);
            auto_import_time = sp.getLong("auto_import_time", 0);
            sourceLanguage = sp.getString("sourceLanguage", "en");
            destLanguage = sp.getString("destLanguage", localeLanguage);
            my_dict_url = sp.getString("my_dict_url", "http://www.yourdictionary.com/%s");
            last_catalog_url = sp.getString("last_catalog_url", "http://");
            dict_index = sp.getInt("dict_index2", 0);
            dict_index2 = sp.getInt("dict_index_l", -1);
            tts_divide = sp.getInt("tts_divide", 1);
            tts_speed = sp.getInt("tts_speed", 10);
            tts_pitch = sp.getInt("tts_pitch", 10);
            tts_stop_time = sp.getInt("tts_stop_time2", 10);
            tts_interval_time = sp.getInt("tts_interval_time", 500);
            tts_stop_enable = sp.getBoolean("tts_stop_enable", false);
            tts_interval_enable = sp.getBoolean("tts_interval_enable", false);
            translateInited = sp.getBoolean("translateInited2", false);
            forceRebootToTxt = sp.getBoolean("forceRebootToTxt", false);
            forceRebootToMain = sp.getBoolean("forceRebootToMain", false);
            loadThemeWithColorOnly = sp.getBoolean("loadThemeWithColorOnly", true);
            autoTheme = sp.getBoolean("autoTheme", false);
            autoThemeDay = sp.getBoolean("autoThemeDay", true);
            autoThemeDayTime = sp.getInt("autoThemeDayTime", 600);
            autoThemeDayBrightness = sp.getInt("autoThemeDayBrightness", 10);
            autoThemeNight = sp.getBoolean("autoThemeNight", true);
            autoThemeNightTime = sp.getInt("autoThemeNightTime", 2200);
            autoThemeNightBrightness = sp.getInt("autoThemeNightBrightness", 1);
            lastReadTime = sp.getLong("lastReadTime", 0);
            askForSwipeEvent = sp.getBoolean("askForOpenNextPriorFile", true);
            askClearMasterIssue = sp.getBoolean("askClearMasterIssue2", true);
            askForScrollEvent = sp.getBoolean("askForScrollEvent", true);
            askForShakeEvent = sp.getBoolean("askForShakeEvent", true);
            openDictDirect = sp.getBoolean("showDictDirect", false);
            oneCharDict = sp.getBoolean("oneCharDict", isAsiaLanguage);
            showChromeButton = sp.getBoolean("showChromeButton", false);
            disableCSS = sp.getBoolean("disableCSS3", false);
            useCssFont = sp.getBoolean("useCssFont", false);
            cssAlignment = sp.getBoolean("cssAlignment", false);
            cssLineSpace = sp.getBoolean("cssLineSpace2", false);
            cssOthers = sp.getBoolean("cssOthers", false);
            cssIndent = sp.getBoolean("cssIndent2", false);
            cssJustify = sp.getBoolean("cssJustify", true);
            cssFontColor = sp.getBoolean("cssFontColor", false);
            cssFontStyle = sp.getBoolean("cssFontStyle", false);
            cssFontSize = sp.getBoolean("cssFontSize2", false);
            useWebView = sp.getBoolean("useWebView", false);
            syncRecentList = sp.getBoolean("syncRecentList", false);
            syncReadingBookFile = sp.getBoolean("syncReadingBook", false);
            syncBookInWiFiOnly = sp.getBoolean("syncBookInWiFiOnly", true);
            int defaultSyncType = (sp.contains("enabledSync") && sp.getBoolean("enabledSync", false)) ? 2 : 0;
            syncType = sp.getInt("syncType", defaultSyncType);
            statusCustomizeFont = sp.getBoolean("statusCustomizeFont", false);
            str = "use12Hour";
            if (w > DropboxServerException._400_BAD_REQUEST) {
                z = true;
            } else {
                z = false;
            }
            use12Hour = sp.getBoolean(str, z);
            statusClickLeft = sp.getInt("statusClickLeft", 6);
            statusClickMiddle = sp.getInt("statusClickMiddle", 15);
            statusClickRight = sp.getInt("statusClickRight", 5);
            statusClickLeft2 = sp.getInt("statusClickLeft3", 20);
            statusClickMiddle2 = sp.getInt("statusClickMiddle2", 15);
            statusClickRight2 = sp.getInt("statusClickRight2", 15);
            statusFontColor = sp.getInt("statusFontColor", -14540254);
            statusBackColor = sp.getInt("statusBackColor", 858993459);
            String str2 = "statusFontSize";
            if (!isTablet) {
                i = 10;
            } else if (isHighResulotionTablet()) {
                i = 13;
            }
            statusFontSize = sp.getInt(str2, i);
            str2 = "statusMargin";
            if (isTablet) {
                i = 2;
            } else {
                i = 0;
            }
            statusMargin = sp.getInt(str2, i);
            shelfFontSize = sp.getInt("shelfFontSize5", isTablet ? 17 : 16);
            fileCoverSize = sp.getInt("fileCoverSize5", 48);
            netCoverSize = sp.getInt("netCoverSize5", 48);
            shelfCoverSize = sp.getInt("shelfCoverSize6", defShelfCoverSize());
            recentCoverSize = sp.getInt("recentCoverSize", shelfCoverSize);
            str2 = "cover_quality";
            if (VERSION.SDK_INT >= 21) {
                i = 0;
            } else {
                i = 1;
            }
            cover_quality = sp.getInt(str2, i);
            fabButtonType = sp.getInt("fabButtonType2", -1);
            shelf_category = sp.getInt("shelf_category", 0);
            shelf_sort_by = sp.getInt("shelf_sort_by2", 0);
            files_sort_by = sp.getInt("files_sort_by", 0);
            shelf_last_item = sp.getInt("shelf_last_item", -1);
            recent_last_item = sp.getInt("recent_last_item", -1);
            initStatisticsFilterDays(sp);
            String str3 = "lastStatisticsPage";
            if (VERSION.SDK_INT < 11) {
                i2 = 1;
            }
            lastStatisticsPage = sp.getInt(str3, i2);
            importFileSize = sp.getInt("importFileSize", 0);
            bluelightOpacity = sp.getInt("bluelightOpacity", 30);
            brightnessValue = sp.getInt("brightnessValue", -100);
            galleryBrightnessValue = sp.getInt("galleryBrightnessValue", -100);
            gallerySlideshowInterval = sp.getInt("gallerySlideshowInterval", 3);
            screenAwakeExtend = sp.getInt("screenAwakeExtend", 100);
            disableLedValue = sp.getInt("disableLedValue", 10);
            maxHtmlChapterSize = sp.getInt("maxHtmlChapterSize", DEAULT_MAX_HTML_CHAPTER_SIZE);
            brightness_edge = sp.getInt("brightness_edge", 0);
            fontsize_edge = sp.getInt("fontsize_edge", 1);
            sysbar_height = sp.getInt("sysbar_height", sysbar_height);
            statusOnBottom = sp.getBoolean("statusOnBottom2", isTablet);
            inviteProVersion = sp.getBoolean("inviteProVersion", false);
            languageID = sp.getInt("languageID2", 0);
            fileImportWithFav = sp.getBoolean("fileImportWithFav", true);
            searchForwardOnly = sp.getBoolean("searchForwardOnly", false);
            searchCaseSensitive = sp.getBoolean("searchCaseSensitive", false);
            searchWholeWord = sp.getBoolean("searchWholeWord", false);
            shelfBoldFont = sp.getBoolean("shelfBoldFont", false);
            importEpub = sp.getBoolean("importEpub", true);
            importFb2 = sp.getBoolean("importFb2", true);
            importChm = sp.getBoolean("importChm", false);
            importMobi = sp.getBoolean("importMobi", true);
            importCbz = sp.getBoolean("importCbz", false);
            importUmd = sp.getBoolean("importUmd", false);
            importTxt = sp.getBoolean("importTxt", false);
            importPdf = sp.getBoolean("importPdf", true);
            importHtml = sp.getBoolean("importHtml", false);
            filterAll = sp.getBoolean("filterAll", true);
            filterEpub = sp.getBoolean("filterEpub", true);
            filterFb2 = sp.getBoolean("filterFb2", false);
            filterChm = sp.getBoolean("filterChm", false);
            filterMobi = sp.getBoolean("filterMobi", false);
            filterCbz = sp.getBoolean("filterCbz", false);
            filterUmd = sp.getBoolean("filterUmd", false);
            filterTxt = sp.getBoolean("filterTxt", false);
            filterPdf = sp.getBoolean("filterPdf", false);
            filterHtml = sp.getBoolean("filterHtml", false);
            lastSearchKey = sp.getString("lastSearchKey", "");
            statusFontName = sp.getString("statusFontName", "sans-serif");
            shelf_sub_collection = sp.getString("shelf_sub_collection", "");
            outerImagesFolder = sp.getString("outerImagesFolder", "/sdcard/backgrounds");
            outerCoversFolder = sp.getString("outerCoversFolder", "/sdcard/backgrounds");
            invokeRemind1Time = SystemClock.elapsedRealtime();
            adjustDisplay();
            checkTextViewProperties();
            cleanReg();
        } catch (Exception e) {
            error(e);
        }
    }

    public static void SaveOptions(Context context) {
        if (appContext == null) {
            appContext = context;
        }
        Editor editor = appContext.getSharedPreferences(OPTIONS_FILE, 0).edit().putString("default_book_folder", default_book_folder).putBoolean(versionTag, false).putBoolean("firstTimeShowShelf", firstTimeShowShelf).putBoolean("adTipShowed", adTipShowed).putBoolean("landscape2PageMode", landscape2PageMode).putBoolean("pageStyle2PageMode", pageStyle2PageMode).putBoolean("showedClickTop2", showedReaderClickTip).putBoolean("showedImageClickTip", showedImageClickTip).putBoolean("forceEnglishAboutText", forceEnglishAboutText).putBoolean("mainNightTheme", mainNightTheme).putBoolean("askAutoScrollTip", askAutoScrollTip).putBoolean("askTtsTip", askTtsTip).putBoolean("tts_regular", tts_filter_with_regular_expression).putBoolean("ratedPro2", ratedPro).putBoolean("bookmarkNoColor", bookmarkNoColor).putBoolean("bookmarkSortByDate", bookmarkSortByDate).putBoolean("bookmarkManually2", bookmarkManually).putBoolean("saveThemeHint", saveThemeHint).putBoolean("filesTabHint2", filesTabHint).putBoolean("woody", woody).putBoolean("askForTts", askForTts).putBoolean("backgroundTtsOption2", backgroundTtsOption).putBoolean("disableAudioFocus", disableAudioFocus).putBoolean("firstTimeUseTts", firstTimeUseTts).putString("lastFile", lastFile).putString("lastTheme", lastTheme).putInt("pdfTheme", pdf_theme).putInt("lastPathItem", lastPathItem).putString("backup_folder", backup_folder).putLong("lastPosition", lastPosition).putLong("screensize", screensize).putInt("lastWebFontSize", lastWebFontSize).putInt("lastChapter", lastChapter).putInt("lastSplitIndex", lastSplitIndex).putInt("openBookCount", openBookCount).putString("lastPath", lastPath).putString("lastDropboxPath", lastDropboxPath).putString("lastGdrivePath", lastGdrivePath).putString("dropboxBookPath", dropboxBookPath).putString("gdriveBookPath", gdriveBookPath).putString("scanPath", scanPath).putString("scanFav", scanFav).putString("deviceRandomID2", deviceRandomID).putString("failedScanBook", failedScanBook).putString("failedScanProgress", failedScanProgress).putLong("license_state", license_state).putString("outerFontsFolder", outerFontsFolder).putFloat("fontSize", fontSize).putInt("fontColor", fontColor).putFloat("longTapInterval", longTapInterval).putInt("bookmark_color", bookmark_color).putInt("highlight_color11", highlight_color1).putInt("highlight_color2", highlight_color2).putInt("highlight_color3", highlight_color3).putInt("highlight_color4", highlight_color4).putInt("backgroundColor", backgroundColor).putInt("pdf_back_color3", pdf_back_color).putInt("pdf_highlight_color", pdf_highlight_color).putFloat("inkWidth", Global.inkWidth).putFloat("freeTextSize", Global.freeTextSize).putInt("inkColor2", Global.inkColor).putInt("fillColor", Global.fillColor).putInt("strikethrough_color", strikethrough_color).putInt("squiggly_color", squiggly_color).putInt("underline_color", underline_color).putInt("pdf_zoom_level", pdf_zoom_level).putInt("pdf_view_mode2", pdf_view_mode).putInt("pdf_render_quality", pdf_render_quality).putBoolean("pdf_scoll_lock", pdf_scoll_lock).putBoolean("pdf_dual_page", pdf_dual_page).putBoolean("pdf_show_thumb", pdf_show_thumb).putBoolean("pdf_font_hint", pdf_font_hint).putBoolean("pdf_text_button2", pdf_text_button).putBoolean("pdf_annot_button", pdf_annot_button).putString("backgroundImage", backgroundImage).putString("coverImage", coverImage).putInt("highlightMode", highlightMode).putBoolean("useBackgroundImage", useBackgroundImage).putBoolean("highlightColorTemplate", showColorTemplate).putBoolean("directHighlight", directHighlight).putBoolean("showMagnifier", showMagnifier).putString("fontName", fontName).putBoolean("fontBold", fontBold).putBoolean("fontUnderline", fontUnderline).putBoolean("fontItalic", fontItalic).putBoolean("fontShadow", fontShadow).putBoolean("textJustified3", textJustified).putBoolean("verticalAlignment", verticalAlignment).putBoolean("textHyphenation", textHyphenation).putBoolean("textCJK", textCJK).putBoolean("textJian2Fan", textJian2Fan).putBoolean("textFan2Jian", textFan2Jian).putString("hyphenationLang", hyphenationLang).putInt("lineSpace", lineSpace).putInt("paragraphSpace2", paragraphSpace).putInt("fontSpace", fontSpace).putInt("topMargin2", topMargin).putInt("bottomMargin2", bottomMargin).putInt("leftMargin", leftMargin).putInt("rightMargin", rightMargin).putInt("screenState", screenState).putInt("doShakePhone", doShakePhone).putInt("doTapScreenTop", doTapScreenTop).putInt("doTapScreenBottom", doTapScreenBottom).putInt("tapMode", tapMode).putInt("doTapScreenLeft", doTapScreenLeft).putInt("doTapScreenRight", doTapScreenRight).putBoolean("toggleTapMode", toggleTapMode).putInt("doSwipeLeftToRight", doSwipeLeftToRight).putInt("doSwipeRightToLeft", doSwipeRightToLeft).putInt("doSwipeTopToBottom", doSwipeTopToBottom).putInt("doSwipeBottomToTop", doSwipeBottomToTop).putInt("doVolumeKeyUp", doVolumeKeyUp).putInt("doVolumeKeyDown", doVolumeKeyDown).putInt("doDPadUp", doDPadUp).putInt("doDPadDown", doDPadDown).putInt("doDPadLeft", doDPadLeft).putInt("doDPadRight", doDPadRight).putInt("doDPadCenter", doDPadCenter).putInt("doHeadsetHook", doHeadsetKey).putInt("doMediaPlayPause", doMediaPlayPause).putInt("doMediaPlayNext", doMediaPlayNext).putInt("doMediaPlayPrevious", doMediaPlayPrevious).putInt("doBackKey", doBackKey).putInt("doSearchKey", doSearchKey).putInt("doMenuKey", doMenuKey).putInt("doCameraKey", doCameraKey).putInt("doLongTap", doLongTap).putInt("cover_search_engine", cover_search_engine).putLong("statistics_pages", statistics_pages).putLong("statistics_words", statistics_words).putLong("statistics_time", statistics_time).putLong("statistics_days", statistics_days).putLong("statistics_last_day", statistics_last_day).putBoolean("trimBlankSpace", trimBlankSpace).putBoolean("indentParagraph", indentParagraph).putBoolean("fullscreen", fullscreen).putBoolean("keepScreenAwake", keepScreenAwake).putBoolean("openLastFile", openLastFile).putBoolean("showStatusbar", showStatusbar).putBoolean("remaingTimeInStatusBar", remaingTimeInStatusBar).putBoolean("showRemainingTime", showRemainingTime).putBoolean("galleryShowStatusbar", galleryShowStatusbar).putBoolean("galleryWhiteBackground", galleryWhiteBackground).putBoolean("keepOneLineWhenPaging", keepOneLineWhenPaging).putBoolean("startAutoScroll", startAutoScroll).putInt("flip_animation", flip_animation).putInt("hori_fling_animation", hori_fling_animation).putInt("flipSpeed2", flipSpeed).putBoolean("pageSound", pageSound).putInt("pageSoundIndex", pageSoundIndex).putString("pageSoundFile", pageSoundFile).putInt("indentLength", indentLength).putInt("autoScrollMode", autoScrollMode).putInt("autoScrollSpeed", autoScrollSpeed).putBoolean("disableMove", disableMove).putBoolean("mult_touch", mult_touch).putInt("shelfStyle", shelfStyle).putInt("readStateFilter", readStateFilter).putInt("tilt_forward", tilt_forward).putInt("tilt_backward", tilt_backward).putFloat("tilt_forward_sensitive", tilt_forward_sensitive).putFloat("tilt_backward_sensitive", tilt_backward_sensitive).putBoolean("showDropboxInLocalFiles", showDropboxInLocalFiles).putBoolean("showGdriveInLocalFiles", showGdriveInLocalFiles).putBoolean("allow_scroll_horizontally", allow_scroll_horizontally).putBoolean("tilt_turn_page", tilt_turn_page).putBoolean("askForTiltAction", askForTiltAction).putBoolean("hintForBottomBar", hintForBottomBar).putBoolean("showSpeakButton", showSpeakButton).putBoolean("showOrientationButton", showOrientationButton).putBoolean("showMiscButton", showMiscButton).putBoolean("showVisualButton", showVisualButton).putBoolean("showControlButton", showControlButton).putBoolean("showOptionButton", showOptionButton).putBoolean("showSearchButton", showSearchButton).putBoolean("showBrightnessButton", showBrightnessButton).putBoolean("showTiltButton", showTiltButton).putBoolean("showFontSizeButton", showFontSizeButton).putBoolean("showShutDownButton", showShutDownButton).putBoolean("showChapterButton", showChapterButton).putBoolean("showBookmarkButton", showBookmarkButton).putBoolean("showAutoscrollButton", showAutoscrollButton).putBoolean("showDayNightButton", showDayNightButton).putBoolean("needPasswordProtect", needPasswordProtect).putString("loginPassword", getEncryptText(startPassword)).putBoolean("remind11", remind1).putInt("remind1Time", remind1Time).putString("remind1Text", remind1Text).putBoolean("remind2", remind2).putInt("remind2TimeHour", remind2TimeHour).putInt("lastDash", lastTab).putString("remind2Text", remind2Text).putBoolean("chapterEndPrompt", chapterEndPrompt).putBoolean("epubThunbnail", epubThunbnail).putBoolean("showHiddenFiles", showHiddenFiles).putBoolean("auto_import", auto_import).putLong("auto_import_time", auto_import_time).putString("sourceLanguage", sourceLanguage).putString("destLanguage", destLanguage).putString("my_dict_url", my_dict_url).putString("last_catalog_url", last_catalog_url).putInt("dict_index2", dict_index).putInt("dict_index_l", dict_index2).putInt("tts_divide", tts_divide).putInt("tts_speed", tts_speed).putInt("tts_pitch", tts_pitch).putInt("tts_stop_time2", tts_stop_time).putInt("tts_interval_time", tts_interval_time).putBoolean("tts_stop_enable", tts_stop_enable).putBoolean("tts_interval_enable", tts_interval_enable).putBoolean("translateInited2", translateInited).putBoolean("forceRebootToTxt", forceRebootToTxt).putBoolean("forceRebootToMain", forceRebootToMain).putBoolean("loadThemeWithColorOnly", loadThemeWithColorOnly).putBoolean("autoTheme", autoTheme).putBoolean("autoThemeDay", autoThemeDay).putInt("autoThemeDayTime", autoThemeDayTime).putInt("autoThemeDayBrightness", autoThemeDayBrightness).putBoolean("autoThemeNight", autoThemeNight).putInt("autoThemeNightTime", autoThemeNightTime).putInt("autoThemeNightBrightness", autoThemeNightBrightness).putLong("lastReadTime", lastReadTime).putBoolean("askForOpenNextPriorFile", askForSwipeEvent).putBoolean("askClearMasterIssue2", askClearMasterIssue).putBoolean("askForScrollEvent", askForScrollEvent).putBoolean("askForShakeEvent", askForShakeEvent).putBoolean("showDictDirect", openDictDirect).putBoolean("oneCharDict", oneCharDict).putBoolean("showChromeButton", showChromeButton).putBoolean("disableCSS3", disableCSS).putBoolean("useCssFont", useCssFont).putBoolean("cssAlignment", cssAlignment).putBoolean("cssLineSpace2", cssLineSpace).putBoolean("cssOthers", cssOthers).putBoolean("cssIndent2", cssIndent).putBoolean("cssJustify", cssJustify).putBoolean("cssFontColor", cssFontColor).putBoolean("cssFontStyle", cssFontStyle).putBoolean("cssFontSize2", cssFontSize).putBoolean("useWebView", useWebView).putBoolean("syncRecentList", syncRecentList).putBoolean("syncReadingBook", syncReadingBookFile).putBoolean("syncBookInWiFiOnly", syncBookInWiFiOnly).putInt("syncType", syncType).putBoolean("statusCustomizeFont", statusCustomizeFont).putBoolean("use12Hour", use12Hour).putInt("fabButtonType2", fabButtonType).putInt("shelf_category", shelf_category).putInt("shelf_sort_by2", shelf_sort_by).putInt("files_sort_by", files_sort_by).putInt("shelf_last_item", shelf_last_item).putInt("recent_last_item", recent_last_item).putLong("statistics_start_day", statistics_start_day).putLong("statistics_end_day", statistics_end_day).putInt("lastStatisticsPage", lastStatisticsPage).putInt("importFileSize", importFileSize).putInt("statusFontSize", statusFontSize).putInt("statusMargin", statusMargin).putInt("statusFontColor", statusFontColor).putInt("statusBackColor", statusBackColor).putInt("statusClickLeft", statusClickLeft).putInt("statusClickMiddle", statusClickMiddle).putInt("statusClickRight", statusClickRight).putInt("statusClickLeft3", statusClickLeft2).putInt("statusClickMiddle2", statusClickMiddle2).putInt("statusClickRight2", statusClickRight2).putInt("shelfFontSize5", shelfFontSize).putInt("shelfCoverSize6", shelfCoverSize).putInt("recentCoverSize", recentCoverSize).putInt("netCoverSize5", netCoverSize).putInt("fileCoverSize5", fileCoverSize).putInt("cover_quality", cover_quality).putBoolean("adjustBrightness", adjustBrightness).putBoolean("adjustBrightnessTipped", adjustBrightnessTipped).putBoolean("adjustLed", adjustLed).putBoolean("adjustNightLed", adjustNightLed).putBoolean("adjustFontSizeAtSlide", adjustFontSizeAtSlide).putBoolean("bluelightEnable", bluelightEnable).putInt("bluelightOpacity", bluelightOpacity).putInt("brightnessValue", brightnessValue).putInt("galleryBrightnessValue", galleryBrightnessValue).putInt("gallerySlideshowInterval", gallerySlideshowInterval).putInt("screenAwakeExtend", screenAwakeExtend).putInt("disableLedValue", disableLedValue).putInt("maxHtmlChapterSize", maxHtmlChapterSize).putInt("brightness_edge", brightness_edge).putInt("fontsize_edge", fontsize_edge).putInt("sysbar_height", sysbar_height).putBoolean("statusOnBottom2", statusOnBottom).putBoolean("inviteProVersion", inviteProVersion).putInt("languageID2", languageID).putBoolean("fileImportWithFav", fileImportWithFav).putBoolean("searchForwardOnly", searchForwardOnly).putBoolean("searchCaseSensitive", searchCaseSensitive).putBoolean("searchWholeWord", searchWholeWord).putBoolean("shelfBoldFont", shelfBoldFont).putBoolean("importEpub", importEpub).putBoolean("importHtml", importHtml).putBoolean("importFb2", importFb2).putBoolean("importChm", importChm).putBoolean("importMobi", importMobi).putBoolean("importCbz", importCbz).putBoolean("importUmd", importUmd).putBoolean("filterAll", filterAll).putBoolean("importTxt", importTxt).putBoolean("importPdf", importPdf).putBoolean("filterEpub", filterEpub).putBoolean("filterHtml", filterHtml).putBoolean("filterFb2", filterFb2).putBoolean("filterChm", filterChm).putBoolean("filterMobi", filterMobi).putBoolean("filterCbz", filterCbz).putBoolean("filterUmd", filterUmd).putBoolean("filterTxt", filterTxt).putBoolean("filterPdf", filterPdf).putString("lastSearchKey", lastSearchKey).putString("statusFontName", statusFontName).putString("shelf_sub_collection", shelf_sub_collection).putString("outerImagesFolder", outerImagesFolder).putString("outerCoversFolder", outerCoversFolder).putInt(RUNCOUNTKEY, runCountInApp);
        if (VERSION.SDK_INT >= 19 && noMenuKey) {
            editor.putBoolean("immersive_fullscreen", immersive_fullscreen);
        }
        editor.commit();
        T.saveFileText(getRunCountFile(), "" + runCount);
    }

    private static void initStatisticsFilterDays(SharedPreferences sp) {
        if (sp.contains("statistics_start_day")) {
            statistics_start_day = sp.getLong("statistics_start_day", 0);
            statistics_end_day = sp.getLong("statistics_end_day", 0);
            return;
        }
        Calendar c = Calendar.getInstance();
        c.set(c.get(1), 11, 31);
        statistics_end_day = c.getTimeInMillis();
        c.set(2010, 7, 1);
        statistics_start_day = c.getTimeInMillis();
    }

    public static int defShelfCoverSize() {
        return (!isTablet && isSmallPhone) ? 100 : 110;
    }

    private static String getDefaultFontName() {
        return "";
    }

    private static void cleanReg() {
        if (newVersionRun) {
            Iterator it;
            SharedPreferences sp = appContext.getSharedPreferences(OPTIONS_FILE, 0);
            ArrayList<String> list = new ArrayList();
            for (String key : sp.getAll().keySet()) {
                if (key.startsWith("v1.") || key.startsWith("v2.") || key.startsWith("v3.") || key.startsWith("appRunCount_")) {
                    list.add(key);
                }
            }
            if (list.size() > 0) {
                Editor et = sp.edit();
                it = list.iterator();
                while (it.hasNext()) {
                    et.remove((String) it.next());
                }
                et.commit();
            }
        }
    }

    private static String getRunCountFile() {
        return download_cache_path + (isProVersion ? "/.cp2" : "/.c2");
    }

    private static int getRunCount(int phoneCount) {
        String sdsave = T.getFileText(getRunCountFile());
        int sdCount = 0;
        if (sdsave != null) {
            try {
                sdCount = Integer.valueOf(sdsave).intValue();
            } catch (Exception e) {
                error(e);
                sdCount = 0;
            }
        }
        if (sdCount > phoneCount) {
            return sdCount;
        }
        return phoneCount;
    }

    private static void adjustDisplay() {
        int i = 17;
        float f = 18.0f;
        if (languageID > 0) {
            setLanguage(appContext);
        } else {
            getLocaleInfo(appContext, true);
        }
        if (firstTimeInstall) {
            getThemeList();
        }
        if (copiedThemes) {
            float f2;
            copiedThemes = false;
            loadTheme(DAY_THEME);
            set_default_Margin();
            if (isTablet) {
                f2 = 18.0f;
            } else {
                f2 = (float) (isAsiaLanguage ? 17 : 14);
            }
            fontSize = f2;
            saveTheme(DAY_THEME, true);
            saveTheme(TEMP_THEME, true);
            loadTheme(NIGHT_THEME);
            set_default_Margin();
            if (!isTablet) {
                if (!isAsiaLanguage) {
                    i = 14;
                }
                f = (float) i;
            }
            fontSize = f;
            saveTheme(NIGHT_THEME, true);
            loadTheme(DAY_THEME);
        }
    }

    public static String getROMInfo() {
        if (rom_info == null) {
            StringBuilder sb = new StringBuilder();
            try {
                sb.append("*" + Build.MODEL + " (" + Build.CPU_ABI).append(", " + Build.MANUFACTURER + ", " + Build.PRODUCT + ", " + Build.ID + ")\n");
            } catch (Exception e) {
                error(e);
            }
            rom_info = sb.toString().toLowerCase();
        }
        return rom_info;
    }

    public static Context getContext() {
        Context c = appContext;
        if (c == null && !T.isNull(ActivityMain.selfPref)) {
            c = ActivityMain.selfPref.getApplicationContext();
        }
        if (c == null && !T.isNull(ActivityTxt.selfPref)) {
            c = ActivityTxt.selfPref.getApplicationContext();
        }
        if (c == null && !T.isNull(PicGalleryShow.selfPref)) {
            c = PicGalleryShow.selfPref.getApplicationContext();
        }
        if (c == null && widgetContext != null) {
            return widgetContext;
        }
        appContext = c;
        return c;
    }

    public static int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenHeight2() {
        return (VERSION.SDK_INT < 19 || screenRealHeight == null) ? getScreenHeight() : screenRealHeight.intValue();
    }

    public static int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenWidth2() {
        return (VERSION.SDK_INT < 19 || screenRealWidth == null) ? getScreenWidth() : screenRealWidth.intValue();
    }

    public static int clickArea(MotionEvent event) {
        return clickArea(event, true);
    }

    public static int clickArea(MotionEvent event, boolean fromReader) {
        float x = event.getX();
        if (fromReader && touchingView == txtScroll2) {
            x += (float) (baseFrame.getWidth() / 2);
        }
        float y = event.getY();
        int w = fromReader ? baseFrame.getWidth() : getScreenWidth();
        int h = fromReader ? baseFrame.getHeight() : getScreenHeight();
        switch (tapMode) {
            case 0:
                if (toggleTapMode) {
                    if (x < ((float) (w / 3))) {
                        return 3;
                    }
                    if (x > ((float) ((w * 2) / 3))) {
                        return 4;
                    }
                    return 0;
                } else if (y < ((float) (h / 3))) {
                    return 1;
                } else {
                    if (y > ((float) ((h * 2) / 3))) {
                        return 2;
                    }
                    return 0;
                }
            case 1:
                if (toggleTapMode) {
                    if (x < ((float) (w / 3))) {
                        return 3;
                    }
                    if (x < ((float) ((w * 2) / 3)) && y < ((float) (h / 3))) {
                        return 3;
                    }
                    if (x > ((float) ((w * 2) / 3)) || (x > ((float) (w / 3)) && y > ((float) ((h * 2) / 3)))) {
                        return 4;
                    }
                    return 0;
                } else if (y < ((float) (h / 3)) || (y < ((float) ((h * 2) / 3)) && x > ((float) ((w * 2) / 3)))) {
                    return 1;
                } else {
                    if (y > ((float) ((h * 2) / 3)) || (y > ((float) (h / 3)) && x < ((float) (w / 3)))) {
                        return 2;
                    }
                    return 0;
                }
            case 2:
                if (toggleTapMode) {
                    if (x < ((float) (w / 3))) {
                        return 3;
                    }
                    if (x < ((float) ((w * 4) / 9)) && (y < ((float) (h / 3)) || y > ((float) ((h * 2) / 3)))) {
                        return 3;
                    }
                    if (x > ((float) ((w * 2) / 3)) || (x > ((float) ((w * 5) / 9)) && (y < ((float) (h / 3)) || y > ((float) ((h * 2) / 3))))) {
                        return 4;
                    }
                    return 0;
                } else if (y < ((float) (h / 3)) || (y < ((float) ((h * 4) / 9)) && (x < ((float) (w / 3)) || x > ((float) ((w * 2) / 3))))) {
                    return 1;
                } else {
                    if (y > ((float) ((h * 2) / 3)) || (y > ((float) ((h * 5) / 9)) && (x < ((float) (w / 3)) || x > ((float) ((w * 2) / 3))))) {
                        return 2;
                    }
                    return 0;
                }
            case 3:
                if (toggleTapMode) {
                    if (x < ((float) (w / 3))) {
                        return 3;
                    }
                    if (x < ((float) (w / 2)) && (y < ((float) (h / 3)) || y > ((float) ((h * 2) / 3)))) {
                        return 3;
                    }
                    if (x > ((float) ((w * 2) / 3)) || (x > ((float) (w / 2)) && (y < ((float) (h / 3)) || y > ((float) ((h * 2) / 3))))) {
                        return 4;
                    }
                    return 0;
                } else if (y < ((float) (h / 3)) || (y < ((float) (h / 2)) && (x < ((float) (w / 3)) || x > ((float) ((w * 2) / 3))))) {
                    return 1;
                } else {
                    if (y > ((float) ((h * 2) / 3)) || (y > ((float) (h / 2)) && (x < ((float) (w / 3)) || x > ((float) ((w * 2) / 3))))) {
                        return 2;
                    }
                    return 0;
                }
            case 4:
                if (y < ((float) (h / 3)) && x < ((float) (w / 3))) {
                    return 1;
                }
                if (x <= ((float) (w / 3)) || x >= ((float) ((w * 2) / 3)) || y <= ((float) (h / 3)) || y >= ((float) ((h * 2) / 3))) {
                    return 2;
                }
                return 0;
            case 5:
                if (y < ((float) (h / 3)) && x > ((float) (w / 3)) && x < ((float) ((w * 2) / 3))) {
                    return 1;
                }
                if (x <= ((float) (w / 3)) || x >= ((float) ((w * 2) / 3)) || y <= ((float) (h / 3)) || y >= ((float) ((h * 2) / 3))) {
                    return 2;
                }
                return 0;
            case 6:
                if (y < ((float) (h / 3)) && x > ((float) ((w * 2) / 3))) {
                    return 1;
                }
                if (x <= ((float) (w / 3)) || x >= ((float) ((w * 2) / 3)) || y <= ((float) (h / 3)) || y >= ((float) ((h * 2) / 3))) {
                    return 2;
                }
                return 0;
            default:
                return 0;
        }
    }

    public static int getPriorTxtLength(int countToBlock) {
        int addedLength = 0;
        int l = txts.size();
        if (countToBlock == -1 || countToBlock > l) {
            countToBlock = l;
        }
        for (int i = 0; i < countToBlock; i++) {
            addedLength += ((String) txts.get(i)).length();
        }
        return addedLength;
    }

    public static long txtLength() {
        return (long) getPriorTxtLength(-1);
    }

    public static String getBlocksText(int blockIndex) {
        switch (txts.size()) {
            case 0:
                return "";
            case 1:
                return getTxts2(0);
            case 2:
                return getTxts2(0) + getTxts2(1);
            default:
                int i0;
                int i1;
                int i2;
                if (blockIndex == 0) {
                    i0 = 0;
                    i1 = 1;
                    i2 = 2;
                } else if (blockIndex >= txts.size() - 1) {
                    i2 = txts.size() - 1;
                    i1 = i2 - 1;
                    i0 = i1 - 1;
                } else {
                    i0 = blockIndex - 1;
                    i1 = i0 + 1;
                    i2 = i1 + 1;
                }
                return getTxts2(i0) + getTxts2(i1) + getTxts2(i2);
        }
    }

    public static int getBlocksLength(int blockIndex) {
        switch (txts.size()) {
            case 0:
                return 0;
            case 1:
                return ((String) txts.get(0)).length();
            case 2:
                return ((String) txts.get(1)).length() + ((String) txts.get(0)).length();
            default:
                int i0;
                int i1;
                int i2;
                if (blockIndex == 0) {
                    i0 = 0;
                    i1 = 1;
                    i2 = 2;
                } else if (blockIndex >= txts.size() - 1) {
                    i2 = txts.size() - 1;
                    i1 = i2 - 1;
                    i0 = i1 - 1;
                } else {
                    i0 = blockIndex - 1;
                    i1 = i0 + 1;
                    i2 = i1 + 1;
                }
                return ((String) txts.get(i2)).length() + (((String) txts.get(i0)).length() + ((String) txts.get(i1)).length());
        }
    }

    public static ArrayList<String> getTxts() {
        if (txts == null) {
            txts = new ArrayList();
        }
        return txts;
    }

    public static String getTxts(int index) {
        if (index > txts.size() - 1) {
            return "";
        }
        return (String) txts.get(index);
    }

    public static void clearTxts2() {
        txts2 = new String[txts.size()];
        txtWordCount = new Integer[txts.size()];
    }

    public static String getTxts2(int index) {
        if (index > txts.size() - 1) {
            return "";
        }
        try {
            if (txts2 == null || txts2.length != txts.size() || isLowestMemory()) {
                txts2 = new String[txts.size()];
            }
            if (txts2[index] == null) {
                txts2[index] = getTxtDisplayText((String) txts.get(index));
                if (chapterEndPrompt && index == txts.size() - 1) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String[] strArr = txts2;
                    strArr[index] = stringBuilder.append(strArr[index]).append("\n\n").append(theEndText()).append("\n\n").toString();
                }
            }
        } catch (OutOfMemoryError e) {
            System.exit(0);
        }
        return txts2[index];
    }

    public static String getTxtDisplayText(String text) {
        text = text.replace("\r", "");
        if (trimBlankSpace) {
            text = doTrimTxtSpace(text);
        }
        if (indentParagraph) {
            return doIndentParagraph(text);
        }
        return text;
    }

    public static long getTxtRealPos(long p) {
        try {
            int index = lastBlockIndex == 0 ? 0 : lastBlockIndex - 1;
            if (index > 0) {
                int l1 = getTxts(index).length();
                int r1 = getTxts2(index).length();
                int l2 = getTxts(index + 1).length();
                int r2 = getTxts2(index + 1).length();
                if (p > ((long) (r1 + r2))) {
                    p = (((long) (l1 - r1)) + p) + ((long) (l2 - r2));
                } else if (p > ((long) r1)) {
                    p += (long) (l1 - r1);
                }
            }
            p += (long) getPriorTxtLength(index);
            if (p > txtLength()) {
                return txtLength();
            }
            return p;
        } catch (Exception e) {
            error(e);
            return -1;
        }
    }

    public static long getTxtDisplayPos(long position, boolean changeRecord) {
        long p = 0;
        if (position < 0) {
            position = 0;
        }
        long pos = position < txtLength() ? position : txtLength();
        int block = (int) (pos / ((long) fixedBlockLength));
        if (block > 0) {
            p = (long) getTxts2(block - 1).length();
        }
        p += pos % ((long) fixedBlockLength);
        if (block == getTxts().size() - 1 && getTxts().size() >= 3) {
            p += (long) getTxts2(block - 2).length();
            block--;
        }
        if (changeRecord) {
            lastPosition = pos;
            lastBlockIndex = block;
        }
        return p;
    }

    public static String doIndentParagraph(String text) {
        return text.replace(INDENT_CHAR, ' ').replace('\t', ' ').replaceAll("\n *", "\n");
    }

    public static String doTrimBlankSpace(String text) {
        return text.replace(INDENT_CHAR, ' ').replace('\t', ' ').replaceAll(" +", " ").replaceAll(" *\n *", "\n").replaceAll("\n+", "\n");
    }

    public static String doTrimTxtSpace(String text) {
        text = text.replace(INDENT_CHAR, ' ').replace('\t', ' ').replaceAll(" +", " ");
        int start = 1;
        while (true) {
            int n = text.indexOf("\n", start);
            if (n != -1 && n != text.length() - 1) {
                char c1 = text.charAt(n - 1);
                char c2 = text.charAt(n + 1);
                if (!(" -.;?\"':)。；”：？！）".indexOf(c1) != -1 || c2 == '\n' || isBlankChar(c2))) {
                    text = text.substring(0, n) + (Character.getType(c1) == 5 ? "☀" : " ") + text.substring(n + 1);
                }
                start = n + 1;
            }
        }
        return text.replaceAll("☀", "").replaceAll(" *\n *", "\n").replaceAll("\n+", "\n");
    }

    public static String getFileEncode(String filename) throws IOException {
        Throwable th;
        RandomAccessFile raf = null;
        String encode = "";
        try {
            RandomAccessFile raf2 = new RandomAccessFile(filename, "r");
            try {
                raf2.seek(0);
                int flag1 = 0;
                int flag2 = 0;
                int flag3 = 0;
                long l = raf2.length();
                if (l >= 2) {
                    flag1 = raf2.readUnsignedByte();
                    flag2 = raf2.readUnsignedByte();
                }
                if (l >= 3) {
                    flag3 = raf2.readUnsignedByte();
                }
                encode = getEncode(flag1, flag2, flag3);
                if (raf2 != null) {
                    raf2.close();
                }
                if (!encode.equals("")) {
                    return encode;
                }
                if ((isChinese || getFileType(filename) == 1) && isNoBomUTF8Chinese(filename)) {
                    return "UTF-8";
                }
                return encode;
            } catch (Throwable th2) {
                th = th2;
                raf = raf2;
                if (raf != null) {
                    raf.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (raf != null) {
                raf.close();
            }
            throw th;
        }
    }

    private static String getEncode(int flag1, int flag2, int flag3) {
        String encode = "";
        if (flag1 == 255 && flag2 == 254) {
            return "UTF-16LE";
        }
        if (flag1 == 254 && flag2 == 255) {
            return "UTF-16BE";
        }
        if (flag1 == 239 && flag2 == 187 && flag3 == 191) {
            return "UTF-8";
        }
        return encode;
    }

    private static boolean isNoBomUTF8Chinese(String filename) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(filename))), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[8192];
            int size = in.read(buffer);
            if (size != -1) {
                sb.append(String.valueOf(buffer, 0, size));
            }
            String text = sb.toString();
            int val = 0;
            if (getFileType(filename) == 1 && (text.indexOf("charset=UTF-8") != -1 || text.indexOf("charset=\"UTF-8\"") != -1)) {
                val = 0 + 1;
            } else if (text.indexOf("的") != -1) {
                val = 0 + 1;
            } else if (text.indexOf("了") != -1) {
                val = 0 + 1;
            } else if (text.indexOf("是") != -1) {
                val = 0 + 1;
            } else if (text.indexOf("在") != -1) {
                val = 0 + 1;
            }
            if (val > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            error(e);
            return false;
        }
    }

    public static String getDefaultTextEncode(String code) {
        if (code.equals("")) {
            return isAsiaLanguage ? CHARSET_AUTO : "UTF-8";
        } else {
            return code;
        }
    }

    private static void getLocaleInfo(Context context) {
        getLocaleInfo(context, false);
    }

    public static void getLocaleInfo(Context context, boolean ignoreChecked) {
        boolean z = false;
        if (appContext == null) {
            appContext = context;
        }
        if (!ignoreChecked || localeLanguage == null) {
            boolean z2;
            localeLanguage = getLocale().getLanguage();
            localeCountry = getLocale().getCountry();
            log(createAppInfo().toString());
            String lng = localeLanguage;
            isKorean = lng.toLowerCase().equals("ko");
            isJapanese = lng.toLowerCase().equals("ja");
            isChinese = lng.equals("zh");
            if (isChinese && localeCountry.toLowerCase().equals("cn")) {
                z2 = true;
            } else {
                z2 = false;
            }
            isChinesePRC = z2;
            if (lng.equals("zh") || lng.equals("ko") || lng.equals("ja")) {
                z = true;
            }
            isAsiaLanguage = z;
        }
    }

    public static StringBuilder createAppInfo() {
        StringBuilder sb = new StringBuilder();
        try {
            int ver = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode;
            String verName = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
            String license = (!isProVersion || license_state <= 0) ? "" + license_state : T.dateToStr(Long.valueOf(license_state), Locale.US);
            sb.append("*VER ").append((isProVersion ? "pro " : "std ") + verName + " (").append(runCount).append("/").append(runCountInApp).append(isProVersion ? ", " + license : "").append("), " + ver).append("\n*ROM ").append(VERSION.RELEASE).append(", ").append("" + VERSION.SDK_INT).append(", ").append(localeLanguage).append(":").append(localeCountry).append(saveMemoryLog("\n*MEM ", false)).append("\n*SCR " + getScreenHeight2() + ":" + getScreenWidth2()).append(", ").append("" + getDensity()).append(", GPU:" + hardwareAccelerated).append("\n").append(getROMInfo());
        } catch (Exception e) {
            error(e);
        }
        return sb;
    }

    public static CharSequence[] getTextEncodes() {
        if (textEncodes == null) {
            textEncodes = getContext().getResources().getTextArray(R.array.charsets);
        }
        return textEncodes;
    }

    public static String getValidedEncoding(String encoding) {
        for (CharSequence c : getTextEncodes()) {
            if (!c.toString().equals(CHARSET_AUTO) && c.toString().toLowerCase().equals(encoding.toLowerCase())) {
                return c.toString();
            }
        }
        return "UTF-8";
    }

    public static ArrayList<String> getLocalFontfaces(String path) {
        int length;
        int i = 0;
        if (localFontfaces == null) {
            localFontfaces = new ArrayList();
        } else {
            localFontfaces.clear();
        }
        for (CharSequence cs : getContext().getResources().getTextArray(R.array.font_faces)) {
            localFontfaces.add(cs.toString());
        }
        Iterator it = getAssetFonts().iterator();
        while (it.hasNext()) {
            localFontfaces.add((String) it.next());
        }
        try {
            File file;
            String filename;
            File[] all = new File(path).listFiles();
            ArrayList<String> list = new ArrayList();
            if (all != null) {
                for (File file2 : all) {
                    if (file2.isFile()) {
                        filename = file2.getName();
                        if (filename.toLowerCase().endsWith(".ttf")) {
                            list.add(filename.substring(0, filename.length() - 4));
                        }
                    }
                }
            }
            if (list.size() > 0) {
                Collections.sort(list);
                localFontfaces.addAll(list);
            }
            all = new File(SYSTEM_FONT_PATH).listFiles();
            list.clear();
            if (all != null) {
                length = all.length;
                while (i < length) {
                    file2 = all[i];
                    if (file2.isFile()) {
                        filename = file2.getName();
                        if (filename.toLowerCase().endsWith(".ttf")) {
                            list.add(filename.substring(0, filename.length() - 4));
                        }
                    }
                    i++;
                }
            }
            if (list.size() > 0) {
                Collections.sort(list);
                localFontfaces.addAll(list);
            }
        } catch (Exception e) {
            error(e);
        }
        return localFontfaces;
    }

    public static boolean permissionAllow(Activity act, String s) {
        if (VERSION.SDK_INT < 23 || act == null || ContextCompat.checkSelfPermission(act, "android.permission.GET_ACCOUNTS") == 0) {
            return true;
        }
        return false;
    }

    public static Drawable getSvgDrawable(InputStream is) {
        try {
            PictureDrawable d1 = new PictureDrawable(SVGParser.getSVGFromInputStream(is).getPicture());
            Bitmap bm = Bitmap.createBitmap(d1.getIntrinsicWidth(), d1.getIntrinsicHeight(), Config.RGB_565);
            Canvas canvas = new Canvas(bm);
            d1.setBounds(0, 0, d1.getIntrinsicWidth(), d1.getIntrinsicHeight());
            d1.draw(canvas);
            return new BitmapDrawable(getContext().getResources(), bm);
        } catch (Throwable e) {
            error(e);
            return null;
        }
    }

    public static float getParagraphRatio() {
        float ratio = (float) paragraphSpace;
        if (trimBlankSpace && ratio > 3.0f) {
            ratio = 3.0f;
        }
        return ratio / 10.0f;
    }

    public static Paint createBookmarkPaint(int padding, int drawWidth, int color) {
        boolean shouldAlpha;
        Paint p = new Paint();
        if (padding - d(1.0f) < drawWidth) {
            shouldAlpha = true;
        } else {
            shouldAlpha = false;
        }
        if (color == 0) {
            p.setAlpha(shouldAlpha ? 140 : 240);
        } else {
            p.setColorFilter(new LightingColorFilter(0, color));
            int a = Color.alpha(color);
            if (!shouldAlpha || a <= 200) {
                p.setAlpha(a);
            } else {
                p.setAlpha(200);
            }
        }
        return p;
    }

    public static boolean fixIndentBookmarkPos(Bookmark bm, String text) {
        if (bm.checkRealPosition) {
            return false;
        }
        bm.checkRealPosition = true;
        String name = bm.name;
        int i1 = name.indexOf(") ");
        if (i1 == -1 || name.length() - i1 < 10) {
            return false;
        }
        name = name.substring(i1 + 2, i1 + 10);
        int pos = (int) bm.position;
        i1 = text.indexOf(name, pos);
        if (i1 != -1 && pos - i1 <= 100) {
            return false;
        }
        int i2 = text.lastIndexOf(name, pos);
        if (i2 == -1 || pos - i2 >= 200) {
            return false;
        }
        BookDb.removeBookmark(bm);
        bm.position = (long) i2;
        BookDb.addBookmark(bm);
        return true;
    }

    public static void loadHighlightAllItems() {
        String xml = lastFile.hashCode() + "_highlight";
        highlightAllItems = new ArrayList();
        highlightAllProperties = new ArrayList();
        if (T.isFile(xml_files_folder + "/" + xml + ".xml")) {
            SharedPreferences sp = getContext().getSharedPreferences(xml, 0);
            for (String key : sp.getAll().keySet()) {
                highlightAllItems.add(key);
                highlightAllProperties.add(sp.getString(key, ""));
            }
        }
    }

    public static void updateHighlightAllItem(String key, String value) {
        getContext().getSharedPreferences(lastFile.hashCode() + "_highlight", 0).edit().putString(key, value).commit();
        txtView.postInvalidate();
        txtView2.postInvalidate();
    }

    public static void deleteHighlightAllItem(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(lastFile.hashCode() + "_highlight", 0);
        if (sp.contains(key)) {
            sp.edit().remove(key).commit();
        }
        int i = highlightAllItems.indexOf(key);
        if (i != -1) {
            highlightAllItems.remove(i);
            highlightAllProperties.remove(i);
            txtView.postInvalidate();
            txtView2.postInvalidate();
        }
    }

    private static ArrayList<MyDrawable> getInnerCoverImages() {
        if (innerCoverImages == null) {
            innerCoverImages = new ArrayList();
            addImagesFromAppResource(innerCoverImages, "cover");
        }
        return innerCoverImages;
    }

    public static ArrayList<MyDrawable> getCoverImages(boolean reset) {
        if (coverImages == null) {
            coverImages = new ArrayList();
            coverImages.addAll(getInnerCoverImages());
            addImagesFromOuterFolder(coverImages, outerCoversFolder);
        } else if (reset) {
            coverImages.clear();
            coverImages.addAll(getInnerCoverImages());
            addImagesFromOuterFolder(coverImages, outerCoversFolder);
        }
        return coverImages;
    }

    public static ArrayList<MyDrawable> getBackgroundImages(boolean reset) {
        if (backgroundImages == null) {
            backgroundImages = new ArrayList();
            addImagesFromOuterFolder(backgroundImages, outerImagesFolder);
            addImagesFromAppAssets(backgroundImages, "page");
        } else if (reset) {
            backgroundImages.clear();
            addImagesFromOuterFolder(backgroundImages, outerImagesFolder);
            addImagesFromAppAssets(backgroundImages, "page");
        }
        return backgroundImages;
    }

    private static void addImagesFromAppAssets(ArrayList<MyDrawable> images, String tag) {
        try {
            for (String filename : getContext().getAssets().list("background")) {
                MyDrawable di = new MyDrawable();
                di.imageName = T.getOnlyFilename(filename);
                di.from = 1;
                di.filename = filename;
                images.add(di);
            }
        } catch (Exception e) {
            error(e);
        }
    }

    public static void addImagesFromOuterFolder(ArrayList<MyDrawable> images, String path) {
        try {
            File[] all = new File(path).listFiles();
            if (all != null) {
                for (File file : all) {
                    if (file.isFile()) {
                        String filename = file.getName();
                        if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".gif") || filename.toLowerCase().endsWith(NETCOVER_TAG)) {
                            MyDrawable di = new MyDrawable();
                            di.imageName = filename.substring(0, filename.length() - 4);
                            di.from = 0;
                            di.filename = file.getAbsolutePath();
                            images.add(di);
                        }
                    }
                }
            }
        } catch (Exception e) {
            error(e);
        }
    }

    public static ArrayList<Integer> getCoverImageResIDs() {
        if (coverImageResIDs == null) {
            coverImageResIDs = new ArrayList();
            for (Field f : drawable.class.getFields()) {
                try {
                    if (f.getName().startsWith("cover")) {
                        coverImageResIDs.add(Integer.valueOf(f.getInt(f)));
                    }
                } catch (Exception e) {
                    error(e);
                }
            }
        }
        return coverImageResIDs;
    }

    private static Integer getRandomCoverRes() {
        getCoverImageResIDs();
        if (usedCoverRes == null) {
            usedCoverRes = new ArrayList();
        }
        if (usedCoverRes.size() == 0) {
            int size = getCoverImageResIDs().size();
            for (int i = 0; i < size; i++) {
                int j = T.myRandom(getCoverImageResIDs().size());
                while (usedCoverRes.indexOf(Integer.valueOf(j)) != -1) {
                    j = T.myRandom(getCoverImageResIDs().size());
                }
                usedCoverRes.add(Integer.valueOf(j));
            }
        }
        int id = ((Integer) usedCoverRes.get(0)).intValue();
        usedCoverRes.remove(0);
        return (Integer) getCoverImageResIDs().get(id);
    }

    private static int getCoverRes(String cover) {
        Iterator it = getInnerCoverImages().iterator();
        while (it.hasNext()) {
            MyDrawable md = (MyDrawable) it.next();
            if (md.imageName.equals(cover)) {
                return md.resourceId;
            }
        }
        return -1;
    }

    private static String getCoverName(int res) {
        Iterator it = getInnerCoverImages().iterator();
        while (it.hasNext()) {
            MyDrawable md = (MyDrawable) it.next();
            if (md.resourceId == res) {
                return md.imageName;
            }
        }
        return null;
    }

    public static void addImagesFromAppResource(ArrayList<MyDrawable> images, String tag) {
        for (Field f : drawable.class.getFields()) {
            try {
                String name = f.getName();
                if (name.startsWith(tag) && !name.equals("coverflow_base")) {
                    MyDrawable di = new MyDrawable();
                    di.imageName = name;
                    di.resourceId = f.getInt(f);
                    di.from = 2;
                    images.add(di);
                }
            } catch (IllegalArgumentException e) {
                error(e);
            } catch (IllegalAccessException e2) {
                error(e2);
            }
        }
    }

    public static Drawable getImagesDrawable(ArrayList<MyDrawable> images, String imageName, int destSize, int quality) {
        if (imageName == null || imageName.equals("")) {
            return null;
        }
        if (imageName.startsWith("/")) {
            return getFileDrawable(new File(imageName), destSize, quality);
        }
        Iterator it = images.iterator();
        while (it.hasNext()) {
            MyDrawable di = (MyDrawable) it.next();
            if (di.imageName.equals(imageName)) {
                return getDrawableFromDI(di, destSize, quality);
            }
        }
        return null;
    }

    public static Drawable getDrawableFromDI(MyDrawable di, int destSize, int quality) {
        try {
            switch (di.from) {
                case 0:
                    File file = new File(di.filename);
                    if (file.isFile()) {
                        return getFileDrawable(file, destSize, quality);
                    }
                    return null;
                case 1:
                    return getDrawableFromAsset(getContext().getAssets(), "background/" + di.filename, destSize);
                case 2:
                    if (destSize <= 0) {
                        return getContext().getResources().getDrawable(di.resourceId);
                    }
                    Options opts = new Options();
                    opts.inSampleSize = 4;
                    return new BitmapDrawable(getContext().getResources(), BitmapFactory.decodeResource(getContext().getResources(), di.resourceId, opts));
                default:
                    return null;
            }
        } catch (OutOfMemoryError e) {
            return null;
        } catch (Exception e2) {
            error(e2);
            return null;
        }
    }

    public static Drawable getDrawableFromAsset(AssetManager am, String imageFile, int destSize) {
        try {
            return new BitmapDrawable(getContext().getResources(), getStreamBitmap(am.open(imageFile), destSize, 0));
        } catch (IOException e) {
            error(e);
            return null;
        }
    }

    public static void checkTextViewProperties() {
        boolean z;
        boolean z2 = true;
        if (!textHyphenation || isAsiaLanguage) {
            z = false;
        } else {
            z = true;
        }
        textHyphenation = z;
        if (textCJK && isAsiaLanguage) {
            z = true;
        } else {
            z = false;
        }
        textCJK = z;
        if (verticalAlignment && isAsiaLanguage) {
            z = true;
        } else {
            z = false;
        }
        verticalAlignment = z;
        if (textCJK || verticalAlignment) {
            z2 = false;
        }
        textBreakOnlyAtSpaces = z2;
    }

    public static boolean checkScrollMargin() {
        if (!textCJK || cjkFontSize == fontSize) {
            return false;
        }
        setTxtScrollMargin(txtScroll);
        return true;
    }

    public static void setTxtScrollMargin(ScrollView txtScroll) {
        setTxtScrollMargin(txtScroll, true);
    }

    public static void setTxtScrollMargin(ScrollView sv, boolean checkCJK) {
        if (sv != null) {
            setMarginLimitation();
            int leftMargin2 = leftMargin;
            int rightMargin2 = rightMargin;
            if (checkCJK && textCJK) {
                int aw;
                cjkFontSize = fontSize;
                float fw = Layout.getDesiredWidth("一", txtView.getPaint());
                float size = ((float) ((getScreenWidth2() - leftMargin) - rightMargin)) / fw;
                float less = size - ((float) ((int) size));
                if (((double) less) <= 0.6d || ((double) (leftMargin + rightMargin)) <= ((double) fw) * 0.9d) {
                    aw = (int) (((((float) (leftMargin + rightMargin)) + (fw * less)) - 1.0f) / 2.0f);
                } else {
                    aw = (int) (((((float) (leftMargin + rightMargin)) - (fw * (1.0f - less))) - 1.0f) / 2.0f);
                }
                leftMargin2 = aw + 1;
                rightMargin2 = aw;
            } else {
                cjkFontSize = -1.0f;
                if (textHyphenation) {
                    float w = Layout.getDesiredWidth("?", txtView.getPaint());
                    if (((float) rightMargin2) - (w / 2.0f) > 0.0f) {
                        rightMargin2 = (int) (((float) rightMargin2) - (w / 2.0f));
                    } else if (((float) rightMargin2) - (w / 3.0f) > 0.0f) {
                        rightMargin2 = (int) (((float) rightMargin2) - (w / 3.0f));
                    }
                }
            }
            sv.setPadding(leftMargin2, topMargin, rightMargin2, bottomMargin);
            txtScroll.setFadingEdgeLength(0);
            txtScroll2.setFadingEdgeLength(0);
        }
    }

    public static void setMarginLimitation() {
        int max = d(200.0f);
        if (topMargin > max) {
            topMargin = max;
        }
        if (topMargin < 0) {
            topMargin = 0;
        }
        if (bottomMargin > max) {
            bottomMargin = max;
        }
        if (bottomMargin < 0) {
            bottomMargin = 0;
        }
        if (leftMargin > max) {
            leftMargin = max;
        }
        if (leftMargin < 0) {
            leftMargin = 0;
        }
        if (rightMargin > max) {
            rightMargin = max;
        }
        if (rightMargin < 0) {
            rightMargin = 0;
        }
    }

    public static void setTxtViewTypeface() {
        setTxtViewTypeface(txtView);
        if (landscape2PageMode && txtScroll2.getVisibility() == 0) {
            setTxtViewTypeface(txtView2);
        }
    }

    public static void setTxtViewTypeface(MRTextView tv) {
        if (tv != null) {
            if (fontName.equals("")) {
                fontName = DEFAULT_FONTFACE;
            }
            if (fontShadow) {
                tv.setShadowLayer(df(1.0f), df(0.5f), df(1.0f), isWhiteFont(fontColor) ? ViewCompat.MEASURED_STATE_MASK : -1);
            } else {
                tv.getPaint().clearShadowLayer();
            }
            tv.getPaint().setFakeBoldText(fontBold);
            tv.getPaint().setUnderlineText(fontUnderline);
            tv.getPaint().setTextSkewX(fontItalic ? -0.25f : 0.0f);
            try {
                if (forceCssFontName == null) {
                    tv.setTypeface(getTypeFace(fontName, 0));
                } else {
                    tv.setTypeface(getTypeFace2(forceCssFontName, 0));
                }
            } catch (Exception e) {
                fontName = DEFAULT_FONTFACE;
                getContext().getSharedPreferences(OPTIONS_FILE, 0).edit().putString("fontName", fontName).commit();
                error(e);
            }
        }
    }

    private static ArrayList<String> getAssetFonts() {
        if (assetFonts == null) {
            assetFonts = new ArrayList();
            try {
                for (String filename : getContext().getAssets().list("fonts")) {
                    assetFonts.add(T.getOnlyFilename(filename));
                }
            } catch (Exception e) {
                error(e);
            }
        }
        return assetFonts;
    }

    public static Typeface getTypeFace(String name, int style) {
        Typeface typeface = null;
        try {
            if (!(name.equals("monospace") || name.equals("sans-serif") || name.equals(DEFAULT_FONTFACE))) {
                if (name.startsWith("/") && T.isFile(name)) {
                    typeface = Typeface.createFromFile(name);
                } else if (getAssetFonts().indexOf(name) != -1) {
                    typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + name + ".ttf");
                } else if (T.isFile(outerFontsFolder + "/" + name + ".ttf")) {
                    typeface = Typeface.createFromFile(outerFontsFolder + "/" + name + ".ttf");
                } else if (T.isFile(SYSTEM_FONT_PATH + "/" + name + ".ttf")) {
                    typeface = Typeface.createFromFile(SYSTEM_FONT_PATH + "/" + name + ".ttf");
                } else {
                    name = DEFAULT_FONTFACE;
                }
                if (!(typeface == null || style == 0)) {
                    typeface = Typeface.create(typeface, style);
                }
            }
        } catch (Exception e) {
            log("####ERROR FONT:" + name);
            error(e);
            typeface = null;
        }
        if (typeface == null) {
            return Typeface.create(name, style);
        }
        return typeface;
    }

    public static Typeface getTypeFace2(String names, int style) {
        if (typefaceCache == null) {
            typefaceCache = new HashMap();
        } else if (typefaceCache.containsKey(names + style)) {
            Typeface tf = (Typeface) typefaceCache.get(names + style);
            if (tf != null) {
                return tf;
            }
        }
        Typeface typeface = null;
        Iterator it = getFontNames(names).iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            try {
                if (name.startsWith("/") && T.isFile(name)) {
                    typeface = Typeface.createFromFile(name);
                } else {
                    String fontFile = ebook != null ? ebook.getFontFile(name) : null;
                    if (fontFile != null && T.isFile(fontFile)) {
                        typeface = Typeface.createFromFile(fontFile);
                    } else if (name.toLowerCase().equals("monospace") || name.toLowerCase().equals("sans-serif") || name.toLowerCase().equals(DEFAULT_FONTFACE)) {
                        typeface = Typeface.create(name, style);
                    } else if (getAssetFonts().indexOf(name) != -1) {
                        typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + name + ".ttf");
                    } else if (T.isFile(outerFontsFolder + "/" + name + ".ttf")) {
                        typeface = Typeface.createFromFile(outerFontsFolder + "/" + name + ".ttf");
                    } else if (T.isFile("/sdcard/fonts/" + name + ".ttf")) {
                        typeface = Typeface.createFromFile("/sdcard/fonts/" + name + ".ttf");
                    } else if (T.isFile(SYSTEM_FONT_PATH + "/" + name + ".ttf")) {
                        typeface = Typeface.createFromFile(SYSTEM_FONT_PATH + "/" + name + ".ttf");
                    } else {
                        addToMissedFonts(name);
                    }
                    if (!(typeface == null || style == 0)) {
                        typeface = Typeface.create(typeface, style);
                    }
                }
                if (typeface != null) {
                    break;
                }
            } catch (Throwable e) {
                log("####ERROR FONT:" + name);
                error(e);
                typeface = null;
            }
        }
        if (typeface == null) {
            Typeface cur;
            if (typefaceCache.containsKey(fontName + style)) {
                cur = (Typeface) typefaceCache.get(fontName + style);
            } else {
                cur = getTypeFace(fontName, style);
                typefaceCache.put(fontName + style, cur);
            }
            typeface = cur;
        }
        typefaceCache.put(names + style, typeface);
        return typeface;
    }

    private static void addToMissedFonts(String name) {
        if (missedFonts == null) {
            missedFonts = new ArrayList();
        }
        if (missedFonts.indexOf(name) == -1) {
            missedFonts.add(name);
        }
    }

    private static ArrayList<String> getFontNames(String s) {
        ArrayList<String> list = new ArrayList();
        for (String name : s.split(",")) {
            String name2 = name2.replace(":", "").replace("'", "").replace("\"", "").replace(";", "").trim();
            if (name2.length() > 0) {
                list.add(name2);
            }
        }
        return list;
    }

    public static boolean hasRealFont(String name, int style) {
        if (typefaceCache == null || ((Typeface) typefaceCache.get(name + style)) == ((Typeface) typefaceCache.get(fontName + style))) {
            return false;
        }
        return true;
    }

    public static boolean clearTxts() {
        ebook = null;
        notes = null;
        highlights = null;
        chapters = null;
        if (txts != null) {
            txts.clear();
        }
        System.gc();
        return true;
    }

    public static boolean isLowestMemory() {
        return isLowMemory(25);
    }

    public static boolean isLowMemory(int percent) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        return (maxMemory - Runtime.getRuntime().totalMemory()) + Runtime.getRuntime().freeMemory() < (((long) percent) * maxMemory) / 100;
    }

    public static boolean lowUnusedMemory(int at_least_percent) {
        return Runtime.getRuntime().totalMemory() > (((long) (100 - at_least_percent)) * Runtime.getRuntime().maxMemory()) / 100;
    }

    public static String saveMemoryLog(String info) {
        return saveMemoryLog(info, true);
    }

    public static String saveMemoryLog(String info, boolean logIt) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long usedMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        String s = info + " " + formateSize(maxMemory) + ", " + formateSize(usedMemory) + ", " + formateSize(freeMemory) + " - " + formateSize(usedMemory - freeMemory);
        if (logIt) {
            log(s);
        }
        return s;
    }

    public static String formateSize(long size) {
        return Formatter.formatFileSize(getContext(), size);
    }

    public static void setLineSpace(MRTextView txtView2) {
        if (lineSpace < -5) {
            lineSpace = -5;
        }
        if (lineSpace > 20) {
            lineSpace = 20;
        }
        txtView2.setLineSpacing(0.0f, 1.0f + (((float) lineSpace) / 10.0f));
    }

    public static void setFontSpace(MRTextView tv) {
        if (fontSpace < -5) {
            fontSpace = -5;
        }
        if (fontSpace > 5) {
            fontSpace = 5;
        }
        float size = 1.0f + (((float) fontSpace) / 10.0f);
        if (tv != null) {
            tv.setTextScaleX(size);
        }
    }

    public static ArrayList<PageTheme> getThemeList() {
        if (themeList == null) {
            try {
                if (firstTimeInstall) {
                    extractThemesFromResource();
                }
                day_theme_name = getContext().getString(R.string.day_theme);
                night_theme_name = getContext().getString(R.string.night_theme);
                themeList = new ArrayList();
                themeList.add(getDayTheme());
                themeList.add(getNightTheme());
                File[] files = new File(xml_files_folder).listFiles();
                ArrayList<String> al = new ArrayList();
                for (File file : files) {
                    al.add(file.getName());
                }
                Collections.sort(al);
                Iterator it = al.iterator();
                while (it.hasNext()) {
                    String name = (String) it.next();
                    if (name.startsWith(THEME_FILE_TAG)) {
                        name = name.substring(THEME_FILE_TAG.length(), name.length() - 4);
                        if (!(name.equals(DAY_THEME) || name.equals(NIGHT_THEME) || name.equals(TEMP_THEME))) {
                            themeList.add(new PageTheme(name));
                        }
                    }
                }
                themeList.add(getTempTheme());
            } catch (Exception e) {
                error(e);
            }
        }
        return themeList;
    }

    private static void extractThemesFromResource() {
        try {
            File folder = new File(xml_files_folder);
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }
            if (!T.isFile(xml_files_folder + "/theme_Day Theme.xml")) {
                AssetManager am = getContext().getAssets();
                for (String filename : am.list("themes")) {
                    T.extractFileFromAsset(am, "themes/" + filename, xml_files_folder + "/" + filename);
                    copiedThemes = true;
                }
            }
        } catch (Exception e) {
            error(e);
        }
    }

    public static PageTheme getDayTheme() {
        if (dayTheme == null) {
            dayTheme = new PageTheme(DAY_THEME);
        }
        return dayTheme;
    }

    public static PageTheme getNightTheme() {
        if (nightTheme == null) {
            nightTheme = new PageTheme(NIGHT_THEME);
        }
        return nightTheme;
    }

    public static PageTheme getTempTheme() {
        if (tempTheme == null) {
            tempTheme = new PageTheme(TEMP_THEME);
        }
        return tempTheme;
    }

    public static void loadTheme(String name) {
        loadTheme(name, false);
    }

    public static void loadTheme(String name, boolean forceOnlyColor) {
        int id = getThemeId(name);
        if (id != -1) {
            PageTheme pt = (PageTheme) getThemeList().get(id);
            fontColor = pt.pFontColor;
            backgroundColor = pt.pBackgroundColor;
            backgroundImage = pt.pBackgroundImage;
            useBackgroundImage = pt.pUseBackgroundImage;
            boolean colorOnly = forceOnlyColor || loadThemeWithColorOnly || pt.emptyFontStyle;
            if (!colorOnly) {
                fontName = pt.pFontName;
                fontSize = pt.pFontSize;
                topMargin = pt.pTopMargin;
                bottomMargin = pt.pBottomMargin;
                leftMargin = pt.pLeftMargin;
                rightMargin = pt.pRightMargin;
                fontSpace = pt.pFontSpace;
                fontBold = pt.pFontBold;
                fontUnderline = pt.pFontUnderline;
                fontItalic = pt.pFontItalic;
                fontShadow = pt.pFontShadow;
            }
            lastTheme = name;
            loadVisualOptions(colorOnly);
        }
    }

    public static int getThemeHash(String name) {
        int id = getThemeId(name);
        if (id == -1) {
            return -1;
        }
        String str;
        PageTheme t = (PageTheme) getThemeList().get(id);
        StringBuilder stringBuilder = new StringBuilder();
        if (loadThemeWithColorOnly) {
            str = "";
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            str = (t.emptyFontStyle || T.isNull(t.pFontName)) ? fontName : t.pFontName;
            str = stringBuilder2.append(str).append(t.pFontSize).append(t.pFontSpace).append(t.pFontBold).append(t.pFontUnderline).append(t.pFontItalic).append(t.pFontShadow).append(t.pTopMargin).append(t.pBottomMargin).append(t.pLeftMargin).append(t.pRightMargin).toString();
        }
        return stringBuilder.append(str).append(t.pFontColor).append(getThemeBackgroundColor(id)).toString().hashCode();
    }

    public static int getCurThemeHash() {
        return ((loadThemeWithColorOnly ? "" : fontName + fontSize + fontSpace + fontBold + fontUnderline + fontItalic + fontShadow + topMargin + bottomMargin + leftMargin + rightMargin) + fontColor + getThemeBackgroundColor(-1)).hashCode();
    }

    public static boolean themeOnlyColorDiffToCur(String name) {
        int id = getThemeId(name);
        if (id == -1) {
            return false;
        }
        PageTheme t = (PageTheme) getThemeList().get(id);
        String s1 = fontName + fontSize + fontSpace + fontBold + fontUnderline + fontItalic + fontShadow + topMargin + bottomMargin + leftMargin + rightMargin;
        StringBuilder stringBuilder = new StringBuilder();
        String str = (t.emptyFontStyle || T.isNull(t.pFontName)) ? fontName : t.pFontName;
        return s1.equals(stringBuilder.append(str).append(t.pFontSize).append(t.pFontSpace).append(t.pFontBold).append(t.pFontUnderline).append(t.pFontItalic).append(t.pFontShadow).append(t.pTopMargin).append(t.pBottomMargin).append(t.pLeftMargin).append(t.pRightMargin).toString());
    }

    public static int getThemeBackgroundColor(int id) {
        if (id != -1) {
            PageTheme pt = (PageTheme) getThemeList().get(id);
            if (!pt.pUseBackgroundImage) {
                return pt.pBackgroundColor;
            }
            if (getImagesDrawable(getBackgroundImages(false), pt.pBackgroundImage, 0, 0) != null) {
                return pt.pBackgroundImage.hashCode();
            }
            return pt.pBackgroundColor;
        } else if (useBackgroundImage) {
            return backgroundImage.hashCode();
        } else {
            return backgroundColor;
        }
    }

    public static void saveTheme(String themeName, boolean overwrite) {
        int id = getThemeId(themeName);
        if (id != -1) {
            PageTheme ph = (PageTheme) getThemeList().get(id);
            ((PageTheme) getThemeList().get(id)).saveToXml(ph.pName, overwrite);
            ((PageTheme) getThemeList().get(id)).loadFromXml(ph.pName);
            return;
        }
        ph = new PageTheme(themeName);
        ph.saveToXml(themeName, overwrite);
        ph.loadFromXml(themeName);
        getThemeList().add(getThemeList().size() - 1, ph);
    }

    public static int getThemeId(String themeName) {
        for (int i = 0; i < getThemeList().size(); i++) {
            if (((PageTheme) getThemeList().get(i)).pName.equals(themeName)) {
                return i;
            }
        }
        return -1;
    }

    public static void deleteTheme(String themeName) {
        int id = getThemeId(themeName);
        if (id != -1) {
            new File(xml_files_folder + "/" + THEME_FILE_TAG + ((PageTheme) getThemeList().get(id)).pName + ".xml").delete();
            getThemeList().remove(id);
        }
    }

    public static void renameTheme(String themeName, String newName) {
        int id = getThemeId(themeName);
        if (id != -1) {
            new File(xml_files_folder + "/" + THEME_FILE_TAG + ((PageTheme) getThemeList().get(id)).pName + ".xml").delete();
            ((PageTheme) getThemeList().get(id)).pName = newName;
            ((PageTheme) getThemeList().get(id)).displayName = newName;
            ((PageTheme) getThemeList().get(id)).saveToXml(newName, false);
        }
    }

    public static void setBackgroundImage(Canvas canvas) {
        if (useBackgroundImage) {
            Drawable d = getBackgroundDrawable(backgroundImage);
            if (d != null) {
                setBackgroundDrawable(canvas, d);
            } else {
                useBackgroundImage = false;
            }
        }
        if (!useBackgroundImage) {
            canvas.drawColor(backgroundColor);
        }
    }

    public static void setBackgroundImage(View view) {
        if (view != null) {
            if (useBackgroundImage) {
                Drawable d = getBackgroundDrawable(backgroundImage);
                if (d != null) {
                    setBackgroundDrawable(view, d);
                } else {
                    useBackgroundImage = false;
                }
            }
            if (!useBackgroundImage) {
                view.setBackgroundColor(backgroundColor);
            }
        }
    }

    public static Drawable getBackgroundDrawable(String imageName) {
        if (savedBackgroundDrwable == null || !savedBackgroundDrawableName.equals(imageName)) {
            savedBackgroundDrawableName = imageName;
            savedBackgroundDrwable = getImagesDrawable(getBackgroundImages(false), imageName, 0, 0);
        }
        return savedBackgroundDrwable;
    }

    public static void setBackgroundDrawable(View view, Drawable d) {
        if (view != null) {
            if (d instanceof BitmapDrawable) {
                BitmapDrawable bd = (BitmapDrawable) d;
                if (bd.getIntrinsicWidth() < 100 && bd.getIntrinsicHeight() < 100) {
                    bd.setTileModeX(TileMode.REPEAT);
                    bd.setTileModeY(TileMode.REPEAT);
                    view.setBackgroundDrawable(bd);
                    return;
                }
            }
            view.setBackgroundDrawable(d);
        }
    }

    public static void setBackgroundDrawable(Canvas canvas, Drawable d) {
        if (d instanceof BitmapDrawable) {
            Bitmap bm = ((BitmapDrawable) d).getBitmap();
            int w = bm.getWidth();
            int h = bm.getHeight();
            int w2 = canvas.getWidth();
            int h2 = canvas.getHeight();
            if (w >= 100 || h >= 100) {
                canvas.drawBitmap(bm, new Rect(0, 0, w, h), new Rect(0, 0, w2, h2), null);
                return;
            }
            BitmapShader bs = new BitmapShader(bm, TileMode.REPEAT, TileMode.REPEAT);
            Paint p = new Paint();
            p.setShader(bs);
            canvas.drawRect(new Rect(0, 0, w2, h2), p);
            return;
        }
        d.draw(canvas);
    }

    public static void setTextFont(MRTextView tv, boolean colorOnly) {
        if (!colorOnly) {
            tv.setTextSize(fontSize);
        }
        tv.setTextColor(fontColor);
    }

    public static void loadVisualOptions(boolean colorOnly) {
        loadVisualOptions2(txtView, colorOnly);
        loadVisualOptions2(txtView2, colorOnly);
        if (txtView != null) {
            MyHtml.initFamilyFontParams();
        }
    }

    private static void loadVisualOptions2(MRTextView tv, boolean colorOnly) {
        if (tv != null) {
            try {
                setBackgroundImage(baseFrame);
                setTextFont(tv, colorOnly);
                if (!colorOnly) {
                    setTxtViewTypeface(tv);
                    setLineSpace(tv);
                    setFontSpace(tv);
                    setTxtScrollMargin(txtScroll);
                }
            } catch (Exception e) {
                error(e);
            }
        }
    }

    public static ArrayList<String> getFileList() {
        int i = 0;
        if (fileList == null) {
            fileList = new ArrayList();
        }
        fileList.clear();
        File folder = new File(lastFile.substring(0, lastFile.lastIndexOf("/")));
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            ArrayList<String> txtList = new ArrayList();
            int length = files.length;
            while (i < length) {
                File f = files[i];
                if (f.isFile()) {
                    String filename = f.getAbsolutePath();
                    if (isSupportedFile(filename)) {
                        txtList.add(filename);
                    }
                }
                i++;
            }
            Collections.sort(txtList);
            fileList.addAll(txtList);
        }
        return fileList;
    }

    public static boolean isSupportedFile(String filename) {
        String ext = filename.toLowerCase();
        return ext.endsWith(".txt") || ext.endsWith(".htm") || ext.endsWith(".html") || ext.endsWith(".epub") || ext.endsWith(".zip") || ext.endsWith(".rar") || ext.endsWith(".cbz") || ext.endsWith(".cbr") || ext.endsWith(".umd") || ext.endsWith(".pdf") || ext.endsWith(".fb2") || ext.endsWith(".mobi") || ext.endsWith(".chm");
    }

    public static boolean isReadableFile(String filename) {
        String ext = filename.toLowerCase();
        return ext.endsWith(".txt") || ext.endsWith(".htm") || ext.endsWith(".html") || ext.endsWith(".epub") || ext.endsWith(".fb2.zip") || ext.endsWith(".umd") || ext.endsWith(".pdf") || ext.endsWith(".fb2") || ext.endsWith(".mobi") || ext.endsWith(".cbz") || ext.endsWith(".cbr") || ext.endsWith(".chm");
    }

    public static void set_default_Do_Events() {
        int i;
        int i2 = 19;
        doShakePhone = 15;
        toggleTapMode = true;
        doTapScreenTop = 0;
        doTapScreenBottom = 1;
        doTapScreenLeft = 0;
        doTapScreenRight = 1;
        doSwipeLeftToRight = 15;
        doSwipeRightToLeft = 15;
        doSwipeTopToBottom = 15;
        doSwipeBottomToTop = 15;
        doVolumeKeyUp = 0;
        doVolumeKeyDown = 1;
        doDPadUp = 0;
        doDPadDown = 1;
        doDPadLeft = 12;
        doDPadRight = 13;
        doDPadCenter = 8;
        doHomeKey = 15;
        doHeadsetKey = isProVersion ? 19 : 15;
        if (!isProVersion) {
            i2 = 15;
        }
        doMediaPlayPause = i2;
        if (isProVersion) {
            i = 1;
        } else {
            i = 15;
        }
        doMediaPlayNext = i;
        if (isProVersion) {
            i = 0;
        } else {
            i = 15;
        }
        doMediaPlayPrevious = i;
        doBackKey = 15;
        doSearchKey = 4;
        doMenuKey = 15;
        doCameraKey = 15;
        doLongTap = 17;
        askForSwipeEvent = true;
    }

    public static void set_default_Margin() {
        int w = isTablet ? (getScreenWidth2() < getScreenHeight2() ? getScreenWidth2() : getScreenHeight2()) / 12 : isLargePhone ? d(18.0f) : d(10.0f);
        rightMargin = w;
        leftMargin = w;
        int d = isTablet ? d(50.0f) : d(10.0f);
        bottomMargin = d;
        topMargin = d;
        if (!isTablet) {
            topMargin = (topMargin * 13) / 8;
        }
    }

    public static void set_default_Line_Font_Space() {
        paragraphSpace = 7;
        lineSpace = 3;
        fontSpace = 0;
        fontShadow = false;
        fontBold = false;
        fontUnderline = false;
        fontItalic = false;
        textJustified = isChinese;
        textHyphenation = false;
        textCJK = isChinese;
    }

    public static void set_default_flip() {
        int i = 0;
        flip_animation = 0;
        if (!isTablet) {
            i = 4;
        }
        hori_fling_animation = i;
        flipSpeed = getROMInfo().indexOf("kindle") != -1 ? 20 : 30;
    }

    public static void set_default_statusbar() {
        statusCustomizeFont = false;
        use12Hour = false;
        statusFontSize = 10;
        statusMargin = 0;
        statusFontColor = -14540254;
        statusBackColor = 858993459;
        statusClickLeft = 6;
        statusClickMiddle = 15;
        statusClickRight = 5;
        statusClickLeft2 = 20;
        statusClickMiddle2 = 15;
        statusClickRight2 = 15;
    }

    public static void set_default_Misc() {
        boolean z;
        trimBlankSpace = false;
        indentParagraph = isChinese;
        fullscreen = true;
        keepScreenAwake = false;
        adjustBrightness = true;
        adjustNightLed = false;
        adjustLed = false;
        adjustFontSizeAtSlide = false;
        openLastFile = false;
        showStatusbar = VERSION.SDK_INT >= 14;
        keepOneLineWhenPaging = false;
        startAutoScroll = false;
        autoScrollMode = 0;
        autoScrollSpeed = 50;
        disableMove = false;
        mult_touch = false;
        allow_scroll_horizontally = true;
        if (isProVersion || inviteProVersion) {
            z = true;
        } else {
            z = false;
        }
        showSpeakButton = z;
        showOrientationButton = true;
        showOptionButton = false;
        showMiscButton = false;
        showControlButton = false;
        showVisualButton = false;
        showSearchButton = false;
        showBrightnessButton = false;
        showTiltButton = false;
        showFontSizeButton = false;
        showShutDownButton = false;
        showChapterButton = true;
        showBookmarkButton = true;
        showAutoscrollButton = true;
        showDayNightButton = true;
        needPasswordProtect = false;
        landscape2PageMode = false;
        remind1 = true;
        remind1Time = 60;
        remind1Text = getContext().getString(R.string.remind1text);
        remind2 = false;
        remind2TimeHour = 23;
        remind2TimeMinute = 30;
        remind2Text = getContext().getString(R.string.remind2text);
        chapterEndPrompt = false;
        epubThunbnail = true;
        askForScrollEvent = true;
        askForTts = true;
    }

    public static boolean isLandscape() {
        return getContext().getResources().getConfiguration().orientation == 2;
    }

    public static long getLastPosition(String s_pos) {
        int dot = s_pos.indexOf("#");
        if (dot == -1) {
            int dot2 = s_pos.indexOf(":");
            return (dot2 == -1 ? Long.valueOf(s_pos) : Long.valueOf(s_pos.substring(0, dot2))).longValue();
        }
        dot2 = s_pos.indexOf(":");
        if (dot2 == -1) {
            return Long.valueOf(s_pos.substring(dot + 1)).longValue();
        }
        return Long.valueOf(s_pos.substring(dot + 1, dot2)).longValue();
    }

    public static int getLastChapter(String s_pos) {
        int dot = s_pos.indexOf("#");
        if (dot == -1) {
            return 0;
        }
        String s = s_pos.substring(0, dot);
        int dot2 = s_pos.indexOf("@");
        if (dot2 == -1) {
            return Integer.valueOf(s).intValue();
        }
        return Integer.valueOf(s.substring(0, dot2)).intValue();
    }

    public static int getLastSplitIndex(String s_pos) {
        int dot1 = s_pos.indexOf("@");
        int dot2 = s_pos.indexOf("#");
        if (dot1 == -1 || dot2 == -1) {
            return 0;
        }
        return Integer.valueOf(s_pos.substring(dot1 + 1, dot2)).intValue();
    }

    public static void getLastPositionAndChapterFromString(String s_pos) {
        lastPosition = getLastPosition(s_pos);
        if (lastPosition < 0) {
            lastPosition = 0;
        }
        lastChapter = getLastChapter(s_pos);
        lastSplitIndex = getLastSplitIndex(s_pos);
    }

    public static ArrayList<Bookmarks> getBookmarks() {
        return getBookmarks(false);
    }

    public static ArrayList<Bookmarks> getBookmarks(boolean forceRefresh) {
        if (bookmarksList == null || forceRefresh) {
            bookmarksList = BookDb.getBookmarks();
        }
        return bookmarksList;
    }

    public static int getBookmarksId(String filename) {
        filename = filename.toLowerCase();
        for (int i = 0; i < getBookmarks().size(); i++) {
            if (((Bookmarks) getBookmarks().get(i)).filename.toLowerCase().equals(filename)) {
                return i;
            }
        }
        return -1;
    }

    public static Bookmarks getBookmarksOfOneFile(String filename) {
        int id = getBookmarksId(filename);
        if (id != -1) {
            return (Bookmarks) getBookmarks().get(id);
        }
        return null;
    }

    public static ArrayList<TxTChapter> getTxtChapters() {
        if (chapters == null) {
            int i;
            chapters = new ArrayList();
            int maxChapterLen = isAsiaLanguage ? 80 : 100;
            String tag = (textCJK || isAsiaLanguage) ? "第" : "chapter";
            for (i = 0; i < txts.size(); i++) {
                String s = textCJK ? (String) txts.get(i) : ((String) txts.get(i)).toLowerCase();
                int from = 0;
                while (true) {
                    int index = s.indexOf(tag, from);
                    String title = "";
                    if (index == -1) {
                        break;
                    }
                    int lineEnd;
                    String text;
                    int lineBegin = s.lastIndexOf("\n", index);
                    int _r = s.indexOf("\r", index);
                    int _n = s.indexOf("\n", index);
                    if (_r < _n) {
                        lineEnd = _r;
                    } else {
                        lineEnd = _n;
                    }
                    String chapterPrior = "";
                    String chapterNext = "";
                    if (lineBegin == -1) {
                        if (i <= 0) {
                            if (lineEnd == -1) {
                                break;
                            }
                            title = ((String) txts.get(i)).substring(0, lineEnd);
                        } else {
                            if (textCJK) {
                                text = (String) txts.get(i - 1);
                            } else {
                                text = ((String) txts.get(i - 1)).toLowerCase();
                            }
                            int lineBegin2 = text.lastIndexOf("\n");
                            if (lineBegin2 == -1) {
                                break;
                            }
                            chapterPrior = ((String) txts.get(i - 1)).substring(lineBegin2 + 1);
                        }
                    }
                    if (lineEnd == -1) {
                        lineEnd = s.indexOf("\n", index);
                        if (lineEnd == -1) {
                            if (i < txts.size() - 1) {
                                if (textCJK) {
                                    text = (String) txts.get(i + 1);
                                } else {
                                    text = ((String) txts.get(i + 1)).toLowerCase();
                                }
                                int lineEnd2 = text.indexOf("\r");
                                if (lineEnd2 == -1) {
                                    break;
                                }
                                chapterNext = ((String) txts.get(i + 1)).substring(0, lineEnd2);
                            } else {
                                break;
                            }
                        }
                    }
                    if (index <= 0) {
                        if (lineEnd == -1) {
                            break;
                        } else if (chapterPrior.length() == 0 || isPreChapterTag(chapterPrior.charAt(chapterPrior.length() - 1))) {
                            title = ((String) txts.get(i)).substring(0, lineEnd);
                        }
                    } else if (isPreChapterTag(s.charAt(index - 1))) {
                        if (lineBegin != -1 && lineEnd != -1) {
                            title = ((String) txts.get(i)).substring(lineBegin + 1, lineEnd);
                        } else if (lineBegin != -1 || lineEnd == -1) {
                            if (lineBegin == -1 || lineEnd != -1) {
                                break;
                            }
                            title = ((String) txts.get(i)).substring(lineBegin) + chapterNext;
                        } else {
                            title = chapterPrior + ((String) txts.get(i)).substring(0, lineEnd);
                        }
                    }
                    if (title.length() >= 3) {
                        boolean ok = false;
                        String title_trim = trimChapterTitle1(title);
                        if (textCJK || isChinese) {
                            if (chapterLengthOk(title_trim) && chineseChapterTagOk(title_trim)) {
                                ok = true;
                            }
                        } else if (title_trim.length() < maxChapterLen && title_trim.toLowerCase().indexOf(tag) < title_trim.length() / 4) {
                            ok = true;
                        }
                        if (ok) {
                            TxTChapter chapter = new TxTChapter(title_trim, title, (long) (getPriorTxtLength(i) + index));
                            if (chapters.size() == 0 || !((TxTChapter) chapters.get(chapters.size() - 1)).chapter_trim.equals(chapter.chapter_trim)) {
                                chapters.add(chapter);
                            }
                        }
                    }
                    if (lineEnd != -1) {
                        from = lineEnd + 2;
                    } else {
                        from = index + 5;
                    }
                }
            }
            if (!textCJK && chapters.size() > 2) {
                int l = 0;
                Iterator it = chapters.iterator();
                while (it.hasNext()) {
                    l += ((TxTChapter) it.next()).chapter_trim.length();
                }
                l /= chapters.size();
                for (i = chapters.size() - 1; i >= 0; i--) {
                    if (((TxTChapter) chapters.get(i)).chapter_trim.length() > l * 2) {
                        chapters.remove(i);
                    }
                }
            }
        }
        return chapters;
    }

    public static boolean isPreChapterTag(char prior) {
        return prior == INDENT_CHAR || prior == ' ' || prior == '\t' || prior == '\n' || prior == '【';
    }

    public static boolean chineseChapterTagOk(String s) {
        int i1 = s.indexOf("第");
        if (s.length() > 18) {
            int i2 = s.indexOf(" ");
            if (i2 == -1 || i2 - i1 > 15) {
                return false;
            }
        }
        if (i1 == -1) {
            return false;
        }
        if (hasChapterTag(s, i1, "卷") || hasChapterTag(s, i1, "章") || hasChapterTag(s, i1, "回") || hasChapterTag(s, i1, "集") || hasChapterTag(s, i1, "話") || hasChapterTag(s, i1, "节") || hasChapterTag(s, i1, "折")) {
            return true;
        }
        return false;
    }

    private static boolean hasChapterTag(String s, int i1, String tag) {
        int i2 = s.indexOf(tag, i1);
        return i2 > i1 && i2 - i1 < 9;
    }

    private static String trimChapterTitle1(String title) {
        title = title.replace(INDENT_CHAR, ' ').replace("\t", " ");
        while (title.length() > 3 && (isBlankChar(title.charAt(0)) || title.charAt(0) == '\n' || title.charAt(0) == '\r')) {
            title = title.substring(1);
        }
        int i = title.indexOf("\n");
        if (i > 3) {
            title = title.substring(0, i);
        }
        return title.trim();
    }

    private static String trimChapterTitle2(String title) {
        return deleteChapterComment(deleteChapterComment(deleteChapterComment(title, "[", "]"), "(", ")"), "（", "）").trim();
    }

    private static String deleteChapterComment(String title, String tag1, String tag2) {
        int i1 = title.indexOf(tag1);
        if (i1 <= 3 || title.indexOf(tag2) <= i1 + 5) {
            return title;
        }
        return title.substring(0, i1);
    }

    private static String trimChapterTitle3(String s) {
        s = getTxtDisplayText(s);
        while (true) {
            if (!s.startsWith(" ") && !s.startsWith("　") && !s.startsWith("\t") && !s.startsWith("\n") && !s.startsWith("\r")) {
                break;
            }
            s = s.substring(1);
        }
        while (true) {
            if (!s.endsWith(" ") && !s.endsWith("　")) {
                return s;
            }
            s = s.substring(0, s.length() - 1);
        }
    }

    public static boolean chapterLengthOk(String s) {
        if (s.length() < 3) {
            return false;
        }
        if (s.length() < 40) {
            return true;
        }
        if (s.length() >= 80) {
            return false;
        }
        if (s.endsWith("]") || s.endsWith(")") || s.endsWith("）")) {
            return true;
        }
        return false;
    }

    public static int getChapterId(long position) {
        for (int i = 1; i < getTxtChapters().size(); i++) {
            if (((TxTChapter) getTxtChapters().get(i)).position > ((long) (trimBlankSpace ? HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES : 50)) + position) {
                return i - 1;
            }
        }
        return 0;
    }

    public static long getTxtChapterDisplayPosition(int chapterId) {
        if (chapterId < 0 || chapterId > getTxtChapters().size() - 1) {
            return 0;
        }
        int iBlock = (int) (((TxTChapter) getTxtChapters().get(chapterId)).position / ((long) fixedBlockLength));
        String chapter_trim = trimChapterTitle3(((TxTChapter) getTxtChapters().get(chapterId)).chapter);
        int index = getTxts2(iBlock).indexOf(chapter_trim);
        if (index == -1 && chapter_trim.length() > 20) {
            index = getTxts2(iBlock).indexOf(chapter_trim.substring(0, 20));
        }
        if (index == -1) {
            index = getTxts2(iBlock).length() - 100;
        }
        return iBlock > 0 ? (long) (getPriorTxtLength(iBlock) + index) : (long) index;
    }

    public static ArrayList<String> getHistory() {
        if (historyFiles == null) {
            try {
                String fileText = T.getFileText(xml_files_folder + "/" + HISTORY_FILE);
                if (fileText == null) {
                    fileText = "";
                }
                historyFiles = T.text2StringList(fileText);
                if (T.isFolder(default_book_folder)) {
                    int i = historyFiles.size() - 1;
                    while (i >= 0) {
                        if (((String) historyFiles.get(i)).indexOf("?") == -1 && !T.isFile((String) historyFiles.get(i))) {
                            historyFiles.remove(i);
                        }
                        i--;
                    }
                }
                if (historyFiles.size() == 0 && firstTimeInstall) {
                    createInitHistory();
                }
            } catch (OutOfMemoryError e) {
            } catch (Exception e2) {
                error(e2);
            }
        }
        if (historyFiles.size() > 0) {
            first_history_file = (String) historyFiles.get(0);
        }
        return historyFiles;
    }

    private static void createInitHistory() {
        int i = 0;
        try {
            AssetManager am = getContext().getAssets();
            try {
                String lang;
                String tag = isChinese ? isChinesePRC ? "cn" : "hk" : null;
                if (tag == null) {
                    for (String lang2 : am.list("book")) {
                        lang2 = T.getOnlyFilename(lang2);
                        if (lang2.toLowerCase().equals(localeCountry.toLowerCase())) {
                            tag = lang2;
                            break;
                        }
                    }
                }
                if (tag == null) {
                    String[] list = am.list("book");
                    int length = list.length;
                    while (i < length) {
                        lang2 = T.getOnlyFilename(list[i]);
                        if (lang2.toLowerCase().equals(localeLanguage.toLowerCase())) {
                            tag = lang2;
                            break;
                        }
                        i++;
                    }
                }
                if (tag == null) {
                    tag = "en";
                }
                String filename = "Alice_" + tag + ".epub";
                String toFile = download_saved_path + "/" + filename;
                String coverFile = download_cache_path + "/" + filename + THUMB_TAG;
                if (T.isNull(lastFile)) {
                    lastFile = toFile;
                }
                if (!T.isFile(toFile)) {
                    T.inputStream2File(am.open("book/" + tag), toFile);
                }
                if (!T.isFile(coverFile)) {
                    T.inputStream2File(am.open("book/cover.jpg"), coverFile);
                }
                historyFiles.add(toFile);
                if (BookDb.getBook(toFile) == null) {
                    BookDb.createBookInfoFromFile("", toFile, true);
                }
            } catch (IOException e) {
                error(e);
            }
            saveHistory();
        } catch (Exception e2) {
            error(e2);
        }
    }

    public static int getHistoryId(String filename) {
        if (getHistory() == null) {
            return -1;
        }
        for (int i = 0; i < getHistory().size(); i++) {
            if (((String) getHistory().get(i)).equals(filename)) {
                return i;
            }
        }
        return -1;
    }

    public static void deleteHistory(String filename) {
        int id = getHistoryId(filename);
        if (id != -1) {
            getHistory().remove(id);
            saveHistory();
        }
    }

    public static void addHistory(String filename) {
        try {
            if (isSupportedFile(filename)) {
                int id = getHistoryId(filename);
                if (id != -1) {
                    getHistory().remove(id);
                }
                getHistory().add(0, filename);
                saveHistory();
            }
        } catch (Exception e) {
            error(e);
            System.exit(0);
        }
    }

    public static void saveHistory() {
        try {
            T.saveFileText(xml_files_folder + "/" + HISTORY_FILE, T.stringList2Text(historyFiles));
        } catch (Exception e) {
            error(e);
        }
    }

    public static void checkIfLoadedOptions(Context context) {
        if (textEncode == null) {
            LoadOptions(context);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap getBytesBitmap(byte[] r5, int r6, int r7) {
        /*
        r0 = 0;
        r2 = new android.graphics.BitmapFactory$Options;	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        r2.<init>();	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        r3 = 1;
        r2.inJustDecodeBounds = r3;	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        r3 = 0;
        r4 = r5.length;	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        android.graphics.BitmapFactory.decodeByteArray(r5, r3, r4, r2);	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        r3 = computeSampleSize(r2, r6, r7);	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        r2.inSampleSize = r3;	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        r3 = 0;
        r2.inJustDecodeBounds = r3;	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        r3 = 0;
        r4 = r5.length;	 Catch:{ OutOfMemoryError -> 0x001e, Exception -> 0x0034 }
        r0 = android.graphics.BitmapFactory.decodeByteArray(r5, r3, r4, r2);	 Catch:{ OutOfMemoryError -> 0x001e, Exception -> 0x0034 }
    L_0x001d:
        return r0;
    L_0x001e:
        r1 = move-exception;
        error(r1);	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        r3 = r2.inSampleSize;	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        r3 = r3 + 1;
        r2.inSampleSize = r3;	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        r3 = 0;
        r4 = r5.length;	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        r0 = android.graphics.BitmapFactory.decodeByteArray(r5, r3, r4, r2);	 Catch:{ OutOfMemoryError -> 0x002f, Exception -> 0x0034 }
        goto L_0x001d;
    L_0x002f:
        r1 = move-exception;
        java.lang.System.gc();
        goto L_0x001d;
    L_0x0034:
        r1 = move-exception;
        error(r1);
        goto L_0x001d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.books.A.getBytesBitmap(byte[], int, int):android.graphics.Bitmap");
    }

    public static Rect getBytesBitmapBounds(byte[] bytes) {
        try {
            Options opts = new Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            int scale = computeSampleSize(opts, 0, 0);
            return new Rect(0, 0, opts.outWidth / scale, opts.outHeight / scale);
        } catch (OutOfMemoryError e) {
        } catch (Exception e2) {
            error(e2);
        }
        return null;
    }

    public static Bitmap getStreamBitmap(InputStream is, int destSize, int quality) {
        try {
            Options opts = new Options();
            if (is.markSupported()) {
                opts.inJustDecodeBounds = true;
                is.mark(is.available());
                BitmapFactory.decodeStream(is, null, opts);
                is.reset();
                opts.inSampleSize = computeSampleSize(opts, destSize, quality);
                opts.inJustDecodeBounds = false;
            } else {
                byte[] bytes = T.InputStream2Byte(is);
                if (bytes != null) {
                    return getBytesBitmap(bytes, destSize, quality);
                }
            }
            return BitmapFactory.decodeStream(is, null, opts);
        } catch (OutOfMemoryError e) {
            System.gc();
            return null;
        } catch (Exception e2) {
            error(e2);
            return null;
        }
    }

    public static Rect getStreamBitmapBounds(InputStream is) {
        try {
            Options opts = new Options();
            if (is.markSupported()) {
                opts.inJustDecodeBounds = true;
                is.mark(is.available());
                BitmapFactory.decodeStream(is, null, opts);
                is.reset();
                int scale = computeSampleSize(opts, 0, 0);
                return new Rect(0, 0, opts.outWidth / scale, opts.outHeight / scale);
            }
            byte[] bytes = T.InputStream2Byte(is);
            if (bytes != null) {
                return getBytesBitmapBounds(bytes);
            }
            return null;
        } catch (OutOfMemoryError e) {
        } catch (Exception e2) {
            error(e2);
        }
    }

    public static Drawable getFileDrawable(File file, int destSize, int quality) {
        return getFileDrawable(getContext(), file, destSize, quality);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.drawable.Drawable getFileDrawable(android.content.Context r11, java.io.File r12, int r13, int r14) {
        /*
        r7 = 0;
        r8 = r12.isFile();
        if (r8 == 0) goto L_0x006d;
    L_0x0007:
        r8 = r12.getName();	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r8 = r8.toLowerCase();	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r9 = ".svg";
        r8 = r8.endsWith(r9);	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        if (r8 == 0) goto L_0x0025;
    L_0x0017:
        r4 = new java.io.FileInputStream;	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r8 = r12.getAbsolutePath();	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r4.<init>(r8);	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r0 = getSvgDrawable(r4);	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
    L_0x0024:
        return r0;
    L_0x0025:
        r5 = new android.graphics.BitmapFactory$Options;	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r5.<init>();	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r8 = r11.getContentResolver();	 Catch:{ Exception -> 0x0066, OutOfMemoryError -> 0x0069 }
        r9 = android.net.Uri.fromFile(r12);	 Catch:{ Exception -> 0x0066, OutOfMemoryError -> 0x0069 }
        r10 = "r";
        r6 = r8.openFileDescriptor(r9, r10);	 Catch:{ Exception -> 0x0066, OutOfMemoryError -> 0x0069 }
        r3 = r6.getFileDescriptor();	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r8 = 1;
        r5.inJustDecodeBounds = r8;	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r8 = 0;
        android.graphics.BitmapFactory.decodeFileDescriptor(r3, r8, r5);	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r8 = computeSampleSize(r5, r13, r14);	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r5.inSampleSize = r8;	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r8 = 0;
        r5.inJustDecodeBounds = r8;	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r0 = new android.graphics.drawable.BitmapDrawable;	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r8 = r11.getResources();	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r9 = r12.getAbsolutePath();	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r9 = android.graphics.BitmapFactory.decodeFile(r9, r5);	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r0.<init>(r8, r9);	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r8 = r0.getIntrinsicWidth();	 Catch:{ OutOfMemoryError -> 0x0069, Exception -> 0x006f }
        r9 = -1;
        if (r8 != r9) goto L_0x0024;
    L_0x0064:
        r0 = r7;
        goto L_0x0024;
    L_0x0066:
        r2 = move-exception;
        r0 = r7;
        goto L_0x0024;
    L_0x0069:
        r1 = move-exception;
        java.lang.System.gc();
    L_0x006d:
        r0 = r7;
        goto L_0x0024;
    L_0x006f:
        r1 = move-exception;
        error(r1);
        goto L_0x006d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.books.A.getFileDrawable(android.content.Context, java.io.File, int, int):android.graphics.drawable.Drawable");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap getFileBitmap(android.content.Context r10, java.io.File r11, int r12, int r13) {
        /*
        r6 = 0;
        r7 = r11.isFile();
        if (r7 == 0) goto L_0x0028;
    L_0x0007:
        r7 = r11.getName();	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r7 = r7.toLowerCase();	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r8 = ".svg";
        r7 = r7.endsWith(r8);	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        if (r7 == 0) goto L_0x0029;
    L_0x0017:
        r3 = new java.io.FileInputStream;	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r7 = r11.getAbsolutePath();	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r3.<init>(r7);	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r7 = getSvgDrawable(r3);	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r6 = com.flyersoft.books.T.drawableToBitmap(r7);	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
    L_0x0028:
        return r6;
    L_0x0029:
        r4 = new android.graphics.BitmapFactory$Options;	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r4.<init>();	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r7 = r10.getContentResolver();	 Catch:{ Exception -> 0x0059, OutOfMemoryError -> 0x005b }
        r8 = android.net.Uri.fromFile(r11);	 Catch:{ Exception -> 0x0059, OutOfMemoryError -> 0x005b }
        r9 = "r";
        r5 = r7.openFileDescriptor(r8, r9);	 Catch:{ Exception -> 0x0059, OutOfMemoryError -> 0x005b }
        r2 = r5.getFileDescriptor();	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r7 = 1;
        r4.inJustDecodeBounds = r7;	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r7 = 0;
        android.graphics.BitmapFactory.decodeFileDescriptor(r2, r7, r4);	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r7 = computeSampleSize(r4, r12, r13);	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r4.inSampleSize = r7;	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r7 = 0;
        r4.inJustDecodeBounds = r7;	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r7 = r11.getAbsolutePath();	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        r6 = android.graphics.BitmapFactory.decodeFile(r7, r4);	 Catch:{ OutOfMemoryError -> 0x005b, Exception -> 0x0060 }
        goto L_0x0028;
    L_0x0059:
        r1 = move-exception;
        goto L_0x0028;
    L_0x005b:
        r0 = move-exception;
        java.lang.System.gc();
        goto L_0x0028;
    L_0x0060:
        r0 = move-exception;
        error(r0);
        goto L_0x0028;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.books.A.getFileBitmap(android.content.Context, java.io.File, int, int):android.graphics.Bitmap");
    }

    public static int computeSampleSize(Options opts, int destSize, int quality) {
        int size;
        int target;
        if (destSize > 0) {
            target = destSize == 2 ? d(60.0f) : d((float) shelfCoverSize);
            if (quality > 0) {
                target = (target * 2) / (quality + 2);
            }
            size = opts.outWidth / target;
        } else {
            target = getScreenWidth2();
            if (quality > 0) {
                target = (target * 2) / (quality + 2);
            }
            size = opts.outWidth / target;
            if (destSize == -1) {
                size--;
            }
        }
        return size > 0 ? size : 1;
    }

    public static void renameThumbFile(String oldName, String newName) {
        oldName = T.getFilename(oldName);
        newName = T.getFilename(newName);
        if (!oldName.equals(newName)) {
            String filename = download_cache_path + "/" + oldName + EDITCOVER_TAG;
            if (T.isFile(filename)) {
                T.renameFile(filename, download_cache_path + "/" + newName + EDITCOVER_TAG, true);
            }
            filename = download_cache_path + "/" + oldName + THUMB_TAG;
            if (T.isFile(filename)) {
                T.renameFile(filename, download_cache_path + "/" + newName + THUMB_TAG, true);
            }
            filename = download_cache_path + "/" + oldName + COVER_TAG;
            if (T.isFile(filename)) {
                T.renameFile(filename, download_cache_path + "/" + newName + COVER_TAG, true);
            }
        }
    }

    public static String getBookThumbFile(String filename) {
        filename = T.getFilename(filename);
        File file = new File(download_cache_path + "/" + filename + EDITCOVER_TAG);
        if (!file.isFile() || file.length() < 10) {
            file = new File(download_cache_path + "/" + filename + THUMB_TAG);
        }
        if (!file.isFile() || file.length() < 10) {
            file = new File(download_cache_path + "/" + filename + COVER_TAG);
        }
        if (!file.isFile() || file.length() < 10) {
            file = new File(download_cache_path + "/" + filename + NETCOVER_TAG);
        }
        return file.isFile() ? file.getAbsolutePath() : "";
    }

    public static Drawable getBookCover(String filename) {
        return getFileDrawable(new File(getBookCoverFile(filename)), 0, 1);
    }

    public static String getBookCoverFile(String filename) {
        filename = T.getFilename(filename);
        File file = new File(download_cache_path + "/" + filename + COVER_TAG);
        if (!file.isFile() || file.length() < 10) {
            file = new File(download_cache_path + "/" + filename + EDITCOVER_TAG);
        }
        if (!file.isFile() || file.length() < 10) {
            file = new File(download_cache_path + "/" + filename + THUMB_TAG);
        }
        if (!file.isFile() || file.length() < 10) {
            file = new File(download_cache_path + "/" + filename + NETCOVER_TAG);
        }
        return file.isFile() ? file.getAbsolutePath() : "";
    }

    public static String getShelfBestCoverFile(String filename) {
        filename = T.getFilename(filename);
        File file = new File(download_cache_path + "/" + filename + EDITCOVER_TAG);
        if (!file.isFile() || file.length() < 10) {
            file = new File(download_cache_path + "/" + filename + COVER_TAG);
        }
        return file.isFile() ? file.getAbsolutePath() : "";
    }

    public static Drawable getShelfCover(String filename) {
        return getFileDrawable(new File(getShelfCoverFile(filename)), 0, 0);
    }

    public static String getShelfCoverFile(String filename) {
        return getShelfCoverFile(BookDb.getDb(), filename, true);
    }

    public static String getShelfCoverFile(SQLiteDatabase db, String filename, boolean createIfNoExist) {
        filename = T.getFilename(BaseCompressor.getZRCacheFile(filename, true));
        File file = new File(download_cache_path + "/" + filename + EDITCOVER_TAG);
        if (!file.isFile() || file.length() < 10) {
            file = new File(download_cache_path + "/" + filename + THUMB_TAG);
        }
        if (!file.isFile() || file.length() < 10) {
            file = new File(download_cache_path + "/" + filename + COVER_TAG);
        }
        if (!file.isFile() || file.length() < 10) {
            file = new File(download_cache_path + "/" + filename + NETCOVER_TAG);
        }
        if (file.isFile()) {
            return file.getAbsolutePath();
        }
        if (db == null) {
            return "" + getRandomCoverRes();
        }
        String cover = BookDb.getSavedRandomBookCover(db, filename);
        if (cover == null && !createIfNoExist) {
            return "" + getRandomCoverRes();
        }
        if (cover != null && getCoverRes(cover) != -1) {
            return "" + getCoverRes(cover);
        }
        int res = getRandomCoverRes().intValue();
        BookDb.insertRandomBookCover(filename, getCoverName(res));
        return "" + res;
    }

    public static int getFileType() {
        return getFileType(lastFile);
    }

    public static int getFileType(String filename) {
        String ext = filename.toLowerCase();
        if (ext.endsWith(".txt")) {
            return 0;
        }
        if (ext.endsWith(".html") || ext.endsWith(".htm")) {
            return 1;
        }
        if (ext.endsWith(".epub")) {
            return 2;
        }
        if (ext.endsWith(".fb2") || ext.endsWith(".fb2.zip")) {
            return 5;
        }
        if (ext.endsWith(".zip")) {
            return 3;
        }
        if (ext.endsWith(".rar")) {
            return 9;
        }
        if (ext.endsWith(".cbz")) {
            return 10;
        }
        if (ext.endsWith(".cbr")) {
            return 11;
        }
        if (ext.endsWith(".umd")) {
            return 4;
        }
        if (ext.endsWith(".chm")) {
            return 6;
        }
        if (ext.endsWith(".pdf")) {
            return 7;
        }
        if (ext.endsWith(".mobi")) {
            return 8;
        }
        return 0;
    }

    public static int getFileIcon(String filename) {
        if (!isSupportedFile(filename)) {
            return R.drawable.aiunknow;
        }
        switch (getFileType(filename)) {
            case 0:
                return R.drawable.aitxt;
            case 1:
                return R.drawable.aihtml;
            case 2:
                return R.drawable.aiepub;
            case 3:
                return R.drawable.aizip;
            case 4:
                return R.drawable.aiumd;
            case 5:
                return R.drawable.aifb2;
            case 6:
                return R.drawable.aichm;
            case 7:
                return R.drawable.aipdf;
            case 8:
                return R.drawable.aimobi;
            case 9:
                return R.drawable.airar;
            case 10:
                return R.drawable.aicbz;
            case 11:
                return R.drawable.aicbr;
            default:
                return R.drawable.aitxt;
        }
    }

    public static int getBookType() {
        return getBookType(lastFile);
    }

    public static int getBookType(String filename) {
        switch (getFileType(filename)) {
            case 1:
                return 1;
            case 2:
            case 4:
            case 5:
            case 6:
            case 8:
                return 100;
            case 7:
                return 7;
            default:
                return 0;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void loadEBook(java.lang.String r3) {
        /*
        r1 = 0;
        ebook = r1;
        r1 = getFileType();	 Catch:{ Exception -> 0x001a }
        switch(r1) {
            case 2: goto L_0x000e;
            case 3: goto L_0x000a;
            case 4: goto L_0x0027;
            case 5: goto L_0x001f;
            case 6: goto L_0x002f;
            case 7: goto L_0x000a;
            case 8: goto L_0x0037;
            default: goto L_0x000a;
        };
    L_0x000a:
        java.lang.System.gc();
        return;
    L_0x000e:
        r1 = "UTF-8";
        fileEncoding = r1;	 Catch:{ Exception -> 0x001a }
        r1 = new com.flyersoft.books.Epub;	 Catch:{ Exception -> 0x001a }
        r1.<init>(r3);	 Catch:{ Exception -> 0x001a }
        ebook = r1;	 Catch:{ Exception -> 0x001a }
        goto L_0x000a;
    L_0x001a:
        r0 = move-exception;
        error(r0);
        goto L_0x000a;
    L_0x001f:
        r1 = new com.flyersoft.books.Fb2;	 Catch:{ Exception -> 0x001a }
        r1.<init>(r3);	 Catch:{ Exception -> 0x001a }
        ebook = r1;	 Catch:{ Exception -> 0x001a }
        goto L_0x000a;
    L_0x0027:
        r1 = new com.flyersoft.books.Umd;	 Catch:{ Exception -> 0x001a }
        r1.<init>(r3);	 Catch:{ Exception -> 0x001a }
        ebook = r1;	 Catch:{ Exception -> 0x001a }
        goto L_0x000a;
    L_0x002f:
        r1 = new com.flyersoft.books.Chm;	 Catch:{ Exception -> 0x001a }
        r1.<init>(r3);	 Catch:{ Exception -> 0x001a }
        ebook = r1;	 Catch:{ Exception -> 0x001a }
        goto L_0x000a;
    L_0x0037:
        r1 = new com.flyersoft.books.Mobi;	 Catch:{ Exception -> 0x001a }
        r2 = getContext();	 Catch:{ Exception -> 0x001a }
        r1.<init>(r2, r3);	 Catch:{ Exception -> 0x001a }
        ebook = r1;	 Catch:{ Exception -> 0x001a }
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.books.A.loadEBook(java.lang.String):void");
    }

    public static boolean isEBookOK() {
        return ebook != null && ebook.isInited();
    }

    public static String getVerionName() {
        try {
            return getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
        } catch (Exception e) {
            error(e);
            return "";
        }
    }

    public static String getAboutText(boolean englishOnly) {
        int i = 0;
        String filename = "en";
        if (!(englishOnly || localeCountry.equals(filename) || localeLanguage.equals(filename))) {
            AssetManager am = getContext().getAssets();
            boolean found = false;
            try {
                String lang;
                for (String lang2 : am.list("readme")) {
                    lang2 = T.getOnlyFilename(lang2);
                    if (lang2.toLowerCase().equals(localeCountry.toLowerCase())) {
                        filename = lang2;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    String[] list = am.list("readme");
                    int length = list.length;
                    while (i < length) {
                        lang2 = T.getOnlyFilename(list[i]);
                        if (lang2.toLowerCase().equals(localeLanguage.toLowerCase())) {
                            filename = lang2;
                            found = true;
                            break;
                        }
                        i++;
                    }
                }
                if (!found && localeLanguage.equals("zh")) {
                    filename = "CN";
                }
            } catch (IOException e) {
                error(e);
            }
        }
        try {
            return T.inputStream2String(getContext().getAssets().open("readme/" + filename + ".htm")) + "<br><br>";
        } catch (Exception e2) {
            error(e2);
            return getContext().getString(R.string.app_name) + " " + versionTag + "<br><br>";
        }
    }

    public static String getDecryptText(String text) {
        if (text != null) {
            try {
                if (text.length() != 0) {
                    if (VERSION.SDK_INT < 17) {
                        return SimpleCrypto.decrypt("moon", text);
                    }
                    if (text.length() <= 20) {
                        return T.mySimpleDecript(text);
                    }
                    needPasswordProtect = false;
                    return "";
                }
            } catch (Exception e) {
                error(e);
                return "";
            }
        }
        return "";
    }

    public static String getEncryptText(String text) {
        if (text != null) {
            try {
                if (text.length() != 0) {
                    return VERSION.SDK_INT >= 17 ? T.mySimpleEncript(text) : SimpleCrypto.encrypt("moon", text);
                }
            } catch (Exception e) {
                error(e);
                return "";
            }
        }
        return "";
    }

    public static boolean isFlipNone() {
        return flip_animation == 0 || flip_animation > 7;
    }

    public static boolean isFlipHorizShift() {
        return flip_animation == 3 || flip_animation == 6;
    }

    public static boolean isFlipVerticalShift() {
        return flip_animation == 4 || flip_animation == 7;
    }

    public static boolean isFlipCurl() {
        return flip_animation == 1 || flip_animation == 2;
    }

    public static boolean isHoriFlipCurl() {
        return hori_fling_animation == 1 || hori_fling_animation == 2;
    }

    public static boolean toggleTapModeForFlip() {
        return flip_animation == 1 || flip_animation == 2 || flip_animation == 3 || flip_animation == 6;
    }

    public static boolean noSplitHtmls() {
        return splitHtmls.size() == 0;
    }

    public static String adjustChapterHtml(String html) {
        return adjustChapterHtml(html, splitHtmls);
    }

    public static String adjustChapterHtml(String html, ArrayList<String> htmls) {
        if (maxHtmlChapterSize == 0) {
            maxHtmlChapterSize = DEAULT_MAX_HTML_CHAPTER_SIZE;
        }
        if (html.length() > maxHtmlChapterSize) {
            createSplitHtmls(html, htmls);
            String result = deal_html_file((String) htmls.get(0));
            if (htmls.size() != 1) {
                return result;
            }
            htmls.clear();
            return result;
        }
        htmls.clear();
        return cleanHtml(deal_html_file(html));
    }

    private static String deal_html_file(String html) {
        return getFileType() != 1 ? html : T.deleteHtmlStyle(T.getHtmlBody(html));
    }

    public static String cleanHtml(String html) {
        try {
            html = html.replace("­", "");
            if (isHtmlContent()) {
                if (trimBlankSpace) {
                    html = html.replaceAll(">(\n|\r\n)*", ">").replaceAll("(\n|\r\n)*<", "<");
                }
                return deleteEndBreak(html);
            } else if (trimBlankSpace) {
                return doTrimBlankSpace(html);
            } else {
                return html;
            }
        } catch (OutOfMemoryError e) {
            return html;
        }
    }

    public static String deleteEndBreak(String html) {
        if (html.length() <= DEAULT_MAX_HTML_CHAPTER_SIZE) {
            html = html.trim();
            while (true) {
                if (!html.endsWith("<br>")) {
                    if (!html.endsWith("　")) {
                        if (!html.endsWith(" ")) {
                            break;
                        }
                        html = html.substring(0, html.length() - 1);
                    } else {
                        html = html.substring(0, html.length() - 1);
                    }
                } else {
                    html = html.substring(0, html.length() - 4);
                }
            }
        }
        return html;
    }

    public static String trimHtml(String html) {
        try {
            html = html.replaceAll("(?i)<div .*?>|</p>|<div>", "").replaceAll("(?i)<p.*?>|<br.*?>|<div.*?>|</div>", "<br>").replaceAll("(?i)( |　|\r|\n|&#160;|&#13;| )*<br>( |　| )*", "<br>").replaceAll("(?i)(<br>)+", "$1").replaceAll("(</h.>)<br>", "$1");
        } catch (Exception e) {
            error(e);
        }
        return html;
    }

    public static void createSplitHtmls(String html, ArrayList<String> htmls) {
        try {
            int i;
            htmls.clear();
            int l = html.length();
            int p1 = 0;
            int p2;
            String s;
            if (!isHtmlContent()) {
                p2 = maxHtmlChapterSize + 1;
                while (true) {
                    if (p2 > l) {
                        p2 = l;
                    }
                    s = html.substring(p1, p2);
                    if (html.length() < 3000000 && !isLowestMemory()) {
                        s = cleanHtml(s);
                    }
                    htmls.add(s);
                    if (p2 >= l) {
                        break;
                    }
                    p1 = p2;
                    p2 += maxHtmlChapterSize;
                }
            } else {
                int p = maxHtmlChapterSize;
                while (true) {
                    if (p > l - 1) {
                        p2 = l;
                    } else {
                        p2 = html.indexOf("<p", p);
                        if (p2 == -1) {
                            p2 = html.indexOf("<P", p);
                        }
                        if (p2 == -1) {
                            p2 = html.indexOf("<div", p);
                        }
                        if (p2 == -1) {
                            p2 = html.indexOf("<DIV", p);
                        }
                        if (p2 == -1 || p2 > (maxHtmlChapterSize / 5) + p) {
                            p2 = html.indexOf(">", p);
                            if (p2 != -1) {
                                p2++;
                            } else {
                                p2 = l;
                            }
                        }
                    }
                    s = html.substring(p1, p2);
                    if (s.length() > maxHtmlChapterSize * 5) {
                        s = s.substring(0, maxHtmlChapterSize * 2);
                    }
                    if (!isLowestMemory()) {
                        s = cleanHtml(s);
                    }
                    if (s.length() < 200) {
                        String tmp = T.html2Text(s);
                        boolean delete = true;
                        for (i = 0; i < tmp.length(); i++) {
                            if (!isEmptyChar(tmp.charAt(i))) {
                                delete = false;
                                break;
                            }
                        }
                        if (delete) {
                            s = null;
                        }
                    }
                    if (s != null) {
                        htmls.add(s);
                    }
                    if (p2 >= l) {
                        break;
                    }
                    p1 = p2;
                    p = p1 + maxHtmlChapterSize;
                }
            }
            if (html.length() < 2000000) {
                int size = htmls.size();
                for (i = 0; i < size - 1; i++) {
                    htmls.set(i, ((String) htmls.get(i)) + "<br/><span align=\"right\"><font color=#6060EE>[" + (i + 1) + "/" + size + "]</font></span>");
                }
            }
        } catch (OutOfMemoryError e) {
            error(e);
        }
    }

    public static boolean isHtmlContent() {
        if (getBookType() == 1) {
            return true;
        }
        if (getBookType() == 100 && ebook != null && ebook.isHtml()) {
            return true;
        }
        return false;
    }

    public static long getFilesize() {
        File file = new File(lastFile);
        if (file.isFile()) {
            return file.length();
        }
        return 0;
    }

    public static String getFileSavedEncode(String filename) {
        String encode;
        try {
            encode = getContext().getSharedPreferences(ENCODING_FILE, 0).getString(filename, "xxx");
        } catch (Exception e) {
            error(e);
            encode = "xxx";
        }
        if (!encode.equals("xxx") && !encode.equals(CHARSET_AUTO)) {
            return encode;
        }
        encode = "UTF-8";
        if (!textEncode.equals(CHARSET_AUTO)) {
            return textEncode;
        }
        try {
            encode = getFileEncode(filename);
        } catch (Exception e2) {
            error(e2);
            encode = "";
        }
        if (!encode.equals("")) {
            return encode;
        }
        if (localeCountry.equals("CN")) {
            return "GB2312";
        }
        if (localeCountry.equals("TW") || localeCountry.equals("HK")) {
            return "BIG5";
        }
        if (localeCountry.equals("KR") || localeLanguage.equals("ko")) {
            return "EUC-KR";
        }
        if (localeCountry.equals("JP") || localeLanguage.equals("ja")) {
            return "SHIFT-JIS";
        }
        if (localeLanguage.equals("uk") || localeLanguage.equals("ru")) {
            return "Windows-1251";
        }
        return "UTF-8";
    }

    public static Locale getLanguageLocale(int id) {
        String lang = null;
        Locale locale = getLocale();
        switch (id) {
            case 0:
                return locale;
            case 1:
                lang = "en";
                break;
            case 2:
                lang = "am";
                break;
            case 3:
                lang = "ar";
                break;
            case 4:
                lang = "hy";
                break;
            case 5:
                lang = "be";
                break;
            case 6:
                lang = "bg";
                break;
            case 7:
                lang = "ca";
                break;
            case 8:
                lang = "hr";
                break;
            case 9:
                lang = "cs";
                break;
            case 10:
                lang = "da";
                break;
            case 11:
                lang = "nl";
                break;
            case 12:
                lang = "et";
                break;
            case 13:
                lang = "fi";
                break;
            case 14:
                lang = "fr";
                break;
            case 15:
                lang = "gl";
                break;
            case 16:
                lang = "ka";
                break;
            case 17:
                lang = "de";
                break;
            case 18:
                lang = "el";
                break;
            case 19:
                lang = "he";
                break;
            case 20:
                lang = "hu";
                break;
            case 21:
                lang = "in";
                break;
            case 22:
                lang = "it";
                break;
            case 23:
                lang = "ja";
                break;
            case 24:
                lang = "ko";
                break;
            case 25:
                lang = "mk";
                break;
            case 26:
                lang = "fa";
                break;
            case 27:
                lang = "pl";
                break;
            case 28:
                lang = "pt";
                break;
            case 29:
                localeLanguage = "pt";
                localeCountry = "BR";
                locale = new Locale(localeLanguage, localeCountry);
                break;
            case 30:
                lang = "ro";
                break;
            case 31:
                lang = "ru";
                break;
            case 32:
                lang = "sr";
                break;
            case 33:
                localeLanguage = "zh";
                localeCountry = "CN";
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case 34:
                lang = "si";
                break;
            case 35:
                lang = "sk";
                break;
            case 36:
                lang = "sl";
                break;
            case 37:
                lang = "es";
                break;
            case 38:
                lang = "sv";
                break;
            case 39:
                localeLanguage = "zh";
                localeCountry = "TW";
                locale = Locale.TRADITIONAL_CHINESE;
                break;
            case 40:
                lang = "tt";
                break;
            case 41:
                lang = "th";
                break;
            case 42:
                lang = "tr";
                break;
            case 43:
                lang = "uk";
                break;
            case 44:
                lang = "vi";
                break;
        }
        if (lang != null) {
            localeLanguage = lang;
            localeCountry = lang;
            locale = new Locale(lang);
        }
        log("*******LNG(2) " + localeLanguage + ":" + localeCountry);
        return locale;
    }

    public static void setLanguage(Context context) {
        if (appContext == null) {
            appContext = context;
        }
        if (languageID != 0) {
            try {
                Locale lc = getLanguageLocale(languageID);
                Resources res = getContext().getResources();
                Configuration config = res.getConfiguration();
                config.locale = lc;
                res.updateConfiguration(config, res.getDisplayMetrics());
                getLocaleInfo(getContext());
            } catch (Exception e) {
                error(e);
            }
        }
    }

    public static float getDensity() {
        if (getContext() == null) {
            return Resources.getSystem().getDisplayMetrics().density;
        }
        return getContext().getResources().getDisplayMetrics().density;
    }

    public static boolean isHighResulotionTablet() {
        return isTablet && getDensity() > 1.0f;
    }

    public static boolean isSuperTablet() {
        return isTablet && ((double) getDensity()) > 1.5d;
    }

    public static String getBookName() {
        if (getBookType() == 100 && ebook != null) {
            return ebook.getBookName();
        }
        if (getBookType() != 7 || T.isNull(ActivityTxt.selfPref) || T.isNull(ActivityTxt.selfPref.pdf_title)) {
            return T.getFilename(lastFile);
        }
        return ActivityTxt.selfPref.pdf_title;
    }

    public static int getTranslationIndex(String sourceLang) {
        if (sourceLang.equals("zh")) {
            sourceLang = sourceLang + "-" + localeCountry;
        }
        String[] transList = getContext().getResources().getStringArray(R.array.translations_abbr);
        for (int i = 0; i < transList.length; i++) {
            if (transList[i].equals(sourceLang)) {
                return i;
            }
        }
        return 0;
    }

    public static String getTranslationAbbr(int index) {
        String[] transList = getContext().getResources().getStringArray(R.array.translations_abbr);
        if (index < 0 || index >= transList.length) {
            return "en";
        }
        return transList[index];
    }

    public static String getDictUrl(int index) {
        if (index == -1) {
            return TRANSLATION_URL;
        }
        try {
            String[] list = getContext().getResources().getStringArray(R.array.dict_list_url);
            return (index < 0 || index >= list.length) ? list[list.length - 2] : list[index];
        } catch (Exception e) {
            error(e);
            return DICTIONARY_URL;
        }
    }

    public static ArrayList<NoteInfo> getNotes() {
        checkNotesHighlights(false);
        return notes;
    }

    public static void resetHighlights() {
        if (highlights != null) {
            Iterator it = highlights.iterator();
            while (it.hasNext()) {
                ((NoteInfo) it.next()).checkRealPosition = false;
            }
        }
    }

    public static ArrayList<NoteInfo> getHighlights() {
        checkNotesHighlights(false);
        return highlights;
    }

    public static void checkNotesHighlights(boolean foreRefresh) {
        if (foreRefresh || notes == null) {
            notes = new ArrayList();
            highlights = new ArrayList();
            BookDb.getNotesHighlights(lastFile, notes, highlights);
        }
    }

    public static void addNote(NoteInfo note) {
        if (note != null) {
            if (!getHighlights().contains(note)) {
                getHighlights().add(note);
            }
            if (!(T.isNull(note.note) || getNotes().contains(note))) {
                getNotes().add(note);
            }
            BookDb.insertNote(note);
            hasNewNoteForUpload = true;
        }
    }

    public static boolean removeNote(NoteInfo note) {
        if (note == null) {
            return false;
        }
        getHighlights().remove(note);
        getNotes().remove(note);
        hasNewNoteForUpload = true;
        if (deletedNotes == null) {
            deletedNotes = new ArrayList();
        }
        deletedNotes.add(note);
        return BookDb.removeNote(note);
    }

    public static void refreshNotes() {
        log("==========refresh highlights/notes:" + getHighlights().size());
        BookDb.deleteNotes(lastFile, false, false);
        for (int i = getHighlights().size() - 1; i > 0; i--) {
            NoteInfo n1 = (NoteInfo) highlights.get(i);
            for (int j = i - 1; j >= 0; j--) {
                NoteInfo n2 = (NoteInfo) highlights.get(j);
                if (n1.lastPosition == n2.lastPosition && n1.lastChapter == n2.lastChapter && n1.lastSplitIndex == n2.lastSplitIndex && n1.highlightLength == n2.highlightLength) {
                    log("======>>>>> remove duplicated:" + n1.original);
                    highlights.remove(i);
                }
            }
        }
        Iterator it = getHighlights().iterator();
        while (it.hasNext()) {
            BookDb.insertNote((NoteInfo) it.next());
        }
    }

    public static boolean updateNote(NoteInfo note) {
        if (note == null) {
            return false;
        }
        boolean result = BookDb.removeNote(note);
        BookDb.insertNote(note);
        hasNewNoteForUpload = true;
        return result;
    }

    public static NoteInfo getPreHighlight(int offset) {
        long real = getBookType() == 0 ? getTxtRealPos((long) offset) : (long) offset;
        Iterator it = getHighlights().iterator();
        while (it.hasNext()) {
            NoteInfo note = (NoteInfo) it.next();
            if (lastChapter == note.lastChapter && lastSplitIndex == note.lastSplitIndex && real >= note.lastPosition && real <= note.lastPosition + ((long) note.highlightLength)) {
                return note;
            }
        }
        return null;
    }

    public static void sortNotes(ArrayList<NoteInfo> notes) {
        try {
            final boolean isPdf = getBookType() == 7;
            Collections.sort(notes, new Comparator<Object>() {
                public int compare(Object object1, Object object2) {
                    NoteInfo n1 = (NoteInfo) object1;
                    NoteInfo n2 = (NoteInfo) object2;
                    if (A.bookmarkSortByDate) {
                        if (n1.time < n2.time) {
                            return 1;
                        }
                        return -1;
                    } else if (isPdf) {
                        if (n1.lastPosition == n2.lastPosition) {
                            if (n1.lastChapter <= n2.lastChapter) {
                                return -1;
                            }
                            return 1;
                        } else if (n1.lastPosition <= n2.lastPosition) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else if (n1.lastChapter != n2.lastChapter) {
                        if (n1.lastChapter <= n2.lastChapter) {
                            return -1;
                        }
                        return 1;
                    } else if (n1.lastSplitIndex != n2.lastSplitIndex) {
                        if (n1.lastSplitIndex <= n2.lastSplitIndex) {
                            return -1;
                        }
                        return 1;
                    } else if (n1.lastPosition <= n2.lastPosition) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        } catch (Exception e) {
            error(e);
        }
    }

    public static void sortBookmarks(Bookmarks bm) {
        try {
            Collections.sort(bm.list, new Comparator<Object>() {
                public int compare(Object object1, Object object2) {
                    Bookmark n1 = (Bookmark) object1;
                    Bookmark n2 = (Bookmark) object2;
                    if (A.bookmarkSortByDate) {
                        if (n1.time < n2.time) {
                            return 1;
                        }
                        return -1;
                    } else if (n1.chapter != n2.chapter) {
                        if (n1.chapter <= n2.chapter) {
                            return -1;
                        }
                        return 1;
                    } else if (n1.splitIndex != n2.splitIndex) {
                        if (n1.splitIndex <= n2.splitIndex) {
                            return -1;
                        }
                        return 1;
                    } else if (n1.position <= n2.position) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        } catch (Exception e) {
            error(e);
        }
    }

    public static boolean canShowAds() {
        return getContext().getPackageName().endsWith("r");
    }

    public static int getDimColor(int color, float dim) {
        int r = Color.red(color);
        return Color.argb(Color.alpha(color), (int) (((float) r) * dim), (int) (((float) Color.green(color)) * dim), (int) (((float) Color.blue(color)) * dim));
    }

    public static int getAlphaColor(int color, int alpha) {
        if (alpha != 0) {
            try {
                int a = Color.alpha(color) + alpha;
                if (a > 255) {
                    a = 255;
                }
                if (a < 50) {
                    a = 50;
                }
                color = Color.argb(a, Color.red(color), Color.green(color), Color.blue(color));
            } catch (Exception e) {
                error(e);
            }
        }
        return color;
    }

    public static int deleteAlphaOfColor(int color) {
        return Color.argb(255, Color.red(color), Color.green(color), Color.blue(color));
    }

    public static boolean isWhiteFont(int color) {
        return colorValue(color) > 355;
    }

    public static int colorValue(int color) {
        return (Color.red(color) + Color.green(color)) + Color.blue(color);
    }

    public static String getHyphenationLang() {
        return hyphenationIndex(hyphenationLang) != -1 ? hyphenationLang : "en";
    }

    public static int hyphenationIndex(String lang) {
        CharSequence[] cs = getContext().getResources().getTextArray(R.array.hyphenation_code);
        for (int i = 0; i < cs.length; i++) {
            if (cs[i].toString().equals(lang)) {
                return i;
            }
        }
        return -1;
    }

    public static void setHyphenation() {
        if (textHyphenation) {
            SHTextHyphenator.Instance().load(getHyphenationLang(), getContext().getAssets());
        }
    }

    public static void initTTSFilters() {
        if (ttsFilters == null) {
            ttsFilters = new ArrayList();
            String filter_file = xml_files_folder + "/tts_filter";
            if (T.isFile(filter_file)) {
                String text = T.getFileText(filter_file);
                while (true) {
                    int n = text.indexOf("\n");
                    if (n != -1) {
                        String s = text.substring(0, n);
                        text = text.substring(n + 1);
                        int i = s.indexOf("#->#");
                        if (i > 0) {
                            ttsFilters.add(new TTS_Filter_Item(s.substring(0, i), s.substring(i + 4)));
                        }
                    } else {
                        return;
                    }
                }
            }
            ttsFilters.add(new TTS_Filter_Item("—", ", "));
            ttsFilters.add(new TTS_Filter_Item("-", " "));
            ttsFilters.add(new TTS_Filter_Item(" '", " "));
            ttsFilters.add(new TTS_Filter_Item("' ", " "));
            saveTTSFilterToFile();
        }
    }

    public static void saveAutoImportFoldersToFile() {
        T.saveFileText(xml_files_folder + "/auto_import_folders", T.stringList2Text(getAutoImportFolders()));
    }

    public static ArrayList<String> getAutoImportFolders() {
        if (auto_folders == null) {
            String xml = xml_files_folder + "/auto_import_folders";
            if (T.isFile(xml)) {
                auto_folders = T.text2StringList(T.getFileText(xml));
            } else {
                auto_folders = T.text2StringList(default_book_folder);
            }
            for (int i = auto_folders.size() - 1; i >= 0; i--) {
                if (!T.isFolder((String) auto_folders.get(i))) {
                    auto_folders.remove(i);
                }
            }
        }
        return auto_folders;
    }

    public static void saveTTSFilterToFile() {
        if (ttsFilters != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ttsFilters.size(); i++) {
                if (((TTS_Filter_Item) ttsFilters.get(i)).original.length() > 0) {
                    sb.append(((TTS_Filter_Item) ttsFilters.get(i)).original + "#->#" + ((TTS_Filter_Item) ttsFilters.get(i)).replaceWith + "\n");
                }
            }
            T.saveFileText(xml_files_folder + "/tts_filter", sb.toString());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.ArrayList<com.flyersoft.books.A.TtsLine> getTtsLines(java.lang.String r23, boolean r24, boolean r25, int r26) {
        /*
        if (r23 != 0) goto L_0x0004;
    L_0x0002:
        r15 = 0;
    L_0x0003:
        return r15;
    L_0x0004:
        r2 = getContext();
        r6 = 2131165318; // 0x7f070086 float:1.794485E38 double:1.052935569E-314;
        r2 = r2.getString(r6);
        r0 = r23;
        r21 = r0.indexOf(r2);
        r2 = -1;
        r0 = r21;
        if (r0 == r2) goto L_0x0023;
    L_0x001a:
        r2 = 0;
        r0 = r23;
        r1 = r21;
        r23 = r0.substring(r2, r1);
    L_0x0023:
        r2 = "Mr.";
        r6 = "#1^";
        r0 = r23;
        r2 = r0.replace(r2, r6);
        r6 = "Mrs.";
        r7 = "#11^";
        r2 = r2.replace(r6, r7);
        r6 = "Dr.";
        r7 = "#2^";
        r23 = r2.replace(r6, r7);
        r15 = new java.util.ArrayList;
        r15.<init>();
        r13 = -1;
        if (r24 == 0) goto L_0x0049;
    L_0x0045:
        r23 = formatPdfTtsText(r23);	 Catch:{ Exception -> 0x0071 }
    L_0x0049:
        r18 = "　 ,.;?:!，。；：？、！\t\n\r";
        r19 = "　 ”\t\n\r";
        r22 = tts_divide;	 Catch:{ Exception -> 0x0071 }
        if (r25 == 0) goto L_0x0058;
    L_0x0051:
        r2 = 3;
        r0 = r22;
        if (r0 != r2) goto L_0x0058;
    L_0x0056:
        r22 = 1;
    L_0x0058:
        switch(r22) {
            case 0: goto L_0x0093;
            case 1: goto L_0x00d5;
            case 2: goto L_0x00d8;
            default: goto L_0x005b;
        };	 Catch:{ Exception -> 0x0071 }
    L_0x005b:
        r2 = new com.flyersoft.books.A$TtsLine;	 Catch:{ Exception -> 0x0071 }
        r4 = 0;
        r5 = r23.length();	 Catch:{ Exception -> 0x0071 }
        r3 = r23;
        r6 = r23;
        r7 = r24;
        r8 = r26;
        r2.<init>(r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x0071 }
        addTtsLine(r15, r2);	 Catch:{ Exception -> 0x0071 }
        goto L_0x0003;
    L_0x0071:
        r14 = move-exception;
        r8 = r13;
    L_0x0073:
        error(r14);
    L_0x0076:
        r2 = r15.size();
        if (r2 != 0) goto L_0x0119;
    L_0x007c:
        r6 = new com.flyersoft.books.A$TtsLine;
        r8 = 0;
        r9 = r23.length();
        r7 = r23;
        r10 = r23;
        r11 = r24;
        r12 = r26;
        r6.<init>(r7, r8, r9, r10, r11, r12);
        addTtsLine(r15, r6);
        goto L_0x0003;
    L_0x0093:
        r18 = "　 ,.;?\"':\\(\\){}[]「」『』!，。；＂”“：？（）、！\t\n\r";
        r19 = "　 ,.;:\\(\\){}[]「」『』，。；＂”“：（）、'\"\t\n\r";
        r20 = "\\(|\\)|：|:|,|;|!|\\. |\\?|，|。|；|？|！|\t|\n|\r";
    L_0x0099:
        r17 = java.util.regex.Pattern.compile(r20);	 Catch:{ Exception -> 0x0071 }
        r0 = r17;
        r1 = r23;
        r16 = r0.matcher(r1);	 Catch:{ Exception -> 0x0071 }
        r4 = 0;
        r5 = -1;
        r8 = r13;
    L_0x00a8:
        r2 = r16.find();	 Catch:{ Exception -> 0x013c }
        if (r2 == 0) goto L_0x0076;
    L_0x00ae:
        r5 = r16.end();	 Catch:{ Exception -> 0x013c }
        r13 = r5;
        r0 = r23;
        r3 = r0.substring(r4, r5);	 Catch:{ Exception -> 0x0071 }
    L_0x00b9:
        r2 = r3.length();	 Catch:{ Exception -> 0x0071 }
        if (r2 <= 0) goto L_0x00db;
    L_0x00bf:
        r2 = 0;
        r2 = r3.charAt(r2);	 Catch:{ Exception -> 0x0071 }
        r0 = r18;
        r2 = r0.indexOf(r2);	 Catch:{ Exception -> 0x0071 }
        r6 = -1;
        if (r2 == r6) goto L_0x00db;
    L_0x00cd:
        r4 = r4 + 1;
        r2 = 1;
        r3 = r3.substring(r2);	 Catch:{ Exception -> 0x0071 }
        goto L_0x00b9;
    L_0x00d5:
        r20 = ";|!|\\. |\\?|。|；|？|！|\t|\n|\r";
        goto L_0x0099;
    L_0x00d8:
        r20 = "\n|\r";
        goto L_0x0099;
    L_0x00db:
        r2 = r3.length();	 Catch:{ Exception -> 0x0071 }
        if (r2 <= 0) goto L_0x0102;
    L_0x00e1:
        r2 = r3.length();	 Catch:{ Exception -> 0x0071 }
        r2 = r2 + -1;
        r2 = r3.charAt(r2);	 Catch:{ Exception -> 0x0071 }
        r0 = r19;
        r2 = r0.indexOf(r2);	 Catch:{ Exception -> 0x0071 }
        r6 = -1;
        if (r2 == r6) goto L_0x0102;
    L_0x00f4:
        r5 = r5 + -1;
        r2 = 0;
        r6 = r3.length();	 Catch:{ Exception -> 0x0071 }
        r6 = r6 + -1;
        r3 = r3.substring(r2, r6);	 Catch:{ Exception -> 0x0071 }
        goto L_0x00db;
    L_0x0102:
        r2 = r3.length();	 Catch:{ Exception -> 0x0071 }
        if (r2 <= 0) goto L_0x0116;
    L_0x0108:
        r2 = new com.flyersoft.books.A$TtsLine;	 Catch:{ Exception -> 0x0071 }
        r6 = r23;
        r7 = r24;
        r8 = r26;
        r2.<init>(r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x0071 }
        addTtsLine(r15, r2);	 Catch:{ Exception -> 0x0071 }
    L_0x0116:
        r4 = r5;
        r8 = r13;
        goto L_0x00a8;
    L_0x0119:
        r2 = -1;
        if (r8 == r2) goto L_0x0003;
    L_0x011c:
        r2 = r23.length();
        if (r8 >= r2) goto L_0x0003;
    L_0x0122:
        r6 = new com.flyersoft.books.A$TtsLine;
        r0 = r23;
        r7 = r0.substring(r8);
        r9 = r23.length();
        r10 = r23;
        r11 = r24;
        r12 = r26;
        r6.<init>(r7, r8, r9, r10, r11, r12);
        addTtsLine(r15, r6);
        goto L_0x0003;
    L_0x013c:
        r14 = move-exception;
        goto L_0x0073;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.books.A.getTtsLines(java.lang.String, boolean, boolean, int):java.util.ArrayList<com.flyersoft.books.A$TtsLine>");
    }

    public static String formatPdfTtsText(String text) {
        text = text.replace("\r", "☀");
        int index = 1;
        while (true) {
            int n = text.indexOf("\n", index);
            if (n == -1) {
                return text.replaceAll("\n", "☀").replaceAll("✔", "\n");
            }
            boolean hasR;
            char c = text.charAt(n - 1);
            if (c != '☀' || n <= 1) {
                hasR = false;
            } else {
                hasR = true;
            }
            if (hasR) {
                c = text.charAt(n - 2);
            }
            if (c == '-') {
                text = text.substring(0, hasR ? n - 2 : n - 1) + (hasR ? "☀☀☀" : "☀☀") + text.substring(n + 1);
            } else if (",.;?:!，。；：？、！".indexOf(c) != -1) {
                int i;
                StringBuilder stringBuilder = new StringBuilder();
                if (hasR) {
                    i = n - 1;
                } else {
                    i = n;
                }
                text = stringBuilder.append(text.substring(0, i)).append(hasR ? " ☀" : " ").append(text.substring(n + 1)).toString();
            } else if (isBlankChar(c) || Character.getType(c) == 5) {
                text = text.substring(0, n) + "☀" + text.substring(n + 1);
            } else {
                text = text.substring(0, n) + ' ' + text.substring(n + 1);
            }
            index = n + 1;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String formatPdfReflowText(java.lang.String r20, com.radaee.pdf.Page r21) {
        /*
        if (r21 != 0) goto L_0x0003;
    L_0x0002:
        return r20;
    L_0x0003:
        r7 = new java.util.ArrayList;
        r7.<init>();
        r21.ObjsStart();
        r12 = 1;
    L_0x000c:
        r17 = "\n";
        r0 = r20;
        r1 = r17;
        r10 = r0.indexOf(r1, r12);
        r17 = r20.length();
        r17 = r17 + -1;
        r0 = r17;
        if (r10 != r0) goto L_0x007e;
    L_0x0020:
        r9 = 0;
        r8 = 0;
        r11 = 0;
        r17 = r7.size();
        if (r17 <= 0) goto L_0x00de;
    L_0x0029:
        r4 = 0;
    L_0x002a:
        r17 = r7.size();
        r0 = r17;
        if (r4 >= r0) goto L_0x00d1;
    L_0x0032:
        r15 = r7.get(r4);
        r15 = (float[]) r15;
        r17 = 2;
        r17 = r15[r17];
        r17 = (r17 > r9 ? 1 : (r17 == r9 ? 0 : -1));
        if (r17 <= 0) goto L_0x0044;
    L_0x0040:
        r17 = 2;
        r9 = r15[r17];
    L_0x0044:
        r17 = 3;
        r17 = r15[r17];
        r18 = 1;
        r18 = r15[r18];
        r17 = r17 - r18;
        r8 = r8 + r17;
        r17 = r7.size();
        r17 = r17 + -1;
        r0 = r17;
        if (r4 >= r0) goto L_0x007b;
    L_0x005a:
        r17 = r4 + 1;
        r0 = r17;
        r16 = r7.get(r0);
        r16 = (float[]) r16;
        r17 = 1;
        r17 = r15[r17];
        r18 = 3;
        r18 = r16[r18];
        r17 = (r17 > r18 ? 1 : (r17 == r18 ? 0 : -1));
        if (r17 <= 0) goto L_0x00cf;
    L_0x0070:
        r17 = 1;
        r17 = r15[r17];
        r18 = 3;
        r18 = r16[r18];
        r11 = r17 - r18;
    L_0x007a:
        r8 = r8 + r11;
    L_0x007b:
        r4 = r4 + 1;
        goto L_0x002a;
    L_0x007e:
        r17 = -1;
        r0 = r17;
        if (r10 != r0) goto L_0x00a1;
    L_0x0084:
        r17 = r7.size();
        if (r17 <= 0) goto L_0x0020;
    L_0x008a:
        r17 = r20.length();
        r0 = r17;
        if (r12 >= r0) goto L_0x0020;
    L_0x0092:
        r17 = 4;
        r0 = r17;
        r13 = new float[r0];
        r0 = r21;
        r0.ObjsGetCharRect(r12, r13);
        r7.add(r13);
        goto L_0x0020;
    L_0x00a1:
        r4 = r10 + -1;
        r0 = r20;
        r17 = r0.charAt(r4);
        r18 = 13;
        r0 = r17;
        r1 = r18;
        if (r0 != r1) goto L_0x00b9;
    L_0x00b1:
        r17 = 1;
        r0 = r17;
        if (r10 <= r0) goto L_0x00b9;
    L_0x00b7:
        r4 = r10 + -2;
    L_0x00b9:
        r17 = 4;
        r0 = r17;
        r13 = new float[r0];
        r17 = r4 + -1;
        r0 = r21;
        r1 = r17;
        r0.ObjsGetCharRect(r1, r13);
        r7.add(r13);
        r12 = r10 + 1;
        goto L_0x000c;
    L_0x00cf:
        r11 = 0;
        goto L_0x007a;
    L_0x00d1:
        r17 = r8 + r11;
        r18 = r7.size();
        r0 = r18;
        r0 = (float) r0;
        r18 = r0;
        r8 = r17 / r18;
    L_0x00de:
        r17 = "\r";
        r18 = "";
        r0 = r20;
        r1 = r17;
        r2 = r18;
        r20 = r0.replace(r1, r2);
        r12 = 1;
        r6 = 0;
    L_0x00ee:
        r17 = "\n";
        r0 = r20;
        r1 = r17;
        r10 = r0.indexOf(r1, r12);
        r17 = -1;
        r0 = r17;
        if (r10 == r0) goto L_0x0112;
    L_0x00fe:
        r17 = r20.length();
        r17 = r17 + -1;
        r0 = r17;
        if (r10 == r0) goto L_0x0112;
    L_0x0108:
        r17 = r7.size();
        r17 = r17 + -1;
        r0 = r17;
        if (r6 < r0) goto L_0x013e;
    L_0x0112:
        r17 = "☀";
        r18 = "";
        r0 = r20;
        r1 = r17;
        r2 = r18;
        r20 = r0.replaceAll(r1, r2);
        r17 = "☆";
        r18 = "\n✔";
        r0 = r20;
        r1 = r17;
        r2 = r18;
        r20 = r0.replaceAll(r1, r2);
        r17 = "✔";
        r18 = "\n";
        r0 = r20;
        r1 = r17;
        r2 = r18;
        r20 = r0.replaceAll(r1, r2);
        goto L_0x0002;
    L_0x013e:
        r4 = r10 + -1;
        r0 = r20;
        r3 = r0.charAt(r4);
        r13 = r7.get(r6);
        r13 = (float[]) r13;
        r17 = r6 + 1;
        r0 = r17;
        r14 = r7.get(r0);
        r14 = (float[]) r14;
        r17 = r7.size();
        r18 = 10;
        r0 = r17;
        r1 = r18;
        if (r0 <= r1) goto L_0x01b6;
    L_0x0162:
        r17 = 2;
        r17 = r13[r17];
        r18 = 1119092736; // 0x42b40000 float:90.0 double:5.529052754E-315;
        r18 = r18 * r9;
        r19 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r18 = r18 / r19;
        r17 = (r17 > r18 ? 1 : (r17 == r18 ? 0 : -1));
        if (r17 <= 0) goto L_0x01b6;
    L_0x0172:
        r5 = 1;
    L_0x0173:
        r17 = 3;
        r17 = r13[r17];
        r18 = 3;
        r18 = r14[r18];
        r17 = r17 - r18;
        r18 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r18 = r18 + r8;
        r17 = (r17 > r18 ? 1 : (r17 == r18 ? 0 : -1));
        if (r17 <= 0) goto L_0x01b8;
    L_0x0185:
        r17 = new java.lang.StringBuilder;
        r17.<init>();
        r18 = 0;
        r0 = r20;
        r1 = r18;
        r18 = r0.substring(r1, r10);
        r17 = r17.append(r18);
        r18 = "☆";
        r17 = r17.append(r18);
        r18 = r10 + 1;
        r0 = r20;
        r1 = r18;
        r18 = r0.substring(r1);
        r17 = r17.append(r18);
        r20 = r17.toString();
    L_0x01b0:
        r12 = r10 + 1;
        r6 = r6 + 1;
        goto L_0x00ee;
    L_0x01b6:
        r5 = 0;
        goto L_0x0173;
    L_0x01b8:
        r17 = 3;
        r17 = r13[r17];
        r18 = 1;
        r18 = r13[r18];
        r17 = r17 - r18;
        r18 = 3;
        r18 = r14[r18];
        r19 = 1;
        r19 = r14[r19];
        r18 = r18 - r19;
        r17 = r17 - r18;
        r17 = java.lang.Math.abs(r17);
        r18 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r17 = (r17 > r18 ? 1 : (r17 == r18 ? 0 : -1));
        if (r17 <= 0) goto L_0x0204;
    L_0x01d8:
        r17 = new java.lang.StringBuilder;
        r17.<init>();
        r18 = 0;
        r0 = r20;
        r1 = r18;
        r18 = r0.substring(r1, r10);
        r17 = r17.append(r18);
        r18 = "✔";
        r17 = r17.append(r18);
        r18 = r10 + 1;
        r0 = r20;
        r1 = r18;
        r18 = r0.substring(r1);
        r17 = r17.append(r18);
        r20 = r17.toString();
        goto L_0x01b0;
    L_0x0204:
        r17 = 45;
        r0 = r17;
        if (r3 != r0) goto L_0x0244;
    L_0x020a:
        r17 = new java.lang.StringBuilder;
        r17.<init>();
        r18 = 0;
        r19 = r10 + -1;
        r0 = r20;
        r1 = r18;
        r2 = r19;
        r18 = r0.substring(r1, r2);
        r18 = r17.append(r18);
        if (r5 == 0) goto L_0x0241;
    L_0x0223:
        r17 = "☀☀";
    L_0x0225:
        r0 = r18;
        r1 = r17;
        r17 = r0.append(r1);
        r18 = r10 + 1;
        r0 = r20;
        r1 = r18;
        r18 = r0.substring(r1);
        r17 = r17.append(r18);
        r20 = r17.toString();
        goto L_0x01b0;
    L_0x0241:
        r17 = "-✔";
        goto L_0x0225;
    L_0x0244:
        if (r5 != 0) goto L_0x0273;
    L_0x0246:
        r17 = new java.lang.StringBuilder;
        r17.<init>();
        r18 = 0;
        r0 = r20;
        r1 = r18;
        r18 = r0.substring(r1, r10);
        r17 = r17.append(r18);
        r18 = "✔";
        r17 = r17.append(r18);
        r18 = r10 + 1;
        r0 = r20;
        r1 = r18;
        r18 = r0.substring(r1);
        r17 = r17.append(r18);
        r20 = r17.toString();
        goto L_0x01b0;
    L_0x0273:
        r17 = new java.lang.StringBuilder;
        r17.<init>();
        r18 = 0;
        r0 = r20;
        r1 = r18;
        r18 = r0.substring(r1, r10);
        r18 = r17.append(r18);
        r17 = isBlankChar(r3);
        if (r17 != 0) goto L_0x0298;
    L_0x028c:
        r17 = java.lang.Character.getType(r3);
        r19 = 5;
        r0 = r17;
        r1 = r19;
        if (r0 != r1) goto L_0x02b6;
    L_0x0298:
        r17 = "☀";
    L_0x029a:
        r0 = r18;
        r1 = r17;
        r17 = r0.append(r1);
        r18 = r10 + 1;
        r0 = r20;
        r1 = r18;
        r18 = r0.substring(r1);
        r17 = r17.append(r18);
        r20 = r17.toString();
        goto L_0x01b0;
    L_0x02b6:
        r17 = " ";
        goto L_0x029a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.books.A.formatPdfReflowText(java.lang.String, com.radaee.pdf.Page):java.lang.String");
    }

    private static void addTtsLine(ArrayList<TtsLine> list, TtsLine ttsLine) {
        if (ttsLine.isAvialable()) {
            list.add(ttsLine);
        }
    }

    public static void addDesktopShortcut(Activity act, String filename, String bookName, Drawable d) {
        Intent intent2 = new Intent(act, ActivityMain.class);
        intent2.setAction("android.intent.action.VIEW");
        intent2.setData(Uri.parse("content://" + filename));
        Bundle extras = new Bundle();
        extras.putString("bookFile", filename);
        extras.putBoolean("fromWidget", true);
        intent2.putExtras(extras);
        Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcutIntent.putExtra("android.intent.extra.shortcut.NAME", bookName);
        shortcutIntent.putExtra("android.intent.extra.shortcut.INTENT", intent2);
        if (d == null) {
            d = getContext().getResources().getDrawable(R.drawable.w_book_bottom);
        }
        int size = d(48.0f);
        if (VERSION.SDK_INT >= 11) {
            try {
                size = ((ActivityManager) act.getSystemService("activity")).getLauncherLargeIconSize();
            } catch (Exception e) {
                error(e);
            }
        }
        shortcutIntent.putExtra("android.intent.extra.shortcut.ICON", T.drawableToBitmap(d, (size * 70) / 95, size));
        act.sendBroadcast(shortcutIntent);
    }

    public static int getHighlightColor() {
        return highlight_color1;
    }

    public static void showBuyDialog(final Activity selfPref, String text) {
        new Builder(selfPref).setTitle((int) R.string.app_name).setMessage((CharSequence) text).setPositiveButton((int) R.string.buy_pro_version, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (A.isAmazonVersion) {
                    T.openUrl(selfPref, "http://www.amazon.com/gp/product/B004TN7QPG");
                } else if (!T.openAppInMarket(selfPref, "com.flyersoft.moonreaderp")) {
                    T.showAlertText(selfPref, selfPref.getString(R.string.error), selfPref.getString(R.string.market_not_install));
                }
            }
        }).setNegativeButton(17039360, null).show();
    }

    public static void customizeBottomIcons(final Activity act, boolean adjustHeight) {
        if (act != null) {
            boolean z;
            hintForBottomBar = false;
            final View view = LayoutInflater.from(act).inflate(R.layout.bottom_bar_setting, null);
            ((CheckBox) view.findViewById(R.id.b_orientation)).setChecked(showOrientationButton);
            ((CheckBox) view.findViewById(R.id.b_option)).setChecked(showOptionButton);
            ((CheckBox) view.findViewById(R.id.b_visual)).setChecked(showVisualButton);
            ((CheckBox) view.findViewById(R.id.b_control)).setChecked(showControlButton);
            ((CheckBox) view.findViewById(R.id.b_misc)).setChecked(showMiscButton);
            ((CheckBox) view.findViewById(R.id.b_daynight)).setChecked(showDayNightButton);
            ((CheckBox) view.findViewById(R.id.b_autoscroll)).setChecked(showAutoscrollButton);
            ((CheckBox) view.findViewById(R.id.b_bookmark)).setChecked(showBookmarkButton);
            ((CheckBox) view.findViewById(R.id.b_chapter)).setChecked(showChapterButton);
            ((CheckBox) view.findViewById(R.id.b_search)).setChecked(showSearchButton);
            ((CheckBox) view.findViewById(R.id.b_brightness)).setChecked(showBrightnessButton);
            ((CheckBox) view.findViewById(R.id.b_tilt)).setChecked(showTiltButton);
            ((CheckBox) view.findViewById(R.id.b_fontsize)).setChecked(showFontSizeButton);
            ((CheckBox) view.findViewById(R.id.b_shutdown)).setChecked(showShutDownButton);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.b_speak);
            if ((isProVersion || inviteProVersion) && showSpeakButton) {
                z = true;
            } else {
                z = false;
            }
            checkBox.setChecked(z);
            if (isProVersion || inviteProVersion) {
                ((TextView) view.findViewById(R.id.tipTV)).setVisibility(8);
            }
            Builder ab = new Builder(act);
            ab.setCancelable(false);
            ab.setTitle((int) R.string.customize_bottom_bar1).setView(view);
            if (isProVersion || inviteProVersion) {
                ab.setPositiveButton(17039370, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        A.showOrientationButton = ((CheckBox) view.findViewById(R.id.b_orientation)).isChecked();
                        A.showOptionButton = ((CheckBox) view.findViewById(R.id.b_option)).isChecked();
                        A.showVisualButton = ((CheckBox) view.findViewById(R.id.b_visual)).isChecked();
                        A.showControlButton = ((CheckBox) view.findViewById(R.id.b_control)).isChecked();
                        A.showMiscButton = ((CheckBox) view.findViewById(R.id.b_misc)).isChecked();
                        A.showDayNightButton = ((CheckBox) view.findViewById(R.id.b_daynight)).isChecked();
                        A.showAutoscrollButton = ((CheckBox) view.findViewById(R.id.b_autoscroll)).isChecked();
                        A.showBookmarkButton = ((CheckBox) view.findViewById(R.id.b_bookmark)).isChecked();
                        A.showChapterButton = ((CheckBox) view.findViewById(R.id.b_chapter)).isChecked();
                        A.showSearchButton = ((CheckBox) view.findViewById(R.id.b_search)).isChecked();
                        A.showBrightnessButton = ((CheckBox) view.findViewById(R.id.b_brightness)).isChecked();
                        A.showTiltButton = ((CheckBox) view.findViewById(R.id.b_tilt)).isChecked();
                        A.showFontSizeButton = ((CheckBox) view.findViewById(R.id.b_fontsize)).isChecked();
                        A.showShutDownButton = ((CheckBox) view.findViewById(R.id.b_shutdown)).isChecked();
                        A.showSpeakButton = ((CheckBox) view.findViewById(R.id.b_speak)).isChecked();
                        if (!T.isNull(ActivityTxt.selfPref)) {
                            ActivityTxt.selfPref.setBottomIconsVisibility();
                        }
                    }
                });
            } else {
                view.findViewById(R.id.tipTV).setClickable(true);
                view.findViewById(R.id.tipTV).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        A.showAboutDialog(act, false);
                    }
                });
                if (!(isAmazonVersion || act == null)) {
                    ab.setPositiveButton((int) R.string.buy_pro_version, new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (!T.openAppInMarket(act, "com.flyersoft.moonreaderp")) {
                                T.showAlertText(act, act.getString(R.string.error), act.getString(R.string.market_not_install));
                            }
                        }
                    });
                }
            }
            ab.setNegativeButton(17039360, null);
            ab.show();
        }
    }

    public static boolean isMimeWithSystem(String mime) {
        return !mime.equals("*/*");
    }

    public static void error(Throwable e) {
        try {
            if (e instanceof OutOfMemoryError) {
                tmpOutOfMemoryTag = true;
                log("####OutOfMemoryError####-----------------------------------", true);
                saveMemoryLog("");
                return;
            }
            log("####ERROR####-----------------------------------", true);
            lastErrorInfo = errorMsg(e);
            log(lastErrorInfo + "##", true);
            e.printStackTrace();
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
    }

    public static String errorMsg(Throwable e) {
        String err = e.getMessage();
        if (err == null) {
            try {
                err = e.toString();
            } catch (Throwable e2) {
                e2.printStackTrace();
            }
        }
        return err;
    }

    public static void log(String info) {
        log(info, false);
    }

    public static void log(String info, boolean forceShowLog) {
        if (!isBetaVersion && !forceShowLog) {
            return;
        }
        if (info == null) {
            Log.i("MR2", "null string");
            return;
        }
        if (logTime > 0) {
            if (priorLogTime < logTime) {
                priorLogTime = logTime;
            }
            long now = SystemClock.elapsedRealtime();
            info = "[" + (now - logTime) + "," + (now - priorLogTime) + "]" + info;
            priorLogTime = now;
        }
        Log.i("MR2", info);
    }

    public static String getAppTag() {
        String from = isAmazonVersion ? "amazon_" : "";
        String tag = isProVersion ? "pro" : "free";
        String ver = versionTag;
        try {
            ver = "" + getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode;
        } catch (Exception e) {
            error(e);
        }
        return from + tag + "_" + ver;
    }

    private static GoogleAnalyticsTracker getTracker() {
        if (tracker == null) {
            tracker = GoogleAnalyticsTracker.getInstance();
            tracker.startNewSession(isProVersion ? "UA-29724898-1" : "UA-27143247-1", getContext());
        }
        return tracker;
    }

    public static void trackEvent(String category, String action, String label, int value) {
        try {
            getTracker().trackEvent(category, action, label, value);
        } catch (Throwable e) {
            error(e);
        }
    }

    public static void trackPageView(String page) {
        try {
            getTracker().trackPageView(page);
        } catch (Throwable e) {
            error(e);
        }
    }

    public static void trackerDispatch(boolean forceDispatch) {
        if (tracker != null) {
            if (forceDispatch || SystemClock.elapsedRealtime() - trackerDispatchTime >= 300000) {
                try {
                    tracker.dispatch();
                    trackerDispatchTime = SystemClock.elapsedRealtime();
                } catch (Throwable e) {
                    error(e);
                }
            }
        }
    }

    public static int d(float i) {
        return (int) (getDensity() * i);
    }

    public static float df(float i) {
        return getDensity() * i;
    }

    public static int vd(float i) {
        return (int) (i / getDensity());
    }

    public static float vdf(float i) {
        return i / getDensity();
    }

    public static DropboxSync getDropbox() {
        if (dropbox == null) {
            dropbox = new DropboxSync(getContext());
            trackEvent("Dropbox", localeCountry, getAppTag(), 1);
        }
        return dropbox;
    }

    public static boolean validateDropboxAccount(Context context) {
        if (getDropbox().isLoggined) {
            return true;
        }
        syncWindowOpened = true;
        getDropbox().showLogin(context);
        return false;
    }

    public static String getDropboxPrefix() {
        return dropboxBookPath + "/.Moon+";
    }

    public static String getDropboxNoteFilename() {
        return getDropboxPrefix() + "/Cache/" + T.getFilename(lastFile) + ".an";
    }

    public static String getDropboxPositionFilename() {
        return getDropboxPrefix() + "/Cache/" + T.getFilename(lastFile) + ".po";
    }

    public static String getDropboxRecentFile() {
        return getDropboxPrefix() + "/Cache/recent.list";
    }

    public static boolean uploadCloudPositionFile() {
        SharedPreferences sp = getContext().getSharedPreferences(POSITION_FILE, 0);
        String pos1 = sp.getString(lastFile.toLowerCase(), null);
        if (pos1 == null) {
            return false;
        }
        int pos2 = sp.getInt(lastFile.toLowerCase() + 2, -1);
        String text = deviceRandomID + "*" + pos1 + (pos2 == -1 ? "" : "|" + pos2);
        tmpRecentListText = !cloudHasCurrentBook ? null : createCloudRecentList(text);
        if (syncType == 2) {
            if (getDropbox().isLoggined && !getDropbox().working) {
                getDropbox().uploadFile(getDropboxPositionFilename(), T.String2InputStream(text), new OnDropboxUploaded() {
                    public void afterUpload(String fileRev, String errorMsg) {
                        if (fileRev != null) {
                            A.uploadCloudNoteFile();
                        } else {
                            ActivityTxt.sync_finish_message();
                        }
                    }
                }, null, false);
                return true;
            }
        } else if (getGdrive().isLoggined && !getGdrive().inWorking()) {
            getGdrive().uploadFile(getGdrivePositionFilename(), T.String2InputStream(text), text, new OnGdriveUploaded() {
                public void afterUpload(String fileId, String errorMsg) {
                    if (fileId != null) {
                        A.uploadCloudNoteFile();
                    } else {
                        ActivityTxt.sync_finish_message();
                    }
                }
            }, false);
            return true;
        }
        ActivityTxt.sync_finish_message();
        return false;
    }

    public static String createCloudRecentList(String current) {
        String remoteFile;
        if (lastFile.startsWith(cloud_cache_path)) {
            remoteFile = getContext().getSharedPreferences(syncType == 2 ? "dropbox_cache" : "gdrive_cache", 0).getString(lastFile, null);
            if (remoteFile == null) {
                return null;
            }
        }
        remoteFile = (syncType == 2 ? dropboxBookPath : gdriveBookPath) + "/" + T.getFilename(lastFile);
        return remoteFile + "??1" + getBookName() + "??2" + System.currentTimeMillis() + "??3" + new File(lastFile).length() + "??4" + lastFile + "??5" + current;
    }

    public static void uploadCloudRecentList(boolean fromMain) {
        if (tmpRecentListText != null) {
            log("sync recentList, fromMain: " + fromMain);
            if (syncType == 2) {
                getDropbox().uploadFile(getDropboxRecentFile(), T.String2InputStream(tmpRecentListText), new OnDropboxUploaded() {
                    public void afterUpload(String fileRev, String errorMsg) {
                        ActivityTxt.sync_finish_message();
                    }
                }, null, true);
            } else {
                getGdrive().uploadFile(getGdriveRecentFile(), T.String2InputStream(tmpRecentListText), tmpRecentListText, new OnGdriveUploaded() {
                    public void afterUpload(String fileId, String errorMsg) {
                        ActivityTxt.sync_finish_message();
                    }
                }, true);
            }
            tmpRecentListText = null;
            return;
        }
        ActivityTxt.sync_finish_message();
    }

    protected static void uploadCloudNoteFile() {
        if (hasNewNoteForUpload) {
            hasNewNoteForUpload = false;
            String text = BookDb.exportNotesToText(lastFile, null);
            if (deletedNotes != null) {
                text = text + BookDb.exportNotesToText(deletedNotes);
            }
            if (text.length() > 10) {
                InputStream is = T.Byte2InputStream(MyZip_Java.compress(text));
                if (syncType == 2) {
                    getDropbox().uploadFile(getDropboxNoteFilename(), is, new OnDropboxUploaded() {
                        public void afterUpload(String fileRev, String errorMsg) {
                            A.uploadCloudRecentList(false);
                        }
                    }, null, false);
                    return;
                } else {
                    getGdrive().uploadFile(getGdriveNoteFilename(), is, new String(MyZip_Java.compress(text)), new OnGdriveUploaded() {
                        public void afterUpload(String fileId, String errorMsg) {
                            A.uploadCloudRecentList(false);
                        }
                    }, false);
                    return;
                }
            }
            ActivityTxt.sync_finish_message();
            return;
        }
        uploadCloudRecentList(false);
    }

    public static CloudRecentItem validateRemoteRecentItem(CloudRecentItem dr) {
        if (dr == null || !dr.ok) {
            return null;
        }
        openFromRemoteRecent = dr;
        String filename = T.getFilename(dr.cloudFile);
        for (int i = 0; i < getHistory().size(); i++) {
            if (T.getFilename((String) getHistory().get(i)).equals(filename)) {
                getHistory().add(0, getHistory().remove(i));
                return null;
            }
        }
        if (T.isFile(dr.filenameInUploadedDevice)) {
            getHistory().add(0, dr.filenameInUploadedDevice);
            return null;
        }
        for (File file : new File(lastPath).listFiles()) {
            if (file.length() == dr.remoteFilesize && T.getFilename(file.getAbsolutePath()).equals(filename)) {
                getHistory().add(0, file.getAbsolutePath());
                return null;
            }
        }
        if (lastPath.equals(download_saved_path)) {
            return dr;
        }
        for (File file2 : new File(download_saved_path).listFiles()) {
            if (file2.length() == dr.remoteFilesize && T.getFilename(file2.getAbsolutePath()).equals(filename)) {
                getHistory().add(0, file2.getAbsolutePath());
                return dr;
            }
        }
        return dr;
    }

    public static void downloadCloudPositionFile() {
        if (openFromRemoteRecent == null || !T.getFilename(lastFile).equals(T.getFilename(openFromRemoteRecent.cloudFile))) {
            openFromRemoteRecent = null;
            if (syncType == 2) {
                if (getDropbox().isLoggined && !getDropbox().working) {
                    getDropbox().downloadFile(getDropboxPositionFilename(), new OnDropBoxDownloaded() {
                        public void afterDownload(ByteArrayOutputStream os, String fileRev, String errorMsg) {
                            A.dealDownloadedCloudPosition(os, null, fileRev, errorMsg);
                        }
                    }, null);
                    return;
                }
                return;
            } else if (getGdrive().isLoggined && !getGdrive().inWorking()) {
                getGdrive().downloadFile(getGdrivePositionFilename(), true, new OnGdriveDownloaded() {
                    public void afterDownload(ByteArrayOutputStream os, String os_text, String fileId, String errorMsg) {
                        A.dealDownloadedCloudPosition(os, os_text, fileId, errorMsg);
                    }
                });
                return;
            } else {
                return;
            }
        }
        notifyNewCloudPosition(openFromRemoteRecent.positionInfo);
        openFromRemoteRecent = null;
        downloadCloudNoteFile();
    }

    private static void dealDownloadedCloudPosition(ByteArrayOutputStream os, String os_text, String fileRev, String errorMsg) {
        String text = os_text;
        if (os != null) {
            try {
                text = new String(os.toByteArray());
            } catch (Exception e) {
                error(e);
            }
        }
        if (text != null) {
            notifyNewCloudPosition(text);
        }
        if (os == null && os_text == null && fileRev == null && errorMsg == null) {
            hasNewNoteForUpload = true;
        }
        downloadCloudNoteFile();
    }

    private static void notifyNewCloudPosition(String text) {
        int index = text.indexOf("*");
        if (index != -1) {
            String pos1;
            String remoteDeviceID = text.substring(0, index);
            text = text.substring(index + 1);
            index = text.indexOf("|");
            int pos2 = -1;
            if (index == -1) {
                pos1 = text;
            } else {
                pos1 = text.substring(0, index);
                pos2 = Integer.valueOf(text.substring(index + 1)).intValue();
            }
            if (!remoteDeviceID.equals(deviceRandomID)) {
                Editor et = getContext().getSharedPreferences(POSITION_FILE, 0).edit();
                et.putString(lastFile.toLowerCase(), pos1);
                if (pos2 != -1) {
                    et.putInt(lastFile.toLowerCase() + 2, pos2);
                }
                et.commit();
                if (!T.isNull(ActivityTxt.selfPref)) {
                    ActivityTxt.selfPref.handler.sendEmptyMessage(801);
                }
            }
        }
    }

    public static void downloadCloudNoteFile() {
        if (syncType == 2) {
            if (getDropbox().isLoggined && !getDropbox().working) {
                getDropbox().downloadFile(getDropboxNoteFilename(), new OnDropBoxDownloaded() {
                    public void afterDownload(ByteArrayOutputStream os, String fileRev, String errorMsg) {
                        A.dealDownloadedCloudNote(os);
                    }
                }, null);
            }
        } else if (getGdrive().isLoggined && !getGdrive().inWorking()) {
            getGdrive().downloadFile(getGdriveNoteFilename(), false, new OnGdriveDownloaded() {
                public void afterDownload(ByteArrayOutputStream os, String os_text, String fileId, String errorMsg) {
                    A.dealDownloadedCloudNote(os);
                }
            });
        }
    }

    private static void dealDownloadedCloudNote(ByteArrayOutputStream os) {
        if (os != null) {
            extractSyncNotes(MyZip_Java.decompress(os.toByteArray()));
            if (!T.isNull(ActivityTxt.selfPref)) {
                ActivityTxt.selfPref.contentLay.postInvalidate();
            }
        }
    }

    private static void extractSyncNotes(String text) {
        if (getFileType() == 7) {
            if (!T.isNull(ActivityTxt.selfPref) && ActivityTxt.selfPref.pdf != null) {
                ActivityTxt.selfPref.pdf.createNotesFromCloud(text);
            }
        } else if (BookDb.importNotesFromText(lastFile, text) == 1) {
            getBookmarks(true);
            checkNotesHighlights(true);
        }
    }

    public static void backupReadingBookToCloud(Activity act, Handler msgHandler) {
        if (!syncReadingBookFile || act == null || lastFile.indexOf("?") != -1) {
            return;
        }
        if ((syncBookInWiFiOnly && !T.isWiFiConnecting(act)) || syncType == 0) {
            return;
        }
        if (syncType == 2 && !getDropbox().isLoggined) {
            return;
        }
        if (syncType == 4 && !getGdrive().isLoggined) {
            return;
        }
        if (lastFile.equals(uploadingBookFile)) {
            if (syncType == 2) {
                new DropboxTask(act, 8, dropboxBookPath + "/" + T.getFilename(lastFile), null, null, false, msgHandler).execute(new Void[0]);
            } else {
                new GdriveTask(act, 8, gdriveBookPath + "/" + T.getFilename(lastFile), null, null, false, msgHandler).execute(new Void[0]);
            }
        } else if (lastFile.startsWith(cloud_cache_path)) {
            String remoteFile = act.getSharedPreferences(syncType == 2 ? "dropbox_cache" : "gdrive_cache", 0).getString(lastFile, null);
            if (remoteFile == null) {
                return;
            }
            if (syncType == 2) {
                new DropboxTask(act, 8, remoteFile, null, null, false, msgHandler).execute(new Void[0]);
            } else {
                new GdriveTask(act, 8, remoteFile, null, null, false, msgHandler).execute(new Void[0]);
            }
        } else {
            uploadingBookFile = lastFile;
            ArrayList<String> list = new ArrayList();
            list.add(lastFile);
            if (syncType == 2) {
                new DropboxTask(act, 2, dropboxBookPath, list, T.getFilePath(lastFile), true, msgHandler).execute(new Void[0]);
            } else {
                new GdriveTask(act, 2, gdriveBookPath, list, T.getFilePath(lastFile), true, msgHandler).execute(new Void[0]);
            }
        }
    }

    public static String getGdriveTitle(int strRes) {
        return getContext().getString(strRes).replaceAll("(?i)dropbox", "Gdrive");
    }

    public static String getGdriveTitle(String text) {
        return text.replaceAll("(?i)dropbox", "Gdrive");
    }

    public static String getGdriveTitle(TextView tv) {
        return tv.getText().toString().replaceAll("(?i)dropbox", "Gdrive");
    }

    public static String fullGdriveTitle(String text) {
        return text.replaceAll("(?i)gdrive", "Google Drive");
    }

    public static String getGdrivePrefix() {
        return gdriveBookPath + "/.Moon+";
    }

    public static String getGdriveNoteFilename() {
        return getGdrivePrefix() + "/Cache/" + T.getFilename(lastFile) + ".an";
    }

    public static String getGdrivePositionFilename() {
        return getGdrivePrefix() + "/Cache/" + T.getFilename(lastFile) + ".po";
    }

    public static String getGdriveRecentFile() {
        return getGdrivePrefix() + "/Cache/recent.list";
    }

    public static GdriveSync getGdrive() {
        return getGdrive(false);
    }

    public static GdriveSync getGdrive(boolean checkService) {
        if (gdrive == null || (checkService && gdrive.service == null)) {
            gdrive = new GdriveSync(getContext());
            trackEvent("Gdrive", localeCountry, getAppTag(), 1);
        }
        return gdrive;
    }

    public static boolean validateGdriveAccount(Activity act) {
        if (getGdrive().isLoggined) {
            return true;
        }
        if (showPlayServiceErrorInfo(act)) {
            return false;
        }
        if (permissionAllow(act, "android.permission.GET_ACCOUNTS")) {
            getGdrive().showAccountPickDialog();
            return false;
        }
        act.requestPermissions(new String[]{"android.permission.GET_ACCOUNTS"}, HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES);
        return false;
    }

    public static Drawable getApkFileIcon(String filename) {
        if (!T.isFile(filename)) {
            return null;
        }
        PackageInfo packageInfo = getContext().getPackageManager().getPackageArchiveInfo(filename, 1);
        if (packageInfo == null) {
            return null;
        }
        ApplicationInfo appInfo = packageInfo.applicationInfo;
        appInfo.sourceDir = filename;
        appInfo.publicSourceDir = filename;
        return appInfo.loadIcon(getContext().getPackageManager());
    }

    public static boolean isImageFileExt(String ext) {
        return ext.equals(NETCOVER_TAG) || ext.equals(".jpg") || ext.equals(".gif") || ext.equals(".jpeg") || ext.equals(".svg");
    }

    public static boolean isGalleryImage(String filename) {
        String ext = T.getFileExt(filename);
        return ext.equals(NETCOVER_TAG) || ext.equals(".jpg") || ext.equals(".gif") || ext.equals(".jpeg") || ext.equals(".svg") || ext.equals(".cbz") || ext.equals(".cbr");
    }

    public static void initPathParameters(boolean forceRefresh) {
        if ((firstTimeInstall || forceRefresh) && !availableFolder(DEFAULT_BOOK_FOLDER)) {
            String inner = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Books";
            if (availableFolder(inner)) {
                default_book_folder = inner;
            } else if (availableFolder("/mnt/sdcard/Books")) {
                default_book_folder = "/mnt/sdcard/Books";
            } else if (availableFolder("/mnt/extsdcard/Books")) {
                default_book_folder = "/mnt/extsdcard/Books";
            }
        }
        download_saved_path = default_book_folder + "/MoonReader";
        download_cache_path = default_book_folder + "/.MoonReader";
        book_cache = download_cache_path + "/cache";
        cloud_cache_path = download_cache_path + "/cloud";
        tmp_out_file = book_cache + "/tmp";
    }

    private static boolean availableFolder(String folder) {
        boolean ok = T.isFolder(T.getFilePath(folder));
        if (!ok || T.isFolder(folder) || T.createFolder(folder)) {
            return ok;
        }
        return false;
    }

    public static boolean isDownloadFile(String filename) {
        String path = T.getFilePath(filename).toLowerCase();
        return path.equals(download_saved_path.toLowerCase()) || path.equals(DEFAULT_DOWNLOAD_SAVED_PATH.toLowerCase());
    }

    public static int defaultScreenState() {
        if (isTablet && isLandscape()) {
            return 5;
        }
        return 6;
    }

    public static int getScreenOrientation(int index) {
        if (index > 2 && VERSION.SDK_INT < 9) {
            return 1;
        }
        switch (index) {
            case 0:
                return VERSION.SDK_INT < 9 ? 4 : 10;
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 8;
            case 4:
                return 9;
            case 5:
                return 6;
            case 6:
                return 7;
            default:
                return 1;
        }
    }

    public static void setSystemUiVisibility(boolean hide) {
        if (!T.isNull(ActivityTxt.selfPref)) {
            setSystemUiVisibility(ActivityTxt.selfPref.getWindow().getDecorView(), hide);
        }
    }

    public static void setSystemUiVisibility(View view, boolean hide) {
        if (view == null || VERSION.SDK_INT < 11) {
            return;
        }
        if (VERSION.SDK_INT >= 14 && !noMenuKey) {
            return;
        }
        if (hide && !fullscreen) {
            return;
        }
        if (!hide) {
            try {
                view.setSystemUiVisibility(0);
            } catch (Throwable e) {
                error(e);
            }
        } else if (immersive_fullscreen) {
            view.setSystemUiVisibility(3846);
        } else {
            view.setSystemUiVisibility(1);
        }
    }

    public static void forceDialogImmersiveMode(Dialog dialog) {
        if (VERSION.SDK_INT >= 11) {
            if ((VERSION.SDK_INT < 14 || noMenuKey) && fullscreen) {
                try {
                    Window window = dialog.getWindow();
                    window.setFlags(1024, 1024);
                    window.getDecorView().setSystemUiVisibility(6150);
                } catch (Exception e) {
                    error(e);
                }
            }
        }
    }

    public static boolean isHoneycombTablet() {
        return VERSION.SDK_INT >= 11 && isTablet;
    }

    public static String chineseJianFanConvert(String text) {
        if (verticalAlignment && !isJapanese) {
            text = text.replaceAll("“|‘|〝", "﹁").replaceAll("”|’|〞", "﹂").replace('(', '︵').replace(')', '︶').replace('[', '︹').replace(']', '︺');
        }
        if (!textFan2Jian && !textJian2Fan) {
            return text;
        }
        if ((textFan2Jian && textJian2Fan) || !isChinese) {
            return text;
        }
        if (textJian2Fan) {
            return ChineseJianFanConvert.convertJian2Fan(text);
        }
        return ChineseJianFanConvert.convertFan2Jian(text);
    }

    public static int getPageWidth() {
        if (txtScroll == null || txtScroll.getWidth() <= 0) {
            return (getScreenWidth2() - leftMargin) - rightMargin;
        }
        int padding = txtScroll.getPaddingLeft() + txtScroll.getPaddingRight();
        int result = txtScroll.getWidth() - padding;
        if (landscape2PageMode && isLandscape() && result > (getScreenWidth2() / 2) + d(2.0f)) {
            return (baseFrame.getWidth() / 2) - padding;
        }
        return result;
    }

    public static int getPageWidth2() {
        if (!textHyphenation || txtView == null || txtView.getWidth() <= 0) {
            return getPageWidth();
        }
        return txtView.getWidth2();
    }

    public static void getScreenRealHeight(Activity act) {
        screenRealHeight = null;
        screenRealWidth = null;
        if (VERSION.SDK_INT >= 17) {
            try {
                Point size = new Point();
                act.getWindowManager().getDefaultDisplay().getRealSize(size);
                screenRealHeight = Integer.valueOf(size.y);
                screenRealWidth = Integer.valueOf(size.x);
            } catch (Throwable e) {
                error(e);
            }
        }
        if (screenRealHeight == null || screenRealHeight.intValue() < getScreenHeight()) {
            screenRealHeight = Integer.valueOf(getScreenHeight());
            screenRealWidth = Integer.valueOf(getScreenWidth());
        }
    }

    public static boolean sysHasNavBar() {
        boolean z = true;
        boolean z2 = false;
        if (hasNavBar != null) {
            return hasNavBar.booleanValue();
        }
        if (VERSION.SDK_INT > 17) {
            if (getAct() == null) {
                return false;
            }
            getScreenRealHeight(getAct());
            if (screenRealHeight.intValue() > getScreenHeight() || screenRealWidth.intValue() > getScreenWidth()) {
                z2 = true;
            }
            hasNavBar = Boolean.valueOf(z2);
            return hasNavBar.booleanValue();
        } else if (getAct() == null) {
            return false;
        } else {
            try {
                boolean hasMenuKey = ViewConfiguration.get(getAct()).hasPermanentMenuKey();
                boolean hasBackKey = KeyCharacterMap.deviceHasKey(4);
                if (hasMenuKey || hasBackKey) {
                    z = false;
                }
                hasNavBar = Boolean.valueOf(z);
            } catch (Throwable e) {
                error(e);
                hasNavBar = Boolean.valueOf(false);
            }
            return hasNavBar.booleanValue();
        }
    }

    public static int getPageHeight() {
        if (txtScroll == null || txtScroll.getWidth() <= 0) {
            return (getScreenHeight2() - topMargin) - bottomMargin;
        }
        return (baseFrame.getHeight() - txtScroll.getPaddingTop()) - txtScroll.getPaddingBottom();
    }

    public static float oneLineTag() {
        float value = lineSpace >= 2 ? 0.75f : 0.82f;
        if (isAsiaLanguage) {
            return value + 0.1f;
        }
        return value + 0.08f;
    }

    public static int oneLineTagHeight() {
        return (int) (((float) txtView.getLineHeight()) * oneLineTag());
    }

    public static int oneLineTagHeight(int line) {
        return (int) (((float) txtView.getLineHeight(line)) * oneLineTag());
    }

    public static int getLastDisplayLine(ScrollView sv, int curLine) {
        MyLayout lo = txtView.getLayout();
        if (lo == null) {
            return 0;
        }
        int y = curLine == -1 ? sv.getScrollY() : txtView.getLineTop2(curLine);
        int line = lo.getLineForVertical((getPageHeight() + y) - 1);
        if (line > 0 && txtView.getLineTop3(line) > (getPageHeight() + y) - oneLineTagHeight(line)) {
            line--;
        }
        int tbLine = (sv == txtScroll ? txtView : txtView2).getLastBorderTopLine(line);
        if (tbLine > 0) {
            line = tbLine - 1;
        }
        if (curLine == -1) {
            curLine = lo.getLineForVertical(y);
        }
        if (line < curLine) {
            return curLine;
        }
        return line;
    }

    public static int getLastDisplayLine2(ScrollView sv) {
        return txtView.getLayout().getLineForVertical((sv.getScrollY() + getPageHeight()) - 1);
    }

    public static boolean checkIfHasMenuKey(Context context) {
        noMenuKey = false;
        if (VERSION.SDK_INT >= 14) {
            try {
                boolean z;
                if (((Boolean) ViewConfiguration.class.getMethod("hasPermanentMenuKey", new Class[0]).invoke(ViewConfiguration.get(context.getApplicationContext()), new Object[0])).booleanValue()) {
                    z = false;
                } else {
                    z = true;
                }
                noMenuKey = z;
            } catch (Exception e) {
                error(e);
            }
        }
        return noMenuKey;
    }

    public static int myAlertDialogWidth(boolean useMax) {
        int target;
        int w = getScreenWidth();
        int h = getScreenHeight();
        if (w > h) {
            target = h;
        } else {
            target = w;
        }
        int phone = useMax ? target : (target * 980) / 1000;
        int i = isLargeTablet ? useMax ? 64 : 60 : useMax ? 88 : 84;
        int tablet = (i * target) / 100;
        if (isTablet) {
            return tablet;
        }
        return phone;
    }

    public static void setAlertDialogWidth(Window window, float dim, boolean max) {
        LayoutParams lp = window.getAttributes();
        lp.width = myAlertDialogWidth(max);
        if (noMenuKey) {
            lp.gravity = 49;
        } else {
            lp.height = -2;
        }
        lp.softInputMode = 3;
        if (dim != -1.0f) {
            lp.dimAmount = dim;
            window.addFlags(2);
            window.addFlags(1024);
        }
        window.setAttributes(lp);
    }

    public static void setSpinnerListStyle(ViewGroup vg) {
        setTextColors(vg);
    }

    public static void setTextColors(ViewGroup vg) {
    }

    public static String deleteChapterEndHint(String text) {
        int p = text.indexOf(BaseEBook.CHAPTER_END_HTMLHINT1);
        if (p != -1) {
            return text.substring(0, p) + "<br>";
        }
        return text;
    }

    public static String getChapterEndText() {
        return getContext().getString(R.string.chapter_end);
    }

    public static boolean disableGPU(View v, Canvas canvas) {
        if (VERSION.SDK_INT >= 11) {
            try {
                boolean systemGPUEnabled = isHardwareAccelerated(canvas);
                log("hardwareAccelerated(1):" + systemGPUEnabled + " " + canvas);
                if (systemGPUEnabled) {
                    disableGPUView(v);
                    return true;
                }
            } catch (Exception e) {
                error(e);
            }
        }
        return false;
    }

    public static boolean isHardwareAccelerated(Canvas canvas) {
        if (VERSION.SDK_INT >= 11) {
            try {
                return ((Boolean) Canvas.class.getMethod("isHardwareAccelerated", new Class[0]).invoke(canvas, new Object[0])).booleanValue();
            } catch (Exception e) {
                error(e);
            }
        }
        return false;
    }

    public static void disableGPUView(View v) {
        if (VERSION.SDK_INT >= 11) {
            try {
                View.class.getMethod("setLayerType", new Class[]{Integer.TYPE, Paint.class}).invoke(v, new Object[]{Integer.valueOf(1), null});
            } catch (Exception e) {
                error(e);
            }
        }
    }

    public static void incReadDays() {
        long current = (((System.currentTimeMillis() / 24) / 60) / 60) / 1000;
        if (statistics_last_day == 0) {
            statistics_last_day = current;
        } else if (current > statistics_last_day) {
            statistics_days++;
            statistics_last_day = current;
        } else if (current < statistics_last_day) {
            statistics_last_day = current;
        }
    }

    public static ArrayList<String> getFavFolders() {
        if (favFolders == null) {
            String text = T.getFileText(xml_files_folder + "/" + FAV_FOLDERS_FILE);
            if (text == null) {
                text = createDefaultFavFolders();
            }
            favFolders = T.text2StringList(text);
            int i = favFolders.size() - 1;
            while (i >= 0) {
                if (!((String) favFolders.get(i)).startsWith(DROPBOX_TAG) && !((String) favFolders.get(i)).startsWith(GDRIVE_TAG)) {
                    if (T.isFolder((String) favFolders.get(i))) {
                        for (int j = i - 1; j >= 0; j--) {
                            if (((String) favFolders.get(i)).equals(favFolders.get(j))) {
                                favFolders.remove(i);
                                break;
                            }
                        }
                    } else {
                        favFolders.remove(i);
                    }
                }
                i--;
            }
        }
        return favFolders;
    }

    private static String createDefaultFavFolders() {
        String text = null;
        if (VERSION.SDK_INT >= 21 && SD.getSDPath() != null) {
            text = SD.getSDPath() + "\n";
        } else if (T.isFolder("/mnt/extSdCard") && T.isFolder("/mnt/extSdCard/Android")) {
            text = "/mnt/extSdCard\n";
        } else if (T.isFolder("/sdcard/external_sd")) {
            text = "/sdcard/external_sd\n";
        } else if (T.isFolder("/mnt/ext_card")) {
            text = "/mnt/ext_card\n";
        } else if (T.isFolder("/mnt/ext_sdcard")) {
            text = "/mnt/ext_sdcard\n";
        } else if (T.isFolder("/mnt/sdcard2")) {
            text = "/mnt/sdcard2\n";
        } else if (T.isFolder("/storage/external_SD")) {
            text = "/storage/external_SD\n";
        }
        if (text == null) {
            text = T.isFolder("/storage") ? "/storage\n" : "";
        }
        if (VERSION.SDK_INT >= 23 && !text.contains("/storage")) {
            text = text + "/storage\n";
        }
        StringBuilder append = new StringBuilder().append(text);
        String str = (T.isFolder("/sdcard/Android") || VERSION.SDK_INT < 18) ? "/sdcard\n" : "";
        text = append.append(str).append(default_book_folder).append("\n").toString();
        log("****fav:\n" + text);
        T.saveFileText(xml_files_folder + "/" + FAV_FOLDERS_FILE, text);
        return text;
    }

    public static void deleteFavFolder(String folder) {
        if (getFavFolders().indexOf(folder) != -1) {
            getFavFolders().remove(folder);
            saveFavFolders();
        }
    }

    public static void addFavFolder(String folder) {
        if (getFavFolders().indexOf(folder) == -1) {
            getFavFolders().add(folder);
            saveFavFolders();
        }
    }

    public static boolean saveFavFolders() {
        return T.saveFileText(xml_files_folder + "/" + FAV_FOLDERS_FILE, T.stringList2Text(favFolders));
    }

    public static void saveLastSearchKey() {
        getContext().getSharedPreferences(SEARCH_HISTORY_FILE, 0).edit().putString(lastSearchKey, "").commit();
    }

    public static void setLanguageList(Spinner v, Context res) {
        String[] original = getContext().getResources().getStringArray(R.array.languages);
        original[0] = "System (" + Locale.getDefault().getDisplayLanguage() + ")";
        ArrayAdapter<String> actionAdapter = new ArrayAdapter(res, 17367048, original);
        actionAdapter.setDropDownViewResource(17367049);
        v.setAdapter(actionAdapter);
    }

    public static String getAppNameAndVer() {
        String name = getContext().getString(R.string.app_name);
        try {
            name = name + " v" + getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
        } catch (Exception e) {
            error(e);
        }
        return name;
    }

    public static boolean adjustLineBreak() {
        return textCJK && textJustified && !isKorean;
    }

    public static boolean isLineEndDot(char c) {
        return isChineseDot(c) || isEnglishDot(c) || c == '》' || c == '〞' || c == '）' || c == ')' || c == '>' || c == '}' || c == ']';
    }

    public static boolean isChineseDot(char c) {
        return c == '。' || c == '，' || c == '：' || c == '；' || c == '？' || c == '！' || c == '’' || c == '”' || c == '＂' || c == '。';
    }

    public static boolean isEnglishDot(char c) {
        return c == '.' || c == ',' || c == ';' || c == '!' || c == '?';
    }

    public static void downloadFileFromBrowser(Context context, String url) {
        Intent i = new Intent(context, BookDownloadService.class);
        i.putExtra("url", url);
        i.putExtra("library_xml", "");
        i.putExtra("id", ("" + SystemClock.elapsedRealtime()).hashCode());
        int d = url.lastIndexOf("/");
        String filename = d == -1 ? url : url.substring(d + 1);
        d = filename.indexOf("?");
        if (d != -1) {
            filename = filename.substring(0, d);
        }
        if (filename.length() == 0) {
            filename = T.deleteSpecialChar(url);
        }
        i.putExtra("saveName", download_saved_path + "/" + T.getFilename(Uri.decode(filename)));
        context.startService(i);
        T.showToastText(context, context.getString(R.string.download) + ": " + url, 1);
    }

    public static Spanned myFromHtml(String html) {
        try {
            return Html.fromHtml(html);
        } catch (OutOfMemoryError e) {
            error(e);
            return new SpannableStringBuilder();
        } catch (Exception e2) {
            error(e2);
            return MyHtml.fromHtml(html);
        }
    }

    public static PrefAbout showAboutDialog(Activity act, boolean fromStart) {
        boolean englishOnly = VERSION.SDK_INT == 16 || VERSION.SDK_INT == 17;
        if (englishOnly && !forceEnglishAboutText) {
            englishOnly = (localeLanguage.equals("de") || localeLanguage.equals("it") || localeLanguage.equals("ko") || localeLanguage.equals("ja") || localeCountry.equals("HK") || localeCountry.equals("TW")) ? false : true;
        }
        String aboutText = getAboutText(englishOnly);
        try {
            String whatsnew = T.inputStream2String(getContext().getAssets().open("!whatisnew"));
            String split = "───────────────────────────";
            TextPaint paint = new TextPaint();
            paint.setTextSize(fontSize);
            if (Layout.getDesiredWidth(split, paint) * getDensity() > ((float) ((myAlertDialogWidth(false) * 9) / 10))) {
                split = "─────────────────";
                aboutText = aboutText.replace("───────────────────────────", split);
            }
            aboutText = aboutText.replace("<#top>", whatsnew.length() > 1 ? whatsnew + ("<p>" + split + "</p>") : "");
        } catch (Exception e) {
            error(e);
        }
        if (isAmazonVersion) {
            aboutText = aboutText.replaceAll("(?i)google play", "Amazon AppStore");
        }
        forceEnglishAboutText = true;
        SaveOptions(getContext());
        PrefAbout dlg = new PrefAbout(act, aboutText, fromStart);
        dlg.show();
        return dlg;
    }

    public static void sendFile(Activity activity, String filename) {
        filename = BaseCompressor.getZRCacheFile(filename);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("application/octet-stream");
        intent.putExtra("android.intent.extra.SUBJECT", T.getFilename(filename));
        intent.putExtra("android.intent.extra.TEXT", "- From " + getAppNameAndVer());
        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(filename)));
        activity.startActivity(Intent.createChooser(intent, null));
    }

    public static void generateBookCovers(Bitmap bm, String filename, boolean includeOriginal) {
        if (bm != null) {
            filename = download_cache_path + "/" + T.getFilename(filename);
            if (includeOriginal) {
                T.bitmapToFile(bm, filename + COVER_TAG);
            }
            float d = getDensity();
            float f = ((float) shelfCoverSize) * d;
            float f2 = d > 2.0f ? 0.6666667f : ((double) d) > 1.5d ? 0.75f : 1.0f;
            int size = (int) (f2 * f);
            bm = T.zoomImage(bm, size, (bm.getHeight() * size) / bm.getWidth());
            T.bitmapToFile(bm, filename + THUMB_TAG);
            bm.recycle();
        }
    }

    public static void playSound(Context context, String filename) {
        if (context == null && filename == null && mp != null) {
            mp.reset();
        }
        if (T.isFile(filename)) {
            try {
                if (mp == null) {
                    mp = new MediaPlayer();
                }
                mp.reset();
                mp.setDataSource(filename);
                mp.prepare();
                mp.start();
            } catch (Exception e) {
                error(e);
            }
        }
    }

    public static void playSound(Context context, int id) {
        try {
            if (mp == null) {
                mp = MediaPlayer.create(context, id);
                mp.start();
                return;
            }
            mp.reset();
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(id);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            error(e);
        }
    }

    public static void playPageSound(Context context) {
        if (pageSoundIndex == 3) {
            playSound(context, pageSoundFile);
            return;
        }
        int id = pageSoundIndex == 1 ? R.raw.sound1 : pageSoundIndex == 2 ? R.raw.sound2 : R.raw.sound0;
        playSound(context, id);
    }

    public static String getStringArrayItem(int id, int index) {
        try {
            return getContext().getResources().getStringArray(id)[index];
        } catch (Exception e) {
            error(e);
            return "";
        }
    }

    public static void addAdBanner(View root) {
    }

    public static String BUILD_CPU(int bookType) {
        String cpu = Build.CPU_ABI;
        if (bookType == 1 && cpu.equals("x86_64")) {
            cpu = "x86";
        }
        if (bookType == 2 && cpu.equals("arm64-v8a")) {
            cpu = "armeabi-v7a";
        }
        if (cpu.equals("armeabi-v7a") || !cpu.startsWith("armeabi")) {
            return cpu;
        }
        return "armeabi";
    }

    public static Drawable getDisplayDrawable(Drawable d) {
        if (d == null) {
            return null;
        }
        if (verticalAlignment) {
            Matrix matrix = new Matrix();
            matrix.postRotate(-90.0f);
            d = new BitmapDrawable(getContext().getResources(), Bitmap.createBitmap(T.drawableToBitmap(d), 0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight(), matrix, true));
        }
        int w1 = d.getIntrinsicWidth();
        int h1 = d.getIntrinsicHeight();
        int w2 = getPageWidth2();
        if (w1 > w2) {
            d = T.zoomDrawable(getContext().getResources(), d, w2, (h1 * w2) / w1);
        }
        float t = zoomImageRate(w1, w2, h1);
        d.setBounds(0, 0, (int) (((float) d.getIntrinsicWidth()) * t), (int) (((float) d.getIntrinsicHeight()) * t));
        return d;
    }

    public static Rect getDisplayDrawableBounds(Rect r) {
        if (verticalAlignment) {
            r = new Rect(0, 0, r.bottom, r.right);
        }
        int w1 = r.width();
        int w2 = getPageWidth2();
        int h1 = r.height();
        if (w1 > w2) {
            h1 = (h1 * w2) / w1;
            w1 = w2;
        }
        float t = zoomImageRate(w1, w2, h1);
        return new Rect(0, 0, (int) (((float) w1) * t), (int) (((float) h1) * t));
    }

    public static float zoomImageRate(int w1, int w2, int h1) {
        int h2 = (getPageHeight() * 95) / 100;
        ActivityTxt act = ActivityTxt.selfPref;
        if (dualPageEnabled() && act != null) {
            if (act.showStatusbar() && !act.checkedBottomHalfLine) {
                act.checkBottomHalfLineHandler();
                h2 = (getPageHeight() * 95) / 100;
            }
            if (h1 > h2) {
                return ((float) h2) / ((float) h1);
            }
        }
        if (w1 < w2 && h1 / w1 >= 2 && h1 > getPageHeight()) {
            return ((float) w2) / ((float) w1);
        }
        if (df((float) w1) < ((float) w2) && df((float) h1) < ((float) h2)) {
            return getDensity();
        }
        if (d((float) w1) >= w2) {
            float t = ((float) w2) / ((float) w1);
            if (((float) h1) * t < ((float) h2)) {
                return t;
            }
        }
        if (w1 >= w2 / 2 || ((getDensity() <= 2.0f && (!isTablet || ((double) getDensity()) < 1.5d)) || (dualPageEnabled() && h1 * 2 >= h2))) {
            return 1.0f;
        }
        return 2.0f;
    }

    public static void clearTextViewImageSpan(MRTextView tv) {
        int i = 0;
        if (tv != null && tv.getSpanned() != null) {
            MyImageSpan[] spans = (MyImageSpan[]) tv.getSpanned().getSpans(0, tv.getSpanned().length() - 1, MyImageSpan.class);
            int length = spans.length;
            while (i < length) {
                spans[i].mDrawable = null;
                i++;
            }
            System.gc();
        }
    }

    private static float getSpFloatOrIntValue(SharedPreferences sp, String key, float defValue) {
        try {
            return sp.getFloat(key, defValue);
        } catch (Exception e) {
            try {
                return (float) sp.getInt(key, (int) defValue);
            } catch (Exception e2) {
                error(e2);
                sp.edit().putFloat(key, defValue).commit();
                return defValue;
            }
        }
    }

    public static boolean isCloudLoggined() {
        switch (files_from) {
            case 2:
            case 4:
                return getDropbox().isLoggined;
            default:
                return false;
        }
    }

    public static void showToast(final Activity act, final String toast, final int duration) {
        if (act != null) {
            act.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(act, toast, duration).show();
                }
            });
        }
    }

    public static void draw4DragPoint(Canvas c, float x1, float y1, float x2, float y2) {
        Paint p = new Paint();
        p.setColor(-1722460843);
        p.setAntiAlias(true);
        p.setStyle(Style.STROKE);
        p.setStrokeWidth((float) d(1.0f));
        drawDragPoint(c, x1, y1, p);
        drawDragPoint(c, x1, y2, p);
        drawDragPoint(c, x2, y1, p);
        drawDragPoint(c, x2, y2, p);
    }

    private static void drawDragPoint(Canvas c, float x, float y, Paint p) {
        c.drawCircle(x, y, (float) d(5.0f), p);
        c.drawCircle(x, y, (float) d(7.0f), p);
    }

    public static boolean currentChapterWithImageOnly() {
        try {
            if (ebook == null || !ebook.inited || ebook.getChapters().size() <= lastChapter || T.isNull(((Chapter) ebook.getChapters().get(lastChapter)).hasImageOnly) || isLandscape()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNightState() {
        return isNightState(true);
    }

    public static boolean isNightState(boolean checkMain) {
        if (checkMain && mainNightTheme) {
            return true;
        }
        if (T.isNull(ActivityTxt.selfPref)) {
            return false;
        }
        if (ActivityTxt.selfPref.pdf == null) {
            return lastTheme.equals(NIGHT_THEME);
        }
        if (pdf_theme != 1) {
            return false;
        }
        return true;
    }

    public static String theEndText() {
        if (theEndText == null) {
            theEndText = "~~~ " + getContext().getString(R.string.end_of_book) + " ~~~";
        }
        return theEndText;
    }

    public static boolean isBlankChar(char c) {
        return c == ' ' || c == ' ';
    }

    public static boolean isSpaceChar(char c) {
        return isBlankChar(c) || c == INDENT_CHAR;
    }

    public static boolean isEmptyChar(char c) {
        return isSpaceChar(c) || c == '\n';
    }

    public static boolean isEmtpyText(CharSequence text, int start, int end) {
        int i = start;
        while (i < end) {
            if (i < text.length() && !isEmptyChar(text.charAt(i))) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean dualPageEnabled() {
        return landscape2PageMode && !isInAutoScroll && !isSpeaking && isLandscape();
    }

    public static int minDualPageMargin() {
        return isSuperTablet() ? d(50.0f) : d(25.0f);
    }

    public static void enlargeTabletTextSize(ViewGroup vg, int add) {
        if (isTablet) {
            for (int i = 0; i < vg.getChildCount(); i++) {
                View v = vg.getChildAt(i);
                if (v instanceof ViewGroup) {
                    enlargeTabletTextSize((ViewGroup) v, add);
                } else if (v instanceof TextView) {
                    TextView tv = (TextView) v;
                    float size = tv.getPaint().getTextSize();
                    ViewGroup.LayoutParams lp = v.getLayoutParams();
                    if (!(lp.width == -1 || lp.width == -2)) {
                        lp.width = (int) ((((float) lp.width) * (((float) add) + size)) / size);
                    }
                    if (!(lp.height == -1 || lp.height == -2)) {
                        lp.height = (int) ((((float) lp.height) * (((float) add) + size)) / size);
                    }
                    tv.getPaint().setTextSize(((float) add) + size);
                }
            }
        }
    }

    public static void drawSquiggly(Canvas c, float x1, float y1, float x2, float y2, Paint p, float distance) {
        p.setAntiAlias(true);
        float w = distance;
        float x = x1;
        boolean down = true;
        float off = w / CSS.FLOAT_ADDED;
        while (x < (w / 2.0f) + x2) {
            if (down) {
                c.drawLine(x, y1, (x + w) + off, (y2 + w) + off, p);
            } else {
                c.drawLine(x, (y1 + w) + off, (x + w) + off, y2, p);
            }
            x += w;
            down = !down;
            if (x > x2) {
                return;
            }
        }
    }

    public static void setTextViewWithTheme(TextView tv) {
        tv.setTextColor(fontColor);
        tv.setTextSize(fontSize);
        tv.setTypeface(getTypeFace(fontName, 0));
        tv.getPaint().setFakeBoldText(fontBold);
        tv.getPaint().setUnderlineText(fontUnderline);
        tv.getPaint().setTextSkewX(fontItalic ? -0.25f : 0.0f);
    }

    public static void updateWidget(Context context, boolean historyOnly) {
        if (VERSION.SDK_INT >= 11) {
            if (historyOnly) {
                if (first_history_file == null || !first_history_file.equals(lastFile)) {
                    first_history_file = lastFile;
                } else {
                    return;
                }
            }
            AppWidgetManager manager = AppWidgetManager.getInstance(getContext());
            int[] a = manager.getAppWidgetIds(new ComponentName(getContext(), WidgetProvider.class));
            if (a.length > 0) {
                if (!historyOnly) {
                    try {
                        Intent intent = new Intent(context, WidgetProvider.class);
                        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                        intent.putExtra("appWidgetIds", a);
                        context.sendBroadcast(intent);
                    } catch (Exception e) {
                        error(e);
                        return;
                    }
                }
                AppWidgetManager.class.getMethod("notifyAppWidgetViewDataChanged", new Class[]{int[].class, Integer.TYPE}).invoke(manager, new Object[]{a, Integer.valueOf(R.id.listView1)});
            }
        }
    }

    public static boolean isKitkatExtSdcardFile(String filename) {
        if (VERSION.SDK_INT < 19) {
            return false;
        }
        if (SD.isSDFile(filename)) {
            return true;
        }
        filename = filename.toLowerCase();
        if (filename.indexOf("extsdcard/") != -1) {
            return true;
        }
        if (filename.indexOf("external_sd/") != -1) {
            return true;
        }
        if (filename.indexOf("ext_card/") != -1) {
            return true;
        }
        if (filename.indexOf("sdcard2/") != -1) {
            return true;
        }
        if (filename.indexOf("external_sd/") != -1) {
            return true;
        }
        if (filename.indexOf("extsd/") != -1) {
            return true;
        }
        if (isLollipopExtSdcardFile(filename)) {
            return true;
        }
        return false;
    }

    public static boolean isLollipopPermissionError() {
        return (VERSION.SDK_INT < 21 || lastErrorInfo == null || lastErrorInfo.indexOf("Permission denied") == -1) ? false : true;
    }

    public static boolean isLollipopExtSdcardFile(String filename) {
        boolean z = false;
        if (VERSION.SDK_INT >= 21) {
            try {
                if (!filename.toLowerCase().startsWith("/sdcard/")) {
                    z = Environment.isExternalStorageRemovable(new File(filename));
                }
            } catch (Exception e) {
                error(e);
            }
        }
        return z;
    }

    public static CharSequence deleteLineBreaks(CharSequence text) {
        if (text.length() == 0) {
            return text;
        }
        int end = text.length();
        while (end > 0 && text.charAt(end - 1) == '\n') {
            end--;
        }
        if (end != text.length()) {
            return text.subSequence(0, end);
        }
        return text;
    }

    public static boolean isNetworkConnecting() {
        Activity a = !T.isNull(ActivityMain.selfPref) ? ActivityMain.selfPref : ActivityTxt.selfPref;
        return !T.isNull(a) && T.isNetworkConnecting(a);
    }

    public static boolean isMultiWindow() {
        int w1 = getScreenWidth();
        int w2 = baseFrame.getWidth();
        return w1 > w2 && w1 - w2 > d(20.0f);
    }

    public static void forceNightTextColors(View v) {
        if (v instanceof GridView) {
            v.setBackgroundColor(-7829368);
        }
        if ((v instanceof Spinner) && VERSION.SDK_INT >= 11) {
            ((Spinner) v).setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (parentView.getChildAt(0) != null) {
                        ((TextView) parentView.getChildAt(0)).setTextColor(-2236963);
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } else if (v instanceof TextView) {
            TextView tv = (TextView) v;
            if ((v instanceof Button) && !(v instanceof CompoundButton)) {
                v.setBackgroundResource(R.drawable.btn_night_state);
                if (v.getTag() == null) {
                    ((Button) v).setTextSize(16.0f);
                }
            }
            if (v instanceof EditText) {
                ((EditText) v).setHintTextColor(-10395295);
            }
            if (!isWhiteFont(tv.getCurrentTextColor())) {
                tv.setTextColor(getContext().getResources().getColor(R.color.material_grey_200));
            }
        } else if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                forceNightTextColors(vg.getChildAt(i));
            }
        }
    }

    public static void forceDayTextColors(View v) {
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            if (!isWhiteFont(tv.getCurrentTextColor())) {
                return;
            }
            if (v instanceof EditText) {
                tv.setTextColor(getContext().getResources().getColor(R.color.dialog_edit_text));
            } else {
                tv.setTextColor(getContext().getResources().getColor(R.color.dialog_second_text));
            }
        } else if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                forceDayTextColors(vg.getChildAt(i));
            }
        }
    }

    public static void setDialogNightState(View root) {
        if (isNightState() && root != null) {
            View background = root.findViewById(R.id.sv);
            if (background != null) {
                background.setBackgroundColor(-11513776);
            }
            View header = root.findViewById(R.id.include1);
            if (header != null) {
                header.setBackgroundColor(-10066330);
            }
            View bottom = root.findViewById(R.id.optionsBottomLay);
            if (bottom != null) {
                bottom.setBackgroundColor(-10461088);
            }
            forceNightTextColors(root);
        }
    }

    public static Locale getLocale() {
        return getContext().getResources().getConfiguration().locale;
    }

    public static String getWikiUrl() {
        return "http://" + localeLanguage + ".wikipedia.org/wiki/%s";
    }

    public static Activity getAct() {
        return getAct(false);
    }

    public static Activity getAct(boolean main) {
        return main ? !T.isNull(ActivityMain.selfPref) ? ActivityMain.selfPref : ActivityTxt.selfPref : !T.isNull(ActivityTxt.selfPref) ? ActivityTxt.selfPref : ActivityMain.selfPref;
    }

    public static int playServiceState() {
        if (serviceInstalledState == null || serviceInstalledState.intValue() != 0) {
            try {
                long t = SystemClock.currentThreadTimeMillis();
                serviceInstalledState = Integer.valueOf(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext()));
                log("service checked time:" + (SystemClock.currentThreadTimeMillis() - t) + ", state:" + serviceInstalledState);
            } catch (Exception e) {
                error(e);
                serviceInstalledState = Integer.valueOf(8);
            }
        }
        return serviceInstalledState.intValue();
    }

    public static boolean showPlayServiceErrorInfo(Activity act) {
        if (playServiceState() == 0) {
            return false;
        }
        try {
            if (isAmazonVersion) {
                new Builder(act).setTitle((int) R.string.error).setMessage(GooglePlayServicesUtil.getErrorString(playServiceState()) + "\nPlease install the latest version of Google Play services firstly.").setPositiveButton(17039370, null).show();
            } else {
                log("state:" + playServiceState() + "," + GooglePlayServicesUtil.getErrorString(playServiceState()));
                GooglePlayServicesUtil.getErrorDialog(playServiceState(), act, 0).show();
            }
        } catch (Exception e) {
            error(e);
            try {
                new AlertDialog.Builder(act).setTitle("Error!").setMessage(GooglePlayServicesUtil.getErrorString(playServiceState()) + "\nPlease install the latest version of Google Play services firstly.").setPositiveButton(17039370, null).show();
            } catch (Exception e2) {
                error(e2);
            }
        }
        return true;
    }

    public static int getBookmarkColor() {
        return bookmarkNoColor ? 0 : bookmark_color;
    }
}