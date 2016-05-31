package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.alipay.PayResult;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.WXpayBean;
import com.hugboga.custom.data.request.RequestOrderDetail;
import com.hugboga.custom.data.request.RequestPayNo;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.wxapi.WXPay;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;

/**
 * Created by qingcha on 16/5/31.
 * 部分代码及逻辑来自FgOrder
 */
@ContentView(R.layout.fg_choose_payment)
public class FgChoosePayment extends BaseFragment {

    @ViewInject(R.id.choose_payment_price_tv)
    TextView priceTV;

    private RequestParams requestParams;
    private DialogUtil mDialogUtil;

    public static FgChoosePayment newInstance(RequestParams params) {
        FgChoosePayment fragment = new FgChoosePayment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS_DATA, params);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initHeader(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            requestParams = (RequestParams)savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                requestParams = (RequestParams)bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        fgTitle.setText(getString(R.string.choose_payment_title));
        priceTV.setText(String.valueOf(requestParams.shouldPay));
    }

    @Override
    protected void initHeader() {

    }

    @Override
    protected void initView() {
        // 将该app注册到微信
        IWXAPI msgApi = WXAPIFactory.createWXAPI(getActivity(), Constants.WX_APP_ID);
        msgApi.registerApp(Constants.WX_APP_ID);
        mDialogUtil = DialogUtil.getInstance(getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (requestParams != null) {
            outState.putSerializable(Constants.PARAMS_DATA, requestParams);
        }
    }

    private void sendRequest(int payType) {
        RequestPayNo request = new RequestPayNo(getActivity(), requestParams.orderId, requestParams.shouldPay, payType, requestParams.couponId);
        requestData(request);
    }

    @Override
    protected Callback.Cancelable requestData() {
        RequestOrderDetail request = new RequestOrderDetail(getActivity(), requestParams.orderId);
        return requestData(request);
    }

    @Override
    protected void inflateContent() {

    }

    @Event({R.id.choose_payment_alipay_layout, R.id.choose_payment_wechat_layout,})
    private void onClickView(View view) {
        switch (view.getId()) {
            case R.id.choose_payment_alipay_layout://支付宝支付
                sendRequest(Constants.PAY_STATE_ALIPAY);
                break;
            case R.id.choose_payment_wechat_layout://微信支付
                if (!WXShareUtils.getInstance(getActivity()).isInstall(true)) {
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
                payByAlipay((String) mParser.getData());
            } else if (mParser.payType == Constants.PAY_STATE_WECHAT) {
                WXpayBean bean = (WXpayBean) mParser.getData();
                if (bean != null) {
                    if (bean.coupPay) {
                        mDialogUtil.showLoadingDialog();
                        mHandler.sendEmptyMessageDelayed(1, 3000);
                    } else {
                        WXPay.pay(getActivity(), bean);
                    }
                }
            }
        }
    }

    private void payByAlipay(final String payInfo) {
        if (!TextUtils.isEmpty(payInfo)) {
            if ("couppay".equals(payInfo)) {
                //优惠券0元支付
                mDialogUtil.showLoadingDialog();
                mHandler.sendEmptyMessageDelayed(1, 3000);//XXX 不理解为啥要延迟3秒
            } else {//正常支付
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(getActivity());
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
            finish();
//            Bundle bundle = new Bundle();
//            int overPrice = 0;
//            bundle.putInt("pay", overPrice);
//            bundle.putInt("orderType", mOrderBean.orderType);
//            bundle.putString(KEY_ORDER_ID, mOrderBean.orderNo);
//            bundle.putString("from", mSourceFragment.getClass().getSimpleName());
//            bundle.putString("source",source);
//            startFragment(new FgPaySuccess(), bundle);
//            notifyOrderList(FgTravel.TYPE_ORDER_RUNNING, true, false, false);
        }
    };

    //支付宝回调
    private Handler mAlipayHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case PayResult.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        mDialogUtil.showLoadingDialog();
                        mHandler.sendEmptyMessageDelayed(1, 3000);
                    } else if (TextUtils.equals(resultStatus, "8000")) {
                        Toast.makeText(getActivity(), "支付结果确认中", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case PayResult.SDK_CHECK_FLAG: {
                    Toast.makeText(getActivity(), "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
            inflateContent();
        }
    };

    public static class RequestParams implements Serializable {
        public String orderId;
        public double shouldPay;
        public String couponId;
        public String source;
        public boolean needShowAlert;
    }
}
