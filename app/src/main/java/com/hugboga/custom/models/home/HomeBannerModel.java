package com.hugboga.custom.models.home;

import android.widget.LinearLayout;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.widget.home.HomeBannerView;

import java.util.ArrayList;

/**
 * Created by qingcha on 17/11/23.
 */
public class HomeBannerModel extends EpoxyModel<HomeBannerView> {

    public HomeBannerView itemView;
    private ArrayList<HomeBean.BannerBean> list;

    public void setData(ArrayList<HomeBean.BannerBean> _List) {
        this.list = _List;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_home_banner;
    }

    @Override
    public void bind(HomeBannerView view) {
        super.bind(view);
        view.update(list);
        this.itemView = view;
    }

}