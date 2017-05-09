package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CollectGuideNewBean implements Serializable{

    @SerializedName("guides")
    public List<FilterGuideBean> guideList;
    public int count;

    public class CollectGuideItemBean implements Serializable{
        public String guideId;                          // 司导id
        public String guideNo;                          // 司导工号
        public String guideName;                        // 司导姓名
        public String avatar;                           // 司导头像
        public String guideAvatarUrl;                   // 司导头像地址
        public int cityId;                              // 司导所在城市ID
        public String cityName;                         // 司导所在城市名
        public int gender;                              // 性别 1:男 2:女 0:未知
        public String genderName;                       // 性别名称
        public int completeOrderNum;                    // 服务完成订单数
        public int commentNum;                          // 评论数
        public Double serviceStar;                      // 星级分数,（null 则暂无星级）
        public ArrayList<String> skillLabelNames;       // 特殊技能标签
        public int availableStatus;                     // 是否可用司导：1-是，0-否
        public int serviceDaily;                        // 是否可服务包车，1-是，0-否
        public int serviceJsc;                          // 是否可服务接送机、单次接送，1-是，0-否
    }
}
