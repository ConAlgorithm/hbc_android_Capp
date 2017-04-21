package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestFilterGuide;
import com.hugboga.custom.fragment.GuideFilterFragment;
import com.hugboga.custom.fragment.GuideFilterSortFragment;
import com.hugboga.custom.widget.GuideFilterLayout;
import com.hugboga.custom.widget.GuideItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilterGuideListActivity extends BaseActivity implements HbcRecyclerTypeBaseAdpater.OnItemClickListener{

    @Bind(R.id.guide_list_filter_layout)
    GuideFilterLayout filterLayout;
    @Bind(R.id.guide_list_recyclerview)
    RecyclerView mRecyclerView;

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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (hideFilterView()) {
                return true;
            } else {
                return super.onKeyUp(keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, GuideItemView.class);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        requestGuideList();
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText("精选司导");
        fgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hideFilterView()) {
                   finish();
                }
            }
        });
        fgRightTV.setVisibility(View.GONE);
    }

    public void requestGuideList() {
        if (cityParams != null) {
            requestGuideList(cityParams.cityHomeType, cityParams.id);
        } else if (paramsData != null) {
            requestGuideList(paramsData.cityHomeType, paramsData.id);
        } else {
            requestGuideList(null, 0);
        }
    }

    public void requestGuideList(CityListActivity.CityHomeType cityHomeType, int id) {
        lastCityHomeType = cityHomeType;
        requestData(new RequestFilterGuide(this, getRequestBuilder(cityHomeType, id)));
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
//        guideList
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

                    requestGuideList(cityParams.cityHomeType, cityParams.id);
                }
                break;
            case GUIDE_FILTER_SORT:
                if (action.getData() instanceof GuideFilterSortFragment.SortTypeBean) {
                    GuideFilterSortFragment.SortTypeBean _sortTypeBean = (GuideFilterSortFragment.SortTypeBean) action.getData();
                    filterLayout.setSortTypeBean(sortTypeBean);
                    if (_sortTypeBean == sortTypeBean) {
                        return;
                    }
                    sortTypeBean = _sortTypeBean;
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
            case GUIDE_FILTER_CLOSE:
                filterLayout.hideFilterView();
                break;
        }
    }

    public RequestFilterGuide.Builder getRequestBuilder(CityListActivity.CityHomeType cityHomeType, int id) {
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
                    builder.setCoutryId("" + id);
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
        return builder;
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if(_request instanceof RequestFilterGuide) {
            guideList = ((RequestFilterGuide) _request).getData();
            mAdapter.addData(guideList);
            if (guideList == null || guideList.size() <= 0) {
                setEmptyLayout(true, true);
            } else {
                setEmptyLayout(false, true);
            }
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        setEmptyLayout(true, false);
    }

    private boolean checkCityParamsChange(CityListActivity.Params params1, CityListActivity.Params params2) {
        if (params1 == null || params2 == null) {
            return true;
        }
        if (params1.id == params2.id && params1.cityHomeType == params2.cityHomeType && TextUtils.equals(params1.titleName, params2.titleName)) {
            return false;
        }
        return true;
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
