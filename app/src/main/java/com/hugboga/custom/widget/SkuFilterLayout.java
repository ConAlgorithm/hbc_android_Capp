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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.data.bean.GoodsFilterBean;
import com.hugboga.custom.fragment.CityFilterFragment;
import com.hugboga.custom.fragment.SkuScopeFilterFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SkuFilterLayout extends LinearLayout {
    @Bind(R.id.filter_sku_type_city_layout)
    LinearLayout cityLayout;
    @Bind(R.id.filter_sku_type_scope_layout)
    LinearLayout scopeLayout;

    @Bind(R.id.filter_sku_viewpager)
    NoScrollViewPager viewPager;

    private SkuFilterAdapter pagerAdapter;
    private List<LinearLayout> tabs = new ArrayList<>();
    private Drawable grayDownArrow, yellowUpArrow, yellowDownArrow;
    private int pagerPosition;

    private String dayTypes;
    private CityListActivity.Params cityParams;
    private SkuScopeFilterFragment.SkuFilterBean skuFilterBean;

    public SkuFilterLayout(Context context) {
        this(context, null);
    }

    public SkuFilterLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_filter_sku_group, this);
        ButterKnife.bind(view);
        setOrientation(LinearLayout.VERTICAL);

        viewPager.setScrollble(false);
        viewPager.setOffscreenPageLimit(2);
        FragmentActivity activity = (FragmentActivity) this.getContext();
        pagerAdapter = new SkuFilterAdapter(activity.getSupportFragmentManager());
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
        tabs.add(scopeLayout);

        Resources resources = MyApplication.getAppContext().getResources();
        grayDownArrow = resources.getDrawable(R.mipmap.city_filter_tab_arrow_down);
        grayDownArrow.setBounds(0, 0, grayDownArrow.getMinimumWidth(), grayDownArrow.getMinimumHeight());

        yellowUpArrow = resources.getDrawable(R.mipmap.guide_filter_tab_arrow_up);
        yellowUpArrow.setBounds(0, 0, yellowUpArrow.getMinimumWidth(), yellowUpArrow.getMinimumHeight());
        yellowDownArrow = resources.getDrawable(R.mipmap.guide_filter_tab_arrow_down);
        yellowDownArrow.setBounds(0, 0, yellowDownArrow.getMinimumWidth(), yellowDownArrow.getMinimumHeight());
    }

    @OnClick({R.id.filter_sku_type_city_layout, R.id.filter_sku_type_scope_layout})
    public void onTabClick(View view) {
        int index = 0;
        switch (view.getId()) {
            case R.id.filter_sku_type_city_layout:
                index = 0;
                break;
            case R.id.filter_sku_type_scope_layout:
                index = 1;
                break;
        }

        pagerAdapter.resetScopeFilter();

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
            boolean isSelectedCity = i == 0 && cityParams != null;

            boolean isSelectedFilter = false;
            if (i == 1) {
                if (skuFilterBean != null && skuFilterBean.getOperateCount() > 0) {
                    isSelectedFilter = true;
                } else if (skuFilterBean == null && !TextUtils.isEmpty(dayTypes)) {
                    isSelectedFilter = true;
                    String[] dayType = dayTypes.split(",");
                    TextView scopeCountTV = (TextView) scopeLayout.findViewById(R.id.filter_sku_type_scope_count_tv);
                    scopeCountTV.setVisibility(View.VISIBLE);
                    scopeCountTV.setText("" + dayType.length);
                }
            }
            if (i == index && open) {
                drawable = yellowUpArrow;
                textView.setTextColor(0xFFFFC110);
            } else {
                if (isSelectedCity || isSelectedFilter) {
                    textView.setTextColor(0xFFFFC110);
                    drawable = yellowDownArrow;
                } else {
                    textView.setTextColor(0xFF898989);
                    drawable = grayDownArrow;
                }
            }
            textView.setCompoundDrawables(null, null, drawable, null);
        }
    }

    public void hideFilterView() {
        viewPager.setVisibility(View.GONE);
        updateSelectStatus(pagerPosition, false);
    }

    public boolean isShowFilterView() {
        return viewPager.isShown();
    }

    public void initCityFilter(CityListActivity.Params cityParams) {
        pagerAdapter.setCityParams(cityParams);
        setCityParams(cityParams);
    }

    public void setCityParams(CityListActivity.Params cityParams) {
        if (cityParams == null) {
            return;
        }
        setSkuFilterBean(null);
        setDayTypes(null);
        pagerAdapter.resetFilter();

        this.cityParams = cityParams;
        TextView cityTV = (TextView) cityLayout.findViewById(R.id.filter_sku_type_city_tv);
        cityTV.setTextColor(0xFFFFC110);
        cityTV.setText(cityParams.titleName);
        updateSelectStatus(0, false);
        viewPager.setVisibility(View.GONE);
    }

    public void setSkuFilterBean(SkuScopeFilterFragment.SkuFilterBean skuFilterBean) {
        this.skuFilterBean = skuFilterBean;
        TextView scopeCountTV = (TextView) scopeLayout.findViewById(R.id.filter_sku_type_scope_count_tv);
        if (skuFilterBean == null) {
            scopeCountTV.setVisibility(View.GONE);
        } else {
            int operateCount = skuFilterBean.getOperateCount();
            if (skuFilterBean.isInitial || operateCount <= 0) {
                scopeCountTV.setVisibility(View.GONE);
            } else {
                scopeCountTV.setVisibility(View.VISIBLE);
                scopeCountTV.setText("" + operateCount);
            }
            updateSelectStatus(1, false);
            viewPager.setVisibility(View.GONE);
        }
    }

    public void setThemeList(ArrayList<GoodsFilterBean.FilterTheme> themeList) {
        pagerAdapter.setThemeList(themeList);
    }

    public void setDayTypes(String dayTypes) {
        this.dayTypes = dayTypes;
        pagerAdapter.setDayTypes(dayTypes);
        updateSelectStatus(1, false);
    }

    public static class SkuFilterAdapter extends FragmentStatePagerAdapter {

        SkuScopeFilterFragment skuScopeFilterFragment;
        ArrayList<GoodsFilterBean.FilterTheme> themeList;
        String dayTypes;
        CityListActivity.Params cityParams;

        public SkuFilterAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    CityFilterFragment cityFilterfragment = new CityFilterFragment();
                    cityFilterfragment.setCityParams(cityParams);
                    return cityFilterfragment;
                case 1:
                    skuScopeFilterFragment = new SkuScopeFilterFragment();
                    if (themeList != null) {
                        skuScopeFilterFragment.setThemeList(themeList);
                    }
                    if (!TextUtils.isEmpty(dayTypes)) {
                        skuScopeFilterFragment.setDayTypes(dayTypes);
                    }
                    return skuScopeFilterFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        public void resetFilter() {
            if (skuScopeFilterFragment != null) {
                skuScopeFilterFragment.resetAllFilterBean();
            }
        }

        public void resetScopeFilter() {
            if (skuScopeFilterFragment != null) {
                skuScopeFilterFragment.resetCacheFilter();
            }
        }

        public void setThemeList(ArrayList<GoodsFilterBean.FilterTheme> themeList) {
            if (skuScopeFilterFragment != null) {
                skuScopeFilterFragment.setThemeList(themeList);
            } else {
                this.themeList = themeList;
            }
        }

        public void setDayTypes(String dayTypes) {
            if (skuScopeFilterFragment != null) {
                skuScopeFilterFragment.setDayTypes(dayTypes);
            } else {
                this.dayTypes = dayTypes;
            }
        }

        public void setCityParams(CityListActivity.Params cityParams) {
            this.cityParams = cityParams;
        }
    }

}
