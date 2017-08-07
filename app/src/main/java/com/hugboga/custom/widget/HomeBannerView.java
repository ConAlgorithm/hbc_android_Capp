package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.Bind;

/**
 * Created by zhangqiang on 17/8/5.
 */

public class HomeBannerView extends HomeActivitiesView {
    @Bind(R.id.indicator)
    CirclePageIndicator circlePageIndicator;
    public HomeBannerView(Context context) {
        this(context,null);
    }

    public HomeBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(circlePageIndicator.getLayoutParams());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL |RelativeLayout.ALIGN_PARENT_BOTTOM);
        circlePageIndicator.setLayoutParams(params);
    }
}
