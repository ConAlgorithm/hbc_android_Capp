package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by qingcha on 16/6/2.
 */
public class OrderDetailTitleBar extends LinearLayout implements HbcViewBehavior, View.OnClickListener {

    private TextView titleTV;
    private ImageView serviceIV, otherIV;
    private ArrayMap<Integer, Integer> titleTextMap;
    private String orderNo;

    public OrderDetailTitleBar(Context context) {
        this(context, null);
    }

    public OrderDetailTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.include_header_detail, this);

        titleTV = (TextView) findViewById(R.id.header_detail_title_tv);
        titleTV.setText(getString(R.string.order_detail_title_default));

        findViewById(R.id.header_detail_back_btn).setOnClickListener(this);

        serviceIV = (ImageView) findViewById(R.id.header_detail_right_2_btn);
        serviceIV.setImageResource(R.mipmap.topbar_cs);
        serviceIV.setPadding(0, 0, 0, 0);
        serviceIV.setOnClickListener(this);
        RelativeLayout.LayoutParams serviceIVParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(38), UIUtils.dip2px(38));
        serviceIVParams.addRule(RelativeLayout.LEFT_OF, R.id.header_detail_right_1_btn);
        serviceIVParams.addRule(RelativeLayout.CENTER_VERTICAL);
        serviceIV.setLayoutParams(serviceIVParams);

        otherIV = (ImageView) findViewById(R.id.header_detail_right_1_btn);
        otherIV.setPadding(UIUtils.dip2px(10), UIUtils.dip2px(10), UIUtils.dip2px(10), UIUtils.dip2px(10));
        otherIV.setImageResource(R.mipmap.topbar_more);
        otherIV.setOnClickListener(this);

        //title类型：中文接机、中文送机、包车游、单次接送、路线包车游
        titleTextMap = new ArrayMap<Integer, Integer>(5);
        titleTextMap.put(1, R.string.order_detail_title_pick);
        titleTextMap.put(2, R.string.order_detail_title_send);
        titleTextMap.put(3, R.string.order_detail_title_chartered);
        titleTextMap.put(4, R.string.order_detail_title_single);
        titleTextMap.put(5, R.string.order_detail_title_route);
        titleTextMap.put(6, R.string.order_detail_title_route);
        titleTextMap.put(888, R.string.order_detail_title_chartered);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        OrderBean orderBean = (OrderBean) _data;

        setTitle(orderBean.orderType);
        orderNo = orderBean.orderNo;
    }

    public void setTitle(int orderGoodsType) {
        //设置title
        int titleTextId = R.string.order_detail_title_default;
//        if (titleTextMap.containsKey(orderGoodsType)) {
//            titleTextId = titleTextMap.get(orderGoodsType);
//        }
        titleTV.setText(getString(titleTextId));
    }

    private final String getString(int resId) {
        return getContext().getString(resId);
    }

    @Override
    public void onClick(View v) {
        EventType type = null;
        switch (v.getId()) {
            case R.id.header_detail_back_btn:
                type = EventType.ORDER_DETAIL_BACK;
                break;
            case R.id.header_detail_right_1_btn:
                type = EventType.ORDER_DETAIL_MORE;
                break;
            case R.id.header_detail_right_2_btn:
                type = EventType.ORDER_DETAIL_CALL;
                addSensors("客服");
                break;
        }
        EventBus.getDefault().post(new EventAction(type, orderNo));
    }

    private void addSensors(String content) {
        if (getContext() != null && (getContext() instanceof OrderDetailActivity)) {
            SensorsUtils.onAppClick(((OrderDetailActivity) getContext()).getEventSource(), content,
                    ((OrderDetailActivity) getContext()).getIntentSource());
        }
    }
}
