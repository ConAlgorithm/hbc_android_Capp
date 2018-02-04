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
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.UIUtils;

import java.util.List;

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
            double flightBrandSignPrice = 0;
            double checkInPrice = 0;
            double childSeatPrice = 0;
            if (orderBean.orderType == 888) {
                lineView.setVisibility(View.VISIBLE);
                addGroupView(billLayout, R.string.order_detail_cost_chartered, "", true);//包车费用
                List<OrderBean> subOrderList = orderBean.subOrderDetail.subOrderList;
                int size = subOrderList.size();
                for (int i = 0; i < size; i++) {
                    OrderPriceInfo childPriceInfo = subOrderList.get(i).orderPriceInfo;
                    addBillView(CommonUtils.getString(R.string.order_detail_item_travel) + " " + (i + 1), "" + (int)childPriceInfo.orderPrice);
                    childSeatPrice += childPriceInfo.childSeatPrice;
                    flightBrandSignPrice += childPriceInfo.flightBrandSignPrice;
                    checkInPrice += childPriceInfo.checkInPrice;
                }
            } else {
                lineView.setVisibility(View.GONE);
                addGroupView(R.string.order_detail_cost_chartered, "" + (int)priceInfo.orderPrice);//包车费用

                flightBrandSignPrice = priceInfo.flightBrandSignPrice;
                checkInPrice = priceInfo.checkInPrice;
                childSeatPrice = priceInfo.childSeatPrice;
            }

            if (flightBrandSignPrice > 0) {//举牌费用
                addGroupView(R.string.order_detail_cost_placards, "" + (int)flightBrandSignPrice);
            }
            if (checkInPrice > 0) {//checkin费用
                addGroupView(R.string.order_detail_cost_checkin, "" + (int)checkInPrice);
            }
            if (childSeatPrice > 0) {//儿童座椅
                addGroupView(R.string.order_detail_cost_child_seats, "" + (int)childSeatPrice);
            }

            if (orderBean.hotelStatus == 1 && priceInfo.priceHotel > 0) {//是否有酒店
                addGroupView(R.string.order_detail_cost_hotel, "" + (int)priceInfo.priceHotel);
            }
            if (priceInfo.goodsOtherPriceTotal > 0) {//其他费用
                addGroupView(R.string.order_detail_cost_other, "" + (int)(priceInfo.goodsOtherPriceTotal));
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
        if (orderBean.orderType == 888 && (priceInfo.isRefund == 1 || orderBean.isGroupRefund())) {
            refundLayout.setVisibility(View.VISIBLE);
            refundItemLayout.removeAllViews();

            addGroupView(refundItemLayout, R.string.order_detail_cost_chartered, "", true);
            if (priceInfo.isRefund == 1) {//全部退款
                addBillView(refundItemLayout, CommonUtils.getString(R.string.order_detail_all_travel), "" + (int)priceInfo.refundPrice);
                setRefundPriceLayout((int)priceInfo.refundPrice, (int)priceInfo.cancelFee, priceInfo.payGatewayName, (int)priceInfo.refundTravelFund, (int)priceInfo.couponPrice, priceInfo.couponRefundStatus);
            } else {
                List<OrderBean> subOrderList = orderBean.subOrderDetail.subOrderList;
                int size = subOrderList.size();
                int allRefundPrice = 0;
                int allCancelFee = 0;
                for (int i = 0; i < size; i++) {
                    OrderPriceInfo childPriceInfo = subOrderList.get(i).orderPriceInfo;
                    if (childPriceInfo.isRefund != 1) {
                        continue;
                    }
                    addBillView(refundItemLayout, CommonUtils.getString(R.string.order_detail_item_travel) + " " + (i + 1), "" + (int)childPriceInfo.refundPrice);
                    allRefundPrice += childPriceInfo.refundPrice;
                    allCancelFee += childPriceInfo.cancelFee;
                }
                setRefundPriceLayout(allRefundPrice, allCancelFee, priceInfo.payGatewayName, (int)priceInfo.refundTravelFund, (int)priceInfo.couponPrice, priceInfo.couponRefundStatus);
            }

        } else if (priceInfo.isRefund == 1) {//已经退款
            refundLayout.setVisibility(View.VISIBLE);
            refundItemLayout.removeAllViews();
            addGroupView(refundItemLayout, R.string.order_detail_cost_refund, "" + (int) priceInfo.refundPrice, false);//退款金额
            setRefundPriceLayout((int) priceInfo.refundPrice, (int) priceInfo.cancelFee, priceInfo.payGatewayName, (int)priceInfo.refundTravelFund, (int)priceInfo.couponPrice, priceInfo.couponRefundStatus);
        } else {
            refundLayout.setVisibility(View.GONE);
        }
    }

    private void setRefundPriceLayout(int allRefundPrice, int cancelFee, String payGatewayName, int refundTravelFund, int couponPrice, int couponRefundStatus) {
        if (refundTravelFund > 0) {
            addGroupView(refundItemLayout, R.string.order_detail_cost_travelfund, "" + refundTravelFund, false, true);//旅游基金退款
        }
//        else if (couponPrice > 0) {//优惠券抵扣金额（券不可退）xx元；优惠券抵扣金额（券可退）xx元
//            int titleID = couponRefundStatus == 1 ? R.string.order_detail_refund_coupon2 : R.string.order_detail_refund_coupon;
//            addGroupView(refundItemLayout, titleID, "" + couponPrice, false, true);//优惠券退改
//        }

        if (cancelFee > 0) {
            addGroupView(refundItemLayout, R.string.order_detail_cost_withhold, "" + cancelFee, false);//订单退改扣款
        }

        String description = null;
        if (allRefundPrice <= 0 && refundTravelFund <= 0) {
            description = getContext().getString(R.string.order_detail_refund_pattern_payment, payGatewayName);
        } else {
            description = getContext().getString(R.string.order_detail_refund_description) + getContext().getString(R.string.order_detail_refund_pattern_payment, payGatewayName);
        }
        refundDescriptionTV.setText(description);
    }

    private View getBillView(String title, String price) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.include_order_detail_amount_item, null, false);
        TextView titleTV = (TextView) itemView.findViewById(R.id.order_detail_amount_title_tv);
        titleTV.setText(title);
        titleTV.setMinWidth(UIUtils.dip2px(60));
        titleTV.setTextSize(13);
        TextView priceTV = (TextView) itemView.findViewById(R.id.order_detail_amount_price_tv);
        priceTV.setTextSize(14);
        if (TextUtils.isEmpty(price)) {
            price = "0";
        }
        priceTV.setText(getContext().getString(R.string.sign_rmb) + price);
        return itemView;
    }

    private void addBillView(String title, String price) {
        addBillView(billLayout, title, price);
    }

    private void addBillView(LinearLayout parentLayout, String title, String price) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.dip2px(20));
        params.setMargins(UIUtils.dip2px(5), 0, 0, 0);
        parentLayout.addView(getBillView(title, price), params);
    }

    private View getGroupView(int titleID, String price) {
        return getGroupView(titleID, price, false, false);
    }

    private View getGroupView(int titleID, String price, boolean isHideSubTitle, boolean isRefund) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.include_order_detail_amount_item, null, false);
        itemView.findViewById(R.id.order_detail_amount_line_view).setVisibility(View.GONE);
        TextView titleTV = (TextView) itemView.findViewById(R.id.order_detail_amount_title_tv);
        titleTV.setText(getContext().getString(titleID));
        TextView priceTV = (TextView) itemView.findViewById(R.id.order_detail_amount_price_tv);
        if (isHideSubTitle) {
            priceTV.setVisibility(View.GONE);
        } else {
            priceTV.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(price)) {
                price = "0";
            }
            String priceText = getContext().getString(R.string.sign_rmb) + price;
            if (!isRefund && (titleID == R.string.order_detail_cost_coupon || titleID == R.string.order_detail_cost_travelfund)) {//旅游基金和优惠券需要加减号
                priceText =  "- " + getContext().getString(R.string.sign_rmb) + price;
            } else if (titleID == R.string.order_detail_cost_realpay) {
                priceTV.setTextColor(0xFFFF6633);
            }
            priceTV.setText(priceText);
        }
        return itemView;
    }

    private void addGroupView(int titleID, String price) {
        addGroupView(groupLayout, titleID, price, false, false);
    }

    private void addGroupView(int titleID, String price, boolean isHideSubTitle) {
        addGroupView(groupLayout, titleID, price, isHideSubTitle, false);
    }

    private void addGroupView(LinearLayout parentLayout, int titleID, String price, boolean isHideSubTitle) {
        addGroupView(parentLayout, titleID, price, isHideSubTitle, false);
    }

    private void addGroupView(LinearLayout parentLayout, int titleID, String price, boolean isHideSubTitle, boolean isRefund) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.dip2px(26));
        parentLayout.addView(getGroupView(titleID, price, isHideSubTitle, isRefund), params);
    }
}
