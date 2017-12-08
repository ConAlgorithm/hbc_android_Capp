package com.hugboga.custom.models.city;

import android.view.View;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.city.CityFilterView;

/**
 * 过滤器view
 * Created by HONGBO on 2017/12/8 19:11.
 */

public class CityFilterModel extends EpoxyModel<CityFilterView> {

    public CityFilterView cityFilterView;
    public CityFilterView.FilterSeeListener filterSeeListener;

    @Override
    public void bind(CityFilterView view) {
        super.bind(view);
        if (view == null) {
            return;
        }
        this.cityFilterView = view;
        //监听筛选项变化
        cityFilterView.setFilterSeeListener(filterSeeListener);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_filter_model_layout;
    }
}
