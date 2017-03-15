package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.LuggageInfoActivity;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/6/2.
 */
public class OrderDetailItineraryView extends LinearLayout implements HbcViewBehavior, View.OnClickListener{

    @Bind(R.id.order_itinerary_item_layout)
    LinearLayout itineraryLayout;
    @Bind(R.id.order_itinerary_order_number_view)
    OrderDetailNoView orderNumberView;

    @Bind(R.id.order_itinerary_route_tv)
    TextView routeTV;
    @Bind(R.id.order_itinerary_route_iv)
    ImageView routeIV;
    @Bind(R.id.order_itinerary_route_tag_iv)
    ImageView routeTagIV;
    @Bind(R.id.order_itinerary_route_layout)
    RelativeLayout routeLayout;

    @Bind(R.id.order_itinerary_charter_layout)
    RelativeLayout charterLayout;
    @Bind(R.id.order_itinerary_charter_city_tv)
    TextView cityTV;

    private OrderBean orderBean;

    public OrderDetailItineraryView(Context context) {
        this(context, null);
    }

    public OrderDetailItineraryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_itinerary, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        orderBean = (OrderBean) _data;
        itineraryLayout.removeAllViews();

        // 包车开始城市
        if (orderBean.orderType == 3 || orderBean.orderType == 888) {
            charterLayout.setVisibility(View.VISIBLE);
            cityTV.setText(orderBean.serviceCityName + " - " + orderBean.serviceEndCityName);
        } else {
            charterLayout.setVisibility(View.GONE);
        }

        // 固定线路 超省心、推荐路线 超自由
        if ((orderBean.orderType == 5 || orderBean.orderType == 6) && !TextUtils.isEmpty(orderBean.lineSubject)) {
            setRouteLayoutVisible();
        } else {
            routeLayout.setVisibility(View.GONE);
            routeLayout.setOnClickListener(null);
        }

        // 游玩日期
        if (orderBean.orderType == 3 || orderBean.orderType == 888 || orderBean.orderType == 5 || orderBean.orderType == 6) {
            String startDate = DateUtils.getPointStrFromDate2(orderBean.serviceTime);
            String totalDays = "" + orderBean.totalDays;
            if (orderBean.isHalfDaily == 1) {//半日包
                totalDays = getContext().getString(R.string.order_detail_half_day);
            } else if (orderBean.totalDays > 1 && !TextUtils.isEmpty(orderBean.serviceEndTime)) {
                startDate = startDate + " - " + DateUtils.getPointStrFromDate2(orderBean.serviceEndTime);
            }
            startDate += String.format("(%1$s天)", totalDays);
            addItemView(R.mipmap.trip_icon_date, startDate);
        } else {
            String startDate = DateUtils.getStrWeekFormat3(orderBean.serviceTime);
            if (!TextUtils.isEmpty(orderBean.serviceStartTime)) {
                startDate += " " + orderBean.serviceStartTime.substring(0, orderBean.serviceStartTime.lastIndexOf(":00"));
            }
            addItemView(R.mipmap.trip_icon_time, startDate);
        }


        if (orderBean.orderType == 5 || orderBean.orderType == 6) {//线路 开始城市 - 结束城市
            addItemView(R.mipmap.trip_icon_line, orderBean.serviceCityName + " - " + orderBean.serviceEndCityName);
        } else if (orderBean.orderType == 1 || orderBean.orderType == 2 || orderBean.orderType == 4) {//开始地点 - 结束地点 接机、送机、单次
            addLocationItem();//TODO 细节
        }

        // 车型描述
        String childSeatCount = "";
        if (orderBean.childSeats != null && orderBean.childSeats.getChildSeatCount() > 0) {//儿童座椅数
            childSeatCount = String.format("儿童座椅%1$s个", orderBean.childSeats.getChildSeatCount());
        }
        addItemView(R.mipmap.trip_icon_car, orderBean.carDesc + childSeatCount);

        // 出行人数
        String passengerCount = String.format("成人%1$s位", orderBean.adult);
        if (orderBean.child != null && orderBean.child > 0) {
            passengerCount += String.format("，儿童%1$s位", orderBean.child);
        }
        if (CommonUtils.getCountInteger(orderBean.luggageNum) > 0) {
            passengerCount += String.format("，行李%1$s件", orderBean.luggageNum);
        }
        addCarDescView(R.mipmap.trip_icon_people, passengerCount);

        if (orderBean.orderGoodsType == 1  && "1".equalsIgnoreCase(orderBean.isFlightSign)) {//接机
            addItemView(R.mipmap.trip_icon_addition, "接机举牌等待");
        } else if (orderBean.orderGoodsType == 2 && "1".equals(orderBean.isCheckin)) {//送机checkin
            addItemView(R.mipmap.trip_icon_addition, "Check in 服务");
        }

//        //酒店预订 TODO 少图片和文字
//        if (orderBean.hotelStatus == 1) {
//            addItemView(R.mipmap.order_jd, getContext().getString(R.string.order_detail_hotle_subscribe, "" + orderBean.hotelDays, "" + orderBean.hotelRoom));
//        }

