package com.hugboga.custom.data.bean;

import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;

import java.io.Serializable;
import java.util.List;

/**
 * 私信
 * Created by ZHZEPHI on 2016/3/4.
 */
public class ChatBean implements Serializable{

    public String targetAvatar; //头像地址
    public String targetName; //用户名
    public String targetId; //目标ID
    public int targetType; //目标类型
    public String message; //消息
    public String timeStr; //时间
    public int imCount; //未读消息数
    public String userId; //用户ID
    public int userType; //用户类型
    public List<ChatOrderBean> orders; //私信显示订单
    public int inBlack;
    public long timeStamp;

    //public String rTargetId;//融云用户id;
    //public String rTargetToken; //融云用户token
    public String nTargetId; //云信用户名
    public String nTargetToken; //云信token
    public int isCancel;

}
