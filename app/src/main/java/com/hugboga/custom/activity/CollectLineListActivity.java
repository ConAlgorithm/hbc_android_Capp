package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.adapter.HbcRecyclerTypeBaseAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CollectGuideNewBean;
import com.hugboga.custom.data.bean.CollectLineBean;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestCollectGuideList;
import com.hugboga.custom.data.request.RequestCollectLineList;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.OrderUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.CollectGuideItemView;
import com.hugboga.custom.widget.CollectLinelistItem;
import com.hugboga.custom.widget.HbcLoadingMoreFooter;
import com.hugboga.custom.widget.LineSearchListItem;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;

/**
 * Created by zhangqiang on 17/8/28.
 */

public class CollectLineListActivity extends BaseActivity implements HbcRecyclerTypeBaseAdpater.OnItemClickListener, XRecyclerView.LoadingListener{
    @Bind(R.id.collect_line_list_recyclerview)
    XRecyclerView mRecyclerView;
    @Bind(R.id.collect_line_listview_empty)
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
        mRecyclerView.getEmptyView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollectLineListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, CollectLinelistItem.class);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        requestCollectLineList(0, true);
        //setSensorsDefaultEvent(getEventSource(), SensorsConstant.COLLCTGLIST);
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

    @Override
    public void onItemClick(View view, int position, Object itemData) {
        List<CollectLineBean.CollectLineItemBean> collectLineItemBeanList = mAdapter.getDatas();
        CollectLineBean.CollectLineItemBean collectLineItemBean = collectLineItemBeanList.get(position);
        if (collectLineItemBean == null) {
            return;
        }
        Intent intent = new Intent(activity, SkuDetailActivity.class);
        intent.putExtra(WebInfoActivity.WEB_URL, collectLineItemBean.goodsDetailUrl);
        intent.putExtra(Constants.PARAMS_ID, collectLineItemBean.no);
        //intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        activity.startActivity(intent);
    }
}
