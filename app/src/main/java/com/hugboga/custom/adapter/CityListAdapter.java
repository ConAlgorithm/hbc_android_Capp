package com.hugboga.custom.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.data.bean.CityListBean;
import com.hugboga.custom.data.bean.CountryGroupBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.models.CityListCustomModel;
import com.hugboga.custom.models.CityListHeaderModel;
import com.hugboga.custom.models.CityListHotModel;
import com.hugboga.custom.models.HotCityModel;
import java.util.List;

/**
 * Created by qingcha on 17/4/14.
 */
public class CityListAdapter extends EpoxyAdapter {

    public CityListHeaderModel cityListHeaderModel;

    public void setData(CityListActivity.CityHomeType type) {
        switch (type) {
            case CITY:
                break;
            case ROUTE:
                break;
            case COUNTRY:
                break;
        }
    }

    public void setCityData(CityListBean cityListBean) {
        if (cityListBean == null) {
            return;
        }
        cityListHeaderModel = new CityListHeaderModel();
        cityListHeaderModel.setData(cityListBean.cityContent);
        addModel(cityListHeaderModel);

        CityListCustomModel cityListCustom = new CityListCustomModel();
        cityListCustom.setData(cityListBean);
        addModel(cityListCustom);

        addCityListHotModel(cityListBean.hotLines, CityListHotModel.TYPE_HOT);
    }

    public void setCountryGroupData(CountryGroupBean countryGroupBean) {
        if (countryGroupBean == null) {
            return;
        }
        if (countryGroupBean.hotCities != null && countryGroupBean.hotCities.size() > 0) {
            addModel(new HotCityModel(countryGroupBean.getHotCityList()));
        }
        addModel(new CityListCustomModel());
        addCityListHotModel(countryGroupBean.shortLines, CityListHotModel.TYPE_SHORT);
        addCityListHotModel(countryGroupBean.deepLines, CityListHotModel.TYPE_DEEP);
    }

    public void addCityListHotModel(List<SkuItemBean> hotLines, int type) {
        if (hotLines != null && hotLines.size() > 0) {
            CityListHotModel cityListHotModel = new CityListHotModel();
            cityListHotModel.setDate(hotLines, type);
            addModel(cityListHotModel);
        }
    }
}
