package com.hugboga.custom.utils;


import android.app.Activity;

import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.LineGroupBean;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;

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

/**
    -- 查询第一级线路圈
    select * from line_group where level=1;

    -- 查询每个线路圈对应的子组/子国家/子城市
    -- 组 parentType 1.组 2.国家
    select * from line_group where parent_type=#{parentType} and parent_id=#{选择的组/国家ID}

    -- 国家
    select * from line_group_item where type=2 and group_id=#{选择的组ID};
    -- 城市
    select * from line_group_item where type=3 and group_id=#{选择的组ID};


    -- 关键字查询 城市
    select * from line_group_item where sub_city_name like '巴里%' union select * from line_group_item where type=3 and sub_city_name like '%巴里%' and sub_city_name not like '巴里%';

    -- 关键字查询 线路圈
    select * from (select gp.*, 1 as rank from line_group as gp where group_name like '新增%' union select gp.*, 2 as rank from line_group as gp where group_name like '%新增%' and group_name not like '新增%') order by level, rank;

    -- 关键字查询 国家
    select * from line_group_item where type=2 and sub_place_name like '意%' union select * from line_group_item where type=2 and sub_place_name like '%意%' and sub_place_name not like '意%';

    **/



    public static List<LineGroupBean> getLevel1City(Activity activity) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupBean.class);
            selector.where("level", "=", 1);
            selector.orderBy("hot_weight", true);
            selector.limit(30);
            return selector.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
