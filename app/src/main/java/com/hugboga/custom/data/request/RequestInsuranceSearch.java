package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.InsureListBean;
import com.hugboga.custom.data.bean.InsureSearchBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.JsonUtils;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by qingcha on 17/4/11.
 */
@HttpRequest(path = UrlLibs.API_INSURANCE_SEARCH, builder = NewParamsBuilder.class)
public class RequestInsuranceSearch extends BaseRequest<InsureSearchBean> {

    public RequestInsuranceSearch(Context context, String orderNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("orderNo", orderNo);
    }

    @Override
    public ImplParser getParser() {
        return new ParserInsureSearchBean();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40136";
    }

    public static class ParserInsureSearchBean extends ImplParser{

        @Override
        public Object parseObject(JSONObject jsonObj) throws Throwable {
            InsureSearchBean insureSearchBean = new InsureSearchBean();
            insureSearchBean.totalSize = jsonObj.getInt("totalSize");
            JSONObject insuranceMapBean = jsonObj.optJSONObject("resultMap");
            if (insuranceMapBean != null) {
                List<List<InsureListBean>> insuranceList = new ArrayList<>();
                Iterator<String> keys = insuranceMapBean.keys();
                for (; keys.hasNext(); ) {
                    String key = keys.next();
                    List<InsureListBean> itemList = JsonUtils.INSTANCE.getGson().fromJson(insuranceMapBean.optString(key), new TypeToken<List<InsureListBean>>(){}.getType());
                    insuranceList.add(itemList);
                }
                insureSearchBean.insuranceMap = insuranceList;
            }
            return insureSearchBean;
        }
    }
}