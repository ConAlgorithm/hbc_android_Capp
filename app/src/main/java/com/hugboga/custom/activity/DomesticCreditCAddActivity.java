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
import com.hugboga.custom.data.bean.epos.EposFirstPay;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestEposFirstPay;
import com.hugboga.custom.utils.PriceFormat;
import com.hugboga.custom.widget.DatePickerYearDialog;
import com.hugboga.custom.widget.domesticcc.DomesticNewPayView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 添加信用卡
 * Created by HONGBO on 2017/10/23 16:57.
 */
public class DomesticCreditCAddActivity extends BaseActivity {

    @Bind(R.id.header_title)
    TextView toolbarTitle;
    @Bind(R.id.domestic_pay_dialog)
    DomesticNewPayView payDialog; //支付弹框

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
    @Bind(R.id.domestic_credit_add_next)
    Button submitBtn; //下一步按钮
    @Bind(R.id.domesticProtocol)
    TextView domesticProtocol; //协议

    private int selectYear;
    private int selectMonth;

    ChoosePaymentActivity.RequestParams params;

    @Override
    public int getContentViewId() {
        return R.layout.activity_domestic_credit_cadd;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(getTitle());
        params = (ChoosePaymentActivity.RequestParams) getIntent().getSerializableExtra(ChoosePaymentActivity.PAY_PARAMS);
        reloadProtocol(); //是否显示协议
        //增加字段监控
        domestic_add_number.addTextChangedListener(watcher);
        domestic_add_date.addTextChangedListener(watcher);
        domestic_add_number3.addTextChangedListener(watcher);
        domestic_add_number4.addTextChangedListener(watcher);
        domestic_add_number5.addTextChangedListener(watcher);
    }

    /**
     * 是否显示协议
     * 1. 如果支付金额小于5万，则显示支付协议默认选中
     * 2. 如果支付金额大于等于5万，则不显示支付金额，协议不选中
     */
    private void reloadProtocol() {
        if (params != null && params.shouldPay < 50000) {
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
                firstPay();
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
        return true;
    }

    /**
     * 首次支付请求
     */
    private void firstPay() {
        if (checkContent()) {
            RequestEposFirstPay request = new RequestEposFirstPay(this, params.orderId, params.shouldPay,
                    params.couponId, domestic_add_number5.getText().toString(), "18637432581", //TODO 手机号后输入
                    domestic_add_number4.getText().toString(), domestic_add_number.getText().toString(),
                    String.valueOf(selectYear), PriceFormat.month2(selectMonth),
                    domestic_add_number3.getText().toString(), domesticCaddCheck.isChecked());
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
                switch (eposFirstPay.eposPaySubmitStatus) {
                    case "1":
                        //提交成功
                        break;
                    case "2":
                        //提交失败
                        ToastUtils.showToast(this, eposFirstPay.errorMsg);
                        break;
                    case "3":
                        //加验要素
                        break;
                    case "4":
                        //短信验证
                        break;
                    case "5":
                        //加验要素及短信验证
                        break;
                }
            }
        }
    }
}
