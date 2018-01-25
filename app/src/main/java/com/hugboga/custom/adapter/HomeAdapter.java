package com.hugboga.custom.adapter;

import com.airbnb.epoxy.EpoxyAdapter;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.models.home.HomeAiModel;
import com.hugboga.custom.models.home.HomeAlbumModel;
import com.hugboga.custom.models.home.HomeBannerModel;
import com.hugboga.custom.models.home.HomeEmptyModel;
import com.hugboga.custom.models.home.HomeExcitedActivityModel;
import com.hugboga.custom.models.home.HomeGoodsModel;
import com.hugboga.custom.models.home.HomeServiceCenterModel;

/**
 * Created by qingcha on 17/11/22.
 */
public class HomeAdapter extends EpoxyAdapter {

    public HomeAiModel homeAiModel;
    public HomeBannerModel homeBannerModel;
    public HomeEmptyModel emptyModel;
    public HomeExcitedActivityModel excitedActivityModel;

    public HomeAdapter() {
        homeAiModel = new HomeAiModel();
        homeBannerModel = new HomeBannerModel();
    }

    public void setData(HomeBean homeBean) {
        removeAllModels();

        if (homeBean.bannerInfolList != null && homeBean.bannerInfolList.size() > 0) {
            homeBannerModel.setData(homeBean.bannerInfolList);
            addModel(homeBannerModel);
        }

        addModel(homeAiModel);

        addModel(new HomeServiceCenterModel());

        if (homeBean.hotAlbumList != null && homeBean.hotAlbumList.size() > 0) {
            int size = homeBean.hotAlbumList.size();
            for (int i = 0; i < size; i++) {
                HomeAlbumModel homeAlbumModel = new HomeAlbumModel();
                homeAlbumModel.setDate(homeBean.hotAlbumList.get(i));
                addModel(homeAlbumModel);
            }
        }

        HomeGoodsModel transferModel = new HomeGoodsModel<HomeBean.TransferBean>();
        transferModel.setDate(homeBean.transferList, HomeGoodsModel.TYPE_TRANSFER);
        addModel(transferModel);

        HomeGoodsModel charteredModel = new HomeGoodsModel<HomeBean.CharteredBean>();
        charteredModel.setDate(homeBean.charteredList, HomeGoodsModel.TYPE_CHARTERED);
        addModel(charteredModel);

        if (homeBean.excitingActivityList != null && homeBean.excitingActivityList.size() > 0) {
            excitedActivityModel = new HomeExcitedActivityModel();
            excitedActivityModel.setData(homeBean.excitingActivityList);
            addModel(excitedActivityModel);
        }
    }

    public void addEmptyModel() {
        removeAllModels();
        if (emptyModel == null) {
            emptyModel = new HomeEmptyModel();
        }
        addModel(emptyModel);
    }

    public void stopAutoScroll() {
        if (homeBannerModel != null && homeBannerModel.itemView != null) {
            homeBannerModel.itemView.stopAutoScroll();
        }
    }

    public void startAutoScroll() {
        if (homeBannerModel != null && homeBannerModel.itemView != null) {
            homeBannerModel.itemView.startAutoScroll();
        }
    }
}
