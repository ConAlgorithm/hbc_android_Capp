package com.hugboga.custom.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.hugboga.custom.R;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeBannerView extends RelativeLayout implements HbcViewBehavior, ViewSwitcher.ViewFactory{

    /**
     * banner默认高宽比  height/width = 639/750
     */
    public static final float BANNER_RATIO_DEFAULT = 0.85f;

    /**
     * banner默认自动切换的时间
     */
    private static final int BANNER_SWITCH_TIME_DEFAULT = 5000;

    private ViewSwitcher mSwitcherView;

    private ArrayList<String> bannerList;
    private Handler cutHandler;
    private Runnable cutRunnable;
    private boolean isAutoLoops = true;
    private int viewHeight;
    private int index;

    private boolean isDefaultData = true;
    private ArrayList<Integer> defaultData;

    public HomeBannerView(Context context) {
        this(context, null);
    }

    public HomeBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewHeight = (int)(UIUtils.getScreenWidth() * BANNER_RATIO_DEFAULT);

        mSwitcherView = new ViewSwitcher(context);
        mSwitcherView.setFactory(this);
        mSwitcherView.setInAnimation(AnimationUtils.loadAnimation(context, R.anim.home_banner_fade_in));
        mSwitcherView.setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.home_banner_fade_out));
        addView(mSwitcherView, LayoutParams.MATCH_PARENT, viewHeight);

        defaultData = new ArrayList<Integer>(4);
        defaultData.add(R.drawable.home_toppic1);
        defaultData.add(R.drawable.home_toppic2);
        defaultData.add(R.drawable.home_toppic3);
        defaultData.add(R.drawable.home_toppic4);
        isDefaultData = true;

        index = 0;
        ImageView img = (ImageView) mSwitcherView.getNextView();
        img.setImageResource(defaultData.get(index));
        mSwitcherView.showNext();
        index++;
        initCutHandler();
    }

    @Override
    public void update(Object _data) {
        this.bannerList = (ArrayList<String>) _data;
        if (bannerList == null || bannerList.size() <= 0) {
            isDefaultData = true;
            return;
        } else {
            isDefaultData = false;
        }
        index = 0;
        mSwitcherView.reset();
        ImageView img = (ImageView) mSwitcherView.getNextView();
        Tools.showImage(img, bannerList.get(index));
        mSwitcherView.showNext();
        if (bannerList.size() > 1) {
            index++;
            onStartChange();
        } else {
            onDestroyHandler();
        }
    }

    public void initCutHandler() {
        if (!isAutoLoops) return;
        if (cutHandler == null || cutRunnable == null) {
            cutHandler = new Handler();
            cutRunnable = new Runnable() {

                @Override
                public void run() {
                    ImageView img = (ImageView) mSwitcherView.getNextView();
                    int size = 0;
                    if (isDefaultData) {
                        size = defaultData.size();
                        img.setImageResource(defaultData.get(index));
                    } else {
                        if (bannerList == null || bannerList.size() <= 0) {
                            return;
                        }
                        size = bannerList.size();
                        Tools.showImage(img, bannerList.get(index));
                    }
                    mSwitcherView.showNext();
                    if (index == size - 1) {
                        index = 0;
                    } else {
                        index++;
                    }
                    cutHandler.removeCallbacks(this);
                    cutHandler.postDelayed(this, getCutTime());
                }
            };
        }
        cutHandler.removeCallbacks(cutRunnable);
        cutHandler.postDelayed(cutRunnable, getCutTime());
    }

    public void onDestroyHandler() {
        if (isAutoLoops) {
            isAutoLoops = false;
            if (cutHandler != null && cutRunnable != null) {
                cutHandler.removeCallbacks(cutRunnable);
            }
        }

    }

    public void onStartChange() {
        if (!isAutoLoops) {
            isAutoLoops = true;
            if (cutHandler != null && cutRunnable != null) {
                cutHandler.removeCallbacks(cutRunnable);
                cutHandler.postDelayed(cutRunnable, getCutTime());
            }
        }
    }

    protected int getCutTime() {
        return BANNER_SWITCH_TIME_DEFAULT;
    }

    @Override
    public View makeView() {
        ImageView imageView  = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        return imageView;
    }
}
