package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.UserBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserChangeUserInfo;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/17.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_INFORMATION_UPDATE, builder = NewParamsBuilder.class)
public class RequestChangeUserInfo extends BaseRequest<UserBean> {

    public RequestChangeUserInfo(Context context, String avatar, String nickname, String gender, String ageType, String sign) {
        super(context);
        map = new HashMap<String, Object>();
        try {
            if (!TextUtils.isEmpty(avatar)) {
                map.put("avatar", avatar);
            }
            if (!TextUtils.isEmpty(nickname)) {
                map.put("nickName", nickname);
            }
            if (!TextUtils.isEmpty(gender)) {
                map.put("gender", gender);
            }
            if (!TextUtils.isEmpty(ageType)) {
                map.put("ageType", ageType);
            }
            if (!TextUtils.isEmpty(sign)) {
                map.put("signature", sign);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ImplParser getParser() {
        return new ParserChangeUserInfo();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
