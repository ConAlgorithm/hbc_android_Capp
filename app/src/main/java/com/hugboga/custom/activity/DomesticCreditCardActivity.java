package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.DomesticCCAdapter;
import com.hugboga.custom.widget.domesticcc.DomesticOldPayView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 国内信用卡列表
 * Created by HONGBO on 2017/10/23 11:55.
 */
public class DomesticCreditCardActivity extends BaseActivity implements DomesticCCAdapter.OnItemClickListener {

    @Bind(R.id.header_title)
    TextView toolbarTitle;
    @Bind(R.id.domestic_list)
    RecyclerView listView;
    @Bind(R.id.domestic_pay_layout)
    DomesticOldPayView domesticOldPayView; //老卡支付

    ChoosePaymentActivity.RequestParams params;

    @Override
    public int getContentViewId() {
        return R.layout.activity_domestic_credit_c1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(getTitle());
        params = (ChoosePaymentActivity.RequestParams) getIntent().getSerializableExtra(ChoosePaymentActivity.PAY_PARAMS);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(manager);
        listView.setNestedScrollingEnabled(false);
        loadData();
    }

    /**
     * 加载信用卡数据
     */
    private void loadData() {
        //TODO 查询是否显示历史卡API
        List data = new ArrayList();
        data.add("1");
        data.add("2");
        data.add("3");
        DomesticCCAdapter adapter = new DomesticCCAdapter(data);
        adapter.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }

    @OnClick({R.id.header_left_btn, R.id.domestic_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.domestic_add:
                // 添加信用卡
                Intent intent = new Intent(this, DomesticCreditCAddActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        //点击进行支付，需要验证码和不需要验证码版本
        domesticOldPayView.show();
    }
}
