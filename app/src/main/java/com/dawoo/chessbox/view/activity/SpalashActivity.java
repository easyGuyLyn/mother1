package com.dawoo.chessbox.view.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.dawoo.chessbox.BoxApplication;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.activity.SplashActivity;
import com.dawoo.chessbox.u.ActivityUtil;
import com.dawoo.chessbox.u.PreLoadH5Manger;
import com.dawoo.coretool.util.ToastUtil;

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
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

        if (!isNativeMJ) {
//            IPCSocketManager.getInstance().startServerService();
//            IPCSocketManager.getInstance().connectTcpService();
        }

        // 第一参数是 className,第二个参数是 objectId
        AVObject todo = AVObject.createWithoutData("UpVersion", getString(R.string.leanCloud_objectId));
        todo.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {

                //   Log.e("lyn", "是否打开网址  " + mShown + "  拿到的网址   " + mUrl);
                if (e != null || avObject == null) {
                    ToastUtil.showToastShort(BoxApplication.getContext(), "网络异常,请检查网络设置~");
                } else {

                    mShown = avObject.getInt("show");
                    mUrl = avObject.getString("url");

                    if (mShown == 2) {
                        preLoadH5Manger.preLoad(mUrl);

                        if (isNativeMJ) {
//                            IPCSocketManager.getInstance().startServerService();
//                            IPCSocketManager.getInstance().connectTcpService();
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
     * 跳入马甲  或  h5
     */
    private void jump() {
        findViewById(R.id.iv_b2).setVisibility(View.VISIBLE);
        if (mShown == 2) {
            if (TextUtils.isEmpty(mUrl)) {
                mUrl = getString(R.string.aim_url);
            }
            ActivityUtil.startH5(mUrl);
            finish();
        } else {
            //  jump  馬甲
            if (isNativeMJ) {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SpalashActivity.this, SplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1500);

            }
        }
    }


}
