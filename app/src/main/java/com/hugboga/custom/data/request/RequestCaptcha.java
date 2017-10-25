package com.hugboga.custom.data.request;

import android.content.Context;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.CommonUtils;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 17/10/25.
 * 获取语音验证码
 */
@HttpRequest(path = UrlLibs.API_CAPTCHA, builder = NewParamsBuilder.class)
public class RequestCaptcha extends BaseRequest {

    public RequestCaptcha(Context context, String areaCode, String mobile, int captchaType) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("sysName", "capp");
        map.put("userType", 2);// 请求发送语音的用户类型，1：司导，2：用户。建议必填
        map.put("areaCode", CommonUtils.removePhoneCodeSign(areaCode));
        map.put("mobile", mobile);
        map.put("captchaType", captchaType);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return null;
    }

    @Override
    public String getUrlErrorCode() {
        return "40187";
    }

}