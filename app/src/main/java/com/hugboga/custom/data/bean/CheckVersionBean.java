package com.hugboga.custom.data.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/11.
 */
public class CheckVersionBean implements IBaseBean {
    public String content;
    public String url;
    public boolean force;
    public boolean hasAppUpdate;
    public boolean debugMod;
    public ArrayList<ResourcesBean> resList;
    public String appVersion;
    public String dbDownloadLink;
    public int dbVersion;
    public boolean hasAntiId;//代表是否已存在反作弊设备id 如果不存在，就可以上报设备id 存在就不用再上报了
}
