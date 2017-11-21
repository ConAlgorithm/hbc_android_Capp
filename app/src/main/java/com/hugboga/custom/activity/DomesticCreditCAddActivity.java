package com.hugboga.custom.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import static com.hugboga.custom.activity.DomesticCreditCardActivity.MAX_PRICE;

/**
 * 添加信用卡
 * Created by HONGBO on 2017/10/23 16:57.
 */
public class DomesticCreditCAddActivity extends BaseActivity {

    public static final String KEY_VALIDE_TYPE = "key_valide_type"; //0：添加卡 1：只加验要素 2：加验要素+验证码
    public static final int KEY_VALIDE_TYPE0 = 0; //0：添加卡
    public static final int KEY_VALIDE_TYPE1 = 1; //1：只加验要素
    public static final int KEY_VALIDE_TYPE2 = 2; //2：加验要素+验证码
    public static final String KEY_VALIDE_NEED = "key_valide_need"; //加验要素编号
    public static final String KEY_VALIDE_BANKNAME = "key_valide_bankname"; //加验银行名
    public static final String KEY_VALIDE_BANKICON = "key_valide_bankicon"; //加验银行图标
    public static final String KEY_VALIDE_CARDNUM = "key_valide_cardnum"; //加验卡号
    public static final String KEY_VALIDE_PAYNUM = "key_valide_paynum"; //加验单号

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
    @Bind(R.id.domestic_add_phone)
    EditText domestic_add_phone; //办卡时预留的手机号
    @Bind(R.id.domestic_credit_add_next)
    Button submitBtn; //下一步按钮
    @Bind(R.id.domesticProtocol)
    TextView domesticProtocol; //协议

    @Bind(R.id.domestic_layout1)
    ConstraintLayout domestic_layout1; //卡号部分
    @Bind(R.id.domestic_add_line1)
    ImageView domestic_add_line1; //卡号部分分割线
    @Bind(R.id.domestic_layout2)
    ConstraintLayout domestic_layout2; //有效期部分
    @Bind(R.id.domestic_add_line2)
    ImageView domestic_add_line2; //有效期部分分割线
    @Bind(R.id.domestic_layout3)
    ConstraintLayout domestic_layout3; //安全码部分
    @Bind(R.id.domestic_add_line3)
    ImageView domestic_add_line3; //安全码分割线
    @Bind(R.id.domestic_layout6)
    ConstraintLayout domestic_layout6; //手机号部分
    @Bind(R.id.domestic_add_line6)
    ImageView domestic_add_line6; //手机号部分分割线

    //==============加验元素============
    @Bind(R.id.domestic_valide_info)
    ConstraintLayout valideLayout;
    @Bind(R.id.domestic_valide_img)
    ImageView domestic_valide_img; //加验卡图标
    @Bind(R.id.domestic_valide_name)
    TextView domestic_valide_name; //加验卡银行名称
    @Bind(R.id.domestic_valide_no)
    TextView domestic_valide_no; //加验卡号

    private int selectYear;
    private int selectMonth;
    private String payNo; //支付单号
    private int valideType; //新卡|加验|加验短信
    private String valideNeed; //加验元素
    private String bankName; //加验银行名称
    private String cardNum; //加验银行卡号
    private int icon; //加验银行卡图标

    ChoosePaymentActivity.RequestParams requestParams;

    @Override
    public int getContentViewId() {
        return R.layout.activity_domestic_credit_cadd;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(getTitle());
        if (savedInstanceState != null) {
            requestParams = (ChoosePaymentActivity.RequestParams) savedInstanceState.getSerializable(ChoosePaymentActivity.PAY_PARAMS);
            payNo = savedInstanceState.getString(KEY_VALIDE_PAYNUM);
            valideType = savedInstanceState.getInt(KEY_VALIDE_TYPE, -1);
            valideNeed = savedInstanceState.getString(KEY_VALIDE_NEED);
            bankName = savedInstanceState.getString(KEY_VALIDE_BANKNAME);
            cardNum = savedInstanceState.getString(KEY_VALIDE_CARDNUM);
            icon = savedInstanceState.getInt(KEY_VALIDE_BANKICON, 0);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                requestParams = (ChoosePaymentActivity.RequestParams) bundle.getSerializable(ChoosePaymentActivity.PAY_PARAMS);
                payNo = bundle.getString(KEY_VALIDE_PAYNUM);
                valideType = bundle.getInt(KEY_VALIDE_TYPE, -1);
                valideNeed = bundle.getString(KEY_VALIDE_NEED);
                bankName = bundle.getString(KEY_VALIDE_BANKNAME);
                cardNum = bundle.getString(KEY_VALIDE_CARDNUM);
                icon = bundle.getInt(KEY_VALIDE_BANKICON, 0);
            }
        }

