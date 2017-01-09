package com.hugboga.custom.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestChangePwd;
import com.hugboga.custom.utils.CommonUtils;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 16/11/21.
 */
public class InitPasswordActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.init_password_first_et)
    EditText newPwdEditText;
    @Bind(R.id.init_password_again_et)
    EditText rewPwdEditText;
    @Bind(R.id.init_password_submit_btn)
    Button submitBTN;

    private String oldPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            oldPassword = savedInstanceState.getString(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                oldPassword = bundle.getString(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.activity_init_password);
        ButterKnife.bind(this);

        initDefaultTitleBar();
        fgTitle.setText("设置密码");
        fgLeftBtn.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        fgTitle.setLayoutParams(params);

        newPwdEditText.addTextChangedListener(this);
        rewPwdEditText.addTextChangedListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.PARAMS_DATA, oldPassword);
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideInputMethod(newPwdEditText);
        hideInputMethod(rewPwdEditText);
    }

    @OnClick({R.id.init_password_submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.init_password_submit_btn:
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
                RequestChangePwd requestChangePwd = new RequestChangePwd(activity, oldPassword, password);
                requestData(requestChangePwd);
                break;
            default:
                break;
        }
    }


    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestChangePwd) {
            CommonUtils.showToast("修改密码成功");
            UserEntity.getUser().setWeakPassword(activity, false);
            finish();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (newPwdEditText.getText() == null || rewPwdEditText.getText() == null) {
            return;
        }
        String newPwd = newPwdEditText.getText().toString().trim();
        String rewPwd = rewPwdEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(newPwd)
                && !TextUtils.isEmpty(rewPwd)) {
            submitBTN.setEnabled(true);
            submitBTN.setBackgroundColor(getResources().getColor(R.color.default_yellow));
            submitBTN.setTextColor(0xFF111111);
        } else {
            submitBTN.setEnabled(false);
            submitBTN.setBackgroundColor(0xFFE1E1E1);
            submitBTN.setTextColor(0xFFFFFFFF);
        }
    }
}
