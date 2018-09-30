package com.dawoo.chessbox.view.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.SPTool;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.activity.ActivityStackManager;
import com.dawoo.coretool.util.activity.DensityUtil;
import com.dawoo.coretool.util.math.BigDemicalUtil;
import com.dawoo.chessbox.BoxApplication;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.adapter.HomeAdapter.DepostTypeAdapter;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.UserAccount;
import com.dawoo.chessbox.bean.payInfo.DepositBean;
import com.dawoo.chessbox.bean.payInfo.DepositResultBean;
import com.dawoo.chessbox.bean.payInfo.PayItemBean;
import com.dawoo.chessbox.bean.payInfo.PayTypeBean;
import com.dawoo.chessbox.bean.payInfo.PopPayBean;
import com.dawoo.chessbox.mvp.presenter.DisportPresenter;
import com.dawoo.chessbox.mvp.view.IDisportView;
import com.dawoo.chessbox.net.GlideApp;
import com.dawoo.chessbox.util.ActivityUtil;
import com.dawoo.chessbox.util.DepositUtil;
import com.dawoo.chessbox.util.FileTool;
import com.dawoo.chessbox.util.NetUtil;
import com.dawoo.chessbox.util.PermissionUtil;
import com.dawoo.chessbox.util.SingleToast;
import com.dawoo.chessbox.view.view.DateTimePickerDailog;
import com.dawoo.chessbox.view.view.DepositSuccessDialog;
import com.dawoo.chessbox.view.view.NoMoveRecycleView;
import com.dawoo.chessbox.view.view.PayNoticeDialog;
import com.dawoo.chessbox.view.view.SelectBankDialog;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author simon
 * 资金存取
 */

public class DepositActivity extends BaseActivity implements IDisportView, PayNoticeDialog.PostInfoInterface {
    @BindView(R.id.content_scroll)
    ScrollView scrollView;
    @BindView(R.id.select_bank_rl)
    RelativeLayout bankSelectedRelative;
    @BindView(R.id.select_bank_sp)
    TextView selectBankTV;
    @BindView(R.id.selected_iv)
    ImageView selectedIV;
    @BindView(R.id.show_notice)
    TextView showNoticeTV;
    private List<PayItemBean> mArrayString = new ArrayList<>();
    @BindView(R.id.pay_type_rl)
    RecyclerView PayTypeRecycle;
    @BindView(R.id.pay_sec_type_rl)
    NoMoveRecycleView chilePayTypeRecycle;
    @BindView(R.id.pay_money_rl)
    NoMoveRecycleView payMoneyRecycle;

    @BindView(R.id.money_et)
    EditText moneyEditView;
    @BindView(R.id.random_tv)
    TextView randomMoneyView;
    @BindView(R.id.pay_sec_type)
    LinearLayout payLayout;

