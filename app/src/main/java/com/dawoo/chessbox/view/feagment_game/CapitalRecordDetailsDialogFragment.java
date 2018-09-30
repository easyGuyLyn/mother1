package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.CapitalRecordDetail;
import com.dawoo.chessbox.mvp.presenter.RecordPresenter;
import com.dawoo.chessbox.mvp.view.ICapitalDetailView;
import com.dawoo.chessbox.push.notifition.HeadsUp;
import com.dawoo.chessbox.util.ComplexFragmentManager;
import com.dawoo.chessbox.util.SharePreferenceUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.util.StringTool;
import com.dawoo.chessbox.view.view.HeaderView;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import butterknife.BindView;

/**
 * 资金记录详情dialog
 */
public class CapitalRecordDetailsDialogFragment extends BaseDialogFragment implements ICapitalDetailView {


    @BindView(R.id.tv_transactionNo)
    TextView mTvTransactionNo;
    @BindView(R.id.tv_createTime)
    TextView mTvCreateTime;
    @BindView(R.id.tv_transactionWayName)
    TextView mTvTransactionWayName;
    @BindView(R.id.tv_failureReason)
    TextView mTvFailureReason;
    @BindView(R.id.tv_realName)
    TextView mTvRealName;
    @BindView(R.id.tv_deductFavorable)
    TextView mTvDeductFavorable;
    @BindView(R.id.tv_poundage)
    TextView mTvPoundage;
    @BindView(R.id.tv_rechargeTotalAmount)
    TextView mRechargeTotalAmount;
    @BindView(R.id.tv_statusName)
    TextView mTvStatusName;
    @BindView(R.id.tv_rechargeAddress)
    TextView mTvRechargeAddress;
    @BindView(R.id.tv_rechargeAmount)
    TextView mTvRechargeAmount;
    @BindView(R.id.tv_administrativeFee)
    TextView mTvAdministrativeFee;
    @BindView(R.id.tv_detail_transactionMoney)
    TextView tvDmEtailTransactionMoney;
    @BindView(R.id.tv_detail_status)
    TextView mTvDetailStatus;
    @BindView(R.id.ll_inOrOut)
    LinearLayout mLlInOrOut;
    @BindView(R.id.tv_poundage_name)
    TextView mTvPoundageName;
    @BindView(R.id.tv_withdraw_money)
    TextView mTvWithdrawMoney;
    @BindView(R.id.tv_transferOut)
    TextView mTvTransferOut;
    @BindView(R.id.tv_transferInto)
    TextView mTvTransferInto;
    @BindView(R.id.img_close)
    ImageView imgClose;


    private String mType;


    private RecordPresenter mRecordPresenter;
    private int mRecordId;
    private String mTImeZone;


    public static CapitalRecordDetailsDialogFragment getInstance(String mType, int mRecordId) {
        CapitalRecordDetailsDialogFragment mCapitalRecordDetailsDialogFragment = new CapitalRecordDetailsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mType", mType);
        bundle.putInt("mRecordId", mRecordId);
        mCapitalRecordDetailsDialogFragment.setArguments(bundle);
        return mCapitalRecordDetailsDialogFragment;
    }

