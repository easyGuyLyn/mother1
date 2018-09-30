package com.dawoo.chessbox.view.feagment_game;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawoo.chessbox.util.ComFragmentManager;
import com.dawoo.chessbox.util.DialogFramentManager;
import com.dawoo.coretool.util.LogUtils;
import com.dawoo.coretool.util.SPTool;
import com.dawoo.coretool.util.ToastUtil;
import com.dawoo.coretool.util.system.SystemUtil;
import com.dawoo.chessbox.ConstantValue;
import com.dawoo.chessbox.R;
import com.dawoo.chessbox.bean.DataCenter;
import com.dawoo.chessbox.bean.RegisterBean;
import com.dawoo.chessbox.bean.RegisterInfoBean;
import com.dawoo.chessbox.mvp.presenter.RegisterPresenter;
import com.dawoo.chessbox.mvp.view.IRegisterView;
import com.dawoo.chessbox.util.RexUtils;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.view.fragment.BaseFragment;
import com.hwangjr.rxbus.RxBus;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 注册Fragment
 */
public class RegisterFragment extends BaseFragment implements IRegisterView {

    @BindView(R.id.iv_invent_code)
    ImageView ivInventCode;
    @BindView(R.id.etYaoQinMa)
    EditText etYaoQinMa;
    @BindView(R.id.ll_invent_code)
    LinearLayout llInventCode;
    @BindView(R.id.iv_username)
    ImageView ivUsername;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.ll_username)
    LinearLayout llUsername;
    @BindView(R.id.iv_password)
    ImageView ivPassword;
    @BindView(R.id.etFirstPassword)
    EditText etFirstPassword;
    @BindView(R.id.ll_code)
    LinearLayout llCode;
    @BindView(R.id.iv_commit_password)
    ImageView ivCommitPassword;
    @BindView(R.id.etCommitPassword)
    EditText etCommitPassword;
    @BindView(R.id.ll_commit_code)
    LinearLayout llCommitCode;
    @BindView(R.id.iv_real_name)
    ImageView ivRealName;
    @BindView(R.id.etRealName)
    EditText etRealName;
    @BindView(R.id.ll_real_name)
    LinearLayout llRealName;
    @BindView(R.id.iv_phone_number)
    ImageView ivPhoneNumber;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;
    @BindView(R.id.ll_phone_number)
    LinearLayout llPhoneNumber;
    @BindView(R.id.iv_sms_code)
    ImageView ivSmsCode;
    @BindView(R.id.tv_send_sms)
    TextView tvSendSms;
    @BindView(R.id.et_sms)
    EditText etSms;
    @BindView(R.id.ll_sms_code)
    LinearLayout llSmsCode;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    @BindView(R.id.etQQ)
    EditText etQQ;
    @BindView(R.id.ll_qq)
    LinearLayout llQq;
    @BindView(R.id.iv_email)
    ImageView ivEmail;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.iv_ss_code)
    ImageView ivSsCode;
    @BindView(R.id.etSSCode)
    EditText etSSCode;
    @BindView(R.id.ll_ss_code)
    LinearLayout llSsCode;
    @BindView(R.id.iv_commit_ss)
    ImageView ivCommitSs;
    @BindView(R.id.etSSCommit)
    EditText etSSCommit;
    @BindView(R.id.ll_commit_ss_code)
    LinearLayout llCommitSsCode;
    @BindView(R.id.iv_time)
    ImageView ivTime;
    @BindView(R.id.nice_spinner_time)
    NiceSpinner niceSpinnerTime;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.iv_birthday)
    ImageView ivBirthday;
    @BindView(R.id.tv_birth_data)
    TextView tvShowBirthday;
    @BindView(R.id.ll_select_birthday)
    RelativeLayout llSelectBirthday;
    @BindView(R.id.ll_birthday)
    LinearLayout llBirthday;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.nice_spinner_sex)
    NiceSpinner niceSpinnerSex;
    @BindView(R.id.ll_sex)
    LinearLayout llSex;
    @BindView(R.id.iv_money)
    ImageView ivMoney;
    @BindView(R.id.nice_spinner_money)
    NiceSpinner niceSpinnerMoney;
    @BindView(R.id.ll_money)
    LinearLayout llMoney;
    @BindView(R.id.iv_lauange)
    ImageView ivLauange;
    @BindView(R.id.ll_launage)
    LinearLayout llLaunage;
    @BindView(R.id.iv_question)
    ImageView ivQuestion;
    @BindView(R.id.nice_spinner_question)
    NiceSpinner niceSpinnerQuestion;
    @BindView(R.id.ll_question)
    LinearLayout llQuestion;
    @BindView(R.id.iv_input_question)
    ImageView ivInputQuestion;
    @BindView(R.id.etAnswer)
    EditText etAnswer;
    @BindView(R.id.ll_input_question)
    LinearLayout llInputQuestion;
    @BindView(R.id.iv_weChat)
    ImageView ivWeChat;
    @BindView(R.id.etWeChatNumber)
    EditText etWeChatNumber;
    @BindView(R.id.ll_wechat)
    LinearLayout llWechat;
    @BindView(R.id.iv_input_captcha)
    ImageView ivInputCaptcha;
    @BindView(R.id.ivCaptcha)
    ImageView ivCaptcha;
    @BindView(R.id.etCaptcha)
    EditText etCaptcha;
    @BindView(R.id.ll)
    RelativeLayout ll;
    @BindView(R.id.ll_captcha)
    LinearLayout llCaptcha;
    @BindView(R.id.iv_allow_choose)
    ImageView ivShouldAllow;
    @BindView(R.id.tv_commit_messaa)
    TextView tvCommitMessaa;
    @BindView(R.id.rl_register_allow)
    RelativeLayout rlRegisterAllow;
    @BindView(R.id.ll_)
    LinearLayout mll;
    @BindView(R.id.btn_Register)
    Button btnRegister;
    @BindView(R.id.nice_spinner_lanuage)
    NiceSpinner niceSpinnerLanuage;
    @BindView(R.id.iv_see_code)
    ImageView ivSeeCode;
    @BindView(R.id.iv_see_commit_code)
    ImageView ivSeeCommitCode;
    Unbinder unbinder;

    private RegisterPresenter<IRegisterView> iRegisterViewRegisterPresenter;
    private String birthDay = "";
    private String sexV = "";
    private String moneyV = "";
    private String lauageV = "";
    private String timeLocal = "";
    //后台说他没法做判空，那我来做吧。
    private boolean isVCNeed;//验证码
    private boolean isRegNeed;//邀请码
    private boolean isＷeChatNeed;//微信号
    private boolean isPhoneNeed;//手机号
    private boolean isEamilNeed;//邮箱
    private boolean isQQNeed;//QQ
    private boolean isRNNeed;//真实姓名
    private boolean isＢirthDayNeed;//出生时间
    private boolean isSSCodeNeed;//支付密码
    private boolean isQuestion;
    private boolean isSmsPhone;//是否开启短信验证码
    private boolean isSmsEmail;//是否开启邮箱验证码
    private boolean isRegisCode;
    private boolean isAllow = true;
    private DatePickerDialog datePickerDialog;
    private String requireJson;
    private String questionValue;
    private boolean isSeeCode;
    private boolean isSeeCommitCode;
    private Handler mHandler;
    private LoadingDialogFragment pd;
    private Animation mAnimation;


    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        RegisterFragment testFragment = new RegisterFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", "注册");
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qt_register_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        loadData();
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void loadData() {
        mHandler = new Handler();
        pd = new LoadingDialogFragment();
        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.qt_btn_single_narrow);
        iRegisterViewRegisterPresenter = new RegisterPresenter(getActivity(), this);
        showEasyProgress();
        iRegisterViewRegisterPresenter.getRegisterInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //EasyProgressDialog.with(getContext()).destory();
        if (mAnimation!=null){
            mAnimation.cancel();
            mAnimation = null;
        }
        iRegisterViewRegisterPresenter.onDestory();
        unbinder.unbind();
    }


    @OnClick({R.id.tv_send_sms, R.id.ll_select_birthday, R.id.iv_input_captcha, R.id.iv_allow_choose, R.id.tv_commit_messaa, R.id.btn_Register, R.id.iv_see_code, R.id.iv_see_commit_code, R.id.ivCaptcha})
    public void onViewClicked(View view) {
        if (SystemUtil.isFastClick()) return;
        if (view.getId() != R.id.img_close) {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.BTN);
        } else {
            SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        }

        switch (view.getId()) {
            case R.id.tv_send_sms:
                sendSmsPhone();
                break;
            case R.id.ll_select_birthday:
                timePick();
                break;
            case R.id.iv_input_captcha:
                iRegisterViewRegisterPresenter.getCaptcha(ivCaptcha);
                break;
            case R.id.iv_allow_choose:
                changeAllow();
                break;
            case R.id.tv_commit_messaa:
                goAllowPage();
                break;
            case R.id.btn_Register:
                if (mAnimation!=null){
                    btnRegister.startAnimation(mAnimation);
                }
                willRegister();
                break;
            case R.id.iv_see_code:
                changePWState();
                break;
            case R.id.iv_see_commit_code:
                changeCommitPWState();
                break;
            case R.id.ivCaptcha:
                iRegisterViewRegisterPresenter.getCaptcha(ivCaptcha);
                break;
        }
    }


    @Override
    public void getRegisterInfoSuccess(RegisterInfoBean registerInfoBean) {
        // EasyProgressDialog.with(getContext()).dismissProgress();
        closeProgress();
        LogUtils.e("register getRegisterInfoResponse:" + registerInfoBean);
        if (registerInfoBean == null) {
            LogUtils.e("register registerInfoBean:" + registerInfoBean);
            return;
        }
        if (registerInfoBean.getData() == null) {
            LogUtils.e("register registerInfoBean.getData():" + registerInfoBean.getData());
            return;
        }

        iRegisterViewRegisterPresenter.getCaptcha(ivCaptcha);
        rlRegisterAllow.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        //获取的数据填充，判断
        //需要展示的界面
        isRegisCode = registerInfoBean.getData().isIsRequiredForRegisterCode();
        requireJson = iRegisterViewRegisterPresenter.gerRequireJson(registerInfoBean.getData().getRequiredJson());
        List<RegisterInfoBean.DataBean.FieldBean> field = registerInfoBean.getData().getField();
        if (registerInfoBean.getData().getField().size() == 0) {
            return;
        }
        for (int i = 0; i < field.size(); i++) {
            RegisterInfoBean.DataBean.FieldBean fieldBean = field.get(i);
            if (fieldBean == null) {
                return;
            }
            boolean isRequited = "0".equals(fieldBean.getIsRequired()) || "1".equals(fieldBean.getIsRequired());
            boolean isFired = "0".equals(fieldBean.getIsRegField()) || "1".equals(fieldBean.getIsRegField());
            String name = fieldBean.getName();
            if ("username".equals(name)) {
                llUsername.setVisibility(View.VISIBLE);
                if (isRequited) {
                    //默认必填
                    ivUsername.setVisibility(View.VISIBLE);
                }
            }
            if ("password".equals(name)) {
                //密码显示
                llCode.setVisibility(View.VISIBLE);
                llCommitCode.setVisibility(View.VISIBLE);
                if (isRequited) {
                    //默认必填
                    ivPassword.setVisibility(View.VISIBLE);
                    ivCommitPassword.setVisibility(View.VISIBLE);
                }
            }
            if ("verificationCode".equals(name)) {
                isVCNeed = true;
                //验证码
                llCaptcha.setVisibility(View.VISIBLE);
                if (isRequited) {
                    ivInputCaptcha.setVisibility(View.VISIBLE);
                }
            }
            if ("regCode".equals(name)) {
                //邀请码
                llInventCode.setVisibility(View.VISIBLE);
                if (isRequited) {
                    isRegNeed = true;
                    ivInventCode.setVisibility(View.VISIBLE);
                }
            }
            if ("304".equals(name)) {
                //微信
                llWechat.setVisibility(View.VISIBLE);
                if (isRequited) {
                    isＷeChatNeed = true;
                    ivWeChat.setVisibility(View.VISIBLE);
                }
            }
            if ("110".equals(name)) {

                llPhoneNumber.setVisibility(View.VISIBLE);
                if (isRequited) {
                    isPhoneNeed = true;
                    //手机号
                    ivPhoneNumber.setVisibility(View.VISIBLE);
                }
                if (registerInfoBean.getData().isIsPhone()) {
                    //开启短信验证
                    isSmsPhone = true;
                    llSmsCode.setVisibility(View.VISIBLE);
                }
            }
            if ("201".equals(name)) {
                llEmail.setVisibility(View.VISIBLE);
                if (isRequited) {
                    //邮箱
                    isEamilNeed = true;
                    ivEmail.setVisibility(View.VISIBLE);
                }
            }
            if ("realName".equals(name)) {
                //真实姓名
                llRealName.setVisibility(View.VISIBLE);
                if (isRequited) {
                    isRNNeed = true;
                    ivRealName.setVisibility(View.VISIBLE);
                }
            }
            if ("301".equals(name)) {
                //qq
                llQq.setVisibility(View.VISIBLE);
                if (isRequited) {
                    isQQNeed = true;
                    ivQq.setVisibility(View.VISIBLE);
                }
            }
            if ("paymentPassword".equals(name)) {
                //支付密码
                llSsCode.setVisibility(View.VISIBLE);
                llCommitSsCode.setVisibility(View.VISIBLE);
                if (isRequited) {
                    isSSCodeNeed = true;
                    ivSsCode.setVisibility(View.VISIBLE);
                    ivCommitSs.setVisibility(View.VISIBLE);
                }
            }
            if ("defaultTimezone".equals(name)) {
                //默认时区
                llTime.setVisibility(View.VISIBLE);
                if (isRequited) {
                    ivTime.setVisibility(View.VISIBLE);
                }
                List<String> times = new ArrayList<>();
                times.add(registerInfoBean.getData().getParams().getTimezone());
                if (times.size() != 0) {
                    niceSpinnerTime.attachDataSource(times);
                    timeLocal = times.get(0);
                }
            }
            if ("birthday".equals(name)) {
                //出生年月
                llBirthday.setVisibility(View.VISIBLE);
                if (isRequited) {
                    isＢirthDayNeed = true;
                    ivBirthday.setVisibility(View.VISIBLE);
                }
            }
            if ("sex".equals(name)) {
                //性别
                llSex.setVisibility(View.VISIBLE);
                if (isRequited) {
                    ivSex.setVisibility(View.VISIBLE);
                }
                List<String> sexKey = new ArrayList<>();
                List<String> sexValue = new ArrayList<>();
                List<RegisterInfoBean.DataBean.SelectOptionBean.SexBean> sex = registerInfoBean.getData().getSelectOption().getSex();
                for (int j = 0; j < sex.size(); j++) {
                    sexKey.add(sex.get(j).getText());
                    sexValue.add(sex.get(j).getValue());
                }
                if (sexValue.size() > 0) {
                    sexV = sexValue.get(0);
                    niceSpinnerSex.attachDataSource(sexKey);
                }
                niceSpinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sexV = sexValue.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        sexV = sexValue.get(0);
                    }
                });
            }
            if ("mainCurrency".equals(name)) {
                //主流币种
                llMoney.setVisibility(View.VISIBLE);
                if (isRequited) {
                    ivMoney.setVisibility(View.VISIBLE);
                }
                List<String> moneyKeys = new ArrayList<>();
                List<String> moneyValues = new ArrayList<>();
                if (!isFired && !TextUtils.isEmpty(registerInfoBean.getData().getParams().getCurrency())) {
                    String currency = registerInfoBean.getData().getParams().getCurrency();
                    moneyKeys.add(currency);
                    moneyV = moneyKeys.get(0);
                } else {
                    List<RegisterInfoBean.DataBean.SelectOptionBean.MainCurrencyBean> mainCurrency = registerInfoBean.getData().getSelectOption().getMainCurrency();
                    if (mainCurrency.size() > 0) {
                        for (int j = 0; j < mainCurrency.size(); j++) {
                            moneyKeys.add(mainCurrency.get(j).getText());
                            moneyValues.add(mainCurrency.get(j).getValue());
                        }
                    }
                }
                if (moneyValues.size() > 0) {
                    moneyV = moneyValues.get(0);
                    niceSpinnerMoney.attachDataSource(moneyKeys);
                }
                niceSpinnerMoney.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        moneyV = moneyValues.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        moneyV = moneyValues.get(0);
                    }
                });
            }
            if ("defaultLocale".equals(name)) {
                //默认语言
                llLaunage.setVisibility(View.VISIBLE);
                if (isRequited) {
                    ivLauange.setVisibility(View.VISIBLE);
                }
                List<RegisterInfoBean.DataBean.SelectOptionBean.DefaultLocaleBean> defaultLocale = registerInfoBean.getData().getSelectOption().getDefaultLocale();
                List<String> lauangeKeys = new ArrayList<>();
                List<String> lauangeValues = new ArrayList<>();
                if (defaultLocale.size() > 0) {
                    for (int k = 0; k < defaultLocale.size(); k++) {
                        lauangeKeys.add(defaultLocale.get(k).getText());
                        lauangeValues.add(defaultLocale.get(k).getValue());
                    }
                }
                if (lauangeValues.size() > 0) {
                    lauageV = lauangeValues.get(0);
                    niceSpinnerLanuage.attachDataSource(lauangeKeys);
                }
                niceSpinnerQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        lauageV = lauangeValues.get(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        lauageV = lauangeValues.get(0);
                    }
                });
            }
            if ("securityIssues".equals(name)) {
                //问卷
                llQuestion.setVisibility(View.VISIBLE);
                llInputQuestion.setVisibility(View.VISIBLE);
                if (isRequited) {
                    isQuestion = true;
                    ivQuestion.setVisibility(View.VISIBLE);
                    ivInputQuestion.setVisibility(View.VISIBLE);
                }
                List<RegisterInfoBean.DataBean.SelectOptionBean.SecurityIssuesBean> securityIssues = registerInfoBean.getData().getSelectOption().getSecurityIssues();
                List<String> questionKeys = new ArrayList<>();
                List<String> questionValues = new ArrayList<>();
                if (securityIssues.size() > 0) {
                    for (int j = 0; j < securityIssues.size(); j++) {
                        questionKeys.add(securityIssues.get(j).getText());
                        questionValues.add(securityIssues.get(j).getValue());
                    }
                    niceSpinnerQuestion.attachDataSource(questionKeys);
                }
                niceSpinnerQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        questionValue = questionValues.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        questionValues.get(0);
                    }
                });
            }
        }


    }

    @Override
    public void registerSuccess(RegisterBean registerBean) {
        // EasyProgressDialog.with(getContext()).dismissProgress();
        closeProgress();
        ToastUtil.showToastShort(getContext(), registerBean.getMessage());
        //返回自动登录
        DataCenter.getInstance().getUser().setUsername(etUsername.getText().toString().trim());
        DataCenter.getInstance().getUser().setPassword(etCommitPassword.getText().toString().trim());
        SPTool.put(getContext(), ConstantValue.KEY_REMEMBER_PWD, true);
        SPTool.put(getContext(), ConstantValue.KEY_USERNAME, etUsername.getText().toString().trim());
        SPTool.put(getContext(), ConstantValue.KEY_PASSWORD, etCommitPassword.getText().toString().trim());
        notifyLogined();
        //setResult(ConstantValue.KEY_REGIST_BACK_LOGIN);
        // finish();
    }


    @Override
    public void registerError(String error) {
        // EasyProgressDialog.with(getContext()).dismissProgress();
        closeProgress();
        ToastUtil.showToastShort(getContext(), "" + error);
        //iRegisterViewRegisterPresenter.getCaptcha(ivCaptcha);

    }

    @Override
    public void dissmissDialog() {
        closeProgress();
    }

    //时间选择
    private void timePick() {
        Calendar calendar = Calendar.getInstance();
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog(
                    getContext(), (arg0, year, monthOfYear, dayOfMonth) -> {
                int currentMouth = monthOfYear + 1;
                String mouth;
                String day;
                if (currentMouth < 10) {
                    mouth = "0" + currentMouth;
                } else {
                    mouth = currentMouth + "";
                }
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = dayOfMonth + "";
                }
                birthDay = year + "-" + mouth + "-" + day;
                tvShowBirthday.setText(birthDay);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }

        datePickerDialog.show();
    }


    private void changeAllow() {
        if (isAllow) {
            ivShouldAllow.setImageDrawable(getResources().getDrawable(R.mipmap.unselect));
            isAllow = false;
        } else {
            ivShouldAllow.setImageDrawable(getResources().getDrawable(R.mipmap.select));
            isAllow = true;
        }
    }

    void notifyLogined() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RxBus.get().post(ConstantValue.QT_EVEBT_REHIST_SECCESS, "login");
            }
        }, 500);
    }


    private void changeCommitPWState() {
        if (isSeeCommitCode) {
            ivSeeCommitCode.setImageDrawable(getResources().getDrawable(R.mipmap.eyeclose));
            etCommitPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isSeeCommitCode = false;
        } else {
            ivSeeCommitCode.setImageDrawable(getResources().getDrawable(R.mipmap.eye));
            etCommitPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isSeeCommitCode = true;
        }
        etCommitPassword.setSelection(etCommitPassword.getText().length());
    }

    private void changePWState() {
        if (isSeeCode) {
            ivSeeCode.setImageDrawable(getResources().getDrawable(R.mipmap.eyeclose));
            etFirstPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isSeeCode = false;
        } else {
            ivSeeCode.setImageDrawable(getResources().getDrawable(R.mipmap.eye));
            etFirstPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isSeeCode = true;
        }
        etFirstPassword.setSelection(etFirstPassword.getText().length());
    }

    private void sendSmsPhone() {
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        if ("".equals(phoneNumber) || !RexUtils.isMobile(phoneNumber)) {
            ToastUtil.showToastLong(getContext(), getString(R.string.register_phone_error));
        } else {
            //  EasyProgressDialog.with(getContext()).showProgress();
            showEasyProgress();
            iRegisterViewRegisterPresenter.getSms(tvSendSms, phoneNumber);
        }
    }

    //注册条款
    private void goAllowPage() {
        //startActivity(new Intent(getContext(), RegisterTermsActivity.class));
        RegisterTermsDialogFragment registerTermsDialogFragment = new RegisterTermsDialogFragment();
        DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), registerTermsDialogFragment);
    }


    private void willRegister() {
        String userName = etUsername.getText().toString().trim();
        String firstPSW = etFirstPassword.getText().toString().trim();
        String commitPSW = etCommitPassword.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String answer = etAnswer.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String qq = etQQ.getText().toString().trim();
        String realName = etRealName.getText().toString().trim();
        String ssCode = etSSCode.getText().toString().trim();
        String commitSSCode = etSSCommit.getText().toString().trim();
        String weChatNumber = etWeChatNumber.getText().toString().trim();
        String inventCode = etYaoQinMa.getText().toString().trim();
        String captcha = etCaptcha.getText().toString().trim();
        String smsCode = etSms.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_username_is_null));
            return;
        }
        if (TextUtils.isEmpty(firstPSW)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_password_is_null));
            return;
        }
        if (isRegNeed && "".equals(inventCode)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_invent_code_is_null));
            return;
        }
        if (isVCNeed && "".equals(captcha)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_vc_code_is_null));
            return;
        }
        if (isPhoneNeed && "".equals(phoneNumber)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_phone_is_null));
            return;
        }
        if (isSmsPhone && TextUtils.isEmpty(smsCode)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_sms_code_is_null));
            return;
        }
        if (isEamilNeed && "".equals(email)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_email_is_null));
            return;
        }
        if (isRNNeed && "".equals(realName)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_real_name_is_null));
            //真实姓名
            return;
        }
        if (isＷeChatNeed && "".equals(weChatNumber)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_wechat_is_null));
            return;
        }
        if (isQQNeed && "".equals(qq)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_qq_is_null));
            return;
        }
        if (isＢirthDayNeed && "".equals(birthDay)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_birthday_is_null));
            return;
        }
        if (isSSCodeNeed && "".equals(ssCode)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_number_code_is_null));
            //支付密码
            return;
        }
        if (isQuestion && "".equals(answer)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.register_answer_is_null));
            return;
        }
        if (!isAllow) {
            ToastUtil.showToastShort(getContext(), getString(R.string.please_allow_register));
            return;
        }
        if (!firstPSW.equals(commitPSW)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.code_different));
            return;
        }
        if (!ssCode.equals(commitSSCode)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.securie_code_different));
            return;
        }
        if (!TextUtils.isEmpty(inventCode) && llInventCode.getVisibility() == View.VISIBLE && !RexUtils.isRecCode(inventCode)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.bad_invent_code));
            return;
        }
        if (!TextUtils.isEmpty(ssCode) && llSsCode.getVisibility() == View.VISIBLE && !RexUtils.isSSCode(ssCode)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.bad_sscode));
            return;
        }
        if (!RexUtils.isAccount(userName)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.bad_account));
            return;
        }
        if (!RexUtils.isLoginPWD(firstPSW)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.bad_account_code));
            return;
        }
        if (!TextUtils.isEmpty(email) && llEmail.getVisibility() == View.VISIBLE && !RexUtils.isEmail(email)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.bad_email_code));
            return;
        }
        if (!TextUtils.isEmpty(qq) && llQq.getVisibility() == View.VISIBLE && !RexUtils.isQQ(qq)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.bad_qq_code));
            return;
        }
        if (!TextUtils.isEmpty(weChatNumber) && llWechat.getVisibility() == View.VISIBLE && !RexUtils.isWeChat(weChatNumber) && !RexUtils.isQQ(weChatNumber) && !RexUtils.isMobile(weChatNumber)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.bad_wechat));
            return;
        }
        if (!TextUtils.isEmpty(realName) && llRealName.getVisibility() == View.VISIBLE && !RexUtils.isRealName(realName)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.bad_realname));
            return;
        }

        if (!TextUtils.isEmpty(phoneNumber) && llPhoneNumber.getVisibility() == View.VISIBLE && !RexUtils.isMobile(phoneNumber)) {
            ToastUtil.showToastShort(getContext(), getString(R.string.phone_decode_error));
            return;
        }

        String term = isAllow ? "11" : "22";
        //EasyProgressDialog.with(getContext()).showProgress();
        showEasyProgress();
        String checkPhone = isSmsPhone ? "checkPhone" : "";
        iRegisterViewRegisterPresenter.register(iRegisterViewRegisterPresenter.getSimpleFiled(birthDay, sexV, ssCode, timeLocal,
                lauageV, phoneNumber, realName, moneyV, answer, email, qq, weChatNumber, userName,
                firstPSW, commitPSW, captcha, smsCode, checkPhone, inventCode, term, String.valueOf(isRegisCode), requireJson));

    }

    //显示进度框
    private void showEasyProgress() {
        if (getContext() != null) {
            if (pd == null) {
                pd = new LoadingDialogFragment();
            }
            DialogFramentManager.getInstance().showDialog(getChildFragmentManager(), pd);
            //EasyProgressDialog.with(getContext()).showProgress();
        }
    }

    //关闭进度框
    private void closeProgress() {
        if (getContext() != null && pd != null) {
            pd.dismiss();
            pd = null;
            //EasyProgressDialog.with(getContext()).dismissProgress();
        }
    }
}