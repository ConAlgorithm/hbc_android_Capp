package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.InsureActivity;
import com.hugboga.custom.activity.InsureInfoActivity;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.activity.OrderDetailTravelerInfoActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/6/2.
 */
public class OrderDetailInfoView extends LinearLayout implements HbcViewBehavior, View.OnClickListener {

    private TextView nameTV;
    private TextView editTV;

    private TextView insurerTV;
    private ImageView insurerErrorIV;
    private ImageView insurerArrowIV;
    private RelativeLayout insuranceInfoLayout;

    private RelativeLayout insuranceAddLayout;
    private TextView insurancePartTV;
    private ImageView insuranceExplainIV;
    private TextView insuranceAddTV;

    private OrderBean orderBean;

    public OrderDetailInfoView(Context context) {
        this(context, null);
    }

    public OrderDetailInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_info, this);
        ButterKnife.bind(view);

        findViewById(R.id.order_detail_info_layout).setOnClickListener(this);
        nameTV = (TextView) findViewById(R.id.order_detail_info_name_tv);
        editTV = (TextView) findViewById(R.id.order_detail_info_edit_tv);

        insuranceInfoLayout = (RelativeLayout) findViewById(R.id.order_detail_insurance_info_layout);
        insurerTV = (TextView) findViewById(R.id.order_detail_insurer_tv);
        insurerErrorIV = (ImageView) findViewById(R.id.order_detail_insurer_state_error_iv);
        insurerArrowIV = (ImageView) findViewById(R.id.order_detail_insurer_arrow_iv);

        insuranceAddLayout = (RelativeLayout) findViewById(R.id.order_detail_insurance_add_layout);
        insurancePartTV = (TextView) findViewById(R.id.order_detail_insurance_part_tv);
        insuranceExplainIV = (ImageView) findViewById(R.id.order_detail_insurance_explain_iv);
        insuranceAddTV = (TextView) findViewById(R.id.order_detail_insurance_add_tv);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        orderBean = (OrderBean) _data;
        nameTV.setText(orderBean.getTravelUserName());
        editTV.setVisibility(orderBean.orderStatus.code > 5 ? View.GONE : View.VISIBLE);// 1-5显示修改

        final int insuranceListSize = orderBean.insuranceMap != null ? orderBean.insuranceMap.size() : 0;
        if (orderBean.orderStatus == OrderStatus.INITSTATE) {
            insuranceInfoLayout.setVisibility(View.GONE);
            insuranceAddLayout.setVisibility(View.GONE);
        } else if (orderBean.insuranceEnable && insuranceListSize == 0) {//添加投保人
            insuranceInfoLayout.setVisibility(View.GONE);
            insuranceAddLayout.setVisibility(View.VISIBLE);
            insuranceAddTV.setOnClickListener(this);
            insurancePartTV.setText(CommonUtils.getString(R.string.order_detail_insurance_hint, "" + (orderBean.adult + orderBean.child)));
            insuranceExplainIV.setOnClickListener(this);
        } else if (insuranceListSize > 0) {
            insuranceInfoLayout.setVisibility(View.VISIBLE);
            insurerArrowIV.setVisibility(View.VISIBLE);
            insuranceAddLayout.setVisibility(View.GONE);
            insurerErrorIV.setVisibility(View.GONE);
            insuranceInfoLayout.setOnClickListener(this);

            String insuranceStatu = "";
            int insuranceColor = getContext().getResources().getColor(R.color.default_black);
            switch (orderBean.insuranceStatusCode) {
                case 1002:
                    insuranceStatu = CommonUtils.getString(R.string.order_detail_insurance_status1);
                    insurerErrorIV.setVisibility(View.VISIBLE);
                    insuranceColor = 0xFFF7350A;
                    break;
                case 1003:
                    insuranceStatu = CommonUtils.getString(R.string.order_detail_insurance_status2);
                    break;
                case 1004:
                    insuranceStatu = CommonUtils.getString(R.string.order_detail_insurance_status3);
                    break;
                default:
                    insuranceStatu = getContext().getString(R.string.order_detail_info, "" + insuranceListSize);
                    break;
            }
            insurerTV.setText(insuranceStatu);
            insurerTV.setTextColor(insuranceColor);
        } else {
            insuranceInfoLayout.setVisibility(View.GONE);
            insuranceAddLayout.setVisibility(View.GONE);
        }
    }

    private void addSensors() {
        if (getContext() != null && (getContext() instanceof OrderDetailActivity)) {
            SensorsUtils.onAppClick(((OrderDetailActivity) getContext()).getEventSource(),
                    "修改出行人信息", ((OrderDetailActivity) getContext()).getIntentSource());
        }
    }

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.order_detail_info_layout://出行人信息
                addSensors(); //修改出行人信息埋点
                intent = new Intent(getContext(), OrderDetailTravelerInfoActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, orderBean);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                getContext().startActivity(intent);
                break;
            case R.id.order_detail_insurance_add_tv:
            case R.id.order_detail_insurance_info_layout:
                final int insuranceListSize = orderBean.insuranceMap != null ? orderBean.insuranceMap.size() : 0;
                if (orderBean.insuranceEnable && insuranceListSize == 0) {//添加投保人
                    Bundle insureBundle = new Bundle();
                    insureBundle.putSerializable("orderBean", orderBean);
                    intent = new Intent(getContext(), InsureActivity.class);
                    intent.putExtras(insureBundle);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    getContext().startActivity(intent);
                } else {//投保人list
                    intent = new Intent(getContext(), InsureInfoActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, orderBean);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    getContext().startActivity(intent);
                }
                break;
            case R.id.order_detail_insurance_explain_iv:
                intent = new Intent(getContext(), WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_INSURANCE);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                getContext().startActivity(intent);
                break;
        }
    }

    public String getEventSource() {
        return "订单详情";
    }
}
