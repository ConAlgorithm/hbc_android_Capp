package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.SharedPre;
import com.hugboga.custom.data.bean.LocationCity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseLocationCityV10;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created  on 2016/4/12.
 */

@HttpRequest(path = UrlLibs.UPLOAD_LOCATION, builder = NewParamsBuilder.class)
public class RequestUploadLocation extends BaseRequest<LocationCity> {

    public RequestUploadLocation(Context context) {
        super(context);
    }


    @Override
    public Map getDataMap() {
        Context mContext = getContext();
        TreeMap map = new TreeMap<String, Object>();
        try {
            map.put("latitude", new SharedPre(mContext).getStringValue("lat"));
            map.put("longitude", new SharedPre(mContext).getStringValue("lng"));
        } catch (Exception e) {
            MLog.e(e.toString());
        }
        return map;
    }

    @Override
    public ImplParser getParser() {
        return new ParseLocationCityV10();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40080";
    }
}
