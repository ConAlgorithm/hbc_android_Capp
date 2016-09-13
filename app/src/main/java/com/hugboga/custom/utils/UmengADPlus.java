package com.hugboga.custom.utils;

/**
 * Created on 16/9/13.
 */


import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UmengADPlus {
    private static final Pattern pattern = Pattern.compile("UTDID\">([^<]+)");
    private Context mContext = null;
    private String mAppkey = null;

    /**
     * * send message To Server * * @param context * android Context. * @param*
     * appkey * umeng appkey
     *
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("deprecation")
    public void sendMessage(Context context, String appkey) {
        mContext = context;
        mAppkey = appkey;
        new Thread(mNetrequest).start();
    }

    private Runnable mNetrequest = new Runnable() {

        @Override
        public void run() {
            HttpURLConnection conn = null;
            try {
                StringBuilder sber = new StringBuilder();
                sber.append("https://ar.umeng.com/stat.htm?");
                if (!TextUtils.isEmpty(mAppkey)) {
                    sber.append("ak=").append(mAppkey);
                }

                String devicename = Build.MODEL;

                if (!TextUtils.isEmpty(devicename)) {
                    sber.append("&device_name=").append(URLEncoder.encode(devicename, "UTF-8"));
                }

                TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                PackageManager pm = mContext.getPackageManager();

                if (pm.checkPermission(permission.READ_PHONE_STATE,
                        mContext.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                    String imei = tm.getDeviceId();

                    if (!TextUtils.isEmpty(imei)) {
                        sber.append("&imei=").append(URLEncoder.encode(imei, "UTF-8"));
                    }
                }

                if (pm.checkPermission(permission.ACCESS_WIFI_STATE,
                        mContext.getPackageName()) == PackageManager.PERMISSION_GRANTED) {

                    WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = wifi.getConnectionInfo();
                    String mac = info.getMacAddress();

                    // String mac = tm.getDeviceId();
                    if (!TextUtils.isEmpty(mac)) {
                        sber.append("&mac=").append(URLEncoder.encode(mac, "UTF-8"));
                    }
                }

                String androidId = Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
                if (!TextUtils.isEmpty(androidId)) {
                    sber.append("&android_id=").append(URLEncoder.encode(androidId, "UTF-8"));
                }

                String utdid = getUTDid(mContext);
                if (!TextUtils.isEmpty(utdid)) {
                    sber.append("&utdid=").append(URLEncoder.encode(utdid, "UTF-8"));
                }

                conn = (HttpURLConnection) new URL(sber.toString()).openConnection();

                conn.setRequestMethod("GET");
                conn.setConnectTimeout(30 * 1000);
                conn.setReadTimeout(30 * 1000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
                    System.setProperty("http.keepAlive", "false");
                }
                int code = conn.getResponseCode();
                if (code == 200) {
                    InputStream inputStream = conn.getInputStream();

                    // do something
                } else {
                    Log.e("test", "status code: " + code);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

        }
    };

    /**
     * * get UTDID * * @param context * @return
     */
    private String getUTDid(Context context) {
        try {
            Class<?> utdevice = Class.forName("com.ut.device.UTDevice");
            Method reMethod = utdevice.getMethod("getUtdid", Context.class);
            return (String) reMethod.invoke(null, context);
        } catch (Exception e) {
            return readNativeFile(context);
        }
    }

    // get mac

    /**
     * * read native file method * * @param context * @return
     */
    private String readNativeFile(Context context) {
        try {
            File f = getFile(context);
            if (f == null || !f.exists()) {
                return null;
            }

            FileInputStream fis = new FileInputStream(f);
            try {
                return parseId(readStreamToString(fis));
            } finally {
                safeClose(fis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * * java pattern parse native utdid * * @param content * @return
     */
    private String parseId(String content) {
        if (content == null) {
            return null;
        }
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * read native file * * @param context * @return
     */

    private File getFile(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm.checkPermission(permission.WRITE_EXTERNAL_STORAGE,
                context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            try {
                return new File(sdCardDir.getCanonicalPath(), ".UTSystemConfig/Global/Alvin2.xml");
            } catch (Exception ignore) {
            }
        }
        return null;
    }

    /**
     * * convert inputstream to String * * @param input * @return * @throws
     * IOException
     */
    private String readStreamToString(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input);
        char[] buffer = new char[1024];
        int n = 0;
        StringWriter writer = new StringWriter();
        while (-1 != (n = reader.read(buffer))) {
            writer.write(buffer, 0, n);

        }
        return writer.toString();
    }

    /**
     * * close io stream * * @param is
     */
    private void safeClose(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception e) {

            }
        }
    }
}