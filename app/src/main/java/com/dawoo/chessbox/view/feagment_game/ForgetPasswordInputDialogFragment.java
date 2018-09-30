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

import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.IsBindUserPhoneBean;
import com.dawoo.chessbox.util.DialogFramentManager;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.view.view.CustomProgressDialog;
import com.google.gson.Gson;

import java.io.IOException;

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
 * 忘记密码　输入姓名
 */

public class ForgetPasswordInputDialogFragment extends BaseDialogFragment {
    @BindView(R.id.et_set_realName)
    EditText etSetRealName;
    @BindView(R.id.ok)
    Button ok;
    @BindView(R.id.ll_tip_bg)
    LinearLayout llTipBg;
    @BindView(R.id.img_close)
    ImageView imgClose;
    Unbinder unbinder;

    private String mIp;
    private CustomProgressDialog customProgressDialog;
    private String inputName;

    private Animation mAnimation;

    @Override
    protected int getViewId() {
        return R.layout.qt_forget_psw_input_fragmentdialog;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void initData() {
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        customProgressDialog = new CustomProgressDialog(getContext());
        mIp = DataCenter.getInstance().getIp();
    }

    public void trySubmint(){
        inputName = etSetRealName.getText().toString().trim();
       if (TextUtils.isEmpty(inputName)||inputName.length()<4){
           ToastUtil.showToastLong(getContext(),"请输入正确账号");
           return;
       }
        getIsBindPhone(inputName);
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

    @OnClick({R.id.ok, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ok:
                if (SystemUtil.isFastClick()) return;
                if (mAnimation!=null){
                    ok.startAnimation(mAnimation);
                }else {
                    mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
                    ok.startAnimation(mAnimation);
                }
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
                trySubmint();
                break;
            case R.id.img_close:
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
                dismiss();
                break;
        }
    }

    private void getIsBindPhone(String enterAccount) {
        customProgressDialog.show();
        String url = mIp + ConstantValue.FIND_PASSWOR_IS_BIND_PHONE;
        OkHttpClient.Builder builder = getOkHttpClientBuilderForHttps();

        RequestBody body = new FormBody.Builder()
                .add("username", enterAccount)
                .build();

        Request request = new Request.Builder().url(url)
                .headers(Headers.of(NetUtil.setHeaders()))
                .post(body)
                .build();

        Call call = builder.build().newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                customProgressDialog.dismiss();
                ToastUtil.showToastLong(getContext(), e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                customProgressDialog.dismiss();
                if (!CheckHasMainEnanceError(response)) {
                    return;
                }
                IsBindUserPhoneBean isBindUserPhoneBean;
                isBindUserPhoneBean = new Gson().fromJson(response.body().string(), IsBindUserPhoneBean.class);
                if (isBindUserPhoneBean!=null){
                    IsBindUserPhoneBean.Data data = isBindUserPhoneBean.getData();
                    //绑定了手机
                    if (!"null".equals(data) && data != null && !TextUtils.isEmpty(data.getPhone())) {
                        if (!TextUtils.isEmpty(data.getEncryptedId()))
                        intentFragment(true,data.getEncryptedId(),data.getPhone());
                    } else {
                        intentFragment(false,"","");
                    }
                }

            }
        });

    }

    private void intentFragment(boolean isBind,String encryptedId,String phoneNumber){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isBind){
                    DialogFramentManager.getInstance().showDialog(getChildFragmentManager(),ForgetPasswordGetMsgCodeDilogFragment.newInstance(encryptedId,phoneNumber,inputName));
                }else {
                    DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), new ForgetPasswordServiceDialogFragment());
                }
            }
        });
    }
}
