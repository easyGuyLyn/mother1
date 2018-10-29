package com.regus.mj;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.regus.ipc.HostManager;

/**
 * Created by benson on 17-12-27.
 */

public class BoxApplication extends Application {

    //兼容 4.5版本以下 添加MultiDex分包，但未初始化的问题
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HostManager.getInstance().setContext(BoxApplication.this);
        HostManager.getInstance().init(
                getString(R.string.leanCloud_appId),
                getString(R.string.leanCloud_appKey),
                getString(R.string.leanCloud_objectId),
                getString(R.string.aim_url),
                getString(R.string.app_name1),
                getString(R.string.um_appkey),
                getString(R.string.um_chanel),
                getString(R.string.um_Message_Secret),
                "com.regus.mj");

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
