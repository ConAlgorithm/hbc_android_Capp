package com.hugboga.custom.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.NetWork;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.adapter.HomeAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.bean.HomeAggregationVo4;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.UserFavoriteGuideListVo3;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.FavoriteGuideSaved;
import com.hugboga.custom.data.request.RequestFilterGuide;
import com.hugboga.custom.data.request.RequestHomeNew;
import com.hugboga.custom.models.HomeNetworkErrorModel;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.nineoldandroids.view.ViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;

import java.util.ArrayList;

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
    }

    private void setListViewScrollerListener() {
        if (homeAdapter == null) {
            return;
        }
        homeListView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                    int statusBarHeight = UIUtils.getStatusBarHeight();
//                    int titleBarHeight = UIUtils.dip2px(50);
//                    int totalTitle = statusBarHeight + titleBarHeight;
//                    int scrollY = homeAdapter.homeServiceModel.getViewTop();
//                    if (scrollY <= viewTopEnd) {
//                        setAnimatorForSearch1();
//                    } else if (scrollY > viewTopEnd) {
//                        //恢复原样
//                        setAnimatorForSearch2();
//
//                    }
//
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (firstEnter) {
                    viewTopBegin = homeAdapter.homeServiceModel.getViewTop();
                    viewTopEnd = viewTopBegin - UIUtils.dip2px(100);
                    firstEnter = false;
                }
                int scrollY = homeAdapter.homeServiceModel.getViewTop();
                if(dy >0){
                    if (scrollY <= viewTopEnd && scrollY >0) {
                        setAnimatorForSearch1();
                    }
                }else{
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
        ValueAnimator animator2 = ValueAnimator.ofInt(marginLeft, UIUtils.dip2px(10));
        animator2.setDuration(200);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue(); // 动态的获取当前运行到的属性值
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) searchIcon.getLayoutParams();
                lp.setMargins(value,0,UIUtils.dip2px(10),0);
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
        ValueAnimator animator2 = ValueAnimator.ofInt(marginLeft, UIUtils.dip2px(94)); // 产生一个从0到100变化的整数的动画
        animator2.setDuration(200);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue(); // 动态的获取当前运行到的属性值
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) searchIcon.getLayoutParams();
                lp.setMargins(value,0,UIUtils.dip2px(10),0);
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
        //requestData();
    }

    protected Callback.Cancelable requestData() {
        //替换新的接口todo!
        return requestData(new RequestHomeNew(getActivity()));
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (!NetWork.isNetworkAvailable(MyApplication.getAppContext())) {
            Toast.makeText(MyApplication.getAppContext(), R.string.net_broken, Toast.LENGTH_LONG).show();
        }
        if (request instanceof RequestHomeNew) {
            //homePageAdapter.addNetworkErrorModel(this);
        } else if (request instanceof FavoriteGuideSaved) {
            Log.d("FavoriteGuideSaved", errorInfo.toString());
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestHomeNew) {
            homeBean = (HomeAggregationVo4) request.getData();
            if (homeBean != null) {
                if (homeBean.bannerActivityList != null && homeBean.bannerActivityList.size() > 0) {
                    homeAdapter.addHomeTitleBannar(getContext(), homeBean.bannerActivityList);
                }

                homeAdapter.addHomeService(getContext());
                homeAdapter.addGuideModels(getActivity(), homeBean.qualityGuides);
                homeAdapter.addHomeH5(getContext());
                if (homeBean != null && homeBean.hotAlbumList.size() > 0) {
                    for (int i = 0; i < homeBean.hotAlbumList.size(); i++) {
                        homeAdapter.addHotAlbum(getActivity(), homeBean.hotAlbumList.get(i), i);
                    }
                }

                //往期专辑
                homeAdapter.addPastAlbum(getContext(),homeBean.pastAlbumList);

                //游客说
                if (homeBean != null && homeBean.commentList.size() > 0) {
                    for (int i = 0; i < homeBean.commentList.size(); i++) {
                        homeAdapter.addHomeGuideEvaluate(getContext(), homeBean.commentList.get(i), i);
                    }
                }

                //推荐线路
                if (homeBean.cityGoodsList.size() >= 2) {
                    for (int i = 0; i < 2; i++) {
                        homeAdapter.addHomeRecommentRout(getContext(), homeBean.cityGoodsList.get(i));
                    }
                }

                //推荐线路广告
                homeAdapter.addHomeBanner(getContext(), homeBean.excitingActivityList);

                //其余城市线路推荐
                if (homeBean.cityGoodsList.size() > 2) {
                    for (int j = 2; j < homeBean.cityGoodsList.size(); j++) {
                        homeAdapter.addHomeRecommentRout(getContext(), homeBean.cityGoodsList.get(j));
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

        } else if (request instanceof FavoriteGuideSaved) {

            if (homeBean == null) {
                return;
            }

            for (int j = 0; j < homeBean.qualityGuides.size(); j++) {
                homeBean.qualityGuides.get(j).isCollected = 0;
            }
            if (request.getData() instanceof UserFavoriteGuideListVo3) {
                UserFavoriteGuideListVo3 favoriteGuideSavedBean = (UserFavoriteGuideListVo3) request.getData();
                for (int i = 0; i < favoriteGuideSavedBean.guides.size(); i++) {
                    for (int j = 0; j < homeBean.qualityGuides.size(); j++) {
                        if (favoriteGuideSavedBean.guides.get(i).equals(homeBean.qualityGuides.get(j).guideId)) {
                            homeBean.qualityGuides.get(j).isCollected = 1;
                        }
                    }
                }
                homeAdapter.notifyDataSetChanged();
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

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                StringBuilder tempUploadGuilds = new StringBuilder();
                String uploadGuilds = "";
                if (homeBean != null && homeBean.qualityGuides != null && homeBean.qualityGuides.size() > 0) {
                    for (FilterGuideBean guild : homeBean.qualityGuides) {
                        tempUploadGuilds.append(guild.guideId).append(",");
                    }
                    if (tempUploadGuilds.length() > 0) {
                        if (tempUploadGuilds.charAt(tempUploadGuilds.length() - 1) == ',') {
                            uploadGuilds = (String) tempUploadGuilds.subSequence(0, tempUploadGuilds.length() - 1);
                        }
                    }
                    Log.d("uploadGuilds", uploadGuilds.toString());
                    FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(getContext(), UserEntity.getUser().getUserId(getContext()), uploadGuilds);
                    HttpRequestUtils.request(getContext(), favoriteGuideSaved, this, false);
                }
                break;
            case CLICK_USER_LOOUT:
                if (homeBean != null) {
                    for (int i = 0; i < homeBean.qualityGuides.size(); i++) {
                        homeBean.qualityGuides.get(i).isCollected = 0;
                    }

                    homeAdapter.notifyDataSetChanged();
                }
                break;
            case ORDER_DETAIL_UPDATE_COLLECT:
                FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(getContext(), UserEntity.getUser().getUserId(getContext()), null);
                HttpRequestUtils.request(getContext(), favoriteGuideSaved, this, false);
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

    @OnClick({R.id.search_icon_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_icon_layout:
                goChooseCity();
                break;

        }

    }
}
