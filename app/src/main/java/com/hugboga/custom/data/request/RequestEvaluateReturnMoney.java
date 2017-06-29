package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.EvaluateReturnMoney;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

/**
 * Created by zhangqiang on 17/6/28.
 */
@HttpRequest(path = UrlLibs.API_EVALUATE_RETURN_MONEY, builder = NewParamsBuilder.class)
public class RequestEvaluateReturnMoney extends BaseRequest<EvaluateReturnMoney> {
    public RequestEvaluateReturnMoney(Context context) {
        super(context);
    }

    @Override
    public ImplParser getParser() {
        return new EvaluateReturnMoneyParser();
    }

    @Override
    public String getUrlErrorCode() {
        return null;
    }

    private static class EvaluateReturnMoneyParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            EvaluateReturnMoney evaluateReturnMoney = gson.fromJson(obj.toString(), EvaluateReturnMoney.class);
            return evaluateReturnMoney;
        }
    }
}
