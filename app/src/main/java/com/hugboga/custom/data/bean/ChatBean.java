package com.hugboga.custom.data.bean;

import com.hugboga.custom.data.parser.ParserChatOrder;
import com.hugboga.im.entity.HbcLogicImBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 私信
 * Created by ZHZEPHI on 2016/3/4.
 */
public class ChatBean extends HbcLogicImBean implements Serializable{

    public String targetAvatar; //头像地址
    public String targetName; //用户名  页面上展示的用户名字
    public String targetId; //目标ID 司导ID
    public String userId; //用户ID
    public int userType; //用户类型
    public List<ChatOrderBean> orderInfo; //私信显示订单
    public int inBlack;//判断是否加入黑名单

    public String flag; //司导所在国家的国旗
    public int timediff; //与北京时间差时
    public int timezone;
    public String city_name;
    public String country_name;

    public int isCancel;//判断和当前司导是否有正在进行的订单 // 1:订单已取消


    public void parseObject(JSONObject jsonObject) throws Throwable {
        this.targetAvatar = jsonObject.optString("targetAvatar");
        this.targetName = jsonObject.optString("targetName");
        this.targetId = jsonObject.optString("targetId");
        this.setTargetType(jsonObject.optInt("targetType"));
        this.userId = jsonObject.optString("userId");
        this.userType = jsonObject.optInt("userType");
        this.inBlack = jsonObject.optInt("inBlack");
        this.setNeTargetId(jsonObject.optString("neTargetId"));
        this.isCancel = jsonObject.optInt("isCancel");

        this.flag = jsonObject.optString("flag");
        this.timediff = jsonObject.optInt("timediff");
        this.timezone = jsonObject.optInt("timezone");
        this.country_name = jsonObject.optString("city_name");
        this.country_name = jsonObject.optString("country_name");


        //解析IM订单
        JSONArray orderArray = jsonObject.optJSONArray("orderInfo");
        this.orderInfo = new ArrayList<>();
        if (orderArray != null) {
            ParserChatOrder mParserChatOrder = new ParserChatOrder();
            for (int j = 0; j < orderArray.length(); j++) {
                this.orderInfo.add(mParserChatOrder.parseObject(orderArray.optJSONObject(j)));
            }
        }
    }
}
