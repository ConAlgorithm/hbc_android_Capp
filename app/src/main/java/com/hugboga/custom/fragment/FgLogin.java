package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestLogin;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.SharedPre;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * 登录页面
 * Created by admin on 2016/3/8.
 */
@ContentView(R.layout.fg_login)
public class FgLogin extends BaseFragment implements TextWatcher{

    public static String KEY_PHONE = "key_phone";
    public static String KEY_AREA_CODE = "key_area_code";

    @ViewInject(R.id.change_mobile_areacode)
    private TextView areaCodeTextView;
    @ViewInject(R.id.login_phone)
    private EditText phoneEditText;
    @ViewInject(R.id.login_password)
    private EditText passwordEditText;
    @ViewInject(R.id.login_submit)
    private Button loginButton;
    @ViewInject(R.id.iv_pwd_visible)
    private ImageView passwordVisible;

    boolean isPwdVisibility = false;
    String phone;
    String areaCode;
    private SharedPre sharedPre;

    @Override
    protected void initHeader() {
        fgTitle.setText(getString(R.string.login));
    }

    @Override
    protected void initView() {
        String areaCode = null;
        String phone = null;
        if (getArguments() != null) {
            areaCode = getArguments().getString(KEY_AREA_CODE, "");
            phone = getArguments().getString(KEY_PHONE, "");
        }
        sharedPre = new SharedPre(getActivity());
        if (TextUtils.isEmpty(areaCode)) {
            areaCode = sharedPre.getStringValue(SharedPre.CODE);
        }
        if (!TextUtils.isEmpty(areaCode)) {
            this.areaCode = areaCode;
            areaCodeTextView.setText("+" + areaCode);
        } else {
            this.areaCode = "86";
        }
        if (TextUtils.isEmpty(phone)) {
            phone = sharedPre.getStringValue(SharedPre.PHONE);
        }
        if (!TextUtils.isEmpty(phone)) {
            this.phone = phone;
            phoneEditText.setText(phone);
        }
        phoneEditText.addTextChangedListener(this);
        passwordEditText.addTextChangedListener(this);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestLogin) {
            RequestLogin mRequest = (RequestLogin) request;
            UserBean user = mRequest.getData();
            user.setUserEntity(getActivity());
            UserEntity.getUser().setAreaCode(getActivity(), mRequest.areaCode);
            UserEntity.getUser().setPhone(getActivity(), mRequest.mobile);
            UserSession.getUser().setUserToken(getActivity(), user.userToken);
            connectIM();
            EventBus.getDefault().post(
                    new EventAction(EventType.CLICK_USER_LOGIN));
            finishForResult(new Bundle());
        }
    }

    private void connectIM() {
        new IMUtil(getActivity()).conn(UserEntity.getUser().imToken);
    }

    @Event({R.id.login_submit, R.id.change_mobile_areacode, R.id.login_register, R.id.change_mobile_diepwd, R.id.iv_pwd_visible})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.login_submit:
                //登录
                loginGo();
                break;
            case R.id.change_mobile_areacode:
                passwordEditText.clearFocus();
                phoneEditText.clearFocus();
                //选择区号
                collapseSoftInputMethod(); //隐藏键盘
                FgChooseCountry fg = new FgChooseCountry();
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM, "login");
                startFragment(fg, bundle);
                break;
            case R.id.login_register:
                //注册
                collapseSoftInputMethod(); //隐藏键盘
                finish();
                Bundle bundle2 = new Bundle();
                bundle2.putString("areaCode", areaCode);
                bundle2.putString("phone", phone);
                startFragment(new FgRegister(), bundle2);
                break;
            case R.id.change_mobile_diepwd:
                //忘记密码
                collapseSoftInputMethod(); //隐藏键盘
                passwordEditText.setText("");
                Bundle bundle1 = new Bundle();
                bundle1.putString("areaCode",areaCode);
                bundle1.putString("phone",phone);
                startFragment(new FgForgetPasswd(), bundle1);
                break;
            case R.id.iv_pwd_visible:
                if(passwordEditText != null){
                    if(isPwdVisibility){//密码可见
                        isPwdVisibility = false;
                        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible.setImageResource(R.mipmap.icon_pwd_invisible);
                    }else{//密码不可见
                        isPwdVisibility = true;
                        passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible.setImageResource(R.mipmap.icon_pwd_visible);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 进行登录
     */
    private void loginGo() {
        collapseSoftInputMethod(); //隐藏键盘
        MLog.e("areaCode4=" + areaCode);
        if (TextUtils.isEmpty(areaCode)) {
            showTip("区号不能为空");
            return;
        }
        areaCode = areaCode.replace("+", "");
        phone = phoneEditText.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showTip("手机号不能为空");
            return;
        }
        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            showTip("密码不能为空");
            return;
        }
        if (!Pattern.matches("[\\w]{4,16}", password)) {
            showTip("密码必须是4-16位数字或字母");
            return;
        }

        RequestLogin request = new RequestLogin(getActivity(), areaCode, phone, password);
        requestData(request);
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCountry.class.getSimpleName().equals(from)) {
            areaCode = bundle.getString(FgChooseCountry.KEY_COUNTRY_CODE);
            MLog.e("areaCode+" + areaCode);
            areaCodeTextView.setText("+" + areaCode);
        }
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
        String password = passwordEditText.getText().toString();
        if (!TextUtils.isEmpty(areaCode)&&!TextUtils.isEmpty(phone)
                &&!TextUtils.isEmpty(password)
                &&Pattern.matches("[\\w]{4,16}", password)) {
            loginButton.setBackgroundColor(getResources().getColor(R.color.login_ready));
        }else{
            loginButton.setBackgroundColor(getResources().getColor(R.color.login_unready));
        }

    }
}
