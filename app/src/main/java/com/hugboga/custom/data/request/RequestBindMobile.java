package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserLogin;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 */
@HttpRequest(path = UrlLibs.WECHAT_CHECK_MOBILE, builder = HbcParamsBuilder.class)
public class RequestBindMobile extends BaseRequest<UserBean> {
    public String unionid;
    public String skip;
    public String areaCode;
    public String mobile;
    public String captcha;

    /**
     *
     * @param context 上下文
     * @param areaCode 国家代码
     * @param mobile 电话号码
     * @param captcha 验证码
     * @param unionid 微信unionid
     * @param skip 是否跳过绑定手机号
     */
    public RequestBindMobile(Context context, String areaCode, String mobile, String captcha, String unionid, String skip) {
        super(context);
        this.unionid = unionid;
        this.skip = skip;
        if(!skip.equals("1")) {
            this.areaCode = areaCode;
            this.mobile = mobile;
            this.captcha = captcha;
        }
    }

    @Override
    public Map<String, Object> getDataMap() {
        TreeMap map = new TreeMap<String, Object>();
        if(!TextUtils.isEmpty(skip) && !skip.equals("1")){
            map.put("areaCode", areaCode);
            map.put("mobile", mobile);
            map.put("captcha", captcha);
        }
        map.put("unionid", unionid);
        map.put("skip", skip);
        map.put("source", 1);
        return map;
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
