package com.huangbaoche.hbcframe;

import com.huangbaoche.hbcframe.data.parser.ServerParser;

/**
 * Created by admin on 2016/2/27.
 */
public class HbcConfig {
    public static boolean IS_DEBUG = false;
    public static String PACKAGE_NAME;//包名
    public static String VERSION_NAME;//版本号
    public static int VERSION_CODE;//版本code
    public static String APP_NAME;//app 名字
    /*** 设置服务端返回格式解析器*/
    public static Class parser = ServerParser.class;
    /*** 设置服务端返回格式解析器*/
    public static Class accessKeyRequest;
    /*** 设置dialogUtil 实现类*/
    public static Class dialogUtil;
    /*** 服务端返回错误码处理器 ServerCodeHandlerInterface 实现类*/
    public static Class serverCodeHandler;
    /*** 设置服务端返回格式解析器*/
    public static  String serverHost = "";
    /**微信*/
    public static java.lang.String WX_APP_ID;
    /*渠道名*/
    public static String FLAVOR;
}
