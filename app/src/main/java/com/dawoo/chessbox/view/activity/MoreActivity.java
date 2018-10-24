package com.dawoo.chessbox.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dawoo.chessbox.R;
import com.guoqi.iosdialog.IOSDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 查看更多
 */
public class MoreActivity extends BaseActivity {



    @BindView(R.id.tv_cache_size)
    TextView mTvCacheSize;
    @BindView(R.id.left_btn)
    FrameLayout leftBtn;
    @BindView(R.id.title_name)
    TextView titleName;


    @Override
    protected void createLayoutView() {
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        titleName.setText("设置");
    }

    @Override
    protected void initData() {
        initCacheSize("");
    }


//    @OnClick({R.id.common_problems_rl, R.id.register_protocol_rl, R.id.about_us_rl, R.id.luanguage_choice_rl})
//    public void onViewClicked(View view) {
//        SoundUtil.getInstance().playVoiceOnclick();
//        switch (view.getId()) {
//            case R.id.common_problems_rl: {
//                startActivity(new Intent(this, ProblemsActivity.class));
//                break;
//            }
//            case R.id.about_us_rl: {
//                startActivity(new Intent(this, AboutActivity.class));
//                break;
//            }
//            default:
//        }
//    }


    public void initCacheSize(String s) {
        mTvCacheSize.setText(getAllCache());
    }

    @OnClick(R.id.setting_clear_cache)
    public void onViewClicked() {
//        SoundUtil.getInstance().playVoiceOnclick();
//        if (getAllCache().equals("0.0Byte")) {
//            SingleToast.showMsg("缓存已清除~");
//            return;
//        }
        final IOSDialog alertDialog = new IOSDialog(this);
        alertDialog.builder();
        alertDialog.setCancelable(true);
        alertDialog.setMsg("是否确定清除缓存?");
        alertDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvCacheSize.setText("0M");
                alertDialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private String getAllCache() {
        return "0M";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.left_btn)
    public void onViewClicked1() {
        finish();
    }
}
