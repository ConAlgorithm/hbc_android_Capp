package com.hugboga.custom.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/3/17.
 */

@ContentView(R.layout.fg_sku_detail)
public class FgSkuDetail extends FgWebInfo {

    public static final String WEB_SKU = "web_sku";
    public static final String WEB_CITY = "web_city";
    private SkuItemBean skuItemBean;//sku详情
    private CityBean cityBean;

    public boolean isGoodsOut = false;//商品是否已下架

    @Override
    protected void initView() {
        super.initView();
        isGoodsOut = false;
        getView().findViewById(R.id.header_right_btn).setVisibility(WXShareUtils.getInstance(getActivity()).isInstall(false)?View.VISIBLE:View.VISIBLE);
        if(this.getArguments()!=null){
            skuItemBean =  (SkuItemBean)getArguments().getSerializable(WEB_SKU);
            cityBean =  (CityBean)getArguments().getSerializable(WEB_CITY);
            source = getArguments().getString("source");
        }
        if (cityBean == null && skuItemBean != null && skuItemBean.arrCityId != 0) {
            cityBean = findCityById("" + skuItemBean.arrCityId);
        }
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private CityBean findCityById(String cityId) {
        DbManager mDbManager = new DBHelper(getActivity()).getDbManager();
        try {
            cityBean = mDbManager.findById(CityBean.class, cityId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return cityBean;
    }

    @Event({R.id.header_right_btn,R.id.goto_order})
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
            case R.id.goto_order:
                Bundle bundle =new Bundle();
                if(getArguments()!=null){
                    bundle.putAll(getArguments());
                }
                bundle.putSerializable(FgSkuDetail.WEB_SKU,skuItemBean);
                if (cityBean != null) {
                    bundle.putSerializable(FgSkuDetail.WEB_CITY,cityBean);
                }
                bundle.putString("source",source);
//                startFragment(new FgSkuSubmit(), source);
                startFragment(new FgSkuNew(), bundle);
//                if (cityBean != null) {
//                    map.put("routecity", cityBean.name);
//                }
                map.put("routename", skuItemBean.goodsName);
//                map.put("quoteprice", skuItemBean.goodsMinPrice);
                int countResult = 0;
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
        if (isGoodsOut) {
            return;
        }
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
        int countResult = 0;
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
