package com.dawoo.chessbox.view.feagment_game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.chessbox.bean.WithdrawResult;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.math.BigDemicalUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.Withdraw;
import com.dawoo.chessbox.bean.WithdrawFee;
import com.dawoo.chessbox.bean.WithdrawSubmitResult;
import com.dawoo.chessbox.mvp.presenter.WithdrawPresenter;
import com.dawoo.chessbox.mvp.view.IWithdrawView;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.DialogFramentManager;
import com.dawoo.chessbox.util.SingleToast;
import com.dawoo.chessbox.util.TipsDialogFaragmentUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

import static com.dawoo.chessbox.ConstantValue.EVENT_TYPE_ACCOUNT;

/**
 * 出币详情DialogFragment
 */
public class ProfitDetailsDialogFragment extends BaseDialogFragment implements IWithdrawView {


    public double num;
    @BindView(R.id.tv_bank_card)
    TextView tvBankCard;
    @BindView(R.id.tv_service_charge)
    TextView mTvServiceCharge;
    @BindView(R.id.et_withdrawals_amount)
    TextView etWithdrawalsAmount;
    @BindView(R.id.tv_administrative_fee)
    TextView mTvAdministrativeFee;
    @BindView(R.id.tv_discount)
    TextView mTvDiscount;
    @BindView(R.id.tv_end_withdrawals_amount)
    TextView mTvEndWithdrawalsAmount;
    @BindView(R.id.tv_look_record)
    Button tvLookRecord;
    @BindView(R.id.bt_submit)
    Button btSubmit;
    @BindView(R.id.ll_withdraw)
    LinearLayout llWithdraw;
    @BindView(R.id.img_close)
    ImageView imgClose;
    private WithdrawPresenter mMWithdrwaPresenter;
    private double mActualWithdraw = 0;
    private String mBankcardNumber;
    private int mType = 1;
    private Withdraw mMWithdraw;
    private Animation mAnimation;

    public static ProfitDetailsDialogFragment newInstance(double num, Withdraw mMWithdraw, int type) {
        Bundle args = new Bundle();
        args.putDouble("num", num);
        args.putInt("type", type);
        args.putSerializable("Withdraw", mMWithdraw);
        ProfitDetailsDialogFragment fragment = new ProfitDetailsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        num = getArguments().getDouble("num");
        mType = getArguments().getInt("type");
        mMWithdraw = (Withdraw) getArguments().getSerializable("Withdraw");
        if (mType == 1) {
            mBankcardNumber = mMWithdraw.getBankcardMap().getBankCardBean1().getBankcardNumber();
        } else {
            mBankcardNumber = mMWithdraw.getBankcardMap().getBankCardBean2().getBankcardNumber();
        }
        RxBus.get().register(this);
    }

    @Override
    protected int getViewId() {
        return R.layout.qt_profit_details_dialogfragment;
    }

    @Override
    protected void initViews(View view) {
        intScreenHProportion = 0.93;
        intScreenWProportion = 0.6;
        tvBankCard.setText(mBankcardNumber);
        etWithdrawalsAmount.setText(num + "");
    }

    @Override
    protected void initData() {
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        mMWithdrwaPresenter = new WithdrawPresenter(getActivity(), this);
        mMWithdrwaPresenter.withdrawFee(num);
    }


    @Override
    public void onWithdrawInfo(Object o) {
        WithdrawResult   withdrawResult = (WithdrawResult) o;
        if (withdrawResult == null) {
            SingleToast.showMsg("系统异常");
            dismiss();
            return;
        }
        mMWithdraw = withdrawResult.getData();
        if (mMWithdraw == null) {
            SingleToast.showMsg("系统异常");
            dismiss();
            return;
        }
    }

    @Override
    public void submitWithdraw(Object o) {
        WithdrawSubmitResult withdrawSubmitResult = (WithdrawSubmitResult) o;
        if (withdrawSubmitResult != null) {
            if (withdrawSubmitResult.getCode() == 1001) {
                ActivityUtil.logout();
                ActivityUtil.gotoLogin();
                dismiss();
                return;
            }
            if (withdrawSubmitResult.getCode() == 1305) {
                TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), getString(R.string.origin_pwd_error));
                return;
            }

//            if (withdrawSubmitResult. () != null && !TextUtils.isEmpty(withdrawSubmitResult.getData().getMsg())) {
//                TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), withdrawSubmitResult.getData().getMsg());
//                return;
//            }


            if (withdrawSubmitResult.getCode() == 1404) {
                RxBus.get().post(ConstantValue.SECURITY_CENTER_DIALOGFRAGMENT, SecurityCenterDialogFragment.MODIFYSAFETYCODEFRAGMENT);
                dismiss();
                return;
            }


