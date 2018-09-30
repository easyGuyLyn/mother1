package com.dawoo.chessbox.adapter.FavourableAdapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.FavourableCenterBean;
import com.dawoo.coretool.util.ToastUtil;

public class FavourableTitleAdapter extends BaseQuickAdapter {

    private Context context;

    public FavourableTitleAdapter(Context context , int layoutResId) {
        super(layoutResId);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        FavourableCenterBean.Data favourableBean = (FavourableCenterBean.Data) item;
        TextView tvTypeName = helper.getView(R.id.tv_type_name);
        if (favourableBean.isSelecte()){
            tvTypeName.setSelected(true);
        }else {
            tvTypeName.setSelected(false);
        }
        if (!TextUtils.isEmpty(favourableBean.getActivityTypeName())){
            tvTypeName.setText(favourableBean.getActivityTypeName());
        }
    }

}
