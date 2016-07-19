package com.hugboga.custom.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.data.bean.UserEntity;

import org.xutils.http.RequestParams;

import java.net.HttpURLConnection;
import java.net.URLConnection;


/**
 * Phone 相关工具类
 *
 * @author wlx
 * @version 0.1
 * @serial
 */
public class PhoneInfo {


    /**
     * 添加与服务器请求URL的手机基本信息 addPhoneInfo
     *
     * @return String 设定文件
     * @Title: getPhoneInfo
     * @date 2012-5-28 下午4:29:36
     */
    public static String getPhoneInfo() {
        StringBuilder url = new StringBuilder();

        url.append("softver=").append(Config.getSoftver());
        url.append("&softvercode=").append(Config.getSoftvercode());
        url.append("&chid=").append(Config.getChid());
        url.append("&imei=").append(Config.getImei());
        url.append("&ua=").append(Config.getUa());
        url.append("&mos=").append(Config.getMos());
        url.append("&screenw=").append(Config.getScreenWidth());
        url.append("&screenh=").append(Config.getScreenHeight());
        url.append("&net=").append(Config.getNet());
        url.append("&imsi=").append(Config.getImsi());
        url.append("&mac=").append(Config.getMac());
//		url.append("&usersession=").append(UserInfo.getInstance().getUserSession());
//		url.append("&devicesession=").append(UserInfo.getInstance().getDevicesession());
        url.append("&appfinger=").append(Config.getAppfinger());
        url.append("&platform=").append("android");
        url.append("&ifacever=").append("1.0.0");
        return url.toString();
    }

    public static void setUserAent(RequestParams params) {
//		HN-Salary Android/1.1.1.1101 (2.3.7; Nexus One Build/FRF91) 300x200 [GooglePlay]
//		params.setHeader("Content-Type", "application/json");
        params.setHeader("Content-Type", "application/x-www-form-urlencoded ");
        params.setHeader("User-Agent", PhoneInfo.getUserAent());
//        params.setHeader("XToken", UserEntity.getUser().getToken());
//        params.setHeader("XDevice", Config.getImei());
    }

    public static void setUserAent(URLConnection conn) {
//		HN-Salary Android/1.1.1.1101 (2.3.7; Nexus One Build/FRF91) 300x200 [GooglePlay]
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", PhoneInfo.getUserAent());
//	        conn.setRequestProperty("XToken", UserEntity.getUser().getToken());
        conn.setRequestProperty("XDevice", Config.getImei());
    }

    public static String getUserAent() {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append(" Android/" + Config.getSoftver() + "." + Config.getSoftvercode());
        strBuffer.append(" (" + Config.getPhonever() + "; " + Config.getUa() + ")");
        strBuffer.append(" " + Config.getScreenHeight() + "x" + Config.getScreenWidth());
        strBuffer.append(" [" + Config.getChid() + "]");
        return strBuffer.toString();
    }

    public static void setConnectHead(HttpURLConnection conn) {
        conn.setRequestProperty("softver", Config.getSoftver());
        conn.setRequestProperty("softvercode", "" + Config.getSoftvercode());
        conn.setRequestProperty("chid", Config.getChid());
        conn.setRequestProperty("imei", Config.getImei());
        conn.setRequestProperty("ua", Config.getUa());
        conn.setRequestProperty("mos", Config.getMos());
        conn.setRequestProperty("screenw", "" + Config.getScreenWidth());
        conn.setRequestProperty("screenh", "" + Config.getScreenHeight());
        conn.setRequestProperty("net", Config.getNet());
        conn.setRequestProperty("imsi", Config.getImsi());
        conn.setRequestProperty("mac", Config.getMac());
//		conn.setRequestProperty("usersession", UserInfo.getInstance().getUserSession());
//		conn.setRequestProperty("devicesession", UserInfo.getInstance().getDevicesession());
        conn.setRequestProperty("appfinger", Config.getAppfinger());
        conn.setRequestProperty("platform", "android");
        conn.setRequestProperty("appfinger", "1.0.0");
    }

    /**
     * 获取打包后的工程渠道标识
     *
     * @return
     */
    public static String getVersionChannel(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String channelStr = String.valueOf(info.metaData.get("APP_CHANNEL"));
            return channelStr;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getNetState(Context ctx) {
        String nettype = ""; // 联网方式
        try {
            ConnectivityManager manager = (ConnectivityManager) ctx
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null)
                nettype = info.getTypeName().toUpperCase();// getTypeName返回一个网络的名字，比如wifi；toUpperCase把字符串变成大写
            if (nettype.indexOf("WIFI") < 0) {
                nettype = (manager.getAllNetworkInfo()[0].getSubtypeName() != null ? manager
                        .getAllNetworkInfo()[0].getSubtypeName().toUpperCase()
                        : nettype);
            }
            nettype = nettype.split(" ")[0];
        } catch (Exception e) {
        }
        return nettype;
    }

    /**
     * 获得设备的IMEI号
     *
     * @return String    返回类型
     */
    public static String getIMEI(Context ctx) {
        String imei = "";
        TelephonyManager telephonyManager = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null)
            imei = telephonyManager.getDeviceId();
        if (TextUtils.isEmpty(imei))
            imei = "0";

