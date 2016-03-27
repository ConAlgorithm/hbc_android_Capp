package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.AssessmentBean;

import org.json.JSONObject;

/**
 * 评价
 * Created by admin on 2016/3/23.
 */
public class ParserAssessment extends ImplParser {
    @Override
    public AssessmentBean parseObject(JSONObject jsonObj) throws Throwable {
        AssessmentBean assessmentBean = new AssessmentBean();
        assessmentBean.content = jsonObj.optString("content");
        assessmentBean.sceneryNarrate = jsonObj.optDouble("sceneryNarrate");
        assessmentBean.serviceAttitude = jsonObj.optDouble("serviceAttitude");
        assessmentBean.routeFamiliar = jsonObj.optDouble("routeFamiliar");
        return assessmentBean;
    }
}
