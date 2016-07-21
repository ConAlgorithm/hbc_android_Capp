package com.hugboga.custom.utils;

import android.text.TextUtils;

import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/7/21.
 */
public class CityListDataHelper {

    /**
     * 查询全部城市
     */
    public static Selector getAllCitySql(DbManager mDbManager, int orderType, int groupId, String keyword, int cityId, String from) {
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        selector.where("1", "=", "1");
        if (!TextUtils.isEmpty(keyword)) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("cn_name", "LIKE", "%" + keyword + "%").or("place_name", "LIKE", "%" + keyword + "%");
            selector.and(whereBuilder);
        }
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
        } else if (orderType == Constants.BUSINESS_TYPE_HOME) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("place_name", "<>", "中国");
            selector.and(whereBuilder);
            WhereBuilder whereBuilder2 = WhereBuilder.b();
            whereBuilder2.and("has_airport", "=", 1).or("is_daily", "=", 1).or("is_single", "=", 1);
            selector.and(whereBuilder2);
        }
        selector.orderBy("initial");
        return selector;
    }

    /**
     * 热门城市
     */
    public static Selector getHotDateSql(DbManager mDbManager, int orderType, int groupId, int cityId, String from) {
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
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
        } else if (orderType == Constants.BUSINESS_TYPE_HOME) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("place_name", "<>", "中国");
            selector.and(whereBuilder);
            WhereBuilder whereBuilder2 = WhereBuilder.b();
            whereBuilder2.and("has_airport", "=", 1).or("is_daily", "=", 1).or("is_single", "=", 1);
            selector.and(whereBuilder2);
        }
        // 修改热门城市排序
        selector.orderBy("hot_weight", true);
        selector.limit(30);
        return selector;
    }

    /**
     * 搜索历史记录
     */
    public static Selector getHistoryDateSql(DbManager mDbManager, int orderType, int groupId, int cityId, String from, ArrayList<String> cityHistory) {
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
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
        } else if (orderType == Constants.BUSINESS_TYPE_HOME) {
            WhereBuilder whereBuilder = WhereBuilder.b();
            whereBuilder.and("place_name", "<>", "中国");
            selector.and(whereBuilder);
            WhereBuilder whereBuilder2 = WhereBuilder.b();
            whereBuilder2.and("has_airport", "=", 1).or("is_daily", "=", 1).or("is_single", "=", 1);
            selector.and(whereBuilder2);
        }
        return selector;
    }

    /**
     * 查询定位城市
     */
    public static Selector getLocationDateSql(DbManager mDbManager, String cityHistoryStr) {
        Selector selector = null;
        try {
            selector = mDbManager.selector(CityBean.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        WhereBuilder where = WhereBuilder.b();
        where.and("city_id", "=", cityHistoryStr);
        selector.where(where);
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("place_name", "<>", "中国");
        selector.and(whereBuilder);
        return selector;
    }

}
