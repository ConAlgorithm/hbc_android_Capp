package com.hugboga.custom.data.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by qingcha on 17/4/19.
 */
public class FilterGuideBean implements Serializable{

    public String guideId;              // 司导标识
    public String guideNo;              // 司导工号
    public int guideLevel;              // 司导等级
    public String guideLevelName;       // 司导等级名称
    public String guideCover;           // 司导主页banner大图
    public String avatar;               // 司导头像
    public String guideAvatarUrl;       // 收藏司导列表
    public int cityId;                  // 司导所在城市标识
    public String cityName;             // 司导所在城市名称
    public String guideName;            // 司导姓名
    public String gender;               // 司导性别
    public String genderName;           // 性别名称
    public int completeOrderNum;        // 服务完成订单数
    public int commentNum;              // 评论数
    private double serviceStar;          // 星级分数
    public ArrayList<String> skillLabelNames;// 特殊技能标签
    public String homeDesc;             // 司导个人简介
    public String serviceTypes;         // 提供的服务,(服务标识，逗号隔开)
    public int isQuality;               // 是否优质司导, 1-是，0-否
    public int availableStatus;         // 是否可用司导：1-是，0-否
    public int isOffline;               // 是否被审核下线状态，默认0：0否，1是
    public int serviceDaily;            // 是否可服务包车，0否，1是
    public int serviceJsc;              // 是否可服务接送机、单次接送，0否，1是

    public int isCollected;             // 是否收藏过司导,1收藏 0 未收藏
    public boolean isShowCity = true;   // 本地字段 筛选当前城市不显示城市
    public String orderUrl;             // 跳转预订页面url
    public String decisionMaker;        // 精选司导列表右侧是否展示消息图片  展示为1   不展示为0
    public String getServiceType() {
        if (serviceDaily == 0 && serviceJsc == 0) {
            return "";
        }
        String result = "";
        if (serviceDaily == 1 && serviceJsc == 1) {
            result += "接送机、单次接送、按天包车游";
        } else if (serviceDaily == 1) {
            result += "按天包车游";
        } else if (serviceJsc == 1) {
            result += "接送机、单次接送";
        }
        return result;
    }

    public boolean isCanService() {
        return availableStatus == 1 && isOffline == 0;
    }

    public double getServiceStar() {
        try {
            BigDecimal bigDecimal = new BigDecimal(serviceStar);
            return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            return serviceStar;
        }
    }

    public String getGuideDesc() {
        String result = "";
        if (!TextUtils.isEmpty(homeDesc) && !TextUtils.isEmpty(homeDesc.trim())) {
            result = homeDesc.trim().replace("\n", " ");
        }
        return result;
    }
}
