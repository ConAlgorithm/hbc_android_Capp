package com.hugboga.custom.utils;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CalendarGoodsBean;
import com.hugboga.custom.data.bean.CalendarGoodsBeanList;
import com.hugboga.custom.data.request.RequestQueryGoodsStock;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Date;

public class SkuCalendarUtils {

    public static final int INIT = 0;
    public static final int SUCCEED = 1;
    public static final int ERROR = 2;

    public static final int ADVANCED_MONTH = 1;//预先加载下X月数据

    private static SkuCalendarUtils skuCalendarUtils;

    private Context context;
    private String goodsNo;
    private String guideId;
    private String selectedYearMonthStr;
    private ArrayMap<String, CalendarGoodsBean> goodsCalendarMap;
    private ArrayMap<String, Integer> requestedMonthMap;
    private SkuCalendarListenr skuCalendarListenr;
    private boolean isDestory = false;

    private SkuCalendarUtils() {
        goodsCalendarMap = new ArrayMap<String, CalendarGoodsBean>();
        requestedMonthMap = new ArrayMap<String, Integer>();
    }

    public static SkuCalendarUtils getInstance() {
        if (skuCalendarUtils == null) {
            skuCalendarUtils = new SkuCalendarUtils();
        }
        return skuCalendarUtils;
    }

    public void onDestory() {
        isDestory = true;
        goodsCalendarMap.clear();
        requestedMonthMap.clear();
        context = null;
    }

    @IntDef({INIT, SUCCEED, ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RequestState {}

    public void sendRequest(Context context, String goodsNo, String guideId, Date _date) {
        isDestory = false;
        sendRequest(context, goodsNo, guideId, _date, ADVANCED_MONTH);
    }

    private void sendRequest(Context context, String goodsNo, String guideId, Date _date, int time) {
        this.context = context;
        this.goodsNo = goodsNo;
        this.guideId = guideId;

        int requestedSize = requestedMonthMap.size();

        Calendar calendar = Calendar.getInstance();
        int maxRequestCount = 0;
        if ("1".equals(calendar.get(Calendar.DAY_OF_MONTH) + "")) {//最大请求月数，如果当前天是1号，请求6个月，不是则请求7个月
            maxRequestCount = 6;
        } else {
            maxRequestCount = 7;
        }

        String currentYearMonthsStr = getDateYearMonthStr(calendar);
        Date currentdate = calendar.getTime();

        calendar.setTime(_date);
        String yearMonthStr = getDateYearMonthStr(calendar);

        boolean isCurrentMonth = false;//是否是当前月份
        if (time == ADVANCED_MONTH) {
            selectedYearMonthStr = yearMonthStr;
            isCurrentMonth = true;
        }

        if (requestedMonthMap.containsKey(yearMonthStr)) {
            setLoadViewShow(isCurrentMonth, requestedMonthMap.get(yearMonthStr) == INIT);
            if (requestedSize < maxRequestCount && time > 0) {
                sendRequest(context, goodsNo, guideId, DateUtils.addMonth(_date, 1), --time);
            }
            return;
        } else {
            setLoadViewShow(isCurrentMonth, true);
            requestedMonthMap.put(yearMonthStr, INIT);
            requestedSize = requestedMonthMap.size();
        }

        if (TextUtils.equals(yearMonthStr, currentYearMonthsStr)) {
            getCalendarList(yearMonthStr, DateUtils.dateDateFormat.format(currentdate), getMonthLastDay(calendar));
        } else {
            String lastDay = "";
            if (maxRequestCount == requestedSize) {
                Calendar endDateCalendar = Calendar.getInstance();
                endDateCalendar.add(Calendar.MONTH, 6);
                lastDay = getMonthDay(endDateCalendar, endDateCalendar.get(Calendar.DATE) - 1);
            } else {
                lastDay = getMonthLastDay(calendar);
            }
            getCalendarList(yearMonthStr, getMonthDay(calendar, 1), lastDay);
        }

        if (requestedSize < maxRequestCount && time > 0) {
            sendRequest(context, goodsNo, guideId, DateUtils.addMonth(_date, 1), --time);
        }
    }

    private void getCalendarList(String yearMonthStr, String startServiceDate, String endServiceDate) {
        Log.i("aa", "yearMonthStr " + yearMonthStr + " startServiceDate " + startServiceDate + "    endServiceDate  " + endServiceDate);
        RequestQueryGoodsStock requestCars = new RequestQueryGoodsStock(context, goodsNo, guideId, startServiceDate, endServiceDate, yearMonthStr);
        HttpRequestUtils.request(context, requestCars, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                if (isDestory) return;
                ApiReportHelper.getInstance().addReport(request);
                RequestQueryGoodsStock requestCalendarList = (RequestQueryGoodsStock) request;
                CalendarGoodsBeanList calendarGoodsBeanList = requestCalendarList.getData();
                Log.i("aa", "onDataRequestSucceed  请求成功  " + requestCalendarList.tag);

                if (skuCalendarListenr != null && getDateYearMonthStr(Calendar.getInstance()).equals(requestCalendarList.tag)) {
                    skuCalendarListenr.onCalendarInit(calendarGoodsBeanList.getStartCalendar());
                }
                goodsCalendarMap.putAll(calendarGoodsBeanList.getMonthMap());
                requestedMonthMap.put(requestCalendarList.tag, SUCCEED);
                boolean isCurrentMonth = TextUtils.equals(selectedYearMonthStr, requestCalendarList.tag);
                setLoadViewShow(isCurrentMonth, false);
                if (skuCalendarListenr != null) {
                    skuCalendarListenr.onCalendarRequestSucceed(isCurrentMonth, goodsCalendarMap);
                }
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
                if (isDestory) return;
                RequestQueryGoodsStock requestCalendarList = (RequestQueryGoodsStock) request;
                requestedMonthMap.put(requestCalendarList.tag, ERROR);
                Log.i("aa", "onDataRequestError  请求失败  " + requestCalendarList.tag);
                setLoadViewShow(TextUtils.equals(selectedYearMonthStr, requestCalendarList.tag), false);
            }
        }, false);
    }

    public interface SkuCalendarListenr {
        public void onCalendarInit(Calendar startCalendar);
        public void onCalendarRequestSucceed(boolean isCurrentMonth, ArrayMap<String, CalendarGoodsBean> guideCalendarMap);
        public void onLoadViewShow(boolean isShow);
    }

    public void setSkuCalendarListenr(SkuCalendarListenr skuCalendarListenr) {
        this.skuCalendarListenr = skuCalendarListenr;
    }

    public void setLoadViewShow(boolean isCurrentMonth, boolean isShow) {
        if (isCurrentMonth && skuCalendarListenr != null) {
            skuCalendarListenr.onLoadViewShow(isShow);
        }
    }

    public String getDateYearMonthStr(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String divide = month < 10 ? "-0" : "-";
        return year + divide + month;
    }

    public String getDateStr(int year, int month, int day) {
        String leftDivide = month < 10 ? "-0" : "-";
        String rightDivide = day < 10 ? "-0" : "-";
        return year + leftDivide + month + rightDivide + day;
    }

    public String getMonthDay(Calendar calendar, int day) {
        return getDateStr(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, day);
    }

    public String getMonthLastDay(Calendar calendar) {
        return getDateStr(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.getActualMaximum(Calendar.DATE));
    }
}
