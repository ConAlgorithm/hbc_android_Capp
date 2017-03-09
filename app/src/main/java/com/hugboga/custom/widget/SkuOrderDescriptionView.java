package com.hugboga.custom.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.Tools;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/12/16.
 */
public class SkuOrderDescriptionView extends LinearLayout implements HbcViewBehavior{

    @Bind(R.id.sku_order_itinerary_iv)
    ImageView itineraryIV;
    @Bind(R.id.sku_order_itinerary_tag_iv)
    ImageView tagIV;
    @Bind(R.id.sku_order_itinerary_title_tv)
    TextView titleTV;

    public SkuOrderDescriptionView(Context context) {
        this(context, null);
    }

    public SkuOrderDescriptionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_sku_order_description, this);
        ButterKnife.bind(view);
    }

    @Override
    public void update(Object _data) {
        if (!(_data instanceof SkuItemBean)) {
            return;
        }
        SkuItemBean skuItemBean = (SkuItemBean) _data;

        Tools.showImage(itineraryIV, skuItemBean.goodsPicture);

        titleTV.setText(skuItemBean.goodsName);

        if (skuItemBean.goodsClass == 1) {//固定线路 超省心
            tagIV.setBackgroundResource(R.mipmap.order_sku_tag_green);
        } else {//推荐路线 超自由
            tagIV.setBackgroundResource(R.mipmap.order_sku_tag_blue);
        }
    }
}
