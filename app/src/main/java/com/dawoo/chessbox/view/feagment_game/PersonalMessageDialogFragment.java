package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.math.BigDemicalUtil;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.DialogFramentManager;
import com.dawoo.chessbox.util.SoundUtil;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 个人中心
 */
public class PersonalMessageDialogFragment extends BaseDialogFragment {
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.tv_ID)
    TextView tvID;
    @BindView(R.id.tv_login_time)
    TextView tvLoginTime;
    @BindView(R.id.tv_personal_welfare)
    TextView tvPersonalWelfare;
    @BindView(R.id.tv_warehouse_welfare)
    TextView tvWarehouseWelfare;
    @BindView(R.id.btn_cancle)
    Button btnCancle;
    @BindView(R.id.btn_setting)
    Button btnSetting;
    @BindView(R.id.tv_tips)
    ImageView tvTips;
    @BindView(R.id.ll_tip_bg)
    RelativeLayout llTipBg;
    @BindView(R.id.img_close)
    ImageView imgClose;

    private Animation mAnimation;

    public static PersonalMessageDialogFragment newInstance() {

        Bundle args = new Bundle();

        PersonalMessageDialogFragment fragment = new PersonalMessageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getViewId() {
        return R.layout.qt_personal_message_dialog_fragment;
    }

    @Override
    protected void initViews(View view) {
    }

    @Override
    protected void initData() {
        RxBus.get().register(this);
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        tvID.setText(DataCenter.getInstance().getNickName());
        tvLoginTime.setText("上次登录时间："+DataCenter.getInstance().getLoginTimeLast());
       // tvPersonalWelfare.setText(DataCenter.getInstance().getWalletBalance());
        if (!TextUtils.isEmpty(DataCenter.getInstance().getWalletBalance())){
            tvPersonalWelfare.setText(BigDemicalUtil.moneyFormat(Double.valueOf(DataCenter.getInstance().getWalletBalance())));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.get().unregister(this);
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
    }

    @OnClick({R.id.btn_cancle, R.id.btn_setting,R.id.img_close})
    public void onViewClicked(View view) {
        if (view.getId()!=R.id.img_close){
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        }else {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        }
        switch (view.getId()) {
            case R.id.btn_cancle:
                if (SystemUtil.isFastClick()) return;
                startAnimation(btnCancle);
                DialogFramentManager.getInstance().showDialog(getActivity().getSupportFragmentManager(), new VoiceContrlFragment());
                break;
            case R.id.btn_setting:
                if (SystemUtil.isFastClick()) return;
                startAnimation(btnSetting);
                if (!DataCenter.getInstance().isLogin()){
                    ActivityUtil.gotoLogin();
                    return;
                }
                DialogFramentManager.getInstance().showDialog(getActivity().getSupportFragmentManager(), new LogOutDialogFragment());
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }

    private void startAnimation(TextView textView){
        if (mAnimation!=null){
            textView.startAnimation(mAnimation);
        }else {
            mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
            textView.startAnimation(mAnimation);
        }
    }

    /**
     * 网络异常 收起刷新
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_LOGOUT)})
    public void shrinkRefreshView(String s) {
      dismiss();
    }

    /**
     * 去首页　关闭界面
     */
    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_GOTOTAB_HOME)})
    public void homePage(String s){
        dismiss();
    }

}
