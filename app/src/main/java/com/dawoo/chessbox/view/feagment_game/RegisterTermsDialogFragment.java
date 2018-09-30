package com.dawoo.chessbox.view.feagment_game;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.MineTeamsBean;
import com.dawoo.chessbox.mvp.presenter.CommonPresenter;
import com.dawoo.chessbox.mvp.view.IMineTeamsView;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.coretool.util.CleanLeakUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 注册页面的条约
 */

public class RegisterTermsDialogFragment extends BaseDialogFragment implements IMineTeamsView {
    @BindView(R.id.tv_register_term)
    TextView tvRegisterTerm;
    @BindView(R.id.img_close)
    ImageView imgClose;
    Unbinder unbinder;

    private CommonPresenter mPresenter;

    @Override
    protected int getViewId() {
        return R.layout.qt_register_terms;
    }

    @Override
    protected void initViews(View view) {

    }

    @Override
    protected void initData() {
        tvRegisterTerm.setMovementMethod(ScrollingMovementMethod.getInstance());
        mPresenter = new CommonPresenter(getActivity(), this);
        mPresenter.getTears();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        CleanLeakUtils.fixInputMethodManagerLeak(getActivity());
        mPresenter.onDestory();
    }

    @OnClick(R.id.img_close)
    public void onViewClicked() {
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        dismiss();
    }

    @Override
    public void onResult(Object o) {
        if (o != null && o instanceof MineTeamsBean) {
            MineTeamsBean mineTeamsBean = (MineTeamsBean) o;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvRegisterTerm.setText(Html.fromHtml(mineTeamsBean.getValue(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvRegisterTerm.setText(Html.fromHtml(mineTeamsBean.getValue()));
            }
        }
    }
}
