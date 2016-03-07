package com.hugboga.custom.utils;


import android.util.Log;

import com.huangbaoche.hbcframe.data.bean.UserEntity;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

/**
 * Created by admin on 2016/3/4.
 */
public class UploadLogs {

    public static void checkAndUpload(){
        StringBuilder stringBuilder = UserEntity.getUser().getLog();
        LogUtil.e("checkAndUpload="+stringBuilder.length()+" UserEntity ="+UserEntity.getUser());
        if(stringBuilder.length()>=10000){
            upload(stringBuilder);
        }


    }
    public static void upload(StringBuilder stringBuilder){
        LogUtil.e("uploadLogs length=" + stringBuilder.length() + " " + (stringBuilder.length() > 100 ? stringBuilder.substring(0, 100) : stringBuilder));
            //upload
            String logs =stringBuilder.toString();
            stringBuilder.setLength(0);
//            x.http().post()

    }

}
