package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestForgetPwd;
import com.hugboga.custom.data.request.RequestVerity;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.regex.Pattern;

@ContentView(R.layout.fg_forget_passwd)
public class FgForgetPasswd extends BaseFragment implements TextWatcher {
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
        String verity_EditText = verityEditText.getText().toString();
        if (!TextUtils.isEmpty(phone)
                &&!TextUtils.isEmpty(password)  &&!TextUtils.isEmpty(verity_EditText)
                &&Pattern.matches("[\\w]{4,16}", password)) {
            forget_passwd_submit.setEnabled(true);
            forget_passwd_submit.setBackgroundColor(getResources().getColor(R.color.login_ready));
        }else{
            forget_passwd_submit.setEnabled(false);
            forget_passwd_submit.setBackgroundColor(getResources().getColor(R.color.login_unready));
        }
    }

    @ViewInject(R.id.forget_passwd_areacode)
    private TextView areaCodeTextView;
    @ViewInject(R.id.forget_passwd_phone)
    private EditText phoneEditText;
    @ViewInject(R.id.forget_passwd_verity)
    private EditText verityEditText;
    @ViewInject(R.id.forget_passwd_newpass_layout)
    private EditText passwordEditText;
    @ViewInject(R.id.forget_passwd_getcode)
    TextView getCodeBtn; //发送验证码按钮
    @ViewInject(R.id.forget_passwd_time)
    TextView timeTextView; //验证码倒计时
    @ViewInject(R.id.forget_passwd_submit)
    Button forget_passwd_submit; //验证码倒计时


    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestForgetPwd) {
            RequestForgetPwd mParser = (RequestForgetPwd) request;
            showTip("重置密码成功");
            UserEntity.getUser().setWeakPassword(getActivity(),false);
            finish();
        }else if(request instanceof RequestVerity){
            RequestVerity parserVerity = (RequestVerity) request;
            showTip("验证码已发送");
            time = 59;
            handler.postDelayed(runnable, 0);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if(request instanceof RequestVerity){
            setBtnVisible(true);
        }
        super.onDataRequestError(errorInfo, request);
    }

    Integer time = 59;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(time>0){
                setBtnVisible(false);
                timeTextView.setText(String.valueOf(time--) + "秒");
                handler.postDelayed(this,1000);
            }else{
                setBtnVisible(true);
                timeTextView.setText(String.valueOf(59) + "秒");
            }

        }
    };

    @Override
    protected void inflateContent() {
    }

    @Event({R.id.forget_passwd_submit, R.id.forget_passwd_getcode, R.id.forget_passwd_areacode})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.forget_passwd_submit:
                //重置密码提交
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
                String verity = verityEditText.getText().toString();
                if(TextUtils.isEmpty(verity)){
                    showTip("验证码不能为空");
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
                RequestForgetPwd requestForgetPwd = new RequestForgetPwd(getActivity(), areaCode,phone,password, verity);
                requestData(requestForgetPwd);
                break;
            case R.id.forget_passwd_getcode:
                //获取验证码
                setBtnVisible(false);
                collapseSoftInputMethod(); //隐藏键盘
                String areaCode1 = areaCodeTextView.getText().toString();
                if(TextUtils.isEmpty(areaCode1)){
                    showTip("区号不能为空");
                    setBtnVisible(true);
                    return;
                }
                areaCode1 = areaCode1.substring(1);
                String phone1 = phoneEditText.getText().toString();
                if(TextUtils.isEmpty(phone1)){
                    showTip("手机号不能为空");
                    setBtnVisible(true);
                    return;
                }

                RequestVerity requestVerity = new RequestVerity(getActivity(),areaCode1,phone1,2);
                requestData(requestVerity);
                break;
            case R.id.forget_passwd_areacode:
                //选择地区
                collapseSoftInputMethod(); //隐藏键盘
                FgChooseCountry fg = new FgChooseCountry();
                Bundle bundle = new Bundle();
                bundle.putString(KEY_FROM, "forgetPasswd");
                startFragment(fg,bundle);
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

    @Override
    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        String from = bundle.getString(KEY_FRAGMENT_NAME);
        if(FgChooseCountry.class.getSimpleName().equals(from)){
            String areaCode = bundle.getString(FgChooseCountry.KEY_COUNTRY_CODE);
            areaCodeTextView.setText("+" + areaCode);
        }
    }

    @Override
    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        fgTitle.setText("忘记密码");
        //初始化数据
        if(mSourceFragment instanceof FgLogin){
            String code = getArguments().getString("areaCode");
            if(code!=null && !code.isEmpty()){
                areaCodeTextView.setText("+" + code);
            }
            String phone = getArguments().getString("phone");
            if(phone!=null && !phone.isEmpty()){
                phoneEditText.setText(phone);
            }
        }else{
            String code = UserEntity.getUser().getAreaCode(getActivity());
            if(code!=null && !code.isEmpty()){
                areaCodeTextView.setText("+" + code);
            }
            String phone = UserEntity.getUser().getPhone(getActivity());
            if(phone!=null && !phone.isEmpty()){
                phoneEditText.setText(phone);
            }
        }
    }

    @Override
    protected void initView() {
        phoneEditText.addTextChangedListener(this);
        passwordEditText.addTextChangedListener(this);
        verityEditText.addTextChangedListener(this);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }


}
