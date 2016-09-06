package com.hugboga.custom.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.hugboga.custom.R;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.PayResultView;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {


//    @Bind(R.id.activity_result_view)
//    PayResultView resultView;

    private IWXAPI api;

    private boolean isPaySucceed = false;
    private String orderId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        ButterKnife.bind(this);

        initDefaultTitleBar();
        fgTitle.setText(getString(R.string.par_result_title));
        fgLeftBtn.setOnClickListener(null);
        fgLeftBtn.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams titleLeftBtnParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(10), RelativeLayout.LayoutParams.MATCH_PARENT);
        fgLeftBtn.setLayoutParams(titleLeftBtnParams);

        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);

        SharedPre sharedPre = new SharedPre(WXPayEntryActivity.this);
        orderId = sharedPre.getStringValue(SharedPre.PAY_WECHAT_DATA);
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
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            isPaySucceed = resp.errCode == BaseResp.ErrCode.ERR_OK;
//            resultView.initView(isPaySucceed, orderId);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//            if (isPaySucceed) {
//                resultView.intentHome();
//                return true;
//            } else {
//                resultView.setStatisticIsRePay(true);
//            }
        }
        return super.onKeyUp(keyCode, event);
    }

}