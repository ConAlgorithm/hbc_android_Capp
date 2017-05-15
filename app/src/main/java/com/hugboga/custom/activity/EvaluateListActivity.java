package com.hugboga.custom.activity;

import android.os.Bundle;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.EvaluateListAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CommentsListData;
import com.hugboga.custom.data.bean.EvaluateItemData;
import com.hugboga.custom.data.request.RequestCommentsList;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;

import java.util.List;

import butterknife.Bind;

/**
 * Created by qingcha on 16/8/4.
 */
public class EvaluateListActivity extends BaseActivity{

    @Bind(R.id.evaluate_list_listview)
    ZListView listView;

    private String guideId;
    private String listCount;
    private EvaluateListAdapter adapter;
    private int totalSize;

    @Override
    public int getContentViewId() {
        return R.layout.fg_evaluate_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            guideId = savedInstanceState.getString(Constants.PARAMS_ID);
            listCount = savedInstanceState.getString(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                guideId = bundle.getString(Constants.PARAMS_ID);
                listCount = bundle.getString(Constants.PARAMS_DATA);
            }
        }

        initDefaultTitleBar();

        initView();
        requestData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (guideId != null) {
            outState.putString(Constants.PARAMS_ID, guideId);
            outState.putString(Constants.PARAMS_DATA, listCount);
        }
    }

    private Callback.Cancelable loadData(int pageIndex) {
        return requestData(new RequestCommentsList(this, guideId, pageIndex));
    }

    private void requestData() {
        if (adapter != null) {
            adapter = null;
        }
        loadData(0);
    }

    private void initView() {
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        fgTitle.setLayoutParams(params);
        fgTitle.setText(getString(R.string.evaluate_list_title, listCount));
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);
    }

    ZListView.OnRefreshListener onRefreshListener = new ZListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (adapter != null) {
                adapter.setList(null);
            }
            loadData(0);
        }
    };

    ZListView.OnLoadListener onLoadListener = new ZListView.OnLoadListener() {
        @Override
        public void onLoad() {
            int count = adapter.getCount();
            if (count > 0 && count < totalSize) {
                loadData(adapter == null ? 0 : adapter.getCount());
            }
        }
    };

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        super.onDataRequestSucceed(_request);
        if (_request instanceof RequestCommentsList) {
            RequestCommentsList request = (RequestCommentsList) _request;
            CommentsListData data = request.getData();
            totalSize = data.getListCount();
            fgTitle.setText(getString(R.string.evaluate_list_title, "" + totalSize));
            List<EvaluateItemData> list = data.getListData();
            if (list != null) {
                if (adapter == null) {
                    adapter = new EvaluateListAdapter(this);
                    listView.setAdapter(adapter);
                    adapter.setList(list);
                } else {
                    adapter.addList(list);
                }
            }
            if (adapter.getCount() >= totalSize) {
                listView.onLoadCompleteNone();
            } else {
                listView.onLoadComplete();
            }
            listView.onRefreshComplete();
        }
    }
}
