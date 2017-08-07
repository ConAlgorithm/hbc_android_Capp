package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhangqiang on 17/8/3.
 */

public class HomeCityContentVo2 implements Serializable {
    public int cityId	= 0;//	城市ID
    public String cityName = "";//城市名称
    public ArrayList<HomeCityGoodsVo> cityGoodsList = null;//商品列表
}
