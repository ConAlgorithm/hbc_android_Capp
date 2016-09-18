package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.huangbaoche.hbcframe.data.net.ExceptionErrorCode;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.ServerException;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.alipay.PayResult;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.WXpayBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestPayNo;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.bean.EventPayBean;
import com.hugboga.custom.statistic.event.EventPay;
import com.hugboga.custom.statistic.event.EventPayResult;
import com.hugboga.custom.statistic.event.EventPayShow;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.wxapi.WXPay;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by on 16/8/4.
 */
public class ChoosePaymentActivity extends BaseActivity {

    @Bind(R.id.choose_payment_price_tv)
    TextView priceTV;
    @Bind(R.id.choose_payment_sign_tv)
    TextView choosePaymentSignTv;
    @Bind(R.id.choose_payment_alipay_iv)
    ImageView choosePaymentAlipayIv;
    @Bind(R.id.choose_payment_alipay_layout)
    RelativeLayout choosePaymentAlipayLayout;
    @Bind(R.id.choose_payment_wechat_iv)
    ImageView choosePaymentWechatIv;
    @Bind(R.id.choose_payment_wechat_layout)
    RelativeLayout choosePaymentWechatLayout;

    private DialogUtil mDialogUtil;
    private int payType;
    public RequestParams requestParams;

    public static class RequestParams implements Serializable {
        public String orderId;
        public double shouldPay;
        public String couponId;
        public String payDeadTime;
        public String source;
        public boolean needShowAlert;
        public EventPayBean eventPayBean;

        public String getShouldPay() {
            return String.valueOf(shouldPay);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            requestParams = (RequestParams) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                requestParams = (RequestParams) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        setContentView(R.layout.fg_choose_payment);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initDefaultTitleBar();

        initView();
        MobClickUtils.onEvent(new EventPayShow(requestParams.eventPayBean));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (requestParams != null) {
            outState.putSerializable(Constants.PARAMS_DATA, requestParams);
        }
    }

