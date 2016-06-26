package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.widget.recycler.ZListRecyclerView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.SkuAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SkuCityBean;
import com.hugboga.custom.data.request.RequestSkuList;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 城市SKU列表
 * Created by admin on 2016/3/3.
 */
@ContentView(R.layout.fg_sku_list)
public class FgSkuList extends BaseFragment {

    public static final String KEY_CITY_ID = "KEY_CITY_ID";

    @Bind(R.id.suk_list_listview)
    ZListRecyclerView recyclerView;
    @Bind(R.id.suk_list_refresh_layout)
    ZSwipeRefreshLayout swipeRefreshLayout;

    private FgSkuList.Params paramsData;
    private SkuAdapter adapter;
    private boolean isFirstRequest = true;
    private boolean isLoading = true;

    public static class Params implements Serializable {
        public int cityId;
    }

    public static FgSkuList newInstance(Params params) {
        FgSkuList fragment = new FgSkuList();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS_DATA, params);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            paramsData = (FgSkuList.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                paramsData = (FgSkuList.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        return rootView;
    }

    @Override
    protected void initHeader() {
        adapter = new SkuAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        isFirstRequest = true;
        sendRequest(0);
        swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFirstRequest = true;
                sendRequest(0);//下拉刷新
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (!isLoading && lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    isFirstRequest = false;
//                    sendRequest(adapter == null ? 0 : adapter.getItemCount());//加载下一页
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    @Override
    protected void initView() {

    }

    private Callback.Cancelable sendRequest(int pageIndex) {
        isLoading = true;
        BaseRequest request = null;
        request = new RequestSkuList(getActivity(), "" + paramsData.cityId, pageIndex);
        return HttpRequestUtils.request(getActivity(), request, this, false);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestSkuList) {
            RequestSkuList request = (RequestSkuList) _request;
            SkuCityBean skuCityBean = request.getData();
            adapter.addDatas(skuCityBean.goodsList, !isFirstRequest);
            swipeRefreshLayout.setRefreshing(false);
            isLoading = false;
            Log.i("aa", "footerPosition " +adapter.getHeadersCount() +" ---  "+ adapter.getItemCount() + " adapter.getFootersCount() "+ adapter.getFootersCount());
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {
        super.onDataRequestCancel(request);
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
    }
}
