package com.hugboga.custom.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.ShareDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    public static void shareDialog(final Context context, final String picUrl
            , final String title, final String content, final String shareUrl) {
//        final AlertDialog.Builder callDialog = new AlertDialog.Builder(context);
//        callDialog.setTitle(context.getString(R.string.share));
//        final String [] callItems = new String[]{context.getString(R.string.share_friend), context.getString(R.string.share_moments)};
//        callDialog.setItems(callItems, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                WXShareUtils.getInstance(context).share(which + 1, Tools.getBitmap(context, picUrl), title, content, shareUrl);
//            }
//        });
//        AlertDialog dialog = callDialog.create();
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
        ShareDialog shareDialog = new ShareDialog(context);
        shareDialog.setParams(new ShareDialog.Params(picUrl, title, content, shareUrl));
        shareDialog.show();
    }

    public static void shareDialog(final Context context, final int resID
            , final String title, final String content, final String shareUrl) {
        ShareDialog shareDialog = new ShareDialog(context);
        shareDialog.setParams(new ShareDialog.Params(resID, title, content, shareUrl));
        shareDialog.show();
//        final AlertDialog.Builder callDialog = new AlertDialog.Builder(context);
//        callDialog.setTitle(context.getString(R.string.share));
//        final String [] callItems = new String[]{context.getString(R.string.share_friend), context.getString(R.string.share_moments)};
//        callDialog.setItems(callItems, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                WXShareUtils.getInstance(context).share(which + 1, resID, title, content, shareUrl);
//            }
//        });
//        AlertDialog dialog = callDialog.create();
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
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
        } else if (utl.lastIndexOf("?") != utl.length() - 1) {
            result += "&";
        }
        return result;
    }
}
