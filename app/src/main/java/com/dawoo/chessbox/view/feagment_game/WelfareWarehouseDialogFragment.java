package com.dawoo.chessbox.view.feagment_game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.util.ComplexFragmentManager;
import com.dawoo.chessbox.util.SoundUtil;
import com.hwangjr.rxbus.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 福利仓库Dialog
 */
public class WelfareWarehouseDialogFragment extends BaseDialogFragment {


    @BindView(R.id.img_close)
    ImageView imgClose;
    Unbinder unbinder;
    @BindView(R.id.tv_deposit)
    TextView tvDeposit;
    @BindView(R.id.tv_draw)
    TextView tvDraw;
    @BindView(R.id.tv_tips)
    TextView tvTips;

    @Override
    protected int getViewId() {
        return R.layout.qt_welfarewarehouse_dialogfragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().register(this);
    }

    @Override
    protected void initViews(View view) {
        intScreenHProportion = 0.93;
        intScreenWProportion = 0.7;

    }

    @Override
    protected void initData() {
        ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), WelfareDepositFragment.class);
        tvDeposit.setSelected(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ComplexFragmentManager.getInstance().clear();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.get().unregister(this);
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @OnClick({R.id.tv_deposit, R.id.tv_draw, R.id.img_close})
    public void onViewClicked(View view) {

        if (view.getId()!=R.id.img_close){
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        }else {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        }
        switch (view.getId()) {
            case R.id.tv_deposit:
                tvDeposit.setSelected(true);
                tvDraw.setSelected(false);
                ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), WelfareDepositFragment.class);
                break;
            case R.id.tv_draw:
                tvDeposit.setSelected(false);
                tvDraw.setSelected(true);
                ComplexFragmentManager.getInstance().switchFragment(getChildFragmentManager(), WelfareDrawFragment.class);
                break;
            case R.id.img_close:
                dismiss();
                break;
        }
    }
}
