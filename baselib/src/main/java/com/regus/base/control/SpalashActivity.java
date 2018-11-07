package com.regus.base.control;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.regus.base.ConstantValue;
import com.regus.base.HostManager;
import com.regus.base.R;
import com.regus.base.util.LogUtils;
import com.regus.base.util.PreLoadH5Manger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpalashActivity extends BaseActivity {

    public static final String TAG = "Line  ";



    private int mShown = 1; //是否展示目标H5  默认关闭
    private String mUrl; //目标h5的链接
    private String mPostDetailUrl = "http://154.48.238.35:8085/AppShellService.svc/GetAppInfo";//请求链接详情
    private PreLoadH5Manger preLoadH5Manger = new PreLoadH5Manger();  //目标h5的预加载器  为了更流畅


    @Override
    protected void createLayoutView() {
        setContentView(R.layout.acitivity_spash_scene);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {
       getData();

//        // 第一参数是 className,第二个参数是 objectId
//        AVObject todo = AVObject.createWithoutData("UpVersion", HostManager.getInstance().getAppId());
//        todo.fetchInBackground(new GetCallback<AVObject>() {
//            @Override
//            public void done(AVObject avObject, AVException e) {
//
//
//                if (e != null || avObject == null) {
//                    SingleToast.showMsg("网络异常,请检查网络设置~");
//                } else {
//                    mShown = avObject.getInt("show");
//                    mUrl = avObject.getString("url");
//                   LogUtils.e(TAG, "是否打开网址  " + mShown + "  拿到的网址   " + mUrl);
//
//                    if (mShown == 2) {
//                        preLoadH5Manger.preLoad(mUrl);
//                        preLoadH5Manger.setmPreLoadListener(new PreLoadH5Manger.PreLoadListener() {
//                            @Override
//                            public void onStart() {
//                                LogUtils.e(TAG, "目标h5 预加载开始");
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                LogUtils.e(TAG, "目标h5 预加载完毕");
//                                jump();
//                            }
//                        });
//                    } else {
//                        jump();
//                    }
//                }
//            }
//        });
    }


    private void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (TextUtils.isEmpty(mUrl)) {
                        mUrl = HostManager.getInstance().getAim_url();
                    }
                    String urlDate = "aid="+HostManager.getInstance().getAppId()+"&sid="+HostManager.getInstance().getmSid();
                    URL url = new URL(mPostDetailUrl+"?"+urlDate);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setReadTimeout(5000);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    int code = urlConnection.getResponseCode();
                    Log.d("jumpuUrl",urlConnection.getResponseCode()+"...");
                    if (code == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;
                        StringBuffer buffer = new StringBuffer();
                        while ((line = bufferedReader.readLine()) != null) {
                            buffer.append(line);
                        }
                        String jsonStr = buffer.toString();
                        Log.e("regus", jsonStr + "");

                        //处理
                        try {
                            JSONObject responseJson = new JSONObject(jsonStr.replace("\\", ""));
                            if (responseJson.has("Status") && responseJson.has("Data")) {
                                if (responseJson.getBoolean("Status")) {

                                    JSONObject dataJsonObject = new JSONObject(responseJson.getString("Data"));

                                    //ture webview
                                    if (dataJsonObject.getBoolean("IsEnable")) {
                                        //startWebview(dataJsonObject.getString("Url"));
                                        Log.d("jumpuUrl",dataJsonObject.getString("Url")+"请求成功！");
                                        jump(dataJsonObject.getBoolean("IsEnable"),dataJsonObject.getString("Url"));
                                    } else {
                                        jump(true,mUrl);
                                    }

                                } else {
                                    jump(true,mUrl);
                                }
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            jump(true,mUrl);
                        }
                    } else {
                        jump(true,mUrl);
                    }
                } catch (Exception e) {
                    jump(true,mUrl);
                }

            }
        }).start();
    }



    /**
     * 跳入马甲  或  h5
     */
    private void jump(boolean IsEnable,String url) {
        if (IsEnable){
            Intent intent = new Intent(mContext, MJWebViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ConstantValue.WEBVIEW_URL, url);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }else {
            if (HostManager.getInstance().isNativeMJ()) {
                LogUtils.e(TAG, " 开始跳原生MJ   反射加载");
                try {
                    Class aimClass = Class.forName("com.regus.main.ui.MainActivity");
                    Intent intent = new Intent(SpalashActivity.this, aimClass);
                    startActivity(intent);
                    finish();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
        }



        //mShown =2;


//        if (mShown == 2) {
//            if (TextUtils.isEmpty(mUrl)) {
//                mUrl = HostManager.getInstance().getAim_url();
//            }
//
//            Intent intent = new Intent(mContext, MJWebViewActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString(ConstantValue.WEBVIEW_URL, mUrl);
//            intent.putExtras(bundle);
//            startActivity(intent);
//            finish();
//        } else {
//
//            if (HostManager.getInstance().isNativeMJ()) {
//                LogUtils.e(TAG, " 开始跳原生MJ   反射加载");
//                try {
//                    Class aimClass = Class.forName("com.regus.main.ui.MainActivity");
//                    Intent intent = new Intent(SpalashActivity.this, aimClass);
//                    startActivity(intent);
//                    finish();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                LogUtils.e(TAG, " 开始跳H5 MJ");
//
//                Intent intent = new Intent(mContext, MJWebViewActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(ConstantValue.WEBVIEW_URL, HostManager.getInstance().getH5MJURl());
//                bundle.putBoolean(MJWebViewActivity.IS_H5_MJ, true);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                finish();

                //H5 马甲  希望也预加载
//                preLoadH5Manger.preLoad("http://154.48.238.35:8081/#/");
//                preLoadH5Manger.setmPreLoadListener(new PreLoadH5Manger.PreLoadListener() {
//                    @Override
//                    public void onStart() {
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        Intent intent = new Intent(mContext, MJWebViewActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString(ConstantValue.WEBVIEW_URL, "http://154.48.238.35:8081/#/");
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                        finish();
//                    }
//                });

//            }
        }
    }


}
