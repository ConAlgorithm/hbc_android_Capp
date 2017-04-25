package com.hugboga.custom.data.bean;

import java.io.Serializable;

public class GuideOrderWebParamsBean implements Serializable{

    public int orderType;  // 1：接送机、2：单次接送、3：包车
    public String guideId;
    public String guideName;
    public String guideAvatar;
    public String guideCountryName;
    public String guideCityId;
    public String guideCityName;
}
