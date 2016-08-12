package com.hugboga.custom.action;

import android.text.TextUtils;

import com.hugboga.custom.data.net.UrlLibs;

/**
 * Created by qingcha on 16/8/12.
 */
public class ActionUtils {

    public static String getWebUrl(String _url) {
        String result = _url;
        if (TextUtils.isEmpty(_url) && _url.length() <= 2) {
            return result;
        }
        if ('$' == _url.charAt(0)) {
            String type = String.valueOf(_url.charAt(1));
            result = getBaseUrl(type);
        }
        return result;
    }

    public static String getBaseUrl(String type) {
        String result = "";
        if (type.equalsIgnoreCase("a")) {
            result = UrlLibs.SERVER_IP_HOST_PUBLIC;
        } else if (type.equalsIgnoreCase("b")) {
            result = UrlLibs.H5_HOST;
        } else if (type.equalsIgnoreCase("c")) {
            result = UrlLibs.SHARE_BASE_URL_2;
        } else if (type.equalsIgnoreCase("d")) {
            result = UrlLibs.SHARE_BASE_URL_1;
        } else if (type.equalsIgnoreCase("e")) {
            result = UrlLibs.SHARE_BASE_URL_4;
        }
        return result;
    }
}
