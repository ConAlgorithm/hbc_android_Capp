package com.hugboga.custom.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.net.UrlLibs;
import com.qiyukf.unicorn.api.ProductDetail;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/11/11.
 */
public class UnicornDetailView extends LinearLayout implements HbcViewBehavior{
    @Bind(R.id.unicorn_detail_title_tv)
    TextView titleTV;
    @Bind(R.id.unicorn_detail_sign_tv)
    TextView signTV;
    @Bind(R.id.unicorn_detail_price_tv)
    TextView priceTV;
    @Bind(R.id.unicorn_detail_unit_tv)
    TextView unitTV;

    private ProductDetail productDetail;

    public UnicornDetailView(Context context) {
        this(context, null);
    }

    public UnicornDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_unicorn_detail, this);
        ButterKnife.bind(view);
    }

    public void setSourceType(int type) {

        if (type == UnicornServiceActivity.TYPE_CHARTERED) {
            signTV.setVisibility(View.GONE);
            unitTV.setVisibility(View.GONE);
            String title = "按天自由定制包车游，专属中文司机兼导游服务";
            titleTV.setText(title);
            priceTV.setText("选择行程后可查看报价");
            priceTV.setTextSize(14);

            String userId = UserEntity.getUser().getUserId(getContext());
            String params = "";
            if (!TextUtils.isEmpty(userId)) {
                params += "?userId=" + userId;
            }
            ProductDetail.Builder builder = new ProductDetail.Builder();
            builder.setTitle("定制包车游");
            builder.setDesc(title);
            builder.setUrl(UrlLibs.H5_DAIRY + params);
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
        titleTV.setText(skuItemBean.goodsName);
        priceTV.setText(skuItemBean.perPrice);

        ProductDetail.Builder builder = new ProductDetail.Builder();
        builder.setTitle("线路包车游");
        builder.setDesc(skuItemBean.goodsName);
        builder.setUrl(skuItemBean.skuDetailUrl);
        builder.setShow(1);
        productDetail = builder.create();
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }
}
