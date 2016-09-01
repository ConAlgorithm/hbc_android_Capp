package com.hugboga.custom.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.SharedPre;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler, View.OnClickListener {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private ImageView resultIV;
    private TextView resultTV;
    private TextView promptTV;
    private TextView leftTV;
    private TextView rightTV;

    private IWXAPI api;
    private boolean isPaySucceed = false;

    private String orderId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_pay_result);
        this.findViewById(R.id.header_left_btn).setVisibility(View.INVISIBLE);
        ((TextView) this.findViewById(R.id.header_title)).setText(getString(R.string.par_result_title));
        resultIV = (ImageView) findViewById(R.id.pay_result_iv);
        resultTV = (TextView) findViewById(R.id.pay_result_tv);
        promptTV = (TextView) findViewById(R.id.par_result_prompt_tv);
        leftTV = (TextView) findViewById(R.id.par_result_left_tv);
        rightTV = (TextView) findViewById(R.id.par_result_right_tv);
        leftTV.setOnClickListener(this);
        rightTV.setOnClickListener(this);

        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);

        SharedPre sharedPre = new SharedPre(WXPayEntryActivity.this);
        orderId = sharedPre.getStringValue(SharedPre.PAY_WECHAT_DATA);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            MLog.e("onPayFinish, errCode = " + resp.errCode + " errStr=" + resp.errStr + " transaction= " +
                    resp.transaction + " openId= " + resp.openId + " resp.getType()=" + resp.getType()+ " --- "+Thread.currentThread());
            isPaySucceed = resp.errCode == BaseResp.ErrCode.ERR_OK;
            if (isPaySucceed) {
                resultIV.setBackgroundResource(R.mipmap.payment_success);
                resultTV.setTextColor(0xFF7CBD55);
                resultTV.setText(getString(R.string.par_result_succeed));
                leftTV.setText(getString(R.string.par_result_back));
                rightTV.setText(getString(R.string.par_result_detail));
                promptTV.setVisibility(View.VISIBLE);
            } else {
                resultIV.setBackgroundResource(R.mipmap.payment_fail);
                resultTV.setTextColor(0xFFC94449);
                resultTV.setText(getString(R.string.par_result_failure));
                leftTV.setText(getString(R.string.par_result_detail));
                rightTV.setText(getString(R.string.par_result_repay));
                promptTV.setVisibility(View.GONE);
            }
            EventBus.getDefault().post(new EventAction(EventType.PAY_RESULT, isPaySucceed));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.par_result_left_tv:
                setStatisticIsRePay(false);
                if (TextUtils.isEmpty(orderId)) {
                    if (isPaySucceed) {//回首页
                        EventBus.getDefault().post(new EventAction(EventType.BACK_HOME));
                        EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
                        finish();
                    } else {//订单详情ORDER_DETAIL
                        EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL));
                        finish();
                    }
                } else {
                    if (isPaySucceed) {//回首页
                        intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
                        EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
                    } else {//订单详情
                        intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));

                        OrderDetailActivity.Params orderParams = new OrderDetailActivity.Params();
                        orderParams.orderId = orderId;
                        intent = new Intent(this, OrderDetailActivity.class);
                        intent.putExtra(Constants.PARAMS_DATA, orderParams);
                        intent.putExtra(Constants.PARAMS_SOURCE,getIntentSource());
                        startActivity(intent);
                    }
                }
                break;
            case R.id.par_result_right_tv:
                if (TextUtils.isEmpty(orderId)) {
                    if (isPaySucceed) {//订单详情
                        EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL, 1));
                        EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
                        setStatisticIsRePay(false);
                        finish();
                    } else {//重新支付
                        setStatisticIsRePay(true);
                        finish();
                    }
                } else {
                    if (isPaySucceed) {//订单详情
                        setStatisticIsRePay(false);
                        intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
                        EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));

                        OrderDetailActivity.Params orderParams = new OrderDetailActivity.Params();
                        orderParams.orderId = orderId;
                        intent = new Intent(this, OrderDetailActivity.class);
                        intent.putExtra(Constants.PARAMS_DATA, orderParams);
                        startActivity(intent);
                    } else {//重新支付
                        setStatisticIsRePay(true);
                        finish();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (isPaySucceed) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
                EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
                return true;
            } else {
                setStatisticIsRePay(true);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void setStatisticIsRePay(boolean isRePay) {
        EventUtil eventUtil = EventUtil.getInstance();
        eventUtil.isRePay = isRePay;
    }

}