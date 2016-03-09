package com.hugboga.custom.data.bean;

import android.content.Context;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZHZEPHI on 2015/7/22.
 */
public class UserBean implements IBaseBean {

    public String avatar;
    public String nickname;
    public String gender;
    public int ageType;
    public String signature;
    public String userID;
    public String areaCode;
    public String mobile;
    public String userToken;
    public boolean weakPassword;  //布尔值 是否弱密码
    public String weakPasswordMsg; //弱密码提示文案


    public String getGenderStr(){
        switch (gender){
            case "0":
//                return "保密";
                return "";
            case "1":
                return "男";
            case "2":
                return "女";
            default:
//                return "未知";
                return "";
        }
    }

    public String getAgeStr(){
        switch (ageType){
            case 0:
                return "40后";
            case 1:
                return "50后";
            case 2:
                return "60后";
            case 3:
                return "70后";
            case 4:
                return "80后";
            case 5:
                return "90后";
            case 6:
                return "00后";
            case 7:
                return "10后";
            case -1:
                return "未知";
            default:
                return "未知";
        }
    }

    public void setUserEntity(Context context){
        UserEntity.getUser().setUserId(this.userID);
        UserEntity.getUser().setAvatar(context, this.avatar);
        UserEntity.getUser().setNickname(context, this.nickname);
        UserEntity.getUser().setCode(context, this.areaCode);
    }
}
