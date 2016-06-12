package com.hugboga.custom.data.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class MostFitBean implements  IBaseBean,Parcelable{

    /**
     * actualPrice : 153
     * applyArea : 全球通用
     * applyCar : 经济型,舒适型,豪华型,奢华型
     * applyCarClass : 5座车系,7座车系,9座车系,12座车系
     * applyType : 接机,送机
     * batchChannel : 你好全世界
     * bindDateTime : 2016-05-20 19:01:55
     * bindUserId : 111496013819
     * bindUserType : 2
     * content : 1.每位用户只可领用一次，仅限皇包车APP下单使用；
     2.每张订单仅可使用一张优惠券；
     3. 如订单发生退款或取消，优惠券会原路退回，可再次使用。
     * couponBatchId : 111446016914
     * couponBatchName : Hi全世界30元接送机券
     * couponCode : hxttyff4
     * couponId : 2114428249168
     * couponPrice : 30
     * couponType : 1
     * createTime : 2016-05-14 20:56:22
     * distanceUpperLimit : 0
     * durationUpperLimit : 0
     * endTime : 2016.12.31
     * orderRuleRemark : 最多可抵用30元|无用车满减限制|无用车里程限制|无用车时长限制
     * priceInfo : 30元
     * startTime : 2016.05.20
     * status : 1
     */
        public int actualPrice;
        public String applyArea;
        public String applyCar;
        public String applyCarClass;
        public String applyType;
        public String batchChannel;
        public String bindDateTime;
        public String bindUserId;
        public int bindUserType;
        public String content;
        public String couponBatchId;
        public String couponBatchName;
        public String couponCode;
        public String couponId;
        public int couponPrice;
        public int couponType;
        public String createTime;
        public int distanceUpperLimit;
        public int durationUpperLimit;
        public String endTime;
        public String orderRuleRemark;
        public String priceInfo;
        public String startTime;
        public int status;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.actualPrice);
        dest.writeString(this.applyArea);
        dest.writeString(this.applyCar);
        dest.writeString(this.applyCarClass);
        dest.writeString(this.applyType);
        dest.writeString(this.batchChannel);
        dest.writeString(this.bindDateTime);
        dest.writeString(this.bindUserId);
        dest.writeInt(this.bindUserType);
        dest.writeString(this.content);
        dest.writeString(this.couponBatchId);
        dest.writeString(this.couponBatchName);
        dest.writeString(this.couponCode);
        dest.writeString(this.couponId);
        dest.writeInt(this.couponPrice);
        dest.writeInt(this.couponType);
        dest.writeString(this.createTime);
        dest.writeInt(this.distanceUpperLimit);
        dest.writeInt(this.durationUpperLimit);
        dest.writeString(this.endTime);
        dest.writeString(this.orderRuleRemark);
        dest.writeString(this.priceInfo);
        dest.writeString(this.startTime);
        dest.writeInt(this.status);
    }

    public MostFitBean() {
    }

    protected MostFitBean(Parcel in) {
        this.actualPrice = in.readInt();
        this.applyArea = in.readString();
        this.applyCar = in.readString();
        this.applyCarClass = in.readString();
        this.applyType = in.readString();
        this.batchChannel = in.readString();
        this.bindDateTime = in.readString();
        this.bindUserId = in.readString();
        this.bindUserType = in.readInt();
        this.content = in.readString();
        this.couponBatchId = in.readString();
        this.couponBatchName = in.readString();
        this.couponCode = in.readString();
        this.couponId = in.readString();
        this.couponPrice = in.readInt();
        this.couponType = in.readInt();
        this.createTime = in.readString();
        this.distanceUpperLimit = in.readInt();
        this.durationUpperLimit = in.readInt();
        this.endTime = in.readString();
        this.orderRuleRemark = in.readString();
        this.priceInfo = in.readString();
        this.startTime = in.readString();
        this.status = in.readInt();
    }

    public static final Creator<MostFitBean> CREATOR = new Creator<MostFitBean>() {
        @Override
        public MostFitBean createFromParcel(Parcel source) {
            return new MostFitBean(source);
        }

        @Override
        public MostFitBean[] newArray(int size) {
            return new MostFitBean[size];
        }
    };
}
