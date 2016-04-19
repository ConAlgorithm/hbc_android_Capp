package com.hugboga.custom.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.huangbaoche.hbcframe.util.MLog;

/**
 * Created by admin on 2016/3/24.
 */
public class AliPay {

    private void pay(final Activity activity, final String payInfo, final Handler mHandler) {
        MLog.e("payInfo=" + payInfo);
        if (!TextUtils.isEmpty(payInfo)) {
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(activity);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo, true);

                    Message msg = new Message();
                    msg.what = PayResult.SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        }
    }
}
