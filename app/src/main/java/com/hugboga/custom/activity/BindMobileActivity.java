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

import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestBindMobile;
import com.hugboga.custom.data.request.RequestChangeMobile;
import com.hugboga.custom.data.request.RequestVerity;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.IMUtil;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.SharedPre;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by on 16/8/6.
 */
public class BindMobileActivity extends BaseActivity implements TextWatcher{

    @Bind(R.id.bind_mobile_areacode)
    TextView areaCodeTextView;
    @Bind(R.id.bind_mobile_mobile)
    EditText mobileEditText;
    @Bind(R.id.bind_mobile_verity)
    EditText verityEditText;
    @Bind(R.id.bind_mobile_getcode)
    TextView getCodeBtn;
    @Bind(R.id.bind_mobile_time)
    TextView timeTextView;
    @Bind(R.id.miaoshu2)
    TextView miaoshu2;
    @Bind(R.id.bind_mobile_submit)
    Button login_submit;

    private String areaCode = "";
    private String mobile = "";
    private String unionid = "";
    private boolean isAfterProcess = false;

    public static int REQUEST_CODE = 0x001;
    private SharedPre sharedPre;

    public static String KEY_PHONE = "key_phone";
    public static String KEY_AREA_CODE = "key_area_code";

    @Override
    public int getContentViewId() {
        return R.layout.fg_bind_mobile;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
        requestData();
        initView();
        OrderUtils.genUserAgreeMent(this,miaoshu2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftInput();
    }

    @Override
    public String getEventSource() {
        return "";
    }

    @Override
    public String getEventId() {
        return StatisticConstant.BIND_LAUNCH;
    }

    private void initView() {
        initDefaultTitleBar();
        fgTitle.setText("绑定手机号");
        fgRightTV.setText("跳过");
        fgRightTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(unionid)){
                    UserEntity.getUser().setUnionid(BindMobileActivity.this, unionid);
                    RequestBindMobile request = new RequestBindMobile(BindMobileActivity.this,null,null,null,unionid,"1");
                    requestData(request);
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            unionid = bundle.getString("unionid");
            source = bundle.getString("source");
            isAfterProcess = bundle.getBoolean("isAfterProcess");
        }

        String areaCode = null;
        String phone = null;
        if (getIntent() != null) {
            areaCode = getIntent().getStringExtra(KEY_AREA_CODE);
            mobile = getIntent().getStringExtra(KEY_PHONE);
        }
        sharedPre = new SharedPre(activity);
        if (TextUtils.isEmpty(areaCode)) {
            areaCode = sharedPre.getStringValue(SharedPre.LOGIN_CODE);
        }
        if (!TextUtils.isEmpty(areaCode)) {
            this.areaCode = areaCode;
            areaCodeTextView.setText("+" + areaCode);
        } else {
            this.areaCode = "86";
        }
        if (TextUtils.isEmpty(phone)) {
            phone = sharedPre.getStringValue(SharedPre.LOGIN_PHONE);
        }
        if (!TextUtils.isEmpty(phone)) {
            this.mobile = phone;
            mobileEditText.setText(phone);
        }

        if(isAfterProcess){
            fgRightTV.setVisibility(View.INVISIBLE);
        }else{
            fgRightTV.setVisibility(View.VISIBLE);
            fgLeftBtn.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(UserEntity.getUser().getUnionid(this))) {
            setSensorsDefaultEvent("微信注册绑定手机页", SensorsConstant.WEIXINBIND);
        }

        mobileEditText.addTextChangedListener(this);
        areaCodeTextView.addTextChangedListener(this);
        verityEditText.addTextChangedListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyHandler();
        EventBus.getDefault().unregister(this);
    }

