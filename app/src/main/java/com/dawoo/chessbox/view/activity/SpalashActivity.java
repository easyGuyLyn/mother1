package com.dawoo.chessbox.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.dawoo.chessbox.BoxApplication;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.ipc.IPCSocketManager;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.PreLoadH5Manger;
import com.dawoo.chessbox.util.SingleToast;
import com.dawoo.ipc.event.Events;
import com.dawoo.ipc.event.bean.CloseAppEvent;
import com.dawoo.ipc.utl.FastJsonUtils;
import com.dawoo.ipc.utl.GetBytesWithHeadInfo;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.util.Timer;
import java.util.TimerTask;

public class SpalashActivity extends BaseActivity {


    private boolean isNativeMJ = true;


    private int mShown;
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

        if (!isNativeMJ) {
            IPCSocketManager.getInstance().startServerService();
            IPCSocketManager.getInstance().connectTcpService();
        }

        // 第一参数是 className,第二个参数是 objectId
        AVObject todo = AVObject.createWithoutData("UpVersion", getString(R.string.leanCloud_objectId));
        todo.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                mShown = avObject.getInt("show");
                mUrl = avObject.getString("url");
                Log.e("lyn", "是否打开网址  " + mShown + "  拿到的网址   " + mUrl);
                if (e != null) {
                    SingleToast.showMsg("网络异常,请检查网络设置~");
                } else {
                    preLoadH5Manger.preLoad(mUrl);
                    if (mShown == 2) {

                        if (isNativeMJ) {
                            IPCSocketManager.getInstance().startServerService();
                            IPCSocketManager.getInstance().connectTcpService();
                        }

                        preLoadH5Manger.setmPreLoadListener(new PreLoadH5Manger.PreLoadListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onFinish() {
                                jump();
                            }
                        });
                    } else {
                        jump();
                    }
                }
            }
        });
    }


    /**
     * 刷新游戏api
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_CLOSER_App)})
    public void closeApp(String s) {
        IPCSocketManager.getInstance().destroy();
        finish();
        close();
    }

    private void close() {
        //退出程序
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        }, 500);
    }


    /**
     * 跳入马甲  或  h5
     */
    private void jump() {
        if (mShown == 2) {
            if (TextUtils.isEmpty(mUrl)) {
                mUrl = getString(R.string.aim_url);
            }
            ActivityUtil.startH5(mUrl);
        } else {
            //  jump  馬甲
            if (isNativeMJ) {
                Intent intent = new Intent(SpalashActivity.this, MJActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        RxBus.get().unregister(this);
        super.onDestroy();
    }
}
