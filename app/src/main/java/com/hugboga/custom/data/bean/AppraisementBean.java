package com.hugboga.custom.data.bean;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/6.
 */
public class AppraisementBean implements Serializable {
    public String orderNo;
    public String content;//内容
    public float totalScore;//总分
    public String userCommentTime;//评价时间
    public ArrayList<GuideLabels> guideLabels;

    public static class GuideLabels implements Serializable {
        public String name;
        public boolean checked;
    }

//    public void parser(JSONObject jsonObj) throws JSONException {
//        Gson gson = new Gson();
//        gson.fromJson(jsonObj.toString(), AppraisementBean.class);
//    }

}
