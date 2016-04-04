package com.hugboga.custom.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.BaseAdapter;
import com.huangbaoche.hbcframe.data.net.DefaultImageCallback;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.widget.STGVImageView;

import org.w3c.dom.Text;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Created by admin on 2016/3/3.
 */
public class SkuAdapter extends BaseAdapter<SkuItemBean> {

    private final ImageOptions options;

    public SkuAdapter(Context context) {
        super(context);
        options = new ImageOptions.Builder()
                .setFailureDrawableId(R.mipmap.img_undertext)
                .setLoadingDrawableId(R.mipmap.img_undertext)
                .build();
    }
    ViewHolder viewHolder = null;
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_city_sku, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.item_sku_title);
            viewHolder.tvLabel = (TextView) view.findViewById(R.id.item_sku_label);
            viewHolder.tvGuide = (TextView) view.findViewById(R.id.item_sku_guide_number);
            viewHolder.tvAmount = (TextView) view.findViewById(R.id.item_sku_amount);
            viewHolder.tvSale = (TextView) view.findViewById(R.id.item_sku_sale);
            viewHolder.imgBg = (STGVImageView) view.findViewById(R.id.item_city_sku_img);
            viewHolder.top_line = (TextView) view.findViewById(R.id.top_line);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SkuItemBean bean = getItem(position);
        if(position == 0){
            viewHolder.top_line.setVisibility(View.GONE);
        }else{
            viewHolder.top_line.setVisibility(View.VISIBLE);
        }
        if (bean != null) {
            viewHolder.tvTitle.setText(bean.goodsName);
            viewHolder.tvLabel.setText(bean.salePoints);
            viewHolder.tvGuide.setText(mContext.getString(R.string.sku_item_guide_number, bean.guideAmount));
            viewHolder.tvAmount.setText(bean.goodsMinPrice);
            viewHolder.tvSale.setText(mContext.getString(R.string.sku_sale_number, bean.saleAmount));
            viewHolder.tvGuide.setVisibility(bean.guideAmount == 0 ? View.INVISIBLE : View.VISIBLE);
            viewHolder.tvSale.setVisibility(bean.saleAmount == 0 ? View.INVISIBLE : View.VISIBLE);

            viewHolder.imgBg.mHeight = 400;
            viewHolder.imgBg.mWidth = 750;
            x.image().bind(viewHolder.imgBg, bean.goodsPicture, options);
        }

        return view;
    }

    final static class ViewHolder {
        STGVImageView imgBg;
        TextView tvTitle;
        TextView tvLabel;
        TextView tvGuide;
        TextView tvAmount;
        TextView tvSale;
        TextView top_line;

    }

}
