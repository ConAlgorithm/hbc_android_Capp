package com.hugboga.custom.data.bean;


import android.support.v4.util.ArrayMap;

import com.google.gson.annotations.SerializedName;
import com.hugboga.custom.utils.DateUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by qingcha on 17/8/4.
 */
public class CalendarGoodsBeanList implements Serializable {

    @SerializedName(value = "goodsStockList", alternate = {"guideGoodsStockList"})
    public ArrayList<CalendarGoodsBean> goodsStockList;

    public Map<String, CalendarGoodsBean> getMonthMap() {
        ArrayMap<String, CalendarGoodsBean> map = new ArrayMap<>();
        if (goodsStockList == null || goodsStockList.size() <= 0) {
            return map;
        }
        for (CalendarGoodsBean item : goodsStockList) {
            map.put(item.serviceDate, item);
        }
        return map;
    }

    public Calendar getStartCalendar() {
        if (goodsStockList == null || goodsStockList.size() <= 0) {
            return null;
        }
        for (CalendarGoodsBean item : goodsStockList) {
            if (item.isPastDate()) {
                continue;
            }
            Date date = DateUtils.getFormatDate(item.serviceDate);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar;
            }
        }
        return null;
    }

    public Calendar getSelectedCalendar() {
        if (goodsStockList == null || goodsStockList.size() <= 0) {
            return null;
        }
        for (CalendarGoodsBean item : goodsStockList) {
            if (!item.isCanService()) {
                continue;
            }
            Date date = DateUtils.getFormatDate(item.serviceDate);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                calendar.setTime(date);
                int month = calendar.get(Calendar.MONTH) + 1;
                return month == currentMonth ? calendar : null;
            }
        }
        return null;
    }

}
