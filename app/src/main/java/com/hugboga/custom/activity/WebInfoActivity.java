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
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.bean.UserSession;
import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.ShareInfoBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.WebAgent;
import com.hugboga.custom.data.request.RequestShareInfo;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import butterknife.BindView;

public class WebInfoActivity extends BaseActivity implements View.OnKeyListener {

    public static final String TAG = WebInfoActivity.class.getSimpleName();
    public static final String WEB_URL = "web_url";
    public static final String WEB_SHARE_BTN = "web_share_btn"; //是否动态控制展示分享按钮
    public static final String WEB_SHARE_NO = "web_share_no"; //动态控制分享的请求码
    public static final String CONTACT_SERVICE = "contact_service";
    public static final String IS_SHOW_TITLE_NAME = "is_show_title_name";

    public boolean isHttps = false;
    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.header_right_btn)
    ImageView headerRightBtn;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.header_right_txt)
    TextView headerRightTxt;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.webview_titlebar)
    RelativeLayout titlebar;

    private DialogUtil mDialogUtil;

    private CityBean cityBean;
    private boolean isLogin = false;
    private String url;
    private WebAgent webAgent;
    private String title;
    private boolean isShowTitleName = true;

    @Override
    public int getContentViewId() {
        return R.layout.fg_webview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.cityBean = (CityBean) getIntent().getSerializableExtra("cityBean");
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        setSensorsDefaultEvent();
        // 查询接口是否分享数据
        getShareInfo();
    }

    protected boolean isDefaultEvent() {
        return false;
    }

    public void setHeaderTitle(String _title) {
        if (headerTitle != null) {
            headerTitle.setText(_title);
            title = _title;
        }
    }

    WebViewClient webClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (TextUtils.isEmpty(title) && headerTitle != null && view != null && !TextUtils.isEmpty(view.getTitle())) {
                WebInfoActivity.this.title = view.getTitle();
                headerTitle.setText(view.getTitle());
                if (webAgent != null) {
                    webAgent.setTitle(view.getTitle());
                }
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!TextUtils.isEmpty(url)) {
                if (url.contains("/app/detail.html")) {
                    String goodsNo = CommonUtils.getUrlValue(url, "goodsNo");
                    if (TextUtils.isEmpty(goodsNo)) {
                        return false;
                    }
                    Intent intent = new Intent(WebInfoActivity.this, SkuDetailActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, url);
                    intent.putExtra(Constants.PARAMS_ID, goodsNo);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    WebInfoActivity.this.startActivity(intent);
                    return true;
                } else if (url.contains("tel:")) {
                    String mobile = url.substring(url.lastIndexOf("/") + 1);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                    WebInfoActivity.this.startActivity(intent);
                }
                WebInfoActivity.this.url = url;
            }
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (error.getPrimaryError() == SslError.SSL_INVALID) {
                handler.proceed();
            } else {
                handler.cancel();
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(final WebView view, String url) {
            MLog.e("WebResourceResponse1 =" + url);
            if (isHttps) {
                return processRequest(Uri.parse(url));
            } else {
                return null;
            }
        }

        @Override
        @TargetApi(21)
        public WebResourceResponse shouldInterceptRequest(final WebView view, WebResourceRequest interceptedRequest) {
            MLog.e("WebResourceResponse2 =" + interceptedRequest.getUrl());
            if (isHttps) {
                return processRequest(interceptedRequest.getUrl());
            } else {
                return null;
            }
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

    WebChromeClient webChromeClient = new WebChromeClient() {


        @Override
        public void onReceivedTitle(WebView view, String _title) {
            super.onReceivedTitle(view, _title);
            if (TextUtils.isEmpty(title) && headerTitle != null) {
                if (!view.getTitle().startsWith("http:") && !TextUtils.isEmpty(view.getTitle())) {
                    WebInfoActivity.this.title = view.getTitle();
                    headerTitle.setText(view.getTitle());
                    if (webAgent != null) {
                        webAgent.setTitle(view.getTitle());
                    }
                }
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


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mDialogUtil != null) {
                mDialogUtil.dismissDialog();
            }
            webView.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(final EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                setUrlUserId();
                addCookies();
                if (!TextUtils.isEmpty(url)) {
                    webView.loadUrl(url);
                }
                break;
            case CLICK_USER_LOOUT:
                removeAllCookies();
                break;
            case WEBINFO_REFRESH:
                if (!TextUtils.isEmpty(url)) {
                    webView.reload();
                }
                break;
            case SHOW_WEB_TITLE_BAR:
                String isShow = (String) action.getData();
                if (TextUtils.equals("0", isShow)) {
                    titlebar.setVisibility(View.VISIBLE);
                } else {
                    titlebar.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean _isLogin = UserEntity.getUser().isLogin(this);
        if (!TextUtils.isEmpty(url) && _isLogin && isLogin != _isLogin) {
            isLogin = true;
            setUrlUserId();
            Intent intent = new Intent(this, WebInfoActivity.class);
            intent.putExtra(WebInfoActivity.WEB_URL, url);
            startActivity(intent);
            finish();
        }
    }

    public void initHeader() {
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (this.getIntent().getBooleanExtra(CONTACT_SERVICE, false)) {
            RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(38), UIUtils.dip2px(38));
            headerRightImageParams.rightMargin = UIUtils.dip2px(18);
            headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
            headerRightBtn.setLayoutParams(headerRightImageParams);
            headerRightBtn.setPadding(0, 0, 0, 0);
            headerRightBtn.setImageResource(R.mipmap.topbar_cs);
            headerRightBtn.setVisibility(View.VISIBLE);
            headerRightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //DialogUtil.getInstance(activity).showCallDialog();
                    CommonUtils.csDialog(activity, null, "联系客服", null, UnicornServiceActivity.SourceType.TYPE_DEFAULT, getEventSource());
                }
            });
        }
    }

//    public void set

    public void initView() {
        if (getIntent() != null) {
            isShowTitleName = getIntent().getBooleanExtra(WebInfoActivity.IS_SHOW_TITLE_NAME, true);
        }
        if (!isShowTitleName) {
            headerTitle.setVisibility(View.INVISIBLE);
        }

        // 启用javaScript
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webAgent = new WebAgent(this, webView, cityBean, headerLeftBtn, TAG);
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
        initHeader();
        isLogin = UserEntity.getUser().isLogin(this);
        url = getIntent().getStringExtra(WEB_URL);
        setUrlUserId();
        addCookies();

        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
        MLog.e("url=" + url);
        SensorsUtils.setSensorsShowUpWebView(webView);
    }

    private void addCookies() {
        if (isLogin) {
            try {
                synCookies(url, "capp_user=" + webAgent.getUserInfoJson());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            removeAllCookies();
        }

        //开发者模式，设置特殊cookies
        CommonUtils.synDebugCookies(url);
    }

    @Override
    public String getEventSource() {
        return TextUtils.isEmpty(url) ? "web页面" : url;
    }

    public void synCookies(String url, String value) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, value);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.createInstance(MyApplication.getAppContext());
            CookieSyncManager.getInstance().sync();
        }
    }

    public void removeAllCookies() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeAllCookie();
        }
    }

    public void setUrlUserId() {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (isLogin) {
            String userId = UserEntity.getUser().getUserId(this);
            if (url.contains("userId=")) {
                url = CommonUtils.replaceUrlValue(url, "userId", userId);
            } else {
                url = CommonUtils.getBaseUrl(url) + "userId=" + userId;
            }
        }
    }

    /**
     * 获取分享数据信息
     */
    private void getShareInfo() {
        //获取分享需要参数WEB_SHARE_NO
        boolean isShareInfo = getIntent().getBooleanExtra(WEB_SHARE_BTN, false);
        String shareNo = getIntent().getStringExtra(WEB_SHARE_NO);
        if (isShareInfo && !TextUtils.isEmpty(shareNo)) {
            RequestShareInfo requestShareInfo = new RequestShareInfo(this, shareNo);
            HttpRequestUtils.request(this, requestShareInfo, this);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestShareInfo) {
            ShareInfoBean bean = (ShareInfoBean) request.getData();
            flushShareBtn(bean);
        }
    }

    /**
     * 动态设置分享方法处理
     *
     * @param bean
     */
    public void flushShareBtn(final ShareInfoBean bean) {
        if (bean.showShare == 1) {
            RelativeLayout.LayoutParams headerRightImageParams = new RelativeLayout.LayoutParams(UIUtils.dip2px(36), UIUtils.dip2px(36));
            headerRightImageParams.rightMargin = UIUtils.dip2px(12);
            headerRightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            headerRightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
            headerRightBtn.setLayoutParams(headerRightImageParams);
            headerRightBtn.setPadding(26, 26, 26, 26);
            headerRightBtn.setImageResource(R.mipmap.guide_homepage_share);
            headerRightBtn.setVisibility(View.VISIBLE);
            headerRightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.shareDialog(activity, bean.shareImageURL, bean.shareTitle, bean.shareContent, bean.shareURL);
                }
            });
        }
    }
}