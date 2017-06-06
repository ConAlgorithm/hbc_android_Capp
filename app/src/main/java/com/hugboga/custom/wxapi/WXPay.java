package com.hugboga.custom.wxapi;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.WXpayBean;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by admin on 2016/3/24.
 */
public class WXPay {
    public static void pay(Context context,WXpayBean bean){
        IWXAPI msgApi = WXAPIFactory.createWXAPI(context, BuildConfig.WX_APP_ID);
//// 将该app注册到微信
//        msgApi.registerApp(Constants.WX_APP_ID);

        PayReq request = new PayReq();
        request.appId = bean.appid;
        request.partnerId = bean.partnerid;
        request.prepayId= bean.prepayid;
        request.packageValue = bean.packageinfo;
        request.nonceStr= bean.noncestr;
        request.timeStamp= bean.timestamp;
        request.sign= bean.sign;
        msgApi.sendReq(request);
    }

}
