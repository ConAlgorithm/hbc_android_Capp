package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.DestinationListBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/12/16.
 */

@HttpRequest(path = UrlLibs.API_QUERY_TAG_GOODS_LIST, builder = NewParamsBuilder.class)
public class RequestDestinationList extends BaseRequest<DestinationListBean> {

    public RequestDestinationList(Context context, String tagId, int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("tagId", tagId);
        map.put("limit", Constants.DEFAULT_PAGESIZE);
        map.put("offset", offset);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_QUERY_TAG_GOODS_LIST, DestinationListBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40201";
    }

}