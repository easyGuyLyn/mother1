package com.dawoo.chessbox.mvp.model.account;

import com.dawoo.chessbox.bean.AuditBean;
import com.dawoo.chessbox.bean.BankCards;
import com.dawoo.chessbox.bean.Bitcoin;
import com.dawoo.chessbox.bean.RefreshhApis;
import com.dawoo.chessbox.bean.UserAccount;
import com.dawoo.chessbox.bean.UserAssert;
import com.dawoo.chessbox.bean.VideoGame;
import com.dawoo.chessbox.bean.WithdrawFee;
import com.dawoo.chessbox.bean.WithdrawResult;
import com.dawoo.chessbox.bean.WithdrawSubmitResult;
import com.dawoo.chessbox.mvp.model.BaseModel;
import com.dawoo.chessbox.mvp.service.IAccountService;
import com.dawoo.chessbox.mvp.service.IGameService;
import com.dawoo.chessbox.mvp.service.IHomeService;
import com.dawoo.chessbox.mvp.service.IWithdrawService;
import com.dawoo.chessbox.net.HttpResult;
import com.dawoo.chessbox.net.RetrofitHelper;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by benson on 18-2-25.
 */

public class AccountModel extends BaseModel implements IAccountModel {
    @Override
    public Subscription getUserAssert(Subscriber subscriber) {
        Observable<UserAssert> observable = RetrofitHelper
                .getInstance().getService(IAccountService.class)
                .getUserAssert()
                .map(new HttpResultFunc<>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getWithDraw(Subscriber subscriber) {
        Observable<WithdrawResult> observable = RetrofitHelper.getInstance().getService(IWithdrawService.class).getWithDraw();
        return toSubscribe(observable, subscriber);
    }


    @Override
    public Subscription submitWithdraw(Subscriber subscriber, double withdrawAmount, String token, int remittanceWay, String originPwd) {
        Observable<WithdrawSubmitResult> observable = RetrofitHelper.getInstance().getService(IWithdrawService.class).submitWithdraw(withdrawAmount, token, remittanceWay, originPwd);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getCardType(Subscriber subscriber) {
        Observable<BankCards> observable = RetrofitHelper.getInstance().getService(IWithdrawService.class).getCardAndBanksInfo().map(new HttpResultFunc<BankCards>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription submitBankCard(Subscriber subscriber, String bankcardMasterName, String bankName, String bankcardNumber, String bankDeposit) {
        Observable<WithdrawSubmitResult> observable = RetrofitHelper.getInstance().getService(IWithdrawService.class).submitBankCard(bankcardMasterName, bankName, bankcardNumber, bankDeposit);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription submitBtc(Subscriber subscriber, String bankcardNumber) {
        Observable<WithdrawSubmitResult> observable = RetrofitHelper.getInstance().getService(IWithdrawService.class).submitBtc(bankcardNumber);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getBtcInfo(Subscriber subscriber) {
        Observable<Bitcoin> observable = RetrofitHelper.getInstance().getService(IWithdrawService.class).getBtcInfo();
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription checkSafePassword(Subscriber subscriber, String originPwd) {
        Observable<WithdrawSubmitResult> observable = RetrofitHelper.getInstance().getService(IWithdrawService.class).checkSafePassword(originPwd);
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription withdrawFee(Subscriber subscriber, double withdrawFee) {
        Observable<WithdrawFee> observable = RetrofitHelper.getInstance().getService(IWithdrawService.class).withdrawFee(withdrawFee);
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
    public Subscription getAccount(Subscriber subscriber) {
        Observable<UserAccount> observable = RetrofitHelper.getInstance().getService(IHomeService.class).getUserInfo().map(new HttpResultFunc<UserAccount>());
        return toSubscribe(observable, subscriber);
    }

    @Override
    public Subscription getAudit(Subscriber subscriber) {
        Observable<AuditBean> observable = RetrofitHelper.getInstance().getService(IHomeService.class).getAudit();
        return toSubscribe(observable, subscriber);
    }

}
