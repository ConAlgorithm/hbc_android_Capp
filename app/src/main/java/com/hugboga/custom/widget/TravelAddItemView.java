package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/3/2.
 */

public class TravelAddItemView extends RelativeLayout {

    private OnAddTravelListener listener;

    public TravelAddItemView(Context context) {
        this(context, null);
    }

    public TravelAddItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, UIUtils.dip2px(14), 0, UIUtils.dip2px(40));
        View view = inflate(context, R.layout.view_travel_add_item, this);
        ButterKnife.bind(view);
    }

    @OnClick({R.id.travel_item_add_tv})
    public void addTravel() {
        if (listener != null) {
            listener.onAddTravel();
        }
    }

    public interface OnAddTravelListener {
        public void onAddTravel();
    }

    public void setOnAddTravelListener(OnAddTravelListener listener) {
        this.listener = listener;
    }
}
