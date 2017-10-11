package com.flyersoft.moonreader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils.TruncateAt;
import android.text.format.Formatter;
import android.text.format.Time;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ZoomControls;
import com.flyersoft.books.A;
import com.flyersoft.books.A.AfterFlipCurl;
import com.flyersoft.books.A.Bookmark;
import com.flyersoft.books.A.Bookmarks;
import com.flyersoft.books.A.TtsLine;
import com.flyersoft.books.A.TxTChapter;
import com.flyersoft.books.BaseEBook;
import com.flyersoft.books.BaseEBook.AfterGetUnReadWords;
import com.flyersoft.books.BaseEBook.Chapter;
import com.flyersoft.books.BaseEBook.FootNote;
import com.flyersoft.books.BookDb;
import com.flyersoft.books.BookDb.BookInfo;
import com.flyersoft.books.BookDb.NoteInfo;
import com.flyersoft.books.BookDb.ReadStatistics;
import com.flyersoft.books.T;
import com.flyersoft.books.T.OnUrlClick;
import com.flyersoft.components.CSS;
import com.flyersoft.components.CSS.PAGE_BREAK;
import com.flyersoft.components.CSS.Style;
import com.flyersoft.components.ColorDialog;
import com.flyersoft.components.ColorDialog.OnSaveColor;
import com.flyersoft.moonreader.PrefSelectHighlight.OnSelectColor;
import com.flyersoft.staticlayout.MRTextView;
import com.flyersoft.staticlayout.MRTextView.MarginF;
import com.flyersoft.staticlayout.MyLayout;
import com.flyersoft.staticlayout.MyUrlSpan;
import com.flyersoft.staticlayout.PicGalleryShow;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityTxt extends AppCompatActivity implements Runnable, OnTouchListener, OnClickListener, OnLongClickListener {
    private static final int AUTO_SCROLL_MESSAGE = 301;
    public static final int BOOK_INFO_COUNT_DONE = 962;
    public static final int CHECK_BLUELIGHT_FILTER = 958;
    public static final int CHECK_BOTTOM_HALF_LINE = 954;
    public static final int CHECK_DUAL_PAGE = 950;
    public static final int DELAY_CACHE_SHOT1 = 9042;
    public static final int DELAY_CACHE_SHOT2 = 9043;
    public static final int DELAY_CURL3D_SHOT2 = 904;
    public static final int DELAY_CURL3D_TOUCH_UP = 906;
    public static final int DELAY_FLIP_CLICK = 907;
    public static final int DELAY_HIDE_CURL_FLIPVIEW = 9044;
    public static final int DO_FINISHED = 960;
    public static final int DROPBOX_AUTH_AGAIN = 802;
    public static final int ERROR_FINISHED = 961;
    private static final int FOOTNOTE_LONG_TIME_TAP_EVENT = 502;
    public static final int FORCEFITHARDWAREACCELERATE = 957;
    public static final int GET_CLOUD_POSITION = 801;
    public static final int GPU_SHADOW = 956;
    public static final int INIT_CURL3D = 851;
    public static final int INIT_CURL3D_RENDER = 850;
    private static final int LANGUAGE_CHECK = 601;
    private static final int LONG_TIME_TAP_EVENT = 501;
    public static final int LOOP_EVENT_CHECK = 902;
    public static final int PAGE_DOWN = 1;
    public static final int PAGE_DOWN_TO_NEXT_CHAPTER = 909;
    public static final int PAGE_UP = -1;
    public static final int PDF_MISS_FONT = 955;
    private static final int PRETEXT_SHOW = 201;
    public static final int PRE_LOAD_NEXT_CHAPTER = 910;
    public static final int REFRESH_EBOOK_REMAINING_TIME = 901;
    public static final int RESTART_CONFIRM_MESSAGE = 921;
    public static final int SCROLL_NO_DELAY = 951;
    private static final int SCROLL_TXT_AGAIN = 202;
    public static final int SET_FONT_SIZE = 952;
    public static final int SET_MARGIN = 953;
    private static final int SHOW_EBOOK = 103;
    private static final int SHOW_EBOOK_COVER = 104;
    private static final int SHOW_HTML = 102;
    private static final int SHOW_PDF = 105;
    private static final int SHOW_PDF2 = 106;
    private static final int SHOW_SCROLLBAR = 401;
    private static final int SHOW_TXT = 101;
    public static final int SPEAK_PDF_TEXT_REFLOW = 920;
    public static final int START_AUTOSCROLL_EVENT = 959;
    public static final int SYNC_FINISHED = 922;
    public static ActivityTxt selfPref;
    private final int CHECK_PDF_SPEAKING_POS = 4;
    boolean LedChanged;
    private final int RESTORE_SPEAKING = 5;
    private final int SPEAK_CHECK_PAGE = 2;
    private final int SPEAK_PAGE_DOWN = 0;
    private final int SPEAK_PAGE_UP = 1;
    private String SPEAK_PUNCT1 = ",.;?\":(){}[]!，。；＂”“：？（）、！\n";
    private final int STOP_SPEAK = 3;
    private final int TIP_TYPE_AUTOSCROLL = 1;
    private final int TIP_TYPE_TTS = 0;
    private int WEB_SCROLL_OFF;
    private long activeTime;
    Handler android22Handle;
    long annotEndTime;
    ImageView antArrow;
    ImageView antCancel;
    ImageView antEllipse;
    ImageView antFreeText;
    ImageView antInk;
    ImageView antLine;
    ImageView antNote;
    ImageView antRect;
    ImageView antSelected;
    ImageView apCancel;
    ImageView apColor;
    ImageView apFillColor;
    ImageView apNote;
    ImageView apThickness;
    private boolean askedForTilt;
    OnAudioFocusChangeListener audioChangeListener;
    boolean audio_focus_lossed;
    boolean audio_pause_from_listener;
    private boolean autoScrollPaused;
    private int autoScrollTimesCount;
    ImageButton b_autoscroll;
    ImageButton b_bookmark;
    ImageButton b_brightness;
    ImageButton b_chapter;
    ImageButton b_control;
    ImageButton b_daynight;
    ImageButton b_fontsize;
    ImageButton b_misc;
    ImageButton b_option;
    ImageButton b_orientation;
    ImageButton b_search;
    ImageButton b_shutdown;
    ImageButton b_speak;
    ImageButton b_tilt;
    ImageButton b_visual;
    View backLay;
    public FrameLayout baseFrame;
    String battery;
    BroadcastReceiver batteryReceiver;
    private float birghtnessPos = -1.0f;
    CheckBox bluelightCheck;
    SeekBar bluelightSK;
    public int bookBackgroundStyle;
    private int bookType = -1;
    View bottomLay;
    CheckBox brightAutoCheck;
    View brightnessPanel;
    SeekBar brightnessSK;
    private boolean brightnessSetted = false;
    View buttonsLay;
    int cPos = 0;
    private int cacheScrollY;
    private String cacheTxtText;
    HashMap<Integer, Integer> cachedSplitsPageCount;
    HashMap<Integer, Integer> cachedSplitsPageNum;
    long cachingPageShotsTime;
    private boolean cancelFade;
    private long changeChapterTime = -1;
    PrefChapters chapterDlg;
    private boolean checkUrlFootnote;
    public boolean checkedBottomHalfLine;
    public boolean checkhBarWidth;
    ImageView chromeIv;
    public ColorTemplate colorTemplate;
    public ContentLay contentLay;
    private float continueMoveX;
    private String cssBodyBackgrondImage;
    private Builder cssFontDlg;
    private Float cssFontSize;
    private Handler curl2dHandler;
    public NewCurl3D curl3d;
    public FlipImageView curl3dCover;
    boolean curl3dCoverShowed;
    public FrameLayout curl3dLay;
    Message curlDelayedMsg;
    private boolean curlDownMsgSending;
    long current;
    private long currentPage;
    private boolean dPadKeyPressed;
    boolean delayCache;
    public boolean dontShowCover = false;
    View dot;
    public DotImageView dot1;
    public DotImageView dot2;
    public boolean dragToSelectText;
    public View dualPageBackground;
    private boolean ebookChapterDown = false;
    private boolean ebookPageUp = false;
    private boolean ebook_inLoading = false;
    boolean fileNotExists;
    int fileProgress = 0;
    int first_select_pos;
    private boolean flingInited = false;
    public FlipImageView flipView;
    private Handler flip_handler;
    public long flippingAnimationTime;
    ZoomControls fontZoom;
    private float fontsizePos = -1.0f;
    private Dialog footnoteDlg;
    private Builder footnoteDlg2;
    public boolean footnoteLongTimeTapEvent = false;
    private String footnoteStr;
    private boolean forceDelayFlipAnimation;
    private boolean forceDisableCurlEvent;
    float forceTiltSpeed;
    private boolean force_tts_not_stop;
    private boolean force_tts_stop;
    private int force_utteranceId = -2;
    private boolean getting3dCurlShot;
    long goToLastTime;
    private boolean gotCurlImage;
    public HighlightLay hBar;
    View hBar2;
    float hBarFontSize;
    TextView hCopy;
    TextView hDict;
    MotionEvent hDownEvent;
    TextView hHighlight;
    int hLastX;
    int hLastY;
    TextView hMore;
    MotionEvent hMotionEvent;
    TextView hNote;
    OnClickListener hOnClick;
    OnTouchListener hOnTouch;
    ImageView hPen;
    public Handler handler = new Handler() {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r14) {
            /*
            r13 = this;
            r1 = r14.what;
            r2 = 960; // 0x3c0 float:1.345E-42 double:4.743E-321;
            if (r1 != r2) goto L_0x000c;
        L_0x0006:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;
            r1.doFinish();
        L_0x000b:
            return;
        L_0x000c:
            r1 = r14.what;
            r2 = 961; // 0x3c1 float:1.347E-42 double:4.75E-321;
            if (r1 != r2) goto L_0x0024;
        L_0x0012:
            r3 = com.flyersoft.moonreader.ActivityTxt.this;
            r1 = r14.obj;
            r1 = (java.lang.String) r1;
            r2 = r14.arg1;
            r4 = 1;
            if (r2 != r4) goto L_0x0022;
        L_0x001d:
            r2 = 1;
        L_0x001e:
            r3.error_and_exit(r1, r2);
            goto L_0x000b;
        L_0x0022:
            r2 = 0;
            goto L_0x001e;
        L_0x0024:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;
            r1 = r1.isFinishing();
            if (r1 != 0) goto L_0x000b;
        L_0x002c:
            r1 = r14.what;	 Catch:{ Exception -> 0x006f }
            switch(r1) {
                case 101: goto L_0x0032;
                case 102: goto L_0x00c2;
                case 103: goto L_0x0180;
                case 104: goto L_0x0179;
                case 105: goto L_0x031e;
                case 106: goto L_0x032c;
                case 201: goto L_0x018d;
                case 202: goto L_0x0199;
                case 301: goto L_0x02bd;
                case 401: goto L_0x02b5;
                case 501: goto L_0x02c4;
                case 502: goto L_0x02ee;
                case 601: goto L_0x0309;
                case 801: goto L_0x0310;
                case 802: goto L_0x0317;
                case 850: goto L_0x0383;
                case 851: goto L_0x038a;
                case 901: goto L_0x0345;
                case 902: goto L_0x0356;
                case 904: goto L_0x039d;
                case 906: goto L_0x03ba;
                case 907: goto L_0x03cd;
                case 909: goto L_0x0419;
                case 910: goto L_0x0435;
                case 920: goto L_0x0446;
                case 921: goto L_0x0459;
                case 922: goto L_0x048f;
                case 950: goto L_0x04a3;
                case 951: goto L_0x03de;
                case 952: goto L_0x03e7;
                case 953: goto L_0x03ee;
                case 954: goto L_0x040b;
                case 955: goto L_0x04ec;
                case 956: goto L_0x04f3;
                case 957: goto L_0x04fb;
                case 958: goto L_0x0502;
                case 959: goto L_0x0518;
                case 962: goto L_0x05c4;
                case 9042: goto L_0x03a5;
                case 9043: goto L_0x03ac;
                case 9044: goto L_0x03b3;
                case 50001: goto L_0x033a;
                default: goto L_0x0031;
            };	 Catch:{ Exception -> 0x006f }
        L_0x0031:
            goto L_0x000b;
        L_0x0032:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.inPreShow;	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x0074;
        L_0x0038:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.inPreShow = r2;	 Catch:{ Exception -> 0x006f }
        L_0x003d:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.resetPageCount();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.showReadProgress(r2);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.preShowTxtChapterList();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.hideProgressDlg();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.hintForBigFileOverload;	 Catch:{ Exception -> 0x006f }
            r2 = "";
            r1 = r1.equals(r2);	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x007d;
        L_0x005e:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.hintForBigFileOverload;	 Catch:{ Exception -> 0x006f }
            r3 = 1;
            com.flyersoft.books.T.showToastText(r1, r2, r3);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = "";
            r1.hintForBigFileOverload = r2;	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x006f:
            r8 = move-exception;
            com.flyersoft.books.A.error(r8);
            goto L_0x000b;
        L_0x0074:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.books.A.lastPosition;	 Catch:{ Exception -> 0x006f }
            r4 = 0;
            r1.showTxtByPosition(r2, r4);	 Catch:{ Exception -> 0x006f }
            goto L_0x003d;
        L_0x007d:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.showHintAtLoadingEnd;	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x0098;
        L_0x0083:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.showHintAtLoadingEnd = r2;	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r3 = 2131165447; // 0x7f070107 float:1.7945111E38 double:1.052935633E-314;
            r2 = r2.getString(r3);	 Catch:{ Exception -> 0x006f }
            com.flyersoft.books.T.showToastText(r1, r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0098:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.fileNotExists;	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x000b;
        L_0x009e:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x006f }
            r2.<init>();	 Catch:{ Exception -> 0x006f }
            r3 = com.flyersoft.books.A.lastFile;	 Catch:{ Exception -> 0x006f }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x006f }
            r3 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r4 = 2131165500; // 0x7f07013c float:1.7945219E38 double:1.052935659E-314;
            r3 = r3.getString(r4);	 Catch:{ Exception -> 0x006f }
            r2 = r2.append(r3);	 Catch:{ Exception -> 0x006f }
            r2 = r2.toString();	 Catch:{ Exception -> 0x006f }
            r3 = 1;
            com.flyersoft.books.T.showToastText(r1, r2, r3);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x00c2:
            r1 = com.flyersoft.books.A.useWebView;	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x00d4;
        L_0x00c6:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.hideProgressDlg();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = "";
            r1.showInWebView(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x00d4:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.fileNotExists;	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x0160;
        L_0x00da:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.isTxtScrollReady;	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x00f5;
        L_0x00e0:
            r2 = com.flyersoft.books.A.lastPosition;	 Catch:{ Exception -> 0x006f }
            r4 = 0;
            r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r1 <= 0) goto L_0x00f5;
        L_0x00e8:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.txtView;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.txtView2;	 Catch:{ Exception -> 0x006f }
            r3 = 1;
            r2.disableDraw = r3;	 Catch:{ Exception -> 0x006f }
            r1.disableDraw = r3;	 Catch:{ Exception -> 0x006f }
        L_0x00f5:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x0152 }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x0152 }
            r2 = r2.htmlText;	 Catch:{ Exception -> 0x0152 }
            r2 = com.flyersoft.books.A.adjustChapterHtml(r2);	 Catch:{ Exception -> 0x0152 }
            r1.htmlSrc = r2;	 Catch:{ Exception -> 0x0152 }
            r1 = com.flyersoft.books.A.noSplitHtmls();	 Catch:{ Exception -> 0x0152 }
            if (r1 != 0) goto L_0x0124;
        L_0x0107:
            r1 = com.flyersoft.books.A.lastSplitIndex;	 Catch:{ Exception -> 0x0152 }
            r2 = com.flyersoft.books.A.splitHtmls;	 Catch:{ Exception -> 0x0152 }
            r2 = r2.size();	 Catch:{ Exception -> 0x0152 }
            r2 = r2 + -1;
            if (r1 <= r2) goto L_0x0116;
        L_0x0113:
            r1 = 0;
            com.flyersoft.books.A.lastSplitIndex = r1;	 Catch:{ Exception -> 0x0152 }
        L_0x0116:
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x0152 }
            r1 = com.flyersoft.books.A.splitHtmls;	 Catch:{ Exception -> 0x0152 }
            r3 = com.flyersoft.books.A.lastSplitIndex;	 Catch:{ Exception -> 0x0152 }
            r1 = r1.get(r3);	 Catch:{ Exception -> 0x0152 }
            r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x0152 }
            r2.htmlSrc = r1;	 Catch:{ Exception -> 0x0152 }
        L_0x0124:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x0152 }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x0152 }
            r2 = r2.htmlSrc;	 Catch:{ Exception -> 0x0152 }
            r2 = com.flyersoft.books.A.chineseJianFanConvert(r2);	 Catch:{ Exception -> 0x0152 }
            r1.htmlSrc = r2;	 Catch:{ Exception -> 0x0152 }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x0152 }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x0152 }
            r2 = r2.htmlSrc;	 Catch:{ Exception -> 0x0152 }
            r3 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x0152 }
            r3 = r3.createHtmlBookImageGetter();	 Catch:{ Exception -> 0x0152 }
            r4 = -1;
            r2 = com.flyersoft.staticlayout.MyHtml.fromHtml(r2, r3, r4);	 Catch:{ Exception -> 0x0152 }
            r1.txtViewSetText(r2);	 Catch:{ Exception -> 0x0152 }
        L_0x0144:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.books.A.lastPosition;	 Catch:{ Exception -> 0x006f }
            r1.txtFileFinalScrollAtStartup(r2);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.hideProgressDlg();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0152:
            r8 = move-exception;
            com.flyersoft.books.A.error(r8);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.htmlText;	 Catch:{ Exception -> 0x006f }
            r1.txtViewSetText(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x0144;
        L_0x0160:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r3 = 2131165500; // 0x7f07013c float:1.7945219E38 double:1.052935659E-314;
            r4 = 1;
            r4 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x006f }
            r5 = 0;
            r6 = com.flyersoft.books.A.lastFile;	 Catch:{ Exception -> 0x006f }
            r4[r5] = r6;	 Catch:{ Exception -> 0x006f }
            r2 = r2.getString(r3, r4);	 Catch:{ Exception -> 0x006f }
            r3 = 1;
            com.flyersoft.books.T.showToastText(r1, r2, r3);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0179:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.show_ebook_cover();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0180:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.handler;	 Catch:{ Exception -> 0x006f }
            r2 = 202; // 0xca float:2.83E-43 double:1.0E-321;
            r4 = 30;
            r1.sendEmptyMessageDelayed(r2, r4);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x018d:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.hideProgressDlg();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.showPreText();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0199:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r7 = r1.initClickTip(r2);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.getBookType();	 Catch:{ Exception -> 0x006f }
            switch(r1) {
                case 0: goto L_0x01f8;
                case 1: goto L_0x01f8;
                case 100: goto L_0x0223;
                default: goto L_0x01a9;
            };	 Catch:{ Exception -> 0x006f }
        L_0x01a9:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.getBookType();	 Catch:{ Exception -> 0x006f }
            r2 = 100;
            if (r1 == r2) goto L_0x01b8;
        L_0x01b3:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.hideProgressDlg();	 Catch:{ Exception -> 0x006f }
        L_0x01b8:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.resetPageCount();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.ebook_inLoading = r2;	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.isTxtScrollReady;	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x01d3;
        L_0x01c9:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 1;
            r1.isTxtScrollReady = r2;	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.loadNotes();	 Catch:{ Exception -> 0x006f }
        L_0x01d3:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.pdfHideLay();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.txtLay;	 Catch:{ Exception -> 0x006f }
            r1 = r1.getVisibility();	 Catch:{ Exception -> 0x006f }
            r2 = 8;
            if (r1 != r2) goto L_0x01ec;
        L_0x01e4:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.txtLay;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.setVisibility(r2);	 Catch:{ Exception -> 0x006f }
        L_0x01ec:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.init3dHandler();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.checkScreenAwake();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x01f8:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.txtView;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.txtView2;	 Catch:{ Exception -> 0x006f }
            r3 = 0;
            r2.disableDraw = r3;	 Catch:{ Exception -> 0x006f }
            r1.disableDraw = r3;	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.txtView;	 Catch:{ Exception -> 0x006f }
            r9 = r1.getLayout();	 Catch:{ Exception -> 0x006f }
            if (r9 == 0) goto L_0x000b;
        L_0x020f:
            r1 = r14.arg1;	 Catch:{ Exception -> 0x006f }
            r10 = r9.getLineForOffset(r1);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.txtView;	 Catch:{ Exception -> 0x006f }
            r12 = r1.getLineTop2(r10);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.txtScrollTo(r12);	 Catch:{ Exception -> 0x006f }
            goto L_0x01a9;
        L_0x0223:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.checkStatusBar();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.books.A.isEBookOK();	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x02a7;
        L_0x022e:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.isDrmBook();	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x000b;
        L_0x0236:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.isTxtScrollReady;	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x02a5;
        L_0x023c:
            r1 = com.flyersoft.books.A.lastChapter;	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x02a5;
        L_0x0240:
            r2 = com.flyersoft.books.A.lastPosition;	 Catch:{ Exception -> 0x006f }
            r4 = 0;
            r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r1 != 0) goto L_0x02a5;
        L_0x0248:
            r0 = 1;
        L_0x0249:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.isTxtScrollReady;	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x0264;
        L_0x024f:
            r2 = com.flyersoft.books.A.lastPosition;	 Catch:{ Exception -> 0x006f }
            r4 = 0;
            r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
            if (r1 <= 0) goto L_0x0264;
        L_0x0257:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.txtView;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.txtView2;	 Catch:{ Exception -> 0x006f }
            r3 = 1;
            r2.disableDraw = r3;	 Catch:{ Exception -> 0x006f }
            r1.disableDraw = r3;	 Catch:{ Exception -> 0x006f }
        L_0x0264:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.books.A.lastChapter;	 Catch:{ Exception -> 0x006f }
            r3 = com.flyersoft.books.A.lastSplitIndex;	 Catch:{ Exception -> 0x006f }
            r4 = com.flyersoft.books.A.lastPosition;	 Catch:{ Exception -> 0x006f }
            r6 = 1;
            r1.showEBookByPosition(r2, r3, r4, r6);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.ignoreChapterListAtBegin;	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x0294;
        L_0x0276:
            if (r0 == 0) goto L_0x0294;
        L_0x0278:
            r1 = com.flyersoft.books.A.ebook;	 Catch:{ Exception -> 0x006f }
            r1 = r1.showChaptersAtBegin();	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x0294;
        L_0x0280:
            r1 = com.flyersoft.books.A.ebook;	 Catch:{ Exception -> 0x006f }
            r1 = r1.getChapters();	 Catch:{ Exception -> 0x006f }
            r1 = r1.size();	 Catch:{ Exception -> 0x006f }
            r2 = 3;
            if (r1 <= r2) goto L_0x0294;
        L_0x028d:
            if (r7 != 0) goto L_0x0294;
        L_0x028f:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.do_show_chapters();	 Catch:{ Exception -> 0x006f }
        L_0x0294:
            if (r0 == 0) goto L_0x01a9;
        L_0x0296:
            r1 = com.flyersoft.books.A.getFileType();	 Catch:{ Exception -> 0x006f }
            r2 = 6;
            if (r1 != r2) goto L_0x01a9;
        L_0x029d:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.inverseLayoutVisible(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x01a9;
        L_0x02a5:
            r0 = 0;
            goto L_0x0249;
        L_0x02a7:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.hideProgressDlg();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r3 = 1;
            r1.error_and_exit(r2, r3);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x02b5:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.showScrollbarProgress(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x02bd:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.do_autoscroll_message();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x02c4:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.longTimeTapEventSent = r2;	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.longTimeTapEvent;	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x000b;
        L_0x02d0:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.pdfAnnotStart;	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x000b;
        L_0x02d6:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.longTimeTapEvent = r2;	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.hMotionEvent;	 Catch:{ Exception -> 0x006f }
            r1 = r1.stopLongTap(r2);	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x000b;
        L_0x02e7:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.doLongTimeTapEvent();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x02ee:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.handler;	 Catch:{ Exception -> 0x006f }
            r2 = 502; // 0x1f6 float:7.03E-43 double:2.48E-321;
            r1.removeMessages(r2);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.footnoteLongTimeTapEvent;	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x000b;
        L_0x02fd:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.footnoteLongTimeTapEvent = r2;	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.copyFootnoteToClipboard(r14);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0309:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            com.flyersoft.books.A.setLanguage(r1);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0310:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.showHintForCloudPositionSync();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0317:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.needAuthAgainForDropbox();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x031e:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.books.A.lastFile;	 Catch:{ Exception -> 0x006f }
            r1.pdfOpen(r2);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.init3dHandler();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x032c:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.hideProgressDlg();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.books.A.lastFile;	 Catch:{ Exception -> 0x006f }
            r1.pdfOpen2(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x033a:
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r14.obj;	 Catch:{ Exception -> 0x006f }
            r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x006f }
            r2.showCloudAsyncMessage(r1);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0345:
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r3 = r14.arg1;	 Catch:{ Exception -> 0x006f }
            r1 = r14.arg1;	 Catch:{ Exception -> 0x006f }
            r4 = -1;
            if (r1 != r4) goto L_0x0354;
        L_0x034e:
            r1 = 1;
        L_0x034f:
            r2.refresh_ebook_remaining_time(r3, r1);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0354:
            r1 = 0;
            goto L_0x034f;
        L_0x0356:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.isFinishing();	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x000b;
        L_0x035e:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.checkRemindEvent();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.checkTiltEvent();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.checkScreenAwake();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.handler;	 Catch:{ Exception -> 0x006f }
            r2 = 902; // 0x386 float:1.264E-42 double:4.456E-321;
            r1.removeMessages(r2);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.handler;	 Catch:{ Exception -> 0x006f }
            r2 = 902; // 0x386 float:1.264E-42 double:4.456E-321;
            r4 = 30000; // 0x7530 float:4.2039E-41 double:1.4822E-319;
            r1.sendEmptyMessageDelayed(r2, r4);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0383:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.initCurl3dRender();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x038a:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.initCurl3d(r2);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.handler;	 Catch:{ Exception -> 0x006f }
            r2 = 850; // 0x352 float:1.191E-42 double:4.2E-321;
            r4 = 50;
            r1.sendEmptyMessageDelayed(r2, r4);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x039d:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.get3dCurlShot(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x03a5:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.createCachePageShots();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x03ac:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.createCachePageShots2();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x03b3:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.endCachingShot();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x03ba:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r3 = r14.arg1;	 Catch:{ Exception -> 0x006f }
            r4 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r4 = r4.hMotionEvent;	 Catch:{ Exception -> 0x006f }
            r2 = r2.createCurlMsg(r3, r4);	 Catch:{ Exception -> 0x006f }
            r1.handleCurlMessage(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x03cd:
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.pageDirection;	 Catch:{ Exception -> 0x006f }
            r3 = 1;
            if (r1 != r3) goto L_0x03dc;
        L_0x03d6:
            r1 = 1;
        L_0x03d7:
            r2.showPageAnimation(r1);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x03dc:
            r1 = 0;
            goto L_0x03d7;
        L_0x03de:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r14.arg1;	 Catch:{ Exception -> 0x006f }
            r1.txtScrollNoDelay(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x03e7:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.setFontSizeHandler();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x03ee:
            r1 = com.flyersoft.books.A.txtScroll;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            com.flyersoft.books.A.setTxtScrollMargin(r1, r2);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.dualPageEnabled();	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x0402;
        L_0x03fc:
            r1 = com.flyersoft.books.A.txtScroll2;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            com.flyersoft.books.A.setTxtScrollMargin(r1, r2);	 Catch:{ Exception -> 0x006f }
        L_0x0402:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.contentLay;	 Catch:{ Exception -> 0x006f }
            r1.postInvalidate();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x040b:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.checkBottomHalfLineHandler();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.contentLay;	 Catch:{ Exception -> 0x006f }
            r1.postInvalidate();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0419:
            r1 = com.flyersoft.books.A.isSpeaking;	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x042c;
        L_0x041d:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.isEndOfBook();	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x042c;
        L_0x0425:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.stop_speak();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x042c:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r14.arg1;	 Catch:{ Exception -> 0x006f }
            r1.pageDownToNextChapter(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0435:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.handler;	 Catch:{ Exception -> 0x006f }
            r2 = 910; // 0x38e float:1.275E-42 double:4.496E-321;
            r1.removeMessages(r2);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.preNextChapter(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0446:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.getSpeakText();	 Catch:{ Exception -> 0x006f }
            r1.speakLines = r2;	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.setSpeakQueue(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0459:
            r1 = new com.flyersoft.components.MyDialog$Builder;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.<init>(r2);	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.books.A.getContext();	 Catch:{ Exception -> 0x006f }
            r3 = 2131165373; // 0x7f0700bd float:1.7944961E38 double:1.0529355964E-314;
            r2 = r2.getString(r3);	 Catch:{ Exception -> 0x006f }
            r2 = r1.setTitle(r2);	 Catch:{ Exception -> 0x006f }
            r1 = r14.obj;	 Catch:{ Exception -> 0x006f }
            r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x006f }
            r1 = r2.setMessage(r1);	 Catch:{ Exception -> 0x006f }
            r2 = 17039370; // 0x104000a float:2.42446E-38 double:8.4185673E-317;
            r3 = 0;
            r1 = r1.setPositiveButton(r2, r3);	 Catch:{ Exception -> 0x006f }
            r2 = 17039360; // 0x1040000 float:2.424457E-38 double:8.4185624E-317;
            r3 = new com.flyersoft.moonreader.ActivityTxt$2$1;	 Catch:{ Exception -> 0x006f }
            r3.<init>();	 Catch:{ Exception -> 0x006f }
            r1 = r1.setNegativeButton(r2, r3);	 Catch:{ Exception -> 0x006f }
            r1.show();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x048f:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.restart_after_sync_finish;	 Catch:{ Exception -> 0x006f }
            if (r1 == 0) goto L_0x000b;
        L_0x0497:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 100;
            r3 = 1;
            r4 = 50;
            r1.restartWhenLowMemory(r2, r3, r4);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x04a3:
            r1 = com.flyersoft.books.A.getScreenHeight2();	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.baseFrame;	 Catch:{ Exception -> 0x006f }
            r2 = r2.getHeight();	 Catch:{ Exception -> 0x006f }
            if (r1 > r2) goto L_0x04bf;
        L_0x04b1:
            r1 = com.flyersoft.books.A.getScreenHeight2();	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.baseFrame;	 Catch:{ Exception -> 0x006f }
            r2 = r2.getWidth();	 Catch:{ Exception -> 0x006f }
            if (r1 <= r2) goto L_0x04e7;
        L_0x04bf:
            r11 = 1;
        L_0x04c0:
            if (r11 != 0) goto L_0x04c7;
        L_0x04c2:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.setTxtView2Visible();	 Catch:{ Exception -> 0x006f }
        L_0x04c7:
            if (r11 != 0) goto L_0x04ce;
        L_0x04c9:
            r1 = r14.arg1;	 Catch:{ Exception -> 0x006f }
            r2 = 1;
            if (r1 == r2) goto L_0x000b;
        L_0x04ce:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.handler;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.handler;	 Catch:{ Exception -> 0x006f }
            r3 = 950; // 0x3b6 float:1.331E-42 double:4.694E-321;
            r4 = 1;
            r5 = 1;
            r4 = r2.obtainMessage(r3, r4, r5);	 Catch:{ Exception -> 0x006f }
            if (r11 == 0) goto L_0x04e9;
        L_0x04e0:
            r2 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        L_0x04e2:
            r1.sendMessageDelayed(r4, r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x04e7:
            r11 = 0;
            goto L_0x04c0;
        L_0x04e9:
            r2 = 100;
            goto L_0x04e2;
        L_0x04ec:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.pdfShowMissFontTip();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x04f3:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = 0;
            r1.eraseGPUShadow(r2);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x04fb:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.updateForFitHardwareAccelerate();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0502:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.showShadeCoverOnView();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r3 = 2131165272; // 0x7f070058 float:1.7944756E38 double:1.0529355465E-314;
            r2 = r2.getString(r3);	 Catch:{ Exception -> 0x006f }
            r3 = 0;
            com.flyersoft.books.T.showToastText(r1, r2, r3);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x0518:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r14.arg1;	 Catch:{ Exception -> 0x006f }
            r1.txtScrollNoDelay(r2);	 Catch:{ Exception -> 0x006f }
            r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x006f }
            r1.<init>();	 Catch:{ Exception -> 0x006f }
            r2 = "start scroll, h:";
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.txtView;	 Catch:{ Exception -> 0x006f }
            r2 = r2.getHeight();	 Catch:{ Exception -> 0x006f }
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x006f }
            r2 = " y:";
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.txtScroll;	 Catch:{ Exception -> 0x006f }
            r2 = r2.getScrollY();	 Catch:{ Exception -> 0x006f }
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x006f }
            r2 = " to:";
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x006f }
            r2 = r14.arg1;	 Catch:{ Exception -> 0x006f }
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x006f }
            r1 = r1.toString();	 Catch:{ Exception -> 0x006f }
            com.flyersoft.books.A.log(r1);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.txtView;	 Catch:{ Exception -> 0x006f }
            r1 = r1.getHeight();	 Catch:{ Exception -> 0x006f }
            if (r1 != 0) goto L_0x05bd;
        L_0x0565:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.txtView;	 Catch:{ Exception -> 0x006f }
            r1 = r1.getText();	 Catch:{ Exception -> 0x006f }
            r1 = r1.length();	 Catch:{ Exception -> 0x006f }
            r2 = 1;
            if (r1 <= r2) goto L_0x05bd;
        L_0x0574:
            r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x006f }
            r1.<init>();	 Catch:{ Exception -> 0x006f }
            r2 = "*******start scroll error, h=0 to:";
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x006f }
            r2 = r14.arg1;	 Catch:{ Exception -> 0x006f }
            r1 = r1.append(r2);	 Catch:{ Exception -> 0x006f }
            r1 = r1.toString();	 Catch:{ Exception -> 0x006f }
            r2 = 1;
            com.flyersoft.books.A.log(r1, r2);	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.txtView;	 Catch:{ Exception -> 0x006f }
            r1.nullLayouts();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.txtView2;	 Catch:{ Exception -> 0x006f }
            r1.nullLayouts();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.clearTxtView();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.checkDualPageMode();	 Catch:{ Exception -> 0x006f }
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1 = r1.handler;	 Catch:{ Exception -> 0x006f }
            r2 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r2 = r2.handler;	 Catch:{ Exception -> 0x006f }
            r3 = 959; // 0x3bf float:1.344E-42 double:4.74E-321;
            r4 = r14.arg1;	 Catch:{ Exception -> 0x006f }
            r5 = 0;
            r2 = r2.obtainMessage(r3, r4, r5);	 Catch:{ Exception -> 0x006f }
            r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
            r1.sendMessageDelayed(r2, r4);	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x05bd:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.startAutoScrollEvent();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
        L_0x05c4:
            r1 = com.flyersoft.moonreader.ActivityTxt.this;	 Catch:{ Exception -> 0x006f }
            r1.do_book_info_count_done();	 Catch:{ Exception -> 0x006f }
            goto L_0x000b;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.moonreader.ActivityTxt.2.handleMessage(android.os.Message):void");
        }
    };
    private boolean hasMarginAction;
    boolean hasMediaTag;
    OpenFile_Receiver headsetReceiver;
    boolean headsetRegistered = false;
    private int highlightY;
    String hintForBigFileOverload = "";
    private int htmlFileCharCount;
    private int htmlFileWordCount;
    public String htmlSrc;
    public String htmlText = "";
    boolean ignoreChapterListAtBegin;
    long ignoreSavePosTime;
    private MyImageGetter imageGetter;
    boolean inCloudSyncing;
    boolean inPreShow = false;
    private View info_sv;
    private String info_text;
    int inversedWebLastChapter;
    int inversedWebLastY;
    private boolean isBackKeyDown = false;
    public boolean isCoverShow = false;
    private boolean isCurlClick;
    private boolean isFootnoteEpub;
    boolean isOnExiting;
    public boolean isPaused;
    private boolean isPressScreenStateButton = false;
    public boolean isTxtScrollReady;
    private boolean justStartScroll;
    private int keyDownCount = 0;
    private long lastCheckProgressTime;
    private int lastEndOfSpeakText = -1;
    public long lastFlipScrollY;
    private long lastPageCountCheckSize;
    private long lastPixelTipNumber;
    private int lastStartOfSpeakText = -1;
    long lastTapUpTime = -1;
    private int last_statistics_Chapter;
    private int last_statistics_Y;
    private long last_statistics_add_time;
    boolean layoutVisible = false;
    ImageView linkBackIv;
    private ArrayList<LINK_BACK> link_backs = new ArrayList();
    boolean locate_to_search_result;
    public boolean longTimeTapEvent = false;
    public boolean longTimeTapEventDown = false;
    private boolean longTimeTapEventSent = false;
    private GestureDetector mCurlDetector;
    private float mCurlFlingDirection;
    float mDeceleration;
    private GestureDetector mGestureDetector;
    int mMaximumVelocity;
    private int mTouchTimes = 0;
    VelocityTracker mVT;
    public Document m_doc;
    ComponentName mediaButtonReciver;
    View menuB;
    public boolean moveStart2;
    private float movingStopEventY;
    MediaPlayer mp3;
    TextView mp3Eclapsed;
    Handler mp3Handler;
    View mp3Lay;
    ImageView mp3Play;
    public int mp3PlayState;
    SeekBar mp3Seek;
    ImageView mp3Stop;
    TextView mp3Total;
    public String mp3_file;
    double multi_distance;
    boolean multi_touching;
    ImageView nav_return;
    private float noScrollDist;
    private int oldPriorChapterId;
    private int oldPriorChapterSplit;
    private CharSequence oldPriorChapterText;
    private int oldPriorChapterY;
    private String oldPriorHtmlSrc;
    private ArrayList<String> oldPriorSplitHtmls;
    private AfterFlipCurl onFlipCurlingEnd = new AfterFlipCurl() {
        public void Finished(boolean success) {
            if (!success) {
                ActivityTxt.this.flippingAnimationTime = 0;
                if (ActivityTxt.this.getBookType() == 0 && ActivityTxt.this.currentPage > 1) {
                    ActivityTxt.this.currentPage = ActivityTxt.this.currentPage - 1;
                }
                if (ActivityTxt.this.pageDirection == 1) {
                    ActivityTxt.this.curl3dCover.setTag(null);
                }
                ActivityTxt.this.pageScroll(-ActivityTxt.this.pageDirection, true, false);
            }
            ActivityTxt.this.handleCurlMessage(ActivityTxt.this.createCurlMsg(1, null));
        }
    };
    private OnInitListener onInitTTS = new OnInitListener() {
        public void onInit(int status) {
            MRTextView tv = null;
            A.log("*TTS onInit:" + status);
            if (status == 0) {
                ActivityTxt.this.tts_stopped = false;
                try {
                    if (ActivityTxt.this.ttsTextFromClipboard != null) {
                        String text = ActivityTxt.this.ttsTextFromClipboard;
                        ActivityTxt.this.ttsTextFromClipboard = null;
                        ActivityTxt.this.tts.setSpeechRate(((float) A.tts_speed) / 10.0f);
                        ActivityTxt.this.tts.setPitch(((float) A.tts_pitch) / 10.0f);
                        ActivityTxt.this.tts.speak(text, 0, null);
                        ActivityTxt.this.tts_start_time = SystemClock.elapsedRealtime();
                        return;
                    } else if (!ActivityTxt.this.isPdf()) {
                        if (ActivityTxt.this.txtView.hStart != -1) {
                            tv = ActivityTxt.this.txtView;
                        } else if (ActivityTxt.this.txtView2.hStart != -1) {
                            tv = ActivityTxt.this.txtView2;
                        }
                        if (tv != null) {
                            String selected = tv.getText().subSequence(tv.hStart, tv.hEnd).toString();
                            ActivityTxt.this.inverseLayoutVisible(true);
                            ActivityTxt.this.tts.setSpeechRate(((float) A.tts_speed) / 10.0f);
                            ActivityTxt.this.tts.setPitch(((float) A.tts_pitch) / 10.0f);
                            ActivityTxt.this.tts.speak(selected, 0, null);
                            ActivityTxt.this.tts_start_time = SystemClock.elapsedRealtime();
                            return;
                        } else if (ActivityTxt.this.dualPageEnabled()) {
                            A.touchingView = null;
                            A.isSpeaking = true;
                            ActivityTxt.this.checkDualPageMode();
                            new Handler() {
                                public void handleMessage(Message msg) {
                                    ActivityTxt.this.txtScrollNoDelay(msg.what);
                                    AnonymousClass54.this.startTTSProc();
                                }
                            }.sendEmptyMessage(ActivityTxt.this.txtView.getCurPosition());
                            return;
                        } else {
                            startTTSProc();
                            return;
                        }
                    } else if (!ActivityTxt.this.dotVisible() || ActivityTxt.this.pdf.selectedText == null || ActivityTxt.this.pdf.selectedText.length() <= 0) {
                        startTTSProc();
                        return;
                    } else {
                        ActivityTxt.this.inverseLayoutVisible(true);
                        ActivityTxt.this.hideDotSelectors();
                        ActivityTxt.this.tts.setSpeechRate(((float) A.tts_speed) / 10.0f);
                        ActivityTxt.this.tts.setPitch(((float) A.tts_pitch) / 10.0f);
                        ActivityTxt.this.tts.speak(ActivityTxt.this.pdf.selectedText, 0, null);
                        ActivityTxt.this.tts_start_time = SystemClock.elapsedRealtime();
                        return;
                    }
                } catch (Exception e) {
                    A.error(e);
                    return;
                }
            }
            T.showAlertText(ActivityTxt.this, ActivityTxt.this.getString(R.string.error), ActivityTxt.this.getString(R.string.tts_init_failed));
        }

        private void startTTSProc() {
            ActivityTxt.this.lastEndOfSpeakText = -1;
            ActivityTxt.this.speakLines = ActivityTxt.this.getSpeakText();
            if (ActivityTxt.this.speakLines == null || ActivityTxt.this.tts == null) {
                A.isSpeaking = false;
                return;
            }
            A.isSpeaking = true;
            ActivityTxt.this.registerAudioFocus();
            ActivityTxt.this.show_tts_icon();
            ActivityTxt.this.inverseLayoutVisible(true);
            ActivityTxt.this.tts_utteranceId = -1;
            ActivityTxt.this.setSpeakQueue(ActivityTxt.this.tts_utteranceId + 1);
            ActivityTxt.this.setStopTtsHandler();
            ActivityTxt.this.showTtsOptionsButton();
        }
    };
    OnClickListener onPdfAnnotButtonClick;
    public int pageDirection = 1;
    private long pageScrollTime;
    private boolean pagingByMove;
    public long pausedTime;
    public PDFReader pdf;
    public boolean pdfAnnotStart;
    FrameLayout pdfBaseFrame;
    View pdfBottomLay;
    private int pdfCheckWordCountState;
    public int pdfCurPageNo;
    Boolean pdfFullStatusBar;
    private boolean pdfInited;
    private long pdfLastSaveTime;
    View pdfLay;
    ImageView pdfLockIv;
    private ArrayList<String> pdfMissedFonts;
    ImageView pdfPenIv;
    ClearableEditText pdfSearchEdit;
    View pdfSearchLay;
    String pdfSearchString;
    private ArrayList<Integer> pdfStatisticsPages;
    PDFThumbView2 pdfThumb;
    public ArrayList<TocChapter> pdfToc;
    View pdfTopLay;
    public boolean pdfTreeTOC;
    private int pdfVerticalOffset;
    private int pdf_sel_index;
    public String pdf_title;
    TextView percentView;
    private long pixelAutoScrollToBottomTime;
    Handler pixelScrollHandler;
    private long pixel_scroll_ignore;
    private int preNextChapterId;
    private int preNextChapterSplit;
    private CharSequence preNextChapterText;
    private String preNextHtmlSrc;
    private boolean preNextInProgress;
    private ArrayList<String> preNextSplitHtmls;
    public NoteInfo preNoteInfo;
    private boolean prePriorInProgress;
    private String preShowText;
    private boolean preparedMovingChangeChapter;
    private boolean pressDown = false;
    private boolean pressDown2 = false;
    public float pressDownX = 0.0f;
    private float pressDownX2 = 0.0f;
    private float pressDownY = 0.0f;
    private float pressDownY2 = 0.0f;
    private boolean pressDown_move_for_speed;
    private boolean pressDown_move_for_volume;
    private int pressDown_scroll_value;
    private int pressDown_speak_value;
    private int pressDown_speed_speed;
    long pressTime = -1;
    private float priorMovingEventY;
    private float priorMovingY;
    private ProgressDialog progressDlg;
    ProgressBar progressIndicator;
    SeekBar progressSK;
    private int read_words;
    private Handler receiveHandler;
    private boolean recheck_sysbar;
    private long registerHardwaresTime = -1;
    public View remainingLay;
    private TextView remainingLeft;
    private TextView remainingRight;
    private Handler restartHandler;
    private boolean restart_after_sync_finish;
    public long resumeTime;
    DotImageView scrollBlock;
    ScrollImage scrollImage;
    private int scrollInterval = -1;
    private int scrollPos;
    private Timer scrollTimer;
    private long scroll_event_time;
    SearchResult search_result;
    OnSeekBarChangeListener seekBarChanged = new OnSeekBarChangeListener() {
        public void onStopTrackingTouch(SeekBar seekBar) {
            ActivityTxt.this.nav_return.setVisibility(0);
            if (ActivityTxt.this.getBookType() == 100 && !ActivityTxt.this.ebook_inLoading && ActivityTxt.this.web == null && ActivityTxt.this.seekBarStartPos != ActivityTxt.this.getSeekBarProgress(seekBar)) {
                ActivityTxt.this.ebook_inLoading = true;
                ActivityTxt.this.showBookByPercent(ActivityTxt.this.getSeekBarProgress(seekBar));
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            if (ActivityTxt.this.nav_return.getVisibility() == 8) {
                ActivityTxt.this.saveLinkBackInfo(true);
            }
            ActivityTxt.this.seekBarStartPos = ActivityTxt.this.getSeekBarProgress(seekBar);
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!ActivityTxt.this.isPdf()) {
                if (progress < 0) {
                    progress = 0;
                }
                if (progress > 1000) {
                    progress = 1000;
                }
                if (ActivityTxt.this.layoutVisible) {
                    float p = (((float) progress) * 100.0f) / 1000.0f;
                    if (fromUser) {
                        ActivityTxt.this.percentView.setText(p + "%");
                    } else {
                        ActivityTxt.this.percentView.setText(ActivityTxt.this.getPercentStr2());
                    }
                }
                if (fromUser && ActivityTxt.this.getBookLength() > 0 && ActivityTxt.this.web == null && (ActivityTxt.this.getBookType() == 0 || ActivityTxt.this.getBookType() == 1)) {
                    ActivityTxt.this.showBookByPercent(progress);
                    ActivityTxt.this.checkStatusBar();
                    ActivityTxt.this.setFlipViewINVISIBLE(true);
                    ActivityTxt.this.resetFlipCache(false, 1000);
                }
                ActivityTxt.this.currentPage = -1;
            } else if (fromUser) {
                goToPdfPage(progress, true);
            }
        }

        private void goToPdfPage(int progress, boolean updatePage) {
            if (ActivityTxt.this.m_doc != null && ActivityTxt.this.m_doc != null) {
                int p = ActivityTxt.this.pdfValidatePageNumber(progress);
                ActivityTxt.this.percentView.setText("" + (p + 1) + "/" + ActivityTxt.this.m_doc.GetPageCount());
                if (updatePage) {
                    A.lastPosition = (long) p;
                    ActivityTxt.this.pdfGotoPage((int) A.lastPosition, true);
                    ActivityTxt.this.updateProgressStatus();
                }
            }
        }
    };
    private int seekBarStartPos;
    long select_scroll_time;
    boolean select_text_state;
    private SensorManager sensorMgr;
    private boolean sentCurlTouchEvent;
    public long setLayoutTime = 0;
    private long setScreenAwakeTime;
    View shadeView;
    public MyShakeSensorListener shakeSensorLisener;
    boolean showHintAtLoadingEnd;
    private Handler speakHandler;
    private ArrayList<TtsLine> speakLines;
    private int speakTextLength = -1;
    private boolean speak_in_background;
    long startTime;
    long statisticsTime;
    public View status2Page;
    private Handler statusHandler;
    public View statusLay;
    public View statusLayHori;
    private TextView statusLeft;
    private TextView statusLeft2;
    public View statusLeftPanel;
    private TextView statusMiddle;
    private TextView statusMiddle21;
    private TextView statusMiddle22;
    private TextView statusRight;
    int stopTtsFromCalling;
    private boolean swipeGuesture;
    private int sysbar_height;
    CheckBox tips_confirm;
    ImageView tips_image;
    View tips_ok;
    View tips_panel;
    private int tips_type;
    TextView titleTextView;
    public Bitmap tmpFlipShot1;
    public Bitmap tmpFlipShot2;
    public Bitmap tmpFlipShot3;
    private boolean tmpHorizaontalScroll = false;
    LINK_BACK tmpPositionInfo;
    View topLay;
    private int topLayPaddingTop = -1;
    long total;
    private long totalPages;
    private TextToSpeech tts;
    private Builder ttsAlertDlg;
    private boolean ttsButtonPressed;
    private OnClickListener ttsClickEvent;
    ImageView ttsIv;
    private OnSeekBarChangeListener ttsSeekBarEvent;
    private String ttsTextFromClipboard;
    boolean tts_from_last_line;
    private OnUtteranceCompletedListener tts_listener;
    public View tts_next;
    Notification tts_nofitication;
    public View tts_options;
    public View tts_page_down;
    public View tts_page_up;
    public View tts_panel;
    boolean tts_panel_visible;
    boolean tts_paused;
    SeekBar tts_pitch;
    public View tts_play;
    public View tts_prior;
    BroadcastReceiver tts_receiver;
    TextView tts_reset;
    private int tts_screen_off_pos = -1;
    SeekBar tts_speed;
    long tts_start_time;
    public View tts_stop;
    boolean tts_stopped;
    private int tts_utteranceId;
    SeekBar tts_volume;
    View txtLay;
    public FrameLayout txtLayS;
    View txtLineS;
    boolean txtReload;
    public ScrollView txtScroll;
    public ScrollView txtScroll2;
    private Handler txtScrollHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (ActivityTxt.this.txtView.getLayout() != null) {
                try {
                    switch (msg.what) {
                        case 0:
                            ActivityTxt.this.txtScrollTo(0);
                            ActivityTxt.this.forceDelayFlipAnimation = false;
                            ActivityTxt.this.ebook_inLoading = false;
                            break;
                        case 1:
                            ActivityTxt.this.disableTxtViewDraw();
                            int tvH = ActivityTxt.this.txtView.getRealHeight();
                            int svH = A.getPageHeight();
                            int lh = ActivityTxt.this.txtView.getLineHeight();
                            int toY = 0;
                            if (!ActivityTxt.this.dualPageEnabled()) {
                                int line = ActivityTxt.this.txtView.getLayout().getLineForVertical((tvH - svH) + (lh * 2));
                                if (ActivityTxt.this.txtView.getSpanned() != null) {
                                    int start = ActivityTxt.this.txtView.getLayout().getLineStart(line);
                                    PAGE_BREAK[] sps = (PAGE_BREAK[]) ActivityTxt.this.txtView.getSpanned().getSpans(start, ActivityTxt.this.txtView.getText().length(), PAGE_BREAK.class);
                                    if (sps.length > 0) {
                                        int start2 = ActivityTxt.this.txtView.getSpanned().getSpanStart(sps[sps.length - 1]);
                                        if (start2 > start) {
                                            line = ActivityTxt.this.txtView.getLayout().getLineForOffset(start2);
                                        }
                                    }
                                }
                                toY = ActivityTxt.this.txtView.getLineTop2(line);
                            } else if (tvH > (svH * 2) - lh) {
                                toY = ActivityTxt.this.txtView.getLineTop2(ActivityTxt.this.txtView.getLayout().getLineForVertical((tvH - (svH * 2)) + (lh * 3)));
                            }
                            ActivityTxt.this.goToLastTime = SystemClock.elapsedRealtime();
                            ActivityTxt.this.txtScrollTo(toY);
                            ActivityTxt.this.enableTxtViewDraw();
                            ActivityTxt.this.forceDelayFlipAnimation = false;
                            ActivityTxt.this.ebook_inLoading = false;
                            break;
                        case 2:
                            ActivityTxt.this.txtScrollTo((int) ((((long) ActivityTxt.this.txtView.getRealHeight()) * ((long) ((Integer) msg.obj).intValue())) / ((long) A.maxHtmlChapterSize)));
                            break;
                        case 3:
                            ActivityTxt.this.txtScrollTo(ActivityTxt.this.txtView.getLineTop2(ActivityTxt.this.txtView.getLayout().getLineForOffset(((Integer) msg.obj).intValue())));
                            break;
                        case 4:
                            ActivityTxt.this.ebook_inLoading = true;
                            ActivityTxt.this.clearTxtView();
                            if (A.ebook == null || A.ebook.isHtml()) {
                                ActivityTxt.this.imageGetter = A.ebook == null ? ActivityTxt.this.createHtmlBookImageGetter() : A.ebook.getMyImageGetter();
                                if (A.noSplitHtmls()) {
                                    ActivityTxt.this.changeChapterTime = SystemClock.elapsedRealtime();
                                    ActivityTxt.this.hideProgressDlg();
                                    return;
                                }
                                if (A.lastSplitIndex >= A.splitHtmls.size()) {
                                    A.lastSplitIndex = A.splitHtmls.size() - 1;
                                }
                                ActivityTxt.this.htmlSrc = (String) A.splitHtmls.get(A.lastSplitIndex);
                                ActivityTxt.this.htmlSrc = A.chineseJianFanConvert(ActivityTxt.this.htmlSrc);
                                ActivityTxt.this.txtViewSetText(MyHtml.fromHtml(ActivityTxt.this.htmlSrc, ActivityTxt.this.imageGetter, A.lastChapter));
                            } else {
                                ActivityTxt.this.txtViewSetText(A.chineseJianFanConvert((String) A.splitHtmls.get(A.lastSplitIndex)));
                            }
                            ActivityTxt.this.txtScrollHandler.sendEmptyMessageDelayed(((Integer) msg.obj).intValue() == 1 ? 0 : 1, 50);
                            if (A.isSpeaking) {
                                ActivityTxt.this.speakCurrentPage(-100);
                                break;
                            }
                            break;
                    }
                    ActivityTxt.this.changeChapterTime = SystemClock.elapsedRealtime();
                    ActivityTxt.this.hideProgressDlg();
                } catch (Throwable th) {
                    ActivityTxt.this.changeChapterTime = SystemClock.elapsedRealtime();
                    ActivityTxt.this.hideProgressDlg();
                }
            }
        }
    };
    MRTextView txtView;
    MRTextView txtView2;
    private String urlFootnote;
    Handler veryifiHandler;
    public MRBookView web;
    ProgressWheel wheel;
    boolean wheelVisible;
    long xFlingTime = 1;
    private ArrayList<Integer> yBeforePageDown = new ArrayList();
    long yFlingTime = 0;

    public class AutoScrollTask extends TimerTask {
        public void run() {
            if (!A.isInAutoScroll) {
                cancel();
            } else if (!ActivityTxt.this.autoScrollPaused) {
                ActivityTxt.this.autoScrollTimesCount = ActivityTxt.this.autoScrollTimesCount + 1;
                switch (ActivityTxt.this.getScrollMode()) {
                    case 0:
                        ActivityTxt.this.handler.sendEmptyMessage(301);
                        return;
                    case 1:
                        if (ActivityTxt.this.autoScrollTimesCount * 4 >= A.autoScrollSpeed) {
                            ActivityTxt.this.autoScrollTimesCount = 0;
                            ActivityTxt.this.handler.sendEmptyMessage(301);
                            return;
                        }
                        return;
                    case 2:
                        ActivityTxt.this.handler.sendEmptyMessage(301);
                        return;
                    case 3:
                        if (ActivityTxt.this.autoScrollTimesCount * 5 >= A.autoScrollSpeed) {
                            ActivityTxt.this.autoScrollTimesCount = 0;
                            ActivityTxt.this.handler.sendEmptyMessage(301);
                            return;
                        }
                        return;
                    case 4:
                        ActivityTxt.this.handler.sendEmptyMessage(301);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public class LINK_BACK {
        int backChapter;
        String backFile;
        long backPosition;
        int backSplitIndex;
        String title;

        public LINK_BACK(String title, String backFile, int backChapter, int backSplitIndex, long backPosition) {
            this.title = title;
            this.backFile = backFile;
            this.backChapter = backChapter;
            this.backSplitIndex = backSplitIndex;
            this.backPosition = backPosition;
        }
    }

    class MyCurlGesture extends SimpleOnGestureListener {
        MyCurlGesture() {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            ActivityTxt.this.mCurlFlingDirection = velocityX;
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }
    }

    class MySimpleGesture extends SimpleOnGestureListener {
        MySimpleGesture() {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (ActivityTxt.this.inPreShow) {
                return false;
            }
            if (ActivityTxt.this.isPdf()) {
                return false;
            }
            if (ActivityTxt.this.dotVisible()) {
                return false;
            }
            if (A.isSpeaking || A.isInAutoScroll) {
                return false;
            }
            if (A.touchingView instanceof ScrollView) {
                try {
                    ActivityTxt.this.mVT.addMovement(ActivityTxt.this.hMotionEvent);
                    ActivityTxt.this.mVT.addMovement(e1);
                    ActivityTxt.this.mVT.computeCurrentVelocity(1000, (float) ActivityTxt.this.mMaximumVelocity);
                    if (ActivityTxt.this.acceptHorizontalFling(ActivityTxt.this.mVT, velocityX, velocityY)) {
                        return true;
                    }
                    if (ActivityTxt.this.isDisableMove()) {
                        return true;
                    }
                    if (ActivityTxt.this.isDisableMovedFlips() && !ActivityTxt.this.isMiddleTap(e1)) {
                        return true;
                    }
                    ActivityTxt.this.currentPage = -1;
                    ActivityTxt.this.longTimeTapEvent = false;
                    ActivityTxt.this.hideDotViews();
                    if (ActivityTxt.this.getBookType() != 0) {
                        ActivityTxt.this.updateProgressStatus();
                        return false;
                    }
                    double flingY = (double) ActivityTxt.this.mVT.getYVelocity();
                    if (flingY == 0.0d) {
                        flingY = (double) velocityY;
                    }
                    int distance = (int) ((flingY * flingY) / ((double) (2.0f * ActivityTxt.this.mDeceleration)));
                    if (Math.abs(flingY) < 5.0d) {
                        return false;
                    }
                    int y = ActivityTxt.this.txtScroll.getScrollY();
                    int h = ActivityTxt.this.txtView.getRealHeight();
                    int lh = ActivityTxt.this.txtView.getLineHeight();
                    MyLayout layout = ActivityTxt.this.txtView.getLayout();
                    if (layout == null) {
                        return false;
                    }
                    if (distance >= h / 2) {
                        double newFling = Math.sqrt((double) (((float) ((h / 2) * 2)) * ActivityTxt.this.mDeceleration));
                        flingY = flingY > 0.0d ? newFling : -newFling;
                    }
                    int curPos;
                    int i;
                    int l0;
                    int newPos;
                    if (flingY <= 0.0d || A.lastBlockIndex <= 2) {
                        if (flingY < 0.0d && A.lastBlockIndex < A.getTxts().size() - 2 && A.lastBlockIndex > 0 && ((double) y) - flingY > ((double) (h - lh))) {
                            flingY = (9.0d * flingY) / 10.0d;
                            curPos = layout.getLineStart(layout.getLineForVertical(y));
                            i = A.lastBlockIndex;
                            l0 = A.getTxts2(i - 1).length();
                            int l1 = A.getTxts2(i).length();
                            newPos = curPos > l0 + l1 ? (curPos - l0) - l1 : curPos - l0;
                            if (curPos > l0 + l1) {
                                i++;
                            }
                            ActivityTxt.this.txtViewSetText(A.chineseJianFanConvert(A.getTxts2(i) + A.getTxts2(i + 1) + A.getTxts2(i + 2)));
                            ActivityTxt.this.txtScrollTo(ActivityTxt.this.txtView.getLineTop2(ActivityTxt.this.txtView.getLayout().getLineForOffset(newPos + 5)));
                            A.lastBlockIndex = i + 1;
                        }
                    } else if (((double) y) - flingY < ((double) lh)) {
                        curPos = layout.getLineStart(layout.getLineForVertical(y));
                        i = A.lastBlockIndex;
                        l0 = A.getTxts2(i - 1).length();
                        if (curPos > l0) {
                            newPos = curPos - l0;
                        } else {
                            newPos = curPos;
                        }
                        if (curPos < l0) {
                            i--;
                        }
                        ActivityTxt.this.txtViewSetText(A.chineseJianFanConvert(A.getTxts2(i - 2) + A.getTxts2(i - 1) + A.getTxts2(i)));
                        ActivityTxt.this.txtScrollTo(ActivityTxt.this.txtView.getLineTop2(ActivityTxt.this.txtView.getLayout().getLineForOffset(((A.getTxts2(i - 2).length() + A.getTxts2(i - 1).length()) + newPos) - 5)));
                        A.lastBlockIndex = i - 1;
                    }
                    ActivityTxt.this.yFlingTime = SystemClock.elapsedRealtime();
                    ActivityTxt.this.txtScroll.fling(-((int) flingY));
                    int delay = (int) Math.abs(velocityY);
                    if (delay > 1500 && !ActivityTxt.this.showStatusbar()) {
                        ActivityTxt.this.handler.sendEmptyMessageDelayed(401, (long) ((delay * 3200) / (delay + 2500)));
                    }
                    ActivityTxt.this.updateProgressStatus();
                    return true;
                } catch (Exception e) {
                    A.error(e);
                }
            }
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }
    }

    public ActivityTxt() {
        int i = (A.isSmallPhone || A.isTablet) ? 25 : 72;
        this.sysbar_height = i;
        this.recheck_sysbar = false;
        this.totalPages = -1;
        this.currentPage = -1;
        this.lastPageCountCheckSize = -1;
        this.lastCheckProgressTime = -1;
        this.hOnTouch = new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                ActivityTxt.this.showSelectBar();
                switch (event.getAction()) {
                    case 0:
                        ActivityTxt activityTxt = ActivityTxt.this;
                        if (!(v == ActivityTxt.this.dot1 || v == ActivityTxt.this.dot2)) {
                            v = null;
                        }
                        activityTxt.dot = v;
                        if (ActivityTxt.this.dot != null) {
                            ActivityTxt.this.hLastX = ((int) event.getRawX()) - ActivityTxt.this.dot.getLeft();
                            ActivityTxt.this.hLastY = ((int) event.getRawY()) - ActivityTxt.this.dot.getTop();
                            break;
                        }
                        break;
                    case 1:
                        if (ActivityTxt.this.dot != null) {
                            ActivityTxt.this.highlightText(true, -1, -1);
                            ActivityTxt.this.dot = null;
                            break;
                        }
                        break;
                    case 2:
                        if (ActivityTxt.this.dot != null) {
                            ActivityTxt.this.select_move_down((float) ActivityTxt.this.dot.getTop());
                            int l = ((int) event.getRawX()) - ActivityTxt.this.hLastX;
                            int t = ((int) event.getRawY()) - ActivityTxt.this.hLastY;
                            ActivityTxt.this.dot.layout(l, t, ActivityTxt.this.dot.getWidth() + l, ActivityTxt.this.dot.getHeight() + t);
                            ActivityTxt.this.dot.postInvalidate();
                            ActivityTxt.this.preNoteInfo = null;
                            ActivityTxt.this.highlightText(false, -1, -1);
                            break;
                        }
                        break;
                }
                return true;
            }
        };
        this.hOnClick = new OnClickListener() {
            public void onClick(View v) {
                String s;
                if (v == ActivityTxt.this.hHighlight) {
                    ActivityTxt.this.doHighlight(false);
                }
                if (v == ActivityTxt.this.hNote) {
                    ActivityTxt.this.do_add_note();
                }
                if (v == ActivityTxt.this.hPen) {
                    if (ActivityTxt.this.isClickOnAnnot()) {
                        if (ActivityTxt.this.isPdf()) {
                            ActivityTxt.this.pdf.annotRemove();
                            A.pdfAnnotUpdated = true;
                            ActivityTxt.this.hideDotViews();
                            ActivityTxt.this.pdfSaveAnnotsForTime();
                        } else {
                            ActivityTxt.this.removeCurrentNote(ActivityTxt.this.preNoteInfo);
                        }
                    } else if (ActivityTxt.this.isPdf()) {
                        if (!ActivityTxt.this.m_doc.CanSave()) {
                            ActivityTxt.this.pdfShowFailedAnnotInfo(ActivityTxt.this.getString(R.string.pdf_cannot_modify));
                        } else if (A.isProVersion) {
                            ActivityTxt.this.hideDotViews();
                            ActivityTxt.this.pdfShowAnnotLay();
                        } else {
                            A.showBuyDialog(ActivityTxt.this, ActivityTxt.this.getString(R.string.pro_only));
                        }
                    }
                }
                if (!ActivityTxt.this.isPdf() || ActivityTxt.this.pdf.textReflow) {
                    s = ActivityTxt.this.getTouchTxtView().selectedText;
                } else {
                    s = ActivityTxt.this.pdf.selectedText;
                    if (s == null && ActivityTxt.this.preNoteInfo == null) {
                        return;
                    }
                }
                if (v == ActivityTxt.this.hCopy && s != null) {
                    ActivityTxt.this.copyToClipboard(s);
                    ActivityTxt.this.hideDotViews();
                }
                if (s != null) {
                    s = s.trim();
                }
                if (v == ActivityTxt.this.hDict) {
                    ActivityTxt.this.showSelectBar();
                    if (A.translateInited) {
                        ActivityTxt.this.doDictButton(false);
                    } else {
                        ActivityTxt.this.customizeDict(ActivityTxt.this, true);
                    }
                }
                if (v == ActivityTxt.this.hMore && s != null) {
                    ActivityTxt.this.showDictMoreMenu(s);
                }
            }
        };
        this.hBarFontSize = -1.0f;
        this.cancelFade = false;
        this.highlightY = -1;
        this.preNoteInfo = null;
        this.inversedWebLastY = -1;
        this.inversedWebLastChapter = -1;
        this.WEB_SCROLL_OFF = A.d(5.0f);
        this.bookBackgroundStyle = -2;
        this.last_statistics_Y = -1;
        this.preNextChapterId = -1;
        this.oldPriorChapterId = -1;
    }

    protected void onCreate(Bundle savedInstanceState) {
        A.log("-txt:onCreate");
        if (!closeOldReader(savedInstanceState)) {
            checkLollipopFullScreenState();
            super.onCreate(savedInstanceState);
            requestWindowFeature(1);
            selfPref = this;
            if (VERSION.SDK_INT >= 11) {
                getWindow().addFlags(16777216);
                if (VERSION.SDK_INT >= 17) {
                    A.fitHardwareAccelerated = true;
                }
            }
            A.checkIfLoadedOptions(this);
            setContentView(R.layout.show_txt);
            initExtras(true);
            if (!restartWhenLowMemory(35, false, 1200)) {
                initView();
                new Thread(this).start();
            }
        }
    }

    public boolean closeOldReader(Bundle savedInstanceState) {
        if (T.isOutOfMemoryError) {
            super.onCreate(savedInstanceState);
            setContentView(new View(this));
            delaySystemExit(1200);
            A.log("==========>>>RestartApp - txt");
            return true;
        }
        if (!T.isNull(selfPref)) {
            try {
                if (!(selfPref.isPaused || selfPref.txtView == null)) {
                    selfPref.saveLastPostion(true, true);
                    A.log("==========>>>Save Prior TXT");
                }
                if (A.isInAutoScroll) {
                    selfPref.do_AutoScroll(false, false);
                }
                if (A.isSpeaking) {
                    selfPref.stop_speak(false);
                }
                A.isSpeaking = false;
                A.isInAutoScroll = false;
                selfPref.finish();
                selfPref = null;
            } catch (Exception e) {
                A.error(e);
            }
        }
        return false;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        A.log("-txt:onConfigurationChanged");
        A.getScreenRealHeight(this);
        if (!this.isPaused) {
            try {
                this.cachedSplitsPageNum = null;
                this.cachedSplitsPageCount = null;
                resetPageCount();
                this.checkhBarWidth = false;
                this.bookBackgroundStyle = -1;
                pdfSetImmersiveMode(false);
                A.onConfigurationChangedTime = SystemClock.elapsedRealtime();
                checkDualPageMode();
                checkLandscape2PagePadding();
                if (this.isTxtScrollReady) {
                    if (!this.isPressScreenStateButton) {
                        if (A.screenState == 0) {
                            if (this.resumeTime != -1 && SystemClock.elapsedRealtime() - this.resumeTime > 1000 && isBigChapter(A.lastChapter)) {
                                createProgressDlg(((Chapter) A.ebook.getChapters().get(A.lastChapter)).name);
                            }
                            resetFlipCache(false, 1000);
                            this.isTxtScrollReady = false;
                            this.handler.post(this);
                        }
                        statusHandlerSendMessage(1, 150);
                    } else if (dualPageEnabled()) {
                        new Handler() {
                            public void handleMessage(Message msg) {
                                ActivityTxt.this.isTxtScrollReady = false;
                                ActivityTxt.this.handler.post(ActivityTxt.this);
                            }
                        }.sendEmptyMessageDelayed(0, 100);
                    } else {
                        this.handler.post(this);
                    }
                }
                if (A.isInAutoScroll) {
                    do_AutoScroll(false);
                }
                if (isPdf() && this.pdf != null) {
                    pdfRemoveThumb();
                    pdfSetProperties(true);
                }
                if (this.curl3d != null) {
                    initCurl3d(true);
                }
                setFlipViewINVISIBLE(true);
            } catch (Exception e) {
                A.error(e);
            }
        } else if (A.screenState == 0 && this.isTxtScrollReady) {
            A.log("******AUTO-SENSOR onConfigurationChanged in background******", true);
            reloadBookOnError();
        }
    }

    private void reloadBookOnError() {
        checkDualPageMode();
        resetFlipCache(false, 1000);
        this.isTxtScrollReady = false;
        this.handler.postDelayed(this, 0);
        statusHandlerSendMessage(1, 100);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        A.log("-txt:onRestoreInstanceState");
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        A.log("-txt:onSaveInstanceState");
    }

    protected void onResume() {
        boolean ok = false;
        try {
            super.onResume();
            A.log("-txt:resume");
            selfPref = this;
            this.resumeTime = SystemClock.elapsedRealtime();
            this.statisticsTime = this.resumeTime;
            A.invokeRemind1Time = SystemClock.elapsedRealtime();
            if (isPdf() && this.pdf != null) {
                this.pdf.isOnPaused = false;
            }
            this.isPressScreenStateButton = false;
            A.getScreenRealHeight(this);
            if (A.adjustBrightness) {
                setScreenBrightness(A.brightnessValue, false);
                setLeds();
            }
            if (this.shakeSensorLisener != null) {
                this.shakeSensorLisener.tilt_hold_count = 0;
                this.shakeSensorLisener.tilt_base_z = 10000.0f;
            }
            registerHardwares();
            if (showStatusbar() && this.isTxtScrollReady) {
                updateBarTime();
            }
            if (A.passwordCheckFailed) {
                terminate_reader();
            }
            if (A.isSpeaking) {
                if (this.tts_screen_off_pos != -1) {
                    txtScrollByDelay(this.tts_screen_off_pos);
                } else if (this.txtView.hStart != -1) {
                    txtScrollByDelay(this.txtView.hStart);
                }
                updateProgressStatus();
            }
            if (A.syncWindowOpened) {
                A.syncWindowOpened = false;
                if (A.getDropbox().checkDropboxAuth()) {
                    A.syncType = 2;
                    A.downloadCloudPositionFile();
                } else {
                    A.syncType = 0;
                    if (PrefMisc.syncDropboxCheck != null) {
                        PrefMisc.syncDropboxCheck.setChecked(false);
                    }
                }
            } else if (A.syncType != 0 && (this.isTxtScrollReady || (isPdf() && this.pdf != null))) {
                A.downloadCloudPositionFile();
            }
            A.setSystemUiVisibility(true);
            this.setScreenAwakeTime = 0;
            checkScreenAwake();
            pdfSetImmersiveMode(false);
            setAutoTheme();
            if (this.txtView != null && this.txtView.getLayout() == null && this.isPaused) {
                A.log("******txtView.getLayout()==null******", true);
                this.isPaused = false;
                clearTxtView();
                resetFlipCache(false, 1000);
                if (getBookType() == 0) {
                    A.getTxts().clear();
                }
                this.handler.postDelayed(this, 100);
            }
            this.isPaused = false;
            ok = true;
        } catch (Exception e) {
            A.error(e);
        }
        if (!ok) {
            restartReaderToMain();
        }
    }

    protected void onPause() {
        super.onPause();
        this.isPaused = true;
        this.pausedTime = SystemClock.elapsedRealtime();
        A.lastReadTime = System.currentTimeMillis();
        this.isPressScreenStateButton = false;
        A.log("-txt:pause");
        try {
            saveLastPostion(true);
            if (!(A.doShakePhone == 19 && A.isSpeaking)) {
                unregisterShakeSensor();
            }
            saveStatisticsInfo();
            if (A.isInAutoScroll) {
                do_AutoScroll(false);
            }
            if (this.isOnExiting) {
                A.forceRebootToMain = true;
                T.deleteFile(A.txtPauseOnlyTagFile);
            } else {
                A.forceRebootToMain = false;
            }
            A.forceRebootToTxt = false;
            A.SaveOptions(this);
            show_tts_icon();
            if (!(A.syncType == 0 || A.syncWindowOpened)) {
                A.uploadCloudPositionFile();
            }
            if (isPdf() && this.pdf != null) {
                this.pdf.isOnPaused = true;
                if (A.pdfAnnotUpdated) {
                    A.log("****SAVE PDF DOCUMENT****");
                    A.pdfAnnotUpdated = false;
                    this.m_doc.Save();
                    this.pdfLastSaveTime = SystemClock.elapsedRealtime();
                } else {
                    pdfCheckBookcover();
                }
            }
            if (this.isOnExiting) {
                clearMemoryOnExit();
            }
            this.txtView.clearLrCache2();
            this.txtView2.clearLrCache2();
        } catch (Exception e) {
            A.error(e);
        }
        A.updateWidget(this, true);
    }

    protected void onStart() {
        A.log("-txt:onStart");
        super.onStart();
        setScreenOrientation();
        if (!this.isTxtScrollReady) {
            checkLanguageSetting();
        }
    }

    private void saveStatisticsInfo() {
        if (!A.isInAutoScroll || getScrollMode() <= 1) {
            A.hasStatisticsAction = true;
            ReadStatistics r = BookDb.getSavedStatistics(A.lastFile);
            String s = "" + ((((System.currentTimeMillis() / 24) / 60) / 60) / 1000) + "\n";
            if (r.dates.indexOf(s) == -1) {
                r.dates += s;
            }
            long readTime = SystemClock.elapsedRealtime() - this.statisticsTime;
            if (this.read_words > 0 && this.statisticsTime > 0 && readTime > 3000) {
                A.statistics_time += readTime;
                r.usedTime += readTime;
                r.readWords += (long) this.read_words;
            }
            BookDb.insertReadStatistics(r);
            this.read_words = 0;
            this.statisticsTime = SystemClock.elapsedRealtime();
        }
    }

    private void checkLanguageSetting() {
        if (A.languageID != 0) {
            this.handler.sendEmptyMessageDelayed(601, 500);
        }
    }

    private void initExtras(boolean checkExtras) {
        try {
            if (!T.isNull(NetBookInfo.selfPref)) {
                NetBookInfo.selfPref.finish();
            }
            if (A.appContext == null) {
                A.LoadOptions(this);
            }
            A.lastFileAnnotUpdated = false;
            A.tmpOutOfMemoryTag = false;
            A.forceCssFontName = null;
            A.typefaceCache = null;
            A.moveStart = false;
            A.deletedNotes = null;
            this.pdfThumb = null;
            A.cloudHasCurrentBook = false;
            this.read_words = 0;
            this.startTime = SystemClock.elapsedRealtime();
            this.inPreShow = false;
            this.showHintAtLoadingEnd = false;
            this.hintForBigFileOverload = "";
            this.isPaused = false;
            this.isTxtScrollReady = false;
            this.htmlText = "";
            this.htmlFileWordCount = 0;
            this.htmlFileCharCount = 0;
            A.forceRebootToMain = false;
            A.fileEncoding = null;
            A.highlighInited = false;
            A.pdfStatus = STATUS.sta_none;
            BaseEBook.isGetWordsWoking = false;
            MyHtml.hasRuby = false;
            resetPageCount();
            A.lastWebY = -1;
            this.bookType = -1;
            this.cachedSplitsPageNum = null;
            this.cachedSplitsPageCount = null;
            A.forceCssFontTipped = null;
            long positionFromExtras = -1;
            int chapterFromExtras = 0;
            int splitIndexFromExtras = 0;
            if (checkExtras) {
                A.clearTxts();
                A.getLocaleInfo(this, true);
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    if (extras.containsKey("bookFile")) {
                        A.lastFile = extras.getString("bookFile");
                    }
                    if (extras.containsKey("position")) {
                        positionFromExtras = extras.getLong("position");
                    }
                    if (extras.containsKey("chapter")) {
                        chapterFromExtras = extras.getInt("chapter");
                    }
                    if (extras.containsKey("splitIndex")) {
                        splitIndexFromExtras = extras.getInt("splitIndex");
                    }
                    if (extras.containsKey("fromWidget") && extras.getBoolean("fromWidget")) {
                        if (A.needPasswordProtect && !A.passwordOK) {
                            A.forceRebootToTxt = false;
                            A.forceRebootToMain = false;
                            A.SaveOptions(this);
                            System.exit(0);
                        } else if (A.getFileType(A.lastFile) == 11 || A.getFileType(A.lastFile) == 10) {
                            Intent i = new Intent(this, PicGalleryShow.class);
                            i.putExtra("imageFile", A.lastFile);
                            startActivityForResult(i, 0);
                            finish();
                            return;
                        }
                    }
                }
            }
            if (positionFromExtras != -1) {
                A.lastPosition = positionFromExtras;
                A.lastChapter = chapterFromExtras;
                A.lastSplitIndex = splitIndexFromExtras;
                if (getSharedPreferences(A.POSITION_FILE, 0).contains(A.lastFile.toLowerCase())) {
                    this.ignoreChapterListAtBegin = true;
                }
            } else {
                getReadingPosition();
            }
            if (getBookType() != 0) {
                if (!isPdf()) {
                    createProgressDlg("", getString(R.string.loading) + "...");
                }
            } else if (isBigFileBigPosition(A.lastFile)) {
                createProgressDlg(A.lastFile);
            }
            if (A.bluelightEnable && A.bluelightOpacity > 0) {
                this.handler.sendEmptyMessage(CHECK_BLUELIGHT_FILTER);
            }
        } catch (Exception e) {
            A.error(e);
        }
    }

    private void getReadingPosition() {
        SharedPreferences sp = getSharedPreferences(A.POSITION_FILE, 0);
        String s = "0";
        String saveName = BaseCompressor.getZRCacheFile(A.lastFile.toLowerCase(), true).toLowerCase();
        if (!sp.contains(saveName)) {
            this.ignoreChapterListAtBegin = false;
            for (String key : sp.getAll().keySet()) {
                if (key.endsWith("/" + T.getFilename(saveName))) {
                    s = sp.getString(key, "0");
                    this.ignoreChapterListAtBegin = true;
                    break;
                }
            }
        }
        s = sp.getString(saveName, "0");
        this.ignoreChapterListAtBegin = true;
        A.getLastPositionAndChapterFromString(s);
        if (A.useWebView && sp.contains(saveName + 2)) {
            A.lastWebY = sp.getInt(saveName + 2, 0);
        }
    }

    public void run() {
        this.currentPage = -1;
        this.bookType = -1;
        this.handler.sendEmptyMessageDelayed(902, 60000);
        switch (getBookType()) {
            case 0:
                if (A.getTxts().size() == 0) {
                    loadFile(A.lastFile);
                }
                this.handler.sendEmptyMessage(SHOW_TXT);
                return;
            case 1:
                if (this.htmlText.equals("") && !A.useWebView) {
                    loadFile(A.lastFile);
                }
                this.handler.sendEmptyMessage(SHOW_HTML);
                return;
            case 7:
                loadFile(A.lastFile);
                this.handler.sendEmptyMessage(SHOW_PDF);
                return;
            case 100:
                if (A.ebook == null) {
                    this.handler.sendEmptyMessage(SHOW_EBOOK_COVER);
                    loadFile(A.lastFile);
                }
                this.handler.sendEmptyMessage(SHOW_EBOOK);
                return;
            default:
                return;
        }
    }

    protected void showHintForCloudPositionSync() {
        final long position = A.lastPosition;
        final int chapter = A.lastChapter;
        final int split = A.lastSplitIndex;
        int webY = A.lastWebY;
        getReadingPosition();
        if (A.lastPosition == 0 && A.lastChapter == 0 && A.lastSplitIndex == 0) {
            A.lastPosition = position;
            A.lastChapter = chapter;
            A.lastSplitIndex = split;
        } else if ((this.web == null && (Math.abs(position - A.lastPosition) > 80 || chapter != A.lastChapter || split != A.lastSplitIndex)) || ((this.web != null && webY != A.lastWebY) || (isPdf() && position != A.lastPosition))) {
            this.inCloudSyncing = true;
            if (this.chapterDlg != null) {
                this.chapterDlg.dismiss();
            }
            CharSequence msg = getString(R.string.sync_hint);
            if (A.syncType == 4) {
                msg = A.fullGdriveTitle(A.getGdriveTitle((String) msg));
            }
           new AlertDialog.Builder(this).setTitle(A.syncType == 2 ? "Dropbox" : "Google Drive").setCancelable(false).setMessage(msg).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityTxt.this.preNextChapterText = null;
                    ActivityTxt.this.oldPriorChapterText = null;
                    if (ActivityTxt.this.isPdf()) {
                        ActivityTxt.this.pdfGotoPage((int) A.lastPosition, true);
                    } else {
                        ActivityTxt.this.handler.post(ActivityTxt.this);
                    }
                    if (ActivityTxt.this.getBookType() != 100) {
                        ActivityTxt.this.updateProgressStatus();
                    }
                    ActivityTxt.this.inCloudSyncing = false;
                }
            }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    A.lastPosition = position;
                    A.lastChapter = chapter;
                    A.lastSplitIndex = split;
                    ActivityTxt.this.inCloudSyncing = false;
                }
            }).setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    A.log("*sync cancel");
                    if (ActivityTxt.this.inCloudSyncing) {
                        ActivityTxt.this.inCloudSyncing = false;
                        A.lastPosition = position;
                        A.lastChapter = chapter;
                        A.lastSplitIndex = split;
                    }
                }
            }).show();
        }
    }

    protected void checkScreenAwake() {
        if (this.baseFrame == null || !A.keepScreenAwake) {
            return;
        }
        if (A.isInAutoScroll) {
            this.baseFrame.setKeepScreenOn(true);
        } else if (A.screenAwakeExtend > 30) {
        } else {
            if (this.setScreenAwakeTime == 0) {
                this.setScreenAwakeTime = SystemClock.elapsedRealtime();
                this.baseFrame.setKeepScreenOn(true);
                this.handler.sendEmptyMessageDelayed(902, 10000);
            } else if (SystemClock.elapsedRealtime() - this.setScreenAwakeTime > ((long) ((A.screenAwakeExtend * 60) * 1000)) && !this.isPaused) {
                A.log("*screen awake OFF");
                this.baseFrame.setKeepScreenOn(false);
            }
        }
    }

    protected void needAuthAgainForDropbox() {
        new AlertDialog.Builder(this).setTitle((CharSequence) "Dropbox").setCancelable(false).setMessage((int) R.string.dropbox_sync_again).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                A.syncWindowOpened = true;
                A.validateDropboxAccount(ActivityTxt.this);
            }
        }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                A.syncType = 0;
            }
        }).setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                A.syncType = 0;
            }
        }).show();
    }

    protected boolean isDrmBook() {
        if (A.ebook == null || !A.ebook.isDrmProtected()) {
            return false;
        }
        hideProgressDlg();
       new AlertDialog.Builder(this).setTitle(getString(R.string.error)).setView(LayoutInflater.from(this).inflate(R.layout.drm, null)).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityTxt.this.doFinish();
            }
        }).show();
        return true;
    }

    protected void preShowTxtChapterList() {
        if (A.lastPosition == 0 && A.isChinese && !this.ignoreChapterListAtBegin) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (A.getTxtChapters().size() > 5 && A.lastPosition == 0) {
                        ActivityTxt.this.do_show_chapters();
                    }
                }
            });
        }
    }

    protected void loadNotes() {
        new Thread() {
            public void run() {
                try {
                    A.checkNotesHighlights(true);
                    A.getBookmarks();
                    A.highlighInited = true;
                    ActivityTxt.this.txtView.postInvalidate();
                    ActivityTxt.this.txtView2.postInvalidate();
                } catch (Exception e) {
                    A.error(e);
                }
                if (T.isFile(A.xml_files_folder + "/prefs.xml")) {
                    T.deleteFile(A.xml_files_folder + "/prefs.xml");
                    if (A.syncType == 2) {
                        ActivityTxt.this.handler.sendEmptyMessage(ActivityTxt.DROPBOX_AUTH_AGAIN);
                    }
                } else if (A.syncType != 0) {
                    A.downloadCloudPositionFile();
                }
            }
        }.start();
        if (A.syncReadingBookFile) {
            A.backupReadingBookToCloud(this, this.handler);
        }
    }

    protected void show_ebook_cover() {
        if (!this.dontShowCover && !isPdf()) {
            Drawable d = A.getBookCover(A.lastFile);
            if (d != null) {
                this.isCoverShow = true;
                this.flipView.setBackgroundDrawable(null);
                this.flipView.setImageDrawable(d);
                this.flipView.setVisibility(0);
                this.contentLay.setVisibility(4);
                this.baseFrame.setBackgroundDrawable(null);
            }
        }
    }

    protected boolean hide_ebook_cover() {
        if (!this.isCoverShow) {
            return false;
        }
        this.isCoverShow = false;
        setFlipViewINVISIBLE(true);
        this.flipView.setImageDrawable(null);
        this.contentLay.setVisibility(0);
        A.setBackgroundImage(this.baseFrame);
        if (!A.isFlipCurl()) {
            return true;
        }
        hideProgressDlg();
        return true;
    }

    protected void showPreText() {
        long p;
        if (A.lastPosition > ((long) A.fixedBlockLength)) {
            p = (long) A.getTxtDisplayText(this.preShowText.substring(0, A.fixedBlockLength)).length();
        } else {
            p = 0;
        }
        p += A.lastPosition % ((long) A.fixedBlockLength);
        this.preShowText = A.getTxtDisplayText(this.preShowText);
        this.preShowText = A.chineseJianFanConvert(this.preShowText);
        if (p > 0) {
            MRTextView mRTextView = this.txtView;
            this.txtView2.disableDraw = true;
            mRTextView.disableDraw = true;
        }
        txtViewSetText(this.preShowText);
        txtFileFinalScrollAtStartup(p);
    }

    public void saveLastPostion(boolean saveToFile) {
        if (!isPdf()) {
            saveLastPostion(saveToFile, false);
        } else if (this.pdf != null && saveToFile) {
            pdfSaveLastPosition();
        }
    }

    public void saveLastPostion(boolean saveToFile, boolean ignoreZero) {
        if (System.currentTimeMillis() - this.ignoreSavePosTime < 2000 || this.inCloudSyncing) {
            return;
        }
        if ((!this.ebook_inLoading || saveToFile) && this.txtView != null) {
            if (this.web == null) {
                MyLayout lo = this.txtView.getLayout();
                if (lo == null || !this.isTxtScrollReady) {
                    saveToFile = false;
                } else {
                    int p = lo.getLineStart(lo.getLineForVertical(this.txtScroll.getScrollY()));
                    if (getBookType() == 0) {
                        A.lastPosition = A.getTxtRealPos((long) p);
                    } else {
                        A.lastPosition = (long) p;
                    }
                    if (!saveToFile || (ignoreZero && p == 0)) {
                        saveToFile = false;
                    } else {
                        saveToFile = true;
                    }
                }
            } else {
                A.lastWebY = A.verticalAlignment ? this.web.getScrollX() : this.web.getScrollY();
            }
            if (saveToFile && !this.inPreShow && this.registerHardwaresTime == -1) {
                Editor et = getSharedPreferences(A.POSITION_FILE, 0).edit();
                et.putString(A.lastFile.toLowerCase(), A.lastChapter + "@" + A.lastSplitIndex + "#" + A.lastPosition + ":" + getPercentStr2());
                if (this.web != null) {
                    et.putInt(A.lastFile.toLowerCase() + 2, A.lastWebY);
                }
                et.commit();
            }
        }
    }

    public int getBookType() {
        if (this.bookType == -1) {
            this.bookType = A.getBookType();
        }
        return this.bookType;
    }

    private boolean isHtmlContent() {
        if (getBookType() != 1) {
            return getBookType() == 100 && A.ebook != null && A.ebook.isHtml();
        } else {
            return true;
        }
    }

    private boolean isBigFileBigPosition(String lastFile) {
        if (lastFile.indexOf("?") != -1) {
            return false;
        }
        File file = new File(lastFile);
        if (!file.isFile()) {
            return false;
        }
        long size = file.length();
        if (A.lastPosition > size) {
            A.lastPosition = size;
        }
        if (size > 2000000) {
            return true;
        }
        return false;
    }

    private boolean restartWhenLowMemory(int percent, boolean restartToMain, long delayTime) {
        if (!A.isLowMemory(percent) && !A.isLowestMemory()) {
            return false;
        }
        boolean z;
        A.saveMemoryLog(restartToMain ? "**txt force reboot to main**" : "**txt force reboot**");
        if (restartToMain) {
            z = false;
        } else {
            z = true;
        }
        A.forceRebootToTxt = z;
        A.forceRebootToMain = restartToMain;
        A.SaveOptions(this);
        A.log("*delay restart: " + delayTime);
        if (delayTime > 0) {
            delaySystemExit(delayTime);
        } else {
            System.exit(0);
        }
        return true;
    }

    private void delaySystemExit(long delayTime) {
        new Handler() {
            public void handleMessage(Message msg) {
                System.exit(0);
            }
        }.sendEmptyMessageDelayed(0, delayTime);
    }

    private void loadTxtHtmlFile(String filename) {
        try {
            BufferedReader read;
            A.fileEncoding = A.getFileSavedEncode(filename);
            InputStream is = new DataInputStream(new FileInputStream(filename));
            if (A.fileEncoding.equals("")) {
                read = new BufferedReader(new InputStreamReader(is));
            } else {
                read = new BufferedReader(new InputStreamReader(is, A.fileEncoding));
            }
            if (getBookType() == 0) {
                A.lastBlockIndex = (int) (A.lastPosition / ((long) A.fixedBlockLength));
                long move = (A.lastBlockIndex > 0 ? (long) (A.lastBlockIndex - 1) : 0) * ((long) A.fixedBlockLength);
                char[] preBuffer = new char[(A.fixedBlockLength * 3)];
                long skipped = move == 0 ? 0 : read.skip(move);
                if (skipped == move) {
                    try {
                        this.preShowText = String.valueOf(preBuffer, 0, read.read(preBuffer));
                        this.handler.sendEmptyMessage(PRETEXT_SHOW);
                        this.inPreShow = true;
                    } catch (Exception e) {
                        A.error(e);
                        this.inPreShow = false;
                    } finally {
                        read.close();
                        read = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(filename)), A.fileEncoding));
                    }
                } else {
                    A.log("------skip:" + skipped + " shouldbe:" + move);
                }
                read.close();
                read = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(filename)), A.fileEncoding));
            }
            long totalSize = 0;
            long checkSize = 0;
            char[] buffer;
            int size;
            if (getBookType() == 0) {
                buffer = new char[A.fixedBlockLength];
                while (true) {
                    size = read.read(buffer);
                    if (size != -1) {
                        A.getTxts().add(String.valueOf(buffer, 0, size));
                        totalSize += (long) size;
                        checkSize += (long) size;
                        if (totalSize > A.BIG_FILESIZE && checkSize > 500000) {
                            checkSize = 0;
                            if (A.isLowMemory(15)) {
                                A.tmpOutOfMemoryTag = true;
                                this.hintForBigFileOverload = getString(R.string.low_memory_limits) + Formatter.formatFileSize(this, 2 * totalSize);
                                read.close();
                                return;
                            }
                        }
                    } else {
                        read.close();
                        return;
                    }
                }
            }
            StringBuilder htmlSB = new StringBuilder();
            buffer = new char[A.fixedBlockLength];
            while (true) {
                size = read.read(buffer);
                if (size != -1) {
                    htmlSB.append(String.valueOf(buffer, 0, size));
                } else {
                    read.close();
                    this.htmlText = htmlSB.toString();
                    this.htmlText = T.deleteHtmlComment(this.htmlText);
                    return;
                }
            }
        } catch (OutOfMemoryError e2) {
            A.error(e2);
            System.exit(0);
        } catch (UnsupportedEncodingException e3) {
            A.error(e3);
            SharedPreferences sp = getSharedPreferences(A.ENCODING_FILE, 0);
            if (sp.contains(A.lastFile)) {
                sp.edit().remove(A.lastFile).commit();
            }
            A.textEncode = A.getDefaultTextEncode("");
            getSharedPreferences(A.OPTIONS_FILE, 0).edit().putString("textEncode", "").commit();
            this.handler.sendMessage(this.handler.obtainMessage(ERROR_FINISHED, 0, 0, "Unsupported Encoding Exception: " + A.errorMsg(e3)));
        } catch (Exception e4) {
            A.error(e4);
            System.exit(0);
        }
    }

    private void loadFile(String filename) {
        this.preNextChapterText = null;
        this.oldPriorChapterText = null;
        A.log("*loadFile:" + filename);
        A.openBookCount++;
        A.setHyphenation();
        getSharedPreferences(A.OPTIONS_FILE, 0).edit().putBoolean("forceRebootToMain", true).commit();
        if (!restartWhenLowMemory(35, false, 0)) {
            filename = BaseCompressor.getZRCacheFile(filename);
            if (filename == null) {
                this.handler.sendEmptyMessage(DO_FINISHED);
                return;
            }
            String str;
            if (this.statusHandler != null) {
                this.statusHandler.removeCallbacksAndMessages(null);
            }
            if (!(A.useCssFont || A.forceCssFontName == null)) {
                A.forceCssFontName = null;
                clearTxtView();
                A.setTxtViewTypeface();
            }
            A.tmp_out_file = A.book_cache + "/tmp";
            if (filename.toLowerCase().endsWith(".fb2") && T.isFile(filename + ".zip")) {
                filename = filename + ".zip";
            }
            if (new File(filename).isFile()) {
                A.clearTxts();
                switch (getBookType()) {
                    case 0:
                    case 1:
                        loadTxtHtmlFile(filename);
                        break;
                    case 100:
                        A.loadEBook(filename);
                        break;
                }
                if (T.isOutOfMemoryError && A.isLowestMemory()) {
                    A.saveMemoryLog(T.getFilename(A.lastFile) + " open failed:");
                    System.exit(0);
                } else {
                    T.isOutOfMemoryError = false;
                }
                addHistory(A.lastFile);
                A.clearTxts2();
            }
            A.lastFile = filename;
            A.forceRebootToMain = false;
            PrefSearch.results = null;
            A.loadHighlightAllItems();
            StringBuilder append = new StringBuilder().append("/").append(T.getFileExt(filename));
            if (A.isLandscape()) {
                str = "/landscape/flip_";
            } else {
                str = "/portrait/flip_";
            }
            A.trackPageView(append.append(str).append(A.flip_animation).toString());
        }
    }

    private void addHistory(final String filename) {
        new Thread() {
            public void run() {
                A.addHistory(filename);
            }
        }.start();
    }

    private void initView() {
        this.baseFrame = (FrameLayout) findViewById(R.id.txtFrame);
        this.contentLay = (ContentLay) findViewById(R.id.contentLay);
        this.dualPageBackground = findViewById(R.id.dualPageBackground);
        this.txtLay = findViewById(R.id.txtLay);
        this.shadeView = findViewById(R.id.shadeView);
        this.txtScroll = (ScrollView) findViewById(R.id.txtScrollView01);
        this.txtScroll2 = (ScrollView) findViewById(R.id.txtScrollView02);
        this.txtView = (MRTextView) findViewById(R.id.txtTextView01);
        this.txtView2 = (MRTextView) findViewById(R.id.txtTextView02);
        this.txtScroll.setSmoothScrollingEnabled(false);
        this.txtScroll2.setSmoothScrollingEnabled(false);
        this.txtLayS = (FrameLayout) findViewById(R.id.txtScrollLay);
        this.curl3dLay = (FrameLayout) findViewById(R.id.curl3dLay);
        this.flipView = (FlipImageView) findViewById(R.id.flipImageView);
        this.linkBackIv = (ImageView) findViewById(R.id.linkBackIv);
        this.ttsIv = (ImageView) findViewById(R.id.ttsB);
        this.scrollBlock = (DotImageView) findViewById(R.id.txtScrollPos);
        initReadProgressViews();
        initHighlightViews();
        this.txtScroll.setOnTouchListener(this);
        this.txtScroll2.setOnTouchListener(this);
        this.flipView.setOnTouchListener(this);
        this.linkBackIv.setOnClickListener(this);
        this.ttsIv.setOnClickListener(this);
        setFullscreen(false);
        this.layoutVisible = false;
        this.setLayoutTime = 0;
        initViewParams();
        if (isPdf()) {
            this.baseFrame.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        } else {
            A.loadVisualOptions(false);
        }
        this.baseFrame.setKeepScreenOn(A.keepScreenAwake);
        this.contentLay.setDrawingCacheEnabled(false);
        this.contentLay.setAnimationCacheEnabled(false);
        if (!(A.immersive_fullscreen && A.landscape2PageMode && getBookType() != 7)) {
            initCurl3d(false);
        }
        checkDualPageMode();
        checkLandscape2PagePadding();
    }

    private void initViewParams() {
        A.baseFrame = this.baseFrame;
        A.contentLay = this.contentLay;
        A.txtLay = this.txtLay;
        A.txtView = this.txtView;
        A.txtScroll = this.txtScroll;
        A.txtView2 = this.txtView2;
        A.txtScroll2 = this.txtScroll2;
    }

    private void initCurl3d(boolean forceRecreate) {
        if (this.curl3d != null && !forceRecreate) {
            return;
        }
        if (forceRecreate || A.isFlipCurl() || (A.isFlipNone() && A.isHoriFlipCurl())) {
            if (this.curl3d != null) {
                this.curl3d.clearAllShots();
                this.curl3dLay.removeView(this.curl3d);
                this.handler.sendEmptyMessage(INIT_CURL3D_RENDER);
            }
            if (needInit3DCurl()) {
                this.curl3dLay.setVisibility(0);
            } else {
                this.curl3dLay.setVisibility(4);
            }
            if (dualPageEnabled() && A.pageStyle2PageMode) {
                Rect r = new Rect();
                getResources().getDrawable(R.drawable.bookstyle).getPadding(r);
                int t = (r.top * 25) / 100;
                int l = (r.left * 72) / 100;
                this.curl3dLay.setPadding(l, t, l, t);
            } else {
                this.curl3dLay.setPadding(0, 0, 0, 0);
            }
            this.curl3d = new NewCurl3D(this);
            this.curl3dLay.addView(this.curl3d, new LayoutParams(-1, -1));
            this.curl3d.setEnableTouchPressure(true);
            this.curl3d.setOnScrollListener(this.onFlipCurlingEnd);
            this.curl3d.setViewMode(dualPageEnabled(), A.getScreenWidth2() - (this.curl3dLay.getPaddingLeft() * 2), A.getScreenHeight2() - (this.curl3dLay.getPaddingTop() * 2));
            if (this.curl3dCover == null) {
                this.curl3dCover = new FlipImageView(this);
                this.curl3dLay.addView(this.curl3dCover, new LayoutParams(-1, -1));
            }
            if (needInit3DCurl()) {
                A.setBackgroundImage(this.curl3dCover);
                this.curl3d.renderState = 2;
            }
            this.curl3d.curl3dLay = this.curl3dLay;
            this.curl3d.curl3dCover = this.curl3dCover;
            this.curl3dCover.setVisibility(0);
            this.curl3dCover.bringToFront();
            this.curl3dLay.setOnTouchListener(this);
        }
    }

    private void initCurl3dRender() {
        if (this.curl3d != null && this.curl3d.renderInited == 0) {
            if (needInit3DCurl()) {
                get3dCurlShot(true);
                this.curl3d.updateBitmaps();
                this.curl3d.pageScroll(true);
            }
            this.curl3d.renderInited = 1;
            this.handler.sendEmptyMessage(INIT_CURL3D_RENDER);
        } else if (this.curl3d != null && this.curl3d.renderInited < 2) {
            this.curl3d.renderInited = 2;
            setCurl3dVisibility(false);
        }
    }

    private boolean needInit3DCurl() {
        return VERSION.SDK_INT < 11 || A.isSonyROM || A.isHtcRom;
    }

    private void setCurl3dVisibility(boolean visible) {
        hide_ebook_cover();
        if (visible) {
            this.curl3d.setDurationTime((long) get3dFlipSpeed());
            this.curl3dLay.setVisibility(0);
            return;
        }
        this.flippingAnimationTime = 0;
        this.curl3dLay.setVisibility(8);
        this.curl3dLay.setTag(null);
        if (this.curl3dCover.getVisibility() != 8) {
            this.curl3dCover.setVisibility(8);
            this.curl3dCover.setBackgroundDrawable(null);
            this.curl3dCover.setImageDrawable(null);
        }
        this.contentLay.postInvalidate();
    }

    private int get3dFlipSpeed() {
        return (60 - A.flipSpeed) * 11;
    }

    private boolean get3dCurlShot(boolean front) {
        if (front) {
            this.curlDelayedMsg = null;
        }
        if (front || !this.forceDelayFlipAnimation) {
            try {
                this.getting3dCurlShot = true;
                boolean dualPage = dualPageEnabled();
                if (this.curl3dLay.getWidth() > 0) {
                    this.curl3d.setViewMode(dualPage, this.curl3dLay.getWidth() - (this.curl3dLay.getPaddingLeft() * 2), this.baseFrame.getHeight() - (this.curl3dLay.getPaddingTop() * 2));
                }
                if (front) {
                    if (this.pageDirection == -1) {
                        T.recycle(this.tmpFlipShot3);
                        this.tmpFlipShot3 = null;
                    }
                    this.curl3dLay.setTag(Integer.valueOf(1));
                }
                long curY = getFlipScrollY();
                if (!front) {
                    this.lastFlipScrollY = curY;
                }
                boolean samePage = false;
                if (front) {
                    samePage = this.lastFlipScrollY == curY && !T.isRecycled(this.tmpFlipShot2);
                    if (samePage) {
                        T.recycle(this.tmpFlipShot1);
                        this.tmpFlipShot1 = this.tmpFlipShot2;
                    } else {
                        T.recycle(this.tmpFlipShot2);
                        this.tmpFlipShot1 = getPageShot(false, HighQuality3D());
                        get3dAboutColor();
                    }
                } else {
                    Bitmap pageShot;
                    if (T.isRecycled(this.tmpFlipShot3)) {
                        pageShot = getPageShot(false, HighQuality3D());
                    } else {
                        pageShot = this.tmpFlipShot3;
                    }
                    this.tmpFlipShot2 = pageShot;
                }
                Bitmap curlShot = front ? this.tmpFlipShot1 : this.tmpFlipShot2;
                if (!front) {
                    this.contentLay.setAnimationState(true);
                }
                boolean hasCache = samePage && this.curl3dCover.getTag() != null;
                if (front && this.curl3d.renderInited > 0) {
                    if (hasCache) {
                        this.curl3d.renderState = 2;
                    } else {
                        showCurl3dCover(curlShot);
                    }
                }
                if (dualPage) {
                    if (front && hasCache) {
                        this.curl3d.shots[0] = this.curl3d.shots[2];
                        this.curl3d.shots[1] = this.curl3d.shots[3];
                    } else {
                        Config config = Config.RGB_565;
                        Bitmap bm1 = Bitmap.createBitmap(this.curl3d.pageWidth, this.curl3d.pageHeight, config);
                        Bitmap bm2 = Bitmap.createBitmap(this.curl3d.pageWidth, this.curl3d.pageHeight, config);
                        int t = this.curl3dLay.getPaddingTop();
                        int l = this.curl3dLay.getPaddingLeft();
                        Rect rect = new Rect(l, t, this.curl3d.pageWidth + l, this.curl3d.pageHeight + t);
                        rect = new Rect(0, 0, this.curl3d.pageWidth, this.curl3d.pageHeight);
                        new Canvas(bm1).drawBitmap(curlShot, rect, rect, null);
                        new Canvas(bm2).drawBitmap(curlShot, new Rect(rect.right, t, rect.right + this.curl3d.pageWidth, this.curl3d.pageHeight + t), rect, null);
                        this.curl3d.setPageShot(front ? 0 : 2, bm1, true);
                        this.curl3d.setPageShot(front ? 1 : 3, bm2, true);
                    }
                } else if (front && hasCache) {
                    this.curl3d.shots[0] = this.curl3d.shots[1];
                } else {
                    this.curl3d.setPageShot(front ? 0 : 1, curlShot, true);
                }
                if (this.curl3d.renderInited > 0) {
                    if (front) {
                        pageScroll(this.pageDirection, true, false);
                        saveLastPostion(false);
                        showReadProgress(this.pageDirection == 1 ? 1 : -1);
                        return get3dCurlShot(false);
                    } else if (this.curlDelayedMsg != null) {
                        Message delayedMsg = this.curlDelayedMsg;
                        this.curlDelayedMsg = null;
                        handleCurlMessage(delayedMsg);
                    }
                }
                this.getting3dCurlShot = false;
                return true;
            } catch (OutOfMemoryError e) {
                A.error(e);
                System.gc();
                setCurl3dVisibility(false);
                this.getting3dCurlShot = false;
                return false;
            } catch (Exception e2) {
                A.error(e2);
                this.getting3dCurlShot = false;
                return false;
            }
        }
        if (this.curl3dCover.getVisibility() != 0) {
            showCurl3dCover(this.tmpFlipShot1);
        }
        setCurl3dVisibility(true);
        this.handler.sendEmptyMessageDelayed(DELAY_CURL3D_SHOT2, 10);
        return false;
    }

    private boolean HighQuality3D() {
        if (this.dualPageBackground.getVisibility() != 0 || getDualDrawableRes() == -1 || VERSION.SDK_INT <= 14) {
            return false;
        }
        return true;
    }

    private void get3dAboutColor() {
        if (this.curl3d != null) {
            NewCurl3D newCurl3D = this.curl3d;
            int drawableAboutColor = isPdf() ? A.pdf_back_color : !A.useBackgroundImage ? A.backgroundColor : T.getDrawableAboutColor(A.getBackgroundDrawable(A.backgroundImage));
            newCurl3D.aboutColor = drawableAboutColor;
        }
    }

    private long getFlipScrollY() {
        if (this.pdf != null) {
            return ((long) (this.pdf.pdfView.viewGetPos().page_x + this.pdf.pdfView.viewGetPos().page_y)) + A.lastPosition;
        }
        if (getBookType() != 0) {
            return (long) this.txtScroll.getScrollY();
        }
        saveLastPostion(false);
        return A.lastPosition;
    }

    private void showCurl3dCover(Bitmap bm) {
        try {
            this.curl3d.renderState = 0;
            int l = this.curl3dLay.getPaddingLeft();
            if (l > 0) {
                int t = this.curl3dLay.getPaddingTop();
                int w = this.curl3dLay.getWidth() - (l * 2);
                int h = this.baseFrame.getHeight() - (t * 2);
                Bitmap bm1 = Bitmap.createBitmap(w, h, Config.RGB_565);
                new Canvas(bm1).drawBitmap(bm, new Rect(l, t, l + w, t + h), new Rect(0, 0, w, h), null);
                this.curl3dCover.setImageBitmap(bm1);
            } else if (!T.isRecycled(bm)) {
                this.curl3dCover.setImageBitmap(bm.copy(bm.getConfig(), false));
            }
            this.curl3dCover.setTag(Integer.valueOf(1));
            this.curl3dCoverShowed = true;
            this.curl3dCover.setVisibility(0);
        } catch (OutOfMemoryError e) {
            A.error(e);
        }
    }

    public void setBottomIconsVisibility() {
        int i = 0;
        if (this.topLay != null) {
            ImageButton imageButton = this.b_speak;
            int i2 = ((A.isProVersion || A.inviteProVersion) && A.showSpeakButton) ? 0 : 8;
            imageButton.setVisibility(i2);
            imageButton = this.b_orientation;
            if (A.showOrientationButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_option;
            if (A.showOptionButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_visual;
            if (A.showVisualButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_control;
            if (A.showControlButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_misc;
            if (A.showMiscButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_daynight;
            if (A.showDayNightButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_autoscroll;
            if (A.showAutoscrollButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_bookmark;
            if (A.showBookmarkButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_chapter;
            if (A.showChapterButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_search;
            if (A.showSearchButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_brightness;
            if (A.showBrightnessButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_tilt;
            if (A.showTiltButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            imageButton = this.b_fontsize;
            if (A.showFontSizeButton) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageButton.setVisibility(i2);
            ImageButton imageButton2 = this.b_shutdown;
            if (!A.showShutDownButton) {
                i = 8;
            }
            imageButton2.setVisibility(i);
            this.b_orientation.setOnClickListener(this);
            this.b_option.setOnClickListener(this);
            this.b_visual.setOnClickListener(this);
            this.b_control.setOnClickListener(this);
            this.b_misc.setOnClickListener(this);
            this.b_daynight.setOnClickListener(this);
            this.b_speak.setOnClickListener(this);
            this.b_autoscroll.setOnClickListener(this);
            this.b_bookmark.setOnClickListener(this);
            this.b_chapter.setOnClickListener(this);
            this.menuB.setOnClickListener(this);
            this.b_brightness.setOnClickListener(this);
            this.b_tilt.setOnClickListener(this);
            this.b_fontsize.setOnClickListener(this);
            this.b_shutdown.setOnClickListener(this);
            this.b_search.setOnClickListener(this);
            this.b_orientation.setOnLongClickListener(this);
            this.b_option.setOnLongClickListener(this);
            this.b_visual.setOnLongClickListener(this);
            this.b_control.setOnLongClickListener(this);
            this.b_misc.setOnLongClickListener(this);
            this.b_daynight.setOnLongClickListener(this);
            this.b_speak.setOnLongClickListener(this);
            this.b_autoscroll.setOnLongClickListener(this);
            this.b_bookmark.setOnLongClickListener(this);
            this.b_chapter.setOnLongClickListener(this);
            this.b_brightness.setOnLongClickListener(this);
            this.b_tilt.setOnLongClickListener(this);
            this.b_fontsize.setOnLongClickListener(this);
            this.b_search.setOnLongClickListener(this);
            this.chromeIv.setOnLongClickListener(this);
        }
    }

    private void clearTxtView() {
        this.txtView.setText("");
        this.txtScroll.scrollTo(0, 0);
        this.txtView2.setText("");
        if (this.txtView.getLayout() == null) {
            try {
                this.txtView.assumeLayout();
                this.txtView2.assumeLayout();
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    private ZoomControls getFontZoom() {
        if (this.fontZoom == null) {
            this.fontZoom = new ZoomControls(this);
            LayoutParams lp = new LayoutParams(-2, -2);
            lp.bottomMargin = A.d(15.0f);
            lp.rightMargin = A.d(15.0f);
            lp.gravity = 85;
            this.baseFrame.addView(this.fontZoom, lp);
            this.fontZoom.setOnZoomInClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (ActivityTxt.this.isPdf() && !ActivityTxt.this.pdf.textReflow) {
                        ActivityTxt.this.pdfZoomRatio(0.1f);
                    } else if (ActivityTxt.this.web != null) {
                        ActivityTxt.this.webViewZoom(true);
                    } else {
                        ActivityTxt.this.resetPageCount();
                        A.fontSize += 1.0f;
                        if (A.fontSize > ((float) A.maxFontSize)) {
                            A.fontSize = (float) A.maxFontSize;
                        }
                        T.showToastText(ActivityTxt.this, new DecimalFormat("0.0").format((double) A.fontSize));
                        ActivityTxt.this.setFontSize();
                    }
                }
            });
            this.fontZoom.setOnZoomOutClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (ActivityTxt.this.isPdf() && !ActivityTxt.this.pdf.textReflow) {
                        ActivityTxt.this.pdfZoomRatio(-0.1f);
                    } else if (ActivityTxt.this.web != null) {
                        ActivityTxt.this.webViewZoom(false);
                    } else {
                        ActivityTxt.this.resetPageCount();
                        A.fontSize -= 1.0f;
                        if (A.fontSize < ((float) A.minFontSize)) {
                            A.fontSize = (float) A.minFontSize;
                        }
                        T.showToastText(ActivityTxt.this, new DecimalFormat("0.0").format((double) A.fontSize));
                        ActivityTxt.this.setFontSize();
                    }
                }
            });
        }
        return this.fontZoom;
    }

    private void showOptionsMenu(final boolean fromButton) {
        if (!this.inPreShow) {
            if (A.isInAutoScroll) {
                do_AutoScroll(false);
            }
            if (A.isSpeaking) {
                stop_speak();
            }
            saveLastPostion(true);
            A.SaveOptions(this);
            if (!fromButton) {
                inverseLayoutVisible(true);
            }
            hideProgressDlg();
            hideScrollbarProgress();
            String[] original = getResources().getStringArray(R.array.options_menu);
            CharSequence[] items = new String[]{original[0], original[1], original[2], original[3], "-", original[4], getString(R.string.share) + "...", getString(R.string.menu_3)};
            if (isPdf() && !this.pdf.textReflow) {
                items[0] = "PDF " + getString(R.string.button_options);
            }
            if (fromButton) {
                new MyDialog(this, this.menuB, items, new MenuItemClick() {
                    public void onClick(int which) {
                        ActivityTxt.this.doOptionsMenuClick(which, fromButton);
                    }
                }).show(A.d(48.0f), -A.d(48.0f));
            } else {
               new AlertDialog.Builder(this).setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityTxt.this.doOptionsMenuClick(which, fromButton);
                    }
                }).show();
            }
        }
    }

    protected void showMoreOptionsMenu(boolean fromButton) {
        CharSequence[] items;
        String[] original = getResources().getStringArray(R.array.more_options_menu);
        int i;
        if (A.isProVersion || A.inviteProVersion) {
            items = new String[(original.length + 2)];
            items[0] = getString(R.string.speak);
            for (i = 0; i < original.length; i++) {
                items[i + 1] = original[i];
            }
            items[items.length - 1] = getString(R.string.button_brightness);
        } else {
            items = new String[(original.length + 1)];
            for (i = 0; i < original.length; i++) {
                items[i] = original[i];
            }
            items[items.length - 1] = getString(R.string.button_brightness);
        }
        if (fromButton) {
            new MyDialog(this, this.menuB, items, new MenuItemClick() {
                public void onClick(int which) {
                    ActivityTxt.this.doMoreOptionsMenuClick(which);
                }
            }).show(A.d(48.0f), -A.d(48.0f));
        } else {
           new AlertDialog.Builder(this).setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityTxt.this.doMoreOptionsMenuClick(which);
                }
            }).show();
        }
    }

    private void doOptionsMenuClick(int which, boolean fromButton) {
        switch (which) {
            case 0:
                showVisualOptions();
                return;
            case 1:
                showControlOptions();
                return;
            case 2:
                showMiscOptions();
                return;
            case 3:
                showThemeWindow();
                return;
            case 5:
                showMoreOptionsMenu(fromButton);
                return;
            case 6:
                shareCurrentBook(fromButton);
                return;
            case 7:
                A.showAboutDialog(this, false);
                return;
            default:
                return;
        }
    }

    private void shareCurrentBook(boolean fromButton) {
        CharSequence[] items = new String[]{getString(R.string.share), getString(R.string.send_file), getString(R.string.bug_report)};
        if (fromButton) {
            new MyDialog(this, this.menuB, items, new MenuItemClick() {
                public void onClick(int which) {
                    ActivityTxt.this.doShareMenu(which);
                }
            }).show(A.d(48.0f), -A.d(48.0f));
        } else {
           new AlertDialog.Builder(this).setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityTxt.this.doShareMenu(which);
                }
            }).show();
        }
    }

    private void doShareMenu(int which) {
        inverseLayoutVisible(true);
        if (which == 0) {
            doShareReadState();
        }
        if (which == 1) {
            A.sendFile(this, A.lastFile);
        }
        if (which == 2) {
            String filename = BaseCompressor.getZRCacheFile(A.lastFile);
            String device = A.createAppInfo().toString();
            String text = "Please describe how to repeat the bug:\n-------------\n" + device + "\n-------------\n";
            String[] tos = new String[]{"seanyword@gmail.com"};
            Bitmap bm = getPageShot(false, false);
            String screenshot = A.book_cache + "/screenshot.jpg";
            T.bitmapToFile(bm, screenshot);
            ArrayList<Uri> uris = new ArrayList();
            uris.add(Uri.fromFile(new File(screenshot)));
            uris.add(Uri.fromFile(new File(filename)));
            T.copyFile(A.xml_files_folder + "/" + A.OPTIONS_FILE + ".xml", A.book_cache + "/settings.txt", true);
            uris.add(Uri.fromFile(new File(A.book_cache + "/settings.txt")));
            Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
            intent.putExtra("android.intent.extra.EMAIL", tos);
            intent.putExtra("android.intent.extra.SUBJECT", "Bug report: v4.1.2");
            intent.putExtra("android.intent.extra.TEXT", text);
            intent.setType("*/*");
            intent.putParcelableArrayListExtra("android.intent.extra.STREAM", uris);
            startActivity(Intent.createChooser(intent, null));
            A.myDebugTag++;
            if (A.myDebugTag > 1) {
                T.showToastText(this, device, 1);
            }
        }
    }

    private String getShareTitle(int type) {
        String text = A.getBookName();
        if (getBookType() != 100 || A.ebook == null || A.ebook.getAuthor().equals("")) {
            return text;
        }
        return text + (type == 0 ? " - " : ",") + A.ebook.getAuthor();
    }

    private void showThemeWindow() {
        inverseLayoutVisible(true);
        final boolean italic = A.fontItalic;
        hideScrollbarProgress();
        new PrefTheme(this, new OnGetTheme() {
            public void getTheme(String name) {
                if (!ActivityTxt.this.isPdf() || ActivityTxt.this.pdf.textReflow) {
                    ActivityTxt.this.loadTheme(name, false);
                    A.saveTheme(A.TEMP_THEME, true);
                    if (ActivityTxt.this.web != null) {
                        ActivityTxt.this.reloadWebView();
                    }
                    if (italic != A.fontItalic && A.textJustified) {
                        ActivityTxt.this.saveLastPostion(true);
                        ActivityTxt.this.restartReaderToTxt();
                        return;
                    }
                    return;
                }
                ActivityTxt.this.pdfRemoveThumb();
                A.pdf_theme = Integer.valueOf(name).intValue();
                ActivityTxt.this.pdfUpdateView(true);
                ActivityTxt.this.setStatusBarProperties(true);
            }
        }, false).show();
    }

    private void checkLollipopFullScreenState() {
        if (!A.fullscreen && VERSION.SDK_INT >= 21) {
            getWindow().addFlags(Integer.MIN_VALUE);
            getWindow().setStatusBarColor(ViewCompat.MEASURED_STATE_MASK);
        }
    }

    public void showMiscOptions() {
        inverseLayoutVisible(true);
        final boolean preFullScreen = A.fullscreen;
        final boolean preChapterEndPrompt = A.chapterEndPrompt;
        final boolean useCssFont = A.useCssFont;
        final boolean preBright = A.adjustBrightness;
        final boolean trimBlank = A.trimBlankSpace;
        final boolean indentParagraph = A.indentParagraph;
        final int indentLength = A.indentLength;
        final boolean tilt_turn_page = A.tilt_turn_page;
        final boolean landscape2Page = A.landscape2PageMode;
        final String disableCSS = "" + A.disableCSS + A.cssFontStyle + A.cssFontColor + A.cssFontSize + A.cssAlignment + A.cssJustify + A.cssIndent + A.cssLineSpace + A.cssOthers;
        final boolean bluelightEnable = A.bluelightEnable;
        final int bluelightOpacity = A.bluelightOpacity;
        saveLastPostion(true);
        PrefMisc pm = new PrefMisc(this);
        pm.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                A.setSystemUiVisibility(true);
                ActivityTxt.this.setBottomIconsVisibility();
                if (preFullScreen != A.fullscreen) {
                    ActivityTxt.this.checkLollipopFullScreenState();
                    ActivityTxt.this.setFullscreen(false);
                }
                if (preBright != A.adjustBrightness) {
                    if (A.adjustBrightness) {
                        ActivityTxt.this.setScreenBrightness(A.brightnessValue, false);
                    } else {
                        ActivityTxt.this.setScreenBrightness(-100, false);
                    }
                    ActivityTxt.this.setLeds();
                }
                if (landscape2Page != A.landscape2PageMode && A.isLandscape() && !ActivityTxt.this.isPdf()) {
                    ActivityTxt.this.restartReaderToTxt();
                } else if (trimBlank == A.trimBlankSpace && indentParagraph == A.indentParagraph && indentLength == A.indentLength) {
                    String disableCSS2 = "" + A.disableCSS + A.cssFontStyle + A.cssFontColor + A.cssFontSize + A.cssAlignment + A.cssJustify + A.cssIndent + A.cssLineSpace + A.cssOthers;
                    if (!(useCssFont == A.useCssFont && preChapterEndPrompt == A.chapterEndPrompt && disableCSS.equals(disableCSS2)) && (ActivityTxt.this.getBookType() == 100 || ActivityTxt.this.getBookType() == 1)) {
                        ActivityTxt.this.createProgressDlg(A.lastFile);
                        A.ebook = null;
                        ActivityTxt.this.handler.post(ActivityTxt.this);
                    }
                } else {
                    A.resetHighlights();
                    if (ActivityTxt.this.getBookType() == 0) {
                        A.clearTxts2();
                        ActivityTxt.this.showTxtByPosition(A.lastPosition, null);
                    } else {
                        ActivityTxt.this.clearpreNextCache();
                        if (trimBlank != A.trimBlankSpace) {
                            ActivityTxt.this.restartReaderToTxt();
                        } else {
                            ActivityTxt.this.handler.post(ActivityTxt.this);
                        }
                    }
                }
                if (tilt_turn_page != A.tilt_turn_page) {
                    if (A.tilt_turn_page) {
                        ActivityTxt.this.askedForTilt = false;
                    }
                    if (ActivityTxt.this.shakeSensorLisener != null) {
                        ActivityTxt.this.shakeSensorLisener.tilt_turn_page = A.tilt_turn_page;
                    }
                    ActivityTxt.this.registerHardwares();
                }
                ActivityTxt.this.baseFrame.setKeepScreenOn(A.keepScreenAwake);
                ActivityTxt.this.checkStatusBar();
                ActivityTxt.this.checkDualPageMode();
                if (bluelightEnable != A.bluelightEnable || (A.bluelightEnable && bluelightOpacity != A.bluelightOpacity)) {
                    ActivityTxt.this.showShadeCoverOnView();
                }
            }
        });
        pm.show();
    }

    public void showControlOptions() {
        inverseLayoutVisible(true);
        final int preScreenState = A.screenState;
        PrefControl pc = new PrefControl(this);
        pc.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                A.setSystemUiVisibility(true);
                if (preScreenState != A.screenState) {
                    ActivityTxt.this.isPressScreenStateButton = false;
                    ActivityTxt.this.setScreenOrientation();
                }
                ActivityTxt.this.registerHardwares();
            }
        });
        pc.show();
    }

    public void showVisualOptions() {
        inverseLayoutVisible(true);
        saveLastPostion(true);
        final int flip_animation = A.flip_animation;
        if (!isPdf() || this.pdf.textReflow) {
            final boolean italic = A.fontItalic;
            final String hyphenationLang = A.hyphenationLang;
            PrefVisual pv = new PrefVisual(this);
            pv.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    A.setSystemUiVisibility(true);
                    if (italic != A.fontItalic && A.textJustified && ActivityTxt.this.getBookType() != 0) {
                        ActivityTxt.this.restartReaderToTxt();
                    } else if (flip_animation != A.flip_animation && A.isFlipCurl()) {
                        if (ActivityTxt.this.curl3d == null || ActivityTxt.this.curl3d.mTurnType != ActivityTxt.this.curl3d.getStyle()) {
                            ActivityTxt.this.restartReaderToTxt();
                        } else {
                            ActivityTxt.this.curl3d.clearAllShots();
                        }
                    }
                    if (ActivityTxt.this.web == null) {
                        ActivityTxt.this.checkStatusBar();
                    } else {
                        ActivityTxt.this.reloadWebView();
                    }
                    if (ActivityTxt.this.showStatusbar()) {
                        ActivityTxt.this.setStatusBarProperties(true);
                    }
                    if (!A.textHyphenation || hyphenationLang.equals(A.hyphenationLang)) {
                        ActivityTxt.this.resetFlipCache(true, 500);
                        return;
                    }
                    A.setHyphenation();
                    ActivityTxt.this.handler.post(ActivityTxt.this);
                }
            });
            pv.show();
            return;
        }
        PrefPdf pp = new PrefPdf(this);
        pp.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                A.setSystemUiVisibility(true);
                if (flip_animation != A.flip_animation && A.isFlipCurl()) {
                    if (ActivityTxt.this.curl3d == null || ActivityTxt.this.curl3d.mTurnType != ActivityTxt.this.curl3d.getStyle()) {
                        ActivityTxt.this.restartReaderToTxt();
                    } else {
                        ActivityTxt.this.curl3d.clearAllShots();
                    }
                }
            }
        });
        pp.show();
    }

    protected void showExitMenu() {
        terminate_reader();
    }

    private void doMoreOptionsMenuClick(int which) {
        inverseLayoutVisible(true);
        if (A.isProVersion || A.inviteProVersion) {
            which--;
        }
        switch (which) {
            case -1:
                do_speak();
                return;
            case 0:
                do_text_select(false);
                return;
            case 1:
                do_search();
                return;
            case 2:
                do_book_info();
                return;
            case 3:
                do_show_chapters();
                return;
            case 4:
                do_prior_chapter();
                return;
            case 5:
                do_next_chapter();
                return;
            case 6:
                do_prior_file();
                return;
            case 7:
                do_next_file();
                return;
            case 8:
                do_bookmark();
                return;
            case 9:
                inverseLayoutVisible(false);
                return;
            case 10:
                do_font_size();
                return;
            case 11:
                do_brightness();
                return;
            default:
                return;
        }
    }

    private void do_prior_chapter() {
        boolean found1 = false;
        switch (getBookType()) {
            case 0:
            case 1:
                if (A.getTxtChapters().size() > 0) {
                    int chapterId = A.getChapterId(A.lastPosition);
                    if (chapterId > 0) {
                        A.lastPosition = A.getTxtChapterDisplayPosition(chapterId - 1);
                        found1 = true;
                        this.handler.post(this);
                        T.showToastText(this, ((TxTChapter) A.getTxtChapters().get(chapterId - 1)).chapter_trim);
                        break;
                    }
                }
                break;
            case 100:
                if (A.lastChapter > 0) {
                    found1 = true;
                    A.lastChapter--;
                    A.lastPosition = 0;
                    A.lastSplitIndex = 0;
                    this.ebookPageUp = false;
                    clearTxtView();
                    A.SaveOptions(this);
                    if (isBigChapter(A.lastChapter)) {
                        createProgressDlg(((Chapter) A.ebook.getChapters().get(A.lastChapter)).name);
                    }
                    this.handler.post(this);
                    break;
                }
                break;
        }
        if (found1) {
            resetFlipCache(false, 1000);
        } else {
            T.showToastText(this, getString(R.string.no_prior_chapter));
        }
    }

    private void do_next_chapter() {
        boolean found2 = false;
        switch (getBookType()) {
            case 0:
            case 1:
                if (A.getTxtChapters().size() > 0) {
                    int chapterId = A.getChapterId(A.lastPosition);
                    if (chapterId != 0 && chapterId < A.getTxtChapters().size() - 1) {
                        found2 = true;
                        A.lastPosition = A.getTxtChapterDisplayPosition(chapterId + 1);
                        this.handler.post(this);
                        T.showToastText(this, ((TxTChapter) A.getTxtChapters().get(chapterId + 1)).chapter_trim);
                        break;
                    }
                }
                break;
            case 100:
                if (A.ebook != null && A.lastChapter < A.ebook.getChapters().size() - 1) {
                    found2 = true;
                    A.lastChapter++;
                    A.lastPosition = 0;
                    A.lastSplitIndex = 0;
                    this.ebookPageUp = false;
                    clearTxtView();
                    A.SaveOptions(this);
                    this.ebook_inLoading = true;
                    if (isBigChapter(A.lastChapter)) {
                        createProgressDlg(((Chapter) A.ebook.getChapters().get(A.lastChapter)).name);
                    }
                    this.handler.post(this);
                    break;
                }
        }
        if (found2) {
            resetFlipCache(false, 1000);
        } else {
            T.showToastText(this, getString(R.string.no_next_chapter));
        }
    }

    public void inverseLayoutVisible(boolean forceHide) {
        updateBarTime();
        hide_ebook_cover();
        if (!this.inPreShow) {
            hideFontZoom();
            hideBrightnessPanel();
            pdfHideSearchLay();
            if (this.setLayoutTime != 0 && SystemClock.elapsedRealtime() - this.setLayoutTime <= 500) {
                return;
            }
            if (!this.layoutVisible && !forceHide) {
                if (A.isInAutoScroll) {
                    do_AutoScroll(false);
                }
                if (A.isSpeaking) {
                    stop_speak();
                }
                if (A.fullscreen) {
                    getWindow().addFlags(2048);
                }
                if (initTopBottomLay()) {
                    setFlipViewINVISIBLE(true);
                    setCurlViewINVISIBLE();
                    restoreAppendedBottomHandler(false);
                    pdfSetImmersiveMode(true);
                    setFullscreen(true);
                    layoutAnimationShow();
                    pdfShowThumb(true);
                    pdfShowLockIcon(true);
                    setBarVisible(0);
                    setChromeVisibility(true);
                    this.nav_return.setVisibility(8);
                    this.layoutVisible = true;
                    showSeekBarProgress();
                    this.setLayoutTime = SystemClock.elapsedRealtime();
                    hideLinkBackButton();
                    showBottomIconsHint();
                    A.setSystemUiVisibility(false);
                    this.flippingAnimationTime = 0;
                    A.moveStart = this.moveStart2;
                }
            } else if (this.layoutVisible) {
                restoreAppendedBottomHandler(true);
                if (this.topLay.getVisibility() == 0) {
                    if (A.fullscreen) {
                        getWindow().clearFlags(2048);
                    }
                    layoutAnimationHide();
                }
                pdfShowThumb(false);
                pdfShowLockIcon(false);
                setBarVisible(4);
                setChromeVisibility(false);
                this.layoutVisible = false;
                this.setLayoutTime = SystemClock.elapsedRealtime();
                A.setSystemUiVisibility(true);
                pdfSetImmersiveMode(false);
                A.moveStart = this.moveStart2;
            }
        } else if (!forceHide && new File(A.lastFile).length() > A.BIG_FILESIZE) {
            this.showHintAtLoadingEnd = true;
            T.showToastText(this, getString(R.string.loading_info));
        }
    }

    private void setBarVisible(int visible) {
        int i = -2039584;
        if (this.topLay != null && this.bottomLay != null) {
            if (visible == 0) {
                int i2;
                this.topLay.findViewById(R.id.sv).setBackgroundColor(A.isNightState() ? -11513776 : -14776091);
                this.topLay.findViewById(R.id.notificationBar).setBackgroundColor(A.isNightState() ? -13421773 : -15374912);
                View findViewById = this.bottomLay.findViewById(R.id.sv);
                if (A.isNightState()) {
                    i2 = -12434878;
                } else {
                    i2 = -2039584;
                }
                findViewById.setBackgroundColor(i2);
                TextView textView = this.percentView;
                if (!A.isNightState()) {
                    i = -10395295;
                }
                textView.setTextColor(i);
            }
            this.topLay.setVisibility(visible);
            this.bottomLay.setVisibility(visible);
        }
    }

    private boolean hideBrightnessPanel() {
        if (this.brightnessPanel == null || this.brightnessPanel.getVisibility() != 0) {
            return false;
        }
        this.brightnessPanel.setVisibility(8);
        return true;
    }

    private boolean hideFontZoom() {
        if (this.fontZoom == null || getFontZoom().getVisibility() != 0) {
            return false;
        }
        getFontZoom().setVisibility(8);
        return true;
    }

    private boolean initTopBottomLay() {
        if (this.topLay != null) {
            return true;
        }
        this.topLay = ((ViewStub) findViewById(R.id.viewStub_top)).inflate();
        this.bottomLay = ((ViewStub) findViewById(R.id.viewStub_bottom)).inflate();
        this.topLay.findViewById(R.id.notificationBar).setVisibility(8);
        this.chromeIv = (ImageView) this.topLay.findViewById(R.id.chromeSwitch);
        this.pdfPenIv = (ImageView) this.topLay.findViewById(R.id.pdfPen);
        this.progressSK = (SeekBar) this.bottomLay.findViewById(R.id.txtSeekBar01);
        this.backLay = this.topLay.findViewById(R.id.txtBackLay);
        this.titleTextView = (TextView) this.topLay.findViewById(R.id.txtTitle);
        this.menuB = this.topLay.findViewById(R.id.menuB);
        this.nav_return = (ImageView) this.bottomLay.findViewById(R.id.nav_return);
        this.percentView = (TextView) this.bottomLay.findViewById(R.id.txtTextViewPercent);
        this.b_orientation = (ImageButton) this.bottomLay.findViewById(R.id.b_orientation);
        this.b_option = (ImageButton) this.bottomLay.findViewById(R.id.b_option);
        this.b_visual = (ImageButton) this.bottomLay.findViewById(R.id.b_visual);
        this.b_control = (ImageButton) this.bottomLay.findViewById(R.id.b_control);
        this.b_misc = (ImageButton) this.bottomLay.findViewById(R.id.b_misc);
        this.b_daynight = (ImageButton) this.bottomLay.findViewById(R.id.b_daynight);
        this.b_speak = (ImageButton) this.bottomLay.findViewById(R.id.b_speak);
        this.b_autoscroll = (ImageButton) this.bottomLay.findViewById(R.id.b_autoscroll);
        this.b_bookmark = (ImageButton) this.bottomLay.findViewById(R.id.b_bookmark);
        this.b_chapter = (ImageButton) this.bottomLay.findViewById(R.id.b_chapter);
        this.b_search = (ImageButton) this.bottomLay.findViewById(R.id.b_search);
        this.b_brightness = (ImageButton) this.bottomLay.findViewById(R.id.b_brightness);
        this.b_tilt = (ImageButton) this.bottomLay.findViewById(R.id.b_tilt);
        this.b_fontsize = (ImageButton) this.bottomLay.findViewById(R.id.b_fontsize);
        this.b_shutdown = (ImageButton) this.bottomLay.findViewById(R.id.b_shutdown);
        this.buttonsLay = this.bottomLay.findViewById(R.id.buttonsLay);
        if (A.isTablet) {
            this.topLay.findViewById(R.id.sv).getLayoutParams().height = A.d(64.0f);
            this.buttonsLay.getLayoutParams().height = A.d(60.0f);
        }
        setBarVisible(4);
        setBottomIconsVisibility();
        this.backLay.setOnClickListener(this);
        this.titleTextView.setOnClickListener(this);
        this.percentView.setOnClickListener(this);
        this.nav_return.setOnClickListener(this);
        this.progressSK.setOnSeekBarChangeListener(this.seekBarChanged);
        this.chromeIv.setOnClickListener(this);
        this.pdfPenIv.setOnClickListener(this);
        new Handler() {
            public void handleMessage(Message msg) {
                ActivityTxt.this.inverseLayoutVisible(false);
            }
        }.sendEmptyMessage(0);
        return false;
    }

    private void showBottomIconsHint() {
        if (A.hintForBottomBar) {
            boolean z;
            boolean z2 = A.runCount;
            if (A.isProVersion) {
                z = false;
            } else {
                z = true;
            }
            if (z2 > z && this.layoutVisible) {
                A.customizeBottomIcons(this, true);
            }
        }
    }

    private void layoutAnimationShow() {
        if (!dontShowLayoutAnimation()) {
            Animation animation1 = new AlphaAnimation(0.1f, 1.0f);
            animation1.setDuration(250);
            Animation animation2 = new TranslateAnimation(0.0f, 0.0f, (float) (((-this.topLay.getHeight()) - this.topLayPaddingTop) - this.sysbar_height), (float) this.topLayPaddingTop);
            animation2.setDuration(250);
            this.topLay.startAnimation(animation2);
            this.bottomLay.startAnimation(animation1);
            if (this.pdfLockIv != null) {
                this.pdfLockIv.startAnimation(animation1);
            }
        }
    }

    private void layoutAnimationHide() {
        if (!isPdf() && !dontShowLayoutAnimation()) {
            Animation animation1 = new AlphaAnimation(1.0f, 0.1f);
            animation1.setDuration(100);
            this.topLay.startAnimation(animation1);
            this.bottomLay.startAnimation(animation1);
        }
    }

    private boolean dontShowLayoutAnimation() {
        if (this.topLay == null || this.bottomLay == null || A.statusOnBottom || A.verticalAlignment) {
            return true;
        }
        if (!isPdf() || this.pdf == null || A.pdf_theme != 1 || this.pdf.textReflow) {
            return false;
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.isPaused) {
            return false;
        }
        if (this.layoutVisible && isDPadKey(keyCode)) {
            return super.onKeyDown(keyCode, event);
        }
        recordUserActiveTime();
        this.setScreenAwakeTime = 0;
        this.keyDownCount++;
        checkScreenAwake();
        if (isWebViewKeyDown(keyCode, event)) {
            return true;
        }
        if (keyCode == 24 || keyCode == 92 || keyCode == SHOW_PDF) {
            if (A.isSpeaking || this.mp3PlayState == 1) {
                return false;
            }
            if (A.isInAutoScroll) {
                autoScrollTurnPage(-1, true);
                return true;
            } else if (this.ebook_inLoading) {
                return true;
            }
        }
        if (keyCode == 25 || keyCode == 93 || keyCode == SHOW_PDF2) {
            if (A.isSpeaking || this.mp3PlayState == 1) {
                return false;
            }
            if (A.isInAutoScroll) {
                autoScrollTurnPage(1, true);
                return true;
            } else if (this.ebook_inLoading) {
                return true;
            }
        }
        if (doKeyDown(keyCode)) {
            return true;
        }
        this.isBackKeyDown = false;
        if (keyCode == 4) {
            if (hideTipsPanel()) {
                return true;
            }
            if (pdfHideAnnotLay(true, true)) {
                return true;
            }
            if (hideDotViews()) {
                return true;
            }
            if (hideFontZoom()) {
                return true;
            }
            if (hideBrightnessPanel()) {
                return true;
            }
            if (pdfHideSearchLay()) {
                return true;
            }
            if (A.isInAutoScroll) {
                do_AutoScroll(false);
                return true;
            } else if (A.isSpeaking) {
                if (this.tts_panel_visible) {
                    showTtsPanel(false);
                } else {
                    stop_speak();
                }
                return true;
            } else {
                if (this.layoutVisible) {
                    inverseLayoutVisible(true);
                    if (!A.immersive_fullscreen) {
                        return true;
                    }
                }
                A.forceRebootToMain = true;
                this.isBackKeyDown = true;
            }
        }
        if (isDPadKey(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.isPaused) {
            return false;
        }
        if (this.layoutVisible && isDPadKey(keyCode)) {
            return super.onKeyUp(keyCode, event);
        }
        this.keyDownCount = 0;
        if (isWebViewKeyUp(keyCode, event)) {
            return true;
        }
        if (this.layoutVisible) {
            switch (keyCode) {
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                    return false;
            }
        }
        switch (keyCode) {
            case 3:
                if (A.doHomeKey != 15) {
                    return true;
                }
                break;
            case 4:
                if (this.isBackKeyDown) {
                    if (A.doBackKey == 15 && !forceExitRestart()) {
                        doFinish();
                    }
                    return true;
                }
                break;
            case 21:
                if (A.doDPadLeft != 15) {
                    return true;
                }
                break;
            case 22:
                if (A.doDPadRight != 15) {
                    return true;
                }
                break;
            case 23:
                if (A.doDPadCenter != 15) {
                    return true;
                }
                break;
            case 24:
            case 92:
            case SHOW_PDF /*105*/:
                if (A.isSpeaking || this.mp3PlayState == 1) {
                    return false;
                }
                if (A.isInAutoScroll || this.ebook_inLoading || A.doVolumeKeyUp != 15) {
                    return true;
                }
                break;
            case 25:
            case 93:
            case SHOW_PDF2 /*106*/:
                if (A.isSpeaking || this.mp3PlayState == 1) {
                    return false;
                }
                if (A.isInAutoScroll || this.ebook_inLoading || A.doVolumeKeyDown != 15) {
                    return true;
                }
                break;
            case 27:
                if (A.doCameraKey != 15) {
                    return true;
                }
                break;
            case 82:
                if (A.doMenuKey == 15) {
                    inverseLayoutVisible(false);
                }
                return true;
            case 84:
                if (A.doSearchKey != 15) {
                    return true;
                }
                return true;
        }
        if (isDPadKey(keyCode)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private boolean isDPadKey(int keyCode) {
        boolean result = keyCode == 20 || keyCode == 19 || keyCode == 21 || keyCode == 22 || keyCode == 23;
        if (result) {
            this.dPadKeyPressed = true;
        }
        return result;
    }

    private long getAboutPosition() {
        int index = 0;
        int y = this.txtScroll.getScrollY();
        int h = this.txtView.getRealHeight();
        if (h == 0) {
            return 0;
        }
        switch (getBookType()) {
            case 0:
                int addedLength = (A.getBlocksLength(A.lastBlockIndex) * y) / h;
                if (A.lastBlockIndex != 0) {
                    index = A.lastBlockIndex - 1;
                }
                return (long) (A.getPriorTxtLength(index) + addedLength);
            case 1:
                return (long) ((this.htmlText.length() * y) / h);
            case 100:
                if (A.ebook.getChapters().size() == 0) {
                    return 0;
                }
                int index2;
                long addedLength2 = (((Chapter) A.ebook.getChapters().get(A.lastChapter)).size * ((long) y)) / ((long) h);
                if (A.lastChapter == 0) {
                    index2 = 0;
                } else {
                    index2 = A.lastChapter - 1;
                }
                return ((long) BaseEBook.getPriorTextLength2(index2)) + addedLength2;
            default:
                return 0;
        }
    }

    private void showSeekBarProgress() {
        long p = 0;
        long p2 = 1000;
        if (this.layoutVisible) {
            try {
                switch (getBookType()) {
                    case 0:
                        this.titleTextView.setText(T.getFilename(A.lastFile));
                        if (A.txtLength() > 0) {
                            setSeekBarProgress(isEndOfBook() ? 1000 : (getAboutPosition() * 1000) / A.txtLength());
                            return;
                        } else {
                            this.progressSK.setProgress(0);
                            return;
                        }
                    case 1:
                        this.titleTextView.setText(T.getFilename(A.lastFile));
                        if (isEndOfBook()) {
                            p = 1000;
                        } else if (!A.noSplitHtmls()) {
                            p = (getCurrentPosition() * 1000) / getBookLength();
                        } else if (this.htmlText.length() > 0) {
                            p = (getCurrentPosition() * 1000) / getBookLength();
                        }
                        setSeekBarProgress(p);
                        return;
                    case 7:
                        this.titleTextView.setText(A.getBookName());
                        int total = this.m_doc.GetPageCount();
                        this.progressSK.setMax(total - 1);
                        this.progressSK.setProgress(pdfGetCurrPageNo());
                        this.percentView.setText(this.statusLay.getVisibility() == 0 ? this.statusRight.getText().toString() : "" + (pdfGetCurrPageNo() + 1) + "/" + total);
                        return;
                    case 100:
                        this.titleTextView.setText(A.ebook.getBookName() + (A.ebook.getAuthor().equals("") ? "" : " - " + A.ebook.getAuthor()));
                        if (getEBookTotalSize() > 0) {
                            saveLastPostion(false);
                            if (!isEndOfBook()) {
                                p2 = (getCurrentPosition() * 1000) / getEBookTotalSize();
                            }
                            setSeekBarProgress(p2);
                            return;
                        }
                        this.progressSK.setProgress(0);
                        return;
                    default:
                        return;
                }
            } catch (Exception e) {
                A.error(e);
            }
            A.error(e);
        }
    }

    private void setSeekBarProgress(long p) {
        this.progressSK.setProgress((int) p);
    }

    private int getSeekBarProgress(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 1000) {
            return 1000;
        }
        return progress;
    }

    private void hideScrollbarProgress() {
        if (this.scrollBlock.getVisibility() != 4) {
            this.scrollBlock.setVisibility(4);
        }
    }

    private void showScrollbarProgress(boolean always) {
        this.scrollBlock.setVisibility(8);
        if (!showStatusbar() && this.web == null && A.isFlipNone() && !this.tmpHorizaontalScroll && !this.inPreShow && !dualPageEnabled() && !isPdf()) {
            int h = T.getScreenHeight(this) - (showStatusbar() ? this.statusLay.getHeight() : 0);
            if (this.txtView.getRealHeight() >= h * 2) {
                switch (getBookType()) {
                    case 0:
                        if (A.getTxts().size() == 0) {
                            return;
                        }
                        break;
                    case 1:
                        if (this.htmlText.length() == 0) {
                            return;
                        }
                        break;
                }
                int bw = this.scrollBlock.getWidth();
                int bh = this.scrollBlock.getHeight();
                long p = 0;
                int pageCount = 1;
                switch (getBookType()) {
                    case 0:
                        p = (getAboutPosition() * ((long) h)) / (A.txtLength() > 0 ? A.txtLength() : (long) h);
                        pageCount = (int) (getTotalPages() / 100000);
                        break;
                    case 1:
                    case 100:
                        pageCount = (this.txtView.getRealHeight() / A.getPageHeight()) + 1;
                        p = (long) ((this.txtScroll.getScrollY() * h) / this.txtView.getRealHeight());
                        break;
                }
                int ad = h / pageCount;
                p += (((long) ad) * p) / ((long) h);
                int l = (A.getScreenWidth2() - bw) - 1;
                int t = (int) p;
                if (t > h - bh || (!A.isTablet && p > ((long) (h - ((ad * 2) / 3))))) {
                    t = (h - bh) - 1;
                }
                if (t < 2) {
                    t = 2;
                }
                this.scrollBlock.layout(l, t, l + bw, t + bh);
                this.scrollBlock.setVisibility(0);
                if (!always) {
                    Animation animation2 = new AlphaAnimation(0.8f, 0.1f);
                    animation2.setDuration(500);
                    this.scrollBlock.clearAnimation();
                    this.scrollBlock.startAnimation(animation2);
                    this.scrollBlock.setVisibility(8);
                }
            }
        }
    }

    private void pageScroll(int direction) {
        pageScroll(direction, false, false);
        saveLastPostion(false);
    }

    private void pageScroll(int direction, boolean fromCurlFlip, boolean forCacheOnly) {
        recordUserActiveTime();
        if ((forCacheOnly || !waitingForCahingShots()) && !ignoreFirstOrLastPageFlip(direction)) {
            try {
                MyLayout lo = this.txtView.getLayout();
                if (lo != null || isPdf()) {
                    int y = this.txtScroll.getScrollY();
                    int svH = A.getPageHeight();
                    int lh = this.txtView.getLineHeight();
                    boolean isPageDown = direction == 1;
                    if (!forCacheOnly) {
                        if (!this.ebook_inLoading && !flippingAnimation() && !hide_ebook_cover() && SystemClock.elapsedRealtime() - this.changeChapterTime >= 500) {
                            this.longTimeTapEvent = false;
                            hideLinkBackButton();
                            inverseLayoutVisible(true);
                            hideDotViews();
                            A.moveStart = false;
                            this.registerHardwaresTime = -1;
                            this.pageDirection = direction;
                            if (!(A.isInAutoScroll || A.isSpeaking || (((!this.tmpHorizaontalScroll || !A.isFlipNone()) && (fromCurlFlip || !A.isFlipCurl())) || (!A.isHoriFlipCurl() && this.tmpHorizaontalScroll)))) {
                                this.tmpHorizaontalScroll = false;
                                if (doCurlFlip()) {
                                    return;
                                }
                            }
                            playPageSound();
                            if (this.pdf == null || !this.pdf.textReflow) {
                                boolean z = this.forceTiltSpeed != 0.0f && A.isFlipNone();
                                saveFlipShot(z);
                            }
                        } else {
                            return;
                        }
                    }
                    if (!pdfPageScroll(isPageDown)) {
                        if (!forCacheOnly) {
                            statistics_add();
                            this.pageScrollTime = SystemClock.elapsedRealtime();
                        }
                        if (getBookType() == 0) {
                            int curLine = getNextPageLine2(isPageDown, forCacheOnly);
                            int atLeast = (txtKeepPage(0) * svH) + (lh * 2);
                            boolean loadPrior = A.lastBlockIndex > 1 && y < atLeast;
                            boolean loadNext = A.lastBlockIndex < A.getTxts().size() + -2 && this.txtView.getHeight() - ((txtKeepPage(0) * svH) + y) < atLeast;
                            if (loadPrior || loadNext) {
                                A.log("reload txt..");
                                this.txtReload = true;
                                if (!this.inPreShow) {
                                    int p = lo.getLineStart(curLine);
                                    String veri = p < this.txtView.getText2().length() + -20 ? this.txtView.getText2().substring(p, p + 20) : null;
                                    A.lastPosition = A.getTxtRealPos((long) p);
                                    showTxtByPosition(A.lastPosition, veri);
                                }
                            } else {
                                txtScrollTo(this.txtView.getLineTop2(curLine));
                            }
                        } else {
                            int y2;
                            boolean dualPageEnabled = dualPageEnabled();
                            boolean forceToNextChapter = false;
                            if (isPageDown) {
                                try {
                                    if (lastLineDisplayed() && (dualPageEnabled || A.getPageHeight() + y >= this.txtView.getRealHeight() || !this.txtView.lineIsSuperLongImage(getRealLineCount() - 1))) {
                                        forceToNextChapter = true;
                                    }
                                } catch (Exception e) {
                                    A.error(e);
                                    y2 = -1;
                                }
                            }
                            if (forceToNextChapter) {
                                pageDownToNextChapter(-1);
                                showPageAnimation(true);
                                return;
                            } else if (y != 0 || isPageDown) {
                                int toLine = getNextPageLine2(isPageDown, forCacheOnly);
                                if (dualPageEnabled || toLine <= 0 || !this.txtView.lineIsSuperLongImage(toLine - 1)) {
                                    y2 = this.txtView.getLineTop2(toLine);
                                    if (isPageDown && y2 <= y) {
                                        y2 = y + svH;
                                    }
                                } else {
                                    if (!isPageDown) {
                                        svH = -svH;
                                    }
                                    y2 = y + svH;
                                }
                                if (y2 < 0) {
                                    y2 = 0;
                                }
                                txtScrollTo(y2);
                                if (!checkIfChangeChapterOrSpeak(direction, y, y2) && isPageDown) {
                                    preNextChapter(true);
                                }
                            } else if (!waitingForCahingShots()) {
                                pageUpToPriorChapter();
                                showPageAnimation(false);
                                return;
                            } else {
                                return;
                            }
                        }
                        if (!forCacheOnly) {
                            if (A.isSpeaking && getBookType() == 0) {
                                speakCurrentPage(y);
                            }
                            get_flip_handler().sendEmptyMessage(direction);
                        }
                    } else if (!this.pdf.textReflow) {
                        showPageAnimation(isPageDown);
                    }
                }
            } catch (Exception e2) {
                A.error(e2);
            }
        }
    }

    private int getNextPageLine2(boolean isPageDown, boolean forCacheOnly) {
        int line = 0;
        MyLayout lo = this.txtView.getLayout();
        boolean dualPage = dualPageEnabled();
        if (isPageDown) {
            if (!(forCacheOnly || isEndOfBook())) {
                this.yBeforePageDown.add(Integer.valueOf(this.txtScroll.getScrollY()));
            }
            if (A.flip_animation != 0 || this.isPaused || ((dualPage || this.txtView.lastIgnoreLine() <= 0) && (!dualPage || this.txtView2.lastIgnoreLine() <= 0))) {
                int lastLine = A.getLastDisplayLine(dualPage ? this.txtScroll2 : this.txtScroll, -1);
                line = lastLine + 1;
                if (!(!this.txtView.isNormalImageLine(lastLine) || this.txtView.isImageDrawed(lastLine) || (dualPage && lo.getLineForVertical(this.txtScroll2.getScrollY()) == lastLine))) {
                    line = lastLine;
                }
            } else {
                line = !dualPage ? this.txtView.lastIgnoreLine() : this.txtView2.lastIgnoreLine();
            }
        } else if (!forCacheOnly && this.yBeforePageDown.size() > 0) {
            return lo.getLineForVertical(((Integer) this.yBeforePageDown.remove(this.yBeforePageDown.size() - 1)).intValue());
        } else {
            int y = this.txtScroll.getScrollY();
            int cur_line = lo.getLineForVertical(y);
            int y2 = y - (A.getPageHeight() * txtKeepPage(0));
            if (y2 >= this.txtView.getLineHeight() / 2) {
                line = lo.getLineForVertical(y2) + 1;
            }
            int suggest_last_line = A.getLastDisplayLine(this.txtScroll, line);
            if (dualPage) {
                suggest_last_line = A.getLastDisplayLine(this.txtScroll2, suggest_last_line + 1);
            }
            if (line > 0 && suggest_last_line >= cur_line) {
                line--;
            }
        }
        if (!(!A.keepOneLineWhenPaging || A.isSpeaking || this.txtView.getPageBreakLine() == line)) {
            line--;
        }
        if (line < 0) {
            line = 0;
        }
        if (line > getRealLineCount() - 1) {
            return getRealLineCount() - 1;
        }
        return line;
    }

    public boolean checkIfChangeChapterOrSpeak(int direction, int y, int y2) {
        int y3 = this.txtScroll.getScrollY();
        int lh = this.txtView.getLineHeight() - 3;
        if (y2 != -1 && Math.abs(y3 - y) >= lh) {
            if (A.isSpeaking) {
                speakCurrentPage(y);
            }
            return false;
        } else if (direction != 1) {
            pageUpToPriorChapter();
            return true;
        } else if (y2 == -1 || A.getPageHeight() + y3 >= this.txtView.getRealHeight() - lh) {
            pageDownToNextChapter(y);
            return true;
        } else {
            this.txtScroll.pageScroll(TransportMediator.KEYCODE_MEDIA_RECORD);
            if (!checkDualPageMode()) {
                return true;
            }
            setTxtView2Visible();
            return true;
        }
    }

    private void pageDownToNextChapter(int y) {
        String progressTitle;
        if (!A.noSplitHtmls() && A.lastSplitIndex < A.splitHtmls.size() - 1) {
            saveChapterTextToPriorCache(A.lastChapter, A.lastSplitIndex, this.htmlSrc);
            A.lastSplitIndex++;
            progressTitle = (A.ebook != null ? ((Chapter) A.ebook.getChapters().get(A.lastChapter)).name : T.getFilename(A.lastFile)) + " (" + (A.lastSplitIndex + 1) + "/" + A.splitHtmls.size() + ")";
            if (!hasNextChapterText(true, progressTitle)) {
                createProgressDlg(progressTitle);
                this.forceDelayFlipAnimation = true;
                this.txtScrollHandler.sendMessageDelayed(this.txtScrollHandler.obtainMessage(4, Integer.valueOf(1)), 50);
            }
        } else if (getBookType() != 1 && A.lastChapter < A.ebook.getChapters().size() - 1) {
            saveChapterTextToPriorCache(A.lastChapter, A.lastSplitIndex, this.htmlSrc);
            A.lastChapter++;
            A.lastPosition = 0;
            A.lastSplitIndex = 0;
            progressTitle = ((Chapter) A.ebook.getChapters().get(A.lastChapter)).name;
            if (!hasNextChapterText(true, progressTitle)) {
                if (isBigChapter(A.lastChapter)) {
                    createProgressDlg(progressTitle);
                }
                this.ebook_inLoading = true;
                this.forceDelayFlipAnimation = true;
                this.ebookPageUp = false;
                this.ebookChapterDown = true;
                this.handler.post(this);
            }
        }
    }

    private void pageUpToPriorChapter() {
        String progressTitle;
        if (!A.noSplitHtmls() && A.lastSplitIndex > 0) {
            saveChapterTextToNextCache(A.lastChapter, A.lastSplitIndex);
            A.lastSplitIndex--;
            progressTitle = (A.ebook != null ? ((Chapter) A.ebook.getChapters().get(A.lastChapter)).name : T.getFilename(A.lastFile)) + " (" + (A.lastSplitIndex + 1) + "/" + A.splitHtmls.size() + ")";
            if (!hasPriorChapterText(progressTitle)) {
                createProgressDlg(progressTitle);
                this.forceDelayFlipAnimation = true;
                this.txtScrollHandler.sendMessageDelayed(this.txtScrollHandler.obtainMessage(4, Integer.valueOf(-1)), 50);
            }
        } else if (A.lastChapter > 0) {
            saveChapterTextToNextCache(A.lastChapter, A.lastSplitIndex);
            A.lastChapter--;
            A.lastSplitIndex = -1;
            A.lastPosition = ((Chapter) A.ebook.getChapters().get(A.lastChapter)).size;
            progressTitle = ((Chapter) A.ebook.getChapters().get(A.lastChapter)).name;
            if (!hasPriorChapterText(progressTitle)) {
                if (isBigChapter(A.lastChapter)) {
                    createProgressDlg(progressTitle);
                }
                A.lastSplitIndex = 0;
                this.ebook_inLoading = true;
                this.forceDelayFlipAnimation = true;
                this.ebookPageUp = true;
                this.handler.post(this);
                forceUpdateForFitHardwareAccelerate();
            }
        }
    }

    private boolean isBigChapter(int id) {
        if (A.ebook == null || id < 0 || id >= A.ebook.getChapters().size()) {
            return false;
        }
        Chapter c = (Chapter) A.ebook.getChapters().get(id);
        int base = 8000;
        if (VERSION.SDK_INT >= 21 || Runtime.getRuntime().maxMemory() > 100000000) {
            base = 8000 * 2;
        }
        if (!(c.css == null || c.css.styles == null)) {
            if (c.css.styles.size() > 1000) {
                base /= 5;
            } else if (c.css.styles.size() > 600) {
                base /= 4;
            } else if (c.css.styles.size() > HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES) {
                base /= 3;
            } else if (c.css.styles.size() > 100) {
                base /= 2;
            }
        }
        if (c.size > ((long) base)) {
            return true;
        }
        return false;
    }

    private int getPageLineCount() {
        float rest = ((float) A.getPageHeight()) / ((float) this.txtView.getLineHeight());
        int pageLineCount = (int) rest;
        if (pageAddOneLine(rest)) {
            return pageLineCount + 1;
        }
        return pageLineCount;
    }

    private boolean pageAddOneLine(float rest) {
        return rest - ((float) ((int) rest)) > A.oneLineTag();
    }

    protected void saveFlipShot(boolean forceVisible) {
        Bitmap bm = null;
        if (!this.inPreShow) {
            if (!forceVisible) {
                if (!A.isSpeaking && !A.isInAutoScroll) {
                    if (!this.tmpHorizaontalScroll && A.flip_animation < 3) {
                        return;
                    }
                    if (this.tmpHorizaontalScroll && A.flip_animation < 3 && A.hori_fling_animation < 3) {
                        return;
                    }
                }
                return;
            }
            inverseLayoutVisible(true);
            if (this.topLay != null) {
                this.topLay.setAnimation(null);
            }
            if (this.bottomLay != null) {
                this.bottomLay.setAnimation(null);
            }
            if (forceVisible || !ignoreFirstOrLastPageFlip(this.pageDirection)) {
                boolean samePage;
                if (this.lastFlipScrollY != getFlipScrollY() || T.isRecycled(this.tmpFlipShot2)) {
                    samePage = false;
                } else {
                    samePage = true;
                }
                if (!samePage) {
                    T.recycle(this.tmpFlipShot2);
                }
                if (samePage) {
                    bm = this.tmpFlipShot2;
                }
                if (!forceVisible) {
                    this.contentLay.setAnimationState(false);
                    if (bm == null) {
                        bm = getPageShot(true, true);
                    }
                    this.flipView.setBitmap1(bm);
                    this.flipView.animateState = 0;
                } else if (bm == null) {
                    bm = getPageShot(true, true);
                }
                if (bm != null) {
                    this.flipView.setImageBitmap(bm);
                    if (this.forceTiltSpeed != 0.0f && A.isFlipNone()) {
                        try {
                            this.flipView.setBitmap1(bm.copy(bm.getConfig(), false));
                        } catch (OutOfMemoryError e) {
                            System.gc();
                        }
                    }
                    this.flipView.setVisibility(0);
                    this.flipView.invalidate();
                    return;
                }
                A.log("*************saveFlipShot failed***********");
            }
        }
    }

    private boolean doCurlFlip() {
        if (isPdf() && this.pdf.textReflow) {
            return false;
        }
        if (get3dCurlShot(true)) {
            handleCurlMessage(createCurlMsg(0, null));
            return true;
        }
        this.curlDelayedMsg = createCurlMsg(0, null);
        return true;
    }

    private void curlHandlerSendMessage(Message msg) {
        if (this.curl2dHandler == null) {
            this.curl2dHandler = new Handler() {
                public void handleMessage(Message msg) {
                    ActivityTxt.this.handleCurlMessage(msg);
                }
            };
        }
        this.curl2dHandler.sendMessage(msg);
    }

    private boolean waitingForCahingShots() {
        if (this.cachingPageShotsTime <= 0) {
            return false;
        }
        if (SystemClock.elapsedRealtime() - this.cachingPageShotsTime > 2500) {
            this.cachingPageShotsTime = 0;
        }
        A.log("waitingForCahingShots:" + A.lastChapter + "," + this.txtScroll.getScrollY());
        return true;
    }

    private boolean createCachePageShotsHandler(int delay) {
        this.cachingPageShotsTime = 0;
        if (!(T.isRecycled(this.tmpFlipShot3) || this.tmpFlipShot3 == this.tmpFlipShot2)) {
            T.recycle(this.tmpFlipShot3);
        }
        this.tmpFlipShot3 = null;
        if (!A.isFlipCurl() || this.flippingAnimationTime > 0) {
            return false;
        }
        if ((!this.curl3dCoverShowed && VERSION.SDK_INT < 11) || getBookType() == 7) {
            return false;
        }
        if (getBookType() == 0 && dualPageEnabled()) {
            return false;
        }
        this.handler.removeMessages(DELAY_CACHE_SHOT1);
        if (delay == 0) {
            return createCachePageShots();
        }
        this.handler.sendEmptyMessageDelayed(DELAY_CACHE_SHOT1, (long) delay);
        return false;
    }

    private boolean createCachePageShots() {
        try {
            if (this.getting3dCurlShot || isEndOfBook() || A.isSpeaking || A.isInAutoScroll) {
                return false;
            }
            if ((getBookType() == 100 && lastLineDisplayed()) || this.flippingAnimationTime > 0) {
                return false;
            }
            boolean samePage;
            boolean z;
            A.logTime = SystemClock.elapsedRealtime();
            this.cachingPageShotsTime = SystemClock.elapsedRealtime();
            get3dAboutColor();
            if (this.lastFlipScrollY != getFlipScrollY() || T.isRecycled(this.tmpFlipShot2)) {
                samePage = false;
            } else {
                samePage = true;
            }
            T.recycle(this.tmpFlipShot1);
            this.tmpFlipShot3 = null;
            this.tmpFlipShot1 = null;
            if (!samePage) {
                T.recycle(this.tmpFlipShot2);
                this.tmpFlipShot2 = getPageShot(false, HighQuality3D());
                this.lastFlipScrollY = getFlipScrollY();
            }
            if (this.curl3dLay.getVisibility() != 0) {
                this.flipView.bm1 = this.tmpFlipShot2;
                this.flipView.for3dCurlCache = true;
                this.flipView.setVisibility(0);
            }
            int v1 = this.txtView2.getVisibility();
            this.cacheScrollY = this.txtScroll.getScrollY();
            this.txtReload = false;
            if (getBookType() == 0) {
                this.cacheTxtText = this.txtView.getLineText(this.txtView.getLayout().getLineForVertical(this.txtScroll.getScrollY()));
            }
            pageScroll(1, false, true);
            if (v1 != this.txtView2.getVisibility()) {
                z = true;
            } else {
                z = false;
            }
            this.delayCache = z;
            if (this.delayCache) {
                this.handler.sendEmptyMessage(DELAY_CACHE_SHOT2);
            } else {
                createCachePageShots2();
            }
            return true;
        } catch (Exception e) {
            A.error(e);
            return false;
        }
    }

    private void createCachePageShots2() {
        this.tmpFlipShot3 = getPageShot(false, HighQuality3D());
        if (this.txtReload) {
            pageScroll(-1, false, true);
            int line = this.txtView.getLayout().getLineForVertical(this.txtScroll.getScrollY());
            if (!this.txtView.getLineText(line).equals(this.cacheTxtText)) {
                int newY = -1;
                if (line > 0 && this.txtView.getLineText(line - 1).equals(this.cacheTxtText)) {
                    newY = this.txtView.getLineTop2(line - 1);
                } else if (line < this.txtView.getLineCount() - 1 && this.txtView.getLineText(line + 1).equals(this.cacheTxtText)) {
                    newY = this.txtView.getLineTop2(line + 1);
                }
                if (newY > 0) {
                    txtScrollTo(newY);
                }
            }
        } else {
            txtScrollTo(this.cacheScrollY);
        }
        statusHandlerSendMessage(0, 0);
        if (this.delayCache) {
            this.handler.sendEmptyMessage(DELAY_HIDE_CURL_FLIPVIEW);
        } else {
            endCachingShot();
        }
    }

    private void endCachingShot() {
        if (this.curl3dLay.getVisibility() != 0) {
            this.flipView.setVisibility(8);
            this.flipView.for3dCurlCache = false;
        } else {
            setCurl3dVisibility(false);
        }
        this.cachingPageShotsTime = 0;
        System.gc();
    }

    private void handleCurlMessage(Message msg) {
        int i = -1;
        boolean z = true;
        if (this.curl3d != null) {
            if (this.curl3d.mAnimate || waitingForCahingShots()) {
                this.pagingByMove = true;
                return;
            }
            MotionEvent e2;
            switch (msg.what) {
                case 0:
                    this.curl3d.updateBitmaps();
                    setCurl3dVisibility(true);
                    NewCurl3D newCurl3D = this.curl3d;
                    if (this.pageDirection != 1) {
                        z = false;
                    }
                    newCurl3D.pageScroll(z);
                    this.flippingAnimationTime = SystemClock.elapsedRealtime();
                    return;
                case 1:
                    this.contentLay.setAnimationState(false);
                    this.contentLay.invalidate();
                    this.curl3d.clearAllShots();
                    setCurl3dVisibility(false);
                    setFlipViewINVISIBLE(true);
                    T.recycle(this.tmpFlipShot1);
                    this.flippingAnimationTime = 0;
                    if (!createCachePageShotsHandler(0)) {
                        setCurl3dVisibility(false);
                        System.gc();
                        return;
                    }
                    return;
                case 2:
                    this.curlDownMsgSending = false;
                    return;
                case 3:
                    if (!this.pagingByMove && !this.forceDisableCurlEvent) {
                        if (!this.sentCurlTouchEvent && !enoughTurnMove((float) msg.arg1, this.pressDownX, true)) {
                            return;
                        }
                        if (!this.gotCurlImage) {
                            this.pageDirection = ((float) msg.arg1) > this.pressDownX ? -1 : 1;
                            if (!ignoreFirstOrLastPageFlip(this.pageDirection)) {
                                this.gotCurlImage = true;
                                if (!this.sentCurlTouchEvent && isOppositeTouch((float) msg.arg1, this.pressDownX)) {
                                    this.pagingByMove = true;
                                    pageScroll(this.pageDirection);
                                    return;
                                } else if (get3dCurlShot(true)) {
                                    handleCurlMessage(createCurlMsg(3, this.hMotionEvent));
                                    return;
                                } else {
                                    this.curlDelayedMsg = createCurlMsg(3, this.hMotionEvent);
                                    return;
                                }
                            }
                            return;
                        } else if (!this.forceDelayFlipAnimation && this.curlDelayedMsg == null) {
                            e2 = MotionEvent.obtain(this.hMotionEvent);
                            e2.setLocation(getTouchX(e2), e2.getY());
                            if (this.sentCurlTouchEvent) {
                                this.curl3d.simulateOnTouch(e2, false);
                                return;
                            }
                            this.sentCurlTouchEvent = true;
                            e2.setAction(0);
                            this.curl3d.updateBitmaps();
                            setCurl3dVisibility(true);
                            this.curl3d.simulateOnTouch(e2, false);
                            this.flippingAnimationTime = SystemClock.elapsedRealtime();
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                case 4:
                    if (!this.pagingByMove && !this.forceDisableCurlEvent) {
                        if (!this.gotCurlImage) {
                            this.gotCurlImage = true;
                            if (Math.abs(this.mCurlFlingDirection) > 600.0f) {
                                if (this.mCurlFlingDirection <= 0.0f) {
                                    i = 1;
                                }
                                this.pageDirection = i;
                            } else if (enoughTurnMove((float) msg.arg1, this.pressDownX, true)) {
                                if (((float) msg.arg1) <= this.pressDownX) {
                                    i = 1;
                                }
                                this.pageDirection = i;
                            } else {
                                this.pageDirection = getCurlClickDirection(this.hMotionEvent);
                            }
                            if (this.pageDirection != 0 && !ignoreFirstOrLastPageFlip(this.pageDirection) && !ignorePdfFlingAction()) {
                                if (get3dCurlShot(true)) {
                                    handleCurlMessage(createCurlMsg(0, null));
                                } else {
                                    this.curlDelayedMsg = createCurlMsg(0, null);
                                }
                            } else {
                                return;
                            }
                        } else if (this.curlDelayedMsg != null) {
                            this.handler.sendEmptyMessageDelayed(DELAY_CURL3D_TOUCH_UP, 10);
                            return;
                        } else if (this.curl3dLay.getVisibility() == 0) {
                            e2 = MotionEvent.obtain(this.hMotionEvent);
                            e2.setLocation(getTouchX(e2), e2.getY());
                            if ((this.mCurlFlingDirection <= 200.0f || this.pageDirection != 1) && (this.mCurlFlingDirection >= -200.0f || this.pageDirection != -1)) {
                                this.curl3d.simulateOnTouch(e2, false);
                            } else {
                                this.curl3d.simulateOnTouch(e2, true);
                            }
                        } else {
                            this.flippingAnimationTime = 0;
                        }
                        this.gotCurlImage = false;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private boolean enoughTurnMove(float nowX, float downX, boolean checkOpposite) {
        if (Math.abs(nowX - downX) > ((float) (!checkOpposite ? false : isOppositeTouch(nowX, downX) ? A.d(20.0f) : A.d(8.0f)))) {
            return true;
        }
        return false;
    }

    private boolean isOppositeTouch(float nowX, float downX) {
        return nowX > ((float) (this.baseFrame.getWidth() / 2)) ? nowX > downX : nowX < downX;
    }

    private boolean ignoreFirstOrLastPageFlip(int direction) {
        if (direction == 1) {
            return isEndOfBook();
        }
        return isBeginOfBook();
    }

    private boolean isBeginOfBook() {
        if (isPdf()) {
            return pdfGetCurrPageNo() == 0;
        } else {
            if (this.txtScroll.getScrollY() == 0) {
                if (getBookType() == 0) {
                    return true;
                }
                if (A.lastChapter == 0 && A.lastSplitIndex == 0) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean ignorePdfFlingAction() {
        return ignorePdfCurl(this.hMotionEvent) && Math.abs(getTouchX(this.hMotionEvent) - this.pressDownX) > ((float) A.d(8.0f));
    }

    private boolean doOnCurlTouch(View v, MotionEvent event) {
        if (this.inPreShow) {
            return false;
        }
        if (this.layoutVisible) {
            return false;
        }
        if (A.isSpeaking) {
            return false;
        }
        if (!A.isFlipCurl()) {
            return false;
        }
        if (isPdf() && this.pdf.textReflow) {
            return false;
        }
        if (event.getAction() == 0) {
            this.isCurlClick = false;
            if (flippingAnimation() || waitingForCahingShots()) {
                return true;
            }
            this.gotCurlImage = false;
            this.pagingByMove = false;
            this.sentCurlTouchEvent = false;
            hideScrollbarProgress();
            this.forceDisableCurlEvent = false;
            this.isCurlClick = true;
            if (isMiddleTap(event)) {
                this.isCurlClick = false;
            }
            if (!this.isCurlClick) {
                return false;
            }
            this.mCurlFlingDirection = 0.0f;
            hideDotViews();
            this.curlDownMsgSending = true;
            handleCurlMessage(createCurlMsg(2, event));
        }
        if (A.moveStart) {
            return false;
        }
        int i;
        if (this.pagingByMove && event.getAction() == 2) {
            if (this.continueMoveX == 0.0f) {
                this.continueMoveX = event.getX();
            }
            if (A.isFlipCurl() && this.curl3d != null && this.curl3d.inDualPageCachingShot()) {
                A.log("***** in caching shot, cancel(2) ******");
                return true;
            } else if (Math.abs(event.getX() - this.continueMoveX) <= ((float) A.d(50.0f))) {
                return true;
            } else {
                if (event.getX() > this.continueMoveX) {
                    i = -1;
                } else {
                    i = 1;
                }
                this.pageDirection = i;
                this.continueMoveX = event.getX();
                pageScroll(this.pageDirection);
                return true;
            }
        }
        Message m;
        if (event.getAction() == 2) {
            if (!this.gotCurlImage && ((A.adjustBrightness && do_AdjustBrightness_Event(event)) || (A.adjustFontSizeAtSlide && do_AdjustFontSize_Event(event)))) {
                this.isCurlClick = false;
                setCurlViewINVISIBLE();
                this.forceDisableCurlEvent = true;
                return true;
            } else if (!this.gotCurlImage && (dotVisible() || isTouchInEdge(event))) {
                return true;
            } else {
                if (ignorePdfCurl(event)) {
                    return false;
                }
                if (this.isCurlClick) {
                    m = createCurlMsg(3, event);
                    if (!A.isFlipCurl() || this.curlDownMsgSending) {
                        curlHandlerSendMessage(m);
                    } else {
                        handleCurlMessage(m);
                    }
                } else if ((isMiddleTap(event) || this.pagingByMove) && Math.abs(getTouchX(event) - this.pressDownX) > Math.abs(event.getY() - this.pressDownY) && enoughTurnMove(getTouchX(event), this.pressDownX, true)) {
                    this.pagingByMove = true;
                    if (getTouchX(event) > this.pressDownX) {
                        i = -1;
                    } else {
                        i = 1;
                    }
                    this.pageDirection = i;
                    pageScroll(this.pageDirection);
                    return true;
                } else if (isMiddleTap(event)) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        if (!ignorePdfCurl(event)) {
            if (this.mCurlDetector == null) {
                this.mCurlDetector = new GestureDetector(new MyCurlGesture());
            }
            this.mCurlDetector.onTouchEvent(event);
        }
        if (event.getAction() != 1 || this.forceDisableCurlEvent) {
            return true;
        }
        if (!do_Gesture_Event(event) || !this.swipeGuesture) {
            if ((this.gotCurlImage || this.mTouchTimes <= 10) && (this.gotCurlImage || this.pressTime == -1 || SystemClock.elapsedRealtime() - this.pressTime < 800)) {
                boolean isUrlClick = false;
                if (!this.gotCurlImage) {
                    isUrlClick = isUrlClick(event, true);
                    float xMove = Math.abs(getTouchX(event) - this.pressDownX);
                    float yMove = Math.abs(event.getY() - this.pressDownY);
                    if (dotVisible() || clickForBookmark(event)) {
                        return true;
                    }
                    if (isMiddleTap(event) && yMove < 12.0f && xMove < 12.0f) {
                        if (isUrlClick) {
                            return true;
                        }
                        inverseLayoutVisible(false);
                        return true;
                    }
                }
                if (this.gotCurlImage || (this.isCurlClick && !isUrlClick)) {
                    m = createCurlMsg(4, event);
                    if (!A.isFlipCurl() || this.curlDownMsgSending) {
                        curlHandlerSendMessage(m);
                        return true;
                    }
                    handleCurlMessage(m);
                    return true;
                }
            }
            if (isMiddleTap(event)) {
                return false;
            }
            return true;
        } else if (!this.gotCurlImage) {
            return true;
        } else {
            this.onFlipCurlingEnd.Finished(false);
            return true;
        }
    }

    private boolean ignorePdfCurl(MotionEvent event) {
        return isPdf() && (!A.pdf_scoll_lock || event.getPointerCount() > 1);
    }

    public float getTouchX(MotionEvent event) {
        return event.getX() + ((float) additionalLeft());
    }

    private Message createCurlMsg(int what, MotionEvent event) {
        Message m = new Message();
        m.what = what;
        if (event != null) {
            m.arg1 = (int) getTouchX(event);
            m.arg2 = (int) event.getY();
        }
        return m;
    }

    private int getCurlClickDirection(MotionEvent event) {
        int action;
        switch (A.clickArea(event)) {
            case 1:
                action = A.doTapScreenTop;
                break;
            case 2:
                action = A.doTapScreenBottom;
                break;
            case 3:
                action = A.doTapScreenLeft;
                break;
            case 4:
                action = A.doTapScreenRight;
                break;
            default:
                return 1;
        }
        if (action == 15) {
            return 0;
        }
        return action == 0 ? -1 : 1;
    }

    private Handler get_flip_handler() {
        if (this.flip_handler == null) {
            this.flip_handler = new Handler() {
                public void handleMessage(Message msg) {
                    int i = 0;
                    try {
                        ActivityTxt.this.showPageAnimation(msg.what == 1);
                        if (!ActivityTxt.this.forceDelayFlipAnimation || A.hori_fling_animation < 3) {
                            ActivityTxt.this.tmpHorizaontalScroll = false;
                        }
                        ActivityTxt.this.showSeekBarProgress();
                        ActivityTxt activityTxt = ActivityTxt.this;
                        if (!A.isFlipCurl()) {
                            i = msg.what == 1 ? 1 : -1;
                        }
                        activityTxt.showReadProgress(i);
                        if (A.isFlipNone() && !ActivityTxt.this.showStatusbar()) {
                            ActivityTxt.this.handler.sendEmptyMessageDelayed(401, 50);
                        }
                    } catch (Exception e) {
                        A.error(e);
                    }
                }
            };
        }
        return this.flip_handler;
    }

    private boolean flippingAnimation() {
        if (this.flippingAnimationTime <= 0) {
            return false;
        }
        if (SystemClock.elapsedRealtime() - this.flippingAnimationTime > 2000) {
            this.flippingAnimationTime = 0;
        }
        return true;
    }

    protected boolean showPageAnimation(boolean isPageDown) {
        if (this.keyDownCount > 1 || flippingAnimation()) {
            return false;
        }
        if (this.forceDelayFlipAnimation && this.flipView.getVisibility() == 0) {
            this.handler.removeMessages(DELAY_FLIP_CLICK);
            this.handler.sendEmptyMessageDelayed(DELAY_FLIP_CLICK, 10);
            return false;
        } else if (this.inPreShow || this.flipView.bm1 == null) {
            return false;
        } else {
            if (this.forceTiltSpeed == 0.0f) {
                boolean go = true;
                if (this.tmpHorizaontalScroll) {
                    if (!(this.flipView.getVisibility() == 0 || this.txtLayS.getVisibility() == 0) || (A.flip_animation < 3 && A.hori_fling_animation < 3)) {
                        go = false;
                    }
                } else if (A.isInAutoScroll || A.isSpeaking || A.flip_animation < 3) {
                    go = false;
                }
                if (!go) {
                    setFlipViewINVISIBLE(true);
                    return false;
                }
            }
            int hori_fling_animation = A.hori_fling_animation;
            if (isPdf() && A.pdf_scoll_lock && Global.def_view == 3 && hori_fling_animation == 4) {
                hori_fling_animation = 3;
            }
            int flip_type = (this.tmpHorizaontalScroll && A.isFlipNone()) ? hori_fling_animation : A.flip_animation;
            if (flip_type == 0 && this.forceTiltSpeed != 0.0f) {
                flip_type = 4;
            }
            if (flip_type < 3) {
                setFlipViewINVISIBLE(true);
                return false;
            }
            this.tmpHorizaontalScroll = false;
            this.flippingAnimationTime = SystemClock.elapsedRealtime();
            this.contentLay.setAnimationState(true);
            setTiltPaused(true);
            this.flipView.setTag(Integer.valueOf(this.txtScroll.getScrollY()));
            this.lastFlipScrollY = getFlipScrollY();
            showReadProgress(0);
            this.tmpFlipShot2 = getPageShot(true, true);
            this.flipView.setBitmap2(this.tmpFlipShot2);
            this.flipView.startAnimation(isPageDown, flip_type, new AfterFlipCurl() {
                public void Finished(boolean success) {
                    ActivityTxt.this.scrollBlock.setVisibility(8);
                    ActivityTxt.this.setTiltPaused(false);
                    ActivityTxt.this.forceTiltSpeed = 0.0f;
                    ActivityTxt.this.flippingAnimationTime = 0;
                    if (!ActivityTxt.this.updateForFitHardwareAccelerate()) {
                        ActivityTxt.this.contentLay.postInvalidate();
                    }
                    System.gc();
                }
            });
            return true;
        }
    }

    protected void setTiltPaused(boolean paused) {
        if (this.shakeSensorLisener != null) {
            this.shakeSensorLisener.tilt_paused = paused;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void showBookByPercent(int r25) {
        /*
        r24 = this;
        r5 = r24.getBookType();	 Catch:{ Exception -> 0x0023 }
        switch(r5) {
            case 0: goto L_0x000d;
            case 1: goto L_0x0028;
            case 100: goto L_0x00b3;
            default: goto L_0x0007;
        };
    L_0x0007:
        r5 = 0;
        r0 = r24;
        r0.ebook_inLoading = r5;
    L_0x000c:
        return;
    L_0x000d:
        r8 = com.flyersoft.books.A.txtLength();	 Catch:{ Exception -> 0x0023 }
        r0 = r25;
        r0 = (long) r0;	 Catch:{ Exception -> 0x0023 }
        r22 = r0;
        r8 = r8 * r22;
        r22 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r8 = r8 / r22;
        r5 = 0;
        r0 = r24;
        r0.showTxtByPosition(r8, r5);	 Catch:{ Exception -> 0x0023 }
        goto L_0x0007;
    L_0x0023:
        r4 = move-exception;
        com.flyersoft.books.A.error(r4);
        goto L_0x0007;
    L_0x0028:
        r5 = com.flyersoft.books.A.noSplitHtmls();	 Catch:{ Exception -> 0x0023 }
        if (r5 == 0) goto L_0x0040;
    L_0x002e:
        r0 = r24;
        r5 = r0.txtView;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.getRealHeight();	 Catch:{ Exception -> 0x0023 }
        r5 = r5 * r25;
        r5 = r5 / 1000;
        r0 = r24;
        r0.txtScrollTo(r5);	 Catch:{ Exception -> 0x0023 }
        goto L_0x0007;
    L_0x0040:
        r0 = r24;
        r5 = r0.htmlText;	 Catch:{ Exception -> 0x0023 }
        r12 = r5.length();	 Catch:{ Exception -> 0x0023 }
        r8 = (long) r12;	 Catch:{ Exception -> 0x0023 }
        r0 = r25;
        r0 = (long) r0;	 Catch:{ Exception -> 0x0023 }
        r22 = r0;
        r8 = r8 * r22;
        r22 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r8 = r8 / r22;
        r14 = (int) r8;	 Catch:{ Exception -> 0x0023 }
        r5 = com.flyersoft.books.A.maxHtmlChapterSize;	 Catch:{ Exception -> 0x0023 }
        r11 = r14 / r5;
        r5 = com.flyersoft.books.A.splitHtmls;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0023 }
        if (r11 >= r5) goto L_0x0007;
    L_0x0061:
        r5 = com.flyersoft.books.A.splitHtmls;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0023 }
        if (r5 <= 0) goto L_0x0007;
    L_0x0069:
        if (r11 < 0) goto L_0x0007;
    L_0x006b:
        com.flyersoft.books.A.lastSplitIndex = r11;	 Catch:{ Exception -> 0x0023 }
        r5 = com.flyersoft.books.A.splitHtmls;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.get(r11);	 Catch:{ Exception -> 0x0023 }
        r5 = (java.lang.String) r5;	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r0.htmlSrc = r5;	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r5 = r0.htmlSrc;	 Catch:{ Exception -> 0x0023 }
        r5 = com.flyersoft.books.A.chineseJianFanConvert(r5);	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r0.htmlSrc = r5;	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r5 = r0.htmlSrc;	 Catch:{ Exception -> 0x0023 }
        r6 = r24.createHtmlBookImageGetter();	 Catch:{ Exception -> 0x0023 }
        r8 = -1;
        r5 = com.flyersoft.staticlayout.MyHtml.fromHtml(r5, r6, r8);	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r0.txtViewSetText(r5);	 Catch:{ Exception -> 0x0023 }
        r5 = com.flyersoft.books.A.maxHtmlChapterSize;	 Catch:{ Exception -> 0x0023 }
        r17 = r14 % r5;
        r0 = r24;
        r5 = r0.txtScrollHandler;	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r6 = r0.txtScrollHandler;	 Catch:{ Exception -> 0x0023 }
        r8 = 2;
        r9 = java.lang.Integer.valueOf(r17);	 Catch:{ Exception -> 0x0023 }
        r6 = r6.obtainMessage(r8, r9);	 Catch:{ Exception -> 0x0023 }
        r8 = 100;
        r5.sendMessageDelayed(r6, r8);	 Catch:{ Exception -> 0x0023 }
        goto L_0x0007;
    L_0x00b3:
        r8 = r24.getEBookTotalSize();	 Catch:{ Exception -> 0x0023 }
        r5 = r25 + -1;
        r0 = (long) r5;	 Catch:{ Exception -> 0x0023 }
        r22 = r0;
        r8 = r8 * r22;
        r22 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r14 = r8 / r22;
        r12 = 0;
        r11 = 0;
    L_0x00c5:
        r5 = com.flyersoft.books.A.ebook;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.getChapters();	 Catch:{ Exception -> 0x0023 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0023 }
        if (r11 >= r5) goto L_0x0007;
    L_0x00d1:
        r5 = com.flyersoft.books.A.ebook;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.getChapters();	 Catch:{ Exception -> 0x0023 }
        r5 = r5.get(r11);	 Catch:{ Exception -> 0x0023 }
        r5 = (com.flyersoft.books.BaseEBook.Chapter) r5;	 Catch:{ Exception -> 0x0023 }
        r0 = r5.pure_text_length;	 Catch:{ Exception -> 0x0023 }
        r16 = r0;
        if (r16 <= 0) goto L_0x0168;
    L_0x00e3:
        r0 = r16;
        r0 = (long) r0;	 Catch:{ Exception -> 0x0023 }
        r18 = r0;
    L_0x00e8:
        r8 = 0;
        r5 = (r18 > r8 ? 1 : (r18 == r8 ? 0 : -1));
        if (r5 > 0) goto L_0x00f0;
    L_0x00ee:
        r18 = 1;
    L_0x00f0:
        r12 = r12 + r18;
        r5 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));
        if (r5 > 0) goto L_0x0104;
    L_0x00f6:
        r5 = com.flyersoft.books.A.ebook;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.getChapters();	 Catch:{ Exception -> 0x0023 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0023 }
        r5 = r5 + -1;
        if (r11 != r5) goto L_0x0215;
    L_0x0104:
        r8 = r12 - r14;
        r20 = r18 - r8;
        r5 = com.flyersoft.books.A.ebook;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.isHtml();	 Catch:{ Exception -> 0x0023 }
        if (r5 == 0) goto L_0x0125;
    L_0x0110:
        if (r16 >= 0) goto L_0x0125;
    L_0x0112:
        r5 = com.flyersoft.books.A.ebook;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.getChapterText(r11);	 Catch:{ Exception -> 0x0023 }
        r5 = com.flyersoft.books.T.html2Text(r5);	 Catch:{ Exception -> 0x0023 }
        r5 = r5.length();	 Catch:{ Exception -> 0x0023 }
        r8 = (long) r5;	 Catch:{ Exception -> 0x0023 }
        r8 = r8 * r20;
        r20 = r8 / r18;
    L_0x0125:
        r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = r25;
        if (r0 != r5) goto L_0x017a;
    L_0x012b:
        com.flyersoft.books.A.lastPosition = r12;	 Catch:{ Exception -> 0x0023 }
    L_0x012d:
        r5 = com.flyersoft.books.A.lastChapter;	 Catch:{ Exception -> 0x0023 }
        if (r5 == r11) goto L_0x0189;
    L_0x0131:
        r5 = 1;
        r0 = r24;
        r0.ebook_inLoading = r5;	 Catch:{ Exception -> 0x0023 }
        com.flyersoft.books.A.lastChapter = r11;	 Catch:{ Exception -> 0x0023 }
        r5 = com.flyersoft.books.A.maxHtmlChapterSize;	 Catch:{ Exception -> 0x0023 }
        r8 = (long) r5;	 Catch:{ Exception -> 0x0023 }
        r5 = (r20 > r8 ? 1 : (r20 == r8 ? 0 : -1));
        if (r5 >= 0) goto L_0x0182;
    L_0x013f:
        r5 = 0;
    L_0x0140:
        com.flyersoft.books.A.lastSplitIndex = r5;	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r5 = r0.isBigChapter(r11);	 Catch:{ Exception -> 0x0023 }
        if (r5 == 0) goto L_0x015d;
    L_0x014a:
        r5 = com.flyersoft.books.A.ebook;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.getChapters();	 Catch:{ Exception -> 0x0023 }
        r5 = r5.get(r11);	 Catch:{ Exception -> 0x0023 }
        r5 = (com.flyersoft.books.BaseEBook.Chapter) r5;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.name;	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r0.createProgressDlg(r5);	 Catch:{ Exception -> 0x0023 }
    L_0x015d:
        r0 = r24;
        r5 = r0.handler;	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r5.post(r0);	 Catch:{ Exception -> 0x0023 }
        goto L_0x000c;
    L_0x0168:
        r5 = com.flyersoft.books.A.ebook;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.getChapters();	 Catch:{ Exception -> 0x0023 }
        r5 = r5.get(r11);	 Catch:{ Exception -> 0x0023 }
        r5 = (com.flyersoft.books.BaseEBook.Chapter) r5;	 Catch:{ Exception -> 0x0023 }
        r0 = r5.size;	 Catch:{ Exception -> 0x0023 }
        r18 = r0;
        goto L_0x00e8;
    L_0x017a:
        r5 = com.flyersoft.books.A.maxHtmlChapterSize;	 Catch:{ Exception -> 0x0023 }
        r8 = (long) r5;	 Catch:{ Exception -> 0x0023 }
        r8 = r20 % r8;
        com.flyersoft.books.A.lastPosition = r8;	 Catch:{ Exception -> 0x0023 }
        goto L_0x012d;
    L_0x0182:
        r5 = com.flyersoft.books.A.maxHtmlChapterSize;	 Catch:{ Exception -> 0x0023 }
        r8 = (long) r5;	 Catch:{ Exception -> 0x0023 }
        r8 = r20 / r8;
        r5 = (int) r8;	 Catch:{ Exception -> 0x0023 }
        goto L_0x0140;
    L_0x0189:
        r0 = r24;
        r1 = r20;
        r7 = r0.getSplitIndex(r1);	 Catch:{ Exception -> 0x0023 }
        r5 = com.flyersoft.books.A.noSplitHtmls();	 Catch:{ Exception -> 0x0023 }
        if (r5 == 0) goto L_0x019b;
    L_0x0197:
        r5 = com.flyersoft.books.A.lastSplitIndex;	 Catch:{ Exception -> 0x0023 }
        if (r7 == r5) goto L_0x0205;
    L_0x019b:
        r10 = 1;
    L_0x019c:
        if (r10 == 0) goto L_0x020a;
    L_0x019e:
        r5 = 1;
        r0 = r24;
        r0.ebook_inLoading = r5;	 Catch:{ Exception -> 0x0023 }
        com.flyersoft.books.A.lastSplitIndex = r7;	 Catch:{ Exception -> 0x0023 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0023 }
        r6.<init>();	 Catch:{ Exception -> 0x0023 }
        r5 = com.flyersoft.books.A.ebook;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.getChapters();	 Catch:{ Exception -> 0x0023 }
        r5 = r5.get(r11);	 Catch:{ Exception -> 0x0023 }
        r5 = (com.flyersoft.books.BaseEBook.Chapter) r5;	 Catch:{ Exception -> 0x0023 }
        r5 = r5.name;	 Catch:{ Exception -> 0x0023 }
        r5 = r6.append(r5);	 Catch:{ Exception -> 0x0023 }
        r6 = " (";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0023 }
        r6 = r7 + 1;
        r6 = r5.append(r6);	 Catch:{ Exception -> 0x0023 }
        r5 = com.flyersoft.books.A.noSplitHtmls();	 Catch:{ Exception -> 0x0023 }
        if (r5 != 0) goto L_0x0207;
    L_0x01ce:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0023 }
        r5.<init>();	 Catch:{ Exception -> 0x0023 }
        r8 = "/";
        r5 = r5.append(r8);	 Catch:{ Exception -> 0x0023 }
        r8 = com.flyersoft.books.A.splitHtmls;	 Catch:{ Exception -> 0x0023 }
        r8 = r8.size();	 Catch:{ Exception -> 0x0023 }
        r5 = r5.append(r8);	 Catch:{ Exception -> 0x0023 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0023 }
    L_0x01e7:
        r5 = r6.append(r5);	 Catch:{ Exception -> 0x0023 }
        r6 = ")";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0023 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r0.createProgressDlg(r5);	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r5 = r0.handler;	 Catch:{ Exception -> 0x0023 }
        r0 = r24;
        r5.post(r0);	 Catch:{ Exception -> 0x0023 }
        goto L_0x000c;
    L_0x0205:
        r10 = 0;
        goto L_0x019c;
    L_0x0207:
        r5 = "";
        goto L_0x01e7;
    L_0x020a:
        r6 = com.flyersoft.books.A.lastChapter;	 Catch:{ Exception -> 0x0023 }
        r8 = com.flyersoft.books.A.lastPosition;	 Catch:{ Exception -> 0x0023 }
        r5 = r24;
        r5.showEBookByPosition(r6, r7, r8, r10);	 Catch:{ Exception -> 0x0023 }
        goto L_0x000c;
    L_0x0215:
        r11 = r11 + 1;
        goto L_0x00c5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.moonreader.ActivityTxt.showBookByPercent(int):void");
    }

    private int getSplitIndex(long tp) {
        int count = 0;
        for (int i = 0; i < A.splitHtmls.size(); i++) {
            count += ((String) A.splitHtmls.get(i)).length();
            if (((long) count) >= tp) {
                return i;
            }
        }
        return 0;
    }

    private MyImageGetter createHtmlBookImageGetter() {
        this.imageGetter = new MyImageGetter() {
            public Drawable getDrawable(String source, boolean original) {
                String imageFile = T.getFilePath(A.lastFile) + "/" + source;
                if (T.isFile(imageFile)) {
                    try {
                        Drawable d = A.getFileDrawable(new File(imageFile), 0, 1);
                        int w1 = d.getIntrinsicWidth();
                        int w2 = ActivityTxt.this.txtView.getWidth();
                        int h1 = d.getIntrinsicHeight();
                        if (w1 > w2 / 2) {
                            int i;
                            if (w2 > w1) {
                                i = (w2 - w1) / 2;
                            } else {
                                i = 0;
                            }
                            int i2 = w2 > w1 ? ((w2 - w1) / 2) + w1 : w2;
                            if (w1 > w2) {
                                h1 = (h1 * w2) / w1;
                            }
                            d.setBounds(i, 0, i2, h1);
                        } else {
                            d.setBounds(0, 0, w1, h1);
                        }
                        return A.getDisplayDrawable(d);
                    } catch (OutOfMemoryError e) {
                        A.error(e);
                        System.gc();
                    } catch (Exception e2) {
                        A.error(e2);
                    }
                }
                return null;
            }

            public Rect getDrawableBounds(String source, boolean original) {
                Drawable d = getDrawable(source, false);
                return d != null ? d.getBounds() : null;
            }
        };
        return this.imageGetter;
    }

    protected void showEBookByPosition(int lastChapter, int lastSplitIndex, long lastPosition, boolean reLoad) {
        try {
            if (A.ebook.getChapters().size() != 0) {
                this.ebook_inLoading = true;
                pausePixelAutoScroll();
                if (this.ebookChapterDown) {
                    this.ebookChapterDown = false;
                    this.ebookPageUp = false;
                    lastPosition = 0;
                }
                if (lastChapter < 0) {
                    lastChapter = 0;
                }
                if (lastChapter > A.ebook.getChapters().size() - 1) {
                    lastChapter = A.ebook.getChapters().size() - 1;
                }
                A.lastChapter = lastChapter;
                A.lastSplitIndex = lastSplitIndex;
                A.lastPosition = lastPosition;
                Chapter chapter = (Chapter) A.ebook.getChapters().get(A.lastChapter);
                int len = A.ebook.getChapterText(A.lastChapter).length();
                if (A.lastPosition > ((long) len) && len > A.maxHtmlChapterSize) {
                    A.lastSplitIndex = len / A.maxHtmlChapterSize;
                }
                if (reLoad) {
                    if (A.ebook.isHtml() && A.useWebView) {
                        generateChapterText(0);
                        showInWebView(this.htmlSrc);
                        this.changeChapterTime = SystemClock.elapsedRealtime();
                        updateProgressStatus();
                        hideProgressDlg();
                        hide_ebook_cover();
                        resetFlipCache(false, 500);
                        return;
                    } else if (hasNextChapterText(false, null)) {
                        this.changeChapterTime = SystemClock.elapsedRealtime();
                        updateProgressStatus();
                        hideProgressDlg();
                        hide_ebook_cover();
                        resetFlipCache(false, 500);
                        return;
                    } else {
                        clearTxtView();
                        generateChapterText(0);
                    }
                }
                if (this.ebookPageUp) {
                    this.ebookPageUp = false;
                    disableTxtViewDraw();
                    this.txtScrollHandler.sendEmptyMessageDelayed(1, 50);
                } else {
                    goToEBookLastPosition();
                }
                this.changeChapterTime = SystemClock.elapsedRealtime();
                updateProgressStatus();
                hideProgressDlg();
                hide_ebook_cover();
                resetFlipCache(false, 500);
            }
        } catch (Throwable e) {
            A.error(e);
        } finally {
            this.changeChapterTime = SystemClock.elapsedRealtime();
            updateProgressStatus();
            hideProgressDlg();
            hide_ebook_cover();
            resetFlipCache(false, 500);
        }
    }

    private void generateChapterText(int type) {
        ArrayList<String> htmls = new ArrayList();
        int id = type == 1 ? this.preNextChapterId : type == -1 ? this.oldPriorChapterId : A.lastChapter;
        int splitIndex = type == 1 ? this.preNextChapterSplit : type == -1 ? this.oldPriorChapterSplit : A.lastSplitIndex;
        String html = A.ebook.getChapterText(id);
        if (A.chapterEndPrompt && id == A.ebook.getChapters().size() - 1) {
            try {
                html = html + (A.ebook.isHtml() ? "<br><br><center>" + A.theEndText() + "</center><br><br>" : "\n\n" + A.theEndText() + "\n\n");
            } catch (OutOfMemoryError e) {
                A.error(e);
            }
        }
        if (A.ebook.isHtml()) {
            if (!A.useWebView) {
                html = A.adjustChapterHtml(html, htmls);
            }
            if (htmls.size() > 0) {
                if ((type == -1 && this.oldPriorChapterSplit == -1) || this.ebookPageUp || splitIndex >= htmls.size()) {
                    splitIndex = htmls.size() - 1;
                }
                html = (String) htmls.get(splitIndex);
            } else {
                splitIndex = 0;
            }
            html = A.chineseJianFanConvert(A.ebook.dealSplitHtml(id, splitIndex, htmls.size(), html));
            this.imageGetter = A.ebook.getMyImageGetter();
            if (type == 1) {
                this.preNextChapterText = MyHtml.fromHtml(html, this.imageGetter, id);
            } else if (type == -1) {
                this.oldPriorChapterText = MyHtml.fromHtml(html, this.imageGetter, id);
            } else if (!A.useWebView) {
                this.htmlSrc = html;
                txtViewSetText(MyHtml.fromHtml(html, this.imageGetter, id));
                if (this.locate_to_search_result) {
                    A.lastPosition = (long) getSearchResultLocation();
                }
            }
        } else {
            html = A.adjustChapterHtml(html, htmls);
            if (htmls.size() > 0) {
                if ((type == -1 && this.oldPriorChapterSplit == -1) || this.ebookPageUp || splitIndex >= htmls.size()) {
                    splitIndex = htmls.size() - 1;
                }
                html = (String) htmls.get(splitIndex);
            } else {
                splitIndex = 0;
            }
            html = A.chineseJianFanConvert(html);
            if (type == 1) {
                this.preNextChapterText = html;
            } else if (type == -1) {
                this.oldPriorChapterText = html;
            } else if (!A.useWebView) {
                txtViewSetText(html);
            }
        }
        if (type == 1) {
            this.preNextHtmlSrc = html;
            this.preNextChapterId = id;
            this.preNextChapterSplit = splitIndex;
            this.preNextSplitHtmls = htmls;
        } else if (type == -1) {
            this.oldPriorHtmlSrc = html;
            this.oldPriorChapterId = id;
            this.oldPriorChapterSplit = splitIndex;
            this.oldPriorSplitHtmls = htmls;
        } else {
            this.htmlSrc = html;
            A.lastChapter = id;
            A.lastSplitIndex = splitIndex;
            A.splitHtmls = htmls;
        }
        if (type == 1) {
            System.gc();
        }
    }

    private void pausePixelAutoScroll() {
        if (!A.isInAutoScroll) {
            return;
        }
        if (getScrollMode() == 2 || getScrollMode() == 3) {
            do_AutoScroll(false, false);
            A.isInAutoScroll = true;
        }
    }

    private int getScrollMode() {
        return isPdf() ? 0 : A.autoScrollMode;
    }

    private boolean isRollingScroll() {
        return getScrollMode() == 0 || getScrollMode() == 1;
    }

    private long delayTime() {
        return 50;
    }

    private void goToEBookLastPosition() {
        this.ebook_inLoading = true;
        disableTxtViewDraw();
        getAndroid22Handler().sendEmptyMessageDelayed(0, delayTime());
    }

    Handler getAndroid22Handler() {
        if (this.android22Handle == null) {
            this.android22Handle = new Handler() {
                public void handleMessage(Message msg) {
                    if (ActivityTxt.this.txtView.getLayout() == null) {
                        ActivityTxt.this.fixBrokenTextView(true);
                        ActivityTxt.this.handler.postDelayed(ActivityTxt.this, 200);
                    } else if (ActivityTxt.this.txtView.layoutState == 1) {
                        A.log("**************SoftHyphenStaticLayout tWorking, delay 100************");
                        ActivityTxt.this.getAndroid22Handler().sendEmptyMessageDelayed(0, 100);
                    } else {
                        if (ActivityTxt.this.dualPageEnabled()) {
                            if (ActivityTxt.this.txtView2.getWidth() == 0 || (ActivityTxt.this.txtView.getText().length() > 0 && ActivityTxt.this.txtView.getHeight() == 0)) {
                                A.log("********error txtView.getHeight():" + ActivityTxt.this.txtView.getHeight());
                                ActivityTxt.this.fixBrokenTextView(true);
                                ActivityTxt.this.getAndroid22Handler().sendEmptyMessageDelayed(0, 200);
                                return;
                            }
                            ActivityTxt.this.fixBottomForScrollTo(ActivityTxt.this.txtView.getRealHeight());
                        }
                        int y = ActivityTxt.this.txtView.getLineTop2(ActivityTxt.this.txtView.getLayout().getLineForOffset((int) A.lastPosition));
                        ActivityTxt.this.goToLastTime = SystemClock.elapsedRealtime();
                        ActivityTxt.this.txtScrollTo(y);
                        ActivityTxt.this.enableTxtViewDraw();
                        ActivityTxt.this.ebook_inLoading = false;
                        ActivityTxt.this.forceDelayFlipAnimation = false;
                        ActivityTxt.this.createCachePageShotsHandler(HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES);
                        ActivityTxt.this.continueSpeakOrAutoScroll();
                        ActivityTxt.this.handler.sendEmptyMessageDelayed(ActivityTxt.PRE_LOAD_NEXT_CHAPTER, 100);
                        ActivityTxt.this.forceUpdateForFitHardwareAccelerate();
                    }
                }
            };
        }
        return this.android22Handle;
    }

    private void continueSpeakOrAutoScroll() {
        if (A.isSpeaking) {
            speakCurrentPage(-100);
        }
        if (!A.isInAutoScroll) {
            return;
        }
        if (getScrollMode() != 2 && getScrollMode() != 3) {
            return;
        }
        if (A.autoScrollSpeed > 10) {
            T.showToastText(this, "3", 0);
            if (this.pixelScrollHandler == null) {
                this.pixelScrollHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        if (!A.isInAutoScroll) {
                            return;
                        }
                        if (msg.what == 3) {
                            T.hideToast();
                            ActivityTxt.this.do_AutoScroll(true, false);
                            return;
                        }
                        T.showToastText(ActivityTxt.this, "" + (3 - msg.what));
                    }
                };
            }
            this.pixelScrollHandler.removeCallbacksAndMessages(null);
            this.pixelScrollHandler.sendEmptyMessageDelayed(1, 1000);
            this.pixelScrollHandler.sendEmptyMessageDelayed(2, 2000);
            this.pixelScrollHandler.sendEmptyMessageDelayed(3, 3000);
            return;
        }
        do_AutoScroll(true, false);
    }

    protected void showTxtByPosition(long position, String veri) {
        if (getBookType() == 0) {
            int p = (int) A.getTxtDisplayPos(position, true);
            this.preShowText = null;
            try {
                if (A.getTxts().size() > 0 && A.lastBlockIndex >= 0 && A.lastBlockIndex < A.getTxts().size()) {
                    String text = A.chineseJianFanConvert(A.getBlocksText(A.lastBlockIndex));
                    if (A.fontItalic) {
                        SpannableString str = SpannableString.valueOf(text);
                        str.setSpan(new StyleSpan(2), 0, str.length(), 0);
                        txtViewSetText(str);
                    } else {
                        txtViewSetText(text);
                    }
                    if (veri != null && text.length() > veri.length() + p) {
                        String cur = text.substring(p, veri.length() + p);
                        if (!cur.equals(veri)) {
                            A.log("showTxtByPosition veri error, cur [" + cur + "], should be [" + veri + "]");
                            int i1 = text.lastIndexOf(veri, p);
                            if (i1 != -1 && p - i1 > 200) {
                                i1 = -1;
                            }
                            int i2 = text.indexOf(veri, p);
                            if (i2 - p > 200) {
                                i2 = -1;
                            }
                            if (i1 != -1 && i2 != -1) {
                                p = p - i1 < i2 - p ? i1 : i2;
                            } else if (i1 != -1) {
                                p = i1;
                            } else if (i2 != -1) {
                                p = i2;
                            }
                        }
                    }
                    if (p > text.length() - 1) {
                        p = text.length() - 1;
                    }
                    MyLayout layout = this.txtView.getLayout();
                    if (!this.isTxtScrollReady || layout == null) {
                        txtFileFinalScrollAtStartup((long) p);
                    } else {
                        txtScrollNoDelay(p);
                    }
                }
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    private void txtFileFinalScrollAtStartup(long p) {
        checkStatusBar();
        new Handler() {
            public void handleMessage(Message msg) {
                ActivityTxt.this.handler.sendMessageDelayed(ActivityTxt.this.handler.obtainMessage(ActivityTxt.SCROLL_TXT_AGAIN, msg.what, 0), ActivityTxt.this.fixBrokenTextView(false) ? 50 : ActivityTxt.this.delayTime());
            }
        }.sendEmptyMessageDelayed((int) p, fixBrokenTextView(false) ? 50 : 0);
    }

    private boolean fixBrokenTextView(boolean forceReload) {
        boolean broken = this.txtView.getText().length() > 0 && this.txtView.getLineCount() == 0;
        if (broken || forceReload) {
            A.log("************txtView broken, lineCount:" + this.txtView.getLineCount() + " tvH: " + this.txtView.getHeight() + " txtLen:" + this.txtView.getText().length());
            CharSequence text = this.txtView.getText();
            clearTxtView();
            txtViewSetText(text);
        }
        return broken;
    }

    private void initBrightSeek() {
        if (this.brightnessPanel == null) {
            this.brightnessPanel = ((ViewStub) findViewById(R.id.viewStub_brightness)).inflate();
            this.brightnessSK = (SeekBar) this.brightnessPanel.findViewById(R.id.brightnessSK);
            this.brightnessSK.setMax(150);
            this.brightAutoCheck = (CheckBox) this.brightnessPanel.findViewById(R.id.autoCheck);
            this.brightnessSK.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        ActivityTxt.this.setScreenBrightness(progress - 50, true);
                        ActivityTxt.this.brightAutoCheck.setChecked(false);
                    }
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            this.brightnessPanel.findViewById(R.id.decTv).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    A.brightnessValue = (ActivityTxt.this.brightnessSK.getProgress() - 50) - 1;
                    ActivityTxt.this.setScreenBrightness(A.brightnessValue, true);
                    ActivityTxt.this.setBrightnessSKValue();
                }
            });
            this.brightnessPanel.findViewById(R.id.incTv).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    A.brightnessValue = (ActivityTxt.this.brightnessSK.getProgress() - 50) + 1;
                    ActivityTxt.this.setScreenBrightness(A.brightnessValue, true);
                    ActivityTxt.this.setBrightnessSKValue();
                }
            });
            this.brightAutoCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        A.brightnessValue = -100;
                        ActivityTxt.this.setScreenBrightness(-100, false);
                        return;
                    }
                    ActivityTxt.this.setScreenBrightness(ActivityTxt.this.brightnessSK.getProgress() - 50, true);
                }
            });
            this.bluelightSK = (SeekBar) this.brightnessPanel.findViewById(R.id.opacitySK);
            this.bluelightSK.setMax(95);
            this.bluelightSK.setProgress(A.bluelightOpacity);
            this.bluelightCheck = (CheckBox) this.brightnessPanel.findViewById(R.id.opacityCheck);
            this.bluelightCheck.setChecked(A.bluelightEnable);
            this.bluelightSK.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        A.bluelightEnable = true;
                        A.bluelightOpacity = progress;
                        ActivityTxt.this.updateBluelightFilter();
                    }
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            this.brightnessPanel.findViewById(R.id.decTv2).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    A.bluelightEnable = true;
                    A.bluelightOpacity--;
                    ActivityTxt.this.updateBluelightFilter();
                }
            });
            this.brightnessPanel.findViewById(R.id.incTv2).setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    A.bluelightEnable = true;
                    A.bluelightOpacity++;
                    ActivityTxt.this.updateBluelightFilter();
                }
            });
            this.bluelightCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    A.bluelightEnable = isChecked;
                    ActivityTxt.this.updateBluelightFilter();
                    A.log("bluelight filter enable: " + A.bluelightEnable);
                }
            });
        }
    }

    private void updateBluelightFilter() {
        if (A.bluelightOpacity < 0) {
            A.bluelightOpacity = 0;
        }
        if (A.bluelightOpacity > 100) {
            A.bluelightOpacity = 100;
        }
        if (A.bluelightOpacity == 0) {
            A.bluelightEnable = false;
        }
        showShadeCoverOnView();
        if (this.bluelightSK != null) {
            this.bluelightSK.setProgress(A.bluelightOpacity);
            this.bluelightCheck.setChecked(A.bluelightEnable);
        }
        T.showToastText(this, T.forceWhite(A.bluelightOpacity + "%"));
    }

    protected void setBrightnessSKValue() {
        if (this.brightnessSK != null) {
            this.brightnessSK.setProgress(A.brightnessValue >= -50 ? A.brightnessValue + 50 : 100);
            this.brightAutoCheck.setChecked(A.brightnessValue == -100);
        }
    }

    private void adjustTxtViewText() {
        try {
            switch (getBookType()) {
                case 0:
                    int i;
                    int y = this.txtScroll.getScrollY();
                    int h = this.txtView.getRealHeight();
                    int lh = this.txtView.getLineHeight();
                    int sh = A.getPageHeight();
                    if (A.isInAutoScroll && A.landscape2PageMode && A.isLandscape()) {
                        h = this.txtView.getLineTop(this.txtView.getLineCount()) + this.txtView.getLineHeight();
                    }
                    int offset = -1;
                    if (y < lh / 2 && A.lastBlockIndex > 1) {
                        offset = 0;
                    } else if (sh + y > ((h - lh) - (lh / 2)) - 1 && A.lastBlockIndex < A.getTxts().size() - 2) {
                        offset = 1;
                    }
                    if (offset == 0) {
                        if (A.lastBlockIndex == 0) {
                            i = 0;
                        } else {
                            i = A.lastBlockIndex - 1;
                        }
                        A.lastPosition = (long) (A.getPriorTxtLength(i) + 5);
                        showTxtByPosition(A.lastPosition, null);
                    }
                    if (offset == 1 && this.txtView.getLayout() != null) {
                        A.log("----load next txt block-----");
                        i = A.lastBlockIndex;
                        int j = 0;
                        if (i == 0) {
                            j = 0 + 1;
                        }
                        i += j;
                        txtViewSetText(A.chineseJianFanConvert(A.getTxts2(i + 0) + A.getTxts2(i + 1) + A.getTxts2(i + 2)));
                        int line = this.txtView.getLayout().getLineForOffset((A.getTxts2(i + 0).length() + A.getTxts2(i + 1).length()) - 1);
                        if ((line - (sh / lh)) - 1 > 0) {
                            line = (line - (sh / lh)) - 1;
                        }
                        txtScrollTo(this.txtView.getLineTop2(line) + 2);
                        A.lastBlockIndex = (i + 1) + j;
                        return;
                    }
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            A.error(e);
        }
        A.error(e);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        recordUserActiveTime();
        if (event.getAction() == 1) {
            this.setScreenAwakeTime = 0;
            checkScreenAwake();
        }
        try {
            saveEvent(event);
            A.touchingView = v;
            boolean webOrPdf = this.web != null || isPdf();
            boolean isAutoState = isAutoState();
            if (stopTouchEvent(v, event)) {
                return true;
            }
            if (!isAutoState && !webOrPdf && multiTouchForFontSize(event)) {
                return true;
            }
            if (this.web == null && isHighlightEvent(v, event)) {
                return true;
            }
            if (!isAutoState && this.web == null && doOnCurlTouch(v, event)) {
                pdfAfterOnTouch(event);
                return true;
            }
            float xMove = Math.abs(getTouchX(event) - this.pressDownX);
            float yMove = Math.abs(event.getY() - this.pressDownY);
            switch (event.getAction()) {
                case 1:
                    if (!(((v instanceof ScrollView) && do_Gesture_Event(event)) || dotVisible())) {
                        boolean movingChangedChapter = false;
                        this.xFlingTime = 0;
                        if (this.mTouchTimes <= 20 && yMove < ((float) A.d(10.0f)) && xMove < ((float) A.d(10.0f)) && SystemClock.elapsedRealtime() - this.yFlingTime > 500) {
                            int ignoreTime = this.web != null ? 200 : A.ONTOUCH_IGNORETIME;
                            if (this.pressTime == -1 || SystemClock.elapsedRealtime() - this.pressTime < ((long) ignoreTime)) {
                                this.xFlingTime = SystemClock.elapsedRealtime();
                                do_TapUp_Event(event);
                                if (this.web != null) {
                                    this.web.clickTime = SystemClock.elapsedRealtime();
                                }
                            }
                        } else if (this.preparedMovingChangeChapter) {
                            this.tmpHorizaontalScroll = true;
                            movingChangedChapter = true;
                            pageScroll(this.noScrollDist > 0.0f ? -1 : 1);
                            A.moveStart = true;
                        }
                        this.yFlingTime = 0;
                        this.mTouchTimes = 0;
                        this.movingStopEventY = -1.0f;
                        this.priorMovingEventY = -1.0f;
                        this.priorMovingY = -1.0f;
                        showMovingChangeChapterState(false);
                        if (movingChangedChapter) {
                            return true;
                        }
                    }
                    break;
                case 2:
                    if (this.autoScrollPaused && !this.pressDown_move_for_speed) {
                        return false;
                    }
                    if (!this.inPreShow && SystemClock.elapsedRealtime() - this.yFlingTime > 500) {
                        if (A.adjustBrightness && do_AdjustBrightness_Event(event)) {
                            return true;
                        }
                        if (A.adjustFontSizeAtSlide && do_AdjustFontSize_Event(event)) {
                            return true;
                        }
                        if (!this.layoutVisible && this.mTouchTimes > 10) {
                            showScrollbarProgress(true);
                        }
                    }
                    if (this.pagingByMove) {
                        return true;
                    }
                    if (isAutoState || webOrPdf || !isShiftGesture(xMove, yMove, event)) {
                        checkMovingToChangeChapter(v, event, webOrPdf, isAutoState, xMove, yMove);
                        break;
                    }
                    return true;
                    break;
            }
            if (!isAutoState && !webOrPdf) {
                initFling();
                if (this.mGestureDetector.onTouchEvent(event)) {
                    return true;
                }
                if (event.getAction() == 2) {
                    if (isDisableMove()) {
                        return true;
                    }
                    if (isDisableMovedFlips() && !isMiddleTap(event)) {
                        return true;
                    }
                    if (!A.moveStart && ((double) xMove) > ((double) yMove) * 0.7d) {
                        return true;
                    }
                    if (this.xFlingTime != 0) {
                        if (yMove < ((float) A.d(5.0f))) {
                            return true;
                        }
                        this.xFlingTime = 0;
                    }
                    A.moveStart = true;
                    this.yBeforePageDown.clear();
                }
            } else if (isPdf()) {
                if (A.pdf_scoll_lock) {
                    initFling();
                    if (this.mGestureDetector.onTouchEvent(event)) {
                        return true;
                    }
                }
                pdfAfterOnTouch(event);
            }
            if (event.getAction() == 1 && (v instanceof ScrollView) && !this.layoutVisible) {
                showScrollbarProgress(false);
            }
            return false;
        } catch (Throwable e) {
            A.error(e);
            return true;
        }
    }

    private void checkMovingToChangeChapter(View v, MotionEvent event, boolean webOrPdf, boolean isAutoState, float xMove, float yMove) {
        boolean pdfReflow = isPdf() && this.pdf != null && this.pdf.textReflow;
        if (pdfReflow || (!this.hasMarginAction && A.isFlipNone() && !isAutoState && (v instanceof ScrollView) && this.web == null && !dualPageEnabled() && !dotVisible() && (getBookType() == 100 || getBookType() == 1))) {
            int y = this.txtScroll.getScrollY();
            if (this.priorMovingY == -1.0f) {
                this.priorMovingY = (float) y;
                this.priorMovingEventY = event.getY();
            } else if (this.wheelVisible || (yMove > xMove && Math.abs(event.getY() - this.priorMovingEventY) > ((float) A.d(2.0f)))) {
                this.priorMovingEventY = event.getY();
                if (!this.wheelVisible && this.priorMovingY != ((float) y)) {
                    this.priorMovingY = (float) y;
                    this.movingStopEventY = -1.0f;
                    this.noScrollDist = 0.0f;
                } else if (this.movingStopEventY == -1.0f) {
                    this.movingStopEventY = event.getY();
                } else {
                    float dist = event.getY() - this.movingStopEventY;
                    if (Math.abs(dist) > ((float) A.d(61.0f))) {
                        this.movingStopEventY = event.getY() - ((float) A.d(dist > 0.0f ? 61.0f : -61.0f));
                    }
                    if ((dist <= 0.0f || this.noScrollDist >= 0.0f) && (dist >= 0.0f || this.noScrollDist <= 0.0f)) {
                        this.noScrollDist = dist;
                    } else {
                        showMovingChangeChapterState(false);
                        this.movingStopEventY = -1.0f;
                        this.noScrollDist = 0.0f;
                    }
                }
            }
            if (this.wheelVisible || ((this.noScrollDist > ((float) A.d(8.0f)) && this.txtScroll.getScrollY() < A.d(2.0f)) || (this.noScrollDist < ((float) (-A.d(8.0f))) && this.txtScroll.getScrollY() > (this.txtView.getHeight() - A.getPageHeight()) - A.d(2.0f)))) {
                showMovingChangeChapterState(true);
                return;
            }
            return;
        }
        showMovingChangeChapterState(false);
    }

    private void showMovingChangeChapterState(boolean visible) {
        if (visible && ((this.noScrollDist < 0.0f && isEndOfBook()) || (this.noScrollDist > 0.0f && isBeginOfBook()))) {
            visible = false;
        }
        this.preparedMovingChangeChapter = visible;
        if (visible) {
            boolean z;
            if (this.wheel == null) {
                this.wheel = new ProgressWheel(this);
                this.baseFrame.addView(this.wheel);
                this.wheel.setCircleColor(0);
                this.wheel.setContourColor(0);
                this.wheel.setRimColor(0);
                this.wheel.startSpinning();
            }
            this.wheel.setBarColor(A.getAlphaColor(A.fontColor, -150));
            LayoutParams fl = (LayoutParams) this.wheel.getLayoutParams();
            int d = A.d(40.0f);
            fl.height = d;
            fl.width = d;
            fl.gravity = 1;
            if (this.noScrollDist > 0.0f) {
                d = A.d(10.0f);
            } else {
                d = ((this.baseFrame.getHeight() - fl.height) - (showStatusbar() ? this.statusLay.getHeight() : 0)) - A.d(10.0f);
            }
            fl.topMargin = d;
            int l = (int) Math.abs((this.noScrollDist * CSS.FLOAT_ADDED) / A.getDensity());
            if (l >= 360) {
                z = true;
            } else {
                z = false;
            }
            this.preparedMovingChangeChapter = z;
            ProgressWheel progressWheel = this.wheel;
            if (l > 360) {
                l = 360;
            }
            progressWheel.setBarLength(l);
            this.wheel.barPaint2.setColor(A.isNightState() ? -1 : ViewCompat.MEASURED_STATE_MASK);
            this.wheel.bringToFront();
            this.wheel.setVisibility(0);
            this.wheelVisible = true;
            if (this.preparedMovingChangeChapter && this.noScrollDist > 0.0f) {
                prePriorChapter();
                return;
            }
            return;
        }
        this.wheelVisible = false;
        if (this.wheel != null && this.wheel.getVisibility() == 0) {
            this.wheel.setVisibility(8);
        }
    }

    private void saveEvent(MotionEvent event) {
        if (this.hMotionEvent != null) {
            this.hMotionEvent.recycle();
        }
        this.hMotionEvent = MotionEvent.obtain(event);
        if (event.getAction() == 0) {
            if (this.hDownEvent != null) {
                this.hDownEvent.recycle();
            }
            this.hDownEvent = MotionEvent.obtain(event);
        }
    }

    private boolean isShiftGesture(float xMove, float yMove, MotionEvent event) {
        if (dotVisible() || SystemClock.elapsedRealtime() - this.pageScrollTime < 500) {
            return false;
        }
        if (A.isFlipHorizShift() && xMove >= yMove && xMove > ((float) A.d(20.0f)) && yMove < ((float) A.d(40.0f))) {
            this.pagingByMove = true;
            pageScroll(getTouchX(event) - this.pressDownX < 0.0f ? 1 : -1);
            return true;
        } else if (!A.isFlipVerticalShift()) {
            return false;
        } else {
            if (isMiddleTap(this.hDownEvent)) {
                return false;
            }
            if (xMove > yMove || yMove <= ((float) A.d(20.0f)) || xMove >= ((float) A.d(40.0f))) {
                return true;
            }
            this.pagingByMove = true;
            pageScroll(event.getY() - this.pressDownY < 0.0f ? 1 : -1);
            return true;
        }
    }

    private boolean isMiddleTap(MotionEvent event) {
        if (event == null) {
            event = this.hMotionEvent;
        }
        int w = this.baseFrame.getWidth();
        int h = this.baseFrame.getHeight();
        float x = getTouchX(event);
        float y = event.getY();
        return x > ((float) (w / 3)) && x < ((float) ((w * 2) / 3)) && y > ((float) (h / 3)) && y < ((float) ((h * 2) / 3)) && this.pressDownX > ((float) (w / 3)) && this.pressDownX < ((float) ((w * 2) / 3)) && this.pressDownY > ((float) (h / 3)) && this.pressDownY < ((float) ((h * 2) / 3));
    }

    private boolean isDisableMovedFlips() {
        return A.flip_animation == 3 || A.flip_animation == 6;
    }

    private boolean isHighlightEvent(View v, MotionEvent event) {
        if ((v == this.pdf || (v instanceof ScrollView)) && A.doLongTap != 15) {
            switch (event.getAction()) {
                case 0:
                    if (!(this.longTimeTapEventSent || this.longTimeTapEventDown)) {
                        this.longTimeTapEvent = true;
                        this.longTimeTapEventDown = true;
                        int delay = (int) (1000.0f * A.longTapInterval);
                        if (isTouchInEdge(event)) {
                            delay = (delay * 15) / 10;
                        }
                        this.handler.sendEmptyMessageDelayed(501, (long) delay);
                        this.longTimeTapEventSent = true;
                    }
                    if (!hideDotViews()) {
                        this.pressDown = true;
                        break;
                    }
                    this.pressDown = false;
                    return true;
                case 1:
                    this.longTimeTapEvent = false;
                    this.longTimeTapEventDown = false;
                    if (this.pressDown) {
                        this.pressDown = false;
                        break;
                    }
                    this.xFlingTime = SystemClock.elapsedRealtime();
                    return true;
                case 2:
                    if (stopLongTap(event)) {
                        this.longTimeTapEvent = false;
                        hideDotViews();
                    }
                    if (!this.pressDown) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    private boolean stopLongTap(MotionEvent event) {
        if ((!isPdf() || event.getPointerCount() <= 1) && Math.abs(event.getY() - this.pressDownY) <= ((float) A.d(10.0f)) && Math.abs(getTouchX(event) - this.pressDownX) <= ((float) A.d(10.0f))) {
            return false;
        }
        return true;
    }

    private boolean stopTouchEvent(View v, MotionEvent event) {
        if (isPdf() && (this.pdfAnnotStart || SystemClock.elapsedRealtime() - this.annotEndTime < 500)) {
            return true;
        }
        if (isPdf() && PDFReader.innerLinkRecord != -1 && event.getAction() == 1) {
            PDFReader.innerLinkRecord = -1;
            return true;
        }
        if (A.isInAutoScroll || v == this.txtLayS) {
            auto_scroll_touch(event);
        }
        if (A.isSpeaking) {
            if (!this.tts_panel_visible) {
                speak_touch(event);
            } else if (event.getAction() != 1) {
                return true;
            } else {
                showTtsPanel(false);
                if (!this.tts_paused) {
                    return true;
                }
                restore_speak();
                return true;
            }
        }
        if (hideBrightnessPanel() || hideFontZoom() || pdfHideSearchLay()) {
            return true;
        }
        if (this.layoutVisible) {
            inverseLayoutVisible(true);
            return true;
        }
        switch (event.getAction()) {
            case 0:
                this.pagingByMove = false;
                setTiltPaused(true);
                this.moveStart2 = A.moveStart;
                A.moveStart = false;
                this.swipeGuesture = false;
                this.mTouchTimes = 0;
                this.continueMoveX = 0.0f;
                this.pressTime = SystemClock.elapsedRealtime();
                this.pressDownX = getTouchX(event);
                this.pressDownY = event.getY();
                this.movingStopEventY = -1.0f;
                this.priorMovingEventY = -1.0f;
                this.priorMovingY = -1.0f;
                this.preparedMovingChangeChapter = false;
                this.birghtnessPos = -1.0f;
                this.fontsizePos = -1.0f;
                this.hasMarginAction = false;
                this.dragToSelectText = false;
                releaseMagnifier();
                break;
            case 1:
                setTiltPaused(false);
                releaseMagnifier();
                this.select_text_state = false;
                if (!this.dragToSelectText) {
                    if (this.brightnessSetted) {
                        this.brightnessSetted = false;
                        setLeds();
                        break;
                    }
                }
                this.dragToSelectText = false;
                this.longTimeTapEventDown = false;
                doHighlight2(null, -1, -1, true);
                A.lastFileAnnotUpdated = true;
                return true;
                break;
            case 2:
                if (!this.autoScrollPaused || this.pressDown_move_for_speed) {
                    this.mTouchTimes++;
                    if (A.pdfStatus == STATUS.sta_sel) {
                        this.select_text_state = true;
                    }
                    if (this.select_text_state) {
                        if (A.pdfStatus != STATUS.sta_sel) {
                            select_move_down(event.getY());
                            selectText(true);
                        }
                        if ((Math.abs(getTouchX(event) - this.pressDownX) > ((float) A.d(24.0f)) || Math.abs(event.getY() - this.pressDownY) > ((float) A.d(16.0f))) && A.directHighlight) {
                            this.dragToSelectText = true;
                        }
                        if (!A.showMagnifier) {
                            return true;
                        }
                        this.contentLay.magnifierStart = true;
                        this.hBar.drawMagnifier(event);
                        return true;
                    } else if (!isAutoState()) {
                        if (!isDisableMove()) {
                            adjustTxtViewText();
                            break;
                        }
                    } else {
                        return true;
                    }
                }
                if (isRollingScroll()) {
                    this.scrollPos = (int) event.getY();
                    this.scrollImage.topLine = this.scrollPos;
                    this.scrollImage.invalidate();
                }
                return isRollingScroll();
                break;
        }
        return false;
    }

    private void releaseMagnifier() {
        if (!T.isRecycled(this.hBar.bm)) {
            this.hBar.stopMagnifier();
        }
        if (this.contentLay.magnifierStart) {
            this.contentLay.magnifierStart = false;
            this.contentLay.releaseCache();
            updateForFitHardwareAccelerate();
            System.gc();
        }
    }

    private void select_move_down(float y) {
        if (SystemClock.elapsedRealtime() - this.select_scroll_time >= 100) {
            this.select_scroll_time = SystemClock.elapsedRealtime();
            int lh = this.txtView.getLineHeight();
            View tmp;
            if (((double) (((float) A.getScreenHeight2()) - y)) < ((double) lh) * 1.5d) {
                if (!lastLineDisplayed() && this.txtView.getHeight() - this.txtScroll.getScrollY() > lh) {
                    txtScrollTo(this.txtScroll.getScrollY() + lh);
                    showReadProgress(0);
                    if (!this.select_text_state) {
                        tmp = this.dot == this.dot1 ? this.dot2 : this.dot1;
                        tmp.layout(tmp.getLeft(), tmp.getTop() - lh, tmp.getRight(), tmp.getBottom() - lh);
                    }
                    resetFlipCache(false, 500);
                }
            } else if (y < ((float) lh) && this.txtScroll.getScrollY() > lh) {
                txtScrollTo(this.txtScroll.getScrollY() - lh);
                showReadProgress(0);
                if (!this.select_text_state) {
                    tmp = this.dot == this.dot1 ? this.dot2 : this.dot1;
                    tmp.layout(tmp.getLeft(), tmp.getTop() + lh, tmp.getRight(), tmp.getBottom() + lh);
                }
                resetFlipCache(false, 500);
            }
        }
    }

    private int getRealLineCount() {
        return this.txtView.getRealLineCount();
    }

    private void auto_scroll_touch(MotionEvent event) {
        boolean z = false;
        switch (event.getActionMasked()) {
            case 0:
                this.pressDownY = event.getY();
                this.pressDownX = event.getX();
                this.pressDown_scroll_value = A.autoScrollSpeed;
                if (event.getPointerCount() < 2) {
                    z = true;
                }
                this.pressDown_move_for_speed = z;
                if (!this.pressDown_move_for_speed) {
                    this.autoScrollPaused = true;
                    return;
                }
                return;
            case 1:
                if (!this.pressDown_move_for_speed) {
                    this.autoScrollPaused = false;
                    return;
                }
                return;
            case 2:
                if (!dotVisible()) {
                    if (this.pressDown_move_for_speed && event.getPointerCount() >= 2) {
                        this.pressDown_move_for_speed = false;
                    }
                    float moveX = Math.abs(event.getX() - this.pressDownX);
                    if (Math.abs(event.getY() - this.pressDownY) > moveX) {
                        int step = ((int) (event.getY() - this.pressDownY)) / A.d(20.0f);
                        if (!this.pressDown_move_for_speed) {
                            this.autoScrollPaused = true;
                            return;
                        } else if (step != 0 || this.pressDown_scroll_value != A.autoScrollSpeed) {
                            A.autoScrollSpeed = this.pressDown_scroll_value + step;
                            if (A.autoScrollSpeed < 0) {
                                A.autoScrollSpeed = 0;
                            }
                            if (A.autoScrollSpeed > 100) {
                                A.autoScrollSpeed = 100;
                            }
                            T.showToastText(this, getString(R.string.scroll_speed) + " " + (100 - A.autoScrollSpeed) + "%");
                            resetPixelScrollSpeed();
                            return;
                        } else {
                            return;
                        }
                    } else if (moveX > ((float) A.d(80.0f)) && this.pressDown_move_for_speed) {
                        do_AutoScroll(false);
                        this.pressDown = false;
                        return;
                    } else {
                        return;
                    }
                }
                return;
            default:
                return;
        }
    }

    private void speak_touch(MotionEvent event) {
        if (this.tts_volume != null) {
            switch (event.getActionMasked()) {
                case 0:
                    this.pressDownY = event.getY();
                    this.pressDownX = event.getX();
                    this.pressDown_speak_value = this.tts_volume.getProgress();
                    this.pressDown_move_for_volume = true;
                    A.log("pressDown_speak_volume: " + this.pressDown_speak_value);
                    return;
                case 1:
                    if (!this.pressDown_move_for_volume && this.pressDown_speed_speed != A.tts_speed) {
                        A.tts_speed = this.pressDown_speed_speed;
                        restore_speak();
                        return;
                    }
                    return;
                case 2:
                    if (!dotVisible()) {
                        if (this.pressDown_move_for_volume && event.getPointerCount() >= 2) {
                            this.pressDown_move_for_volume = false;
                            this.pressDown_speak_value = A.tts_speed;
                            this.pressDown_speed_speed = A.tts_speed;
                        }
                        float moveX = Math.abs(event.getX() - this.pressDownX);
                        if (Math.abs(event.getY() - this.pressDownY) > moveX) {
                            int step = ((int) (event.getY() - this.pressDownY)) / A.d(40.0f);
                            int value;
                            if (this.pressDown_move_for_volume) {
                                if (step != 0 || this.tts_volume.getProgress() != this.pressDown_speak_value) {
                                    value = this.pressDown_speak_value - step;
                                    if (value < 1) {
                                        value = 1;
                                    }
                                    if (value > this.tts_volume.getMax()) {
                                        value = this.tts_volume.getMax();
                                    }
                                    this.tts_volume.setProgress(value);
                                    T.showToastText(this, getString(R.string.tts_volume) + " " + value);
                                    ((AudioManager) getSystemService("audio")).setStreamVolume(3, value, 0);
                                    return;
                                }
                                return;
                            } else if (step != 0 || A.tts_speed != this.pressDown_speak_value) {
                                value = this.pressDown_speak_value - step;
                                if (value < 1) {
                                    value = 1;
                                }
                                if (value > maxTtsSpeed()) {
                                    value = maxTtsSpeed();
                                }
                                tts_speed_setProgress(value);
                                this.pressDown_speed_speed = value;
                                T.showToastText(this, getString(R.string.tts_speed) + " " + value);
                                return;
                            } else {
                                return;
                            }
                        } else if (moveX > ((float) A.d(80.0f)) && this.pressDown_move_for_volume) {
                            stop_speak();
                            this.pressDown = false;
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void tts_speed_setProgress(int value) {
        if (value > 30) {
            value = ((value - 30) / 2) + 30;
        }
        this.tts_speed.setProgress(value);
    }

    protected int get_tts_speed_progress(int progress) {
        if (progress > 30) {
            return ((progress - 30) * 2) + 30;
        }
        return progress > 0 ? progress : 1;
    }

    private int maxTtsSpeed() {
        return this.tts_speed.getMax() + 10;
    }

    double getDistanceOfTwoPoint(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return Math.sqrt((double) ((dx * dx) + (dy * dy)));
    }

    private boolean multiTouchForFontSize(MotionEvent event) {
        if ((!A.mult_touch && this.web == null) || isPdf() || isTouchInEdge(event)) {
            return false;
        }
        switch (event.getActionMasked()) {
            case 0:
                return false;
            case 1:
                if (!this.multi_touching) {
                    return false;
                }
                this.multi_touching = false;
                this.multi_distance = -1.0d;
                return true;
            case 2:
                if (!this.multi_touching || event.getPointerCount() < 2) {
                    return false;
                }
                setCurlViewINVISIBLE();
                setFlipViewINVISIBLE(true);
                if (this.multi_distance == -1.0d) {
                    this.multi_distance = getDistanceOfTwoPoint(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                } else {
                    double distance2 = getDistanceOfTwoPoint(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    if (this.web == null) {
                        float adjust = A.fontSize + ((float) (((int) (distance2 - this.multi_distance)) / A.d(20.0f)));
                        if (A.fontSize != adjust && adjust > 5.0f && adjust < 200.0f) {
                            if (event.getPointerCount() > 2) {
                                adjust = A.fontSize + ((adjust - A.fontSize) / 10.0f);
                            }
                            A.fontSize = adjust;
                            if (A.fontSize > ((float) A.maxFontSize)) {
                                A.fontSize = (float) A.maxFontSize;
                            }
                            if (A.fontSize < ((float) A.minFontSize)) {
                                A.fontSize = (float) A.minFontSize;
                            }
                            setFontSize();
                            checkStatusBar();
                            this.multi_distance = distance2;
                            T.showToastText(this, T.forceWhite(new DecimalFormat("0.0").format((double) A.fontSize)));
                        }
                    } else {
                        int adjust2 = ((int) (distance2 - this.multi_distance)) / A.d(20.0f);
                        if (Math.abs(adjust2) > 0) {
                            A.lastWebFontSize += adjust2;
                            if (A.lastWebFontSize > 72) {
                                A.lastWebFontSize = 72;
                            }
                            if (A.lastWebFontSize < 1) {
                                A.lastWebFontSize = 1;
                            }
                            setFontSize();
                            this.multi_distance = distance2;
                            T.showToastText(this, T.forceWhite(new DecimalFormat("0.0").format((long) A.lastWebFontSize)));
                        }
                    }
                }
                return true;
            case 5:
                if (event.getPointerCount() < 2) {
                    return false;
                }
                this.multi_touching = true;
                return true;
            default:
                return false;
        }
    }

    public boolean isTouchInEdge(MotionEvent event) {
        return (A.adjustBrightness && isWipeInEdge(A.brightness_edge, event)) || (A.adjustFontSizeAtSlide && isWipeInEdge(A.fontsize_edge, event));
    }

    public boolean isWipeInEdge(int edge, MotionEvent event) {
        int cw = getEdgeSize();
        int w = this.baseFrame.getWidth();
        int h = this.baseFrame.getHeight();
        float x = getTouchX(event);
        float y = event.getY();
        switch (edge) {
            case 0:
                if (x >= ((float) cw) || this.pressDownX >= ((float) cw)) {
                    return false;
                }
                return true;
            case 1:
                if (x <= ((float) (w - cw)) || this.pressDownX <= ((float) (w - cw))) {
                    return false;
                }
                return true;
            case 2:
                if (y >= ((float) cw) || this.pressDownY >= ((float) cw)) {
                    return false;
                }
                return true;
            default:
                if (y <= ((float) (h - cw)) || this.pressDownY <= ((float) (h - cw))) {
                    return false;
                }
                return true;
        }
    }

    private int getEdgeSize() {
        float f;
        if (A.isTablet) {
            f = (float) (A.isLargeTablet ? 60 : 50);
        } else {
            f = 40.0f;
        }
        return A.d(f);
    }

    private float edgeMoveLength(int edge, MotionEvent event) {
        return (edge == 0 || edge == 1) ? event.getY() - this.pressDownY : getTouchX(event) - this.pressDownX;
    }

    private float edgeMovePos(int edge, MotionEvent event) {
        return (edge == 0 || edge == 1) ? event.getY() : getTouchX(event);
    }

    private int edgeLength(int edge) {
        return (edge == 0 || edge == 1) ? this.baseFrame.getHeight() : this.baseFrame.getWidth();
    }

    private boolean do_AdjustBrightness_Event(MotionEvent event) {
        if (isWipeInEdge(A.brightness_edge, event)) {
            if (Math.abs(edgeMoveLength(A.brightness_edge, event)) > ((float) A.d(15.0f)) || this.fontsizePos != -1.0f) {
                float pos = edgeMovePos(A.brightness_edge, event);
                if (this.birghtnessPos == -1.0f) {
                    this.birghtnessPos = pos;
                }
                int total = edgeLength(A.brightness_edge);
                if (total < HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES) {
                    total = HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES;
                }
                int light = (int) (((this.birghtnessPos - pos) * 100.0f) / ((float) total));
                this.birghtnessPos = pos;
                if (light != 0) {
                    if (A.brightnessValue == -100) {
                        A.brightnessValue = 50;
                    }
                    A.brightnessValue += light;
                    setScreenBrightness(A.brightnessValue, true);
                    resetFlipCache(false, 1000);
                    this.hasMarginAction = true;
                    if (A.adjustBrightnessTipped) {
                        return true;
                    }
                    A.adjustBrightnessTipped = true;
                    T.showAlertText(this, getString(R.string.tip) + " (" + getString(R.string.miscellaneous) + ")", getString(R.string.swpie_edge_brightness));
                    return true;
                } else if (A.isFlipCurl()) {
                    return false;
                } else {
                    return true;
                }
            } else if (!A.isFlipCurl()) {
                return true;
            }
        }
        this.birghtnessPos = -1.0f;
        return false;
    }

    private void resetFlipCache(boolean resetCachShot, int delay) {
        this.lastFlipScrollY = -1;
        if (this.curl3dCover != null) {
            this.curl3dCover.setTag(null);
        }
        if (resetCachShot && this.tmpFlipShot3 != null) {
            createCachePageShotsHandler(delay);
        }
        if (this.curl3d != null && this.curl3dLay.getVisibility() == 0) {
            setCurl3dVisibility(false);
        }
        if (getBookType() == 0) {
            T.recycle(this.tmpFlipShot3);
        }
    }

    private boolean do_AdjustFontSize_Event(MotionEvent event) {
        float f = 0.1f;
        if (isWipeInEdge(A.fontsize_edge, event)) {
            float pos = edgeMovePos(A.fontsize_edge, event);
            if (this.fontsizePos == -1.0f) {
                this.fontsizePos = pos;
            } else {
                float adjust = this.fontsizePos - pos;
                if (Math.abs(adjust) > ((float) A.d(40.0f))) {
                    resetPageCount();
                    if (isPdf() && !this.pdf.textReflow) {
                        if (adjust <= 0.0f) {
                            f = -0.1f;
                        }
                        pdfZoomRatio(f);
                    } else if (this.web == null) {
                        float level = event.getPointerCount() >= 2 ? 0.1f : 1.0f;
                        f = A.fontSize;
                        if (adjust <= 0.0f) {
                            level = -level;
                        }
                        A.fontSize = f + level;
                        if (A.fontSize > ((float) A.maxFontSize)) {
                            A.fontSize = (float) A.maxFontSize;
                        }
                        if (A.fontSize < ((float) A.minFontSize)) {
                            A.fontSize = (float) A.minFontSize;
                        }
                        T.showToastText(this, T.forceWhite(new DecimalFormat("0.0").format((double) A.fontSize)));
                        setFontSize();
                    } else {
                        webViewZoom(adjust > 0.0f);
                    }
                    this.fontsizePos = pos;
                    this.hasMarginAction = true;
                    return true;
                }
            }
            if (!A.isFlipCurl()) {
                return true;
            }
        }
        this.fontsizePos = -1.0f;
        return false;
    }

    private void do_TapUp_Event(MotionEvent event) {
        boolean z = true;
        if (!hide_ebook_cover()) {
            if (this.layoutVisible) {
                inverseLayoutVisible(true);
            } else if (this.lastTapUpTime == -1 || SystemClock.elapsedRealtime() - this.lastTapUpTime >= 100) {
                this.lastTapUpTime = SystemClock.elapsedRealtime();
                if (A.isInAutoScroll) {
                    if (this.autoScrollPaused) {
                        z = false;
                    }
                    this.autoScrollPaused = z;
                    if (this.autoScrollPaused) {
                        T.showToastText(this, getString(R.string.pause));
                    }
                } else if (A.isSpeaking) {
                    do_tts_buttons(this.tts_play);
                    if (this.tts_paused) {
                        T.showToastText(this, getString(R.string.pause));
                    }
                } else if (!isUrlClick(event, true) && !clickForBookmark(event)) {
                    switch (A.clickArea(event)) {
                        case 0:
                            boolean interrupt = false;
                            if (isSingleTapForTextSelection()) {
                                do_text_select(true);
                                if (this.pdf == null || this.pdf.textReflow) {
                                    interrupt = dotVisible();
                                }
                            }
                            if (!interrupt) {
                                inverseLayoutVisible(false);
                                return;
                            }
                            return;
                        case 1:
                            inverseLayoutVisible(true);
                            doTouchDown(1);
                            return;
                        case 2:
                            inverseLayoutVisible(true);
                            doTouchDown(2);
                            return;
                        case 3:
                            inverseLayoutVisible(true);
                            doTouchDown(3);
                            return;
                        case 4:
                            inverseLayoutVisible(true);
                            doTouchDown(4);
                            return;
                        default:
                            return;
                    }
                }
            }
        }
    }

    private boolean clickForBookmark(MotionEvent event) {
        if (getTouchX(event) <= ((float) (this.baseFrame.getWidth() - A.d(40.0f))) || event.getY() >= ((float) A.d(40.0f))) {
            return false;
        }
        createBookmark();
        resetFlipCache(false, 500);
        return true;
    }

    private boolean isSingleTapForTextSelection() {
        return A.doTapScreenTop == 17 || A.doTapScreenBottom == 17 || A.doTapScreenLeft == 17 || A.doTapScreenRight == 17;
    }

    private boolean isVisualBoomarkClick(MotionEvent event) {
        if (this.contentLay.visualBookmarks.size() == 0) {
            return false;
        }
        int h = (this.txtView.getLineHeight() * 80) / 100;
        int w = (h * 50) / 70;
        float x = getTouchX(event);
        float y = event.getY();
        Iterator it = this.contentLay.visualBookmarks.iterator();
        while (it.hasNext()) {
            final Bookmark bm = (Bookmark) it.next();
            if (x > ((float) ((this.contentLay.getWidth() - w) - A.d(1.0f))) && y > ((float) bm.drawY2) && y < ((float) (bm.drawY2 + h))) {
                CharSequence[] items = new String[]{A.getStringArrayItem(R.array.catalog_popup_menu, 1), A.getStringArrayItem(R.array.one_file_bookmark, 1)};
                CharSequence title = bm.name;
                if (title.indexOf(") ") != -1) {
                    title = title.substring(title.indexOf(") ") + 2, title.length());
                }
               new AlertDialog.Builder(this).setTitle(title).setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                PrefEditBookmark prefEditBookmark = new PrefEditBookmark(ActivityTxt.this, bm.name, new OnAfterEdit() {
                                    public void AfterEdit(String result, Integer editColor) {
                                        bm.name = result;
                                        bm.color = editColor.intValue();
                                        BookDb.removeBookmark(bm);
                                        BookDb.addBookmark(bm);
                                        A.hasNewNoteForUpload = true;
                                        A.lastFileAnnotUpdated = true;
                                        if (!ActivityTxt.this.updateForFitHardwareAccelerate()) {
                                            ActivityTxt.this.contentLay.postInvalidate();
                                        }
                                    }
                                }, Integer.valueOf(bm.color));
                                return;
                            case 1:
                                int id = A.getBookmarksId(A.lastFile);
                                if (id != -1) {
                                    ((Bookmarks) A.getBookmarks().get(id)).remove(bm);
                                    BookDb.removeBookmark(bm);
                                    A.hasNewNoteForUpload = true;
                                    A.lastFileAnnotUpdated = true;
                                    if (!ActivityTxt.this.updateForFitHardwareAccelerate()) {
                                        ActivityTxt.this.contentLay.postInvalidate();
                                        return;
                                    }
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                    }
                }).show();
                return true;
            }
        }
        return false;
    }

    private boolean isImageClick(MotionEvent event, boolean forMediaOnly, boolean execute) {
        if (isPdf()) {
            return false;
        }
        if (this.web != null) {
            return false;
        }
        if (!isHtmlContent()) {
            return false;
        }
        try {
            ScrollView sv;
            if (A.touchingView == this.txtScroll2) {
                sv = this.txtScroll2;
            } else {
                sv = this.txtScroll;
            }
            MRTextView tv = getTouchTxtView();
            float hx = event.getX() - ((float) sv.getPaddingLeft());
            int hy = (((int) event.getY()) + sv.getScrollY()) - sv.getPaddingTop();
            MyLayout lo = tv.getLayout();
            if (lo == null) {
                return false;
            }
            int line = lo.getLineForVertical(hy);
            int j1 = lo.getLineStart(line);
            int j2 = lo.getLineVisibleEnd(line);
            int floatWidth = lo.getLineFloat(line);
            if (floatWidth != 0) {
                MyFloatSpan sp = lo.getLineFloatSp(line);
                MarginF margins = tv.getCssMargins(line);
                float fx = -1.0f;
                if (floatWidth > 0) {
                    if (hx < margins.left) {
                        fx = hx;
                    }
                } else if (floatWidth < 0 && hx > ((float) tv.getWidth()) - margins.right) {
                    fx = hx - (((float) tv.getWidth()) - margins.right);
                }
                if (!(fx == -1.0f || sp.isDropcap)) {
                    if (sp.float_text == null || ((float) hy) < sp.float_sl_v) {
                        return dealImageSpan(sp.imageSpan, forMediaOnly, execute);
                    } else if (execute) {
                        int fstart = sp.float_sl.getOffsetForHorizontal(sp.float_sl.getLineForVertical((int) (((float) hy) - sp.float_sl_v)), fx);
                        int fend = fstart + 2;
                        fstart -= 2;
                        if (fstart < 0) {
                            fstart = 0;
                        }
                        if (fend > sp.float_text.length()) {
                            fend = sp.float_text.length();
                        }
                        MyUrlSpan[] spans = (MyUrlSpan[]) ((Spanned) sp.float_text).getSpans(fstart, fend, MyUrlSpan.class);
                        if (spans.length > 0) {
                            return executeUrl((Spanned) sp.float_text, spans[0], spans[0].getURL());
                        }
                        copyToClipboard(sp.float_text.toString());
                        return true;
                    }
                }
            }
            int offset = tv.getLineOffset(line, hx);
            if (offset == -1) {
                return false;
            }
            if (j2 > j1) {
                Spanned spanned = null;
                if (tv.getText() instanceof SpannedString) {
                    spanned = (SpannedString) tv.getText();
                }
                if (tv.getText() instanceof SpannableStringBuilder) {
                    spanned = (SpannableStringBuilder) tv.getText();
                }
                if (spanned != null) {
                    Object[] spans2 = spanned.getSpans(j1, j2, Object.class);
                    String lineText = tv.getLineText(line).replace("\n", "").replace("　", "").trim();
                    for (Object o : spans2) {
                        if (o instanceof MyImageSpan) {
                            MyImageSpan sp2 = (MyImageSpan) o;
                            if (sp2.getDrawable().getBounds().width() > this.baseFrame.getWidth() / 2 || lineText.length() == 1) {
                                return dealImageSpan(sp2, forMediaOnly, execute);
                            }
                        }
                    }
                    if (j1 < offset || j2 > offset) {
                        if (j1 < offset) {
                            j1 = offset - 1;
                        }
                        if (j2 > offset) {
                            j2 = offset + 1;
                        }
                        for (Object o2 : spanned.getSpans(j1, j2, Object.class)) {
                            if (o2 instanceof MyImageSpan) {
                                return dealImageSpan((MyImageSpan) o2, forMediaOnly, execute);
                            }
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            A.error(e);
        }
    }

    private boolean dealImageSpan(MyImageSpan sp, boolean forMediaOnly, boolean execute) {
        if (isMediaSpan(sp, forMediaOnly)) {
            return true;
        }
        if (forMediaOnly) {
            return false;
        }
        if (!execute) {
            return true;
        }
        startImageGallery(sp);
        return true;
    }

    private boolean isMediaSpan(MyImageSpan sp, boolean playMedia) {
        String source = sp.getSource();
        if (source.equals("#audio#null") || source.startsWith("#video#null")) {
            T.showToastText(this, "null");
            return true;
        } else if (source.startsWith("#audio#")) {
            if (!playMedia) {
                return true;
            }
            if (source.startsWith("#audio#http") || source.startsWith("#audio#rtsp")) {
                playMp3(source.substring(7));
                return true;
            }
            A.ebook.getDrawableFromSource(source, 0);
            cacheFile = A.book_cache + "/" + T.getFilename(A.lastFile) + "/" + T.getFilename(source).replace("#audio#", "");
            if (!T.isFile(cacheFile)) {
                return true;
            }
            playMp3(cacheFile);
            return true;
        } else if (!source.startsWith("#video#")) {
            return false;
        } else {
            if (!playMedia) {
                return true;
            }
            if (source.startsWith("#video#http")) {
                playVideo(source.substring(7));
                return true;
            }
            A.ebook.getDrawableFromSource(source, 0);
            cacheFile = A.book_cache + "/" + T.getFilename(A.lastFile) + "/" + T.getFilename(source).replace("#video#", "");
            if (!T.isFile(cacheFile)) {
                return true;
            }
            playVideo(cacheFile);
            return true;
        }
    }

    private boolean isUrlClick(MotionEvent event, boolean checkHighlight) {
        this.urlFootnote = null;
        this.checkUrlFootnote = true;
        if (isPdf()) {
            return false;
        }
        if (this.web != null) {
            return false;
        }
        if (A.currentChapterWithImageOnly()) {
            return false;
        }
        if (this.hasMediaTag && isImageClick(event, true, true)) {
            return true;
        }
        if (isHtmlContent()) {
            try {
                ScrollView sv = A.touchingView == this.txtScroll2 ? this.txtScroll2 : this.txtScroll;
                MRTextView tv = getTouchTxtView();
                Spanned spanned = tv.getSpanned();
                if (spanned == null) {
                    return false;
                }
                int y = sv.getScrollY();
                float hx = event.getX() - ((float) sv.getPaddingLeft());
                int hy = (((int) event.getY()) + y) - sv.getPaddingTop();
                MyLayout lo = tv.getLayout();
                if (lo == null) {
                    return false;
                }
                if (hy > tv.getHeight()) {
                    return false;
                }
                int line = lo.getLineForVertical(hy);
                if (tv.lastIgnoreLine() > 0 && line >= tv.lastIgnoreLine()) {
                    return false;
                }
                if (isTableZoomIcon(tv, spanned, line, hx)) {
                    return true;
                }
                int offset = tv.getLineOffset(line, hx);
                if (offset == -1) {
                    return false;
                }
                int j1 = lo.getLineStart(line);
                int j2 = lo.getLineVisibleEnd(line);
                if (j2 == j1) {
                    return false;
                }
                if (offset == j1 && hx < tv.getTextX(line, j1) - ((float) A.d(20.0f))) {
                    return false;
                }
                if (offset >= j2) {
                    if (j2 - j1 > 1) {
                        return false;
                    }
                    float realx1 = tv.getTextX(line, j1);
                    int w = tv.getWidth();
                    if (realx1 < ((float) (w / 3)) && hx > ((float) (w / 3))) {
                        return false;
                    }
                    if (hx > ((float) A.d(20.0f)) + tv.getTextX(line, j2)) {
                        return false;
                    }
                    if (j2 - j1 == 1 && spanned.charAt(j1) == '￼' && (hx < ((float) (w / 3)) || hx > ((float) ((w * 2) / 3)))) {
                        return false;
                    }
                }
                int start = offset;
                if (hx > ((float) A.d(16.0f))) {
                    start = this.txtView.getLineOffset(line, hx - ((float) A.d(16.0f)));
                } else if (j1 < start) {
                    start -= 2;
                }
                if (start < j1) {
                    start = j1;
                }
                int end = offset;
                if (hx < ((float) (this.txtView.getWidth() - A.d(16.0f)))) {
                    end = this.txtView.getLineOffset(line, ((float) A.d(16.0f)) + hx);
                } else if (j2 > offset) {
                    end = offset + 2;
                }
                if (end > j2) {
                    end = j2;
                }
                MyUrlSpan o = getUrlSpan(start, end, offset, spanned);
                if (o != null) {
                    o.clicked = true;
                    if (executeUrl(spanned, o, o.getURL())) {
                        return true;
                    }
                }
            } catch (Exception e) {
                A.error(e);
            }
        }
        if (checkHighlight && isNoteHighlightClick(event)) {
            return true;
        }
        return false;
    }

    private MyUrlSpan getUrlSpan(int start, int end, int offset, Spanned spanned) {
        MyUrlSpan o = null;
        for (MyUrlSpan sp : (MyUrlSpan[]) spanned.getSpans(start, end, MyUrlSpan.class)) {
            if (o == null) {
                o = sp;
            } else {
                if (Math.abs(offset - spanned.getSpanStart(sp)) < Math.abs(offset - spanned.getSpanStart(o))) {
                    o = sp;
                }
            }
        }
        return o;
    }

    private boolean executeUrl(Spanned spanned, Object o, String url) {
        if (T.isNull(url)) {
            return false;
        }
        if (A.ebook != null && url.startsWith("***")) {
            this.footnoteStr = url.substring(3);
            showEpub3Footnote();
            return true;
        } else if (A.ebook != null && url.startsWith("?")) {
            return dealFb2Link(url);
        } else {
            if (url.startsWith("@@")) {
                return false;
            }
            openUrlLink(url, spanned.subSequence(spanned.getSpanStart(o), spanned.getSpanEnd(o)).toString().trim(), 50);
            return true;
        }
    }

    private boolean isTableZoomIcon(MRTextView tv, Spanned spanStr, int line, float hx) {
        int i1 = line > 0 ? line - 1 : 0;
        int i2;
        if (line < tv.getRealLineCount() - 1) {
            i2 = line + 1;
        } else {
            i2 = line;
        }
        for (MyTableSpan sp : (MyTableSpan[]) spanStr.getSpans(this.txtView.getLayout().getLineStart(i1), this.txtView.getLayout().getLineVisibleEnd(i2), MyTableSpan.class)) {
            if (sp.html != null && sp.renderTime > 0) {
                int tl = this.txtView.getLayout().getLineForOffset(sp.spStart);
                if ((line == tl || line == tl - 1) && hx > sp.r - ((float) A.d(48.0f)) && hx < sp.r) {
                    new PrefHtmlViewer(this, sp.html).show();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dealFb2Link(String url) {
        if (url.startsWith("??")) {
            String tag = url.substring(2, url.length());
            try {
                int i = A.ebook.getChapters().size() - 1;
                while (i >= 0) {
                    String html = A.ebook.getChapterText(i);
                    int j = html.indexOf("id=\"" + tag + "\"");
                    if (j != -1) {
                        j = html.lastIndexOf("<", j);
                    }
                    if (j != -1) {
                        int p = MyHtml.fromHtml(html.substring(0, j), this.imageGetter, A.lastChapter).toString().length();
                        saveLinkBackInfo(true);
                        saveChapterTextToPriorCache(A.lastChapter, A.lastSplitIndex, this.htmlSrc);
                        boolean same = A.lastChapter == i;
                        A.lastChapter = i;
                        A.lastPosition = (long) p;
                        if (same) {
                            MyLayout lo = this.txtView.getLayout();
                            txtScrollTo(lo.getLineTop(lo.getLineForOffset((int) A.lastPosition)));
                        } else {
                            showEBookByPosition(A.lastChapter, 0, A.lastPosition, true);
                        }
                        linkBackSetVisible();
                        resetFlipCache(false, 500);
                        hideProgressDlg();
                    }
                    i--;
                }
            } catch (OutOfMemoryError e) {
                A.error(e);
            } catch (Exception e2) {
                A.error(e2);
            }
            return true;
        }
        FootNote fn = A.ebook.getFootNote(url.substring(1, url.length()));
        getTouchTxtView().invalidate();
        if (fn == null) {
            return false;
        }
        showFB2Footnote(fn);
        return true;
    }

    private void doMobiLink(String url, String title) {
        url = url.substring(5);
        while (true) {
            if (url.startsWith("0")) {
                url = url.substring(1);
            } else {
                try {
                    break;
                } catch (Exception e) {
                    A.error(e);
                    return;
                }
            }
        }
        int p = Integer.valueOf(url).intValue();
        int count = 0;
        int chapterSize = A.ebook.getChapters().size();
        int i = 0;
        while (i < chapterSize) {
            count = (int) (((long) count) + ((Chapter) A.ebook.getChapters().get(i)).size);
            if (count >= p) {
                int cIndex = A.lastChapter;
                int splitIndex = A.lastSplitIndex;
                int tmpPosition = 0;
                if (count != p) {
                    String s;
                    boolean done = false;
                    if (p > count - 200 && i < chapterSize - 1) {
                        s = A.ebook.getChapterText(i);
                        if (A.chapterEndPrompt) {
                            if (s.indexOf(BaseEBook.CHAPTER_END_HTMLHINT1) != -1) {
                                s = s.substring(0, s.indexOf(BaseEBook.CHAPTER_END_HTMLHINT1));
                            }
                        }
                        int off = p - (count - s.length());
                        if (s.length() > off) {
                            s = s.substring(off);
                            int j = s.indexOf(">");
                            int k = s.indexOf("<");
                            if ((j == -1 || k != -1) && (j != -1 || k == -1)) {
                                if (k > j) {
                                    s = s.substring(k);
                                }
                                if (Html.fromHtml(s).toString().trim().length() == 0) {
                                    done = true;
                                }
                            } else {
                                done = true;
                            }
                        }
                    }
                    if (done) {
                        cIndex = i + 1;
                        splitIndex = 0;
                        tmpPosition = 0;
                    } else {
                        s = A.ebook.getChapterText(i);
                        cIndex = i;
                        int t = (int) ((((long) s.length()) * ((long) ((int) (((Chapter) A.ebook.getChapters().get(i)).size - ((long) (count - p)))))) / ((Chapter) A.ebook.getChapters().get(i)).size);
                        if (t < A.maxHtmlChapterSize) {
                            splitIndex = 0;
                        } else {
                            ArrayList<String> htmls = new ArrayList();
                            A.createSplitHtmls(A.ebook.getChapterText(i), htmls);
                            long count2 = 0;
                            for (int i2 = 0; i2 < htmls.size(); i2++) {
                                count2 += (long) ((String) htmls.get(i2)).length();
                                if (count2 > ((long) t)) {
                                    splitIndex = i2;
                                    s = (String) htmls.get(i2);
                                    t -= (int) (count2 - ((long) s.length()));
                                    break;
                                }
                            }
                        }
                        title = title.trim();
                        int t2 = s.lastIndexOf(title, t);
                        int t3 = s.indexOf(title, t);
                        A.log("---------------t2:" + t2 + " t:" + t + " t3:" + t3 + "  (t-t2):" + (t - t2) + "  (t3-t):" + (t3 - t));
                        if (t3 == -1) {
                            if (t2 != -1 && t - t2 < HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES) {
                                t = t2;
                            }
                        } else if (t3 - t < 150) {
                            t = t3;
                        } else if (t3 > 1000 && t2 != -1 && t - t2 < HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES) {
                            t = t2;
                        }
                        int idPos = s.lastIndexOf("<", t);
                        StringBuilder sb = new StringBuilder();
                        sb.append(s.substring(0, idPos));
                        sb.append("(<a href=\"@@\">#</a>)");
                        sb.append(s.substring(idPos));
                        Spanned spanned = MyHtml.fromHtml(sb.toString(), this.imageGetter, i);
                        for (MyUrlSpan o : (MyUrlSpan[]) spanned.getSpans(0, spanned.length(), MyUrlSpan.class)) {
                            if (o.getURL().equals("@@")) {
                                tmpPosition = spanned.getSpanStart(o);
                                break;
                            }
                        }
                    }
                } else if (i < chapterSize - 1) {
                    cIndex = i + 1;
                    splitIndex = 0;
                    tmpPosition = 0;
                } else {
                    return;
                }
                saveLastPostion(false);
                saveChapterTextToPriorCache(A.lastChapter, A.lastSplitIndex, this.htmlSrc);
                saveLinkBackInfo(true);
                if (A.lastChapter == cIndex && A.lastSplitIndex == splitIndex) {
                    txtScrollTo(this.txtView.getLineTop2(this.txtView.getLayout().getLineForOffset(tmpPosition)));
                } else {
                    showEBookByPosition(cIndex, splitIndex, (long) tmpPosition, true);
                }
                linkBackSetVisible();
                return;
            }
            i++;
        }
    }

    private void showFB2Footnote(FootNote fn) {
        this.footnoteStr = "<h3>" + fn.title + "</h3>" + fn.html;
        showEpub3Footnote();
    }

    private MRTextView getTouchTxtView() {
        return A.touchingView == this.txtScroll2 ? this.txtView2 : this.txtView;
    }

    private void startImageGallery(MyImageSpan gallerySp) {
        if (gallerySp != null) {
            Intent i = new Intent(this, PicGalleryShow.class);
            if (A.ebook != null) {
                i.putExtra("ebookImage", gallerySp.getSource());
            } else {
                i.putExtra("imageFile", T.getFilePath(A.lastFile) + "/" + gallerySp.getSource());
            }
            startActivity(i);
        }
    }

    private void openUrlLink(final String url, final String title, long delay) {
        if (!isEpub3Footnote(url, title)) {
            createProgressDlg(title);
            new Handler() {
                public void handleMessage(Message msg) {
                    try {
                        ActivityTxt.this.getTouchTxtView().invalidate();
                        ActivityTxt.this.openUrlLinkHandler(url, title);
                    } catch (OutOfMemoryError e) {
                        A.error(e);
                    } catch (Exception e2) {
                        A.error(e2);
                    }
                }
            }.sendEmptyMessageDelayed(0, delay);
        }
    }

    private void openUrlLinkHandler(String url, String title) {
        if (!url.startsWith("@mobi") || A.ebook == null) {
            if (url.startsWith("http")) {
                T.openUrl(this, url);
                hideProgressDlg();
                return;
            }
            String aUrl;
            LINK_BACK lb_tag = saveLinkBackInfo(true);
            this.cPos = 0;
            int tmpLastSplitIndex = A.lastSplitIndex;
            int tmpLastChapter = A.lastChapter;
            String tmpHtmlSrc = this.htmlSrc;
            A.logTime = SystemClock.elapsedRealtime();
            A.log("1:");
            ArrayList<String> oldSplitHtmls = (ArrayList) A.splitHtmls.clone();
            ArrayList<String> oldPreNextSplitHtmls = this.preNextSplitHtmls == null ? null : A.splitHtmls == this.preNextSplitHtmls ? oldSplitHtmls : (ArrayList) this.preNextSplitHtmls.clone();
            int tagIndex;
            if (getBookType() == 1) {
                if (url.startsWith("#")) {
                    if (openHtmlFileWithTag(url.substring(1), false)) {
                        linkBackSetVisible();
                        hideProgressDlg();
                        return;
                    }
                }
                tagIndex = url.indexOf("#");
                String newFile = T.getFilePath(A.lastFile) + "/" + (tagIndex == -1 ? url : url.substring(0, tagIndex));
                if (T.isFile(newFile)) {
                    A.lastFile = newFile;
                    if (tagIndex != -1) {
                        this.htmlSrc = T.getFileText(A.lastFile);
                        openHtmlFileWithTag(url.substring(tagIndex + 1), true);
                    } else {
                        this.htmlText = "";
                        this.handler.post(this);
                    }
                    linkBackSetVisible();
                    hideProgressDlg();
                    return;
                }
            }
            int i;
            Chapter c;
            String html;
            ArrayList<String> list;
            int j;
            aUrl = Uri.decode(url);
            int cIndex = -1;
            if (!aUrl.contains("#")) {
                for (i = 0; i < A.ebook.getChapters().size(); i++) {
                    c = (Chapter) A.ebook.getChapters().get(i);
                    if (c.filename != null && (c.filename.equals(aUrl) || c.filename.endsWith("/" + aUrl))) {
                        cIndex = i;
                        break;
                    }
                }
            }
            if (cIndex == -1) {
                if (aUrl.indexOf("./") != -1) {
                    String aUrl2 = aUrl.substring(aUrl.lastIndexOf("/") + 1);
                    for (i = 0; i < A.ebook.getChapters().size(); i++) {
                        c = (Chapter) A.ebook.getChapters().get(i);
                        if (c.filename != null && (c.filename.equals(aUrl2) || c.filename.endsWith("/" + aUrl2))) {
                            cIndex = i;
                            break;
                        }
                    }
                }
            }
            if (cIndex == -1) {
                int idIndex = aUrl.indexOf("#");
                String id_str;
                if (idIndex == 0) {
                    int split = tmpLastSplitIndex;
                    boolean inSplit = false;
                    html = this.htmlSrc;
                    id_str = aUrl.substring(idIndex + 1);
                    String tag1 = "id=\"" + id_str + "\"";
                    String tag12 = "id = \"" + id_str + "\"";
                    String tag2 = "name=\"" + id_str + "\"";
                    String tag22 = "name = \"" + id_str + "\"";
                    int start = (((int) ((((long) html.length()) * A.lastPosition) / ((long) this.txtView.getText().length()))) * 9) / 10;
                    if (A.noSplitHtmls()) {
                        tagIndex = getTagIndexFromHtml(html, tag1, tag12, tag2, tag22, start);
                        if (tagIndex == -1) {
                            tagIndex = getTagIndexFromHtml(html, tag1, tag12, tag2, tag22, 0);
                        }
                    } else {
                        tagIndex = getTagIndexFromHtml(html, tag1, tag12, tag2, tag22, start);
                        if (tagIndex == -1) {
                            for (i = tmpLastSplitIndex + 1; i < A.splitHtmls.size(); i++) {
                                tagIndex = getTagIndexFromHtml((String) A.splitHtmls.get(i), tag1, tag12, tag2, tag22, 0);
                                if (tagIndex != -1) {
                                    split = i;
                                    inSplit = true;
                                    break;
                                }
                            }
                        }
                        if (tagIndex == -1) {
                            for (i = tmpLastSplitIndex; i >= 0; i--) {
                                tagIndex = getTagIndexFromHtml((String) A.splitHtmls.get(i), tag1, tag12, tag2, tag22, 0);
                                if (tagIndex != -1) {
                                    split = i;
                                    inSplit = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (tagIndex == -1) {
                        Chapter c1 = (Chapter) A.ebook.getChapters().get(A.lastChapter);
                        String filename = c1.filename;
                        for (i = 0; i < A.ebook.getChapters().size(); i++) {
                            if (i != A.lastChapter) {
                                Iterator it;
                                Chapter c2 = (Chapter) A.ebook.getChapters().get(i);
                                boolean hasSameFile = c2.filename.equals(filename);
                                if (!hasSameFile && c1.usedFiles.size() > 0) {
                                    it = c1.usedFiles.iterator();
                                    while (it.hasNext()) {
                                        if (c2.filename.equals((String) it.next())) {
                                            hasSameFile = true;
                                            break;
                                        }
                                    }
                                }
                                if (!hasSameFile && c2.usedFiles.size() > 1) {
                                    it = c2.usedFiles.iterator();
                                    while (it.hasNext()) {
                                        String s = (String) it.next();
                                        if (!s.equals(filename)) {
                                            if (!s.endsWith("/" + filename)) {
                                                if (s.indexOf("#") <= 0 || !T.getFilename(s).startsWith(T.getFilename(filename) + "#")) {
                                                }
                                            }
                                        }
                                        hasSameFile = true;
                                    }
                                }
                                if (hasSameFile) {
                                    list = new ArrayList();
                                    String src = A.adjustChapterHtml(A.ebook.getChapterText(i), list);
                                    if (list.size() == 0) {
                                        tagIndex = getTagIndexFromHtml(src, tag1, tag12, tag2, tag22, 0);
                                    } else {
                                        for (j = 0; j < list.size(); j++) {
                                            src = (String) list.get(j);
                                            tagIndex = getTagIndexFromHtml(src, tag1, tag12, tag2, tag22, 0);
                                            if (tagIndex != -1) {
                                                split = j;
                                                break;
                                            }
                                        }
                                    }
                                    if (tagIndex != -1) {
                                        html = src;
                                        A.lastChapter = i;
                                        break;
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                    if (tagIndex != -1) {
                        getCIndex(0, null, id_str, aUrl, null, !inSplit ? html : (String) A.splitHtmls.get(split), tagIndex);
                        cIndex = A.lastChapter;
                        A.lastSplitIndex = split;
                    }
                } else if (idIndex != -1) {
                    String file = aUrl.substring(0, idIndex);
                    id_str = aUrl.substring(idIndex + 1);
                    for (i = 0; i < A.ebook.getChapters().size(); i++) {
                        cIndex = existTag(aUrl, file, id_str, i);
                        if (cIndex != -1) {
                            break;
                        }
                    }
                    if (cIndex == -1 && !T.getFilename(file).equals(file)) {
                        file = T.getFilename(file);
                        for (i = 0; i < A.ebook.getChapters().size(); i++) {
                            cIndex = existTag(aUrl, file, id_str, i);
                            if (cIndex != -1) {
                                break;
                            }
                        }
                    }
                }
            }
            if (cIndex == -1) {
                i = 0;
                while (i < A.ebook.getChapters().size()) {
                    if (inUsedFiles(aUrl, (Chapter) A.ebook.getChapters().get(i))) {
                        cIndex = i;
                        String fileTag = "<a name=" + T.getFilename(aUrl) + ">";
                        list = new ArrayList();
                        if (i == tmpLastChapter) {
                            list = A.splitHtmls;
                            html = list.size() > 0 ? (String) list.get(0) : this.htmlSrc;
                        } else {
                            html = A.adjustChapterHtml(A.ebook.getChapterText(i), list);
                        }
                        int k;
                        if (list.size() == 0) {
                            k = html.indexOf(fileTag);
                            if (k != -1) {
                                this.cPos = MyHtml.fromHtml(html.substring(0, k), this.imageGetter, i).length() + 1;
                            }
                        } else {
                            for (j = 0; j < list.size(); j++) {
                                html = (String) list.get(j);
                                k = html.indexOf(fileTag);
                                if (k != -1) {
                                    this.cPos = MyHtml.fromHtml(html.substring(0, k), this.imageGetter, i).length() + 1;
                                    A.lastSplitIndex = j;
                                    break;
                                }
                            }
                        }
                    } else {
                        i++;
                    }
                }
            }
            if (cIndex >= 0) {
                if (this.urlFootnote != null) {
                    A.lastChapter = tmpLastChapter;
                    A.lastSplitIndex = tmpLastSplitIndex;
                    A.splitHtmls = oldSplitHtmls;
                    this.htmlSrc = tmpHtmlSrc;
                    this.preNextSplitHtmls = oldPreNextSplitHtmls;
                    hideProgressDlg();
                    this.link_backs.remove(lb_tag);
                    showUrlFootnote(url, title, cIndex);
                    return;
                }
                saveLastPostion(false);
                A.splitHtmls = oldSplitHtmls;
                saveChapterTextToPriorCache(A.lastChapter, tmpLastSplitIndex, tmpHtmlSrc);
                boolean same = tmpLastChapter == cIndex && tmpLastSplitIndex == A.lastSplitIndex;
                A.lastChapter = cIndex;
                A.lastPosition = (long) this.cPos;
                if (A.lastPosition == 0) {
                    this.ebookChapterDown = true;
                    this.handler.post(this);
                } else if (same) {
                    txtScrollTo(this.txtView.getLineTop2(this.txtView.getLayout().getLineForOffset((int) A.lastPosition)));
                } else {
                    showEBookByPosition(A.lastChapter, A.lastSplitIndex, A.lastPosition, true);
                }
                linkBackSetVisible();
                hideProgressDlg();
                updateProgressStatus();
                A.log("3:");
                return;
            }
            if (A.ebook != null) {
                aUrl = Uri.decode(url);
                if (aUrl.indexOf("#") != -1) {
                    aUrl = aUrl.substring(0, aUrl.indexOf("#"));
                }
                String singleFileText = A.ebook.getSingleFileText(aUrl);
                if (singleFileText != null && singleFileText.length() > 0) {
                    linkBackSetVisible();
                    this.htmlSrc = T.deleteHtmlStyle(T.getHtmlBody(singleFileText));
                    this.htmlSrc = A.adjustChapterHtml(this.htmlSrc);
                    this.htmlSrc = A.chineseJianFanConvert(this.htmlSrc);
                    txtViewSetText(MyHtml.fromHtml(this.htmlSrc, this.imageGetter, A.lastChapter));
                    txtScrollByDelay(this.cPos);
                    hideProgressDlg();
                    return;
                }
            }
            A.lastSplitIndex = tmpLastSplitIndex;
            hideProgressDlg();
            T.showToastText(this, "\"" + url + "\" " + getString(R.string.link_not_found));
            return;
        }
        doMobiLink(url, title);
        hideProgressDlg();
    }

    private void showUrlFootnote(final String url, final String title, int cIndex) {
        Builder dlg =new AlertDialog.Builder(this);
        this.handler.sendEmptyMessageDelayed(GPU_SHADOW, 50);
        dlg.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                A.setSystemUiVisibility(true);
                ActivityTxt.this.eraseGPUShadow(0);
            }
        });
        TextView tv = new TextView(this);
        tv.setTextSize(A.fontSize > 19.0f ? A.fontSize - 2.0f : A.fontSize);
        tv.setPadding(A.d(4.0f), A.d(4.0f), A.d(4.0f), A.d(4.0f));
        tv.setText(Html.fromHtml(this.urlFootnote, A.ebook.getImageGetter(), null));
        if (title.length() > 1 || (title.length() == 1 && title.charAt(0) != '￼')) {
            dlg.setTitle((CharSequence) title);
        }
        dlg.setView(tv).setPositiveButton((int) R.string.open_note, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityTxt.this.checkUrlFootnote = false;
                ActivityTxt.this.urlFootnote = null;
                new Handler() {
                    public void handleMessage(Message msg) {
                        ActivityTxt.this.openUrlLink(url, title, 50);
                    }
                }.sendEmptyMessageDelayed(0, 50);
            }
        }).setNegativeButton((int) R.string.cancel, null).setCancelable(true).show();
    }

    private int getTagIndexFromHtml(String html, String tag1, String tag12, String tag2, String tag22, int start) {
        int result = html.indexOf(tag1, start);
        if (result == -1) {
            result = html.indexOf(tag12, start);
        }
        if (result == -1) {
            result = html.indexOf(tag2, start);
        }
        if (result == -1) {
            return html.indexOf(tag22, start);
        }
        return result;
    }

    public void linkBackSetVisible() {
        this.linkBackIv.setImageResource(R.drawable.linkback_button);
        this.linkBackIv.setVisibility(0);
    }

    private int existTag(String aUrl, String file, String id_str, int i) {
        Chapter c = (Chapter) A.ebook.getChapters().get(i);
        if (!file.equals("") && (c.filename == null || (!c.filename.equals(file) && !c.filename.endsWith("/" + file) && !inUsedFiles(file, c)))) {
            return -1;
        }
        return getCIndex(i, T.getFilename(file), id_str, aUrl, c, null, 0);
    }

    private boolean inUsedFiles(String file, Chapter c) {
        String name = T.getFilename(file);
        Iterator it = c.usedFiles.iterator();
        while (it.hasNext()) {
            String usedFile = (String) it.next();
            if (!usedFile.equals(name)) {
                if (usedFile.endsWith("/" + name)) {
                }
            }
            return true;
        }
        return false;
    }

    private int getCIndex(int i, String file, String id_str, String aUrl, Chapter c, String htmlContent, int tagIndex) {
        String html;
        int cIndex = -1;
        int tmpCIndex = i;
        String idStr = "id=\"" + id_str + "\"";
        boolean forceReturnFileHtml = false;
        if (htmlContent == null) {
            String chapterText = A.ebook.getChapterText(tmpCIndex).replace("\n\"" + id_str, "\"" + id_str);
            if (c.additionalText.equals(BaseEBook.ADITIONAL_ERROR_TAG)) {
                html = chapterText;
                forceReturnFileHtml = true;
            } else {
                html = getTagHtml(chapterText, file, idStr);
                if (html == null) {
                    idStr = "name=\"" + id_str + "\"";
                    html = getTagHtml(chapterText, file, idStr);
                }
                if (html == null && i > 0 && !((Chapter) A.ebook.getChapters().get(i - 1)).filename.equals(c.filename)) {
                    tmpCIndex--;
                    html = getTagHtml(chapterText, file, idStr);
                    if (html == null) {
                        idStr = "id=\"" + id_str + "\"";
                        html = getTagHtml(chapterText, file, idStr);
                    }
                }
            }
        } else {
            html = htmlContent.replace("\n\"" + id_str, "\"" + id_str);
        }
        if (html != null) {
            int idPos = -1;
            if (forceReturnFileHtml) {
                html = A.ebook.getSingleFileText(file);
                idPos = html.indexOf(idStr);
                this.cPos = 0;
                if (idPos == -1) {
                    return -100;
                }
            } else if (htmlContent == null) {
                int fileIndex = html.indexOf("<a name=" + file + ">");
                if (fileIndex != -1) {
                    idPos = html.indexOf(idStr, fileIndex);
                }
                if (idPos == -1) {
                    idPos = html.indexOf(idStr);
                }
            } else {
                idPos = tagIndex;
            }
            if (this.checkUrlFootnote && isUrlLikeFootnote(html, idStr, idPos)) {
                return i;
            }
            cIndex = i;
            if (tmpCIndex == i) {
                try {
                    String substring = html.substring(0, html.lastIndexOf("<", idPos));
                    MyImageGetter myImageGetter = this.imageGetter;
                    if (htmlContent != null) {
                        i = A.lastChapter;
                    }
                    this.cPos = MyHtml.fromHtml(substring, myImageGetter, i).length() + 1;
                    if (forceReturnFileHtml) {
                        return -100;
                    }
                } catch (Throwable e) {
                    A.error(e);
                }
            }
        }
        return cIndex;
    }

    private boolean isEpub3Footnote(String url, String title) {
        if (!url.startsWith("#") || this.htmlSrc.indexOf("\"noteref\"") == -1) {
            return false;
        }
        int i;
        int i1;
        int i2;
        this.footnoteStr = null;
        String html = this.htmlSrc;
        if (!A.noSplitHtmls() && A.lastSplitIndex < A.splitHtmls.size() - 1) {
            try {
                for (i = A.lastSplitIndex + 1; i < A.splitHtmls.size(); i++) {
                    html = html + ((String) A.splitHtmls.get(i));
                }
            } catch (OutOfMemoryError e) {
                A.error(e);
            }
        }
        int start = 0;
        while (true) {
            i = html.indexOf(" href=\"" + url + "\"", start);
            if (i == -1) {
                i = html.indexOf(" href = \"" + url + "\"", start);
            }
            if (i == -1) {
                break;
            }
            i1 = html.lastIndexOf("<", i);
            i2 = html.indexOf(">", i);
            if (!(i1 == -1 || i2 == -1 || i2 - i1 >= 200 || html.substring(i1, i2).indexOf("noteref") == -1)) {
                int i3 = html.indexOf(" id=\"" + url.substring(1) + "\"", i2);
                if (i3 == -1) {
                    i3 = html.indexOf(" id = \"" + url.substring(1) + "\"", i2);
                }
                if (i3 == -1) {
                    i3 = html.indexOf(" id=\"" + url.substring(1) + "\"", start);
                }
                if (i3 != -1 && i3 - html.lastIndexOf("<aside ", i3) < 1000) {
                    int exact = html.substring(i2 + 1, i2 + 100).replace(' ', ' ').indexOf(title.replace(' ', ' '));
                    i2 = html.indexOf("</aside>", i3);
                    if (i2 != -1) {
                        int j = url.length() - 1;
                        while (j > 0 && T.charIsNumber(url.charAt(j))) {
                            j--;
                        }
                        int j2 = html.indexOf(" id=\"" + url.substring(1, j + 1), i3 + 10);
                        if (!(j2 == -1 || j2 >= i2 || j2 == html.indexOf(" id=\"" + url.substring(1), i3 + 10))) {
                            int j3 = html.lastIndexOf("<", j2);
                            if (j3 > i3) {
                                i2 = j3;
                            }
                        }
                        i1 = html.indexOf(">", i3);
                        if (this.footnoteStr == null) {
                            this.footnoteStr = html.substring(i1 + 1, i2);
                        }
                        start = i2;
                        if (exact != -1 && exact < 10) {
                            break;
                        }
                    }
                }
            }
            if (start < i) {
                start = i + 100;
            }
        }
        this.footnoteStr = html.substring(i1 + 1, i2);
        if (this.footnoteStr == null) {
            return false;
        }
        hideProgressDlg();
        this.footnoteStr = this.footnoteStr.trim().replace("‏^", "^");
        this.footnoteStr = (!"￼".equals(title) ? "<h3>" + title + "</h3>" : "") + this.footnoteStr;
        showEpub3Footnote();
        return true;
    }

    private void showEpub3Footnote() {
        getTouchTxtView().invalidate();
        int c;
        if (this.footnoteStr.indexOf("<ruby") != -1) {
            String css;
            WebView wb = new WebView(this);
            c = A.backgroundColor;
            if (A.useBackgroundImage && A.savedBackgroundDrwable != null) {
                c = T.getDrawableAboutColor(A.savedBackgroundDrwable);
            }
            String background = "background-color:" + T.color2Html(c) + ";";
            if (A.isNightState()) {
                css = MRBookView.CSS.replace("%BACKGROUND", background).replace("%COLOR", T.color2Html(A.fontColor)).replace("%LEFT", "20").replace("%RIGHT", "0").replace("%TOP", "0").replace("%BOTTOM", "0");
            } else {
                css = "";
            }
            wb.loadDataWithBaseURL("", "<html>" + css + "<body>" + this.footnoteStr + "<body></html>", "text/html", "UTF-8", null);
           new AlertDialog.Builder(this).setView(wb).setPositiveButton(17039370, null).show();
            return;
        }
        CharSequence text = A.deleteLineBreaks(Html.fromHtml(this.footnoteStr, A.ebook.getImageGetter(), null));
        View tv = new TextView(this);
        tv.setId(R.id.childId);
        tv.setText(text);
        tv.setTextSize(A.fontSize);
        tv.setPadding(A.d(28.0f), A.d(28.0f), A.d(28.0f), A.d(28.0f));
        View sv = new ScrollView(this);
        sv.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        A.setTextViewWithTheme(tv);
        if (!A.useBackgroundImage || A.savedBackgroundDrwable == null) {
            A.setBackgroundImage(tv);
            A.setBackgroundImage(sv);
        } else {
            c = T.getDrawableAboutColor(A.savedBackgroundDrwable);
            tv.setBackgroundColor(c);
            sv.setBackgroundColor(c);
        }
        sv.addView(tv, new ViewGroup.LayoutParams(-1, -2));
        this.handler.sendEmptyMessageDelayed(GPU_SHADOW, 50);
        this.footnoteDlg = new Dialog(this, R.style.dialog_fullscreen);
        this.footnoteDlg.setContentView(sv);
        this.footnoteDlg.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                A.setSystemUiVisibility(true);
                ActivityTxt.this.eraseGPUShadow(0);
            }
        });
        this.footnoteDlg.show();
        if (A.isMultiWindow()) {
            sv.setVerticalScrollBarEnabled(false);
        }
        sv.setOnTouchListener(new OnTouchListener() {
            long downTime;
            float downX;
            float downY;

            public boolean onTouch(View v, MotionEvent event) {
                try {
                    A.log("*action:" + event.getAction());
                    ScrollView sv = (ScrollView) v;
                    if (sv.getScrollY() > 0) {
                        ActivityTxt.this.eraseGPUShadow(0);
                    }
                    if (event.getAction() == 0) {
                        this.downTime = SystemClock.elapsedRealtime();
                        this.downX = event.getX();
                        this.downY = event.getY();
                        if (A.isMultiWindow()) {
                            ActivityTxt.this.eraseGPUShadow(2000);
                        }
                        ActivityTxt.this.footnoteLongTimeTapEvent = true;
                        ActivityTxt.this.handler.removeMessages(502);
                        ActivityTxt.this.handler.sendMessageDelayed(ActivityTxt.this.handler.obtainMessage(502, 1, 1), 400);
                    }
                    if (event.getAction() == 2 && (Math.abs(event.getY() - this.downY) > ((float) A.d(10.0f)) || Math.abs(ActivityTxt.this.getTouchX(event) - this.downX) > ((float) A.d(10.0f)))) {
                        ActivityTxt.this.footnoteLongTimeTapEvent = false;
                    }
                    if (event.getAction() == 1) {
                        ActivityTxt.this.handler.sendEmptyMessageDelayed(ActivityTxt.GPU_SHADOW, 3000);
                        if (SystemClock.elapsedRealtime() - this.downTime > 400) {
                            return false;
                        }
                        ActivityTxt.this.footnoteLongTimeTapEvent = false;
                        TextView t = (TextView) v.findViewById(R.id.childId);
                        Layout lo = t.getLayout();
                        int hx = ((int) event.getX()) - t.getPaddingLeft();
                        int line = lo.getLineForVertical((((int) event.getY()) + sv.getScrollY()) - t.getPaddingTop());
                        int offset = lo.getOffsetForHorizontal(line, (float) hx);
                        int j1 = lo.getLineStart(line);
                        int j2 = lo.getLineEnd(line);
                        if (offset >= j1 && offset < j2 && j1 != j2) {
                            Spanned spanStr = (Spanned) t.getText();
                            if (j1 < offset) {
                                j1 = offset - 1;
                            }
                            if (j2 > offset) {
                                j2 = offset + 1;
                            }
                            Object[] spans = spanStr.getSpans(j1, j2, Object.class);
                            int length = spans.length;
                            int i = 0;
                            while (i < length) {
                                Object o = spans[i];
                                if ((o instanceof URLSpan) || (o instanceof MyUrlSpan)) {
                                    String url = o instanceof URLSpan ? ((URLSpan) o).getURL() : ((MyUrlSpan) o).getURL();
                                    String title = "";
                                    int i2 = ActivityTxt.this.footnoteStr.indexOf("<a href=\"" + url + "\">");
                                    if (i2 != -1) {
                                        i2 = ActivityTxt.this.footnoteStr.indexOf(">", i2);
                                        title = ActivityTxt.this.footnoteStr.substring(i2 + 1, ActivityTxt.this.footnoteStr.indexOf("<", i2 + 1));
                                    }
                                    A.log("title:#" + title + "#url:" + url);
                                    if (ActivityTxt.this.footnoteDlg != null) {
                                        ActivityTxt.this.footnoteDlg.dismiss();
                                    }
                                    ActivityTxt.this.footnoteDlg = null;
                                    if (ActivityTxt.this.footnoteDlg2 != null) {
                                        ActivityTxt.this.footnoteDlg2.dismiss();
                                    }
                                    ActivityTxt.this.footnoteDlg2 = null;
                                    if (url.startsWith("http:") || url.startsWith("https:")) {
                                        T.openUrl(ActivityTxt.this, url);
                                        A.log("text click: " + t.getText().toString().substring(j1, j2) + "#" + t.getText().toString().substring(offset - 4, offset + 4));
                                    } else {
                                        if (url.startsWith("#")) {
                                            ActivityTxt.this.eraseGPUShadow(50);
                                        }
                                        if (url.startsWith("?") && A.ebook != null) {
                                            FootNote fn = A.ebook.getFootNote(url.substring(1, url.length()));
                                            if (fn != null) {
                                                ActivityTxt.this.showFB2Footnote(fn);
                                                return false;
                                            }
                                        }
                                        ActivityTxt.this.openUrlLink(url, title, 50);
                                        A.log("text click: " + t.getText().toString().substring(j1, j2) + "#" + t.getText().toString().substring(offset - 4, offset + 4));
                                    }
                                } else {
                                    i++;
                                }
                            }
                            A.log("text click: " + t.getText().toString().substring(j1, j2) + "#" + t.getText().toString().substring(offset - 4, offset + 4));
                        }
                    }
                } catch (Exception e) {
                    A.error(e);
                }
                return false;
            }
        });
    }

    protected void copyFootnoteToClipboard(Message msg) {
        String s = msg.arg1 == 1 ? this.footnoteStr : this.urlFootnote;
        if (msg.obj != null && (msg.obj instanceof FootNote)) {
            s = ((FootNote) msg.obj).html;
        }
        if (s != null) {
            if (s.startsWith("<h3>")) {
                s = s.substring(s.indexOf("</h3>") + 5);
            }
            s = Html.fromHtml(s).toString();
            while (s.indexOf("\n\n") != -1) {
                s = s.replace("\n\n", "\n");
            }
            while (s.length() > 0 && s.charAt(0) == '\n') {
                s = s.substring(1);
            }
            while (s.length() > 0 && s.charAt(s.length() - 1) == '\n') {
                s = s.substring(0, s.length() - 1);
            }
            copyToClipboard(s);
        }
    }

    private boolean isUrlLikeFootnote(String html, String idStr, int idPos) {
        this.urlFootnote = null;
        if (html == null || idStr == null) {
            return false;
        }
        String idStr2;
        int i;
        if (idStr.endsWith("\"")) {
            idStr2 = idStr.substring(0, idStr.length() - 1);
        } else {
            idStr2 = idStr;
        }
        String tag = null;
        for (i = idStr2.length() - 1; i > 3; i--) {
            char c = idStr2.charAt(i);
            if (i == idStr2.length() - 1) {
                if (!T.charIsNumber(c)) {
                    return false;
                }
            } else if (!T.charIsNumber(c)) {
                tag = idStr2.substring(0, i + 1);
                break;
            }
        }
        if (tag != null) {
            boolean ok = true;
            if (!this.isFootnoteEpub) {
                int count = 0;
                int start = 0;
                while (true) {
                    i = html.indexOf(tag, start);
                    if (i == -1) {
                        break;
                    }
                    count++;
                    if (count == 2) {
                        break;
                    }
                    start = i + tag.length();
                }
                ok = count >= 2;
            }
            int i1 = html.indexOf(idStr, idPos);
            if (i1 == -1) {
                return false;
            }
            if (!ok && html.length() - i1 > ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED) {
                return false;
            }
            int i3;
            int i2 = html.indexOf(tag, idStr.length() + i1);
            if (i2 == -1) {
                i2 = html.length();
            }
            if (i2 - i1 > 2000) {
                i3 = html.indexOf("id=\"", idStr.length() + i1);
                if (i3 != -1 && i3 < i2) {
                    i2 = i3;
                }
            }
            if (i2 - i1 < (this.isFootnoteEpub ? 10000 : 2000)) {
                i1 = html.lastIndexOf("<", i1);
                c = i1 == 0 ? ' ' : html.charAt(i1 - 1);
                if (c == '(' || c == '{' || c == '[') {
                    i1--;
                } else {
                    i3 = html.lastIndexOf("(", i1);
                    if (i3 != -1 && i1 - i3 < 50 && T.html2Text(html.substring(i3, i1)).length() < 10) {
                        i1 = i3;
                    }
                }
                if (tag.indexOf("<") == -1) {
                    i2 = html.lastIndexOf("<", i2);
                }
                i3 = html.lastIndexOf("<a href", i2);
                if (i3 > i1 + 10 && i2 - i3 < 100 && T.html2Text(html.substring(i3, i2)).length() < 30 && T.html2Text(html.substring(i1, i3)).length() > 0) {
                    i2 = i3;
                }
                String s = html.substring(i1, i2);
                while (s.length() != 0) {
                    c = s.charAt(s.length() - 1);
                    if (c == ' ' || c == '—' || c == '(' || c == '{' || c == '[') {
                        s = s.substring(0, s.length() - 1);
                    } else {
                        i3 = s.lastIndexOf("(");
                        if (i3 != -1 && s.length() - i3 < 50 && T.html2Text(s.substring(i3)).length() < 10) {
                            s = s.substring(0, i3);
                        }
                        if (T.html2Text(s).trim().replace(" ", "").length() == 0) {
                            return false;
                        }
                        this.urlFootnote = s;
                        this.isFootnoteEpub = true;
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private String getTagHtml(String html, String file, String idStr) {
        int idPos = -1;
        int fileIndex = html.indexOf("<a name=" + file + ">");
        if (fileIndex != -1) {
            idPos = html.indexOf(idStr, fileIndex);
        }
        if (idPos == -1) {
            idPos = html.indexOf(idStr);
        }
        if (html == null || idStr == null || idPos == -1) {
            return null;
        }
        this.htmlSrc = A.adjustChapterHtml(html);
        if (A.noSplitHtmls()) {
            A.lastSplitIndex = 0;
        } else {
            int i;
            int start = 0;
            int l = 0;
            if (fileIndex != -1) {
                for (i = 0; i < A.splitHtmls.size(); i++) {
                    l += ((String) A.splitHtmls.get(i)).length();
                    if (l > idPos) {
                        start = i;
                        break;
                    }
                }
            }
            for (i = start; i < A.splitHtmls.size(); i++) {
                if (((String) A.splitHtmls.get(i)).indexOf(idStr) != -1) {
                    A.lastSplitIndex = i;
                    return (String) A.splitHtmls.get(i);
                }
            }
        }
        return this.htmlSrc;
    }

    private boolean openHtmlFileWithTag(String tagUrl, boolean reload) {
        if (reload) {
            this.htmlSrc = A.adjustChapterHtml(this.htmlSrc);
        }
        if (!A.noSplitHtmls()) {
            for (int i = 0; i < A.splitHtmls.size(); i++) {
                if (openHtmlFileWithTag2((String) A.splitHtmls.get(i), i, tagUrl)) {
                    return true;
                }
            }
        } else if (openHtmlFileWithTag2(this.htmlSrc, 0, tagUrl)) {
            return true;
        }
        if (reload) {
            txtViewSetText(MyHtml.fromHtml(this.htmlSrc, this.imageGetter, -1));
        }
        return false;
    }

    private boolean openHtmlFileWithTag2(String html, int splitIndex, String tagUrl) {
        String tag = "@@" + tagUrl;
        String tagStr = "(<a href=\"" + tag + "\">#</a>)";
        String tagStr2 = "<a name=\"" + tagUrl + "\"></a>";
        if (html.indexOf(tagStr2) == -1) {
            tagStr2 = "<a name='" + tagUrl + "'></a>";
            if (html.indexOf(tagStr2) == -1) {
                tagStr2 = "<a name=" + tagUrl + "></a>";
                if (html.indexOf(tagStr2) == -1) {
                    tagStr2 = "<a name=\"" + tagUrl + "\">";
                    if (html.indexOf(tagStr2) == -1) {
                        tagStr2 = "<a name=" + tagUrl + ">";
                        if (html.indexOf(tagStr2) == -1) {
                            tagStr2 = "<a id='" + tagUrl + "'></a>";
                            if (html.indexOf(tagStr2) == -1) {
                                tagStr2 = "<a id=\"" + tagUrl + "\"></a>";
                                if (html.indexOf(tagStr2) == -1) {
                                    tagStr2 = "<a id=" + tagUrl + "></a>";
                                    if (html.indexOf(tagStr2) == -1) {
                                        tagStr2 = "<a id=\"" + tagUrl + "\">";
                                        if (html.indexOf(tagStr2) == -1) {
                                            tagStr2 = "<a id=" + tagUrl + ">";
                                            if (html.indexOf(tagStr2) == -1) {
                                                return false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        html = html.replace(tagStr2, tagStr);
        Spanned spanned = MyHtml.fromHtml(html, this.imageGetter, -1);
        for (MyUrlSpan o : (MyUrlSpan[]) spanned.getSpans(0, spanned.length(), MyUrlSpan.class)) {
            if (o.getURL().equals(tag)) {
                this.htmlSrc = html;
                txtViewSetText(spanned);
                A.lastSplitIndex = splitIndex;
                txtFileFinalScrollAtStartup((long) spanned.getSpanStart(o));
                return true;
            }
        }
        return false;
    }

    private boolean isNoteHighlightClick(MotionEvent event) {
        if (isPdf()) {
            return false;
        }
        if (A.getHighlights().size() == 0) {
            return false;
        }
        if (getTouchX(event) > ((float) (this.contentLay.getWidth() - this.txtScroll.getPaddingRight()))) {
            return false;
        }
        ScrollView sv;
        if (A.touchingView == this.txtScroll2) {
            sv = this.txtScroll2;
        } else {
            sv = this.txtScroll;
        }
        MRTextView tv = getTouchTxtView();
        float hx = event.getX() - ((float) sv.getPaddingLeft());
        int hy = (((int) event.getY()) + sv.getScrollY()) - sv.getPaddingTop();
        MyLayout lo = tv.getLayout();
        if (lo == null) {
            return false;
        }
        int offset = tv.getLineOffset(lo.getLineForVertical(hy), hx);
        if (offset == -1) {
            return false;
        }
        if (offset < tv.getText().length() && tv.getText().charAt(offset) == '\n') {
            return false;
        }
        long A1 = (long) offset;
        if (getBookType() == 0) {
            A1 = A.getTxtRealPos((long) offset);
            if (A1 == -1) {
                return false;
            }
        }
        this.preNoteInfo = A.getPreHighlight(offset);
        if (this.preNoteInfo == null) {
            return false;
        }
        if (A.touchingView == this.txtScroll2 && this.txtView2.getVisibility() != 0) {
            return false;
        }
        if (this.preNoteInfo.note.length() > 0) {
            do_edit_note(this.preNoteInfo);
        } else {
            int start = (int) (getBookType() == 0 ? ((long) offset) - (A1 - this.preNoteInfo.lastPosition) : this.preNoteInfo.lastPosition);
            highlightText(true, start, this.preNoteInfo.highlightLength + start);
        }
        return true;
    }

    private boolean do_Gesture_Event(MotionEvent event) {
        if (this.web != null) {
            return false;
        }
        if (this.dot1 != null && this.dot1.getVisibility() == 0) {
            return false;
        }
        int w = A.getScreenWidth2();
        int destLength = w / 2 < DropboxServerException._400_BAD_REQUEST ? w / 2 : DropboxServerException._400_BAD_REQUEST;
        if (Math.abs(event.getY() - this.pressDownY) < 120.0f) {
            if (getTouchX(event) - this.pressDownX > ((float) destLength) && doHorizontalSwipe(true)) {
                return true;
            }
            if (getTouchX(event) - this.pressDownX < ((float) (-destLength)) && doHorizontalSwipe(false)) {
                return true;
            }
        }
        w = this.baseFrame.getHeight();
        destLength = w / 2 < DropboxServerException._400_BAD_REQUEST ? w / 2 : DropboxServerException._400_BAD_REQUEST;
        if (Math.abs(getTouchX(event) - this.pressDownX) < 120.0f) {
            if (event.getY() - this.pressDownY > ((float) destLength)) {
                if (A.doSwipeTopToBottom != 15) {
                    this.xFlingTime = 1;
                    this.swipeGuesture = true;
                }
                if (doEvent(A.doSwipeTopToBottom)) {
                    return true;
                }
            }
            if (event.getY() - this.pressDownY < ((float) (-destLength))) {
                if (A.doSwipeBottomToTop != 15) {
                    this.xFlingTime = 1;
                    this.swipeGuesture = true;
                }
                if (doEvent(A.doSwipeBottomToTop)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r6) {
        /*
        r5 = this;
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r3 = 1;
        r1 = r5.multiTouchForFontSize(r6);	 Catch:{ Exception -> 0x0022 }
        if (r1 == 0) goto L_0x000a;
    L_0x0009:
        return r3;
    L_0x000a:
        r1 = r6.getAction();	 Catch:{ Exception -> 0x0022 }
        switch(r1) {
            case 0: goto L_0x0012;
            case 1: goto L_0x004f;
            case 2: goto L_0x0027;
            default: goto L_0x0011;
        };	 Catch:{ Exception -> 0x0022 }
    L_0x0011:
        goto L_0x0009;
    L_0x0012:
        r1 = r5.getTouchX(r6);	 Catch:{ Exception -> 0x0022 }
        r5.pressDownX2 = r1;	 Catch:{ Exception -> 0x0022 }
        r1 = r6.getY();	 Catch:{ Exception -> 0x0022 }
        r5.pressDownY2 = r1;	 Catch:{ Exception -> 0x0022 }
        r1 = 1;
        r5.pressDown2 = r1;	 Catch:{ Exception -> 0x0022 }
        goto L_0x0009;
    L_0x0022:
        r0 = move-exception;
        com.flyersoft.books.A.error(r0);
        goto L_0x0009;
    L_0x0027:
        r1 = r6.getY();	 Catch:{ Exception -> 0x0022 }
        r2 = r5.pressDownY2;	 Catch:{ Exception -> 0x0022 }
        r1 = r1 - r2;
        r1 = java.lang.Math.abs(r1);	 Catch:{ Exception -> 0x0022 }
        r1 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1));
        if (r1 > 0) goto L_0x0045;
    L_0x0036:
        r1 = r5.getTouchX(r6);	 Catch:{ Exception -> 0x0022 }
        r2 = r5.pressDownX2;	 Catch:{ Exception -> 0x0022 }
        r1 = r1 - r2;
        r1 = java.lang.Math.abs(r1);	 Catch:{ Exception -> 0x0022 }
        r1 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1));
        if (r1 <= 0) goto L_0x0009;
    L_0x0045:
        r1 = 0;
        r5.longTimeTapEvent = r1;	 Catch:{ Exception -> 0x0022 }
        r1 = 0;
        r5.pressDown2 = r1;	 Catch:{ Exception -> 0x0022 }
        r5.hideDotViews();	 Catch:{ Exception -> 0x0022 }
        goto L_0x0009;
    L_0x004f:
        r1 = r5.dotVisible();	 Catch:{ Exception -> 0x0022 }
        if (r1 != 0) goto L_0x0009;
    L_0x0055:
        r1 = r5.pressDown2;	 Catch:{ Exception -> 0x0022 }
        if (r1 == 0) goto L_0x005c;
    L_0x0059:
        r5.do_TapUp_Event(r6);	 Catch:{ Exception -> 0x0022 }
    L_0x005c:
        r1 = 0;
        r5.pressDown2 = r1;	 Catch:{ Exception -> 0x0022 }
        goto L_0x0009;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.flyersoft.moonreader.ActivityTxt.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private void initFling() {
        if (!this.flingInited) {
            this.flingInited = true;
            this.mGestureDetector = new GestureDetector(new MySimpleGesture());
            if (this.mVT == null) {
                this.mVT = VelocityTracker.obtain();
            }
            this.mMaximumVelocity = ViewConfiguration.get(this).getScaledMaximumFlingVelocity();
            this.mDeceleration = (386.0878f * (getResources().getDisplayMetrics().density * 160.0f)) * ViewConfiguration.getScrollFriction();
        }
    }

    private boolean acceptHorizontalFling(VelocityTracker vt, float velocityX, float velocityY) {
        boolean z = false;
        if (!A.allow_scroll_horizontally || this.swipeGuesture) {
            return false;
        }
        float x = Math.abs(this.mVT.getXVelocity());
        float y = Math.abs(this.mVT.getYVelocity());
        if (x == 0.0f) {
            x = Math.abs(velocityX);
        }
        if (y == 0.0f) {
            y = Math.abs(velocityY);
        }
        float xMove = Math.abs(getTouchX(this.hMotionEvent) - this.pressDownX);
        float yMove = Math.abs(this.hMotionEvent.getY() - this.pressDownY);
        if (A.isFlipVerticalShift()) {
            if (isMiddleTap(this.hDownEvent)) {
                return false;
            }
            if (this.pagingByMove) {
                return true;
            }
            if (y <= 600.0f && x <= 600.0f) {
                return false;
            }
            if (xMove <= yMove && xMove < ((float) A.d(40.0f))) {
                this.xFlingTime = SystemClock.elapsedRealtime();
                if (velocityY < 0.0f) {
                    z = true;
                }
                flingPageScroll(z, 0.0f);
            } else if (xMove > yMove && yMove < ((float) A.d(40.0f))) {
                this.xFlingTime = SystemClock.elapsedRealtime();
                if (velocityX < 0.0f) {
                    z = true;
                }
                flingPageScroll(z, 0.0f);
            }
            return true;
        } else if (x <= 600.0f || x <= y) {
            return false;
        } else {
            if (this.pagingByMove) {
                return true;
            }
            if (xMove >= yMove && yMove < ((float) A.d(40.0f))) {
                this.xFlingTime = SystemClock.elapsedRealtime();
                if (velocityX < 0.0f) {
                    z = true;
                }
                flingPageScroll(z, 0.0f);
            }
            return true;
        }
    }

    public void flingPageScroll(boolean pageDown, float tiltSpeed) {
        int i = 1;
        if (this.web == null) {
            this.forceTiltSpeed = tiltSpeed;
            if (tiltSpeed == 0.0f) {
                this.tmpHorizaontalScroll = true;
            }
            if (SystemClock.elapsedRealtime() - this.pageScrollTime > 500) {
                if (!pageDown) {
                    i = -1;
                }
                pageScroll(i);
            }
        } else if (pageDown) {
            webViewPageDown();
        } else {
            webViewPageUp();
        }
    }

    private void do_floatBarButton(View v) {
        if (!disableFunctionsInWebView(v)) {
            if (v == this.backLay) {
                do_back_to_book_stack();
            }
            if (v == this.titleTextView) {
                do_book_info();
            }
            if (v == this.percentView) {
                do_go_percent();
            }
            if (v == this.statusLeftPanel) {
                doEvent(A.statusClickLeft);
            }
            if (v == this.statusMiddle || v == this.statusMiddle21 || v == this.statusMiddle22) {
                doEvent(A.statusClickMiddle);
            }
            if (v == this.statusRight) {
                doEvent(A.statusClickRight);
            }
            if (v == this.b_orientation) {
                if (A.immersive_fullscreen && !dualPageEnabled()) {
                    this.handler.sendEmptyMessageDelayed(CHECK_DUAL_PAGE, 500);
                }
                this.isPressScreenStateButton = true;
                saveLastPostion(true);
                generateNextScreenOrientation();
                if (A.isFlipCurl()) {
                    restartReaderToTxt();
                } else {
                    setScreenOrientation();
                    statusHandlerSendMessage(1, 100);
                    pdfRemoveThumb();
                }
            }
            if (v == this.b_daynight) {
                boolean z;
                boolean oldMainNightTheme = A.mainNightTheme;
                resetFlipCache(true, 500);
                if (!isPdf() || this.pdf.textReflow) {
                    cycleThemes(false);
                    setLeds();
                    if (this.web != null) {
                        reloadWebView();
                    }
                } else {
                    int i;
                    pdfRemoveThumb();
                    if (A.pdf_theme == 0) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    A.pdf_theme = i;
                    pdfUpdateView(true);
                }
                A.mainNightTheme = A.isNightState(false);
                if (oldMainNightTheme != A.mainNightTheme) {
                    z = true;
                } else {
                    z = false;
                }
                A.mainThemeChanged = z;
                setBarVisible(0);
            }
            if (v == this.b_speak) {
                do_speak();
            }
            if (v == this.b_autoscroll) {
                do_AutoScrollAsk();
            }
            if (v == this.b_bookmark) {
                do_bookmark();
            }
            if (v == this.b_chapter) {
                inverseLayoutVisible(true);
                do_show_chapters();
            }
            if (v == this.menuB) {
                showOptionsMenu(true);
            }
            if (v == this.b_search) {
                do_search();
            }
            if (v == this.b_fontsize) {
                do_font_size();
            }
            if (v == this.b_shutdown) {
                terminate_reader();
            }
            if (v == this.b_brightness) {
                do_brightness();
            }
            if (v == this.b_tilt) {
                do_tilt();
            }
            if (v == this.b_option) {
                do_options_menu();
            }
            if (v == this.b_visual) {
                showVisualOptions();
            }
            if (v == this.b_control) {
                showControlOptions();
            }
            if (v == this.b_misc) {
                showMiscOptions();
            }
            if (v == this.linkBackIv) {
                doLinkBack();
            }
            if (v == this.chromeIv) {
                doChromeButton(false);
            }
            if (v == this.pdfPenIv) {
                if (A.isProVersion) {
                    inverseLayoutVisible(true);
                    pdfShowAnnotLay();
                } else {
                    A.showBuyDialog(this, getString(R.string.pro_only));
                }
            }
            if (v == this.nav_return) {
                doLinkBack();
                inverseLayoutVisible(true);
            }
            if (v == this.ttsIv) {
                if (this.tts_panel == null) {
                    initTtsPanel();
                }
                showTtsPanel(true);
            }
            if (v == this.tips_ok) {
                hideTipsPanel();
            }
        }
    }

    private void cycleThemes(boolean saveTempThemeOnly) {
        int dayHash = A.getThemeHash(A.DAY_THEME);
        int nightHash = A.getThemeHash(A.NIGHT_THEME);
        int curHash = A.getCurThemeHash();
        if (!saveTempThemeOnly) {
            if (curHash != dayHash && curHash != nightHash) {
                A.saveTheme(A.TEMP_THEME, true);
                loadTheme(A.NIGHT_THEME, A.loadThemeWithColorOnly);
            } else if (curHash == nightHash) {
                loadTheme(A.DAY_THEME, A.loadThemeWithColorOnly);
            } else if (curHash == dayHash) {
                int tempHash = A.getThemeHash(A.TEMP_THEME);
                if (tempHash == dayHash || tempHash == nightHash) {
                    loadTheme(A.NIGHT_THEME, A.loadThemeWithColorOnly);
                } else {
                    loadTheme(A.TEMP_THEME, A.loadThemeWithColorOnly);
                }
            }
            setFlipViewINVISIBLE(true);
        } else if (curHash != dayHash && curHash != nightHash) {
            A.saveTheme(A.TEMP_THEME, true);
        }
    }

    private void do_tilt() {
        A.tilt_turn_page = !A.tilt_turn_page;
        T.showToastText(this, "\"" + getString(R.string.tilt_turn_page) + "\" " + getString(A.tilt_turn_page ? 17039370 : 17039360));
        if (this.shakeSensorLisener != null) {
            this.shakeSensorLisener.tilt_turn_page = A.tilt_turn_page;
        }
        registerHardwares();
        inverseLayoutVisible(true);
    }

    public LINK_BACK saveLinkBackInfo(boolean addToList) {
        saveLastPostion(false);
        try {
            String title;
            if (isPdf()) {
                if (PDFReader.innerLinkRecord >= 0) {
                    A.lastPosition = (long) PDFReader.innerLinkRecord;
                } else {
                    A.lastPosition = (long) pdfGetCurrPageNo();
                }
                title = getString(R.string.page) + " " + (A.lastPosition + 1);
            } else {
                String bookmark = this.txtView.getText2().substring(this.txtView.getLayout().getLineStart(this.txtView.getLayout().getLineForVertical(this.txtScroll.getScrollY()))).replace("\n", "").replace(A.INDENT_CHAR, ' ').trim() + "...";
                int max = A.isAsiaLanguage ? 15 : 20;
                if (bookmark.length() > max) {
                    bookmark = bookmark.substring(0, max);
                }
                title = "(" + getPercentStr2() + ") " + bookmark;
            }
            Iterator it = this.link_backs.iterator();
            while (it.hasNext()) {
                LINK_BACK item = (LINK_BACK) it.next();
                if (item.title.equals(title)) {
                    this.link_backs.remove(item);
                    break;
                }
            }
            this.tmpPositionInfo = new LINK_BACK(title, A.lastFile, A.lastChapter, A.lastSplitIndex, A.lastPosition);
            if (addToList) {
                this.link_backs.add(0, this.tmpPositionInfo);
                return this.tmpPositionInfo;
            }
        } catch (Throwable e) {
            A.error(e);
        }
        return null;
    }

    private void doLinkBack() {
        if (this.link_backs.size() != 0) {
            LINK_BACK back = (LINK_BACK) this.link_backs.get(0);
            this.link_backs.remove(0);
            this.linkBackIv.setVisibility(4);
            goToBackLink(back);
        }
    }

    private void goToBackLink(LINK_BACK back) {
        if (isPdf()) {
            pdfGotoPage((int) back.backPosition, true);
        } else {
            if (getBookType() == 1 && !A.lastFile.equals(back.backFile)) {
                this.htmlText = "";
            }
            A.lastFile = back.backFile;
            A.lastChapter = back.backChapter;
            A.lastSplitIndex = back.backSplitIndex;
            A.lastPosition = back.backPosition;
            if (!hasPriorChapterText(null)) {
                createProgressDlg("");
                this.handler.post(this);
            }
        }
        updateProgressStatus();
    }

    private boolean navigateBackLinks() {
        if (this.link_backs.size() == 0) {
            return false;
        }
        int i;
        String[] items = new String[(this.link_backs.size() + 2)];
        for (int i2 = 0; i2 < this.link_backs.size(); i2++) {
            items[i2] = ((LINK_BACK) this.link_backs.get(i2)).title;
        }
        items[items.length - 2] = "-";
        items[items.length - 1] = getString(R.string.exit);
        int[] colors = new int[items.length];
        int length = colors.length - 1;
        if (A.isNightState()) {
            i = 1713512994;
        } else {
            i = 1719105399;
        }
        colors[length] = i;
        new MyDialog(this, null, items, null, 0, new MenuItemClick() {
            public void onClick(int which) {
                if (which == ActivityTxt.this.link_backs.size() + 1 && !ActivityTxt.this.forceExitRestart()) {
                    ActivityTxt.this.doFinish();
                }
                if (which < ActivityTxt.this.link_backs.size()) {
                    LINK_BACK back = (LINK_BACK) ActivityTxt.this.link_backs.get(which);
                    ActivityTxt.this.saveLinkBackInfo(true);
                    ActivityTxt.this.hideLinkBackButton();
                    ActivityTxt.this.goToBackLink(back);
                }
            }
        }, colors).show();
        return true;
    }

    public void hideLinkBackButton() {
        if (this.linkBackIv != null && this.linkBackIv.getVisibility() == 0) {
            this.linkBackIv.setVisibility(4);
        }
    }

    private void do_go_percent() {
        final EditText et = new EditText(this);
        s = isPdf() ? "" + (pdfGetCurrPageNo() + 1) : this.topLay.getVisibility() == 0 ? this.percentView.getText().toString() : getPercentStr2();
        et.setText(s);
       new AlertDialog.Builder(this).setTitle(isPdf() ? "1 - " + this.m_doc.GetPageCount() : "0.0% - 100%").setView(et).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityTxt.this.web == null) {
                    try {
                        String s = et.getText().toString().replace("%", "");
                        if (ActivityTxt.this.isPdf()) {
                            int i = Integer.valueOf(s).intValue();
                            if (i > 0 && i <= ActivityTxt.this.m_doc.GetPageCount()) {
                                ActivityTxt.this.pdfGotoPage(i, true);
                            }
                            ActivityTxt.this.updateProgressStatus();
                        } else {
                            float p = Float.valueOf(s).floatValue() * 10.0f;
                            if (p >= 0.0f && p <= 1000.0f) {
                                ActivityTxt.this.showBookByPercent((int) p);
                                ActivityTxt.this.updateProgressStatus();
                            }
                        }
                        ActivityTxt.this.inverseLayoutVisible(true);
                    } catch (Exception e) {
                    }
                }
            }
        }).setNegativeButton(17039360, null).show();
    }

    private ArrayList<TtsLine> getSpeakText() {
        String text;
        getSpeakHandler();
        if (isPdf() && !this.pdf.textReflow) {
            return pdfGetTtsLines();
        }
        MyLayout l = this.txtView.getLayout();
        if (l == null) {
            return null;
        }
        saveLastPostion(true);
        int startLine = l.getLineForVertical(this.txtScroll.getScrollY());
        int endLine = getSpeakPageLastLine(startLine, -1);
        if (A.backgroundTtsOption || !this.isPaused || getBookType() == 0) {
            this.speak_in_background = false;
        } else {
            endLine = getRealLineCount() - 1;
            preNextChapter(false);
            this.speak_in_background = true;
        }
        int i1 = l.getLineStart(startLine);
        int i2 = l.getLineEnd(endLine);
        int t1 = i1;
        int t2 = i2;
        String all = this.txtView.getText2();
        int pre = -1;
        if (i1 < 10) {
            try {
                this.lastEndOfSpeakText = -1;
            } catch (Exception e) {
                T.showToastText(this, BaseEBook.ADITIONAL_ERROR_TAG + A.errorMsg(e), 1);
                A.error(e);
                this.lastEndOfSpeakText = -1;
                this.lastStartOfSpeakText = t1;
                text = all.substring(t1, t2);
                this.speakTextLength = text.replace("\n", "").replace(" ", "").length();
                return A.getTtsLines(text, false, this.speak_in_background, this.lastStartOfSpeakText);
            }
        }
        if (this.lastEndOfSpeakText != -1 && this.lastEndOfSpeakText < i2) {
            pre = i1;
            i1 = this.lastEndOfSpeakText;
            if (this.SPEAK_PUNCT1.indexOf(all.charAt(i1)) != -1) {
                i1++;
            }
        }
        if (i2 < all.length() - 20) {
            while (i2 > i1 + 15 && this.SPEAK_PUNCT1.indexOf(all.charAt(i2 - 1)) == -1) {
                i2--;
            }
        }
        this.lastEndOfSpeakText = i2;
        this.lastStartOfSpeakText = i1;
        text = all.substring(i1, i2);
        if (pre > i1 && all.substring(i1, pre).trim().length() > 0 && !this.isPaused) {
            txtScrollNoDelay(i1);
            int startLine2 = l.getLineForOffset(i1);
            if (startLine2 < startLine) {
                i2 = l.getLineEnd(getSpeakPageLastLine(startLine, startLine2));
                if (i2 < all.length() - 20) {
                    while (i2 > i1 + 15 && this.SPEAK_PUNCT1.indexOf(all.charAt(i2 - 1)) == -1) {
                        i2--;
                    }
                }
                this.lastEndOfSpeakText = i2;
                text = all.substring(i1, i2);
            }
        }
        setFlipViewINVISIBLE(true);
        this.speakTextLength = text.replace("\n", "").replace(" ", "").length();
        return A.getTtsLines(text, false, this.speak_in_background, this.lastStartOfSpeakText);
    }

    private int getSpeakPageLastLine(int startLine, int curLine) {
        int endLine = A.getLastDisplayLine(this.txtScroll, curLine);
        if (endLine >= startLine) {
            return endLine;
        }
        endLine = (getPageLineCount() + startLine) - 1;
        if (endLine > this.txtView.getLineCount() - 1) {
            return this.txtView.getLineCount() - 1;
        }
        return endLine;
    }

    private void setSpeakOnUtteranceComplete() {
        if (this.tts_listener == null) {
            this.tts_listener = new OnUtteranceCompletedListener() {
                public void onUtteranceCompleted(String utteranceId) {
                    ActivityTxt.this.tts_screen_off_pos = -1;
                    if (utteranceId != null) {
                        PDFPage.delForceSel(false);
                        if (!A.isSpeaking) {
                            return;
                        }
                        if (ActivityTxt.this.tts_stopped) {
                            A.log("-----------setSpeakOnUtteranceComplete, tts_stopped=true");
                            return;
                        }
                        ActivityTxt.this.tts_utteranceId = Integer.valueOf(utteranceId).intValue();
                        if (ActivityTxt.this.tts_utteranceId < ActivityTxt.this.speakLines.size() - 1) {
                            TtsLine sl = (TtsLine) ActivityTxt.this.speakLines.get(ActivityTxt.this.tts_utteranceId + 1);
                            if (!(A.backgroundTtsOption || !ActivityTxt.this.isPaused || ActivityTxt.this.isPdf())) {
                                ActivityTxt.this.tts_screen_off_pos = ActivityTxt.this.lastStartOfSpeakText + sl.start;
                            }
                            if (!ActivityTxt.this.isPdf() || (ActivityTxt.this.pdf != null && ActivityTxt.this.pdf.textReflow)) {
                                A.log(ActivityTxt.this.tts_utteranceId + "/" + ActivityTxt.this.speakLines.size() + "->" + sl.s + "##");
                                ActivityTxt.this.highlightText(ActivityTxt.this.lastStartOfSpeakText + sl.start, ActivityTxt.this.lastStartOfSpeakText + sl.end);
                                if (ActivityTxt.this.speak_in_background && !ActivityTxt.this.isPaused && ActivityTxt.this.txtView.hStart >= 0 && ActivityTxt.this.txtView.getLayout() != null) {
                                    int y1 = ActivityTxt.this.txtView.getLineTop3(ActivityTxt.this.txtView.getLayout().getLineForOffset(ActivityTxt.this.txtView.hStart));
                                    int y2 = ActivityTxt.this.txtView.getLineTop3(ActivityTxt.this.txtView.getLayout().getLineForOffset(ActivityTxt.this.txtView.hEnd));
                                    if (y1 < ActivityTxt.this.txtScroll.getScrollY() || y2 > (ActivityTxt.this.txtScroll.getScrollY() + A.getPageHeight()) - ActivityTxt.this.txtView.getLineHeight()) {
                                        ActivityTxt.this.txtScrollByDelay(ActivityTxt.this.txtView.hStart);
                                        ActivityTxt.this.updateProgressStatus();
                                    }
                                }
                                if (ActivityTxt.this.speak_in_background && ActivityTxt.this.isPaused) {
                                    ActivityTxt.this.getSharedPreferences(A.POSITION_FILE, 0).edit().putString(A.lastFile.toLowerCase(), A.lastChapter + "@" + A.lastSplitIndex + "#" + (ActivityTxt.this.lastStartOfSpeakText + sl.start) + ":" + ActivityTxt.this.getPercentStr2()).commit();
                                    return;
                                }
                                return;
                            }
                            ActivityTxt.this.pdf.selectedPara = null;
                            PDFPage.setForceSel(sl.start, sl.end);
                            ActivityTxt.this.pdf.postInvalidate();
                            ActivityTxt.this.getSpeakHandler().sendEmptyMessageDelayed(4, 100);
                        } else if (ActivityTxt.this.speak_in_background) {
                            ActivityTxt.this.tts_screen_off_pos = -1;
                            ActivityTxt.this.handler.sendMessage(ActivityTxt.this.handler.obtainMessage(ActivityTxt.PAGE_DOWN_TO_NEXT_CHAPTER, ActivityTxt.this.txtView.getLineTop2(ActivityTxt.this.txtView.getLineCount()), 0));
                        } else {
                            ActivityTxt.this.speakNextPage();
                        }
                    }
                }
            };
        }
        if (this.tts != null) {
            this.tts.setOnUtteranceCompletedListener(this.tts_listener);
        }
    }

    private void setSpeakQueue(int fromId) {
        this.audio_pause_from_listener = false;
        if (this.speakLines != null && this.tts != null) {
            if (this.force_utteranceId != -2) {
                fromId = this.force_utteranceId + 1;
                this.force_utteranceId = -2;
            }
            A.log("tts_utteranceId 2*:" + this.tts_utteranceId + "/" + fromId + " size:" + this.speakLines.size());
            setSpeakOnUtteranceComplete();
            this.tts_stopped = false;
            if (this.tts_from_last_line) {
                this.tts_from_last_line = false;
                fromId = this.speakLines.size() - 1;
            }
            this.tts_utteranceId = fromId - 1;
            this.tts.setSpeechRate(((float) A.tts_speed) / 10.0f);
            this.tts.setPitch(((float) A.tts_pitch) / 10.0f);
            for (int i = fromId; i < this.speakLines.size(); i++) {
                TtsLine sl = (TtsLine) this.speakLines.get(i);
                if (i == fromId && A.tts_divide != 3) {
                    highlightText(this.lastStartOfSpeakText + sl.start, this.lastStartOfSpeakText + sl.end);
                }
                HashMap<String, String> speakParams = new HashMap();
                speakParams.put("utteranceId", String.valueOf(i));
                this.tts.speak(sl.s, 1, speakParams);
                if (A.tts_interval_enable && A.tts_interval_time > 0) {
                    this.tts.playSilence((long) A.tts_interval_time, 1, null);
                }
                this.tts_start_time = SystemClock.elapsedRealtime();
            }
            if (this.speakLines.size() == 0) {
                speakNextPage();
            }
        }
    }

    private Handler getSpeakHandler() {
        if (this.speakHandler == null) {
            this.speakHandler = new Handler() {
                public void handleMessage(Message msg) {
                    if (!(msg.what != 4 || ActivityTxt.this.pdf == null || ActivityTxt.this.pdf.selectedPara == null)) {
                        int y = 0;
                        int x = 0;
                        int h = (ActivityTxt.this.baseFrame.getHeight() - A.d(2.0f)) - (ActivityTxt.this.tts_panel.getVisibility() == 0 ? ActivityTxt.this.tts_panel.getHeight() : 0);
                        if (ActivityTxt.this.pdf.selectedPara != null) {
                            if (ActivityTxt.this.pdf.selectedPara.y2 > ((float) h)) {
                                y = ((int) ActivityTxt.this.pdf.selectedPara.y1) - A.d(5.0f);
                            } else if (ActivityTxt.this.pdf.selectedPara.y1 < 0.0f) {
                                y = (int) ActivityTxt.this.pdf.selectedPara.y1;
                            }
                            if (ActivityTxt.this.pdf.selectedPara.x1 < 0.0f || ActivityTxt.this.pdf.selectedPara.x1 > ((float) (ActivityTxt.this.baseFrame.getWidth() - A.d(2.0f)))) {
                                x = ((int) ActivityTxt.this.pdf.selectedPara.x1) - A.d(3.0f);
                            }
                        }
                        PDFPosition p = ActivityTxt.this.pdf.pdfView.viewGetPos();
                        if (!(y == 0 && x == 0)) {
                            if (y != 0) {
                                p.page_y += y;
                            }
                            if (x != 0) {
                                p.page_x += x;
                            }
                            ActivityTxt.this.pdf.pdfView.viewGoto(p);
                            ActivityTxt.this.pdf.postInvalidate();
                        }
                    }
                    if (msg.what == 0) {
                        if (!ActivityTxt.this.isPdf() || ActivityTxt.this.pdf.textReflow) {
                            ActivityTxt.this.do_page_down();
                        } else {
                            speakPdfText(true);
                        }
                    }
                    if (msg.what == 1) {
                        if (!ActivityTxt.this.isPdf() || ActivityTxt.this.pdf.textReflow) {
                            ActivityTxt.this.do_page_up();
                        } else {
                            speakPdfText(false);
                        }
                    }
                    if (msg.what == 2) {
                        try {
                            if (Math.abs(ActivityTxt.this.txtScroll.getScrollY() - ((Integer) msg.obj).intValue()) > 5) {
                                ActivityTxt.this.speakLines = ActivityTxt.this.getSpeakText();
                                if (ActivityTxt.this.speakTextLength < 2) {
                                    ActivityTxt.this.speakNextPage();
                                } else {
                                    ActivityTxt.this.setSpeakQueue(0);
                                }
                            } else if (ActivityTxt.this.isEndOfBook()) {
                                ActivityTxt.this.stop_speak();
                            }
                        } catch (Exception e) {
                            ActivityTxt.this.stop_speak();
                            A.error(e);
                        }
                    }
                    if (msg.what == 3 && A.tts_stop_enable && A.isSpeaking) {
                        ActivityTxt.this.stop_speak();
                    }
                    if (msg.what == 5 && ActivityTxt.this.speakLines != null) {
                        if (ActivityTxt.this.tts_utteranceId > ActivityTxt.this.speakLines.size() - 2) {
                            ActivityTxt.this.tts_utteranceId = -1;
                        }
                        if (!(!ActivityTxt.this.isPdf() || ActivityTxt.this.pdf.textReflow || ActivityTxt.this.tts_utteranceId == -1)) {
                            TtsLine sl = (TtsLine) ActivityTxt.this.speakLines.get(ActivityTxt.this.tts_utteranceId + 1);
                            ActivityTxt.this.pdf.selectedPara = null;
                            PDFPage.setForceSel(sl.start, sl.end);
                            ActivityTxt.this.pdf.postInvalidate();
                            ActivityTxt.this.getSpeakHandler().sendEmptyMessageDelayed(4, 100);
                        }
                        ActivityTxt.this.setSpeakQueue(ActivityTxt.this.tts_utteranceId + 1);
                    }
                }

                private void speakPdfText(boolean pageDown) {
                    if (!(pageDown && ActivityTxt.this.pdfGetCurrPageNo() == ActivityTxt.this.m_doc.GetPageCount() - 1) && (pageDown || ActivityTxt.this.pdfGetCurrPageNo() != 0)) {
                        do {
                            ActivityTxt.this.pdfPageScroll(pageDown);
                            if (!T.isNull(ActivityTxt.this.pdf.pdfView.viewGetCurPageText())) {
                                break;
                            }
                        } while (ActivityTxt.this.pdfGetCurrPageNo() < ActivityTxt.this.m_doc.GetPageCount() - 1);
                        ActivityTxt.this.speakLines = ActivityTxt.this.getSpeakText();
                        ActivityTxt.this.setSpeakQueue(0);
                        return;
                    }
                    ActivityTxt.this.stop_speak();
                }
            };
        }
        return this.speakHandler;
    }

    private void setStopTtsHandler() {
        if ((A.tts_stop_enable && !this.force_tts_not_stop) || this.force_tts_stop) {
            getSpeakHandler().removeMessages(3);
            getSpeakHandler().sendMessageDelayed(getSpeakHandler().obtainMessage(3), (long) ((A.tts_stop_time * 60) * 1000));
        }
    }

    private void speakCurrentPage(int y) {
        if (y == -100) {
            this.lastEndOfSpeakText = -1;
        }
        getSpeakHandler().sendMessageDelayed(getSpeakHandler().obtainMessage(2, Integer.valueOf(y)), 50);
    }

    private void speakNextPage() {
        try {
            int y1 = this.txtScroll.getScrollY();
            getSpeakHandler().removeMessages(0);
            getSpeakHandler().sendMessageDelayed(getSpeakHandler().obtainMessage(0, Integer.valueOf(y1)), 100);
        } catch (Exception e) {
            A.error(e);
        }
    }

    private void speakPriorPage() {
        try {
            int y1 = this.txtScroll.getScrollY();
            getSpeakHandler().removeMessages(1);
            getSpeakHandler().sendMessageDelayed(getSpeakHandler().obtainMessage(1, Integer.valueOf(y1)), 100);
        } catch (Exception e) {
            A.error(e);
        }
    }

    private void do_speak() {
        if (A.isProVersion || A.inviteProVersion) {
            this.force_tts_not_stop = false;
            this.force_tts_stop = false;
            if (this.web != null) {
                return;
            }
            if (A.isSpeaking) {
                stop_speak();
                return;
            }
            boolean askForTts;
            kill_tts(false);
            MRTextView tv = this.txtView.hStart != -1 ? this.txtView : this.txtView2.hStart != -1 ? this.txtView2 : null;
            if (A.askForTts && (tv == null || A.firstTimeUseTts)) {
                askForTts = true;
            } else {
                askForTts = false;
            }
            if ((!askForTts && !A.tts_stop_enable) || this.txtView.hStart != -1) {
                A.log("*TTS start 1");
                initTTS();
            } else if (this.ttsAlertDlg == null) {
                if (A.firstTimeUseTts) {
                    A.firstTimeUseTts = false;
                    T.openTtsOptions(this);
                }
                ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(R.layout.tts_options, null);
                final CheckBox askCheck = (CheckBox) layout.findViewById(R.id.ofNeverAskAgain);
                final CheckBox stopCheck = (CheckBox) layout.findViewById(R.id.tts_stop_check);
                final EditText stopEt = (EditText) layout.findViewById(R.id.tts_stop_time);
                final Spinner sp = (Spinner) layout.findViewById(R.id.pmTtsDivides);
                if (A.tts_stop_enable) {
                    stopCheck.setChecked(true);
                    askCheck.setEnabled(false);
                }
                stopCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            askCheck.setChecked(false);
                            askCheck.setEnabled(false);
                            return;
                        }
                        askCheck.setEnabled(true);
                    }
                });
                stopEt.setText("" + A.tts_stop_time);
                ((ImageButton) layout.findViewById(R.id.pmSetTTS)).setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        T.openTtsOptions(ActivityTxt.this);
                    }
                });
                sp.setSelection(A.tts_divide);
                this.ttsAlertDlg =new AlertDialog.Builder(this);
                this.ttsAlertDlg.setTitle(getString(R.string.tts_options)).setView(layout).setPositiveButton((int) R.string.start_speak_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        A.tts_stop_enable = stopCheck.isChecked();
                        int tmp = A.tts_stop_time;
                        try {
                            tmp = Integer.valueOf(stopEt.getText().toString()).intValue();
                        } catch (Exception e) {
                        }
                        A.tts_stop_time = tmp;
                        A.askForTts = !askCheck.isChecked();
                        A.tts_divide = sp.getSelectedItemPosition();
                        ActivityTxt.this.initTTS();
                        ActivityTxt.this.ttsAlertDlg = null;
                    }
                }).setOnCancelListener(new OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        A.tts_stop_enable = stopCheck.isChecked();
                        int tmp = A.tts_stop_time;
                        try {
                            tmp = Integer.valueOf(stopEt.getText().toString()).intValue();
                        } catch (Exception e) {
                        }
                        A.tts_stop_time = tmp;
                        A.askForTts = !askCheck.isChecked();
                        A.tts_divide = sp.getSelectedItemPosition();
                        ActivityTxt.this.ttsAlertDlg = null;
                    }
                }).show();
            }
        }
    }

    private void initTTS() {
        this.tts = new TextToSpeech(this, this.onInitTTS);
    }

    public void stop_speak() {
        stop_speak(true);
    }

    public void stop_speak(boolean showTip) {
        if (A.doLongTap != 19 || SystemClock.elapsedRealtime() - this.tts_start_time >= 3000) {
            A.isSpeaking = false;
            PDFPage.delForceSel(true);
            this.bookBackgroundStyle = -1;
            this.yBeforePageDown.clear();
            if (dualPageEnabled()) {
                saveLastPostion(true);
                this.txtView.nullLayouts();
                this.txtView2.nullLayouts();
                clearTxtView();
                checkDualPageMode();
                new Handler() {
                    public void handleMessage(Message msg) {
                        ActivityTxt.this.isTxtScrollReady = false;
                        ActivityTxt.this.txtLay.requestLayout();
                        ActivityTxt.this.handler.post(ActivityTxt.this);
                    }
                }.sendEmptyMessageDelayed(0, 100);
            }
            if (!this.isPaused || this.txtView.hStart == -1) {
                this.txtView.hStart = -1;
            } else {
                txtScrollByDelay(this.txtView.hStart);
                updateProgressStatus();
            }
            this.lastEndOfSpeakText = -1;
            showTtsPanel(false);
            hide_tts_icon();
            hideTipsPanel();
            kill_tts(showTip);
            unregisterAudioFocus();
        }
    }

    private void kill_tts(boolean showTip) {
        if (this.tts != null) {
            if (showTip) {
                try {
                    T.showToastText(this, getString(R.string.tts_stop));
                } catch (Exception e) {
                    A.error(e);
                }
            }
            this.tts_stopped = true;
            tts_stop();
            this.tts.shutdown();
            this.tts = null;
        }
    }

    private void show_tts_icon() {
        if (A.isSpeaking) {
            try {
                if (this.tts_nofitication == null) {
                    Notification notification;
                    if (VERSION.SDK_INT >= 15) {
                        RemoteViews rview = new RemoteViews(getPackageName(), R.layout.tts_notification);
                        this.tts_nofitication = new Notification();
                        this.tts_nofitication.icon = R.drawable.ttsb3;
                        notification = this.tts_nofitication;
                        notification.flags |= 2;
                        this.tts_nofitication.contentView = rview;
                        String TTS_STOP = "mr.tts.stop";
                        String TTS_PAGE_UP = "mr.tts.page.up";
                        String TTS_PRIOR = "mr.tts.prior";
                        String TTS_PLAY = "mr.tts.play";
                        String TTS_NEXT = "mr.tts.next";
                        String TTS_PAGE_DOWN = "mr.tts.page.down";
                        String TTS_TO_READER = "mr.tts.to.reader";
                        Drawable d = A.getBookCover(A.lastFile);
                        if (d != null) {
                            rview.setImageViewBitmap(R.id.tts_lay, T.drawableToBitmap(d));
                        }
                        rview.setOnClickPendingIntent(R.id.tts_stop, PendingIntent.getBroadcast(this, 0, new Intent("mr.tts.stop"), 134217728));
                        rview.setOnClickPendingIntent(R.id.tts_page_up, PendingIntent.getBroadcast(this, 0, new Intent("mr.tts.page.up"), 134217728));
                        rview.setOnClickPendingIntent(R.id.tts_prior, PendingIntent.getBroadcast(this, 0, new Intent("mr.tts.prior"), 134217728));
                        rview.setOnClickPendingIntent(R.id.tts_play, PendingIntent.getBroadcast(this, 0, new Intent("mr.tts.play"), 134217728));
                        rview.setOnClickPendingIntent(R.id.tts_next, PendingIntent.getBroadcast(this, 0, new Intent("mr.tts.next"), 134217728));
                        rview.setOnClickPendingIntent(R.id.tts_page_down, PendingIntent.getBroadcast(this, 0, new Intent("mr.tts.page.down"), 134217728));
                        rview.setOnClickPendingIntent(R.id.tts_lay, PendingIntent.getBroadcast(this, 0, new Intent("mr.tts.to.reader"), 134217728));
                        IntentFilter filter = new IntentFilter();
                        filter.addAction("mr.tts.stop");
                        filter.addAction("mr.tts.page.up");
                        filter.addAction("mr.tts.prior");
                        filter.addAction("mr.tts.play");
                        filter.addAction("mr.tts.next");
                        filter.addAction("mr.tts.page.down");
                        filter.addAction("mr.tts.to.reader");
                        this.tts_receiver = new BroadcastReceiver() {
                            public void onReceive(Context context, Intent intent) {
                                String action = intent.getAction();
                                if (action.equals("mr.tts.stop")) {
                                    ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_stop, false);
                                }
                                if (action.equals("mr.tts.page.up")) {
                                    ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_page_up, false);
                                }
                                if (action.equals("mr.tts.prior")) {
                                    ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_prior, false);
                                }
                                if (action.equals("mr.tts.play")) {
                                    ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_play, false);
                                }
                                if (action.equals("mr.tts.next")) {
                                    ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_next, false);
                                }
                                if (action.equals("mr.tts.page.down")) {
                                    ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_page_down, false);
                                }
                                if (action.equals("mr.tts.to.reader")) {
                                    Intent i = ActivityTxt.this.getPackageManager().getLaunchIntentForPackage(ActivityTxt.this.getPackageName());
                                    if (i != null) {
                                        i.setFlags(268566528);
                                        i.setPackage(null);
                                        ActivityTxt.this.startActivity(i);
                                    }
                                }
                            }
                        };
                        registerReceiver(this.tts_receiver, filter);
                    } else {
                        Intent i = new Intent(getApplicationContext(), ActivityMain.class);
                        i.setAction("android.intent.action.MAIN");
                        i.addCategory("android.intent.category.LAUNCHER");
                        PendingIntent p = PendingIntent.getActivity(this, 0, i, 0);
                        if (VERSION.SDK_INT < 19) {
                            this.tts_nofitication = new Notification(R.drawable.ttsb3, getString(R.string.app_name), System.currentTimeMillis());
                            notification = this.tts_nofitication;
                            notification.flags |= 2;
                            try {
                                this.tts_nofitication.getClass().getMethod("setLatestEventInfo", new Class[]{Context.class, CharSequence.class, CharSequence.class, PendingIntent.class}).invoke(this.tts_nofitication, new Object[]{getApplicationContext(), getString(R.string.app_name), "TTS", p});
                            } catch (Throwable e) {
                                A.error(e);
                            }
                        } else {
                            this.tts_nofitication = new Notification.Builder(getApplicationContext()).setSmallIcon(R.drawable.ttsb3).setOngoing(true).setContentIntent(p).setContentTitle(getString(R.string.app_name)).setContentText("TTS").build();
                        }
                    }
                }
                ((NotificationManager) getSystemService("notification")).notify(A.MOONREADER_OPEN_BROADCAST.hashCode(), this.tts_nofitication);
            } catch (Exception e2) {
                A.error(e2);
            }
        }
    }

    public void hide_tts_icon() {
        try {
            if (this.tts_receiver != null) {
                unregisterReceiver(this.tts_receiver);
                this.tts_receiver = null;
            }
            if (this.tts_nofitication != null) {
                ((NotificationManager) getSystemService("notification")).cancel(A.MOONREADER_OPEN_BROADCAST.hashCode());
                this.tts_nofitication = null;
            }
        } catch (Exception e) {
            A.error(e);
        }
    }

    private boolean showTipsPanel(int type) {
        if (type == 0 && !A.askTtsTip) {
            return false;
        }
        if (type == 1 && !A.askAutoScrollTip) {
            return false;
        }
        if (this.tips_panel == null) {
            this.tips_panel = ((ViewStub) findViewById(R.id.tipStub)).inflate();
            this.tips_ok = this.tips_panel.findViewById(R.id.okB);
            this.tips_ok.setOnClickListener(this);
            this.tips_confirm = (CheckBox) this.tips_panel.findViewById(R.id.ofNeverAskAgain);
            this.tips_image = (ImageView) this.tips_panel.findViewById(R.id.imageView1);
        }
        this.tips_confirm.setChecked(false);
        if (type == 0) {
            this.tips_image.setImageDrawable(A.getDrawableFromAsset(getAssets(), "tips_tts.png", 0));
        }
        if (type == 1) {
            this.tips_image.setImageDrawable(A.getDrawableFromAsset(getAssets(), "tips_autoscroll.png", 0));
        }
        this.tips_type = type;
        this.tips_panel.setVisibility(0);
        return true;
    }

    private boolean hideTipsPanel() {
        if (this.tips_panel == null || this.tips_panel.getVisibility() != 0) {
            return false;
        }
        this.tips_panel.setVisibility(8);
        this.tips_image.setImageDrawable(null);
        if (!this.tips_confirm.isChecked()) {
            return true;
        }
        if (this.tips_type == 0) {
            A.askTtsTip = false;
        }
        if (this.tips_type != 1) {
            return true;
        }
        A.askAutoScrollTip = false;
        return true;
    }

    private void initTtsPanel() {
        this.tts_panel = ((ViewStub) findViewById(R.id.viewStub1)).inflate();
        this.ttsClickEvent = new OnClickListener() {
            public void onClick(View v) {
                ActivityTxt.this.do_tts_buttons(v);
            }
        };
        this.ttsSeekBarEvent = new OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar == ActivityTxt.this.tts_speed || seekBar == ActivityTxt.this.tts_pitch) {
                    int progress = seekBar.getProgress();
                    if (seekBar == ActivityTxt.this.tts_speed) {
                        A.tts_speed = ActivityTxt.this.get_tts_speed_progress(progress);
                    }
                    if (seekBar == ActivityTxt.this.tts_pitch) {
                        if (progress <= 0) {
                            progress = 1;
                        }
                        A.tts_pitch = progress;
                    }
                    ActivityTxt.this.setTtsResetVisibile();
                    ActivityTxt.this.restore_speak();
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (seekBar == ActivityTxt.this.tts_speed) {
                        progress = ActivityTxt.this.get_tts_speed_progress(progress);
                    }
                    T.showToastText(ActivityTxt.this, "" + progress);
                    ActivityTxt.this.keepTtsPanelVisible();
                    if (seekBar == ActivityTxt.this.tts_volume) {
                        ((AudioManager) ActivityTxt.this.getSystemService("audio")).setStreamVolume(3, seekBar.getProgress(), 0);
                    }
                }
            }
        };
        this.tts_page_up = this.tts_panel.findViewById(R.id.tts_page_up);
        this.tts_page_up.setOnClickListener(this.ttsClickEvent);
        this.tts_page_down = this.tts_panel.findViewById(R.id.tts_page_down);
        this.tts_page_down.setOnClickListener(this.ttsClickEvent);
        this.tts_stop = this.tts_panel.findViewById(R.id.tts_stop);
        this.tts_stop.setOnClickListener(this.ttsClickEvent);
        this.tts_prior = this.tts_panel.findViewById(R.id.tts_prior);
        this.tts_prior.setOnClickListener(this.ttsClickEvent);
        this.tts_play = this.tts_panel.findViewById(R.id.tts_play);
        this.tts_play.setOnClickListener(this.ttsClickEvent);
        this.tts_next = this.tts_panel.findViewById(R.id.tts_next);
        this.tts_next.setOnClickListener(this.ttsClickEvent);
        this.tts_reset = (TextView) this.tts_panel.findViewById(R.id.resetTv);
        this.tts_reset.setOnClickListener(this.ttsClickEvent);
        this.tts_reset.setText(getString(R.string.reset).replace(":", ""));
        setTtsResetVisibile();
        this.tts_options = this.tts_panel.findViewById(R.id.tts_options);
        this.tts_options.setOnClickListener(this.ttsClickEvent);
        this.tts_speed = (SeekBar) this.tts_panel.findViewById(R.id.tts_speed);
        this.tts_speed.setOnSeekBarChangeListener(this.ttsSeekBarEvent);
        tts_speed_setProgress(A.tts_speed);
        this.tts_pitch = (SeekBar) this.tts_panel.findViewById(R.id.tts_pitch);
        this.tts_pitch.setOnSeekBarChangeListener(this.ttsSeekBarEvent);
        this.tts_pitch.setProgress(A.tts_pitch);
        this.tts_volume = (SeekBar) this.tts_panel.findViewById(R.id.tts_volume);
        AudioManager am = (AudioManager) getSystemService("audio");
        this.tts_volume.setMax(am.getStreamMaxVolume(3));
        this.tts_volume.setProgress(am.getStreamVolume(3));
        this.tts_volume.setOnSeekBarChangeListener(this.ttsSeekBarEvent);
    }

    private void setTtsResetVisibile() {
        TextView textView = this.tts_reset;
        int i = (A.tts_speed == 10 && A.tts_pitch == 10) ? 8 : 0;
        textView.setVisibility(i);
    }

    private void do_tts_buttons(View v) {
        do_tts_buttons(v, true);
    }

    public void do_tts_buttons(View v, boolean showPanel) {
        this.audio_pause_from_listener = false;
        if (showPanel) {
            keepTtsPanelVisible();
        }
        if (v == this.tts_page_up) {
            switchTtsPlayPause(true);
            this.tts_stopped = true;
            tts_stop();
            this.lastEndOfSpeakText = -1;
            speakPriorPage();
        }
        if (v == this.tts_page_down) {
            switchTtsPlayPause(true);
            this.tts_stopped = true;
            tts_stop();
            this.lastEndOfSpeakText = -1;
            speakNextPage();
        }
        if (v == this.tts_stop) {
            stop_speak();
        }
        if (v == this.tts_prior) {
            switchTtsPlayPause(true);
            A.log("tts_utteranceId 1:" + this.tts_utteranceId);
            if (this.tts_utteranceId > -1) {
                this.tts_utteranceId--;
                this.force_utteranceId = this.tts_utteranceId;
                restore_speak();
            } else {
                this.tts_from_last_line = true;
                this.tts_stopped = true;
                tts_stop();
                speakPriorPage();
            }
        }
        if (v == this.tts_play) {
            if (this.tts_paused) {
                restore_speak();
            } else if (this.tts != null) {
                switchTtsPlayPause(false);
                this.tts_stopped = true;
                tts_stop();
                if (this.tts_panel.getVisibility() != 0) {
                    showTtsPanel(true, true);
                }
            }
        }
        if (v == this.tts_next) {
            switchTtsPlayPause(true);
            if (this.speakLines == null || this.tts_utteranceId >= this.speakLines.size() - 2) {
                this.tts_stopped = true;
                if (this.tts != null) {
                    tts_stop();
                    speakNextPage();
                }
            } else {
                this.tts_utteranceId++;
                this.force_utteranceId = this.tts_utteranceId;
                restore_speak();
            }
        }
        if (v == this.tts_options) {
            T.openTtsOptions(this);
        }
        if (v == this.tts_reset) {
            A.tts_speed = 10;
            tts_speed_setProgress(10);
            A.tts_pitch = 10;
            this.tts_pitch.setProgress(10);
            setTtsResetVisibile();
            if (A.isSpeaking) {
                restore_speak();
            }
        }
    }

    private void tts_stop() {
        try {
            this.tts.stop();
        } catch (Exception e) {
            A.error(e);
        }
    }

    private void restore_speak() {
        try {
            this.tts_stopped = true;
            tts_stop();
            getSpeakHandler().removeMessages(5);
            getSpeakHandler().sendEmptyMessageDelayed(5, 100);
            switchTtsPlayPause(true);
        } catch (Exception e) {
            A.error(e);
            stop_speak();
            T.showToastText(this, A.errorMsg(e));
        }
    }

    private void switchTtsPlayPause(boolean playState) {
        if (playState) {
            this.tts_paused = false;
            if (this.tts_play != null) {
                ((ImageView) this.tts_play).setImageResource(R.drawable.tts_pause);
                return;
            }
            return;
        }
        this.tts_paused = true;
        if (this.tts_play != null) {
            ((ImageView) this.tts_play).setImageResource(R.drawable.tts_play);
        }
    }

    protected void keepTtsPanelVisible() {
        if (this.tts_panel != null) {
            this.ttsButtonPressed = true;
            this.tts_panel.setAnimation(null);
            this.tts_panel.setVisibility(0);
            this.ttsIv.setVisibility(8);
            this.tts_panel_visible = true;
        }
    }

    private void showTtsPanel(boolean visible) {
        showTtsPanel(visible, true);
    }

    private void showTtsPanel(boolean visible, boolean animate) {
        int i = 0;
        if (this.tts_panel != null) {
            if (!visible && showTipsPanel(0)) {
                animate = false;
            }
            this.tts_panel_visible = visible;
            this.tts_panel.setVisibility(visible ? 0 : 8);
            if (A.isSpeaking) {
                Animation animation1;
                int h = this.baseFrame.getHeight();
                if (visible) {
                    animation1 = new TranslateAnimation(0.0f, 0.0f, (float) this.baseFrame.getHeight(), (float) (this.baseFrame.getHeight() - h));
                } else {
                    animation1 = new TranslateAnimation(0.0f, 0.0f, (float) (this.baseFrame.getHeight() - h), (float) this.baseFrame.getHeight());
                }
                animation1.setDuration(1000);
                this.tts_panel.startAnimation(animation1);
                if (animate) {
                    Animation animation2 = visible ? new AlphaAnimation(1.0f, 0.1f) : new AlphaAnimation(0.1f, 1.0f);
                    animation2.setDuration(visible ? 800 : 1600);
                    this.ttsIv.startAnimation(animation2);
                }
            }
        }
        ImageView imageView = this.ttsIv;
        if (!A.isSpeaking || visible) {
            i = 8;
        }
        imageView.setVisibility(i);
    }

    private void showTtsOptionsButton() {
        if (this.tts_panel == null) {
            initTtsPanel();
        } else {
            this.tts_volume.setProgress(((AudioManager) getSystemService("audio")).getStreamVolume(3));
        }
        switchTtsPlayPause(true);
        showTtsPanel(true, false);
        this.ttsButtonPressed = false;
        new Handler() {
            public void handleMessage(Message msg) {
                if (!ActivityTxt.this.ttsButtonPressed && ActivityTxt.this.tts_panel_visible) {
                    ActivityTxt.this.showTtsPanel(false);
                }
            }
        }.sendEmptyMessageDelayed(0, 4000);
    }

    protected void onStop() {
        A.log("-txt:stop");
        super.onStop();
        if (this.isOnExiting) {
            T.deleteFile(A.txtPauseOnlyTagFile);
        } else if (A.isProVersion) {
            T.saveFileText(A.txtPauseOnlyTagFile, "" + System.currentTimeMillis());
        }
    }

    protected void onDestroy() {
        A.log("-txt:destroy");
        this.bookType = -1;
        if (this.mp3 != null) {
            this.mp3.stop();
        }
        unregisterHeadset();
        if (this.batteryReceiver != null) {
            unregisterReceiver(this.batteryReceiver);
            this.batteryReceiver = null;
            this.battery = null;
        }
        super.onDestroy();
        T.deleteFile(A.txtPauseOnlyTagFile);
        if (ActivityMain.selfPref == null && SystemClock.elapsedRealtime() - ActivityMain.backExitTime > 1000) {
            A.forceRebootToMain = true;
            A.SaveOptions(this);
            startActivity(new Intent(this, ActivityMain.class));
        }
    }

    private void clearMemoryOnExit() {
        if (A.isSpeaking) {
            stop_speak();
        }
        if (this.web != null) {
            this.web.resetZoom();
        }
        this.bookType = -1;
        selfPref = null;
        A.baseFrame = null;
        A.txtScroll = null;
        A.txtScroll2 = null;
        A.txtView = null;
        A.txtView2 = null;
        A.splitHtmls = new ArrayList();
        A.clearTxts2();
        A.clearTxts();
        A.savedBackgroundDrwable = null;
        if (this.curl3d != null) {
            this.curl3d.clearAllShots();
        }
        T.recycle(this.flipView.bm1);
        T.recycle(this.flipView.bm2);
        T.recycle(this.tmpFlipShot1);
        T.recycle(this.tmpFlipShot2);
        if (isPdf() && this.pdf != null) {
            if (this.pdfThumb != null && ((Integer) this.pdfThumb.getTag()).intValue() == Global.def_view) {
                this.pdfThumb.thumbClose();
            }
            this.pdf.set_thumb(null);
            this.pdf.close();
            if (this.m_doc != null) {
                this.m_doc.Close();
            }
            Global.RemoveTmp();
        }
    }

    private void createRollingScrollLayout() {
        this.txtLayS.setVisibility(0);
        if (this.scrollImage == null) {
            this.scrollImage = new ScrollImage(this);
            this.txtLayS.addView(this.scrollImage, new LayoutParams(-1, -1));
        }
        this.txtLayS.setOnTouchListener(this);
    }

    private void do_AutoScrollAsk() {
        if (A.askForScrollEvent) {
            ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(R.layout.scroll_event_confirm, null);
            final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.ofNeverAskAgain);
            final SeekBar sb = (SeekBar) layout.findViewById(R.id.pmScrollSpeed);
            sb.setProgress(100 - A.autoScrollSpeed);
            final Spinner sp = (Spinner) layout.findViewById(R.id.pmScrollMode);
            sp.setSelection(A.autoScrollMode);
            if (isPdf()) {
                layout.findViewById(R.id.modeLay).setVisibility(8);
            }
           new AlertDialog.Builder(this).setTitle(getString(R.string.auto_scroll_control)).setView(layout).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    A.askForScrollEvent = !checkBox.isChecked();
                    A.autoScrollMode = sp.getSelectedItemPosition();
                    A.autoScrollSpeed = 100 - sb.getProgress();
                    ActivityTxt.this.do_AutoScroll(true);
                }
            }).show();
            return;
        }
        do_AutoScroll(true);
    }

    private void do_AutoScroll(boolean start) {
        if (this.web == null) {
            do_AutoScroll(start, true);
        }
    }

    private void do_AutoScroll(boolean start, boolean showHint) {
        if (this.web == null) {
            if (start) {
                saveStatisticsInfo();
            } else {
                this.read_words = 0;
                this.statisticsTime = SystemClock.elapsedRealtime();
            }
            this.autoScrollPaused = false;
            if (start) {
                showTipsPanel(1);
                if (!(showHint && resetPositionFrom2PageModeBeforeAutoScroll())) {
                    startAutoScrollEvent();
                }
                inverseLayoutVisible(true);
                this.baseFrame.setKeepScreenOn(true);
                A.isInAutoScroll = true;
                return;
            }
            this.yBeforePageDown.clear();
            A.isInAutoScroll = false;
            if (!isPdf()) {
                this.bookBackgroundStyle = -1;
                if (showHint && dualPageEnabled()) {
                    saveLastPostion(true);
                    this.handler.removeMessages(301);
                    this.txtView.nullLayouts();
                    this.txtView2.nullLayouts();
                    clearTxtView();
                    checkDualPageMode();
                    new Handler() {
                        public void handleMessage(Message msg) {
                            ActivityTxt.this.isTxtScrollReady = false;
                            ActivityTxt.this.txtLay.requestLayout();
                            ActivityTxt.this.handler.post(ActivityTxt.this);
                        }
                    }.sendEmptyMessageDelayed(0, 200);
                }
            }
            if (this.scrollTimer != null) {
                this.scrollTimer.cancel();
            }
            this.txtLayS.setVisibility(8);
            if (this.scrollImage != null) {
                T.recycle(this.scrollImage.bitmap);
            }
            if (this.scrollImage != null) {
                T.recycle(this.scrollImage.shadowBm);
            }
            if (this.txtLineS != null) {
                this.txtLineS.setVisibility(8);
            }
            if (showHint) {
                T.showToastText(this, getString(R.string.scroll_cancel));
            }
            this.baseFrame.setKeepScreenOn(A.keepScreenAwake);
            hideTipsPanel();
            if (!(isPdf() || dualPageEnabled())) {
                statusHandlerSendMessage(0, 0);
            }
            this.pixel_scroll_ignore = 0;
        }
    }

    private void startAutoScrollEvent() {
        A.isInAutoScroll = true;
        switch (getScrollMode()) {
            case 0:
            case 1:
                createRollingScrollLayout();
                if (isPdf()) {
                    this.pdfVerticalOffset = pdfVerticalParaMargin();
                }
                this.scrollPos = isPdf() ? this.pdfVerticalOffset : this.txtScroll.getPaddingTop();
                break;
            case 2:
            case 3:
                if (A.ebook != null && this.txtView.getRealLineCount() > 2) {
                    int l = A.getPageHeight() / this.txtView.getLineHeight(this.txtView.getRealLineCount() - 1);
                    if (this.txtView.appendLineCount < l / 4) {
                        appendLines((l / 4) - this.txtView.appendLineCount);
                        break;
                    }
                }
                break;
            case 4:
                if (this.txtLineS == null) {
                    this.txtLineS = new View(this);
                    this.txtLineS.setBackgroundColor(-1436129690);
                    this.baseFrame.addView(this.txtLineS, new LayoutParams(-1, 1));
                }
                this.txtLineS.setVisibility(0);
                break;
        }
        this.autoScrollTimesCount = 0;
        this.scroll_event_time = SystemClock.elapsedRealtime();
        this.justStartScroll = true;
        this.scrollTimer = new Timer();
        this.scrollTimer.schedule(new AutoScrollTask(), 0, (long) getAutoScrollInterval());
    }

    private boolean resetPositionFrom2PageModeBeforeAutoScroll() {
        if (dualPageEnabled()) {
            try {
                int p = this.txtView.getCurPosition();
                A.isInAutoScroll = true;
                checkDualPageMode();
                this.handler.sendMessageDelayed(this.handler.obtainMessage(START_AUTOSCROLL_EVENT, p, 0), 100);
                return true;
            } catch (Exception e) {
                A.error(e);
            }
        }
        return false;
    }

    private int getAutoScrollInterval() {
        int i = 0;
        int l = A.autoScrollSpeed;
        int interval = 100;
        if (getScrollMode() == 0) {
            if (l >= 50) {
                interval = (((l - 50) * 150) / 50) + 40;
            } else {
                int step = A.autoScrollSpeed % 5 == 0 ? 5 : l % 5;
                if (getPixelScrollSpace() <= 4) {
                    interval = (step * 3) + 20;
                } else {
                    interval = 25;
                }
            }
            int i2 = interval * 100;
            if (l < 50) {
                i = 50 - l;
            }
            interval = i2 / ((i * 2) + 100);
        } else if (getScrollMode() == 1) {
            if (l < 50) {
                i = 50 - l;
            }
            interval = 10000 / ((i * 2) + 100);
        } else if (getScrollMode() == 2) {
            interval = 4;
        }
        A.log("----interval:" + interval);
        return interval;
    }

    private int getPixelScrollSpace() {
        int l = A.autoScrollSpeed;
        return l >= 50 ? 1 : (55 - l) / 5;
    }

    private void resetPixelScrollSpeed() {
        if ((getScrollMode() == 2 || getScrollMode() == 0) && this.scrollTimer != null) {
            int interval = getAutoScrollInterval();
            if (interval != this.scrollInterval) {
                this.scrollInterval = interval;
                if (this.scrollTimer != null) {
                    this.scrollTimer.cancel();
                }
                this.scrollTimer = new Timer();
                this.scrollTimer.schedule(new AutoScrollTask(), 10, (long) interval);
            }
        }
    }

    private void do_autoscroll_message() {
        if (!this.autoScrollPaused) {
            boolean show_scroll_progress = false;
            if (SystemClock.elapsedRealtime() - this.scroll_event_time > 2500) {
                this.scroll_event_time = SystemClock.elapsedRealtime();
                show_scroll_progress = true;
            }
            int l = A.autoScrollSpeed;
            int lh;
            int i;
            switch (getScrollMode()) {
                case 0:
                case 1:
                    boolean isRollingLine = getScrollMode() == 1;
                    if (this.justStartScroll) {
                        autoScrollTurnPage(1, false);
                        this.justStartScroll = false;
                        this.scrollImage.topLine = 0;
                    }
                    if (isRollingLine) {
                        lh = this.txtView.getLineHeight(this.txtView.getLayout().getLineForVertical((this.txtScroll.getScrollY() + this.scrollImage.topLine) + 1));
                        i = this.scrollPos;
                        if (lh > this.txtView.getLineHeight() * 2) {
                            lh = this.txtView.getLineHeight();
                        }
                        this.scrollPos = i + lh;
                    } else {
                        this.scrollPos = ((getPixelScrollSpace() * 55) / (55 - (l < 50 ? 50 - l : 0))) + this.scrollPos;
                    }
                    int bottomOffset = isPdf() ? this.pdfVerticalOffset : showStatusbar() ? this.statusLay.getHeight() : 0;
                    if (this.scrollPos > this.baseFrame.getHeight() - bottomOffset) {
                        if (isEndOfBook()) {
                            do_AutoScroll(false);
                        } else {
                            autoScrollTurnPage(1, true);
                        }
                    }
                    this.scrollImage.topLine = this.scrollPos;
                    this.scrollImage.invalidate();
                    return;
                case 2:
                    long j = this.pixel_scroll_ignore + 1;
                    this.pixel_scroll_ignore = j;
                    i = l < 20 ? 2 : l < 30 ? 3 : l < 40 ? 4 : 5;
                    if (j > ((long) (l / i))) {
                        this.pixel_scroll_ignore = 0;
                        adjustTxtViewText();
                        int y1 = this.txtScroll.getScrollY();
                        boolean forceToNext = false;
                        if (A.ebook != null && A.landscape2PageMode && A.isLandscape() && A.getPageHeight() + y1 > this.txtView.getLineTop3(this.txtView.getLineCount()) + (this.txtView.getLineHeight() * 5)) {
                            forceToNext = true;
                        }
                        if (!forceToNext) {
                            this.txtScroll.scrollBy(0, l < 40 ? ((50 - l) * 5) / (50 - (40 - l)) : 1);
                        }
                        pixelAutoScrollLastLineHint(y1);
                    }
                    if (show_scroll_progress) {
                        showReadProgress(0);
                        return;
                    }
                    return;
                case 3:
                    adjustTxtViewText();
                    int y2 = this.txtScroll.getScrollY();
                    lh = this.txtView.getLineHeight(this.txtView.getLayout().getLineForVertical((this.txtScroll.getScrollY() + A.getPageHeight()) + 1));
                    ScrollView scrollView = this.txtScroll;
                    if (lh > this.txtView.getLineHeight() * 2) {
                        lh = this.txtView.getLineHeight();
                    }
                    scrollView.scrollBy(0, lh);
                    pixelAutoScrollLastLineHint(y2);
                    if (show_scroll_progress) {
                        showReadProgress(0);
                        return;
                    }
                    return;
                case 4:
                    float speed = (float) ((l + 20) / 10);
                    speed = ((((speed * speed) / (5.0f + speed)) * 30.0f) * 100.0f) / ((float) (((l < 50 ? 50 - l : 0) * 2) + 100));
                    if (((float) this.autoScrollTimesCount) >= speed) {
                        this.autoScrollTimesCount = 0;
                        if (isEndOfBook()) {
                            do_AutoScroll(false);
                        } else {
                            pageScroll(1);
                        }
                    }
                    int w = (int) (15.0f + (((float) (A.getScreenWidth2() * this.autoScrollTimesCount)) / speed));
                    if (this.autoScrollTimesCount % 2 == 0 && this.txtLineS != null) {
                        this.txtLineS.layout(0, 0, w, A.d(2.0f));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private void pixelAutoScrollLastLineHint(int y) {
        if (y != this.txtScroll.getScrollY()) {
            this.pixelAutoScrollToBottomTime = 0;
        } else if (isEndOfBook()) {
            do_AutoScroll(false);
        } else if (getBookType() != 100) {
            pageScroll(1);
        } else if (this.pixelAutoScrollToBottomTime == 0) {
            this.pixelAutoScrollToBottomTime = SystemClock.elapsedRealtime();
        } else {
            long interval = (1 + SystemClock.elapsedRealtime()) - this.pixelAutoScrollToBottomTime;
            if (A.autoScrollSpeed <= 10 || interval > 3000) {
                this.pixelAutoScrollToBottomTime = 0;
                T.hideToast();
                pageScroll(1);
                return;
            }
            long i = (4000 - interval) / 1000;
            if (i != this.lastPixelTipNumber) {
                this.lastPixelTipNumber = i;
                T.showToastText(this, "" + i);
            }
        }
    }

    private void autoScrollTurnPage(int direction, boolean resetScrollPos) {
        switch (getScrollMode()) {
            case 0:
            case 1:
                if (this.scrollImage != null) {
                    boolean hideTips;
                    if (resetScrollPos) {
                        boolean isRollingLine;
                        int i;
                        if (getScrollMode() == 1) {
                            isRollingLine = true;
                        } else {
                            isRollingLine = false;
                        }
                        if (isPdf()) {
                            i = this.pdfVerticalOffset;
                        } else {
                            i = (isRollingLine ? this.txtView.getLineHeight() : 0) + this.txtScroll.getPaddingTop();
                        }
                        this.scrollPos = i;
                    }
                    this.scrollImage.setVisibility(4);
                    if (this.tips_panel == null || this.tips_panel.getVisibility() != 0) {
                        hideTips = false;
                    } else {
                        hideTips = true;
                    }
                    if (hideTips) {
                        this.tips_panel.setVisibility(4);
                    }
                    this.scrollImage.setBitmap(getPageShot(true, true));
                    this.scrollImage.topLine = 0;
                    this.scrollImage.setVisibility(0);
                    if (hideTips) {
                        this.tips_panel.setVisibility(0);
                    }
                    this.scrollImage.invalidate();
                    pageScroll(direction);
                    return;
                }
                return;
            case 2:
            case 3:
                pageScroll(direction);
                return;
            case 4:
                this.autoScrollTimesCount = 0;
                pageScroll(direction);
                return;
            default:
                return;
        }
    }

    private boolean forceExitRestart() {
        if (!A.tmpOutOfMemoryTag && !A.isLowMemory(30) && !A.isLowestMemory()) {
            return false;
        }
        if (A.syncType != 0) {
            onPause();
            this.restart_after_sync_finish = true;
            String tip = getString(R.string.sync_option) + "...";
            if (A.syncType == 4) {
                tip = A.getGdriveTitle(tip);
            }
            ProgressDialog dlg = new ProgressDialog(this);
            dlg.setTitle(A.syncType == 2 ? "Dropbox" : "Google Drive");
            dlg.setMessage(tip);
            dlg.setCancelable(true);
            dlg.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    ActivityTxt.this.restartReaderToMain();
                }
            });
            dlg.setButton(-2, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityTxt.this.restartReaderToMain();
                }
            });
            dlg.show();
        } else {
            restartReaderToMain();
        }
        return true;
    }

    void do_back_to_book_stack() {
        if (!forceExitRestart()) {
            doFinish();
        }
    }

    private void doFinish() {
        this.contentLay.finished = true;
        MrAd.showFullAdMobOnExit(false);
        saveEBookName();
        this.isOnExiting = true;
        finish();
        selfPref = null;
        A.typefaceCache = null;
    }

    public void terminate_reader() {
        try {
            if (!T.isNull(ActivityMain.selfPref)) {
                ActivityMain.selfPref.finish();
            }
            ActivityMain.selfPref = null;
            saveLastPostion(true);
            A.forceRebootToMain = false;
            A.forceRebootToTxt = false;
            T.deleteFile(A.txtPauseOnlyTagFile);
            A.SaveOptions(this);
            finish();
            selfPref = null;
            System.exit(0);
        } catch (Exception e) {
            A.error(e);
        }
    }

    private void saveEBookName() {
        if (BookDb.getBook(A.lastFile) == null) {
            String bookName = null;
            String author = "";
            String description = "";
            String category = "";
            if (getBookType() == 100 && A.ebook != null) {
                bookName = A.ebook.getBookName();
                author = A.ebook.getAuthor();
                description = A.ebook.description;
                category = T.stringList2Text(A.ebook.categories);
            }
            if (getBookType() == 7 && this.m_doc != null) {
                bookName = this.m_doc.GetMeta("Title");
                author = this.m_doc.GetMeta("Author");
            }
            if (bookName != null) {
                BookDb.insertBookToTemp(new BookInfo(bookName, A.lastFile, author, description, category, "", "", "" + System.currentTimeMillis(), "", "", "", "", "", ""));
            }
        }
    }

    public void setScreenBrightness(int brightnessValue, boolean showHint) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (brightnessValue == -100) {
            lp.screenBrightness = -1.0f;
        } else {
            if (brightnessValue > 100) {
                brightnessValue = 100;
            }
            if (brightnessValue < -50) {
                brightnessValue = -50;
            }
            if (showHint) {
                T.showToastText(this, T.forceWhite(brightnessValue + "%"));
            }
            A.brightnessValue = brightnessValue;
            showShadeCoverOnView();
            lp.screenBrightness = (A.brightnessValue > 0 ? (float) A.brightnessValue : 1.0f) / 100.0f;
        }
        this.brightnessSetted = true;
        getWindow().setAttributes(lp);
    }

    private void showShadeCoverOnView() {
        if (this.shadeView != null) {
            if (A.brightnessValue <= 0 || A.bluelightEnable) {
                int r;
                int g;
                int b;
                int i = A.bluelightOpacity;
                int a = A.bluelightEnable ? ((i * 180) / 80) + getShadeAlph() : getShadeAlph();
                if (i > 80) {
                    i = 80;
                }
                if (A.bluelightEnable) {
                    r = 200 - ((i * 190) / 80);
                } else {
                    r = 0;
                }
                if (A.bluelightEnable) {
                    g = 180 - ((i * 170) / 80);
                } else {
                    g = 0;
                }
                if (A.bluelightEnable) {
                    b = 60 - ((i * 60) / 80);
                } else {
                    b = 0;
                }
                if (a > 230) {
                    a = 230;
                }
                this.shadeView.setBackgroundColor(Color.argb(a, r, g, b));
                this.shadeView.setVisibility(0);
            } else if (this.shadeView.getVisibility() != 8) {
                this.shadeView.setVisibility(8);
            }
        }
    }

    public int getShadeAlph() {
        if (A.brightnessValue > 0 || A.brightnessValue == -100) {
            return 0;
        }
        int value = 1 - A.brightnessValue;
        if (value > 50) {
            value = 50;
        }
        return value * 4;
    }

    public void setLeds() {
        if ((A.adjustLed || A.adjustNightLed) && A.brightnessValue != -100) {
            setLedValue(-1.0f, true);
            if ((A.adjustLed && A.brightnessValue <= A.disableLedValue) || (A.adjustNightLed && A.lastTheme.equals(A.NIGHT_THEME))) {
                new Handler() {
                    public void handleMessage(Message msg) {
                        ActivityTxt.this.setLedValue(0.0f, true);
                    }
                }.sendEmptyMessage(0);
                return;
            }
            return;
        }
        setLedValue(-1.0f, false);
    }

    public void setLedValue(float value, boolean force) {
        if (VERSION.SDK_INT >= 8) {
            if (this.LedChanged || force) {
                try {
                    this.LedChanged = true;
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.getClass().getField("buttonBrightness").set(lp, Float.valueOf(value));
                    getWindow().setAttributes(lp);
                } catch (Exception e) {
                    this.LedChanged = false;
                    A.error(e);
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void do_page_up() {
        if (!webViewPageUp() && !A.isInAutoScroll) {
            if (A.flip_animation > 2) {
                prePriorChapterForPageUp();
            }
            pageScroll(-1);
        }
    }

    public void do_page_down() {
        if (!webViewPageDown() && !A.isInAutoScroll) {
            pageScroll(1);
        }
    }

    void do_open_file(String nextFile) {
        if (T.isFile(nextFile)) {
            if (!A.lastFile.equals(nextFile)) {
                saveLastPostion(true);
            }
            A.lastFile = nextFile;
            if (this.pdf != null) {
                restartReaderToTxt();
            }
            A.clearTxts();
            clearTxtView();
            clearpreNextCache();
            initExtras(false);
            if (!isPdf()) {
                A.loadVisualOptions(false);
            }
            this.handler.post(this);
            return;
        }
        T.showToastText(this, nextFile + getString(R.string.not_exists));
    }

    public void restartReaderToMain() {
        restartWhenLowMemory(100, true, 0);
    }

    public void restartReaderToTxt() {
        restartWhenLowMemory(100, false, 0);
    }

    void do_prior_file() {
        int index = A.getFileList().indexOf(A.lastFile);
        if (index > 1) {
            String nextFile = (String) A.getFileList().get(index - 1);
            T.showToastText(this, T.getFilename(nextFile), 1);
            do_open_file(nextFile);
            return;
        }
        T.showToastText(this, getString(R.string.no_prior_file), 1);
    }

    void do_next_file() {
        int index = A.getFileList().indexOf(A.lastFile);
        if (index < A.getFileList().size() - 1) {
            String nextFile = (String) A.getFileList().get(index + 1);
            T.showToastText(this, T.getFilename(nextFile), 1);
            do_open_file(nextFile);
            return;
        }
        T.showToastText(this, getString(R.string.no_next_file), 1);
    }

    private void do_font_size() {
        inverseLayoutVisible(true);
        getFontZoom().setVisibility(0);
    }

    private void do_brightness() {
        inverseLayoutVisible(true);
        initBrightSeek();
        this.brightnessPanel.setVisibility(0);
        setBrightnessSKValue();
        this.bluelightSK.setProgress(A.bluelightOpacity);
    }

    void do_search() {
        inverseLayoutVisible(true);
        if (isPdf()) {
            if (this.pdf.textReflow) {
                pdfTextReflowSwitch(true);
            }
            pdfShowSearchLay();
            return;
        }
        showPrefSearch(null);
    }

    void showPrefSearch(String key) {
        hideDotViews();
        if (!webViewSearch()) {
            if (isPdf()) {
                if (this.pdf.textReflow) {
                    pdfTextReflowSwitch(true);
                }
                pdfShowSearchLay();
                this.pdfSearchEdit.setText(key);
                return;
            }
            new PrefSearch(this, new OnClickResult() {
                public void onClick(int resultId) {
                    if (PrefSearch.results != null) {
                        boolean sameHtmlSrc;
                        ActivityTxt.this.saveLinkBackInfo(true);
                        ActivityTxt.this.hideDotViews();
                        ActivityTxt.this.linkBackSetVisible();
                        ActivityTxt.this.search_result = (SearchResult) PrefSearch.results.get(resultId);
                        SearchResult sr = ActivityTxt.this.search_result;
                        if (A.lastChapter == sr.chapter && A.lastSplitIndex == sr.splitIndex) {
                            sameHtmlSrc = true;
                        } else {
                            sameHtmlSrc = false;
                        }
                        A.lastChapter = sr.chapter;
                        A.lastSplitIndex = sr.splitIndex;
                        int l = ActivityTxt.this.getDisplayTextLength(ActivityTxt.this.txtView, ActivityTxt.this.txtScroll);
                        long j = sr.start > l / 4 ? (long) (sr.start - (l / 4)) : sr.start > 80 ? (long) (sr.start - 80) : (long) sr.start;
                        A.lastPosition = j;
                        switch (ActivityTxt.this.getBookType()) {
                            case 0:
                                ActivityTxt.this.txtView.hStart = (int) A.getTxtDisplayPos((long) sr.start, false);
                                ActivityTxt.this.txtView.hEnd = (int) A.getTxtDisplayPos((long) sr.end, false);
                                ActivityTxt.this.handler.post(ActivityTxt.this);
                                break;
                            case 1:
                            case 100:
                                ActivityTxt.this.txtView.hStart = sr.start;
                                ActivityTxt.this.txtView.hEnd = sr.end;
                                if (!sameHtmlSrc) {
                                    ActivityTxt.this.createProgressDlg(Html.fromHtml(sr.html).toString());
                                    ActivityTxt.this.locate_to_search_result = true;
                                    ActivityTxt.this.handler.post(ActivityTxt.this);
                                    break;
                                }
                                ActivityTxt.this.txtScrollNoDelay(ActivityTxt.this.getSearchResultLocation());
                                break;
                        }
                        ActivityTxt.this.updateProgressStatus();
                    }
                }
            }, key).show();
        }
    }

    protected int getSearchResultLocation() {
        this.locate_to_search_result = false;
        if (this.search_result == null) {
            return (int) A.lastPosition;
        }
        String text = this.txtView.getText2();
        String key = A.lastSearchKey;
        if (!A.searchCaseSensitive) {
            text = text.toLowerCase();
            key = key.toLowerCase();
        }
        int pos = this.search_result.start;
        int i = -1;
        int i1 = text.indexOf(key, pos);
        if (i1 != pos) {
            int i2 = text.lastIndexOf(key, pos);
            if (i2 == -1 || (i1 != -1 && i1 - pos < (pos - i2) * 2)) {
                i = i1;
            } else {
                i = i2;
            }
        }
        if (i == -1 || i == pos || Math.abs(i - pos) >= 1000) {
            return (int) A.lastPosition;
        }
        this.txtView.hStart = i;
        this.txtView.hEnd = this.txtView.hStart + key.length();
        if (i > 80) {
            return i - 80;
        }
        return i;
    }

    void do_show_chapters() {
        String errorMsg = null;
        try {
            switch (getBookType()) {
                case 0:
                case 1:
                    if (A.getTxtChapters().size() == 0) {
                        errorMsg = getString(R.string.no_chapter_found);
                        break;
                    }
                    break;
                case 7:
                    pdfGetToc();
                    if (this.pdfToc.size() == 0) {
                        errorMsg = getString(R.string.no_chapter_found);
                        break;
                    }
                    break;
                case 100:
                    if (A.ebook.getChapters().size() == 0) {
                        errorMsg = getString(R.string.no_chapter_found);
                        break;
                    }
                    break;
            }
            if (errorMsg != null) {
                T.showToastText(this, errorMsg);
                return;
            }
            this.chapterDlg = new PrefChapters(this, new OnChangeChapter() {
                public void onGetChapter(int chapterId) {
                    ActivityTxt.this.saveLinkBackInfo(true);
                    if (ActivityTxt.this.isPdf()) {
                        A.lastPosition = (long) ((TocChapter) ActivityTxt.this.pdfToc.get(chapterId)).pageNumber;
                        ActivityTxt.this.pdfGotoPage((int) A.lastPosition, true);
                        ActivityTxt.this.showReadProgress(0);
                        return;
                    }
                    A.lastSplitIndex = 0;
                    switch (ActivityTxt.this.getBookType()) {
                        case 0:
                        case 1:
                            A.lastPosition = A.getTxtChapterDisplayPosition(chapterId);
                            ActivityTxt.this.handler.post(ActivityTxt.this);
                            ActivityTxt.this.checkStatusBar();
                            ActivityTxt.this.resetFlipCache(false, 1000);
                            break;
                        case 100:
                            if (ActivityTxt.this.oldPriorSplitHtmls != null && ActivityTxt.this.oldPriorSplitHtmls.size() > 1) {
                                ActivityTxt.this.oldPriorChapterText = null;
                            }
                            A.lastChapter = chapterId;
                            A.lastPosition = 0;
                            ActivityTxt.this.clearTxtView();
                            A.SaveOptions(ActivityTxt.this);
                            if (ActivityTxt.this.isBigChapter(A.lastChapter)) {
                                ActivityTxt.this.createProgressDlg(((Chapter) A.ebook.getChapters().get(A.lastChapter)).name);
                            }
                            ActivityTxt.this.ebookPageUp = false;
                            ActivityTxt.this.handler.post(ActivityTxt.this);
                            break;
                    }
                    if (A.immersive_fullscreen && ActivityTxt.this.dualPageEnabled()) {
                        ActivityTxt.this.handler.sendEmptyMessageDelayed(ActivityTxt.CHECK_DUAL_PAGE, 500);
                    }
                }
            });
            this.chapterDlg.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    A.setSystemUiVisibility(true);
                    ActivityTxt.this.chapterDlg = null;
                }
            });
            this.chapterDlg.show();
            A.moveStart = false;
        } catch (Exception e) {
            A.error(e);
        }
    }

    private void createBookmark() {
        Bookmarks bms;
        int id = A.getBookmarksId(A.lastFile);
        if (id == -1) {
            bms = new Bookmarks(A.lastFile);
        } else {
            bms = (Bookmarks) A.getBookmarks().get(id);
        }
        if (id == -1) {
            A.getBookmarks().add(bms);
        }
        if (isPdf()) {
            A.lastPosition = (long) pdfGetCurrPageNo();
        }
        saveLastPostion(false);
        boolean exist = false;
        Iterator it = bms.list.iterator();
        while (it.hasNext()) {
            Bookmark bm = (Bookmark) it.next();
            if (bm.position == A.lastPosition && bm.chapter == A.lastChapter && bm.splitIndex == A.lastSplitIndex) {
                bms.remove(bm);
                exist = true;
                break;
            }
        }
        if (!exist) {
            String bookmark = "(" + (isPdf() ? (A.lastPosition + 1) + "/" + this.m_doc.GetPageCount() : getPercentStr2()) + ") " + generateBookmarkName();
            if (A.bookmarkManually) {
                PrefEditBookmark prefEditBookmark = new PrefEditBookmark(this, bookmark, new OnAfterEdit() {
                    public void AfterEdit(String result, Integer editColor) {
                        ActivityTxt.this.createBookmark2(bms, result);
                        if (!ActivityTxt.this.updateForFitHardwareAccelerate()) {
                            ActivityTxt.this.contentLay.postInvalidate();
                        }
                    }
                }, null);
            } else {
                createBookmark2(bms, bookmark);
            }
        }
        A.hasNewNoteForUpload = true;
        A.lastFileAnnotUpdated = true;
        if (!updateForFitHardwareAccelerate()) {
            this.contentLay.postInvalidate();
        }
    }

    private void createBookmark2(Bookmarks bms, String bookmark) {
        Bookmark bm = new Bookmark(A.lastFile, bookmark, A.lastChapter, A.lastSplitIndex, A.lastPosition, System.currentTimeMillis(), A.getBookmarkColor());
        bms.list.add(bm);
        BookDb.addBookmark(bm);
        T.showToastText(this, getString(R.string.add_bookmark), bookmark, 0);
    }

    private void forceUpdateForFitHardwareAccelerate() {
        if (A.fitHardwareAccelerated) {
            this.handler.sendEmptyMessageDelayed(FORCEFITHARDWAREACCELERATE, 10);
        }
    }

    private boolean updateForFitHardwareAccelerate() {
        if (!A.fitHardwareAccelerated) {
            return false;
        }
        if (this.pdf != null) {
            this.pdf.invalidate();
        } else if (this.isPaused || A.screenState != 0 || this.txtView.getText().length() <= 0 || this.txtView.getLineCount() != 0) {
            this.txtView.invalidate();
            this.txtView2.invalidate();
        } else {
            A.log("******txtView.getText().length() > 0 && txtView.getLineCount() == 0******", true);
            clearTxtView();
            reloadBookOnError();
            this.ignoreSavePosTime = System.currentTimeMillis();
            T.showToastText(this, getString(R.string.loading) + "...");
            this.handler.sendEmptyMessageDelayed(FORCEFITHARDWAREACCELERATE, 1000);
            return false;
        }
        this.contentLay.postInvalidate();
        return true;
    }

    void do_bookmark() {
        if (this.web == null) {
            try {
                saveLinkBackInfo(false);
                Intent i = new Intent(this, BookMarkAct.class);
                i.putExtra("bookFile", A.lastFile);
                saveLastPostion(true);
                String bookmark = generateBookmarkName();
                String progress = isPdf() ? (A.lastPosition + 1) + "/" + this.m_doc.GetPageCount() : getPercentStr2();
                i.putExtra("bookmark", bookmark);
                i.putExtra("position", A.lastPosition);
                i.putExtra("chapter", A.lastChapter);
                i.putExtra("splitIndex", A.lastSplitIndex);
                i.putExtra("progress", progress);
                inverseLayoutVisible(true);
                A.lastFileFromBookmark = null;
                startActivityForResult(i, 1);
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    private String generateBookmarkName() {
        String bookmark;
        int addLength = A.isChinese ? 38 : 78;
        if (isPdf()) {
            bookmark = pdfCreateReflowTextForShow((long) pdfGetCurrPageNo()).trim();
            if (bookmark.length() > addLength) {
                bookmark = bookmark.substring(0, addLength);
            }
            if (bookmark.length() == 0) {
                bookmark = "" + A.lastPosition;
            }
        } else {
            int offset = this.txtView.getLayout().getLineStart(this.txtView.getLayout().getLineForVertical(this.txtScroll.getScrollY()));
            bookmark = this.txtView.getText2();
            if (bookmark.length() > offset + addLength) {
                bookmark = bookmark.substring(offset, offset + addLength);
                int i = bookmark.lastIndexOf(" ");
                if (bookmark.length() - i < 8) {
                    bookmark = bookmark.substring(0, i);
                }
            } else if (bookmark.length() < 3) {
                bookmark = T.dateTimeToStr(Long.valueOf(System.currentTimeMillis()));
            } else {
                bookmark = bookmark.substring(offset, bookmark.length());
            }
        }
        return bookmark.replace("\n", " ").replace(A.INDENT_CHAR, ' ').trim() + "...";
    }

    public long getBookLength() {
        try {
            switch (getBookType()) {
                case 1:
                    return this.web != null ? (long) this.web.getContentHeight() : (long) this.htmlText.length();
                case 7:
                    return (long) this.m_doc.GetPageCount();
                case 100:
                    return getEBookTotalSize();
                default:
                    return A.txtLength();
            }
        } catch (Exception e) {
            A.error(e);
            return 1;
        }
    }

    private long getEBookTotalSize() {
        int textSize = BaseEBook.getBookCharCountIfComplete(-1);
        return textSize > 0 ? (long) textSize : A.ebook.getTotalSize();
    }

    private long getCurrentPosition() {
        try {
            switch (getBookType()) {
                case 1:
                    if (this.web != null) {
                        return (long) this.web.getScrollY();
                    }
                    if (A.noSplitHtmls()) {
                        return (((long) this.htmlSrc.length()) * ((long) this.txtScroll.getScrollY())) / ((long) this.txtView.getRealHeight());
                    }
                    return ((long) (A.lastSplitIndex * A.maxHtmlChapterSize)) + ((((long) this.htmlSrc.length()) * ((long) this.txtScroll.getScrollY())) / ((long) this.txtView.getRealHeight()));
                case 100:
                    if (this.web != null) {
                        return (long) (BaseEBook.getPriorTextLength2(A.lastChapter) + this.web.getScrollY());
                    }
                    int bookSize = BaseEBook.getBookCharCountIfComplete(-1);
                    if (bookSize > 0) {
                        Chapter c = (Chapter) A.ebook.getChapters().get(A.lastChapter);
                        int bookLeft = BaseEBook.getBookCharCountIfComplete(A.lastChapter);
                        int line = this.txtView.getLayout().getLineForVertical(this.txtScroll.getScrollY());
                        long chapterSize = (long) this.txtView.getText().length();
                        if (chapterSize == 0) {
                            chapterSize = 1;
                        }
                        long chapterLeft = chapterSize - ((long) this.txtView.getLayout().getLineStart(line));
                        if (!A.noSplitHtmls()) {
                            if (A.lastChapter == 0 && A.lastSplitIndex == 0 && A.lastPosition == 0) {
                                return 0;
                            }
                            long left = 0;
                            long total = 0;
                            for (int i = 0; i < A.splitHtmls.size(); i++) {
                                total += (long) ((String) A.splitHtmls.get(i)).length();
                                if (i > A.lastSplitIndex) {
                                    left += (long) ((String) A.splitHtmls.get(i)).length();
                                }
                            }
                            chapterSize = total;
                            chapterLeft += left;
                        }
                        long result = ((long) (bookSize - bookLeft)) - ((((long) c.pure_text_length) * chapterLeft) / chapterSize);
                        if (result <= 0) {
                            return 0;
                        }
                        return result;
                    }
                    long tp = A.lastPosition;
                    if (!A.noSplitHtmls()) {
                        tp = ((long) (A.lastSplitIndex * A.maxHtmlChapterSize)) + ((((long) this.txtView.getText().length()) * ((long) this.txtScroll.getScrollY())) / ((long) this.txtView.getRealHeight()));
                    } else if (A.ebook.isHtml() && this.txtView.getText().length() != 0) {
                        tp = (((Chapter) A.ebook.getChapters().get(A.lastChapter)).size * tp) / ((long) this.txtView.getText().length());
                    }
                    return ((long) BaseEBook.getPriorTextLength2(A.lastChapter)) + tp;
                default:
                    return A.lastPosition;
            }
        } catch (OutOfMemoryError e) {
            A.error(e);
            return 0;
        } catch (Exception e2) {
            A.error(e2);
            return 0;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 21) {
            String account = null;
            if (!(resultCode != -1 || data == null || data.getExtras() == null)) {
                account = data.getStringExtra("authAccount");
            }
            if (account != null) {
                A.getGdrive().saveAccount(account);
                A.getGdrive().initService();
                A.getGdrive().checkAccountAuthThread();
                return;
            }
            A.syncType = 0;
            if (PrefMisc.syncGdriveCheck != null) {
                PrefMisc.syncGdriveCheck.setChecked(false);
            }
        } else if (requestCode != 22) {
            if (requestCode == 1) {
                try {
                    if (A.lastFileFromBookmark != null) {
                        if (A.lastFileFromBookmark.equals(A.lastFile)) {
                            if (this.tmpPositionInfo != null && this.link_backs.indexOf(this.tmpPositionInfo) == -1) {
                                this.link_backs.add(0, this.tmpPositionInfo);
                            }
                            switch (getBookType()) {
                                case 0:
                                    showTxtByPosition(A.lastPosition, null);
                                    updateProgressStatus();
                                    break;
                                case 1:
                                    if (!A.noSplitHtmls()) {
                                        do_open_file2(A.lastFileFromBookmark);
                                        break;
                                    }
                                    txtScrollTo(this.txtView.getLineTop2(this.txtView.getLayout().getLineForOffset((int) A.lastPosition)));
                                    updateProgressStatus();
                                    break;
                                case 7:
                                    pdfGotoPage((int) A.lastPosition, true);
                                    updateProgressStatus();
                                    break;
                                case 100:
                                    this.isTxtScrollReady = false;
                                    this.ebookPageUp = false;
                                    showEBookByPosition(A.lastChapter, A.lastSplitIndex, A.lastPosition, true);
                                    this.isTxtScrollReady = true;
                                    break;
                            }
                        }
                        do_open_file2(A.lastFileFromBookmark);
                    } else if (getBookType() == 0 && dualPageEnabled()) {
                        showTxtByPosition(A.lastPosition, null);
                    }
                    resetFlipCache(true, 500);
                } catch (Exception e) {
                    A.error(e);
                    return;
                }
            }
            if (requestCode == 123 && A.noMenuKey) {
                T.showAlertText(this, getString(R.string.tip), getString(R.string.no_menu_key));
            }
            if (requestCode == ModelPPM.MAX_FREQ) {
                this.isPaused = false;
                this.contentLay.postInvalidate();
            }
        } else if (resultCode == -1) {
            A.syncType = 4;
            A.downloadCloudPositionFile();
        } else {
            A.getGdrive().showAccountPickDialog();
        }
    }

    private void do_open_file2(String filename) {
        Message msg = new Message();
        msg.obj = filename;
        new Handler() {
            public void handleMessage(Message msg) {
                ActivityTxt.this.do_open_file((String) msg.obj);
            }
        }.sendMessage(msg);
    }

    void do_book_info() {
        String text;
        String time;
        String speed;
        try {
            hideProgressDlg();
            String author;
            StringBuilder append;
            String str;
            if (!isPdf()) {
                long current;
                long total;
                long l = getBookLength();
                if (getBookType() == 100 || (getBookType() == 0 && A.getTxts().size() > 3)) {
                    current = getAboutCurrentPage(0);
                    total = getTotalPages() / 100000;
                    if (total == 0) {
                        total = 1;
                    }
                } else {
                    total = (long) ((this.txtView.getRealHeight() / A.getPageHeight()) + 1);
                    current = ((A.lastPosition * total) / l) + 1;
                }
                text = "<b>" + getString(R.string.filename) + ":</b>  " + T.getFilename(A.lastFile) + "<br><b>" + getString(R.string.location) + ":</b>  " + T.getFilePath(A.lastFile) + "<br><b>" + getString(R.string.file_size) + ":</b>  " + Formatter.formatFileSize(this, new File(A.lastFile).length()) + "<br><b>" + getString(R.string.total_pages) + ":</b>  " + total + "<br><b>" + getString(R.string.current_page) + ":</b>  " + current;
                switch (getBookType()) {
                    case 0:
                    case 1:
                        if (A.getTxtChapters().size() > 0) {
                            text = text + "<br><b>" + getString(R.string.total_chapters) + ":</b> " + A.getTxtChapters().size() + "<br><b>" + getString(R.string._current_chapter) + ":</b> " + (A.getChapterId(A.lastPosition) + 1) + "<br>" + ((TxTChapter) A.getTxtChapters().get(A.getChapterId(A.lastPosition))).chapter_trim;
                            break;
                        }
                        break;
                    case 100:
                        String serial = BookDb.createSerialText(T.stringList2Text(A.ebook.categories));
                        author = A.ebook.getAuthor();
                        append = new StringBuilder().append("<b>").append(A.ebook.getBookName()).append("</b>");
                        if (serial != null) {
                            str = "<br>" + serial + (author.equals("") ? "" : "<br>" + author);
                        } else {
                            str = author.equals("") ? "" : " - " + author;
                        }
                        append = append.append(str);
                        str = (A.ebook.description == null || A.ebook.description.equals("")) ? "" : "<br><br><i>" + A.ebook.description + "</i>";
                        text = append.append(str).append("<br><br>").append(text).append("<br><b>").append(getString(R.string.total_chapters)).append(":</b> ").append(A.ebook.getChapters().size()).append("<br><b>").append(getString(R.string._current_chapter)).append(":</b> ").append(A.lastChapter + 1).append(", ").append(((Chapter) A.ebook.getChapters().get(A.lastChapter)).name).toString();
                        break;
                    default:
                        break;
                }
            }
            author = this.m_doc.GetMeta("Author");
            append = new StringBuilder().append("<b>").append(A.getBookName()).append("</b>");
            str = (author == null || author.length() == 0) ? "" : " - " + author;
            text = append.append(str).append("<br><br><b>").append(getString(R.string.filename)).append(":</b>  ").append(T.getFilename(A.lastFile)).append("<br><b>").append(getString(R.string.location)).append(":</b>  ").append(T.getFilePath(A.lastFile)).append("<br><b>").append(getString(R.string.file_size)).append(":</b>  ").append(Formatter.formatFileSize(this, new File(A.lastFile).length())).append("<br><b>").append(getString(R.string.total_pages)).append(":</b>  ").append(this.m_doc.GetPageCount()).append("<br><b>").append(getString(R.string.current_page)).append(":</b>  ").append(pdfGetCurrPageNo() + 1).append("<br><b>Creator:</b>  ").append(this.m_doc.GetMeta("Creator")).append("<br><b>Subject:</b>  ").append(this.m_doc.GetMeta("Subject")).append("<br><b>Keywords:</b>  ").append(this.m_doc.GetMeta("Keywords")).append("<br><b>Producer:</b>  ").append(this.m_doc.GetMeta("Producer")).append("<br><b>Version:</b>  ").append(this.m_doc.GetMeta("ver")).toString();
            time = "0";
            speed = "0";
            saveStatisticsInfo();
            ReadStatistics r = BookDb.getSavedStatistics(A.lastFile);
            if (r.usedTime > 0 && r.readWords > 0) {
                time = "" + new DecimalFormat("0.0").format((double) (((((float) r.usedTime) / 60.0f) / 60.0f) / 1000.0f));
                float minutes = (float) ((r.usedTime / 60) / 1000);
                if (minutes < 1.0f) {
                    minutes = 1.0f;
                }
                speed = "" + new DecimalFormat("0").format((double) (((float) r.readWords) / minutes));
            }
        } catch (Exception e) {
            A.error(e);
        } catch (Throwable e2) {
            System.gc();
            A.error(e2);
            return;
        }
        this.info_text = text + "<br><b>" + getString(R.string.read_speed) + ": </b>" + speed + "<br><b>" + getString(R.string.read_hour) + ":</b> " + time + "<br><b>" + getString(R.string.chars_in_book) + ":</b>  ";
        this.info_sv = LayoutInflater.from(this).inflate(R.layout.book_info, null);
        ((TextView) this.info_sv.findViewById(R.id.rdmTextView01)).setText(Html.fromHtml(this.info_text));
        ImageView iv = (ImageView) this.info_sv.findViewById(R.id.imageView1);
        Drawable d = A.getBookCover(A.lastFile);
        if (d != null) {
            iv.setImageDrawable(d);
            iv.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(ActivityTxt.this, PicGalleryShow.class);
                    if (A.ebook == null || A.ebook.getCoverFile() == null) {
                        i.putExtra("imageFile", A.getBookCoverFile(A.lastFile));
                        i.putExtra("singPicOnly", true);
                    } else {
                        i.putExtra("ebookImage", A.ebook.getCoverFile());
                    }
                    ActivityTxt.this.startActivity(i);
                }
            });
        } else {
            iv.setVisibility(8);
        }
        int textSize = -1;
        switch (getBookType()) {
            case 0:
                textSize = BaseEBook.getTxtUnReadWordCount(new AfterGetUnReadWords() {
                    public void refresh(int wordCount) {
                        ActivityTxt.this.handler.sendEmptyMessage(ActivityTxt.BOOK_INFO_COUNT_DONE);
                    }
                }, true);
                break;
            case 1:
                do_book_info_count_done();
                break;
            case 7:
                textSize = BaseEBook.getPdfUnReadWordCoun(new AfterGetUnReadWords() {
                    public void refresh(int wordCount) {
                        ActivityTxt.this.handler.sendEmptyMessage(ActivityTxt.BOOK_INFO_COUNT_DONE);
                    }
                }, true);
                break;
            case 100:
                textSize = BaseEBook.getUnReadWordCountFrom(A.ebook, 0, new AfterGetUnReadWords() {
                    public void refresh(int wordCount) {
                        ActivityTxt.this.handler.sendEmptyMessage(ActivityTxt.BOOK_INFO_COUNT_DONE);
                    }
                }, true);
                break;
        }
        if (textSize > 0) {
            do_book_info_count_done();
        }
        A.setDialogNightState(this.info_sv);
        Builder dlg =new AlertDialog.Builder(this);
        dlg.setTitle((CharSequence) getString(R.string.book_info)).setView(this.info_sv);
        dlg.setPositiveButton((CharSequence) getString(R.string.add_to_favorites), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityTxt.this.do_add_favorite();
            }
        });
        dlg.setNeutralButton((CharSequence) getString(R.string.more) + "... ", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            private String createTitle(String title) {
                return title + " " + ActivityTxt.this.getString(R.string.book_information);
            }

            public void onClick(DialogInterface dialog, int which) {
                int i;
                if (A.isProVersion) {
                    i = 10;
                } else {
                    i = 9;
                }
                CharSequence[] items = new String[i];
                items[0] = createTitle(A.isChinesePRC ? "百度" : "Google");
                items[1] = createTitle("Wikipedia");
                items[2] = createTitle("Facebook");
                items[3] = createTitle("Twitter");
                items[4] = createTitle("GoodReads");
                items[5] = "-";
                items[6] = ActivityTxt.this.getString(R.string.share);
                items[7] = ActivityTxt.this.getString(R.string.send_file);
                items[8] = ActivityTxt.this.getString(R.string.clear_dict_history) + " \"" + ActivityTxt.this.getString(R.string.read_statistics) + "\"";
                if (A.isProVersion) {
                    items[9] = ActivityTxt.this.getString(R.string.add_to_desktop);
                }
               new AlertDialog.Builder(ActivityTxt.this).setItems(items, new DialogInterface.OnClickListener() {
                    private void searchWeb(String url) {
                        ActivityTxt.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url + ActivityTxt.this.getShareTitle(1))));
                    }

                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            searchWeb(A.isChinesePRC ? "http://www.baidu.com/s?ie=utf-8&wd=" : "http://www.google.com/search?q=");
                        }
                        if (which == 1) {
                            ActivityTxt.this.openDictUrl(A.getWikiUrl(), A.getBookName());
                        }
                        if (which == 2) {
                            searchWeb("http://www.facebook.com/search/results.php?q=");
                        }
                        if (which == 3) {
                            searchWeb("https://twitter.com/search/");
                        }
                        if (which == 4) {
                            searchWeb("http://www.goodreads.com/search?query=");
                        }
                        if (which == 6) {
                            ActivityTxt.this.doShareReadState();
                        }
                        if (which == 7) {
                            A.sendFile(ActivityTxt.this, A.lastFile);
                        }
                        if (which == 8) {
                            BookDb.deleteStatistics(A.lastFile);
                            ActivityTxt.this.statisticsTime = SystemClock.elapsedRealtime();
                            ActivityTxt.this.showReadProgress(0);
                        }
                        if (which == 9) {
                            ActivityMain.do_add_desktop(ActivityTxt.this, A.lastFile, A.getBookName());
                        }
                    }
                }).show();
            }
        });
        dlg.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                ActivityTxt.this.info_sv = null;
                ActivityTxt.this.info_text = null;
            }
        });
        dlg.show();
        inverseLayoutVisible(true);
    }

    private void do_book_info_count_done() {
        if (!isFinishing() && this.info_sv != null && this.info_text != null) {
            try {
                long word_count = (long) BaseEBook.getBookWordCountIfComplete();
                String size_text = this.pdf == null ? NumberFormat.getInstance().format(getBookLength()) : "";
                if (word_count > 0) {
                    size_text = T.buildString(size_text, "<br><b>", getString(R.string.words_in_book), ":</b>  ", NumberFormat.getInstance().format(word_count));
                    int book_left = getBookLeftWords();
                    int chapter_left = getChapterLeftWords();
                    if (chapter_left > 0) {
                        book_left += chapter_left;
                    }
                    if (book_left > 0) {
                        int min = book_left / getReadingSpeed();
                        int book_hour = min / 60;
                        int book_min = min % 60;
                        String chapterLeft = (A.ebook == null || chapter_left <= 0) ? "<br>" : "<br><br>" + String.format(getString(R.string.remaining_time_chapter) + "", new Object[]{Integer.valueOf(chapter_left / getReadingSpeed())});
                        String[] strArr = new String[6];
                        strArr[0] = size_text;
                        strArr[1] = "<i>";
                        strArr[2] = chapterLeft;
                        strArr[3] = "<br>";
                        strArr[4] = String.format(getString(R.string.remaining_time_book), new Object[]{Integer.valueOf(book_hour), Integer.valueOf(book_min)});
                        strArr[5] = "</i>";
                        size_text = T.buildString(strArr);
                    }
                }
                ((TextView) this.info_sv.findViewById(R.id.rdmTextView01)).setText(Html.fromHtml(this.info_text + size_text));
            } catch (Throwable e) {
                System.gc();
                A.error(e);
            }
            this.info_sv.findViewById(R.id.progressBar3).setVisibility(8);
        }
    }

    protected void do_add_favorite() {
        BookInfo bi = BookDb.getBook(A.lastFile);
        if (bi == null) {
            bi = BookDb.createBookInfoFromFile(BookDb.DEFAULT_FAV, A.lastFile, false);
        } else if (bi.favorite.equals("")) {
            bi.favorite = BookDb.DEFAULT_FAV;
        }
        if (bi != null) {
            PrefEditBook dlg = new PrefEditBook(this, new OnBookEdited() {
                public void onSaveBookInfo() {
                }
            }, false, bi);
            dlg.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    A.setSystemUiVisibility(true);
                }
            });
            dlg.show();
        }
    }

    void do_options_menu() {
        showOptionsMenu(false);
    }

    void do_change_theme() {
        if (!A.isInAutoScroll) {
            showThemeWindow();
        }
    }

    void do_disable() {
    }

    void do_none() {
    }

    boolean doKeyDown(int keyCode) {
        switch (keyCode) {
            case 3:
                if (doEvent(A.doHomeKey)) {
                    return true;
                }
                break;
            case 4:
                if (!(A.isInAutoScroll || A.isSpeaking)) {
                    boolean result = false;
                    if (navigateBackLinks()) {
                        result = true;
                    }
                    if (doEvent(A.doBackKey)) {
                        result = true;
                    }
                    if (result) {
                        return true;
                    }
                }
                break;
            case 19:
                if (doEvent(A.doDPadUp)) {
                    return true;
                }
                break;
            case 20:
                if (doEvent(A.doDPadDown)) {
                    return true;
                }
                break;
            case 21:
                if (doEvent(A.doDPadLeft)) {
                    return true;
                }
                break;
            case 22:
                if (doEvent(A.doDPadRight)) {
                    return true;
                }
                break;
            case 23:
                if (doEvent(A.doDPadCenter)) {
                    return true;
                }
                break;
            case 24:
            case 92:
            case SHOW_PDF /*105*/:
                if (!this.layoutVisible) {
                    if (A.noMenuKey && A.fullscreen && (A.doVolumeKeyUp == 0 || A.doVolumeKeyUp == 1)) {
                        hideSystemUi();
                    }
                    if (A.doVolumeKeyUp == 20) {
                        setScreenBrightness(A.brightnessValue + 1, true);
                        return true;
                    } else if (doEvent(A.doVolumeKeyUp)) {
                        return true;
                    }
                }
                break;
            case 25:
            case 93:
            case SHOW_PDF2 /*106*/:
                if (!this.layoutVisible) {
                    if (A.noMenuKey && A.fullscreen && (A.doVolumeKeyDown == 1 || A.doVolumeKeyDown == 0)) {
                        hideSystemUi();
                    }
                    if (A.doVolumeKeyUp == 20) {
                        setScreenBrightness(A.brightnessValue - 1, true);
                        return true;
                    } else if (doEvent(A.doVolumeKeyDown)) {
                        return true;
                    }
                }
                break;
            case 27:
                if (doEvent(A.doCameraKey)) {
                    return true;
                }
                break;
            case 82:
                if (doEvent(A.doMenuKey)) {
                    return true;
                }
                break;
            case 84:
                if (A.isSpeaking && A.doSearchKey != 19) {
                    showTtsPanel(true);
                    return true;
                } else if (doEvent(A.doSearchKey)) {
                    return true;
                }
                break;
        }
        return false;
    }

    void doTouchDown(int id) {
        switch (id) {
            case 1:
                doEvent(A.doTapScreenTop);
                return;
            case 2:
                doEvent(A.doTapScreenBottom);
                return;
            case 3:
                doEvent(A.doTapScreenLeft);
                return;
            case 4:
                doEvent(A.doTapScreenRight);
                return;
            default:
                return;
        }
    }

    public boolean doEvent2(int action) {
        if (action == 5) {
            createBookmark();
        } else {
            doEvent(action);
        }
        return true;
    }

    public boolean doEvent(int action) {
        boolean z = false;
        if (action == 15) {
            return false;
        }
        switch (action) {
            case 0:
                do_page_up();
                break;
            case 1:
                do_page_down();
                break;
            case 2:
                do_prior_file();
                break;
            case 3:
                do_next_file();
                break;
            case 4:
                do_search();
                break;
            case 5:
                do_bookmark();
                break;
            case 6:
                do_book_info();
                break;
            case 7:
                do_change_theme();
                break;
            case 8:
                do_options_menu();
                break;
            case 9:
                showMoreOptionsMenu(false);
                break;
            case 10:
                do_font_size();
                break;
            case 11:
                do_show_chapters();
                break;
            case 12:
                do_prior_chapter();
                break;
            case 13:
                do_next_chapter();
                break;
            case 14:
                do_back_to_book_stack();
                break;
            case 16:
                terminate_reader();
                break;
            case 17:
                do_text_select(isSingleTapForTextSelection());
                break;
            case 18:
                if (!A.isInAutoScroll) {
                    z = true;
                }
                do_AutoScroll(z);
                break;
            case 19:
                do_speak();
                break;
            case 20:
                do_brightness();
                break;
            case 21:
                showVisualOptions();
                break;
            case 22:
                showControlOptions();
                break;
            case 23:
                showMiscOptions();
                break;
        }
        return true;
    }

    void setScreenOrientation() {
        setRequestedOrientation(A.getScreenOrientation(A.screenState));
    }

    private void generateNextScreenOrientation() {
        int i = 2;
        int o = getResources().getConfiguration().orientation;
        if (VERSION.SDK_INT >= 9) {
            A.screenState = o == 2 ? 6 : 5;
            return;
        }
        if (o != 2) {
            i = 1;
        }
        A.screenState = i;
    }

    private void registerHeadset() {
        if (this.headsetRegistered || !A.isProVersion) {
            return;
        }
        if (A.doHeadsetKey != 15 || A.doMediaPlayPause != 15 || A.doMediaPlayNext != 15 || A.doMediaPlayPrevious != 15) {
            try {
                ((AudioManager) getSystemService("audio")).registerMediaButtonEventReceiver(new ComponentName(getPackageName(), OpenFile_Receiver.class.getName()));
                if (VERSION.SDK_INT >= 14) {
                    this.headsetReceiver = new OpenFile_Receiver();
                    registerReceiver(this.headsetReceiver, new IntentFilter("android.intent.action.HEADSET_PLUG"));
                }
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    private void unregisterHeadset() {
        if (this.headsetRegistered) {
            this.headsetRegistered = false;
            try {
                ((AudioManager) getSystemService("audio")).unregisterMediaButtonEventReceiver(this.mediaButtonReciver);
                if (this.headsetReceiver != null) {
                    unregisterReceiver(this.headsetReceiver);
                }
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    private void registerAudioFocus() {
        if (!A.disableAudioFocus) {
            AudioManager am = (AudioManager) getSystemService("audio");
            this.audio_pause_from_listener = false;
            this.audio_focus_lossed = false;
            if (this.audioChangeListener == null) {
                this.audioChangeListener = new OnAudioFocusChangeListener() {
                    public void onAudioFocusChange(int focusChange) {
                        if (!A.disableAudioFocus) {
                            A.log("##onAudioFocusChange: " + focusChange);
                            if (focusChange == -3) {
                                return;
                            }
                            if (focusChange == -2) {
                                if (A.isSpeaking && !ActivityTxt.this.tts_paused) {
                                    ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_play, false);
                                    ActivityTxt.this.audio_pause_from_listener = true;
                                }
                            } else if (focusChange == 1) {
                                if (ActivityTxt.this.tts_paused && ActivityTxt.this.audio_pause_from_listener) {
                                    ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_play, false);
                                }
                            } else if (focusChange == -1 && A.isSpeaking && !ActivityTxt.this.tts_paused) {
                                ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_play, false);
                                ActivityTxt.this.audio_pause_from_listener = true;
                            }
                        }
                    }
                };
            }
            A.log("##requestAudioFocus:" + am.requestAudioFocus(this.audioChangeListener, 3, 1));
        }
    }

    private void unregisterAudioFocus() {
        if (this.audioChangeListener != null) {
            ((AudioManager) getSystemService("audio")).abandonAudioFocus(this.audioChangeListener);
            this.audioChangeListener = null;
        }
    }

    public Handler getReceiveHandler() {
        if (this.receiveHandler == null) {
            this.receiveHandler = new Handler() {
                public void handleMessage(Message msg) {
                    Integer key = msg.obj;
                    A.log("received key:" + key + ", msg.waht:" + msg.what);
                    if (msg.what == 1) {
                        switch (key.intValue()) {
                            case 79:
                                if (A.doHeadsetKey == 19) {
                                    if (!A.isSpeaking) {
                                        ActivityTxt.this.do_speak();
                                        break;
                                    } else {
                                        ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_play, false);
                                        break;
                                    }
                                }
                                ActivityTxt.this.doEvent2(A.doHeadsetKey);
                                break;
                            case 85:
                            case TransportMediator.KEYCODE_MEDIA_PLAY /*126*/:
                            case TransportMediator.KEYCODE_MEDIA_PAUSE /*127*/:
                                if (A.doMediaPlayPause == 19) {
                                    if (!A.isSpeaking) {
                                        ActivityTxt.this.do_speak();
                                        break;
                                    } else {
                                        ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_play, false);
                                        break;
                                    }
                                }
                                ActivityTxt.this.doEvent2(A.doMediaPlayPause);
                                break;
                            case 87:
                                if (!A.isSpeaking) {
                                    ActivityTxt.this.doEvent2(A.doMediaPlayNext);
                                    break;
                                } else {
                                    ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_next, false);
                                    break;
                                }
                            case 88:
                                if (!A.isSpeaking) {
                                    ActivityTxt.this.doEvent2(A.doMediaPlayPrevious);
                                    break;
                                } else {
                                    ActivityTxt.this.do_tts_buttons(ActivityTxt.this.tts_prior, false);
                                    break;
                                }
                        }
                    }
                    if (msg.what != 2 || key.intValue() != 0 || !A.isSpeaking) {
                        return;
                    }
                    if (ActivityTxt.this.stopTtsFromCalling == 2) {
                        ActivityTxt.this.stopTtsFromCalling = 0;
                    } else if (SystemClock.elapsedRealtime() - ActivityTxt.this.resumeTime > 3000 && SystemClock.elapsedRealtime() - ActivityTxt.this.pausedTime > 2000) {
                        ActivityTxt.this.stop_speak();
                    }
                }
            };
        }
        return this.receiveHandler;
    }

    private void registerHardwares() {
        if (this.registerHardwaresTime == -1) {
            new Thread() {
                public void run() {
                    try {
                        ActivityTxt.this.registerHardwaresTime = SystemClock.elapsedRealtime();
                        ActivityTxt.this.registerHeadset();
                        ActivityTxt.this.registerShakeSensor();
                        ActivityTxt.this.registerHardwaresTime = -1;
                        ActivityTxt.this.headsetRegistered = true;
                        if (!A.tilt_turn_page) {
                            return;
                        }
                        if (A.askForTiltAction) {
                            ActivityTxt.this.handler.sendEmptyMessageDelayed(902, 100);
                        } else {
                            ActivityTxt.this.shakeSensorLisener.tilt_turn_page = true;
                        }
                    } catch (Exception e) {
                        A.error(e);
                    }
                }
            }.start();
        } else if (SystemClock.elapsedRealtime() - this.registerHardwaresTime > 2000) {
            this.headsetRegistered = false;
            A.doShakePhone = 15;
            A.SaveOptions(this);
        }
    }

    public void registerShakeSensor() {
        if (A.doShakePhone != 15 || A.tilt_turn_page) {
            if (this.sensorMgr == null) {
                this.sensorMgr = (SensorManager) getSystemService("sensor");
            }
            this.sensorMgr.registerListener(getShakeSensorLisener(), this.sensorMgr.getDefaultSensor(1), 3);
        }
    }

    public void unregisterShakeSensor() {
        if (this.sensorMgr != null) {
            this.sensorMgr.unregisterListener(getShakeSensorLisener());
        }
    }

    private MyShakeSensorListener getShakeSensorLisener() {
        if (this.shakeSensorLisener == null) {
            this.shakeSensorLisener = new MyShakeSensorListener();
        }
        return this.shakeSensorLisener;
    }

    private boolean doHorizontalSwipe(final boolean fromLeftToRight) {
        if ((fromLeftToRight && A.doSwipeLeftToRight != 15) || !(fromLeftToRight || A.doSwipeRightToLeft == 15)) {
            this.xFlingTime = 1;
            this.swipeGuesture = true;
        }
        if (A.askForSwipeEvent && A.doSwipeLeftToRight == 3 && A.doSwipeRightToLeft == 2) {
            String hint;
            if (fromLeftToRight) {
                hint = getString(R.string.swipe_hint_1);
            } else {
                hint = getString(R.string.swipe_hint_2);
            }
            ScrollView layout = (ScrollView) LayoutInflater.from(this).inflate(R.layout.do_event_confirm, null);
            final CheckBox checkBox = (CheckBox) layout.findViewById(R.id.ofNeverAskAgain);
            ((TextView) layout.findViewById(R.id.ofTextView)).setText(hint);
           new AlertDialog.Builder(this).setTitle(getString(R.string.swipe_gesture)).setView(layout).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    A.askForSwipeEvent = !checkBox.isChecked();
                    if (fromLeftToRight) {
                        ActivityTxt.this.do_next_file();
                    } else {
                        ActivityTxt.this.do_prior_file();
                    }
                }
            }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    A.askForSwipeEvent = !checkBox.isChecked();
                    if (!A.askForSwipeEvent) {
                        A.doSwipeLeftToRight = 15;
                        A.doSwipeRightToLeft = 15;
                    }
                }
            }).show();
            return true;
        } else if (fromLeftToRight) {
            return doEvent(A.doSwipeLeftToRight);
        } else {
            return doEvent(A.doSwipeRightToLeft);
        }
    }

    private void setFullscreen(boolean topLayHeightOnly) {
        int l = 0;
        int r = 0;
        int b = 0;
        if (topLayHeightOnly) {
            if (this.topLayPaddingTop == -1) {
                this.topLayPaddingTop = this.topLay.getPaddingTop();
            }
            b = this.topLay.getPaddingBottom();
            l = this.topLay.getPaddingLeft();
            r = this.topLay.getPaddingRight();
            setStatusBarHeight();
        }
        if (A.fullscreen) {
            if (!topLayHeightOnly) {
                getWindow().addFlags(1024);
            } else if (!A.statusOnBottom) {
                topLaySetPadding(l, this.topLayPaddingTop + this.sysbar_height, r, b);
            } else if (VERSION.SDK_INT < 11 || !A.isTablet) {
                ((MarginLayoutParams) this.bottomLay.getLayoutParams()).bottomMargin = this.sysbar_height;
            }
        } else if (topLayHeightOnly) {
            topLaySetPadding(l, this.topLayPaddingTop, r, b);
        } else {
            getWindow().clearFlags(2048);
            getWindow().clearFlags(1024);
        }
    }

    private void topLaySetPadding(int l, int t, int r, int b) {
        if (VERSION.SDK_INT >= 21) {
            View bar = this.topLay.findViewById(R.id.notificationBar);
            bar.getLayoutParams().height = t;
            bar.setVisibility(0);
            this.topLay.setPadding(l, 0, r, b);
            return;
        }
        this.topLay.setPadding(l, t, r, b);
    }

    private void setStatusBarHeight() {
        if (A.sysbar_height != -1) {
            this.sysbar_height = A.sysbar_height;
            if (this.recheck_sysbar || A.openLastFile) {
                return;
            }
        }
        try {
            Rect r = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            int tmp = r.top;
            if (r.top == 0 && !T.isNull(selfPref)) {
                ActivityMain.selfPref.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                tmp = r.top;
            }
            if (tmp <= 5 || tmp >= 120) {
                tmp = T.getScreenHeight(this) - r.bottom;
                if (tmp > 5 && tmp < 120) {
                    this.sysbar_height = tmp;
                    A.sysbar_height = tmp;
                    A.statusOnBottom = true;
                    this.recheck_sysbar = true;
                    return;
                } else if (A.isTablet && VERSION.SDK_INT >= 11 && VERSION.SDK_INT < 14) {
                    A.statusOnBottom = true;
                    return;
                } else {
                    return;
                }
            }
            this.sysbar_height = tmp;
            A.sysbar_height = tmp;
            A.statusOnBottom = false;
            this.recheck_sysbar = true;
        } catch (Exception e) {
            A.error(e);
        }
    }

    public void onClick(View v) {
        do_floatBarButton(v);
    }

    private void createProgressDlg(String info) {
        createProgressDlg(getString(R.string.loading), info);
    }

    private void createProgressDlg(String title, String info) {
        if (this.progressDlg != null) {
            this.progressDlg.dismiss();
        }
        if (!A.isInAutoScroll) {
            if (A.isNightState()) {
                this.progressDlg = new ProgressDialog(this, R.style.MyProgressDialogDark);
                this.progressDlg.setTitle(title);
                while (info.length() < 40) {
                    info = info + "   ";
                }
                this.progressDlg.setMessage(info);
                this.progressDlg.show();
            } else {
                this.progressDlg = ProgressDialog.show(this, title, info, true);
            }
            this.progressDlg.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == 25 || keyCode == 24 || keyCode == 92 || keyCode == 93 || keyCode == ActivityTxt.SHOW_PDF || keyCode == ActivityTxt.SHOW_PDF2) {
                        return true;
                    }
                    return false;
                }
            });
            this.progressDlg.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    A.setSystemUiVisibility(true);
                }
            });
            this.progressDlg.setCancelable(true);
        }
    }

    public void hideProgressDlg() {
        this.ebook_inLoading = false;
        if (this.progressDlg != null) {
            this.progressDlg.dismiss();
        }
        this.progressDlg = null;
        if (this.progressIndicator != null) {
            this.progressIndicator.setVisibility(8);
        }
    }

    public void showProgressIndicator() {
        try {
            if (this.progressIndicator == null) {
                this.progressIndicator = new ProgressBar(this);
                this.baseFrame.addView(this.progressIndicator);
                LayoutParams fl = (LayoutParams) this.progressIndicator.getLayoutParams();
                fl.width = A.d(30.0f);
                fl.height = A.d(30.0f);
                fl.gravity = 17;
                fl.bottomMargin = A.d(10.0f);
                fl.rightMargin = A.d(10.0f);
            }
            this.progressIndicator.bringToFront();
            this.progressIndicator.setVisibility(0);
        } catch (Exception e) {
            A.error(e);
        }
    }

    private boolean checkRemindEvent() {
        if (isAutoState()) {
            A.invokeRemind1Time = SystemClock.elapsedRealtime();
            return false;
        } else if (!A.remind1 || SystemClock.elapsedRealtime() - A.invokeRemind1Time <= ((long) ((A.remind1Time * 60) * 1000))) {
            if (A.remind2) {
                if (this.isPaused) {
                    return false;
                }
                if (!A.invokeRemind2) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), A.remind2TimeHour, A.remind2TimeMinute, 0);
                    long invokeTime = calendar.getTimeInMillis();
                    long nowTime = System.currentTimeMillis();
                    if (((nowTime - invokeTime) / 60) / 1000 < 15 && nowTime > invokeTime) {
                        this.longTimeTapEvent = false;
                        A.invokeRemind2 = true;
                       new AlertDialog.Builder(this).setTitle(T.time()).setMessage(A.remind2Text).setPositiveButton(17039370, null).show();
                        return true;
                    }
                }
            }
            return false;
        } else {
            this.longTimeTapEvent = false;
            A.invokeRemind1Time = SystemClock.elapsedRealtime();
            if (this.isPaused) {
                return false;
            }
            View v = LayoutInflater.from(this).inflate(R.layout.health_remind, null);
            ((TextView) v.findViewById(R.id.tipTv)).setText("(" + getString(R.string.miscellaneous) + " -> " + getString(R.string.remindlabel) + ": " + A.remind1Time + getString(R.string.tts_stop_time2) + ")");
            final TextView timeTv = (TextView) v.findViewById(R.id.timeTv);
            timeTv.setText("60");
            final Builder reminder =new AlertDialog.Builder(this);
            reminder.setView(v).setCancelable(false).show();
            new Handler() {
                public void handleMessage(Message msg) {
                    A.invokeRemind1Time = SystemClock.elapsedRealtime();
                    timeTv.setText("" + msg.what);
                    msg.what--;
                    if (msg.what >= 0) {
                        sendEmptyMessageDelayed(msg.what, 1000);
                    } else {
                        reminder.dismiss();
                    }
                }
            }.sendEmptyMessage(60);
            return true;
        }
    }

    private void initReadProgressViews() {
        this.statusLay = findViewById(R.id.txtStatusLay);
        this.remainingLay = findViewById(R.id.remainingLay);
        this.statusLayHori = findViewById(R.id.txtStatusLayHori);
        this.statusLeftPanel = findViewById(R.id.statusLeftPanel);
        this.status2Page = findViewById(R.id.status2Page);
        this.statusLeft = (TextView) findViewById(R.id.statusLeft);
        this.statusLeft2 = (TextView) findViewById(R.id.statusLeft2);
        this.statusRight = (TextView) findViewById(R.id.statusRight);
        this.statusMiddle = (TextView) findViewById(R.id.statusMiddle);
        this.statusMiddle21 = (TextView) findViewById(R.id.statusMiddle21);
        this.statusMiddle22 = (TextView) findViewById(R.id.statusMiddle22);
        this.remainingLeft = (TextView) findViewById(R.id.remainingLeft);
        this.remainingRight = (TextView) findViewById(R.id.remainingRight);
        this.statusLeftPanel.setOnClickListener(this);
        this.statusRight.setOnClickListener(this);
        this.statusMiddle.setOnClickListener(this);
        this.statusMiddle21.setOnClickListener(this);
        this.statusMiddle22.setOnClickListener(this);
        this.statusLeftPanel.setOnLongClickListener(this);
        this.statusRight.setOnLongClickListener(this);
        this.statusMiddle.setOnLongClickListener(this);
        this.statusMiddle21.setOnLongClickListener(this);
        this.statusMiddle22.setOnLongClickListener(this);
        setStatusBarProperties(false);
    }

    private long getTotalPages() {
        try {
            if (getBookType() == 100) {
                if (A.ebook.getChapters().size() > 0) {
                    int l = this.txtView.getText().length();
                    if (((long) l) - this.lastPageCountCheckSize > 100) {
                        this.lastPageCountCheckSize = (long) l;
                        this.totalPages = (long) (100000.0d * (((((double) this.txtView.getRealHeight()) / ((double) A.getPageHeight())) * ((double) getEBookTotalSize())) / ((double) ((Chapter) A.ebook.getChapters().get(A.lastChapter)).size)));
                    }
                }
            } else if (this.totalPages < 100000 && this.txtView.getRealHeight() > this.baseFrame.getHeight()) {
                if (getBookType() != 0 || A.getTxts().size() <= 3) {
                    double t = (double) ((A.DEAULT_MAX_HTML_CHAPTER_SIZE * this.txtView.getRealHeight()) / A.getPageHeight());
                    if (!A.noSplitHtmls()) {
                        t *= (double) (this.htmlText.length() / this.txtView.getText().length());
                    }
                    this.totalPages = (long) t;
                } else {
                    this.totalPages = ((long) A.getTxts().size()) * (((((long) this.txtView.getRealHeight()) * 100000) / ((long) A.getPageHeight())) / 3);
                }
            }
        } catch (Exception e) {
            A.error(e);
            this.totalPages = 100000;
        }
        if (this.totalPages > 100000) {
            return this.totalPages;
        }
        return 100000;
    }

    private long getAboutCurrentPage(int pageAdd) {
        if (this.currentPage == -1) {
            long l = getBookLength();
            if (l == 0) {
                l = 1;
            }
            switch (getBookType()) {
                case 0:
                    this.currentPage = (((getTotalPages() * A.lastPosition) / l) / 100000) + 1;
                    break;
                case 1:
                    this.currentPage = ((((A.noSplitHtmls() ? A.lastPosition : ((long) (A.maxHtmlChapterSize * A.lastSplitIndex)) + A.lastPosition) * getTotalPages()) / l) / 100000) + 1;
                    break;
                case 100:
                    this.currentPage = (((getTotalPages() * ((long) BaseEBook.getPriorTextLength2(A.lastChapter))) / l) / 100000) + ((long) ((((this.txtScroll.getScrollY() + (A.getPageHeight() / 5)) * ((this.txtView.getRealHeight() / A.getPageHeight()) + 1)) / this.txtView.getRealHeight()) + 1));
                    break;
            }
        }
        this.currentPage += (long) pageAdd;
        if (this.currentPage < 1) {
            this.currentPage = 1;
        }
        if (this.currentPage > getTotalPages() / 100000) {
            this.currentPage = getTotalPages() / 100000;
        }
        return this.currentPage;
    }

    private void fixBottomForScrollTo(int toY) {
        if (toY != 0) {
            int svH = A.getPageHeight();
            int tvH = this.txtView.getHeight();
            if (tvH - toY < svH) {
                int off = svH - (tvH - toY);
                int lh = this.txtView.getLineHeight();
                int count = off / lh;
                if (off % lh > 0) {
                    count++;
                }
                appendLines(count);
            }
        }
    }

    private void appendLines(int count) {
        this.txtView.appendEmptyLines(count);
    }

    public void checkBottomHalfLineHandler() {
        this.checkedBottomHalfLine = true;
        A.checkScrollMargin();
        txtScrollSetPadding(this.txtScroll.getPaddingLeft(), A.topMargin, this.txtScroll.getPaddingRight(), A.bottomMargin + (showStatusbar() ? this.statusLay.getHeight() - this.statusLay.getPaddingTop() : 0));
    }

    public void checkBottomHalfLine() {
        this.handler.sendEmptyMessage(CHECK_BOTTOM_HALF_LINE);
    }

    private void checkStatusBar() {
        if (showStatusbar() && this.web == null) {
            this.statusLay.setVisibility(0);
            setStatusBarProperties(true);
            updateProgressStatus();
            if (isPdf()) {
                this.pdfFullStatusBar = null;
                pdfShowFullStatusBarInfo();
            }
        } else {
            this.statusLay.setVisibility(8);
        }
        checkBottomHalfLine();
    }

    private void updateProgressStatus() {
        statusHandlerSendMessage(100, 200);
    }

    public void setStatusBarProperties(boolean colorOnly) {
        int i;
        int i2 = 8;
        boolean z = true;
        int color = A.statusCustomizeFont ? A.statusFontColor : isPdf() ? A.isWhiteFont(A.pdf_back_color) ? -14540254 : -3355444 : A.fontColor;
        this.statusLeft.setTextColor(A.getAlphaColor(color, -50));
        this.statusLeft2.setTextColor(color);
        this.statusMiddle.setTextColor(color);
        this.statusMiddle21.setTextColor(color);
        this.statusMiddle22.setTextColor(color);
        this.statusRight.setTextColor(color);
        View view = this.statusLayHori;
        if (A.showStatusbar || this.status2Page.getVisibility() == 0 || (A.showRemainingTime && A.remaingTimeInStatusBar)) {
            i = 0;
        } else {
            i = 8;
        }
        view.setVisibility(i);
        View view2 = this.remainingLay;
        if (A.showRemainingTime && !A.remaingTimeInStatusBar && this.status2Page.getVisibility() == 8) {
            i2 = 0;
        }
        view2.setVisibility(i2);
        this.remainingLeft.setTextColor(color);
        this.remainingRight.setTextColor(color);
        if (!colorOnly) {
            boolean z2;
            this.statusLeft.setTextSize((float) A.statusFontSize);
            this.statusLeft2.setTextSize((float) A.statusFontSize);
            this.statusMiddle.setTextSize((float) A.statusFontSize);
            this.statusRight.setTextSize((float) A.statusFontSize);
            this.statusLay.setPadding(A.statusMargin, A.statusMargin, A.statusMargin, A.statusMargin);
            this.remainingLeft.setTextSize((float) A.statusFontSize);
            this.remainingRight.setTextSize((float) A.statusFontSize);
            this.remainingLay.setPadding(A.statusMargin, A.statusMargin, A.statusMargin, A.statusMargin);
            if (A.statusCustomizeFont) {
                this.statusLeft.setTextColor(A.getAlphaColor(A.statusFontColor, -50));
                this.statusLeft2.setTextColor(A.statusFontColor);
                this.statusMiddle.setTextColor(A.statusFontColor);
                this.statusRight.setTextColor(A.statusFontColor);
                this.statusLeft.setTypeface(A.getTypeFace(A.statusFontName, 1));
                this.statusLeft2.setTypeface(A.getTypeFace(A.statusFontName, 1));
                this.statusMiddle.setTypeface(A.getTypeFace(A.statusFontName, 0));
                this.statusRight.setTypeface(A.getTypeFace(A.statusFontName, 1));
                this.remainingLeft.setTypeface(A.getTypeFace(A.statusFontName, 0));
                this.remainingRight.setTypeface(A.getTypeFace(A.statusFontName, 0));
            } else {
                this.statusLeft.setTypeface(A.getTypeFace("sans-serif", 1));
                this.statusLeft2.setTypeface(A.getTypeFace("sans-serif", 1));
                this.statusMiddle.setTypeface(A.getTypeFace("sans-serif", 0));
                this.statusRight.setTypeface(A.getTypeFace("sans-serif", 1));
                this.remainingLeft.setTypeface(A.getTypeFace("sans-serif", 1));
                this.remainingRight.setTypeface(A.getTypeFace("sans-serif", 1));
            }
            View view3 = this.statusLeftPanel;
            if (A.statusClickLeft != 15) {
                z2 = true;
            } else {
                z2 = false;
            }
            view3.setClickable(z2);
            TextView textView = this.statusMiddle;
            if (A.statusClickMiddle != 15) {
                z2 = true;
            } else {
                z2 = false;
            }
            textView.setClickable(z2);
            textView = this.statusMiddle21;
            if (A.statusClickMiddle != 15) {
                z2 = true;
            } else {
                z2 = false;
            }
            textView.setClickable(z2);
            textView = this.statusMiddle22;
            if (A.statusClickMiddle != 15) {
                z2 = true;
            } else {
                z2 = false;
            }
            textView.setClickable(z2);
            textView = this.statusRight;
            if (A.statusClickRight != 15) {
                z2 = true;
            } else {
                z2 = false;
            }
            textView.setClickable(z2);
            view3 = this.statusLeftPanel;
            if (A.statusClickLeft2 != 15) {
                z2 = true;
            } else {
                z2 = false;
            }
            view3.setLongClickable(z2);
            textView = this.statusMiddle;
            if (A.statusClickMiddle2 != 15) {
                z2 = true;
            } else {
                z2 = false;
            }
            textView.setLongClickable(z2);
            textView = this.statusMiddle21;
            if (A.statusClickMiddle2 != 15) {
                z2 = true;
            } else {
                z2 = false;
            }
            textView.setLongClickable(z2);
            textView = this.statusMiddle22;
            if (A.statusClickMiddle2 != 15) {
                z2 = true;
            } else {
                z2 = false;
            }
            textView.setLongClickable(z2);
            TextView textView2 = this.statusRight;
            if (A.statusClickRight2 == 15) {
                z = false;
            }
            textView2.setLongClickable(z);
        }
        setStatusLayBackgroundColor();
    }

    public void setStatusLayBackgroundColor() {
        boolean z = true;
        if (this.status2Page.getVisibility() == 0) {
            this.statusLayHori.setBackgroundDrawable(null);
            this.statusLeft.setBackgroundDrawable(null);
            setStatusLayTextColorFor2PageMode();
        } else if (isPdf()) {
            this.statusLayHori.setBackgroundDrawable(null);
        } else {
            View view = this.statusLayHori;
            int i = (!isPureBlackBackground() || A.statusBackColor == 0) ? A.statusBackColor : -2009910477;
            view.setBackgroundColor(i);
        }
        boolean white = isPdf() ? !A.isWhiteFont(A.pdf_back_color) : A.isWhiteFont(A.fontColor);
        BatteryTextView batteryTextView = (BatteryTextView) this.statusLeft;
        if (white) {
            z = false;
        }
        batteryTextView.setBatteryIcon(z);
    }

    private boolean isPureBlackBackground() {
        return !A.useBackgroundImage && A.backgroundColor == ViewCompat.MEASURED_STATE_MASK;
    }

    private void statusHandlerSendMessage(int what, int delay) {
        if (this.statusHandler == null) {
            this.statusHandler = new Handler() {
                public void handleMessage(Message msg) {
                    if (!ActivityTxt.this.isFinishing()) {
                        try {
                            if (msg.what == 0) {
                                ActivityTxt.this.showReadProgress(0);
                                if (!(ActivityTxt.this.ebook_inLoading || ActivityTxt.this.isPaused)) {
                                    ActivityTxt.this.saveLastPostion(true, true);
                                }
                            }
                            if (msg.what == 100) {
                                if (!(ActivityTxt.this.isPdf() || ActivityTxt.this.getBookType() == 0 || ActivityTxt.this.txtView.getRealHeight() >= ActivityTxt.this.baseFrame.getHeight() * 3)) {
                                    ActivityTxt.this.preNextChapter(false);
                                }
                                ActivityTxt.this.showReadProgress(0);
                            }
                            if (msg.what == 1) {
                                A.setTxtScrollMargin(ActivityTxt.this.txtScroll);
                                ActivityTxt.this.checkStatusBar();
                            }
                        } catch (Exception e) {
                            A.error(e);
                        }
                    }
                }
            };
        }
        this.statusHandler.removeMessages(what);
        this.statusHandler.sendEmptyMessageDelayed(what, (long) delay);
    }

    public void showReadProgress(int pageAdd) {
        if (!this.inCloudSyncing && showStatusbar() && !this.isPaused && this.web == null && SystemClock.elapsedRealtime() - this.lastCheckProgressTime >= 100) {
            pageAdd *= txtKeepPage(0);
            this.lastCheckProgressTime = SystemClock.elapsedRealtime();
            saveLastPostion(false);
            updateBarTime();
            String text = "";
            String progress = "";
            this.total = -1;
            this.current = -1;
            switch (getBookType()) {
                case 0:
                    text = T.getFilename(A.lastFile);
                    if (!this.inPreShow) {
                        this.total = getTotalPages() / 100000;
                        this.current = getAboutCurrentPage(pageAdd);
                        if (this.current > this.total) {
                            this.current = this.total;
                        }
                        text = text + (" (" + this.current + "/" + this.total + ")");
                        break;
                    }
                    break;
                case 1:
                case 100:
                    if (getBookType() == 1) {
                        text = T.getFilename(A.lastFile);
                    } else if (A.ebook != null) {
                        text = ((Chapter) A.ebook.getChapters().get(A.lastChapter)).name;
                    } else {
                        return;
                    }
                    int h = this.txtView.getRealHeight();
                    int tmp = h / A.getPageHeight();
                    int oneLineTagHeight = tmp > 3 ? (A.oneLineTagHeight() * tmp) / 3 : tmp > 2 ? A.oneLineTagHeight() / 2 : 0;
                    h += oneLineTagHeight;
                    if (h < 1) {
                        h = 1;
                    }
                    this.total = (long) ((h / A.getPageHeight()) + 1);
                    this.current = ((this.total * ((long) (this.txtScroll.getScrollY() + (A.getPageHeight() / 5)))) / ((long) h)) + 1;
                    if (!A.noSplitHtmls()) {
                        if (A.lastSplitIndex > A.splitHtmls.size() - 1) {
                            A.lastSplitIndex = A.splitHtmls.size() - 1;
                        }
                        int l = ((String) A.splitHtmls.get(A.lastSplitIndex)).length();
                        int priorTotal = -1;
                        if (this.cachedSplitsPageCount == null) {
                            this.cachedSplitsPageCount = new HashMap();
                            this.cachedSplitsPageNum = new HashMap();
                        }
                        this.cachedSplitsPageCount.put(Integer.valueOf(A.lastSplitIndex), Integer.valueOf((int) this.total));
                        if (A.lastSplitIndex > 0 && this.cachedSplitsPageCount.containsKey(Integer.valueOf(A.lastSplitIndex - 1))) {
                            this.total = (long) ((Integer) this.cachedSplitsPageCount.get(Integer.valueOf(A.lastSplitIndex - 1))).intValue();
                            l = ((String) A.splitHtmls.get(A.lastSplitIndex - 1)).length();
                            if (this.cachedSplitsPageNum.containsKey(Integer.valueOf(A.lastSplitIndex - 1))) {
                                priorTotal = ((Integer) this.cachedSplitsPageNum.get(Integer.valueOf(A.lastSplitIndex - 1))).intValue();
                            }
                        }
                        this.current = (priorTotal > 0 ? (long) priorTotal : (this.total * stringArrayLength(A.splitHtmls, A.lastSplitIndex)) / ((long) l)) + this.current;
                        this.total = (this.total * stringArrayLength(A.splitHtmls, A.splitHtmls.size())) / ((long) l);
                        if (lastLineDisplayed()) {
                            HashMap hashMap = this.cachedSplitsPageNum;
                            Integer valueOf = Integer.valueOf(A.lastSplitIndex);
                            long j = this.current;
                            oneLineTagHeight = (dualPageEnabled() && this.txtView2.getVisibility() == 0) ? 1 : 0;
                            hashMap.put(valueOf, Integer.valueOf((int) (j + ((long) oneLineTagHeight))));
                        }
                    }
                    if (this.current > this.total || (A.noSplitHtmls() && !dualPageEnabled() && lastLineDisplayed())) {
                        this.current = this.total;
                    }
                    text = text + (" (" + this.current + "/" + this.total + ")");
                    break;
                case 7:
                    if (this.pdf != null && this.pdf.pdfView != null) {
                        pdfShowFullStatusBarInfo();
                        pdfGetToc();
                        if (this.pdfToc.size() > 0 && A.lastChapter < this.pdfToc.size()) {
                            text = ((TocChapter) this.pdfToc.get(A.lastChapter)).name;
                            break;
                        } else {
                            text = A.getBookName();
                            break;
                        }
                    }
                    return;
            }
            int book_left = -1;
            if (showRemaingTime()) {
                book_left = getBookLeftWords();
            }
            if (isPdf()) {
                this.statusRight.setText((this.pdf.textReflow ? A.lastPosition + 1 : (long) (pdfGetCurrPageNo() + 1)) + "/" + this.m_doc.GetPageCount());
            } else {
                this.statusRight.setText(this.inPreShow ? "" : getPercentStr2());
            }
            if (this.status2Page.getVisibility() == 0) {
                if (this.total != -1) {
                    this.statusMiddle21.setText(this.current + "/" + this.total);
                    this.statusMiddle21.setTag(" (" + this.current + "/" + this.total + ")");
                    String text2 = this.current + 1 > this.total ? "" : (this.current + 1) + "/" + this.total;
                    this.statusMiddle22.setText(text2);
                    this.statusMiddle22.setTag(text2.length() > 0 ? " (" + text2 + ")" : "");
                } else {
                    this.statusMiddle21.setText("");
                    this.statusMiddle22.setText("");
                }
                this.statusMiddle.setText("");
            } else {
                restStatusMiddlePadding();
                this.statusMiddle.setText(text);
                this.statusMiddle.setTag(isPdf() ? "" : " (" + this.current + "/" + this.total + ")");
            }
            refresh_ebook_remaining_time(book_left, false);
            statusHandlerSendMessage(0, 60000);
        }
    }

    private int getBookLeftWords() {
        switch (getBookType()) {
            case 0:
                if (!this.inPreShow) {
                    return BaseEBook.getTxtUnReadWordCount(new AfterGetUnReadWords() {
                        public void refresh(int wordCount) {
                            ActivityTxt.this.handler.sendMessage(ActivityTxt.this.handler.obtainMessage(901, wordCount, 0));
                        }
                    }, false);
                }
                break;
            case 1:
                if (this.total > 0 && this.current > 0) {
                    return (int) ((((long) getHtmlFileWordCount()) * (this.total - this.current)) / this.total);
                }
            case 7:
                return BaseEBook.getPdfUnReadWordCoun(new AfterGetUnReadWords() {
                    public void refresh(int wordCount) {
                        ActivityTxt.this.handler.sendMessage(ActivityTxt.this.handler.obtainMessage(901, wordCount, 0));
                    }
                }, false);
            case 100:
                return BaseEBook.getUnReadWordCountFrom(A.ebook, A.lastChapter, new AfterGetUnReadWords() {
                    public void refresh(int wordCount) {
                        ActivityTxt.this.handler.sendMessage(ActivityTxt.this.handler.obtainMessage(901, wordCount, 0));
                    }
                }, false);
        }
        return -1;
    }

    protected void refresh_ebook_remaining_time(int book_left, boolean refreshTotalOnly) {
        if (!showRemaingTime()) {
            return;
        }
        if (refreshTotalOnly) {
            this.statusRight.setText(getPercentStr2());
            return;
        }
        if (book_left == -1 && A.ebook != null) {
            this.statusRight.setText("");
        }
        try {
            int chapter_left = getChapterLeftWords();
            if (chapter_left != -1 || book_left != -1) {
                int chapter_min = 0;
                int book_hour = 0;
                int book_min = 0;
                int speed = getReadingSpeed();
                if (book_left != -1) {
                    if (chapter_left != -1) {
                        book_left += chapter_left;
                    }
                    int min = book_left / speed;
                    book_hour = min / 60;
                    book_min = min % 60;
                }
                if (chapter_left != -1 && getBookType() == 100 && A.ebook != null && A.ebook.getChapters().size() == 1) {
                    chapter_left = -1;
                }
                if (chapter_left != -1) {
                    chapter_min = chapter_left / speed;
                }
                if (this.status2Page.getVisibility() == 0) {
                    String str;
                    this.statusMiddle21.setText(String.format(getString(R.string.remaining_time_chapter) + "", new Object[]{Integer.valueOf(chapter_min)}) + ((String) this.statusMiddle21.getTag()));
                    TextView textView = this.statusMiddle22;
                    StringBuilder stringBuilder = new StringBuilder();
                    if (book_left == -1) {
                        str = "...";
                    } else {
                        str = String.format(getString(R.string.remaining_time_book), new Object[]{Integer.valueOf(book_hour), Integer.valueOf(book_min)});
                    }
                    textView.setText(stringBuilder.append(str).append((String) this.statusMiddle22.getTag()).toString());
                    this.statusMiddle.setText("");
                } else if (A.remaingTimeInStatusBar) {
                    String text;
                    if (book_left == -1) {
                        text = String.format(getString(R.string.remaining_time_chapter) + "", new Object[]{Integer.valueOf(chapter_min)});
                    } else if (chapter_left == -1) {
                        text = String.format(getString(R.string.remaining_time_book), new Object[]{Integer.valueOf(book_hour), Integer.valueOf(book_min)});
                    } else {
                        text = String.format(getString(R.string.remaining_time_format), new Object[]{Integer.valueOf(chapter_min), Integer.valueOf(book_hour), Integer.valueOf(book_min)});
                    }
                    String tmp = text + ((String) this.statusMiddle.getTag());
                    if (Layout.getDesiredWidth(tmp, this.statusMiddle.getPaint()) < ((float) (this.statusMiddle.getWidth() - A.d(4.0f)))) {
                        this.statusMiddle.setText(tmp);
                    } else {
                        this.statusMiddle.setText(text);
                    }
                } else {
                    String left = "";
                    String right = "";
                    if (chapter_left != -1) {
                        left = String.format(getString(R.string.remaining_time_chapter) + "", new Object[]{Integer.valueOf(chapter_min)});
                    }
                    if (book_left != -1) {
                        right = String.format(getString(R.string.remaining_time_book), new Object[]{Integer.valueOf(book_hour), Integer.valueOf(book_min)});
                    }
                    this.remainingLeft.setText(left);
                    this.remainingRight.setText(right);
                }
            }
        } catch (OutOfMemoryError e) {
            A.error(e);
        } catch (Exception e2) {
            A.error(e2);
        }
    }

    private int getChapterLeftWords() {
        switch (getBookType()) {
            case 0:
            case 100:
                MyLayout lo = this.txtView.getLayout();
                if (lo == null) {
                    return -1;
                }
                String text_left = this.txtView.getText2().substring(lo.getLineStart(lo.getLineForVertical(this.txtScroll.getScrollY())), this.txtView.getText().length());
                if (getBookType() == 100 && !A.noSplitHtmls() && A.lastSplitIndex < A.splitHtmls.size() - 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = A.lastSplitIndex + 1; i < A.splitHtmls.size(); i++) {
                        sb.append((String) A.splitHtmls.get(i));
                    }
                    text_left = text_left + (A.ebook.isHtml() ? T.html2Text(sb.toString()) : sb.toString());
                }
                return T.getWordsCount(text_left, A.isAsiaLanguage);
            default:
                return -1;
        }
    }

    private void restStatusMiddlePadding() {
        if (showRemaingTime() && A.remaingTimeInStatusBar) {
            if (this.statusMiddle.getPaddingLeft() == A.d(30.0f)) {
                this.statusMiddle.setPadding(A.d(2.0f), 0, A.d(2.0f), 0);
                this.statusMiddle.setEllipsize(TruncateAt.END);
            }
        } else if (this.statusMiddle.getPaddingLeft() == A.d(2.0f)) {
            this.statusMiddle.setPadding(A.d(30.0f), 0, A.d(30.0f), 0);
            this.statusMiddle.setEllipsize(TruncateAt.MIDDLE);
        }
    }

    private int getReadingSpeed() {
        ReadStatistics r = BookDb.getSavedStatistics(A.lastFile);
        if (SystemClock.elapsedRealtime() - this.statisticsTime > ((long) 1800000)) {
            saveStatisticsInfo();
        }
        if (r.usedTime > ((long) 1800000)) {
            return (int) (r.readWords / ((r.usedTime / 60) / 1000));
        } else if (A.statistics_time <= ((long) 1800000)) {
            return A.isAsiaLanguage ? DropboxServerException._400_BAD_REQUEST : 220;
        } else {
            return (int) (A.statistics_words / ((A.statistics_time / 60) / 1000));
        }
    }

    public int getHtmlFileWordCount() {
        if (this.htmlFileWordCount == 0) {
            String text = T.html2Text(this.htmlText);
            this.htmlFileCharCount = text.length();
            this.htmlFileWordCount = T.getWordsCount(text, A.isAsiaLanguage);
        }
        return this.htmlFileWordCount;
    }

    public int getHtmlFileCharCount() {
        if (this.htmlFileCharCount == 0) {
            String text = T.html2Text(this.htmlText);
            this.htmlFileCharCount = text.length();
            this.htmlFileWordCount = T.getWordsCount(text, A.isAsiaLanguage);
        }
        return this.htmlFileCharCount;
    }

    private String getPercentStr2() {
        if (isPdf()) {
            return T.getPercentStr((long) (pdfGetCurrPageNo() + 1), (long) this.m_doc.GetPageCount());
        }
        if (isEndOfBook()) {
            return "100%";
        }
        return T.getPercentStr(getCurrentPosition(), getBookLength());
    }

    private boolean isEndOfBook() {
        int y = this.txtScroll.getScrollY();
        int tvH = this.txtView.getRealHeight();
        int svH = A.getPageHeight();
        int lh = this.txtView.getLineHeight();
        switch (getBookType()) {
            case 0:
                if (A.lastBlockIndex >= A.getTxts().size() - 2 && (txtKeepPage(0) * svH) + y > tvH - ((lh * 9) / 10)) {
                    return true;
                }
            case 1:
                if ((A.splitHtmls.size() == 0 || A.lastSplitIndex == A.splitHtmls.size() - 1) && (txtKeepPage(0) * svH) + y > tvH - ((lh * 9) / 10)) {
                    return true;
                }
            case 7:
                if (pdfGetCurrPageNo() == this.m_doc.GetPageCount() - 1) {
                    return true;
                }
                break;
            case 100:
                if (A.ebook != null && A.lastChapter == A.ebook.getChapters().size() - 1 && ((A.splitHtmls.size() == 0 || A.lastSplitIndex == A.splitHtmls.size() - 1) && lastLineDisplayed())) {
                    return true;
                }
        }
        return false;
    }

    private boolean lastLineDisplayed() {
        int line;
        if (!dualPageEnabled()) {
            line = A.getLastDisplayLine(this.txtScroll, -1);
            if (line == getRealLineCount() - 1 && this.txtView.lineHasImage(line) && this.txtScroll.getScrollY() + A.getPageHeight() < this.txtView.getLineTop(line + 1)) {
                return false;
            }
        } else if (!isPage2Visible() || this.txtScroll2.getScrollY() + A.getPageHeight() >= this.txtView.getRealHeight()) {
            return true;
        } else {
            line = A.getLastDisplayLine(this.txtScroll2, -1);
        }
        if (line >= getRealLineCount() - 1) {
            return true;
        }
        if (this.txtView.getLayout().getLineFloat(line) != 0) {
            MyFloatSpan floatSp = this.txtView.getLayout().getLineFloatSp(line);
            ScrollView sv = dualPageEnabled() ? this.txtScroll2 : this.txtScroll;
            if (floatSp != null && (floatSp.float_v + floatSp.float_height) - ((float) A.d(10.0f)) > ((float) (sv.getScrollY() + A.getPageHeight()))) {
                return false;
            }
        }
        if (line >= getRealLineCount() - 3) {
            String s = this.txtView.getText2().substring(this.txtView.getLayout().getLineStart(line + 1), this.txtView.getText2().length());
            if (A.isEmtpyText(s, 0, s.length())) {
                return true;
            }
        }
        return false;
    }

    private long stringArrayLength(ArrayList<String> list, int index) {
        long result = 0;
        for (int i = 0; i < index; i++) {
            result += (long) ((String) list.get(i)).length();
        }
        return result;
    }

    private void updateBarTime() {
        if (showStatusbar()) {
            try {
                if (!isPdf() || pdfShowFullStatusBarInfo()) {
                    this.statusLeft2.setText(T.time(false, A.use12Hour, A.getLocale()));
                    if (this.battery == null) {
                        String battery2 = T.getBattery();
                        updateBatteryInfo(battery2);
                        if (battery2 == null) {
                            registerBattery();
                            return;
                        }
                        return;
                    }
                    updateBatteryInfo(this.battery);
                }
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    private void updateBatteryInfo(String battery1) {
        if (battery1 != null && battery1.length() > 0) {
            this.statusLeft.setVisibility(0);
            this.statusLeft.setText(battery1.trim());
        } else if (this.statusLeft.getVisibility() == 0) {
            this.statusLeft.setVisibility(8);
        }
    }

    private void error_and_exit(String errMsg, boolean clearLastFile) {
        CharSequence errMsg2;
        if (clearLastFile) {
            A.lastFile = "";
        }
        if (errMsg == null) {
            if (A.ebook == null || A.ebook.errMsg == null) {
                errMsg2 = getString(R.string.invalid_file);
            } else {
                errMsg2 = A.ebook.errMsg;
            }
        }
        final boolean oom = errMsg2.indexOf(".OutOfMemoryError") != -1;
       new AlertDialog.Builder(this).setTitle(getString(R.string.error)).setMessage(errMsg2).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (oom) {
                    ActivityTxt.this.restartReaderToMain();
                } else {
                    ActivityTxt.this.doFinish();
                }
            }
        }).setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                if (oom) {
                    ActivityTxt.this.restartReaderToMain();
                } else {
                    ActivityTxt.this.doFinish();
                }
            }
        }).show();
    }

    private void setFlipViewINVISIBLE(boolean clear) {
        if (this.flipView.getVisibility() != 8) {
            this.flipView.setVisibility(8);
            if (clear) {
                this.flipView.setImageDrawable(null);
            }
            this.contentLay.setAnimationState(false);
        }
    }

    private void setCurlViewINVISIBLE() {
        if (this.curl3d != null && this.curl3dLay.getVisibility() == 0) {
            setCurl3dVisibility(false);
        }
    }

    private void initHighlightViews() {
        int i = 0;
        this.hBar = (HighlightLay) findViewById(R.id.HighlightBar);
        this.dot1 = (DotImageView) findViewById(R.id.dot1);
        this.dot2 = (DotImageView) findViewById(R.id.dot2);
        this.hPen = (ImageView) findViewById(R.id.penIv);
        this.hCopy = (TextView) findViewById(R.id.copyTv);
        this.hHighlight = (TextView) findViewById(R.id.highlightTv);
        this.hNote = (TextView) findViewById(R.id.noteTv);
        this.hDict = (TextView) findViewById(R.id.dictTv);
        this.hMore = (TextView) findViewById(R.id.shareTv);
        this.colorTemplate = (ColorTemplate) findViewById(R.id.colorTemplate);
        this.hBar2 = findViewById(R.id.HighlightBar2);
        this.hCopy.getPaint().setFakeBoldText(true);
        this.hHighlight.getPaint().setFakeBoldText(true);
        this.hNote.getPaint().setFakeBoldText(true);
        this.hDict.getPaint().setFakeBoldText(true);
        this.hMore.getPaint().setFakeBoldText(true);
        this.hPen.setOnClickListener(this.hOnClick);
        this.hCopy.setOnClickListener(this.hOnClick);
        this.hHighlight.setOnClickListener(this.hOnClick);
        this.hNote.setOnClickListener(this.hOnClick);
        this.hDict.setOnClickListener(this.hOnClick);
        this.hMore.setOnClickListener(this.hOnClick);
        this.dot1.setOnTouchListener(this.hOnTouch);
        this.dot2.setOnTouchListener(this.hOnTouch);
        setHPenVisible(isPdf() ? 0 : 8);
        this.hHighlight.setOnLongClickListener(this);
        this.hDict.setOnLongClickListener(this);
        ColorTemplate colorTemplate = this.colorTemplate;
        if (!A.showColorTemplate) {
            i = 8;
        }
        colorTemplate.setVisibility(i);
        if (A.showColorTemplate) {
            initColorTemplateEvents();
        }
    }

    public void initColorTemplateEvents() {
        this.colorTemplate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                boolean z = true;
                boolean top = ActivityTxt.this.colorTemplate.isClickTop();
                int index = ActivityTxt.this.colorTemplate.getClicked();
                if (top && index == 4) {
                    T.showToastText(ActivityTxt.this, ActivityTxt.this.getString(R.string.show_highlight_template_tip), 1);
                } else if (!ActivityTxt.this.isPdf()) {
                    if (top) {
                        A.highlightMode = index;
                        ActivityTxt.this.colorTemplate.postInvalidate();
                    }
                    ActivityTxt activityTxt = ActivityTxt.this;
                    MRTextView mRTextView = A.touchingView == ActivityTxt.this.txtScroll2 ? ActivityTxt.this.txtView2 : ActivityTxt.this.txtView;
                    if (!top) {
                        index = -1;
                    }
                    int clickedColor = ActivityTxt.this.colorTemplate.getClickedColor();
                    if (top) {
                        z = false;
                    }
                    activityTxt.doHighlight2(mRTextView, index, clickedColor, z);
                } else if (!top || !ActivityTxt.this.isClickOnAnnot()) {
                    if (top) {
                        A.highlightMode = index;
                    }
                    ActivityTxt.this.pdfDoHighlight2(ActivityTxt.this.colorTemplate.getClickedColor());
                }
            }
        });
        this.colorTemplate.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                if (!(ActivityTxt.this.colorTemplate.isClickTop() && ActivityTxt.this.colorTemplate.getClicked() == 4)) {
                    new ColorDialog(ActivityTxt.this, ActivityTxt.this.getString(R.string.highlight_color), true, ActivityTxt.this.colorTemplate.getLongClickedColor(), new OnSaveColor() {
                        public void getColor(int color) {
                            if (!ActivityTxt.this.colorTemplate.isClickTop()) {
                                switch (ActivityTxt.this.colorTemplate.getClicked()) {
                                    case 0:
                                        if (!ActivityTxt.this.isPdf()) {
                                            A.highlight_color1 = color;
                                            break;
                                        } else {
                                            A.pdf_highlight_color = color;
                                            break;
                                        }
                                    case 1:
                                        A.highlight_color2 = color;
                                        break;
                                    case 2:
                                        A.highlight_color3 = color;
                                        break;
                                    case 3:
                                        A.highlight_color4 = color;
                                        break;
                                    case 4:
                                        if (A.highlightMode != 3) {
                                            if (A.highlightMode != 2) {
                                                A.underline_color = color;
                                                break;
                                            } else {
                                                A.strikethrough_color = color;
                                                break;
                                            }
                                        }
                                        A.squiggly_color = color;
                                        break;
                                    default:
                                        break;
                                }
                            }
                            switch (ActivityTxt.this.colorTemplate.getClicked()) {
                                case 0:
                                    if (!ActivityTxt.this.isPdf()) {
                                        A.highlight_color1 = color;
                                        break;
                                    } else {
                                        A.pdf_highlight_color = color;
                                        break;
                                    }
                                case 1:
                                    A.underline_color = color;
                                    break;
                                case 2:
                                    A.strikethrough_color = color;
                                    break;
                                case 3:
                                    A.squiggly_color = color;
                                    break;
                                case 4:
                                    return;
                            }
                            ActivityTxt.this.colorTemplate.postInvalidate();
                        }
                    }).show();
                }
                return true;
            }
        });
    }

    public void updateHBarHeight(int topOff) {
        if (A.showColorTemplate) {
            this.hBar.layout(this.hBar.getLeft(), this.hBar.getTop() + topOff, this.hBar.getRight(), (((this.hBar.getTop() + this.hBar2.getHeight()) + A.d(70.0f)) + A.d(10.0f)) + topOff);
        } else {
            this.hBar.layout(this.hBar.getLeft(), this.hBar.getTop() + topOff, this.hBar.getRight(), ((this.hBar.getTop() + this.hBar2.getHeight()) + A.d(10.0f)) + topOff);
        }
        this.hBar.requestLayout();
    }

    private void copyToClipboard(String s) {
        try {
            ((ClipboardManager) getSystemService("clipboard")).setText(s);
            T.showToastText(this, getString(R.string.copy_to_clipboard), s.replace("\n", "<br>"), 0);
        } catch (Exception e) {
            A.error(e);
        }
    }

    private void showSelectBar() {
        this.cancelFade = true;
        this.hBar.setAnimation(null);
        this.dot1.setAnimation(null);
        this.dot2.setAnimation(null);
        showhBar();
        this.dot1.setVisibility(0);
        this.dot2.setVisibility(0);
    }

    public void showhBar() {
        if (this.checkhBarWidth) {
            setHBarVisible(true);
        } else {
            this.checkhBarWidth = true;
            if (((float) this.hMore.getWidth()) < this.hMore.getPaint().measureText(this.hMore.getText().toString())) {
                if (this.hBarFontSize == -1.0f) {
                    this.hBarFontSize = this.hMore.getTextSize() / A.getDensity();
                }
                float fs = this.hBarFontSize;
                int pd = fs >= 14.0f ? 4 : 2;
                this.hCopy.setTextSize(fs);
                this.hHighlight.setTextSize(fs);
                this.hNote.setTextSize(fs);
                this.hDict.setTextSize(fs);
                this.hMore.setTextSize(fs);
                this.hCopy.setPadding(pd, 0, pd, 0);
                this.hHighlight.setPadding(pd, 0, pd, 0);
                this.hNote.setPadding(pd, 0, pd, 0);
                this.hDict.setPadding(pd, 0, pd, 0);
                this.hMore.setPadding(pd, 0, pd, 0);
                new Handler() {
                    public void handleMessage(Message msg) {
                        int hw = (((((ActivityTxt.this.hCopy.getWidth() + ActivityTxt.this.hHighlight.getWidth()) + ActivityTxt.this.hNote.getWidth()) + ActivityTxt.this.hDict.getWidth()) + ActivityTxt.this.hMore.getWidth()) + (A.d(4.0f) * 4)) + 16;
                        if (ActivityTxt.this.isPdf()) {
                            hw += ActivityTxt.this.hPenWidth();
                        }
                        int hl = Math.abs(A.getScreenWidth2() - hw) / 2;
                        ActivityTxt.this.hBar.layout(hl, ActivityTxt.this.hBar.getTop(), hl + hw, ActivityTxt.this.hBar.getBottom());
                        new Handler() {
                            public void handleMessage(Message msg) {
                                if (((float) ActivityTxt.this.hMore.getWidth()) < ActivityTxt.this.hMore.getPaint().measureText(ActivityTxt.this.hMore.getText().toString())) {
                                    ActivityTxt activityTxt = ActivityTxt.this;
                                    activityTxt.hBarFontSize -= 1.0f;
                                    if (ActivityTxt.this.hBarFontSize > 10.0f) {
                                        ActivityTxt.this.checkhBarWidth = false;
                                        ActivityTxt.this.showhBar();
                                        return;
                                    }
                                }
                                ActivityTxt.this.setHBarVisible(true);
                            }
                        }.sendEmptyMessage(0);
                    }
                }.sendEmptyMessage(0);
            } else {
                setHBarVisible(true);
            }
        }
        if (A.showColorTemplate) {
            this.colorTemplate.postInvalidate();
        }
        if (this.hPen.getVisibility() != 0 && hPenWidth() > 0 && this.hCopy.getLeft() > hPenWidth() / 2) {
            updateHBarHandler();
        }
    }

    private void setHBarVisible(boolean visible) {
        A.moveStart = this.moveStart2;
        if (visible) {
            this.hBar.setBackgroundResource(A.isNightState() ? R.drawable.hbar : R.drawable.hbar2);
            int c = A.isNightState() ? -1118482 : -10395295;
            this.hCopy.setTextColor(c);
            this.hHighlight.setTextColor(c);
            this.hNote.setTextColor(c);
            this.hDict.setTextColor(c);
            this.hMore.setTextColor(c);
        }
        this.hBar.setVisibility(visible ? 0 : 4);
    }

    private void setHPenImage(boolean trash) {
        if (trash) {
            this.hPen.setImageResource(A.isNightState() ? R.drawable.trash : R.drawable.trash2);
        } else {
            this.hPen.setImageResource(R.drawable.pen);
        }
    }

    private void fadeSelectBar() {
        this.cancelFade = false;
        Animation animation1 = new AlphaAnimation(1.0f, 0.6f);
        animation1.setDuration(A.dict_index == 0 ? 4000 : 2500);
        animation1.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (!ActivityTxt.this.cancelFade) {
                    ActivityTxt.this.hideDotSelectors();
                    ActivityTxt.this.getTouchTxtView().postInvalidate();
                    ActivityTxt.this.setHBarVisible(false);
                    ActivityTxt.this.dot1.setVisibility(4);
                    ActivityTxt.this.dot2.setVisibility(4);
                }
            }
        });
        this.hBar.startAnimation(animation1);
        this.dot1.startAnimation(animation1);
        this.dot2.startAnimation(animation1);
    }

    private void doHighlight(boolean fromLongTap) {
        int mode = -1;
        if (isPdf()) {
            pdfDoHighlight(fromLongTap);
            return;
        }
        final MRTextView tv = A.touchingView == this.txtScroll2 ? this.txtView2 : this.txtView;
        if (!tv.hasHighlight()) {
            return;
        }
        if (this.preNoteInfo == null && fromLongTap) {
            doHighlight2(tv, -1, -1, true);
            return;
        }
        if (this.preNoteInfo != null) {
            mode = this.preNoteInfo.underline ? 1 : this.preNoteInfo.strikethrough ? 2 : this.preNoteInfo.squiggly ? 3 : 0;
        }
        new PrefSelectHighlight(this, new OnSelectColor() {
            @Override
            public void selectColor(int mode, boolean delete) {
                if (delete) {
                    ActivityTxt.this.removeCurrentNote(ActivityTxt.this.preNoteInfo);
                } else {
                    ActivityTxt.this.doHighlight2(tv, mode, -1, true);
                }
            }
        }, false, mode, tv.selectedText).show();
    }

    private void doHighlight2(MRTextView tv, int mode, int forceColor, boolean hideDots) {
        boolean z = true;
        if (isPdf()) {
            pdfDoHighlight2(-1);
            return;
        }
        boolean z2;
        if (tv == null) {
            tv = A.touchingView == this.txtScroll2 ? this.txtView2 : this.txtView;
        }
        if (mode == -1) {
            mode = A.highlightMode;
        }
        NoteInfo note = this.preNoteInfo != null ? this.preNoteInfo : createDotNoteInfo();
        String tag1 = "" + note.underline + note.strikethrough + note.squiggly + note.highlightColor;
        if (mode == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        note.underline = z2;
        if (mode == 2) {
            z2 = true;
        } else {
            z2 = false;
        }
        note.strikethrough = z2;
        if (mode != 3) {
            z = false;
        }
        note.squiggly = z;
        if (forceColor == -1) {
            note.highlightColor = A.highlight_color1;
            if (note.underline) {
                note.highlightColor = A.underline_color;
            }
            if (note.strikethrough) {
                note.highlightColor = A.strikethrough_color;
            }
            if (note.squiggly) {
                note.highlightColor = A.squiggly_color;
            }
        } else {
            note.highlightColor = forceColor;
        }
        if (this.preNoteInfo == null) {
            A.addNote(note);
        } else if (!(tag1.equals("" + note.underline + note.strikethrough + note.squiggly + note.highlightColor) || A.updateNote(note))) {
            A.refreshNotes();
        }
        if (hideDots) {
            hideDotViews();
        } else {
            this.preNoteInfo = note;
        }
        tv.hStart = -1;
        tv.postInvalidate();
        resetFlipCache(false, 500);
    }

    private NoteInfo createDotNoteInfo() {
        MRTextView tv = getTouchTxtView();
        return new NoteInfo(0, A.getBookName(), A.lastFile, A.lastChapter, A.lastSplitIndex, getBookType() == 0 ? A.getTxtRealPos((long) tv.hStart) : (long) tv.hStart, tv.hEnd - tv.hStart, A.highlight_color1, System.currentTimeMillis(), "", "", tv.selectedText, false, false, "");
    }

    private void removeCurrentNote(NoteInfo note) {
        if (note != null) {
            if (!A.removeNote(note)) {
                A.refreshNotes();
            }
            MRTextView tv = getTouchTxtView();
            tv.hStart = -1;
            hideDotViews();
            if (!T.isNull(note.original)) {
                A.deleteHighlightAllItem(note.original);
            }
            tv.postInvalidate();
            resetFlipCache(false, 500);
        }
    }

    protected void do_add_note() {
        if (isPdf()) {
            pdfDoAnnotation();
            return;
        }
        final NoteInfo fromNote = this.preNoteInfo != null ? this.preNoteInfo : createDotNoteInfo();
        new PrefEditNote(this, fromNote, false, new OnAfterEditNote() {
            public void AfterEditNote(int result, String text) {
                if (result == 1) {
                    A.addNote(fromNote);
                    ActivityTxt.this.hideDotViews();
                    MRTextView tv = ActivityTxt.this.getTouchTxtView();
                    tv.hStart = -1;
                    tv.postInvalidate();
                    ActivityTxt.this.createCachePageShotsHandler(DropboxServerException._400_BAD_REQUEST);
                }
            }
        }).show();
    }

    protected void do_edit_note(final NoteInfo note) {
        final String tag1 = note.note + note.highlightColor + note.strikethrough + note.squiggly + note.underline;
        new PrefEditNote(this, note, true, new OnAfterEditNote() {
            public void AfterEditNote(int result, String text) {
                if (result == 1) {
                    if (!tag1.equals(note.note + note.highlightColor + note.strikethrough + note.squiggly + note.underline)) {
                        A.updateNote(note);
                    }
                } else if (result == 2) {
                    ActivityTxt.this.removeCurrentNote(note);
                }
                ActivityTxt.this.contentLay.postInvalidate();
            }
        }).show();
    }

    private void doDictButton(boolean longTap) {
        try {
            String s = (!isPdf() || this.pdf.textReflow) ? getTouchTxtView().selectedText.replace(A.INDENT_CHAR, ' ').trim() : this.pdf.selectedText.trim();
            lookupWord(s, longTap);
        } catch (Exception e) {
            A.error(e);
        }
    }

    private void lookupWord(String word, boolean longTap) {
        if (!T.isNull(word)) {
            getSharedPreferences(A.DICT_HISTORY_FILE, 0).edit().putString(word, "").commit();
            openDictUrl(A.getDictUrl(longTap ? A.dict_index2 : A.dict_index), word);
        }
    }

    protected void showDictHistory() {
        SharedPreferences sp = getSharedPreferences(A.DICT_HISTORY_FILE, 0);
        final CharSequence[] list = new String[sp.getAll().size()];
        int i = 0;
        for (String key : sp.getAll().keySet()) {
            int i2 = i + 1;
            list[i] = key;
            i = i2;
        }
       new AlertDialog.Builder(this).setTitle((int) R.string.dict_history).setItems(list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityTxt.this.openDictUrl(A.getDictUrl(A.dict_index), list[which]);
            }
        }).setPositiveButton((int) R.string.clear_dict_history, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityTxt.this.getSharedPreferences(A.DICT_HISTORY_FILE, 0).edit().clear().commit();
            }
        }).setNegativeButton(17039360, null).show();
    }

    protected void openDictUrl(String url, String s) {
        Intent i;
        int offset;
        pdfSetImmersiveMode(true);
        if (url.equals("Lingvo")) {
            try {
                fadeSelectBar();
                i = new Intent("com.abbyy.mobile.lingvo.intent.action.TRANSLATE");
                i.putExtra("com.abbyy.mobile.lingvo.intent.extra.TEXT", s);
                if (this.highlightY != -1) {
                    if (A.isLandscape()) {
                        offset = 5 - this.txtView.getLineHeight();
                    } else {
                        offset = 5;
                    }
                    if (this.highlightY > getResources().getDisplayMetrics().heightPixels / 2) {
                        i.putExtra("com.abbyy.mobile.lingvo.intent.extra.HEIGHT", (this.highlightY - this.txtView.getLineHeight()) - offset);
                        i.putExtra("com.abbyy.mobile.lingvo.intent.extra.EXTRA_GRAVITY", 48);
                    } else {
                        i.putExtra("com.abbyy.mobile.lingvo.intent.extra.HEIGHT", ((getResources().getDisplayMetrics().heightPixels - this.highlightY) - this.txtView.getLineHeight()) - offset);
                        i.putExtra("com.abbyy.mobile.lingvo.intent.extra.EXTRA_GRAVITY", 80);
                    }
                }
                i.putExtra("com.abbyy.mobile.lingvo.intent.extra.MARGIN_LEFT", 4);
                i.putExtra("com.abbyy.mobile.lingvo.intent.extra.MARGIN_RIGHT", 4);
                i.putExtra("com.abbyy.mobile.lingvo.intent.extra.MARGIN_TOP", 4);
                i.putExtra("com.abbyy.mobile.lingvo.intent.extra.MARGIN_BOTTOM", 4);
                startActivity(i);
            } catch (Exception e) {
                if (A.isAmazonVersion) {
                    T.showAlertText(this, "Failed to load dictionary, please install this app firstly.");
                } else {
                    T.openAppInMarket(this, "com.abbyy.mobile.lingvo.market");
                }
            }
        }
        if (url.equals("FreeDictionary")) {
            try {
                i = new Intent("android.intent.action.VIEW");
                i.setClassName("org.freedictionary", "org.freedictionary.MainActivity");
                i.setData(Uri.parse(s));
                startActivity(i);
            } catch (Exception e2) {
                if (A.isAmazonVersion) {
                    T.showAlertText(this, "Failed to load dictionary, please install this app firstly.");
                } else {
                    T.openAppInMarket(this, "org.freedictionary");
                }
            }
        } else if (url.equals("Leo")) {
            try {
                i = new Intent("android.intent.action.VIEW");
                i.setClassName("org.leo.android.dict", "org.leo.android.dict.LeoDict");
                i.setData(Uri.parse(s));
                startActivity(i);
            } catch (Exception e3) {
                if (A.isAmazonVersion) {
                    T.showAlertText(this, "Failed to load dictionary, please install this app firstly.");
                } else {
                    T.openAppInMarket(this, "org.leo.android.dict");
                }
            }
        } else if (url.equals("YunCi")) {
            try {
                int i2;
                i = new Intent("android.intent.action.VIEW");
                i.setAction("com.yunci.search");
                i.putExtra("EXTRA_QUERY", s);
                String str = "EXTRA_GRAVITY";
                if (this.highlightY < getResources().getDisplayMetrics().heightPixels / 2) {
                    i2 = 80;
                } else {
                    i2 = 48;
                }
                i.putExtra(str, i2);
                startActivity(i);
            } catch (Exception e4) {
                T.openUrl(this, "http://www.yunci4.com/");
            }
        } else if (url.equals("ColorDict3")) {
            try {
                fadeSelectBar();
                i = new Intent("colordict.intent.action.SEARCH");
                i.putExtra("EXTRA_QUERY", s);
                if (this.highlightY != -1) {
                    if (A.isLandscape()) {
                        offset = 5 - this.txtView.getLineHeight();
                    } else {
                        offset = 5;
                    }
                    if (this.highlightY > getResources().getDisplayMetrics().heightPixels / 2) {
                        i.putExtra("EXTRA_HEIGHT", (this.highlightY - this.txtView.getLineHeight()) - offset);
                        i.putExtra("EXTRA_GRAVITY", 48);
                    } else {
                        i.putExtra("EXTRA_HEIGHT", ((getResources().getDisplayMetrics().heightPixels - this.highlightY) - this.txtView.getLineHeight()) - offset);
                        i.putExtra("EXTRA_GRAVITY", 80);
                    }
                }
                i.putExtra("EXTRA_MARGIN_LEFT", 4);
                i.putExtra("EXTRA_MARGIN_RIGHT", 4);
                i.putExtra("EXTRA_MARGIN_TOP", 4);
                i.putExtra("EXTRA_MARGIN_BOTTOM", 4);
                startActivity(i);
            } catch (Exception e5) {
                if (A.isAmazonVersion) {
                    T.showAlertText(this, "Failed to load ColorDict, please install this app firstly.");
                } else {
                    openMarketForDict("com.socialnmobile.colordict");
                }
            }
        } else if (url.equals("ColorDict")) {
            try {
                i = new Intent("android.intent.action.VIEW");
                i.setClassName("com.socialnmobile.colordict", "com.socialnmobile.colordict.activity.Main");
                i.setData(Uri.parse(s));
                startActivity(i);
            } catch (Exception e6) {
                if (A.isAmazonVersion) {
                    T.showAlertText(this, "Failed to load ColorDict, please install this app firstly.");
                } else {
                    openMarketForDict("com.socialnmobile.colordict");
                }
            }
        } else if (url.equals("Fora")) {
            try {
                i = new Intent("android.intent.action.VIEW");
                i.setAction("com.ngc.fora.action.LOOKUP");
                i.putExtra("HEADWORD", s);
                startActivity(i);
            } catch (Exception e7) {
                if (A.isAmazonVersion) {
                    T.showAlertText(this, "Failed to load Fora, please install this app firstly.");
                } else {
                    openMarketForDict("com.ngc.fora");
                }
            }
        } else if (url.equals("XinHua")) {
            try {
                i = new Intent();
                i.setClassName("hugh.android.app.zidian", "hugh.android.app.zidian.ZiDian");
                i.putExtra("zi", s);
                startActivity(i);
            } catch (Exception e8) {
                if (A.isAmazonVersion) {
                    T.showAlertText(this, "Failed to load dictionary, please install this app firstly.");
                } else {
                    openMarketForDict("hugh.android.app.zidian");
                }
            }
        } else {
            if (url.equals("Google")) {
                url = A.DICTIONARY_URL;
            }
            if (url.equals("Customized")) {
                url = A.my_dict_url;
            }
            if (!url.equals(A.TRANSLATION_URL) || !callGoogleTranslateApp(s)) {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url.replace("%s", Uri.encode(s)).replace("<SL>", A.sourceLanguage).replace("<TL>", A.destLanguage))));
                } catch (Exception e9) {
                    A.error(e9);
                }
            }
        }
    }

    private void openMarketForDict(final String packageName) {
       new AlertDialog.Builder(this).setMessage((int) R.string.dict_install).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (!T.openAppInMarket(ActivityTxt.this, packageName)) {
                    T.showAlertText(ActivityTxt.this, ActivityTxt.this.getString(R.string.error), ActivityTxt.this.getString(R.string.market_not_install));
                }
            }
        }).setNegativeButton(17039360, null).show();
    }

    public void customizeDict(Context context, boolean showMenu) {
        View layout = (ScrollView) LayoutInflater.from(context).inflate(R.layout.dictionary_select, null);
        final Spinner source_sp = (Spinner) layout.findViewById(R.id.sourceLangSp);
        final Spinner dest_sp = (Spinner) layout.findViewById(R.id.destLangSp);
        final Spinner dict_sp = (Spinner) layout.findViewById(R.id.cnDictSp);
        final Spinner dict_sp2 = (Spinner) layout.findViewById(R.id.cnDictSp2);
        final CheckBox cb = (CheckBox) layout.findViewById(R.id.openDict);
        final CheckBox cb2 = (CheckBox) layout.findViewById(R.id.oneChar);
        ((TextView) layout.findViewById(R.id.tv1)).setText(Html.fromHtml(getString(R.string.dictionary) + "1 <small>(" + getString(R.string.single_tap_dict) + ")</small>"));
        ((TextView) layout.findViewById(R.id.tv2)).setText(Html.fromHtml(getString(R.string.dictionary) + "2 <small>(" + getString(R.string.long_tap_dict) + ")</small>"));
        ((TextView) layout.findViewById(R.id.tv3)).setText(getString(R.string.google_translate));
        if (isPdf()) {
            cb.setVisibility(8);
        }
        cb2.setChecked(A.oneCharDict);
        cb2.setVisibility(A.isAsiaLanguage ? 0 : 8);
        source_sp.setSelection(A.getTranslationIndex(A.sourceLanguage));
        dest_sp.setSelection(A.getTranslationIndex(A.destLanguage));
        cb.setChecked(A.openDictDirect);
        String[] original = getResources().getStringArray(R.array.dict_list);
        final String[] items = new String[(original.length + 1)];
        for (int i = 0; i < original.length; i++) {
            items[i] = original[i];
        }
        items[items.length - 1] = getString(R.string.google_translate);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, 17367048, items);
        arrayAdapter.setDropDownViewResource(17367049);
        dict_sp.setAdapter(arrayAdapter);
        dict_sp2.setAdapter(arrayAdapter);
        if (A.dict_index >= items.length - 1) {
            A.dict_index = 0;
        }
        dict_sp.setSelection(A.dict_index == -1 ? items.length - 1 : A.dict_index);
        if (A.dict_index2 >= items.length - 1) {
            A.dict_index2 = 0;
        }
        dict_sp2.setSelection(A.dict_index2 == -1 ? items.length - 1 : A.dict_index2);
        final Context context2 = context;
        new Handler() {
            public void handleMessage(Message msg) {
                OnItemSelectedListener selectDictEvent = new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        if (position < items.length - 1 && A.getDictUrl(position).equals("Customized")) {
                            final EditText et = new EditText(context2);
                            et.setText(A.my_dict_url);
                            et.setSingleLine(true);
                           new AlertDialog.Builder(context2).setTitle(ActivityTxt.this.getString(R.string.my_dict_hint)).setView(et).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    A.my_dict_url = et.getText().toString();
                                }
                            }).show();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                };
                dict_sp.setOnItemSelectedListener(selectDictEvent);
                dict_sp2.setOnItemSelectedListener(selectDictEvent);
            }
        }.sendEmptyMessage(0);
        A.setSpinnerListStyle(layout);
        Builder builder =new AlertDialog.Builder(context);
        final Spinner spinner = dict_sp;
        final String[] strArr = items;
        final Spinner spinner2 = dict_sp2;
        final Context context3 = context;
        final boolean z = showMenu;
        builder.setTitle(getString(R.string.customize_dictionary)).setView(layout).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                A.sourceLanguage = A.getTranslationAbbr(source_sp.getSelectedItemPosition());
                A.destLanguage = A.getTranslationAbbr(dest_sp.getSelectedItemPosition());
                A.dict_index = spinner.getSelectedItemPosition();
                if (A.dict_index == strArr.length - 1) {
                    A.dict_index = -1;
                }
                A.dict_index2 = spinner2.getSelectedItemPosition();
                if (A.dict_index2 == strArr.length - 1) {
                    A.dict_index2 = -1;
                }
                A.openDictDirect = cb.isChecked();
                A.oneCharDict = cb2.isChecked();
                if (!A.translateInited) {
                    A.translateInited = true;
                    A.SaveOptions(context3);
                }
                if (z) {
                    ActivityTxt.this.doDictButton(false);
                }
            }
        }).setNegativeButton(17039360, null);
        if (A.isNightState()) {
            builder.setHideKeyboard(false);
        }
        builder.show();
    }

    private void highlightText(int start, int end) {
        highlightText(this.txtView, start, end);
        if (dualPageEnabled()) {
            highlightText(this.txtView2, start, end);
        }
        if (!this.isPaused && A.fitHardwareAccelerated) {
            this.contentLay.postInvalidate();
        }
        if (A.isBetaVersion && !A.isSpeaking && start >= 0 && start < this.txtView.getText().length()) {
            char c = this.txtView.getText().charAt(start);
            A.log("[" + c + "]ASCII:" + (c + 0) + " CharacterType:" + Character.getType(c) + " UnicodeBlock:" + UnicodeBlock.of(c));
        }
    }

    private void highlightText(MRTextView tv, int start, int end) {
        if (!A.isSpeaking) {
            end = adjustLastHighlightPosition(tv, end);
            if (start > end) {
                start = end;
            }
        }
        tv.hStart = start;
        tv.hEnd = end;
        if (!this.isPaused) {
            tv.postInvalidate();
        }
    }

    private int adjustLastHighlightPosition(MRTextView tv, int end) {
        if (isPdf()) {
            return end;
        }
        if (getBookType() != 100) {
            return end;
        }
        int maxLine = getRealLineCount() - 1;
        if (A.chapterEndPrompt && !A.trimBlankSpace) {
            int tmp = tv.getText2().indexOf(A.getChapterEndText());
            if (tmp > 0) {
                maxLine = tv.getLayout().getLineForOffset(tmp) - 1;
            }
        }
        int line = tv.getLayout().getLineForOffset(end);
        if (line > 0 && line > maxLine) {
            end = tv.getLayout().getLineVisibleEnd(maxLine);
        }
        return end;
    }

    public int additionalLeft() {
        if (A.touchingView == this.txtScroll2) {
            return this.txtScroll.getWidth() + this.txtLay.getPaddingLeft();
        }
        if (dualPageEnabled()) {
            return this.txtLay.getPaddingLeft();
        }
        return 0;
    }

    private int additionalLeft(MRTextView tv) {
        if (tv == this.txtView2) {
            return this.txtScroll.getWidth() + this.txtLay.getPaddingLeft();
        }
        if (dualPageEnabled()) {
            return this.txtLay.getPaddingLeft();
        }
        return 0;
    }

    private void highlightText(boolean setDotPosition, int offset1, int offset2) {
        if (!isPdf() || this.pdf.textReflow) {
            MyLayout lo = this.txtView.getLayout();
            if (lo != null) {
                MRTextView tv1;
                MRTextView tv2;
                ScrollView sv2;
                int line1;
                int line2;
                if (offset1 == -1 && dualPageEnabled()) {
                    tv1 = this.dot1.getLeft() + (this.dot1.getWidth() / 2) > this.baseFrame.getWidth() / 2 ? this.txtView2 : this.txtView;
                    tv2 = this.dot2.getLeft() + (this.dot1.getWidth() / 2) > this.baseFrame.getWidth() / 2 ? this.txtView2 : this.txtView;
                } else if (dualPageEnabled()) {
                    tv1 = offsetInTxtScroll2(offset1) ? this.txtView2 : this.txtView;
                    tv2 = offsetInTxtScroll2(offset2) ? this.txtView2 : this.txtView;
                } else {
                    tv2 = this.txtView;
                    tv1 = tv2;
                }
                ScrollView sv1 = tv1 == this.txtView2 ? this.txtScroll2 : this.txtScroll;
                if (tv2 == this.txtView2) {
                    sv2 = this.txtScroll2;
                } else {
                    sv2 = this.txtScroll;
                }
                int lh = tv1.getLineHeight();
                int sy1 = sv1.getScrollY();
                int sy2 = sv2.getScrollY();
                int pl = sv1.getPaddingLeft();
                int pt = sv1.getPaddingTop();
                if (offset1 == -1 || offset2 == -1) {
                    line1 = lo.getLineForVertical(((this.dot1.getTop() + sy1) - (lh / 3)) - pt);
                    line2 = lo.getLineForVertical(((this.dot2.getTop() + sy2) - (lh / 3)) - pt);
                    offset1 = tv1.getLineOffset(line1, (float) (((this.dot1.getLeft() - additionalLeft(tv1)) + (this.dot1.getWidth() / 2)) - pl));
                    offset2 = tv2.getLineOffset(line2, (float) (((this.dot2.getLeft() - additionalLeft(tv2)) + (this.dot1.getWidth() / 2)) - pl));
                    if (offset1 == -1 || offset2 == -1) {
                        return;
                    }
                }
                offset1 = adjustLastHighlightPosition(tv1, offset1);
                offset2 = adjustLastHighlightPosition(tv2, offset2);
                line1 = lo.getLineForOffset(offset1);
                line2 = lo.getLineForOffset(offset2 - 1);
                int textLength = tv1.getText().length();
                tv2.hEnd = -1;
                tv2.hStart = -1;
                tv1.hEnd = -1;
                tv1.hStart = -1;
                if (offset1 >= 0 && offset2 >= 0 && offset1 != offset2 && offset1 < textLength && offset2 <= textLength) {
                    int i;
                    boolean inverse = offset1 > offset2;
                    if (inverse) {
                        i = offset2;
                    } else {
                        i = offset1;
                    }
                    highlightText(i, inverse ? offset1 : offset2);
                    if (tv1.hStart > tv1.hEnd) {
                        A.log("ERROR tv1.hStart-End:" + tv1.hStart + "/" + tv1.hEnd);
                        return;
                    }
                    MRTextView mRTextView = this.txtView;
                    MRTextView mRTextView2 = this.txtView2;
                    String substring = this.txtView.getText2().substring(tv1.hStart, tv1.hEnd);
                    mRTextView2.selectedText = substring;
                    mRTextView.selectedText = substring;
                    if (setDotPosition) {
                        int dw = this.dot1.getWidth();
                        int dh = this.dot1.getHeight();
                        int l1 = (int) tv1.getTextX(line1, offset1);
                        int t1 = ((lo.getLineBottom(line1) + pt) - sy1) - 3;
                        this.highlightY = t1 - (lh / 2);
                        int l2 = (int) tv2.getTextX(line2, offset2);
                        int t2 = ((lo.getLineBottom(line2) + pt) - sy2) - 3;
                        if (tv1.getLayout().getParagraphDirection(line1) == -1) {
                            int l3 = l1;
                            if (tv1.selfDrawLine(line1)) {
                                l1 = ((l2 + pl) - (dw / 2)) + additionalLeft(tv2);
                                l2 = ((l3 + pl) - (dw / 2)) + additionalLeft(tv1);
                            } else {
                                l1 = (((tv1.getWidth2() - l2) + pl) - (dw / 2)) + additionalLeft(tv2);
                                l2 = (((tv1.getWidth2() - l3) + pl) - (dw / 2)) + additionalLeft(tv1);
                            }
                            l3 = t1;
                            t1 = t2;
                            t2 = l3;
                        } else {
                            l1 += (pl - (dw / 2)) + additionalLeft(tv1);
                            l2 += (pl - (dw / 2)) + additionalLeft(tv2);
                        }
                        this.dot1.layout(l1, t1, l1 + dw, t1 + dh);
                        this.dot2.layout(l2, t2, l2 + dw, t2 + dh);
                        layoutHBar();
                        if (this.preNoteInfo != null) {
                            this.hPen.setTag(Integer.valueOf(1));
                            setHPenImage(true);
                            setHPenVisible(0);
                            new Handler() {
                                public void handleMessage(Message msg) {
                                    int l = ActivityTxt.this.hBar.getLeft();
                                    int r = ActivityTxt.this.hBar.getRight() + ActivityTxt.this.hPenWidth();
                                    if (r > ActivityTxt.this.baseFrame.getWidth()) {
                                        int off = (r - ActivityTxt.this.baseFrame.getWidth()) + A.d(2.0f);
                                        l -= off;
                                        r -= off;
                                    }
                                    ActivityTxt.this.hBar.layout(l, ActivityTxt.this.hBar.getTop(), r, ActivityTxt.this.hBar.getBottom());
                                    ActivityTxt.this.hBar.requestLayout();
                                }
                            }.sendEmptyMessage(0);
                        } else if (hPenWidth() > 0 && this.hCopy.getLeft() > hPenWidth()) {
                            updateHBarHandler();
                        }
                        showDotViews();
                        return;
                    }
                    setHBarVisible(false);
                    return;
                }
                return;
            }
            return;
        }
        pdfHighlightText(setDotPosition, true);
    }

    private void setHPenVisible(int v) {
        if (this.hPen.getVisibility() != v) {
            updateHBarHandler();
        }
        this.hPen.setVisibility(v);
        findViewById(R.id.penSplit).setVisibility(v);
    }

    private void updateHBarHandler() {
        new Handler() {
            public void handleMessage(Message msg) {
                ActivityTxt.this.updateHBarHeight(0);
                ActivityTxt.this.hBar2.requestLayout();
            }
        }.sendEmptyMessageDelayed(0, 50);
    }

    private boolean offsetInTxtScroll2(int offset) {
        MyLayout lo = this.txtView2.getLayout();
        if (lo == null || this.txtView2.getVisibility() != 0) {
            return false;
        }
        int l = lo.getLineForOffset(offset);
        int l1 = lo.getLineForVertical(this.txtScroll2.getScrollY());
        int l2 = A.getLastDisplayLine(this.txtScroll2, l1);
        if (l < l1 || l > l2) {
            return false;
        }
        return true;
    }

    public void layoutHBar() {
        int t;
        int t1 = this.dot1.getTop();
        int t2 = this.dot2.getTop();
        if (t1 > t2) {
            t = t2;
            t2 = t1;
            t1 = t;
        }
        int dh = this.dot1.getHeight();
        int h = this.hBar.getHeight();
        int lh = this.txtView.getLineHeight();
        if (t1 > (h + lh) + A.d(20.0f)) {
            t = t1 - ((h + lh) + A.d(15.0f));
        } else if ((this.baseFrame.getHeight() - t2) - dh > h - 24) {
            t = t2 + dh;
        } else {
            t = ((t1 + dh) + ((((t2 - t1) - dh) - h) / 2)) - (dh / 5);
        }
        int w = this.hBar.getWidth();
        if (!isPdf() && this.hPen.getVisibility() == 0) {
            w -= hPenWidth();
        }
        int l = getHBarLeftFromDot();
        if (this.dot == null) {
            this.dot = this.dot1.getTop() > this.dot2.getTop() ? this.dot1 : this.dot2;
        }
        if (t < A.d(2.0f) || t > (this.baseFrame.getHeight() - h) - A.d(1.0f)) {
            t = (A.getScreenHeight2() - h) / 2;
        }
        if ((t < t1 && t + h > t1) || (t < t2 && t + h > t2)) {
            int l1 = this.dot1.getLeft();
            int l2 = this.dot2.getLeft();
            if (l1 > l2) {
                int x = l2;
                l2 = l1;
                l1 = x;
            }
            if (l2 - l1 < this.dot1.getWidth() + w) {
                if (l1 > w) {
                    l = l1 - w;
                } else if (l2 < (this.baseFrame.getWidth() - w) - this.dot1.getWidth()) {
                    l = l2 + this.dot1.getWidth();
                } else if (t1 > this.baseFrame.getHeight() - t2) {
                    t = A.d(2.0f);
                } else {
                    t = (this.baseFrame.getHeight() - h) - A.d(1.0f);
                }
            }
        }
        this.hBar.layout(l, t, l + w, t + h);
        this.hPen.setTag(Integer.valueOf(0));
        setHPenImage(false);
        setHPenVisible(isPdf() ? 0 : 8);
        showhBar();
    }

    private int hPenWidth() {
        return (this.hPen.getWidth() + A.d(4.0f)) + 1;
    }

    private int getHBarLeftFromDot() {
        int i1 = this.dot1.getLeft() > this.dot2.getLeft() ? this.dot2.getLeft() : this.dot1.getLeft();
        int i = ((this.dot1.getWidth() / 2) + i1) + (((this.dot1.getLeft() > this.dot2.getLeft() ? this.dot1.getLeft() : this.dot2.getLeft()) - i1) / 2);
        int w = this.hBar.getWidth();
        int sw = A.getScreenWidth2();
        if ((w / 2) + i < sw && i - (w / 2) > 5) {
            return i - (w / 2);
        }
        if ((w / 2) + i < sw) {
            return 5;
        }
        if (i - (w / 2) > 5) {
            return (sw - w) - 5;
        }
        return sw;
    }

    private void showDotViews() {
        this.dot1.setVisibility(0);
        this.dot2.setVisibility(0);
    }

    private boolean hideDotViews() {
        if (this.txtView == null) {
            return false;
        }
        this.txtView.hStart = -1;
        this.txtView2.hStart = -1;
        this.highlightY = -1;
        if (this.hBar == null || this.dot1 == null || this.dot2 == null) {
            return false;
        }
        if (this.hBar.getVisibility() != 0 && !dotVisible()) {
            return false;
        }
        this.txtView.postInvalidate();
        this.txtView2.postInvalidate();
        this.xFlingTime = SystemClock.elapsedRealtime();
        this.longTimeTapEvent = false;
        this.hBar.setAnimation(null);
        this.dot1.setAnimation(null);
        this.dot2.setAnimation(null);
        this.dot1.setVisibility(4);
        this.dot2.setVisibility(4);
        setHBarVisible(false);
        if (isPdf() && this.pdf != null) {
            this.pdf.recordDotsAct(null);
            this.pdf.pdfView.viewEnableTextSelection(false);
            PDFPage.delForceSel(true);
        }
        return true;
    }

    private void hideDotSelectors() {
        MRTextView tv = getTouchTxtView();
        int hStart = tv.hStart;
        int hEnd = tv.hEnd;
        hideDotViews();
        tv.hStart = hStart;
        tv.hEnd = hEnd;
    }

    private void doLongTimeTapEvent() {
        A.moveStart = this.moveStart2;
        if (this.web == null) {
            if ((!A.mult_touch || this.hMotionEvent.getPointerCount() < 2) && !isVisualBoomarkClick(this.hMotionEvent) && !isImageClick(this.hMotionEvent, false, true) && !isNoteHighlightClick(this.hMotionEvent) && !isUrlClick(this.hMotionEvent, false)) {
                showMovingChangeChapterState(false);
                if (A.doLongTap == 17) {
                    selectText(false);
                    if (A.showMagnifier) {
                        this.hBar.drawMagnifier(this.hMotionEvent);
                    }
                    lookupWordAutomatically();
                    return;
                }
                doEvent(A.doLongTap);
            }
        }
    }

    private void lookupWordAutomatically() {
        if (!isPdf() && A.openDictDirect && dotVisible()) {
            lookupWord(getTouchTxtView().selectedText.replace(A.INDENT_CHAR, ' ').trim(), false);
        }
    }

    public boolean dotVisible() {
        return this.dot1 != null && this.dot1.getVisibility() == 0;
    }

    private void selectText(boolean endWithTouch) {
        if (!isPdf() || this.pdf.textReflow) {
            ScrollView sv = A.touchingView == this.txtScroll2 ? this.txtScroll2 : this.txtScroll;
            MRTextView tv = getTouchTxtView();
            float setX = (float) (sv.getWidth() / 2);
            float setY = (float) (sv.getHeight() / 2);
            if (this.hMotionEvent != null) {
                try {
                    setX = this.hMotionEvent.getX();
                    setY = this.hMotionEvent.getY();
                } catch (Exception e) {
                    A.error(e);
                    return;
                }
            }
            int hy = (((int) setY) + sv.getScrollY()) - sv.getPaddingTop();
            float hx = setX - ((float) sv.getPaddingLeft());
            MyLayout lo = tv.getLayout();
            if (lo != null) {
                int line = lo.getLineForVertical(hy);
                if (line <= getRealLineCount() - 1) {
                    int offset = tv.getLineOffset(line, hx);
                    if (offset != -1) {
                        int j1 = lo.getLineStart(line);
                        int j2 = lo.getLineVisibleEnd(line);
                        if (offset >= j1 && offset < j2 && j1 != j2) {
                            this.preNoteInfo = A.getPreHighlight(offset);
                            int start;
                            if (this.preNoteInfo != null) {
                                start = (int) (getBookType() == 0 ? ((long) offset) - (A.getTxtRealPos((long) offset) - this.preNoteInfo.lastPosition) : this.preNoteInfo.lastPosition);
                                highlightText(true, start, this.preNoteInfo.highlightLength + start);
                                return;
                            }
                            int end;
                            if (!endWithTouch && A.oneCharDict && Character.getType(tv.getText().charAt(offset)) == 5) {
                                start = offset - 1;
                                end = offset + 1;
                                j1 = 0;
                                this.first_select_pos = (start + 0) + 1;
                            } else {
                                int i;
                                String s = tv.getText2().substring(j1, j2);
                                int pos = offset - j1;
                                while (pos > 0 && A.PUNCTUATIONS.indexOf(s.charAt(pos)) != -1) {
                                    pos--;
                                }
                                start = -1;
                                end = s.length();
                                if (!endWithTouch) {
                                    for (i = pos - 1; i >= 0; i--) {
                                        if (isSplitChar(s.charAt(i))) {
                                            start = i;
                                            break;
                                        }
                                    }
                                    this.first_select_pos = (start + j1) + 1;
                                }
                                for (i = pos + 1; i < s.length(); i++) {
                                    if (isSplitChar(s.charAt(i))) {
                                        end = i;
                                        break;
                                    }
                                }
                            }
                            this.select_text_state = true;
                            if (endWithTouch) {
                                highlightText(true, this.first_select_pos, end + j1);
                                return;
                            } else {
                                highlightText(true, (start + j1) + 1, end + j1);
                                return;
                            }
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        pdfSelectText(true);
    }

    private boolean isSplitChar(char c) {
        return A.PUNCTUATIONS.indexOf(c) != -1 || (A.oneCharDict && Character.getType(c) == 5);
    }

    protected void do_text_select(boolean lookup) {
        if (this.web != null) {
            this.web.selectAndCopyText();
            return;
        }
        inverseLayoutVisible(true);
        selectText(false);
        if (lookup) {
            lookupWordAutomatically();
        }
    }

    public void setFontSize() {
        if (this.txtView.getSpanned() == null || ((MyFloatSpan[]) this.txtView.getSpanned().getSpans(0, this.txtView.getText().length(), MyFloatSpan.class)).length <= 0) {
            this.handler.removeMessages(SET_FONT_SIZE);
            this.handler.sendEmptyMessageDelayed(SET_FONT_SIZE, (long) 250);
            return;
        }
        refreshTxtRender();
    }

    private void setFontSizeHandler() {
        if (this.web != null) {
            updateWebViewFontSize(false);
            return;
        }
        MyLayout layout = this.txtView.getLayout();
        if (layout != null) {
            saveLastPostion(false);
            int p = layout.getLineStart(layout.getLineForVertical(this.txtScroll.getScrollY()));
            txtViewSetTextSize(A.fontSize);
            txtScrollByDelay(p);
            statusHandlerSendMessage(1, 100);
            resetFlipCache(true, 500);
        }
    }

    private void txtScrollByDelay(int p) {
        this.handler.sendMessage(this.handler.obtainMessage(SCROLL_NO_DELAY, p, 0));
    }

    private void txtScrollNoDelay(int p) {
        MyLayout layout = this.txtView.getLayout();
        if (layout != null) {
            txtScrollTo(this.txtView.getLineTop2(layout.getLineForOffset(p)));
        }
    }

    private void doChromeButton(boolean fromLongTap) {
        if (fromLongTap && this.web != null && !T.isNull(this.htmlSrc)) {
            this.web.loadDataWithBaseURL("", "<pre>" + (this.htmlSrc + "\n@\n" + MRBookView.ebook_css).replace("<", "&lt;").replace(">", "&gt;").replace("\n@\n", "<br><hr><br>") + "</code>", "text/html", "UTF-8", null);
        } else if (isPdf()) {
            pdfTextReflowSwitch(true);
        } else {
            doWebViewSwitch(true);
        }
    }

    private void doWebViewSwitch(boolean reverse) {
        if (reverse) {
            A.useWebView = !A.useWebView;
        }
        this.chromeIv.setImageResource(A.useWebView ? R.drawable.mrbookview2 : R.drawable.chromebookview2);
        if (A.useWebView) {
            txtViewSetText("");
            System.gc();
            this.handler.post(this);
        } else if (this.web != null) {
            this.inversedWebLastChapter = A.lastChapter;
            this.inversedWebLastY = this.web.getScrollY();
            removeWebView();
            this.oldPriorChapterText = null;
            this.preNextChapterText = null;
            this.handler.post(this);
        }
        inverseLayoutVisible(true);
    }

    private void showInWebView(String html) {
        this.statusLay.setVisibility(8);
        createWebView();
        if (!this.isTxtScrollReady) {
            MRBookView mRBookView = this.web;
            MRBookView mRBookView2 = this.web;
            int i = A.lastWebY;
            mRBookView2.lastX = i;
            mRBookView.lastY = i;
        }
        if (this.inversedWebLastY != -1 && this.inversedWebLastChapter == A.lastChapter) {
            this.web.lastY = this.inversedWebLastY;
            this.inversedWebLastY = -1;
            this.inversedWebLastChapter = -1;
        }
        this.web.clearHistory();
        MRBookView.ebook_css = null;
        if (getBookType() == 1) {
            this.web.loadUrl("file://" + A.lastFile);
        } else if (isHtmlContent() && A.ebook.getChapters().size() != 0) {
            if (A.lastChapter >= A.ebook.getChapters().size()) {
                A.lastChapter = A.ebook.getChapters().size() - 1;
            }
            Chapter c = (Chapter) A.ebook.getChapters().get(A.lastChapter);
            MRBookView.ebook_css = c.css_str;
            WebSettings settings = this.web.getSettings();
            if (c.id_Tag != null || c.filename == null || c.filename.length() <= 0 || c.filename.endsWith(".xml") || c.filename.endsWith(".xhtml") || c.usedFiles.size() > 1) {
                T.saveFileText(A.tmp_out_file, "<html>" + html + "</html>");
                if (!(settings == null || settings.getDefaultTextEncodingName().equals("UTF-8"))) {
                    settings.setDefaultTextEncodingName("UTF-8");
                }
                this.web.loadPage(A.lastFile, A.tmp_out_file);
            } else {
                if (!(settings == null || settings.getDefaultTextEncodingName().equals(A.fileEncoding))) {
                    settings.setDefaultTextEncodingName(A.fileEncoding);
                }
                this.web.loadPage(A.lastFile, c.filename);
            }
            showProgressIndicator();
            this.web.clearHistory();
        }
    }

    public void webViewZoom(boolean in) {
        if (this.web != null) {
            if (in) {
                A.lastWebFontSize++;
            } else {
                A.lastWebFontSize--;
            }
            updateWebViewFontSize(true);
        }
    }

    public void updateWebViewFontSize(boolean showToast) {
        if (this.web != null) {
            if (A.lastWebFontSize < 1) {
                A.lastWebFontSize = 1;
            }
            if (A.lastWebFontSize > 72) {
                A.lastWebFontSize = 72;
            }
            this.web.getSettings().setDefaultFontSize(A.lastWebFontSize);
            if (showToast) {
                T.showToastText(this, "" + A.lastWebFontSize);
            }
            A.log("------new defaultFontSize:" + A.lastWebFontSize);
        }
    }

    private boolean disableFunctionsInWebView(View v) {
        boolean disable = this.web != null && (v == this.b_autoscroll || v == this.b_bookmark || v == this.b_speak || v == this.b_search);
        if (disable) {
           new AlertDialog.Builder(this).setMessage((int) R.string.preview_disable_hint).setPositiveButton((int) R.string.preview_turn_off, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityTxt.this.doChromeButton(false);
                }
            }).setNegativeButton((CharSequence) "Cancel", null).show();
        }
        return disable;
    }

    private void createWebView() {
        if (this.web == null) {
            this.web = new MRBookView(this);
            this.web.setFocusableInTouchMode(false);
            setWebViewVisual();
            this.web.setScrollBarStyle(0);
            this.web.getSettings().setDefaultTextEncodingName(A.fileEncoding);
            this.web.getSettings().setJavaScriptEnabled(true);
            this.web.getSettings().setCacheMode(1);
            this.web.getSettings().setBuiltInZoomControls(false);
            updateWebViewFontSize(false);
            this.baseFrame.addView(this.web, new LayoutParams(-1, -1));
            this.web.bringToFront();
            this.shadeView.bringToFront();
            initTopBottomLay();
            this.topLay.bringToFront();
            this.bottomLay.bringToFront();
        }
    }

    private void setWebViewVisual() {
        String background = "";
        if (A.useBackgroundImage) {
            this.web.setBackgroundColor(0);
        } else {
            background = "background-color:" + T.color2Html(A.backgroundColor) + ";";
        }
        MRBookView.css = MRBookView.CSS.replace("%BACKGROUND", background).replace("%COLOR", T.color2Html(A.fontColor)).replace("%LEFT", "" + A.vd((float) A.leftMargin)).replace("%RIGHT", "" + A.vd((float) A.rightMargin)).replace("%TOP", "" + A.vd((float) A.topMargin)).replace("%BOTTOM", "" + A.vd((float) A.bottomMargin));
    }

    private void reloadWebView() {
        int scrollY = this.web.getScrollY();
        this.inversedWebLastY = scrollY;
        A.lastWebY = scrollY;
        removeWebView();
        this.statusLay.setVisibility(8);
        createWebView();
        this.web.lastY = A.lastWebY;
        showInWebView(this.htmlSrc);
    }

    private void removeWebView() {
        if (this.web != null) {
            this.web.setVisibility(8);
            this.baseFrame.removeView(this.web);
            this.web.destroy();
            this.web = null;
            System.gc();
            if (showStatusbar()) {
                this.statusLay.setVisibility(0);
            }
        }
    }

    private void setChromeVisibility(boolean visible) {
        boolean show = true;
        if (this.pdfPenIv != null) {
            ImageView imageView = this.pdfPenIv;
            int i = (!A.pdf_annot_button || !isPdf() || this.pdf == null || this.pdf.textReflow) ? 8 : 0;
            imageView.setVisibility(i);
        }
        if (this.chromeIv != null) {
            if (!((isPdf() && A.pdf_text_button) || ((this.web != null || A.showChromeButton || A.getFileType() == 6 || A.getFileType() == 1) && visible && isHtmlContent()))) {
                show = false;
            }
            if (show) {
                ImageView imageView2 = this.chromeIv;
                i = isPdf() ? this.pdf.textReflow ? R.drawable.mrbookview2 : R.drawable.text_reflow : A.useWebView ? R.drawable.mrbookview2 : R.drawable.chromebookview2;
                imageView2.setImageResource(i);
                this.chromeIv.setVisibility(0);
                return;
            }
            this.chromeIv.setVisibility(8);
        }
    }

    private boolean isWebViewKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 && this.web != null && this.web.canGoBack()) {
            return true;
        }
        return false;
    }

    private boolean isWebViewKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 4 || this.web == null || !this.web.canGoBack()) {
            return false;
        }
        this.web.goBack();
        return true;
    }

    private boolean webViewPageDown() {
        if (this.web == null) {
            return false;
        }
        int x = this.web.getScrollX();
        int y = this.web.getScrollY();
        if (A.verticalAlignment) {
            if (x > 0) {
                this.web.scrollTo(x - (this.web.getWidth() - this.WEB_SCROLL_OFF), y);
            } else if (A.ebook != null && A.lastChapter < A.ebook.getChapters().size() - 1) {
                this.web.lastX = 0;
                do_next_chapter();
            }
        } else if (this.web.getHeight() + y < this.web.getContentHeight2()) {
            this.web.scrollTo(x, (this.web.getHeight() + y) - this.WEB_SCROLL_OFF);
        } else if (A.ebook != null && A.lastChapter < A.ebook.getChapters().size() - 1) {
            this.web.lastY = 0;
            do_next_chapter();
        }
        return true;
    }

    private boolean webViewPageUp() {
        if (this.web == null) {
            return false;
        }
        int x = this.web.getScrollX();
        int y = this.web.getScrollY();
        if (A.verticalAlignment) {
            if (this.web.getWidth() + x < this.web.getContentWidth2()) {
                this.web.scrollTo((this.web.getWidth() - this.WEB_SCROLL_OFF) + x, y);
            } else if (A.ebook != null && A.lastChapter > 0) {
                this.web.lastX = -1;
                do_prior_chapter();
            } else if (this.web.canGoBack()) {
                this.web.goBack();
            }
        } else if (y > 0) {
            this.web.scrollTo(x, y - (this.web.getHeight() - this.WEB_SCROLL_OFF));
        } else if (A.ebook != null && A.lastChapter > 0) {
            this.web.lastY = -1;
            do_prior_chapter();
        } else if (this.web.canGoBack()) {
            this.web.goBack();
        }
        return true;
    }

    private boolean webViewSearch() {
        if (this.web != null) {
            return true;
        }
        return false;
    }

    private void resetPageCount() {
        this.currentPage = -1;
        this.totalPages = -1;
    }

    private void showDictMoreMenu(final String s) {
        int i;
        String[] original = getResources().getStringArray(R.array.dictionary_list);
        if (A.isChinesePRC) {
            original[3] = "百度";
        }
        int length = original.length;
        if (A.isProVersion || A.inviteProVersion) {
            i = 2;
        } else {
            i = 1;
        }
        CharSequence[] items = new String[(i + length)];
        int i2;
        if (A.isProVersion || A.inviteProVersion) {
            items[0] = getString(R.string.speak);
            items[1] = getString(R.string.share);
            items[2] = getString(R.string.button_search);
            for (i2 = 1; i2 < original.length; i2++) {
                items[i2 + 2] = original[i2];
            }
        } else {
            items[0] = getString(R.string.share);
            items[1] = getString(R.string.button_search);
            for (i2 = 1; i2 < original.length; i2++) {
                items[i2 + 1] = original[i2];
            }
        }
        pdfSetImmersiveMode(true);
       new AlertDialog.Builder(this).setTitle((CharSequence) s).setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (A.isProVersion || A.inviteProVersion) {
                    if (which == 0) {
                        ActivityTxt.this.do_speak();
                    }
                    which--;
                }
                switch (which) {
                    case 0:
                        String text = s;
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.SUBJECT", ActivityTxt.this.getString(R.string.share_page) + ": " + A.getBookName());
                        intent.putExtra("android.intent.extra.TEXT", text);
                        ActivityTxt.this.startActivity(Intent.createChooser(intent, null));
                        return;
                    case 1:
                        ActivityTxt.this.showPrefSearch(s);
                        return;
                    case 2:
                        if (A.translateInited) {
                            ActivityTxt.this.openDictUrl(A.TRANSLATION_URL, s);
                            return;
                        } else {
                            ActivityTxt.this.customizeDict(ActivityTxt.this, false);
                            return;
                        }
                    case 3:
                        ActivityTxt.this.openDictUrl(A.getWikiUrl(), s);
                        return;
                    case 4:
                        ActivityTxt.this.openDictUrl(A.isChinesePRC ? A.BAIDUE_URL : A.GOOGLE_URL, s);
                        return;
                    case 5:
                        ActivityTxt.this.showDictHistory();
                        return;
                    case 6:
                        ActivityTxt.this.customizeDict(ActivityTxt.this, false);
                        return;
                    default:
                        return;
                }
            }
        }).setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                ActivityTxt.this.pdfSetImmersiveMode(false);
            }
        }).show();
    }

    private boolean callGoogleTranslateApp(String text) {
        if (!googleTranslateInstalled()) {
            return false;
        }
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setPackage("com.google.android.apps.translate");
            intent.setData(new Uri.Builder().scheme("http").authority("translate.google.com").path("/m/translate").appendQueryParameter("q", text).appendQueryParameter("tl", A.destLanguage).appendQueryParameter("sl", A.sourceLanguage).build());
            startActivity(intent);
            return true;
        } catch (Exception e) {
            A.error(e);
            return false;
        }
    }

    private boolean googleTranslateInstalled() {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.translate", 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    private boolean checkDualPageMode() {
        boolean enable = dualPageEnabled();
        if (enable) {
            this.txtScroll2.setVisibility(0);
            setBookStyleBackground();
            if (!(A.pageStyle2PageMode || this.dualPageBackground.getBackground() == null)) {
                this.dualPageBackground.setBackgroundDrawable(null);
                this.dualPageBackground.setVisibility(8);
            }
        } else if (this.txtScroll2.getVisibility() != 8) {
            this.txtView2.setText(null);
            this.txtScroll2.setVisibility(8);
            this.dualPageBackground.setBackgroundDrawable(null);
            this.dualPageBackground.setVisibility(8);
        }
        return enable;
    }

    public void setBookStyleBackground() {
        if (A.pageStyle2PageMode) {
            int dest = getDualDrawableRes();
            if (this.bookBackgroundStyle != dest) {
                this.bookBackgroundStyle = dest;
                if (dest == -1) {
                    this.dualPageBackground.setBackgroundDrawable(null);
                } else {
                    this.dualPageBackground.setBackgroundResource(dest);
                }
                if (this.dualPageBackground.getVisibility() != 0) {
                    this.dualPageBackground.setVisibility(0);
                }
            }
        } else if (this.dualPageBackground.getVisibility() != 8) {
            this.dualPageBackground.setVisibility(8);
        }
    }

    private int getDualDrawableRes() {
        if (isPureBlackBackground()) {
            return -1;
        }
        return A.isWhiteFont(A.fontColor) ? R.drawable.bookstyle_night : R.drawable.bookstyle;
    }

    public boolean dualPageEnabled() {
        return A.dualPageEnabled() && this.web == null && !isPdf();
    }

    private void setTxtView2Visible() {
        if (A.getLastDisplayLine(this.txtScroll, -1) < getRealLineCount() - 1) {
            txtScroll2ScrollTo(getDualPageNextY(this.txtScroll.getScrollY()));
        } else if (this.txtView2.getVisibility() != 4) {
            this.txtView2.setVisibility(4);
        }
    }

    private void txtScroll2ScrollTo(int y) {
        if (this.txtView2.getVisibility() != 0) {
            this.txtView2.setVisibility(0);
        }
        if (this.txtScroll2.getScrollY() != y) {
            fixBottomForScrollTo(y);
            this.txtScroll2.scrollTo(0, y);
            if (SystemClock.elapsedRealtime() - this.goToLastTime < 100) {
                verifiyBottomFixed(this.txtScroll.getScrollY(), this.txtView.getHeight());
                updateProgressStatus();
            }
        }
    }

    private void verifiyBottomFixed(int scrollY, int height) {
        if (this.veryifiHandler == null) {
            this.veryifiHandler = new Handler() {
                public void handleMessage(Message msg) {
                    if (ActivityTxt.this.txtScroll.getScrollY() != msg.arg1 || ActivityTxt.this.txtView.getHeight() < msg.arg2) {
                        A.log("*verifiyBottomFixed, txtView.height: " + ActivityTxt.this.txtView.getHeight() + ", should be: " + msg.arg2);
                        ActivityTxt.this.txtScrollTo(msg.arg1);
                    }
                }
            };
        }
        this.veryifiHandler.sendMessageDelayed(this.veryifiHandler.obtainMessage(0, scrollY, height), 100);
    }

    private int getDualPageNextY(int curY) {
        int curLine = this.txtView.getLayout().getLineForVertical(curY);
        int lastLine = A.getLastDisplayLine(this.txtScroll, -1);
        if (!this.txtView.isNormalImageLine(lastLine)) {
            lastLine++;
        }
        if (lastLine == curLine && curLine < this.txtView.getRealLineCount()) {
            lastLine++;
        }
        return this.txtView.getLineTop2(lastLine);
    }

    public int txtKeepPage(int add) {
        return dualPageEnabled() ? add + 2 : 1;
    }

    private boolean isDisableMove() {
        return A.disableMove || dualPageEnabled();
    }

    private boolean isPage2Visible() {
        return this.txtScroll2.getVisibility() == 0 && this.txtView2.getVisibility() == 0;
    }

    private void txtViewSetText(CharSequence text) {
        boolean z = false;
        this.yBeforePageDown.clear();
        if (A.missedFonts != null) {
            A.missedFonts.clear();
        }
        setCssBodyStyles();
        if (A.isJapanese && A.getFileType() == 0) {
            text = JpRubyTxt.dealJpRubyTxt(text);
        }
        this.txtView.setText(text);
        if (A.getFileType() != 2 || this.htmlSrc == null) {
            this.hasMediaTag = false;
        } else {
            if (!(this.htmlSrc.indexOf("<audio ") == -1 && this.htmlSrc.indexOf("<video ") == -1)) {
                z = true;
            }
            this.hasMediaTag = z;
            showCssMissedFontsTip();
        }
        this.contentLay.lastDrawY1 = -1;
        if (dualPageEnabled()) {
            this.txtView2.setVisibility(4);
            this.txtView2.setText(this.txtView.getText());
        }
        checkDualPageMode();
    }

    private void setCssBodyStyles() {
        MRTextView.global_alignment = 0;
        if (A.ebook != null && A.lastChapter <= A.ebook.getChapters().size() - 1) {
            Chapter c = (Chapter) A.ebook.getChapters().get(A.lastChapter);
            CSS css = c.css;
            Style bodyStyle = MyHtml.getClassStyle("body", c.css_body_id, c.css_body_class, c, null, null);
            if (c.css_body_style != null) {
                Style bodyStyle2 = new Style(bodyStyle);
                bodyStyle2.scanPropertyForStyle(c.css_body_style);
                bodyStyle = bodyStyle2;
            }
            String cssFont = null;
            Float fontSize = null;
            if (bodyStyle != null) {
                if (bodyStyle.align > 1) {
                    MRTextView.global_alignment = bodyStyle.align;
                }
                if (bodyStyle.fontFace != null) {
                    cssFont = bodyStyle.fontFace;
                    if (A.forceCssFontName == null || !A.forceCssFontName.equals(cssFont)) {
                        A.forceCssFontName = cssFont;
                        clearTxtView();
                        A.setTxtViewTypeface();
                    }
                }
                if (bodyStyle.fontSize > 0.0f && bodyStyle.fontSize != 1.0f) {
                    fontSize = Float.valueOf(bodyStyle.fontSize);
                }
            }
            if (fontSize != null) {
                if (fontSize != this.cssFontSize) {
                    clearTxtView();
                    this.cssFontSize = fontSize;
                    txtViewSetTextSize(A.fontSize);
                }
            } else if (this.cssFontSize != null) {
                this.cssFontSize = null;
                clearTxtView();
                txtViewSetTextSize(A.fontSize);
            }
            if (cssFont == null && A.forceCssFontName != null) {
                A.forceCssFontName = null;
                clearTxtView();
                A.setTxtViewTypeface();
            }
            if (css != null && bodyStyle != null && bodyStyle.backgroundImage != null && chapterHasOnlySingleFile(c)) {
                String s = bodyStyle.backgroundImage;
                if (s != this.cssBodyBackgrondImage) {
                    int i1 = s.indexOf("(");
                    int i2 = s.indexOf(")");
                    if (i1 != -1 && i2 > i1) {
                        Drawable d = A.ebook.getDrawableFromSource(s.substring(i1 + 1, i2), 0);
                        if (d != null) {
                            boolean repeat;
                            boolean contain;
                            boolean cover;
                            int dw;
                            int dh;
                            int w;
                            int h;
                            String property = bodyStyle.css_text;
                            if (property != null) {
                                if (property.contains("no-repeat")) {
                                    repeat = false;
                                    if (property != null) {
                                        if (property.contains("contain")) {
                                            contain = true;
                                            if (property != null) {
                                                if (property.contains("cover")) {
                                                    cover = true;
                                                    dw = d.getIntrinsicWidth();
                                                    dh = d.getIntrinsicHeight();
                                                    if (dw > 0 && dh > 0 && contain) {
                                                        w = this.txtScroll.getWidth();
                                                        h = this.txtScroll.getHeight();
                                                        if (dh / h <= dw / w) {
                                                            d = T.zoomDrawable(A.getContext().getResources(), d, (dw * h) / dh, h);
                                                        } else {
                                                            d = T.zoomDrawable(A.getContext().getResources(), d, w, (dh * w) / dw);
                                                        }
                                                    }
                                                    if (!cover && repeat && (d instanceof BitmapDrawable)) {
                                                        ((BitmapDrawable) d).setTileModeX(TileMode.REPEAT);
                                                        ((BitmapDrawable) d).setTileModeY(TileMode.REPEAT);
                                                    }
                                                    this.cssBodyBackgrondImage = s;
                                                    this.txtScroll.setBackgroundDrawable(d);
                                                    this.txtScroll2.setBackgroundDrawable(d);
                                                }
                                            }
                                            cover = false;
                                            dw = d.getIntrinsicWidth();
                                            dh = d.getIntrinsicHeight();
                                            w = this.txtScroll.getWidth();
                                            h = this.txtScroll.getHeight();
                                            if (dh / h <= dw / w) {
                                                d = T.zoomDrawable(A.getContext().getResources(), d, w, (dh * w) / dw);
                                            } else {
                                                d = T.zoomDrawable(A.getContext().getResources(), d, (dw * h) / dh, h);
                                            }
                                            ((BitmapDrawable) d).setTileModeX(TileMode.REPEAT);
                                            ((BitmapDrawable) d).setTileModeY(TileMode.REPEAT);
                                            this.cssBodyBackgrondImage = s;
                                            this.txtScroll.setBackgroundDrawable(d);
                                            this.txtScroll2.setBackgroundDrawable(d);
                                        }
                                    }
                                    contain = false;
                                    if (property != null) {
                                        if (property.contains("cover")) {
                                            cover = true;
                                            dw = d.getIntrinsicWidth();
                                            dh = d.getIntrinsicHeight();
                                            w = this.txtScroll.getWidth();
                                            h = this.txtScroll.getHeight();
                                            if (dh / h <= dw / w) {
                                                d = T.zoomDrawable(A.getContext().getResources(), d, (dw * h) / dh, h);
                                            } else {
                                                d = T.zoomDrawable(A.getContext().getResources(), d, w, (dh * w) / dw);
                                            }
                                            ((BitmapDrawable) d).setTileModeX(TileMode.REPEAT);
                                            ((BitmapDrawable) d).setTileModeY(TileMode.REPEAT);
                                            this.cssBodyBackgrondImage = s;
                                            this.txtScroll.setBackgroundDrawable(d);
                                            this.txtScroll2.setBackgroundDrawable(d);
                                        }
                                    }
                                    cover = false;
                                    dw = d.getIntrinsicWidth();
                                    dh = d.getIntrinsicHeight();
                                    w = this.txtScroll.getWidth();
                                    h = this.txtScroll.getHeight();
                                    if (dh / h <= dw / w) {
                                        d = T.zoomDrawable(A.getContext().getResources(), d, w, (dh * w) / dw);
                                    } else {
                                        d = T.zoomDrawable(A.getContext().getResources(), d, (dw * h) / dh, h);
                                    }
                                    ((BitmapDrawable) d).setTileModeX(TileMode.REPEAT);
                                    ((BitmapDrawable) d).setTileModeY(TileMode.REPEAT);
                                    this.cssBodyBackgrondImage = s;
                                    this.txtScroll.setBackgroundDrawable(d);
                                    this.txtScroll2.setBackgroundDrawable(d);
                                }
                            }
                            repeat = true;
                            if (property != null) {
                                if (property.contains("contain")) {
                                    contain = true;
                                    if (property != null) {
                                        if (property.contains("cover")) {
                                            cover = true;
                                            dw = d.getIntrinsicWidth();
                                            dh = d.getIntrinsicHeight();
                                            w = this.txtScroll.getWidth();
                                            h = this.txtScroll.getHeight();
                                            if (dh / h <= dw / w) {
                                                d = T.zoomDrawable(A.getContext().getResources(), d, (dw * h) / dh, h);
                                            } else {
                                                d = T.zoomDrawable(A.getContext().getResources(), d, w, (dh * w) / dw);
                                            }
                                            ((BitmapDrawable) d).setTileModeX(TileMode.REPEAT);
                                            ((BitmapDrawable) d).setTileModeY(TileMode.REPEAT);
                                            this.cssBodyBackgrondImage = s;
                                            this.txtScroll.setBackgroundDrawable(d);
                                            this.txtScroll2.setBackgroundDrawable(d);
                                        }
                                    }
                                    cover = false;
                                    dw = d.getIntrinsicWidth();
                                    dh = d.getIntrinsicHeight();
                                    w = this.txtScroll.getWidth();
                                    h = this.txtScroll.getHeight();
                                    if (dh / h <= dw / w) {
                                        d = T.zoomDrawable(A.getContext().getResources(), d, w, (dh * w) / dw);
                                    } else {
                                        d = T.zoomDrawable(A.getContext().getResources(), d, (dw * h) / dh, h);
                                    }
                                    ((BitmapDrawable) d).setTileModeX(TileMode.REPEAT);
                                    ((BitmapDrawable) d).setTileModeY(TileMode.REPEAT);
                                    this.cssBodyBackgrondImage = s;
                                    this.txtScroll.setBackgroundDrawable(d);
                                    this.txtScroll2.setBackgroundDrawable(d);
                                }
                            }
                            contain = false;
                            if (property != null) {
                                if (property.contains("cover")) {
                                    cover = true;
                                    dw = d.getIntrinsicWidth();
                                    dh = d.getIntrinsicHeight();
                                    w = this.txtScroll.getWidth();
                                    h = this.txtScroll.getHeight();
                                    if (dh / h <= dw / w) {
                                        d = T.zoomDrawable(A.getContext().getResources(), d, (dw * h) / dh, h);
                                    } else {
                                        d = T.zoomDrawable(A.getContext().getResources(), d, w, (dh * w) / dw);
                                    }
                                    ((BitmapDrawable) d).setTileModeX(TileMode.REPEAT);
                                    ((BitmapDrawable) d).setTileModeY(TileMode.REPEAT);
                                    this.cssBodyBackgrondImage = s;
                                    this.txtScroll.setBackgroundDrawable(d);
                                    this.txtScroll2.setBackgroundDrawable(d);
                                }
                            }
                            cover = false;
                            dw = d.getIntrinsicWidth();
                            dh = d.getIntrinsicHeight();
                            w = this.txtScroll.getWidth();
                            h = this.txtScroll.getHeight();
                            if (dh / h <= dw / w) {
                                d = T.zoomDrawable(A.getContext().getResources(), d, w, (dh * w) / dw);
                            } else {
                                d = T.zoomDrawable(A.getContext().getResources(), d, (dw * h) / dh, h);
                            }
                            ((BitmapDrawable) d).setTileModeX(TileMode.REPEAT);
                            ((BitmapDrawable) d).setTileModeY(TileMode.REPEAT);
                            this.cssBodyBackgrondImage = s;
                            this.txtScroll.setBackgroundDrawable(d);
                            this.txtScroll2.setBackgroundDrawable(d);
                        }
                    }
                }
            } else if (this.cssBodyBackgrondImage != null) {
                this.cssBodyBackgrondImage = null;
                this.txtScroll.setBackgroundDrawable(null);
                this.txtScroll2.setBackgroundDrawable(null);
            }
        }
    }

    private boolean chapterHasOnlySingleFile(Chapter c) {
        if (c.usedFiles.size() == 0) {
            return true;
        }
        if (c.usedFiles.size() != 1) {
            return false;
        }
        String f1 = c.filename;
        String f2 = (String) c.usedFiles.get(0);
        if (f1.endsWith(f2) || f2.endsWith(f1)) {
            return true;
        }
        return false;
    }

    private boolean showCssMissedFontsTip() {
        final ArrayList<String> fonts = isPdf() ? this.pdfMissedFonts : A.missedFonts;
        if (fonts != null && fonts.size() > 0) {
            try {
                String str;
                StringBuilder sb = new StringBuilder();
                Iterator it = fonts.iterator();
                while (it.hasNext()) {
                    String name = (String) it.next();
                    sb.append(" <a href=\"" + name + "\">" + name + ".ttf" + "</a>  ");
                }
                StringBuilder append = new StringBuilder().append("<b>");
                if (isPdf()) {
                    str = "PDF " + getString(R.string.button_options);
                } else {
                    str = getString(R.string.miscellaneous);
                }
                append = append.append(str).append(" -> ");
                if (isPdf()) {
                    str = getString(R.string.pdf_missed_font_hint);
                } else {
                    str = getString(R.string.epub_embedded_fonts);
                }
                String html = append.append(str).append("</b><br><br>").append(getString(R.string.epub_embedded_fonts_missied, new Object[]{"<b>" + sb.toString() + "</b><br>", "\"" + A.outerFontsFolder + "\""})).toString();
                if (this.cssFontDlg != null) {
                    this.cssFontDlg.dismiss();
                }
                this.cssFontDlg =new AlertDialog.Builder(this);
                this.handler.sendEmptyMessageDelayed(GPU_SHADOW, 50);
                this.cssFontDlg.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        A.setSystemUiVisibility(true);
                        ActivityTxt.this.eraseGPUShadow(0);
                    }
                });
                TextView tv = new TextView(this);
                tv.setTextSize(A.fontSize > 19.0f ? A.fontSize - 2.0f : A.fontSize);
                tv.setPadding(A.d(4.0f), A.d(4.0f), A.d(4.0f), A.d(4.0f));
                T.setTextViewLinkClickable(tv, html, new OnUrlClick() {
                    public void onClick(String url) {
                        boolean z = true;
                        ActivityTxt activityTxt = ActivityTxt.this;
                        if (fonts.size() <= 1) {
                            z = false;
                        }
                        activityTxt.downloadFont(url, z);
                    }
                });
                this.cssFontDlg.setView(tv).setPositiveButton((int) R.string.open_note, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (fonts != null && fonts.size() > 0) {
                            ActivityTxt.this.downloadFont((String) fonts.get(0), false);
                        }
                    }
                }).setNegativeButton((int) R.string.cancel, null).show();
                return true;
            } catch (Exception e) {
                A.error(e);
            }
        }
        return false;
    }

    private void downloadFont(final String font, final boolean restartTip) {
        new PrefDownloadCover(this, new OnSaveImage() {
            public void onGetImageFile(final String saveTo, Drawable d) {
                if (saveTo == null) {
                    return;
                }
                if (T.getOnlyFilename(saveTo).toLowerCase().equals(font.toLowerCase())) {
                    restartForFont();
                    return;
                }
               new AlertDialog.Builder(ActivityTxt.this).setMessage(Html.fromHtml("\"<b>" + T.getFilename(saveTo) + "</b>\" " + ActivityTxt.this.getString(R.string.rename_file) + " \"<b>" + font + ".ttf</b>\"?")).setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (T.renameFile(saveTo, T.getFilePath(saveTo) + "/" + font + ".ttf", true)) {
                            AnonymousClass122.this.restartForFont();
                        }
                    }
                }).setNegativeButton(17039360, null).show();
            }

            private void restartForFont() {
                if (restartTip) {
                   new AlertDialog.Builder(ActivityTxt.this).setMessage((int) R.string.use_css_font_now).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityTxt.this.saveLastPostion(true);
                            ActivityTxt.this.restartReaderToTxt();
                        }
                    }).setNegativeButton(17039360, null).show();
                    return;
                }
                ActivityTxt.this.saveLastPostion(true);
                ActivityTxt.this.restartReaderToTxt();
            }
        }, null, font.replace("\"", "").replace("'", "").trim() + (A.isChinese ? " 字体下载" : " ttf download"), 2).show();
    }

    private void restoreAppendedBottomHandler(boolean restore) {
        if (!A.isSpeaking && !A.isInAutoScroll && A.immersive_fullscreen && !isPdf() && this.web == null && A.sysHasNavBar() && dualPageEnabled() && lastLineDisplayed()) {
            disableTxtViewDraw();
            new Handler() {
                public void handleMessage(Message msg) {
                    ActivityTxt.this.txtScrollTo(msg.what);
                    ActivityTxt.this.enableTxtViewDraw();
                }
            }.sendEmptyMessageDelayed(this.txtScroll.getScrollY(), 100);
        }
    }

    private void txtScrollTo(int y) {
        initViewParams();
        if (this.txtScroll.getScrollY() != y) {
            fixBottomForScrollTo(y);
            this.txtScroll.scrollTo(0, y);
            this.contentLay.lastDrawY1 = -1;
        }
        if (checkDualPageMode()) {
            setTxtView2Visible();
        }
    }

    private void txtScrollSetPadding(int left, int top, int right, int bottom) {
        if (dualPageEnabled()) {
            if (A.pageStyle2PageMode) {
                int m1 = A.minDualPageMargin();
                if (left < m1) {
                    left = m1;
                }
            }
            right = left;
            int m2 = A.isTablet ? 12 : A.d(8.0f);
            if (paddingDifferent(this.txtLay, m2, 0, m2, 0)) {
                this.txtLay.setPadding(m2, 0, m2, 0);
            }
            if (paddingDifferent(this.txtScroll, left, top, right, bottom)) {
                setStatusBarPadding(m2, left, bottom);
                this.txtScroll.setPadding(left, top, right, bottom);
                this.txtScroll2.setPadding(left, top, right, bottom);
                return;
            }
            return;
        }
        if (paddingDifferent(this.txtLay, 0, 0, 0, 0)) {
            this.txtLay.setPadding(0, 0, 0, 0);
            setStatusBarPadding(0, 0, 0);
        }
        if (paddingDifferent(this.txtScroll, left, top, right, bottom)) {
            this.txtScroll.setPadding(left, top, right, bottom);
        }
    }

    private void setStatusBarPadding(int m2, int left, int bottom) {
        if (!showStatusbar()) {
            return;
        }
        if (m2 == 0) {
            this.status2Page.setVisibility(8);
            this.statusLay.setPadding(0, 0, 0, 0);
            setStatusBarProperties(false);
            return;
        }
        int p1 = m2 + ((left * 12) / 10);
        int b = (bottom / 2) - ((this.statusLay.getHeight() * 3) / 10);
        if (b < A.d(5.0f)) {
            b = A.d(5.0f);
        }
        if (A.isHighResulotionTablet()) {
            b -= A.d(4.0f);
        }
        this.statusLay.setPadding(p1 - A.d(3.0f), 0, A.d(1.0f) + p1, b);
        this.statusLayHori.setBackgroundDrawable(null);
        this.statusLeft.setBackgroundDrawable(null);
        this.status2Page.setVisibility(0);
        this.remainingLay.setVisibility(8);
        setStatusLayTextColorFor2PageMode();
    }

    public void setStatusLayTextColorFor2PageMode() {
        int color = A.fontColor;
        color = Color.argb(200, Color.red(color), Color.green(color), Color.blue(color));
        this.statusMiddle21.setTextColor(color);
        this.statusMiddle22.setTextColor(color);
        this.statusRight.setTextColor(color);
        this.statusLeft.setTextColor(color);
        this.statusLeft2.setTextColor(color);
        int size = A.statusFontSize;
        this.statusMiddle21.setTextSize((float) size);
        this.statusMiddle22.setTextSize((float) size);
        this.statusRight.setTextSize((float) (size - 1));
        this.statusLeft.setTextSize((float) (size - 1));
        this.statusLeft2.setTextSize((float) (size - 1));
        Typeface typeFace = A.getTypeFace(A.statusCustomizeFont ? A.statusFontName : "sans-serif", 0);
        this.statusMiddle21.setTypeface(typeFace);
        this.statusMiddle22.setTypeface(typeFace);
        this.statusLeft.setTypeface(typeFace);
        this.statusLeft2.setTypeface(typeFace);
        this.statusRight.setTypeface(typeFace);
    }

    private boolean paddingDifferent(View sv, int left, int top, int right, int bottom) {
        return (sv.getPaddingLeft() == left && sv.getPaddingTop() == top && sv.getPaddingRight() == right && sv.getPaddingBottom() == bottom) ? false : true;
    }

    private void checkLandscape2PagePadding() {
        if (dualPageEnabled()) {
            A.setTxtScrollMargin(this.txtScroll);
            txtScrollSetPadding(this.txtScroll.getPaddingLeft(), this.txtScroll.getPaddingTop(), this.txtScroll.getPaddingRight(), this.txtScroll.getPaddingTop());
        }
    }

    private void txtViewSetTextSize(float size) {
        if (this.cssFontSize != null) {
            size *= this.cssFontSize.floatValue();
        }
        this.txtView.setTextSize(size);
        this.txtView2.setTextSize(size);
        this.yBeforePageDown.clear();
    }

    private void loadTheme(String name, boolean forceOnlyColor) {
        boolean z = true;
        boolean oldMainNightTheme = A.mainNightTheme;
        resetFlipCache(true, 500);
        if (!isPdf() || this.pdf.textReflow) {
            boolean lastLineDisplayed;
            if (getBookType() == 100 && lastLineDisplayed()) {
                lastLineDisplayed = true;
            } else {
                lastLineDisplayed = false;
            }
            if (lastLineDisplayed) {
                saveLastPostion(true);
            }
            MyLayout layout = this.txtView.getLayout();
            int p = -1;
            if (!forceOnlyColor) {
                forceOnlyColor = A.themeOnlyColorDiffToCur(name);
            }
            if (!(forceOnlyColor || layout == null)) {
                p = layout.getLineStart(layout.getLineForVertical(this.txtScroll.getScrollY()));
            }
            A.loadTheme(name, forceOnlyColor);
            if (forceOnlyColor) {
                setStatusBarProperties(true);
            } else {
                checkStatusBar();
            }
            if (p != -1) {
                txtScrollByDelay(p);
            }
            if (checkDualPageMode()) {
                setTxtView2Visible();
            }
            if (lastLineDisplayed) {
                new Handler() {
                    public void handleMessage(Message msg) {
                        ActivityTxt.this.txtViewSetText(ActivityTxt.this.txtView.getText());
                        ActivityTxt.this.goToEBookLastPosition();
                    }
                }.sendEmptyMessage(0);
            }
        } else {
            pdfRemoveThumb();
            A.pdf_theme = name.equals(A.DAY_THEME) ? 1 : 0;
            pdfUpdateView(true);
            setStatusBarProperties(true);
        }
        if (isPdf() || A.lastTheme.equals(A.DAY_THEME) || A.lastTheme.equals(A.NIGHT_THEME)) {
            A.mainNightTheme = A.isNightState(false);
        }
        if (oldMainNightTheme == A.mainNightTheme) {
            z = false;
        }
        A.mainThemeChanged = z;
    }

    public boolean initClickTip(final boolean forceShow) {
        if (!forceShow && A.showedReaderClickTip) {
            return false;
        }
        A.showedReaderClickTip = true;
        new Handler() {
            public void handleMessage(Message msg) {
                int i = 0;
                try {
                    ClickTip.drawable = new BitmapDrawable(ActivityTxt.this.getResources(), ActivityTxt.this.getPageShot(false, false));
                    ActivityTxt activityTxt = ActivityTxt.this;
                    Intent intent = new Intent(ActivityTxt.this, ClickTip.class);
                    if (!forceShow) {
                        i = 123;
                    }
                    activityTxt.startActivityForResult(intent, i);
                } catch (Exception e) {
                    A.error(e);
                }
            }
        }.sendEmptyMessage(0);
        return true;
    }

    public Bitmap getPageShot(boolean hideStatusBar, boolean highQuality) {
        Bitmap bm = null;
        try {
            if ((this.flipView.getVisibility() == 0 || (this.curl3d != null && this.curl3d.getVisibility() == 0)) && this.txtView.getHeight() < A.getPageHeight() && this.pdf == null && !A.dualPageEnabled()) {
                this.txtView.setForceHeight(A.getPageHeight());
            }
            bm = Bitmap.createBitmap(this.baseFrame.getWidth(), this.baseFrame.getHeight(), highQuality ? Config.ARGB_8888 : Config.RGB_565);
            Canvas c = new Canvas(bm);
            A.setBackgroundImage(c);
            if (this.dualPageBackground.getVisibility() == 0) {
                this.dualPageBackground.draw(c);
            }
            this.contentLay.draw(c);
        } catch (OutOfMemoryError e) {
            A.error(e);
        }
        return bm;
    }

    private void statistics_add() {
        int i = 1;
        if (A.isInAutoScroll || this.pageDirection != 1) {
            return;
        }
        if (this.last_statistics_Y == -1 || this.last_statistics_Y != this.txtScroll.getScrollY() || this.last_statistics_Chapter != A.lastChapter) {
            if (SystemClock.elapsedRealtime() - this.last_statistics_add_time > 1000) {
                this.last_statistics_Y = this.txtScroll.getScrollY();
                this.last_statistics_Chapter = A.lastChapter;
                long j = A.statistics_pages;
                if (isPage2Visible()) {
                    i = 2;
                }
                A.statistics_pages = ((long) i) + j;
                try {
                    int count = getDisplayTextWords();
                    A.statistics_words += (long) count;
                    this.read_words += count;
                } catch (Exception e) {
                    A.error(e);
                }
            }
            this.last_statistics_add_time = SystemClock.elapsedRealtime();
        }
    }

    private int getDisplayTextWords() {
        MyLayout layout = this.txtView.getLayout();
        if (layout == null || layout.getLineCount() == 0) {
            return 0;
        }
        int p1 = layout.getLineStart(layout.getLineForVertical(this.txtScroll.getScrollY()));
        int line = A.getLastDisplayLine(isPage2Visible() ? this.txtScroll2 : this.txtScroll, -1);
        if (line > this.txtView.getRealLineCount() - 1) {
            line = this.txtView.getRealLineCount() - 1;
        }
        return T.getWordsCount(this.txtView.getText().subSequence(p1, layout.getLineVisibleEnd(line)).toString(), A.isAsiaLanguage);
    }

    private int getDisplayTextLength(MRTextView txtView, ScrollView txtScroll) {
        MyLayout layout = txtView.getLayout();
        return layout.getLineVisibleEnd(A.getLastDisplayLine(txtScroll, -1)) - layout.getLineStart(layout.getLineForVertical(txtScroll.getScrollY()));
    }

    public boolean onLongClick(final View v) {
        if (v == this.chromeIv) {
            if (this.web == null) {
                return false;
            }
            doChromeButton(true);
            return true;
        } else if (v == this.hHighlight) {
            doHighlight(true);
            return true;
        } else if (v == this.hDict) {
            showSelectBar();
            if (A.translateInited) {
                doDictButton(true);
            } else {
                customizeDict(this, true);
            }
            return true;
        } else if (v == this.statusLeftPanel) {
            return doEvent2(A.statusClickLeft2);
        } else {
            if (v == this.statusMiddle || v == this.statusMiddle21 || v == this.statusMiddle22) {
                return doEvent2(A.statusClickMiddle2);
            }
            if (v == this.statusRight) {
                return doEvent2(A.statusClickRight2);
            }
            if ((!A.isProVersion && !A.inviteProVersion) || v == this.b_option || v == this.b_visual || v == this.b_control || v == this.b_misc) {
                int id = -1;
                if (v == this.b_orientation) {
                    id = R.string.button_orientation;
                }
                if (v == this.b_daynight) {
                    id = R.string.button_daynight;
                }
                if (v == this.b_speak) {
                    id = R.string.button_speak;
                }
                if (v == this.b_autoscroll) {
                    id = R.string.button_autoscroll;
                }
                if (v == this.b_bookmark) {
                    id = R.string.button_bookmarks;
                }
                if (v == this.b_chapter) {
                    id = R.string.button_chapters;
                }
                if (v == this.b_search) {
                    id = R.string.button_search;
                }
                if (v == this.b_fontsize) {
                    id = R.string.button_fontsize;
                }
                if (v == this.b_shutdown) {
                    id = R.string.button_shutdown;
                }
                if (v == this.b_brightness) {
                    id = R.string.button_brightness;
                }
                if (v == this.b_tilt) {
                    id = R.string.tilt_turn_page;
                }
                if (v == this.b_option) {
                    id = R.string.button_options;
                }
                if (v == this.b_visual) {
                    id = R.string.visual_options;
                }
                if (v == this.b_control) {
                    id = R.string.control_options;
                }
                if (v == this.b_misc) {
                    id = R.string.miscellaneous;
                }
                if (id != -1) {
                    T.showToastText(this, getString(id));
                }
                return false;
            }
            String[] items;
            if (v == this.b_orientation) {
                String[] olds = getResources().getStringArray(R.array.ScreenOrientation2);
                items = new String[(VERSION.SDK_INT >= 9 ? 4 : 2)];
                for (int i = 0; i < items.length; i++) {
                    items[i] = olds[i + 1];
                }
                new MyDialog(this, v, items, getString(R.string.button_orientation), new MenuItemClick() {
                    public void onClick(int which) {
                        A.screenState = which + 1;
                        ActivityTxt.this.setRequestedOrientation(A.getScreenOrientation(A.screenState));
                    }
                }).show();
            }
            if (v == this.b_daynight) {
                items = new String[(isPdf() ? 3 : 4)];
                items[0] = getString(R.string.day_theme);
                items[1] = getString(R.string.night_theme);
                items[2] = getString(R.string.load_from_theme);
                if (!isPdf()) {
                    items[3] = getString(R.string.save_as_theme);
                }
                new MyDialog(this, v, items, getString(R.string.button_daynight), new MenuItemClick() {
                    public void onClick(int which) {
                        if (which == 0) {
                            ActivityTxt.this.loadTheme(A.DAY_THEME, true);
                        }
                        if (which == 1) {
                            ActivityTxt.this.loadTheme(A.NIGHT_THEME, true);
                        }
                        if (which == 0 || which == 1) {
                            ActivityTxt.this.setLeds();
                            if (ActivityTxt.this.web != null) {
                                ActivityTxt.this.reloadWebView();
                            }
                        }
                        if (which == 2) {
                            ActivityTxt.this.showThemeWindow();
                        }
                        if (which == 3) {
                            new PrefTheme(ActivityTxt.this, new OnGetTheme() {
                                public void getTheme(String name) {
                                    A.saveTheme(name, true);
                                    A.saveTheme(A.TEMP_THEME, true);
                                }
                            }, true).show();
                        }
                    }
                }).show();
            }
            if (v == this.b_speak) {
                new MyDialog(this, v, new String[]{getString(R.string.start_speak_button), getString(R.string.start_speak_button) + " (" + A.tts_stop_time + getString(R.string.tts_stop_time2) + ")", getString(R.string.speak_text_in_clipboard)}, getString(R.string.button_speak), new MenuItemClick() {
                    public void onClick(int which) {
                        boolean z = true;
                        ActivityTxt.this.kill_tts(false);
                        if (which == 2) {
                            CharSequence s = ((ClipboardManager) ActivityTxt.this.getSystemService("clipboard")).getText();
                            if (!T.isNull(s)) {
                                ActivityTxt.this.ttsTextFromClipboard = s.toString();
                                ActivityTxt.this.initTTS();
                                return;
                            }
                            return;
                        }
                        boolean z2;
                        ActivityTxt activityTxt = ActivityTxt.this;
                        if (which == 0) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        activityTxt.force_tts_not_stop = z2;
                        ActivityTxt activityTxt2 = ActivityTxt.this;
                        if (which != 1) {
                            z = false;
                        }
                        activityTxt2.force_tts_stop = z;
                        ActivityTxt.this.initTTS();
                    }
                }).show();
            }
            if (v == this.b_autoscroll && !isPdf()) {
                new MyDialog(this, v, getResources().getStringArray(R.array.auto_scroll_mode), getString(R.string.button_autoscroll), new MenuItemClick() {
                    public void onClick(int which) {
                        A.autoScrollMode = which;
                        ActivityTxt.this.do_AutoScroll(true);
                    }
                }).show();
            }
            if (v == this.b_bookmark) {
                new MyDialog(this, v, new String[]{getString(R.string.saved_bookmarks), getString(R.string.add_new_bookmark)}, getString(R.string.button_bookmarks), new MenuItemClick() {
                    public void onClick(int which) {
                        if (which == 0) {
                            ActivityTxt.this.do_bookmark();
                        }
                        if (which == 1) {
                            ActivityTxt.this.createBookmark();
                        }
                    }
                }).show();
            }
            if (v == this.b_chapter) {
                String[] tmps = getResources().getStringArray(R.array.more_options_menu);
                new MyDialog(this, v, new String[]{getString(R.string.total_chapters), tmps[4], tmps[5]}, getString(R.string.button_chapters), new MenuItemClick() {
                    public void onClick(int which) {
                        if (which == 0) {
                            ActivityTxt.this.do_show_chapters();
                        }
                        if (which == 1) {
                            ActivityTxt.this.do_prior_chapter();
                        }
                        if (which == 2) {
                            ActivityTxt.this.do_next_chapter();
                        }
                    }
                }).show();
            }
            if (v == this.b_search) {
                new MyDialog(this, v, new String[]{getString(R.string.button_search), getString(R.string.history)}, getString(R.string.button_search), new MenuItemClick() {
                    public void onClick(int which) {
                        if (which == 0) {
                            ActivityTxt.this.showPrefSearch(null);
                        }
                        if (which == 1) {
                            SharedPreferences sp = ActivityTxt.this.getSharedPreferences(A.SEARCH_HISTORY_FILE, 0);
                            final CharSequence[] list = new String[sp.getAll().size()];
                            int i = 0;
                            for (String key : sp.getAll().keySet()) {
                                int i2 = i + 1;
                                list[i] = key;
                                i = i2;
                            }
                           new AlertDialog.Builder(ActivityTxt.this).setTitle((int) R.string.history).setItems(list, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityTxt.this.showPrefSearch(list[which]);
                                }
                            }).setPositiveButton((int) R.string.clear_dict_history, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityTxt.this.getSharedPreferences(A.SEARCH_HISTORY_FILE, 0).edit().clear().commit();
                                }
                            }).setNegativeButton(17039360, null).show();
                        }
                    }
                }).show();
            }
            if (v == this.b_fontsize) {
                new MyDialog(this, v, new String[]{getString(R.string.font_name), getString(R.string.button_fontsize) + " +0.5", getString(R.string.button_fontsize) + " -0.5"}, getString(R.string.button_fontsize), new MenuItemClick() {
                    public void onClick(int which) {
                        if (which == 0) {
                            A.getLocalFontfaces(A.outerFontsFolder);
                            final String[] fonts = (String[]) A.localFontfaces.toArray(new String[A.localFontfaces.size()]);
                            new MyDialog(ActivityTxt.this, v, fonts, ActivityTxt.this.getString(R.string.font_name), new MenuItemClick() {
                                public void onClick(int which) {
                                    A.fontName = fonts[which];
                                    A.setTxtViewTypeface();
                                }
                            }).show();
                            return;
                        }
                        if (which == 1) {
                            A.fontSize += 1.0f;
                        }
                        if (which == 2) {
                            A.fontSize -= 1.0f;
                        }
                        ActivityTxt.this.setFontSize();
                    }
                }).show();
            }
            if (v == this.b_shutdown) {
                showExitMenu();
            }
            if (v == this.b_tilt) {
                PrefMisc.setTiltOptions(true);
            }
            if (v == this.b_brightness) {
                new MyDialog(this, v, new String[]{getString(R.string.button_brightness) + " " + getString(R.string.auto), getString(R.string.button_brightness) + " 100%", getString(R.string.button_brightness) + " 50%", getString(R.string.button_brightness) + " 1%", getString(R.string.button_brightness) + " +1", getString(R.string.button_brightness) + " -1"}, getString(R.string.button_brightness), new MenuItemClick() {
                    public void onClick(int which) {
                        if (which == 0) {
                            A.brightnessValue = -100;
                            ActivityTxt.this.setScreenBrightness(-100, false);
                        }
                        if (which == 1) {
                            ActivityTxt.this.setScreenBrightness(100, true);
                        }
                        if (which == 2) {
                            ActivityTxt.this.setScreenBrightness(50, true);
                        }
                        if (which == 3) {
                            ActivityTxt.this.setScreenBrightness(1, true);
                        }
                        if (which == 4) {
                            ActivityTxt.this.setScreenBrightness(A.brightnessValue + 1, true);
                        }
                        if (which == 5) {
                            ActivityTxt.this.setScreenBrightness(A.brightnessValue - 1, true);
                        }
                    }
                }).show();
            }
            return false;
        }
    }

    public boolean isPdf() {
        return getBookType() == 7;
    }

    public void pdfRemoveThumb() {
        if (isPdf() && this.pdf != null && this.pdfThumb != null) {
            inverseLayoutVisible(true);
            try {
                this.pdf.set_thumb(null);
                this.pdfThumb.setVisibility(8);
                if (((Integer) this.pdfThumb.getTag()).intValue() == Global.def_view) {
                    this.pdfThumb.thumbClose();
                }
                this.pdfBaseFrame.removeView(this.pdfThumb);
            } catch (Exception e) {
                A.error(e);
            }
            this.pdfThumb = null;
        }
    }

    private void pdfShowThumb(boolean show) {
        if (show) {
            if (!A.pdf_show_thumb || !isPdf() || this.pdf == null || this.pdf.textReflow) {
                if (this.pdfThumb != null) {
                    this.pdfThumb.setVisibility(8);
                }
            } else if (this.pdfThumb != null) {
                this.pdfThumb.setVisibility(0);
                this.pdfThumb.thumbGotoPage(pdfGetCurrPageNo());
                this.pdf.set_thumb(this.pdfThumb);
            } else if (this.pdf != null && this.pdf.pdfView != null) {
                this.pdfThumb = new PDFThumbView2(this, null);
                this.pdfThumb.setTag(Integer.valueOf(Global.def_view));
                LayoutParams lp = new LayoutParams(-1, A.d(A.isTablet ? 150.0f : 100.0f));
                lp.bottomMargin = this.bottomLay.getHeight();
                lp.gravity = 80;
                this.pdfBaseFrame.addView(this.pdfThumb, lp);
                this.pdfThumb.thumbOpen(this.m_doc, new PDFThumbListener() {
                    public void OnPageClicked(int pageno) {
                        ActivityTxt.this.pdfGotoPage(pageno, true);
                        ActivityTxt.this.percentView.setText("" + (pageno + 1) + "/" + ActivityTxt.this.m_doc.GetPageCount());
                    }
                });
                new Handler() {
                    public void handleMessage(Message msg) {
                        ActivityTxt.this.pdfShowThumb(true);
                    }
                }.sendEmptyMessage(0);
            }
        } else if (this.pdfThumb != null && this.pdfThumb.getVisibility() == 0) {
            this.pdfThumb.setVisibility(8);
        }
    }

    private void pdfInitLay() {
        if (this.pdfLay == null) {
            this.pdfLay = ((ViewStub) findViewById(R.id.pdfStub)).inflate();
            this.pdfBaseFrame = (FrameLayout) this.pdfLay.findViewById(R.id.base);
            this.pdf = (PDFReader) this.pdfLay.findViewById(R.id.PDFView);
            this.pdfSearchLay = this.pdfLay.findViewById(R.id.searchLay);
            this.pdfTopLay = this.pdfLay.findViewById(R.id.pdfTop);
            this.pdfBottomLay = this.pdfLay.findViewById(R.id.pdfBottom);
            this.antNote = (ImageView) this.pdfLay.findViewById(R.id.imageView1);
            this.antFreeText = (ImageView) this.pdfLay.findViewById(R.id.imageView2);
            this.antInk = (ImageView) this.pdfLay.findViewById(R.id.imageView3);
            this.antLine = (ImageView) this.pdfLay.findViewById(R.id.imageView4);
            this.antArrow = (ImageView) this.pdfLay.findViewById(R.id.imageView5);
            this.antRect = (ImageView) this.pdfLay.findViewById(R.id.imageView6);
            this.antEllipse = (ImageView) this.pdfLay.findViewById(R.id.imageView7);
            this.antCancel = (ImageView) this.pdfLay.findViewById(R.id.imageView8);
            this.apCancel = (ImageView) this.pdfLay.findViewById(R.id.ImageView01);
            this.apThickness = (ImageView) this.pdfLay.findViewById(R.id.ImageView02);
            this.apColor = (ImageView) this.pdfLay.findViewById(R.id.ImageView03);
            this.apFillColor = (ImageView) this.pdfLay.findViewById(R.id.ImageView04);
            this.apNote = (ImageView) this.pdfLay.findViewById(R.id.ImageView05);
            this.pdfTopLay.setVisibility(8);
            this.pdfBottomLay.setVisibility(8);
            this.pdfSearchEdit = (ClearableEditText) findViewById(R.id.keyEdit);
            pdfHideSearchLay();
            pdfInitAnnotButtons();
            if (A.sysHasNavBar()) {
                this.pdfSearchEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(View view, boolean hasFocus) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                A.setSystemUiVisibility(true);
                            }
                        }, 400);
                    }
                });
                this.pdfSearchEdit.setOnEditorActionListener(new OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        A.setSystemUiVisibility(true);
                        return false;
                    }
                });
            }
            this.pdfLockIv = (ImageView) this.pdfLay.findViewById(R.id.pdfLockIv);
            this.pdfLockIv.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    A.pdf_scoll_lock = !A.pdf_scoll_lock;
                    ActivityTxt.this.pdf.pdfView.viewLockSide(A.pdf_scoll_lock, ActivityTxt.this.pdf.getContext());
                    ActivityTxt.this.inverseLayoutVisible(true);
                }
            });
        }
    }

    private void pdfShowLockIcon(boolean visible) {
        if (this.pdfLockIv != null) {
            if (this.pdf.textReflow) {
                visible = false;
            }
            if (visible) {
                this.pdfLockIv.setImageResource(A.pdf_scoll_lock ? R.drawable.pdf_unlock : R.drawable.pdf_lock);
                LayoutParams lp = (LayoutParams) this.pdfLockIv.getLayoutParams();
                lp.bottomMargin = this.bottomLay.getHeight();
                if (A.pdf_show_thumb && this.pdfThumb != null) {
                    if (this.pdfThumb.getHeight() == 0) {
                        new Handler() {
                            public void handleMessage(Message msg) {
                                ActivityTxt.this.pdfShowLockIcon(true);
                            }
                        }.sendEmptyMessage(0);
                        return;
                    }
                    lp.bottomMargin += this.pdfThumb.getHeight();
                }
                lp.gravity = 85;
                lp.bottomMargin -= A.d(8.0f);
            }
            this.pdfLockIv.setVisibility(visible ? 0 : 8);
        }
    }

    private void pdfInitAnnotButtons() {
        if (this.onPdfAnnotButtonClick == null) {
            this.onPdfAnnotButtonClick = new OnClickListener() {
                public void onClick(View v) {
                    ActivityTxt.this.hideDotViews();
                    if (v.getId() == R.id.optionB) {
                        ActivityTxt.this.pdfSetImmersiveMode(true);
                        LinearLayout ll = (LinearLayout) LayoutInflater.from(ActivityTxt.this).inflate(R.layout.search_options, null);
                        ll.findViewById(R.id.forwardcb).setVisibility(8);
                        ll.findViewById(R.id.TextView02).setVisibility(8);
                        final CheckBox caseCb = (CheckBox) ll.findViewById(R.id.casecb);
                        final CheckBox wholeCb = (CheckBox) ll.findViewById(R.id.wholecb);
                        caseCb.setChecked(A.searchCaseSensitive);
                        wholeCb.setChecked(A.searchWholeWord);
                       new AlertDialog.Builder(ActivityTxt.this).setTitle((int) R.string.search_options).setView(ll).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                A.searchCaseSensitive = caseCb.isChecked();
                                A.searchWholeWord = wholeCb.isChecked();
                                ActivityTxt.this.pdfSearchString = null;
                                ActivityTxt.this.pdfSetImmersiveMode(false);
                            }
                        }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityTxt.this.pdfSetImmersiveMode(false);
                            }
                        }).setOnCancelListener(new OnCancelListener() {
                            public void onCancel(DialogInterface dialog) {
                                ActivityTxt.this.pdfSetImmersiveMode(false);
                            }
                        }).show();
                    }
                    if (v.getId() == R.id.priorB) {
                        ActivityTxt.this.pdfSearchText(false);
                    }
                    if (v.getId() == R.id.nextB) {
                        ActivityTxt.this.pdfSearchText(true);
                    }
                    if (A.isProVersion) {
                        Annotation annot;
                        ColorDialog dlg;
                        ActivityTxt.this.pdfSaveAnnotsForTime();
                        if (v == ActivityTxt.this.antNote) {
                            if (ActivityTxt.this.antSelected == ActivityTxt.this.antInk && A.pdfStatus == STATUS.sta_ink) {
                                ActivityTxt.this.pdf.annotEnd();
                            }
                            ActivityTxt.this.antSelected = ActivityTxt.this.antNote;
                            Global.tmpNoteText = null;
                            ActivityTxt.this.pdf.annotNote();
                            ActivityTxt.this.pdfShowAnnotLay();
                        }
                        if (v == ActivityTxt.this.antFreeText) {
                            if (ActivityTxt.this.antSelected == ActivityTxt.this.antInk && A.pdfStatus == STATUS.sta_ink) {
                                ActivityTxt.this.pdf.annotEnd();
                            }
                            ActivityTxt.this.pdfSaveAnnotsForSecurity();
                            ActivityTxt.this.antSelected = ActivityTxt.this.antFreeText;
                            Global.tmpNoteText = null;
                            ActivityTxt.this.pdf.annotFreeText();
                            ActivityTxt.this.pdfShowAnnotLay();
                        }
                        if (v == ActivityTxt.this.antInk) {
                            if (ActivityTxt.this.antSelected == ActivityTxt.this.antInk && A.pdfStatus == STATUS.sta_ink) {
                                ActivityTxt.this.pdf.annotEnd();
                            }
                            ActivityTxt.this.antSelected = ActivityTxt.this.antInk;
                            ActivityTxt.this.pdf.annotInk();
                            ActivityTxt.this.pdfShowAnnotLay();
                        }
                        if (v == ActivityTxt.this.antLine) {
                            if (ActivityTxt.this.antSelected == ActivityTxt.this.antInk && A.pdfStatus == STATUS.sta_ink) {
                                ActivityTxt.this.pdf.annotEnd();
                            }
                            ActivityTxt.this.antSelected = ActivityTxt.this.antLine;
                            ActivityTxt.this.pdf.annotLine();
                            ActivityTxt.this.pdfShowAnnotLay();
                        }
                        if (v == ActivityTxt.this.antArrow) {
                            if (ActivityTxt.this.antSelected == ActivityTxt.this.antInk && A.pdfStatus == STATUS.sta_ink) {
                                ActivityTxt.this.pdf.annotEnd();
                            }
                            ActivityTxt.this.antSelected = ActivityTxt.this.antArrow;
                            ActivityTxt.this.pdf.annotArrow();
                            ActivityTxt.this.pdfShowAnnotLay();
                        }
                        if (v == ActivityTxt.this.antRect) {
                            if (ActivityTxt.this.antSelected == ActivityTxt.this.antInk && A.pdfStatus == STATUS.sta_ink) {
                                ActivityTxt.this.pdf.annotEnd();
                            }
                            ActivityTxt.this.antSelected = ActivityTxt.this.antRect;
                            ActivityTxt.this.pdf.annotRect();
                            ActivityTxt.this.pdfShowAnnotLay();
                        }
                        if (v == ActivityTxt.this.antEllipse) {
                            if (ActivityTxt.this.antSelected == ActivityTxt.this.antInk && A.pdfStatus == STATUS.sta_ink) {
                                ActivityTxt.this.pdf.annotEnd();
                            }
                            ActivityTxt.this.antSelected = ActivityTxt.this.antEllipse;
                            ActivityTxt.this.pdf.annotEllipse();
                            ActivityTxt.this.pdfShowAnnotLay();
                        }
                        if (v == ActivityTxt.this.antCancel) {
                            if (ActivityTxt.this.antSelected == ActivityTxt.this.antInk && A.pdfStatus == STATUS.sta_ink) {
                                ActivityTxt.this.pdf.annotEnd();
                            }
                            A.pdfStatus = STATUS.sta_none;
                            ActivityTxt.this.antSelected = null;
                            Global.tmpNoteText = null;
                            ActivityTxt.this.pdfHideAnnotLay(true, true);
                        }
                        if (v == ActivityTxt.this.apCancel) {
                            if (ActivityTxt.this.pdf.pdfView.getSelectedAnnot() != null) {
                                A.pdfStatus = STATUS.sta_annot;
                                ActivityTxt.this.pdf.annotRemove();
                                A.pdfAnnotUpdated = true;
                            }
                            if (ActivityTxt.this.antSelected == ActivityTxt.this.antInk && A.pdfStatus == STATUS.sta_ink) {
                                ActivityTxt.this.pdf.annotEnd();
                            }
                            A.pdfStatus = STATUS.sta_none;
                            ActivityTxt.this.antSelected = null;
                            Global.tmpNoteText = null;
                            if (ActivityTxt.this.pdfTopLay.getVisibility() == 0) {
                                ActivityTxt.this.pdfShowAnnotLay();
                            } else {
                                ActivityTxt.this.pdfBottomLay.setVisibility(8);
                            }
                        }
                        if (v == ActivityTxt.this.apThickness) {
                            annot = ActivityTxt.this.pdf.pdfView.getSelectedAnnot();
                            CharSequence[] items = new String[20];
                            float GetStrokeWidth = annot == null ? ActivityTxt.this.antSelected == ActivityTxt.this.antFreeText ? Global.freeTextSize : Global.inkWidth : annot.GetStrokeWidth();
                            int cur = (int) GetStrokeWidth;
                            for (int i = 0; i < items.length; i++) {
                                int value = i + (ActivityTxt.this.antSelected == ActivityTxt.this.antFreeText ? 11 : 1);
                                items[i] = (value == cur ? MyDialog.OFFTAG : "") + value;
                            }
                            ActivityTxt.this.pdfSetImmersiveMode(true);
                           new AlertDialog.Builder(ActivityTxt.this).setTitle(ActivityTxt.this.antSelected == ActivityTxt.this.antFreeText ? R.string.font_size : R.string.pdf_pen_width).setItems(items, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (ActivityTxt.this.pdf.pdfView.getSelectedAnnot() != null) {
                                        ActivityTxt.this.pdf.pdfView.getSelectedAnnot().SetStrokeWidth((float) (which + 1));
                                        ActivityTxt.this.pdf.pdfView.refreshCurPage();
                                        A.pdfAnnotUpdated = true;
                                    } else if (ActivityTxt.this.antSelected == ActivityTxt.this.antFreeText) {
                                        Global.freeTextSize = (float) (which + 11);
                                    } else {
                                        if (A.pdfStatus == STATUS.sta_ink) {
                                            ActivityTxt.this.onPdfAnnotButtonClick.onClick(ActivityTxt.this.antInk);
                                        }
                                        Global.inkWidth = (float) (which + 1);
                                    }
                                    ActivityTxt.this.pdfSetImmersiveMode(false);
                                }
                            }).setOnCancelListener(new OnCancelListener() {
                                public void onCancel(DialogInterface dialog) {
                                    ActivityTxt.this.pdfSetImmersiveMode(false);
                                }
                            }).show();
                        }
                        if (v == ActivityTxt.this.apColor) {
                            ActivityTxt.this.pdfSetImmersiveMode(true);
                            annot = ActivityTxt.this.pdf.pdfView.getSelectedAnnot();
                            dlg = new ColorDialog(ActivityTxt.this, ActivityTxt.this.getString(R.string.highlight_color), true, annot == null ? Global.inkColor : annot.GetStrokeColor(), new OnSaveColor() {
                                public void getColor(int color) {
                                    Global.inkColor = color;
                                    if (ActivityTxt.this.pdf.pdfView.getSelectedAnnot() != null) {
                                        ActivityTxt.this.pdf.pdfView.getSelectedAnnot().SetStrokeColor(color);
                                        ActivityTxt.this.pdf.pdfView.refreshCurPage();
                                        A.pdfAnnotUpdated = true;
                                    } else if (A.pdfStatus == STATUS.sta_ink) {
                                        ActivityTxt.this.onPdfAnnotButtonClick.onClick(ActivityTxt.this.antInk);
                                    }
                                }
                            });
                            dlg.setOnDismissListener(new OnDismissListener() {
                                public void onDismiss(DialogInterface dialog) {
                                    A.setSystemUiVisibility(true);
                                    ActivityTxt.this.pdfSetImmersiveMode(false);
                                }
                            });
                            dlg.show();
                        }
                        if (v == ActivityTxt.this.apFillColor) {
                            ActivityTxt.this.pdfSetImmersiveMode(true);
                            annot = ActivityTxt.this.pdf.pdfView.getSelectedAnnot();
                            dlg = new ColorDialog(ActivityTxt.this, ActivityTxt.this.getString(R.string.fill_color), true, annot == null ? Global.fillColor : annot.GetFillColor(), new OnSaveColor() {
                                public void getColor(int color) {
                                    Global.fillColor = color;
                                    if (ActivityTxt.this.pdf.pdfView.getSelectedAnnot() != null) {
                                        ActivityTxt.this.pdf.pdfView.getSelectedAnnot().SetFillColor(color);
                                        ActivityTxt.this.pdf.pdfView.refreshCurPage();
                                        A.pdfAnnotUpdated = true;
                                    }
                                }
                            });
                            dlg.setOnDismissListener(new OnDismissListener() {
                                public void onDismiss(DialogInterface dialog) {
                                    A.setSystemUiVisibility(true);
                                    ActivityTxt.this.pdfSetImmersiveMode(false);
                                }
                            });
                            dlg.show();
                        }
                        if (v == ActivityTxt.this.apNote) {
                            ActivityTxt.this.pdfSetImmersiveMode(true);
                            final EditText et = new EditText(ActivityTxt.this);
                            annot = ActivityTxt.this.pdf.pdfView.getSelectedAnnot();
                            CharSequence GetEditText = annot == null ? Global.tmpNoteText == null ? "" : Global.tmpNoteText : annot.GetType() == 3 ? annot.GetEditText() : annot.GetPopupText();
                            et.setText(GetEditText);
                           new AlertDialog.Builder(ActivityTxt.this).setView(et).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String text = et.getText().toString();
                                    Annotation annot = ActivityTxt.this.pdf.pdfView.getSelectedAnnot();
                                    if (annot != null) {
                                        if (annot.GetType() == 3) {
                                            annot.SetEditText(text);
                                            ActivityTxt.this.pdf.pdfView.refreshCurPage();
                                        } else {
                                            annot.SetPopupText(text);
                                        }
                                        A.pdfAnnotUpdated = true;
                                    } else if (text.length() > 0) {
                                        Global.tmpNoteText = text;
                                    } else {
                                        Global.tmpNoteText = null;
                                    }
                                }
                            }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityTxt.this.pdfSetImmersiveMode(false);
                                }
                            }).setOnCancelListener(new OnCancelListener() {
                                public void onCancel(DialogInterface dialog) {
                                    ActivityTxt.this.pdfSetImmersiveMode(false);
                                }
                            }).show();
                            return;
                        }
                        return;
                    }
                    A.showBuyDialog(ActivityTxt.this, ActivityTxt.this.getString(R.string.pro_only));
                }
            };
            this.pdfLay.findViewById(R.id.optionB).setOnClickListener(this.onPdfAnnotButtonClick);
            this.pdfLay.findViewById(R.id.priorB).setOnClickListener(this.onPdfAnnotButtonClick);
            this.pdfLay.findViewById(R.id.nextB).setOnClickListener(this.onPdfAnnotButtonClick);
            this.antNote.setOnClickListener(this.onPdfAnnotButtonClick);
            this.antFreeText.setOnClickListener(this.onPdfAnnotButtonClick);
            this.antInk.setOnClickListener(this.onPdfAnnotButtonClick);
            this.antLine.setOnClickListener(this.onPdfAnnotButtonClick);
            this.antArrow.setOnClickListener(this.onPdfAnnotButtonClick);
            this.antRect.setOnClickListener(this.onPdfAnnotButtonClick);
            this.antEllipse.setOnClickListener(this.onPdfAnnotButtonClick);
            this.antCancel.setOnClickListener(this.onPdfAnnotButtonClick);
            this.apCancel.setOnClickListener(this.onPdfAnnotButtonClick);
            this.apThickness.setOnClickListener(this.onPdfAnnotButtonClick);
            this.apColor.setOnClickListener(this.onPdfAnnotButtonClick);
            this.apFillColor.setOnClickListener(this.onPdfAnnotButtonClick);
            this.apNote.setOnClickListener(this.onPdfAnnotButtonClick);
            this.antSelected = null;
        }
    }

    private void pdfShowAnnotLay() {
        int i = 8;
        this.pdfTopLay.setVisibility(0);
        pdfSetAnnotButtonBackground();
        View view = this.pdfBottomLay;
        int i2 = (this.antSelected == null || this.antSelected == this.antNote) ? 8 : 0;
        view.setVisibility(i2);
        this.apCancel.setImageResource(R.drawable.pdf_ok);
        ImageView imageView = this.apThickness;
        if (this.antSelected == this.antNote) {
            i2 = 8;
        } else {
            i2 = 0;
        }
        imageView.setVisibility(i2);
        imageView = this.apColor;
        if (this.antSelected == this.antNote) {
            i2 = 8;
        } else {
            i2 = 0;
        }
        imageView.setVisibility(i2);
        imageView = this.apFillColor;
        if (this.antSelected == this.antRect || this.antSelected == this.antEllipse) {
            i2 = 0;
        } else {
            i2 = 8;
        }
        imageView.setVisibility(i2);
        ImageView imageView2 = this.apNote;
        if (this.antSelected != this.antFreeText) {
            i = 0;
        }
        imageView2.setVisibility(i);
    }

    private void pdfSetAnnotButtonBackground() {
        this.antNote.setBackgroundResource(R.drawable.my_list_selector);
        this.antFreeText.setBackgroundResource(R.drawable.my_list_selector);
        this.antInk.setBackgroundResource(R.drawable.my_list_selector);
        this.antLine.setBackgroundResource(R.drawable.my_list_selector);
        this.antArrow.setBackgroundResource(R.drawable.my_list_selector);
        this.antRect.setBackgroundResource(R.drawable.my_list_selector);
        this.antEllipse.setBackgroundResource(R.drawable.my_list_selector);
        this.antNote.setPadding(A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED));
        this.antFreeText.setPadding(A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED));
        this.antInk.setPadding(A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED));
        this.antLine.setPadding(A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED));
        this.antArrow.setPadding(A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED));
        this.antRect.setPadding(A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED));
        this.antEllipse.setPadding(A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED), A.d(CSS.FLOAT_ADDED));
        if (this.antSelected == this.antNote) {
            this.antNote.setBackgroundColor(-293042040);
        }
        if (this.antSelected == this.antFreeText) {
            this.antFreeText.setBackgroundColor(-293042040);
        }
        if (this.antSelected == this.antInk) {
            this.antInk.setBackgroundColor(-293042040);
        }
        if (this.antSelected == this.antLine) {
            this.antLine.setBackgroundColor(-293042040);
        }
        if (this.antSelected == this.antArrow) {
            this.antArrow.setBackgroundColor(-293042040);
        }
        if (this.antSelected == this.antRect) {
            this.antRect.setBackgroundColor(-293042040);
        }
        if (this.antSelected == this.antEllipse) {
            this.antEllipse.setBackgroundColor(-293042040);
        }
    }

    protected boolean pdfHideAnnotLay(boolean hideTop, boolean hideBottom) {
        if (!isPdf()) {
            return false;
        }
        boolean ok = false;
        if (hideBottom && this.pdfBottomLay != null && this.pdfBottomLay.getVisibility() == 0) {
            this.pdfBottomLay.setVisibility(8);
            ok = true;
        }
        if (!hideTop || this.pdfTopLay == null || this.pdfTopLay.getVisibility() != 0) {
            return ok;
        }
        this.pdfTopLay.setVisibility(8);
        return true;
    }

    private void pdfSearchText(boolean forward) {
        int i = 1;
        A.lastSearchKey = this.pdfSearchEdit.getText().toString();
        PDFReader pDFReader;
        if (this.pdfSearchString != null && A.lastSearchKey.compareTo(this.pdfSearchString) == 0) {
            pDFReader = this.pdf;
            if (!forward) {
                i = -1;
            }
            pDFReader.find(i);
        } else if (A.lastSearchKey.length() > 0) {
            this.pdfSearchString = A.lastSearchKey;
            this.pdf.findStart(A.lastSearchKey, A.searchCaseSensitive, A.searchWholeWord);
            pDFReader = this.pdf;
            if (!forward) {
                i = -1;
            }
            pDFReader.find(i);
        }
    }

    private void pdfHideLay() {
        if (this.pdfLay != null) {
            this.pdfLay.setVisibility(8);
        }
    }

    private void pdfShowSearchLay() {
        if (this.pdfSearchLay != null) {
            inverseLayoutVisible(true);
            this.statusLay.setVisibility(8);
            this.pdfSearchLay.setVisibility(0);
            this.pdfSearchEdit.setText(A.lastSearchKey);
        }
    }

    private boolean pdfHideSearchLay() {
        if (this.pdfSearchLay == null || this.pdfSearchLay.getVisibility() != 0) {
            return false;
        }
        this.pdfSearchLay.setVisibility(8);
        if (showStatusbar()) {
            this.statusLay.setVisibility(0);
        }
        return true;
    }

    protected void pdfOpen(String filename) {
        if (Global.isPriorInited(this)) {
            this.pdfInited = Global.Init(this, A.lastFile);
            pdfOpen2(filename);
            return;
        }
        createProgressDlg("", getString(R.string.pdf_init));
        new Thread() {
            public void run() {
                ActivityTxt.this.pdfInited = Global.Init(ActivityTxt.this, A.lastFile);
                ActivityTxt.this.handler.sendEmptyMessage(ActivityTxt.SHOW_PDF2);
            }
        }.start();
    }

    private void pdfOpen2(String filename) {
        if (this.pdfInited) {
            A.SaveOptions(this);
            pdfInitLay();
            this.txtLay.setVisibility(8);
            removeWebView();
            pdfOpen3(filename);
            return;
        }
        A.SaveOptions(this);
       new AlertDialog.Builder(this).setTitle((CharSequence) "PDF").setMessage(getString(R.string.pdf_init_error, new Object[]{Global.PDF_VER})).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (T.openAppInMarket(ActivityTxt.this, "com.flyersoft.plugin_pdf")) {
                    ActivityTxt.this.doFinish();
                } else {
                    ActivityTxt.this.error_and_exit(ActivityTxt.this.getString(R.string.market_not_install), true);
                }
            }
        }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityTxt.this.doFinish();
            }
        }).setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                ActivityTxt.this.doFinish();
            }
        }).show();
    }

    private void pdfOpen3(String filename) {
        int i = 0;
        if (this.m_doc != null) {
            restartReaderToTxt();
            return;
        }
        filename = BaseCompressor.getZRCacheFile(filename);
        this.m_doc = new Document();
        String upassword = "";
        SharedPreferences sp = getSharedPreferences("pdf", 0);
        if (sp.contains(filename + "s")) {
            upassword = A.getDecryptText(sp.getString(filename + "u", ""));
            if (!sp.getBoolean(filename + "s", false)) {
                sp.edit().remove(filename + "s").commit();
            }
        }
        String errorMsg = null;
        switch (this.m_doc.Open(filename, upassword)) {
            case -10:
                errorMsg = "Access denied or invalid file path.";
                break;
            case -3:
                errorMsg = "Damaged or invalid format.";
                break;
            case -2:
                errorMsg = "Unknown encryption.";
                break;
            case -1:
                pdfCheckPassword(filename);
                return;
            case 0:
                break;
            default:
                errorMsg = "Unknown error";
                break;
        }
        A.pdfAnnotUpdated = false;
        if (errorMsg != null) {
            error_and_exit(errorMsg, true);
            return;
        }
        pdfFontDelegate();
        hideProgressDlg();
        pdfSetProperties(false);
        this.pdf.open(this.m_doc, filename);
        this.pdf.pdfView.viewEnableTextSelection(false);
        this.pdf.setViewListener(this.pdf);
        this.pdf.setAnnotListener(pdfOnAnnoListener());
        Global.selColor = this.pdf.RGB2PDfColor(A.highlight_color1, 80, -40);
        this.pdf_title = this.m_doc.GetMeta("Title");
        A.lastSplitIndex = 0;
        A.lastChapter = 0;
        boolean clickTipShowed = initClickTip(false);
        if (A.lastPosition == 0) {
            pdfGetToc();
            if (!(clickTipShowed || this.ignoreChapterListAtBegin || this.pdfToc.size() <= 3)) {
                do_show_chapters();
            }
        } else {
            pdfGotoPage((int) A.lastPosition, false);
        }
        if (Global.def_view == 0) {
            i = 200;
        }
        pdfLoadLastPosition(i);
        loadNotes();
        checkStatusBar();
    }

    private void pdfFontDelegate() {
        this.m_doc.SetFontDel(new PDFFontDelegate() {
            public String GetExtFont(String collection, String fname, int flag, int[] ret_flags) {
                A.log("-------->pdfFontDelegate:" + fname);
                if (A.pdf_font_hint) {
                    if (ActivityTxt.this.pdfMissedFonts == null) {
                        ActivityTxt.this.pdfMissedFonts = new ArrayList();
                    }
                    if (ActivityTxt.this.pdfMissedFonts.indexOf(fname) == -1) {
                        if (ActivityTxt.this.getSharedPreferences(A.lastFile.replace("/", "") + "_font", 0).contains(fname)) {
                            A.log("*****ERROR: pdf font already added to delete sp:" + fname);
                        } else {
                            ActivityTxt.this.pdfMissedFonts.add(fname);
                            ActivityTxt.this.handler.removeMessages(ActivityTxt.PDF_MISS_FONT);
                            ActivityTxt.this.handler.sendEmptyMessageDelayed(ActivityTxt.PDF_MISS_FONT, 1000);
                        }
                    }
                }
                return null;
            }
        });
    }

    private void pdfShowMissFontTip() {
        if (this.pdfMissedFonts != null && this.pdfMissedFonts.size() != 0) {
            SharedPreferences sp = getSharedPreferences(A.lastFile.replace("/", "") + "_font", 0);
            boolean restart = false;
            Iterator it = this.pdfMissedFonts.iterator();
            while (it.hasNext()) {
                String fname = (String) it.next();
                if (T.isFile(A.outerFontsFolder + "/" + fname + ".ttf")) {
                    restart = true;
                }
                sp.edit().putString(fname, "").commit();
            }
            if (restart) {
                saveLastPostion(true);
                restartReaderToTxt();
                return;
            }
            showCssMissedFontsTip();
        }
    }

    private boolean pdfAllowCheckWordCount() {
        if (this.pdfCheckWordCountState == 0) {
            int i;
            if (getSharedPreferences("pdf_words_failed", 0).contains(A.lastFile)) {
                i = 2;
            } else {
                i = 1;
            }
            this.pdfCheckWordCountState = i;
        }
        if (this.pdfCheckWordCountState == 1) {
            return true;
        }
        return false;
    }

    private boolean showRemaingTime() {
        if (A.showRemainingTime && isPdf() && (!pdfAllowCheckWordCount() || !pdfShowFullStatusBarInfo())) {
            return false;
        }
        return A.showRemainingTime;
    }

    private void pdfSetProperties(boolean updateView) {
        if (isPdf() && this.pdf != null) {
            int old_def_view = Global.def_view;
            setGlobalValue();
            if (updateView && old_def_view != Global.def_view) {
                pdfUpdateView(false);
            }
        }
    }

    private void pdfCheckPassword(final String filename) {
        View v = LayoutInflater.from(this).inflate(R.layout.pdf_password, null);
        final EditText uEdit = (EditText) v.findViewById(R.id.ueit);
        final CheckBox cb = (CheckBox) v.findViewById(R.id.saveCheck);
       new AlertDialog.Builder(this).setTitle((CharSequence) "Need input password").setView(v).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String up = uEdit.getText().toString();
                if (ActivityTxt.this.m_doc.Open(filename, up) == 0) {
                    ActivityTxt.this.getSharedPreferences("pdf", 0).edit().putString(filename + "u", A.getEncryptText(up)).putBoolean(filename + "s", cb.isChecked()).commit();
                    ActivityTxt.this.restartReaderToTxt();
                    return;
                }
                ActivityTxt.this.error_and_exit("Invalidated password.", false);
            }
        }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityTxt.this.doFinish();
            }
        }).setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                ActivityTxt.this.doFinish();
            }
        }).show();
    }

    private boolean pdfPageScroll(boolean pageDown) {
        if (!isPdf()) {
            return false;
        }
        this.linkBackIv.setVisibility(4);
        int off = (A.pdf_dual_page && A.isLandscape()) ? 2 : 1;
        int i;
        if (this.pdf.textReflow) {
            int y = this.txtScroll.getScrollY();
            int tvH = this.txtView.getRealHeight();
            int svH = A.getPageHeight();
            if ((pageDown || y != 0) && (!pageDown || (!lastLineDisplayed() && y + svH < tvH - ((this.txtView.getLineHeight() * 95) / 100)))) {
                txtScrollTo(this.txtView.getLineTop(getNextPageLine2(pageDown, false)));
            } else {
                i = (int) A.lastPosition;
                if (!pageDown) {
                    off = -off;
                }
                A.lastPosition = (long) pdfValidatePageNumber(i + off);
                this.yBeforePageDown.clear();
                clearTxtView();
                this.txtView.setText(pdfCreateReflowTextForShow(A.lastPosition));
                txtScrollByDelay(0);
            }
            if (A.isSpeaking) {
                this.handler.sendEmptyMessageDelayed(SPEAK_PDF_TEXT_REFLOW, 50);
            }
        } else {
            PDFPosition p = this.pdf.pdfView.viewGetPos();
            if (Global.def_view == 0) {
                pdfVerticalPageScroll(pageDown, p);
            } else {
                pdfResetPageXY(p);
                i = p.page;
                if (!pageDown) {
                    off = -off;
                }
                p.page = pdfValidatePageNumber(i + off);
                this.pdf.pdfView.viewGoto(p);
            }
            A.lastPosition = (long) p.page;
        }
        showReadProgress(0);
        if (!pageDown) {
            return true;
        }
        pdfSaveReadStatistics();
        return true;
    }

    private void pdfVerticalPageScroll(boolean pageDown, PDFPosition p) {
        float ph = pdfPageParaHeight(p.page);
        float h = (float) (this.pdf.getHeight() - A.d(10.0f));
        if (pageDown) {
            if (!A.isSpeaking && ((float) p.page_y) + h <= ph) {
                p.page_y += (int) h;
            } else if (p.page == this.m_doc.GetPageCount() - 1) {
                p.page_y = (int) (ph - h);
            } else {
                p.page = pdfValidatePageNumber(p.page + 1);
                if (A.isSpeaking) {
                    p.page_y = 0;
                } else {
                    p.page_y = (int) ((h - (ph - ((float) p.page_y))) - 4.0f);
                }
            }
        } else if (((float) p.page_y) >= h) {
            p.page_y -= (int) h;
        } else if (p.page == 0) {
            p.page_y = 0;
        } else {
            p.page--;
            p.page_y = (int) ((ph - (h - ((float) p.page_y))) + 4.0f);
        }
        this.pdf.pdfView.viewGoto(p);
        this.pdf.postInvalidate();
    }

    private void pdfResetPageXY(PDFPosition p) {
        if (!A.pdf_scoll_lock) {
            if (p.page_y == 0) {
                float ph = pdfPageParaHeight(p.page);
                int h = this.pdf.getHeight();
                if (((float) h) > ph) {
                    p.page_y = (-((int) ((((float) h) - ph) / 2.0f))) - 1;
                }
            }
            if (p.page_x == 0) {
                float pw = pdfPageParaWidth(p.page);
                int w = this.pdf.getWidth();
                if (((float) w) > pw) {
                    p.page_x = -((int) ((((float) w) - pw) / 2.0f));
                }
            }
        }
    }

    private void pdfSaveReadStatistics() {
        try {
            if (this.pdfStatisticsPages == null) {
                this.pdfStatisticsPages = new ArrayList();
            }
            if (this.pdf.turnedPages.indexOf(Integer.valueOf((int) A.lastPosition)) == -1) {
                this.pdf.turnedPages.add(Integer.valueOf((int) A.lastPosition));
            }
            Iterator it = this.pdf.turnedPages.iterator();
            while (it.hasNext()) {
                int i = ((Integer) it.next()).intValue();
                if (this.pdfStatisticsPages.indexOf(Integer.valueOf(i)) == -1) {
                    this.pdfStatisticsPages.add(Integer.valueOf(i));
                    A.statistics_pages++;
                    String text = pdfGetPageText(i);
                    if (text.length() > 0) {
                        pdfSaveReadStatistics2(text);
                    }
                }
            }
            this.pdf.turnedPages.clear();
        } catch (Exception e) {
            A.error(e);
        }
    }

    public String pdfGetPageText(int i) {
        try {
            Page page = this.m_doc.GetPage(i);
            page.ObjsStart();
            String text = page.ObjsGetString(0, page.ObjsGetCharCount());
            if (text == null) {
                return "";
            }
            return text;
        } catch (Exception e) {
            A.error(e);
            return "";
        }
    }

    private void pdfSaveReadStatistics2(String text) {
        int wordsCount = T.getWordsCount(text, A.isAsiaLanguage);
        A.statistics_words += (long) wordsCount;
        this.read_words += wordsCount;
    }

    private void pdfGetToc() {
        if (this.pdfToc == null) {
            this.pdfToc = new ArrayList();
            SharedPreferences sp = getSharedPreferences("pdf_toc_failed", 0);
            if (!sp.contains(A.lastFile)) {
                sp.edit().putBoolean(A.lastFile, true).commit();
                pdfAddTocTree(this.m_doc.GetOutlines(), 0);
                sp.edit().remove(A.lastFile).commit();
                if (this.pdfTreeTOC) {
                    for (int j = 0; j < this.pdfToc.size(); j++) {
                        TocChapter tocChapter = (TocChapter) this.pdfToc.get(j);
                        tocChapter.indent++;
                    }
                }
            } else {
                return;
            }
        }
        A.lastPosition = (long) pdfGetCurrPageNo();
        int i = 0;
        while (i < this.pdfToc.size() && A.lastPosition >= ((long) ((TocChapter) this.pdfToc.get(i)).pageNumber)) {
            A.lastChapter = i;
            if (A.lastPosition != ((long) ((TocChapter) this.pdfToc.get(i)).pageNumber)) {
                i++;
            } else {
                return;
            }
        }
    }

    private void pdfAddTocTree(Outline o, int indent) {
        while (o != null) {
            Outline child = o.GetChild();
            this.pdfToc.add(new TocChapter(o.GetTitle(), this.pdfToc.size(), indent, child != null, true, o.GetDest()));
            if (child != null) {
                this.pdfTreeTOC = true;
                pdfAddTocTree(child, indent + 1);
            }
            o = o.GetNext();
        }
    }

    void pdfZoomRatio(float add) {
        if (this.pdf != null && this.pdf.pdfView != null) {
            float ratio = this.pdf.pdfView.viewGetRatio() + add;
            PDFPosition p = this.pdf.pdfView.viewGetPos();
            this.pdf.pdfView.viewSetRatio(ratio, (float) p.page_x, (float) p.page_y, false);
        }
    }

    private void pdfSelectText(boolean showSelector) {
        if (this.hMotionEvent != null) {
            int x = (int) this.hMotionEvent.getX();
            int y = (int) this.hMotionEvent.getY();
            int dw = this.dot1.getWidth();
            int dh = this.dot1.getHeight();
            this.dot1.layout(x - (dw / 2), y, (x - (dw / 2)) + dw, y + dh);
            this.dot2.layout(x - (dw / 2), y, (x - (dw / 2)) + dw, y + dh);
            this.hHighlight.setTag(Integer.valueOf(0));
            if (showSelector) {
                showDotViews();
                this.pdf.recordDotsAct(this);
            }
            pdfHighlightText(true, showSelector);
        }
    }

    private void pdfHighlightText(boolean showHBar, boolean showHighlight) {
        int off = A.d(5.0f);
        int dw = this.dot1.getWidth();
        int x1 = this.dot1.getLeft() + (dw / 2);
        int y1 = this.dot1.getTop() - off;
        int x2 = this.dot2.getLeft() + (dw / 2);
        int y2 = this.dot2.getTop() - off;
        if (showHighlight) {
            if (x1 != x2) {
                A.pdfHighlightFromDot = true;
            }
            pdfHighlight(x1, y1, x2, y2);
        }
        if (showHBar) {
            layoutHBar();
        } else {
            setHBarVisible(false);
        }
    }

    private void pdfHighlight(int x1, int y1, int x2, int y2) {
        try {
            if (Global.def_view == 3) {
                PDFPosition p = this.pdf.pdfView.viewGetPos();
                x1 += p.page_x;
                x2 += p.page_x;
            }
            this.pdf.pdfView.viewSetSel((float) x1, (float) y1, (float) x2, (float) y2);
        } catch (Exception e) {
            A.error(e);
        }
    }

    public void pdfAfterOnTouch(MotionEvent event) {
    }

    public void pdfShowFullStatusBarInfoHandler() {
        if (showStatusbar()) {
            new Handler() {
                public void handleMessage(Message msg) {
                    ActivityTxt.this.pdfShowFullStatusBarInfo();
                }
            }.sendEmptyMessage(0);
        }
    }

    private void pdfUpdateStatusBarColor(boolean full) {
        int visible = 0;
        if (this.pdfFullStatusBar == null || this.pdfFullStatusBar.booleanValue() != full) {
            A.log("*pdfCheckStatusBar--------");
            this.pdfFullStatusBar = Boolean.valueOf(full);
            if (full) {
                this.statusRight.setBackgroundDrawable(null);
                this.statusRight.setPadding(0, 0, A.d(4.0f), 0);
                setStatusBarProperties(true);
                updateBarTime();
            } else {
                this.statusRight.setBackgroundColor(-1442840576);
                this.statusRight.setTextColor(-1);
                this.statusRight.setPadding(A.d(4.0f), 0, A.d(4.0f), 0);
            }
            if (!full) {
                visible = 4;
            }
            this.remainingRight.setVisibility(visible);
            this.statusLeft.setVisibility(visible);
            this.statusLeft2.setVisibility(visible);
            this.statusMiddle.setVisibility(visible);
        }
    }

    public boolean pdfShowFullStatusBarInfo() {
        boolean full = true;
        if (!this.pdf.textReflow && (Global.def_view == 0 || ((float) A.getScreenHeight2()) - pdfPageParaHeight(-1) <= ((float) (this.statusLay.getHeight() * 2)))) {
            full = false;
        }
        if (showStatusbar()) {
            pdfUpdateStatusBarColor(full);
        }
        return full;
    }

    private float pdfPageParaWidth(int pageno) {
        if (pageno == -1) {
            pageno = pdfGetCurrPageNo();
        }
        return this.m_doc.GetPageWidth(pageno) * this.pdf.getParaRatio();
    }

    private float pdfPageParaHeight(int pageno) {
        if (pageno == -1) {
            pageno = pdfGetCurrPageNo();
        }
        return this.m_doc.GetPageHeight(pageno) * this.pdf.getParaRatio();
    }

    private int pdfVerticalParaMargin() {
        int h = this.baseFrame.getHeight();
        float ph = pdfPageParaHeight(-1);
        return ph >= ((float) h) ? 0 : ((int) (((float) h) - ph)) / 2;
    }

    public void pdfUpdateView(boolean updateRatio) {
        setGlobalValue();
        float ratio = this.pdf.pdfView.viewGetRatio();
        PDFPosition p = this.pdf.pdfView.viewGetPos();
        this.pdf.set_viewer(Global.def_view);
        if (updateRatio) {
            pdfResetRatio(p, ratio);
        }
    }

    private void setGlobalValue() {
        int i = 0;
        int i2 = A.pdf_render_quality == 0 ? 2 : A.pdf_render_quality == 1 ? 1 : 0;
        Global.render_mode = i2;
        if (A.pdf_dual_page && A.isLandscape()) {
            Global.def_view = 4;
            return;
        }
        if (A.pdf_view_mode == 0) {
            if (!A.isLandscape()) {
                i = 3;
            }
        } else if (A.pdf_view_mode == 1) {
            i = 3;
        }
        Global.def_view = i;
    }

    private void pdfSaveLastPosition() {
        try {
            float ratio = this.pdf.pdfView.viewGetRatio();
            PDFPosition p = this.pdf.pdfView.viewGetPos();
            if (!this.pdf.textReflow) {
                A.lastPosition = (long) p.page;
            }
            getSharedPreferences(A.POSITION_FILE, 0).edit().putString(A.lastFile.toLowerCase(), "" + A.lastPosition + ":" + getPercentStr2()).putString(A.lastFile.toLowerCase() + "pdf", "" + ratio + "#" + p.page_x + ":" + p.page_y + "@" + A.pdf_scoll_lock).commit();
        } catch (Exception e) {
            A.error(e);
        }
    }

    private void pdfLoadLastPosition(int delay) {
        new Handler() {
            public void handleMessage(Message msg) {
                boolean z = true;
                String s = ActivityTxt.this.getSharedPreferences(A.POSITION_FILE, 0).getString(A.lastFile.toLowerCase() + "pdf", "");
                if (s.length() > 0) {
                    try {
                        ActivityTxt.this.ignoreChapterListAtBegin = true;
                        PDFPosition p = ActivityTxt.this.pdf.pdfView.viewGetPos();
                        if (((long) p.page) != A.lastPosition) {
                            A.log("*********incorrect pdf load page, why:" + A.lastPosition + " - " + p.page);
                            p.page = (int) A.lastPosition;
                        }
                        float ratio = Float.valueOf(s.substring(0, s.indexOf("#"))).floatValue();
                        p.page_x = Integer.valueOf(s.substring(s.indexOf("#") + 1, s.indexOf(":"))).intValue();
                        p.page_y = Integer.valueOf(s.substring(s.indexOf(":") + 1, s.indexOf("@"))).intValue();
                        ActivityTxt.this.pdfResetRatio(p, ratio);
                        if (A.pdf_scoll_lock) {
                            if (s.indexOf("true") <= 0) {
                                z = false;
                            }
                            A.pdf_scoll_lock = z;
                        }
                        if (A.pdf_scoll_lock) {
                            ActivityTxt.this.pdf.pdfView.viewLockSide(A.pdf_scoll_lock, ActivityTxt.this.pdf.getContext());
                            return;
                        }
                        return;
                    } catch (Exception e) {
                        A.error(e);
                        return;
                    }
                }
                A.pdf_scoll_lock = false;
            }
        }.sendEmptyMessageDelayed(0, (long) delay);
    }

    private void pdfResetRatio(PDFPosition p, float ratio) {
        pdfResetPageXY(p);
        this.pdf.pdfView.viewSetRatio(ratio, (float) p.page_x, (float) p.page_y, false);
        this.pdf.pdfView.viewGoto(p);
    }

    private void pdfTextReflowSwitch(boolean reverse) {
        int i;
        if (reverse) {
            this.pdf.textReflow = !this.pdf.textReflow;
        }
        ImageView imageView = this.pdfLockIv;
        if (this.pdf.textReflow) {
            i = 8;
        } else {
            i = 0;
        }
        imageView.setVisibility(i);
        this.chromeIv.setImageResource(this.pdf.textReflow ? R.drawable.mrbookview2 : R.drawable.text_reflow);
        inverseLayoutVisible(true);
        if (this.pdf.textReflow) {
            this.pdf.setVisibility(8);
            A.loadVisualOptions(false);
            this.txtLay.setVisibility(0);
            this.txtView.setText(pdfCreateReflowTextForShow(A.lastPosition));
            txtScrollByDelay(0);
        } else {
            this.pdf.setVisibility(0);
            this.txtLay.setVisibility(8);
            this.pdf.pdfView.viewGotoPage((int) A.lastPosition);
        }
        checkStatusBar();
    }

    private String pdfCreateReflowTextForShow(long i) {
        String text = A.formatPdfReflowText(pdfGetPageText((int) i), this.m_doc.GetPage((int) i));
        if (A.isSpeaking) {
            return text;
        }
        return text + " <" + (1 + i) + "/" + this.m_doc.GetPageCount() + ">";
    }

    protected int pdfValidatePageNumber(int progress) {
        if (progress >= this.m_doc.GetPageCount()) {
            progress = this.m_doc.GetPageCount() - 1;
        }
        if (progress < 0) {
            return 0;
        }
        return progress;
    }

    protected void pdfGotoPage(int i, boolean sameRatio) {
        try {
            i = pdfValidatePageNumber(i);
            if (this.pdf.textReflow) {
                this.txtView.setText(pdfCreateReflowTextForShow((long) i));
            } else if (sameRatio) {
                PDFPosition p = this.pdf.pdfView.viewGetPos();
                pdfResetPageXY(p);
                p.page = i;
                this.pdf.pdfView.viewGoto(p);
            } else {
                this.pdf.pdfView.viewGotoPage(i);
            }
            A.lastPosition = (long) i;
        } catch (Exception e) {
            A.error(e);
        }
    }

    public int pdfGetCurrPageNo() {
        if (this.pdf == null || this.pdf.textReflow || this.pdf.pdfView == null) {
            this.pdfCurPageNo = (int) A.lastPosition;
        } else {
            this.pdfCurPageNo = this.pdf.pdfView.viewGetCurPageNo();
        }
        if (this.pdfCurPageNo == -1) {
            return (int) A.lastPosition;
        }
        return this.pdfCurPageNo;
    }

    private void pdfCheckBookcover() {
        String destFile = BaseCompressor.getZRCacheFile(A.lastFile);
        if (!T.isFile(A.download_cache_path + "/" + T.getFilename(destFile) + A.THUMB_TAG)) {
            long t = System.currentTimeMillis();
            A.generateBookCovers(Global.createBitmapOfPage(this.m_doc, 0, null), destFile, false);
            A.log("extract cover time:" + (System.currentTimeMillis() - t));
        }
    }

    private void pdfDoAnnotation() {
        boolean z = true;
        if ((this.pdfAnnotStart || this.pdf.selectedText != null) && pdfCanSave()) {
            pdfSetImmersiveMode(true);
            this.pdf.lockResize(true);
            if (!this.pdfAnnotStart || T.isNull(this.pdf.annotGetText())) {
                z = false;
            }
            new PrefEditNote(this, null, z, new OnAfterEditNote() {
                public void AfterEditNote(int result, String text) {
                    if (text.length() == 0 || !ActivityTxt.this.m_doc.CanSave()) {
                        result = 0;
                    }
                    switch (result) {
                        case 0:
                            ActivityTxt.this.pdf.annotEnd();
                            break;
                        case 1:
                            try {
                                if (ActivityTxt.this.pdfAnnotStart) {
                                    if (!ActivityTxt.this.pdf.annotGetText().equals(text)) {
                                        ActivityTxt.this.pdf.annotSetText(text);
                                    }
                                } else if (ActivityTxt.this.pdf.pdfView.annotSetMarkup(0)) {
                                    Page page = ActivityTxt.this.m_doc.GetPage(ActivityTxt.this.pdfGetCurrPageNo());
                                    page.ObjsStart();
                                    A.log("*page:" + page + " annotCount:" + page.GetAnnotCount());
                                    if (page.GetAnnotCount() > 0) {
                                        page.GetAnnot(page.GetAnnotCount() - 1).SetPopupText(text);
                                        ActivityTxt.this.pdfSaveHighlightNote((int) A.lastPosition, ActivityTxt.this.pdf.selectedText, text, 0);
                                    } else {
                                        A.log("#Error pdf note# GetAnnotCount()==0");
                                    }
                                } else {
                                    ActivityTxt.this.pdfShowFailedAnnotInfo("Failed to add annotation!");
                                    return;
                                }
                                ActivityTxt.this.pdf.annotEnd();
                                A.pdfAnnotUpdated = true;
                                break;
                            } catch (Exception e) {
                                A.error(e);
                                break;
                            }
                        case 2:
                            ActivityTxt.this.pdf.annotRemove();
                            A.pdfAnnotUpdated = true;
                            ActivityTxt.this.hideDotViews();
                            break;
                    }
                    ActivityTxt.this.pdf.lockResize(false);
                    ActivityTxt.this.hideDotViews();
                    ActivityTxt.this.pdfSaveAnnotsForTime();
                    ActivityTxt.this.pdfSetImmersiveMode(false);
                }
            }).show();
        }
    }

    protected void pdfShowFailedAnnotInfo(String text) {
        if (A.isKitkatExtSdcardFile(A.lastFile)) {
           new AlertDialog.Builder(this).setMessage(Html.fromHtml("<b>" + text + "</b><br><br>" + getString(R.string.copy_pdf_file_to_edit, new Object[]{"<b><font color=\"#FF0000\">/sdcard/Books/Pdf</font></b>"}))).setCancelable(false).setPositiveButton(17039379, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String dest = "/sdcard/Books/Pdf/" + T.getFilename(A.lastFile);
                    File destFile = new File(dest);
                    if (!(destFile.isFile() && destFile.length() == T.getFileSize(A.lastFile))) {
                        T.copyFile(A.lastFile, dest, true);
                    }
                    if (T.isFile(dest)) {
                        ActivityTxt.this.saveLastPostion(true);
                        A.deleteHistory(A.lastFile);
                        A.lastFile = dest;
                        A.lastPath = T.getFilePath(dest);
                        ActivityTxt.this.saveLastPostion(true);
                        ActivityTxt.this.restartReaderToTxt();
                    }
                }
            }).setNegativeButton(17039369, null).show();
            return;
        }
        T.showToastText(this, text);
    }

    private boolean pdfCanSave() {
        if (this.m_doc.CanSave()) {
            return true;
        }
        pdfShowFailedAnnotInfo(getString(R.string.pdf_cannot_modify));
        return false;
    }

    private void pdfDoHighlight(boolean fromLongTap) {
        if ((isClickOnAnnot() || this.pdf.selectedText != null) && pdfCanSave()) {
            pdfSetImmersiveMode(true);
            this.pdf.lockResize(true);
            if (isClickOnAnnot() || !fromLongTap) {
                int mode = -1;
                if (isClickOnAnnot()) {
                    Annotation annot = this.pdf.pdfView.getSelectedAnnot();
                    if (annot != null) {
                        if (annot.GetType() == 9) {
                            mode = 0;
                        } else if (annot.GetType() == 10) {
                            mode = 1;
                        } else if (annot.GetType() == 12) {
                            mode = 2;
                        } else if (annot.GetType() == 11) {
                            mode = 3;
                        }
                    }
                }
                PrefSelectHighlight h = new PrefSelectHighlight(this, new OnSelectColor() {
                    public void selectColor(int mode, boolean delete) {
                        if (delete) {
                            ActivityTxt.this.pdf.annotRemove();
                            A.pdfAnnotUpdated = true;
                            ActivityTxt.this.hideDotViews();
                            ActivityTxt.this.pdfSaveAnnotsForTime();
                            return;
                        }
                        ActivityTxt.this.pdfDoHighlight2(-1);
                    }
                }, true, mode, null);
                h.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        ActivityTxt.this.pdf.annotEnd();
                        ActivityTxt.this.pdf.lockResize(false);
                        ActivityTxt.this.pdfSetImmersiveMode(false);
                    }
                });
                h.show();
                return;
            }
            pdfDoHighlight2(-1);
            this.pdf.annotEnd();
            this.pdf.lockResize(false);
        }
    }

    private boolean pdfDoHighlight2(int forceColor) {
        boolean result = false;
        try {
            if (isClickOnAnnot()) {
                Annotation annot = this.pdf.pdfView.getSelectedAnnot();
                if (annot != null) {
                    if (annot.GetType() == 9) {
                        if (forceColor == -1) {
                            forceColor = A.pdf_highlight_color;
                        }
                        annot.SetFillColor(Page.fixHighlightColor(forceColor));
                    } else if (annot.GetType() == 10) {
                        if (forceColor == -1) {
                            forceColor = A.underline_color;
                        }
                        annot.SetStrokeColor(forceColor);
                    } else if (annot.GetType() == 12) {
                        if (forceColor == -1) {
                            forceColor = A.strikethrough_color;
                        }
                        annot.SetStrokeColor(forceColor);
                    } else if (annot.GetType() == 11) {
                        if (forceColor == -1) {
                            forceColor = A.squiggly_color;
                        }
                        annot.SetStrokeColor(forceColor);
                    }
                    this.pdf.pdfView.refreshCurPage();
                    result = true;
                }
            } else {
                boolean ret = false;
                int savedColor = -1;
                if (forceColor != -1) {
                    if (A.highlightMode == 0) {
                        savedColor = A.pdf_highlight_color;
                        A.pdf_highlight_color = forceColor;
                    } else if (A.highlightMode == 1) {
                        savedColor = A.underline_color;
                        A.underline_color = forceColor;
                    } else if (A.highlightMode == 2) {
                        savedColor = A.strikethrough_color;
                        A.strikethrough_color = forceColor;
                    } else if (A.highlightMode == 3) {
                        savedColor = A.squiggly_color;
                        A.squiggly_color = forceColor;
                    }
                }
                if (A.highlightMode == 0) {
                    ret = this.pdf.pdfView.annotSetMarkup(0);
                } else if (A.highlightMode == 1) {
                    ret = this.pdf.pdfView.annotSetMarkup(1);
                } else if (A.highlightMode == 2) {
                    ret = this.pdf.pdfView.annotSetMarkup(2);
                } else if (A.highlightMode == 3) {
                    ret = this.pdf.pdfView.annotSetMarkup(4);
                }
                if (!ret) {
                    pdfShowFailedAnnotInfo("Failed to add highlight!");
                } else if (pdfCanSave()) {
                    pdfSaveHighlightNote((int) A.lastPosition, this.pdf.selectedText, "", A.highlightMode);
                    A.pdfAnnotUpdated = true;
                    result = true;
                }
                if (forceColor != -1) {
                    if (A.highlightMode == 0) {
                        A.pdf_highlight_color = savedColor;
                    } else if (A.highlightMode == 1) {
                        A.underline_color = savedColor;
                    } else if (A.highlightMode == 2) {
                        A.strikethrough_color = savedColor;
                    } else if (A.highlightMode == 3) {
                        A.squiggly_color = savedColor;
                    }
                }
            }
            pdfSaveAnnotsForTime();
        } catch (Exception e) {
            A.error(e);
        }
        hideDotViews();
        return result;
    }

    private void pdfSetImmersiveMode(boolean immersive) {
        if (A.immersive_fullscreen && this.pdf != null) {
            this.pdf.forbid_immersive_mode = immersive;
        }
    }

    protected void pdfSaveHighlightNote(int pageno, String original, String noteText, int type) {
        String bookName = A.getBookName();
        String str = A.lastFile;
        int i = Page.note_start;
        int i2 = Page.note_end;
        long j = (long) pageno;
        int i3 = Page.note_end - Page.note_start;
        int i4 = type == 0 ? A.pdf_highlight_color : type == 1 ? A.underline_color : type == 2 ? A.strikethrough_color : A.squiggly_color;
        A.addNote(new NoteInfo(0, bookName, str, i, i2, j, i3, i4, System.currentTimeMillis(), "", noteText, original, type == 1, type == 2, type == 3 ? "1" : ""));
    }

    private PDFAnnotListener pdfOnAnnoListener() {
        return new PDFAnnotListener() {
            public void onAnnotUpdate() {
                Log.i("MR2", "--annot update");
                ActivityTxt.this.hideDotViews();
            }

            public void onAnnotEnd() {
                Log.i("MR2", "--annot end");
                ActivityTxt.this.annotEndTime = SystemClock.elapsedRealtime();
                ActivityTxt.this.pdfAnnotStart = false;
                ActivityTxt.this.antSelected = null;
                A.pdfStatus = STATUS.sta_none;
                if (ActivityTxt.this.pdfTopLay.getVisibility() == 0) {
                    ActivityTxt.this.pdfShowAnnotLay();
                }
                ActivityTxt.this.pdfHideAnnotLay(false, true);
                ActivityTxt.this.hideDotViews();
            }

            public void onAnnotDragStart(boolean has_goto, boolean has_popup) {
                boolean ok;
                int i = 8;
                ActivityTxt.this.pdfAnnotStart = true;
                ActivityTxt.this.inverseLayoutVisible(true);
                int type = ActivityTxt.this.pdf.pdfView.getSelectedAnnot().GetType();
                if (type == 1 || type == 3 || type == 4 || type == 5 || type == 6 || type == 15) {
                    ok = true;
                } else {
                    ok = false;
                }
                if (ok) {
                    int i2;
                    ActivityTxt.this.apNote.setVisibility(0);
                    ImageView imageView = ActivityTxt.this.apThickness;
                    if (type == 1 || type == 3) {
                        i2 = 8;
                    } else {
                        i2 = 0;
                    }
                    imageView.setVisibility(i2);
                    imageView = ActivityTxt.this.apColor;
                    if (type == 1 || type == 3) {
                        i2 = 8;
                    } else {
                        i2 = 0;
                    }
                    imageView.setVisibility(i2);
                    ImageView imageView2 = ActivityTxt.this.apFillColor;
                    if (type == 5 || type == 6) {
                        i = 0;
                    }
                    imageView2.setVisibility(i);
                    ActivityTxt.this.apCancel.setImageResource(R.drawable.trash);
                    ActivityTxt.this.pdfBottomLay.setVisibility(0);
                    if (ActivityTxt.this.pdfTopLay.getVisibility() == 0) {
                        ActivityTxt.this.antSelected = null;
                        ActivityTxt.this.pdfSetAnnotButtonBackground();
                        return;
                    }
                    return;
                }
                Log.i("MR2", "--annot drag start");
                if (T.isNull(ActivityTxt.this.pdf.annotGetText())) {
                    ActivityTxt.this.pdfSelectText(false);
                    ActivityTxt.this.hPen.setTag(Integer.valueOf(1));
                    ActivityTxt.this.setHPenImage(true);
                    ActivityTxt.this.hCopy.setTextColor(-2002081110);
                    ActivityTxt.this.hDict.setTextColor(-2002081110);
                    ActivityTxt.this.hMore.setTextColor(-2002081110);
                    return;
                }
                ActivityTxt.this.pdfDoAnnotation();
            }

            public void onAnnotEditBox(int type, String val, float text_size, float left, float top, float right, float bottom) {
                ActivityTxt.this.pdfSaveAnnotsForSecurity();
                if (PDFReader.isFreeTextAnnot(ActivityTxt.this.pdf.pdfView.getSelectedAnnot())) {
                    onAnnotDragStart(false, false);
                    return;
                }
                ActivityTxt.this.pdfAnnotStart = true;
                ActivityTxt.this.pdf.lockResize(true);
                final EditText et = new EditText(ActivityTxt.this);
                et.setText(val);
               new AlertDialog.Builder(ActivityTxt.this).setView(et).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityTxt.this.pdf.annotSetEditText(et.getText().toString());
                        A.pdfAnnotUpdated = true;
                        ActivityTxt.this.pdf.lockResize(false);
                        ActivityTxt.this.pdfAnnotStart = false;
                    }
                }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityTxt.this.pdf.lockResize(false);
                        ActivityTxt.this.pdfAnnotStart = false;
                    }
                }).setOnCancelListener(new OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        ActivityTxt.this.pdf.lockResize(false);
                        ActivityTxt.this.pdfAnnotStart = false;
                    }
                }).show();
            }

            public void onAnnotComboBox(int sel, String[] opts, float arg2, float arg3, float arg4, float arg5) {
                ActivityTxt.this.pdfAnnotStart = true;
                ActivityTxt.this.pdf.lockResize(true);
                ActivityTxt.this.pdf_sel_index = sel;
                new AlertDialog.Builder(ActivityTxt.this).setSingleChoiceItems(opts, sel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityTxt.this.pdf_sel_index = which;
                    }
                }).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityTxt.this.pdf.annotSetChoice(ActivityTxt.this.pdf_sel_index);
                        A.pdfAnnotUpdated = true;
                        ActivityTxt.this.pdf.lockResize(false);
                        ActivityTxt.this.pdfAnnotStart = false;
                    }
                }).setNegativeButton(17039360, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityTxt.this.pdf.lockResize(false);
                        ActivityTxt.this.pdfAnnotStart = false;
                    }
                }).setOnCancelListener(new OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        ActivityTxt.this.pdf.lockResize(false);
                        ActivityTxt.this.pdfAnnotStart = false;
                    }
                }).show();
            }
        };
    }

    private void pdfSaveAnnotsForTime() {
        if (A.pdfAnnotUpdated && this.pdf != null && this.m_doc != null) {
            if (this.pdfLastSaveTime == 0) {
                this.pdfLastSaveTime = SystemClock.elapsedRealtime();
            }
            if (SystemClock.elapsedRealtime() - this.pdfLastSaveTime >= 300000) {
                A.log("****SAVE PDF DOCUMENT****2");
                A.pdfAnnotUpdated = false;
                this.m_doc.Save();
                this.pdfLastSaveTime = SystemClock.elapsedRealtime();
            }
        }
    }

    private void pdfSaveAnnotsForSecurity() {
        if (A.pdfAnnotUpdated && this.pdf != null && this.m_doc != null && !Build.CPU_ABI.equals("armeabi-v7a")) {
            A.log("****SAVE PDF DOCUMENT****3");
            A.pdfAnnotUpdated = false;
            this.m_doc.Save();
            this.pdfLastSaveTime = SystemClock.elapsedRealtime();
        }
    }

    private ArrayList<TtsLine> pdfGetTtsLines() {
        if (this.pdf.textReflow) {
            pdfTextReflowSwitch(true);
        }
        String text = this.pdf.pdfView.viewGetCurPageText();
        if (T.isNull(text)) {
            return null;
        }
        try {
            Page page = this.m_doc.GetPage(pdfGetCurrPageNo());
            page.ObjsStart();
            int index = 0;
            while (true) {
                int r = text.indexOf("\r", index);
                if (r != -1) {
                    float[] v1 = new float[4];
                    page.ObjsGetCharRect(r - 1, v1);
                    if (text.length() <= r + 2) {
                        break;
                    }
                    float[] v2 = new float[4];
                    page.ObjsGetCharRect(r + 2, v2);
                    if (Math.abs((v1[3] - v1[1]) - (v2[3] - v2[1])) > 1.0f) {
                        text = text.substring(0, r) + "✔" + text.substring(r + 1);
                    }
                    index = r + 2;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            A.error(e);
        }
        ArrayList<TtsLine> ttsLines = A.getTtsLines(text, true, false, 0);
        if (ttsLines.size() == 0) {
            return null;
        }
        TtsLine sl = (TtsLine) ttsLines.get(0);
        this.pdf.selectedPara = null;
        PDFPage.setForceSel(sl.start, sl.end);
        this.pdf.postInvalidate();
        getSpeakHandler().sendEmptyMessageDelayed(4, 100);
        return ttsLines;
    }

    private void preNextChapter(boolean checkY) {
        if (!this.preNextInProgress) {
            if (!checkY || this.txtScroll.getScrollY() >= this.txtView.getRealHeight() / 3) {
                int chapterId = A.lastChapter;
                int splitIndex = A.lastSplitIndex;
                if (A.noSplitHtmls() || A.lastSplitIndex >= A.splitHtmls.size() - 1) {
                    splitIndex = 0;
                    chapterId++;
                    if (getBookType() == 1) {
                        return;
                    }
                    if (isPdf()) {
                        return;
                    }
                }
                splitIndex++;
                if (A.ebook != null && chapterId > A.ebook.getChapters().size() - 1) {
                    return;
                }
                if (this.preNextChapterText == null || this.preNextChapterId != chapterId || this.preNextChapterSplit != splitIndex) {
                    this.preNextChapterId = chapterId;
                    this.preNextChapterSplit = splitIndex;
                    this.preNextChapterText = null;
                    Thread thread = new Thread() {
                        public void run() {
                            ActivityTxt.this.preNextInProgress = true;
                            try {
                                if (!ActivityTxt.this.isFinishing()) {
                                    if (ActivityTxt.this.preNextChapterSplit > 0) {
                                        ActivityTxt.this.imageGetter = A.ebook == null ? ActivityTxt.this.createHtmlBookImageGetter() : A.ebook.getMyImageGetter();
                                        ActivityTxt.this.preNextHtmlSrc = A.chineseJianFanConvert((String) A.splitHtmls.get(ActivityTxt.this.preNextChapterSplit));
                                        ActivityTxt.this.preNextChapterText = MyHtml.fromHtml(ActivityTxt.this.preNextHtmlSrc, ActivityTxt.this.imageGetter, ActivityTxt.this.preNextChapterId);
                                        ActivityTxt.this.preNextSplitHtmls = A.splitHtmls;
                                    } else {
                                        ActivityTxt.this.generateChapterText(1);
                                    }
                                    ActivityTxt.this.preNextInProgress = false;
                                }
                            } catch (Exception e) {
                                A.error(e);
                            } finally {
                                ActivityTxt.this.preNextInProgress = false;
                            }
                        }
                    };
                    thread.setPriority(1);
                    thread.start();
                }
            }
        }
    }

    private boolean hasNextChapterText(boolean toChapterBegin, String progressTitle) {
        if (this.preNextInProgress || this.preNextChapterText == null || this.preNextChapterId != A.lastChapter || this.preNextChapterSplit != A.lastSplitIndex) {
            return false;
        }
        goNextCacheChapter(toChapterBegin);
        return true;
    }

    private void goNextCacheChapter(boolean toChapterBegin) {
        disableTxtViewDraw();
        clearTxtView();
        A.lastChapter = this.preNextChapterId;
        A.lastSplitIndex = this.preNextChapterSplit;
        A.splitHtmls = this.preNextSplitHtmls;
        this.htmlSrc = this.preNextHtmlSrc;
        txtViewSetText(this.preNextChapterText);
        if (toChapterBegin) {
            A.lastPosition = 0;
        }
        pausePixelAutoScroll();
        this.ebook_inLoading = true;
        this.forceDelayFlipAnimation = true;
        txtScrollToLastPostion();
        new Handler() {
            public void handleMessage(Message msg) {
                ActivityTxt.this.txtScrollToLastPostion();
                ActivityTxt.this.enableTxtViewDraw();
                ActivityTxt.this.ebook_inLoading = false;
                ActivityTxt.this.forceDelayFlipAnimation = false;
                ActivityTxt.this.updateProgressStatus();
                ActivityTxt.this.continueSpeakOrAutoScroll();
                ActivityTxt.this.handler.sendEmptyMessageDelayed(ActivityTxt.PRE_LOAD_NEXT_CHAPTER, 100);
            }
        }.sendEmptyMessageDelayed(0, 0);
    }

    private void txtScrollToLastPostion() {
        if (this.txtView.getLayout() != null) {
            txtScrollTo(this.txtView.getLineTop2(this.txtView.getLayout().getLineForOffset((int) A.lastPosition)));
        }
    }

    private void prePriorChapterForPageUp() {
        if (!(A.ebook == null && A.noSplitHtmls()) && !isBeginOfBook() && this.web == null && this.txtScroll.getScrollY() == 0) {
            prePriorChapter();
        }
    }

    private void prePriorChapter() {
        int chapterId = A.lastChapter;
        int splitIndex = A.lastSplitIndex;
        if (A.noSplitHtmls() || A.lastSplitIndex <= 0) {
            splitIndex = -1;
            chapterId--;
            if (getBookType() == 1) {
                return;
            }
            if (isPdf()) {
                return;
            }
        }
        splitIndex--;
        if (A.ebook != null && chapterId < 0) {
            return;
        }
        if (this.oldPriorChapterText == null || this.oldPriorChapterId != chapterId || (this.oldPriorChapterSplit != splitIndex && splitIndex != -1)) {
            this.oldPriorChapterId = chapterId;
            this.oldPriorChapterSplit = splitIndex;
            this.oldPriorChapterText = null;
            if (this.oldPriorChapterSplit != -1) {
                this.imageGetter = A.ebook == null ? createHtmlBookImageGetter() : A.ebook.getMyImageGetter();
                this.oldPriorHtmlSrc = A.chineseJianFanConvert((String) A.splitHtmls.get(this.oldPriorChapterSplit));
                this.oldPriorChapterText = MyHtml.fromHtml(this.oldPriorHtmlSrc, this.imageGetter, this.oldPriorChapterId);
                this.oldPriorSplitHtmls = A.splitHtmls;
            } else {
                generateChapterText(-1);
            }
            this.oldPriorChapterY = -1;
        }
    }

    private boolean hasPriorChapterText(String progressTitle) {
        if (this.oldPriorChapterText == null || this.oldPriorChapterId != A.lastChapter) {
            return false;
        }
        if (this.oldPriorChapterSplit != A.lastSplitIndex && A.lastSplitIndex != -1) {
            return false;
        }
        if (A.lastSplitIndex == -1) {
            A.lastSplitIndex = this.oldPriorChapterSplit;
        }
        if (A.lastSplitIndex == -1) {
            A.lastSplitIndex = 0;
        }
        goPriorCacheChapter();
        return true;
    }

    private void goPriorCacheChapter() {
        clearTxtView();
        A.splitHtmls = this.oldPriorSplitHtmls;
        this.htmlSrc = this.oldPriorHtmlSrc;
        txtViewSetText(this.oldPriorChapterText);
        disableTxtViewDraw();
        this.forceDelayFlipAnimation = true;
        new Handler() {
            public void handleMessage(Message msg) {
                if (ActivityTxt.this.oldPriorChapterY != -1) {
                    ActivityTxt.this.enableTxtViewDraw();
                    ActivityTxt.this.txtScrollTo(ActivityTxt.this.oldPriorChapterY);
                    ActivityTxt.this.forceDelayFlipAnimation = false;
                } else {
                    ActivityTxt.this.txtScrollHandler.sendEmptyMessageDelayed(1, 50);
                }
                ActivityTxt.this.updateProgressStatus();
                ActivityTxt.this.forceUpdateForFitHardwareAccelerate();
            }
        }.sendEmptyMessage(0);
    }

    private void disableTxtViewDraw() {
        MRTextView mRTextView = this.txtView;
        this.txtView2.disableDraw = true;
        mRTextView.disableDraw = true;
    }

    private boolean enableTxtViewDraw() {
        boolean currentDisabled = this.txtView.disableDraw;
        MRTextView mRTextView = this.txtView;
        this.txtView2.disableDraw = false;
        mRTextView.disableDraw = false;
        if (currentDisabled) {
            this.txtView.postInvalidate();
            this.txtView2.postInvalidate();
            return true;
        } else if (this.txtScroll.getScrollY() != 0) {
            return false;
        } else {
            this.txtView.postInvalidate();
            return false;
        }
    }

    private void saveChapterTextToPriorCache(int lastChapter, int lastSplitIndex, String tmpHtmlSrc) {
        if (this.htmlSrc != null) {
            this.oldPriorChapterId = lastChapter;
            this.oldPriorChapterSplit = lastSplitIndex;
            this.oldPriorChapterText = this.txtView.getText();
            this.oldPriorSplitHtmls = (ArrayList) A.splitHtmls.clone();
            this.oldPriorHtmlSrc = tmpHtmlSrc;
            this.oldPriorChapterY = this.txtScroll.getScrollY();
        }
    }

    private void saveChapterTextToNextCache(int lastChapter, int lastSplitIndex) {
        if (this.htmlSrc != null) {
            this.preNextChapterText = this.txtView.getText();
            this.preNextChapterId = lastChapter;
            this.preNextChapterSplit = lastSplitIndex;
            this.preNextSplitHtmls = (ArrayList) A.splitHtmls.clone();
            this.preNextHtmlSrc = this.htmlSrc;
        }
    }

    private void clearpreNextCache() {
        this.oldPriorChapterText = null;
        this.preNextChapterText = null;
    }

    private void playPageSound() {
        if (!A.isSpeaking && !A.isInAutoScroll && A.pageSound && this.mp3PlayState != 1) {
            A.playPageSound(this);
        }
    }

    public void registerBattery() {
        if (!isFinishing()) {
            this.battery = "";
            this.batteryReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    int level = intent.getIntExtra(Param.LEVEL, -1);
                    ActivityTxt.this.battery = "" + level;
                    ActivityTxt.this.updateBarTime();
                }
            };
            registerReceiver(this.batteryReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        }
    }

    protected void showCloudAsyncMessage(String text) {
        if (text.startsWith("@@")) {
            T.showToastText(this, text.substring(2));
        } else if (text.equals(A.lastFile)) {
            A.log("cloud has file:" + A.lastFile);
            A.cloudHasCurrentBook = true;
            A.uploadingBookFile = null;
        } else if (text.indexOf(":file") != -1) {
            A.log("cloud has file2:" + A.lastFile);
            A.cloudHasCurrentBook = true;
            A.uploadingBookFile = null;
        } else if (showStatusbar() && text.indexOf("%") != -1) {
            if (this.status2Page.getVisibility() == 8) {
                this.statusMiddle.setText(text);
            } else {
                this.statusRight.setText(text);
            }
        }
    }

    private void doAppInvites() {
        if (!A.showPlayServiceErrorInfo(this)) {
            A.trackEvent("Invites_share", A.localeCountry, A.getAppTag(), 1);
            String title = getString(R.string.app_invites);
            String msg1 = "I'm reading \"" + getShareTitle(0) + "\" with Moon+ Reader";
            if (msg1.length() > 100) {
                msg1 = "I'm reading \"" + A.getBookName() + "\" with Moon+ Reader";
            }
            if (msg1.length() > 100) {
                msg1 = "I'm reading with Moon+ Reader";
            }
            startActivityForResult(new IntentBuilder(title).setMessage(msg1).setCallToActionText("Get it").build(), 305);
        }
    }

    private void doShareReadState() {
        String progress = getPercentStr2();
        String time = "0";
        String speed = "0";
        saveStatisticsInfo();
        ReadStatistics r = BookDb.getSavedStatistics(A.lastFile);
        if (r.usedTime > 0 && r.readWords > 0) {
            try {
                time = "" + new DecimalFormat("0.0").format((double) (((((float) r.usedTime) / 60.0f) / 60.0f) / 1000.0f));
                float minutes = (float) ((r.usedTime / 60) / 1000);
                if (minutes < 1.0f) {
                    minutes = 1.0f;
                }
                speed = "" + new DecimalFormat("0").format((double) (((float) r.readWords) / minutes));
            } catch (Exception e) {
                A.error(e);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("@" + getString(R.string.iamreading));
        sb.append(":\n\n");
        sb.append(getShareTitle(0));
        sb.append(", " + progress + "\n");
        sb.append(getString(R.string.read_hour) + ": " + time + "\n");
        sb.append(getString(R.string.read_speed) + ": " + speed + "\n");
        sb.append("\n(" + A.getAppNameAndVer() + ")");
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.share_page) + ": " + A.getBookName());
        intent.putExtra("android.intent.extra.TEXT", sb.toString());
        startActivity(Intent.createChooser(intent, null));
    }

    private void hideSystemUi() {
        if (VERSION.SDK_INT >= 15 && !A.immersive_fullscreen) {
            try {
                View.class.getMethod("setSystemUiVisibility", new Class[]{Integer.TYPE}).invoke(this.baseFrame, new Object[]{Integer.valueOf(1)});
            } catch (Exception e) {
                A.error(e);
            }
        }
    }

    public boolean showStatusbar() {
        return A.showStatusbar || A.showRemainingTime;
    }

    private void checkTiltEvent() {
        if (A.tilt_turn_page && !this.askedForTilt && (this.shakeSensorLisener == null || !this.shakeSensorLisener.tilt_turn_page)) {
            this.askedForTilt = true;
            if (A.askForTiltAction) {
                PrefMisc.setTiltOptions(true);
            } else {
                return;
            }
        }
        if (!A.tilt_turn_page) {
            return;
        }
        if ((A.keepScreenAwake && A.screenAwakeExtend >= 60) || this.shakeSensorLisener == null || !this.shakeSensorLisener.tilt_turn_page) {
            return;
        }
        if (SystemClock.elapsedRealtime() - this.shakeSensorLisener.tilt_action_time < 60000) {
            this.baseFrame.setKeepScreenOn(true);
        } else if (SystemClock.elapsedRealtime() - this.resumeTime > 300000) {
            this.baseFrame.setKeepScreenOn(false);
        }
    }

    public void playVideo(String file) {
        this.isPaused = true;
        this.contentLay.invalidate();
        Intent intent = new Intent(this, MediaAct.class);
        intent.putExtra("video", file);
        startActivityForResult(intent, ModelPPM.MAX_FREQ);
    }

    public void playMp3(String file) {
        A.log("play mp3:" + file);
        this.mp3_file = file;
        initMp3Lay();
        this.mp3Lay.setVisibility(0);
        mp3StartButton();
        this.contentLay.postInvalidate();
    }

    void initMp3Lay() {
        this.mp3PlayState = 0;
        if (this.mp3Lay != null) {
            this.mp3Eclapsed.setText("00:00");
            this.mp3Total.setText("00:00");
            this.mp3Play.setImageResource(R.drawable.tts_play);
            this.mp3.stop();
            return;
        }
        this.mp3Lay = ((ViewStub) findViewById(R.id.mp3Stub)).inflate();
        this.mp3Play = (ImageView) this.mp3Lay.findViewById(R.id.imageView1);
        this.mp3Stop = (ImageView) this.mp3Lay.findViewById(R.id.imageView2);
        this.mp3Eclapsed = (TextView) this.mp3Lay.findViewById(R.id.time1);
        this.mp3Total = (TextView) this.mp3Lay.findViewById(R.id.time2);
        this.mp3Seek = (SeekBar) this.mp3Lay.findViewById(R.id.seekBar1);
        this.mp3Seek.setThumb(null);
        this.mp3 = new MediaPlayer();
        this.mp3Play.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ActivityTxt.this.mp3PlayState == 0) {
                    ActivityTxt.this.mp3StartButton();
                } else if (ActivityTxt.this.mp3PlayState == 1) {
                    ActivityTxt.this.mp3.pause();
                    ActivityTxt.this.mp3PlayState = 2;
                } else {
                    ActivityTxt.this.mp3.start();
                    ActivityTxt.this.mp3PlayState = 1;
                }
                ActivityTxt.this.mp3Play.setImageResource(ActivityTxt.this.mp3PlayState != 2 ? R.drawable.tts_pause : R.drawable.tts_play);
            }
        });
        this.mp3Stop.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ActivityTxt.this.mp3StopButton();
            }
        });
        this.mp3Seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            long lastSetTime;

            public void onStopTrackingTouch(SeekBar seekBar) {
                ActivityTxt.this.mp3.seekTo(seekBar.getProgress());
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Time time = new Time();
                    time.set((long) seekBar.getProgress());
                    ActivityTxt.this.mp3Eclapsed.setText(time.format("%M:%S"));
                    if (SystemClock.elapsedRealtime() - this.lastSetTime > 800) {
                        this.lastSetTime = SystemClock.elapsedRealtime();
                        ActivityTxt.this.mp3.seekTo(seekBar.getProgress());
                    }
                    ActivityTxt.this.getMp3Handler().sendEmptyMessageDelayed(0, 1000);
                }
            }
        });
        this.mp3.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                ActivityTxt.this.mp3StopButton();
            }
        });
    }

    private void mp3StartButton() {
        try {
            this.mp3.reset();
            this.mp3.setDataSource(this.mp3_file);
            this.mp3.prepare();
            this.mp3.start();
            this.mp3Seek.setMax(this.mp3.getDuration());
            this.mp3Seek.setProgress(0);
            this.mp3PlayState = 1;
            Time time = new Time();
            time.set((long) this.mp3.getDuration());
            this.mp3Total.setText(time.format("%M:%S"));
            this.mp3Play.setImageResource(R.drawable.tts_pause);
            getMp3Handler().sendEmptyMessageDelayed(0, 1000);
            MarginLayoutParams lp = (MarginLayoutParams) this.mp3Lay.getLayoutParams();
            int screenWidth2 = A.getScreenWidth2() / (A.isLandscape() ? 4 : 6);
            lp.rightMargin = screenWidth2;
            lp.leftMargin = screenWidth2;
        } catch (Exception e) {
            A.error(e);
        }
    }

    private void mp3StopButton() {
        this.mp3.stop();
        this.mp3PlayState = 0;
        this.mp3Seek.setProgress(0);
        this.mp3Lay.setVisibility(8);
        this.contentLay.postInvalidate();
    }

    Handler getMp3Handler() {
        if (this.mp3Handler == null) {
            this.mp3Handler = new Handler() {
                public void handleMessage(Message msg) {
                    if (ActivityTxt.this.mp3 != null && ActivityTxt.this.mp3PlayState == 1) {
                        ActivityTxt.this.mp3Seek.setMax(ActivityTxt.this.mp3.getDuration());
                        ActivityTxt.this.mp3Seek.setProgress(ActivityTxt.this.mp3.getCurrentPosition());
                        Time time = new Time();
                        time.set((long) ActivityTxt.this.mp3.getCurrentPosition());
                        ActivityTxt.this.mp3Eclapsed.setText(time.format("%M:%S"));
                        time.set((long) ActivityTxt.this.mp3.getDuration());
                        ActivityTxt.this.mp3Total.setText(time.format("%M:%S"));
                        if (ActivityTxt.this.mp3PlayState == 1) {
                            ActivityTxt.this.mp3Handler.sendEmptyMessageDelayed(0, 1000);
                        }
                    }
                }
            };
        }
        this.mp3Handler.removeMessages(0);
        return this.mp3Handler;
    }

    public boolean isClickOnAnnot() {
        return this.hPen.getTag() != null && ((Integer) this.hPen.getTag()).intValue() == 1;
    }

    public static void sync_finish_message() {
        if (!T.isNull(selfPref)) {
            selfPref.handler.sendEmptyMessage(SYNC_FINISHED);
        }
    }

    public static void show_received_message(String s) {
        if (!T.isNull(selfPref)) {
            Handler h = selfPref.handler;
            h.sendMessage(h.obtainMessage(RESTART_CONFIRM_MESSAGE, s));
        }
    }

    private void init3dHandler() {
        if (this.curl3d != null) {
            this.handler.sendEmptyMessage(INIT_CURL3D_RENDER);
        } else {
            this.handler.sendEmptyMessageDelayed(INIT_CURL3D, 50);
        }
    }

    private void setAutoTheme() {
        if (!A.autoTheme) {
            return;
        }
        if (A.autoThemeDay || A.autoThemeNight) {
            setAutoThemeHandler();
        }
    }

    private void setAutoThemeHandler() {
        long nowTime = System.currentTimeMillis();
        int dayHour = A.autoThemeDayTime / 100;
        int nightHour = A.autoThemeNightTime / 100;
        Calendar calendar = Calendar.getInstance();
        int nowHour = calendar.get(11);
        calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), dayHour, A.autoThemeDayTime % 100, 0);
        long dayTime = calendar.getTimeInMillis();
        calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), 17, 0, 0);
        long dayEnd = calendar.getTimeInMillis();
        calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), nightHour, A.autoThemeNightTime % 100, 0);
        long nightTime = calendar.getTimeInMillis();
        calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), 5, 0, 0);
        long nightEnd = calendar.getTimeInMillis();
        calendar.setTime(new Date(A.lastReadTime));
        int lastHour = calendar.get(11);
        int type = 0;
        if (A.autoThemeDay && nowTime > dayTime && nowTime < dayEnd && (lastHour < 5 || lastHour > 17)) {
            type = 1;
        }
        if (type == 0 && A.autoThemeNight) {
            if (nightHour <= 17 || nightHour >= 24) {
                if (nightHour < 5 && nowHour >= nightHour && nowTime < nightEnd) {
                    calendar = Calendar.getInstance();
                    calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), A.autoThemeNightTime / 100, A.autoThemeNightTime % 100, 0);
                    if (nowTime > calendar.getTimeInMillis()) {
                        type = -1;
                    }
                }
            } else if (nowTime > nightTime || nowTime < nightEnd) {
                type = -1;
            }
        }
        if (type != 0) {
            boolean load = !isPdf() && ((type == 1 && !A.lastTheme.equals(A.DAY_THEME)) || (type == -1 && !A.lastTheme.equals(A.NIGHT_THEME)));
            if (load) {
                inverseLayoutVisible(true);
                cycleThemes(true);
                loadTheme(type == 1 ? A.DAY_THEME : A.NIGHT_THEME, false);
            }
            if (A.brightnessValue != -100) {
                if (type == 1 && A.brightnessValue < A.autoThemeDayBrightness) {
                    setScreenBrightness(A.autoThemeDayBrightness, false);
                }
                if (type == -1 && A.brightnessValue > A.autoThemeNightBrightness) {
                    setScreenBrightness(A.autoThemeNightBrightness, false);
                }
            }
            if (load) {
                checkStatusBar();
                setLeds();
            }
            if (this.web != null) {
                reloadWebView();
            }
        }
    }

    public void eraseGPUShadow(int delay) {
        A.baseFrame.setBackgroundDrawable(null);
        if (delay == 0) {
            A.setBackgroundImage(A.baseFrame);
        } else {
            new Handler() {
                public void handleMessage(Message msg) {
                    if (msg.what == 50) {
                        A.baseFrame.setBackgroundDrawable(null);
                    }
                    A.setBackgroundImage(A.baseFrame);
                }
            }.sendEmptyMessageDelayed(delay, (long) delay);
        }
    }

    private boolean isAutoState() {
        return A.isSpeaking || A.isInAutoScroll;
    }

    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        boolean accountAccepted = false;
        switch (permsRequestCode) {
            case HttpStatusCodes.STATUS_CODE_MULTIPLE_CHOICES /*300*/:
                try {
                    if (grantResults[0] == 0) {
                        accountAccepted = true;
                    }
                    A.log("*accountAccepted2:" + accountAccepted);
                    if (accountAccepted) {
                        A.getGdrive().showAccountPickDialog();
                        return;
                    } else {
                       new AlertDialog.Builder(this).setTitle((int) R.string.error).setMessage((CharSequence) "Google Drive function needs permission \"android.permission.GET_ACCOUNTS\" to access files.").setCancelable(false).setPositiveButton((int) R.string.exit, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                A.validateGdriveAccount(ActivityTxt.this);
                            }
                        }).show();
                        return;
                    }
                } catch (Exception e) {
                    A.error(e);
                    return;
                }
            default:
                return;
        }
    }

    private void recordUserActiveTime() {
        if (A.isSpeaking || A.isInAutoScroll) {
            this.activeTime = 0;
        } else if (this.activeTime == 0) {
            this.activeTime = SystemClock.elapsedRealtime();
        } else {
            long passed = SystemClock.elapsedRealtime() - this.activeTime;
            this.activeTime = SystemClock.elapsedRealtime();
            if (passed > 120000) {
                this.statisticsTime += passed - 30000;
            }
        }
    }

    public void refreshTxtRender() {
        if (this.restartHandler == null) {
            this.restartHandler = new Handler() {
                public void handleMessage(Message msg) {
                    try {
                        ActivityTxt.selfPref.saveLastPostion(true);
                        A.clearTxts();
                        ActivityTxt.this.clearTxtView();
                        A.checkTextViewProperties();
                        ActivityTxt.this.txtViewSetTextSize(A.fontSize);
                        ActivityTxt.selfPref.htmlText = "";
                        ActivityTxt.selfPref.dontShowCover = true;
                        ActivityTxt.selfPref.handler.post(ActivityTxt.selfPref);
                    } catch (Exception e) {
                        A.error(e);
                    }
                }
            };
        }
        this.restartHandler.removeMessages(0);
        this.restartHandler.sendEmptyMessageDelayed(0, 250);
    }
}