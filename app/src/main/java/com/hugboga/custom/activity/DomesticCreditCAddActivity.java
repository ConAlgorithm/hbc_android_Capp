package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.ToastUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.epos.EposFirstPay;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestEposCheckFactor;
import com.hugboga.custom.data.request.RequestEposFirstPay;
import com.hugboga.custom.utils.PriceFormat;
import com.hugboga.custom.widget.DatePickerYearDialog;
import com.hugboga.custom.widget.domesticcc.DomesticOldPayView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

import static com.hugboga.custom.activity.ChoosePaymentActivity.PAY_PARAMS;

/**
 * 添加信用卡
 * Created by HONGBO on 2017/10/23 16:57.
 */
public class DomesticCreditCAddActivity extends BaseActivity {

    public static final String KEY_VALIDE_TYPE = "key_valide_type"; //0：添加卡 1：只加验要素 2：加验要素+验证码
    public static final int KEY_VALIDE_TYPE0 = 0; //0：添加卡
    public static final int KEY_VALIDE_TYPE1 = 1; //1：只加验要素
    public static final int KEY_VALIDE_TYPE2 = 2; //2：加验要素+验证码

    @Bind(R.id.header_title)
    TextView toolbarTitle;
    @Bind(R.id.domestic_pay_dialog)
    DomesticOldPayView domesticOldPayView; //支付弹框

    @Bind(R.id.domesticCaddCheck)
    CheckBox domesticCaddCheck; //是否同意协议
    @Bind(R.id.domestic_add_number)
    EditText domestic_add_number; //银行卡号
    @Bind(R.id.domestic_add_date)
    TextView domestic_add_date; //有效期
    @Bind(R.id.domestic_add_number3)
    EditText domestic_add_number3; //安全码
    @Bind(R.id.domestic_add_number4)
    EditText domestic_add_number4; //持卡人姓名
    @Bind(R.id.domestic_add_number5)
    EditText domestic_add_number5; //持卡人身份证
    @Bind(R.id.domestic_add_phone)
    EditText domestic_add_phone; //办卡时预留的手机号
    @Bind(R.id.domestic_credit_add_next)
    Button submitBtn; //下一步按钮
    @Bind(R.id.domesticProtocol)
    TextView domesticProtocol; //协议

    private int selectYear;
    private int selectMonth;

    ChoosePaymentActivity.RequestParams requestParams;

    @Override
    public int getContentViewId() {
        return R.layout.activity_domestic_credit_cadd;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(getTitle());
        requestParams = (ChoosePaymentActivity.RequestParams) getIntent().getSerializableExtra(ChoosePaymentActivity.PAY_PARAMS);
        reloadProtocol(); //是否显示协议
        //增加字段监控
        domestic_add_number.addTextChangedListener(watcher);
        domestic_add_date.addTextChangedListener(watcher);
        domestic_add_number3.addTextChangedListener(watcher);
        domestic_add_number4.addTextChangedListener(watcher);
        domestic_add_number5.addTextChangedListener(watcher);
        domestic_add_phone.addTextChangedListener(watcher);
        // 如果是加验要素处理，则根据需要加验内容进行显示字段，验证只对显示组件进行校验
        if (getIntent().getIntExtra(KEY_VALIDE_TYPE, -1) != 0) {
            //加载加验界面
            //TODO 拿到加验需要的要素，对应各个字段展示
            reloadValideField();
        }
    }

    /**
     * 加验要素界面显示
     */
    private void reloadValideField() {
        domestic_add_number.setVisibility(View.GONE);
//        domestic_add_date.setVisibility(View.GONE);
        domestic_add_number3.setVisibility(View.GONE);
        domestic_add_number4.setVisibility(View.GONE);
        domestic_add_number5.setVisibility(View.GONE);
        domestic_add_phone.setVisibility(View.GONE);
    }

