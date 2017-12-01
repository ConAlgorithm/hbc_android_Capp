package com.hugboga.custom.data.bean.city;

import java.util.List;

/**
 * 目的地首页
 * Created by HONGBO on 2017/11/27 20:58.
 */

public class DestinationHomeVo {
    public int destinationId;   //目的地ID
    public String destinationName;  //目的地名称
    public int destinationType;    //目的地类型 101：线路圈 201：国家 202：城市
    public String destinationImageUrl; //目的地图片URL
    public int destinationGoodsCount;  //目的地关联玩法数量
    public int destinationAssociateGuideCount;  //目的地关联司导数量
    public int destinationServiceGuideCount;   //目的地服务司导数量
    public BeginnerDirectionVo beginnerDirection;  //新手入门
    public List<DayCountVo> dayCountList;  //游玩天数列表
    public List<DestinationTagVo> destinationTagList;  //目的地标签列表
    public List<DestinationCityVo> depCityList;    //出发城市列表
    public List<DestinationGoodsVo> destinationGoodsList;  //目的地玩法列表
    public List<ServiceConfigVo> serviceConfigList;    //自定义包车配置
}
