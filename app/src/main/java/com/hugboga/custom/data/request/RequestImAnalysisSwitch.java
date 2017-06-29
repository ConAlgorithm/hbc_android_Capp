package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

/**
 * Created by SPW on 2017/6/29.
 */
@HttpRequest(path = UrlLibs.API_IMANALYSIS_SWITCH, builder = NewParamsBuilder.class)
public class RequestImAnalysisSwitch extends BaseRequest<String> {

    public RequestImAnalysisSwitch(Context context) {
        super(context);
    }

    @Override
    public String getUrlErrorCode() {
        return "40159";
    }

    @Override
    public ImplParser getParser() {
        return null;
    }
}
