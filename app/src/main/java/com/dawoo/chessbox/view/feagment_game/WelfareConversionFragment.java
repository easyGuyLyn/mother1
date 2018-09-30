package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.math.BigDemicalUtil;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.ApiBean;
import com.dawoo.chessbox.bean.ConversionInfoBean;
import com.dawoo.chessbox.bean.QuotaConversionBean;
import com.dawoo.chessbox.bean.UserAssert;
import com.dawoo.chessbox.mvp.presenter.QuotaConversionPresenter;
import com.dawoo.chessbox.mvp.view.IQuotaConversionView;
import com.dawoo.chessbox.net.HttpResult;
import com.dawoo.chessbox.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 福利转换
 */
public class WelfareConversionFragment extends BaseFragment implements IQuotaConversionView {
    @BindView(R.id.bt_one_key_back)
    TextView mBtOneKeyBack;
    @BindView(R.id.bt_one_key_fresh)
    TextView mBtOneKeyFresh;
    @BindView(R.id.tv_transfer_pending_amount)
    TextView mTvTransferPendingAmount;
    @BindView(R.id.tv_out_account)
    TextView mTvOutAccount;
    @BindView(R.id.tv_in_account)
    TextView mTvInAccount;
    @BindView(R.id.ed_input_money)
    EditText mEdInputMoney;
    @BindView(R.id.sure_submit)
    Button mSureSubmit;
    @BindView(R.id.fresh_quota)
    TextView mFreshQuota;
    @BindView(R.id.ll_conversion)
    LinearLayout mLlConversion;
    @BindView(R.id.ryl_game_quota)
    RecyclerView mRylGameQuota;
    Unbinder unbinder;

    boolean isAutoPay;
    private GameQuotaAdapter mGameQuotaAdapter;
    private QuotaConversionPresenter mConversionPresenter;
    private List<ConversionInfoBean.SelectBean> selects = new ArrayList<>();
    private List<String> games = new ArrayList<>();
    private String token;
    private boolean isOutAccount = true;
    private int apiPosition;
    private String mOutAccountName = "";
    private String mOutAccountValue = "";
    private String mInAccountName = "";
    private String mInAccountValue = "";
    private List<UserAssert.ApisBean> mApis;
    private ConversionInfoBean mConversionInfoBean;
    private boolean isFirst = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qt_welfare_conversion_dialogfragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @Override
    protected void loadData() {

    }

