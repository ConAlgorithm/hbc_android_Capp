package com.hugboga.custom.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.charter.CharterPickupView;
import com.hugboga.custom.widget.charter.CharterSendAirportView;

/**
 * Created by qingcha on 17/2/24.
 */
public class CharterSendModel extends EpoxyModel<CharterSendAirportView> implements CharterModelBehavior{

    private boolean selected;
    private int tag;
    private View.OnClickListener clickListener;

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_charter_send_airport;
    }

    @Override
    public boolean shouldSaveViewState() {
        return true;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public int getTag() {
        return tag;
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void bind(CharterSendAirportView view) {
        super.bind(view);
        view.setOnClickListener(clickListener);
        view.setSelected(selected);
        view.setTag(tag);
        view.update();
    }
}