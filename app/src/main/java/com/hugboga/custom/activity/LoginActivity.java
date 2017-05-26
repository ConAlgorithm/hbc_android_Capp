package com.hugboga.custom.activity;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestLogin;
import com.hugboga.custom.data.request.RequestLoginBycaptcha;
import com.hugboga.custom.data.request.RequestLoginCheckOpenId;
import com.hugboga.custom.data.request.RequestVerity;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.SharedPre;
import com.qiyukf.unicorn.api.Unicorn;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by zhangqiang on 17/5/16.
 */

public class LoginActivity extends BaseActivity implements TextWatcher {
    @Bind(R.id.verify)
    TextView verify;
    @Bind(R.id.login_password)
    EditText loginPassword;
    @Bind(R.id.change_mobile_areacode)
    TextView areaCodeTextView;
    @Bind(R.id.login_phone)
    EditText phoneEditText;
    @Bind(R.id.back)
    ImageView headerLeftBtn;
    @Bind(R.id.login_submit)
    Button login_submit;
    @Bind(R.id.miaoshu2)
    TextView miaoshu2;
    @Bind(R.id.delete)
    ImageView delete;



    private SharedPre sharedPre;
    private String source = "";
    String phone;
    String areaCode;
    String captcha;
    private IWXAPI wxapi;
    public static boolean isWXLogin = false;

    public static String KEY_PHONE = "key_phone";
    public static String KEY_AREA_CODE = "key_area_code";

