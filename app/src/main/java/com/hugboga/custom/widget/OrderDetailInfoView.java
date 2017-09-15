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
import com.hugboga.custom.activity.OrderDetailTravelerInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.utils.CommonUtils;

import butterknife.ButterKnife;
/**
 * Created by qingcha on 16/6/2.
 */
public class OrderDetailInfoView extends LinearLayout implements HbcViewBehavior, View.OnClickListener{

    private TextView nameTV;
    private TextView editTV;

    private TextView insurerTV;
    private TextView insurerStateTV;
    private ImageView insurerErrorIV;
    private ImageView insurerArrowIV;
    private RelativeLayout insuranceInfoLayout, insuranceAddLayout;

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
        insuranceAddLayout = (RelativeLayout) findViewById(R.id.order_detail_insurance_add_layout);
        insurerTV = (TextView) findViewById(R.id.order_detail_insurer_tv);
        insurerStateTV = (TextView) findViewById(R.id.order_detail_insurer_state_tv);
        insurerErrorIV = (ImageView) findViewById(R.id.order_detail_insurer_state_error_iv);
        insurerArrowIV = (ImageView) findViewById(R.id.order_detail_insurer_arrow_iv);
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
            insuranceInfoLayout.setVisibility(View.VISIBLE);
            insuranceAddLayout.setVisibility(View.VISIBLE);
            insurerErrorIV.setVisibility(View.GONE);
            insurerStateTV.setVisibility(View.VISIBLE);
            insurerArrowIV.setVisibility(View.INVISIBLE);
            insurerTV.setText(CommonUtils.getString(R.string.order_detail_insurance_hint, "" + (orderBean.adult + orderBean.child)));
            insuranceInfoLayout.setOnClickListener(null);
            insuranceAddLayout.setOnClickListener(this);
        } else if (insuranceListSize > 0) {
            insuranceInfoLayout.setVisibility(View.VISIBLE);
            insurerArrowIV.setVisibility(View.VISIBLE);
            insuranceAddLayout.setVisibility(View.GONE);
            insurerStateTV.setVisibility(View.GONE);
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

    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.order_detail_info_layout://出行人信息
                intent = new Intent(getContext(), OrderDetailTravelerInfoActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, orderBean);
                getContext().startActivity(intent);
                break;
            case R.id.order_detail_insurance_add_layout:
            case R.id.order_detail_insurance_info_layout:
                final int insuranceListSize = orderBean.insuranceMap != null ? orderBean.insuranceMap.size() : 0;
                if (orderBean.insuranceEnable && insuranceListSize == 0) {//添加投保人
                    Bundle insureBundle = new Bundle();
                    insureBundle.putSerializable("orderBean", orderBean);
                    intent = new Intent(getContext(), InsureActivity.class);
                    intent.putExtras(insureBundle);
                    getContext().startActivity(intent);
                } else {//投保人list
                    intent = new Intent(getContext(), InsureInfoActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, orderBean);
                    getContext().startActivity(intent);
                }
                break;
        }
    }
}
