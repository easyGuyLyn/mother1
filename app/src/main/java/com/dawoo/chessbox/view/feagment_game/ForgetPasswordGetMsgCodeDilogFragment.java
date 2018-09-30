package com.dawoo.chessbox.view.feagment_game;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.IsBindUserPhoneBean;
import com.dawoo.chessbox.mvp.presenter.UserPresenter;
import com.dawoo.chessbox.mvp.view.BindUserPhoneView;
import com.dawoo.chessbox.util.MyCountDownTimer;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.SoundUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 忘记密码　发送验证码
 */
public class ForgetPasswordGetMsgCodeDilogFragment extends BaseDialogFragment implements BindUserPhoneView {
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_sms)
    TextView tvSendSms;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.ll_tip_bg)
    LinearLayout llTipBg;
    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.et_phone_code)
    EditText etPhoneCode;
    Unbinder unbinder;

    public static final String ENCRYPTEDID = "encryptedId";
    public static final String PHONENUMBER = "phonenumber";
    public static final String NAME = "name";

    MyCountDownTimer myCountDownTimer;
    UserPresenter userPresenter;

    private String cookie;
    private String mIp;
    private String encryptedId;

    private Animation mAnimation;

    public static ForgetPasswordGetMsgCodeDilogFragment newInstance(String encryptedId, String number,String name) {

        Bundle args = new Bundle();
        args.putString(ENCRYPTEDID, encryptedId);
        args.putString(PHONENUMBER, number);
        args.putString(NAME,name);
        ForgetPasswordGetMsgCodeDilogFragment fragment = new ForgetPasswordGetMsgCodeDilogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getViewId() {
        return R.layout.qt_forget_psw_msgcode_dialogfragment;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void initData() {
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        userPresenter = new UserPresenter(getContext(),this);
        mIp = DataCenter.getInstance().getIp();
        cookie = NetUtil.setHeaders().get("Cookie");
        if (cookie == null) {
            cookie = "";
        }
        encryptedId = getArguments().getString(ENCRYPTEDID);
        tvPhoneNumber.setText(getArguments().getString(PHONENUMBER));
        tvPhoneNumber.setFocusable(false);
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
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
    }

    @OnClick({R.id.tv_sms, R.id.btn_next, R.id.img_close})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.img_close) {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        } else {
            if (SystemUtil.isFastClick()) return;
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        }
        switch (view.getId()) {
            case R.id.tv_sms:
                sendMessageCode();
                break;
            case R.id.btn_next:
                if (mAnimation!= null){
                    btnNext.startAnimation(mAnimation);
                }else {
                    mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
                    btnNext.startAnimation(mAnimation);
                }
                validationCode();
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }

    //发送手机验证码
    private void sendMessageCode() {
        userPresenter.sendSms(getActivity(),tvSendSms, encryptedId, cookie);
    }


    //验证手机号码  验证码
    private void validationCode() {
        String code = etPhoneCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToastLong(getContext(), "请输入验证码！");
            return;
        }
        userPresenter.validationCode(code);
    }

    @Override
    public void bindUserPhoneSuccess(Object O) {

    }

    @Override
    public void validationCode(Object O) {
        IsBindUserPhoneBean isBindUserPhoneBean = (IsBindUserPhoneBean) O;
        if (isBindUserPhoneBean.getCode() == 0) {
            ForgetPswSetNewDialogFragment.newInstance(getArguments().getString(NAME)).show(getChildFragmentManager(),null);
            return;
        }
        ToastUtil.showToastLong(getContext(), isBindUserPhoneBean.getMessage());
    }
}
