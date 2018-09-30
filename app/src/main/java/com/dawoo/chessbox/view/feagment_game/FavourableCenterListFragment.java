package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.adapter.FavourableAdapter.FavourableTitleAdapter;
import com.dawoo.chessbox.adapter.FavourableAdapter.HorizontalRcyFavourableAdapter;
import com.dawoo.chessbox.bean.FavourableCenterBean;
import com.dawoo.chessbox.bean.FavourableDetailBean;
import com.dawoo.chessbox.bean.FavourableReslutBean;
import com.dawoo.chessbox.mvp.presenter.PromoFramentPresenter;
import com.dawoo.chessbox.mvp.view.FavourableCenterView;
import com.dawoo.chessbox.net.GlideApp;
import com.dawoo.chessbox.util.DialogFramentManager;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.util.TipsDialogFaragmentUtils;
import com.dawoo.chessbox.util.htmlimage.HtmlUtils;
import com.dawoo.chessbox.view.fragment.BaseFragment;
import com.dawoo.chessbox.view.view.SpaceItemDecoration;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.system.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 优惠中心　原生版本
 */
public class FavourableCenterListFragment extends BaseFragment implements FavourableCenterView {


    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.horizontal_recycle)
    RecyclerView mhorizontalRecycle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.img_title)
    ImageView imgTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_activity_detail)
    TextView tvActivityDetail;
    @BindView(R.id.img_no_date)
    ImageView imgNoDate;
    @BindView(R.id.tv_apply)
    TextView tvApply;
    @BindView(R.id.scrollable)
    ScrollView scrollable;
    @BindView(R.id.img_empty)
    ImageView imgEmpty;
    @BindView(R.id.tv_title_time)
    TextView tvTitleTime;
    @BindView(R.id.tv_title_detail)
    TextView tvTitleDetail;
    Unbinder unbinder;

    private Animation mAnimation;

    FavourableTitleAdapter favourableTitleAdapter;
    HorizontalRcyFavourableAdapter horizontalRcyFavourableAdapter;


    private PromoFramentPresenter mPresenter;
    List<FavourableCenterBean.Data> dataArrayList = new ArrayList<>(); //纵向listview
    List<FavourableCenterBean.Data.ActivityList> mdataList = new ArrayList<>();//横向listview

    private String searchId = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qt_favourable_center_list_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initDate();
        return view;
    }

    private void initDate() {
        mPresenter = new PromoFramentPresenter(getContext(), this);
        favourableTitleAdapter = new FavourableTitleAdapter(getContext(), R.layout.qt_item_favourable);
        horizontalRcyFavourableAdapter = new HorizontalRcyFavourableAdapter(getContext(), R.layout.qt_item_horizontal_favourable);
        mhorizontalRecycle.addItemDecoration(new SpaceItemDecoration(1, 8));
        mAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.qt_btn_single_narrow);
        LinearLayoutManager ms = new LinearLayoutManager(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ms.setOrientation(LinearLayoutManager.VERTICAL);// 设置 recyclerview 布局方式为横向布局
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleView.setLayoutManager(ms);
        mhorizontalRecycle.setLayoutManager(layoutManager);
        recycleView.setAdapter(horizontalRcyFavourableAdapter);
        mhorizontalRecycle.setAdapter(favourableTitleAdapter);
        mPresenter.getFavourableList();

        favourableTitleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mdataList != null) {
                    mdataList.clear();
                }
                scrollable.fullScroll(View.FOCUS_UP);
                for (int i = 0; i < dataArrayList.size(); i++) {
                    if (i == position) {
                        dataArrayList.get(i).setSelecte(true);
                    } else {
                        dataArrayList.get(i).setSelecte(false);
                    }
                }

                if (dataArrayList.get(position).getActivityList() != null && dataArrayList.size() > 0) {
                    mdataList.addAll(dataArrayList.get(position).getActivityList());
                    if (mdataList.size() > 0) {
                        for (int i = 0; i < mdataList.size(); i++) {
                            mdataList.get(i).setSelecte(false);
                        }
                    }
                    mdataList.get(0).setSelecte(true);
                    mdataList.get(0).setStartAnimation(true);
                    searchId = mdataList.get(0).getSearchId();
                    setDatailMsg(mdataList.get(0));
                    setActivityDetail(mdataList.get(0).isActivityStatus(), mdataList.get(0).getActivityDetaail(), searchId, 0);
                }
                favourableTitleAdapter.notifyDataSetChanged();
                horizontalRcyFavourableAdapter.setNewData(mdataList);
            }
        });

        horizontalRcyFavourableAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i = 0; i < mdataList.size(); i++) {
                    if (i == position) {
                        mdataList.get(i).setSelecte(true);
                        mdataList.get(i).setStartAnimation(true);
                    } else {
                        mdataList.get(i).setStartAnimation(false);
                        mdataList.get(i).setSelecte(false);
                    }
                }
                TextView textView = view.findViewById(R.id.tv_type_name);
                textView.setText("****");
                if (mAnimation != null) {
                    textView.startAnimation(mAnimation);
                } else {
                    mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.qt_btn_single_narrow);
                    textView.startAnimation(mAnimation);
                }
                scrollable.fullScroll(View.FOCUS_UP);
                setDatailMsg(mdataList.get(position));
                horizontalRcyFavourableAdapter.notifyDataSetChanged();
                searchId = mdataList.get(position).getSearchId();
                setActivityDetail(mdataList.get(position).isActivityStatus(), mdataList.get(position).getActivityDetaail(), searchId, position);
                horizontalRcyFavourableAdapter.setStartAnimationListener(new HorizontalRcyFavourableAdapter.startAnimationListener() {
                    @Override
                    public void startAnimationOnListener(TextView textView) {
                        if (mAnimation == null) {
                            mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.qt_btn_single_narrow);
                        }
                        textView.startAnimation(mAnimation);
                    }
                });
            }

        });

    }


    private void setActivityDetail(boolean activityStatus, String activiyuDetail, String searchId, int position) {
        if (!TextUtils.isEmpty(activiyuDetail)) {
            if (activityStatus) {
                tvApply.setVisibility(View.GONE);
            } else {
                tvApply.setVisibility(View.VISIBLE);
            }
            tvActivityDetail.setText(HtmlUtils.getHtml(getActivity(), tvActivityDetail, String.valueOf(activiyuDetail)));
        } else {
            mPresenter.getFavurableDetail(searchId, position);
        }
    }

    private void setDatailMsg(FavourableCenterBean.Data.ActivityList bean) {
        tvName.setVisibility(View.VISIBLE);
        tvTime.setVisibility(View.VISIBLE);
        tvTitleTime.setVisibility(View.VISIBLE);
        tvTitleDetail.setVisibility(View.VISIBLE);
        tvActivityDetail.setVisibility(View.VISIBLE);
        tvName.setText(bean.getName());
        tvTime.setText(bean.getTime());
        if (!TextUtils.isEmpty(bean.getPhoto())) {
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.loading_activity);
            String url = NetUtil.handleUrl(bean.getPhoto());
            imgTitle.setVisibility(View.VISIBLE);
            GlideApp.with(mContext)
                    .load(url)
                    .apply(options)
                    .into(imgTitle);
        }
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAnimation != null) {
            mAnimation.cancel();
            mAnimation = null;
        }
        unbinder.unbind();
    }

    @Override
    public void onFavourableVRcy(Object o) {
        FavourableCenterBean favourableCenterBean = (FavourableCenterBean) o;
        if (favourableCenterBean != null && favourableCenterBean.getCode() == 0) {
            scrollable.setVisibility(View.VISIBLE);
            if (dataArrayList != null) {
                dataArrayList.clear();
            }
            if (favourableCenterBean.getData().size() > 0) {
                imgNoDate.setVisibility(View.GONE);
                dataArrayList.addAll(favourableCenterBean.getData());
                dataArrayList.get(0).setSelecte(true);
            } else {
                imgNoDate.setVisibility(View.VISIBLE);
            }
            if (dataArrayList != null) {
                mdataList.addAll(dataArrayList.get(0).getActivityList());
                mdataList.get(0).setSelecte(true);
                mdataList.get(0).setStartAnimation(true);
                setDatailMsg(mdataList.get(0));
                searchId = mdataList.get(0).getSearchId();
            }
            if (!TextUtils.isEmpty(searchId)) {
                setActivityDetail(mdataList.get(0).isActivityStatus(), mdataList.get(0).getActivityDetaail(), searchId, 0);
            }
            favourableTitleAdapter.setNewData(dataArrayList);
            horizontalRcyFavourableAdapter.setNewData(mdataList);

        }
    }

    @Override
    public void onFavourableDetail(Object o, int position) {
        FavourableDetailBean favourableDetailBean = (FavourableDetailBean) o;
        if (favourableDetailBean != null) {
            FavourableDetailBean.Data data = favourableDetailBean.getData();
            if (data != null) {
                mdataList.get(position).setActivityDetaail(data.getCode());
                mdataList.get(position).setActivityStatus(data.isStatus());
                if (data.isStatus()) {
                    tvApply.setVisibility(View.GONE);
                } else {
                    tvApply.setVisibility(View.VISIBLE);
                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                    tvActivityDetail.setText(Html.fromHtml(String.valueOf(data.getCode()), Html.FROM_HTML_MODE_LEGACY));
//                else
//                    tvActivityDetail.setText(Html.fromHtml(String.valueOf(data.getCode())));

                tvActivityDetail.setText(HtmlUtils.getHtml(getActivity(), tvActivityDetail, String.valueOf(data.getCode())));
                tvActivityDetail.setVisibility(View.VISIBLE); // 加载完之后进行设置显示，以免加载时初始化效果不好看
            } else {
                TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), favourableDetailBean.getMessage());
            }
        }
    }


    @Override
    public void onFavourableStatus(Object o) {
        FavourableReslutBean favourableReslutBean = (FavourableReslutBean) o;
        if (favourableReslutBean != null && favourableReslutBean.getCode().contains("0")) {
            FavourableAgainApplyDialogFragment favourableReslutDIalogFragment = FavourableAgainApplyDialogFragment.newInstance(favourableReslutBean.getData(), searchId);
            DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), favourableReslutDIalogFragment);
//            if (favourableReslutBean.getData().getStatus().equals("3")) {
//                FavourableAgainApplyDialogFragment favourableReslutDIalogFragment = FavourableAgainApplyDialogFragment.newInstance(favourableReslutBean.getData(), searchId);
//                DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), favourableReslutDIalogFragment);
//            } else {
//                FavourableReslutDIalogFragment favourableReslutDIalogFragment = FavourableReslutDIalogFragment.newInstance(favourableReslutBean.getData());
//                DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), favourableReslutDIalogFragment);
//            }
        } else {
            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), favourableReslutBean.getMessage());
        }
    }

    @OnClick(R.id.tv_apply)
    public void onViewClicked() {
        if (SystemUtil.isFastClick()) return;
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        mPresenter.getFavourableReslut(searchId, "");
    }
}
