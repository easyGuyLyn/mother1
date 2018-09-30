package com.dawoo.chessbox.view.activity.message;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.date.DateTool;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.MessageDetail;
import com.dawoo.chessbox.bean.SysNotice;
import com.dawoo.chessbox.mvp.presenter.MessagePresenter;
import com.dawoo.chessbox.mvp.view.IMessageDetailView;
import com.dawoo.chessbox.mvp.view.ISysNoticeView;
import com.dawoo.chessbox.util.SharePreferenceUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.util.TipsDialogFaragmentUtils;
import com.dawoo.chessbox.view.fragment.BaseFragment;
import com.dawoo.chessbox.view.view.CustomPopupWindow;
import com.dawoo.chessbox.view.view.swipetoloadlayout.LoadMoreFooterView;
import com.dawoo.chessbox.view.view.swipetoloadlayout.RefreshHeaderView;
import com.dawoo.chessbox.view.view.WrapContentLinearLayoutManager;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 系统公告
 */
public class SysNoticeFragment extends BaseFragment implements ISysNoticeView,IMessageDetailView, OnLoadMoreListener {
    @BindView(R.id.start_time)
    FrameLayout mStartTimeFl;
    @BindView(R.id.start_time_tv)
    TextView mStartTimeTv;
    @BindView(R.id.end_time_tv)
    TextView mEndTimeTv;
    @BindView(R.id.end_time_fl)
    FrameLayout mEndTimeFl;
    @BindView(R.id.fast_btns)
    Button mFastBtns;
    @BindView(R.id.choose_type_ll)
    LinearLayout mChooseTypeLl;
    @BindView(R.id.paras_rl)
    RelativeLayout mParasRl;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    Unbinder unbinder;
    private MessagePresenter mPresneter;
    private SysNoticeAdapter2 mAdapter;
    private String mStartTime;
    private String mEndTime;
    private int mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
    private int mPageSize = ConstantValue.RECORD_LIST_PAGE_SIZE;
    private CustomPopupWindow mFastChoosePopupWindow;
    private DatePickerDialog mStartDatePickerDialog;
    private DatePickerDialog mEndDatePickerDialog;
    private long mMinTime;
    private long mMaxTime;
    private String mTImeZone;

    private String mCurrentChooseId;//当前点进详情的  未读消息id

