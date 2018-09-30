package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dawoo.chessbox.R;
import com.dawoo.chessbox.adapter.FavourableAdapter.FavourableResultAdapter;
import com.dawoo.chessbox.bean.FavourableReslutBean;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.util.TipsDialogFaragmentUtils;
import com.dawoo.chessbox.view.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 优惠活动弹窗
 */
public class FavourableReslutDIalogFragment extends BaseDialogFragment {

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
    @BindView(R.id.img_title)
    ImageView imgTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    Unbinder unbinder;

    public static final String FAVOURABLEdATA = "FAVOURABLEdATA";

    FavourableReslutBean.Data mData;
    FavourableResultAdapter mAdapter;

    private List<FavourableReslutBean.Data.ApplyDetails> mList = new ArrayList<>();

    public static FavourableReslutDIalogFragment newInstance(FavourableReslutBean.Data favourableReslutBean) {

        Bundle args = new Bundle();
        args.putSerializable(FAVOURABLEdATA, favourableReslutBean);
        FavourableReslutDIalogFragment fragment = new FavourableReslutDIalogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getViewId() {
        return R.layout.favourable_reslut;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void initData() {
        intScreenHProportion = 0.93;
        intScreenWProportion = 0.7;
        mAdapter = new FavourableResultAdapter(getContext(), R.layout.item_favouable_progress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.addItemDecoration(new SpaceItemDecoration(1, 8));
        recyclerview.setAdapter(mAdapter);
        mData = (FavourableReslutBean.Data) getArguments().getSerializable(FAVOURABLEdATA);

        if (mData == null) {
            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), "数据为空！");
            return;
        }
        tvTitle.setText(mData.getActibityTitle());
        tvContent.setText(mData.getApplyResult());

        if (mData.getApplyDetails().size() > 0) {
            mList.addAll(mData.getApplyDetails());
        }
        mAdapter.setNewData(mList);

        //申请状态，1：成功，2：失败，3：部分可领取奖励
        String status = mData.getStatus();
        if (status.equals("1")) {
            getSuccessReslut(mData.getTips());
        }
        if (status.equals("2")) {
            getFailureReslut();
        }

    }

    /**
     * 成功　的页面展示
     */
    private void getSuccessReslut(String tips) {
        tvStatus.setVisibility(View.VISIBLE);
        imgTitle.setImageDrawable(getResources().getDrawable(R.mipmap.title20));
        imgStatus.setImageDrawable(getResources().getDrawable(R.mipmap.success));
        if (!TextUtils.isEmpty(tips)) {
            tvStatus.setText(tips);
        }

    }

    /**
     * 失败　页面展示
     */
    private void getFailureReslut() {
        tvStatus.setVisibility(View.GONE);
        imgTitle.setImageDrawable(getResources().getDrawable(R.mipmap.title21));
        imgStatus.setImageDrawable(getResources().getDrawable(R.mipmap.error));
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
                dismiss();
                break;
            case R.id.img_close:
                SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
                dismiss();
                break;
        }
    }
}
