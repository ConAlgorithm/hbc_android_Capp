package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.View;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.viewholder.HistoryOrderVH;
import com.hugboga.custom.data.bean.OrderBean;

/**
 * 聊天历史订单
 * Created by ZHZEPHI on 2015/11/7 11:47.
 */
public class HistoryOrderAdapter extends ZBaseAdapter<OrderBean, HistoryOrderVH> {

    public HistoryOrderAdapter(Context context) {
        super(context);
    }

    @Override
    protected int initResource() {
        return R.layout.order_history_item;
    }

    @Override
    protected ZBaseViewHolder getVH(View view) {
        return new HistoryOrderVH(view);
    }

    @Override
    protected void getView(int position, HistoryOrderVH vh) {
        OrderBean orderBean = datas.get(position);
    }
}
