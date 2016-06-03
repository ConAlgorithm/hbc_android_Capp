package com.hugboga.custom.fragment;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FgInsureInfoAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.InsureBean;
import com.hugboga.custom.data.bean.InsureResultBean;
import com.hugboga.custom.data.bean.UserEntity;
import com.hugboga.custom.data.request.RequestInsureList;
import com.hugboga.custom.widget.ZListView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by qingcha on 16/6/3.
 */
@ContentView(R.layout.fg_insure_info)
public class FgInsureInfo extends BaseFragment {

    @ViewInject(R.id.insure_info_listview)
    private ZListView listView;

    private FgInsureInfoAdapter adapter;

    @Override
    protected void initHeader() {
        fgTitle.setText(getString(R.string.insure_info_title));
        listView.setonRefreshListener(onRefreshListener);
        listView.setonLoadListener(onLoadListener);
    }

    @Override
    protected void initView() {

    }

    private Callback.Cancelable runData(int pageIndex) {
        RequestInsureList request = new RequestInsureList(this.getActivity(), UserEntity.getUser().getUserId(this.getActivity()), "", pageIndex + "", Constants.DEFAULT_PAGESIZE + "");
        return requestData(request);
    }

    ZListView.OnRefreshListener onRefreshListener = new ZListView.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (adapter != null) {
                adapter = null;
            }
            runData(0);
        }
    };

    ZListView.OnLoadListener onLoadListener = new ZListView.OnLoadListener() {
        @Override
        public void onLoad() {
            if (adapter.getCount() > 0) {
                runData(adapter == null ? 0 : adapter.getCount());
            }
        }
    };

    @Override
    protected Callback.Cancelable requestData() {
        if (adapter != null) {
            adapter = null;
        }
        return runData(0);
    }

    @Override
    protected void inflateContent() {

    }

    @Override
    public void onDataRequestSucceed(BaseRequest _request) {
        if (_request instanceof RequestInsureList) {
            InsureBean bean = (InsureBean) _request.getData();
            List<InsureResultBean> list = bean.resultBean;
            if (list != null) {
                if (adapter == null) {
                    adapter = new FgInsureInfoAdapter(getActivity());
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
