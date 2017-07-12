package com.hugboga.custom.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.fragment.FgHomePage;
import com.hugboga.custom.models.DestinationAggModel;
import com.hugboga.custom.models.HomeEndModel;

import java.util.List;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class DesPageItemAdapter extends EpoxyAdapter {

    public void addService(int position){
        removeAllModels();
        addModel(new DestinationAggModel(position));

    }
    public void addHotCitys(List<HomeBeanV2.HotCity> cities) {
        //removeAllAfterModel(homeHeaderModel);
        if(cities!=null && cities.size()>0){
            addModel(new DestinationAggModel(cities));
        }
    }

    public void addDestionLineGroups(List<HomeBeanV2.LineGroupAgg> groupAggs,int listCount,int dataSize) {
        if (groupAggs != null && groupAggs.size() > 0) {
            for (HomeBeanV2.LineGroupAgg lineGroupAgg : groupAggs) {
                if((lineGroupAgg.lineGroupCities==null || lineGroupAgg.lineGroupCities.size()==0)&&
                        (lineGroupAgg.lineGroupCountries==null || lineGroupAgg.lineGroupCountries.size()==0)){
                    continue;
                }
                addModel(new DestinationAggModel(lineGroupAgg));
            }
        }
        if(listCount==dataSize){
            addFooterModel(FgHomePage.TAB_DESTION);
        }
    }
    public void addFooterModel(int tab){
        HomeEndModel homeEndModel = new HomeEndModel();
        homeEndModel.setCurrentTab(tab);
        addModel(homeEndModel);
    }
}
