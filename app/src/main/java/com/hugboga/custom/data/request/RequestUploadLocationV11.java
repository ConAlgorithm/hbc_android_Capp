package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.bean.GPSBean;
import com.hugboga.custom.data.bean.LocationData;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseLocationCity;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created  on 16/5/23.
 */
@HttpRequest(path = UrlLibs.UPLOAD_LOCATION_V11, builder = NewParamsBuilder.class)
public class RequestUploadLocationV11 extends BaseRequest<LocationData> {

    public RequestUploadLocationV11(Context context) {
        super(context);
    }


    @Override
    public Map getDataMap() {
        Context mContext = getContext();
        TreeMap map = new TreeMap<String, Object>();
        try {
            map.put("latitude", GPSBean.lat);
            map.put("longitude", GPSBean.lng);
        } catch (Exception e) {
            MLog.e(e.toString());
        }
        return map;
    }

    @Override
    public ImplParser getParser() {
        return new ParseLocationCity();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40081";
    }
}