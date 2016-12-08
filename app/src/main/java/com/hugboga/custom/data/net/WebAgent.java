package com.hugboga.custom.data.net;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
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
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.WXShareUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.action.ActionController;
import com.hugboga.custom.action.data.ActionBean;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.CityHomeListActivity;
import com.hugboga.custom.activity.DailyWebInfoActivity;
import com.hugboga.custom.activity.LoginActivity;
import com.hugboga.custom.activity.OrderSelectCityActivity;
import com.hugboga.custom.activity.PickSendActivity;
import com.hugboga.custom.activity.ServiceQuestionActivity;
import com.hugboga.custom.activity.SingleNewActivity;
import com.hugboga.custom.activity.SkuDetailActivity;
import com.hugboga.custom.activity.UnicornServiceActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.ChatInfo;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.parser.ParserChatInfo;
import com.hugboga.custom.data.request.RequestWebInfo;
import com.hugboga.custom.statistic.event.EventUtil;
import com.hugboga.custom.utils.ApiReportHelper;
import com.hugboga.custom.utils.CommonUtils;
import com.hugboga.custom.utils.JsonUtils;
import com.hugboga.custom.utils.PhoneInfo;
import com.hugboga.custom.utils.SaveFileTask;
import com.hugboga.custom.widget.DialogUtil;

import org.json.JSONObject;

import java.io.File;


/**
 * 请求代理模式
 * Created by admin on 2016/3/16.
 */
public class WebAgent implements HttpRequestListener {

    private Activity mActivity;
    private WebView mWebView;
    private DialogUtil dialog;
    private CityBean cityBean;
    private View leftBtn;

    private String agentType = "";
    private SkuItemBean skuItemBean;

    public WebAgent(Activity activity, WebView webView, CityBean cityBean, View leftBtn, String agentType) {
        this.mActivity = activity;
        this.mWebView = webView;
        dialog = DialogUtil.getInstance(mActivity);
        this.cityBean = cityBean;
        this.leftBtn = leftBtn;
        this.agentType = agentType;
    }

    public void setCityBean(CityBean cityBean) {
        this.cityBean = cityBean;
    }

