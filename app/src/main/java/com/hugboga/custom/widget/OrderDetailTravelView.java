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
import com.hugboga.custom.utils.UIUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/3/14.
 * OrderDetailAllTravelView
 */
public class OrderDetailTravelView extends LinearLayout implements HbcViewBehavior{

    @BindView(R.id.order_detail_travel_more_tv)
    TextView moreTV;
    @BindView(R.id.order_detail_travel_more_iv)
    ImageView moreIV;

    @BindView(R.id.order_detail_travel_first_date_tv)
    TextView firstDateTV;
    @BindView(R.id.order_detail_travel_first_title_tv)
    TextView firstTitleTV;

    @BindView(R.id.order_detail_travel_second_layout)
    RelativeLayout secondLayout;
    @BindView(R.id.order_detail_travel_second_date_tv)
    TextView secondDateTV;
    @BindView(R.id.order_detail_travel_second_title_tv)
    TextView secondTitleTV;

    @BindView(R.id.order_detail_travel_no_view)
    OrderDetailNoView orderNoView;

    @BindView(R.id.order_detail_travel_parrent_layout)
    LinearLayout parrentLayout;

    private OrderBean orderBean;
    private boolean isSingleTravel = false;

    public OrderDetailTravelView(Context context) {
        this(context, null);
    }

    public OrderDetailTravelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_travel, this);
        ButterKnife.bind(view);
    }

    public void singleTravel() {
        isSingleTravel = true;
        orderNoView.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.bottomMargin = UIUtils.dip2px(4);
        parrentLayout.setLayoutParams(params);
    }


    @Override
    public void update(Object _data) {
        orderBean = (OrderBean) _data;
        if (orderBean.totalDays > 1) {
            moreTV.setVisibility(View.VISIBLE);
            moreIV.setVisibility(View.VISIBLE);
        } else {
            moreTV.setVisibility(View.GONE);
            moreIV.setVisibility(View.GONE);
        }
        if (isSingleTravel) {
            orderNoView.setVisibility(View.GONE);
        } else {
            orderNoView.update(orderBean.orderNo);
        }

        ArrayList<CityBean> passCityList = orderBean.passByCity;
        if (passCityList == null) {
            if (orderBean.orderType == 1) {//只接机
                firstDateTV.setText(DateUtils.orderChooseDateTransform(orderBean.serviceTime));
                firstTitleTV.setText("只接机，航班：" + orderBean.flightNo);
            } else if (orderBean.orderType == 2) {//只送机
                firstDateTV.setText(DateUtils.orderChooseDateTransform(orderBean.serviceTime));
                String airportName = TextUtils.isEmpty(orderBean.flightAirportName) ? orderBean.destAddress : orderBean.flightAirportName;
                String timeStr = "";
                if (!TextUtils.isEmpty(orderBean.serviceTime)) {
                    try {
                        Date date = DateUtils.dateTimeFormat.parse(orderBean.serviceTime);
                        timeStr = String.format("(%1$s出发)", DateUtils.getTime(date));
                    } catch (ParseException e) {
                        timeStr = "";
                        e.printStackTrace();
                    }
                }
                firstTitleTV.setText(String.format("只送机：%1$s", airportName) + timeStr);
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
            if (isSingleTravel) {
                secondLayout.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.order_detail_travel_more_tv, R.id.order_detail_travel_more_iv})
    public void showAllTravel() {
        Intent intent = new Intent(getContext(), DetailPassCityListActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, orderBean);
        getContext().startActivity(intent);
    }
}
