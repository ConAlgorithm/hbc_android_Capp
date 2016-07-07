package com.hugboga.custom.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

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
        return INSTANCE.gson.fromJson(obj, type);
    }

    public static <T> T getObject(String objStr, Type type) {
        return INSTANCE.gson.fromJson(objStr, type);
    }

    public static <T> T getNativeObject(String fileName, Type type) {
        String fileJson = FileUtil.getNativeFile(fileName);
        return INSTANCE.gson.fromJson(fileJson, type);
    }

    public static String toJson(Object obj) {
        return INSTANCE.gson.toJson(obj);
    }

    public static String toJson(Object obj, Type tpye) {
        return INSTANCE.gson.toJson(obj, tpye);
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
