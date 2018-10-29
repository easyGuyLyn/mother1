package com.regus.ipc.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.regus.ipc.R;
import com.regus.ipc.view.WDragViewLayout;
import com.gyf.barlibrary.ImmersionBar;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * archar  天纵神武
 **/
public class IpcWebViewActivity extends AppCompatActivity  {

    private static final String TAG = "IpcWebViewActivity  ";

    private ImmersionBar mImmersionBar;

    private WDragViewLayout mWebviewFL;
    private ProgressBar mProgressBar;
    private FrameLayout mVideoContainer;

    /**
     * webView 类型
     */
    static final String WEBVIEW_URL = "WEBVIEW_URL";
    static final String WEBVIEW_TYPE = "WEBVIEW_TYPE";
    static final String WEBVIEW_TYPE_GAME = "WEBVIEW_TYPE_GAME";
    static final String WEBVIEW_TYPE_GAME_FULLSCREEN_ALWAYS = "WEBVIEW_TYPE_GAME_FULLSCREEN_ALWAYS";
    // 平台一般网页
    static final String WEBVIEW_TYPE_ORDINARY = "WEBVIEW_TYPE_ORDINARY";
    // 第三方一般网页
    static final String WEBVIEW_TYPE_THIRD_ORDINARY = "WEBVIEW_TYPE_THIRD_ORDINARY";
    public static final String SCREEN_ORITATION = "ScreenOrientationEvent";

    public static final String IS_H5_MJ ="is_h5_mj";

    static final String GAME_APIID = "GAME_APIID";
    /**
     * Android 5.0以下版本的文件选择回调
     */
    protected ValueCallback<Uri> mFileUploadCallbackFirst;
    /**
     * Android 5.0及以上版本的文件选择回调
     */
    protected ValueCallback<Uri[]> mFileUploadCallbackSecond;
    protected static final int REQUEST_CODE_FILE_PICKER = 51426;
    protected String mUploadableFileTypes = "image/*";
    private WebView mWebview;
    private Handler mHandler = new Handler();
    private ImageView mHomeIv;
    private ImageView mBackIv;
    private String mWebViewType;
    private LinearLayout mLl;
    private String mUrl;
    private int mScreenOrientatioType = 3;// 1 必须竖屏  2 必须横屏  3 动态切换
    private int mGameApi = -1;//游戏api id
    private boolean misRefreshPage;

