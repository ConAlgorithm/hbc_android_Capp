package com.hugboga.custom.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.hugboga.custom.MyApplication;

public class ToastUtils {

    private ToastUtils() {}

    private static void show(Context context, int resId, int duration) {
        Toast.makeText(context, resId, duration).show();
    }

    private static void show(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void showShort(int resId) {
        Toast.makeText(MyApplication.getAppContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String message) {
        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int resId) {
        Toast.makeText(MyApplication.getAppContext(), resId, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String message) {
        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_LONG).show();
    }
    /**
	 * 显示Toast,保证到主线程吐司
	 * @param ctx
	 * @param msg
	 */
	public static void showTaost(final Activity ctx,final String msg){
		
		if("main".equals(Thread.currentThread().getName())){ // 判断 当前是否是在主线程 
			Toast.makeText(ctx.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		}else{
			// 不是主线程 
			ctx.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

}
