package com.hugboga.custom.activity;

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
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestChangePwd;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.UIUtils;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created on 16/8/6.
 */

public class ChangePswActivity extends BaseActivity implements TextWatcher{
    @Bind(R.id.old_psw)
    EditText oldPwdEditText; //旧密码
    @Bind(R.id.pwd_new)
    EditText newPwdEditText; //新密码
    @Bind(R.id.set_psw_again)
    EditText rewPwdEditText; //重复密码
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;

    @Bind(R.id.header_title)
    TextView headerTitle;

    @Bind(R.id.login_submit)
    Button changePasswdSubmit;

    @Bind(R.id.iv_pwd_visible1)
    ImageView ivPwdVisible1;
    @Bind(R.id.iv_pwd_visible2)
    ImageView ivPwdVisible2;
    @Bind(R.id.iv_pwd_visible3)
    ImageView ivPwdVisible3;

    boolean isPwd1Visibility = false;
    boolean isPwd2Visibility = false;
    boolean isPwd3Visibility = false;
    boolean isFirstEnter = true;
    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestChangePwd) {
            RequestChangePwd mParser = (RequestChangePwd) request;
            CommonUtils.showToast(R.string.login_change_pwd_success);
            UserEntity.getUser().setWeakPassword(activity, false);
            finish();
        }
    }


    @Override
    public int getContentViewId() {
        return R.layout.change_psw;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initHeader();
        hideInputMethod(oldPwdEditText);
        hideInputMethod(newPwdEditText);
        hideInputMethod(rewPwdEditText);
        oldPwdEditText.addTextChangedListener(this);
        newPwdEditText.addTextChangedListener(this);
        rewPwdEditText.addTextChangedListener(this);

        oldPwdEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ivPwdVisible1.setVisibility(View.VISIBLE);
                ivPwdVisible2.setVisibility(View.GONE);
                ivPwdVisible3.setVisibility(View.GONE);
                return false;
            }
        });
        newPwdEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ivPwdVisible1.setVisibility(View.GONE);
                ivPwdVisible2.setVisibility(View.VISIBLE);
                ivPwdVisible3.setVisibility(View.GONE);
                return false;
            }
        });
        rewPwdEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ivPwdVisible1.setVisibility(View.GONE);
                ivPwdVisible2.setVisibility(View.GONE);
                ivPwdVisible3.setVisibility(View.VISIBLE);
                return false;
            }
        });
        /*oldPwdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //if(isSoftInputShow()){
                        if(isFirstEnter){
                            isFirstEnter = false;
                            ivPwdVisible1.setVisibility(View.GONE);
                        }else {
                            ivPwdVisible1.setVisibility(View.VISIBLE);

                        }
                        ivPwdVisible2.setVisibility(View.GONE);
                        ivPwdVisible3.setVisibility(View.GONE);
                    //}else{
                     //   ivPwdVisible1.setVisibility(View.VISIBLE);
                   // }
                }else{
                    ivPwdVisible1.setVisibility(View.GONE);
                }
            }
        });

        newPwdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //if(isSoftInputShow()){
                        if(isFirstEnter){
                            isFirstEnter = false;
                            ivPwdVisible2.setVisibility(View.GONE);
                        }else {
                            ivPwdVisible2.setVisibility(View.VISIBLE);

                        }
                        ivPwdVisible1.setVisibility(View.GONE);
                        ivPwdVisible3.setVisibility(View.GONE);
                    //}else{
                    //    ivPwdVisible2.setVisibility(View.VISIBLE);
                    //}
                }else{
                    ivPwdVisible2.setVisibility(View.GONE);
                }
            }
        });
        rewPwdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //if(isSoftInputShow()){
                        if(isFirstEnter){
                            isFirstEnter = false;
                            ivPwdVisible3.setVisibility(View.GONE);
                        }else {
                            ivPwdVisible3.setVisibility(View.VISIBLE);

                        }
                        ivPwdVisible1.setVisibility(View.GONE);
                        ivPwdVisible2.setVisibility(View.GONE);
                   // }else{
                   //     ivPwdVisible3.setVisibility(View.VISIBLE);
                    //}
                }else{
                    ivPwdVisible3.setVisibility(View.GONE);
                }
            }
        });*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftInput();
    }


    @OnClick({R.id.header_right_txt,R.id.iv_pwd_visible1,R.id.iv_pwd_visible2,R.id.iv_pwd_visible3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_right_txt:
                //修改密码
                String oldStr = oldPwdEditText.getText().toString();
                if (TextUtils.isEmpty(oldStr)) {
                    CommonUtils.showToast(R.string.login_check_old_pwd);
                    oldPwdEditText.requestFocus();
                    return;
                }
                String password = newPwdEditText.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    CommonUtils.showToast(R.string.login_check_new_pwd);
                    newPwdEditText.requestFocus();
                    return;
                }
                if (!Pattern.matches("[\\w]{6,16}", password)) {
                    CommonUtils.showToast(R.string.login_check_pwd_length);
                    return;
                }
                String repassword = rewPwdEditText.getText().toString();
                if (TextUtils.isEmpty(repassword)) {
                    CommonUtils.showToast(R.string.login_check_new_pwd_confirm);
                    rewPwdEditText.requestFocus();
                    return;
                }
                if (!TextUtils.equals(password, repassword)) {
                    CommonUtils.showToast(R.string.login_check_pwd_inconformity);
                    return;
                }
                RequestChangePwd requestChangePwd = new RequestChangePwd(activity, oldStr, password);
                requestData(requestChangePwd);
                break;
            case R.id.iv_pwd_visible1:
                if (oldPwdEditText != null) {
                    oldPwdEditText.setSelection(oldPwdEditText.getText().toString().length());
                    if (isPwd1Visibility) {//密码可见
                        isPwd1Visibility = false;
                        oldPwdEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        ivPwdVisible1.setImageResource(R.mipmap.login_invisible);
                    } else {//密码不可见
                        isPwd1Visibility = true;
                        oldPwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        ivPwdVisible1.setImageResource(R.mipmap.login_visible);
                    }
                }
                break;
            case R.id.iv_pwd_visible2:
                if (newPwdEditText != null) {
                    newPwdEditText.setSelection(newPwdEditText.getText().toString().length());
                    if (isPwd2Visibility) {//密码可见
                        isPwd2Visibility = false;
                        newPwdEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        ivPwdVisible2.setImageResource(R.mipmap.login_invisible);
                    } else {//密码不可见
                        isPwd2Visibility = true;
                        newPwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        ivPwdVisible2.setImageResource(R.mipmap.login_visible);
                    }
                }
                break;
            case R.id.iv_pwd_visible3:
                if (rewPwdEditText != null) {
                    rewPwdEditText.setSelection(rewPwdEditText.getText().toString().length());
                    if (isPwd3Visibility) {//密码可见
                        isPwd3Visibility = false;
                        rewPwdEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        ivPwdVisible3.setImageResource(R.mipmap.login_invisible);
                    } else {//密码不可见
                        isPwd3Visibility = true;
                        rewPwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        ivPwdVisible3.setImageResource(R.mipmap.login_visible);
                    }
                }
                break;
            default:
                break;
        }
    }

    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        headerTitle.setText(R.string.setting_chang_pwd);
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerRightTxt.setVisibility(View.VISIBLE);
        headerRightTxt.setText(getResources().getString(R.string.traveler_info_save));
        headerRightTxt.setTextSize(15);
        headerRightTxt.setTextColor(getResources().getColor(R.color.color_151515));
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(oldPwdEditText.hasFocus()){
            oldPwdEditText.setSelection(oldPwdEditText.getText().toString().length());
        }
        if(newPwdEditText.hasFocus()){
            newPwdEditText.setSelection(newPwdEditText.getText().toString().length());
        }
        if(rewPwdEditText.hasFocus()){
            rewPwdEditText.setSelection(rewPwdEditText.getText().toString().length());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String oldPwd = oldPwdEditText.getText().toString().trim();
        String newPwd = newPwdEditText.getText().toString().trim();
        String reNewPwd = rewPwdEditText.getText().toString().trim();

        if(oldPwd.length() >= 6 && newPwd.length() >=6 && reNewPwd.length() >=6){
            changePasswdSubmit.setEnabled(true);
            //changePasswdSubmit.setBackgroundColor(getResources().getColor(R.color.login_ready));
        }else{
            changePasswdSubmit.setEnabled(false);
            //changePasswdSubmit.setBackgroundColor(getResources().getColor(R.color.login_unready));
        }

        if (!isPwd1Visibility) {
            oldPwdEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPwdVisible1.setImageResource(R.mipmap.login_invisible);
        } else {
            oldPwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivPwdVisible1.setImageResource(R.mipmap.login_visible);
        }

        if (!isPwd2Visibility) {
            newPwdEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPwdVisible2.setImageResource(R.mipmap.login_invisible);
        } else {
            newPwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivPwdVisible2.setImageResource(R.mipmap.login_visible);
        }

        if (!isPwd3Visibility) {
            rewPwdEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivPwdVisible3.setImageResource(R.mipmap.login_invisible);
        } else {
            rewPwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivPwdVisible3.setImageResource(R.mipmap.login_visible);
        }

        if(oldPwdEditText.hasFocus()){
            oldPwdEditText.setSelection(oldPwdEditText.getText().toString().length());
        }
        if(newPwdEditText.hasFocus()){
            newPwdEditText.setSelection(newPwdEditText.getText().toString().length());
        }
        if(rewPwdEditText.hasFocus()){
            rewPwdEditText.setSelection(rewPwdEditText.getText().toString().length());
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }
}

