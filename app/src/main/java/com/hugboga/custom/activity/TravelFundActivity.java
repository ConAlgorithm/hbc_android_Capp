package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.TravelFundData;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestTravelFundExpenseLog;
import com.hugboga.custom.data.request.RequestTravelFundHome;
import com.hugboga.custom.data.request.RequestTravelFundIncomeLog;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.HbcLoadingMoreFooter;
import com.hugboga.custom.widget.LoadMoreRecyclerView;
import com.hugboga.custom.widget.TravelFundHeaderView;
import com.hugboga.custom.widget.TravelFundItemView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created on 16/8/4.TravelFund
 */
public class TravelFundActivity extends BaseActivity implements LoadMoreRecyclerView.LoadingListener, TravelFundHeaderView.OnSwitchRecordListener {

    @BindView(R.id.tracel_fund_titler)
    RelativeLayout titlerBar;
    @BindView(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @BindView(R.id.view_bottom)
    View titlerBottomLine;
    @BindView(R.id.tracel_fund_recyclerview)
    LoadMoreRecyclerView recyclerView;

    private TravelFundData travelFundData;
    private HbcRecyclerSingleTypeAdpater adapter;
    private TravelFundHeaderView travelFundHeaderView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_travel_fund;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        initView();
        setSensorsPageViewEvent("旅游基金", SensorsConstant.TRAVELFOUND);
    }

    private void initView() {
        initTitleBar();

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFootView(new HbcLoadingMoreFooter(this));
        recyclerView.setLoadingListener(this);
        adapter = new HbcRecyclerSingleTypeAdpater(this, TravelFundItemView.class);
        recyclerView.setAdapter(adapter);

        travelFundHeaderView = new TravelFundHeaderView(TravelFundActivity.this);
        travelFundHeaderView.setOnSwitchRecordListener(this);
        adapter.addHeaderView(travelFundHeaderView);
        sendRequest();
    }

