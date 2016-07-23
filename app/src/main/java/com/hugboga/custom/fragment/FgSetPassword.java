
package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestAfterSetPwd;
import com.hugboga.custom.data.request.RequestChangePwd;
import com.hugboga.custom.data.request.RequestSetPwd;
import com.hugboga.custom.utils.IMUtil;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.regex.Pattern;

import org.greenrobot.eventbus.EventBus;

@ContentView(R.layout.fg_set_passwd)
public class FgSetPassword extends BaseFragment {

    @ViewInject(R.id.change_passwd_newpwd)
    EditText newPwdEditText; //新密码
    @ViewInject(R.id.change_passwd_rewpwd)
    EditText rewPwdEditText; //重复密码

    private String areaCode;
    private String mobile;
    private String unionid;
    private UserBean userBean;
    private boolean isAfterProcess = false;

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
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
            UserEntity.getUser().setWeakPassword(getActivity(), false);
            bundle.putString(KEY_FRAGMENT_NAME, FgSetPassword.class.getSimpleName());
            finishForResult(bundle);
        }else if(request instanceof RequestAfterSetPwd){
            RequestAfterSetPwd requestAfterSetPwd = (RequestAfterSetPwd) request;
            showTip("密码设置成功");
            UserEntity.getUser().setWeakPassword(getActivity(), false);
            Bundle bundle = new Bundle();
            bundle.putString(KEY_FRAGMENT_NAME, FgSetPassword.class.getSimpleName());
            finishForResult(bundle);
        }
    }

    @Override
    protected void inflateContent() {
    }

    @Event({R.id.change_passwd_submit})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.change_passwd_submit:
                //修改密码
                collapseSoftInputMethod();
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
                    RequestAfterSetPwd requestAfterSetPwd = new RequestAfterSetPwd(getActivity(), areaCode, mobile, password);
                    requestData(requestAfterSetPwd);
                }else{
                    RequestSetPwd requestSetPwd = new RequestSetPwd(getActivity(), areaCode, mobile, password, unionid);
                    requestData(requestSetPwd);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        MLog.w(this + " onFragmentResult " + bundle);
    }

    @Override
    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        fgTitle.setText("设置密码");
        fgLeftBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initView() {
        if(getArguments() != null){
            areaCode = getArguments().getString("areaCode");
            mobile = getArguments().getString("mobile");
            unionid = getArguments().getString("unionid");
            userBean = (UserBean) getArguments().getSerializable("userBean");
            isAfterProcess = getArguments().getBoolean("isAfterProcess");
        }
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

}
