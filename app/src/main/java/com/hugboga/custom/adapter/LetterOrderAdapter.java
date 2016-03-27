package com.hugboga.custom.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ChatOrderBean;

import org.xutils.view.annotation.ViewInject;

/**
 * 私信订单显示
 * Created by ZHZEPHI on 2016/3/25.
 */
public class LetterOrderAdapter extends BaseAdapter<ChatOrderBean> {

    public LetterOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LetterOrderVH viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.letter_order_item, null);
            viewHolder = new LetterOrderVH(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LetterOrderVH) convertView.getTag();
        }
        ChatOrderBean chatOrderBean = getItem(position);
        if (chatOrderBean != null) {
            viewHolder.mOrderState.setText(chatOrderBean.status);
            resetStatusColor(viewHolder.mOrderState, chatOrderBean.status);
            viewHolder.mOrderType.setText(chatOrderBean.orderTypeStr);
            viewHolder.mOrderTime.setText(chatOrderBean.serviceTime);
        }
        return convertView;
    }

    /**
     * 根据私信订单状态改变颜色
     *
     * @param tv
     * @param status
     */
    private void resetStatusColor(TextView tv, String status) {
        if (!TextUtils.isEmpty(status)) {
            if (mContext.getString(R.string.letter_order_state1).equals(status)) {
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.letter_item_order1));
            } else if (mContext.getString(R.string.letter_order_state2).equals(status)) {
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.letter_item_order2));
            } else if (mContext.getString(R.string.letter_order_state3).equals(status)) {
                tv.setTextColor(ContextCompat.getColor(mContext, R.color.letter_item_order3));
            }
        }
    }

    public class LetterOrderVH extends BaseViewHolder {

        @ViewInject(R.id.letter_item_order_state)
        TextView mOrderState;
        @ViewInject(R.id.letter_item_order_content)
        TextView mOrderType;
        @ViewInject(R.id.letter_item_order_time)
        TextView mOrderTime;

        public LetterOrderVH(View itemView) {
            super(itemView);
        }
    }
}
