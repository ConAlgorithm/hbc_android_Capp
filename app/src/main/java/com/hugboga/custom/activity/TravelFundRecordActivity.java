package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.TravelFundAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.request.RequestInvitationFriendsLogs;
import com.hugboga.custom.data.request.RequestTravelFundLogs;
import com.hugboga.custom.widget.TravelFundRecordHeaderView;
import com.hugboga.custom.widget.ZListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by on 16/8/8.
 */
public class TravelFundRecordActivity extends BaseActivity{

    public static final int TYPE_USE_Bill = 0;//使用明细
    public static final int TYPE_INVITE_FRIENDS = 1;//邀请明细

    @Bind(R.id.trave_fund_record_listview)
    ZListView listView;
    @Bind(R.id.trave_fund_record_empty_layout)
    LinearLayout emptyLayout;
    @Bind(R.id.trave_fund_record_empty_tv)
    TextView emptyHintTV;
    private TravelFundRecordHeaderView headerView;

    private int type = 0;
    private TravelFundAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            type = savedInstanceState.getInt(Constants.PARAMS_TYPE);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                type = bundle.getInt(Constants.PARAMS_TYPE);
            }
        }

        setContentView(R.layout.activity_travelfund_record);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.PARAMS_TYPE, type);
    }

    private void sendRequest(int pageIndex) {
        BaseRequest request = null;
        switch (type) {
            case TYPE_USE_Bill:
                request = new RequestTravelFundLogs(activity, pageIndex);
                break;
            case TYPE_INVITE_FRIENDS:
                request = new RequestInvitationFriendsLogs(activity, pageIndex);
                break;
        }
        requestData(request);
    }

    ZListView.OnLoadListener onLoadListener = new ZListView.OnLoadListener() {
        @Override
        public void onLoad() {
            if (adapter.getCount() > 0) {
                sendRequest(adapter == null ? 0 : adapter.getCount());
            }
        }
    };

    ZListView.OnRefreshListener onRefreshListener = new ZListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (adapter != null) {
                adapter.setList(null);
            }
            sendRequest(0);
        }
    };


    private void initView() {
        initDefaultTitleBar();
        adapter = new TravelFundAdapter(activity);
        adapter.setType(type);
        listView.setAdapter(adapter);
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);

        switch (type) {
            case TYPE_USE_Bill:
                fgTitle.setText(getString(R.string.travel_fund_use_record_title));
                break;
            case TYPE_INVITE_FRIENDS:
                fgTitle.setText(getString(R.string.travel_fund_invite_record_title));
                headerView = new TravelFundRecordHeaderView(this);
                listView.addHeaderView(headerView);
                break;
        }

        sendRequest(0);
    }

    @Override
    public void onDataRequestSucceed(final BaseRequest _request) {
        TravelFundData travelFundData = null;
        ArrayList<TravelFundData.TravelFundBean> listData = null;
        if (_request instanceof RequestTravelFundLogs) {
            RequestTravelFundLogs request = (RequestTravelFundLogs) _request;
            travelFundData = request.getData();
            listData = travelFundData.getListData();
        } else if (_request instanceof RequestInvitationFriendsLogs) {
            RequestInvitationFriendsLogs request = (RequestInvitationFriendsLogs) _request;
            travelFundData = request.getData();
            listData = travelFundData.getLogs();
        }

        if (travelFundData != null) {
            adapter.addList(listData);
            if (listData != null && listData.size() < Constants.DEFAULT_PAGESIZE) {
                listView.onLoadCompleteNone();
            }
        } else {
            listView.onLoadComplete();
        }
        listView.onRefreshComplete();


        if (_request instanceof RequestTravelFundLogs) {//使用明细
            emptyHintTV.setText(getString(R.string.travel_fund_record_empty_hint));
        } else if (_request instanceof RequestInvitationFriendsLogs) {
            headerView.update(travelFundData);//邀请记录
            emptyHintTV.setText(getString(R.string.travel_fund_invitation_friends_record_empty_hint));
        }
        if (adapter != null && adapter.getCount() <= 0) {
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            emptyLayout.setVisibility(View.GONE);
        }
    }
}
