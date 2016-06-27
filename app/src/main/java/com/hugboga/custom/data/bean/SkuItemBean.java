package com.hugboga.custom.data.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SKU item
 * Created by admin on 2016/3/3.
 */
public class SkuItemBean implements Serializable {

    public String goodsNo;//商品ID
    public String goodsName;//商品名
    public String goodsPicture;//商品图片
    public String goodsDesc;//商品描述
    public String perPrice;//商品人均价格
    public int guideAmount;//车导数量
    public String headLable;//商品标签（超省心或超自由)
    public String bookLable;//预定日期标签(今日可订)
    public int goodsClass;//商品类别（1，固定线路；2，推荐线路）
    public ArrayList<CharacteristicLables> characteristicLables;

    public String cityId;


    public String goodsMinPrice;//最低价格
    public int saleAmount;//销售数量
    public String salePoints;//标签
    public String keyWords;//关键字
    public int daysCount;//天数
    public String places;
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


    public int hotelCostAmount;//天数
    public int hotelStatus;//是否有酒店

    public static class CharacteristicLables implements Serializable {
        public String lableName;
        public String lableType;
    }
}
