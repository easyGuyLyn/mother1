package com.dawoo.chessbox.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.dawoo.chessbox.BoxApplication;
import com.dawoo.chessbox.ipc.IPCSocketManager;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.chessbox.view.Utils.MediaplayerUtil;
import com.dawoo.chessbox.view.activity.SplashActivity;
import com.dawoo.coretool.util.packageref.PackageInfoUtil;
import com.dawoo.ipc.control.IpcWebViewActivity;
import com.dawoo.ipc.server.IPCServerService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Archar on 2018
 */
public class BackGroundUtil {


    private static long minTime = 0;//app需要在后台多长时间，才应该成立手势检测的条件之一
    public static boolean isShouldJumpGesture = false;
    public static boolean isShouldReStartRemoteSv = false;//是否应该重启远程服务

    //定义计时器
    private static Timer mTimer;
    private static TimerTask mTimerTask;
    private static long time = 0;
    public volatile static int refCount = 0;//当前处于前台的页面

    public static void registerActivityLifecycleCallbacks(Application application) {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new InnerTimerTask();
        }
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleListener());
    }

    /**
     * 全局页面的生命周期回调
     */
    private static class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {


        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.e("refCount", refCount + "onActivityCreated");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            LogUtils.e(activity.getComponentName() + " start 了");
            refCount++;
            getureActivityStart(activity);
            if (refCount == 1) {
                if (isShouldReStartRemoteSv
                        && !PackageInfoUtil.isServiceRunning(BoxApplication.getContext(), "com.dawoo.ipc.server.IPCServerService")) {
                    Log.e("IPC ", "startServerService");
                    IPCSocketManager.getInstance().startServerService();
                    isShouldReStartRemoteSv = false;
                }
                if (activity instanceof IpcWebViewActivity) {
                    return;
                }
                MediaplayerUtil.newInstance().play();
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.e("refCount", refCount + "onActivityResumed");
        }

        @Override
        public void onActivityPaused(Activity activity) {

            Log.e("refCount", refCount + "onActivityPaused");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.e("refCount", refCount + "onActivityStopped");
            LogUtils.e(activity.getComponentName() + "  stop 了");
            refCount--;
            getureActivityStop(activity);
            if (refCount == 0) {
                isShouldReStartRemoteSv = true;
                closeMusic();
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.e("refCount", refCount + "onActivitySaveInstanceState");

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.e("refCount", refCount + "onActivityDestroyed");
            if (refCount == 0) {
                isShouldReStartRemoteSv = true;
                closeMusic();
            }
        }
    }

    private static void closeMusic() {
        MediaplayerUtil.newInstance().release();
    }

    private static class InnerTimerTask extends TimerTask {

        @Override
        public void run() {
            time += 1000;
            //   LogUtils.e("gesture timer----------- " + time);
            if (time >= minTime) {
                isShouldJumpGesture = true;
            }
        }
    }

    private static void getureActivityStart(Activity activity) {
        if (!SharePreferenceUtil.getGestureFlag()) {
            return;
        }
        LogUtils.e("gesture Started refCount----------- " + refCount);
        mTimerTask.cancel();
        time = 0;
        if (isShouldJumpGesture) {
            gestureCheck(activity);
        }
    }

    private static void getureActivityStop(Activity activity) {
        if (!SharePreferenceUtil.getGestureFlag()) {
            return;
        }
        LogUtils.e(activity.getComponentName() + "refCount " + refCount);
        if (refCount == 0) {
            LogUtils.e("gesture refCount----------- " + refCount);
            isShouldJumpGesture = false;
            time = 0;
            mTimerTask = new InnerTimerTask();
            mTimer.schedule(mTimerTask, 0, 1001);
        }
    }

    /**
     * 跳转逻辑条件
     *
     * @param activity
     */
    public synchronized static void gestureCheck(Activity activity) {
        if (!SharePreferenceUtil.getGestureFlag()) {
            return;
        }
        if (activity instanceof SplashActivity) { //启动页不检测
            return;
        }
//        if (activity instanceof RegisterActivity) {//注册页面不检测
//            return;
//        }
//        if (activity instanceof LoginActivity) {//登录页面不检测
//            return;
//        }
//        if (activity instanceof GestureActivity) {//手势页不自检
//            return;
//        }
//
//        if (!ActivityStackManager.getInstance().checkActivity(GestureActivity.class)) {
//            Intent intent = new Intent(activity, GestureActivity.class);
//            intent.putExtra(GEST_FLAG, CHECK_PWD_BACKGROUND);
//            activity.startActivity(intent);
//        }
    }


}
