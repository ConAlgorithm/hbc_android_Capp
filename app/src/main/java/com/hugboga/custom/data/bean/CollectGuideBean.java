package com.hugboga.custom.data.bean;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 16/5/23.
 */
public class CollectGuideBean implements Serializable {

    public String guideId;//司导id
    public String name;//司导名字
    public String carDesc;//车型描述 (如经济5坐)
    public String carModel;//车型信息(凯迪拉克 ATS 2.5)
    public float stars;//评分
    public int status;//可预约状态 1.可预约、0.不可预约
    public String avatar;//用户头像
    public int numOfLuggage;//行李数
    public int numOfPerson;//乘坐人数
    public int carClass;//座系 5/7/9/12
    public int carType;//车型 1/2/3/4 分别对应 经济/舒适/豪华/奢华
    public ArrayList<Integer> serviceTypes;//服务类型列表 1.接送机，2.包车，3.单次接送

    /**
     * 判断可预约状态 1.可预约(true)、0.不可预约
     * */
    public boolean isAppointments() {
        return status == 1;
    }
}