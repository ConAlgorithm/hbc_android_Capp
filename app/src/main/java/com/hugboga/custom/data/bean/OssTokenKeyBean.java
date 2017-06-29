package com.hugboga.custom.data.bean;

/**
 * Created by zhangqiang on 17/6/22.
 */

public class OssTokenKeyBean {
    private String path;
    private String key;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "OssTokenKeyBean"+ "key="+key+"path="+path;
    }
}
