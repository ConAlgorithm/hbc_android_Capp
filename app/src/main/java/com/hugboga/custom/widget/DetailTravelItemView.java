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
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityRouteBean;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.UIUtils;

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
        String startDate = DateUtils.getWeekOfDate2(data.dateStr);
        travelItemDataTv.setText(startDate);
        String title = "";
        if (data.journey == null && data.pickup == null && data.transfer == null) {//随便转转
            title = "自己转转，不包车";
            travelItemCharterLineLayout.setVisibility(View.GONE);
            travelItemLineTagLayout.setVisibility(View.GONE);
            travelItemPickupLayout.setVisibility(View.GONE);
            travelItemTimeLayout.setVisibility(View.GONE);
            travelItemStartLayout.setVisibility(View.GONE);
            travelItemEndLayout.setVisibility(View.GONE);
        } else if (data.pickup != null && data.journey != null) {//接机+包车
            title = data.journey.startCityName;
            travelItemTimeLayout.setVisibility(View.GONE);
            travelItemStartLayout.setVisibility(View.GONE);
            travelItemEndLayout.setVisibility(View.GONE);
            updatePickupLayout(data.pickup, false, startDate);
            updateLineLayout(data.journey, data.pickup);
        } else if (data.pickup != null && data.journey == null) {//只接机
            title = data.pickup.serviceCityName;
            travelItemLineTagLayout.setVisibility(View.GONE);
            travelItemCharterLineLayout.setVisibility(View.GONE);
            travelItemTimeLayout.setVisibility(View.GONE);
            updatePickupLayout(data.pickup, true , startDate);
            updateOnlyPickupLayout(data.pickup);
        } else if (data.transfer != null && data.journey != null) {//送机+包车
            title = data.transfer.startAddress;
            travelItemCharterLineLayout.setVisibility(View.GONE);
            travelItemLineTagLayout.setVisibility(View.GONE);
            travelItemPickupLayout.setVisibility(View.GONE);

            travelItemStartLayout.setVisibility(View.GONE);
            travelItemEndLayout.setVisibility(View.GONE);
            travelItemTimeLayout.setVisibility(View.VISIBLE);
            travelItemTimeHintTv.setVisibility(View.GONE);
            travelItemTimeTv.setText("游玩结束送机: " + data.transfer.startAddress);
            updateLineLayout(data.journey, data.pickup);
        } else if (data.transfer != null && data.journey == null) {//只送机
            title = data.journey.startCityName;
            travelItemCharterLineLayout.setVisibility(View.GONE);
            travelItemLineTagLayout.setVisibility(View.GONE);
            travelItemPickupLayout.setVisibility(View.GONE);
            travelItemTimeLayout.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(data.transfer.serviceTimeStr)) {
                travelItemTimeHintTv.setVisibility(View.VISIBLE);
                travelItemTimeTv.setText("只送机，");
            } else {
                travelItemTimeHintTv.setVisibility(View.GONE);
                travelItemTimeTv.setText(String.format("只送机，%1$s出发", data.transfer.serviceTimeStr));
            }
            updateSendLayout(data.transfer);
        } else if (data.journey != null) {
            travelItemPickupLayout.setVisibility(View.GONE);
            travelItemTimeLayout.setVisibility(View.GONE);
            travelItemStartLayout.setVisibility(View.GONE);
            travelItemEndLayout.setVisibility(View.GONE);
            if (data.journey.startCityId != data.journey.cityId && data.journey.cityId > 0) {//跨城市
                title = data.journey.startCityName + "-" + data.journey.cityName;
            } else {//包车
                title = data.journey.startCityName;
            }
            updateLineLayout(data.journey, null);
        }
        travelItemTitleTv.setText(String.format("Day%1$s: %2$s", "" + data.day, title));//标题
    }

    public void updatePickupLayout(OrderBean.CTravelDayPickup pickup, boolean isOnly, String dateStr) {
        travelItemPickupLayout.setVisibility(View.VISIBLE);
        if (isOnly) {
            travelItemPickupTv.setText("只接机，航班:" + pickup.flightNo);//只接机，航班：NH956
        } else {
            travelItemPickupTv.setText("接机航班: " + pickup.flightNo);//接机航班：NH956
        }
        travelItemArrdateTv.setText(String.format("当地时间%1$s %2$s降落", dateStr, pickup.serviceTimeStr));//计划到达时间（当地时间：2017年02月18日 周五 12:40降落）
    }

    public void updateOnlyPickupLayout(OrderBean.CTravelDayPickup pickup) {
        travelItemStartLayout.setVisibility(View.VISIBLE);
        travelItemStartDesTv.setVisibility(View.GONE);
        travelItemStartTv.setText(pickup.startAddress);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIUtils.dip2px(1), UIUtils.dip2px(6));
        params.topMargin = UIUtils.dip2px(20);
        params.leftMargin = UIUtils.dip2px(7.5f);
        travelItemStartLineIv.setLayoutParams(params);

        travelItemEndLayout.setVisibility(View.VISIBLE);
        travelItemEndTv.setTextColor(getResources().getColor(R.color.default_black));
        travelItemEndTv.setText(pickup.destAddress);//到达地
        travelItemEndDesTv.setText(pickup.destAddressDetail);//到达地描述
        travelItemEndDesTv.setVisibility(View.VISIBLE);
    }

    public void updateLineLayout(OrderBean.CJourneyInfo journey, OrderBean.CTravelDayPickup pickup) {
        travelItemLineTagLayout.setVisibility(View.VISIBLE);
        travelItemCharterLineLayout.setVisibility(View.VISIBLE);
        travelItemLineTv.setTextColor(getResources().getColor(R.color.default_black));
        if (isOuttown(journey)) {
            String startAddress = journey.startCityName;
            if (pickup != null) {
                startAddress = pickup.startAddress;
            }
            travelItemLineTv.setText(String.format("%1$s出发，%2$s结束", startAddress, journey.cityName));
        } else {
            if (pickup != null) {
                travelItemLineTv.setText(String.format("%1$s出发，%2$s", pickup.startAddress, journey.description));
            } else {
                travelItemLineTv.setText(journey.description);
            }
        }
        travelItemLineTimeTv.setText(journey.getLabelTime());
        travelItemLineDistanceTv.setText(journey.getLabelKilometre());
    }

    public void updateSendLayout(OrderBean.CTravelDayTransfer transfer) {
        travelItemStartLayout.setVisibility(View.VISIBLE);
        travelItemStartTv.setTextColor(getResources().getColor(R.color.default_black));
        travelItemStartTv.setText(transfer.startAddress);//出发地点
        travelItemStartDesTv.setVisibility(View.VISIBLE);
        travelItemStartDesTv.setText(transfer.startAddressDetail);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIUtils.dip2px(1), UIUtils.dip2px(20));
        params.topMargin = UIUtils.dip2px(20);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.travel_item_start_des_tv);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.leftMargin = UIUtils.dip2px(7.5f);
        travelItemStartLineIv.setLayoutParams(params);

        travelItemEndLayout.setVisibility(View.VISIBLE);
        travelItemEndDesTv.setVisibility(View.GONE);
        travelItemEndTv.setText(transfer.startAddress);//送达机场
    }

    public boolean isOuttown(OrderBean.CJourneyInfo journey) {
        if (journey.startCityId != journey.cityId && journey.cityId > 0) {
            return true;
        } else {
            return false;
        }
    }

}
