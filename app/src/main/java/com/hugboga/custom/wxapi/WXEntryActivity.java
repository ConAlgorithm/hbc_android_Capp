package com.hugboga.custom.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.constants.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by ZONGFI on 2015/5/14.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = WXEntryActivity.class.getSimpleName();
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onReq(BaseReq req) {
        MLog.e("onResp "+req.openId+" "+req.transaction );
    }

    @Override
    public void onResp(BaseResp resp) {
        MLog.e("onResp "+resp.errCode+" "+resp.errStr );
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                Log.i(TAG,"分享成功");
                break;
            case BaseResp.ErrCode.ERR_COMM:
                //一般错误
                Log.i(TAG,"一般错误");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                Log.i(TAG,"用户取消");
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                //发送失败
                Log.i(TAG,"发送失败");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //认证被否决
                Log.i(TAG,"认证被否决"+resp.errStr);
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                //不支持错误
                Log.i(TAG,"不支持错误");
                break;
        }
        finish();
    }
}
