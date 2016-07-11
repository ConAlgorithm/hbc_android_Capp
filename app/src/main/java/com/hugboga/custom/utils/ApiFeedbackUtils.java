package com.hugboga.custom.utils;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestApiFeedback;

import io.rong.imlib.RongIMClient;

/**
 * Created by qingcha on 16/7/9.
 */
public final class ApiFeedbackUtils {

    private ApiFeedbackUtils() {}

    private static final String SEPARATOR = "|";

    public static void requestIMFeedback(int errorMessage, String errorCode) {
        requestIMFeedback(errorMessage, errorCode, null);
    }

    public static void requestIMFeedback(int errorMessage, String errorCode, String describeStr) {
        requestAPIFeedback(UserEntity.getUser().getUserId(MyApplication.getAppContext()),
                ApiFeedbackUtils.getImErrorFeedback(errorMessage, errorCode, describeStr));
    }

    public static void requestAPIFeedback(String userId, String error) {
        RequestApiFeedback requestApiFeedback = new RequestApiFeedback(MyApplication.getAppContext(), userId, error);
        HttpRequestUtils.request(MyApplication.getAppContext(), requestApiFeedback, new HttpRequestListener() {
            @Override
            public void onDataRequestSucceed(BaseRequest request) {

            }

            @Override
            public void onDataRequestCancel(BaseRequest request) {

            }

            @Override
            public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

            }
        }, false);
    }

    public static String getImErrorFeedback(int errorMessage, String errorCode) {
        return getImErrorFeedback(errorMessage, errorCode, null);
    }

    //大类别|小类别|错误点|问题描述
    //例如: RongIM|发送消息|发送错误|错误码【21007】，错误描述【服务器错误，无法发送】
    //wiki: http://wiki.hbc.tech/pages/viewpage.action?pageId=4916716
    public static String getImErrorFeedback(int errorMessage, String errorCode, String describeStr) {
        String classesStr = null;
        switch (errorMessage) {
            case 1:
            case 2:
            case 3:
            case 4:
                classesStr = "连接融云服务器";
                break;
            case 5:
            case 6:
            case 7:
                classesStr = "长连接监控";
                break;
            case 8:
                classesStr = "发送消息";
                break;
        }

        String errorMessageStr = null;
        switch (errorMessage) {
            case 1:
                errorMessageStr = "token失效";
                errorCode = "30001";
                break;
            case 2:
                errorMessageStr = "连接错误";
                break;
            case 3:
                errorMessageStr = "连接失败";
                break;
            case 4:
                errorMessageStr = "token为空";
                break;
            case 5:
                errorMessageStr = "断开连接";
                describeStr = "异常断开";
                errorCode = "30011";
                break;
            case 6:
                errorMessageStr = "网络不可用";
                describeStr = "没有网络不能连接";
                errorCode = "30002";
                break;
            case 7:
                errorMessageStr = "账户在其它设备登录";
                describeStr = "账户在其它设别已登录";
                errorCode = "31010";
                break;
            case 8:
                errorMessageStr = "发送错误";
                describeStr = "服务器错误，无法发送";
                break;
        }

        String result = "RongIM" + SEPARATOR + classesStr + SEPARATOR + errorMessageStr + SEPARATOR;
        if (!TextUtils.isEmpty(errorCode)) {
            result += "错误码【" + errorCode + "】";
        }
        if (!TextUtils.isEmpty(describeStr)) {
            if (!TextUtils.isEmpty(errorCode)) {
                result += "，";
            }
            result += "错误描述【"+ describeStr + "】";
        }
        return result;
    }

}
