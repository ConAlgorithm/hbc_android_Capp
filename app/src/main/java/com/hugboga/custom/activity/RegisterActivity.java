package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionErrorCode;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.ServerException;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestLogin;
import com.hugboga.custom.data.request.RequestRegister;
import com.hugboga.custom.data.request.RequestVerity;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 16/8/6.
 */
public class RegisterActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.register_areacode)
    TextView areaCodeTextView;
    @Bind(R.id.register_phone)
    EditText phoneEditText;
    @Bind(R.id.register_verity)
    EditText verityEditText;
    @Bind(R.id.register_password)
    EditText passwordEditText;
    @Bind(R.id.register_getcode)
    TextView getCodeBtn; //发送验证码按钮
    @Bind(R.id.register_time)
    TextView timeTextView; //验证码倒计时

    @Bind(R.id.left_protocol)
    TextView leftProtocol; //协议

    private String source = "";

    @Bind(R.id.register_submit)
    Button registButton;

    String areaCode;
    String phone;
    String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_register);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initDefaultTitleBar();
        fgTitle.setText("注册");

        OrderUtils.genRegisterAgreeMent(this,leftProtocol);

        Bundle bundle = getIntent().getExtras();
        String keyFrom = null;
        if (bundle != null) {
            keyFrom = bundle.getString(KEY_FROM);
        }
        //初始化数据
        if ("login".equals(keyFrom) && bundle != null) {
            String code = bundle.getString("areaCode");
            if (!TextUtils.isEmpty(code)) {
                areaCodeTextView.setText("+" + code);
            }
            String phone = bundle.getString("phone");
            if (!TextUtils.isEmpty(phone)) {
                phoneEditText.setText(phone);
            }
            source = bundle.getString("source");
        }

        phoneEditText.addTextChangedListener(this);
        verityEditText.addTextChangedListener(this);
        passwordEditText.addTextChangedListener(this);
    }

    @Override
    public String getEventId() {
        return StatisticConstant.REGIST_LAUNCH;
    }

    @Override
    public String getEventSource() {
        return "注册页";
    }

    @Override
    public Map getEventMap() {
        return super.getEventMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyHandler();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideInputMethod(areaCodeTextView);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_COUNTRY_BACK:
                if (!(action.getData() instanceof AreaCodeBean)) {
                    break;
                }
                AreaCodeBean areaCodeBean = (AreaCodeBean) action.getData();
                if (areaCodeBean == null) {
                    break;
                }
                areaCodeTextView.setText("+" + areaCodeBean.getCode());
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestRegister) {
            RequestRegister register = (RequestRegister) request;
            UserBean userBean = register.getData();
            if (userBean != null) {
                StatisticClickEvent.click(StatisticConstant.REGIST_SUCCEED,getIntentSource());
                //注册成功，进行登录操作
                //登录成功
                UserEntity.getUser().setUserId(this, userBean.userID);
                UserEntity.getUser().setUserToken(this, userBean.userToken);
                UserEntity.getUser().setPhone(this, phone); //手机号已经不再返回
                UserEntity.getUser().setAreaCode(this, areaCode);
                UserEntity.getUser().setLoginAreaCode(this, areaCode);
                UserEntity.getUser().setLoginPhone(this, phone);
                UserEntity.getUser().setNickname(this, userBean.nickname);
                UserEntity.getUser().setAvatar(this, userBean.avatar);
                UserEntity.getUser().setOrderPoint(this, 0); //清空IM未读的小红点
                showTip("注册成功");
                Bundle bundle = new Bundle();
                bundle.putBoolean("isLogin", true);
                EventBus.getDefault().post(
                        new EventAction(EventType.CLICK_USER_LOGIN));

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("source", source);
                MobclickAgent.onEvent(this, "regist_succeed", map);
                destroyHandler();
                finish();
            }
        } else if (request instanceof RequestVerity) {
            RequestVerity parserVerity = (RequestVerity) request;
            showTip("验证码已发送");
            setBtnVisible(false);
            time = 59;
            handler.postDelayed(runnable, 0);
        } else if (request instanceof RequestLogin) {
            RequestLogin requestLogin = (RequestLogin) request;
            UserBean userBean = requestLogin.getData();
            if (userBean != null) {
                //登录成功
                UserEntity.getUser().setUserId(this, userBean.userID);
                UserEntity.getUser().setUserToken(this, userBean.userToken);
                UserEntity.getUser().setPhone(this, phone); //手机号已经不再返回
                UserEntity.getUser().setAreaCode(this, areaCode);
                UserEntity.getUser().setLoginAreaCode(this, areaCode);
                UserEntity.getUser().setLoginPhone(this, phone);
                UserEntity.getUser().setNickname(this, userBean.nickname);
                UserEntity.getUser().setAvatar(this, userBean.avatar);
                UserEntity.getUser().setOrderPoint(this, 0); //清空IM未读的小红点
                showTip("登录成功");
                Bundle bundle = new Bundle();
                bundle.putBoolean("isLogin", true);
                EventBus.getDefault().post(
                        new EventAction(EventType.CLICK_USER_LOGIN));

                HashMap<String,String> map = new HashMap<String,String>();
                map.put("source", source);
                MobclickAgent.onEvent(this, "regist_succeed", map);
                destroyHandler();
                finish();
            }
        }
    }

    Integer time = 59;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time > 0) {
                setBtnVisible(false);
                timeTextView.setText(String.valueOf(time--) + "秒");
                handler.postDelayed(this, 1000);
            } else {
                setBtnVisible(true);
                timeTextView.setText(String.valueOf(59) + "秒");
            }

        }
    };

    private void destroyHandler() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            handler = null;
            runnable = null;
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        setBtnVisible(true);
        if (errorInfo.state == ExceptionErrorCode.ERROR_CODE_SERVER) {
            if (errorInfo.exception instanceof ServerException) {
                ServerException se = (ServerException) errorInfo.exception;
                if (se.getCode() == 40070010 || se.getCode() == 10014) {
                    //区号手机号，已经被注册
                    DialogUtil.getInstance(this).showCustomDialog("提醒", "此手机号已经注册，是否直接登录？", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, "登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            destroyHandler();
                            finish();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Bundle bundle = new Bundle();
                            bundle.putString("key_area_code", areaCode);
                            bundle.putString("key_phone", phone);
                            intent.putExtras(bundle);
                            startActivity(intent);
//                            startFragment(new FgLogin(), bundle);
                        }
                    }).show();
                    return;
                }
            }
        }
        super.onDataRequestError(errorInfo, request);
    }


    @OnClick({R.id.register_submit, R.id.register_login, R.id.register_areacode, R.id.register_getcode, R.id.register_protocol})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.register_submit:
                //注册提交
                collapseSoftInputMethod(verityEditText); //隐藏键盘
                collapseSoftInputMethod(passwordEditText);
                collapseSoftInputMethod(phoneEditText);
                areaCode = areaCodeTextView.getText().toString();
                if (TextUtils.isEmpty(areaCode)) {
                    showTip("区号不能为空");
                    return;
                }
                areaCode = areaCode.substring(1);
                phone = phoneEditText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showTip("手机号不能为空");
                    return;
                }
                String verity = verityEditText.getText().toString();
                if (TextUtils.isEmpty(verity)) {
                    showTip("验证码不能为空");
                    return;
                }
                password = passwordEditText.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    showTip("密码不能为空");
                    return;
                }
                if (!Pattern.matches("[\\w]{4,16}", password)) {
                    showTip("密码必须是4-16位数字或字母");
                    return;
                }
                String channelStr = BuildConfig.FLAVOR;
                Integer channelInt = 1000;
                try {
                    channelInt = Integer.valueOf(channelStr);
                } catch (Exception e) {
                    MLog.e("getVersionChannel ", e);
                }
                RequestRegister requestRegister = new RequestRegister(this, areaCode, phone, password, verity, null, channelInt);
                requestData(requestRegister);

                StatisticClickEvent.click(StatisticConstant.CLICK_VERIFTXT,getIntentSource());

                break;
            case R.id.register_login:
                destroyHandler();
                //跳转到登录
                finish();
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.register_areacode:
                handler.removeCallbacks(runnable);
                //选择地区
                collapseSoftInputMethod(verityEditText); //隐藏键盘
                collapseSoftInputMethod(passwordEditText);
                collapseSoftInputMethod(phoneEditText);
                intent = new Intent(RegisterActivity.this, ChooseCountryActivity.class);
                intent.putExtra(KEY_FROM, "register");
                startActivity(intent);
                break;
            case R.id.register_getcode:
                //获取验证码
                getCodeBtn.setClickable(false);
                collapseSoftInputMethod(verityEditText); //隐藏键盘
                collapseSoftInputMethod(passwordEditText);
                collapseSoftInputMethod(phoneEditText);
                areaCode = areaCodeTextView.getText().toString();
                if (TextUtils.isEmpty(areaCode)) {
                    showTip("区号不能为空");
                    setBtnVisible(true);
                    return;
                }
                areaCode = areaCode.substring(1);
                phone = phoneEditText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showTip("手机号不能为空");
                    setBtnVisible(true);
                    return;
                }
                RequestVerity requestVerity = new RequestVerity(this, areaCode, phone, 1);
                requestData(requestVerity);
                StatisticClickEvent.click(StatisticConstant.CLICK_VERIFTXT,"点击获取短信验证码");
                break;
            case R.id.register_protocol:
