package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.adapter.HbcRecyclerTypeBaseAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectGuideNewBean;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCollectGuideList;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.CollectGuideItemView;
import com.hugboga.custom.widget.HbcLoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;

/**
 * Created by qingcha on 16/8/4.
 */
public class CollectGuideListActivity extends BaseActivity implements HbcRecyclerTypeBaseAdpater.OnItemClickListener, XRecyclerView.LoadingListener{

    @Bind(R.id.collect_list_recyclerview)
    XRecyclerView mRecyclerView;
    @Bind(R.id.collect_listview_empty)
    LinearLayout emptyLayout;

    private HbcRecyclerSingleTypeAdpater<FilterGuideBean> mAdapter;
    private List<FilterGuideBean> guideList;

    @Override
    public int getContentViewId() {
        return R.layout.fg_collect_guide_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initDefaultTitleBar();

        fgTitle.setText(getString(R.string.collect_guide_title));

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setFootView(new HbcLoadingMoreFooter(this));
        mRecyclerView.setLoadingListener(this);
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, CollectGuideItemView.class);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        requestCollectGuideList(0, true);
        setSensorsDefaultEvent(getEventSource(), SensorsConstant.COLLCTGLIST);
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
                requestCollectGuideList(0, true);
                break;
        }
    }

    public void requestCollectGuideList(int offset, boolean isShowLoading) {
        requestData(new RequestCollectGuideList(this, offset), isShowLoading);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCollectGuideList) {
            CollectGuideNewBean collectGuideNewBean = ((RequestCollectGuideList) _request).getData();
            int offset = _request.getOffset();
            if (offset == 0 && (collectGuideNewBean == null || collectGuideNewBean.guideList == null || collectGuideNewBean.count <= 0)) {
                emptyLayout.setVisibility(View.VISIBLE);
                return;
            } else {
                emptyLayout.setVisibility(View.GONE);
            }
            guideList = collectGuideNewBean.guideList;
            mAdapter.addData(guideList, offset > 0);
            if (offset == 0) {
                mRecyclerView.smoothScrollToPosition(0);
            }
            mRecyclerView.refreshComplete();
            mRecyclerView.setNoMore(mAdapter.getListCount() >= collectGuideNewBean.count);
        }
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_COLLCTGLIST;
    }

    @Override
    public String getEventSource() {
        return "收藏司导列表";
    }

    @Override
    public void onRefresh() {
        requestCollectGuideList(0, false);
    }

    @Override
    public void onLoadMore() {
        requestCollectGuideList(mAdapter.getListCount(), false);
    }

    @Override
    public void onItemClick(View view, int position, Object itemData) {
        FilterGuideBean filterGuideBean = guideList.get(position);
        if (filterGuideBean == null || TextUtils.isEmpty(filterGuideBean.getServiceType())) {
            return;
        }
        GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
        params.guideId = filterGuideBean.guideId;
        Intent intent = new Intent(CollectGuideListActivity.this, GuideWebDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }
}
