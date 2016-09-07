package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.KeyEvent;

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

        setContentView(R.layout.activity_pay_result);

        fgPayResult = (FgPayResult)getSupportFragmentManager().findFragmentById(R.id.fgPayResult);
        fgPayResult.initView(params.payResult, params.orderId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
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
