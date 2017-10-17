package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.json.JSONObject;
import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/7/6.
 */
@HttpRequest(path = UrlLibs.API_GOODS_BY_ID, builder = NewParamsBuilder.class)
public class RequestGoodsById extends BaseRequest<SkuItemBean> {

    public RequestGoodsById(Context context, String goodsNo, String guideId) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("goodsNo", goodsNo);
        map.put("userId", UserEntity.getUser().getUserId(context));
        map.put("picSize", 101);
        if (!TextUtils.isEmpty(guideId)) {
            map.put("guideId", guideId);
        }
    }

    @Override
    public ImplParser getParser() {
        return new DataParser();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40045";
    }

    private static class DataParser extends ImplParser {
        @Override
        public Object parseObject(JSONObject obj) throws Throwable {
            Gson gson = new Gson();
            SkuItemBean skuItemBean = gson.fromJson(obj.toString(), SkuItemBean.class);
            return skuItemBean;
        }
    }
}