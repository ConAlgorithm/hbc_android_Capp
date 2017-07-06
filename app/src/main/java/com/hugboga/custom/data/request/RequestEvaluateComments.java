package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.AppraisementBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqiang on 17/6/28.
 */
@HttpRequest(path = UrlLibs.API_EVALUATE_COMMENTS, builder = NewParamsBuilder.class)
public class RequestEvaluateComments extends BaseRequest<AppraisementBean> {
    public RequestEvaluateComments(Context context,String orderNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderNo",orderNo);
    }

    @Override
    public Map<String, Object> getDataMap() {
        return super.getDataMap();
    }

    @Override
    public ImplParser getParser() {
        return new EvaluateCommentsParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "420160";
    }

    private static class EvaluateCommentsParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            AppraisementBean appraisementBean = gson.fromJson(obj.toString(), AppraisementBean.class);
            return appraisementBean;
        }
    }
}
