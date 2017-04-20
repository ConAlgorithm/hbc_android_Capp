package com.hugboga.custom.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.fragment.CityFilterFragment;
import com.hugboga.custom.fragment.GuideFilterFragment;
import com.hugboga.custom.fragment.GuideFilterSortFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideFilterLayout extends LinearLayout {

    @Bind(R.id.filter_guide_type_city_layout)
    LinearLayout cityLayout;
    @Bind(R.id.filter_guide_type_filter_layout)
    LinearLayout filterLayout;
    @Bind(R.id.filter_guide_type_sort_layout)
    LinearLayout sortLayout;

    @Bind(R.id.filter_guide_viewpager)
    NoScrollViewPager viewPager;

    private GuideFilterAdapter pagerAdapter;
    private List<LinearLayout> tabs = new ArrayList<>();
    private Drawable grayUpArrow, grayDownArrow, yellowUpArrow, yellowDownArrow;
    private int pagerPosition;

    private CityListActivity.Params cityParams;

    public GuideFilterLayout(Context context) {
        this(context, null);
    }

    public GuideFilterLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_filter_guide_group, this);
        ButterKnife.bind(view);
        setOrientation(LinearLayout.VERTICAL);

        viewPager.setScrollble(false);
        viewPager.setOffscreenPageLimit(3);
        FragmentActivity activity = (FragmentActivity) this.getContext();
        pagerAdapter = new GuideFilterAdapter(activity.getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                pagerPosition = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        tabs.add(cityLayout);
        tabs.add(filterLayout);
        tabs.add(sortLayout);

        Resources resources = MyApplication.getAppContext().getResources();
        grayDownArrow = resources.getDrawable(R.mipmap.city_filter_tab_arrow_down);
        grayDownArrow.setBounds(0, 0, grayDownArrow.getMinimumWidth(), grayDownArrow.getMinimumHeight());
        grayUpArrow = resources.getDrawable(R.mipmap.city_filter_tab_arrow_up);
        grayUpArrow.setBounds(0, 0, grayUpArrow.getMinimumWidth(), grayUpArrow.getMinimumHeight());

        yellowUpArrow = resources.getDrawable(R.mipmap.guide_filter_tab_arrow_up);
        yellowUpArrow.setBounds(0, 0, yellowUpArrow.getMinimumWidth(), yellowUpArrow.getMinimumHeight());
        yellowDownArrow = resources.getDrawable(R.mipmap.guide_filter_tab_arrow_down);
        yellowDownArrow.setBounds(0, 0, yellowDownArrow.getMinimumWidth(), yellowDownArrow.getMinimumHeight());
    }

    @OnClick({R.id.filter_guide_type_city_layout, R.id.filter_guide_type_filter_layout, R.id.filter_guide_type_sort_layout})
    public void onTabClick(View view) {
        int index = 0;
        switch (view.getId()) {
            case R.id.filter_guide_type_city_layout:
                index = 0;
                break;
            case R.id.filter_guide_type_filter_layout:
                index = 1;
                break;
            case R.id.filter_guide_type_sort_layout:
                index = 2;
                break;
        }

        if (pagerPosition == index && viewPager.isShown()) {
            updateSelectStatus(index, false);
            viewPager.setVisibility(View.GONE);
            return;
        } else {
            updateSelectStatus(index, true);
            viewPager.setCurrentItem(index);
            viewPager.setVisibility(View.VISIBLE);
        }
    }

    private void updateSelectStatus(int index, boolean open) {
        for (int i = 0;i < tabs.size(); i++) {
            LinearLayout linearLayout = tabs.get(i);
            TextView textView = (TextView) linearLayout.getChildAt(1);
            Drawable drawable = null;
            if (i == index) {
                if (i == 0 && cityParams != null) {
                    drawable = open ? yellowUpArrow : yellowDownArrow;
                } else {
                    drawable = open ? grayUpArrow : grayDownArrow;
                }
            } else {
                if (i == 0 && cityParams != null) {
                    drawable = yellowDownArrow;
                } else {
                    drawable = grayDownArrow;
                }
            }
            textView.setCompoundDrawables(null, null, drawable, null);
        }
    }

    public void showFilterView() {
        if (viewPager.isShown()) {
            viewPager.setVisibility(View.GONE);
        } else {
            viewPager.setVisibility(View.VISIBLE);
        }
    }

    public void showFilterView(int index) {
        if (pagerPosition == index){
            updateSelectStatus(index, true);
        } else {
            viewPager.setCurrentItem(index);
        }

        if (!viewPager.isShown()) {
            viewPager.setVisibility(View.VISIBLE);
        }
    }

    public void hideFilterView() {
        viewPager.setVisibility(View.GONE);
    }

    public void setCityParams(CityListActivity.Params cityParams) {
        if (cityParams == null) {
            return;
        }
        this.cityParams = cityParams;
        TextView cityTV = (TextView) cityLayout.findViewById(R.id.filter_guide_city_tv);
        cityTV.setTextColor(0xFFFFC110);
        cityTV.setText(cityParams.titleName);
        updateSelectStatus(0, false);
        hideFilterView();
    }

    public static class GuideFilterAdapter extends FragmentStatePagerAdapter {

        public GuideFilterAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    CityFilterFragment fragment1 = new CityFilterFragment();
                    return fragment1;
                case 1:
                    GuideFilterFragment fragment2 = new GuideFilterFragment();
                    return fragment2;
                case 2:
                    GuideFilterSortFragment fragment3 = new GuideFilterSortFragment();
                    return fragment3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
