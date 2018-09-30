package com.dawoo.chessbox.util.htmlimage;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import com.dawoo.chessbox.view.activity.ImgDetailActivity;
import com.dawoo.chessbox.view.view.PinchImageView;
import com.dawoo.coretool.util.system.SystemUtil;

import org.xml.sax.XMLReader;

import java.util.Locale;

public class URLTagHandler implements TagHandler {

    private Context mContext;


    public URLTagHandler(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        // 处理标签<img>
        if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
            // 获取长度
            int len = output.length();
            // 获取图片地址
            ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
            String imgURL = images[0].getSource();
            Log.d("URLTagHandler","img地址"+imgURL);
            // 使图片可点击并监听点击事件
            output.setSpan(new ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class ClickableImage extends ClickableSpan {
        private String url;
        private Context context;

        public ClickableImage(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            Log.d("URLTagHandler","进来了");
            // 进行图片点击之后的处理
            if (TextUtils.isEmpty(url)) return;
            if (SystemUtil.isFastClick()) return;
            context.startActivity(new Intent(context,ImgDetailActivity.class).putExtra(ImgDetailActivity.IMG_URL,url));
        }
    }

}
