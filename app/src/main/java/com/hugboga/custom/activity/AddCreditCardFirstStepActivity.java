package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CreditCardInfoBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestAddCreditCard;
import com.hugboga.custom.data.request.RequestTypeQueryCreditCard;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/7.
 */

public class AddCreditCardFirstStepActivity extends BaseActivity {

    public static final String TAG = "AddCreditCardFirstStepActivity";

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;

    @Bind(R.id.credit_card_number)
    EditText creditCardNumber;
    @Bind(R.id.next_step)
    Button nextStep;
    @Bind(R.id.credit_card_clear)
    ImageView creditCardClear;

    String priceStr;   //要付的价格
    ChoosePaymentActivity.RequestParams params;
    CreditCardInfoBean creditCardInfoBean;
    StringBuffer buffer;//用来缓冲的buffer
    StringBuffer bufferCreditNum;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_add_credit_card_first_step);
        ButterKnife.bind(this);

        params = (ChoosePaymentActivity.RequestParams)getIntent().getSerializableExtra(ChoosePaymentActivity.PAY_PARAMS);
        init();
    }

    public void init(){
        headerTitle.setText(R.string.add_credit_activity_title);
        headerTitle.setVisibility(View.VISIBLE);
        headerLeftBtn.setVisibility(View.VISIBLE);
        setNextStepStatus(false);

        buffer = new StringBuffer();//用来缓冲的buffer
        bufferCreditNum = new StringBuffer();

        priceStr = String.valueOf(params.shouldPay);

        //卡号输入进行监听
        creditCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0 || TextUtils.isEmpty(s)){
                    setNextStepStatus(false);
                } else {
                    setNextStepStatus(true);
                }
            }
        });

    }

    /**
     * 通过卡号查询银行
     */
    public void initCreditCardRequest(){
        RequestTypeQueryCreditCard requestQueryCreditCard = new RequestTypeQueryCreditCard(getBaseContext(), creditCardNumber.getText().toString(), priceStr);
        requestData(requestQueryCreditCard);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestTypeQueryCreditCard) {
            if (null == ((RequestTypeQueryCreditCard) request).getData()){
                return;
            }
            creditCardInfoBean = ((RequestTypeQueryCreditCard) request).getData();

            Intent intent = new Intent(this, AddCreditCardSecondStepActivity.class);
            intent.putExtra(AddCreditCardSecondStepActivity.CRAD_INFO, creditCardInfoBean);
            intent.putExtra(AddCreditCardSecondStepActivity.CRAD_NUMBER, creditCardNumber.getText().toString().trim());
            if (null != params) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ChoosePaymentActivity.PAY_PARAMS, params);
                intent.putExtras(bundle);
            }
            intent.putExtra(Constants.PARAMS_SOURCE, TAG);
            startActivity(intent);
        }
    }

    /**
     * 设置下一步按钮状态
     * @param b
     */
    public void setNextStepStatus(Boolean b){
        if (b){
            nextStep.setEnabled(true);
            nextStep.setBackgroundResource(R.drawable.shape_rounded_yellow_btn);
        }else {
            nextStep.setEnabled(false);
            nextStep.setBackgroundResource(R.drawable.shape_rounded_gray_btn);
        }
    }

    @OnClick({R.id.header_left_btn, R.id.next_step, R.id.credit_card_clear})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.next_step:
                //信用卡添加完下一步
                initCreditCardRequest();
                bufferCreditNum.setLength(0);
                buffer.setLength(0);
                break;
            case R.id.credit_card_clear:
                creditCardNumber.setText("");
                break;
        }
    }

}