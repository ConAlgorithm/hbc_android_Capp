package com.huangbaoche.hbcframe.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;

import com.huangbaoche.hbcframe.BuildConfig;
import com.huangbaoche.hbcframe.HbcConfig;

import org.xutils.common.util.LogUtil;


/**
 * Phone 相关工具类
 * 
 * @version 0.1
 * @author wlx
 * @serial
 * 
 */
public class PhoneInfo {


	/** 手机平台 */
	private static String mos;
	private static String sdkver=android.os.Build.VERSION.SDK;
	/** 触摸屏 yes/flightNo */
	private static String touch;
	/** 手机上网方式 */
	private static String net; // 手机上网方式
	/** 通信运营商 */
	private static String opr; // 通信运营商
	/** 手机串号 */
	private static String imei; // 手机串号
	/** 手机型号 */
	private static String phonever;
	/**mac地址*/
	private static String mac;
	/**手机imsi*/
	private static String imsi;

	private static String appfinger;//指纹

	/*** 系统版本号*/
	public static String getPhoneVer(){
		return android.os.Build.VERSION.RELEASE;
	}

	/**手机型号*/
	public static String getUa(){
		return android.os.Build.MODEL;
	}

	/**包名*/
	public static String getPackageName(){
		return HbcConfig.PACKAGE_NAME;
	}
	/**软件版本号*/
	public static String getSoftVer(){
		return HbcConfig.VERSION_NAME;
	}
	/**软件版本Code*/
	public static int getSoftVerCode(){
		return HbcConfig.VERSION_CODE;
	}
	/**app 名字*/
	public static String getAppName(){
		return HbcConfig.APP_NAME;
	}



	/** 联网方式*/
	public static String getNet(Context ctx){
		if(net ==null){
		try{
		ConnectivityManager manager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null)
			net = info.getTypeName().toUpperCase();// getTypeName返回一个网络的名字，比如wifi；toUpperCase把字符串变成大写
		if (net.indexOf("WIFI") < 0) {
			net = (manager.getAllNetworkInfo()[0].getSubtypeName() != null ? manager
					.getAllNetworkInfo()[0].getSubtypeName().toUpperCase()
					: net);
		}
		net = net.split(" ")[0];
		}catch (Exception e) {
		}}
		return  net;
		}
	/**
	 * 获得设备的IMEI号
	  * @return String    返回类型
	 * */
	public static String getImei(Context ctx)
	{
		if(imei==null) {
			TelephonyManager telephonyManager = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager != null)
				imei = telephonyManager.getDeviceId();
		}
		return imei;
	}

	/**
	 * 
	  * @Title getIMSI
	  * @Description 返回唯一的用户ID;就是这张卡的唯一编号
	  * @return String    返回类型
	 */
 
	public static String getIMSI(Context ctx)
	{
		if(imsi==null) {
			TelephonyManager telephonyManager = (TelephonyManager) ctx
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (imsi != null) {
				imsi = telephonyManager.getSubscriberId();
			}
		}
		return imsi;
	}
	
	/**
	  * 获得mac地址
	  * 
	  * @return String    返回类型
	 * */
	public static String getMac(Context ctx){
		if(mac == null){
		WifiManager wifi = (WifiManager)ctx.getSystemService(Context.WIFI_SERVICE);
		
		if(wifi!=null){
			WifiInfo info = wifi.getConnectionInfo();
			if(info!=null)
			mac = info.getMacAddress();
		}
		}
        return mac;
	}
	/**
	 * 获得设备上的手机号码
	 * 
	 * @return String    返回类型
	 * */
	public static String getPhoneNumber(Context ctx)
	{
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
		if(opr == null) {
			// 返回唯一的用户ID;就是这张卡的编号神马的
			String IMSI = getIMSI(context);
			// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
				opr = "中国移动";
			} else if (IMSI.startsWith("46001")) {
				opr = "中国联通";
			} else if (IMSI.startsWith("46003")) {
				opr = "中国电信";
			}
		}
		return opr;
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
			LogUtil.e(e.toString());
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
			LogUtil.e(e.toString());
		}
		return version;
	}
	/**
	  * @Title getScreenWidth
	  * @Description  屏幕的宽
	  * @param activity
	  * @author aceway-liwei
	  * @date 2013-12-29 下午03:56:07
	 */
	public static  int getScreenWidth(Activity activity){
	Display display = activity.getWindowManager().getDefaultDisplay(); // 获得屏幕显示
	return  display.getWidth();
	}
	/**
	  * @Title getScreenWidth
	  * @Description  屏幕的高
	  * @param activity
	  * @author aceway-liwei
	  * @date 2013-12-29 下午03:56:07
	 */
	public static  int getScreenHeight(Activity activity){
		
		Display display = activity.getWindowManager().getDefaultDisplay(); // 获得屏幕显示
		return  display.getHeight();
	}
	/**
	  * @Title getScreenWidth
	  * @Description  密度
	  * @param activity
	  * @author aceway-liwei
	  * @date 2013-12-29 下午03:56:07
	 */
	public static float getDensity(Activity activity){
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
	 * @<uses-permission 
	 *                   android:name="android.permission.SEND_SMS"></uses-permission
	 *                   >
	 * @param context
	 * @param number
	 * @param msg
	 * @param show
	 * 
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
	 * @<uses-permission android:name="android.permission.INTERNET" />
	 * @param context
	 * @param url
	 */
	public static void OpenBrowser(Context context, String url) {
		Uri uri = Uri.parse(url); // 浏览器
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(it);
	}
	
	/**
     * 检测该包名所对应的应用是否存在
     * @param packageName
     * @return
     */

    public static boolean checkPackage(Activity activity,String packageName)
    { 
        if (packageName == null || "".equals(packageName)) 
            return false; 
        try
        { 
        	activity.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true; 
        }
        catch (NameNotFoundException e)
        { 
            return false; 
        } 
    }


	
}
