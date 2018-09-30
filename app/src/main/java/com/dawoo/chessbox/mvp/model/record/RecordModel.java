package com.dawoo.chessbox.mvp.model.record;

import com.dawoo.chessbox.bean.ActivityType;
import com.dawoo.chessbox.bean.ActivityTypeList;
import com.dawoo.chessbox.bean.BettingDetail;
import com.dawoo.chessbox.bean.CapitalRecord;
import com.dawoo.chessbox.bean.CapitalRecordDetail;
import com.dawoo.chessbox.bean.CapitalRecordType;
import com.dawoo.chessbox.bean.FavourableCenterBean;
import com.dawoo.chessbox.bean.FavourableDetailBean;
import com.dawoo.chessbox.bean.FavourableReslutBean;
import com.dawoo.chessbox.bean.MyPromo;
import com.dawoo.chessbox.bean.NoteRecord;
import com.dawoo.chessbox.mvp.model.BaseModel;
import com.dawoo.chessbox.mvp.service.IMineService;
import com.dawoo.chessbox.mvp.service.IRecordService;
import com.dawoo.chessbox.mvp.view.FavourableCenterView;
import com.dawoo.chessbox.net.RetrofitHelper;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * 记录相关
 * Created by benson on 18-1-07.
 */

public class RecordModel extends BaseModel implements IRecordModel {
    public Subscription getNoteRecord(Subscriber subscriber,
                                      String beginBetTime,
                                      String endBetTime,
                                      int pageSize,
                                      int pageNumber,
                                      boolean isShowStatistics) {
        Observable<NoteRecord> observable = RetrofitHelper.getInstance().getService(IRecordService.class).getNoteRecord(beginBetTime, endBetTime, pageSize, pageNumber,isShowStatistics).map(new HttpResultFunc<NoteRecord>());
        return toSubscribe(observable, subscriber);
    }


    @Override
    public Subscription getBettingDetail(Subscriber subscriber, String id) {
        Observable<BettingDetail> observable = RetrofitHelper.getInstance().getService(IRecordService.class).getBettingDetail(id).map(new HttpResultFunc<BettingDetail>());
        return toSubscribe(observable, subscriber);
    }


    @Override
    public Subscription getCapitalRecord(Subscriber subscriber, String beginBetTime, String endBetTime, String transactionType, int pageNumber, int pageSize) {
        Observable<CapitalRecord> observable = RetrofitHelper.getInstance().getService(IRecordService.class)
                .getCapitalRecord(beginBetTime, endBetTime, transactionType, pageNumber, pageSize)
                .map(new HttpResultFunc<CapitalRecord>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getCapitalRecordType(Subscriber subscriber) {
        Observable<CapitalRecordType> observable = RetrofitHelper.getInstance().getService(IRecordService.class)
                .getCapitalRecordType()
                .map(new HttpResultFunc<CapitalRecordType>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getCapitalRecordDetail(Subscriber subscriber, int id) {
        Observable<CapitalRecordDetail> observable = RetrofitHelper.getInstance().getService(IRecordService.class)
                .getCapitalRecordDetail(id)
                .map(new HttpResultFunc<CapitalRecordDetail>());
        return toSubscribe(observable, subscriber);
    }


    @Override
    public Subscription getMyPromo(Subscriber subscriber, int pageNumber, int pageSize) {
        Observable<MyPromo> observable = RetrofitHelper
                .getInstance().getService(IMineService.class)
                .getMyPromo(pageNumber, pageSize)
                .map(new HttpResultFunc<MyPromo>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getActivitysType(Subscriber subscriber) {
        Observable<List<ActivityType>> observable = RetrofitHelper.getInstance().getService(IMineService.class).getActivityType().map(new HttpResultFunc<List<ActivityType>>());
        return toSubscribe(observable, subscriber);
    }


    /**
     * 根据activityClassifyKey获取数据
     *
     * @param subscriber
     * @param pageNumber
     * @param pageSize
     * @param activityClassifyKey
     * @return
     */
    @Override
    public Subscription getActivityTypeList(Subscriber subscriber, int pageNumber, int pageSize, String activityClassifyKey) {
        Observable<ActivityTypeList> observable = RetrofitHelper
                .getInstance().getService(IMineService.class)
                .getActivityTypeList(activityClassifyKey)
                .map(new HttpResultFunc<ActivityTypeList>());
        return toSubscribe(observable, subscriber);
    }

    /**
     * 获取优惠列表
     */
    @Override
    public Subscription getFavourableList(Subscriber subscriber) {
        Observable<FavourableCenterBean> observable = RetrofitHelper.getInstance().getService(IMineService.class).getFavourableList();
        return toSubscribe(observable,subscriber);
    }

    @Override
    public Subscription getFavourableDetial(Subscriber subscriber, String id,int position) {
        Observable<FavourableDetailBean> observable = RetrofitHelper.getInstance().getService(IMineService.class).getFavourableDetial(id);
        return toSubscribe(observable,subscriber);
    }

    @Override
    public Subscription getFavourableReslut(Subscriber subscriber, String searchId,String searchCode) {
        Observable<FavourableReslutBean> observable = RetrofitHelper.getInstance().getService(IMineService.class).getFavourableReslut(searchId,searchCode);
        return toSubscribe(observable,subscriber);
    }


}
