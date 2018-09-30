package com.dawoo.chessbox.view.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.dawoo.coretool.util.LogUtils;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.util.cache.CacheUtils;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * archar  天纵神武
 **/
public class AdvertisingPageActivity extends BaseActivity {

    public static final String PIC_AD = "pic_ad";

    @BindView(R.id.tv_timer)
    TextView mTvTimer;

    private Handler mHandler = new Handler();
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mLeftTime;
    private Bitmap bitmap;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.acitivity_advertising_page);
        RxBus.get().register(this);
        getWindow().setBackgroundDrawable(null);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    protected void initViews() {
        bitmap = null;
        if (getIntent().getStringExtra(PIC_AD) != null) {
            bitmap = CacheUtils.getInstance().getBitmap(getIntent().getStringExtra(PIC_AD));
        }
        if (bitmap != null) {
            getWindow().setBackgroundDrawable(new BitmapDrawable(bitmap));
            statTimer();
        } else {
            Intent intent = new Intent(AdvertisingPageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void initData() {
    }

    /**
     * 开始倒计时
     */
    private void statTimer() {
        cancelTimerTask();
        mLeftTime = 4;
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isDestroyed() && mTvTimer != null) {
                            mTvTimer.setVisibility(View.VISIBLE);
                            mTvTimer.setText((mLeftTime - 1) + "秒后跳过");
                            mLeftTime--;
                            if (mLeftTime == 1) {
                                cancelTimerTask();
                                Intent intent = new Intent(AdvertisingPageActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 1000, 1000);
    }


    public void cancelTimerTask() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        cancelTimerTask();
        RxBus.get().unregister(this);
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_timer)
    public void onViewClicked() {
        cancelTimerTask();
        Intent intent = new Intent(AdvertisingPageActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
