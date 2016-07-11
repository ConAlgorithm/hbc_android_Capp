package com.hugboga.custom.utils;


import com.hugboga.custom.BuildConfig;
import com.orhanobut.logger.Logger;

public class LogUtils {
    static String LOG_TAG = "HUANG_BAO_CHE";
    static{
        Logger.init(LOG_TAG);
    }
    public static void e(String msg){
        if(BuildConfig.DEBUG) {
            Logger.e(msg);
        }
    }

    public static void json(String msg){
        if(BuildConfig.DEBUG) {
            Logger.json(msg);
        }
    }

}