        //增加字段监控
        domestic_add_number.addTextChangedListener(watcher);
        domestic_add_date.addTextChangedListener(watcher);
        domestic_add_number3.addTextChangedListener(watcher);
        domestic_add_phone.addTextChangedListener(watcher);
        // 如果是加验要素处理，则根据需要加验内容进行显示字段，验证只对显示组件进行校验
        if (valideType != KEY_VALIDE_TYPE0) {
            toolbarTitle.setText(R.string.title_domestic_pay_validate);
            showProtocol(false); //加验要素不显示协议
            //显示加验银行卡信息
            showValideView();
            // 拿到加验需要的要素，对应各个字段展示
            reloadValideField(valideNeed);
        } else {
            toolbarTitle.setText(R.string.title_domestic_cc);
            //添加银行卡界面
            reloadProtocol(); //是否显示协议
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_VALIDE_PAYNUM, payNo);
        outState.putInt(KEY_VALIDE_TYPE, valideType);
        outState.putString(KEY_VALIDE_NEED, valideNeed);
        outState.putString(KEY_VALIDE_BANKNAME, bankName);
        outState.putString(KEY_VALIDE_CARDNUM, cardNum);
        outState.putInt(KEY_VALIDE_BANKICON, icon);
    }

    /**
     * 显示加验银行卡信息
     */
    private void showValideView() {
        // 加验银行卡信息显示
        if (valideLayout != null) {
            valideLayout.setVisibility(View.VISIBLE);
            domestic_valide_img.setImageResource(icon);
            domestic_valide_name.setText(bankName);
            domestic_valide_no.setText(cardNum);
        }
    }

    /**
     * 加验要素界面显示
     */
    private void reloadValideField(String valideNeed) {
        String[] needField = valideNeed.split(",");
        hideFieldAll(); //隐藏所有字段
        for (String str : needField) {
            valideHideField(str);
        }
    }

    private void hideFieldAll() {
        domestic_add_line3.setVisibility(View.GONE);
        domestic_layout3.setVisibility(View.GONE);
        domestic_add_line2.setVisibility(View.GONE);
        domestic_layout2.setVisibility(View.GONE);
        domestic_add_line6.setVisibility(View.GONE);
        domestic_layout6.setVisibility(View.GONE);
        domestic_add_line1.setVisibility(View.GONE);
        domestic_layout1.setVisibility(View.GONE);
    }

    /**
     * 根据字段隐藏指定输入框
     *
     * @param field
     */
    private void valideHideField(String field) {
        //CVV(1, "cvv2"), AVALIDDATE(2, "avalidDate"), NAME(3, "name"), PHONE(4, "phone"), CREDCODE(5, "credCode"), CARDNO(6, "cardNo");
        switch (field) {
            case "1":
                //cvv2 安全码
                domestic_add_line3.setVisibility(View.VISIBLE);
                domestic_layout3.setVisibility(View.VISIBLE);
                break;
            case "2":
                //avalidDate 有效期
                domestic_add_line2.setVisibility(View.VISIBLE);
                domestic_layout2.setVisibility(View.VISIBLE);
                break;
            case "4":
                //phone 手机号
                domestic_add_line6.setVisibility(View.VISIBLE);
                domestic_layout6.setVisibility(View.VISIBLE);
                break;
            case "6":
                //cardNo 卡号
                domestic_add_line1.setVisibility(View.GONE);
                domestic_layout1.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 是否显示协议
     * 1. 如果支付金额小于5万，则显示支付协议默认选中
     * 2. 如果支付金额大于等于5万，则不显示支付金额，协议不选中
     */
    private void reloadProtocol() {
        showProtocol(requestParams != null && requestParams.shouldPay <= MAX_PRICE);
    }

    /**
     * 隐藏协议
     */
    private void showProtocol(boolean isShow) {
        domesticCaddCheck.setVisibility(isShow ? View.VISIBLE : View.GONE);
        domesticProtocol.setVisibility(isShow ? View.VISIBLE : View.GONE);
        domesticCaddCheck.setChecked(isShow);
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
            if (isFactor()) {
                submitBtn.setEnabled(checkFactor()); //加验要素验证按钮控制
            } else {
                submitBtn.setEnabled(checkContent()); //验证通过启用按钮
            }
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
                if (selectYear != 0) {
                    calendar.set(Calendar.YEAR, selectYear);
                }
                if (selectMonth != 0) {
                    calendar.set(Calendar.MONTH, selectMonth - 1);
                }
                DatePickerYearDialog dialog = new DatePickerYearDialog(this, 0, new DatePickerYearDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth) {
                        selectYear = startYear;
                        selectMonth = startMonthOfYear + 1;
                        domestic_add_date.setText(String.format(getString(R.string.domestic_add_card_month), PriceFormat.month2(selectMonth), String.valueOf(selectYear)));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setTitle(R.string.domestic_date_dialog_title);
                dialog.show();
                break;
            case R.id.domestic_credit_add_next:
                //点击下一步，首次支付进行绑定卡操作
                switch (valideType) {
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
        //银行卡号
        if (domestic_layout1.getVisibility() == View.VISIBLE) {
            String number = domestic_add_number.getText().toString().trim();
            if (TextUtils.isEmpty(number)) {
                return false;
            }
        }
        //有效期
        if (domestic_layout2.getVisibility() == View.VISIBLE) {
            String date = domestic_add_date.getText().toString().trim();
            if (TextUtils.isEmpty(date)) {
                return false;
            }
        }
        //安全码
        if (domestic_layout3.getVisibility() == View.VISIBLE) {
            String cvv = domestic_add_number3.getText().toString().trim();
            if (TextUtils.isEmpty(cvv)) {
                return false;
            }
        }
        //预留的手机号
        if (domestic_layout6.getVisibility() == View.VISIBLE) {
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
                    domestic_add_phone.getText().toString().trim(),
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
            //卡号
            String number = "";
            if (domestic_layout1.getVisibility() == View.VISIBLE) {
                number = domestic_add_number.getText().toString().trim();
            }
            //有效期
            String year = "";
            String month = "";
            if (domestic_layout2.getVisibility() == View.VISIBLE) {
                year = String.valueOf(selectYear);
                month = PriceFormat.month2(selectMonth);
            }
            //安全码
            String cvv = "";
            if (domestic_layout3.getVisibility() == View.VISIBLE) {
                cvv = domestic_add_number3.getText().toString().trim();
            }
            //预留的手机号
            String phone = "";
            if (domestic_layout6.getVisibility() == View.VISIBLE) {
                phone = domestic_add_phone.getText().toString().trim();
            }
            RequestEposCheckFactor request = new RequestEposCheckFactor(this, payNo, phone, number, year, month, cvv);
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
                payNo = eposFirstPay.payNo; //绑定新卡的payNo复制
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

    public void gotoSuccess() {
        Intent intentSuccess = new Intent(this, PayResultActivity.class);
        PayResultActivity.Params params1 = new PayResultActivity.Params();
        params1.orderId = requestParams.orderId;
        params1.orderType = requestParams.orderType;
        params1.apiType = requestParams.apiType;
        params1.payResult = true;
        params1.extarParamsBean = requestParams.extarParamsBean;
        intentSuccess.putExtra(Constants.PARAMS_DATA, params1);
        startActivity(intentSuccess);
        finish(); //支付成功关闭当前界面
    }

    /**
     * 新添加信用卡支付结果处理
     *
     * @param eposFirstPay
     */
    private void doFirstPay(EposFirstPay eposFirstPay) {
        if (eposFirstPay == null) return;
        if (!TextUtils.isEmpty(eposFirstPay.eposPaySubmitStatus)) {
            switch (eposFirstPay.eposPaySubmitStatus) {
                case "1":
                    //提交成功
                    gotoSuccess();
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
                    intent.putExtra(KEY_VALIDE_NEED, eposFirstPay.needVaildFactors);
                    intent.putExtra(KEY_VALIDE_CARDNUM, domestic_add_number.getText().toString().trim());
                    intent.putExtra(KEY_VALIDE_PAYNUM, eposFirstPay.payNo);
                    startActivity(intent);
                    break;
                case "4":
                    //短信验证
                    // 首次绑卡，不知道卡icon
                    String number = domestic_add_number.getText().toString().trim();
                    domesticOldPayView.show(eposFirstPay.payNo, 0, "", number, PriceFormat.price(requestParams.shouldPay));
                    break;
                case "5":
                    //加验要素+短信验证
                    Intent intents = new Intent(this, DomesticCreditCAddActivity.class);
                    intents.putExtra(PAY_PARAMS, requestParams);
                    intents.putExtra(KEY_VALIDE_TYPE, KEY_VALIDE_TYPE2);
                    intents.putExtra(KEY_VALIDE_NEED, eposFirstPay.needVaildFactors);
                    intents.putExtra(KEY_VALIDE_CARDNUM, domestic_add_number.getText().toString().trim());
                    intents.putExtra(KEY_VALIDE_PAYNUM, eposFirstPay.payNo);
                    startActivity(intents);
                    break;
            }
        } else {
            //新卡下一步未知错误弹框，关闭当前页面
            showSysAlert(eposFirstPay);
        }
    }

    /**
     * 如果是加验要素则关闭当前加验要素窗口
     */
    public void closeOfValide() {
        if (isFactor()) {
            finish();
        }
    }

    /**
     * 判断是否加验要素界面
     *
     * @return
     */
    private boolean isFactor() {
        return valideType == KEY_VALIDE_TYPE1 || valideType == KEY_VALIDE_TYPE2;
    }

    /**
     * 加验要素请求结果处理
     *
     * @param eposPayFactor
     */
    private void doPayFactor(EposFirstPay eposPayFactor) {
        //加验要素成功之后进行短信校验
        if (eposPayFactor == null) return;
        if (!TextUtils.isEmpty(eposPayFactor.eposPaySubmitStatus)) {
            switch (eposPayFactor.eposPaySubmitStatus) {
                case "1":
                    //成功进入下一步
                    doPayFactorSms(eposPayFactor);
                    break;
                case "7":
                    //加验失效
                    ToastUtils.showToast(this, eposPayFactor.errorMsg);
                    finish();
                    break;
                default:
                    //失败弹出错误提示
                    ToastUtils.showToast(this, eposPayFactor.errorMsg);
                    break;
            }
        } else {
            showSysAlert(eposPayFactor);
        }
    }

    /**
     * 加验要素成功后判断是否验证短信验证码
     *
     * @param eposPayFactor
     */
    private void doPayFactorSms(EposFirstPay eposPayFactor) {
        /*
        此结果是加验要素结果,只有两种可能
        1. 只有加验要素，进入支付成功界面
        2. 加验短信验证码，则弹出短信验证码界面
         */
        switch (valideType) {
            case KEY_VALIDE_TYPE1:
                // 只验证要素，这里进入支付成功界面
                gotoSuccess();
                break;
            case KEY_VALIDE_TYPE2:
                //验证要素和验证码
                domesticOldPayView.show(payNo, icon, bankName, cardNum, PriceFormat.price(requestParams.shouldPay));
                break;
        }
    }

    /**
     * 弹出系统级错误
     *
     * @param result
     */
    private void showSysAlert(EposFirstPay result) {
        //银行其他错误
        new AlertDialog.Builder(this).setMessage(result.errorMsg).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }
}
