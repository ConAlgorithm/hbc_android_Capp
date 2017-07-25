package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.ServiceQuestionBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.UnicornUtils;
import com.hugboga.custom.widget.UnicornDetailView;
import com.hugboga.custom.widget.UnicornOrderView;
import com.qiyukf.unicorn.api.ProductDetail;

import java.io.Serializable;

import butterknife.Bind;

/**
 * Created by qingcha on 16/11/9.
 */
public class UnicornServiceActivity extends BaseActivity{

    @Bind(R.id.unicorn_service_order_state_layout)
    FrameLayout orderStateLayout;

    private Params params;

    public static class Params implements Serializable {
        public int sourceType;
        public OrderBean orderBean;
        public SkuItemBean skuItemBean;
        public ServiceQuestionBean.QuestionItem questionItem;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_unicorn_service;
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

        initDefaultTitleBar();
        fgTitle.setText("皇包车客服");

        ProductDetail productDetail = null;
        switch (params.sourceType) {
            case SourceType.TYPE_CHARTERED:
            case SourceType.TYPE_LINE:
                orderStateLayout.setVisibility(View.VISIBLE);
                UnicornDetailView unicornDetailView = new UnicornDetailView(this, params.sourceType);
                if (params.sourceType == SourceType.TYPE_LINE) {
                    unicornDetailView.update(params.skuItemBean);
                }
                orderStateLayout.addView(unicornDetailView);
                productDetail = unicornDetailView.getProductDetail();
                break;
            case SourceType.TYPE_ORDER:
                orderStateLayout.setVisibility(View.VISIBLE);
                UnicornOrderView unicornOrderView = new UnicornOrderView(this);
                unicornOrderView.update(params.orderBean);
                orderStateLayout.addView(unicornOrderView);
                productDetail = unicornOrderView.getProductDetail();
                break;
            default:
                orderStateLayout.setVisibility(View.GONE);
                productDetail = null;
                break;
        }

        UnicornUtils.addServiceFragment(this, R.id.unicorn_service_container_layout, productDetail, params.questionItem != null ? params.questionItem.customRole : 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftInput();
    }

    public static class SourceType {
        public static final int TYPE_DEFAULT = 0x000;   // 默认
        public static final int TYPE_CHARTERED = 0x001; // 自定义包车游
        public static final int TYPE_LINE = 0x002;      // 固定线路、推荐线路
        public static final int TYPE_ORDER = 0x003;     // 订单信息
        public static final int TYPE_CHAT_LIST = 0x004; // im_list

        /*
        * 返回请求所需的type(API_SERVICE_QUESTION_LIST)
        * 场景id: 1订单售后，2商品售前，3默认通用
        * */
        public static int getRequsetType(int _type) {
            int result = 3;
            switch (_type) {
                case TYPE_DEFAULT:
                case TYPE_CHAT_LIST:
                    result = 3;
                    break;
                case TYPE_LINE:
                case TYPE_CHARTERED:
                    result = 2;
                    break;
                case TYPE_ORDER:
                    result = 1;
                    break;
            }
            return result;
        }
    }
}
