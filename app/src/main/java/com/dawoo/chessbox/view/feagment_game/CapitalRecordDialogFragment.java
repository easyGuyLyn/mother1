package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.CapitalRecord;
import com.dawoo.chessbox.bean.CapitalRecordType;
import com.dawoo.chessbox.mvp.presenter.RecordPresenter;
import com.dawoo.chessbox.mvp.view.ICapitalRecordView;
import com.dawoo.chessbox.mvp.view.INoteRecordView;
import com.dawoo.chessbox.util.DialogFramentManager;
import com.dawoo.chessbox.util.SharePreferenceUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.util.StringTool;
import com.dawoo.chessbox.view.view.CustomPopupWindow;
import com.dawoo.chessbox.view.view.swipetoloadlayout.LoadMoreFooterView;
import com.dawoo.chessbox.view.view.swipetoloadlayout.RefreshHeaderView;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 资金记录dialog
 */
public class CapitalRecordDialogFragment extends BaseDialogFragment implements INoteRecordView, ICapitalRecordView, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.recharge_tx)
    TextView rechargeTx;
    @BindView(R.id.favorable_tx)
    TextView favorableTx;
    @BindView(R.id.fast_choose_btn)
    TextView mFastChooseBtn;
    @BindView(R.id.type_choose)
    TextView mTypeChoose;
    @BindView(R.id.search_btn)
    TextView mSearchBtn;
    @BindView(R.id.title_list_ll)
    LinearLayout mTitleListLl;
    @BindView(R.id.type_choose_layout)
    LinearLayout mTypeChooseLayout;
    //    @BindView(R.id.divider_1)
