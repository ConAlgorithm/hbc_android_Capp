package com.hugboga.custom.wxapi;

import android.content.Context;
import android.content.Intent;

import com.hugboga.custom.constants.Constants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by admin on 2016/3/24.
 */
public class WXPay {
    public static void pay(Context context){
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
// 将该app注册到微信
        msgApi.registerApp(Constants.WX_APP_ID);

        PayReq request = new PayReq();
        request.appId = Constants.WX_APP_ID;
        request.partnerId = "1316752401";
        request.prepayId= "1101000000140415649af9fc314aa427";
        request.packageValue = "Sign=WXPay";
        request.nonceStr= "1101000000140429eb40476f8896f4c9";
        request.timeStamp= "1398746574";
        request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
        msgApi.sendReq(request);
        context.startActivity(new Intent(context,WXEntryActivity.class));
    }

}
