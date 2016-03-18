/*
 * ImageUtils.java [V1.0.0]
 * classes:com.hugboga.custom.utils.ImageUtils
 * ZHZEPHI Create at 2015年3月23日 上午11:41:44
 */
package com.hugboga.custom.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.hugboga.custom.constants.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * com.hugboga.custom.utils.ImageUtils
 * @author ZHZEPHI
 * Create at 2015年3月23日 上午11:41:44
 */
public class ImageUtils {
	
    public static String bitmapToString(Bitmap bitmap) {

        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }
    
    /**
     * 检查图片目录是否存在
     * 不存在，则创建
     * Created by ZHZEPHI at 2015年3月23日 下午12:10:21
     */
    public static void checkDir(){
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
    		File fileDir = new File(Constants.IMAGE_DIR);
        	if(!fileDir.exists()){
        		fileDir.mkdir();
        	}
    	}else{
    		Log.i("ERROR:", "SD卡不存在");
    	}
    }

}
