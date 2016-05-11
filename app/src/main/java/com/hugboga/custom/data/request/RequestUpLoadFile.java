package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserUpLoadFile;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/17.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_PIC_UPLOAD, builder = NewParamsBuilder.class)
public class RequestUpLoadFile extends BaseRequest {

    public RequestUpLoadFile(Context context, Map<String, Object> map) {
        super(context);
        this.map = map;
    }

    @Override
    public ImplParser getParser() {
        return new ParserUpLoadFile();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
