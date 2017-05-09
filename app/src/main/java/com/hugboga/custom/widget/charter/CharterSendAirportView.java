package com.hugboga.custom.widget.charter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.PoiSearchActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.DirectionBean;
import com.hugboga.custom.data.bean.PoiBean;
import com.hugboga.custom.utils.CharterDataUtils;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.UIUtils;

import java.text.ParseException;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.TimePicker;

/**
 * Created by qingcha on 17/2/24.
 */

public class CharterSendAirportView extends LinearLayout{

    private Context context;

    @Bind(R.id.charter_pickup_item_root_layout)
    RelativeLayout rootLayout;
    @Bind(R.id.charter_pickup_item_selected_iv)
    ImageView selectedIV;
    @Bind(R.id.charter_pickup_item_bottom_space_view)
    View bottomSpaceView;

    @Bind(R.id.charter_pickup_item_title_tv)
    TextView titleTV;

    @Bind(R.id.charter_pickup_item_add_layout)
    LinearLayout addLayout;

    @Bind(R.id.charter_pickup_item_add_address_layout)
    LinearLayout addAddressLayout;
    @Bind(R.id.charter_pickup_item_add_address_tv)
    TextView addAddressTV;

    @Bind(R.id.charter_pickup_item_add_address_layout2)
    LinearLayout addAddressLayout2;
    @Bind(R.id.charter_pickup_item_add_address_tv2)
    TextView addAddressTV2;

    @Bind(R.id.charter_pickup_item_add_time_layout)
    LinearLayout addTimeLayout;
    @Bind(R.id.charter_pickup_item_add_time_tv)
    TextView addTimeTV;

    @Bind(R.id.charter_pickup_item_address_layout)
    RelativeLayout addressLayout;
    @Bind(R.id.charter_pickup_item_address_tv)
    TextView addressTV;
    @Bind(R.id.charter_pickup_item_address_des_tv)
    TextView addressDesTV;

    @Bind(R.id.charter_pickup_item_time_layout)
    RelativeLayout timeLayout;
    @Bind(R.id.charter_pickup_item_time_tv)
    TextView timeTV;

    @Bind(R.id.charter_pickup_item_distance_tv)
    TextView distanceTV;

    private CharterDataUtils charterDataUtils;
    private TimePicker picker;
    private String serverTime = "00:00";

    public CharterSendAirportView(Context context) {
        this(context, null);
    }

    public CharterSendAirportView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setPadding(UIUtils.dip2px(8), UIUtils.dip2px(5), UIUtils.dip2px(8), UIUtils.dip2px(5));
        View view = inflate(context, R.layout.view_charter_pickup_item, this);
        ButterKnife.bind(view);
        charterDataUtils = CharterDataUtils.getInstance();

