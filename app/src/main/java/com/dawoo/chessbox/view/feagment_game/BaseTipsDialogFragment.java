package com.dawoo.chessbox.view.feagment_game;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.util.SoundUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BaseTipsDialogFragment extends BaseDialogFragment {

    @BindView(R.id.btn_cancle)
    Button btnCancle;
    @BindView(R.id.btn_ok)
    Button btnOk;
    @BindView(R.id.tv_tips)
    ImageView tvTips;
    @BindView(R.id.ll_tip_bg)
    RelativeLayout llTipBg;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.img_close)
    ImageView imgClose;

    public static final String CONTENT = "CONTENT";
    public static final String ISCANCLE = "ISCANCLE";
    public static final String ISH5 = "ISH5";
    OnSweetClickListener mConfirmClickListener = null; //确认
    OnSweetClickListener mCancelClickListener = null; //取消
    public boolean isCancle = true;
    public boolean isH5 = false;
    public String content;

    private Animation mAnimation;

    public static BaseTipsDialogFragment newInstance(String content, boolean isCancle) {
        Bundle args = new Bundle();
        args.putString(CONTENT, content);
        args.putBoolean(ISCANCLE, isCancle);
        BaseTipsDialogFragment fragment = new BaseTipsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static BaseTipsDialogFragment newInstance(String content, boolean isCancle, boolean isH5) {

        Bundle args = new Bundle();
        args.putString(CONTENT, content);
        args.putBoolean(ISCANCLE, isCancle);
        args.putBoolean(ISH5, isH5);
        BaseTipsDialogFragment fragment = new BaseTipsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!TextUtils.isEmpty(getArguments().getString(CONTENT))) {
            isCancle = getArguments().getBoolean(ISCANCLE);
            content = getArguments().getString(CONTENT);
            isH5 = getArguments().getBoolean(ISH5);
        }
    }

    @Override
    protected int getViewId() {
        return R.layout.qt_tips_dialog_fragment;
    }

    @Override
    protected void initViews(View view) {
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (isH5) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                tvContent.setText(Html.fromHtml(String.valueOf(content), Html.FROM_HTML_MODE_LEGACY));
            else
                tvContent.setText(Html.fromHtml(content));

            // tvContent.setText(Html.fromHtml(content));
        } else {
            tvContent.setText(content);
        }
        if (isCancle) {
            btnCancle.setVisibility(View.VISIBLE);
        } else {
            btnCancle.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @OnClick({R.id.btn_cancle, R.id.btn_ok, R.id.img_close})
    public void onViewClicked(View view) {
        if (view.getId() != R.id.img_close) {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        } else {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        }
        switch (view.getId()) {
            case R.id.btn_cancle:
                startAnimation(btnCancle);
                if (mCancelClickListener == null) {
                    dismiss();
                } else {
                    mCancelClickListener.onClick(this);
                }
                break;
            case R.id.btn_ok:
                startAnimation(btnOk);
                if (mConfirmClickListener == null) {
                    dismiss();
                } else {
                    mConfirmClickListener.onClick(this);
                }

                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }

    private void startAnimation(TextView textView){
        if (mAnimation!=null){
            textView.startAnimation(mAnimation);
        }else{
            mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
            textView.startAnimation(mAnimation);
        }
    }

    public void setConfirmClickListener(OnSweetClickListener listener) {
        mConfirmClickListener = listener;
    }

    public void setCancelClickListener(OnSweetClickListener listener) {
        mCancelClickListener = listener;
    }

    public interface OnSweetClickListener {
        public void onClick(BaseTipsDialogFragment baseTipsDialogFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
    }
}
