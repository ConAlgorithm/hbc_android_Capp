package com.hugboga.custom.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.fragment.FgPayResult;
import com.hugboga.custom.utils.SharedPre;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {


    private FgPayResult fgPayResult;
    private IWXAPI api;

    private boolean isPaySucceed = false;
    private String orderId;
    private int orderType;
    public int apiType;//0：正常  1：买券

    @Override
    public int getContentViewId() {
        return R.layout.activity_pay_result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fgPayResult = (FgPayResult)getSupportFragmentManager().findFragmentById(R.id.fgPayResult);

        SharedPre sharedPre = new SharedPre(WXPayEntryActivity.this);
        orderId = sharedPre.getStringValue(SharedPre.PAY_WECHAT_ORDER_ID);
        orderType = sharedPre.getIntValue(SharedPre.PAY_WECHAT_ORDER_TYPE);
        apiType = sharedPre.getIntValue(SharedPre.PAY_WECHAT_APITYPE, 0);

        api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DefaultSSLSocketFactory.resetSSLSocketFactory(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq resq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            isPaySucceed = resp.errCode == BaseResp.ErrCode.ERR_OK;
            if (apiType == 1) {
                fgPayResult.initCouponView(isPaySucceed);
            } else {
                fgPayResult.initView(isPaySucceed, orderId, orderType);
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (fgPayResult != null) {
            return fgPayResult.onKeyUp(keyCode, event) ? true : super.onKeyUp(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }

}