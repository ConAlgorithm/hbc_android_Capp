package com.hugboga.custom.models.city;

import android.content.Context;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.widget.city.CitySkuView;

/**
 * Created by HONGBO on 2017/11/30 11:15.
 */

public class CityListModel extends EpoxyModel<CitySkuView> {

    Context mContext;
    DestinationGoodsVo destinationGoodsVo;
    CitySkuView view;

    public CityListModel(Context mContext, DestinationGoodsVo destinationGoodsVo) {
        this.mContext = mContext;
        this.destinationGoodsVo = destinationGoodsVo;
    }

    @Override
    public void bind(CitySkuView view) {
        super.bind(view);
        this.view = view;
        view.init(destinationGoodsVo);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_list_model;
    }

    public DestinationGoodsVo getDestinationGoodsVo() {
        return destinationGoodsVo;
    }

    public CitySkuView getView() {
        return view;
    }
}
