package com.hugboga.custom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CouponPayResultView;
import com.hugboga.custom.widget.PayResultView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;

/**
 * Created by qingcha on 16/9/6.
 */
public class FgPayResult extends BaseFragment{

    @Bind(R.id.fg_result_view)
    PayResultView payResultView;
    @Bind(R.id.fg_coupon_result_view)
    CouponPayResultView couponPayResultView;

    private boolean isPaySucceed;
    public int apiType;//0：正常  1：买券

    @Override
    public int getContentViewId() {
        return R.layout.fg_payresult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    protected void initView() {
        fgTitle.setText(getString(R.string.par_result_title));
    }

    public void initView(boolean _isPaySucceed, String _orderId, int orderType) {
        this.apiType = 0;
        this.isPaySucceed = _isPaySucceed;
        couponPayResultView.setVisibility(View.GONE);
        payResultView.setVisibility(View.VISIBLE);
        payResultView.initView(_isPaySucceed, _orderId, orderType);

        fgLeftBtn.setOnClickListener(null);
        fgLeftBtn.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams titleLeftBtnParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(10), RelativeLayout.LayoutParams.MATCH_PARENT);
        fgLeftBtn.setLayoutParams(titleLeftBtnParams);
    }

    public void initCouponView(boolean _isPaySucceed) {
        this.apiType = 1;
        this.isPaySucceed = _isPaySucceed;
        payResultView.setVisibility(View.GONE);
        couponPayResultView.setVisibility(View.VISIBLE);
        couponPayResultView.initView(_isPaySucceed);

        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
                if (isPaySucceed) {
                    EventBus.getDefault().post(EventType.ACTIVITY_FINISH);
                }
            }
        });
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (apiType == 1) {
                if (isPaySucceed) {
                    EventBus.getDefault().post(EventType.ACTIVITY_FINISH);
                }
            } else {
                if (isPaySucceed) {
                    payResultView.intentHome();
                    return true;
                } else {
                    payResultView.setStatisticIsRePay(true);
                }
            }
        }
        return false;
    }
}
