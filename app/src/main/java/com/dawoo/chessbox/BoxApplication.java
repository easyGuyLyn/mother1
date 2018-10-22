package com.dawoo.chessbox;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.dawoo.chessbox.util.ActivityUtil;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

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
        initOkHttpUtils();
        context = getApplicationContext();

        ActivityUtil.setContext(context);

        loadX5();

        initLeanCloud();

        initUM();
    }

    private void initLeanCloud() {
        AVOSCloud.initialize(this
                , getString(R.string.leanCloud_appId)
                , getString(R.string.leanCloud_appKey));

//        AVObject avObject = new AVObject("UpVersion");
//        avObject.put("name", getString(R.string.app_name);
//        avObject.put("url", getString(R.string.aim_url));
//        avObject.put("show", 2);
//        avObject.put("chanel", "应用宝");
//        avObject.saveInBackground();


    }


    private void initUM() {
        if (BuildConfig.DEBUG) {
            UMConfigure.setLogEnabled(true);
        }

        UMConfigure.init(context, getString(R.string.um_appkey)
                , getString(R.string.um_chanel)
                , UMConfigure.DEVICE_TYPE_PHONE
                , getString(R.string.um_Message_Secret));

        //统计
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        // 将默认Session间隔时长改为40秒。
        MobclickAgent.setSessionContinueMillis(1000*40);

        //推送
        PushAgent mPushAgent = PushAgent.getInstance(this);

        mPushAgent.setResourcePackageName("com.dawoo.chessbox");

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.e("u_push", "推送注册成功  deviceToken : " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("u_push", "推送注册失败  error 1 : " + s + " error 2 " + s1);
            }
        });


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
//        //设置https
//        OkHttpClient client = new OkHttpClient.Builder()
//                .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
//                .readTimeout(DEFAULT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
//                .writeTimeout(DEFAULT_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true)//失败重连
//                .sslSocketFactory(new TlsSniSocketFactory(), new SSLUtil.TrustAllManager())
//                .hostnameVerifier(new TrueHostnameVerifier())
//                .build();
//        OkHttpUtils.initClient(client);
    }


}
