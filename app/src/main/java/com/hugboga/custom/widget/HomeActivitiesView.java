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
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.action.ActionController;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Random;

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
    private static final int BANNER_SWITCH_TIME_DEFAULT = 3000;

    @Bind(R.id.home_activities_viewpager)
    LoopViewPager mViewPager;
    @Bind(R.id.home_activities_indicator)
    CirclePageIndicator mIndicator;

    private int cutIndex;
    private ArrayList<HomeBean.ActivePage> activitiesList;
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

        final int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_view_padding_left);
        int pagerWidth = UIUtils.getScreenWidth() - paddingLeft * 2;
        int pagerHeight = (int)(pagerWidth * BANNER_RATIO_DEFAULT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(pagerWidth, pagerHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.addRule(RelativeLayout.BELOW, R.id.home_activities_title_tv);
        mViewPager.setLayoutParams(params);
        mViewPager.setScanScroll(true);
        mViewPager.setBoundaryCaching(true);
        mViewPager.setScrolDuration(400);
    }

    @Override
    public void update(Object _data) {
        activitiesList = (ArrayList<HomeBean.ActivePage>) _data;
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
        private ArrayList<HomeBean.ActivePage> itemList;
        private ViewGroup.LayoutParams itemParams;

        public HomeBannerAdapter(Context mContext, ArrayList<HomeBean.ActivePage> _itemParams) {
            this.mContext = mContext;
            this.itemList = _itemParams;
            itemParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (itemList == null) {
                return super.instantiateItem(container, position);
            }
            final HomeBean.ActivePage itemData = itemList.get(position);
            ImageView itemView = new ImageView(mContext);
            itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (itemData != null && !TextUtils.isEmpty(itemData.picture)) {
                Tools.showRoundImage(itemView, itemData.picture, UIUtils.dip2px(3));
            } else {
                itemView.setImageResource(R.mipmap.home_default_route_item);
            }
            itemView.setLayoutParams(itemParams);
            container.addView(itemView, 0);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!UserEntity.getUser().isLogin(mContext)) {
                            CommonUtils.showToast(R.string.login_hint);
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        return;
                    }
                    if (itemData == null) {
                        return;
                    }
                    EventUtil.onDefaultEvent(StatisticConstant.CLICK_ACTIVITY, "首页精选活动");
                    EventUtil.onDefaultEvent(StatisticConstant.LAUNCH_ACTIVITY, "首页精选活动");
                    if (TextUtils.isEmpty(itemData.urlAddress) && itemData.actionBean != null) {
                        ActionController actionFactory = ActionController.getInstance(mContext);
                        actionFactory.doAction(itemData.actionBean);
                    } else if (!TextUtils.isEmpty(itemData.urlAddress)) {
                        String urlAddress = itemData.urlAddress;
                        if (urlAddress.lastIndexOf("?") != urlAddress.length() - 1) {
                            urlAddress = urlAddress + "?";
                        }
                        urlAddress = urlAddress + "userId="+ UserEntity.getUser().getUserId(mContext)+"&t=" + new Random().nextInt(100000);
                        Intent intent = new Intent(mContext, WebInfoActivity.class);
                        intent.putExtra(WebInfoActivity.WEB_URL, urlAddress);
                        mContext.startActivity(intent);
                        StatisticClickEvent.click(StatisticConstant.LAUNCH_TRAVELFOUND, "首页");
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
}
