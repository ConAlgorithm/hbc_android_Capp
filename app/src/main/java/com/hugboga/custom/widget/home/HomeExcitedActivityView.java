package com.hugboga.custom.widget.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.action.ActionController;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.utils.Tools;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.HbcViewBehavior;
import com.hugboga.custom.widget.recyclerviewpager.LoopRecyclerViewPager;
import com.hugboga.custom.widget.recyclerviewpager.RVViewUtils;
import com.hugboga.custom.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/11/24.
 */

public class HomeExcitedActivityView extends LinearLayout implements HbcViewBehavior {

    @BindView(R.id.home_excited_indicator_view)
    HomeIndicatorView indicatorView;
    @BindView(R.id.home_excited_view_pager)
    LoopRecyclerViewPager mViewPager;

    private ArrayList<HomeBean.ExcitedActivityBean> itemList;
    private HbcRecyclerSingleTypeAdpater<HomeBean.ExcitedActivityBean> mAdapter;

    public HomeExcitedActivityView(Context context) {
        this(context, null);
    }

    public HomeExcitedActivityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setPadding(0, 0, 0, UIUtils.dip2px(45));
        inflate(context, R.layout.view_home_excited_activity, this);
        ButterKnife.bind(this);

        final int marginLeft = context.getResources().getDimensionPixelOffset(R.dimen.home_margin_left);
        int bannerWidth = UIUtils.getScreenWidth() - marginLeft;
        int bannerHeight = (int) ((400 / 670.0f) * bannerWidth);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, bannerHeight);
        mViewPager.setLayoutParams(params);

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
        itemList = (ArrayList<HomeBean.ExcitedActivityBean>) _data;
        if (itemList == null || itemList.size() <= 0) {
            this.setVisibility(View.GONE);
            return;
        }
        this.setVisibility(View.VISIBLE);
        if (mAdapter == null) {
            mAdapter = new HbcRecyclerSingleTypeAdpater(getContext(), ItemImageView.class);
            RVViewUtils.setDataCompat(mAdapter, itemList);
            mViewPager.setAdapter(mAdapter);
        } else {
            RVViewUtils.setDataCompat(mAdapter, itemList);
            mAdapter.notifyDataSetChanged();
            mViewPager.scrollToPosition(mViewPager.getMiddlePosition());
        }
        indicatorView.setItemCount(itemList.size());
    }

    private static class ItemImageView extends AppCompatImageView implements HbcViewBehavior, OnClickListener {

        private HomeBean.ExcitedActivityBean itemData;

        public ItemImageView(Context context) {
            this(context, null);
        }

        public ItemImageView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            setScaleType(ImageView.ScaleType.CENTER_CROP);
            setOnClickListener(this);
        }

        @Override
        public void update(Object _data) {
            if (!(_data instanceof HomeBean.ExcitedActivityBean) || _data == null) {
                return;
            }
            itemData = (HomeBean.ExcitedActivityBean) _data;
            if (!TextUtils.isEmpty(itemData.picture)) {
                Tools.showImage(this, itemData.picture, R.mipmap.home_default_route_item);
            } else {
                setImageResource(R.mipmap.home_default_route_item);
            }
        }

        @Override
        public void onClick(View v) {
            if (itemData == null) {
                return;
            }
            if (itemData.pushScheme == null) {
                if (!TextUtils.isEmpty(itemData.urlAddress)) {
                    Intent intent = new Intent(v.getContext(), WebInfoActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, "活动位");
                    intent.putExtra(WebInfoActivity.WEB_URL, itemData.urlAddress);
                    v.getContext().startActivity(intent);
                }
            } else {
                ActionController actionFactory = ActionController.getInstance();
                itemData.pushScheme.source = "活动位";
                actionFactory.doAction(getContext(), itemData.pushScheme);
            }
        }
    }
}