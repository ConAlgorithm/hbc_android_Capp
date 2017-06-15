package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.fragment.FgPayResult;
import java.io.Serializable;

/**
 * Created by qingcha on 16/8/4.
 */
public class PayResultActivity extends BaseActivity{

    private FgPayResult fgPayResult;
    private Params params;

    public static class Params implements Serializable {
        public boolean payResult;//支付结果 1.支付成功，2.支付失败
        public String orderId;
        public int orderType;

        public int apiType;//0：正常  1：买券
        public String couponAreaCode;//买劵人手机区号
        public String couponPhone;//买劵人手机号
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_pay_result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params)savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (Params)bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        fgPayResult = (FgPayResult)getSupportFragmentManager().findFragmentById(R.id.fgPayResult);
        if (params.apiType == 1) {
            fgPayResult.initCouponView(params.payResult, params.couponAreaCode, params.couponPhone);
        } else {
            fgPayResult.initView(params.payResult, params.orderId, params.orderType);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DefaultSSLSocketFactory.resetSSLSocketFactory(this);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (fgPayResult != null) {
            return fgPayResult.onKeyUp(keyCode, event) ? true : super.onKeyUp(keyCode, event);
        }
        return super.onKeyUp(keyCode, event);
    }
}
