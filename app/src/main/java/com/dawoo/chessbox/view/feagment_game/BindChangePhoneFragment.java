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
import android.util.Log;
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
import com.dawoo.chessbox.bean.RegisterBean;
import com.dawoo.chessbox.bean.RegisterInfoBean;
import com.dawoo.chessbox.mvp.presenter.RegisterPresenter;
import com.dawoo.chessbox.mvp.presenter.UserPresenter;
import com.dawoo.chessbox.mvp.view.BindUserPhoneView;
import com.dawoo.chessbox.mvp.view.IRegisterView;
import com.dawoo.chessbox.net.HttpResult;
import com.dawoo.chessbox.util.DepositUtil;
import com.dawoo.chessbox.util.RexUtils;
import com.dawoo.chessbox.view.activity.MainActivity;
import com.dawoo.chessbox.view.fragment.BaseFragment;
import com.hwangjr.rxbus.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BindChangePhoneFragment extends BaseFragment implements BindUserPhoneView, IRegisterView {
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.tv_send_sms)
    TextView tvSendSms;
    @BindView(R.id.et_psw_code)
    EditText etPswCode;
    @BindView(R.id.ll_input_code)
    LinearLayout llInputCode;
    @BindView(R.id.bt_right_bind)
    Button btRightBind;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.et_old_phone)
    EditText etOldPhone;
    Unbinder unbinder;

    private UserPresenter userPresenter;
    private RegisterPresenter registerPresenter;
    private Animation mAnimation;

    protected void initData() {
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        userPresenter = new UserPresenter(getContext(), this);
        registerPresenter = new RegisterPresenter(getContext(), this);
        changeNote();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View view = inflater.inflate(R.layout.qt_bind_change_phone_dialogfragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                String sendMesPhone = etPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(sendMesPhone) || !RexUtils.isMobile(sendMesPhone)) {
                    ToastUtil.showToastLong(getContext(), "请输入正确的手机号码！");
                    return;
                }

                if (TextUtils.isEmpty(sendMesPhone) || !RexUtils.isMobile(sendMesPhone)) {
                    ToastUtil.showToastLong(getContext(), "请输入正确的手机号码！");
                    return;
                }
                sendMessageCode(sendMesPhone);
                break;
            case R.id.bt_right_bind:
                if (mAnimation!=null){
                    btRightBind.startAnimation(mAnimation);
                }else {
                    mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
                    btRightBind.startAnimation(mAnimation);
                }
                trySubmit();
                break;
        }
    }

    //发送手机验证码
    private void sendMessageCode(String sendMesPhone) {
        registerPresenter.getSms(tvSendSms, sendMesPhone);
        Log.e("PHONE", sendMesPhone);
    }


    private void trySubmit() {
        String oldPhone = etOldPhone.getText().toString().trim();
        String newPhone = etPhoneNumber.getText().toString().trim();
        String code = etPswCode.getText().toString().trim();

        if (TextUtils.isEmpty(oldPhone) || !RexUtils.isMobile(oldPhone)) {
            ToastUtil.showToastLong(getContext(), "请输入正确手机号！");
            return;
        }

        if (TextUtils.isEmpty(newPhone) || !RexUtils.isMobile(newPhone)) {
            ToastUtil.showToastLong(getContext(), "请输入正确手机号！");
            return;
        }

        if (oldPhone.equals(newPhone)){
            ToastUtil.showToastLong(getContext(), "旧手机号码与新手机号码相同！");
            return;
        }


        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToastLong(getContext(), "请输入验证码！");
            return;
        }

        userPresenter.binUserPhone(newPhone, code, oldPhone);

    }


    void changeNote() {
        String notes = "";
        notes = DepositUtil.getNoteByCode("bindPhone", false, 2, true);
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
                    // RxBus.get().post(ConstantValue.EVENT_TYPE_GOTOTAB_SERVICE, "gotoService");
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
        if (httpResult.getCode() == 0){
            Log.e("HttpResult","成功"+httpResult.getMessage());
            //跳转页面
            RxBus.get().post(ConstantValue.BIND_NEW_PHONE_SUCCESS, "SUCCESS");
            return;
        }
        ToastUtil.showToastLong(getContext(),httpResult.getMessage());

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

    }

    @Override
    public void dissmissDialog() {

    }

    @Override
    protected void loadData() {

    }
}
