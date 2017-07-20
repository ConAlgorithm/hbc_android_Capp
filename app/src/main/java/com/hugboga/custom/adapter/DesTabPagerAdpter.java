package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.data.bean.SimpleLineGroupVo;
import com.hugboga.custom.widget.DesPager;

import java.util.ArrayList;

/**
 * Created by zhangqiang on 17/7/12.
 */

public class DesTabPagerAdpter extends PagerAdapter {
    ArrayList<SimpleLineGroupVo> simpleLineGroupVo;

    Context context;
    public DesPager homePager;
    public DesTabPagerAdpter(ArrayList<SimpleLineGroupVo> simpleLineGroupVo, Context context) {
        this.simpleLineGroupVo = simpleLineGroupVo;
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return simpleLineGroupVo.get(position).getGroupName();
    }

    @Override
    public int getCount() {
        return simpleLineGroupVo.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        homePager = new DesPager(context);//pagerList.get(position);
        homePager.initData(simpleLineGroupVo.get(position),position);
        //homePager.selectDestionTab(position,currentGroundId);
        container.addView(homePager,0);
        return homePager;
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
