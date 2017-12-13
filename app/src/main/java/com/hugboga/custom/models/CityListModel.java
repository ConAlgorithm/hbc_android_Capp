package com.hugboga.custom.models;

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
    boolean isFavious = false;
    CitySkuView view;
    CitySkuView.OnChangeFaviousListener onChangeFaviousListener; //修改收藏状态

    public CityListModel(Context mContext, DestinationGoodsVo destinationGoodsVo,CitySkuView.OnChangeFaviousListener onChangeFaviousListener) {
        this.mContext = mContext;
        this.destinationGoodsVo = destinationGoodsVo;
        this.onChangeFaviousListener = onChangeFaviousListener;
    }

    @Override
    public void bind(CitySkuView view) {
        super.bind(view);
        this.view = view;
        view.init(destinationGoodsVo, isFavious);
        view.setOnChangeFavious(onChangeFaviousListener);
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.city_list_model;
    }

    public void setFavious(boolean favious) {
        isFavious = favious;
    }

    public DestinationGoodsVo getDestinationGoodsVo() {
        return destinationGoodsVo;
    }

    public CitySkuView getView() {
        return view;
    }
}
