package com.hugboga.custom.models;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyHolder;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.adapter.HomeRecommendedRoutAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeCityContentVo2;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.UIUtils;
import com.tmall.ultraviewpager.UltraViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/7/31.
 */

public class HomeRecommendedRouteModel extends EpoxyModelWithHolder {
    HomeRecommendedRouteHolder homeRecommendedRouteHolder;
    Context context;
    HomeCityContentVo2 homeCityContentVo2;
    PagerAdapter adapter;

    @Override
    protected EpoxyHolder createNewHolder() {
        return new HomeRecommendedRouteHolder();
    }

    public HomeRecommendedRouteModel(Context context, HomeCityContentVo2 homeCityContentVo2) {
        this.context = context;
        this.homeCityContentVo2 = homeCityContentVo2;
    }

    @Override
    protected int getDefaultLayout() {
        return R.layout.home_recomment_rout;
    }

    @Override
    public void bind(EpoxyHolder holder) {
        super.bind(holder);
        if (holder == null) {
            return;
        }
        homeRecommendedRouteHolder = (HomeRecommendedRouteHolder) holder;

        if (homeRecommendedRouteHolder != null) {
            //if (adapter == null) {
            homeRecommendedRouteHolder.ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
            if(homeCityContentVo2.cityItemList.size() == 1){
                homeRecommendedRouteHolder.ultraViewPager.disableIndicator();
                homeRecommendedRouteHolder.ultraViewPager.setInfiniteLoop(false);
                homeRecommendedRouteHolder.ultraViewPager.setAutoMeasureHeight(true);
                //initialize UltraPagerAdapter，and add child view to UltraViewPager
                adapter = new HomeRecommendedRoutAdapter(context, homeCityContentVo2);
//                homeRecommendedRouteHolder.ultraViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                    @Override
//                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//                    }
//
//                    @Override
//                    public void onPageSelected(int position) {
//                        Log.i("aa", "position" + position);
//                        int index = homeRecommendedRouteHolder.ultraViewPager.getCurrentItem();
//                        if(index < homeCityContentVo2.cityGoodsList.size()){
//                            setData(homeCityContentVo2.cityName, homeCityContentVo2.cityGoodsList.get(index));
//                        }
//                    }
//
//                    @Override
//                    public void onPageScrollStateChanged(int state) {
//
//                    }
//                });
                homeRecommendedRouteHolder.ultraViewPager.setAdapter(adapter);
                homeRecommendedRouteHolder.leftClick.setVisibility(View.GONE);
                homeRecommendedRouteHolder.rightClick.setVisibility(View.GONE);
                //setData(homeCityContentVo2.cityName, homeCityContentVo2.cityGoodsList.get(0));
            }else if (homeCityContentVo2.cityItemList.size() > 1) {
                //initialize built-in indicator
                homeRecommendedRouteHolder.ultraViewPager.initIndicator();
                //set style of indicators
                homeRecommendedRouteHolder.ultraViewPager.getIndicator()
                        .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                        .setFocusColor(0xFFFFC118)
                        .setNormalColor(0xFFDBDBDB)
                        .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()));
                //set the alignment
                homeRecommendedRouteHolder.ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM).setMargin(0,0,0, UIUtils.dip2px(20));
                //construct built-in indicator, and add it to  UltraViewPager
                homeRecommendedRouteHolder.ultraViewPager.getIndicator().build();

                //set an infinite loop
                homeRecommendedRouteHolder.ultraViewPager.setInfiniteLoop(true);
                //homeRecommendedRouteHolder.ultraViewPager.setAutoScroll(3000);
                homeRecommendedRouteHolder.ultraViewPager.setAutoMeasureHeight(true);
                homeRecommendedRouteHolder.ultraViewPager.setOffscreenPageLimit(3);

