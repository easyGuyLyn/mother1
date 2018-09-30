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
import android.widget.TextView;

import com.dawoo.chessbox.R;
import com.dawoo.chessbox.util.SingleToast;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 输入安全密码dialog
 */
public class SecurityPasswordDialogFragment extends BaseDialogFragment {

    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.bt_submit)
    Button btSubmit;
    @BindView(R.id.img_close)
    ImageView imgClose;
    SubmitClicked mSubmitClicked;

    private Animation mAnimation;

    public static SecurityPasswordDialogFragment newInstance() {
        SecurityPasswordDialogFragment fragment = new SecurityPasswordDialogFragment();
        return fragment;
    }

    public void setSubmitClicked(SubmitClicked submitClicked) {
        mSubmitClicked = submitClicked;
    }

    @Override
    protected int getViewId() {
        return R.layout.qt_security_password_dialogfragment;
    }

    @Override
    protected void initViews(View view) {
        intScreenWProportion = 0.5;
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
    }

    @Override
    protected void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
    }

    @OnClick({R.id.bt_submit, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                if (mAnimation!=null){
                    btSubmit.startAnimation(mAnimation);
                }else {
                    mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
                    btSubmit.startAnimation(mAnimation);
                }
                if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
                    SingleToast.showMsg("请输入密码");
                    return;
                } else {
                    mSubmitClicked.onSubmitClicked(etPassword.getText().toString().trim());
                    dismiss();
                }
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }

    public interface SubmitClicked {
        void onSubmitClicked(String password);
    }

}
