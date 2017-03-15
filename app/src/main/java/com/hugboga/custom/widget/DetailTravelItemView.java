package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OrderBean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/16.
 */

public class DetailTravelItemView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.travel_item_edit_iv)
    ImageView travelItemEditIv;
    @Bind(R.id.travel_item_edit_tv)
    TextView travelItemEditTv;
    @Bind(R.id.travel_item_edit_line_view)
    View travelItemEditLineView;
    @Bind(R.id.travel_item_del_tv)
    TextView travelItemDelTv;

    @Bind(R.id.travel_item_title_tv)
    TextView travelItemTitleTv;
    @Bind(R.id.travel_item_data_tv)
    TextView travelItemDataTv;

    @Bind(R.id.travel_item_pickup_no_iv)
    ImageView travelItemPickupNoIv;
    @Bind(R.id.travel_item_pickup_tv)
    TextView travelItemPickupTv;
    @Bind(R.id.travel_item_arrdate_tv)
    TextView travelItemArrdateTv;
    @Bind(R.id.travel_item_pickup_layout)
    RelativeLayout travelItemPickupLayout;

    @Bind(R.id.travel_item_charter_line_iv)
    ImageView travelItemCharterLineIv;
    @Bind(R.id.travel_item_line_tv)
    TextView travelItemLineTv;
    @Bind(R.id.travel_item_line_time_tv)
    TextView travelItemLineTimeTv;
    @Bind(R.id.travel_item_line_distance_tv)
    TextView travelItemLineDistanceTv;
    @Bind(R.id.travel_item_line_tag_layout)
    LinearLayout travelItemLineTagLayout;
    @Bind(R.id.travel_item_charter_line_layout)
    RelativeLayout travelItemCharterLineLayout;

    @Bind(R.id.travel_item_time_iv)
    ImageView travelItemTimeIv;
    @Bind(R.id.travel_item_time_tv)
    TextView travelItemTimeTv;
    @Bind(R.id.travel_item_time_hint_tv)
    TextView travelItemTimeHintTv;
    @Bind(R.id.travel_item_time_layout)
    RelativeLayout travelItemTimeLayout;

    @Bind(R.id.travel_item_start_line_view)
    View travelItemStartLineIv;
    @Bind(R.id.travel_item_start_tv)
    TextView travelItemStartTv;
    @Bind(R.id.travel_item_start_des_tv)
    TextView travelItemStartDesTv;
    @Bind(R.id.travel_item_start_layout)
    RelativeLayout travelItemStartLayout;

    @Bind(R.id.travel_item_end_tv)
    TextView travelItemEndTv;
    @Bind(R.id.travel_item_end_des_tv)
    TextView travelItemEndDesTv;
    @Bind(R.id.travel_item_end_layout)
    RelativeLayout travelItemEndLayout;

    public DetailTravelItemView(Context context) {
        this(context, null);
    }

    public DetailTravelItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_travel_list_item, this);
        ButterKnife.bind(view);
        travelItemTitleTv.setTextColor(getResources().getColor(R.color.default_black));
        travelItemEditIv.setVisibility(View.GONE);
        travelItemEditTv.setVisibility(View.GONE);
        travelItemEditLineView.setVisibility(View.GONE);
        travelItemDelTv.setVisibility(View.GONE);
    }

    @Override
    public void update(Object _data) {
        OrderBean.JourneyItem data = (OrderBean.JourneyItem) _data;
        String title = "";
        if (data.pickup != null && data.journey != null) {//接机+包车

        } else if (data.pickup != null && data.journey == null) {//只接机

        } else if (data.transfer != null && data.journey != null) {//送机+包车

        } else if (data.transfer != null && data.journey == null) {//只送机

        } else if (data.journey != null) {
            if (TextUtils.isEmpty(data.journey.startCityName)) {//随便转转

            } else if (data.journey.startCityId != data.journey.cityId && data.journey.cityId > 0) {//跨城市

            } else {//包车

            }
        }
//        travelItemTitleTv.setText(String.format("Day%1$s: %2$s", "" + data.day, startCityBean.name));//标题
    }
}
