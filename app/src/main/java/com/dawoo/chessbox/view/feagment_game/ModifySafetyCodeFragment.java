package com.dawoo.chessbox.view.feagment_game;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
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

import com.dawoo.chessbox.view.view.numberprogressbar.NumberProgressBar;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.ResetSecurityPwd;
import com.dawoo.chessbox.mvp.presenter.UserPresenter;
import com.dawoo.chessbox.mvp.view.IBaseView;
import com.dawoo.chessbox.mvp.view.IModifySecurityPwdView;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.DialogFramentManager;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.view.activity.MainActivity;
import com.dawoo.chessbox.view.fragment.BaseFragment;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 修改安全密码
 */
public class ModifySafetyCodeFragment extends BaseFragment implements IModifySecurityPwdView, IBaseView {

    @BindView(R.id.old_pwd_et)
    EditText mOldPwdEt;
    @BindView(R.id.real_name_et)
    EditText mRrealNameEt;
    @BindView(R.id.new_pwd_et)
    EditText mNewPwdEt;
    @BindView(R.id.confirm_pwd_et)
    EditText mConfirmPwdEt;
    @BindView(R.id.btn_modify)
    Button mLogoutBtn;
    @BindView(R.id.pwd_et)
    EditText mPwdEt;
    @BindView(R.id.etCaptcha)
    EditText mEtCaptcha;
    @BindView(R.id.ivCaptcha)
    ImageView mIvCaptcha;
    Unbinder unbinder;
    @BindView(R.id.rlCaptcha)
    LinearLayout mRlCaptcha;

    private UserPresenter mPresenter;

    private int mUploadType = 1; //0 设置安全密码  1 修改安全密码
    private Boolean mNeedCapcha = false;
    private String mCachaCode = "";
    private String mFirstRealName = "";
    private Boolean mIsSucessSetRealName = true;
    private String mCapChaUrl;
    private boolean isEnterName = false;
    private boolean isSuccess = false;
    SecurityCenterDialogFragment modifySafetyCodeFragment;

