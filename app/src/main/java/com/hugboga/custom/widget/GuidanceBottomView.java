package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

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


    public GuidanceBottomView(Context context) {
        this(context, null);
    }

    public GuidanceBottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_guidance_order_bottom, this);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.guidance_bottom_switch_pickup_tv, R.id.guidance_bottom_switch_send_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.guidance_bottom_switch_pickup_tv:
                switchPickupTV.setBackgroundResource(R.drawable.bg_guidance_order_send);
                switchPickupTV.setTextColor(getContext().getResources().getColor(R.color.default_black));
                switchSendTV.setBackgroundColor(0x00000000);
                switchSendTV.setTextColor(0xFF7f7f7f);
                infoView.setTitle("您的航班号？");
                infoView.setHintText("请输入航班号，如CA167");
                break;
            case R.id.guidance_bottom_switch_send_tv:
                switchPickupTV.setBackgroundColor(0x00000000);
                switchPickupTV.setTextColor(0xFF7f7f7f);
                switchSendTV.setBackgroundResource(R.drawable.bg_guidance_order_send);
                switchSendTV.setTextColor(getContext().getResources().getColor(R.color.default_black));
                infoView.setTitle("送您到哪个机场?");
                infoView.setHintText("请选择送达机场");
                break;
        }
    }

    public void setOrderType(int orderType) {
        switch (orderType) {
            case 1:
            case 2:
                switchLayout.setVisibility(View.VISIBLE);
                switchPickupTV.performClick();
                break;
            case 3:
            case 888:
                switchLayout.setVisibility(View.GONE);
                infoView.setTitle("想去哪个城市包车?");
                infoView.setHintText("请选择包车开始城市");
                break;
            case 4:
                switchLayout.setVisibility(View.GONE);
                infoView.setTitle("从哪个地点上车?");
                infoView.setHintText("请选择用车城市");
                break;
        }
    }
}
