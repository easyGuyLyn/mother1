package com.dawoo.ipc.control;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * activity的基本类
 * Created by benson on 17-12-19.
 */

public abstract class BaseActivity extends AppCompatActivity {
    // private ImmersionBar mImmersionBar;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityStackManager.getInstance().addActivity(this);
        createLayoutView();
        mContext = this;
        PushAgent.getInstance(mContext).onAppStart();
        initViews();
        initData();
    }

    protected abstract void createLayoutView();


    protected abstract void initViews();

    protected abstract void initData();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.getInstance().removeActivity(this);
    }

}
