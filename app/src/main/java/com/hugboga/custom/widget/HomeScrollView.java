package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeScrollView extends ScrollView {

    private int binnerHeight;
    private int searchLayoutHeight;

    private HomeSearchView searchView;
    private boolean isHide = true;

    public HomeScrollView(Context context) {
        this(context, null);
    }

    public HomeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        binnerHeight = (int)(UIUtils.getScreenWidth() * HomeBannerView.BANNER_RATIO_DEFAULT);
        searchLayoutHeight = context.getResources().getDimensionPixelOffset(R.dimen.home_search_layout_height);
    }

    public void setSearchView(HomeSearchView searchView) {
        this.searchView = searchView;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (searchView == null) {
            return;
        }
        if (scrollY < 0) {
            scrollY = 0;
        }
        if (scrollY >= binnerHeight - searchLayoutHeight && isHide) {
            searchView.setSearchLayoutHide(false);
            isHide = false;
        } else if (scrollY < binnerHeight - searchLayoutHeight && !isHide) {
            searchView.setSearchLayoutHide(true);
            isHide = true;
        }

        if (scrollY >= binnerHeight - searchLayoutHeight * 2) {
            float alpha = 0.0f;
            if(scrollY <= 0) {
                alpha = 0.0f;
            } else {
                alpha = Math.min(1, (float) (scrollY - (binnerHeight - searchLayoutHeight * 2)) / searchLayoutHeight);
            }
            searchView.setBackgroundColor(UIUtils.getColorWithAlpha(alpha, 0xFF2D2B28));
        } else {
            searchView.setBackgroundColor(0x00000000);
        }
    }
}
