package com.hugboga.custom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.widget.recycler.ZListRecyclerView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerTypeBaseAdpater;
import com.hugboga.custom.adapter.TravelPurposeFormListAdapter;
import com.hugboga.custom.data.bean.TravelPurposeFormBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestTravelPurposeFormList;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.widget.DialogUtil;

import org.xutils.common.Callback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/2.
 */

public class TravelPurposeFormListActivity extends BaseActivity implements HbcRecyclerTypeBaseAdpater.OnItemClickListener{

    @Bind(R.id.header_left_btn)
    ImageView headerLeftBtn;
    @Bind(R.id.header_right_btn)
    ImageView headerRightBtn;
    @Bind(R.id.header_title)
    TextView headerTitle;

    @Bind(R.id.swipe)
    ZSwipeRefreshLayout swipe;
    @Bind(R.id.listview)
    ZListRecyclerView recyclerView;

    TravelPurposeFormListAdapter adapter;
    HeaderAddView headerAddView;
    TravelPurposeFormBean travelPurposeFormBean = null;
    TravelPurposeFormBean.ListData data;
    List<TravelPurposeFormBean.ListData> listData;
    Boolean isFirst = true;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_travel_purpose_form_list);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initView(){
        headerTitle.setText(R.string.travel_purpose_form_list);
        headerRightBtn.setImageResource(R.mipmap.topbar_cs);
        headerRightBtn.setVisibility(View.VISIBLE);
        headerLeftBtn.setVisibility(View.VISIBLE);

        headerAddView = new HeaderAddView(this);
        setRequest(0,true);

        adapter = new TravelPurposeFormListAdapter(this);
        GridLayoutManager manager = new GridLayoutManager(this,2);
        adapter.addHeaderView(headerAddView);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        recyclerView.setBackgroundResource(R.color.default_bg);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRequest(0,false);
                isFirst = true;
            }
        });
        addOnScrollEvent();
    }

    public Callback.Cancelable setRequest(int pageIndex, boolean needShowLoading){
        RequestTravelPurposeFormList requestTravelPurposeFormList = new RequestTravelPurposeFormList(this,
                UserEntity.getUser().getUserId(this), pageIndex+"","10");
        return HttpRequestUtils.request(this,requestTravelPurposeFormList,this,needShowLoading);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if (request instanceof RequestTravelPurposeFormList){
            travelPurposeFormBean = ((RequestTravelPurposeFormList) request).getData();
            listData = travelPurposeFormBean.listDatas;
            adapter.addData(listData, !isFirst);
        }
        swipe.setRefreshing(false);
    }

    @OnClick({R.id.header_left_btn,R.id.header_right_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.header_left_btn:
                finish();
                break;
            case R.id.header_right_btn:
                DialogUtil.getInstance(TravelPurposeFormListActivity.this).showDefaultServiceDialog(TravelPurposeFormListActivity.this,null);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position, Object itemData) {
        if (null == itemData){
            return;
        }
        if (itemData instanceof TravelPurposeFormBean.ListData){
            data = (TravelPurposeFormBean.ListData)itemData;
            Intent intent = new Intent(this,TravelPurposeFormDetail.class);
            Bundle bundle = new Bundle();
            bundle.putString("opUserId", data.opUserId);
            bundle.putInt("id", data.id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void addOnScrollEvent(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (null == travelPurposeFormBean && listData.size() == 0){
                        return;
                    }
                    int lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    int totalCount = listData.size() - 1;
                    if (lastVisibleItem >= totalCount - adapter.getHeadersCount() && adapter.getListCount() < travelPurposeFormBean.listCount ){
                        isFirst = false;
                        setRequest(adapter.getListCount(), false);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    /**
     * headerView
     */
    public class HeaderAddView extends LinearLayout{

        public HeaderAddView(Context context) {
            this(context,null);
        }

        public HeaderAddView(Context context, AttributeSet attrs) {
            this(context, attrs,0);
        }

        public HeaderAddView(final Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            View view = inflate(context,R.layout.item_travel_purpose_form,this);
            ViewGroup.LayoutParams paramsRoot = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(paramsRoot);

            float displayWidth = UIUtils.screenWidth * 260f/750f;
            int displayHeight = (int) (displayWidth*(360f/260f));
            LayoutParams paramsLay = new LayoutParams((int)displayWidth,displayHeight);
            LinearLayout layoutbg = (LinearLayout)view.findViewById(R.id.travel_form_item_bg_layout);
            layoutbg.setLayoutParams(paramsLay);
            layoutbg.setBackground(getResources().getDrawable(R.drawable.shape_rounded_yellow));

            ImageView addBtn = (ImageView)view.findViewById(R.id.add_btn);
            TextView title = (TextView)view.findViewById(R.id.travel_purpose_form_title);
            TextView date = (TextView)view.findViewById(R.id.travel_purpose_form_date);
            title.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
            addBtn.setVisibility(View.VISIBLE);
            addBtn.setImageResource(R.mipmap.wish_add);
            layoutbg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), TravelPurposeFormActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