    private void initView() {
        initDefaultTitleBar();
        fgTitle.setText(getString(R.string.choose_payment_title));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        fgTitle.setLayoutParams(params);
        fgLeftBtn.setVisibility(View.GONE);
        fgRightBtn.setVisibility(View.GONE);
        TextView rightTV = (TextView) findViewById(R.id.header_right_txt);
        rightTV.setText("查看行程");
        rightTV.setVisibility(View.VISIBLE);
        rightTV.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        rightTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backWarn();
            }
        });

        priceTV.setText(requestParams.getShouldPay());
        // 将该app注册到微信
        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        msgApi.registerApp(Constants.WX_APP_ID);
        mDialogUtil = DialogUtil.getInstance(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case PAY_RESULT:
                if (requestParams.eventPayBean != null) {
                    requestParams.eventPayBean.paystyle = this.payType == Constants.PAY_STATE_ALIPAY ? "支付宝" : "微信支付";
                    MobClickUtils.onEvent(new EventPayResult(requestParams.eventPayBean, (boolean) action.getData()));
                }
                break;
            default:
                break;
        }
    }



    private void sendRequest(int payType) {
        if (requestParams.eventPayBean != null) {
            requestParams.eventPayBean.paystyle = payType == Constants.PAY_STATE_ALIPAY ? "支付宝" : "微信支付";
            MobClickUtils.onEvent(new EventPay(requestParams.eventPayBean));
        }
        this.payType = payType;
        RequestPayNo request = new RequestPayNo(this, requestParams.orderId, requestParams.shouldPay, payType, requestParams.couponId);
        requestData(request);
    }

    @OnClick({R.id.choose_payment_alipay_layout, R.id.choose_payment_wechat_layout,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_payment_alipay_layout://支付宝支付
                sendRequest(Constants.PAY_STATE_ALIPAY);
                break;
            case R.id.choose_payment_wechat_layout://微信支付
                if (!WXShareUtils.getInstance(this).isInstall(true)) {
                    return;
                }
                sendRequest(Constants.PAY_STATE_WECHAT);
                break;
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestPayNo) {
            RequestPayNo mParser = (RequestPayNo) request;
            if (mParser.payType == Constants.PAY_STATE_ALIPAY) {
                //支付宝使用旅游基金和优惠券0元支付
                if ("travelFundPay".equals(mParser.getData()) || "couppay".equals(mParser.getData())) {
                    mHandler.sendEmptyMessage(1);
                } else {
                    payByAlipay((String) mParser.getData());
                }
            } else if (mParser.payType == Constants.PAY_STATE_WECHAT) {
                WXpayBean bean = (WXpayBean) mParser.getData();
                if (bean != null) {
                    if (bean.travelFundPay) {//全部使用旅游基金支付的时候
                        mDialogUtil.showLoadingDialog();
                        mHandler.sendEmptyMessage(1);
                    } else if (bean.coupPay) {
                        mDialogUtil.showLoadingDialog();
                        mHandler.sendEmptyMessage(1);
                    } else {
                        SharedPre sharedPre = new SharedPre(ChoosePaymentActivity.this);
                        sharedPre.saveStringValue(SharedPre.PAY_WECHAT_DATA, requestParams.orderId);
                        WXPay.pay(this, bean);
                    }
                }
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest parser) {
        if (errorInfo.state == ExceptionErrorCode.ERROR_CODE_SERVER) {
            ServerException exception = (ServerException) errorInfo.exception;
            if (exception.getCode() == 2) {
                mDialogUtil.showLoadingDialog();
                mHandler.sendEmptyMessageDelayed(1, 3000);
                return;
            }
        }
        super.onDataRequestError(errorInfo, parser);
    }

    private void payByAlipay(final String payInfo) {
        if (!TextUtils.isEmpty(payInfo)) {
            if ("couppay".equals(payInfo)) {
                //优惠券0元支付
                mDialogUtil.showLoadingDialog();
                mHandler.sendEmptyMessage(1);
            } else {//正常支付
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(ChoosePaymentActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(payInfo, true);
                        Message msg = new Message();
                        msg.what = PayResult.SDK_PAY_FLAG;
                        msg.obj = result;
                        mAlipayHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mDialogUtil.dismissLoadingDialog();
            PayResultActivity.Params params = new PayResultActivity.Params();
            params.payResult = msg.what == 1;//1.支付成功，2.支付失败
            params.orderId = requestParams.orderId;
            Intent intent = new Intent(ChoosePaymentActivity.this, PayResultActivity.class);
            intent.putExtra(Constants.PARAMS_DATA, params);
            ChoosePaymentActivity.this.startActivity(intent);
        }
    };

    //支付宝回调
    private Handler mAlipayHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PayResult.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {//支付成功
                        mDialogUtil.showLoadingDialog();
                        mHandler.sendEmptyMessageDelayed(1, 3000);
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        CommonUtils.showToast("支付结果确认中");
                    } else {//支付失败
                        mHandler.sendEmptyMessage(2);
                    }
                    break;
                }
                case PayResult.SDK_CHECK_FLAG: {
                    CommonUtils.showToast("检查结果为：" + msg.obj);
                    break;
                }
                default:
                    break;
            }
        }
    };

    private void backWarn() {
        DialogUtil dialogUtil = DialogUtil.getInstance(this);
        dialogUtil.showCustomDialog(getString(R.string.app_name), getString(R.string.order_cancel_pay, requestParams.payDeadTime), "确定离开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EventUtil eventUtil = EventUtil.getInstance();
                eventUtil.isRePay = false;

                Intent intent = new Intent(ChoosePaymentActivity.this, MainActivity.class);
                startActivity(intent);
                EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 2));
                EventBus.getDefault().post(new EventAction(EventType.TRAVEL_LIST_TYPE, 1));
            }
        }, "继续支付", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            backWarn();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
