package com.hugboga.custom.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CouponBean;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by ZHZEPHI on 2015/7/24.
 */
public class CouponAdapter extends BaseAdapter<CouponBean> {

    public CouponAdapter(Activity context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fg_coupon_item, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CouponBean couponBean = getItem(position);
        if (!TextUtils.isEmpty(couponBean.price) && couponBean.price.contains("元")) {
            couponBean.price = couponBean.price.substring(0, couponBean.price.indexOf("元"));
        }
        holder.mPrice.setText(couponBean.price);
        holder.mContent.setText(couponBean.batchName);
        if (couponBean.endDate.equals("0")) {
            holder.mDateBettow.setText("长期有效");
        } else {
            holder.mDateBettow.setText("有效期：" + couponBean.startDate + " 至 " + couponBean.endDate);
        }
        //根据状态显示不同背景
        if (couponBean.couponStatus.equals(1) || couponBean.couponStatus.equals(98)) {
            //可用
            holder.mLayout.setBackgroundResource(R.mipmap.coupon_available);
            final int normalColor = mContext.getResources().getColor(R.color.coupon_label_normal);
            holder.mLabel.setTextColor(normalColor);
            holder.mPrice.setTextColor(normalColor);
            holder.lineView.setBackgroundColor(normalColor);
            holder.yuanTV.setTextColor(normalColor);
        } else if (couponBean.couponStatus.equals(2)) {
            //已使用
            holder.mLayout.setBackgroundResource(R.mipmap.coupon_unavailable);
            final int pressColor = mContext.getResources().getColor(R.color.coupon_label_press);
            holder.mLabel.setTextColor(pressColor);
            holder.mPrice.setTextColor(pressColor);
            holder.lineView.setBackgroundColor(pressColor);
            holder.yuanTV.setTextColor(pressColor);
        } else if (couponBean.couponStatus.equals(-1)) {
            //过期
            holder.mLayout.setBackgroundResource(R.mipmap.coupon_overdue);
            final int pressColor = mContext.getResources().getColor(R.color.coupon_label_press);
            holder.mLabel.setTextColor(pressColor);
            holder.mPrice.setTextColor(pressColor);
            holder.lineView.setBackgroundColor(pressColor);
            holder.yuanTV.setTextColor(pressColor);
        }
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.coupon_item_bg)
        View mLayout;
        @ViewInject(R.id.coupon_item_type)
        TextView mLabel;
        @ViewInject(R.id.coupon_item_price)
        TextView mPrice;
        @ViewInject(R.id.coupon_item_content)
        TextView mContent;
        @ViewInject(R.id.coupon_item_date_between)
        TextView mDateBettow;
        @ViewInject(R.id.coupon_vertical_line)
        View lineView;
        @ViewInject(R.id.coupon_item_yuan_tv)
        TextView yuanTV;
    }
}
