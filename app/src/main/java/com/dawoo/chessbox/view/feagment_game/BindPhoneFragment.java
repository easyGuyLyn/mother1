package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.GetBindUserPhoneNumberBean;
import com.dawoo.chessbox.bean.IsOpenPwdByPhoneBean;
import com.dawoo.chessbox.bean.RegisterBean;
import com.dawoo.chessbox.bean.RegisterInfoBean;
import com.dawoo.chessbox.mvp.presenter.RegisterPresenter;
import com.dawoo.chessbox.mvp.presenter.UserPresenter;
import com.dawoo.chessbox.mvp.view.AlreadyBindPhoneView;
import com.dawoo.chessbox.mvp.view.BindUserPhoneView;
import com.dawoo.chessbox.mvp.view.IRegisterView;
import com.dawoo.chessbox.mvp.view.IsOpenPwdByPhoneView;
import com.dawoo.chessbox.net.HttpResult;
import com.dawoo.chessbox.util.ComplexFragmentManager;
import com.dawoo.chessbox.util.DepositUtil;
import com.dawoo.chessbox.util.RexUtils;
import com.dawoo.chessbox.view.activity.MainActivity;
import com.dawoo.chessbox.view.fragment.BaseFragment;
import com.hwangjr.rxbus.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * \
 * 绑定手机　为进行绑定
 */

public class BindPhoneFragment extends BaseFragment implements BindUserPhoneView, IRegisterView, AlreadyBindPhoneView,IsOpenPwdByPhoneView {
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.tv_send_sms)
    TextView tvSendSms;
    @BindView(R.id.et_psw_code)
    EditText etPswCode;
    @BindView(R.id.bt_right_bind)
    Button btRightBind;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.ll_input_code)
    LinearLayout llInputCode;
    Unbinder unbinder;


    private UserPresenter userPresenter;
    private RegisterPresenter registerPresenter;
    private Animation mAnimation;

    private boolean isBind = false; //true 绑定了　false 没有绑定


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qt_bind_phone_dialogfragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    protected void initData() {
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        userPresenter = new UserPresenter(getActivity(), this);
        registerPresenter = new RegisterPresenter(getActivity(), this);
        //不删除　防止产品作妖
        //userPresenter.isOPenPwdByPhone();
        userPresenter.getUserBIndPhone();
        changeNote();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.get().unregister(this);
        unbinder.unbind();
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
    }

    @OnClick({R.id.tv_send_sms, R.id.bt_right_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_send_sms:
                sendMessageCode();
                break;
            case R.id.bt_right_bind:
                if (mAnimation!=null){
                    btRightBind.startAnimation(mAnimation);
                }else {
                    mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
                    btRightBind.startAnimation(mAnimation);
                }
                //绑定
                if (isBind) {
                    tryChangePhone();
                } else {
                    trySubmit();
                }
                break;
        }
    }


    //发送手机验证码
    private void sendMessageCode() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)||!RexUtils.isMobile(phoneNumber)) {
            ToastUtil.showToastLong(getContext(), "请输入手机号码！");
            return;
        }
        registerPresenter.getSms(tvSendSms, phoneNumber);
    }

    //提交绑定手机
    public void trySubmit() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String MsgCode = etPswCode.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)||!RexUtils.isMobile(phoneNumber)) {
            ToastUtil.showToastLong(getContext(), "请输入正确的电话号码");
            return;
        }

        if (TextUtils.isEmpty(MsgCode)) {
            ToastUtil.showToastLong(getContext(), "请输入验证码");
            return;
        }
        userPresenter.binUserPhone(phoneNumber, MsgCode, "");
    }

    //更换绑定手机
    public void tryChangePhone(){
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)){
            ToastUtil.showToastLong(getContext(),"请输入手机号码");
            return;
        }
        RxBus.get().post(ConstantValue.CHANGE_BIND_PHONE,"change");
    }



    void changeNote() {
        String notes = "";
        notes = DepositUtil.getNoteByCode("registerBindPhone", false, 2, true);
        notes = notes.replaceAll("<br>", "\n");
        SpannableStringBuilder mspan = new SpannableStringBuilder("温馨提示：" + notes);
        if (notes.contains("客服")) {
            SpannableString colorSpan = new SpannableString("点击联系在线客服");
            colorSpan.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getActivity().getResources().getColor(R.color.colorPrimary));
                    ds.setUnderlineText(false);
                    ds.clearShadowLayer();
                }

                @Override
                public void onClick(View widget) {
                    ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
                    RxBus.get().post(ConstantValue.EVENT_TYPE_GOTOTAB_SERVICE, "gotoService");
                }
            }, 0, colorSpan.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mspan.append(colorSpan);
        }
        tvTips.setText(mspan);
        tvTips.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void bindUserPhoneSuccess(Object O) {
        HttpResult httpResult = (HttpResult) O;
        if (httpResult.getCode() == 0) {
            userPresenter.getUserBIndPhone();
            return;
        }
        ToastUtil.showToastLong(getContext(), httpResult.getMessage());
    }

    @Override
    public void validationCode(Object O) {

    }

    @Override
    public void getRegisterInfoSuccess(RegisterInfoBean registerInfoBean) {

    }

    @Override
    public void registerSuccess(RegisterBean registerBean) {

    }

    @Override
    public void registerError(String error) {
        ToastUtil.showToastLong(mContext,error);

    }

    @Override
    public void dissmissDialog() {

    }

    @Override
    public void getPhoneNumber(Object O) {
        GetBindUserPhoneNumberBean rdsmsBean = (GetBindUserPhoneNumberBean) O;
        //绑定了手机
        if (!TextUtils.isEmpty(rdsmsBean.getData())) {
            isBind = true;
            llInputCode.setVisibility(View.GONE);
            tvSendSms.setVisibility(View.GONE);
            btRightBind.setText("更换绑定手机号");
            etPhoneNumber.setFocusable(false);
            etPhoneNumber.setText(rdsmsBean.getData());
        } else {
            isBind = false;
            llInputCode.setVisibility(View.VISIBLE);
            tvSendSms.setVisibility(View.VISIBLE);
            btRightBind.setText("立即绑定");
        }
    }

    @Override
    public void isOpenResult(Object o) {
        IsOpenPwdByPhoneBean isOpenPwdByPhoneBean = (IsOpenPwdByPhoneBean)o;
        if ("1".equals(isOpenPwdByPhoneBean.getData())){
            etPhoneNumber.setFocusable(true);
            etPswCode.setFocusable(true);
            llInputCode.setVisibility(View.VISIBLE);
            etPhoneNumber.setBackground(getResources().getDrawable(R.drawable.qt_shape_login_input));
            llInputCode.setVisibility(View.VISIBLE);
            etPswCode.setBackground(getResources().getDrawable(R.drawable.qt_shape_login_input));
        }else {
            etPhoneNumber.setFocusable(false);
            etPswCode.setFocusable(false);
            etPhoneNumber.setBackground(getResources().getDrawable(R.drawable.qt_bind_phone_unuse_shape));
            llInputCode.setVisibility(View.GONE);
            etPswCode.setBackground(getResources().getDrawable(R.drawable.qt_bind_phone_unuse_shape));
        }
        userPresenter.getUserBIndPhone();
    }

//    @Subscribe(tags = {@Tag(ConstantValue.EVENT_DISCONNECT_WEB_SOCKET)})
//    public void bindSuccessInitView(){
//        userPresenter.getUserBIndPhone();
//    }

    @Override
    protected void loadData() {
        RxBus.get().register(this);
    }
}
