package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.PhoneInfo;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;

/**
 * Created by admin on 2016/3/17.
 */

@ContentView(R.layout.fg_sku_detail)
public class FgSkuDetail extends FgWebInfo {

    public static final String WEB_CITY = "web_city";
    private SkuItemBean skuItemBean;//sku详情

    @Override
    protected void initView() {
        super.initView();
        getView().findViewById(R.id.header_right_btn).setVisibility(View.VISIBLE);
        if(this.getArguments()!=null){
            skuItemBean =  (SkuItemBean)getArguments().getSerializable(WEB_CITY);
        }
    }

    @Event({R.id.header_right_btn,R.id.phone_consultation,R.id.goto_order})
    private void onClickView(View view){
        switch (view.getId()){
            case R.id.header_right_btn:
                if(skuItemBean!=null){
                    String title =skuItemBean.goodsName;
                    String content = "这是详情";
                    WXShareUtils.getInstance(getActivity()).share(WXShareUtils.TYPE_SESSION,skuItemBean.goodsPicture,title,content, skuItemBean.url);
                }
                break;
            case R.id.phone_consultation:
                PhoneInfo.CallDial(getActivity(), Constants.CALL_NUMBER_IN);
                break;
            case R.id.goto_order:
                Bundle bundle =new Bundle();
                if(getArguments()!=null){
                    bundle.putAll(getArguments());
                }
                startFragment(new FgSkuSubmit(),bundle);
                break;
        }
    }

}
