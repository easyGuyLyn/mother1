package com.dawoo.chessbox.mvp.model.mine;

import com.dawoo.chessbox.bean.AboutBean;
import com.dawoo.chessbox.bean.CommentNextProblemBean;
import com.dawoo.chessbox.bean.GradientTemp;
import com.dawoo.chessbox.bean.HelpDetailsBean;
import com.dawoo.chessbox.bean.MineLink;
import com.dawoo.chessbox.bean.MineTeamsBean;
import com.dawoo.chessbox.bean.ProblemBean;
import com.dawoo.chessbox.bean.UserPlayerRecommend;
import com.dawoo.chessbox.mvp.model.BaseModel;
import com.dawoo.chessbox.mvp.service.IMineService;
import com.dawoo.chessbox.net.RetrofitHelper;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by benson on 18-1-4.
 */

public class MineModel extends BaseModel implements IMineModel {
    @Override
    public Subscription getLink(Subscriber subscriber) {
        Observable<MineLink> observable = RetrofitHelper.getInstance().getService(IMineService.class).getLink().map(new HttpResultFunc<MineLink>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getUserPlayerRecommend(Subscriber subscriber) {
        Observable<UserPlayerRecommend> observable = RetrofitHelper.getInstance().getService(IMineService.class).getUserPlayerRecommend().map(new HttpResultFunc<UserPlayerRecommend>());
        return toSubscribe(observable, subscriber);
    }


    @Override
    public Subscription terms(Subscriber subscriber) {
        Observable<MineTeamsBean> observable = RetrofitHelper.getInstance().getService(IMineService.class).terms().map(new HttpResultFunc<MineTeamsBean>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription about(Subscriber subscriber) {
        Observable<AboutBean> observable = RetrofitHelper.getInstance().getService(IMineService.class).about().map(new HttpResultFunc<AboutBean>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription helpFirstType(Subscriber subscriber) {
        Observable<List<ProblemBean>> observable = RetrofitHelper.getInstance().getService(IMineService.class).helpFirstType().map(new HttpResultFunc<List<ProblemBean>>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription secondType(Subscriber subscriber, String searchId) {
        Observable<CommentNextProblemBean> observable = RetrofitHelper
                .getInstance().getService(IMineService.class)
                .secondType(searchId)
                .map(new HttpResultFunc<CommentNextProblemBean>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription helpDetail(Subscriber subscriber, String searchId) {
        Observable<HelpDetailsBean> observable = RetrofitHelper
                .getInstance().getService(IMineService.class)
                .helpDetail(searchId)
                .map(new HttpResultFunc<HelpDetailsBean>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription gradientTempArrayList(Subscriber subscriber, String startTime, String endTime, Integer pageNumber, Integer pageSize) {
        Observable<GradientTemp> observable = RetrofitHelper.getInstance().getService(IMineService.class).getPlayerRecommendRecord(startTime, endTime, pageNumber, pageSize).map(new HttpResultFunc<GradientTemp>());
        return toSubscribe(observable, subscriber);
    }
}
