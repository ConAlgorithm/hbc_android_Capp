package com.hugboga.custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderPriceInfoBean;
import com.hugboga.custom.data.request.RequestBatchPriceInfo;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.CircularProgress;
import com.hugboga.custom.widget.OrderPriceInfoItemView;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/8/25.
 */
public class OrderPriceInfoActivity extends Activity implements HttpRequestListener {

    @BindView(R.id.activity_order_price_info_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_order_price_info_limit_layout)
    LinearLayout limitLayout;
    @BindView(R.id.activity_order_price_info_layout)
    LinearLayout priceInfoLayout;
    @BindView(R.id.activity_order_price_info_progress)
    CircularProgress progressView;

    private ErrorHandler errorHandler;
    private HbcRecyclerSingleTypeAdpater<OrderPriceInfoBean> mAdapter;

    private Params params;

    public static class Params implements Serializable {
        public String batchNo;
        public long carId;
        public int pickUpFlag;
        public int checkInFlag;
        public int childSeatFlag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            params = (Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                params = (Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.activity_order_price_info);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (params != null) {
            outState.putSerializable(Constants.PARAMS_DATA, params);
        }
    }

    public void init() {
        limitLayout.getLayoutParams().height = UIUtils.getScreenHeight() / 6 * 4;

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, OrderPriceInfoItemView.class);
        mRecyclerView.setAdapter(mAdapter);

        requestBatchPriceInfo();
    }

    public void requestBatchPriceInfo() {
        RequestBatchPriceInfo request = new RequestBatchPriceInfo(this, params.batchNo, params.carId, params.pickUpFlag, params.checkInFlag, params.childSeatFlag);
        HttpRequestUtils.request(this, request, this, false);
        progressView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.activity_order_price_info_root_layout)
    public void onClose() {
        finish();
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        ApiReportHelper.getInstance().addReport(_request);
        if (_request instanceof RequestBatchPriceInfo) {
            ArrayList<OrderPriceInfoBean> dataList = ((RequestBatchPriceInfo) _request).getData();
            mAdapter.addData(dataList);
            priceInfoLayout.setVisibility(View.VISIBLE);
            progressView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (errorHandler == null) {
            errorHandler = new ErrorHandler(this, this);
        }
        errorHandler.onDataRequestError(errorInfo, request);
        DefaultSSLSocketFactory.resetSSLSocketFactory(this);
        if (isFinishing()) {
            finish();
        }
    }
}
