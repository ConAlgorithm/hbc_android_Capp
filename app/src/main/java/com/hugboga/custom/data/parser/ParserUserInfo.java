package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.UserBean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ParserUserInfo extends ImplParser {
    @Override
    public UserBean parseObject(JSONObject jsonObj) throws Throwable {
        UserBean userBean = new UserBean();
        userBean.avatar = jsonObj.optString("avatar");
        userBean.nickname = jsonObj.optString("nickName");
        userBean.gender = jsonObj.optString("gender");
        userBean.name = jsonObj.optString("name");
        userBean.ageType = jsonObj.optInt("ageType", -1);
        userBean.signature = jsonObj.optString("signature");
        userBean.userID = jsonObj.optString("userId");
        userBean.areaCode = jsonObj.optString("areaCode");
        userBean.mobile = jsonObj.optString("mobile");
        userBean.userToken = jsonObj.optString("userToken");
        userBean.weakPassword = jsonObj.optBoolean("weakPassword");
        userBean.weakPasswordMsg = jsonObj.optString("weakPasswordMsg");
        userBean.travelFund = jsonObj.optInt("travelFund", 0);
        userBean.coupons = jsonObj.optInt("coupons", 0);
        userBean.needInitPwd = jsonObj.optBoolean("needInitPwd",false);
        userBean.bannerUrl = jsonObj.optString("bannerUrl");
        userBean.userType = jsonObj.optInt("userType");
        return userBean;
    }
}
