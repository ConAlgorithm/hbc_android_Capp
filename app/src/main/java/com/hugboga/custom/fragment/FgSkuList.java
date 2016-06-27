package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.widget.recycler.ZDefaultDivider;
import com.huangbaoche.hbcframe.widget.recycler.ZListRecyclerView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerBaseAdapter;
import com.hugboga.custom.adapter.SkuAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SkuCityBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.request.RequestCitySkuList;
import com.hugboga.custom.data.request.RequestCountrySkuList;
import com.hugboga.custom.data.request.RequestRouteSkuList;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.SkuCityFooterView;
import com.hugboga.custom.widget.SkuCityHeaderView;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.io.Serializable;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 城市SKU列表
 * Created by admin on 2016/3/3.
 */
@ContentView(R.layout.fg_sku_list)
public class FgSkuList extends BaseFragment implements HbcRecyclerBaseAdapter.OnItemClickListener{

    public static final String KEY_CITY_ID = "KEY_CITY_ID";

    @Bind(R.id.suk_list_titlebar)
    RelativeLayout titlebar;
    @Bind(R.id.suk_list_listview)
    ZListRecyclerView recyclerView;
    @Bind(R.id.suk_list_refresh_layout)
    ZSwipeRefreshLayout swipeRefreshLayout;

    private SkuCityHeaderView cityHeaderView;
    private SkuCityFooterView cityFooterView;

    private FgSkuList.Params paramsData;
    private SkuAdapter adapter;
    private boolean isFirstRequest = true;
    private boolean isLoading = true;

    public enum SkuType {
        CITY, ROUTE, COUNTRY;
    }

    public static class Params implements Serializable {
        public int id;
        public SkuType skuType;
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
        if (paramsData == null) {
            return;
        }

        ZDefaultDivider divider = recyclerView.getItemDecoration();
        divider.setItemOffsets(0, 0, 0, 0);

        adapter = new SkuAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);

        //城市页没有titleBar，有header和footer
        if (paramsData.skuType == SkuType.CITY) {
            titlebar.setVisibility(View.GONE);

            cityHeaderView = new SkuCityHeaderView(getContext());
            cityHeaderView.setFragment(this);
            adapter.addHeaderView(cityHeaderView);

            cityFooterView = new SkuCityFooterView(getContext());
            cityFooterView.setFragment(this);
            adapter.addFooterView(cityFooterView);
        } else {
            titlebar.setVisibility(View.VISIBLE);
        }

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
        adapter.setOnItemClickListener(this);
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
        switch (paramsData.skuType) {
            case CITY:
                request = new RequestCitySkuList(getActivity(), "" + paramsData.id, pageIndex);
                break;
            case ROUTE:
                request = new RequestRouteSkuList(getActivity(), "" + paramsData.id, pageIndex);
                break;
            case COUNTRY:
                request = new RequestCountrySkuList(getActivity(), "" + paramsData.id, pageIndex);
                break;
        }
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
    public void onItemClick(View view, int position, Object _itemData) {
        if (_itemData != null && _itemData instanceof SkuItemBean) {
            SkuItemBean skuItemBean = (SkuItemBean) _itemData;
            if (skuItemBean.goodsClass == -1) {//按天包车
                startFragment(new FgOrderSelectCity());
            } else {
                FgSkuDetail fgSkuDetail = new FgSkuDetail();
                Bundle bundle = new Bundle();
                bundle.putString(FgWebInfo.WEB_URL, skuItemBean.skuDetailUrl);
                bundle.putSerializable(FgSkuDetail.WEB_SKU, skuItemBean);
                fgSkuDetail.setArguments(bundle);
                startFragment(fgSkuDetail, bundle);
            }
        }
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        SkuCityBean skuCityBean = null;
        if (_request instanceof RequestCitySkuList) {
            skuCityBean = ((RequestCitySkuList) _request).getData();
            cityHeaderView.update(skuCityBean);
            cityFooterView.update(skuCityBean);
        } else if (_request instanceof RequestRouteSkuList) {
            skuCityBean = ((RequestRouteSkuList) _request).getData();
            fgTitle.setText(skuCityBean.lineGroupName);
        } else if (_request instanceof RequestCountrySkuList) {
            skuCityBean = ((RequestCountrySkuList) _request).getData();
            fgTitle.setText(skuCityBean.countryName);
        }
        adapter.addDatas(skuCityBean.goodsList, !isFirstRequest);
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
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
