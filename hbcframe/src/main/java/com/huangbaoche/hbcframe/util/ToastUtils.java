package com.huangbaoche.hbcframe.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.R;

/**
 * Created by qingcha on 17/8/19.
 */
public final class ToastUtils {

    private ToastUtils() {}

    private static Toast toast;

    public static Toast makeToast(Context context, String string) {
        LayoutInflater inflater = LayoutInflater.from(context);
        TextView tv = (TextView) inflater.inflate(R.layout.view_toast, null);
        tv.setText(string);
        Toast toast = new Toast(context);
        toast.setView(tv);
        toast.setGravity(android.view.Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    public static Toast makeToast(Context context, int resId) {
        return makeToast(context, context.getString(resId));
    }

    public static void showToast(Context context, int resId) {
        try {
            if (toast == null) {
                toast = makeToast(context, resId);
            } else {
                TextView tv = (TextView) toast.getView().findViewById(R.id.view_toast_tv);
                tv.setText(resId);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context context, String msg) {
        try {
            if (toast == null) {
                toast = makeToast(context, msg);
            } else {
                TextView tv = (TextView) toast.getView().findViewById(R.id.view_toast_tv);
                tv.setText(msg);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
