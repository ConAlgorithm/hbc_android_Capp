package com.hugboga.custom.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HbcRecyclerSingleTypeAdpater;
import com.hugboga.custom.data.bean.ChoiceCommentsBean;
import com.hugboga.custom.data.request.RequestChoiceComments;
import com.hugboga.custom.utils.UIUtils;
import com.hugboga.custom.utils.WrapContentLinearLayoutManager;
import com.hugboga.custom.widget.ChoiceCommentView;
import com.hugboga.custom.widget.HbcLoadingMoreFooter;
import com.hugboga.custom.widget.SpaceItemDecoration;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;

/**
 * Created by qingcha on 17/8/3.
 */
public class ChoiceCommentActivity extends BaseActivity implements XRecyclerView.LoadingListener{

    @BindView(R.id.choice_comment_recyclerview)
    XRecyclerView mRecyclerView;
    @BindView(R.id.choice_comment_empty_layout)
    LinearLayout emptyLayout;

    private HbcRecyclerSingleTypeAdpater<ChoiceCommentsBean.ChoiceCommentsItemBean> mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_choice_comment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        initTitleBar();
    }

    public void initTitleBar() {
        initDefaultTitleBar();
        fgTitle.setText(R.string.choice_comment_title);
        fgRightTV.setVisibility(View.GONE);

        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setFootView(new HbcLoadingMoreFooter(this));
        mRecyclerView.setLoadingListener(this);
        SpaceItemDecoration itemDecoration = new SpaceItemDecoration();
        itemDecoration.setItemOffsets(0, UIUtils.dip2px(10), 0, 0, LinearLayout.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mAdapter = new HbcRecyclerSingleTypeAdpater(this, ChoiceCommentView.class);
        mRecyclerView.setAdapter(mAdapter);

        requestChoiceComments(0, true);
    }

    public void requestChoiceComments(int offset, boolean isShowLoading) {
        RequestChoiceComments requestChoiceComments = new RequestChoiceComments(this, offset);
        requestData(requestChoiceComments, isShowLoading);
    }

    @Override
    public void onLoadMore() {
        requestChoiceComments(mAdapter.getListCount(), false);
    }

    @Override
    public void onRefresh() {
        requestChoiceComments(0, false);
    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestChoiceComments) {
            ChoiceCommentsBean choiceCommentsBean = ((RequestChoiceComments) _request).getData();
            emptyLayout.setVisibility(View.GONE);
            int offset = _request.getOffset();
            mAdapter.addData(choiceCommentsBean.listData, offset > 0);
            if (offset == 0) {
                mRecyclerView.smoothScrollToPosition(0);
            }
            mRecyclerView.refreshComplete();
            mRecyclerView.setNoMore(mAdapter.getListCount() >= choiceCommentsBean.totalSize);
        }
    }

    @Override
    public void onDataRequestError(ExceptionInfo errorInfo, BaseRequest request) {
        super.onDataRequestError(errorInfo, request);
        emptyLayout.setVisibility(View.VISIBLE);
        emptyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestChoiceComments(0, true);
            }
        });
    }

    @Override
    public String getEventSource() {
        return "游客说";
    }
}
