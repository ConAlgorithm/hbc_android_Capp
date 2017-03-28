package com.hugboga.custom.models;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.charter.TravelItemView;

/**
 * Created by qingcha on 17/2/28.
 */
public class TravelItemModel extends EpoxyModel<TravelItemView> {

    private int position;
    private TravelItemView.OnEditClickListener listener;

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_travel_item;
    }

    @Override
    public boolean shouldSaveViewState() {
        return true;
    }

    public void setPosition(int _position) {
        this.position = _position;
    }

    public void setOnEditClickListener(TravelItemView.OnEditClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void bind(TravelItemView view) {
        super.bind(view);
        view.update(position);
        view.setOnEditClickListener(listener);
    }
}
