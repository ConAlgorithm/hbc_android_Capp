package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OrderBean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/14.
 */
public class OrderDetailTravelView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.order_detail_travel_more_tv)
    TextView moreTV;
    @Bind(R.id.order_detail_travel_more_iv)
    ImageView moreIV;

    @Bind(R.id.order_detail_travel_first_date_tv)
    TextView firstDateTV;
    @Bind(R.id.order_detail_travel_first_title_tv)
    TextView firstTitleTV;

    @Bind(R.id.order_detail_travel_second_layout)
    RelativeLayout secondLayout;
    @Bind(R.id.order_detail_travel_second_date_tv)
    TextView secondDateTV;
    @Bind(R.id.order_detail_travel_second_title_tv)
    TextView secondTitleTV;

    @Bind(R.id.order_detail_travel_no_view)
    OrderDetailNoView orderNoView;

    public OrderDetailTravelView(Context context) {
        this(context, null);
    }

    public OrderDetailTravelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_travel, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        OrderBean orderBean = (OrderBean) _data;
        if (orderBean.totalDays > 1) {
            moreTV.setVisibility(View.VISIBLE);
            moreIV.setVisibility(View.VISIBLE);
        } else {
            moreTV.setVisibility(View.GONE);
            moreIV.setVisibility(View.GONE);
        }
        orderNoView.update(orderBean.orderNo);
    }
}
