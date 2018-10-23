package com.dawoo.ipc;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;


public class HostManager {

    private Context context;

    private String leanCloud_objectId;
    private String aim_url;


    private static final HostManager ourInstance = new HostManager();

    public static HostManager getInstance() {
        return ourInstance;
    }

    private HostManager() {
    }

    public String getLeanCloud_objectId() {
        return leanCloud_objectId;
    }


    public String getAim_url() {
        return aim_url;
    }



    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public void init(String leanCloud_appId,
                     String leanCloud_appKey,
                     String leanCloudObjectId,
                     String aimUrl,
                     String app_name,
                     String um_appkey,
                     String um_chanel,
                     String um_Message_Secret,
                     String resourcePackageName) {

        if (context == null) return;
        aim_url = aimUrl;

        leanCloud_objectId = leanCloudObjectId;

        initLeanCloud(leanCloud_appId, leanCloud_appKey, aimUrl, app_name);

        initUM(um_appkey, um_chanel, um_Message_Secret, resourcePackageName);

        loadX5();
    }


    private void initLeanCloud(String leanCloud_appId,
                               String leanCloud_appKey,
                               String aim_url,
                               String app_name) {
        AVOSCloud.initialize(context
                , leanCloud_appId
                , leanCloud_appKey);

//        AVObject avObject = new AVObject("UpVersion");
//        avObject.put("name", getString(R.string.app_name));
//        avObject.put("url", getString(R.string.aim_url));
//        avObject.put("show", 2);
//        avObject.put("chanel", "应用宝");
//        avObject.saveInBackground();


    }


    private void initUM(
            String um_appkey,
            String um_chanel,
            String um_Message_Secret,
            String ResourcePackageName) {

        if (BuildConfig.DEBUG) {
            UMConfigure.setLogEnabled(true);
        }

        UMConfigure.init(context, um_appkey
                , um_chanel
                , UMConfigure.DEVICE_TYPE_PHONE
                , um_Message_Secret);

        //统计
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        // 将默认Session间隔时长改为40秒。
        MobclickAgent.setSessionContinueMillis(1000 * 40);

        //推送
        PushAgent mPushAgent = PushAgent.getInstance(context);

        mPushAgent.setResourcePackageName(ResourcePackageName);

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
        QbSdk.initX5Environment(context, cb);
    }

}