    /**
     * 是否显示协议
     * 1. 如果支付金额小于5万，则显示支付协议默认选中
     * 2. 如果支付金额大于等于5万，则不显示支付金额，协议不选中
     */
    private void reloadProtocol() {
        if (requestParams != null && requestParams.shouldPay < 50000) {
            domesticCaddCheck.setVisibility(View.VISIBLE);
            domesticProtocol.setVisibility(View.VISIBLE);
        } else {
            domesticCaddCheck.setVisibility(View.GONE);
            domesticCaddCheck.setChecked(false);
            domesticProtocol.setVisibility(View.GONE);
        }
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
            submitBtn.setEnabled(checkContent()); //验证通过启用按钮
        }
    };

    @OnClick({R.id.header_left_btn, R.id.domestic_layout2, R.id.domestic_credit_add_next, R.id.domesticProtocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.domestic_layout2:
                //选择有效期
                Calendar calendar = Calendar.getInstance();
                DatePickerYearDialog dialog = new DatePickerYearDialog(this, 0, new DatePickerYearDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth) {
                        selectYear = startYear;
                        selectMonth = startMonthOfYear + 1;
                        domestic_add_date.setText(PriceFormat.month2(selectMonth) + "月份／" + selectYear + "年");
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setTitle(R.string.domestic_date_dialog_title);
                dialog.show();
                break;
            case R.id.domestic_credit_add_next:
                //点击下一步，首次支付进行绑定卡操作
                if (getIntent() != null) {
                    switch (getIntent().getIntExtra(KEY_VALIDE_TYPE, -1)) {
                        case KEY_VALIDE_TYPE0:
                            //新卡绑定
                            firstPay();
                            break;
                        case KEY_VALIDE_TYPE1:
                            //只验证要素
                            payFactor();
                            break;
                        case KEY_VALIDE_TYPE2:
                            //验证要素和验证码
                            payFactor();
                            break;
                    }
                }
                break;
            case R.id.domesticProtocol:
                //支付协议查看
                Intent intent = new Intent(activity, WebInfoActivity.class);
                intent.putExtra("web_url", UrlLibs.H5_CREDIT_CARD_ARGEEMENT);
                activity.startActivity(intent);
                break;
        }
    }

    /**
     * 检查填写内容是否完整
     *
     * @return
     */
    private boolean checkContent() {
        String number = domestic_add_number.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        String date = domestic_add_date.getText().toString().trim();
        if (TextUtils.isEmpty(date)) {
            return false;
        }
        String cvv = domestic_add_number3.getText().toString().trim();
        if (TextUtils.isEmpty(cvv)) {
            return false;
        }
        String username = domestic_add_number4.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            return false;
        }
        String cardNum = domestic_add_number5.getText().toString().trim();
        if (TextUtils.isEmpty(cardNum)) {
            return false;
        }
        String phone = domestic_add_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        return true;
    }

    /**
     * 加验要素验证处理
     *
     * @return
     */
    private boolean checkFactor() {
        if (domestic_add_number.getVisibility() == View.VISIBLE) {
            String number = domestic_add_number.getText().toString().trim();
            if (TextUtils.isEmpty(number)) {
                return false;
            }
        }
        if (domestic_add_date.getVisibility() == View.VISIBLE) {
            String date = domestic_add_date.getText().toString().trim();
            if (TextUtils.isEmpty(date)) {
                return false;
            }
        }
        if (domestic_add_number3.getVisibility() == View.VISIBLE) {
            String cvv = domestic_add_number3.getText().toString().trim();
            if (TextUtils.isEmpty(cvv)) {
                return false;
            }
        }
        if (domestic_add_number4.getVisibility() == View.VISIBLE) {
            String username = domestic_add_number4.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                return false;
            }
        }
        if (domestic_add_number5.getVisibility() == View.VISIBLE) {
            String cardNum = domestic_add_number5.getText().toString().trim();
            if (TextUtils.isEmpty(cardNum)) {
                return false;
            }
        }
        if (domestic_add_phone.getVisibility() == View.VISIBLE) {
            String phone = domestic_add_phone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 首次支付请求
     */
    private void firstPay() {
        if (checkContent()) {
            RequestEposFirstPay request = new RequestEposFirstPay(this,
                    requestParams.orderId, requestParams.shouldPay, requestParams.couponId,
                    domestic_add_number5.getText().toString().trim(),
                    domestic_add_phone.getText().toString().trim(),
                    domestic_add_number4.getText().toString().trim(),
                    domestic_add_number.getText().toString().trim(),
                    String.valueOf(selectYear), PriceFormat.month2(selectMonth),
                    domestic_add_number3.getText().toString().trim(),
                    domesticCaddCheck.isChecked());
            HttpRequestUtils.request(this, request, this);
        }
    }

    /**
     * 加验要素请求
     */
    private void payFactor() {
        if (checkFactor()) {
            RequestEposCheckFactor request = new RequestEposCheckFactor(this,
                    domestic_add_number5.getText().toString().trim(),
                    domestic_add_phone.getText().toString().trim(),
                    domestic_add_number4.getText().toString().trim(),
                    domestic_add_number.getText().toString().trim(),
                    String.valueOf(selectYear), PriceFormat.month2(selectMonth),
                    domestic_add_number3.getText().toString().trim());
            HttpRequestUtils.request(this, request, this);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestEposFirstPay) {
            //首次信用卡支付
            EposFirstPay eposFirstPay = ((RequestEposFirstPay) request).getData();
            if (eposFirstPay != null) {
                doFirstPay(eposFirstPay);
            }
        } else if (request instanceof RequestEposCheckFactor) {
            //加验要素
            EposFirstPay eposPayFactor = ((RequestEposCheckFactor) request).getData();
            if (eposPayFactor != null) {
                doPayFactor(eposPayFactor);
            }
        }
    }

    /**
     * 新添加信用卡支付结果处理
     *
     * @param eposFirstPay
     */
    private void doFirstPay(EposFirstPay eposFirstPay) {
        switch (eposFirstPay.eposPaySubmitStatus) {
            case "1":
                //提交成功
                Intent intentSuccess = new Intent(this, PayResultActivity.class);
                PayResultActivity.Params params1 = new PayResultActivity.Params();
                params1.orderId = requestParams.orderId;
                params1.orderType = requestParams.orderType;
                params1.apiType = requestParams.apiType;
                params1.payResult = true;
                intentSuccess.putExtra(Constants.PARAMS_DATA, params1);
                startActivity(intentSuccess);
                break;
            case "2":
                //提交失败
                ToastUtils.showToast(this, eposFirstPay.errorMsg);
                break;
            case "3":
                //加验要素
                Intent intent = new Intent(this, DomesticCreditCAddActivity.class);
                intent.putExtra(PAY_PARAMS, requestParams);
                intent.putExtra(KEY_VALIDE_TYPE, KEY_VALIDE_TYPE1);
                startActivity(intent);
                break;
            case "4":
                //短信验证
                domesticOldPayView.show(eposFirstPay.payNo, PriceFormat.price(requestParams.shouldPay));
                break;
            case "5":
                //加验要素+短信验证
                Intent intents = new Intent(this, DomesticCreditCAddActivity.class);
                intents.putExtra(PAY_PARAMS, requestParams);
                intents.putExtra(KEY_VALIDE_TYPE, KEY_VALIDE_TYPE2);
                startActivity(intents);
                break;
        }
    }

    /**
     * 加验要素请求结果处理
     *
     * @param eposPayFactor
     */
    private void doPayFactor(EposFirstPay eposPayFactor) {
        //加验要素成功之后进行短信校验
        /*
        此结果是加验要素结果,只有两种可能
        1. 校验成功
        2. 加验短信验证码
         */
        if (getIntent() != null) {
            switch (getIntent().getIntExtra(KEY_VALIDE_TYPE, -1)) {
                case KEY_VALIDE_TYPE1:
                    //只验证要素
                    break;
                case KEY_VALIDE_TYPE2:
                    //验证要素和验证码
                    domesticOldPayView.show(eposPayFactor.payNo, PriceFormat.price(requestParams.shouldPay));
                    break;
            }
        }
    }
}
