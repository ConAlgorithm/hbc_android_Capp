package com.hugboga.custom.data.bean;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.action.ActionBean;
import com.hugboga.custom.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZHZEPHI on 2015/7/29.
 */
public class PushMessage implements IBaseBean {

    public String type; //App根据此参数的内容判断此条message的意图
    public String messageID; //如果有对应的PUSH，此参数必有，且与PUSH的pushID相同
    public String title; //标题。如没有该参数，在通知栏中标题显示为App名称，在App中的弹出框中标题显示为“提示”
    public String message; //描述，App将原样显示其中的内容。如没有则无该参数
    public String force;
    public String url;
    public String orderNo;
    public String orderType;

    public String action;

    private ActionBean actionBean;

    public ActionBean getActionBean() {
        if (actionBean == null && !TextUtils.isEmpty(action)) {
            actionBean = (ActionBean) JsonUtils.fromJson(action, ActionBean.class);
        }
        return actionBean;
    }

}
