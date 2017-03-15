package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.widget.DetailTravelItemView;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qingcha on 17/3/16.
 */

public class DetailPassCityListActivity extends BaseActivity{

    @Bind(R.id.detail_pass_city_title_tv)
    TextView titleTV;
    @Bind(R.id.detail_pass_city_recyclerview)
    RecyclerView recyclerView;

    private OrderBean orderBean;

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
        setContentView(R.layout.activity_detail_pass_city);
        ButterKnife.bind(this);

        if (orderBean == null || orderBean.passByCity == null) {
            finish();
        }
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.PARAMS_DATA, orderBean);
    }

    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        HbcRecyclerSingleTypeAdpater mAdapter = new HbcRecyclerSingleTypeAdpater(this, DetailTravelItemView.class);
        recyclerView.setAdapter(mAdapter);
        List<CityBean> passByCityList = orderBean.passByCity;
        int size = passByCityList.size();
        for (int i = 0; i < size; i++) {
            passByCityList.get(i).index = i;
        }
        mAdapter.addData(passByCityList);
    }

    @OnClick({R.id.detail_pass_city_close_iv})
    public void closeActivity() {
        finish();
    }
}
