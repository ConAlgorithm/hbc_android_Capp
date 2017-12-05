package com.hugboga.custom.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;

import butterknife.ButterKnife;

/**
 * Created by HONGBO on 2017/12/5 19:05.
 */

public class CitySkuNoModel extends EpoxyModelWithHolder<CitySkuNoModel.CitySkuNoHolder> {

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_sku_no_layout;
    }

    @Override
    public void bind(CitySkuNoHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
    }

    @Override
    protected CitySkuNoHolder createNewHolder() {
        return new CitySkuNoHolder();
    }

    class CitySkuNoHolder extends EpoxyHolder {

        @Override
        protected void bindView(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
