package com.hugboga.custom.data.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/4/15.
 */
public class CountryGroupBean implements Serializable{
    public List<HomeBeanV2.HotCity> hotCities;  // 热门城市
    public List<SkuItemBean> shortLines;        // 热门线路列表
    public List<SkuItemBean> deepLines;         // 深度长度
    public boolean hasAirportService;           // 接机服务
    public boolean hasSingleService;            // 次租服务
    public boolean hasDailyService;             // 日租服务

    private ArrayList<ArrayList<HomeBeanV2.HotCity>> hotCityList;  // 对数据做拆分,6个一条

    public ArrayList<ArrayList<HomeBeanV2.HotCity>> getHotCityList() {
        if (hotCityList == null) {
            hotCityList = new ArrayList<ArrayList<HomeBeanV2.HotCity>>();
            if (hotCities != null && hotCities.size() > 0) {
                ArrayList<HomeBeanV2.HotCity> itemList = new ArrayList<HomeBeanV2.HotCity>(6);
                final int size = hotCities.size();
                for (int i = 0; i < size; i++) {
                    HomeBeanV2.HotCity hotCity = hotCities.get(i);
                    if (hotCity == null) {
                        continue;
                    }
                    itemList.add(hotCity);
                    final int itemListSize = itemList.size();
                    if (itemListSize == 6) {
                        hotCityList.add(itemList);
                        itemList = new ArrayList<HomeBeanV2.HotCity>(6);
                    } else if (i == size -1 && itemListSize > 0) {
                        hotCityList.add(itemList);
                    }
                }
            }
        }
        return hotCityList;
    }

    public boolean isEmpty() {
        boolean hotListEmpty = hotCities == null || hotCities.size() <= 0;
        boolean shortLinesEmpty = shortLines == null || shortLines.size() <= 0;
        boolean deepLinesEmpty = deepLines == null || deepLines.size() <= 0;
        return hotListEmpty && shortLinesEmpty && deepLinesEmpty;
    }
}
