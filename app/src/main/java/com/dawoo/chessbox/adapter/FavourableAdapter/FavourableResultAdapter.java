package com.dawoo.chessbox.adapter.FavourableAdapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.FavourableReslutBean;


/**
 * 优惠活动　申请结果
 */
public class FavourableResultAdapter extends BaseQuickAdapter {

    private Context mContext;
    private setBtnOnClick setBtnOnClick;

    public FavourableResultAdapter(Context context, int layoutResId) {
        super(layoutResId);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        ImageView imgTips = helper.getView(R.id.img_tips);
        TextView tvTitle = helper.getView(R.id.tv_title);
        TextView tvAlreadyNUmber = helper.getView(R.id.tv_already_number);
        TextView tvTotleNumber = helper.getView(R.id.tv_totle_number);
        SeekBar tvSeekBar = helper.getView(R.id.sb_seekbar);
        TextView tvApplayReward = helper.getView(R.id.tv_apply_reward);
        tvSeekBar.setClickable(false);
        tvSeekBar.setSelected(false);
        tvSeekBar.setEnabled(false);
        tvSeekBar.setFocusable(false);

        FavourableReslutBean.Data.ApplyDetails applyDetails = (FavourableReslutBean.Data.ApplyDetails) item;
        if (applyDetails != null) {
            if (applyDetails.showSchedule) {
                tvSeekBar.setVisibility(View.VISIBLE);
                int allProgress = (int) applyDetails.getStandard();
                int progress = (int) applyDetails.getReached();
                tvSeekBar.setMax(allProgress);
                tvSeekBar.setProgress(progress);
                tvAlreadyNUmber.setVisibility(View.VISIBLE);
                tvTotleNumber.setVisibility(View.VISIBLE);
                tvAlreadyNUmber.setText(applyDetails.getReached()+ "");
                tvTotleNumber.setText("/" + applyDetails.getStandard());
                if (applyDetails.isSatisfy()) {
                    tvAlreadyNUmber.setTextColor(mContext.getResources().getColor(R.color.qt_forgetpsw_send_code));
                } else {
                    tvAlreadyNUmber.setTextColor(mContext.getResources().getColor(R.color.qt_favoubale_reslut_text));
                }
            } else {
                tvSeekBar.setVisibility(View.GONE);
                if (applyDetails.isSatisfy()) {
                    tvTitle.setTextColor(mContext.getResources().getColor(R.color.qt_forgetpsw_send_code));
                } else {
                    tvTitle.setTextColor(mContext.getResources().getColor(R.color.qt_favoubale_reslut_text));
                }
            }

            if (applyDetails.isSatisfy()) {
                imgTips.setImageResource(R.mipmap.success2);

            } else {
                imgTips.setImageResource(R.mipmap.error2);
            }

            if (applyDetails.isMayApply()) {
                tvApplayReward.setVisibility(View.VISIBLE);
            } else {
                tvApplayReward.setVisibility(View.GONE);
            }

            tvTitle.setText(applyDetails.getCondition());

            tvApplayReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setBtnOnClick.setApply(applyDetails.getTransactionNo());
                }
            });

        }
    }

    public interface setBtnOnClick {
        void setApply(String searchCode);
    }

    public void setOnApplayFavourableStatus(setBtnOnClick setBtnOnClick1) {
        setBtnOnClick = setBtnOnClick1;
    }

}
