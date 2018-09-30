package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.activity.DensityUtil;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.adapter.HomeAdapter.AdapterSet.BannerConfigInit;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.SiteApiBean;
import com.dawoo.chessbox.bean.UrlBean;
import com.dawoo.chessbox.mvp.presenter.GameFragmentPresenter;
import com.dawoo.chessbox.mvp.view.IGameFragmentView;
import com.dawoo.chessbox.net.GlideApp;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.BitmapUtils;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.RoundCornersTransformation;
import com.dawoo.chessbox.util.SingleToast;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.view.Utils.snap.PagingScrollHelper;
import com.dawoo.chessbox.view.activity.MainActivity;
import com.dawoo.chessbox.view.view.SpaceItemDecoration;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * archar  主页游戏
 **/
public class HomeGameFragment extends Fragment implements IGameFragmentView, PagingScrollHelper.onPageChangeListener {
    //1 一级页面  2 二级api页面  3 三级api页面
    public static final int MODE_ONE = 1;
    public static final int MODE_TWO = 2;
    public static final int MODE_THREE_GAME = 3;
    public static final int START_ANIMATION_LEFT = 0;
    public static final int START_ANIMATION_TWO_RIGHT = 1;

    Unbinder unbinder;
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.ll_left)
    LinearLayout mLlLeft;
    @BindView(R.id.rv_game_list)
    RecyclerView mRvGameList;
    @BindView(R.id.iv_prev)
    ImageView mIvPrev;
    @BindView(R.id.iv_next)
    ImageView mIvNext;

    private GameFragmentPresenter mGameFragmentPresenter;
    private List<SiteApiBean.BannerBean> mBannerBeans = new ArrayList<>();//banner
    private Stack<List<SiteApiBean.SiteApiRelationBean>> mSiteApiRelationBeansStack = new Stack<>();//存放历史的游戏数据体栈
    private GameListAdapter mGameListAdapter;
    private int mUiType = MODE_ONE;
    private PagingScrollHelper mPagingScrollHelper = new PagingScrollHelper();
    private int mCurrentAllPage;
    private int mCurrentPagePosition = 0;
    private Animation mAnimation;
    private TranslateAnimation mTranslateAnimation;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler mHandler;


    public HomeGameFragment() {
    }

    @Override
    public void onDestroyView() {
        mGameFragmentPresenter.onDestory();
        if (mAnimation != null) {
            mAnimation.cancel();
            mAnimation = null;
        }
        if (mTranslateAnimation != null) {
            mTranslateAnimation.cancel();
            mTranslateAnimation = null;
        }
        stopTimer();
        RxBus.get().unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qt_layout_home_fragmnet, container, false);
        RxBus.get().register(this);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        initRecycleView();
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.qt_btn_narrow);
        initAnimation();
        mGameFragmentPresenter = new GameFragmentPresenter(getActivity(), this);
        mGameFragmentPresenter.getSiteApiRelation();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mTranslateAnimation == null) {
                    initAnimation();
                }
                switch (msg.what) {
                    case START_ANIMATION_LEFT:
                        mIvPrev.startAnimation(mTranslateAnimation);
                        break;
                    case START_ANIMATION_TWO_RIGHT:
                        mIvNext.startAnimation(mTranslateAnimation);
                        break;
                }
            }
        };

    }

    private void initAnimation() {
        mTranslateAnimation = new TranslateAnimation(0, -10, 0, 0);
        mTranslateAnimation.setInterpolator(new OvershootInterpolator());
        mTranslateAnimation.setDuration(300);
        mTranslateAnimation.setRepeatCount(6);
        mTranslateAnimation.setRepeatMode(Animation.REVERSE);
    }

    private void initRecycleView() {
        mGameListAdapter = new GameListAdapter(R.layout.qt_game_item);
        mRvGameList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        if (mRvGameList.getItemDecorationCount() == 0) {
            mRvGameList.addItemDecoration(new SpaceItemDecoration(DensityUtil.dp2px(getActivity(), 0)
                    , DensityUtil.dp2px(getActivity(), 1)));
        }
        mRvGameList.setAdapter(mGameListAdapter);
        mPagingScrollHelper.setUpRecycleView(mRvGameList);
        mPagingScrollHelper.setOnPageChangeListener(this);
        mPagingScrollHelper.updateLayoutManger();
    }


    @Override
    public void onSiteApiRelationResult(Object o) {
        if (null != o && o instanceof SiteApiBean) {
            SiteApiBean siteApiBean = (SiteApiBean) o;
            if (siteApiBean.getBanner() != null) {
                mBannerBeans.addAll(siteApiBean.getBanner());
            }

            if (siteApiBean.getSiteApiRelation() != null) {
                mSiteApiRelationBeansStack.push(siteApiBean.getSiteApiRelation());
            }
            setRecycleViewData(null);
            initBanner();
            setUIVisible();
        }
    }

    @Override
    public void onGameLinkResult(Object o, int apiId) {
        if (o != null && o instanceof UrlBean) {
            UrlBean urlBean = (UrlBean) o;
            if (urlBean.getGameLink() == null) {
                SingleToast.showMsg(urlBean.getGameMsg());
                return;
            }

            String url = urlBean.getGameLink();
            url = NetUtil.handleUrl(url);
            ActivityUtil.startGameWebView(url, urlBean.getGameMsg(), ConstantValue.WEBVIEW_TYPE_GAME_FULLSCREEN_ALWAYS, 3, apiId);
        }
    }

    /**
     * banner
     */
    private void initBanner() {
        List images = new ArrayList();
        for (int i = 0; i < mBannerBeans.size(); i++) {
            images.add(NetUtil.handleUrl(mBannerBeans.get(i).getCover()));
        }
        BannerConfigInit init = new BannerConfigInit(mBanner, images, null, new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (!TextUtils.isEmpty(mBannerBeans.get(position).getLink())) {
                    ActivityUtil.startWebView(NetUtil.handleUrl(mBannerBeans.get(position).getLink()), "", ConstantValue.WEBVIEW_TYPE_THIRD_ORDINARY, 1);
                }
            }
        });
        init.start();
    }

    /**
     * 更新列表数据
     * 参数为 搜索数据
     */
    private void setRecycleViewData(List<SiteApiBean.SiteApiRelationBean> siteApiRelationBeans) {
        mIvPrev.setVisibility(View.GONE);
        mIvNext.setVisibility(View.GONE);
        if (mSiteApiRelationBeansStack.size() > 0) {
            List<SiteApiBean.SiteApiRelationBean> beans = mSiteApiRelationBeansStack.get(mSiteApiRelationBeansStack.size() - 1);
            for (SiteApiBean.SiteApiRelationBean bean : beans) {
                Log.e("Home ", bean.getName() + "/ " + bean.getApiId() + " / " + bean.getApiTypeId());
            }
            initRecycleView();
            if (siteApiRelationBeans != null) {
                mGameListAdapter.setNewData(siteApiRelationBeans);
            } else {
                mGameListAdapter.setNewData(beans);
            }
            mPagingScrollHelper.scrollToPosition(0);
            mCurrentPagePosition = 0;
            mRvGameList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCurrentAllPage = mPagingScrollHelper.getPageCount();
                    Log.e("HomeGame CurrentAllPage", mCurrentAllPage + "");
                    if (mCurrentPagePosition == 0 && mCurrentAllPage > 1) {
                        mIvNext.setVisibility(View.VISIBLE);
                    }
                }
            }, 100);
        }
    }

    /**
     * 刷新数据
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_REFRESH_MAIN)})
    public void refresh(String s) {
        mBannerBeans.clear();
        mUiType = MODE_ONE;
        while (!mSiteApiRelationBeansStack.empty()) {
            mSiteApiRelationBeansStack.pop();
        }
        mGameFragmentPresenter.getSiteApiRelation();
    }

    /**
     * 返回上一层级
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_MAIN_BACK)})
    public void back(String s) {
        mSiteApiRelationBeansStack.pop();
        if (mSiteApiRelationBeansStack.size() == 1) {
            mUiType = MODE_ONE;
        } else if (mSiteApiRelationBeansStack.size() == 2) {
            mUiType = MODE_TWO;
        } else if (mSiteApiRelationBeansStack.size() == 3) {
            mUiType = MODE_THREE_GAME;
        }
        setUIVisible();
        setRecycleViewData(null);
    }

    /**
     * 搜索游戏
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_MAIN_GAME)})
    public void searchGame(String s) {
        if (!TextUtils.isEmpty(s)) {
            List<SiteApiBean.SiteApiRelationBean> siteApiRelationBeans = mSiteApiRelationBeansStack.get(mSiteApiRelationBeansStack.size() - 1);
            List<SiteApiBean.SiteApiRelationBean> findRelationBeans = new ArrayList<>();
            for (SiteApiBean.SiteApiRelationBean bean : siteApiRelationBeans) {
                if (bean.getName().contains(s)) {
                    findRelationBeans.add(bean);
                }
            }
            setRecycleViewData(findRelationBeans);
        } else {
            setRecycleViewData(null);
        }
    }

    /**
     * 设置banner是否可见
     * 设置返回键是否可见
     * 设置搜索游戏是否可见
     */
    private void setUIVisible() {
        if (mUiType == MODE_ONE) {
            mLlLeft.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).setBackAndSearchVisible(1, false);
            ((MainActivity) getActivity()).setBackAndSearchVisible(0, false);
        } else if (mUiType == MODE_TWO) {
            mLlLeft.setVisibility(View.GONE);
            ((MainActivity) getActivity()).setBackAndSearchVisible(1, true);
            ((MainActivity) getActivity()).setBackAndSearchVisible(0, false);
        } else if (mUiType == MODE_THREE_GAME) {
            mLlLeft.setVisibility(View.GONE);
            ((MainActivity) getActivity()).setBackAndSearchVisible(1, true);
            ((MainActivity) getActivity()).setBackAndSearchVisible(0, true);
        }
    }

    @OnClick({R.id.iv_prev, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_prev:
                mPagingScrollHelper.scrollToPosition(mCurrentPagePosition - 1);
                break;
            case R.id.iv_next:
                mPagingScrollHelper.scrollToPosition(mCurrentPagePosition + 1);
                break;
        }
    }

    private void setJTUI() {
        if (mCurrentPagePosition == 0 && mCurrentAllPage > 1) {
            mIvPrev.setVisibility(View.GONE);
            mIvNext.setVisibility(View.VISIBLE);
            startTimer(START_ANIMATION_TWO_RIGHT);
        } else if (mCurrentPagePosition > 0 && mCurrentPagePosition < mCurrentAllPage - 1) {
            mIvPrev.setVisibility(View.VISIBLE);
            mIvNext.setVisibility(View.VISIBLE);
            stopTimer();
        } else if (mCurrentPagePosition == mCurrentAllPage - 1 && mCurrentAllPage > 1) {
            mIvPrev.setVisibility(View.VISIBLE);
            mIvNext.setVisibility(View.GONE);
            startTimer(START_ANIMATION_LEFT);
        }
    }

    @Override
    public void onPageChange(int index) {
        Log.e("HomeGameF PageIndex", index + "");
        mCurrentPagePosition = index;
        setJTUI();
    }


    private class GameListAdapter extends BaseQuickAdapter {

        public GameListAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            SiteApiBean.SiteApiRelationBean siteApiRelationBean = (SiteApiBean.SiteApiRelationBean) item;
            ImageView pic = helper.getView(R.id.game_icon_iv);
            RelativeLayout linearlayout = helper.getView(R.id.linearlayout);
            RelativeLayout frameLayout = helper.getView(R.id.frame_layout);
            TextView name = helper.getView(R.id.name);
            if (mUiType == MODE_THREE_GAME) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(BitmapUtils.dp2px(getActivity(), 105), RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin = BitmapUtils.dp2px(getActivity(), 15);
                linearlayout.setLayoutParams(layoutParams);  //设置图片宽高
                RelativeLayout.LayoutParams imaglayoutParams = new RelativeLayout.LayoutParams(BitmapUtils.dp2px(getActivity(), 90), BitmapUtils.dp2px(getActivity(), 90));
                pic.setLayoutParams(imaglayoutParams);  //设置图片宽高
                pic.setBackgroundResource(R.drawable.home_image_fillet);
                frameLayout.setLayoutParams(imaglayoutParams);  //设置图片宽高
                frameLayout.setVisibility(View.VISIBLE);
                name.setText(siteApiRelationBean.getName());
                String url = NetUtil.handleUrl(siteApiRelationBean.getCover());
                RequestOptions options = new RequestOptions().placeholder(R.mipmap.loading);
                RoundCornersTransformation transformation = new RoundCornersTransformation(mContext, BitmapUtils.dp2px(mContext, 20), RoundCornersTransformation.CornerType.ALL);
                GlideApp.with(mContext)
                        .load(url)
                        .apply(options)
                        .transforms(new FitCenter(), transformation)
                        .into(pic);
            } else {
                frameLayout.setVisibility(View.GONE);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(BitmapUtils.dp2px(getActivity(), 140), RelativeLayout.LayoutParams.WRAP_CONTENT);
                linearlayout.setLayoutParams(layoutParams);  //设置图片宽高
                RelativeLayout.LayoutParams imaglayoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                pic.setLayoutParams(imaglayoutParams);  //设置图片宽高
                String url = NetUtil.handleUrl(siteApiRelationBean.getCover());
                RequestOptions options = new RequestOptions().placeholder(R.mipmap.loading);
                GlideApp.with(mContext)
                        .load(url)
                        .apply(options)
                        .into(pic);
            }

            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAnimation != null) {
                        v.startAnimation(mAnimation);
                    }
                    clickGameNext(siteApiRelationBean);
                }
            });
        }
    }

    /**
     * 点击某个游戏
     *
     * @param siteApiRelationBean
     */
    private void clickGameNext(SiteApiBean.SiteApiRelationBean siteApiRelationBean) {
        if (SystemUtil.isFastClick()) return;
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        if (siteApiRelationBean.getType().equals("apiType")) {
            mUiType = MODE_TWO;
            mSiteApiRelationBeansStack.push(siteApiRelationBean.getRelation());
            setUIVisible();
            setRecycleViewData(null);
        } else if (siteApiRelationBean.getType().equals("api")) {
            mUiType = MODE_THREE_GAME;
            mSiteApiRelationBeansStack.push(siteApiRelationBean.getRelation());
            setUIVisible();
            setRecycleViewData(null);
            RxBus.get().post(ConstantValue.EVENT_MAIN_GAME_APILOGO, siteApiRelationBean.getApiId() + "");
        } else if (siteApiRelationBean.getType().equals("game")) {
            String gameMsg = siteApiRelationBean.getGameMsg() == null ? "游戏异常" : ((String) siteApiRelationBean.getGameMsg());
            goToWebView(siteApiRelationBean.getGameLink(), gameMsg, siteApiRelationBean.getApiId());
        }
    }

    /**
     * 去打游戏了
     *
     * @param link
     */
    private void goToWebView(String link, String msg, int apiId) {
        if (!DataCenter.getInstance().isLogin()) {
            ActivityUtil.gotoLogin();
            return;
        }
        Log.e("Home", link);
        if (link.startsWith("/mobile-api")) {
            // link含有 /mobile-api
            // 非电子游戏 请求链接
            mGameFragmentPresenter.getGameLink(link, apiId);
        } else {
            String ip = DataCenter.getInstance().getIp();
            ActivityUtil.startGameWebView(ip + link, msg, ConstantValue.WEBVIEW_TYPE_GAME_FULLSCREEN_ALWAYS, 3, apiId);
        }
    }

    private void startTimer(int id) {
        stopTimer();
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if (id == START_ANIMATION_LEFT) {
                        sendMessage(START_ANIMATION_LEFT);
                    }
                    if (id == START_ANIMATION_TWO_RIGHT) {
                        sendMessage(START_ANIMATION_TWO_RIGHT);
                    }
                }
            };
        }
        if (mTimerTask != null && mTimer != null) {
            mTimer.schedule(mTimerTask, 5000,5000);
        }

    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void sendMessage(int id) {
        if (mHandler != null) {
            Message message = Message.obtain(mHandler, id);
            mHandler.sendMessage(message);
        }
    }


}
