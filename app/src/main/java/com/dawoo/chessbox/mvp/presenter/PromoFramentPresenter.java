package com.dawoo.chessbox.mvp.presenter;

import android.content.Context;

import com.dawoo.chessbox.mvp.model.record.RecordModel;
import com.dawoo.chessbox.mvp.view.FavourableCenterView;
import com.dawoo.chessbox.mvp.view.IBaseView;
import com.dawoo.chessbox.mvp.view.IPromoFragmentView;
import com.dawoo.chessbox.net.rx.ProgressSubscriber;

import rx.Subscription;


/**
 * 优惠presenter
 */

public class PromoFramentPresenter<T extends IBaseView> extends BasePresenter {

    private final Context mContext;
    private T mView;
    private final RecordModel mModel;

    public PromoFramentPresenter(Context context, T mView) {
        super(context, mView);
        mContext = context;
        this.mView = mView;
        mModel = new RecordModel();
    }

    /**
     * 获取优惠类别和列表
     */
    public void getActivityType() {
        Subscription subscription = mModel.getActivitysType(new ProgressSubscriber(o -> ((IPromoFragmentView) mView).onPromoResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 获取下面列表的数据
     */
    public void getLoadMoreListDate(int pageNumber, int pageSize, String activityClassifyKey) {
        Subscription subscription = mModel.getActivityTypeList(new ProgressSubscriber(o ->
                        ((IPromoFragmentView) mView).loadMoreListDate(o), mContext),
                pageNumber,
                pageSize,
                activityClassifyKey
        );
        subList.add(subscription);
    }

    /**
     * 获取下面列表的数据
     */
    public void getPromoListResult(int pageNumber, int pageSize, String activityClassifyKey) {
        Subscription subscription = mModel.getActivityTypeList(new ProgressSubscriber(o ->
                        ((IPromoFragmentView) mView).onPromoListResult(o), mContext),
                pageNumber,
                pageSize,
                activityClassifyKey
        );
        subList.add(subscription);
    }

    /**
     * 获取优惠活动列表
     */
    public void getFavourableList(){
        Subscription subscription = mModel.getFavourableList(new ProgressSubscriber(o ->
                        ((FavourableCenterView) mView).onFavourableVRcy(o), mContext));
        subList.add(subscription);
    }

    /**
     * 获取优惠活动详情
     */
    public void getFavurableDetail(String id,int position){
        Subscription subscription = mModel.getFavourableDetial(new ProgressSubscriber(o ->
                ((FavourableCenterView) mView).onFavourableDetail(o,position), mContext,false),id,position);
        subList.add(subscription);
    }

    /**
     * 优惠活动申请结果
     */
    public void getFavourableReslut(String searchId,String searchCode){
        Subscription subscription = mModel.getFavourableReslut(new ProgressSubscriber(o ->
                ((FavourableCenterView) mView).onFavourableStatus(o), mContext,true),searchId,searchCode);
        subList.add(subscription);
    }

    @Override
    public void onDestory() {
        super.onDestory();
    }


}