    private Animation mAnimation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onStart", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qt_modify_safetycode_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDate();
        RxBus.get().register(this);
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
        if (mAnimation != null) {
            mAnimation.cancel();
            mAnimation = null;
        }
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    @OnClick({R.id.ivCaptcha, R.id.btn_modify})
    public void onViewClicked(View view) {
        if (SystemUtil.isFastClick()) return;
        switch (view.getId()) {
            case R.id.ivCaptcha:
                getCaptcha(mCapChaUrl);
                break;
            case R.id.btn_modify:
                if (mAnimation != null) {
                    mLogoutBtn.startAnimation(mAnimation);
                } else {
                    mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.qt_btn_single_narrow);
                    mLogoutBtn.startAnimation(mAnimation);
                }
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
                doModify();
                break;
        }
    }

    public void initDate() {
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.qt_btn_single_narrow);
        mPresenter = new UserPresenter(getContext(), this);
        modifySafetyCodeFragment = (SecurityCenterDialogFragment) getParentFragment();
        initSafePwd();
    }

    @Override
    public void onInitResult(Object o) {
        ResetSecurityPwd resetSecurityPwd = (ResetSecurityPwd) o;
        if (resetSecurityPwd == null) return;
        if (resetSecurityPwd.getData() != null) {
            Log.e("onInitResult", resetSecurityPwd.getData().toString());
            if (!resetSecurityPwd.getData().getHasPermissionPwd()) {
//                ((ViewGroup) mOldPwdEt.getParent()).setVisibility(View.GONE);
//                ((ViewGroup) mRrealNameEt.getParent()).setVisibility(View.GONE);
//                ((ViewGroup) mNewPwdEt.getParent()).setVisibility(View.GONE);
//                ((ViewGroup) mPwdEt.getParent()).setVisibility(View.VISIBLE);
//                ((ViewGroup) mConfirmPwdEt.getParent()).setVisibility(View.VISIBLE);
                showEditext(false);
                mUploadType = 0;
                if (!resetSecurityPwd.getData().getHasRealName()) {
                    isEnterName = true;
                    setRealName();
                    mLogoutBtn.setText("设    置");
                } else {
                    isEnterName = false;
                    mLogoutBtn.setText("修    改");
                }
            } else if (!resetSecurityPwd.getData().getHasRealName()) {
                showEditext(false);
                setRealName();
                isEnterName = true;
                mLogoutBtn.setText("设    置");
            } else {
                isEnterName = false;
                showEditext(true);
                mLogoutBtn.setText("修    改");
            }
        }
    }


    @Override
    public void onSetRealNameResult(Object o) {
        ResetSecurityPwd resetSecurityPwd = (ResetSecurityPwd) o;
        if (resetSecurityPwd == null) {
            mPresenter.dimissRealNameDialog();
            return;
        }
        //mPresenter.dimissRealNameDialog();
        if (resetSecurityPwd.getCode().equals("0")) {

            if ("请求成功".equals(resetSecurityPwd.getMessage())) {
                // showTips(getString(R.string.action_success));
                // mPresenter.showErrorMsg("操作成功！");
                mPresenter.dimissRealNameDialog();
                mIsSucessSetRealName = true;
            } else {
                //showTips(resetSecurityPwd.getMessage());
                mPresenter.showErrorMsg(resetSecurityPwd.getMessage());
            }


            if (!TextUtils.isEmpty(mFirstRealName) && TextUtils.isEmpty(DataCenter.getInstance().getRealName())) {
                DataCenter.getInstance().setRealName(mFirstRealName);
            }
        } else {
            mIsSucessSetRealName = false;
        }

    }

    @Override
    public void onModifyResult(Object o) {
        ResetSecurityPwd resetSecurityPwd = (ResetSecurityPwd) o;
        if (resetSecurityPwd == null) return;
        if (mUploadType == 0) {
            if (resetSecurityPwd.getCode().equals("0")) {
                if (!TextUtils.isEmpty(mFirstRealName) && TextUtils.isEmpty(DataCenter.getInstance().getRealName())) {
                    DataCenter.getInstance().setRealName(mFirstRealName);
                }
                isSuccess = true;
                //              showTips(getString(R.string.action_success));
                mUploadType = 1;
                initSafePwd();
                modifyInput();

            } else {
                isSuccess = false;
                showTips(resetSecurityPwd.getMessage());
            }
        }
        if (mUploadType == 1) {
            if (resetSecurityPwd.getData() != null) {
                if (null != resetSecurityPwd.getData().getIsOpenCaptcha()
                        && resetSecurityPwd.getData().getIsOpenCaptcha().equals("true")) {
                    mRlCaptcha.setVisibility(View.VISIBLE);
                    mNeedCapcha = true;
                    mCapChaUrl = resetSecurityPwd.getData().getCaptChaUrl();
                    getCaptcha(mCapChaUrl);
                }
            }
            if (resetSecurityPwd.getCode().equals("1308")) {
                mRlCaptcha.setVisibility(View.VISIBLE);
                mNeedCapcha = true;
                mCapChaUrl = resetSecurityPwd.getData().getCaptChaUrl();
                getCaptcha(mCapChaUrl);
            }
            if (resetSecurityPwd.getCode().equals("0")) {
                if (!TextUtils.isEmpty(mFirstRealName) && TextUtils.isEmpty(DataCenter.getInstance().getRealName())) {
                    DataCenter.getInstance().setRealName(mFirstRealName);
                }
                isSuccess = true;
                showTips(getString(R.string.action_success));
                initSafePwd();
                modifyInput();
            } else if ("1303".equals(resetSecurityPwd.getCode())) {
                // 原始密码有误 remindTimes 4
                ResetSecurityPwd.UpdateLoginData data = resetSecurityPwd.getData();
                if (data == null) {
                    return;
                }
                if (0 != data.getRemindTimes()) {
                    isSuccess = false;
                    showTips(resetSecurityPwd.getMessage() + ",你还有" + data.getRemindTimes() + "次机会");
                } else {
                    // 跳转客服
                    ToastUtil.showToastLong(getContext(), "余额已被冻结，请联系客服找回~");
                    ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
                    RxBus.get().post(ConstantValue.EVENT_TYPE_GOTOTAB_SERVICE, "gotoService");
                }

            } else {

                if ("请求成功".equals(resetSecurityPwd.getMessage())) {
                    showTips(getString(R.string.action_success));
                    isSuccess = true;
                } else {
                    isSuccess = false;
                    showTips(resetSecurityPwd.getMessage());
                }

            }
        }
        if (resetSecurityPwd.getCode().equals("1001")) {
            ActivityUtil.gotoLogin();
        }

    }

    @Override
    public void doModify() {
        if (mUploadType == 0) {
            mFirstRealName = DataCenter.getInstance().getRealName();
            if (TextUtils.isEmpty(mFirstRealName)) {
                showTips(getString(R.string.set_real_name_error));
                //ToastUtil.showToastShort(getContext(), getString(R.string.set_real_name_error));
                mPresenter.setRealNameDialog();
                return;
            }
            String firstPwd = mPwdEt.getText().toString().trim();
            String confirmpwd = mConfirmPwdEt.getText().toString().trim();
            if (validate(firstPwd, confirmpwd)) {
                mPresenter.modifySecurityPwd(mNeedCapcha, mFirstRealName, "", firstPwd, confirmpwd, mCachaCode);
            }
        } else if (mUploadType == 1) {
            String realName = mRrealNameEt.getText().toString().trim();
            String oldPwd = mOldPwdEt.getText().toString().trim();
            String newPwd = mNewPwdEt.getText().toString().trim();
            String confirmpwd = mConfirmPwdEt.getText().toString().trim();
            mCachaCode = mEtCaptcha.getText().toString().trim();
            //mLogoutBtn.setText("修改");
            if (validate(realName, oldPwd, newPwd, confirmpwd)) {
                mPresenter.modifySecurityPwd(mNeedCapcha, realName, oldPwd, newPwd, confirmpwd, mCachaCode);
            }
        }
    }

    @Override
    public void initSafePwd() {
        mPresenter.initSecurityPwd();
    }

    @Override
    public void setRealName() {
        mPresenter.setRealNameDialog();
    }

    @Override
    public void backRealName(String name) {
        if (TextUtils.isEmpty(name)) {
            mIsSucessSetRealName = false;
            return;
        }
        mFirstRealName = name;
    }


    private boolean validate(String newPwd, String confirmpwd) {
        if (TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmpwd)) {
            //showTips(getString(R.string.input_cant_null));
            showTips("请输入密码！");
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

        return true;
    }


    private boolean validate(String realName, String oldPwd, String newPwd, String confirmpwd) {
//        if (TextUtils.isEmpty(realName) || TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd) || TextUtils.isEmpty(confirmpwd)) {
//            showTips(getString(R.string.input_cant_null));
//            return false;
//        }

        if (TextUtils.isEmpty(realName)) {
            showTips("请输入真实姓名！");
            return false;
        }

        if (TextUtils.isEmpty(oldPwd)) {
            showTips("请输入旧密码！");
            return false;
        }

        if (TextUtils.isEmpty(newPwd)) {
            showTips("请输入新密码！");
            return false;
        }

        if (TextUtils.isEmpty(confirmpwd)) {
            showTips("请再次输入新密码！");
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

        return true;
    }

    //修改成功　取消输入显示
    public void modifyInput() {
        mOldPwdEt.setText("");
        mRrealNameEt.setText("");
        mOldPwdEt.setText("");
        mNewPwdEt.setText("");
        mConfirmPwdEt.setText("");
        mPwdEt.setText("");
    }

    //显示提示信息
    public void showTips(String tipsContent) {
        BaseTipsDialogFragment baseTipsDialogFragment = BaseTipsDialogFragment.newInstance(tipsContent, false);
        baseTipsDialogFragment.setConfirmClickListener(new BaseTipsDialogFragment.OnSweetClickListener() {
            @Override
            public void onClick(BaseTipsDialogFragment baseTipsDialogFragment) {
                if (!isEnterName) {
                    if (modifySafetyCodeFragment.isDismiss && isSuccess) {
                        baseTipsDialogFragment.dismiss();
                        modifySafetyCodeFragment.dismiss();
                        RxBus.get().post(ConstantValue.SRCOURITY_REFRESH, "SRCOURITY_REFRESH");
                    } else {
                        baseTipsDialogFragment.dismiss();
                    }
                } else {
                    baseTipsDialogFragment.dismiss();
                }
            }
        });
        DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), baseTipsDialogFragment);
    }


    //获取验证码
    private void getCaptcha(String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(url)) return;
                OkHttpUtils.get().url(DataCenter.getInstance().getIp() + url).addParams("_t", String.valueOf(new Date().getTime()))
                        .headers(NetUtil.setHeaders()).build().execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("captcha error ==> " + e.getMessage());
                        showTips(getString(R.string.getCaptchaFail));
                        ///ToastUtil.showResShort(getContext(), R.string.getCaptchaFail);
                    }

                    @Override
                    public void onResponse(Bitmap response, int id) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mIvCaptcha != null)
                                    mIvCaptcha.setImageBitmap(response);
                            }
                        });
                    }
                });
            }
        }).start();
    }

    //是否有安全码
    public void showEditext(boolean hasRealName) {
        if (hasRealName) {
            ((ViewGroup) mOldPwdEt.getParent()).setVisibility(View.VISIBLE);
            ((ViewGroup) mRrealNameEt.getParent()).setVisibility(View.VISIBLE);
            ((ViewGroup) mNewPwdEt.getParent()).setVisibility(View.VISIBLE);
            ((ViewGroup) mPwdEt.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mConfirmPwdEt.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((ViewGroup) mOldPwdEt.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mRrealNameEt.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mNewPwdEt.getParent()).setVisibility(View.GONE);
            ((ViewGroup) mPwdEt.getParent()).setVisibility(View.VISIBLE);
            ((ViewGroup) mConfirmPwdEt.getParent()).setVisibility(View.VISIBLE);
        }

    }

    @Subscribe(tags = {@Tag(ConstantValue.SRCOURITY_REALNAME)})
    public void startService(String aa) {
        if (!mIsSucessSetRealName) {
            setRealName();
        }
    }

}
