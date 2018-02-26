package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.DestinationListBean;
import com.hugboga.custom.data.bean.DestinationTabItemBean;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.request.RequestDestinationList;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.HbcLoadingMoreFooter;
import com.hugboga.custom.widget.LoadMoreRecyclerView;
import com.hugboga.custom.widget.city.CitySkuView;
import com.hugboga.custom.widget.title.TitleBar;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class DestinationListActivity extends BaseActivity implements LoadMoreRecyclerView.LoadingListener {

    @BindView(R.id.destination_list_titlebar)
    TitleBar titlebar;
    @BindView(R.id.destination_list_recyclerview)
    LoadMoreRecyclerView recyclerView;
    @BindView(R.id.destination_list_empty)
    LinearLayout emptyView;

    private HbcRecyclerSingleTypeAdpater<DestinationGoodsVo> adapter;
    private DestinationTabItemBean.TagItemBean tagItemBean;
    private DestinationListBean destinationListBean;

    @Override
    public int getContentViewId() {
        return R.layout.activity_destination_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            tagItemBean = (DestinationTabItemBean.TagItemBean) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                tagItemBean = (DestinationTabItemBean.TagItemBean) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        init();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.PARAMS_DATA, tagItemBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        titlebar.setTitle(tagItemBean.tagName);

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFootView(new HbcLoadingMoreFooter(this));
        recyclerView.setLoadingListener(this);
        adapter = new HbcRecyclerSingleTypeAdpater(this, CitySkuView.class);
        recyclerView.setAdapter(adapter);

        requestList(0, true);
    }

    public void requestList(int offset, boolean isShowLoading) {
        RequestDestinationList requestDestinationList = new RequestDestinationList(this, tagItemBean.tagId, offset);
        requestData(requestDestinationList, isShowLoading);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestDestinationList) {
            destinationListBean = ((RequestDestinationList) _request).getData();
            int offset = _request.getOffset();
            adapter.addData(destinationListBean.tagGoodsList, offset > 0);
            if (offset == 0) {
                recyclerView.smoothScrollToPosition(0);
            }
            recyclerView.setNoMore(adapter.getListCount() >= destinationListBean.tagGoodsCount);
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        super.onDataRequestError(errorInfo, _request);
        if (_request instanceof RequestDestinationList) {
            int offset = _request.getOffset();
            if (offset == 0) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.destination_list_empty)
    public void onEmptyRefreshData() {
        requestList(0, true);
    }

    @Override
    public void onLoadMore() {
        requestList(adapter.getListCount(), false);
    }

    @Override
    public String getEventSource() {
        return "玩法标签结果";
    }
}
