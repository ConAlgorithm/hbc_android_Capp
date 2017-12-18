package com.hugboga.custom.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.DestinationListBean;
import com.hugboga.custom.data.bean.DestinationTabItemBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.bean.UserFavoriteLineList;
import com.hugboga.custom.data.bean.city.DestinationGoodsVo;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.FavoriteLinesaved;
import com.hugboga.custom.data.request.RequestDestinationList;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.HbcLoadingMoreFooter;
import com.hugboga.custom.widget.LoadMoreRecyclerView;
import com.hugboga.custom.widget.city.CitySkuView;
import com.hugboga.custom.widget.title.TitleBar;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class DestinationListActivity extends BaseActivity implements XRecyclerView.LoadingListener{

    @BindView(R.id.destination_list_titlebar)
    TitleBar titlebar;
    @BindView(R.id.destination_list_recyclerview)
    LoadMoreRecyclerView recyclerView;
    @BindView(R.id.destination_list_empty)
    LinearLayout emptyView;

    private HbcRecyclerSingleTypeAdpater<DestinationGoodsVo> adapter;
    private DestinationTabItemBean.TagItemBean tagItemBean;
    private DestinationListBean destinationListBean;
    private UserFavoriteLineList userFavoriteLineList;

    @Override
    public int getContentViewId() {
        return R.layout.activity_destination_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            tagItemBean = (DestinationTabItemBean.TagItemBean)savedInstanceState.getSerializable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                tagItemBean = (DestinationTabItemBean.TagItemBean)bundle.getSerializable(Constants.PARAMS_DATA);
            }
        }
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.PARAMS_DATA, tagItemBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        titlebar.setTitle(tagItemBean.tagName);

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFootView(new HbcLoadingMoreFooter(this));
        recyclerView.setLoadingListener(this);
        adapter = new HbcRecyclerSingleTypeAdpater(this, CitySkuView.class);
        recyclerView.setAdapter(adapter);

        requestList(0, true);
    }

    public void requestList(int offset, boolean isShowLoading) {
        RequestDestinationList requestDestinationList = new RequestDestinationList(this, tagItemBean.tagId, offset);
        requestData(requestDestinationList, isShowLoading);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestDestinationList) {
            destinationListBean = ((RequestDestinationList)_request).getData();
            int offset = _request.getOffset();
            adapter.addData(destinationListBean.tagGoodsList, offset > 0);
            if (offset == 0) {
                recyclerView.smoothScrollToPosition(0);
            }
            recyclerView.setNoMore(adapter.getListCount() >= destinationListBean.tagGoodsCount);
            requestFavoriteLinesaved();
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else if (_request instanceof FavoriteLinesaved) {
            onRequestFavoriteLineSucceed(_request);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest _request) {
        super.onDataRequestError(errorInfo, _request);
        if (_request instanceof RequestDestinationList) {
            int offset = _request.getOffset();
            if (offset == 0) {
                emptyView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.destination_list_empty)
    public void onEmptyRefreshData() {
        requestList(0, true);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        requestList(adapter.getListCount(), false);
    }

    private boolean isListEmpty() {
        if (destinationListBean != null && destinationListBean.tagGoodsList != null && destinationListBean.tagGoodsList.size() > 0) {
            return false;
        } else {
            return true;
        }
    }


    // -----**  收藏功能历史遗留，我也很无奈--！，待优化 **-----
    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case CLICK_USER_LOGIN:
                if (!isListEmpty()) {
                    FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(this, UserEntity.getUser().getUserId(this));
                    HttpRequestUtils.request(this, favoriteLinesaved, this, false);
                }
                break;
            case CLICK_USER_LOOUT:
                if (!isListEmpty()) {
                    int size = destinationListBean.tagGoodsList.size();
                    for (int i = 0; i < size; i++) {
                        destinationListBean.tagGoodsList.get(i).isCollected = 0;
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case LINE_UPDATE_COLLECT:
                requestFavoriteLinesaved();
                break;
        }
    }

    private void requestFavoriteLinesaved() {
        if (UserEntity.getUser().isLogin(this)) {
            FavoriteLinesaved favoriteLinesaved = new FavoriteLinesaved(this, UserEntity.getUser().getUserId(this));
            HttpRequestUtils.request(this, favoriteLinesaved, this, false);
        }
    }

    private void onRequestFavoriteLineSucceed(BaseRequest _request) {
        if (isListEmpty()) {
            return;
        }
        int size = destinationListBean.tagGoodsList.size();
        for (int i = 0; i < size; i++) {
            destinationListBean.tagGoodsList.get(i).isCollected = 0;
        }
        //所有线路的收藏状态同步在此
        if (_request.getData() instanceof UserFavoriteLineList) {
            userFavoriteLineList = (UserFavoriteLineList) _request.getData();
            for (int o = 0; o < userFavoriteLineList.goodsNos.size(); o++) {
                for (int k = 0; k < destinationListBean.tagGoodsList.size(); k++) {
                    if (TextUtils.equals(userFavoriteLineList.goodsNos.get(o), destinationListBean.tagGoodsList.get(k).goodsNo)) {
                        destinationListBean.tagGoodsList.get(k).isCollected = 1;
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