        return imei;
    }

    /**
     * @return String    返回类型
     * @Title getIMSI
     * @Description 返回唯一的用户ID;就是这张卡的唯一编号
     */

    public static String getIMSI(Context ctx) {
        String imsi = "";
        TelephonyManager telephonyManager = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (imsi != null) {
            imsi = telephonyManager.getSubscriberId();
        }
        if (TextUtils.isEmpty(imsi))
            imsi = "0";
        return imsi;
    }

    /**
     * 获得mac地址
     *
     * @return String    返回类型
     */
    public static String getMac(Context ctx) {
        String mac = "";
        WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);

        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            if (info != null)
                mac = info.getMacAddress();
        }
        if (TextUtils.isEmpty(mac)) {
            mac = "0";
        }
        return mac;
    }

    /**
     * 获得设备上的手机号码
     *
     * @return String    返回类型
     */
    public static String getPhoneNumber(Context ctx) {
        String number = "";
        TelephonyManager telephonyManager = (TelephonyManager) ctx
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null)
            number = telephonyManager.getLine1Number();
        if (TextUtils.isEmpty(number))
            number = "0";

        return number;
    }

    /**
     * Role:Telecom service providers获取手机服务商信息 &lt;BR&gt;
     * 需要加入权限&lt;uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/&gt; &lt;BR&gt;
     * Date:2012-3-12 &lt;BR&gt;
     *
     * @param context
     * @return String
     */
    public static String getProvidersName(Context context) {
        String ProvidersName = null;
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String IMSI = getIMSI(context);
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }

    /**
     * 获取应用版本号
     *
     * @param ctx
     * @return
     */
    public static String getSoftwareVersion(Context ctx) {
        String version = "1.0";
        try { // PackageInfo:通过manifast收集关于这个包得所有信息
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (Exception e) {
            MLog.e(e.toString());
        }
        return version;
    }

    /**
     * 获取应用版本号
     *
     * @param ctx
     * @return
     */
    public static int getSoftwareVersionCode(Context ctx) {
        int version = 1;
        try { // PackageInfo:通过manifast收集关于这个包得所有信息
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);
            version = packageInfo.versionCode;
        } catch (Exception e) {
            MLog.e(e.toString());
        }
        return version;
    }

    /**
     * @param activity
     * @Title getScreenWidth
     * @Description 屏幕的宽
     * @author aceway-liwei
     * @date 2013-12-29 下午03:56:07
     */
    public static int getScreenWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay(); // 获得屏幕显示
        return display.getWidth();
    }

    /**
     * @param activity
     * @Title getScreenWidth
     * @Description 屏幕的高
     * @author aceway-liwei
     * @date 2013-12-29 下午03:56:07
     */
    public static int getScreenHeight(Activity activity) {

        Display display = activity.getWindowManager().getDefaultDisplay(); // 获得屏幕显示
        return display.getHeight();
    }

    /**
     * @param activity
     * @Title getScreenWidth
     * @Description 密度
     * @author aceway-liwei
     * @date 2013-12-29 下午03:56:07
     */
    public static float getDensity(Activity activity) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(
                mDisplayMetrics);
        return mDisplayMetrics.density;
    }

    /**
     * 打电话接口，并调取拨号盘
     *
     * @param context
     * @param number
     */
    public static void CallDial(Context context, String number) {
        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }


    /**
     * 发短信接口 show = true 调出编辑短信界面,show = false 直接发送短信
     *
     * @param context
     * @param number
     * @param msg
     * @param show
     * @<uses-permission android:name="android.permission.SEND_SMS"></uses-permission
     * >
     */
    public static void SendMessage(Context context, String number, String msg,
                                   boolean show) {
        if (show) {
            Uri uri1 = Uri.parse("smsto:" + number);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri1);
            i.putExtra("sms_body", msg);
            context.startActivity(i);
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, msg, null, null);
        }
    }

    /**
     * 播放音乐接口 example path = "file:///sdcard/song.mp3"
     *
     * @param context
     * @param filepath
     */
    public static void PlayMedia(Context context, String filepath) {
        Uri uri = Uri.parse(filepath);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        it.setType("audio/mp3");
        context.startActivity(it);
    }

    /**
     * 播放视频接口 example path = "file:///sdcard/media.mp4"
     *
     * @param context
     * @param filepath
     */
    public static void PlayVideo(Context context, String filepath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(filepath);
        intent.setDataAndType(uri, "video/*");
        context.startActivity(intent);
    }

    /**
     * 打开浏览器 并打开网页 example ur = "http://www.baidu.com"
     *
     * @param context
     * @param url
     * @<uses-permission android:name="android.permission.INTERNET" />
     */
    public static void OpenBrowser(Context context, String url) {
        Uri uri = Uri.parse(url); // 浏览器
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param packageName
     * @return
     */

    public static boolean checkPackage(Activity activity, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            activity.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 判断是否是新版本
     * Created by ZHZEPHI at 2015年4月3日 下午3:45:04
     *
     * @return
     */
    public static boolean isNewVersion(Context context) {
        String versionInfo = PhoneInfo.getSoftwareVersion(context);
        String oldVersion = UserEntity.getUser().getVersion(context);
        return oldVersion == null || !versionInfo.equals(oldVersion);
    }

    //通讯录
    public static String[] getPhoneContacts(Context context, Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = context.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
            //取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);

            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);

            if(phone != null){
                phone.moveToFirst();
                try{
                    contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }catch (Exception e){
                    contact[1] = "";
                }
            }

            phone.close();
            cursor.close();
        }else{
//            (TAG, "get Contacts is fail");
        }

        return contact;
    }

}
