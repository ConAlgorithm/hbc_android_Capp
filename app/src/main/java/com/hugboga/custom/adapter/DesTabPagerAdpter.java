package com.hugboga.custom.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.widget.DesPager;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class DesTabPagerAdpter extends PagerAdapter {
    private List<String> titleTextList;
    List<DesPager> pagerList;
    private LinkedHashMap<Integer, View> views = new LinkedHashMap<>();

    public DesTabPagerAdpter(List<DesPager> pagerList, List<String> titleTextList) {
        this.pagerList = pagerList;
        this.titleTextList = titleTextList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleTextList.get(position);
    }

    @Override
    public int getCount() {
        return pagerList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        DesPager homePager = pagerList.get(position);
        View view = views.get(position);
        if (view == null) {
            view = homePager.inflateView();
            views.put(position, view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
