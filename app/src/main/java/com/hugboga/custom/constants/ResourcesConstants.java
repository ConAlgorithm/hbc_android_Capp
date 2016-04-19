package com.hugboga.custom.constants;

import java.io.File;
import java.util.HashMap;

/**
 * 资源 常量
 * Created by admin on 2015/11/20.
 */
public class ResourcesConstants {

    public static final int RESOURCES_H5_VERSION_DEFAULT = 19113;//默认H5的版本号
    public static final int RESOURCES_DB_VERSION_DEFAULT = 81;//默认DB的版本号
    public static final String RESOURCES_H5_NAME = "h5";//h5
    public static final String RESOURCES_PATH = "Resources";//资源目录路径
    public static final String RESOURCES_LOCAL_NAME = "hbc_h5";//资源名字
    public static final String RESOURCES_LOCAL_ZIP = ".zip";//压缩包后缀

    public static final String APP_CACHE = "file:///data/data/com.hugboga.custom/cache/";

    public static final String H5_LOCAL_PATH = APP_CACHE + RESOURCES_PATH + File.separator + RESOURCES_LOCAL_NAME + File.separator;
    public static final String H5_ABOUT = H5_LOCAL_PATH + "about.html";//关于我们
    public static final String H5_CANCEL = H5_LOCAL_PATH + "cancel.html";//订单取消规则
    public static final String H5_NOTICE = H5_LOCAL_PATH + "notice_v2_2.html";//预订须知
    public static final String H5_PRICE = H5_LOCAL_PATH + "price_v2_2.html";//费用说明
    public static final String H5_PROBLEM = H5_LOCAL_PATH + "problem.html";//常见问题
    public static final String H5_PROTOCOL = H5_LOCAL_PATH + "protocol.html";//协议
    public static final String H5_SERVICE = H5_LOCAL_PATH + "service.html";//服务承诺

    public static final String H5_TAI = H5_LOCAL_PATH + "tai/";
    public static final String H5_TAI_MANGU = H5_TAI + "BBK.html";//曼谷
    public static final String H5_TAI_PUJIDAO = H5_TAI + "bki.html";//普吉
    public static final String H5_TAI_QINGMAI = H5_TAI + "cnx.html";//清迈
    public static final String H5_TAI_SUMEIDAO = H5_TAI + "VSM.html";//苏梅岛

    //增项费用
    public static final String H5_OVER_PRICE_PICKUP = H5_LOCAL_PATH + "addfee_j.html";//接机
    public static final String H5_OVER_PRICE_SEND = H5_LOCAL_PATH + "addfee_s.html";//送机
    public static final String H5_OVER_PRICE_DAILY = H5_LOCAL_PATH + "addfee_r.html";//包车
    public static final String H5_OVER_PRICE_SINGLE = H5_LOCAL_PATH + "addfee_c.html";//次租
    public static final String H5_OVER_PRICE_X = H5_LOCAL_PATH + "addfee_x.html";//x

    public static HashMap<Integer, String> OverPriceMap = new HashMap<Integer, String>();

    static {
        OverPriceMap.put(Constants.BUSINESS_TYPE_PICK, H5_OVER_PRICE_PICKUP);
        OverPriceMap.put(Constants.BUSINESS_TYPE_SEND, H5_OVER_PRICE_SEND);
        OverPriceMap.put(Constants.BUSINESS_TYPE_DAILY, H5_OVER_PRICE_DAILY);
        OverPriceMap.put(Constants.BUSINESS_TYPE_RENT, H5_OVER_PRICE_SINGLE);
        OverPriceMap.put(Constants.BUSINESS_TYPE_OTHER, H5_OVER_PRICE_X);
    }

}
