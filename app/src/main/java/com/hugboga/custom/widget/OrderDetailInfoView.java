package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.InsureInfoActivity;
import com.hugboga.custom.activity.OrderEditActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;

/**
 * Created by qingcha on 16/6/2.
 */
public class OrderDetailInfoView extends LinearLayout implements HbcViewBehavior, View.OnClickListener{

    private RelativeLayout insuranceInfoLayout;
    private RelativeLayout insuranceGetLayout;
    private TextView nameTV;
    private TextView editTV;
    private TextView insurerTV;
    private TextView insurerStateTV;

    private OrderBean orderBean;

    public OrderDetailInfoView(Context context) {
        this(context, null);
    }

    public OrderDetailInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_order_detail_info, this);

        findViewById(R.id.order_detail_info_layout).setOnClickListener(this);
        nameTV = (TextView) findViewById(R.id.order_detail_info_name_tv);
        editTV = (TextView) findViewById(R.id.order_detail_info_edit_tv);

        insuranceInfoLayout = (RelativeLayout) findViewById(R.id.order_detail_insurance_info_layout);
        insurerTV = (TextView) findViewById(R.id.order_detail_insurer_tv);
        insurerStateTV = (TextView) findViewById(R.id.order_detail_insurer_state_tv);

        insuranceGetLayout = (RelativeLayout) findViewById(R.id.order_detail_insurance_get_layout);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        orderBean = (OrderBean) _data;
        nameTV.setText(orderBean.contactName);
        if (orderBean.orderStatus.code > 5) {//1-5显示修改,其它显示详情
            editTV.setText(getContext().getString(R.string.order_detail));
            editTV.setTextColor(0xFF999999);
        } else {
            editTV.setText(getContext().getString(R.string.order_detail_edit));
            editTV.setTextColor(0xFFFF6633);
        }

        final int insuranceListSize = orderBean.insuranceList != null ? orderBean.insuranceList.size() : 0;
        if (orderBean.orderStatus == OrderStatus.INITSTATE) {
            insuranceInfoLayout.setVisibility(View.GONE);
            insuranceGetLayout.setVisibility(View.VISIBLE);
            insuranceGetLayout.setOnClickListener(this);
        } else if (insuranceListSize > 0) {
            insuranceInfoLayout.setVisibility(View.VISIBLE);
            insuranceInfoLayout.setOnClickListener(this);
            insuranceGetLayout.setVisibility(View.GONE);
            insurerTV.setText(getContext().getString(R.string.order_detail_info, "" + insuranceListSize));
            insurerStateTV.setText(orderBean.getInsuranceStatus());
        } else {
            insuranceInfoLayout.setVisibility(View.GONE);
            insuranceGetLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.order_detail_info_layout://出行人信息
                intent = new Intent(getContext(), OrderEditActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, orderBean);
                getContext().startActivity(intent);
                break;
            case R.id.order_detail_insurance_info_layout://投保人list
                intent = new Intent(getContext(), InsureInfoActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, orderBean);
                getContext().startActivity(intent);
                break;
        }
    }
}
