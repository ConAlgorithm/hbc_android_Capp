package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhangqiang on 17/7/12.
 */
@HttpRequest(path = UrlLibs.API_DESTINATIONS_LINE, builder = NewParamsBuilder.class)
public class DestinationLine extends BaseRequest<HomeBeanV2.LineGroupAgg> {
    int groupId;
    public DestinationLine(Context context,int groupId) {
        super(context);
        this.groupId = groupId;
    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String, Object>();
        map.put("groupId", groupId);
        return map;
    }

    @Override
    public ImplParser getParser() {
        return new ParseDesLine();
    }

    @Override
    public String getUrlErrorCode() {
        return "430168";
    }

    public class ParseDesLine extends ImplParser {
        @Override
        public HomeBeanV2.LineGroupAgg parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            HomeBeanV2.LineGroupAgg lineGroupAggregationVo = gson.fromJson(obj.toString(),HomeBeanV2.LineGroupAgg.class);
            return lineGroupAggregationVo;
        }
    }
}
