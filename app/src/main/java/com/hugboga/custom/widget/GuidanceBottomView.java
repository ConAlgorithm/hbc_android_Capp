package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.GuidanceOrderActivity;
import com.hugboga.custom.statistic.sensors.SensorsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/12/7.
 */

public class GuidanceBottomView extends LinearLayout {

    @BindView(R.id.guidance_bottom_title_tv)
    TextView titleTV;
    @BindView(R.id.guidance_bottom_switch_pickup_tv)
    TextView switchPickupTV;
    @BindView(R.id.guidance_bottom_switch_send_tv)
    TextView switchSendTV;
    @BindView(R.id.guidance_bottom_confirm_tv)
    TextView confirmTV;

    private OnInfoViewClickListener listener;
    private boolean isPickup;
    private int orderType;

    public GuidanceBottomView(Context context) {
        this(context, null);
    }

    public GuidanceBottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_guidance_order_bottom, this);
        ButterKnife.bind(this);
        confirmTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int _orderType = orderType;
                    if (_orderType == 1 || _orderType == 2) {
                        _orderType = isPickup ? 1 : 2;
                    }
                    listener.onInfoViewClicked(_orderType);
                }
            }
        });
    }

    @OnClick({R.id.guidance_bottom_switch_pickup_tv, R.id.guidance_bottom_switch_send_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.guidance_bottom_switch_pickup_tv:
                onClickPickup();
                SensorsUtils.onAppClick(getEventSource(), "接机", getIntentSource());
                break;
            case R.id.guidance_bottom_switch_send_tv:
                onClickSend();
                SensorsUtils.onAppClick(getEventSource(), "送机", getIntentSource());
                break;
        }
    }

    private void onClickPickup() {
        switchPickupTV.setBackgroundResource(R.drawable.bg_guidance_order_send);
        switchPickupTV.setTextColor(getContext().getResources().getColor(R.color.default_black));
        switchSendTV.setBackgroundColor(0x00000000);
        switchSendTV.setTextColor(0xFF7f7f7f);
        confirmTV.setText(getContext().getResources().getString(R.string.guidance_pickup_hint_subtitle));
        isPickup = true;
    }

    private void onClickSend() {
        switchPickupTV.setBackgroundColor(0x00000000);
        switchPickupTV.setTextColor(0xFF7f7f7f);
        switchSendTV.setBackgroundResource(R.drawable.bg_guidance_order_send);
        switchSendTV.setTextColor(getContext().getResources().getColor(R.color.default_black));
        confirmTV.setText(getContext().getResources().getString(R.string.guidance_send_hint_subtitle));
        isPickup = false;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
        setPickupOrSendBottomUI(orderType);
        switch (orderType) {
            case 1:
                onClickPickup();
                break;
            case 2:
                onClickSend();
                break;
            case 3:
            case 888:
                titleTV.setText(getContext().getResources().getString(R.string.guidance_charter_hint_title));
                confirmTV.setText(getContext().getResources().getString(R.string.guidance_charter_hint_subtitle));
                break;
            case 4:
                titleTV.setText(getContext().getResources().getString(R.string.guidance_single_hint_title));
                confirmTV.setText(getContext().getResources().getString(R.string.guidance_single_hint_subtitle));
                break;
        }
    }

    private void setPickupOrSendBottomUI(int orderType) {
        if (orderType == 1 || orderType == 2) {
            titleTV.setVisibility(View.GONE);
            switchPickupTV.setVisibility(View.VISIBLE);
            switchSendTV.setVisibility(View.VISIBLE);
        } else {
            titleTV.setVisibility(View.VISIBLE);
            switchPickupTV.setVisibility(View.GONE);
            switchSendTV.setVisibility(View.GONE);
        }
    }

    public interface OnInfoViewClickListener {
        public void onInfoViewClicked(int orderType);
    }

    public void setOnInfoViewClickListener(OnInfoViewClickListener listener) {
        this.listener = listener;
    }

    public String getIntentSource() {
        if (getContext() instanceof GuidanceOrderActivity) {
            return ((GuidanceOrderActivity) getContext()).getIntentSource();
        } else {
          return "";
        }
    }

    public String getEventSource() {
        if (getContext() instanceof GuidanceOrderActivity) {
            return ((GuidanceOrderActivity) getContext()).getEventSource();
        } else {
            return "";
        }
    }
}
