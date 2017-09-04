package com.hugboga.custom.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.NetWork;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.adapter.HomePageAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.bean.FilterGuideListBean;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.UserFavoriteGuideListVo3;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.FavoriteGuideSaved;
import com.hugboga.custom.data.request.RequestDestinations;
import com.hugboga.custom.data.request.RequestFilterGuide;
import com.hugboga.custom.data.request.RequestHome;
import com.hugboga.custom.data.request.RequestHotExploration;
import com.hugboga.custom.models.HomeNetworkErrorModel;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.home.HomeSearchTabView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by SPW on 2017/3/7.
 */
public class FgHomePage extends BaseFragment implements HomeSearchTabView.HomeTabClickListener,HomeNetworkErrorModel.ReloadListener {

    private static final int CHOICENESS_GUIDES_COUNT = 40;

    /**
     * 热门探索
     */
    public final static int TAB_HOTEXPLORE = 0b00;
    /**
     * 目的地
     */
    public final static int TAB_DESTION = 0b01;
    /**
     * 精选司导
     */
    public final static int TAB_GUIDE = 0b10;

    @Bind(R.id.home_list_view)
    RecyclerView homeListView;
    @Bind(R.id.home_tab_view)
    FrameLayout tabParentContainer;
    @Bind(R.id.home_title_layout)
    View homeTitleLayout;
    @Bind(R.id.home_logo_icon)
    View homeBindIcon;
    @Bind(R.id.search_icon_layout)
    RelativeLayout searchTitle;
    HomePageAdapter homePageAdapter;
    HomeBeanV2 homeBean;

    private int tabIndex = TAB_GUIDE;

    HomeSearchTabView homeSearchTabView;

    @Override
    public int getContentViewId() {
        return R.layout.fg_homepage;
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_DISCOVERY;
    }

