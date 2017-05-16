package com.hugboga.custom.widget.calendar;

import java.util.Calendar;

public class CalendarCell {

    Calendar calendar; //单元日期
    String name; //cell显示内容
    Integer type; //cell类型
    int txtColor; //文字颜色
    int txtSize;//文字大小
    boolean isenable; //是否可选择

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public int getTxtColor() {
        return txtColor;
    }

    public void setTxtColor(int txtColor) {
        this.txtColor = txtColor;
    }

    public int getTxtSize() {
        return txtSize;
    }

    public void setTxtSize(int txtSize) {
        this.txtSize = txtSize;
    }

    public boolean isenable() {
        return isenable;
    }

    public void setIsenable(boolean isenable) {
        this.isenable = isenable;
    }
}
