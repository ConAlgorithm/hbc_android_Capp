package com.hugboga.custom.action.data;

import com.google.gson.annotations.SerializedName;
import com.hugboga.custom.action.constants.ActionType;

import java.io.Serializable;

/**
 * Created by qingcha on 16/7/27.
 *
 * acition协议wiki：http://wiki.hbc.tech/pages/viewpage.action?pageId=5505202
 */
public class ActionBean implements Serializable {

    /**
     * t: type  value: 1 活动; 2 UI界面; 3 功能; 无此字段则为 旧协议
     * */
    @SerializedName("t")
    public String type;

    /**
     * u: url  value: path路径（需要app拼接host，详见 特殊处理）
     * */
    @SerializedName("u")
    public String url;

    /**
     * v: vcid  外部调起界面id，映射表详见WIKI: http://wiki.hbc.tech/pages/viewpage.action?pageId=5505204
     * */
    @SerializedName("v")
    public String vcid;

    /**
     * f: function（功能id）映射表详见WIKI: http://wiki.hbc.tech/pages/viewpage.action?pageId=5505206
     * */
    @SerializedName("f")
    public String function;

    /**
     * d: data  value: json数据（有数据的界面会用到此key读取其数据）
     * */
    @SerializedName("d")
    public Object data;

    /**
     * 记录来源(本地定义)
     * */
    public String source;

    public ActionBean(int vcid) {
        this.type = "" + ActionType.NATIVE_PAGE;
        this.vcid = "" + vcid;
    }

}
