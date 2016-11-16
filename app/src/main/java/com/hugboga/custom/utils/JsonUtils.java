package com.hugboga.custom.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.huangbaoche.hbcframe.HbcConfig;

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
            result = INSTANCE.gson.fromJson(INSTANCE.gson.toJson(obj), type);
        } else {
            try {
                result = INSTANCE.gson.fromJson(INSTANCE.gson.toJson(obj), type);
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
}
