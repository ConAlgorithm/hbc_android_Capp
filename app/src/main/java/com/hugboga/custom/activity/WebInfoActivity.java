package com.hugboga.custom.activity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
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
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.net.WebAgent;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.widget.DialogUtil;

import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebInfoActivity extends BaseActivity implements View.OnKeyListener {

    public static final String WEB_URL = "web_url";
    public static final String CONTACT_SERVICE = "contact_service";

    public boolean isHttps = false;
    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;
    @Bind(R.id.header_right_txt)
    TextView headerRightTxt;
    @Bind(R.id.webview)
    WebView webView;
    private DialogUtil mDialogUtil;

    private CityBean cityBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.cityBean = getIntent().getParcelableExtra("cityBean");
        setContentView(R.layout.fg_webview);
        ButterKnife.bind(this);
        initView();
    }


    WebViewClient webClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            fgTitle.setText(view.getTitle());
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
        webView.destroy();
        super.onDestroy();
        ButterKnife.unbind(this);
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
            headerTitle.setVisibility(View.VISIBLE);
            headerTitle.setText("联系客服");
            headerTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogUtil.showCallDialog();
                }
            });
        }
    }

    public void initView() {
        // 启用javaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.addJavascriptInterface(new WebAgent(this, webView, cityBean), "javaObj");
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

}