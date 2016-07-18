package com.hugboga.custom.data.bean;

import java.util.List;

/**
 * 私信
 * Created by ZHZEPHI on 2016/3/4.
 */
public class ChatBean {

    public String targetAvatar; //头像地址
    public String targetName; //用户名
    public String targetId; //目标ID
    public String targetType; //目标类型
    public String message; //消息
    public String timeStr; //时间
    public int imCount; //未读消息数
    public String userId; //用户ID
    public String userType; //用户类型
    public List<ChatOrderBean> orders; //私信显示订单
    public int inBlack;

}
