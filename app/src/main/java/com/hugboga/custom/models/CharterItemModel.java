package com.hugboga.custom.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.widget.charter.CharterItemView;

/**
 * Created by qingcha on 17/2/24.
 */
public class CharterItemModel extends EpoxyModel<CharterItemView> implements CharterModelBehavior{

    private CityRouteBean.CityRouteScope cityRouteScope;
    private View.OnClickListener clickListener;
    private boolean selected;
    private int position;

    @Override
    protected int getDefaultLayout() {
        return R.layout.model_charter_item;
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

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void bind(CharterItemView view) {
        super.bind(view);
        view.setCityRouteScope(cityRouteScope);
        view.setOnClickListener(clickListener);
        view.setSelected(selected);
        view.setTag(getRouteType());
    }
}