    protected void requestData() {
        StringBuilder sb = new StringBuilder();
        String code = UserEntity.getUser().getAreaCode(BindMobileActivity.this);
        if (!TextUtils.isEmpty(code)) {
            sb.append("+" + code);
        }
        String phone = UserEntity.getUser().getPhone(BindMobileActivity.this);
        if (!TextUtils.isEmpty(phone)) {
            sb.append(phone);
        }
    }

//    @Override
//    public void onFragmentResult(Bundle bundle) {
//        String from = bundle.getString(KEY_FRAGMENT_NAME);
//        if (FgChooseCountry.class.getSimpleName().equals(from)) {
//            String areaCode = bundle.getString(FgChooseCountry.KEY_COUNTRY_CODE);
//            areaCodeTextView.setText("+" + areaCode);
//        }else if(FgSetPassword.class.getSimpleName().equals(from)){
//            Object object = bundle.getSerializable("userBean");
//            UserBean userBean = null;
//            if(object != null && object instanceof UserBean){
//                userBean = (UserBean) object;
//            }
//
//            Bundle bundle1 = new Bundle();
//            bundle1.putString(KEY_FRAGMENT_NAME, FgBindMobile.class.getSimpleName());
//            if(userBean != null){
//                bundle1.putSerializable("userBean",userBean);
//            }
//            finishForResult(bundle1);
//        }
//    }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode != REQUEST_CODE) {
                    break;
                }
                Bundle bundle = data.getExtras();

                Object object = bundle.getSerializable("userBean");
                UserBean userBean = null;
                if(object != null && object instanceof UserBean){
                    userBean = (UserBean) object;
                }