            mMWithdraw.setToken(withdrawSubmitResult.getData().getToken());
            if (withdrawSubmitResult.getCode() == 0) {
                TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), "出币成功", new BaseTipsDialogFragment.OnSweetClickListener() {
                    @Override
                    public void onClick(BaseTipsDialogFragment baseTipsDialogFragment) {
//                        DialogFramentManager.getInstance().clearDialog(3);
                        RxBus.get().post(ConstantValue.EVENT_TYPE_ACCOUNT, "ConstantValue");
                        DialogFramentManager.getInstance().clearDialog();
                    }
                });

            } else {
                TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), withdrawSubmitResult.getMessage());
            }
        }
    }

    @Override
    public void checkSafePassword(Object o) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Subscribe(tags = {@Tag(ConstantValue.SRCOURITY_REFRESH)})
    public void safePassword(String sting) {
        mMWithdrwaPresenter.getWithdraw();
    }

    @Override
    public void withdrawFee(Object o) {
        WithdrawFee withdrawFee = (WithdrawFee) o;
        if (withdrawFee != null) {
            if (withdrawFee.getCode() == 1001) {
                ActivityUtil.gotoLogin();
                dismiss();
                return;
            }
            if (withdrawFee.getData() != null) {
                mActualWithdraw = withdrawFee.getData().getActualWithdraw();
                BigDecimal bg = new BigDecimal(withdrawFee.getData().getCounterFee());
                double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                if (f1 == 0) {
                    mTvServiceCharge.setText("免手续费");
                } else
                    mTvServiceCharge.setText("¥" + Math.abs(f1));
                mTvAdministrativeFee.setText("¥" + Math.abs(withdrawFee.getData().getAdministrativeFee()));
                mTvDiscount.setText("¥" + Math.abs(withdrawFee.getData().getDeductFavorable()));
                mTvEndWithdrawalsAmount.setText("¥" + BigDemicalUtil.moneyFormat(withdrawFee.getData().getActualWithdraw()));
            }
        }

    }


    @Override
    public void onRecoveryResult(Object o) {

    }

    @Override
    public void onRefreshResult(Object o) {

    }

    @Override
    public void onAssertResult(Object o) {

    }

    @Override
    public void onAccountResult(Object o) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        if (mMWithdrwaPresenter != null) {
            mMWithdrwaPresenter.onDestory();
        }
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
        RxBus.get().unregister(this);
        super.onDestroyView();
    }

    @OnClick({R.id.tv_look_record, R.id.bt_submit, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_look_record:
                startAnimation(tvLookRecord);
                AuditDialogFragment auditDialogFragment = new AuditDialogFragment();
                DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), auditDialogFragment);
                break;
            case R.id.bt_submit:
                startAnimation(btSubmit);
                if (mActualWithdraw > 0) {
                    if (!mMWithdraw.isSafePassword()) {
                        BaseTipsDialogFragment baseTipsDialogFragment = BaseTipsDialogFragment.newInstance(getString(R.string.set_origin_pwd), false);
                        baseTipsDialogFragment.setConfirmClickListener(new BaseTipsDialogFragment.OnSweetClickListener() {
                            @Override
                            public void onClick(BaseTipsDialogFragment baseTipsDialogFragment) {
                                baseTipsDialogFragment.dismiss();
                                RxBus.get().post(ConstantValue.SECURITY_CENTER_DIALOGFRAGMENT, SecurityCenterDialogFragment.MODIFYSAFETYCODEFRAGMENT);
                            }
                        });
                        DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), baseTipsDialogFragment);
                        return;
                    }

                    SecurityPasswordDialogFragment securityPasswordDialogFragment = new SecurityPasswordDialogFragment();
                    securityPasswordDialogFragment.setSubmitClicked(new SecurityPasswordDialogFragment.SubmitClicked() {
                        @Override
                        public void onSubmitClicked(String password) {
                            mMWithdrwaPresenter.submitWithdraw(num, mMWithdraw.getToken(), mType, password);
                        }
                    });
                    DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), securityPasswordDialogFragment);
                    //  mMWithdrwaPresenter.submitWithdraw(num, mMWithdraw.getToken(), mType, "123123");
                } else {
                    TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), String.format(getString(R.string.withdraw_min)));
                }
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }
    private void startAnimation(TextView textView){
        if (mAnimation!=null){
            textView.startAnimation(mAnimation);
        }else {
            mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
            textView.startAnimation(mAnimation);
        }
    }

}
