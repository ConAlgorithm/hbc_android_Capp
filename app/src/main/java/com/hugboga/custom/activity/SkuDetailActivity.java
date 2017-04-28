package com.hugboga.custom.activity;


import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.WebAgent;
import com.hugboga.custom.data.request.RequestGoodsById;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.UnicornUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.GiftController;
import com.hugboga.custom.widget.ShareDialog;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import butterknife.Bind;
import butterknife.OnClick;

import static com.hugboga.custom.activity.WebInfoActivity.WEB_URL;


public class SkuDetailActivity extends BaseActivity implements View.OnKeyListener  {

    public static final String TAG = SkuDetailActivity.class.getSimpleName();
    public static final String WEB_SKU = "web_sku";
    public static final String WEB_CITY = "web_city";

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.goto_order)
    TextView gotoOrder;
    @Bind(R.id.webview)
    WebView webView;
    @Bind(R.id.sku_detail_empty_layout)
    LinearLayout emptyLayout;
    @Bind(R.id.sku_detail_content_layout)
    RelativeLayout contentLayout;

    private SkuItemBean skuItemBean;//sku详情
    private CityBean cityBean;
    private String goodsNo;

    private boolean isPerformClick = false;

    private DialogUtil mDialogUtil;
    private WebAgent webAgent;


    public void initView() {
        headerRightBtn.setVisibility(WXShareUtils.getInstance(activity).isInstall(false) ? View.VISIBLE : View.VISIBLE);
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSkuItemBean(false);


        // 启用javaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webAgent = new WebAgent(this, webView, cityBean, headerLeftBtn, TAG);
        webAgent.setSkuItemBean(skuItemBean);
        webView.addJavascriptInterface(webAgent, "javaObj");
        webView.setOnKeyListener(this);
        webView.setWebViewClient(webClient);
        webView.setWebChromeClient(webChromeClient);
        webView.setBackgroundColor(0x00000000);
        String ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(ua + " HbcC/" + ChannelUtils.getVersion());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        mDialogUtil = DialogUtil.getInstance(activity);
        String url = getIntent().getStringExtra(WEB_URL);
        if (!TextUtils.isEmpty(url)) {
            url = CommonUtils.getBaseUrl(url) + "userId="+ UserEntity.getUser().getUserId(activity)+"&t=" + new Random().nextInt(100000);
            webView.loadUrl(url);
        }

        SensorsUtils.setSensorsShowUpWebView(webView);
    }

    public void setGoodsOut() {// 商品已下架
        headerRightBtn.setVisibility(View.GONE);
        gotoOrder.setText("该商品已下架");
        gotoOrder.setTextColor(0xFFFFFFFF);
        gotoOrder.setBackgroundResource(R.drawable.bg_sku_detial_grey);
        gotoOrder.setOnClickListener(null);
    }

    @Override
    public String getEventId() {
        if (skuItemBean == null) {
            return "";
        }
        if (skuItemBean.goodsClass == 1) {//固定
            return StatisticConstant.LAUNCH_DETAIL_RG;
        }else {
            return StatisticConstant.LAUNCH_DETAIL_RT;
        }
    }

    @Override
    public String getEventSource() {
        if (skuItemBean == null) {
            return "";
        }
        if(skuItemBean.goodsClass == 1) {//固定
            return "固定线路包车";
        }else {
            return "推荐线路包车";
        }
    }

    public void goodsSoldOut() {
        headerRightBtn.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
    }

    private void getSkuItemBean(boolean isShowLoading) {
        if (!TextUtils.isEmpty(goodsNo)) {//skuItemBean == null &&
            isPerformClick = isShowLoading;
            RequestGoodsById request = new RequestGoodsById(activity, goodsNo);
            HttpRequestUtils.request(activity, request, new HttpRequestListener() {
                @Override
                public void onDataRequestCancel(BaseRequest request) {

                }

                @Override
                public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

                }

                public void onDataRequestSucceed(BaseRequest _request) {
                    ApiReportHelper.getInstance().addReport(_request);
                    if (_request instanceof RequestGoodsById) {
                        RequestGoodsById requestGoodsById = (RequestGoodsById) _request;
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
                        if (webAgent!= null) {
                            if (cityBean != null) {
                                webAgent.setCityBean(cityBean);
                            }
                            webAgent.setSkuItemBean(skuItemBean);
                        }
                        setSensorsEvent();
                    }
                }
            }, isShowLoading);
        } else {
            setSensorsEvent();
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }

    @OnClick({R.id.header_right_btn, R.id.goto_order,R.id.sku_detail_bottom_service_layout,R.id.sku_detail_bottom_online_layout,R.id.sku_detail_empty_tv,R.id.hint_iv})
    public void onClick(View view) {
        HashMap<String, String> map = new HashMap<String, String>();
        switch (view.getId()) {
            case R.id.header_right_btn:
                if (skuItemBean != null) {
                    String title = skuItemBean.goodsName;
                    String content = skuItemBean.salePoints;
                    String shareUrl = skuItemBean.shareURL == null ? skuItemBean.skuDetailUrl : skuItemBean.shareURL;
                    shareUrl = shareUrl == null ? "http://www.huangbaoche.com" : shareUrl;
                    skuShare(skuItemBean.goodsPicture, title, content, shareUrl);
                }
                break;
            case R.id.goto_order:
                if (!CommonUtils.isLogin(this)) {
                    return;
                }
                if (skuItemBean == null) {
                    getSkuItemBean(true);
                    break;
                }
                if (cityBean == null) {
                    cityBean = findCityById("" + skuItemBean.arrCityId);
                }
                SkuOrderActivity.Params params = new SkuOrderActivity.Params();
                params.skuItemBean = skuItemBean;
                params.cityBean = cityBean;
                Intent intent = new Intent(activity, SkuOrderActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
                startActivity(intent);

                map.put("routename", skuItemBean.goodsName);
                int countResult = 0;
                try {
                    countResult = Integer.parseInt(skuItemBean.goodsMinPrice);
                } catch (Exception e) {
                    LogUtil.e(e.toString());
                }
                MobclickAgent.onEventValue(activity, "chose_route", map, countResult);
                setSensorsOnClickEvent();
                break;
            case R.id.sku_detail_bottom_service_layout://联系客服
                if (TextUtils.isEmpty(getIntent().getStringExtra("type"))){
                    StatisticClickEvent.click(StatisticConstant.CLICK_CONCULT,"1".equals(getIntent().getStringExtra("type"))?"固定线路":"推荐线路");
                }
                DialogUtil.showCallDialogTitle(this, "");
                break;
            case R.id.sku_detail_bottom_online_layout://在线咨询
                if (TextUtils.isEmpty(getIntent().getStringExtra("type"))){
                    StatisticClickEvent.click(StatisticConstant.CLICK_CONCULT,"1".equals(getIntent().getStringExtra("type"))?"固定线路":"推荐线路");
                }
                UnicornUtils.openServiceActivity(this, UnicornServiceActivity.SourceType.TYPE_LINE, null, skuItemBean);
                break;
            case R.id.sku_detail_empty_tv:
                startActivity(new Intent(activity, MainActivity.class));
                EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
                break;
            case R.id.hint_iv:
                findViewById(R.id.hint_layout).setVisibility(View.GONE);
                break;
        }
    }

    private void skuShare(String goodsPicture, final String title, final String content, final String shareUrl) {
        CommonUtils.shareDialog(activity, skuItemBean.goodsPicture, title, content, shareUrl, getClass().getSimpleName()
                , new ShareDialog.OnShareListener() {
                    @Override
                    public void onShare(int type) {
                        if (skuItemBean != null) {
                            if (skuItemBean.goodsClass == 1) {
                                EventUtil.onShareSkuEvent(StatisticConstant.SHARERG, "" + type, getCityName());
                            } else if (skuItemBean.goodsClass == 2) {
                                EventUtil.onShareSkuEvent(StatisticConstant.SHARERT, "" + type, getCityName());
                            }
                        }
                    }
                });
    }

    private String getCityName() {
        return cityBean != null ? cityBean.enName : "";
    }


    WebChromeClient webChromeClient = new WebChromeClient() {


        @Override
        public void onReceivedTitle(WebView view, String title) {
//            super.onReceivedTitle(view, title);
            if (headerTitle == null) {
                return;
            }
            if (!view.getTitle().startsWith("http:")) {
//                headerTitle.setText(view.getTitle());
//                if (!TextUtils.isEmpty(getIntent().getStringExtra("type"))){
//                    if ("1".equals(getIntent().getStringExtra("type"))){
//                        headerTitle.setText(R.string.route_goods_detial_title);
//                    }
//                    if(getIntent().getStringExtra("type").equals("2")){
//                        headerTitle.setText(R.string.free_route_goods_detial_title);
//                    }
//                }else {
                    headerTitle.setText("线路详情");
//                }
            } else {
                headerTitle.setText("");
            }

        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            MLog.e("onJsAlert = " + message);
            mDialogUtil.showCustomDialog(message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            mDialogUtil.showCustomDialog(message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            return true;
        }
    };

    WebViewClient webClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            fgTitle.setText(view.getTitle());
            if (!SkuDetailActivity.this.isFinishing()) {
                gotoOrder.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            webView.loadUrl(url);
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            handler.proceed();
            handler.cancel();
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
            MLog.e("WebResourceResponse1 =" + url);
            return null;
        }

        @Override
        @TargetApi(21)
        public WebResourceResponse shouldInterceptRequest(final WebView view, WebResourceRequest interceptedRequest) {
            MLog.e("WebResourceResponse2 =" + interceptedRequest.getUrl());

            return null;
        }

        private WebResourceResponse processRequest(Uri uri) {
            MLog.d("GET: " + uri.toString());
            try {
                // Setup connection
                URL url = new URL(uri.toString());
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                // Set SSL Socket Factory for this request
                urlConnection.setSSLSocketFactory(DefaultSSLSocketFactory.getSocketFactory(activity).getSslContext().getSocketFactory());
                // Get content, contentType and encoding
                InputStream is = urlConnection.getInputStream();
                String contentType = urlConnection.getContentType();
                String encoding = urlConnection.getContentEncoding();
                // If got a contentType header
                if (contentType != null) {
                    String mimeType = contentType;
                    // Parse mime type from contenttype string
                    if (contentType.contains(";")) {
                        mimeType = contentType.split(";")[0].trim();
                    }
                    Log.d("SSL_PINNING_WEBVIEWS", "Mime: " + mimeType);
                    // Return the response
                    return new WebResourceResponse(mimeType, encoding, is);
                }

            } catch (SSLHandshakeException e) {
                Log.d("SSL_PINNING_WEBVIEWS", e.getLocalizedMessage());
            } catch (Exception e) {
                Log.d("SSL_PINNING_WEBVIEWS", e.getLocalizedMessage());
            }
            // Return empty response for this request
            return new WebResourceResponse(null, null, null);
        }

    };

    @Override
    public void onPause() {
        super.onPause();
        GiftController.getInstance(this).abortion();
    }

    @Override
    public void onResume() {
        super.onResume();
        GiftController.getInstance(this).showGiftDialog();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fg_sku_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (this.getIntent() != null) {
            skuItemBean = (SkuItemBean) getIntent().getSerializableExtra(WEB_SKU);
            cityBean = (CityBean) getIntent().getSerializableExtra(WEB_CITY);
            source = getIntent().getStringExtra("source");
            goodsNo = getIntent().getStringExtra(Constants.PARAMS_ID);
        }
        if (cityBean == null && skuItemBean != null && skuItemBean.arrCityId != 0) {
            cityBean = findCityById("" + skuItemBean.arrCityId);
        }
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        setSensorsShowEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case WECHAT_SHARE_SUCCEED:
                WXShareUtils wxShareUtils = WXShareUtils.getInstance(this);
                if (getClass().getSimpleName().equals(wxShareUtils.source)) {//分享成功
                    if (skuItemBean != null) {
                        if (skuItemBean.goodsClass == 1) {
                            EventUtil.onShareSkuEvent(StatisticConstant.SHARERG_BACK, "" + wxShareUtils.type, getCityName());
                        } else if (skuItemBean.goodsClass == 2) {
                            EventUtil.onShareSkuEvent(StatisticConstant.SHARERT_BACK, "" + wxShareUtils.type, getCityName());
                        }
                    }
                }
                break;
        }
    }

    //神策统计_浏览页面
    private void setSensorsShowEvent() {
        try {
            if (skuItemBean == null) {
                return;
            }
            JSONObject properties = new JSONObject();
            properties.put("hbc_web_title", "商品详情页");
            properties.put("hbc_web_url", SensorsConstant.SKUDETAIL + "?sku_id=" + skuItemBean.goodsNo);
            properties.put("hbc_refer", getIntentSource());
            SensorsDataAPI.sharedInstance(this).track("page_view", properties);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //神策统计_浏览商品线路
    private void setSensorsEvent() {
        try {
            if (skuItemBean == null) {
                return;
            }
            JSONObject properties = new JSONObject();
            properties.put("hbc_refer", getIntentSource());
            properties.put("hbc_sku_id", skuItemBean.goodsNo);
            properties.put("hbc_sku_name", skuItemBean.goodsName);
            properties.put("hbc_sku_type", skuItemBean.goodsClass == 1 ? "固定线路" : "推荐线路");
            properties.put("hbc_city_name", skuItemBean.depCityName);
            properties.put("hbc_price_average", CommonUtils.getCountInteger(skuItemBean.perPrice));
            SensorsDataAPI.sharedInstance(this).track("view_skudetail", properties);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //神策统计_商品进入下单
    private void setSensorsOnClickEvent() {
        try {
            if (skuItemBean == null) {
                return;
            }
            JSONObject properties = new JSONObject();
            properties.put("hbc_sku_id", skuItemBean.goodsNo);
            properties.put("hbc_sku_name", skuItemBean.goodsName);
            properties.put("hbc_sku_type", skuItemBean.goodsClass == 1 ? "固定线路" : "推荐线路");
            SensorsDataAPI.sharedInstance(this).track("sku_buy", properties);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
