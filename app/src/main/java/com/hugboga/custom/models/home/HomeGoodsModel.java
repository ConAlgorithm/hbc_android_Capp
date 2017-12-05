package com.hugboga.custom.models.home;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.home.HomeGoodsView;

import java.util.List;

/**
 * Created by qingcha on 17/11/24.
 */

public class HomeGoodsModel<T> extends EpoxyModel<HomeGoodsView<T>> {

    public static final int TYPE_TRANSFER = 1;
    public static final int TYPE_CHARTERED = 2;

    public List<T> itemList;
    public int type;// 1:接送机，2:包车

    @Override
    public boolean shouldSaveViewState() {
        return true;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_home_goods;
    }

    public void setDate(List<T> itemList, int type) {
        this.itemList = itemList;
        this.type = type;
    }

    @Override
    public void bind(HomeGoodsView view) {
        super.bind(view);
        view.setDate(itemList, type);
    }
}