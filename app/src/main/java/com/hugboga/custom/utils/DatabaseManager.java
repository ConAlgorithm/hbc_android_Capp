package com.hugboga.custom.utils;

import android.database.Cursor;
import android.text.TextUtils;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.activity.ChooseCityActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.AirPort;
import com.hugboga.custom.data.bean.CityBean;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

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
    public static Selector getAllCitySql(int orderType, int groupId, int cityId, String from, int countryId) {
        Selector selector = null;
        try {
            selector = getDbManager().selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("1", "=", "1");
        if (orderType == Constants.BUSINESS_TYPE_DAILY) {
            if (ChooseCityActivity.GROUP_START.equals(from)) {
                selector.and("is_daily", "=", 1);
                selector.and("group_id", "=", groupId);
                if (cityId != -1) {
                    selector.and("city_id", "<>", cityId);
                }
            } else if (ChooseCityActivity.CITY_LIST.equals(from) && countryId != -1) {
                selector.and("place_id", "=", countryId);
            } else {
                if (groupId == -1) {
                    selector.and("is_daily", "=", 1);
                } else {
                    selector.and("group_id", "=", groupId);
                }
                if ("lastCity".equals(from) && cityId != -1){
                    selector.and("city_id", "<>", cityId);
                } else if (ChooseCityActivity.GROUP_OUTTOWN.equals(from) && cityId != -1){
                    selector.and("city_id", "<>", cityId);
                }
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
        return selector;
    }

    /**
     * 热门城市
     */
    public static Selector getHotDateSql(int orderType, int groupId, int cityId, String from, int countryId) {
        Selector selector = null;
        try {
            selector = getDbManager().selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        if( "lastCity".equals(from)){
            selector.where("is_passcity_hot", "=", 1);
        }else{
            selector.where("is_hot", "=", 1);
        }
        if (orderType == Constants.BUSINESS_TYPE_DAILY) {
            if (ChooseCityActivity.GROUP_START.equals(from)) {
                selector.and("is_daily", "=", 1);
                selector.and("group_id", "=", groupId);
                if (cityId != -1) {
                    selector.and("city_id", "<>", cityId);
                }
            } else if (ChooseCityActivity.CITY_LIST.equals(from) && countryId != -1) {
                selector.and("place_id", "=", countryId);
            } else {
                if (groupId == -1) {
                    selector.and("is_daily", "=", 1);
                } else {
                    selector.and("group_id", "=", groupId);
                }
                if ("lastCity".equals(from) && cityId != -1){
                    selector.and("city_id", "<>", cityId);
                } else if (ChooseCityActivity.GROUP_OUTTOWN.equals(from) && cityId != -1){
                    selector.and("city_id", "<>", cityId);
                }
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

    public static String getCitysByPlaceIdSql(int place_id) {
        return getCitysByPlaceIdSql(place_id, null, false, false);
    }

    public static String getCitysByPlaceIdSql(int place_id, String _keyword, boolean isNeedMore, boolean ishot) {
        String sql = "select * from city where place_id=%1$s %2$s %3$s and (city.has_airport=1 or city.is_daily=1 or city.is_single=1 or city.has_goods=1) order by %4$s";
        String keywordSql = "";
        if (!TextUtils.isEmpty(_keyword)) {
            if (isNeedMore) {
                keywordSql = "and cn_name like '%"+ _keyword + "%'";
            } else {
                keywordSql = "and cn_name like '"+ _keyword + "%'";
            }
        }
        String hotCitySql = "";
        if (ishot) {
            hotCitySql = "and is_hot=1";
        }

        String sortSql = "initial asc";
        if (ishot) {
            sortSql = "hot_weight desc";
        }

        return String.format(sql, place_id, TextUtils.isEmpty(keywordSql) ? "" : keywordSql, hotCitySql, sortSql);
    }

    public static String getCitysByGroupIdSql(int place_id) {
        return getCitysByGroupIdSql(place_id, null, false, false);
    }

    public static String getCitysByGroupIdSql(int place_id, String _keyword, boolean isNeedMore, boolean ishot) {
        try {
            DbManager mDbManager = getDbManager();
            SqlInfo sqlinfo = new SqlInfo();
            String keywordSql = "";
            if (!TextUtils.isEmpty(_keyword)) {
                if (isNeedMore) {
                    keywordSql = "and cn_name like '%"+ _keyword + "%'";
                } else {
                    keywordSql = "and cn_name like '"+ _keyword + "%'";
                }
            }
            String hotCitySql = "";
            if (ishot) {
                hotCitySql = "and is_hot=1";
            }

            String sortSql = "initial asc";
            if (ishot) {
                sortSql = "hot_weight desc";
            }

            sqlinfo.setSql(String.format("select * from line_group where group_id=%1$s", place_id));
            String sql = "select * from city where (%1$s) %2$s %3$s and (city.has_airport=1 or city.is_daily=1 or city.is_single=1 or city.has_goods=1) order by %4$s;";
            try {
                List<DbModel> modelList = mDbManager.findDbModelAll(sqlinfo);
                if (modelList != null && modelList.size() > 0) {
                    final int listsize = modelList.size();
                    for (int modelindex = 0; modelindex <listsize; modelindex++) {
                        DbModel model = modelList.get(modelindex);
                        if (model != null) {
                            String subPlaces = model.getString("sub_places");
                            String subCities = model.getString("sub_cities");//city_id in (1270, 1269) or place_id in(70)
                            String params = "";
                            if (!TextUtils.isEmpty(subCities) && !TextUtils.isEmpty(subPlaces)) {
                                params = String.format("city_id in (%1$s) or place_id in(%2$s)", subCities, subPlaces);
                            } else if (!TextUtils.isEmpty(subPlaces)) {
                                params = String.format("place_id in(%1$s)", subPlaces);
                            } else if (!TextUtils.isEmpty(subCities)) {
                                params = String.format("city_id in(%1$s)", subCities);
                            } else {
                                return null;
                            }
                            return String.format(sql, params, TextUtils.isEmpty(keywordSql) ? "" : keywordSql, hotCitySql, sortSql);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public static List<AirPort> queryAirPortByCityId(String cityId) {
        DbManager mDbManager = new DBHelper(MyApplication.getAppContext()).getDbManager();
        List<AirPort> airPorts = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = "select * from airport where city_id=" + cityId;
            cursor = mDbManager.execQuery(sql);
            while (cursor!=null && cursor.moveToNext()){
                AirPort airPort = new AirPort();
                airPort.airportId = cursor.getInt(cursor.getColumnIndexOrThrow("airport_id"));
                airPort.airportCode = cursor.getString(cursor.getColumnIndexOrThrow("airport_code"));
                airPort.airportName = cursor.getString(cursor.getColumnIndexOrThrow("airport_name"));
                airPort.areaCode = cursor.getString(cursor.getColumnIndexOrThrow("area_code"));
                airPort.cityFirstLetter = cursor.getString(cursor.getColumnIndexOrThrow("city_initial"));
                int bannerswitch = cursor.getInt(cursor.getColumnIndexOrThrow("banner_switch"));
                airPort.bannerSwitch = bannerswitch==0?false:true;
                airPort.cityId = cursor.getInt(cursor.getColumnIndexOrThrow("city_id"));
                airPort.cityName = cursor.getString(cursor.getColumnIndexOrThrow("city_name"));
                airPort.location = cursor.getString(cursor.getColumnIndexOrThrow("airport_location"));
                airPort.hotWeight = cursor.getInt(cursor.getColumnIndexOrThrow("hot_weight"));
                int hot = cursor.getInt(cursor.getColumnIndexOrThrow("is_hot"));
                airPort.isHot = hot==0?false:true;
                int visaSwitch = cursor.getInt(cursor.getColumnIndexOrThrow("landing_visa_switch"));
                airPort.visaSwitch = visaSwitch==0?false:true;
                int childSwitch = cursor.getInt(cursor.getColumnIndexOrThrow("childseat_switch"));
                airPort.childSeatSwitch = childSwitch==0?false:true;
                airPorts.add(airPort);
            }
            if(cursor!=null){
                cursor.close();
            }
        } catch (Exception e) {

        }finally {
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return airPorts;
    }
}