//                Bundle bundle1 = new Bundle();
//                bundle1.putString(KEY_FRAGMENT_NAME, FgBindMobile.class.getSimpleName());
//                if(userBean != null){
//                    bundle1.putSerializable("userBean",userBean);
//                }
//                finishForResult(bundle1);
                destroyHandler();
                finish();
                EventBus.getDefault().post(new EventAction(EventType.BIND_MOBILE, userBean));
            default:
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestVerity) {
            RequestVerity requestVerity = (RequestVerity) request;
            showTip("验证码已发送");
            time = 59;
            handler.postDelayed(runnable, 0);
        } else if(request instanceof RequestBindMobile){
            RequestBindMobile checkMobile = (RequestBindMobile) request;
            UserBean userBean = checkMobile.getData();
            if(userBean.isNotRegister == 1){//未注册，跳转设置密码
                handler.removeCallbacks(runnable);
                setBtnVisible(true);

                Bundle bundle = new Bundle();
                bundle.putString("areaCode",areaCode);
                bundle.putString("mobile",mobile);
                bundle.putString("unionid", unionid);

                if(userBean != null){
                    bundle.putSerializable("userBean",userBean);
                    UserEntity.getUser().setNickname(this, userBean.nickname);
                    UserEntity.getUser().setAvatar(this, userBean.avatar);
                    userBean.setUserEntity(this);
                    UserSession.getUser().setUserToken(this, userBean.userToken);
                    connectIM();
                    EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOGIN));
                }
                EventBus.getDefault().post(new EventAction(EventType.BIND_MOBILE));
                Intent intent = new Intent(BindMobileActivity.this, SetPasswordActivity.class);
                intent.putExtras(bundle);
                BindMobileActivity.this.startActivityForResult(intent, REQUEST_CODE);
                finish();
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("source", source);
                MobclickAgent.onEvent(this, "bind_succeed", map);

            }else { //注册且登录成功
                userBean.setUserEntity(this);
                UserSession.getUser().setUserToken(this, userBean.userToken);
                connectIM();
                EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOGIN));
                EventBus.getDefault().post(new EventAction(EventType.BIND_MOBILE));
                destroyHandler();
                finish();
            }
        } else if(request instanceof RequestChangeMobile){
            handler.removeCallbacks(runnable);
            setBtnVisible(true);

            //isAfterProcess 绑定手机号
            RequestChangeMobile requestChangeMobile = (RequestChangeMobile) request;
            Bundle bundle = new Bundle();
            bundle.putString("areaCode",areaCode);
            bundle.putString("mobile",mobile);
            bundle.putBoolean("isAfterProcess",isAfterProcess);
            Intent intent = new Intent(BindMobileActivity.this, SetPasswordActivity.class);
            intent.putExtras(bundle);
            finish();
            BindMobileActivity.this.startActivityForResult(intent, REQUEST_CODE);
            EventBus.getDefault().post(new EventAction(EventType.BIND_MOBILE));
            MobClickUtils.onEvent(StatisticConstant.BIND_SUCCEED);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (request instanceof RequestVerity) {
            setBtnVisible(true);
        }
        super.onDataRequestError(errorInfo, request);
    }

    private void connectIM() {
        IMUtil.getInstance().connect();
    }

    Integer time = 59;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time > 0) {
                setBtnVisible(false);
                timeTextView.setText("("+String.valueOf(time--) + "s)");
                handler.postDelayed(this, 1000);
            } else {
                setBtnVisible(true);
                timeTextView.setText("("+String.valueOf(59) + "s)");
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

    @OnClick({R.id.bind_mobile_submit, R.id.bind_mobile_areacode, R.id.bind_mobile_getcode,R.id.delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.delete:
                mobileEditText.setText("");
                break;
            case R.id.bind_mobile_submit:
                //更换手机号
                collapseSoftInputMethod(mobileEditText); //隐藏键盘
                collapseSoftInputMethod(verityEditText);
                areaCode = areaCodeTextView.getText().toString();
                if (TextUtils.isEmpty(areaCode)) {
                    showTip("区号不能为空");
                    return;
                }
                areaCode = areaCode.substring(1);
                mobile = mobileEditText.getText().toString();
                if (TextUtils.isEmpty(mobile)) {
                    showTip("手机号不能为空");
                    return;
                }
                String verity = verityEditText.getText().toString();
                if (TextUtils.isEmpty(verity)) {
                    showTip("验证码不能为空");
                    return;
                }
                if(isAfterProcess){
                    //绑定手机号
                    RequestChangeMobile requestChangeMobile = new RequestChangeMobile(this,areaCode,mobile,verity);
                    requestData(requestChangeMobile);
                }else{
                    RequestBindMobile request = new RequestBindMobile(this,areaCode,mobile,verity,unionid,"0");
                    requestData(request);
                }
                MobClickUtils.onEvent(StatisticConstant.BIND);
                break;
            case R.id.bind_mobile_areacode:
                //选择区号
                collapseSoftInputMethod(mobileEditText); //隐藏键盘
                collapseSoftInputMethod(verityEditText);
                Intent intent = new Intent(BindMobileActivity.this, ChooseCountryActivity.class);
                intent.putExtra(KEY_FROM, "changeMobile");
                startActivity(intent);
                break;
            case R.id.bind_mobile_getcode:
                //获取验证码
                login_submit.setEnabled(false);
                collapseSoftInputMethod(mobileEditText); //隐藏键盘
                collapseSoftInputMethod(verityEditText);
                String areaCode1 = areaCodeTextView.getText().toString();
                if (TextUtils.isEmpty(areaCode1)) {
                    showTip("区号不能为空");
                    setBtnVisible(true);
                    return;
                }
                areaCode1 = areaCode1.substring(1);
                String phone1 = mobileEditText.getText().toString();
                if (TextUtils.isEmpty(phone1)) {
                    showTip("手机号不能为空");
                    setBtnVisible(true);
                    return;
                }
                if(isAfterProcess){
                    RequestVerity requestVerity = new RequestVerity(this, areaCode1, phone1, 5);
                    requestData(requestVerity);
                }else {
                    RequestVerity requestVerity = new RequestVerity(this, areaCode1, phone1, 4);
                    requestData(requestVerity);
                }
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
        String phone = mobileEditText.getText().toString().trim();
        String capthca = verityEditText.getText().toString().trim();
        String areaCode = areaCodeTextView.getText().toString().trim();

        if (!TextUtils.isEmpty(areaCode) && !TextUtils.isEmpty(capthca) && !TextUtils.isEmpty(phone)) {
            login_submit.setEnabled(true);
            login_submit.setBackgroundColor(getResources().getColor(R.color.login_ready));
        } else {
            login_submit.setEnabled(false);
            login_submit.setBackgroundColor(getResources().getColor(R.color.login_unready));
        }
    }
}
