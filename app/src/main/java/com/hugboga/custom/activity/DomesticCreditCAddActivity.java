package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.data.bean.epos.EposFirstPay;
import com.hugboga.custom.data.request.RequestEposFirstPay;
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

        submitBtn.setEnabled(true); //验证通过启用按钮
    }

    @OnClick({R.id.header_left_btn, R.id.domestic_layout2, R.id.domestic_credit_add_next})
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
                        domestic_add_date.setText((startMonthOfYear + 1) + "月份／" + startYear + "年");
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setTitle(R.string.domestic_date_dialog_title);
                dialog.show();
                break;
            case R.id.domestic_credit_add_next:
                //点击下一步，首次支付进行绑定卡操作
                firstPay();
                break;
        }
    }

    /**
     * 检查填写内容是否完整
     *
     * @return
     */
    private boolean checkContent() {
        return true;
    }

    /**
     * 首次支付请求
     */
    private void firstPay() {
        if (checkContent()) {
            RequestEposFirstPay request = new RequestEposFirstPay(this, params.orderId, params.shouldPay,
                    params.couponId, domestic_add_number5.getText().toString(), "18637432581",
                    domestic_add_number4.getText().toString(), domestic_add_number.getText().toString(),
                    domestic_add_date.getText().toString(),domestic_add_date.getText().toString(),
                    domestic_add_number3.getText().toString(),domesticCaddCheck.isChecked());
            HttpRequestUtils.request(this, request, this);
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestEposFirstPay) {
            //首次信用卡支付
            EposFirstPay data = ((RequestEposFirstPay) request).getData();

        }
    }
}
