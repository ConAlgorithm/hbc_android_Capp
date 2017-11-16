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
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.UIUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/16.
 */

public class DetailTravelItemView extends LinearLayout implements HbcViewBehavior{

    @BindView(R.id.travel_item_parent_layout)
    LinearLayout parentLayout;

    @BindView(R.id.travel_item_edit_iv)
    ImageView travelItemEditIv;
    @BindView(R.id.travel_item_edit_tv)
    TextView travelItemEditTv;
    @BindView(R.id.travel_item_edit_line_view)
    View travelItemEditLineView;
    @BindView(R.id.travel_item_del_tv)
    TextView travelItemDelTv;

    @BindView(R.id.travel_item_title_tv)
    TextView travelItemTitleTv;
    @BindView(R.id.travel_item_data_tv)
    TextView travelItemDataTv;

    @BindView(R.id.travel_item_pickup_no_iv)
    ImageView travelItemPickupNoIv;
    @BindView(R.id.travel_item_pickup_tv)
    TextView travelItemPickupTv;
    @BindView(R.id.travel_item_arrdate_tv)
    TextView travelItemArrdateTv;
    @BindView(R.id.travel_item_pickup_layout)
    RelativeLayout travelItemPickupLayout;

    @BindView(R.id.travel_item_charter_line_iv)
    ImageView travelItemCharterLineIv;
    @BindView(R.id.travel_item_charter_line_layout)
    RelativeLayout travelItemCharterLineLayout;
    @BindView(R.id.travel_item_scope_tv)
    TextView travelItemScopeTv;
    @BindView(R.id.travel_item_places_tv)
    TextView travelItemPlacesTv;

    @BindView(R.id.travel_item_scope_tv2)
    TextView travelItemScopeTv2;
    @BindView(R.id.travel_item_places_tv2)
    TextView travelItemPlacesTv2;

    @BindView(R.id.travel_item_line_tv)
    TextView travelItemLineTv;
    @BindView(R.id.travel_item_line_time_tv)
    TextView travelItemLineTimeTv;
    @BindView(R.id.travel_item_line_distance_tv)
    TextView travelItemLineDistanceTv;
    @BindView(R.id.travel_item_line_tag_layout)
    LinearLayout travelItemLineTagLayout;

    @BindView(R.id.travel_item_time_iv)
    ImageView travelItemTimeIv;
    @BindView(R.id.travel_item_time_tv)
    TextView travelItemTimeTv;
    @BindView(R.id.travel_item_time_hint_tv)
    TextView travelItemTimeHintTv;
    @BindView(R.id.travel_item_time_layout)
    RelativeLayout travelItemTimeLayout;

    @BindView(R.id.travel_item_start_line_view)
    View travelItemStartLineIv;
    @BindView(R.id.travel_item_start_tv)
    TextView travelItemStartTv;
    @BindView(R.id.travel_item_start_des_tv)
    TextView travelItemStartDesTv;
    @BindView(R.id.travel_item_start_layout)
    RelativeLayout travelItemStartLayout;

    @BindView(R.id.travel_item_end_tv)
    TextView travelItemEndTv;
    @BindView(R.id.travel_item_end_des_tv)
    TextView travelItemEndDesTv;
    @BindView(R.id.travel_item_end_layout)
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
        travelItemScopeTv.setVisibility(View.GONE);
        travelItemPlacesTv.setVisibility(View.GONE);

