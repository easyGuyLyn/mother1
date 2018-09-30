package com.dawoo.chessbox.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.ApiEnum;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.Notice;
import com.dawoo.chessbox.bean.RefreshhApis;
import com.dawoo.chessbox.bean.ServiceBean;
import com.dawoo.chessbox.bean.UserAccount;
import com.dawoo.chessbox.bean.WebSocketConnectBean;
import com.dawoo.chessbox.ipc.IPCSocketManager;
import com.dawoo.chessbox.mvp.presenter.MainActivityPresenter;
import com.dawoo.chessbox.mvp.view.IMainActivityView;
import com.dawoo.chessbox.net.HttpResult;
import com.dawoo.chessbox.push.PushDispatchManager;
import com.dawoo.chessbox.push.type.Topics;
import com.dawoo.chessbox.service.BestLineService;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.AutoLogin;
import com.dawoo.chessbox.util.DeviceUtils;
import com.dawoo.chessbox.util.DialogFramentManager;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.PushControlUtils;
import com.dawoo.chessbox.util.SharePreferenceUtil;
import com.dawoo.chessbox.util.SingleToast;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.util.TipsDialogFaragmentUtils;
import com.dawoo.chessbox.view.Utils.MediaplayerUtil;
import com.dawoo.chessbox.view.feagment_game.CapitalRecordDialogFragment;
import com.dawoo.chessbox.view.feagment_game.FavourableMessagwCenterDialogFragment;
import com.dawoo.chessbox.view.feagment_game.GameRecordDialogFragment;
import com.dawoo.chessbox.view.feagment_game.HomeGameFragment;
import com.dawoo.chessbox.view.feagment_game.LoginDialogFragment;
import com.dawoo.chessbox.view.feagment_game.PersonalMessageDialogFragment;
import com.dawoo.chessbox.view.feagment_game.ProfitDialogFragment;
import com.dawoo.chessbox.view.feagment_game.SecurityCenterDialogFragment;
import com.dawoo.chessbox.view.feagment_game.ShareDialogFragment;
import com.dawoo.chessbox.view.view.CustomDrawerLayout;
import com.dawoo.chessbox.view.view.SnowFlyView;
import com.dawoo.chessbox.view.view.VerticalSwipeRefreshLayout;
import com.dawoo.chessbox.view.view.marqueeText;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.SPTool;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.coretool.util.activity.DensityUtil;
import com.dawoo.coretool.util.math.BigDemicalUtil;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.pushsdk.callback.ConnectStatusListener;
import com.dawoo.pushsdk.callback.ReceiveMessageListener;
import com.dawoo.pushsdk.client.GBWebSocketClient;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener, IMainActivityView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainActivity  ";
    private long currentBackPressedTime = 0;    // 退出时间
    private static final int BACK_PRESSED_INTERVAL = 2000;  // 退出间隔
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.simple_navigation_drawer)
    CustomDrawerLayout mNavigationDrawer;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_return)
    ImageView mTvReturn;
    @BindView(R.id.swipeRefreshLayout)
    VerticalSwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_suishen_fl)
    TextView mTvSuishenFl;
    @BindView(R.id.tv_cangku_fl)
    TextView mTvCangkuFl;
    @BindView(R.id.game_name_et)
    EditText mGame_name_et;
    @BindView(R.id.rl_search)
    RelativeLayout mRl_search;
    @BindView(R.id.iv_logo)
    ImageView mIv_logo;
    @BindView(R.id.iv_bg_portrait)
    ImageView mIv_bg_portrait;
    @BindView(R.id.snow_fly)
    SnowFlyView mSnowFlyView;
    @BindView(R.id.toolbarleft)
    TextView mLeftToolbar;
    @BindView(R.id.toolbarright)
    TextView mRightToolbar;
    @BindView(R.id.tv_game_recharge)
    ImageView tvGameRecharge;
    @BindView(R.id.tv_welfareBox)
    ImageView tvWelfareBox;
    @BindView(R.id.tv_income)
    ImageView tvIncome;
    @BindView(R.id.tv_share)
    ImageView tvShare;
    @BindView(R.id.tv_prom_activities)
    ImageView tvPromActivities;
    @BindView(R.id.tv_user_center)
    ImageView tvUserCenter;
    @BindView(R.id.notice_tv)
    marqueeText mNoticeTv;

    /**
     * 抽屉里的
     */
    private View mRightSlideView;
    private TextView mtv_UserName;
    private TextView mTv_welfareValue;
    private TextView mtv_WelfareRecord;
    private TextView mtv_GameRecord;
    private TextView mtv_SecurityCenter;
    private TextView mtv_service;
    private ImageView mIv_Back;
    /**
     * 其他
     */
    private MainActivityPresenter mMainActivityPresenter;
    private long mInterval = 29L * 60L * 1000L;
    private Subscription mLineTaskSubscription;
    private boolean mIsStart;
    private UserAccount mAccount;
    private FragmentManager mFragmentManager;

    private LoginDialogFragment mLoginDialogFragment; //登录dialog
    private Handler mHandler = new Handler();

    private boolean mIsAutomatic = false;
    private DeviceUtils mDeviceUtils;

    private Animation mAnimation;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RxBus.get().register(this);
    }

    @Override
    protected void initViews() {
        initNavigationView();
        initRefreshUI();
        mDeviceUtils = new DeviceUtils(this, mLeftToolbar, mRightToolbar);
        mDeviceUtils.Device();
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.qt_btn_single_narrow);
    }


    /**
     * 初始化抽屉
     */
    private void initNavigationView() {

        mNavigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mRightSlideView = mNavigationView.inflateHeaderView(R.layout.layout_custom_navigationview_content);
        NavigationMenuView navigationMenuView = (NavigationMenuView) mNavigationView.getChildAt(0);
        navigationMenuView.setVerticalScrollBarEnabled(false);
        mtv_UserName = mRightSlideView.findViewById(R.id.tv_user_id);
        mTv_welfareValue = mRightSlideView.findViewById(R.id.tv_welfareValue);
        mtv_WelfareRecord = mRightSlideView.findViewById(R.id.tv_WelfareRecord);
        mtv_GameRecord = mRightSlideView.findViewById(R.id.tv_GameRecord);
        mtv_SecurityCenter = mRightSlideView.findViewById(R.id.tv_SecurityCenter);
        mtv_service = mRightSlideView.findViewById(R.id.tv_service);
        mIv_Back = mRightSlideView.findViewById(R.id.iv_right_back);
        mtv_WelfareRecord.setOnClickListener(this);
        mtv_GameRecord.setOnClickListener(this);
        mtv_SecurityCenter.setOnClickListener(this);
        mtv_service.setOnClickListener(this);
        mIv_Back.setOnClickListener(this);
    }

    private void resetNavigationHeight() {
        mRightSlideView.post(new Runnable() {
            @Override
            public void run() {
                mRightSlideView.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.getScreenHeight(MainActivity.this)));
            }
        });
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e(TAG, "横屏");
            mIv_bg_portrait.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e(TAG, "竖屏");
            mIv_bg_portrait.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void initData() {
        mIsAutomatic = false;
        mMainActivityPresenter = new MainActivityPresenter(this, this);
        mMainActivityPresenter.getCustomerService();
        mMainActivityPresenter.getNotice();
        mMainActivityPresenter.getTimeZone();
        mMainActivityPresenter.recovery();
        keepSessionAlive();
        startBestLineService();
        initPush();
        initHomeFragment();
        if (AutoLogin.isSuccessLogin) {
            RxBus.get().post(ConstantValue.EVENT_TYPE_LOGINED, "login");
        }
    }

    /**
     * 首页游戏
     */
    private void initHomeFragment() {
        mFragmentManager = getSupportFragmentManager();
        HomeGameFragment homeGameFragment = new HomeGameFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.fragment_content, homeGameFragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 注册推送能力
     */
    private void initPush() {
        //推送
        GBWebSocketClient.getInstance().init(getApplication());
        initConnectListener();
        initReceiveMessageListener();
    }

    /**
     * 刷新框架init
     */
    private void initRefreshUI() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        int[] colors = new int[]{ContextCompat.getColor(getBaseContext(), R.color.color_theme_blue),
                ContextCompat.getColor(getBaseContext(), R.color.color_theme_red),
                ContextCompat.getColor(getBaseContext(), R.color.color_theme_green_black),
                ContextCompat.getColor(getBaseContext(), R.color.color_theme_black)};
        mSwipeRefreshLayout.setColorSchemeColors(colors);
    }

    /**
     * 刷新框架回调
     */
    @Override
    public void onRefresh() {
        resetData();
        RxBus.get().post(ConstantValue.EVENT_REFRESH_MAIN, "");
    }

    /**
     * 刷新数据
     */
    private void resetData() {
        mMainActivityPresenter.getCustomerService();
        mMainActivityPresenter.getNotice();
        mMainActivityPresenter.getTimeZone();
        PushControlUtils.connect();
        if (DataCenter.getInstance().isLogin()) {
            //获取账户数据
            mMainActivityPresenter.getAccount();
        } else {
            mTvUserName.setText("请先登录");
            mTv_welfareValue.setText("---");
            mTvSuishenFl.setText("---");
            mTvCangkuFl.setText("---");
        }
    }

    /**
     * 控制返回按钮和搜索区域的是否可见
     *
     * @param type 0  搜索  1 返回按钮
     */
    public void setBackAndSearchVisible(int type, boolean isShow) {
        if (type == 0) {
            mRl_search.setVisibility((isShow == true) ? View.VISIBLE : View.GONE);
            mIv_logo.setVisibility((isShow == true) ? View.VISIBLE : View.GONE);
        } else if (type == 1) {
            mTvReturn.setVisibility((isShow == true) ? View.VISIBLE : View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        if (SystemUtil.isFastClick()) return;
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        switch (v.getId()) {
            case R.id.tv_WelfareRecord:
                startTextAnimations(mtv_WelfareRecord);
                CapitalRecordDialogFragment capitalRecordDialogFragment = new CapitalRecordDialogFragment();
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), capitalRecordDialogFragment);
                break;
            case R.id.tv_GameRecord:
                startTextAnimations(mtv_GameRecord);
                GameRecordDialogFragment mGameRecordDialogFragment = new GameRecordDialogFragment();
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), mGameRecordDialogFragment);
                break;
            case R.id.tv_SecurityCenter:
                startTextAnimations(mtv_SecurityCenter);
                SecurityCenterDialogFragment mSecurityCenterDialogFragment = SecurityCenterDialogFragment.newInstance(SecurityCenterDialogFragment.MODIFYPASSWORDFRAGMENT, true);
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), mSecurityCenterDialogFragment);
                break;
            case R.id.tv_service:
                startTextAnimations(mtv_service);
                ActivityUtil.startServiceDialog(getSupportFragmentManager());
                if (mNavigationDrawer.isDrawerOpen(GravityCompat.END)) {
                    mNavigationDrawer.closeDrawer(GravityCompat.END);
                }
                break;
            case R.id.iv_right_back:
                if (mNavigationDrawer.isDrawerOpen(GravityCompat.END)) {
                    mNavigationDrawer.closeDrawer(GravityCompat.END);
                }
                break;
        }
    }

    @OnClick({R.id.tv_prom_activities, R.id.tv_game_recharge, R.id.tv_welfareBox
            , R.id.tv_income, R.id.tv_share, R.id.tv_return, R.id.tv_user_center
            , R.id.rl_user, R.id.iv_refresh_money, R.id.iv_add_right, R.id.search_btn})
    public void onViewClicked(View view) {

        if (SystemUtil.isFastClick()) return;
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        if (!DataCenter.getInstance().isLogin() && view.getId() != R.id.tv_return) {
            //ActivityUtil.gotoLogin();
            if (mLoginDialogFragment == null) {
                mLoginDialogFragment = new LoginDialogFragment();
            }
            DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), mLoginDialogFragment);
            return;
        }

        switch (view.getId()) {
            case R.id.tv_prom_activities:
                startAnimations(tvPromActivities);
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), new FavourableMessagwCenterDialogFragment());
                break;
            case R.id.tv_game_recharge:
                startAnimations(tvGameRecharge);
                Intent intent1 = new Intent(this, DepositActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_add_right:
                Intent intent = new Intent(this, DepositActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_welfareBox:
//                WelfareWarehouseDialogFragment mWelfareWarehouseDialogFragment = new WelfareWarehouseDialogFragment();
//                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), mWelfareWarehouseDialogFragment);
                startAnimations(tvWelfareBox);
                TipsDialogFaragmentUtils.showTipsDialogFragmentdialog(getSupportFragmentManager(), "功能正在开发中...");
                break;
            case R.id.tv_income:
                startAnimations(tvIncome);
                ProfitDialogFragment mProfitDialogFragment = new ProfitDialogFragment();
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), mProfitDialogFragment);
                break;
            case R.id.tv_share:
                startAnimations(tvShare);
                ShareDialogFragment mShareDialogFragment = new ShareDialogFragment();
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), mShareDialogFragment);
                break;
            case R.id.tv_return:
                RxBus.get().post(ConstantValue.EVENT_MAIN_BACK, "");
                break;
            case R.id.tv_user_center:
                startAnimations(tvUserCenter);
                resetNavigationHeight();
                mNavigationDrawer.openDrawer(GravityCompat.END);
                break;
            case R.id.rl_user:
                if (mMainActivityPresenter != null) {
                    mMainActivityPresenter.alwaysRequest();
                }
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), new PersonalMessageDialogFragment());
                break;
            case R.id.iv_refresh_money:
                mIsAutomatic = true;
                if (mAccount != null) {

                    if (mAccount.getUser().isAutoPay()) {
                        mMainActivityPresenter.recovery();
                    } else {
                        mMainActivityPresenter.refreshAPI();
                    }

                } else {
                    if (mLoginDialogFragment == null) {
                        mLoginDialogFragment = new LoginDialogFragment();
                    }
                    DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), mLoginDialogFragment);
                    SingleToast.showMsg("请重新登录");
                }

                break;
            case R.id.search_btn:
                String gameName = mGame_name_et.getText().toString().trim();
                RxBus.get().post(ConstantValue.EVENT_MAIN_GAME, gameName == null ? "" : gameName);
                break;
        }
    }

    private void startAnimations(ImageView imageView) {
        if (mAnimation != null) {
            imageView.startAnimation(mAnimation);
        } else {
            mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.qt_btn_single_narrow);
            imageView.startAnimation(mAnimation);
        }
    }

    private void startTextAnimations(TextView textView) {
        if (mAnimation != null) {
            textView.startAnimation(mAnimation);
        } else {
            mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.qt_btn_single_narrow);
            textView.startAnimation(mAnimation);
        }
    }


    @Override
    public void onNoticeResult(Object o) {
        if (o != null && o instanceof Notice) {
            // 设置公告
            StringBuilder stringBuilder = new StringBuilder();
            for (Notice.AnnouncementBean bean : ((Notice) o).getAnnouncement()) {
                stringBuilder.append(bean.getContent());
            }
            String showAnnouncement = stringBuilder.toString();
            showAnnouncement = showAnnouncement.replaceAll("\t|\r|\n", " ");
            if (!TextUtils.isEmpty(showAnnouncement)) {
                mNoticeTv.setVisibility(View.VISIBLE);
                mNoticeTv.setText(showAnnouncement);
//                TextView textView = new TextView(MainActivity.this);
//                textView.setGravity(Gravity.CENTER);
//                textView.setTextColor(getResources().getColor(R.color.white));
//                textView.setText(showAnnouncement);
//                textView.setPadding(0, DensityUtil.dp2px(this, 3), 0, 0);
//                mNoticeTv.stopScroll();
//                mNoticeTv.clearAllView();
//                mNoticeTv.addViewInQueue(textView);
//                mNoticeTv.setScrollSpeed(8);
//                mNoticeTv.setScrollDirection(MarqueeView.RIGHT_TO_LEFT);
//                mNoticeTv.setViewMargin(15);
//                mNoticeTv.startScroll();
            } else {
                mNoticeTv.setVisibility(View.GONE);
            }
        }
        //  收起刷新
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onAccountResult(Object o) {
        initAccountData(o);
    }

    @Override
    public void onAssertResult(Object o) {
        Object o1 = mMainActivityPresenter.rePlaceData(o, mAccount);
        initAccountData(o1);
    }

    @Override
    public void getTimeZone(Object o) {
        if (o != null && o instanceof String) {
            String timeZone = (String) o;
            Log.e(TAG, timeZone + "");
            SharePreferenceUtil.saveTimeZone(getBaseContext(), timeZone);
        }
    }

    @Override
    public void onAlwaysRequestResult(Object o) {

    }

    @Override
    public void onCustomerService(Object o) {
        if (o != null && o instanceof ServiceBean) {
            ServiceBean serviceBean = (ServiceBean) o;
            String url = serviceBean.getCustomerUrl();
            Log.e(TAG, "getServiceUrl ==> " + url);
            SPTool.remove(getApplicationContext(), ConstantValue.KEY_CUSTOMER_SERVICE);
            SPTool.put(getApplicationContext(), ConstantValue.KEY_CUSTOMER_SERVICE, url);
            SPTool.remove(getApplicationContext(), ConstantValue.KEY_CUSTOMER_SERVICE_ISINLAY);
            SPTool.put(getApplicationContext(), ConstantValue.KEY_CUSTOMER_SERVICE_ISINLAY, serviceBean.isIsInlay());
        }
    }

    @Override
    public void onRecovery(HttpResult refreshhApis) {
        Log.e(TAG, "回收单个api： " + refreshhApis.getMessage());
        mMainActivityPresenter.getAccount();
    }


    @Override
    public void onRecoveryResult(Object o) {
        if (o != null && o instanceof HttpResult) {
            HttpResult httpResult = (HttpResult) o;
            if (mIsAutomatic) {
                SingleToast.showMsg(httpResult.getMessage());
            }
            mMainActivityPresenter.getAssert();
        } else {
            if (mIsAutomatic) {
                SingleToast.showMsg("回收失败!");
            }
        }
    }

    @Override
    public void onRefreshResult(Object o) {
        if (o != null && o instanceof RefreshhApis) {
            SingleToast.showMsg("刷新成功!");
            if (o != null && o instanceof RefreshhApis) {
                // 刷新apis
                // 请求刷新 account
                mMainActivityPresenter.getAssert();
            }
        } else {
            SingleToast.showMsg("刷新失败!");
        }
    }

    /**
     * 账户数据
     */
    private void initAccountData(Object o) {
        if (o != null && o instanceof UserAccount) {
            // 设置账户
            mAccount = ((UserAccount) o);
            UserAccount.UserBean userBean = mAccount.getUser();
            mTvUserName.setText(userBean.getUsername());
            mtv_UserName.setText(userBean.getUsername());
            DataCenter.getInstance().setRealName(userBean.getRealName());
            DataCenter.getInstance().setNickName(userBean.getUsername());
            DataCenter.getInstance().setBalanceAccount(userBean.getTotalAssets() + "");
            DataCenter.getInstance().setWalletBalance(userBean.getWalletBalance() + "");
            DataCenter.getInstance().setLoginTimeLast(userBean.getLastLoginTime());
            mTv_welfareValue.setText(BigDemicalUtil.moneyFormat(userBean.getWalletBalance()));
            mTvSuishenFl.setText(BigDemicalUtil.moneyFormat(userBean.getWalletBalance()));
            mTvCangkuFl.setText("---");
        }
    }

    /**
     * push连接实时回调
     */
    private void initConnectListener() {
        GBWebSocketClient.getInstance().setConnectListener(new ConnectStatusListener() {
            @Override
            public void onConnectStatus(int code, String status) {
                Log.e("GBMessageService", status);
            }
        });
    }

    /**
     * push 接受到消息内容  json串
     */
    private void initReceiveMessageListener() {
        GBWebSocketClient.getInstance().setReceiveMessageLister(new ReceiveMessageListener() {
            @Override
            public void onReceiveJson(String json) {
                Log.e("GBMessageService", json);
                PushDispatchManager.getInstance().disPatch(json);
            }
        });
    }

    /**
     * 连接push
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_CONNECT_WEB_SOCKET)})
    public void connect(WebSocketConnectBean webSocketConnectBean) {
        GBWebSocketClient.getInstance().login(webSocketConnectBean.getUrl()
                , webSocketConnectBean.getSession()
                , Topics.SUBSCRIBE_TOPICS
                , webSocketConnectBean.getDomain()
                , NetUtil.getOkHttpClientBuilderForHttps().build());
    }

    /**
     * 切断push
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_DISCONNECT_WEB_SOCKET)})
    public void disConnect(String s) {
        GBWebSocketClient.getInstance().logout();
    }


    /**
     * 退出登录登录后，回调 刷新某些ui
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_LOGOUT)})
    public void logout(String s) {
        Log.e(TAG, "logout ");
        resetData();
        //断开推送
        PushControlUtils.disConnect();

    }

    /**
     * 登录成功后，回调 刷新某些ui
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_LOGINED)})
    public void logined(String s) {
        Log.e(TAG, "logined ");
        //连接推送
        PushControlUtils.connect();
        //获取账户数据
        mMainActivityPresenter.getAccount();
    }

    /**
     * 跳存款
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_GOTOTAB_DEPOSIT)})
    public void goToDesport(String s) {
        Log.e(TAG, "goToDesport ");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, DepositActivity.class);
                startActivity(intent);
            }
        }, 500);
    }


    /**
     * 刷新单个游戏api
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_REFRSH_API)})
    public void refreshApi(String apiId) {
        mMainActivityPresenter.recovery(apiId);
    }

    /**
     * 刷新账户数据
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_ACCOUNT)})
    public void updateAccount(String s) {
        LogUtils.d(s);
        //  加载账户数据
        mMainActivityPresenter.getAccount();
    }

    /**
     * 展现资金记录弹窗
     */
    @Subscribe(tags = {@Tag(ConstantValue.SHOW_CAPITALRECORDDIALOGFRAGMENR)})
    public void showCapitalRecordDialog(String s) {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                CapitalRecordDialogFragment capitalRecordDialogFragment = new CapitalRecordDialogFragment();
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), capitalRecordDialogFragment);
            }
        }, 500);
    }

    /**
     * 展现优惠记录弹窗
     */
    @Subscribe(tags = {@Tag(ConstantValue.SHOW_PROME_RECORD)})
    public void showPromeDialog(String s) {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), new FavourableMessagwCenterDialogFragment());
            }
        }, 500);
    }

    /**
     * 游戏logo
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_MAIN_GAME_APILOGO)})
    public void showApiLogo(String apiId) {
        String drawableId = ApiEnum.getDrawableByCode(apiId);
        if (TextUtils.isEmpty(drawableId)) {
            mIv_logo.setVisibility(View.GONE);
        } else {
            mIv_logo.setImageResource(getResources().getIdentifier(drawableId, "mipmap", getPackageName()));
        }
    }


    /**
     * 展现登录弹窗
     */
    @Subscribe(tags = {@Tag(ConstantValue.SHOW_LOGINDIALOGFRAGMENR)})
    public void showLoginDialog(String s) {
        mHandler.postDelayed(new Runnable() {
            public void run() {
                SingleToast.showMsg("您还未登录,请重新登录");
                if (mLoginDialogFragment == null) {
                    mLoginDialogFragment = new LoginDialogFragment();
                }
                DialogFramentManager.getInstance().retainLastDialog(getSupportFragmentManager(), mLoginDialogFragment);
                // DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), mLoginDialogFragment);
            }
        }, 500);

    }


    /**
     * 跳转安全中心弹窗
     */
    @Subscribe(tags = {@Tag(ConstantValue.SECURITY_CENTER_DIALOGFRAGMENT)})
    public void showBankcardFragmentDialog(Integer s) {
        SecurityCenterDialogFragment mSecurityCenterDialogFragment;
        if (s == SecurityCenterDialogFragment.BANKCARDFRAGMENT) {
            mSecurityCenterDialogFragment = SecurityCenterDialogFragment.newInstance(s, false);
            DialogFramentManager.getInstance().retainLastDialog(getSupportFragmentManager(), mSecurityCenterDialogFragment);
        } else if (s == SecurityCenterDialogFragment.MODIFYSAFETYCODEFRAGMENT) {
            mSecurityCenterDialogFragment = SecurityCenterDialogFragment.newInstance(s, true);
            DialogFramentManager.getInstance().showDialog(getSupportFragmentManager(), mSecurityCenterDialogFragment);
        }
    }

    /**
     * 网络异常 收起刷新
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_NETWORK_EXCEPTION)})
    public void shrinkRefreshView(String s) {
        LogUtils.d(s);
        //  收起刷新
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 重力感應  橫屏切換
     */
    @Subscribe(tags = {@Tag(ConstantValue.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)})
    public void landScapeReverse(String s) {
        LogUtils.d(s);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    /**
     * 重力感應  豎屏切換
     */
    @Subscribe(tags = {@Tag(ConstantValue.SCREEN_ORIENTATION_LANDSCAPE)})
    public void landScape(String s) {
        LogUtils.d(s);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 保持session更新
     * 不掉线
     */
    private void keepSessionAlive() {
        mLineTaskSubscription = Observable.interval(mInterval, mInterval, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        DataCenter dataCenter = DataCenter.getInstance();
                        if (mMainActivityPresenter != null && dataCenter != null && dataCenter.isLogin()) {
                            mMainActivityPresenter.alwaysRequest();
                        }
                    }
                });
    }


    //*********************优选线路********************
    BestLineService.BestLineBinder mBestLineBinder;
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBestLineBinder = (BestLineService.BestLineBinder) service;
            mBestLineBinder.startGetBestLine((MainActivity.this));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (mBestLineBinder != null) {
                mBestLineBinder.stopGetBestLine();
            }
        }
    };

    /**
     * 线路自更新
     */
    private void startBestLineService() {
        mIsStart = SharePreferenceUtil.isStartBestLineService(getBaseContext());
        if (mIsStart) {
            Intent intent = new Intent(MainActivity.this, BestLineService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }


    @Override
    protected void onDestroy() {
        RxBus.get().unregister(this);
        mMainActivityPresenter.onDestory();
        if (mLineTaskSubscription != null && !mLineTaskSubscription.isUnsubscribed()) {
            mLineTaskSubscription.unsubscribe();
        }
        if (mConnection != null && mIsStart) {
            unbindService(mConnection);
        }
        if (mAnimation != null) {
            mAnimation.cancel();
            mAnimation = null;
        }
        DialogFramentManager.getInstance().clearDialog();
        super.onDestroy();
    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.END)) {
            mNavigationDrawer.closeDrawer(GravityCompat.END);
        } else {
            closeApp();
        }
    }

    /**
     * closeApp
     */
    private void closeApp() {
        // 判断时间间隔
        if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis();
            SingleToast.showMsg("再按一次返回键退出程序");
        } else {
            // 退出
            IPCSocketManager.getInstance().destroy();
            ActivityStackManager.getInstance().finishAllActivity();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e("SoundPoolstatus", "onStart");
        MediaplayerUtil.newInstance().play();
        if (mSnowFlyView != null)
            mSnowFlyView.startAnimation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.END)) {
            mNavigationDrawer.closeDrawer(GravityCompat.END);
        }
        if (mSnowFlyView != null)
            mSnowFlyView.stopAnimationDely();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDeviceUtils != null) {
            mDeviceUtils.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDeviceUtils != null) {
            mDeviceUtils.onPause();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
