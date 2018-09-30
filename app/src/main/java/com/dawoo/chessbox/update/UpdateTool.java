package com.dawoo.chessbox.update;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.dawoo.chessbox.BuildConfig;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.UpdateBean;
import com.dawoo.chessbox.util.FastJsonUtils;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.SingleToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 应用更新工具
 * Created by fei on 16-11-21.
 */
public class UpdateTool {
    @SuppressWarnings("unused")
    private static final String TAG = "AppUpdate";

    /**
     * 检查更新（新）
     */
    @SuppressWarnings("unused")
    public static void checkUpdate(Activity activity, String appType, String siteId, String keyCode, UpdateCallback updateCallback) {
        OkHttpClient.Builder okHttpClientBuilder = NetUtil.getOkHttpClientBuilderForHttps();

        RequestBody body = new FormBody.Builder()
                .add("type", appType)
                .add("siteId", siteId)
                .add("code", keyCode)
                .build();
        String url;
        url = DataCenter.getInstance().getIp() + ConstantValue.UPDATE_URL;
        Request request = new Request.Builder().url(url)
                .headers(Headers.of(NetUtil.setHeaders()))
                .post(body).build();
        okHttpClientBuilder.build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "update error ==> " + e.getMessage());
                UpdateTool.onError(e, updateCallback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.e(TAG, "update info ==> " + response);
                try {
                    String data = response.body().string();
                    Log.e(TAG, data);

                    if (TextUtils.isEmpty(data) || data.length() < 8) {
                        updateCallback.onError();
                        return;
                    }
                    UpdateBean updateInfo = FastJsonUtils.toBean(data, UpdateBean.class);
                    onNext(updateInfo, updateCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                    UpdateTool.onError(e, updateCallback);
                }
            }
        });


    }

    // 显示信息
    private static void onNext(UpdateBean updateInfo, UpdateCallback updateCallback) {
        if (updateInfo != null) {
            updateCallback.onSuccess(updateInfo);
        } else {
            updateCallback.onError(); // 失败
        }
    }

    // 错误信息
    private static void onError(Throwable throwable, UpdateCallback updateCallback) {
        updateCallback.onError();
        Log.e(TAG, "onError" + throwable.getMessage());
    }


    public static void downloadApk(String apkUrl, String fileName, DownLoadCallBack downLoadCallBack, Activity activity) {
        Log.e(TAG, apkUrl + "");

        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

        //http://35.229.226.165:8787/android/4.0.1/app_57h0_4.0.1.apk
        OkHttpUtils.post()
                .url(apkUrl)
                .build()
                .execute(new FileCallBack(filePath, fileName) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        SingleToast.showMsg("资源准备中...");
                        downLoadCallBack.onBefore();
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        Log.e(TAG, "onAfter ");
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        downLoadCallBack.inProgress(progress);
                        Log.e("progress", progress + "");
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        SingleToast.showMsg("下载apk失败,错误代码： " + e.getMessage());
                        Log.e(TAG, "onError " + e.getMessage());
                        downLoadCallBack.onError();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        if (response == null) {
                            SingleToast.showMsg("下载的apk为空");
                            downLoadCallBack.onError();
                            return;
                        }
                        downLoadCallBack.onSuccess(response);
                    }
                });
    }


    /**
     * 安装 apk 文件
     *
     * @param apkFile
     */
    public static void installApk(File apkFile, Activity activity) {
        if (apkFile == null) return;
        Log.e(TAG, " 新apk 路径: " + apkFile.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri =
                    FileProvider.getUriForFile(activity, getPid() + ".fileprovider", apkFile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile),
                    "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
    }

    public static String getPid() {
        String pid = BuildConfig.APPLICATION_ID;
        if (BuildConfig.APPLICATION_ID.contains(".debug")) {
            pid = BuildConfig.APPLICATION_ID.replace(".debug", "");
        }
        return pid;
    }


    // 错误回调
    public interface UpdateCallback {
        void onSuccess(UpdateBean updateInfo);

        void onError();
    }

    //下载回调
    public interface DownLoadCallBack {
        void onBefore();

        void inProgress(float progress);

        void onSuccess(File response);

        void onError();
    }


}