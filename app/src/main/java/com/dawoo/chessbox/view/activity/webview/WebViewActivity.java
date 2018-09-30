package com.dawoo.chessbox.view.activity.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.dawoo.chessbox.BoxApplication;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.SchemeEnum;
import com.dawoo.chessbox.mvp.view.IWebView;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.view.activity.BaseActivity;
import com.dawoo.chessbox.view.activity.MainActivity;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.coretool.util.activity.DensityUtil;
import com.dawoo.ipc.view.WDragViewLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dawoo.chessbox.ConstantValue.EVENT_REFRSH_API;
import static com.dawoo.chessbox.ConstantValue.WEBVIEW_TYPE_GAME;
import static com.dawoo.chessbox.ConstantValue.WEBVIEW_TYPE_GAME_FULLSCREEN_ALWAYS;
import static com.dawoo.chessbox.ConstantValue.WEBVIEW_TYPE_THIRD_ORDINARY;


public class WebViewActivity extends BaseActivity implements IWebView, View.OnClickListener {
    public static final String SCREEN_ORITATION = "ScreenOrientationEvent";

    private ImmersionBar mImmersionBar;

    //    @BindView(R.id.webview_fl)
//    DragViewLayout mWebviewFL;
    //    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.webview_fl)
    WDragViewLayout mWebviewFL;
    @BindView(R.id.videoContainer)
    FrameLayout mVideoContainer;

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
    private boolean misRefreshPage;
    private int mScreenOrientatioType = 3;// 1 必须竖屏  2 必须横屏  3 动态切换


    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_ipc_web_view);
        initScreenOrientation();
        if (getIntent().getStringExtra(ConstantValue.WEBVIEW_TYPE) != null) {
            mWebViewType = getIntent().getStringExtra(ConstantValue.WEBVIEW_TYPE);
            //除了电子之外的游戏和彩票                   第三方网页
            if (WEBVIEW_TYPE_GAME.equals(mWebViewType) || WEBVIEW_TYPE_THIRD_ORDINARY.equals(mWebViewType)) {
                setFullScreen(false);
                mImmersionBar = ImmersionBar.with(this).statusBarColor(R.color.black);
                mImmersionBar.init();
            } else if (WEBVIEW_TYPE_GAME_FULLSCREEN_ALWAYS.equals(mWebViewType)) {//电子有些游戏一开始不设置全屏，他会认为你永远不全屏
                setFullScreen(true);
            }
        }
    }

    //屏幕方向
    private void initScreenOrientation() {
        mScreenOrientatioType = getIntent().getIntExtra(SCREEN_ORITATION, 3);
        if (mScreenOrientatioType == 1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (mScreenOrientatioType == 2) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void initViews() {
        createWebView();
        initWebSetting();
    }

    @Override
    protected void initData() {
        RxBus.get().register(this);
        Intent intent = getIntent();
        mUrl = intent.getStringExtra(ConstantValue.WEBVIEW_URL);
        mUrl = NetUtil.handleUrl(mUrl);

        Log.e("Webview ", mUrl);

        // NetUtil.syncCookie(mUrl,DataCenter.getInstance().getCookie());
        mWebview.getSettings().setUserAgentString(mWebview.getSettings()
                .getUserAgentString().replace("app_android", "Android") + "; is_native=true");

        request(mUrl);
    }

    /**
     * 重力感應  橫屏切換
     */
    @Subscribe(tags = {@Tag(ConstantValue.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)})
    public void landScapeReverse(String s) {
        LogUtils.d(s);
        if (mScreenOrientatioType == 1 || mScreenOrientatioType == 2) return;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    /**
     * 重力感應   橫屏
     */
    @Subscribe(tags = {@Tag(ConstantValue.SCREEN_ORIENTATION_LANDSCAPE)})
    public void landScape(String s) {
        LogUtils.d(s);
        if (mScreenOrientatioType == 1 || mScreenOrientatioType == 2) return;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    /**
     * 重力感應  竖屏切換
     */
    @Subscribe(tags = {@Tag(ConstantValue.SCREEN_ORIENTATION_REVERSE_PORTRAIT)})
    public void portraitReverse(String s) {
        LogUtils.d(s);
        if (mScreenOrientatioType == 1 || mScreenOrientatioType == 2) return;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
    }

    /**
     * 重力感應   竖屏
     */
    @Subscribe(tags = {@Tag(ConstantValue.SCREEN_ORIENTATION_PORTRAIT)})
    public void portrait(String s) {
        LogUtils.d(s);
        if (mScreenOrientatioType == 1 || mScreenOrientatioType == 2) return;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    /**
     * 请求网页
     *
     * @param url
     */
    private void request(String url) {
        mWebview.loadUrl(url);
    }

    private void createWebView() {
        mWebview = new WebView(BoxApplication.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mWebview.setLayoutParams(layoutParams);
        mWebviewFL.addView(mWebview);
        createDragViewButton();
    }

    private void createDragViewButton() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                DensityUtil.dp2px(this, 50),
                DensityUtil.dp2px(this, 56),
                Gravity.BOTTOM | Gravity.RIGHT);
        params.setMargins(0, 0, 0, DensityUtil.dp2px(this, 56));
        mLl = new LinearLayout(this);
        mLl.setGravity(Gravity.CENTER);
        mLl.setLayoutParams(params);
        mLl.setBackgroundResource(R.mipmap.game_bg);
        mLl.setOrientation(LinearLayout.VERTICAL);

        View view = View.inflate(this, R.layout.qt_webview_progressbar, null);
        mProgressBar = view.findViewById(R.id.progressBar);
        mWebviewFL.addView(view);


        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(DensityUtil.dp2px(this, 28), DensityUtil.dp2px(this, 28));
        mHomeIv = new ImageView(this);
        mHomeIv.setLayoutParams(childParams);
        mHomeIv.setImageResource(R.mipmap.game_home);
        mHomeIv.setId(R.id.webview_iv_home);
        mHomeIv.setOnClickListener(this);

        mBackIv = new ImageView(this);
        mBackIv.setLayoutParams(childParams);
        mBackIv.setImageResource(R.mipmap.game_back);
        mBackIv.setId(R.id.webview_iv_back);
        mBackIv.setOnClickListener(this);
        mLl.addView(mHomeIv);
        mLl.addView(mBackIv);
        mWebviewFL.addView(mLl);
    }

    void setDragViewLandScapePos() {
        if (mLl == null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                DensityUtil.dp2px(this, 50),
                DensityUtil.dp2px(this, 56),
                Gravity.END);
        mLl.setLayoutParams(params);
    }

    void setDragViewPortrait() {
        if (mLl == null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                DensityUtil.dp2px(this, 50),
                DensityUtil.dp2px(this, 56),
                Gravity.BOTTOM | Gravity.RIGHT);
        params.setMargins(0, 0, 0, DensityUtil.dp2px(this, 56));
        mLl.setLayoutParams(params);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("web", "横屏");
            setFullScreen(true);
            // 横屏
            setDragViewLandScapePos();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("web", "竖屏");
            if (!mWebViewType.equals(WEBVIEW_TYPE_GAME_FULLSCREEN_ALWAYS)) {
                setFullScreen(false);
            }
            // 竖屏
            setDragViewPortrait();
        }
    }

    //动态设置全屏与非全屏   只有横屏有全屏
    private void setFullScreen(boolean issFullScreen) {
        if (issFullScreen) {
            setTheme(R.style.SplashTheme);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            setTheme(R.style.AppTheme);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    @Override
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
        webSettings.setPluginsEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        //多窗口
        webSettings.supportMultipleWindows();
        //获取触摸焦点
        mWebview.requestFocusFromTouch();
        //允许访问文件
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowUniversalAccessFromFileURLs(false);
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
        webSettings.setMediaPlaybackRequiresUserGesture(true);
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

        CookieManager.getInstance().setAcceptCookie(true);


        mWebview.setDownloadListener(new FileDownLoadListener());
        mWebview.setOnTouchListener(new MyWebviewOnTouchListener());
        mWebview.setWebViewClient(new CommonWebViewClient());
        mWebview.setWebChromeClient(new CommonWebChromeClient());
        mWebview.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        mWebview.setBackgroundResource(R.color.black);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == mHomeIv.getId()) {
            ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
        } else if (v.getId() == mBackIv.getId()) {
            ActivityStackManager.getInstance().finishActivity(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class CommonWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            setProgressBar(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            //  setWebViewTitleName(title);
        }

        //  Android 2.2 (API level 8)到Android 2.3 (API level 10)版本选择文件时会触发该隐藏方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, null);
        }

        // Android 3.0 (API level 11)到 Android 4.0 (API level 15))版本选择文件时会触发，该方法为隐藏方法
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, acceptType, null);
        }

        // Android 4.1 (API level 16) -- Android 4.3 (API level 18)版本选择文件时会触发，该方法为隐藏方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileInput(uploadMsg, null, false);
        }

        // Android 5.0 (API level 21)以上版本会触发该方法，该方法为公开方法
        @SuppressWarnings("all")
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (Build.VERSION.SDK_INT >= 21) {
                final boolean allowMultiple = fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE;//是否支持多选
                openFileInput(null, filePathCallback, allowMultiple);
                return true;
            } else {
                return false;
            }
        }


        @SuppressLint("NewApi")
        protected void openFileInput(final ValueCallback<Uri> fileUploadCallbackFirst, final ValueCallback<Uri[]> fileUploadCallbackSecond, final boolean allowMultiple) {
            //Android 5.0以下版本
            if (mFileUploadCallbackFirst != null) {
                mFileUploadCallbackFirst.onReceiveValue(null);
            }
            mFileUploadCallbackFirst = fileUploadCallbackFirst;

            //Android 5.0及以上版本
            if (mFileUploadCallbackSecond != null) {
                mFileUploadCallbackSecond.onReceiveValue(null);
            }
            mFileUploadCallbackSecond = fileUploadCallbackSecond;

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);

            if (allowMultiple) {
                if (Build.VERSION.SDK_INT >= 18) {
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
            }

            i.setType(mUploadableFileTypes);

            startActivityForResult(Intent.createChooser(i, "选择文件"), REQUEST_CODE_FILE_PICKER);
        }

        IX5WebChromeClient.CustomViewCallback mCallBack;

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
            fullScreen();
            mWebview.setVisibility(View.GONE);
            mVideoContainer.setVisibility(View.VISIBLE);
            mVideoContainer.addView(view);
            mCallBack = customViewCallback;
            super.onShowCustomView(view, customViewCallback);
        }

        @Override
        public void onShowCustomView(View view, int i, IX5WebChromeClient.CustomViewCallback customViewCallback) {
            fullScreen();
            mWebview.setVisibility(View.GONE);
            mVideoContainer.setVisibility(View.VISIBLE);
            mVideoContainer.addView(view);
            mCallBack = customViewCallback;
            super.onShowCustomView(view, i, customViewCallback);
        }

        @Override
        public void onHideCustomView() {
            fullScreen();
            if (mCallBack != null) {
                mCallBack.onCustomViewHidden();
            }
            mWebview.setVisibility(View.VISIBLE);
            mVideoContainer.removeAllViews();
            mVideoContainer.setVisibility(View.GONE);
            super.onHideCustomView();
        }
    }

    private void fullScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    private class CommonWebViewClient extends WebViewClient {

        @Override
        public void onLoadResource(WebView webView, String s) {
            super.onLoadResource(webView, s);
            Log.e("onPageLoadResource", s);
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e("onPageStarted", url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("onPageFinished", url);
            setRefreshPageClearHistory();
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            super.onReceivedError(webView, webResourceRequest, webResourceError);
        }


        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("onPageShoudOver", url);
            if (url == null) return false;
            if (url.contains("login/commonLogin.html")) {
                ActivityUtil.gotoLogin();
                return false;

            } else if (url.contains("/mainIndex.html")) {
                ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
//                RxBus.get().post(ConstantValue.EVENT_TYPE_GOTOTAB_HOME, "gotoHome");
            }

            return shouldfileterUrl(url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            switch (errorCode) {
                case ERROR_CONNECT:
                    mWebview.loadUrl("file:///android_asset/html/unNet.html");
                    break;
            }
        }


        /**
         * 拦截WebView网络请求（Android API < 21）
         * 只能拦截网络请求的URL，请求方法、请求内容等无法拦截
         */
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }

        /**
         * 拦截WebView网络请求（Android API >= 21）
         * 通过解析WebResourceRequest对象获取网络请求相关信息
         */
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Log.e("WebResourceRequest  ", "Cookie: " + CookieManager.getInstance().getCookie(DataCenter.getInstance().getDomain()) + " \n Method: " + request.getMethod() + "  \n Headers: " + request.getRequestHeaders().toString() + "\n");
            return super.shouldInterceptRequest(view, request);
        }
    }

    private void setRefreshPageClearHistory() {
        if (misRefreshPage) {
            misRefreshPage = false;
            mWebview.clearHistory();
        }
    }


    private class MyWebviewOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mWebview.requestFocus();
            return false;
        }
    }


    private class FileDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            startBrowsers(url);
        }
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
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * 调用浏览
     *
     * @param url
     */
    private void startBrowsers(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * 设置进度条
     *
     * @param progress
     */
    private void setProgressBar(int progress) {
        if (progress == 100) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            if (View.INVISIBLE == mProgressBar.getVisibility()) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            mProgressBar.setProgress(progress);
        }
    }


    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (requestCode == REQUEST_CODE_FILE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    //Android 5.0以下版本
                    if (mFileUploadCallbackFirst != null) {
                        mFileUploadCallbackFirst.onReceiveValue(intent.getData());
                        mFileUploadCallbackFirst = null;
                    } else if (mFileUploadCallbackSecond != null) {//Android 5.0及以上版本
                        Uri[] dataUris = null;

                        try {
                            if (intent.getDataString() != null) {
                                dataUris = new Uri[]{Uri.parse(intent.getDataString())};
                            } else {
                                if (Build.VERSION.SDK_INT >= 16) {
                                    if (intent.getClipData() != null) {
                                        final int numSelectedFiles = intent.getClipData().getItemCount();

                                        dataUris = new Uri[numSelectedFiles];

                                        for (int i = 0; i < numSelectedFiles; i++) {
                                            dataUris[i] = intent.getClipData().getItemAt(i).getUri();
                                        }
                                    }
                                }
                            }
                        } catch (Exception ignored) {
                        }
                        mFileUploadCallbackSecond.onReceiveValue(dataUris);
                        mFileUploadCallbackSecond = null;
                    }
                }
            } else {
                //这里mFileUploadCallbackFirst跟mFileUploadCallbackSecond在不同系统版本下分别持有了
                //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
                //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
                if (mFileUploadCallbackFirst != null) {
                    mFileUploadCallbackFirst.onReceiveValue(null);
                    mFileUploadCallbackFirst = null;
                } else if (mFileUploadCallbackSecond != null) {
                    mFileUploadCallbackSecond.onReceiveValue(null);
                    mFileUploadCallbackSecond = null;
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        }
    }


    boolean shouldfileterUrl(String url) {
        if (url == null) return false;
        try {
            if (url.startsWith(SchemeEnum.INTENT.getCode()) || url.startsWith(SchemeEnum.QQ.getCode())
                    || url.startsWith(SchemeEnum.ALIPAY.getCode()) || url.startsWith(SchemeEnum.WECHAT.getCode())) {


                if (url.startsWith(SchemeEnum.ALIPAY.getCode()) && !checkAliPayInstalled(this)) {
//                    mWebview.loadUrl("http://ds.alipay.com/?from=mobileweb");
//                    return false;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://ds.alipay.com/?from=mobileweb"));
                    startActivity(intent);
                } else {
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    intent.addCategory("android.intent.category.BROWSABLE");
                    intent.setComponent(null);
                    intent.setSelector(null);
                    List<ResolveInfo> resolves = getPackageManager().queryIntentActivities(intent, 0);
                    if (resolves.size() > 0) {
                        startActivityIfNeeded(intent, -1);
                    }
                }

            } else if (url.startsWith("https://payh5.bbnpay.com/") || isZXPay(url) || isGaotongPay(url) || isAimiSenPay(url)) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } else {
                mWebview.loadUrl(url);
            }
        } catch (Exception e) {
            LogUtils.e("跳转支付页面异常 ==> " + e.getMessage());
        }

        return true;
    }

    public boolean checkAliPayInstalled(Context context) {

        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }


    boolean isZXPay(String url) {
        // String code = getString(R.string.app_code);
        if (url.startsWith("https://zhongxin.junka.com/")) {
            return true;
        }
        return false;
    }

    boolean isGaotongPay(String url) {
        //String code = getString(R.string.app_code);
        if (url.startsWith("http://wgtj.gaotongpay.com/")) {
            return true;
        }
        return false;
    }

    boolean isAimiSenPay(String url) {
        //String code = getString(R.string.app_code);
        if (url.startsWith("https://www.joinpay.com")) {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            RxBus.get().post(EVENT_REFRSH_API, getIntent().getIntExtra(ConstantValue.GAME_APIID, -1) + "");
//            if (getIntent().getIntExtra(ConstantValue.GAME_APIID, -1) != -1) {
//                RxBus.get().post(EVENT_REFRSH_API, getIntent().getIntExtra(ConstantValue.GAME_APIID, -1) + "");
//            }
            //  mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();
            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //必须调用该方法，防止内存泄漏，

        RxBus.get().unregister(this);
        super.onDestroy();
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_LOGOUT)})
    public void logout(String s) {
        LogUtils.d(s);
        finish();
    }


}
