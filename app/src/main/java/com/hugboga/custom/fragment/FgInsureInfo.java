package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.widget.ListView;

import com.hugboga.custom.R;
import com.hugboga.custom.adapter.FgInsureInfoAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.InsureListBean;
import com.hugboga.custom.data.bean.OrderBean;

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
    private ListView listView;

    private FgInsureInfoAdapter adapter;
    private OrderBean orderBean;

    public static FgInsureInfo newInstance(OrderBean orderBean) {
        FgInsureInfo fragment = new FgInsureInfo();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PARAMS_DATA, orderBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            orderBean = savedInstanceState.getParcelable(Constants.PARAMS_DATA);
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                orderBean = bundle.getParcelable(Constants.PARAMS_DATA);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (orderBean != null) {
            outState.putSerializable(Constants.PARAMS_DATA, orderBean);
        }
    }

    @Override
    protected void initHeader() {
        fgTitle.setText(getString(R.string.insure_info_title));
        List<InsureListBean> list = orderBean.insuranceList;
        if (list != null && list.size() > 0) {
            if (adapter == null) {
                adapter = new FgInsureInfoAdapter(getActivity());
                listView.setAdapter(adapter);
            }
            adapter.setList(list);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected Callback.Cancelable requestData() {
        return null;
    }

    @Override
    protected void inflateContent() {

    }

}
