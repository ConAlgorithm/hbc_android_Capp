package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.ToastUtils;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.DomesticCCAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.epos.EposBindCard;
import com.hugboga.custom.data.bean.epos.EposBindList;
import com.hugboga.custom.data.bean.epos.EposFirstPay;
import com.hugboga.custom.data.request.RequestEposBindList;
import com.hugboga.custom.data.request.RequestEposBindPay;
import com.hugboga.custom.utils.PriceFormat;
import com.hugboga.custom.widget.domesticcc.DomesticHeadView;
import com.hugboga.custom.widget.domesticcc.DomesticOldPayView;
import com.hugboga.custom.widget.domesticcc.DomesticPayOkView;

import butterknife.Bind;
import butterknife.OnClick;

import static com.hugboga.custom.activity.ChoosePaymentActivity.PAY_PARAMS;
import static com.hugboga.custom.activity.DomesticCreditCAddActivity.KEY_VALIDE_BANKICON;
import static com.hugboga.custom.activity.DomesticCreditCAddActivity.KEY_VALIDE_BANKNAME;
import static com.hugboga.custom.activity.DomesticCreditCAddActivity.KEY_VALIDE_CARDNUM;
import static com.hugboga.custom.activity.DomesticCreditCAddActivity.KEY_VALIDE_NEED;
import static com.hugboga.custom.activity.DomesticCreditCAddActivity.KEY_VALIDE_PAYNUM;
import static com.hugboga.custom.activity.DomesticCreditCAddActivity.KEY_VALIDE_TYPE;
import static com.hugboga.custom.activity.DomesticCreditCAddActivity.KEY_VALIDE_TYPE0;
import static com.hugboga.custom.activity.DomesticCreditCAddActivity.KEY_VALIDE_TYPE1;
import static com.hugboga.custom.activity.DomesticCreditCAddActivity.KEY_VALIDE_TYPE2;

/**
 * 国内信用卡列表
 * Created by HONGBO on 2017/10/23 11:55.
 */
public class DomesticCreditCardActivity extends BaseActivity implements DomesticCCAdapter.OnItemClickListener {

    public static final int MAX_PRICE = 50000; //区分消费渠道和绑卡渠道

    @Bind(R.id.header_title)
    TextView toolbarTitle;
    @Bind(R.id.domestic_list)
    RecyclerView listView;
    @Bind(R.id.domestic_totle)
    DomesticHeadView domesticHeadView; //国内信用卡列表，还需支付部分
    @Bind(R.id.domestic_pay_layout)
    DomesticOldPayView domesticOldPayView; //验证码验证
    @Bind(R.id.domestic_pay_ok_layout)
    DomesticPayOkView domesticPayOkView; //历史卡支付确认

    ChoosePaymentActivity.RequestParams requestParams;

    DomesticCCAdapter adapter;

    EposBindCard ebc; //选择的历史卡

