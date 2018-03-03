package com.hugboga.custom.data.bean;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.hugboga.custom.utils.SharedPre;


public class UserEntity {
    private static UserEntity user = new UserEntity();

    private String accessKey;//="85862151eaed9bbc8b94373243e687cf";//token
    private String userId;//="14993482136";//userid
    private String userToken;//="94b561a28ecbcf49c42fb9abe7746663";//userToken
    private String phone; //手机号
    private String code; //区号
    private String loginPhone; //用于登录手机号
    private String loginCode; //用于登录区号
    private String avatar; //头像
    private String nickname; //昵称
    private String version; //记录版本号
    private Integer orderPoint; //订单IM消息数
    private Boolean isNewVersion; //是否有新版本
    private boolean hasNewCoupon; //是否有新优惠券
    public boolean weakPassword;  //布尔值 是否弱密码
    public String weakPasswordMsg; //弱密码提示文案
    private String imToken;//聊天token
    private String unionid;
    private int travelFund;//旅游基金
    private int coupons;//优惠卷
    private boolean needInitPwd;//是否需要设置密码 仅当返回true时需要
    public String gender;//性别
    public String ageType;//年龄
    public double longitude;//经度
    public double latitude;//维度
    public int userType;//用户类型 101：普通用户 201：代理用户

    private String rimUserId;
    private String nimUserId;
    private String nimUserToken;

    //begin 评价返现活动
    public Integer backFlag = 0;
    public Integer contentCnt = 50;
    public Integer imageCnt = 1;
    public Integer money = 50;
    public String activityImgUrl="";
    public String activityUrl ="";
    //end

    public String getUserName(Context activity) {
//        if (userName == null) {
            SharedPre shared = new SharedPre(activity);
            userName = shared.getStringValue(SharedPre.USERNAME);
//        }
        return userName;
    }

    public void setUserName(Context activity,String userName) {
        SharedPre shared = new SharedPre(activity);
        shared.saveStringValue(SharedPre.USERNAME, userName);
        this.userName = userName;
    }

    private String userName;//真实姓名

    private UserEntity() {
    }


    public static UserEntity getUser() {
        if (user == null) {
            user = new UserEntity();
        }
        return user;
    }

    public void setTravelFund(Context active, int travelFund) {
        SharedPre shared = new SharedPre(active);
        shared.saveIntValue(SharedPre.TRAVELFUND, travelFund);
        this.travelFund = travelFund;
    }

    public int getTravelFund(Context activity) {
        if (travelFund == 0) {
            try {
                SharedPre shared = new SharedPre(activity);
                travelFund = shared.getIntValue(SharedPre.TRAVELFUND);
            } catch (Exception e) {
                travelFund = 0;
            }
        }
        return travelFund;
    }

    public void setCoupons(Context active, int coupons) {
        SharedPre shared = new SharedPre(active);
        shared.saveIntValue(SharedPre.COUPONS, coupons);
        this.coupons = coupons;
    }

