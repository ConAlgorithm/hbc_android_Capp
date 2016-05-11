package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserLogin;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by GrandFather on 2016/4/23.
 */

@HttpRequest(path = UrlLibs.WECHAT_SET_PASSWORD, builder = NewParamsBuilder.class)
public class RequestSetPwd extends BaseRequest<UserBean> {
    public RequestSetPwd(Context context, String areaCode, String mobile, String password, String unionid) {
        super(context);
        map = new HashMap<String, Object>();
        try {
            map.put("areaCode", areaCode);
            map.put("mobile", mobile);
            map.put("password", password);
            map.put("unionid", unionid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ImplParser getParser() {
        return new ParserLogin();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
