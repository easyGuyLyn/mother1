package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.chessbox.R;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.SoundUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 活动申请　结果  item_favouavle_progress
 */

public class FavourableReslutFragmentDiaog extends BaseDialogFragment {
    @BindView(R.id.img_status)
    ImageView imgStatus;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_content_service)
    TextView tvContentService;
    @BindView(R.id.img_close)
    ImageView imgClose;
    Unbinder unbinder;

    @Override
    protected int getViewId() {
        return R.layout.favourable_reslut;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void initData() {

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
    }

    @OnClick({R.id.tv_content_service, R.id.img_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_content_service:
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
                ActivityUtil.startServiceDialog(getChildFragmentManager());
                break;
            case R.id.img_close:
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
                dismiss();
                break;
        }
    }
}
