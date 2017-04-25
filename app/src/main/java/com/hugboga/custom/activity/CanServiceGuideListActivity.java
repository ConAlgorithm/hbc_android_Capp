package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.net.HttpRequestListener;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.ChooseGuideAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CanServiceGuideBean;
import com.hugboga.custom.data.request.RequestAcceptGuide;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.ChooseGuideUtils;
import com.hugboga.custom.widget.ZListView;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created on 16/9/9.
 */
public class CanServiceGuideListActivity extends BaseActivity implements HttpRequestListener{

    @Bind(R.id.choose_guide_listview)
    ZListView listView;

    private String orderNo;
    private String orderType;

    private ChooseGuideAdapter adapter;
    private List<CanServiceGuideBean.GuidesBean> list;
    private LinearLayout footerLayout;
    private ChooseGuideUtils chooseGuideUtils;
    private int totalSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            orderNo = savedInstanceState.getString(Constants.PARAMS_ORDER_NO);
            orderType = savedInstanceState.getString(Constants.PARAMS_ORDER_TYPE);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                orderNo = bundle.getString(Constants.PARAMS_ORDER_NO);
                orderType = bundle.getString(Constants.PARAMS_ORDER_TYPE);
            }
        }
        setContentView(R.layout.activity_choose_guide);
        ButterKnife.bind(this);

        initView();

        setSensorsDefaultEvent(getEventSource(), SensorsConstant.WAITGLIST);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.PARAMS_ORDER_NO, orderNo);
        outState.putString(Constants.PARAMS_ORDER_TYPE, orderType);
    }

    private void initView() {
        initDefaultTitleBar();
        chooseGuideUtils = new ChooseGuideUtils(this, orderNo, getEventSource());

        footerLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.choose_guide_footer, null);
        footerLayout.setVisibility(View.GONE);
        listView.addFooterView(footerLayout);
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);
        sendRequest(0);
    }

    ZListView.OnRefreshListener onRefreshListener = new ZListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            sendRequest(0);
        }
    };

    ZListView.OnLoadListener onLoadListener = new ZListView.OnLoadListener() {
        @Override
        public void onLoad() {
            int count = adapter.getCount();
            if (count > 0 && count < totalSize) {
                sendRequest(adapter == null ? 0 : adapter.getCount());
            }
        }
    };

    private void sendRequest(int pageIndex) {
        if (pageIndex == 0 && adapter != null) {
            adapter = null;
        }
        RequestAcceptGuide requestAcceptGuide = new RequestAcceptGuide(this, orderNo, Constants.DEFAULT_PAGESIZE, pageIndex);
        HttpRequestUtils.request(this, requestAcceptGuide, this);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestAcceptGuide) {
            CanServiceGuideBean canServiceGuideBean = ((RequestAcceptGuide)request).getData();
            totalSize = canServiceGuideBean.getTotalSize();
            fgTitle.setText(String.format(getString(R.string.choose_guide_title), totalSize));
            list = canServiceGuideBean.getGuides();
            if (list != null) {
                if (adapter == null) {
                    adapter = new ChooseGuideAdapter(activity);
                    listView.setAdapter(adapter);
                    adapter.setList(list);
                    footerLayout.setVisibility(View.VISIBLE);
                } else {
                    adapter.addList(list);
                }
            }
            if (list != null && list.size() < Constants.DEFAULT_PAGESIZE) {
                listView.onLoadCompleteNone();
            } else {
                listView.onLoadComplete();
            }
            listView.onRefreshComplete();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public String getEventId() {
        String str = "";
        switch (orderType) {
            case "3":
                str = "包车游";
                break;
            case "5":
                str = "固定线路";
                break;
            case "6":
                str = "推荐线路";
                break;
        }
        return str;
    }

    @Override
    public String getEventSource() {
        return "表态司导列表";
    }

    public void chooseGuide(CanServiceGuideBean.GuidesBean bean) {
        chooseGuideUtils.chooseGuide(bean);
    }

    public void intentGuideDetail(CanServiceGuideBean.GuidesBean bean) {
//        GuideDetailActivity.Params params = new GuideDetailActivity.Params();
//        params.guideId = bean.getGuideId();
//        params.isSelectedService = true;
//        params.chooseGuide = bean;
//        params.orderNo = orderNo;
//        Intent intent = new Intent(this, GuideDetailActivity.class);
//        intent.putExtra(Constants.PARAMS_DATA, params);
//        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
//        startActivity(intent);
    }
}
