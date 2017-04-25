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
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.GuideExtinfoBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.net.WebAgent;
import com.hugboga.custom.data.request.RequestCollectGuidesId;
import com.hugboga.custom.data.request.RequestGuideExtinfo;
import com.hugboga.custom.data.request.RequestUncollectGuidesId;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.GuideWebDetailBottomView;
import com.hugboga.custom.widget.ShareDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideWebDetailActivity extends BaseActivity implements View.OnKeyListener{

    public final static String PARAM_GUIDE_BEAN = "guidesDetailData";

    @Bind(R.id.titlebar_detail_right_1_btn)
    ImageView collectIV;
    @Bind(R.id.titlebar_detail_right_2_btn)
    ImageView shareIV;
    @Bind(R.id.titlebar_detail_title_tv)
    TextView titleTV;

    @Bind(R.id.guide_detail_webview)
    WebView webView;
    @Bind(R.id.guide_detail_bottom_view)
    GuideWebDetailBottomView bottomView;

    private DialogUtil mDialogUtil;
    private WebAgent webAgent;
    private Params paramsData;
    private GuideExtinfoBean guideExtinfoBean;

    public static class Params implements Serializable {
        public String guideId;
        public boolean canService = true;
        public boolean canCollect = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (GuideWebDetailActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (GuideWebDetailActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.activity_guide_web_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        if (bottomView != null) {
            bottomView.setStop(true);
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                loadUrl();
                break;
        }
    }

    public void initView() {
        titleTV.setText("精选司导");
        if (paramsData.canCollect) {
            shareIV.setEnabled(false);
            collectIV.setEnabled(false);
            shareIV.setVisibility(View.VISIBLE);
            collectIV.setVisibility(View.VISIBLE);
        } else {
            shareIV.setVisibility(View.GONE);
            collectIV.setVisibility(View.GONE);
        }

        // 启用javaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webAgent = new WebAgent(this, webView, null, null, null);
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

        loadUrl();

        requestData(new RequestGuideExtinfo(GuideWebDetailActivity.this, paramsData.guideId), false);

    }

    public String loadUrl() {
        String isCanService = "0";
        if (UserEntity.getUser().isLogin(this)) {
            isCanService = paramsData.canService ? "1" : "0";
        }
        String url = UrlLibs.H5_GUIDE_DETAIL + "guideId=" + paramsData.guideId + "&canService=" + isCanService;
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
        return url;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }

    WebChromeClient webChromeClient = new WebChromeClient() {


        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
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
//            if (!SkuDetailActivity.this.isFinishing()) {
//                gotoOrder.setVisibility(View.VISIBLE);
//            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
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
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestGuideExtinfo) {
            guideExtinfoBean = ((RequestGuideExtinfo) _request).getData();
            if (paramsData.canService) {
                bottomView.update(guideExtinfoBean);
            } else {
                bottomView.setVisibility(View.GONE);
            }
            shareIV.setEnabled(true);
            collectIV.setEnabled(true);
            if (guideExtinfoBean.isCollected != null) {
                collectIV.setSelected(guideExtinfoBean.isCollected == 1);
            }
            bottomView.setVisibility(View.VISIBLE);
        } else if (_request instanceof RequestUncollectGuidesId) {//取消收藏
            guideExtinfoBean.isCollected = 0;
            collectIV.setSelected(false);
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_COLLECT, 0));
            CommonUtils.showToast(getString(R.string.collect_cancel));
        } else if (_request instanceof RequestCollectGuidesId) {//收藏
            guideExtinfoBean.isCollected = 1;
            collectIV.setSelected(true);
            EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_COLLECT, 1));
            CommonUtils.showToast(getString(R.string.collect_succeed));
        }
    }

    @OnClick({R.id.titlebar_detail_back_btn})
    public void onBack() {
        finish();
    }

    @OnClick({R.id.titlebar_detail_right_1_btn})
    public void onCollect() {
        if (guideExtinfoBean == null || !CommonUtils.isLogin(GuideWebDetailActivity.this)) {
            return;
        }
        EventUtil.onDefaultEvent(StatisticConstant.COLLECTG, getEventSource());
        mDialogUtil.showLoadingDialog();
        BaseRequest baseRequest = null;
        if (guideExtinfoBean.isCollected == 1) {
            baseRequest = new RequestUncollectGuidesId(this, guideExtinfoBean.guideId);
        } else {
            baseRequest = new RequestCollectGuidesId(this, guideExtinfoBean.guideId);
        }
        requestData(baseRequest);
    }

    @OnClick({R.id.titlebar_detail_right_2_btn})
    public void onShare() {
        if (guideExtinfoBean == null) {
            return;
        }
        String title = String.format("去%1$s，推荐你找当地华人司导%2$s开车带你玩！", guideExtinfoBean.cityName, guideExtinfoBean.guideName);
        String desc = TextUtils.isEmpty(guideExtinfoBean.homeDesc) ? "我可以为您规划行程、陪同翻译和向导，让您舒舒服服坐车玩！" : guideExtinfoBean.homeDesc;
        CommonUtils.shareDialog(this, guideExtinfoBean.avatarUrl, title, desc, loadUrl(),
                GuideWebDetailActivity.class.getSimpleName(),
                new ShareDialog.OnShareListener() {
                    @Override
                    public void onShare(int type) {
                        StatisticClickEvent.clickShare(StatisticConstant.SHAREG_TYPE, type == 1 ? "微信好友" : "朋友圈");
                    }
                });
    }

}
