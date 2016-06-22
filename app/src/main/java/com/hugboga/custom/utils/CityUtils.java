package com.hugboga.custom.utils;


import android.app.Activity;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.LineGroupBean;
import com.hugboga.custom.data.bean.LineGroupItem;
import com.hugboga.custom.data.bean.LineHotSpotBean;
import com.hugboga.custom.data.bean.SaveStartEndCity;
import com.hugboga.custom.data.bean.SearchGroupBean;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;

import java.lang.reflect.Type;
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
     select * from line_group where parent_type=1 and parent_id=#{选择的组ID}

     -- 国家
     select * from line_group_item where type=2 and group_id=#{选择的组ID};
     -- 城市
     select * from line_group_item where type=3 and group_id=#{选择的组ID};


     -- 查询国家下的子组/城市 （国家下没有国家）
     -- 组
     select * from line_group where parent_type=2 and parent_id=#{选择的国家ID}

     -- 城市
     select distinct * from line_group_item where sub_place_id=#{选择的国家ID};


    -- 关键字查询 城市
    select * from line_group_item where sub_city_name like '巴里%' union select * from line_group_item where type=3 and sub_city_name like '%巴里%' and sub_city_name not like '巴里%';

    -- 关键字查询 线路圈
    select * from (select gp.*, 1 as rank from line_group as gp where group_name like '新增%' union select gp.*, 2 as rank from line_group as gp where group_name like '%新增%' and group_name not like '新增%') order by level, rank;

    -- 关键字查询 国家
    select * from line_group_item where type=2 and sub_place_name like '意%' union select * from line_group_item where type=2 and sub_place_name like '%意%' and sub_place_name not like '意%';

    **/


   public static  List<SearchGroupBean> lineGroupBeanAdapter(List<LineGroupBean> list,int flag){
       List<SearchGroupBean> searchList = new ArrayList<>();
       SearchGroupBean searchGroupBean = null;
       for(LineGroupBean bean:list){
           searchGroupBean = new SearchGroupBean();
           searchGroupBean.group_id = bean.group_id;
           searchGroupBean.group_name = bean.group_name;
           searchGroupBean.type = 1;
           searchGroupBean.flag = flag;
           searchList.add(searchGroupBean);
       }
       return searchList;
   }

    public static  List<SearchGroupBean> lineGroupItemAdapter(List<LineGroupItem> list,int flag){
        List<SearchGroupBean> searchList = new ArrayList<>();
        SearchGroupBean searchGroupBean = null;
        for(LineGroupItem bean:list){
            searchGroupBean = new SearchGroupBean();
            searchGroupBean.group_id = bean.group_id;
            searchGroupBean.group_name = bean.group_name;

            searchGroupBean.sub_place_id = bean.sub_place_id;
            searchGroupBean.sub_place_name = bean.sub_place_name;

            searchGroupBean.sub_city_id = bean.sub_city_id;
            searchGroupBean.sub_city_name = bean.sub_city_name;

            searchGroupBean.type = bean.type;
            searchGroupBean.flag = flag;
            searchList.add(searchGroupBean);
        }
        return searchList;
    }

    public static  List<SearchGroupBean> lineHotCityAdapter(List<LineHotSpotBean> list){
        List<SearchGroupBean> searchList = new ArrayList<>();
        SearchGroupBean searchGroupBean = null;
        for(LineHotSpotBean bean:list){
            searchGroupBean = new SearchGroupBean();
            searchGroupBean.spot_id = bean.spot_id;
            searchGroupBean.spot_name = bean.spot_name;
            searchGroupBean.type = bean.type;
            searchGroupBean.flag = 4;
            searchList.add(searchGroupBean);
        }
        return searchList;
    }


    public static List<SearchGroupBean> getLevel1City(Activity activity) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupBean.class);
            selector.where("level", "=", 1);
            selector.orderBy("hot_weight", true);
            List<LineGroupBean> list = selector.findAll();
            return lineGroupBeanAdapter(list,1);
        }catch (Exception e){
            e.printStackTrace();
           return null;
        }
    }

//    -- 组 parentType 1.组 2.国家
//    select * from line_group where parent_type=1 and parent_id=#{选择的组ID}

    public static List<SearchGroupBean> getType1City(Activity activity,int group_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupBean.class);
            selector.where("parent_type", "=", 1);
            selector.and("parent_id", "=",group_id);
            List<LineGroupBean> list = selector.findAll();
            return lineGroupBeanAdapter(list,2);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


