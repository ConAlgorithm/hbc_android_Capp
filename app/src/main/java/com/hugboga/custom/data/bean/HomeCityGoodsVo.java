package com.hugboga.custom.data.bean;

import java.io.Serializable;

/**
 * Created by zhangqiang on 17/8/3.
 */

public class HomeCityGoodsVo implements Serializable {
    public String goodsNo	= "";//	商品NO
    public String goodsName	= "";//	商品名称
    public String recommendedReason ="";//		推荐语
    public int guidesNum	= 0;//	可服务司导数
    public int purchases	= 0;//	已购买数
    public int perPrice	= 0;//	人均单价
    public String goodsPic	 = "";//	商品图片
    public String guidePic = "";//司导头像
    public String goodsDetailUrl = ""; //跳转
    public int isCollected;     // 非接口返回,本地判断用,是否收藏过线路,1收藏 0 未收藏
}
