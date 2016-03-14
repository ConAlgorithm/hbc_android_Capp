package com.hugboga.custom.data.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * 资源文件
 * Created by admin on 2015/11/20.
 */
public class ResourcesBean implements IBaseBean {
    public int resVersion;
    public String resName;
    public String resUrl;

    @Override
    public void parser(JSONObject jsonObj) throws JSONException {
        resVersion = jsonObj.optInt("resVersion");
        resName = jsonObj.optString("resName");
        resUrl = jsonObj.optString("resDownloadLink");
    }
}
