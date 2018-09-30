package com.dawoo.chessbox.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.mvp.model.user.IUserModel;
import com.dawoo.chessbox.mvp.model.user.UserModel;
import com.dawoo.chessbox.mvp.view.AlreadyBindPhoneView;
import com.dawoo.chessbox.mvp.view.BindUserPhoneView;
import com.dawoo.chessbox.mvp.view.IBaseView;
import com.dawoo.chessbox.mvp.view.ILoginOutView;
import com.dawoo.chessbox.mvp.view.ILoginView;
import com.dawoo.chessbox.mvp.view.IModifyLoginPwdView;
import com.dawoo.chessbox.mvp.view.IModifySecurityPwdView;
import com.dawoo.chessbox.mvp.view.IServiceFragmentView;
import com.dawoo.chessbox.mvp.view.IsOpenPwdByPhoneView;
import com.dawoo.chessbox.mvp.view.SetNewPasswordView;
import com.dawoo.chessbox.net.rx.ProgressSubscriber;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.EasyCountDownTimer;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.view.view.CustomDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Subscription;

import static com.dawoo.chessbox.util.NetUtil.getOkHttpClientBuilderForHttps;

/**
 * 用户相关的presenter
 * Created by benson on 18-1-7.
 */

public class UserPresenter<T extends IBaseView> extends BasePresenter {
    private final Context mContext;
    private T mView;
    private final IUserModel mModel;
    private CustomDialog mCustomDialog;
    public EasyCountDownTimer easyCountDownTimer;

    public UserPresenter(Context context, T mView) {
        super(context, mView);

        mContext = context;
        this.mView = mView;
        mModel = new UserModel();
        mCustomDialog = new CustomDialog(mContext, R.style.customIosDialog, R.layout.dialog_input_real_name);
    }


    /**
     * 修改登录密码
     */
    public void modifyLoginPwd(String oldPwd, String newPwd) {
        Subscription subscription = mModel.modifyLoginPwd(new ProgressSubscriber(o ->
                ((IModifyLoginPwdView) mView).onModifyResult(o), mContext), oldPwd, newPwd);
        subList.add(subscription);
    }

    /**
     * 修改登录密码,带验证码
     */
    public void modifyLoginPwdWithCode(String oldPwd, String newPwd, String code) {
        Subscription subscription = mModel.modifyLoginPwd(new ProgressSubscriber(o ->
                ((IModifyLoginPwdView) mView).onModifyResult(o), mContext), oldPwd, newPwd, code);
        subList.add(subscription);
    }


    /**
     * init 安全密码
     */
    public void initSecurityPwd() {
        Subscription subscription = mModel.initSecurityPwd(new ProgressSubscriber(o ->
                ((IModifySecurityPwdView) mView).onInitResult(o), mContext));
        subList.add(subscription);
    }

    /**
     * set realName
     */
    public void setRealName(String realName) {
        Subscription subscription = mModel.setRealSafeName(new ProgressSubscriber(o ->
                ((IModifySecurityPwdView) mView).onSetRealNameResult(o), mContext), realName);
        subList.add(subscription);
    }


    /**
     * 修改安全密码
     */
    public void modifySecurityPwd(Boolean needCaptcha, String realName, String oldPwd, String newPwd, String confirmPwd, String code) {
        Subscription subscription = mModel.modifySecurityPwd(new ProgressSubscriber(o ->
                ((IModifySecurityPwdView) mView).onModifyResult(o), mContext), needCaptcha, realName, oldPwd, newPwd, confirmPwd, code);
        subList.add(subscription);
    }


