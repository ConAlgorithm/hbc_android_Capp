package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.hugboga.custom.R;
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
    @Bind(R.id.domestic_add_number2)
    TextView tvDomesticDate; //信用卡有效期
    @Bind(R.id.domestic_pay_dialog)
    DomesticNewPayView payDialog; //支付弹框

    @Override
    public int getContentViewId() {
        return R.layout.activity_domestic_credit_cadd;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(getTitle());
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
                        tvDomesticDate.setText((startMonthOfYear + 1) + "月份／" + startYear + "年");
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.setTitle(R.string.domestic_date_dialog_title);
                dialog.show();
                break;
            case R.id.domestic_credit_add_next:
                //支付弹框
                payDialog.show();
                break;
        }
    }
}
