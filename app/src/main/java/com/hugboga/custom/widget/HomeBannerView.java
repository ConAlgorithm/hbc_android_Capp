package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeBannerAdapter;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeBannerView extends RelativeLayout implements HbcViewBehavior{

    /**
     * banner默认高宽比  height/width = 639/750
     */
    public static final float BANNER_RATIO_DEFAULT = 0.85f;

    /**
     * banner默认自动切换的时间
     */
    private static final int BANNER_SWITCH_TIME_DEFAULT = 5000;

    private LoopViewPager mViewPager;

    private ArrayList<String> bannerList;
    private int cutIndex;
    private HomeBannerAdapter mAdapter;
    private Handler cutHandler;
    private Runnable cutRunnable;
    private boolean isAutoLoops = true;
    private int viewHeight;

    public HomeBannerView(Context context) {
        this(context, null);
    }

    public HomeBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewHeight = (int)(UIUtils.getScreenWidth() * BANNER_RATIO_DEFAULT);
    }

    public void onDestroy() {
        mAdapter = null;
        if (mViewPager != null) {
            mViewPager.setAdapter(null);
        }
        bannerList = null;
    }

    @Override
    public void update(Object _data) {
        this.bannerList = (ArrayList<String>) _data;
        if (bannerList == null || bannerList.size() <= 0) {
            return;
        }
        if (bannerList.size() == 1) {
            removeAllViews();
            onDestroyHandler();
            ImageView itemView = new ImageView(getContext());
            Tools.showImage(getContext(), itemView, bannerList.get(0));

            this.addView(itemView, RelativeLayout.LayoutParams.MATCH_PARENT, viewHeight);
        } else {
            if (mViewPager == null) {
                removeAllViews();
                mViewPager = new LoopViewPager(getContext());
                mViewPager.setBoundaryCaching(true);
                this.addView(mViewPager, RelativeLayout.LayoutParams.MATCH_PARENT, viewHeight);
                mViewPager.setOnPageChangeListener(new BannerCutListener());
                mViewPager.setPageTransformer(true, new StackTransformer());
                mViewPager.setScrolDuration(800);
            }
            if (mAdapter == null) {
                mAdapter = new HomeBannerAdapter(getContext(), bannerList);
                mViewPager.setAdapter(mAdapter);
            } else {
                onDestroyHandler();
                mAdapter.setData(bannerList, mViewPager);
                mViewPager.setCurrentItem(0, false);
            }
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
                            || bannerList == null
                            || bannerList.size() <= 0) {
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
//            if (isAutoLoops && cutHandler != null && cutRunnable != null) {
//                if (ViewPager.SCROLL_STATE_DRAGGING == state) {
//                    cutHandler.removeCallbacks(cutRunnable);
//                } else if (ViewPager.SCROLL_STATE_IDLE == state) {
//                    cutHandler.removeCallbacks(cutRunnable);
//                    cutHandler.postDelayed(cutRunnable, getCutTime());
//                }
//            }
        }
        @Override
        public void onPageSelected(int position) {
            cutIndex = position;
        }
    }

    public void onDestroyHandler() {
//        if (cutHandler != null && cutRunnable != null) {
//            cutHandler.removeCallbacks(cutRunnable);
//        }
    }

    public void onStartChange() {
//        if (mViewPager != null && cutHandler != null && cutRunnable != null) {
//            mViewPager.setPageTransformer(true, new StackTransformer());
//            mViewPager.setScrolDuration(800);
//            cutHandler.removeCallbacks(cutRunnable);
//            cutHandler.postDelayed(cutRunnable, getCutTime());
//        }
    }

    protected int getCutTime() {
        return BANNER_SWITCH_TIME_DEFAULT;
    }

    private class StackTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            final float width = view.getWidth();
            ViewHelper.setTranslationY(view, 0);
            ViewHelper.setTranslationX(view, -width * position);
            final float scale = 1f + Math.abs(position);
            ViewHelper.setAlpha(view,position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
//            if(position < -0.9){
//                //-0.9 to prevent a small bug
//                ViewHelper.setTranslationX(view,view.getWidth() * position);
//            }
        }
    }
}
