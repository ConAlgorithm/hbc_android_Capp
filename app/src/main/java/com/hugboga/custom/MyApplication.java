package com.hugboga.custom;


import android.util.Log;

import com.huangbaoche.hbcframe.HbcApplication;
import com.huangbaoche.hbcframe.HbcConfig;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestAccessKey;

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
        HbcConfig.accessKeyRequest = RequestAccessKey.class;
        HbcConfig.PACKAGE_NAME = BuildConfig.APPLICATION_ID;
        HbcConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        HbcConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        HbcConfig.APP_NAME = getString(R.string.app_name);
        HbcConfig.APP_NAME = "皇包车";
    }
}
