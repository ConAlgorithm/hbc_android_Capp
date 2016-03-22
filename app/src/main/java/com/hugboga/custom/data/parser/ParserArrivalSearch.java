package com.hugboga.custom.data.parser;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.hugboga.custom.data.bean.ArrivalBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/21.
 */
public class ParserArrivalSearch extends ImplParser {
    public ArrayList<ArrivalBean> listDate ;

    @Override
    public Object parseObject(JSONObject obj) throws Throwable {
        JSONArray contentArray = obj.optJSONArray("places");
        if(contentArray!=null){
            listDate = new ArrayList<ArrivalBean>();
            JSONObject segObj;
            for(int i=0;i<contentArray.length();i++){
                segObj = contentArray.optJSONObject(i);
                ParserArrivalBean parserArrivalBean = new ParserArrivalBean();
                ArrivalBean arrivalBean = parserArrivalBean.parseObject(segObj);
                if(arrivalBean != null){
                    listDate.add(arrivalBean);
                }
            }
        }
        return listDate;
    }
}
