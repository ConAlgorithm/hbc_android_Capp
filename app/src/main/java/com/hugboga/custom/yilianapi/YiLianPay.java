package com.hugboga.custom.yilianapi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.huangbaoche.hbcframe.HbcApplication;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.PhoneInfo;
import com.hugboga.custom.BuildConfig;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.activity.BaseActivity;
import com.hugboga.custom.activity.ChoosePaymentActivity;
import com.hugboga.custom.activity.PayResultActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.YiLianPayBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.utils.JsonUtils;
import com.payeco.android.plugin.PayecoPluginPayCallBack;
import com.payeco.android.plugin.PayecoPluginPayIn;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale;
import static android.support.v4.content.ContextCompat.checkSelfPermission;


/**
 * Created by Administrator on 2017/3/10.
 */

public class YiLianPay {
    //商户服务器下单地址，此地址为商户平台测试环境下单地址，商户接入需改为商户自己的服务器下单地址
//    private final static String URL_PAY_ORDER= UrlLibs.API_CREDIT_PAY;
//    private final static String URL_PAY_ORDER = "https://api5-dev.huangbaoche.com/trade/v1.0/c/yilian/pay";

    //模拟通知商户地址，建议在接收到支付成功结果时，通知商户服务器
    private final static String URL_PAY_NOTIFY="http://10.123.54.7:8080/Notify.do";

    // 00: 测试环境 01: 生产环境
    private final static String ENVIRONMENT = "00";

    private ProgressDialog dia;

    private Context payContext;
    private Activity payActivtiy;
    private String orderNo;//订单号
    private int orderType;
    private String actualPrice;//实际支付金额
    private String coupId;//劵id
    private String cardId;//卡id
    YiLianPayBean yiLianPayBean;
    private ChoosePaymentActivity.RequestParams choosePaymentParams;

    public YiLianPay(Context context, Activity activity, YiLianPayBean yiLianPayBean,String orderNo,int orderType, ChoosePaymentActivity.RequestParams params){
        this.payContext = context;
        this.payActivtiy = activity;
        this.yiLianPayBean = yiLianPayBean;
        this.orderNo = orderNo;
        this.orderType = orderType;
        this.choosePaymentParams = params;
    }

