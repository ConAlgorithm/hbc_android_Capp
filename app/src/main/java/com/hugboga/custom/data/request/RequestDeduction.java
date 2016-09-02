package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.DeductionBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParseDeduction;

import java.util.HashMap;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;


@HttpRequest(path = UrlLibs.DEDUCTION, builder = NewParamsBuilder.class)
public class RequestDeduction extends BaseRequest<DeductionBean> {

    public RequestDeduction(Context context, String priceToPay) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("userId", UserEntity.getUser().getUserId(context));
        map.put("priceToPay", priceToPay);

    }

    @Override
    public ImplParser getParser() {
        return new ParseDeduction();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getUrlErrorCode() {
        return "40031";
    }

}