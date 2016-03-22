package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.DailyBean;

/**
 * Created by Administrator on 2016/3/21.
 */
public class RequestCheckPriceForDaily extends BaseRequest {
    public RequestCheckPriceForDaily(Context context,DailyBean bean) {
        super(3, null,bean.startCityID, bean.startLocation, bean.terminalLocation, null);
        map.put("startCityId", bean.startCityID);
        map.put("endCityId", bean.terminalCityID);
        map.put("startDate", bean.startDate + " 00:00:00");
        map.put("endDate",bean.endDate+" 00:00:00");
        map.put("intownDays",bean.inTownDays);
        map.put("outtownDays",bean.outTownDays);
        map.put("halfDay", bean.isHalfDay ? 1 : 0);
    }

    @Override
    public ImplParser getParser() {
        return null;
    }
}
