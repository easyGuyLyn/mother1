package com.dawoo.chessbox.mvp.service;


import com.dawoo.chessbox.bean.AboutBean;
import com.dawoo.chessbox.bean.ActivityType;
import com.dawoo.chessbox.bean.ActivityTypeList;
import com.dawoo.chessbox.bean.CommentNextProblemBean;
import com.dawoo.chessbox.bean.FavourableCenterBean;
import com.dawoo.chessbox.bean.FavourableDetailBean;
import com.dawoo.chessbox.bean.FavourableReslutBean;
import com.dawoo.chessbox.bean.GradientTemp;
import com.dawoo.chessbox.bean.HelpDetailsBean;
import com.dawoo.chessbox.bean.MineLink;
import com.dawoo.chessbox.bean.MineTeamsBean;
import com.dawoo.chessbox.bean.MyPromo;
import com.dawoo.chessbox.bean.ProblemBean;
import com.dawoo.chessbox.bean.UserPlayerRecommend;
import com.dawoo.chessbox.net.HttpResult;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 获取我的页面相关接口
 * Created by benson on 18-01-03.
 */

public interface IMineService {

    /**
     * 获取我的
     *
     * @return
     */
    @POST("mobile-api/mineOrigin/getLink.html")
    Observable<HttpResult<MineLink>> getLink();

    /**
     * 分享
     *
     * @return
     */
    @POST("mobile-api/mineOrigin/getUserPlayerRecommend.html")
    Observable<HttpResult<UserPlayerRecommend>> getUserPlayerRecommend();


    /**
     * 获取分享的内容列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/getPlayerRecommendRecord.html")
    Observable<HttpResult<GradientTemp>> getPlayerRecommendRecord(
            @Field("search.startTime") String startTime,
            @Field("search.endTime") String endTime,
            @Field("paging.pageNumber") Integer pageNumber,
            @Field("paging.pageSize") Integer pageSize);


    /**
     * 关于我们
     */

    @POST("/mobile-api/origin/about.html")
    Observable<HttpResult<AboutBean>> about();

    /**
     * 注册条款
     */
    @POST("/mobile-api/origin/terms.html")
    Observable<HttpResult<MineTeamsBean>> terms();

    /**
     * 常见问题一级分类
     */
    @POST("/mobile-api/origin/helpFirstType.html")
    Observable<HttpResult<List<ProblemBean>>> helpFirstType();

    /**
     * 常见问题二级分类
     * /mobile-api/origin/secondType.html
     */
    @FormUrlEncoded
    @POST("/mobile-api/origin/secondType.html")
    Observable<HttpResult<CommentNextProblemBean>> secondType(@Field("searchId") String searchId);

    /**
     * 常见问题详情
     */
    @FormUrlEncoded
    @POST("/mobile-api/origin/helpDetail.html")
    Observable<HttpResult<HelpDetailsBean>> helpDetail(@Field("searchId") String searchId);

    /**
     * 获取优惠分类和列表
     *
     * @return
     */
    @POST("mobile-api/discountsOrigin/getActivityType.html")
    Observable<HttpResult<List<ActivityType>>> getActivityType();

    /**
     * 获取优惠活动列表
     *
     * @param activityClassifyKey
     * @return
     */
    @FormUrlEncoded
    @POST("mobile-api/discountsOrigin/getActivityTypeList.html")
    Observable<HttpResult<ActivityTypeList>> getActivityTypeList(
            @Field("search.activityClassifyKey") String activityClassifyKey);

    /**
     * @param pageNumber
     * @param pageSize
     * @return port/mobile-api/mineOrigin/getMyPromo.html
     */
    @FormUrlEncoded
    @POST("mobile-api/mineOrigin/getMyPromo.html")
    Observable<HttpResult<MyPromo>> getMyPromo(@Field("paging.pageNumber") int pageNumber, @Field("paging.pageSize") int pageSize);

    /**
     * 优惠活动　原生版本
     */
    @POST("mobile-api/chessActivity/getActivityTypes.html")
    Observable<FavourableCenterBean> getFavourableList();

    /**
     * 获取优惠活动详情
     */
    @FormUrlEncoded
    @POST("mobile-api/chessActivity/getActivityById.html")
    Observable<FavourableDetailBean>getFavourableDetial(@Field("searchId")String id);

    @FormUrlEncoded
    @POST("mobile-api/chessActivity/toApplyActivity.html")
    Observable<FavourableReslutBean>getFavourableReslut(@Field("searchId")String searchId,@Field("search.code")String searchCode);

}
