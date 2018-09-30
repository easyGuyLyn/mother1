package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.coretool.util.math.BigDemicalUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.UserAccount;
import com.dawoo.chessbox.bean.Withdraw;
import com.dawoo.chessbox.bean.WithdrawFee;
import com.dawoo.chessbox.bean.WithdrawResult;
import com.dawoo.chessbox.mvp.presenter.WithdrawPresenter;
import com.dawoo.chessbox.mvp.view.IWithdrawView;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.DialogFramentManager;
import com.dawoo.chessbox.util.SingleToast;
import com.dawoo.chessbox.util.TipsDialogFaragmentUtils;
import com.hwangjr.rxbus.RxBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收益dialog
 */
public class ProfitDialogFragment extends BaseDialogFragment implements IWithdrawView {

    @BindView(R.id.user_account_tv)
    TextView mUserAccountTv;
    @BindView(R.id.tv_service_charge)
    TextView mTvServiceCharge;  //手续费
    @BindView(R.id.tv_bank_card)
    TextView mTvBankCard; //银行卡
    @BindView(R.id.et_withdrawals_amount)
    EditText mEtWithdrawalsAmount; //输入金额
    @BindView(R.id.add_fifty)
    TextView mAddFifty;  //加50
    @BindView(R.id.add_hundred)
    TextView mAddHundred;  //加100
    @BindView(R.id.bt_submit)
    Button mBtSubmit; //确认出币
    @BindView(R.id.tv_bind_bank_card)
    TextView mAddTvBindBankCard; //绑定银行卡
    @BindView(R.id.ll_withdraw)
    LinearLayout mLlWithdraw;  //输入界面
    @BindView(R.id.img_close)
    ImageView imgClose;//关闭

    private int mType = 1;
    private final int BANK_CARD = 1;
    private final int BITCOIN_CARD = 2;

    private WithdrawPresenter mMWithdrwaPresenter;
    private Withdraw mMWithdraw;
    private Withdraw.AuditMapBean mAuditMapBean;
    private double mActualWithdraw;
    private UserAccount mAccount;  //资金
    public WithdrawResult withdrawResult; //状态

    private Animation mAnimation;

    @Override
    protected int getViewId() {
        return R.layout.qt_profit_dialogfragment;
    }

