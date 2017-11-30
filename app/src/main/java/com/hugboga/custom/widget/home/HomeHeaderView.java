package com.hugboga.custom.widget.home;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.data.bean.HomeTopBean;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.recyclerviewpager.LoopRecyclerViewPager;
import com.hugboga.custom.widget.recyclerviewpager.RVViewUtils;
import com.hugboga.custom.widget.recyclerviewpager.RecyclerViewPager;
import com.hugboga.custom.widget.recyclerviewpager.RecyclerViewPagerIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/11/25.
 */
public class HomeHeaderView extends LinearLayout implements HbcViewBehavior {


    @BindView(R.id.home_header_title_tv)
    TextView titleTV;
    @BindView(R.id.home_header_view_pager)
    LoopRecyclerViewPager mViewPager;
    @BindView(R.id.home_header_indicator)
    RecyclerViewPagerIndicator indicatorView;

    private ArrayList<HomeTopBean> itemList;
    private HbcRecyclerSingleTypeAdpater<HomeTopBean> mAdapter;

    public HomeHeaderView(Context context) {
        this(context, null);
    }

    public HomeHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        inflate(context, R.layout.view_home_header, this);
        ButterKnife.bind(this);

        final int marginLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_margin_left);
        int pagerWidth = UIUtils.getScreenWidth() - marginLeft;
        int desplayHeight = (int) (HomeHeaderItemView.DESPLAY_RATIO * pagerWidth);
        int pagerHeight = desplayHeight + UIUtils.dip2px(210);
        LayoutParams params = new LayoutParams(pagerWidth, pagerHeight);
        mViewPager.setLayoutParams(params);

        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mViewPager.setLayoutManager(layout);
        mAdapter = new HbcRecyclerSingleTypeAdpater(getContext(), HomeHeaderItemView.class);
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                int position = mViewPager.transformToActualPosition(newPosition);
                if (itemList != null) {
                    final int size = itemList.size();
                    if (size == 2 && position > 1) {
                        position = position % 2;
                    }
                    if (position < size && itemList.get(position) != null) {
                        titleTV.setText(itemList.get(position).headerTitle);
                    }
                }
                indicatorView.onPageChanged(position);
            }
        });
    }

    @Override
    public void update(Object _data) {
        itemList = (ArrayList<HomeTopBean>) _data;
        if (itemList == null || itemList.size() <= 0) {
            this.setVisibility(View.GONE);
            return;
        }
        this.setVisibility(View.VISIBLE);
        RVViewUtils.setDataCompat(mAdapter, itemList);
        indicatorView.setItemCount(itemList.size());
        mViewPager.scrollToMiddlePosition();
    }

}