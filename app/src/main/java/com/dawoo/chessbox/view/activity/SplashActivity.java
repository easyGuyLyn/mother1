package com.dawoo.chessbox.view.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.line.CommonIp;
import com.dawoo.chessbox.bean.line.LineErrorDialogBean;
import com.dawoo.chessbox.ipc.IPCSocketManager;
import com.dawoo.chessbox.net.GlideApp;
import com.dawoo.chessbox.update.AppUpdate;
import com.dawoo.chessbox.util.AutoLogin;
import com.dawoo.chessbox.util.FastJsonUtils;
import com.dawoo.chessbox.util.SharePreferenceUtil;
import com.dawoo.chessbox.util.SingleToast;
import com.dawoo.chessbox.util.line.LineHelperUtil;
import com.dawoo.chessbox.util.line.LineProgressString;
import com.dawoo.chessbox.view.view.CustomDialog;
import com.dawoo.chessbox.view.view.numberprogressbar.LineTaskProgressListener;
import com.dawoo.chessbox.view.view.numberprogressbar.NumberProgressBar;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.packageref.PackageInfoUtil;
import com.dawoo.pushsdk.util.GsonUtil;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.dawoo.chessbox.util.line.LineHelperUtil.progress_finish_CheckDomain;
import static com.dawoo.chessbox.util.line.LineHelperUtil.progress_finish_CheckLine;
import static com.dawoo.chessbox.util.line.LineHelperUtil.progress_finish_GetBaseLine;
import static com.dawoo.chessbox.util.line.LineHelperUtil.progress_start_CheckDomain;


/**
 * 过渡页
 * 检查线路
 * Created by benson on 18-04-08.
 */

