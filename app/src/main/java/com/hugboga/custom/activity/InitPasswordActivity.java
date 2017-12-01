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
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestChangePwd;
import com.hugboga.custom.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by on 16/11/21.
 */
public class InitPasswordActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.init_password_first_et)
    EditText newPwdEditText;
    @BindView(R.id.init_password_again_et)
    EditText rewPwdEditText;
    @BindView(R.id.init_password_submit_btn)
    Button submitBTN;

    private String oldPassword;
    private ActionBean actionBean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_init_password;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            oldPassword = savedInstanceState.getString(Constants.PARAMS_DATA);
            actionBean = (ActionBean) savedInstanceState.getSerializable(Constants.PARAMS_ACTION);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                oldPassword = bundle.getString(Constants.PARAMS_DATA);
                actionBean = (ActionBean) bundle.getSerializable(Constants.PARAMS_ACTION);
            }
        }

        initDefaultTitleBar();
        fgTitle.setText(R.string.login_set_pwd);
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
        outState.putSerializable(Constants.PARAMS_ACTION, actionBean);
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
            CommonUtils.showToast(R.string.login_change_pwd_success);
            UserEntity.getUser().setWeakPassword(activity, false);
            EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOGIN));
            CommonUtils.loginDoAction(this, actionBean);
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
