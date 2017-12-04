package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.widget.recycler.ZListPageView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.NewOrderAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.event.EventAction;
import com.hugboga.custom.data.request.RequestOrder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * Created by on 16/8/9.
 */
public class NewOrderActivity extends BaseActivity implements ZBaseAdapter.OnItemClickListener{

    public static final String SEARCH_TYPE = "search_type";
    public static final String SEARCH_USER = "search_user";

    @BindView(R.id.listview)
    ZListPageView recyclerView;
    @BindView(R.id.swipe)
    ZSwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.order_empty)
    RelativeLayout emptyLayout;

    private NewOrderAdapter adapter;
    Bundle bundle;

    @Override
    public int getContentViewId() {
        return R.layout.activity_new_order;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        SearchType searchType = SearchType.getSerachType(bundle.getInt(SEARCH_TYPE, SearchType.SEARCH_TYPE_HISTORY.getType()));
        initDefaultTitleBar();
        initTitle(searchType);
        fgTitle.setText(R.string.letter_chat_btn);
        fgRightTV.setVisibility(View.GONE);

        adapter = new NewOrderAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setzSwipeRefreshLayout(swipeRefreshLayout);
        recyclerView.setEmptyLayout(emptyLayout);
        recyclerView.setRequestData(getRequest(searchType));
        recyclerView.setOnItemClickListener(this);
        loadData();
    }

    @Subscribe
    public void onEventMainThread(EventAction action) {
        switch (action.getType()) {
            case ORDER_DETAIL_UPDATE_COLLECT:
            case ORDER_DETAIL_UPDATE_EVALUATION:
                loadData();
                break;
        }
    }

    /**
     * 获取请求的数据Request
     *
     * @param searchType
     * @return
     */
    private BaseRequest getRequest(SearchType searchType) {
        switch (searchType) {
            case SEARCH_TYPE_NEW:
                return new RequestOrder(this, bundle.getString(SEARCH_USER, ""));
            case SEARCH_TYPE_HISTORY:
                return new RequestOrder(this, bundle.getString(SEARCH_USER, ""));
            default:
                return new RequestOrder(this, bundle.getString(SEARCH_USER, ""));
        }
    }

    /**
     * 初始化标题
     * searchType为查询类型
     * 1：新订单
     * 2：历史订单
     *
     * @param searchType
     */
    private void initTitle(SearchType searchType) {
        switch (searchType) {
            case SEARCH_TYPE_NEW:
                fgTitle.setText(getString(R.string.letter_chat_btn));
                break;
            case SEARCH_TYPE_HISTORY:
                fgTitle.setText(getString(R.string.letter_chat_btn));
                break;
            default:
                break;
        }
    }

    /**
     * 加载数据
     */
    public void loadData() {
        if (recyclerView != null) {
            recyclerView.showPageFirst();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getParent() == recyclerView) {
            OrderBean order = adapter.getDatas().get(position);
            if (order != null) {
                // 跳转到订单详情
                OrderDetailActivity.Params params = new OrderDetailActivity.Params();
                params.orderType = order.orderType;
                params.orderId = order.orderNo;
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PARAMS_DATA, params);
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra(Constants.PARAMS_DATA, params);
                startActivity(intent);
            }
        }
    }

    @Override
    public String getEventSource() {
        return "历史订单";
    }

    //    ===========枚举部分======================
    public enum SearchType {

        SEARCH_TYPE_NEW(1), //新订单
        SEARCH_TYPE_HISTORY(2); //历史订单

        private int type;

        SearchType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public static SearchType getSerachType(Integer type) {
            switch (type) {
                case 1:
                    return SEARCH_TYPE_NEW;
                case 2:
                    return SEARCH_TYPE_HISTORY;
                default:
                    return SEARCH_TYPE_NEW;
            }
        }
    }
}
