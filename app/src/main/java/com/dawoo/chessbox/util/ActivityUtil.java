package com.dawoo.chessbox.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.dawoo.chessbox.BoxApplication;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.ipc.IPCSocketManager;
import com.dawoo.chessbox.util.Utils.MediaplayerUtil;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.packageref.PackageInfoUtil;
import com.dawoo.ipc.control.IpcWebViewActivity;

import java.util.List;

import static com.dawoo.chessbox.ConstantValue.WEBVIEW_TYPE_THIRD_ORDINARY;

/**
 * 一些页面的跳转
 * Created by benson on 18-1-14.
 */

public class ActivityUtil {
    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }



    /**
     * 跳转游戏
     *
     * @param url               全路径
     * @param msg               错误信息
     * @param type              类型
     * @param ScreenOrientation 携带屏幕方向  1 必须竖屏  2 必须横屏  3 动态切换
     * @param apiId             游戏apiId
     */
    public static void startGameWebView(String url, String msg, String type, int ScreenOrientation, int apiId) {
//        if (TextUtils.isEmpty(url)) {
//            SingleToast.showMsg("正在维护中。。。。");
//            return;
//        }
//
//        if (TextUtils.isEmpty(url)) {
//            SingleToast.showMsg(msg);
//            return;
//        }
//
//        if (!url.contains("http")) {
//            url = DataCenter.getInstance().getIp() + "/" + url;
//        }

//        Intent intent = new Intent(mContext, WebViewActivity.class);
//        intent.putExtra(ConstantValue.WEBVIEW_URL,  NetUtil.replaceIp2Domain(url));
//        intent.putExtra(ConstantValue.WEBVIEW_TYPE, type);
//        intent.putExtra(ConstantValue.GAME_APIID, apiId);
//        intent.putExtra(WebViewActivity.SCREEN_ORITATION, ScreenOrientation);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
        if (!PackageInfoUtil.isServiceRunning(mContext, "com.dawoo.ipc.server.IPCServerService")) {
            IPCSocketManager.getInstance().startServerService();
        }
        Intent intent = new Intent(mContext, IpcWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConstantValue.WEBVIEW_URL, url);
        bundle.putString(ConstantValue.WEBVIEW_TYPE, type);
        bundle.putInt(IpcWebViewActivity.SCREEN_ORITATION, ScreenOrientation);
       // bundle.putInt(ConstantValue.GAME_APIID, apiId);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
     //  MediaplayerUtil.newInstance().releaseWebView();
    }


    public static void startH5(String url) {
        ActivityUtil.startGameWebView(url, "", WEBVIEW_TYPE_THIRD_ORDINARY, 3, 0);
    }



    public static void startOtherapp(String packageName) {
        if (isInstalled(packageName)) {
            try {
                PackageManager packageManager
                        = BoxApplication.getContext().getPackageManager();
                Intent intent = packageManager.
                        getLaunchIntentForPackage(packageName);
                mContext.startActivity(intent);
            } catch (Exception e) {
                SingleToast.showMsg("您的手机未安装该应用");
            }
        } else {
            SingleToast.showMsg("您的手机未安装该应用");
        }

    }

    private static boolean isInstalled(String packageName) {
        final PackageManager packageManager = BoxApplication.getContext().getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            Log.e("PackageInfo", pinfo.get(i).packageName);
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }




}
