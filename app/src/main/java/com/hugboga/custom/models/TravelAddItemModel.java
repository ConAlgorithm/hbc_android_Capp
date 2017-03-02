package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.TravelAddItemView;

/**
 * Created by qingcha on 17/3/2.
 */
public class TravelAddItemModel extends EpoxyModel<TravelAddItemView> {

    private TravelAddItemView.OnAddTravelListener listener;

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_travel_add_item;
    }

    @Override
    public boolean shouldSaveViewState() {
        return true;
    }

    public void setOnAddTravelListener(TravelAddItemView.OnAddTravelListener listener) {
        this.listener = listener;
    }

    @Override
    public void bind(TravelAddItemView view) {
        super.bind(view);
        view.setOnAddTravelListener(listener);
    }
}