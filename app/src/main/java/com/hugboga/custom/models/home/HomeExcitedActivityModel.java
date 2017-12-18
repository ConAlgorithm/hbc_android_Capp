package com.hugboga.custom.models.home;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.widget.home.HomeExcitedActivityView;

import java.util.ArrayList;

/**
 * Created by qingcha on 17/11/24.
 */

public class HomeExcitedActivityModel extends EpoxyModel<HomeExcitedActivityView> {

    ArrayList<HomeBean.ExcitedActivityBean> excitingActivityList;
    public HomeExcitedActivityView itemView;

    public void setData(ArrayList<HomeBean.ExcitedActivityBean> _List) {
        this.excitingActivityList = _List;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_home_excited_activity;
    }

    @Override
    public void bind(HomeExcitedActivityView view) {
        super.bind(view);
        view.update(excitingActivityList);
        this.itemView = view;
    }

}