        // 订单号
        orderNumberView.update(orderBean.orderNo);


//        //"当地时间 04月21日（周五）10:05" orderBean.serviceTime
//        String localTime = getContext().getString(R.string.order_detail_local_time, orderBean.serviceTimeStr);
//        if (orderBean.orderType == 3) {
//            //主标题：东京-6天包车  副标题：当地时间
//            String totalDays = "" + orderBean.totalDays;
//            if (orderBean.isHalfDaily == 1) {//半日包
//                totalDays = getContext().getString(R.string.order_detail_half_day);
//            } else if (orderBean.totalDays > 1 && !TextUtils.isEmpty(orderBean.serviceEndTimeStr)) {
//                localTime = localTime + " - " + orderBean.serviceEndTimeStr;
//            }
//            addItemView(R.mipmap.order_time, getContext().getString(R.string.order_detail_local_chartered, orderBean.serviceCityName, totalDays), null, localTime);
//        } else if (orderBean.orderType == 5 || orderBean.orderType == 6) {
//            String serviceTimeFormatStr = DateUtils.getOrderDateFormat(orderBean.serviceTime);
//            String serviceTime = "";
//            if (!TextUtils.isEmpty(serviceTimeFormatStr)) {
//                serviceTime = getContext().getString(R.string.order_detail_local_time, serviceTimeFormatStr);
//            } else {
//                serviceTime = localTime;
//            }
//            addItemView(R.mipmap.order_time, getContext().getString(R.string.order_detail_local_chartered, orderBean.serviceCityName, "" + orderBean.totalDays), null, serviceTime);
//        } else {
//            //主标题：当地时间   副标题："航班HKJHKJ 东京-北京"
//            String flight = "";
//            if (!TextUtils.isEmpty(orderBean.flightNo)) {
//                flight = getContext().getString(R.string.order_detail_flight, orderBean.flightNo);
//            }
//            if (!TextUtils.isEmpty(orderBean.flightDeptCityName) && !TextUtils.isEmpty(orderBean.flightDestCityName)) {
//                flight += getContext().getString(R.string.separator, orderBean.flightDeptCityName, orderBean.flightDestCityName);
//            }
//            addItemView(R.mipmap.order_time, localTime, null, flight);
//        }
//
//        //包车线路列表
//        if (orderBean.orderType == 3 && orderBean.isHalfDaily == 0 && orderBean.passByCity != null && orderBean.passByCity.size() > 0) {
//            OrderDetailRouteView routeView = new OrderDetailRouteView(getContext());
//            itineraryLayout.addView(routeView);
//            routeView.update(orderBean.passByCity);
//        }
//
//        //出发地
//        if (!TextUtils.isEmpty(orderBean.startAddress)) {
//            addItemView(R.mipmap.order_place, orderBean.startAddress, null, orderBean.startAddressDetail);
//        }
//
//        //目的地
//        if (!TextUtils.isEmpty(orderBean.destAddress) && orderBean.orderType != 3) {
//            addItemView(R.mipmap.order_flag, orderBean.destAddress, null, orderBean.destAddressDetail);
//        }
//
//        //车型描述
//        String passengerInfos = getContext().getString(R.string.order_detail_adult_seat_info, "" + (orderBean.adult + orderBean.child));//座位总数
//        if (orderBean.childSeats != null && orderBean.childSeats.getChildSeatCount() > 0) {//儿童座椅数
//            passengerInfos += getContext().getString(R.string.order_detail_child_seat_info, "" + orderBean.childSeats.getChildSeatCount());
//        }
//        if (orderBean.carPool) {//拼车
//            addCarDescView(R.mipmap.order_passanger, passengerInfos, null);
//        } else if (!TextUtils.isEmpty(orderBean.carDesc)) {
//            passengerInfos = getContext().getString(R.string.order_detail_seat_info, passengerInfos);
//            addCarDescView(R.mipmap.order_car, orderBean.carDesc, passengerInfos);
//        }
//
//        if (orderBean.orderGoodsType == 1  && "1".equalsIgnoreCase(orderBean.isFlightSign)) {//接机
//            addItemView(R.mipmap.order_jp, getContext().getString(R.string.order_detail_airport_card));
//        } else if (orderBean.orderGoodsType == 2 && "1".equals(orderBean.isCheckin)) {//送机checkin
//            addItemView(R.mipmap.order_jp, getContext().getString(R.string.order_detail_checkin));
//        }
//
//        //酒店预订
//        if (orderBean.hotelStatus == 1) {
//            addItemView(R.mipmap.order_jd, getContext().getString(R.string.order_detail_hotle_subscribe, "" + orderBean.hotelDays, "" + orderBean.hotelRoom));
//        }
    }


    private LinearLayout addItemView(int iconId, String title) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_order_detail_itinerary, null, false);

        ImageView iconIV = (ImageView) itemView.findViewById(R.id.item_itinerary_iv);
        iconIV.setBackgroundResource(iconId);
        TextView titleTV = (TextView) itemView.findViewById(R.id.item_itinerary_title_tv);
        titleTV.setText(title);

        itineraryLayout.addView(itemView);
        return itemView;
    }

    public void setRouteLayoutVisible() {
        routeLayout.setVisibility(View.VISIBLE);
        Tools.showImage(routeIV, orderBean.picUrl);
        routeTV.setText(orderBean.lineSubject);
        routeTagIV.setVisibility(orderBean.carPool ? View.VISIBLE : View.GONE);//拼车
        if (orderBean.orderSource == 1 && !TextUtils.isEmpty(orderBean.skuDetailUrl)) {
            routeLayout.setOnClickListener(this);
        }
    }

    private SpannableString getRouteSpannableString(String lineSubject, int resId) {
        Drawable drawable = getContext().getResources().getDrawable(resId);
        drawable.setBounds(0, 0, UIUtils.dip2px(36), UIUtils.dip2px(18));
        SpannableString spannable = new SpannableString("[icon]" + lineSubject);
        VerticalImageSpan span = new VerticalImageSpan(drawable);
        spannable.setSpan(span, 0, "[icon]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    private void addCarDescView(int iconId, String title) {
        LinearLayout itemView = addItemView(iconId, title);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = UIUtils.dip2px(6);

        LinearLayout luggageItemLayout = new LinearLayout(getContext());
        luggageItemLayout.setOrientation(LinearLayout.HORIZONTAL);
        luggageItemLayout.setGravity(Gravity.CENTER_VERTICAL);

        ImageView iconIV = new ImageView(getContext());
        iconIV.setBackgroundResource(R.mipmap.icon_order_info);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(UIUtils.dip2px(14), UIUtils.dip2px(14));
        iconParams.topMargin = UIUtils.dip2px(1);
        luggageItemLayout.addView(iconIV, iconParams);

        TextView textView = new TextView(getContext());
        textView.setPadding(UIUtils.dip2px(1), 0, 0, 0);
        textView.setTextColor(0xFF1CBADC);
        textView.setTextSize(12);
        textView.setText("行李标准");
        luggageItemLayout.addView(textView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        itemView.addView(luggageItemLayout, params);

        luggageItemLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), LuggageInfoActivity.class));
            }
        });
    }

    private LinearLayout addLocationItem() {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_order_detail_location, null, false);
        TextView startTV = (TextView) itemView.findViewById(R.id.order_travel_item_start_tv);
        TextView startDesTV = (TextView) itemView.findViewById(R.id.order_travel_item_start_des_tv);
        View startLineView = itemView.findViewById(R.id.order_travel_item_start_line_view);

        TextView endTV = (TextView) itemView.findViewById(R.id.order_travel_item_end_tv);
        TextView endDesTV = (TextView) itemView.findViewById(R.id.order_travel_item_end_des_tv);
        startTV.setText(orderBean.startAddress);
        if (!TextUtils.isEmpty(orderBean.startAddressDetail)) {
            startDesTV.setVisibility(View.VISIBLE);
            startDesTV.setText(orderBean.startAddressDetail);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIUtils.dip2px(1), UIUtils.dip2px(20));
            params.topMargin = UIUtils.dip2px(20);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.travel_item_start_des_tv);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.leftMargin = UIUtils.dip2px(7.5f);
            startLineView.setLayoutParams(params);
        } else {
            startDesTV.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIUtils.dip2px(1), UIUtils.dip2px(6));
            params.topMargin = UIUtils.dip2px(20);
            params.leftMargin = UIUtils.dip2px(7.5f);
            startLineView.setLayoutParams(params);
        }
        endTV.setText(orderBean.destAddress);
        if (!TextUtils.isEmpty(orderBean.destAddressDetail)) {
            endDesTV.setVisibility(View.VISIBLE);
            endDesTV.setText(orderBean.destAddressDetail);
        } else {
            endDesTV.setVisibility(View.GONE);
        }
        itineraryLayout.addView(itemView);
        return itemView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_itinerary_route_layout://路线详情
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_ROUTE, orderBean.orderNo));
                break;
        }
    }

    @OnClick({R.id.order_itinerary_charter_travel_tv, R.id.order_itinerary_charter_arrow_iv})
    public void intentTravelList() {

    }
}
