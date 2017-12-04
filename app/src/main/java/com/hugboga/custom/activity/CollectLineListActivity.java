package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.data.bean.CollectLineBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCollectLineList;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.CollectLinelistItem;
import com.hugboga.custom.widget.HbcLoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangqiang on 17/8/28.
 */

public class CollectLineListActivity extends BaseActivity implements XRecyclerView.LoadingListener{
    @BindView(R.id.collect_line_list_recyclerview)
    XRecyclerView mRecyclerView;
    @BindView(R.id.collect_line_listview_empty)
    LinearLayout emptyLayout;
    private HbcRecyclerSingleTypeAdpater<CollectLineBean.CollectLineItemBean> mAdapter;
    @Override
    public int getContentViewId() {
        return R.layout.fg_collect_line_list;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initDefaultTitleBar();

        fgTitle.setText(getString(R.string.collect_line_title));

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setFootView(new HbcLoadingMoreFooter(this));
        mRecyclerView.setLoadingListener(this);
        mRecyclerView.setEmptyView(emptyLayout);
        mRecyclerView.getEmptyView().findViewById(R.id.next_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollectLineListActivity.this, MainActivity.class);
                startActivity(intent);
                EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
            }
        });
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, CollectLinelistItem.class);
        mRecyclerView.setAdapter(mAdapter);
        requestCollectLineList(0, true);
        //setSensorsPageViewEvent(getEventSource(), SensorsConstant.COLLCTGLIST);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case LINE_UPDATE_COLLECT:
                requestCollectLineList(0, true);
                break;
        }
    }

    public void requestCollectLineList(int offset, boolean isShowLoading) {
        requestData(new RequestCollectLineList(this, offset), isShowLoading);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCollectLineList) {
            CollectLineBean collectLineBean = (CollectLineBean)_request.getData();
            int offset = _request.getOffset();
            List<CollectLineBean.CollectLineItemBean> collectLineItemBeanList = collectLineBean.goodsList;
            mAdapter.addData(collectLineItemBeanList, offset > 0);
            if (offset == 0) {
                mRecyclerView.smoothScrollToPosition(0);
            }
            mRecyclerView.refreshComplete();
            mRecyclerView.setNoMore(mAdapter.getListCount() >= collectLineBean.count);
        }
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_COLLCTGLIST;
    }

    @Override
    public String getEventSource() {
        return "已收藏司导";
    }

    @Override
    public void onRefresh() {
        requestCollectLineList(0, false);
    }

    @Override
    public void onLoadMore() {
        requestCollectLineList(mAdapter.getListCount(), false);
    }

}
