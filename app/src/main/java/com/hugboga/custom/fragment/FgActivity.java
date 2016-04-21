package com.hugboga.custom.fragment;

/**
 * Created by dyt on 16/4/21.
 */

import android.view.View;

import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;



/**
 * Created by admin on 2016/3/17.
 */

@ContentView(R.layout.fg_sku_detail)
public class FgActivity extends FgWebInfo {

    @Override
    protected void initView() {
        super.initView();
        getView().findViewById(R.id.header_right_btn).setVisibility(WXShareUtils.getInstance(getActivity()).isInstall(false)? View.VISIBLE:View.GONE);

    }

    @Event({R.id.header_right_btn,R.id.phone_consultation,R.id.goto_order})
    private void onClickView(View view){
        switch (view.getId()){
            case R.id.header_right_btn:

                break;
            case R.id.phone_consultation:
                DialogUtil.getInstance(getActivity()).showCallDialog();
                break;
            case R.id.goto_order:

                break;
        }
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
