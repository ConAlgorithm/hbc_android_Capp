package com.hugboga.custom.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;

import org.xutils.view.annotation.ViewInject;

/**
 * 订单列表VH
 * Created by ZHZEPHI on 2015/11/7 11:44.
 */
public class HistoryOrderVH extends ZBaseViewHolder {

    @ViewInject(R.id.travel_item_typestr)
    public TextView mOrderType;

    public HistoryOrderVH(View itemView) {
        super(itemView);
    }
}
