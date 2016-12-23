package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.GoodsBookDateBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/12/23.
 *
 * WIKI: http://wiki.hbc.tech/pages/viewpage.action?pageId=6619493#id-%E9%A6%96%E9%A1%B5%EF%BC%8D%E5%95%86%E5%93%81-home-goodsbookdate1.0
 */
@HttpRequest(path = UrlLibs.GOODS_BOOK_DATE, builder = NewParamsBuilder.class)
public class RequestGoodsBookDate extends BaseRequest<GoodsBookDateBean> {

    public RequestGoodsBookDate(Context context, String goodsNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("goodsNo", goodsNo);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.GOODS_BOOK_DATE, GoodsBookDateBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40113";
    }

}

