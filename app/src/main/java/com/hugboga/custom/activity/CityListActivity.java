package com.hugboga.custom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.CityListAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CityListBean;
import com.hugboga.custom.data.bean.CountryGroupBean;
import com.hugboga.custom.data.bean.FilterGuideBean;
import com.hugboga.custom.data.request.RequestCityHomeList;
import com.hugboga.custom.data.request.RequestCountryGroup;
import com.hugboga.custom.data.request.RequestFilterGuide;
import com.hugboga.custom.utils.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by qingcha on 17/4/14.
 */
public class CityListActivity extends BaseActivity{

    public static final int GUIDE_LIST_COUNT = 8;//精选司导显示的条数

    @Bind(R.id.city_list_titlebar)
    RelativeLayout titlebar;
    @Bind(R.id.view_bottom)
    View titlebarBottomLineView;
    @Bind(R.id.city_list_recyclerview)
    RecyclerView recyclerView;

    @Bind(R.id.city_list_back_layout)
    FrameLayout backLayout;

    @Bind(R.id.city_list_empty_layout)
    LinearLayout emptyLayout;
    @Bind(R.id.city_list_empty_iv)
    ImageView emptyIV;
    @Bind(R.id.city_list_empty_hint_tv)
    TextView emptyHintTV;

    public CityListActivity.Params paramsData;
    private CityListAdapter cityListAdapter;

    public enum CityHomeType {
        CITY, ROUTE, COUNTRY, ALL
    }

    public static class Params implements Serializable {
        public int id;
        public CityListActivity.CityHomeType cityHomeType;
        public String titleName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (CityListActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (CityListActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.activity_city_list);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (paramsData != null) {
            outState.putSerializable(Constants.PARAMS_DATA, paramsData);
        }
    }

    protected void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText(paramsData.titleName);
        fgTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.share_unfold, 0);
        fgTitle.setCompoundDrawablePadding(UIUtils.dip2px(3));
        if (paramsData.cityHomeType == CityListActivity.CityHomeType.CITY) {
            titlebar.setBackgroundColor(0x00000000);
            titlebarBottomLineView.setBackgroundColor(0x00000000);
            fgTitle.setAlpha(0);
            fgLeftBtn.setAlpha(0);
            backLayout.setVisibility(View.VISIBLE);
        } else {
            backLayout.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.topMargin = UIUtils.getActionBarSize();
            recyclerView.setLayoutParams(params);
        }
        fgTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ChooseCityNewActivity.class);
                intent.putExtra("com.hugboga.custom.home.flush", Constants.BUSINESS_TYPE_RECOMMEND);
                intent.putExtra("isHomeIn", false);
                intent.putExtra("source", getEventSource());
                context.startActivity(intent);
            }
        });
    }

    public void initView() {
        initTitleBar();
        cityListAdapter = new CityListAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cityListAdapter);
        requestCityList();
        setOnScrollListener();
    }

    public void setOnScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (paramsData.cityHomeType == CityListActivity.CityHomeType.CITY && cityListAdapter.cityListHeaderModel != null) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    int scrollY = Math.abs(recyclerView.getChildAt(0).getTop());
                    float showRegionHight = cityListAdapter.cityListHeaderModel.getDisplayLayoutHeight() / 2.0f;
                    if (firstVisibleItemPosition == 0 && scrollY <= showRegionHight) {
                        float alpha;
                        if (scrollY <= 0) {
                            alpha = 0.0f;
                        } else {
                            alpha = Math.min(1, scrollY / showRegionHight);
                        }
                        titlebar.setBackgroundColor(UIUtils.getColorWithAlpha(alpha, 0xFFFFFFFF));
                        titlebarBottomLineView.setBackgroundColor(UIUtils.getColorWithAlpha(alpha, 0xFFE5E5E5));
                        backLayout.setAlpha(1 - alpha);
                        fgLeftBtn.setAlpha(alpha);
                        fgTitle.setAlpha(0);
                        fgTitle.setEnabled(false);
                    } else {
                        showTitleBar();
                    }
                }
            }
        });
    }

    private void showTitleBar() {
        titlebar.setBackgroundColor(0xFFFFFFFF);
        titlebarBottomLineView.setBackgroundColor(0xFFE5E5E5);
        fgTitle.setEnabled(true);
        fgTitle.setAlpha(1);
        fgLeftBtn.setAlpha(1);
        backLayout.setAlpha(0);
    }

    public void requestCityList() {
        BaseRequest baseRequest = null;
        switch (paramsData.cityHomeType) {
            case CITY:
                baseRequest = new RequestCityHomeList(this, "" + paramsData.id);
                break;
            case ROUTE:
                baseRequest = new RequestCountryGroup(this, paramsData.id, "1");
                break;
            case COUNTRY:
                baseRequest = new RequestCountryGroup(this, paramsData.id, "2");
                break;
        }
        requestData(baseRequest);
    }

    public void requestGuideList() {
        RequestFilterGuide.Builder builder = new RequestFilterGuide.Builder();
        switch (paramsData.cityHomeType) {
            case CITY:
                builder.setCityIds("" + paramsData.id);
                break;
            case ROUTE:
                builder.setLineGroupId("" + paramsData.id);
                break;
            case COUNTRY:
                builder.setCoutryId("" + paramsData.id);
                break;
        }
        builder.setLimit(GUIDE_LIST_COUNT);
        requestData(new RequestFilterGuide(this, builder));
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCityHomeList) {
            CityListBean cityListBean = ((RequestCityHomeList) _request).getData();
            boolean isCanService = cityListBean != null && cityListBean.isCanService();
            if (isCanService) {
                setEmptyLayout(false, true);
                cityListAdapter.setCityData(cityListBean);
                fgTitle.setText(cityListBean.cityContent.cityName);
                requestGuideList();
            } else {
                setEmptyLayout(true, true);
            }
        } else if (_request instanceof RequestCountryGroup) {
            setEmptyLayout(false, true);
            CountryGroupBean countryGroupBean = ((RequestCountryGroup) _request).getData();
            cityListAdapter.setCountryGroupData(countryGroupBean);
            requestGuideList();
        } else if(_request instanceof RequestFilterGuide) {
            setEmptyLayout(false, true);
            ArrayList<FilterGuideBean> guideList = ((RequestFilterGuide) _request).getData();
            cityListAdapter.setGuideListData(guideList);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (request instanceof RequestCityHomeList || request instanceof RequestCountryGroup) {
            setEmptyLayout(true, false);
        }
    }

    @Override
    public String getEventSource() {
        String result = "";
        switch (paramsData.cityHomeType) {
            case CITY:
                result = "城市页";
                break;
            case ROUTE:
                result = "线路页";
                break;
            case COUNTRY:
                result = "国家页";
                break;
        }
        return result;
    }

    private void setEmptyLayout(boolean isShow, boolean isDataNull) {
        emptyLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (!isShow) {
            return;
        }
        if (isDataNull) {
            if (paramsData.cityHomeType == CityHomeType.CITY) {
                emptyIV.setBackgroundResource(R.drawable.empty_city);
                emptyHintTV.setText("很抱歉该地区还未开通服务");
                emptyLayout.setEnabled(false);

                showTitleBar();
            }
        } else {
            emptyIV.setBackgroundResource(R.drawable.empty_wifi);
            emptyHintTV.setText("似乎与网络断开，点击屏幕重试");
            emptyLayout.setEnabled(true);
            emptyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestCityList();
                }
            });
        }
    }
}
