package com.hugboga.custom.data.bean;

import com.google.gson.Gson;
import com.huangbaoche.hbcframe.data.parser.ImplParser;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangqiang on 17/6/22.
 */

public class OssTokenBean extends ImplParser {
    private String address;
    private Long validMinutes;
    private OssTokenParamBean param;
    private List<OssTokenKeyBean> keys;

    /*public OssTokenBean parse(String jsonStr) {
        return new Gson().fromJson(jsonStr, this.getClass());
    }*/

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getValidMinutes() {
        return validMinutes;
    }

    public void setValidMinutes(Long validMinutes) {
        this.validMinutes = validMinutes;
    }

    public OssTokenParamBean getOssTokenParamBean() {
        return param;
    }

    public void setOssTokenParamBean(OssTokenParamBean param) {
        this.param = param;
    }

    public List<OssTokenKeyBean> getKeys() {
        return keys;
    }

    public void setKeys(List<OssTokenKeyBean> keys) {
        this.keys = keys;
    }

    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        Gson gson = new Gson();
        OssTokenBean bean =  gson.fromJson(obj.toString(),OssTokenBean.class);
        return bean;
    }

    @Override
    public String toString() {
        return "OssTokenBean"+"address=" +address+"validMinutes=" +validMinutes+"OssTokenParamBean="+param.toString();
    }
}
