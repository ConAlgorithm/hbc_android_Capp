package com.hugboga.custom.activity;


import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.net.WebAgent;
import com.hugboga.custom.data.request.RequestGoodsById;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.widget.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hugboga.custom.activity.WebInfoActivity.WEB_URL;


@ContentView(R.layout.fg_sku_detail)
public class SkuDetailActivity extends BaseActivity implements View.OnKeyListener  {

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


    public void initView() {
        isGoodsOut = false;
        findViewById(R.id.header_right_btn).setVisibility(WXShareUtils.getInstance(activity).isInstall(false) ? View.VISIBLE : View.VISIBLE);
        if (this.getIntent() != null) {
            skuItemBean = (SkuItemBean) getIntent().getSerializableExtra(WEB_SKU);
            cityBean = (CityBean) getIntent().getSerializableExtra(WEB_CITY);
            source = getIntent().getStringExtra("source");
            goodsNo = getIntent().getStringExtra(Constants.PARAMS_ID);
        }
        if (cityBean == null && skuItemBean != null && skuItemBean.arrCityId != 0) {
            cityBean = findCityById("" + skuItemBean.arrCityId);
        }
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
        String url = getIntent().getStringExtra(WEB_URL);
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    @Override
    public String getEventSource() {
        return "商品详情咨询客服";
    }

    private void getSkuItemBean(boolean isShowLoading) {
        if (skuItemBean == null && !TextUtils.isEmpty(goodsNo)) {
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }

    @OnClick({R.id.header_right_btn, R.id.goto_order})
    public void onClick(View view) {
        HashMap<String, String> map = new HashMap<String, String>();
        switch (view.getId()) {
            case R.id.header_right_btn:
                if (skuItemBean != null) {
                    String title = skuItemBean.goodsName;
                    String content = activity.getString(R.string.wx_share_content);
                    String shareUrl = skuItemBean.shareURL == null ? skuItemBean.skuDetailUrl : skuItemBean.shareURL;
                    shareUrl = shareUrl == null ? "http://www.huangbaoche.com" : shareUrl;
                    skuShare(skuItemBean.goodsPicture, title, content, shareUrl);
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
                Bundle bundle = new Bundle();
                bundle.putSerializable(SkuDetailActivity.WEB_SKU, skuItemBean);
                if (cityBean != null) {
                    bundle.putSerializable(SkuDetailActivity.WEB_CITY, cityBean);
                }
                bundle.putString("source", source);
//                startFragment(new FgSkuSubmit(), source);
//                startFragment(new FgSkuNew(), bundle);

                Intent intent = new Intent(activity,SkuNewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

//                if (cityBean != null) {
//                    map.put("routecity", cityBean.name);
//                }
                map.put("routename", skuItemBean.goodsName);
//                map.put("quoteprice", skuItemBean.goodsMinPrice);
                int countResult = 0;
                try {
                    countResult = Integer.parseInt(skuItemBean.goodsMinPrice);
                } catch (Exception e) {
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
        final String[] callItems = new String[]{"分享好友", "分享朋友圈"};
        callDialog.setItems(callItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WXShareUtils.getInstance(activity).share(which + 1, skuItemBean.goodsPicture, title, content, shareUrl);
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
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.cityBean = (CityBean) getIntent().getSerializableExtra("cityBean");
        setContentView(R.layout.fg_sku_detail);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
