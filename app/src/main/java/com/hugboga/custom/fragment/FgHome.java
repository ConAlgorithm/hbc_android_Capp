package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.request.RequestHome;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.widget.HomeActivitiesView;
import com.hugboga.custom.widget.HomeBannerView;
import com.hugboga.custom.widget.HomeChoicenessRouteView;
import com.hugboga.custom.widget.HomeCustomLayout;
import com.hugboga.custom.widget.HomeHotCityView;
import com.hugboga.custom.widget.HomeScrollView;
import com.hugboga.custom.widget.HomeSearchView;
import com.hugboga.custom.widget.HomeTravelStoriesView;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/8/2.
 */
@ContentView(R.layout.fg_home)
public class FgHome extends BaseFragment {

    @Bind(R.id.home_scrollview)
    HomeScrollView scrollView;

    @Bind(R.id.home_banner_view)
    HomeBannerView bannerView;
    @Bind(R.id.home_custom_layout)
    HomeCustomLayout customLayout;

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

    @Bind(R.id.home_empty_layout)
    LinearLayout emptyLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        setSensorsDefaultEvent(getEventSource(), SensorsConstant.DISCOVERY);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bannerView != null) {
            bannerView.onDestroy();
        }
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_DISCOVERY;
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
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestHome) {

            emptyLayout.setVisibility(View.GONE);
            routeView.setVisibility(View.VISIBLE);
            routeFreeView.setVisibility(View.VISIBLE);
            hotCityView.setVisibility(View.VISIBLE);
            travelStoriesView.setVisibility(View.VISIBLE);
            activitiesView.setVisibility(View.VISIBLE);

            RequestHome request = (RequestHome) _request;
            HomeBean data = request.getData();
            bannerView.update(data);
            routeView.update(data.fixGoods);
            routeFreeView.update(data.recommendGoods);
            hotCityView.update(data.getHotCityList());
            travelStoriesView.update(data.travelStories);
            activitiesView.update(data.activities);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        emptyLayout.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.home_empty_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_empty_tv:
                requestData();
                break;
        }
    }

    @Override
    public String getEventSource() {
        return "首页";
    }

    public boolean closeGuideView() {
        return customLayout.closeGuideView();
    }
}
