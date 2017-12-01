package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.InsureActivity;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.SingleActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.PayResultExtarParamsBean;
import com.hugboga.custom.data.bean.PaySucceedBean;
import com.hugboga.custom.data.bean.PickupCouponOpenBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventBusPayResult;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestPaySucceed;
import com.hugboga.custom.data.request.RequestPickupCouponOpen;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.SharedPre;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/9/5.
 */
public class PayResultView extends RelativeLayout implements HttpRequestListener {

    @BindView(R.id.view_pay_result_state_iv)
    ImageView stateIV;
    @BindView(R.id.view_pay_result_state_tv)
    TextView stateTV;
    @BindView(R.id.view_pay_result_order_tv)
    TextView orderTV;
    @BindView(R.id.view_pay_result_book_tv)
    TextView bookTV;
    @BindView(R.id.view_pay_result_service_layout)
    LinearLayout serviceLayout;
    @BindView(R.id.view_pay_result_desc_layout)
    LinearLayout descLayout;
    @BindView(R.id.view_pay_result_recommend_layout)
    PayResultRecommendLayout recommendView;
    @BindView(R.id.view_pay_result_bargain_layout)
    PayResultBargainLayout bargainLayout;
    @BindView(R.id.view_pay_result_bargain_bottom_space)
    View bargainBottomSpace;

    private boolean isPaySucceed; //支付结果
    private String orderId;
    private int orderType;
    private PaySucceedBean paySucceedBean;
    private ErrorHandler errorHandler;
    private PayResultExtarParamsBean extarParamsBean;

    public PayResultView(Context context) {
        this(context, null);
    }

