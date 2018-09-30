package com.dawoo.chessbox.util;

import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.WebSocketConnectBean;
import com.hwangjr.rxbus.RxBus;

public class PushControlUtils {

    /**
     * 开始长连接
     */
    public static void connect() {
        RxBus.get().post(ConstantValue.EVENT_CONNECT_WEB_SOCKET, new WebSocketConnectBean(
                DataCenter.getInstance().getIp() + ConstantValue.WEB_SOCKET_URL
                , DataCenter.getInstance().getCookie()
                , DataCenter.getInstance().getDomain()));
    }

    /**
     * 断开长连接
     */
    public static void disConnect() {
        RxBus.get().post(ConstantValue.EVENT_DISCONNECT_WEB_SOCKET, "");
    }

}
