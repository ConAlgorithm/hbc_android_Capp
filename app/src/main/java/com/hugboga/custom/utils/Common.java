package com.hugboga.custom.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.TypedValue;

import com.huangbaoche.hbcframe.util.MLog;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class Common {


    private static String CHANNEL_FILENAME = "source.txt";


    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());
    }

    /**
     * 将流转成字节数组
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream2Byte(InputStream is) throws IOException {
        if (is == null)
            return null;
        byte[] mByte;
        // RandomAccessFile baos = new RandomAccessFile(file, "rw");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = is.read(buffer, 0, 1024)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
            mByte = baos.toByteArray();

        } finally {
            baos.close();
            baos = null;
            is.close();
            is = null;
        }
        return mByte;
    }

    /**
     * 将流转成字节数组
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String readInputStream(InputStream is) throws IOException {
        if (is == null)
            return null;
        String mStr;
        byte[] mByte;
        mByte = readInputStream2Byte(is);
        mStr = new String(mByte, "UTF-8");
        mByte = null;
        return mStr;
    }

    /**
     * 得到相应的网络信息,包括记录的头信息
     *
     * @param urlPath
     * @return
     * @throws IOException
     */
    public static byte[] getConnection(Context context, String urlPath)
            throws IOException {
        byte[] data = null;
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setRequestProperty("Connection", "close");
//		conn.setRequestProperty("Connection", "keep-alive");
        // setHttpHeader(conn, context);
        conn.connect();
        data = readInputStream2Byte(conn.getInputStream());
        conn.disconnect();
        return data;
    }

    public static void saveFileToLocal(byte[] data, String filePath,
                                       String fileName) {
        File pathFile = new File(filePath);
        File imageFile = new File(filePath, fileName);
        imageFile.delete();
        try {
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            imageFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            outputStream.write(data);
            outputStream.close();
            outputStream = null;
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    /**
     * 从assets得到渠道ID(chid)
     *
     * @return 存储结果 longal 2012-05-23
     */
    public static String readAssetsChId(Context context) {
        String chid = "";
        try {
            chid = readInputStream(context.getResources().getAssets().open(CHANNEL_FILENAME));
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return chid;
    }

    /**
     * @param mContext 上下文环境
     * @param name     资源中的 名字
     * @param dir      路径 名字  如：layout 或 drawable
     * @return int 参数 在 R 中的 id,Returns 0 if flightNo such resource was found.
     * @Title getIdByName
     * @Description 通过 传入 资源中 的名字 得到 id，相当于 mContext.findViewById(R.dir.name);
     * @author aceway-liwei
     * @date 2012-10-10 下午02:36:54
     */
    public static int getIdByName(Context mContext, String dir, String name) {
        return mContext.getResources().getIdentifier(name, dir, mContext.getPackageName());
    }

    /**
     * @param mContext 上下文环境
     * @param name     layout的 名字
     * @return int 参数 在 R 中的 id ,Returns 0 if flightNo such resource was found.
     * @Title getIdFormLayout
     * @Description 通过 传入 layout 的名字 得到 id，相当于 mContext.findViewById(R.layout.name);
     * @author aceway-liwei
     * @date 2012-10-10 下午02:36:54
     */
    public static int getIdFormLayout(Context mContext, String name) {
        return getIdByName(mContext, "layout", name);
    }

    /**
     * @param mContext 上下文环境
     * @param name     drawable的 名字
     * @return int 参数 在 R 中的 id,Returns 0 if flightNo such resource was found.
     * @Title getIdFormDraw
     * @Description 通过 传入 drawable 的名字 得到 id，相当于 mContext.findViewById(R.drawable.name);
     * @author aceway-liwei
     * @date 2012-10-10 下午02:36:54
     */
    public static int getIdFormDraw(Context mContext, String name) {
        return getIdByName(mContext, "drawable", name);
    }

    /**
     * @param mContext 上下文环境
     * @param name     在布局中的 名字
     * @return int 参数 在 R 中的 id,Returns 0 if flightNo such resource was found.
     * @Title getIdFormView
     * @Description 通过 传入 view 的名字 得到 id，相当于 mContext.findViewById(R.id.name);
     * @author aceway-liwei
     * @date 2012-10-10 下午02:36:54
     */
    public static int getIdFormView(Context mContext, String name) {
        return getIdByName(mContext, "id", name);
    }

    /**
     * @param mContext 上下文环境
     * @param name     在布局中的 名字
     * @return int 参数 在 R 中的 id,Returns 0 if flightNo such resource was found.
     * @Title getIdFormAnim
     * @Description 通过 传入 anim 的名字 得到 id，R.anim.rotate_in  ;
     * @author aceway-liwei
     * @date 2012-10-10 下午02:36:54
     */
    public static int getIdFormAnim(Context mContext, String name) {
        return getIdByName(mContext, "anim", name);
    }

    public static int StringToInt(String str) {
        int n = 0;
        if (null != str && !str.equals("")) {
            try {
                n = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                MLog.e(e.toString());
            }
        }
        return n;
    }

    public static Float StringToFloat(String str) {
        Float f = (float) 0;
        if (null != str && !str.equals("")) {
            try {
                f = Float.parseFloat(str);
            } catch (Exception e) {
                MLog.e(e.toString());
            }
        }
        return f;
    }

    /**
     * MD5运算
     *
     * @param s 传入明文
     * @return String 返回密文
     */
    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * MD5
     *
     * @param b byte数组
     * @return String byte数组处理后字符串
     */
    public static String toHexString(byte[] b) {//String to  byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    /**
     * @param urlPath 文件的 地址
     * @return int  文件在服务器上的大小
     * @Title getFileSizeByUrl
     * @Description 通过url地址，得到文件的大小，只是通过连接并获得流大小，不会下载文件，所以耗时在200ms左右，具体跟网速也有关
     * @author aceway-liwei
     * @date 2012-10-18 下午05:44:17
     */
    public static int getFileSizeByUrl(String urlPath) {
        int size = 0;
        URL url;
        try {
            url = new URL(urlPath);

            HttpURLConnection httpConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置 User-Agent
            httpConnection
                    .setRequestProperty("User-Agent", "Internet Explorer");
            httpConnection.connect();
            size = httpConnection.getContentLength();
            httpConnection.disconnect();
            httpConnection = null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * @param urlPath 文件地址
     * @return String    返回类型
     * @Title getFileLastModifiedByUrl
     * @Description 通过URL地址得到服务器上文件的最后修改时间
     * @author aceway-liwei
     * @date 2012-10-18 下午05:48:11
     */
    public static String getFileLastModifiedByUrl(String urlPath) {
        String time = "";
        URL url;
        try {
            url = new URL(urlPath);
            HttpURLConnection httpConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置 User-Agent
            httpConnection
                    .setRequestProperty("User-Agent", "Internet Explorer");
            httpConnection.connect();
            time = httpConnection.getHeaderField("Last-Modified");
            httpConnection.disconnect();
            httpConnection = null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * @param context
     * @param key
     * @return String    返回类型
     * @Title getConfigParams
     * @Description 通过Key 得到配置文档里的参数
     * @author aceway-liwei
     * @date 2013-2-21 上午10:28:02
     */
    public static String getConfigParams(Context context, String key) {
        ApplicationInfo appi = null;
        String value;
        try {
            appi = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = appi.metaData;
            if (bundle != null) {
                value = bundle.getString(key);
                if (value != null)
                    return value;
            }
            throw new RuntimeException(
                    "请检查" + key + " 在Manifest文件中是否配置正确");

        } catch (Exception e) {
            MLog.e(e.getMessage());
//			e.printStackTrace();
        }
        return "";
    }

    /**
     * @param context
     * @param uri
     * @return File    返回类型
     * @Title getFileByUri
     * @Description 通过uri  获取 file
     * @author aceway-liwei
     * @date 2013-9-5 上午11:25:40
     */
    public static File getFileByUri(Activity context, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = context.managedQuery(uri, proj, null, null, null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        File file = new File(img_path);
        return file;
    }

    public static void installAPK(File file, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        //intent.addFlags(Intent.flag_ac);
        activity.startActivity(intent);
    }

    public static void startCalendar(Activity activity) {
//		Intent intent = new Intent();
//		intent.setComponent(new ComponentName("com.android.calendar",
//				"com.android.calendar.LaunchActivity"));
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        activity.startActivity(intent);
    }

    /**
     * 获取 keystore 的密码
     *
     * @param context
     * @return
     */
    public static String getKeyStorePsw(Context context) {
        String getSign = JNIUtil.getSign(context);
        String md5 = md5(getSign);
        return md5;
    }

    /**
     * 获取客户端私钥P12的密钥
     *
     * @param context
     * @return
     */
    public static String getClientP12Key(Context context) {
        String md5 = getKeyStorePsw(context);
        StringBuffer deskey = new StringBuffer();
        for (int i = 2; i < md5.length() && deskey.length() < 6; i += 4) {
            deskey.append(md5.charAt(i));
        }
        return deskey.toString();
    }

    public static String jsonToStr(JSONObject object) {
        if (object == null) return "";
        StringBuffer sb = new StringBuffer();
        Iterator<String> keys = object.keys();
        try {
            if (keys.hasNext()) {
                String key = keys.next();
                String value = URLEncoder.encode(String.valueOf(object.opt(key)), "UTF-8");
                sb.append(key).append("=").append(value);
            }
            while (keys.hasNext()) {
                String key = keys.next();
                String value = URLEncoder.encode(String.valueOf(object.opt(key)), "UTF-8");
                sb.append("&").append(key).append("=").append(value);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String mapToStr(Map map) {
        return mapToStr(map, false);
    }

    /**
     * map 转 string
     *
     * @param map          数据源
     * @param containsNull 是否允许null，true 允许Value是null，
     * @return
     */
    public static String mapToStr(Map map, boolean containsNull) {
        if (map == null) return "";
        StringBuffer sb = new StringBuffer();
        Iterator<Entry> iterator = map.entrySet().iterator();
        try {
            boolean isFirst = true;
            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                if (containsNull || entry.getValue() != null) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        sb.append("&");
                    }
                    String value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
                    sb.append(entry.getKey()).append("=").append(value);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}