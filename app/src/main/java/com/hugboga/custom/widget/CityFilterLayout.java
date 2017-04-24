package com.hugboga.custom.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.fragment.CityFilterDaysFragment;
import com.hugboga.custom.fragment.CityFilterThemesFragment;
import com.hugboga.custom.fragment.CityFilterTypeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/22.
 */
public class CityFilterLayout extends LinearLayout implements View.OnClickListener{

    ViewPager cityFilterViewPager;
    CityFilterPagerAdapter pagerAdapter;
    ViewGroup typeTabViewLayout;
    ViewGroup dayTabViewLayout;
    ViewGroup themeTabViewLayout;

    List<CityHomeBean.GoodsThemes> goodsThemesList;

    private Drawable textUpArraw,textDownArraw;

    private List<ViewGroup> tabs = new ArrayList<>();

    public CityFilterLayout(Context context) {
        this(context,null);
    }

    public CityFilterLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
        initRes();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    private void initViews() {
        initTabs();
        initViewPagers();
    }

    private void initRes(){
        Resources resources = MyApplication.getAppContext().getResources();
        textDownArraw = resources.getDrawable(R.mipmap.city_filter_tab_arrow_down);
        textDownArraw.setBounds(0, 0, textDownArraw.getMinimumWidth(), textDownArraw.getMinimumHeight());
        textUpArraw = resources.getDrawable(R.mipmap.city_filter_tab_arrow_up);
        textUpArraw.setBounds(0, 0, textUpArraw.getMinimumWidth(), textUpArraw.getMinimumHeight());
    }


    private void initTabs(){
        tabs.clear();

        typeTabViewLayout = (ViewGroup) this.findViewById(R.id.cityHome_unlimited_type_lay);
        typeTabViewLayout.setOnClickListener(this);

        dayTabViewLayout = (ViewGroup) this.findViewById(R.id.cityHome_unlimited_days_lay);
        dayTabViewLayout.setOnClickListener(this);

        themeTabViewLayout = (ViewGroup) this.findViewById(R.id.cityHome_unlimited_theme_lay);
        themeTabViewLayout.setOnClickListener(this);

        tabs.add(typeTabViewLayout);
        tabs.add(dayTabViewLayout);
        tabs.add(themeTabViewLayout);
    }

