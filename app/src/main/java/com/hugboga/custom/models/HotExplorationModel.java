package com.hugboga.custom.models;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeHotSearchViewPagerAdapter;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SPW on 2017/3/9.
 */
public class HotExplorationModel extends EpoxyModelWithHolder {

    private HomeBeanV2.HotExploration hotExploration;

    public HotExplorationModel(HomeBeanV2.HotExploration hotExploration) {
        this.hotExploration = hotExploration;
    }

    @Override
    protected HotExplorationHolder createNewHolder() {
        return new HotExplorationHolder();
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        HotExplorationHolder hotExplorationHolder = (HotExplorationHolder) holder;
        if(hotExploration.explorationType==1){
            hotExplorationHolder.homeHotTitle.setText(hotExploration.explorationName);
            hotExplorationHolder.guideCity.setText("/" + hotExploration.explorationName+"司导");
            hotExplorationHolder.guideCity.setVisibility(View.VISIBLE);
            hotExplorationHolder.cityNameIcon.setVisibility(View.VISIBLE);
        }else{
            hotExplorationHolder.homeHotTitle.setText("/  "+hotExploration.explorationName+"  /");
            hotExplorationHolder.guideCity.setVisibility(View.GONE);
            hotExplorationHolder.cityNameIcon.setVisibility(View.GONE);
        }

        hotExplorationHolder.guideName.setText(hotExploration.guideName);
        hotExplorationHolder.cityDesc.setText(hotExploration.guideRecommendedReason);
        int viewWidth = ScreenUtil.screenWidth - ScreenUtil.dip2px(40);
        int height = viewWidth * 200 / 325 + ScreenUtil.dip2px(95);
        hotExplorationHolder.cityViewPager.getLayoutParams().height = height;
        //hotExplorationHolder.cityViewPager.setOffscreenPageLimit(3);
        //hotExplorationHolder.cityViewPager.setPageMargin(20);
        //hotExplorationHolder.cityViewPager.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        hotExplorationHolder.cityViewPager.setAdapter(new HomeHotSearchViewPagerAdapter(hotExploration.explorationGoods));
    }


    @Override
    protected int getDefaultLayout() {
        return R.layout.home_hot_search_item;
    }

    @Override
    public boolean shouldSaveViewState() {
        return true;
    }


    static class HotExplorationHolder extends EpoxyHolder {
        @Bind(R.id.home_hot_search_title)
        TextView homeHotTitle;
        @Bind(R.id.home_hot_search_more)
        View moreView;
        @Bind(R.id.home_hot_search_guide_avtar)
        ImageView guideAvtar;
        @Bind(R.id.home_hot_search_guide_name)
        TextView guideName;
        @Bind(R.id.home_hot_search_guide_city)
        TextView guideCity;
        @Bind(R.id.home_hot_search_city_desc)
        TextView cityDesc;
        @Bind(R.id.home_hot_city_name_icon)
        ImageView cityNameIcon;
        @Bind(R.id.home_hot_search_city_viewpager)
        ViewPager cityViewPager;

        View itemView;

        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