    public void pay(){

        dia = new ProgressDialog(payContext);

        //Android 6.0 请求权限
        requestNeedPermissions();

        //使用异步通讯
        new AsyncTask<Void, Void ,String>(){

            @Override
            protected void onPreExecute() {
                dia.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                String respString = null;

                JSONObject json = new JSONObject();
                try {
                    json.put("Version",yiLianPayBean.version);
                    json.put("MerchOrderId",yiLianPayBean.merchOrderId);
                    json.put("MerchantId", yiLianPayBean.merchantId);
                    json.put("Amount", yiLianPayBean.amount);
                    json.put("TradeTime", yiLianPayBean.tradeTime);
                    json.put("OrderId", yiLianPayBean.orderId);
                    json.put("Sign", yiLianPayBean.sign);
                    respString = json.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return respString;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dia.dismiss();

                if (s == null){
                    MLog.d("从数据获取数据失败");
                    return;
                }

                try {
                    JSONObject json = new JSONObject(s);

                    //组织请求参数
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("Environment", BuildConfig.CREDIT_CARD_PAY_ENVIRONMENT); // 00: 测试环境 01: 生产环境
                    params.put("upPay.Req", json.toString());
                    params.put("thePackageName", PhoneInfo.getPackageName()); //提交包名

                    PayecoPluginPayIn.doPay(payActivtiy, params, new PayecoPluginPayCallBack() {
                        @SuppressLint("ShowToast")
                        @Override
                        public void callBack(String result, String errCode, String errMsg) {
                            if (errCode != null) {

                                Log.e("test", "errCode:" + errCode);
                                Log.e("test", "errMsg:" + errMsg);
                                MLog.e("支付结果+++++++："+result);

                                //支付操作发错错误
                                Toast.makeText(payContext,
                                        String.format("发生异常，错误码：%s，错误描述：%s", errCode, errMsg),
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            //判断是否是用户主动退出
                            //返回报文为：{"respDesc":"用户主动退出插件","respCode":"W101"}
                            try {
                                Intent intent = new Intent(payContext, PayResultActivity.class);
                                PayResultActivity.Params params1 = new PayResultActivity.Params();
                                params1.orderId = orderNo;
                                params1.orderType = orderType;

                                params1.apiType = choosePaymentParams.apiType;
                                params1.couponPhone = choosePaymentParams.couponPhone;
                                params1.couponAreaCode = choosePaymentParams.couponAreaCode;

                                JSONObject obj = new JSONObject(result);
                                String code = obj.getString("respCode");
                                String msg = obj.getString("respDesc");
                                if("W101".equals(code)){
                                    Toast.makeText(payContext, msg, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (!"0000".equals(code)) { //非0000，订单支付响应异常
                                    if (!TextUtils.isEmpty(JsonUtils.getJsonStr(payContext, "yilianErrorCode.json"))) {
                                        JSONObject jsonObject = new JSONObject(JsonUtils.getJsonStr(payContext, "yilianErrorCode.json"));
                                        if (jsonObject.has(code)) {
                                            Toast.makeText(payContext, msg, Toast.LENGTH_LONG).show();
                                            params1.payResult = false;
                                            intent.putExtra(Constants.PARAMS_DATA, params1);
                                            payContext.startActivity(intent);
                                            return;
                                        }
                                    }
                                }

                                if(obj.has("Status")){
                                    String status = "";
                                    if ("01".equals(obj.getString("Status"))) {
                                        status = "未支付";
                                        params1.payResult = false;
                                    }
                                    if ("02".equals(obj.getString("Status"))) {
                                        status = "已支付";
                                        params1.payResult = true;
                                    }
                                    if ("03".equals(obj.getString("Status"))) {
                                        status = "已退款(全额撤销/冲正)";
                                        params1.payResult = false;
                                    }
                                    if ("04".equals(obj.getString("Status"))) {
                                        status = "已过期";
                                        params1.payResult = false;
                                    }
                                    if ("05".equals(obj.getString("Status"))) {
                                        status = "已作废";
                                        params1.payResult = false;
                                    }
                                    if ("06".equals(obj.getString("Status"))) {
                                        status = "支付中";
                                        params1.payResult = false;
                                    }
                                    if ("07".equals(obj.getString("Status"))) {
                                        status = "退款中";
                                        params1.payResult = false;
                                    }
                                    if ("08".equals(obj.getString("Status"))) {
                                        status = "已被商户撤销";
                                        params1.payResult = false;
                                    }
                                    if ("09".equals(obj.getString("Status"))) {
                                        status = "已被持卡人撤销";
                                        params1.payResult = false;
                                    }
                                    if ("10".equals(obj.getString("Status"))) {
                                        status = "调账-支付成功";
                                        params1.payResult = true;
                                    }
                                    if ("11".equals(obj.getString("Status"))) {
                                        status = "调账-退款成功";
                                        params1.payResult = false;
                                    }
                                    if ("12".equals(obj.getString("Status"))) {
                                        status = "已退货";
                                        params1.payResult = false;
                                    }
                                    intent.putExtra(Constants.PARAMS_DATA, params1);
                                    payContext.startActivity(intent);
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } catch (JSONException e) {
                    MLog.e("YiLianPay解析处理失败！");
                    e.printStackTrace();
                }

            }
        }.execute();

    }

    //Android 6.0 需要请求权限
    @TargetApi(23)
    private void requestNeedPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(payContext, permissionsList, Manifest.permission.READ_PHONE_STATE))
                permissionsNeeded.add("获取手机信息");
            if (!addPermission(payContext, permissionsList,Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("读取数据卡");
            if (!addPermission(payContext, permissionsList,Manifest.permission.ACCESS_FINE_LOCATION))
                permissionsNeeded.add("定位");
            if (!addPermission(payContext, permissionsList,Manifest.permission.ACCESS_COARSE_LOCATION))
                permissionsNeeded.add("定位");
            if (permissionsList.size() > 0) {
                requestPermissions(payActivtiy,permissionsList.toArray(new String[permissionsList.size()]), 111);
                return;
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(Context Ctx, List<String> permissionsList,
                                  String permission) {
        if (checkSelfPermission(Ctx,permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (shouldShowRequestPermissionRationale(payActivtiy,permission))
                return false;
        }
        return true;
    }

    //http通讯
    private String httpComm(String reqUrl, ArrayList<NameValuePair> reqParams) throws UnsupportedEncodingException,
            IOException, ClientProtocolException {
        String respString = null;
        HttpPost httpPost = new HttpPost(reqUrl);
        HttpEntity entity = new UrlEncodedFormEntity(reqParams, HTTP.UTF_8);
        httpPost.setEntity(entity);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResp = httpClient.execute(httpPost);
        int statecode = httpResp.getStatusLine().getStatusCode();
        if (statecode == 200) {
            respString = EntityUtils.toString(httpResp.getEntity());
        }else{
            Log.e("test", "通讯发生异常，响应码["+statecode+"]");
        }
        return respString;
    }


}
