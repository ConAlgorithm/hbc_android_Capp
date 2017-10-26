package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestChangeMobile;
import com.hugboga.custom.data.request.RequestVerity;
import com.hugboga.custom.utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by on 16/8/6.
 */
public class ChangeMobileActivtiy extends BaseActivity implements TextWatcher{

    @Bind(R.id.change_mobile_phone_view)
    TextView phoneTextView;
    @Bind(R.id.change_mobile_areacode)
    TextView areaCodeTextView;
    @Bind(R.id.change_mobile_mobile)
    EditText mobileEditText;
    @Bind(R.id.change_mobile_verity)
    EditText verityEditText;
    @Bind(R.id.change_mobile_getcode)
    TextView getCodeBtn;
    @Bind(R.id.change_mobile_time)
    TextView timeTextView;
    @Bind(R.id.change_mobile_submit)
    Button changMobileSubnit;

    @Override
    public int getContentViewId() {
        return R.layout.fg_change_mobile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        requestData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyHandler();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftInput();
    }

    private void initView() {
        initDefaultTitleBar();
        if(mobileEditText!=null){
            if(mobileEditText.getText().toString().length() >0){
                getCodeBtn.setTextColor(getResources().getColor(R.color.forget_pwd));
            }else{
                getCodeBtn.setTextColor(getResources().getColor(R.color.common_font_color_gray));
            }
        }
        mobileEditText.addTextChangedListener(this);
        areaCodeTextView.addTextChangedListener(this);
        verityEditText.addTextChangedListener(this);
        fgTitle.setText(R.string.login_change_mobile);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CHOOSE_COUNTRY_BACK:
                if (!(action.getData() instanceof AreaCodeBean)) {
                    break;
                }
                AreaCodeBean areaCodeBean = (AreaCodeBean) action.getData();
                if (areaCodeBean == null) {
                    break;
                }
                areaCodeTextView.setText("+" + areaCodeBean.getCode());
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestChangeMobile) {
            RequestChangeMobile requestChangeMobile = (RequestChangeMobile) request;
            UserEntity.getUser().setAreaCode(this, requestChangeMobile.areaCode);
            UserEntity.getUser().setPhone(this, requestChangeMobile.mobile);
            UserEntity.getUser().setLoginAreaCode(this, requestChangeMobile.areaCode);
            UserEntity.getUser().setLoginPhone(this, requestChangeMobile.mobile);
            showTip(CommonUtils.getString(R.string.login_change_mobile_toast1));
//            Bundle bundle = new Bundle();
//            bundle.putString(KEY_FRAGMENT_NAME, FgChangeMobile.class.getSimpleName());
//            finishForResult(bundle);
            destroyHandler();
            EventBus.getDefault().post(new EventAction(EventType.CHANGE_MOBILE));
            finish();
//            notifyFragment(FgSetting.class, null);
//            notifyFragment(FgPersonCenter.class,null);
        } else if (request instanceof RequestVerity) {
            RequestVerity requestVerity = (RequestVerity) request;
            showTip(CommonUtils.getString(R.string.login_change_mobile_toast2));
            time = 59;
            handler.postDelayed(runnable, 0);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (request instanceof RequestVerity) {
            setBtnVisible(true);
        }
        super.onDataRequestError(errorInfo, request);
    }

