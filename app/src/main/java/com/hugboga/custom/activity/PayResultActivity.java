package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.Event;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/8/4.
 */
public class PayResultActivity extends BaseActivity{

    @Bind(R.id.pay_result_iv)
    ImageView resultIV;
    @Bind(R.id.pay_result_tv)
    TextView resultTV;
    @Bind(R.id.par_result_prompt_tv)
    TextView promptTV;
    @Bind(R.id.par_result_left_tv)
    TextView leftTV;
    @Bind(R.id.par_result_right_tv)
    TextView rightTV;

    private Params params;

    public static class Params implements Serializable {
        public String paymentAmount;//支付金额
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

        setContentView(R.layout.fg_pay_result);
        ButterKnife.bind(this);
        initDefaultTitleBar();

        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    private void initView() {
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


    @OnClick({R.id.par_result_left_tv, R.id.par_result_right_tv})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.par_result_left_tv:
                if (params.payResult) {//回首页
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
                    EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
                } else {//订单详情
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));

                    OrderDetailActivity.Params orderParams = new OrderDetailActivity.Params();
                    orderParams.orderId = params.orderId;
                    intent = new Intent(this, OrderDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, orderParams);
                    startActivity(intent);
                }
                break;
            case R.id.par_result_right_tv:
                if (params.payResult) {//订单详情
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
                    EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));

                    OrderDetailActivity.Params orderParams = new OrderDetailActivity.Params();
                    orderParams.orderId = params.orderId;
                    intent = new Intent(this, OrderDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, orderParams);
                    startActivity(intent);
                } else {//重新支付
                    finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (params.payResult) {
//            Bundle bundle =new Bundle();
//            bundle.putString(KEY_FRAGMENT_NAME, this.getClass().getSimpleName());
//            bringToFront(FgHome.class, bundle);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
        } else {
            finish();
        }
    }
}
