package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.utils.DateUtils;
import com.hugboga.custom.utils.UIUtils;

import java.util.List;
/**
 * To be used with ViewPager to provide a tab indicator component which give constant feedback as to
 * the user's scroll progress.
 * <p>
 * To use the component, simply add it to your view hierarchy. Then in your
 * {@link android.app.Activity} or {@link android.support.v4.app.Fragment} call
 * {@link #setViewPager(ViewPager)} providing it the ViewPager this layout is being used for.
 * <p>
 * The colors can be customized in two ways. The first and simplest is to provide an array of colors
 * via {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)}. The
 * alternative is via the {@link TabColorizer} interface which provides you complete control over
 * which color is used for any individual position.
 * <p>
 * The views used as tabs can be customized by calling {@link #setCustomTabView(int, int)},
 * providing the layout ID of your custom layout.
 */

/**
 * Created by qingcha on 17/3/14.
 */
public class OrderDetailTravelTabLayout extends HorizontalScrollView {

    private List<OrderBean> items;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private int currentIndex = -1;
    private int restoreIndex = -1;

    private final OrderDetailTabStripView mTabStrip;

    public OrderDetailTravelTabLayout(Context context) {
        this(context, null);
    }

    public OrderDetailTravelTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrderDetailTravelTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTabStrip = new OrderDetailTabStripView(context);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link OrderDetailTabStripView} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager, OrderBean _data, int _currentIndex) {
        currentIndex = _currentIndex;
        mTabStrip.removeAllViews();
        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new InternalViewPagerListener());

            final PagerAdapter adapter = mViewPager.getAdapter();
            final View.OnClickListener tabClickListener = new TabClickListener();
            items = _data.subOrderDetail.subOrderList;

            final int adapterCount = adapter.getCount();
            for (int i = 0; i < adapterCount; i++) {
                View tabView = LayoutInflater.from(getContext()).inflate(R.layout.view_order_travel_tab_item, mTabStrip, false);
                TextView titleTV = (TextView) tabView.findViewById(R.id.order_travel_tab_title_tv);
                TextView dateTV = (TextView) tabView.findViewById(R.id.order_travel_tab_date_tv);
                titleTV.setText("行程" + (i + 1));
                final OrderBean itemOrderBean = items.get(i);
                String dateStr = DateUtils.orderChooseDateTransform(itemOrderBean.serviceTime);
                if (_data.totalDays > 1) {
                    dateStr += "-" + DateUtils.orderChooseDateTransform(itemOrderBean.serviceEndTime);
                }
                dateTV.setText(dateStr);
                ImageView tabIV = (ImageView) tabView.findViewById(R.id.order_travel_tab_iv);
                tabIV.setBackgroundResource(_currentIndex == i ? R.mipmap.order_timeline_focus : R.mipmap.order_timeline_normal);
                tabView.setOnClickListener(tabClickListener);
                mTabStrip.addView(tabView, LinearLayout.LayoutParams.WRAP_CONTENT, UIUtils.dip2px(60));
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0, true);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset, boolean isSelected) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;
            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= selectedChild.getWidth() * 2;
            }
            if (isSelected) {
                smoothScrollTo(targetScrollX, 0);
            } else {
                scrollTo(targetScrollX, 0);
            }
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }
            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null) ? (int) (positionOffset * selectedTitle.getWidth()) + (selectedTitle.getWidth() * 2 - UIUtils.dip2px(25)) : 0;
            scrollToTab(position, extraOffset, false);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0, true);
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
            View oldChild = mTabStrip.getChildAt(currentIndex);
            if (oldChild != null) {
                ImageView tabIV = (ImageView) oldChild.findViewById(R.id.order_travel_tab_iv);
                tabIV.setBackgroundResource(R.mipmap.order_timeline_normal);
            }

            View selectedChild = mTabStrip.getChildAt(position);
            if (selectedChild != null) {
                ImageView tabIV = (ImageView) selectedChild.findViewById(R.id.order_travel_tab_iv);
                tabIV.setBackgroundResource(R.mipmap.order_timeline_focus);
            }
            currentIndex = position;
        }

    }

    private class TabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    public int getRestoreIndex() {
        return restoreIndex;
    }

    public void setRestoreIndex(int restoreIndex) {
        this.restoreIndex = restoreIndex;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentIndex = savedState.oldTabIndex;
        restoreIndex = currentIndex;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.oldTabIndex = currentIndex;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        private int oldTabIndex;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            oldTabIndex = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(oldTabIndex);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}