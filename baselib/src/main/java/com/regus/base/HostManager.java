package com.regus.base;

import android.content.Context;
import android.os.Handler;

import com.avos.avoscloud.AVOSCloud;
import com.regus.base.util.LogUtils;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;


public class HostManager {


    private static final String TAG = "HostManager ";

    private Context context;

    private String leanCloud_objectId;
    private String aim_url; //指向的推广h5

    private Handler mHandler; //主线程handler

    private String mH5MjURL;//h5的url

    private boolean isNativeMJ = false; //是否是原生馬甲   否则就是跳H5的马甲


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

    public Handler getHandler() {
        return mHandler;
    }

    public String getH5MJURl() {
        return mH5MjURL;
    }


    public boolean isNativeMJ() {
        return isNativeMJ;
    }

    public void init(
            Context context,
            Handler handler,
            String H5MJUrl,
            boolean isH5Mj,
            String leanCloud_appId,
            String leanCloud_appKey,
            String leanCloudObjectId,
            String aimUrl,
            String app_name,
            String um_appkey,
            String um_chanel,
            String um_Message_Secret) {

        this.context = context;

        mHandler = handler;

        mH5MjURL = H5MJUrl;

        isNativeMJ = isH5Mj;

        aim_url = aimUrl.trim();

        leanCloud_objectId = leanCloudObjectId.trim();


        initLeanCloud(leanCloud_appId.trim(), leanCloud_appKey.trim(), aimUrl.trim(), app_name.trim());

        initUM(um_appkey.trim(), um_chanel.trim(), um_Message_Secret.trim());

        loadX5();
    }


    private void initLeanCloud(String leanCloud_appId,
                               String leanCloud_appKey,
                               String aim_url,
                               String app_name) {
        AVOSCloud.initialize(context
                , leanCloud_appId
                , leanCloud_appKey);
    }


    private void initUM(
            String um_appkey,
            String um_chanel,
            String um_Message_Secret) {

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

        //  mPushAgent.setResourcePackageName(ResourcePackageName);

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.e(TAG, "推送注册成功  deviceToken : " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.e(TAG, "推送注册失败  error 1 : " + s + " error 2 " + s1);
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
                LogUtils.e(TAG, " load x5   onViewInitFinished is " + arg0);
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
