package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.OrderActivity;
import com.hugboga.custom.data.bean.FlightBean;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/5/22.
 */
public class OrderDescriptionView extends LinearLayout {

    @BindView(R.id.order_desc_title_tv)
    TextView titleTV;

    @BindView(R.id.item_itinerary_sub_title_tv)
    TextView timeTV;
    @BindView(R.id.item_itinerary_sub_describe_tv)
    TextView flightTV;

    @BindView(R.id.order_travel_item_start_tv)
    TextView startTV;
    @BindView(R.id.order_travel_item_start_des_tv)
    TextView startDesTV;
    @BindView(R.id.order_travel_item_start_line_view)
    View startLineView;
    @BindView(R.id.order_travel_item_end_tv)
    TextView endTV;
    @BindView(R.id.order_travel_item_end_des_tv)
    TextView endDesTV;

    @BindView(R.id.item_itinerary_title_tv)
    TextView carDescTV;

    public OrderDescriptionView(Context context) {
        this(context, null);
    }

    public OrderDescriptionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_description, this);
        ButterKnife.bind(view);
        setOrientation(LinearLayout.VERTICAL);
        flightTV.setVisibility(View.GONE);
    }

    public void setData(OrderActivity.Params params) {
        switch (params.orderType) {
            case 1:
                titleTV.setText(R.string.order_pickup);

                FlightBean flightBean = params.flightBean;
                flightTV.setVisibility(View.VISIBLE);
                flightTV.setText(String.format("航班: %1$s %2$s-%3$s", flightBean.flightNo, flightBean.depCityName, flightBean.arrCityName));

                setAddress(flightBean.arrAirportName, null, params.endPoiBean.placeName, params.endPoiBean.placeDetail);
                break;
            case 2:
                titleTV.setText(R.string.order_send);

                setAddress(params.startPoiBean.placeName, params.startPoiBean.placeDetail, params.airPortBean.airportName, null);
                break;
            case 4:
                titleTV.setText(R.string.custom_single);

                setAddress(params.startPoiBean.placeName, params.startPoiBean.placeDetail, params.endPoiBean.placeName, params.endPoiBean.placeDetail);
                break;
        }

        setTime(params.serverDate, params.serverTime);
        carDescTV.setText(params.carBean.carDesc);
    }

    private void setTime(String date, String time) {
        timeTV.setText(DateUtils.getStrWeekFormat3(date) + " " + time);
    }

    private void setAddress(String startAddress, String startAddressDetail, String destAddress, String destAddressDetail) {
        startTV.setText(startAddress);
        if (!TextUtils.isEmpty(startAddressDetail)) {
            startDesTV.setVisibility(View.VISIBLE);
            startDesTV.setText(startAddressDetail);

            int showWidth = UIUtils.getScreenWidth() - (getContext().getResources().getDimensionPixelOffset(R.dimen.order_padding_left) * 2 + UIUtils.dip2px(24) + UIUtils.dip2px(3));
            int stringWidth = UIUtils.getStringWidth(startDesTV, startAddressDetail);
            int lines = (int)Math.ceil(stringWidth / showWidth);
            int startDesViewWidth = UIUtils.dip2px(20) + lines * UIUtils.dip2px(10);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIUtils.dip2px(2), startDesViewWidth);
            params.topMargin = UIUtils.dip2px(20);
            params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.travel_item_start_des_tv);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.leftMargin = UIUtils.dip2px(7.5f);
            startLineView.setLayoutParams(params);
        } else {
            startDesTV.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIUtils.dip2px(2), UIUtils.dip2px(5));
            params.topMargin = UIUtils.dip2px(20);
            params.leftMargin = UIUtils.dip2px(7.5f);
            startLineView.setLayoutParams(params);
        }
        endTV.setText(destAddress);
        if (!TextUtils.isEmpty(destAddressDetail)) {
            endDesTV.setVisibility(View.VISIBLE);
            endDesTV.setText(destAddressDetail);
        } else {
            endDesTV.setVisibility(View.GONE);
        }
    }
}
