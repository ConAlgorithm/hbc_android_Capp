package com.hugboga.custom.widget.charter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.ChooseCityActivity;
import com.hugboga.custom.activity.OrderSelectCityActivity;
import com.hugboga.custom.activity.PoiSearchActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.fragment.BaseFragment;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/2/24.
 */
public class CharterItemView extends LinearLayout{

    @Bind(R.id.charter_item_root_layout)
    RelativeLayout rootLayout;
    @Bind(R.id.charter_item_selected_iv)
    ImageView selectedIV;
    @Bind(R.id.charter_item_bottom_space_view)
    View bottomSpaceView;

    @Bind(R.id.charter_item_title_tv)
    TextView titleTV;
    @Bind(R.id.charter_item_scope_tv)
    TextView scopeTV;
    @Bind(R.id.charter_item_places_tv)
    TextView placesTV;
    @Bind(R.id.charter_item_time_tv)
    TextView timeTV;
    @Bind(R.id.charter_item_distance_tv)
    TextView distanceTV;
    @Bind(R.id.charter_item_tag_layout)
    LinearLayout tagLayout;

    @Bind(R.id.charter_item_edit_arrived_city_layout)
    RelativeLayout editArrivedCityLayout;
    @Bind(R.id.charter_item_edit_arrived_city_tv)
    TextView editArrivedCityTV;
    @Bind(R.id.charter_item_add_arrived_city_layout)
    LinearLayout addArrivedCityLayout;

    private Context context;
    private CharterDataUtils charterDataUtils;
    private CityRouteBean.CityRouteScope cityRouteScope;

    public CharterItemView(Context context) {
        this(context, null);
    }

    public CharterItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_charter_item, this);
        ButterKnife.bind(view);
        setPadding(UIUtils.dip2px(8), UIUtils.dip2px(5), UIUtils.dip2px(8), UIUtils.dip2px(5));
        charterDataUtils = CharterDataUtils.getInstance();
    }

    public void setCityRouteScope(CityRouteBean.CityRouteScope _cityRouteScope) {
        this.cityRouteScope = _cityRouteScope;
        if (_cityRouteScope.routeType == CityRouteBean.RouteType.AT_WILL) {
            titleTV.setText("自己转转，不包车");
        } else {
            titleTV.setText(cityRouteScope.routeTitle);
            timeTV.setText(String.format("%1$s小时", "" + cityRouteScope.routeLength));
            distanceTV.setText(String.format("%1$s公里", "" + cityRouteScope.routeKms));
            if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {//跨城市
//                scopeTV.setText("热门城市：" + cityRouteScope.routePlaces);

                CityBean cityBean = charterDataUtils.getNextDayCityBean();
                if (cityBean != null) {
                    editArrivedCityTV.setText("送达城市：" + cityBean.name);
                }
            } else {
                scopeTV.setText("范围：" + cityRouteScope.routeScope);
//                placesTV.setText("推荐景点：" + cityRouteScope.routePlaces);
            }
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (cityRouteScope.routeType == CityRouteBean.RouteType.OUTTOWN) {//跨城市
            bottomSpaceView.setVisibility(View.VISIBLE);
            scopeTV.setVisibility(View.VISIBLE);
            tagLayout.setVisibility(View.VISIBLE);
            placesTV.setVisibility(View.GONE);
            if (selected) {
                CityBean cityBean = charterDataUtils.getNextDayCityBean();
                if (cityBean == null) {
                    addArrivedCityLayout.setVisibility(View.VISIBLE);
                    editArrivedCityLayout.setVisibility(View.GONE);
                } else {
                    addArrivedCityLayout.setVisibility(View.GONE);
                    editArrivedCityLayout.setVisibility(View.VISIBLE);
                }
            } else {
                addArrivedCityLayout.setVisibility(View.GONE);
                editArrivedCityLayout.setVisibility(View.GONE);
            }
        } else if (cityRouteScope.routeType == CityRouteBean.RouteType.AT_WILL) {//随便转转，不包车
            scopeTV.setVisibility(View.GONE);
            placesTV.setVisibility(View.GONE);
            tagLayout.setVisibility(View.GONE);
            addArrivedCityLayout.setVisibility(View.GONE);
            editArrivedCityLayout.setVisibility(View.GONE);
            bottomSpaceView.setVisibility(View.GONE);
        } else {
            bottomSpaceView.setVisibility(View.VISIBLE);
            scopeTV.setVisibility(View.VISIBLE);
            tagLayout.setVisibility(View.VISIBLE);
            addArrivedCityLayout.setVisibility(View.GONE);
            editArrivedCityLayout.setVisibility(View.GONE);
            if (selected) {
                placesTV.setVisibility(View.VISIBLE);
            } else {
                placesTV.setVisibility(View.GONE);
            }
        }
        setBackgroundChange();
    }

    @OnClick({R.id.charter_item_edit_arrived_city_layout,
            R.id.charter_item_add_arrived_city_layout})
    public void intentPoiSearch() {
        if (charterDataUtils == null) {
            return;
        }
        CityBean cityBean = charterDataUtils.getCurrentDayCityBean();
        Intent intent = new Intent(getContext(), ChooseCityActivity.class);
        if (context instanceof BaseActivity) {
            intent.putExtra(Constants.PARAMS_SOURCE, ((BaseActivity) context).getEventSource());
        }
        intent.putExtra(ChooseCityActivity.KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_DAILY);
        intent.putExtra(ChooseCityActivity.KEY_CITY_ID, cityBean.cityId);
        intent.putExtra(ChooseCityActivity.KEY_FROM, "lastCity");
        getContext().startActivity(intent);
    }

    public void setBackgroundChange() {
        if (isSelected()) {
            rootLayout.setBackgroundResource(R.drawable.shape_rounded_charter_selected);
            selectedIV.setVisibility(View.VISIBLE);
        } else {
            rootLayout.setBackgroundResource(R.drawable.shape_rounded_charter_unselected);
            selectedIV.setVisibility(View.GONE);
        }
    }

}
