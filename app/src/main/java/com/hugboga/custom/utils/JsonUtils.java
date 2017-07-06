package com.hugboga.custom.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.huangbaoche.hbcframe.HbcConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Created by qingcha on 16/7/7.
 */
public enum JsonUtils {

    INSTANCE;

    private Gson gson = null;

    JsonUtils() {
        gson = new Gson();
    }

    public Gson getGson() {
        return gson;
    }

    public static Object fromJson(String obj, Type type) {
        Object result = null;
        if (HbcConfig.IS_DEBUG) {
            result = INSTANCE.gson.fromJson(obj, type);
        } else {
            try {
                result = INSTANCE.gson.fromJson(obj, type);
            } catch (Exception e) {
                result = null;
            }
        }
        return result;
    }

    public static Object fromJson(Object obj, Type type) {
        Object result = null;
        if (HbcConfig.IS_DEBUG) {
            if (obj instanceof String) {
                result = INSTANCE.gson.fromJson((String) obj, type);
            } else {
                result = INSTANCE.gson.fromJson(INSTANCE.gson.toJson(obj), type);
            }
        } else {
            try {
                if (obj instanceof String) {
                    result = INSTANCE.gson.fromJson((String) obj, type);
                } else {
                    result = INSTANCE.gson.fromJson(INSTANCE.gson.toJson(obj), type);
                }
            } catch (Exception e) {
                result = null;
            }
        }
        return result;
    }

    public static <T> T getObject(String objStr, Type type) {
        return INSTANCE.gson.fromJson(objStr, type);
    }

    public static <T> T getNativeObject(String fileName, Type type) {
        String fileJson = FileUtil.getNativeFile(fileName);
        return INSTANCE.gson.fromJson(fileJson, type);
    }

    public static String toJson(Object obj) {
        String result = null;
        try {
            result = INSTANCE.gson.toJson(obj);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public static String toJson(Object obj, Type tpye) {
        String result = null;
        try {
            result = INSTANCE.gson.toJson(obj, tpye);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public static boolean isJson(String str) {
        try {
            new JsonParser().parse(str);
            return true;
        } catch(JsonParseException e) {
            return false;
        }
    }

    /**
     * 从文件中获取Json字符串
     * @param context
     * @param fileName
     * @return
     */
    public static String getJsonStr(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
