package com.hugboga.custom.data.net;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.huangbaoche.hbcframe.data.net.ErrorHandler;
import com.huangbaoche.hbcframe.data.net.ExceptionErrorCode;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.parser.ServerParser;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.fragment.BaseFragment;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.CurrentServerInfoData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.data.request.RequestCurrentServerInfo;
import com.hugboga.custom.data.request.RequestWebInfo;
import com.hugboga.custom.fragment.FgActivity;
import com.hugboga.custom.fragment.FgLogin;
import com.hugboga.custom.fragment.FgOrderSelectCity;
import com.hugboga.custom.fragment.FgSkuDetail;
import com.hugboga.custom.fragment.FgWebInfo;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.widget.DialogUtil;

import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 请求代理模式
 * Created by admin on 2016/3/16.
 */
public class WebAgent implements HttpRequestListener {

    private Activity mActivity;
    private BaseFragment mFragment;
    private WebView mWebView;
    private DialogUtil dialog;

    public WebAgent(Activity activity, WebView webView) {
        this.mActivity = activity;
        this.mWebView = webView;
        dialog = DialogUtil.getInstance(mActivity);
    }

    public WebAgent(BaseFragment fragment, WebView webView) {
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
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mFragment != null && mFragment.getView() != null) {
                    boolean isVisible = Boolean.valueOf(isBack);
                    mFragment.getView().findViewById(R.id.header_left_btn).setVisibility(isVisible ? View.VISIBLE : View.GONE);
                    View backBtn = mFragment.getView().findViewById(R.id.header_left_btn);
                    if (backBtn != null) {
                        backBtn.setVisibility(isVisible ? View.VISIBLE : View.GONE);
                    }
                }
            }
        });
    }


    @JavascriptInterface
    public void wxShare(final String picUrl, final String title, final String content, final String goUrl) {
        MLog.e("ZWebView-wxShare===>picUrl:" + picUrl + " title:" + title + " content:" + content + " goUrl:" + goUrl);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 调用分享操作
                WXShareUtils.getInstance(mActivity).share(0, picUrl, title, content, goUrl);
            }
        });
    }

    @JavascriptInterface
    public void wxShareByType(final int type, final String picUrl, final String title, final String content, final String goUrl) {
        MLog.e("ZWebView-wxShare===>picUrl:" + picUrl + " title:" + title + " content:" + content + " goUrl:" + goUrl);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 调用分享操作
                WXShareUtils.getInstance(mActivity).share(type, picUrl, title, content, goUrl);
            }
        });
    }


    @JavascriptInterface
    public void backUrl() {
        MLog.e("ZWebView-backUrl===>canGoBack  ");
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWebView != null && mWebView.canGoBack()) {
                    mWebView.goBack();
                }
            }
        });

    }

    //新增方法

    @JavascriptInterface
    public void httpRequest(final String requestType, final String apiUrl, final String params, final String successFunction, final String failureFunction) {
        MLog.e("ZWebView-wxShare===>requestType:" + requestType + " apiUrl:" + apiUrl + " params:" + params + " successFunction:" + successFunction + " failureFunction:" + failureFunction);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestWebInfo request = new RequestWebInfo(mActivity, apiUrl, requestType, params, successFunction, failureFunction);
                HttpRequestUtils.request(mActivity, request, WebAgent.this);
            }
        });
    }

    @JavascriptInterface
    public void finish() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mWebView != null) {
                    if (mFragment != null) {
                        mFragment.finish();
                    } else if (mActivity != null) {
                        mActivity.finish();
                    }
                }
            }
        });
    }

    @JavascriptInterface
    public void showLogin() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mFragment.startFragment(new FgLogin());
            }
        });
    }

    @JavascriptInterface
    public String getUserId(){
        return  UserEntity.getUser().getUserId(mActivity);
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
                            mFragment.startFragment(new FgLogin(), bundle);
                        }
                    }
                });
            }
        });
    }

    @JavascriptInterface
    public void callPhone(final String phone) {
        MLog.e("ZWebView-callPhone===>phone:" + phone);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PhoneInfo.CallDial(mActivity, phone);
            }
        });
    }

    @JavascriptInterface
    public void getUserInfo(final String callBack) {
        //获取getUserInfo，并回调
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", UserEntity.getUser().getUserId(mActivity));
            jsonObject.put("ut", UserEntity.getUser().getUserToken(mActivity));
            jsonObject.put("name", UserEntity.getUser().getNickname(mActivity));
            jsonObject.put("areacode", UserEntity.getUser().getAreaCode(mActivity));
            jsonObject.put("phone", UserEntity.getUser().getPhone(mActivity));
            callBack(callBack, jsonObject.toString());
        } catch (Exception e) {
            MLog.e("getUserInfo ", e);
        }
    }

    private String getChatInfo(String userId, String userAvatar, String title, String targetType) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.isChat = true;
        chatInfo.userId = userId;
        chatInfo.userAvatar = userAvatar;
        chatInfo.title = title;
        chatInfo.targetType = targetType;
        chatInfo.isHideMoreBtn = 1;
        return new ParserChatInfo().toJsonString(chatInfo);
    }

    /**
     * 在线咨询客服
     */
    @JavascriptInterface
    public void pushToServiceChatVC() {
        if (mActivity != null && !UserEntity.getUser().isLogin(mActivity) && mFragment != null) {
            CommonUtils.showToast(R.string.login_hint);
            Bundle bundle = new Bundle();
            ;
            mFragment.startFragment(new FgLogin(), bundle);
            return;
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RequestCurrentServerInfo request = new RequestCurrentServerInfo(mActivity);
                HttpRequestUtils.request(mActivity, request, WebAgent.this);
            }
        });
    }

    /**
     * 拨打客服热线
     */
    @JavascriptInterface
    public void callServicePhone() {
        if (mActivity != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogUtil mDialogUtil = DialogUtil.getInstance(mActivity);
                    mDialogUtil.showCallDialog();
                }
            });
        }
    }

    /**
     * 立即定制（定制线路下单）
     */
    @JavascriptInterface
    public void customLineOrder() {
        if (mFragment != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFragment.startFragment(new FgOrderSelectCity());
                }
            });
        }
    }

    /**
     * 现在预订（固定线路下单）
     */
    @JavascriptInterface
    public void fixedLineOrder() {
        if (mFragment != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mFragment.startFragment(new FgOrderSelectCity());
                }
            });
        }
    }

    /**
     * URL重定向
     */
    @JavascriptInterface
    public void pushToNextPageWithUrl(final String url) {
        if (mFragment != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bundle bundle = new Bundle();
                    bundle.putString(FgWebInfo.WEB_URL, url);
                    mFragment.startFragment(new FgActivity(), bundle);
                }
            });
        }
    }

    /**
     * 设置title
     */
    @JavascriptInterface
    public void setWebTitle(final String title) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(title) && mFragment instanceof FgWebInfo) {
                    FgWebInfo fgWebInfo = ((FgWebInfo) mFragment);
                    fgWebInfo.setTitle(title);
                }
            }
        });
    }

    /**
     * 商品下架（不能分享）
     */
    @JavascriptInterface
    public void goodsHadOutOfStock() {
        if (mFragment instanceof FgSkuDetail) {
            FgSkuDetail fgSkuDetail = ((FgSkuDetail) mFragment);
            fgSkuDetail.isGoodsOut = true;
        }
    }

    private void callBack(final String callBackMethod, final String callBackResult) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:" + callBackMethod + "(" + callBackResult + ")");
            }
        });

    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestWebInfo) {
            RequestWebInfo webInfoRequest = (RequestWebInfo) _request;
            callBack(webInfoRequest.successCallBack, webInfoRequest.getData());
        } else if (_request instanceof RequestCurrentServerInfo) {
            RequestCurrentServerInfo request = (RequestCurrentServerInfo) _request;
            CurrentServerInfoData data = (CurrentServerInfoData) request.getData();
            if (data != null) {
                String titleJson = getChatInfo(data.userId, data.avatar, data.name, "0");
                RongIM.getInstance().startConversation(mActivity, Conversation.ConversationType.APP_PUBLIC_SERVICE, data.userId, titleJson);
            }
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        if (errorInfo.state == ExceptionErrorCode.ERROR_CODE_SERVER) {
            if (request instanceof RequestWebInfo) {
                RequestWebInfo webInfoRequest = (RequestWebInfo) request;
                String errorInfoJson = new ServerParser().errorInfoToStr(errorInfo);
                callBack(webInfoRequest.failCallBack, errorInfoJson);
            }
        } else {
            new ErrorHandler(mActivity, this).onDataRequestError(errorInfo, request);
        }
    }
}
