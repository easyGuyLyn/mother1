package com.dawoo.ipc.control;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.dawoo.ipc.ConstantValue;
import com.dawoo.ipc.HostManager;
import com.dawoo.ipc.R;
import com.dawoo.ipc.utl.PreLoadH5Manger;

import static com.dawoo.ipc.ConstantValue.WEBVIEW_TYPE_THIRD_ORDINARY;

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


        // 第一参数是 className,第二个参数是 objectId
        AVObject todo = AVObject.createWithoutData("UpVersion", HostManager.getInstance().getLeanCloud_objectId());
        todo.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {

                if (e != null || avObject == null) {
                    Toast.makeText(HostManager.getInstance().getContext(), "网络异常,请检查网络设置~", Toast.LENGTH_SHORT);
                } else {
                    mShown = avObject.getInt("show");
                    mUrl = avObject.getString("url");

                    Log.e("lyn", "是否打开网址  " + mShown + "  拿到的网址   " + mUrl);

                    if (mShown == 2) {
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
        if (mShown == 2) {
            if (TextUtils.isEmpty(mUrl)) {
                mUrl = HostManager.getInstance().getAim_url();
            }

            Intent intent = new Intent(mContext, IpcWebViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ConstantValue.WEBVIEW_URL, mUrl);
            bundle.putString(ConstantValue.WEBVIEW_TYPE, WEBVIEW_TYPE_THIRD_ORDINARY);
            bundle.putInt(IpcWebViewActivity.SCREEN_ORITATION, 3);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            finish();
        } else {
            //  jump  馬甲
            if (isNativeMJ) {
//                Intent intent = new Intent(SpalashActivity.this, MJActivity.class);
//                startActivity(intent);
//                finish();
            }
        }
    }


}
