package com.hugboga.custom.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeScrollView extends ScrollView {

    private int binnerHeight;
    private int searchLayoutHeight;

    private FrameLayout searchLayout;
    private FrameLayout searchFloatLayout;
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

    public void setSearchView(FrameLayout searchLayout, FrameLayout searchFloatLayout, HomeSearchView searchView) {
        this.searchLayout = searchLayout;
        this.searchFloatLayout = searchFloatLayout;
        this.searchView = searchView;

        searchLayout.addView(searchView);
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
        if (scrollY >= binnerHeight && isHide) {
            searchLayout.removeAllViews();
            searchFloatLayout.addView(searchView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            searchView.setServiceViewVisibility(View.VISIBLE);
            isHide = false;
        } else if (scrollY < binnerHeight && !isHide) {
            searchFloatLayout.removeAllViews();
            searchLayout.addView(searchView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            searchView.setServiceViewVisibility(View.GONE);
            isHide = true;
        }
    }
}
