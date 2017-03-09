package com.hugboga.custom.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBeanV2;

import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/9.
 */
public class HomeHeaderModel extends EpoxyModelWithHolder {

    HomeBeanV2.HomeHeaderInfo homeHeaderInfo;

    public HomeHeaderModel(HomeBeanV2.HomeHeaderInfo homeHeaderInfo) {
        this.homeHeaderInfo = homeHeaderInfo;
    }

    @Override
    protected HomeHeaderHolder createNewHolder() {
        return new HomeHeaderHolder();
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_header_view;
    }

    static class HomeHeaderHolder extends EpoxyHolder {

        View itemView;

        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
