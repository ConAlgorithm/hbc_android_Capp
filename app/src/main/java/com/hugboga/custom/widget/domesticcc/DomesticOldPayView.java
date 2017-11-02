package com.hugboga.custom.widget.domesticcc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.hugboga.custom.data.bean.epos.EposFirstPay;
import com.hugboga.custom.data.request.RequestEposSendSms;
import com.hugboga.custom.data.request.RequestEposSmsVerify;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 易宝短信验证码验证
 * Created by HONGBO on 2017/10/23 19:54.
 */

public class DomesticOldPayView extends FrameLayout implements HttpRequestListener {

    @Bind(R.id.domestic_pay_ok_img)
    ImageView domestic_pay_ok_img;
    @Bind(R.id.domestic_pay_ok_name)
    TextView domestic_pay_ok_name;
    @Bind(R.id.domestic_pay_ok_card)
    TextView domestic_pay_ok_card;
    @Bind(R.id.pay_sms_resend)
    TextView pay_sms_resend; //重新发送验证码
    @Bind(R.id.pay_sms_time)
    TextView pay_sms_time; //倒计时
    @Bind(R.id.pay_sms_et_code)
    EditText pay_sms_et_code; //验证码输入
    @Bind(R.id.pay_sms_btn)
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
        domestic_pay_ok_img.setImageResource(iconResId);
        domestic_pay_ok_name.setText(bankName);
        domestic_pay_ok_card.setText(cardNum);
        pay_sms_btn.setText("支付 " + price);
        setVisibility(VISIBLE);
    }

    public void close() {
        setVisibility(GONE);
    }

    @OnClick({R.id.domestic_old_pay_root, R.id.domestic_old_pay_close, R.id.pay_sms_resend, R.id.pay_sms_et_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.domestic_old_pay_root:
                //屏蔽点击项
                break;
            case R.id.domestic_old_pay_close:
                setVisibility(GONE);
                break;
            case R.id.pay_sms_resend:
                //重新发送验证码
                RequestEposSendSms requestEposSendSms = new RequestEposSendSms(getContext(), payNo);
                HttpRequestUtils.request(getContext(), requestEposSendSms, this);
                break;
            case R.id.pay_sms_et_code:
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
            ToastUtils.showToast(getContext(), result.errorMsg);
        } else if (request instanceof RequestEposSmsVerify) {
            //验证码校验
            EposFirstPay result = (EposFirstPay) request.getData();
            ToastUtils.showToast(getContext(), result.errorMsg);
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {

    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {

    }
}