//    -- 国家
//    select * from line_group_item where type=2 and group_id=#{选择的组ID};

    public static List<SearchGroupBean> getType2City(Activity activity,int group_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupItem.class);
            selector.where("type", "=", 2);
            selector.and("group_id", "=",group_id);
            List<LineGroupItem> list = selector.findAll();
            return lineGroupItemAdapter(list,2);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


//    -- 城市
//    select * from line_group_item where type=3 and group_id=#{选择的组ID};
    public static List<SearchGroupBean> getType3City(Activity activity,int group_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupItem.class);
            selector.where("type", "=", 3);
            selector.and("group_id", "=",group_id);
            List<LineGroupItem> list = selector.findAll();
            return lineGroupItemAdapter(list,2);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static List<SearchGroupBean> getLevel2City(Activity activity,int group_id) {
        try {
            List<SearchGroupBean> list = new ArrayList<>();
            list.addAll(getType1City(activity,group_id));
            list.addAll(getType2City(activity,group_id));
            list.addAll(getType3City(activity,group_id));
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


//    -- 查询国家下的子组/城市 （国家下没有国家）
//            -- 组
//    select * from line_group where parent_type=2 and parent_id=#{选择的国家ID}
    public static List<SearchGroupBean> getType21City(Activity activity,int group_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupBean.class);
            selector.where("parent_type", "=", 2);
            selector.and("parent_id", "=",group_id);
            List<LineGroupBean> list = selector.findAll();
            return lineGroupBeanAdapter(list,3);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

//    -- 城市
//    select distinct * from line_group_item where sub_place_id=#{选择的国家ID};

    public static List<SearchGroupBean> getType31City(Activity activity,int group_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupItem.class);
            selector.where("sub_place_id", "=",group_id);
            List<LineGroupItem> list = selector.findAll();
            return lineGroupItemAdapter(list,3);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static List<SearchGroupBean> getLevel3City(Activity activity,int group_id) {
        try {
            List<SearchGroupBean> list = new ArrayList<>();

            List<SearchGroupBean> list1 = getType21City(activity,group_id);
            List<SearchGroupBean> list2 = getType31City(activity,group_id);
            if(null != list1 && list1.size() > 0) {
                list.addAll(list1);
            }
            if(null != list2 && list2.size() > 0) {
                list.addAll(list2);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //type 1 组  2,国家  3, 城市
    //热门   type 1 = 城市   2, 国家

    //获取热门城市
    public static List<SearchGroupBean> getHotCity(Activity activity) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineHotSpotBean.class);
            List<LineHotSpotBean> list = selector.findAll();
            return lineHotCityAdapter(list);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<SearchGroupBean> getHotCityWithHead(Activity activity) {
        List<SearchGroupBean> hotCityList = getHotCity(activity);
        SearchGroupBean searchGroupBean = new SearchGroupBean();
        searchGroupBean.spot_id = -1;
        searchGroupBean.spot_name = "接送机";
        searchGroupBean.flag = 4;
        hotCityList.add(0,searchGroupBean);

        searchGroupBean = new SearchGroupBean();
        searchGroupBean.spot_id = -2;
        searchGroupBean.flag = 4;
        searchGroupBean.spot_name = "单次接送";
        hotCityList.add(1,searchGroupBean);

        searchGroupBean = new SearchGroupBean();
        searchGroupBean.spot_id = -3;
        searchGroupBean.flag = 4;
        searchGroupBean.spot_name = "包车游";
        hotCityList.add(2,searchGroupBean);
        return hotCityList;
    }


    public static List<SearchGroupBean>  getSaveCity(){
        try {
            Type resultType = new TypeToken<List<SearchGroupBean>>() {}.getType();
            return Reservoir.get("savedHistoryCity", resultType);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void addCityHistoryData(SearchGroupBean searchGroupBean ){

        List<SearchGroupBean> list = getSaveCity();
        if(null == list){
            list = new ArrayList<>();
        }else{

            if(list.size() > 10){
                for(int i = list.size() -1 ;i >= 10;i--){
                    list.remove(i);
                }
            }
        }
        list.add(searchGroupBean);
        try {
            Reservoir.put("savedHistoryCity", list);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //type 1 组  2,国家  3, 城市
    //热门   type 1 = 城市   2, 国家
    public static boolean canGoCityList(SearchGroupBean searchGroupBean ){
        if(searchGroupBean.flag == 2){
            if(searchGroupBean.type == 1){
                return true;
            }else if(searchGroupBean.type == 2){
                return false;
            }else if(searchGroupBean.type == 3){
                return true;
            }
        }else if(searchGroupBean.flag == 4){
            return true;
        }
        return true;
    }
}
