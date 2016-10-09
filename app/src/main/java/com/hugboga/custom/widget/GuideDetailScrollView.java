package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by on 16/10/8.
 */
public class GuideDetailScrollView extends ScrollView {

    private int cityBgHeight;

    private RelativeLayout titlebar;
    private TextView titleTV;

    public GuideDetailScrollView(Context context) {
        this(context, null);
    }

    public GuideDetailScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTitlebar(RelativeLayout titlebar, int cityBgHeight) {
        this.titlebar = titlebar;
        this.cityBgHeight = cityBgHeight;
        titlebar.setBackgroundColor(0x00000000);
        titleTV = (TextView) titlebar.findViewById(R.id.header_detail_title_tv);
        titleTV.setTextColor(0x00000000);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (titlebar == null) {
            return;
        }
        if (scrollY < 0) {
            scrollY = 0;
        }
        if (scrollY <= cityBgHeight) {
            float alpha = 0.0f;
            if (scrollY <= 0) {
                alpha = 0.0f;
            } else {
                alpha = Math.min(1, (float) scrollY / cityBgHeight);
            }
            titlebar.setBackgroundColor(UIUtils.getColorWithAlpha(alpha, 0xFF2D2B28));
            titleTV.setTextColor(UIUtils.getColorWithAlpha(alpha, 0xFFFFFFFF));
        } else {
            titlebar.setBackgroundColor(0xFF2D2B28);
            titleTV.setTextColor(0xFFFFFFFF);
        }
    }
}