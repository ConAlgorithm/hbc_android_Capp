package com.hugboga.custom.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.UnicornUtils;
import com.hugboga.custom.widget.UnicornDetailView;
import com.hugboga.custom.widget.UnicornOrderView;
import com.qiyukf.unicorn.api.ProductDetail;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by on 16/11/9.
 */
public class UnicornServiceActivity extends BaseActivity{

    public static final int TYPE_CHARTERED = 0x001; // 自定义包车游
    public static final int TYPE_LINE = 0x002;      // 固定线路、推荐线路
    public static final int TYPE_ORDER = 0x003;     // 订单信息

    @Bind(R.id.unicorn_service_order_state_layout)
    FrameLayout orderStateLayout;

    private Params params;

    public static class Params implements Serializable {
        public int sourceType;
        public OrderBean orderBean;
        public SkuItemBean skuItemBean;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (UnicornServiceActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (UnicornServiceActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        setContentView(R.layout.activity_unicorn_service);
        ButterKnife.bind(this);

        initDefaultTitleBar();
        fgTitle.setText("皇包车客服");

        ProductDetail productDetail = null;
        switch (params.sourceType) {
            case TYPE_CHARTERED:
            case TYPE_LINE:
                UnicornDetailView unicornDetailView = new UnicornDetailView(this);
                unicornDetailView.setSourceType(params.sourceType);
                if (params.sourceType == TYPE_LINE) {
                    unicornDetailView.update(params.skuItemBean);
                }
                orderStateLayout.addView(unicornDetailView);
                productDetail = unicornDetailView.getProductDetail();
                break;
            case TYPE_ORDER:
                UnicornOrderView unicornOrderView = new UnicornOrderView(this);
                unicornOrderView.update(params.orderBean);
                orderStateLayout.addView(unicornOrderView);
                productDetail = unicornOrderView.getProductDetail();
                break;
        }

        UnicornUtils.openServiceActivity(this, R.id.unicorn_service_container_layout, productDetail);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }
}
