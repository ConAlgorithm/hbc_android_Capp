package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by spw on 2017/6/27.
 */
@HttpRequest(path = UrlLibs.API_UPLOAD_IMANALYSIS_INFO,builder = NewParamsBuilder.class)
public class UploadImAnalysisInfoRequest extends BaseRequest {


    public UploadImAnalysisInfoRequest(Context context, String content) {
        super(context);
        map = new HashMap<String, Object>();
        setAsJsonContent(true);
        bodyEntity = content;
    }


    @Override
    public ImplParser getParser() {
        return new ImplParser() {
            @Override
            public Object parseObject(JSONObject obj) throws Throwable {
                return obj.toString();
            }
        };
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40161";
    }
}
