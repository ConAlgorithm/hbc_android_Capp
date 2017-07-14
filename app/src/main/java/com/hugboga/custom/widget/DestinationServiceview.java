package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.CharterFirstStepActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class DestinationServiceview extends LinearLayout implements HbcViewBehavior{


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

    @OnClick({R.id.picksend_id,R.id.single_id,R.id.day_service})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.picksend_id:
                intentActivity(getContext(), PickSendActivity.class, StatisticConstant.LAUNCH_J);

                break;
            case R.id.single_id:
                intentActivity(getContext(), SingleActivity.class, StatisticConstant.LAUNCH_C);
                break;
            case R.id.day_service:
                intentActivity(getContext(), CharterFirstStepActivity.class, StatisticConstant.LAUNCH_DETAIL_R);
                break;
        }

    }
    private void intentActivity(Context context, Class<?> cls, String eventId) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        context.startActivity(intent);
        if (!TextUtils.isEmpty(eventId)) {
            MobClickUtils.onEvent(eventId);
        }
    }
    @Override
    public void update(Object _data) {

    }
    public String getEventSource() {
        return "目的地";
    }
}
