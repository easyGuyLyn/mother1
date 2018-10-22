package com.dawoo.chessbox.view.view;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by kwf on 2016/3/23 0023.
 */
public class CustomDrawerLayout extends DrawerLayout {
    private SlideMode mSlideMode = SlideMode.HOR;
    ;

    public CustomDrawerLayout(Context context) {
        this(context, null);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final ViewConfiguration configuration = ViewConfiguration
                .get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            final float x = event.getX();
            final float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(mLastMotionX - event.getX()) > 10) {
                        mSlideMode = SlideMode.HOR;
                    } else if (Math.abs(mLastMotionY - event.getY()) > 40) {
                        mSlideMode = SlideMode.VER;
                    }
                    if (mSlideMode==SlideMode.VER&&isDrawerOpen(GravityCompat.END)) {
                        return true;
                    }
                    break;
                default:

                    break;
            }
            return super.onInterceptTouchEvent(event);
        } catch (IllegalArgumentException ex) {
        }
        mSlideMode = SlideMode.HOR;
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }

    private enum SlideMode {
        VER,
        HOR
    }
}