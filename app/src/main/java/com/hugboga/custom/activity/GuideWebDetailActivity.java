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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CanServiceGuideBean;
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
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.ChannelUtils;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.widget.DialogUtil;
import com.hugboga.custom.widget.GuideWebDetailBottomView;
import com.hugboga.custom.widget.ShareDialog;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;

import butterknife.BindView;
import butterknife.OnClick;

public class GuideWebDetailActivity extends BaseActivity implements View.OnKeyListener {

    @BindView(R.id.guide_web_detail_titlebar)
    RelativeLayout titlebar;
    @BindView(R.id.titlebar_detail_right_1_btn)
    ImageView collectIV;
    @BindView(R.id.titlebar_detail_right_2_btn)
    ImageView shareIV;
    @BindView(R.id.titlebar_detail_title_tv)
    TextView titleTV;

    @BindView(R.id.guide_detail_webview)
    WebView webView;
    @BindView(R.id.guide_detail_bottom_view)
    GuideWebDetailBottomView bottomView;

    private DialogUtil mDialogUtil;
    private WebAgent webAgent;
    private Params paramsData;
    private GuideExtinfoBean guideExtinfoBean;
    boolean isFromHome;

    public static class Params implements Serializable {
        public String guideId;
        public boolean isChooseGuide = false;
        public String orderNo;
        public CanServiceGuideBean.GuidesBean chooseGuide;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_guide_web_detail;
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
        isFromHome = getIntent().getBooleanExtra("isFromHome", false);
        EventBus.getDefault().register(this);
        initView();
        setSensorsDefaultEvent();
    }

    protected boolean isDefaultEvent() {
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    public void sendRequest() {
        requestData(new RequestGuideExtinfo(GuideWebDetailActivity.this, paramsData.guideId), false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (bottomView != null) {
            bottomView.setStop(true);
        }
        setSensorsViewGuideEndEvent();
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                addCookies(getLoadUrl());
                loadUrl();
                if (!paramsData.isChooseGuide) {
                    sendRequest();
                }
                break;
            case CLICK_USER_LOOUT:
                CommonUtils.removeAllCookies();
                break;
            case SHOW_GUIDE_DETAIL_BAR:
                int isShow = (int) action.getData();
                if (isShow == 1) {
                    titlebar.setVisibility(View.VISIBLE);
                    if (UserEntity.getUser().isLogin(this)) {
                        bottomView.setVisibility(View.VISIBLE);
                    } else {
                        bottomView.setVisibility(View.GONE);
                    }
                } else {
                    titlebar.setVisibility(View.GONE);
                    bottomView.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void initView() {
        setSensorsViewGuideBeginEvent();

        titleTV.setText("");

        // 启用javaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
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

        addCookies(getLoadUrl());
        loadUrl();

        if (paramsData.isChooseGuide) {
            shareIV.setVisibility(View.GONE);
            collectIV.setVisibility(View.GONE);
            bottomView.showChooseGuideView(paramsData);
        } else {
            shareIV.setEnabled(false);
            collectIV.setEnabled(false);
            shareIV.setVisibility(View.VISIBLE);
            collectIV.setVisibility(View.VISIBLE);
        }

        bottomView.getBookTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsUtils.onAppClick(getEventSource(), getString(R.string.guide_list_bottom_button), getIntentSource());
                webAgent.callBack("goToNext", "");
            }
        });

        sendRequest();
    }

    private void addCookies(String _url) {
        if (UserEntity.getUser().isLogin(this)) {
            try {
                CommonUtils.synCookies(_url, "capp_user=" + webAgent.getUserInfoJson());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CommonUtils.removeAllCookies();
        }

        //开发者模式，设置特殊cookies
        CommonUtils.synDebugCookies(_url);
    }

    public String getLoadUrl() {
        String isCanService = !paramsData.isChooseGuide ? "1" : "0";
        String url = UrlLibs.H5_GUIDE_DETAIL + "guideId=" + paramsData.guideId + "&canService=" + isCanService;
        return url;
    }

    public void loadUrl() {
        String url = getLoadUrl();
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
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
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            addCookies(url);
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
            return null;
        }

        @Override
        @TargetApi(21)
        public WebResourceResponse shouldInterceptRequest(final WebView view, WebResourceRequest interceptedRequest) {
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
            titleTV.setText(guideExtinfoBean.guideName);
            if (paramsData.isChooseGuide) {
                return;
            }
            shareIV.setEnabled(true);
            collectIV.setEnabled(true);
            bottomView.update(guideExtinfoBean);
            if (guideExtinfoBean.isCollected != null) {
                collectIV.setSelected(guideExtinfoBean.isCollected == 1);
            }
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
            SensorsUtils.setSensorsFavorite("司导", "", guideExtinfoBean.guideId);
        }
    }

    @OnClick({R.id.titlebar_detail_back_btn})
    public void onBack() {
        finish();
    }

    @OnClick({R.id.titlebar_detail_right_1_btn})
    public void onCollect() {
        if (guideExtinfoBean == null || !CommonUtils.isLogin(GuideWebDetailActivity.this, getEventSource())) {
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
        SensorsUtils.onAppClick(getEventSource(), "收藏", getIntentSource());
    }

    @OnClick({R.id.titlebar_detail_right_2_btn})
    public void onShare() {
        if (guideExtinfoBean == null) {
            return;
        }
        SensorsUtils.onAppClick(getEventSource(), "分享", getIntentSource());
        String title = String.format("去%1$s，推荐你找当地华人司导%2$s开车带你玩！", guideExtinfoBean.cityName, guideExtinfoBean.guideName);
        String desc = TextUtils.isEmpty(guideExtinfoBean.homeDesc) ? "我可以为您规划行程、陪同翻译和向导，让您舒舒服服坐车玩！" : guideExtinfoBean.homeDesc;
        CommonUtils.shareDialog(this, guideExtinfoBean.avatarUrl, title, desc, getLoadUrl(),
                GuideWebDetailActivity.class.getSimpleName(),
                new ShareDialog.OnShareListener() {
                    @Override
                    public void onShare(int type) {
                        StatisticClickEvent.clickShare(StatisticConstant.SHAREG_TYPE, type == 1 ? "微信好友" : "朋友圈");
                        SensorsUtils.setSensorsShareEvent(type == 1 ? "微信好友" : "朋友圈", "司导", null, paramsData.guideId);
                    }
                });
    }

    @Override
    public String getEventSource() {
        return "司导个人页";
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_GPROFILE;
    }

    private void setSensorsViewGuideBeginEvent() {
        try {
            SensorsDataAPI.sharedInstance(this).trackTimerBegin("viewGuide");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //神策统计_浏览司导详情
    private void setSensorsViewGuideEndEvent() {
        if (guideExtinfoBean == null) {
            return;
        }
        try {
            JSONObject properties = new JSONObject();
            properties.put("refer", getIntentSource());
            properties.put("guideId", guideExtinfoBean.guideId);
            properties.put("cityName", guideExtinfoBean.cityName);
            properties.put("cityId", guideExtinfoBean.cityId);
            SensorsDataAPI.sharedInstance(this).trackTimerEnd("viewGuide", properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getWeburl(){
        return webView.getUrl();
    }
}
