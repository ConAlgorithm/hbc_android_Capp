package com.hugboga.custom.utils;

import android.content.Context;
import android.os.Environment;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.UserEntity;

import java.io.File;
import java.net.URLEncoder;


public class Config {
	public static boolean DEBUG_ENABLE = true;
	
	
	public static final int channelId = 18;//查价 下单的 渠道号


	public static final int PAGENO = 0;//初始第几页
	public static final int PAGESIZE = 10;//每页的数量
	
	public static final int REFRESH = 1;//刷新
	public static final int LOADMORE = 2;//加载更多
	
	/**文件下载存储的路径           */
	public  static String APP_SAVE_DIR ="hugboga";
	/**文件 的路径           */
	public  static String HOST_SET_PATH ="urlseting";
	public  static String HOST_SET_FILE ="hugbogaurl.txt";
	public  static String CONFIG_Path ="config";
	public  static String CONFIG_FILE ="init.txt";

	
	/**错误信息 提交地址  */
//	private static String API_ERRORINFO = "www.错误信息 提交地址";
	/**下载状态通知         */
	private static String NOTIFY_STATE_ACTION ="hugboga_download_state_action";
	/**下载进度的通知            */
	private static String NOTIFY_DOWNLOAD_ACTION ="hugboga_download_progress_action" ;
	
	/** 软件编号（本软件为： ）           */
	private static String softid="hugboga";
	private static String pageName="com.hugboga.custom";
	private static String appName="皇包车";

	
	/**渠道存储的文件名 */
	private static String CHANNEL_FILENAME = "source.txt";
	/**文件下载存储的文件夹名*/
	private static String FILE_DOWNLOAD_NAME =  "file";
	/**图片下载存储的文件夹名*/
	private static String PIC_CACHE_NAME = "pic_cache";
	/**记录下载信息到本地 文件名签名*/
	private static String LOCAL_APK_INFO_CONFIG = ".apkinfoconfig.config";
	/**后缀*/
	private  static String IMAGE_TMP =".tmp";

	
	private final static String SDCardRoot = Environment
	.getExternalStorageDirectory().getAbsolutePath()
	+ File.separator;
	/**文件下载存储的路径*/
	private static String FILE_DOWNLOAD_DIR ;
	/**图片下载存储的路径*/
	private static String PIC_CACHE_DIR ;

	

	/** 渠道编号 Channel ID             */
	private static String chid; // 渠道编号
	/** 屏幕宽 */
	private static int screenWidth;
	/** 屏幕高 */
	private static int screenHeight;
	/** 手机平台 */
	private static String mos;
	private static String sdkver;
	/** 软件版本  */
	private static String softver;
	/** 软件版本码 */
	private static int softvercode;
	/***/
	private static String userkey;
	/** 手机型号 */
	private static String ua; // 手机型号
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

	/**
	 * 获得手机设备的信息（渠道编号、IMEI、通信方式、联网方式、操作系统版本、软件版本、用户信息、用户名等等）
	 * */
	public static void phoneInit(Context ctx) {
		pageName = ctx.getPackageName();
		sdkver = android.os.Build.VERSION.SDK;
		mos = "android_" + android.os.Build.VERSION.SDK; // 获得手机平台（操作系统版本）
		softver = PhoneInfo.getSoftwareVersion(ctx); //得到软件的版本号
		softvercode = PhoneInfo.getSoftwareVersionCode(ctx); //得到软件的版本码

//		chid = Common.readAssetsChId(ctx).trim();//渠道号
//		chid = Common.getConfigParams(ctx, "UMENG_CHANNEL").trim();//渠道号
		try {
			// 获取设备的手机型号
			ua = android.os.Build.MODEL;
			phonever = android.os.Build.VERSION.RELEASE;
			if(opr!=null){
				opr = URLEncoder.encode(opr, "utf-8");
			}else{
				opr="0";
			}
		} catch (Exception e) {
			e.printStackTrace();
			MLog.e(e.toString());
		}
		touch = "yes";

		
		net = PhoneInfo.getNetState(ctx); //联网状态

		imei = PhoneInfo.getIMEI(ctx);
		imsi = PhoneInfo.getIMSI(ctx);
//		phone = PhoneInfo.getPhoneNumber(ctx);
		mac = PhoneInfo.getMac(ctx);
		UserEntity.getUser().getUserId(ctx);
		appName = ctx.getString(R.string.app_name);
	}
	/**
	  * @Title isEnable
	  * @Description 是否可用
	  * @return    参数
	  * @return boolean    返回类型
	  * @author aceway-liwei
	  * @date 2012-10-22 下午03:31:50
	 */
	public static boolean  isAvailable(){
		if(chid==null)
			return false;
		else
			return true;
	}
	public static String getRootDir(){
		return SDCardRoot;
	}
	
