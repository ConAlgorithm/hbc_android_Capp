package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.OrderDetailTravelAdapter;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/3/14.
 */
public class OrderDetailTravelGroup extends LinearLayout implements HbcViewBehavior, ViewPager.OnPageChangeListener{

    @BindView(R.id.order_detail_travel_tablayout)
    OrderDetailTravelTabLayout tabLayout;
    @BindView(R.id.order_detail_travel_pager)
    ViewPager travelPager;
    private int lastPosition = 0;
    private OrderBean orderBean;

    public OrderDetailTravelGroup(Context context) {
        this(context, null);
    }

    public OrderDetailTravelGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_order_detail_travel_group, this);
        ButterKnife.bind(view);

        final int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.order_bottom_padding_left);
        travelPager.setPageMargin(paddingLeft / 2);
        travelPager.setOffscreenPageLimit(3);
        travelPager.addOnPageChangeListener(this);
    }

    @Override
    public void update(Object _data) {
        orderBean = (OrderBean) _data;
        if (orderBean.orderType != 888 || orderBean.subOrderDetail.totalCount <= 1) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);
        final OrderDetailTravelAdapter listAdapter = new OrderDetailTravelAdapter(getContext(), orderBean);
        travelPager.setAdapter(listAdapter);
        tabLayout.setViewPager(travelPager, orderBean, 0);
        int pagerHeight = 0;

        if (orderBean.isSeparateOrder() && orderBean.isTwiceConfirm) {// 二次确认订单
            pagerHeight = UIUtils.dip2px(orderBean.isTwiceCancelShowSpan ? 255 : 130);
        } else if ((orderBean.orderStatus.code == 2 && orderBean.isSeparateOrder()) || orderBean.orderStatus.code > 2) {
            pagerHeight = UIUtils.dip2px(255);//125 + 130
        } else {
            pagerHeight = UIUtils.dip2px(130);
        }
        final int paddingLeft = getContext().getResources().getDimensionPixelOffset(R.dimen.order_bottom_padding_left);
        int itemWidth = UIUtils.getScreenWidth() - paddingLeft * 2;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(itemWidth, pagerHeight);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, R.id.order_detail_travel_tablayout);
        travelPager.setLayoutParams(params);
        travelPager.setClipChildren(false);
        if (lastPosition < travelPager.getChildCount()) {
            travelPager.setCurrentItem(lastPosition, false);
        }
    }

    public void onChangeSubOrder(String subOrderId) {
        if (travelPager == null || TextUtils.isEmpty(subOrderId) || orderBean.orderType != 888) {
            return;
        }
        int subOrderPosition = orderBean.getSubOrderPosition(subOrderId);
        if (subOrderPosition > 0) {
            travelPager.setCurrentItem(subOrderPosition, false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.lastPosition =  position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
