package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.recycler.ZDefaultDivider;
import com.huangbaoche.hbcframe.widget.recycler.ZListRecyclerView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityHomeAdapter;
import com.hugboga.custom.adapter.HbcRecyclerTypeBaseAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.CityFilterData;
import com.hugboga.custom.data.bean.CityHomeBean;
import com.hugboga.custom.data.bean.GoodsSec;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.net.UrlLibs;
import com.hugboga.custom.data.request.RequestCityHomeList;
import com.hugboga.custom.data.request.RequestCountryHomeList;
import com.hugboga.custom.data.request.RequestRouteCityHomeList;
import com.hugboga.custom.statistic.MobClickUtils;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.statistic.sensors.SensorsConstant;
import com.hugboga.custom.utils.AnimationUtils;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.CityFilterLayout;
import com.hugboga.custom.widget.CityHomeFooter;
import com.hugboga.custom.widget.CityHomeHeader;
import com.hugboga.custom.widget.CityHomeListItemFree;
import com.hugboga.custom.widget.CityHomeListItemWorry;
import com.hugboga.custom.widget.CityPlaceHolderView;
import com.hugboga.custom.widget.SkuListEmptyView;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.exceptions.InvalidDataException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/18.
 */

public class CityHomeListActivity extends BaseActivity implements HbcRecyclerTypeBaseAdpater.OnItemClickListener, CityHomeHeader.HeaderTabClickListener {
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
    private CityHomeFooter cityHomeFooter;
    private CityPlaceHolderView placeHolderView;

    private CityHomeListActivity.Params paramsData;
    private CityHomeAdapter adapter;
    private boolean isFirstRequest = true;
    private boolean isLoading = true;
    private CityHomeBean cityHomeBean;

    public String typeValue="不限";
    public String dayValue="不限";
    public String themesValues="不限";

    private int statusBarHeight;
    private int titleBarHeight;
    private int filterTabBarHeight;
    private int animationIconLabelPaddingDistance = UIUtils.getScreenHeight()/12;

    public enum CityHomeType {
        CITY, ROUTE, COUNTRY
    }


    public static class Params implements Serializable {
        public int id;
        public CityHomeType cityHomeType;
        public String titleName;
        public int daysCountMin = 0;
        public int daysCountMax = 0;
        public int goodsClass = 0;
        public int themeId = 0;
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

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        setSensorsShowEvent();
    }

