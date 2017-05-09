package com.hugboga.custom.data.request;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.huangbaoche.hbcframe.data.parser.ImplParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CountryGroupBean;
import com.hugboga.custom.data.net.NewParamsBuilder;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.HbcParser;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DatabaseManager;

import org.xutils.db.Selector;
import org.xutils.ex.DbException;
import org.xutils.http.annotation.HttpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qingcha on 17/4/15.
 *
 * http://wiki.hbc.tech/pages/viewpage.action?pageId=6619493#id-首页－商品-HomeHotCityVo
 */
@HttpRequest(path = UrlLibs.API_COUNTRY_GROUP, builder = NewParamsBuilder.class)
public class RequestCountryGroup extends BaseRequest<CountryGroupBean> {

    private static final int MAX_CITY_COUNT = 12;

    public RequestCountryGroup(Context context, int id, String type) {
        super(context);
        map = new HashMap<>();
        map.put("source", Constants.REQUEST_SOURCE);
        map.put("channelId","18");
        map.put("id", id);//国家/线路圈ID
        map.put("type", type);//类型 1.线路圈 2.国家
        map.put("hotCityIds", getHotCityId(id, CommonUtils.getCountInteger(type)));
    }

    public String getHotCityId(int id, int type) {

        ArrayMap<Integer, CityBean> arrayMap = new ArrayMap<>();
        try {
            String hotCitySql = "";
            if (type == 1) {
                hotCitySql = DatabaseManager.getCitysByGroupIdSql(id, null, false, true);
            } else {
                hotCitySql = DatabaseManager.getCitysByPlaceIdSql(id, null, false, true);
            }
            List<CityBean> hotCityList = DatabaseManager.getCityBeanList(hotCitySql);
            int hotCityListSize = hotCityList.size();
            for (int i = 0; i < hotCityListSize; i++) {
                CityBean cityBean = hotCityList.get(i);
                arrayMap.put(cityBean.cityId, cityBean);
            }
            if (hotCityListSize < MAX_CITY_COUNT) {
                String citySql = "";
                if (type == 1) {
                    citySql = DatabaseManager.getCitysByGroupIdSql(id, null, false, false);
                } else {
                    citySql = DatabaseManager.getCitysByPlaceIdSql(id, null, false, false);
                }
                List<CityBean> allCityList = DatabaseManager.getCityBeanList(citySql);
                int allCityListSize = allCityList.size();
                for (int i = 0; i < allCityListSize && arrayMap.size() < MAX_CITY_COUNT; i++) {
                    CityBean cityBean = allCityList.get(i);
                    if (!arrayMap.containsKey(cityBean.cityId)) {
                        arrayMap.put(cityBean.cityId, cityBean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int mapSize = arrayMap.size();
        int i = 0;
        String result = "";
        for (Integer key : arrayMap.keySet()) {
            result += key;
            if (i != mapSize - 1) {
                result += ",";
            }
            i++;
        }
        return result;
    }

    @Override
    public ImplParser getParser() {
        return new HbcParser(UrlLibs.API_COUNTRY_GROUP, CountryGroupBean.class);
    }

    @Override
    public String getUrlErrorCode() {
        return "40137";
    }

}