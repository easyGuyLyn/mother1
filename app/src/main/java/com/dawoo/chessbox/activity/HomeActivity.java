package com.dawoo.chessbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.chessbox.MusicService;
import com.dawoo.chessbox.MyApplication;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.adapter.RadioAdapter;
import com.dawoo.chessbox.adapter.WapHeaderAndFooterAdapter;
import com.dawoo.chessbox.base.BaseMusicActivity;
import com.dawoo.chessbox.beans.LiveChannelBean;
import com.dawoo.chessbox.beans.PlaceBean;
import com.dawoo.chessbox.dialog.LocalTypeDialog;
import com.dawoo.chessbox.dialog.NormalAskDialog;
import com.dawoo.chessbox.http.serviceapi.RadioApi;
import com.dawoo.chessbox.http.subscribers.HttpSubscriber;
import com.dawoo.chessbox.http.subscribers.SubscriberOnListener;
import com.dawoo.chessbox.log.MyLog;
import com.dawoo.chessbox.u.ActivityUtil;
import com.dawoo.chessbox.view.activity.MoreActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ywl on 2018/1/10.
 */

public class HomeActivity extends BaseMusicActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.swipRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.ly_status)
    LinearLayout lyStatus;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.simple_navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.iv_sldie)
    ImageView mIv_sldie;
    @BindView(R.id.iv_me_dot_t)
    ImageView ivMeDotT;
    @BindView(R.id.fragment_content)
    FrameLayout fragmentContent;
    @BindView(R.id.tv_gb)
    TextView tvGb;
    @BindView(R.id.tv_news)
    TextView tvNews;
    @BindView(R.id.tv_more)
    TextView tv_more;


    private List<PlaceBean> placeBeans;
    private RadioAdapter radioAdapter;
    private WapHeaderAndFooterAdapter headerAndFooterAdapter;
    private List<LiveChannelBean> datas;
    private TextView tvLoadMsg;

    private int pageSize = 10;
    private int currentPage = 1;
    private boolean isLoading = false;
    private String channelPlaceId = "3225";
    private long mPressedTime = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_layout1);
        ButterKnife.bind(this);
        setTitle("网络广播");
        setRightView(R.mipmap.icon_more);
        initNavigationView();
        showDadaLoad();
        initAdapter();
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_ec4c48));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    isLoading = true;
                    currentPage = 1;
                    getLiveList();
                }
            }
        });
        getLiveList();
        getLocalData();
        MyLog.d("token is :" + MyApplication.getInstance().getToken());


        tvGb.setTextColor(getResources().getColor(R.color.colorAccent));
    }


    /**
     * 初始化抽屉
     */
    private void initNavigationView() {
        mIv_sldie.setVisibility(View.VISIBLE);
        mIv_sldie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationDrawer.openDrawer(GravityCompat.START);
            }
        });

        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    private void initAdapter() {
        datas = new ArrayList<>();
        radioAdapter = new RadioAdapter(this, datas);
        radioAdapter.setOnItemClickListener(new RadioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LiveChannelBean liveChannelBean, int position) {
            }
        });
        headerAndFooterAdapter = new WapHeaderAndFooterAdapter(radioAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        View footerView = LayoutInflater.from(this).inflate(R.layout.footer_layout, recyclerView, false);
        tvLoadMsg = footerView.findViewById(R.id.tv_loadmsg);
        headerAndFooterAdapter.addFooter(footerView);

        recyclerView.setAdapter(headerAndFooterAdapter);
        headerAndFooterAdapter.notifyDataSetChanged();

        headerAndFooterAdapter.setOnloadMoreListener(recyclerView, new WapHeaderAndFooterAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isLoading) {
                    getLiveList();
                }
            }

            @Override
            public void onClickLoadMore() {

            }

            @Override
            public void onItemClick(View view, int position) {

            }
        });

        radioAdapter.setOnItemClickListener(new RadioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LiveChannelBean liveChannelBean, int position) {
                if (liveChannelBean.getStreams() != null && liveChannelBean.getStreams().size() > 0) {
                    getPlayBean().setName(liveChannelBean.getName());
                    getPlayBean().setSubname(liveChannelBean.getLiveSectionName());
                    getPlayBean().setChannelId(liveChannelBean.getId());
                    getPlayBean().setImg(liveChannelBean.getImg());
                    getPlayBean().setUrl(liveChannelBean.getStreams().get(0).getUrl());
                    getPlayBean().setTiming(1);
                    liveUrl = liveChannelBean.getStreams().get(0).getUrl();//记录当前直播url
                    startActivity(HomeActivity.this, PlayActivity.class);
                } else {
                    showToast("数据出错,请稍后再试");
                }
            }
        });

    }


    @Override
    public void onClickMenu() {
        super.onClickMenu();
        if (placeBeans != null) {
            LocalTypeDialog localTypeDialog = new LocalTypeDialog(this);
            localTypeDialog.show();
            localTypeDialog.setDatas(placeBeans, channelPlaceId);
            localTypeDialog.setOnTypeSelectedListener(new LocalTypeDialog.OnTypeSelectedListener() {
                @Override
                public void onTypeSelected(PlaceBean placeBean) {
                    channelPlaceId = placeBean.getId();
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isLoading) {
                                        isLoading = true;
                                        currentPage = 1;
                                        getLiveList();
                                    }
                                }
                            }, 500);
                        }
                    });
                }
            });
        } else {
            getLocalData();
        }
    }

    @Override
    public void onBackPressed() {

        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
            return;
        }


        if (musicStatus != -1) {
            NormalAskDialog normalAskDialog = new NormalAskDialog(this);
            normalAskDialog.show();
            normalAskDialog.setData("是否继续后台播放？", "退出程序", "后台播放", true);
            normalAskDialog.setOnActionListener(new NormalAskDialog.OnActionListener() {
                @Override
                public void onLeftAction() {
                    Intent intent = new Intent(HomeActivity.this, MusicService.class);
                    stopService(intent);
                    onRelease();
                    HomeActivity.this.finish();
                }

                @Override
                public void onRightAction() {
                    HomeActivity.this.finish();
                }
            });
        } else {
            long mNowTime = System.currentTimeMillis();
            if ((mNowTime - mPressedTime) > 1500) {
                showToast("再按一次退出程序");
                mPressedTime = mNowTime;
            } else {
                HomeActivity.this.finish();
            }
        }
    }

    private void getLocalData() {
        RadioApi.getInstance().getLivePlace(MyApplication.getInstance().getToken(), new HttpSubscriber<List<PlaceBean>>(new SubscriberOnListener<List<PlaceBean>>() {
            @Override
            public void onSucceed(List<PlaceBean> data) {
                placeBeans = new ArrayList<>();
                placeBeans.addAll(data);
                MyLog.d(data);
                getLiveList();
            }

            @Override
            public void onError(int code, String msg) {
                MyLog.d(msg);
            }
        }, this));
    }

    private void getLiveList() {
        tvLoadMsg.setText("加载中");
        RadioApi.getInstance().getLiveByParam(MyApplication.getInstance().getToken(), channelPlaceId, pageSize, currentPage, new HttpSubscriber<List<LiveChannelBean>>(new SubscriberOnListener<List<LiveChannelBean>>() {
            @Override
            public void onSucceed(List<LiveChannelBean> data) {
                if (data != null) {
                    MyLog.d("get data...");
                    if (currentPage == 1) {
                        datas.clear();
                    }
                    if (data.size() == 0) {
                        tvLoadMsg.setText("没有更多了");
                    } else {
                        tvLoadMsg.setText("加载更多");
                        currentPage++;
                    }
                    if (data.size() > 0) {
                        datas.addAll(data);
                    }
                    if (datas.size() < 10) {
                        tvLoadMsg.setVisibility(View.GONE);
                    } else {
                        tvLoadMsg.setVisibility(View.VISIBLE);
                    }
                    headerAndFooterAdapter.notifyDataSetChanged();
                }
                hideDataLoad();
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int code, String msg) {
                isLoading = false;
                swipeRefreshLayout.setRefreshing(false);
                MyLog.d(msg);
                showToast(msg);
            }
        }, this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mm:
                ActivityUtil.startH5("http://sports.rbc.cn/");
                break;
//            case R.id.mm1:
//                ActivityUtil.startH5("http://www.rthk.hk/");
//                break;
            case R.id.more:
                Intent intent = new Intent(HomeActivity.this, MoreActivity.class);
                startActivity(intent);
                break;
            default:
        }
        mNavigationDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick({R.id.tv_gb, R.id.tv_news, R.id.tv_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_gb:
                 tvGb.setTextColor(getResources().getColor(R.color.colorAccent));
                tvNews.setTextColor(getResources().getColor(R.color.black));
                tv_more.setTextColor(getResources().getColor(R.color.black));
                fragmentContent.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_news:
                tvGb.setTextColor(getResources().getColor(R.color.black));
                tvNews.setTextColor(getResources().getColor(R.color.colorAccent));
                tv_more.setTextColor(getResources().getColor(R.color.black));
                fragmentContent.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_more:
                tvGb.setTextColor(getResources().getColor(R.color.black));
                tvNews.setTextColor(getResources().getColor(R.color.black));
                tv_more.setTextColor(getResources().getColor(R.color.colorAccent));
                fragmentContent.setVisibility(View.VISIBLE);
                break;
        }
    }
}
