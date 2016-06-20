package com.hugboga.custom.utils;


import android.app.Activity;

import com.hugboga.custom.data.bean.CityBean;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class CityUtils {

    /**
     * 热门城市
     *
     * @param orderType
     * @param groupId
     */
    public static  List<CityBean>  requestHotDate(Activity activity, int groupId) {
        List<CityBean> sourceDateList = new ArrayList<CityBean>();//全部城市数据
        DbManager mDbManager = new DBHelper(activity).getDbManager();
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("is_hot", "=", 1);
        if (groupId == -1) {
            selector.and("is_daily", "=", 1);
        } else {
            selector.and("group_id", "=", groupId);
        }
//        if ("lastCity".equals(from) && cityId != -1){
//            selector.and("city_id", "<>", cityId);
//        }
        // 修改热门城市排序
        selector.orderBy("hot_weight", true);
//        selector.orderBy("hot_weight");
        selector.limit(30);
        try {
            List<CityBean> hotCityDate = selector.findAll();
            if(hotCityDate != null && hotCityDate.size() > 0) {
                for (CityBean bean : hotCityDate) {
                    bean.firstLetter = "热门城市";
                }
            }
            if (hotCityDate != null && hotCityDate.size() > 0 && hotCityDate.get(0) != null) {
                hotCityDate.get(0).isFirst = true;
                sourceDateList.add(0, hotCityDate.get(0));
                return hotCityDate;
            }
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static String getHotCityStr(List<CityBean> hotCitys){
        if(null == hotCitys){
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("热门城市:");
        CityBean cityBean;
        int size = hotCitys.size();
        for(int i = 0;i< size;i++){
            cityBean = hotCitys.get(i);
            if(size >= 20){
                stringBuffer.append("等");
                return stringBuffer.toString();
            }else {
                if(i == (size -1)){
                    stringBuffer.append(cityBean.name);
                }else{
                    stringBuffer.append(cityBean.name).append(",");
                }
            }
        }
        return stringBuffer.toString();
    }
}
