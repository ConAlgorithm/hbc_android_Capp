package com.hugboga.custom.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.request.RequestGoodsById;
import com.hugboga.custom.fragment.FgSkuNew;
import com.hugboga.custom.utils.DBHelper;
import com.umeng.analytics.MobclickAgent;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;


@ContentView(R.layout.fg_sku_detail)
public class SkuDetailActivity extends WebInfoActivity {
    @ViewInject(R.id.goto_order)
    TextView gotoOrder;

    public static final String WEB_SKU = "web_sku";
    public static final String WEB_CITY = "web_city";

    private SkuItemBean skuItemBean;//sku详情
    private CityBean cityBean;
    private String goodsNo;

    public boolean isGoodsOut = false;//商品是否已下架
    private boolean isPerformClick = false;

    @Override
    public void initView() {
        super.initView();
        isGoodsOut = false;
        findViewById(R.id.header_right_btn).setVisibility(WXShareUtils.getInstance(activity).isInstall(false)? View.VISIBLE:View.VISIBLE);
        if(this.getIntent()!=null){
            skuItemBean =  (SkuItemBean)getIntent().getSerializableExtra(WEB_SKU);
            cityBean =  (CityBean)getIntent().getSerializableExtra(WEB_CITY);
            source = getIntent().getStringExtra("source");
            goodsNo = getIntent().getStringExtra(Constants.PARAMS_ID);
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
        getSkuItemBean(false);
    }

    private void getSkuItemBean(boolean isShowLoading) {
        if (skuItemBean == null && !TextUtils.isEmpty(goodsNo)) {
            isPerformClick = isShowLoading;
            RequestGoodsById request = new RequestGoodsById(activity, goodsNo);
            HttpRequestUtils.request(activity, request, new HttpRequestListener(){
                @Override
                public void onDataRequestCancel(BaseRequest request) {

                }

                @Override
                public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

                }

                public void onDataRequestSucceed(BaseRequest _request) {
                    if (_request instanceof RequestGoodsById) {
                        RequestGoodsById requestGoodsById = (RequestGoodsById)_request;
                        skuItemBean = requestGoodsById.getData();
                        if (skuItemBean == null) {
                            return;
                        }
                        if (cityBean == null) {
                            cityBean = findCityById("" + skuItemBean.arrCityId);
                        }
                        if (isPerformClick) {
                            gotoOrder.performClick();
                        }
                    }
                }
            }, isShowLoading);
        }
    }

    private CityBean findCityById(String cityId) {
        DbManager mDbManager = new DBHelper(activity).getDbManager();
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
                    String content = activity.getString(R.string.wx_share_content);
                    String shareUrl = skuItemBean.shareURL==null?skuItemBean.skuDetailUrl:skuItemBean.shareURL;
                    shareUrl = shareUrl==null?"http://www.huangbaoche.com":shareUrl;
                    skuShare(skuItemBean.goodsPicture,title,content,shareUrl);
                }
                break;
            case R.id.goto_order:
                if (skuItemBean == null) {
                    getSkuItemBean(true);
                    break;
                }
                if (cityBean == null) {
                    cityBean = findCityById("" + skuItemBean.arrCityId);
                }
                Bundle bundle =new Bundle();
                bundle.putSerializable(SkuDetailActivity.WEB_SKU,skuItemBean);
                if (cityBean != null) {
                    bundle.putSerializable(SkuDetailActivity.WEB_CITY,cityBean);
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
                MobclickAgent.onEventValue(activity, "chose_route", map, countResult);
                break;
        }
    }

    private void skuShare(String goodsPicture, final String title, final String content, final String shareUrl) {
        if (isGoodsOut) {
            return;
        }
        final AlertDialog.Builder callDialog = new AlertDialog.Builder(activity);
        callDialog.setTitle("分享");
        final String [] callItems = new String[]{"分享好友","分享朋友圈"};
        callDialog.setItems(callItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uMengClickEvent("share_route");
                WXShareUtils.getInstance(activity).share(which+1, skuItemBean.goodsPicture, title, content, shareUrl);
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
        int countResult = 0;
        if (skuItemBean != null) {
            map_value.put("routename" , skuItemBean.goodsName);
//          map_value.put("quoteprice" , skuItemBean.goodsMinPrice);
            try {
                countResult = Integer.parseInt(skuItemBean.goodsMinPrice);
            }catch (Exception e){
                LogUtil.e(e.toString());
            }
        }
        MobclickAgent.onEventValue(activity, type, map_value, countResult);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
