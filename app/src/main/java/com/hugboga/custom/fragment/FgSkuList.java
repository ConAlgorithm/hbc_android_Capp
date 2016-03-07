package com.hugboga.custom.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeAdapter;
import com.hugboga.custom.adapter.SkuAdapter;
import com.hugboga.custom.data.bean.CityBean;
import com.hugboga.custom.data.request.RequestSkuList;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 城市SKU列表
 * Created by admin on 2016/3/3.
 */
@ContentView(R.layout.fg_sku_list)
public class FgSkuList extends  BaseFragment {

    public static final String KEY_CITY_ID = "KEY_CITY_ID";

    @ViewInject(android.R.id.list)
    ListView listView;


    protected String mCityId;

    private SkuAdapter adapter;


    @Override
    protected void initHeader() {
        fgTitle.setText("测试");
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.fg_sku_header, null);
        listView.addHeaderView(header);
    }

    @Override
    protected void initView() {
        adapter = new SkuAdapter(getActivity());
        listView.setAdapter(adapter);
    }

    @Override
    protected Callback.Cancelable requestData() {
        mCityId = getArguments().getString(KEY_CITY_ID);
        RequestSkuList requestSkuList = new RequestSkuList(getActivity(),mCityId);
        return requestData(requestSkuList);
    }


    @Override
    public void onDataRequestSucceed(BaseRequest request) {
        if(request instanceof RequestSkuList){
            RequestSkuList requestSkuList = (RequestSkuList)request;
            adapter.setList(requestSkuList.getData().goodsList);
            MLog.e("onDataRequestSucceed ");
        }
    }

    @Override
    protected void inflateContent() {

    }

    private CityBean findCityById(String id){

        return null;
    }

}
