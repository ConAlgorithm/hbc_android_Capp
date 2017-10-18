package com.hugboga.custom.data.request;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.data.bean.EvaluateData;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;

import org.xutils.http.HttpMethod;
import org.xutils.http.annotation.HttpRequest;

import java.util.HashMap;

/**
 * Created by qingcha on 16/6/6.
 */
@HttpRequest(path = UrlLibs.API_EVALUATE_NEW, builder = NewParamsBuilder.class)
public class RequestEvaluateNew extends BaseRequest<EvaluateData> {

    public RequestEvaluateNew(Context context, RequestParams params) {
        super(context);
        map = new HashMap<String, Object>();
        map.put("fromUname", params.fromUname);//评价人姓名：张三
        map.put("guideId", params.guideId);//被评价导游id：1
        map.put("guideName", params.guideName);//被评价导游name：李四
        map.put("orderNo", params.orderNo);//关联订单号：S123
        map.put("orderType", params.orderType);//关联订单类型
        map.put("totalScore", params.totalScore);//总分：5
        if (!TextUtils.isEmpty(params.labels)) {
            map.put("labels", params.labels);//可选 选定标签id拼接：1,2,3,4
        }
        if (!TextUtils.isEmpty(params.content)) {
            map.put("content", params.content);//可选 评价内容：123
        }
        map.put("commentPics",params.commentPics);
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_EVALUATE_NEW, EvaluateData.class);
    }

    @Override
    public HttpMethod getHttpMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getUrlErrorCode() {
        return "40037";
    }

    public static class RequestParams {
        public String fromUname;
        public String guideId;
        public String guideName;
        public String orderNo;
        public int orderType;
        public int totalScore;
        public String labels;
        public String content;
        public String commentPics;
    }
}