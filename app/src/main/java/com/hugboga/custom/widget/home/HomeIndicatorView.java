package com.hugboga.custom.widget.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/11/24.
 */

public class HomeIndicatorView extends LinearLayout {

    @BindView(R.id.home_indicator_index_tv)
    TextView indexTV;
    @BindView(R.id.home_indicator_count_tv)
    TextView countTV;

    private int itemCount;

    public HomeIndicatorView(Context context) {
        this(context, null);
    }

    public HomeIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        inflate(context, R.layout.view_home_indicator, this);
        ButterKnife.bind(this);
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
        countTV.setText("" + itemCount);
    }

    public void setCurrentPosition(int selectedPosition) {
        int position = selectedPosition;
        // 为了解决LoopRecyclerViewPager itemCount = 2时的切换的bug
        // RVViewUtils.setDataCompat(mAdapter, itemList);
        if (itemCount == 2 && position > 1) {
            position = position % 2;
        }
        indexTV.setText("" + (position + 1));
    }
}
