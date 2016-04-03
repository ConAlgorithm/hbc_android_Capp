package com.hugboga.custom.fragment;

import android.os.Bundle;
import android.view.View;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.ZDefaultDivider;
import com.huangbaoche.hbcframe.widget.ZSwipeRefreshLayout;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.adapter.HomeAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.request.RequestHome;
import com.hugboga.custom.widget.recycler.ZListPageView;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 首页
 * Created by admin on 2016/3/1.
 */
@ContentView(R.layout.fg_home)
public class FgHome extends BaseFragment implements View.OnClickListener, ZBaseAdapter.OnItemClickListener {

    public static final String FILTER_FLUSH = "com.hugboga.custom.home.flush";

    @ViewInject(R.id.listview)
    ZListPageView recyclerView;
    @ViewInject(R.id.swipe)
    ZSwipeRefreshLayout swipeRefreshLayout;

    private HomeAdapter adapter;

    @Override
    protected void initHeader() {
        getView().findViewById(R.id.header_left_btn).setOnClickListener(this);
        getView().findViewById(R.id.header_right_btn).setOnClickListener(this);
    }

    @Override
    protected void initView() {
        initListView(); //初始化列表
    }

    @Override
    protected Callback.Cancelable requestData() {
        loadData();
        return null;
    }

    private void initListView() {
        adapter = new HomeAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setzSwipeRefreshLayout(swipeRefreshLayout);
        RequestHome requestHome = new RequestHome(getActivity());
        recyclerView.setRequestData(requestHome);
        recyclerView.setOnItemClickListener(this);
        //设置间距
        ZDefaultDivider zDefaultDivider = recyclerView.getItemDecoration();
        zDefaultDivider.setItemOffsets(0, 2, 0, 2);
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
    protected void inflateContent() {
    }

    @Override
    public void onClick(View v) {
        MLog.e("onClick=" + v);
        switch (v.getId()) {
            case R.id.header_left_btn:
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.header_right_btn:
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_HOME);
                startFragment(new FgChooseCity(), bundle);
                break;
            case R.id.fg_home_menu1://中文接送机
                startFragment(new FgTransfer());
                break;
            case R.id.fg_home_menu2://按天包车
                startFragment(new FgDaily());
                break;
            case R.id.fg_home_menu3://单次接送
                startFragment(new FgSingle());
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        HomeBean homeBean = adapter.getDatas().get(position);
        FgSkuList fg = new FgSkuList();
        Bundle bundle = new Bundle();
        bundle.putString(FgSkuList.KEY_CITY_ID, homeBean.cityId);
        startFragment(fg, bundle);
    }
}
