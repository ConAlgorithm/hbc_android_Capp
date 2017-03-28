package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.widget.OrderDetailChildView;

import java.util.List;

/**
 * Created by qingcha on 17/3/14.
 */
public class OrderDetailTravelAdapter extends PagerAdapter {

    private Context mContext;
    private List<OrderBean> items;
    public OrderDetailTravelAdapter(Context context, OrderBean orderBean) {
        this.mContext = context;
        this.items = orderBean.subOrderDetail.subOrderList;
    }

    public void setData(OrderBean orderBean) {
        this.items = orderBean.subOrderDetail.subOrderList;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        OrderDetailChildView itemView = new OrderDetailChildView(mContext);
        OrderBean orderBean = items.get(position);
        orderBean.orderIndex = position + 1;
        itemView.update(orderBean);
        container.addView(itemView, 0);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
