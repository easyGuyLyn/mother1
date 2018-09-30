package com.dawoo.chessbox.mvp.model.conversion;

import com.dawoo.chessbox.bean.ApiBean;
import com.dawoo.chessbox.bean.ConversionInfoBean;
import com.dawoo.chessbox.bean.QuotaConversionBean;
import com.dawoo.chessbox.mvp.model.BaseModel;
import com.dawoo.chessbox.mvp.service.IQuotaConversionService;
import com.dawoo.chessbox.net.HttpResult;
import com.dawoo.chessbox.net.RetrofitHelper;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by b on 18-3-26.
 */

public class QuotaConversionModel extends BaseModel implements IQuotaConversionModel {
    @Override
    public Subscription getNoAutoTransferInfo(Subscriber subscriber) {
        Observable<ConversionInfoBean> observable = RetrofitHelper
                .getInstance().getService(IQuotaConversionService.class)
                .getNoAutoTransferInfo()
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription transfersMoney(Subscriber subscriber, String token, String transferOut, String transferInto, String transferAmount) {
        Observable<QuotaConversionBean> observable = RetrofitHelper
                .getInstance().getService(IQuotaConversionService.class)
                .transfersMoney(token, transferOut, transferInto, transferAmount);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription reconnectTransfer(Subscriber subscriber, String transactionNo) {
        Observable<QuotaConversionBean> observable = RetrofitHelper
                .getInstance().getService(IQuotaConversionService.class)
                .reconnectTransfer(transactionNo);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription refreshApi(Subscriber subscriber, String apiId) {
        Observable<ApiBean> observable = RetrofitHelper
                .getInstance().getService(IQuotaConversionService.class)
                .refreshApi(apiId)
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription recovery(Subscriber subscriber, String apiId) {
        Observable<HttpResult> observable = RetrofitHelper
                .getInstance().getService(IQuotaConversionService.class)
                .recovery(apiId);
        return toSubscribe(observable, subscriber);
    }
}
