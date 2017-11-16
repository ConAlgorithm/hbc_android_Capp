package com.hugboga.custom.models;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeBannerAdapter;
import com.hugboga.custom.data.bean.HomeAggregationVo4;
import com.hugboga.custom.utils.UIUtils;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/5.
 */

public class HomeBannerModel extends EpoxyModelWithHolder {
    ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activityPageSettings;
    Context context;
    HomeBannarHolder homeBannarHolder;
    PagerAdapter pagerAdapter;
    public HomeBannerModel(Context context,ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activityPageSettings){
        this.context = context;
        this.activityPageSettings = activityPageSettings;
    }

    static class HomeBannarHolder extends EpoxyHolder{
        View itemView;
        @BindView(R.id.ultra_viewpager)
        UltraViewPager ultraViewPager;
        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            int imageWidth = UIUtils.getScreenWidth()-2*UIUtils.dip2px(16);
            int imageHeight = imageWidth * 160 /328;
            ultraViewPager.getLayoutParams().height = imageHeight + UIUtils.dip2px(56);
        }
    }
    @Override
    protected EpoxyHolder createNewHolder() {
        return new HomeBannarHolder();
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_banner;
    }
    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        init(holder);
    }
    private void init(EpoxyHolder holder){
        homeBannarHolder = (HomeBannerModel.HomeBannarHolder) holder;
        if(homeBannarHolder != null){

            if(pagerAdapter == null){
                homeBannarHolder.ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);

                //if(homeCityContentVo2.cityGoodsList.size() > 1){
                //initialize built-in indicator
                homeBannarHolder.ultraViewPager.initIndicator();
                //set style of indicators
                homeBannarHolder.ultraViewPager.getIndicator()
                        .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                        .setFocusColor(0xFFFFC118)
                        .setNormalColor(0xFFDBDBDB)
                        .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()));
                //set the alignment
                homeBannarHolder.ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM).setMargin(0,0,0, UIUtils.dip2px(20));
                //construct built-in indicator, and add it to  UltraViewPager
                homeBannarHolder.ultraViewPager.getIndicator().build();

                //set an infinite loop
                homeBannarHolder.ultraViewPager.setInfiniteLoop(true);
                homeBannarHolder.ultraViewPager.setAutoScroll(3000);
                homeBannarHolder.ultraViewPager.setAutoMeasureHeight(true);
                //homeRecommendedRouteHolder.ultraViewPager.setMultiScreen(0.9f);
                //ultraViewPager.setItemRatio(1.0f);
                //homeBannarHolder.ultraViewPager.setItemMargin(0,0,0,10);


                //initialize UltraPagerAdapter，and add child view to UltraViewPager
                pagerAdapter = new HomeBannerAdapter(context,activityPageSettings);
                homeBannarHolder.ultraViewPager.setAdapter(pagerAdapter);
            }else {
                ((HomeBannerAdapter)pagerAdapter).setData(activityPageSettings);
            }

        }

    }
}
