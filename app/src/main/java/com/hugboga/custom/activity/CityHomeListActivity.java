package com.hugboga.custom.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.recycler.ZDefaultDivider;
import com.huangbaoche.hbcframe.widget.recycler.ZListRecyclerView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.MyApplication;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityHomeAdapter;
import com.hugboga.custom.adapter.HbcRecyclerBaseAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.data.bean.GoodsSec;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCityHomeList;
import com.hugboga.custom.data.request.RequestCountryHomeList;
import com.hugboga.custom.data.request.RequestRouteCityHomeList;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CityFilterLayout;
import com.hugboga.custom.widget.CityHomeHeader;
import com.hugboga.custom.widget.SkuListEmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/18.
 */

public class CityHomeListActivity extends BaseActivity implements HbcRecyclerBaseAdapter.OnItemClickListener,CityHomeHeader.HeaderTabClickListener {
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

    @Bind(R.id.header_right_image)
    ImageView headerRightIV;
    @Bind(R.id.city_home_filter_layout)
    CityFilterLayout cityFilterLayout;

    private CityHomeHeader cityHomeHeader;

    private CityHomeListActivity.Params paramsData;
    private CityHomeAdapter adapter;
    private boolean isFirstRequest = true;
    private boolean isLoading = true;
    private CityHomeBean cityHomeBean;

    public enum CityHomeType {
        CITY, ROUTE, COUNTRY
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
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (CityHomeListActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.fg_city_home_list);

        ButterKnife.bind(this);

        initDefaultTitleBar();

        backEvent();

