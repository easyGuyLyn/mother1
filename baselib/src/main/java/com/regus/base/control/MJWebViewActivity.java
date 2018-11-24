package com.regus.base.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.regus.base.R;
import com.regus.base.util.LogUtils;
import com.regus.base.view.WDragViewLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * archar  天纵神武
 **/
public class MJWebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MJWebViewActivity  ";

    private ImmersionBar mImmersionBar;

    private WDragViewLayout mWebviewFL;
    private ProgressBar mProgressBar;

    /**
     * webView 类型
     */
    static final String WEBVIEW_URL = "WEBVIEW_URL";


    public static final String IS_H5_MJ = "is_h5_mj";

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
    private String mUrl;
    private int mScreenOrientatioType = 3;// 1 必须竖屏  2 必须横屏  3 动态切换

    private boolean mIS_h5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_ipc_web_view);
        PushAgent.getInstance(MJWebViewActivity.this).onAppStart();
        initView();
        initData();
    }

    private void initView() {
        mWebviewFL = findViewById(R.id.webview_fl);
        mProgressBar = findViewById(R.id.progressBar);
        createWebView();
        initWebSetting();
    }

    private void createWebView() {
        mWebview = new WebView(MJWebViewActivity.this);
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

        mIS_h5 = bundle.getBoolean(IS_H5_MJ);
        if (mIS_h5) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        initScreenOrientation();
        initStatusBar();
        if (!TextUtils.isEmpty(mUrl) && mWebview != null) {
            LogUtils.e(TAG, mUrl);
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
        setFullScreen(false);
        mImmersionBar = ImmersionBar.with(this).statusBarColor(R.color.black);
        mImmersionBar.init();
    }


    //动态设置全屏与非全屏   只有横屏有全屏
    private void setFullScreen(boolean fullScreen) {
        if (fullScreen) {
            setTheme(R.style.FullScreenThem);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            setTheme(R.style.BaseAppTheme);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtils.e(TAG, "横屏");
            setFullScreen(true);
            // 横屏
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LogUtils.e(TAG, "竖屏");
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

        DocumentBuilderFactory.newInstance().setExpandEntityReferences(false);

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

        //  CookieManager.getInstance().setAcceptCookie(true);


        mWebview.setDownloadListener(new FileDownLoadListener());
        mWebview.setOnTouchListener(new MyWebviewOnTouchListener());
        mWebview.setWebViewClient(new CommonWebViewClient());
        mWebview.setWebChromeClient(new CommonWebChromeClient());
        mWebview.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        mWebview.setBackgroundResource(R.color.black);

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
            mCallBack = customViewCallback;
            super.onShowCustomView(view, customViewCallback);
        }

        @Override
        public void onShowCustomView(View view, int i, IX5WebChromeClient.CustomViewCallback customViewCallback) {
            fullScreen();
            mWebview.setVisibility(View.GONE);
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
            LogUtils.e("onPageLoadResource", s);
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //    Log.e("onPageStarted", url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //    Log.e("onPageFinished", url);
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            super.onReceivedError(webView, webResourceRequest, webResourceError);
        }


        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.cancel();
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //  Log.e("onPageShoudOver", url);
            mWebview.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            switch (errorCode) {
                case ERROR_CONNECT:
                    //  mWebview.loadUrl("file:///android_asset/html/unNet.html");
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
            //   Log.e("WebResourceRequest  ", "Cookie: " + " \n Method: " + request.getMethod() + "  \n Headers: " + request.getRequestHeaders().toString() + "\n");
            return super.shouldInterceptRequest(view, request);
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
            return super.onKeyDown(keyCode, event);
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

        if (mIS_h5) return;

        if (progress == 100) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            if (View.INVISIBLE == mProgressBar.getVisibility()) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            mProgressBar.setProgress(progress);
        }
    }


    @Override
    public void onClick(View v) {

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
