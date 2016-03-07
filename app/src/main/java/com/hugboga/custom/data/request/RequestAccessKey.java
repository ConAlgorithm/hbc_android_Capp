package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.data.request.HbcParamsBuilder;
import com.huangbaoche.hbcframe.util.PhoneInfo;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/2/29.
 */

@HttpRequest(path = UrlLibs.SERVER_IP_ACCESSKEY,builder = HbcParamsBuilder.class)
public class RequestAccessKey extends BaseRequest<String> {

    public RequestAccessKey(Context context){
        super(context);
    }


    @Override
    public Map getDataMap() {
        HashMap map = new HashMap<String,Object>();
        try {
            map.put("deviceId", PhoneInfo.getImei(mContext));
            map.put("model", "Android");
            map.put("os", 1);
            map.put("osVersion",PhoneInfo.getPhoneVer());
            map.put("sysVersion", PhoneInfo.getPhoneVer());
            map.put("modelIdentifier", PhoneInfo.getUa());
            map.put("pushToken", PhoneInfo.getImei(mContext));
            map.put("imei", PhoneInfo.getImei(mContext));
            map.put("macAddress", PhoneInfo.getMac(mContext));
            map.put("networkType", PhoneInfo.getNet(mContext));
            map.put("appKey", PhoneInfo.getPackageName());
            map.put("appVersion", PhoneInfo.getSoftVer());
            map.put("appName", PhoneInfo.getAppName());
            map.put("source", 1);
            LogUtil.e("getDataMap = " + map.get("appName"));
        }catch (Exception e){
            LogUtil.e(e.toString());
        }
        LogUtil.e("getDataMap = "+map);
        return map;
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
