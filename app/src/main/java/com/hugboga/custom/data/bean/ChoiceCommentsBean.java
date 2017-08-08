package com.hugboga.custom.data.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/8/7.
 */
public class ChoiceCommentsBean implements Serializable{

    @SerializedName("resultBean")
    public ArrayList<ChoiceCommentsItemBean> listData;
    public int totalSize;

    public static class ChoiceCommentsItemBean implements Serializable {
        // 用户信息
        public String userId; // 用户id
        public String userAvatar; // 用户头像
        public String userName; // 用户姓名
        public String nickName; // 用户昵称

        // 订单信息
        public Integer totalDays; // 订单天数，仅包车有效
        public String serviceTime; // 订单服务时间
        public Integer orderType; // 订单类型
        public String orderTypeName; // 后补字段，订单类型名称
        public Integer serviceCityId; // 服务城市
        public String serviceCityName; // 服务城市名称

        // 评价信息
        public Integer totalScore; // 评价星级
        public String content; // 评价内容
        public String commentPics; // 评价图片
        public List<String> commentPic; // 后补字段，评价图片列表
        public List<String> commentPicL; // 后补字段，评价图片大图列表
        public String guideReply; // 司导回复

        // 司导信息
        public String guideId; // 司导id
        public String guideAvatar; // 司导头像
        public String guideName; // 司导姓名
        public List<String> guideLabels; // 司导标签
    }

}
