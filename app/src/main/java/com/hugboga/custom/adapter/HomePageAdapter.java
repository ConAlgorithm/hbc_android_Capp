package com.hugboga.custom.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.models.DestinationAggModel;
import com.hugboga.custom.models.HomeEndModel;
import com.hugboga.custom.models.HomeHeaderModel;
import com.hugboga.custom.models.HomeNetworkErrorModel;
import com.hugboga.custom.models.HotExplorationModel;
import com.hugboga.custom.models.TravelStoryModel;
import com.hugboga.custom.widget.home.HomeSearchTabView;

import java.util.List;

/**
 * Created by SPW on 2017/3/9.
 */
public class HomePageAdapter extends EpoxyAdapter {

    public HomeHeaderModel homeHeaderModel;

    public void showHeader(HomeBeanV2.HomeHeaderInfo homeHeaderInfo, HomeSearchTabView.HomeTabClickListener homeTabClickListener) {
        homeHeaderModel = new HomeHeaderModel(homeHeaderInfo, homeTabClickListener);
        addModel(homeHeaderModel);
    }

    public void addHotExploations(List<HomeBeanV2.HotExploration> hotExplorationList,boolean switchTab,int listCount,int dataSize) {
        if(switchTab){
            removeAllAfterModel(homeHeaderModel);
        }
        if (hotExplorationList != null && hotExplorationList.size() > 0) {
            for (HomeBeanV2.HotExploration hotExploration : hotExplorationList) {
                addModel(new HotExplorationModel(hotExploration));
            }
        }
        if(listCount==dataSize){
            addFooterModel();
        }
    }

    public void addHotCitys(List<HomeBeanV2.HotCity> cities) {
        removeAllAfterModel(homeHeaderModel);
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
            addFooterModel();
        }
    }

    public void addStoryModels(List<HomeBeanV2.TravelStory> stories,boolean switchTab,int listCount,int dataSize) {
        if(switchTab){
            removeAllAfterModel(homeHeaderModel);
        }
        if (stories != null && stories.size() > 0) {
            for (int i = 0; i < stories.size(); i++) {
                TravelStoryModel model = new TravelStoryModel(stories.get(i), i);
                addModel(model);
            }
        }
        if(listCount==dataSize){
            addFooterModel();
        }
    }

    public void addFooterModel(){
        addModel(new HomeEndModel());
    }

    public void clearAll(){
        removeAllModels();
    }

    HomeNetworkErrorModel homeNetworkErrorModel;
    public void addNetworkErrorModel(HomeNetworkErrorModel.ReloadListener reloadListener){
        if(homeNetworkErrorModel==null){
            homeNetworkErrorModel = new HomeNetworkErrorModel(reloadListener);
        }
        removeNetworkErrorModel();
        addModel(homeNetworkErrorModel);
    }

    public void removeNetworkErrorModel(){
        if(homeNetworkErrorModel!=null){
            removeModel(homeNetworkErrorModel);
        }
    }

}
