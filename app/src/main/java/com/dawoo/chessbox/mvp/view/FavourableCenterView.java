package com.dawoo.chessbox.mvp.view;

/**
 * 优惠活动　view
 */
public interface FavourableCenterView extends IBaseView {

    void onFavourableVRcy(Object o);//加载spinner的数据
    void onFavourableDetail(Object o,int position);//优惠详情
    void onFavourableStatus(Object o);//申请结果
}
