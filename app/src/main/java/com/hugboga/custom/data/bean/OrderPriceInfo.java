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
    public double refundPrice;        // 退款金额
    public double refundablePrice;    // 可退款金额
    public double checkInPrice;// check in费用
    public double cancelFee;//退改费用
    public double priceFlightBrandSign;//举牌接机

    public void parser(JSONObject jsonObj) throws JSONException {
        if (jsonObj == null) return;
        orderPrice = jsonObj.optDouble("orderPrice");
        shouldPay = jsonObj.optDouble("shouldPay");
        actualPay = jsonObj.optDouble("actualPay");
        checkInPrice = jsonObj.optDouble("checkInPrice");
        refundPrice = jsonObj.optDouble("refundPrice");
        refundablePrice = jsonObj.optDouble("refundablePrice");
        cancelFee = jsonObj.optDouble("cancelFee", 0);
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
