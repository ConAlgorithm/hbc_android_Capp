package com.hugboga.custom.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.data.bean.CityListBean;
import com.hugboga.custom.data.bean.CountryGroupBean;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.models.ChoicenessGuideModel;
import com.hugboga.custom.models.CityListCustomModel;
import com.hugboga.custom.models.CityListGuideFooterModel;
import com.hugboga.custom.models.CityListGuideHeaderModel;
import com.hugboga.custom.models.CityListHeaderModel;
import com.hugboga.custom.models.CityListHotModel;
import com.hugboga.custom.models.HomeEndModel;
import com.hugboga.custom.models.HotCityModel;
import com.hugboga.custom.widget.CityListHotView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingcha on 17/4/14.
 */
public class CityListAdapter extends EpoxyAdapter {

    public CityListHeaderModel cityListHeaderModel;
    private CityListActivity.Params paramsData;

    public void setData(CityListActivity.Params paramsData) {
        this.paramsData = paramsData;
    }

    public void setCityData(CityListBean cityListBean) {
        if (cityListBean == null) {
            return;
        }
        removeAllModels();
        cityListHeaderModel = new CityListHeaderModel();
        cityListHeaderModel.setData(cityListBean.cityContent);
        addModel(cityListHeaderModel);

        CityListCustomModel cityListCustom = new CityListCustomModel();
        cityListCustom.setData(cityListBean);
        addModel(cityListCustom);

        addCityListHotModel(cityListBean.hotLines, CityListHotView.TYPE_HOT);
    }

    public void setCountryGroupData(CountryGroupBean countryGroupBean) {
        if (countryGroupBean == null) {
            return;
        }
        removeAllModels();
        if (countryGroupBean.hotCities != null && countryGroupBean.hotCities.size() > 0) {
            addModel(new HotCityModel(countryGroupBean.getHotCityList()));
        }
        addModel(new CityListCustomModel());
        addCityListHotModel(countryGroupBean.shortLines, CityListHotView.TYPE_SHORT);
        addCityListHotModel(countryGroupBean.deepLines, CityListHotView.TYPE_DEEP);
    }

    public void setGuideListData(ArrayList<FilterGuideBean> guideList) {
        if (guideList == null || guideList.size() <= 0) {
            return;
        }
        addModel(new CityListGuideHeaderModel(paramsData));
        int size = guideList.size();
        for (int i = 0; i < size; i++) {
            ChoicenessGuideModel guideModel = new ChoicenessGuideModel();
            guideModel.setGuideData(guideList.get(i));
            addModel(guideModel);
        }
        addModel(new CityListGuideFooterModel(paramsData));
    }

    public void addCityListHotModel(List<SkuItemBean> hotLines, int type) {
        if (hotLines != null && hotLines.size() > 0) {
            CityListHotModel cityListHotModel = new CityListHotModel();
            cityListHotModel.setDate(paramsData, hotLines, type);
            addModel(cityListHotModel);
        }
    }

    public void addFooterModel(int tab){
        HomeEndModel homeEndModel = new HomeEndModel();
        homeEndModel.setCurrentTab(tab);
        addModel(homeEndModel);
    }
}
