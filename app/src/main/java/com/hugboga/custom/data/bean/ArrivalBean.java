package com.hugboga.custom.data.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2015/7/21.
 */
public class ArrivalBean implements IBaseBean {

    public String key;
    public String placeName;
    public String placeDetail;
    public double lng;
    public double lat;
    public String location;

    public boolean isFirst =false;
    public boolean isHistory =false;

}
