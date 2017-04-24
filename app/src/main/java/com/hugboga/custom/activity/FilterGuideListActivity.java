package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

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
import com.hugboga.custom.widget.ChoicenessGuideView;
import com.hugboga.custom.widget.GuideFilterLayout;

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

    private Params paramsData;

    private CityListActivity.Params cityParams;
    private GuideFilterFragment.GuideFilterBean guideFilterBean;
    private GuideFilterSortFragment.SortTypeBean sortTypeBean;

    private HbcRecyclerSingleTypeAdpater<FilterGuideBean> mAdapter;
    private ArrayList<FilterGuideBean> guideList;

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
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, ChoicenessGuideView.class);
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
        requestData(new RequestFilterGuide(this, getRequestBuilder(cityHomeType, id)));
    }

    @Override
    public void onItemClick(View view, int position, Object itemData) {
//        guideList
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case GUIDE_FILTER_CITY:
                guideFilterBean = null;
                sortTypeBean = null;
                if (action.getData() instanceof CityListActivity.Params) {
                    cityParams = (CityListActivity.Params) action.getData();
                    filterLayout.setCityParams(cityParams);
                    requestGuideList(cityParams.cityHomeType, cityParams.id);
                } else {
                    cityParams = null;
                    requestGuideList(null, 0);
                }
                break;
            case GUIDE_FILTER_SORT:
                if (action.getData() instanceof GuideFilterSortFragment.SortTypeBean) {
                    sortTypeBean = (GuideFilterSortFragment.SortTypeBean) action.getData();
                    filterLayout.setSortTypeBean(sortTypeBean);
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
        }
    }
}
