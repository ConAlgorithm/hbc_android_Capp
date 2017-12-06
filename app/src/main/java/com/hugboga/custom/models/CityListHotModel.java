package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityActivity;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.widget.CityListHotView;

import java.util.List;

public class CityListHotModel extends EpoxyModel<CityListHotView> {

    public List<SkuItemBean> hotLines;
    public int type;
    public CityActivity.Params paramsData;

    @Override
    public boolean shouldSaveViewState() {
        return true;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_city_list_hot;
    }

    public void setDate(CityActivity.Params paramsData, List<SkuItemBean> hotLines, int type) {
        this.paramsData = paramsData;
        this.hotLines = hotLines;
        this.type = type;
    }

    @Override
    public void bind(CityListHotView view) {
        super.bind(view);
        view.setDate(paramsData,hotLines,type);
    }
}
