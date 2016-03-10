package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.bean.UserEntity;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.request.RequestLogin;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.regex.Pattern;

/**
 *
 * 登录页面
 * Created by admin on 2016/3/8.
 */
@ContentView(R.layout.fg_login)
public class FgLogin extends BaseFragment {

    @ViewInject(R.id.change_mobile_areacode)
    private TextView areaCodeTextView;
    @ViewInject(R.id.login_phone)
    private EditText phoneEditText;
    @ViewInject(R.id.login_password)
    private EditText passwordEditText;
    @ViewInject(R.id.login_submit)
    Button loginButton;

    @Override
    protected void initHeader() {
        fgTitle.setText(getString(R.string.login));
    }

    @Override
    protected void initView() {

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
        if(request instanceof RequestLogin){
            RequestLogin mRequest = (RequestLogin)request;
            UserBean user = mRequest.getData();
            user.setUserEntity(getActivity());
            UserEntity.getUser().setUserToken(getActivity(),user.userToken);
            finish();
        }
    }
    @Event({R.id.login_submit,R.id.change_mobile_areacode,R.id.login_register,R.id.change_mobile_diepwd})
    private void onClickView(View view){
        switch (view.getId()) {
            case R.id.login_submit:
                //登录
                loginGo();
                break;
            /*case R.id.change_mobile_areacode:
                passwordEditText.clearFocus();
                phoneEditText.clearFocus();
                //选择区号
                collapseSoftInputMethod(); //隐藏键盘
                FgChooseCountry fg = new FgChooseCountry();
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM, "login");
                startFragment(fg,bundle);
                break;
            case R.id.login_register:
                //注册
                collapseSoftInputMethod(); //隐藏键盘
                finish();
                Bundle bundle2 = new Bundle();
                bundle2.putString("areaCode",areaCode);
                bundle2.putString("phone", phone);
                startFragment(new FgRegister(),bundle2);
                break;
            case R.id.change_mobile_diepwd:
                //忘记密码
                collapseSoftInputMethod(); //隐藏键盘
                passwordEditText.setText("");
                Bundle bundle1 = new Bundle();
                bundle1.putString("areaCode",areaCode);
                bundle1.putString("phone",phone);
                startFragment(new FgForgetPasswd(), bundle1);
                break;*/
            default:
                break;
        }
    }

    /**
     * 进行登录
     */
    private void loginGo(){
        collapseSoftInputMethod(); //隐藏键盘
        String areaCode = areaCodeTextView.getText().toString();
        if(TextUtils.isEmpty(areaCode)){
            showTip("区号不能为空");
            return;
        }
        areaCode = areaCode.substring(1);
        String phone = phoneEditText.getText().toString();
        if(TextUtils.isEmpty(phone)){
            showTip("手机号不能为空");
            return;
        }
        String password = passwordEditText.getText().toString();
        if(TextUtils.isEmpty(password)){
            showTip("密码不能为空");
            return;
        }
        if(!Pattern.matches("[\\w]{4,16}", password)){
            showTip("密码必须是4-16位数字或字母");
            return;
        }

        RequestLogin request = new RequestLogin(getActivity(),areaCode,phone,password);
        requestData(request);
    }


}
