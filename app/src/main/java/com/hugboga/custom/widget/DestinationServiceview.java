package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.IntentUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class DestinationServiceview extends LinearLayout implements HbcViewBehavior {


    public DestinationServiceview(Context context) {
        super(context);
    }

    public DestinationServiceview(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.des_service_layout, this);
        ButterKnife.bind(view);
    }

    public DestinationServiceview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @OnClick({R.id.picksend_id, R.id.single_id, R.id.day_service})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.picksend_id:
                IntentUtils.intentPickupActivity(getContext(), getEventSource());
                MobClickUtils.onEvent(StatisticConstant.LAUNCH_J);
                SensorsUtils.onAppClick(getEventSource(), "接机", "");
                break;
            case R.id.single_id:
                IntentUtils.intentSingleActivity(getContext(), getEventSource());
                MobClickUtils.onEvent(StatisticConstant.LAUNCH_C);
                SensorsUtils.onAppClick(getEventSource(), "单次接送", "");
                break;
            case R.id.day_service:
                IntentUtils.intentCharterActivity(getContext(), getEventSource());
                MobClickUtils.onEvent(StatisticConstant.LAUNCH_DETAIL_R);
                SensorsUtils.onAppClick(getEventSource(), "按天包车游", "");
                break;
        }

    }

    @Override
    public void update(Object _data) {

    }

    public String getEventSource() {
        return "目的地";
    }
}
