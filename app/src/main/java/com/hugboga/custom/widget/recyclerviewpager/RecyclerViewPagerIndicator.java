package com.hugboga.custom.widget.recyclerviewpager;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

public class RecyclerViewPagerIndicator extends LinearLayout {

    private int itemCount;
    private LinearLayout.LayoutParams itemParams;

    public RecyclerViewPagerIndicator(Context context) {
        this(context, null);
    }

    public RecyclerViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        itemParams = new LinearLayout.LayoutParams(UIUtils.dip2px(5), UIUtils.dip2px(5));
        itemParams.leftMargin = UIUtils.dip2px(6);
        itemParams.rightMargin = UIUtils.dip2px(6);
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
        while (getChildCount() < itemCount) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(R.drawable.bg_recycler_viewpager_indicator);
            addView(imageView, itemParams);
        }
        int count = getChildCount();
        for (int i = itemCount; i < count; i++) {
            getChildAt(itemCount).setVisibility(View.GONE);
        }
    }

    public void onPageChanged(int selectedPosition) {
        // 为了解决LoopRecyclerViewPager itemCount = 2时的切换的bug
        // RVViewUtils.setDataCompat(mAdapter, itemList);
        int position = selectedPosition;
        if (itemCount == 2 && position > 1) {
            position = position % 2;
        }
        for (int i = 0; i < itemCount; i++) {
            getChildAt(i).setSelected(i == position);
        }
    }
}
