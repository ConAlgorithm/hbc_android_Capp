package com.hugboga.custom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.widget.CouponPayResultView;
import com.hugboga.custom.widget.PayResultView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    public boolean isInWechat = false;//微信好友分享BUG，在微信点击物理返回会调用支付结果页的回调

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

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

        fgLeftBtn.setVisibility(View.GONE);
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        fgTitle.setLayoutParams(titleParams);
        fgTitle.setText(_isPaySucceed ? "支付成功" : "支付失败");

        fgRightTV.setVisibility(View.VISIBLE);
        fgRightTV.setText(isPaySucceed ? "返回首页" : "查看订单");
        fgRightTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaySucceed) {
                    payResultView.intentHome();
                } else {
                    DefaultSSLSocketFactory.resetSSLSocketFactory(getContext());
                    payResultView.intentOrderDetail();
                }
            }
        });
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
                if (isPaySucceed) {
                    getContext().startActivity(new Intent(getContext(), MainActivity.class));
                } else {
                    ((Activity) getContext()).finish();
                }
            }
        });
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isInWechat) {//在微信中不处理该回调
            isInWechat = false;
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (apiType == 1) {
                if (isPaySucceed) {
                    getContext().startActivity(new Intent(getContext(), MainActivity.class));
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

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case WECHAT_SHARE:
                int shareType = (int)action.getData();
                if (shareType == 1) {
                    isInWechat = true;
                }
                break;
        }
    }
}
