package com.hugboga.custom.adapter;

import android.app.Activity;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
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

import java.util.zip.Inflater;

/**
 * Created by ZHZEPHI on 2015/7/24.
 */
public class CouponAdapter extends BaseAdapter<CouponBean> {

    public String idStr; //默认选中的优惠券
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
        holder.mPrice.setText(couponBean.price);
        holder.mContent.setText(couponBean.batchName);
            if (couponBean.endDate.equals("0")) {
                holder.mDateBettow.setText("长期有效");
            } else {
//                holder.mDateBettow.setText("有效期：" + DateUtils.getPointStrFromDate1(couponBean.startDate) + " 至 " + DateUtils.getPointStrFromDate1(couponBean.endDate));
                holder.mDateBettow.setText("有效期：" + couponBean.startDate + " 至 " + couponBean.endDate);
            }
        //根据状态显示不同背景
        if (couponBean.couponStatus.equals(1) || couponBean.couponStatus.equals(98)) {
            //可用
            holder.mLayout.setBackgroundResource(R.mipmap.coupon_yes);
            holder.mLabel.setTextColor(mContext.getResources().getColor(R.color.coupon_label_normal));
            holder.mPrice.setTextColor(mContext.getResources().getColor(R.color.coupon_label_normal));
            holder.mContent.setTextColor(mContext.getResources().getColor(R.color.coupon_content_normal));
            holder.mDateBettow.setTextColor(mContext.getResources().getColor(R.color.coupon_content_normal));
        } else if (couponBean.couponStatus.equals(2)) {
            //已使用
            holder.mLayout.setBackgroundResource(R.mipmap.coupon_used);
            holder.mLabel.setTextColor(mContext.getResources().getColor(R.color.coupon_label_press));
            holder.mPrice.setTextColor(mContext.getResources().getColor(R.color.coupon_label_press));
            holder.mContent.setTextColor(mContext.getResources().getColor(R.color.coupon_content_press));
            holder.mDateBettow.setTextColor(mContext.getResources().getColor(R.color.coupon_content_press));
        } else if (couponBean.couponStatus.equals(-1)) {
            //过期
            holder.mLayout.setBackgroundResource(R.mipmap.coupon_expired);
            holder.mLabel.setTextColor(mContext.getResources().getColor(R.color.coupon_label_press));
            holder.mPrice.setTextColor(mContext.getResources().getColor(R.color.coupon_label_press));
            holder.mContent.setTextColor(mContext.getResources().getColor(R.color.coupon_content_press));
            holder.mDateBettow.setTextColor(mContext.getResources().getColor(R.color.coupon_content_press));
        }
        if (idStr != null && !idStr.isEmpty() && couponBean.couponID.equals(idStr)) {
            holder.mSelected.setVisibility(View.VISIBLE);
        } else {
            holder.mSelected.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        @ViewInject(R.id.coupon_item_layout)
        RelativeLayout mLayout;
        @ViewInject(R.id.coupon_item_title)
        TextView mLabel;
        @ViewInject(R.id.coupon_item_title)
        TextView mTitle;
        @ViewInject(R.id.coupon_item_price)
        TextView mPrice;
        @ViewInject(R.id.coupon_item_content)
        TextView mContent;
        @ViewInject(R.id.coupon_item_date_between)
        TextView mDateBettow;
        @ViewInject(R.id.coupon_item_selected)
        ImageView mSelected;
    }
}
