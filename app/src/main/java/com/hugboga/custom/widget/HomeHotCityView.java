package com.hugboga.custom.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.utils.UIUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/10/20.
 */
public class HomeHotCityView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.home_hotcity_viewpager)
    ViewPager mViewPager;
    @Bind(R.id.home_hotcity_indicator)
    CirclePageIndicator mIndicator;

    public HomeHotCityView(Context context) {
        this(context, null);
    }

    public HomeHotCityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_home_hotcity, this);
        ButterKnife.bind(this, view);

        final int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        int itemWidth = (UIUtils.getScreenWidth() - paddingLeft * 2 - UIUtils.dip2px(8) * 2) / 3;
        int pagerHeight = (itemWidth + UIUtils.dip2px(56)) * 2 + UIUtils.dip2px(8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, pagerHeight);
        mViewPager.setLayoutParams(params);
    }

    @Override
    public void update(Object _data) {
        ArrayList<ArrayList<HomeBean.HotCity>> hotCityList = (ArrayList<ArrayList<HomeBean.HotCity>>)_data;
        HomeHotCityAdapter adapter = new HomeHotCityAdapter(getContext(), hotCityList);
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
        if (hotCityList.size() <= 0) {
            mIndicator.setVisibility(View.GONE);
        }
    }

    public static class HomeHotCityAdapter extends PagerAdapter {

        private Context mContext;

        private ArrayList<ArrayList<HomeBean.HotCity>> hotCityList;

        public HomeHotCityAdapter(Context context, ArrayList<ArrayList<HomeBean.HotCity>> hotCityList) {
            this.mContext = context;
            this.hotCityList = hotCityList;
        }

        @Override
        public int getCount() {
            return hotCityList == null ? 0 : hotCityList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final int paddingLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
            FrameLayout frameLayout = new FrameLayout(mContext);
            frameLayout.setPadding(paddingLeft, 0, paddingLeft, 0);
            HomeHotCityPageView itemView = new HomeHotCityPageView(mContext);
            itemView.update(hotCityList.get(position));
            frameLayout.addView(itemView);
            container.addView(frameLayout, 0);
            return frameLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
