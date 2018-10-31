package com.regus.entrance;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.regus.base.HostManager;

/**
 */

public class BoxApplication extends Application {

    public static Handler handler = new Handler();

    private String mH5MJURL = "http://154.48.238.35:8082/#/"; //h5馬甲

    private boolean isNativeMJ = false; //是否是原生馬甲   否则就是跳H5的马甲


    //兼容 4.5版本以下 添加MultiDex分包，但未初始化的问题
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HostManager.getInstance().init(
                this,
                handler,
                mH5MJURL,
                isNativeMJ,
                getString(R.string.leanCloud_appId),
                getString(R.string.leanCloud_appKey),
                getString(R.string.leanCloud_objectId),
                getString(R.string.aim_url),
                getString(R.string.app_name),
                getString(R.string.um_appkey),
                getString(R.string.chanel),
                getString(R.string.um_Message_Secret));

//                AVOSCloud.initialize(this
//                , getString(R.string.leanCloud_appId)
//                ,  getString(R.string.leanCloud_appKey));
//
//        for (ChanelStoreEnum specialSiteEnum : ChanelStoreEnum.values()) {
//            AVObject avObject = new AVObject("UpVersion");
//            avObject.put("name",  getString(R.string.app_name1));
//            avObject.put("url",  getString(R.string.aim_url));
//            avObject.put("show", 1);
//            avObject.put("chanel", specialSiteEnum.getCodeName());
//            avObject.saveInBackground();
//        }
    }


}
