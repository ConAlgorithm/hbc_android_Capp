package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.HomeData;
import com.hugboga.custom.data.request.RequestHome;
import com.hugboga.custom.widget.HomeBannerView;
import com.hugboga.custom.widget.HomeBottomLayout;
import com.hugboga.custom.widget.HomeChoicenessRouteView;
import com.hugboga.custom.widget.HomeDynamicView;
import com.hugboga.custom.widget.HomeScrollView;
import com.hugboga.custom.widget.HomeSearchView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 首页
 */
@ContentView(R.layout.fg_home)
public class FgHome extends BaseFragment {

    @Bind(R.id.home_banner_view)
    HomeBannerView bannerView;

    @Bind(R.id.home_dynamic_view)
    HomeDynamicView dynamicView;

    @Bind(R.id.home_choiceness_route_view)
    HomeChoicenessRouteView routeView;

    @Bind(R.id.home_bottom_layout)
    HomeBottomLayout bottomLayout;

    @Bind(R.id.home_scrollview)
    HomeScrollView scrollview;

    @Bind(R.id.home_search_view)
    HomeSearchView searchView;

    //img_undertext
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bannerView != null) {
            bannerView.onStartChange();
        }
        if (dynamicView != null) {
            dynamicView.onRestart();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bannerView != null) {
            bannerView.onDestroyHandler();
        }
        if (dynamicView != null) {
            dynamicView.onSuspend();
        }
    }

    @Override
    protected void initHeader() {
        bottomLayout.setFragment(FgHome.this);
        searchView.setFragment(FgHome.this);
        scrollview.setSearchView(searchView);
    }

    @Override
    protected void initView() {

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
        if (_request instanceof RequestHome) {
            RequestHome request = (RequestHome) _request;
            HomeData data = request.getData();
            bannerView.update(data.getBannerList());
            routeView.setData(FgHome.this, data.getCityContentList());
            bottomLayout.setSalesPromotion(data.getSalesPromotion());
        }
    }
}
