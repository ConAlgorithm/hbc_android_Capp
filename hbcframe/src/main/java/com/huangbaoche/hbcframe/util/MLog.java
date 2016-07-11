package com.huangbaoche.hbcframe.util;


import android.util.Log;

import com.huangbaoche.hbcframe.HbcConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MLog {



	public static final String TAG = "HBC.LOG";

	public static  void i(String msg) {
			if(HbcConfig.IS_DEBUG)
			Log.i(TAG, formatMsg(msg));
	}

	public static  void i(String tag, String msg) {
		if(HbcConfig.IS_DEBUG)
			Log.i(tag, formatMsg(msg));
	}

	public static  void w(String msg) {
		if(HbcConfig.IS_DEBUG)
			Log.w(TAG, formatMsg(msg));
	}

	public static  void e(String msg) {
		if(HbcConfig.IS_DEBUG)
			Log.e(TAG, formatMsg(msg));
	}

	public static  void e(String msg, Throwable throwable) {
		if(HbcConfig.IS_DEBUG)
			Log.e(TAG, formatMsg(msg), throwable);
	}

	public static  void d(String msg) {
		if(HbcConfig.IS_DEBUG)
			Log.d(TAG, formatMsg(msg));
	}

	public static  void d(String msg, Throwable throwable) {
		if(HbcConfig.IS_DEBUG)
			Log.d(TAG, formatMsg(msg),throwable);
	}

	public static String formatMsg(String msg) {
		StackTraceElement cStackTraceElement = null;
		try {
			cStackTraceElement = Thread.currentThread().getStackTrace()[4];
			msg = String.format("%s %s(%s:%s):%s",
					getCurrentTime(),
					cStackTraceElement.getClassName(),
					cStackTraceElement.getMethodName(),
					cStackTraceElement.getLineNumber(), msg);
		} catch (Exception e) {
			MLog.e(e.toString());
		}
		return msg;
	}


	public static String getCurrentTime(){
		 SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSSZ");
		return  timeFormat.format(new Date());
	}
}
