package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (params.payResult) {
            //FIXME qingcha
//            Bundle bundle =new Bundle();
//            bundle.putString(KEY_FRAGMENT_NAME, this.getClass().getSimpleName());
//            bringToFront(FgHome.class, bundle);
        } else {
            finish();
        }
    }
}
