package com.huangbaoche.hbcframe.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetWork {
	private static String TAG = "NetWork";
	
	/**
	 * 
	 * @Title getWapUrl
	 * @Description 返回wap 的拼接后的url，如 http://www.baidu.com/img/baidu_logo.gif
	 *              拼接后http://10.0.0.172/img/baidu_logo.gif
	 * @param url
	 * @return String 返回类型
	 * @throws
	 * @author aceway-liwei
	 * @date 2012-6-5 上午11:04:03
	 */
	public static String getWapUrl(String url)
	{
		if ((url == null) || (url.equals("")))
		{
			return null;
		}
		url = url.replaceFirst("http://", "");
		url = url.substring(url.indexOf("/"), url.length());
		return "http://10.0.0.172" + url;
	}
	
	
	/**
	 * 当前网络是否为3G或WIFI
	 * 
	 * @param ctx  参数
	 * @return boolean  返回类型
	 */
	public static boolean isWifiOr3G(Context ctx)
	{
		boolean isok = false;
		ConnectivityManager connManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		String netTypeName = connManager.getActiveNetworkInfo().getTypeName();
		if (netTypeName != null
				&& (netTypeName.toUpperCase().indexOf("WIFI") >= 0 || netTypeName.toUpperCase()
						.indexOf("NET") >= 0))
		{
			isok = true;
		}

		return isok;
	}

	/**
	 * 打开wifi设置
	 * 
	 * @param ctx
	 */
	public static void openWIFI(Context ctx)
	{
		Intent intent = new Intent("android.settings.WIFI_SETTINGS");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(intent);
	}
	/**
	 * 判断网络是否可用    true 可用
	 * 
	 * @param mContext
	 * @return 
	 */
	public static boolean isNetworkAvailable(Context mContext)
	{
		if(mContext==null)return false;
		ConnectivityManager mConnectManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectManager!=null&&mConnectManager.getActiveNetworkInfo() != null
				&& mConnectManager.getActiveNetworkInfo().isAvailable())
		{
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @Title isUsingCmwap
	 * 
	 * @Description 是否 是用2G的 wap 联网
	 * @param context
	 * @return boolean true 是用wap，false 不是
	 */

	public static boolean isUsingCmwap(Context context)
	{
		boolean result = false;
		try
		{
			ConnectivityManager cwjManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cwjManager.getActiveNetworkInfo();
			if ((info.getTypeName().equalsIgnoreCase("MOBILE"))
					&& ((info.getExtraInfo().contains("cmwap")) || 

(info.getExtraInfo()
							.contains("CMWAP"))))
				return true;
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}

	
	/**
	 * 
	 * @Title getHost
	 * @Description 得到 网址的 host 如 http://www.baidu.com/img/baidu_logo.gif 得到
	 *              www.baidu.com
	 * @param url
	 * @return 参数
	 * @return String 返回类型
	 */
	public static String getHost(String url)
	{
		if ((url == null) || (url.equals("")))
			return null;
		url = url.replaceFirst("http://", "");
		return url.substring(0, url.indexOf("/"));
	}

	/**
	 * 判断是否为WIFI网络连接   true 是
	 * 
	 * @param ctx
	 * @return boolean
	 */
	public static boolean isWifiNetworkAvailable(Context ctx)
	{
		ConnectivityManager connManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (netInfo != null)
		{
			boolean isConntected = netInfo.isConnected();
			return isConntected;
		} else
		{
			return false;
		}
	}

	/**
	 * 判断是否需要代理，如果需要代理ture则为wap， 不需要代理false则为net或WIFI
	 * 
	 * @param ctx
	 * @return boolean
	 */
	public static boolean isProxy(Context ctx)
	{
		boolean isProxy = false;
		ConnectivityManager connManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		NetworkInfo mobNetInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


		if (wifiNetInfo != null)
		{
			boolean isConntected = wifiNetInfo.isConnected();
			if (isConntected)
				return false;
		}
		if (mobNetInfo != null)
		{
			boolean isConntected = mobNetInfo.isConnected();
			String mobTypeName = mobNetInfo.getExtraInfo();
			mobNetInfo.getSubtypeName();
			// 判断是net OR wap
			if (mobTypeName != null && mobTypeName.toLowerCase().indexOf("wap") >= 0
					&& isConntected)
			{
				return true;
			}
		}

		return isProxy;
	}
}

