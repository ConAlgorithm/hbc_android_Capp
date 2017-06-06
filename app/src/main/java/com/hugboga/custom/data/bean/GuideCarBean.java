package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 2016/10/8.
 */

public class GuideCarBean implements Serializable{
    public int carBrandId;
    public String carBrandName;//车品牌名
    public int carClass;
    public int carId;
    public String carInfo1;//"经济5座", #车型信息
    public String carInfo2;//"本田 Vezel", #车品牌信息
    public String carLicenceNo;// "A1234567", #车牌号
    public String carLicenceNoCovered;// "A1****67", #车牌号-遮盖
    public int carModelId;
    public String carName;
    public String carPhoto;//#车正面照
    public int carSeatNum;
    public int carType;
    public int guideCarId;
    public int modelGuestNum;//#客人数
    public int modelLuggageNum; //#行李数
    public int special;
    public int isInOrder;//#是不是本次服务，1是，0否
    public ArrayList<String> carPhotosL;

    public boolean isSpecialCar() {
        return special == 1;
    }
}
