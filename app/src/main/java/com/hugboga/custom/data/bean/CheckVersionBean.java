package com.hugboga.custom.data.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/11.
 */
public class CheckVersionBean implements IBaseBean{
    public String content;
    public String url;
    public String force;
    public ArrayList<ResourcesBean> resList;
    public String appVersion;
    public String dbDownloadLink;
    public int dbVersion;
}
