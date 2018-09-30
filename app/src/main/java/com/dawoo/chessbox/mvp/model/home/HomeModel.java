package com.dawoo.chessbox.mvp.model.home;

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
import com.dawoo.chessbox.mvp.model.BaseModel;
import com.dawoo.chessbox.mvp.service.IHomeService;
import com.dawoo.chessbox.net.HttpResult;
import com.dawoo.chessbox.net.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by benson on 17-12-21.
 */

public class HomeModel extends BaseModel implements IHomeModel {


    @Override
    public Subscription getBanner(Subscriber subscriber) {
        Observable<Banner> observable = RetrofitHelper.getInstance().getService(IHomeService.class).getBanner().map(new HttpResultFunc<Banner>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getNotice(Subscriber subscriber) {
        Observable<Notice> observable = RetrofitHelper.getInstance().getService(IHomeService.class).getNotice().map(new HttpResultFunc<Notice>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getSiteApiRelation(Subscriber subscriber) {
        Observable<SiteApiBean> observable = RetrofitHelper.getInstance().getService(IHomeService.class).getSiteApiRelation().map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getFAB(Subscriber subscriber) {
        Observable<FAB> observable = RetrofitHelper.getInstance().getService(IHomeService.class).getFAB().map(new HttpResultFunc<FAB>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription countDrawTimes(Subscriber subscriber, String activityMessageId) {
        Observable<HongbaoCount> observable = RetrofitHelper.getInstance().getService(IHomeService.class).countDrawTimes(activityMessageId).map(new HttpResultFunc<HongbaoCount>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getPacket(Subscriber subscriber, String activityMessageId, String token) {
        Observable<GetPacket> observable = RetrofitHelper.getInstance().getService(IHomeService.class).getPacket(activityMessageId, token).map(new HttpResultFunc<GetPacket>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getAccount(Subscriber subscriber) {
        Observable<UserAccount> observable = RetrofitHelper.getInstance().getService(IHomeService.class).getUserInfo().map(new HttpResultFunc<UserAccount>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription recovery(Subscriber subscriber) {
        Observable<HttpResult> observable = RetrofitHelper.getInstance().getService(IHomeService.class).recovery();
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription refresh(Subscriber subscriber) {
        Observable<RefreshhApis> observable = RetrofitHelper.getInstance().getService(IHomeService.class).refresh().map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getTimeZone(Subscriber subscribe) {
        Observable<String> observable = RetrofitHelper.getInstance().getService(IHomeService.class).getTimeZone().map(new HttpResultFunc<>());
        return toSubscribe(observable, subscribe);
    }


    @Override
    public Subscription alwaysRequest(Subscriber subscribe) {
        Observable<String> observable = RetrofitHelper.getInstance().getService(IHomeService.class).alwaysRequest().map(new HttpResultFunc<>());
        return toSubscribe(observable, subscribe);
    }

    @Override
    public Subscription getAPILink(Subscriber subscribe, String link) {
        Observable<UrlBean> observable = RetrofitHelper.getInstance().getService(IHomeService.class).getAPILink(link).map(new HttpResultFunc<>());
        return toSubscribe(observable, subscribe);
    }

    @Override
    public Subscription getShareQRCode(Subscriber subscribe) {
        Observable<QRCodeBean> observable = RetrofitHelper.getInstance().getService(IHomeService.class).getShareQRCode().map(new HttpResultFunc<>());
        return toSubscribe(observable, subscribe);
    }
}
