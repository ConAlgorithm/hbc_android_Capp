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
public class RequestCollectGuidesFilter extends BaseRequest<ArrayList<CollectGuideBean>> {
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
            map.put("carType", params.carType);
            map.put("carClass", params.carClass);
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

    public static class CollectGuidesFilterParams implements Serializable {
        //开始日期 yyyy-MM-dd HH:mm:ss
        public String startTime;
        //结束日期 yyyy-MM-dd HH:mm:ss
        public String endTime;
        //出发城市
        public int startCityId;
        //订单类型
        public int orderType;
        //车型 经济/舒适/豪华/奢华
        public int carType;
        //座系 5/7/9/12
        public int carClass;
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

        public CollectGuidesFilterParams setStartTime(String startTime) {
            this.startTime = startTime;
            return this;
        }

        public CollectGuidesFilterParams setPassCityId(String passCityId) {
            this.passCityId = passCityId;
            return this;
        }

        public CollectGuidesFilterParams setTotalDays(int totalDays) {
            this.totalDays = totalDays;
            return this;
        }

        public CollectGuidesFilterParams setChildSeatNum(int childSeatNum) {
            this.childSeatNum = childSeatNum;
            return this;
        }

        public CollectGuidesFilterParams setLuggageNum(int luggageNum) {
            this.luggageNum = luggageNum;
            return this;
        }

        public CollectGuidesFilterParams setChildrenNum(int childrenNum) {
            this.childrenNum = childrenNum;
            return this;
        }

        public CollectGuidesFilterParams setAdultNum(int adultNum) {
            this.adultNum = adultNum;
            return this;
        }

        public CollectGuidesFilterParams setCarClass(int carClass) {
            this.carClass = carClass;
            return this;
        }

        public CollectGuidesFilterParams setCarType(int carType) {
            this.carType = carType;
            return this;
        }

        public CollectGuidesFilterParams setOrderType(int orderType) {
            this.orderType = orderType;
            return this;
        }

        public CollectGuidesFilterParams setStartCityId(int startCityId) {
            this.startCityId = startCityId;
            return this;
        }

        public CollectGuidesFilterParams setEndTime(String endTime) {
            this.endTime = endTime;
            return this;
        }
    }
}