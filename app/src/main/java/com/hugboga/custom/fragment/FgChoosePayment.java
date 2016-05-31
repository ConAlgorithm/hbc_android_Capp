package com.hugboga.custom.fragment;

import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by qingcha on 16/5/31.
 */
@ContentView(R.layout.fg_choose_payment)
public class FgChoosePayment extends BaseFragment {

    @ViewInject(R.id.choose_payment_price_tv)
    TextView priceTV;


    @Override
    protected void initHeader() {
        fgTitle.setText(getString(R.string.guide_detail_subtitle_title));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Event({R.id.choose_payment_alipay_layout, R.id.choose_payment_wechat_layout,})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.choose_payment_alipay_layout:
                break;
            case R.id.choose_payment_wechat_layout:
                break;
        }
    }
}
