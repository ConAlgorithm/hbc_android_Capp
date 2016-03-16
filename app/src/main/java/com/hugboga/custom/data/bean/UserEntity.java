package com.hugboga.custom.data.bean;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.hugboga.custom.utils.SharedPre;


public class UserEntity {
    private static UserEntity user = new UserEntity();

    private String accessKey;//="85862151eaed9bbc8b94373243e687cf";//token
    private String userId;//="14993482136";//userid
    private String userToken;//="94b561a28ecbcf49c42fb9abe7746663";//userToken
    private String phone; //手机号
    private String code; //区号
    private String avatar; //头像
    private String nickname; //昵称
    private String version; //记录版本号
    private Integer orderPoint; //订单IM消息数
    private Boolean isNewVersion; //是否有新版本
    private boolean hasNewCoupon; //是否有新优惠券
    public boolean weakPassword;  //布尔值 是否弱密码
    public String weakPasswordMsg; //弱密码提示文案
    public String imToken;//聊天token

    private UserEntity() {
    }


    public static UserEntity getUser() {
        if (user == null) {
            user = new UserEntity();
        }
        return user;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserId(Context active, String userId) {
        SharedPre shared = new SharedPre(active);
        shared.saveStringValue(SharedPre.USERID, userId);
        this.userId = userId;
    }

    public String getUserId(Context activity) {
        if (TextUtils.isEmpty(userId)) {
            SharedPre shared = new SharedPre(activity);
            userId = shared.getStringValue(SharedPre.USERID);
        }
        return userId;
    }
    public String getUserToken(Context activity) {
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
    public void setNickname(Context active, String nickname) {
        SharedPre shared = new SharedPre(active);
        shared.saveStringValue(SharedPre.NICKNAME, nickname);
        this.nickname = nickname;
    }

    public String getNickname(Context activity) {
        if (nickname == null) {
            SharedPre shared = new SharedPre(activity);
            nickname = shared.getStringValue(SharedPre.NICKNAME);
        }
        return nickname;
    }

    public void setAvatar(Context active, String avatar) {
        SharedPre shared = new SharedPre(active);
        shared.saveStringValue(SharedPre.USERAVATAR, avatar);
        this.avatar = avatar;
    }

    public String getAvatar(Context activity) {
        if (avatar == null) {
            SharedPre shared = new SharedPre(activity);
            avatar = shared.getStringValue(SharedPre.USERAVATAR);
        }
        return avatar;
    }

    public void setPhone(Context active, String phone) {
        SharedPre shared = new SharedPre(active);
        shared.saveStringValue(SharedPre.PHONE, phone);
        this.phone = phone;
    }

    public String getPhone(Context activity) {
        if (phone == null || phone.isEmpty()) {
            SharedPre shared = new SharedPre(activity);
            phone = shared.getStringValue(SharedPre.PHONE);
        }
        return phone;
    }

    public Integer getOrderPoint(Context activity) {
        if (phone == null || phone.isEmpty()) {
            SharedPre shared = new SharedPre(activity);
            orderPoint = shared.getIntValue(SharedPre.ORDER_POINT_NUM);
        }
        return orderPoint;
    }

    public void setOrderPoint(Context active, Integer orderPoint) {
        SharedPre shared = new SharedPre(active);
        shared.saveIntValue(SharedPre.ORDER_POINT_NUM, orderPoint);
        this.orderPoint = orderPoint;
    }

    public String getCode(Context activity) {
        if (code == null) {
            SharedPre shared = new SharedPre(activity);
            code = shared.getStringValue(SharedPre.CODE);
        }
        return code;
    }

    public void setCode(Context activity, String code) {
        SharedPre shared = new SharedPre(activity);
        shared.saveStringValue(SharedPre.CODE, code);
        this.code = code;
    }

    public String getVersion(Context activity) {
        if (version == null) {
            SharedPre shared = new SharedPre(activity);
            version = shared.getStringValue(SharedPre.VERSION);
        }
        return version;
    }

    public void setVersion(Context activity, String version) {
        SharedPre shared = new SharedPre(activity);
        shared.saveStringValue(SharedPre.VERSION, version);
        this.version = version;
    }

    public Boolean getIsNewVersion(Context activity) {
        if (isNewVersion == null) {
            SharedPre shared = new SharedPre(activity);
            isNewVersion = shared.getBooleanValue(SharedPre.IS_NEW_VERSION);
        }
        return isNewVersion;
    }

    public void setIsNewVersion(Context activity, Boolean isNewVersion) {
        SharedPre shared = new SharedPre(activity);
        shared.saveBooleanValue(SharedPre.IS_NEW_VERSION, isNewVersion);
        this.isNewVersion = isNewVersion;
    }

    public boolean isLogin(Activity activity) {
        return !TextUtils.isEmpty(getUserId(activity));
    }

    public boolean hasNewCoupon() {
        return hasNewCoupon;
    }

    public void setHasNewCoupon(boolean hasNewCoupon) {
        this.hasNewCoupon = hasNewCoupon;
    }

    public String getWeakPasswordMsg(Context activity) {
        SharedPre shared = new SharedPre(activity);
        weakPasswordMsg = shared.getStringValue(SharedPre.WEAK_PSW_MESSAGE);
        return weakPasswordMsg;
    }

    public void setWeakPasswordMsg(Context activity,String weakPasswordMsg) {
        SharedPre shared = new SharedPre(activity);
         shared.saveStringValue(SharedPre.WEAK_PSW_MESSAGE, weakPasswordMsg);
        this.weakPasswordMsg = weakPasswordMsg;
    }

    public boolean isWeakPassword(Context activity) {
        SharedPre shared = new SharedPre(activity);
        weakPassword = shared.getBooleanValue(SharedPre.IS_WEAK_PSW);
        return weakPassword;
    }

    public void setWeakPassword(Context activity,boolean weakPassword) {
        SharedPre shared = new SharedPre(activity);
        shared.saveBooleanValue(SharedPre.IS_WEAK_PSW, weakPassword);
        this.weakPassword = weakPassword;
    }

    public String getImToken(Context activity) {
        SharedPre shared = new SharedPre(activity);
        imToken = shared.getStringValue(SharedPre.IM_TOKEN);
        return imToken;
    }

    public void setImToken(Context activity,String imToken) {
        SharedPre shared = new SharedPre(activity);
        shared.saveStringValue(SharedPre.IM_TOKEN, imToken);
        this.imToken = imToken;
    }

    public void clean(Activity activity) {
        SharedPre shared = new SharedPre(activity);
        shared.clean();
        userId = null;
        accessKey = null;
        avatar = null;
        nickname = null;
        weakPassword = false;
    }

}
