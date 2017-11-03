package com.hugboga.custom.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCaptcha;
import com.hugboga.custom.data.request.RequestLoginBycaptcha;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.VoiceCaptchaGetView;
import com.hugboga.custom.widget.VoiceCaptchaInputView;
import com.hugboga.custom.widget.title.TitleBar;
import com.qiyukf.unicorn.api.Unicorn;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import java.util.HashMap;

import butterknife.Bind;

/**
 * Created by qingcha on 17/10/20.
 */

public class VoiceCaptchaActivity extends BaseActivity implements VoiceCaptchaGetView.OnConfirmListener, VoiceCaptchaInputView.InputCompleteListener {

    public static final String PARAM_LAST_REQUEST_TIME  = "param_last_request_time";
    public static final long REQUEST_INTERVAL_TIME  = 60 * 1000;

    @Bind(R.id.vc_titlebar)
    TitleBar titlebar;
    @Bind(R.id.vc_get_view)
    VoiceCaptchaGetView getView;
    @Bind(R.id.vc_input_view)
    VoiceCaptchaInputView inputView;

    private String code;
    private String phone;
    private ActionBean actionBean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_voice_captcha;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            code = savedInstanceState.getString(LoginActivity.KEY_AREA_CODE, "86");
            phone = savedInstanceState.getString(LoginActivity.KEY_PHONE);
            actionBean = (ActionBean) savedInstanceState.getSerializable(Constants.PARAMS_ACTION);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                code = bundle.getString(LoginActivity.KEY_AREA_CODE, "86");
                phone = bundle.getString(LoginActivity.KEY_PHONE);
                actionBean = (ActionBean) bundle.getSerializable(Constants.PARAMS_ACTION);
            }
        }
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LoginActivity.KEY_AREA_CODE, code);
        outState.putString(LoginActivity.KEY_PHONE, phone);
        outState.putSerializable(Constants.PARAMS_ACTION, actionBean);
    }

    @Override
    public void onDestroy() {
        if (inputView != null) {
            inputView.onDestroy();
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(final EventAction action) {
        switch (action.getType()) {
            case CHOOSE_COUNTRY_BACK:
                AreaCodeBean areaCodeBean = (AreaCodeBean) action.getData();
                getView.setAreaCode(areaCodeBean.getCode());
                break;
        }
    }

    private void initView() {
        getView.setAreaCode(code);
        getView.setPhone(phone);
        getView.setOnConfirmListener(this);
        inputView.setInputCompleteListener(this);
        showSoftInput(getView.getPhoneEditText());
        titlebar.setTitleBarBackListener(new TitleBar.OnTitleBarBackListener() {
            @Override
            public boolean onTitleBarBack() {
                return isShowInputView();
            }
        });
        setSensorsDefaultEvent();
    }

    private void showSoftInput(final EditText editText) {
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    editText.requestFocus();
                    imm.showSoftInput(editText, 0);
                }
            }
        }, 100);
    }

    @Override
    public void onConfirm(String _code, String _phone) {
        SensorsUtils.onAppClick(getEventSource(), "获取语音验证码", getIntentSource());
        this.code = _code;
        this.phone = _phone;
        long lastRequestTime = SharedPre.getLong(VoiceCaptchaActivity.PARAM_LAST_REQUEST_TIME, 0);
        long surplus = System.currentTimeMillis() - lastRequestTime;
        if (surplus > VoiceCaptchaActivity.REQUEST_INTERVAL_TIME) {
            getCaptcha();
        } else {
            inputView.setData(_code, _phone, VoiceCaptchaActivity.REQUEST_INTERVAL_TIME - surplus);
            getView.setVisibility(View.GONE);
            showSoftInput(inputView.getCodeEditText());
        }
    }

    @Override
    public void inputComplete(String captcha) {
        RequestLoginBycaptcha request = new RequestLoginBycaptcha(this, code, phone, captcha, 3, 1, 4);
        requestData(request);
        StatisticClickEvent.click(StatisticConstant.LOGIN_CODE, getIntentSource());
    }

    @Override
    public void deleteContent(boolean isDelete) {

    }

    @Override
    public void againRequest() {
        SensorsUtils.onAppClick(getEventSource(), "重新获取验证码", getIntentSource());
        getCaptcha();
    }

    public void showInputView() {
        SharedPre.setLong(VoiceCaptchaActivity.PARAM_LAST_REQUEST_TIME, System.currentTimeMillis());
        inputView.setData(code, phone, VoiceCaptchaActivity.REQUEST_INTERVAL_TIME);
        getView.setVisibility(View.GONE);
        showSoftInput(inputView.getCodeEditText());
        setSensorsDefaultEvent();
    }

    public void getCaptcha() {
        RequestCaptcha requestCaptcha = new RequestCaptcha(this, code, phone, 1);
        requestData(requestCaptcha);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestCaptcha) {
            showInputView();
        } else if (request instanceof RequestLoginBycaptcha) {
            login(request);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (request instanceof RequestLoginBycaptcha) {
            if (isFinishing()) {
                return;
            }
            inputView.clearEditText();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (isShowInputView()) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private boolean isShowInputView() {
        if (inputView.getVisibility() == View.VISIBLE) {
            inputView.setVisibility(View.GONE);
            getView.setVisibility(View.VISIBLE);
            setSensorsDefaultEvent();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getEventSource() {
        if (inputView.getVisibility() == View.VISIBLE) {
            return "语音验证码登录-输入验证码";
        } else {
            return "语音验证码登录-输入手机号";
        }
    }

    @Override
    protected boolean isDefaultEvent() {
        return false;
    }

    @Override
    protected void setSensorsDefaultEvent() {
        SensorsUtils.setPageEvent(getEventSource(), getEventSource(), getIntentSource());
    }

    // 拷贝自LoginActivity，待优化
    private void login(BaseRequest request) {
        RequestLoginBycaptcha mRequest = (RequestLoginBycaptcha) request;
        UserBean user = mRequest.getData();
        user.setUserEntity(activity);
        UserEntity.getUser().setAreaCode(activity, mRequest.areaCode);
        UserEntity.getUser().setPhone(activity, mRequest.mobile);
        UserEntity.getUser().setLoginAreaCode(activity, mRequest.areaCode);
        UserEntity.getUser().setLoginPhone(activity, mRequest.mobile);
        UserSession.getUser().setUserToken(activity, user.userToken);
        UserEntity.getUser().setUserName(activity, user.name);
        UserEntity.getUser().setNimUserId(activity, user.nimUserId);
        UserEntity.getUser().setNimUserToken(activity, user.nimToken);
        UserEntity.getUser().setUnionid(activity, "");
        try {
            SensorsDataAPI.sharedInstance(this).login(user.userID);
            setSensorsUserEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        IMUtil.getInstance().connect();
        Unicorn.setUserInfo(null);
        EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOGIN));
        CommonUtils.loginDoAction(this, actionBean);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("source", getIntentSource());
        map.put("loginstyle", "手机号");
        map.put("head", !TextUtils.isEmpty(user.avatar) ? "是" : "否");
        map.put("nickname", !TextUtils.isEmpty(user.nickname) ? "是" : "否");
        map.put("phone", !TextUtils.isEmpty(user.mobile) ? "是" : "否");
        MobClickUtils.onEvent(StatisticConstant.LOGIN_SUCCEED, map);
        CommonUtils.showToast(R.string.setting_login_succeed);
        CommonUtils.getAgainstDeviceId();
        finish();
    }
}
