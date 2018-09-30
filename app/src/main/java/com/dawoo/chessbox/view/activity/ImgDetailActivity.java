package com.dawoo.chessbox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.RequestOptions;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.net.GlideApp;
import com.dawoo.chessbox.view.view.PinchImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 优惠活动　图片点击详情
 */
public class ImgDetailActivity extends BaseActivity {

    public static final String IMG_URL = "IMG_URL";
    @BindView(R.id.img_detail_gesture)
    PinchImageView imgDetailGesture;
    @BindView(R.id.image_scale_rll)
    RelativeLayout imageScaleRll;
    private String url;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.acticity_image_detail);
    }

    @Override
    protected void initViews() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();
        url = intent.getStringExtra(IMG_URL);
        if (!TextUtils.isEmpty(url)) {
            RequestOptions options = new RequestOptions().placeholder(R.mipmap.nodata_bg);
            GlideApp.with(mContext)
                    .load(url)
                    .apply(options)
                    .into(imgDetailGesture);
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