    public void setSkuItemBean(SkuItemBean skuItemBean) {
        this.skuItemBean = skuItemBean;
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
                if (leftBtn != null) {
                    boolean isVisible = Boolean.valueOf(isBack);
                    leftBtn.setVisibility(isVisible ? View.VISIBLE : View.GONE);
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

    @JavascriptInterface
    public void doAction(final String action) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(action)) {
                    ActionBean actionBean = (ActionBean) JsonUtils.fromJson(action, ActionBean.class);
                    if (actionBean != null) {
                        ActionController actionFactory = ActionController.getInstance(mActivity);
                        actionFactory.doAction(actionBean);
                    }
                }

            }
        });

    }

    /**
     * 线路详情empty
     * */
    @JavascriptInterface
    public void showGoodsError() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity instanceof SkuDetailActivity) {
                    ((SkuDetailActivity) mActivity).goodsSoldOut();
                }
            }
        });
    }

    /**
     * areaID:城市ID
     * areaName：城市名称
     * areaType：1:city、2:country、3:group
     */
    @JavascriptInterface
    public void pushToGoodList(final String areaID,final String areaName, final String areaType) {
        if (TextUtils.isEmpty(areaID)) {
            return;
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mActivity, CityHomeListActivity.class);
                CityHomeListActivity.Params params = new CityHomeListActivity.Params();
                params.id = CommonUtils.getCountInteger(areaID);
                switch (CommonUtils.getCountInteger(areaType)) {
                    case 1:
                        params.cityHomeType = CityHomeListActivity.CityHomeType.CITY;
                        break;
                    case 2:
                        params.cityHomeType = CityHomeListActivity.CityHomeType.COUNTRY;
                        break;
                    case 3:
                        params.cityHomeType = CityHomeListActivity.CityHomeType.ROUTE;
                        break;
                    default:
                        return;
                }
                intent.putExtra(Constants.PARAMS_DATA, params);
                intent.putExtra("isHomeIn", false);
                mActivity.startActivity(intent);
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
                    if (mActivity != null) {
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
                Intent intent = new Intent(mActivity, LoginActivity.class);
                intent.putExtra("source",((BaseActivity)mActivity).getIntentSource());
                mActivity.startActivity(intent);
            }
        });
    }

    @JavascriptInterface
    public void getUserId(String callBack){
        //获取getUserInfo，并回调
        try {
            callBack(callBack, UserEntity.getUser().getUserId(mActivity));
        } catch (Exception e) {
            MLog.e("getUserInfo ", e);
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
                    bundle.putString(LoginActivity.KEY_AREA_CODE, countryCode);
                    bundle.putString(LoginActivity.KEY_PHONE, phone);
                    if (mActivity instanceof BaseActivity) {
                        bundle.putString(Constants.PARAMS_SOURCE, ((BaseActivity)mActivity).getEventSource());
                    }
                } catch (Exception e) {
                    MLog.e("gotoLogin", e);
                }
                dialog.showCustomDialog(message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mActivity, LoginActivity.class);
                        intent.putExtras(bundle);
                        mActivity.startActivity(intent);
                    }
                });
            }
        });
    }

    /**
     * 自定义包车下单
     * */
    @JavascriptInterface
    public void pushToDailyOrder(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    Intent intent = new Intent(mActivity,OrderSelectCityActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, "商品详情");
                    intent.putExtra(Constants.PARAMS_SOURCE_DETAIL, EventUtil.getInstance().sourceDetail);
                    if (cityBean != null) {
                        intent.putExtra("cityBean", cityBean);
                    }
                    mActivity.startActivity(intent);
//                    mFragment.startFragment(fgOrderSelectCity,bundle);
            }
        });

    }

    /**
     * 接送机下单
     * */
    @JavascriptInterface
    public void pushToAirportOrder() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mActivity, PickSendActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_DAIRY);
                mActivity.startActivity(intent);
            }
        });
    }

    /**
     * 单次接送下单
     * */
    @JavascriptInterface
    public void pushToSingleOrder() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mActivity, SingleNewActivity.class);
                mActivity.startActivity(intent);
            }
        });
    }

    /**
     * 城市列表
     * */
    @JavascriptInterface
    public void pushToGoodsOrder(final String cityId) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CityHomeListActivity.Params params = new CityHomeListActivity.Params();
                params.id = CommonUtils.getCountInteger(cityId);
                params.cityHomeType = CityHomeListActivity.CityHomeType.CITY;
                Intent intent = new Intent(mActivity, CityHomeListActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                mActivity.startActivity(intent);
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

    @JavascriptInterface
    public void getUserLoginStatus(final String callBack) {
        try {
            callBack(callBack, UserEntity.getUser().isLogin(mActivity) ? "1" : "0");
        } catch (Exception e) {
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
        if (mActivity != null && !UserEntity.getUser().isLogin(mActivity)) {
            CommonUtils.showToast(R.string.login_hint);
            Intent intent= new Intent(mActivity, LoginActivity.class);
            intent.putExtra("source","商品详情咨询客服");
            mActivity.startActivity(intent);
            return;
        }
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mActivity, ServiceQuestionActivity.class);
                UnicornServiceActivity.Params params = new UnicornServiceActivity.Params();
                if (agentType.equals(DailyWebInfoActivity.TAG)) {
                    params.sourceType = UnicornServiceActivity.SourceType.TYPE_CHARTERED;
                } else if (agentType.equals(SkuDetailActivity.TAG) && skuItemBean != null) {
                    params.sourceType = UnicornServiceActivity.SourceType.TYPE_LINE;
                    params.skuItemBean = skuItemBean;
                } else {
                    params.sourceType = UnicornServiceActivity.SourceType.TYPE_DEFAULT;
                }
                intent.putExtra(Constants.PARAMS_DATA, params);
                mActivity.startActivity(intent);

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
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mActivity,OrderSelectCityActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, "商品详情");
                if (cityBean != null) {
                    intent.putExtra("cityBean", cityBean);
                }
                mActivity.startActivity(intent);
            }
        });
    }

    /**
     * 现在预订（固定线路下单）
     */
    @JavascriptInterface
    public void fixedLineOrder() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mActivity,OrderSelectCityActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, "商品详情");
                if (cityBean != null) {
                    intent.putExtra("cityBean", cityBean);
                }
                mActivity.startActivity(intent);
            }
        });
    }

    /**
     * URL重定向
     */
    @JavascriptInterface
    public void pushToNextPageWithUrl(final String url) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                    Bundle bundle = new Bundle();
//                    bundle.putString(FgWebInfo.WEB_URL, url);
//                    mFragment.startFragment(new FgActivity(), bundle);

                Intent intent = new Intent(mActivity, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, url);
                mActivity.startActivity(intent);
            }
        });
    }

    /**
     * 设置title
     */
    @JavascriptInterface
    public void setWebTitle(final String title) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(title) && mActivity instanceof WebInfoActivity) {
                    WebInfoActivity fgWebInfo = ((WebInfoActivity) mActivity);
                    fgWebInfo.setTitle(title);
                    fgWebInfo.setHeaderTitle(title);
                }
            }
        });
    }

    /**
     * 商品下架（不能分享）
     */
    @JavascriptInterface
    public void goodsHadOutOfStock() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity instanceof SkuDetailActivity) {
                    SkuDetailActivity fgSkuDetail = ((SkuDetailActivity) mActivity);
                    fgSkuDetail.setGoodsOut();
                }
            }
        });
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
        ApiReportHelper.getInstance().addReport(_request);
        if (_request instanceof RequestWebInfo) {
            RequestWebInfo webInfoRequest = (RequestWebInfo) _request;
            callBack(webInfoRequest.successCallBack, webInfoRequest.getData());
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

    /**
     * 长按下载图片
     */
    @JavascriptInterface
    public void saveImageToNative(final String url) {
        String [] str = {"查看大图", "保存图片"};
        AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setItems(str,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            CommonUtils.showLargerImage(mActivity, url);
                        } else {
                            SaveFileTask saveImageTask = new SaveFileTask(mActivity, new SaveFileTask.FileDownLoadCallBack() {
                                @Override
                                public void onDownLoadSuccess(File file) {
                                     CommonUtils.showToast("图片保存成功");
                                }
                                @Override
                                public void onDownLoadFailed() {

                                }
                            });
                            saveImageTask.execute(url);
                        }
                    }
                }).create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
