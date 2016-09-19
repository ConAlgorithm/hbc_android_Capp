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


    //大类别|小类别|错误点|问题描述
    //例如: RongIM|发送消息|发送错误|错误码【21007】，错误描述【服务器错误，无法发送】
    //wiki: http://wiki.hbc.tech/pages/viewpage.action?pageId=4916716
    public static String getImErrorFeedback(int errorMessage, String errorCode, String describeStr) {
        String classesStr = null;
        switch (errorMessage) {
            case 1:
                classesStr = "云信登录异常";
                break;
            case 2:
                classesStr = "发送消息异常";
                break;
            case 3:
                classesStr = "长连接异常";
                break;
            default:
              classesStr = "其它问题";
                break;
        }

        String errorMessageStr = null;
        switch (errorMessage) {
            case 1:
                errorMessageStr = "登录失败，具体错误，请参照云信错误码文档：http://dev.netease.im/docs?doc=nim_status_code";
                break;
            case 2:
                errorMessageStr = "发送消息失败，具体错误，请参照云信错误码文档：http://dev.netease.im/docs?doc=nim_status_code";
                break;
            case 3:
                errorMessageStr = "连接问题，具体错误，请参照云信错误码文档：http://dev.netease.im/docs?doc=nim_status_code";
                break;
            default:
                errorMessageStr = "其它问题，具体错误，请参照云信错误码文档：http://dev.netease.im/docs?doc=nim_status_code";
                break;

        }

        String result = "NIM" + SEPARATOR + classesStr + SEPARATOR + errorMessageStr + SEPARATOR;
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
