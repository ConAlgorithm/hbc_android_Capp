package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.utils.DateUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/24.
 * OrderDetailTravelView
 */

public class OrderDetailAllTravelView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.order_detail_all_travel_container)
    LinearLayout containerLayout;

    public OrderDetailAllTravelView(Context context) {
        this(context, null);
    }

    public OrderDetailAllTravelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0xFFFFFFFF);
        setOrientation(LinearLayout.VERTICAL);
        View view = inflate(context, R.layout.view_order_detail_all_travel, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        OrderBean orderBean = (OrderBean) _data;
        containerLayout.removeAllViews();
        ArrayList<CityBean> passCityList = orderBean.passByCity;
        if (passCityList == null) {
            if (orderBean.orderType == 1) {//只接机
                addItemView(DateUtils.orderChooseDateTransform(orderBean.serviceTime), "只接机，航班：" + orderBean.flightNo);
            } else if (orderBean.orderType == 2) {//只送机
                String airportName = TextUtils.isEmpty(orderBean.flightAirportName) ? orderBean.destAddress : orderBean.flightAirportName;
                addItemView(DateUtils.orderChooseDateTransform(orderBean.serviceTime), "只送机，机场：" + airportName);
            }
            return;
        }
        int size = passCityList.size();
        for (int i = 0; i < size; i++) {
            String date = DateUtils.orderChooseDateTransform(DateUtils.getDay(orderBean.serviceTime, i));
            addItemView(date, passCityList.get(i).description);
        }
    }

    private LinearLayout addItemView(String date, String title) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_order_detail_travel_item, null, false);
        TextView dateTV = (TextView) itemView.findViewById(R.id.order_detail_travel_item_date_tv);
        dateTV.setText(date);
        TextView titleTV = (TextView) itemView.findViewById(R.id.order_detail_travel_item_title_tv);
        titleTV.setText(title);
        containerLayout.addView(itemView);
        return itemView;
    }
}
