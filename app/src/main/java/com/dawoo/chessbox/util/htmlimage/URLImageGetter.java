package com.dawoo.chessbox.util.htmlimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;


public class URLImageGetter implements ImageGetter {
    Context c;
    TextView tv_image;
    private List<Target> targets = new ArrayList<>();

    public URLImageGetter(TextView t, Context c) {
        this.tv_image = t;
        this.c = c;
        tv_image.setTag(targets);
    }

    @Override
    public Drawable getDrawable(final String source) {
        final URLDrawable urlDrawable = new URLDrawable();

        Glide.with(c).load(source).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                urlDrawable.bitmap = resource;
                BitmapDrawable bd = (BitmapDrawable) resource;
                urlDrawable.bitmap = bd.getBitmap();
                Log.d("TT", "加载的图片，Width：" + urlDrawable.bitmap.getWidth() + "，Height：" + urlDrawable.bitmap.getHeight());
                urlDrawable.setBounds(0, 0, urlDrawable.bitmap.getWidth(), urlDrawable.bitmap.getHeight());
                tv_image.invalidate();
                tv_image.setText(tv_image.getText());//不加这句显示不出来图片，原因不详
            }
        });
        return urlDrawable;
    }


    public class URLDrawable extends BitmapDrawable {
        public Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }
}

