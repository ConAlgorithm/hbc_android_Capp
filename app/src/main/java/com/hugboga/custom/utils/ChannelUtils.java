package com.hugboga.custom.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.hugboga.custom.MyApplication;

import java.util.UUID;

/**
 * Created  on 16/4/19.
 */
public class ChannelUtils {

    public static String getChannel(Context context){
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object channel = appInfo.metaData.get("APP_CHANNEL");
            if(channel instanceof Integer){
               return  ""+Integer.valueOf(channel.toString());
            }else if(channel instanceof String){
                return channel.toString();
            }
            return "";

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

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
