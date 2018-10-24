package com.dawoo.chessbox.u;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dawoo.chessbox.ConstantValue;
import com.dawoo.ipc.control.IpcWebViewActivity;

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

        Intent intent = new Intent(mContext, IpcWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ConstantValue.WEBVIEW_URL, url);
        bundle.putString(ConstantValue.WEBVIEW_TYPE, type);
        bundle.putInt(IpcWebViewActivity.SCREEN_ORITATION, ScreenOrientation);
       // bundle.putInt(ConstantValue.GAME_APIID, apiId);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    public static void startH5(String url) {
        ActivityUtil.startGameWebView(url, "", WEBVIEW_TYPE_THIRD_ORDINARY, 3, 0);
    }


}