    public int getCoupons(Context activity) {
        if (coupons == 0) {
            try {
                SharedPre shared = new SharedPre(activity);
                coupons = shared.getIntValue(SharedPre.COUPONS);
            } catch (Exception e) {
                coupons = 0;
            }
        }
        return coupons;
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

    public void setUserToken(Context active, String userToken) {
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

    public void setPhone(Context activity, String phone) {
        SharedPre shared = new SharedPre(activity);
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

    public void setGender(Context active, String gender) {
        SharedPre shared = new SharedPre(active);
        shared.saveStringValue(SharedPre.GENDER, gender);
        this.gender = gender;
    }

    public String getGender(Context activity) {
        if (gender == null) {
            SharedPre shared = new SharedPre(activity);
            gender = shared.getStringValue(SharedPre.GENDER);
        }
        return gender;
    }

    public void setAgeType(Context active, String ageType) {
        SharedPre shared = new SharedPre(active);
        shared.saveStringValue(SharedPre.AGETYPE, ageType);
        this.ageType = ageType;
    }


    public String getAgeType(Context activity) {
        if (ageType == null) {
            SharedPre shared = new SharedPre(activity);
            ageType = shared.getStringValue(SharedPre.AGETYPE);
        }
        return ageType;
    }

    public void setNeedInitPwd(Context context, boolean setNeedInitPwd){
        SharedPre sharedPre = new SharedPre(context);
        sharedPre.saveBooleanValue(SharedPre.NEEDINITPWD,setNeedInitPwd);
        this.needInitPwd = setNeedInitPwd;
    }

    public boolean getNeedInitPwd(Context activity) {
        SharedPre shared = new SharedPre(activity);
        needInitPwd = shared.getBooleanValue(SharedPre.NEEDINITPWD);
        return needInitPwd;
    }
    public String getLoginPhone(Context activity) {
        if (TextUtils.isEmpty(loginPhone)) {
            SharedPre shared = new SharedPre(activity);
            loginPhone = shared.getStringValue(SharedPre.LOGIN_PHONE);
        }
        return loginPhone;
    }

    public void setLoginPhone(Context active, String loginPhone) {
        SharedPre shared = new SharedPre(active);
        shared.saveStringValue(SharedPre.LOGIN_PHONE, phone);
        this.loginPhone = loginPhone;
    }


    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    public String getAreaCode(Context activity) {
        if (code == null) {
            SharedPre shared = new SharedPre(activity);
            code = shared.getStringValue(SharedPre.CODE);
        }
        return code;
    }

    public void setAreaCode(Context activity, String code) {
        if (!TextUtils.isEmpty(code)) {
            SharedPre shared = new SharedPre(activity);
            shared.saveStringValue(SharedPre.CODE, code);
            this.code = code;
        }
    }

    public void setLoginAreaCode(Context activity, String loginCode) {
        SharedPre shared = new SharedPre(activity);
        shared.saveStringValue(SharedPre.LOGIN_CODE, loginCode);
        this.loginCode = loginCode;
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
        return !TextUtils.isEmpty(UserSession.getUser().getUserToken(activity));
    }

    public boolean isLogin(Context context) {
        return !TextUtils.isEmpty(UserSession.getUser().getUserToken(context));
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

    public void setWeakPasswordMsg(Context activity, String weakPasswordMsg) {
        SharedPre shared = new SharedPre(activity);
        shared.saveStringValue(SharedPre.WEAK_PSW_MESSAGE, weakPasswordMsg);
        this.weakPasswordMsg = weakPasswordMsg;
    }

    public boolean isWeakPassword(Context activity) {
        SharedPre shared = new SharedPre(activity);
        weakPassword = shared.getBooleanValue(SharedPre.IS_WEAK_PSW);
        return weakPassword;
    }

    public void setWeakPassword(Context activity, boolean weakPassword) {
        SharedPre shared = new SharedPre(activity);
        shared.saveBooleanValue(SharedPre.IS_WEAK_PSW, weakPassword);
        this.weakPassword = weakPassword;
    }

    public String getImToken(Context activity) {
        SharedPre shared = new SharedPre(activity);
        imToken = shared.getStringValue(SharedPre.IM_TOKEN);
        return imToken;
    }

//    public void setImToken(Context activity, String imToken) {
//        if (!TextUtils.isEmpty(imToken)) {
//            SharedPre shared = new SharedPre(activity);
//            shared.saveStringValue(SharedPre.IM_TOKEN, imToken);
//            this.imToken = imToken;
//        }
//    }

    public String getRimUserId(Context activity) {
        SharedPre shared = new SharedPre(activity);
        rimUserId = shared.getStringValue(SharedPre.RIM_USERID);
        return rimUserId;
    }

    public void setRimUserId(Context activity,String rimUserId) {
        if (!TextUtils.isEmpty(rimUserId)) {
            SharedPre shared = new SharedPre(activity);
            shared.saveStringValue(SharedPre.RIM_USERID, rimUserId);
            this.rimUserId = rimUserId;
        }
    }

    public boolean isProxyUser(Context activity) {//是否是代理用户
        SharedPre shared = new SharedPre(activity);
        userType = shared.getIntValue(SharedPre.USER_TYPE);
        return userType == 201;
    }

    public Integer getUserType(Context activity) {
        SharedPre shared = new SharedPre(activity);
        userType = shared.getIntValue(SharedPre.USER_TYPE);
        return userType;
    }

    public void setUserType(Context activity, Integer userType) {
        if (userType != 0) {
            SharedPre shared = new SharedPre(activity);
            shared.saveIntValue(SharedPre.USER_TYPE, userType);
            this.userType = userType;
        }
    }

    public String getNimUserId(Context activity) {
        SharedPre shared = new SharedPre(activity);
        nimUserId = shared.getStringValue(SharedPre.NIM_USERID);
        return nimUserId;
    }

    public void setNimUserId(Context context,String nimUserId) {
        if (!TextUtils.isEmpty(nimUserId)) {
            SharedPre shared = new SharedPre(context);
            shared.saveStringValue(SharedPre.NIM_USERID, nimUserId);
            this.nimUserId = nimUserId;
        }
    }

    public String getNimUserToken(Context context) {
        SharedPre shared = new SharedPre(context);
        nimUserToken = shared.getStringValue(SharedPre.NIM_TOKEN);
        return nimUserToken;
    }

    public void setNimUserToken(Context context,String nimUserToken) {
        if (!TextUtils.isEmpty(nimUserToken)) {
            SharedPre shared = new SharedPre(context);
            shared.saveStringValue(SharedPre.NIM_TOKEN, nimUserToken);
            this.nimUserToken = nimUserToken;
        }
    }

    public String getUnionid(Context activity) {
        SharedPre shared = new SharedPre(activity);
        unionid = shared.getStringValue(SharedPre.UNIONID);
        return unionid;
    }

    public void setUnionid(Context activity, String unionid) {
        SharedPre shared = new SharedPre(activity);
        shared.saveStringValue(SharedPre.UNIONID, unionid);
        this.unionid = unionid;
    }

    public void clean(Activity activity) {
        SharedPre shared = new SharedPre(activity);
        shared.clean();
        UserSession.getUser().setUserToken(activity,null);
        userId = null;
        accessKey = null;
        avatar = null;
        nickname = null;
        weakPassword = false;
        coupons = 0;
        travelFund = 0;
    }
    public void setEvaluateReTurnMoneyBackFlag(Context activity, Integer backFlag) {
        SharedPre shared = new SharedPre(activity);
        shared.saveIntValue("backFlag",backFlag);
        this.backFlag = backFlag;
    }
    public void setEvaluateReTurnMoneyContentCnt(Context activity, Integer contentCnt) {
        SharedPre shared = new SharedPre(activity);
        shared.saveIntValue("contentCnt",contentCnt);
        this.contentCnt = contentCnt;
    }
    public void setEvaluateReTurnMoneyImageCnt(Context activity, Integer imageCnt) {
        SharedPre shared = new SharedPre(activity);
        shared.saveIntValue("imageCnt",imageCnt);
        this.imageCnt = imageCnt;
    }
    public void setEvaluateReTurnMoney(Context activity, Integer money) {
        SharedPre shared = new SharedPre(activity);
        shared.saveIntValue("money",money);
        this.money = money;
    }
    public void setEvaluateReTurnMoneyActivityUrl(Context activity, String activityUrl) {
        SharedPre shared = new SharedPre(activity);
        shared.saveStringValue("activityUrl",activityUrl);
        this.activityUrl = activityUrl;
    }
    public void setEvaluateReTurnMoneyImageUrl(Context activity, String activityImgUrl) {
        SharedPre shared = new SharedPre(activity);
        shared.saveStringValue("money",activityImgUrl);
        this.activityImgUrl = activityImgUrl;
    }
}
