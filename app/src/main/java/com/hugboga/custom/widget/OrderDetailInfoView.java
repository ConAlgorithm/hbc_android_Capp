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
import com.hugboga.custom.activity.OrderEditActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;
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
    private RelativeLayout insuranceInfoLayout;

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
        insurerStateTV = (TextView) findViewById(R.id.order_detail_insurer_state_tv);
        insurerErrorIV = (ImageView) findViewById(R.id.order_detail_insurer_state_error_iv);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        orderBean = (OrderBean) _data;
        nameTV.setText(orderBean.userName);
        editTV.setVisibility(orderBean.orderStatus.code > 5 ? View.GONE : View.VISIBLE);// 1-5显示修改

        final int insuranceListSize = orderBean.insuranceList != null ? orderBean.insuranceList.size() : 0;
        if (orderBean.orderStatus == OrderStatus.INITSTATE) {
            insuranceInfoLayout.setVisibility(View.GONE);
        } else if (orderBean.insuranceEnable && insuranceListSize == 0) {//添加投保人
            insuranceInfoLayout.setVisibility(View.VISIBLE);
            insurerErrorIV.setVisibility(View.GONE);
            insurerStateTV.setVisibility(View.VISIBLE);
            insurerTV.setText(String.format("平安境外用车险 × %1$s份", "" + (orderBean.adult + orderBean.child)));
            insuranceInfoLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle insureBundle = new Bundle();
                    insureBundle.putSerializable("orderBean", orderBean);
                    Intent intent = new Intent(getContext(), InsureActivity.class);
                    intent.putExtras(insureBundle);
                    getContext().startActivity(intent);
                }
            });
        } else if (insuranceListSize > 0) {
            insuranceInfoLayout.setVisibility(View.VISIBLE);
            insuranceInfoLayout.setOnClickListener(this);
            insurerStateTV.setVisibility(View.GONE);
            insurerErrorIV.setVisibility(View.GONE);

            String insuranceStatu = "";
            switch (orderBean.insuranceStatusCode) {
                case 1002:
                    insuranceStatu = "保单出现问题，请尽快联系客服";
                    insurerErrorIV.setVisibility(View.VISIBLE);
                    break;
                case 1003:
                    insuranceStatu = "为您购买的保险已注销";
                    break;
                case 1004:
                    insuranceStatu = "保单正在处理中...";
                    break;
                default:
                    insuranceStatu = getContext().getString(R.string.order_detail_info, "" + insuranceListSize);
                    break;
            }
            insurerTV.setText(insuranceStatu);
        } else {
            insuranceInfoLayout.setVisibility(View.GONE);
        }
    }

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
