package com.hugboga.custom.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
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

public class FgHome extends BaseFragment implements HomeNetworkErrorModel.ReloadListener{

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
        if(homeAdapter == null){
            return;
        }
        homeListView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE){

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(firstEnter){
                    viewTopBegin = homeAdapter.homeServiceModel.getViewTop();
                    viewTopEnd = viewTopBegin - 50;
                }
                int statusBarHeight = UIUtils.getStatusBarHeight();
                int titleBarHeight = UIUtils.dip2px(50);
                int totalTitle = statusBarHeight + titleBarHeight;
                int scrollY = homeAdapter.homeServiceModel.getViewTop();
                int lastScrollY = scrollY;
                /*if(scrollY <totalTitle && scrollY >0){
                    if(lastScrollY - scrollY > 0){
                        //向上滑动
                        int m = lastScrollY - scrollY;
                    }else{
                        //向下滑动
                        int n = lastScrollY - scrollY;
                    }
                }else */if(scrollY == viewTopEnd){
                    //隐藏黄包车
                    //appIcon.setVisibility(View.GONE);
                    //setAnimatorForSearch1();

                }else if(scrollY == viewTopBegin){
                    //恢复原样
                    //appIcon.setVisibility(View.VISIBLE);
                    if(!firstEnter){
                        //setAnimatorForSearch2();
                    }else{
                        firstEnter = false;
                    }

                }

            }
        });
    }
    private void setAnimatorForSearch1(){
        ViewHelper.setPivotX(searchIcon,0f);
        float searchIconWBegin = UIUtils.getScreenWidth() -UIUtils.dip2px(10)*2 - UIUtils.dip2px(64)- UIUtils.dip2px(20);
        float searchIconWEnd = UIUtils.getScreenWidth() -UIUtils.dip2px(10)*2;
        float X = searchIconWEnd/searchIconWBegin;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(searchIcon, "scaleX", 1f, X);
        scaleX.setDuration(200);//设置动画时间
        scaleX.setInterpolator(new DecelerateInterpolator());//设置动画插入器，减速
        //alpha.setRepeatCount(-1);//设置动画重复次数，这里-1代表无限
        //alpha.setRepeatMode(Animation.REVERSE);//设置动画循环模式。
        scaleX.start();//启动动画。

    }
    private void setAnimatorForSearch2(){
        ViewHelper.setPivotX(searchIcon,0f);
        float searchIconWBegin = UIUtils.getScreenWidth() -UIUtils.dip2px(10)*2 - UIUtils.dip2px(64)- UIUtils.dip2px(20);
        float searchIconWEnd = UIUtils.getScreenWidth() -UIUtils.dip2px(10)*2;
        float X = searchIconWEnd/searchIconWBegin;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(searchIcon, "scaleX", X, 1f);
        scaleX.setDuration(200);//设置动画时间
        scaleX.setInterpolator(new DecelerateInterpolator());//设置动画插入器，减速
        //alpha.setRepeatCount(-1);//设置动画重复次数，这里-1代表无限
        //alpha.setRepeatMode(Animation.REVERSE);//设置动画循环模式。
        scaleX.start();//启动动画。
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
        if(!NetWork.isNetworkAvailable(MyApplication.getAppContext())){
            Toast.makeText(MyApplication.getAppContext(),R.string.net_broken,Toast.LENGTH_LONG).show();
        }
        if(request instanceof RequestHomeNew){
            //homePageAdapter.addNetworkErrorModel(this);
        }else if (request instanceof FavoriteGuideSaved){
            Log.d("FavoriteGuideSaved",errorInfo.toString());
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestHomeNew) {
            homeBean = (HomeAggregationVo4) request.getData();
            if (homeBean != null) {
                    if(homeBean.bannerActivityList !=null && homeBean.bannerActivityList.size() >0){
                        homeAdapter.addHomeTitleBannar(getContext(),homeBean.bannerActivityList);
                    }

                    homeAdapter.addHomeService(getContext());
                    homeAdapter.addGuideModels(getActivity(),homeBean.qualityGuides);
                    homeAdapter.addHomeH5(getContext());
                    if(homeBean!= null && homeBean.hotAlbumList.size()>0){
                        for (int i= 0; i<homeBean.hotAlbumList.size();i++){
                            homeAdapter.addHotAlbum(getActivity(),homeBean.hotAlbumList.get(i),i);
                        }
                    }
//                if(homeBean!= null && homeBean.pastAlbumList.size()>0){
//                    for (int i= 0; i<homeBean.pastAlbumList.size();i++){
//                        homeAdapter.addPastAlbum(getContext(),homeBean.pastAlbumList.get(i));
//                    }
//                }
                    homeAdapter.addPastAlbum(getContext());

                //游客说
                if(homeBean != null && homeBean.commentList.size()>0){
                    for (int i = 0;i< homeBean.commentList.size(); i++){
                        homeAdapter.addHomeGuideEvaluate(getContext(),homeBean.commentList.get(i),i);
                    }
                }

                //推荐线路
                if(homeBean.cityGoodsList.size() >=2){
                    for (int i = 0; i < 2; i++) {
                        homeAdapter.addHomeRecommentRout(getContext(), homeBean.cityGoodsList.get(i));
                    }
                }

                //推荐线路广告
                homeAdapter.addHomeBanner(getContext(),homeBean.excitingActivityList);

                //其余城市线路推荐
                if(homeBean.cityGoodsList.size() >2){
                    for(int j = 2;j< homeBean.cityGoodsList.size();j++){
                        homeAdapter.addHomeRecommentRout(getContext(), homeBean.cityGoodsList.get(j));
                    }
                }

                //底部活动
                homeAdapter.addHomeBottomBanner();

            }
            //重新请求已收藏司导状态
            if(UserEntity.getUser().isLogin(getContext())){
                FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(getContext(),UserEntity.getUser().getUserId(getContext()),null);
                HttpRequestUtils.request(getContext(),favoriteGuideSaved,this,false);
            }

        }else if (request instanceof FavoriteGuideSaved){

            if(homeBean==null){
                return;
            }

            for(int j=0;j<homeBean.qualityGuides.size();j++){
                homeBean.qualityGuides.get(j).isCollected  = 0;
            }
            if(request.getData() instanceof UserFavoriteGuideListVo3){
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

                    homeAdapter.notifyDataSetChanged();
                }
                break;
            case ORDER_DETAIL_UPDATE_COLLECT:
                FavoriteGuideSaved favoriteGuideSaved = new FavoriteGuideSaved(getContext(),UserEntity.getUser().getUserId(getContext()),null);
                HttpRequestUtils.request(getContext(),favoriteGuideSaved,this,false);
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
        switch (view.getId()){
            case R.id.search_icon_layout:
                goChooseCity();
                break;

        }

    }
}