	public static String getFileDownloadDir() {
		
		if(FILE_DOWNLOAD_DIR==null)
			FILE_DOWNLOAD_DIR=getRootDir()+getAPP_SAVE_DIR() + File.separator+ getFILE_DOWNLOAD_NAME()+ File.separator;
		 return FILE_DOWNLOAD_DIR;
	}
	public static String getPicCacheDir() {
		if(PIC_CACHE_DIR==null){
			PIC_CACHE_DIR= getRootDir()+getAPP_SAVE_DIR() + File.separator+ getPIC_CACHE_NAME()+ File.separator;
		}
		return PIC_CACHE_DIR;
	}
	public static String getAppDir(){
		return getRootDir()+APP_SAVE_DIR;
	}
	 
	public static String getUrlSetDir(){
		return getRootDir()+APP_SAVE_DIR+ File.separator+HOST_SET_PATH;
	}
	public static String getConfigDir(){
		return getRootDir()+APP_SAVE_DIR+ File.separator+CONFIG_Path;
	}
	
	/**
	 * 下载状态通知NOTIFY_STATE_ACTION         
	 * @return
	 */
	public static String getNOTIFY_STATE_ACTION() {
		return NOTIFY_STATE_ACTION;
	}
	/**
	 * 下载进度的通知NOTIFY_DOWNLOAD_ACTION   
	 * @return
	 */
	public static String getNOTIFY_DOWNLOAD_ACTION() {
		return NOTIFY_DOWNLOAD_ACTION;
	}
	
	/** 软件编号softid（本软件为：dahan）必须初始化 */
	public static String getSoftid() {
		return softid;
	}
	/**
	 * 文件下载存储的路径APP_SAVE_DIR    
	 * 例："engine"
	 * @return
	 */
	public static String getAPP_SAVE_DIR() {
		return APP_SAVE_DIR;
	}
 
	
	/**
	 * 渠道存储的文件名  默认为："source.txt"
	 * @return
	 */
	public static String getCHANNEL_FILENAME() {
		return CHANNEL_FILENAME;
	}
	/**
	 * 文件下载存储的文件夹名    默认为："file"
	 * @return
	 */
	public static String getFILE_DOWNLOAD_NAME() {
		return FILE_DOWNLOAD_NAME;
	}
	/**
	 * 图片下载存储的文件夹名      默认为："pic_cache"
	 * @return
	 */
	public static String getPIC_CACHE_NAME() {
		return PIC_CACHE_NAME;
	}
	/**
	 * 记录下载信息到本地 文件名签名     默认为：".apkinfoconfig.config"
	 * @return
	 */
	public static String getLOCAL_APK_INFO_CONFIG() {
		return LOCAL_APK_INFO_CONFIG;
	}
	 
	/**
	 * 后缀     默认为：".tmp"
	 * @return
	 */
	public static String getTmpSuffix() {
		return IMAGE_TMP;
	}
	
	public static String getChid() {
		return chid;
	}
	public static void setChid(String chid) {
		Config.chid = chid;
	}
	public static String getSoftver() {
		return softver;
	}
	public static void setSoftver(String softver) {
		Config.softver = softver;
	}
	public static int getSoftvercode() {
		return softvercode;
	}
	public static void setSoftvercode(int softvercode) {
		Config.softvercode = softvercode;
	}	
	public static String getUserkey() {
		return userkey;
	}
	public static void setUserkey(String userkey) {
		Config.userkey = userkey;
	}
	public static int getScreenWidth() {
		return screenWidth;
	}
	public static int getScreenHeight() {
		return screenHeight;
	}
	public static String getMos() {
		return mos;
	}
	public static String getUa() {
		return ua;
	}
	public static String getPhonever() {
		return phonever;
	}
	public static void setPhonever(String phonever) {
		Config.phonever = phonever;
	}
	public static String getTouch() {
		return touch;
	}
	public static String getNet() {
		return net;
	}
	public static String getOpr() {
		return opr;
	}
	public static String getImei() {
		return imei;
	}
	public static String getMac() {
		return mac;
	}
	public static String getImsi() {
		return imsi;
	}

	public static String getScreen() {
		return 	 Config.screenWidth + "x"
		+  Config.screenHeight;
	}
	public static void setAppfinger(String appfinger) {
		Config.appfinger = appfinger;
	}
	public static String getAppfinger() {
		return appfinger;
	}
	public static String getSdkver() {
		return sdkver;
	}public static String getphonever() {
		return phonever;
	}


	public static String getPageName() {
		return pageName;
	}

	public static void setPageName(String pageName) {
		Config.pageName = pageName;
	}

	public static String getAppName() {
		return appName;
	}

	public static void setAppName(String appName) {
		Config.appName = appName;
	}
}
