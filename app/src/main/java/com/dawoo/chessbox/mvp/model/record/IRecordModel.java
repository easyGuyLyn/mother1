package com.dawoo.chessbox.mvp.model.record;

import com.hwangjr.rxbus.annotation.Subscribe;

import rx.Subscriber;
import rx.Subscription;

/**
 * 记录model
 * Created by benson on 18-1-07.
 */

public interface IRecordModel {
    Subscription getNoteRecord(Subscriber subscriber,
                               String beginBetTime,
                               String endBetTime,
                               int pageSize,
                               int pageNumber,boolean isShowStatistics);


    Subscription getCapitalRecord(Subscriber subscriber, String beginBetTime, String endBetTime, String transactionType, int pageNumber, int pageSize);

    Subscription getCapitalRecordType(Subscriber subscriber);

    Subscription getCapitalRecordDetail(Subscriber subscriber, int id);

    Subscription getBettingDetail(Subscriber subscriber,String id);

    Subscription getMyPromo(Subscriber subscriber, int pageNumber, int pageSize);


    Subscription getActivitysType(Subscriber subscriber);

    Subscription getActivityTypeList(Subscriber subscriber, int pageNumber, int pageSize, String activityClassifyKey);

    Subscription getFavourableList(Subscriber subscriber);

    Subscription getFavourableDetial(Subscriber subscriber,String id,int position);

    Subscription getFavourableReslut(Subscriber subscriber,String searchId,String searchCode);
}
