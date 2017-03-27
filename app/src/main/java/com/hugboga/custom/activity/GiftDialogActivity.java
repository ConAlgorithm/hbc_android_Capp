package com.hugboga.custom.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AcquirePacketBean;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.CouponActivityBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestAcquirePacket;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.GiftController;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/9.
 */
public class GiftDialogActivity extends Activity implements HttpRequestListener {

    //屏幕中的占比
    private static final float WIDTH_SCALE = 0.77f;

    @Bind(R.id.dialog_gift_parent_layout)
    LinearLayout parentLayout;
    @Bind(R.id.dialog_gift_display_iv)
    ImageView displayIV;
    @Bind(R.id.dialog_gift_title_tv)
    TextView titleTV;
    @Bind(R.id.dialog_gift_first_register_tv)
    TextView firstRegisterTV;
    @Bind(R.id.dialog_gift_subtitle_tv)
    TextView subtitleTV;
    @Bind(R.id.dialog_gift_phone_code_tv)
    TextView phoneCodeTV;
    @Bind(R.id.dialog_gift_error_hint_tv)
    TextView errorHintTV;
    @Bind(R.id.dialog_gift_phone_et)
    EditText phoneET;
    @Bind(R.id.dialog_gift_confirm_tv)
    TextView confirmTV;
    @Bind(R.id.dialog_gift_input_layout)
    LinearLayout inputLayout;

    private boolean isRequestSucceed = false;
    private CouponActivityBean couponActivityBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            couponActivityBean = (CouponActivityBean) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                couponActivityBean = (CouponActivityBean) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        if (couponActivityBean == null || couponActivityBean.couponActiviyVo == null) {
            finish();
        }
        setContentView(R.layout.view_dialog_gift);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        final int screenWidth = UIUtils.getScreenWidth();
        int dialogWidth = (int) (screenWidth * WIDTH_SCALE);
        FrameLayout.LayoutParams dialogParams = new FrameLayout.LayoutParams(dialogWidth, FrameLayout.LayoutParams.WRAP_CONTENT);
        parentLayout.setLayoutParams(dialogParams);

        int imgHeight = (int) (dialogWidth * (205 / 554.0f));
        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(dialogWidth, imgHeight);
        displayIV.setLayoutParams(imgParams);

        setTitleTV(couponActivityBean.couponActiviyVo.activityTitle);
        subtitleTV.setText("现在领取，即可在下单时使用");

        setSensorsCouponShow();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.PARAMS_DATA, couponActivityBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @OnClick({R.id.dialog_gift_close_iv, R.id.dialog_gift_phone_code_tv, R.id.dialog_gift_confirm_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_gift_close_iv:
                finish();
                break;
            case R.id.dialog_gift_phone_code_tv:
                startActivity(new Intent(this, ChooseCountryActivity.class));
                break;
            case R.id.dialog_gift_confirm_tv:
                if (isRequestSucceed) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(LoginActivity.KEY_AREA_CODE, CommonUtils.removePhoneCodeSign(phoneCodeTV.getText().toString()));
                    intent.putExtra(LoginActivity.KEY_PHONE, phoneET.getText().toString().replaceAll(" ",""));
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    startActivity(intent);
                    setSensorsLogin();
                    finish();
                } else {
                    if (CommonUtils.checkTextIsNull(phoneET)) {
                        phoneET.setBackgroundResource(R.drawable.bg_gift_phone_error);
                        errorHintTV.setVisibility(View.VISIBLE);
                        break;
                    }
                    String code = CommonUtils.removePhoneCodeSign(phoneCodeTV.getText().toString());
                    String phone = phoneET.getText().toString().replaceAll(" ","");
                    if (code.equals("86") && phone.length() != 11) { //验证国内手机号
                        phoneET.setBackgroundResource(R.drawable.bg_gift_phone_error);
                        errorHintTV.setVisibility(View.VISIBLE);
                        break;
                    }
                    phoneET.setBackgroundResource(R.drawable.bg_gift_phone);
                    errorHintTV.setVisibility(View.INVISIBLE);
                    HttpRequestUtils.request(this, new RequestAcquirePacket(this, code, phone), this, true);
                    isRequestSucceed = false;
                    CommonUtils.hideSoftInputMethod(phoneET);
                    setSensorsCouponGet();
                }
                break;
        }
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
                String areaCode = areaCodeBean.getCode();
                if (!"86".equals(areaCode)) {
                    phoneET.setBackgroundResource(R.drawable.bg_gift_phone);
                    errorHintTV.setVisibility(View.INVISIBLE);
                }
                phoneCodeTV.setText(CommonUtils.addPhoneCodeSign(areaCode));
                break;
        }
    }

    private void setTitleTV(String _title) {
        if (TextUtils.isEmpty(_title)) {
            return;
        }
        Pattern pattern = Pattern.compile("[^0-9]");
        String str = pattern.matcher(_title).replaceAll("");
        if (TextUtils.isEmpty(str)) {
            titleTV.setText(_title);
        } else {
            int startIndex = _title.lastIndexOf(str);
            int endIndex = _title.lastIndexOf(str) + str.length() + 1;
            SpannableStringBuilder ssb = new SpannableStringBuilder(_title);
            ForegroundColorSpan yellowSpan = new ForegroundColorSpan(Color.parseColor("#FF6600"));
            ssb.setSpan(yellowSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            titleTV.setText(ssb);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        ApiReportHelper.getInstance().addReport(_request);
        if (_request instanceof RequestAcquirePacket) {
            SharedPre.setBoolean(GiftController.PARAMS_GAINED, true);
            RequestAcquirePacket request = (RequestAcquirePacket)_request;
            AcquirePacketBean acquirePacketBean = request.getData();
            if (acquirePacketBean.registState == 1) {//新用户注册
                firstRegisterTV.setVisibility(View.VISIBLE);
            }
            inputLayout.setVisibility(View.GONE);
            titleTV.setText("领取成功");
            confirmTV.setText("去登录");
            subtitleTV.setText("登录后即可在\"我的优惠券\"中查看礼包");
            isRequestSucceed = true;
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        ErrorHandler errorHandler = new ErrorHandler(this, this);
        errorHandler.onDataRequestError(errorInfo, request);
    }

    public String getEventSource() {
        return "领取礼包弹框";
    }

    //神策统计_未登录弹层显示
    public void setSensorsCouponShow() {
        try {
            SensorsDataAPI.sharedInstance(this).track("coupon_show", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //神策统计_未登录领券填写手机
    public void setSensorsCouponGet() {
        JSONObject properties = new JSONObject();
        try {
            properties.put("hbc_areacode", phoneCodeTV.getText().toString().trim());
            properties.put("hbc_tel", phoneET.getText().toString().trim());
            SensorsDataAPI.sharedInstance(this).track("coupon_get", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //神策统计_未登录领券登录
    public void setSensorsLogin() {
        try {
            SensorsDataAPI.sharedInstance(this).track("coupon_login", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
