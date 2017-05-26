package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.JsonUtils;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@HttpRequest(path = UrlLibs.API_GUIDE_AVAILABLE_CHECK, builder = NewParamsBuilder.class)
public class RequestCheckGuide extends BaseRequest {

    public RequestCheckGuide(Context context, CheckGuideBeanList checkGuideBeanList) {
        super(context);
        map = new HashMap<String, Object>();
        bodyEntity = JsonUtils.toJson(checkGuideBeanList);
        errorType = ERROR_TYPE_IGNORE;
    }

    public RequestCheckGuide(Context context, CheckGuideBean checkGuideBean) {
        super(context);
        map = new HashMap<String, Object>();
        CheckGuideBeanList checkGuideBeanList = new CheckGuideBeanList();
        checkGuideBeanList.guideCheckInfos.add(checkGuideBean);
        bodyEntity = JsonUtils.toJson(checkGuideBeanList);
        errorType = ERROR_TYPE_IGNORE;
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
        return "40143";
    }

    public static class CheckGuideBeanList implements Serializable {
        public ArrayList<CheckGuideBean> guideCheckInfos;
        public CheckGuideBeanList() {
            guideCheckInfos = new ArrayList<CheckGuideBean>();
        }

        public void updateFirstDayServiceTime(String time) {
            if (guideCheckInfos.size() <= 0 || TextUtils.isEmpty(time)) {
                return;
            }
            CheckGuideBean checkGuideBean = guideCheckInfos.get(0);
            checkGuideBean.startTime = time;
        }
    }

    public static class CheckGuideBean implements Serializable {
        public String guideId;
        public int cityId;
        public int orderType;
        public String startTime;
        public String endTime;
    }
}