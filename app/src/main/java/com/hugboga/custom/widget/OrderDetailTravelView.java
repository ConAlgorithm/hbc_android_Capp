package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.DetailPassCityListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.utils.DateUtils;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/3/14.
 * OrderDetailAllTravelView
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

    @Bind(R.id.order_detail_travel_parrent_layout)
    LinearLayout parrentLayout;

    private OrderBean orderBean;

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

        ArrayList<CityBean> passCityList = orderBean.passByCity;
        if (passCityList == null) {
            if (orderBean.orderType == 1) {//只接机
                firstDateTV.setText(DateUtils.orderChooseDateTransform(orderBean.serviceTime));
                firstTitleTV.setText("只接机，航班：" + orderBean.flightNo);
            } else if (orderBean.orderType == 2) {//只送机
                firstDateTV.setText(DateUtils.orderChooseDateTransform(orderBean.serviceTime));
                String airportName = TextUtils.isEmpty(orderBean.flightAirportName) ? orderBean.destAddress : orderBean.flightAirportName;
                firstTitleTV.setText("只送机，机场：" + airportName);
            }
            return;
        }
        if (passCityList.size() > 0) {
            firstDateTV.setText(DateUtils.orderChooseDateTransform(orderBean.serviceTime));
            firstTitleTV.setText(passCityList.get(0).description);
        }

        if (passCityList.size() > 1) {
            secondDateTV.setText(DateUtils.orderChooseDateTransform(DateUtils.getDay(orderBean.serviceTime, 1)));
            secondTitleTV.setText(passCityList.get(1).description);
        } else {
            secondDateTV.setText("");
            secondTitleTV.setText("");
        }
    }

    @OnClick({R.id.order_detail_travel_more_tv, R.id.order_detail_travel_more_iv})
    public void showAllTravel() {
        Intent intent = new Intent(getContext(), DetailPassCityListActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, orderBean);
        getContext().startActivity(intent);
    }
}
