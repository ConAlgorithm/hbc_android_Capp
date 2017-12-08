package com.hugboga.custom.models.city;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.city.CityFilterView;

/**
 * 过滤器view
 * Created by HONGBO on 2017/12/8 19:11.
 */

public class CityFilterModel extends EpoxyModel<CityFilterView> {

    public CityFilterView cityFilterView;

    @Override
    public void bind(CityFilterView view) {
        super.bind(view);
        if (view == null) {
            return;
        }
        this.cityFilterView = view;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_filter_model_layout;
    }
}
