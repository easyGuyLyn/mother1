package com.dawoo.chessbox.view.activity;

import android.content.Intent;
import android.os.CountDownTimer;

import com.dawoo.chessbox.R;

public class PreventAddictionActivity extends BaseActivity {

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_preventaddiction);
    }

    @Override
    protected void initViews() {
        mCountDownTimer.cancel();
        mCountDownTimer.start();
    }

    @Override
    protected void initData() {

    }


    private CountDownTimer mCountDownTimer = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            Intent intent = new Intent(PreventAddictionActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
    };

}
