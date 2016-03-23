package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.bean.ArrivalBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserArrivalSearch;

import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/21.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_POI, builder = HbcParamsBuilder.class)
public class RequestArrivalSearch extends BaseRequest<ArrayList<ArrivalBean>> {
    public RequestArrivalSearch(Context context, int cityId,String location,String keyword,int offset,int limit) {
        super(context);
        map =new HashMap();
        try {
            map.put("location",location);
            if(cityId!=-1)
                map.put("cityId", cityId);
            map.put("input", keyword);
            map.put("offset", offset);
            map.put("limit", limit);
        }catch (Exception e){
            MLog.e(e.toString());
        }
    }

    @Override
    public ImplParser getParser() {
        return new ParserArrivalSearch();
    }
}
