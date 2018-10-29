package com.regus.ipc.control;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.regus.ipc.ConstantValue;
import com.regus.ipc.HostManager;
import com.regus.ipc.R;

import static com.regus.ipc.ConstantValue.WEBVIEW_TYPE_THIRD_ORDINARY;

public class SpalashActivity extends BaseActivity {


    private boolean isNativeMJ = false;


    private int mShown;
    private String mUrl;

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
                    jump();
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
            mContext.startActivity(intent);
            finish();
        } else {
            //  jump  馬甲
            if (isNativeMJ) {
//                Intent intent = new Intent(SpalashActivity.this, MJActivity.class);
//                startActivity(intent);
//                finish();
            } else {
                Intent intent = new Intent(mContext, IpcWebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ConstantValue.WEBVIEW_URL, "http://154.48.238.35:8081/#/");
                bundle.putString(ConstantValue.WEBVIEW_TYPE, WEBVIEW_TYPE_THIRD_ORDINARY);
                bundle.putInt(IpcWebViewActivity.SCREEN_ORITATION, 3);
                bundle.putBoolean(IpcWebViewActivity.IS_H5_MJ, true);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                finish();
            }
        }
    }


}
