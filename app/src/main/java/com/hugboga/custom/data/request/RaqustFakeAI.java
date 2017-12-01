package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.FakeAIBean;
import com.hugboga.custom.data.net.NewParamsBuilder;

import org.json.JSONObject;
import org.xutils.http.annotation.HttpRequest;

/**
 * Created by Administrator on 2017/12/1.
 */
@HttpRequest(path = "trade/v1.0/e/ai/askready", builder = NewParamsBuilder.class)
public class RaqustFakeAI extends BaseRequest<FakeAIBean> {
    public RaqustFakeAI(Context context) {
        super(context);

    }

    @Override
    public String getUrlErrorCode() {
        return null;
    }

    @Override
    public ImplParser getParser() {
        return new Parse();
    }
    public class Parse extends ImplParser {
        @Override
        public FakeAIBean parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            FakeAIBean lineGroupAggregationVo = gson.fromJson(obj.toString(),FakeAIBean.class);
            return lineGroupAggregationVo;
        }
    }
}
