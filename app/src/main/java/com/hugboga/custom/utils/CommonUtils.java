package com.hugboga.custom.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.hugboga.custom.MyApplication;

/**
 * Created by qingcha on 16/5/26.
 */
public final class CommonUtils {

    private CommonUtils() {}

    private static Toast toast;

    public static void showToast(int resId) {
        try {
            if (toast == null) {
                toast = Toast.makeText(MyApplication.getAppContext(), resId, Toast.LENGTH_SHORT);
            } else {
                toast.cancel();
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(String msg) {
        try {
            if (toast == null) {
                toast = Toast.makeText(MyApplication.getAppContext(), msg, Toast.LENGTH_SHORT);
            } else {
                toast.cancel();
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getCountString(String count) {
        if (TextUtils.isEmpty(count)) {
            return "0";
        }
        try {
            count = Integer.valueOf(count) < 0 ? "0" : count;
        } catch(Exception e) {
            return "0";
        }
        return count;
    }

    public static Integer getCountInteger(String _count) {
        if (TextUtils.isEmpty(_count)) {
            return 0;
        }
        int count = 0;
        try {
            count = Integer.valueOf(_count);
        } catch(Exception e) {
            return 0;
        }
        return count;
    }
}
