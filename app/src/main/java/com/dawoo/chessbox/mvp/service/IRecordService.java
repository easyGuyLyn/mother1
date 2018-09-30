package com.dawoo.chessbox.mvp.service;


import com.dawoo.chessbox.bean.BettingDetail;
import com.dawoo.chessbox.bean.MineLink;
import android.support.annotation.NonNull;

import com.dawoo.chessbox.bean.CapitalRecord;
import com.dawoo.chessbox.bean.CapitalRecordDetail;
import com.dawoo.chessbox.bean.CapitalRecordType;
import com.dawoo.chessbox.bean.NoteRecord;
import com.dawoo.chessbox.net.HttpResult;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 获取记录相关
 * Created by benson on 18-01-07.
 */

public interface IRecordService {

    /**
     * 投注记录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/getBettingList.html")
    Observable<HttpResult<NoteRecord>> getNoteRecord(
            @Field("search.beginBetTime") String beginBetTime,
            @Field("search.endBetTime") String endBetTime,
            @Field("paging.pageSize") int pageSize,
            @Field("paging.pageNumber") int pageNumber,
            @Field("isShowStatistics") boolean isShowStatistics);

    /**
     * 资金记录
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/getFundRecord.html")
    Observable<HttpResult<CapitalRecord>> getCapitalRecord(@Field("search.beginCreateTime") String beginBetTime,
                                                           @Field("search.endCreateTime") String endBetTime,
                                                           @Field("search.transactionType") String transactionType,
                                                           @Field("paging.pageNumber") int pageNumber,
                                                           @Field("paging.pageSize") int pageSize);

    /**
     * 资金记录类型的获取
     *
     * @return
     */
    @POST("mobile-api/mineOrigin/getTransactionType.html")
    Observable<HttpResult<CapitalRecordType>> getCapitalRecordType();

    /**
     * 资金记录详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/getFundRecordDetails.html")
    Observable<HttpResult<CapitalRecordDetail>> getCapitalRecordDetail(@Field("searchId") @NonNull int id);



    /**
     * 获取投注记录明细
     *
     * */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/getBettingDetails.html")
    Observable<HttpResult<BettingDetail>> getBettingDetail(@Field("id") String id);

}
