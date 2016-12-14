package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.AcquirePacketBean;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import com.hugboga.custom.utils.Common;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/12/10.
 */
@HttpRequest(path = UrlLibs.ACQUIRE_PACKET, builder = NewParamsBuilder.class)
public class RequestAcquirePacket extends BaseRequest<AcquirePacketBean> {

    private static final String SIGN_KEY = "$RFVbgt5";

    public RequestAcquirePacket(Context context, String areaCode, String mobile) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("source", "1");
        map.put("areaCode", areaCode);
        map.put("mobile", mobile);
        map.put("sign", Common.md5(Common.md5(mobile) + SIGN_KEY));
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.ACQUIRE_PACKET, AcquirePacketBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40110";
    }

}