package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseGuideConflict;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;
import java.util.List;


@HttpRequest(path = UrlLibs.GUIDE_CONFLIC, builder = NewParamsBuilder.class)
public class RequestGuideConflict extends BaseRequest<List<String>> {

//    orderType Integer 必填 订单类型（仅支持这几种类型）：1 接机 2 送机 3 包车 4 次租
//    cityId Integer 必填 城市Id
//    carType Integer 必填 车类型：1-经济型;2-舒适型;3-豪华型;4-奢华型
//    carClass Integer 必填 车座数：5  7 9 12
//    guideIds String 必填 导游Id  1,2,3
//    startTime String 必填 服务开始时间 2016-06-11 12:00:00
//    endTime String 必填 服务结束时间 2016-06-11 16:00:00
//    passCityId String 选填 途径城市Id（日租，跨城市包车必填）  2,3,5   城市id,城市id
//    totalDay Integer 选填 用车天数（包车必填）
    public RequestGuideConflict(Context context,int orderType,int cityId,String guideIds,
                               String startTime,String endTime,String passCityId,int totalDay,int carType,int carClass) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderType", orderType);
        map.put("cityId", cityId);
        map.put("guideIds", guideIds);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("passCityId", passCityId);
        map.put("totalDay", totalDay);
        map.put("carType", carType);
        map.put("carClass", carClass);

    }

    @Override
    public ImplParser getParser() {
        return new ParseGuideConflict();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }
}
