package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.widget.HotCityView;

import java.util.ArrayList;

/**
 * Created by qingcha on 17/4/17.
 */

public class HotCityModel extends EpoxyModel<HotCityView> {

    private ArrayList<ArrayList<HomeBeanV2.HotCity>> hotCityList;

    public HotCityModel(ArrayList<ArrayList<HomeBeanV2.HotCity>> hotCityList) {
        this.hotCityList = hotCityList;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_hot_city;
    }

    @Override
    public void bind(HotCityView view) {
        super.bind(view);
        view.update(hotCityList);
    }
}
