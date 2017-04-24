package com.hugboga.custom.models;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;

/**
 * Created by qingcha on 17/4/19.
 */
public class CityListGuideHeaderModel extends EpoxyModel<LinearLayout> {

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_city_list_guide_header;
    }

    @Override
    public void bind(LinearLayout view) {
        super.bind(view);
        TextView moretv = (TextView) view.findViewById(R.id.city_guide_more_tv);
        moretv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}