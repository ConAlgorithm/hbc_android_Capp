package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseInsureAdd;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created  on 16/4/22.
 */
@HttpRequest(path = UrlLibs.ADD_INSURE, builder = NewParamsBuilder.class)
public class RequestAddInsure extends BaseRequest {

    public RequestAddInsure(Context context, String userId, String name,
                            String passportNo, String sex,String birthday) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("name", name);
        map.put("passportNo", passportNo);
        map.put("sex", sex);
        map.put("birthday", birthday);
    }

    @Override
    public ImplParser getParser() {
        return new ParseInsureAdd();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40003";
    }
}