    private void initViewPagers(){
        cityFilterViewPager = (ViewPager) findViewById(R.id.city_filter_viewpager);
        cityFilterViewPager.setOffscreenPageLimit(3);
        cityFilterViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                showFilterView(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        initViewPagerAdapter();
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
        if(cityFilterViewPager!=null && cityFilterViewPager.isShown()){
            cityFilterViewPager.setVisibility(View.GONE);
        }

    }

    public void hideTab(){
        this.setVisibility(View.GONE);
        if(cityFilterViewPager!=null && cityFilterViewPager.isShown()){
            cityFilterViewPager.setVisibility(View.GONE);
        }
        hideSelecteIcon();
    }

    private void hideSelecteIcon(){
        for(int i=0;i<tabs.size();i++){
            ViewGroup viewGroup = tabs.get(i);
            viewGroup.getChildAt(1).setVisibility(View.GONE);
            TextView textView =  (TextView) viewGroup.getChildAt(0);
            textView.setCompoundDrawables(null,null,textDownArraw,null);
        }
    }

    public void setFilterTabTypeValue(String value){
        ((TextView)typeTabViewLayout.getChildAt(0)).setText(value);
    }

    public void setFilterTabDayValue(String value){
        ((TextView)dayTabViewLayout.getChildAt(0)).setText(value);
    }

    public void setFilterTabThemeValue(String value){
        ((TextView)themeTabViewLayout.getChildAt(0)).setText(value);
    }




    public void showFilterView(){
        onlyShowTab();
        if(!cityFilterViewPager.isShown()){
            cityFilterViewPager.setVisibility(View.VISIBLE);
        }
    }

    public void showFilterView(int index){
        onlyShowTab();
        int curr = cityFilterViewPager.getCurrentItem();
        if(curr==index){
            updateSelectStatus(index,true);
        }else{
            cityFilterViewPager.setCurrentItem(index);
        }

        if(!cityFilterViewPager.isShown()){
            cityFilterViewPager.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @return
     */
    public boolean filterViewIsShow(){
        return cityFilterViewPager.isShown();
    }

    private void updateSelectStatus(int index,boolean open){
        for(int i=0;i<tabs.size();i++){
            ViewGroup viewGroup = tabs.get(i);
            if(i==index){
                TextView textView =  (TextView) viewGroup.getChildAt(0);
                if(open){
                    textView.setCompoundDrawables(null,null,textUpArraw,null);
                    viewGroup.getChildAt(1).setVisibility(View.VISIBLE);
                }else{
                    textView.setCompoundDrawables(null,null,textDownArraw,null);
                    viewGroup.getChildAt(1).setVisibility(View.GONE);
                }
            }else{
                TextView textView =  (TextView) viewGroup.getChildAt(0);
                textView.setCompoundDrawables(null,null,textDownArraw,null);
                viewGroup.getChildAt(1).setVisibility(View.GONE);
            }
        }
    }

    public int getTabLayoutHeight(){
        if(typeTabViewLayout==null){
            return 0;
        }
        return  typeTabViewLayout.getHeight();
    }

    public void onlyHideFilterView(){
        cityFilterViewPager.setVisibility(View.GONE);
        hideSelecteIcon();

    }

    public void hideFilterView(){
        onlyHideFilterView();
        this.setVisibility(GONE);
    }

    public void setGoodsThemesList(List<CityHomeBean.GoodsThemes> goodsThemesList) {
//        if(this.goodsThemesList!=null && this.goodsThemesList.size()>1){
//            return;
//        }
        this.goodsThemesList = goodsThemesList;
        if(pagerAdapter!=null){
            pagerAdapter.updateThemesValue(goodsThemesList);
        }
    }

    @Override
    public void onClick(View v) {
         switch (v.getId())  {
             case R.id.cityHome_unlimited_type_lay:
                 if(cityFilterViewPager.getCurrentItem()==0 && cityFilterViewPager.isShown()){
                     cityFilterViewPager.setVisibility(View.GONE);
                     updateSelectStatus(0,false);
                     return;
                 }
                 showFilterView(0);
                 break;
             case R.id.cityHome_unlimited_days_lay:
                 if(cityFilterViewPager.getCurrentItem()==1 && cityFilterViewPager.isShown()){
                     cityFilterViewPager.setVisibility(View.GONE);
                     updateSelectStatus(1,false);
                     return;
                 }
                 showFilterView(1);
                 break;
             case R.id.cityHome_unlimited_theme_lay:
                 if(cityFilterViewPager.getCurrentItem()==2 && cityFilterViewPager.isShown()){
                     cityFilterViewPager.setVisibility(View.GONE);
                     updateSelectStatus(2,false);
                     return;
                 }
                 showFilterView(2);
                 break;
             default:
                 break;
         }
    }


    public void resetDatas(){
        if(tabs!=null && tabs.size()>0){
            for(int i=0;i<tabs.size();i++){
                ((TextView)tabs.get(i).getChildAt(0)).setText("不限");
            }
        }

        if(pagerAdapter!=null){
            if(pagerAdapter.cityFilterThemesFragment!=null){
                pagerAdapter.cityFilterThemesFragment.resetData();
            }
        }

        if(pagerAdapter.cityFilterDaysFragment!=null){
            pagerAdapter.cityFilterDaysFragment.resetData();
        }

        if(pagerAdapter.cityFilterTypeFragment!=null){
            pagerAdapter.cityFilterTypeFragment.resetData();
        }
    }

    class CityFilterPagerAdapter extends FragmentStatePagerAdapter {

        CityFilterThemesFragment cityFilterThemesFragment;
        CityFilterTypeFragment cityFilterTypeFragment;
        CityFilterDaysFragment cityFilterDaysFragment;

        public CityFilterPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    cityFilterTypeFragment = new CityFilterTypeFragment();
                    return cityFilterTypeFragment;
                case 1:
                    cityFilterDaysFragment = new CityFilterDaysFragment();
                    return cityFilterDaysFragment;
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