    //神策统计_浏览页面
    private void setSensorsShowEvent() {
        try {
            if (paramsData == null) {
                return;
            }
            final String id = "" + paramsData.id;
            JSONObject properties = new JSONObject();
            String webTitle = "";
            String webUrl = SensorsConstant.CITIES;
            switch (paramsData.cityHomeType) {
                case CITY:
                    webTitle = "城市";
                    webUrl += "?city_id=";
                    break;
                case ROUTE:
                    webTitle = "线路圈页";
                    webUrl += "?linegroup_id=";
                    break;
                case COUNTRY:
                    webTitle = "国家";
                    webUrl += "?country_id=";
                    break;
            }
            webUrl += id;
            properties.put("web_title", webTitle);
            properties.put("web_url", webUrl);
            SensorsDataAPI.sharedInstance(this).track("page_view", properties);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //神策统计_浏览城市
    private void setSensorsEvent() {
        try {
            if (paramsData == null || cityHomeBean == null) {
                return;
            }
            final String id = "" + paramsData.id;
            //浏览城市
            JSONObject properties = new JSONObject();
            properties.put("refer", getIntentSource());
            switch (paramsData.cityHomeType) {
                case CITY:
                    properties.put("city_id", id);
                    properties.put("city_name", cityHomeBean.cityContent.cityName);
                    break;
                case ROUTE:
                    properties.put("linegroup_id", id);
                    properties.put("linegroup_name", cityHomeBean.lineGroupContent.lineGroupName);
                    break;
                case COUNTRY:
                    properties.put("country_id", id);
                    properties.put("country_name", cityHomeBean.countryContent.countryName);
                    break;
            }
            SensorsDataAPI.sharedInstance(this).track("view_citis", properties);
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void backEvent() {
        if (fgLeftBtn != null) {
            fgLeftBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (goBack()) {
                        return;
                    }
                    finish();
                }
            });
        }
    }

    private boolean goBack() {
        if (cityFilterLayout != null && cityFilterLayout.filterViewIsShow()) {
            if(adapter!=null && adapter.getListCount()<2){
                cityFilterLayout.hideTab();
                titlebar.setBackgroundColor(0x00000000);
            }else{
                cityFilterLayout.onlyHideFilterView();
            }
            if(placeHolderView!=null){
                placeHolderView.hide();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (goBack()) {
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
        if(cityFilterLayout!=null){
            cityFilterLayout.resetDatas();
        }
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
    private void setRecyclerViewByFromType() {

        cityHomeHeader = new CityHomeHeader(this);
        cityHomeHeader.setHeaderTabClickListener(this);
        if (paramsData.cityHomeType == CityHomeType.CITY) {
            swipeRefreshLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            titlebar.setVisibility(View.GONE);
            titlebar.setBackgroundColor(0x00000000);
            fgTitle.setTextColor(0x00000000);
        } else {
            titlebar.setBackgroundColor(0xFF111111);
            fgTitle.setTextColor(0xFFFFFFFF);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            swipeRefreshLayout.setLayoutParams(params);
            params.addRule(RelativeLayout.BELOW, R.id.cityHome_list_titlebar);
            titlebar.setVisibility(View.VISIBLE);
            cityHomeHeader.hideHeaderContent();
        }
        adapter.addHeaderView(cityHomeHeader);


        cityHomeFooter = new CityHomeFooter(this);
        adapter.addFooterView(cityHomeFooter);

        if(paramsData!=null && paramsData.cityHomeType == CityHomeType.CITY){
            placeHolderView = new CityPlaceHolderView(this);
            adapter.addFooterView(placeHolderView);
        }
        ZDefaultDivider divider = recyclerView.getItemDecoration();
        divider.setItemOffsets(0, 0, 0, 0);
    }

    /**
     * 搜索处理
     */
    private void setSerachView() {
        if (headerRightIV.getLayoutParams() instanceof RelativeLayout.LayoutParams) ;
        {
            RelativeLayout.LayoutParams layoutParam = (RelativeLayout.LayoutParams) headerRightIV.getLayoutParams();
            layoutParam.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParam.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParam.setMargins(UIUtils.dip2px(2), UIUtils.dip2px(2), UIUtils.dip2px(2), UIUtils.dip2px(2));
        }
        headerRightIV.setVisibility(View.VISIBLE);
        headerRightIV.setBackgroundResource(R.drawable.black_circle);
        headerRightIV.setImageResource(R.mipmap.search_box_white);
        headerRightIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ChooseCityNewActivity.class);
                intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_RECOMMEND);
                intent.putExtra("isHomeIn", false);
                intent.putExtra("source", "小搜索框");
                startActivity(intent);
                StatisticClickEvent.click(StatisticConstant.SEARCH_LAUNCH,"城市切换");
            }
        });
    }

