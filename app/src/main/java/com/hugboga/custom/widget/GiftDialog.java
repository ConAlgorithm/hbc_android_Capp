package com.hugboga.custom.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
import com.hugboga.custom.activity.ChooseCountryActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.RegisterActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AreaCodeBean;
import com.hugboga.custom.data.bean.CouponActivityBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestAcquirePacket;
import com.hugboga.custom.data.request.RequestCouponActivity;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/12/9.
 */
public class GiftDialog extends Dialog implements HttpRequestListener, HbcViewBehavior, TextWatcher {

    //屏幕中的占比
    private static final float WIDTH_SCALE = 0.77f;

    @Bind(R.id.dialog_gift_parent_layout)
    LinearLayout parentLayout;
    @Bind(R.id.dialog_gift_display_iv)
    ImageView displayIV;
    @Bind(R.id.dialog_gift_title_tv)
    TextView titleTV;
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

    private CouponActivityBean data;
    private boolean isRequestSucceed = false;

    public GiftDialog(Context context) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.view_dialog_gift);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setCanceledOnTouchOutside(true);

        final int screenWidth = UIUtils.getScreenWidth();
        int dialogWidth = (int) (screenWidth * WIDTH_SCALE);
        FrameLayout.LayoutParams dialogParams = new FrameLayout.LayoutParams(dialogWidth, FrameLayout.LayoutParams.WRAP_CONTENT);
        parentLayout.setLayoutParams(dialogParams);

        int imgHeight = (int) (dialogWidth * (205 / 554.0f));
        RelativeLayout.LayoutParams imgParams = new RelativeLayout.LayoutParams(dialogWidth, imgHeight);
        displayIV.setLayoutParams(imgParams);

        phoneET.addTextChangedListener(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.dialog_gift_close_iv, R.id.dialog_gift_phone_code_tv, R.id.dialog_gift_confirm_tv})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.dialog_gift_close_iv:
                dismiss();
                break;
            case R.id.dialog_gift_phone_code_tv:
                intent = new Intent(getContext(), ChooseCountryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_gift_confirm_tv:
                if (isRequestSucceed) {
                    intent = new Intent(getContext(), LoginActivity.class);
                    intent.putExtra(LoginActivity.KEY_AREA_CODE, phoneCodeTV.getText());
                    intent.putExtra(LoginActivity.KEY_PHONE, phoneCodeTV.getText());
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    getContext().startActivity(intent);
                } else {
                    if (CommonUtils.checkTextIsNull(phoneET)) {
                        phoneET.setBackgroundResource(R.drawable.bg_gift_phone_error);
                        errorHintTV.setVisibility(View.VISIBLE);
                        break;
                    }
                    phoneET.setBackgroundResource(R.drawable.bg_gift_phone);
                    errorHintTV.setVisibility(View.INVISIBLE);

//                    phoneET.getText().toString().replaceAll(" ","");
//                  HttpRequestUtils.request(getContext(), new RequestAcquirePacket(), this, true);
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
                phoneCodeTV.setText("+" + areaCodeBean.getCode());//TODO
                break;
        }
    }

    @Override
    public void update(Object _data) {
        CouponActivityBean data = (CouponActivityBean) _data;
        if (data == null) {
            return;
        }
        setTitleTV(data.activityTitle);
        subtitleTV.setText("现在领取，即可在下单时使用");
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
    public void onDataRequestSucceed(BaseRequest request) {
        ApiReportHelper.getInstance().addReport(request);
        if (request instanceof RequestAcquirePacket) {
//            SharedPre.setBoolean(GiftController.PARAMS_GAINED, true);
            //注册
            boolean login = false;

            inputLayout.setVisibility(View.GONE);
            titleTV.setText("领取成功");
            if (login) {
                confirmTV.setText("去登录");
                subtitleTV.setText("登录后即可在\"我的优惠券\"中查看礼包");
            } else {
                confirmTV.setText("去注册");
                subtitleTV.setText("注册后即可在\"我的优惠券\"中查看礼包");
            }
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        ErrorHandler errorHandler = new ErrorHandler((Activity) getContext(), this);
        errorHandler.onDataRequestError(errorInfo, request);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (CommonUtils.checkTextIsNull(phoneET)) {
            confirmTV.setBackgroundResource(R.drawable.shape_rounded_gray_btn);
        } else {
            confirmTV.setBackgroundResource(R.drawable.shape_rounded_yellow_btn);
        }
    }

    public String getEventSource() {
        return "领取礼包弹框";
    }
}
