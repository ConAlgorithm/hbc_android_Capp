package com.hugboga.custom;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.huangbaoche.hbcframe.HbcApplication;
import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestAccessKey;
import com.hugboga.custom.utils.Common;
import com.hugboga.custom.utils.Config;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.x;

import io.rong.imkit.RongIM;

/**
 * Created by admin on 2016/2/25.
 */
public class MyApplication extends HbcApplication {

    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.setDebug(true);
        initUrlHost();
        RongIM.init(this); // 初始化融云IM
        initConfig();
        mAppContext = this.getApplicationContext();
        Log.e("hbcApplication", "debug " + BuildConfig.DEBUG);
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    private void initUrlHost() {
        String channel = PhoneInfo.getVersionChannel(this);
        //根据工程渠道标识，设置访问的服务器全局信息，没有标识则默认访问开发服务器
        MLog.e("channel=" + channel);
        //根据工程渠道标识，设置访问的服务器全局信息，没有标识则默认访问开发服务器
        if(TextUtils.isEmpty(channel))channel = "formal";
        String host = UrlLibs.SERVER_IP_HOST_PUBLIC_FORMAL;
        MLog.e("channel = "+channel);
        UrlLibs.UrlHost urlHost = UrlLibs.UrlHost.valueOf(channel.toUpperCase());
        MLog.e("urlHost="+urlHost);
        if(urlHost!=null){
            host = urlHost.url;
        }
        UrlLibs.SERVER_IP_HOST_PUBLIC = UrlLibs.SERVER_HTTP_SCHEME_HTTP+host;
    }

    private void initConfig(){
        HbcConfig.serverHost= UrlLibs.SERVER_IP_HOST_PUBLIC;
        HbcConfig.accessKeyRequest = RequestAccessKey.class;
        HbcConfig.dialogUtil = DialogUtil.class;
        HbcConfig.PACKAGE_NAME = BuildConfig.APPLICATION_ID;
        HbcConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        HbcConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        HbcConfig.APP_NAME = getString(R.string.app_name);
        HbcConfig.IS_DEBUG=true;
        HbcConfig.WX_APP_ID = Constants.WX_APP_ID;
    }
}