public class SplashActivity extends BaseActivity {
    public static final String TAG = "SplashActivity";
    private static float progressWidth;
    private AVLoadingIndicatorView mAvi;
    private TextView mTvLoading;
    private ImageView mIvLogo;
    private ImageView imageView;
    private TextView mTvCopyright;
    private NumberProgressBar mNumberProgressBar;
    private Button mReGet;
    private Button mTv_error_detail;
    private SplashActivity mContext;
    private LineHelperUtil mHelperUtil;
    private CustomDialog mCustomDialog;
    private LineErrorDialogBean mLineErrorDialogBean;
    private String mOutIp;
    private ImageView imgProgress;
    private RelativeLayout flProgress;
    private ImageView mRotatingImge;
    private AnimationDrawable animationDrawable;
    private AnimationDrawable mRotatingImgeDrawable;
    private AppUpdate mAppUpdate;

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.acitivity_splash);
        RxBus.get().register(this);
        mAvi = findViewById(R.id.avi);
        mTvLoading = findViewById(R.id.tvLoading);
        mIvLogo = findViewById(R.id.ivLogo);
        mTvCopyright = findViewById(R.id.tvCopyright);
        mNumberProgressBar = findViewById(R.id.numberBar);
        mReGet = findViewById(R.id.b_reGet);
        mTv_error_detail = findViewById(R.id.tv_error_detail);
        imgProgress = findViewById(R.id.img_progress);
        flProgress = findViewById(R.id.rl_img);
        mRotatingImge = findViewById(R.id.rotating_imge);
    }

    @Override
    protected void initViews() {
        mContext = this;
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //  设置Copyright
        mTvCopyright.setText(String.format("Copyright © %s Reserved. v%s", getResources().getString(R.string.app_name), PackageInfoUtil.getVersionName(this)));
        mCustomDialog = new CustomDialog(mContext, R.style.customIosDialog, R.layout.dialog_ios_line_error);
        IPCSocketManager.getInstance().connectTcpService();

        mRotatingImgeDrawable = (AnimationDrawable) mRotatingImge.getDrawable();
        animationDrawable = (AnimationDrawable) imgProgress.getDrawable();
    }


    /**
     * 检查版本
     */
    private void checkAppUpdate(LineTaskProgressListener lineTaskProgressListener) {
        mAppUpdate = new AppUpdate(this);
        mAppUpdate.setIsNeedUpdateCallBack(new AppUpdate.UpdateResult() {
            @Override
            public void callBack(boolean result) {
                if (result) {
                    SharePreferenceUtil.saveTimeAppUpdate(SplashActivity.this, System.currentTimeMillis());
                    AutoLogin.loginOrGoMain(SplashActivity.this, lineTaskProgressListener);
                }
            }
        });
        mAppUpdate.checkUpdate();
    }

    @Override
    protected void initData() {
//        if (true) {
//            DataCenter.getInstance().setDomain("test01.ccenter.test.so");
//            DataCenter.getInstance().setIp("http://192.168.0.92:8787");
//            AutoLogin.loginOrGoMain(this);
//            mNumberProgressBar.setProgress(100);
//            return;
//        }
//
//        if (true) {
//            DataCenter.getInstance().setDomain("test71.hongtubet.com");
//            DataCenter.getInstance().setIp("http://47.90.51.75:8787");
//            AutoLogin.loginOrGoMain(this);
//            mNumberProgressBar.setProgress(100);
//            return;
//        }
//
//
//        if (true) {
//            DataCenter.getInstance().setDomain("test18.ampinplayopt0matrix.com");
//            DataCenter.getInstance().setIp("https://61.28.172.7:8989");
//            AutoLogin.loginOrGoMain(this);
//            mNumberProgressBar.setProgress(100);
//            return;
//        }

        mHelperUtil = new LineHelperUtil();
        mHelperUtil.checkSp();
        setProgressUI();
        mReGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelperUtil.checkSp();
            }
        });
        mTv_error_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLineErrorDialogBean == null) return;
                oPenErrorDialog(mLineErrorDialogBean);
                getLocalIp();
            }
        });
        mNumberProgressBar.setProgressNumber(new NumberProgressBar.changeProgressNumber() {
            @Override
            public void progressChangeClick(float progressNumber) {
                Log.e("NumberProgressBar", progressNumber + "传递距离");
                ViewGroup.LayoutParams layoutParams = (ViewGroup.MarginLayoutParams) imgProgress.getLayoutParams();
                ((ViewGroup.MarginLayoutParams) layoutParams).setMarginStart((int) progressNumber);
                imgProgress.setLayoutParams(layoutParams);
            }
        });

    }


    private void setProgressUI() {
        mHelperUtil.setLineTaskProgressListener(new LineTaskProgressListener() {
            @Override
            public void onGetLineSuccess(String domain, String ip) {

            }

            @Override
            public void onProgressBarChange(int current, int max) {
                if (!isDestroyed() && mNumberProgressBar != null && mTvLoading != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mReGet.setVisibility(View.GONE);
                            mTv_error_detail.setVisibility(View.GONE);
                            mNumberProgressBar.setProgress(current);
                            if (current < progress_finish_GetBaseLine) {
                                mTvLoading.setText(LineProgressString.LINE_PROGRESS_GETTING_BASE_LINE);
                            } else if (current < progress_start_CheckDomain && current >= progress_finish_GetBaseLine) {
                                mTvLoading.setText(LineProgressString.LINE_PROGRESS_GETTING_LINE);
                            } else if (current == progress_start_CheckDomain) {
                                mTvLoading.setText(LineProgressString.LINE_PROGRESS_SETTING_PORT);
                            } else if (current >= progress_finish_CheckDomain && current < progress_finish_CheckLine) {
                                mTvLoading.setText(LineProgressString.LINE_PROGRESS_CHECKING_LINE);
                            } else if (current == progress_finish_CheckLine) {
                                mTvLoading.setText(LineProgressString.LINE_PROGRESS_CONNECTING);
                            } else if (current == 100) {
                                mTvLoading.setText(LineProgressString.LINE_PROGRESS_CONNECTED);
                            }
                        }
                    });
                }
            }

            @Override
            public void onErrorSimpleReason(String result) {
                if (!isDestroyed() && mTvLoading != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTvLoading.setText(result);
                            mReGet.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onErrorComplexReason(LineErrorDialogBean lineErrorDialogBean) {
                if (!isDestroyed() && mTv_error_detail != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLineErrorDialogBean = lineErrorDialogBean;
                            mTv_error_detail.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onSpalshGetLineSuccess(String domain, String ip, LineTaskProgressListener lineTaskProgressListener) {
                Log.e("LineHelperUtil ", "跳到 SplashActivity " + domain + " " + ip);
                if (!isDestroyed()) {

                    long time = SharePreferenceUtil.getTimeAppUpdate(SplashActivity.this);
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - time > AutoLogin.interval_checke_APP_UPDATE) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkAppUpdate(lineTaskProgressListener);
                            }
                        });
                    } else {
                        AutoLogin.loginOrGoMain(SplashActivity.this, lineTaskProgressListener);
                    }
                }
            }


        });
    }


    /**
     * 获取屏宽度
     */

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 错误信息dialog
     */
    public void oPenErrorDialog(LineErrorDialogBean lineErrorDialogBean) {
        showErrorDialog();
        initDialog(lineErrorDialogBean);

    }

    private void initDialog(LineErrorDialogBean lineErrorDialogBean) {
        mCustomDialog.setCanceledOnTouchOutside(false);
        mCustomDialog.setCancelable(false);
        TextView tv_title = mCustomDialog.findViewById(R.id.tv_title);
        TextView tv_msg = mCustomDialog.findViewById(R.id.tv_msg);
        TextView tvOk = mCustomDialog.findViewById(R.id.ok);

        tv_title.setText("线路检测失败");

        String ip = "获取公网IP失败...";
        if (!TextUtils.isEmpty(mOutIp)) {
            ip = mOutIp;
        }
        String tip = "出现未知错误，请联系在线客服并提供以下信息"
                + "\n当前ip: " + ip
                + "\n版本号: Android v" + PackageInfoUtil.getVersionName(mContext) + "  " + PackageInfoUtil.getVersionCode(mContext);

        if (lineErrorDialogBean.getCode().equals(LineProgressString.CODE_001)) {
            tip = "网络连接失败，请确认您的网络连接正常后再次尝试！";
        } else if (lineErrorDialogBean.getCode().equals(LineProgressString.CODE_002)) {
            tip = "线路获取失败，请确认您的网络连接正常后再次尝试！";
        }

        tv_msg.setText(tip);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dimissErrorDialog();
            }
        });
    }


    public void showErrorDialog() {
        if (mCustomDialog != null) {
            mCustomDialog.show();
        }
    }

    public void dimissErrorDialog() {
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
    }

    /**
     * 获取公网ip
     */
    private void getLocalIp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Request request = new Request.Builder().url(ConstantValue.IP_LOCAL_URL).get().build();
        builder.build().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "getLocalIp onError  " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String json = response.body().string();
                    if (TextUtils.isEmpty(json)) return;
                    Log.e(TAG, "getLocalIp response  " + json);
                    try {
                        CommonIp commonIp = GsonUtil.GsonToBean(json, CommonIp.class);
                        Log.e("getLocalIp onError", mOutIp + commonIp);
                        if (commonIp != null) {
                            mOutIp = commonIp.getIp();
                            if (!isDestroyed()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mLineErrorDialogBean != null) {
                                            if (mCustomDialog.findViewById(R.id.tv_msg) != null) {
                                                ((TextView) mCustomDialog.findViewById(R.id.tv_msg)).setText("错误码: " + mLineErrorDialogBean.getCode()
                                                        + "\n当前ip: " + mOutIp
                                                        + "\n版本号: Android v" + PackageInfoUtil.getVersionName(mContext) + "  " + PackageInfoUtil.getVersionCode(mContext)
                                                        + "\n"
                                                        + "\n" + "很抱歉，请联系客服并提供以上信息");
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mLineErrorDialogBean != null) {
                                    if (mCustomDialog.findViewById(R.id.tv_msg) != null) {
                                        ((TextView) mCustomDialog.findViewById(R.id.tv_msg)).setText("错误码: " + mLineErrorDialogBean.getCode()
                                                + "\n当前ip: " + "未知"
                                                + "\n版本号: Android v" + PackageInfoUtil.getVersionName(mContext) + "  " + PackageInfoUtil.getVersionCode(mContext)
                                                + "\n"
                                                + "\n" + "很抱歉，请联系客服并提供以上信息");
                                    }
                                }
                            }
                        });

                    }
                }
            }
        });
    }


    /**
     * 重力感應  橫屏切換
     */
    @Subscribe(tags = {@Tag(ConstantValue.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)})
    public void landScapeReverse(String s) {
        LogUtils.d(s);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    /**
     * 重力感應  豎屏切換
     */
    @Subscribe(tags = {@Tag(ConstantValue.SCREEN_ORIENTATION_LANDSCAPE)})
    public void landScape(String s) {
        LogUtils.d(s);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAvi == null) return;
        mAvi.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable.start();
        mRotatingImgeDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        animationDrawable.stop();
        mRotatingImgeDrawable.stop();
        if (mAvi == null) return;
        mAvi.hide();
    }


    @Override
    protected void onDestroy() {
        dimissErrorDialog();
        RxBus.get().unregister(this);
        super.onDestroy();
    }

    /**
     * 动态权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 1) {
                mAppUpdate.permissionsResult(grantResults);
            }
        } else {
            // Permission Denied
            SingleToast.showMsg("您没有授权存储权限，请在设置中打开授权");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}