package com.hugboga.custom.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.DefaultSSLSocketFactory;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.PhoneInfo;
import com.huangbaoche.hbcframe.widget.DialogUtilInterface;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.BankLogoBean;
import com.hugboga.custom.data.bean.CreditCardInfoBean;
import com.hugboga.custom.data.bean.YiLianPayBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestAddCreditCard;
import com.hugboga.custom.data.request.RequestCreditCardPay;
import com.hugboga.custom.utils.SharedPre;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CircleImageView;
import com.hugboga.custom.yilianapi.YiLianPay;
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
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

import com.huangbaoche.hbcframe.HbcApplication;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.util.PhoneInfo;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.activity.BaseActivity;
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
import static com.huangbaoche.hbcframe.data.net.HttpRequestUtils.getDialogUtil;

/**
 * Created by Administrator on 2017/3/7.
 */

public class AddCreditCardSecondStepActivity extends BaseActivity{
    public static final String CRAD_INFO = "credit_card_info";
    public static final String CRAD_NUMBER = "card_num";

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;

    @Bind(R.id.choose_payment_price_tv)
    TextView choosePaymentPriceTV;//要付的金额
    @Bind(R.id.add_credit_sec_step_logo)
    ImageView addCreditSecStepLogo;//银行logo
    @Bind(R.id.add_credit_sec_step_bank_name)
    TextView addCreditSecStepBankName;//银行名称
    @Bind(R.id.add_credit_sec_step_bank_type)
    TextView addCreditSecStepBankType;//银行卡的类型
    @Bind(R.id.add_credit_sec_step_number)
    TextView addCreditSecStepNumber;//银行卡卡号
    @Bind(R.id.add_credit_sec_step_reserved_phone)
    EditText addCreditSecStepReserverdPhone;//预留手机号
    @Bind(R.id.common_credit_card_payment_protocol)
    TextView commomCreditCardPaymentProtocol;//常用卡支付协议
    @Bind(R.id.common_credit_card_checkBox)
    CheckBox commonCheckBox;
    @Bind(R.id.submit_btn)
    Button submitBtn;

