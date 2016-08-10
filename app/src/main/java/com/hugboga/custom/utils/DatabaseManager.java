package com.hugboga.custom.utils;

import android.text.TextUtils;
import android.util.Log;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 16/7/21.
 */
public final class DatabaseManager {

    public static DbManager getDbManager() {
        return new DBHelper(MyApplication.getAppContext()).getDbManager();
    }

    /**
     * 根据cityId获取城市信息
     * 子线程执行
     */
    public static CityBean getCityBean(String cityId) {
        CityBean cityBean = null;
        try {
            Selector<CityBean> selector = getDbManager().selector(CityBean.class);
            cityBean = selector.where("city_id", "=", cityId).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return cityBean;
    }

    /**
     * 查询全部城市
     */
    public static Selector getAllCitySql(int orderType, int groupId, int cityId, String from) {
        Selector selector = null;
        try {
            selector = getDbManager().selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("1", "=", "1");
        if (orderType == Constants.BUSINESS_TYPE_DAILY) {
            if (groupId == -1) {
                selector.and("is_daily", "=", 1);
            } else {
                selector.and("group_id", "=", groupId);
            }
            if ("lastCity".equals(from) && cityId != -1){
                selector.and("city_id", "<>", cityId);
            }
        } else if (orderType == Constants.BUSINESS_TYPE_RENT) {
            selector.and("is_single", "=", 1);
        } else if (orderType == Constants.BUSINESS_TYPE_PICK || orderType == Constants.BUSINESS_TYPE_SEND) {
            selector.and("is_city_code", "=", 1);
        }
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("place_name", "<>", "中国");
        whereBuilder.and("place_name", "<>", "中国大陆");
        selector.and(whereBuilder);
        selector.orderBy("initial");
        Log.i("aa", "selector " + selector.toString());
        return selector;
    }

    /**
     * 热门城市
     */
    public static Selector getHotDateSql(int orderType, int groupId, int cityId, String from) {
        Selector selector = null;
        try {
            selector = getDbManager().selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("is_hot", "=", 1);
        if (orderType == Constants.BUSINESS_TYPE_DAILY) {
            if (groupId == -1) {
                selector.and("is_daily", "=", 1);
            } else {
                selector.and("group_id", "=", groupId);
            }
            if ("lastCity".equals(from) && cityId != -1){
                selector.and("city_id", "<>", cityId);
            }
        } else if (orderType == Constants.BUSINESS_TYPE_RENT) {
            selector.and("is_single", "=", 1);
        } else if (orderType == Constants.BUSINESS_TYPE_PICK || orderType == Constants.BUSINESS_TYPE_SEND) {
            selector.and("is_city_code", "=", 1);
        }

        if (orderType != Constants.BUSINESS_TYPE_PICK && orderType != Constants.BUSINESS_TYPE_SEND) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("place_name", "<>", "中国");
            whereBuilder.and("place_name", "<>", "中国大陆");
            selector.and(whereBuilder);
        }
        // 修改热门城市排序
        selector.orderBy("hot_weight", true);
        selector.limit(30);
        return selector;
    }

    /**
     * 搜索历史记录
     */
    public static Selector getHistoryDateSql(int orderType, int groupId, int cityId, String from, ArrayList<String> cityHistory) {
        Selector selector = null;
        try {
            selector = getDbManager().selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        WhereBuilder where = WhereBuilder.b();
        if (cityHistory.size() > 0) {
            where.and("city_id", "IN", cityHistory);
        }
        selector.where(where);
        if (orderType == Constants.BUSINESS_TYPE_DAILY) {
            if (groupId == -1) {
                selector.and("is_daily", "=", 1);
            } else {
                selector.and("group_id", "=", groupId);
            }
            if ("lastCity".equals(from) && cityId != -1){
                selector.and("city_id", "<>", cityId);
            }
        } else if (orderType == Constants.BUSINESS_TYPE_RENT) {
            selector.and("is_single", "=", 1);
        } else if (orderType == Constants.BUSINESS_TYPE_PICK || orderType == Constants.BUSINESS_TYPE_SEND) {
            selector.and("is_city_code", "=", 1);
        }
        if (orderType != Constants.BUSINESS_TYPE_PICK && orderType != Constants.BUSINESS_TYPE_SEND) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("place_name", "<>", "中国");
            whereBuilder.and("place_name", "<>", "中国大陆");
            selector.and(whereBuilder);
        }
        return selector;
    }

    /**
     * 查询定位城市
     */
    public static Selector getLocationDateSql(String cityHistoryStr) {
        Selector selector = null;
        try {
            selector = getDbManager().selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        WhereBuilder where = WhereBuilder.b();
        where.and("city_id", "=", cityHistoryStr);
        selector.where(where);
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("place_name", "<>", "中国");
        whereBuilder.and("place_name", "<>", "中国大陆");
        selector.and(whereBuilder);
        return selector;
    }

    /**
     * 获取国内城市
     */
    public static String getInlandCitySql() {
        return "select * from city where is_city_code=1 and place_name='中国' or place_name='中国大陆' order by initial asc";
    }

    /**
     * 获取国外城市
     */
    public static String getAbroadCitySql() {
        return "select * from city where is_city_code=1 and place_name<>'中国' and place_name<>'中国大陆' order by initial asc";
    }

    /**
     * 获取国内热门城市
     */
    public static String getInlandHotCitySql() {
        return "select * from city where is_city_code=1 and (place_name='中国' or place_name='中国大陆') and is_hot=1 order by hot_weight desc";
    }

    /**
     * 获取国际热门城市
     */
    public static String getAbroadHotCitySql() {
        return "select * from city where is_city_code=1 and (place_name<>'中国' and place_name<>'中国大陆') and is_hot=1 order by hot_weight desc";
    }

    /**
     * 获取城市列表
     * 子线程执行
     */
    public static List<CityBean> getCityBeanList(String sql) {
        DbManager mDbManager = new DBHelper(MyApplication.getAppContext()).getDbManager();
        List<CityBean> resultList = new ArrayList<CityBean>();
        SqlInfo sqlinfo = new SqlInfo();
        sqlinfo.setSql(sql);
        try {
            List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
            if (modelList != null && modelList.size() > 0) {
                final int modelListSize = modelList.size();
                for (int i = 0; i < modelListSize; i++) {
                    DbModel model = modelList.get(i);
                    try {
                        if (model != null) {
                            CityBean cityBean = new CityBean();
                            cityBean.cityId = model.getInt("city_id");
                            cityBean.name = model.getString("cn_name");
                            cityBean.areaCode = model.getString("area_code");
                            cityBean.firstLetter = model.getString("initial");
                            cityBean.enName = model.getString("en_name");
                            cityBean.location = model.getString("location");
                            cityBean.placeName = model.getString("place_name");
                            if (!TextUtils.isEmpty(model.getString("group_id")) && "null".equals(model.getString("group_id"))) {
                                cityBean.groupId = model.getInt("group_id");
                            }
                            cityBean.childSeatSwitch = "1".equals(model.getString("childseat_switch"));
                            cityBean.isDaily = model.getBoolean("is_daily");
                            cityBean.isSingle = model.getBoolean("is_single");
                            cityBean.isCityCode = model.getBoolean("is_city_code");
                            cityBean.isHot = model.getBoolean("is_hot");
                            cityBean.hotWeight = model.getInt("hot_weight");
                            cityBean.dailyTip = model.getString("daily_tip");
                            cityBean.neighbourTip = model.getString("neighbourTip");
                            cityBean.hasAirport = model.getBoolean("has_airport");
                            cityBean.description = model.getString("description");
                            resultList.add(cityBean);
                        }
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
