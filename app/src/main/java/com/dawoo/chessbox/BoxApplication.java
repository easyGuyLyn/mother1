package com.dawoo.chessbox;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.ipc.IPCSocketManager;
import com.dawoo.chessbox.net.TlsSniSocketFactory;
import com.dawoo.chessbox.net.TrueHostnameVerifier;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.BackGroundUtil;
import com.dawoo.chessbox.util.ScreenRotateUtil;
import com.dawoo.chessbox.util.line.CrashHandler;
import com.dawoo.chessbox.util.SSLUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.dawoo.chessbox.net.RetrofitHelper.DEFAULT_READ_TIMEOUT_SECONDS;
import static com.dawoo.chessbox.net.RetrofitHelper.DEFAULT_TIMEOUT_SECONDS;
import static com.dawoo.chessbox.net.RetrofitHelper.DEFAULT_WRITE_TIMEOUT_SECONDS;

/**
 * Created by benson on 17-12-27.
 */

public class BoxApplication extends Application {
    private static Context context;
    public static Handler handler = new Handler();

    //兼容 4.5版本以下 添加MultiDex分包，但未初始化的问题
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //不用这个工具库我浑身难受
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not connect your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
        initOkHttpUtils();
        context = getApplicationContext();
        BackGroundUtil.registerActivityLifecycleCallbacks(this);
        if (!BuildConfig.DEBUG) {
            CrashHandler.getInstance().init();
        }
        DataCenter.getInstance().getSysInfo().initSysInfo(context);
        ActivityUtil.setContext(context);
        // 加载音频
        SoundUtil.getInstance().load();


        loadX5();

        ScreenRotateUtil.getInstance(this).start();

        IPCSocketManager.getInstance().startServerService();
    }


    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 加载x5
     */
    void loadX5() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("loadX5", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }



    public static void initOkHttpUtils() {
        //设置https
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)//失败重连
                .sslSocketFactory(new TlsSniSocketFactory(), new SSLUtil.TrustAllManager())
                .hostnameVerifier(new TrueHostnameVerifier())
                .build();
        OkHttpUtils.initClient(client);
    }


}
