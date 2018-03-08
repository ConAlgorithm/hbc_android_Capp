package com.hugboga.custom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.activity.WebInfoActivity;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.adapter.HbcRecyclerTypeBaseAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.TravelListAllBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.parser.ParserTravel;
import com.hugboga.custom.data.request.RequestOrderListAll;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.sensors.SensorsUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.TravelListItem;
import com.hugboga.custom.widget.TravelLoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.common.Callback;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangqiang on 17/6/26.
 */

public class TravelListAll extends FgBaseTravel {

    @BindView(R.id.travel_recyclerview)
    XRecyclerView mXRecyclerView;
    @BindView(R.id.list_empty_all)
    RelativeLayout emptyView;
    @BindView(R.id.travel_footer_get_layout)
    LinearLayout footerGet;
    @BindView(R.id.travel_footer_text_layout)
    TextView textView;
    int refreshOrNot = 1;
    protected HbcRecyclerSingleTypeAdpater<OrderBean> hbcRecyclerSingleTypeAdpater;
    private TravelLoadingMoreFooter travelLoadingMoreFooter;

    @Override
    protected void loadData() {
        refreshOrNot = 2;
        runData(0, 0, 10);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.travel_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initViews() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(getContext());
        mXRecyclerView.setLayoutManager(layoutManager);
        hbcRecyclerSingleTypeAdpater = new HbcRecyclerSingleTypeAdpater(getContext(), TravelListItem.class);
        mXRecyclerView.setAdapter(hbcRecyclerSingleTypeAdpater);
        travelLoadingMoreFooter = new TravelLoadingMoreFooter(getContext());
        travelLoadingMoreFooter.setCustomlayout(inflater);
        mXRecyclerView.setFootView(travelLoadingMoreFooter);
        mXRecyclerView.setEmptyView(emptyView);
        footerGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WebInfoActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_INVITE_FRIEND);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                getContext().startActivity(intent);
                MobClickUtils.onEvent(StatisticConstant.CLICK_TRAVELFOUND_XC);
                SensorsUtils.onAppClick(getEventSource(), "旅游基金-点击领取", "");
            }
        });
        //hbcRecyclerSingleTypeAdpater.addFooterView(getFooterView(inflater));
        hbcRecyclerSingleTypeAdpater.setOnItemClickListener(new HbcRecyclerTypeBaseAdpater.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object itemData) {
                OrderBean bean = (OrderBean) itemData;
                if (bean != null) {
                    OrderDetailActivity.Params params = new OrderDetailActivity.Params();
                    params.orderType = bean.orderType;
                    params.orderId = bean.orderNo;
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                    getActivity().startActivity(intent);
                }
            }
        });
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshOrNot = 1;
                runData(0, 0, 10);
            }

            @Override
            public void onLoadMore() {
                refreshOrNot = 2;
                if (hbcRecyclerSingleTypeAdpater.getListCount() > 0) {
                    runData(0, hbcRecyclerSingleTypeAdpater == null ? 0 : hbcRecyclerSingleTypeAdpater.getListCount(), 10);
                }
            }
        });
    }


    public Callback.Cancelable runData(int orderShowType, int pageIndex, int pageSize) {
        BaseRequest request = new RequestOrderListAll(getActivity(), orderShowType, pageSize, pageIndex);
        return HttpRequestUtils.request(getActivity(), request, this, true);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        TravelListAllBean travelListAllBean = (TravelListAllBean) request.getData();
        textView.setText("".equals(travelListAllBean.inviteContent) ? getResources().getText(R.string.travel_footer_fund_content) : travelListAllBean.inviteContent);
        mXRecyclerView.setEmptyView(emptyView);
        if (request != null && request.getOffset() == 0) {
            mXRecyclerView.smoothScrollToPosition(0);
        }
        if (mXRecyclerView != null && travelListAllBean != null) {
            travelLoadingMoreFooter.setFooterContent(travelListAllBean.inviteContent);

            if (hbcRecyclerSingleTypeAdpater != null) {
                hbcRecyclerSingleTypeAdpater.addData(travelListAllBean.resultBean, request.getOffset() > 0);
            }

            if (refreshOrNot == 1) {
                mXRecyclerView.refreshComplete();
            } else if (refreshOrNot == 2) {
                mXRecyclerView.loadMoreComplete();
            }
            if (hbcRecyclerSingleTypeAdpater != null) {
                mXRecyclerView.setNoMore(hbcRecyclerSingleTypeAdpater.getListCount() >= travelListAllBean.totalSize);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("travelListAllBean", travelListAllBean);
            bundle.putInt("requestType", ParserTravel.AllLISTT);
            EventBus.getDefault().post(new EventAction(EventType.TRAVEL_LIST_NUMBER, bundle));

        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (request.getOffset() == 0 && mXRecyclerView != null) {
            mXRecyclerView.smoothScrollToPosition(0);
        }
        if (mXRecyclerView != null) {
            mXRecyclerView.refreshComplete();
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {
        super.onDataRequestCancel(request);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        MLog.e(this + " onEventMainThread " + action.getType());
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                refreshOrNot = 1;
                runData(0, 0, 10);
                break;
        }
    }

    @OnClick(R.id.travel_empty_btn)
    public void onStartTravel() {
        EventBus.getDefault().post(new EventAction(EventType.SET_MAIN_PAGE_INDEX, 0));
    }
}
