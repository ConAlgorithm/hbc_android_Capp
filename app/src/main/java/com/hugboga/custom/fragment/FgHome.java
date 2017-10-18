package com.hugboga.custom.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.NetWork;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.SearchDestinationGuideLineActivity;
import com.hugboga.custom.adapter.HomeAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeAggregationVo4;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.UserFavoriteGuideListVo3;
import com.hugboga.custom.data.bean.UserFavoriteLineList;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.FavoriteGuideSaved;
import com.hugboga.custom.data.request.FavoriteLinesaved;
import com.hugboga.custom.data.request.RequestHomeNew;
import com.hugboga.custom.models.HomeNetworkErrorModel;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 17/8/1.
 */

public class FgHome extends BaseFragment implements HomeNetworkErrorModel.ReloadListener {

    private static final int CHOICENESS_GUIDES_COUNT = 40;
    HomeAdapter homeAdapter;
    @Bind(R.id.home_list_view)
    RecyclerView homeListView;
    @Bind(R.id.app_icon)
    ImageView appIcon;
    @Bind(R.id.search_icon_layout)
    RelativeLayout searchIcon;
    HomeAggregationVo4 homeBean;
    int viewTopBegin;
    int viewTopEnd;
    boolean firstEnter = false;

    @Override
    public int getContentViewId() {
        return R.layout.fg_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        setSensorsDefaultEvent(getEventSource(), SensorsConstant.DISCOVERY);
        firstEnter = true;
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
    }

