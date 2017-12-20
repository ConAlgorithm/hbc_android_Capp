package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.qiyukf.unicorn.api.ProductDetail;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/11/11.
 */
public class UnicornDetailView extends LinearLayout implements HbcViewBehavior{
    @BindView(R.id.unicorn_detail_title_tv)
    TextView titleTV;
    @BindView(R.id.unicorn_detail_sign_tv)
    TextView signTV;
    @BindView(R.id.unicorn_detail_price_tv)
    TextView priceTV;
    @BindView(R.id.unicorn_detail_unit_tv)
    TextView unitTV;

    private ProductDetail productDetail;

    public UnicornDetailView(Context context, int type) {
        super(context);
        View view = inflate(context, R.layout.view_unicorn_detail, this);
        ButterKnife.bind(view);
        setSourceType(type);
    }

    public void setSourceType(int type) {
        if (type == UnicornServiceActivity.SourceType.TYPE_CHARTERED) {
            signTV.setVisibility(View.GONE);
            unitTV.setVisibility(View.GONE);
            String title = "按天自由定制包车游，专属中文司机兼导游服务";
            titleTV.setText(title);
            priceTV.setText("选择行程后可查看报价");
            priceTV.setTextSize(14);

            ProductDetail.Builder builder = new ProductDetail.Builder();
            builder.setTitle("按天包车游");
            builder.setPicture("http://fr.huangbaoche.com/default/dingzhi.png");
            builder.setDesc(title);
            builder.setShow(1);
            productDetail = builder.create();
        } else {
            signTV.setVisibility(View.VISIBLE);
            unitTV.setVisibility(View.VISIBLE);
            priceTV.setTextSize(16);
        }
    }

    @Override
    public void update(Object _data) {
        SkuItemBean skuItemBean = (SkuItemBean) _data;
        if (skuItemBean == null) {
            return;
        }
        titleTV.setText(skuItemBean.getGoodsName());
        priceTV.setText(skuItemBean.perPrice);
        String otherStr = String.format("起/人 · %1$s日", skuItemBean.daysCount);
        if (skuItemBean.hotelStatus == 1) {// 是否含酒店
            otherStr += " · 含酒店";
        }
        unitTV.setText(otherStr);

        ProductDetail.Builder builder = new ProductDetail.Builder();
        builder.setTitle("线路包车游");
        builder.setDesc(skuItemBean.getGoodsName());
        builder.setPicture(skuItemBean.goodsPicture);
        builder.setShow(1);
        builder.setAlwaysSend(true);

        if (!TextUtils.isEmpty(skuItemBean.perPrice)) {
            builder.setNote(getContext().getResources().getString(R.string.sign_rmb) + skuItemBean.perPrice + otherStr);
        }

        productDetail = builder.create();
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }
}
