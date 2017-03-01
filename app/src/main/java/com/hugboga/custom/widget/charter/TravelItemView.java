package com.hugboga.custom.widget.charter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ChooseDateBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.utils.CharterDataUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/2/28.
 */
public class TravelItemView extends LinearLayout {

    @Bind(R.id.travel_item_edit_iv)
    ImageView travelItemEditIv;
    @Bind(R.id.travel_item_edit_tv)
    TextView travelItemEditTv;
    @Bind(R.id.travel_item_edit_line_view)
    View travelItemEditLineView;
    @Bind(R.id.travel_item_del_tv)
    TextView travelItemDelTv;
    @Bind(R.id.travel_item_edit_layout)
    LinearLayout travelItemEditLayout;

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
    @Bind(R.id.travel_item_time_layout)
    RelativeLayout travelItemTimeLayout;

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

    private CharterDataUtils charterDataUtils;

    public TravelItemView(Context context) {
        this(context, null);
    }

    public TravelItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_travel_list_item, this);
        ButterKnife.bind(view);
        this.charterDataUtils = CharterDataUtils.getInstance();
    }

    public void update(int _position) {
        ArrayList<CityRouteBean.CityRouteScope> travelList = charterDataUtils.travelList;

        ChooseDateBean chooseDateBean = charterDataUtils.params.chooseDateBean;//start
//            travelItemDataTv.setText();  DataUtils.getNextDay

        final int travelListSize = travelList.size();
        if (_position < travelListSize) {
            CityRouteBean.CityRouteScope cityRouteScope = travelList.get(_position);
            travelItemTitleTv.setText(cityRouteScope.routeTitle);//标题

            if (_position == 0) {//第一天（只有接机，只有"编辑"状态）
                if (cityRouteScope.routeType == CityRouteBean.RouteType.SEND) {//只接机
                    travelItemPickupLayout.setVisibility(View.VISIBLE);
                    travelItemPickupTv.setText(charterDataUtils.flightBean.flightNo);//航班号
                    travelItemArrdateTv.setText(charterDataUtils.flightBean.arrDate);//计划到达时间（当地时间：2017-02-18 12:40降落）

                    travelItemStartLayout.setVisibility(View.VISIBLE);
                    travelItemStartDesTv.setVisibility(View.GONE);
                    //先的高度改变
                    travelItemStartTv.setText(charterDataUtils.flightBean.arrAirportName);//到达机场

                    travelItemEndLayout.setVisibility(View.VISIBLE);
                    travelItemEndDesTv.setVisibility(View.VISIBLE);
                    travelItemEndTv.setText(charterDataUtils.pickUpPoiBean.placeName);//到达地
                    travelItemEndDesTv.setText(charterDataUtils.pickUpPoiBean.placeName);//到达地描述
                } else if (charterDataUtils.isSelectedPickUp) {//包车加接机

                } else {//只包车

                }
            } else if (_position == travelListSize - 1) {//最后一天（有送机，"编辑 | 删除"）
                if (cityRouteScope.routeType == CityRouteBean.RouteType.PICKUP) {//只送机

                } else if (charterDataUtils.isSelectedSend) {//包车加送机

                } else {//只包车

                }
            } else {//包车、或随便转转（只有"编辑"）

            }
        } else {

        }
    }
}
