package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.AuditBean;
import com.dawoo.chessbox.mvp.presenter.WithdrawPresenter;
import com.dawoo.chessbox.mvp.view.AuditView;
import com.dawoo.chessbox.util.SharePreferenceUtil;
import com.dawoo.chessbox.view.view.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 查看稽核dialog
 */
public class AuditDialogFragment extends BaseDialogFragment implements AuditView {


    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.img_close)
    ImageView imgClose;
    Unbinder unbinder;
    private AuditAdapter mAuditAdapter;
    private List<AuditBean.WithdrawAudit> list = new ArrayList<>();
    WithdrawPresenter withdrawPresenter;
    AuditBean auditBean;

    @Override
    protected int getViewId() {
        return R.layout.qt_profit_audit_dialogfragment;
    }

    @Override
    protected void initViews(View view) {
        intScreenHProportion = 0.93;
        intScreenWProportion = 0.8;
        mAuditAdapter = new AuditAdapter(R.layout.qt_profit_audit_dialogfragment_item, list);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAuditAdapter);
    }

    @Override
    protected void initData() {
        withdrawPresenter = new WithdrawPresenter(getActivity(), this);
        withdrawPresenter.getAudit();
    }

    @Override
    public void getAuditState(Object o) {
        list.clear();
        auditBean = (AuditBean) o;
        if (!"0".equals(auditBean.getCode())) {
            ToastUtil.showToastLong(getActivity(), auditBean.getMessage());
            return;
        }

        AuditBean.Data data = auditBean.getData();
        if (data != null && !"null".equals(data)) {
            if (data.getWithdrawAudit() != null && !"null".equals(data.getWithdrawAudit())) {
                list.addAll(data.getWithdrawAudit());
                mAuditAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.img_close)
    public void onViewClicked() {
        dismiss();
    }

    class AuditAdapter extends BaseQuickAdapter {

        public AuditAdapter(int layoutResId, @Nullable List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            AuditBean.WithdrawAudit withdrawAudit = (AuditBean.WithdrawAudit) item;
            helper.setText(R.id.tv_deposit_money, String.valueOf(item));
            helper.setText(R.id.tv_audit_point, String.valueOf(item));


            //存款时间
            if (!TextUtils.isEmpty(withdrawAudit.getCreateTime() + "")) {
                String mTimeZone = SharePreferenceUtil.getTimeZone(getActivity());
                long time = DateTool.convertTimeInMillisWithTimeZone(withdrawAudit.getCreateTime(), mTimeZone);
                helper.setText(R.id.tv_deposit_time, DateTool.getTimeFromLong(DateTool.FMT_DATE_TIME, time));
            }

            //存款金额
            if (!TextUtils.isEmpty(withdrawAudit.getRechargeAmount())) {
                helper.setText(R.id.tv_deposit_money, auditBean.getData().getCurrencySign() + withdrawAudit.getRechargeAmount());
            }

            //存款稽核
            if (!TextUtils.isEmpty(withdrawAudit.getRechargeAudit()) && !TextUtils.isEmpty(withdrawAudit.getRechargeRemindAudit())) {
                helper.setText(R.id.tv_audit_point, withdrawAudit
                        .getRechargeRemindAudit() + "/" + withdrawAudit.getRechargeAudit());
            }
            //剩余存款稽核
            if (!TextUtils.isEmpty(withdrawAudit.getRechargeRemindAudit())) {

            }
            //行政费用，如果为0显示通过，其他直接展示费用
            if (withdrawAudit.getRechargeFee() != null && !"null".equals(withdrawAudit.getRechargeFee())) {
                if (withdrawAudit.getRechargeFee() == 0) {
                    helper.setText(R.id.tv_administrative_costs, "通过");
                } else {
                    helper.setText(R.id.tv_administrative_costs, auditBean.getData().getCurrencySign() + withdrawAudit.getRechargeFee());
                }
            }
            //优惠金额
            if (!TextUtils.isEmpty(withdrawAudit.getFavorableAmount())) {
                helper.setText(R.id.tv_discount_amont, auditBean.getData().getCurrencySign() + withdrawAudit.getFavorableAmount());
            }

            // //优惠稽核

            if (!TextUtils.isEmpty(withdrawAudit.getFavorableAudit()) && !TextUtils.isEmpty(withdrawAudit.getFavorableRemindAudit())) {
                helper.setText(R.id.tv_discount_audit_point, withdrawAudit.getFavorableRemindAudit() + "/" + withdrawAudit.getFavorableAudit());
            }

            //优惠扣除，如果为0显示通过，其他直接展示费用
            if (withdrawAudit.getFavorableFee() != null && !"null".equals(withdrawAudit.getFavorableFee())) {
                if (withdrawAudit.getFavorableFee() == 0) {
                    helper.setText(R.id.tv_discount_deducted, "通过");
                } else {
                    helper.setText(R.id.tv_discount_deducted, auditBean.getData().getCurrencySign() + withdrawAudit.getFavorableFee());
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        if (withdrawPresenter != null) {
            withdrawPresenter.onDestory();
        }
        super.onDestroy();
    }
}
