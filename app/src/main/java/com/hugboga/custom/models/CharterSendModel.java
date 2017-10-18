package com.hugboga.custom.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.widget.charter.CharterSendAirportView;

/**
 * Created by qingcha on 17/2/24.
 */
public class CharterSendModel extends EpoxyModel<CharterSendAirportView> implements CharterModelBehavior{

    private CityRouteBean.CityRouteScope cityRouteScope;
    private View.OnClickListener clickListener;
    private boolean selected;

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_charter_send_airport;
    }

    @Override
    public boolean shouldSaveViewState() {
        return true;
    }

    @Override
    public void setCityRouteScope(CityRouteBean.CityRouteScope cityRouteScope) {
        this.cityRouteScope = cityRouteScope;
    }

    @Override
    public CityRouteBean.CityRouteScope getCityRouteScope() {
        return cityRouteScope;
    }

    @Override
    public int getRouteType() {
        return cityRouteScope != null ? cityRouteScope.routeType : -1;
    }

    @Override
    public void setOnClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
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
    public void bind(CharterSendAirportView view) {
        super.bind(view);
        view.setOnClickListener(clickListener);
        view.setSelected(selected);
        view.update();
        view.setTag(getRouteType());
    }
}