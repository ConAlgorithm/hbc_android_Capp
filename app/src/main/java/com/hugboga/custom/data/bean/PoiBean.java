package com.hugboga.custom.data.bean;

/**
 * Created by admin on 2015/7/21.
 */
public class PoiBean implements IBaseBean {

    public int id;
    public String key;
    public String placeName;
    public String placeDetail;
    public double lng;
    public double lat;
    public String location;

    public boolean isFirst = false;
    public boolean isHistory = false;

}
