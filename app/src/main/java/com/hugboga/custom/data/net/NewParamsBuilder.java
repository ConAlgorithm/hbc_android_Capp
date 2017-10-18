package com.hugboga.custom.data.net;

import com.huangbaoche.hbcframe.data.net.HbcParamsBuilder;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.PhoneInfo;

import org.xutils.http.RequestParams;

/**
 * Created  on 16/5/4.
 */
public class NewParamsBuilder extends HbcParamsBuilder {

    @Override
    public void buildParams(RequestParams params) {
        super.buildParams(params);

//        String android_id = Secure.getString(((BaseRequest)params).getContext().getContentResolver(), Secure.ANDROID_ID);

        params.setHeader("appChannel", ChannelUtils.getChannel(((BaseRequest)params).getContext()));
        params.setHeader("deviceId", com.huangbaoche.hbcframe.util.PhoneInfo.getImei(((BaseRequest)params).getContext()));//PhoneInfo.getImei(((BaseRequest)params).getContext()));
        params.setHeader("os","android");
        params.setHeader("appVersion",ChannelUtils.getVersion());
        params.setHeader("idfa", PhoneInfo.getIMEI(((BaseRequest)params).getContext()));
        params.setHeader("ts", "" + System.currentTimeMillis());
        MLog.e("URL = " + params.getUri());
        for (int i=0;i<params.getHeaders().size();i++) {
            MLog.e("new header = " +params.getHeaders().get(i).key+":"+params.getHeaders().get(i).value);
        }
    }
}
