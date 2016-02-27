package com.huangbaoche.hbcframe;

import com.huangbaoche.hbcframe.data.parser.ServerParser;

/**
 * Created by admin on 2016/2/27.
 */
public class HbcConfig {

    /*** 设置服务端返回格式解析器*/
    public static  Class parser = ServerParser.class;
    public static  String serverHost = "";
}
