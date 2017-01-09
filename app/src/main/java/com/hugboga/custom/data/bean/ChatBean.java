package com.hugboga.custom.data.bean;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.parser.ParserChatOrder;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 私信
 * Created by ZHZEPHI on 2016/3/4.
 */
public class ChatBean  implements Serializable{

    public String targetAvatar; //头像地址
    public String targetName; //用户名  页面上展示的用户名字
    public String targetId; //目标ID
    public int targetType; //目标类型
    public String message; //消息
    public String timeStr; //时间
    public int imCount; //未读消息数
    public String userId; //用户ID
    public int userType; //用户类型
    public List<ChatOrderBean> orders; //私信显示订单
    public int inBlack;//判断是否加入黑名单
    public long timeStamp;

    public String flag; //司导所在国家的国旗
    public int timediff; //与北京时间差时
    public int timezone;
    public String cityName;
    public String countryName;

    //public String rTargetId;//融云用户id;
    //public String rTargetToken; //融云用户token
    public String nTargetId; //云信用户名  登录云信时的用户名
    public String nTargetToken; //云信token
    public int isCancel;//判断和当前司导是否有正在进行的订单



    public void parseObject(JSONObject jsonObject) throws Throwable {
        this.targetAvatar = jsonObject.optString("targetAvatar");
        this.targetName = jsonObject.optString("targetName");
        this.targetId = jsonObject.optString("targetId");
        this.targetType = jsonObject.optInt("targetType");
        this.imCount = jsonObject.optInt("imCount");
        this.message = jsonObject.optString("lastMsg");
        this.timeStr = jsonObject.optString("lastTime");
        this.userId = jsonObject.optString("userId");
        this.userType = jsonObject.optInt("userType");
        this.inBlack = jsonObject.optInt("inBlack");
        this.nTargetId = jsonObject.optString("neTargetId");
        this.nTargetToken = jsonObject.optString("neTargetToken");
        this.isCancel = jsonObject.optInt("isCancel");

        this.flag = jsonObject.optString("flag");
        this.timediff = jsonObject.optInt("timediff");
        this.timezone = jsonObject.optInt("timezone");
        this.cityName = jsonObject.optString("city_name");
        this.countryName = jsonObject.optString("country_name");


        //解析IM订单
        JSONArray orderArray = jsonObject.optJSONArray("orderInfo");
        this.orders = new ArrayList<>();
        if (orderArray != null) {
            ParserChatOrder mParserChatOrder = new ParserChatOrder();
            for (int j = 0; j < orderArray.length(); j++) {
                this.orders.add(mParserChatOrder.parseObject(orderArray.optJSONObject(j)));
            }
        }
    }
}
