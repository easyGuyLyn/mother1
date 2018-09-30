package com.dawoo.chessbox.view.feagment_game;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.QRCodeBean;
import com.dawoo.chessbox.mvp.presenter.MainActivityPresenter;
import com.dawoo.chessbox.mvp.view.IQRcodeView;
import com.dawoo.chessbox.net.GlideApp;
import com.dawoo.chessbox.util.BitmapUtils;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.SingleToast;
import com.dawoo.pushsdk.util.CommonUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 分享dialog
 */
public class ShareDialogFragment extends BaseDialogFragment implements IQRcodeView {


    @BindView(R.id.img_close)
    ImageView imgClose;
    @BindView(R.id.qrcode)
    ImageView mQrcode;
    @BindView(R.id.sava)
    Button sava;

    private MainActivityPresenter mMainActivityPresenter;
    private Bitmap bm;
    private Animation mAnimation;

    @Override
    protected int getViewId() {
        return R.layout.qt_share_dialogfragment;
    }

    @Override
    protected void initViews(View view) {
        intScreenHProportion = 0.8;
        intScreenWProportion = 0.51;
    }

    @Override
    protected void initData() {
        mMainActivityPresenter = new MainActivityPresenter(getActivity(), this);
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.qt_btn_single_narrow);
        mMainActivityPresenter.getShareQRCode();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAnimation != null) {
            mAnimation.cancel();
            mAnimation = null;
        }
    }

    @OnClick({R.id.img_close, R.id.sava})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.sava:
                if (mAnimation != null) {
                    sava.startAnimation(mAnimation);
                } else {
                    mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.qt_btn_single_narrow);
                    sava.startAnimation(mAnimation);
                }
                sava();
                break;
        }

    }

    @Override
    public void onShareQRCodeResult(Object o) {
        QRCodeBean qrCodeBean = (QRCodeBean) o;
        String path = NetUtil.handleUrl(qrCodeBean.qrCodeUrl);
        //String path = NetUtil.handleUrl("/fserver/files/gb/18/carousel/10011/1531903475635.png");
        // RequestOptions options = new RequestOptions().placeholder(R.mipmap.banner1);
        GlideApp.with(getActivity()).load(path).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (resource != null) {
                    bm = BitmapUtils.drawableToBitmap(resource);
                    // mQrcode.setImageBitmap(bm);
                }
                return false;
            }
        }).into(mQrcode);
    }


    @Override
    public void onDestroy() {
        if (mMainActivityPresenter != null) {
            mMainActivityPresenter.onDestory();
        }
        super.onDestroy();
    }

    public void sava() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int hasAuth = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (hasAuth != PackageManager.PERMISSION_GRANTED) {
                // 提交请求权限
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                downBitmap();
            }
        } else {
            downBitmap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                downBitmap();
            } else {
                // Permission Denied
                SingleToast.showMsg("您没有授权存储权限，请在设置中打开授权");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void downBitmap() {
        if (bm == null) {
            SingleToast.showMsg("保存二维码失败");
            return;
        }
        BitmapUtils.saveBitmap(bm, getActivity());
    }

}
