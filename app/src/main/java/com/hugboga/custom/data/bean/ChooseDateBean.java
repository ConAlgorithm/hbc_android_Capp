package com.hugboga.custom.data.bean;


import java.util.Date;

public class ChooseDateBean implements IBaseBean {

    public String start_date;
    public String end_date;
    public String showStartDateStr;
    public String showEndDateStr;
    public String showHalfDateStr;
    public String halfDateStr;
    public Date startDate;
    public Date endDate;
    public Date halfDate;
    public int dayNums;
    public boolean isToday;
    public int type;//1,单天,2 多天
    public Date minDate;

    public ChooseDateBean() {
    }


}
