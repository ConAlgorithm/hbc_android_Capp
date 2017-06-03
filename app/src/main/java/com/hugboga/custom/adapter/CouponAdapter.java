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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ZHZEPHI on 2015/7/24.
 */
public class CouponAdapter extends BaseAdapter<CouponBean> {

    public String idStr; //默认选中的优惠券

    public CouponAdapter(Activity context,String idStr) {
        super(context);
        this.idStr = idStr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fg_coupon_item_new, null);
            holder = new ViewHolder();
            ButterKnife.bind(holder,convertView);
            //x.view().inject(holder, convertView);
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
            holder.mDateBettow.setText("有效期：" + couponBean.startDate + " 至 " + couponBean.endDate);
        }
        //根据状态显示不同背景
        if (couponBean.couponStatus.equals(1)) {
            //可用
            //holder.mLayout.setBackgroundResource(R.mipmap.coupon_available);
            final int batchNameColor = mContext.getResources().getColor(R.color.common_font_color_black);
            final int priceColor = mContext.getResources().getColor(R.color.all_bg_yellow);
            final int contentColor = mContext.getResources().getColor(R.color.common_font_color_gray1);
            //holder.mLabel.setTextColor(normalColor);
            holder.mPrice.setTextColor(priceColor);
            //holder.lineView.setBackgroundColor(normalColor);
            holder.couponBatchName.setTextColor(batchNameColor);
            holder.mContent.setTextColor(contentColor);
            holder.couponInvalid.setVisibility(View.GONE);
        } else if (couponBean.couponStatus.equals(2)) {
            //已使用
            final int batchNameColor = mContext.getResources().getColor(R.color.common_font_color_gray2);
            final int priceColor = mContext.getResources().getColor(R.color.common_font_color_gray2);
            final int contentColor = mContext.getResources().getColor(R.color.common_font_color_gray2);
            //holder.mLayout.setBackgroundResource(R.mipmap.coupon_unavailable);
            //final int pressColor = mContext.getResources().getColor(R.color.coupon_label_press);
            //holder.mLabel.setTextColor(pressColor);
            holder.mPrice.setTextColor(priceColor);
            //holder.lineView.setBackgroundColor(pressColor);
            holder.couponBatchName.setTextColor(batchNameColor);
            holder.mContent.setTextColor(contentColor);
            holder.couponInvalid.setVisibility(View.VISIBLE);
            holder.couponInvalid.setText("已使用");
        } else if (couponBean.couponStatus.equals(-1)) {
            //过期
            final int batchNameColor = mContext.getResources().getColor(R.color.common_font_color_gray2);
            final int priceColor = mContext.getResources().getColor(R.color.common_font_color_gray2);
            final int contentColor = mContext.getResources().getColor(R.color.common_font_color_gray2);
            //holder.mLayout.setBackgroundResource(R.mipmap.coupon_overdue);
            //final int pressColor = mContext.getResources().getColor(R.color.coupon_label_press);
            //holder.mLabel.setTextColor(pressColor);
            holder.mPrice.setTextColor(priceColor);
            //holder.lineView.setBackgroundColor(pressColor);
            holder.couponBatchName.setTextColor(batchNameColor);
            holder.mContent.setTextColor(contentColor);
            holder.couponInvalid.setVisibility(View.VISIBLE);
            holder.couponInvalid.setText("已过期");
        } else if(couponBean.couponStatus.equals(98)){
            //冻结
            final int batchNameColor = mContext.getResources().getColor(R.color.common_font_color_gray2);
            final int priceColor = mContext.getResources().getColor(R.color.common_font_color_gray2);
            final int contentColor = mContext.getResources().getColor(R.color.common_font_color_gray2);
            //holder.mLayout.setBackgroundResource(R.mipmap.coupon_overdue);
            //final int pressColor = mContext.getResources().getColor(R.color.coupon_label_press);
            //holder.mLabel.setTextColor(pressColor);
            holder.mPrice.setTextColor(priceColor);
            //holder.lineView.setBackgroundColor(pressColor);
            holder.couponBatchName.setTextColor(batchNameColor);
            holder.mContent.setTextColor(contentColor);
            holder.couponInvalid.setVisibility(View.VISIBLE);
            holder.couponInvalid.setText("已冻结");
        }
        if (!TextUtils.isEmpty(idStr) && couponBean.couponID.equals(idStr)) {
            holder.mSelected.setVisibility(View.VISIBLE);
        } else {
            holder.mSelected.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        /*@ViewInject(R.id.coupon_item_bg)
        View mLayout;
        @ViewInject(R.id.coupon_item_type)
        TextView mLabel;*/
        @Bind(R.id.coupon_item_price)
        TextView mPrice;
        @Bind(R.id.coupon_item_content)
        TextView mContent;
        @Bind(R.id.coupon_item_date_between)
        TextView mDateBettow;
        /*@ViewInject(R.id.coupon_vertical_line)
        View lineView;*/
        @Bind(R.id.coupon_item_selected)
        ImageView mSelected;
        @Bind(R.id.coupon_batch_name)
        TextView couponBatchName;
        @Bind(R.id.coupon_invalid)
        TextView couponInvalid;
    }
}
