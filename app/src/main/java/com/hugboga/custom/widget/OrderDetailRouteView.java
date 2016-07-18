package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/3.
 */
public class OrderDetailRouteView extends LinearLayout implements View.OnClickListener, HbcViewBehavior {

    private static final int DEFAULT_SHOW_COUNT = 3;//默认展示条数

    private LinearLayout itemLayout;
    private LinearLayout moreLayout;
    private LinearLayout moreBtn;
    private ImageView moreIV;
    private TextView moreTV;
    private OrderDetailRouteItemView lastItemView;//默认展示条数里最后一个View

    public OrderDetailRouteView(Context context) {
        this(context, null);
    }

    public OrderDetailRouteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_order_detail_route, this);
        itemLayout = (LinearLayout) findViewById(R.id.order_route_item_layout);
        moreLayout = (LinearLayout) findViewById(R.id.order_route_more_layout);
        moreBtn = (LinearLayout) findViewById(R.id.order_route_more_btn);
        moreBtn.setOnClickListener(this);
        moreIV = (ImageView) findViewById(R.id.order_route_more_iv);
        moreTV = (TextView) findViewById(R.id.order_route_more_tv);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        ArrayList<CityBean> passCityList = (ArrayList<CityBean>) _data;

        setUIChange(false);
        final int size = passCityList.size();

        if (size <= DEFAULT_SHOW_COUNT) {
            moreBtn.setVisibility(View.GONE);
            itemLayout.setPadding(0, UIUtils.dip2px(10), 0, UIUtils.dip2px(10));
        } else {
            moreBtn.setVisibility(View.VISIBLE);
            itemLayout.setPadding(0, UIUtils.dip2px(10), 0, 0);
        }

        for (int i = 0; i < size; i++) {
            OrderDetailRouteItemView itemView = new OrderDetailRouteItemView(getContext());
            itemView.setText(passCityList.get(i).description);
            itemView.setStyle(i, size);
            if (i < DEFAULT_SHOW_COUNT) {
                itemLayout.addView(itemView);
                if (i == DEFAULT_SHOW_COUNT - 1) {
                    itemView.setStyleType(OrderDetailRouteItemView.StyleType.BOTTOM);
                    lastItemView = itemView;
                }
            } else {
                moreLayout.addView(itemView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_route_more_btn:
                setUIChange(moreLayout.getVisibility() == View.GONE);
                break;
        }
    }

    private void setUIChange(boolean isShowMore) {
        if (isShowMore) {
            moreTV.setText(getContext().getString(R.string.order_detail_close));
            moreIV.setBackgroundResource(R.mipmap.journey_withdraw);
            moreLayout.setVisibility(View.VISIBLE);
            if (lastItemView != null) {
                lastItemView.setStyleType(OrderDetailRouteItemView.StyleType.ALL);
            }
        } else {
            moreTV.setText(getContext().getString(R.string.order_detail_opean));
            moreIV.setBackgroundResource(R.mipmap.journey_unfold);
            moreLayout.setVisibility(View.GONE);
            if (lastItemView != null) {
                lastItemView.setStyleType(OrderDetailRouteItemView.StyleType.BOTTOM);
            }
        }
    }
}