    @Override
    protected int getViewId() {
        return R.layout.qt_activity_capital_record_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getString("mType");
            mRecordId = bundle.getInt("mRecordId");

        }
    }

    @Override
    protected void initViews(View view) {
        intScreenHProportion = 0.93;
        intScreenWProportion = 0.7;
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
                dismiss();
            }
        });
    }

    @Override
    protected void initData() {
        mTImeZone = SharePreferenceUtil.getTimeZone(getActivity());
        Log.e("lyn_mRecordId", mRecordId + "");
        mRecordPresenter = new RecordPresenter(getActivity(), this);
        mRecordPresenter.getCapitalRecordDetail(mRecordId);

        if (mType.equals("deposit") || mType.equals("withdrawals")) {
            if (mType.equals("deposit")) {
                ((ViewGroup) mTvRechargeAmount.getParent()).setVisibility(View.VISIBLE);
            } else if (mType.equals("withdrawals")) {
                ((ViewGroup) mTvWithdrawMoney.getParent()).setVisibility(View.VISIBLE);
            }
            mLlInOrOut.setVisibility(View.VISIBLE);
            ((ViewGroup) tvDmEtailTransactionMoney.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mTvDetailStatus.getParent()).setVisibility(View.GONE);
        } else {
            mLlInOrOut.setVisibility(View.GONE);
            ((ViewGroup) tvDmEtailTransactionMoney.getParent()).setVisibility(View.VISIBLE);
            ((ViewGroup) mTvDetailStatus.getParent()).setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onCapitalDetailResult(Object o) {
        CapitalRecordDetail capitalRecordDetail = (CapitalRecordDetail) o;
        setData(capitalRecordDetail);
    }

    private void setData(CapitalRecordDetail capitalRecordDetail) {
        if (capitalRecordDetail.getTransactionNo() != null) {
            mTvTransactionNo.setText(capitalRecordDetail.getTransactionNo());
        }

        Log.e("time zone", mTImeZone);
        Log.e("time new Zone", DateTool.getWuYong());

        Log.e("time old", DateTool.getTimeFromLong(DateTool.FMT_DATE_TIME, capitalRecordDetail.getCreateTime()));
        Log.e("time new", DateTool.getTimeFromLong(DateTool.FMT_DATE_TIME, DateTool.changeTimeZone(capitalRecordDetail.getCreateTime(), mTImeZone, DateTool.getWuYong())));

        mTvCreateTime.setText(DateTool.getTimeFromLong(DateTool.FMT_DATE_TIME, DateTool.changeTimeZone(capitalRecordDetail.getCreateTime(), mTImeZone, DateTool.getWuYong())));
        if (StringTool.isEmpty(capitalRecordDetail.getTransactionWayName())) {
            ((ViewGroup) mTvTransactionWayName.getParent()).setVisibility(View.GONE);
        } else {
            ((ViewGroup) mTvTransactionWayName.getParent()).setVisibility(View.VISIBLE);
            mTvTransactionWayName.setText(capitalRecordDetail.getTransactionWayName());
        }

        if (StringTool.isEmpty(capitalRecordDetail.getFailureReason())) {
            ((ViewGroup) mTvFailureReason.getParent()).setVisibility(View.GONE);
        } else {
            ((ViewGroup) mTvFailureReason.getParent()).setVisibility(View.GONE);
            mTvFailureReason.setText(capitalRecordDetail.getFailureReason());
        }
        if (StringTool.isEmpty(capitalRecordDetail.getRechargeAddress())) {
            ((ViewGroup) mTvRechargeAddress.getParent()).setVisibility(View.GONE);
        } else {
            ((ViewGroup) mTvRechargeAddress.getParent()).setVisibility(View.VISIBLE);
            mTvRechargeAddress.setText(capitalRecordDetail.getRechargeAddress());
        }

        if (StringTool.isEmpty(capitalRecordDetail.getTransferOut())) {
            ((ViewGroup) mTvTransferOut.getParent()).setVisibility(View.GONE);
        } else {
            ((ViewGroup) mTvTransferOut.getParent()).setVisibility(View.VISIBLE);
            mTvTransferOut.setText(capitalRecordDetail.getTransferOut());
        }

        if (StringTool.isEmpty(capitalRecordDetail.getTransferInto())) {
            ((ViewGroup) mTvTransferInto.getParent()).setVisibility(View.GONE);
        } else {
            ((ViewGroup) mTvTransferInto.getParent()).setVisibility(View.VISIBLE);
            mTvTransferInto.setText(capitalRecordDetail.getTransferInto());
        }

        if (capitalRecordDetail.getRealName() != null) {
            mTvRealName.setText(capitalRecordDetail.getRealName());
            ((ViewGroup) mTvRealName.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((ViewGroup) mTvRealName.getParent()).setVisibility(View.GONE);
        }
        if (StringTool.isEmpty(capitalRecordDetail.getPoundage())) {
            // ((ViewGroup) mTvPoundage.getParent()).setVisibility(View.GONE);
        } else {
            ((ViewGroup) mTvPoundage.getParent()).setVisibility(View.VISIBLE);
            mTvPoundage.setText(capitalRecordDetail.getPoundage());
            if (mType.equals("withdrawals") && TextUtils.isEmpty(capitalRecordDetail.getPoundage())) {
                mTvPoundageName.setText(getString(R.string.capital_poundage_free));
            }
            if (mType.equals("deposit")) {
                mTvPoundageName.setText(getString(R.string.capital_poundage_cunKuan));
            }
        }
        if (StringTool.isEmpty(capitalRecordDetail.getRechargeTotalAmount())) {
            //  ((ViewGroup) mRechargeTotalAmount.getParent()).setVisibility(View.GONE);
        } else {
            ((ViewGroup) mRechargeTotalAmount.getParent()).setVisibility(View.VISIBLE);
            mRechargeTotalAmount.setText(capitalRecordDetail.getRechargeTotalAmount());
        }

        if ("bitcoin".equals(capitalRecordDetail.getBankCode())) {
            if (mType.equals("deposit")) {
                ((ViewGroup) mTvPoundage.getParent()).setVisibility(View.GONE);
                ((ViewGroup) mRechargeTotalAmount.getParent()).setVisibility(View.GONE);
            } else if (mType.equals("withdrawals")) {
                if (capitalRecordDetail.getPoundage().startsWith("Ƀ0.")) {
                    ((ViewGroup) mTvPoundage.getParent()).setVisibility(View.GONE);
                }
                if (capitalRecordDetail.getRechargeTotalAmount().startsWith("Ƀ0.")) {
                    ((ViewGroup) mRechargeTotalAmount.getParent()).setVisibility(View.GONE);
                }
            }
        }

        if (StringTool.isEmpty(capitalRecordDetail.getDeductFavorable())) {
            ((ViewGroup) mTvDeductFavorable.getParent()).setVisibility(View.GONE);
        } else {
            ((ViewGroup) mTvDeductFavorable.getParent()).setVisibility(View.VISIBLE);
            mTvDeductFavorable.setText(capitalRecordDetail.getDeductFavorable());
        }

        if (capitalRecordDetail.getStatusName() != null) {
            mTvStatusName.setText(capitalRecordDetail.getStatusName());
            mTvDetailStatus.setText(capitalRecordDetail.getStatusName());
            setStatusColor(mTvStatusName, capitalRecordDetail.getStatusName());
            setStatusColor(mTvDetailStatus, capitalRecordDetail.getStatusName());
        }
        if (capitalRecordDetail.getTransactionMoney() != null) {
            tvDmEtailTransactionMoney.setText(capitalRecordDetail.getTransactionMoney());
        }

        if (capitalRecordDetail.getDeductFavorable() != null && mType.equals("backwater")) {
            tvDmEtailTransactionMoney.setText(capitalRecordDetail.getDeductFavorable());
        }

        if (StringTool.isEmpty(capitalRecordDetail.getRechargeAmount())) {
            //  ((ViewGroup) mTvRechargeAmount.getParent()).setVisibility(View.GONE);
        } else {
            ((ViewGroup) mTvRechargeAmount.getParent()).setVisibility(View.VISIBLE);
            mTvRechargeAmount.setText(capitalRecordDetail.getRechargeAmount());
        }
        if (StringTool.isEmpty(capitalRecordDetail.getWithdrawMoney())) {
            //  ((ViewGroup) mTvWithdrawMoney.getParent()).setVisibility(View.GONE);
        } else {
            ((ViewGroup) mTvWithdrawMoney.getParent()).setVisibility(View.VISIBLE);
            mTvWithdrawMoney.setText(capitalRecordDetail.getWithdrawMoney());
        }
        if (StringTool.isEmpty(capitalRecordDetail.getAdministrativeFee())) {
            ((ViewGroup) mTvAdministrativeFee.getParent()).setVisibility(View.GONE);
        } else {
            ((ViewGroup) mTvAdministrativeFee.getParent()).setVisibility(View.VISIBLE);
            mTvAdministrativeFee.setText(capitalRecordDetail.getAdministrativeFee());
        }
    }

    private void setStatusColor(TextView textView, String status) {
        switch (status) {
            case "失败":
                textView.setTextColor(getResources().getColor(R.color.failure));
                break;
            case "待支付":
                textView.setTextColor(getResources().getColor(R.color.btn_yellow_normal));
                break;
            case "成功":
                textView.setTextColor(getResources().getColor(R.color.sucess));
                break;
            case "处理中":
                textView.setTextColor(getResources().getColor(R.color.process));
                break;
            case "拒绝":
                textView.setTextColor(getResources().getColor(R.color.failure));
                break;
            case "待处理":
                textView.setTextColor(getResources().getColor(R.color.process));
                break;
            default:
        }
    }

    @Override
    public void onDestroy() {
        mRecordPresenter.onDestory();
        super.onDestroy();
    }

}
