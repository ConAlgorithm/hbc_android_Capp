package com.hugboga.custom.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.amap.view.HbcMapViewTools;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.models.HomeHeaderModel;
import com.hugboga.custom.models.HotExplorationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SPW on 2017/3/9.
 */
public class HomePageAdapter extends EpoxyAdapter {

    HomeHeaderModel homeHeaderModel;
    ArrayList<HotExplorationModel> hotExplorationModels = new ArrayList<>();//热门探索城市列表

    public void showHeader(HomeBeanV2.HomeHeaderInfo homeHeaderInfo){
        homeHeaderModel = new HomeHeaderModel(homeHeaderInfo);
        addModel(homeHeaderModel);
    }

    public void addHotExploations(List<HomeBeanV2.HotExploration> hotExplorationList) {
        if (hotExplorationList != null && hotExplorationList.size() > 0) {
            for (HomeBeanV2.HotExploration hotExploration : hotExplorationList) {
                //hotExplorationModels.add(new HotExplorationModel(hotExploration));
                addModel(new HotExplorationModel(hotExploration));
            }
            //addModels(hotExplorationModels);
        }
    }
}
