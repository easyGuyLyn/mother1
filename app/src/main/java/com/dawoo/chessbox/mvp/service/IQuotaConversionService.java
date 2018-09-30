package com.dawoo.chessbox.mvp.service;

import com.dawoo.chessbox.bean.ApiBean;
import com.dawoo.chessbox.bean.ConversionInfoBean;
import com.dawoo.chessbox.bean.NoteRecord;
import com.dawoo.chessbox.bean.QuotaConversionBean;
import com.dawoo.chessbox.bean.RefreshhApis;
import com.dawoo.chessbox.net.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by b on 18-3-26.
 */

public interface IQuotaConversionService {

    /**
     * 获取非免转额度转换初始化信息
     *
     * @return
     */

    @GET("mobile-api/userInfoOrigin/getNoAutoTransferInfo.html")
    Observable<HttpResult<ConversionInfoBean>> getNoAutoTransferInfo();



    /**
     * 非免转额度转换提交：
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/userInfoOrigin/transfersMoney.html")
    Observable<QuotaConversionBean> transfersMoney(
            @Field("gb.token") String token,
            @Field("transferOut") String transferOut,
            @Field("transferInto") String transferInto,
            @Field("result.transferAmount") String transferAmount
    );

    /**
     * 非免转额度转换异常再次请求：
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/userInfoOrigin/reconnectTransfer.html")
    Observable<QuotaConversionBean> reconnectTransfer(
            @Field("search.transactionNo") String transactionNo
    );



    /**
     * 非免转刷新单个api：
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/userInfoOrigin/refreshApi.html")
    Observable<HttpResult<ApiBean>> refreshApi(
            @Field("search.apiId") String apiId
    );

    /**
     * 一键回收
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/recovery.html")
    Observable<HttpResult> recovery(@Field("search.apiId") String aipId);
}
