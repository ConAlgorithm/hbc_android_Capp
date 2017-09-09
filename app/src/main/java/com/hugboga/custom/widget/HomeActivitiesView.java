package com.hugboga.custom.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.action.ActionController;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.HomeAggregationVo4;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/10/24.
 */
public class HomeActivitiesView extends LinearLayout implements HbcViewBehavior{

    /**
     * banner默认高宽比  height/width = 240/646
     */
    private static final float BANNER_RATIO_DEFAULT = 0.371f;

    /**
     * banner默认自动切换的时间
     */
    private static final int BANNER_SWITCH_TIME_DEFAULT = 5000;

    @Bind(R.id.home_header_image)
    LoopViewPager mViewPager;
    @Bind(R.id.indicator)
    CirclePageIndicator mIndicator;

    private int cutIndex;
    private ArrayList<HomeAggregationVo4.ActivityPageSettingVo> activitiesList;
    private HomeBannerAdapter mAdapter;
    private Runnable cutRunnable;
    private Handler cutHandler;
    private boolean isAutoLoops = true;

    public HomeActivitiesView(Context context) {
        this(context, null);
    }

    public HomeActivitiesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_home_activities, this);
        ButterKnife.bind(this, view);

        //final int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        //int pagerWidth = UIUtils.getScreenWidth() - paddingLeft * 2;
        //int pagerHeight = (int)(pagerWidth * BANNER_RATIO_DEFAULT);
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(pagerWidth, pagerHeight);
        //params.addRule(RelativeLayout.CENTER_IN_PARENT);
        //params.addRule(RelativeLayout.BELOW, R.id.home_activities_title_tv);
        //mViewPager.setLayoutParams(params);
        mViewPager.setScanScroll(true);
        mViewPager.setBoundaryCaching(true);
        mViewPager.setScrolDuration(400);
    }

    @Override
    public void update(Object _data) {
        activitiesList = (ArrayList<HomeAggregationVo4.ActivityPageSettingVo>) _data;
        if (activitiesList == null || activitiesList.size() <= 0) {
            this.setVisibility(View.GONE);
            return;
        }
        this.setVisibility(View.VISIBLE);

        mAdapter = new HomeBannerAdapter(getContext(), activitiesList);
        mViewPager.setAdapter(mAdapter);

        if (activitiesList.size() == 1) {
            onDestroyHandler();
            mIndicator.setVisibility(View.GONE);
            mViewPager.setScanScroll(false);
        } else {
            mIndicator.setVisibility(VISIBLE);
            mIndicator.setViewPager(mViewPager);
            mViewPager.setScanScroll(true);
            mIndicator.setOnPageChangeListener(new BannerCutListener());
            initCutHandler();
        }
    }

    public void initCutHandler(boolean isAutoLoops) {
        this.isAutoLoops = isAutoLoops;
        initCutHandler();
    }

    public void initCutHandler() {
        if (!isAutoLoops) return;
        if (cutHandler == null || cutRunnable == null) {
            cutHandler = new Handler();
            cutRunnable = new Runnable() {

                @Override
                public void run() {
                    if (mAdapter == null
                            || activitiesList == null
                            || activitiesList.size() <= 0) {
                        return;
                    }
                    if (cutIndex == mAdapter.getCount() - 1) {
                        try {
                            cutIndex += 1;
                            mViewPager.setCurrentItem(cutIndex, true);
                            cutIndex = 0;
                        } catch (Exception e) {
                            cutIndex = 0;
                            mViewPager.setCurrentItem(cutIndex, false);
                        }
                    } else {
                        cutIndex += 1;
                        mViewPager.setCurrentItem(cutIndex, true);
                    }

                    cutHandler.removeCallbacks(this);
                    cutHandler.postDelayed(this, getCutTime());
                }
            };
        }
        cutHandler.removeCallbacks(cutRunnable);
        cutHandler.postDelayed(cutRunnable, getCutTime());
    }

    private class BannerCutListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
            if (isAutoLoops && cutHandler != null && cutRunnable != null) {
                if (ViewPager.SCROLL_STATE_DRAGGING == state) {
                    cutHandler.removeCallbacks(cutRunnable);
                } else if (ViewPager.SCROLL_STATE_IDLE == state) {
                    cutHandler.removeCallbacks(cutRunnable);
                    cutHandler.postDelayed(cutRunnable, getCutTime());
                }
            }
        }
        @Override
        public void onPageSelected(int position) {
            cutIndex = position;
        }
    }

    public void onDestroy() {
        mAdapter = null;
        if (mViewPager != null) {
            mViewPager.setAdapter(null);
        }
        activitiesList = null;
    }

    public void onDestroyHandler() {
        if (cutHandler != null && cutRunnable != null) {
            cutHandler.removeCallbacks(cutRunnable);
        }
    }

    public void onStartChange() {
        if (cutHandler != null && cutRunnable != null) {
            cutHandler.removeCallbacks(cutRunnable);
            cutHandler.postDelayed(cutRunnable, getCutTime());
        }
    }

    protected int getCutTime() {
        return BANNER_SWITCH_TIME_DEFAULT;
    }

    public static class HomeBannerAdapter extends PagerAdapter {

        private Context mContext;
        private ArrayList<HomeAggregationVo4.ActivityPageSettingVo> itemList;
        private ViewGroup.LayoutParams itemParams;

        public HomeBannerAdapter(Context mContext, ArrayList<HomeAggregationVo4.ActivityPageSettingVo> _itemParams) {
            this.mContext = mContext;
            this.itemList = _itemParams;
            itemParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if (itemList == null) {
                return super.instantiateItem(container, position);
            }
            final HomeAggregationVo4.ActivityPageSettingVo itemData = itemList.get(position);
            ImageView itemView = new ImageView(mContext);
            itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (itemData != null && !TextUtils.isEmpty(itemData.picture)) {
                Tools.showImageForHomePage(itemView, itemData.picture, R.mipmap.empty_home_banner);
            } else {
                itemView.setImageResource(R.mipmap.empty_home_banner);
            }
            itemView.setLayoutParams(itemParams);
            container.addView(itemView, 0);

            if(itemData == null ){
                return itemView;
            }

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (!UserEntity.getUser().isLogin(mContext)) {
                            CommonUtils.showToast(R.string.login_hint);
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra(Constants.PARAMS_SOURCE, "首页-活动");
                        mContext.startActivity(intent);
                        return;
                    }*/
                    if (itemData == null) {
                        return;
                    }
                    setSensorsShareEvent(itemData.urlAddress,position);
                    //EventUtil.onDefaultEvent(StatisticConstant.CLICK_ACTIVITY, "首页精选活动");
                    //StatisticClickEvent.click(StatisticConstant.LAUNCH_ACTIVITY, "首页活动详情展示");

//                    if(itemData.requestType == 1){
//                        Intent intent = new Intent(v.getContext(), WebInfoActivity.class);
//                        intent.putExtra(WebInfoActivity.WEB_URL, itemData.urlAddress);
//                        v.getContext().startActivity(intent);
//                    }else if(itemData.requestType == 2){
//                        ActionController actionFactory = ActionController.getInstance();
//                        actionFactory.doAction(mContext, itemData.pushScheme);
//                    }

                    if (itemData.pushScheme == null) {
                        if (!TextUtils.isEmpty(itemData.urlAddress)) {
                            Intent intent = new Intent(v.getContext(), WebInfoActivity.class);
                            intent.putExtra(WebInfoActivity.WEB_URL, itemData.urlAddress);
                            v.getContext().startActivity(intent);
                        }
                    } else {
                        ActionController actionFactory = ActionController.getInstance();
                        itemData.pushScheme.source = itemData.pushScheme.url;
                        actionFactory.doAction(mContext, itemData.pushScheme);
                    }
                }
            });
            return itemView;
        }

        @Override
        public int getCount() {
            return itemList == null ? 0 : itemList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewGroup) container.getParent()).removeView((View)object);
        }
    }

    //首页bannar
    public static void setSensorsShareEvent(String bannerUrl,int position) {
        try {
            JSONObject properties = new JSONObject();
            properties.put("bannerUrl", bannerUrl);
            properties.put("bannerNo", position);
            SensorsDataAPI.sharedInstance(MyApplication.getAppContext()).track("clickBanner", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
