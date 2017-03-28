package com.hugboga.custom.models;

import android.view.View;

import com.hugboga.custom.data.bean.CityRouteBean;

/**
 * Created by qingcha on 17/2/25.
 */
public interface CharterModelBehavior {

    public void setSelected(boolean selected);

    public boolean isSelected();

    public void setCityRouteScope(CityRouteBean.CityRouteScope cityRouteScope);

    public CityRouteBean.CityRouteScope getCityRouteScope();

    public void setOnClickListener(View.OnClickListener clickListener);

    public int getRouteType();
}
