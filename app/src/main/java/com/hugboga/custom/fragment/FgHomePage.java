package com.hugboga.custom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.adapter.HomePageAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestDestinations;
import com.hugboga.custom.data.request.RequestHome;
import com.hugboga.custom.data.request.RequestHotExploration;
import com.hugboga.custom.data.request.RequestTravelStorys;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.widget.home.HomeSearchTabView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/7.
 */
@ContentView(R.layout.fg_homepage)
public class FgHomePage extends BaseFragment implements HomeSearchTabView.HomeTabClickListener {

    /**
     * 热门探索
     */
    final static int TAB_HOTEXPLORE = 0b00;
    /**
     * 目的地
     */
    final static int TAB_DESTION = 0b01;
    /**
     * 司导故事
     */
    final static int TAB_TRAVEL_STORY = 0b10;

    @Bind(R.id.home_list_view)
    RecyclerView homeListView;

    HomePageAdapter homePageAdapter;
    HomeBeanV2 homeBean;

    private int tabIndex = TAB_HOTEXPLORE;

    //private boolean isLoading = false;
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
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_DISCOVERY;
    }

    @Override
    protected void initView() {
        homePageAdapter = new HomePageAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        homeListView.setLayoutManager(layoutManager);
        homeListView.setHasFixedSize(true);
        homeListView.setAdapter(homePageAdapter);
        setListViewScrollerListener();
    }


    private void setListViewScrollerListener() {
        homeListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (homeBean == null) {
                    return;
                }
                handleScrollerLoadEvent(); //判断是否滚动到底部是否加载更多
            }
        });
    }

    private void handleScrollerLoadEvent() {
        if (!ViewCompat.canScrollVertically(homeListView, 1)) {
            switch (tabIndex) {
                case TAB_HOTEXPLORE:
                    requestHotExploration();
                    break;
                case TAB_DESTION:
                    rquestDestinations();
                    break;
                case TAB_TRAVEL_STORY:
                    requestTravelStorys();
                    break;
            }
        }
    }


    protected Callback.Cancelable requestData() {
        return requestData(new RequestHome(getActivity()));
    }

    private void requestHotExploration() {
        HomeBeanV2.HotExplorationAggregation hotExplorationAggregation = homeBean.hotExplorationAggVo;
        if (hotExplorationAggregation != null && hotExplorationAggregation.hotExplorations != null) {
            if (hotExplorationAggregation.hotExplorations.size() < hotExplorationAggregation.listCount) {
                RequestHotExploration request = new RequestHotExploration(getActivity(), hotExplorationAggregation.hotExplorations.size());
                HttpRequestUtils.request(getActivity(), request, this, false);
            } else {
                return;
            }
        }
    }

    private void rquestDestinations() {
        HomeBeanV2.DestinationAggregation destinationAggregation = homeBean.destinationAggVo;
        if (destinationAggregation != null && destinationAggregation.lineGroupAggVos != null) {
            if (destinationAggregation.lineGroupAggVos.size() < destinationAggregation.listCount) {
                RequestDestinations request = new RequestDestinations(getActivity(), destinationAggregation.lineGroupAggVos.size());
                HttpRequestUtils.request(getActivity(), request, this, false);
            } else {
                return;
            }
        }
    }

    private void requestTravelStorys() {
        HomeBeanV2.TravelStoryAggregation travelStoryAggregation = homeBean.storyAggVo;
        if (travelStoryAggregation != null && travelStoryAggregation.travelStories != null) {
            if (travelStoryAggregation.travelStories.size() < travelStoryAggregation.listCount) {
                RequestTravelStorys request = new RequestTravelStorys(getActivity(), travelStoryAggregation.travelStories.size());
                HttpRequestUtils.request(getActivity(), request, this, false);
            } else {
                return;
            }
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestHome) {
            homeBean = ((RequestHome) _request).getData();
            if (homeBean != null) {
                homePageAdapter.clearAll();
                if (homeBean.headAggVo != null) {
                    addHeader(homeBean.headAggVo);
                }
                if (homeBean.hotExplorationAggVo != null) {
                    homePageAdapter.addHotExploations(homeBean.hotExplorationAggVo.hotExplorations,
                            false,homeBean.hotExplorationAggVo.listCount,homeBean.hotExplorationAggVo.getHotExplorationSize());
                }
            }

        } else if (_request instanceof RequestHotExploration) {
            HomeBeanV2.HotExplorationAggregation hotExplorationAggregation = ((RequestHotExploration) _request).getData();
            if (hotExplorationAggregation != null && hotExplorationAggregation.hotExplorations != null) {
                addMoreHotExplorations(hotExplorationAggregation.hotExplorations);
            }
        } else if (_request instanceof RequestDestinations) {
            HomeBeanV2.DestinationAggregation destinationAggregation = ((RequestDestinations) _request).getData();
            if (destinationAggregation != null && destinationAggregation.lineGroupAggVos != null) {
                addMoreDestinations(destinationAggregation.lineGroupAggVos);
            }
        } else if (_request instanceof RequestTravelStorys) {
            HomeBeanV2.TravelStoryAggregation storyAggregation = ((RequestTravelStorys) _request).getData();
            if (storyAggregation != null && storyAggregation.travelStories != null) {
                addMoreTravelStorys(storyAggregation.travelStories);
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }


    private void addHeader(HomeBeanV2.HomeHeaderInfo homeHeaderInfo) {
        homePageAdapter.showHeader(homeHeaderInfo, this);
    }

    private void addMoreHotExplorations(List<HomeBeanV2.HotExploration> hotExplorations) {
        if (homeBean == null || homeBean.hotExplorationAggVo == null) {
            return;
        }
        if (homeBean.hotExplorationAggVo.hotExplorations != null) {
            homeBean.hotExplorationAggVo.hotExplorations.addAll(hotExplorations);
            if (tabIndex == TAB_HOTEXPLORE) {
                homePageAdapter.addHotExploations(hotExplorations, false,
                        homeBean.hotExplorationAggVo.listCount,homeBean.hotExplorationAggVo.getHotExplorationSize());
            }
        }
    }

    private void addMoreDestinations(List<HomeBeanV2.LineGroupAgg> lineGroupAggs) {
        if (homeBean == null || homeBean.destinationAggVo == null) {
            return;
        }
        if (homeBean.destinationAggVo.lineGroupAggVos != null) {
            homeBean.destinationAggVo.lineGroupAggVos.addAll(lineGroupAggs);
            if (tabIndex == TAB_DESTION) {
                homePageAdapter.addDestionLineGroups(lineGroupAggs,homeBean.destinationAggVo.listCount
                ,homeBean.destinationAggVo.getLineGroupAggSize());
            }
        }
    }

    private void addMoreTravelStorys(List<HomeBeanV2.TravelStory> stories) {
        if (homeBean == null || homeBean.storyAggVo == null) {
            return;
        }
        if (homeBean.storyAggVo.travelStories != null) {
            homeBean.storyAggVo.travelStories.addAll(stories);
            if (tabIndex == TAB_TRAVEL_STORY) {
                homePageAdapter.addStoryModels(stories, false,homeBean.storyAggVo.listCount
                ,homeBean.storyAggVo.getTravelStoreySize());
            }
        }
    }

    @Override
    public void onTabClick(int resId) {
        if (homeBean == null) {
            return;
        }
        switch (resId) {
            case R.id.home_header_hot_tab:
                selectHotExploerTab();
                break;
            case R.id.home_header_dest_tab:
                selectDestionTab();
                break;
            case R.id.home_header_story_tab:
                selectTravelStoryTab();
                break;
            case R.id.home_activies_view:
                openActivitesPage();
                break;
            default:
                break;
        }
    }

    private void selectHotExploerTab() {
        if (tabIndex == TAB_HOTEXPLORE) {
            return;
        }
        tabIndex = TAB_HOTEXPLORE;
        if (homeBean.hotExplorationAggVo != null && homeBean.hotExplorationAggVo.hotExplorations != null) {
            homePageAdapter.addHotExploations(homeBean.hotExplorationAggVo.hotExplorations, true
            ,homeBean.hotExplorationAggVo.listCount,homeBean.hotExplorationAggVo.getHotExplorationSize());
        }
    }

    private void selectDestionTab() {
        if (tabIndex == TAB_DESTION) {
            return;
        }
        tabIndex = TAB_DESTION;
        if (homeBean.destinationAggVo != null) {
            if (homeBean.destinationAggVo.hotCities != null) {
                homePageAdapter.addHotCitys(homeBean.destinationAggVo.hotCities);
            }
            if(homeBean.destinationAggVo.lineGroupAggVos!=null){
                homePageAdapter.addDestionLineGroups(homeBean.destinationAggVo.lineGroupAggVos
                        ,homeBean.destinationAggVo.listCount,homeBean.destinationAggVo.getLineGroupAggSize());
            }
        }
    }

    private void selectTravelStoryTab() {
        if (tabIndex == TAB_TRAVEL_STORY) {
            return;
        }
        tabIndex = TAB_TRAVEL_STORY;
        if (homeBean.storyAggVo != null && homeBean.storyAggVo.travelStories != null) {
            homePageAdapter.addStoryModels(homeBean.storyAggVo.travelStories, true
                    ,homeBean.storyAggVo.listCount
                    ,homeBean.storyAggVo.getTravelStoreySize());
        }
    }

    /**
     * 打开活动页面
     */
    private void openActivitesPage() {
        if (!UserEntity.getUser().isLogin(getActivity())) {
            CommonUtils.showToast(R.string.login_hint);
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra(Constants.PARAMS_SOURCE, "首页-活动");
            startActivity(intent);
            return;
        }
    }

    @Override
    public String getEventSource() {
        return "首页";
    }

}