    @Override
    public void onStop() {
        super.onStop();
        setSensorsViewScreenEndEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        homeAdapter = new HomeAdapter();
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this.getActivity());
        homeListView.setLayoutManager(layoutManager);
        homeListView.setHasFixedSize(true);
        homeListView.setAdapter(homeAdapter);
        //homeListView.getRecycledViewPool().setMaxRecycledViews(0,20);
        setListViewScrollerListener();
        requestFgHomeData();
    }

    private void setListViewScrollerListener() {
        if (homeAdapter == null) {
            return;
        }
        homeListView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (homeAdapter.homeServiceModel == null) {
                    return;
                }
                if (firstEnter) {
                    viewTopBegin = homeAdapter.homeServiceModel.getViewTop();
                    viewTopEnd = viewTopBegin - UIUtils.dip2px(100);
                    firstEnter = false;
                }
                int scrollY = homeAdapter.homeServiceModel.getViewTop();
                if (dy > 0) {
                    if (scrollY <= viewTopEnd && scrollY > 0) {
                        setAnimatorForSearch1();
                    }
                } else {
                    if (scrollY > viewTopEnd) {
                        //恢复原样
                        setAnimatorForSearch2();

                    }
                }
            }
        });
    }

    private void setAnimatorForSearch1() {
        //icon动画
        FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) appIcon.getLayoutParams();
        int width = lp1.width;
        ValueAnimator animator1 = ValueAnimator.ofInt(width, 0);
        animator1.setDuration(200);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue(); // 动态的获取当前运行到的属性值
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) appIcon.getLayoutParams();
                lp.width = value;
                appIcon.setLayoutParams(lp);
            }
        });
        animator1.start(); // 开始播放动画

        //search动画
        FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) searchIcon.getLayoutParams();
        int marginLeft = lp2.leftMargin;
        ValueAnimator animator2 = ValueAnimator.ofInt(marginLeft, UIUtils.dip2px(16));
        animator2.setDuration(200);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue(); // 动态的获取当前运行到的属性值
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) searchIcon.getLayoutParams();
                lp.setMargins(value, 0, UIUtils.dip2px(16), 0);
                searchIcon.setLayoutParams(lp);
            }
        });
        animator2.start(); // 开始播放动画

    }

    private void setAnimatorForSearch2() {

        //icon动画
        FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) appIcon.getLayoutParams();
        int width = lp1.width;
        ValueAnimator animator1 = ValueAnimator.ofInt(width, UIUtils.dip2px(64)); // 产生一个从0到100变化的整数的动画
        animator1.setDuration(200);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue(); // 动态的获取当前运行到的属性值
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) appIcon.getLayoutParams();
                lp.width = value;
                appIcon.setLayoutParams(lp);
            }
        });
        animator1.start(); // 开始播放动画

        //search动画
        FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) searchIcon.getLayoutParams();
        int marginLeft = lp2.leftMargin;
        ValueAnimator animator2 = ValueAnimator.ofInt(marginLeft, UIUtils.dip2px(85)); // 产生一个从0到100变化的整数的动画
        animator2.setDuration(200);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue(); // 动态的获取当前运行到的属性值
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) searchIcon.getLayoutParams();
                lp.setMargins(value, 0, UIUtils.dip2px(16), 0);
                searchIcon.setLayoutParams(lp);
            }
        });
        animator2.start(); // 开始播放动画
    }

    @Override
    public String getEventSource() {
        return "首页";
    }

    @Override
    public void reload() {
        requestFgHomeData();
    }

    private void requestFgHomeData() {
        HttpRequestUtils.request(getActivity(), new RequestHomeNew(getActivity()), this, true);
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (request instanceof RequestHomeNew) {
            if(!NetWork.isNetworkAvailable(MyApplication.getAppContext())){
                homeAdapter.removeModels();
                //服务类型
                homeAdapter.addHomeService(getContext());
                //广告
                homeAdapter.addHomeH5(getContext());
                //底部活动
                homeAdapter.addHomeBottomBanner(getContext());
                //添加错误提示
                homeAdapter.addNetworkErrorModel(this);
            }
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestHomeNew) {
            homeAdapter.removeModels();
            homeBean = (HomeAggregationVo4) request.getData();
            if (homeBean != null) {
                //顶部活动
                if (homeBean.bannerActivityList != null && homeBean.bannerActivityList.size() > 0) {
                    homeAdapter.addHomeTitleBannar(getContext(), homeBean.bannerActivityList);
                }

                //服务类型
                homeAdapter.addHomeService(getContext());

                //热门目的地
                if(homeBean.hotCities != null && homeBean.hotCities.size() >0){
                    homeAdapter.addHomeHotDestination(getContext(),homeBean.hotCities);
                }

                //心仪司导
                if (homeBean.qualityGuides != null && homeBean.qualityGuides.size() > 0) {
                    homeAdapter.addGuideModels(getActivity(), homeBean.qualityGuides);
                }

                //广告
                homeAdapter.addHomeH5(getContext());

                //热门专辑
                if (homeBean.hotAlbumList != null && homeBean.hotAlbumList.size() > 0) {
                    for (int i = 0; i < homeBean.hotAlbumList.size(); i++) {
                        homeAdapter.addHotAlbum(getActivity(), homeBean.hotAlbumList.get(i), i);
                    }
                }

                //往期专辑
                if (homeBean.pastAlbumList != null && homeBean.pastAlbumList.size() > 0) {
                    homeAdapter.addPastAlbum(getContext(), homeBean.pastAlbumList);
                }

                //游客说
                if (homeBean.commentList != null && homeBean.commentList.size() > 0) {
                    for (int i = 0; i < homeBean.commentList.size(); i++) {
                        homeAdapter.addHomeGuideEvaluate(getContext(), homeBean.commentList.get(i), i);
                    }
                }

                //推荐线路/司导
                if (homeBean.cityRecommendedList != null && homeBean.cityRecommendedList.size() >= 2) {
                    for (int i = 0; i < 2; i++) {
                        if(homeBean.cityRecommendedList.get(i).contentType ==2){
                            homeAdapter.addHomeRecommentRout(getContext(), homeBean.cityRecommendedList.get(i));
                        }else if(homeBean.cityRecommendedList.get(i).contentType ==1){
                            homeAdapter.addHomeRecommentGuide(getContext(),homeBean.cityRecommendedList.get(i));
                        }

                    }
                }else if(homeBean.cityRecommendedList.size() == 1){
                    if(homeBean.cityRecommendedList.get(0).contentType ==2){
                        homeAdapter.addHomeRecommentRout(getContext(), homeBean.cityRecommendedList.get(0));
                    }else if(homeBean.cityRecommendedList.get(0).contentType ==1){
                        homeAdapter.addHomeRecommentGuide(getContext(),homeBean.cityRecommendedList.get(0));
                    }
                }

                //推荐线路广告
                if (homeBean.excitingActivityList != null && homeBean.excitingActivityList.size() > 0) {
                    homeAdapter.addHomeBanner(getContext(), homeBean.excitingActivityList);
                }

                //其余城市线路/司导推荐
                if (homeBean.cityRecommendedList != null && homeBean.cityRecommendedList.size() > 2) {
                    for (int j = 2; j < homeBean.cityRecommendedList.size(); j++) {
                        if(homeBean.cityRecommendedList.get(j).contentType ==2){
                            homeAdapter.addHomeRecommentRout(getContext(), homeBean.cityRecommendedList.get(j));
                        }else if(homeBean.cityRecommendedList.get(j).contentType ==1){
                            homeAdapter.addHomeRecommentGuide(getContext(),homeBean.cityRecommendedList.get(j));
                        }
                    }
                }

                //底部活动
                homeAdapter.addHomeBottomBanner(getContext());

            }
            //重新请求已收藏司导状态
            if (UserEntity.getUser().isLogin(getContext())) {
                FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(getContext(), UserEntity.getUser().getUserId(getContext()), null);
                HttpRequestUtils.request(getContext(), favoriteGuideSaved, this, false);
            }
            if(UserEntity.getUser().isLogin(getContext())){
                FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(getContext(),UserEntity.getUser().getUserId(getContext()));
                HttpRequestUtils.request(getContext(),favoriteLinesaved,this,false);
            }

        } else if (request instanceof FavoriteGuideSaved) {

            if (homeBean == null) {
                return;
            }

            for (int j = 0; j < homeBean.qualityGuides.size(); j++) {
                homeBean.qualityGuides.get(j).isCollected = 0;
            }
            for(int p=0;p<homeBean.hotAlbumList.size();p++){
                if(homeBean.hotAlbumList.get(p).albumType == 2){
                    for(int q=0;q<homeBean.hotAlbumList.get(p).albumRelItems.size();q++){
                        homeBean.hotAlbumList.get(p).albumRelItems.get(q).isCollected = 0;
                    }
                }
            }

            for (int y =0;y<homeBean.cityRecommendedList.size();y++){
                if(homeBean.cityRecommendedList.get(y).contentType == 1){
                    for(int z=0;z<homeBean.cityRecommendedList.get(y).cityItemList.size();z++){
                        homeBean.cityRecommendedList.get(y).cityItemList.get(z).isCollected =0;
                    }
                }
            }
            if (request.getData() instanceof UserFavoriteGuideListVo3) {
                //所有司导的收藏状态同步在此
                UserFavoriteGuideListVo3 favoriteGuideSavedBean = (UserFavoriteGuideListVo3) request.getData();
                for (int i = 0; i < favoriteGuideSavedBean.guides.size(); i++) {
                    for (int j = 0; j < homeBean.qualityGuides.size(); j++) {
                        if (favoriteGuideSavedBean.guides.get(i).equals(homeBean.qualityGuides.get(j).guideId)) {
                            homeBean.qualityGuides.get(j).isCollected = 1;
                        }
                    }
                }
                for(int o=0;o<favoriteGuideSavedBean.guides.size();o++){
                    for(int p=0;p<homeBean.hotAlbumList.size();p++){
                        if(homeBean.hotAlbumList.get(p).albumType == 2){
                            for(int q=0;q<homeBean.hotAlbumList.get(p).albumRelItems.size();q++){
                                if(favoriteGuideSavedBean.guides.get(o).equals(homeBean.hotAlbumList.get(p).albumRelItems.get(q).guideId)){
                                    homeBean.hotAlbumList.get(p).albumRelItems.get(q).isCollected = 1;
                                }
                            }
                        }
                    }
                }
                for(int x=0;x<favoriteGuideSavedBean.guides.size();x++){
                    for (int y =0;y<homeBean.cityRecommendedList.size();y++){
                        if(homeBean.cityRecommendedList.get(y).contentType == 1){
                            for(int z=0;z<homeBean.cityRecommendedList.get(y).cityItemList.size();z++){
                                if(favoriteGuideSavedBean.guides.get(x).equals(homeBean.cityRecommendedList.get(y).cityItemList.get(z).guideId)){
                                    homeBean.cityRecommendedList.get(y).cityItemList.get(z).isCollected =1;
                                }
                            }
                        }
                    }
                }
                homeAdapter.notifyDataSetChanged();
            }
        } else if(request instanceof FavoriteLinesaved){
            if (homeBean == null) {
                return;
            }
            for(int k = 0;k <homeBean.cityRecommendedList.size();k++){
                if(homeBean.cityRecommendedList.get(k).contentType == 2){
                    for(int m = 0;m<homeBean.cityRecommendedList.get(k).cityItemList.size();m++){
                        homeBean.cityRecommendedList.get(k).cityItemList.get(m).isCollected = 0;
                    }
                }
            }
            //所有线路的收藏状态同步在此
            if(request.getData() instanceof UserFavoriteLineList){
                UserFavoriteLineList userFavoriteLineList = (UserFavoriteLineList) request.getData();
                for (int o = 0; o<userFavoriteLineList.goodsNos.size();o++){
                    for(int k = 0;k <homeBean.cityRecommendedList.size();k++){
                        if(homeBean.cityRecommendedList.get(k).contentType == 2){
                            for(int m = 0;m<homeBean.cityRecommendedList.get(k).cityItemList.size();m++){
                                if(userFavoriteLineList.goodsNos.get(o).equals(homeBean.cityRecommendedList.get(k).cityItemList.get(m).goodsNo)){
                                    homeBean.cityRecommendedList.get(k).cityItemList.get(m).isCollected = 1;
                                }
                            }
                        }
                    }
                }
                homeAdapter.notifyDataSetChanged();
            }
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                StringBuilder tempUploadGuilds = new StringBuilder();
                String uploadGuilds = "";
                if (homeBean != null && homeBean.qualityGuides != null && homeBean.qualityGuides.size() > 0) {
//                    for (FilterGuideBean guild : homeBean.qualityGuides) {
//                        tempUploadGuilds.append(guild.guideId).append(",");
//                    }
//                    if (tempUploadGuilds.length() > 0) {
//                        if (tempUploadGuilds.charAt(tempUploadGuilds.length() - 1) == ',') {
//                            uploadGuilds = (String) tempUploadGuilds.subSequence(0, tempUploadGuilds.length() - 1);
//                        }
//                    }
                    Log.d("uploadGuilds", uploadGuilds.toString());
                    FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(getContext(), UserEntity.getUser().getUserId(getContext()), null);
                    HttpRequestUtils.request(getContext(), favoriteGuideSaved, this, false);

                    FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(getContext(),UserEntity.getUser().getUserId(getContext()));
                    HttpRequestUtils.request(getContext(),favoriteLinesaved,this,false);
                }

                break;
            case CLICK_USER_LOOUT:
                if (homeBean != null) {
                    for (int i = 0; i < homeBean.qualityGuides.size(); i++) {
                        homeBean.qualityGuides.get(i).isCollected = 0;
                    }
                    for(int k = 0;k <homeBean.cityRecommendedList.size();k++){
                        for(int m = 0;m<homeBean.cityRecommendedList.get(k).cityItemList.size();m++){
                            homeBean.cityRecommendedList.get(k).cityItemList.get(m).isCollected = 0;
                        }
                    }
                    homeAdapter.notifyDataSetChanged();
                }
                break;
            case ORDER_DETAIL_UPDATE_COLLECT:
                FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(getContext(), UserEntity.getUser().getUserId(getContext()), null);
                HttpRequestUtils.request(getContext(), favoriteGuideSaved, this, false);
                break;
            case LINE_UPDATE_COLLECT:
                FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(getContext(),UserEntity.getUser().getUserId(getContext()));
                HttpRequestUtils.request(getContext(),favoriteLinesaved,this,false);
                break;
        }
    }

    private void goChooseCity() {
        //Intent intent = new Intent(this.getContext(), ChooseCityNewActivity.class);
        Intent intent = new Intent(this.getContext(), SearchDestinationGuideLineActivity.class);
        intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_HOME);
        intent.putExtra("isHomeIn", true);
        intent.putExtra("source", "首页搜索框");
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        this.getContext().startActivity(intent);
        StatisticClickEvent.click(StatisticConstant.SEARCH_LAUNCH, "首页");
    }

    @OnClick({R.id.search_icon_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_icon_layout:
                goChooseCity();
                break;

        }
    }

    private void setSensorsViewScreenBeginEvent() {
        try {
            SensorsDataAPI.sharedInstance(getContext()).trackTimerBegin("AppViewScreen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSensorsViewScreenEndEvent() {
        try {
            JSONObject properties = new JSONObject();
            properties.put("pageName", getEventSource());
            properties.put("pageTitle", getEventSource());
            properties.put("refer", "");
            SensorsDataAPI.sharedInstance(getContext()).trackTimerEnd("AppViewScreen", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
