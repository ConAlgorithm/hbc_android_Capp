package com.hugboga.custom.activity;

import android.os.Bundle;
import android.util.Log;
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
import com.hugboga.custom.data.bean.GoodsFilterBean;
import com.hugboga.custom.data.bean.SkuItemBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestGoodsFilter;
import com.hugboga.custom.fragment.SkuScopeFilterFragment;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.HbcLoadingMoreFooter;
import com.hugboga.custom.widget.SkuFilterLayout;
import com.hugboga.custom.widget.SkuItemView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilterSkuListActivity extends BaseActivity implements HbcRecyclerTypeBaseAdpater.OnItemClickListener, XRecyclerView.LoadingListener{

    @Bind(R.id.filter_sku_list_filter_layout)
    SkuFilterLayout filterLayout;
    @Bind(R.id.filter_sku_list_recyclerview)
    XRecyclerView mRecyclerView;

    @Bind(R.id.filter_sku_list_empty_layout)
    LinearLayout emptyLayout;
    @Bind(R.id.filter_sku_list_empty_iv)
    ImageView emptyIV;
    @Bind(R.id.filter_sku_list_empty_hint_tv)
    TextView emptyHintTV;

    private FilterSkuListActivity.Params paramsData;

    private CityListActivity.Params cityParams;
    private SkuScopeFilterFragment.SkuFilterBean skuFilterBean;

    private HbcRecyclerSingleTypeAdpater<SkuItemBean> mAdapter;
    public List<SkuItemBean> listData;

    public static class Params implements Serializable {
        public int id;
        public CityListActivity.CityHomeType cityHomeType;
        public String titleName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            paramsData = (FilterSkuListActivity.Params) savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                paramsData = (FilterSkuListActivity.Params) bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        setContentView(R.layout.activity_filter_sku_list);
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

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setFootView(new HbcLoadingMoreFooter(this));
        mRecyclerView.setLoadingListener(this);
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, SkuItemView.class);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        requestGuideList(null, 0, null, true, 0, true);
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText("包车线路");
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

    @Override
    public void onItemClick(View view, int position, Object itemData) {

    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case GUIDE_FILTER_CITY:
                if (action.getData() instanceof CityListActivity.Params) {
                    paramsData = null;
                    skuFilterBean = null;
                    cityParams = (CityListActivity.Params) action.getData();
                    filterLayout.setCityParams(cityParams);
                    requestGuideList(true);
                }
                break;
            case SKU_FILTER_SCOPE:
                if (action.getData() instanceof SkuScopeFilterFragment.SkuFilterBean) {
                    skuFilterBean = (SkuScopeFilterFragment.SkuFilterBean) action.getData();
                    filterLayout.setSkuFilterBean(skuFilterBean);
                    requestGuideList(false);
                }
                break;
            case FILTER_CLOSE:
                filterLayout.hideFilterView();
                break;
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        Log.i("aa", "onLoadMore");
        requestGuideList(false, mAdapter.getListCount(), false);
    }

    public void requestGuideList(boolean isThemes) {
        requestGuideList(isThemes, 0, true);
    }

    public void requestGuideList(boolean isThemes, int offset, boolean isShowLoading) {
        if (cityParams != null) {
            requestGuideList(cityParams.cityHomeType, cityParams.id, null, isThemes, offset, isShowLoading);
        } else if (paramsData != null) {
            requestGuideList(paramsData.cityHomeType, paramsData.id, null, isThemes, offset, isShowLoading);
        } else {
            requestGuideList(null, 0, null, isThemes, offset, isShowLoading);
        }
    }

    public void requestGuideList(CityListActivity.CityHomeType cityHomeType, int id, String themeIds, boolean isThemes, int offset, boolean isShowLoading) {
        RequestGoodsFilter.Builder builder = new RequestGoodsFilter.Builder();
        int type = -1;//全部
        if (cityHomeType != null && id > 0) {
            switch (cityHomeType) {
                case CITY:
                    type = 3;
                    break;
                case ROUTE:
                    type = 1;
                    break;
                case COUNTRY:
                    type = 2;
                    break;
            }
            builder.id = id;
        }
        builder.type = type;
        builder.limit = Constants.DEFAULT_PAGESIZE;
        builder.offset = offset;
        builder.themeIds = themeIds;
        builder.returnThemes = isThemes;
        if (skuFilterBean != null) {

        }
        requestData(new RequestGoodsFilter(this, builder), isShowLoading);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestGoodsFilter) {
            GoodsFilterBean goodsFilterBean = ((RequestGoodsFilter) _request).getData();
            int offset = _request.getOffset();
            if (offset == 0 && (goodsFilterBean == null || goodsFilterBean.listData == null || goodsFilterBean.listCount <= 0)) {
                setEmptyLayout(true, true);
            } else {
                setEmptyLayout(false, true);
            }
            listData = goodsFilterBean.listData;
            mAdapter.addData(listData, offset > 0);
            if (offset == 0) {
                mRecyclerView.smoothScrollToPosition(0);
            }
            if (hasThemes(_request)) {
                filterLayout.setThemeList(goodsFilterBean.themes);
            }
            mRecyclerView.setNoMore(mAdapter.getListCount() >= goodsFilterBean.listCount);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        if (request instanceof RequestGoodsFilter) {
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
//                    requestGuideList();
                }
            });
        }
    }


    public boolean hasThemes(BaseRequest request) {
        boolean returnThemes = false;
        if (request.map != null && request.map.containsKey("returnThemes") && request.map.get("returnThemes") != null) {
            Object returnThemesObj = request.map.get("returnThemes");
            if (returnThemesObj instanceof Boolean) {
                returnThemes = (Boolean) returnThemesObj;
            }
        }
        return returnThemes;
    }
}
