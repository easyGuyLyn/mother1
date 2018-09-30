package com.dawoo.chessbox.view.activity.message;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.CommonRequestResult;
import com.dawoo.chessbox.bean.SiteMyNotice;
import com.dawoo.chessbox.mvp.presenter.MessagePresenter;
import com.dawoo.chessbox.mvp.view.ISiteSysNoticeView;
import com.dawoo.chessbox.push.PushDispatchManager;
import com.dawoo.chessbox.util.SharePreferenceUtil;
import com.dawoo.chessbox.view.fragment.BaseFragment;
import com.dawoo.chessbox.view.view.swipetoloadlayout.LoadMoreFooterView;
import com.dawoo.chessbox.view.view.swipetoloadlayout.RefreshHeaderView;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Archar on 2018
 */
public class SiteMyMsgFragment extends BaseFragment implements ISiteSysNoticeView, OnRefreshListener, OnLoadMoreListener {
    Unbinder unbinder;
    @BindView(R.id.cb_all_choose)
    CheckBox mCbAllChoose;
    @BindView(R.id.tv_siteMsg_delete)
    TextView mTvSiteMsgDelete;
    @BindView(R.id.tv_siteMsg_read)
    TextView mTvSiteMsgRead;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target_recyclerView)
    RecyclerView mMSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private MessagePresenter mPresneter;
    private int mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
    private int mPageSize = ConstantValue.RECORD_LIST_PAGE_SIZE;
    private SiteSysMsgQwuickAdapter mAdapter;
    private String mCurrentChooseId;//当前点进详情的  未读消息id
    private boolean mIsAllChoose; //是否是全选
    private String mTImeZone;

    public SiteMyMsgFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        mPresneter.onDestory();
        RxBus.get().unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_site_my_msg_layout, container, false);
        unbinder = ButterKnife.bind(this, v);
        RxBus.get().register(this);
        initView();
        initData();
        return v;
    }

    public void initView() {
        mAdapter = new SiteSysMsgQwuickAdapter(R.layout.recyclerview_list_item_site_sys_fragment_view);
        mAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.empty_no_date_view, null));
        mMSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mMSwipeTarget.setAdapter(mAdapter);
        // 设置 下拉刷新，加载更多
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setRefreshEnabled(true);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        initAllChooseListen();
    }

    public void initAllChooseListen() {
        if (mSwipeToLoadLayout.isLoadingMore() || mSwipeToLoadLayout.isRefreshing())
            return;
        mCbAllChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chooseAll(isChecked);
            }
        });
    }

    public void initData() {
        mTImeZone = SharePreferenceUtil.getTimeZone(getActivity());
        mPresneter = new MessagePresenter(getActivity(), this);
        loadData();
    }

    @Override
    protected void loadData() {
        mPresneter.getSiteMyMsg(mPageNumber, mPageSize, false);
    }

    @OnClick({R.id.tv_siteMsg_delete, R.id.tv_siteMsg_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_siteMsg_delete:
                String ids = getIds();
                if (!TextUtils.isEmpty(ids)) {
                    mPresneter.deleteSiteMyNotice(ids);
                }
                break;
            case R.id.tv_siteMsg_read:
                String ids_ = getIds();
                if (!TextUtils.isEmpty(ids_)) {
                    mPresneter.setSiteMyNoticeStatus(ids_);
                }
                break;
        }
    }

    /**
     * 选中一项
     */

    private void chooseOne(int position, Boolean isCheck) {
        ((SiteMyNotice.ListBean) (mAdapter.getData().get(position))).setSelected(isCheck);
        mAdapter.notifyItemChanged(position);
    }

    /**
     * 全选
     */
    private void chooseAll(Boolean isCheck) {
        if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
            mCbAllChoose.setChecked(false);
            return;
        }
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            ((SiteMyNotice.ListBean) (mAdapter.getData().get(i))).setSelected(isCheck);
        }
        mIsAllChoose = isCheck;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取 已选中的消息的id,并组成ids
     */
    private String getIds() {
        if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
            return null;
        }
        String ids = "";
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            if (((SiteMyNotice.ListBean) (mAdapter.getData().get(i))).getSelected()) {
                if (i == mAdapter.getData().size() - 1) {
                    ids = ids + ((SiteMyNotice.ListBean) (mAdapter.getData().get(i))).getId();
                } else {
                    ids = ids + ((SiteMyNotice.ListBean) (mAdapter.getData().get(i))).getId() + ",";
                }
            }
        }
        return ids;
    }


    @Override
    public void onLoadResult(Object o) {
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
        SiteMyNotice siteMyNotice = (SiteMyNotice) o;
        setData(siteMyNotice);
    }

    private void setData(SiteMyNotice data) {
        if (data.getCode() == 0) {
            if (data.getData() == null) return;

            if (data.getData().getDataList() != null && data.getData().getDataList().size() > 0) {
                mAdapter.setNewData(data.getData().getDataList());
                if (data.getData().getDataList().size() < mPageSize) {
                    mSwipeToLoadLayout.setLoadMoreEnabled(false);
                } else {
                    mPageNumber++;
                }
            }
        } else {
            ToastUtil.showToastShort(getActivity(), data.getMessage());
        }
    }

    @Override
    public void onLoadMoreResult(Object o) {
        if (mSwipeToLoadLayout.isLoadingMore()) {
            mSwipeToLoadLayout.setLoadingMore(false);
        }
        SiteMyNotice data = (SiteMyNotice) o;
        if (data.getData() == null) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            return;
        }
        if (data.getData().getDataList() == null) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            return;
        }
        if (mAdapter.getData().size() >= data.getTotal()) {
            ToastUtil.showToastShort(getActivity(), getString(R.string.NO_MORE_DATA));
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            return;
        }

        mAdapter.addData(data.getData().getDataList());
        if (data.getData().getDataList().size() < mPageSize) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mPageNumber++;
        }
    }

    @Override
    public void onReadMsgsResult(Object o) {
        CommonRequestResult commonRequestResult = (CommonRequestResult) o;
        if (commonRequestResult.getCode() == 0) {
            reSetDataBySucessAction(0);
        } else {
            ToastUtil.showToastShort(getActivity(), commonRequestResult.getMessage());
        }
    }

    @Override
    public void onReadMsgResult(Object o) {
        CommonRequestResult commonRequestResult = (CommonRequestResult) o;
        if (commonRequestResult.getCode() == 0) {
            reSetDataByReadDetailMsg();
        } else {
            ToastUtil.showToastShort(getActivity(), commonRequestResult.getMessage());
        }
    }

    /**
     * 阅读 未读消息后  更新UI
     */
    private void reSetDataByReadDetailMsg() {
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            SiteMyNotice.ListBean bean = (SiteMyNotice.ListBean) mAdapter.getData().get(i);
            if (bean.getId() == mCurrentChooseId) {
                bean.setRead(true);
            }
        }
        mAdapter.notifyDataSetChanged();
        refeshSmallRedPoint();
    }

    /**
     * type   0 为改数据里的read状态   1 为根据删除了的数据重新设置数据集
     *
     * @param type
     */
    private void reSetDataBySucessAction(int type) {
        List<SiteMyNotice.ListBean> cutList = new ArrayList<>();
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            SiteMyNotice.ListBean bean = (SiteMyNotice.ListBean) mAdapter.getData().get(i);
            if (bean.getSelected()) {
                if (type == 0) {
                    bean.setRead(true);
                    bean.setSelected(false);
                } else if (type == 1) {
                    cutList.add(bean);
                }
            }
        }
        if (type == 1) {
            mAdapter.getData().removeAll(cutList);
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getData().size() == 0) {
                onRefresh();
            }
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.notifyDataSetChanged();
        if (mIsAllChoose) {
            mCbAllChoose.setChecked(false);
            mIsAllChoose = false;
        }
        refeshSmallRedPoint();
    }

    /**
     * 更新小红点
     */
    private void refeshSmallRedPoint() {
//        MessageCenterActivity messageCenterActivity = ((MessageCenterActivity) getActivity());
//        if (messageCenterActivity.siteMsgFragment != null) {
//            ((SiteMsgFragment) messageCenterActivity.siteMsgFragment).getSiteUnReadMsgCount();
//        }
//        ((MessageCenterActivity) getActivity()).getSiteUnReadMsgCount();
    }


    @Override
    public void onDeleteMsgsResult(Object o) {
        CommonRequestResult commonRequestResult = (CommonRequestResult) o;
        if (commonRequestResult.getCode() == 0) {
            reSetDataBySucessAction(1);
        } else {
            ToastUtil.showToastShort(getActivity(), commonRequestResult.getMessage());
        }
    }

    @Override
    public void onLoadMore() {
        mPresneter.getSiteMyMsg(mPageNumber, mPageSize, true);
    }

    @Override
    public void onRefresh() {
        mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
        mPresneter.getSiteMyMsg(mPageNumber, mPageSize, false);
    }

    private class SiteSysMsgQwuickAdapter extends BaseQuickAdapter {

        public SiteSysMsgQwuickAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            SiteMyNotice.ListBean fundListBean = (SiteMyNotice.ListBean) item;
            helper.setText(R.id.tv_title, fundListBean.getAdvisoryTitle());
            helper.setText(R.id.tv_time, DateTool.getTimeFromLong(DateTool.FMT_DATE_TIME,
                    DateTool.convertTimeInMillisWithTimeZone(fundListBean.getAdvisoryTime(), mTImeZone)));
            CheckBox checkBox = helper.getView(R.id.cb_siteSys_item_choose);
            View v_cb = helper.getView(R.id.v_cb);
            int currentPosition = helper.getAdapterPosition();
            if (fundListBean.getSelected()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            if (fundListBean.getRead()) {
                helper.getView(R.id.iv_unread).setVisibility(View.GONE);
            } else {
                helper.getView(R.id.iv_unread).setVisibility(View.VISIBLE);
            }
            if (mSwipeToLoadLayout.isLoadingMore() || mSwipeToLoadLayout.isRefreshing())
                return;
            v_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(!checkBox.isChecked());
                    chooseOne(currentPosition, checkBox.isChecked());
                }
            });
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, MessageDetailActivity.class);
//                    intent.putExtra(MessageDetailActivity.TYPE_NOTICE, MessageDetailActivity.SITE_NOTICE_MY);
//                    intent.putExtra(MessageDetailActivity.DETAIL_ID, fundListBean.getId());
//                    if (fundListBean.getRead()) {
//                        startActivity(intent);
//                    } else {
//                        mCurrentChooseId = fundListBean.getId();
//                        mPresneter.setSiteMyNoticeReadStatus(mCurrentChooseId);
//                        startActivity(intent);
//                    }
                }
            });
        }
    }

    @Subscribe(tags = {@Tag(PushDispatchManager.EVENT_PUSH_MSG_CENTER)})
    public void pushData(String s) {
        onRefresh();
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_NETWORK_EXCEPTION)})
    public void shrinkRefreshView(String s) {
        LogUtils.d(s);
        //  收起刷新
        if (null != mSwipeToLoadLayout && mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }
}
