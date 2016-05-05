package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ADPictureBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseGetAD;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

/**
 * Created by dyt on 16/5/4.
 */
@HttpRequest(path = UrlLibs.GET_AD_PICTURE, builder = NewParamsBuilder.class)
public class RequestADPicture extends BaseRequest<ADPictureBean> {

    public RequestADPicture(Context context) {
        super(context);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new ParseGetAD();
    }
}
