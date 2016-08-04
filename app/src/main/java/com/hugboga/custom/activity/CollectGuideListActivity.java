package com.hugboga.custom.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CollectGuideAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.request.RequestCollectGuideList;
import com.hugboga.custom.data.request.RequestCollectGuidesFilter;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 16/8/4.
 * adapter
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
                adapter = new CollectGuideAdapter(this);
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
}