                //homeRecommendedRouteHolder.ultraViewPager.setMultiScreen(0.9f);
                //homeRecommendedRouteHolder.ultraViewPager.setPageMargin();
                //homeRecommendedRouteHolder.ultraViewPager.setPageTransformer(false, new UltraScaleTransformer());
                homeRecommendedRouteHolder.ultraViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        Log.i("aa", "position" + position);
                        int index = homeRecommendedRouteHolder.ultraViewPager.getCurrentItem();
                        //setData(homeCityContentVo2.cityName, homeCityContentVo2.cityGoodsList.get(index));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                //initialize UltraPagerAdapter，and add child view to UltraViewPager
                adapter = new HomeRecommendedRoutAdapter(context, homeCityContentVo2);
                homeRecommendedRouteHolder.ultraViewPager.setAdapter(adapter);
                //setData(homeCityContentVo2.cityName, homeCityContentVo2.cityGoodsList.get(0));
                homeRecommendedRouteHolder.leftClick.setVisibility(View.VISIBLE);
                homeRecommendedRouteHolder.leftClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        homeRecommendedRouteHolder.ultraViewPager.getViewPager().arrowScroll(1);
                    }
                });
                homeRecommendedRouteHolder.rightClick.setVisibility(View.VISIBLE);
                homeRecommendedRouteHolder.rightClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        homeRecommendedRouteHolder.ultraViewPager.getViewPager().arrowScroll(2);
                    }
                });
            }
            homeRecommendedRouteHolder.filter_guide.setText(homeCityContentVo2.cityName + context.getResources().getString(R.string.custom_recommend_line));
            homeRecommendedRouteHolder.filterGuideMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CityListActivity.Params params = new CityListActivity.Params();
                    params.id = homeCityContentVo2.cityId;
                    params.cityHomeType = CityListActivity.CityHomeType.CITY;
                    params.titleName = homeCityContentVo2.cityName;
                    intentActivity(context, CityListActivity.class, getEventSource(), params);
                    SensorsUtils.onAppClick(getEventSource(),"推荐线路","首页-推荐线路");
                }
            });

        }

    }

    public void update() {

    }

    static class HomeRecommendedRouteHolder extends EpoxyHolder {
        View itemView;
        @Bind(R.id.ultra_viewpager)
        UltraViewPager ultraViewPager;
        @Bind(R.id.filter_guide_more)
        TextView filterGuideMore;
        @Bind(R.id.filter_guide)
        TextView filter_guide;
        @Bind(R.id.click_viewpager)
        RelativeLayout clickViewpager;
        @Bind(R.id.right_click)
        ImageView rightClick;
        @Bind(R.id.left_click)
        ImageView leftClick;


        @Override
        protected void bindView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            int imageWidth = UIUtils.getScreenWidth();
            int imageHeight = (int)(imageWidth * 210.0 / 375.0);
            ultraViewPager.getLayoutParams().height = imageHeight + UIUtils.dip2px(250);
            //click按钮上下居中
            clickViewpager.getLayoutParams().height = imageHeight;
        }
    }

//    private void setData(String cityName, final HomeCityGoodsVo homeCityGoodsVo) {
//        if (homeRecommendedRouteHolder != null) {
//            homeRecommendedRouteHolder.des1.setText(homeCityGoodsVo.goodsName);
//            homeRecommendedRouteHolder.cityName.setText(cityName + "司导推荐");
//            homeRecommendedRouteHolder.guidesNum.setText(homeCityGoodsVo.guidesNum + "位中文司导可服务");
//            homeRecommendedRouteHolder.des2.setText(homeCityGoodsVo.recommendedReason);
//            homeRecommendedRouteHolder.tiyan.setText("已体验" + homeCityGoodsVo.purchases);
//            homeRecommendedRouteHolder.perPrice.setText("¥" + homeCityGoodsVo.perPrice + "起/人");
//            homeRecommendedRouteHolder.filter_guide.setText(cityName + "推荐线路");
//            if (!TextUtils.isEmpty(homeCityGoodsVo.goodsPic)) {
//                Tools.showRoundImage(homeRecommendedRouteHolder.polygonImageView, homeCityGoodsVo.guidePic, UIUtils.dip2px(5),R.mipmap.icon_avatar_guide);
//            }
//            homeRecommendedRouteHolder.filterGuideMore.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    CityListActivity.Params params = new CityListActivity.Params();
//                    params.id = homeCityContentVo2.cityId;
//                    params.cityHomeType = CityListActivity.CityHomeType.CITY;
//                    params.titleName = homeCityContentVo2.cityName;
//                    intentActivity(context, CityListActivity.class, getEventSource(), params);
//                    SensorsUtils.onAppClick(getEventSource(),"推荐线路","首页-推荐线路");
//                }
//            });
//            homeRecommendedRouteHolder.perPrice.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, SkuDetailActivity.class);
//                    intent.putExtra(WebInfoActivity.WEB_URL, homeCityGoodsVo.goodsDetailUrl);
//                    intent.putExtra(Constants.PARAMS_ID, homeCityGoodsVo.goodsNo);
//                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
//                    context.startActivity(intent);
//                    SensorsUtils.onAppClick(getEventSource(),"推荐线路","首页-推荐线路");
//                }
//            });
//
//        }
//
//    }

    private void intentActivity(Context context, Class<?> cls, String eventId, CityListActivity.Params params) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        intent.putExtra(Constants.PARAMS_DATA, params);
        context.startActivity(intent);
        if (!TextUtils.isEmpty(eventId)) {
            MobClickUtils.onEvent(eventId);
        }
    }

    public String getEventSource() {
        return "首页";
    }
}
