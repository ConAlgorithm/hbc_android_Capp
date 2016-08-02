package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderPriceInfo;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.fragment.FgOrderDetail;
import com.hugboga.custom.fragment.FgSkuList;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by qingcha on 16/6/2.
 */
public class OrderDetailItineraryView extends LinearLayout implements HbcViewBehavior, View.OnClickListener{

    private FgOrderDetail mFragment;

    private LinearLayout itineraryLayout;
    private TextView orderNumberTV;
    private TextView carpoolTV;

    private TextView routeTV;
    private ImageView routeIV;
    private RelativeLayout routeLayout;

    private OrderBean orderBean;

    public OrderDetailItineraryView(Context context) {
        this(context, null);
    }

    public OrderDetailItineraryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_order_detail_itinerary, this);
        itineraryLayout = (LinearLayout) findViewById(R.id.order_itinerary_item_layout);
        orderNumberTV = (TextView) findViewById(R.id.order_itinerary_order_number_tv);
        carpoolTV = (TextView) findViewById(R.id.order_itinerary_carpool_tv);

        routeTV = (TextView) findViewById(R.id.order_itinerary_route_tv);
        routeIV = (ImageView) findViewById(R.id.order_itinerary_route_iv);
        routeLayout = (RelativeLayout) findViewById(R.id.order_itinerary_route_layout);
    }

    public void setFragment(FgOrderDetail _fragment) {
        this.mFragment = _fragment;
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        orderBean = (OrderBean) _data;
        orderNumberTV.setText(getContext().getString(R.string.order_detail_order_number, orderBean.orderNo));
        itineraryLayout.removeAllViews();

        if (orderBean.orderType == 5 && !TextUtils.isEmpty(orderBean.lineSubject)) {//固定线路 超省心
            setRouteLayoutVisible(R.mipmap.chaoshengxin);
        } else if(orderBean.orderType == 6 && !TextUtils.isEmpty(orderBean.lineSubject)) {//推荐路线 超自由
            setRouteLayoutVisible(R.mipmap.chaoziyou);
        } else {
            routeLayout.setVisibility(View.GONE);
            routeLayout.setOnClickListener(null);
        }

        carpoolTV.setVisibility(orderBean.carPool ? View.VISIBLE : View.GONE);//拼车

        //"当地时间 04月21日（周五）10:05" orderBean.serviceTime
        String localTime = getContext().getString(R.string.order_detail_local_time, orderBean.serviceTimeStr);
        if (orderBean.orderType == 3 || orderBean.orderType == 5 || orderBean.orderType == 6) {
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

        //包车线路列表
        if (orderBean.orderType == 3 && orderBean.isHalfDaily == 0 && orderBean.passByCity != null && orderBean.passByCity.size() > 0) {
            OrderDetailRouteView routeView = new OrderDetailRouteView(getContext());
            itineraryLayout.addView(routeView);
            routeView.update(orderBean.passByCity);
        }

        if (!TextUtils.isEmpty(orderBean.startAddress)) {//出发地
            addItemView(R.mipmap.order_place, orderBean.startAddress, null, orderBean.startAddressDetail);
        }

        if (!TextUtils.isEmpty(orderBean.destAddress) && orderBean.orderType != 3) {//目的地
            addItemView(R.mipmap.order_flag, orderBean.destAddress, null, orderBean.destAddressDetail);
        }

        if (!TextUtils.isEmpty(orderBean.carDesc)) {//车型描述
            String passengerInfos = null;
            if (!TextUtils.isEmpty(orderBean.passengerInfos)) {
                passengerInfos = getContext().getString(R.string.order_detail_seat_info, orderBean.passengerInfos);
            }
            LinearLayout itemView = addItemView(R.mipmap.order_car, orderBean.carDesc, passengerInfos, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = UIUtils.dip2px(25);
            params.topMargin = UIUtils.dip2px(2);
            params.bottomMargin = UIUtils.dip2px(2);
            LuggageItemLayout luggageItemLayout = new LuggageItemLayout(getContext());
            itemView.addView(luggageItemLayout, params);
            luggageItemLayout.setText(CommonUtils.getCountString(orderBean.luggageNum) + getContext().getString(R.string.piece));//可携带行李数
        }

        if (orderBean.orderGoodsType == 1  && "1".equalsIgnoreCase(orderBean.isFlightSign)) {//接机
            addItemView(R.mipmap.order_jp, getContext().getString(R.string.order_detail_airport_card));
        } else if (orderBean.orderGoodsType == 2 && "1".equals(orderBean.isCheckin)) {//送机checkin
            addItemView(R.mipmap.order_jp, getContext().getString(R.string.order_detail_checkin));
        }

        if (orderBean.hotelStatus == 1) {//酒店预订
            addItemView(R.mipmap.order_jp, getContext().getString(R.string.order_detail_hotle_subscribe, orderBean.hotelDays, orderBean.hotelRoom));
        }
    }

    private void addItemView(int iconId, String title) {
        addItemView(iconId, title, null, null);
    }

    private LinearLayout addItemView(int iconId, String title, String subtitle, String describe) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_order_detail_itinerary, null, false);

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
        return itemView;
    }

    public void setRouteLayoutVisible(int resId) {
        routeLayout.setVisibility(View.VISIBLE);
        Tools.showRoundImage(routeIV, orderBean.picUrl, UIUtils.dip2px(5));
        routeTV.setText(getRouteSpannableString(orderBean.lineSubject, resId));
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

    @Override
    public void onClick(View v) {
        if (mFragment == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.order_itinerary_route_layout:
                SkuDetailActivity fgSkuDetail = new SkuDetailActivity();
//                intent.putString(WebInfoActivity.WEB_URL, orderBean.skuDetailUrl);
//                intent.putString(Constants.PARAMS_ID, orderBean.goodsNo);
//                fgSkuDetail.setArguments(bundle);
//                mFragment.startFragment(fgSkuDetail, bundle);
//
                Intent intent = new Intent(context, SkuDetailActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, orderBean.skuDetailUrl);
                intent.putExtra(Constants.PARAMS_ID, orderBean.goodsNo);
//                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_DAIRY);
                context.startActivity(intent);


                break;
        }
    }
}