    private void initTitleBar() {
        initDefaultTitleBar();
        titlerBottomLine.setVisibility(View.GONE);
        titlerBar.setBackgroundColor(0xFFFFAA74);
        fgTitle.setTextColor(0xFFFFFFFF);
        fgTitle.setText(getString(UserEntity.getUser().isProxyUser(this) ? R.string.travel_fund_title2 : R.string.travel_fund_title));
        fgRightTV.setVisibility(View.VISIBLE);
        fgRightTV.setText(getString(R.string.travel_fund_rule));
        fgRightTV.setTextColor(0xFFFFFFFF);
        fgRightTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobClickUtils.onEvent(StatisticConstant.LAUNCH_FOUNDREGUL);
                SensorsUtils.onAppClick(getEventSource(), "使用规则", getIntentSource());
                Intent intent = new Intent(TravelFundActivity.this, WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, isProxyUser() ? UrlLibs.H5_RAVEL_FUND_RULE_AGENTS : UrlLibs.H5_RAVEL_FUND_RULE);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                startActivity(intent);
            }
        });
        headerLeftBtn.setImageResource(R.mipmap.top_back_white2);
        headerLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventAction(EventType.SETTING_BACK));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new EventAction(EventType.SETTING_BACK));
        finish();
    }

    private void sendRequest() {
        RequestTravelFundHome request = new RequestTravelFundHome(TravelFundActivity.this);
        requestData(request);
    }

    private void sendRequestTravelFundExpenseLog(int offset, boolean isShowLoading) {
        RequestTravelFundExpenseLog requestTravelFundExpenseLog = new RequestTravelFundExpenseLog(TravelFundActivity.this, offset);
        requestData(requestTravelFundExpenseLog, isShowLoading);
    }

    private void sendRequestTravelFundIncomeLog(int offset, boolean isShowLoading) {
        RequestTravelFundIncomeLog requestTravelFundIncomeLog = new RequestTravelFundIncomeLog(TravelFundActivity.this, offset);
        requestData(requestTravelFundIncomeLog, isShowLoading);
    }

    @Override
    public void onDataRequestSucceed(final BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestTravelFundHome) {
            RequestTravelFundHome request = (RequestTravelFundHome) _request;
            TravelFundData _travelFundData = request.getData();
            if (_travelFundData == null) {
                return;
            }
            travelFundData = _travelFundData;
            travelFundHeaderView.update(travelFundData);
            if (_travelFundData.incomeLogList != null && _travelFundData.incomeTotalCount > 0) {
                travelFundHeaderView.hideEmptyView();
                adapter.addData(_travelFundData.incomeLogList, false);
            } else {
                travelFundHeaderView.showEmptyView();
            }
            if (adapter.getListCount() == 0) {
                recyclerView.loadMoreComplete();
            } else {
                recyclerView.setNoMore(adapter.getListCount() >= _travelFundData.incomeTotalCount);
            }
        } else if (_request instanceof RequestTravelFundExpenseLog) {//使用明细
            RequestTravelFundExpenseLog request = (RequestTravelFundExpenseLog) _request;
            TravelFundData _travelFundData = request.getData();
            if (travelFundData.expenseLogList == null) {
                travelFundData.expenseLogList = new ArrayList<TravelFundData.TravelFundItemBean>();
            }
            travelFundData.expenseTotalCount = _travelFundData.expenseTotalCount;
            travelFundData.expenseLogList.addAll(_travelFundData.expenseLogList);

            if (travelFundHeaderView.isUsed()) {
                int offset = _request.getOffset();
                adapter.addData(_travelFundData.expenseLogList, offset > 0);
                if (_travelFundData.expenseTotalCount == 0) {
                    travelFundHeaderView.showEmptyView();
                    recyclerView.loadMoreComplete();
                } else {
                    travelFundHeaderView.hideEmptyView();
                    recyclerView.setNoMore(adapter.getListCount() >= _travelFundData.expenseTotalCount);
                }
            }
        } else if (_request instanceof RequestTravelFundIncomeLog) {//奖励明细
            RequestTravelFundIncomeLog request = (RequestTravelFundIncomeLog) _request;
            TravelFundData _travelFundData = request.getData();
            if (travelFundData.incomeLogList == null) {
                travelFundData.incomeLogList = new ArrayList<TravelFundData.TravelFundItemBean>();
            }
            travelFundData.incomeTotalCount = _travelFundData.incomeTotalCount;
            travelFundData.incomeLogList.addAll(_travelFundData.incomeLogList);

            if (!travelFundHeaderView.isUsed()) {
                int offset = _request.getOffset();
                adapter.addData(_travelFundData.incomeLogList, offset > 0);
                recyclerView.setNoMore(adapter.getListCount() >= _travelFundData.incomeTotalCount);
            }
        }
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_TRAVELFOUND;
    }

    @Override
    public String getEventSource() {
        if (isProxyUser()) {//代理用户
            return "钱包";
        } else {
            return "旅游基金";
        }
    }

    //是否是代理用户
    private boolean isProxyUser() {
        return UserEntity.getUser().isProxyUser(this);
    }

    @Override
    public void onLoadMore() {
        if (travelFundData == null) {
            return;
        }
        if (travelFundHeaderView.isUsed()) {//使用明细
            sendRequestTravelFundExpenseLog(adapter.getListCount(),false);
        } else {//奖励明细
            sendRequestTravelFundIncomeLog(adapter.getListCount(),false);
        }
    }

    @Override
    public void onSwitchRecord(boolean isUsed) {
        if (isProxyUser()) {
            SensorsUtils.onAppClick(getEventSource(), isUsed ? "支出明细" : "收入明细", getIntentSource());
        } else {
            SensorsUtils.onAppClick(getEventSource(), isUsed ? "使用明细" : "奖励明细", getIntentSource());
        }

        List<TravelFundData.TravelFundItemBean> list = !isUsed ? travelFundData.incomeLogList : travelFundData.expenseLogList;
        if (list != null && list.size() == 0) {
            travelFundHeaderView.showEmptyView();
        } else {
            travelFundHeaderView.hideEmptyView();
        }
        adapter.addData(list, false);
        if (adapter.getListCount() == 0) {
            recyclerView.loadMoreComplete();
        } else {
            recyclerView.setNoMore(adapter.getListCount() >= (!isUsed ? travelFundData.incomeTotalCount : travelFundData.expenseTotalCount));
        }
        recyclerView.smoothScrollToPosition(0);
        if (travelFundData.expenseLogList == null) {
            sendRequestTravelFundExpenseLog(0,true);
        }
    }
}
