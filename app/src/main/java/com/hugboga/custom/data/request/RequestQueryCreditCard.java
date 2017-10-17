package com.hugboga.custom.data.request;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.CreditCardInfoBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/8.
 */
@HttpRequest(path = UrlLibs.API_QUERY_CREDIT_CARD, builder = NewParamsBuilder.class)
public class RequestQueryCreditCard extends BaseRequest<ArrayList<CreditCardInfoBean>> {

    public RequestQueryCreditCard(Context context) {
        super(context);
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public String getUrlErrorCode() {
        return "40128";
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

   private class DataParser extends ImplParser {

       @Override
       public Object parseArray(JSONArray array) throws Throwable {
//           super.parseArray(array);
           Gson gson = new Gson();
           ArrayList<CreditCardInfoBean> beanArrayList = gson.fromJson(array.toString(),new TypeToken<ArrayList<CreditCardInfoBean>>(){}.getType());
           return beanArrayList;
       }

       @Override
       public Object parseObject(JSONObject obj) throws Throwable {
           return null;
       }
   }


}
