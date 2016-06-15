package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OrderBean;

/**
 * Created by qingcha on 16/6/2.
 */
public class OrderDetailItineraryView extends LinearLayout implements HbcViewBehavior {

    private LinearLayout itineraryLayout;
    private TextView orderNumberTV;
    private TextView routeTV;
    private TextView carpoolTV;

    public OrderDetailItineraryView(Context context) {
        this(context, null);
    }

    public OrderDetailItineraryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_order_detail_itinerary, this);
        itineraryLayout = (LinearLayout) findViewById(R.id.order_itinerary_item_layout);
        orderNumberTV = (TextView) findViewById(R.id.order_itinerary_order_number_tv);
        routeTV = (TextView) findViewById(R.id.order_itinerary_route_tv);
        carpoolTV = (TextView) findViewById(R.id.order_itinerary_carpool_tv);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        OrderBean orderBean = (OrderBean) _data;
        orderNumberTV.setText(getContext().getString(R.string.order_detail_order_number, orderBean.orderNo));
        itineraryLayout.removeAllViews();

        //精品路线
        if (orderBean.orderGoodsType == 5 && !TextUtils.isEmpty(orderBean.lineSubject)) {
            routeTV.setVisibility(View.VISIBLE);
            routeTV.setText(orderBean.lineSubject);
        } else {
            routeTV.setVisibility(View.GONE);
        }

        carpoolTV.setVisibility(orderBean.carPool ? View.VISIBLE : View.GONE);//拼车

        //"当地时间 04月21日（周五）10:05" orderBean.serviceTime
        String localTime = getContext().getString(R.string.order_detail_local_time, orderBean.serviceTimeStr);
        if (orderBean.orderGoodsType == 3 || orderBean.orderGoodsType == 5 || orderBean.orderGoodsType == 6 || orderBean.orderGoodsType == 7) {
            //主标题：东京-6天包车  副标题：当地时间
            String totalDays = "" + orderBean.totalDays;
            if (orderBean.isHalfDaily == 1) {//半日包
                totalDays = getContext().getString(R.string.order_detail_half_day);
            } else if (orderBean.totalDays > 1 && !TextUtils.isEmpty(orderBean.serviceEndTimeStr)) {
                localTime = localTime + " - " + orderBean.serviceEndTimeStr;
            }
            addItemView(R.mipmap.order_time, getContext().getString(R.string.order_detail_local_chartered, orderBean.serviceCityName, totalDays), null, localTime);
        } else {
            //主标题：当地时间   副标题："航班HKJHKJ 东京-北京"
            String flight = "";
            if (!TextUtils.isEmpty(orderBean.flightNo)) {
                flight = getContext().getString(R.string.order_detail_flight, orderBean.flightNo);
            }
            if (!TextUtils.isEmpty(orderBean.flightDeptCityName) && !TextUtils.isEmpty(orderBean.flightDestCityName)) {
                flight += getContext().getString(R.string.separator, orderBean.flightDeptCityName, orderBean.flightDestCityName);
            }
            addItemView(R.mipmap.order_time, localTime, null, flight);
        }

        if (orderBean.isHalfDaily == 0 && orderBean.passByCity != null && orderBean.passByCity.size() > 0) {//TODO 条件 当前按照旧的逻辑添加
            OrderDetailRouteView routeView = new OrderDetailRouteView(getContext());
            itineraryLayout.addView(routeView);
            routeView.update(orderBean.passByCity);
        }

        if (!TextUtils.isEmpty(orderBean.startAddress)) {//出发地
            addItemView(R.mipmap.order_place, orderBean.startAddress, null, orderBean.startAddressDetail);
        }

        if (!TextUtils.isEmpty(orderBean.destAddress)) {//目的地
            addItemView(R.mipmap.order_flag, orderBean.destAddress, null, orderBean.destAddressDetail);
        }

        if (!TextUtils.isEmpty(orderBean.carDesc)) {//车型描述
            String passengerInfos = null;
            if (!TextUtils.isEmpty(orderBean.passengerInfos)) {
                passengerInfos = getContext().getString(R.string.order_detail_seat_info, orderBean.passengerInfos);
            }
            addItemView(R.mipmap.order_car, orderBean.carDesc, passengerInfos, null);
        }

        if (orderBean.orderGoodsType == 1  && "1".equalsIgnoreCase(orderBean.isFlightSign)) {//接机
            addItemView(R.mipmap.order_jp, getContext().getString(R.string.order_detail_airport_card));
        }

    }

    private void addItemView(int iconId, String title) {
        addItemView(iconId, title, null, null);
    }

    private void addItemView(int iconId, String title, String subtitle, String describe) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_order_detail_itinerary, null, false);

        ImageView iconIV = (ImageView) itemView.findViewById(R.id.item_itinerary_iv);
        iconIV.setBackgroundResource(iconId);
        TextView titleTV = (TextView) itemView.findViewById(R.id.item_itinerary_title_tv);
        titleTV.setText(title);

        TextView subtitleTV = (TextView) itemView.findViewById(R.id.item_itinerary_subtitle_tv);
        if (TextUtils.isEmpty(subtitle)) {
            subtitleTV.setVisibility(View.GONE);
        } else {
            subtitleTV.setVisibility(View.VISIBLE);
            subtitleTV.setText(subtitle);
        }

        TextView describeTV = (TextView) itemView.findViewById(R.id.item_itinerary_describe_tv);
        if (TextUtils.isEmpty(describe)) {
            describeTV.setVisibility(View.GONE);
        } else {
            describeTV.setVisibility(View.VISIBLE);
            describeTV.setText(describe);
        }

        itineraryLayout.addView(itemView);
    }
}
