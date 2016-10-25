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

import com.bumptech.glide.Glide;
import com.hugboga.custom.R;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/6/19.
 */
public class HomeBannerView extends RelativeLayout implements HbcViewBehavior{

    /**
     * banner默认高宽比  height/width = 460/720
     */
    public static final float BANNER_RATIO_DEFAULT = 0.639f;

    @Bind(R.id.home_banner_bg_iv)
    ImageView bannerBgIV;

    private int bannerHeight;

    public HomeBannerView(Context context) {
        this(context, null);
    }

    public HomeBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_home_banner, this);
        ButterKnife.bind(this, view);

        bannerHeight = (int)(UIUtils.getScreenWidth() * BANNER_RATIO_DEFAULT);
        RelativeLayout.LayoutParams bgParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, bannerHeight);
        bannerBgIV.setLayoutParams(bgParams);
        Tools.showGif(bannerBgIV, R.drawable.rock);

    }

    @Override
    public void update(Object _data) {
    }

    public int getBannerHeight() {
        return bannerHeight;
    }

}
