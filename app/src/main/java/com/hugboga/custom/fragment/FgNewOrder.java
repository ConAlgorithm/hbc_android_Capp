package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.widget.recycler.ZListPageView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.NewOrderAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.request.RequestOrder;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_new_order)
public class FgNewOrder extends BaseFragment implements ZBaseAdapter.OnItemClickListener {

    public static final String SEARCH_TYPE = "search_type";
    public static final String SEARCH_USER = "search_user";

    @ViewInject(R.id.listview)
    ZListPageView recyclerView;
    @ViewInject(R.id.swipe)
    ZSwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.order_empty)
    RelativeLayout emptyLayout;

    private NewOrderAdapter adapter;
    Bundle bundle;

    @Override
    protected void initHeader() {
       fgTitle.setText(R.string.letter_chat_btn);
        fgRightBtn.setVisibility(View.GONE);
    }

    @Override
    protected void initView() {
        bundle = getArguments();
        if (bundle == null) {
            return;
        }
        SearchType searchType = SearchType.getSerachType(bundle.getInt(SEARCH_TYPE, SearchType.SEARCH_TYPE_HISTORY.getType()));
        initTitle(searchType);
        initListView(searchType);
    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

    /**
     * 设置列表数据
     *
     * @param searchType
     */
    private void initListView(SearchType searchType) {
        adapter = new NewOrderAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setzSwipeRefreshLayout(swipeRefreshLayout);
        recyclerView.setEmptyLayout(emptyLayout);
        recyclerView.setRequestData(getRequest(searchType));
        recyclerView.setOnItemClickListener(this);
        loadData();
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
                return new RequestOrder(getActivity(), bundle.getString(SEARCH_USER, ""));
            case SEARCH_TYPE_HISTORY:
                return new RequestOrder(getActivity(), bundle.getString(SEARCH_USER, ""));
            default:
                return new RequestOrder(getActivity(), bundle.getString(SEARCH_USER, ""));
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
//                Bundle bundle = new Bundle();
//                bundle.putInt(KEY_BUSINESS_TYPE, order.orderType);
//                bundle.putInt(KEY_GOODS_TYPE, order.orderGoodsType);
//                bundle.putString(FgOrder.KEY_ORDER_ID, order.orderNo);
//                startFragment(new FgOrder(), bundle);
                FgOrderDetail.Params params = new FgOrderDetail.Params();
                params.orderGoodsType = order.orderGoodsType;
                params.orderId = order.orderNo;
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PARAMS_DATA, params);
                startFragment(new FgOrder(), bundle);
            }
        }
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
