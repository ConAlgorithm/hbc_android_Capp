package com.hugboga.custom.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.widget.ShareDialog;
import com.ta.utdid2.android.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
                toast.setText(resId);
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
                toast.setText(msg);
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

    public static Long getCountLong(String _count) {
        if (TextUtils.isEmpty(_count)) {
            return 0L;
        }
        long count = 0;
        try {
            count = Long.valueOf(_count);
        } catch(Exception e) {
            return 0L;
        }
        return count;
    }

    public static void shareDialog(final Context context, final String picUrl
            , final String title, final String content, final String shareUrl) {
        shareDialog(context, picUrl, title, content, shareUrl, "", null);
    }

    public static void shareDialog(final Context context, final String picUrl
            , final String title, final String content, final String shareUrl, final String source, ShareDialog.OnShareListener listener) {
        ShareDialog shareDialog = new ShareDialog(context);
        shareDialog.setParams(new ShareDialog.Params(picUrl, title, content, shareUrl, source));
        if (listener != null) {
            shareDialog.setOnShareListener(listener);
        }
        shareDialog.show();
    }

    public static void shareDialog(final Context context, final int resID
            , final String title, final String content, final String shareUrl) {
        shareDialog(context, resID, title, content, shareUrl, "", null);
    }

    public static void shareDialog(final Context context, final int resID
            , final String title, final String content, final String shareUrl, final String source) {
        shareDialog(context, resID, title, content, shareUrl, source, null);
    }

    public static void shareDialog(final Context context, final int resID
            , final String title, final String content, final String shareUrl, final String source, ShareDialog.OnShareListener listener) {
        ShareDialog shareDialog = new ShareDialog(context);
        shareDialog.setParams(new ShareDialog.Params(resID, title, content, shareUrl, source));
        if (listener != null) {
            shareDialog.setOnShareListener(listener);
        }
        shareDialog.show();
    }

    public static void share(final Context context, final int type, final String picUrl, final String title, final String content, final String shareUrl) {
        try {
            Glide.with(context)
                    .load(picUrl)
                    .asBitmap()
                    .centerCrop()
                    .listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            WXShareUtils.getInstance(context).share(type, resource, title, content, shareUrl);
                            return false;
                        }
                    })
                    .into(500, 500)
                    .get();
        }catch (Exception e){
        }
    }


    public static String getEncodedString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String encodedString = null;
        try {
            encodedString = URLEncoder.encode(URLEncoder.encode(str, "utf-8"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedString;
    }

    public static String getBaseUrl(String utl) {
        if (TextUtils.isEmpty(utl)) {
            return "";
        }
        String result = utl;
        if (utl.indexOf("?") == -1) {
            result += "?";
        } else if (utl.lastIndexOf("?") != utl.length() - 1 && utl.charAt(utl.length() - 1) != '&') {
            result += "&";
        }
        return result;
    }

    public static String replaceUrlValue(String url, String key, String value) {
        if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(key)) {
            url = url.replaceAll("(" + key +"=[^&]*)", key + "=" + value);
        }
        return url;
    }

    public static String getDiskCacheDir() {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = MyApplication.getAppContext().getExternalCacheDir().getPath();
        } else {
            cachePath = MyApplication.getAppContext().getCacheDir().getPath();
        }
        return cachePath;
    }

    public static String getDiskFilesDir(String type) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = MyApplication.getAppContext().getExternalFilesDir(type).getPath();
        } else {
            cachePath = MyApplication.getAppContext().getFilesDir().getPath();
        }
        return cachePath;
    }

    public static boolean isLogin(Context context) {
        if (context != null && !UserEntity.getUser().isLogin(context)) {
            CommonUtils.showToast(R.string.login_hint);
            Intent intent= new Intent(context, LoginActivity.class);
            if (context instanceof BaseActivity) {
                intent.putExtra(Constants.PARAMS_SOURCE, ((BaseActivity)context).getEventSource());
            }
            context.startActivity(intent);
            return false;
        } else {
            return true;
        }
    }
}
