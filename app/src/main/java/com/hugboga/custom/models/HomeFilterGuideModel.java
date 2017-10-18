package com.hugboga.custom.models;

import android.app.Activity;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.widget.HomeFilterGuideView;

import java.util.List;

/**
 * Created by zhangqiang on 17/8/1.
 */

public class HomeFilterGuideModel extends EpoxyModel<HomeFilterGuideView> {

    private List<FilterGuideBean> guideList;
    Activity activity;
    public HomeFilterGuideModel(Activity activity){
        this.activity = activity;
    }
    @Override
    protected int getDefaultLayout() {
        return R.layout.model_filter_guide;
    }

    public void setGuideData(List<FilterGuideBean> guideList) {
        this.guideList = guideList;
    }
    @Override
    public void bind(HomeFilterGuideView view) {
        super.bind(view);
        view.setActivity(activity);
        view.setGuideBeanList(guideList);
    }
}
