package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.hugboga.custom.fragment.FgOrderDetail;
import com.hugboga.custom.utils.CommonUtils;
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
 * Created by qingcha on 16/8/4.
 */
public class ChoosePaymentActivity extends BaseActivity {

    @Bind(R.id.choose_payment_price_tv)
    TextView priceTV;

    public static RequestParams requestParams;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
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
    private int wxResultCode = 0;

    public static class RequestParams implements Serializable {
        public String orderId;
        public double shouldPay;
        public String couponId;
        public String source;
        public boolean needShowAlert;

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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (requestParams != null) {
            outState.putSerializable(Constants.PARAMS_DATA, requestParams);
        }
    }

    private void initView() {
        headerTitle.setText(getString(R.string.choose_payment_title));
        priceTV.setText(requestParams.getShouldPay());
        // 将该app注册到微信
        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        msgApi.registerApp(Constants.WX_APP_ID);
        mDialogUtil = DialogUtil.getInstance(this);
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backWarn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = null;
        if (wxResultCode == EventType.BACK_HOME.ordinal()) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
            EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
        } else if (wxResultCode == EventType.ORDER_DETAIL.ordinal()) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
            EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));

            OrderDetailActivity.Params orderParams = new OrderDetailActivity.Params();
            orderParams.orderId = requestParams.orderId;
            intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra(Constants.PARAMS_DATA, orderParams);
            startActivity(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        wxResultCode = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case BACK_HOME:
                EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
                wxResultCode = EventType.BACK_HOME.ordinal();
                break;
            case ORDER_DETAIL:
                if (action.getData() instanceof Integer && (int)action.getData() == 1) {
                    EventBus.getDefault().post(new EventAction(EventType.FGTRAVEL_UPDATE));
                }
                wxResultCode = EventType.ORDER_DETAIL.ordinal();
            default:
                break;
        }
    }



    private void sendRequest(int payType) {
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
                if ("travelFundPay".equals(mParser.getData())) {//全部使用旅游基金支付的时候
                    mHandler.sendEmptyMessageDelayed(1, 3000);
                } else {
                    payByAlipay((String) mParser.getData());
                }
            } else if (mParser.payType == Constants.PAY_STATE_WECHAT) {
                WXpayBean bean = (WXpayBean) mParser.getData();
                if (bean != null) {
                    if (bean.travelFundPay) {//全部使用旅游基金支付的时候
                        mHandler.sendEmptyMessageDelayed(1, 3000);
                    } else if (bean.coupPay) {
                        mDialogUtil.showLoadingDialog();
                        mHandler.sendEmptyMessageDelayed(1, 3000);
                    } else {
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
                mHandler.sendEmptyMessageDelayed(1, 3000);
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
//            finish();
//            Bundle bundle = new Bundle();
//            int overPrice = 0;
//            bundle.putInt("pay", overPrice);
//            bundle.putInt("orderType", mOrderBean.orderType);
//            bundle.putString(KEY_ORDER_ID, mOrderBean.orderNo);
//            bundle.putString("from", mSourceFragment.getClass().getSimpleName());
//            bundle.putString("source",source);
//            startFragment(new FgPayResult(), bundle);
//            notifyOrderList(FgTravel.TYPE_ORDER_RUNNING, true, false, false);

            PayResultActivity.Params params = new PayResultActivity.Params();
            params.payResult = msg.what == 1;//1.支付成功，2.支付失败
            params.orderId = requestParams.orderId;
            params.paymentAmount = requestParams.getShouldPay();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backWarn();
    }

    private void backWarn() {
        DialogUtil dialogUtil = DialogUtil.getInstance(this);
        dialogUtil.showCustomDialog(getString(R.string.app_name), getString(R.string.order_cancel_pay), "确定离开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                clearFragmentList();
//                FgOrderDetail.Params orderParams = new FgOrderDetail.Params();
//                orderParams.orderId = requestParams.orderId;
//                startFragment(FgOrderDetail.newInstance(orderParams));

                Intent intent = new Intent(ChoosePaymentActivity.this, MainActivity.class);
                startActivity(intent);
                EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));

                OrderDetailActivity.Params orderParams = new OrderDetailActivity.Params();
                orderParams.orderId = requestParams.orderId;
                intent = new Intent(ChoosePaymentActivity.this, OrderDetailActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, orderParams);
                startActivity(intent);
            }
        }, "继续支付", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
