package com.dawoo.chessbox.view.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.ipc.IPCSocketManager;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.PreLoadH5Manger;
import com.dawoo.chessbox.util.SingleToast;
import com.hwangjr.rxbus.RxBus;

import java.util.Timer;
import java.util.TimerTask;

public class SpalashActivity extends BaseActivity {

    private Handler mHandler = new Handler();
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mLeftTime;

    private boolean mShown;
    private String mUrl;
    private PreLoadH5Manger preLoadH5Manger = new PreLoadH5Manger();


    @Override
    protected void createLayoutView() {
        setContentView(R.layout.acitivity_spash_scene);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RxBus.get().register(this);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {
        IPCSocketManager.getInstance().startServerService();
        IPCSocketManager.getInstance().connectTcpService();

        // 第一参数是 className,第二个参数是 objectId
        AVObject todo = AVObject.createWithoutData("Line", "5bc846109f545400709268aa");
        todo.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                mShown = avObject.getBoolean("show");// 读取 title
                mUrl = avObject.getString("url");// 读取 content
                Log.e("lyn", "是否打开网址  " + mShown + "  拿到的网址   " + mUrl);
                if (e != null) {
                    SingleToast.showMsg("网络异常,请检查网络设置~");
                } else {
                    preLoadH5Manger.preLoad(mUrl);
                    preLoadH5Manger.setmPreLoadListener(new PreLoadH5Manger.PreLoadListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFinish() {
                          jump();
                        }
                    });

                }
            }
        });
    }

    /**
     * 开始倒计时
     */
    private void statTimer() {
        cancelTimerTask();
        mLeftTime = 3;
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!isDestroyed()) {
                            mLeftTime--;
                            if (mLeftTime == 1) {
                                cancelTimerTask();
                                jump();
                            }
                        }
                    }
                });
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 1000, 1000);
    }


    /**
     * 跳入马甲  或  h5
     */
    private void jump() {
        if (mShown) {
            if (TextUtils.isEmpty(mUrl)) {
                mUrl = "https://m.ttc178.com";
            }
            ActivityUtil.startH5(mUrl);
            finish();
        } else {


        }
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


    @Override
    protected void onDestroy() {
        cancelTimerTask();
        RxBus.get().unregister(this);
        super.onDestroy();
    }
}
