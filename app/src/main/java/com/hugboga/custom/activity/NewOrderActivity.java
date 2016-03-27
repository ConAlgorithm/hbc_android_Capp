package com.hugboga.custom.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.activity.BaseFragmentActivity;
import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.NewOrderAdapter;
import com.hugboga.custom.data.bean.OrderBean;
import com.hugboga.custom.data.request.RequestOrder;
import com.hugboga.custom.widget.recycler.ZListPageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_new_order)
public class NewOrderActivity extends BaseFragmentActivity implements ZBaseAdapter.OnItemClickListener {

    public static final String SEARCH_TYPE = "search_type";
    public static final String SEARCH_USER = "search_user";

    @ViewInject(R.id.header_title)
    TextView title;
    @ViewInject(R.id.header_left_btn)
    ImageView backBtn;
    @ViewInject(R.id.listview)
    ZListPageView recyclerView;
    @ViewInject(R.id.order_empty)
    RelativeLayout emptyLayout;

    private NewOrderAdapter adapter;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backBtn.setVisibility(View.VISIBLE);
        title.setText(getTitle());
        bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        SearchType searchType = SearchType.getSerachType(bundle.getInt(SEARCH_TYPE, SearchType.SEARCH_TYPE_HISTORY.getType()));
        initTitle(searchType);
        initListView(searchType);
    }

    /**
     * 设置列表数据
     *
     * @param searchType
     */
    private void initListView(SearchType searchType) {
        adapter = new NewOrderAdapter(NewOrderActivity.this);
        recyclerView.setAdapter(adapter);
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
                return new RequestOrder(NewOrderActivity.this, bundle.getString(SEARCH_USER, ""));
            case SEARCH_TYPE_HISTORY:
                return new RequestOrder(NewOrderActivity.this, bundle.getString(SEARCH_USER, ""));
            default:
                return new RequestOrder(NewOrderActivity.this, bundle.getString(SEARCH_USER, ""));
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
                title.setText(getString(R.string.letter_chat_btn));
                break;
            case SEARCH_TYPE_HISTORY:
                title.setText(getString(R.string.letter_chat_btn));
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

    @Event({R.id.header_left_btn})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (view.getParent() == recyclerView) {
            OrderBean order = adapter.getDatas().get(position);
            if (order != null) {
                //TODO 跳转到订单详情
                Snackbar.make(recyclerView, "打开订单详情界面", Snackbar.LENGTH_SHORT).show();
//                Intent intent = new Intent(NewOrderActivity.this, WorkOrderInfoActivity.class);
//                intent.putExtra(OrderInfoActivity.ORDER_NO, order.getOrderNo());
//                intent.putExtra(OrderInfoActivity.ORDER_TYPE, order.getOrderType().getCode());
//                startActivity(intent);
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
