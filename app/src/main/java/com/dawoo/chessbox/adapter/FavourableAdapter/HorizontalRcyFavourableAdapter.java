package com.dawoo.chessbox.adapter.FavourableAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.FavourableCenterBean;

public class HorizontalRcyFavourableAdapter extends BaseQuickAdapter {
    private Context context;
    public startAnimationListener mStartAnimationListener;

    public HorizontalRcyFavourableAdapter(Context context , int layoutResId) {
        super(layoutResId);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        FavourableCenterBean.Data.ActivityList  favourableBean = (FavourableCenterBean.Data.ActivityList) item;
        TextView tvTypeName = helper.getView(R.id.tv_type_name);
        if (!TextUtils.isEmpty(favourableBean.getName())){
            if (favourableBean.isSelecte()){
                tvTypeName.setSelected(true);
            }else {
                tvTypeName.setSelected(false);
            }
            tvTypeName.setText(favourableBean.getName());
           // helper.setText(R.id.tv_type_name,favourableBean.getName());
        }
        if (mStartAnimationListener!=null&&favourableBean.isStartAnimation())
        mStartAnimationListener.startAnimationOnListener(tvTypeName);
    }

    public interface startAnimationListener {
        void startAnimationOnListener(TextView textView);
    }

    public void setStartAnimationListener(startAnimationListener changeProgressNumber) {
        mStartAnimationListener = changeProgressNumber;
    }
}
