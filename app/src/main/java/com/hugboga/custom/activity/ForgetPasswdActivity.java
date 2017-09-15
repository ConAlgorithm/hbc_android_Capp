package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestForgetPwd;
import com.hugboga.custom.data.request.RequestVerity;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by on 16/8/6.
 */
public class ForgetPasswdActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.forget_passwd_areacode)
    TextView areaCodeTextView;
    @Bind(R.id.forget_passwd_phone)
    EditText phoneEditText;
    @Bind(R.id.forget_passwd_verity)
    EditText verityEditText;
    @Bind(R.id.forget_passwd_newpass_layout)
    EditText passwordEditText;
    @Bind(R.id.forget_passwd_getcode)
    TextView getCodeBtn; //发送验证码按钮
    @Bind(R.id.forget_passwd_time)
    TextView timeTextView; //验证码倒计时
    @Bind(R.id.forget_passwd_submit)
    Button forget_passwd_submit; //验证码倒计时
    @Bind(R.id.iv_pwd_visible)
    ImageView passwordVisible;
    @Bind(R.id.delete)
            ImageView delete;
    boolean isPwdVisibility = false;
    @Override
    public int getContentViewId() {
        return R.layout.fg_forget_passwd_new;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
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

    private void initView() {
        initDefaultTitleBar();
        fgTitle.setText(R.string.login_pwd_reset);

        Bundle bundle = getIntent().getExtras();
        String keyFrom = null;
        if (bundle != null) {
            keyFrom = bundle.getString(KEY_FROM);
        }

        //初始化数据
        if ("login".equals(keyFrom) && bundle != null) {
            String code = bundle.getString("areaCode");
            if(code!=null && !code.isEmpty()){
                areaCodeTextView.setText("+" + code);
            }
            String phone = bundle.getString("phone");
            if(phone!=null && !phone.isEmpty()){
                phoneEditText.setText(phone);
            }
        } else {
            String code = UserEntity.getUser().getAreaCode(this);
            if(code!=null && !code.isEmpty()){
                areaCodeTextView.setText("+" + code);
            }
            String phone = UserEntity.getUser().getPhone(this);
            if(phone!=null && !phone.isEmpty()){
                phoneEditText.setText(phone);
            }
        }
        if(phoneEditText!=null){
            if(phoneEditText.getText().toString().length() >0){
                delete.setVisibility(View.VISIBLE);
                getCodeBtn.setTextColor(getResources().getColor(R.color.forget_pwd));
            }else{
                delete.setVisibility(View.GONE);
                getCodeBtn.setTextColor(getResources().getColor(R.color.common_font_color_gray));
            }
        }
        phoneEditText.addTextChangedListener(this);
        passwordEditText.addTextChangedListener(this);
        verityEditText.addTextChangedListener(this);

        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                passwordVisible.setVisibility(View.VISIBLE);
                return false;
            }
        });
        verityEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                passwordVisible.setVisibility(View.GONE);
                return false;
            }
        });
        phoneEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                passwordVisible.setVisibility(View.GONE);
                return false;
            }
        });
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
    protected void onStop() {
        super.onStop();
        hideInputMethod(areaCodeTextView);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(phoneEditText.hasFocus()){
            phoneEditText.setSelection(phoneEditText.getText().toString().length());
        }
        if(passwordEditText.hasFocus()){
            passwordEditText.setSelection(passwordEditText.getText().toString().length());
        }
        if(verityEditText.hasFocus()){
            verityEditText.setSelection(verityEditText.getText().toString().length());
        }

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String verity_EditText = verityEditText.getText().toString();
        if(phone.length() >0){
            delete.setVisibility(View.VISIBLE);
            getCodeBtn.setTextColor(getResources().getColor(R.color.forget_pwd));
        }else{
            delete.setVisibility(View.GONE);
            getCodeBtn.setTextColor(getResources().getColor(R.color.common_font_color_gray));
        }
        if (!TextUtils.isEmpty(phone)
                &&!TextUtils.isEmpty(password)  &&!TextUtils.isEmpty(verity_EditText)
                && Pattern.matches("[\\w]{4,16}", password)) {
            forget_passwd_submit.setEnabled(true);
            //forget_passwd_submit.setBackgroundColor(getResources().getColor(R.color.login_ready));
        }else{
            forget_passwd_submit.setEnabled(false);
            //forget_passwd_submit.setBackgroundColor(getResources().getColor(R.color.login_unready));
        }

        if (!isPwdVisibility) {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordVisible.setImageResource(R.mipmap.login_invisible);
        } else {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            passwordVisible.setImageResource(R.mipmap.login_visible);
        }

        if(phoneEditText.hasFocus()){
            phoneEditText.setSelection(phoneEditText.getText().toString().length());
        }
        if(passwordEditText.hasFocus()){
            passwordEditText.setSelection(passwordEditText.getText().toString().length());
        }
        if(verityEditText.hasFocus()){
            verityEditText.setSelection(verityEditText.getText().toString().length());
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestForgetPwd) {
            RequestForgetPwd mParser = (RequestForgetPwd) request;
            showTip(CommonUtils.getString(R.string.login_pwd_reset_sucess));
            UserEntity.getUser().setWeakPassword(this,false);
            handler.removeCallbacks(runnable);
            finish();
        }else if(request instanceof RequestVerity){
            RequestVerity parserVerity = (RequestVerity) request;
            showTip(CommonUtils.getString(R.string.login_already_verity));
            time = 59;
            handler.postDelayed(runnable, 0);
        }
    }

    Integer time = 59;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time > 0) {
                setBtnVisible(false);
                timeTextView.setText("("+ String.valueOf(time--) + "s)");
                handler.postDelayed(this, 1000);
            } else {
                setBtnVisible(true);
                timeTextView.setText("("+String.valueOf(59) + "s)");
            }

        }
    };

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if(request instanceof RequestVerity){
            setBtnVisible(true);
        }
        super.onDataRequestError(errorInfo, request);
    }

    @OnClick({R.id.forget_passwd_submit, R.id.forget_passwd_getcode, R.id.forget_passwd_areacode,R.id.iv_pwd_visible,R.id.delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_passwd_submit:
                //重置密码提交
                collapseSoftInputMethod(phoneEditText); //隐藏键盘
                collapseSoftInputMethod(verityEditText);
                collapseSoftInputMethod(passwordEditText);
                String areaCode = areaCodeTextView.getText().toString();
                if(TextUtils.isEmpty(areaCode)){
                    showTip(R.string.login_check_areacode);
                    return;
                }
                areaCode = areaCode.substring(1);
                String phone = phoneEditText.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    showTip(R.string.login_check_phone_empty);
                    return;
                }
                String verity = verityEditText.getText().toString();
                if(TextUtils.isEmpty(verity)){
                    showTip(R.string.login_check_verity);
                    return;
                }
                String password = passwordEditText.getText().toString();
                if(TextUtils.isEmpty(password)){
                    showTip(R.string.login_check_pwd);
                    return;
                }
                if(!Pattern.matches("[\\w]{6,16}", password)){
                    showTip(R.string.login_check_pwd_length);
                    return;
                }
                if(areaCode.equals("86")){
                    if(!phone.startsWith("1") || phone.length() != 11){
                        CommonUtils.showToast(R.string.login_check_phone_length);
                        return;
                    }
                }
                RequestForgetPwd requestForgetPwd = new RequestForgetPwd(this, areaCode,phone,password, verity);
                requestData(requestForgetPwd);
                MobClickUtils.onEvent(StatisticConstant.PASSWORD_RESET);
                break;
            case R.id.forget_passwd_getcode:
                //获取验证码
                setBtnVisible(false);
                collapseSoftInputMethod(phoneEditText); //隐藏键盘
                collapseSoftInputMethod(verityEditText);
                collapseSoftInputMethod(passwordEditText);
                String areaCode1 = areaCodeTextView.getText().toString();
                if(TextUtils.isEmpty(areaCode1)){
                    showTip(R.string.login_check_areacode);
                    setBtnVisible(true);
                    return;
                }
                areaCode1 = areaCode1.substring(1);
                String phone1 = phoneEditText.getText().toString();
                if(TextUtils.isEmpty(phone1)){
                    showTip(R.string.login_check_phone_empty);
                    setBtnVisible(true);
                    return;
                }

                RequestVerity requestVerity = new RequestVerity(this,areaCode1,phone1,2);
                requestData(requestVerity);
                break;
            case R.id.forget_passwd_areacode:
                //选择地区
                collapseSoftInputMethod(phoneEditText); //隐藏键盘
                collapseSoftInputMethod(verityEditText);
                collapseSoftInputMethod(passwordEditText);

                Intent intent = new Intent(ForgetPasswdActivity.this, ChooseCountryActivity.class);
                intent.putExtra(KEY_FROM, "forgetPasswd");
                startActivity(intent);
                break;
            case R.id.iv_pwd_visible:
                if (passwordEditText != null) {
                    if (isPwdVisibility) {//密码可见
                        isPwdVisibility = false;
                        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible.setImageResource(R.mipmap.login_invisible);
                    } else {//密码不可见
                        isPwdVisibility = true;
                        passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible.setImageResource(R.mipmap.login_visible);
                    }
                }
                break;
            case R.id.delete:
                phoneEditText.setText("");
                break;
            default:
                break;
        }
    }

    /**
     * 设置按钮是否可以点击
     * @param isClick
     */
    private void setBtnVisible(boolean isClick){
        if(isClick){
            getCodeBtn.setVisibility(View.VISIBLE);
            timeTextView.setVisibility(View.GONE);
        }else{
            getCodeBtn.setVisibility(View.GONE);
            timeTextView.setVisibility(View.VISIBLE);
        }
    }

    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    private void showTip(String tips) {
        CommonUtils.showToast(tips);
    }

    private void showTip(int tips) {
        CommonUtils.showToast(tips);
    }

}