    @Override
    public int getContentViewId() {
        return R.layout.activity_domestic_credit_c1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarTitle.setText(getTitle());
        if (savedInstanceState != null) {
            requestParams = (ChoosePaymentActivity.RequestParams) savedInstanceState.getSerializable(PAY_PARAMS);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                requestParams = (ChoosePaymentActivity.RequestParams) bundle.getSerializable(PAY_PARAMS);
            }
        }
        domesticHeadView.init(PriceFormat.price(requestParams.shouldPay));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(manager);
        listView.setNestedScrollingEnabled(false);
        loadData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (requestParams != null) {
            outState.putSerializable(PAY_PARAMS, requestParams);
        }
    }

    /**
     * 加载信用卡数据
     */
    private void loadData() {
         /*
        国内信用卡支付区分大于5万和小于5万
        1. 小于5万显示历史卡界面，可以走新卡界面，显示绑定协议
        2. 大于5万不加载历史卡界面，只能走新卡界面，消费支付，不显示绑定协议
         */
        if (requestParams != null && requestParams.shouldPay <= MAX_PRICE) {
            // 查询显示历史卡API
            RequestEposBindList request = new RequestEposBindList(this);
            HttpRequestUtils.request(this, request, this);
        }
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
                intent.putExtra(PAY_PARAMS, requestParams);
                intent.putExtra(KEY_VALIDE_TYPE, KEY_VALIDE_TYPE0);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        //点击历史卡进行支付，需要验证码和不需要验证码版本
        if (adapter != null && adapter.getData() != null && adapter.getData().size() > 0) {
            ebc = adapter.getData().get(position);
            if (ebc != null) {
                domesticPayOkView.show(ebc.bindId, ebc.getBankIconId(), ebc.bankName, ebc.cardNo, PriceFormat.price(requestParams.shouldPay));
            }
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestEposBindList) {
            //查询绑定历史卡列表
            EposBindList data = (EposBindList) request.getData();
            if (data != null && data.bindList != null && data.bindList.size() > 0) {
                adapter = new DomesticCCAdapter(data.bindList);
                adapter.setOnItemClickListener(this);
                listView.setAdapter(adapter);
            }
        } else if (request instanceof RequestEposBindPay) {
            //已绑定历史卡进行支付
            oldPayResult(((RequestEposBindPay) request).getData());
        }
    }

    public void gotoSuccess() {
        Intent intentSuccess = new Intent(this, PayResultActivity.class);
        PayResultActivity.Params params1 = new PayResultActivity.Params();
        params1.orderId = requestParams.orderId;
        params1.orderType = requestParams.orderType;
        params1.apiType = requestParams.apiType;
        params1.payResult = true;
        intentSuccess.putExtra(Constants.PARAMS_DATA, params1);
        startActivity(intentSuccess);
        finish(); //支付成功关闭当前界面
    }

    /**
     * 历史卡支付结果处理
     *
     * @param data
     */
    private void oldPayResult(EposFirstPay data) {
        domesticPayOkView.close();
        switch (data.eposPaySubmitStatus) {
            case "1":
                //成功
                gotoSuccess();
                break;
            case "2":
                //失败
                ToastUtils.showToast(this, data.errorMsg);
                break;
            case "3":
                //加验要素
                Intent intent = new Intent(this, DomesticCreditCAddActivity.class);
                intent.putExtra(PAY_PARAMS, requestParams);
                intent.putExtra(KEY_VALIDE_TYPE, KEY_VALIDE_TYPE1);
                intent.putExtra(KEY_VALIDE_NEED, data.needVaildFactors);
                intent.putExtra(KEY_VALIDE_BANKICON, ebc.getBankIconId());
                intent.putExtra(KEY_VALIDE_BANKNAME, ebc.bankName);
                intent.putExtra(KEY_VALIDE_CARDNUM, ebc.cardNo);
                intent.putExtra(KEY_VALIDE_PAYNUM, data.payNo);
                startActivity(intent);
                break;
            case "4":
                //短信验证
                if (ebc != null) {
                    domesticOldPayView.show(data.payNo, ebc.getBankIconId(), ebc.bankName, ebc.cardNo, PriceFormat.price(requestParams.shouldPay));
                }
                break;
            case "5":
                //加验要素+短信验证
                Intent intents = new Intent(this, DomesticCreditCAddActivity.class);
                intents.putExtra(PAY_PARAMS, requestParams);
                intents.putExtra(KEY_VALIDE_TYPE, KEY_VALIDE_TYPE2);
                intents.putExtra(KEY_VALIDE_NEED, data.needVaildFactors);
                intents.putExtra(KEY_VALIDE_BANKICON, ebc.getBankIconId());
                intents.putExtra(KEY_VALIDE_BANKNAME, ebc.bankName);
                intents.putExtra(KEY_VALIDE_CARDNUM, ebc.cardNo);
                intents.putExtra(KEY_VALIDE_PAYNUM, data.payNo);
                startActivity(intents);
                break;
        }
    }

    /**
     * 开始支付
     */
    public void payOk(String bindId, View view) {
        // 进入老卡支付流程
        RequestEposBindPay requestEposBindPay = new RequestEposBindPay(this, requestParams.orderId, requestParams.shouldPay, bindId);
        HttpRequestUtils.request(this, requestEposBindPay, this, view);
    }
}
