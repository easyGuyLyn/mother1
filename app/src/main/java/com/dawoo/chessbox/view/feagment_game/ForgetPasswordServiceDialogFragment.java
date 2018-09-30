package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.coretool.util.SPTool;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.SoundUtil;

import java.sql.SQLOutput;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ForgetPasswordServiceDialogFragment extends BaseDialogFragment {
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.btn_cancle)
    Button btnCancle;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.tv_tips)
    ImageView tvTips;
    @BindView(R.id.ll_tip_bg)
    RelativeLayout llTipBg;
    @BindView(R.id.img_close)
    ImageView imgClose;
    Unbinder unbinder;

    private Animation mAnimation;

    @Override
    protected int getViewId() {
        return R.layout.qt_tips_dialog_fragment;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void initData() {
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        tvContent.setText(R.string.qt_unbind_phone);
        btnCancle.setVisibility(View.GONE);
        tvTips.setImageDrawable(getResources().getDrawable(R.mipmap.title18));
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

    @OnClick({ R.id.btn_ok, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
                startAnimation(btnOk);
                ActivityUtil.startServiceDialog(getChildFragmentManager());
                break;
            case R.id.img_close:
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
                dismiss();
                break;
        }
    }

    private void startAnimation(TextView textView){
        if (mAnimation!=null){
            textView.startAnimation(mAnimation);
        }else {
            mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
            textView.startAnimation(mAnimation);
        }
    }
}
