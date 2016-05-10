package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2016/3/17.
 */

@ContentView(R.layout.fg_sku_detail)
public class FgSkuDetail extends FgWebInfo {

    public static final String WEB_SKU = "web_sku";
    public static final String WEB_CITY = "web_city";
    private SkuItemBean skuItemBean;//sku详情
    private CityBean cityBean;

    @Override
    protected void initView() {
        super.initView();
        getView().findViewById(R.id.header_right_btn).setVisibility(WXShareUtils.getInstance(getActivity()).isInstall(false)?View.VISIBLE:View.VISIBLE);
        if(this.getArguments()!=null){
            skuItemBean =  (SkuItemBean)getArguments().getSerializable(WEB_SKU);
            cityBean =  (CityBean)getArguments().getSerializable(WEB_CITY);
            source = getArguments().getString("source");
        }
    }

    @Event({R.id.header_right_btn,R.id.phone_consultation,R.id.goto_order})
    private void onClickView(View view){
        HashMap<String,String> map = new HashMap<String,String>();
        switch (view.getId()){
            case R.id.header_right_btn:
                if(skuItemBean!=null){
                    String title =skuItemBean.goodsName;
                    String content = getActivity().getString(R.string.wx_share_content);
                    String shareUrl = skuItemBean.shareURL==null?skuItemBean.skuDetailUrl:skuItemBean.shareURL;
                    shareUrl = shareUrl==null?"http://www.huangbaoche.com":shareUrl;
                    skuShare(skuItemBean.goodsPicture,title,content,shareUrl);
                }
                break;
            case R.id.phone_consultation:
                map.put("source", "线路SKU页面");
                MobclickAgent.onEvent(getActivity(), "callcenter_route", map);

                DialogUtil.getInstance(getActivity()).showCallDialog("线路SKU页面","calldomestic_route","calloverseas_route");
                break;
            case R.id.goto_order:
                Bundle bundle =new Bundle();
                if(getArguments()!=null){
                    bundle.putAll(getArguments());
                }
                startFragment(new FgSkuSubmit(), bundle);

                map.put("routecity", cityBean.name);
                map.put("routename", skuItemBean.goodsName);
//                map.put("quoteprice", skuItemBean.goodsMinPrice);
                int countResult = -1;
                try {
                    countResult = Integer.parseInt(skuItemBean.goodsMinPrice);
                }catch (Exception e){
                    LogUtil.e(e.toString());
                }
                MobclickAgent.onEventValue(getActivity(), "chose_route", map, countResult);
                break;
        }
    }

    private void skuShare(String goodsPicture, final String title, final String content, final String shareUrl) {
        final AlertDialog.Builder callDialog = new AlertDialog.Builder(getActivity());
        callDialog.setTitle("分享");
        final String [] callItems = new String[]{"分享好友","分享朋友圈"};
        callDialog.setItems(callItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uMengClickEvent("share_route");
                WXShareUtils.getInstance(getActivity()).share(which+1, skuItemBean.goodsPicture, title, content, shareUrl);
            }
        });
        AlertDialog dialog = callDialog.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        uMengClickEvent("launch_route");
    }

    private void uMengClickEvent(String type){
        Map<String, String> map_value = new HashMap<String, String>();
        map_value.put("routecity" , source);
        map_value.put("routename" , skuItemBean.goodsName);
//        map_value.put("quoteprice" , skuItemBean.goodsMinPrice);
        int countResult = -1;
        try {
            countResult = Integer.parseInt(skuItemBean.goodsMinPrice);
        }catch (Exception e){
            LogUtil.e(e.toString());
        }
        MobclickAgent.onEventValue(getActivity(), type, map_value, countResult);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
