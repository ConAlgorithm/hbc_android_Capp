package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.utils.UIUtils;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangqiang on 17/8/8.
 */

public class PurposeFormImgView extends LinearLayout implements HbcViewBehavior {

    private static final float BANNER_RATIO_DEFAULT = 0.371f;

    /**
     * banner默认自动切换的时间
     */
    private static final int BANNER_SWITCH_TIME_DEFAULT = 5000;

    @BindView(R.id.home_header_image)
    LoopViewPager mViewPager;
    @BindView(R.id.indicator)
    CirclePageIndicator mIndicator;

    private int cutIndex;
    private PurposeAdapter mAdapter;
    private Runnable cutRunnable;
    private Handler cutHandler;
    private boolean isAutoLoops = true;
    public PurposeFormImgView(Context context) {
        this(context, null);
    }

    public PurposeFormImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.purpose_form_activitues, this);
        ButterKnife.bind(this, view);

        mViewPager.setScanScroll(true);
        mViewPager.setBoundaryCaching(true);
        mViewPager.setScrolDuration(400);
    }

    @Override
    public void update(Object _data) {
        this.setVisibility(View.VISIBLE);

        mAdapter = new PurposeAdapter(getContext());
        mViewPager.setAdapter(mAdapter);
        mViewPager.getLayoutParams().width = UIUtils.getScreenWidth();
        mViewPager.getLayoutParams().height = mViewPager.getLayoutParams().width *192/360;
        mIndicator.setVisibility(VISIBLE);
        mIndicator.setViewPager(mViewPager);
        mViewPager.setScanScroll(true);
        mIndicator.setOnPageChangeListener(new BannerCutListener());
        initCutHandler();

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

    public static class PurposeAdapter extends PagerAdapter {

        private Context mContext;
        private Integer[] Pics = {R.mipmap.wish_banner_a, R.mipmap.wish_banner_b, R.mipmap.wish_banner_c, R.mipmap.wish_banner_d};
        private ViewGroup.LayoutParams itemParams;

        public PurposeAdapter(Context mContext) {
            this.mContext = mContext;
            itemParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            ImageView itemView = new ImageView(mContext);
            itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemView.setImageResource(Pics[position]);
            itemView.setLayoutParams(itemParams);
            container.addView(itemView, 0);

            return itemView;
        }

        @Override
        public int getCount() {
            return Pics == null ? 0 : Pics.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewGroup) container.getParent()).removeView((View) object);
        }
    }

    //首页bannar
    public static void setSensorsShareEvent(String bannerUrl, int position) {
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