//                FgWebInfo fgWebInfo = new FgWebInfo();
//                Bundle bundle1 = new Bundle();
//                bundle1.putString(FgWebInfo.WEB_URL, UrlLibs.H5_PROTOCOL);
//                fgWebInfo.setArguments(bundle1);
//                startFragment(fgWebInfo);
                handler.removeCallbacks(runnable);
                setBtnVisible(true);
                intent = new Intent(RegisterActivity.this, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_PROTOCOL);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    /**
     * 设置按钮是否可以点击
     *
     * @param isClick
     */
    private void setBtnVisible(boolean isClick) {
        if (isClick) {
            getCodeBtn.setClickable(true);
            getCodeBtn.setVisibility(View.VISIBLE);
            timeTextView.setVisibility(View.GONE);
        } else {
            getCodeBtn.setVisibility(View.GONE);
            timeTextView.setVisibility(View.VISIBLE);
        }
    }

    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String verityCode = verityEditText.getText().toString().trim();
        String areaCode = areaCodeTextView.getText().toString().trim();
        if (!TextUtils.isEmpty(areaCode)&&!TextUtils.isEmpty(phone)
                &&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(verityCode)
                &&Pattern.matches("[\\w]{4,16}", password)) {
            registButton.setEnabled(true);
            registButton.setBackgroundColor(getResources().getColor(R.color.login_ready));
        }else{
            registButton.setEnabled(false);
            registButton.setBackgroundColor(getResources().getColor(R.color.login_unready));
        }
    }

    private void showTip(String tips) {
        CommonUtils.showToast(tips);
    }
}
