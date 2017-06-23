package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.FilterGuideListBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/4/19.
 */
@HttpRequest(path = UrlLibs.API_FILTER_GUIDES, builder = NewParamsBuilder.class)
public class RequestFilterGuide extends BaseRequest<FilterGuideListBean> {

    public RequestFilterGuide(Context context, Builder builder) {
        super(context);
        map = new HashMap<String, Object>();
        if (!TextUtils.isEmpty(builder.cityIds)) {
            map.put("cityIds", builder.cityIds);                // 城市标识,(多个用逗号隔开)
        }
        if (!TextUtils.isEmpty(builder.countryId)) {
            map.put("countryId", builder.countryId);            // 国家ID
        }
        if (!TextUtils.isEmpty(builder.lineGroupId)) {
            map.put("lineGroupId", builder.lineGroupId);        // 线路圈ID
        }
        if (!TextUtils.isEmpty(builder.genders)) {
            map.put("genders", builder.genders);                // 性别标识,(多个用逗号隔开)
        }
        if (!TextUtils.isEmpty(builder.serviceTypes)) {
            map.put("serviceTypes", builder.serviceTypes);      // 服务类型标识,(多个用逗号隔开)
        }
        if (!TextUtils.isEmpty(builder.guestNum)) {
            map.put("guestNum", builder.guestNum);              // 可接待人数
        }
        map.put("orderByType", builder.orderByType);            // 排序类型:0-默认排序; 1-按星级排序;2-按评分排序;3-按单数排序
        map.put("orderDesc", builder.orderDesc);                // 排序方式,desc-从高到低, asc-从低到高
        if (builder.isQuality != null) {
            map.put("isQuality", builder.isQuality);            // 是否优质司导, 1-是，0-否
        }
        map.put("limit", builder.limit);
        map.put("offset", builder.offset);

        if (!TextUtils.isEmpty(builder.langCodes)) {
            map.put("langCodes", builder.langCodes);            // 语言代码,多个用逗号隔开(包括方言和外语)
        }
        if (!TextUtils.isEmpty(builder.labelIds)) {
            map.put("labelIds", builder.labelIds);              // 技能标签标识,多个用逗号隔开
        }
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_FILTER_GUIDES, FilterGuideListBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40138";
    }

    public static class Builder {
        public String cityIds;
        public String countryId;
        public String lineGroupId;
        public String genders;
        public String serviceTypes;
        public String guestNum;
        public int orderByType = 0;
        public String orderDesc = "desc";
        public Integer isQuality;
        public int limit = Constants.DEFAULT_PAGESIZE;
        public int offset = 0;
        public String langCodes;
        public String labelIds;

        public Builder setLangCodes(String langCodes) {
            this.langCodes = langCodes;
            return this;
        }

        public Builder setLabelIds(String labelIds) {
            this.labelIds = labelIds;
            return this;
        }

        public Builder setCityIds(String cityIds) {
            this.cityIds = cityIds;
            return this;
        }

        public Builder setCountryId(String countryId) {
            this.countryId = countryId;
            return this;
        }

        public Builder setLineGroupId(String lineGroupId) {
            this.lineGroupId = lineGroupId;
            return this;
        }

        public Builder setGenders(String genders) {
            this.genders = genders;
            return this;
        }

        public Builder setServiceTypes(String serviceTypes) {
            this.serviceTypes = serviceTypes;
            return this;
        }

        public Builder setGuestNum(String guestNum) {
            this.guestNum = guestNum;
            return this;
        }

        public Builder setOrderByType(int orderByType) {
            this.orderByType = orderByType;
            return this;
        }

        public Builder setOrderDesc(String orderDesc) {
            this.orderDesc = orderDesc;
            return this;
        }

        public Builder setIsQuality(int isQuality) {
            this.isQuality = isQuality;
            return this;
        }

        public Builder setLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder setOffset(int offset) {
            this.offset = offset;
            return this;
        }
    }

}