    CircleImageView circleImageView;
    String creditNum;//卡号
    String priceStr;//价格
    String creditType;
    String phoneStr;
    Integer cardId;  //绑定完返回的id,需要作为支付的参数
    CreditCardInfoBean creditCardInfoBean;//用来接收上一界面返回的数据
    CreditCardInfoBean creditAddInfo;//信用卡绑定完返回的数据
    ChoosePaymentActivity.RequestParams params;
    String sourse;
    DialogUtilInterface dialogUtil = getDialogUtil(this);

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setNextStepStatus(checkEmpty());
            phoneStr = s.toString();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_credit_sec_step);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        if (null == intent.getSerializableExtra(CRAD_INFO)) {
            return;
        }
        creditCardInfoBean = (CreditCardInfoBean) intent.getSerializableExtra(CRAD_INFO);
        sourse = intent.getStringExtra(Constants.PARAMS_SOURCE);
        creditNum = intent.getStringExtra(CRAD_NUMBER);

        if (null != intent.getSerializableExtra(ChoosePaymentActivity.PAY_PARAMS)) {
            params = (ChoosePaymentActivity.RequestParams)intent.getSerializableExtra(ChoosePaymentActivity.PAY_PARAMS);
            priceStr = String.valueOf(params.shouldPay);
        }
        init();
        setCreditLayStatus(creditCardInfoBean);   //初始化卡的类型
    }

    public void init(){
        headerTitle.setText(R.string.add_credit_activity_title);
        headerTitle.setVisibility(View.VISIBLE);
        headerLeftBtn.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(creditCardInfoBean.telNo)){
            phoneStr = creditCardInfoBean.telNo;
            addCreditSecStepReserverdPhone.setText(phoneStr);
        }

        choosePaymentPriceTV.setText(priceStr);
        //协议设置下划线
        commomCreditCardPaymentProtocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        commomCreditCardPaymentProtocol.getPaint().setAntiAlias(true);

        addCreditSecStepReserverdPhone.addTextChangedListener(watcher);
        commonCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setNextStepStatus(checkEmpty());
            }
        });

        setNextStepStatus(checkEmpty());
    }

    @OnClick({R.id.header_left_btn, R.id.submit_btn})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.submit_btn:
                gotoSubmit();
                break;
        }
    }

    /**
     * 提交
     */
    public void gotoSubmit(){
        if (null != creditCardInfoBean && AddCreditCardFirstStepActivity.TAG.equals(sourse)) {
            RequestAddCreditCard requestAddCreditCard = new RequestAddCreditCard(getBaseContext(), creditNum,
                    creditCardInfoBean.idCardNo, phoneStr, creditCardInfoBean.userName,
                    creditCardInfoBean.accType, creditCardInfoBean.bankId, creditCardInfoBean.bankName);
            HttpRequestUtils.request(this,requestAddCreditCard,this,false);
            new Runnable(){
                @Override
                public void run() {
                    dialogUtil.showLoadingDialog();
                }
            }.run();
        }else if(ChoosePaymentActivity.TAG.equals(sourse)){
            cardId = creditCardInfoBean.id;
            RequestCreditCardPay creditCardPay = new RequestCreditCardPay(getBaseContext(),params.orderId ,priceStr, params.couponId, cardId,addCreditSecStepReserverdPhone.getText().toString());
            requestData(creditCardPay);
        }
    }

    /**
     * 判断为空
     * @return
     */
    public Boolean checkEmpty(){
        if (TextUtils.isEmpty(addCreditSecStepReserverdPhone.getText().toString())){
            return false;
        }else if (!commonCheckBox.isChecked()){
            return false;
        }
        return true;
    }

    @Subscribe()
    public void onEventMainThread(EventAction action){
        switch (action.getType()){
            case YILIAN_PAY:
                Intent resultIntent = new Intent(this, PayResultActivity.class);
                Bundle bundle = new Bundle();
                PayResultActivity.Params params1 = new PayResultActivity.Params();
                params1.orderId = params.orderId;
                params1.payResult = true;
//                bundle.putSerializable("result", (Serializable) action.getData());
//                startActivity(resultIntent);
                break;
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        dialogUtil.dismissLoadingDialog();
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof  RequestAddCreditCard){
            if (null == request.getData()){
                dialogUtil.dismissLoadingDialog();
                return;
            }
            creditAddInfo = (CreditCardInfoBean)request.getData();
            cardId = creditAddInfo.id;

            //绑定成功进行支付请求
            RequestCreditCardPay creditCardPay = new RequestCreditCardPay(getBaseContext(),params.orderId ,priceStr, params.couponId, cardId, addCreditSecStepReserverdPhone.getText().toString());
            HttpRequestUtils.request(this,creditCardPay,this,false);
        }else if (request instanceof  RequestCreditCardPay){
            dialogUtil.dismissLoadingDialog();
            if (null != ((RequestCreditCardPay) request).getData()){
                YiLianPayBean yiLianPayBean = ((RequestCreditCardPay) request).getData();
                gotoPayYiLian(yiLianPayBean);
            }
        }
    }

    /**
     * 对银行卡信息进行初始化
     * @param bean
     */
    public void setCreditLayStatus(CreditCardInfoBean bean){
        if (null == bean){
            return;
        }
        addCreditSecStepBankName.setText(bean.bankName);
        addCreditSecStepBankType.setText(bean.accType);
        creditType = "01".equals(bean.accType) ? "借记卡" : "信用卡";
        addCreditSecStepBankType.setText(creditType);

        StringBuffer bufferNum = new StringBuffer(creditNum);
        for (int i=0; i < creditNum.length() ;i++){
            if ((i == 4 || i == 9 || i == 14 )) {
                bufferNum.insert(i, ' ');
            }
        }
        if (bufferNum.length() >=19){
            bufferNum.insert(19, ' ');
        }
        addCreditSecStepNumber.setText(bufferNum.toString());
        bufferNum.setLength(0);

        //设置银行卡logo
        if ("ChoosePaymentActivity".equals(sourse)){
            bean.bankId = bean.bandId;
        }
        setBankLogo(bean.bankId);
    }

    /**
     * 设置银行logo
     * @param bankId
     */
    public void setBankLogo(String bankId){
        String path;
        List<BankLogoBean> logoBeanList = setListData();//获得银行卡logo列表
        for (BankLogoBean logoBean: logoBeanList){
            if (bankId.equals(logoBean.cardType)){
                path = logoBean.url;
                AssetManager manager = getBaseContext().getAssets();
                try {
                    InputStream is = manager.open(path);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    addCreditSecStepLogo.setImageBitmap(bitmap);
                    addCreditSecStepLogo.setMinimumWidth(UIUtils.dip2px(32));
                    addCreditSecStepLogo.setMinimumHeight(UIUtils.dip2px(32));
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 设置确认订单按钮状态
     * @param b
     */
    public void setNextStepStatus(Boolean b){
        if (b){
            submitBtn.setEnabled(true);
            submitBtn.setBackgroundResource(R.drawable.shape_rounded_yellow_btn);
        }else {
            submitBtn.setEnabled(false);
            submitBtn.setBackgroundResource(R.drawable.shape_rounded_gray_btn);
        }
    }

    /**
     * 读取银行图标字符串
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 获得json数据
     */
    public List<BankLogoBean> setListData() {
        String bankStr = getJson(getBaseContext(),"bank.json");//根据读取的字符串进行解析

        List<BankLogoBean> data = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(bankStr);
            int len = array.length();
            BankLogoBean bankLogoBean = null;
            for (int i = 0; i < len; i++) {
                JSONObject object = array.getJSONObject(i);
                bankLogoBean = new BankLogoBean();
                bankLogoBean.url = object.getString("url");
                bankLogoBean.cardType = object.getString("cardType");
                data.add(bankLogoBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void gotoPayYiLian(YiLianPayBean yiLianPayBean){
        YiLianPay yiLianPay = new YiLianPay(this,this,yiLianPayBean,params.orderId);
        yiLianPay.pay();
    }
}