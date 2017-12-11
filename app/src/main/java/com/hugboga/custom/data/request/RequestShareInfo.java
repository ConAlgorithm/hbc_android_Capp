package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.ShareInfoBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by HONGBO on 2017/12/11 15:41.
 */
@HttpRequest(path = UrlLibs.API_QUERY_SHARE_INFO, builder = NewParamsBuilder.class)
public class RequestShareInfo extends BaseRequest {

    public RequestShareInfo(Context context, String shareNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("shareNo", shareNo);
        map.put("shareType", "3"); //1专辑 2攻略 3活动，本期只有活动，定着穿3
    }

    @Override
    public String getUrlErrorCode() {
        return "40200";
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_QUERY_SHARE_INFO, ShareInfoBean.class);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}
