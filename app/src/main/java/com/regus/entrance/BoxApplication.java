package com.regus.entrance;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.regus.base.HostManager;

/**
 */

public class BoxApplication extends Application {

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

        HostManager.getInstance().init(
                this,
                handler,
                BuildConfig.h5_mj_url,
                BuildConfig.isNativeMJ,
                BuildConfig.leanCloud_appId,
                BuildConfig.leanCloud_appKey,
                getString(R.string.leanCloud_objectId),
               BuildConfig.aim_url,
                getString(R.string.app_name),
                BuildConfig.um_appkey,
                getString(R.string.chanel),
                BuildConfig.um_Message_Secret);

//                AVOSCloud.initialize(this
//                , getString(R.string.leanCloud_appId)
//                ,  getString(R.string.leanCloud_appKey));
//
//        for (ChanelStoreEnum specialSiteEnum : ChanelStoreEnum.values()) {
//            AVObject avObject = new AVObject("UpVersion");
//            avObject.put("name",  getString(R.string.app_name));
//            avObject.put("url",  getString(R.string.aim_url));
//            avObject.put("show", 1);
//            avObject.put("chanel", "app_三星应用商店");
//            avObject.saveInBackground();
////        }
    }


}
