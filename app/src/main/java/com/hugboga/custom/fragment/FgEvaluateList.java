package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.EvaluateListAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.CommentsListData;
import com.hugboga.custom.data.bean.EvaluateItemData;
import com.hugboga.custom.data.request.RequestCommentsList;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by qingcha on 16/6/18.
 */
@ContentView(R.layout.fg_evaluate_list)
public class FgEvaluateList extends BaseFragment{

    @ViewInject(R.id.evaluate_list_listview)
    ZListView listView;

    private String guideId;
    private String listCount;
    private EvaluateListAdapter adapter;

    public static FgEvaluateList newInstance(String _guideId, String _listCount) {
        FgEvaluateList fragment = new FgEvaluateList();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PARAMS_ID, _guideId);
        bundle.putString(Constants.PARAMS_DATA, _listCount);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            guideId = savedInstanceState.getString(Constants.PARAMS_ID);
            listCount = savedInstanceState.getString(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                guideId = bundle.getString(Constants.PARAMS_ID);
                listCount = bundle.getString(Constants.PARAMS_DATA);
            }
        }
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
        return requestData(new RequestCommentsList(getActivity(), guideId, pageIndex));
    }

    @Override
    protected void initHeader() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        fgTitle.setLayoutParams(params);
        fgTitle.setText(getString(R.string.evaluate_list_title, listCount));
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);
    }

    ZListView.OnRefreshListener onRefreshListener = new ZListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (adapter != null) {
                adapter = null;
            }
            loadData(0);
        }
    };

    ZListView.OnLoadListener onLoadListener = new ZListView.OnLoadListener() {
        @Override
        public void onLoad() {
            if (adapter.getCount() > 0) {
                loadData(adapter == null ? 0 : adapter.getCount());
            }
        }
    };

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        if (adapter != null) {
            adapter = null;
        }
        return loadData(0);
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {

        if (_request instanceof RequestCommentsList) {
            RequestCommentsList request = (RequestCommentsList) _request;
            CommentsListData data = request.getData();
            fgTitle.setText(getString(R.string.evaluate_list_title, data.getListCount()));
            List<EvaluateItemData> list = data.getListData();
            if (list != null) {
                if (adapter == null) {
                    adapter = new EvaluateListAdapter(getActivity());
                    listView.setAdapter(adapter);
                    adapter.setList(list);
                } else {
                    adapter.addList(list);
                }
            }
            if (list != null && list.size() < Constants.DEFAULT_PAGESIZE) {
                listView.onLoadCompleteNone();
            } else {
                listView.onLoadComplete();
            }
            listView.onRefreshComplete();
        }
    }
}
