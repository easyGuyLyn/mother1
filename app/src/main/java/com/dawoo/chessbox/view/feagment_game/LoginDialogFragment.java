package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.util.ComplexFragmentManager;
import com.dawoo.chessbox.util.SoundUtil;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

/**
 * 登录dialog
 */
public class LoginDialogFragment extends BaseDialogFragment implements View.OnClickListener {

    private TextView mLogin;
    private TextView mRegister;
    private ImageView close;
    private ImageView mTitle;
    private Animation mAnimation;


    @Override
    protected int getViewId() {
        return R.layout.qt_dialogfragment_login;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
    }

    @Override
    protected void initViews(View view) {

        intScreenHProportion = 0.93;
        intScreenWProportion = 0.7;
        mLogin = view.findViewById(R.id.tv_login);
        mRegister = view.findViewById(R.id.tv_register);
        close = view.findViewById(R.id.img_close);
        mTitle = view.findViewById(R.id.img_title);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        close.setOnClickListener(this);
        mLogin.setSelected(true);
        mRegister.setSelected(false);

    }

    @Override
    protected void initData() {
        mAnimation = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.qt_btn_single_narrow);
        ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), LoginFragment.class);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
        ComplexFragmentManager.getInstance().clear();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.img_close) {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        } else {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        }
        switch (view.getId()) {
            case R.id.tv_login:
                if (mAnimation != null) {
                    mLogin.startAnimation(mAnimation);
                }
                mLogin.setSelected(true);
                mRegister.setSelected(false);
                mTitle.setImageDrawable(getResources().getDrawable(R.mipmap.title01));
                ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), LoginFragment.class);
                break;
            case R.id.tv_register:
                if (mAnimation != null) {
                    mRegister.startAnimation(mAnimation);
                }
                mLogin.setSelected(false);
                mRegister.setSelected(true);
                mTitle.setImageDrawable(getResources().getDrawable(R.mipmap.title02));
                ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), RegisterFragment.class);
                break;
            case R.id.img_close:
                if (mAnimation!=null){
                    close.startAnimation(mAnimation);
                }
                dismiss();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.get().unregister(this);
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_LOGINED)})
    public void closeDialog(String sting) {
        dismiss();
    }

    @Subscribe(tags = {@Tag(ConstantValue.QT_EVEBT_REHIST_SECCESS)})
    public void loginShow(String sting) {
        ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), LoginFragment.class);
        mLogin.setSelected(true);
        mRegister.setSelected(false);
    }
}