    Integer time = 59;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time > 0) {
                setBtnVisible(false);
                timeTextView.setText(String.valueOf(time--) + "秒");
                handler.postDelayed(this, 1000);
            } else {
                setBtnVisible(true);
                timeTextView.setText(String.valueOf(59) + "秒");
            }

        }
    };

    private void destroyHandler() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            handler = null;
            runnable = null;
        }
    }

    @OnClick({R.id.change_mobile_submit, R.id.change_mobile_areacode, R.id.change_mobile_getcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_mobile_submit:
                //更换手机号
                collapseSoftInputMethod(mobileEditText); //隐藏键盘
                collapseSoftInputMethod(verityEditText);
                String areaCode = areaCodeTextView.getText().toString();
                if (TextUtils.isEmpty(areaCode)) {
                    showTip(CommonUtils.getString(R.string.login_change_mobile_check1));
                    return;
                }
                areaCode = areaCode.substring(1);
                String phone = mobileEditText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showTip(CommonUtils.getString(R.string.login_change_mobile_check2));
                    return;
                }
                String verity = verityEditText.getText().toString();
                if (TextUtils.isEmpty(verity)) {
                    showTip(CommonUtils.getString(R.string.login_change_mobile_check3));
                    return;
                }
                if (TextUtils.equals(phone, UserEntity.getUser().getPhone(this)) && TextUtils.equals(phone, areaCode)) {
                    showTip(CommonUtils.getString(R.string.login_change_mobile_check4));
                    return;
                }

                RequestChangeMobile changeMobile = new RequestChangeMobile(this, areaCode, phone, verity);
                requestData(changeMobile);
                break;
            case R.id.change_mobile_areacode:
                //选择区号
                collapseSoftInputMethod(mobileEditText); //隐藏键盘
                collapseSoftInputMethod(verityEditText);
                Intent intent = new Intent(ChangeMobileActivtiy.this, ChooseCountryActivity.class);
                intent.putExtra(KEY_FROM, "changeMobile");
                startActivity(intent);
                break;
            case R.id.change_mobile_getcode:
                //获取验证码
                collapseSoftInputMethod(mobileEditText); //隐藏键盘
                collapseSoftInputMethod(verityEditText);
                String areaCode1 = areaCodeTextView.getText().toString();
                if (TextUtils.isEmpty(areaCode1)) {
                    showTip(CommonUtils.getString(R.string.login_change_mobile_check1));
                    setBtnVisible(true);
                    return;
                }
                areaCode1 = areaCode1.substring(1);
                String phone1 = mobileEditText.getText().toString();
                if (TextUtils.isEmpty(phone1)) {
                    showTip(CommonUtils.getString(R.string.login_change_mobile_check2));
                    setBtnVisible(true);
                    return;
                }
                if (TextUtils.equals(phone1, UserEntity.getUser().getPhone(this))
                        && TextUtils.equals(CommonUtils.removePhoneCodeSign(areaCode1), CommonUtils.removePhoneCodeSign(UserEntity.getUser().getAreaCode(this)))) {
                    showTip(CommonUtils.getString(R.string.login_change_mobile_check4));
                    setBtnVisible(true);
                    return;
                }
                RequestVerity requestVerity = new RequestVerity(this, areaCode1, phone1, 5);
                requestData(requestVerity);
                break;
            default:
                break;
        }
    }

    /**
     * 设置按钮是否可以点击
     *
     * @param isClick
     */
    private void setBtnVisible(boolean isClick) {
        if (isClick) {
            getCodeBtn.setVisibility(View.VISIBLE);
            timeTextView.setVisibility(View.GONE);
        } else {
            getCodeBtn.setVisibility(View.GONE);
            timeTextView.setVisibility(View.VISIBLE);
        }
    }

    protected int getBusinessType() {
        return Constants.BUSINESS_TYPE_OTHER;
    }

    private void requestData() {
        StringBuilder sb = new StringBuilder();
        String code = UserEntity.getUser().getAreaCode(this);
        if (!TextUtils.isEmpty(code)) {
            sb.append("+" + code);
        }
        String phone = UserEntity.getUser().getPhone(this);
        if (!TextUtils.isEmpty(phone)) {
            sb.append(phone);
        }
        phoneTextView.setText(CommonUtils.getString(R.string.login_change_mobile_current) + sb.toString());
    }

    private void showTip(String tips) {
        CommonUtils.showToast(tips);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(mobileEditText.length() >0){
            getCodeBtn.setTextColor(getResources().getColor(R.color.forget_pwd));
        }else{
            getCodeBtn.setTextColor(getResources().getColor(R.color.common_font_color_gray));
        }

        if (!TextUtils.isEmpty(areaCodeTextView.getText().toString().trim()) &&
                !TextUtils.isEmpty(verityEditText.getText().toString().trim()) &&
                !TextUtils.isEmpty(mobileEditText.getText().toString().trim())) {
            changMobileSubnit.setEnabled(true);
            //login_submit.setBackgroundColor(getResources().getColor(R.color.login_ready));
        } else {
            changMobileSubnit.setEnabled(false);
            //login_submit.setBackgroundColor(getResources().getColor(R.color.login_unready));
        }

    }
}
