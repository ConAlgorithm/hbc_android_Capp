package com.hugboga.custom.activity;

import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 16/8/6.
 */

public class ChangePswActivity extends BaseActivity {
    @Bind(R.id.change_passwd_oldpwd)
    EditText oldPwdEditText; //旧密码
    @Bind(R.id.change_passwd_newpwd)
    EditText newPwdEditText; //新密码
    @Bind(R.id.change_passwd_rewpwd)
    EditText rewPwdEditText; //重复密码
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.change_passwd_submit)
    Button changePasswdSubmit;

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestChangePwd) {
            RequestChangePwd mParser = (RequestChangePwd) request;
            CommonUtils.showToast("修改密码成功");
            UserEntity.getUser().setWeakPassword(activity, false);
            finish();
        }
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fg_change_passwd);
        ButterKnife.bind(this);
        initHeader();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideInputMethod(oldPwdEditText);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.change_passwd_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_passwd_submit:
                //修改密码
                String oldStr = oldPwdEditText.getText().toString();
                if (TextUtils.isEmpty(oldStr)) {
                    CommonUtils.showToast("原密码不能为空");
                    oldPwdEditText.requestFocus();
                    return;
                }
                String password = newPwdEditText.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    CommonUtils.showToast("新密码不能为空");
                    newPwdEditText.requestFocus();
                    return;
                }
                if (!Pattern.matches("[\\w]{6,16}", password)) {
                    CommonUtils.showToast("密码必须是6-16位数字或字母");
                    return;
                }
                String repassword = rewPwdEditText.getText().toString();
                if (TextUtils.isEmpty(repassword)) {
                    CommonUtils.showToast("确认新密码不能为空");
                    rewPwdEditText.requestFocus();
                    return;
                }
                if (!TextUtils.equals(password, repassword)) {
                    CommonUtils.showToast("两次填写的新密码不一致");
                    return;
                }
                RequestChangePwd requestChangePwd = new RequestChangePwd(activity, oldStr, password);
                requestData(requestChangePwd);
                break;
            default:
                break;
        }
    }

    protected void initHeader() {
        //设置标题颜色，返回按钮图片
//        leftBtn.setImageResource(R.mipmap.top_back_black);
        headerTitle.setText("修改密码");
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}

