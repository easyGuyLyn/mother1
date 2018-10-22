package com.dawoo.chessbox.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jeff on 2016/5/26.
 */
public class BitmapUtils {
    protected static float sPixelScale = 0.0f;
    public static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/gbchess/Camera/");

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dp) {
        if (sPixelScale == 0.0f) {
            sPixelScale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dp * sPixelScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float px) {
        if (sPixelScale == 0.0f) {
            sPixelScale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (px / sPixelScale + 0.5f);
    }

    /**
     * Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * Bitmap to Drawable
     *
     * @param bitmap
     * @param mcontext
     * @return
     */
    public static Drawable bitmapToDrawble(Bitmap bitmap, Context mcontext) {
        Drawable drawable = new BitmapDrawable(mcontext.getResources(), bitmap);
        return drawable;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static String bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static File saveBitmap(Bitmap bm, String filePath) {
        Log.e("fh", "保存图片");
        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i("fh", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    public static float getScreenWidth(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics outMetrics = new DisplayMetrics();

        wm.getDefaultDisplay().getMetrics(outMetrics);

        int mScreenWidth = outMetrics.widthPixels;//屏幕的宽度

        return mScreenWidth;
    }

    //110,50
    public static Bitmap getRectColors(String[] colors, int width, int height, int borderColor,
                                       int type) {
        if (colors == null || colors.length <= 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint();
        p.setAntiAlias(true);
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            String color = colors[i];
            if (color.length() == 4) {
                if (color.lastIndexOf("f") >= 0) {
                    colors[i] = "#ffffff";
                }
                if (color.lastIndexOf("0") >= 0) {
                    colors[i] = "#000000";
                }
            }
        }
        if (TextUtils.isEmpty(colors[0])) {
            return null;
        }
        p.setColor(Color.parseColor(colors[0]));// 设置红色
        canvas.drawRect(0, 0, width, height, p);
        if (colors.length != 1) {
            p.setColor(Color.parseColor(colors[1]));// 设置灰色
            if (type == 1) {
                canvas.drawRect(width / 2, 0, width, height, p);
            } else if (type == 2) {
                canvas.drawRect(0, height / 2, width, height, p);
            }

        }
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(2);
        p.setColor(borderColor);
        canvas.drawRect(0, 0, width, height, p);
        canvas.save();
        canvas.restore();
        return bitmap;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Bitmap screenShot(Activity activity, int viewId) {

        View tempView = activity.findViewById(viewId);
        //        View tempView = button;
        tempView.setDrawingCacheEnabled(true);

        Bitmap bitmap = tempView.getDrawingCache();

        return bitmap;

    }


    /**
     * 保存
     */
    public static void saveBitmap(Bitmap qrBitmap, Activity activity) {
        // setLoadingIndicator(true);
        PHOTO_DIR.mkdirs();// 创建照片的存储目录
        File file = createFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera"), "Qrcode", ".png");
        File file1 = BitmapUtils.saveBitmap(qrBitmap, file.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file1);
        intent.setData(uri);
        activity.sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
        // CommonUtils.showToast(mContext, "二维码保存成功");
        SingleToast.showMsg("二维码保存成功");
        //  setLoadingIndicator(false);
    }

    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    public static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory())
            folder.mkdirs();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

}
