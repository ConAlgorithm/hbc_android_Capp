package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.OrderPriceInfo;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/2.
 *
 * 单次接送：包车费用、儿童座椅
 * 接机：包车费用、举牌费用、儿童座椅
 * 送机：包车费用、checkin费用、儿童座椅
 * 包车游、线路包车游：包车费用
 * 1）除了包车费用，剩下的费用只有勾选了这项增加服务才会显示
 * 2）包车游、线路包车游不单独展示儿童座椅费用（但是可以选儿童座椅数量）
 */
public class OrderDetailAmountView extends LinearLayout implements HbcViewBehavior  {

    private LinearLayout billLayout;
    private LinearLayout groupLayout;
    private View lineView;

    private LinearLayout refundLayout;
    private LinearLayout refundItemLayout;
    private TextView refundDescriptionTV;

    public OrderDetailAmountView(Context context) {
        this(context, null);
    }

    public OrderDetailAmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_order_detail_amount, this);
        billLayout = (LinearLayout) findViewById(R.id.order_detail_amount_bill_layout);
        groupLayout = (LinearLayout) findViewById(R.id.order_detail_amount_group_layout);
        lineView = findViewById(R.id.order_detail_amount_line_view);

        refundLayout = (LinearLayout) findViewById(R.id.order_detail_amount_refund_layout);
        refundItemLayout = (LinearLayout) findViewById(R.id.order_detail_amount_refund_item_layout);
        refundDescriptionTV = (TextView) findViewById(R.id.order_detail_amount_refund_description_tv);
    }

    @Override
    public void update(Object _data) {
        if (_data == null) {
            return;
        }
        OrderBean orderBean = (OrderBean) _data;

        billLayout.removeAllViews();
        groupLayout.removeAllViews();

        OrderPriceInfo priceInfo = orderBean.orderPriceInfo;
        if (orderBean.orderSource == 1) {
            lineView.setVisibility(View.VISIBLE);
            addBillView(R.string.order_detail_cost_chartered, "" + (int)priceInfo.orderPrice);//包车费用
            if (orderBean.orderGoodsType == 1 && priceInfo.flightBrandSignPrice > 0) {//接机 举牌费用
                addBillView(R.string.order_detail_cost_placards, "" + (int)priceInfo.flightBrandSignPrice);
            } else if(orderBean.orderGoodsType == 2 && !Double.isNaN(priceInfo.checkInPrice) && priceInfo.checkInPrice > 0) {//送机 checkin费用
                addBillView(R.string.order_detail_cost_checkin, "" + (int)priceInfo.checkInPrice);
            }
            if (priceInfo.childSeatPrice > 0) {
                addBillView(R.string.order_detail_cost_child_seats, "" + (int)priceInfo.childSeatPrice);//儿童座椅
            }
            if (orderBean.hotelStatus == 1 && priceInfo.priceHotel > 0) {//是否有酒店
                addBillView(R.string.order_detail_cost_hotel, "" + (int)priceInfo.priceHotel);
            }

            addGroupView(R.string.order_detail_cost_total, "" + (int)priceInfo.shouldPay);//费用总计
            if (priceInfo.couponPrice != 0) {
                addGroupView(R.string.order_detail_cost_coupon, "" + (int)priceInfo.couponPrice);//优惠金额
            }
            if (priceInfo.travelFundPrice != 0) {
                addGroupView(R.string.order_detail_cost_travelfund, "" + (int)priceInfo.travelFundPrice);//旅游基金
            }
            addGroupView(R.string.order_detail_cost_realpay, "" + (int)priceInfo.actualPay);//实付款
        } else {
            lineView.setVisibility(View.GONE);
            addGroupView(R.string.order_detail_cost_total, "" + (int)priceInfo.shouldPay);//费用总计
            addGroupView(R.string.order_detail_cost_realpay, "" + (int)priceInfo.actualPay);//实付款
        }

        setRefundLayout(orderBean, priceInfo);//退款详情
    }

    /**
     * 退款详情
     * */
    private void setRefundLayout(OrderBean orderBean, OrderPriceInfo priceInfo) {
        if (orderBean.orderSource != 1) {//非app下单不显示退款
            return;
        }
        if (priceInfo.isRefund == 1) {//已经退款
            refundLayout.setVisibility(View.VISIBLE);

            refundItemLayout.removeAllViews();
            addGroupView(refundItemLayout, R.string.order_detail_cost_refund, "" + (int) priceInfo.refundPrice);//退款金额

            String description = null;
            if (priceInfo.refundPrice <= 0) {//退款金额为0
                description = getContext().getString(R.string.order_detail_refund_pattern_payment, priceInfo.payGatewayName);
            } else {
                View withholdView = getGroupView(R.string.order_detail_cost_withhold, "" + (int) priceInfo.cancelFee);//订单退改扣款
                ((TextView) withholdView.findViewById(R.id.order_detail_amount_title_tv)).setTextSize(13);
                ((TextView) withholdView.findViewById(R.id.order_detail_amount_price_tv)).setTextSize(14);
                LinearLayout.LayoutParams withholdViewParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.dip2px(35));
                withholdViewParams.setMargins(UIUtils.dip2px(10), 0, UIUtils.dip2px(10), 0);
                refundItemLayout.addView(withholdView, withholdViewParams);

                description = getContext().getString(R.string.order_detail_refund_description) + getContext().getString(R.string.order_detail_refund_pattern_payment, priceInfo.payGatewayName);
            }
            refundDescriptionTV.setText(description);
        } else {
            refundLayout.setVisibility(View.GONE);
        }
    }


    private View getBillView(int titleID, String price) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.include_order_detail_amount_item, null, false);
        TextView titleTV = (TextView) itemView.findViewById(R.id.order_detail_amount_title_tv);
        titleTV.setText(getContext().getString(titleID));
        titleTV.setMinWidth(UIUtils.dip2px(80));
        titleTV.setTextSize(13);
        TextView priceTV = (TextView) itemView.findViewById(R.id.order_detail_amount_price_tv);
        priceTV.setTextSize(14);
        if (TextUtils.isEmpty(price)) {
            price = "0";
        }
        priceTV.setText(getContext().getString(R.string.sign_rmb) + price);
        return itemView;
    }

    private void addBillView(int titleID, String price) {
        addBillView(billLayout, titleID, price);
    }

    private void addBillView(LinearLayout parentLayout, int titleID, String price) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.dip2px(35));
        params.setMargins(UIUtils.dip2px(20), 0,UIUtils.dip2px(20), 0);
        parentLayout.addView(getBillView(titleID, price), params);
    }

    private View getGroupView(int titleID, String price) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.include_order_detail_amount_item, null, false);
        TextView titleTV = (TextView) itemView.findViewById(R.id.order_detail_amount_title_tv);
        titleTV.setText(getContext().getString(titleID));
        TextView priceTV = (TextView) itemView.findViewById(R.id.order_detail_amount_price_tv);
        if (TextUtils.isEmpty(price)) {
            price = "0";
        }
        String priceText = getContext().getString(R.string.sign_rmb) + price;
        if (titleID == R.string.order_detail_cost_coupon || titleID == R.string.order_detail_cost_travelfund) {//旅游基金和优惠券需要加减号
            priceText = getContext().getString(R.string.sign_rmb) + " -" + price;
        } else if (titleID == R.string.order_detail_cost_realpay) {
            priceTV.setTextColor(0xFFFF6633);
        }
        priceTV.setText(priceText);
        return itemView;
    }

    private void addGroupView(int titleID, String price) {
        addGroupView(groupLayout, titleID, price);
    }

    private void addGroupView(LinearLayout parentLayout, int titleID, String price) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.dip2px(35));
        params.setMargins(UIUtils.dip2px(10), 0, UIUtils.dip2px(10), 0);
        parentLayout.addView(getGroupView(titleID, price), params);
    }
}
