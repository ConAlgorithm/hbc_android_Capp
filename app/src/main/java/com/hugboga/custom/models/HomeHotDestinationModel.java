package com.hugboga.custom.models;

import android.content.Context;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeHotDestination;
import com.hugboga.custom.widget.HomeHotDestinationView;

import java.util.ArrayList;

/**
 * Created by zhangqiang on 17/9/13.
 */

public class HomeHotDestinationModel extends EpoxyModel<HomeHotDestinationView> {
    Context context;
    ArrayList<HomeHotDestination> hotCities;
    public HomeHotDestinationModel(Context context, ArrayList<HomeHotDestination> hotCities){
        this.context = context;
        this.hotCities = hotCities;
    }
    @Override
    protected int getDefaultLayout() {
        return R.layout.home_hot_destination;
    }
    @Override
    public void bind(HomeHotDestinationView view) {
        super.bind(view);
        //view.setHotCities(hotCities);
        view.update(hotCities);

    }
}
