package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dawoo.chessbox.R;
import com.dawoo.chessbox.adapter.MessageFragementAdapter;
import com.dawoo.chessbox.bean.SiteMsgUnReadCount;
import com.dawoo.chessbox.mvp.presenter.MessagePresenter;
import com.dawoo.chessbox.mvp.view.ISiteMsgView;
import com.dawoo.chessbox.push.PushDispatchManager;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.view.activity.message.GameNoticeFragment;
import com.dawoo.chessbox.view.activity.message.SiteMsgFragment;
import com.dawoo.chessbox.view.activity.message.SysNoticeFragment;
import com.dawoo.chessbox.view.fragment.BaseFragment;
import com.dawoo.chessbox.view.view.CustomViewPager;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MessageCenterFragment extends BaseFragment implements ISiteMsgView {

    @BindView(R.id.game_notice)
    Button mGameNotice;
    @BindView(R.id.sys_notice)
    Button mSysNotice;
    @BindView(R.id.zhan_notice)
    TextView mZhanNotice;
    @BindView(R.id.v_tip_my_notice)
    TextView mVTipMyNotice;
    @BindView(R.id.view_pager)
    CustomViewPager mViewPager;
    Unbinder unbinder;

    private MessagePresenter mPresneter;
    public Fragment siteMsgFragment;

    private final int TAB_GAME = 0;
    private final int TAB_SYS = 1;
    private final int TAB_ZHAN = 2;
    // 申请优惠
    public final static int TAB_APPLICATION_PREFERENTAIL = 3;
    public final static String WHERE_FROM = "WHERE_FROM";
    private int mType = 0;

    public static MessageCenterFragment newInstance(int whereFrom) {

        Bundle args = new Bundle();
        args.putInt(WHERE_FROM, whereFrom);
        MessageCenterFragment fragment = new MessageCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qt_message_center_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initData() {
        RxBus.get().register(this);
        mPresneter = new MessagePresenter(getContext(), this);
        //mType = getIntent().getIntExtra(WHERE_FROM, 0);
        List<String> titles = new ArrayList<>();
        List<Fragment> fragments = new ArrayList<>();
        titles.add(getString(R.string.game_noitce));
        titles.add(getString(R.string.sys_notice));
        titles.add(getString(R.string.zhan_message));
        fragments.add(new GameNoticeFragment());
        fragments.add(new SysNoticeFragment());
        siteMsgFragment = SiteMsgFragment.newInstance(mType);
        fragments.add(siteMsgFragment);

        MessageFragementAdapter adapter = new MessageFragementAdapter(getContext(), getChildFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        getSiteUnReadMsgCount();


        if (TAB_APPLICATION_PREFERENTAIL == mType) {
            switchTab(TAB_ZHAN);
        } else {
            switchTab(TAB_GAME);
        }
    }


    public void getSiteUnReadMsgCount() {
        mPresneter.getUnReadMsgCount();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        RxBus.get().unregister(this);
    }

    @OnClick({R.id.game_notice, R.id.sys_notice, R.id.zhan_notice})
    public void onViewClicked(View view) {
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        switch (view.getId()) {
            case R.id.game_notice:
                switchTab(TAB_GAME);
                break;
            case R.id.sys_notice:
                switchTab(TAB_SYS);
                break;
            case R.id.zhan_notice:
                switchTab(TAB_ZHAN);
                break;
        }
    }

    @Override
    public void onGetUnReadCountResult(Object o) {
        SiteMsgUnReadCount siteMsgUnReadCount = (SiteMsgUnReadCount) o;
        if (mVTipMyNotice == null) return;
        if (siteMsgUnReadCount.getSysMessageUnReadCount() > 0 || siteMsgUnReadCount.getAdvisoryUnReadCount() > 0) {
            mVTipMyNotice.setVisibility(View.VISIBLE);
//            mVTipMyNotice.setText((siteMsgUnReadCount.getSysMessageUnReadCount()
//                    + siteMsgUnReadCount.getAdvisoryUnReadCount()) + "");
        } else {
            mVTipMyNotice.setVisibility(View.GONE);
        }

    }

    void switchTab(int tabIndex) {
        switch (tabIndex) {
            case TAB_GAME:
                mGameNotice.setSelected(true);
                mSysNotice.setSelected(false);
                mZhanNotice.setSelected(false);
                mViewPager.setCurrentItem(TAB_GAME);
                break;
            case TAB_SYS:
                mGameNotice.setSelected(false);
                mSysNotice.setSelected(true);
                mZhanNotice.setSelected(false);
                mViewPager.setCurrentItem(TAB_SYS);
                break;
            case TAB_ZHAN:
                mGameNotice.setSelected(false);
                mSysNotice.setSelected(false);
                mZhanNotice.setSelected(true);
                mViewPager.setCurrentItem(TAB_ZHAN);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        RxBus.get().post(PushDispatchManager.EVENT_PUSH_MSG_CENTER, "");
    }

    @Subscribe(tags = {@Tag(PushDispatchManager.EVENT_PUSH_MSG_CENTER)})
    public void getSiteUnReadMsg(String s) {
        mPresneter.getUnReadMsgCount();
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onDestroy() {
        if (mPresneter != null) {
            mPresneter.onDestory();
        }
        super.onDestroy();
    }
}
