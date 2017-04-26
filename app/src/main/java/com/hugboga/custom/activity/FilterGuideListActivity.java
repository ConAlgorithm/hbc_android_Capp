package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.adapter.HbcRecyclerTypeBaseAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CapacityBean;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.bean.FilterGuideListBean;
import com.hugboga.custom.data.bean.LineGroupBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestFilterGuide;
import com.hugboga.custom.data.request.RequestMaxCapacityOverall;
import com.hugboga.custom.fragment.GuideFilterFragment;
import com.hugboga.custom.fragment.GuideFilterSortFragment;
import com.hugboga.custom.utils.CityUtils;
import com.hugboga.custom.utils.DatabaseManager;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.GuideFilterLayout;
import com.hugboga.custom.widget.GuideItemView;
import com.hugboga.custom.widget.HbcLoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilterGuideListActivity extends BaseActivity implements HbcRecyclerTypeBaseAdpater.OnItemClickListener, XRecyclerView.LoadingListener{

    @Bind(R.id.guide_list_filter_layout)
    GuideFilterLayout filterLayout;
    @Bind(R.id.guide_list_recyclerview)
    XRecyclerView mRecyclerView;

    @Bind(R.id.guide_list_empty_layout)
    LinearLayout emptyLayout;
    @Bind(R.id.guide_list_empty_iv)
    ImageView emptyIV;
    @Bind(R.id.guide_list_empty_hint_tv)
    TextView emptyHintTV;

    private Params paramsData;

    private CityListActivity.Params cityParams;
    private GuideFilterFragment.GuideFilterBean guideFilterBean;
    private GuideFilterSortFragment.SortTypeBean sortTypeBean;

    private HbcRecyclerSingleTypeAdpater<FilterGuideBean> mAdapter;
    private ArrayList<FilterGuideBean> guideList;
    private CityListActivity.CityHomeType lastCityHomeType;//用来判断是否显示当前城市

    public static class Params implements Serializable {
        public int id;
        public CityListActivity.CityHomeType cityHomeType;
        public String titleName;
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
        setContentView(R.layout.activity_filter_guide_list);
        ButterKnife.bind(this);
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
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setFootView(new HbcLoadingMoreFooter(this));
        mRecyclerView.setLoadingListener(this);
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, GuideItemView.class);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        requestMaxCapacityOverall();
        requestGuideList();
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText("精选司导");
        fgRightTV.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        //没有下拉刷新
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

    public void requestGuideList(CityListActivity.CityHomeType cityHomeType, int id, int offset, boolean isShowLoading) {
        lastCityHomeType = cityHomeType;
        RequestFilterGuide requestFilterGuide = new RequestFilterGuide(this, getRequestBuilder(cityHomeType, id, offset));
        requestData(requestFilterGuide, isShowLoading);
    }

    // 可服务车型最大乘坐人数
    public void requestMaxCapacityOverall() {
        requestData(new RequestMaxCapacityOverall(this));
    }

    public boolean isShowCity() {
        if (lastCityHomeType != null && lastCityHomeType == CityListActivity.CityHomeType.CITY) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onItemClick(View view, int position, Object itemData) {
        GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
        params.guideId = guideList.get(position).guideId;
        Intent intent = new Intent(FilterGuideListActivity.this, GuideWebDetailActivity.class);
        intent.putExtra(Constants.PARAMS_DATA, params);
        startActivity(intent);
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case GUIDE_FILTER_CITY:
                if (action.getData() instanceof CityListActivity.Params) {
                    paramsData = null;
                    guideFilterBean = null;
                    sortTypeBean = null;

                    cityParams = (CityListActivity.Params) action.getData();
                    filterLayout.setCityParams(cityParams);
                    requestGuideList();

                    if (cityParams.cityHomeType == CityListActivity.CityHomeType.CITY) {
                        CityBean cityBean = DatabaseManager.getCityBean("" + cityParams.id);
                        if (cityBean != null && !TextUtils.isEmpty(cityBean.placeId)) {
                            LineGroupBean getLineGroupBean = CityUtils.getLineGroupBean(this, cityBean.placeId);
                        }
                    }
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

    public RequestFilterGuide.Builder getRequestBuilder(CityListActivity.CityHomeType cityHomeType, int id, int offset) {
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
        }
        if (guideFilterBean != null) {
            builder.setGenders(guideFilterBean.getGendersRequestParams());
            builder.setServiceTypes(guideFilterBean.getCharterRequestParams());
            builder.setGuestNum("" + guideFilterBean.travelerCount);
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
            guideList = filterGuideListBean.listData;
            mAdapter.addData(guideList, offset > 0);
            if (offset == 0) {
                mRecyclerView.smoothScrollToPosition(0);
            }
            mRecyclerView.setNoMore(mAdapter.getListCount() >= filterGuideListBean.listCount);
        } else if (_request instanceof RequestMaxCapacityOverall) {
            CapacityBean capacityBean = ((RequestMaxCapacityOverall) _request).getData();
            filterLayout.setCapacityBean(capacityBean);
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
        }
        hideFilterView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        if (isDataNull) {
            params.addRule(RelativeLayout.BELOW, R.id.guide_list_filter_layout);
            emptyLayout.setLayoutParams(params);

            emptyIV.setBackgroundResource(R.drawable.empty_city);
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
}
