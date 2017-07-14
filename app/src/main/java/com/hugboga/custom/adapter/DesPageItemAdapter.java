package com.hugboga.custom.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.bean.HomeHotCityVo;
import com.hugboga.custom.fragment.FgHomePage;
import com.hugboga.custom.models.DestinationAggModel;
import com.hugboga.custom.models.HomeEndModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class DesPageItemAdapter extends EpoxyAdapter {

    public void addHotCitys(ArrayList<HomeBeanV2.HotCity> homeHotCityVos, int position) {
        if(getItemCount()==0){
            if (homeHotCityVos != null && homeHotCityVos.size() > 0) {
                addModel(new DestinationAggModel(homeHotCityVos, position));
            }
        }
    }

    public void addDestionLineGroups(HomeBeanV2.LineGroupAgg groupAggs, int position) {
        if(getItemCount() == 0){
            if (groupAggs != null) {
                addModel(new DestinationAggModel(groupAggs, position));
            }
        }
    }
    /*public void addFooterModel(int tab){
        HomeEndModel homeEndModel = new HomeEndModel();
        homeEndModel.setCurrentTab(tab);
        addModel(homeEndModel);
    }*/
}
