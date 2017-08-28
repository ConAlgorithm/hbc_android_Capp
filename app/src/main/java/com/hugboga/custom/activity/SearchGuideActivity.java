package com.hugboga.custom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.net.HttpRequestUtils;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.adapter.HbcRecyclerTypeBaseAdpater;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.SearchGuideBean;
import com.hugboga.custom.data.request.RequestSearchGuide;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.GuideSearchListItem;
import com.hugboga.custom.widget.LineSearchListItem;
import com.hugboga.custom.widget.SearchlineGuideLoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.Bind;

/**
 * Created by zhangqiang on 17/8/24.
 */

public class SearchGuideActivity extends BaseActivity {
    @Bind(R.id.header_left_btn)
    ImageView headerLeft;
    @Bind(R.id.header_right_btn)
    ImageView headerRight;
    @Bind(R.id.header_title)
    TextView title;
    @Bind(R.id.search_guide_recyclerview)
    XRecyclerView mXRecyclerView;
    protected HbcRecyclerSingleTypeAdpater hbcRecyclerSingleTypeAdpater;
    int refreshOrNot = 1;
    String keyword;
    @Override
    public int getContentViewId() {
        return R.layout.search_guide_activity;
    }

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        keyword = getIntent().getStringExtra("keyword");
        initView();
    }

    private void initView(){
        title.setText("相关司导");
        headerLeft.setVisibility(View.VISIBLE);
        headerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerRight.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(this);
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        mXRecyclerView.setLayoutManager(layoutManager);
        SearchlineGuideLoadingMoreFooter travelLoadingMoreFooter = new SearchlineGuideLoadingMoreFooter(this);
        travelLoadingMoreFooter.setCustomlayout(inflater);
        mXRecyclerView.setFootView(travelLoadingMoreFooter);
        hbcRecyclerSingleTypeAdpater = new HbcRecyclerSingleTypeAdpater(this, GuideSearchListItem.class);
        mXRecyclerView.setAdapter(hbcRecyclerSingleTypeAdpater);
        hbcRecyclerSingleTypeAdpater.setOnItemClickListener(new HbcRecyclerTypeBaseAdpater.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object itemData) {
                SearchGuideBean.GuideSearchItemBean guideSearchItemBean = (SearchGuideBean.GuideSearchItemBean)itemData;
                GuideWebDetailActivity.Params params = new GuideWebDetailActivity.Params();
                params.guideId = guideSearchItemBean.guideId;
                Intent intent = new Intent(SearchGuideActivity.this,GuideWebDetailActivity.class);
                //intent.putExtra(Constants.PARAMS_SOURCE, getEventSource());
                intent.putExtra(Constants.PARAMS_DATA, params);
                startActivity(intent);
            }
        });
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshOrNot = 1;
                runData(0,10);
            }

            @Override
            public void onLoadMore() {
                refreshOrNot = 2;
                if (hbcRecyclerSingleTypeAdpater.getListCount() > 0) {
                    runData(hbcRecyclerSingleTypeAdpater == null ? 0 : hbcRecyclerSingleTypeAdpater.getListCount(), 10);
                }
            }
        });
        //首次进入
        runData(0,10);
    }
    private void runData(int offsize,int pageSize){
        if(keyword!= null){
            RequestSearchGuide requestSearchGuide = new RequestSearchGuide(this,keyword,offsize,pageSize);
            HttpRequestUtils.request(this,requestSearchGuide,this,false);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        super.onDataRequestSucceed(request);
        if(request instanceof RequestSearchGuide){
            if (hbcRecyclerSingleTypeAdpater != null) {
                SearchGuideBean searchGuideBean = (SearchGuideBean) request.getData();
                if (request!=null && request.getOffset() == 0) {
                    mXRecyclerView.smoothScrollToPosition(0);
                }
                if(mXRecyclerView!= null && searchGuideBean!= null){
                    for(int i= 0; i<searchGuideBean.resultBean.size();i++){
                        if(keyword!= null){
                            searchGuideBean.resultBean.get(i).keyword = keyword;
                        }
                    }
                    if (hbcRecyclerSingleTypeAdpater != null) {
                        hbcRecyclerSingleTypeAdpater.addData(searchGuideBean.resultBean, request.getOffset() > 0);
                    }
                    if (refreshOrNot == 1) {
                        mXRecyclerView.refreshComplete();
                    } else if (refreshOrNot == 2) {
                        mXRecyclerView.loadMoreComplete();
                    }
                    if (hbcRecyclerSingleTypeAdpater != null) {
                        mXRecyclerView.setNoMore(hbcRecyclerSingleTypeAdpater.getListCount() >= searchGuideBean.totalSize);
                    }
                }
            }
        }
    }

    @Override
    public void onDataRequestCancel(BaseRequest request) {
        super.onDataRequestCancel(request);
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
    }
}
