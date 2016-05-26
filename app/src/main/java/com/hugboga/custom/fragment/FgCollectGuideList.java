package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CollectGuideAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectGuideBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.request.RequestCollectGuideList;
import com.hugboga.custom.data.request.RequestCollectGuidesFilter;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
import de.greenrobot.event.EventBus;


/**
 * Created by qingcha on 16/5/24.
 */
@ContentView(R.layout.fg_collect_guide_list)
public class FgCollectGuideList extends BaseFragment implements AdapterView.OnItemClickListener {

    @ViewInject(R.id.collect_list_listview)
    ZListView listView;
    @ViewInject(R.id.collect_listview_empty)
    LinearLayout emptyLayout;

    private RequestCollectGuidesFilter.CollectGuidesFilterParams paramsData;
    private CollectGuideAdapter adapter;

    @Override
    protected void initHeader(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            paramsData = (RequestCollectGuidesFilter.CollectGuidesFilterParams) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                paramsData = (RequestCollectGuidesFilter.CollectGuidesFilterParams) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }

        fgTitle.setText(getString(R.string.collect_guide_title));
        listView.setEmptyView(emptyLayout);
        listView.setOnItemClickListener(this);
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    @Override
    protected void initHeader() {}

    @Override
    protected void initView() {}

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
            request = new RequestCollectGuideList(getActivity(), pageIndex);
        } else {
            request = new RequestCollectGuidesFilter(getActivity(), paramsData, pageIndex);
        }
        return requestData(request);
    }

    @Override
    protected Callback.Cancelable requestData() {
        if (adapter != null) {
            adapter = null;
        }
        return runData(0);
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventBus.getDefault().post(new EventAction(EventType.CHOOSE_GUIDE,adapter.getItem(position)));
        finish();
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
                adapter = new CollectGuideAdapter(getActivity());
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
