package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 支付成功
 * Created by admin on 2015/7/25.
 */
@ContentView(R.layout.fg_pay_result)
public class FgPaySuccess extends BaseFragment {

    @ViewInject(R.id.pay_success_tip)
    private TextView payTip;
    @ViewInject(R.id.pay_success_btn)
    private TextView payBtn;
    private int payType;//0,下单，1，增项费用
    private int orderType;
    private String orderId;


    @Override
    protected void initHeader() {
        fgTitle.setText(Constants.TitleMap.get(mGoodsType));
    }



    protected void initView() {
        payType = getArguments().getInt("pay");
        orderType = getArguments().getInt("orderType");
        orderId = getArguments().getString(FgOrder.KEY_ORDER_ID);
        if(payType==1) {
            payTip.setText("感谢您选择皇包车");
            payBtn.setText("返回行程列表");
        }
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Event({R.id.head_btn_left,R.id.pay_success_btn})
    private void onClickView(View view) {
        Bundle bundle;
        switch (view.getId()){
            case R.id.head_btn_left:
                bundle =new Bundle();
                bundle.putString(KEY_FRAGMENT_NAME, this.getClass().getSimpleName());
                bringToFront(FgTravel.class, bundle);
                break;
            case R.id.pay_success_btn:
                bundle =new Bundle();
                bundle.putString(KEY_FRAGMENT_NAME, this.getClass().getSimpleName());
                if(payType==0){
                    finish();
                    bundle.putInt(KEY_BUSINESS_TYPE, orderType);
                    bundle.putString(FgOrder.KEY_ORDER_ID, orderId);
                    startFragment(new FgOrder(), bundle);
                }else{
                    bringToFront(FgTravel.class,bundle);
                }

                break;
        }
    }
}
