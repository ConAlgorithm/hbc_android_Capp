package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BargainActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.iwgang.countdownview.CountdownView;

/**
 * Created by qingcha on 16/9/9.
 */
public class OrderDetailBargainEntr extends RelativeLayout implements HbcViewBehavior, CountdownView.OnCountdownEndListener, View.OnClickListener{

    @Bind(R.id.detail_bargain_entr_layout)
    RelativeLayout parentLayout;
    @Bind(R.id.detail_bargain_entr_title_tv)
    TextView titleTV;
    @Bind(R.id.detail_bargain_entr_price_tv)
    TextView priceTV;
    @Bind(R.id.detail_bargain_entr_countdown_view)
    CountdownView countdownView;

    private String orderNo;

    public OrderDetailBargainEntr(Context context) {
        this(context, null);
    }

    public OrderDetailBargainEntr(Context context, AttributeSet attrs) {
        super(context, attrs);
        final View view = inflate(context, R.layout.view_order_detail_bargain_entr, this);
        ButterKnife.bind(view);
        setOnClickListener(this);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        OrderBean orderBean = (OrderBean) _data;
        this.orderNo = orderBean.orderNo;
        if (orderBean.isShowBargain != 1) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
            switch (orderBean.bargainStatus) {
                case 0://初始状态
                    parentLayout.setBackgroundResource(R.mipmap.bargain_challenge);
                    titleTV.setVisibility(View.GONE);
                    priceTV.setVisibility(View.GONE);
                    countdownView.setVisibility(View.GONE);
                    countdownView.setOnCountdownEndListener(null);
                    countdownView.stop();
                    break;
                case 1://激活
                    parentLayout.setBackgroundResource(R.mipmap.bargain_entr_bg);
                    titleTV.setVisibility(View.VISIBLE);
                    priceTV.setVisibility(View.VISIBLE);
                    titleTV.setText(getContext().getString(R.string.order_detail_bargain));
                    priceTV.setText(orderBean.bargainAmount + getContext().getString(R.string.yuan));
                    countdownView.setVisibility(View.VISIBLE);
                    countdownView.start(orderBean.bargainSeconds * 1000);
                    countdownView.setOnCountdownEndListener(this);
                    break;
                case 2://活动结束
                    parentLayout.setBackgroundResource(R.mipmap.bargain_succeed);
                    titleTV.setVisibility(View.VISIBLE);
                    priceTV.setVisibility(View.VISIBLE);
                    titleTV.setText(getContext().getString(R.string.order_detail_bargain_end));
                    priceTV.setText(orderBean.bargainAmount + getContext().getString(R.string.yuan));
                    countdownView.setVisibility(View.GONE);
                    countdownView.setOnCountdownEndListener(null);
                    countdownView.stop();
                    break;
            }
        }
    }

    @Override
    public void onEnd(CountdownView cv) {
        EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE, orderNo));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), BargainActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.order_detail_title_default));
        intent.putExtra("orderNo", orderNo);
        getContext().startActivity(intent);
    }
}
