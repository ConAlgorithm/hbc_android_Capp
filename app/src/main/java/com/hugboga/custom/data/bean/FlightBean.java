package com.hugboga.custom.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 航班信息
 */
public class FlightBean implements IBaseBean ,Parcelable{

    /**
     *
     */
    private static final long serialVersionUID = -4484992592489918542L;

    public String flightNo;
    public String company;//"中国国际航空股份有限公司"
    //	public String date; //到达日期
    public String depTime;//计划出发时间 HH-mm
    public String arrivalTime;//计划到达时间 HH-mm
    public String depAirportCode;// 出发机场三字码
    public String arrivalAirportCode;// 到达机场三字码
    public String depAirportName; //出发机场
    public String arrAirportName; //到达机场
    public String depTerminal; //候机楼
    public String arrTerminal; //接机楼
    public String arrDate; //计划到达时间
    public String depDate; //计划出发时间

    public String depCityName; // 出发城市名
    public String arrCityName; // 到达城市名


    public boolean serviceStatus = true;

    public AirPort depAirport;
    public AirPort arrivalAirport;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.flightNo);
        dest.writeString(this.company);
        dest.writeString(this.depTime);
        dest.writeString(this.arrivalTime);
        dest.writeString(this.depAirportCode);
        dest.writeString(this.arrivalAirportCode);
        dest.writeString(this.depAirportName);
        dest.writeString(this.arrAirportName);
        dest.writeString(this.depTerminal);
        dest.writeString(this.arrTerminal);
        dest.writeString(this.arrDate);
        dest.writeString(this.depDate);
        dest.writeString(this.depCityName);
        dest.writeString(this.arrCityName);
        dest.writeByte(serviceStatus ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.depAirport, flags);
        dest.writeParcelable(this.arrivalAirport, flags);
    }

    public FlightBean() {
    }

    protected FlightBean(Parcel in) {
        this.flightNo = in.readString();
        this.company = in.readString();
        this.depTime = in.readString();
        this.arrivalTime = in.readString();
        this.depAirportCode = in.readString();
        this.arrivalAirportCode = in.readString();
        this.depAirportName = in.readString();
        this.arrAirportName = in.readString();
        this.depTerminal = in.readString();
        this.arrTerminal = in.readString();
        this.arrDate = in.readString();
        this.depDate = in.readString();
        this.depCityName = in.readString();
        this.arrCityName = in.readString();
        this.serviceStatus = in.readByte() != 0;
        this.depAirport = in.readParcelable(AirPort.class.getClassLoader());
        this.arrivalAirport = in.readParcelable(AirPort.class.getClassLoader());
    }

    public static final Creator<FlightBean> CREATOR = new Creator<FlightBean>() {
        @Override
        public FlightBean createFromParcel(Parcel source) {
            return new FlightBean(source);
        }

        @Override
        public FlightBean[] newArray(int size) {
            return new FlightBean[size];
        }
    };
}
