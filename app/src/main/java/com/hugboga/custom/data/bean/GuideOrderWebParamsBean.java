package com.hugboga.custom.data.bean;

import java.io.Serializable;

public class GuideOrderWebParamsBean implements Serializable{

    public int orderType;  // 1：接送机、2：单次接送、3：包车、4：线路
    public String guideId;
    public String guideName;
    public String guideAvatar;
    public String guideCountryName;
    public String guideCityId;
    public String guideCityName;
    public int isQuality; //是否优质司导, 1-是，0-否
    public String goodsNo;//orderType=4，必传 商品ID

}
