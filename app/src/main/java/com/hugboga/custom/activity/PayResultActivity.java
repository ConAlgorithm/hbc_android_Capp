package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.PayResultView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/8/4.
 */
public class PayResultActivity extends BaseActivity{

//    @Bind(R.id.activity_result_view)
//    PayResultView resultView;

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
//        ButterKnife.bind(this);

//        initDefaultTitleBar();
//        fgTitle.setText(getString(R.string.par_result_title));
//        fgLeftBtn.setOnClickListener(null);
//        fgLeftBtn.setVisibility(View.INVISIBLE);
//        RelativeLayout.LayoutParams titleLeftBtnParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(10), RelativeLayout.LayoutParams.MATCH_PARENT);
//        fgLeftBtn.setLayoutParams(titleLeftBtnParams);
//
//        resultView.initView(params.payResult, params.orderId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//            if (params.payResult) {
//                resultView.intentHome();
//                return true;
//            } else {
//                resultView.setStatisticIsRePay(true);
//            }
//        }
//        return super.onKeyUp(keyCode, event);
//    }
}
