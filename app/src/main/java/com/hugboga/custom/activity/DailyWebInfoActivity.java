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
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.net.WebAgent;
import com.hugboga.custom.data.request.RequestGoodsById;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.ShareDialog;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hugboga.custom.activity.WebInfoActivity.CONTACT_SERVICE;
import static com.hugboga.custom.activity.WebInfoActivity.WEB_URL;

public class DailyWebInfoActivity extends BaseActivity implements View.OnKeyListener {

    public static final String EVENT_SOURCE = "包车详情咨询客服";

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

    private SkuItemBean skuItemBean;//sku详情

    private CityBean cityBean;
    private String goodsNo;

    public boolean isGoodsOut = false;//商品是否已下架
    private boolean isPerformClick = false;

    private DialogUtil mDialogUtil;
    CollectGuideBean collectGuideBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.cityBean = (CityBean)getIntent().getSerializableExtra("cityBean");
        this.collectGuideBean = (CollectGuideBean)getIntent().getSerializableExtra("collectGuideBean");
        setContentView(R.layout.fg_sku_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();

        EventUtil eventUtil = EventUtil.getInstance();
        eventUtil.sourceDetail = getIntentSource();
        Map map = new HashMap();
        map.put(Constants.PARAMS_SOURCE_DETAIL,eventUtil.sourceDetail);
        MobClickUtils.onEvent(getEventId(),map);
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_DETAIL_R;
    }

    @Override
    public String getEventSource() {
        return EVENT_SOURCE;
    }

    WebChromeClient webChromeClient = new WebChromeClient() {


        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!view.getTitle().startsWith("http:")) {
                headerTitle.setText(view.getTitle());
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
            gotoOrder.setVisibility(View.VISIBLE);
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
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case WECHAT_SHARE_SUCCEED:
                WXShareUtils wxShareUtils = WXShareUtils.getInstance(this);
                if (getClass().getSimpleName().equals(wxShareUtils.source)) {//分享成功
                    EventUtil.onShareDefaultEvent(StatisticConstant.SHARER_BACK, "" + wxShareUtils.type);
                }
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }

    public void initView() {
        isGoodsOut = false;
        findViewById(R.id.header_right_btn).setVisibility(WXShareUtils.getInstance(this).isInstall(false) ? View.VISIBLE : View.VISIBLE);
        if (this.getIntent() != null) {
            cityBean = (CityBean) getIntent().getSerializableExtra("cityBean");
        }
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 启用javaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.addJavascriptInterface(new WebAgent(this, webView, cityBean, headerLeftBtn), "javaObj");
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
        initHeader();
        String url = getIntent().getStringExtra(WEB_URL);
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
        MLog.e("url=" + url);
    }

    public void initHeader() {
//        fgTitle.setTextColor(getResources().getColor(R.color.my_content_title_color));
//        fgTitle.setText("客服中心");
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
        if (this.getIntent().getBooleanExtra(CONTACT_SERVICE, false)) {
            headerRightTxt.setVisibility(View.VISIBLE);
            headerRightTxt.setText("联系客服");
            headerRightTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogUtil.showCallDialog();
                }
            });
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

    private void getSkuItemBean(boolean isShowLoading) {
        if (skuItemBean == null && !TextUtils.isEmpty(goodsNo)) {
            isPerformClick = isShowLoading;
            RequestGoodsById request = new RequestGoodsById(activity, goodsNo);
            HttpRequestUtils.request(activity, request, this, isShowLoading);
        }
    }

    @OnClick({R.id.header_right_btn,R.id.goto_order})
    public void onClick(View view){
        HashMap<String,String> map = new HashMap<String,String>();
        switch (view.getId()){
            case R.id.header_right_btn:
                skuShare(UrlLibs.H5_DAIRY);
                break;
            case R.id.goto_order:

                Bundle bundle =new Bundle();

                if (cityBean != null) {
                    bundle.putSerializable("cityBean",cityBean);
                }
                bundle.putString("source",source);
//                bundle.putSerializable("collectGuideBean",collectGuideBean);
//                startFragment(new FgSkuSubmit(), source);
//                startFragment(new FgSkuNew(), bundle);

                Intent intent = new Intent(activity,OrderSelectCityActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void skuShare(final String shareUrl) {
        CommonUtils.shareDialog(activity,R.drawable.wxshare_img
                , getString(R.string.share_title), getString(R.string.share_content)
                , shareUrl
                , DailyWebInfoActivity.this.getClass().getSimpleName()
                , new ShareDialog.OnShareListener() {
                    @Override
                    public void onShare(int type) {
                        EventUtil.onShareDefaultEvent(StatisticConstant.SHARER, "" + type);
                    }
                });
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

}
