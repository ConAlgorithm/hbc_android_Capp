package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerTypeBaseAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.widget.SkuFilterLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilterSkuListActivity extends BaseActivity implements HbcRecyclerTypeBaseAdpater.OnItemClickListener{

    @Bind(R.id.filter_sku_list_filter_layout)
    SkuFilterLayout filterLayout;
    @Bind(R.id.filter_sku_list_recyclerview)
    RecyclerView mRecyclerView;

    @Bind(R.id.filter_sku_list_empty_layout)
    LinearLayout emptyLayout;
    @Bind(R.id.filter_sku_list_empty_iv)
    ImageView emptyIV;
    @Bind(R.id.filter_sku_list_empty_hint_tv)
    TextView emptyHintTV;

    private FilterSkuListActivity.Params paramsData;

    private CityListActivity.Params cityParams;
    private CityListActivity.CityHomeType lastCityHomeType;//用来判断是否显示当前城市

    public static class Params implements Serializable {
        public int id;
        public CityListActivity.CityHomeType cityHomeType;
        public String titleName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (FilterSkuListActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (FilterSkuListActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.activity_filter_sku_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (hideFilterView()) {
                return true;
            } else {
                return super.onKeyUp(keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private boolean hideFilterView() {
        if (filterLayout.isShowFilterView()) {
            filterLayout.hideFilterView();
            return true;
        } else {
            return false;
        }
    }

    private void initView() {
        initTitleBar();

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        mListView.setLayoutManager(layoutManager);
//        mAdapter = new HbcRecyclerSingleTypeAdpater(this, GuideItemView.class);
//        mAdapter.setOnItemClickListener(this);
//        mListView.setAdapter(mAdapter);
//
//        requestGuideList();
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText("包车线路");
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hideFilterView()) {
                    finish();
                }
            }
        });
        fgRightTV.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(View view, int position, Object itemData) {

    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {}
    }
}
