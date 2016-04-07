package com.hugboga.custom.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.viewholder.ZBaseViewHolder;
import com.hugboga.custom.R;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by admin on 2016/4/6.
 */
public class SkuVH extends ZBaseViewHolder {

    @ViewInject(R.id.item_city_sku_img)
    public ImageView imgBg;
    @ViewInject(R.id.item_sku_title)
    public TextView tvTitle;
    @ViewInject(R.id.item_sku_label)
    public TextView tvLabel;
    @ViewInject(R.id.item_sku_guide_number)
    public TextView tvGuide;
    @ViewInject(R.id.item_sku_amount)
    public TextView tvAmount;
    @ViewInject(R.id.item_sku_sale)
    public TextView tvSale;
    @ViewInject(R.id.top_line)
    public TextView top_line;


    public SkuVH(View itemView) {
        super(itemView);
    }

}
