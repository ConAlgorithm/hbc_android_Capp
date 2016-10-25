package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.fragment.CityFilterDaysFragment;
import com.hugboga.custom.fragment.CityFilterThemesFragment;
import com.hugboga.custom.fragment.CityFilterTypeFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/10/22.
 */
public class CityFilterLayout extends LinearLayout {

    ViewPager cityFilterViewPager;
    ViewGroup cityFilterTabLayout;

    CityFilterPagerAdapter pagerAdapter;

    TextView typeTabTextView;
    TextView dayTabTextView;
    TextView themeTabTextView;

    List<CityHomeBean.GoodsThemes> goodsThemesList;



    public CityFilterLayout(Context context) {
        this(context,null);
    }

    public CityFilterLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        inflate(context, R.layout.city_home_filter_view_group,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        cityFilterViewPager = (ViewPager) findViewById(R.id.city_filter_viewpager);
        cityFilterViewPager.setOffscreenPageLimit(3);
        cityFilterViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                //// TODO: 2016/10/22
                //切换tab
            }
        });

        cityFilterTabLayout = (ViewGroup) this.getChildAt(0);
        int tabCount = cityFilterTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            switch (i) {
                case 0:
                    initCityTypeTabView(cityFilterTabLayout.getChildAt(i));
                    break;
                case 1:
                    initCityDayTabView(cityFilterTabLayout.getChildAt(i));
                    break;
                case 2:
                    intiCityThemeTabView(cityFilterTabLayout.getChildAt(i));
                    break;
            }
        }

    }



    private void initCityTypeTabView(View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterView(0);
            }
        });

    }

    private void initCityDayTabView(View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterView(1);
            }
        });
    }

    private void intiCityThemeTabView(View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterView(2);
            }
        });
    }

    private void initViewPagerAdapter(){
        if(pagerAdapter==null){
            FragmentActivity activity = (FragmentActivity)this.getContext();
            pagerAdapter = new CityFilterPagerAdapter(activity.getSupportFragmentManager());
            cityFilterViewPager.setAdapter(pagerAdapter);
        }
    }

    public void onlyShowTab(){
        this.setVisibility(View.VISIBLE);
        cityFilterTabLayout.setVisibility(View.VISIBLE);
    }

    public void hideTab(){
        this.setVisibility(View.GONE);
    }

    public void showFilterView(int index){
        onlyShowTab();
        initViewPagerAdapter();
        cityFilterViewPager.setVisibility(View.VISIBLE);
        cityFilterViewPager.setCurrentItem(index);
    }

    public void hideFilterView(){
        cityFilterViewPager.setVisibility(GONE);
        this.setVisibility(GONE);
    }

    public void setGoodsThemesList(List<CityHomeBean.GoodsThemes> goodsThemesList) {
        this.goodsThemesList = goodsThemesList;
        if(pagerAdapter!=null){
            pagerAdapter.updateThemesValue(goodsThemesList);
        }
    }


    class CityFilterPagerAdapter extends FragmentStatePagerAdapter {

        CityFilterThemesFragment cityFilterThemesFragment;

        public CityFilterPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new CityFilterTypeFragment();
                case 1:
                    return new CityFilterDaysFragment();
                case 2:
                    cityFilterThemesFragment = new CityFilterThemesFragment();
                    cityFilterThemesFragment.setDatas(goodsThemesList);
                    return cityFilterThemesFragment;
            }
            return null;
        }
        @Override
        public int getCount() {
            return 3;
        }


        public void updateThemesValue(List<CityHomeBean.GoodsThemes> goodsThemes){
            if(cityFilterThemesFragment!=null){
                cityFilterThemesFragment.setDatas(goodsThemes);
            }
        }
    }


}
