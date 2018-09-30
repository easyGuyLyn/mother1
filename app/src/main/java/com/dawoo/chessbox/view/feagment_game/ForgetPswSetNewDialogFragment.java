package com.dawoo.chessbox.view.feagment_game;

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

import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.coretool.util.SPTool;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.IsBindUserPhoneBean;
import com.dawoo.chessbox.mvp.presenter.UserPresenter;
import com.dawoo.chessbox.mvp.view.SetNewPasswordView;
import com.dawoo.chessbox.util.SoundUtil;
import com.hwangjr.rxbus.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 忘记密码　设置新密码
 */
public class ForgetPswSetNewDialogFragment extends BaseDialogFragment implements SetNewPasswordView {
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.et_psw_again)
    EditText etPswAgain;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.ll_tip_bg)
    LinearLayout llTipBg;
    @BindView(R.id.img_close)
    ImageView imgClose;
    Unbinder unbinder;

    private UserPresenter userPresenter;
    private String name;
    private String psw;

    private Animation mAnimation;

    public static ForgetPswSetNewDialogFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString(ForgetPasswordGetMsgCodeDilogFragment.NAME, name);
        ForgetPswSetNewDialogFragment fragment = new ForgetPswSetNewDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getViewId() {
        return R.layout.qt_forget_psw_set_new_dialogfragment;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void initData() {
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        userPresenter = new UserPresenter(getContext(), this);
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

    @OnClick({R.id.btn_next, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
                if (mAnimation!=null){
                    btnNext.startAnimation(mAnimation);
                }else {
                    mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
                    btnNext.startAnimation(mAnimation);
                }
                trySubmit();
                break;
            case R.id.img_close:
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
                dismiss();
                break;
        }
    }

    public void trySubmit() {
        psw = etPsw.getText().toString().trim();
        String pswNew = etPswAgain.getText().toString().trim();
        name = getArguments().getString(ForgetPasswordGetMsgCodeDilogFragment.NAME);

        if (TextUtils.isEmpty(psw) || TextUtils.isEmpty(pswNew)||pswNew.length()<6||psw.length()<6) {
            ToastUtil.showToastLong(getContext(), "请输入密码！");
            return;
        }

        if (!psw.equals(pswNew)) {
            ToastUtil.showToastLong(getContext(), "请输入相同的密码！");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToastLong(getContext(), "用户姓名为空！");
            return;
        }
        userPresenter.modifyLoginPassword(name, psw);
    }

    @Override
    public void modifyLoginPassword(Object O) {
        IsBindUserPhoneBean isBindUserPhoneBean = (IsBindUserPhoneBean) O;
        if (isBindUserPhoneBean.getCode() == 0) {
            //返回自动登录
            DataCenter.getInstance().getUser().setUsername(name);
            DataCenter.getInstance().getUser().setPassword(psw);
            SPTool.put(getContext(), ConstantValue.KEY_REMEMBER_PWD, true);
            SPTool.put(getContext(), ConstantValue.KEY_USERNAME, name);
            SPTool.put(getContext(), ConstantValue.KEY_PASSWORD, psw);
            RxBus.get().post(ConstantValue.QT_EVEBT_REHIST_SECCESS, "login");
            return;
        }
        ToastUtil.showToastLong(getContext(), isBindUserPhoneBean.getMessage());
    }

    @Override
    public void bindPhoneState(Object O) {

    }
}
