package com.hugboga.custom.widget.domesticcc;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.ToastUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.DomesticCreditCAddActivity;
import com.hugboga.custom.activity.DomesticCreditCardActivity;
import com.hugboga.custom.data.bean.epos.EposFirstPay;
import com.hugboga.custom.data.request.RequestEposSendSms;
import com.hugboga.custom.data.request.RequestEposSmsVerify;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 易宝短信验证码验证
 * Created by HONGBO on 2017/10/23 19:54.
 */

public class DomesticOldPayView extends FrameLayout implements HttpRequestListener {

    @BindView(R.id.domestic_pay_ok_img)
    ImageView domestic_pay_ok_img;
    @BindView(R.id.domestic_pay_ok_name)
    TextView domestic_pay_ok_name;
    @BindView(R.id.domestic_pay_ok_card)
    TextView domestic_pay_ok_card;
    @BindView(R.id.pay_sms_resend)
    TextView pay_sms_resend; //重新发送验证码
    @BindView(R.id.pay_sms_time)
    TextView pay_sms_time; //倒计时
    @BindView(R.id.pay_sms_et_code)
    EditText pay_sms_et_code; //验证码输入
    @BindView(R.id.pay_sms_btn)
    Button pay_sms_btn;

    String payNo;

    public DomesticOldPayView(@NonNull Context context) {
        this(context, null);
    }

