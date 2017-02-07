package com.hugboga.custom.data.bean;

/**
 * Created by admin on 2015/7/21.
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=7932956
 */
public class PoiBean implements IBaseBean {

    public String placeName;
    public String placeDetail;
    public double lng;
    public double lat;
    public String location;

    public boolean isHistory = false; //本地字段
    public String type;//from,to 本地字段



}
