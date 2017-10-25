package com.hugboga.custom.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.VoiceVerifyCodeGetView;
import com.hugboga.custom.widget.VoiceVerifyCodeInputView;
import com.hugboga.custom.widget.title.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;

/**
 * Created by qingcha on 17/10/20.
 */

public class VoiceVerificationCodeActivity extends BaseActivity implements VoiceVerifyCodeGetView.OnConfirmListener, VoiceVerifyCodeInputView.InputCompleteListener {

    public static final String PARAM_LAST_REQUEST_TIME  = "param_last_request_time";
    public static final long REQUEST_INTERVAL_TIME  = 60 * 1000;

    @Bind(R.id.vvc_titlebar)
    TitleBar titlebar;
    @Bind(R.id.vvc_get_view)
    VoiceVerifyCodeGetView getView;
    @Bind(R.id.vvc_input_view)
    VoiceVerifyCodeInputView inputView;

    private String code;
    private String phone;

    @Override
    public int getContentViewId() {
        return R.layout.activity_voice_verification_code;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
        initView();
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
        getView.setOnConfirmListener(this);
        inputView.setInputCompleteListener(this);
    }

    @Override
    public void onConfirm(String _code, String _phone) {
        this.code = _code;
        this.phone = _phone;
        long lastRequestTime = SharedPre.getLong(VoiceVerificationCodeActivity.PARAM_LAST_REQUEST_TIME, 0);
        long surplus = System.currentTimeMillis() - lastRequestTime;
        if (surplus > VoiceVerificationCodeActivity.REQUEST_INTERVAL_TIME) {
            //TODO 发请求
            Log.i("aa", "发请求 倒计时" + System.currentTimeMillis());
            SharedPre.setLong(VoiceVerificationCodeActivity.PARAM_LAST_REQUEST_TIME, System.currentTimeMillis());
            inputView.setData(_code, _phone, VoiceVerificationCodeActivity.REQUEST_INTERVAL_TIME);
            getView.setVisibility(View.GONE);
        } else {
            //继续倒计时
            Log.i("aa", "继续 倒计时 + " + surplus + "  System.currentTimeMillis() " + System.currentTimeMillis() + "  " + lastRequestTime);
            inputView.setData(_code, _phone, VoiceVerificationCodeActivity.REQUEST_INTERVAL_TIME - surplus);
            getView.setVisibility(View.GONE);
        }
    }

    @Override
    public void inputComplete() {

    }

    @Override
    public void deleteContent(boolean isDelete) {

    }

    @Override
    public void againRequest() {
        //TODO 发请求
        Log.i("aa", "againRequest 倒计时 " + System.currentTimeMillis());
        SharedPre.setLong(VoiceVerificationCodeActivity.PARAM_LAST_REQUEST_TIME, System.currentTimeMillis());
        inputView.setData(code, phone, VoiceVerificationCodeActivity.REQUEST_INTERVAL_TIME);
        getView.setVisibility(View.GONE);
    }
}
