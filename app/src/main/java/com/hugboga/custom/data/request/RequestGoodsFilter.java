package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.GoodsFilterBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

@HttpRequest(path = UrlLibs.API_GOODS_FILTER, builder = NewParamsBuilder.class)
public class RequestGoodsFilter extends BaseRequest<GoodsFilterBean> {

    public RequestGoodsFilter(Context context, Builder builder) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("channelId", "18");

        map.put("id", builder.id);//线路圈ID/国家ID/城市ID
        map.put("type", builder.type);//1.线路圈 2.国家 3.城市 -1.全部目的地（此时id无限制）
        if (!TextUtils.isEmpty(builder.themeIds)) {
            map.put("themeIds", builder.themeIds);//主题ID列表  格式：主题ID,主题ID
        }

        if (!TextUtils.isEmpty(builder.days)) {
            map.put("days", builder.days);
        }

        map.put("returnThemes", builder.returnThemes);//是否返回主题集合  默认false，不返

        map.put("offset", builder.offset);//偏移量
        map.put("limit", builder.limit);//条数
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_GOODS_FILTER, GoodsFilterBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40139";
    }

    public static class Builder {
        public int id;
        public int type;
        public String themeIds;
        public boolean returnThemes;
        public int offset;
        public int limit = Constants.DEFAULT_PAGESIZE;
        public String days;
    }
}