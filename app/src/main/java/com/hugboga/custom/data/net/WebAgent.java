package com.hugboga.custom.data.net;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionErrorCode;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.fragment.BaseFragment;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestWebInfo;
import com.hugboga.custom.fragment.FgLogin;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.widget.DialogUtil;

import org.json.JSONObject;

/**
 *
 * 请求代理模式
 * Created by admin on 2016/3/16.
 */
public class WebAgent implements HttpRequestListener {

    private Activity mActivity;
    private BaseFragment mFragment;
    private WebView mWebView;
    private DialogUtil dialog;

    public WebAgent(Activity activity,WebView webView){
        this.mActivity = activity;
        this.mWebView = webView;
        dialog = DialogUtil.getInstance(mActivity);
    }
    public WebAgent(BaseFragment fragment,WebView webView){
        this.mFragment = fragment;
        this.mWebView = webView;
        mActivity = fragment.getActivity();
        dialog = DialogUtil.getInstance(mActivity);
    }



    @JavascriptInterface
    public void redirect(final String url) {
        MLog.e("ZWebView-Redirect===>" + url);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(url);
            }
        });
    }

    @JavascriptInterface
    public void uploadPic(final String sequence, final String callback) {
    }


    @JavascriptInterface
    public void show(final String title, final String content, final String btnTxt, final String callBack) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.showCustomDialog(title, content, btnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callBack(callBack, "");
                    }
                }, null, null);
            }
        });

    }

    @JavascriptInterface
    public void setBackBtn(final String isBack) {
        if(mFragment!=null&&mFragment.getView()!=null){
            boolean isVisible =  Boolean.valueOf(isBack);
            mFragment.getView().findViewById(R.id.header_left_btn).setVisibility(isVisible? View.VISIBLE:View.GONE);
        }

    }



    @JavascriptInterface
    public void wxShare(final String picUrl, final String title, final String content, final String goUrl) {
        MLog.e("ZWebView-wxShare===>picUrl:" + picUrl + " title:" + title + " content:" + content + " goUrl:" + goUrl);
                // 调用分享操作
//                WXShareUtils.getInstance(getContext()).orderTwogo(picUrl, title, content, goUrl);
    }


    @JavascriptInterface
    public void backUrl() {
        MLog.e("ZWebView-backUrl===>canGoBack  "  );
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWebView != null&&mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }
        });

    }

    //新增方法

    @JavascriptInterface
    public void httpRequest(final String requestType, final String apiUrl, final String params, final String successFunction, final String failureFunction) {
        RequestWebInfo request = new RequestWebInfo(mActivity,apiUrl,requestType,params,successFunction,failureFunction);
        HttpRequestUtils.request(mActivity,request,this,null);
    }

    @JavascriptInterface
    public void finish() {
                if (mWebView != null) {
                    if(mFragment !=null){
                        mFragment.finish();
                    }else if(mActivity !=null){
                        mActivity.finish();
                    }
                }
    }
    @JavascriptInterface
    public void gotoLogin(final String jsonObj) {
        MLog.e("ZWebView-gotoLogin===>jsonObj:" + jsonObj);
        MLog.e("ZWebView-gotoLogin===>jsonObj:" + Uri.decode(jsonObj));

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Bundle bundle = new Bundle();
                String message = "请登录";
                try {
                    JSONObject json = new JSONObject(Uri.decode(jsonObj));
                    String countryCode = json.optString("countryCode");
                    String phone = json.optString("phone");
                    message = json.optString("message");
                    bundle.putString(FgLogin.KEY_AREA_CODE, countryCode);
                    bundle.putString(FgLogin.KEY_PHONE, phone);
                } catch (Exception e) {
                    MLog.e("gotoLogin", e);
                }
                dialog.showCustomDialog(message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mFragment != null) {
                            mFragment.startFragment(new FgLogin(),bundle);
                        }
                    }
                });
            }
        });

    }

    @JavascriptInterface
    public void callPhone(final String phone) {
        MLog.e("ZWebView-callPhone===>phone:" + phone);
        PhoneInfo.CallDial(mActivity, phone);
    }

    @JavascriptInterface
    public void getUserInfo(final String callBack) {
                //获取getGuideInfo，并回调
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", UserEntity.getUser().getUserId(mActivity));
                    jsonObject.put("name", UserEntity.getUser().getNickname(mActivity));
                    jsonObject.put("areacode", UserEntity.getUser().getAreaCode(mActivity));
                    jsonObject.put("phone", UserEntity.getUser().getPhone(mActivity));
                    callBack(callBack,jsonObject.toString());
                }catch (Exception e){
                    MLog.e("getUserInfo ",e);
                }
    }



    private void callBack(final String callBackMethod,final String callBackResult){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:" + callBackMethod + "(" + callBackResult + ")");
            }
        });

    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if(request instanceof RequestWebInfo){
            RequestWebInfo webInfoRequest = (RequestWebInfo)request;
            callBack(webInfoRequest.successCallBack, webInfoRequest.getData());
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if(errorInfo.state == ExceptionErrorCode.ERROR_CODE_SERVER){
            if(request instanceof RequestWebInfo){
                RequestWebInfo webInfoRequest = (RequestWebInfo)request;
                callBack(webInfoRequest.failCallBack, webInfoRequest.getData());
            }
        }else{
            new ErrorHandler(mActivity).onDataRequestError(errorInfo,request);
        }
    }
}
