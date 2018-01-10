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

    @BindView(R.id.guidance_bottom_switch_pickup_tv)
    TextView switchPickupTV;
    @BindView(R.id.guidance_bottom_switch_send_tv)
    TextView switchSendTV;
    @BindView(R.id.guidance_bottom_switch_layout)
    LinearLayout switchLayout;
    @BindView(R.id.guidance_bottom_info_view)
    OrderInfoItemView infoView;

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
        infoView.setOnClickListener(new OnClickListener() {
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
                switchPickupTV.setBackgroundResource(R.drawable.bg_guidance_order_send);
                switchPickupTV.setTextColor(getContext().getResources().getColor(R.color.default_black));
                switchSendTV.setBackgroundColor(0x00000000);
                switchSendTV.setTextColor(0xFF7f7f7f);
                infoView.setTitle(getContext().getResources().getString(R.string.guidance_pickup_hint_title));
                infoView.setHintText(getContext().getResources().getString(R.string.guidance_pickup_hint_subtitle));
                isPickup = true;
                SensorsUtils.onAppClick(getEventSource(), "接机", getIntentSource());
                break;
            case R.id.guidance_bottom_switch_send_tv:
                switchPickupTV.setBackgroundColor(0x00000000);
                switchPickupTV.setTextColor(0xFF7f7f7f);
                switchSendTV.setBackgroundResource(R.drawable.bg_guidance_order_send);
                switchSendTV.setTextColor(getContext().getResources().getColor(R.color.default_black));
                infoView.setTitle(getContext().getResources().getString(R.string.guidance_send_hint_title));
                infoView.setHintText(getContext().getResources().getString(R.string.guidance_send_hint_subtitle));
                isPickup = false;
                SensorsUtils.onAppClick(getEventSource(), "送机", getIntentSource());
                break;
        }
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
        switch (orderType) {
            case 1:
                switchLayout.setVisibility(View.VISIBLE);
                switchPickupTV.performClick();
                break;
            case 2:
                switchLayout.setVisibility(View.VISIBLE);
                switchSendTV.performClick();
                break;
            case 3:
            case 888:
                switchLayout.setVisibility(View.GONE);
                infoView.setTitle(getContext().getResources().getString(R.string.guidance_charter_hint_title));
                infoView.setHintText(getContext().getResources().getString(R.string.guidance_charter_hint_subtitle));
                break;
            case 4:
                switchLayout.setVisibility(View.GONE);
                infoView.setTitle(getContext().getResources().getString(R.string.guidance_single_hint_title));
                infoView.setHintText(getContext().getResources().getString(R.string.guidance_single_hint_subtitle));
                break;
        }
    }

    public OrderInfoItemView getInfoView() {
        return infoView;
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
