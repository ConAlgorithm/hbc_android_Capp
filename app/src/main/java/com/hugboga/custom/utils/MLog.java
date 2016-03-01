package com.hugboga.custom.utils;

import android.util.Log;

public class MLog {

	private static final String TAG = "Hugboga.LOG";

	public static  void i(String msg) {
		if (Config.DEBUG_ENABLE) {
			Log.i(TAG, formatMsg(msg));
		}
	}

	public static  void w(String msg) {
		if (Config.DEBUG_ENABLE) {
			Log.w(TAG, formatMsg(msg));
		}
	}

	public static  void e(String msg) {
		if (Config.DEBUG_ENABLE) {
			Log.e(TAG, formatMsg(msg));
		}
	}

	public static  void e(String msg, Throwable throwable) {
		if (Config.DEBUG_ENABLE) {
			Log.e(TAG, formatMsg(msg), throwable);
		}
	}

	public static  void d(String msg) {
		if (Config.DEBUG_ENABLE) {
			Log.d(TAG, formatMsg(msg));
		}
	}

	public static  void d(String msg, Throwable throwable) {
		if (Config.DEBUG_ENABLE) {
			Log.d(TAG, formatMsg(msg), throwable);
		}
	}

	public static String formatMsg(String msg) {
		StackTraceElement cStackTraceElement = null;
		try {
			cStackTraceElement = Thread.currentThread().getStackTrace()[4];
			msg = String.format("%s(%s:%s):%s",
					cStackTraceElement.getClassName(),
					cStackTraceElement.getMethodName(),
					cStackTraceElement.getLineNumber(), msg);
		} catch (Exception e) {
			MLog.e(e.toString());
		}
		return msg;
	}

}
