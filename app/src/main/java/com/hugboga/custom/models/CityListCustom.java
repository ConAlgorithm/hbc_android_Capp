package com.hugboga.custom.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyModel;
import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/4/14.
 */
public class CityListCustom extends EpoxyModel<View> {

    @Bind(R.id.city_custom_title_tv)
    TextView titleTV;

    @Bind(R.id.city_custom_charter_layout)
    RelativeLayout charterLayout;
    @Bind(R.id.city_custom_charter_desc_tv)
    TextView charterDescTV;
    @Bind(R.id.city_custom_charter_desc2_tv)
    TextView charterDesc2TV;

    @Bind(R.id.city_custom_line_view)
    View lineView;

    @Bind(R.id.city_custom_bottom_layout)
    LinearLayout bottomLayout;

    @Override
    protected int getDefaultLayout() {
        return R.layout.view_city_custom;
    }

    @Override
    public void bind(View view) {
        super.bind(view);
        ButterKnife.bind(view);
        //city_custom_charter_layout
    }

    @OnClick({R.id.city_custom_charter_layout})
    public void intentCharter() {

    }

//    @OnClick({R.id.city_custom_charter_layout})
//    public void intentPickSend() {
//
//    }
//
//    @OnClick({R.id.city_custom_charter_layout})
//    public void intentPickSend() {
//
//    }
}
