package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qingcha on 17/8/25.
 */

public class OrderPriceInfoBean implements Serializable{

    public List<OrderPriceInfoItemBean> priceInfoList;

    public static class OrderPriceInfoItemBean implements Serializable {
        public String title;
        public List<String> labelList;
    }
}
