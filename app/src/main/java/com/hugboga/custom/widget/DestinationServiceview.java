package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hugboga.custom.R;

import butterknife.ButterKnife;

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

    @Override
    public void update(Object _data) {

    }
}
