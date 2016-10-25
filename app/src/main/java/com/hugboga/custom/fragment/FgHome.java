package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.request.RequestHome;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.widget.HomeActivitiesView;
import com.hugboga.custom.widget.HomeBannerView;
import com.hugboga.custom.widget.HomeChoicenessRouteView;
import com.hugboga.custom.widget.HomeHotCityView;
import com.hugboga.custom.widget.HomeScrollView;
import com.hugboga.custom.widget.HomeSearchView;
import com.hugboga.custom.widget.HomeTravelStoriesView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 首页
 */
@ContentView(R.layout.fg_home)
public class FgHome extends BaseFragment {

    @Bind(R.id.home_scrollview)
    HomeScrollView scrollView;

    @Bind(R.id.home_banner_view)
    HomeBannerView bannerView;

    @Bind(R.id.home_choiceness_route_view)
    HomeChoicenessRouteView routeView;
    @Bind(R.id.home_choiceness_free_view)
    HomeChoicenessRouteView routeFreeView;

    @Bind(R.id.home_hotcity_view)
    HomeHotCityView hotCityView;

    @Bind(R.id.home_travel_stories_view)
    HomeTravelStoriesView travelStoriesView;

    @Bind(R.id.home_activities_view)
    HomeActivitiesView activitiesView;

    @Bind(R.id.home_search_layout)
    FrameLayout searchLayout;
    @Bind(R.id.home_search_float_layout)
    FrameLayout searchFloatLayout;
    private HomeSearchView homeSearchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_DISCOVERY;
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
    protected void initHeader() {

    }

    @Override
    protected void initView() {
        homeSearchView = new HomeSearchView(getContext());
        scrollView.setSearchView(searchLayout, searchFloatLayout, homeSearchView);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return requestData(new RequestHome(getActivity()));
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestHome) {
            RequestHome request = (RequestHome) _request;
            HomeBean data = request.getData();
            routeView.update(data.fixGoods);
            routeFreeView.update(data.recommendGoods);
            hotCityView.update(data.getHotCityList());
            travelStoriesView.update(data.travelStories);
            activitiesView.update(data.activities);
        }
    }
}
