package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/3/24.
 * OrderDetailTravelView
 */

public class OrderDetailAllTravelView extends LinearLayout implements HbcViewBehavior{

    private static final int DEFAULT_VIEW_COUNT = 3;

    @BindView(R.id.order_detail_all_travel_container)
    LinearLayout containerLayout;

    @BindView(R.id.order_detail_all_travel_more_layout)
    LinearLayout moreLayout;
    @BindView(R.id.order_detail_all_travel_more_iv)
    ImageView moreIV;
    @BindView(R.id.order_detail_all_travel_more_tv)
    TextView moreTV;

    private boolean isShow = false;
    private ArrayList<CityBean> passCityList;
    private OrderBean orderBean;

    public OrderDetailAllTravelView(Context context) {
        this(context, null);
    }

    public OrderDetailAllTravelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0xFFFFFFFF);
        setPadding(0, 0, 0, UIUtils.dip2px(12));
        setOrientation(LinearLayout.VERTICAL);
        View view = inflate(context, R.layout.view_order_detail_all_travel, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        orderBean = (OrderBean) _data;
        containerLayout.removeAllViews();
        passCityList = orderBean.passByCity;
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
        for (int i = 0; i < size && i < DEFAULT_VIEW_COUNT; i++) {
            String date = DateUtils.orderChooseDateTransform(DateUtils.getDay(orderBean.serviceTime, i));
            addItemView(date, passCityList.get(i).description);
        }
        isShow = false;
        if (size > DEFAULT_VIEW_COUNT) {
            moreLayout.setVisibility(View.VISIBLE);
            moreIV.setBackgroundResource(R.mipmap.more_arrow_down);
            moreTV.setText("展开日程");
        } else {
            moreLayout.setVisibility(View.GONE);
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

    @OnClick({R.id.order_detail_all_travel_more_layout})
    public void setMoreItem() {
        if (isShow) {
            moreIV.setBackgroundResource(R.mipmap.more_arrow_down);
            moreTV.setText("展开日程");
        } else {
            moreIV.setBackgroundResource(R.mipmap.more_arrow_up);
            moreTV.setText("收起日程");
        }
        isShow = !isShow;
        int childCount = containerLayout.getChildCount();
        int size = passCityList.size();
        for (int i = 0; i < size; i++) {
            if (!isShow && i == DEFAULT_VIEW_COUNT) {
                break;
            }
            String date = DateUtils.orderChooseDateTransform(DateUtils.getDay(orderBean.serviceTime, i));
            String description = passCityList.get(i).description;
            if (i < childCount) {
                LinearLayout itemView = (LinearLayout) containerLayout.getChildAt(i);
                itemView.setVisibility(View.VISIBLE);
                TextView dateTV = (TextView) itemView.findViewById(R.id.order_detail_travel_item_date_tv);
                dateTV.setText(date);
                TextView titleTV = (TextView) itemView.findViewById(R.id.order_detail_travel_item_title_tv);
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