    public PayResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final View view = inflate(context, R.layout.view_pay_result, this);
        ButterKnife.bind(view);
    }

    @OnClick({R.id.view_pay_result_order_tv
            , R.id.view_pay_result_book_tv
            , R.id.view_pay_result_domestic_service_layout
            , R.id.view_pay_result_overseas_service_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_pay_result_order_tv:
                if (isPaySucceed) {
                    intentOrderDetail();
                } else {
                    setStatisticIsRePay(true);
                    ((Activity) getContext()).finish();
                }
                break;
            case R.id.view_pay_result_book_tv:
                if (orderType == 4) {
                    SingleActivity.Params params = new SingleActivity.Params();
                    params.cityId = "" + extarParamsBean.cityId;
                    params.startPoiBean = extarParamsBean.startPoiBean;
                    params.endPoiBean = extarParamsBean.destPoiBean;
                    params.guidesDetailData = extarParamsBean.guidesDetailData;
                    Intent intent = new Intent(getContext(), SingleActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_succeed));
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    getContext().startActivity(intent);

                    //单次接送催促下单后不再显示催促下单入口。
                    SharedPre.setString(SharedPre.PAY_ORDER, orderId);
                } else if (orderType == 1) {
                    PickSendActivity.Params params = new PickSendActivity.Params();
                    params.airPortBean = extarParamsBean.airPortBean;
                    params.startPoiBean = extarParamsBean.startPoiBean;
                    params.guidesDetailData = extarParamsBean.guidesDetailData;
                    params.type = 1;
                    Intent intent = new Intent(getContext(), PickSendActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_succeed));
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    getContext().startActivity(intent);
                }
                break;
            case R.id.view_pay_result_domestic_service_layout:
                PhoneInfo.CallDial(getContext(), Constants.CALL_NUMBER_IN);
                break;
            case R.id.view_pay_result_overseas_service_layout:
                PhoneInfo.CallDial(getContext(), Constants.CALL_NUMBER_OUT);
                break;
        }
    }

    public void initView(boolean _isPaySucceed, String _orderId, int orderType, PayResultExtarParamsBean _extarParamsBean) {
        this.isPaySucceed = _isPaySucceed;
        this.orderId = _orderId;
        this.orderType = orderType;

        EventBus.getDefault().post(new EventBusPayResult(isPaySucceed, _orderId));

        if (isPaySucceed) {
            stateIV.setBackgroundResource(R.mipmap.pay_succeed_icon);
            stateTV.setVisibility(GONE);
            orderTV.setText(R.string.par_result_detail2);
            RequestPaySucceed request = new RequestPaySucceed(getContext(), orderId);
            HttpRequestUtils.request(getContext(), request, this);
            if (orderType == 1) {
                HttpRequestUtils.request(getContext(), new RequestPickupCouponOpen(getContext()), this, false);
            }
            bargainLayout.setParams(orderId, orderType);

            if (_extarParamsBean == null) {
                bookTV.setVisibility(View.GONE);
            } else {
                this.extarParamsBean = _extarParamsBean;
                String oldOrderId = SharedPre.getString(SharedPre.PAY_ORDER, "");
                if (orderType == 4 && TextUtils.isEmpty(oldOrderId)) {
                    bookTV.setVisibility(View.VISIBLE);
                    bookTV.setText(R.string.par_result_book_back);
                } else if (orderType == 1) {
                    bookTV.setVisibility(View.VISIBLE);
                    bookTV.setText(R.string.par_result_book_send);
                } else {
                    bookTV.setVisibility(View.GONE);
                }
            }
        } else {
            stateIV.setBackgroundResource(R.mipmap.pay_failure_icon);
            stateTV.setText(R.string.par_result_failure_prompt);
            orderTV.setText(R.string.par_result_repay);
            serviceLayout.setVisibility(View.VISIBLE);
            bookTV.setVisibility(View.GONE);
        }
    }

    public void intentOrderDetail() {
        intentHome();

        OrderDetailActivity.Params orderParams = new OrderDetailActivity.Params();
        orderParams.orderId = orderId;
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, orderParams);
        intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_title));
        getContext().startActivity(intent);
    }

    public void intentHome() {
        Intent intent = new Intent(getContext(),MainActivity.class);
        intent.putExtras(new Bundle());
        getContext().startActivity(intent);
        EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
        if (isPaySucceed) {
            EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
        }
        setStatisticIsRePay(false);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        ApiReportHelper.getInstance().addReport(_request);
        if (_request instanceof RequestPaySucceed) {
            RequestPaySucceed request = (RequestPaySucceed) _request;
            paySucceedBean = request.getData();
            if (paySucceedBean == null) {
                return;
            }

            String stateStr = paySucceedBean.getStateStr();
            if (!TextUtils.isEmpty(stateStr)) {
                stateTV.setVisibility(View.VISIBLE);
                stateTV.setText(stateStr);
            }

            descLayout.setVisibility(View.VISIBLE);

            String descStr = paySucceedBean.getDescStr();
            if (!TextUtils.isEmpty(descStr)) {
                TextView descTV = (TextView) descLayout.findViewById(R.id.view_pay_result_desc_tv);
                descTV.setVisibility(View.VISIBLE);
                descTV.setText(descStr);
            }

            TextView insuranceTV = (TextView) descLayout.findViewById(R.id.view_pay_result_desc_insurance_tv);
            String insuranceStr = CommonUtils.getString(R.string.par_result_succeed_desc_insurance);
            String insuranceFocusStr = CommonUtils.getString(R.string.par_result_succeed_desc_insurance_click);
            if (getContext() instanceof Activity) {
                insuranceTV.setVisibility(View.VISIBLE);
                OrderUtils.genCLickSpan((Activity) getContext(), insuranceTV, insuranceStr, insuranceFocusStr, null
                        , getContext().getResources().getColor(R.color.default_highlight_blue)
                        , new OrderUtils.MyCLickSpan.OnSpanClickListener() {
                            @Override
                            public void onSpanClick(View view) {
                                Bundle insureBundle = new Bundle();
                                insureBundle.putString(Constants.PARAMS_ID, orderId);
                                Intent intent = new Intent(getContext(), InsureActivity.class);
                                intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_title));
                                intent.putExtras(insureBundle);
                                getContext().startActivity(intent);
                            }
                        });
            } else {
                insuranceTV.setVisibility(View.GONE);
            }

            bargainLayout.setVisibility(paySucceedBean.getBargainStatus() ? View.VISIBLE : View.GONE);
            bargainBottomSpace.setVisibility(paySucceedBean.getBargainStatus() ? View.VISIBLE : View.GONE);

            recommendView.setRequestParams(paySucceedBean.getCityName(), "" + paySucceedBean.getCityId(), paySucceedBean.getGoodsNo(), orderType);
        } else if (_request instanceof RequestPickupCouponOpen) {
            PickupCouponOpenBean pickupCouponOpenBean = ((RequestPickupCouponOpen)_request).getData();
            if (pickupCouponOpenBean != null && pickupCouponOpenBean.isOpen) {
                PickupCouponDialog dialog = new PickupCouponDialog(getContext());
                dialog.show();
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

    /**
     * 统计是否是重新支付
     * */
    public void setStatisticIsRePay(boolean isRePay) {
        EventUtil eventUtil = EventUtil.getInstance();
        eventUtil.isRePay = isRePay;
    }
}
