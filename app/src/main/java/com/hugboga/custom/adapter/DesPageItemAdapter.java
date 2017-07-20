package com.hugboga.custom.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.models.DestinationAggModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

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
        EventBus.getDefault().post(new EventAction(EventType.SHOW_DATA));
        /*if (lister != null) {
            lister.visible();
        }*/
    }

    public void addDestionLineGroups(HomeBeanV2.LineGroupAgg groupAggs, int position) {
        if(getItemCount() == 0){
            if (groupAggs != null) {
                addModel(new DestinationAggModel(groupAggs, position));
            }
        }
        EventBus.getDefault().post(new EventAction(EventType.SHOW_DATA));
        /*if (lister != null) {
            lister.visible();
        }*/
    }
    /*public void addFooterModel(int tab){
        HomeEndModel homeEndModel = new HomeEndModel();
        homeEndModel.setCurrentTab(tab);
        addModel(homeEndModel);
    }*/
}
