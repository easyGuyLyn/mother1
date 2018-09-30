package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.dawoo.chessbox.R;
import com.dawoo.chessbox.util.ComplexFragmentManager;
import com.dawoo.chessbox.util.SoundUtil;
import com.hwangjr.rxbus.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 优惠活动　改版
 */

public class FavourableMessagwCenterDialogFragment extends BaseDialogFragment {
    @BindView(R.id.fragment_content)
    LinearLayout fragmentContent;
    @BindView(R.id.img_title)
    Switch imgTitle;
    @BindView(R.id.tv_tips)
    LinearLayout tvTips;
    @BindView(R.id.img_close)
    ImageView imgClose;
    Unbinder unbinder;
    @BindView(R.id.img_favourable)
    ImageView imgFavourable;
    @BindView(R.id.img_message)
    ImageView imgMessage;
    @BindView(R.id.img_remove)
    ImageView imgRemove;
    @BindView(R.id.rl_table)
    RelativeLayout rlTable;

    private int width;
    private boolean isLeft = true;//是否在左边　默认左边
    private boolean isClickFavourable = true;//优惠活动是否可以点击
    private boolean isClickMessage = true;

    @Override
    protected int getViewId() {
        return R.layout.qt_favourable_message_dialog_fragment;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void initData() {
        intScreenHProportion = 0.93;
        intScreenWProportion = 0.7;
        RxBus.get().register(this);
        //ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), FavourableCenterFragment.class);
        ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), FavourableCenterListFragment.class);
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
        RxBus.get().unregister(this);
    }

    @OnClick(R.id.img_close)
    public void onViewClicked() {
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        dismiss();
    }

    @OnClick({R.id.img_favourable, R.id.img_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_favourable:
                isLeft = false;
                if (isClickFavourable){
                    setImgRemove();
                    isClickMessage = true;
                    isClickFavourable = false;
                }
             //   imgFavourable.setImageResource(R.mipmap.btn_activity);
                imgMessage.setImageResource(R.mipmap.title16nw);

                //   ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), FavourableCenterFragment.class);
                ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), FavourableCenterListFragment.class);
                break;
            case R.id.img_message:
                isLeft = true;
                if (isClickMessage){
                    setImgRemove();
                    isClickFavourable = true;
                    isClickMessage = false;
                }
               // imgMessage.setImageResource(R.mipmap.btn_news);
                imgFavourable.setImageResource(R.mipmap.title15nw);
                ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), MessageAllCenterFragment.class);
                break;
        }
    }

    private void setImgRemove() {
        int widthAll = rlTable.getWidth()-16;
        width = widthAll/2;
        //左边　向右边移动
        if (isLeft){
            Animation animation = new TranslateAnimation(0,width,0,0);
            animation.setDuration(300);
            animation.setFillAfter(true);
            imgRemove.startAnimation(animation);
        }else {
            Animation animation = new TranslateAnimation(width,0,0,0);
            animation.setDuration(300);
            animation.setFillAfter(true);
            imgRemove.startAnimation(animation);

        }

    }
}