        initView();

        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    private void backEvent(){
        if (fgLeftBtn != null) {
            fgLeftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(goBack()){
                        return;
                    }
                    finish();
                }
            });
        }
    }

    private boolean goBack(){
        if(cityFilterLayout!=null && cityFilterLayout.filterViewIsShow()){
            cityFilterLayout.onlyHideFilterView();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(goBack()){
            return;
        }
        super.onBackPressed();
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
        setSerachView();

        if (paramsData == null) {
            return;
        }
        fgTitle.setText(paramsData.titleName);
        isFirstRequest = true;

        adapter = new CityHomeAdapter(this);
        recyclerView.setAdapter(adapter);

        setRecyclerViewByFromType();

        sendRequest(0, true);

        setRefreshEvent();

        addScrollerEvent();

        adapter.setOnItemClickListener(this);
    }

    /**
     * 根据类型判断是否加载header
     */
    private void setRecyclerViewByFromType(){
        //城市页有header
        if (paramsData.cityHomeType == CityHomeType.CITY) {
            swipeRefreshLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            titlebar.setVisibility(View.GONE);
            titlebar.setBackgroundColor(0x00000000);
            fgTitle.setTextColor(0x00000000);
            cityHomeHeader = new CityHomeHeader(this);
            cityHomeHeader.setHeaderTabClickListener(this);
            adapter.addHeaderView(cityHomeHeader);
        } else {
            titlebar.setBackgroundColor(0xFF2D2B28);
            fgTitle.setTextColor(0xFFFFFFFF);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            swipeRefreshLayout.setLayoutParams(params);
            params.addRule(RelativeLayout.BELOW, R.id.cityHome_list_titlebar);
            titlebar.setVisibility(View.VISIBLE);
        }

        ZDefaultDivider divider = recyclerView.getItemDecoration();
        divider.setItemOffsets(0, 0, 0, 0);
        //recyclerView.addItemDecoration(new SpaceItemDecoration());
    }

    /**
     * 搜索处理
     */
    private void setSerachView(){
        if(headerRightIV.getLayoutParams() instanceof  RelativeLayout.LayoutParams);{
            RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams)headerRightIV.getLayoutParams();
            layoutParam.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParam.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParam.setMargins(UIUtils.dip2px(4),UIUtils.dip2px(4),UIUtils.dip2px(4),UIUtils.dip2px(4));
        }
        headerRightIV.setVisibility(View.VISIBLE);
        headerRightIV.setBackgroundResource(R.drawable.black_circle);
        headerRightIV.setImageResource(R.mipmap.search_box_white);
        headerRightIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ChooseCityNewActivity.class);
                intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_RECOMMEND);
                intent.putExtra("isHomeIn",false);
                intent.putExtra("source","小搜索框");
                startActivity(intent);
            }
        });
    }

    /**
     * 列表刷新
     */
    private void setRefreshEvent(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isFirstRequest = true;
                sendRequest(0, false);//下拉刷新
            }
        });
    }

    /**
     * 滑动处理
     */
    private void addScrollerEvent(){
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

                isShowCityFilter(layoutManager);
            }
        });
    }

    public Callback.Cancelable sendRequest(int pageIndex, boolean needShowLoading) {
        isLoading = true;
        BaseRequest request = null;
        switch (paramsData.cityHomeType) {
            case CITY:
                request = new RequestCityHomeList(this, "" + paramsData.id, pageIndex,paramsData.daysCountMin,paramsData.daysCountMax,paramsData.goodsClass,paramsData.themeId);
                break;
            case ROUTE:
                request = new RequestRouteCityHomeList(this, "" + paramsData.id, pageIndex,paramsData.daysCountMin,paramsData.daysCountMax,paramsData.goodsClass,paramsData.themeId);
                break;
            case COUNTRY:
                request = new RequestCountryHomeList(this, "" + paramsData.id, pageIndex,paramsData.daysCountMin,paramsData.daysCountMax,paramsData.goodsClass,paramsData.themeId);
                break;
        }
        return HttpRequestUtils.request(this, request, this, needShowLoading);
    }

    @Override
    public void onItemClick(View view, int position, Object itemData) {
        if (itemData != null && itemData instanceof GoodsSec) {
            GoodsSec goodsSec = (GoodsSec) itemData;
            if (goodsSec.goodsClass == -1) {//超省心（固定线路）
                if (cityBean != null) {
                    String userId = UserEntity.getUser().getUserId(this);
                    String params = "";
                    if(!TextUtils.isEmpty(userId)){
                        params += "?userId="+userId;
                    }
                    String cityId = cityBean.cityId + "";
                    if(!TextUtils.isEmpty(cityId)){
                        if(params.contains("?")) {
                            params += "&cityId=" + cityId;
                        }else{
                            params += "?cityId=" + cityId;
                        }
                    }
                    Intent intent = new Intent(CityHomeListActivity.this, DailyWebInfoActivity.class);
                    intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
                    intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_DAIRY+params);
                    intent.putExtra("cityBean", cityBean);
                    intent.putExtra("source", cityBean.name);
                    intent.putExtra(KEY_CITY_BEAN, cityBean);
                    startActivity(intent);

                } else {
                    String userId = UserEntity.getUser().getUserId(this);
                    String params = "";
                    if(!TextUtils.isEmpty(userId)){
                        params += "?userId=" + userId;
                    }
                    Intent intent = new Intent(CityHomeListActivity.this, WebInfoActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_DAIRY + params);
                    startActivity(intent);

                }
            } else {
                String userId = UserEntity.getUser().getUserId(this);
                String cityHomeDetailUrl = goodsSec.skuDetailUrl;
                if(!TextUtils.isEmpty(userId)){
                    cityHomeDetailUrl += "&userId="+userId;
                }
                Intent intent = new Intent(CityHomeListActivity.this, SkuDetailActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, cityHomeDetailUrl);
                intent.putExtra(Constants.PARAMS_ID, goodsSec.goodsNo);
                //intent.putExtra(SkuDetailActivity.WEB_SKU, goodsSec);
                startActivity(intent);
                if(goodsSec.goodsClass == 1) {
                    StatisticClickEvent.click(StatisticConstant.CLICK_RG, "城市页");
                }else {
                    StatisticClickEvent.click(StatisticConstant.CLICK_RT, "城市页");
                }
            }
        }
    }


    private CityBean cityBean = null;
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
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestCityHomeList) {
            cityHomeBean = ((RequestCityHomeList) _request).getData();
            cityHomeHeader.update(cityHomeBean);
            fgTitle.setText(cityHomeBean.cityContent.cityName);
            titlebar.setVisibility(View.VISIBLE);
            showEmptyView(true);
        }else if (_request instanceof RequestRouteCityHomeList) {
            cityHomeBean = ((RequestRouteCityHomeList) _request).getData();
            fgTitle.setText(cityHomeBean.lineGroupContent.lineGroupName);
            showEmptyView(false);
        } else if (_request instanceof RequestCountryHomeList) {
            cityHomeBean = ((RequestCountryHomeList) _request).getData();
            fgTitle.setText(cityHomeBean.countryContent.countryName);
            showEmptyView(false);

        }
        if(cityHomeBean.goodsThemesList==null){
            cityHomeBean.goodsThemesList = new ArrayList<>();
        }
        cityHomeBean.goodsThemesList.add(0, CityHomeBean.GoodsThemes.getDefaultTheme());
        cityFilterLayout.setGoodsThemesList(cityHomeBean.goodsThemesList);
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


    @Override
    public String getEventSource() {
        return "城市列表";
    }

    public void isShowCityFilter( RecyclerView.LayoutManager layoutManager){
        if (paramsData.cityHomeType == CityHomeType.CITY && cityHomeHeader != null) {
            int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            int scrollY = Math.abs(recyclerView.getChildAt(0).getTop());
            float showRegionHight = cityHomeHeader.getDisplayLayoutHeight()+(recyclerView.getChildAt(0).getHeight()-cityHomeHeader.getDisplayLayoutHeight());
            if (firstVisibleItemPosition == 0 && scrollY <= showRegionHight){
                cityFilterLayout.hideTab();
            } else {
                cityFilterLayout.onlyShowTab();
            }
        }
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        MLog.e(this + " onEventMainThread " + action.getType());
        switch (action.getType()) {
            case CITY_FILTER_TYPE:
                setFilterType((Integer) action.getData());
                hideFilterView();
                break;
            case CITY_FILTER_DAY:
                setByFilterDay((Integer) action.getData());
                hideFilterView();
                break;
            case CITY_FILTER_THEME:
                setByFilterTheme((Integer) action.getData());
                hideFilterView();
                break;

            default:
                break;
        }
        Toast.makeText(MyApplication.getAppContext(),action.getData().toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void headerTabClick(int position) {
        showFilterView(position);
    }

    private void setFilterType(int value){
        paramsData.goodsClass = value;

    }

    private void setByFilterDay(int value){
        paramsData.daysCountMin = value;
        if(value==1){
            paramsData.daysCountMax = 1 ;
        }else{
            paramsData.daysCountMax = 0;
        }
    }

    private void setByFilterTheme(int value){
        paramsData.themeId  = value;
    }

    private void hideFilterView(){
        cityFilterLayout.hideFilterView();
    }

    private void showFilterView(int index){
        if(cityFilterLayout!=null){
            cityFilterLayout.showFilterView(index);
        }
    }

}