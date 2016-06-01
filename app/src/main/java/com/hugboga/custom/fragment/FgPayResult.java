package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;

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

//    @ViewInject(R.id.pay_success_tip)
//    private TextView payTip;
//    @ViewInject(R.id.pay_success_btn)
//    private TextView payBtn;
//    private int payType;//0,下单，1，增项费用
//    private int orderType;
//    private String orderId;

    @ViewInject(R.id.pay_result_iv)
    private ImageView resultIV;
    @ViewInject(R.id.pay_result_tv)
    private TextView resultTV;
    @ViewInject(R.id.par_result_sum_tv)
    private TextView sumTV;
    @ViewInject(R.id.par_result_prompt_tv)
    private TextView promptTV;
    @ViewInject(R.id.par_result_left_tv)
    private TextView leftTV;
    @ViewInject(R.id.par_result_right_tv)
    private TextView rightTV;


    private Params params;

    public static FgPayResult newInstance(Params params) {
        FgPayResult fragment = new FgPayResult();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS_DATA, params);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initHeader(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            params = (Params)savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                params = (Params)bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        fgTitle.setText(getString(R.string.par_result_title));
        fgLeftBtn.setOnClickListener(null);
        fgLeftBtn.setVisibility(View.INVISIBLE);
        sumTV.setText(getString(R.string.sign_rmb) + params.paymentAmount);
        if (params.payResult) {
            resultIV.setBackgroundResource(R.mipmap.payment_success);
            resultTV.setText(getString(R.string.par_result_succeed));
            promptTV.setText(getString(R.string.par_result_succeed_prompt));
            leftTV.setText(getString(R.string.par_result_back));
            rightTV.setText(getString(R.string.par_result_detail));
        } else {
            resultIV.setBackgroundResource(R.mipmap.payment_fail);
            resultTV.setText(getString(R.string.par_result_failure));
            promptTV.setText(getString(R.string.par_result_failure_prompt));
            leftTV.setText(getString(R.string.par_result_detail));
            rightTV.setText(getString(R.string.par_result_repay));
        }
    }

    @Override
    protected void initHeader() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }



    protected void initView() {
//        payType = getArguments().getInt("pay");
//        orderType = getArguments().getInt("orderType");
//        orderId = getArguments().getString(FgOrder.KEY_ORDER_ID);
//        if(payType==1) {
//            payTip.setText("感谢您选择皇包车");
//            payBtn.setText("返回行程列表");
//        }
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
        Bundle bundle =new Bundle();
        bundle.putString(KEY_FRAGMENT_NAME, this.getClass().getSimpleName());
        switch (view.getId()){
            case R.id.par_result_left_tv:
                if (params.payResult) {//回首页
                    bringToFront(FgTravel.class, bundle);
                } else {//订单详情
                    bringToFront(FgAbout.class, bundle);// FIXME: qingcha
                }
                break;
            case R.id.par_result_right_tv:
                if (params.payResult) {//订单详情
                    bringToFront(FgAbout.class, new Bundle());// FIXME: qingcha
                } else {//重新支付
                    finish();
                }
                break;
        }
    }

    public static class Params implements Serializable {
        public String paymentAmount;//支付金额
        public boolean payResult;//支付结果 1.支付成功，2.支付失败
    }

    @Override
    public boolean onBackPressed() {// FIXME: qingcha
        return true;
    }
}
