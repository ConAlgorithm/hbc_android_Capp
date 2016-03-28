package com.hugboga.custom.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.OrderCostApplyInfo;

/**
 * Created by admin on 2015/7/31.
 */
public class OverPriceAdapter extends BaseAdapter<OrderCostApplyInfo> {


    public OverPriceAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_over_price, null);
            viewHolder.overTitle = (TextView) view.findViewById(R.id.over_title);
            viewHolder.overItemTitle = (TextView) view.findViewById(R.id.over_item_title);
            viewHolder.overItemValue = (TextView) view.findViewById(R.id.over_item_value);
            viewHolder.overOtherValue = (TextView) view.findViewById(R.id.over_other_value);
            viewHolder.overOtherLayout = (View) view.findViewById(R.id.item_over_other_layout);
            viewHolder.overItemLayout = (View) view.findViewById(R.id.over_item_layout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        OrderCostApplyInfo model = getItem(position);
        if (!TextUtils.isEmpty(model.prePaymentPrice)) {//垫付费用
            viewHolder.overOtherLayout.setVisibility(View.VISIBLE);
            viewHolder.overOtherValue.setText(model.prePaymentPrice + "");
        }
        if (!TextUtils.isEmpty(model.dailyDate)) {
            viewHolder.overTitle.setVisibility(View.VISIBLE);
            viewHolder.overTitle.setText(model.dailyDate);
        } else if (!TextUtils.isEmpty(model.overDay)) {
            viewHolder.overTitle.setVisibility(View.VISIBLE);
            viewHolder.overItemLayout.setVisibility(View.VISIBLE);
            viewHolder.overTitle.setText("超出天数");
            viewHolder.overItemTitle.setText("超出天数费用");
            viewHolder.overItemValue.setText(model.overDayPrice + "(" + model.overDay + ")");
        }

        if (!TextUtils.isEmpty(model.overTime)) {
            viewHolder.overItemLayout.setVisibility(View.VISIBLE);
            viewHolder.overItemTitle.setText("超时费用");
            viewHolder.overItemValue.setText(model.overTimePrice + "(" + model.overTime + ")");
        } else if (!TextUtils.isEmpty(model.overDistance)) {
            viewHolder.overItemLayout.setVisibility(View.VISIBLE);
            viewHolder.overItemTitle.setText("超里程费");
            viewHolder.overItemValue.setText(model.overDistancePrice + "(" + model.overDistance + ")");
        }
        return view;
    }

    class ViewHolder {
        TextView overTitle;
        TextView overItemTitle;
        TextView overItemValue;
        TextView overOtherValue;
        View overOtherLayout;
        View overItemLayout;
    }
}
