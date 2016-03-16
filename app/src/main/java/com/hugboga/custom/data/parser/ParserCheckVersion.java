package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.CheckVersionBean;
import com.hugboga.custom.data.bean.ResourcesBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/11.
 */
public class ParserCheckVersion extends ImplParser {
//    public String content;
//    public String url;
//    public String force;
//    public ArrayList<ResourcesBean> resList;
//    public String appVersion;
//    public String dbDownloadLink;
//    public int dbVersion;

    @Override
    public CheckVersionBean parseObject(JSONObject obj) throws Throwable {
        CheckVersionBean cvBean = new CheckVersionBean();
        cvBean.content = obj.optString("releaseNote");
        cvBean.url = obj.optString("appDownloadLink");
        cvBean.force = obj.optString("forceUpdate");
        cvBean.appVersion = obj.optString("appVersion");
        cvBean.dbDownloadLink = obj.optString("dbDownloadLink");
        cvBean.dbVersion = obj.optInt("dbVersion");
        JSONArray resUpdates = obj.optJSONArray("resUpdates");
        cvBean.resList = new ArrayList<>();
        if(resUpdates!=null)
            for(int i=0;i<resUpdates.length();i++){
                ResourcesBean bean = new ResourcesBean();
                ParserResourceBean parserRB = new ParserResourceBean();
                bean = parserRB.parseObject(resUpdates.optJSONObject(i));
                cvBean.resList.add(bean);
            }
        return cvBean;
    }
}
