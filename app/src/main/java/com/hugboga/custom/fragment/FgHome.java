package com.hugboga.custom.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.FakeAIActivity;
import com.hugboga.custom.activity.QueryCityActivity;
import com.hugboga.custom.adapter.HomeAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.bean.HomeTopBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestHome;
import com.hugboga.custom.data.request.RequestHomeTop;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.home.HomeRefreshHeader;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.yan.pullrefreshlayout.PullRefreshLayout;
import com.yan.pullrefreshlayout.ShowGravity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/11/22.
 */

public class FgHome extends BaseFragment {

    @BindView(R.id.home_titlebar_app_icon)
    ImageView appIconIV;
    @BindView(R.id.home_refresh_layout)
    PullRefreshLayout refreshLayout;
    @BindView(R.id.home_list_view)
    RecyclerView homeRecyclerView;
    @BindView(R.id.homed_titlebar_ai_iv)
    ImageView titlebarAiIV;
    @BindView(R.id.home_titlebar_search_hint_tv)
    TextView searchHintTV;
    @BindView(R.id.home_titlebar_search_bottom_line)
    View searchBottomLineView;

    HomeRefreshHeader homeRefreshHeader;

    private HomeAdapter homeAdapter;
    private HomeBean homeBean;
    private int movedDistance = 0;

