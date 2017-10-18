package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityListBean;
import com.hugboga.custom.data.bean.CountryGroupBean;
import com.hugboga.custom.widget.CityListCustomView;

/**
 * Created by qingcha on 17/4/14.
 */
public class CityListCustomModel extends EpoxyModel<CityListCustomView> {

    private CityListBean cityListBean;
    private CountryGroupBean countryGroupBean;

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_citylist_custom;
    }

    @Override
    public void bind(CityListCustomView view) {
        super.bind(view);
        if (cityListBean != null) {
            view.setData(cityListBean);
        }
        if (countryGroupBean != null) {
            view.setData(countryGroupBean);
        }
    }

    public void setData(CityListBean cityListBean) {
        this.cityListBean = cityListBean;
    }

    public void setData(CountryGroupBean countryGroupBean) {
        this.countryGroupBean = countryGroupBean;
    }
}
