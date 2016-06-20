package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/5/28.
 */
public class GuidesDetailData implements Serializable {

    private String areaCode; //导游手机区号
    private String mobile;//导游手机号
    private int carBrandId;
    private String carBrandName; //车品牌名
    private int carClass;
    private String carClassName;//车座系
    private int carId;
    private String carLicenceNo;//车牌号
    private int carLuggageNum;
    private String carName;//车型名称
    private int carSeatNum;
    private int carType;
    private String carTypeName;//车型级别名称
    private int cityId;
    private String cityName;//导游所在城市名
    private int continentId;
    private String continentName;//司导所在大洲
    private int countryId;
    private String countryName;//司导所在国家
    private String guideId;//导游id
    private int guideLevel;//5不显示包车
    private String guideName;//导游姓名
    private String guideNo;//导游编号
    private float serviceStar;//导游服务星级
    private String avatar;
    private int isFavored;//是否被该用户收藏，0否，1是
    private ArrayList<Integer> serviceTypes;//服务类型列表 1.接送机，2.包车，3.单次接送
    private ArrayList<String> carPhotosL;//全图
    private ArrayList<String> carPhotosS;//缩略图
    private int commentNum;//评价数
    private ArrayList<EvaluateItemData> comments;

    public ArrayList<EvaluateItemData> getComments() {
        return comments;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public ArrayList<String> getCarPhotosS() {
        return carPhotosS;
    }

    public ArrayList<String> getCarPhotosL() {
        return carPhotosL;
    }

    public int getCarPersonNum() {
        return carPersonNum;
    }

    public void setCarPersonNum(int carPersonNum) {
        this.carPersonNum = carPersonNum;
    }

    private int carPersonNum;// 乘坐人数上限

    public String getCarDesc() {
        return carDesc;
    }

    public void setCarDesc(String carDesc) {
        this.carDesc = carDesc;
    }

    private String carDesc;// 车型描述


    public boolean isCollected() {
        return isFavored == 1;
    }

    public void setIsFavored(int isFavored) {
        this.isFavored = isFavored;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getMobile() {
        return mobile;
    }

    public int getCarBrandId() {
        return carBrandId;
    }

    public String getCarBrandName() {
        return carBrandName;
    }

    public int getCarClass() {
        return carClass;
    }

    public String getCarClassName() {
        return carClassName;
    }

    public int getCarId() {
        return carId;
    }

    public String getCarLicenceNo() {
        return carLicenceNo;
    }

    public int getCarLuggageNum() {
        return carLuggageNum;
    }

    public String getCarName() {
        return carName;
    }

    public int getCarSeatNum() {
        return carSeatNum;
    }

    public int getCarType() {
        return carType;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public int getContinentId() {
        return continentId;
    }

    public String getContinentName() {
        return continentName;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getGuideId() {
        return guideId;
    }

    public int getGuideLevel() {
        return guideLevel;
    }

    public String getGuideName() {
        return guideName;
    }

    public String getGuideNo() {
        return guideNo;
    }

    public float getServiceStar() {
        return serviceStar;
    }

    public ArrayList<Integer> getServiceTypes() {
        return serviceTypes;
    }
}