        titleTV.setText("只送机，不包车游玩");
        addTimeTV.setText("选择出发时间");
        addAddressTV.setText("添加出发地点");
        addAddressTV2.setText("添加出发地点");
    }

    public void update() {
        if (isSelected()) {
            rootLayout.setBackgroundResource(R.drawable.shape_rounded_charter_selected);
            selectedIV.setVisibility(View.VISIBLE);
            bottomSpaceView.setVisibility(View.VISIBLE);

            String sendServerTime = charterDataUtils.sendServerTime;
            PoiBean sendPoiBean = charterDataUtils.sendPoiBean;

            boolean isAddTime = !TextUtils.isEmpty(sendServerTime);
            boolean isAddPoi = sendPoiBean != null;

            LinearLayout addAddressLayoutLoc = this.addAddressLayout;
            if (!isAddTime && !isAddPoi) {
                addLayout.setVisibility(View.VISIBLE);
                addAddressLayoutLoc = this.addAddressLayout2;
                addAddressLayout.setVisibility(View.GONE);
            } else if (!isAddTime) {
                addLayout.setVisibility(View.VISIBLE);
                addAddressLayoutLoc = this.addAddressLayout2;
                addAddressLayout.setVisibility(View.GONE);
            } else if (!isAddPoi) {
                addLayout.setVisibility(View.GONE);
                addAddressLayoutLoc = this.addAddressLayout;
            } else {
                addLayout.setVisibility(View.GONE);
            }

            if (isAddTime) {
                addTimeLayout.setVisibility(View.GONE);
                timeLayout.setVisibility(View.VISIBLE);
                timeTV.setVisibility(View.VISIBLE);
                timeTV.setText(String.format("出发时间：%1$s", sendServerTime));
            } else {
                addTimeLayout.setVisibility(View.VISIBLE);
                timeLayout.setVisibility(View.GONE);
                timeTV.setVisibility(View.GONE);
            }

            if (isAddPoi) {
                addAddressLayoutLoc.setVisibility(View.GONE);
                addressLayout.setVisibility(View.VISIBLE);
                addressDesTV.setVisibility(View.VISIBLE);

                addressTV.setText(sendPoiBean.placeName);
                addressDesTV.setText(sendPoiBean.placeDetail);

                DirectionBean sendDirectionBean = charterDataUtils.sendDirectionBean;
                if (charterDataUtils.sendDirectionBean == null || sendDirectionBean.distanceDesc == null || sendDirectionBean.durationDesc == null) {
                    distanceTV.setVisibility(View.GONE);
                }else {
                    distanceTV.setVisibility(View.VISIBLE);
                    distanceTV.setText(String.format("距离：%1$s  时长：约%2$s", sendDirectionBean.distanceDesc, sendDirectionBean.durationDesc));
                }
            } else {
                addAddressLayoutLoc.setVisibility(View.VISIBLE);
                addressLayout.setVisibility(View.GONE);
                addressDesTV.setVisibility(View.GONE);
                distanceTV.setVisibility(View.GONE);
            }
        } else {
            rootLayout.setBackgroundResource(R.drawable.shape_rounded_charter_unselected);
            selectedIV.setVisibility(View.GONE);
            bottomSpaceView.setVisibility(View.GONE);

            addLayout.setVisibility(View.GONE);
            addAddressLayout.setVisibility(View.GONE);
            addTimeLayout.setVisibility(View.GONE);
            addressLayout.setVisibility(View.GONE);
            addressDesTV.setVisibility(View.GONE);
            timeLayout.setVisibility(View.GONE);
            distanceTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        update();
    }

    @OnClick({R.id.charter_pickup_item_add_address_layout,
            R.id.charter_pickup_item_add_address_layout2,
            R.id.charter_pickup_item_address_layout,
            R.id.charter_pickup_item_address_tv
            })
    public void intentPoiSearch() {
        if (charterDataUtils == null || charterDataUtils.airPortBean == null) {
            return;
        }
        Bundle bundle = new Bundle();
        if (context instanceof BaseActivity) {
            bundle.putString(Constants.PARAMS_SOURCE, ((BaseActivity) context).getEventSource());
        }
        bundle.putInt(PoiSearchActivity.KEY_CITY_ID, charterDataUtils.airPortBean.cityId);
        bundle.putString(PoiSearchActivity.KEY_LOCATION, charterDataUtils.airPortBean.location);
        Intent intent = new Intent(context, PoiSearchActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(PoiSearchActivity.PARAM_BUSINESS_TYPE, Constants.BUSINESS_TYPE_SEND);
        context.startActivity(intent);
    }

    @OnClick({R.id.charter_pickup_item_add_time_layout,
            R.id.charter_pickup_item_add_time_tv,
            R.id.charter_pickup_item_time_layout,
            R.id.charter_pickup_item_time_tv})
    public void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        try {
            if (!TextUtils.isEmpty(charterDataUtils.sendServerTime)) {
                serverTime = charterDataUtils.sendServerTime;
            }
            if (!"00:00".equals(serverTime)) {
                calendar.setTime(DateUtils.timeFormat.parse(serverTime + ":00"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (picker == null) {
            picker = new TimePicker((Activity) context, TimePicker.HOUR_24);
            picker.setTitleText("请选择上车时间");
            picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                @Override
                public void onTimePicked(String hour, String minute) {
                    serverTime = hour + ":" + minute;
                    charterDataUtils.sendServerTime = serverTime;
                    update();
                    picker.dismiss();
                }
            });
        }
        picker.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.show();
    }
}
