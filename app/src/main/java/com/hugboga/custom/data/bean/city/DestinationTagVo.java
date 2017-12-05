package com.hugboga.custom.data.bean.city;

import java.util.List;

/**
 * 二级子标签
 * Created by HONGBO on 2017/11/28 10:31.
 */

public class DestinationTagVo {
    public String tagId;  //标签ID
    public String tagName; //标签名称
    public int tagLevel; //签级别 1：一级 2：二级
    public List<String> goodsDepCityIdSet; //标签关联的商品出发城市集合
}
