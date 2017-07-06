package com.hugboga.custom.utils;

import android.content.Context;
import android.util.Log;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.request.RequestCalendarList;
import com.squareup.timessquare.CalendarListBean;

import java.util.Calendar;
import java.util.HashMap;

public class GuideCalendarUtils {

    private static GuideCalendarUtils guideCalendarUtils;

    private Context context;
    private HashMap<String, CalendarListBean> guideCalendarMap;
    private int succeedCount;
    private String guideId;
    private int orderType;
    private int requestTag;//通过该字段来比较是否处理接口回调
    private OnAllRequestSucceedListener listener;

    private GuideCalendarUtils() {
        guideCalendarMap = new HashMap<String, CalendarListBean>();
    }

    public static GuideCalendarUtils getInstance() {
        if (guideCalendarUtils == null) {
            guideCalendarUtils = new GuideCalendarUtils();
        }
        return guideCalendarUtils;
    }

    public void onDestory() {
        guideCalendarMap.clear();
        requestTag++;
        context = null;
    }

    public void sendRequest(Context context, String guideId, int orderType) {
        this.context = context;
        this.guideId = guideId;
        this.orderType = orderType;
        requestTag++;
        Calendar now = Calendar.getInstance();
        if ("1".equals(now.get(Calendar.DAY_OF_MONTH) + "")) {//如果当前天是1号，请求6个月，不是则请求7个月
            succeedCount = 6;
        } else {
            succeedCount = 7;
        }
        for (int i = 0; i < succeedCount; i++) {
            int month = now.get(Calendar.MONTH) + 1 + i;
            int year = now.get(Calendar.YEAR);
            if (month > 12) {
                month = month - 12;
                year++;
            }
            String guideMonth = null;
            if (month < 10) {
                guideMonth = year + "-0" + month;
            } else {
                guideMonth = year + "-" + month;
            }
            getCalendarList(guideMonth);
        }
    }

    public boolean isRequestSucceed() {
        return succeedCount == 0;
    }

    public HashMap<String, CalendarListBean> getCalendarMap() {
        return guideCalendarMap;
    }

    public CalendarListBean getCalendarListBean(String date) {
        if (guideCalendarMap != null && guideCalendarMap.containsKey(date)) {
            return guideCalendarMap.get(date);
        } else {
            return null;
        }
    }

    private void getCalendarList(String guideMonth) {
        RequestCalendarList requestCars = new RequestCalendarList(context, guideMonth, guideId, orderType, requestTag + "");
        HttpRequestUtils.request(context, requestCars, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {
                ApiReportHelper.getInstance().addReport(request);
                RequestCalendarList requestCalendarList = (RequestCalendarList) request;
                if (!(requestTag + "").equals(requestCalendarList.tag)) {
                    return;
                }
                HashMap<String, CalendarListBean> itemMap = requestCalendarList.getData();
                guideCalendarMap.putAll(itemMap);
                succeedCount--;
                if (listener != null && succeedCount == 0) {
                    listener.onAllRequestSucceed(guideCalendarMap);
                }
            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
            }
        }, false);
    }

    public interface OnAllRequestSucceedListener {
        public void onAllRequestSucceed(HashMap<String, CalendarListBean> guideCalendarMap);
    }

    public void setOnAllRequestSucceedListener(OnAllRequestSucceedListener listener) {
        this.listener = listener;
    }
}
