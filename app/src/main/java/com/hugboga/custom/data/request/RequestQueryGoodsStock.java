package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CalendarGoodsBeanList;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/8/4.
 */
@HttpRequest(path = "", builder = NewParamsBuilder.class)
public class RequestQueryGoodsStock extends BaseRequest<CalendarGoodsBeanList> {

    private String guideId;

    public RequestQueryGoodsStock(Context context, String goodsNo, String guideId, String startServiceDate, String endServiceDate, String yearMonthStr) {
        super(context);
        this.guideId = guideId;
        map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(guideId)) {
            map.put("guideNo", guideId);               // 司导编号
        } else {
            map.put("goodsNo", goodsNo);               // 商品编号
        }
        map.put("startServiceDate", startServiceDate); // 开始服务日期 格式 yyyy-MM-dd
        map.put("endServiceDate", endServiceDate);     // 结束服务日期 格式 yyyy-MM-dd
        tag = yearMonthStr;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(getUrl(), CalendarGoodsBeanList.class);
    }

    @Override
    public String getUrl() {
        return !TextUtils.isEmpty(guideId) ? UrlLibs.API_QUERY_GUIDE_STOCK : UrlLibs.API_QUERY_GOODS_STOCK;
    }

    @Override
    public String getUrlErrorCode() {
        return !TextUtils.isEmpty(guideId) ? "40172" : "40171";
    }

}