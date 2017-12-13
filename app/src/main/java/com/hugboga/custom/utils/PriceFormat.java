package com.hugboga.custom.utils;

import java.text.DecimalFormat;

/**
 * 价格部分格式化
 * Created by HONGBO on 2017/10/28 17:50.
 */

public class PriceFormat {

    public static String price(double price) {
        return new DecimalFormat("¥###,###.00").format(price);
    }

    public static String priceNoPoint(String price){
        Double priceD = CommonUtils.getCountDouble(price);
        return new DecimalFormat("###").format(priceD);
    }

    public static String month2(int month) {
        return new DecimalFormat("00").format(month);
    }
}