    private boolean mIS_h5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_ipc_web_view);
        PushAgent.getInstance(IpcWebViewActivity.this).onAppStart();
        initView();
        initData();
    }

    private void initView() {
        mWebviewFL = findViewById(R.id.webview_fl);
        mProgressBar = findViewById(R.id.progressBar);
        mVideoContainer = findViewById(R.id.videoContainer);
        createWebView();
        initWebSetting();
    }

    private void createWebView() {
        mWebview = new WebView(IpcWebViewActivity.this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mWebview.setLayoutParams(layoutParams);
        mWebviewFL.addView(mWebview);
        View view = View.inflate(this, R.layout.qt_webview_progressbar, null);
        mProgressBar = view.findViewById(R.id.progressBar);
        mWebviewFL.addView(view);
    }


    private void initData() {
        Bundle bundle = getIntent().getExtras();
        mUrl = bundle.getString(WEBVIEW_URL);
        mScreenOrientatioType = bundle.getInt(SCREEN_ORITATION);
        mWebViewType = bundle.getString(WEBVIEW_TYPE);
        mGameApi = bundle.getInt(GAME_APIID);
        mIS_h5=bundle.getBoolean(IS_H5_MJ);
        if(mIS_h5){
            mProgressBar.setVisibility(View.GONE);
        }else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        initScreenOrientation();
        initStatusBar();
        if (!TextUtils.isEmpty(mUrl) && mWebview != null) {
            Log.e(TAG, mUrl);
            mWebview.getSettings().setUserAgentString(mWebview.getSettings()
                    .getUserAgentString().replace("app_android", "Android") + "; is_native=true");
            mWebview.loadUrl(mUrl);
        }
    }


    //屏幕方向
    private void initScreenOrientation() {
        if (mScreenOrientatioType == 1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (mScreenOrientatioType == 2) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    //状态栏
    private void initStatusBar() {
        //除了电子之外的游戏和彩票                   第三方网页
        if (WEBVIEW_TYPE_GAME.equals(mWebViewType) || WEBVIEW_TYPE_THIRD_ORDINARY.equals(mWebViewType)) {
            setFullScreen(false);
            mImmersionBar = ImmersionBar.with(this).statusBarColor(R.color.black);
            mImmersionBar.init();
        } else if (WEBVIEW_TYPE_GAME_FULLSCREEN_ALWAYS.equals(mWebViewType)) {//电子有些游戏一开始不设置全屏，他会认为你永远不全屏
            setFullScreen(true);
        }
    }


    //动态设置全屏与非全屏   只有横屏有全屏
    private void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            setTheme(R.style.IPCFullScreenThem);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            setTheme(R.style.IPCAppTheme);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e(TAG, "横屏");
            setFullScreen(true);
            // 横屏
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e(TAG, "竖屏");
            if (!mWebViewType.equals(WEBVIEW_TYPE_GAME_FULLSCREEN_ALWAYS)) {
                setFullScreen(false);
            }
        }
    }


    public void initWebSetting() {
        WebSettings webSettings = mWebview.getSettings();

        //支持缩放，默认为true。
        webSettings.setSupportZoom(true);
        //调整图片至适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //设置默认编码
        webSettings.setDefaultTextEncodingName("utf-8");
        //支持插件
     //   webSettings.setPluginsEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        //多窗口
        webSettings.supportMultipleWindows();
        //获取触摸焦点
        mWebview.requestFocusFromTouch();
        //允许访问文件
        webSettings.setAllowFileAccess(true);
     //   webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setAllowContentAccess(true);
        //开启javascript
        webSettings.setJavaScriptEnabled(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        webSettings.setDomStorageEnabled(true);        //设置支持DomStorage
        //图片先不加载最后再加载
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }
    //    webSettings.setMediaPlaybackRequiresUserGesture(true);
        webSettings.setAppCacheEnabled(true);          // 启用缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //修改硬件加速导致页面渲染闪烁问题
        // mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebview.requestFocus();
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        /**
         * MIXED_CONTENT_ALWAYS_ALLOW：允许从任何来源加载内容，即使起源是不安全的；
         * MIXED_CONTENT_NEVER_ALLOW：不允许Https加载Http的内容，即不允许从安全的起源去加载一个不安全的资源；
         * MIXED_CONTENT_COMPATIBILITY_MODE：当涉及到混合式内容时，WebView 会尝试去兼容最新Web浏览器的风格。
         **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebview.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //    mWebview.addJavascriptInterface(new InJavaScriptCommon(), "gamebox");

        //  CookieManager.getInstance().setAcceptCookie(true);


//        mWebview.setDownloadListener(new FileDownLoadListener());
//        mWebview.setOnTouchListener(new MyWebviewOnTouchListener());
//        mWebview.setWebViewClient(new CommonWebViewClient());
//        mWebview.setWebChromeClient(new CommonWebChromeClient());
//        mWebview.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
//        mWebview.setBackgroundResource(R.color.black);

    }



    private void fullScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 返回上一个页面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
            // 返回键退回
            mWebview.goBack();
            return true;
        } else
           // back(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }






    @Override
    protected void onDestroy() {
        try {
            if (mWebview != null) {
                mWebview.clearHistory();
                ((ViewGroup) mWebview.getParent()).removeView(mWebview);
                mWebview.destroy();
                mWebview = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        super.onDestroy();

    }

}
