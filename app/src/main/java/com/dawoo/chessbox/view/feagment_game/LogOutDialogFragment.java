package com.dawoo.chessbox.view.feagment_game;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.Logout;
import com.dawoo.chessbox.mvp.presenter.UserPresenter;
import com.dawoo.chessbox.mvp.view.ILoginOutView;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.util.cache.GlideCacheUtil;
import com.dawoo.coretool.util.system.SystemUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 退出登录DialogFragment
 */
public class LogOutDialogFragment extends BaseDialogFragment implements ILoginOutView {


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

    private UserPresenter userPresenter;
    private Animation mAnimation;

    @Override
    protected int getViewId() {
        return R.layout.qt_logout_dialog_fragment;
    }

    @Override
    protected void initViews(View view) {
        tvContent.setText("确定退出？");
    }

    @Override
    protected void initData() {
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        userPresenter = new UserPresenter(getContext(), this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
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
                if (mAnimation!=null){
                    btnCancle.startAnimation(mAnimation);
                }else {
                    mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
                    btnCancle.startAnimation(mAnimation);
                }
                dismiss();
                break;
            case R.id.btn_ok:
                if (SystemUtil.isFastClick()) return;
                if (mAnimation!=null){
                    btnOk.startAnimation(mAnimation);
                }else {
                    mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
                    btnOk.startAnimation(mAnimation);
                }
                userPresenter.LoginOut();
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }

    @Override
    public void onClickResult(Object o) {
        Logout logout = (Logout) o;
        if (logout.isSuccess()) {
            GlideCacheUtil.getInstance().clearImageDiskCache(getContext());
            ActivityUtil.logout();
            dismiss();
        } else {
            BaseTipsDialogFragment.newInstance(logout.getMessage().toString(), true).show(getChildFragmentManager(), null);
        }
    }
}