    @Override
    public int getContentViewId() {
        return R.layout.fg_login_new;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView(getIntent());
        //initHeader();
        setSensorsDefaultEvent("登录页", SensorsConstant.LOGIN);
        OrderUtils.genUserAgreeMent(this,miaoshu2);
    }
    protected void initView(Intent intent) {
        String areaCode = null;
        String phone = null;
        if (getIntent() != null) {
            areaCode = intent.getStringExtra(KEY_AREA_CODE);
            phone = intent.getStringExtra(KEY_PHONE);
            source = intent.getStringExtra("source");
        }
        sharedPre = new SharedPre(activity);
        if (TextUtils.isEmpty(areaCode)) {
            areaCode = sharedPre.getStringValue(SharedPre.LOGIN_CODE);
        }
        if (!TextUtils.isEmpty(areaCode)) {
            this.areaCode = areaCode;
            areaCodeTextView.setText("+" + areaCode);
        } else {
            this.areaCode = "86";
        }
        if (TextUtils.isEmpty(phone)) {
            phone = sharedPre.getStringValue(SharedPre.LOGIN_PHONE);
        }
        if (!TextUtils.isEmpty(phone)) {
            this.phone = phone;
            phoneEditText.setText(phone);
        }
        if(phone!= null){
            if(phone.length() >0){
                delete.setVisibility(View.VISIBLE);
            }else{
                delete.setVisibility(View.GONE);
            }
        }
        if(phoneEditText!=null){
            if(phoneEditText.getText().toString().length() >0){
                verify.setTextColor(getResources().getColor(R.color.forget_pwd));
            }else{
                verify.setTextColor(getResources().getColor(R.color.common_font_color_gray));
            }
        }
        phoneEditText.addTextChangedListener(this);
        areaCodeTextView.addTextChangedListener(this);
        loginPassword.addTextChangedListener(this);


        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                finish();
            }
        });
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(phoneEditText.hasFocus()){
            phoneEditText.setSelection(phoneEditText.getText().toString().length());
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String phone = phoneEditText.getText().toString().trim();
        String capthca = loginPassword.getText().toString().trim();

        if(phone.length() >0){
            delete.setVisibility(View.VISIBLE);
            verify.setTextColor(getResources().getColor(R.color.forget_pwd));
        }else{
            delete.setVisibility(View.GONE);
            verify.setTextColor(getResources().getColor(R.color.common_font_color_gray));
        }
        if (!TextUtils.isEmpty(areaCode) && !TextUtils.isEmpty(capthca) && !TextUtils.isEmpty(phone)) {
            login_submit.setEnabled(true);
            //login_submit.setBackgroundColor(getResources().getColor(R.color.login_ready));
        } else {
            login_submit.setEnabled(false);
            //login_submit.setBackgroundColor(getResources().getColor(R.color.login_unready));
        }

        if(phoneEditText.hasFocus()){
            phoneEditText.setSelection(phoneEditText.getText().toString().length());
        }
    }
    public void loginCheckOpenId(String code) {
        RequestLoginCheckOpenId request = new RequestLoginCheckOpenId(activity, code);
        requestData(request);
    }
    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case WECHAT_LOGIN_CODE:
                if (action.getData() != null && action.getData() instanceof SendAuth.Resp) {
                    SendAuth.Resp resp = (SendAuth.Resp) action.getData();
                    if (!TextUtils.isEmpty(resp.code) && !TextUtils.isEmpty(resp.state) && resp.state.equals("hbc")) {
                        loginCheckOpenId(resp.code);
                    }
                }
                break;
            case BIND_MOBILE:
                finish();
                break;
            case CHOOSE_COUNTRY_BACK:
                if (!(action.getData() instanceof AreaCodeBean)) {
                    break;
                }
                AreaCodeBean areaCodeBean = (AreaCodeBean) action.getData();
                if (areaCodeBean == null) {
                    break;
                }
                this.areaCode = areaCodeBean.getCode();
                areaCodeTextView.setText("+" + areaCodeBean.getCode());
                break;
            case CLICK_USER_LOGIN:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            handler = null;
            runnable = null;
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView(intent);
    }
    @Override
    public void onStop() {
        super.onStop();
        hideInputMethod(areaCodeTextView);
        this.activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    Integer time = 59;
    Handler handler = new Handler();
    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestLoginBycaptcha) {
            RequestLoginBycaptcha mRequest = (RequestLoginBycaptcha) request;
            UserBean user = mRequest.getData();
            user.setUserEntity(activity);
            UserEntity.getUser().setAreaCode(activity, mRequest.areaCode);
            UserEntity.getUser().setPhone(activity, mRequest.mobile);
            UserEntity.getUser().setLoginAreaCode(activity, mRequest.areaCode);
            UserEntity.getUser().setLoginPhone(activity, mRequest.mobile);
            UserSession.getUser().setUserToken(activity, user.userToken);
            UserEntity.getUser().setUserName(activity, user.name);
            UserEntity.getUser().setNimUserId(activity, user.nimUserId);
            UserEntity.getUser().setNimUserToken(activity, user.nimToken);
            UserEntity.getUser().setUnionid(LoginActivity.this, "");
            try {
                SensorsDataAPI.sharedInstance(this).login(user.userID);
                //setSensorsUserEvent();
            } catch (InvalidDataException e) {
                e.printStackTrace();
            }

            connectIM();
            Unicorn.setUserInfo(null);
            EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOGIN));

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("source", getIntentSource());
            map.put("loginstyle", "手机号");
            map.put("head", !TextUtils.isEmpty(user.avatar) ? "是" : "否");
            map.put("nickname", !TextUtils.isEmpty(user.nickname) ? "是" : "否");
            map.put("phone", !TextUtils.isEmpty(user.mobile) ? "是" : "否");
            MobClickUtils.onEvent(StatisticConstant.LOGIN_SUCCEED, map);
            CommonUtils.showToast("登录成功");
            /*if (user.mustRestPwd && passwordEditText.getText() != null) {
                final String password = passwordEditText.getText().toString();
                Intent intent = new Intent(this, InitPasswordActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, password);
                startActivity(intent);
            }*/
            finish();
        } else if (request instanceof RequestLoginCheckOpenId) {
            RequestLoginCheckOpenId request1 = (RequestLoginCheckOpenId) request;
            UserBean userBean = request1.getData();
            UserEntity.getUser().setUnionid(LoginActivity.this, userBean.unionid);
            if (userBean.isNotRegister == 1) {//未注册，走注册流程
                Bundle bundle = new Bundle();
                bundle.putString("unionid", userBean.unionid);
                bundle.putString("source", getEventSource());
                Intent intent = new Intent(LoginActivity.this, BindMobileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                MobClickUtils.onEvent(StatisticConstant.WEIXINBIND_LAUNCH);
            } else {//注册了，有用户信息
                userBean.setUserEntity(activity);
                UserSession.getUser().setUserToken(activity, userBean.userToken);
                Unicorn.setUserInfo(null);
                connectIM();
                EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOGIN));

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("bind", !TextUtils.isEmpty(userBean.mobile) ? "是" : "否");
                MobClickUtils.onEvent(StatisticConstant.WEIXINREGISTER_SUCCEED, map);
                CommonUtils.showToast("登录成功");
                finish();
            }
        }else if(request instanceof RequestVerity){
            CommonUtils.showToast("验证码已发送");
            phoneEditText.setText(phone);
            time = 59;
            handler.postDelayed(runnable, 0);
        }
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time > 0) {
                if(verify!= null){
                    verify.setText("   ("+String.valueOf(time--) + "s)");
                    verify.setTextColor(0xffa8a8a8);
                    verify.setEnabled(false);
                }
                handler.postDelayed(this, 1000);
            } else {
                if(verify!= null){
                    verify.setText("获取验证码");
                    verify.setTextColor(0xff24B5FF);
                    verify.setEnabled(true);
                }
            }

        }
    };


    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if(request instanceof RequestVerity){
            verify.setText("获取验证码");
            verify.setTextColor(0xff24B5FF);
            phoneEditText.setText(phone);
        }else if(request instanceof  RequestLoginBycaptcha){
            //CommonUtils.showToast("验证码错误，请重新输入");

        }
        super.onDataRequestError(errorInfo, request);
    }
    @OnClick({R.id.wechat_layout, R.id.login_submit, R.id.change_mobile_areacode,R.id.verify,R.id.back,R.id.delete,R.id.shouji_layout})
    public void onClick(View view) {
        Intent intent = null;
        HashMap<String, String> map = new HashMap<String, String>();
        switch (view.getId()) {
            case R.id.back:
                StatisticClickEvent.click(StatisticConstant.LOGIN_CLOSE,getIntentSource());
                finish();
                break;
            case R.id.wechat_layout:
                if (!WXShareUtils.getInstance(activity).isInstall(false)) {
                    CommonUtils.showToast("手机未安装微信或版本太低");
                    return;
                }
                StatisticClickEvent.click(StatisticConstant.LOGIN_WEIXIN,getIntentSource());
                wxapi = WXAPIFactory.createWXAPI(this.activity, Constants.WX_APP_ID);
                wxapi.registerApp(Constants.WX_APP_ID);
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "hbc";
                wxapi.sendReq(req);
                isWXLogin = true;

                map.put("source", source);
                MobclickAgent.onEvent(activity, "login_weixin", map);

                break;
            case R.id.shouji_layout:
                intent = new Intent(this, AccountPwdLoginActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);
                /*overridePendingTransition(R.anim.enter_from_right,
                        0);*/
                break;
            case R.id.login_submit:
                //登录
                loginGo();
                break;
            case R.id.change_mobile_areacode:
                phoneEditText.clearFocus();
                //选择区号
//                collapseSoftInputMethod(); //隐藏键盘
                intent = new Intent(LoginActivity.this, ChooseCountryActivity.class);
                intent.putExtra(KEY_FROM, "login");
                startActivity(intent);
                break;
            case R.id.verify:
                login_submit.setEnabled(false);
                collapseSoftInputMethod(phoneEditText); //隐藏键盘
                collapseSoftInputMethod(loginPassword);
                String areaCode1 = areaCodeTextView.getText().toString();
                if(TextUtils.isEmpty(areaCode1)){
                    CommonUtils.showToast("区号不能为空");
                    return;
                }
                areaCode1 = areaCode1.substring(1);
                phone = phoneEditText.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    CommonUtils.showToast("请输入手机号");
                    return;
                }
                if(areaCode1.equals("86")){
                    if(!phone.startsWith("1") || phone.length() != 11){
                        CommonUtils.showToast("请输入正确的国内手机号，11位数字");
                        return;
                    }
                }

                RequestVerity requestVerity = new RequestVerity(this,areaCode1,phone,4);
                requestData(requestVerity);
                break;
            case R.id.delete:
                phoneEditText.setText("");
                break;
            default:
                break;
        }
    }

    /**
     * 进行登录
     */
    private void loginGo() {
//        collapseSoftInputMethod(); //隐藏键盘
        MLog.e("areaCode4=" + areaCode);
        if (TextUtils.isEmpty(areaCode)) {
            CommonUtils.showToast("区号不能为空");
            return;
        }
        areaCode = areaCode.replace("+", "");
        phone = phoneEditText.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            CommonUtils.showToast("手机号不能为空");
            return;
        }
        captcha = loginPassword.getText().toString().trim();
        if (TextUtils.isEmpty(captcha)) {
            CommonUtils.showToast("验证码不能为空");
            return;
        }

        RequestLoginBycaptcha request = new RequestLoginBycaptcha(activity, areaCode, phone, captcha,3,1);
        requestData(request);

        StatisticClickEvent.click(StatisticConstant.LOGIN_PHONE,getIntentSource());
    }
    @Override
    public String getEventId() {
        return StatisticConstant.LOGIN_LAUNCH;
    }

    @Override
    public String getEventSource() {
        return "登录页";
    }

    @Override
    public Map getEventMap() {
        return super.getEventMap();
    }

    private void connectIM() {
        IMUtil.getInstance().connect();
    }
    public void onBackPressed() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("source", source);
        MobclickAgent.onEvent(activity, "login_close", map);
        super.onBackPressed();
    }

}
