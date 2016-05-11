package com.hugboga.custom.utils;

import android.text.TextUtils;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  on 16/4/15.
 */
public class DBCityUtils {

    private  DbManager mDbManager;


    public  String getGroupId(String cityId) {
        String groupId = "";
        mDbManager = new DBHelper(MyApplication.getAppContext()).getDbManager();
            try {
                Selector<CityBean> selector = mDbManager.selector(CityBean.class);
                CityBean cityBean = selector.where("city_id", "=", cityId).findFirst();
                if (cityBean != null) {
                    groupId = cityBean.groupId+"";
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        return groupId;
    }
    /**
     * 按关键词搜索
     *
     * @param orderType  业务类型(BUSINESS_TYPE)
     * @param groupId    群组关系
     * @param keyword    关键词
     * @param isNeedMore 是否需要更多结果(%keyword%的),第一次调置为false
     */
    public List<CityBean> requestDataByKeyword( String keyword, boolean isNeedMore) {
        mDbManager = new DBHelper(MyApplication.getAppContext()).getDbManager();
        List<CityBean> dataList = new ArrayList<CityBean>();
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

        try {
            dataList = selector.findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
