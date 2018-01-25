package com.hugboga.custom.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.huangbaoche.hbcframe.HbcConfig;
import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.ToastUtils;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.action.ActionController;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.LargerImageActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.widget.CallPhoneDialog;
import com.hugboga.custom.widget.CsDialog;
import com.hugboga.custom.widget.ShareDialog;
import com.hugboga.custom.widget.UpPicDialog;
import com.ishumei.smantifraud.SmAntiFraud;


import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.tongdun.android.shell.FMAgent;


/**
 * Created by qingcha on 16/5/26.
 */
public final class CommonUtils {

    private CommonUtils() {
    }

    public static void showToast(int resId) {
        ToastUtils.showToast(MyApplication.getAppContext(), resId);
    }

    public static void showToast(String msg) {
        ToastUtils.showToast(MyApplication.getAppContext(), msg);
    }

    public static String getCountString(String count) {
        if (TextUtils.isEmpty(count)) {
            return "0";
        }
        try {
            count = Integer.valueOf(count) < 0 ? "0" : count;
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            return 0L;
        }
        return count;
    }

    public static Double getCountDouble(String _count) {
        if (TextUtils.isEmpty(_count)) {
            return 0d;
        }
        double count = 0d;
        try {
            count = Double.valueOf(_count);
        } catch (Exception e) {
            return 0d;
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

    public static void callPhoneDialog(final Context context, final String guideId, final String guidePhone) {
        CallPhoneDialog callPhoneDialog = new CallPhoneDialog(context);
        callPhoneDialog.setParams(guideId, guidePhone);
        callPhoneDialog.show();
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
        } catch (Exception e) {
        }
    }

    public static CsDialog csDialog(final Context context, final OrderBean orderBean
            , final String title, final SkuItemBean skuItemBean, final int sourceType, final String source) {
        CsDialog csDialog = new CsDialog(context);
        csDialog.setParams(new CsDialog.Params(title, sourceType, orderBean, skuItemBean, source));
        csDialog.show();
        return csDialog;
    }

    public static CsDialog csDialog(final Context context, final OrderBean orderBean
            , final String title, final SkuItemBean skuItemBean, final int sourceType, final String source, boolean hasOnLine) {
        CsDialog csDialog = new CsDialog(context, hasOnLine);
        csDialog.setParams(new CsDialog.Params(title, sourceType, orderBean, skuItemBean, source));
        csDialog.show();
        return csDialog;
    }

    public static CsDialog csDialog(final Context context, final OrderBean orderBean
            , final String title, final SkuItemBean skuItemBean, final int sourceType,
                                    final String source, CsDialog.OnCsListener listener) {
        CsDialog csDialog = new CsDialog(context);
        csDialog.setParams(new CsDialog.Params(title, sourceType, orderBean, skuItemBean, source));
        if (listener != null) {
            csDialog.setOnCsListener(listener);
        }
        csDialog.show();
        return csDialog;
    }

    public static CsDialog csDialog(final Context context, final OrderBean orderBean
            , final String title, final SkuItemBean skuItemBean, final int sourceType, final String source, boolean hasOnLine, CsDialog.OnCsListener listener) {
        CsDialog csDialog = new CsDialog(context, hasOnLine);
        csDialog.setParams(new CsDialog.Params(title, sourceType, orderBean, skuItemBean, source));
        if (listener != null) {
            csDialog.setOnCsListener(listener);
        }
        csDialog.show();
        return csDialog;
    }

    public static UpPicDialog uppicDialog(final Context context) {
        UpPicDialog upPicDialog = new UpPicDialog(context);
        upPicDialog.show();
        return upPicDialog;
    }

    public static String getDoubleEncodedString(String str) {
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

    public static String getEncodedString(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String encodedString = null;
        try {
            encodedString = URLEncoder.encode(str, "utf-8");
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
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
            url = url.replaceAll("(" + key + "=[^&]*)", key + "=" + value);
        }
        return url;
    }

    public static String getUrlValue(String url, String key) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
            Map<String, String> map = getUrlValues(url);
            if (map != null && map.containsKey(key)) {
                return map.get(key);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static Map<String, String> getUrlValues(String _url) {
        if (TextUtils.isEmpty(_url)) {
            return null;
        }
        String strAllParam = null;
        String strURL = _url.trim();
        String[] arrSplit = strURL.split("[?]");
        if (strURL.length() > 1 && arrSplit.length > 1 && arrSplit[1] != null) {
            strAllParam = arrSplit[1];
        } else {
            return null;
        }
        if (TextUtils.isEmpty(strAllParam)) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        if (strAllParam.indexOf("&") > -1 && strAllParam.indexOf("=") > -1) {
            String[] arrTemp = strAllParam.split("&");
            for (String str : arrTemp) {
                String[] qs = str.split("=");
                map.put(qs[0], qs[1]);
            }
        } else if (strAllParam.indexOf("=") > -1) {
            String[] qs = strAllParam.split("=");
            map.put(qs[0], qs[1]);
        }
        return map;
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

    public static boolean isLogin(Context context, String source) {
        if (context != null && !UserEntity.getUser().isLogin(context)) {
            CommonUtils.showToast(R.string.login_hint);
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, source);
            context.startActivity(intent);
            return false;
        } else {
            return true;
        }
    }

    public static void showLargerImage(Context context, String url) {
        List<String> imageUrlList = new ArrayList<String>(1);
        imageUrlList.add(url);
        showLargerImages(context, imageUrlList, 0);
    }

    public static void showLargerImages(Context context, List<String> imageUrlList, int position) {
        LargerImageActivity.Params params = new LargerImageActivity.Params();
        params.imageUrlList = imageUrlList;
        params.position = position;
        Intent intent = new Intent(context, LargerImageActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        context.startActivity(intent);
    }

    public static boolean checkTextIsNull(EditText editText) {
        if (editText == null) {
            return true;
        }
        Editable editable = editText.getText();
        if (editable != null && editable.toString() != null && !TextUtils.isEmpty(editable.toString().trim())) {
            return false;
        } else {
            return true;
        }
    }

    public static String removePhoneCodeSign(String phoneCode) {
        String result = "86";
        if (TextUtils.isEmpty(phoneCode) || TextUtils.isEmpty(phoneCode.trim())) {
            return result;
        }
        result = phoneCode.replaceAll(" ", "");
        result = result.replace("+", "");
        if (TextUtils.isEmpty(result)) {
            result = "86";
        }
        return result;
    }

    public static String addPhoneCodeSign(String phoneCode) {
        String result = "+86";
        if (!TextUtils.isEmpty(phoneCode)) {
            if (phoneCode.contains("+")) {
                result = phoneCode.replaceAll(" ", "");
            } else {
                result = "+" + phoneCode.trim();
            }
        }
        return result;
    }

    public static void hideSoftInputMethod(EditText inputText) {
        if (inputText == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) inputText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && inputText != null) {
            imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
        }
    }

    public static void hideSoftInput(Activity activity) {
        if (activity == null || activity.getCurrentFocus() == null || activity.getCurrentFocus().getWindowToken() == null) {
            return;
        }
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据payType返回支付方式
     *
     * @param payType
     * @return
     */
    public static String selectPayStyle(int payType) {
        String result = "";
        switch (payType) {
            case Constants.PAY_STATE_ALIPAY:
                result = "支付宝";
                break;
            case Constants.PAY_STATE_WECHAT:
                result = "微信";
                break;
            case Constants.PAY_STATE_BANK:
                result = "信用卡";
                break;
            default:
                break;
        }
        return result;
    }

    public static void apiErrorShowService(final Context context, ExceptionInfo errorInfo, BaseRequest request, final String source) {
        apiErrorShowService(context, errorInfo, request, source, true);
    }

    static CsDialog csDialog;

    public static void apiErrorShowService(final Context context, ExceptionInfo errorInfo, BaseRequest request, final String source, boolean isShowHint) {
        if (request.errorType == BaseRequest.ERROR_TYPE_PROCESSED) {
            return;
        }
        String errorMessage = ErrorHandler.getErrorMessage(errorInfo, request);
        if (isShowHint) {
            errorMessage += "\n请联系客服，我们会协助您完成预订";
        }
        AlertDialogUtils.showAlertDialog(context, errorMessage, "知道了", "联系客服", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DialogUtil.showServiceDialog(context, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, null, null, source);
                csDialog = CommonUtils.csDialog(context, null, null, null, UnicornServiceActivity.SourceType.TYPE_CHARTERED, source, new CsDialog.OnCsListener() {
                    @Override
                    public void onCs() {
                        csDialog.dismiss();
                    }
                });
                dialog.dismiss();
            }
        });
    }

    /**
     * 启动到应用商店app详情界面
     *
     * @param appPkg    目标App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)) return;

            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getAppContext().startActivity(intent);
        } catch (Exception e) {
            CommonUtils.showToast("您可能没有安装谷歌应用商店");
        }

    }

    public static boolean checkInlandPhoneNumber(String code, String phone) {
        if (("+86".equals(code) || "86".equals(code))) {
            if (phone != null && phone.length() == 11 && phone.startsWith("1")) {
                return true;
            } else {
                CommonUtils.showToast("手机号输入有误");
                return false;
            }
        } else {
            return true;
        }
    }

    public static String doubleTrans(double num) {
        try {
            if (num % 1.0 == 0) {
                return String.valueOf((long) num);
            }
            return String.valueOf(num);
        } catch (Exception e) {
            return "" + num;
        }
    }

    public static String getNum(String text) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim())) {
            return "";
        }
        try {
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(text);
            return m.replaceAll("").trim();
        } catch (Exception e) {
            return text;
        }
    }

    public static void loginDoAction(Context context, ActionBean actionBean) {
        if (actionBean != null && !"1".equals(actionBean.vcid)) {
            ActionController actionFactory = ActionController.getInstance();
            actionFactory.doAction(context, actionBean);
        }
    }

    public static void logout(Context context) {
        if (context instanceof Activity) {
            UserEntity.getUser().clean((Activity) context);
            IMUtil.getInstance().logoutNim();
            EventBus.getDefault().post(new EventAction(EventType.CLICK_USER_LOOUT));
        }
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static String getText(EditText editText, boolean isRemoveAllBlank) {
        String result = editText.getText() != null ? editText.getText().toString() : "";
        if (!TextUtils.isEmpty(result)) {
            if (isRemoveAllBlank) {
                result.replaceAll(" ", "");
            } else {
                result.trim();
            }
        }
        return result;
    }

    /**
     * 根据渠道初始化不同反作弊SDK，android 大渠道（例如：官方渠道）接入数美， 其他小渠道接入同盾科技
     *
     * @return 是否是数美
     */
    public static boolean isAgainstSM() {
        return Constants.CHANNEL_OFFICIAL.equals(MyApplication.getChannelNum());
    }

    public static String getAgainstDeviceId() {
        String deviceId = "";
        if (isAgainstSM()) {
            deviceId = SmAntiFraud.getDeviceId();
        } else {
            deviceId = FMAgent.onEvent(MyApplication.getAppContext());
        }
        return deviceId;
    }

    public static String getString(int id) {
        return MyApplication.getAppContext().getResources().getString(id);
    }

    public static String getString(int id, Object... formatArgs) {
        return MyApplication.getAppContext().getResources().getString(id, formatArgs);
    }

    public static void synCookies(String url, String value) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, value);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(MyApplication.getAppContext());
            CookieSyncManager.getInstance().sync();
        }
    }

    public static void removeAllCookies() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeAllCookie();
        }
    }

    public static void synCookiesArray(String url, String... value) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (value != null) {
            for (String i : value) {
                cookieManager.setCookie(url, i);
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(MyApplication.getAppContext());
            CookieSyncManager.getInstance().sync();
        }
    }

    public static void synDebugCookies(String url) {
        if (HbcConfig.IS_DEBUG) {
            String key1 = "HbcAppAk=" + UserSession.getUser().getAccessKey(MyApplication.getAppContext()) + ";Domain=huangbaoche.com;Path=/";
            synCookiesArray(url, key1);
        }
    }

    /**
     * 是否支持google服务
     */
    public static boolean isSupportGoogleService() {
        try {
            GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
            int result = googleApiAvailability.isGooglePlayServicesAvailable(MyApplication.getAppContext());
            if (result != ConnectionResult.SUCCESS) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 微信分享图片优化
     *
     * @param mActivity
     * @param type      (1分享到微信好友,2分享到微信朋友圈)
     * @param _picUrl
     * @param title
     * @param content
     * @param goUrl
     * @param source    评价来源
     */
    public static void shareOptimize(final Context mActivity, final int type, final String _picUrl, final String title, final String content, final String goUrl, final String source) {
        WXShareUtils wxShareUtils = WXShareUtils.getInstance(mActivity);
        if (!TextUtils.isEmpty(source)) {
            wxShareUtils.source = source;
        }
        String picUrl = Tools.transformImgUrl(100, 100, _picUrl);
        wxShareUtils.share(type, picUrl, title, content, goUrl);
    }
}
