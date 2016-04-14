package com.hugboga.custom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.huangbaoche.hbcframe.adapter.ZBaseAdapter;
import com.huangbaoche.hbcframe.data.net.ExceptionErrorCode;
import com.huangbaoche.hbcframe.data.net.ExceptionInfo;
import com.huangbaoche.hbcframe.data.request.BaseRequest;
import com.huangbaoche.hbcframe.util.MLog;
import com.huangbaoche.hbcframe.widget.recycler.ZDefaultDivider;
import com.huangbaoche.hbcframe.widget.recycler.ZListPageView;
import com.huangbaoche.hbcframe.widget.recycler.ZSwipeRefreshLayout;
import com.hugboga.custom.MainActivity;
import com.hugboga.custom.R;
import com.hugboga.custom.activity.OrderSelectCityActivity;
import com.hugboga.custom.adapter.HomeAdapter;
import com.hugboga.custom.constants.Constants;
import com.hugboga.custom.data.bean.HomeBean;
import com.hugboga.custom.data.request.RequestHome;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * 首页
 * Created by admin on 2016/3/1.
 */
@ContentView(R.layout.fg_home)
public class FgHome extends BaseFragment implements View.OnClickListener, ZBaseAdapter.OnItemClickListener, ZListPageView.NoticeViewTask {

    public static final String FILTER_FLUSH = "com.hugboga.custom.home.flush";

    @ViewInject(R.id.listview)
    ZListPageView recyclerView;
    @ViewInject(R.id.swipe)
    ZSwipeRefreshLayout swipeRefreshLayout;

    View emptyView;
    View header;

    HomeAdapter adapter;
    @Override
    protected void initHeader() {
        header = View.inflate(getActivity(), R.layout.fg_home_header, null);
        header.findViewById(R.id.fg_home_menu1).setOnClickListener(this);
        header.findViewById(R.id.fg_home_menu2).setOnClickListener(this);
        header.findViewById(R.id.fg_home_menu3).setOnClickListener(this);
        emptyView = header.findViewById(R.id.header_empty);
        emptyView.findViewById(R.id.home_empty_refresh).setOnClickListener(this);
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
        adapter = new HomeAdapter(getActivity(), this,header);
        recyclerView.setAdapter(adapter);
        recyclerView.setzSwipeRefreshLayout(swipeRefreshLayout);
        RequestHome requestHome = new RequestHome(getActivity());
        recyclerView.setRequestData(requestHome);
        recyclerView.setOnItemClickListener(this);
        recyclerView.setNoticeViewTask(this);
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
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.header_left_btn:
                ((MainActivity) getActivity()).openDrawer();
                break;
            case R.id.header_right_btn:
                bundle.putInt(KEY_BUSINESS_TYPE, Constants.BUSINESS_TYPE_HOME);
                startFragment(new FgChooseCity(), bundle);
                break;
            case R.id.fg_home_menu1://中文接送机
                FgTransfer  fgTransfer = new FgTransfer();
                bundle.putString("umeng_from","首页");
                fgTransfer.setArguments(bundle);
                startFragment(fgTransfer);
                break;
            case R.id.fg_home_menu2://按天包车
                startActivity(new Intent(this.getActivity(), OrderSelectCityActivity.class));
//                startFragment(new FgDaily());
                break;
            case R.id.fg_home_menu3://单次接送
                startFragment(new FgSingle());
                break;
            case R.id.home_empty_refresh://单次接送
                recyclerView.showPageFirst();
                break;
        }
    }

    private  void doUmengClickEvent(){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("source","首页");
        MobclickAgent.onEvent(this.getActivity(), "click_city", map);
    }

    @Override
    public void onItemClick(View view, int position) {
        HomeBean homeBean = adapter.getDatas().get(position);
        FgSkuList fg = new FgSkuList();
        Bundle bundle = new Bundle();
        bundle.putString(FgSkuList.KEY_CITY_ID, homeBean.cityId);
        startFragment(fg, bundle);
        doUmengClickEvent();
    }

    @Override
    public void notice(Object object) {
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void error(ExceptionInfo errorInfo, BaseRequest request) {
        MLog.e("errorInfo.state = "+errorInfo.state);
        if(errorInfo.state== ExceptionErrorCode.ERROR_CODE_NET_UNAVAILABLE){
            emptyView.setVisibility(View.VISIBLE);
            needHttpRequest = true;
            MLog.e("emptyView.state = "+emptyView.getVisibility());
        }else{
            super.onDataRequestError(errorInfo, request);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this.getActivity(),"launch_discovery");

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
