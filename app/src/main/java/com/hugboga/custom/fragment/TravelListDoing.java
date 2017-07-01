package com.hugboga.custom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.OrderDetailActivity;
import com.hugboga.custom.activity.TravelFundActivity;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.adapter.HbcRecyclerTypeBaseAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.bean.TravelListAllBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.event.EventType;
import com.hugboga.custom.data.parser.ParserTravel;
import com.hugboga.custom.data.request.RequestOrderListDoing;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.HbcLoadingMoreFooter;
import com.hugboga.custom.widget.TravelListItem;
import com.hugboga.custom.widget.TravelLoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;

import butterknife.Bind;

/**
 * Created by zhangqiang on 17/6/26.
 */

public class TravelListDoing extends FgBaseTravel {

    @Bind(R.id.travel_recyclerview)
    XRecyclerView mXRecyclerView;
    @Bind(R.id.list_empty)
    RelativeLayout emptyView;
    @Bind(R.id.travel_footer_get_layout)
    LinearLayout footerGet;
    protected HbcRecyclerSingleTypeAdpater hbcRecyclerSingleTypeAdpater;
    int refreshOrNot = 1;
    @Override
    protected void loadData() {
        refreshOrNot = 2;
        runData(5,0,10);
    }
    public Callback.Cancelable runData(int orderShowType, int pageIndex,int pageSize) {
        BaseRequest request = new RequestOrderListDoing(getActivity(),orderShowType,pageSize,pageIndex);
        return HttpRequestUtils.request(getActivity(), request, this,true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public int getContentViewId() {
        return R.layout.travel_list;
    }
    @Override
    protected void initViews() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(getContext());
        mXRecyclerView.setLayoutManager(layoutManager);
        TravelLoadingMoreFooter travelLoadingMoreFooter = new TravelLoadingMoreFooter(getContext());
        travelLoadingMoreFooter.setCustomlayout(inflater);
        mXRecyclerView.setFootView(travelLoadingMoreFooter);
        hbcRecyclerSingleTypeAdpater = new HbcRecyclerSingleTypeAdpater(getContext(), TravelListItem.class);
        mXRecyclerView.setAdapter(hbcRecyclerSingleTypeAdpater);
        //getFooterView(inflater);
        mXRecyclerView.setEmptyView(emptyView);
        footerGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TravelFundActivity.class);
                intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                getContext().startActivity(intent);
                MobClickUtils.onEvent(StatisticConstant.CLICK_TRAVELFOUND_XC);
            }
        });
        //hbcRecyclerSingleTypeAdpater.addFooterView(getFooterView(inflater));
        hbcRecyclerSingleTypeAdpater.setOnItemClickListener(new HbcRecyclerTypeBaseAdpater.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object itemData) {
                OrderBean bean = (OrderBean) itemData;
                if(bean != null){
                    OrderDetailActivity.Params params = new OrderDetailActivity.Params();
                    params.orderType = bean.orderType;
                    params.orderId = bean.orderNo;
                    params.source = bean.orderType == 5 ? bean.serviceCityName : "首页";
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    intent.putExtra(Constants.PARAMS_DATA, params);
                    intent.putExtra(Constants.PARAMS_SOURCE,params.source);
                    getActivity().startActivity(intent);
                }
            }
        });
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshOrNot = 1;
                runData(5,0,10);
            }

            @Override
            public void onLoadMore() {
                refreshOrNot = 2;
                if (hbcRecyclerSingleTypeAdpater.getListCount()>0) {
                    runData(5, hbcRecyclerSingleTypeAdpater == null ? 0 : hbcRecyclerSingleTypeAdpater.getListCount(),10);
                }
            }
        });
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {
        super.onDataRequestCancel(request);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        TravelListAllBean travelListAllBean = (TravelListAllBean) request.getData();
        if (mXRecyclerView != null && travelListAllBean!=null) {
            if (hbcRecyclerSingleTypeAdpater != null) {
                hbcRecyclerSingleTypeAdpater.addData(travelListAllBean.resultBean, request.getOffset() > 0);
            }
            if (request.getOffset() == 0) {
                mXRecyclerView.smoothScrollToPosition(0);
            }
            if(refreshOrNot == 1){
                mXRecyclerView.refreshComplete();
            }else if(refreshOrNot == 2){
                mXRecyclerView.loadMoreComplete();
            }
            if(hbcRecyclerSingleTypeAdpater!= null){
                mXRecyclerView.setNoMore(hbcRecyclerSingleTypeAdpater.getListCount() >= travelListAllBean.totalSize);
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("travelListAllBean",travelListAllBean);
            bundle.putInt("requestType", ParserTravel.INGISTT);
            EventBus.getDefault().post(new EventAction(EventType.TRAVEL_LIST_NUMBER, bundle));

        }
    }
}
