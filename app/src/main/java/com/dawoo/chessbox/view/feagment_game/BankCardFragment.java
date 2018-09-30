package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.RexUtils;
import com.dawoo.coretool.util.CleanLeakUtils;
import com.dawoo.coretool.util.SPTool;
import com.dawoo.coretool.util.StringTool;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.BankCards;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.WithdrawSubmitResult;
import com.dawoo.chessbox.mvp.presenter.WithdrawPresenter;
import com.dawoo.chessbox.mvp.view.IAddBankCardView;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.view.fragment.BaseFragment;
import com.hwangjr.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 银行卡
 */
public class BankCardFragment extends BaseFragment implements IAddBankCardView {


    @BindView(R.id.et_real_name)
    EditText mEtRealName;
    @BindView(R.id.tv_real_name_hint)
    TextView mHintRealName;
    @BindView(R.id.tv_bank)
    TextView mTvBank;
    @BindView(R.id.iv_select)
    ImageView mIvSelect;
    @BindView(R.id.ll_select_bank)
    RelativeLayout mLlSelectBank;
    @BindView(R.id.et_card_number)
    EditText mEtCardNumber;
    @BindView(R.id.et_open_account_bank)
    EditText mEtOpenAccountBank;
    @BindView(R.id.bt_submit)
    Button mBtSubmit;
    @BindView(R.id.tv_open_account_bank_info)
    LinearLayout mTvOpenAccountInfo;
    Unbinder unbinder;

    private List<String> mBanks = new ArrayList<>();
    private String mBankName;
    private WithdrawPresenter mMWithdrawPresenter;
    public static final int ADD_BANK_CARD = 8008;
    private List<BankCards.BankListBean> mBankList;
    private int mIndex = 3;

    private Animation mAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qt_bankcard_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    public void initData() {
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        mMWithdrawPresenter = new WithdrawPresenter(getContext(), this);
        mMWithdrawPresenter.getCardType();
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
        CleanLeakUtils.fixInputMethodManagerLeak(getContext());
        mMWithdrawPresenter.onDestory();
    }

    @OnClick({R.id.ll_select_bank, R.id.bt_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_select_bank:
                onSelectBank(view);
                break;
            case R.id.bt_submit:
                if (SystemUtil.isFastClick()) return;
                if (mAnimation!=null){
                    mBtSubmit.startAnimation(mAnimation);
                }else {
                    mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
                    mBtSubmit.startAnimation(mAnimation);
                }
                onSubmit(view);
                break;
        }
    }

    @Override
    public void getCardType(Object o) {
        BankCards bankCards = (BankCards) o;
        if (bankCards == null)
            return;
        BankCards.UserBean.BankcardBean bankcardBean = bankCards.getUser().getBankcard();
        mBankList = bankCards.getBankList();

        if (mBankList.size() != 0) {
            for (BankCards.BankListBean bank : mBankList) {
                mBanks.add(bank.getText());
            }
        }
        if (bankcardBean != null) {
            initView(bankcardBean);
        } else {
            String realName = DataCenter.getInstance().getRealName();
            if (!TextUtils.isEmpty(realName)) {
                mEtRealName.setFocusable(false);
                mEtRealName.setText(StringTool.getCoverRealName(realName));
            }
        }
    }

    @Override
    public void submitBankCard(Object o) {
        WithdrawSubmitResult withdrawSubmitResult = (WithdrawSubmitResult) o;
        if (withdrawSubmitResult.getCode() == 0 && withdrawSubmitResult.getError() == 0) {
            showTips("绑定成功");
            // ToastUtil.showToastLong(getContext(), "绑定成功");
            RxBus.get().post(ConstantValue.EVENT_TYPE_ACCOUNT, "bank update");
            getActivity().setResult(ADD_BANK_CARD);
            modifyInput();
            return;
        }
        showTips(withdrawSubmitResult.getMessage());
        // ToastUtil.showToastLong(getContext(), withdrawSubmitResult.getMessage());
    }

    @Override
    public void selectedBank(String bankName, int index) {
        mBankName = bankName;
        this.mIndex = index;
        mTvBank.setText(mBankName);
    }

    private void initView(BankCards.UserBean.BankcardBean bankcardBean) {
        mEtRealName.setFocusable(false);
        mEtCardNumber.setFocusable(false);
        mEtOpenAccountBank.setFocusable(false);
        mHintRealName.setVisibility(View.GONE);
        mIvSelect.setVisibility(View.GONE);
        mBtSubmit.setVisibility(View.GONE);
        mTvOpenAccountInfo.setVisibility(View.GONE);
        mEtRealName.setText(bankcardBean.getBankcardMasterName());
        mTvBank.setText(bankcardBean.getBankName());
        mEtCardNumber.setText(bankcardBean.getBankcardNumber());
        if (TextUtils.isEmpty(bankcardBean.getBankDeposit())) {
            mEtOpenAccountBank.setHint("");
        } else
            mEtOpenAccountBank.setText(bankcardBean.getBankDeposit());
        mLlSelectBank.setEnabled(false);
    }


    public void onSelectBank(View view) {
        mMWithdrawPresenter.initSelectBankDialog(mBanks);
    }

    public void onSubmit(View view) {
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        if (mEtCardNumber.getText().toString().trim().length() < 10) {
            showTips(getResources().getString(R.string.card_lenght_hint));
            //ToastUtil.showResLong(getContext(), R.string.card_lenght_hint);
            return;
        }
        String mRealName;
        String realName = DataCenter.getInstance().getRealName();
        if (!TextUtils.isEmpty(realName)) {
            mRealName = realName;
        } else {
            mRealName = mEtRealName.getText().toString().trim();
        }


        String mCardNumber = mEtCardNumber.getText().toString().trim();
        String mOpenAccountBank = mEtOpenAccountBank.getText().toString().trim();
        if (TextUtils.isEmpty(mRealName)||mRealName.length()<2||mRealName.length()>30) {
            showTips(getResources().getString(R.string.bad_realname));
            //ToastUtil.showResLong(getContext(), R.string.card_name_hint);
            return;
        }
        if (TextUtils.isEmpty(mBankName)) {
            showTips(getString(R.string.card_bank_type_hint));
            //ToastUtil.showResLong(getContext(), R.string.card_bank_type_hint);
            return;
        } else if (mBankList.get(mIndex).getValue().equals("other_bank")) {
            if (TextUtils.isEmpty(mOpenAccountBank)) {
                showTips(getString(R.string.please_input_open_account_bank_info));
                //ToastUtil.showResLong(getContext(), R.string.please_input_open_account_bank_info);
                return;
            }
        }
        mMWithdrawPresenter.submitBankCard(mRealName, mBankList.get(mIndex).getValue(), mCardNumber, mOpenAccountBank);
    }

    //修改成功　取消输入显示
    public void modifyInput() {
        mMWithdrawPresenter.getCardType();
    }

    //显示提示信息
    public void showTips(String tipsContent) {
        BaseTipsDialogFragment.newInstance(tipsContent, false).show(getChildFragmentManager(), null);
    }

    @Override
    public void onDestroy() {
        if (mMWithdrawPresenter!=null){
            mMWithdrawPresenter.onDestory();
        }
        super.onDestroy();
    }
}
