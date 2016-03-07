package com.hugboga.custom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.SkuItemBean;

/**
 * Created by admin on 2016/3/3.
 */
public class SkuAdapter extends BaseAdapter<SkuItemBean> {

    public SkuAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_city_sku, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.item_sku_title);
            viewHolder.tvLabel = (TextView) view.findViewById(R.id.item_sku_label);
            viewHolder.tvGuide = (TextView) view.findViewById(R.id.item_sku_guide_number);
            viewHolder.tvAmount = (TextView) view.findViewById(R.id.item_sku_amount);
            viewHolder.tvSale  = (TextView) view.findViewById(R.id.item_sku_sale);
            viewHolder.imgBg = (ImageView) view.findViewById(R.id.item_home_img);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SkuItemBean bean =  getItem(position);
        if(bean!=null){
            viewHolder.tvTitle.setText(bean.goodsName);
            viewHolder.tvLabel.setText(bean.salePoints);
            viewHolder.tvGuide.setText(bean.guideAmount);
            viewHolder.tvAmount.setText(bean.goodsMinPrice);
            viewHolder.tvSale.setText(bean.saleAmount);
            //x.image().bind(viewHolder.imgBg,bean.picture);
        }

        return view;
    }

    final static class ViewHolder {
        ImageView imgBg;
        TextView tvTitle;
        TextView tvLabel;
        TextView tvGuide;
        TextView tvAmount;
        TextView tvSale;

    }

}
