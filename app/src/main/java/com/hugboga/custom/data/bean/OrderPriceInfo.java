package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZHZEPHI on 2015/7/20.
 */
public class OrderPriceInfo implements IBaseBean ,Parcelable{

    public double orderPrice;  //订单金额
    public double shouldPay;   //应付
    public double actualPay;           //实付金额
    public double refundablePrice;    // 可退款金额
    public double checkInPrice;// check in费用
    public double couponPrice;//优惠价格
    public double travelFundPrice;//旅游基金
    public double childSeatPrice;//儿童座椅价格
    public double flightBrandSignPrice;//举牌价格
    public double refundPrice;        // 退款金额
    public double cancelFee;//退改费用
    public double priceHotel;// 住宿总费用(单价 * hotelRoom * hotelDays)
    public double refundTravelFund;//退旅游基金金额
    public double travelFundRefundable;//旅游基金可退
    public int couponRefundStatus;//0= 无损显示-优惠券抵扣金额（券不可退） 1=有损显示-优惠券抵扣金额（券可退）

    public int isRefund;//是否已经退款 0没有退款 1 已经退款
    public int payGateway;//支付方式 1:支付宝,2:微信,3:内部账户支付,4:券支付,5:旅游基金,6:百付宝,17:QUNA渠道,20:携程渠道,19:去啊渠道,
    public String payGatewayName;//支付方式的文字描述

    public void parser(JSONObject jsonObj) throws JSONException {
        if (jsonObj == null) return;
        orderPrice = jsonObj.optDouble("orderPrice", 0);
        shouldPay = jsonObj.optDouble("shouldPay", 0);
        actualPay = jsonObj.optDouble("actualPay", 0);
        checkInPrice = jsonObj.optDouble("checkInPrice");
        refundPrice = jsonObj.optDouble("refundPrice", 0);
        refundablePrice = jsonObj.optDouble("refundablePrice", 0);
        cancelFee = jsonObj.optDouble("cancelFee", 0);
        couponPrice = jsonObj.optDouble("couponPrice", 0);
        travelFundPrice = jsonObj.optDouble("travelFundPrice", 0);
        childSeatPrice = jsonObj.optDouble("childSeatPrice", 0);
        flightBrandSignPrice = jsonObj.optDouble("flightBrandSignPrice", 0);
        priceHotel = jsonObj.optDouble("priceHotel", 0);
        isRefund = jsonObj.optInt("isRefund");
        payGateway = jsonObj.optInt("payGateway");
        payGatewayName = jsonObj.optString("payGatewayName");
        refundTravelFund = jsonObj.optDouble("refundTravelFund", 0);
        travelFundRefundable = jsonObj.optDouble("travelFundRefundable", 0);
        couponRefundStatus = jsonObj.optInt("couponRefundStatus", 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.orderPrice);
        dest.writeDouble(this.shouldPay);
        dest.writeDouble(this.actualPay);
        dest.writeDouble(this.refundPrice);
        dest.writeDouble(this.refundablePrice);
        dest.writeDouble(this.checkInPrice);
        dest.writeDouble(this.cancelFee);
        dest.writeDouble(this.couponPrice);
        dest.writeDouble(this.travelFundPrice);
        dest.writeDouble(this.childSeatPrice);
        dest.writeDouble(this.flightBrandSignPrice);
        dest.writeDouble(this.priceHotel);
        dest.writeInt(this.isRefund);
        dest.writeInt(this.payGateway);
        dest.writeString(this.payGatewayName);
        dest.writeDouble(this.refundTravelFund);
        dest.writeDouble(this.travelFundRefundable);
        dest.writeInt(this.couponRefundStatus);
    }

    public OrderPriceInfo() {
    }

    protected OrderPriceInfo(Parcel in) {
        this.orderPrice = in.readDouble();
        this.shouldPay = in.readDouble();
        this.actualPay = in.readDouble();
        this.refundPrice = in.readDouble();
        this.refundablePrice = in.readDouble();
        this.checkInPrice = in.readDouble();
        this.cancelFee = in.readDouble();
        this.couponPrice = in.readDouble();
        this.travelFundPrice = in.readDouble();
        this.childSeatPrice = in.readDouble();
        this.flightBrandSignPrice = in.readDouble();
        this.priceHotel = in.readDouble();
        this.isRefund = in.readInt();
        this.payGateway = in.readInt();
        this.payGatewayName = in.readString();
        this.refundTravelFund = in.readDouble();
        this.travelFundRefundable = in.readDouble();
        this.couponRefundStatus = in.readInt();
    }

    public static final Creator<OrderPriceInfo> CREATOR = new Creator<OrderPriceInfo>() {
        @Override
        public OrderPriceInfo createFromParcel(Parcel source) {
            return new OrderPriceInfo(source);
        }

        @Override
        public OrderPriceInfo[] newArray(int size) {
            return new OrderPriceInfo[size];
        }
    };
}
