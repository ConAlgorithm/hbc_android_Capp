package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created  on 16/4/24.
 */
public class InsureListBean implements Parcelable, Serializable {
    public  String  insuranceUserName;//下保险人
    public  String  passportNo;//护照号
    public  String  createtime;//下保险时间
    public  String  orderNo;//C120341171520",
    public  String  insuranceUserId;//IU91440316919",
    public int insuranceStatus;
    public  String  source;
    public  String  userId;
    public  String  hbcFee;//保险金额
    public  String  insuranceNo;//保单编号
    public  String  beginTime;//保单生效开始日期
    public  String  endTime;//保单生效结束日期
    public  String  updatetime;//2016-04-18 16:18:03"
    public  String  policyNo; //新的保单号

    public boolean isResubmit = false;//本地字段 是否重试

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.insuranceUserName);
        dest.writeString(this.passportNo);
        dest.writeString(this.createtime);
        dest.writeString(this.orderNo);
        dest.writeString(this.insuranceUserId);
        dest.writeInt(this.insuranceStatus);
        dest.writeString(this.source);
        dest.writeString(this.userId);
        dest.writeString(this.hbcFee);
        dest.writeString(this.insuranceNo);
        dest.writeString(this.beginTime);
        dest.writeString(this.endTime);
        dest.writeString(this.updatetime);
        dest.writeString(this.policyNo);
    }

    public InsureListBean() {
    }

    protected InsureListBean(Parcel in) {
        this.insuranceUserName = in.readString();
        this.passportNo = in.readString();
        this.createtime = in.readString();
        this.orderNo = in.readString();
        this.insuranceUserId = in.readString();
        this.insuranceStatus = in.readInt();
        this.source = in.readString();
        this.userId = in.readString();
        this.hbcFee = in.readString();
        this.insuranceNo = in.readString();
        this.beginTime = in.readString();
        this.endTime = in.readString();
        this.updatetime = in.readString();
        this.policyNo = in.readString();
    }

    public static final Creator<InsureListBean> CREATOR = new Creator<InsureListBean>() {
        @Override
        public InsureListBean createFromParcel(Parcel source) {
            return new InsureListBean(source);
        }

        @Override
        public InsureListBean[] newArray(int size) {
            return new InsureListBean[size];
        }
    };

    public String getUserStatusString() {
        String result = null;
        switch (insuranceStatus) {
            case 1:
            case 2:
                result = "购买中";
                break;
            case 3:
                result = "购买成功";
                break;
            case 4:
                result = "购买失败";
                break;
            case 5:
                result = "注销中";
                break;
            case 6:
                result = "注销成功";
                break;
            case 7:
                result = "注销失败";
                break;
            case 8:
                result = "有问题";
                break;
        }
        return result;
    }

}
