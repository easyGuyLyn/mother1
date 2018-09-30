package com.dawoo.chessbox.view.feagment_game;

import android.graphics.Bitmap;
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
import android.widget.TextView;

import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.SPTool;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.UpdateLoginPwd;
import com.dawoo.chessbox.mvp.presenter.UserPresenter;
import com.dawoo.chessbox.mvp.view.IBaseView;
import com.dawoo.chessbox.mvp.view.IModifyLoginPwdView;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.view.fragment.BaseFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 修改密码
 */
public class ModifyPasswordFragment extends BaseFragment implements IModifyLoginPwdView, IBaseView {


    @BindView(R.id.etPsd)
    EditText etPsd;
    @BindView(R.id.ll_now_password)
    LinearLayout llNowPassword;
    @BindView(R.id.etNewPsd)
    EditText etNewPsd;
    @BindView(R.id.ll_new_password)
    LinearLayout llNewPassword;
    @BindView(R.id.etPsdAgin)
    EditText etPsdAgain;
    @BindView(R.id.ll_invent_code)
    LinearLayout llInventCode;
    @BindView(R.id.ivCaptcha)
    ImageView ivCaptcha;
    @BindView(R.id.etCaptcha)
    EditText etCaptcha;
    @BindView(R.id.rlCaptcha)
    LinearLayout rlCaptcha;
    @BindView(R.id.error_tv)
    TextView errorTv;
    @BindView(R.id.btn_modify)
    Button btnModify;
    Unbinder unbinder;

    private UserPresenter mPresenter;
    private Boolean mIsNeedCheckCode = false;
    private int mCount = 0;
    public String newPwd;
    private Animation mAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qt_modify_password_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDate();
        return view;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.onDestory();
        }
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ivCaptcha, R.id.btn_modify})
    public void onViewClicked(View view) {
        if (SystemUtil.isFastClick()) return;
        switch (view.getId()) {
            case R.id.ivCaptcha:
                getCaptcha();
                break;
            case R.id.btn_modify:
                if (mAnimation!=null){
                    btnModify.startAnimation(mAnimation);
                }else {
                    mAnimation = AnimationUtils.loadAnimation(mContext,R.anim.qt_btn_single_narrow);
                    btnModify.startAnimation(mAnimation);
                }
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
                doModify();
                break;
        }
    }

    public void initDate() {
        mAnimation = AnimationUtils.loadAnimation(mContext,R.anim.qt_btn_single_narrow);
        mPresenter = new UserPresenter(getActivity(), ModifyPasswordFragment.this);
    }

    @Override
    public void onModifyResult(Object o) {

        UpdateLoginPwd updateLoginPwd = (UpdateLoginPwd) o;
        if (updateLoginPwd == null) {
            showTips(getString(R.string.http_code_default));
            return;
        }
        if (updateLoginPwd.getCode() != 0) {
            showTips(updateLoginPwd.getMessage());
        } else {
            showTips(getString(R.string.action_success));
            modifyInput();
        }
        if (updateLoginPwd.getData() != null && updateLoginPwd.getData().getIsOpenCaptcha() != null) {
            if (updateLoginPwd.getData().getIsOpenCaptcha().equals("true")) {
                rlCaptcha.setVisibility(View.VISIBLE);
                mIsNeedCheckCode = true;
                getCaptcha();
            }
        }
        if (updateLoginPwd.getCode() == 1308) {
            rlCaptcha.setVisibility(View.VISIBLE);
            mIsNeedCheckCode = true;
            getCaptcha();
        }

        if (updateLoginPwd.getCode() == 1001) {
            ActivityUtil.gotoLogin();
        }


    }

    @Override
    public void doModify() {

        mCount++;

        if (mCount > 2) {
            errorTv.setVisibility(View.VISIBLE);
        }

        String oldPwd = etPsd.getText().toString().trim();
        newPwd = etNewPsd.getText().toString().trim();
        String confirmpwd = etPsdAgain.getText().toString().trim();
        String code = etCaptcha.getText().toString().trim();

        if (validate(oldPwd, newPwd, confirmpwd, code)) {
            if (mIsNeedCheckCode) {
                mPresenter.modifyLoginPwdWithCode(oldPwd, newPwd, code);
            } else {
                mPresenter.modifyLoginPwd(oldPwd, newPwd);
            }
        }

    }

    private boolean validate(String oldPwd, String newPwd, String confirmpwd, String code) {
//        if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmpwd)) {
//            showTips(getString(R.string.input_cant_null));
//            return false;
//        }

        if (TextUtils.isEmpty(oldPwd)) {
            showTips("旧密码不能为空！");
            return false;
        }

        if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmpwd)) {
            showTips("新密码不能为空！");
            return false;
        }

        if (newPwd.length() < 6) {
            showTips(getString(R.string.pwd_at_lest_six));
            return false;
        }

        if (!newPwd.equals(confirmpwd)) {
            showTips(getString(R.string.pwd_input_different));
            return false;
        }
        if (mIsNeedCheckCode) {
            if (TextUtils.isEmpty(code)) {
                showTips(getString(R.string.captcha_hint));
                return false;
            }
        }
        return true;
    }

    //修改成功　取消输入显示
    public void modifyInput() {
        etNewPsd.setText("");
        etCaptcha.setText("");
        etPsd.setText("");
        etPsdAgain.setText("");
    }

    //显示提示信息
    public void showTips(String tipsContent) {
        SPTool.put(getContext(), ConstantValue.KEY_PASSWORD, newPwd);
        DataCenter.getInstance().setPassword(newPwd);
        BaseTipsDialogFragment.newInstance(tipsContent, true).show(getChildFragmentManager(), null);
    }


    //获取验证码
    private void getCaptcha() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().url(DataCenter.getInstance().getIp() + ConstantValue.CAPTCHA_URL).addParams("_t", String.valueOf(new Date().getTime()))
                        .headers(NetUtil.setHeaders()).build().execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("captcha error ==> " + e.getMessage());
                        showTips(getString(R.string.getCaptchaFail));
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (ivCaptcha != null)
                                    ivCaptcha.setImageBitmap(response);
                            }
                        });
                    }
                });
            }
        }).start();
    }
}
