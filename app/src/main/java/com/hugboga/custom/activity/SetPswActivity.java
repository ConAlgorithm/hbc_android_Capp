package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
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

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.request.PasswordInitSet;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 17/5/20.
 */

public class SetPswActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;

    @Bind(R.id.iv_pwd_visible1)
    ImageView ivPwdVisible1;
    @Bind(R.id.iv_pwd_visible2)
    ImageView ivPwdVisible2;
    @Bind(R.id.login_submit)
    Button loginSubmit;
    @Bind(R.id.set_psw)
    EditText setPsw;
    @Bind(R.id.set_psw_again)
    EditText setPswAgain;

    boolean isPwd1Visibility = false;
    boolean isPwd2Visibility = false;
    boolean isFirstEnter = true;
    @Override
    public int getContentViewId() {
        return R.layout.fg_set_passwd_new;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        hideInputMethod(setPsw);
        hideInputMethod(setPswAgain);
        initHeader();
        setPsw.addTextChangedListener(this);
        setPswAgain.addTextChangedListener(this);

        setPsw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ivPwdVisible1.setVisibility(View.VISIBLE);
                ivPwdVisible2.setVisibility(View.GONE);
                return false;
            }
        });
        setPswAgain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ivPwdVisible1.setVisibility(View.GONE);
                ivPwdVisible2.setVisibility(View.VISIBLE);
                return false;
            }
        });
       /* setPsw.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                        if(isSoftInputShow()){
                            if(isFirstEnter){
                                isFirstEnter = false;
                                ivPwdVisible1.setVisibility(View.GONE);
                            }else {
                            ivPwdVisible1.setVisibility(View.VISIBLE);
                        }
                            ivPwdVisible2.setVisibility(View.GONE);
                        }else{
                            ivPwdVisible1.setVisibility(View.GONE);
                        }

                }else{
                    ivPwdVisible1.setVisibility(View.GONE);
                }
            }
        });

        setPswAgain.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(isSoftInputShow()){
                        if(isFirstEnter){
                            isFirstEnter = false;
                            ivPwdVisible2.setVisibility(View.GONE);
                        }else{
                            ivPwdVisible2.setVisibility(View.VISIBLE);
                        }
                        ivPwdVisible1.setVisibility(View.GONE);
                    }else{
                        ivPwdVisible2.setVisibility(View.GONE);
                    }
                }else{
                    ivPwdVisible2.setVisibility(View.GONE);
                }
            }
        });*/
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(setPsw.hasFocus()){
            setPsw.setSelection(setPsw.getText().toString().length());
        }
        if(setPswAgain.hasFocus()){
            setPswAgain.setSelection(setPswAgain.getText().toString().length());
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
    @OnClick({R.id.iv_pwd_visible1,R.id.iv_pwd_visible2,R.id.login_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pwd_visible1:
                if (setPsw != null) {
                    setPsw.setSelection(setPsw.getText().toString().length());
                    if (isPwd1Visibility) {//密码可见
                        isPwd1Visibility = false;
                        setPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        ivPwdVisible1.setImageResource(R.mipmap.login_invisible);
                    } else {//密码不可见
                        isPwd1Visibility = true;
                        setPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        ivPwdVisible1.setImageResource(R.mipmap.login_visible);
                    }
                }
                break;
            case R.id.iv_pwd_visible2:
                if (setPswAgain != null) {
                    setPswAgain.setSelection(setPswAgain.getText().toString().length());
                    if (isPwd2Visibility) {//密码可见
                        isPwd2Visibility = false;
                        setPswAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        ivPwdVisible2.setImageResource(R.mipmap.login_invisible);
                    } else {//密码不可见
                        isPwd2Visibility = true;
                        setPswAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        ivPwdVisible2.setImageResource(R.mipmap.login_visible);
                    }
                }
                break;
            case R.id.login_submit:
                String set_psw = setPsw.getText().toString().trim();
                String set_psw_again = setPswAgain.getText().toString().trim();
                if(!TextUtils.isEmpty(set_psw) && !TextUtils.isEmpty(set_psw_again) && !TextUtils.equals(set_psw,set_psw_again)){
                    CommonUtils.showToast("两次密码不一致");
                }
                setPwd(setPswAgain.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    public void setPwd(String pwd) {

        PasswordInitSet request = new PasswordInitSet(activity, pwd);
        requestData(request);
    }
    @Override
    public void afterTextChanged(Editable editable) {

        String set_psw = setPsw.getText().toString().trim();
        String set_psw_again = setPswAgain.getText().toString().trim();

        if(set_psw.length() > 6 && set_psw_again.length() >6){
            loginSubmit.setEnabled(true);
            loginSubmit.setText("提交");
            //loginSubmit.setBackgroundColor(getResources().getColor(R.color.login_ready));
        }else{
            loginSubmit.setEnabled(false);
            //loginSubmit.setBackgroundColor(getResources().getColor(R.color.login_unready));
        }

        if (!isPwd1Visibility) {
            setPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPwdVisible1.setImageResource(R.mipmap.login_invisible);
        } else {
            setPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivPwdVisible1.setImageResource(R.mipmap.login_visible);
        }

        if (!isPwd2Visibility) {
            setPswAgain.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPwdVisible2.setImageResource(R.mipmap.login_invisible);
        } else {
            setPswAgain.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivPwdVisible2.setImageResource(R.mipmap.login_visible);
        }
        if(setPsw.hasFocus()){
            setPsw.setSelection(setPsw.getText().toString().length());
        }
        if(setPswAgain.hasFocus()){
            setPswAgain.setSelection(setPswAgain.getText().toString().length());
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if(request instanceof PasswordInitSet){
            CommonUtils.showToast("密码设置成功");
            Intent intent = new Intent();
            intent.putExtra("needInitPwd",false);
            setResult(SettingActivity.RESULT_OK, intent);
            finish();
        }
    }

    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        headerTitle.setText("设置密码");
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
