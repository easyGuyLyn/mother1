package com.dawoo.chessbox.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class DeviceUtils {
    private static final boolean isFullScreen = false; //是否28
    public SreenOrientationListener mSreenOrientationListener = null;

    public static final int BELOW_CHANGED = 1;
    public static final int ABOVE_CHANGED = 2;
    public static final int LEFT_CHANGED = 3;
    public static final int RIGHT_CHANGED = 4;

    public View lefttoolbar;
    public View righttoolbar;
    public Activity activity;

    public DeviceUtils(Activity activity, View lefttoolbar, View righttoolbar) {
        this.activity = activity;
        this.lefttoolbar = lefttoolbar;
        this.righttoolbar = righttoolbar;
    }

    public void Device() {
        if (isFullScreen) {
            if (Build.VERSION.SDK_INT >= 28) {
                lefttoolbar.setVisibility(View.GONE);
                righttoolbar.setVisibility(View.GONE);
                setLiuHai();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                DeviceAdapter.setwidth(lefttoolbar, activity);
                DeviceAdapter.setwidth(righttoolbar, activity);
                Drvice();
            }
        }
    }

    @RequiresApi(api = 28)
    private void setLiuHai() {
        Toast.makeText(activity, "进来了", Toast.LENGTH_LONG).show();
        //设置页面全屏显示
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

        lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;

        //设置页面延伸到刘海区显示
        activity.getWindow().setAttributes(lp);

//        //设置全屏
//        //去掉标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
//
//        getWindow().setAttributes(lp);
    }


    public void Drvice() {
        if (DeviceAdapter.hasNotchScreen(activity)) {
            mSreenOrientationListener = new SreenOrientationListener(activity, new OnChanged() {
                @SuppressLint("WrongConstant")
                @Override
                public void onChanged(int direction) {
                    switch (direction) {
                        case BELOW_CHANGED:
                            if (lefttoolbar.getVisibility() == View.VISIBLE) {
                                lefttoolbar.setVisibility(View.GONE);
                                righttoolbar.setVisibility(View.VISIBLE);
                            }
                            break;

                        case ABOVE_CHANGED:
                            if (righttoolbar.getVisibility() == View.VISIBLE) {
                                lefttoolbar.setVisibility(View.VISIBLE);
                                righttoolbar.setVisibility(View.GONE);
                            }
                            break;

                        case LEFT_CHANGED:
                            if (lefttoolbar.getVisibility() == View.VISIBLE) {
                                lefttoolbar.setVisibility(View.GONE);
                                righttoolbar.setVisibility(View.VISIBLE);
                            }
                            break;

                        case RIGHT_CHANGED:
                            if (righttoolbar.getVisibility() == View.VISIBLE) {
                                lefttoolbar.setVisibility(View.VISIBLE);
                                righttoolbar.setVisibility(View.GONE);
                            }
                            break;

                    }
                }
            });
        } else {
            lefttoolbar.setVisibility(View.GONE);
            righttoolbar.setVisibility(View.GONE);
        }
    }

    public interface OnChanged {
        void onChanged(int direction);
    }

    /**
     * 屏幕旋转
     */
    class SreenOrientationListener extends OrientationEventListener {
        OnChanged onChanged;

        public SreenOrientationListener(Context context, OnChanged onChanged) {
            super(context);
            this.onChanged = onChanged;
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                //judgeRotate();
                return; // 手机平放时，检测不到有效的角度
            }
            // 只检测是否有四个角度的改变 Below
            if (orientation > 340 || orientation < 20) {
                // 0度：手机默认竖屏状态（home键在正下方）
                orientation = 0;
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                Log.i("orientation", "orientation" + orientation);
                onChanged.onChanged(BELOW_CHANGED);
            } else if (orientation > 70 && orientation < 110) {
                // 90度：手机顺时针旋转90度横屏（home建在左侧）Left
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                Log.i("orientation", "orientation" + orientation);
                onChanged.onChanged(LEFT_CHANGED);
            } else if (orientation > 160 && orientation < 200) {
                // 手机顺时针旋转180度竖屏（home键在上方）  Above
                orientation = 180;
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Log.i("orientation", "orientation" + orientation);
                onChanged.onChanged(ABOVE_CHANGED);
            } else if (orientation > 250 && orientation < 290) {
                // 手机顺时针旋转270度横屏，（home键在右侧）Right
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Log.i("orientation", "orientation" + orientation);
                onChanged.onChanged(RIGHT_CHANGED);
            }
        }
    }

    public void onResume() {
        if (mSreenOrientationListener != null) {
            mSreenOrientationListener.enable();
        }
    }

    public void onPause() {
        if (mSreenOrientationListener != null) {
            mSreenOrientationListener.disable();
        }
    }

    public void judgeRotate() {
        if (righttoolbar.getVisibility() == View.VISIBLE && righttoolbar.getVisibility() == View.VISIBLE) {
            return;
        }
        if (righttoolbar.getVisibility() == View.VISIBLE) {
            lefttoolbar.setVisibility(View.VISIBLE);
            righttoolbar.setVisibility(View.GONE);
        } else if (lefttoolbar.getVisibility() == View.VISIBLE) {
            righttoolbar.setVisibility(View.VISIBLE);
            lefttoolbar.setVisibility(View.GONE);
        }
    }

}
