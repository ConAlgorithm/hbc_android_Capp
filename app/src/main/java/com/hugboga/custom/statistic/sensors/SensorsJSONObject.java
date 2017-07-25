package com.hugboga.custom.statistic.sensors;

import com.hugboga.custom.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qingcha on 17/7/25.
 */

public class SensorsJSONObject extends JSONObject {

    public SensorsJSONObject() {
        boolean isTest = "developer".equals(BuildConfig.FLAVOR) || "examination".equals(BuildConfig.FLAVOR);
        try {
            put("isTest", isTest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
