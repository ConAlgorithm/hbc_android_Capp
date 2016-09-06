package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.PaySucceedBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/9/5.
 */
@HttpRequest(path = UrlLibs.PAY_SUCCESS, builder = NewParamsBuilder.class)
public class RequestPaySucceed extends BaseRequest<PaySucceedBean> {

    public RequestPaySucceed(Context context, String orderNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderNo", orderNo);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.PAY_SUCCESS, PaySucceedBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40100";
    }

}