//    View mDivider1;
    @BindView(R.id.drawcash_lable)
    TextView mDrawcashLable;
    @BindView(R.id.drawcash_value)
    TextView mDrawcashValue;
    @BindView(R.id.transfer_lable)
    TextView mTransferLable;
    @BindView(R.id.transfer_value)
    TextView mTransferValue;
    @BindView(R.id.doing_rl)
    RelativeLayout mDoingRl;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.img_close)
    ImageView imgClose;
    Unbinder unbinder;

    private RecordPresenter mRecordPresenter;
    private CapitalRecordQwuickAdapter mAdapter;
    private int mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
    private int mPageSize = ConstantValue.RECORD_LIST_PAGE_SIZE;
    private long mMinTime = 0;
    private long mMaxTime = 0;
    private String mTransactionType = "";//空字符串为 全部类型
    private String[] mTypeArrays; //存放类型的中文名的数组
    private Map<String, String> mTypesMap = new ArrayMap<>(); //存放类型的map,中文-英文,传参要英文
    public static final String CAPITAL_RECORD_ID = "searchId";
    public static final String CAPITAL_RECORD_TYPE = "type";
    private CustomPopupWindow mFastChoosePopupWindow;
    private CustomPopupWindow mTypePopupWindow;
    private boolean mIsInit = true;

    private String mTimezone;

    public String mStartTimeTv = "";
    public String mEndTimeTv = "";
    FastPopupAdapter fastPopupAdapter = null;
    TypeChooseAdapter typeChooseAdapter = null;

    @Override
    protected int getViewId() {
        return R.layout.qt_capitalrecord_dialogfragment;
    }

    @Override
    protected void initViews(View view) {
        intScreenHProportion = 0.93;
        intScreenWProportion = 0.7;
        RxBus.get().register(this);
        mRecordPresenter = new RecordPresenter(getActivity(), this);
        String[] dateOfFastChoose = DateTool.getDateOfFastChoose(DateTool.DAYS_7);
        mStartTimeTv = dateOfFastChoose[0];
        mEndTimeTv = dateOfFastChoose[1];
        mFastChooseBtn.setText("近七天");
        mAdapter = new CapitalRecordQwuickAdapter(R.layout.recyclerview_list_item_capital_activity_view);
        mAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.qt_capital_empty_view, null));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        //shao mRecyclerView.setNestedScrollingEnabled(false);
        // 设置 下拉刷新，加载更多
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setRefreshEnabled(true);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        mTimezone = SharePreferenceUtil.getTimeZone(getActivity());
        mRecordPresenter = new RecordPresenter(getActivity(), this);
        mRecordPresenter.getCapitalRecord(mStartTimeTv, mEndTimeTv, mTransactionType, mPageNumber, mPageSize);
        mRecordPresenter.getCapitalRecordType();
    }


    @OnClick({R.id.fast_choose_btn, R.id.search_btn, R.id.type_choose, R.id.img_close})
    public void onViewClicked(View view) {
        if (view.getId() != R.id.img_close) {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        } else {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        }
        switch (view.getId()) {
//            case R.id.start_time_fl:
//                if (SystemUtil.isFastClick()) return;
//                mRecordPresenter.selectTime(0, mMinTime, mMaxTime);
//                break;
//            case R.id.end_time_fl:
//                if (SystemUtil.isFastClick()) return;
//                long minTime = mMinTime;
//                minTime = DateTool.getLongFromTime("yyyy-MM-dd", mStartTimeTv.getText().toString());
//                mRecordPresenter.selectTime(1, minTime, mMaxTime);
//                break;
            case R.id.fast_choose_btn:
                //快选
                openFastChoosePopup();
                break;
            case R.id.type_choose:
                initOpenTypeChoosePopup(mTypeArrays);
                break;
            case R.id.search_btn:
                if (SystemUtil.isFastClick()) return;
                doSearch();
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }

    @Override
    public void onRecordResult(Object o) {
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
        CapitalRecord capitalRecord = (CapitalRecord) o;
        setData(capitalRecord);
    }

    private void setData(CapitalRecord capitalRecord) {
        if (capitalRecord.getFundListApps() != null) {
            mAdapter.setNewData(capitalRecord.getFundListApps());
            if (capitalRecord.getFundListApps().size() < mPageSize) {
                mSwipeToLoadLayout.setLoadMoreEnabled(false);
            } else {
                mPageNumber++;
            }
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        }
        if (!StringTool.isEmpty(capitalRecord.getWithdrawSum())) {
            mDrawcashValue.setText(capitalRecord.getCurrency() + " " + capitalRecord.getWithdrawSum());
        }
        if (!StringTool.isEmpty(capitalRecord.getTransferSum())) {
            mTransferValue.setText(capitalRecord.getCurrency() + " " + capitalRecord.getTransferSum());
        }
//        if (!StringTool.isEmpty(capitalRecord.getTotalCount())) {
//            mUserCapitalTotal.setText("￥ " + capitalRecord.getTotalCount());
//            mAccountBanlance.setText(getString(R.string.recharge_total, capitalRecord.getTotalCount()));
//        }
//        if (capitalRecord.getSumPlayerMap() != null) {
//            mAccountBanlance.setText(getString(R.string.recharge_total, capitalRecord.getSumPlayerMap().getRecharge()));
//            mPayoutReward.setText(getString(R.string.withdraw_total, capitalRecord.getSumPlayerMap().getWithdraw()));
//            mEffectiveBetting.setText(getString(R.string.favorable_total, capitalRecord.getSumPlayerMap().getFavorable()));
//            mLotteryBonus.setText(getString(R.string.rakeback_total, capitalRecord.getSumPlayerMap().getRakeback()));
//        }
        if (mIsInit) {
//            mStartTimeTv.setText(DateTool.getTimeFromLong(DateTool.FMT_DATE, DateTool.convertTimeInMillisWithTimeZone(capitalRecord.getMinDate(), mTimezone)));
//            mEndTimeTv.setText(DateTool.getTimeFromLong(DateTool.FMT_DATE, DateTool.convertTimeInMillisWithTimeZone(capitalRecord.getMaxDate(), mTimezone)));
        }
        mIsInit = false;
        mMinTime = DateTool.convertTimeInMillisWithTimeZone(capitalRecord.getMinDate(), mTimezone);
        mMaxTime = DateTool.convertTimeInMillisWithTimeZone(capitalRecord.getMaxDate(), mTimezone);
        rechargeTx.setText("充值合计：" + capitalRecord.getSumPlayerMap().getRecharge() + "");
        favorableTx.setText("优惠合计：" + capitalRecord.getSumPlayerMap().getFavorable() + "");
    }


    @Override
    public void loadMoreData(Object o) {
        if (mSwipeToLoadLayout.isLoadingMore()) {
            mSwipeToLoadLayout.setLoadingMore(false);
        }
        CapitalRecord capitalRecord = (CapitalRecord) o;
        if (capitalRecord.getFundListApps() == null) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            return;
        }

        if (mAdapter.getData().size() >= capitalRecord.getTotalCount()) {
            //ToastUtil.showToastShort(getActivity(), getString(R.string.NO_MORE_DATA));
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            //return;
        }
        mAdapter.addData(capitalRecord.getFundListApps());
        if (capitalRecord.getFundListApps().size() < mPageSize) {
            // mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mPageNumber++;
        }
    }

    @Override
    public void doSearch(Object o) {

    }

    public void doSearch() {
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        if (mSwipeToLoadLayout.isRefreshing() || mSwipeToLoadLayout.isLoadingMore()) {
            return;
        }
        mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
        mRecordPresenter.getCapitalRecord(mStartTimeTv, mEndTimeTv,
                mTransactionType, mPageNumber, mPageSize);
    }

    @Override
    public void chooseStartTime(String time) {
//        mStartTimeTv.setText(time);
//        mEndTimeTv.setText(DateTool.getTimeFromLong(DateTool.FMT_DATE, mMaxTime));
    }

    @Override
    public void chooseEndTime(String time) {
//        mEndTimeTv.setText(time);
    }

    @Override
    public void onRefresh() {
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
        mRecordPresenter.getCapitalRecordType();
        mRecordPresenter.getCapitalRecord(mStartTimeTv, mEndTimeTv,
                mTransactionType, mPageNumber, mPageSize);
    }

    @Override
    public void onLoadMore() {
        mRecordPresenter.getCapitalMoreRecord(mStartTimeTv, mEndTimeTv,
                mTransactionType, mPageNumber, mPageSize);
    }

    /**
     * 创建快速选中
     */
    private void openFastChoosePopup() {
        if (mFastChoosePopupWindow == null) {
            String[] array = getResources().getStringArray(R.array.fast_choose_list);
            List<String> list = Arrays.asList(array);
            fastPopupAdapter = new FastPopupAdapter(R.layout.custom_popup_list_item_view, list, mFastChooseBtn.getText().toString().trim());
            mFastChoosePopupWindow = new CustomPopupWindow(getActivity(), fastPopupAdapter);
        }
        if (fastPopupAdapter != null) {
            fastPopupAdapter.setItemData(mFastChooseBtn.getText().toString().trim());
        }
        mFastChoosePopupWindow.doTogglePopupWindow(mFastChooseBtn);
    }


    /**
     * 创建类型选择
     */
    private void initOpenTypeChoosePopup(String[] array) {
        if (mTypePopupWindow == null) {
            List<String> list = Arrays.asList(array);
            typeChooseAdapter = new TypeChooseAdapter(R.layout.custom_popup_list_item_view, list, mTypeChoose.getText().toString().trim());
            mTypePopupWindow = new CustomPopupWindow(getActivity(), typeChooseAdapter);
        }
        if (typeChooseAdapter != null) {
            typeChooseAdapter.setItemData(mTypeChoose.getText().toString().trim());
        }
        mTypePopupWindow.doTogglePopupWindow(mTypeChoose);
    }


    /**
     * 处理获得的资金记录的所有类型
     *
     * @param o
     */
    @Override
    public void chooseTypeResult(Object o) {
        CapitalRecordType capitalRecordType = (CapitalRecordType) o;
        if (capitalRecordType == null) return;
        String withdrawals = "";
        String deposit = "";
        if (!TextUtils.isEmpty(capitalRecordType.getDeposit())) {
            deposit = "充值";
        }
        if (!TextUtils.isEmpty(capitalRecordType.getWithdrawals())) {
            withdrawals = "出币";
        }

        mTypeArrays = new String[]{
                getString(R.string.all_type),
                capitalRecordType.getBackwater(),
                deposit,
                capitalRecordType.getFavorable(),
                capitalRecordType.getRecommend(),
                capitalRecordType.getTransfers(),
                withdrawals};

        mTypesMap.clear();
        mTypesMap.put(getString(R.string.all_type), "");
        mTypesMap.put(capitalRecordType.getBackwater(), "backwater");
        mTypesMap.put(deposit, "deposit");
        mTypesMap.put(capitalRecordType.getFavorable(), "favorable");
        mTypesMap.put(capitalRecordType.getRecommend(), "recommend");
        mTypesMap.put(capitalRecordType.getTransfers(), "transfers");
        mTypesMap.put(withdrawals, "withdrawals");
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
        if (mRecordPresenter != null) {
            mRecordPresenter.onDestory();
        }
        super.onDestroyView();
        unbinder.unbind();
    }


    class TypeChooseAdapter extends BaseQuickAdapter {
        public String mItemData;

        public TypeChooseAdapter(int layoutResId, @Nullable List data, String itemData) {
            super(layoutResId, data);
            mItemData = itemData;
        }

        public void setItemData(String data) {
            mItemData = data;
            notifyDataSetChanged();
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            helper.setText(R.id.item_tv, String.valueOf(item));
            if (mItemData.equals(String.valueOf(item))) {
                helper.setBackgroundColor(R.id.item_tv, getActivity().getResources().getColor(R.color.qt_audit_item_bg));
            } else {
                helper.setBackgroundColor(R.id.item_tv, getActivity().getResources().getColor(R.color.transparent));
            }
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemUtil.isFastClick()) return;
                    int position = helper.getAdapterPosition();
                    if (mTypeArrays != null && mTypesMap.size() > 0) {
                        for (int i = 0; i < mTypeArrays.length; i++) {
                            if (position == i) {
                                mTransactionType = mTypesMap.get(mTypeArrays[i]);
                                mTypeChoose.setText(mTypeArrays[i]);
                                // doSearch();
                                mSwipeToLoadLayout.setLoadMoreEnabled(true);
                                mTypePopupWindow.dissMissPopWindow();
                            }
                        }
                    }
                }
            });
        }
    }


    class FastPopupAdapter extends BaseQuickAdapter {

        public String mItemData;

        public FastPopupAdapter(int layoutResId, @Nullable List data, String itemData) {
            super(layoutResId, data);
            mItemData = itemData;
        }

        public void setItemData(String data) {
            mItemData = data;
            notifyDataSetChanged();
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            helper.setText(R.id.item_tv, String.valueOf(item));
            if (mItemData.equals(String.valueOf(item))) {
                helper.setBackgroundColor(R.id.item_tv, getActivity().getResources().getColor(R.color.qt_audit_item_bg));
            } else {
                helper.setBackgroundColor(R.id.item_tv, getActivity().getResources().getColor(R.color.transparent));
            }
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemUtil.isFastClick()) return;
                    int position = helper.getAdapterPosition();
                    String[] arr = new String[2];
                    switch (position) {
                        case 0://今天
                            arr = DateTool.getDateOfFastChoose(DateTool.TODAY);
                            break;
                        case 1://昨天
                            arr = DateTool.getDateOfFastChoose(DateTool.YESTERDAY);
                            break;
                        case 2://本周
                            arr = DateTool.getDateOfFastChoose(DateTool.THISWEEK);
                            break;
//                        case 3://上周
//                            arr = DateTool.getDateOfFastChoose(DateTool.LASTWEEK);
//                            break;
//                        case 4://本月
//                            arr = DateTool.getDateOfFastChoose(DateTool.THISMONTH);
//                            break;
                        case 3://最近7天
                            arr = DateTool.getDateOfFastChoose(DateTool.DAYS_7);
                            break;
//                        case 6://最近30天
//                            arr = DateTool.getDateOfFastChoose(DateTool.DAYS_30);
//                            break;
                    }
                    setTimeAndRearch(arr[0], arr[1]);
                    mFastChoosePopupWindow.dissMissPopWindow();
                    mFastChooseBtn.setText(String.valueOf(item));
                }
            });
        }
    }

    private void setTimeAndRearch(String startTime, String endTime) {
        mStartTimeTv = startTime;
        mEndTimeTv = endTime;
        // doSearch();
    }


    class CapitalRecordQwuickAdapter extends BaseQuickAdapter {

        public CapitalRecordQwuickAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {

            CapitalRecord.FundListBean fundListBean = (CapitalRecord.FundListBean) item;

//            Calendar calendar =Calendar.getInstance();
//            TimeZone timeZone = TimeZone.getTimeZone(mTimezone);
//            calendar.setTimeZone(timeZone);
//            helper.setText(R.id.tv_record_time, calendar.get(Calendar.YEAR)+"-"
//                    +(calendar.get(Calendar.MONTH)+1)+"-"
//                    +calendar.get(Calendar.DAY_OF_MONTH));
            helper.setText(R.id.tv_record_time, DateTool.getTimeFromLong(DateTool.FMT_DATE, DateTool.changeTimeZone(fundListBean.getCreateTime(), mTimezone, DateTool.getWuYong())));
            Log.e("TAG", "aaaaaaaaaaaaaaa" + DateTool.getTimeFromLong(DateTool.FMT_DATE_TIME, fundListBean.getCreateTime()));
            helper.setText(R.id.tv_record_value, fundListBean.getTransactionMoney());
            helper.setText(R.id.tv_record_status, fundListBean.getStatusName());
            if ("withdrawals".equals(fundListBean.getTransactionType())) {
                helper.setText(R.id.tv_record_type, "出币");
            } else if ("deposit".equals(fundListBean.getTransactionType())) {
                helper.setText(R.id.tv_record_type, " 充值");
            } else {
                helper.setText(R.id.tv_record_type, fundListBean.getTransaction_typeName());
            }
            setStatusColor(helper.getView(R.id.tv_record_status), fundListBean.getStatus());
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemUtil.isFastClick()) return;
//                    SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
//                    CapitalRecordDetailsDialogFragment dialogFragment = CapitalRecordDetailsDialogFragment.getInstance(fundListBean.getTransactionType(), fundListBean.getId());
//                    DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), dialogFragment);
//                    Intent intent = new Intent(getActivity(), CapitalDetailRecordActivity.class);
//                    intent.putExtra(CAPITAL_RECORD_TYPE, fundListBean.getTransactionType());
//                    intent.putExtra(CAPITAL_RECORD_ID, fundListBean.getId());
//                    startActivity(intent);
                }
            });
        }
    }

    private void setStatusColor(TextView textView, String status) {
        switch (status) {
            case "failure":
//                textView.setTextColor(getResources().getColor(R.color.failure));
                textView.setTextColor(getResources().getColor(R.color.white));
                break;
            case "pending_pay":
                textView.setTextColor(getResources().getColor(R.color.btn_yellow_normal));
                break;
            case "success":
//                textView.setTextColor(getResources().getColor(R.color.sucess));
                textView.setTextColor(getResources().getColor(R.color.white));
                break;
            case "process":
//                textView.setTextColor(getResources().getColor(R.color.process));
                textView.setTextColor(getResources().getColor(R.color.white));
                break;
            case "reject":
//                textView.setTextColor(getResources().getColor(R.color.failure));
                textView.setTextColor(getResources().getColor(R.color.white));
                break;
            case "pending":
//                textView.setTextColor(getResources().getColor(R.color.process));
                textView.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    @Override
    public void onDestroy() {
        mRecordPresenter.onDestory();
        RxBus.get().unregister(this);
        super.onDestroy();
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
