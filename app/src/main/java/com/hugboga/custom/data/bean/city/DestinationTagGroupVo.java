package com.hugboga.custom.data.bean.city;

import java.util.List;

/**
 * 一级标签
 * Created by HONGBO on 2017/12/1 12:00.
 */

public class DestinationTagGroupVo {
    public int tagId;  //标签ID
    public String tagName; //标签名称
    public int tagLevel; //签级别 1：一级 2：二级
    public List<String> goodsDepCityIdSet; //标签关联的商品出发城市集合
    public List<DestinationTagVo> subTagList; //子标签类别
}
