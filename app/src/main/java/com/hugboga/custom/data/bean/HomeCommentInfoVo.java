package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangqiang on 17/8/4.
 */

public class HomeCommentInfoVo implements Serializable {
    public String userId; // 用户id
    public String userAvatar; // 用户头像
    public String userName; // 用户姓名

    // 订单信息
    public Integer days; // 订单天数，仅包车有效
    public String serviceTime; // 订单服务时间
    public Integer orderType; // 订单类型
    public String orderTypeName; // 订单类型名称
    public Integer serviceCityId; // 服务城市
    public String serviceCityName; // 服务城市名称

    // 评价信息
    public Integer star; // 评价星级
    public String comment; // 评价内容
    public ArrayList<String> commentPics; // 评价图片
    public ArrayList<String> commentPicsL; // 评价图片大图
    public String guideReply; // 司导回复

    // 司导信息
    public String guideId; // 司导id
    public String guideAvatar; // 司导头像
    public String guideName; // 司导姓名
    public ArrayList<String> guideLabels; // 司导标签
}
