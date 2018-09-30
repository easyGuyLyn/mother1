package com.dawoo.chessbox.view.feagment_game;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Annotation;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.IsOpenPwdByPhoneBean;
import com.dawoo.chessbox.bean.LoginBean;
import com.dawoo.chessbox.bean.VerifyRealNameBean;
import com.dawoo.chessbox.mvp.presenter.UserPresenter;
import com.dawoo.chessbox.mvp.view.ILoginView;
import com.dawoo.chessbox.mvp.view.IsOpenPwdByPhoneView;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.DialogFramentManager;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.SingleToast;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.util.TipsDialogFaragmentUtils;
import com.dawoo.chessbox.view.fragment.BaseFragment;
import com.dawoo.chessbox.view.view.CustomProgressDialog;
import com.dawoo.chessbox.view.view.InputBoxDialog;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.SPTool;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.system.SystemUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.tencent.smtt.sdk.CookieManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.dawoo.chessbox.util.AutoLogin.CheckHasMainEnanceError;
import static com.dawoo.chessbox.util.NetUtil.getOkHttpClientBuilderForHttps;

/**
 * 登录Fragment
 */
public class LoginFragment extends BaseFragment implements ILoginView, IsOpenPwdByPhoneView {

    Bundle bundle;
    String string = "";
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.etUsername)
    EditText mEtUsername;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.etCaptcha)
    EditText mEtCaptcha;
    @BindView(R.id.ivCaptcha)
    ImageView mIvCaptcha;
    @BindView(R.id.rlCaptcha)
    LinearLayout mRlCaptcha;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;
    @BindView(R.id.forget_pwd_tv)
    TextView forgetPwdTv;
    @BindView(R.id.cb_password)
    CheckBox cbPassword;
    Unbinder unbinder;

    private String mIp;
    private boolean needCaptcha;
    private int mMCode;  //登录返回状态码
    private String successUserName = "";
    private String successUserPwd = "";
    private Response mResponse;
    private boolean isRememberPwd = true;
    private CustomProgressDialog mProgressDialog;
    private Handler mHandler;
    private UserPresenter userPresenter;
    private LoadingDialogFragment pd;
    private Animation mAnimation;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment testFragment = new LoginFragment();
        // 步骤4:创建Bundle对象
        Bundle bundle = new Bundle();

        // 步骤5:往bundle中添加数据
        bundle.putString("message", "登录");

        // 步骤6:把数据设置到Fragment中
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qt_login_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }
    // 作用:存储数据，并传递到Fragment中

    @Override
    protected void loadData() {
        pd = new LoadingDialogFragment();
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.qt_btn_single_narrow);
        mAnimation.setFillAfter(true);
        userPresenter = new UserPresenter(getContext(), this);
        mHandler = new Handler();
        mIp = DataCenter.getInstance().getIp();
        RxBus.get().register(this);
        setSwitchCompat();
        // 填充输入框
        fillInput();
        getSID();
        boolean isNeedVc = (boolean) SPTool.get(getContext(), ConstantValue.KEY_NEED_CAPTCHA, false);
        if (isNeedVc) {
            getCaptcha();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
        RxBus.get().unregister(this);
        unbinder.unbind();
    }

    private void fillInput() {
        isRememberPwd = (Boolean) SPTool.get(getContext(), ConstantValue.KEY_REMEMBER_PWD, true);
        cbPassword.setChecked(isRememberPwd);
        String spUsername = (String) SPTool.get(getContext(), ConstantValue.KEY_USERNAME, "");
        mEtUsername.setText(spUsername);
        if (isRememberPwd) {
            String spPassword = (String) SPTool.get(getContext(), ConstantValue.KEY_PASSWORD, "");
            if (!TextUtils.isEmpty(spPassword)) {
                mEtPassword.setText(spPassword);
            } else {
                if (!TextUtils.isEmpty(spUsername)) {
                    mEtPassword.requestFocus();
                }
            }
        }


        needCaptcha = (boolean) SPTool.get(getContext(), ConstantValue.KEY_NEED_CAPTCHA, false);
        if (needCaptcha) {
            long now = System.currentTimeMillis();
            long date = (long) SPTool.get(getContext(), ConstantValue.KEY_CAPTCHA_TIME, now);
            if (now - date < 30 * 60 * 1000) {
                mRlCaptcha.setVisibility(View.VISIBLE);
                mTvCode.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getSID() {
        CookieManager.getInstance().setCookie(mIp, "");
        String url = mIp + ConstantValue.GET_SID;
        OkHttpClient.Builder builder = getOkHttpClientBuilderForHttps();

        Request request = new Request.Builder().url(url)
                .headers(Headers.of(NetUtil.setHeaders()))
                .get()
                .build();
        Call call = builder.build().newCall(request);
        try {

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    NetUtil.setCookie(response);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //获取验证码
    private void getCaptcha() {
        OkHttpUtils.get().url(mIp + ConstantValue.CAPTCHA_URL).addParams("_t", String.valueOf(System.currentTimeMillis()))
                .headers(NetUtil.setHeaders()).build().execute(new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.e("captcha error ==> " + e.getMessage());
                ToastUtil.showResShort(getContext(), R.string.getCaptchaFail);
            }

            @Override
            public void onResponse(Bitmap response, int id) {
                if (mIvCaptcha != null && response != null) {
                    mIvCaptcha.setImageBitmap(response);
                }

            }

            @Override
            public boolean validateReponse(Response response, int id) {
                return super.validateReponse(response, id);
            }
        });
    }


    @OnClick({R.id.ivCaptcha, R.id.btnLogin, R.id.forget_pwd_tv})
    public void onViewClicked(View view) {
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        switch (view.getId()) {
            case R.id.ivCaptcha:
                getCaptcha();
                break;
            case R.id.btnLogin:
                if (SystemUtil.isFastClick()) return;
                if (mAnimation!=null){
                    mBtnLogin.startAnimation(mAnimation);
                }
                doOnLogin();
                break;
            case R.id.forget_pwd_tv:
                if (SystemUtil.isFastClick()) return;
                userPresenter.isOPenPwdByPhone();
                break;
        }
    }

    @Override
    public void doOnLogin() {
        String name = mEtUsername.getText().toString().trim();
        String pwd = mEtPassword.getText().toString().trim();
        String captcha = mEtCaptcha.getText().toString().trim();
        successUserName = name;
        successUserPwd = pwd;

        if (validate(name, pwd, captcha)) {
            // mLoginPresenter.doLogin(name, pwd, new ProgressSubscriber(subcriberOnNextListner, this));
            //EasyProgressDialog.with(getContext()).showProgress();
            showEasyProgress();
            String url = mIp + ConstantValue.LOGIN_URL;
            OkHttpClient.Builder builder = getOkHttpClientBuilderForHttps();

            RequestBody body = new FormBody.Builder()
                    .add("username", name)
                    .add("password", pwd)
                    .add("captcha", captcha).build();

            Request request = new Request.Builder().url(url)
                    .headers(Headers.of(NetUtil.setHeaders()))
                    .post(body)
                    .build();

            Call call = builder.build().newCall(request);
            try {

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //uiToast("" + e.getMessage());
                        uiToast("网络异常，请点击重试！");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // EasyProgressDialog.with(getContext()).dismissProgress();
                                closeProgress();
                            }
                        });
                        LogUtils.e("login Error ==> " + e.getLocalizedMessage());

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!CheckHasMainEnanceError(response)) {
                            return;
                        }
                        mResponse = response;
                        mResponse = response;
                        mMCode = response.code();
                        if (mMCode < 200 || mMCode > 302) {
                            LogUtils.e("login Error ==> " + response.message());
                            //SingleToast.showMsg("" + response.message());
                            uiToast(response.message());
                            // EasyProgressDialog.with(getContext()).dismissProgress();
                            closeProgress();
                            return;
                        }
                        final String jsonData = response.body().string();
                        Log.e("登录中返回报文：", jsonData);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleLogin(jsonData, response);

                            }
                        });
                    }
                });
            } catch (Exception e) {
                LogUtils.e("login Error ==> " + e.getLocalizedMessage());
                //ToastUtil.showToastShort(LoginActivity.this, "" + e.getMessage());
                ToastUtil.showToastShort(getContext(), "网络异常，请点击重试！");
                // EasyProgressDialog.with(getContext()).dismissProgress();
                closeProgress();
            }
        }
    }

    @Override
    public void verifyRealName(Object o) {

    }

    /**
     * 表单验证
     */
    public boolean validate(String username, String password, String captcha) {
        if (TextUtils.isEmpty(username)) {
            getFocusable(mEtUsername);
            ToastUtil.showResShort(getContext(), R.string.username_hint);
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            getFocusable(mEtPassword);
            ToastUtil.showResShort(getContext(), R.string.password_hint);
            return false;
        }

        if (needCaptcha && captcha.length() != 4) {
            getFocusable(mEtCaptcha);

            ToastUtil.showResShort(getContext(), R.string.enter_captcha);
            if (mRlCaptcha.getVisibility() == View.GONE) {
                mRlCaptcha.setVisibility(View.VISIBLE);
                mTvCode.setVisibility(View.VISIBLE);
            }
            getCaptcha();
            return false;
        }

        return true;
    }

    private void getFocusable(EditText view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    private void uiToast(String string) {
        TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), string, true);
        // ToastUtil.showToastLong(getContext(), sring);
    }


    /**
     * 处理登录
     */
    private void handleLogin(String jsonData, Response response) {
        LoginBean loginBean = null;
        try {
            loginBean = new Gson().fromJson(jsonData, LoginBean.class);
            needCaptcha = loginBean.isIsOpenCaptcha();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            SingleToast.showMsg("网络异常，请重试！");
            // SingleToast.showMsg(e.getMessage());
        }

        if (loginBean == null) {
            return;
        }

        if (loginBean.isSuccess()) {
            if (mMCode == 302 && !TextUtils.isEmpty(loginBean.getPropMessages().getLocation())) {
                InputBoxDialog inputBoxDialog = new InputBoxDialog(getContext(), R.style.CommonHintDialog);
                inputBoxDialog.show();
                LoginBean finalLoginBean = loginBean;
                inputBoxDialog.setOkonClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String realName = inputBoxDialog.mEtSetRealName.getText().toString().trim();
                        if (!TextUtils.isEmpty(realName)) {
//                            mLoginPresenter.verifyRealName(loginBean.getPropMessages().getGbToken(), realName, successUserName, successUserName, successUserPwd, successUserPwd);
                            inputBoxDialog.dismiss();
                            // EasyProgressDialog.with(getContext()).showProgress();
                            showEasyProgress();
                            String url = mIp + ConstantValue.REAL_NAME_URL;
                            OkHttpClient.Builder builder = getOkHttpClientBuilderForHttps();

                            RequestBody body = new FormBody.Builder()
                                    .add("gb.token", finalLoginBean.getPropMessages().getGbToken())
                                    .add("result.realName", realName)
                                    .add("needRealName", "yes")
                                    .add("result.playerAccount", successUserName)
                                    .add("search.playerAccount", successUserName)
                                    .add("tempPass", successUserPwd)
                                    .add("newPassword", successUserPwd)
                                    .add("passLevel", "20").build();

                            Request request = new Request.Builder().url(url)
                                    .headers(Headers.of(NetUtil.setHeaders()))
                                    .post(body)
                                    .build();

                            Call call = builder.build().newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    //uiToast("" + e.getMessage());
                                    uiToast("网络异常，请点击重试！");
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //  EasyProgressDialog.with(getContext()).dismissProgress();
                                            closeProgress();
                                        }
                                    });

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    // EasyProgressDialog.with(getContext()).dismissProgress();
                                    closeProgress();
                                    mResponse = response;
                                    mMCode = response.code();
                                    if (mMCode != 200) {
                                        uiToast(getString(R.string.verify_name_error));
                                        return;
                                    }
                                    final String jsonData = response.body().string();
                                    Log.e("验证中返回报文：", jsonData);
                                    if (!TextUtils.isEmpty(jsonData)) {
                                        VerifyRealNameBean verifyRealNameBean = new Gson().fromJson(jsonData, VerifyRealNameBean.class);
                                        if (verifyRealNameBean != null) {
                                            if (!verifyRealNameBean.getData().isNameSame()) {
                                                uiToast(getString(R.string.real_name_error));
                                            } else if (verifyRealNameBean.getData().isConflict())
                                                uiToast(getString(R.string.account_existence));
                                            else {
                                                uiToast(getString(R.string.verify_name_ok));
                                                needCaptcha = false;
                                                doOnLogin();
                                            }

                                        }
                                    } else
                                        uiToast(getString(R.string.verify_name_error));

                                }
                            });
                        }
                    }
                });

            } else
                loginSuccess(response);
        } else {
            String message = loginBean.getMessage() + "";
            if (loginBean.getPropMessages().getCaptcha() != null) {
                message = loginBean.getPropMessages().getCaptcha();
            }
            uiToast(message);
            //ToastUtil.showToastShort(getContext(), message);
        }
        if (loginBean.isIsOpenCaptcha()) {
            SPTool.put(getContext(), ConstantValue.KEY_NEED_CAPTCHA, true);
            SPTool.put(getContext(), ConstantValue.KEY_CAPTCHA_TIME, new Date().getTime());
            mRlCaptcha.setVisibility(View.VISIBLE);
            mTvCode.setVisibility(View.VISIBLE);
            getCaptcha();
        }
        closeProgress();
        //EasyProgressDialog.with(getContext()).dismissProgress();
    }


    /**
     * 登录成功
     */
    private void loginSuccess(Response response) {

        SPTool.put(getContext(), ConstantValue.KEY_NEED_CAPTCHA, false);
        SPTool.remove(getContext(), ConstantValue.KEY_CAPTCHA_TIME);

        NetUtil.setCookie(response);

        DataCenter.getInstance().setLogin(true);
        DataCenter.getInstance().setUserName(successUserName);
        DataCenter.getInstance().setPassword(successUserPwd);

        // 用来自动登录
        SPTool.put(getContext(), ConstantValue.KEY_USERNAME_AUTO_LOGIN, successUserName);
        SPTool.put(getContext(), ConstantValue.KEY_PASSWORD_AUTO_LOGIN, successUserPwd);
        SPTool.put(getContext(), ConstantValue.KEY_TIME_AUTO_LOGIN, System.currentTimeMillis());

        // 用来记住密码
        SPTool.put(getContext(), ConstantValue.KEY_USERNAME, successUserName);
        if (isRememberPwd) {
            SPTool.put(getContext(), ConstantValue.KEY_PASSWORD, successUserPwd);
        }

        notifyLogined();
        //   BaseTipsDialogFragment.newInstance(response.message(), true).show(getActivity().getSupportFragmentManager(), "");

    }

    void notifyLogined() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RxBus.get().post(ConstantValue.EVENT_TYPE_LOGINED, "login");
            }
        }, 500);
    }

    @Subscribe(tags = {@Tag(ConstantValue.QT_EVEBT_REHIST_SECCESS)})
    public void tryLogin(String sting) {
        fillInput();
        doOnLogin();
    }

    public void setSwitchCompat() {
        cbPassword.setChecked(true);
        cbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRememberPwd = isChecked;
                SPTool.put(getContext(), ConstantValue.KEY_REMEMBER_PWD, isRememberPwd);
            }
        });
    }

    @Override
    public void isOpenResult(Object o) {
        IsOpenPwdByPhoneBean isOpenPwdByPhoneBean = (IsOpenPwdByPhoneBean) o;
        if ("1".equals(isOpenPwdByPhoneBean.getData())) {
            DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), new ForgetPasswordInputDialogFragment());
        } else {
            ActivityUtil.startServiceDialog(getChildFragmentManager());
        }
    }

    //显示进度框
    private void showEasyProgress() {
        if (getContext() != null) {
            if (pd == null) {
                pd = new LoadingDialogFragment();
            }
            DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), pd);
            //EasyProgressDialog.with(getContext()).showProgress();
        }
    }

    //关闭进度框
    private void closeProgress() {
        if (getContext() != null && pd != null) {
            pd.dismiss();
            pd = null;
            //EasyProgressDialog.with(getContext()).dismissProgress();
        }
    }
}
