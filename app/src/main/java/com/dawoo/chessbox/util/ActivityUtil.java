package com.dawoo.chessbox.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;

import com.dawoo.chessbox.ipc.IPCSocketManager;
import com.dawoo.chessbox.view.feagment_game.WebViewDialogFragment;
import com.dawoo.coretool.util.SPTool;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.chessbox.BoxApplication;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.view.Utils.MediaplayerUtil;
import com.dawoo.chessbox.view.activity.MainActivity;
import com.dawoo.chessbox.view.activity.MaintenanceActivity;
import com.dawoo.chessbox.view.activity.webview.WebViewActivity;
import com.dawoo.coretool.util.packageref.PackageInfoUtil;
import com.dawoo.ipc.control.IpcWebViewActivity;
import com.hwangjr.rxbus.RxBus;

import java.util.List;

import static com.dawoo.chessbox.view.feagment_game.WebViewDialogFragment.TYPE_SERVCIE;

/**
 * 一些页面的跳转
 * Created by benson on 18-1-14.
 */

public class ActivityUtil {
    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }


    public static void gotoLogin() {
        RxBus.get().post(ConstantValue.SHOW_LOGINDIALOGFRAGMENR, "LoinDialogFragment");
        ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
    }

    /**
     * 跳转支付，客服
     * @param url               全路径
     * @param msg               错误信息
     * @param type              类型
     * @param ScreenOrientation 携带屏幕方向  1 必须竖屏  2 必须横屏  3 动态切换  4跟随上次页面
     */
    public static void startWebView(String url, String msg, String type, int ScreenOrientation) {
        if (TextUtils.isEmpty(url) && TextUtils.isEmpty(msg)) {
            ToastUtil.showToastShort(mContext, mContext.getString(R.string.game_maintenance));
            return;
        }

        if (TextUtils.isEmpty(url)) {
            ToastUtil.showToastShort(mContext, mContext.getString(R.string.game_maintenance));
            return;
        }

        if (!url.contains("http")) {
            url = DataCenter.getInstance().getIp() + "/" + url;
        }

        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra(ConstantValue.WEBVIEW_URL,  NetUtil.replaceIp2Domain(url));
        intent.putExtra(ConstantValue.WEBVIEW_TYPE, type);
        intent.putExtra(WebViewActivity.SCREEN_ORITATION, ScreenOrientation);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

//        if (!PackageInfoUtil.isServiceRunning(mContext, "com.dawoo.ipc.server.IPCServerService")) {
//            IPCSocketManager.getInstance().startServerService();
//        }
//        Intent intent = new Intent(mContext, IpcWebViewActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(ConstantValue.WEBVIEW_URL, NetUtil.replaceIp2Domain(url));
//        bundle.putString(ConstantValue.WEBVIEW_TYPE, type);
//        bundle.putInt(WebViewActivity.SCREEN_ORITATION, ScreenOrientation);
//        intent.putExtras(bundle);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
        MediaplayerUtil.newInstance().releaseWebView();
    }

    /**
     * 跳转游戏
     * @param url               全路径
     * @param msg               错误信息
     * @param type              类型
     * @param ScreenOrientation 携带屏幕方向  1 必须竖屏  2 必须横屏  3 动态切换
     * @param apiId             游戏apiId
     */
    public static void startGameWebView(String url, String msg, String type, int ScreenOrientation, int apiId) {
        if (TextUtils.isEmpty(url)) {
            SingleToast.showMsg("正在维护中。。。。");
            return;
        }

        if (TextUtils.isEmpty(url)) {
            SingleToast.showMsg(msg);
            return;
        }

        if (!url.contains("http")) {
            url = DataCenter.getInstance().getIp() + "/" + url;
        }

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
        bundle.putString(ConstantValue.WEBVIEW_URL, NetUtil.replaceIp2Domain(url));
        bundle.putString(ConstantValue.WEBVIEW_TYPE, type);
        bundle.putInt(WebViewActivity.SCREEN_ORITATION, ScreenOrientation);
        bundle.putInt(ConstantValue.GAME_APIID, apiId);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        MediaplayerUtil.newInstance().releaseWebView();
    }

    /**
     * 打开客服弹窗 或调用浏览器
     */
    public static void startServiceDialog(FragmentManager fragmentManager) {
        String url = (String) SPTool.get(BoxApplication.getContext(), ConstantValue.KEY_CUSTOMER_SERVICE, "");
        boolean isInlay = (Boolean) SPTool.get(BoxApplication.getContext(), ConstantValue.KEY_CUSTOMER_SERVICE_ISINLAY, false);

        if (TextUtils.isEmpty(url)) {
            SingleToast.showMsg("暂无客服地址");
            return;
        }
        if (isInlay) {
            WebViewDialogFragment.newInstance(url, TYPE_SERVCIE).show(fragmentManager, null);
        } else {
            //去浏览器
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            mContext.startActivity(intent);
        }
    }


    /**
     * 登出
     */
    public static void logout() {
        DataCenter.getInstance().setLogin(false);
        DataCenter.getInstance().setCookie("");
        DataCenter.getInstance().setUserName("");
        DataCenter.getInstance().setPassword("");
        SPTool.remove(BoxApplication.getContext(), ConstantValue.KEY_PASSWORD_AUTO_LOGIN);
        RxBus.get().post(ConstantValue.EVENT_TYPE_LOGOUT, "logout");
        ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
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

    /**
     * 开启维护界面
     */
    public static void startMaintenanceActivity(int code) {
        Intent intent = new Intent(mContext, MaintenanceActivity.class);
        intent.putExtra(MaintenanceActivity.maintenance_code, code);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


}
