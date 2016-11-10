package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestAfterSetPwd;
import com.hugboga.custom.data.request.RequestSetPwd;
import com.hugboga.custom.utils.CommonUtils;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 16/8/6.
 */
public class SetPasswordActivity extends BaseActivity{

    @Bind(R.id.change_passwd_newpwd)
    EditText newPwdEditText; //新密码
    @Bind(R.id.change_passwd_rewpwd)
    EditText rewPwdEditText; //重复密码

    private String areaCode;
    private String mobile;
    private String unionid;
    private UserBean userBean;
    private boolean isAfterProcess = false;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_set_passwd);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            areaCode = bundle.getString("areaCode");
            mobile = bundle.getString("mobile");
            unionid = bundle.getString("unionid");
            userBean = (UserBean) bundle.getSerializable("userBean");
            isAfterProcess = bundle.getBoolean("isAfterProcess");
        }

        initDefaultTitleBar();
        fgTitle.setText("设置密码");
        fgLeftBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestSetPwd) {
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
            showTip("密码设置成功");
            UserEntity.getUser().setWeakPassword(this, false);
//            bundle.putString(KEY_FRAGMENT_NAME, FgSetPassword.class.getSimpleName());
//            finishForResult(bundle);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(BindMobileActivity.REQUEST_CODE, intent);
            finish();
        }else if(request instanceof RequestAfterSetPwd){
            RequestAfterSetPwd requestAfterSetPwd = (RequestAfterSetPwd) request;
            showTip("密码设置成功");
            UserEntity.getUser().setWeakPassword(this, false);
//            Bundle bundle = new Bundle();
//            bundle.putString(KEY_FRAGMENT_NAME, SetPasswordActivity.class.getSimpleName());
//            finishForResult(bundle);
            setResult(BindMobileActivity.REQUEST_CODE);
            finish();
        }
    }

    @OnClick({R.id.change_passwd_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_passwd_submit:
                //修改密码
                collapseSoftInputMethod(newPwdEditText);
                collapseSoftInputMethod(rewPwdEditText);
                String password = newPwdEditText.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    showTip("密码不能为空");
                    newPwdEditText.requestFocus();
                    return;
                }
                if (!Pattern.matches("[\\w]{6,16}", password)) {
                    showTip("密码必须是6-16位数字或字母");
                    return;
                }
                String repassword = rewPwdEditText.getText().toString();
                if (TextUtils.isEmpty(repassword)) {
                    showTip("确认密码不能为空");
                    rewPwdEditText.requestFocus();
                    return;
                }
                if (!TextUtils.equals(password, repassword)) {
                    showTip("两次填写的密码不一致");
                    return;
                }
                if(isAfterProcess){
                    RequestAfterSetPwd requestAfterSetPwd = new RequestAfterSetPwd(this, areaCode, mobile, password);
                    requestData(requestAfterSetPwd);
                }else{
                    RequestSetPwd requestSetPwd = new RequestSetPwd(this, areaCode, mobile, password, unionid);
                    requestData(requestSetPwd);
                }
                break;
            default:
                break;
        }
    }

    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    private void showTip(String tips) {
        CommonUtils.showToast(tips);
    }
}
