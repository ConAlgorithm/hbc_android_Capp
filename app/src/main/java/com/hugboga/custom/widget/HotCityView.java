package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityActivity;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.utils.UIUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/10/20.
 */
public class HotCityView extends LinearLayout implements HbcViewBehavior{

    @BindView(R.id.home_hotcity_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.home_hotcity_indicator)
    CirclePageIndicator mIndicator;

    public HotCityView(Context context) {
        this(context, null);
    }

    public HotCityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_home_hotcity, this);
        ButterKnife.bind(this, view);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xFFFFFFFF);
        setPadding(0, 0, 0, UIUtils.dip2px(8));
    }

    @Override
    public void update(Object _data) {
        ArrayList<ArrayList<HomeBeanV2.HotCity>> hotCityList = (ArrayList<ArrayList<HomeBeanV2.HotCity>>)_data;
        if (hotCityList == null || hotCityList.size() <= 0) {
            this.setVisibility(View.GONE);
            return;
        }
        this.setVisibility(View.VISIBLE);
        HotCityAdapter adapter = new HotCityAdapter(getContext(), hotCityList);
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
        if (hotCityList.size() <= 1) {
            mIndicator.setVisibility(View.GONE);
        } else {
            mIndicator.setVisibility(View.VISIBLE);
        }

        int rowCount = 2;
        if (hotCityList.size() <= 1 && hotCityList.get(0) != null && hotCityList.get(0).size() <= 3) {
            rowCount = 1;
            setPadding(0, 0, 0, 0);
        }
        setItemHeight(rowCount);
    }

    private void setItemHeight(int rowCount) {
        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.city_view_padding_left);
        int itemWidth = (UIUtils.getScreenWidth() - paddingLeft * rowCount - UIUtils.dip2px(8) * 2) / 3;
        int itemHeigh = (int)((160 / 220.0f) * itemWidth);
        int pagerHeight = (itemHeigh + UIUtils.dip2px(54)) * rowCount + UIUtils.dip2px(8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, pagerHeight);
        mViewPager.setLayoutParams(params);
    }

    @OnClick({R.id.home_hotcity_more_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_hotcity_more_tv:
                if (getContext() instanceof CityListActivity) {
                    CityListActivity cityListActivity = (CityListActivity) getContext();
                    Intent intent = new Intent(getContext(), ChooseCityActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, cityListActivity.getEventSource());
                    intent.putExtra(ChooseCityActivity.KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_DAILY);
                    if (cityListActivity.paramsData.cityHomeType == CityListActivity.CityHomeType.ROUTE) {
                        intent.putExtra(ChooseCityActivity.KEY_GROUP_ID, cityListActivity.paramsData.id);
                    } else if (cityListActivity.paramsData.cityHomeType == CityListActivity.CityHomeType.COUNTRY) {
                        intent.putExtra(ChooseCityActivity.KEY_COUNTRY_ID, cityListActivity.paramsData.id);
                    }
                    intent.putExtra(ChooseCityActivity.KEY_FROM, ChooseCityActivity.CITY_LIST);
                    intent.putExtra(ChooseCityActivity.KEY_SHOW_TYPE, ChooseCityActivity.ShowType.CITY_LIST);
                    getContext().startActivity(intent);
                }
                break;
        }
    }

    public static class HotCityAdapter extends PagerAdapter {

        private Context mContext;

        private ArrayList<ArrayList<HomeBeanV2.HotCity>> hotCityList;

        public HotCityAdapter(Context context, ArrayList<ArrayList<HomeBeanV2.HotCity>> hotCityList) {
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
            final int paddingLeft = mContext.getResources().getDimensionPixelOffset(R.dimen.city_view_padding_left);
            FrameLayout frameLayout = new FrameLayout(mContext);
            frameLayout.setPadding(paddingLeft, 0, paddingLeft, 0);
            HotCityPageView itemView = new HotCityPageView(mContext);
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
