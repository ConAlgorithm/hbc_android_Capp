package com.hugboga.custom.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CollectGuideAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCollectGuideList;
import com.hugboga.custom.data.request.RequestCollectGuidesFilter;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.widget.ZListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/8/4.
 */
public class CollectGuideListActivity extends BaseActivity{

    @Bind(R.id.collect_list_listview)
    ZListView listView;
    @Bind(R.id.collect_listview_empty)
    LinearLayout emptyLayout;

    private RequestCollectGuidesFilter.CollectGuidesFilterParams paramsData;
    private CollectGuideAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (RequestCollectGuidesFilter.CollectGuidesFilterParams) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                paramsData = (RequestCollectGuidesFilter.CollectGuidesFilterParams) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        setContentView(R.layout.fg_collect_guide_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initDefaultTitleBar();

        fgTitle.setText(getString(R.string.collect_guide_title));
        listView.setEmptyView(emptyLayout);
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);

        sendRequest();
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

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case ORDER_DETAIL_UPDATE_COLLECT:
                sendRequest();
                break;
        }
    }

    ZListView.OnRefreshListener onRefreshListener = new ZListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (adapter != null) {
                adapter = null;
            }
            runData(0);
        }
    };

    ZListView.OnLoadListener onLoadListener = new ZListView.OnLoadListener() {
        @Override
        public void onLoad() {
            if (adapter.getCount() > 0) {
                runData(adapter == null ? 0 : adapter.getCount());
            }
        }
    };

    private Callback.Cancelable runData(int pageIndex) {
        BaseRequest request = null;
        if (paramsData == null) {
            request = new RequestCollectGuideList(this, pageIndex);
        } else {
            request = new RequestCollectGuidesFilter(this, paramsData, pageIndex);
        }
        return requestData(request);
    }

    private void sendRequest() {
        if (adapter != null) {
            adapter = null;
        }
        runData(0);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        List<CollectGuideBean> list = null;
        if (_request instanceof RequestCollectGuideList) {
            RequestCollectGuideList request = (RequestCollectGuideList) _request;
            list = request.getData();
        } else if (_request instanceof RequestCollectGuidesFilter) {
            RequestCollectGuidesFilter filterRequest = (RequestCollectGuidesFilter) _request;
            list = filterRequest.getData();
        }
        if (list != null) {
            if (adapter == null) {
                adapter = new CollectGuideAdapter(this, paramsData != null);
                if (paramsData != null) {
                    adapter.setShowStatusLayout(false);
                }
                listView.setAdapter(adapter);
                adapter.setList(list);
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

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_COLLCTGLIST;
    }

    @Override
    public String getEventSource() {
        return "收藏司导列表";
    }
}
