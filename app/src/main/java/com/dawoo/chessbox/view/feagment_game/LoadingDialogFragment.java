package com.dawoo.chessbox.view.feagment_game;

import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dawoo.chessbox.R;
import com.dawoo.chessbox.util.ComplexFragmentManager;
import com.hwangjr.rxbus.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 等待 Dialog
 */
public class LoadingDialogFragment extends BaseDialogFragment {


    @BindView(R.id.loading_pro)
    ImageView loadingPro;
    @BindView(R.id.loading_rotating)
    ImageView loadingRotating;
    Unbinder unbinder;
    ObjectAnimator anim;
    AnimationDrawable animationDrawable;

    @Override
    protected int getViewId() {
        return R.layout.qt_loading_dialogfragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mIsOutCanback = false;
        mIsKeyCanback = false;
        AnimationsStyle = -1;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews(View view) {
        anim = ObjectAnimator.ofInt(loadingPro, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        animationDrawable = (AnimationDrawable) loadingRotating.getDrawable();
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        anim.start();
        animationDrawable.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        anim.cancel();
        animationDrawable.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

}