    @Override
    public int getContentViewId() {
        return R.layout.fg_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        setSensorsDefaultEvent(getEventSource(), SensorsConstant.DISCOVERY);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        setSensorsViewScreenBeginEvent();
        if (homeAdapter != null) {
            homeAdapter.startAutoScroll();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (homeAdapter != null) {
            homeAdapter.stopAutoScroll();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        if (BuildConfig.FLAVOR == Constants.CHANNEL_GOOGLE_PLAY) {
            appIconIV.setBackgroundResource(R.drawable.home_hbc_logo_icon2);
            appIconIV.getLayoutParams().height = UIUtils.dip2px(27);
        }

        homeAdapter = new HomeAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this.getActivity());
        homeRecyclerView.setLayoutManager(layoutManager);
        homeRecyclerView.setHasFixedSize(true);
        homeRecyclerView.setAdapter(homeAdapter);
        sendRequest();

        refreshLayout.setHeaderShowGravity(ShowGravity.FOLLOW);
        refreshLayout.setDragDampingRatio(0.7f);
        refreshLayout.setTwinkEnable(false);
        homeRefreshHeader = new HomeRefreshHeader(getContext(), refreshLayout);
        refreshLayout.setHeaderView(homeRefreshHeader);
        refreshLayout.setRefreshEnable(false);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HomeRefreshHeader homeRefreshHeader = refreshLayout.getHeaderView();
                if (!homeRefreshHeader.isTwoRefresh()) {
                    refreshLayout.refreshComplete();
                }
            }

            @Override
            public void onLoading() {
            }
        });
        setRecyclerViewScrollListener();
    }

    private void setRecyclerViewScrollListener() {
        homeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (homeAdapter.homeBannerModel == null || homeAdapter.homeBannerModel.itemView == null
                        || homeAdapter.homeAiModel == null || homeAdapter.homeAiModel.homeAIView == null) {
                    return;
                }
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                int scrollY = Math.abs(recyclerView.getChildAt(0).getTop());
                float bannerHeight = homeAdapter.homeBannerModel.itemView.getBannerLayoutHeight();
                float region = UIUtils.dip2px(130);
                if (firstVisibleItemPosition == 0 && scrollY <= bannerHeight && scrollY >= bannerHeight - region) {
                    float progress = ((scrollY - (bannerHeight - region)) / region);
                    homeAdapter.homeAiModel.homeAIView.setProgress(progress);
                    titlebarAiIV.setVisibility(View.GONE);
                    searchHintTV.setVisibility(View.VISIBLE);
                    searchBottomLineView.setVisibility(View.VISIBLE);
                    searchHintTV.setTextColor(UIUtils.getColorWithAlpha(1 - progress,0xFF929292));
                    searchBottomLineView.setBackgroundColor(UIUtils.getColorWithAlpha(1 - progress,0xFFE6E6E6));
                } else if (firstVisibleItemPosition > 1) {
                    titlebarAiIV.setVisibility(View.VISIBLE);
                    searchHintTV.setVisibility(View.GONE);
                    searchBottomLineView.setVisibility(View.GONE);
                } else {
                    titlebarAiIV.setVisibility(View.GONE);
                }
                if (firstVisibleItemPosition == 0 && scrollY < bannerHeight - region - UIUtils.dip2px(20)) {
                    homeAdapter.homeAiModel.homeAIView.setProgress(0);
                    searchHintTV.setVisibility(View.VISIBLE);
                    searchBottomLineView.setVisibility(View.VISIBLE);
                    searchHintTV.setTextColor(0xFF929292);
                    searchBottomLineView.setBackgroundColor(0xFFE6E6E6);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (homeAdapter.homeBannerModel == null || homeAdapter.homeBannerModel.itemView == null
                        || homeRecyclerView.getChildCount() <= 0 || homeRecyclerView.getChildAt(0) == null) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = homeRecyclerView.getLayoutManager();
                    int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    int scrollY = Math.abs(homeRecyclerView.getChildAt(0).getTop());
                    float bannerHeight = homeAdapter.homeBannerModel.itemView.getBannerLayoutHeight();
                    float region = UIUtils.dip2px(130);
                    int aiViewHeight = UIUtils.dip2px(46);

                    boolean scope = firstVisibleItemPosition == 0 && scrollY <= bannerHeight && scrollY >= bannerHeight - region;
                    boolean scope2 = firstVisibleItemPosition == 1 && scrollY <= aiViewHeight;
                    if (scope) {
                        setHeaderAnimator((int) (bannerHeight + aiViewHeight + UIUtils.dip2px(15)) - scrollY);
                    } else if (scope2) {
                        setHeaderAnimator(aiViewHeight - scrollY);
                    }
                }
            }
        });
    }

    private void setHeaderAnimator(int distance) {
        if (distance <= 0) {
            return;
        }
        movedDistance = 0;
        ValueAnimator animator = ValueAnimator.ofInt(0, distance);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                homeRecyclerView.scrollBy(0, value - movedDistance);
                homeRecyclerView.invalidate();
                movedDistance = value;
            }
        });
        animator.start();
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestHome) {
            homeBean = ((RequestHome) _request).getData();
            homeAdapter.setData(homeBean);
            RequestHomeTop requestHomeTop = new RequestHomeTop(getActivity());
            requestData(requestHomeTop);
        } else if (_request instanceof RequestHomeTop) {
            List<HomeTopBean> homeTopBeanList = ((RequestHomeTop) _request).getData();
            homeRefreshHeader.update(homeTopBeanList);
            refreshLayout.setRefreshEnable(true);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        super.onDataRequestError(errorInfo, _request);
        if (_request instanceof RequestHome) {
            homeAdapter.addEmptyModel();
        }
    }

    private void sendRequest() {
        RequestHome requestHome = new RequestHome(getActivity());
        requestData(requestHome);
    }

    @OnClick({R.id.homed_titlebar_search_iv, R.id.home_titlebar_search_hint_tv})
    public void intentSearchActivity() {
        Intent intent = new Intent(this.getContext(), QueryCityActivity.class);
        intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
        intent.putExtra("isHomeIn", true);
        intent.putExtra("source", getEventSource());
        startActivity(intent);
        StatisticClickEvent.click(StatisticConstant.SEARCH_LAUNCH, getEventSource());
        SensorsUtils.onAppClick("首页", "搜索","");
    }

    @OnClick({R.id.homed_titlebar_ai_iv})
    public void aiClickActivity() {
        Intent intent = new Intent(getContext(), FakeAIActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        startActivity(intent);
    }

    @Override
    public String getEventSource() {
        return "首页";
    }

    private void setSensorsViewScreenBeginEvent() {
        try {
            SensorsDataAPI.sharedInstance(getContext()).trackTimerBegin("AppViewScreen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                break;
            case CLICK_USER_LOOUT:
                break;
            case LINE_UPDATE_COLLECT:
                homeAdapter.notifyDataSetChanged();
                break;
            case REQUEST_HOME_DATA:
                if (homeBean == null) {
                    sendRequest();
                }
                break;
        }
    }
}
