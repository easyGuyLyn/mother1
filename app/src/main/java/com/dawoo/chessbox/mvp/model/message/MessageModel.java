package com.dawoo.chessbox.mvp.model.message;

import com.dawoo.chessbox.bean.CommonRequestResult;
import com.dawoo.chessbox.bean.GameNotice;
import com.dawoo.chessbox.bean.MessageDetail;
import com.dawoo.chessbox.bean.ResetSecurityPwd;
import com.dawoo.chessbox.bean.SiteMsgType;
import com.dawoo.chessbox.bean.SiteMsgUnReadCount;
import com.dawoo.chessbox.bean.SiteMyMsgDetailList;
import com.dawoo.chessbox.bean.SiteMyNotice;
import com.dawoo.chessbox.bean.SiteSysMsgDetail;
import com.dawoo.chessbox.bean.SiteSysNotice;
import com.dawoo.chessbox.bean.SysNotice;
import com.dawoo.chessbox.mvp.model.BaseModel;
import com.dawoo.chessbox.mvp.service.IMessageService;
import com.dawoo.chessbox.net.RetrofitHelper;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * 消息
 * Created by benson on 18-1-15.
 */

public class MessageModel extends BaseModel implements IMessageModel {
    @Override
    public Subscription getGameNotice(Subscriber subscriber, String startTime, String endTime, int pageNumber, int pageSize, Integer apiId) {
        Observable<GameNotice> observable = RetrofitHelper.getInstance().getService(IMessageService.class).getGameNotice(
                startTime,
                endTime,
                pageNumber,
                pageSize,
                apiId).map(new HttpResultFunc<GameNotice>());

        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getGameNotice(Subscriber subscriber, String startTime, String endTime, int pageNumber, int pageSize) {
        Observable<GameNotice> observable = RetrofitHelper.getInstance().getService(IMessageService.class).getGameNotice(
                startTime,
                endTime,
                pageNumber,
                pageSize).map(new HttpResultFunc<GameNotice>());

        return toSubscribe(observable, subscriber);
    }


    @Override
    public Subscription getGameNotice(Subscriber subscriber, int pageNumber, int pageSize) {
        Observable<GameNotice> observable = RetrofitHelper.getInstance().getService(IMessageService.class).getGameNotice(pageNumber, pageSize).map(new HttpResultFunc<GameNotice>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getSysNotice(Subscriber subscriber, String startTime, String endTime, int pageNumber, int pageSize) {
        Observable<SysNotice> observable = RetrofitHelper.getInstance().getService(IMessageService.class).getSysNotice(
                startTime,
                endTime,
                pageNumber,
                pageSize).map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getSysNotice(Subscriber subscriber, int pageNumber, int pageSize) {
        Observable<SysNotice> observable = RetrofitHelper.getInstance().getService(IMessageService.class).getSysNotice(pageNumber, pageSize).map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getGameNoticeDetail(Subscriber subscriber, String searchId) {
        Observable<MessageDetail> observable = RetrofitHelper.getInstance().getService(IMessageService.class).getGameNoticeDetail(searchId).map(new HttpResultFunc<MessageDetail>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getSysNoticeDetail(Subscriber subscriber, String searchId) {
        Observable<MessageDetail> observable = RetrofitHelper.getInstance().getService(IMessageService.class).getSysNoticeDetail(searchId).map(new HttpResultFunc<MessageDetail>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getSiteSysNotice(Subscriber subscriber, int pageNumber, int pageSize) {
        Observable<SiteSysNotice> observable = RetrofitHelper.getInstance().getService(IMessageService.class)
                .getSiteSysNotice(pageNumber, pageSize)
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription setSiteSysNoticeStatus(Subscriber subscriber, String ids) {
        Observable<CommonRequestResult> observable = RetrofitHelper.getInstance().getService(IMessageService.class)
                .setSiteSysNoticeStatus(ids);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription deleteSiteSysNotice(Subscriber subscriber, String ids) {
        Observable<CommonRequestResult> observable = RetrofitHelper.getInstance().getService(IMessageService.class)
                .deleteSiteSysNotice(ids);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getSiteMyNotice(Subscriber subscriber, int pageNumber, int pageSize) {
        Observable<SiteMyNotice> observable = RetrofitHelper.getInstance().getService(IMessageService.class)
                .getSiteMyNotice(pageNumber, pageSize);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription setSiteMyNoticeStatus(Subscriber subscriber, String ids) {
        Observable<CommonRequestResult> observable = RetrofitHelper.getInstance().getService(IMessageService.class)
                .setSiteMyNoticeStatus(ids);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription deleteSiteMyNotice(Subscriber subscriber, String ids) {
        Observable<CommonRequestResult> observable = RetrofitHelper.getInstance().getService(IMessageService.class)
                .deleteSiteMyNotice(ids);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getSiteMsgType(Subscriber subscriber) {
        Observable<SiteMsgType> observable = RetrofitHelper.getInstance().getService(IMessageService.class)
                .getSiteMsgType()
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }


    @Override
    public Subscription addNoticeSite(Subscriber subscriber, String advisoryType, String advisoryTitle, String advisoryContent) {
        Observable<ResetSecurityPwd> observable = RetrofitHelper.getInstance().getService(IMessageService.class)
                .addNoticeSite(advisoryType, advisoryTitle, advisoryContent);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription addNoticeSite(Subscriber subscriber, String advisoryType, String advisoryTitle, String advisoryContent, String code) {
        Observable<ResetSecurityPwd> observable = RetrofitHelper.getInstance().getService(IMessageService.class)
                .addNoticeSite(advisoryType, advisoryTitle, advisoryContent, code);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getUnReadMsgCount(Subscriber subscriber) {
        Observable<SiteMsgUnReadCount> observable = RetrofitHelper.getInstance().getService(IMessageService.class)
                .getUnReadCount()
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getSiteSysNoticeDetail(Subscriber subscriber, String searchId) {
        Observable<SiteSysMsgDetail> observable = RetrofitHelper.getInstance().getService(IMessageService.class).getSiteSysNoticeDetail(searchId).map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription advisoryMessageDetail(Subscriber subscriber, String id) {
        Observable<SiteMyMsgDetailList> observable = RetrofitHelper.getInstance().getService(IMessageService.class)
                .advisoryMessageDetail(id);
        return toSubscribe(observable, subscriber);
    }
}
