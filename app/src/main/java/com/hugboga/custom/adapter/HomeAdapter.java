package com.hugboga.custom.adapter;

import android.app.Activity;
import android.content.Context;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.bean.HomeAggregationVo4;
import com.hugboga.custom.data.bean.HomeAlbumInfoVo;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.data.bean.HomeCityContentVo2;
import com.hugboga.custom.data.bean.HomeCommentInfoVo;
import com.hugboga.custom.fragment.FgHomePage;
import com.hugboga.custom.models.ChoicenessGuideModel;
import com.hugboga.custom.models.HomeBannerModel;
import com.hugboga.custom.models.HomeBottomBannerModel;
import com.hugboga.custom.models.HomeFilterGuideModel;
import com.hugboga.custom.models.HomeGuideEvaluateModel;
import com.hugboga.custom.models.HomeH5Model;
import com.hugboga.custom.models.HomeHotAlnum;
import com.hugboga.custom.models.HomeNetworkErrorModel;
import com.hugboga.custom.models.HomePastAlbum;
import com.hugboga.custom.models.HomeRecommendedRouteModel;
import com.hugboga.custom.models.HomeServiceModel;
import com.hugboga.custom.models.HomeTitleBannarModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqiang on 17/8/1.
 */

public class HomeAdapter extends EpoxyAdapter {
    HomeNetworkErrorModel homeNetworkErrorModel;
    public HomeTitleBannarModel homeTitleBannarModel;
    public HomeServiceModel homeServiceModel;
    public HomeH5Model homeH5Model;
    public HomeFilterGuideModel homeFilterGuideModel;
    public HomeHotAlnum homeHotAlnum;
    public HomePastAlbum homePastAlbum;
    public HomeGuideEvaluateModel homeGuideEvaluateModel;
    public HomeRecommendedRouteModel homeRecommendedRouteModel;
    public HomeBannerModel homeBannerModel;
    public HomeBottomBannerModel homeBottomBannerModel;


    public void addHomeTitleBannar(Context context, ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activityPageSettings) {
        if(activityPageSettings != null && activityPageSettings.size()>0){
            homeTitleBannarModel = new HomeTitleBannarModel(context, activityPageSettings);
            addModel(homeTitleBannarModel);
        }

    }

    public void addHomeService(Context context) {
        homeServiceModel = new HomeServiceModel(context);
        addModel(homeServiceModel);
    }

    public void addGuideModels(Activity activity, List<FilterGuideBean> guideList) {

        if (guideList != null && guideList.size() > 0) {
            homeFilterGuideModel = new HomeFilterGuideModel(activity);
            homeFilterGuideModel.setGuideData(guideList);
            addModel(homeFilterGuideModel);
        }
    }

    public void addHomeH5(Context context) {
        homeH5Model = new HomeH5Model(context);
        addModel(homeH5Model);
    }

    public void addHotAlbum(Activity activity, HomeAlbumInfoVo homeAlbumInfoVo, int position) {
        if(homeAlbumInfoVo != null){
            homeHotAlnum = new HomeHotAlnum(activity);
            homeHotAlnum.setAlbumData(homeAlbumInfoVo, position);
            addModel(homeHotAlnum);
        }
    }

    public void addPastAlbum(Context context, ArrayList<HomeAlbumInfoVo> pastAlbumList) {
        if(pastAlbumList!= null && pastAlbumList.size()>0){
            homePastAlbum = new HomePastAlbum(context, pastAlbumList);
            addModel(homePastAlbum);
        }
    }

    public void addHomeGuideEvaluate(Context context, HomeCommentInfoVo homeCommentInfoVo, int position) {
        if(homeCommentInfoVo != null){
            homeGuideEvaluateModel = new HomeGuideEvaluateModel(context, homeCommentInfoVo, position);
            addModel(homeGuideEvaluateModel);
        }
    }

    public void addHomeRecommentRout(Context context, HomeCityContentVo2 cityGoodsList) {
        if (cityGoodsList != null) {
            homeRecommendedRouteModel = new HomeRecommendedRouteModel(context, cityGoodsList);
            addModel(homeRecommendedRouteModel);
        }
    }

    public void addHomeBanner(Context context, ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activityPageSettings) {
        if(activityPageSettings != null && activityPageSettings.size()>0) {
            homeBannerModel = new HomeBannerModel(context, activityPageSettings);
            addModel(homeBannerModel);
        }
    }

    public void addHomeBottomBanner(Context context) {
        homeBottomBannerModel = new HomeBottomBannerModel(context);
        addModel(homeBottomBannerModel);
    }

    public void addNetworkErrorModel(HomeNetworkErrorModel.ReloadListener reloadListener) {
        if (homeNetworkErrorModel == null) {
            homeNetworkErrorModel = new HomeNetworkErrorModel(reloadListener);
        }
        removeNetworkErrorModel();
        addModel(homeNetworkErrorModel);
    }

    public void removeNetworkErrorModel() {
        if (homeNetworkErrorModel != null) {
            removeModel(homeNetworkErrorModel);
        }
    }

    public void removeModels(){
        if(getItemCount() > 0){
            removeAllModels();
        }
    }
}
