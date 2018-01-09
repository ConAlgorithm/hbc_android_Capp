package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by zhangqiang on 17/8/28.
 */

@HttpRequest(path = UrlLibs.COLLECT_LINES_NO, builder = NewParamsBuilder.class)
public class RequestCollectLineNo extends BaseRequest {

    private String goodsNo; //订单号

    public RequestCollectLineNo(Context context, String goodsNo) {
        super(context);
        this.goodsNo = goodsNo;
        map = new HashMap<String, Object>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("userId", UserEntity.getUser().getUserId(context));
        map.put("goodsNo", goodsNo);//司导ID
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40183";
    }

    public String getGoodsNo() {
        return goodsNo;
    }
}
