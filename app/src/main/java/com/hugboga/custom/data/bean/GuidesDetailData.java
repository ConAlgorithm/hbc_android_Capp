package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/5/28.
 */
public class GuidesDetailData implements Serializable {

    public String guideId;                          // 导游id
    public String guideNo;                          // 导游编号
    public String guideName;                        // 导游姓名
    public String avatar;                           // 司导或地接社头像
    public float serviceStar;                       // 导游服务星级
    public int guideLevel;                          // 司导等级
    public int gender;                              // 司导性别: 1男，2女
    public String cityBackGroundPicSrc;             // 城市背景图

    public int agencyType;                          // 地接社类型: 0个人司导，1老版地接社员工，2老版地接社老板，3新版地接社
    public String agencyDriverAvatar;               // 司机头像（如果是地接社）
    public String agencyDriverName;                 // 司机姓名（如果是地接社）
    public int agencyDriverGender;                  // 司机性别: 1男，2女

    public int isFavored;                           // 是否被该用户收藏，0否，1是
    public int serviceDaily;                        // 是否可服务包车，0否，1是
    public int serviceJsc;                          // 是否可服务接送机、单次接送，0否，1是

    public int cityId;                              // 城市id
    public String cityName;                         // 导游所在城市名
    public int continentId;                         // 大洲id
    public String continentName;                    // 司导所在大洲
    public int countryId;                           // 国家id
    public String countryName;                      // 司导所在国家

    public int commentNum;                          // 评价数
    public ArrayList<EvaluateItemData> comments;
    public ArrayList<CommentLabel> commentLabels;   // 评价标签排序

    public ArrayList<GuideCarBean> guideCars;       // 车辆信息
    public int guideCarCount;                       // 车辆数

    public int isQuality;                           // 是否优质司导, 1-是，0-否

    public static class CommentLabel implements Serializable {
        public int labelCount;      // 评价次数
        public int labelId;         // 标签id
        public String labelName;    // 标签名
    }

    public boolean isCollected() {
        return isFavored == 1;
    }

    public String getCarIds() {
        if (guideCars == null || guideCars.size() <= 0) {
            return "";
        }
        String carIds = "";
        int size = guideCars.size();
        for (int i = 0; i < size; i++) {
            carIds += guideCars.get(i).carModelId;
            if (i < size - 1) {
                carIds += ",";
            }
        }
        return carIds;
    }

    public String getLabels() {
        if (commentLabels == null || commentLabels.size() <= 0) {
            return "";
        }
        String labels = "";
        int size = commentLabels.size();
        for (int i = 0; i < size; i++) {
            labels += commentLabels.get(i).labelName;
            if (i < size - 1) {
                labels += ",";
            }
        }
        return labels;
    }

}
