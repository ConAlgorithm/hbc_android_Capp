package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderStatus;
import com.hugboga.custom.utils.DateUtils;
import com.qiyukf.unicorn.api.ProductDetail;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/11/11.
 */
public class UnicornOrderView extends LinearLayout implements HbcViewBehavior{

    @BindView(R.id.unicorn_order_title_tv)
    TextView titleTV;
    @BindView(R.id.unicorn_order_description_tv)
    TextView descriptionTV;
    @BindView(R.id.unicorn_order_state_tv)
    TextView stateTV;
    @BindView(R.id.unicorn_order_time_tv)
    TextView timeTV;
    @BindView(R.id.unicorn_order_no_tv)
    TextView orderNoTV;

    private ProductDetail productDetail;

    public UnicornOrderView(Context context) {
        this(context, null);
    }

    public UnicornOrderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_unicorn_order, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        OrderBean orderBean = (OrderBean) _data;
        if (orderBean == null) {
            return;
        }

        stateTV.setText(orderBean.orderStatus.name);
        if (orderBean.orderStatus == OrderStatus.NOT_EVALUATED || orderBean.orderStatus == OrderStatus.COMPLETE) {
            if (!orderBean.isEvaluated()) {
                stateTV.setText("未评价");
            } else {
                stateTV.setText("服务完成");
            }
        }

        orderNoTV.setText("订单号:  " + orderBean.orderNo);

        String timeStr = "";
        switch (orderBean.orderType) {
            case Constants.BUSINESS_TYPE_COMMEND:// 固定路线
            case Constants.BUSINESS_TYPE_RECOMMEND:// 推荐线路
                titleTV.setText(orderBean.carDesc);
                descriptionTV.setText(orderBean.lineSubject);
                timeStr = orderBean.serviceTime + " 至 " + orderBean.serviceEndTime + " " + orderBean.totalDays + "天" + " ";
                break;
            case Constants.BUSINESS_TYPE_PICK:// 接机
                titleTV.setText(getContext().getString(R.string.custom_pick_up));
                descriptionTV.setText(orderBean.carDesc);
                try {
                    timeStr = DateUtils.getWeekStrFromDate(orderBean.serviceTime) + " ";
                } catch (ParseException e) {
                    timeStr = "";
                }
                break;
            case Constants.BUSINESS_TYPE_SEND:// 送机
                titleTV.setText(getContext().getString(R.string.custom_send));
                descriptionTV.setText(orderBean.carDesc);
                try {
                    timeStr = DateUtils.getWeekStrFromDate(orderBean.serviceTime) + " ";
                } catch (ParseException e) {
                    timeStr = "";
                }
                break;
            case Constants.BUSINESS_TYPE_DAILY:// 包车
            case Constants.BUSINESS_TYPE_COMBINATION:// 组合单
                titleTV.setText(getContext().getString(R.string.custom_chartered));
                descriptionTV.setText(orderBean.carDesc);
                if (orderBean.isHalfDaily == 1) {//半日包
                    timeStr = orderBean.serviceTime + " 半天 ";
                } else {
                    timeStr = orderBean.serviceTime + " 至 " + orderBean.serviceEndTime + " " + orderBean.totalDays + "天 ";
                }
                break;
            case Constants.BUSINESS_TYPE_RENT:// 单次接送
                titleTV.setText(getContext().getString(R.string.custom_single));
                descriptionTV.setText(orderBean.carDesc);
                try {
                    timeStr = DateUtils.getWeekStrFromDate(orderBean.serviceTime) + " ";
                } catch (ParseException e) {
                    timeStr = "";
                }
                break;
        }
        timeStr += "(" + orderBean.serviceCityName + "时间)";
        timeTV.setText(timeStr);

        ProductDetail.Builder builder = new ProductDetail.Builder();
        builder.setTitle(titleTV.getText().toString() + " | " + descriptionTV.getText().toString());
        builder.setDesc(timeTV.getText().toString());
        builder.setNote(orderNoTV.getText().toString());
        builder.setShow(1);
        builder.setAlwaysSend(true);
        productDetail = builder.create();
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }
}
