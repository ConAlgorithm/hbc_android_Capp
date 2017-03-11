package com.hugboga.custom.yilianapi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.PhoneInfo;
import com.hugboga.custom.activity.ChoosePaymentActivity;
import com.hugboga.custom.activity.PayResultActivity;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.YiLianPayBean;
import com.hugboga.custom.data.net.UrlLibs;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private String actualPrice;//实际支付金额
    private String coupId;//劵id
    private String cardId;//卡id
    YiLianPayBean yiLianPayBean;

    public YiLianPay(Context context, Activity activity, YiLianPayBean yiLianPayBean){
        this.payContext = context;
        this.payActivtiy = activity;
        this.yiLianPayBean = yiLianPayBean;
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
                //组织参数，用于向商户服务器下单的参数
//                ArrayList<NameValuePair> reqParams = new ArrayList<NameValuePair>();
//                reqParams.add(new BasicNameValuePair("orderNo", orderNo));//订单号
//                reqParams.add(new BasicNameValuePair("actualPrice", actualPrice+""));
//                reqParams.add(new BasicNameValuePair("coupId", coupId));
//                reqParams.add(new BasicNameValuePair("cardId", cardId));
                //以上参数根据实际需要来组织

                //用于接收通讯响应的内容
                String respString = null;

                //请求商户服务器下单地址
//                try {
//                    MLog.i("test", "正在请求："+URL_PAY_ORDER);
//                    respString = httpComm(URL_PAY_ORDER, reqParams);
//
//                } catch (Exception e) {
//                    MLog.e("下单失败，通讯发生异常", e);
//                    e.printStackTrace();
//                }
                return respString;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dia.dismiss();

//                if (s == null){
//                    MLog.d("从数据获取数据失败");
//                    return;
//                }

//                其中RetCode、RetMsg用于告诉客户端请求是否成功，其它参数可直接传递给易联支付插件
//                解析数据
                if (null == yiLianPayBean){
                    MLog.d("传入数据获取数据为空");
                    return;
                }
                try {
//                    JSONObject json = new JSONObject(s);
//                    YiLianPayBean payBean = new YiLianPayBean();
//                    payBean.MerchOrderId = json.getString("MerchOrderId");
//                    payBean.MerchantId = json.getString("MerchantId");
//                    payBean.Amount = json.getString("Amount");
//                    payBean.TradeTime = json.getString("TradeTime");
//                    payBean.OrderId = json.getString("OrderId");
//                    payBean.Sign = json.getString("Sign");

//                    //校验返回结果
//                    if (!json.has("RetCode") || !"0000".equals(json.getString("RetCode"))) {
//                        if (json.has("RetMsg")) {
//                            Toast.makeText(payContext, json.getString("RetMsg"), Toast.LENGTH_LONG).show();
//                            MLog.e(json.getString("RetMsg"));
//                        }else{
//                            Toast.makeText(payContext, "返回数据有误:"+s, Toast.LENGTH_LONG).show();
//                            MLog.e("返回数据有误:"+s);
//                        }
//                        return;
//                    }

//                    json.remove("RetCode");//RetCode参数不需要传递给易联支付插件
//                    json.remove("RetMsg");//RetMsg参数不需要传递给易联支付插件
//                    String upPayReqString = json.toString();
//                    MLog.i("YiLianPay", "请求易联支付插件，参数："+upPayReqString);
                    JSONObject json = new JSONObject();
                    json.put("Version",yiLianPayBean.version);
                    json.put("MerchOrderId",yiLianPayBean.merchOrderId);
                    json.put("MerchantId", yiLianPayBean.merchantId);
                    json.put("Amount", yiLianPayBean.amount);
                    json.put("TradeTime", yiLianPayBean.tradeTime);
                    json.put("OrderId", yiLianPayBean.orderId);
                    json.put("Sign", yiLianPayBean.sign);

                    //组织请求参数
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("Environment", ENVIRONMENT); // 00: 测试环境 01: 生产环境
                    params.put("upPay.Req", json.toString());
                    params.put("thePackageName", PhoneInfo.getPackageName()); //提交包名

                    PayecoPluginPayIn.doPay(payActivtiy, params, new PayecoPluginPayCallBack() {
                        @SuppressLint("ShowToast")
                        @Override
                        public void callBack(String result, String errCode, String errMsg) {
                            if (errCode != null) {

                                Log.e("test", "errCode:" + errCode);
                                Log.e("test", "errMsg:" + errMsg);

                                //支付操作发错错误
                                Toast.makeText(payContext,
                                        String.format("发生异常，错误码：%s，错误描述：%s", errCode, errMsg),
                                        Toast.LENGTH_LONG).show();
//								new AlertDialog.Builder(MainActivity.this).setTitle("提示")
//								.setMessage(String.format("发生异常，错误码：%s，错误描述：%s", errCode, errMsg))
//								.setPositiveButton("确定", null).show();
                                return;
                            }

                            final String notifyParams = result;

                            //判断是否是用户主动退出
                            //返回报文为：{"respDesc":"用户主动退出插件","respCode":"W101"}
                            try {
                                JSONObject obj = new JSONObject(result);
                                String code = obj.getString("respCode");
                                String msg = obj.getString("respDesc");
                                if("W101".equals(code)){
                                    Toast.makeText(payContext, msg, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // 使用异步通讯
                            new AsyncTask<Void, Void, String>(){
                                @Override
                                protected String doInBackground(Void... params) {
                                    //用于接收通讯响应的内容
                                    String respString = null;

                                    //通知商户服务器
                                    try {
                                        JSONObject reqJsonParams = new JSONObject(notifyParams);

                                        ArrayList<NameValuePair> reqParams = new ArrayList<NameValuePair>();
                                        @SuppressWarnings("unchecked")
                                        Iterator<String> keys = reqJsonParams.keys();
                                        while (keys.hasNext()) {
                                            String key = keys.next();
                                            String value = reqJsonParams.getString(key);
                                            reqParams.add(new BasicNameValuePair(key, value));
                                        }

                                        Log.i("test", "正在请求："+URL_PAY_NOTIFY);
                                        respString = httpComm(URL_PAY_NOTIFY, reqParams);
                                    } catch (JSONException e) {
                                        Log.e("test", "解析处理失败！", e);
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        Log.e("test", "通知失败，通讯发生异常", e);
                                        e.printStackTrace();
                                    }
                                    return respString;
                                }

                                @Override
                                protected void onPostExecute(String result) {
                                    super.onPostExecute(result);

                                    if (result == null) {
                                        Log.e("test", "通知失败！");
                                        return ;
                                    }

                                    Log.i("test", "响应数据："+result);

                                    try {
                                        //解析响应数据
                                        JSONObject json = new JSONObject(result);

                                        //校验返回结果
                                        if (!json.has("RetMsg")) {
                                            Toast.makeText(payContext, "返回数据有误:"+result, Toast.LENGTH_LONG).show();
                                            Log.e("test", "返回数据有误:"+result);
                                            return ;
                                        }
                                        Toast.makeText(payContext, json.getString("RetMsg"), Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        Log.e("test", "解析处理失败！", e);
                                        e.printStackTrace();
                                    }
                                }
                            }.execute();

                            //跳转至支付结果页面
                            Intent resultIntent = new Intent(payContext, PayResultActivity.class);
                            resultIntent.putExtra("result", result);
                            payContext.startActivity(resultIntent);
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
                requestPermissions(null,permissionsList.toArray(new String[permissionsList.size()]), 111);
                return;
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(Context Ctx, List<String> permissionsList,
                                  String permission) {
        if (checkSelfPermission(null,permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (shouldShowRequestPermissionRationale(null,permission))
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
