package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.PasswordInitSet;
import com.hugboga.custom.data.request.RequestAfterSetPwd;
import com.hugboga.custom.data.request.RequestSetPwd;
import com.hugboga.custom.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 17/5/20.
 */

public class SetPswActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.header_right_txt)
    TextView headerRightTxt;

    @BindView(R.id.iv_pwd_visible1)
    ImageView ivPwdVisible1;
    @BindView(R.id.iv_pwd_visible2)
    ImageView ivPwdVisible2;
    @BindView(R.id.login_submit)
    Button loginSubmit;
    @BindView(R.id.set_psw)
    EditText setPsw;
    @BindView(R.id.set_psw_again)
    EditText setPswAgain;

    boolean isPwd1Visibility = false;
    boolean isPwd2Visibility = false;
    boolean isFirstEnter = true;

    private boolean isAfterProcess = false;
    private String areaCode;
    private String mobile;
    private String unionid;
    private UserBean userBean;
    boolean isFromWeChat;
    boolean isFromSetting;

    @Override
    public int getContentViewId() {
        return R.layout.fg_set_passwd_new;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            areaCode = bundle.getString("areaCode");
            mobile = bundle.getString("mobile");
            unionid = bundle.getString("unionid");
            userBean = (UserBean) bundle.getSerializable("userBean");
            isAfterProcess = bundle.getBoolean("isAfterProcess");
        }
        isFromWeChat = getIntent().getBooleanExtra("isFromWeChat",false);
        isFromSetting = getIntent().getBooleanExtra("isFromSetting",false);
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
    }

    @Override
    protected void onPause() {
        hideSoftInput();
        super.onPause();
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
    @OnClick({R.id.iv_pwd_visible1,R.id.iv_pwd_visible2,R.id.header_right_txt})
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
            case R.id.header_right_txt:
                String set_psw = setPsw.getText().toString().trim();
                String set_psw_again = setPswAgain.getText().toString().trim();
                if (TextUtils.isEmpty(set_psw)) {
                    CommonUtils.showToast(R.string.login_check_pwd);
                    setPsw.requestFocus();
                    return;
                }
                if (!Pattern.matches("[\\w]{6,16}", set_psw)) {
                    CommonUtils.showToast(R.string.login_check_pwd_length);
                    return;
                }
                if (TextUtils.isEmpty(set_psw_again)) {
                    CommonUtils.showToast(R.string.login_check_new_pwd_confirm);
                    setPswAgain.requestFocus();
                    return;
                }
                if (!TextUtils.equals(set_psw, set_psw_again)) {
                    CommonUtils.showToast(R.string.login_check_pwd_inconformity2);
                    return;
                }

                if (isAfterProcess) {
                    RequestAfterSetPwd requestAfterSetPwd = new RequestAfterSetPwd(this, areaCode, mobile, setPswAgain.getText().toString().trim());
                    requestData(requestAfterSetPwd);
                } else if (isFromWeChat) {
                    RequestSetPwd requestSetPwd = new RequestSetPwd(this, areaCode, mobile, setPswAgain.getText().toString().trim(), unionid);
                    requestData(requestSetPwd);
                } else if (isFromSetting) {
                    setPwd(setPswAgain.getText().toString().trim());
                }

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

        if(set_psw.length() >= 6 && set_psw_again.length() >=6){
            loginSubmit.setEnabled(true);
            loginSubmit.setText(R.string.hbc_submit);
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
            CommonUtils.showToast(R.string.login_set_pwd_success);
            Intent intent = new Intent();
            intent.putExtra("needInitPwd",false);
            setResult(SettingActivity.RESULT_OK, intent);
            finish();
        } else  if (request instanceof RequestSetPwd) {
            RequestSetPwd requestSetPwd = (RequestSetPwd) request;
//            UserBean userBean = requestSetPwd.getData();
            Bundle bundle = new Bundle();
            if(userBean != null){
                bundle.putSerializable("userBean",userBean);
//                UserEntity.getUser().setNickname(getActivity(), userBean.nickname);
//                UserEntity.getUser().setAvatar(getActivity(), userBean.avatar);
//                userBean.setUserEntity(getActivity());
//                UserSession.getUser().setUserToken(getActivity(), userBean.userToken);
//                IMUtil.getInstance().connect();
//                EventBus.getDefault().post(
//                        new EventAction(EventType.CLICK_USER_LOGIN));
            }
            CommonUtils.showToast(R.string.login_set_pwd_success);
            //UserEntity.getUser().setWeakPassword(this, false);
//            bundle.putString(KEY_FRAGMENT_NAME, FgSetPassword.class.getSimpleName());
//            finishForResult(bundle);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(BindMobileActivity.RESULT_OK, intent);
            EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOGIN));
            finish();
        }else if(request instanceof RequestAfterSetPwd){
            RequestAfterSetPwd requestAfterSetPwd = (RequestAfterSetPwd) request;
            CommonUtils.showToast(R.string.login_set_pwd_success);

            setResult(BindMobileActivity.REQUEST_CODE);
            finish();
        }
    }

    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        headerTitle.setText(R.string.login_set_pwd);
        if(isAfterProcess || isFromWeChat){
            headerLeftBtn.setVisibility(View.GONE);
            headerTitle.setText("  " + CommonUtils.getString(R.string.login_set_pwd));
        }else if(isFromSetting){
            headerLeftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        headerRightTxt.setVisibility(View.VISIBLE);
        headerRightTxt.setText(getResources().getString(R.string.traveler_info_save));
        headerRightTxt.setTextSize(15);
        headerRightTxt.setTextColor(getResources().getColor(R.color.color_151515));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((isAfterProcess || isFromWeChat) && keyCode==KeyEvent.KEYCODE_BACK){
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
