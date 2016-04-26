package com.hugboga.custom.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hugboga.custom.MyApplication;

/**
 * Created  on 16/4/19.
 */
public class ChannelUtils {

    public static String getChannel(Context context){
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String channel = appInfo.metaData.getInt("APP_CHANNEL",10003)+"";
            return channel;
        }catch (Exception e){
            return "not_get";
        }
    }

    public static PackageManager manager;
    public static PackageInfo info;

    public static String getVersion() {
        manager = MyApplication.getAppContext().getPackageManager();
        try {
            info = manager.getPackageInfo(MyApplication.getAppContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }

    public static int getVersionCode() {
        manager = MyApplication.getAppContext().getPackageManager();
        try {
            info = manager.getPackageInfo(MyApplication.getAppContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionCode;
    }
}
