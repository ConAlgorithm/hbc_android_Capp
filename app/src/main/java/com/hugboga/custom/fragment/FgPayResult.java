package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import de.greenrobot.event.EventBus;

/**
 * 支付成功
 * Created by admin on 2015/7/25.
 * 微信的回掉WXPayEntryActivity
 */
@ContentView(R.layout.fg_pay_result)
public class FgPayResult extends BaseFragment {

    @ViewInject(R.id.pay_result_iv)
    private ImageView resultIV;
    @ViewInject(R.id.pay_result_tv)
    private TextView resultTV;
    @ViewInject(R.id.par_result_prompt_tv)
    private TextView promptTV;
    @ViewInject(R.id.par_result_left_tv)
    private TextView leftTV;
    @ViewInject(R.id.par_result_right_tv)
    private TextView rightTV;

    private Params params;

    public static class Params implements Serializable {
        public String paymentAmount;//支付金额
        public boolean payResult;//支付结果 1.支付成功，2.支付失败
        public String orderId;
    }

    public static FgPayResult newInstance(Params params) {
        FgPayResult fragment = new FgPayResult();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS_DATA, params);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initHeader(Bundle savedInstanceState) {
        super.initHeader(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params)savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                params = (Params)bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
    }

    @Override
    protected void initHeader() {
        fgTitle.setText(getString(R.string.par_result_title));
        fgLeftBtn.setOnClickListener(null);
        fgLeftBtn.setVisibility(View.INVISIBLE);
        if (params.payResult) {
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
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Event({R.id.par_result_left_tv, R.id.par_result_right_tv})
    private void onClickView(View view) {

        switch (view.getId()){
            case R.id.par_result_left_tv:
                if (params.payResult) {//回首页
                    clearFragmentList();
                    EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
                    EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
                } else {//订单详情
                    clearFragmentList();
                    FgOrderDetail.Params orderParams = new FgOrderDetail.Params();
                    orderParams.orderId = params.orderId;
                    startFragment(FgOrderDetail.newInstance(orderParams));
                }
                break;
            case R.id.par_result_right_tv:
                if (params.payResult) {//订单详情
                    clearFragmentList();
                    FgOrderDetail.Params orderParams = new FgOrderDetail.Params();
                    orderParams.orderId = params.orderId;
                    startFragment(FgOrderDetail.newInstance(orderParams));
                    EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
                } else {//重新支付
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (params.payResult) {
            Bundle bundle =new Bundle();
            bundle.putString(KEY_FRAGMENT_NAME, this.getClass().getSimpleName());
            bringToFront(FgHome.class, bundle);
        } else {
            finish();
        }
        return true;
    }

}
