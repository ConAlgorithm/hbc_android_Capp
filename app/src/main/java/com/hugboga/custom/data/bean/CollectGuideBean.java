package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by qingcha on 16/5/23.
 */
public class CollectGuideBean implements Serializable {

    public String guideId;                      // 司导id
    public String name;                         // 司导名字
    public String carDesc;                      // 车型描述 (如经济5坐)
    public String carModel;                     // 车型信息(凯迪拉克 ATS 2.5)
    public String avatar;                       // 用户头像
    public float stars;                         // 评分
    public int numOfLuggage;                    // 行李数
    public int numOfPerson;                     // 乘坐人数
    public int status;                          // 可预约状态 1.可预约、0.不可预约
    public int carClass;                        // 座系 5/7/9/12
    public int carType;                         // 车型 1/2/3/4 分别对应 经济/舒适/豪华/奢华
    public int carModelId;                      // 车型ID
    public int special;                         // 0.普通车型 1.特殊车型
    public ArrayList<Integer> serviceTypes;     // 服务类型列表 1.接送机，2.包车，3.单次接送

    public int gender;                          //性别 1:男 2:女 0:未知

    public int cityId;                          //司导所在城市ID
    public String cityName;                     //司导所在城市名
    public int countryId;                       //司导所在国家ID
    public String countryName;                  //司导所在国家名称

    /**
     * 判断可预约状态 1.可预约(true)、0.不可预约
     * */
    public boolean isAppointments() {
        return status == 1;
    }
}
