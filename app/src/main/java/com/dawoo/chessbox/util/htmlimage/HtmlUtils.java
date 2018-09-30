package com.dawoo.chessbox.util.htmlimage;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * textview 加载图片
 */

public class HtmlUtils {

    public static Spanned getHtml(Context context, TextView textView, String string) {
//        textView.setMovementMethod(ScrollingMovementMethod.getInstance());// 滚动
        //            tvActivityDetail.loadData(String.valueOf(activiyuDetail), "text/html", "uft-8");
        textView.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页//click must
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY, new URLImageGetter(textView, context), new URLTagHandler(context));
        else
            return Html.fromHtml(string, new URLImageGetter(textView, context), new URLTagHandler(context));
    }

}
