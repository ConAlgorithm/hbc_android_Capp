package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.DateUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/4/12.
 */
public class OrderTravelView extends LinearLayout {

    private static final int DEFAULT_VIEW_COUNT = 3;

    @Bind(R.id.order_travel_container)
    LinearLayout containerLayout;

    @Bind(R.id.order_travel_more_layout)
    LinearLayout moreLayout;
    @Bind(R.id.order_travel_more_tv)
    TextView moreTV;
    @Bind(R.id.order_travel_more_iv)
    ImageView moreIV;
    @Bind(R.id.order_travel_line_view)
    View lineView;

    private boolean isShow = false;
    private CharterDataUtils charterDataUtils;

    public OrderTravelView(Context context) {
        this(context, null);
    }

    public OrderTravelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0xFFFFFFFF);
        setOrientation(LinearLayout.VERTICAL);
        View view = inflate(context, R.layout.view_order_travel, this);
        ButterKnife.bind(view);
    }

    public void update(CharterDataUtils charterDataUtils) {
        this.charterDataUtils = charterDataUtils;
        containerLayout.removeAllViews();
        ArrayList<CityRouteBean.CityRouteScope> travelList = charterDataUtils.travelList;
        int size = travelList.size();
        for (int i = 0; i < size && i < DEFAULT_VIEW_COUNT; i++) {
            String date = DateUtils.orderChooseDateTransform(DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, i));
            String description = getDescription(size, i, travelList.get(i));
            addItemView(date, description);
        }
        isShow = false;
        if (size > DEFAULT_VIEW_COUNT) {
            lineView.setVisibility(View.VISIBLE);
            moreLayout.setVisibility(View.VISIBLE);
            moreIV.setBackgroundResource(R.mipmap.icon_black_arrow);
            moreTV.setText("更多日程");
        } else {
            lineView.setVisibility(View.GONE);
            moreLayout.setVisibility(View.GONE);
        }
    }

    private String getDescription(int size, int index, CityRouteBean.CityRouteScope cityRouteScope) {
        String result = cityRouteScope.routeTitle;
        if (index == 0 && charterDataUtils.isSelectedPickUp && charterDataUtils.flightBean != null) {
            if (cityRouteScope.routeType == CityRouteBean.RouteType.PICKUP) {//只接机
                result = "只接机，航班：" + charterDataUtils.flightBean.flightNo;
            } else {//包车加接机
                if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
                    result = charterDataUtils.flightBean.arrAirportName + "机场出发，" + String.format("游玩至%1$s结束", charterDataUtils.getEndCityBean(index + 1).name);
                } else {
                    result = charterDataUtils.flightBean.arrAirportName + "机场出发，" + result;
                }
            }
        } else if (index == size - 1 && charterDataUtils.isSelectedSend && charterDataUtils.airPortBean != null) {
            if (cityRouteScope.routeType == CityRouteBean.RouteType.SEND) {//只送机
                result = String.format("只送机：%1$s(%2$s出发)", charterDataUtils.airPortBean.airportName, charterDataUtils.sendServerTime);
            } else {//包车加送机
                result = result + "游玩结束送机";
            }
        } else if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {
            result = String.format("%1$s出发，%2$s结束", charterDataUtils.getStartCityBean(index + 1).name, charterDataUtils.getEndCityBean(index + 1).name);
        }
        return result;
    }

    private LinearLayout addItemView(String date, String title) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_order_travel_item, null, false);
        TextView dateTV = (TextView) itemView.findViewById(R.id.order_travel_item_date_tv);
        dateTV.setText(date);
        TextView titleTV = (TextView) itemView.findViewById(R.id.order_travel_item_title_tv);
        titleTV.setText(title);
        containerLayout.addView(itemView);
        return itemView;
    }

    @OnClick({R.id.order_travel_more_layout})
    public void setMoreItem() {
        if (isShow) {
            moreIV.setBackgroundResource(R.mipmap.icon_black_arrow);
            moreTV.setText("更多日程");
        } else {
            moreIV.setBackgroundResource(R.mipmap.icon_black_arrow_up);
            moreTV.setText("收起日程");
        }
        isShow = !isShow;

        int childCount = containerLayout.getChildCount();
        ArrayList<CityRouteBean.CityRouteScope> travelList = charterDataUtils.travelList;
        int size = travelList.size();
        for (int i = 0; i < size; i++) {
            if (!isShow && i == DEFAULT_VIEW_COUNT) {
                break;
            }
            String date = DateUtils.orderChooseDateTransform(DateUtils.getDay(charterDataUtils.chooseDateBean.start_date, i));
            String description = getDescription(size, i, travelList.get(i));
            if (i < childCount) {
                LinearLayout itemView = (LinearLayout) containerLayout.getChildAt(i);
                itemView.setVisibility(View.VISIBLE);
                TextView dateTV = (TextView) itemView.findViewById(R.id.order_travel_item_date_tv);
                dateTV.setText(date);
                TextView titleTV = (TextView) itemView.findViewById(R.id.order_travel_item_title_tv);
                titleTV.setText(description);
            } else {
                addItemView(date, description);
            }
        }

        int startIndex = isShow ? size : DEFAULT_VIEW_COUNT;
        for (int i = startIndex; i < childCount; i++) {
            containerLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }
}
