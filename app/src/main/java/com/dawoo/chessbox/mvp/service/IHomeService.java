package com.dawoo.chessbox.mvp.service;


import android.support.annotation.NonNull;

import com.dawoo.chessbox.bean.AuditBean;
import com.dawoo.chessbox.bean.Banner;
import com.dawoo.chessbox.bean.FAB;
import com.dawoo.chessbox.bean.GetPacket;
import com.dawoo.chessbox.bean.HongbaoCount;
import com.dawoo.chessbox.bean.Notice;
import com.dawoo.chessbox.bean.QRCodeBean;
import com.dawoo.chessbox.bean.RefreshhApis;
import com.dawoo.chessbox.bean.SiteApiBean;
import com.dawoo.chessbox.bean.SiteApiRelation;
import com.dawoo.chessbox.bean.UrlBean;
import com.dawoo.chessbox.bean.UserAccount;
import com.dawoo.chessbox.net.HttpResult;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * 首页相关的接口
 * Created by benson on 17-12-21.
 */

public interface IHomeService {

    /**
     * 获取banner
     *
     * @return
     */
    @POST("mobile-api/origin/getCarouse.html")
    Observable<HttpResult<Banner>> getBanner();

    /**
     * 获取公告
     *
     * @return
     */
    @POST("mobile-api/origin/getAnnouncement.html")
    Observable<HttpResult<Notice>> getNotice();

    /**
     * 游戏名称及对应游戏种类
     *
     * @return
     */
    @POST("mobile-api/chess/mainIndex.html")
    Observable<HttpResult<SiteApiBean>> getSiteApiRelation();


    /**
     * 浮动图
     *
     * @return
     */
    @POST("mobile-api/origin/getFloat.html")
    Observable<HttpResult<FAB>> getFAB();

    /**
     * 浮动图抢红包次数
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/activityOrigin/countDrawTimes.html")
    Observable<HttpResult<HongbaoCount>> countDrawTimes(@Field("activityMessageId") String activityMessageId);

    /**
     * 打开红包
     *
     * @param activityMessageId
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/activityOrigin/getPacket.html")
    Observable<HttpResult<GetPacket>> getPacket(@NonNull @Field("activityMessageId") String activityMessageId, @NonNull @Field("gb.token") String token);

    /**
     * 用户信息
     *
     * @return
     */
    @POST("mobile-api/userInfoOrigin/getUserInfo.html")
    Observable<HttpResult<UserAccount>> getUserInfo();


    /**
     * 一键回收
     *
     * @return
     */
    @POST("mobile-api/mineOrigin/recovery.html")
    Observable<HttpResult> recovery();

    /**
     * 一键刷新
     *
     * @return
     */
    @POST("mobile-api/userInfoOrigin/refresh.html")
    Observable<HttpResult<RefreshhApis>> refresh();

    /**
     * 获取时区
     */
    @POST("mobile-api/origin/getTimeZone.html")
    Observable<HttpResult<String>> getTimeZone();

    /**
     * 防掉线
     */
    @POST("mobile-api/mineOrigin/alwaysRequest.html")
    Observable<HttpResult<String>> alwaysRequest();

    /**
     * 获取link
     */
    @GET
    Observable<HttpResult<UrlBean>> getAPILink(@Url String url);

    /**
     * 查看稽核
     */
    @POST("mobile-api/withdrawOrigin/getAuditLog.html")
    Observable<AuditBean> getAudit();

    /**
     * 查看稽核
     */
    @POST("mobile-api/chess/getShareQRCode.html")
    Observable<HttpResult<QRCodeBean>> getShareQRCode();


}
