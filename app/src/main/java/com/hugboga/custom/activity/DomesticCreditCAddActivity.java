package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 添加信用卡
 * Created by HONGBO on 2017/10/23 16:57.
 */
public class DomesticCreditCAddActivity extends BaseActivity {

    @Bind(R.id.header_title)
    TextView toolbarTitle;

    @Override
    public int getContentViewId() {
        return R.layout.activity_domestic_credit_cadd;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(getTitle());
    }

    @OnClick({R.id.header_left_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
        }
    }
}
