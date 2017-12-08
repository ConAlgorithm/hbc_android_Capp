package com.hugboga.custom.models.city;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.city.CityHeaderView;

/**
 * Created by HONGBO on 2017/12/8 19:07.
 */

public class CityHeaderModel extends EpoxyModel<CityHeaderView> {
    @Override
    protected int getDefaultLayout() {
        return R.layout.city_header_model_layout;
    }

    @Override
    public void bind(CityHeaderView view) {
        super.bind(view);
        if (view == null) {
            return;
        }
    }
}
