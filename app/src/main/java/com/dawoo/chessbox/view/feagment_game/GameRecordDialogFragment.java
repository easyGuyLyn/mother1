package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.util.CleanLeakUtils;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.coretool.util.math.BigDemicalUtil;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.NoteRecord;
import com.dawoo.chessbox.mvp.presenter.RecordPresenter;
import com.dawoo.chessbox.mvp.view.INoteRecordView;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.SharePreferenceUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.view.view.CustomPopupWindow;
import com.dawoo.chessbox.view.view.swipetoloadlayout.LoadMoreFooterView;
import com.dawoo.chessbox.view.view.swipetoloadlayout.RefreshHeaderView;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 牌局记录　dialog
 */
public class GameRecordDialogFragment extends BaseDialogFragment implements INoteRecordView, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.tx_effective)
    TextView mTxEffective;
    @BindView(R.id.tx_winning)
    TextView mTxWinning;
    @BindView(R.id.fast_choose_btn)
    TextView mFastChooseBtn;
    @BindView(R.id.search_btns)
    TextView mSearchBtns;
    @BindView(R.id.count)
    TextView mCount;
    @BindView(R.id.note_account_tv)
    TextView mNoteAccountTv;
    @BindView(R.id.payout_tv)
    TextView mPayoutTv;
    @BindView(R.id.total_account)
    TextView mTotalAccount;
    @BindView(R.id.account_banlance)
    TextView mAccountBanlance;
    @BindView(R.id.payout_reward)
    TextView mPayoutReward;
    @BindView(R.id.effective_betting)
    TextView mEffectiveBetting;
    @BindView(R.id.lottery_bonus)
    TextView mLotteryBonus;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private RecordPresenter mRecordPresenter;
    private long minTime = 0;
    private long maxTime = 0;
    private int mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
    private List<NoteRecord.ListBean> mListBeans = new ArrayList<>();  //加载出的所有数据
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private NoteRecordAdapter mNoteRecordAdapter;
    private boolean isFirst = true;
    private String mTimeZone;

    private CustomPopupWindow mFastChoosePopupWindow;
    public String mStartTimeTv = "";
    public String mEndTimeTv = "";

    FastPopupAdapter fastPopupAdapter = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
    }

    @Override
    protected int getViewId() {
        return R.layout.qt_gamerecord_dialogfragment;
    }

    @Override
    protected void initViews(View view) {
        intScreenHProportion = 0.93;
        intScreenWProportion = 0.7;
        mSwipeToLoadLayout.setRefreshEnabled(true);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mSwipeToLoadLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        mTimeZone = SharePreferenceUtil.getTimeZone(getActivity());
        mRecordPresenter = new RecordPresenter(getActivity(), this);
        String[] dateOfFastChoose = DateTool.getDateOfFastChoose(DateTool.DAYS_7);
        mStartTimeTv = dateOfFastChoose[0];
        mEndTimeTv = dateOfFastChoose[1];
        mFastChooseBtn.setText("近七天");
        mRecordPresenter.getNoteRecord(mStartTimeTv, mEndTimeTv, ConstantValue.RECORD_LIST_PAGE_SIZE, mPageNumber, true);
        mNoteRecordAdapter = new NoteRecordAdapter(R.layout.recyclerview_item_note_recor);//mListBeans
        mNoteRecordAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.qt_capital_empty_view, null));
        mNoteRecordAdapter.setNewData(mListBeans);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        // mSwipeTarget.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mSwipeTarget.setItemAnimator(new DefaultItemAnimator());
        mSwipeTarget.setAdapter(mNoteRecordAdapter);
    }


    @OnClick({R.id.fast_choose_btn, R.id.search_btns, R.id.img_close})
    public void onViewClicked(View view) {
        if (view.getId() != R.id.img_close) {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        } else {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        }
        switch (view.getId()) {
            case R.id.start_time:
//                if (SystemUtil.isFastClick()) return;
//                try {
//                    Date date = formatter.parse(mTvEndTime.getText().toString());
//                    mRecordPresenter.selectTime(0, minTime, date.getTime());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                    mRecordPresenter.selectTime(0, 0, new Date().getTime());
//                }
//                break;
//            case R.id.end_time_fl:
//                if (SystemUtil.isFastClick()) return;
//                try {
//                    Date date = formatter.parse(mTvStartTime.getText().toString());
//                    mRecordPresenter.selectTime(1, date.getTime(), maxTime);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                    mRecordPresenter.selectTime(1, 0, new Date().getTime());
//                }
//
//                break;
            case R.id.search_btns:
                if (SystemUtil.isFastClick()) return;
                mSwipeToLoadLayout.setLoadMoreEnabled(true);
                mRecordPresenter.doSearch(mStartTimeTv, mEndTimeTv);
                break;
            case R.id.img_close:
                dismiss();
                break;
            case R.id.fast_choose_btn:
                //快选
                openFastChoosePopup();
                break;
            default:
        }
    }

    @Override
    public void onRecordResult(Object o) {
        NoteRecord noteRecord = (NoteRecord) o;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

//        if (isFirst) {
//            mTvStartTime.setText(DateTool.getTimeFromLong(DateTool.FMT_DATE, DateTool.convertTimeInMillisWithTimeZone(noteRecord.getMinDate(), mTimeZone)));
//            mTvEndTime.setText(DateTool.getTimeFromLong(DateTool.FMT_DATE, DateTool.convertTimeInMillisWithTimeZone(noteRecord.getMaxDate(), mTimeZone)));
//        }
        minTime = DateTool.convertTimeInMillisWithTimeZone(noteRecord.getMinDate(), mTimeZone);
        maxTime = DateTool.convertTimeInMillisWithTimeZone(noteRecord.getMaxDate(), mTimeZone);

        NoteRecord.StatisticsDataBean statisticsDataBean = noteRecord.getStatisticsData();
        if (statisticsDataBean != null) {
            //        mNoteAccountTv.setText(noteRecord.getStatisticsData().getSingle());
            //        mPayoutTv.setText(noteRecord.getStatisticsData().getProfit());
            // 投资总额
            mAccountBanlance.setText(getString(R.string.account_banlance, BigDemicalUtil.moneyFormat(statisticsDataBean.getSingle())));
            // 投注笔数
            mLotteryBonus.setText(getString(R.string.lottery_bonus, String.valueOf(noteRecord.getTotalSize())));
            // 派彩金额
            mPayoutReward.setText(getString(R.string.payout_reward, BigDemicalUtil.moneyFormat(noteRecord.getStatisticsData().getProfit())));
            // 有效投注
            mEffectiveBetting.setText(getString(R.string.effective_betting, BigDemicalUtil.moneyFormat(noteRecord.getStatisticsData().getEffective())));

            mTxEffective.setText(BigDemicalUtil.moneyFormat(noteRecord.getStatisticsData().getSingle()));
            mTxWinning.setText(BigDemicalUtil.moneyFormat(noteRecord.getStatisticsData().getProfit()));
        }

        if (mSwipeToLoadLayout.isLoadingMore())
            mSwipeToLoadLayout.setLoadingMore(false);

        if (mSwipeToLoadLayout.isRefreshing())
            mSwipeToLoadLayout.setRefreshing(false);

        if (mPageNumber == 1) {
            mListBeans.clear();
        }

        if (noteRecord == null || noteRecord.getList() == null || noteRecord.getList().size() == 0) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            if (noteRecord.getTotalSize() > mListBeans.size()) {
                mListBeans.addAll(noteRecord.getList());
                if (noteRecord.getList().size() < ConstantValue.RECORD_LIST_PAGE_SIZE) {
                    mSwipeToLoadLayout.setLoadMoreEnabled(false);
                }

            } else {
                mSwipeToLoadLayout.setLoadMoreEnabled(false);
            }

        }
        mNoteRecordAdapter.notifyDataSetChanged();
        isFirst = false;
        mPageNumber++;
    }

    @Override
    public void loadMoreData(Object o) {
        onRecordResult(o);
    }


    @Override
    public void doSearch(Object o) {
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
        mListBeans.clear();
        onRecordResult(o);
    }

    @Override
    public void chooseStartTime(String time) {
//        mTvStartTime.setText(time);
    }

    @Override
    public void chooseEndTime(String time) {
//        mTvEndTime.setText(time);
    }

    @Override
    public void onLoadMore() {
        mRecordPresenter.loadMoreData(mStartTimeTv, mEndTimeTv, mPageNumber);

    }

    @Override
    public void onRefresh() {
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
        mRecordPresenter.loadMoreData(mStartTimeTv, mEndTimeTv, mPageNumber);
    }


    class NoteRecordAdapter extends BaseQuickAdapter {


        public NoteRecordAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            TextView mGameName = helper.itemView.findViewById(R.id.tv_game_name);
            TextView mBetTime = helper.itemView.findViewById(R.id.tv_bet_time);
            TextView mBetAmount = helper.itemView.findViewById(R.id.tv_bet_Amount);
            TextView mPayout = helper.itemView.findViewById(R.id.tv_Payout);
            TextView mSettlement = helper.itemView.findViewById(R.id.tv_Settlement);
            NoteRecord.ListBean lBean = (NoteRecord.ListBean) item;
            long time = DateTool.convertTimeInMillisWithTimeZone(lBean.getBetTime(), SharePreferenceUtil.getTimeZone(getActivity()));
            mGameName.setText(lBean.getGameName());
            mBetTime.setText(DateTool.getTimeFromLong(DateTool.FMT_DATE_TIME, time));

            mBetAmount.setText(BigDemicalUtil.moneyFormat(lBean.getSingleAmount()));

            if (!TextUtils.isEmpty(lBean.getProfitAmount())) {
                mPayout.setText(lBean.getProfitAmount());
            } else {
                mPayout.setText("- -");
            }

            if ("pending_settle".equals(lBean.getOrderState())) {
                mSettlement.setText("未结算");
            } else if ("settle".equals(lBean.getOrderState())) {
                mSettlement.setText("已结算");
            } else if ("cancel".equals(lBean.getOrderState())) {
                mSettlement.setText("取消订单");
            } else {
                mSettlement.setText(lBean.getOrderState() + "");
            }

            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemUtil.isFastClick()) return;
//                    SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
//                    String url = DataCenter.getInstance().getIp() + lBean.getUrl();
//                    ActivityUtil.startWebView(url, "", ConstantValue.WEBVIEW_TYPE_ORDINARY, 1);
                  /*  startActivity(new Intent(NoteRecordActivity.this,SettingDetailActivity.class)
                    .putExtra(SettingDetailActivity.RECORD_DETAIL_ID,lBean.getId()));*/
                }
            });
        }
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_LOGOUT)})
    public void logout(String s) {
        LogUtils.d(s);
        dismiss();
    }

    @Override
    public void onDestroy() {
        if (mRecordPresenter != null) {
            mRecordPresenter.onDestory();
        }
        RxBus.get().unregister(this);
        CleanLeakUtils.fixInputMethodManagerLeak(getActivity());
        super.onDestroy();
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

}
