package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.utils.PhoneInfo;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by admin on 2016/3/17.
 */

@ContentView(R.layout.fg_sku_detail)
public class FgSkuDetail extends FgWebInfo {

    @Override
    protected void initView() {
        super.initView();

    }

    @Event({R.id.phone_consultation,R.id.goto_order})
    private void onClickView(View view){
        switch (view.getId()){
            case R.id.phone_consultation:
                PhoneInfo.CallDial(getActivity(), Constants.CALL_NUMBER_IN);
                break;
            case R.id.goto_order:
                Bundle bundle =new Bundle();
                startFragment(new FgSkuSubmit(),bundle);
                break;
        }
    }

}