    @BindView(R.id.user_name_tv)
    TextView userNameTV;
    @BindView(R.id.user_account_tv)
    TextView accountTv;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.note_tv)
    TextView noteTV;

    @BindView(R.id.default_pay_ll)
    LinearLayout defayltLayout;
    @BindView(R.id.sub_ll)
    LinearLayout submitLayout;

    @BindView(R.id.bitcoin_ll)
    LinearLayout bitLayout;
    @BindView(R.id.bitcoin_name)
    TextView bitName;
    @BindView(R.id.qrcode_iv)
    ImageView bitQrCode;
    @BindView(R.id.save_pic_tv)
    TextView savaTV;
    @BindView(R.id.address_et)
    EditText addressET;
    @BindView(R.id.text_id_et)
    EditText txIdET;
    @BindView(R.id.count_et)
    EditText countET;
    @BindView(R.id.charge_time)
    TextView timeTV;
    @BindView(R.id.left_btn)
    FrameLayout leftBtn;

    private DisportPresenter mPresenter;
    private List<DepositBean> mDeposits = new ArrayList<>();
    private PayTypeBean childs;
    private DepostTypeAdapter mAdapter, childAdapter, moneyAdapter;
    private int bankSelected, firstTypeSelected;
    private PayNoticeDialog mDialog;
    private double money;
    //long refrashTime;
    private List<String> defaultMoney = new ArrayList<>();
    private DateTimePickerDailog timePop;
    private DepositSuccessDialog successDialog;
    private SelectBankDialog<PayItemBean> mBankPop;
    private boolean hide;
    private UserAccount mAccount;

    public static DepositActivity newInstance() {
        DepositActivity fragment = new DepositActivity();
        return fragment;
    }


    protected void loadData() {
        mPresenter.getPayTypes();
        mPresenter.getAccount();
    }

    @Override
    protected void createLayoutView() {
        setContentView(R.layout.qt_activity_deposit);
    }

    @Override
    protected void initViews() {
        RxBus.get().register(this);
        PayTypeRecycle.setLayoutManager(new GridLayoutManager(mContext, 5));
        PayTypeRecycle.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int debice = DensityUtil.dp2px(mContext, 4);
                outRect.top = debice;
                outRect.bottom = debice;
                outRect.left = debice;
                outRect.right = debice;
            }
        });
        mAdapter = new DepostTypeAdapter(R.layout.item_deposit_type);
        PayTypeRecycle.setAdapter(mAdapter);
        chilePayTypeRecycle.setLayoutManager(new GridLayoutManager(mContext, 2));
        chilePayTypeRecycle.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int debice = DensityUtil.dp2px(mContext, 4);
                outRect.top = debice;
                outRect.bottom = debice;
                outRect.left = debice;
                outRect.right = debice;
            }
        });
        childAdapter = new DepostTypeAdapter(R.layout.item_child_depost_type);
        chilePayTypeRecycle.setAdapter(childAdapter);
        payMoneyRecycle.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        payMoneyRecycle.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = 5;
                outRect.bottom = 5;
                outRect.left = 5;
                outRect.right = 5;
            }
        });
        moneyAdapter = new DepostTypeAdapter(R.layout.item_money_selected_layout);
        payMoneyRecycle.setAdapter(moneyAdapter);
        defaultMoney.add("101");
        defaultMoney.add("302");
        defaultMoney.add("504");
        defaultMoney.add("1006");
        defaultMoney.add("4998");
        moneyAdapter.replaceData(defaultMoney);
        int[] colors = new int[]{ContextCompat.getColor(mContext, R.color.color_theme_blue),
                ContextCompat.getColor(mContext, R.color.color_theme_red),
                ContextCompat.getColor(mContext, R.color.color_theme_green_black),
                ContextCompat.getColor(mContext, R.color.color_theme_black)};
        refreshLayout.setColorSchemeColors(colors);
        initListeners();
        leftBtn.setOnClickListener(v -> finish());

    }

    @Override
    protected void initData() {
        mPresenter = new DisportPresenter(mContext, this);
        loadData();
    }


    /**
     * 账户数据
     */
    private void initAccountData(Object o) {
        if (o != null && o instanceof UserAccount) {
            // 设置账户
            mAccount = ((UserAccount) o);
            UserAccount.UserBean userBean = mAccount.getUser();
            userNameTV.setText(userBean.getUsername());
            accountTv.setText(userBean.getCurrency() + BigDemicalUtil.moneyFormat(userBean.getTotalAssets()));
            DataCenter.getInstance().setRealName(userBean.getRealName());
        }
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_MINE_LINK)})
    public void receiveUserAccount(UserAccount userAccount) {
        if (userAccount.getUser() != null) {
            UserAccount.UserBean userBean = userAccount.getUser();
            userNameTV.setText(userBean.getUsername());
            accountTv.setText(userBean.getCurrency() + BigDemicalUtil.moneyFormat(userBean.getTotalAssets()));
        }

    }

    /**
     * 初始化银行选择下来菜单
     */
    void initBankPop() {
        mBankPop = new SelectBankDialog(mContext);
        mBankPop.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                selectedIV.setImageResource(R.mipmap.drop_down);
            }
        });
        mBankPop.setSureClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBankPop.dismiss();
                if (mBankPop.getIndex() == bankSelected) {
                    return;
                }
                bankSelected = mBankPop.getIndex();
                selectBankTV.setText(mArrayString.get(bankSelected).getPayName());
                initPay();
            }
        });
    }

    void initListeners() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
              /*  if (firstTypeSelected == position) {
                    return;
                }*/

                if (firstTypeSelected == position) {
                    if (mDeposits.size() > 1 ){
                        return;
                    }else if (mDeposits.size() == 1){
                        if (!mDeposits.get(position).getCode().contains("http")){
                            return;
                        }
                    }
                }
                if (mDeposits.get(position).getCode().contains("http")) {
                    ActivityUtil.startWebView(mDeposits.get(position).getCode(), mDeposits.get(position).getName(), ConstantValue.WEBVIEW_TYPE_THIRD_ORDINARY, 1);
                    return;
                }
                mAdapter.setSeletedIndex(position);
                bankSelected = 0;
                firstTypeSelected = position;
                mPresenter.getSecTypes(mDeposits.get(position).getCode());
            }

        });
        childAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == bankSelected) {
                    return;
                }
                childAdapter.setSeletedIndex(position);
                bankSelected = position;
                initPay();
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    loadData();
                } else {
                    initData();
                }
            }
        });
        moneyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                moneyEditView.setText(adapter.getItem(position) + "");
            }
        });
        moneyEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (randomMoneyView.getVisibility() == View.VISIBLE) {
                    int randomNum = new Random().nextInt(99);
                    randomMoneyView.setText("." + randomNum);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
        RxBus.get().unregister(this);

    }

    /**
     * 获取充值列表
     *
     * @param o
     */
    @Override
    public void getPayTypes(List<DepositBean> o) {
        if (o == null || o.isEmpty()) {
            ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_get_type_err));
            return;
        }
        //  refrashTime = System.currentTimeMillis();
        mDeposits = o;
        firstTypeSelected = 0;
        bankSelected = 0;
        mAdapter.replaceData(mDeposits);
        refreshLayout.setRefreshing(false);
        if (!mDeposits.get(firstTypeSelected).getCode().contains("http")) {
            mPresenter.getSecTypes(mDeposits.get(firstTypeSelected).getCode());
        }

      //  mPresenter.getSecTypes(mDeposits.get(firstTypeSelected).getCode());
    }

    @Override
    public void getSecType(PayTypeBean o) {
        if (o == null) {
            ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_get_type_err));
        } else {
            hide = o.isHide();
            childs = o;
            initDatas();
            showNoticeTV.setVisibility(o.isNewActivity() ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 获取提交信息优惠
     *
     * @param o
     */
    @Override
    public void getPayInfo(PopPayBean o) {
        if (o == null) {
            ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_get_sales_err));
            return;
        }
        if (childs.getArrayList().get(bankSelected).getType() == 2 || ConstantValue.BITCOIN.equalsIgnoreCase(mDeposits.get(firstTypeSelected).getCode())) {
            if (mDialog == null) {
                mDialog = new PayNoticeDialog(mContext);
                mDialog.setSureOnlick(this);
            }
            mDialog.setData(money, o);
            mDialog.show();
        } else {
            Bundle mbundle = new Bundle();
            mbundle.putBoolean("isHide", hide);
            mbundle.putString("hideCode", childs.getArrayList().get(bankSelected).getCode());
            mbundle.putDouble("money", money);
            mbundle.putSerializable("popPayBean", o);
            mbundle.putSerializable("item", childs.getArrayList().get(bankSelected));
            mbundle.putString("code", mDeposits.get(firstTypeSelected).getCode());
            mbundle.putSerializable("counters", (Serializable) childs.getCounterRechargeTypes());
            startActivity(new Intent(mContext, DisportLevelActivity.class).putExtras(mbundle));
        }
    }

    /**
     * 存款信息提交返回
     *
     * @param o
     */
    @Override
    public void postPayInfo(DepositResultBean o) {
        if (o == null) {
            ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_post_err));
        } else {
            if (o.getPayLink() == null) {
                if (successDialog == null) {
                    successDialog = new DepositSuccessDialog(mContext);
                    successDialog.setCloseLinstener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            successDialog.dismiss();
                        }
                    });
                    successDialog.setSureLinstener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            successDialog.dismiss();

                        }
                    });
                    successDialog.setRechargeLinstener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            successDialog.dismiss();
                            ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
                            RxBus.get().post(ConstantValue.SHOW_CAPITALRECORDDIALOGFRAGMENR, "");
                        }
                    });
                }
                successDialog.show();
                return;
            }
          //  ActivityUtil.startWebView(o.getPayLink(), "", ConstantValue.WEBVIEW_TYPE_THIRD_ORDINARY, 1);
            if (TextUtils.isEmpty(o.getPayLink())) {
                SingleToast.showMsg("链接为空，请重试");
                return;
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(o.getPayLink());
            intent.setData(content_url);
            startActivity(intent);
        }
    }

    @Override
    public void onAccountResult(Object o) {
        initAccountData(o);
    }

    void initDatas() {
        //比特币
        if (mDeposits.get(firstTypeSelected).getCode().equalsIgnoreCase(ConstantValue.BITCOIN)) {
            defayltLayout.setVisibility(View.GONE);
            bitLayout.setVisibility(View.VISIBLE);
            PayItemBean bean = childs.getArrayList().get(bankSelected);
            SpannableStringBuilder mNameSpan = new SpannableStringBuilder("姓名：");

            if (bean.getFullName() != null) {
                mNameSpan.append(bean.getFullName());
            } else {
                mNameSpan.append(bean.getAliasName() + "");
            }
            bitName.setText(mNameSpan);
            if (bean.getQrCodeUrl() != null && !bean.getQrCodeUrl().isEmpty()) {
                GlideApp.with(this).load(NetUtil.handleUrl(bean.getQrCodeUrl())).into(bitQrCode);
            }
            changeNote();
        } else {
            //非比特币
            defayltLayout.setVisibility(View.VISIBLE);
            bitLayout.setVisibility(View.GONE);
            if (ConstantValue.ONLINE.equalsIgnoreCase(mDeposits.get(firstTypeSelected).getCode())) {
                bankSelected = 0;
                payLayout.setVisibility(View.GONE);
                mArrayString = childs.getArrayList();
                selectBankTV.setText(mArrayString.get(0).getPayName());
                initPay();
                bankSelectedRelative.setVisibility(View.VISIBLE);
            } else {
                if (mBankPop != null) {
                    mBankPop.clearDatas();
                }
                bankSelectedRelative.setVisibility(View.GONE);
                payLayout.setVisibility(View.VISIBLE);
                childAdapter.replaceData(childs.getArrayList());
            }

            initPay();
        }
        submitLayout.setVisibility(View.VISIBLE);

    }

    void initPay() {

        if (payLayout.getVisibility() == View.VISIBLE) {
            if (childs.getQuickMoneys() != null && !childs.getQuickMoneys().isEmpty()) {
                moneyAdapter.replaceData(childs.getQuickMoneys());
            } else {
                moneyAdapter.replaceData(defaultMoney);
            }
        }
        if (childs.getArrayList().get(bankSelected).isRandomAmount()) {
            randomMoneyView.setVisibility(View.VISIBLE);
        } else {
            randomMoneyView.setVisibility(View.GONE);
        }
        moneyEditView.setText("");
        moneyEditView.setHint("￥" + childs.getArrayList().get(bankSelected).getSingleDepositMin()
                + "~￥" + childs.getArrayList().get(bankSelected).getSingleDepositMax());

        changeNote();

    }

    void changeNote() {

        String notes = "";
        PayItemBean bean = childs.getArrayList().get(bankSelected);
        notes = DepositUtil.getNoteByCode(mDeposits.get(firstTypeSelected).getCode(), bean.isRandomAmount(), bean.getType(), true);
        notes = notes.replaceAll("<br>", "\n");
        SpannableStringBuilder mspan = new SpannableStringBuilder("温馨提示：\n" + notes);
        if (notes.contains("客服")) {
            SpannableString colorSpan = new SpannableString("点击联系在线客服");
            colorSpan.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(mContext.getResources().getColor(R.color.colorPrimary));
                    ds.setUnderlineText(false);
                    ds.clearShadowLayer();
                }

                @Override
                public void onClick(View widget) {
                    String url = (String) SPTool.get(BoxApplication.getContext(), ConstantValue.KEY_CUSTOMER_SERVICE, "");
                    boolean isInlay = (Boolean) SPTool.get(BoxApplication.getContext(), ConstantValue.KEY_CUSTOMER_SERVICE_ISINLAY, false);
                    if (TextUtils.isEmpty(url)) {
                        SingleToast.showMsg("暂无客服地址");
                        return;
                    }
                    if (isInlay) {
                        ActivityUtil.startWebView(url, "", ConstantValue.WEBVIEW_TYPE_THIRD_ORDINARY, 1);
                    } else {
                        //去浏览器
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        Uri content_url = Uri.parse(url);
                        intent.setData(content_url);
                        mContext.startActivity(intent);
                    }
                }
            }, 0, colorSpan.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mspan.append(colorSpan);
        }
        noteTV.setText(mspan);
        noteTV.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick({R.id.submit_bt, R.id.save_pic_tv, R.id.charge_time, R.id.select_bank_sp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit_bt:
                if (childs == null) {
                    ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_unselected_type_toast));
                    return;
                } else if (bankSelected == -1) {
                    ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_unselected_type_toast));
                    return;
                }
                setInfo();
                break;
            case R.id.save_pic_tv:
                if (bitQrCode.getDrawable() == null) {
                    ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_uncatch_image_toast));
                    return;
                }
                bitQrCode.setDrawingCacheEnabled(true);
                if (!PermissionUtil.checkPermission(this, ConstantValue.PERMISSIONS_STORAGE_WRITE, 1)) {
                    return;
                }
                Bitmap bitmap = bitQrCode.getDrawingCache();
                if (bitmap == null) {
                    ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_uncatch_image_toast));
                    return;
                }
                FileTool.saveImageToGallery(mContext, bitmap);
                bitQrCode.destroyDrawingCache();
                break;
            case R.id.charge_time:
                showDatePop();
                break;
            case R.id.select_bank_sp:
                if (mArrayString.isEmpty()) {
                    ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_get_type_err));
                    return;
                }
                if (mBankPop == null) {
                    initBankPop();
                }
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                if (mBankPop.isEmpty()) {
                    mBankPop.setData(mArrayString, bankSelected);
                }
                selectedIV.setImageResource(R.mipmap.up);
                mBankPop.show();
                break;
            default:
                break;
        }
    }

    /**
     * 获取弹框事件提交充值信息
     */
    @Override
    public void postPayInfo() {
        long activityId = mDialog.getId();
        PayItemBean bean = childs.getArrayList().get(bankSelected);

        String code = mDeposits.get(firstTypeSelected).getCode();
        if (code == null || code.isEmpty()) {
            return;
        }
        if (ConstantValue.ONLINE.equalsIgnoreCase(code)) {
            mPresenter.postOnline(money, bean.getRechargeType(), bean.getSearchId(), activityId, bean.getBankCode());
        } else if (ConstantValue.BITCOIN.equalsIgnoreCase(code)) {
            mPresenter.postBitcoin(bean.getRechargeType(), bean.getSearchId(), activityId, addressET.getText().toString().trim()
                    , txIdET.getText().toString().trim(), money, timeTV.getText().toString().trim());
        } else {
            mPresenter.postScan(money, bean.getRechargeType(), bean.getSearchId(), activityId, "");
        }
    }

    void setInfo() {
        PayItemBean bean = childs.getArrayList().get(bankSelected);
        //比特币
        if (ConstantValue.BITCOIN.equalsIgnoreCase(mDeposits.get(firstTypeSelected).getCode())) {
            if (addressET.getText().toString().isEmpty()) {
                ToastUtil.showToastShort(mContext, addressET.getHint().toString());
            } else if (addressET.getText().toString().trim().length() > 34) {
                ToastUtil.showToastShort(mContext, "请输入26到34位比特币地址");
            } else if (addressET.getText().toString().trim().length() < 26) {
                ToastUtil.showToastShort(mContext, "请输入26到34位比特币地址");
            } else if (txIdET.getText().toString().isEmpty()) {
                ToastUtil.showToastShort(mContext, txIdET.getHint().toString());
            } else if (countET.getText().toString().isEmpty()) {
                ToastUtil.showToastShort(mContext, countET.getHint().toString());
            } else if (timeTV.getText().toString().isEmpty()) {
                ToastUtil.showToastShort(mContext, timeTV.getHint().toString());
            } else {
                money = Double.parseDouble(countET.getText().toString());
                if (money <= 0) {
                    ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_bitcoin_need_toast));
                } else {
                    mPresenter.getPayInfo(bean.getSearchId(), money, bean.getDepositWay(), txIdET.getText().toString().trim());
                }
            }


        } else {
            if (moneyEditView.getText().toString().isEmpty()) {
                ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_money_need_toast));
            } else {
                money = Double.parseDouble(moneyEditView.getText().toString());
                int min = bean.getSingleDepositMin();
                int max = bean.getSingleDepositMax();
                if (randomMoneyView.getVisibility() == View.VISIBLE) {
                    String floatNum = "0" + randomMoneyView.getText().toString();
                    money = money + Double.valueOf(floatNum);
                }
                if (money < min) {
                    ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_money_toast));
                } else if (money > max) {
                    ToastUtil.showToastShort(mContext, mContext.getString(R.string.deposit_money_toastmax));
                } else {
                    mPresenter.getPayInfo(bean.getSearchId(), money, bean.getDepositWay());
                }
            }
        }
    }


    void showDatePop() {
        if (timePop == null) {
            timePop = new DateTimePickerDailog(mContext);
            timePop.setSureTimeClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeTV.setText(timePop.getTime());
                    timePop.dismiss();
                }
            });
        }
        timePop.show();
    }

    @Subscribe(tags = {@Tag(ConstantValue.EVENT_TYPE_NETWORK_EXCEPTION)})
    public void shrinkRefreshView(String s) {
        LogUtils.d(s);
        //  收起刷新
        if (refreshLayout.isRefreshing() && null != refreshLayout) {
            refreshLayout.setRefreshing(false);
        }
    }

}
