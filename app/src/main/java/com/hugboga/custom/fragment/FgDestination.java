package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.DesTabPagerAdpter;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.widget.DesPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by zhangqiang on 17/7/11.
 */

public class FgDestination extends BaseFragment {

    @Bind(R.id.vp_view)
    ViewPager mViewPager;

    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    DesPager desPager;
    ArrayList<DesPager> pagerList =  new ArrayList<DesPager>();
    ArrayList<String> titleTextList = new ArrayList<>();
    @Override
    public int getContentViewId() {
        return R.layout.fg_destination;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void initView() {
        super.initView();
        pagerList = new ArrayList<>();

        //测试
        titleTextList.add("热门");
        titleTextList.add("港澳台");
        titleTextList.add("东南亚");
        titleTextList.add("日韩");
        titleTextList.add("欧洲");
        titleTextList.add("北美洲");
        titleTextList.add("南美洲");
        titleTextList.add("大洋洲");
        titleTextList.add("非洲");
        titleTextList.add("中东");
        for(int i=0;i<10;i++){
            pagerList.add(new DesPager(getContext()));
        }

        DesTabPagerAdpter mAdapter = new DesTabPagerAdpter(pagerList,titleTextList);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        mViewPager.setCurrentItem(0);
        selectDestionTab(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int position;

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                this.position = position;
                CommonUtils.showToast("position=="+position);
                selectDestionTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void selectDestionTab(int position) {
        DesPager desPager = pagerList.get(position);
        String title = titleTextList.get(position);
        //需要将请求的数据添加到DesPager中
        desPager.requestData(title,position);
    }
}
