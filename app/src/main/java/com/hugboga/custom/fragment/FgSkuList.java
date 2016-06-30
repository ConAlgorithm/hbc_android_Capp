package com.hugboga.custom.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.hugboga.custom.widget.SkuListEmptyView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;

import java.io.Serializable;
import java.util.ArrayList;

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
    @Bind(R.id.suk_list_empty_view)
    SkuListEmptyView emptyView;

    private SkuCityHeaderView cityHeaderView;
    private SkuCityFooterView cityFooterView;

    private FgSkuList.Params paramsData;
    private SkuAdapter adapter;
    private boolean isFirstRequest = true;
    private boolean isLoading = true;
    private SkuCityBean skuCityBean;

    public enum SkuType {
        CITY, ROUTE, COUNTRY;
    }

    public Params getParamsData() {
        return paramsData;
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
        emptyView.setFragment(this);

        ZDefaultDivider divider = recyclerView.getItemDecoration();
        divider.setItemOffsets(0, 0, 0, 0);

        adapter = new SkuAdapter(getContext());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        //城市页有header和footer
        if (paramsData.skuType == SkuType.CITY) {
            swipeRefreshLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            titlebar.setVisibility(View.GONE);
            titlebar.setBackgroundColor(0x00000000);
            fgTitle.setTextColor(0x00000000);

            cityHeaderView = new SkuCityHeaderView(getContext());
            cityHeaderView.setFragment(this);
            adapter.addHeaderView(cityHeaderView);

            cityFooterView = new SkuCityFooterView(getContext());
            cityFooterView.setFragment(this);
            adapter.addFooterView(cityFooterView);
        } else {
            titlebar.setBackgroundColor(0xFF2D2B28);
            fgTitle.setTextColor(0xFFFFFFFF);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            swipeRefreshLayout.setLayoutParams(params);
            params.addRule(RelativeLayout.BELOW, R.id.suk_list_titlebar);
            recyclerView.addItemDecoration(new SpaceItemDecoration());
            titlebar.setVisibility(View.VISIBLE);
        }

        isFirstRequest = true;
        sendRequest(0, true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFirstRequest = true;
                sendRequest(0, false);//下拉刷新
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (skuCityBean == null || adapter == null) {
                    return;
                }

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (!isLoading && lastVisibleItem >= totalItemCount - 1 && dy > 0 && adapter.getListCount() < skuCityBean.goodsCount) {
                    isFirstRequest = false;
                    int pageIndex = adapter == null ? 0 : adapter.getListCount();
                    if (skuCityBean.hasDailyservice() && pageIndex == Constants.DEFAULT_PAGESIZE + 1) {//第一页带包车的需减去包车
                        --pageIndex;
                    }
                    sendRequest(pageIndex, false);//加载下一页
                }

                if (paramsData.skuType == SkuType.CITY && cityHeaderView != null) {
                    int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    int scrollY = Math.abs(recyclerView.getChildAt(0).getTop());
                    float showRegionHight = cityHeaderView.getDisplayLayoutHeight() / 2.0f;
                    if (firstVisibleItemPosition == 0 && scrollY <= showRegionHight) {
                        float alpha = 0.0f;
                        if (scrollY <= 0) {
                            alpha = 0.0f;
                        } else {
                            alpha = Math.min(1, scrollY / showRegionHight);
                        }
                        titlebar.setBackgroundColor(UIUtils.getColorWithAlpha(alpha, 0xFF2D2B28));
                        fgTitle.setTextColor(UIUtils.getColorWithAlpha(alpha, 0xFFFFFFFF));
                    } else {
                        titlebar.setBackgroundColor(0xFF2D2B24);
                        fgTitle.setTextColor(0xFFFFFFFF);
                    }
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

    public Callback.Cancelable sendRequest(int pageIndex, boolean needShowLoading) {
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
        return HttpRequestUtils.request(getActivity(), request, this, needShowLoading);
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
        if (_request instanceof RequestCitySkuList) {
            skuCityBean = ((RequestCitySkuList) _request).getData();
            cityHeaderView.update(skuCityBean);
            cityFooterView.update(skuCityBean);
            fgTitle.setText(skuCityBean.cityName);
            titlebar.setVisibility(View.VISIBLE);
            showEmptyView(true);
        } else if (_request instanceof RequestRouteSkuList) {
            skuCityBean = ((RequestRouteSkuList) _request).getData();
            fgTitle.setText(skuCityBean.lineGroupName);
            showEmptyView(false);
        } else if (_request instanceof RequestCountrySkuList) {
            skuCityBean = ((RequestCountrySkuList) _request).getData();
            fgTitle.setText(skuCityBean.countryName);
            showEmptyView(false);
        }
        adapter.addDatas(skuCityBean.goodsList, !isFirstRequest);
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        super.onDataRequestError(errorInfo, _request);
        requestFailure(_request);
    }

    @Override
    public void onDataRequestCancel(BaseRequest _request) {
        super.onDataRequestCancel(_request);
        requestFailure(_request);
    }

    private void requestFailure(BaseRequest _request) {
        if (adapter.getItemCount() <= 0) {
            emptyView.requestFailure();
        }
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
    }

    private void showEmptyView(boolean isCity) {
        if (adapter.getListCount() <= 0 && (skuCityBean.goodsList == null || skuCityBean.goodsList.size() <= 0)) {
            if (isCity) {
                if (!skuCityBean.hasSingleService() && !skuCityBean.hasAirporService()) {
                    emptyView.showEmptyView(true);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    titlebar.setBackgroundColor(0xFF2D2B28);
                    fgTitle.setTextColor(0xFFFFFFFF);
                } else {
                    emptyView.setVisibility(View.GONE);
                }
            } else {
                emptyView.showEmptyView(false);
            }
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int pos = parent.getChildAdapterPosition(view);
            outRect.left = 0;
            outRect.bottom = 0;
            outRect.right = 0;
            if (pos == 0) {
                outRect.top = UIUtils.dip2px(15);
            } else {
                outRect.top = 0;
            }
        }
    }

    @Override
    public void onFragmentResult(Bundle bundle) {
        String fragmentName = bundle.getString(KEY_FRAGMENT_NAME);
        if (FgChooseCityNew.class.getSimpleName().equals(fragmentName)) {
            paramsData = (FgSkuList.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            initHeader();
        }
    }
}
