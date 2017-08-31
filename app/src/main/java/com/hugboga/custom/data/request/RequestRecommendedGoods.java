package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.RecommendedGoodsBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/8/29.
 */
@HttpRequest(path = UrlLibs.API_RECOMMENDED_GOODS, builder = NewParamsBuilder.class)
public class RequestRecommendedGoods extends BaseRequest<RecommendedGoodsBean> {

    public RequestRecommendedGoods(Context context, String cityId, String goodsNo) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("cityId", cityId);//城市编号
        if (!TextUtils.isEmpty(goodsNo)) {
            map.put("goodsNo", goodsNo);//否 商品编号
        }
        map.put("picSize", "101");
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_RECOMMENDED_GOODS, RecommendedGoodsBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40048";
    }

}