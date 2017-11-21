package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FgInsureInfoAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.InsureListBean;
import com.hugboga.custom.data.bean.InsureSearchBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestInsuranceSearch;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by on 16/8/4.
 */
public class InsureInfoActivity extends BaseActivity {

    @BindView(R.id.insure_info_listview)
    ListView listView;
    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.header_right_btn)
    ImageView headerRightBtn;
    @BindView(R.id.header_title)
    TextView headerTitle;
    @BindView(R.id.header_right_txt)
    TextView headerRightTxt;

    @BindView(R.id.insure_info_add_layout)
    RelativeLayout addLayout;
    @BindView(R.id.insure_info_add_hint_tv)
    TextView addHintTV;
    @BindView(R.id.insure_info_add_tv)
    TextView addTV;

    private FgInsureInfoAdapter adapter;
    private OrderBean orderBean;

    public boolean isUpdateOrderDetail = false;
    public boolean isRefreshInsuranceList = false;

    @Override
    public int getContentViewId() {
        return R.layout.fg_insure_info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            orderBean = (OrderBean) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                orderBean = (OrderBean) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isRefreshInsuranceList) {
            requestInsuranceSearch();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (orderBean != null) {
            outState.putSerializable(Constants.PARAMS_DATA, orderBean);
        }
    }

    private void initView() {
        initDefaultTitleBar();
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdateOrderDetail) {
                    EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_INFO, orderBean.orderNo));
                }
                finish();
            }
        });
        headerTitle.setText(getString(R.string.insure_info_title));
        setData(orderBean.insuranceMap);
    }

    public void setData(List<List<InsureListBean>> list) {
        if (adapter == null) {
            adapter = new FgInsureInfoAdapter(this);
            listView.setAdapter(adapter);
        }
        adapter.setList(list);
        final int size = list.size();
        final int travelerCount = orderBean.getTravelerCount();
        if (orderBean.insuranceEnable && size < travelerCount) {
            isRefreshInsuranceList = true;
            addLayout.setVisibility(View.VISIBLE);
            addHintTV.setText(String.format("还可添加%1$s个投保人","" + (travelerCount - size)));
        } else {
            addLayout.setVisibility(View.GONE);
        }
    }

    public void requestInsuranceSearch() {
        if (orderBean == null) {
            return;
        }
        requestData(new RequestInsuranceSearch(this, orderBean.orderNo));
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestInsuranceSearch) {
            InsureSearchBean insureSearchBean = ((RequestInsuranceSearch)_request).getData();
            setData(insureSearchBean.insuranceMap);
        }
    }

    @OnClick({R.id.insure_info_add_tv})
    public void addInsure() {
        Bundle insureBundle = new Bundle();
        insureBundle.putSerializable("orderBean", orderBean);
        Intent intent = new Intent(InsureInfoActivity.this, InsureActivity.class);
        intent.putExtras(insureBundle);
        InsureInfoActivity.this.startActivity(intent);
        isUpdateOrderDetail = true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (isUpdateOrderDetail) {
                EventBus.getDefault().post(new EventAction(EventType.ORDER_DETAIL_UPDATE_INFO, orderBean.orderNo));
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
