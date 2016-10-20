package com.hugboga.custom.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.widget.recycler.ZDefaultDivider;
import com.huangbaoche.hbcframe.widget.recycler.ZListRecyclerView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityHomeAdapter;
import com.hugboga.custom.adapter.HbcRecyclerBaseAdapter;;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.data.request.RequestCityHomeList;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CityHomeHeader;
import com.hugboga.custom.widget.SkuListEmptyView;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/18.
 */

public class CityHomeListActivity extends BaseActivity implements HbcRecyclerBaseAdapter.OnItemClickListener {
    public static final String KEY_CITY_ID = "KEY_CITY_ID";
    public static final String KEY_CITY_BEAN = "KEY_CITY_BEAN";

    @Bind(R.id.cityHome_list_titlebar)
    RelativeLayout titlebar;
    @Bind(R.id.cityHome_list_listview)
    ZListRecyclerView recyclerView;
    @Bind(R.id.cityHome_list_refresh_layout)
    ZSwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.cityHome_list_empty_view)
    SkuListEmptyView emptyView;//和sku一样

//    @Bind(R.id.header_right_image)
//    TextView headerRightIV;

    private CityHomeHeader cityHomeHeader;

    private CityHomeListActivity.Params paramsData;
    private CityHomeAdapter adapter;
    private boolean isFirstRequest = true;
    private boolean isLoading = true;
    private CityHomeBean cityHomeBean;
    private CityBean cityBean = null;

    public enum CityHomeType {
        CITY, ROUTE, COUNTRY;
    }

    public Params getParamsData() {
        return paramsData;
    }

    public static class Params implements Serializable {
        public int id;
        public CityHomeType cityHomeType;
        public String titleName;
        public int daysCountMin=0;
        public int daysCountMax=0;
        public int goodsClass=0;
        public int themeId=0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (CityHomeListActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                paramsData = (CityHomeListActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.fg_city_home_list);
        initDefaultTitleBar();
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_CITY;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            paramsData = (CityHomeListActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
        }
        initView();
    }

    private void initView() {
        if (paramsData == null) {
            return;
        }
        fgTitle.setText(paramsData.titleName);
        fgRightBtn.setBackgroundResource(R.mipmap.home_search_icon);

        ZDefaultDivider divider = recyclerView.getItemDecoration();
        divider.setItemOffsets(0, 0, 0, 0);

        adapter = new CityHomeAdapter(this);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);


        //城市页有header
        if (paramsData.cityHomeType == CityHomeType.CITY) {
            swipeRefreshLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            titlebar.setVisibility(View.GONE);
            titlebar.setBackgroundColor(0x00000000);
            fgTitle.setTextColor(0x00000000);

            cityHomeHeader = new CityHomeHeader(this);
            adapter.addHeaderView(cityHomeHeader);


            getCityBean();
        } else {
            titlebar.setBackgroundColor(0xFF2D2B28);
            fgTitle.setTextColor(0xFFFFFFFF);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            swipeRefreshLayout.setLayoutParams(params);
            params.addRule(RelativeLayout.BELOW, R.id.cityHome_list_titlebar);
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
                if (cityHomeBean == null || adapter == null) {
                    return;
                }

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                if (!isLoading && lastVisibleItem >= totalItemCount - 1 && dy > 0 && adapter.getListCount() < cityHomeBean.goodsCount) {
                    isFirstRequest = false;
                    int pageIndex = adapter == null ? 0 : adapter.getListCount();
                    if (cityHomeBean.cityService.hasDailyservice() && pageIndex == Constants.DEFAULT_PAGESIZE + 1) {//第一页带包车的需减去包车
                        --pageIndex;
                    }
                    sendRequest(pageIndex, false);//加载下一页
                }

                if (paramsData.cityHomeType == CityHomeType.CITY && cityHomeHeader != null) {
                    int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    int scrollY = Math.abs(recyclerView.getChildAt(0).getTop());
                    float showRegionHight = cityHomeHeader.getDisplayLayoutHeight() / 2.0f;
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


    public Callback.Cancelable sendRequest(int pageIndex, boolean needShowLoading) {
        isLoading = true;
        BaseRequest request = null;
        switch (paramsData.cityHomeType) {
            case CITY:
                request = new RequestCityHomeList(this, "" + paramsData.id, pageIndex,paramsData.daysCountMin,paramsData.daysCountMax,paramsData.goodsClass,paramsData.themeId);
                break;
            case ROUTE:
                request = new RequestCityHomeList(this, "" + paramsData.id, pageIndex,paramsData.daysCountMin,paramsData.daysCountMax,paramsData.goodsClass,paramsData.themeId);
                break;
            case COUNTRY:
                request = new RequestCityHomeList(this, "" + paramsData.id, pageIndex,paramsData.daysCountMin,paramsData.daysCountMax,paramsData.goodsClass,paramsData.themeId);
                break;
        }
        return HttpRequestUtils.request(this, request, this, needShowLoading);
    }

    @Override
    public void onItemClick(View view, int position, Object itemData) {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestCityHomeList) {
            cityHomeBean = ((RequestCityHomeList) _request).getData();
            cityHomeHeader.update(cityHomeBean);
            fgTitle.setText(cityHomeBean.cityContent.cityName);
            titlebar.setVisibility(View.VISIBLE);
            showEmptyView(true);
        }

            adapter.addDatas(cityHomeBean.goodsSecList, !isFirstRequest);

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
        if (adapter.getListCount() <= 0) {
            emptyView.requestFailure();
        }
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;
    }

    private void showEmptyView(boolean isCity) {
        if (adapter.getListCount() <= 0 && (cityHomeBean.goodsSecList == null || cityHomeBean.goodsSecList.size() <= 0)) {
            if (isCity) {
                if (!cityHomeBean.cityService.hasSingleService() && !cityHomeBean.cityService.hasAirporService()) {
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

    public CityBean getCityBean() {
        if (cityBean == null && paramsData.cityHomeType == CityHomeType.CITY) {
            DbManager mDbManager = new DBHelper(this).getDbManager();
            try {
                cityBean = mDbManager.findById(CityBean.class, "" + paramsData.id);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return cityBean;
    }

    @Override
    public String getEventSource() {
        return "城市列表";
    }
}