    public DomesticOldPayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.domestic_old_pay_layout, this);
        ButterKnife.bind(this, view);
        pay_sms_et_code.addTextChangedListener(watcher);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            pay_sms_btn.setEnabled(valide());
        }
    };

    public void show(String payNo, int iconResId, String bankName, String cardNum, String price) {
        this.payNo = payNo;
        initSmsView(); //初始化短信发送界面
        if (iconResId != 0) {
            domestic_pay_ok_img.setImageResource(iconResId);
        }
        if (!TextUtils.isEmpty(bankName)) {
            domestic_pay_ok_name.setText(bankName);
        }
        if (!TextUtils.isEmpty(cardNum)) {
            domestic_pay_ok_card.setText(cardNum);
        }
        pay_sms_btn.setText(String.format(getContext().getString(R.string.domestic_card_pay_btn_txt), price));
        setVisibility(VISIBLE);
        //验证码开启倒计时
        startSmsStart();
    }

    private void initSmsView() {
        time = 0; //倒计时停止
        pay_sms_resend.setText(R.string.domestic_sms_send);
        pay_sms_et_code.setText(""); //清空上次输入
        pay_sms_time.setText(""); //清空倒计时
    }

    private void startSmsStart() {
        time = 30;
        pay_sms_resend.setText(R.string.domestic_sms_resend);
        enableResendBtn(false);
        handler.postDelayed(runnable, 0);
    }

    //短信验证码倒计时
    private int time = 30;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (time > 0) {
                pay_sms_time.setText("(" + time + ")");
                handler.postDelayed(runnable, 1000);
            } else {
                enableResendBtn(true);
                pay_sms_time.setText("");
            }
            time--;
        }
    };

    private void enableResendBtn(boolean isEnable) {
        if (isEnable) {
            pay_sms_resend.setClickable(true);
            pay_sms_resend.setEnabled(true);
        } else {
            pay_sms_resend.setClickable(false);
            pay_sms_resend.setEnabled(false);
        }
    }

    public void close() {
        setVisibility(GONE);
    }

    @OnClick({R.id.domestic_old_pay_root, R.id.domestic_old_pay_close, R.id.pay_sms_resend, R.id.pay_sms_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.domestic_old_pay_root:
                //屏蔽点击项
                break;
            case R.id.domestic_old_pay_close:
                setVisibility(GONE);
                initSmsView(); //关闭窗口初始化短信窗口界面
                break;
            case R.id.pay_sms_resend:
                //重新发送验证码
                RequestEposSendSms requestEposSendSms = new RequestEposSendSms(getContext(), payNo);
                HttpRequestUtils.request(getContext(), requestEposSendSms, this);
                break;
            case R.id.pay_sms_btn:
                //验证码验证
                if (valide()) {
                    RequestEposSmsVerify requestEposSmsVerify = new RequestEposSmsVerify(getContext(), payNo, pay_sms_et_code.getText().toString().trim());
                    HttpRequestUtils.request(getContext(), requestEposSmsVerify, this);
                }
                break;
        }
    }

    /**
     * 验证短信验证码是否输入
     *
     * @return
     */
    private boolean valide() {
        String smsCode = pay_sms_et_code.getText().toString().trim();
        return !TextUtils.isEmpty(smsCode);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if (request instanceof RequestEposSendSms) {
            //重新发送验证码
            EposFirstPay result = (EposFirstPay) request.getData();
            if (result != null) {
                doReSmsResult(result); //处理重新发送短信结果
            }
        } else if (request instanceof RequestEposSmsVerify) {
            //验证码校验
            EposFirstPay result = (EposFirstPay) request.getData();
            if (result != null) {
                doSmsResult(result);
            }
        }
    }

    /**
     * 重新发送验证码处理结果
     */
    private void doReSmsResult(EposFirstPay result) {
        ToastUtils.showToast(getContext(), result.errorMsg);
        /*
        重新发送验证码成功之后做倒计时，如果失败则只提示不做任何处理
        重新发送验证码返回无效，则和校验无效走同样流程，关闭当前验证码弹框，如果是加验则关闭加验界面
         */
        if ("1".equals(result.eposPaySubmitStatus)) {
            startSmsStart(); //开始倒计时
        } else if ("7".equals(result.eposPaySubmitStatus)) {
            //验证码已无效
            setVisibility(GONE);
            doSmsUI(); //加验如果出现错误
        }
    }

    /**
     * 验证码验证处理结果
     * 银行把加验要素在短信验证通过后进行验证
     *
     * @param result
     */
    private void doSmsResult(EposFirstPay result) {
        if (!TextUtils.isEmpty(result.eposPaySubmitStatus)) {
            switch (result.eposPaySubmitStatus) {
                case "1":
                    //成功支付跳转成功
                    setVisibility(GONE);
                    gotoSmsSuccess();
                    break;
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                    //验证码出现错误
                    ToastUtils.showToast(getContext(), result.errorMsg);
                    break;
                case "7": //验证码已无效
                    setVisibility(GONE);
                    doSmsUI(); //加验如果出现错误
                    ToastUtils.showToast(getContext(), result.errorMsg);
                    break;
                default:
                    showSysAlert(result);
                    break;
            }
        } else {
            showSysAlert(result);
        }
    }

    /**
     * 弹出系统级错误
     *
     * @param result
     */
    private void showSysAlert(EposFirstPay result) {
        //银行其他错误
        new AlertDialog.Builder(getContext()).setMessage(result.errorMsg).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setVisibility(GONE); //其他错误也关闭当前短信框
                doSmsUI(); //银行未知错误
            }
        }).show();
    }

    /**
     * 短信验证码错误或者位置错误时界面交互处理
     * 1. 历史卡短信校验出错，关闭当前短信验证弹框，保留在历史卡列表界面
     * 2. 新绑卡短信校验出错，关闭当前短信验证弹框，点击下一步重新绑卡
     * 3. 加验要素短信校验出错，关闭当前短信验证弹框，关闭加验要素界面，显示绑新卡或者历史卡列表
     */
    private void doSmsUI() {
        //此处已经关闭了当前短信弹框，只需要做后续操作
        if (getContext() != null && (getContext() instanceof DomesticCreditCAddActivity)) {
            //判断如果是加验要素界面则关闭加验要素窗口
            ((DomesticCreditCAddActivity) getContext()).closeOfValide();
        }
    }

    /**
     * 支付成功打开成功界面
     */
    private void gotoSmsSuccess() {
        if (getContext() != null && (getContext() instanceof DomesticCreditCardActivity)) {
            ((DomesticCreditCardActivity) getContext()).gotoSuccess();
        } else if (getContext() != null && (getContext() instanceof DomesticCreditCAddActivity)) {
            ((DomesticCreditCAddActivity) getContext()).gotoSuccess();
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        ToastUtils.showToast(getContext(), errorInfo.exception.getMessage());
    }
}
