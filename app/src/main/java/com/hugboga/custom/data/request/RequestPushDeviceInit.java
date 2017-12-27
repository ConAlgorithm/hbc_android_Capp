package com.hugboga.custom.data.request;


import android.content.Context;

import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.SharedPre;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.TreeMap;

@HttpRequest(path = UrlLibs.API_PUSH_DEVICE, builder = NewParamsBuilder.class)
public class RequestPushDeviceInit extends BaseRequest {

    public RequestPushDeviceInit(Context context, int pushGateway, String uniqueId) {
        super(context);
        map = new TreeMap();
        map.put("accessKey", UserSession.getUser().getAccessKey(context));
        map.put("deviceToken", PhoneInfo.getIMEI(context));
        map.put("os", 1);
        map.put("osVersion", PhoneInfo.getSoftwareVersion(context));
        map.put("appType", 2);
        map.put("appVersion", BuildConfig.VERSION_NAME);
        if (UserEntity.getUser().isLogin(context)) {
            map.put("status", 1);// 设备状态，0：离线，1：在线，必填
            map.put("userId", UserEntity.getUser().getUserId(context));
            map.put("userType", 2);
        } else {
            map.put("status", 0);
        }
        map.put("pushAlias", PhoneInfo.getIMEI(context));// 统一发送别名，必填
        map.put("pushGateway", pushGateway); // 给该设备发送push使用的方式，1：华为，2：小米，3：个推（GooglePlay），4：其他（极光），必填
        switch (pushGateway) {
            case 1:
                SharedPre.setString("uniqueHW", uniqueId);
                break;
            case 2:
                SharedPre.setString("uniqueXM", uniqueId);
                break;
            case 3:
                SharedPre.setString("uniqueGT", uniqueId);
                break;
            case 4:
                SharedPre.setString("uniqueJG", uniqueId);
                break;
        }
        map.put("uniqueHW", pushGateway == 1 ? uniqueId : SharedPre.getString("uniqueHW", ""));
        map.put("uniqueXM", pushGateway == 2 ? uniqueId : SharedPre.getString("uniqueXM", ""));
        map.put("uniqueGT", pushGateway == 3 ? uniqueId : SharedPre.getString("uniqueGT", ""));
        map.put("uniqueJG", pushGateway == 4 ? uniqueId : SharedPre.getString("uniqueJG", ""));
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public String getUrlErrorCode() {
        return "40202";
    }

}