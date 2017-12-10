package com.hugboga.custom.widget.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.recyclerviewpager.LoopRecyclerViewPager;
import com.hugboga.custom.widget.recyclerviewpager.RVViewUtils;
import com.hugboga.custom.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/11/22.
 */
public class HomeBannerView extends LinearLayout implements HbcViewBehavior {

    @BindView(R.id.home_indicator_layout)
    FrameLayout indicatorLayout;
    @BindView(R.id.home_indicator_view)
    HomeIndicatorView indicatorView;
    @BindView(R.id.home_view_pager)
    LoopRecyclerViewPager mViewPager;

    private ArrayList<HomeBean.BannerBean> itemList;
    private HbcRecyclerSingleTypeAdpater<HomeBean.BannerBean> mAdapter;
    private int bannerHeight;

    public HomeBannerView(Context context) {
        this(context, null);
    }

    public HomeBannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        inflate(context, R.layout.view_home_banner, this);
        ButterKnife.bind(this);

        final int marginLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_margin_left);
        int bannerWidth = UIUtils.getScreenWidth() - marginLeft;
        int desplayHeight = (int)(HomeBannerItemView.DESPLAY_IMG_RATIO * bannerWidth);
        bannerHeight = desplayHeight + + UIUtils.dip2px(50) + UIUtils.dip2px(80);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, bannerHeight);
        mViewPager.setLayoutParams(params);
        indicatorLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, desplayHeight));

        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mViewPager.setLayoutManager(layout);
        mViewPager.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                indicatorView.setCurrentPosition(mViewPager.transformToActualPosition(newPosition));
            }
        });
    }

    @Override
    public void update(Object _data) {
        itemList = (ArrayList<HomeBean.BannerBean>) _data;
        if (itemList == null || itemList.size() <= 0) {
            this.setVisibility(View.GONE);
            return;
        }
        this.setVisibility(View.VISIBLE);
        if (mAdapter == null) {
            mAdapter = new HbcRecyclerSingleTypeAdpater(getContext(), HomeBannerItemView.class);
            RVViewUtils.setDataCompat(mAdapter, itemList);
            mViewPager.setAdapter(mAdapter);
        } else {
            RVViewUtils.setDataCompat(mAdapter, itemList);
            mAdapter.notifyDataSetChanged();
            mViewPager.scrollToPosition(mViewPager.getMiddlePosition());
        }
        indicatorView.setItemCount(itemList.size());
    }

    public int getBannerLayoutHeight() {
        return bannerHeight;
    }
}
