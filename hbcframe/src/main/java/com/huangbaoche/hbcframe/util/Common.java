package com.huangbaoche.hbcframe.util;

import android.content.Context;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by admin on 2016/3/27.
 */
public class Common {

    /**
     * MD5运算
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
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }
    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    /**
     * MD5
     * @param b byte数组
     * @return String byte数组处理后字符串
     */
    public static String toHexString(byte[] b) {//String to  byte
        StringBuilder sb = new StringBuilder(b.length * 2);
        for(int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }
    /**
     * 获取 keystore 的密码
     * @param context
     * @return
     */
    public static String getKeyStorePsw(Context context){
        String getSign = JNIUtil.getSign(context);
        String md5 = md5(getSign);
        return  md5;
    }

    /**
     * 获取客户端私钥P12的密钥
     * @param context
     * @return
     */
    public static String getClientP12Key(Context context){
        String md5 = getKeyStorePsw(context);
        StringBuffer deskey = new StringBuffer();
        for(int i=2;i<md5.length()&&deskey.length()<6;i+=4){
            deskey.append(md5.charAt(i));
        }
        return deskey.toString();
    }
}
