package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.RecommendedGoodsBean;
import com.hugboga.custom.data.request.RequestRecommendedGoods;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.UIUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/8/30.
 */
public class PayResultRecommendLayout extends LinearLayout implements HttpRequestListener {

    @Bind(R.id.view_pay_result_recommend_container_layout)
    LinearLayout containerLayout;
    @Bind(R.id.view_pay_result_footer_layout)
    LinearLayout footerLayout;
    @Bind(R.id.view_pay_result_footer_tv)
    TextView footerTV;

    private ErrorHandler errorHandler;
    private String cityName;
    private String cityId;
    private int orderType;

    public PayResultRecommendLayout(Context context) {
        this(context, null);
    }

    public PayResultRecommendLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.view_pay_result_recommend, this);
        ButterKnife.bind(view);
    }

    @OnClick(R.id.view_pay_result_footer_layout)
    public void intentCityList() {
        if (TextUtils.isEmpty(cityName) || TextUtils.isEmpty(cityId)) {
            return;
        }
        CityListActivity.Params params = new CityListActivity.Params();
        params.id = CommonUtils.getCountInteger(cityId);
        params.cityHomeType = CityListActivity.CityHomeType.CITY;
        params.titleName = cityName;
        Intent intent = new Intent(getContext(), CityListActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_title));
        intent.putExtra(Constants.PARAMS_DATA, params);
        getContext().startActivity(intent);
    }

    public void setRequestParams(String cityName, String cityId, String goodsNo, int orderType) {
        this.cityName = cityName;
        this.cityId = cityId;
        this.orderType = orderType;
        RequestRecommendedGoods request = new RequestRecommendedGoods(getContext(), cityId, goodsNo);
        HttpRequestUtils.request(getContext(), request, this, true);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        ApiReportHelper.getInstance().addReport(_request);
        if (_request instanceof RequestRecommendedGoods) {
            RecommendedGoodsBean recommendedGoodsBean = ((RequestRecommendedGoods) _request).getData();
            addRecommendedItemView(recommendedGoodsBean);
            setVisibility(View.VISIBLE);
            if (recommendedGoodsBean.listCount > 9) {
                footerTV.setText(String.format("查看更多%1$s线路和司导", cityName));
                footerLayout.setVisibility(View.VISIBLE);
            } else {
                footerLayout.setVisibility(View.INVISIBLE);
                footerLayout.setPadding(0, UIUtils.dip2px(10), 0, 0);
            }
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (errorHandler == null) {
            errorHandler = new ErrorHandler((Activity)getContext(), this);
        }
        errorHandler.onDataRequestError(errorInfo, request);
    }

    private void addRecommendedItemView(RecommendedGoodsBean recommendedGoodsBean) {
        containerLayout.removeAllViews();
        int itemWidth = (UIUtils.getScreenWidth() - UIUtils.dip2px(10) * 2 - UIUtils.dip2px(14)) / 2;
        int itemHeight = (int)((236 / 328.0) * itemWidth) + UIUtils.dip2px(40) + UIUtils.dip2px(20);

        ArrayList<RecommendedGoodsBean.RecommendedGoodsItemBean> listData = recommendedGoodsBean.listData;
        if (listData != null && listData.size() > 0) {
            int size = listData.size();
            LinearLayout itemLayout = null;
            for (int i = 0; i < size; i++) {
                if (i == 1 && !isCharter()) {
                    addCharterView(itemLayout, itemWidth, itemHeight, recommendedGoodsBean.cityHeadPicture);
                }
                PayResultRecommendItemView itemView = new PayResultRecommendItemView(getContext());
                itemView.update(listData.get(i));
                LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(itemWidth, itemHeight);
                itemParams.topMargin = UIUtils.dip2px(18);
                boolean isNewLayout = isCharter() ? (i % 2 == 0) : (i == 0 || i % 2 != 0);
                if (isNewLayout) {
                    itemLayout = new LinearLayout(getContext());
                    itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                    containerLayout.addView(itemLayout);
                    itemParams.rightMargin = UIUtils.dip2px(14);
                }
                itemLayout.addView(itemView, itemParams);
            }
            if (size == 1 && !isCharter()) {
                addCharterView(itemLayout, itemWidth, itemHeight, recommendedGoodsBean.cityHeadPicture);
            }
        } else {
            if (isCharter()) {
                setVisibility(View.GONE);
            } else {
                addCharterView(containerLayout, itemWidth, itemHeight, recommendedGoodsBean.cityHeadPicture);
            }
        }
    }

    public void addCharterView(ViewGroup parentLayout, int itemWidth, int itemHight, String cityHeadPicture) {
        PayResultRecommendCharterView charterView = new PayResultRecommendCharterView(getContext());
        charterView.setData(cityName, cityId, cityHeadPicture);
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(itemWidth, itemHight);
        itemParams.topMargin = UIUtils.dip2px(18);
        parentLayout.addView(charterView, itemParams);
    }

    public boolean isCharter() {
        return orderType == 3 || orderType == 888;
    }
}
