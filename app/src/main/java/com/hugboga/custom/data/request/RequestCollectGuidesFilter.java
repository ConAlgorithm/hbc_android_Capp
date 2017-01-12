package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserCollectGuideList;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by qingcha on 16/5/24.
 */
@HttpRequest(path = UrlLibs.COLLECT_GUIDES_FILTER, builder = NewParamsBuilder.class)
public class RequestCollectGuidesFilter extends BaseRequest<ParserCollectGuideList.CollectGuideList> {
    public RequestCollectGuidesFilter(Context context, CollectGuidesFilterParams params, int offset) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("userId", UserEntity.getUser().getUserId(context));
        map.put("offset", offset);
        map.put("limit", Constants.DEFAULT_PAGESIZE);

        if (params != null) {
            map.put("startTime", params.startTime);
            map.put("endTime", params.endTime);
            map.put("startCityId", params.startCityId);
            map.put("orderType", params.orderType);
            map.put("adultNum", params.adultNum);
            map.put("childrenNum", params.childrenNum);
            map.put("childSeatNum", params.childSeatNum);
            map.put("luggageNum", params.luggageNum);
            map.put("passCityId", params.passCityId);
            map.put("totalDays", params.totalDays);
        }
    }

    @Override
    public ImplParser getParser() {
        return new ParserCollectGuideList();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40024";
    }

    public static class CollectGuidesFilterParams implements Serializable {
        //开始日期 yyyy-MM-dd HH:mm:ss
        public String startTime;
        //结束日期 yyyy-MM-dd HH:mm:ss
        public String endTime;
        //出发城市
        public int startCityId;
        //订单类型
        public int orderType;
        //成人数
        public int adultNum;
        //儿童数
        public int childrenNum;
        //儿童座椅数
        public int childSeatNum;
        //行李数
        public int luggageNum;
        //(限日租)途径城市ID 逗号分隔 e.g:5,7
        public String passCityId;
        //限日租)总天数 半日包天数为1
        public int totalDays;
    }
}