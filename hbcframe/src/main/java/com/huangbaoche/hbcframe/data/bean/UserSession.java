package com.huangbaoche.hbcframe.data.bean;

import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.SharedPre;

/**
 * 用户单例
 * Created by admin on 2016/2/29.
 */
public class UserSession {


    private static UserSession user = new UserSession();

    private String accessKey;//="85862151eaed9bbc8b94373243e687cf";//token
    private String userToken;//="94b561a28ecbcf49c42fb9abe7746663";//userToken


    private UserSession() {
    }

    public static UserSession getUser() {
        if (user == null) {
            user = new UserSession();
        }
        return user;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getAccessKey(Context activity) {
        if (TextUtils.isEmpty(accessKey)) {
            SharedPre shared = new SharedPre(activity);
            accessKey = shared.getStringValue(SharedPre.ACCESSKEY);
        }
        return accessKey;
    }

    public void setAccessKey(Context active, String accessKey) {
        SharedPre shared = new SharedPre(active);
        shared.saveStringValue(SharedPre.ACCESSKEY, accessKey);
        this.accessKey = accessKey;
    }
    public String getUserToken() {
        return userToken;
    }

    public String getUserToken(Context activity) {
        MLog.e("getUserToken Context = "+activity);
        if (TextUtils.isEmpty(userToken)) {
            SharedPre shared = new SharedPre(activity);
            userToken = shared.getStringValue(SharedPre.USERTOKEN);
        }
        return userToken;
    }

    public void setUserToken(Context active,String userToken) {
        SharedPre shared = new SharedPre(active);
        shared.saveStringValue(SharedPre.USERTOKEN, userToken);
        this.userToken = userToken;
    }


}
