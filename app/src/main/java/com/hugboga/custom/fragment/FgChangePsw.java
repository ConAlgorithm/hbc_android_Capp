
package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestChangePwd;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.regex.Pattern;

@ContentView(R.layout.fg_change_passwd)
public class FgChangePsw extends BaseFragment {

    @ViewInject(R.id.change_passwd_oldpwd)
    EditText oldPwdEditText; //旧密码
    @ViewInject(R.id.change_passwd_newpwd)
    EditText newPwdEditText; //新密码
    @ViewInject(R.id.change_passwd_rewpwd)
    EditText rewPwdEditText; //重复密码

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestChangePwd) {
            RequestChangePwd mParser = (RequestChangePwd) request;
            showTip("修改密码成功");
            UserEntity.getUser().setWeakPassword(getActivity(),false);
            finish();
        }
    }

    @Override
    protected void inflateContent() {
    }

    @Event({R.id.change_passwd_submit})
    protected void onClickView(View view) {
        switch (view.getId()) {
            case R.id.change_passwd_submit:
                //修改密码
                collapseSoftInputMethod();
                String oldStr = oldPwdEditText.getText().toString();
                if(TextUtils.isEmpty(oldStr)){
                    showTip("原密码不能为空");
                    oldPwdEditText.requestFocus();
                    return;
                }
                String password = newPwdEditText.getText().toString();
                if(TextUtils.isEmpty(password)){
                    showTip("新密码不能为空");
                    newPwdEditText.requestFocus();
                    return;
                }
                if(!Pattern.matches("[\\w]{4,16}", password)){
                    showTip("密码必须是4-16位数字或字母");
                    return;
                }
                String repassword = rewPwdEditText.getText().toString();
                if(TextUtils.isEmpty(repassword)){
                    showTip("确认新密码不能为空");
                    rewPwdEditText.requestFocus();
                    return;
                }
                if(!TextUtils.equals(password, repassword)) {
                    showTip("两次填写的新密码不一致");
                    return;
                }
                RequestChangePwd requestChangePwd = new RequestChangePwd(getActivity(),oldStr,password);
                requestData(requestChangePwd);
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
        fgTitle.setTextColor(getResources().getColor(R.color.my_content_title_color));
        fgTitle.setText("修改密码");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

}
