package com.hugboga.custom.wxapi;







import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler, View.OnClickListener {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_pay_result);
        MLog.e(this + " onCreate");
    	api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
		this.findViewById(R.id.pay_success_btn).setOnClickListener(this);
		this.findViewById(R.id.header_left_btn).setOnClickListener(this);
		((TextView)this.findViewById(R.id.header_title)).setText("支付结果");
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		MLog.e("onPayFinish, errCode = " + resp.errCode+" errStr="+resp.errStr+" transaction= "+resp.transaction+" openId= "+resp.openId);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode!=BaseResp.ErrCode.ERR_OK){
				EventBus.getDefault().post(
						new EventAction(EventType.PAY_CANCEL));
				finish();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.header_left_btn:
			case R.id.pay_success_btn:
				notifyRefreshOrder();
				finish();
				break;
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			notifyRefreshOrder();
		}
		return super.onKeyUp(keyCode, event);
	}

	protected void notifyRefreshOrder() {
		EventBus.getDefault().post(
				new EventAction(EventType.REFRESH_ORDER_DETAIL));
		super.onDestroy();

	}
}