package com.hugboga.custom.data.bean.city;

import java.util.List;

/**
 * 目的地玩法下单配置
 * Created by HONGBO on 2017/11/28 10:37.
 */

public class ServiceConfigVo {
    public String title; //名称
    public String desc; //说明
    public String imageUrl; //图片地址
    public List<String> serviceLabelList; //服务标签列表
    public int depCityId; //出发城市Id 线路圈、国家为0
    public int serviceType; //1：接送机 3：包车 4：次租
}
