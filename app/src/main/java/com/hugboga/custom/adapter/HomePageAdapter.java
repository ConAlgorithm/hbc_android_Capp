package com.hugboga.custom.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.fragment.FgHomePage;
import com.hugboga.custom.models.ChoicenessGuideModel;
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
        if(homeHeaderModel!=null){
            homeHeaderModel.setHomeHeaderInfo(homeHeaderInfo);
            homeHeaderModel.update();
        }else{
            homeHeaderModel = new HomeHeaderModel(homeHeaderInfo, homeTabClickListener);
            addModel(homeHeaderModel);
        }


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
            addFooterModel(FgHomePage.TAB_HOTEXPLORE);
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
        if(listCount>=dataSize){
            addFooterModel(FgHomePage.TAB_DESTION);
        }
    }

    public void addGuideModels(List<FilterGuideBean> guideList, boolean switchTab, int listCount, int dataSize) {
        if (switchTab) {
            removeAllAfterModel(homeHeaderModel);
        }
        if (guideList != null && guideList.size() > 0) {
            int size = guideList.size();
            for (int i = 0; i < size; i++) {
                ChoicenessGuideModel guideModel = new ChoicenessGuideModel();
                guideModel.setGuideData(guideList.get(i));
                addModel(guideModel);
            }
        }
        if(listCount==dataSize){
            addFooterModel(FgHomePage.TAB_GUIDE);
        }
    }

    public void addFooterModel(int tab){
        HomeEndModel homeEndModel = new HomeEndModel();
        homeEndModel.setCurrentTab(tab);
        addModel(homeEndModel);
    }

    public void clearAll(){
        removeAllModels();
    }

    public void removeAfterHeader(){
        if(homeHeaderModel!=null){
            removeAllAfterModel(homeHeaderModel);
        }else{
            removeAllModels();
        }

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
