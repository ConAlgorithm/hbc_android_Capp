package com.hugboga.custom.data.bean;

import android.content.Context;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZHZEPHI on 2015/7/22.
 */
public class UserBean implements IBaseBean {

    public String avatar;//头像
    public String nickname;//昵称
    public String gender;//性别
    public int ageType;//年龄
    public String signature;//签名
    public String userID;//用户ID
    public String areaCode;//区号
    public String mobile;//手机
    public String userToken;//用户身份凭证
    public boolean weakPassword;  //布尔值 是否弱密码
    public String weakPasswordMsg; //弱密码提示文案
    public String imToken;//聊天token


    public String getGenderStr(){git 
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
        UserEntity.getUser().setAreaCode(context, this.areaCode);
        UserEntity.getUser().setImToken(context, imToken);
    }
}