    /**
     * 输入realName的dialog
     */
    public void setRealNameDialog() {
        showRealNameDialog();
        mCustomDialog.setCanceledOnTouchOutside(false);
        mCustomDialog.setCancelable(false);
        TextView tvOk = mCustomDialog.findViewById(R.id.ok);
        EditText et_set_realName = mCustomDialog.findViewById(R.id.et_set_realName);
        ImageView imgClose = mCustomDialog.findViewById(R.id.img_close);
        et_set_realName.setText("");
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mRealName = et_set_realName.getText().toString().trim();
                if (!TextUtils.isEmpty(mRealName)&&mRealName.length()>=2&&mRealName.length()<30) {
                    setRealName(mRealName);
                    ((IModifySecurityPwdView) mView).backRealName(mRealName);
                } else {
//                    mEtErrorDetial.setVisibility(View.VISIBLE);
//                    mEtErrorDetial.setText(mContext.getString(R.string.bad_realname));
                    //ToastUtil.showToastShort(mContext, mContext.getString(R.string.bad_realname));
                    showErrorMsg(mContext.getString(R.string.bad_realname));
                }
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IModifySecurityPwdView) mView).backRealName("");
                dimissRealNameDialog();
            }
        });

    }

    public void showErrorMsg(String msg){
        if (mCustomDialog!= null){
            TextView mEtErrorDetial = mCustomDialog.findViewById(R.id.tv_error_detail);
            mEtErrorDetial.setVisibility(View.VISIBLE);
            mEtErrorDetial.setText(msg);
        }
    }


    public void LoginOut() {
        Subscription subscription = mModel.logOut(new ProgressSubscriber(o ->
                ((ILoginOutView) mView).onClickResult(o), mContext));
        subList.add(subscription);
    }


    public void showRealNameDialog() {
        if (mCustomDialog != null) {
            mCustomDialog.show();
        }
    }

    public void dimissRealNameDialog() {
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
    }

    /**
     * 验证真实姓名
     */
    public void verifyRealName(String token, String realName, String playerAccount, String playeAccount, String tempPass, String newPassword) {
        Subscription subscription = mModel.verifyRealName(new ProgressSubscriber(o ->
                        ((ILoginView) mView).verifyRealName(o), mContext),
                token,
                realName,
                playerAccount,
                playeAccount,
                tempPass,
                newPassword);
        subList.add(subscription);
    }

    /**
     * 获取客服地址
     */
    public void getCustomerService() {
        Subscription subscription = mModel.getCustomerService(new ProgressSubscriber(o ->
                        ((IServiceFragmentView) mView).onCustomerService(o), mContext));
        subList.add(subscription);
    }


    /**
     *
     * 41.绑定手机号获取用户手机号接口:
     */
    public void getUserBIndPhone(){
        Subscription subscription = mModel.getBindUserPhoneNUmber(new ProgressSubscriber(o ->
                ((AlreadyBindPhoneView) mView).getPhoneNumber(o), mContext));
        subList.add(subscription);
    }

    /**
     * 42.绑定用户手机号接口:
     */

    public void binUserPhone(String phone,String code,String oldPhone){
        Subscription subscription = mModel.bindUserPhone(new ProgressSubscriber(o ->
                ((BindUserPhoneView) mView).bindUserPhoneSuccess(o), mContext),phone,code,oldPhone);
        subList.add(subscription);
    }


    /**
     * 忘记密码  发送验证码
     */
    public void sendSms(Activity activity,TextView textView, String encryptedId, String cookie){
        String url =  DataCenter.getInstance().getIp() + ConstantValue.SEND_MESSAGE_COED;
        OkHttpClient.Builder builder = getOkHttpClientBuilderForHttps();

        RequestBody body = new FormBody.Builder()
                .add("encryptedId", encryptedId)
                .build();

        Request request = new Request.Builder().url(url)
                .headers(Headers.of(NetUtil.setHeaders()))
                .post(body)
                .build();

        Call call = builder.build().newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure","发送失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (easyCountDownTimer == null) {
                            easyCountDownTimer = new EasyCountDownTimer(textView, 90000, 1000);
                        }
                        easyCountDownTimer.start();
                    }
                });
            }
        });
    }


    /**
     * 忘记密码验证验证码
     */

    public void validationCode(String code){
        Subscription subscription = mModel.validationCode(new ProgressSubscriber(o ->
                ((BindUserPhoneView) mView).validationCode(o), mContext),code);
        subList.add(subscription);
    }

    /**
     * 修改登录密码
     */
    public void modifyLoginPassword(String username,String newPassword){
        Subscription subscription = mModel.modifyLoginPassword(new ProgressSubscriber(o ->
                ((SetNewPasswordView) mView).modifyLoginPassword(o), mContext),username,newPassword);
        subList.add(subscription);
    }

    /**
     * 判断是否显示手机找回密码功能
     */
    public void isOPenPwdByPhone(){
        Subscription subscription = mModel.isOpenPwdByPhone(new ProgressSubscriber(o ->
                ((IsOpenPwdByPhoneView) mView).isOpenResult(o), mContext));
        subList.add(subscription);
    }

    @Override
    public void onDestory() {
        super.onDestory();
        if (mCustomDialog != null) {
            mCustomDialog.cancel();
            mCustomDialog = null;
        }

    }

}
