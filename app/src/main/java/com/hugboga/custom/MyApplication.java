package com.hugboga.custom;


import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.huangbaoche.hbcframe.HbcApplication;
import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.net.ServerCodeHandler;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestAccessKey;
import com.hugboga.custom.widget.DialogUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

/**
 * Created by admin on 2016/2/25.
 */
public class MyApplication extends HbcApplication {

    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MobclickAgent.setDebugMode(HbcConfig.IS_DEBUG);
        x.Ext.setDebug(true);
        initUrlHost();
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        initRongIm(); // 初始化融云IM
        initConfig();
        mAppContext = this.getApplicationContext();
        Log.e("hbcApplication", "debug " + BuildConfig.DEBUG);
        try {
            CrashReport.initCrashReport(this, "900024779", false);
//            Reservoir.init(this, 4096);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    private void initUrlHost() {
        MLog.e("urlHost=" + BuildConfig.API_SERVER_URL);
        if(TextUtils.isEmpty(BuildConfig.API_SERVER_URL)) {
            String channel = PhoneInfo.getVersionChannel(this);
            MLog.e("channel=" + channel);
            //根据工程渠道标识，设置访问的服务器全局信息，没有标识则默认访问正式服务器
            if (TextUtils.isEmpty(channel)) channel = "formal";
            String host = UrlLibs.SERVER_IP_HOST_PUBLIC_FORMAL;
            String scheme = UrlLibs.SERVER_HTTP_SCHEME_HTTPS;
            MLog.e("channel = " + channel);
            UrlLibs.UrlHost urlHost = UrlLibs.UrlHost.valueOf(channel.toUpperCase());

            if (urlHost != null) {
                host = urlHost.url;
            }
            if("formal".equals(channel)) {
                scheme = UrlLibs.SERVER_HTTP_SCHEME_HTTPS;
            }else{
                scheme = UrlLibs.SERVER_HTTP_SCHEME_HTTP;
            }
            UrlLibs.SERVER_IP_HOST_PUBLIC = scheme + host;
        }else {
            UrlLibs.SERVER_IP_HOST_PUBLIC = BuildConfig.API_SERVER_URL;
        }
    }

    private void initConfig() {
        HbcConfig.serverHost = UrlLibs.SERVER_IP_HOST_PUBLIC;
        HbcConfig.accessKeyRequest = RequestAccessKey.class;
        HbcConfig.dialogUtil = DialogUtil.class;
        HbcConfig.serverCodeHandler = ServerCodeHandler.class;
        HbcConfig.PACKAGE_NAME = BuildConfig.APPLICATION_ID;
        HbcConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        HbcConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        HbcConfig.APP_NAME = getString(R.string.app_name);
        x.Ext.setDebug(HbcConfig.IS_DEBUG);
        HbcConfig.WX_APP_ID = Constants.WX_APP_ID;
    }

    /**
     * 初始化融云IM
     */
    private void initRongIm() {
        try {
            if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) || "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
                RongIM.init(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