        parentLayout.setLayoutParams(new LinearLayout.LayoutParams(UIUtils.getScreenWidth(), LayoutParams.WRAP_CONTENT));
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
            title = data.journey.type == 3 ? data.journey.startCityName : data.journey.cityName;
            travelItemTimeLayout.setVisibility(View.GONE);
            travelItemStartLayout.setVisibility(View.GONE);
            travelItemEndLayout.setVisibility(View.GONE);
            updatePickupLayout(data.pickup, false, startDate);
            updateLineLayout(data.journey, data.pickup, null);
        } else if (data.pickup != null && data.journey == null) {//只接机
            title = data.pickup.serviceCityName;
            travelItemLineTagLayout.setVisibility(View.GONE);
            travelItemCharterLineLayout.setVisibility(View.GONE);
            travelItemTimeLayout.setVisibility(View.GONE);
            updatePickupLayout(data.pickup, true , startDate);
            updateOnlyPickupLayout(data.pickup);
        } else if (data.transfer != null && data.journey != null) {//送机+包车
            title = data.journey.type == 3 ? data.journey.startCityName : data.journey.cityName;
            travelItemCharterLineLayout.setVisibility(View.GONE);
            travelItemLineTagLayout.setVisibility(View.GONE);
            travelItemPickupLayout.setVisibility(View.GONE);

            travelItemStartLayout.setVisibility(View.GONE);
            travelItemEndLayout.setVisibility(View.GONE);
            travelItemTimeLayout.setVisibility(View.VISIBLE);
            travelItemTimeHintTv.setVisibility(View.GONE);
            travelItemTimeTv.setText("游玩结束送机: " + data.transfer.destAddress);
            updateLineLayout(data.journey, null, data.transfer);
        } else if (data.transfer != null && data.journey == null) {//只送机
            title = data.transfer.serviceCityName;
            travelItemCharterLineLayout.setVisibility(View.GONE);
            travelItemLineTagLayout.setVisibility(View.GONE);
            travelItemPickupLayout.setVisibility(View.GONE);
            travelItemTimeLayout.setVisibility(View.VISIBLE);
            travelItemTimeHintTv.setVisibility(View.GONE);
            travelItemScopeTv2.setVisibility(View.GONE);
            travelItemPlacesTv2.setVisibility(View.GONE);
            if (TextUtils.isEmpty(data.transfer.serviceTimeStr)) {
                travelItemTimeTv.setText("只送机");
            } else {
                travelItemTimeTv.setText(String.format("只送机，%1$s出发", data.transfer.serviceTimeStr));
            }
            updateSendLayout(data.transfer);
        } else if (data.journey != null) {
            travelItemPickupLayout.setVisibility(View.GONE);
            travelItemTimeLayout.setVisibility(View.GONE);
            travelItemStartLayout.setVisibility(View.GONE);
            travelItemEndLayout.setVisibility(View.GONE);
            if (data.journey.type != null && data.journey.type == 3) {//跨城市
                title = data.journey.startCityName + "-" + data.journey.cityName;
            } else {//包车
                title = data.journey.cityName;
            }
            updateLineLayout(data.journey, null, null);
        }
        travelItemTitleTv.setText(String.format("第%1$s天: %2$s", "" + data.day, title));//标题
    }

    public void updatePickupLayout(OrderBean.CTravelDayPickup pickup, boolean isOnly, String dateStr) {
        travelItemPickupLayout.setVisibility(View.VISIBLE);
        if (isOnly) {
            travelItemPickupTv.setText("只接机，航班:" + pickup.flightNo);//只接机，航班：NH956
        } else {
            travelItemPickupTv.setText("接机航班: " + pickup.flightNo);//接机航班：NH956
        }
        String serviceTime = pickup.serviceTimeStr;
        Date date = DateUtils.getDateTimeFromStr2(pickup.flightArriveTime);
        if (date != null) {
            serviceTime = DateUtils.getTime(date);
        }
        travelItemArrdateTv.setText(String.format("当地时间%1$s %2$s降落", dateStr, serviceTime));//计划到达时间（当地时间：2017年02月18日 周五 12:40降落）
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

    public void updateLineLayout(OrderBean.CJourneyInfo journey, OrderBean.CTravelDayPickup pickup, OrderBean.CTravelDayTransfer transfer) {
        travelItemLineTagLayout.setVisibility(View.VISIBLE);
        travelItemCharterLineLayout.setVisibility(View.VISIBLE);
        travelItemLineTv.setTextColor(getResources().getColor(R.color.default_black));
        travelItemScopeTv.setVisibility(View.GONE);
        travelItemPlacesTv.setVisibility(View.GONE);
        travelItemScopeTv2.setVisibility(View.GONE);
        travelItemPlacesTv2.setVisibility(View.GONE);
        if (isOuttown(journey)) {
            if (pickup != null) {
                travelItemLineTv.setText(String.format("%1$s出发，游玩至%2$s结束", journey.startCityName, journey.cityName));
            } else {
                travelItemLineTv.setText(String.format("%1$s出发，%2$s结束", journey.startCityName, journey.cityName));
            }
        } else {
            if (pickup != null) {
                travelItemLineTv.setText(String.format("%1$s出发，%2$s", pickup.startAddress, journey.description));
            } else {
                travelItemLineTv.setText(journey.description);
            }
        }
        boolean isSend = transfer != null;

        TextView scopeTV = isSend ? travelItemScopeTv2: travelItemScopeTv;
        TextView placesTV = isSend ? travelItemPlacesTv2: travelItemPlacesTv;
        if (!TextUtils.isEmpty(journey.scopeDesc)) {
            scopeTV.setText(journey.scopeDesc);
            scopeTV.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(journey.scenicDesc)) {
            placesTV.setText(journey.scenicDesc);
            placesTV.setVisibility(View.VISIBLE);
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
        travelItemEndTv.setText(transfer.destAddress);//送达机场
    }

    public boolean isOuttown(OrderBean.CJourneyInfo journey) {
        if (journey.type == 3) {
            return true;
        } else {
            return false;
        }
    }

}
