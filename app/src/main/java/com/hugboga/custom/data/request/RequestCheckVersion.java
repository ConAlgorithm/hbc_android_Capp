package com.hugboga.custom.data.request;

import android.content.Context;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.ResourcesConstants;
import com.hugboga.custom.data.bean.CheckVersionBean;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserCheckVersion;
import com.hugboga.custom.utils.Config;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/11.
 */
@HttpRequest(path = UrlLibs.SERVER_IP_CHECK_APP_VERSION, builder = HbcParamsBuilder.class)
public class RequestCheckVersion extends BaseRequest<CheckVersionBean> {
    public RequestCheckVersion(Context context, String currentVersion, int resVersion) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("appVersion", currentVersion);
        map.put("appKey", Config.getPageName()); //Android
        map.put("resVersions", ResourcesConstants.RESOURCES_H5_NAME + ":" + resVersion); //Android
    }

    @Override
    public ImplParser getParser() {
        return new ParserCheckVersion();
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }
}