    @Override
    protected void initViews(View view) {
        intScreenHProportion = 0.93;
        intScreenWProportion = 0.7;
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        mEtWithdrawalsAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    if ("0".equals(s.toString())) {
                        mEtWithdrawalsAmount.setText(null);
                        return;
                    }
                    mCountDownTimer.cancel();
                    mCountDownTimer.start();
                }
            }
        });

    }

    private CountDownTimer mCountDownTimer = new CountDownTimer(1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            String num;
            if (mEtWithdrawalsAmount == null) {
                return;
            }
            num = mEtWithdrawalsAmount.getText().toString().trim();

            if (!TextUtils.isEmpty(num)) {
                mActualWithdraw = 0;
                mMWithdrwaPresenter.withdrawFee(Double.parseDouble(num));
            }
        }
    };

    @Override
    protected void initData() {
        mMWithdrwaPresenter = new WithdrawPresenter(getActivity(), this);
        mMWithdrwaPresenter.getWithdraw();
        mMWithdrwaPresenter.getAccount();
    }

    @Override
    public void onWithdrawInfo(Object o) {
        withdrawResult = (WithdrawResult) o;
        if (withdrawResult == null) {
            SingleToast.showMsg("系统异常");
            dismiss();
            return;
        }

        mLlWithdraw.setVisibility(View.VISIBLE);
        mMWithdraw = withdrawResult.getData();
        if (mMWithdraw == null) {
            return;
        }
        //  mEtWithdrawalsAmount.setHint(mMWithdraw.getCurrencySign() + mMWithdraw.getRank().getWithdrawMinNum() + " - " + mMWithdraw.getCurrencySign() + mMWithdraw.getRank().getWithdrawMaxNum());
        if (mMWithdraw.isIsCash() && mMWithdraw.isIsBit()) { //是否有比特币账户
            // mLlTab.setVisibility(View.VISIBLE);
        }

        if (mMWithdraw.isIsCash()) {
            //银行卡
            //setDataToView(BANK_CARD);
            Withdraw.BankcardMapBean.BankCardBean bankBean = mMWithdraw.getBankcardMap().getBankCardBean1();
            if (bankBean != null) {
                mTvBankCard.setText(bankBean.getBankcardNumber());
                mAddTvBindBankCard.setText("已绑定");
                mAddTvBindBankCard.setEnabled(false);
            } else {
                mTvBankCard.setText("请先绑定银行卡");
                mAddTvBindBankCard.setText("绑定福利账号");
                mAddTvBindBankCard.setEnabled(true);
            }
        } else if (mMWithdraw.isIsBit()) { //比特币
            //setDataToView(BITCOIN_CARD);
        }
    }

    public void showBankcardfragmentDialog() {
        BaseTipsDialogFragment baseTipsDialogFragment = BaseTipsDialogFragment.newInstance("没有添加银行卡", false);
        baseTipsDialogFragment.setConfirmClickListener(new BaseTipsDialogFragment.OnSweetClickListener() {
            @Override
            public void onClick(BaseTipsDialogFragment baseTipsDialogFragment) {
                RxBus.get().post(ConstantValue.SECURITY_CENTER_DIALOGFRAGMENT, SecurityCenterDialogFragment.BANKCARDFRAGMENT);
                dismiss();
            }
        });
        DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), baseTipsDialogFragment);
    }

    public void submitWithdraw(Object o) {

    }

    @Override
    public void checkSafePassword(Object o) {

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
//                mTvAdministrativeFee.setText("¥" + Math.abs(withdrawFee.getData().getAdministrativeFee()));
//                mTvDiscount.setText("¥" + Math.abs(withdrawFee.getData().getDeductFavorable()));
//                mTvEndWithdrawalsAmount.setText("¥" + BigDemicalUtil.moneyFormat(withdrawFee.getData().getActualWithdraw()));
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
        setPopuPWindData(o);
    }

    void setPopuPWindData(Object o) {
        if (o != null && o instanceof UserAccount) {
            // 设置账户
            mAccount = ((UserAccount) o);
            //oneKeyBack(mAccount.getUser());  //判断是一免转用户？
            //RxBus.get().post(ConstantValue.EVENT_TYPE_MINE_LINK, mAccount);
            UserAccount.UserBean userBean = mAccount.getUser();
            mUserAccountTv.setText(BigDemicalUtil.moneyFormat(userBean.getWalletBalance()));
            if (userBean.getWalletBalance() >= 100) {
                // mIlNoSufficientFunds.setVisibility(View.GONE);
//                mLlWithdraw.setVisibility(View.VISIBLE);
                // mMWithdrwaPresenter.getWithdraw();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.img_close, R.id.bt_submit, R.id.add_fifty, R.id.add_hundred, R.id.tv_bind_bank_card})
    public void onViewClicked(View view) {
        long num = 0;
        switch (view.getId()) {
            case R.id.img_close:
                dismiss();
                break;

            case R.id.bt_submit:
                if (mAnimation!=null){
                    mBtSubmit.startAnimation(mAnimation);
                }else {
                    mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
                    mBtSubmit.startAnimation(mAnimation);
                }
                testInputAndSubmit();
                break;
            case R.id.tv_bind_bank_card:
                RxBus.get().post(ConstantValue.SECURITY_CENTER_DIALOGFRAGMENT, SecurityCenterDialogFragment.BANKCARDFRAGMENT);
                dismiss();
                break;
            case R.id.add_fifty:
                if (TextUtils.isEmpty(mEtWithdrawalsAmount.getText().toString().trim())) {
                    mEtWithdrawalsAmount.setText("50");
                } else {
                    num = Long.parseLong(mEtWithdrawalsAmount.getText().toString().trim());
                    num = num + 50;
                    if (num>99999999){
                        mEtWithdrawalsAmount.setText("99999999");
                    }else {
                        mEtWithdrawalsAmount.setText(num + "");
                    }
                }

                break;
            case R.id.add_hundred:
                if (TextUtils.isEmpty(mEtWithdrawalsAmount.getText().toString().trim())) {
                    mEtWithdrawalsAmount.setText("100");
                } else {
                    num = Long.parseLong(mEtWithdrawalsAmount.getText().toString().trim());
                    num = num + 100;
                    if (num>99999999){
                        mEtWithdrawalsAmount.setText("99999999");
                    }else {
                        mEtWithdrawalsAmount.setText(num + "");
                    }
                }
                break;
        }
    }

    private void testInputAndSubmit() {
        if (TextUtils.isEmpty(mEtWithdrawalsAmount.getText().toString().trim())) {
            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), "金额不能为空");
            return;
        }
        if (mMWithdraw == null) {
            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), "系统异常", new BaseTipsDialogFragment.OnSweetClickListener() {
                @Override
                public void onClick(BaseTipsDialogFragment baseTipsDialogFragment) {
                    baseTipsDialogFragment.dismiss();
                    dismiss();
                }
            });
            return;
        }
        if (mMWithdraw.isHasBank() == false) {//没有添加银行卡
            showBankcardfragmentDialog();
            return;
        }

        double num = Double.valueOf(mEtWithdrawalsAmount.getText().toString().trim());
        if (num == 0) {
            // ToastUtil.showResShort(getActivity(), R.string.input_withdrawals_amount);
            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), String.format(getString(R.string.input_withdrawals_amount)));
            return;
        } else if (num > mMWithdraw.getTotalBalance()) {
            DecimalFormat df = new DecimalFormat("######0.00");
            String f1 = df.format(mMWithdraw.getTotalBalance());

            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), "福利余额不足");
            return;
        } else if (num < Double.parseDouble(mMWithdraw.getRank().getWithdrawMinNum())) {
            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), "出币金额不低于" + Double.parseDouble(mMWithdraw.getRank().getWithdrawMinNum()));
            return;
        } else if (num > Double.parseDouble(mMWithdraw.getRank().getWithdrawMaxNum())) {
            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), "出币金额不高于" + Double.parseDouble(mMWithdraw.getRank().getWithdrawMaxNum()));
            return;
        }
        inspect();
        // mMWithdrwaPresenter.withdrawFee(Double.parseDouble(mEtWithdrawalsAmount.getText().toString().trim()), true);
    }


    public void inspect() {
        if (withdrawResult == null) {
            SingleToast.showMsg("系统异常");
            dismiss();
            return;
        }

        if (withdrawResult.getCode() == 1100) { //订单正在审核
            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), "已有取款订单，正在处理中", new BaseTipsDialogFragment.OnSweetClickListener() {
                @Override
                public void onClick(BaseTipsDialogFragment baseTipsDialogFragment) {
                    baseTipsDialogFragment.dismiss();
                    dismiss();
                }
            });
            return;
        }
        if (withdrawResult.getCode() == 1001) { //登录超时
            ActivityUtil.logout();
            ActivityUtil.gotoLogin();
            dismiss();
            return;
        }
        //账户提现次数过多
        if (withdrawResult.getCode() == 1024) {
            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), withdrawResult.getMsg(), new BaseTipsDialogFragment.OnSweetClickListener() {
                @Override
                public void onClick(BaseTipsDialogFragment baseTipsDialogFragment) {
                    baseTipsDialogFragment.dismiss();
                    dismiss();
                }
            });
            return;
        }
        if (withdrawResult.getCode() == 1103) { //没有添加银行卡
            showBankcardfragmentDialog();
            return;
        }

        // 全款玩家已被冻结，请联系客服处理
        if (withdrawResult.getCode() == 1101) {
            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), withdrawResult.getMsg(), new BaseTipsDialogFragment.OnSweetClickListener() {
                @Override
                public void onClick(BaseTipsDialogFragment baseTipsDialogFragment) {
                    baseTipsDialogFragment.dismiss();
                    dismiss();
                }
            });
            return;
        }

        if (withdrawResult.getCode() == 1102) {  //余额不足
//            mTvWithdrawState.setText(String.format(getString(R.string.not_sufficient_funds_hint), withdrawResult.getMsg()));
//            mIlNoSufficientFunds.setVisibility(View.VISIBLE);
            //  return;
        }
        Withdraw.BankcardMapBean.BankCardBean bankBean = mMWithdraw.getBankcardMap().getBankCardBean1();
        double textDouble = Double.parseDouble(mEtWithdrawalsAmount.getText().toString().trim());
        ProfitDetailsDialogFragment profitDetailsDialogFragment = ProfitDetailsDialogFragment.newInstance(textDouble, mMWithdraw, mType);
        DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), profitDetailsDialogFragment);
    }

    @Override
    public void onDestroy() {
        mMWithdrwaPresenter.onDestory();
        mCountDownTimer.cancel();
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
        super.onDestroy();
    }
}
