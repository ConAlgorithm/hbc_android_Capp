package com.hugboga.custom.data.bean;

/**
 *
 * poi 点
 * Created by admin on 2016/3/22.
 */
public class PoiBean implements IBaseBean {

    public int id;
    public String key;
    public String placeName;
    public String placeDetail;
    public double lng;
    public double lat;
    public String location;

    public boolean isFirst =false;
    public boolean isHistory =false;
}
