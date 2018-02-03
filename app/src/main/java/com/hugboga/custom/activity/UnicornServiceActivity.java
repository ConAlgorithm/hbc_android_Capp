package com.hugboga.custom.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.ServiceQuestionBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.request.RequestAvailableCustomerService;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.UnicornUtils;
import com.hugboga.custom.widget.UnicornDetailView;
import com.hugboga.custom.widget.UnicornOrderView;
import com.qiyukf.unicorn.api.ProductDetail;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by qingcha on 16/11/9.
 */
public class UnicornServiceActivity extends BaseActivity {

    @BindView(R.id.unicorn_service_order_state_layout)
    FrameLayout orderStateLayout;

    private Params params;

    private ArrayList<UnicornUtils.ServiceUserInfo> extraList = new ArrayList<>();
    private ProductDetail productDetail = null;

    public static class Params implements Serializable {
        public int sourceType;
        public OrderBean orderBean;
        public SkuItemBean skuItemBean;
        public ServiceQuestionBean.QuestionItem questionItem;
        public int groupId;
        public ProductDetail productDetail;
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
        if (params == null) {
            throw new IllegalArgumentException("UnicornServiceActivity params = null");
        }

        initDefaultTitleBar();
        fgTitle.setText(R.string.unicorn_service_title);

        switch (params.sourceType) {
            case SourceType.TYPE_CHARTERED:
            case SourceType.TYPE_LINE:
                if (params.skuItemBean == null) {
                    orderStateLayout.setVisibility(View.GONE);
                } else {
                    orderStateLayout.setVisibility(View.VISIBLE);
                    UnicornDetailView unicornDetailView = new UnicornDetailView(this, params.sourceType);
                    if (params.sourceType == SourceType.TYPE_LINE) {
                        unicornDetailView.update(params.skuItemBean);
                    }
                    orderStateLayout.addView(unicornDetailView);
                    productDetail = unicornDetailView.getProductDetail();
                    extraList.add(new UnicornUtils.ServiceUserInfo("goodsNo", "goodsNo", params.skuItemBean.goodsNo));
                    extraList.add(new UnicornUtils.ServiceUserInfo("goodsName", "goodsName",  params.skuItemBean.getGoodsName()));
                }
                break;
            case SourceType.TYPE_ORDER:
                if (params.orderBean == null) {
                    orderStateLayout.setVisibility(View.GONE);
                } else {
                    orderStateLayout.setVisibility(View.VISIBLE);
                    UnicornOrderView unicornOrderView = new UnicornOrderView(this);
                    unicornOrderView.update(params.orderBean);
                    orderStateLayout.addView(unicornOrderView);
                    productDetail = unicornOrderView.getProductDetail();
                }
                break;
            case SourceType.TYPE_AI_RESULT:
                if (params.productDetail != null) {
                    productDetail = params.productDetail;
                }
                break;
            default:
                orderStateLayout.setVisibility(View.GONE);
                productDetail = null;
                break;
        }
        int groupId = params.questionItem != null ? params.questionItem.customRole : 0;
        if (groupId == 0 && isPreSales(params.sourceType) && params.groupId != 0) {
            groupId = params.groupId;
        }
        if (groupId == 0) {
            requestData(new RequestAvailableCustomerService(this),true);
        } else {
            addServiceFragment(groupId);
        }
    }

    //是否是是售前客服
    private boolean isPreSales(int sourceType) {
        return sourceType == SourceType.TYPE_CHARTERED || sourceType == SourceType.TYPE_LINE || sourceType == SourceType.TYPE_AI_RESULT;
    }

    private void addServiceFragment(int _groupId) {
        int groupId = _groupId;
        if (groupId == 0 && isPreSales(params.sourceType)) {
            groupId = UnicornUtils.UNICORN_ERP_GROUPID;//默认售前ID
        }
        UnicornUtils.addServiceFragment(this, R.id.unicorn_service_container_layout, productDetail, groupId, extraList, getIntentSource());
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        ApiReportHelper.getInstance().addReport(request);
        if (request instanceof RequestAvailableCustomerService) {
            String groupId = ((RequestAvailableCustomerService)request).getData();
            addServiceFragment(CommonUtils.getCountInteger(groupId));
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        addServiceFragment(0);
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

    @Override
    public String getEventSource() {
        return "客服IM";
    }

    public static class SourceType {
        public static final int TYPE_DEFAULT = 0x000;   // 默认
        public static final int TYPE_CHARTERED = 0x001; // 自定义包车游
        public static final int TYPE_LINE = 0x002;      // 固定线路、推荐线路
        public static final int TYPE_ORDER = 0x003;     // 订单信息
        public static final int TYPE_CHAT_LIST = 0x004; // im_list
        public static final int TYPE_AI_RESULT = 0x005; // AI结果

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
                case TYPE_AI_RESULT:
                    result = 2;
                    break;
                case TYPE_ORDER:
                    result = 1;
                    break;
            }
            return result;
        }

        /*
        * 返回请求所需的type(API_SERVICE_QUESTION_LIST)
        * 场景id: 1订单售后，2商品售前，3默认通用
        * */
        public static String getRequsetTypeString(int _type) {
            String result = "";
            switch (_type) {
                case TYPE_DEFAULT:
                case TYPE_CHAT_LIST:
                    result = "通用";
                    break;
                case TYPE_LINE:
                case TYPE_CHARTERED:
                case TYPE_AI_RESULT:
                    result = "售前";
                    break;
                case TYPE_ORDER:
                    result = "售后";
                    break;
            }
            return result;
        }
    }

}
