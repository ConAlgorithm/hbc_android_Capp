package com.hugboga.custom.data.bean;

import java.util.List;

/**
 * SKU item
 * Created by admin on 2016/3/3.
 */
public class SkuItemBean implements com.huangbaoche.hbcframe.data.bean.IBaseBean {

    public String goodsMinPrice;//最低价格
    public String goodsName;//商品名
    public String goodsNo;//商品ID
    public String goodsPicture;//图片
    public int saleAmount;//销售数量
    public String salePoints;//标签
    public int guideAmount;//车导数量
    public int daysCount;//天数
    public String places;//
    public String skuDetailUrl;//详情地址
    public String shareURL;//分享地址

    public int arrCityId;
    public String arrCityName;
    public int depCityId;
    public String depCityName;
    public int goodsType;
    public List<CityBean> passCityList;
    public List<PoiBean> passPoiList;
    public String passPoiListStr;

}
