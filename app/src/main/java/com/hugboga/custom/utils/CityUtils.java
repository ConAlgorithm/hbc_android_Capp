package com.hugboga.custom.utils;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.LineGroupBean;
import com.hugboga.custom.data.bean.LineGroupItem;
import com.hugboga.custom.data.bean.LineHotSpotBean;
import com.hugboga.custom.data.bean.SearchGroupBean;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;
import static android.R.attr.order;

public class CityUtils {


    public static SpannableString addImg(Activity activity,String content,int imgIds){
        Drawable drawable = activity.getResources().getDrawable(imgIds);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        SpannableString spannable = new SpannableString("[icon]  " + content);
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(span, 0, "[icon]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannable;
    }

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

    public static List<CityBean> requestDataByKeyword(Activity activity, int groupId, int cityId,
                                                  String keyword, boolean isNeedMore) {
        List<CityBean> dataList = new ArrayList<CityBean>();
        DbManager mDbManager = new DBHelper(activity).getDbManager();
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("1", "=", "1");
        if (!TextUtils.isEmpty(keyword)) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            if (isNeedMore) {
                whereBuilder.and("cn_name", "LIKE", "%" + keyword + "%");
            } else {
                whereBuilder.and("cn_name", "LIKE", keyword + "%");
            }
            selector.and(whereBuilder);
        }

            if (groupId == -1) {
                selector.and("is_daily", "=", 1);
            } else {
                selector.and("group_id", "=", groupId);
            }
          selector.and("city_id", "<>", cityId);

        try {
            dataList = selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (dataList.size() > 0) {
            for (CityBean cb : dataList) {
                cb.keyWord = keyword;
            }
        }
        return dataList;
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
     select * from line_group where parent_type=2 and parent_id=#{选择的国家ID}

     -- 城市
     select distinct * from line_group_item where sub_place_id=#{选择的国家ID} and type=3;


 -- 搜索部分查询
 -- 关键字查询 城市
 select gi.*, 1 as rank from line_group_item as gi where type=3 and sub_city_name like '巴里%'
 union
 select gi.*, 2 as rank from line_group_item as gi where type=3 and sub_city_name like '%巴里%' and sub_city_name not like '巴里%' order by rank;

 -- 关键字查询 线路圈
 select * from (select gp.*, 1 as rank from line_group as gp where group_name like '新增%'
 union
 select gp.*, 2 as rank from line_group as gp where group_name like '%新增%' and group_name not like '新增%') order by level, rank;

 -- 关键字查询 国家
 select gi.*, 1 as rank from line_group_item as gi where type=2 and sub_place_name like '意%'
 union
 select gi.*, 2 as rank from line_group_item as gi where type=2 and sub_place_name like '%意%' and sub_place_name not like '意%';

    **/


/**
    -- 组 parentType 1.组 2.国家
    select * from line_group where parent_type=1 and parent_id=#{选择的组ID}
    -- 国家
    select * from line_group_item where type=2 and group_id=#{选择的组ID};
    -- 城市
    select * from line_group_item where type=3 and group_id=#{选择的组ID};
    -- 查询国家下的子组/城市 （国家下没有国家）
    select * from line_group where parent_type=2 and parent_id=#{选择的国家ID}
 **/

    //获取国家下面的热门线路
    public static List<SearchGroupBean> getCountryLine(Activity activity,String parent_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupBean.class);
            selector.where("parent_type", "=", 2);
            selector.and("parent_id","=",parent_id);
            selector.limit(3);
            List<LineGroupBean> list = selector.findAll();
            return lineGroupBeanAdapter(list,1);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //获取国家下面的城市
    public static List<SearchGroupBean> getCountryCity(Activity activity,String sub_place_id) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupItem.class);
            selector.where("type", "=", 3);
            selector.and("sub_place_id","=",sub_place_id);
            selector.limit(5);
            List<LineGroupItem> list = selector.findAll();
            return lineGroupItemAdapter(list,3);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //搜索用
    public static List<SearchGroupBean> getCountLineCity(Activity activity,SearchGroupBean bean){
        List<SearchGroupBean> list = new ArrayList<>();
        if(bean.type == 1 && bean.level == 1) {
            List<SearchGroupBean> lineList = getCountryLine(activity, bean.parent_id + "");

            if (null != lineList && lineList.size() > 0) {
                list.addAll(lineList);
            }
            List<SearchGroupBean> level2List = getType1City(activity,bean.group_id);
            if(null != level2List && level2List.size() > 0) {
                list.addAll(level2List);
            }

            if(null != lineList && lineList.size() > 0 || null != level2List && level2List.size() > 0){
                SearchGroupBean searchGroupBean = new SearchGroupBean();
                searchGroupBean.group_id = -1;
                searchGroupBean.group_name = getShowName(bean) + "多地畅游";
                list.add(0, searchGroupBean);
            }



            List<SearchGroupBean> cityList = new ArrayList<>();
            List<SearchGroupBean> type2List = getType2City(activity, bean.group_id);
            List<SearchGroupBean> type3List = getType3City(activity, bean.group_id);

            if (null != type2List && type2List.size() > 0) {
                cityList.addAll(type2List);
            }

            if (null != type3List && type3List.size() > 0) {
                cityList.addAll(type3List);
            }

            if(null != type2List && type2List.size() > 0 || null != type3List && type3List.size() > 0){
                SearchGroupBean searchGroupBean = new SearchGroupBean();
                searchGroupBean.group_id = -2;
                searchGroupBean.group_name = getShowName(bean) + "热门目的地";
                cityList.add(0, searchGroupBean);
                list.addAll(cityList);
            }
        }

        if(bean.type == 2) {
            List<SearchGroupBean> cityList = getCountryCity(activity, bean.sub_place_id + "");

            if (null != cityList && cityList.size() > 0) {
                list.addAll(cityList);
                SearchGroupBean searchGroupBean = new SearchGroupBean();
                searchGroupBean.group_id = -2;
                searchGroupBean.group_name = getShowName(bean) + "热门目的地";
                list.add(0, searchGroupBean);
            }
        }

        return list;

    }


    public static  List<SearchGroupBean> lineGroupBeanAdapter(List<LineGroupBean> list,int flag){
       List<SearchGroupBean> searchList = new ArrayList<>();
       SearchGroupBean searchGroupBean = null;
       if(null == list){
           return searchList;
       }
       for(LineGroupBean bean:list){
           searchGroupBean = new SearchGroupBean();
           searchGroupBean.group_id = bean.group_id;
           searchGroupBean.group_name = bean.group_name;

           searchGroupBean.parent_name = bean.parent_name;
           searchGroupBean.parent_id = bean.parent_id;

           searchGroupBean.level = bean.level;
           searchGroupBean.type = 1;
           searchGroupBean.flag = flag;
           searchList.add(searchGroupBean);
       }
       return searchList;
   }

    public static  List<SearchGroupBean> lineGroupItemAdapter(List<LineGroupItem> list,int flag){
        List<SearchGroupBean> searchList = new ArrayList<>();
        if(null == list){
            return searchList;
        }
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
        if(null == list){
            return searchList;
        }
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
            return lineGroupBeanAdapter(list,1);
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
            return lineGroupItemAdapter(list,3);
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
            selector.and("type","=",3);
            List<LineGroupItem> list = selector.findAll();
            return lineGroupItemAdapter(list,3);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static List<SearchGroupBean> getLevel3City(Activity activity,int place_id) {
        try {
            List<SearchGroupBean> list = new ArrayList<>();


            List<SearchGroupBean> list3 = getType4City(activity,place_id);
//            List<SearchGroupBean> list1 = getType21City(activity,group_id);
//            List<SearchGroupBean> list2 = getType31City(activity,group_id);
//            if(null != list1 && list1.size() > 0) {
//                list.addAll(list1);
//            }
//            if(null != list2 && list2.size() > 0) {
//                list.addAll(list2);
//            }
            if(null != list3 && list3.size() > 0) {
                list.addAll(list3);
            }

            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    public static List<SearchGroupBean> getType4City(Activity activity,int place_id){
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(CityBean.class);
            selector.where("place_id", "=",place_id);
            List<CityBean> list = selector.findAll();
            return cityAdapter(list);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static List<SearchGroupBean> cityAdapter(List<CityBean> list){
        List<SearchGroupBean> searchList = new ArrayList<>();
        if(null == list){
            return searchList;
        }
        SearchGroupBean searchGroupBean = null;
        for(CityBean bean:list){
            searchGroupBean = new SearchGroupBean();
            searchGroupBean.group_id = -1;
            searchGroupBean.group_name = "";

            searchGroupBean.sub_place_id = -1;
            searchGroupBean.sub_place_name = bean.placeName;

            searchGroupBean.sub_city_id = bean.cityId;
            searchGroupBean.sub_city_name = bean.name;

            searchGroupBean.hot_weight = bean.hotWeight;

            searchGroupBean.type = 3;
            searchGroupBean.flag = 3;
            searchList.add(searchGroupBean);
        }
        return searchList;
    }

    //type 1 组  2,国家  3, 城市
    //热门   type 1 = 城市   2, 国家

    //获取热门城市
    public static List<SearchGroupBean> getHotCity(Activity activity) {
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineHotSpotBean.class);
            selector.orderBy("hot_weight",true);
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

            for(int i = list.size() -1 ;i >= 0;i--){
                if(searchGroupBean.flag == 2){
                    if(searchGroupBean.sub_place_id == list.get(i).sub_place_id){
                        list.remove(i);
                    }
                }else if(searchGroupBean.flag == 3){
                    if(searchGroupBean.sub_city_id == list.get(i).sub_city_id){
                        list.remove(i);
                    }
                }else if(searchGroupBean.flag == 4){
                    if(searchGroupBean.spot_id == list.get(i).spot_id){
                        list.remove(i);
                    }
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


    public static String getShowName(SearchGroupBean searchGroupBean ){
            if(searchGroupBean.flag == 2){
                if(searchGroupBean.type == 1){
                    return searchGroupBean.group_name;
                }else if(searchGroupBean.type == 2){
                    return searchGroupBean.sub_place_name;
                }else if(searchGroupBean.type == 3){
                    return searchGroupBean.sub_city_name;
                }
            }else if(searchGroupBean.flag == 3){
                if(searchGroupBean.type == 1){
                    return searchGroupBean.group_name;
                }else if(searchGroupBean.type == 2){
                    return searchGroupBean.sub_place_name;
                }else if(searchGroupBean.type == 3){
                    return searchGroupBean.sub_city_name;
                }
            }else if(searchGroupBean.flag == 1){
                return searchGroupBean.group_name;
            }else if(searchGroupBean.flag == 4){
                return searchGroupBean.spot_name;
            }
            return "";

    }

    //获取上级名称
    public static String getParentName(SearchGroupBean searchGroupBean ){
            if(searchGroupBean.type == 1){
                return searchGroupBean.parent_name;
            }else if(searchGroupBean.type == 2){
                return searchGroupBean.group_name;
            }else if(searchGroupBean.type == 3){
                return searchGroupBean.sub_place_name;
            }

        return "";

    }


/**
    -- 搜索部分查询
    -- 关键字查询 城市
    select gi.*, 1 as rank from line_group_item as gi where type=3 and sub_city_name like '巴里%'
    union
    select gi.*, 2 as rank from line_group_item as gi where type=3 and sub_city_name like '%巴里%' and sub_city_name not like '巴里%' order by rank;

    -- 关键字查询 线路圈
    select * from
 (select gp.*, 1 as rank from line_group as gp where group_name like '新增%'
   union
    select gp.*, 2 as rank from line_group as gp where group_name like '%新增%' and group_name not like '新增%') order by level, rank;

    -- 关键字查询 国家
    select gi.*, 1 as rank from line_group_item as gi where type=2 and sub_place_name like '意%'
    union
    select gi.*, 2 as rank from line_group_item as gi where type=2 and sub_place_name like '%意%' and sub_place_name not like '意%';

    **/

/**
    -- 关键字查询 城市   改
    SELECT -1 as group_id, '' as group_name, 4 AS type, city_id AS sub_city_id, cn_name AS sub_city_name, place_name AS sub_place_name, - 1 AS sub_group_id,
 '' AS sub_group_name, city.hot_weight, 1 as rank
    FROM city where cn_name like '巴里%' AND sub_place_name != '中国' AND sub_place_name != '中国大陆'

    union

    SELECT -1 as group_id, '' as group_name, 4 AS type, city_id AS sub_city_id, cn_name AS sub_city_name, place_name AS sub_place_name, - 1 AS sub_group_id,
 '' AS sub_group_name, city.hot_weight, 2 as rank
    from city where cn_name like '%巴里%' and cn_name not like '巴里%' and place_name!='中国' and place_name!='中国大陆' order by rank;
 **/


//搜索获取城市
    public static List<SearchGroupBean> searchCity(Activity activity,String key){
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(CityBean.class);
            selector.where("cn_name","like",key+"%");
            selector.and("sub_place_name","<>","中国");
            selector.and("sub_place_name","<>","中国大陆");
            List<CityBean> list1 = selector.findAll();


            selector = mDbManager.selector(CityBean.class);
            selector.where("cn_name","like","%"+key + "%");
            selector.and("cn_name","not like",key+"%");
            selector.and("place_name","<>","中国");
            selector.and("sub_place_name","<>","中国大陆");
            selector.orderBy("rank");
            List<CityBean> list2 = selector.findAll();

            List<CityBean> list = new ArrayList<>();
            if(null != list1) {
                list.addAll(list1);
            }
            if(null != list2) {
                list.addAll(list2);
            }
            return cityAdapter(list,3);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static List<SearchGroupBean> cityAdapter(List<CityBean> list,int type){
        List<SearchGroupBean> searchList = new ArrayList<>();
        SearchGroupBean searchGroupBean = null;
        if(null == list){
            return searchList;
        }
        for(CityBean bean:list){
            searchGroupBean = new SearchGroupBean();
            searchGroupBean.group_id = -1;
            searchGroupBean.group_name = "";

            searchGroupBean.sub_city_id = bean.cityId;
            searchGroupBean.sub_city_name = bean.name;

            searchGroupBean.sub_place_name = bean.placeName;
            searchGroupBean.hot_weight = bean.hotWeight;

            searchGroupBean.type = type;
            searchGroupBean.flag = type;
            searchList.add(searchGroupBean);
        }
        return searchList;
    }

//查询线路圈
    public static List<SearchGroupBean> searchLine(Activity activity,String key){
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupBean.class);
            selector.where("group_name","like",key+"%");
            List<LineGroupBean> list1 = selector.findAll();

            selector = mDbManager.selector(LineGroupBean.class);
            selector.where("group_name","like","%"+key+"%");
            selector.and("group_name","not like",key+"%");
            selector.orderBy("level").orderBy("rank");
            List<LineGroupBean> list2 = selector.findAll();

            List<LineGroupBean> list  = new ArrayList<>();
            if(null != list1) {
                list.addAll(list1);
            }
            if(null != list2) {
                list.addAll(list2);
            }
            return lineGroupBeanAdapter(list,1);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

//查询国家
    public static List<SearchGroupBean> searchCountry(Activity activity,String key){
        try {
            DbManager mDbManager = new DBHelper(activity).getDbManager();
            Selector selector = null;
            selector = mDbManager.selector(LineGroupItem.class);
            selector.where("type","=",2);
            selector.and("sub_place_name","like",key+"%");
            List<LineGroupItem> list1 = selector.findAll();

            selector = mDbManager.selector(LineGroupItem.class);
            selector.where("type","=",2);
            selector.and("sub_place_name","like","%"+key+"%");
            selector.and("sub_place_name","not like",key+"%");
            selector.groupBy("sub_place_id");
            selector.orderBy("rank");
            List<LineGroupItem> list2 = selector.findAll();

            List<LineGroupItem> list  = new ArrayList<>();
            if(null != list1) {
                list.addAll(list1);
            }
            if(null != list2) {
                list.addAll(list2);
            }
            return lineGroupItemAdapter(list,2);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<SearchGroupBean> search(Activity activity,String key){
        List<SearchGroupBean> list  = new ArrayList<>();

        List<SearchGroupBean> cityList  = searchCity(activity,key);
        List<SearchGroupBean> lineList  = searchLine(activity,key);
        List<SearchGroupBean> countryList  = searchCountry(activity,key);

        list.addAll(cityList);
        list.addAll(lineList);
        list.addAll(countryList);
        return list;
    }


    public static SpannableStringBuilder getSpannableString(String name, String keyWord) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(name);
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(Color.parseColor("#FDCE02"));
        int start = ssb.toString().indexOf(keyWord);
        if(start != -1){
            int end = start + keyWord.length();
            ssb.setSpan(yellowSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ssb;
    }


}
