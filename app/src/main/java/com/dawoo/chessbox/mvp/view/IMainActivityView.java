package com.dawoo.chessbox.mvp.view;

import com.dawoo.chessbox.net.HttpResult;

/**
 */

public interface IMainActivityView extends IBaseView {

    void onNoticeResult(Object o);//公告

    void onAccountResult(Object o);//获取账户数据

    void onAssertResult(Object o);//账户金额

    void getTimeZone(Object o);//时区

    void onAlwaysRequestResult(Object o);//session

    void onCustomerService(Object o);//客服

    void onRecovery(HttpResult refreshhApis);//回收单个api

    void onRecoveryResult(Object o);//一键回收

    void onRefreshResult(Object o);//一键刷新
}