    private void initData(){
        mConversionPresenter = new QuotaConversionPresenter(getContext(), this);
        mConversionPresenter.getNoAutoTransferInfo();
        mGameQuotaAdapter = new GameQuotaAdapter(R.layout.qt_item_welfare_fragment);
        mRylGameQuota.setLayoutManager(new LinearLayoutManager(getContext()));
        mRylGameQuota.setNestedScrollingEnabled(false);
        mRylGameQuota.setAdapter(mGameQuotaAdapter);
        mGameQuotaAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                apiPosition = position;
                switch (view.getId()) {
                    case R.id.iv_quota_fresh:
                        mConversionPresenter.refreshApi(mApis.get(position).getApiId());
                        break;
                    case R.id.tv_quota_back:
                        Log.e("apiId", mApis.get(position).getApiId() + "tv_quota_back");
                        mConversionPresenter.recovery(mApis.get(position).getApiId());
                        break;
                }

            }
        });
    }


    @OnClick({R.id.bt_one_key_back, R.id.bt_one_key_fresh, R.id.tv_out_account, R.id.tv_in_account, R.id.sure_submit, R.id.fresh_quota})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_one_key_back:
                //mConversionPresenter.recovery(null);
                mConversionPresenter.refreshAPIs();
                break;
            case R.id.bt_one_key_fresh:
                mConversionPresenter.refreshAPIs();
                break;
            case R.id.tv_out_account:
                isOutAccount = true;
                mConversionPresenter.initSelectGameDialog(games);
                break;
            case R.id.tv_in_account:
                isOutAccount = false;
                mConversionPresenter.initSelectGameDialog(games);
                break;
            case R.id.sure_submit:
                submitConversion();
                break;
            case R.id.fresh_quota:
                mConversionPresenter.refreshAPIs();
                break;
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onNoAutoTransferInfo(ConversionInfoBean conversionInfoBean) {
        if (conversionInfoBean != null) {
            mConversionInfoBean = conversionInfoBean;
            token = mConversionInfoBean.getToken();
            isAutoPay = conversionInfoBean.isAutoPay();
            mConversionPresenter.refreshAPIs();
        }
    }

    @Override
    public void onRefreshApis(UserAssert userAssert) {
        if (userAssert != null) {
            mApis = userAssert.getApis();
            mGameQuotaAdapter.setNewData(mApis);
            if (isFirst) {
                if (isAutoPay) {
                    mBtOneKeyBack.setVisibility(View.VISIBLE);
                    mBtOneKeyFresh.setVisibility(View.GONE);
                } else {
                    selects = mConversionInfoBean.getSelect();
                    mTvTransferPendingAmount.setText(mConversionInfoBean.getCurrency() + BigDemicalUtil.moneyFormat(mConversionInfoBean.getTransferPendingAmount()));
                    mOutAccountName = selects.get(0).getText();
                    mOutAccountValue = selects.get(0).getValue();
                    mTvOutAccount.setText(mOutAccountName);
                    mLlConversion.setVisibility(View.VISIBLE);
                    games.clear();
                    for (ConversionInfoBean.SelectBean selectBean : selects) {
                        games.add(selectBean.getText());
                    }
                }
            }
            isFirst = false;
        }

    }

    @Override
    public void onTransfersMoney(QuotaConversionBean quotaConversionBean) {
        if (quotaConversionBean != null && quotaConversionBean.getData() != null) {
            QuotaConversionBean.DataBean dataBean = quotaConversionBean.getData();
            token = dataBean.getToken();
            if (dataBean.isState()) {
                if (dataBean.getResult() == 0) {
                    mConversionPresenter.refreshAPIs();
                    ToastUtil.showResShort(getContext(), R.string.conversion_success);
                    return;
                } else if (dataBean.getResult() == 1) {
                    ToastUtil.showResShort(getContext(), R.string.conversion_not_success);
                    return;
                } else if (!TextUtils.isEmpty(dataBean.getOrderId())) {
                    mConversionPresenter.reconnectTransfer(dataBean.getOrderId());
                    return;
                }
            }
            if (!TextUtils.isEmpty(quotaConversionBean.getMessage()))
                ToastUtil.showToastLong(getContext(), quotaConversionBean.getMessage());

        }
    }

    @Override
    public void onReconnectTransfer(QuotaConversionBean quotaConversionBean) {
        if (quotaConversionBean != null && quotaConversionBean.getData() != null) {
            QuotaConversionBean.DataBean dataBean = quotaConversionBean.getData();
            token = dataBean.getToken();
            if (dataBean.isState()) {
                if (dataBean.getResult() == 0) {
                    mConversionPresenter.refreshAPIs();
                    ToastUtil.showResShort(getContext(), R.string.conversion_success);
                    return;
                } else if (dataBean.getResult() == 1) {
                    ToastUtil.showResShort(getContext(), R.string.conversion_not_success);
                    return;
                }
            }
            if (!TextUtils.isEmpty(quotaConversionBean.getMessage()))
                ToastUtil.showToastLong(getContext(), quotaConversionBean.getMessage());

        }
    }

    @Override
    public void onRefreshApi(ApiBean apiBean) {
        if (apiBean != null) {
            UserAssert.ApisBean bean = mApis.get(apiPosition);
            if (!TextUtils.isEmpty(apiBean.getStatus()))
                bean.setStatus(apiBean.getStatus());
            if (!TextUtils.isEmpty(apiBean.getApiMoney()))
                bean.setBalance(Double.valueOf(apiBean.getApiMoney()));
            mGameQuotaAdapter.notifyItemChanged(apiPosition);
        }
    }

    @Override
    public void selectedGame(String bankName, int index) {
        if (isOutAccount) {
            mOutAccountName = bankName;
            mOutAccountValue = selects.get(index).getValue();
            mTvOutAccount.setText(bankName);
            if (mInAccountName.equals(bankName)) {
                mInAccountName = "";
                mInAccountValue = "";
                mTvInAccount.setText(getString(R.string.please_select));
            }
        } else {
            mInAccountName = bankName;
            mInAccountValue = selects.get(index).getValue();
            mTvInAccount.setText(bankName);
            if (mOutAccountName.equals(bankName)) {
                mOutAccountName = "";
                mOutAccountValue = "";
                mTvOutAccount.setText(getString(R.string.please_select));
            }
        }
    }

    @Override
    public void onRecovery(HttpResult httpResult) {
        if (httpResult != null) {
            ToastUtil.showToastShort(getContext(), httpResult.getMessage());
            mConversionPresenter.refreshAPIs();
        } else {
            ToastUtil.showToastShort(getContext(), "回收失败!");
        }
    }

    private void submitConversion() {
        if (TextUtils.isEmpty(mOutAccountValue)) {
            ToastUtil.showResShort(getContext(), R.string.please_select_out_account);
            return;
        }
        if (TextUtils.isEmpty(mInAccountValue)) {
            ToastUtil.showResShort(getContext(), R.string.please_select_in_account);
            return;
        }
        String inputMoney = mEdInputMoney.getText().toString().trim();
        if (TextUtils.isEmpty(inputMoney)) {
            ToastUtil.showResShort(getContext(), R.string.please_input_money);
            return;
        }
        mConversionPresenter.transfersMoney(token, mOutAccountValue, mInAccountValue, inputMoney);
    }

    class GameQuotaAdapter extends BaseQuickAdapter {

        public GameQuotaAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            if (isAutoPay)
                helper.setVisible(R.id.iv_quota_fresh, false);
            else helper.setVisible(R.id.tv_quota_back, false);
            UserAssert.ApisBean apisBean = (UserAssert.ApisBean) item;
            helper.setText(R.id.tv_game_name, apisBean.getApiName());
            helper.setText(R.id.tv_quota, BigDemicalUtil.moneyFormat(apisBean.getBalance()));
            helper.addOnClickListener(R.id.iv_quota_fresh);
            helper.addOnClickListener(R.id.tv_quota_back);
        }
    }
}
