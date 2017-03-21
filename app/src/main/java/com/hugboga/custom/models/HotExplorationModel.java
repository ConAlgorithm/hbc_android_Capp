package com.hugboga.custom.models;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.ChooseCityNewActivity;
import com.hugboga.custom.activity.CityHomeListActivity;
import com.hugboga.custom.adapter.HomeHotSearchViewPagerAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBeanV2;
import com.hugboga.custom.utils.Tools;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by SPW on 2017/3/9.
 */
public class HotExplorationModel extends EpoxyModelWithHolder {

    public HomeBeanV2.HotExploration hotExploration;

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
        Tools.showImage(hotExplorationHolder.guideAvtar,hotExploration.guidePic,R.mipmap.icon_avatar_guide);
        hotExplorationHolder.guideName.setText(hotExploration.guideName);
        hotExplorationHolder.cityDesc.setText(hotExploration.guideRecommendedReason);
        int viewWidth = ScreenUtil.screenWidth - ScreenUtil.dip2px(40);
        int height = viewWidth * 200 / 325 + ScreenUtil.dip2px(95);
        hotExplorationHolder.cityViewPager.getLayoutParams().height = height;
        hotExplorationHolder.cityViewPager.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        hotExplorationHolder.cityViewPager.setAdapter(new HomeHotSearchViewPagerAdapter(hotExploration.explorationGoods,hotExploration));

        hotExplorationHolder.moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityHomeListActivity.Params params = new CityHomeListActivity.Params();
                params.id = hotExploration.explorationId;
                switch (hotExploration.explorationType) {
                    case 1:
                        params.cityHomeType = CityHomeListActivity.CityHomeType.CITY;
                        break;
                    case 2:
                        params.cityHomeType = CityHomeListActivity.CityHomeType.COUNTRY;
                        break;
                    case 3:
                        params.cityHomeType = CityHomeListActivity.CityHomeType.ROUTE;
                        break;
                    default:
                        return;
                }
                params.titleName = hotExploration.explorationName;
                Intent intent = new Intent(v.getContext(), CityHomeListActivity.class);
                intent.putExtra(Constants.PARAMS_DATA,params);
                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    protected int getDefaultLayout() {
        return R.layout.home_hot_search_item;
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
