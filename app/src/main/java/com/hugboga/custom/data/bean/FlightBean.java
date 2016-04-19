package com.hugboga.custom.data.bean;

/**
 * 航班信息
 */
public class FlightBean implements IBaseBean {

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

}
