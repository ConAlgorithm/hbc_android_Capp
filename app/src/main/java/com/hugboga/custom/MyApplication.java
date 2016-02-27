package com.hugboga.custom;


import android.util.Log;

import com.huangbaoche.hbcframe.*;
import com.huangbaoche.hbcframe.BuildConfig;
import com.hugboga.custom.data.net.UrlLibs;

import org.xutils.x;

/**
 * Created by admin on 2016/2/25.
 */
public class MyApplication extends HbcApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.setDebug(true);
        Log.e("hbcApplication", "debug " + BuildConfig.DEBUG);
        initConfig();
    }

    private void initConfig(){
        HbcConfig.serverHost= UrlLibs.SERVER_IP_HOST_PUBLIC;
    }
}
