package com.dawoo.chessbox.view.feagment_game;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.coretool.util.SPTool;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.IsOpenPwdByPhoneBean;
import com.dawoo.chessbox.mvp.presenter.UserPresenter;
import com.dawoo.chessbox.mvp.view.IsOpenPwdByPhoneView;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.ComplexFragmentManager;
import com.dawoo.chessbox.util.SoundUtil;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 安全中心Dialog
 */
public class SecurityCenterDialogFragment extends BaseDialogFragment implements IsOpenPwdByPhoneView {

    public static final int MODIFYPASSWORDFRAGMENT = 1;
    public static final int MODIFYSAFETYCODEFRAGMENT = 2;
    public static final int BANKCARDFRAGMENT = 3;
    public static final int BINGPHONE = 4;
    public static final int WELFARECONVERSION = 5;

    @BindView(R.id.tv_modify_password)
    TextView tvModifyPassword;
    @BindView(R.id.tv_modify_safetycode)
    TextView tvModifySafetycode;
    @BindView(R.id.tv_bankcard)
    TextView tvBankcard;
    @BindView(R.id.tv_tips)
    LinearLayout tvTips;
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.tv_welfare_conversion)
    TextView tvWelfareConversion;
    @BindView(R.id.tv_bind_phone)
    TextView tvBindPhone;
    @BindView(R.id.fragment_content)
    LinearLayout fragmentContent;

    public int type = 1;
    private UserPresenter userPresenter;
    public boolean isDismiss = false;
    private Boolean mIsSucessSetRealName = true;

    private Animation mAnimation;

    public static SecurityCenterDialogFragment newInstance(int type, boolean isDismiss) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putBoolean("isDismiss", isDismiss);
        SecurityCenterDialogFragment fragment = new SecurityCenterDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getViewId() {
        return R.layout.qt_securitycenter_dialogfragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
        isDismiss = getArguments().getBoolean("isDismiss");
        RxBus.get().register(this);
    }

    @Override
    protected void initViews(View view) {
        intScreenHProportion = 0.93;
        intScreenWProportion = 0.7;
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        userPresenter = new UserPresenter(getContext(), this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isSelected();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void isSelected(){
        if (type == MODIFYPASSWORDFRAGMENT) {
            tvModifyPassword.setSelected(true);
            tvModifySafetycode.setSelected(false);
            tvBankcard.setSelected(false);
            tvBindPhone.setSelected(false);
            tvWelfareConversion.setSelected(false);
            ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), ModifyPasswordFragment.class);
        } else if (type == MODIFYSAFETYCODEFRAGMENT) {
            tvModifyPassword.setSelected(false);
            tvBankcard.setSelected(false);
            tvModifySafetycode.setSelected(true);
            tvBindPhone.setSelected(false);
            tvWelfareConversion.setSelected(false);
            ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), ModifySafetyCodeFragment.class);
        } else if (type == BANKCARDFRAGMENT) {
            tvBankcard.setSelected(true);
            tvModifyPassword.setSelected(false);
            tvModifySafetycode.setSelected(false);
            tvBindPhone.setSelected(false);
            tvWelfareConversion.setSelected(false);
            ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), BankCardFragment.class);
        } else if (type == BINGPHONE) {
            tvBankcard.setSelected(false);
            tvModifyPassword.setSelected(false);
            tvModifySafetycode.setSelected(false);
            tvBindPhone.setSelected(true);
            tvWelfareConversion.setSelected(false);
            ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), BindPhoneFragment.class);
        } else if (type == WELFARECONVERSION) {
            tvBankcard.setSelected(false);
            tvModifyPassword.setSelected(false);
            tvModifySafetycode.setSelected(false);
            tvBindPhone.setSelected(false);
            tvWelfareConversion.setSelected(true);
            ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), WelfareConversionFragment.class);
        }
    }
    @Override
    protected void initData() {
        userPresenter.isOPenPwdByPhone();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
        ComplexFragmentManager.getInstance().clear();
        RxBus.get().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @OnClick({R.id.tv_modify_password, R.id.tv_modify_safetycode, R.id.tv_bankcard, R.id.img_close, R.id.tv_welfare_conversion, R.id.tv_bind_phone})
    public void onViewClicked(View view) {
        if (view.getId() != R.id.img_close) {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        } else {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        }
        switch (view.getId()) {
            case R.id.tv_modify_password:
                startAnimation(tvModifyPassword);
                tvModifyPassword.setSelected(true);
                tvModifySafetycode.setSelected(false);
                tvBankcard.setSelected(false);
                tvBindPhone.setSelected(false);
                tvWelfareConversion.setSelected(false);
                ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), ModifyPasswordFragment.class);
                break;
            case R.id.tv_modify_safetycode:
                startAnimation(tvModifySafetycode);
                tvModifyPassword.setSelected(false);
                tvBankcard.setSelected(false);
                tvModifySafetycode.setSelected(true);
                tvBindPhone.setSelected(false);
                tvWelfareConversion.setSelected(false);
                RxBus.get().post(ConstantValue.SRCOURITY_REALNAME, "222");
                ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), ModifySafetyCodeFragment.class);
                break;
            case R.id.tv_bankcard:
                startAnimation(tvBankcard);
                tvBankcard.setSelected(true);
                tvModifyPassword.setSelected(false);
                tvModifySafetycode.setSelected(false);
                tvBindPhone.setSelected(false);
                tvWelfareConversion.setSelected(false);
                ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), BankCardFragment.class);
                break;
            case R.id.img_close:
                dismiss();
                break;
            case R.id.tv_welfare_conversion:
                startAnimation(tvWelfareConversion);
                tvBankcard.setSelected(false);
                tvModifyPassword.setSelected(false);
                tvModifySafetycode.setSelected(false);
                tvBindPhone.setSelected(false);
                tvWelfareConversion.setSelected(true);
                ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), WelfareConversionFragment.class);
                break;
            case R.id.tv_bind_phone:
                startAnimation(tvBindPhone);
                tvBankcard.setSelected(false);
                tvModifyPassword.setSelected(false);
                tvModifySafetycode.setSelected(false);
                tvBindPhone.setSelected(true);
                tvWelfareConversion.setSelected(false);
                ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), BindPhoneFragment.class);
                break;
        }
    }

    @Subscribe(tags = {@Tag(ConstantValue.BIND_NEW_PHONE_SUCCESS)})
    public void bindSuccessInitView(String aa) {
        tvBankcard.setSelected(false);
        tvModifyPassword.setSelected(false);
        tvModifySafetycode.setSelected(false);
        tvBindPhone.setSelected(true);
        tvWelfareConversion.setSelected(false);
        ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), BindPhoneFragment.class);
    }


    @Subscribe(tags = {@Tag(ConstantValue.CHANGE_BIND_PHONE)})
    public void changeBindPhone(String string) {
        tvBankcard.setSelected(false);
        tvModifyPassword.setSelected(false);
        tvModifySafetycode.setSelected(false);
        tvBindPhone.setSelected(true);
        tvWelfareConversion.setSelected(false);
        ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), BindChangePhoneFragment.class);
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_GOTOTAB_SERVICE)})
    public void startService(String aa) {
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        ActivityUtil.startServiceDialog(getChildFragmentManager());
    }

    /**
     * 1 显示
     */
    @Override
    public void isOpenResult(Object o) {
        IsOpenPwdByPhoneBean isOpenPwdByPhoneBean = (IsOpenPwdByPhoneBean) o;
        if ("1".equals(isOpenPwdByPhoneBean.getData())) {
            tvBindPhone.setVisibility(View.VISIBLE);
        } else {
            tvBindPhone.setVisibility(View.GONE);
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