    public SysNoticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        RxBus.get().unregister(this);
        mPresneter.onDestory();
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sys_notice, container, false);

        unbinder = ButterKnife.bind(this, v);
        RxBus.get().register(this);
        initView();
        initData();
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView() {
        mSwipeToLoadLayout.setRefreshEnabled(false);
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);

        // 设置recycleview
        //mAdapter = new SysNoticeAdapter(mContext, new OnSysNoticeItemClickListener());
        mAdapter = new SysNoticeAdapter2(R.layout.message_list_item_game_notice);
        mAdapter.setEmptyView(LayoutInflater.from(mContext).inflate(R.layout.empty_no_date_view, null));
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.qt_dialog_bg));
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mPresneter = new MessagePresenter(mContext, this);
        mTImeZone = SharePreferenceUtil.getTimeZone(mContext);
        mStartTimeTv.setText(DateTool.convert2String(new Date(), DateTool.FMT_DATE));
        mEndTimeTv.setText(DateTool.convert2String(new Date(), DateTool.FMT_DATE));
    }


    @Override
    protected void loadData() {
        mPresneter.getSysNotice(mPageNumber, mPageSize);
    }

    @Override
    public void onLoadResult(Object o) {
        if (o != null && o instanceof SysNotice) {
            SysNotice sysNotice = (SysNotice) o;
            setTime(sysNotice.getMinDate(), sysNotice.getMaxDate());
            mAdapter.setNewData(sysNotice.getList());
        }
    }

    @Override
    public void onLoadMoreResult(Object o) {
        mSwipeToLoadLayout.setLoadingMore(false);

        if (o != null && o instanceof SysNotice) {
            SysNotice sysNotice = (SysNotice) o;
            if (0 == sysNotice.getList().size()) {
                ToastUtil.showResShort(mContext, R.string.NO_MORE_DATA);
            } else {
                mAdapter.addData(sysNotice.getList());
            }
        }
    }

    private void setTime(long minDate, long MaxDate) {
        mMinTime = DateTool.convertTimeInMillisWithTimeZone(minDate, mTImeZone);
        mMaxTime = DateTool.convertTimeInMillisWithTimeZone(MaxDate, mTImeZone);
    }


    @OnClick({R.id.start_time, R.id.end_time_fl, R.id.fast_btns})
    public void onViewClicked(View view) {
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        switch (view.getId()) {
            case R.id.start_time:
                createStartDatePicker();
                break;
            case R.id.end_time_fl:
                createEndDatePicker();
                break;
            case R.id.fast_btns:
                //快选
                openFastChoosePopup();
                break;
        }
    }

    /**
     * 创建开始日期
     */
    private void createStartDatePicker() {
        if (mStartDatePickerDialog == null) {
            mStartDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Date endDate = DateTool.convert2Date(mEndTimeTv.getText().toString(), DateTool.FMT_DATE);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, dayOfMonth, 0, 0, 0);
                    Date startDate = calendar.getTime();
                    String startStr = DateTool.convert2String(startDate, DateTool.FMT_DATE);

                    if (startDate.getTime() - endDate.getTime() > 10000) {
                        ToastUtil.showResShort(mContext, R.string.start_time_cant_greater_than_end_time);
                        return;
                    }

                    mStartTimeTv.setText(startStr);
                    mStartTime = startStr;
                    mEndTime = mEndTimeTv.getText().toString();

                    mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
                    mPresneter.getSysNotice(mStartTime, mEndTime,
                            mPageNumber,
                            mPageSize);
                }
            }, DateTool.getYear(new Date()), DateTool.getMonth(new Date()), DateTool.getDay(new Date()));

            if (0 != mMaxTime && 0 != mMinTime) {
                DatePicker picker = mStartDatePickerDialog.getDatePicker();
                picker.setMinDate(mMinTime);
                picker.setMaxDate(mMaxTime);
            }
        }

        mStartDatePickerDialog.show();
    }

    /**
     * 创建结束日期
     */
    private void createEndDatePicker() {
        if (mEndDatePickerDialog == null) {
            mEndDatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // 结束时间不能小于开始时间
                    Date startDate = DateTool.convert2Date(mStartTimeTv.getText().toString(), DateTool.FMT_DATE);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, dayOfMonth, 0, 0, 0);
                    Date endDate = calendar.getTime();
                    String endTimeStr = DateTool.convert2String(endDate, DateTool.FMT_DATE);

                    if (startDate.getTime() - endDate.getTime() > 10000) {
                        ToastUtil.showResShort(mContext, R.string.end_time_cant_less_than_start_time);
                        return;
                    }

                    mEndTimeTv.setText(endTimeStr);
                    mStartTime = mStartTimeTv.getText().toString();
                    mEndTime = endTimeStr;

                    mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
                    mPresneter.getSysNotice(mStartTime, mEndTime,
                            mPageNumber,
                            mPageSize);
                }
            }, DateTool.getYear(new Date()), DateTool.getMonth(new Date()), DateTool.getDay(new Date()));

            if (0 != mMaxTime && 0 != mMinTime) {
                DatePicker picker = mEndDatePickerDialog.getDatePicker();
                picker.setMinDate(mMinTime);
                picker.setMaxDate(mMaxTime);
            }
        }

        mEndDatePickerDialog.show();
    }


    @Override
    public void onLoadMore() {
        mPageNumber++;
        mPresneter.loadMoreSysNotice(mStartTime, mEndTime,
                mPageNumber,
                mPageSize);
    }


    /**
     * 创建快速选中
     */
    private void openFastChoosePopup() {
        if (mFastChoosePopupWindow == null) {
            String[] array = getResources().getStringArray(R.array.fast_choose_list);
            List<String> list = Arrays.asList(array);
            mFastChoosePopupWindow = new CustomPopupWindow(mContext, new SysNoticeFragment.FastPopupAdapter(R.layout.custom_popup_list_item_view, list));
        }
        mFastChoosePopupWindow.doTogglePopupWindow(mFastBtns);
    }

    @Override
    public void onLoadGameDetailResult(Object o) {

    }

    @Override
    public void onLoadSysDetailResult(Object o) {
        if (o != null && o instanceof MessageDetail) {
            MessageDetail messageDetail = (MessageDetail) o;
            TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getChildFragmentManager(), messageDetail.getContent(),false,true);
        }
    }

    @Override
    public void onLoadSiteSysDetailResult(Object o) {

    }

    @Override
    public void onLoadSiteMyDetailResult(Object o) {

    }


    class FastPopupAdapter extends BaseQuickAdapter {

        public FastPopupAdapter(int layoutResId, @Nullable List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            helper.setText(R.id.item_tv, String.valueOf(item));
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = helper.getAdapterPosition();
                    if (mFastChoosePopupWindow != null) {
                        mFastChoosePopupWindow.doTogglePopupWindow(mFastBtns);
                    }
                    String[] arr = new String[2];
                    SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
                    switch (position) {
                        case 0://今天
                            arr = DateTool.getDateOfFastChoose(DateTool.TODAY);
                            break;
                        case 1://昨天
                            arr = DateTool.getDateOfFastChoose(DateTool.YESTERDAY);
                            break;
                        case 2://最近7天
                            arr = DateTool.getDateOfFastChoose(DateTool.DAYS_7);
                            break;
                        /*case 2://本周
                            arr = DateTool.getDateOfFastChoose(DateTool.THISWEEK);
                            break;
                        case 3://上周
                            arr = DateTool.getDateOfFastChoose(DateTool.LASTWEEK);
                            break;
                        case 4://本月
                            arr = DateTool.getDateOfFastChoose(DateTool.THISMONTH);
                            break;
                        case 5://最近7天
                            arr = DateTool.getDateOfFastChoose(DateTool.DAYS_7);
                            break;
                        case 6://最近30天
                            arr = DateTool.getDateOfFastChoose(DateTool.DAYS_30);
                            break;*/
                    }

                    mStartTime = arr[0];
                    mEndTime = arr[1];

                    Date st = DateTool.convert2Date(mStartTime, DateTool.FMT_DATE);
                    Date et = DateTool.convert2Date(mEndTime, DateTool.FMT_DATE);

                    if (st.after(et)) {
                        ToastUtil.showResShort(mContext, R.string.start_time_cant_greater_than_end_time);
                        return;
                    }


                    mStartTimeTv.setText(mStartTime);
                    mEndTimeTv.setText(mEndTime);
                    mPageNumber = ConstantValue.RECORD_LIST_PAGE_NUMBER;
                    mPresneter.getSysNotice(mStartTime, mEndTime,
                            mPageNumber,
                            mPageSize);
                }
            });
        }
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_NETWORK_EXCEPTION)})
    public void shrinkLoadMoreView(String s) {
        LogUtils.d(s);
        //  收起刷新
        if (null != mSwipeToLoadLayout && mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }


    class SysNoticeAdapter2 extends BaseQuickAdapter {

        public SysNoticeAdapter2(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            SysNotice.ListBean bean = (SysNotice.ListBean) item;
            if (bean != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    helper.setText(R.id.content, "\t\t" + Html.fromHtml(bean.getContent(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    helper.setText(R.id.content, "\t\t" + Html.fromHtml(bean.getContent()));
                }

//                if (bean.isRead()) {
//                    helper.getView(R.id.iv_unread).setVisibility(View.GONE);
//                } else {
//                    helper.getView(R.id.iv_unread).setVisibility(View.VISIBLE);
//                }

                helper.setVisible(R.id.game_type, false);
                helper.setText(R.id.time, DateTool.convert2String(new Date(bean.getPublishTime()), DateTool.FMT_DATE_TIME));
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
                        mCurrentChooseId = bean.getSearchId();
//                        if (!bean.isRead()){
//                            mCurrentChooseId = bean.getSearchId();
//                            mPresneter.setSiteSysNoticeReadStatus(mCurrentChooseId);
//                        }
                        mPresneter.getSysNoticeDetail(mCurrentChooseId);

                    }
                });
            }
        }
    }
}
