package com.dawoo.chessbox.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dawoo.chessbox.BoxApplication;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.MaintenanceBean;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.SingleToast;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.SPTool;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.google.gson.Gson;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.dawoo.chessbox.ConstantValue.MAINTAIN_URL;
import static com.dawoo.chessbox.util.NetUtil.getOkHttpClientBuilderForHttps;

public class MaintenanceActivity extends BaseActivity {
    public static final String maintenance_code = "error_code";

    @BindView(R.id.content_tv)
    TextView mContentTv;
    @BindView(R.id.left_btn)
    FrameLayout mLeftBtn;
    @BindView(R.id.im_maintenance)
    ImageView mImMaintenance;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.cs_btn)
    LinearLayout mCsBtn;

    private int mCode;//错误类型

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_maintenance);
        RxBus.get().register(this);
    }

    @Override
    protected void initViews() {
        mCode = getIntent().getIntExtra(maintenance_code, 0);
        initDefaultUI();
    }

    private void initDefaultUI() {
        if (mCode == 605) {
            mCsBtn.setVisibility(View.GONE);
            mTvTitle.setText("您所在的地区禁止访问");
            Glide.with(this).load(R.mipmap.ic_ip_refuse).into(mImMaintenance);
            mContentTv.setText("由于您的所在地不在我们的服务允许范围内，我们暂时无法为您服务。");
        } else if (mCode == 607) {
            mCsBtn.setVisibility(View.VISIBLE);
            mTvTitle.setText("网站维护中, 暂停访问");
            Glide.with(this).load(R.mipmap.maintenance_img).into(mImMaintenance);
            mContentTv.setText("抱歉！ 本系统程序升级，将暂停访问，请敬请期待。如果有什么疑问，请联系我们的客服。");
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


    @Override
    protected void initData() {
        if (mCode == 607) {
            checkStatus();
        }
    }


    @OnClick(R.id.cs_btn)
    public void onViewClicked() {
        String url = (String) SPTool.get(BoxApplication.getContext(), ConstantValue.KEY_CUSTOMER_SERVICE, "");
        boolean isInlay = (Boolean) SPTool.get(BoxApplication.getContext(), ConstantValue.KEY_CUSTOMER_SERVICE_ISINLAY, false);
        if (TextUtils.isEmpty(url)) {
            SingleToast.showMsg("暂无客服地址");
            return;
        }
        if (isInlay) {
            ActivityUtil.startWebView(url, "", ConstantValue.WEBVIEW_TYPE_THIRD_ORDINARY, 1);
        } else {
            //去浏览器
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            mContext.startActivity(intent);
        }
    }

    /**
     * 动态挂维护
     */
    public void checkStatus() {
        String url = DataCenter.getInstance().getIp() + MAINTAIN_URL;
        OkHttpClient.Builder builder = getOkHttpClientBuilderForHttps();

        Request request = new Request.Builder().url(url)
                .headers(Headers.of(NetUtil.setHeaders()))
                .get()
                .build();

        Call call = builder.build().newCall(request);
        try {
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 失败
                        }
                    });
                    LogUtils.e("login Error ==> " + e.getLocalizedMessage());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int code = response.code();
                    if (607 == code) {
                        LogUtils.e("login Error ==> " + response.message());

                    }
                    final String jsonData = response.body().string();
                    Log.e("登录中返回报文：", jsonData);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doResponseData(jsonData);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void doResponseData(String jsonData) {
        if (TextUtils.isEmpty(jsonData)) {
            return;
        }

        String[] totalData = jsonData.split("#@#");
        if (totalData.length > 1) {
            String part1 = totalData[0];
            String part2 = totalData[1];
            Gson gson = new Gson();
            MaintenanceBean maintenanceBean = gson.fromJson(part1, MaintenanceBean.class);
            maintenanceBean.setContentText(part2);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mContentTv.setText(Html.fromHtml(part2, Html.FROM_HTML_MODE_COMPACT));
            } else {
                mContentTv.setText(Html.fromHtml(part2));
            }

            SPTool.remove(this, ConstantValue.KEY_CUSTOMER_SERVICE);
            SPTool.put(this, ConstantValue.KEY_CUSTOMER_SERVICE, maintenanceBean.getMobileCustomerServiceUrl());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.left_btn)
    public void onViewClicked1() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mCode == 605) {
            ActivityStackManager.getInstance().finishAllActivity();
        } else if (mCode == 607) {
            ActivityStackManager.getInstance().finishAllActivity();
        }
    }
}
