package com.dawoo.chessbox.mvp.presenter;

import android.content.Context;

import com.dawoo.chessbox.bean.UserAccount;
import com.dawoo.chessbox.bean.UserAssert;
import com.dawoo.chessbox.mvp.model.account.AccountModel;
import com.dawoo.chessbox.mvp.model.conversion.QuotaConversionModel;
import com.dawoo.chessbox.mvp.model.home.HomeModel;
import com.dawoo.chessbox.mvp.model.user.UserModel;
import com.dawoo.chessbox.mvp.view.IBaseView;
import com.dawoo.chessbox.mvp.view.IMainActivityView;
import com.dawoo.chessbox.mvp.view.IQRcodeView;
import com.dawoo.chessbox.net.HttpResult;
import com.dawoo.chessbox.net.rx.ProgressSubscriber;

import java.util.List;

import rx.Subscription;


/**
 */

public class MainActivityPresenter<T extends IBaseView> extends BasePresenter {

    private final Context mContext;
    private T mView;
    private final HomeModel mModel;
    private final AccountModel mAccountModel;
    private final UserModel mUserModel;
    private final QuotaConversionModel mQuotaConversionModel;

    public MainActivityPresenter(Context context, T mView) {
        super(context, mView);

        mContext = context;
        this.mView = mView;
        mModel = new HomeModel();
        mAccountModel = new AccountModel();
        mUserModel = new UserModel();
        mQuotaConversionModel = new QuotaConversionModel();
    }


    /**
     * 获取公告
     */
    public void getNotice() {
        Subscription subscription = mModel.getNotice(new ProgressSubscriber(o -> ((IMainActivityView) mView).onNoticeResult(o), mContext, false));
        subList.add(subscription);
    }


    /**
     * 获取账户数据
     */
    public void getAccount() {
        Subscription subscription = mModel.getAccount(new ProgressSubscriber(((IMainActivityView) mView)::onAccountResult, mContext, false));
        subList.add(subscription);
    }

    /**
     * 获取用户资产
     */
    public void getAssert() {
        Subscription subscription = mAccountModel.getUserAssert(new ProgressSubscriber(((IMainActivityView) mView)::onAssertResult, mContext));
        subList.add(subscription);
    }


    /**
     * 获取时区
     */
    public void getTimeZone() {
        Subscription subscription = mModel.getTimeZone(new ProgressSubscriber(o ->
                ((IMainActivityView) mView).getTimeZone(o), mContext, false));
        subList.add(subscription);
    }

    /**
     * 防止掉线
     */
    public void alwaysRequest() {
        Subscription subscription = mModel.alwaysRequest(new ProgressSubscriber(o ->
                ((IMainActivityView) mView).onAlwaysRequestResult(o), mContext, false));
        subList.add(subscription);
    }

    /**
     * 获取客服地址
     */
    public void getCustomerService() {
        Subscription subscription = mUserModel.getCustomerService(new ProgressSubscriber(o ->
                ((IMainActivityView) mView).onCustomerService(o), mContext));
        subList.add(subscription);
    }

    /**
     * 一键回收 单个api
     */
    public void recovery(String apiId) {
        Subscription subscription = mQuotaConversionModel.recovery(new ProgressSubscriber(o ->
                ((IMainActivityView) mView).onRecovery((HttpResult) o), mContext, false), apiId);
        subList.add(subscription);
    }


    /**
     * 一键回收
     */
    public void recovery() {
        Subscription subscription = mModel.recovery(new ProgressSubscriber(o ->
                ((IMainActivityView) mView).onRecoveryResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 二维码
     */
    public void getShareQRCode() {
        Subscription subscription = mModel.getShareQRCode(new ProgressSubscriber(o -> ((IQRcodeView) mView).onShareQRCodeResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 一键刷新
     */
    public void refreshAPI() {
        Subscription subscription = mModel.refresh(new ProgressSubscriber(o ->
                ((IMainActivityView) mView).onRefreshResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * 替换不同类型数据
     * 资产数据填充到账户类型数据
     *
     * @param o
     * @return
     */
    public Object rePlaceData(Object o, UserAccount account) {
        if (o != null && o instanceof UserAssert) {
            UserAssert userAssert = (UserAssert) o;
            UserAccount.UserBean userBean = account.getUser();
            userBean.setUsername(userAssert.getUsername());
            userBean.setCurrency(userAssert.getCurrSign());
            userBean.setTotalAssets(userAssert.getAssets());
            userBean.setWalletBalance(userAssert.getBalance());

            // 替换api
            List<UserAccount.UserBean.ApisBean> apis = userBean.getApis();
            List<UserAssert.ApisBean> apis2 = userAssert.getApis();
            UserAccount.UserBean.ApisBean api;
            UserAssert.ApisBean api2;
            for (int i = 0; i < apis.size(); i++) {
                api = new UserAccount.UserBean.ApisBean();
                api2 = apis2.get(i);
                api.setApiId(api2.getApiId());
                api.setApiName(api2.getApiName());
                api.setBalance(api2.getBalance());
                api.setStatus(api2.getStatus());
                apis.set(i, api);
            }
            userBean.setApis(apis);
            account.setUser(userBean);
        }

        return account;
    }


    @Override
    public void onDestory() {
        super.onDestory();
    }
}
