package com.dawoo.chessbox.mvp.presenter;

import android.content.Context;

import com.dawoo.chessbox.mvp.model.home.HomeModel;
import com.dawoo.chessbox.mvp.view.IBaseView;
import com.dawoo.chessbox.mvp.view.IGameFragmentView;
import com.dawoo.chessbox.mvp.view.IHomeFragmentView;
import com.dawoo.chessbox.net.rx.ProgressSubscriber;

import rx.Subscription;


/**
 * 首页presenter
 */

public class GameFragmentPresenter<T extends IBaseView> extends BasePresenter {

    private final Context mContext;
    private T mView;
    private final HomeModel mModel;

    public GameFragmentPresenter(Context context, T mView) {
        super(context, mView);
        mContext = context;
        this.mView = mView;
        mModel = new HomeModel();
    }


    /**
     * 获取游戏列表
     */
    public void getSiteApiRelation() {
        Subscription subscription = mModel.getSiteApiRelation(new ProgressSubscriber(o -> ((IGameFragmentView) mView).onSiteApiRelationResult(o), mContext, true));
        subList.add(subscription);
    }

    /**
     * 获取游戏link
     */
    public void getGameLink(String link, int apiId) {
        Subscription subscription = mModel.getAPILink(new ProgressSubscriber(o ->
                        ((IGameFragmentView) mView).onGameLinkResult(o, apiId), mContext),
                link);
        subList.add(subscription);
    }


    @Override
    public void onDestory() {
        super.onDestory();
    }
}