    @Override
    protected void initView() {
        homePageAdapter = new HomePageAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this.getActivity());
        homeListView.setLayoutManager(layoutManager);
        homeListView.setHasFixedSize(true);
        homeListView.setAdapter(homePageAdapter);
        homePageAdapter.showHeader(getContext(),new HomeBeanV2.HomeHeaderInfo(),new ArrayList<HomeBeanV2.ActivityPageSettingVo>(),this);
        setListViewScrollerListener();
        addTabView();
    }

    private void addTabView(){
        homeSearchTabView = new HomeSearchTabView(getActivity());
        homeSearchTabView.setHomeTabClickListener(this);
        tabParentContainer.addView(homeSearchTabView);
    }

    private void setListViewScrollerListener() {
        homeListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){
                    //handleScrollerIdleEvent(); //滑动停止后判断头部显示状态
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (/*homeBean == null ||*/ dy==0) {
                    return;
                }
                handleScrollerLoadEvent(); //判断是否滚动到底部 如果是底部请求一下页

                handleScrollerTabEvent();//是否需要浮层显示tab

                //handleScrollerServiceViewEvent();//头部动画处理

            }
        });
    }


    private void handleScrollerTabEvent(){
        if (/*homeBean != null && homeBean.headAggVo!=null &&*/ homeSearchTabView!=null) {
            if(homePageAdapter.homeHeaderModel!=null){
                int scrollY = Math.abs(homePageAdapter.homeHeaderModel.getTabViewTop());
                int scrollYFastYudingViewHeight = homePageAdapter.homeHeaderModel.getFastYudingViewTop();
                //int scrollYOtherServiceViewHeight = Math.abs(homePageAdapter.homeHeaderModel.getOtherServiceViewHeight());
                int scrollYOtherServiceViewTop = homePageAdapter.homeHeaderModel.getOtherServiceViewTop();
                //int serviceHeight = Math.abs(homePageAdapter.homeHeaderModel.getServiceLayout());

                int statusBarHeight = UIUtils.getStatusBarHeight();
                int titleBarHeight = UIUtils.dip2px(48);
                int totalTitle = statusBarHeight + titleBarHeight;
                int fastYudingViewHeight = UIUtils.dip2px(39);

                if (scrollY >= totalTitle) {
                    tabParentContainer.setVisibility(View.GONE);
                    homePageAdapter.homeHeaderModel.locationTab(tabIndex);
                } else {
                    tabParentContainer.setVisibility(View.VISIBLE);
                    homeSearchTabView.tabIndex(tabIndex);
                }
                int titleBarLayoutDis = scrollY-statusBarHeight-titleBarHeight;
                if(scrollYOtherServiceViewTop<totalTitle){
                    homeTitleLayout.setVisibility(View.VISIBLE);
                    /*float alpha = (float)(totalTitle-scrollYOtherServiceViewTop)/(float)totalTitle*1.0f;
                    if(alpha>1.0f){
                        alpha = 1.0f;
                    }*/
                    homeTitleLayout.setAlpha(1.0f);
                }else{
                    homeTitleLayout.setAlpha(0);
                    homeTitleLayout.setVisibility(View.GONE);
                }

                //int disSearchTitle = serviceHeight+scrollYOtherServiceViewHeight+fastYudingViewHeight;
                if(scrollYFastYudingViewHeight < totalTitle){
                    /*float alpha = (float)scrollYFastYudingViewHeight/(float)totalTitle*1.0f;
                    if(alpha < 0){
                        alpha = 0;
                        searchTitle.setVisibility(View.GONE);
                    }
                    if(alpha>=0){
                        searchTitle.setVisibility(View.VISIBLE);
                    }
                    if(alpha>1.0f){
                        alpha = 1.0f;
                    }*/
                    searchTitle.setAlpha(0);
                    searchTitle.setVisibility(View.GONE);
                }else{
                    searchTitle.setAlpha(1.0f);
                    searchTitle.setVisibility(View.VISIBLE);
                }

                /*if(titleBarLayoutDis<125){
                    float alpha = (125-titleBarLayoutDis)/(float)125;
                    homeBindIcon.setAlpha(alpha);
                    //searchTitle.setVisibility(View.GONE);
                }else{
                    homeBindIcon.setAlpha(0);
                }*/
            }
        }

    }

    private void handleScrollerServiceViewEvent(){
        if (homePageAdapter.homeHeaderModel!=null){
            homePageAdapter.homeHeaderModel.animateServiceView(homeTitleLayout.findViewById(R.id.home_header_search_title).getMeasuredWidth());
        }
    }

    private void handleScrollerLoadEvent() {
        if(homeBean==null){
            return;
        }
        if (!ViewCompat.canScrollVertically(homeListView, 1)) {
            switch (tabIndex) {
                case TAB_HOTEXPLORE:
                    requestHotExploration();
                    break;
                case TAB_DESTION:
                    rquestDestinations();
                    break;
                case TAB_GUIDE:
                    requestChoicenessGuides();
                    break;
            }
        }
    }

    private void handleScrollerIdleEvent(){
        if(homePageAdapter.homeHeaderModel!=null){
            int toTop = homePageAdapter.homeHeaderModel.getTabViewTop();
            int maxHeight = ScreenUtil.screenWidth * (810 - ScreenUtil.statusbarheight) / 750;
            if(toTop<=0 || toTop>maxHeight){
                return;
            }
            if(toTop>maxHeight/2){
                homeListView.smoothScrollToPosition(0);
            }else{
                tabScrollToTop();
               if(homeSearchTabView!=null){
                   homeSearchTabView.setAlpha(1.0f);
               }
               if(homeBindIcon!=null){
                   homeBindIcon.setAlpha(1.0f);
               }
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

    private void requestChoicenessGuides() {
        ArrayList<FilterGuideBean> storyAggVo = homeBean.qualityGuides;
        if (storyAggVo != null && storyAggVo.size() < CHOICENESS_GUIDES_COUNT) {
            RequestFilterGuide.Builder builder = new RequestFilterGuide.Builder();
                builder.isQuality = 1;
                builder.limit = 10;
                builder.offset = storyAggVo.size();
            RequestFilterGuide requestFilterGuide = new RequestFilterGuide(getActivity(), builder);
            HttpRequestUtils.request(getActivity(), requestFilterGuide, this, false);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestHome) {
            homeBean = ((RequestHome) _request).getData();
            if (homeBean != null) {
                homePageAdapter.removeAfterHeader();
                if (homeBean.headAggVo != null ) {
                    if(homeBean.bannerActivityList !=null){
                        addHeader(getContext(),homeBean.bannerActivityList,homeBean.headAggVo);
                    }else{
                        addHeader(getContext(),null,homeBean.headAggVo);
                    }
                }

                switch (tabIndex) {
                    case TAB_GUIDE:
                        if (homeBean!=null && homeBean.qualityGuides != null) {
                            homePageAdapter.addGuideModels(getActivity(),homeBean.qualityGuides, true
                                    ,CHOICENESS_GUIDES_COUNT
                                    ,homeBean.qualityGuides != null ? homeBean.qualityGuides.size() : 0);
                        }
                        break;
                    case TAB_HOTEXPLORE:
                        if (homeBean!=null && homeBean.hotExplorationAggVo != null && homeBean.hotExplorationAggVo.hotExplorations != null) {
                            homePageAdapter.addHotExploations(homeBean.hotExplorationAggVo.hotExplorations, true
                                    ,homeBean.hotExplorationAggVo.listCount,homeBean.hotExplorationAggVo.getHotExplorationSize());
                        }
                        break;
                    case TAB_DESTION:
                        if (homeBean!=null && homeBean.destinationAggVo != null) {
                            if (homeBean.destinationAggVo.hotCities != null) {
                                homePageAdapter.addHotCitys(homeBean.destinationAggVo.hotCities);
                            }
                            if(homeBean.destinationAggVo.lineGroupAggVos!=null){
                                homePageAdapter.addDestionLineGroups(homeBean.destinationAggVo.lineGroupAggVos
                                        ,homeBean.destinationAggVo.listCount,homeBean.destinationAggVo.getLineGroupAggSize());
                            }
                        }
                        break;
                }
                //重新请求已收藏司导状态
                if(UserEntity.getUser().isLogin(getContext())){
                    FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(getContext(),UserEntity.getUser().getUserId(getContext()),null);
                    HttpRequestUtils.request(getContext(),favoriteGuideSaved,this,false);
                }
            }

        } else if (_request instanceof RequestHotExploration) {
            HomeBeanV2.HotExplorationAggregation hotExplorationAggregation = ((RequestHotExploration) _request).getData();
            if (hotExplorationAggregation != null && hotExplorationAggregation.hotExplorations != null) {
                addMoreHotExplorations(hotExplorationAggregation.hotExplorations);
            }
            //homePageAdapter.addHomeRecommentRout(getContext());
        } else if (_request instanceof RequestDestinations) {
            HomeBeanV2.DestinationAggregation destinationAggregation = ((RequestDestinations) _request).getData();
            if (destinationAggregation != null && destinationAggregation.lineGroupAggVos != null) {
                addMoreDestinations(destinationAggregation.lineGroupAggVos);
            }
        } else if (_request instanceof RequestFilterGuide) {
            FilterGuideListBean filterGuideListBean = ((RequestFilterGuide) _request).getData();
            if (filterGuideListBean != null && filterGuideListBean.listData != null) {
                addMoreGuides(filterGuideListBean.listData);
            }
            //homePageAdapter.addHomeRecommentRout(getContext());
        }else if (_request instanceof FavoriteGuideSaved){
            for(int j=0;j<homeBean.qualityGuides.size();j++){
                homeBean.qualityGuides.get(j).isCollected  = 0;
            }
            if(_request.getData() instanceof UserFavoriteGuideListVo3){
                UserFavoriteGuideListVo3 favoriteGuideSavedBean = (UserFavoriteGuideListVo3) _request.getData();
                for (int i = 0; i < favoriteGuideSavedBean.guides.size(); i++) {
                    for (int j = 0; j < homeBean.qualityGuides.size(); j++) {
                        if (favoriteGuideSavedBean.guides.get(i).equals(homeBean.qualityGuides.get(j).guideId)) {
                            homeBean.qualityGuides.get(j).isCollected = 1;
                        }
                    }
                }
                homePageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if(!NetWork.isNetworkAvailable(MyApplication.getAppContext())){
            CommonUtils.showToast(R.string.net_broken);
        }
        if(request instanceof RequestHome){
            homePageAdapter.addNetworkErrorModel(this);
        }else if (request instanceof FavoriteGuideSaved){
            Log.d("FavoriteGuideSaved",errorInfo.toString());
        }
    }


    private void addHeader(Context context, ArrayList<HomeBeanV2.ActivityPageSettingVo> activityPageSettings, HomeBeanV2.HomeHeaderInfo homeHeaderInfo) {
        homePageAdapter.showHeader(context,homeHeaderInfo, activityPageSettings,this);
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

    private void addMoreGuides(ArrayList<FilterGuideBean> storyAggVo) {
        if (homeBean == null || homeBean.qualityGuides == null) {
            return;
        }
        if (homeBean.qualityGuides != null) {
            homeBean.qualityGuides.addAll(storyAggVo);
            if (tabIndex == TAB_GUIDE) {
                homePageAdapter.addGuideModels(getActivity(),storyAggVo, false, CHOICENESS_GUIDES_COUNT
                ,homeBean.qualityGuides != null ? homeBean.qualityGuides.size() : 0);
            }
        }
    }

    @Override
    public void onTabClick(int resId) {
        if(resId==R.id.home_activies_view){
            openActivitesPage();
            return;
        }
      
        switch (resId) {
            case R.id.home_header_hot_tab:
                selectHotExploerTab();
                MobClickUtils.onEvent(StatisticConstant.CLICK_HOT);
                SensorsUtils.onAppClick(getEventSource(),"热门线路",null);
                break;
            case R.id.home_header_dest_tab:
                selectDestionTab();
                MobClickUtils.onEvent(StatisticConstant.CLICK_DEST);
                break;
            case R.id.home_header_story_tab:
                selectGuideTab();
                MobClickUtils.onEvent(StatisticConstant.CLICK_GSTORY);
                SensorsUtils.onAppClick(getEventSource(),"精选司导",null);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.home_search_icon,R.id.home_title_layout,R.id.search_icon_layout})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_search_icon:
            case R.id.search_icon_layout:
                goChooseCity();
                break;
            case R.id.home_title_layout:
                if(homeListView!=null){
                    homeListView.smoothScrollToPosition(0);
                }
                break;
        }

    }

    private void goChooseCity() {
        Intent intent = new Intent(this.getContext(), ChooseCityNewActivity.class);
        intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
        intent.putExtra("isHomeIn", true);
        intent.putExtra("source", "首页搜索框");
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        this.getContext().startActivity(intent);
        StatisticClickEvent.click(StatisticConstant.SEARCH_LAUNCH, "首页");
    }

    private void selectHotExploerTab() {
        if (tabIndex == TAB_HOTEXPLORE) {
            tabScrollToTop();
            return;
        }
        tabIndex = TAB_HOTEXPLORE;
        swtichTabScrollToTop();
        if (homeBean!=null && homeBean.hotExplorationAggVo != null && homeBean.hotExplorationAggVo.hotExplorations != null) {
            homePageAdapter.addHotExploations(homeBean.hotExplorationAggVo.hotExplorations, true
            ,homeBean.hotExplorationAggVo.listCount,homeBean.hotExplorationAggVo.getHotExplorationSize());
        }
    }

    private void selectDestionTab() {
        if (tabIndex == TAB_DESTION) {
            tabScrollToTop();
            return;
        }
        tabIndex = TAB_DESTION;

        swtichTabScrollToTop();
        if (homeBean!=null && homeBean.destinationAggVo != null) {
            if (homeBean.destinationAggVo.hotCities != null) {
                homePageAdapter.addHotCitys(homeBean.destinationAggVo.hotCities);
            }
            if(homeBean.destinationAggVo.lineGroupAggVos!=null){
                homePageAdapter.addDestionLineGroups(homeBean.destinationAggVo.lineGroupAggVos
                        ,homeBean.destinationAggVo.listCount,homeBean.destinationAggVo.getLineGroupAggSize());
            }
        }
    }

    private void selectGuideTab() {
        if (tabIndex == TAB_GUIDE) {
            tabScrollToTop();
            return;
        }
        tabIndex = TAB_GUIDE;

        swtichTabScrollToTop();
        if (homeBean!=null && homeBean.qualityGuides != null) {
            homePageAdapter.addGuideModels(getActivity(),homeBean.qualityGuides, true
                    ,CHOICENESS_GUIDES_COUNT
                    ,homeBean.qualityGuides != null ? homeBean.qualityGuides.size() : 0);
        }
    }


    private void tabScrollToTop(){
        int toTopDis = Math.abs(homePageAdapter.homeHeaderModel.getTabViewTop());
        int distance = toTopDis - UIUtils.statusBarHeight - UIUtils.dip2px(48);
        if(distance>0){
            homeListView.smoothScrollBy(0, distance);
        }
        if(homeTitleLayout != null){
            homeTitleLayout.setVisibility(View.VISIBLE);
            homeTitleLayout.setAlpha(1.0f);
        }
        /*if(homeBindIcon != null){
            homeBindIcon.setAlpha(1f);
        }*/
    }

    private void swtichTabScrollToTop(){
        ((LinearLayoutManager)homeListView.getLayoutManager()).scrollToPositionWithOffset(1,UIUtils.dip2px(88));
        if(homeTitleLayout!=null){
            homeTitleLayout.setVisibility(View.VISIBLE);
            homeTitleLayout.setAlpha(1.0f);
        }
        /*if(homeBindIcon != null){
            homeBindIcon.setAlpha(1f);
        }*/
    }

    /**
     * 打开活动页面
     */
    private void openActivitesPage() {
        MobClickUtils.onEvent(StatisticConstant.LAUNCH_ACTLIST, (Map)new HashMap<>().put("source", "首页"));
        Intent intent = new Intent(getContext(), WebInfoActivity.class);
        if(UserEntity.getUser().isLogin(getActivity())){
            intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_ACTIVITY + UserEntity.getUser().getUserId(getContext()) + "&t=" + new Random().nextInt(100000));
        }else{
            intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_ACTIVITY );
        }
        startActivity(intent);
        setSensorsDefaultEvent("活动列表", SensorsConstant.ACTLIST);
    }

    @Override
    public String getEventSource() {
        return "首页";
    }

    @Override
    public void reload() {
        requestData();
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                StringBuilder tempUploadGuilds = new StringBuilder();
                String uploadGuilds = "";
                if(homeBean!= null && homeBean.qualityGuides != null  && homeBean.qualityGuides.size() > 0){
                    for (FilterGuideBean guild : homeBean.qualityGuides) {
                        tempUploadGuilds.append(guild.guideId).append(",");
                    }
                    if (tempUploadGuilds.length() > 0) {
                        if (tempUploadGuilds.charAt(tempUploadGuilds.length() - 1) == ',') {
                            uploadGuilds = (String) tempUploadGuilds.subSequence(0, tempUploadGuilds.length() - 1);
                        }
                    }
                    Log.d("uploadGuilds",uploadGuilds.toString());
                    FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(getContext(),UserEntity.getUser().getUserId(getContext()),uploadGuilds);
                    HttpRequestUtils.request(getContext(),favoriteGuideSaved,this,false);
                }
                break;
            case CLICK_USER_LOOUT:
                if(homeBean!= null){
                    for(int i=0;i<homeBean.qualityGuides.size();i++){
                        homeBean.qualityGuides.get(i).isCollected = 0;
                    }

                    homePageAdapter.notifyDataSetChanged();
                }
                break;
            case ORDER_DETAIL_UPDATE_COLLECT:
                FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(getContext(),UserEntity.getUser().getUserId(getContext()),null);
                HttpRequestUtils.request(getContext(),favoriteGuideSaved,this,false);
                break;
        }
    }
}