    /**
     * 列表刷新
     */
    private void setRefreshEvent() {
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
    private void addScrollerEvent() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isClickEvent) {
                    cityFilterLayout.showFilterView();
                    isClickEvent = false;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (cityHomeBean == null || adapter == null) {
                    return;
                }
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();


                int totalItemCount = layoutManager.getItemCount();
                if (!isLoading && lastVisibleItem >= totalItemCount -adapter.getHeadersCount()-adapter.getFootersCount() && dy > 0 && adapter.getListCount() < cityHomeBean.goodsCount) {
                    isFirstRequest = false;
                    int pageIndex = adapter == null ? 0 : adapter.getListCount();
                    if (cityHomeBean.cityService != null && cityHomeBean.cityService.hasDailyservice() && pageIndex == Constants.DEFAULT_PAGESIZE + 1) {//第一页带包车的需减去包车
                        --pageIndex;
                    }
                    if(!isClickEvent){
                        sendRequest(pageIndex, false);//加载下一页
                    }
                }

                if (paramsData.cityHomeType == CityHomeType.CITY && cityHomeHeader != null) {
                    int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    int scrollY = Math.abs(recyclerView.getChildAt(0).getTop());
                    float showRegionHight = cityHomeHeader.getDisplayLayoutHeight() / 2.0f;
                    if (firstVisibleItemPosition == 0 && scrollY <= showRegionHight) {
                        float alpha;
                        if (scrollY <= 0) {
                            alpha = 0.0f;
                        } else {
                            alpha = Math.min(1, scrollY / showRegionHight);
                        }
                        titlebar.setBackgroundColor(UIUtils.getColorWithAlpha(alpha, 0xFF111111));
                        fgTitle.setTextColor(UIUtils.getColorWithAlpha(alpha, 0xFFFFFFFF));
                    } else {
                        titlebar.setBackgroundColor(0xFF111111);
                        fgTitle.setTextColor(0xFFFFFFFF);
                    }
                }
                isShowCityFilter();
                showAnimLabel();
            }
        });
    }

    private void showAnimLabel(){
        if(recyclerView==null || recyclerView.getChildCount()==0){
            return;
        }
        int childCount = recyclerView.getChildCount();
        if(filterTabBarHeight==0){
            filterTabBarHeight = cityFilterLayout.getTabLayoutHeight();
        }
        int animTopPadding = statusBarHeight + titleBarHeight + filterTabBarHeight;
        for(int i =0;i<childCount;i++){
            View view = recyclerView.getChildAt(i);
            if(view instanceof CityHomeListItemWorry) {
                View labelView = view.findViewById(R.id.city_home_item_head_lable);
                if(setIconAnimations(labelView,animTopPadding)){
                    continue;
                }
            }
            if(view instanceof CityHomeListItemFree){
                View labelView = view.findViewById(R.id.city_home_item_free_head_lable);
                if(setIconAnimations(labelView,animTopPadding)){
                    continue;
                }
            }
        }
    }

    private boolean setIconAnimations(View labelView,int animTopPadding){
        if (labelView != null) {
            int topPadding = CityHomeAdapter.getViewTopOnScreen(labelView);
            Object obj = labelView.getTag();
            if(obj!=null){
                if((Boolean)obj){
                    if (topPadding <= animTopPadding-labelView.getHeight() || topPadding >= UIUtils.getScreenHeight()+labelView.getHeight()){
                        labelView.setTag(false);
                        labelView.setVisibility(View.GONE);
                    }else{
                        labelView.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
            }
            labelView.setVisibility(View.VISIBLE);
            if (topPadding > animTopPadding || topPadding < UIUtils.getScreenHeight()-animationIconLabelPaddingDistance) {
                AnimationUtils.showAnimationtranslationX(labelView, 300, UIUtils.dip2px(70), null);
                labelView.setTag(true);
            }else{
                labelView.setTag(false);
            }
        }
        return  false;
    }


    public Callback.Cancelable sendRequest(int pageIndex, boolean needShowLoading) {
        isLoading = true;
        BaseRequest request = null;
        switch (paramsData.cityHomeType) {
            case CITY:
                if(placeHolderView!=null){
                    placeHolderView.hide();
                }
                request = new RequestCityHomeList(this, "" + paramsData.id, pageIndex, paramsData.daysCountMin, paramsData.daysCountMax, paramsData.goodsClass, paramsData.themeId);
                break;
            case ROUTE:
                request = new RequestRouteCityHomeList(this, "" + paramsData.id, pageIndex, paramsData.daysCountMin, paramsData.daysCountMax, paramsData.goodsClass, paramsData.themeId);
                break;
            case COUNTRY:
                request = new RequestCountryHomeList(this, "" + paramsData.id, pageIndex, paramsData.daysCountMin, paramsData.daysCountMax, paramsData.goodsClass, paramsData.themeId);
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
                    Intent intent = new Intent(CityHomeListActivity.this, OrderSelectCityActivity.class);
                    intent.putExtra("cityBean", cityBean);
                    intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
                    startActivity(intent);
                } else {
                    String userId = UserEntity.getUser().getUserId(this);
                    String params = "";
                    if (!TextUtils.isEmpty(userId)) {
                        params += "?userId=" + userId;
                    }
                    Intent intent = new Intent(CityHomeListActivity.this, WebInfoActivity.class);
                    intent.putExtra(WebInfoActivity.WEB_URL, UrlLibs.H5_DAIRY + params);
                    startActivity(intent);

                }
            } else {
                String userId = UserEntity.getUser().getUserId(this);
                String cityHomeDetailUrl = goodsSec.skuDetailUrl;
                if (!TextUtils.isEmpty(userId)) {
                    cityHomeDetailUrl += "&userId=" + userId;
                }
                Intent intent = new Intent(CityHomeListActivity.this, SkuDetailActivity.class);
                intent.putExtra(WebInfoActivity.WEB_URL, cityHomeDetailUrl);
                intent.putExtra(Constants.PARAMS_ID, goodsSec.goodsNo);
                intent.putExtra("goodtype",goodsSec.goodsType);
                intent.putExtra("type",goodsSec.goodsClass==1?"1":"2");
                startActivity(intent);
                if (goodsSec.goodsClass == 1) {
                    StatisticClickEvent.click(StatisticConstant.CLICK_RG, "城市页");
                    StatisticClickEvent.click(StatisticConstant.LAUNCH_DETAIL_RG,"城市页");
                } else {
                    StatisticClickEvent.click(StatisticConstant.CLICK_RT, "城市页");
                    StatisticClickEvent.click(StatisticConstant.LAUNCH_DETAIL_RT,"城市页");
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
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCityHomeList) {
            cityHomeBean = ((RequestCityHomeList) _request).getData();
            cityHomeHeader.update(cityHomeBean);
            fgTitle.setText(cityHomeBean.cityContent.cityName);
            titlebar.setVisibility(View.VISIBLE);
            showEmptyView(true);
        } else if (_request instanceof RequestRouteCityHomeList) {
            cityHomeBean = ((RequestRouteCityHomeList) _request).getData();
            fgTitle.setText(cityHomeBean.lineGroupContent.lineGroupName);
            showEmptyView(false);
        } else if (_request instanceof RequestCountryHomeList) {
            cityHomeBean = ((RequestCountryHomeList) _request).getData();
            fgTitle.setText(cityHomeBean.countryContent.countryName);
            showEmptyView(false);

        }
        if (cityHomeBean.goodsThemesList == null) {
            cityHomeBean.goodsThemesList = new ArrayList<>();
        }
        cityHomeBean.goodsThemesList.add(0, CityHomeBean.GoodsThemes.getDefaultTheme());

        cityFilterLayout.setGoodsThemesList(cityHomeBean.goodsThemesList);
        adapter.addData(cityHomeBean.goodsSecList, !isFirstRequest);
        swipeRefreshLayout.setRefreshing(false);
        isLoading = false;

        setSensorsEvent();
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
        if (paramsData.daysCountMax != 0 ||
                paramsData.daysCountMin != 0 || paramsData.themeId != 0 || paramsData.goodsClass != 0) {
            if (adapter != null && adapter.getListCount() == 0 && cityHomeBean != null && (cityHomeBean.goodsSecList == null || cityHomeBean.goodsSecList.size() == 0)) {
                cityHomeFooter.showOtherEmpty();
                if(isCity){
                    titlebar.setBackgroundColor(0x00000000);
                    fgTitle.setTextColor(0x00000000);
                }
            } else {
                cityHomeFooter.hideFooter();
            }
        } else {
            if (adapter.getListCount() <= 0 && (cityHomeBean.goodsSecList == null || cityHomeBean.goodsSecList.size() <= 0)) {
                if (isCity) {
                    if(cityHomeBean==null || cityHomeBean.cityService==null){
                        titlebar.setVisibility(View.VISIBLE);
                        titlebar.setBackgroundColor(0xFF111111);
                        fgTitle.setTextColor(0xFFFFFFFF);
                        headerRightIV.setVisibility(View.GONE);
                        emptyView.showEmptyView(true);
                        return;
                    }
                    if(!cityHomeBean.cityService.hasDailyservice() && !cityHomeBean.cityService.hasAirporService()
                            && !cityHomeBean.cityService.hasSingleService()){
                        titlebar.setVisibility(View.VISIBLE);
                        titlebar.setBackgroundColor(0xFF111111);
                        fgTitle.setTextColor(0xFFFFFFFF);
                        headerRightIV.setVisibility(View.GONE);
                        emptyView.showEmptyView(true);
                        return;
                    }
                    if (cityHomeBean != null && cityHomeBean.cityService != null) {
                        if (cityHomeBean.cityService.hasDailyservice()) {
                            cityHomeFooter.showCityEmpty(true);
                        } else {
                            cityHomeFooter.showCityEmpty(false);
                        }
                    }
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.showEmptyView(false);
                }
            } else {
                emptyView.setVisibility(View.GONE);
                if(cityHomeFooter!=null){
                    cityHomeFooter.hideFooter();
                }
            }
        }


    }

    @Override
    public String getEventSource() {
        return "城市列表";
    }



    public void isShowCityFilter() {
        if (cityHomeHeader != null) {
            int scrollY = Math.abs(cityHomeHeader.getFilterTabTop());
            if(statusBarHeight==0){
                statusBarHeight = UIUtils.getStatusBarHeight();
            }
            if(titleBarHeight==0){
                titleBarHeight = titlebar.getHeight();
            }
            if (scrollY >= statusBarHeight + titleBarHeight) {
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
                CityFilterData filterData = (CityFilterData) action.getData();
                setFilterType(filterData.type);
                cityFilterLayout.setFilterTabTypeValue(filterData.label);
                cityHomeHeader.setFilterTypeTabValue(filterData.label);
                hideFilterView();
                setEvent(0,filterData.value);
                break;
            case CITY_FILTER_DAY:
                CityFilterData dayFilterData = (CityFilterData) action.getData();
                setByFilterDay(dayFilterData.type);
                cityFilterLayout.setFilterTabDayValue(dayFilterData.label);
                cityHomeHeader.setFilterDayTabValue(dayFilterData.label);
                hideFilterView();
                setEvent(1,dayFilterData.value);
                break;
            case CITY_FILTER_THEME:
                CityHomeBean.GoodsThemes goodsThemes = (CityHomeBean.GoodsThemes) action.getData();
                setByFilterTheme(goodsThemes.themeId);
                if (goodsThemes.themeId == 0) {
                    cityFilterLayout.setFilterTabThemeValue(goodsThemes.label);
                    cityHomeHeader.setFilterThemeTabValue(goodsThemes.label);
                } else {
                    cityFilterLayout.setFilterTabThemeValue(goodsThemes.themeName);
                    cityHomeHeader.setFilterThemeTabValue(goodsThemes.themeName);
                }
                hideFilterView();
                setEvent(2,goodsThemes.themeName);
                break;
            case CITY_FILTER_CLOSE:
                if(cityFilterLayout!=null){
                    cityFilterLayout.onlyHideFilterView();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void headerTabClick(int position) {
        showFilterView(position,true);
    }

    private void setFilterType(int value) {
        paramsData.goodsClass = value;
        isFirstRequest = true;
        if (adapter != null && adapter.getDatas() != null) {
            adapter.getDatas().clear();
            adapter.notifyDataSetChanged();
        }
        sendRequest(0, true);
    }

    private void setByFilterDay(int value) {
        paramsData.daysCountMin = value;
        if (value == 1) {
            paramsData.daysCountMax = 1;
        } else {
            paramsData.daysCountMax = 0;
        }
        isFirstRequest = true;
        sendRequest(0, true);
        if (adapter != null && adapter.getDatas() != null) {
            adapter.getDatas().clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void setByFilterTheme(int value) {
        paramsData.themeId = value;
        isFirstRequest = true;
        sendRequest(0, true);
        if (adapter != null && adapter.getDatas() != null) {
            adapter.getDatas().clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void hideFilterView() {
        cityFilterLayout.hideFilterView();
    }

    boolean isClickEvent = false;

    private void showFilterView(final int index,final boolean showFilterView) {
        if(adapter!=null && adapter.getListCount()<3 &&paramsData.cityHomeType==CityHomeType.CITY){
            placeHolderView.show();
            adapter.notifyDataSetChanged();
        }

        int headerFilterTabPaddingTop = cityHomeHeader.getFilterTabTop();
        int titleBarHeight = titlebar.getHeight();
        int distance = headerFilterTabPaddingTop - statusBarHeight - titleBarHeight;

        if (distance > 0) {
            isClickEvent = true;
            recyclerView.smoothScrollBy(0, distance);
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (cityFilterLayout != null) {
                        if(showFilterView){
                            cityFilterLayout.showFilterView(index);
                        }else{
                            cityFilterLayout.onlyShowTab();
                        }
                    }
                }
            },500);
        }else{
            if (cityFilterLayout != null) {
                if (cityFilterLayout != null) {
                    if(showFilterView){
                        cityFilterLayout.showFilterView(index);
                    }else{
                        cityFilterLayout.onlyShowTab();
                    }
                }
            }
        }

    }

    //埋点
    public void setEvent(int type,String value){
        Map<String,String> map=new HashMap<>();
        switch (type){
            case 0:
                typeValue=value;
                map.put("screentype",typeValue);
                MobClickUtils.onEvent(StatisticConstant.GSCREEN_TRIGGER,map);
                break;
            case 1:
                dayValue=value;
                map.put("screentype",typeValue);
                MobClickUtils.onEvent(StatisticConstant.GSCREEN_TRIGGER,map);
                break;
            case 2:
                themesValues=value;
                map.put("screentype",typeValue);
                MobClickUtils.onEvent(StatisticConstant.GSCREEN_TRIGGER,map);
                break;
        }
        StatisticClickEvent.showGscreenClick(StatisticConstant.GSCREEN_CLICK,StatisticConstant.GSCREEN_TRIGGER,themesValues,dayValue,themesValues);
    }
}
