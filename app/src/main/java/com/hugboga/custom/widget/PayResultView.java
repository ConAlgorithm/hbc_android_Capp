package com.hugboga.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.BargainActivity;
import com.hugboga.custom.activity.CityListActivity;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.PaySucceedBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestPaySucceed;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 16/9/5.
 */
public class PayResultView extends RelativeLayout implements HttpRequestListener {

    @Bind(R.id.view_pay_result_succeed_prompt_layout)
    LinearLayout succeedPromptLayout;
    @Bind(R.id.view_pay_result_bargain_layout)
    LinearLayout bargainLayout;
    @Bind(R.id.view_pay_result_line_tv)
    TextView lineTV;

    private boolean isPaySucceed; //支付结果
    private String orderId;
    private PaySucceedBean paySucceedBean;
    private ErrorHandler errorHandler;

    public PayResultView(Context context) {
        this(context, null);
    }

    public PayResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final View view = inflate(context, R.layout.view_pay_result, this);
        ButterKnife.bind(view);
    }

    /**
     * 按钮状态
     * 支付成功: 返回首页 | 查看订单
     * 支付失败: 查看订单 | 重新支付
     * */
    @OnClick({R.id.view_pay_result_left_tv
            , R.id.view_pay_result_right_tv
            , R.id.view_pay_result_domestic_service_layout
            , R.id.view_pay_result_overseas_service_layout
            , R.id.view_pay_result_bargain_layout
            , R.id.view_pay_result_line_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_pay_result_left_tv:
                if (isPaySucceed) {
                    intentHome();
                } else {
                    DefaultSSLSocketFactory.resetSSLSocketFactory(getContext());
                    intentOrderDetail();
                }
                break;
            case R.id.view_pay_result_right_tv:
                if (isPaySucceed) {
                    intentOrderDetail();
                } else {
                    setStatisticIsRePay(true);
                    ((Activity) getContext()).finish();
                }
                break;
            case R.id.view_pay_result_domestic_service_layout:
                PhoneInfo.CallDial(getContext(), Constants.CALL_NUMBER_IN);
                break;
            case R.id.view_pay_result_overseas_service_layout:
                PhoneInfo.CallDial(getContext(), Constants.CALL_NUMBER_OUT);
                break;
            case R.id.view_pay_result_bargain_layout: //砍价
                Intent intentBargain = new Intent(getContext(), BargainActivity.class);
                intentBargain.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_title));
                intentBargain.putExtra("orderNo", orderId);
                getContext().startActivity(intentBargain);
                break;
            case R.id.view_pay_result_line_tv: //城市列表
                if (paySucceedBean != null && paySucceedBean.getCityId() != 0) {
                    CityListActivity.Params params = new CityListActivity.Params();
                    params.id = paySucceedBean.getCityId();
                    params.cityHomeType = CityListActivity.CityHomeType.CITY;
                    params.titleName = paySucceedBean.getCityName();
                    Intent intent = new Intent(getContext(), CityListActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_title));
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    getContext().startActivity(intent);
                }
                break;
        }
    }

    public void initView(boolean _isPaySucceed, String _orderId) {
        this.isPaySucceed = _isPaySucceed;
        this.orderId = _orderId;

        EventBus.getDefault().post(new EventAction(EventType.PAY_RESULT, isPaySucceed));

        TextView leftTV = (TextView)findViewById(R.id.view_pay_result_left_tv);
        TextView rightTV = (TextView)findViewById(R.id.view_pay_result_right_tv);
        leftTV.setText(getContext().getString(isPaySucceed ? R.string.par_result_back : R.string.par_result_detail));
        rightTV.setText(getContext().getString(isPaySucceed ? R.string.par_result_detail : R.string.par_result_repay));

        updateStateLayout();
        updateServiceLayout();

        succeedPromptLayout.setVisibility(View.GONE);
        bargainLayout.setVisibility(View.GONE);
        lineTV.setText("");
        if (isPaySucceed) {
            RequestPaySucceed request = new RequestPaySucceed(getContext(), orderId);
            HttpRequestUtils.request(getContext(), request, this);
        }
    }

    private void updateStateLayout() {
        ImageView stateIV = (ImageView)findViewById(R.id.view_pay_result_state_iv);
        TextView stateTV = (TextView)findViewById(R.id.view_pay_result_state_tv);
        TextView promptTV = (TextView)findViewById(R.id.view_pay_result_prompt_tv);
        stateIV.setBackgroundResource(isPaySucceed ? R.mipmap.payment_success : R.mipmap.payment_fail);
        stateTV.setText(getContext().getString(isPaySucceed ? R.string.par_result_succeed : R.string.par_result_failure));
        promptTV.setText(getContext().getString(isPaySucceed ? R.string.par_result_succeed_prompt : R.string.par_result_failure_prompt));
    }

    private void updateServiceLayout() {
        final int visibility = isPaySucceed ? View.GONE : View.VISIBLE;
        findViewById(R.id.view_pay_result_service_title_layout).setVisibility(visibility);
        findViewById(R.id.view_pay_result_domestic_service_layout).setVisibility(visibility);
        findViewById(R.id.view_pay_result_overseas_service_layout).setVisibility(visibility);
    }

    private void intentOrderDetail() {
        intentHome();

        OrderDetailActivity.Params orderParams = new OrderDetailActivity.Params();
        orderParams.orderId = orderId;
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, orderParams);
        intent.putExtra(Constants.PARAMS_SOURCE, getContext().getString(R.string.par_result_title));
        getContext().startActivity(intent);
    }

    public void intentHome() {
        getContext().startActivity(new Intent(getContext(), MainActivity.class));
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

            String succeedPrompt = paySucceedBean.getSucceedPrompt();
            if (TextUtils.isEmpty(succeedPrompt)) {
                succeedPromptLayout.setVisibility(View.GONE);
            } else {
                succeedPromptLayout.setVisibility(View.VISIBLE);
                TextView promptTV = (TextView) findViewById(R.id.view_pay_result_succeed_prompt_tv);
                if (!TextUtils.isEmpty(paySucceedBean.getHighLightStr())) {
                    int startIndex = succeedPrompt.indexOf(paySucceedBean.getHighLightStr());
                    int endIndex = startIndex + paySucceedBean.getHighLightStr().length();
                    SpannableStringBuilder ssb = new SpannableStringBuilder(succeedPrompt);
                    ForegroundColorSpan yellowSpan = new ForegroundColorSpan(Color.parseColor("#ff6633"));
                    ssb.setSpan(yellowSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    promptTV.setText(ssb);
                } else {
                    promptTV.setText(succeedPrompt);
                }
            }
            bargainLayout.setVisibility(paySucceedBean.getBargainStatus() ? View.VISIBLE : View.GONE);
            lineTV.setText(paySucceedBean.getGoodMsg());
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
