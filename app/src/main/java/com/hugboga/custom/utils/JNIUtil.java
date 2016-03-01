package com.hugboga.custom.utils;

import android.content.Context;

/**
 *
 * JNI
 *
 * Created by admin on 2015/12/2.
 */
public class JNIUtil {

   static {
		System.loadLibrary("hugboga");
	}

    public static native String getSign(Context con);

}
