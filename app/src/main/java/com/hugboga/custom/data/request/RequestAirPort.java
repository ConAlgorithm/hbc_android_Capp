package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserAirPort;

import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/22.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_AIRPORT, builder = HbcParamsBuilder.class)
public class RequestAirPort extends BaseRequest<ArrayList<AirPort>> {
    public RequestAirPort(Context context, String keyword) {
        super(context);
        map = new HashMap<String,Object>();
        map.put("keyword", keyword);
    }

    @Override
    public ImplParser getParser() {
        return new ParserAirPort();
    }
}
