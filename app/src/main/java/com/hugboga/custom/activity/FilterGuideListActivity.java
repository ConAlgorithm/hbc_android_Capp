package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.adapter.HbcRecyclerTypeBaseAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.bean.FilterGuideListBean;
import com.hugboga.custom.data.bean.FilterGuideOptionsBean;
import com.hugboga.custom.data.bean.GuidesDetailData;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestFilterGuide;
import com.hugboga.custom.data.request.RequestGoodsById;
import com.hugboga.custom.data.request.RequestGuideFilterOptions;
import com.hugboga.custom.fragment.GuideFilterFragment;
import com.hugboga.custom.fragment.GuideFilterSortFragment;
import com.hugboga.custom.statistic.StatisticConstant;
import com.hugboga.custom.statistic.click.StatisticClickEvent;
import com.hugboga.custom.utils.DBHelper;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.GuideFilterLayout;
import com.hugboga.custom.widget.GuideItemView;
import com.hugboga.custom.widget.HbcLoadingMoreFooter;
import com.hugboga.custom.widget.LoadMoreRecyclerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class FilterGuideListActivity extends BaseActivity implements HbcRecyclerTypeBaseAdpater.OnItemClickListener, LoadMoreRecyclerView.LoadingListener{

    @BindView(R.id.guide_list_filter_layout)
    GuideFilterLayout filterLayout;
    @BindView(R.id.guide_list_recyclerview)
    LoadMoreRecyclerView mRecyclerView;
    @BindView(R.id.guide_list_ptrframe_layout)
    PtrFrameLayout ptrFrameLayout;
    @BindView(R.id.guide_list_empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.guide_list_empty_iv)
    ImageView emptyIV;
    @BindView(R.id.guide_list_empty_hint_tv)
    TextView emptyHintTV;

    private Params paramsData;
    private SkuItemBean skuItemBean;//sku详情
    private CityBean cityBean;
    private CityActivity.Params cityParams;
    private GuideFilterFragment.GuideFilterBean guideFilterBean;
    private GuideFilterSortFragment.SortTypeBean sortTypeBean;

    private HbcRecyclerSingleTypeAdpater<FilterGuideBean> mAdapter;
    private CityActivity.CityHomeType lastCityHomeType;//用来判断是否显示当前城市

    public static class Params implements Serializable {
        public int id;
        public CityActivity.CityHomeType cityHomeType;
        public String titleName;
        public String goodsNo;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_filter_guide_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (FilterGuideListActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (FilterGuideListActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private boolean hideFilterView() {
        if (filterLayout.isShowFilterView()) {
            filterLayout.hideFilterView();
            return true;
        } else {
            return false;
        }
    }

    private void initView() {
        initTitleBar();

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setFootView(new HbcLoadingMoreFooter(this));
        mRecyclerView.setLoadingListener(this);
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, GuideItemView.class);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        initHeader();

        if (paramsData != null) {
            CityActivity.Params params = new CityActivity.Params();
            params.cityHomeType = paramsData.cityHomeType;
            params.id = paramsData.id;
            params.titleName = paramsData.titleName;
            filterLayout.initCityFilter(params);
            if (isGoods()) {
                filterLayout.hideCityLayout();
            }
        }

        requestGuideFilterOptions();
        requestGuideList();
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText(isGoods() ? R.string.service_guide_list_title : R.string.guide_list_title);
        fgRightTV.setVisibility(View.GONE);
    }

    public void initHeader() {
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(this);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                requestGuideList(0, false);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }

    public boolean isGoods() {
        if (paramsData != null && !TextUtils.isEmpty(paramsData.goodsNo)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getEventSource() {
        return "精选司导";
    }

    @Override
    public String getEventId() {
        return StatisticConstant.LAUNCH_GLIST;
    }

    @Override
    public void onLoadMore() {
        requestGuideList(mAdapter.getListCount(), false);
    }

    public void requestGuideList() {
        requestGuideList(0, true);
    }

    public void requestGuideList(int offset, boolean isShowLoading) {
        if (cityParams != null) {
            requestGuideList(cityParams.cityHomeType, cityParams.id, offset, isShowLoading);
        } else if (paramsData != null) {
            requestGuideList(paramsData.cityHomeType, paramsData.id, offset, isShowLoading);
        } else {
            requestGuideList(null, 0, offset, isShowLoading);
        }
    }

    public void requestGuideList(CityActivity.CityHomeType cityHomeType, int id, int offset, boolean isShowLoading) {
        lastCityHomeType = cityHomeType;
        RequestFilterGuide requestFilterGuide = new RequestFilterGuide(this, getRequestBuilder(cityHomeType, id, offset));
        requestData(requestFilterGuide, isShowLoading);
    }

    public void requestGuideFilterOptions() {
        if (isGoods()) {
            requestData(new RequestGuideFilterOptions(this, paramsData.goodsNo));
            RequestGoodsById request = new RequestGoodsById(activity, paramsData.goodsNo, ""+paramsData.id);  //请求商品信息 “找我预定” 使用
            HttpRequestUtils.request(activity, request, FilterGuideListActivity.this, false);
        } else {
            CityActivity.CityHomeType cityHomeType = null;
            String id = "";
            if (cityParams != null) {
                cityHomeType = cityParams.cityHomeType;
                id = "" + cityParams.id;
            } else if (paramsData != null) {
                cityHomeType = paramsData.cityHomeType;
                id = "" + paramsData.id;
            }
            requestData(new RequestGuideFilterOptions(this, cityHomeType, id));
        }
    }

    public boolean isShowCity() {
        if (lastCityHomeType != null && lastCityHomeType == CityActivity.CityHomeType.CITY) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onItemClick(View view, int position, Object itemData) {
        if (position >= mAdapter.getListCount()) {
            mAdapter.notifyDataSetChanged();
            return;
        }
        GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
        ArrayList<FilterGuideBean> guideList = mAdapter.getDatas();
        params.guideId = guideList.get(position).guideId;
        Intent intent = new Intent(FilterGuideListActivity.this, GuideWebDetailActivity.class);
        intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case GUIDE_FILTER_CITY:
                if (action.getData() instanceof CityActivity.Params) {
                    paramsData = null;
                    cityParams = (CityActivity.Params) action.getData();
                    filterLayout.setCityParams(cityParams);
                    guideFilterBean = null;
                    requestGuideFilterOptions();
                    requestGuideList();
                }
                break;
            case GUIDE_FILTER_SCOPE:
                if (action.getData() instanceof GuideFilterFragment.GuideFilterBean) {
                    guideFilterBean = (GuideFilterFragment.GuideFilterBean) action.getData();
                    filterLayout.setGuideFilterBean(guideFilterBean);
                    requestGuideList();
                }
                break;
            case GUIDE_FILTER_SORT:
                if (action.getData() instanceof GuideFilterSortFragment.SortTypeBean) {
                    sortTypeBean= (GuideFilterSortFragment.SortTypeBean) action.getData();
                    filterLayout.setSortTypeBean(sortTypeBean);
                    requestGuideList();
                }
                break;
            case FILTER_CLOSE:
                filterLayout.hideFilterView();
                break;
        }
    }

    public RequestFilterGuide.Builder getRequestBuilder(CityActivity.CityHomeType cityHomeType, int id, int offset) {
        RequestFilterGuide.Builder builder = new RequestFilterGuide.Builder();
        if (cityHomeType != null && id > 0) {
            switch (cityHomeType) {
                case CITY:
                    builder.setCityIds("" + id);
                    break;
                case ROUTE:
                    builder.setLineGroupId("" + id);
                    break;
                case COUNTRY:
                    builder.setCountryId("" + id);
                    break;
            }
        } else if (isGoods()) {
            builder.setGoodsNo(paramsData.goodsNo);
        }

        if (guideFilterBean != null) {
            builder.setGenders(guideFilterBean.getGendersRequestParams());
            builder.setServiceTypes(guideFilterBean.getCharterRequestParams());
            builder.setGuestNum("" + guideFilterBean.travelerCount);
            builder.setForeignLanguageIds(guideFilterBean.getForeignLanguageRequestParams());
            builder.setLocalLanguageIds(guideFilterBean.getLocalLanguageRequestParams());
            builder.setLabelIds(guideFilterBean.getSkillRequestParams());
        }
        if (sortTypeBean != null) {
            builder.setOrderByType(sortTypeBean.type);
        }
        builder.setOffset(offset);
        return builder;
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestFilterGuide) {
            FilterGuideListBean filterGuideListBean = ((RequestFilterGuide) _request).getData();
            int offset = _request.getOffset();
            if (offset == 0 && (filterGuideListBean == null || filterGuideListBean.listData == null || filterGuideListBean.listCount <= 0)) {
                setEmptyLayout(true, true);
                return;
            } else {
                setEmptyLayout(false, true);
            }
            mAdapter.addData(filterGuideListBean.listData, offset > 0);
            if (offset == 0) {
                mRecyclerView.smoothScrollToPosition(0);
            }
            ptrFrameLayout.refreshComplete();
            mRecyclerView.setNoMore(mAdapter.getListCount() >= filterGuideListBean.listCount);
        } else if (_request instanceof RequestGuideFilterOptions) {
            FilterGuideOptionsBean filterGuideOptionsBean = ((RequestGuideFilterOptions) _request).getData();
            filterGuideOptionsBean.setMandarinBean();
            filterLayout.setFilterGuideOptionsBean(filterGuideOptionsBean);
        }else  if (_request instanceof RequestGoodsById) {
            RequestGoodsById requestGoodsById = (RequestGoodsById) _request;
            skuItemBean = requestGoodsById.getData();
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (request instanceof RequestFilterGuide) {
            int offset = request.getOffset();
            if (offset == 0) {
                setEmptyLayout(true, false);
            }
        }
    }

    private void setEmptyLayout(boolean isShow, boolean isDataNull) {
        emptyLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (!isShow) {
            return;
        } else {
            mAdapter.clearData();
        }
        hideFilterView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        if (isDataNull) {
            params.addRule(RelativeLayout.BELOW, R.id.guide_list_filter_layout);
            emptyLayout.setLayoutParams(params);

            emptyIV.setBackgroundResource(R.drawable.empty_car);
            emptyHintTV.setText("暂无满足当前筛选条件的司导");
            emptyLayout.setEnabled(false);
        } else {
            params.addRule(RelativeLayout.BELOW, R.id.guide_list_titlebar);
            emptyLayout.setLayoutParams(params);

            emptyIV.setBackgroundResource(R.drawable.empty_wifi);
            emptyHintTV.setText("似乎与网络断开，点击屏幕重试");
            emptyLayout.setEnabled(true);
            emptyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestGuideList();
                }
            });
        }
    }

    public void orderJumpDate(String guideId, String guideName) {
        if (skuItemBean == null) {

            return;
        }
        if (cityBean == null) {
            cityBean = findCityById("" + skuItemBean.depCityId);
        }
        SkuOrderActivity.Params params = new SkuOrderActivity.Params();
        params.skuItemBean = skuItemBean;
        params.cityBean = cityBean;
        GuidesDetailData guidesDetailData = new GuidesDetailData();
        guidesDetailData.guideId = guideId;
        guidesDetailData.guideName = guideName;
        params.guidesDetailData = guidesDetailData;
        Intent intent = new Intent(activity, SkuDateActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        intent.putExtra(Constants.PARAMS_SOURCE, getIntentSource());
        startActivity(intent);
        StatisticClickEvent.click(StatisticConstant.CLICK_SKUDATE);

    }

    private CityBean findCityById(String cityId) {
        DbManager mDbManager = new DBHelper(FilterGuideListActivity.this).getDbManager();
        try {
            cityBean = mDbManager.findById(CityBean.class, cityId);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return cityBean;
    }
}
